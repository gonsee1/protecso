/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.cores;

import com.americatel.facturacion.models.CIER;
import com.americatel.facturacion.models.CLIE;
import com.americatel.facturacion.models.NEGO;
import com.americatel.facturacion.models.NEGO_SUCU;
import com.americatel.facturacion.models.PROD;
import com.americatel.facturacion.models.CORT;
import com.americatel.facturacion.models.NEGO_SUCU_PLAN;
import com.americatel.facturacion.models.NEGO_SUCU_SERV_SUPL;
import com.americatel.facturacion.models.NEGO_SUCU_SERV_UNIC;
import com.americatel.facturacion.models.SUCU;
import com.americatel.facturacion.models.SUSP;
import java.util.List;

/**
 *
 * @author crodas
 */
public class SucursalFacturar {
    private CIER cier=null;
    private PROD prod=null;
    private NEGO nego=null;    
    private List<CORT> cortes=null;
    private List<SUSP> suspensiones=null;
    private NEGO_SUCU nego_sucu=null;
    private CLIE clie=null;
    private SUCU sucu_corr=null;
    private LstMotivoNoFactura lstMotivoNoFactura=new LstMotivoNoFactura();
    
    public EMotivoNoFactura getMotivoNoFactura(){
        return this.lstMotivoNoFactura.getMotivoNoFactura();
    }    
    
    public SucursalFacturar(CIER cier,PROD prod,CLIE clie,NEGO nego,SUCU sucu_corr,List<CORT> cortes,NEGO_SUCU nego_sucu){
        this.prod=prod;
        this.cier=cier;        
        this.nego=nego;
        this.cortes=cortes;        
        this.nego_sucu=nego_sucu;
        this.suspensiones=this.nego_sucu.getSuspensionesPendientes();
        this.clie=clie;
        this.sucu_corr=sucu_corr;
    }
    
    public LstRecibo facturar(Boolean preview){
        SUCU sucu=this.nego_sucu.getSucu_ForJSON();
        Logs.printMsg("Facturando Sucursal "+sucu.getDE_DIRE_ForJSON()+" OIT : "+this.nego_sucu.getCO_OIT_INST_ForJSON()+" CIRC: "+this.nego_sucu.getCO_CIRC_ForJSON()+" Cortes "+this.cortes.size()+" Suspensiones "+this.suspensiones.size(),2,!preview,this.cier,this.nego,this.nego_sucu);
        List<NEGO_SUCU_PLAN> planes=this.nego_sucu.getPlanes();
        List<NEGO_SUCU_SERV_SUPL> servicios_suplementarios=this.nego_sucu.getServiciosSuplementarios();
        List<NEGO_SUCU_SERV_UNIC> servicios_unicos=this.nego_sucu.getServiciosUnicosPendientes(this.cier.getCO_CIER_ForJSON());
        NEGO_SUCU_PLAN plan;
        NEGO_SUCU_SERV_SUPL servicio_suplementario;
        LstRecibo lstRecibo=new LstRecibo();
        for (NEGO_SUCU_PLAN plane : planes) {
            plan = plane;
            PlanFacturar pf=new PlanFacturar(this.cier,this.prod,this.clie,this.nego,this.sucu_corr,this.cortes,this.nego_sucu,this.suspensiones,plan,planes);
            pf.facturar(preview);
            this.lstMotivoNoFactura.addMotivosNoFactura(pf.getMotivoNoFactura());
            lstRecibo.unir(pf.getLstFacturas());
        }
        for (NEGO_SUCU_SERV_SUPL servicios_suplementario : servicios_suplementarios) {
            servicio_suplementario = servicios_suplementario;
            ServicioSuplementarioFacturar ssf=new ServicioSuplementarioFacturar(this.cier,this.prod,this.clie,this.nego,this.sucu_corr,this.cortes,this.nego_sucu,this.suspensiones,servicio_suplementario);
            ssf.facturar(preview);  
            this.lstMotivoNoFactura.addMotivosNoFactura(ssf.getMotivoNoFactura());
            lstRecibo.unir(ssf.getLstFacturas());
        }
        
        //Realizar cobros de servicios unicos, si existe algun recibo
        if (lstRecibo.size()>0){
           Recibo recibo=lstRecibo.getLst().get(0);           
           for (NEGO_SUCU_SERV_UNIC servicio_unico:servicios_unicos){
               recibo.addGlosa(nego_sucu,new ReciboGlosa(servicio_unico.getNO_SERV_UNIC_ForJSON(), servicio_unico.getIM_MONTO_ForJSON(), ETipoReciboGlosa.SERVICIO_UNICO,servicio_unico));
               if (!preview){
                   //guardamos en que cierre se cobro
                   servicio_unico.setCO_CIER_COBR(this.cier.getCO_CIER_ForJSON());
                   servicio_unico.saveCierCobr();
               }
           } 
        }
        return lstRecibo;
    }
}
