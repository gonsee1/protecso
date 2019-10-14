/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.reportes;

import com.americatel.facturacion.fncs.ExcelWriter;
import com.americatel.facturacion.models.BOLE;
import com.americatel.facturacion.models.CIER;
import com.americatel.facturacion.models.CIER_DATA_PLAN;
import com.americatel.facturacion.models.FACT;
import com.americatel.facturacion.models.NEGO_SUCU_PLAN;
import com.americatel.facturacion.models.PROD;
import com.americatel.facturacion.models.RECI;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author rordonez
 */
public class ReporteAnulados {
    private List<CIER> listaCier;
    private Integer NU_ANO;
    private Integer NU_PERI;
    private ExcelWriter excel;
    public ReporteAnulados(List<CIER> listaCier,Integer COD_PROD_PADRE){
        this.listaCier=listaCier;
        if (listaCier.size()>0){
            this.NU_ANO=listaCier.get(0).getNU_ANO_ForJSON();
            this.NU_PERI=listaCier.get(0).getNU_PERI_ForJSON();
        }
        this.excel=new ExcelWriter();
        this.excel.addHoja("Reporte Anulados");
        this.calcular(COD_PROD_PADRE);
    }

    public void save(HttpServletResponse response) {
        response.setHeader("Content-Type", "application/vnd.ms-excel");
        response.addHeader("Content-Disposition", "attachment; filename=\"Reportes_Anulados_"+NU_ANO+"_"+NU_PERI+"\".xls" );
        this.excel.save(response);
    }
        
    public String getStringOrCero(String cadena){
        if (cadena!=null){
            if (!cadena.trim().equals(""))
                return cadena;
        }
        return "0";
    }
    
