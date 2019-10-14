/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.reportes;

import com.americatel.facturacion.fncs.ExcelWriter;
import com.americatel.facturacion.models.CIER;
import com.americatel.facturacion.models.CLIE;
import com.americatel.facturacion.models.DEPA;
import com.americatel.facturacion.models.DIST;
import com.americatel.facturacion.models.NEGO;
import com.americatel.facturacion.models.NEGO_SALD;
import com.americatel.facturacion.models.PROV;
import com.americatel.facturacion.models.RECI;
import com.americatel.facturacion.models.SUCU;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author rordonez
 */
public class ReporteNotasCredito {
    private List<CIER> listaCier;
    private Integer NU_ANO;
    private Integer NU_PERI;
    private ExcelWriter excel;
    public ReporteNotasCredito(List<CIER> listaCier,Integer COD_PROD_PADRE,Integer COD_SERVICIO){
        this.listaCier=listaCier;
        if (listaCier.size()>0){
            this.NU_ANO=listaCier.get(0).getNU_ANO_ForJSON();
            this.NU_PERI=listaCier.get(0).getNU_PERI_ForJSON();
        }
        this.excel=new ExcelWriter();
        this.excel.addHoja("Reporte Notas de Credito");
        this.calcular( COD_PROD_PADRE, COD_SERVICIO);
    }
    
    public void save(HttpServletResponse response) {
        response.setHeader("Content-Type", "application/vnd.ms-excel");
        response.addHeader("Content-Disposition", "attachment; filename=\"Reportes_Notas_Credito_"+NU_ANO+"_"+NU_PERI+"\".xls" );
        this.excel.save(response);
    }
        
    public String getStringOrCero(String cadena){
        if (cadena!=null){
            if (!cadena.trim().equals(""))
                return cadena;
        }
        return "0";
    }
    
    private void calcular(Integer COD_PROD_PADRE,Integer COD_SERVICIO) {
        // Se quito la columna descuento a pedido de facturacion 19-02-2016
        this.excel.writeCols(new Object[]{"Nota de Cr\u00e9dito","Fecha de Emisi\u00f3n","Se\u00f1or (es)","C\u00f3digo de Clte BUN","Direcci\u00f3n FISCAL","N\u00famero de Documento","Nº NUEVO RECIBO/ FAC","Glosa","Valor Venta","I.G.V.","Total de devoluci\u00f3n ( Incluido IGV)","NEGOCIO PAGADOR","MONEDA","COD SERVICIO"});
        for(CIER cier:listaCier){
            List<NEGO_SALD> saldos=cier.getSaldos();
            for(NEGO_SALD saldo : saldos){
                NEGO negocio = saldo.getNEGO_ForJSON();
                if (negocio.isDesactivado_ForJSON()){
                    CLIE cliente = negocio.getCLIE_ForJSON();
                    SUCU sucursalFiscal = cliente.getSucursalFiscal();
                    DIST dist_fiscal = sucursalFiscal.getDistrito();
                    PROV prov_fiscal = dist_fiscal.getPROV_ForJSON();
                    DEPA depa_fiscal = prov_fiscal.getDEPA_ForJSON();
                    String tipo_servicio="";
                    Boolean ST_BACKUP_BU=negocio.isArrendamiento_ForJSON();                                       
                            
                    List<RECI> recibos=saldo.getRecibosByCierreAndNego();
                    String numeroRecibo="";
                    String glosa="";
                    for(RECI rec:recibos){
                        if (rec.getIM_TOTA_ForJSON()!=0){
                            numeroRecibo=getStringOrCero(rec.getCO_RECI_ForJSON().toString());
                            glosa=rec.getDE_PERI_ForJSON();
                            break;
                        }
                    }
                    
                    if(COD_PROD_PADRE == 2){        
                    
                       
                        if (COD_SERVICIO==3){
                                tipo_servicio="IPB";
                          
                        } else if(COD_SERVICIO==4){
                          
                                tipo_servicio="ADD";
                        }                                           
                
                     }
                       
                    if(COD_PROD_PADRE == 3){        

                         if (COD_SERVICIO==5){
                             tipo_servicio="IPB SAT - INGENIO";
                         }else if (COD_SERVICIO==6){
                             tipo_servicio="ADD SAT -INGENIO";
                         }
                    } 
                   
                      
                    if(COD_PROD_PADRE == 4){        

                        if(COD_SERVICIO == 7){
                        tipo_servicio="HOU";
                        //servicioReporte="TI-Housing";
                        }else if(COD_SERVICIO == 8){
                        tipo_servicio="IAAS";
                        //servicioReporte="TI-IAAS";
                        }else if(COD_SERVICIO == 9){
                        tipo_servicio="SAAS - CB";
                        //servicioReporte="TI-SAAS-CB";
                        }else if(COD_SERVICIO == 10){
                        tipo_servicio="SAAS - SAS";
                        //servicioReporte="TI-SAAS-SAS";
                        }else if(COD_SERVICIO == 11){
                        tipo_servicio="HOU - ONE SHOT";
                        //servicioReporte="TI - HOU - ONE SHOT";
                        }

                    }  
                    
                    if(COD_PROD_PADRE == 5){
                    
                        if (COD_SERVICIO==12){
                             tipo_servicio="IPB - WHO";
                        }else if(COD_SERVICIO==15){
                             tipo_servicio="COL - WHO";
                        }else if(COD_SERVICIO==13){
                        
                        // tipo_servicio="ADD - WHO(FO)";
                        
                         tipo_servicio="ADD - WHO";
                         
                                                 
                    }else if(COD_SERVICIO==14){
                        
                         tipo_servicio="DUC - WHO";
                                                                     
                    }else if(COD_SERVICIO==16){
                         
                         tipo_servicio="OTH - WHO";
                       
                    }
                    
                     //nombrePlan
                   }
                                        
                    if(COD_PROD_PADRE == 1){
                        
                     if (!ST_BACKUP_BU){
                         if (negocio.getCO_PROD_ForJSON()==1){
                             tipo_servicio="IPB SAT";
                         } else {
                             tipo_servicio="ADD SAT";
                         }
                     } else {
                         if (negocio.getCO_PROD_ForJSON()==1){
                             tipo_servicio="IPB SAT - BU";
                         } else {
                             tipo_servicio="ADD SAT - BU";
                         }
                     }
                    
                    }
                    

                    Double valor_venta = saldo.getIM_MONT_ForJSON();
                    Double igv = com.americatel.facturacion.fncs.Numero.redondear(valor_venta*0.18,2);
                    Double total = com.americatel.facturacion.fncs.Numero.redondear((valor_venta + igv),2);
                    this.excel.writeCols(new Object[]{
                         "",
                         "",
                         cliente.getFullNameCliente(),
                         cliente.getCodigoCliente(),
                         sucursalFiscal.getDE_DIRE_ForJSON()+"("+dist_fiscal.getNO_DIST_ForJSON()+" - "+prov_fiscal.getNO_PROV_ForJSON()+" - "+depa_fiscal.getNO_DEPA_ForJSON()+")",
                         cliente.getDE_NUME_DOCU_ForJSON(),
                         numeroRecibo,
                         glosa,
                         getStringOrCero(valor_venta.toString()),
                         getStringOrCero(igv.toString()),
                         getStringOrCero(total.toString()),
                         negocio.getCO_NEGO_ForJSON(),
                         negocio.getMonedaFacturacion().getNO_MONE_FACT_ForJSON(),
                         tipo_servicio, //TIPO DE SERVICIO 1:INTERNET, 2:DATOS
                     }); 
               }
           }
        }
    }
    
}
