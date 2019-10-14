/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.models;

import com.americatel.facturacion.historial.Historial;
import com.americatel.facturacion.mappers.mapFACT_GLOS;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author crodas
 */
@Component
public class FACT_GLOS extends Historial {
    private static mapFACT_GLOS mapFact_Glos=null;
    
    
    private Integer CO_FACT_GLOS;
    private Long CO_FACT;
    private Integer CO_NEGO_SUCU;
    private String DE_PERI;//periodo
    private String NO_GLOS;
    private Double IM_MONT;
    private Boolean ST_ELIM=false;
    private Integer TI_GLOS;
    private TIPO_GLOS tipo_glos;
    
    private String DE_DIRE_SUCU;
    private Integer CO_DIST;
    private String NO_DIST;
    
    
    private NEGO_SUCU nego_sucu;
    private DIST dist;
    
    @Autowired
    public void setmapFACT_GLOS(mapFACT_GLOS mapFact_Glos){
        FACT_GLOS.mapFact_Glos=mapFact_Glos;
    }    
    
    /*GETS y SETS*/
    
    @JsonProperty(value = "DE_DIRE_SUCU")
    public String getDE_DIRE_SUCU_ForJSON() {
        return DE_DIRE_SUCU;
    }
    
    @JsonProperty(value = "CO_DIST")
    public Integer getCO_DIST_ForJSON() {
        return CO_DIST;
    }
    
    @JsonProperty(value = "NO_DIST")
    public String getNO_DIST_ForJSON() {
        return NO_DIST;
    }
    
    
    @JsonProperty("DIST")
    public DIST getDIST_ForJSON() {
        return dist;
    }     
    
    @JsonProperty("DE_PERI")
    public String getDE_PERI_ForJSON() {
        return DE_PERI;
    }    
    @JsonProperty("CO_FACT_GLOS")
    public Integer getCO_FACT_GLOS_ForJSON() {
        return CO_FACT_GLOS;
    }
    @JsonProperty("CO_FACT")
    public Long getCO_FACT_ForJSON() {
        return CO_FACT;
    }
    @JsonProperty("CO_NEGO_SUCU")
    public Integer getCO_NEGO_SUCU_ForJSON() {
        return CO_NEGO_SUCU;
    }
    
    @JsonProperty("NEGO_SUCU")
    public NEGO_SUCU getNEGO_SUCU_ForJSON() {
        return nego_sucu;
    }  
    
    @JsonProperty("NO_GLOS")
    public String getNO_GLOS_ForJSON() {
        return NO_GLOS;
    }
    @JsonProperty("IM_MONT")
    public Double getIM_MONT_ForJSON() {
        return IM_MONT;
    }
    @JsonProperty("ST_ELIM")
    public Boolean getST_ELIM_ForJSON() {
        return ST_ELIM;
    }
    
    @JsonProperty("TIPO_GLOS")
    public TIPO_GLOS getTIPO_GLOS_ForJSON() {
        return tipo_glos;
    }     

    @JsonProperty(value = "TI_GLOS")
    public Integer getTI_GLOS_ForJSON() {
        return TI_GLOS;
    }
    
    public void setDE_DIRE_SUCU(String DE_DIRE_SUCU) {
        this.DE_DIRE_SUCU = DE_DIRE_SUCU;
    }
    
    public void setCO_DIST(Integer CO_DIST) {
        this.CO_DIST = CO_DIST;
    }
    
    public void setNO_DIST(String NO_DIST) {
        this.NO_DIST = NO_DIST;
    }
        
    public void setTI_GLOS(Integer TI_GLOS) {
        this.TI_GLOS = TI_GLOS;
    }
      
     public void setDE_PERI(String DE_PERI) {
        this.DE_PERI = DE_PERI;
    }   
    
    public void setCO_FACT_GLOS(Integer CO_FACT_GLOS) {
        this.CO_FACT_GLOS = CO_FACT_GLOS;
    }

    public void setCO_FACT(Long CO_FACT) {
        this.CO_FACT = CO_FACT;
    }

    public void setCO_NEGO_SUCU(Integer CO_NEGO_SUCU) {
        this.CO_NEGO_SUCU = CO_NEGO_SUCU;
    }

    public void setNO_GLOS(String NO_GLOS) {
        this.NO_GLOS = NO_GLOS;
    }

    public void setIM_MONT(Double IM_MONT) {
        this.IM_MONT = IM_MONT;
    }

    public void setST_ELIM(Boolean ST_ELIM) {
        this.ST_ELIM = ST_ELIM;
    }
    
    @JsonIgnore
    public void save() {
        mapFact_Glos.insertar(this);
    }    
            
    @JsonIgnore
    public void insertar() {
        FACT_GLOS.mapFact_Glos.insertar(this);
    }

    @JsonIgnore
    public void update() {
       FACT_GLOS.mapFact_Glos.update(this); 
    }

    @JsonIgnore
    public void delete() {
       FACT_GLOS.mapFact_Glos.delete(this);  
    }
    
    
}
