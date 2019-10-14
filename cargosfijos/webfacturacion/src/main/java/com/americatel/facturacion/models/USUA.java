/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.models;

import com.americatel.facturacion.fncs.permissions;
import com.americatel.facturacion.historial.Historial;
import com.americatel.facturacion.mappers.mapPERF;
import com.americatel.facturacion.mappers.mapUSUA;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author crodas
 * Usuarios
 */
@Component
public class USUA extends Historial{    
    private static mapPERF mapPerf;    
    private Integer CO_USUA;
    private Integer CO_PERF;
    private String NO_USUA;
    private String AP_USUA;
    private String DE_CORR;
    private String DE_USER;
    private String DE_PASS;
    private String DE_TOCK;
    private Boolean ST_ELIM=false;
    
    private PERF perf;
    
    
   @Autowired
    public void setmapPERF(mapPERF mapUsu){
        USUA.mapPerf = mapUsu;
    }      
    
    public PERF getPerfil(){        
        return mapPerf.getId(this.CO_PERF);
    }
    /*Getter*/
    
    @JsonProperty("ST_ELIM")
    public Boolean getST_ELIM_ForJSON() {
        return ST_ELIM;
    }    
    
    @JsonProperty("CO_USUA")
    public Integer getCO_USUA_ForJSON() {
        return CO_USUA;
    }
    
    @JsonProperty("CO_PERF")
    public Integer getCO_PERF_ForJSON() {
        return CO_PERF;
    }
    
    @JsonProperty("NO_USUA")
    public String getNO_USUA_ForJSON() {
        return NO_USUA;
    }
    @JsonProperty("AP_USUA")
    public String getAP_USUA_ForJSON() {
        return AP_USUA;
    }
    @JsonProperty("DE_CORR")
    public String getDE_CORR_ForJSON() {
        return DE_CORR;
    }
    @JsonProperty("DE_USER")
    public String getDE_USER_ForJSON() {
        return DE_USER;
    }
    @JsonProperty("PERF")
    public PERF getPERF_ForJSON() {
        return this.perf;
    }
    @JsonIgnore
    public String getDE_PASS() {
        return DE_PASS;
    }
    @JsonIgnore
    public String getDE_TOCK() {
        return DE_TOCK;
    }
    /*Setter*/

    public void setCO_USUA(Integer CO_USUA) {
        this.CO_USUA = CO_USUA;
    }

    public void setCO_PERF(Integer CO_PERF) {
        this.CO_PERF = CO_PERF;
    }

    public void setNO_USUA(String NO_USUA) {
        this.NO_USUA = NO_USUA;
    }

    public void setAP_USUA(String AP_USUA) {
        this.AP_USUA = AP_USUA;
    }

    public void setDE_CORR(String DE_CORR) {
        this.DE_CORR = DE_CORR;
    }

    public void setDE_USER(String DE_USER) {
        this.DE_USER = DE_USER;
    }

    public void setDE_PASS(String DE_PASS) {
        this.DE_PASS = DE_PASS;
    }

    public void setDE_TOCK(String DE_TOCK) {
        this.DE_TOCK = DE_TOCK;
    }
    public void setST_ELIM(Boolean ST_ELIM) {
        this.ST_ELIM = ST_ELIM;
    }     
    
    
}
