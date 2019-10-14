/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.core3;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.americatel.facturacion.models.CIER_DATA_PLAN;
import com.americatel.facturacion.models.CIER_DATA_SERV_SUPL;
import com.americatel.facturacion.models.CIER_DATA_SUCU;

/**
 *
 * @author crodas
 */
public class ListaRecibo {
    final static Logger logger = Logger.getLogger(ListaRecibo.class);
    private List<Recibo> recibos=new ArrayList<Recibo>();
    private List<Factura> facturas=new ArrayList<Factura>();
    private ProcesoNegocio proceso_negocio=null;
    private ZipOutputStream zout=null;
    public ListaRecibo(ProcesoNegocio proceso_negocio ){
        this.proceso_negocio=proceso_negocio;
    }
    public void addRecibo(Recibo r){
        recibos.add(r);
    }
    public void addFactura(Factura f){
    	facturas.add(f);
    }
    public void savePdf(HttpServletResponse response){
//        this.crearPdf();
        if (response!=null){
            response.setHeader("Content-Type", "application/zip");
            response.addHeader("Content-Disposition", "attachment; filename=\"pdfs.zip\"" ); 
            try {
                zout = new ZipOutputStream(response.getOutputStream());
                int i=0;
//                Factura factura=this.proceso_negocio.getFactura();
                for(Recibo recibo : recibos){
                    if (recibo.generaPdf()){
                        ReciboPdf recibo_pdf=recibo.getReciboPdf();
                        recibo.getReciboPdf().crearPdf();
                        i++;
                        OutputStream outStream=new ByteArrayOutputStream (); 
                        recibo_pdf.savePdf(outStream);
                        ByteArrayOutputStream outStreamByte=(ByteArrayOutputStream) outStream;  
                        
                        String nombrePdf = "";
                        if(recibo.isIsBoleta()){
                            nombrePdf = recibo.getNombreBoleta();
                        }else{
                            nombrePdf = recibo.getNombreRecibo();
                        }
                                                
                        ZipEntry ze = new ZipEntry(nombrePdf+".pdf");
                        zout.putNextEntry(ze);
                        zout.write(outStreamByte.toByteArray());
                        zout.closeEntry();
                    }
                } 
                for(Factura factura : facturas){
	                if (factura.getGeneraFactura()){
	                    FacturaPdf factura_pdf=factura.getFacturaPdf();
	                    factura_pdf.crearPdf();
	                    OutputStream outStream=new ByteArrayOutputStream (); 
	                    factura_pdf.savePdf(outStream);
	                    ByteArrayOutputStream outStreamByte=(ByteArrayOutputStream) outStream;  
	                    ZipEntry ze = new ZipEntry(factura.getNombreFactura()+".pdf");
	                    zout.putNextEntry(ze);
	                    zout.write(outStreamByte.toByteArray());
	                    zout.closeEntry();                    
	                }  
                }
                String msj="";
                msj+="Saldo Pendiente : "+this.proceso_negocio.getSaldo()+" \r\n";
                msj+="Saldo Aproximado : "+this.proceso_negocio.getTotalSaldoAproximado()+" \r\n";
                ZipEntry ze = new ZipEntry("informacion.txt");
                zout.putNextEntry(ze);
                zout.write(msj.getBytes("UTF-8"));
                zout.closeEntry(); 
                
                zout.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
//    public void crearPdf(){
//        for (Recibo r : recibos){
//            r.getReciboPdf().crearPdf();
//        }
//    }
    public List<Recibo> getRecibos() {
        return recibos;
    }
    public List<Factura> getfacturas() {
        return facturas;
    }
    public int getNumeroRecibosPdfs(){
        int r=0;
        for(Recibo p : this.recibos){
            if (p.generaPdf()){
                r++;
            }
        }
        return r;
    }
    
    public Boolean generaFactura(){
        for(Recibo r :this.recibos){
            if (r.generaFactura()) return true;
        }
        return false;
    }

    void saveBD() {
        logger.info("*******************************************************************************");
        logger.info("GUARDAR EN BASE DE DATOS");
        logger.info("*******************************************************************************");
        
//        log.log(Level.INFO, "this.proceso_negocio.esPreview() :"+ this.proceso_negocio.esPreview());
        
        if(!this.proceso_negocio.esPreview()){
        	
    //modificado par prueba     	
        	
       
            for(Recibo r : this.recibos){
                boolean generaRecibo = r.generaPdf();
                logger.info("Genera Recibo :"+generaRecibo);
                if (generaRecibo)
                    r.saveBD();
                else{
                    //Todos lo detalles de devoluciones se guardan
                    for(Map.Entry<CIER_DATA_SUCU,Object[]> entry : r.mi_data_facturar.entrySet()){
                        List<CIER_DATA_PLAN> planes=(List<CIER_DATA_PLAN>) entry.getValue()[0];
                        List<CIER_DATA_SERV_SUPL> sss=(List<CIER_DATA_SERV_SUPL>) entry.getValue()[1]; 
                        CIER_DATA_SUCU key=entry.getKey();
                        if (key.getCO_CIER_DATA_SUCU()==null){
                            key.setCO_CIER_DATA_NEGO(this.proceso_negocio.getCier_Data_Nego().getCO_CIER_DATA_NEGO());
                            ProcesoNegocio.mapCier_Data_Sucu.insertar(key);
                        }
                        for(CIER_DATA_PLAN plan : planes){ 
                            if (plan.getST_TIPO_DEVO()!=null){
                                plan.setCO_CIER_DATA_SUCU(key.getCO_CIER_DATA_SUCU());
                                if (plan.getCO_FACT() != null || plan.getCO_BOLE()!= null || plan.getCO_RECI()!=null){
                                	ProcesoNegocio.mapCier_Data_Plan.insertar(plan);
                                }
                            }
                        }
                        for(CIER_DATA_SERV_SUPL ss : sss){  
                            if (ss.getST_TIPO_DEVO()!=null){
                                ss.setCO_CIER_DATA_SUCU(key.getCO_CIER_DATA_SUCU());
                                ProcesoNegocio.mapCier_Data_Serv_Supl.insertar(ss);
                            }
                        }
                    }                   
                   
                }
            }
            
       
        	
            logger.info("NUMERO DE FACTURAS A PROCESAR :"+this.facturas.size());
            for(Factura f : this.facturas){
            //Factura f=this.proceso_negocio.getFactura();
            	logger.info("NUMERO FACTURA -ListaRecibo :"+f.getNumeroFactura());
	            if (f!=null){
	                f.saveBD();
	            }
            }
            
          
            
        }
    }

    void savePDF(String ruta) {
        logger.info("*******************************************************************************");
        logger.info("GENERA PDF");
        logger.info("*******************************************************************************");
        if(!this.proceso_negocio.esPreview()){
            for(Recibo r : this.recibos){
                if(r.getNombreRecibo() == "1-5193533-2018-1")
                {
                	logger.info("Ingreso Generacion Recibo :");
                	
                }
                boolean generaRecibo = r.generaPdf();
                logger.info("Genera Recibo :" + generaRecibo);
                if (generaRecibo){                    
                    ReciboPdf pdf=r.getReciboPdf();
                    pdf.crearPdf();
                    File f;
                    if(r.getNumeroRecibo() == null){
                    	f=new File(ruta+"recibos/"+r.getNombreBoleta()+".pdf");
                    }else{
                    	f=new File(ruta+"recibos/"+r.getNombreRecibo()+".pdf");
                    }

                    FileOutputStream fos;
                    
                    try {
                        fos = new FileOutputStream(f);
                        pdf.savePdf(fos);
                        fos.close();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }                    
            }
//            Factura f=this.proceso_negocio.getFactura();
            for(Factura f : this.facturas){
	            if (f!=null){
	                FacturaPdf pdf=f.getFacturaPdf();
	                pdf.crearPdf();
	                File i=new File(ruta+"facturas/"+f.getNombreFactura()+".pdf");
	                FileOutputStream fos;  
	                try {
	                    fos = new FileOutputStream(i);
	                    pdf.savePdf(fos);
	                    fos.close();
	                }catch(Exception e){
	                    e.printStackTrace();
	                }
	                    
	            }
            }
        }
    }
    
    public void guardarFacturaRecibo(Long codProdPadre){
        
    }
    
}
