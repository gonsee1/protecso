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
public class TIPO_FACT  extends Historial {
    private int CO_TIPO_FACT;
    private String NO_TIPO_FACT;
    private Boolean ST_ELIM=false;
    
    
    @Override
    public String toString(){
        return "(id = "+this.CO_TIPO_FACT+", Nombre = " +this.NO_TIPO_FACT+")";
    }
    
    /*GET SET*/
    
    @JsonProperty("ST_ELIM")
    public Boolean getST_ELIM_ForJSON() {
        return ST_ELIM;
    }      
    
    @JsonProperty("CO_TIPO_FACT")
    public int getCO_TIPO_FACT_ForJSON() {
        return CO_TIPO_FACT;
    }
    @JsonProperty("NO_TIPO_FACT")
    public String getNO_TIPO_FACT_ForJSON() {
        return NO_TIPO_FACT;
    }

    public void setCO_TIPO_FACT(int CO_TIPO_FACT) {
        this.CO_TIPO_FACT = CO_TIPO_FACT;
    }

    public void setNO_TIPO_FACT(String NO_TIPO_FACT) {
        this.NO_TIPO_FACT = NO_TIPO_FACT;
    }
    public void setST_ELIM(Boolean ST_ELIM) {
        this.ST_ELIM = ST_ELIM;
    }     
}
