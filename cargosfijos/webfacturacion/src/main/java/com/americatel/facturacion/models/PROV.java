/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.models;

import com.americatel.facturacion.historial.Historial;
import com.americatel.facturacion.mappers.mapDEPA;
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
public class PROV   extends Historial{
    
    private static mapDEPA mapDepa=null;
    
    private int CO_PROV;
    private String NO_PROV;
    private int CO_DEPA;
    private DEPA depa;
    private Boolean ST_ELIM=false;
    
    
    @Autowired
    public void setmapDEPA(mapDEPA mapDepa){
        PROV.mapDepa=mapDepa;
    }      
    
    /*GET SET*/
    
    @JsonProperty("ST_ELIM")
    public Boolean getST_ELIM_ForJSON() {
        return ST_ELIM;
    }      
    
    @JsonProperty(value = "CO_DEPA")
    public int getCO_DEPA_ForJSON() {
        return CO_DEPA;
    }

    @JsonProperty(value = "DEPA")
    public DEPA getDEPA_ForJSON() {
        return depa;
    }
    @JsonProperty("CO_PROV")
    public int getCO_PROV_ForJSON() {
        return CO_PROV;
    }
    @JsonProperty("NO_PROV")
    public String getNO_PROV_ForJSON() {
        return NO_PROV;
    }

    public void setCO_PROV(int CO_PROV) {
        this.CO_PROV = CO_PROV;
    }

    public void setNO_PROV(String NO_PROV) {
        this.NO_PROV = NO_PROV;
    }

    public void setCO_DEPA(int CO_DEPA) {
        this.CO_DEPA = CO_DEPA;
    }
    public void setST_ELIM(Boolean ST_ELIM) {
        this.ST_ELIM = ST_ELIM;
    } 

    @JsonIgnore
    public DEPA getDepartamento() {
        return mapDepa.getId(this.CO_DEPA);
    }
    
}
