/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.cores.modelo;

import com.americatel.facturacion.cores.Logs;
import com.americatel.facturacion.cores.ObjetoFacturar;
import com.americatel.facturacion.fncs.FechaHora;
import com.americatel.facturacion.fncs.Numero;
import com.americatel.facturacion.models.CIER;
import com.americatel.facturacion.models.CLIE;
import com.americatel.facturacion.models.CORT;
import com.americatel.facturacion.models.NEGO;
import com.americatel.facturacion.models.NEGO_PROM;
import com.americatel.facturacion.models.NEGO_SUCU;
import com.americatel.facturacion.models.NEGO_SUCU_PLAN;
import com.americatel.facturacion.models.NEGO_SUCU_SERV_SUPL;
import com.americatel.facturacion.models.SUCU;
import com.americatel.facturacion.models.SUSP;
import java.util.Date;
import java.util.List;

/**
 *
 * @author crodas
 */
public abstract class ModeloFacturacion {
    ObjetoFacturar objeto=null;
    
    protected Date CierreRealInicio=null;
    protected Date CierreRealFin=null;
    protected Date PeriodoInicio=null;
    protected Date PeriodoFin=null;
    protected Integer MesesPeriodo=null;
    protected Date FechaUltimaFacturacion=null;
    protected Date FechaActivacion=null;
    protected Date FechaDesactivacion=null;
    protected List<Date[]> FechasCierresFactura=null;// no considera cortes para su calculo, solo FA y FD
    protected String Nombre;
    protected Double Monto; 
    protected Boolean Preview;
    
    protected NEGO nego=null;
    protected CIER cier=null;
    protected CLIE clie=null; 
    protected NEGO_SUCU nego_sucu=null;
    protected SUCU sucu_corr=null;
    protected NEGO_SUCU_SERV_SUPL serv_supl=null;
    protected NEGO_SUCU_PLAN plan=null;
    protected List<CORT> cortes=null;
    protected List<SUSP> suspensiones=null;

    public ModeloFacturacion(ObjetoFacturar objeto) {
        this.objeto=objeto;
        this.nego=objeto.getNego();
        this.cier=objeto.getCier();
        this.clie=objeto.getClie(); 
        this.nego_sucu=objeto.getNego_Sucu();
        this.sucu_corr=objeto.getSucu_Corr();
        this.serv_supl=objeto.getServ_Supl();
        this.plan=objeto.getPlan();
        this.cortes=objeto.getCortes();
        this.suspensiones=objeto.getSuspensiones();
    }
    
    protected void mostrarLog(){
        Logs.printMsg(
                "NO: "+ this.Nombre+", MO: "+this.Monto+
                ",CRI: "+ FechaHora.getStringDate(CierreRealInicio)+", CRF: "+FechaHora.getStringDate(CierreRealFin)+
                ", PI: "+ FechaHora.getStringDate(PeriodoInicio)+", PF: "+ FechaHora.getStringDate(PeriodoFin)+
                ", FA: "+FechaHora.getStringDate(FechaActivacion)+", FD: "+FechaHora.getStringDate(FechaDesactivacion)+
                ", UF: "+FechaHora.getStringDate(FechaUltimaFacturacion)+
                ", FACTURA : "+(this.isFacturable()?"Si":"No"),3,!this.Preview,this.cier,this.nego,this.nego_sucu);
    }
    
    protected Boolean isDesactivadoAntesUltimaFacturacion(){
        if (this.FechaDesactivacion!=null && this.FechaUltimaFacturacion!=null)
            return this.FechaUltimaFacturacion.getTime()>this.FechaDesactivacion.getTime();
        return false;        
    }
    
    public abstract void facturar(Boolean preview);    
    protected abstract void facturar(Date inicio,Date fin);
    protected abstract void calcularConfiguraciones();    
    protected abstract Boolean isFacturable();    
    protected Boolean isFacturable(Date inicio,Date fin) {
        return true;
    }    
    protected abstract Boolean estaCortado();
    protected abstract Boolean estaSuspendido();    
    protected abstract void guardarSiguienteUltimaFacturacion();
    
