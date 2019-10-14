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
 */
public class PLAN_MEDI_INST  extends Historial {
    private int CO_PLAN_MEDI_INST;
    private String NO_PLAN_MEDI_INST;
    private Boolean ST_ELIM=false;
    
    
    /*GETS y SETS*/
    @JsonProperty("ST_ELIM")
    public Boolean getST_ELIM_ForJSON() {
        return ST_ELIM;
    }  
    
    @JsonProperty("CO_PLAN_MEDI_INST")
    public int getCO_PLAN_MEDI_INST_ForJSON() {
        return CO_PLAN_MEDI_INST;
    }

    @JsonProperty("NO_PLAN_MEDI_INST")
    public String getNO_PLAN_MEDI_INST_ForJSON() {
        return NO_PLAN_MEDI_INST;
    }

    public void setCO_PLAN_MEDI_INST(int CO_PLAN_MEDI_INST) {
        this.CO_PLAN_MEDI_INST = CO_PLAN_MEDI_INST;
    }

    public void setNO_PLAN_MEDI_INST(String NO_PLAN_MEDI_INST) {
        this.NO_PLAN_MEDI_INST = NO_PLAN_MEDI_INST;
    }
    public void setST_ELIM(Boolean ST_ELIM) {
        this.ST_ELIM = ST_ELIM;
    }     
    
    
}
