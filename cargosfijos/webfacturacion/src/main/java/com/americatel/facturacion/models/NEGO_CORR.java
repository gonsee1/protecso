/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author crodas
 */
public class NEGO_CORR {
    private Integer CO_NEGO_CORR;
    private Integer CO_NEGO;
    private String DE_CORR;
    private Boolean ST_ACTI;

    
    /*GETS y SETS*/
    
    @JsonProperty("CO_NEGO_CORR")
    public Integer getCO_NEGO_CORR_ForJSON() {
        return CO_NEGO_CORR;
    }

    @JsonProperty("CO_NEGO")
    public Integer getCO_NEGO_ForJSON() {
        return CO_NEGO;
    }

    @JsonProperty("DE_CORR")
    public String getDE_CORR_ForJSON() {
        return DE_CORR;
    }

    @JsonProperty("ST_ACTI")
    public Boolean getST_ACTI_ForJSON() {
        return ST_ACTI;
    }

    public void setCO_NEGO_CORR(Integer CO_NEGO_CORR) {
        this.CO_NEGO_CORR = CO_NEGO_CORR;
    }

    public void setCO_NEGO(Integer CO_NEGO) {
        this.CO_NEGO = CO_NEGO;
    }

    public void setDE_CORR(String DE_CORR) {
        this.DE_CORR = DE_CORR;
    }

    public void setST_ACTI(Boolean ST_ACTI) {
        this.ST_ACTI = ST_ACTI;
    }
    
    
}
