/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.models;

import com.americatel.facturacion.historial.Historial;
import com.americatel.facturacion.mappers.mapAJUS;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author crodas
 */
@Component
public class AJUS extends Historial {
    private static mapAJUS mapAjus;
    
    
    private Integer CO_AJUS;
    private Integer CO_NEGO;
    private String DE_GLOS;
    private Double IM_MONT;
    private Boolean ST_AFEC_IGV;
    private Integer CO_CIER_APLI=null;
    private Boolean ST_PEND=Boolean.TRUE;

    
    @Autowired
    public void setmapAJUS(mapAJUS mapAjus){
        AJUS.mapAjus=mapAjus;
    }    
    
    
    @JsonProperty("CO_AJUS")
    public Integer getCO_AJUS_ForJSON() {
        return CO_AJUS;
    }

    @JsonProperty("CO_NEGO")
    public Integer getCO_NEGO_ForJSON() {
        return CO_NEGO;
    }

    @JsonProperty("DE_GLOS")
    public String getDE_GLOS_ForJSON() {
        return DE_GLOS;
    }

    @JsonProperty("IM_MONT")
    public Double getIM_MONT_ForJSON() {
        return IM_MONT;
    }

    @JsonProperty("ST_AFEC_IGV")
    public Boolean getST_AFEC_IGV_ForJSON() {
        return ST_AFEC_IGV;
    }

    @JsonProperty("CO_CIER_APLI")
    public Integer getCO_CIER_APLI_ForJSON() {
        return CO_CIER_APLI;
    }

    @JsonProperty("ST_PEND")
    public Boolean getST_PEND_ForJSON() {
        return ST_PEND;
    }

    public void setCO_AJUS(Integer CO_AJUS) {
        this.CO_AJUS = CO_AJUS;
    }

    public void setCO_NEGO(Integer CO_NEGO) {
        this.CO_NEGO = CO_NEGO;
    }

    public void setDE_GLOS(String DE_GLOS) {
        this.DE_GLOS = DE_GLOS;
    }

    public void setIM_MONT(Double IM_MONT) {
        this.IM_MONT = IM_MONT;
    }

    public void setST_AFEC_IGV(Boolean ST_AFEC_IGV) {
        this.ST_AFEC_IGV = ST_AFEC_IGV;
    }

    public void setCO_CIER_APLI(Integer CO_CIER_APLI) {
        this.CO_CIER_APLI = CO_CIER_APLI;
    }

    public void setST_PEND(Boolean ST_PEND) {
        this.ST_PEND = ST_PEND;
    }
    
    
    public void saveCierreAplico() {
        mapAjus.saveCierreAplico(this);
    }
    
    
}
