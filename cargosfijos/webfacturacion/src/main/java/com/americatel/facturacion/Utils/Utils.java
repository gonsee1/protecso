package com.americatel.facturacion.Utils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.americatel.facturacion.mappers.mapPLAN;
import com.americatel.facturacion.models.PLAN;
import com.americatel.facturacion.models.REGLAS_NOMBRE_PLAN;
import com.americatel.facturacion.reportes.ReporteGerenteSunat;


@Component  
public class Utils {
	
	public static mapPLAN mapPlan;
	
	@Autowired
    public void setmapPlan(mapPLAN mapPlan){
       ReporteGerenteSunat.mapPlan = mapPlan;
    }
	
	public String getCodServicio(Integer cod_ProductoParam,String codPlanParam){
	    
        int  relacionadoTI = -1;
        int  aplica_velocidad = -1;
        int  arrendamiento = -1;
        int  cod_prod = -1; 
        int  cod_servicio = -1;        
        String nombrePlan = "";
        String productoCarga ="";
        
            int codPlan =  Integer.parseInt(codPlanParam); 
        
            PLAN objPlanBean  = new PLAN();  
            objPlanBean = mapPlan.getId(codPlan);
            
            if(objPlanBean != null){

               nombrePlan = objPlanBean.getNO_PLAN_ForJSON();
                                
               if(objPlanBean.getRELACIONADO_TI_ForJSON()== true){
                    relacionadoTI = 1;
               }else{
                    relacionadoTI = 0;
               }               
               String   velo_bajada   = objPlanBean.getDE_VELO_BAJA_ForJSON();
               String   velo_subida   = objPlanBean.getDE_VELO_SUBI_ForJSON();
                             
               if(velo_bajada.length() > 0 || velo_subida.length()>0){
                    aplica_velocidad = 1;
               }else{
                    aplica_velocidad = 0;
               }
               
               if(objPlanBean.getST_BACKUP_BU_ForJSON()== true){
                    arrendamiento = 1;
               }else{
                    arrendamiento = 0;
               }               
               cod_servicio  = objPlanBean.getCO_PROD_ForJSON();
               
               cod_prod  = cod_ProductoParam;                                     
            
                if(cod_ProductoParam == 1){

                    if(arrendamiento == 1){                    
                      if(cod_servicio == 1){
                        productoCarga="IPB SAT - BU";
                      }else{
                        productoCarga="ADD SAT - BU";
                      }  
                    }else {                
                      if(cod_servicio == 1){
                        productoCarga="IPB - SAT";
                      }else{
                        productoCarga="ADD - SAT";
                      }                       
                    }

                }else if(cod_ProductoParam == 2){            
                    if(aplica_velocidad == 0){
                        productoCarga="ADD - FO";
                    }else{
                      if(relacionadoTI == 1){
                         if(cod_servicio == 3){
                          productoCarga="IPB (ICX-TI)";
                         }else{
                          productoCarga="ADD (ICX-TI)";
                         }                      
                      }else{
                         if(cod_servicio == 3){
                          productoCarga="IPB";
                         }else{
                          productoCarga="ADD";
                         }  
                      }                                        
                    }            
                }else if(cod_ProductoParam == 3){
                    if(cod_servicio == 5){
                     productoCarga="IPB SAT - INGENIO";
                    }else{
                    productoCarga="ADD SAT -INGENIO";
                    }
                    
                }else if(cod_ProductoParam == 4){
                    
                    if(cod_servicio == 7){
                      productoCarga="HOU";
                    }else if(cod_servicio == 8){
                      productoCarga="IAAS";
                    }else if(cod_servicio == 9){
                      productoCarga="SAAS - CB";
                    }else if(cod_servicio == 10){
                      productoCarga="SAAS - SAS";
                    }else if(cod_servicio == 11){
                      productoCarga="HOU - ONE SHOT";
                    }
                }else if(cod_ProductoParam == 5){
                 
                    if(cod_servicio == 12){
                    productoCarga="IPB - WHO";
                    }else if(cod_servicio == 13){
                        if(aplica_velocidad == 1){
                         productoCarga="ADD - WHO";
                        }else{
                         productoCarga="ADD - WHO(FO)";
                        }                                           
                    }else if(cod_servicio == 15){
                     productoCarga="COL - WHO";
                    }
                }
            
            }  
           
            if(productoCarga.length()==0){
                
                List<REGLAS_NOMBRE_PLAN> objListReglasNomPlan = new ArrayList<REGLAS_NOMBRE_PLAN>();            
      
                objListReglasNomPlan = mapPlan.getProductoCarga(cod_prod,cod_servicio,aplica_velocidad,arrendamiento,relacionadoTI);
       
                if(objListReglasNomPlan.size()>0){

                    if(objListReglasNomPlan.size()>1){

                        for (int i = 0; i < objListReglasNomPlan.size(); i++) {

                            if(objListReglasNomPlan.get(i).getNOMBRE_PLAN_ForJSON().equalsIgnoreCase(nombrePlan)){
                                 productoCarga = objListReglasNomPlan.get(i).getPRODUCTO_CARGA_ForJSON() ;
                                 break;
                            }                        
                        }

                    }else{
                        productoCarga = objListReglasNomPlan.get(0).getPRODUCTO_CARGA_ForJSON() ;
                    }

                }else{
                   productoCarga = "";
                }            
            
            }
    
     return productoCarga;
    }

}
