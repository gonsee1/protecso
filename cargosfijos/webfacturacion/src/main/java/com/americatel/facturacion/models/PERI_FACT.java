/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.models;

import com.americatel.facturacion.historial.Historial;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author crodas
 * 
 * Tipo Facturacion (Adelantado o vencido)
 */
public class PERI_FACT   extends Historial{    
    private Integer CO_PERI_FACT;
    private String NO_PERI_FACT;
    private Boolean ST_ELIM=false;
    
    
    @JsonProperty("ST_ELIM")
    public Boolean getST_ELIM_ForJSON() {
        return ST_ELIM;
    }      
    
    @JsonProperty("CO_PERI_FACT")
    public Integer getCO_PERI_FACT_ForJSON() {
        return CO_PERI_FACT;
    }

    @JsonProperty("NO_PERI_FACT")
    public String getNO_PERI_FACT_ForJSON() {
        return NO_PERI_FACT;
    }


    public void setCO_PERI_FACT(int CO_PERI_FACT) {
        this.CO_PERI_FACT = CO_PERI_FACT;
    }

    public void setNO_PERI_FACT(String NO_PERI_FACT) {
        this.NO_PERI_FACT = NO_PERI_FACT;
    }  
    
    public void setST_ELIM(Boolean ST_ELIM) {
        this.ST_ELIM = ST_ELIM;
    }     
}
