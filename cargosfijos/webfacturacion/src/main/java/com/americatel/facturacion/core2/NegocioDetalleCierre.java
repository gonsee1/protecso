/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.core2;

import com.americatel.facturacion.cores.Logs;
import com.americatel.facturacion.fncs.FechaHora;
import com.americatel.facturacion.fncs.fnc;
import com.americatel.facturacion.models.AJUS;
import com.americatel.facturacion.models.CIER;
import com.americatel.facturacion.models.CIER_DATA_PLAN;
import com.americatel.facturacion.models.CIER_DATA_PROM_MONT;
import com.americatel.facturacion.models.CIER_DATA_SERV_SUPL;
import com.americatel.facturacion.models.CIER_DATA_SERV_UNIC;
import com.americatel.facturacion.models.CIER_DATA_SUCU;
import com.americatel.facturacion.models.CLIE;
import com.americatel.facturacion.models.CORT;
import com.americatel.facturacion.models.DEPA;
import com.americatel.facturacion.models.DIST;
import com.americatel.facturacion.models.MONE_FACT;
import com.americatel.facturacion.models.NEGO;
import com.americatel.facturacion.models.NEGO_PROM;
import com.americatel.facturacion.models.NEGO_SUCU;
import com.americatel.facturacion.models.NEGO_SUCU_PLAN;
import com.americatel.facturacion.models.NEGO_SUCU_PROM;
import com.americatel.facturacion.models.NEGO_SUCU_SERV_SUPL;
import com.americatel.facturacion.models.NEGO_SUCU_SERV_UNIC;
import com.americatel.facturacion.models.PROD;
import com.americatel.facturacion.models.PROV;
import com.americatel.facturacion.models.SUCU;
import com.americatel.facturacion.models.SUSP;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author crodas
 */
public class NegocioDetalleCierre{
    public static final String GLOSA_PROMOCION ="DESCUENTO POR PROMOCI\u00d3N";
    public static final String GLOSA_DEVOLUCION_CARGOS_FIJOS ="DEVOLUCI\u00d3N DE CARGOS FIJOS";
    public static final String GLOSA_DIFERENCIAL_CARGOS_FIJOS ="DIFERENCIAL DE CARGOS FIJOS";
    public static final String GLOSA_AJUSTE_POR_REDONDEO ="AJUSTE POR REDONDEO";
    
    private CIER cier=null;
    private PROD prod=null;
    private NEGO nego=null; 
    private MONE_FACT mone_fact=null;
    private List<CORT> cortes=null;
    private CLIE clie=null;
    private Date fecha_real_inicio=null;
    private Date fecha_real_fin=null;
    
