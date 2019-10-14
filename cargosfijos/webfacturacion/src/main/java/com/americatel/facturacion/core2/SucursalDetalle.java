/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.core2;

import com.americatel.facturacion.models.CIER_DATA_PLAN;
import com.americatel.facturacion.models.CIER_DATA_PROM_MONT;
import com.americatel.facturacion.models.CIER_DATA_SERV_SUPL;
import com.americatel.facturacion.models.CIER_DATA_SERV_UNIC;
import com.americatel.facturacion.models.CIER_DATA_SUCU;
import com.americatel.facturacion.models.DEPA;
import com.americatel.facturacion.models.DIST;
import com.americatel.facturacion.models.NEGO_SUCU;
import com.americatel.facturacion.models.NEGO_SUCU_SERV_UNIC;
import com.americatel.facturacion.models.PROV;
import com.americatel.facturacion.models.SUCU;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author crodas
 */
public class SucursalDetalle {
    private CIER_DATA_SUCU sucu=new CIER_DATA_SUCU();
    private List<CIER_DATA_PLAN> detalle_planes=new ArrayList<CIER_DATA_PLAN>();
    private List<CIER_DATA_SERV_SUPL> detalle_servicios_suplementarios=new ArrayList<CIER_DATA_SERV_SUPL>();
    private List<CIER_DATA_PROM_MONT> detalle_promociones_monto=new ArrayList<CIER_DATA_PROM_MONT>();
    private List<CIER_DATA_SERV_UNIC> detalle_servicios_unicos=new ArrayList<CIER_DATA_SERV_UNIC>();
    
    public SucursalDetalle(NEGO_SUCU nego_sucu){
    //public SucursalDetalle(String direccion,String a,String orden_servicio,Integer CO_NEGO_SUCU){
        SUCU sucu=nego_sucu.getSUCU();
        DIST dist=sucu.getDist_ForJSON();
        PROV prov=dist.getPROV_ForJSON();
        DEPA depa=prov.getDEPA_ForJSON();
        this.sucu.setDE_DIRE_SUCU(sucu.getDE_DIRE_ForJSON());
        this.sucu.setCO_DEPA(depa.getCO_DEPA_ForJSON());
        this.sucu.setNO_DEPA(depa.getNO_DEPA_ForJSON());
        this.sucu.setCO_PROV(prov.getCO_PROV_ForJSON());
        this.sucu.setNO_PROV(prov.getNO_PROV_ForJSON());
        this.sucu.setCO_DIST(dist.getCO_DIST_ForJSON());
        this.sucu.setNO_DIST(dist.getNO_DIST_ForJSON());
        this.sucu.setDE_ORDE(nego_sucu.getDE_ORDE_SERV_ForJSON());
        this.sucu.setCO_NEGO_SUCU(nego_sucu.getCO_NEGO_SUCU_ForJSON());
    }

    public List<CIER_DATA_PLAN> getDetalle_planes() {
        return detalle_planes;
    }

    public List<CIER_DATA_SERV_SUPL> getDetalle_servicios_suplementarios() {
        return detalle_servicios_suplementarios;
    }
    
    public List<CIER_DATA_PROM_MONT> getDetalle_promociones_monto() {
        return detalle_promociones_monto;
    }    

    public CIER_DATA_SUCU getSucu() {
        return sucu;
    }

    public List<CIER_DATA_SERV_UNIC> getDetalle_servicios_unicos() {
        return detalle_servicios_unicos;
    }

    void save() {
        sucu.save();
        for (CIER_DATA_PROM_MONT pro : detalle_promociones_monto){
            pro.setCO_CIER_DATA_SUCU(sucu.getCO_CIER_DATA_SUCU());
            pro.save();
        }
        for(CIER_DATA_PLAN p : this.detalle_planes){
            p.setCO_CIER_DATA_SUCU(sucu.getCO_CIER_DATA_SUCU());
            p.save();
        }
        
        for(CIER_DATA_SERV_SUPL s : this.detalle_servicios_suplementarios){
            s.setCO_CIER_DATA_SUCU(sucu.getCO_CIER_DATA_SUCU());
            s.save();
        }

        NEGO_SUCU_SERV_UNIC servicio_unico;
        for(CIER_DATA_SERV_UNIC su : this.detalle_servicios_unicos){
            // Registra el cobro de los servicios unicos
            servicio_unico=new NEGO_SUCU_SERV_UNIC();
            servicio_unico.setCO_NEGO_SUCU_SERV_UNIC(su.getCO_NEGO_SUCU_SERV_UNIC());
            servicio_unico.setCO_CIER_COBR(su.getCO_CIER_COBR());
            servicio_unico.saveCierCobr();
            
            su.setCO_CIER_DATA_SUCU(sucu.getCO_CIER_DATA_SUCU());
            su.save();
        }        
        
    }
    
}
