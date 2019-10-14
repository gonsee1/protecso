/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.fncs;

import com.americatel.facturacion.models.CORT;
import com.americatel.facturacion.models.SUSP;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author crodas
 */
public class CorteSuspension {
    public static Date[] getDatesSinServicio(Date comienza,Date acaba,Date sin_servicio_inicio,Date sin_servicio_fin){
        //Calcula los verdaderos limites de un corte o suspension
        //comparandolos con un comienzo y fin
        long long_periodo_inicio=comienza.getTime(),long_periodo_fin=acaba.getTime();
        if(sin_servicio_fin==null)
            sin_servicio_fin=acaba;
        long long_corte_inicio=sin_servicio_inicio.getTime(),long_corte_fin=sin_servicio_fin.getTime();                
        if(
                ((long_corte_inicio>=long_periodo_inicio) && (long_corte_inicio<=long_periodo_fin)) && 
                (((long_corte_fin>=long_periodo_inicio) && (long_corte_fin<=long_periodo_fin)))
          ){            
            return (new Date[]{sin_servicio_inicio,sin_servicio_fin});
        }else{
            if(long_corte_fin>=long_periodo_inicio && long_corte_inicio<=long_periodo_fin){
                if(long_corte_fin>long_periodo_fin)
                    sin_servicio_fin=acaba;
                if(long_corte_inicio<long_periodo_inicio)
                    sin_servicio_inicio=comienza;
                return (new Date[]{sin_servicio_inicio,sin_servicio_fin});
            }
        }
        return null;
    }
    
    public static List<Date[]> getDatesCortes(Date comienza,Date acaba,List<CORT> listaCortes){
        List<Date[]> lst=new ArrayList<Date[]>();
        Date [] tmp=null;
        for(CORT cort:listaCortes){
            tmp=getDatesSinServicio(comienza,acaba,cort.getFE_INIC_ForJSON(),cort.getFE_FIN_ForJSON());
            if (tmp!=null)
                lst.add(tmp);
        }
        return lst;
    }
    public static List<Date[]> getDatesSuspensiones(Date comienza,Date acaba,List<SUSP> listaSuspensiones){
        List<Date[]> lst=new ArrayList<Date[]>();
        Date [] tmp=null;
        for(SUSP susp:listaSuspensiones){
            tmp=getDatesSinServicio(comienza,acaba,susp.getFE_INIC_ForJSON(),susp.getFE_FIN_ForJSON());
            if (tmp!=null)
                lst.add(tmp);
        }
        return lst;
    }
  
    public static int getDaysSinServicioByCorte(Date comienza,Date acaba,List<CORT> listaCortes){
        int dias=0;
        List<Date[]> lst=getDatesCortes(comienza, acaba, listaCortes);
        for(Date d[]:lst){
            dias+=FechaHora.getDiffDays(d[0], d[1]);
        }
        return dias;
    }  
    public static int getDaysSinServicioBySuspension(Date comienza,Date acaba,List<SUSP> listaSuspensiones){
        int dias=0;
        List<Date[]> lst=getDatesSuspensiones(comienza, acaba, listaSuspensiones);
        for(Date d[]:lst){
            dias+=FechaHora.getDiffDays(d[0], d[1]);
        }
        return dias;
    } 
    public static int getDaysSinServicio(Date comienza,Date acaba,List<CORT> listaCortes,List<SUSP> listaSuspensiones){
        return getDaysSinServicioByCorte(comienza, acaba, listaCortes)+getDaysSinServicioBySuspension(comienza, acaba, listaSuspensiones);
    }
}
