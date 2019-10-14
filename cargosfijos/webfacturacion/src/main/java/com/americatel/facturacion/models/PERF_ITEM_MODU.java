/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.persistence.AssociationOverride;
import javax.persistence.AttributeOverride;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Result;

/**
 *
 * @author crodas
 * 
 * Items de Modulos
 */
//@AssociationOverride()
public class PERF_ITEM_MODU{    
    private int CO_PERF;
    private int CO_ITEM_MODU;
    private PERF perf;
    private ITEM_MODU item_modu;
    private Boolean ST_ELIM=false;
    
    /*GET SET*/

    @JsonProperty("ST_ELIM")
    public Boolean getST_ELIM_ForJSON() {
        return ST_ELIM;
    }      
    
    @JsonProperty("PERF")
    public PERF getPerf_ForJSON() {
        return perf;
    }
    @JsonProperty("ITEM_MODU")
    public ITEM_MODU getItem_modu_ForJSON() {    
        return item_modu;
    }
    
    @JsonIgnore
    public int getCO_PERF() {
        return CO_PERF;
    }
    
    @JsonIgnore
    public int getCO_ITEM_MODU() {
        return CO_ITEM_MODU;
    }

    public void setCO_PERF(int CO_PERF) {
        this.CO_PERF = CO_PERF;
    }

    public void setCO_ITEM_MODU(int CO_ITEM_MODU) {
        this.CO_ITEM_MODU = CO_ITEM_MODU;
    }
    public void setST_ELIM(Boolean ST_ELIM) {
        this.ST_ELIM = ST_ELIM;
    }     
}
