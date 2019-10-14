/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.core3;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.americatel.facturacion.Utils.Constante;
import com.americatel.facturacion.historial.Historial;
import com.americatel.facturacion.models.CIER;
import com.americatel.facturacion.models.NEGO;
import com.americatel.facturacion.models.PROD;

/**
 *
 * @author crodas
 */
public class Main implements Runnable {
    Date FE_EMIS=null;
    Date FE_VENC=null;
    Long DE_RAIZ_RECI=null;
    Long DE_RAIZ_FACT=null;
    Long DE_RAIZ_BOLE=null;
    Long semilla_factura=null;
    Long semilla_recibo=null;
    Long semilla_boleta=null;
    Double NU_TIPO_CAMB = null;
    List<PROD> productos=null;
    Integer COD_PROD_PADRE = null;
//    private static Logger log = Logger.getLogger(Main.class.getName());
    final static Logger logger = Logger.getLogger(Main.class);

    public Main(Integer COD_PROD_PADRE,List<PROD> productos,Date FE_EMIS,Date FE_VENC,Long DE_RAIZ_RECI,Long DE_RAIZ_FACT,Double NU_TIPO_CAMB, Long DE_RAIZ_BOLE) {
        //Corre el cierre de todos los productos
        this.FE_EMIS=FE_EMIS;
        this.FE_VENC=FE_VENC;
        this.DE_RAIZ_RECI=DE_RAIZ_RECI;
        this.DE_RAIZ_FACT=DE_RAIZ_FACT;
        this.DE_RAIZ_BOLE=DE_RAIZ_BOLE;
        this.semilla_recibo=this.DE_RAIZ_RECI;
        this.semilla_factura=this.DE_RAIZ_FACT;
        this.semilla_boleta = this.DE_RAIZ_BOLE;
        this.NU_TIPO_CAMB = NU_TIPO_CAMB;
        this.productos=productos;
        this.COD_PROD_PADRE = COD_PROD_PADRE;
    }      

    public void run() {
        logger.info("Inicia proceso de cierre de ejecucion");
        List<CIER> ciers=new ArrayList<CIER>();
        List<PROD> prods=new ArrayList<PROD>();
        Boolean con_error=false;
        for (PROD producto : productos){
            CIER cier=producto.getCierrePendiente();
            logger.info("Cierre : "+cier.getCO_CIER_ForJSON()+" Para el Servicio: "+ producto.getNO_PROD_ForJSON());
            if (cier!=null){
                Integer st=cier.getST_CIER_ForJSON();
                if (st==Constante.ESTADO_CIERRE_PENDIENTE || st==Constante.ESTADO_CIERRE_TERMINADO){//pendiente o termino cierre
                    ciers.add(cier);
                    prods.add(producto);
//                    log.log(Level.INFO, "Cierre Pendiente o Terminado - True");
                }else{
//                    log.log(Level.INFO, "Cierre Pendiente o Terminado - False");
                    con_error=true;
                }
            }
        }
//        log.log(Level.INFO, "Existen cierres en proceso : "+con_error );
        int num=1;
        if (!con_error){
        	
            for (CIER cier : ciers)
                /*CAMBIA EL ESTADO DEL CIERRE A (EN PROCESO), ACTUALIZA LA FECHA Y USUARIO DE MODIFICACION, 
                ELIMINA TODAS LOS REGISTROS GENERADOS POR UN CIERRE ANTERIOR */
                cier.EnCierre(null);
            
            
            for (CIER cier : ciers){ 
            	
           //probar el cierre 72
          // 	if(num!=1){
            		
                cier.setDE_RAIZ_RECI(DE_RAIZ_RECI);
                cier.setDE_RAIZ_FACT(DE_RAIZ_FACT);
                cier.setDE_RAIZ_BOLE(DE_RAIZ_BOLE);
                cier.setFE_EMIS(FE_EMIS);
                cier.setFE_VENC(FE_VENC);
                cier.setNU_TIPO_CAMB(NU_TIPO_CAMB);
                Historial.initModificar(null, cier);
                cier.updateLanzar(); 
                
                this.lanzar(cier);
         // }
         //  	num+=1;	
            //for (CIER cier : ciers){
            //    cier.TerminoCierre(null); 
            //}
            	
            } //fin de prueba	
            
        }     
//        log.log(Level.INFO, "FIN HILO DE EJECUCION DE CIERRE" );
        logger.info("Finaliza proceso de cierre de ejecucion");
    }
    
    private void lanzar(CIER cier){
        PROD prod=cier.getProd_fromMapper();
        List<NEGO> negocios=prod.getNegociosParaProcesoCierre();
        String base=this.getPathBaseRecibos();  
        //prueba
        String prodPadre = getNoPadre(COD_PROD_PADRE);
        base = base + prodPadre + "/";
        logger.info(base);
        logger.info("*******************************************************************************");
        logger.info("Periodo del cierre " +cier.getNU_PERI_ForJSON()+"/"+cier.getNU_ANO_ForJSON());
        logger.info("Servicio :" +prod.getCO_PROD_ForJSON()+" - "+prod.getNO_PROD_ForJSON());
        logger.info("cantidad de negocios correspondiente al servicio :" +negocios.size());
        logger.info("*******************************************************************************");
        
        for(NEGO negocio : negocios){  
        	
        	//prueba negocio 5191127
        	//if(negocio.getCO_NEGO_ForJSON() == 5195127){
  
            ProcesoNegocio p=new ProcesoNegocio(negocio,prod,cier);         
            logger.info("CMC DOCUMENTOS A PROCESAR :" +p.getNumeroRecibosPdfs());
   
            p.setLog_view(true);
            p.setSemilla_factura(semilla_factura);
            p.setSemilla_recibo(semilla_recibo);
            p.setSemilla_boleta(semilla_boleta);
            p.facturar(negocio);     
            logger.info("CMC DOCUMENTOS A PROCESAR :" +p.getLista_Recibo());
            p.saveBD();
            p.savePDF(base);
            semilla_factura=p.getSemilla_factura_sgte();
            semilla_recibo=p.getSemilla_recibo_sgte();
            semilla_boleta=p.getSemilla_boleta_sgte();
        
        
        	//}//fin de prueba
        
        
        }  
        cier.TerminoCierre(null); 
        
        
       
        
        
    }

    
    public String getNoPadre(int CO_PROD_PADRE){
    	String nombre_padre = null;
    	
    	switch(CO_PROD_PADRE) {
    	  case 1:
    		nombre_padre = "Satelital";
    	    break;
    	  case 2:
    		nombre_padre = "ID_AD_EMPRESAS";
    	    break;
    	  case 3:
    		nombre_padre = "MIGRACION_INGENIO";
      	    break;
    	  case 4:
    		nombre_padre = "TI";
      	    break;
    	  case 5:
    		nombre_padre = "ID_AD_MAYORISTA";
        	break;
    	}
    	
    	
    	return nombre_padre;
    }
    
    
    public String getPathBaseRecibos(){
//        String rutas[]=new String[]{"/home/cmarinos/facturas_satelital/","C:\\facturas_satelital\\"};
        String rutas[]=new String[]{"/home/crodas/facturas_satelital/"};
        for (String ruta : rutas){
            if (this.isValidPathBaseRecibo(ruta))
                return ruta;
        }
        return null;
    } 
    public boolean isValidPathBaseRecibo(String ruta){
        File f=new File(ruta);
        try {
            return (f.exists() && f.isDirectory());
        } catch (Exception e) {}
        return false;        
    }       
}
