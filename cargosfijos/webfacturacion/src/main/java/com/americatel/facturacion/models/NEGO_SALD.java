/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.models;

import com.americatel.facturacion.historial.Historial;
import com.americatel.facturacion.mappers.mapMONE_FACT;
import com.americatel.facturacion.mappers.mapNEGO;
import com.americatel.facturacion.mappers.mapNEGO_SALD;
import com.americatel.facturacion.mappers.mapRECI;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author crodas
 */
@Component
public class NEGO_SALD   extends Historial {
    private static mapNEGO_SALD mapNego_Sald=null; 
    private static mapNEGO mapNego=null;
    private static mapRECI mapReci=null;
    private static mapMONE_FACT mapMone_Fact=null;
    
    private Integer CO_NEGO_SALD;
    private Integer CO_NEGO;
    private Integer CO_CIER;
    private Double IM_MONT;
    private String DE_SALD;
    private Integer CO_MONE_FACT;
    private Boolean ST_ELIM=false;
    
    private NEGO nego;
    private MONE_FACT mone_fact;

    @Autowired
    public void setmapNEGO_SALD(mapNEGO_SALD mapNego_Sald){
        NEGO_SALD.mapNego_Sald=mapNego_Sald;
    }       
    
    @Autowired
    public void setmapNEGO(mapNEGO mapNego){
        NEGO_SALD.mapNego=mapNego;        
    }
    
    @Autowired
    public void setmapRECI(mapRECI mapReci){
        NEGO_SALD.mapReci=mapReci;        
    }
    
    @Autowired
    public void setmapMONE_FACT(mapMONE_FACT mapMone_Fact){
        NEGO_SALD.mapMone_Fact=mapMone_Fact;
    } 
    
    /*GETS & SETS*/
    
    @JsonProperty("ST_ELIM")
    public Boolean getST_ELIM_ForJSON() {
        return ST_ELIM;
    }    
    @JsonProperty("DE_SALD")
    public String getDE_SALD_ForJSON() {
        return DE_SALD;
    }    
    
    @JsonProperty("CO_NEGO_SALD")
    public Integer getCO_NEGO_SALD_ForJSON() {
        return CO_NEGO_SALD;
    }

    @JsonProperty("CO_NEGO")
    public Integer getCO_NEGO_ForJSON() {
        return CO_NEGO;
    }

    @JsonProperty("CO_CIER")
    public Integer getCO_CIER_ForJSON() {
        return CO_CIER;
    }

    @JsonProperty("IM_MONT")
    public Double getIM_MONT_ForJSON() {
        return IM_MONT;
    }
    
    @JsonProperty("NEGO")
    public NEGO getNEGO_ForJSON() {
        return nego;
    }
    
    @JsonProperty("CO_MONE_FACT")
    public Integer getCO_MONE_FACT_ForJSON() {
        return CO_MONE_FACT;
    }
    
    @JsonProperty("MONE_FACT")
    public MONE_FACT getMone_fact_ForJSON() {
        return mone_fact;
    }
        
    public void setCO_NEGO_SALD(Integer CO_NEGO_SALD) {
        this.CO_NEGO_SALD = CO_NEGO_SALD;
    }

    public void setCO_NEGO(Integer CO_NEGO) {
        this.CO_NEGO = CO_NEGO;
    }

    public void setCO_CIER(Integer CO_CIER) {
        this.CO_CIER = CO_CIER;
    }

    public void setIM_MONT(Double IM_MONT) {
        this.IM_MONT = IM_MONT;
    }

    public void setDE_SALD(String DE_SALD) {
        this.DE_SALD = DE_SALD;
    }
    
    public void setST_ELIM(Boolean ST_ELIM) {
        this.ST_ELIM = ST_ELIM;
    }

    public void setCO_MONE_FACT(Integer CO_MONE_FACT) {
        this.CO_MONE_FACT = CO_MONE_FACT;
    }    
    
    @JsonIgnore
    public NEGO getNEGO() {
        return mapNego.getIdClientAndProduct(this.CO_NEGO);
    }
    
    @JsonIgnore
    public List<RECI> getRecibosByCierreAndNego() {
        return mapReci.getRecibosByCierreAndNego(this.CO_CIER, this.CO_NEGO);
    }
    
    @JsonIgnore
    public void insertar() {
        mapNego_Sald.insertar(this);
    }
    
    @JsonIgnore
    public String getStringMonedaFacturacion(){
        if (this.CO_MONE_FACT==1)
            return "Soles";
        return "Dolares";
    }
    
    @JsonIgnore
    public MONE_FACT getMonedaFacturacion() {
        return mapNego_Sald.getMonedaFacturacion(this);
    }
    
}
