/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.cores.modelo;

import com.americatel.facturacion.cores.ETipoReciboGlosa;
import com.americatel.facturacion.cores.Logs;
import com.americatel.facturacion.cores.ObjetoFacturar;
import com.americatel.facturacion.cores.Recibo;
import com.americatel.facturacion.cores.ReciboGlosa;
import com.americatel.facturacion.fncs.FechaHora;
import com.americatel.facturacion.fncs.Numero;
import com.americatel.facturacion.models.CORT;
import com.americatel.facturacion.models.SUSP;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author crodas
 */
public class MensualVencida extends ModeloFacturacion {
    public MensualVencida(ObjetoFacturar objeto) {
        super(objeto);
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
        }        
    }

    @Override
    protected void facturar(Date inicio, Date fin) {
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

    @Override
    protected void calcularConfiguraciones() {
        this.Nombre=objeto.getNombre();
        this.Monto=objeto.getMonto();
        this.FechaActivacion=objeto.getFechaActivacion();
        this.FechaDesactivacion=objeto.getFechaDesactivacion();
        this.FechaUltimaFacturacion=objeto.getUltimaFacturacion();
        this.MesesPeriodo=1; 
        this.CierreRealInicio=FechaHora.getDate(this.cier.getNU_ANO_ForJSON(), this.cier.getNU_PERI_ForJSON()-2,1);//un mes menos -2
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
                tmpPI=FechaHora.addMonths(tmpPI, 1);
            }
        }
        this.mostrarLog();        
    }

    @Override
    protected Boolean isFacturable() {
        //Regla 1)No factura si su ultima facturacion es diferente de NULL y Fecha desactivacion tambien
        //es diferente de NULL y la ultima facturacion es menor o igual  a fecha desactivacion        
        if (
                this.FechaUltimaFacturacion!=null && this.FechaDesactivacion!=null &&
                this.FechaUltimaFacturacion.getTime()>=this.FechaDesactivacion.getTime()
            )
            return false;
        
        //Regla 3)Debe verificar que no este en corte ni suspension
        if (this.estaSuspendido() || this.estaCortado() )
            return false;
        
        return true;
    }

    @Override
    protected Boolean estaCortado() {
        return this.objeto.isCortado();
    }

    @Override
    protected Boolean estaSuspendido() {
        return this.objeto.isSuspendido();
    }

    @Override
    protected void guardarSiguienteUltimaFacturacion() {
        if (this.serv_supl!=null){
            this.serv_supl.saveSiguienteUltimaFacturacion(this.PeriodoFin);
        } else if (this.plan!=null){
            this.plan.saveSiguienteUltimaFacturacion(this.PeriodoFin);
        }        
    }   
}
