/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.core3;

import com.americatel.facturacion.Utils.Constante;
import com.americatel.facturacion.controllers.Plan;
import com.americatel.facturacion.fncs.FechaHora;
import com.americatel.facturacion.fncs.fnc;
import com.americatel.facturacion.historial.Historial;
import com.americatel.facturacion.mappers.mapPLAN;
import com.americatel.facturacion.models.AJUS;
import com.americatel.facturacion.models.CIER;
import com.americatel.facturacion.models.CIER_DATA_PLAN;
import com.americatel.facturacion.models.CIER_DATA_SERV_SUPL;
import com.americatel.facturacion.models.CIER_DATA_SERV_UNIC;
import com.americatel.facturacion.models.CIER_DATA_SUCU;
import com.americatel.facturacion.models.CLIE;
import com.americatel.facturacion.models.DEPA;
import com.americatel.facturacion.models.DETALLE_DOCUMENTO;
import com.americatel.facturacion.models.DIST;
import com.americatel.facturacion.models.FACT;
import com.americatel.facturacion.models.FACT_GLOS;
import com.americatel.facturacion.models.MONE_FACT;
import com.americatel.facturacion.models.NEGO;
import com.americatel.facturacion.models.NEGO_SUCU;
import com.americatel.facturacion.models.NEGO_SUCU_SERV_UNIC;
import com.americatel.facturacion.models.PLAN;
import com.americatel.facturacion.models.PROD_PADRE;
import com.americatel.facturacion.models.PROV;
import com.americatel.facturacion.models.SUCU;
import com.americatel.facturacion.models.TIPO_DOCU;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author crodas
 */
public class Factura {
//    private static Logger log = Logger.getLogger(Factura.class.getName());
    final static Logger logger = Logger.getLogger(Factura.class);
    Map<CIER_DATA_SUCU,List<CIER_DATA_SERV_UNIC>> servicios_unicos_data=null;
    List<NEGO_SUCU_SERV_UNIC> servicios_unicos=null;
    Map<CIER_DATA_SUCU,List<CIER_DATA_SERV_SUPL>> servicios_suplementarios=null;//si tiene periodo
    /*INICIO    - AVM - CAMBIO REGISTRAR FACTURA CUANDO SEA PRODUCTO TI*/ 
    Map<CIER_DATA_SUCU,Object[]> planes=null;//creado solo para el caso de Producto TI
    /*FIN    - AVM - CAMBIO REGISTRAR FACTURA CUANDO SEA PRODUCTO TI*/ 
    private List<Date[]> periodos=new ArrayList<Date[]>();
    ProcesoNegocio proceso_negocio;
    Long NumeroFactura=0l;
    FacturaPdf factura_pdf=null;
    CIER cier;
    NEGO nego;
    CLIE cliente;
    SUCU sucursal_fiscal;
    DIST distrito_fiscal;
    PROV provincia_fiscal;
    DEPA departamento_fiscal;
    SUCU sucursal_correspondencia;
    DIST distrito_correspondencia;
    PROV provincia_correspondencia;
    DEPA departamento_correspondencia;    
    
    MONE_FACT moneda_facturacion=null;
    TIPO_DOCU tipo_documento;    
    
    List<AJUS> ajustes = null;
    List<AJUS> mi_ajustes=null;
    List<AJUS> ajustes_no_aplicados=null;
    
    Double saldo_entra=0d;// son valores positivos
    Double saldo_aplica=0d;// son valores positivos
    Double saldo_promocion=0d;//son valores positivos

    Double ajustes_por_redondeo=0d;
    
    FACT fact_modificado;    
    List<FACT_GLOS> glosas;
    Integer cantidad_sucursales_facturan=0;
    Map<NEGO_SUCU,List<FACT_GLOS>> mi_data_factura=new HashMap<NEGO_SUCU,List<FACT_GLOS>>();
    
    private Date periodo_inicio=null;
    private Date periodo_fin=null;
    
    private Double[] totales=null;   
    private Double redondearPorDefecto(Double n){return com.americatel.facturacion.fncs.Numero.redondear(n,2);}
    
