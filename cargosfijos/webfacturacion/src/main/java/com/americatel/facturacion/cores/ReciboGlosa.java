/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.cores;

import com.americatel.facturacion.models.NEGO_SUCU_PLAN;
import com.americatel.facturacion.models.NEGO_SUCU_SERV_SUPL;
import com.americatel.facturacion.models.NEGO_SUCU_SERV_UNIC;

/**
 *
 * @author crodas
 */
public class ReciboGlosa {
    private String nombre=null;
    private Double monto=null;
    private ETipoReciboGlosa tipo_glosa;
    private NEGO_SUCU_SERV_SUPL serv_supl=null;
    private NEGO_SUCU_PLAN plan=null;
    private NEGO_SUCU_SERV_UNIC serv_unic=null;

    public ReciboGlosa(String nombre, Double monto, ETipoReciboGlosa tipo_glosa, NEGO_SUCU_SERV_SUPL serv_supl) {
        this.nombre=nombre;
        this.monto=monto;
        this.tipo_glosa=tipo_glosa;
        this.serv_supl=serv_supl;
    }

    public ReciboGlosa(String nombre, Double monto, ETipoReciboGlosa tipo_glosa, NEGO_SUCU_PLAN plan) {
        this.nombre=nombre;
        this.monto=monto;
        this.tipo_glosa=tipo_glosa;  
        this.plan=plan;
    }
    
    public ReciboGlosa(String nombre, Double monto, ETipoReciboGlosa tipo_glosa, NEGO_SUCU_SERV_UNIC serv_unic) {
        this.nombre=nombre;
        this.monto=monto;
        this.tipo_glosa=tipo_glosa;  
        this.serv_unic=serv_unic;
    }
    
    public ReciboGlosa(String nombre, Double monto, ETipoReciboGlosa tipo_glosa) {
        //if (tipo_glosa==ETipoReciboGlosa.DEVOLUCION_CARGOS_FIJOS_SALDO){
        this.nombre=nombre;
        this.monto=monto;
        this.tipo_glosa=tipo_glosa;              
        //}
    }
    
    
    
    public String toString(){
        return "Glosa: "+this.nombre+", monto: "+this.monto+", tipo glosa: "+this.tipo_glosa;
    }

    public String getNombre() {
        return nombre;
    }

    public Double getMonto() {
        return monto;
    }
    
    public ETipoReciboGlosa getETipoGlosa(){
        return this.tipo_glosa;
    }
    
    public Integer getTipoGlosa(){
        return ReciboGlosa.getTipoGlosa(this.tipo_glosa);
    }
    public static ETipoReciboGlosa getETipoReciboGlosa(Integer t){
        if (t.equals(1)) return ETipoReciboGlosa.PLAN;
        if (t.equals(2)) return ETipoReciboGlosa.SERVICIO_SUPLEMENTARIO;
        if (t.equals(3)) return ETipoReciboGlosa.SERVICIO_UNICO;
        if (t.equals(10)) return ETipoReciboGlosa.DEVOLUCION_POR_DESACTIVACION;
        if (t.equals(11)) return ETipoReciboGlosa.DEVOLUCION_CARGOS_FIJOS_SALDO;
        if (t.equals(12)) return ETipoReciboGlosa.DEVOLUCION_POR_CORTE_O_SUSPENSION_SERVICIO;
        if (t.equals(20)) return ETipoReciboGlosa.PROMOCIONES_PORCENTAJE;
        if (t.equals(21)) return ETipoReciboGlosa.PROMOCIONES_MONTO;
        if (t.equals(30)) return ETipoReciboGlosa.AJUSTES_AFECTOS_IGV;
        if (t.equals(31)) return ETipoReciboGlosa.AJUSTES_NO_AFECTOS_IGV;
        if (t.equals(50)) return ETipoReciboGlosa.COBRO_ADICIONAL_UPGRADE;
        return null;
    }
    public static Integer getTipoGlosa(ETipoReciboGlosa tipo_glosa){
        if (tipo_glosa.equals(ETipoReciboGlosa.PLAN)) return 1;
        if (tipo_glosa.equals(ETipoReciboGlosa.SERVICIO_SUPLEMENTARIO)) return 2;
        if (tipo_glosa.equals(ETipoReciboGlosa.SERVICIO_UNICO)) return 3;
        if (tipo_glosa.equals(ETipoReciboGlosa.DEVOLUCION_POR_DESACTIVACION)) return 10;
        if (tipo_glosa.equals(ETipoReciboGlosa.DEVOLUCION_CARGOS_FIJOS_SALDO)) return 11;
        if (tipo_glosa.equals(ETipoReciboGlosa.DEVOLUCION_POR_CORTE_O_SUSPENSION_SERVICIO)) return 12;   
        if (tipo_glosa.equals(ETipoReciboGlosa.PROMOCIONES_PORCENTAJE)) return 20;  
        if (tipo_glosa.equals(ETipoReciboGlosa.PROMOCIONES_MONTO)) return 21;
        if (tipo_glosa.equals(ETipoReciboGlosa.AJUSTES_AFECTOS_IGV)) return 30;
        if (tipo_glosa.equals(ETipoReciboGlosa.AJUSTES_NO_AFECTOS_IGV)) return 31;
        if (tipo_glosa.equals(ETipoReciboGlosa.COBRO_ADICIONAL_UPGRADE)) return 50;
        return -1;
    }    
    public Boolean isServicioSuplementario(){
        if (this.serv_supl!=null && this.tipo_glosa==ETipoReciboGlosa.SERVICIO_SUPLEMENTARIO)
            return true;
        return false;
    }
    public Boolean isServicioUnico(){
        if (this.serv_unic!=null && this.tipo_glosa==ETipoReciboGlosa.SERVICIO_UNICO)
            return true;
        return false;
    }
    public Boolean isAfectoDetraccion(){        
        if (this.isServicioSuplementario() )
            return this.serv_supl.getST_AFEC_DETR_ForJSON();
        if (this.isServicioUnico() )
            return this.serv_unic.getST_AFEC_DETR_ForJSON();
        return false;
    }

    void setMonto(double monto) {
        this.monto=monto;
    }

    public Boolean isPlan() {
        return this.plan!=null;
    }

    public NEGO_SUCU_SERV_SUPL getServ_supl() {
        return serv_supl;
    }

    public NEGO_SUCU_PLAN getPlan() {
        return plan;
    }

    public NEGO_SUCU_SERV_UNIC getServ_unic() {
        return serv_unic;
    }
    
    
    
}
