/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.cores.modelo;

import com.americatel.facturacion.cores.EMotivoNoFactura;
import com.americatel.facturacion.cores.ETipoReciboGlosa;
import com.americatel.facturacion.cores.Logs;
import com.americatel.facturacion.cores.ObjetoFacturar;
import com.americatel.facturacion.cores.Recibo;
import com.americatel.facturacion.cores.ReciboGlosa;
import com.americatel.facturacion.fncs.FechaHora;
import com.americatel.facturacion.fncs.Numero;
import com.americatel.facturacion.models.CIER;
import com.americatel.facturacion.models.CORT;
import com.americatel.facturacion.models.NEGO;
import com.americatel.facturacion.models.NEGO_SUCU_PLAN;
import com.americatel.facturacion.models.NEGO_SUCU_SERV_SUPL;
import com.americatel.facturacion.models.SUSP;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author crodas
 */
public class MensualAdelantado extends ModeloFacturacion {    
    public MensualAdelantado(ObjetoFacturar objeto) {
        super(objeto);
    } 
    
    @Override
    protected Boolean estaCortado() {
        Boolean ret=this.objeto.isCortado();
        //regla adicional, si el inicio de cierre real no debe estar cortado
        //regla de leslie 22/07/2015        
        if (!ret){
            if (this.CierreRealInicio!=null){
                for (CORT corte : cortes){ 
                    if (corte.getFE_FIN_ForJSON()!=null){
                        Long is=this.CierreRealInicio.getTime();
                        if (corte.getFE_INIC_ForJSON().getTime()<=is && corte.getFE_FIN_ForJSON().getTime()>is){
                            ret=true;
                            break;
                        }
                    }                
                } 
            }
        }
        return ret;
    }

    @Override
    protected Boolean estaSuspendido() {
        return this.objeto.isSuspendido();
    }
    
    @Override
    protected Boolean isFacturable() {
        //Regla 1) Cuando se factura un negocio tipo adelantada y recien se activa
        //esta facturara si la fecha de activacion es menor a cierre real inicio.
        //en otro caso no factura        
        if (this.FechaActivacion.getTime()>this.CierreRealInicio.getTime()){ 
            this.objeto.addMotivosNoFactura(EMotivoNoFactura.RECIEN_ACTIVADA);
            return false;
        }
        //Regla 2)No factura si su ultima facturacion es diferente de NULL y Fecha desactivacion tambien
        //es diferente de NULL y la ultima facturacion es menor o igual  a fecha desactivacion        
        if (
                this.FechaUltimaFacturacion!=null && this.FechaDesactivacion!=null &&
                this.FechaUltimaFacturacion.getTime()>=this.FechaDesactivacion.getTime()
            ){
            this.objeto.addMotivosNoFactura(EMotivoNoFactura.DESACTIVADA);
            return false;
        }        
        //Regla 3)Debe verificar que no este en corte ni suspension
        if (this.estaSuspendido()){
            this.objeto.addMotivosNoFactura(EMotivoNoFactura.SUSPENDIDO);
            return false;
        }else if (this.estaCortado()){
            this.objeto.addMotivosNoFactura(EMotivoNoFactura.CORTADO);
            return false;
        }
            
            
               
        return true;
    }
    
    @Override
    protected Boolean isFacturable(Date inicio,Date fin) {
        // verifica si es facturable por cada periodo
        
        
        //Regla) Cuando se realice  un upgrade no se debe cobrar el periodo ya facturado en un recibo anterior para un plan.
        //(No se cobra en enero si no en febrero solo para mensual)        
        //No factura si es un upgrade, para el caso de planes
        if (this.plan!=null){
            if (this.plan.isUpgradeAntiguo(inicio,fin)){
                return false;
            }
        }
        
        //Regla) Cuando se realice  un downgrade no se debe cobrar el periodo ya facturado en un recibo anterior para un plan.
        //(No se cobra en enero si no en febrero solo para mensual)        
        //No factura si es un downgrade, para el caso de planes
        if (this.plan!=null){
            if (this.plan.isDowngradeAntiguo(inicio,fin)){
                return false;
            }
        }        
        return true;
    }    
    
