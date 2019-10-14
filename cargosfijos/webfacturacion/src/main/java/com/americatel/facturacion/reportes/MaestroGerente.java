/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.reportes;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletResponse;

import com.americatel.facturacion.fncs.ExcelWriter;
import com.americatel.facturacion.models.BOLE;
import com.americatel.facturacion.models.CIER;
import com.americatel.facturacion.models.CIER_DATA_PLAN;
import com.americatel.facturacion.models.CIER_DATA_SERV_SUPL;
import com.americatel.facturacion.models.CIER_DATA_SERV_UNIC;
import com.americatel.facturacion.models.CIER_DATA_SUCU;
import com.americatel.facturacion.models.DIST;
import com.americatel.facturacion.models.FACT;
import com.americatel.facturacion.models.NEGO_SUCU;
import com.americatel.facturacion.models.NEGO_SUCU_PLAN;
import com.americatel.facturacion.models.PROD;
import com.americatel.facturacion.models.RECI;

/**
 *
 * @author crodas
 */
public class MaestroGerente {
    private List<CIER> listaCier;
    private Integer NU_ANO;
    private Integer NU_PERI;
    private ExcelWriter excel;
    public MaestroGerente(List<CIER> listaCier,Integer COD_PROD_PADRE,Integer COD_SERVICIO){
        this.listaCier=listaCier;
        if (listaCier.size()>0){
            this.NU_ANO=listaCier.get(0).getNU_ANO_ForJSON();
            this.NU_PERI=listaCier.get(0).getNU_PERI_ForJSON();
        }
               
        this.excel=new ExcelWriter();
        this.excel.addHoja("Reporte Gerentes");               
                
        this.calcular(COD_PROD_PADRE,COD_SERVICIO);
       
                        
    }
    
    public void save(HttpServletResponse response) {
        response.setHeader("Content-Type", "application/vnd.ms-excel");
        response.addHeader("Content-Disposition", "attachment; filename=\"Reportes_Gerentes_"+NU_ANO+"_"+NU_PERI+"\".xls" );
        this.excel.save(response);
    }
        
    public String getStringOrCero(String cadena){
        if (cadena!=null){
            if (!cadena.trim().equals(""))
                return cadena;
        }
        return "0";
    }    

