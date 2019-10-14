/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.core3;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.americatel.facturacion.Utils.Constante;
import com.americatel.facturacion.fncs.FechaHora;
import com.americatel.facturacion.fncs.fnc;
import com.americatel.facturacion.historial.Historial;
import com.americatel.facturacion.mappers.mapPROD;
import com.americatel.facturacion.mappers.mapSERV_SUPL;
import com.americatel.facturacion.mappers.mapSERV_UNIC;
import com.americatel.facturacion.models.AJUS;
import com.americatel.facturacion.models.BOLE;
import com.americatel.facturacion.models.CIER;
import com.americatel.facturacion.models.CIER_DATA_PLAN;
import com.americatel.facturacion.models.CIER_DATA_SERV_SUPL;
import com.americatel.facturacion.models.CIER_DATA_SERV_UNIC;
import com.americatel.facturacion.models.CIER_DATA_SUCU;
import com.americatel.facturacion.models.CLIE;
import com.americatel.facturacion.models.DEPA;
import com.americatel.facturacion.models.DETALLE_DOCUMENTO;
import com.americatel.facturacion.models.DIST;
import com.americatel.facturacion.models.MONE_FACT;
import com.americatel.facturacion.models.NEGO;
import com.americatel.facturacion.models.NEGO_SUCU;
import com.americatel.facturacion.models.NEGO_SUCU_SERV_UNIC;
import com.americatel.facturacion.models.PROD_PADRE;
import com.americatel.facturacion.models.PROV;
import com.americatel.facturacion.models.RECI;
import com.americatel.facturacion.models.RECI_GLOS;
import com.americatel.facturacion.models.SUCU;
import com.americatel.facturacion.models.TIPO_DOCU;

/**
 *
 * @author crodas
 */
public class Recibo{
	
        final static Logger logger = Logger.getLogger(Recibo.class);
        
        private boolean isBoleta = false;
    
        Date periodo_inicio=null;
        Date periodo_fin=null;
        
        @Autowired mapSERV_UNIC mapServ_Unic;
        @Autowired mapSERV_SUPL mapServ_Supl;
        @Autowired mapPROD mapProd;
        
        List<Date[]> periodos=new ArrayList<Date[]>();
        Map<CIER_DATA_SUCU,Object[]> mi_data_facturar=new HashMap<CIER_DATA_SUCU, Object[]>();
        Map<CIER_DATA_SUCU,List<CIER_DATA_SERV_UNIC>>  mi_servicios_unicos_data=null;
        List<NEGO_SUCU_SERV_UNIC>  mi_servicios_unicos=null;
        Map<CIER_DATA_SUCU,List<NEGO_SUCU_SERV_UNIC>> servicios_unicos=null;
        //para factura
        Map<CIER_DATA_SUCU,List<CIER_DATA_SERV_UNIC>> para_factura_servicios_unicos_data=null; 
        List<NEGO_SUCU_SERV_UNIC> para_factura_servicios_unicos=null; 
        Map<CIER_DATA_SUCU,List<CIER_DATA_SERV_SUPL>> para_factura_servicios_suplementarios_data=null;; 
        Map<NEGO_SUCU,List<RECI_GLOS>> mi_data_recibo=new HashMap<NEGO_SUCU,List<RECI_GLOS>>();
        
        SUCU sucursal_fiscal=null;
        SUCU sucursal_correspondencia=null;
        DIST distrito_fiscal=null;
        DIST distrito_correspondencia=null;
        PROV provincia_fiscal=null;
        PROV provincia_correspondencia=null;
        DEPA departamento_fiscal=null;
        DEPA departamento_correspondencia=null; 
        MONE_FACT moneda_facturacion=null;
        TIPO_DOCU tipo_documento;
        CLIE cliente=null;        
        
        Double saldo_entra=0d;// son valores positivos
        Double saldo_aplica=0d;// son valores positivos
        Double saldo_promocion=0d;//son valores positivos
                
        NEGO nego;
        CIER cier;
        Double sub_total_afecto_detraccion_soles=0d;
        Boolean es_afecto_detraccion=false;
        ProcesoNegocio proceso_negocio=null;
        List<AJUS> ajustes=null;
        List<AJUS> mi_ajustes=null;
        List<AJUS> ajustes_no_aplicados=null;
        Double ajustes_por_redondeo=0d;
        Integer cantidad_sucursales_facturan=0;
        Map<Recibo,Map<CIER_DATA_SUCU,List<CIER_DATA_PLAN>>> planes_diferenciales_por_upgrade=null;
        Long NumeroRecibo=null;
        Long NumeroBoleta=null;
        Double totales[];
        ReciboPdf recibo_pdf=null;
        Boolean generaPdf=null;
        Boolean generaFactura=null;
        public Double getSaldoQueda(){
            Double r=(saldo_entra+saldo_promocion)-saldo_aplica;
            if (r!=0d){
                if (this.getMonedaRecibo()!=null && !this.getMonedaRecibo().equals(this.moneda_facturacion.getCO_MONE_FACT_ForJSON())){
                    if (this.moneda_facturacion.esSoles())
                        r*=this.cier.getNU_TIPO_CAMB_ForJSON();
                    else
                        r/=this.cier.getNU_TIPO_CAMB_ForJSON();
                }
            }
            return redondearPorDefecto(r);
        }
        private Double redondearPorDefecto(Double n){return com.americatel.facturacion.fncs.Numero.redondear(n,2);}
        
        RECI reci_modificado;
        BOLE bole_modificado;
        
