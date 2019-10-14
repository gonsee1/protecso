/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.core2;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author crodas
 */
public class ListaRecibo {
    private List<Recibo> recibos=new ArrayList<Recibo>();
    private Factura factura=null;
    private ZipOutputStream zout=null;
    private Double saldo=0d;
    public void savePdf(HttpServletResponse response){
        if (response!=null){
            response.setHeader("Content-Type", "application/zip");
            response.addHeader("Content-Disposition", "attachment; filename=\"pdfs.zip\"" ); 
            try {
                zout = new ZipOutputStream(response.getOutputStream());
                for(Recibo recibo : recibos){
                    OutputStream outStream=new ByteArrayOutputStream (); 
                    recibo.save(outStream);
                    ByteArrayOutputStream outStreamByte=(ByteArrayOutputStream) outStream;  
                    String nombre=recibo.getNombre(); 
                    ZipEntry ze = new ZipEntry(nombre+".pdf");
                    zout.putNextEntry(ze);
                    zout.write(outStreamByte.toByteArray());
                    zout.closeEntry();  
                }                
                String msj="";
                msj+="Saldo Pendiente : "+this.saldo+" \r\n";
                ZipEntry ze = new ZipEntry("informacion.txt");
                zout.putNextEntry(ze);
                zout.write(msj.getBytes("UTF-8"));
                zout.closeEntry(); 
            } catch (Exception e) {
                e.printStackTrace();
            }            
        }else{
            String pathBase=ListaRecibo.getPathBaseRecibos();
            for(Recibo recibo : recibos){
                String nombre=recibo.getNombre(); 
                File file=new File(pathBase+nombre+".pdf");
                FileOutputStream fos;
                try {
                    fos = new FileOutputStream(file);
                    recibo.save(fos);
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                }                   
            }            
        }
    } 

    public void setSaldo(Double saldo) {
        this.saldo = saldo;
    }

    public void closeZip(){
        try {
            if (zout!=null)
                zout.close();
        } catch (Exception e) {
            e.printStackTrace();
        }        
    }
    public List<Recibo> getRecibos() {
        return recibos;
    }

    public Factura getFactura() {
        return factura;
    }   

    public ZipOutputStream getZout() {
        return zout;
    }

    public void setFactura(Factura factura) {
        this.factura = factura;
    }
    
    public Boolean isGeneraFacturaDetraccion(){
        for(Recibo rec : this.recibos){
            if (rec.isGeneraFacturaDetraccion())
                return true;
        }
        return false;
    }
    
    
    public static boolean isValidPathBaseRecibo(String ruta){
        File f=new File(ruta);
        try {
            return (f.exists() && f.isDirectory());
        } catch (Exception e) {}
        return false;        
    }     
    public static String getPathBaseRecibos(){
//        String rutas[]=new String[]{"/home/crodas/facturas_satelital/","C:\\facturas_satelital\\","/sistemas/satelital/input"};
        String rutas[]=new String[]{"/sistemas/satelital/input/"};
        for (String ruta : rutas){
            if (ListaRecibo.isValidPathBaseRecibo(ruta))
                return ruta;
        }
        return null;
    }    

    void savePdfFactura() {
        if (this.factura!=null)
            this.factura.savePdf();
    }
}

