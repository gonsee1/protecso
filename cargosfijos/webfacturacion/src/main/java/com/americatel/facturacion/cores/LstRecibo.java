/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.cores;

import com.americatel.facturacion.models.CIER;
import java.io.ByteArrayOutputStream;
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
public class LstRecibo {
    private List<Recibo> Lst;
    public LstRecibo(){
        this.Lst=new ArrayList<Recibo>();        
    }
    public Recibo getReciboByCodigoCierreRecibo(Recibo f){
        for(Recibo ft:this.Lst){            
            if (ft.getCodigoCierreRecibo().equals(f.getCodigoCierreRecibo()))
               return ft;
        }        
        return null;        
    }
    
    public void add(Recibo f){
        this.Lst.add(f); 
    }
    
    public void unir(Recibo f){
        //permite agregar una factura validando que si existe agrega solo las glosas
        Recibo existe=this.getReciboByCodigoCierreRecibo(f);
        if(existe==null){
            this.Lst.add(f);
        }else{
            //existe
            existe.unir(f);
        }
    }
    
    public void unir(LstRecibo l){
        for(Recibo f:l.Lst)
            this.unir(f);
    }
    public int size(){
        return this.Lst.size();
    }
    public Recibo get(int i){
        return this.Lst.get(i);
    }
    
 

           
    /*
public void save(){
        String pathBase=this.getPathBaseRecibos();
        if (pathBase!=null){
            for(Recibo recibo :this.Lst){
                ReciboPdf rp=new ReciboPdf(recibo);
                File file=new File(pathBase+recibo.getNombreFileReciboPdf()+".pdf");
                FileOutputStream fos;
                try {
                    fos = new FileOutputStream(file);
                    rp.createPDF();
                    rp.save(fos);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(LstRecibo.class.getName()).log(Level.SEVERE, null, ex);
                }  
                recibo.saveBD();
            }
            
            if (this.isAlgunReciboDetraccion()){                
                FacturaPdf fp=new FacturaPdf(this);
                File file=new File(pathBase+this.getNombreFileFacturaPdf()+".pdf");
                FileOutputStream fos;
                try {
                    fos = new FileOutputStream(file);
                    fp.createPDF();
                    fp.save(fos);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(LstRecibo.class.getName()).log(Level.SEVERE, null, ex);
                }  
                recibo.saveBD();                
            }
        }
    }    
    */    
    


    


    public List<Recibo> getLst() {
        return Lst;
    }
    
    
}