        List<RECI_GLOS> glosas;
        public RECI getReci_modificado() {
            return reci_modificado;
        }
        public BOLE getBole_modificado() {
            return bole_modificado;
        }
        public List<RECI_GLOS> getReci_Glosas() {
            return glosas;
        }
        public Recibo(RECI reci, BOLE bole){
            if(reci!=null){
                this.isBoleta = false;
                this.reci_modificado=reci;
                this.NumeroRecibo=reci.getCO_RECI_ForJSON();
                this.periodo_inicio=reci.getFE_INI_ForJSON();
                this.periodo_fin=reci.getFE_FIN_ForJSON();
    //            this.cliente=reci.getClie_ForJSON();
                this.nego=reci.getNego_ForJSON();
                this.distrito_fiscal=reci.getDist_Fisc_ForJSON();
                this.provincia_fiscal=reci.getDist_Fisc_ForJSON().getPROV_ForJSON();
                this.departamento_fiscal=reci.getDist_Fisc_ForJSON().getPROV_ForJSON().getDEPA_ForJSON();
    //            this.sucursal_fiscal=reci.getClie_ForJSON().getSUCU_FISC_ForJSON();
    //            this.distrito_fiscal=reci.getClie_ForJSON().getSUCU_FISC_ForJSON().getDist_ForJSON();
    //            this.provincia_fiscal=reci.getClie_ForJSON().getSUCU_FISC_ForJSON().getDist_ForJSON().getPROV_ForJSON();
    //            this.departamento_fiscal=reci.getClie_ForJSON().getSUCU_FISC_ForJSON().getDist_ForJSON().getPROV_ForJSON().getDEPA_ForJSON();
                this.cier=reci.getCier_ForJSON();
                this.cantidad_sucursales_facturan=reci.getCantidadSucursalesFacturaron();
                this.mi_ajustes=new ArrayList<AJUS>();    
                this.ajustes=new ArrayList<AJUS>();    
                this.mi_servicios_unicos_data=new HashMap<CIER_DATA_SUCU, List<CIER_DATA_SERV_UNIC>>();
                this.glosas=reci.getGlosas();
                for(RECI_GLOS glosa : this.glosas){
                    //cargos los tipos de glosa 4 y 5  que son los ajustes
                    if (glosa.getTI_GLOS_ForJSON().equals(4) || glosa.getTI_GLOS_ForJSON().equals(5) || glosa.getTI_GLOS_ForJSON().equals(6) || glosa.getTI_GLOS_ForJSON().equals(7)){
                        AJUS x=new AJUS();
                        x.setDE_GLOS(glosa.getNO_GLOS_ForJSON());
                        x.setIM_MONT(glosa.getIM_MONT_ForJSON());
                        if (glosa.getTI_GLOS_ForJSON().equals(5))
                            x.setST_AFEC_IGV(false);
                        else
                            x.setST_AFEC_IGV(true);
                        this.mi_ajustes.add(x);
                    }else{
                        if (!this.mi_data_recibo.containsKey(glosa.getNEGO_SUCU_ForJSON())){
                            this.mi_data_recibo.put(glosa.getNEGO_SUCU_ForJSON(), new ArrayList<RECI_GLOS>());                            
                        }                            
                        List<RECI_GLOS> lista=(List<RECI_GLOS>)this.mi_data_recibo.get(glosa.getNEGO_SUCU_ForJSON());
                        lista.add(glosa);
                    }
                }
            }else if(bole != null){
                this.isBoleta = true;
                this.bole_modificado = bole;
                this.NumeroBoleta=bole.getCO_BOLE_ForJSON();
                this.periodo_inicio=bole.getFE_INI_ForJSON();
                this.periodo_fin=bole.getFE_FIN_ForJSON();
    //            this.cliente=reci.getClie_ForJSON();
                this.nego=bole.getNego_ForJSON();
                this.distrito_fiscal=bole.getDist_Fisc_ForJSON();
                this.provincia_fiscal=bole.getDist_Fisc_ForJSON().getPROV_ForJSON();
                this.departamento_fiscal=bole.getDist_Fisc_ForJSON().getPROV_ForJSON().getDEPA_ForJSON();
    //            this.sucursal_fiscal=reci.getClie_ForJSON().getSUCU_FISC_ForJSON();
    //            this.distrito_fiscal=reci.getClie_ForJSON().getSUCU_FISC_ForJSON().getDist_ForJSON();
    //            this.provincia_fiscal=reci.getClie_ForJSON().getSUCU_FISC_ForJSON().getDist_ForJSON().getPROV_ForJSON();
    //            this.departamento_fiscal=reci.getClie_ForJSON().getSUCU_FISC_ForJSON().getDist_ForJSON().getPROV_ForJSON().getDEPA_ForJSON();
                this.cier=bole.getCier_ForJSON();
                this.cantidad_sucursales_facturan=bole.getCantidadSucursalesFacturaron();
                this.mi_ajustes=new ArrayList<AJUS>();    
                this.ajustes=new ArrayList<AJUS>();    
                this.mi_servicios_unicos_data=new HashMap<CIER_DATA_SUCU, List<CIER_DATA_SERV_UNIC>>();
                this.glosas=bole.getGlosas();
                for(RECI_GLOS glosa : this.glosas){
                    //cargos los tipos de glosa 4 y 5  que son los ajustes
                    if (glosa.getTI_GLOS_ForJSON().equals(4) || glosa.getTI_GLOS_ForJSON().equals(5) || glosa.getTI_GLOS_ForJSON().equals(6) || glosa.getTI_GLOS_ForJSON().equals(7)){
                        AJUS x=new AJUS();
                        x.setDE_GLOS(glosa.getNO_GLOS_ForJSON());
                        x.setIM_MONT(glosa.getIM_MONT_ForJSON());
                        if (glosa.getTI_GLOS_ForJSON().equals(5))
                            x.setST_AFEC_IGV(false);
                        else
                            x.setST_AFEC_IGV(true);
                        this.mi_ajustes.add(x);
                    }else{
                        if (!this.mi_data_recibo.containsKey(glosa.getNEGO_SUCU_ForJSON())){
                            this.mi_data_recibo.put(glosa.getNEGO_SUCU_ForJSON(), new ArrayList<RECI_GLOS>());                            
                        }                            
                        List<RECI_GLOS> lista=(List<RECI_GLOS>)this.mi_data_recibo.get(glosa.getNEGO_SUCU_ForJSON());
                        lista.add(glosa);
                    }
                }
            }
            
            this.recalcular_totales();
        }
        public Recibo(ProcesoNegocio proceso_negocio,Date periodo_inicio,Date periodo_fin,Map<CIER_DATA_SUCU,Object[]> listaPlanesySsPorSucursal,Double saldo,Map<CIER_DATA_SUCU,List<NEGO_SUCU_SERV_UNIC>> servicios_unicos,List<AJUS> ajustes,Map<Recibo,Map<CIER_DATA_SUCU,List<CIER_DATA_PLAN>>> planes_diferenciales_por_upgrade,
                CIER cier,NEGO nego,CLIE cliente,SUCU sucursal_fiscal,SUCU sucursal_correspondencia,
                DIST distrito_fiscal,DIST distrito_correspondencia,PROV provincia_fiscal,PROV provincia_correspondencia,
                DEPA departamento_fiscal,DEPA departamento_correspondencia,MONE_FACT moneda_facturacion,
                TIPO_DOCU tipo_documento,
                String descripcionProducto
        ){
            this.sucursal_fiscal=sucursal_fiscal;
            this.sucursal_correspondencia=sucursal_correspondencia;
            this.distrito_fiscal=distrito_fiscal;
            this.distrito_correspondencia=distrito_correspondencia;
            this.provincia_fiscal=provincia_fiscal;
            this.provincia_correspondencia=provincia_correspondencia;
            this.departamento_fiscal=departamento_fiscal;
            this.departamento_correspondencia=departamento_correspondencia;
            this.moneda_facturacion=moneda_facturacion;
            this.tipo_documento=tipo_documento;
            
            
            this.ajustes=ajustes;
            this.mi_ajustes=new ArrayList<AJUS>();
            this.ajustes_no_aplicados=new ArrayList<AJUS>();
            this.proceso_negocio=proceso_negocio;
            this.periodo_inicio=periodo_inicio;
            this.periodo_fin=periodo_fin;
            this.saldo_entra=Math.abs(saldo);//siempre se maneja en positivo
            this.nego=nego;
            this.cier=cier;
            this.cliente=cliente;
            this.servicios_unicos=servicios_unicos;
            this.mi_servicios_unicos_data=new HashMap<CIER_DATA_SUCU, List<CIER_DATA_SERV_UNIC>>();
            this.para_factura_servicios_unicos_data=new HashMap<CIER_DATA_SUCU, List<CIER_DATA_SERV_UNIC>>();
            this.para_factura_servicios_unicos=new ArrayList<NEGO_SUCU_SERV_UNIC>();
            this.mi_servicios_unicos=new ArrayList<NEGO_SUCU_SERV_UNIC>();
            this.para_factura_servicios_suplementarios_data=new HashMap<CIER_DATA_SUCU, List<CIER_DATA_SERV_SUPL>>();
            this.planes_diferenciales_por_upgrade=new HashMap<Recibo, Map<CIER_DATA_SUCU, List<CIER_DATA_PLAN>>>();
            if (planes_diferenciales_por_upgrade!=null){ 
                //Map<CIER_DATA_SUCU, List<CIER_DATA_PLAN>> tmp_planes_diferenciales_por_upgrade=new HashMap<CIER_DATA_SUCU, List<CIER_DATA_PLAN>>();
                //this.planes_diferenciales_por_upgrade.put(this, tmp_planes_diferenciales_por_upgrade);
                for(Entry<Recibo,Map<CIER_DATA_SUCU,List<CIER_DATA_PLAN>>> entry_a : planes_diferenciales_por_upgrade.entrySet()){
                    Map<CIER_DATA_SUCU, List<CIER_DATA_PLAN>> tmp_planes_diferenciales_por_upgrade=new HashMap<CIER_DATA_SUCU, List<CIER_DATA_PLAN>>();
                    this.planes_diferenciales_por_upgrade.put(entry_a.getKey(), tmp_planes_diferenciales_por_upgrade);
                    for(Entry<CIER_DATA_SUCU,List<CIER_DATA_PLAN>> entry_b : entry_a.getValue().entrySet()){                        
                        if (!tmp_planes_diferenciales_por_upgrade.containsKey(entry_b.getKey())){
                            tmp_planes_diferenciales_por_upgrade.put(entry_b.getKey(), new ArrayList<CIER_DATA_PLAN>());
                        }
                        tmp_planes_diferenciales_por_upgrade.get(entry_b.getKey()).addAll(entry_b.getValue());                        
                    }
                }
            }
            Double sub_total_temporal=0d;            
            for(Map.Entry<CIER_DATA_SUCU,Object[]> entry : listaPlanesySsPorSucursal.entrySet()){                
                List<CIER_DATA_SERV_SUPL> sss=(List<CIER_DATA_SERV_SUPL>) entry.getValue()[1];                
                for(CIER_DATA_SERV_SUPL ss : sss){
                    if ( FechaHora.isBetween(periodo_inicio, periodo_fin, ss.getFE_INIC()) ){
                        if (ss.getST_TIPO_COBR()!=null){
                            if (ss.getST_AFEC_DETR()){                            
                                sub_total_afecto_detraccion_soles+=ss.getIM_MONT()* (nego.getCO_MONE_FACT_ForJSON()==2?cier.getNU_TIPO_CAMB_ForJSON():1d);                            
                            }
                        }
                    }
                }
            }
            for(Entry<CIER_DATA_SUCU,List<NEGO_SUCU_SERV_UNIC>> entry : this.servicios_unicos.entrySet()){
                for(NEGO_SUCU_SERV_UNIC su : entry.getValue()){
                    if (su.getST_AFEC_DETR_ForJSON()){                    
                        sub_total_afecto_detraccion_soles+=su.getIM_MONTO_ForJSON()* (nego.getCO_MONE_FACT_ForJSON()==2?cier.getNU_TIPO_CAMB_ForJSON():1d);                            
                    }else{
                        sub_total_temporal+=su.getIM_MONTO_ForJSON();
                    }
                }                
            }

            this.es_afecto_detraccion=this.sub_total_afecto_detraccion_soles>700;
            
            if(descripcionProducto.equalsIgnoreCase("TI")){
               this.es_afecto_detraccion = true;                        
            }
            
            PROD_PADRE pp = ProcesoNegocio.mapProd.getProductoByNegocio(this.nego.getCO_PROD_ForJSON());
            if(Constante.COD_TIPO_DOC_OTRO_DOC == this.cliente.getTipoDocumento().getCO_TIPO_DOCU_ForJSON()
                && !(pp.getCO_PROD_PADRE_ForJSON() == Constante.INT_COD_PROD_PADRE_TI)){
                this.setIsBoleta(true);

            }
            
                    
            for(Entry<CIER_DATA_SUCU,List<NEGO_SUCU_SERV_UNIC>> entry : this.servicios_unicos.entrySet()){
                List<CIER_DATA_SERV_UNIC> para_recibo_data=new ArrayList<CIER_DATA_SERV_UNIC>();
                List<CIER_DATA_SERV_UNIC> para_factura_data=new ArrayList<CIER_DATA_SERV_UNIC>();
                CIER_DATA_SUCU cier_data_sucu=entry.getKey();
                for(NEGO_SUCU_SERV_UNIC su : entry.getValue()){     
                    CIER_DATA_SERV_UNIC cdsu=this.proceso_negocio.getServicios_unicos_datas().get(cier_data_sucu).get(su);
                    if(this.isBoleta){
                        para_recibo_data.add(cdsu);
                        mi_servicios_unicos.add(su);
                    }else if(this.es_afecto_detraccion){
                        para_factura_data.add(cdsu);
                        para_factura_servicios_unicos.add(su);
                    }else if (!this.es_afecto_detraccion || !su.getST_AFEC_DETR_ForJSON()){
                        para_recibo_data.add(cdsu);
                        mi_servicios_unicos.add(su);
                        //this.mi_servicios_unicos_data.add(this.proceso_negocio.getServicios_unicos_data().get(su));
                    }else{
                        //this.para_factura_servicios_unicos_data.add(this.proceso_negocio.getServicios_unicos_data().get(su));
                        para_factura_data.add(cdsu);
                        para_factura_servicios_unicos.add(su);
                    }                                        
                    
                }
                this.mi_servicios_unicos_data.put(cier_data_sucu, para_recibo_data);
                this.para_factura_servicios_unicos_data.put(cier_data_sucu, para_factura_data);
            }
            Map<CIER_DATA_SUCU,Boolean> sucursal_ya_se_conto=new HashMap<CIER_DATA_SUCU, Boolean>();
            for(Map.Entry<CIER_DATA_SUCU,Object[]> entry : listaPlanesySsPorSucursal.entrySet()){
                List<CIER_DATA_PLAN> planes=(List<CIER_DATA_PLAN>) entry.getValue()[0];
                List<CIER_DATA_SERV_SUPL> sss=(List<CIER_DATA_SERV_SUPL>) entry.getValue()[1]; 
                for(CIER_DATA_PLAN plan : planes){
                    
                    if ( FechaHora.isBetween(periodo_inicio, periodo_fin, plan.getFE_INIC()) ){
                        if (plan.getST_TIPO_COBR()==null || plan.getST_TIPO_COBR()==1){
                            if (!this.mi_data_facturar.containsKey(entry.getKey())){
                                this.mi_data_facturar.put(entry.getKey(), new Object[]{new ArrayList<CIER_DATA_PLAN>(),new ArrayList<CIER_DATA_SERV_SUPL>()});                            
                            }                            
                            List<CIER_DATA_PLAN> lista=(List<CIER_DATA_PLAN>)this.mi_data_facturar.get(entry.getKey())[0];
                            lista.add(plan);

                            if(plan.getST_TIPO_COBR()!=null){
                                periodos.add(new Date[]{plan.getFE_INIC(),plan.getFE_FIN()});
                                
                                sub_total_temporal+=Math.abs(plan.getIM_MONT());
                                if (!sucursal_ya_se_conto.containsKey(entry.getKey())){sucursal_ya_se_conto.put(entry.getKey(), true);this.cantidad_sucursales_facturan++;}//para saber la cantidad de sucursales que facturan
                            }else{
                                if(plan.getST_TIPO_DEVO()==4) saldo_promocion+=Math.abs(plan.getIM_MONT());
                            }                        
                        }else{
                            //si es diferencial por upgrade se genera en el recibo despues de UFF
                            if (!this.planes_diferenciales_por_upgrade.containsKey(this))
                                this.planes_diferenciales_por_upgrade.put(this,new HashMap<CIER_DATA_SUCU, List<CIER_DATA_PLAN>>());
                            if (!this.planes_diferenciales_por_upgrade.get(this).containsKey(entry.getKey()))
                                this.planes_diferenciales_por_upgrade.get(this).put(entry.getKey(), new ArrayList<CIER_DATA_PLAN>());
                            this.planes_diferenciales_por_upgrade.get(this).get(entry.getKey()).add(plan);
                        }
                    }
                } 
                List<CIER_DATA_SERV_SUPL> tmp_para_factura_servicios_suplementarios_data=new ArrayList<CIER_DATA_SERV_SUPL>();
                for(CIER_DATA_SERV_SUPL ss : sss){
                    if ( FechaHora.isBetween(periodo_inicio, periodo_fin, ss.getFE_INIC()) ){
                        if(this.es_afecto_detraccion && !this.isBoleta){
                            tmp_para_factura_servicios_suplementarios_data.add(ss);
                        }else if (this.isBoleta || (!this.es_afecto_detraccion || !ss.getST_AFEC_DETR() || ss.getST_TIPO_DEVO()!=null)){
                                if (!this.mi_data_facturar.containsKey(entry.getKey())){
                                    this.mi_data_facturar.put(entry.getKey(), new Object[]{new ArrayList<CIER_DATA_PLAN>(),new ArrayList<CIER_DATA_SERV_SUPL>()});                            
                                }                            
                                List<CIER_DATA_SERV_SUPL> lista=(List<CIER_DATA_SERV_SUPL>)this.mi_data_facturar.get(entry.getKey())[1];
                                lista.add(ss);

                                if(ss.getST_TIPO_COBR()!=null){
                                    periodos.add(new Date[]{ss.getFE_INIC(),ss.getFE_FIN()});
                                    sub_total_temporal+=Math.abs(ss.getIM_MONT());
                                    if (!sucursal_ya_se_conto.containsKey(entry.getKey())){sucursal_ya_se_conto.put(entry.getKey(), true);this.cantidad_sucursales_facturan++;}//para saber la cantidad de sucursales que facturan
                                }else{
                                    if(ss.getST_TIPO_DEVO()==4) saldo_promocion+=Math.abs(ss.getIM_MONT());
                                } 
                        }else{
                            tmp_para_factura_servicios_suplementarios_data.add(ss);
                        }
                    }                   
                }
                if (!para_factura_servicios_suplementarios_data.containsKey(entry.getKey())){
                    para_factura_servicios_suplementarios_data.put(entry.getKey(), tmp_para_factura_servicios_suplementarios_data);
                }else{
                    para_factura_servicios_suplementarios_data.get(entry.getKey()).addAll(tmp_para_factura_servicios_suplementarios_data);
                }
            }
            boolean generaRecibo = this.generaPdf();
            boolean generaFactura = this.generaFactura();
            logger.info("Genera Recibo :"+generaRecibo);
            if (generaRecibo || generaFactura){
                for(Entry<Recibo,Map<CIER_DATA_SUCU,List<CIER_DATA_PLAN>>> entry_a : this.planes_diferenciales_por_upgrade.entrySet()){                    
                    for(Entry<CIER_DATA_SUCU,List<CIER_DATA_PLAN>> entry : entry_a.getValue().entrySet()){
                        List<CIER_DATA_PLAN> lista=null;
                        if(!this.mi_data_facturar.containsKey(entry.getKey())){
                            this.mi_data_facturar.put(entry.getKey(), new Object[]{new ArrayList<CIER_DATA_PLAN>(),new ArrayList<CIER_DATA_SERV_SUPL>()});
                        }
                        lista=(List<CIER_DATA_PLAN>)this.mi_data_facturar.get(entry.getKey())[0];
                        for(CIER_DATA_PLAN plan : entry.getValue()){
                            Double m=plan.getIM_MONT();
                            if (!plan.getCO_MONE_FACT().equals(this.getMonedaRecibo())){
                                if (this.esMonedaSolesRecibo()){
                                    m*=this.cier.getNU_TIPO_CAMB_ForJSON();
                                }else{
                                    m/=this.cier.getNU_TIPO_CAMB_ForJSON();
                                }
                            }
                            lista.add(plan);
                            sub_total_temporal+=Math.abs(m);
                            if (!sucursal_ya_se_conto.containsKey(entry.getKey())){sucursal_ya_se_conto.put(entry.getKey(), true); this.cantidad_sucursales_facturan++;}

                        }  
                    }
                }
                this.planes_diferenciales_por_upgrade.clear();
            }
            
            if (this.saldo_entra>0d){
                if (this.getMonedaRecibo()!=null && !this.getMonedaRecibo().equals(this.moneda_facturacion.getCO_MONE_FACT_ForJSON())){
                    if (this.moneda_facturacion.esSoles())
                        this.saldo_entra/=this.cier.getNU_TIPO_CAMB_ForJSON();
                    else
                        this.saldo_entra*=this.cier.getNU_TIPO_CAMB_ForJSON();
                }
            }
            
            if (sub_total_temporal>0d){
                Double saldo_total=saldo_entra+saldo_promocion;
                if (saldo_total>0d){
                    if (saldo_total>sub_total_temporal){
                        if (saldo_promocion>sub_total_temporal)
                            saldo_promocion=sub_total_temporal;
                        saldo_aplica=sub_total_temporal;
                    }else{
                        saldo_aplica=saldo_total;
                    }
                }
            }
            this.periodos=FechaHora.getUnionElementoJuntosLista(periodos);            
            this.periodos=FechaHora.getListaPeriodosOrdenadoPorColumna(this.periodos, 0);//ordernado por la columna "0" fecha inicio
            this.periodos=FechaHora.getListaPeriodosContinuosUnidos(periodos);
            this.calcular_totales();
        }
        public String getPeriodo(){
            String retornar="";
            int son=this.getPeriodos().size(),van=0;
            for(Date periodo [] : this.getPeriodos()){
                van++;
                if (van==1)
                    retornar+=FechaHora.getStringDateShortStringMonth(periodo[0])+" a "+FechaHora.getStringDateShortStringMonth(periodo[1]);
                else if (van!=son)
                    retornar+=", "+FechaHora.getStringDateShortStringMonth(periodo[0])+" a "+FechaHora.getStringDateShortStringMonth(periodo[1]);
                else
                    retornar+=" y "+FechaHora.getStringDateShortStringMonth(periodo[0])+" a "+FechaHora.getStringDateShortStringMonth(periodo[1]);
                
            }
            if (retornar.length()>0){
                retornar="Periodo del "+retornar;
            }
            return retornar;
        }
        public void mostrar(){
            /*System.out.println("+++++++++++++++++++++++++++++");
            for(Map.Entry<CIER_DATA_SUCU,Object[]> entry : this.mi_data_facturar.entrySet()){
                List<CIER_DATA_PLAN> planes=(List<CIER_DATA_PLAN>) entry.getValue()[0];
                List<CIER_DATA_SERV_SUPL> sss=(List<CIER_DATA_SERV_SUPL>) entry.getValue()[1];            
                for(CIER_DATA_PLAN plan : planes){  
                    plan.mostrar();
                }
                for(CIER_DATA_SERV_SUPL ss : sss){  
                    ss.mostrar();
                }
            }
            System.out.println("Devolucion por cargos fijos "+saldo_aplica);*/
        }
        