    private SUCU sucu_fiscal=null;
    private SUCU sucu_corr=null;
    private DIST dist_fiscal=null;
    private DIST dist_corr=null;
    private PROV prov_fiscal=null;
    private PROV prov_corr=null;
    private DEPA depa_fiscal=null;
    private DEPA depa_corr=null;
    protected List<NEGO_PROM> promociones_pendientes_negocio=null;
    protected List<AJUS> ajustes_pendientes_todos=null;//todos los ajustes pendientes
    protected List<AJUS> ajustes_pendientes_aplican=null;// solo los que se aplican
    protected NegocioDetalle negocio_detalle=null;    
    protected Boolean preview=false;
    protected Double saldo_aproximado=0d; // Es el saldo aproximado cuando un negocio esta en corte o suspension. El monto es positivo
    protected NegocioDetalleCierre(CIER cier,PROD prod,NEGO nego,HttpServletRequest request){
        this.negocio_detalle=new NegocioDetalle(nego.getCO_NEGO_ForJSON(),cier.getCO_CIER_ForJSON());        
        this.prod=prod;
        this.cier=cier;        
        this.nego=nego;
        this.mone_fact=this.nego.getMonedaFacturacion();
        this.cortes=this.nego.getCortesPendientes();
        this.clie=this.nego.getClie();
        this.sucu_fiscal=this.clie.getSucursalFiscal();
        this.sucu_corr=this.nego.getSucursalCorrespondencia();    
        this.dist_fiscal=this.sucu_fiscal.getDistrito();
        this.dist_corr=this.sucu_corr.getDistrito();
        this.prov_fiscal=this.dist_fiscal.getPROV_ForJSON();
        this.prov_corr=this.dist_corr.getPROV_ForJSON();
        this.depa_fiscal=this.prov_fiscal.getDEPA_ForJSON();
        this.depa_corr=this.prov_corr.getDEPA_ForJSON();
        
        Logs.printMsg("Facturando Producto "+prod.getNO_PROD_ForJSON()+" PERIODO / A\u00d1O "+cier.getNU_PERI_ForJSON()+"/"+cier.getNU_ANO_ForJSON(),true,cier,nego);
        
        if (nego.getCO_TIPO_FACT_ForJSON().equals(2)){// 1 vencida, 2 adelantada
            Date ultima_facturacion=null;
            switch(nego.getCO_PERI_FACT_ForJSON()){//1 mensual, 2 trimestral, 3 semestral, 4 anual
                case 1:
                        this.fecha_real_inicio=FechaHora.getDate(this.cier.getNU_ANO_ForJSON(),this.cier.getNU_PERI_ForJSON()-1, 1);
                        this.fecha_real_fin=FechaHora.getUltimaFechaMes(this.fecha_real_inicio);
                        break;
                case 2:
                        ultima_facturacion=this.nego.getFechaUltimaFacturacionMenor();                     
                        this.fecha_real_inicio=FechaHora.addDays(ultima_facturacion, 1);                                
                        this.fecha_real_fin=FechaHora.addDays(FechaHora.addMonths(this.fecha_real_inicio,3), -1);
//                        this.fecha_real_fin=FechaHora.getUltimaFechaMes(FechaHora.addMonths(this.fecha_real_inicio,2));
                        break;
                case 3:
                        ultima_facturacion=this.nego.getFechaUltimaFacturacionMenor();                        
                        this.fecha_real_inicio=FechaHora.addDays(ultima_facturacion, 1);   
                        this.fecha_real_fin=FechaHora.addDays(FechaHora.addMonths(this.fecha_real_inicio,6), -1);
//                        this.fecha_real_fin=FechaHora.getUltimaFechaMes(FechaHora.addMonths(this.fecha_real_inicio,5));
                        break;
                case 4:
                        ultima_facturacion=this.nego.getFechaUltimaFacturacionMenor();                        
                        this.fecha_real_inicio=FechaHora.addDays(ultima_facturacion, 1);
                        this.fecha_real_fin=FechaHora.addDays(FechaHora.addMonths(this.fecha_real_inicio,12), -1);
//                        this.fecha_real_fin=FechaHora.getUltimaFechaMes(FechaHora.addMonths(this.fecha_real_inicio,11));
                        break;
            }   
        }else{
            this.fecha_real_inicio=FechaHora.getDate(this.cier.getNU_ANO_ForJSON(),this.cier.getNU_PERI_ForJSON()-1, 1);
            this.fecha_real_inicio=FechaHora.addMonths(this.fecha_real_inicio, -1);
            this.fecha_real_fin=FechaHora.getUltimaFechaMes(this.fecha_real_inicio);            
        }
        
        
        this.promociones_pendientes_negocio=this.nego.getPromocionesPendientes(cier);    
        this.ajustes_pendientes_todos=this.nego.getAjustesPendientes(cier);        
    }
    protected Boolean isCortado(){
        //Verifica si este objecto esta con corte sin reconexion pendiente por facturar
        for (CORT corte : cortes){ 
            if (corte.getFE_FIN_ForJSON() == null){
                return true;
            }
        }
        return false;
    }
    protected Boolean isSuspendido(List<SUSP> suspensiones){
        //Verifica si este objecto esta con suspension sin reconexion
        for (SUSP suspension : suspensiones) 
            if (suspension.getFE_FIN_ForJSON() == null) 
                return true;
        return false;
    }
    private Date getPeriodos_getFin(Date inicio){
        int meses=0;
        Date result = null;
        switch (this.nego.getCO_PERI_FACT_ForJSON()){
            case 1:
                result = FechaHora.getUltimaFechaMes(inicio);
                break;
            case 2://trimestral
                meses=3;
                result = FechaHora.addDays(FechaHora.addMonths(inicio, meses), -1);
                break;
            case 3://semestral
                meses=6;
                result = FechaHora.addDays(FechaHora.addMonths(inicio, meses), -1);
                break;
            case 4://anual
                meses=12;
                result = FechaHora.addDays(FechaHora.addMonths(inicio, meses), -1);
                break;
//                return FechaHora.getUltimaFechaMes(inicio);
        }            
        return result;
    }
    protected List<Date[]> getPeriodos(Date inicio,Date fin){
        List<Date[]> ret=new ArrayList<Date[]>();
        if (inicio.getTime()<fin.getTime()){
            Date sgte=this.getPeriodos_getFin(inicio);
            while(sgte.getTime()<fin.getTime()){
                ret.add(new Date[]{inicio,sgte});
                inicio=FechaHora.addDays(sgte, 1);
                sgte=this.getPeriodos_getFin(inicio);
            }
            ret.add(new Date[]{inicio,fin});        
        }
        return ret;
    }
    protected List<Date[]> getPeriodosCortesSuspensiones(Date fecha_activacion,Date corte_inicio,Date corte_fin){
        Date inicio=corte_inicio;
        if (inicio.getTime()<fecha_activacion.getTime())
            inicio=fecha_activacion;
        return this.getPeriodos(inicio, corte_fin);
    }
    protected List<Date[]> getPeriodos(Date fecha_activacion,Date fecha_desactivacion,Date ultima_facturacion){
        List<Date[]> ret=new ArrayList<Date[]>();
        Date inicio=null;
        Date fin=fecha_desactivacion;
        if (fin==null){
            fin=this.fecha_real_fin;
        }
        if (ultima_facturacion==null){
            inicio=fecha_activacion;
        }else{
            inicio=FechaHora.addDays(ultima_facturacion, 1);
        }
        if (!inicio.equals(fin)){
            return this.getPeriodos(inicio, fin);
        }
        return ret;
    }
    //Calcula el rango de fechas intersectando planes, cortes y suspensiones
    //fecha_inicio: Del plan o servicio suplementario
    //fecha_fin: Del plan o servicio suplementario
    protected List<Date[]> getPeriodosGeneralCortesSuspensiones(Date fecha_inicio,Date fecha_fin,Date ultima_fecha_fact,List<CORT> cortes,List<SUSP> suspensiones, Boolean considerarFechaFin){
        List<Date[]> ret=new ArrayList<Date[]>();
        List<Date[]> fechasCortSusp=new ArrayList<Date[]>();
        Date inicio=null;
        Date fin=fecha_fin;
        Date fin_cortes_susp=null;
        
        if(cortes.isEmpty() && suspensiones.isEmpty()){
            if (fin==null){
                fin=this.fecha_real_fin;
            }
            
            if (ultima_fecha_fact==null){
                inicio=fecha_inicio;
            } else{
                inicio=FechaHora.addDays(ultima_fecha_fact, 1);
            }
            
            if (!inicio.equals(fin)){
                ret=this.getPeriodos(inicio, fin);
            }
        } else {
            if (fin != null){
                fin=FechaHora.addDays(fin, 1);
            }
            Boolean incluye;
            for (CORT cort : cortes){
                fin_cortes_susp=fin;
                incluye=true;
                if (cort.getFE_FIN_ForJSON()==null){
                    if (fin_cortes_susp==null){
                        fin_cortes_susp=FechaHora.addYears(this.fecha_real_fin, 8);
                    } else {
                        if (fin_cortes_susp.getTime()<cort.getFE_INIC_ForJSON().getTime()){
                            if (ultima_fecha_fact!=null){
                                if (cort.getFE_INIC_ForJSON().getTime()>ultima_fecha_fact.getTime()){
                                    incluye=false;
                                }
                               if (considerarFechaFin){
                                   fin_cortes_susp=FechaHora.addYears(this.fecha_real_fin, 8);
                               } 
                            } else {
                                incluye=false;
                            }
                        }
                    }
                    if (incluye && cort.getFE_INIC_ForJSON().getTime()!=fin_cortes_susp.getTime()){
                        fechasCortSusp.add(new Date[]{cort.getFE_INIC_ForJSON(),fin_cortes_susp}); 
                    }
                } else {
                    if (fecha_inicio.getTime()<=cort.getFE_FIN_ForJSON().getTime()){
                        if (fin_cortes_susp==null){
                            fin_cortes_susp=cort.getFE_FIN_ForJSON();
                        } else {
                            if (fin_cortes_susp.getTime()<cort.getFE_INIC_ForJSON().getTime()){
                                if (ultima_fecha_fact!=null){
                                    if (cort.getFE_INIC_ForJSON().getTime()>ultima_fecha_fact.getTime()){
                                        incluye=false;
                                    }
                                    if (considerarFechaFin){
                                        fin_cortes_susp=cort.getFE_FIN_ForJSON();
                                    }
                                } else {
                                    incluye=false;
                                }
                            }
                            if (cort.getFE_FIN_ForJSON().getTime()<fin_cortes_susp.getTime()){
                                fin_cortes_susp=cort.getFE_FIN_ForJSON();
                            }
                        }
                        if (incluye && cort.getFE_INIC_ForJSON().getTime()!=fin_cortes_susp.getTime()){
                            fechasCortSusp.add(new Date[]{cort.getFE_INIC_ForJSON(),fin_cortes_susp});  
                        }
                    }
                }
                
            }
            int contador=0;
            if (!cortes.isEmpty()){
                for (SUSP susp : suspensiones){
                    fin_cortes_susp=fin;
                    contador=0;
                    incluye=true;
                    for (Date[] date : fechasCortSusp){
                        contador++;
                        if (susp.getFE_INIC_ForJSON().getTime()<date[0].getTime()){
                            int index=fechasCortSusp.indexOf(date);
//                            fin_cortes_susp=fin;
                            if (susp.getFE_FIN_ForJSON()==null){
                                if (fin_cortes_susp==null){
                                    fin_cortes_susp=FechaHora.addYears(this.fecha_real_fin, 8);
                                } else {
                                    if (fin_cortes_susp.getTime()<susp.getFE_INIC_ForJSON().getTime()){
                                        if (ultima_fecha_fact!=null){
                                            if (susp.getFE_INIC_ForJSON().getTime()>ultima_fecha_fact.getTime()){
                                                incluye=false;
                                            }
                                            if (considerarFechaFin){
                                                fin_cortes_susp=FechaHora.addYears(this.fecha_real_fin, 8);
                                            }
                                        } else {
                                            incluye=false;
                                        }
                                    }
                                }
                                if (incluye && susp.getFE_INIC_ForJSON().getTime()!=fin_cortes_susp.getTime()){
                                    fechasCortSusp.add(index, new Date[]{susp.getFE_INIC_ForJSON(),fin_cortes_susp});
                                    contador=0;
                                    break;
                                }
                            } else {
                                if (fecha_inicio.getTime()<=susp.getFE_FIN_ForJSON().getTime() && susp.getFE_INIC_ForJSON().getTime()!=susp.getFE_FIN_ForJSON().getTime()){
                                    if (fin_cortes_susp==null){
                                        fin_cortes_susp=susp.getFE_FIN_ForJSON();
                                    } else {
                                        if (fin_cortes_susp.getTime()<susp.getFE_INIC_ForJSON().getTime()){
                                            if (ultima_fecha_fact!=null){
                                                if (susp.getFE_INIC_ForJSON().getTime()>ultima_fecha_fact.getTime()){
                                                    incluye=false;
                                                }
                                                if (considerarFechaFin){
                                                    fin_cortes_susp=susp.getFE_FIN_ForJSON();
                                                }
                                            } else {
                                                incluye=false;
                                            }
                                        }
                                        if (susp.getFE_FIN_ForJSON().getTime()<fin_cortes_susp.getTime()){
                                            fin_cortes_susp=susp.getFE_FIN_ForJSON();
                                        }
                                    }
                                    if (incluye && susp.getFE_INIC_ForJSON().getTime()!=fin_cortes_susp.getTime()){
                                        fechasCortSusp.add(index, new Date[]{susp.getFE_INIC_ForJSON(),fin_cortes_susp});
                                        contador=0;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    if(contador==fechasCortSusp.size()){
                        fin_cortes_susp=fin;
                        if (susp.getFE_FIN_ForJSON()==null){
                            if (fin_cortes_susp==null){
                                fin_cortes_susp=FechaHora.addYears(this.fecha_real_fin, 8);
                            } else {
                                if (fin_cortes_susp.getTime()<susp.getFE_INIC_ForJSON().getTime()){
                                    if (ultima_fecha_fact!=null){
                                        if (susp.getFE_INIC_ForJSON().getTime()>ultima_fecha_fact.getTime()){
                                            incluye=false;
                                        }
                                        if (considerarFechaFin){
                                            fin_cortes_susp=FechaHora.addYears(this.fecha_real_fin, 8);
                                        }
                                    } else {
                                        incluye=false;
                                    }
                                }
                            }
                            if (incluye && susp.getFE_INIC_ForJSON().getTime()!=fin_cortes_susp.getTime()){
                                fechasCortSusp.add(new Date[]{susp.getFE_INIC_ForJSON(),fin_cortes_susp});
                            }
                        } else {
                            if (fecha_inicio.getTime()<=susp.getFE_FIN_ForJSON().getTime() && susp.getFE_INIC_ForJSON().getTime()!=susp.getFE_FIN_ForJSON().getTime()){
                                if (fin_cortes_susp==null){
                                    fin_cortes_susp=susp.getFE_FIN_ForJSON();
                                } else {
                                    if (fin_cortes_susp.getTime()<susp.getFE_INIC_ForJSON().getTime()){
                                        if (ultima_fecha_fact!=null){
                                            if (susp.getFE_INIC_ForJSON().getTime()>ultima_fecha_fact.getTime()){
                                                incluye=false;
                                            }
                                            if (considerarFechaFin){
                                                fin_cortes_susp=susp.getFE_FIN_ForJSON();
                                            }
                                        } else {
                                            incluye=false;
                                        }
                                    }
                                    if (susp.getFE_FIN_ForJSON().getTime()<fin_cortes_susp.getTime()){
                                        fin_cortes_susp=susp.getFE_FIN_ForJSON();
                                    }
                                }
                                if (incluye && susp.getFE_INIC_ForJSON().getTime()!=fin_cortes_susp.getTime()){
                                    fechasCortSusp.add(new Date[]{susp.getFE_INIC_ForJSON(),fin_cortes_susp});
                                }
                            }
                        }
                    }
                }
            } else {
                for (SUSP susp : suspensiones){
                    incluye=true;
                    fin_cortes_susp=fin;
                    if (susp.getFE_FIN_ForJSON()==null){
                        if (fin_cortes_susp==null){
                            fin_cortes_susp=FechaHora.addYears(this.fecha_real_fin, 8);
                        } else {
                            if (fin_cortes_susp.getTime()<susp.getFE_INIC_ForJSON().getTime()){
                                if (ultima_fecha_fact!=null){
                                    if (susp.getFE_INIC_ForJSON().getTime()>ultima_fecha_fact.getTime()){
                                        incluye=false;
                                    }
                                    if (considerarFechaFin){
                                        fin_cortes_susp=FechaHora.addYears(this.fecha_real_fin, 8);
                                    }
                                } else {
                                    incluye=false;
                                }
                            }
                        }
                        if (incluye && susp.getFE_INIC_ForJSON().getTime()!=fin_cortes_susp.getTime()){
                            fechasCortSusp.add(new Date[]{susp.getFE_INIC_ForJSON(),fin_cortes_susp});
                        }
                    } else {
                        if (fecha_inicio.getTime()<=susp.getFE_FIN_ForJSON().getTime() && susp.getFE_INIC_ForJSON().getTime()!=susp.getFE_FIN_ForJSON().getTime()){
                            if (fin_cortes_susp==null){
                                fin_cortes_susp=susp.getFE_FIN_ForJSON();
                            } else {
                                if (fin_cortes_susp.getTime()<susp.getFE_INIC_ForJSON().getTime()){
                                    if (ultima_fecha_fact!=null){
                                        if (susp.getFE_INIC_ForJSON().getTime()>ultima_fecha_fact.getTime()){
                                            incluye=false;
                                        }
                                        if (considerarFechaFin){
                                            fin_cortes_susp=susp.getFE_FIN_ForJSON();
                                        }
                                    } else {
                                        incluye=false;
                                    }
                                }
                                if (susp.getFE_FIN_ForJSON().getTime()<fin_cortes_susp.getTime()){
                                    fin_cortes_susp=susp.getFE_FIN_ForJSON();
                                }
                            }
                            if (incluye && susp.getFE_INIC_ForJSON().getTime()!=fin_cortes_susp.getTime()){
                                fechasCortSusp.add(new Date[]{susp.getFE_INIC_ForJSON(),fin_cortes_susp});
                            }
                        }
                    }
                }
            }
            
            // Unimos todos los periodos de cortes y suspensiones
            contador=0;
            Date[] periodo_inicial=null;
            for(Date[] periodo : fechasCortSusp){
                contador++;
                if (contador==1){
                    periodo_inicial=periodo;
                    if (fechasCortSusp.size()==1){
                        ret.add(periodo_inicial);
                    }
                } else {
                    Date[] fechaUnion=FechaHora.getFechaUnion(periodo_inicial, periodo);
                    if (fechaUnion==null){
                        if (!ret.contains(periodo_inicial)){
                            ret.add(periodo_inicial);
                        }
                        if(fechasCortSusp.size()==contador){
                            if (!ret.contains(periodo)){
                                ret.add(periodo);
                            }
                        }
                        periodo_inicial=periodo;
                    } else {
                        if (!ret.contains(fechaUnion)){
                            ret.add(fechaUnion);
                        }
                        periodo_inicial=fechaUnion;
                    }
                }
            }
            
        }
        
        return ret;
    }
    
    private Double getMonto(Date inicio,Date fin,Double normal){//Resultados son positivos
        Integer completo=FechaHora.getDiffDays(FechaHora.getPrimeraFechaMes(inicio), FechaHora.getUltimaFechaMes(fin));
        Integer dias=FechaHora.getDiffDays(inicio, fin);
        switch(this.nego.getCO_PERI_FACT_ForJSON()){
            case 1://mensual
                    if (!completo.equals(dias)){            
                        normal=(normal/completo)*dias;
                    }
                    break;
            case 2://trimestral
                    normal=(normal)*3;
                    break;
            case 3://semestral
                    normal=(normal)*6;
                    break;
            case 4://anual
                    normal=(normal)*12;
                    break;  
            /*
            case 2://trimestral
            case 3://semestral
            case 4://anual
                    normal=(normal/30d)*dias;
                    break;         
            */
        }
        return normal;
    }
    
    private void evaluarPlanPeriodosYaCobrados(List<CIER_DATA_PLAN> lista,List<CIER_DATA_PLAN> cobrados,NEGO_SUCU_PLAN plan,Date fecha_inicio,Date fecha_fin, Boolean finPeriodo){
        //buscamos si ya cobramos el mismo periodo  en detalle del plan                            
        CIER_DATA_PLAN deta_plan_tmp=null;
        CIER_DATA_PLAN deta_plan=new CIER_DATA_PLAN();
        deta_plan.setCO_NEGO_SUCU_PLAN(plan.getCO_NEGO_SUCU_PLAN_ForJSON());
        deta_plan.setFE_INIC(fecha_inicio);
        deta_plan.setFE_FIN(fecha_fin);
        deta_plan.setIM_MONT(this.getMonto(fecha_inicio,fecha_fin,plan.getIM_MONTO_ForJSON()));      
        deta_plan.setDE_NOMB(fnc.capitalize(plan.getNO_PLAN_ForJSON()));
        deta_plan.setST_TIPO_COBR(1);
        deta_plan.setIS_UPGRADE(false);
        deta_plan.setCO_MONE_FACT(plan.getPlan_ForJSON().getCO_MONE_FACT_ForJSON());
        boolean ya_cobrado=false;
        for(CIER_DATA_PLAN cobrado : cobrados){
            Date cruce[]=FechaHora.getFechaIntersecta(new Date[]{fecha_inicio,fecha_fin}, new Date[]{cobrado.getFE_INIC(),cobrado.getFE_FIN()});
            if (cruce!=null){                
                Double antes=cobrado.getMontoPorDia();
                Double ahora=deta_plan.getMontoPorDia();
                Double diffPositivo=Math.abs(antes-ahora);
                Integer diasSon=FechaHora.getDiffDays(cruce[0], cruce[1]);
                if (ahora>antes){
                    //upgrade                    
                    deta_plan_tmp=new CIER_DATA_PLAN();
                    deta_plan_tmp.setCO_NEGO_SUCU_PLAN(plan.getCO_NEGO_SUCU_PLAN_ForJSON());
                    deta_plan_tmp.setFE_INIC(cruce[0]);
                    deta_plan_tmp.setFE_FIN(cruce[1]);
                    deta_plan_tmp.setIM_MONT(diasSon*diffPositivo);      
                    deta_plan_tmp.setDE_NOMB(fnc.capitalize(plan.getNO_PLAN_ForJSON()));
                    deta_plan_tmp.setST_TIPO_COBR(2);
                    deta_plan_tmp.setIS_UPGRADE(true);
                    deta_plan_tmp.setCO_MONE_FACT(plan.getPlan_ForJSON().getCO_MONE_FACT_ForJSON());
                    lista.add(deta_plan_tmp);
                }else{
                    //downgrade
                    deta_plan_tmp=new CIER_DATA_PLAN();
                    deta_plan_tmp.setCO_NEGO_SUCU_PLAN(plan.getCO_NEGO_SUCU_PLAN_ForJSON());
                    deta_plan_tmp.setFE_INIC(cruce[0]);
                    deta_plan_tmp.setFE_FIN(cruce[1]);
                    deta_plan_tmp.setIM_MONT(diasSon*diffPositivo);      
                    deta_plan_tmp.setDE_NOMB(fnc.capitalize(plan.getNO_PLAN_ForJSON()));
                    deta_plan_tmp.setST_TIPO_DEVO(5);
                    deta_plan_tmp.setIS_UPGRADE(false);
                    deta_plan_tmp.setCO_MONE_FACT(plan.getPlan_ForJSON().getCO_MONE_FACT_ForJSON());
                    lista.add(deta_plan_tmp);                    
                }                
                ya_cobrado=true;
                // [INICIO NUEVO]
                // Esto es para el caso de un downgrade/upgrade y baja. Se tiene que devolver el monto de la baja
                /*
                if (deta_plan.getFE_FIN() != null && finPeriodo){
                    Date fecha_inic_cobrado=FechaHora.addDays(deta_plan.getFE_FIN(), 1);
                    Date fecha_fin_cobrado=FechaHora.getUltimaFechaMes(fecha_inic_cobrado);
                    deta_plan_tmp=new CIER_DATA_PLAN();
                    deta_plan_tmp.setCO_NEGO_SUCU_PLAN(cobrado.getCO_NEGO_SUCU_PLAN());
                    deta_plan_tmp.setFE_INIC(fecha_inic_cobrado);
                    deta_plan_tmp.setFE_FIN(fecha_fin_cobrado);
                    deta_plan_tmp.setIM_MONT(this.getMonto(fecha_inic_cobrado,fecha_fin_cobrado,cobrado.getIM_MONT()));      
                    deta_plan_tmp.setDE_NOMB(fnc.capitalize(cobrado.getDE_NOMB()));
                    deta_plan_tmp.setST_TIPO_DEVO(1);
                    deta_plan_tmp.setCO_MONE_FACT(plan.getPlan_ForJSON().getCO_MONE_FACT_ForJSON());
                    lista.add(deta_plan_tmp); 
                }*/
                // [FIN NUEVO]
            }
        }          
        if (!ya_cobrado){
            lista.add(deta_plan);
        }
    }
    private Double evaluarSaldoAproximadoParaPlanes(List<CIER_DATA_PLAN> cobrados,NEGO_SUCU_PLAN plan,Date fecha_inicio,Date fecha_fin,List<NEGO_SUCU_PROM> promociones_pendientes_sucursal){
        Double monto=0d;
        for(CIER_DATA_PLAN cobrado : cobrados){
            Date cruce[]=FechaHora.getFechaIntersecta(new Date[]{fecha_inicio,fecha_fin}, new Date[]{cobrado.getFE_INIC(),cobrado.getFE_FIN()});            
            if (cruce!=null){
                Double montoPromocion=this.getMontoPromocionPlan(plan,promociones_pendientes_sucursal,cruce[0],cruce[1]);
                monto = this.getMonto(cruce[0],cruce[1],plan.getIM_MONTO_ForJSON())-montoPromocion;               
            }  
        }
        return monto;
    }
    private Double evaluarSaldoAproximadoParaServSupl(List<CIER_DATA_SERV_SUPL> cobrados,NEGO_SUCU_SERV_SUPL servicio_suplementario,Date fecha_inicio,Date fecha_fin,List<NEGO_SUCU_PROM> promociones_pendientes_sucursal){
        Double monto=0d;
        for(CIER_DATA_SERV_SUPL cobrado : cobrados){
            Date cruce[]=FechaHora.getFechaIntersecta(new Date[]{fecha_inicio,fecha_fin}, new Date[]{cobrado.getFE_INIC(),cobrado.getFE_FIN()});            
            if (cruce!=null){
                Double montoPromocion=this.getMontoPromocionServSuplementarios(servicio_suplementario,promociones_pendientes_sucursal,cruce[0],cruce[1]);
                monto = this.getMonto(cruce[0],cruce[1],servicio_suplementario.getIM_MONTO_ForJSON())-montoPromocion;               
            }  
        }
        return monto;
    }
    private void evaluarCortesSuspencionesServSuplPeriodosYaCobrados(List<CIER_DATA_SERV_SUPL> lista,List<CIER_DATA_SERV_SUPL> cobrados,NEGO_SUCU_SERV_SUPL servicio_suplementario,Date fecha_inicio,Date fecha_fin,Boolean ultimo_continuo, List<NEGO_SUCU_PROM> promociones_pendientes_sucursal, Boolean consideraDev){
        boolean ya_cobrado=false;
        for(CIER_DATA_SERV_SUPL cobrado : cobrados){
            Date cruce[]=FechaHora.getFechaIntersecta(new Date[]{fecha_inicio,fecha_fin}, new Date[]{cobrado.getFE_INIC(),cobrado.getFE_FIN()});            
            if (cruce!=null){ 
                //cuando los periodos son continuos significa que los el mismo perido y no se debe restar un dia como
                //la funcion evaluarCortesPlanPeriodosYaCobrados quita un dia antes le sumamos un dia
                //hasta que no sea el ultimo
                if (ultimo_continuo){
                    //por regla el ultimo dia ya tiene servicio por tanto se resta uno, si no son cortes continuos
                    cruce[1]=FechaHora.addDays(cruce[1], -1);                
                }
                Double montoPromocion=this.getMontoPromocionServSuplementarios(servicio_suplementario,promociones_pendientes_sucursal,cruce[0],cruce[1]);
                CIER_DATA_SERV_SUPL deta_serv_supl=new CIER_DATA_SERV_SUPL();
                deta_serv_supl.setCO_NEGO_SUCU_SERV_SUPL(servicio_suplementario.getCO_NEGO_SUCU_SERV_SUPL_ForJSON());
                deta_serv_supl.setFE_INIC(cruce[0]);
                deta_serv_supl.setFE_FIN(cruce[1]);
                deta_serv_supl.setIM_MONT(this.getMonto(cruce[0],cruce[1],servicio_suplementario.getIM_MONTO_ForJSON())-montoPromocion);      
                deta_serv_supl.setDE_NOMB(fnc.capitalize(servicio_suplementario.getNO_SERV_SUPL_ForJSON()));
                deta_serv_supl.setST_TIPO_DEVO(2);
                deta_serv_supl.setST_AFEC_DETR(servicio_suplementario.getST_AFEC_DETR_ForJSON());
                deta_serv_supl.setCO_MONE_FACT(servicio_suplementario.getServ_Supl_ForJSON().getCO_MONE_FACT_ForJSON());
                lista.add(deta_serv_supl);                
                ya_cobrado=true;
            }  
        }
        if (!ya_cobrado && consideraDev){
            Date fecha_fin_aux;
            if (ultimo_continuo){
                //por regla el ultimo dia ya tiene servicio por tanto se resta uno, si no son cortes continuos
                fecha_fin_aux = FechaHora.addDays(fecha_fin, -1);                
            } else {
                fecha_fin_aux = fecha_fin;
            }
            Double montoPromocion=this.getMontoPromocionServSuplementarios(servicio_suplementario,promociones_pendientes_sucursal,fecha_inicio,fecha_fin_aux);
            CIER_DATA_SERV_SUPL deta_serv_supl=new CIER_DATA_SERV_SUPL();
            deta_serv_supl.setCO_NEGO_SUCU_SERV_SUPL(servicio_suplementario.getCO_NEGO_SUCU_SERV_SUPL_ForJSON());
            deta_serv_supl.setFE_INIC(fecha_inicio);
            deta_serv_supl.setFE_FIN(fecha_fin_aux);
            deta_serv_supl.setIM_MONT(this.getMonto(fecha_inicio,fecha_fin_aux,servicio_suplementario.getIM_MONTO_ForJSON())-montoPromocion);      
            deta_serv_supl.setDE_NOMB(fnc.capitalize(servicio_suplementario.getNO_SERV_SUPL_ForJSON()));
            deta_serv_supl.setST_TIPO_DEVO(2);
            deta_serv_supl.setST_AFEC_DETR(servicio_suplementario.getST_AFEC_DETR_ForJSON());
            deta_serv_supl.setCO_MONE_FACT(servicio_suplementario.getServ_Supl_ForJSON().getCO_MONE_FACT_ForJSON());
            lista.add(deta_serv_supl);  
        }
    }
    private void evaluarCortesSuspensionesPlanPeriodosYaCobrados(List<CIER_DATA_PLAN> lista,List<CIER_DATA_PLAN> cobrados,NEGO_SUCU_PLAN plan,Date fecha_inicio,Date fecha_fin,Boolean ultimo_continuo, List<NEGO_SUCU_PROM> promociones_pendientes_sucursal, Boolean consideraDev){
        boolean ya_cobrado=false;
        for(CIER_DATA_PLAN cobrado : cobrados){
            Date cruce[]=FechaHora.getFechaIntersecta(new Date[]{fecha_inicio,fecha_fin}, new Date[]{cobrado.getFE_INIC(),cobrado.getFE_FIN()});            
            if (cruce!=null){ 
                //cuando los periodos son continuos significa que los el mismo perido y no se debe restar un dia como
                //la funcion evaluarCortesPlanPeriodosYaCobrados quita un dia antes le sumamos un dia
                //hasta que no sea el ultimo
                if (ultimo_continuo){
                    //por regla el ultimo dia ya tiene servicio por tanto se resta uno, si no son cortes continuos
                    cruce[1]=FechaHora.addDays(cruce[1], -1);                
                }
                Double montoPromocion=this.getMontoPromocionPlan(plan,promociones_pendientes_sucursal,cruce[0],cruce[1]);
                CIER_DATA_PLAN deta_plan=new CIER_DATA_PLAN();
                deta_plan.setCO_NEGO_SUCU_PLAN(plan.getCO_NEGO_SUCU_PLAN_ForJSON());
                deta_plan.setFE_INIC(cruce[0]);
                deta_plan.setFE_FIN(cruce[1]);
                deta_plan.setIM_MONT(this.getMonto(cruce[0],cruce[1],plan.getIM_MONTO_ForJSON())-montoPromocion);      
                deta_plan.setDE_NOMB(fnc.capitalize(plan.getNO_PLAN_ForJSON()));
                deta_plan.setST_TIPO_DEVO(2);
                deta_plan.setCO_MONE_FACT(plan.getPlan_ForJSON().getCO_MONE_FACT_ForJSON());
                lista.add(deta_plan);                
                ya_cobrado=true;
            }  
        }
        if (!ya_cobrado && consideraDev){
            Date fecha_fin_aux;
            if (ultimo_continuo){
                //por regla el ultimo dia ya tiene servicio por tanto se resta uno, si no son cortes continuos
                fecha_fin_aux = FechaHora.addDays(fecha_fin, -1);                
            } else {
                fecha_fin_aux = fecha_fin;
            }
            Double montoPromocion=this.getMontoPromocionPlan(plan,promociones_pendientes_sucursal,fecha_inicio,fecha_fin_aux);
            CIER_DATA_PLAN deta_plan=new CIER_DATA_PLAN();
            deta_plan.setCO_NEGO_SUCU_PLAN(plan.getCO_NEGO_SUCU_PLAN_ForJSON());
            deta_plan.setFE_INIC(fecha_inicio);
            deta_plan.setFE_FIN(fecha_fin);
            deta_plan.setIM_MONT(this.getMonto(fecha_inicio,fecha_fin_aux,plan.getIM_MONTO_ForJSON())-montoPromocion);      
            deta_plan.setDE_NOMB(fnc.capitalize(plan.getNO_PLAN_ForJSON()));
            deta_plan.setST_TIPO_DEVO(2);
            deta_plan.setCO_MONE_FACT(plan.getPlan_ForJSON().getCO_MONE_FACT_ForJSON());
            lista.add(deta_plan);  
        }
    }
    private Double getMontoPromocionServSuplementarios(NEGO_SUCU_SERV_SUPL servicio_suplementario,List<NEGO_SUCU_PROM> promociones_pendientes_sucursal, Date inicio, Date fin) {        
        Double monto=0d;
        if (this.promociones_pendientes_negocio.size()>0){
            for(NEGO_PROM nego_promo : this.promociones_pendientes_negocio){
                Date ini_promo=FechaHora.getDate(nego_promo.getDE_ANO_INIC_ForJSON(), nego_promo.getDE_PERI_INIC_ForJSON()-1, 1);
                Date fin_promo=FechaHora.getUltimaFechaMes(FechaHora.getDate(nego_promo.getDE_ANO_FIN_ForJSON(), nego_promo.getDE_PERI_FIN_ForJSON()-1, 1));                
                Date cruce []=FechaHora.getFechaIntersecta(new Date[]{inicio,fin}, new Date[]{ini_promo,fin_promo});
                if (cruce !=null){                
                    if (nego_promo.getST_SERV_SUPL_ForJSON()){ //Verificar si la promocion aplica al plan
                        if (nego_promo.getDE_TIPO_ForJSON().equals(1)){//1 porcentaje, 2 Monto
                            monto=this.getMonto(cruce[0],cruce[1],servicio_suplementario.getIM_MONTO_ForJSON())*(nego_promo.getIM_VALO_ForJSON()/100d);
                        } else {
                            monto=this.getMonto(cruce[0],cruce[1],nego_promo.getIM_VALO_ForJSON());
                        }
                    } 
                }
            }
        }else if (promociones_pendientes_sucursal.size()>0){
            for(NEGO_SUCU_PROM nego_sucu_promo : promociones_pendientes_sucursal){
                Date ini_promo=FechaHora.getDate(nego_sucu_promo.getDE_ANO_INIC_ForJSON(), nego_sucu_promo.getDE_PERI_INIC_ForJSON()-1, 1);
                Date fin_promo=FechaHora.getUltimaFechaMes(FechaHora.getDate(nego_sucu_promo.getDE_ANO_FIN_ForJSON(), nego_sucu_promo.getDE_PERI_FIN_ForJSON()-1, 1));                
                Date cruce []=FechaHora.getFechaIntersecta(new Date[]{inicio,fin}, new Date[]{ini_promo,fin_promo});
                if (cruce !=null){
                    if (nego_sucu_promo.getST_SERV_SUPL_ForJSON()){ //Verificar si la promocion aplica al plan
                        if (nego_sucu_promo.getDE_TIPO_ForJSON().equals(1)){//1 porcentaje, 2 Monto
                            monto=this.getMonto(cruce[0],cruce[1],servicio_suplementario.getIM_MONTO_ForJSON())*(nego_sucu_promo.getIM_VALO_ForJSON()/100d);
                        } else {
                            monto=this.getMonto(cruce[0],cruce[1],nego_sucu_promo.getIM_VALO_ForJSON());
                        }  
                    }
                }
            }            
        }
        return monto;
    }
    private Double getMontoPromocionPlan(NEGO_SUCU_PLAN plan,List<NEGO_SUCU_PROM> promociones_pendientes_sucursal, Date inicio, Date fin) {        
        Double monto=0d;
        if (this.promociones_pendientes_negocio.size()>0){
            for(NEGO_PROM nego_promo : this.promociones_pendientes_negocio){
                Date ini_promo=FechaHora.getDate(nego_promo.getDE_ANO_INIC_ForJSON(), nego_promo.getDE_PERI_INIC_ForJSON()-1, 1);
                Date fin_promo=FechaHora.getUltimaFechaMes(FechaHora.getDate(nego_promo.getDE_ANO_FIN_ForJSON(), nego_promo.getDE_PERI_FIN_ForJSON()-1, 1));                
                Date cruce []=FechaHora.getFechaIntersecta(new Date[]{inicio,fin}, new Date[]{ini_promo,fin_promo});
                if (cruce !=null){                
                    if (nego_promo.getST_PLAN_ForJSON()){ //Verificar si la promocion aplica al plan
                        if (nego_promo.getDE_TIPO_ForJSON().equals(1)){//1 porcentaje, 2 Monto
                            monto=this.getMonto(cruce[0],cruce[1],plan.getIM_MONTO_ForJSON())*(nego_promo.getIM_VALO_ForJSON()/100d);
                        } else {
                            monto=this.getMonto(cruce[0],cruce[1],nego_promo.getIM_VALO_ForJSON());
                        }
                    } 
                }
            }
        }else if (promociones_pendientes_sucursal.size()>0){
            for(NEGO_SUCU_PROM nego_sucu_promo : promociones_pendientes_sucursal){
                Date ini_promo=FechaHora.getDate(nego_sucu_promo.getDE_ANO_INIC_ForJSON(), nego_sucu_promo.getDE_PERI_INIC_ForJSON()-1, 1);
                Date fin_promo=FechaHora.getUltimaFechaMes(FechaHora.getDate(nego_sucu_promo.getDE_ANO_FIN_ForJSON(), nego_sucu_promo.getDE_PERI_FIN_ForJSON()-1, 1));                
                Date cruce []=FechaHora.getFechaIntersecta(new Date[]{inicio,fin}, new Date[]{ini_promo,fin_promo});
                if (cruce !=null){
                    if (nego_sucu_promo.getST_PLAN_ForJSON()){ //Verificar si la promocion aplica al plan
                        if (nego_sucu_promo.getDE_TIPO_ForJSON().equals(1)){//1 porcentaje, 2 Monto
                            monto=this.getMonto(cruce[0],cruce[1],plan.getIM_MONTO_ForJSON())*(nego_sucu_promo.getIM_VALO_ForJSON()/100d);
                        } else {
                            monto=this.getMonto(cruce[0],cruce[1],nego_sucu_promo.getIM_VALO_ForJSON());
                        }  
                    }
                }
            }            
        }
        return monto;
    }
    private void evaluarPromocionPlan(List<CIER_DATA_PLAN> lista,NEGO_SUCU_PLAN plan,List<NEGO_SUCU_PROM> promociones_pendientes_sucursal, Date inicio, Date fin) {        
        if (this.promociones_pendientes_negocio.size()>0){
            for(NEGO_PROM nego_promo : this.promociones_pendientes_negocio){
                Date ini_promo=FechaHora.getDate(nego_promo.getDE_ANO_INIC_ForJSON(), nego_promo.getDE_PERI_INIC_ForJSON()-1, 1);
                Date fin_promo=FechaHora.getUltimaFechaMes(FechaHora.getDate(nego_promo.getDE_ANO_FIN_ForJSON(), nego_promo.getDE_PERI_FIN_ForJSON()-1, 1));                
                Date cruce []=FechaHora.getFechaIntersecta(new Date[]{inicio,fin}, new Date[]{ini_promo,fin_promo});
                if (cruce !=null){                
                    if (nego_promo.getST_PLAN_ForJSON()){ //Verificar si la promocion aplica al plan
                        if (nego_promo.getDE_TIPO_ForJSON().equals(1)){//1 porcentaje, 2 Monto
                            Double monto=this.getMonto(cruce[0],cruce[1],plan.getIM_MONTO_ForJSON())*(nego_promo.getIM_VALO_ForJSON()/100d);
                            CIER_DATA_PLAN deta_plan=new CIER_DATA_PLAN();
                            deta_plan.setCO_NEGO_SUCU_PLAN(plan.getCO_NEGO_SUCU_PLAN_ForJSON());
                            deta_plan.setFE_INIC(cruce[0]);
                            deta_plan.setFE_FIN(cruce[1]);
                            deta_plan.setIM_MONT(monto);      
                            deta_plan.setDE_NOMB(fnc.capitalize(plan.getNO_PLAN_ForJSON()));
                            deta_plan.setST_TIPO_DEVO(4);
                            deta_plan.setCO_MONE_FACT(plan.getPlan_ForJSON().getCO_MONE_FACT_ForJSON());
                            lista.add(deta_plan);
                        } else {
                            Double monto=this.getMonto(cruce[0],cruce[1],nego_promo.getIM_VALO_ForJSON());
                            CIER_DATA_PLAN deta_plan=new CIER_DATA_PLAN();
                            deta_plan.setCO_NEGO_SUCU_PLAN(plan.getCO_NEGO_SUCU_PLAN_ForJSON());
                            deta_plan.setFE_INIC(cruce[0]);
                            deta_plan.setFE_FIN(cruce[1]);
                            deta_plan.setIM_MONT(monto);      
                            deta_plan.setDE_NOMB(fnc.capitalize(plan.getNO_PLAN_ForJSON()));
                            deta_plan.setST_TIPO_DEVO(4);
                            deta_plan.setCO_MONE_FACT(plan.getPlan_ForJSON().getCO_MONE_FACT_ForJSON());
                            lista.add(deta_plan);
                        }
                    } 
                }
            }
        }else if (promociones_pendientes_sucursal.size()>0){
            for(NEGO_SUCU_PROM nego_sucu_promo : promociones_pendientes_sucursal){
                Date ini_promo=FechaHora.getDate(nego_sucu_promo.getDE_ANO_INIC_ForJSON(), nego_sucu_promo.getDE_PERI_INIC_ForJSON()-1, 1);
                Date fin_promo=FechaHora.getUltimaFechaMes(FechaHora.getDate(nego_sucu_promo.getDE_ANO_FIN_ForJSON(), nego_sucu_promo.getDE_PERI_FIN_ForJSON()-1, 1));                
                Date cruce []=FechaHora.getFechaIntersecta(new Date[]{inicio,fin}, new Date[]{ini_promo,fin_promo});
                if (cruce !=null){
                    if (nego_sucu_promo.getST_PLAN_ForJSON()){ //Verificar si la promocion aplica al plan
                        if (nego_sucu_promo.getDE_TIPO_ForJSON().equals(1)){//1 porcentaje, 2 Monto
                            Double monto=this.getMonto(cruce[0],cruce[1],plan.getIM_MONTO_ForJSON())*(nego_sucu_promo.getIM_VALO_ForJSON()/100d);
                            CIER_DATA_PLAN deta_plan=new CIER_DATA_PLAN();
                            deta_plan.setCO_NEGO_SUCU_PLAN(plan.getCO_NEGO_SUCU_PLAN_ForJSON());
                            deta_plan.setFE_INIC(cruce[0]);
                            deta_plan.setFE_FIN(cruce[1]);
                            deta_plan.setIM_MONT(monto);      
                            deta_plan.setDE_NOMB(fnc.capitalize(plan.getNO_PLAN_ForJSON()));
                            deta_plan.setST_TIPO_DEVO(4);
                            deta_plan.setCO_MONE_FACT(plan.getPlan_ForJSON().getCO_MONE_FACT_ForJSON());
                            lista.add(deta_plan);
                        } else {
                            Double monto=this.getMonto(cruce[0],cruce[1],nego_sucu_promo.getIM_VALO_ForJSON());
                            CIER_DATA_PLAN deta_plan=new CIER_DATA_PLAN();
                            deta_plan.setCO_NEGO_SUCU_PLAN(plan.getCO_NEGO_SUCU_PLAN_ForJSON());
                            deta_plan.setFE_INIC(cruce[0]);
                            deta_plan.setFE_FIN(cruce[1]);
                            deta_plan.setIM_MONT(monto);      
                            deta_plan.setDE_NOMB(fnc.capitalize(plan.getNO_PLAN_ForJSON()));
                            deta_plan.setST_TIPO_DEVO(4);
                            deta_plan.setCO_MONE_FACT(plan.getPlan_ForJSON().getCO_MONE_FACT_ForJSON());
                            lista.add(deta_plan);
                        }  
                    }
                }
            }            
        }
    } 
    private void evaluarPromocionServicioSuplementario(List<CIER_DATA_SERV_SUPL> lista,NEGO_SUCU_SERV_SUPL servicio_suplementario,List<NEGO_SUCU_PROM> promociones_pendientes_sucursal, Date inicio, Date fin) {        
        if (this.promociones_pendientes_negocio.size()>0){
            for(NEGO_PROM nego_promo : this.promociones_pendientes_negocio){
                Date ini_promo=FechaHora.getDate(nego_promo.getDE_ANO_INIC_ForJSON(), nego_promo.getDE_PERI_INIC_ForJSON()-1, 1);
                Date fin_promo=FechaHora.getUltimaFechaMes(FechaHora.getDate(nego_promo.getDE_ANO_FIN_ForJSON(), nego_promo.getDE_PERI_FIN_ForJSON()-1, 1));                
                Date cruce []=FechaHora.getFechaIntersecta(new Date[]{inicio,fin}, new Date[]{ini_promo,fin_promo});
                if (cruce !=null){         
                    if (nego_promo.getST_SERV_SUPL_ForJSON()){ //Verificar si la promocion aplica al servicio suplementario
                        if (nego_promo.getDE_TIPO_ForJSON().equals(1)){//1 porcentaje, 2 Monto
                                Double monto=this.getMonto(cruce[0],cruce[1],servicio_suplementario.getIM_MONTO_ForJSON())*(nego_promo.getIM_VALO_ForJSON()/100d);
                                CIER_DATA_SERV_SUPL deta_ss=new CIER_DATA_SERV_SUPL();
                                deta_ss.setCO_NEGO_SUCU_SERV_SUPL(servicio_suplementario.getCO_NEGO_SUCU_SERV_SUPL_ForJSON());
                                deta_ss.setFE_INIC(cruce[0]);
                                deta_ss.setFE_FIN(cruce[1]);
                                deta_ss.setIM_MONT(monto);      
                                deta_ss.setDE_NOMB(fnc.capitalize(servicio_suplementario.getNO_SERV_SUPL_ForJSON()));
                                deta_ss.setST_AFEC_DETR(servicio_suplementario.getST_AFEC_DETR_ForJSON());
                                deta_ss.setST_TIPO_DEVO(4);
                                deta_ss.setCO_MONE_FACT(servicio_suplementario.getServ_Supl_ForJSON().getCO_MONE_FACT_ForJSON());
                                lista.add(deta_ss);
                        } else {
                                Double monto=this.getMonto(cruce[0],cruce[1],nego_promo.getIM_VALO_ForJSON());
                                CIER_DATA_SERV_SUPL deta_ss=new CIER_DATA_SERV_SUPL();
                                deta_ss.setCO_NEGO_SUCU_SERV_SUPL(servicio_suplementario.getCO_NEGO_SUCU_SERV_SUPL_ForJSON());
                                deta_ss.setFE_INIC(cruce[0]);
                                deta_ss.setFE_FIN(cruce[1]);
                                deta_ss.setIM_MONT(monto);      
                                deta_ss.setDE_NOMB(fnc.capitalize(servicio_suplementario.getNO_SERV_SUPL_ForJSON()));
                                deta_ss.setST_AFEC_DETR(servicio_suplementario.getST_AFEC_DETR_ForJSON());
                                deta_ss.setST_TIPO_DEVO(4);
                                deta_ss.setCO_MONE_FACT(servicio_suplementario.getServ_Supl_ForJSON().getCO_MONE_FACT_ForJSON());
                                lista.add(deta_ss);
                        }
                    }  
                }
            }
        }else if (promociones_pendientes_sucursal.size()>0){  
            for(NEGO_SUCU_PROM nego_sucu_promo : promociones_pendientes_sucursal){
                Date ini_promo=FechaHora.getDate(nego_sucu_promo.getDE_ANO_INIC_ForJSON(), nego_sucu_promo.getDE_PERI_INIC_ForJSON()-1, 1);
                Date fin_promo=FechaHora.getUltimaFechaMes(FechaHora.getDate(nego_sucu_promo.getDE_ANO_FIN_ForJSON(), nego_sucu_promo.getDE_PERI_FIN_ForJSON()-1, 1));                
                Date cruce []=FechaHora.getFechaIntersecta(new Date[]{inicio,fin}, new Date[]{ini_promo,fin_promo});
                if (cruce !=null){
                    if (nego_sucu_promo.getST_SERV_SUPL_ForJSON()){ //Verificar si la promocion aplica al servicio suplementario
                        if (nego_sucu_promo.getDE_TIPO_ForJSON().equals(1)){//1 porcentaje, 2 Monto
                                Double monto=this.getMonto(cruce[0],cruce[1],servicio_suplementario.getIM_MONTO_ForJSON())*(nego_sucu_promo.getIM_VALO_ForJSON()/100d);
                                CIER_DATA_SERV_SUPL deta_ss=new CIER_DATA_SERV_SUPL();
                                deta_ss.setCO_NEGO_SUCU_SERV_SUPL(servicio_suplementario.getCO_NEGO_SUCU_SERV_SUPL_ForJSON());
                                deta_ss.setFE_INIC(cruce[0]);
                                deta_ss.setFE_FIN(cruce[1]);
                                deta_ss.setIM_MONT(monto);      
                                deta_ss.setDE_NOMB(fnc.capitalize(servicio_suplementario.getNO_SERV_SUPL_ForJSON()));
                                deta_ss.setST_AFEC_DETR(servicio_suplementario.getST_AFEC_DETR_ForJSON());
                                deta_ss.setST_TIPO_DEVO(4);
                                deta_ss.setCO_MONE_FACT(servicio_suplementario.getServ_Supl_ForJSON().getCO_MONE_FACT_ForJSON());
                                lista.add(deta_ss);

                        } else {
                                Double monto=this.getMonto(cruce[0],cruce[1],nego_sucu_promo.getIM_VALO_ForJSON());
                                CIER_DATA_SERV_SUPL deta_ss=new CIER_DATA_SERV_SUPL();
                                deta_ss.setCO_NEGO_SUCU_SERV_SUPL(servicio_suplementario.getCO_NEGO_SUCU_SERV_SUPL_ForJSON());
                                deta_ss.setFE_INIC(cruce[0]);
                                deta_ss.setFE_FIN(cruce[1]);
                                deta_ss.setIM_MONT(monto);      
                                deta_ss.setDE_NOMB(fnc.capitalize(servicio_suplementario.getNO_SERV_SUPL_ForJSON()));
                                deta_ss.setST_AFEC_DETR(servicio_suplementario.getST_AFEC_DETR_ForJSON());
                                deta_ss.setST_TIPO_DEVO(4);
                                deta_ss.setCO_MONE_FACT(servicio_suplementario.getServ_Supl_ForJSON().getCO_MONE_FACT_ForJSON());
                                lista.add(deta_ss);
                        }
                    }
                }
            }           
        }
    }     
    protected void facturar(){  
        List<NEGO_SUCU> sucursales= this.nego.getSucursales();
        List<Date[]> periodos=null;
        NEGO_SUCU nego_sucu=null;
        Date nueva_ultima_facturacion=null;
        
        for(int i=0;i<sucursales.size();i++){
            nego_sucu=sucursales.get(i);
            if (!nego_sucu.isMudado_ForJSON()){
                List<SUSP> suspensiones=nego_sucu.getSuspensionesPendientes();        
                List<NEGO_SUCU_SERV_UNIC> sus=nego_sucu.getServiciosUnicosPendientes(this.cier.getCO_CIER_ForJSON());
                List<NEGO_SUCU_PROM> promociones_pendientes_sucursal=nego_sucu.getPromocionesPendientes(this.cier);                    
                List<NEGO_SUCU_PLAN> planes=nego_sucu.getPlanes();
                Boolean existe_aun_algun_plan_activo=false;
                SucursalDetalle sucursal_detalle=new SucursalDetalle(nego_sucu);
                this.negocio_detalle.getDetalle_sucursales().add(sucursal_detalle);
                for(NEGO_SUCU_PLAN plan : planes){
                    existe_aun_algun_plan_activo=(plan.getFE_FIN_ForJSON()==null);
                    if (existe_aun_algun_plan_activo) break;
                }
                //[NUEVO DESARROLLO]
                Boolean consideraPlan;
                Integer contador_planes=0;
                for(NEGO_SUCU_PLAN plan : planes){
                    contador_planes++;
                    consideraPlan=true;
                    if (plan.getFE_FIN_ForJSON() != null && plan.getFE_ULTI_FACT_ForJSON() != null){
                        if (plan.getFE_ULTI_FACT_ForJSON().getTime()==plan.getFE_FIN_ForJSON().getTime()){
                            consideraPlan=false;
                        }
                    }
                    
                    if (consideraPlan){
                        nueva_ultima_facturacion=null;
                        List<CIER_DATA_PLAN> cobrados=plan.getCIER_DATA_PLANES_COBRADOS(this.cier);
                        periodos=this.getPeriodosGeneralCortesSuspensiones(plan.getFE_INIC_ForJSON(),plan.getFE_FIN_ForJSON(),plan.getFE_ULTI_FACT_ForJSON(),this.cortes,suspensiones,contador_planes==1 && !(contador_planes==planes.size()));
                        if ((this.cortes.isEmpty() && suspensiones.isEmpty()) || periodos.isEmpty()){                            
                            if (periodos.isEmpty()){
                               periodos=this.getPeriodos(plan.getFE_INIC_ForJSON(), plan.getFE_FIN_ForJSON(), plan.getFE_ULTI_FACT_ForJSON());
                            }
                            int cont_periodo=0;
                            for(Date[] periodo : periodos){
                                cont_periodo++;
                                if (nueva_ultima_facturacion==null){
                                    nueva_ultima_facturacion=periodo[1];
                                }else if (nueva_ultima_facturacion.getTime()<periodo[1].getTime()){
                                    nueva_ultima_facturacion=periodo[1];
                                }
                                this.evaluarPlanPeriodosYaCobrados(sucursal_detalle.getDetalle_planes(),cobrados,plan,periodo[0],periodo[1],cont_periodo==periodos.size());
                                this.evaluarPromocionPlan(sucursal_detalle.getDetalle_planes(),plan,promociones_pendientes_sucursal,periodo[0],periodo[1]);
                            }
                        } else {
                            //Generar devoluciones por cortes y suspensiones
                            /*
                            Se va tener en cuenta la siguiente regla: 
                            Si la fecha esta antes de la Ultima Fecha de Facturacion entonces se genera una devolucion
                            Si la fecha esta despues de la Ultima Fecha de Facturacion entonces se genera un cobro
                            */
                            List<Date[]> periodos_tmp=null;
                            Date fecha_inicio=plan.getFE_INIC_ForJSON();
                            int count_periodos=0;
                            for(Date[] periodo : periodos){
                                count_periodos++;
                                Boolean realizaCobro=false;
                                if (plan.getFE_ULTI_FACT_ForJSON() == null){
                                    realizaCobro=true;
                                } else if (plan.getFE_ULTI_FACT_ForJSON().getTime()<=periodo[0].getTime()){
                                    realizaCobro=true;
                                }

                                if(realizaCobro){
                                    if (plan.getFE_ULTI_FACT_ForJSON() != null){
                                        if (fecha_inicio.getTime()<plan.getFE_ULTI_FACT_ForJSON().getTime()){
                                            fecha_inicio=FechaHora.addDays(plan.getFE_ULTI_FACT_ForJSON(), 1);
                                        }
                                    }
                                    nueva_ultima_facturacion=null;
                                    periodos_tmp=this.getPeriodos(fecha_inicio,periodo[0], nueva_ultima_facturacion);
                                    int cont_periodo=0;
                                    for(Date[] per : periodos_tmp){
                                        cont_periodo++;
                                        if (nueva_ultima_facturacion==null){
                                            nueva_ultima_facturacion=per[1];
                                        }else if (nueva_ultima_facturacion.getTime()<per[1].getTime()){
                                            nueva_ultima_facturacion=per[1];
                                        }
                                        if (cont_periodo==periodos_tmp.size()){
                                            per[1]=FechaHora.addDays(per[1], -1);
                                        }
                                        this.evaluarPlanPeriodosYaCobrados(sucursal_detalle.getDetalle_planes(),cobrados,plan,per[0],per[1],cont_periodo==periodos_tmp.size());
                                        this.evaluarPromocionPlan(sucursal_detalle.getDetalle_planes(),plan,promociones_pendientes_sucursal,per[0],per[1]);
                                    }
                                    fecha_inicio=periodo[1];
                                    if(periodos.size()==count_periodos){
                                        Date fecha_fin=plan.getFE_FIN_ForJSON();
                                        if (fecha_fin==null){
                                            fecha_fin=this.fecha_real_fin;
                                        }
                                        if (fecha_inicio.getTime()!=fecha_fin.getTime()){
                                            periodos_tmp=this.getPeriodos(fecha_inicio,fecha_fin);
                                        } else {
                                            periodos_tmp.clear();
                                            periodos_tmp.add(new Date[]{fecha_inicio,fecha_fin});
                                        }
                                        
                                        cont_periodo=0;
                                        for(Date[] per : periodos_tmp){
                                            cont_periodo++;
                                            if (nueva_ultima_facturacion==null){
                                                nueva_ultima_facturacion=per[1];
                                            }else if (nueva_ultima_facturacion.getTime()<per[1].getTime()){
                                                nueva_ultima_facturacion=per[1];
                                            }
                                            this.evaluarPlanPeriodosYaCobrados(sucursal_detalle.getDetalle_planes(),cobrados,plan,per[0],per[1],cont_periodo==periodos_tmp.size());
                                            this.evaluarPromocionPlan(sucursal_detalle.getDetalle_planes(),plan,promociones_pendientes_sucursal,per[0],per[1]);
                                        }
                                    }
                                } else {
                                    Date fin_cortes_susp=FechaHora.addYears(this.fecha_real_fin, 8);
                                    if (periodo[1].getTime()!=fin_cortes_susp.getTime()){
                                        //Realiza devolucion
                                        periodos_tmp=this.getPeriodosCortesSuspensiones(plan.getFE_INIC_ForJSON(),periodo[0],periodo[1]);
                                        int van_periodos=0;
                                        for(Date[] per : periodos_tmp){
                                            van_periodos++;
                                            this.evaluarCortesSuspensionesPlanPeriodosYaCobrados(sucursal_detalle.getDetalle_planes(),cobrados,plan,per[0],per[1],van_periodos==periodos_tmp.size(),promociones_pendientes_sucursal,false);
                                        }
                                        fecha_inicio=periodo[1];
                                        //Realiza el cobro despues de las devoluciones. Esto solo se aplica cuando han habido cortes y reconexiones antes de la UFF
                                        if(periodos.size()==count_periodos && planes.size()==contador_planes){
                                            Date inicio=FechaHora.addDays(plan.getFE_ULTI_FACT_ForJSON(), 1);
                                            if (periodo[1].getTime()>plan.getFE_ULTI_FACT_ForJSON().getTime()){
                                                inicio=periodo[1];
                                            }
                                            periodos_tmp=this.getPeriodos(inicio,plan.getFE_FIN_ForJSON(), null);
                                            int cont_periodo=0;
                                            for(Date[] per : periodos_tmp){
                                                cont_periodo++;
                                                if (nueva_ultima_facturacion==null){
                                                    nueva_ultima_facturacion=per[1];
                                                }else if (nueva_ultima_facturacion.getTime()<per[1].getTime()){
                                                    nueva_ultima_facturacion=per[1];
                                                }
                                                this.evaluarPlanPeriodosYaCobrados(sucursal_detalle.getDetalle_planes(),cobrados,plan,per[0],per[1],cont_periodo==periodos_tmp.size());
                                                this.evaluarPromocionPlan(sucursal_detalle.getDetalle_planes(),plan,promociones_pendientes_sucursal,per[0],per[1]);
                                            }
                                        }
                                    } else { 
                                        // Aqui entran los cortes que no tienen fecha fin de corte
                                        //Generar saldo aproximado
                                        Double cal_saldo_aprox=0d;
                                        periodos_tmp=this.getPeriodosCortesSuspensiones(plan.getFE_INIC_ForJSON(),periodo[0],plan.getFE_ULTI_FACT_ForJSON());
                                        int van_periodos=0;
                                        for(Date[] per : periodos_tmp){
                                            van_periodos++;
                                            cal_saldo_aprox+=this.evaluarSaldoAproximadoParaPlanes(cobrados,plan,per[0],per[1],promociones_pendientes_sucursal);
                                        }
                                        cal_saldo_aprox=com.americatel.facturacion.fncs.Numero.redondear(cal_saldo_aprox,2)*-1;
                                        this.saldo_aproximado+=cal_saldo_aprox;
                                        plan.setIM_SALD_APROX(cal_saldo_aprox);
                                        if (!this.preview){
                                                plan.updateSaldoAproximado();
                                        }
                                    }

                                }
                            }

                        }

                        //desactivar antes de la ultima fecha facturacion
                        // Para que no genere mas de un recibo teniendo una ultima fecha de facturacion
                        // nuevo verificar para el caso que se registren dos cambios de tarifa y luego se de de baja
                        if (plan.getFE_ULTI_FACT_ForJSON()!=null && plan.getFE_FIN_ForJSON()!=null && !existe_aun_algun_plan_activo && planes.size()==1){
                            if (plan.getFE_ULTI_FACT_ForJSON().getTime()>plan.getFE_FIN_ForJSON().getTime()){
                                int van_periodos=0;
                                for(Date periodo[] : this.getPeriodos(plan.getFE_FIN_ForJSON(), plan.getFE_ULTI_FACT_ForJSON())){
                                    van_periodos++;
                                    if (nueva_ultima_facturacion==null){
                                        nueva_ultima_facturacion=periodo[1];
                                    }else if (nueva_ultima_facturacion.getTime()<periodo[1].getTime()){
                                        nueva_ultima_facturacion=periodo[1];
                                    }   
                                    //Cuando es una baja se le descuenta un dia
                                    if (van_periodos==1){
                                        periodo[1] = FechaHora.addDays(periodo[1], -1);
                                    }
                                    CIER_DATA_PLAN deta_plan=new CIER_DATA_PLAN();
                                    deta_plan.setCO_NEGO_SUCU_PLAN(plan.getCO_NEGO_SUCU_PLAN_ForJSON());
                                    deta_plan.setFE_INIC(periodo[0]);
                                    deta_plan.setFE_FIN(periodo[1]);
                                    deta_plan.setIM_MONT(this.getMonto(periodo[0],periodo[1],plan.getIM_MONTO_ForJSON()));      
                                    deta_plan.setDE_NOMB(fnc.capitalize(plan.getNO_PLAN_ForJSON()));
                                    deta_plan.setST_TIPO_DEVO(1);
                                    deta_plan.setCO_MONE_FACT(plan.getPlan_ForJSON().getCO_MONE_FACT_ForJSON());
                                    sucursal_detalle.getDetalle_planes().add(deta_plan);
                                }

                            }
                        }
                        if (nueva_ultima_facturacion!=null){
                            if (!this.preview){
                                plan.saveSiguienteUltimaFacturacion(nueva_ultima_facturacion);
                            }
                        }
                    }
                }

                List<NEGO_SUCU_SERV_SUPL> servicios_suplementarios=nego_sucu.getServiciosSuplementarios();
                for(NEGO_SUCU_SERV_SUPL servicio_suplementario : servicios_suplementarios){
                    nueva_ultima_facturacion=null;
                    List<CIER_DATA_SERV_SUPL> cobrados=servicio_suplementario.getCIER_DATA_SERVICIOS_SUPL_COBRADOS(this.cier);
                    periodos=this.getPeriodosGeneralCortesSuspensiones(servicio_suplementario.getFE_INIC_ForJSON(),servicio_suplementario.getFE_FIN_ForJSON(),servicio_suplementario.getFE_ULTI_FACT_ForJSON(),this.cortes,suspensiones, false);
                    if ((this.cortes.isEmpty() && suspensiones.isEmpty()) || periodos.isEmpty()){
                        if (periodos.isEmpty()){
                            periodos=this.getPeriodos(servicio_suplementario.getFE_INIC_ForJSON(), servicio_suplementario.getFE_FIN_ForJSON(), servicio_suplementario.getFE_ULTI_FACT_ForJSON());
                        }
                        int cont_periodo=0;
                        for(Date[] periodo : periodos){
                            cont_periodo++;
                            if (nueva_ultima_facturacion==null){
                                nueva_ultima_facturacion=periodo[1];
                            }else if (nueva_ultima_facturacion.getTime()<periodo[1].getTime()){
                                nueva_ultima_facturacion=periodo[1];
                            }
                            CIER_DATA_SERV_SUPL deta_serv_supl=new CIER_DATA_SERV_SUPL();
                            deta_serv_supl.setCO_NEGO_SUCU_SERV_SUPL(servicio_suplementario.getCO_NEGO_SUCU_SERV_SUPL_ForJSON());
                            deta_serv_supl.setFE_INIC(periodo[0]);
                            deta_serv_supl.setFE_FIN(periodo[1]);
                            deta_serv_supl.setIM_MONT(this.getMonto(periodo[0],periodo[1],servicio_suplementario.getIM_MONTO_ForJSON()));      
                            deta_serv_supl.setDE_NOMB(fnc.capitalize(servicio_suplementario.getNO_SERV_SUPL_ForJSON()));
                            deta_serv_supl.setST_TIPO_COBR(1);
                            deta_serv_supl.setST_AFEC_DETR(servicio_suplementario.getST_AFEC_DETR_ForJSON());
                            deta_serv_supl.setCO_MONE_FACT(servicio_suplementario.getServ_Supl_ForJSON().getCO_MONE_FACT_ForJSON());
                            sucursal_detalle.getDetalle_servicios_suplementarios().add(deta_serv_supl);

                            this.evaluarPromocionServicioSuplementario(sucursal_detalle.getDetalle_servicios_suplementarios(),servicio_suplementario,promociones_pendientes_sucursal,periodo[0],periodo[1]);
                        }
                    } else {
                        //Generar devoluciones por cortes y suspensiones
                        /*
                        Se va tener en cuenta la siguiente regla: 
                        Si la fecha esta antes de la Ultima Fecha de Facturacion entonces se genera una devolucion
                        Si la fecha esta despues de la Ultima Fecha de Facturacion entonces se genera un cobro
                        */
                        List<Date[]> periodos_tmp=null;
                        Date fecha_inicio=servicio_suplementario.getFE_INIC_ForJSON();
                        int count_periodos=0;
                        for(Date[] periodo : periodos){
                            count_periodos++;
                            Boolean realizaCobro=false;
                            if (servicio_suplementario.getFE_ULTI_FACT_ForJSON() == null){
                                realizaCobro=true;
                            } else if (servicio_suplementario.getFE_ULTI_FACT_ForJSON().getTime()<=periodo[0].getTime()){
                                realizaCobro=true;
                            }

                            if(realizaCobro){
                                if (servicio_suplementario.getFE_ULTI_FACT_ForJSON() != null){
                                    if (fecha_inicio.getTime()<servicio_suplementario.getFE_ULTI_FACT_ForJSON().getTime()){
                                        fecha_inicio=FechaHora.addDays(servicio_suplementario.getFE_ULTI_FACT_ForJSON(), 1);
                                    }
                                }
                                nueva_ultima_facturacion=null;
                                periodos_tmp=this.getPeriodos(fecha_inicio,periodo[0], nueva_ultima_facturacion);
                                int cont_periodo=0;
                                for(Date[] per : periodos_tmp){
                                    cont_periodo++;
                                    if (nueva_ultima_facturacion==null){
                                        nueva_ultima_facturacion=per[1];
                                    }else if (nueva_ultima_facturacion.getTime()<per[1].getTime()){
                                        nueva_ultima_facturacion=per[1];
                                    }
                                    if (cont_periodo==periodos_tmp.size()){
                                        per[1]=FechaHora.addDays(per[1], -1);
                                    }
                                    CIER_DATA_SERV_SUPL deta_serv_supl=new CIER_DATA_SERV_SUPL();
                                    deta_serv_supl.setCO_NEGO_SUCU_SERV_SUPL(servicio_suplementario.getCO_NEGO_SUCU_SERV_SUPL_ForJSON());
                                    deta_serv_supl.setFE_INIC(per[0]);
                                    deta_serv_supl.setFE_FIN(per[1]);
                                    deta_serv_supl.setIM_MONT(this.getMonto(per[0],per[1],servicio_suplementario.getIM_MONTO_ForJSON()));      
                                    deta_serv_supl.setDE_NOMB(fnc.capitalize(servicio_suplementario.getNO_SERV_SUPL_ForJSON()));
                                    deta_serv_supl.setST_TIPO_COBR(1);
                                    deta_serv_supl.setST_AFEC_DETR(servicio_suplementario.getST_AFEC_DETR_ForJSON());
                                    deta_serv_supl.setCO_MONE_FACT(servicio_suplementario.getServ_Supl_ForJSON().getCO_MONE_FACT_ForJSON());
                                    sucursal_detalle.getDetalle_servicios_suplementarios().add(deta_serv_supl);

                                    this.evaluarPromocionServicioSuplementario(sucursal_detalle.getDetalle_servicios_suplementarios(),servicio_suplementario,promociones_pendientes_sucursal,per[0],per[1]);
                                }
                                fecha_inicio=periodo[1];
                                if(periodos.size()==count_periodos){
                                    Date fecha_fin=servicio_suplementario.getFE_FIN_ForJSON();
                                    if (fecha_fin==null){
                                        fecha_fin=this.fecha_real_fin;
                                    }
                                    periodos_tmp=this.getPeriodos(fecha_inicio,fecha_fin, null);
                                    cont_periodo=0;
                                    for(Date[] per : periodos_tmp){
                                        cont_periodo++;
                                        if (nueva_ultima_facturacion==null){
                                            nueva_ultima_facturacion=per[1];
                                        }else if (nueva_ultima_facturacion.getTime()<per[1].getTime()){
                                            nueva_ultima_facturacion=per[1];
                                        }
                                        CIER_DATA_SERV_SUPL deta_serv_supl=new CIER_DATA_SERV_SUPL();
                                        deta_serv_supl.setCO_NEGO_SUCU_SERV_SUPL(servicio_suplementario.getCO_NEGO_SUCU_SERV_SUPL_ForJSON());
                                        deta_serv_supl.setFE_INIC(per[0]);
                                        deta_serv_supl.setFE_FIN(per[1]);
                                        deta_serv_supl.setIM_MONT(this.getMonto(per[0],per[1],servicio_suplementario.getIM_MONTO_ForJSON()));      
                                        deta_serv_supl.setDE_NOMB(fnc.capitalize(servicio_suplementario.getNO_SERV_SUPL_ForJSON()));
                                        deta_serv_supl.setST_TIPO_COBR(1);
                                        deta_serv_supl.setST_AFEC_DETR(servicio_suplementario.getST_AFEC_DETR_ForJSON());
                                        deta_serv_supl.setCO_MONE_FACT(servicio_suplementario.getServ_Supl_ForJSON().getCO_MONE_FACT_ForJSON());
                                        sucursal_detalle.getDetalle_servicios_suplementarios().add(deta_serv_supl);

                                        this.evaluarPromocionServicioSuplementario(sucursal_detalle.getDetalle_servicios_suplementarios(),servicio_suplementario,promociones_pendientes_sucursal,per[0],per[1]);
                                    }
                                }
                            } else {
                                Date fin_cortes_susp=FechaHora.addYears(this.fecha_real_fin, 8);
                                if (periodo[1].getTime()!=fin_cortes_susp.getTime()){
                                    //Realiza devolucion
                                    periodos_tmp=this.getPeriodosCortesSuspensiones(servicio_suplementario.getFE_INIC_ForJSON(),periodo[0],periodo[1]);
                                    int van_periodos=0;
                                    for(Date[] per : periodos_tmp){
                                        van_periodos++;
                                        this.evaluarCortesSuspencionesServSuplPeriodosYaCobrados(sucursal_detalle.getDetalle_servicios_suplementarios(),cobrados,servicio_suplementario,per[0],per[1],van_periodos==periodos_tmp.size(),promociones_pendientes_sucursal,false);
                                    }
                                    fecha_inicio=periodo[1];
                                    //Realiza el cobro despues de las devoluciones. Esto solo se aplica cuando han habido cortes y reconexiones antes de la UFF
                                    if(periodos.size()==count_periodos){
                                        Date inicio=FechaHora.addDays(servicio_suplementario.getFE_ULTI_FACT_ForJSON(), 1);
                                        if (periodo[1].getTime()>servicio_suplementario.getFE_ULTI_FACT_ForJSON().getTime()){
                                            inicio=periodo[1];
                                        }
                                        periodos_tmp=this.getPeriodos(inicio,servicio_suplementario.getFE_FIN_ForJSON(), null);
                                        int cont_periodo=0;
                                        for(Date[] per : periodos_tmp){
                                            cont_periodo++;
                                            if (nueva_ultima_facturacion==null){
                                                nueva_ultima_facturacion=per[1];
                                            }else if (nueva_ultima_facturacion.getTime()<per[1].getTime()){
                                                nueva_ultima_facturacion=per[1];
                                            }
                                            CIER_DATA_SERV_SUPL deta_serv_supl=new CIER_DATA_SERV_SUPL();
                                            deta_serv_supl.setCO_NEGO_SUCU_SERV_SUPL(servicio_suplementario.getCO_NEGO_SUCU_SERV_SUPL_ForJSON());
                                            deta_serv_supl.setFE_INIC(per[0]);
                                            deta_serv_supl.setFE_FIN(per[1]);
                                            deta_serv_supl.setIM_MONT(this.getMonto(per[0],per[1],servicio_suplementario.getIM_MONTO_ForJSON()));      
                                            deta_serv_supl.setDE_NOMB(fnc.capitalize(servicio_suplementario.getNO_SERV_SUPL_ForJSON()));
                                            deta_serv_supl.setST_TIPO_COBR(1);
                                            deta_serv_supl.setST_AFEC_DETR(servicio_suplementario.getST_AFEC_DETR_ForJSON());
                                            deta_serv_supl.setCO_MONE_FACT(servicio_suplementario.getServ_Supl_ForJSON().getCO_MONE_FACT_ForJSON());
                                            sucursal_detalle.getDetalle_servicios_suplementarios().add(deta_serv_supl);

                                            this.evaluarPromocionServicioSuplementario(sucursal_detalle.getDetalle_servicios_suplementarios(),servicio_suplementario,promociones_pendientes_sucursal,per[0],per[1]);
                                        }
                                    }
                                } else {
                                    // Aqui entran los cortes que no tienen fecha fin de corte
                                    //Generar saldo aproximado
                                    Double cal_saldo_aprox=0d;
                                    periodos_tmp=this.getPeriodosCortesSuspensiones(servicio_suplementario.getFE_INIC_ForJSON(),periodo[0],servicio_suplementario.getFE_ULTI_FACT_ForJSON());
                                    int van_periodos=0;
                                    for(Date[] per : periodos_tmp){
                                        van_periodos++;
                                        cal_saldo_aprox+=this.evaluarSaldoAproximadoParaServSupl(cobrados,servicio_suplementario,per[0],per[1],promociones_pendientes_sucursal);
                                    }
                                    cal_saldo_aprox=com.americatel.facturacion.fncs.Numero.redondear(cal_saldo_aprox,2)*-1;
                                    this.saldo_aproximado+=cal_saldo_aprox;
                                    servicio_suplementario.setIM_SALD_APROX(cal_saldo_aprox);
                                    if (!this.preview){
                                            servicio_suplementario.updateSaldoAproximado();
                                    }
                                }
                            }
                        }
                    }

                    //desactivar antes de la ultima fecha desactivacion
                    if (servicio_suplementario.getFE_ULTI_FACT_ForJSON()!=null && servicio_suplementario.getFE_FIN_ForJSON()!=null){
                        if (servicio_suplementario.getFE_ULTI_FACT_ForJSON().getTime()>servicio_suplementario.getFE_FIN_ForJSON().getTime()){
                            int van_periodos=0;
                            for(Date periodo[] : this.getPeriodos(servicio_suplementario.getFE_FIN_ForJSON(), servicio_suplementario.getFE_ULTI_FACT_ForJSON())){
                                van_periodos++;
                                if (nueva_ultima_facturacion==null){
                                    nueva_ultima_facturacion=periodo[1];
                                }else if (nueva_ultima_facturacion.getTime()<periodo[1].getTime()){
                                    nueva_ultima_facturacion=periodo[1];
                                }
                                //En bajas se le resta un dia
                                if (van_periodos==1){
                                    periodo[1] = FechaHora.addDays(periodo[1], -1);
                                }
                                CIER_DATA_SERV_SUPL deta_serv_supl=new CIER_DATA_SERV_SUPL();
                                deta_serv_supl.setCO_NEGO_SUCU_SERV_SUPL(servicio_suplementario.getCO_NEGO_SUCU_SERV_SUPL_ForJSON());
                                deta_serv_supl.setFE_INIC(periodo[0]);
                                deta_serv_supl.setFE_FIN(periodo[1]);
                                deta_serv_supl.setIM_MONT(this.getMonto(periodo[0],periodo[1],servicio_suplementario.getIM_MONTO_ForJSON()));      
                                deta_serv_supl.setDE_NOMB(fnc.capitalize(servicio_suplementario.getNO_SERV_SUPL_ForJSON()));
                                deta_serv_supl.setST_TIPO_DEVO(1);
                                deta_serv_supl.setST_AFEC_DETR(servicio_suplementario.getST_AFEC_DETR_ForJSON());
                                deta_serv_supl.setCO_MONE_FACT(servicio_suplementario.getServ_Supl_ForJSON().getCO_MONE_FACT_ForJSON());
                                sucursal_detalle.getDetalle_servicios_suplementarios().add(deta_serv_supl);
                            }

                        }
                    }

                    if (nueva_ultima_facturacion!=null){
                        if (!this.preview){
                            servicio_suplementario.saveSiguienteUltimaFacturacion(nueva_ultima_facturacion);
                        }
                    }

                }
                //promociones de montos a nivel de sucursal
                /*
                Double total_promocion_sucursal=0d;
                Date ini_promo=null;
                Date fin_promo=null;
                if (this.promociones_pendientes_negocio.isEmpty()){
                    for(NEGO_SUCU_PROM nego_sucu_prom : promociones_pendientes_sucursal){
                        if (nego_sucu_prom.getDE_TIPO_ForJSON().equals(2)){//1 porcentaje,2monto
                            ini_promo=FechaHora.getDate(nego_sucu_prom.getDE_ANO_INIC_ForJSON(), nego_sucu_prom.getDE_PERI_INIC_ForJSON()-1, 1);
                            fin_promo=FechaHora.getUltimaFechaMes(FechaHora.getDate(nego_sucu_prom.getDE_ANO_FIN_ForJSON(), nego_sucu_prom.getDE_PERI_FIN_ForJSON()-1, 1));                
                            total_promocion_sucursal+=nego_sucu_prom.getIM_VALO_ForJSON();                        
                        }
                    }
                }

                if (total_promocion_sucursal>0){
                    CIER_DATA_PROM_MONT prom=new CIER_DATA_PROM_MONT();
                    prom.setCO_CIER(this.cier.getCO_CIER_ForJSON());
                    prom.setDE_NOMB(fnc.capitalize(GLOSA_PROMOCION));
                    prom.setIM_MONT(total_promocion_sucursal);
                    prom.setFE_INIC(ini_promo);
                    prom.setFE_FIN(fin_promo);
                    sucursal_detalle.getDetalle_promociones_monto().add(prom);
                }
                */
                for(NEGO_SUCU_SERV_UNIC su : sus){
                    CIER_DATA_SERV_UNIC unic=new CIER_DATA_SERV_UNIC();
                    unic.setCO_NEGO_SUCU_SERV_UNIC(su.getCO_NEGO_SUCU_SERV_UNIC_ForJSON());
                    unic.setCO_CIER_COBR(this.cier.getCO_CIER_ForJSON());
                    unic.setDE_NOMB(fnc.capitalize(su.getNO_SERV_UNIC_ForJSON()));
                    unic.setIM_MONT(su.getIM_MONTO_ForJSON());
                    unic.setST_AFEC_DETR(su.getST_AFEC_DETR_ForJSON());
                    sucursal_detalle.getDetalle_servicios_unicos().add(unic);
                }
            }
        }
        //promociones de montos a nivel de negocio
        /*
        Double total_promocion_sucursal=0d;
        Date ini_promo=null;
        Date fin_promo=null;
        for(NEGO_PROM nego_prom : this.promociones_pendientes_negocio){
            if (nego_prom.getDE_TIPO_ForJSON().equals(2)){//1 porcentaje,2monto
                ini_promo=FechaHora.getDate(nego_prom.getDE_ANO_INIC_ForJSON(), nego_prom.getDE_PERI_INIC_ForJSON()-1, 1);
                fin_promo=FechaHora.getUltimaFechaMes(FechaHora.getDate(nego_prom.getDE_ANO_FIN_ForJSON(), nego_prom.getDE_PERI_FIN_ForJSON()-1, 1));                
                total_promocion_sucursal+=nego_prom.getIM_VALO_ForJSON();                        
            }
        }
        if (total_promocion_sucursal>0){
            CIER_DATA_PROM_MONT prom=new CIER_DATA_PROM_MONT();
            prom.setCO_CIER(this.cier.getCO_CIER_ForJSON());
            prom.setDE_NOMB(fnc.capitalize(GLOSA_PROMOCION));
            prom.setIM_MONT(total_promocion_sucursal);
            prom.setFE_INIC(ini_promo);
            prom.setFE_FIN(fin_promo);
            this.negocio_detalle.getDetalle_promociones_monto().add(prom);
        }  
        */
    }

    public CIER getCier() {
        return cier;
    }

    public PROD getProd() {
        return prod;
    }

    public NEGO getNego() {
        return nego;
    }

    public List<CORT> getCortes() {
        return cortes;
    }

    public CLIE getClie() {
        return clie;
    }

    public Date getFecha_real_inicio() {
        return fecha_real_inicio;
    }

    public Date getFecha_real_fin() {
        return fecha_real_fin;
    }

    public NegocioDetalle getNegocio_detalle() {
        return negocio_detalle;
    }


    
    public SUCU getSucu_fiscal() {
        return sucu_fiscal;
    }

    public SUCU getSucu_corr() {
        return sucu_corr;
    }

    public DIST getDist_fiscal() {
        return dist_fiscal;
    }

    public DIST getDist_corr() {
        return dist_corr;
    }

    public PROV getProv_fiscal() {
        return prov_fiscal;
    }

    public PROV getProv_corr() {
        return prov_corr;
    }

    public DEPA getDepa_fiscal() {
        return depa_fiscal;
    }

    public DEPA getDepa_corr() {
        return depa_corr;
    }

    public MONE_FACT getMone_fact() {
        return mone_fact;
    }


    

    
    
}
