/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.reportes;

import com.americatel.facturacion.Utils.Constante;
import com.americatel.facturacion.Utils.Utils;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.americatel.facturacion.fncs.ExcelWriter;
import com.americatel.facturacion.mappers.mapBOLE;
import com.americatel.facturacion.mappers.mapCLIE;
import com.americatel.facturacion.mappers.mapFACT;
import com.americatel.facturacion.mappers.mapPLAN;
import com.americatel.facturacion.mappers.mapPROD;
import com.americatel.facturacion.models.CIER;
import com.americatel.facturacion.models.CLIE;
import com.americatel.facturacion.models.PLAN;
import com.americatel.facturacion.models.REGLAS_NOMBRE_PLAN;
import com.americatel.facturacion.models.REPORTE_FACTURA;
/**
 *
 * @author crodas
 */
@Component  
public class ReporteGerenteSunat {
    private List<CIER> listaCier;
    private Integer NU_ANO;
    private Integer NU_PERI;
    private String tipDocumento;
    private ExcelWriter excel;
    
    public static mapCLIE mapClie;
    

	@Autowired
    public void setmapClie(mapCLIE mapClie){
       ReporteGerenteSunat.mapClie = mapClie;
    }
    
    public static mapFACT mapFact;    
    
    @Autowired
    public void setmapFact(mapFACT mapFact){
       ReporteGerenteSunat.mapFact = mapFact;
    }
    
    public static mapBOLE mapBole;
    
    @Autowired
    public void setmapBole(mapBOLE mapBole){
       ReporteGerenteSunat.mapBole = mapBole;
    }
    
    public static mapPLAN mapPlan;
    
    @Autowired
    public void setmapPlan(mapPLAN mapPlan){
       ReporteGerenteSunat.mapPlan = mapPlan;
    }
    
     public static mapPROD mapProd;    
    
    @Autowired
    public void setmapProd(mapPROD mapProd){
       ReporteGerenteSunat.mapProd = mapProd;
    }
    
    public ReporteGerenteSunat(){
        
    }
        
    public ReporteGerenteSunat(List<CIER> listaCier,Integer COD_PROD_PADRE,Integer CO_ANO,Integer CO_PERI,Integer COD_TIPO_DOC){
        this.listaCier=listaCier;
        if (listaCier.size()>0){
            this.NU_ANO=listaCier.get(0).getNU_ANO_ForJSON();
            this.NU_PERI=listaCier.get(0).getNU_PERI_ForJSON();
        }
        
        if(1 == COD_TIPO_DOC){
        this.tipDocumento="Factura";
        }else{
        this.tipDocumento="Boleta";
        }
        
        this.excel=new ExcelWriter();
        this.excel.addHoja("Reporte Cargas");
        
        this.calcular(COD_PROD_PADRE,CO_ANO,CO_PERI,COD_TIPO_DOC);
        
        
    }

    public void save(HttpServletResponse response) {
        response.setHeader("Content-Type", "application/vnd.ms-excel");
        response.addHeader("Content-Disposition", "attachment; filename=\"Reportes_Sunat_"+tipDocumento+"_"+NU_ANO+"_"+NU_PERI+"\".xls" );
        this.excel.save(response);
    }