        public ReciboPdf getReciboPdf(){
            if (this.recibo_pdf==null)
                this.recibo_pdf=new ReciboPdf(this);
            return recibo_pdf;
        }
        
        public ProcesoNegocio getProcesoNegocio(){
            return this.proceso_negocio;
        }
        
        public void generar_recibo_diferencial(){
            Double sub_total_temporal=0d; 
            this.totales=null;
            Map<CIER_DATA_SUCU,Boolean> sucursal_ya_se_conto=new HashMap<CIER_DATA_SUCU, Boolean>();
            for(Entry<Recibo,Map<CIER_DATA_SUCU,List<CIER_DATA_PLAN>>> entry_a : this.planes_diferenciales_por_upgrade.entrySet()){
                //Solo considera el recibo que coincide con el presente recibo. 
                //Si considera todos, se genera duplicidad y acumula los montos por diferencial
                if (entry_a.getKey().equals(this)){
                    for(Entry<CIER_DATA_SUCU,List<CIER_DATA_PLAN>> entry : entry_a.getValue().entrySet()){
                        List<CIER_DATA_PLAN> lista=null;
                        if(!this.mi_data_facturar.containsKey(entry.getKey())){
                            this.mi_data_facturar.put(entry.getKey(), new Object[]{new ArrayList<CIER_DATA_PLAN>(),new ArrayList<CIER_DATA_SERV_SUPL>()});
                        }
                        lista=(List<CIER_DATA_PLAN>)this.mi_data_facturar.get(entry.getKey())[0];
                        for(CIER_DATA_PLAN plan : entry.getValue()){
                            Double m=plan.getIM_MONT();
                            if (!plan.getCO_MONE_FACT().equals(this.getMonedaRecibo())){
                                if (this.esMonedaSolesRecibo()){
                                    m*=this.cier.getNU_TIPO_CAMB_ForJSON();
                                }else{
                                    m/=this.cier.getNU_TIPO_CAMB_ForJSON();
                                }
                            }
                            if(plan.getST_TIPO_COBR()!=null){
                                getPeriodos().add(new Date[]{plan.getFE_INIC(),plan.getFE_FIN()});
                            }
                            lista.add(plan);
                            sub_total_temporal+=Math.abs(m);
                            if (!sucursal_ya_se_conto.containsKey(entry.getKey())){sucursal_ya_se_conto.put(entry.getKey(), true); this.cantidad_sucursales_facturan++;}
                        } 
                    }
                }
            }
            
            if (sub_total_temporal>0d){
                Double saldo_total=saldo_entra+saldo_promocion;
                if (saldo_total>0d){
                    if (saldo_total>sub_total_temporal){
                        if (saldo_promocion>sub_total_temporal)
                            saldo_promocion=sub_total_temporal;
                        saldo_aplica=sub_total_temporal;
                    }else{
                        saldo_aplica=saldo_total;
                    }
                }
            }
            this.setPeriodos(FechaHora.getUnionElementoJuntosLista(getPeriodos()));            
            this.setPeriodos(FechaHora.getListaPeriodosOrdenadoPorColumna(this.getPeriodos(), 0));//ordernado por la columna "0" fecha inicio
            this.setPeriodos(FechaHora.getListaPeriodosContinuosUnidos(getPeriodos()));
            
            this.calcular_totales();
        }
        
