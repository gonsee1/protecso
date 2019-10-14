/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.cores;

import com.americatel.facturacion.fncs.CorteSuspension;
import com.americatel.facturacion.fncs.FechaHora;
import com.americatel.facturacion.fncs.Numero;
import com.americatel.facturacion.models.CIER;
import com.americatel.facturacion.models.CLIE;
import com.americatel.facturacion.models.CORT;
import com.americatel.facturacion.models.NEGO;
import com.americatel.facturacion.models.NEGO_SUCU;
import com.americatel.facturacion.models.NEGO_SUCU_PLAN;
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
public class PlanFacturar extends ObjetoFacturar{
    List<NEGO_SUCU_PLAN> planes;
    
    public PlanFacturar(CIER cier,PROD prod,CLIE clie,NEGO nego,SUCU sucu_corr,List<CORT> cortes,NEGO_SUCU nego_sucu,List<SUSP> suspensiones,NEGO_SUCU_PLAN plan,List<NEGO_SUCU_PLAN> planes){
        this.prod=prod;
        this.cier=cier;        
        this.nego=nego;
        this.cortes=cortes;        
        this.nego_sucu=nego_sucu;   
        this.suspensiones=suspensiones;
        this.plan=plan;
        this.etiqueta="Plan";
        this.clie=clie;
        this.sucu_corr=sucu_corr;
        this.planes=planes;
    }
/*
    private NEGO_SUCU_PLAN getCambioTarifa(Date periodo_inicio,Date periodo_fin) {
        if (FechaHora.isBetween(periodo_inicio, periodo_fin, this.plan.getFE_INIC_ForJSON())){
            for(NEGO_SUCU_PLAN p : this.planes){
                Date fdlst=p.getFE_FIN_ForJSON();
                Date fip=this.plan.getFE_INIC_ForJSON();
                if(p.getCO_PLAN_ForJSON()==this.plan.getCO_PLAN_ForJSON() && fdlst!=null && fdlst.getTime()==fip.getTime()){
                    return p;
                }
            }
        }
        return null;
    }
    
    @Override
    protected Boolean isCambioTarifa(Date periodo_inicio,Date periodo_fin) {
        NEGO_SUCU_PLAN p=this.getCambioTarifa(periodo_inicio, periodo_fin);
        if (p==null)
            return false;
        return true;
    }

    @Override
    protected Date getFechaUltimaFacturacionCambioTarifa(Date periodo_inicio,Date periodo_fin) {
        NEGO_SUCU_PLAN p=this.getCambioTarifa(periodo_inicio, periodo_fin);
        if (p!=null)
            return p.getFE_ULTI_FACT_ForJSON();
        return null;
    }

    @Override
    protected ReciboGlosa getGlosaCambioTarifa(Date periodo_inicio,Date periodo_fin) {
        NEGO_SUCU_PLAN p=this.getCambioTarifa(periodo_inicio, periodo_fin);
        if (p!=null){
            //int dias_sin_servicio=CorteSuspension.getDaysSinServicio(p.getFE_FIN_ForJSON(), p.getFE_ULTI_FACT_ForJSON(), cortes, suspensiones);
            Date fecha_desactivacion=p.getFE_FIN_ForJSON();
            Date ultima_facturacion=p.getFE_ULTI_FACT_ForJSON();
            double dias_devolver=FechaHora.getDiffDays(fecha_desactivacion,ultima_facturacion );
            double divisor=FechaHora.getDay(ultima_facturacion);
            double monto_glosa=Numero.redondear(((this.plan.getIM_MONTO_ForJSON()-p.getIM_MONTO_ForJSON())/divisor)*dias_devolver,2);
            return new ReciboGlosa("Devoluci\u00f3n por cargos fijos ", monto_glosa, ETipoGlosa.DEVOLUCION_CARGOS_FIJOS,p);
        }
        return null;
    }
*/

}
