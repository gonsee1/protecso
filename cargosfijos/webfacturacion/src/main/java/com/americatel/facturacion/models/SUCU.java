/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.models;

import com.americatel.facturacion.historial.Historial;
import com.americatel.facturacion.mappers.mapDIST;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author crodas
 * 
 * Productos
 */
@Component
public class SUCU  extends Historial {
    private static mapDIST mapDist=null; 
    
    private Integer CO_SUCU;
    private String DE_DIRE;
    private String DE_REF_DIRE;
    private int CO_DIST;
    private int CO_CLIE;
    private Boolean ST_ELIM=false;
    
    private DIST dist;
    private CLIE clie;

    @Autowired
    public void setmapDIST(mapDIST mapDist){
        SUCU.mapDist=mapDist;
    }      
    
    
    /*GET SET*/ 
    @JsonProperty("ST_ELIM")
    public Boolean getST_ELIM_ForJSON() {
        return ST_ELIM;
    }      
    
    @JsonProperty("CO_SUCU")
    public Integer getCO_SUCU_ForJSON() {
        return CO_SUCU;
    }

    @JsonProperty("DE_DIRE")
    public String getDE_DIRE_ForJSON() {
        return DE_DIRE;
    }
    
    @JsonProperty("DE_REF_DIRE")
    public String getDE_REF_DIRE_ForJSON() {
        return DE_REF_DIRE;
    }

    @JsonProperty("CO_DIST")
    public int getCO_DIST_ForJSON() {
        return CO_DIST;
    }

    @JsonProperty("CO_CLIE")
    public int getCO_CLIE_ForJSON() {
        return CO_CLIE;
    }

    @JsonProperty("DIST")
    public DIST getDist_ForJSON() {
        return dist;
    }

    @JsonProperty("CLIE")
    public CLIE getClie() {
        return clie;
    }

    public void setCO_SUCU(Integer CO_SUCU) {
        this.CO_SUCU = CO_SUCU;
    }

    public void setDE_DIRE(String DE_DIRE) {
        this.DE_DIRE = DE_DIRE;
    }
    
    public void setDE_REF_DIRE(String DE_REF_DIRE) {
        this.DE_REF_DIRE = DE_REF_DIRE;
    }

    public void setCO_DIST(int CO_DIST) {
        this.CO_DIST = CO_DIST;
    }

    public void setCO_CLIE(int CO_CLIE) {
        this.CO_CLIE = CO_CLIE;
    }
    public void setST_ELIM(Boolean ST_ELIM) {
        this.ST_ELIM = ST_ELIM;
    }        
    
    @JsonIgnore
    public String getUbicacion(){
        if (this.dist!=null){
            PROV prov=this.dist.getPROV_ForJSON();
            if (prov!=null){
                DEPA depa=prov.getDEPA_ForJSON();
                if (depa!=null)
                    return depa.getNO_DEPA_ForJSON()+" - "+prov.getNO_PROV_ForJSON()+" - "+this.dist.getNO_DIST_ForJSON();
            }
        }
        return "";
    }
    @JsonIgnore
    public String getDireccion(){
        if (this.getDE_DIRE_ForJSON()!=null){
            return this.getDE_DIRE_ForJSON() + " (" + this.getUbicacion()+")";
        }
        return "";
    }    
    @JsonIgnore
    public DIST getDistrito(){
        return mapDist.getId(this.CO_DIST);
    }    
}