        public void calcular_totales(){
            /*TOTALES
            0 VALOR GRAVADAS
            1 AFECTOS IGV 
            2 IGV
            3 NO AFECTOS IGV 
            4 TOTAL
            5 INSTALACION
            6 ALQUILER
            7 OTROS
            8 DESCUENTOS
            9 RENTA MENSUAL
            10 DIFERENCIAL CARGO FIJO
            */
            if (this.totales==null){
                Double neto=0d,ai=0d,igv=0d,nai=0d,total=0d, importeValorVenta=0d;
                Double inst=0d,alqu=0d,otros=0d;
                Double descuentos=0d,renta_mensual=0d, diferencial_cargo_fijo=0d;
                
                for(Map.Entry<CIER_DATA_SUCU,Object[]> entry : this.mi_data_facturar.entrySet()){
                    List<CIER_DATA_PLAN> planes=(List<CIER_DATA_PLAN>) entry.getValue()[0];
                    List<CIER_DATA_SERV_SUPL> sss=(List<CIER_DATA_SERV_SUPL>) entry.getValue()[1];            
                    for(CIER_DATA_PLAN plan : planes){  
                        if (plan.getST_TIPO_COBR()!=null){
                            if (plan.getST_TIPO_COBR()==1){
                                importeValorVenta=redondearPorDefecto(importeValorVenta+plan.getIM_MONT());
                                renta_mensual=redondearPorDefecto(renta_mensual+plan.getIM_MONT());
                            }else{
                                Double m=plan.getIM_MONT();
                                if (!plan.getCO_MONE_FACT().equals(this.getMonedaRecibo())){
                                    if (this.esMonedaSolesRecibo()){
                                        m*=this.cier.getNU_TIPO_CAMB_ForJSON();
                                    }else{
                                        m/=this.cier.getNU_TIPO_CAMB_ForJSON();
                                    }
                                }
                                importeValorVenta=redondearPorDefecto(importeValorVenta+m);
                                diferencial_cargo_fijo=redondearPorDefecto(diferencial_cargo_fijo+m);
                            }
                        }else{ 
                            if (plan.getST_TIPO_DEVO().equals(4))
                                descuentos=redondearPorDefecto(descuentos+plan.getIM_MONT()*-1);
                        }
                    }
                    for(CIER_DATA_SERV_SUPL ss : sss){  
                        if (ss.getST_TIPO_COBR()!=null){
                            importeValorVenta=redondearPorDefecto(importeValorVenta+ss.getIM_MONT());
                            
                            if (ss.getDE_NOMB().toLowerCase().indexOf("instalaci")!=-1){
                                inst=redondearPorDefecto(inst+ss.getIM_MONT());
                            }else if (ss.getDE_NOMB().toLowerCase().indexOf("alquiler")!=-1){
                                alqu=redondearPorDefecto(alqu+ss.getIM_MONT());
                            }else{
                                otros=redondearPorDefecto(otros+ss.getIM_MONT());
                            }
                        }else{ 
                            if (ss.getST_TIPO_DEVO().equals(4))
                                descuentos=redondearPorDefecto(descuentos+ss.getIM_MONT()*-1);
                        }
                        
                    }
                }
                for(Entry<CIER_DATA_SUCU,List<CIER_DATA_SERV_UNIC>> entry : this.mi_servicios_unicos_data.entrySet()){
                    for(CIER_DATA_SERV_UNIC su: entry.getValue()){
                        importeValorVenta=redondearPorDefecto(importeValorVenta+Math.abs(su.getIM_MONT()));
                        
                        if (su.getDE_NOMB().toLowerCase().indexOf("instalaci")!=-1){
                            inst=redondearPorDefecto(inst+su.getIM_MONT());
                        }else if (su.getDE_NOMB().toLowerCase().indexOf("alquiler")!=-1){
                            alqu=redondearPorDefecto(alqu+su.getIM_MONT());
                        }else{
                            otros=redondearPorDefecto(otros+su.getIM_MONT());
                        }  
                    }
                }
                ai+=redondearPorDefecto(this.saldo_aplica*-1);                
                this.calcular_mis_ajustes(importeValorVenta+ai);                
                for(AJUS ajus : this.mi_ajustes){
                    if (ajus.getST_AFEC_IGV_ForJSON()){
                    	
                        ai=redondearPorDefecto(ai+Math.abs(ajus.getIM_MONT_ForJSON())*-1);

                    }else{
                        nai=redondearPorDefecto(nai+Math.abs(ajus.getIM_MONT_ForJSON())*-1);
                    }
                }
//                descuentos += (ai + nai);
              
                igv     =   redondearPorDefecto((importeValorVenta+ai)*Constante.IGV);
                total   =   redondearPorDefecto(importeValorVenta+igv+ai+nai);            
                                
                if (this.esMonedaSolesRecibo()){
                    ajustes_por_redondeo=0d;
                    //redondeo a 0.00 o 0.05            
                    Double tmp=com.americatel.facturacion.fncs.Numero.redondear(total,2);
                    Double tmpEnt=Math.floor(tmp);
                    Double tmpDiff=com.americatel.facturacion.fncs.Numero.redondear(tmp-tmpEnt,2)*10;
                    tmpEnt=Math.floor(tmpDiff);
                    tmpDiff=com.americatel.facturacion.fncs.Numero.redondear(tmpDiff-tmpEnt,1);

                    if (tmpDiff!=0.0d && tmpDiff!=0.5d){
                        tmp=com.americatel.facturacion.fncs.Numero.redondear(total*10,2);
                        tmpEnt=Math.floor(tmp);
                        tmpDiff=com.americatel.facturacion.fncs.Numero.redondear(tmp-tmpEnt,2);
                        if (tmpDiff>=0.5d){
                            this.ajustes_por_redondeo=(com.americatel.facturacion.fncs.Numero.redondear((tmpDiff-0.5d)/10,2))*-1;
                        }else{
                            this.ajustes_por_redondeo=(com.americatel.facturacion.fncs.Numero.redondear((tmpDiff)/10,2))*-1;
                        }
                        nai=com.americatel.facturacion.fncs.Numero.redondear(nai+this.ajustes_por_redondeo,2);
                        descuentos=com.americatel.facturacion.fncs.Numero.redondear(descuentos+Math.abs(this.ajustes_por_redondeo),2)*-1;
                    }   
                    total   =   com.americatel.facturacion.fncs.Numero.redondear(importeValorVenta+ai+igv+nai,2);
                } 
                this.totales=new Double[]{importeValorVenta,ai,igv,nai,total,inst,alqu,otros,descuentos,renta_mensual,diferencial_cargo_fijo,ajustes_por_redondeo};
            }
        }
        //Funcion para recalcular los montos del recibo en el mantenimiento del recibo
        public void recalcular_totales(){
            if (this.totales==null){
                Double neto=0d,ai=0d,igv=0d,nai=0d,total=0d, importeValorVenta=0d;
                Double inst=0d,alqu=0d,otros=0d;
                Double descuentos=0d,renta_mensual=0d, diferencial_cargo_fijo=0d;
                List<RECI_GLOS> glosasReci=this.getReci_Glosas();

                for(RECI_GLOS glos:glosasReci){
                    String nombre=glos.getNO_GLOS_ForJSON();
                    Double monto=glos.getIM_MONT_ForJSON();
                    if (glos.getTI_GLOS_ForJSON()==Constante.GLOSA_TIPO_PLAN){ 
                        importeValorVenta=redondearPorDefecto(importeValorVenta+monto);
                        renta_mensual=redondearPorDefecto(renta_mensual+monto);
                    }else if (glos.getTI_GLOS_ForJSON()==Constante.GLOSA_TIPO_SERVICIO_SUPLEMENTARIO){
                        importeValorVenta=redondearPorDefecto(importeValorVenta+monto);
                        if (nombre.toLowerCase().indexOf("instalaci")!=-1){
                            inst=redondearPorDefecto(inst+monto);
                        }else if (nombre.toLowerCase().indexOf("alquiler")!=-1){
                            alqu=redondearPorDefecto(alqu+monto);
                        }else{
                            otros=redondearPorDefecto(otros+monto);
                        }
                    }else if (glos.getTI_GLOS_ForJSON()==Constante.GLOSA_TIPO_SERVICIO_UNICO){
                        importeValorVenta=redondearPorDefecto(importeValorVenta+Math.abs(monto));
                        if (nombre.toLowerCase().indexOf("instalaci")!=-1){
                            inst=redondearPorDefecto(inst+monto);
                        }else if (nombre.toLowerCase().indexOf("alquiler")!=-1){
                            alqu=redondearPorDefecto(alqu+monto);
                        }else{
                            otros=redondearPorDefecto(otros+monto);
                        }
                    }else if (glos.getTI_GLOS_ForJSON()==Constante.GLOSA_TIPO_AJUSTE_AFECTO_IGV || 
                                glos.getTI_GLOS_ForJSON()==Constante.GLOSA_TIPO_DEVOLUCION_POR_CARGO_FIJO){
                        ai=redondearPorDefecto(ai+Math.abs(monto)*-1);
                    }else if (glos.getTI_GLOS_ForJSON()==Constante.GLOSA_TIPO_AJUSTE_NO_AFECTO_IGV){
                        nai=redondearPorDefecto(nai+Math.abs(monto)*-1);
                    }else if (glos.getTI_GLOS_ForJSON()==Constante.GLOSA_TIPO_DEVOLUCION_X_PROMOCION){
                        descuentos=redondearPorDefecto(descuentos+monto*-1);
                    }else if (glos.getTI_GLOS_ForJSON()==Constante.GLOSA_TIPO_DIFERENCIAL_CARGO_FIJO){
                        importeValorVenta=redondearPorDefecto(importeValorVenta+monto);
                        diferencial_cargo_fijo=redondearPorDefecto(diferencial_cargo_fijo+monto);
                    }
                }
//                descuentos += (ai + nai);
                

                
                igv     =   redondearPorDefecto((importeValorVenta+ai)*Constante.IGV);
                total   =   redondearPorDefecto(importeValorVenta+igv+ai+nai);  
                                
                if ((this.reci_modificado != null && this.reci_modificado.getCO_MONE_FACT_ForJSON()==1) ||
                    (this.bole_modificado != null && this.bole_modificado.getCO_MONE_FACT_ForJSON()==1)   ){ //Soles
                    this.ajustes_por_redondeo=0d;
                    //redondeo a 0.00 o 0.05            
                    Double tmp=com.americatel.facturacion.fncs.Numero.redondear(total,2);
                    Double tmpEnt=Math.floor(tmp);
                    Double tmpDiff=com.americatel.facturacion.fncs.Numero.redondear(tmp-tmpEnt,2)*10;
                    tmpEnt=Math.floor(tmpDiff);
                    tmpDiff=com.americatel.facturacion.fncs.Numero.redondear(tmpDiff-tmpEnt,1);

                    if (tmpDiff!=0.0d && tmpDiff!=0.5d){
                        tmp=com.americatel.facturacion.fncs.Numero.redondear(total*10,2);
                        tmpEnt=Math.floor(tmp);
                        tmpDiff=com.americatel.facturacion.fncs.Numero.redondear(tmp-tmpEnt,2);
                        if (tmpDiff>=0.5d){
                            this.ajustes_por_redondeo=(com.americatel.facturacion.fncs.Numero.redondear((tmpDiff-0.5d)/10,2))*-1;
                        }else{
                            this.ajustes_por_redondeo=(com.americatel.facturacion.fncs.Numero.redondear((tmpDiff)/10,2))*-1;
                        }
                        nai=com.americatel.facturacion.fncs.Numero.redondear(nai+this.ajustes_por_redondeo,2);
                        descuentos=com.americatel.facturacion.fncs.Numero.redondear(descuentos+Math.abs(this.ajustes_por_redondeo),2)*-1;
                    }  
                    total   =   com.americatel.facturacion.fncs.Numero.redondear(importeValorVenta+ai+igv+nai,2);
                } 
                this.totales=new Double[]{importeValorVenta,ai,igv,nai,total,inst,alqu,otros,descuentos,renta_mensual,diferencial_cargo_fijo,ajustes_por_redondeo};
            }
        }
        
