/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.cores;

import com.americatel.facturacion.cores.modelo.AnualAdelantado;
import com.americatel.facturacion.cores.modelo.MensualAdelantado;
import com.americatel.facturacion.cores.modelo.MensualVencida;
import com.americatel.facturacion.cores.modelo.ModeloFacturacion;
import com.americatel.facturacion.cores.modelo.GenericoNoMensualAdelantado;
import com.americatel.facturacion.cores.modelo.SemestralAdelantado;
import com.americatel.facturacion.cores.modelo.TrimestralAdelantado;
import com.americatel.facturacion.fncs.CorteSuspension;
import com.americatel.facturacion.fncs.FechaHora;
import com.americatel.facturacion.fncs.Numero;
import com.americatel.facturacion.models.CIER;
import com.americatel.facturacion.models.CLIE;
import com.americatel.facturacion.models.CORT;
import com.americatel.facturacion.models.NEGO;
import com.americatel.facturacion.models.NEGO_SUCU;
import com.americatel.facturacion.models.NEGO_SUCU_PLAN;
import com.americatel.facturacion.models.NEGO_SUCU_SERV_SUPL;
import com.americatel.facturacion.models.PROD;
import com.americatel.facturacion.models.SUCU;
import com.americatel.facturacion.models.SUSP;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
/**
 *
 * @author crodas
 */
public abstract class ObjetoFacturar {
    protected String etiqueta=null;
    protected CIER cier=null;
    protected PROD prod=null;
    protected NEGO nego=null;    
    protected List<CORT> cortes=null;    
    protected NEGO_SUCU nego_sucu=null;
    protected List<SUSP> suspensiones=null;  
    
    protected NEGO_SUCU_SERV_SUPL serv_supl=null;
    protected NEGO_SUCU_PLAN plan=null;
    protected CLIE clie=null;
    
    protected SUCU sucu_corr=null;
    protected LstRecibo lstRecibos=new LstRecibo();
    
    protected LstMotivoNoFactura lstMotivoNoFactura=new LstMotivoNoFactura();
       
    public void addMotivosNoFactura(EMotivoNoFactura motivo){
        this.lstMotivoNoFactura.addMotivosNoFactura(motivo);
    }
    
    public EMotivoNoFactura getMotivoNoFactura(){
        return this.lstMotivoNoFactura.getMotivoNoFactura();
    }
    
    public Date getFechaActivacion(){
        if (this.plan!=null)
            return this.plan.getFE_INIC_ForJSON();
        return this.serv_supl.getFE_INIC_ForJSON();
    }
    public Date getFechaDesactivacion(){
        if (this.plan!=null)
            return this.plan.getFE_FIN_ForJSON();
        return this.serv_supl.getFE_FIN_ForJSON();
    }       
    public Double getMonto(){
        if(this.plan!=null)
            return this.plan.getIM_MONTO_ForJSON();
        return this.serv_supl.getIM_MONTO_ForJSON();
    }
    public String getNombre(){
        if(this.plan!=null)
            return this.plan.getNO_PLAN_ForJSON();
        return this.serv_supl.getNO_SERV_SUPL_ForJSON();
    }
    public ETipoReciboGlosa getTipoGlosa(){
        if(this.plan!=null)
            return ETipoReciboGlosa.PLAN;
        return ETipoReciboGlosa.SERVICIO_SUPLEMENTARIO;                    
    }
    public Boolean isCortado(){
        //Verifica si este objecto esta con corte sin reconexion
        for (CORT corte : cortes){ 
            if (corte.getFE_FIN_ForJSON() == null){
                return true;
            }
        }
        return false;
    }
    public Boolean isSuspendido(){
        //Verifica si este objecto esta con suspension sin reconexion
        for (SUSP suspension : suspensiones) 
            if (suspension.getFE_FIN_ForJSON() == null) 
                return true;
        return false;
    }    
    
    public Date getUltimaFacturacion(){
        if (this.plan!=null)
            return this.plan.getFE_ULTI_FACT_ForJSON();
        return this.serv_supl.getFE_ULTI_FACT_ForJSON();
    }       
   
    public NEGO getNego(){
        return this.nego;
    }
    public CIER getCier(){
        return this.cier;
    }    
    public CLIE getClie(){
        return this.clie;
    }     
    public NEGO_SUCU getNego_Sucu(){
        return this.nego_sucu;
    }     
    public LstRecibo getLstFacturas() {
        return lstRecibos;
    }  
    
    public SUCU getSucu_Corr(){
        return this.sucu_corr;
    }
    public NEGO_SUCU_SERV_SUPL getServ_Supl(){
        return this.serv_supl;
    }
    public NEGO_SUCU_PLAN getPlan(){
        return this.plan;
    }
    public List<CORT> getCortes(){
        return this.cortes;
    }
    public List<SUSP> getSuspensiones(){
        return this.suspensiones;
    }
    
    
    public void addRecibo(Recibo rec){
        this.lstRecibos.add(rec);
    }
    
    public void facturar(Boolean preview){
        NEGO nego=this.nego;
        ModeloFacturacion modelo=null;
        String periodo_string=null;
        String tipo_facturacion_string=null;
        if (nego.getCO_TIPO_FACT_ForJSON().equals(1)){//1 Vencida 
            tipo_facturacion_string="Vencida";
        }else{//2 Adelantada
            tipo_facturacion_string="Adelantada";
        }        
        if (nego.getCO_PERI_FACT_ForJSON().equals(1)){//1 Mes a Mes
            periodo_string="Mensual";
            if (nego.getCO_TIPO_FACT_ForJSON().equals(1)){//1 Vencida   
                modelo=new MensualVencida(this);
            }else{//2 Adelantada
                modelo=new MensualAdelantado(this);
            }
        }else if(nego.getCO_PERI_FACT_ForJSON().equals(2)){//2 trimestral
            periodo_string="Trimestral";
            if (nego.getCO_TIPO_FACT_ForJSON().equals(1)){//1 Vencida   
            }else{//2 Adelantada
                modelo=new TrimestralAdelantado(this);
            }            
        }else if(nego.getCO_PERI_FACT_ForJSON().equals(3)){//3 semestral
            periodo_string="Semestral";
            if (nego.getCO_TIPO_FACT_ForJSON().equals(1)){//1 Vencida   
            }else{//2 Adelantada
                modelo=new SemestralAdelantado(this);
            }             
        }else if(nego.getCO_PERI_FACT_ForJSON().equals(4)){//4 anual            
            periodo_string="Anual";
            periodo_string="Semestral";
            if (nego.getCO_TIPO_FACT_ForJSON().equals(1)){//1 Vencida   
            }else{//2 Adelantada
                modelo=new AnualAdelantado(this);
            }            
        }
        if (modelo!=null)
            modelo.facturar(preview);
        else{
            Logs.printError("NO SE PUEDE FACTURAR, YA QUE NO EXISTE EL MODELO DE FACTURACION DEFINIDO (NEGO: "+ nego.getCO_NEGO_ForJSON()+" periodo: "+ periodo_string+" - tipo facturacion: "+tipo_facturacion_string+" ).",1,!preview,this.cier,this.nego,this.nego_sucu);
        }
    }
    
}