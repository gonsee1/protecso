/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.models;

import com.americatel.facturacion.historial.Historial;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;

/**
 *
 * @author crodas
 * 
 * Tipo Facturacion (Adelantado o vencido)
 */
public class DEPA extends Historial{
    private int CO_DEPA;
    private String NO_DEPA;
    private Boolean ST_ELIM=false;
    
    /*GET SET*/
    
    @JsonProperty("ST_ELIM")
    public Boolean getST_ELIM_ForJSON() {
        return ST_ELIM;
    }      
    
    @JsonProperty("CO_DEPA")
    public int getCO_DEPA_ForJSON() {
        return CO_DEPA;
    }
    @JsonProperty("NO_DEPA")
    public String getNO_DEPA_ForJSON() {
        return NO_DEPA;
    }

    public void setCO_DEPA(int CO_DEPA) {
        this.CO_DEPA = CO_DEPA;
    }

    public void setNO_DEPA(String NO_DEPA) {
        this.NO_DEPA = NO_DEPA;
    }
    public void setST_ELIM(Boolean ST_ELIM) {
        this.ST_ELIM = ST_ELIM;
    }     

    public void setCO_USUA_CREO(Integer CO_USUA_CREO) {
        this.CO_USUA_CREO = CO_USUA_CREO;
    }

    public void setCO_USUA_MODI(Integer CO_USUA_MODI) {
        this.CO_USUA_MODI = CO_USUA_MODI;
    }

    public void setFH_CREO(Date FH_CREO) {
        this.FH_CREO = FH_CREO;
    }

    public void setFH_MODI(Date FH_MODI) {
        this.FH_MODI = FH_MODI;
    }
    
    
    
}