        public Double [] getTotales(){
            if (this.totales==null)  this.calcular_totales();
            return this.totales;
        }
        public Boolean esMonedaSolesRecibo(){
            Integer r=this.getMonedaRecibo();
            if (r!=null){
                return r==1;
            }
            return false;
        }
        public Boolean esMonedaDolaresRecibo(){
            Integer r=this.getMonedaRecibo();
            if (r!=null){
                return r==2;
            }
            return false;
        }
        
        public Integer getMonedaRecibo(){
            for(Map.Entry<CIER_DATA_SUCU,Object[]> entry : this.mi_data_facturar.entrySet()){
                List<CIER_DATA_PLAN> planes=(List<CIER_DATA_PLAN>) entry.getValue()[0];
                List<CIER_DATA_SERV_SUPL> sss=(List<CIER_DATA_SERV_SUPL>) entry.getValue()[1];     
                for(CIER_DATA_PLAN plan : planes){
                    return plan.getCO_MONE_FACT();
                }

                for(CIER_DATA_SERV_SUPL ss : sss){
                    return ss.getCO_MONE_FACT();
                }                
            }
            return null;
        }
        
        public Map<CIER_DATA_SUCU,Object[]> getDataPlanesParaFactura(){
        	return this.mi_data_facturar;
//        	return this.proceso_negocio.data_facturar;
        }
        
        public Map<CIER_DATA_SUCU,List<CIER_DATA_SERV_SUPL>> getDataServiciosSuplementariosParaFactura(){
            return this.para_factura_servicios_suplementarios_data;
        }
        
        public Map<CIER_DATA_SUCU,List<CIER_DATA_SERV_UNIC>> getDataServiciosUnicosParaFactura(){
            return this.para_factura_servicios_unicos_data;
        }

        public Map<Recibo,Map<CIER_DATA_SUCU,List<CIER_DATA_PLAN>>> getPlanes_diferenciales_por_upgrade() {
            return this.planes_diferenciales_por_upgrade;
        }
           
        public Boolean generaPdf(){
            if (this.generaPdf==null){
                this.generaPdf=false;
                PROD_PADRE pp = ProcesoNegocio.mapProd.getProductoByNegocio(this.nego.getCO_PROD_ForJSON());
                //AVM - Agregando validacion para generar boletas
                if(Constante.COD_TIPO_DOC_OTRO_DOC == this.cliente.getTipoDocumento().getCO_TIPO_DOCU_ForJSON()
                    && !(pp.getCO_PROD_PADRE_ForJSON() == Constante.INT_COD_PROD_PADRE_TI)){
                    this.generaPdf=true;
                    this.setIsBoleta(true);
                    
                }else{
                    this.setIsBoleta(false);
                    /*INICIO    - AVM - CAMBIO REGISTRAR FACTURA CUANDO SEA PRODUCTO TI*/                  
                    if( pp != null && pp.getCO_PROD_PADRE_ForJSON() == Constante.INT_COD_PROD_PADRE_TI){
                        return this.generaPdf=false;
                    }
                    /*FIN    - AVM - CAMBIO REGISTRAR FACTURA CUANDO SEA PRODUCTO TI*/

                    for(Map.Entry<CIER_DATA_SUCU,Object[]> entry : this.mi_data_facturar.entrySet()){

                        List<CIER_DATA_PLAN> planes=(List<CIER_DATA_PLAN>) entry.getValue()[0];
                        List<CIER_DATA_SERV_SUPL> sss=(List<CIER_DATA_SERV_SUPL>) entry.getValue()[1];            
                        for(CIER_DATA_PLAN plan : planes){     

                            if (plan.getST_TIPO_COBR()!=null && plan.getST_TIPO_COBR()==1) 
                                this.generaPdf=true;
                        }
                        for(CIER_DATA_SERV_SUPL ss : sss){  
                            if (ss.getST_TIPO_COBR()!=null && ss.getST_TIPO_COBR()==1) 
                                this.generaPdf=true;
                        }
                    }
                    for(Entry<CIER_DATA_SUCU,List<CIER_DATA_SERV_UNIC>> entry : this.mi_servicios_unicos_data.entrySet()){
                        if (entry.getValue().size()>0) this.generaPdf=true;
                    }
                }
                      
            
            }
//            log.log(Level.INFO, "generaPdf() :"+this.generaPdf );
            return this.generaPdf;
        }
        
        public Boolean generaFactura(){
        	if (this.generaFactura==null){
        		this.generaFactura=false;
        		PROD_PADRE pp = ProcesoNegocio.mapProd.getProductoByNegocio(this.nego.getCO_PROD_ForJSON());
                //AVM - Agregando validacion para generar boletas
                if(Constante.COD_TIPO_DOC_OTRO_DOC == this.cliente.getTipoDocumento().getCO_TIPO_DOCU_ForJSON()
                        && !(pp.getCO_PROD_PADRE_ForJSON() == Constante.INT_COD_PROD_PADRE_TI)){
                	this.generaFactura = false;;
                }else{
                	
                    /*INICIO    AVM - CAMBIO REGISTRAR FACTURA CUANDO SEA PRODUCTO TI*/
                    
                    for(Map.Entry<CIER_DATA_SUCU,Object[]> entry : this.mi_data_facturar.entrySet()){
	                	List<CIER_DATA_PLAN> planes=(List<CIER_DATA_PLAN>) entry.getValue()[0];
	                    for(CIER_DATA_PLAN plan : planes){     
	                        if (plan.getST_TIPO_COBR()!=null && plan.getST_TIPO_COBR()==1){ 
	                        	if( pp != null && pp.getCO_PROD_PADRE_ForJSON() == Constante.INT_COD_PROD_PADRE_TI){
		                        	this.generaFactura = true;
		                        }
	                        }
	                    }
                	}
                    /*FIN       AVM - CAMBIO REGISTRAR FACTURA CUANDO SEA PRODUCTO TI*/
                    for(Entry<CIER_DATA_SUCU,List<CIER_DATA_SERV_SUPL>> entry : this.para_factura_servicios_suplementarios_data.entrySet() ){
                        //if (entry.getValue().size()>0 && this.totales[0] > 0) this.generaFactura = true;
                        if (entry.getValue().size()>0 ) this.generaFactura = true;
                    }
                    for(Entry<CIER_DATA_SUCU,List<CIER_DATA_SERV_UNIC>> entry : this.para_factura_servicios_unicos_data.entrySet() ){
                        //if (entry.getValue().size()>0 && this.totales[0] > 0) this.generaFactura = true;
                        if (entry.getValue().size()>0 ) this.generaFactura = true;
                    }

                }
        	}
        	return this.generaFactura;
            
        }

