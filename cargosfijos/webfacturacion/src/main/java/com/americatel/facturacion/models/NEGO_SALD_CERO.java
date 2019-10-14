/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import org.springframework.stereotype.Component;

/**
 *
 * @author rordonez
 */
@Component
public class NEGO_SALD_CERO {
    private Integer CO_NEGO_SALD_CERO;
    private Integer CO_NEGO;
    private Date FE_CORTE;
    private Boolean ST_ELIM=false;
    
    @JsonProperty("CO_NEGO_SALD_CERO")
    public Integer getCO_NEGO_SALD_CERO_ForJSON() {
        return CO_NEGO_SALD_CERO;
    }
    
    @JsonProperty("CO_NEGO")
    public Integer getCO_NEGO_ForJSON() {
        return CO_NEGO;
    }
    
    @JsonProperty("FE_CORTE")
    public Date getFE_CORTE_ForJSON() {
        return FE_CORTE;
    }
    
    @JsonProperty("ST_ELIM")
    public Boolean getST_ELIM_ForJSON() {
        return ST_ELIM;
    }  
    
    public void setCO_NEGO_SALD_CERO(Integer CO_NEGO_SALD_CERO) {
        this.CO_NEGO_SALD_CERO = CO_NEGO_SALD_CERO;
    }

    public void setCO_NEGO(Integer CO_NEGO) {
        this.CO_NEGO = CO_NEGO;
    }

    public void setFE_CORTE(Date FE_CORTE) {
        this.FE_CORTE = FE_CORTE;
    }

    public void setST_ELIM(Boolean ST_ELIM) {
        this.ST_ELIM = ST_ELIM;
    }
}
