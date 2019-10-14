/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.cores.modelo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.americatel.facturacion.cores.ETipoReciboGlosa;
import com.americatel.facturacion.cores.Logs;
import com.americatel.facturacion.cores.ObjetoFacturar;
import com.americatel.facturacion.cores.Recibo;
import com.americatel.facturacion.cores.ReciboGlosa;
import com.americatel.facturacion.fncs.FechaHora;
import com.americatel.facturacion.fncs.Numero;

/**
 *
 * @author crodas
 */
public class GenericoNoMensualAdelantado extends ModeloFacturacion{

    public GenericoNoMensualAdelantado(ObjetoFacturar objeto) {
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
        
        if (this.FechaUltimaFacturacion==null){
            Date activo_primer_servicio=this.nego.getFecha_Inicio_Ciclo_No_Mensual();
            this.CierreRealInicio=FechaHora.getDate(this.cier.getNU_ANO_ForJSON(), this.cier.getNU_PERI_ForJSON()-1,FechaHora.getDay(activo_primer_servicio)); 
        }else{
            this.CierreRealInicio=FechaHora.addDays(this.FechaUltimaFacturacion, 1);
        }        
        this.CierreRealFin=FechaHora.addDays(FechaHora.addMonths(this.CierreRealInicio,this.MesesPeriodo),-1); 
        //le ponemos el mismo dia de fecha activacion
        //this.CierreRealFin=FechaHora.setDay(this.CierreRealFin,FechaHora.getDay(this.FechaActivacion));
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
        Date tmpPI=CierreRealInicio;
        Date tmpnextFI=null;
        this.FechasCierresFactura.add(new Date[]{tmpPI,PeriodoFin});
        while( tmpPI.getTime()>PeriodoInicio.getTime() ){
            tmpnextFI=FechaHora.addMonths(tmpPI, this.MesesPeriodo*-1);
            if (tmpnextFI.getTime()<PeriodoInicio.getTime())
                tmpnextFI=PeriodoInicio;
            this.FechasCierresFactura.add(new Date[]{tmpnextFI,FechaHora.addDays(tmpPI,-1)});
            tmpPI=tmpnextFI;
        }    
        this.mostrarLog();        
    }

    @Override
    protected Boolean isFacturable() {
        Date primer_dia_cierre_real=FechaHora.getDate(this.cier.getNU_ANO_ForJSON(), this.cier.getNU_PERI_ForJSON()-1,1); 
        
        //Regla 1)No factura si su ultima facturacion es diferente de NULL y Fecha desactivacion tambien
        //es diferente de NULL y la ultima facturacion es menor o igual  a fecha desactivacion        
        if (
                this.FechaUltimaFacturacion!=null && this.FechaDesactivacion!=null &&
                this.FechaUltimaFacturacion.getTime()>=this.FechaDesactivacion.getTime()
            )
            return false;
        
        //Regla 2)Debe verificar que no este en corte ni suspension
        if (this.estaSuspendido() || this.estaCortado() )
            return false;
        
        //Regla 3)
        if (this.FechaUltimaFacturacion!=null && this.FechaUltimaFacturacion.getTime()<primer_dia_cierre_real.getTime())
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
    protected Double getMonto(Date inicio, Date fin) {
        Integer dm=30;                        
        return Numero.redondear((this.Monto/(double)dm)*(double)FechaHora.getDiffDays(inicio, fin),2);       
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