        public Integer getCantidad_Sucursales_Facturan() {
            return cantidad_sucursales_facturan;
        }
        
        public String getNombreRecibo(){
            return this.NumeroRecibo+"-"+this.nego.getCO_NEGO_ForJSON()+"-"+FechaHora.getYear(this.periodo_inicio)+"-"+(FechaHora.getMonth(this.periodo_inicio)+1);
        }        
        public Long getNumeroRecibo() {
            return this.NumeroRecibo;
        }
        public void setNumeroRecibo(Long NumeroRecibo) {
            this.NumeroRecibo = NumeroRecibo;
        }
        public String getNombreBoleta(){
            return this.NumeroBoleta+"-"+this.nego.getCO_NEGO_ForJSON()+"-"+FechaHora.getYear(this.periodo_inicio)+"-"+(FechaHora.getMonth(this.periodo_inicio)+1);
        }
        public Long getNumeroBoleta() {
            return NumeroBoleta;
        }
        public void setNumeroBoleta(Long NumeroBoleta) {
            this.NumeroBoleta = NumeroBoleta;
        }
        public Boolean getGeneraPdf(){
            return this.generaPdf;
        }
        public void setGeneraPdf(Boolean generaPdf) {
            this.generaPdf = generaPdf;
        } 
        private void calcular_mis_ajustes(double queda) {
            for(AJUS a : this.ajustes){
                if(queda-a.getIM_MONT_ForJSON()>0){
                    this.mi_ajustes.add(a);
                    queda-=a.getIM_MONT_ForJSON();
                }else{
                    this.ajustes_no_aplicados.add(a);
                }
            }
        }

        public List<AJUS> getAjustes_No_Aplicados() {
            return ajustes_no_aplicados;
        }
        
