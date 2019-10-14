/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.models;

import com.americatel.facturacion.historial.Historial;
import com.americatel.facturacion.mappers.mapDIST;
import com.americatel.facturacion.mappers.mapPROV;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author crodas
 * 
 * Tipo Facturacion (Adelantado o vencido)
 */
@Component
public class DIST extends Historial {
    
    private static mapPROV mapProv;
    private static mapDIST mapDist;
    
    private int CO_DIST;
    private String NO_DIST;
    private int CO_PROV;
    private PROV prov;
    private Boolean ST_ELIM=false;
  
    
    @Autowired
    public void setmapPROV(mapPROV mapProv){
        DIST.mapProv=mapProv;
    }
    @Autowired
    public void setmapDIST(mapDIST mapDist){
        DIST.mapDist=mapDist;
    }    
    
    /*GET SET*/

    @JsonProperty("ST_ELIM")
    public Boolean getST_ELIM_ForJSON() {
        return ST_ELIM;
    }      
    
    @JsonProperty(value = "CO_PROV")
    public int getCO_DEPA_ForJSON() {
        return CO_PROV;
    }

    @JsonProperty(value = "PROV")
    public PROV getPROV_ForJSON() {
        return prov;
    }
    @JsonProperty("CO_DIST")
    public int getCO_DIST_ForJSON() {
        return CO_DIST;
    }
    @JsonProperty("NO_DIST")
    public String getNO_DIST_ForJSON() {
        return NO_DIST;
    }

    public void setCO_DIST(int CO_DIST) {
        this.CO_DIST = CO_DIST;
    }

    public void setNO_DIST(String NO_DIST) {
        this.NO_DIST = NO_DIST;
    }

    public void setCO_PROV(int CO_PROV) {
        this.CO_PROV = CO_PROV;
    }
    
    public void setST_ELIM(Boolean ST_ELIM) {
        this.ST_ELIM = ST_ELIM;
    }     

    
    @JsonIgnore
    public PROV getProvincia() {
        return mapProv.getId(this.CO_PROV);
    }
    
    
    
    @JsonIgnore
    public static DIST getById(Integer CO_DIST){
        return mapDist.getId(CO_DIST);
    }
}
