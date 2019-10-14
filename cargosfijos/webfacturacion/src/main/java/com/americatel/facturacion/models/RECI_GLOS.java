/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.models;

import com.americatel.facturacion.cores.ETipoReciboGlosa;
import com.americatel.facturacion.cores.ReciboGlosa;
import com.americatel.facturacion.historial.Historial;
import com.americatel.facturacion.mappers.mapRECI;
import com.americatel.facturacion.mappers.mapRECI_GLOS;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author crodas
 * 
 * Factura
 */
@Component
public class RECI_GLOS  extends Historial {
    private static mapRECI_GLOS mapReci_Glos=null; 
    
    private Integer CO_RECI_GLOS;
    private Long CO_RECI;
    private Integer CO_NEGO_SUCU;
    private String NO_GLOS;
    private String DE_DIRE_SUCU;
    private Integer CO_DIST;
    private String NO_DIST;
    private Double IM_MONT;
    private Integer TI_GLOS;
    private Boolean ST_ELIM=false;
    private Long CO_BOLE;
    
    private NEGO_SUCU nego_sucu;
    private DIST dist;
    private TIPO_GLOS tipo_glos;


    
    @Autowired
    public void setmapRECI_GLOS(mapRECI_GLOS mapReci_Glos){
        RECI_GLOS.mapReci_Glos=mapReci_Glos;
    }
    
    /*GETS y SETS*/
    @JsonProperty("NEGO_SUCU")
    public NEGO_SUCU getNEGO_SUCU_ForJSON() {
        return nego_sucu;
    }   
    
    @JsonProperty("DIST")
    public DIST getDIST_ForJSON() {
        return dist;
    } 
    
    @JsonProperty("TIPO_GLOS")
    public TIPO_GLOS getTIPO_GLOS_ForJSON() {
        return tipo_glos;
    } 
    
    @JsonProperty("ST_ELIM")
    public Boolean getST_ELIM_ForJSON() {
        return ST_ELIM;
    }  
    
    @JsonProperty(value = "CO_RECI_GLOS")
    public Integer getCO_RECI_GLOS_ForJSON() {
        return CO_RECI_GLOS;
    }

    @JsonProperty(value = "CO_RECI")
    public Long getCO_RECI_ForJSON() {
        return CO_RECI;
    }

    @JsonProperty(value = "CO_NEGO_SUCU")
    public Integer getCO_NEGO_SUCU_ForJSON() {
        return CO_NEGO_SUCU;
    }

    @JsonProperty(value = "NO_GLOS")
    public String getNO_GLOS_ForJSON() {
        return NO_GLOS;
    }
    
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

    @JsonProperty(value = "IM_MONT")
    public Double getIM_MONT_ForJSON() {
        return IM_MONT;
    }

    @JsonProperty(value = "TI_GLOS")
    public Integer getTI_GLOS_ForJSON() {
        return TI_GLOS;
    }
     
    public void setCO_RECI_GLOS(Integer CO_RECI_GLOS) {
        this.CO_RECI_GLOS = CO_RECI_GLOS;
    }

    public void setCO_RECI(Long CO_RECI) {
        this.CO_RECI = CO_RECI;
    }

    public void setCO_NEGO_SUCU(Integer CO_NEGO_SUCU) {
        this.CO_NEGO_SUCU = CO_NEGO_SUCU;
    }

    public void setNO_GLOS(String NO_GLOS) {
        this.NO_GLOS = NO_GLOS;
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

    public void setIM_MONT(Double IM_MONT) {
        this.IM_MONT = IM_MONT;
    }

    public void setTI_GLOS(Integer TI_GLOS) {
        this.TI_GLOS = TI_GLOS;
    }
    
    public void setST_ELIM(Boolean ST_ELIM) {
        this.ST_ELIM = ST_ELIM;
    } 
    
    
    @JsonIgnore
    public void save() {
        RECI_GLOS.mapReci_Glos.insertar(this);
    }
    
    @JsonIgnore
    public void insertar() {
        RECI_GLOS.mapReci_Glos.insertar(this);
    }

    @JsonIgnore
    public void update() {
       RECI_GLOS.mapReci_Glos.update(this); 
    }

    @JsonIgnore
    public void delete() {
       RECI_GLOS.mapReci_Glos.delete(this);  
    }

    @JsonProperty(value = "CO_BOLE")
    public Long getCO_BOLE() {
        return CO_BOLE;
    }

    public void setCO_BOLE(Long CO_BOLE) {
        this.CO_BOLE = CO_BOLE;
    }
    
}