    @Override
    protected void calcularConfiguraciones() {        
        this.Nombre=objeto.getNombre();
        this.Monto=objeto.getMonto();
        this.FechaActivacion=objeto.getFechaActivacion();
        this.FechaDesactivacion=objeto.getFechaDesactivacion();
        this.FechaUltimaFacturacion=objeto.getUltimaFacturacion();
        this.MesesPeriodo=1; 
        this.CierreRealInicio=FechaHora.getDate(this.cier.getNU_ANO_ForJSON(), this.cier.getNU_PERI_ForJSON()-1,1);
        //verificamos que tien de facturacion y adelantamos un mes si es adelantada
        //CierreRealInicio=FechaHora.addMonths(CierreRealInicio, 1);        
        this.CierreRealFin=FechaHora.addDays(FechaHora.addMonths(this.CierreRealInicio,this.MesesPeriodo),-1);        
        //calculamos el inicio del periodo
        //comparamos con el CRI y fecha de activacion        
        if (this.FechaUltimaFacturacion==null){
            this.PeriodoInicio=this.FechaActivacion;
        }else{
            this.PeriodoInicio=FechaHora.addDays(this.FechaUltimaFacturacion,1);//Le sumamos un dia, ya que en esta fecha si se le cobro
        }  
        this.PeriodoFin=this.CierreRealFin;
        if (this.FechaDesactivacion!=null){
            if (FechaHora.getDiffMonths(this.PeriodoInicio, this.FechaDesactivacion)<=0)
                this.PeriodoFin=this.FechaDesactivacion;
        }        
        //Calculamos las fechas de cierre        
        this.FechasCierresFactura=new ArrayList<Date[]>();
        Date tmpPI=PeriodoInicio;
        Date tmpCRF=PeriodoFin;
        while( tmpPI.getTime()<tmpCRF.getTime() ){
            Date ultima=FechaHora.getUltimaFechaMes(tmpPI);
            
            if (ultima.getTime()>tmpCRF.getTime())
                ultima=tmpCRF;
            
            this.FechasCierresFactura.add(new Date[]{tmpPI,ultima});            
            if (!FechaHora.getDay(tmpPI).equals(1)){
                tmpPI=FechaHora.getPrimeraFechaMes(FechaHora.addMonths(tmpPI, 1));                
            }else{
                tmpPI=FechaHora.addMonths(tmpPI, this.MesesPeriodo);
            }
        }
        this.mostrarLog();
    }    
    
    
    @Override
    protected void guardarSiguienteUltimaFacturacion() {
        if (this.serv_supl!=null){
            this.serv_supl.saveSiguienteUltimaFacturacion(this.PeriodoFin);
        } else if (this.plan!=null){
            this.plan.saveSiguienteUltimaFacturacion(this.PeriodoFin);
        }
    }      

    protected Double getMontoDevolucionPorDesactivacionAntesUltimaFacturacion(){
        //Es devolucion si es que esta desactivado y tiene ultima fecha de desactivacion
        //Y la fecha ultima facturacion es mayor a la de desactivacion
        //Si es devolucion retorna el monto en otro caso retorna null
        if (this.isDesactivadoAntesUltimaFacturacion()){
            return this.getMonto(FechaHora.addDays(this.FechaDesactivacion,1), this.FechaUltimaFacturacion);            
        } 
        return null;
    }

    
    @Override
    protected void facturar(Date inicio, Date fin) {
        if (this.isFacturable(inicio,fin)){
            Logs.printMsg("Facturar -> CI: "+ FechaHora.getStringDate(inicio)+", CF: "+FechaHora.getStringDate(fin),4,!this.Preview,this.cier,this.nego,this.nego_sucu);
            List <Date []> dias_sin_servicio=new ArrayList<Date[]>();
            Recibo rec=new Recibo(inicio,fin,this.cier,this.clie,this.nego,this.sucu_corr,dias_sin_servicio);        
            ReciboGlosa rg;
            if (this.plan!=null)
                rg=new ReciboGlosa(this.Nombre,this.getMontoReal(inicio,fin),ETipoReciboGlosa.PLAN, this.plan);
            else
                rg=new ReciboGlosa(this.Nombre,this.getMontoReal(inicio,fin),ETipoReciboGlosa.SERVICIO_SUPLEMENTARIO, this.serv_supl);
            rec.addGlosa(this.nego_sucu, rg);
            this.objeto.addRecibo(rec);
        }
    }
    