    private void calcular(Integer COD_PROD_PADRE,Integer CO_ANO,Integer CO_PERI,Integer COD_TIPO_DOC) {
        this.excel.writeCols(new Object[]{
            "ITEM","N\u00b0 DOCUMENTO (FC/NC/ND/BV)","FECHA DE EMISI\u00d3N","RAZ\u00d3N SOCIAL","C\u00d3D CLIENTE","DIRECCI\u00d3N FISCAL","DISTRITO","PROVINCIA","DEPARTAMENTO","RUC",
            "FECHA DE VENCIMIENTO","Descripcion","CANTIDAD","VALOR UNITARIO","DESCUENTO","TOTAL","OP. GRAVADAS","OP. INAFECTAS","OP. EXONERADAS","OP. GRATUITAS",
            "DESCUENTOS GLOBALES","IGV","OTROS TRIBUTOS","OTROS CARGOS","TOTAL","AJUSTE POR REDONDEO","PERIODO","NEGOCIO ","REFERENCIA (DOCUMENTO AFECTO)","TIPO DE MONEDA",
            "REFERENCIAS ADICIONALES 1","REFERENCIAS ADICIONALES 2","REFERENCIAS ADICIONALES 3","REFERENCIAS ADICIONALES 4","GLOSA DETRACCION","IGV","SERVICIO","ORDEN DE COMPRA","TIPO OPERACI\u00d3N","TIPO DOC IDENTIDAD",
            "TIPO AFECT. IGV","TIPO PRECIO","Archivo Adjunto"
        });
        
        List<REPORTE_FACTURA> objConsultaFacturas = null; 
        
        if(Constante.FACTURA == COD_TIPO_DOC){
            objConsultaFacturas = mapFact.consultaReporteGerenteSunatFactura(COD_PROD_PADRE,CO_PERI,CO_ANO);
        } else if(Constante.BOLETA == COD_TIPO_DOC){
            objConsultaFacturas = mapBole.consultaReporteGerenteSunatBoleta(COD_PROD_PADRE,CO_PERI,CO_ANO);
        } 
       
           
        int item = 1; 
        String negocioTemporal = ""; 
        String codigoBun="";
        String codigoDigitador="";
        String codigoClienteFinal = "";
        String productoCarga ="";
        PLAN planBean; 
        
        CLIE clieBean = new CLIE();
        
        for (REPORTE_FACTURA obj : objConsultaFacturas) {
           
           codigoBun=""; 
           codigoDigitador="";
           codigoClienteFinal = "";
           planBean = null ;
            
           if(obj.getCAMPO_4_ForJSON() != null){
                 
               clieBean = mapClie.getId(Integer.parseInt(obj.getCAMPO_4_ForJSON()));
               
               codigoBun = clieBean.getDE_CODI_BUM_ForJSON();
               codigoDigitador = clieBean.getDE_DIGI_BUM_ForJSON();
               
              codigoClienteFinal = codigoBun +"-"+codigoDigitador;           
           } 
            
           
           if(negocioTemporal.equalsIgnoreCase(obj.getNEGOCIO_ForJSON())){
              item++;                                     
           }else{
              negocioTemporal = obj.getNEGOCIO_ForJSON();
              item=1;
           }
           
           
           productoCarga = "";
         
        
           if(obj.getSERVICIO_ForJSON().length() > 0){
                
//            productoCarga = getCodServicio(COD_PROD_PADRE,obj.getSERVICIO_ForJSON());
        	   planBean = mapPlan.getOnlyObjectForId(Integer.parseInt(obj.getSERVICIO_ForJSON()));
        	   productoCarga = planBean==null?"":planBean.getPRODUCTO_CARGA(); 
           }
           
           this.excel.writeCols(new Object[]{
           item,
           getFormatoCodigo(Long.valueOf(obj.getCAMPO_1_ForJSON()), COD_TIPO_DOC),
           obj.getCAMPO_2_ForJSON(),
           obj.getCAMPO_3_ForJSON(),
           codigoClienteFinal,
           obj.getCAMPO_5_ForJSON(),
           obj.getDISTRITO_ForJSON(),
           obj.getPROVINCIA_ForJSON(),
           obj.getDEPARTAMENTO_ForJSON(),
           obj.getRUC_ForJSON(),
           obj.getFECHA_VENCIMIENTO_ForJSON(),
           obj.getDESCRIPCION_ForJSON(),
           obj.getCANTIDAD_ForJSON(),
           obj.getVALOR_UNITARIO_ForJSON(),
           obj.getDESCUENTO_ForJSON(),
           obj.getTOTAL_ForJSON(),
           obj.getOP_GRAVADAS_ForJSON(),
           obj.getOP_INAFECTAS_ForJSON(),
           obj.getOP_EXONERADAS_ForJSON(),
           obj.getOP_GRATUITAS_ForJSON(),
           obj.getDESCUENTOS_GLOBALES_ForJSON(),
           obj.getIGV_ForJSON(),
           obj.getOTROS_TRIBUTOS_ForJSON(),
           obj.getOTROS_CARGOS_ForJSON(),
           obj.getTOTAL_2_ForJSON(),
           obj.getAJUSTE_REDONDEO_ForJSON(),
           obj.getPERIODO_ForJSON(),
           obj.getNEGOCIO_ForJSON(),
           obj.getREFERENCIA_ForJSON(),
           obj.getTIPO_MONEDA_ForJSON(),
           obj.getREFERENCIAS_ADICIONALES_1_ForJSON(),
           obj.getREFERENCIAS_ADICIONALES_2_ForJSON(),
           obj.getREFERENCIAS_ADICIONALES_3_ForJSON(),
           obj.getREFERENCIAS_ADICIONALES_4_ForJSON(),
           obj.getGLOSA_DETRACCION_ForJSON(),
           obj.getIGV_2_ForJSON(),
           productoCarga,
           obj.getORDEN_COMPRA_ForJSON(),
           obj.getTIPO_OPERACION_ForJSON(),
           obj.getTIPO_DOC_IDENTIDAD_ForJSON(),
           obj.getTIPO_AFECT_IGV_ForJSON(),
           obj.getTIPO_PRECIO_ForJSON(),
          });
        }
    }
 
    
//    public String getCodServicio(Integer cod_ProductoParam,String codPlanParam){
//    
//        int  relacionadoTI = -1;
//        int  aplica_velocidad = -1;
//        int  arrendamiento = -1;
//        int  cod_prod = -1; 
//        int  cod_servicio = -1;        
//        String nombrePlan = "";
//        String productoCarga ="";
//        
//            int codPlan =  Integer.parseInt(codPlanParam); 
//        
//            PLAN objPlanBean  = new PLAN();  
//            objPlanBean = mapPlan.getId(codPlan);
//            
//            if(objPlanBean != null){
//
//               nombrePlan = objPlanBean.getNO_PLAN_ForJSON();
//                                
//               if(objPlanBean.getRELACIONADO_TI_ForJSON()== true){
//                    relacionadoTI = 1;
//               }else{
//                    relacionadoTI = 0;
//               }               
//               String   velo_bajada   = objPlanBean.getDE_VELO_BAJA_ForJSON();
//               String   velo_subida   = objPlanBean.getDE_VELO_SUBI_ForJSON();
//                             
//               if(velo_bajada.length() > 0 || velo_subida.length()>0){
//                    aplica_velocidad = 1;
//               }else{
//                    aplica_velocidad = 0;
//               }
//               
//               if(objPlanBean.getST_BACKUP_BU_ForJSON()== true){
//                    arrendamiento = 1;
//               }else{
//                    arrendamiento = 0;
//               }               
//               cod_servicio  = objPlanBean.getCO_PROD_ForJSON();
//               
//               cod_prod  = cod_ProductoParam;                                     
//            
//                if(cod_ProductoParam == 1){
//
//                    if(arrendamiento == 1){                    
//                      if(cod_servicio == 1){
//                        productoCarga="IPB SAT - BU";
//                      }else{
//                        productoCarga="ADD SAT - BU";
//                      }  
//                    }else {                
//                      if(cod_servicio == 1){
//                        productoCarga="IPB - SAT";
//                      }else{
//                        productoCarga="ADD - SAT";
//                      }                       
//                    }
//
//                }else if(cod_ProductoParam == 2){            
//                    if(aplica_velocidad == 0){
//                        productoCarga="ADD - FO";
//                    }else{
//                      if(relacionadoTI == 1){
//                         if(cod_servicio == 3){
//                          productoCarga="IPB (ICX-TI)";
//                         }else{
//                          productoCarga="ADD (ICX-TI)";
//                         }                      
//                      }else{
//                         if(cod_servicio == 3){
//                          productoCarga="IPB";
//                         }else{
//                          productoCarga="ADD";
//                         }  
//                      }                                        
//                    }            
//                }else if(cod_ProductoParam == 3){
//                    if(cod_servicio == 5){
//                     productoCarga="IPB SAT - INGENIO";
//                    }else{
//                    productoCarga="ADD SAT -INGENIO";
//                    }
//                    
//                }else if(cod_ProductoParam == 4){
//                    
//                    if(cod_servicio == 7){
//                      productoCarga="HOU";
//                    }else if(cod_servicio == 8){
//                      productoCarga="IAAS";
//                    }else if(cod_servicio == 9){
//                      productoCarga="SAAS - CB";
//                    }else if(cod_servicio == 10){
//                      productoCarga="SAAS - SAS";
//                    }else if(cod_servicio == 11){
//                      productoCarga="HOU - ONE SHOT";
//                    }
//                }else if(cod_ProductoParam == 5){
//                 
//                    if(cod_servicio == 12){
//                    productoCarga="IPB - WHO";
//                    }else if(cod_servicio == 13){
//                        if(aplica_velocidad == 1){
//                         productoCarga="ADD - WHO";
//                        }else{
//                         productoCarga="ADD - WHO(FO)";
//                        }                                           
//                    }else if(cod_servicio == 15){
//                     productoCarga="COL - WHO";
//                    }
//                }
//            
//            }  
//           
//            if(productoCarga.length()==0){
//                
//                List<REGLAS_NOMBRE_PLAN> objListReglasNomPlan = new ArrayList<REGLAS_NOMBRE_PLAN>();            
//      
//                objListReglasNomPlan = mapPlan.getProductoCarga(cod_prod,cod_servicio,aplica_velocidad,arrendamiento,relacionadoTI);
//       
//                if(objListReglasNomPlan.size()>0){
//
//                    if(objListReglasNomPlan.size()>1){
//
//                        for (int i = 0; i < objListReglasNomPlan.size(); i++) {
//
//                            if(objListReglasNomPlan.get(i).getNOMBRE_PLAN_ForJSON().equalsIgnoreCase(nombrePlan)){
//                                 productoCarga = objListReglasNomPlan.get(i).getPRODUCTO_CARGA_ForJSON() ;
//                                 break;
//                            }                        
//                        }
//
//                    }else{
//                        productoCarga = objListReglasNomPlan.get(0).getPRODUCTO_CARGA_ForJSON() ;
//                    }
//
//                }else{
//                   productoCarga = "";
//                }            
//            
//            }
//    
//     return productoCarga;
//    }
    
    public String getFormatoCodigo(Long codigoParam,Integer tipoDocumento){
    
        String codigo="";

        if(Constante.BOLETA == tipoDocumento){
            codigo =  "B050" + String.format("%08d", codigoParam);
        }else if(Constante.FACTURA == tipoDocumento){
            codigo =  "F050" + String.format("%08d", codigoParam);
        }

        return codigo;
    }  
    
}
