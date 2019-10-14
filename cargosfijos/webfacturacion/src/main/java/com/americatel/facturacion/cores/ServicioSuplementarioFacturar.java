/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.cores;

import com.americatel.facturacion.models.CIER;
import com.americatel.facturacion.models.CLIE;
import com.americatel.facturacion.models.CORT;
import com.americatel.facturacion.models.NEGO;
import com.americatel.facturacion.models.NEGO_SUCU;
import com.americatel.facturacion.models.NEGO_SUCU_PLAN;
import com.americatel.facturacion.models.NEGO_SUCU_SERV_SUPL;
import com.americatel.facturacion.models.PLAN;
import com.americatel.facturacion.models.PROD;
import com.americatel.facturacion.models.SUCU;
import com.americatel.facturacion.models.SUSP;
import java.util.Date;
import java.util.List;

/**
 *
 * @author crodas
 */
public class ServicioSuplementarioFacturar extends ObjetoFacturar {
    public ServicioSuplementarioFacturar(CIER cier,PROD prod,CLIE clie,NEGO nego,SUCU sucu_corr,List<CORT> cortes,NEGO_SUCU nego_sucu,List<SUSP> suspensiones,NEGO_SUCU_SERV_SUPL serv_supl){
        this.prod=prod;
        this.cier=cier;        
        this.nego=nego;
        this.cortes=cortes;        
        this.nego_sucu=nego_sucu;
        this.suspensiones=suspensiones;
        this.serv_supl=serv_supl;
        this.etiqueta="Servicio Suplementario";
        this.clie=clie;
        this.sucu_corr=sucu_corr;
    }
    /*
    public void facturar(){
        Logs.println("Facturando Servicio Suplementario "+this.serv_supl.getNO_SERV_SUPL(),3);
    }*/
/*
    @Override
    protected Boolean isCambioTarifa(Date periodo_inicio,Date periodo_fin) {
        return false;
    }

    @Override
    protected Date getFechaUltimaFacturacionCambioTarifa(Date periodo_inicio,Date periodo_fin) {
        return null;
    }

    @Override
    protected ReciboGlosa getGlosaCambioTarifa(Date periodo_inicio,Date periodo_fin) {
        return null;
    }
*/    
    
}