    /*
    protected abstract Double getMontoDevolucionPorCortesServicioFueraPeriodo();
    protected abstract Double getMontoDevolucionPorCortesServicioDentroPeriodo(Date inicio,Date fin);
    protected abstract Double getMontoDevolucionPorSuspensionesServicioFueraPeriodo();
    protected abstract Double getMontoDevolucionPorSuspensionesServicioDentroPeriodo(Date inicio,Date fin);
    */
    

    protected void calcularCobroAdicionalPorUpGrade(){}
    protected void calcularCobroAdicionalPorDownGrade(){}
    
    protected Double getMontoDevolucionPorCortesServicioFueraPeriodo(){
        Date tmp_inicio=FechaHora.getDate(1980, 0, 1);
        Date tmp_fin=this.PeriodoInicio;
        Date Fb[]=new Date[]{tmp_inicio,tmp_fin};
        Double ret=0d;
        for(CORT cort : this.cortes){
            Date lista[]=FechaHora.getFechaIntersecta( new Date[]{cort.getFE_INIC_ForJSON(),cort.getFE_FIN_ForJSON() == null ? FechaHora.getDate(2050, 12, 31) : cort.getFE_FIN_ForJSON()} , Fb);
            if (lista!=null){
                /*
                Regla por dia mas o menus
                1)Fecha desactivacion aun tiene servicio
                2) Cortes y Suspensiones
                    Fecha inicio No tiene servicio
                    Fecha fin Si tiene servicio
                */
                //Regla 3)Solo devuelve si la diferencia entre fecha inicio y fecha fin es menor a un mes
                //si no, no realiza nada motivo es que ya se devolvio el monto en otro cierre y debe estar como saldo
                if(FechaHora.getDiffMonths(lista[0], lista[1])<=1){              
                    if (cort.getFE_FIN_ForJSON()==null)
                        ret+=this.getMonto(lista[0], lista[1]);
                    else
                        ret+=this.getMonto(lista[0], lista[1],false);
                }
            }            
        }
        return ret;
    }
    
    protected Double getMontoDevolucionPorCortesServicioDentroPeriodo(Date inicio,Date fin){
        Double ret=0d;
        Date Fb[]=new Date[]{inicio,fin};
        for(CORT cort : this.cortes){
            Date lista[]=FechaHora.getFechaIntersecta( new Date[]{cort.getFE_INIC_ForJSON(),cort.getFE_FIN_ForJSON()} , Fb);
            if (lista!=null){
                /*
                Regla por dia mas o menus
                1)Fecha desactivacion aun tiene servicio
                2) Cortes y Suspensiones
                    Fecha inicio No tiene servicio
                    Fecha fin Si tiene servicio
                */
                if (cort.getFE_FIN_ForJSON()==null)
                    ret+=this.getMonto(lista[0], lista[1]);
                else
                    ret+=this.getMonto(lista[0], lista[1],false);
            }
        }        
        return ret;
    }   
    
    protected Double getMontoDevolucionPorSuspensionesServicioFueraPeriodo(){
        Date tmp_inicio=FechaHora.getDate(1980, 0, 1);
        Date tmp_fin=FechaHora.addDays(this.PeriodoInicio,-1);
        Date Fb[]=new Date[]{tmp_inicio,tmp_fin};
        Double ret=0d;
        for(SUSP susp : this.suspensiones){
            Date susp_inicio=susp.getFE_INIC_ForJSON();
            Date susp_fin=susp.getFE_FIN_ForJSON();
            if (susp_fin==null) susp_fin=FechaHora.getDate(2050, 11, 31);
            Date lista[]=FechaHora.getFechaIntersecta( new Date[]{susp_inicio,susp_fin} , Fb);
            if (lista!=null){
                /*
                Regla por dia mas o menos
                1)Fecha desactivacion aun tiene servicio
                2) Cortes y Suspensiones
                    Fecha inicio No tiene servicio
                    Fecha fin Si tiene servicio
                */
                //Regla 3)Solo devuelve si la diferencia entre fecha inicio y fecha fin es menor a un mes
                //si no, no realiza nada motivo es que ya se devolvio el monto en otro cierre y debe estar como saldo
                if(FechaHora.getDiffMonths(lista[0], lista[1])<=1){
                    if (susp.getFE_FIN_ForJSON()==null)
                        ret+=this.getMonto(lista[0], lista[1]);
                    else
                        ret+=this.getMonto(lista[0], lista[1],false);
                }
            }            
        }
        return ret;
    }
    
