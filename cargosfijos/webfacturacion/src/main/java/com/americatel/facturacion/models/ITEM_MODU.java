/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.models;

import com.americatel.facturacion.historial.Historial;
import com.americatel.facturacion.mappers.mapMODU;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author crodas
 * 
 * Items de Modulos
 */
//@JsonIgnoreProperties(value={"modulo"})
@Component
public class ITEM_MODU implements Serializable{
    private static mapMODU mapModu; 
    
    private int CO_ITEM_MODU;    
    private int CO_MODU;
    private String NO_ITEM_MODU;
    private String DE_PACK;
    private MODU modu;
    private Boolean ST_ELIM=false;
    
    @Autowired
    public void setmapMODU(mapMODU mapModu){
        ITEM_MODU.mapModu=mapModu;
    }
    
    @JsonIgnore
    public MODU getModulo(){
        return mapModu.getId(this.CO_MODU);
    }
    
    /*GET SET*/ 
    
    @JsonProperty("ST_ELIM")
    public Boolean getST_ELIM_ForJSON() {
        return ST_ELIM;
    }      
    
    @JsonProperty("MODU")
    public MODU getMODU_ForJSON(){
        return this.modu;
    }
    
    
    @JsonProperty("CO_ITEM_MODU")
    public int getCO_ITEM_MODU_ForJSON() {
        return CO_ITEM_MODU;
    }
    
    @JsonProperty("CO_MODU")
    public int getCO_MODU_ForJSON() {
        return CO_MODU;
    }
    
    @JsonProperty("NO_ITEM_MODU")
    public String getNO_ITEM_MODU_ForJSON() {
        return NO_ITEM_MODU;
    }
    
    @JsonProperty("DE_PACK")
    public String getDE_PACK_ForJSON() {
        return DE_PACK;
    }

    public void setCO_ITEM_MODU(int CO_ITEM_MODU) {
        this.CO_ITEM_MODU = CO_ITEM_MODU;
    }

    public void setCO_MODU(int CO_MODU) {
        this.CO_MODU = CO_MODU;
    }

    public void setNO_ITEM_MODU(String NO_ITEM_MODU) {
        this.NO_ITEM_MODU = NO_ITEM_MODU;
    }

    public void setDE_PACK(String DE_PACK) {
        this.DE_PACK = DE_PACK;
    }
    public void setST_ELIM(Boolean ST_ELIM) {
        this.ST_ELIM = ST_ELIM;
    }     
    
}
