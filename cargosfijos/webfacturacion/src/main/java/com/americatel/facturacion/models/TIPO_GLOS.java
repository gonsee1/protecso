/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author rordonez
 */
public class TIPO_GLOS {
    private int CO_TIPO_GLOS;
    private String NO_TIPO_GLOS;
    private Boolean ST_ELIM=false;
    
    
    @Override
    public String toString(){
        return "(id = "+this.CO_TIPO_GLOS+", Nombre = " +this.NO_TIPO_GLOS+")";
    }
    
    /*GET SET*/
    
    @JsonProperty("ST_ELIM")
    public Boolean getST_ELIM_ForJSON() {
        return ST_ELIM;
    }      
    
    @JsonProperty("CO_TIPO_GLOS")
    public int getCO_TIPO_GLOS_ForJSON() {
        return CO_TIPO_GLOS;
    }
    @JsonProperty("NO_TIPO_GLOS")
    public String getNO_TIPO_GLOS_ForJSON() {
        return NO_TIPO_GLOS;
    }

    public void setCO_TIPO_GLOS(int CO_TIPO_GLOS) {
        this.CO_TIPO_GLOS = CO_TIPO_GLOS;
    }

    public void setNO_TIPO_GLOS(String NO_TIPO_GLOS) {
        this.NO_TIPO_GLOS = NO_TIPO_GLOS;
    }
    public void setST_ELIM(Boolean ST_ELIM) {
        this.ST_ELIM = ST_ELIM;
    }     
}
