/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.models;

import com.americatel.facturacion.historial.Historial;
import com.americatel.facturacion.mappers.mapPERF;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author crodas
 * 
 * Perfiles
 */
@Component
public class PERF {
    private static mapPERF mapPerf;    
    private int CO_PERF;
    private String NO_PERF;
    private Boolean ST_ELIM=false;
    
    
    @Autowired
    public void setmapPERF(mapPERF mapUsu){
        PERF.mapPerf = mapUsu;
    }
    @JsonIgnore
    public List<ITEM_MODU> getItemsModulos(){
        return mapPerf.getItemModulosByPerfil(this.CO_PERF);
    }
    
    /*GET SET*/
    
    @JsonProperty("ST_ELIM")
    public Boolean getST_ELIM_ForJSON() {
        return ST_ELIM;
    }      
    
    @JsonProperty("CO_PERF")
    public int getCO_PERF_ForJSON() {
        return CO_PERF;
    }
    @JsonProperty("NO_PERF")
    public String getNO_PERF_ForJSON() {
        return NO_PERF;
    }

    public void setCO_PERF(int CO_PERF) {
        this.CO_PERF = CO_PERF;
    }

    public void setNO_PERF(String NO_PERF) {
        this.NO_PERF = NO_PERF;
    }
    public void setST_ELIM(Boolean ST_ELIM) {
        this.ST_ELIM = ST_ELIM;
    }     
}
