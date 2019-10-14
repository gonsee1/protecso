/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.models;

import com.americatel.facturacion.historial.Historial;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

/**
 *
 * @author crodas
 * 
 * Modulos
 */
public class MODU implements Serializable{
    private int CO_MODU; 
    private String NO_MODU; 
    private String DE_PACK;
    private String DE_ICON_CLSS;
    private Boolean ST_ELIM=false;
   
    /*GET SET*/  
    
    @JsonProperty("ST_ELIM")
    public Boolean getST_ELIM_ForJSON() {
        return ST_ELIM;
    }      
    
    @JsonProperty(value = "DE_ICON_CLSS")
    public String getDE_ICON_CLSS_ForJSON() {
        return DE_ICON_CLSS;
    }

    @JsonProperty(value = "CO_MODU")
    public int getCO_MODU_ForJSON() {
        return CO_MODU;
    }
    @JsonProperty("NO_MODU")
    public String getNO_MODU_ForJSON() {
        return NO_MODU;
    }
    @JsonProperty("DE_PACK")
    public String getDE_PACK_ForJSON() {
        return DE_PACK;
    }

    public void setCO_MODU(int CO_MODU) {
        this.CO_MODU = CO_MODU;
    }

    public void setNO_MODU(String NO_MODU) {
        this.NO_MODU = NO_MODU;
    }

    public void setDE_PACK(String DE_PACK) {
        this.DE_PACK = DE_PACK;
    }

    public void setDE_ICON_CLSS(String DE_ICON_CLSS) {
        this.DE_ICON_CLSS = DE_ICON_CLSS;
    }
    public void setST_ELIM(Boolean ST_ELIM) {
        this.ST_ELIM = ST_ELIM;
    }     
    
    
}