    private static mapPLAN mapPLAN;
    private Boolean generaFactura=null;
    public Factura (FACT fact){
        this.fact_modificado=fact;
        this.NumeroFactura=fact.getCO_FACT_ForJSON();
        this.periodo_inicio=fact.getFE_INI_ForJSON();
        this.periodo_fin=fact.getFE_FIN_ForJSON();
        this.nego=fact.getNego_ForJSON();
        this.distrito_fiscal=fact.getDist_Fisc_ForJSON();
        this.provincia_fiscal=fact.getDist_Fisc_ForJSON().getPROV_ForJSON();
        this.departamento_fiscal=fact.getDist_Fisc_ForJSON().getPROV_ForJSON().getDEPA_ForJSON();
        this.cier=fact.getCier_ForJSON();
        this.cantidad_sucursales_facturan=fact.getCantidadSucursalesFacturaron();
        this.mi_ajustes=new ArrayList<AJUS>();    
        this.ajustes=new ArrayList<AJUS>();    
        this.servicios_unicos_data=new HashMap<CIER_DATA_SUCU, List<CIER_DATA_SERV_UNIC>>();
        this.glosas=fact.getGlosas();
        for(FACT_GLOS glosa : this.glosas){
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
                if (!this.mi_data_factura.containsKey(glosa.getNEGO_SUCU_ForJSON())){
                    this.mi_data_factura.put(glosa.getNEGO_SUCU_ForJSON(), new ArrayList<FACT_GLOS>());                            
                }                            
                List<FACT_GLOS> lista=(List<FACT_GLOS>)this.mi_data_factura.get(glosa.getNEGO_SUCU_ForJSON());
                lista.add(glosa);
            }
        }
        this.recalcular_totales();
    }
    
    
    public Factura(ProcesoNegocio proceso_negocio,CIER cier,CLIE cliente,NEGO nego,
            MONE_FACT moneda_facturacion,TIPO_DOCU tipo_documento,
            SUCU sucursal_fiscal,DIST distrito_fiscal,PROV provincia_fiscal,DEPA departamento_fiscal,
            SUCU sucursal_correspondencia,DIST distrito_correspondencia,PROV provincia_correspondencia,DEPA departamento_correspondencia, Double saldo, Date periodo_inicio , Date periodo_fin){
        this.servicios_unicos_data=new HashMap<CIER_DATA_SUCU, List<CIER_DATA_SERV_UNIC>>();
        this.servicios_unicos=new ArrayList<NEGO_SUCU_SERV_UNIC>();
        this.servicios_suplementarios=new HashMap<CIER_DATA_SUCU, List<CIER_DATA_SERV_SUPL>>();
        this.proceso_negocio=proceso_negocio;
        this.cier=cier;
        this.cliente=cliente;
        this.nego=nego;
        this.sucursal_fiscal=sucursal_fiscal;
        this.distrito_fiscal=distrito_fiscal;
        this.provincia_fiscal=provincia_fiscal;
        this.departamento_fiscal=departamento_fiscal;
        
        this.sucursal_correspondencia=sucursal_correspondencia;
        this.distrito_correspondencia=distrito_correspondencia;
        this.provincia_correspondencia=provincia_correspondencia;
        this.departamento_correspondencia=departamento_correspondencia;
        
        this.moneda_facturacion=moneda_facturacion;
        this.tipo_documento=tipo_documento;
        this.ajustes = new ArrayList<AJUS>();
        this.mi_ajustes = new ArrayList<AJUS>(); 
        this.ajustes_no_aplicados = new ArrayList<AJUS>();
        
        this.saldo_entra=Math.abs(saldo);
        
        this.periodo_inicio=periodo_inicio;
        this.periodo_fin=periodo_fin;
        
        if (this.saldo_entra>0d){
            if (this.nego.getCO_MONE_FACT_ForJSON()!= null && !(this.nego.getCO_MONE_FACT_ForJSON()==this.moneda_facturacion.getCO_MONE_FACT_ForJSON())){
                if (this.moneda_facturacion.esSoles())
                    this.saldo_entra/=this.cier.getNU_TIPO_CAMB_ForJSON();
                else
                    this.saldo_entra*=this.cier.getNU_TIPO_CAMB_ForJSON();
            }
        }
        
        
    }
    
    public void recalcular_totales(){
            if (this.totales==null){
                Double neto=0d,ai=0d,igv=0d,nai=0d,total=0d, importeValorVenta=0d;
                Double inst=0d,alqu=0d,otros=0d;
                Double descuentos=0d,renta_mensual=0d, diferencial_cargo_fijo=0d;
                List<FACT_GLOS> glosas=this.getFact_Glosas() ;

                for(FACT_GLOS glos:glosas){
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

                if (Constante.SOLES == this.nego.getCO_MONE_FACT_ForJSON()){
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
    
    public void addServiciosUnicos(Map<CIER_DATA_SUCU,List<CIER_DATA_SERV_UNIC>> l){
        for(Entry<CIER_DATA_SUCU,List<CIER_DATA_SERV_UNIC>> entry : l.entrySet()){            
            if (!servicios_unicos_data.containsKey(entry.getKey())){
                servicios_unicos_data.put(entry.getKey(), new ArrayList<CIER_DATA_SERV_UNIC>());
            }   
            servicios_unicos_data.get(entry.getKey()).addAll(entry.getValue());
        }         
    }
    public void addServiciosUnicos(List<NEGO_SUCU_SERV_UNIC> l){
        this.servicios_unicos.addAll(l);
    }
    
    public void addServiciosSuplementarios(Map<CIER_DATA_SUCU,List<CIER_DATA_SERV_SUPL>> l){
        for(Entry<CIER_DATA_SUCU,List<CIER_DATA_SERV_SUPL>> entry : l.entrySet()){
            if (!servicios_suplementarios.containsKey(entry.getKey())){
                servicios_suplementarios.put(entry.getKey(), new ArrayList<CIER_DATA_SERV_SUPL>());
            }   
            servicios_suplementarios.get(entry.getKey()).addAll(entry.getValue());
        }        
    }
    
    public void addPlanes(Map<CIER_DATA_SUCU,Object[]> l){
    	this.planes = l;       
    }
    
    
    FacturaPdf getFacturaPdf() {
        if (factura_pdf==null)
            factura_pdf=new FacturaPdf(this);
        return factura_pdf;
    }

    public ProcesoNegocio getProceso_negocio() {
        return proceso_negocio;
    }

    public Long getNumeroFactura() {
        return NumeroFactura;
    }

    public FacturaPdf getFactura_pdf() {
        return factura_pdf;
    }
    
    public void calcular_totales() {
        /* TOTALES OLD
            0 VALOR VENTA 
            1 DESCUENTO
            2 VALOR DE VENTA NETO
            3 IGV
            4 AJUSTE POR REDONDEO
            5 TOTAL 
            6 MONTO INSTALACION
            7 MONTO ALQUILER
            8 MONTO OTROS
        */
        
        /*TOTALES NEW
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
            11 AJUSTE POR REDONDEO
        */
        if (this.totales==null){
            Double importeValorVenta=0d,neto=0d,ai=0d,igv=0d,nai=0d,total=0d;
            Double inst=0d,alqu=0d,otros=0d;
            Double descuentos=0d,renta_mensual=0d, diferencial_cargo_fijo=0d;
            
            /*INICIO    - AVM - CAMBIO REGISTRAR FACTURA CUANDO SEA PRODUCTO TI*/
            PROD_PADRE pp = ProcesoNegocio.mapProd.getProductoByNegocio(this.nego.getCO_PROD_ForJSON());
            if( pp != null && pp.getCO_PROD_PADRE_ForJSON() == Constante.INT_COD_PROD_PADRE_TI){
                
                for(Map.Entry<CIER_DATA_SUCU,Object[]> entry : getPlanes().entrySet()){
                    List<CIER_DATA_PLAN> planes=(List<CIER_DATA_PLAN>) entry.getValue()[0];                    
                    for(CIER_DATA_PLAN plan : planes){  
                        if (plan.getST_TIPO_COBR()!=null){
                           if (plan.getST_TIPO_COBR()==1){
                                importeValorVenta=redondearPorDefecto(importeValorVenta+plan.getIM_MONT());
                                renta_mensual=redondearPorDefecto(renta_mensual+plan.getIM_MONT());
                            }else{
                                Double m=plan.getIM_MONT();
                                if (!plan.getCO_MONE_FACT().equals(this.nego.getCO_MONE_FACT_ForJSON())){
                                    if (Constante.SOLES.intValue() == this.nego.getCO_MONE_FACT_ForJSON()){
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
                }
            }
            /*FIN    - AVM - CAMBIO REGISTRAR FACTURA CUANDO SEA PRODUCTO TI*/
            
            for(Entry<CIER_DATA_SUCU,List<CIER_DATA_SERV_SUPL>> entry : this.servicios_suplementarios.entrySet()){
                for (CIER_DATA_SERV_SUPL data : entry.getValue()){   
                    if (data.getST_TIPO_COBR()!=null){
                        importeValorVenta=redondearPorDefecto(importeValorVenta+data.getIM_MONT());
                        if (data.getDE_NOMB().toLowerCase().indexOf("instalaci")!=-1){
                            inst=redondearPorDefecto(inst+data.getIM_MONT());
                        }else if (data.getDE_NOMB().toLowerCase().indexOf("alquiler")!=-1){
                            alqu=redondearPorDefecto(alqu+data.getIM_MONT());
                        }else{
                            otros=redondearPorDefecto(otros+data.getIM_MONT());
                        }
                    }else{ 
                        if (data.getST_TIPO_DEVO().equals(4))
                            descuentos=redondearPorDefecto(descuentos+data.getIM_MONT()*-1);
                    }
                }
            } 
            for(Entry<CIER_DATA_SUCU,List<CIER_DATA_SERV_UNIC>> entry : this.servicios_unicos_data.entrySet() ){
                for (CIER_DATA_SERV_UNIC data : entry.getValue())
                {
                    importeValorVenta=redondearPorDefecto(importeValorVenta+data.getIM_MONT());
                    if (data.getDE_NOMB().toLowerCase().indexOf("instalaci")!=-1){
                        inst=redondearPorDefecto(inst+data.getIM_MONT());
                    }else if (data.getDE_NOMB().toLowerCase().indexOf("alquiler")!=-1){
                        alqu=redondearPorDefecto(alqu+data.getIM_MONT());
                    }else{
                        otros=redondearPorDefecto(otros+data.getIM_MONT());
                    }
                }  
            }
            ajustes=nego.getAjustesPendientes(cier);  
            
            ai+=redondearPorDefecto(this.saldo_aplica*-1); 
            this.calcular_mis_ajustes(importeValorVenta+ai); 
            for(AJUS ajus : this.mi_ajustes){
                if (ajus.getST_AFEC_IGV_ForJSON()){
                    ai=redondearPorDefecto(ai+Math.abs(ajus.getIM_MONT_ForJSON())*-1);
                }else{
                    nai=redondearPorDefecto(nai+Math.abs(ajus.getIM_MONT_ForJSON())*-1);
                }
            }
//            descuentos += (ai + nai);
            igv     =   redondearPorDefecto((importeValorVenta+ai)*Constante.IGV);
            total   =   redondearPorDefecto(importeValorVenta+igv+ai+nai); 
            
             if (Constante.SOLES == this.nego.getCO_MONE_FACT_ForJSON()){
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
        
        logger.info("Calcular Totales : " +totales );
    }    
    
    public Double[] getTotales() {
        /*
        0 VALOR VENTA 
        1 DESCUENTO
        2 VALOR DE VENTA NETO
        3 IGV
        4 AJUSTE POR REDONDEO
        5 TOTAL 
        */ 
        return totales;
    }    
    
    
    public Double getMontoValorVenta(){
        return this.totales!=null ? this.totales[0] : null;
    }
    public Double getMontoAfectoIGV(){
        return this.totales!=null ? this.totales[1] : null;
    }
    public Double getMontoIGV(){
        return this.totales!=null ? this.totales[2] : null;
    }
    public Double getMontoNoAfectoIGV(){
        return this.totales!=null ? this.totales[3]: null;
    }
    public Double getMontoTotal(){
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
    public Double getMontoAjustePorRedondeo(){
        return this.totales!=null ? this.totales[11] : null;
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
     

    public CIER_DATA_SUCU getAlgunaSucursalFacturable(){
        for(Entry<CIER_DATA_SUCU,List<CIER_DATA_SERV_UNIC>> entry : this.servicios_unicos_data.entrySet()){
            return entry.getKey();
        }
        for(Entry<CIER_DATA_SUCU,List<CIER_DATA_SERV_SUPL>> entry : this.servicios_suplementarios.entrySet()){
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
    
    
    @Transactional(readOnly = false)
    void saveBD() {
    	
        if (!this.proceso_negocio.esPreview()){
        	Double valor_soles = this.getMontoTotal();
        	Boolean is_afecto_detraccion = false;
        	
        	if (this.moneda_facturacion.esDolares()){// 1 soles, 2 dolares
                valor_soles*=this.cier.getNU_TIPO_CAMB_ForJSON();
            }
            if (valor_soles > 700){
            	//factura
            	is_afecto_detraccion = true;
            }
        	
            FACT f=new FACT();
            f.setCO_CIER(this.cier.getCO_CIER_ForJSON());
            f.setCO_CLIE(this.cliente.getCO_CLIE_ForJSON());
            f.setCO_DIST_CORR(this.distrito_correspondencia.getCO_DIST_ForJSON());
            f.setCO_DIST_FISC(this.distrito_fiscal.getCO_DIST_ForJSON());
            f.setCO_FACT(this.NumeroFactura);
            f.setCO_MONE_FACT(this.moneda_facturacion.getCO_MONE_FACT_ForJSON());
            f.setCO_NEGO(this.nego.getCO_NEGO_ForJSON());
            f.setCO_TIPO_DOCU(this.tipo_documento.getCO_TIPO_DOCU_ForJSON());
            f.setCO_DIST_INST(this.getCodigoDistritoAlgunaSucursalFacturable());
            f.setDE_CODI_BUM(this.cliente.getDE_CODI_BUM_ForJSON()+"-"+this.cliente.getDE_DIGI_BUM_ForJSON());
            f.setDE_DIRE_CORR(this.sucursal_correspondencia.getDE_DIRE_ForJSON());
            f.setDE_DIRE_FISC(this.sucursal_fiscal.getDE_DIRE_ForJSON());
            f.setDE_MONT("");
            f.setDE_NUME_DOCU(this.cliente.getDE_NUME_DOCU_ForJSON());
            f.setDE_DIRE_INST(this.getDireccionAlgunaSucursalFacturable());
            f.setDE_REF_DIRE_CORR(this.sucursal_correspondencia.getDE_REF_DIRE_ForJSON());
            f.setDE_SIMB_MONE_FACT(this.moneda_facturacion.getDE_SIMB_ForJSON());
            f.setFE_EMIS(this.cier.getFE_EMIS_ForJSON());
            f.setFE_VENC(this.cier.getFE_VENC_ForJSON());
            f.setIM_AJUS_NIGV(this.getMontoNoAfectoIGV());            
            f.setIM_ALQU(this.getMontoAlquiler());
            f.setIM_IGV(this.getMontoIGV());
            f.setIM_INST(this.getMontoInstalacion());
            f.setIM_OTRO(this.getMontoOtro());
            f.setIM_TOTA(this.getMontoTotal());
            f.setIM_VALO_DESC(this.getMontoDescuento());
            f.setIM_VALO_VENT(this.getMontoValorVenta());
            f.setIM_VALO_VENT_NETO(this.getMontoValorVenta());            
            f.setNO_CLIE(this.cliente.getFullNameCliente());
            f.setNO_DIST_CORR(this.distrito_correspondencia.getNO_DIST_ForJSON());
            f.setNO_DIST_FISC(this.distrito_fiscal.getNO_DIST_ForJSON());
            f.setNO_MONE_FACT(this.moneda_facturacion.getNO_MONE_FACT_ForJSON());
            f.setNO_TIPO_DOCU(this.tipo_documento.getNO_TIPO_DOCU_ForJSON());     
            f.setNO_DIST_INST(this.getNombreDistritoAlgunaSucursalFacturable());            
            f.setIM_AJUS_SIGV(this.getMontoAfectoIGV());
            f.setDE_PERI(this.getPeriodo());
            f.setIM_SERV_MENS(this.getMontoServicioMensual());
            f.setIM_DIF_CARGO_FIJO(this.getMontoDiferencialCargosFijos());
            f.setFE_INI(this.getPeriodo_inicio());
            f.setFE_FIN(this.getPeriodo_fin());
            f.setST_AFECTO_DETRACCION(is_afecto_detraccion);
            
            logger.info("FACTURA INSERTAR - NRO :"+this.NumeroFactura +"     COD NEGOCIO :"+this.nego.getCO_NEGO_ForJSON());
            ProcesoNegocio.mapFact.insertar(f);        
            logger.info("FIN INSERTAR - NRO :"+this.NumeroFactura +"     COD NEGOCIO :"+this.nego.getCO_NEGO_ForJSON());
         
            /*[INICIO] Recibos Glosas*/
            Map<Integer,Object[]> resumen_planes=new HashMap<Integer, Object[]>();
            Map<Integer,Object[]> resumen_sss=new HashMap<Integer, Object[]>();
            Double monto_cobrar=0d;
            Double devolucion_por_promocion=0d;
            Double devolucion_cargos_fijos=0d;
            Integer count_planes=0;
            
            /*add INICIO reporte TI - carga*/                
            DETALLE_DOCUMENTO detalleDocumentos = null;
            PLAN planObject = null;
            /*add FIN reporte TI - carga*/
            
//            for(Entry<CIER_DATA_SUCU,Object[]> entry : this.proceso_negocio.data_facturar.entrySet()){
        	for(Entry<CIER_DATA_SUCU,Object[]> entry : this.getPlanes().entrySet()){
                CIER_DATA_SUCU sucu_data=entry.getKey();
                List<CIER_DATA_PLAN> planes=(List<CIER_DATA_PLAN>) entry.getValue()[0];
                List<CIER_DATA_SERV_SUPL> sss=(List<CIER_DATA_SERV_SUPL>) entry.getValue()[1];   
                
                PROD_PADRE pp = ProcesoNegocio.mapProd.getProductoByNegocio(this.nego.getCO_PROD_ForJSON());
                if(pp.getCO_PROD_PADRE_ForJSON() == Constante.INT_COD_PROD_PADRE_TI){
                   for(CIER_DATA_PLAN plan : planes){
                        if (plan.getST_TIPO_DEVO()==null){   
                            String nombre=plan.getDE_NOMB();
                            String direccion=sucu_data.getDE_DIRE_SUCU();
                            Integer co_dist=sucu_data.getCO_DIST();
                            String no_dist=sucu_data.getNO_DIST();
                            Double m=plan.getIM_MONT();
                            
                            if (plan.getST_TIPO_COBR()==2){
                                nombre=ReciboPdf.GLOSA_DIFERENCIAL_CARGOS_FIJOS;
                                if (!plan.getCO_MONE_FACT().equals(this.nego.getCO_MONE_FACT_ForJSON())){
                                    if (Constante.SOLES.equals(this.nego.getCO_MONE_FACT_ForJSON())){
                                        m*=this.cier.getNU_TIPO_CAMB_ForJSON();
                                    }else{
                                        m/=this.cier.getNU_TIPO_CAMB_ForJSON();
                                    }
                                }

                            }
                            count_planes++;
                            /*add INICIO reporte TI - carga*/     
                            planObject = ProcesoNegocio.mapPlan.getPlanForCodNegoSucuPlan(plan.getCO_NEGO_SUCU_PLAN());
                            /*add FIN reporte TI - carga*/     
                            resumen_planes.put(count_planes, new Object[]{nombre,0d,0,direccion,co_dist,no_dist,planObject.getPRODUCTO_CARGA()});
                            Object data[]=resumen_planes.get(count_planes);
                            data[1]=m;
                            data[2]=sucu_data.getCO_NEGO_SUCU();
                            monto_cobrar+=m; 
                        }else{
                            if (plan.getST_TIPO_DEVO()==4){
                                devolucion_por_promocion+=Math.abs(plan.getIM_MONT())*-1;
                            }
                        }
                    }
                }
                
                          
                for(CIER_DATA_SERV_SUPL ss : sss){ 
                    String direccion	=sucu_data.getDE_DIRE_SUCU();
                    Integer co_dist		=sucu_data.getCO_DIST();
                    String no_dist		=sucu_data.getNO_DIST();
                    if (ss.getST_TIPO_DEVO()==null){            
                        resumen_sss.put(ss.getCO_NEGO_SUCU_SERV_SUPL(), new Object[]{ss.getDE_NOMB(),0d,0,direccion,co_dist,no_dist});
                        Object data[]=resumen_sss.get(ss.getCO_NEGO_SUCU_SERV_SUPL());
                        data[1]=ss.getIM_MONT(); 
                        data[2]=sucu_data.getCO_NEGO_SUCU();
                        monto_cobrar+=ss.getIM_MONT();
                    }else{
                        if (ss.getST_TIPO_DEVO()==4){
                            devolucion_por_promocion+=Math.abs(ss.getIM_MONT())*-1;
                        }
                    }
                }
            }
            for(Entry<CIER_DATA_SUCU,List<CIER_DATA_SERV_UNIC>>  entry : this.servicios_unicos_data.entrySet() ){
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
                String nombre		=fnc.capitalize((String)p.getValue()[0]);
                Double monto		=(Double)p.getValue()[1];
                Integer co_nego_sucu=(Integer)p.getValue()[2];
                String direccion	=(String)p.getValue()[3];
                Integer co_dist 	=(Integer)p.getValue()[4];
                String no_dist		=(String)p.getValue()[5];
                String productoCarga=(String)p.getValue()[6];
                
                FACT_GLOS fg = new FACT_GLOS();
                fg.setCO_FACT(this.NumeroFactura);
                fg.setCO_NEGO_SUCU(co_nego_sucu);
                fg.setDE_DIRE_SUCU(direccion);
                fg.setCO_DIST(co_dist);
                fg.setNO_DIST(no_dist);
                fg.setNO_GLOS(nombre);
                fg.setIM_MONT(monto);
                if (nombre.equals(ReciboPdf.GLOSA_DIFERENCIAL_CARGOS_FIJOS)){
                    fg.setTI_GLOS(Constante.GLOSA_TIPO_DIFERENCIAL_CARGO_FIJO); //DIFERENCIAL CARGOS FIJOS
                } else {
                    fg.setTI_GLOS(Constante.GLOSA_TIPO_PLAN); //PLAN
                }
                Historial.initInsertar(null, fg);
                ProcesoNegocio.mapFact_Glos.insertar(fg);
                
                /*add INICIO reporte TI - carga*/                
                detalleDocumentos = new DETALLE_DOCUMENTO(null, this.cier.getCO_CIER_ForJSON(), this.NumeroFactura, productoCarga, Constante.GLOSA_TIPO_PLAN, nombre, new BigDecimal(monto),this.cliente.getAbrevTipoDoc());
                /*add FIN reporte TI - carga*/
                ProcesoNegocio.mapDetalle_Documento.insertar(detalleDocumentos);
     
            }
            for(Entry<Integer,Object[]> s : resumen_sss.entrySet()){
                String nombre=fnc.capitalize((String)s.getValue()[0]);
                Double monto=(Double)s.getValue()[1];
                Integer co_nego_sucu=(Integer)s.getValue()[2];
                String direccion=(String)s.getValue()[3];
                Integer co_dist =(Integer)s.getValue()[4];
                String no_dist=(String)s.getValue()[5];
                FACT_GLOS fg = new FACT_GLOS();
                fg.setCO_FACT(this.NumeroFactura);
                fg.setCO_NEGO_SUCU(co_nego_sucu);
                fg.setDE_DIRE_SUCU(direccion);
                fg.setCO_DIST(co_dist);
                fg.setNO_DIST(no_dist);
                fg.setNO_GLOS(nombre);
                fg.setIM_MONT(monto);
                fg.setTI_GLOS(Constante.GLOSA_TIPO_SERVICIO_SUPLEMENTARIO); //SERVICIO SUPL
                Historial.initInsertar(null, fg);
                ProcesoNegocio.mapFact_Glos.insertar(fg);
                
                /*add INICIO reporte TI - carga*/                
                detalleDocumentos = new DETALLE_DOCUMENTO(null, this.cier.getCO_CIER_ForJSON(), this.NumeroFactura, null, Constante.GLOSA_TIPO_SERVICIO_SUPLEMENTARIO, nombre, new BigDecimal(monto),this.cliente.getAbrevTipoDoc());
                /*add FIN reporte TI - carga*/
                
                //comprobar detalle de documentos
                logger.info("tipo de servicio : " +detalleDocumentos.getCO_CIER());
                logger.info("tipo de servicio : " +detalleDocumentos.getCO_FACT());
                logger.info("tipo de servicio : " +detalleDocumentos.getTIP_SERV());
                logger.info("tipo de servicio : " +detalleDocumentos.getCO_TIPO_GLOS());
                logger.info("tipo de servicio : " +detalleDocumentos.getMON_SERVICIO());
                logger.info("tipo de servicio : " +detalleDocumentos.getNOMBRE_SERVICIO());
                logger.info("tipo de servicio : " +detalleDocumentos.getABREV_TIPO_DOCUMENTO());
                
     
                ProcesoNegocio.mapDetalle_Documento.insertar(detalleDocumentos);
            }     
            for(Entry<CIER_DATA_SUCU,List<CIER_DATA_SERV_UNIC>> entry : this.servicios_unicos_data.entrySet() ){
                CIER_DATA_SUCU sucu_data=entry.getKey();
                Integer co_nego_sucu=sucu_data.getCO_NEGO_SUCU();
                String direccion=sucu_data.getDE_DIRE_SUCU();
                Integer co_dist=sucu_data.getCO_DIST();
                String no_dist=sucu_data.getNO_DIST();
                for(CIER_DATA_SERV_UNIC su : entry.getValue()){
                    String nombre=fnc.capitalize(su.getDE_NOMB());
                    Double monto=su.getIM_MONT();

                    FACT_GLOS fg = new FACT_GLOS();
                    fg.setCO_FACT(this.NumeroFactura);
                    fg.setCO_NEGO_SUCU(co_nego_sucu);
                    fg.setDE_DIRE_SUCU(direccion);
                    fg.setCO_DIST(co_dist);
                    fg.setNO_DIST(no_dist);
                    fg.setNO_GLOS(nombre);
                    fg.setIM_MONT(monto);
                    fg.setTI_GLOS(Constante.GLOSA_TIPO_SERVICIO_UNICO); //SERVICIO UNICO
                    Historial.initInsertar(null, fg);
                    ProcesoNegocio.mapFact_Glos.insertar(fg);
                    
                    /*add INICIO reporte TI - carga*/                
                    detalleDocumentos = new DETALLE_DOCUMENTO(null, this.cier.getCO_CIER_ForJSON(), this.NumeroFactura, null, Constante.GLOSA_TIPO_SERVICIO_UNICO, nombre, new BigDecimal(monto),this.cliente.getAbrevTipoDoc());
                    /*add FIN reporte TI - carga*/
                    
                    
                    //comprobar detalle de documentos
                    logger.info("tipo de servicio : " +detalleDocumentos.getCO_CIER());
                    logger.info("tipo de servicio : " +detalleDocumentos.getCO_FACT());
                    logger.info("tipo de servicio : " +detalleDocumentos.getTIP_SERV());
                    logger.info("tipo de servicio : " +detalleDocumentos.getCO_TIPO_GLOS());
                    logger.info("tipo de servicio : " +detalleDocumentos.getMON_SERVICIO());
                    logger.info("tipo de servicio : " +detalleDocumentos.getNOMBRE_SERVICIO());
                    logger.info("tipo de servicio : " +detalleDocumentos.getABREV_TIPO_DOCUMENTO());
                    
                    
                    
                    
                    ProcesoNegocio.mapDetalle_Documento.insertar(detalleDocumentos);
                }
            }
            if (cantidad_ajustes_afectos>0 || (devolucion_por_promocion!=0 || devolucion_cargos_fijos!=0 )){
                for(AJUS ajus : this.mi_ajustes){                
                    if (ajus.getST_AFEC_IGV_ForJSON()){
                        String nombre=fnc.capitalize(ajus.getDE_GLOS_ForJSON());
                        Double monto=ajus.getIM_MONT_ForJSON();
                        FACT_GLOS fg= new FACT_GLOS();
                        fg.setCO_FACT(this.NumeroFactura);
                        fg.setNO_GLOS(nombre);
                        fg.setIM_MONT(monto);
                        fg.setTI_GLOS(Constante.GLOSA_TIPO_AJUSTE_AFECTO_IGV); //AJUSTE AFECTO A IGV
                        Historial.initInsertar(null, fg);
                        ProcesoNegocio.mapFact_Glos.insertar(fg);
                    }
                }

                // Estos son los AJUSTES AFECTOS A IGV
                if (devolucion_por_promocion!=0){     
                    String nombre=fnc.capitalize(ReciboPdf.GLOSA_DEVOLUCION_PROMOCION);
                    Double monto=Math.abs(devolucion_por_promocion);
                    FACT_GLOS fg= new FACT_GLOS();
                    fg.setCO_FACT(this.NumeroFactura);
                    fg.setNO_GLOS(nombre);
                    fg.setIM_MONT(monto);
                    fg.setTI_GLOS(Constante.GLOSA_TIPO_DEVOLUCION_X_PROMOCION); //DEV POR PROMOCION
                    Historial.initInsertar(null, fg);
                    ProcesoNegocio.mapFact_Glos.insertar(fg);
                }
                if (devolucion_cargos_fijos!=0){ 
                    String nombre=fnc.capitalize(ReciboPdf.GLOSA_DEVOLUCION_CARGOS_FIJOS);
                    Double monto=Math.abs(devolucion_cargos_fijos);
                    FACT_GLOS fg= new FACT_GLOS();
                    fg.setCO_FACT(this.NumeroFactura);
                    //rg.setCO_NEGO_SUCU(ns_su.getCO_NEGO_SUCU_ForJSON());
                    fg.setNO_GLOS(nombre);
                    fg.setIM_MONT(monto);
                    fg.setTI_GLOS(Constante.GLOSA_TIPO_DEVOLUCION_POR_CARGO_FIJO); //DEV CARGOS FIJOS
                    Historial.initInsertar(null, fg);
                    ProcesoNegocio.mapFact_Glos.insertar(fg);
                }             
            }
            if (cantidad_ajustes_no_afectos>0){
                for(AJUS ajus : this.mi_ajustes){
                    if (!ajus.getST_AFEC_IGV_ForJSON()){ 
                        String nombre=fnc.capitalize(ajus.getDE_GLOS_ForJSON());
                        Double monto=ajus.getIM_MONT_ForJSON();
                        FACT_GLOS fg= new FACT_GLOS();
                        fg.setCO_FACT(this.NumeroFactura);
                        //rg.setCO_NEGO_SUCU(ns_su.getCO_NEGO_SUCU_ForJSON());
                        fg.setNO_GLOS(nombre);
                        fg.setIM_MONT(monto);
                        fg.setTI_GLOS(Constante.GLOSA_TIPO_AJUSTE_NO_AFECTO_IGV); //AJUSTE NO AFECTO A IGV
                        Historial.initInsertar(null, fg);
                        ProcesoNegocio.mapFact_Glos.insertar(fg);
                    }
                }
            }
                /*[FIN] Recibos Glosas*/
            
            
            /*INICIO    - AVM - CAMBIO REGISTRAR FACTURA CUANDO SEA PRODUCTO TI*/
            PROD_PADRE pp = ProcesoNegocio.mapProd.getProductoByNegocio(this.nego.getCO_PROD_ForJSON());
            if( pp != null && pp.getCO_PROD_PADRE_ForJSON() == Constante.INT_COD_PROD_PADRE_TI){
                
//                for(Map.Entry<CIER_DATA_SUCU,Object[]> entry : this.proceso_negocio.data_facturar.entrySet()){
                for(Map.Entry<CIER_DATA_SUCU,Object[]> entry : this.getPlanes().entrySet()){
                    List<CIER_DATA_PLAN> planes=(List<CIER_DATA_PLAN>) entry.getValue()[0];
                    CIER_DATA_SUCU key=entry.getKey();
                    if (key.getCO_CIER_DATA_SUCU()==null){
                        key.setCO_CIER_DATA_NEGO(this.proceso_negocio.getCier_Data_Nego().getCO_CIER_DATA_NEGO());
                        ProcesoNegocio.mapCier_Data_Sucu.insertar(key);
                    }
                    for(CIER_DATA_PLAN plan : planes){  
                        plan.setCO_CIER_DATA_SUCU(key.getCO_CIER_DATA_SUCU());
                        plan.setCO_FACT(this.NumeroFactura);
                        if (plan.getCO_FACT() != null || plan.getCO_BOLE()!= null || plan.getCO_RECI()!=null){
                        	ProcesoNegocio.mapCier_Data_Plan.insertar(plan);
                        }
                        
                    }
                }
            }
            /*FIN    - AVM - CAMBIO REGISTRAR FACTURA CUANDO SEA PRODUCTO TI*/
            
            
            for(Map.Entry<CIER_DATA_SUCU,List<CIER_DATA_SERV_SUPL>> entry : this.servicios_suplementarios.entrySet()){
                CIER_DATA_SUCU key=entry.getKey();
                if (key.getCO_CIER_DATA_SUCU()==null){
                    key.setCO_CIER_DATA_NEGO(this.proceso_negocio.getCier_Data_Nego().getCO_CIER_DATA_NEGO());
                    ProcesoNegocio.mapCier_Data_Sucu.insertar(key);
                }                    
                for (CIER_DATA_SERV_SUPL data : entry.getValue()){
                    data.setCO_CIER_DATA_SUCU(key.getCO_CIER_DATA_SUCU());
                    data.setCO_FACT(this.NumeroFactura);
                    ProcesoNegocio.mapCier_Data_Serv_Supl.insertar(data);
                }
            }            
            for(Entry<CIER_DATA_SUCU,List<CIER_DATA_SERV_UNIC>> entry : this.servicios_unicos_data.entrySet()){
                CIER_DATA_SUCU key=entry.getKey();
                if (key.getCO_CIER_DATA_SUCU()==null){
                    key.setCO_CIER_DATA_NEGO(this.proceso_negocio.getCier_Data_Nego().getCO_CIER_DATA_NEGO());
                    ProcesoNegocio.mapCier_Data_Sucu.insertar(key);
                }                
                for(CIER_DATA_SERV_UNIC su : entry.getValue()){
                    su.setCO_CIER_DATA_SUCU(key.getCO_CIER_DATA_SUCU());
                    su.setCO_FACT(this.NumeroFactura);
                    ProcesoNegocio.mapCier_Data_Serv_Unic.insertar(su);
                }
            }
            
            //guardamos SU que se usaron
            for(NEGO_SUCU_SERV_UNIC su : this.servicios_unicos){
                su.setCO_CIER_COBR(this.cier.getCO_CIER_ForJSON());
                ProcesoNegocio.mapNego_Sucu_Serv_Unic.saveCierCobr(su);
            }
           
        }
    }

    public void setNumeroFactura(Long NumeroFactura) {
        this.NumeroFactura = NumeroFactura;
    }

    public String getNombreFactura(){
        
//        String r;
//        
//        if(cier!=null){
//            r="F"+this.NumeroFactura+"-"+this.nego.getCO_NEGO_ForJSON()+"-"+this.cier.getNU_PERI_ForJSON()+"-"+this.cier.getNU_ANO_ForJSON();
//        }else{
//            
//            Calendar cal= Calendar.getInstance(); 
//            int year= cal.get(Calendar.YEAR);    
//            int mes= cal.get(Calendar.MONTH);             
//            
//            r="F"+this.NumeroFactura+"-"+this.nego.getCO_NEGO_ForJSON()+"-"+mes+"-"+year;
//        }
        
//        return r;
    	
    	return "F"+this.NumeroFactura+"-"+this.nego.getCO_NEGO_ForJSON()+"-"+FechaHora.getYear(this.periodo_inicio)+"-"+(FechaHora.getMonth(this.periodo_inicio)+1);
    }

   
    public Map<CIER_DATA_SUCU,Object[]> getPlanes() {
//        return planes = this.proceso_negocio.data_facturar;
    	return planes;
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
    
    public FACT getFact_modificado() {
        return fact_modificado;
    }
    
    public List<FACT_GLOS> getFact_Glosas() {
        return glosas;
    }
 
    public Integer getCantidad_Sucursales_Facturan() {
        return cantidad_sucursales_facturan;
    }

    public List<Date[]> getPeriodos() {
        return periodos;
    }

    public void setPeriodos(List<Date[]> periodos) {
        this.periodos = periodos;
    }

    /**
     * @return the periodo_inicio
     */
    public Date getPeriodo_inicio() {
        return periodo_inicio;
    }

    /**
     * @param periodo_inicio the periodo_inicio to set
     */
    public void setPeriodo_inicio(Date periodo_inicio) {
        this.periodo_inicio = periodo_inicio;
    }

    /**
     * @return the periodo_fin
     */
    public Date getPeriodo_fin() {
        return periodo_fin;
    }

    /**
     * @param periodo_fin the periodo_fin to set
     */
    public void setPeriodo_fin(Date periodo_fin) {
        this.periodo_fin = periodo_fin;
    }


	public Boolean getGeneraFactura() {
		return generaFactura;
	}


	public void setGeneraFactura(Boolean generaFactura) {
		this.generaFactura = generaFactura;
	}
}