    protected Double getMontoDevolucionPorSuspensionesServicioDentroPeriodo(Date inicio,Date fin){
        Double ret=0d;
        Date Fb[]=new Date[]{inicio,fin};
        for(SUSP susp : this.suspensiones){
            Date lista[]=FechaHora.getFechaIntersecta( new Date[]{susp.getFE_INIC_ForJSON(),susp.getFE_FIN_ForJSON()} , Fb);
            if (lista!=null){
                /*
                Regla por dia mas o menus
                1)Fecha desactivacion aun tiene servicio
                2) Cortes y Suspensiones
                    Fecha inicio No tiene servicio
                    Fecha fin Si tiene servicio
                */
                if (susp.getFE_FIN_ForJSON()==null)
                    ret+=this.getMonto(lista[0], lista[1]);
                else
                    ret+=this.getMonto(lista[0], lista[1],false);
            }
        }        
        return ret;
    }    
    
    
    
    
    protected Double getMontoReal(Date inicio,Date fin){
        Double ret=this.getMonto(inicio, fin);
        ret-=this.getMontoDevolucionPorSinServicioDentroPeriodo(inicio, fin);
        return ret;        
    }
    
    protected Double getMonto(Date inicio,Date fin){
        return this.getMonto(inicio, fin, null);
    }
    
    protected Double getMonto(Date inicio,Date fin,Boolean add_dia){        
        return this.getMonto(this.Monto, inicio, fin, add_dia);
    }
    protected Double getMonto(Double monto,Date inicio,Date fin){ 
        return this.getMonto(monto, inicio, fin, null);
    }
    protected Double getMonto(Double monto,Date inicio,Date fin,Boolean add_dia){        
        if (FechaHora.esMismoAnioPeriodo(inicio, fin)){
            Integer dm=FechaHora.getNumDaysOfMonth(inicio);           
            int dias_add=0;
            if (add_dia!=null){
                if (add_dia)
                    dias_add++;
                else
                    dias_add--;
            }
            Double ret=Numero.redondear((monto/(double)dm)*(double)(FechaHora.getDiffDays(inicio, fin)+dias_add),2);
            return ret;
        }else{
            Double retornar=0D;
            Date tmpInicio=(Date)inicio.clone();
            Date tmpFin=FechaHora.getUltimaFechaMes(tmpInicio);
            retornar+=this.getMonto(monto,tmpInicio, tmpFin);
            
            tmpInicio=FechaHora.addDays(tmpFin, 1);
            tmpFin=FechaHora.getUltimaFechaMes(tmpInicio);
            
            while(tmpFin.getTime()<fin.getTime()){                
                retornar+=this.getMonto(monto,tmpInicio, tmpFin);
                tmpInicio=FechaHora.addDays(tmpFin, 1);
                tmpFin=FechaHora.getUltimaFechaMes(tmpInicio);                
            }
            retornar+=this.getMonto(monto,tmpInicio, fin,add_dia);
            return retornar;
        }        
    }
    
    
    protected Double getMontoDevolucionPorSinServicioFueraPeriodo(){
        return this.getMontoDevolucionPorSuspensionesServicioFueraPeriodo()+this.getMontoDevolucionPorCortesServicioFueraPeriodo();
    }
            
    protected Double getMontoDevolucionPorSinServicioDentroPeriodo(Date inicio,Date fin){
        return this.getMontoDevolucionPorSuspensionesServicioDentroPeriodo(inicio, fin)+this.getMontoDevolucionPorCortesServicioDentroPeriodo(inicio, fin);
    }   
    
    
}