    private void calcular(Integer COD_PROD_PADRE, Integer COD_SERVICIO) {
    	try
    	{// Se quito la columna descuento a pedido de facturacion 19-02-2016
        this.excel.writeCols(new Object[]{"TIPO DOCUMENTO","NRO DOCUMENTO","FECHA EMISION","COD. CLIENTE ","NEGOCIO","CLIENTE ","DIRECCION FISCAL","RUC","DIRECCION CORRESPONDENCA","DISTRITO CORRESPONDENCIA","PROVINCIA CORRESPONDENCIA","DEPARTAMENTO CORRESPONDENCIA","DIRECCION INSTALACION","TIPO SERVICIO ","MEDIO ","MONEDA ","INSTALACION","SERV MENSUAL","ALQ EQUIPOS","OTROS","SUB TOTAL","IGV","TOTAL FACTURA ","FECHA VCTO.","DETALLE DE SERVICIO","PERIODO","VELOCIDAD BAJADA","VELOCIDAD SUBIDA","PUNTOS","CLIENTE NUEVOS","DETRACCION","AJUSTE AFECTO IGV","AJUSTE NO AFECTO IGV","OBSERVACI\u00d3N","SERVICIO"});
        for(CIER cier:listaCier){
            List<RECI> recibos=cier.getRecibos();
            System.out.println("cantidad RECIBOS :" + recibos.size());
            List<BOLE> boletas;
            List<CIER_DATA_PLAN> data_planes_cobrados_en_cierre=cier.getDataPlanesCobrados_forMaestroGerentes();
            List<CIER_DATA_SERV_SUPL> data_serv_spl_cobrados_en_cierre=cier.getDataServiciosSuplCobrados_forMaestroGerentes();
            List<CIER_DATA_SERV_UNIC> data_serv_unic_cobrados_en_cierre=cier.getDataServiciosUnicCobrados_forMaestroGerentes();
            Map<Long,CIER_DATA_PLAN> data_detalle_del_servicio=new TreeMap<Long, CIER_DATA_PLAN>();
            Map<Long,CIER_DATA_SERV_SUPL> data_detalle_del_servicio_supl=new TreeMap<Long, CIER_DATA_SERV_SUPL>();
            Map<Long,CIER_DATA_SERV_SUPL> data_detalle_del_servicio_supl_fact=new TreeMap<Long, CIER_DATA_SERV_SUPL>();
            Map<Long,CIER_DATA_SERV_UNIC> data_detalle_del_servicio_unic=new TreeMap<Long, CIER_DATA_SERV_UNIC>();
            Map<Long,CIER_DATA_SERV_UNIC> data_detalle_del_servicio_unic_fact=new TreeMap<Long, CIER_DATA_SERV_UNIC>();
            //Map<Long,Integer> cantidad_planes_por_recibos=new TreeMap<Long, Integer>();
            
            List<NEGO_SUCU_PLAN> nego_sucu_planes=cier.getNEGO_SUCU_PLAN_forMaestroGerentes();
            Map<Integer,NEGO_SUCU_PLAN> map_nego_sucu_planes=new TreeMap<Integer, NEGO_SUCU_PLAN>();
            Map<Integer,CIER_DATA_SERV_SUPL> map_nego_sucu_serv_supl=new TreeMap<Integer, CIER_DATA_SERV_SUPL>();
            
            
            
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
                }                    
            }
    
            for (CIER_DATA_SERV_SUPL cier_data_serv_supl : data_serv_spl_cobrados_en_cierre){
                if (cier_data_serv_supl.getCO_RECI() != null){
                    if (!data_detalle_del_servicio_supl.containsKey(cier_data_serv_supl.getCO_RECI())){
                        data_detalle_del_servicio_supl.put(cier_data_serv_supl.getCO_RECI(), cier_data_serv_supl);
                    }
                }
                if (cier_data_serv_supl.getCO_FACT() != null){
                    if (!data_detalle_del_servicio_supl_fact.containsKey(cier_data_serv_supl.getCO_FACT())){
                        data_detalle_del_servicio_supl_fact.put(cier_data_serv_supl.getCO_FACT(), cier_data_serv_supl);
                    }
                }
            }
            
            for (CIER_DATA_SERV_UNIC cier_data_serv_unic : data_serv_unic_cobrados_en_cierre){
                if (cier_data_serv_unic.getCO_RECI()!=null){
                    if (!data_detalle_del_servicio_unic.containsKey(cier_data_serv_unic.getCO_RECI())){
                        data_detalle_del_servicio_unic.put(cier_data_serv_unic.getCO_RECI(), cier_data_serv_unic);
                    }
                }
                if (cier_data_serv_unic.getCO_FACT()!=null){
                    if (!data_detalle_del_servicio_unic_fact.containsKey(cier_data_serv_unic.getCO_FACT())){
                        data_detalle_del_servicio_unic_fact.put(cier_data_serv_unic.getCO_FACT(), cier_data_serv_unic);
                    }
                }
            }
            
            for (NEGO_SUCU_PLAN nego_sucu_plan : nego_sucu_planes){
                if (!map_nego_sucu_planes.containsKey(nego_sucu_plan.getCO_NEGO_SUCU_PLAN_ForJSON())){
                    map_nego_sucu_planes.put(nego_sucu_plan.getCO_NEGO_SUCU_PLAN_ForJSON(), nego_sucu_plan);
                }
            }            
            
            PROD prod=cier.getProducto();
            String velocidad_subida="";
            String velocidad_bajada="";
            String medio="";
            Boolean ST_BACKUP_BU;
            Boolean RELACIONADO_TI;
            String nombrePlan = "";
//            String servicioReporte="";
            String tipo_servicio="";
            String detalle_servicio="";
            Integer cantidad_puntos=0;
            //Integer cantidad_sucursales=0;
            CIER_DATA_PLAN cier_data_plan=null;
            CIER_DATA_SERV_SUPL cier_data_serv_supl=null;
            CIER_DATA_SERV_UNIC cier_data_serv_unic=null;
            NEGO_SUCU_PLAN nego_sucu_plan=null;
            int cont = 0;
            for(RECI recibo : recibos){
            	//probar reporte recibo 5188192
            //	if(recibo.getCO_NEGO_ForJSON() == 5188192)
            //	{	
            	
            	cont +=1;
            	System.out.println("recibo"+cont);
                DIST dist_corr=recibo.getDist_Corr_ForJSON();
                DIST dist_fisc=recibo.getDist_Fisc_ForJSON();
                DIST dist_inst=recibo.getDist_Inst_ForJSON();
                velocidad_subida="";
                velocidad_bajada="";
                medio="";
                cier_data_plan=null;
                nego_sucu_plan=null;
                ST_BACKUP_BU=false;
                RELACIONADO_TI=false;
                nombrePlan="";
                detalle_servicio=recibo.getDetalleServicio();
                cantidad_puntos=recibo.getCantidad_sucursales_facturaron();
                
                if (data_detalle_del_servicio.containsKey(recibo.getCO_RECI_ForJSON())){
                    cier_data_plan=data_detalle_del_servicio.get(recibo.getCO_RECI_ForJSON());
//                    string_datelle_servicio=cier_data_plan.getDE_NOMB();
                    if (map_nego_sucu_planes.containsKey(cier_data_plan.getCO_NEGO_SUCU_PLAN())){
                        nego_sucu_plan=map_nego_sucu_planes.get(cier_data_plan.getCO_NEGO_SUCU_PLAN());
                        velocidad_bajada=nego_sucu_plan.getDE_VELO_BAJA_ForJSON();
                        velocidad_subida=nego_sucu_plan.getDE_VELO_SUBI_ForJSON();
                        medio=nego_sucu_plan.getPLAN().getPLAN_MEDI_INST_ForJSON().getNO_PLAN_MEDI_INST_ForJSON();
                        ST_BACKUP_BU=nego_sucu_plan.getPLAN().getST_BACKUP_BU_ForJSON();
                        RELACIONADO_TI=nego_sucu_plan.getPLAN().getRELACIONADO_TI_ForJSON();
                        nombrePlan = nego_sucu_plan.getPLAN().getNO_PLAN_ForJSON();
                    }
                    //cantidad_sucursales=cantidad_planes_por_recibos.get(recibo.getCO_RECI_ForJSON());
                } else {
                    //Recibos que generaron solo diferenciales
                    cier_data_plan=recibo.getPlanFromDiferencial();
                    cantidad_puntos=recibo.getCantidadPuntos();
                    if (cier_data_plan!=null){
                        detalle_servicio=cier_data_plan.getDE_NOMB();
                        if (map_nego_sucu_planes.containsKey(cier_data_plan.getCO_NEGO_SUCU_PLAN())){
                            nego_sucu_plan=map_nego_sucu_planes.get(cier_data_plan.getCO_NEGO_SUCU_PLAN());
                            velocidad_bajada=nego_sucu_plan.getDE_VELO_BAJA_ForJSON();
                            velocidad_subida=nego_sucu_plan.getDE_VELO_SUBI_ForJSON();
                            medio=nego_sucu_plan.getPLAN().getPLAN_MEDI_INST_ForJSON().getNO_PLAN_MEDI_INST_ForJSON();
                            ST_BACKUP_BU=nego_sucu_plan.getPLAN().getST_BACKUP_BU_ForJSON();
                            RELACIONADO_TI=nego_sucu_plan.getPLAN().getRELACIONADO_TI_ForJSON();
                            nombrePlan = nego_sucu_plan.getPLAN().getNO_PLAN_ForJSON();
                        }
                    }
                }
                                
//                servicioReporte = getNombreServicio(COD_PROD_PADRE,prod.getCO_PROD_ForJSON());  
                tipo_servicio = obtenerTipoServicio(COD_PROD_PADRE,ST_BACKUP_BU,prod.getCO_PROD_ForJSON(),RELACIONADO_TI,nombrePlan);
                
                
                Double neto=com.americatel.facturacion.fncs.Numero.redondear(recibo.getIM_NETO_ForJSON()+recibo.getIM_AJUS_SIGV_ForJSON(),2);
                Double servicios_mensuales=com.americatel.facturacion.fncs.Numero.redondear(recibo.getIM_SERV_MENS_ForJSON()+recibo.getIM_DIF_CARGO_FIJO_ForJSON(),2);
                //System.out.println("NEGOCIO RECIBO "+recibo.getCO_RECI_ForJSON()+ ": " + recibo.getCO_NEGO_ForJSON());
                // detalle_servicio=recibo.getPlanDescription_Recibo().getDE_NOMB();
                if (detalle_servicio == "" || detalle_servicio == null )
                {
                	detalle_servicio=recibo.getPlanDescription_SS().DE_NOMB();
                }
                
                this.excel.writeCols(new Object[]{
                    "RECIBO",
                    recibo.getCO_RECI_ForJSON(),
                    recibo.getFE_EMIS_ForJSON(),
                    recibo.getDE_CODI_BUM_ForJSON(),
                    recibo.getCO_NEGO_ForJSON(),
                    recibo.getNO_CLIE_ForJSON(),
                    recibo.getDE_DIRE_FISC_ForJSON()+","+dist_fisc.getNO_DIST_ForJSON()+" - "+dist_fisc.getPROV_ForJSON().getNO_PROV_ForJSON()+" - "+dist_fisc.getPROV_ForJSON().getDEPA_ForJSON().getNO_DEPA_ForJSON(),
                    recibo.getDE_NUME_DOCU_ForJSON(),
                    recibo.getDE_DIRE_CORR_ForJSON(),
                    dist_corr.getNO_DIST_ForJSON(),	
                    dist_corr.getPROV_ForJSON().getNO_PROV_ForJSON(),
                    dist_corr.getPROV_ForJSON().getDEPA_ForJSON().getNO_DEPA_ForJSON(),
                    recibo.getDE_DIRE_INST_ForJSON()+"("+dist_inst.getNO_DIST_ForJSON()+" - "+dist_inst.getPROV_ForJSON().getNO_PROV_ForJSON()+" - "+dist_inst.getPROV_ForJSON().getDEPA_ForJSON().getNO_DEPA_ForJSON()+")",
                    tipo_servicio, //TIPO DE SERVICIO 1:INTERNET, 2:DATOS
                    medio,
                    recibo.getNO_MONE_FACT_ForJSON(),
                    getStringOrCero(recibo.getIM_INST_ForJSON().toString()),
                    getStringOrCero(servicios_mensuales.toString()),
                    getStringOrCero(recibo.getIM_ALQU_ForJSON().toString()),
                    getStringOrCero(recibo.getIM_OTRO_ForJSON().toString()),
//                    getStringOrCero(recibo.getIM_DESC_ForJSON().toString()), -- DESCUENTO
                    getStringOrCero(neto.toString()), // A solicitud al neto se le resta ajuste afecto igv
                    getStringOrCero(recibo.getIM_IGV_ForJSON().toString()),
//                    getStringOrCero(recibo.getIM_TOTA_ForJSON().toString()),
                    
                    //getStringOrCero(String.valueOf(recibo.getIM_IGV_ForJSON() + neto)),
                    com.americatel.facturacion.fncs.Numero.redondear(recibo.getIM_IGV_ForJSON() + neto , 2)  + recibo.getIM_AJUS_NIGV_ForJSON(),				
					
                    recibo.getFE_VENC_ForJSON(),
                    detalle_servicio,
                    recibo.getDE_PERI_ForJSON(),
                    velocidad_bajada,
                    velocidad_subida,
                    cantidad_puntos,
                    recibo.isClienteNuevo() ? "SI":"NO", // Indica si el negocio es nuevo.Un negocio es nuevo cuando factura por primera vez
                    		
//                    recibo.isAfectoDetraccion() ?"SI":"NO",
                    "NO", // Ningun recibo tiene detraccion
                    Math.abs(recibo.getIM_AJUS_SIGV_ForJSON()),
                    Math.abs(recibo.getIM_AJUS_NIGV_ForJSON()),
                    "",
//                    cier.getNombreServicio(COD_SERVICIO)
                    prod.getNO_PROD_ForJSON()
                });  
                //System.out.println(" ----- FIN NEGOCIO RECIBO "+recibo.getCO_RECI_ForJSON()+ ": " + recibo.getCO_NEGO_ForJSON());
            
            //}
            
            }
            
            boletas = cier.getBoletas();
            prod=cier.getProducto();
            //System.out.println("cantidad BOLETAS :" + boletas.size());
            
            for(BOLE boleta : boletas){
                DIST dist_corr=boleta.getDist_Corr_ForJSON();
                DIST dist_fisc=boleta.getDist_Fisc_ForJSON();
                DIST dist_inst=boleta.getDist_Inst_ForJSON();
                velocidad_subida="";
                velocidad_bajada="";
                medio="";
                cier_data_plan=null;
                nego_sucu_plan=null;
                ST_BACKUP_BU=false;
                RELACIONADO_TI=false;
                nombrePlan="";
                
                detalle_servicio=boleta.getDetalleServicio();
                cantidad_puntos=boleta.getCantidad_sucursales_facturaron();
                
                if (data_detalle_del_servicio.containsKey(boleta.getCO_BOLE_ForJSON())){
                    cier_data_plan=data_detalle_del_servicio.get(boleta.getCO_BOLE_ForJSON());
//                    string_datelle_servicio=cier_data_plan.getDE_NOMB();
                    if (map_nego_sucu_planes.containsKey(cier_data_plan.getCO_NEGO_SUCU_PLAN())){
                        nego_sucu_plan=map_nego_sucu_planes.get(cier_data_plan.getCO_NEGO_SUCU_PLAN());
                        velocidad_bajada=nego_sucu_plan.getDE_VELO_BAJA_ForJSON();
                        velocidad_subida=nego_sucu_plan.getDE_VELO_SUBI_ForJSON();
                        medio=nego_sucu_plan.getPLAN().getPLAN_MEDI_INST_ForJSON().getNO_PLAN_MEDI_INST_ForJSON();
                        ST_BACKUP_BU=nego_sucu_plan.getPLAN().getST_BACKUP_BU_ForJSON();
                        RELACIONADO_TI=nego_sucu_plan.getPLAN().getRELACIONADO_TI_ForJSON();
                        nombrePlan = nego_sucu_plan.getPLAN().getNO_PLAN_ForJSON();
                    }
                    //cantidad_sucursales=cantidad_planes_por_recibos.get(recibo.getCO_RECI_ForJSON());
                } else {
                    //Recibos que generaron solo diferenciales
                    cier_data_plan=boleta.getPlanFromDiferencial();
                    cantidad_puntos=boleta.getCantidadPuntos();
                    if (cier_data_plan!=null){
                        detalle_servicio=cier_data_plan.getDE_NOMB();
                        if (map_nego_sucu_planes.containsKey(cier_data_plan.getCO_NEGO_SUCU_PLAN())){
                            nego_sucu_plan=map_nego_sucu_planes.get(cier_data_plan.getCO_NEGO_SUCU_PLAN());
                            velocidad_bajada=nego_sucu_plan.getDE_VELO_BAJA_ForJSON();
                            velocidad_subida=nego_sucu_plan.getDE_VELO_SUBI_ForJSON();
                            medio=nego_sucu_plan.getPLAN().getPLAN_MEDI_INST_ForJSON().getNO_PLAN_MEDI_INST_ForJSON();
                            ST_BACKUP_BU=nego_sucu_plan.getPLAN().getST_BACKUP_BU_ForJSON();
                            RELACIONADO_TI=nego_sucu_plan.getPLAN().getRELACIONADO_TI_ForJSON();
                            nombrePlan = nego_sucu_plan.getPLAN().getNO_PLAN_ForJSON();
                        }
                    }
                }
                
//                servicioReporte = getNombreServicio(COD_PROD_PADRE,prod.getCO_PROD_ForJSON());
                 
                tipo_servicio = obtenerTipoServicio(COD_PROD_PADRE,ST_BACKUP_BU,prod.getCO_PROD_ForJSON(),RELACIONADO_TI,nombrePlan);
                
                
                Double neto=com.americatel.facturacion.fncs.Numero.redondear(boleta.getIM_NETO_ForJSON()+boleta.getIM_AJUS_SIGV_ForJSON(),2);
                Double servicios_mensuales=com.americatel.facturacion.fncs.Numero.redondear(boleta.getIM_SERV_MENS_ForJSON()+boleta.getIM_DIF_CARGO_FIJO_ForJSON(),2);
                
                //System.out.println("NEGOCIO BOLETA "+boleta.getCO_BOLE_ForJSON()+ ": " + boleta.getCO_NEGO_ForJSON());
                detalle_servicio=boleta.getPlanDescription().getDE_NOMB();
                
                this.excel.writeCols(new Object[]{
                    "BOLETA",
                    boleta.getCO_BOLE_ForJSON(),
                    boleta.getFE_EMIS_ForJSON(),
                    boleta.getDE_CODI_BUM_ForJSON(),
                    boleta.getCO_NEGO_ForJSON(),
                    boleta.getNO_CLIE_ForJSON(),
                    boleta.getDE_DIRE_FISC_ForJSON()+","+dist_fisc.getNO_DIST_ForJSON()+" - "+dist_fisc.getPROV_ForJSON().getNO_PROV_ForJSON()+" - "+dist_fisc.getPROV_ForJSON().getDEPA_ForJSON().getNO_DEPA_ForJSON(),
                    boleta.getDE_NUME_DOCU_ForJSON(),
                    boleta.getDE_DIRE_CORR_ForJSON(),
                    dist_corr.getNO_DIST_ForJSON(),
                    dist_corr.getPROV_ForJSON().getNO_PROV_ForJSON(),
                    dist_corr.getPROV_ForJSON().getDEPA_ForJSON().getNO_DEPA_ForJSON(),
                    boleta.getDE_DIRE_INST_ForJSON()+"("+dist_inst.getNO_DIST_ForJSON()+" - "+dist_inst.getPROV_ForJSON().getNO_PROV_ForJSON()+" - "+dist_inst.getPROV_ForJSON().getDEPA_ForJSON().getNO_DEPA_ForJSON()+")",
                    tipo_servicio, //TIPO DE SERVICIO 1:INTERNET, 2:DATOS
                    medio,
                    boleta.getNO_MONE_FACT_ForJSON(),
                    getStringOrCero(boleta.getIM_INST_ForJSON().toString()),
                    getStringOrCero(servicios_mensuales.toString()),
                    getStringOrCero(boleta.getIM_ALQU_ForJSON().toString()),
                    getStringOrCero(boleta.getIM_OTRO_ForJSON().toString()),
//                    getStringOrCero(recibo.getIM_DESC_ForJSON().toString()), -- DESCUENTO
                    getStringOrCero(neto.toString()), // A solicitud al neto se le resta ajuste afecto igv
                    getStringOrCero(boleta.getIM_IGV_ForJSON().toString()),
                    com.americatel.facturacion.fncs.Numero.redondear(boleta.getIM_TOTA_ForJSON() + boleta.getIM_AJUS_SIGV_ForJSON() + boleta.getIM_AJUS_NIGV_ForJSON() , 2),
                    
                    boleta.getFE_VENC_ForJSON(),
                    detalle_servicio,
                    boleta.getDE_PERI_ForJSON(),
                    velocidad_bajada,
                    velocidad_subida,
                    cantidad_puntos,
                    boleta.isClienteNuevo() ? "SI":"NO", // Indica si el negocio es nuevo.Un negocio es nuevo cuando factura por primera vez
//                    recibo.isAfectoDetraccion() ?"SI":"NO",
                    "NO", // Ningun recibo tiene detraccion
                    Math.abs(boleta.getIM_AJUS_SIGV_ForJSON()),
                    Math.abs(boleta.getIM_AJUS_NIGV_ForJSON()),
                    "",
//                    cier.getNombreServicio(COD_SERVICIO)
                    prod.getNO_PROD_ForJSON()
                });                
            }
            
            
            
            List<FACT> facturas=cier.getFacturas();
            
            //System.out.println("cantidad FACTURAS :" + facturas.size());
            boolean generoRecibo=false;
            String det_servicio;
            for(FACT factura : facturas){
                DIST dist_corr=factura.getDist_Corr_ForJSON();
                DIST dist_fisc=factura.getDist_Fisc_ForJSON();
                DIST dist_inst=factura.getDist_Inst_ForJSON();
                Integer cantidad_sucursales_fact=null;
                ST_BACKUP_BU=false;
                cier_data_plan=null;
                nego_sucu_plan=null;
                cier_data_serv_supl=null;
                cier_data_serv_unic=null;
                det_servicio="";
                velocidad_subida="";
                velocidad_bajada="";
                medio="";
                generoRecibo=false;
                RELACIONADO_TI=false;
                nombrePlan="";
                
                for(RECI recibo : recibos){
                    if (recibo.getCO_NEGO_ForJSON().equals(factura.getCO_NEGO_ForJSON())){
                        generoRecibo=true;
                        cantidad_sucursales_fact=recibo.getCantidad_sucursales_facturaron();
                        if (data_detalle_del_servicio.containsKey(recibo.getCO_RECI_ForJSON())){
                            cier_data_plan=data_detalle_del_servicio.get(recibo.getCO_RECI_ForJSON());
                            if (map_nego_sucu_planes.containsKey(cier_data_plan.getCO_NEGO_SUCU_PLAN())){
                                nego_sucu_plan=map_nego_sucu_planes.get(cier_data_plan.getCO_NEGO_SUCU_PLAN());
                                velocidad_bajada=nego_sucu_plan.getDE_VELO_BAJA_ForJSON();
                                velocidad_subida=nego_sucu_plan.getDE_VELO_SUBI_ForJSON();
                                medio=nego_sucu_plan.getPLAN().getPLAN_MEDI_INST_ForJSON().getNO_PLAN_MEDI_INST_ForJSON();
                                ST_BACKUP_BU=nego_sucu_plan.getPLAN().getST_BACKUP_BU_ForJSON();
                                RELACIONADO_TI=nego_sucu_plan.getPLAN().getRELACIONADO_TI_ForJSON();
                                nombrePlan = nego_sucu_plan.getPLAN().getNO_PLAN_ForJSON();
                            }
                            break;
                            //cantidad_sucursales=cantidad_planes_por_recibos.get(recibo.getCO_RECI_ForJSON());
                        }
                    }
                }
                
                if (data_detalle_del_servicio_supl_fact.containsKey(factura.getCO_FACT_ForJSON())){
                    cier_data_serv_supl=data_detalle_del_servicio_supl_fact.get(factura.getCO_FACT_ForJSON());
                    det_servicio=cier_data_serv_supl.getDE_NOMB();
                } else {
                    if (data_detalle_del_servicio_unic_fact.containsKey(factura.getCO_FACT_ForJSON())){
                        cier_data_serv_unic=data_detalle_del_servicio_unic_fact.get(factura.getCO_FACT_ForJSON());
                        det_servicio=cier_data_serv_unic.getDE_NOMB();
                        /*
                        29-11-2016: Se debe tener las velocidades de bajada y de subida, medio y arrendamiento
                        */
                        if (!generoRecibo){
                            cantidad_sucursales_fact=factura.getCantidad_sucursales_facturaron();
                            CIER_DATA_SUCU cier_data_sucu=cier_data_serv_unic.getCIER_DATA_SUCU();
                            NEGO_SUCU nego_Sucu = new NEGO_SUCU();
                            nego_Sucu.setCO_NEGO_SUCU(cier_data_sucu.getCO_NEGO_SUCU());
                            
                            //obtiene los planes desactivados con fecha fin
                            NEGO_SUCU_PLAN nsp=nego_Sucu.getUltimoPlanDesactivado();

                            // si hay planes desactivados
                            if(nsp != null){
                            velocidad_bajada=nsp.getDE_VELO_BAJA_ForJSON();
                            velocidad_subida=nsp.getDE_VELO_SUBI_ForJSON();
                            medio=nsp.getPLAN().getPLAN_MEDI_INST_ForJSON().getNO_PLAN_MEDI_INST_ForJSON();
                            ST_BACKUP_BU=nsp.getPLAN().getST_BACKUP_BU_ForJSON();	
                            RELACIONADO_TI=nego_sucu_plan.getPLAN().getRELACIONADO_TI_ForJSON();
                            nombrePlan = nego_sucu_plan.getPLAN().getNO_PLAN_ForJSON();
                            } else
                            {
                            velocidad_bajada="";
                            velocidad_subida="";
                            medio="";
                            ST_BACKUP_BU=false;
                            RELACIONADO_TI= false;
                            nombrePlan = "";
                            }
                        }
                    }
                }
                                 
//                servicioReporte = getNombreServicio(COD_PROD_PADRE,prod.getCO_PROD_ForJSON());
                                
                Double servicios_mensuales=com.americatel.facturacion.fncs.Numero.redondear(factura.getIM_SERV_MENS_ForJSON()+factura.getIM_DIF_CARGO_FIJO_ForJSON(),2);
                //System.out.println("NEGOCIO FACTURA "+factura.getCO_FACT_ForJSON()+ ": " + factura.getCO_NEGO_ForJSON());
                this.excel.writeCols(new Object[]{
                   "FACTURA",
                    factura.getCO_FACT_ForJSON(),
                    factura.getFE_EMIS_ForJSON(),
                    factura.getDE_CODI_BUM_ForJSON(),
                    factura.getCO_NEGO_ForJSON(),
                    factura.getNO_CLIE_ForJSON(),
                    factura.getDE_DIRE_FISC_ForJSON()+"("+dist_fisc.getPROV_ForJSON().getDEPA_ForJSON().getNO_DEPA_ForJSON()+" - "+dist_fisc.getPROV_ForJSON().getNO_PROV_ForJSON()+"-"+dist_fisc.getNO_DIST_ForJSON()+")",
                    factura.getDE_NUME_DOCU_ForJSON(),
                    factura.getDE_DIRE_CORR_ForJSON(),
                    dist_corr.getNO_DIST_ForJSON(),
                    dist_corr.getPROV_ForJSON().getNO_PROV_ForJSON(),
                    dist_corr.getPROV_ForJSON().getDEPA_ForJSON().getNO_DEPA_ForJSON(),
                    factura.getDE_DIRE_INST_ForJSON()+"("+dist_inst.getNO_DIST_ForJSON()+" - "+dist_inst.getPROV_ForJSON().getNO_PROV_ForJSON()+" - "+dist_inst.getPROV_ForJSON().getDEPA_ForJSON().getNO_DEPA_ForJSON()+")",
                    tipo_servicio, //TIPO DE SERVICIO 1:INTERNET, 2:DATOS
                    medio,
                    factura.getNO_MONE_FACT_ForJSON(),
                    getStringOrCero(factura.getIM_INST_ForJSON().toString()),
                    getStringOrCero(servicios_mensuales.toString()),
                    getStringOrCero(factura.getIM_ALQU_ForJSON().toString()),
                    getStringOrCero(factura.getIM_OTRO_ForJSON().toString()),
//                    0, -- DESCUENTO
                    factura.getIM_VALO_VENT_NETO_ForJSON(),
                    factura.getIM_IGV_ForJSON(),
                    //factura.getIM_TOTA_ForJSON(),
                    //com.americatel.facturacion.fncs.Numero.redondear(factura.getIM_TOTA_ForJSON() + Math.abs(factura.getIM_AJUS_NIGV_ForJSON()) , 2),                    
                    com.americatel.facturacion.fncs.Numero.redondear(factura.getIM_VALO_VENT_NETO_ForJSON() + factura.getIM_IGV_ForJSON() - Math.abs(factura.getIM_AJUS_NIGV_ForJSON()) , 2),
                    factura.getFE_VENC_ForJSON(),
                    det_servicio, // del recibo relacionado
                    factura.getDE_PERI_ForJSON(),//periodo
                    velocidad_bajada,
                    velocidad_subida,
                    cantidad_sucursales_fact!=null ? cantidad_sucursales_fact:"", // cantidad de sucursales
                    factura.isClienteNuevo() ? "SI":"NO", // Indica si el negocio es nuevo.Un negocio es nuevo cuando factura por primera vez
                    factura.getST_AFECTO_DETRACCION_ForJSON() ?"SI":"NO",
                    0, // ajuste afecto a igv
                    Math.abs(factura.getIM_AJUS_NIGV_ForJSON()), // ajuste no afecto a igv
                    "",
//                    cier.getNombreServicio(COD_SERVICIO)
                    prod.getNO_PROD_ForJSON()
                });
            }
        }
	    }
        catch(Exception e)
        {
        	System.out.print("ERROR ...........................................");
        	System.out.print(e);
        }
    }
    
    
