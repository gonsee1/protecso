/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.models;

import com.americatel.facturacion.historial.Historial;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author crodas
 * 
 * Tipo Facturacion (Adelantado o vencido)
 */
public class MONE_FACT  extends Historial {
    private int CO_MONE_FACT;
    private String NO_MONE_FACT;
    private String DE_SIMB;
    private Boolean ST_ELIM=false;
    
    
    /*GETS y SETS*/
    @JsonProperty("ST_ELIM")
    public Boolean getST_ELIM_ForJSON() {
        return ST_ELIM;
    }      
    
    @JsonProperty("CO_MONE_FACT")
    public int getCO_MONE_FACT_ForJSON() {
        return CO_MONE_FACT;
    }

    @JsonProperty("NO_MONE_FACT")
    public String getNO_MONE_FACT_ForJSON() {
        return NO_MONE_FACT;
    }
    @JsonProperty("DE_SIMB")
    public String getDE_SIMB_ForJSON() {
        return DE_SIMB;
    }

    public void setCO_MONE_FACT(int CO_MONE_FACT) {
        this.CO_MONE_FACT = CO_MONE_FACT;
    }

    public void setNO_MONE_FACT(String NO_MONE_FACT) {
        this.NO_MONE_FACT = NO_MONE_FACT;
    }

    public void setDE_SIMB(String DE_SIMB) {
        this.DE_SIMB = DE_SIMB;
    }
    public void setST_ELIM(Boolean ST_ELIM) {
        this.ST_ELIM = ST_ELIM;
    }     
    
    
    @JsonIgnore
    public boolean esSoles(){
        return this.CO_MONE_FACT==1;
    }
    @JsonIgnore
    public boolean esDolares(){
        return this.CO_MONE_FACT==2;
    }    
}