    private void calcular(Integer COD_PROD_PADRE) {
        this.excel.writeCols(new Object[]{
            "TIPO DOCUMENTO","NRO DOCUMENTO","FECHA EMISION",
            "COD. CLIENTE","NEGOCIO CLIENTE","TIPO SERVICIO","MONEDA","INSTALACION","SERV MENSUAL",
            "ALQ EQUIPOS","OTROS","DESCUENTO","SUB TOTAL","IGV","TOTAL FACTURA",
            "FECHA VCTO.","DETALLE DE SERVICIO","PERIODO","AJUSTE AFECTO IGV","AJUSTE NO AFECTO IGV"
        });
        
        List<RECI> recibos;
        List<BOLE> boletos;
        List<FACT> facturas;
        PROD prod;
        
        for(CIER cier:listaCier){
            recibos=cier.getRecibosAnulados();
            prod=cier.getProducto();
            List<CIER_DATA_PLAN> data_planes_cobrados_en_cierre=cier.getDataPlanesCobrados_forMaestroGerentes();
            Map<Long,CIER_DATA_PLAN> data_detalle_del_servicio=new TreeMap<Long, CIER_DATA_PLAN>();
            
            List<NEGO_SUCU_PLAN> nego_sucu_planes=cier.getNEGO_SUCU_PLAN_forMaestroGerentes();
            Map<Integer,NEGO_SUCU_PLAN> map_nego_sucu_planes=new TreeMap<Integer, NEGO_SUCU_PLAN>();
            
            for (CIER_DATA_PLAN cier_data_plan : data_planes_cobrados_en_cierre){
                               
                 if(cier_data_plan.getCO_RECI()!=null){
                  if (!data_detalle_del_servicio.containsKey(cier_data_plan.getCO_RECI())){
                    data_detalle_del_servicio.put(cier_data_plan.getCO_RECI(), cier_data_plan);
                    //cantidad_planes_por_recibos.put(cier_data_plan.getCO_RECI(), 0);
                  }
                }else if(cier_data_plan.getCO_BOLE()!=null){
                  if (!data_detalle_del_servicio.containsKey(cier_data_plan.getCO_BOLE())){
                    data_detalle_del_servicio.put(cier_data_plan.getCO_BOLE(), cier_data_plan);
                    //cantidad_planes_por_recibos.put(cier_data_plan.getCO_RECI(), 0);
                  }
                }else if(cier_data_plan.getCO_FACT()!=null){
                  if (!data_detalle_del_servicio.containsKey(cier_data_plan.getCO_FACT())){
                    data_detalle_del_servicio.put(cier_data_plan.getCO_FACT(), cier_data_plan);
                    //cantidad_planes_por_recibos.put(cier_data_plan.getCO_RECI(), 0);
                  }
                }
            }
            
            for (NEGO_SUCU_PLAN nego_sucu_plan : nego_sucu_planes){
                if (!map_nego_sucu_planes.containsKey(nego_sucu_plan.getCO_NEGO_SUCU_PLAN_ForJSON())){
                    if(nego_sucu_plan.getCO_NEGO_SUCU_PLAN_ForJSON() != null){
                    map_nego_sucu_planes.put(nego_sucu_plan.getCO_NEGO_SUCU_PLAN_ForJSON(), nego_sucu_plan);
                    }
                }
            }
            CIER_DATA_PLAN cier_data_plan=null;
            NEGO_SUCU_PLAN nego_sucu_plan=null;
            Boolean ST_BACKUP_BU;
            Boolean RELACIONADO_TI;
            String nombrePlan = "";
            
            String tipo_servicio;
            for (RECI reci:recibos){
                ST_BACKUP_BU=false;
                tipo_servicio="";
                cier_data_plan=null;
                nego_sucu_plan=null;
                 RELACIONADO_TI=false;
                nombrePlan="";
                
                if (data_detalle_del_servicio.containsKey(reci.getCO_RECI_ForJSON())){
                    cier_data_plan=data_detalle_del_servicio.get(reci.getCO_RECI_ForJSON());
                    if (map_nego_sucu_planes.containsKey(cier_data_plan.getCO_NEGO_SUCU_PLAN())){
                        nego_sucu_plan=map_nego_sucu_planes.get(cier_data_plan.getCO_NEGO_SUCU_PLAN());
                        ST_BACKUP_BU=nego_sucu_plan.getPLAN().getST_BACKUP_BU_ForJSON();
                        RELACIONADO_TI=nego_sucu_plan.getPLAN().getRELACIONADO_TI_ForJSON();
                        nombrePlan = nego_sucu_plan.getPLAN().getNO_PLAN_ForJSON();
                    }
                }
               
                 
                if(COD_PROD_PADRE == 1){        
                        
                    if (!ST_BACKUP_BU){
                        if (prod.getCO_PROD_ForJSON()==1){
                            tipo_servicio="IPB SAT";
                        } else {
                            tipo_servicio="ADD SAT";
                        }
                    } else {
                        if (prod.getCO_PROD_ForJSON()==1){
                            tipo_servicio="IPB SAT - BU";
                        } else {
                            tipo_servicio="ADD SAT - BU";
                        }
                    }
                
                }
                
                if(COD_PROD_PADRE == 2){        
                    
                    if (!RELACIONADO_TI){
                        if (prod.getCO_PROD_ForJSON()==3){
                            tipo_servicio="IPB";
                        } else {
                            tipo_servicio="ADD";
                        }
                    } else {
                        if (prod.getCO_PROD_ForJSON()==3){
                            tipo_servicio="IPB (ICX-TI)";
                        } else {
                            tipo_servicio="ADD (ICX-TI)";
                        }
                    }                    
                
                }
                  
                if(COD_PROD_PADRE == 3){        
                    
                     if (prod.getCO_PROD_ForJSON()==5){
                         tipo_servicio="IPB SAT - INGENIO";
                     }else if (prod.getCO_PROD_ForJSON()==6){
                         tipo_servicio="ADD SAT -INGENIO";
                     }
                }
                
                if(COD_PROD_PADRE == 5){
                    
                    if (prod.getCO_PROD_ForJSON()==12){
                         tipo_servicio="IPB - WHO";
                    }else if(prod.getCO_PROD_ForJSON()==15){
                         tipo_servicio="COL - WHO";
                    }else if(prod.getCO_PROD_ForJSON()==13){
                         if(nombrePlan.equalsIgnoreCase("ARRENDAMIENTO DE FIBRA OSCURA")){
                         tipo_servicio="ADD - WHO(FO)";
                         }else{
                         tipo_servicio="ADD - WHO";
                         }                        
                    }else if(prod.getCO_PROD_ForJSON()==14){
                         if(nombrePlan.equalsIgnoreCase("SERVICIO DE DUCTERIA")){
                         tipo_servicio="DUC - WHO";
                         }else if(nombrePlan.equalsIgnoreCase("SERVICIO DE CABLEADO")){
                         tipo_servicio="DUC - WHO(SS)";
                         }                                                 
                    }else if(prod.getCO_PROD_ForJSON()==16){
                         if(nombrePlan.equalsIgnoreCase("SERVICIO DE CROSSCONEXION")){
                         tipo_servicio="OTH - WHO";
                         }else if(nombrePlan.equalsIgnoreCase("OTROS SERVICIOS TECNICOS POST VENTA")){
                         tipo_servicio="OTH - WHO(SS)";
                         }else if(nombrePlan.equalsIgnoreCase("CAPACIDAD DE INTERCONEXI\u00d3N")){
                         tipo_servicio="ICX - WHO";
                         }    
                    }
                    
                    //nombrePlan
                }
                
                
                
                
                Double neto=com.americatel.facturacion.fncs.Numero.redondear(reci.getIM_NETO_ForJSON()+reci.getIM_AJUS_SIGV_ForJSON(),2);
                Double servicios_mensuales=com.americatel.facturacion.fncs.Numero.redondear(reci.getIM_SERV_MENS_ForJSON()+reci.getIM_DIF_CARGO_FIJO_ForJSON(),2);
                this.excel.writeCols(new Object[]{
                    "RECIBO",
                    reci.getCO_RECI_ForJSON(),
                    reci.getFE_EMIS_ForJSON(),
                    reci.getDE_CODI_BUM_ForJSON(),
                    reci.getCO_NEGO_ForJSON(),
                    tipo_servicio,
                    reci.getNO_MONE_FACT_ForJSON(),
                    getStringOrCero(reci.getIM_INST_ForJSON().toString()),
                    getStringOrCero(servicios_mensuales.toString()),
                    getStringOrCero(reci.getIM_ALQU_ForJSON().toString()),
                    getStringOrCero(reci.getIM_OTRO_ForJSON().toString()),
                    getStringOrCero(reci.getIM_DESC_ForJSON().toString()),
                    getStringOrCero(neto.toString()), // A solicitud al neto se le resta ajuste afecto igv
                    getStringOrCero(reci.getIM_IGV_ForJSON().toString()),
                    getStringOrCero(reci.getIM_TOTA_ForJSON().toString()),
                    reci.getFE_VENC_ForJSON(),
                    reci.getDetalleServicio(),
                    reci.getDE_PERI_ForJSON(),
                    Math.abs(reci.getIM_AJUS_SIGV_ForJSON()),
                    Math.abs(reci.getIM_AJUS_NIGV_ForJSON()),
                });  
            }
            
            
            boletos =cier.getBoletasAnulados(); 
            
            for (BOLE boleto:boletos){
                ST_BACKUP_BU=false;
                tipo_servicio="";
                cier_data_plan=null;
                nego_sucu_plan=null;
                 RELACIONADO_TI=false;
                nombrePlan="";
                
                if (data_detalle_del_servicio.containsKey(boleto.getCO_BOLE_ForJSON())){
                    cier_data_plan=data_detalle_del_servicio.get(boleto.getCO_BOLE_ForJSON());
                    if (map_nego_sucu_planes.containsKey(cier_data_plan.getCO_NEGO_SUCU_PLAN())){
                        nego_sucu_plan=map_nego_sucu_planes.get(cier_data_plan.getCO_NEGO_SUCU_PLAN());
                        ST_BACKUP_BU=nego_sucu_plan.getPLAN().getST_BACKUP_BU_ForJSON();
                        RELACIONADO_TI=nego_sucu_plan.getPLAN().getRELACIONADO_TI_ForJSON();
                        nombrePlan = nego_sucu_plan.getPLAN().getNO_PLAN_ForJSON();
                    }
                }
               
                 
                if(COD_PROD_PADRE == 1){        
                        
                    if (!ST_BACKUP_BU){
                        if (prod.getCO_PROD_ForJSON()==1){
                            tipo_servicio="IPB SAT";
                        } else {
                            tipo_servicio="ADD SAT";
                        }
                    } else {
                        if (prod.getCO_PROD_ForJSON()==1){
                            tipo_servicio="IPB SAT - BU";
                        } else {
                            tipo_servicio="ADD SAT - BU";
                        }
                    }
                
                }
                
                if(COD_PROD_PADRE == 2){        
                    
                    if (!RELACIONADO_TI){
                        if (prod.getCO_PROD_ForJSON()==3){
                            tipo_servicio="IPB";
                        } else {
                            tipo_servicio="ADD";
                        }
                    } else {
                        if (prod.getCO_PROD_ForJSON()==3){
                            tipo_servicio="IPB (ICX-TI)";
                        } else {
                            tipo_servicio="ADD (ICX-TI)";
                        }
                    }                    
                
                }
                  
                if(COD_PROD_PADRE == 3){        
                    
                     if (prod.getCO_PROD_ForJSON()==5){
                         tipo_servicio="IPB SAT - INGENIO";
                     }else if (prod.getCO_PROD_ForJSON()==6){
                         tipo_servicio="ADD SAT -INGENIO";
                     }
                }
                
                if(COD_PROD_PADRE == 5){
                    
                    if (prod.getCO_PROD_ForJSON()==12){
                         tipo_servicio="IPB - WHO";
                    }else if(prod.getCO_PROD_ForJSON()==15){
                         tipo_servicio="COL - WHO";
                    }else if(prod.getCO_PROD_ForJSON()==13){
                         if(nombrePlan.equalsIgnoreCase("ARRENDAMIENTO DE FIBRA OSCURA")){
                         tipo_servicio="ADD - WHO(FO)";
                         }else{
                         tipo_servicio="ADD - WHO";
                         }                        
                    }else if(prod.getCO_PROD_ForJSON()==14){
                         if(nombrePlan.equalsIgnoreCase("SERVICIO DE DUCTERIA")){
                         tipo_servicio="DUC - WHO";
                         }else if(nombrePlan.equalsIgnoreCase("SERVICIO DE CABLEADO")){
                         tipo_servicio="DUC - WHO(SS)";
                         }                                                 
                    }else if(prod.getCO_PROD_ForJSON()==16){
                         if(nombrePlan.equalsIgnoreCase("SERVICIO DE CROSSCONEXION")){
                         tipo_servicio="OTH - WHO";
                         }else if(nombrePlan.equalsIgnoreCase("OTROS SERVICIOS TECNICOS POST VENTA")){
                         tipo_servicio="OTH - WHO(SS)";
                         }else if(nombrePlan.equalsIgnoreCase("CAPACIDAD DE INTERCONEXI\u00d3N")){
                         tipo_servicio="ICX - WHO";
                         }    
                    }
                    
                    //nombrePlan
                }
                
                
                
                
                Double neto=com.americatel.facturacion.fncs.Numero.redondear(boleto.getIM_NETO_ForJSON()+boleto.getIM_AJUS_SIGV_ForJSON(),2);
                Double servicios_mensuales=com.americatel.facturacion.fncs.Numero.redondear(boleto.getIM_SERV_MENS_ForJSON()+boleto.getIM_DIF_CARGO_FIJO_ForJSON(),2);
                this.excel.writeCols(new Object[]{
                    "BOLETA",
                    boleto.getCO_BOLE_ForJSON(),
                    boleto.getFE_EMIS_ForJSON(),
                    boleto.getDE_CODI_BUM_ForJSON(),
                    boleto.getCO_NEGO_ForJSON(),
                    tipo_servicio,
                    boleto.getNO_MONE_FACT_ForJSON(),
                    getStringOrCero(boleto.getIM_INST_ForJSON().toString()),
                    getStringOrCero(servicios_mensuales.toString()),
                    getStringOrCero(boleto.getIM_ALQU_ForJSON().toString()),
                    getStringOrCero(boleto.getIM_OTRO_ForJSON().toString()),
                    getStringOrCero(boleto.getIM_DESC_ForJSON().toString()),
                    getStringOrCero(neto.toString()), // A solicitud al neto se le resta ajuste afecto igv
                    getStringOrCero(boleto.getIM_IGV_ForJSON().toString()),
                    getStringOrCero(boleto.getIM_TOTA_ForJSON().toString()),
                    boleto.getFE_VENC_ForJSON(),
                    boleto.getDetalleServicio(),
                    boleto.getDE_PERI_ForJSON(),
                    Math.abs(boleto.getIM_AJUS_SIGV_ForJSON()),
                    Math.abs(boleto.getIM_AJUS_NIGV_ForJSON()),
                });  
            }
            
            
            
            facturas = cier.getFacturasAnulados(); 
            
             for (FACT factura:facturas){
                ST_BACKUP_BU=false;
                tipo_servicio="";
                cier_data_plan=null;
                nego_sucu_plan=null;
                RELACIONADO_TI=false;
                nombrePlan="";
                
                if (data_detalle_del_servicio.containsKey(factura.getCO_FACT_ForJSON())){
                    cier_data_plan=data_detalle_del_servicio.get(factura.getCO_FACT_ForJSON());
                    if (map_nego_sucu_planes.containsKey(cier_data_plan.getCO_NEGO_SUCU_PLAN())){
                        nego_sucu_plan=map_nego_sucu_planes.get(cier_data_plan.getCO_NEGO_SUCU_PLAN());
                        ST_BACKUP_BU=nego_sucu_plan.getPLAN().getST_BACKUP_BU_ForJSON();
                        RELACIONADO_TI=nego_sucu_plan.getPLAN().getRELACIONADO_TI_ForJSON();
                        nombrePlan = nego_sucu_plan.getPLAN().getNO_PLAN_ForJSON();
                    }
                }
               
                if(COD_PROD_PADRE == 4){        

                    if(prod.getCO_PROD_ForJSON() == 7){

                       tipo_servicio="HOU";
                       //servicioReporte="TI-Housing";
                    }else if(prod.getCO_PROD_ForJSON() == 8){
                       tipo_servicio="IAAS";
                                //servicioReporte="TI-IAAS";
                    }else if(prod.getCO_PROD_ForJSON() == 9){
                       tipo_servicio="SAAS - CB";
                                //servicioReporte="TI-SAAS-CB";
                    }else if(prod.getCO_PROD_ForJSON() == 10){
                       tipo_servicio="SAAS - SAS";
                                //servicioReporte="TI-SAAS-SAS";
                    }else if(prod.getCO_PROD_ForJSON() == 11){
                       tipo_servicio="HOU - ONE SHOT";
                                //servicioReporte="TI - HOU - ONE SHOT";
                    }
                } 
                
                
                if(COD_PROD_PADRE == 1){        
                        
                    if (!ST_BACKUP_BU){
                        if (prod.getCO_PROD_ForJSON()==1){
                            tipo_servicio="IPB SAT";
                        } else {
                            tipo_servicio="ADD SAT";
                        }
                    } else {
                        if (prod.getCO_PROD_ForJSON()==1){
                            tipo_servicio="IPB SAT - BU";
                        } else {
                            tipo_servicio="ADD SAT - BU";
                        }
                    }
                
                }
                
                if(COD_PROD_PADRE == 2){        
                    
                    if (!RELACIONADO_TI){
                        if (prod.getCO_PROD_ForJSON()==3){
                            tipo_servicio="IPB";
                        } else {
                            tipo_servicio="ADD";
                        }
                    } else {
                        if (prod.getCO_PROD_ForJSON()==3){
                            tipo_servicio="IPB (ICX-TI)";
                        } else {
                            tipo_servicio="ADD (ICX-TI)";
                        }
                    }                    
                
                }
                  
                if(COD_PROD_PADRE == 3){        
                    
                     if (prod.getCO_PROD_ForJSON()==5){
                         tipo_servicio="IPB SAT - INGENIO";
                     }else if (prod.getCO_PROD_ForJSON()==6){
                         tipo_servicio="ADD SAT -INGENIO";
                     }
                }
                
                if(COD_PROD_PADRE == 5){
                    
                    if (prod.getCO_PROD_ForJSON()==12){
                         tipo_servicio="IPB - WHO";
                    }else if(prod.getCO_PROD_ForJSON()==15){
                         tipo_servicio="COL - WHO";
                    }else if(prod.getCO_PROD_ForJSON()==13){
                         if(nombrePlan.equalsIgnoreCase("ARRENDAMIENTO DE FIBRA OSCURA")){
                         tipo_servicio="ADD - WHO(FO)";
                         }else{
                         tipo_servicio="ADD - WHO";
                         }                        
                    }else if(prod.getCO_PROD_ForJSON()==14){
                         if(nombrePlan.equalsIgnoreCase("SERVICIO DE DUCTERIA")){
                         tipo_servicio="DUC - WHO";
                         }else if(nombrePlan.equalsIgnoreCase("SERVICIO DE CABLEADO")){
                         tipo_servicio="DUC - WHO(SS)";
                         }                                                 
                    }else if(prod.getCO_PROD_ForJSON()==16){
                         if(nombrePlan.equalsIgnoreCase("SERVICIO DE CROSSCONEXION")){
                         tipo_servicio="OTH - WHO";
                         }else if(nombrePlan.equalsIgnoreCase("OTROS SERVICIOS TECNICOS POST VENTA")){
                         tipo_servicio="OTH - WHO(SS)";
                         }else if(nombrePlan.equalsIgnoreCase("CAPACIDAD DE INTERCONEXI\u00d3N")){
                         tipo_servicio="ICX - WHO";
                         }    
                    }
                    
                    //nombrePlan
                }

                Double neto=com.americatel.facturacion.fncs.Numero.redondear(factura.getIM_VALO_VENT_NETO_ForJSON()+factura.getIM_AJUS_SIGV_ForJSON(),2);
                Double servicios_mensuales=com.americatel.facturacion.fncs.Numero.redondear(factura.getIM_SERV_MENS_ForJSON()+factura.getIM_DIF_CARGO_FIJO_ForJSON(),2);
                this.excel.writeCols(new Object[]{
                    "FACTURA",
                    factura.getCO_FACT_ForJSON(),
                    factura.getFE_EMIS_ForJSON(),
                    factura.getDE_CODI_BUM_ForJSON(),
                    factura.getCO_NEGO_ForJSON(),
                    tipo_servicio,
                    factura.getNO_MONE_FACT_ForJSON(),
                    getStringOrCero(factura.getIM_INST_ForJSON().toString()),
                    getStringOrCero(servicios_mensuales.toString()),
                    getStringOrCero(factura.getIM_ALQU_ForJSON().toString()),
                    getStringOrCero(factura.getIM_OTRO_ForJSON().toString()),
                    getStringOrCero(factura.getIM_VALO_DESC_ForJSON().toString()),
                    getStringOrCero(neto.toString()), // A solicitud al neto se le resta ajuste afecto igv
                    getStringOrCero(factura.getIM_IGV_ForJSON().toString()),
                    getStringOrCero(factura.getIM_TOTA_ForJSON().toString()),
                    factura.getFE_VENC_ForJSON(),
                    factura.getDetalleServicio(),
                    factura.getDE_PERI_ForJSON(),
                    Math.abs(factura.getIM_AJUS_SIGV_ForJSON()),
                    Math.abs(factura.getIM_AJUS_NIGV_ForJSON()),
                });  
            }
            
            
        }
        
        
        
        
    }
}