//    public String getNombreServicio(Integer COD_PRODUCTO, Integer COD_SERVICIO){
//    
//            String nombreServicio = "";
//        
//            switch (COD_PRODUCTO) {
//                 
//                case 1: if(COD_SERVICIO==1){
//                           nombreServicio="Cargos Fijos Satelital - INTERNET";
//                        }else if(COD_SERVICIO==2){
//                           nombreServicio="Cargos Fijos Satelital - DATOS";
//                        }
//                        break;
//                case 2: if(COD_SERVICIO==3){
//                           nombreServicio="Internet Dedicado";
//                        }else if(COD_SERVICIO==4){
//                           nombreServicio="Datos Dedicados";
//                        }else if (COD_SERVICIO==17){
//                        	nombreServicio="Hosting";
//		                }
//                        break;
//                case 3: if(COD_SERVICIO==5){
//                           nombreServicio="Internet Migracion Ingenio";
//                        }else if(COD_SERVICIO==6){
//                           nombreServicio="Datos Migracion Ingenio";
//                        }
//                        break;
//                case 4: if(COD_SERVICIO==7){
//                           nombreServicio="TI-HOUSING";
//                        }else if(COD_SERVICIO==8){
//                           nombreServicio="TI-HOS-COM";
//                        }else if(COD_SERVICIO==9){
//                           nombreServicio="TI-HOS-DED";
//                        }else if(COD_SERVICIO==10){
//                           nombreServicio="TI-CB-SAAS";
//                        }else if(COD_SERVICIO==11){
//                           nombreServicio="TI-CB-OF365";
//                        }
//                        else if(COD_SERVICIO==18){
//                            nombreServicio="TI-CB-IAAS";
//                         }
//                        else if(COD_SERVICIO==19){
//                            nombreServicio="TI-CB-STAAS";
//                         }
//                        else if(COD_SERVICIO==20){
//                            nombreServicio="TI-CB-MAS";
//                         }
//                        else if(COD_SERVICIO==21){
//                            nombreServicio="TI-CB-CRM";
//                         }
//                        else if(COD_SERVICIO==22){
//                            nombreServicio="TI-CB-SAAS-SAS";
//                         }
//                        else if(COD_SERVICIO==23){
//                            nombreServicio="TI - HOS - ONE SHOT";
//                         }
//                        else if(COD_SERVICIO==24){
//                            nombreServicio="TI - CB - ONE SHOT";
//                         }
//                        else if(COD_SERVICIO==25){
//                            nombreServicio="TI - HOU - ONE SHOT";
//                         }
//                        break;
//                case 5: if(COD_SERVICIO==12){
//                           nombreServicio="Internet Dedicados Mayorista";
//                        }else if(COD_SERVICIO==13){
//                           nombreServicio="Datos Dedicado Mayorista";
//                        }else if(COD_SERVICIO==14){
//                           nombreServicio="Ducteria";
//                        }else if(COD_SERVICIO==15){
//                           nombreServicio="Colocacion";
//                        }else if(COD_SERVICIO==16){
//                           nombreServicio="Otros Servicios";
//                        }
//                        break;
//                default: nombreServicio = "null";
//                        break;   
//                 
//            }
//        return nombreServicio;
//    }
    
    public String obtenerTipoServicio(Integer codProducto, Boolean stBackupBu, Integer codServicio, Boolean relacionadoTI, String nombrePlan){
    	if(codProducto == 1){        
    		if (!stBackupBu){
    			if (codServicio==1){
    				return "IPB SAT";
    			} else {
    				return "ADD SAT";
    			}
    		} else {
    			if (codServicio==1){
    				return "IPB SAT - BU";
    			} else {
    				return "ADD SAT - BU";
    			}
    		}
    	}else if(codProducto == 2){  
    		if (codServicio==3){
    			if(relacionadoTI){
    				return "IPB (ICX-TI)";
    			}else{
    				return "IPB";
    			}
			}else if (codServicio==4){
				if(relacionadoTI){
    				return "ADD (ICX-TI)";
    			}else if(!relacionadoTI && "ARRENDAMIENTO DE INFRAESTRUCTURA".equalsIgnoreCase(nombrePlan)){
    				return "ADD - FO";
    			}else{
    				return "ADD";
    			}
			}else if (codServicio==17){
				return "HOS";
			}

    	}else if(codProducto == 3){        

    		if (codServicio==5){
    			return "IPB SAT - INGENIO";
    		}else if (codServicio==6){
    			return "ADD SAT -INGENIO";
    		}
    	}else if(codProducto == 4){   
    		
            if(codServicio == 7){
            	return "HOU";
            }else if(codServicio == 8){
            	return "IAAS - Compartido";
            }else if(codServicio == 9){
            	return "IAAS - Dedicado";
            }else if(codServicio == 10){
            	return "SAAS - CB";
            }else if(codServicio == 11){
            	return "OF365 - CB";
            }else if(codServicio == 18){
            	return "IAAS - CB";
            }else if(codServicio == 19){
            	return "STAAS - CB";
            }else if(codServicio == 20){
            	return "MAS - CB";
            }else if(codServicio == 21){
            	return "CRM - CB";
            }else if(codServicio == 22){
            	return "SAAS - SAS";
            }else if(codServicio == 23){
            	return "HOS - ONE SHOT";
            }else if(codServicio == 24){
            	return "CB - ONE SHOT";
            }else if(codServicio == 25){
            	return "HOU - ONE SHOT";
            }
            
        } else if(codProducto == 5){

    		if (codServicio==12){
    			return "IPB - WHO";
    		}else if(codServicio==13){
    			if(nombrePlan.equalsIgnoreCase("ARRENDAMIENTO DE FIBRA OSCURA")){
    				return "ADD - WHO(FO)";
    			}else{
    				return "ADD - WHO";
    			}                        
    		}else if(codServicio==14){
    			if(nombrePlan.equalsIgnoreCase("SERVICIO DE DUCTERIA")){
    				return "DUC - WHO";
    			}else if(nombrePlan.equalsIgnoreCase("SERVICIO DE CABLEADO")){
    				return "DUC - WHO(SS)";
    			}                                                 
    		}else if(codServicio==15){
    			return "COL - WHO";
    		}else if(codServicio==16){
    			if(nombrePlan.equalsIgnoreCase("SERVICIO DE CROSSCONEXION")){
    				return"OTH - WHO";
    			}else if(nombrePlan.equalsIgnoreCase("OTROS SERVICIOS TECNICOS POST VENTA")){
    				return"OTH - WHO(SS)";
    			}else if(nombrePlan.equalsIgnoreCase("CAPACIDAD DE INTERCONEXI\u00d3N")){
    				return"ICX - WHO";
    			}    
    		}
    	}
    	return "";
    }
}