    @Override
    protected void calcularCobroAdicionalPorUpGrade(){
        //Caso upgrade la glosa en el periodo que el corresponde
        if (this.plan!=null){
            NEGO_SUCU_PLAN tmp_plan=null;
            Date tmp_fechas[]=null;
            int van=0;
            Double valor_cobro_adicional_upgrade=0d;
            for(Date [] fechaCierreFactura: this.FechasCierresFactura){ 
                tmp_plan=this.plan.getUpgradeAntiguo(fechaCierreFactura[0], fechaCierreFactura[1]);
                if (tmp_plan!=null){
                    if (this.FechasCierresFactura.size()>(van+1)){
                        tmp_fechas=this.FechasCierresFactura.get(van+1);

                        valor_cobro_adicional_upgrade=this.getMonto(fechaCierreFactura[0], fechaCierreFactura[1])-this.getMonto(tmp_plan.getIM_MONTO_ForJSON(),fechaCierreFactura[0], fechaCierreFactura[1]);


                        Recibo rec=new Recibo(tmp_fechas[0], tmp_fechas[1],this.cier,this.clie,this.nego,this.sucu_corr);                              
                        rec.addGlosa(this.nego_sucu, new ReciboGlosa("COBRO ADICIONAL "+this.plan.getNO_PLAN_ForJSON(), valor_cobro_adicional_upgrade , ETipoReciboGlosa.COBRO_ADICIONAL_UPGRADE, this.plan));
                        this.objeto.addRecibo(rec);
                        break;
                    }
                }
                van++;
            }
        }        
    }
    
    @Override
    protected void calcularCobroAdicionalPorDownGrade(){
        //Caso downgrade la glosa en el periodo que el corresponde
        if (this.plan!=null){
            NEGO_SUCU_PLAN tmp_plan=null;
            Date tmp_fechas[]=null;
            int van=0;
            Double valor_cobro_adicional_downgrade=0d;
            for(Date [] fechaCierreFactura: this.FechasCierresFactura){ 
                tmp_plan=this.plan.getDowngradeAntiguo(fechaCierreFactura[0], fechaCierreFactura[1]);
                if (tmp_plan!=null){
                    if (this.FechasCierresFactura.size()>(van+1)){
                        tmp_fechas=this.FechasCierresFactura.get(van+1);

                        valor_cobro_adicional_downgrade=this.getMonto(fechaCierreFactura[0], fechaCierreFactura[1])-this.getMonto(tmp_plan.getIM_MONTO_ForJSON(),fechaCierreFactura[0], fechaCierreFactura[1]);


                        Recibo rec=new Recibo(tmp_fechas[0], tmp_fechas[1],this.cier,this.clie,this.nego,this.sucu_corr);                              
                        rec.addGlosa(this.nego_sucu, new ReciboGlosa("DEVOLUCI\u00d3N POR CARGOS FIJOS "+tmp_plan.getNO_PLAN_ForJSON(), valor_cobro_adicional_downgrade , ETipoReciboGlosa.DEVOLUCION_CARGOS_FIJOS_SALDO, tmp_plan));
                        this.objeto.addRecibo(rec);
                        break;
                    }
                }
                van++;
            }
        }        
    }
    