        public Double getMontoNeto(){
            //{neto,ai,igv,nai,total};
            return this.totales!=null ? this.totales[0] : null;
        }                  
        public Double getMontoAfectoIGV(){
            //{neto,ai,igv,nai,total};
            return this.totales!=null ? this.totales[1] : null;
        }
        public Double getMontoIGV(){
            //{neto,ai,igv,nai,total};
            return this.totales!=null ? this.totales[2] : null;
        }                  
        public Double getMontoNoAfectoIGV(){
            return this.totales!=null ? this.totales[3]: null;
        }        
        public Double getMontoTotal(){
            //{neto,ai,igv,nai,total};
            return this.totales!=null ? this.totales[4] : null;
        }
        public Double getMontoInstalacion(){
            return this.totales!=null ? this.totales[5] : null;
        }
        public Double getMontoAlquiler(){
            return this.totales!=null ? this.totales[6] : null;
        } 
        public Double getMontoOtro(){
            return this.totales!=null ? this.totales[7] : null;
        }
        public Double getMontoDescuento(){
            return this.totales!=null ? this.totales[8] : null;
        } 
        public Double getMontoServicioMensual(){
            return this.totales!=null ? this.totales[9] : null;
        }
        public Double getMontoDiferencialCargosFijos(){
            return this.totales!=null ? this.totales[10] : null;
        }
        public CIER_DATA_SUCU getAlgunaSucursalFacturable(){
            for(Entry<CIER_DATA_SUCU,Object[]> entry : this.mi_data_facturar.entrySet()){
                return entry.getKey();
            }       
            return null;
        }        
        public Integer getCodigoDistritoAlgunaSucursalFacturable(){
            CIER_DATA_SUCU r=this.getAlgunaSucursalFacturable();
            if (r!=null) return r.getCO_DIST();
            return null;
        }
        public String getNombreDistritoAlgunaSucursalFacturable(){
            CIER_DATA_SUCU r=this.getAlgunaSucursalFacturable();
            if (r!=null) return r.getNO_DIST();        
            return null;
        } 
        public String getDireccionAlgunaSucursalFacturable(){
            CIER_DATA_SUCU r=this.getAlgunaSucursalFacturable();
            if (r!=null) return r.getDE_DIRE_SUCU();           
            return null;
        }        
        public void saveBD() {
            if (!this.proceso_negocio.esPreview() && this.generaPdf()){
                PROD_PADRE pp = ProcesoNegocio.mapProd.getProductoByNegocio(this.nego.getCO_PROD_ForJSON());
                //AVM - Agregando validacion para generar boletas
                if(Constante.COD_TIPO_DOC_OTRO_DOC == this.cliente.getTipoDocumento().getCO_TIPO_DOCU_ForJSON()
                    && !(pp.getCO_PROD_PADRE_ForJSON() == Constante.INT_COD_PROD_PADRE_TI)){
                    BOLE r=new BOLE();
                    r.setCO_CIER(this.cier.getCO_CIER_ForJSON());
                    r.setCO_CLIE(this.cliente.getCO_CLIE_ForJSON());
                    r.setCO_DIST_CORR(this.sucursal_correspondencia.getCO_DIST_ForJSON());
                    r.setCO_DIST_FISC(this.sucursal_fiscal.getCO_DIST_ForJSON());
                    r.setCO_MONE_FACT(this.moneda_facturacion.getCO_MONE_FACT_ForJSON());
                    r.setCO_NEGO(this.nego.getCO_NEGO_ForJSON());
                    r.setCO_BOLE(this.NumeroBoleta);
                    r.setFE_INI(this.periodo_inicio);
                    r.setFE_FIN(this.periodo_fin);
                    r.setCO_TIPO_DOCU(this.cliente.getCO_TIPO_DOCU_ForJSON());
                    r.setCO_DIST_INST(this.getCodigoDistritoAlgunaSucursalFacturable());
                    r.setDE_CODI_BUM(this.cliente.getDE_CODI_BUM_ForJSON()+"-"+this.cliente.getDE_DIGI_BUM_ForJSON());
                    r.setDE_DIRE_CORR(this.sucursal_correspondencia.getDE_DIRE_ForJSON());
                    r.setDE_DIRE_FISC(this.sucursal_fiscal.getDE_DIRE_ForJSON());
                    r.setDE_NUME_DOCU(this.cliente.getDE_NUME_DOCU_ForJSON());
                    r.setDE_DIRE_INST(this.getDireccionAlgunaSucursalFacturable());
                    r.setDE_PERI(this.getPeriodo());
                    r.setDE_REF_DIRE_CORR(this.sucursal_correspondencia.getDE_REF_DIRE_ForJSON());
                    r.setDE_SIMB_MONE_FACT(this.moneda_facturacion.getDE_SIMB_ForJSON());
                    r.setFE_EMIS(this.cier.getFE_EMIS_ForJSON());
                    r.setFE_VENC(this.cier.getFE_VENC_ForJSON());                
                    r.setIM_AJUS_NIGV(this.getMontoNoAfectoIGV());
                    r.setIM_AJUS_SIGV(this.getMontoAfectoIGV());
                    r.setIM_ALQU(this.getMontoAlquiler());
                    r.setIM_DESC(this.getMontoDescuento());
                    r.setIM_DIF_CARGO_FIJO(this.getMontoDiferencialCargosFijos());                
                    r.setIM_IGV(this.getMontoIGV());
                    r.setIM_INST(this.getMontoInstalacion());                
                    r.setIM_NETO(this.getMontoNeto());
                    r.setIM_OTRO(this.getMontoOtro());
                    r.setIM_SERV_MENS(this.getMontoServicioMensual());
                    r.setIM_TOTA(this.getMontoTotal());                
                    r.setNO_CLIE(this.cliente.getFullNameCliente());
                    r.setNO_DIST_CORR(this.distrito_correspondencia.getNO_DIST_ForJSON());
                    r.setNO_DIST_FISC(this.distrito_fiscal.getNO_DIST_ForJSON());
                    r.setNO_MONE_FACT(this.moneda_facturacion.getNO_MONE_FACT_ForJSON());
                    r.setNO_TIPO_DOCU(this.tipo_documento.getNO_TIPO_DOCU_ForJSON());
                    r.setNO_DIST_INST(this.getNombreDistritoAlgunaSucursalFacturable());
                    r.setST_ANUL(false);
                    r.setST_ELIM(false);

                    ProcesoNegocio.mapBole.insertar(r);
                    
                }else{
                    RECI r=new RECI();
                    r.setCO_CIER(this.cier.getCO_CIER_ForJSON());
                    r.setCO_CLIE(this.cliente.getCO_CLIE_ForJSON());
                    r.setCO_DIST_CORR(this.sucursal_correspondencia.getCO_DIST_ForJSON());
                    r.setCO_DIST_FISC(this.sucursal_fiscal.getCO_DIST_ForJSON());
                    r.setCO_MONE_FACT(this.moneda_facturacion.getCO_MONE_FACT_ForJSON());
                    r.setCO_NEGO(this.nego.getCO_NEGO_ForJSON());
                    r.setCO_RECI(this.NumeroRecibo);
                    r.setFE_INI(this.periodo_inicio);
                    r.setFE_FIN(this.periodo_fin);
                    r.setCO_TIPO_DOCU(this.cliente.getCO_TIPO_DOCU_ForJSON());
                    r.setCO_DIST_INST(this.getCodigoDistritoAlgunaSucursalFacturable());
                    r.setDE_CODI_BUM(this.cliente.getDE_CODI_BUM_ForJSON()+"-"+this.cliente.getDE_DIGI_BUM_ForJSON());
                    r.setDE_DIRE_CORR(this.sucursal_correspondencia.getDE_DIRE_ForJSON());
                    r.setDE_DIRE_FISC(this.sucursal_fiscal.getDE_DIRE_ForJSON());
                    r.setDE_NUME_DOCU(this.cliente.getDE_NUME_DOCU_ForJSON());
                    r.setDE_DIRE_INST(this.getDireccionAlgunaSucursalFacturable());
                    r.setDE_PERI(this.getPeriodo());
                    r.setDE_REF_DIRE_CORR(this.sucursal_correspondencia.getDE_REF_DIRE_ForJSON());
                    r.setDE_SIMB_MONE_FACT(this.moneda_facturacion.getDE_SIMB_ForJSON());
                    r.setFE_EMIS(this.cier.getFE_EMIS_ForJSON());
                    r.setFE_VENC(this.cier.getFE_VENC_ForJSON());                
                    r.setIM_AJUS_NIGV(this.getMontoNoAfectoIGV());
                    
                    r.setIM_ALQU(this.getMontoAlquiler());
                    r.setIM_DESC(this.getMontoDescuento());
                    r.setIM_DIF_CARGO_FIJO(this.getMontoDiferencialCargosFijos());                
                    r.setIM_IGV(this.getMontoIGV());
                    r.setIM_INST(this.getMontoInstalacion());                
                    r.setIM_NETO(this.getMontoNeto());
                    r.setIM_OTRO(this.getMontoOtro());
                    r.setIM_SERV_MENS(this.getMontoServicioMensual());
                                 
                    r.setNO_CLIE(this.cliente.getFullNameCliente());
                    r.setNO_DIST_CORR(this.distrito_correspondencia.getNO_DIST_ForJSON());
                    r.setNO_DIST_FISC(this.distrito_fiscal.getNO_DIST_ForJSON());
                    r.setNO_MONE_FACT(this.moneda_facturacion.getNO_MONE_FACT_ForJSON());
                    r.setNO_TIPO_DOCU(this.tipo_documento.getNO_TIPO_DOCU_ForJSON());
                    r.setNO_DIST_INST(this.getNombreDistritoAlgunaSucursalFacturable());
                    r.setST_ANUL(false);
                    r.setST_ELIM(false);
                                                            
                    r.setIM_AJUS_SIGV(this.getMontoAfectoIGV());
                    r.setIM_TOTA(this.getMontoTotal());     

                    ProcesoNegocio.mapReci.insertar(r);
                }
                /*[INICIO] Recibos Glosas*/
                Map<Integer,Object[]> resumen_planes=new HashMap<Integer, Object[]>();
                Map<Integer,Object[]> resumen_sss=new HashMap<Integer, Object[]>();
                Double monto_cobrar=0d;
                Double devolucion_por_promocion=0d;
                Double devolucion_cargos_fijos=0d;
                Integer count_planes=0;
                
                for(Entry<CIER_DATA_SUCU,Object[]> entry : this.mi_data_facturar.entrySet()){
                    CIER_DATA_SUCU sucu_data=entry.getKey();
                    List<CIER_DATA_PLAN> planes=(List<CIER_DATA_PLAN>) entry.getValue()[0];
                    List<CIER_DATA_SERV_SUPL> sss=(List<CIER_DATA_SERV_SUPL>) entry.getValue()[1];            
                    for(CIER_DATA_PLAN plan : planes){                        
                        if (plan.getST_TIPO_DEVO()==null){   
                            String nombre=plan.getDE_NOMB();
                            String direccion=sucu_data.getDE_DIRE_SUCU();
                            Integer co_dist=sucu_data.getCO_DIST();
                            String no_dist=sucu_data.getNO_DIST();
                            Double m=plan.getIM_MONT();
                            /*
                            if (plan.getCO_NEGO_SUCU_PLAN()==467 || plan.getCO_NEGO_SUCU_PLAN()==466 || plan.getCO_NEGO_SUCU_PLAN()==495 || plan.getCO_NEGO_SUCU_PLAN()==468){
                                System.out.println("Aqui");
                            }*/
                            if (plan.getST_TIPO_COBR()==2){
                                nombre=ReciboPdf.GLOSA_DIFERENCIAL_CARGOS_FIJOS;
                                if (!plan.getCO_MONE_FACT().equals(this.getMonedaRecibo())){
                                    if (this.esMonedaSolesRecibo()){
                                        m*=this.cier.getNU_TIPO_CAMB_ForJSON();
                                    }else{
                                        m/=this.cier.getNU_TIPO_CAMB_ForJSON();
                                    }
                                }

                            }
                            count_planes++;
                            resumen_planes.put(count_planes, new Object[]{nombre,0d,0,direccion,co_dist,no_dist});
                            Object data[]=resumen_planes.get(count_planes);
                            data[1]=(Double)m;
                            data[2]=sucu_data.getCO_NEGO_SUCU();
                            monto_cobrar+=m; 
                        }else{
                            if (plan.getST_TIPO_DEVO()==4){
                                devolucion_por_promocion+=Math.abs(plan.getIM_MONT())*-1;
                            }
                        }
                    }          
                    for(CIER_DATA_SERV_SUPL ss : sss){ 
                        String direccion=sucu_data.getDE_DIRE_SUCU();
                        Integer co_dist=sucu_data.getCO_DIST();
                        String no_dist=sucu_data.getNO_DIST();
                        if (ss.getST_TIPO_DEVO()==null){            
                            resumen_sss.put(ss.getCO_NEGO_SUCU_SERV_SUPL(), new Object[]{ss.getDE_NOMB(),0d,0,direccion,co_dist,no_dist});
                            Object data[]=resumen_sss.get(ss.getCO_NEGO_SUCU_SERV_SUPL());
                            data[1]=(Double)ss.getIM_MONT(); 
                            data[2]=sucu_data.getCO_NEGO_SUCU();
                            monto_cobrar+=ss.getIM_MONT();
                        }else{
                            if (ss.getST_TIPO_DEVO()==4){
                                devolucion_por_promocion+=Math.abs(ss.getIM_MONT())*-1;
                            }
                        }
                    }
                }
                for(Entry<CIER_DATA_SUCU,List<CIER_DATA_SERV_UNIC>>  entry : this.mi_servicios_unicos_data.entrySet() ){
                    for (CIER_DATA_SERV_UNIC su : entry.getValue()){
                        monto_cobrar+=su.getIM_MONT();
                    }
                }
                
                /*Validamos la devolucion por promociones y por cargos fijos que no exeda lo que se cobra*/
                if (Math.abs(this.saldo_aplica)>Math.abs(devolucion_por_promocion))
                    devolucion_cargos_fijos=(this.saldo_aplica+devolucion_por_promocion)*-1;
                if (monto_cobrar>0){
                    if ((monto_cobrar+devolucion_por_promocion)<0d){
                        devolucion_por_promocion=monto_cobrar*-1;
                        devolucion_cargos_fijos=0d;
                    }else if ((monto_cobrar+devolucion_por_promocion+devolucion_cargos_fijos)<0d){
                        devolucion_cargos_fijos=(monto_cobrar+devolucion_por_promocion)*-1;
                    }
                }else{
                    devolucion_por_promocion=0d;
                    devolucion_cargos_fijos=0d;            
                }
                
                int cantidad_ajustes_afectos=0,cantidad_ajustes_no_afectos=0;
                for(AJUS ajus : this.mi_ajustes){
                    if (ajus.getST_AFEC_IGV_ForJSON()){
                        cantidad_ajustes_afectos++;
                    }else{
                        cantidad_ajustes_no_afectos++;
                    }
                }        
                if (this.ajustes_por_redondeo!=0d){
                    cantidad_ajustes_no_afectos++;
                }
        
                for(Entry<Integer,Object[]> p : resumen_planes.entrySet()){
                    String nombre=fnc.capitalize((String)p.getValue()[0]);
                    Double monto=(Double)p.getValue()[1];
                    Integer co_nego_sucu=(Integer)p.getValue()[2];
                    String direccion=(String)p.getValue()[3];
                    Integer co_dist =(Integer)p.getValue()[4];
                    String no_dist=(String)p.getValue()[5];
                    if (nombre.equals(ReciboPdf.GLOSA_DIFERENCIAL_CARGOS_FIJOS)){
                        RECI_GLOS rg= new RECI_GLOS();
                        if(isIsBoleta()){
                            rg.setCO_BOLE(this.NumeroBoleta);
                        }else{
                            rg.setCO_RECI(this.NumeroRecibo);
                        }
                        
                        rg.setCO_NEGO_SUCU(co_nego_sucu);
                        rg.setDE_DIRE_SUCU(direccion);
                        rg.setCO_DIST(co_dist);
                        rg.setNO_DIST(no_dist);
                        rg.setNO_GLOS(nombre);
                        rg.setIM_MONT(monto);
                        rg.setTI_GLOS(Constante.GLOSA_TIPO_DIFERENCIAL_CARGO_FIJO); //DIFERENCIAL CARGOS FIJOS
                        Historial.initInsertar(null, rg);
                        ProcesoNegocio.mapReci_Glos.insertar(rg);
                    } else {
                        RECI_GLOS rg= new RECI_GLOS();
                        if(isIsBoleta()){
                            rg.setCO_BOLE(this.NumeroBoleta);
                        }else{
                            rg.setCO_RECI(this.NumeroRecibo);
                        }
                        rg.setCO_NEGO_SUCU(co_nego_sucu);
                        rg.setDE_DIRE_SUCU(direccion);
                        rg.setCO_DIST(co_dist);
                        rg.setNO_DIST(no_dist);
                        rg.setNO_GLOS(nombre);
                        rg.setIM_MONT(monto);
                        rg.setTI_GLOS(Constante.GLOSA_TIPO_PLAN); //PLAN
                        Historial.initInsertar(null, rg);
                        ProcesoNegocio.mapReci_Glos.insertar(rg);
                        
                    }
                }
                for(Entry<Integer,Object[]> s : resumen_sss.entrySet()){
                    String nombre=fnc.capitalize((String)s.getValue()[0]);
                    Double monto=(Double)s.getValue()[1];
                    Integer co_nego_sucu=(Integer)s.getValue()[2];
                    String direccion=(String)s.getValue()[3];
                    Integer co_dist =(Integer)s.getValue()[4];
                    String no_dist=(String)s.getValue()[5];
                    RECI_GLOS rg= new RECI_GLOS();
                    if(isIsBoleta()){
                        rg.setCO_BOLE(this.NumeroBoleta);
                    }else{
                        rg.setCO_RECI(this.NumeroRecibo);
                    }
                    rg.setCO_NEGO_SUCU(co_nego_sucu);
                    rg.setDE_DIRE_SUCU(direccion);
                    rg.setCO_DIST(co_dist);
                    rg.setNO_DIST(no_dist);
                    rg.setNO_GLOS(nombre);
                    rg.setIM_MONT(monto);
                    rg.setTI_GLOS(Constante.GLOSA_TIPO_SERVICIO_SUPLEMENTARIO); //SERVICIO SUPL
                    Historial.initInsertar(null, rg);
                    ProcesoNegocio.mapReci_Glos.insertar(rg);
                }     
                for(Entry<CIER_DATA_SUCU,List<CIER_DATA_SERV_UNIC>> entry : this.mi_servicios_unicos_data.entrySet() ){
                    CIER_DATA_SUCU sucu_data=entry.getKey();
                    Integer co_nego_sucu=sucu_data.getCO_NEGO_SUCU();
                    String direccion=sucu_data.getDE_DIRE_SUCU();
                    Integer co_dist=sucu_data.getCO_DIST();
                    String no_dist=sucu_data.getNO_DIST();
                    for(CIER_DATA_SERV_UNIC su : entry.getValue()){
                        String nombre=fnc.capitalize(su.getDE_NOMB());
                        Double monto=su.getIM_MONT();
                        
                        RECI_GLOS rg= new RECI_GLOS();
                        if(isIsBoleta()){
                            rg.setCO_BOLE(this.NumeroBoleta);
                        }else{
                            rg.setCO_RECI(this.NumeroRecibo);
                        }
                        rg.setCO_NEGO_SUCU(co_nego_sucu);
                        rg.setDE_DIRE_SUCU(direccion);
                        rg.setCO_DIST(co_dist);
                        rg.setNO_DIST(no_dist);
                        rg.setNO_GLOS(nombre);
                        rg.setIM_MONT(monto);
                        rg.setTI_GLOS(Constante.GLOSA_TIPO_SERVICIO_UNICO); //SERVICIO UNICO
                        Historial.initInsertar(null, rg);
                        ProcesoNegocio.mapReci_Glos.insertar(rg);
                    }
                }
                if (cantidad_ajustes_afectos>0 || (devolucion_por_promocion!=0 || devolucion_cargos_fijos!=0 )){
                    for(AJUS ajus : this.mi_ajustes){                
                        if (ajus.getST_AFEC_IGV_ForJSON()){
                            String nombre=fnc.capitalize(ajus.getDE_GLOS_ForJSON());
                            Double monto=ajus.getIM_MONT_ForJSON();
                            RECI_GLOS rg= new RECI_GLOS();
                            if(isIsBoleta()){
                                rg.setCO_BOLE(this.NumeroBoleta);
                            }else{
                                rg.setCO_RECI(this.NumeroRecibo);
                            }
                            //rg.setCO_NEGO_SUCU(ns_su.getCO_NEGO_SUCU_ForJSON());
                            rg.setNO_GLOS(nombre);
                            rg.setIM_MONT(monto);
                            rg.setTI_GLOS(Constante.GLOSA_TIPO_AJUSTE_AFECTO_IGV); //AJUSTE AFECTO A IGV
                            Historial.initInsertar(null, rg);
                            ProcesoNegocio.mapReci_Glos.insertar(rg);
                        }
                    }

                    // Estos son los AJUSTES AFECTOS A IGV
                    if (devolucion_por_promocion!=0){     
                        String nombre=fnc.capitalize(ReciboPdf.GLOSA_DEVOLUCION_PROMOCION);
                        Double monto=Math.abs(devolucion_por_promocion);
                        RECI_GLOS rg= new RECI_GLOS();
                        if(isIsBoleta()){
                            rg.setCO_BOLE(this.NumeroBoleta);
                        }else{
                            rg.setCO_RECI(this.NumeroRecibo);
                        }
                        //rg.setCO_NEGO_SUCU(ns_su.getCO_NEGO_SUCU_ForJSON());
                        rg.setNO_GLOS(nombre);
                        rg.setIM_MONT(monto);
                        rg.setTI_GLOS(Constante.GLOSA_TIPO_DEVOLUCION_X_PROMOCION); //DEV POR PROMOCION
                        Historial.initInsertar(null, rg);
                        ProcesoNegocio.mapReci_Glos.insertar(rg);
                    }
                    if (devolucion_cargos_fijos!=0){ 
                        String nombre=fnc.capitalize(ReciboPdf.GLOSA_DEVOLUCION_CARGOS_FIJOS);
                        Double monto=Math.abs(devolucion_cargos_fijos);
                        RECI_GLOS rg= new RECI_GLOS();
                        if(isIsBoleta()){
                            rg.setCO_BOLE(this.NumeroBoleta);
                        }else{
                            rg.setCO_RECI(this.NumeroRecibo);
                        }
                        //rg.setCO_NEGO_SUCU(ns_su.getCO_NEGO_SUCU_ForJSON());
                        rg.setNO_GLOS(nombre);
                        rg.setIM_MONT(monto);
                        rg.setTI_GLOS(Constante.GLOSA_TIPO_DEVOLUCION_POR_CARGO_FIJO); //DEV CARGOS FIJOS
                        Historial.initInsertar(null, rg);
                        ProcesoNegocio.mapReci_Glos.insertar(rg);
                    }             
                }
                if (cantidad_ajustes_no_afectos>0){
                    for(AJUS ajus : this.mi_ajustes){
                        if (!ajus.getST_AFEC_IGV_ForJSON()){ 
                            String nombre=fnc.capitalize(ajus.getDE_GLOS_ForJSON());
                            Double monto=ajus.getIM_MONT_ForJSON();
                            RECI_GLOS rg= new RECI_GLOS();
                            if(isIsBoleta()){
                                rg.setCO_BOLE(this.NumeroBoleta);
                            }else{
                                rg.setCO_RECI(this.NumeroRecibo);
                            }
                            //rg.setCO_NEGO_SUCU(ns_su.getCO_NEGO_SUCU_ForJSON());
                            rg.setNO_GLOS(nombre);
                            rg.setIM_MONT(monto);
                            rg.setTI_GLOS(Constante.GLOSA_TIPO_AJUSTE_NO_AFECTO_IGV); //AJUSTE NO AFECTO A IGV
                            Historial.initInsertar(null, rg);
                            ProcesoNegocio.mapReci_Glos.insertar(rg);
                        }
                    }
                }
                /*[FIN] Recibos Glosas*/
                
                for(Map.Entry<CIER_DATA_SUCU,Object[]> entry : this.mi_data_facturar.entrySet()){
                    List<CIER_DATA_PLAN> planes=(List<CIER_DATA_PLAN>) entry.getValue()[0];
                    List<CIER_DATA_SERV_SUPL> sss=(List<CIER_DATA_SERV_SUPL>) entry.getValue()[1]; 
                    CIER_DATA_SUCU key=entry.getKey();
                    if (key.getCO_CIER_DATA_SUCU()==null){
                        key.setCO_CIER_DATA_NEGO(this.proceso_negocio.getCier_Data_Nego().getCO_CIER_DATA_NEGO());
                        ProcesoNegocio.mapCier_Data_Sucu.insertar(key);
                    }
                    for(CIER_DATA_PLAN plan : planes){  
                        plan.setCO_CIER_DATA_SUCU(key.getCO_CIER_DATA_SUCU());
                        if(isIsBoleta()){
                            plan.setCO_BOLE(this.NumeroBoleta);
                        }else{
                            plan.setCO_RECI(this.NumeroRecibo);
                        }
                        if (plan.getCO_FACT() != null || plan.getCO_BOLE()!= null || plan.getCO_RECI()!=null){
                        	ProcesoNegocio.mapCier_Data_Plan.insertar(plan);
                        }
                    }
                    for(CIER_DATA_SERV_SUPL ss : sss){  
                        ss.setCO_CIER_DATA_SUCU(key.getCO_CIER_DATA_SUCU());
                        if(isIsBoleta()){
                            ss.setCO_BOLE(this.NumeroBoleta);
                        }else{
                            ss.setCO_RECI(this.NumeroRecibo);
                        }
                        ProcesoNegocio.mapCier_Data_Serv_Supl.insertar(ss);
                    }
                }
                
                for(Entry<CIER_DATA_SUCU,List<CIER_DATA_SERV_UNIC>> entry : this.mi_servicios_unicos_data.entrySet() ){
                    CIER_DATA_SUCU key=entry.getKey();
                    if (key.getCO_CIER_DATA_SUCU()==null){
                        key.setCO_CIER_DATA_NEGO(this.proceso_negocio.getCier_Data_Nego().getCO_CIER_DATA_NEGO());
                        ProcesoNegocio.mapCier_Data_Sucu.insertar(key);
                    }
                    for(CIER_DATA_SERV_UNIC su : entry.getValue()){  
                        su.setCO_CIER_DATA_SUCU(key.getCO_CIER_DATA_SUCU());
                        if(isIsBoleta()){
                            su.setCO_BOLE(this.NumeroBoleta);
                        }else{
                            su.setCO_RECI(this.NumeroRecibo);
                        }
                        ProcesoNegocio.mapCier_Data_Serv_Unic.insertar(su);                                                
                    }
                }
                
                //guardamos SU que se usaron
                for(NEGO_SUCU_SERV_UNIC su : this.mi_servicios_unicos){
                    su.setCO_CIER_COBR(this.cier.getCO_CIER_ForJSON());
                    ProcesoNegocio.mapNego_Sucu_Serv_Unic.saveCierCobr(su);
                }
                
                //guardamos ajustes
                for(AJUS a : this.mi_ajustes){
                    a.setCO_CIER_APLI(this.cier.getCO_CIER_ForJSON());
                    a.setST_PEND(false);
                    ProcesoNegocio.mapAjus.saveCierreAplico(a);
                }
            }
        }

        public List<NEGO_SUCU_SERV_UNIC> getMi_servicios_unicos() {
            return mi_servicios_unicos;
        }
        
        public List<NEGO_SUCU_SERV_UNIC> getServiciosUnicosParaFactura() {
            return this.para_factura_servicios_unicos;
        }

    /**
     * @return the isBoleta
     */
    public boolean isIsBoleta() {
        return isBoleta;
    }

    /**
     * @param isBoleta the isBoleta to set
     */
    public void setIsBoleta(boolean isBoleta) {
        this.isBoleta = isBoleta;
    }

    public List<Date[]> getPeriodos() {
        return periodos;
    }

    public void setPeriodos(List<Date[]> periodos) {
        this.periodos = periodos;
    }
	public Boolean getGeneraFactura() {
		return generaFactura;
	}
	public void setGeneraFactura(Boolean generaFactura) {
		this.generaFactura = generaFactura;
	}

    
        
    }