/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.core2;

import java.util.ArrayList;
import java.util.List;

import com.americatel.facturacion.models.CIER_DATA_NEGO;
import com.americatel.facturacion.models.CIER_DATA_PROM_MONT;

/**
 *
 * @author crodas
 */
public class NegocioDetalle {
    private CIER_DATA_NEGO nego=new CIER_DATA_NEGO();
    private List<CIER_DATA_PROM_MONT> detalle_promociones_monto=new ArrayList<CIER_DATA_PROM_MONT>();
    private List<SucursalDetalle> detalle_sucursales=new ArrayList<SucursalDetalle>();
    public NegocioDetalle(Integer CO_NEGO,Integer CO_CIER){
        nego.setCO_NEGO(CO_NEGO);
        nego.setCO_CIER(CO_CIER);
    }

    public List<CIER_DATA_PROM_MONT> getDetalle_promociones_monto() {
        return detalle_promociones_monto;
    }

    public List<SucursalDetalle> getDetalle_sucursales() {
        return detalle_sucursales;
    }

    public CIER_DATA_NEGO getNego() {
        return nego;
    }

    void save() {
        this.nego.save();
        for (CIER_DATA_PROM_MONT pro : detalle_promociones_monto){
            pro.setCO_CIER_DATA_NEGO(this.nego.getCO_CIER_DATA_NEGO());
            pro.save();
        }
        for(SucursalDetalle sd : this.detalle_sucursales){
            sd.getSucu().setCO_CIER_DATA_NEGO(this.nego.getCO_CIER_DATA_NEGO());
            sd.save();
        }
    }
    
    
}