    @Override
    public void facturar(Boolean preview) {
        this.Preview=preview;
        this.calcularConfiguraciones();        
        if (this.isFacturable()){
            for(Date [] fechaCierreFactura: this.FechasCierresFactura){                    
                this.facturar(fechaCierreFactura[0],fechaCierreFactura[1]);
            }   
            if (!preview){//si no es preview
                this.guardarSiguienteUltimaFacturacion();
            }  
            //this.calcularCobroAdicionalPorUpGrade();  
            //this.calcularCobroAdicionalPorDownGrade();
        }else{
            if (this.isDesactivadoAntesUltimaFacturacion()){                 
                Boolean es_caso_upgrade=this.plan!=null && this.plan.isUpgradeNuevo();                
                Boolean es_caso_downgrade=this.plan!=null && this.plan.isDowngradeNuevo(); 
                /*
                if (!es_caso_upgrade && !es_caso_downgrade){                
                    Double monto_devolucion_por_desactivacion=this.getMontoDevolucionPorDesactivacionAntesUltimaFacturacion();//NEGATIVO
                    if (monto_devolucion_por_desactivacion!=null && monto_devolucion_por_desactivacion>0){
                        monto_devolucion_por_desactivacion*=-1d;
                        Recibo rec=new Recibo(this.FechaDesactivacion, this.FechaUltimaFacturacion,this.cier,this.clie,this.nego,this.sucu_corr);  
                        if (this.plan!=null)
                            rec.addGlosa(this.nego_sucu, new ReciboGlosa(this.Nombre, monto_devolucion_por_desactivacion , ETipoReciboGlosa.DEVOLUCION_POR_DESACTIVACION, plan));
                        else
                            rec.addGlosa(this.nego_sucu, new ReciboGlosa(this.Nombre, monto_devolucion_por_desactivacion , ETipoReciboGlosa.DEVOLUCION_POR_DESACTIVACION, serv_supl));
                        this.objeto.addRecibo(rec);
                    }
                }else{
                    if (es_caso_downgrade){*/
                        Double monto_devolucion_por_desactivacion=this.getMontoDevolucionPorDesactivacionAntesUltimaFacturacion();//NEGATIVO
                        monto_devolucion_por_desactivacion*=-1d;
                        Recibo rec=new Recibo(this.FechaDesactivacion, this.FechaUltimaFacturacion,this.cier,this.clie,this.nego,this.sucu_corr);
                        if (this.plan!=null)
                            rec.addGlosa(this.nego_sucu, new ReciboGlosa(this.Nombre, monto_devolucion_por_desactivacion , ETipoReciboGlosa.DEVOLUCION_POR_DESACTIVACION, plan));
                        else
                            rec.addGlosa(this.nego_sucu, new ReciboGlosa(this.Nombre, monto_devolucion_por_desactivacion , ETipoReciboGlosa.DEVOLUCION_POR_DESACTIVACION, serv_supl));
                        this.objeto.addRecibo(rec); /*                       
                    }
                }
                if (!preview){//si no es preview
                    this.guardarSiguienteUltimaFacturacion();
                } */
            }
        }
        
        Double monto_devolucion_sin_servicio_fuerda_periodo=this.getMontoDevolucionPorSinServicioFueraPeriodo();
        if (monto_devolucion_sin_servicio_fuerda_periodo!=null && monto_devolucion_sin_servicio_fuerda_periodo>0){
            monto_devolucion_sin_servicio_fuerda_periodo*=-1d;           
            Recibo rec=new Recibo(this.PeriodoInicio, this.PeriodoFin,this.cier,this.clie,this.nego,this.sucu_corr);                
            if (this.plan!=null)
                rec.addGlosa(this.nego_sucu, new ReciboGlosa("DEVOLUCI\u00d3N DE CARGO FIJO", monto_devolucion_sin_servicio_fuerda_periodo , ETipoReciboGlosa.DEVOLUCION_POR_CORTE_O_SUSPENSION_SERVICIO, plan));
            else
                rec.addGlosa(this.nego_sucu, new ReciboGlosa("DEVOLUCI\u00d3N DE CARGO FIJO", monto_devolucion_sin_servicio_fuerda_periodo , ETipoReciboGlosa.DEVOLUCION_POR_CORTE_O_SUSPENSION_SERVICIO, serv_supl));
            this.objeto.addRecibo(rec);
        }
          
    } 

}