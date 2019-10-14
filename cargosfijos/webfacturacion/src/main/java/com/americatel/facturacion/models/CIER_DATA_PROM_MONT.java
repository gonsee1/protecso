/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.models;

import com.americatel.facturacion.mappers.mapCIER_DATA_PROM_MONT;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author crodas
 */
@Component
public class CIER_DATA_PROM_MONT {
    static mapCIER_DATA_PROM_MONT mapCIER_DATA_Prom_Mont;
    private Integer CO_CIER_DATA_PROM;
    private Integer CO_CIER;
    private Long CO_RECI;
    private Long CO_FACT;
    private Integer CO_CIER_DATA_SUCU;
    private Integer CO_CIER_DATA_NEGO;
    private Double IM_MONT;
    private String DE_NOMB;
    private Date FE_INIC;
    private Date FE_FIN;

    @Autowired
    public void setmapCIER_DATA_PROM_MONT(mapCIER_DATA_PROM_MONT mapCIER_DATA_Prom_Mont){
    	CIER_DATA_PROM_MONT.mapCIER_DATA_Prom_Mont=mapCIER_DATA_Prom_Mont;
    }    
    
    public Integer getCO_CIER_DATA_PROM() {
        return CO_CIER_DATA_PROM;
    }

    public Integer getCO_CIER() {
        return CO_CIER;
    }

    public Long getCO_RECI() {
        return CO_RECI;
    }

    public Long getCO_FACT() {
        return CO_FACT;
    }


    public Integer getCO_CIER_DATA_SUCU() {
        return CO_CIER_DATA_SUCU;
    }

    public Double getIM_MONT() {
        return IM_MONT;
    }

    public String getDE_NOMB() {
        return DE_NOMB;
    }
    
    public Date getFE_INIC() {
        return FE_INIC;
    }

    public Date getFE_FIN() {
        return FE_FIN;
    }

    public void setCO_CIER_DATA_PROM(Integer CO_CIER_DATA_PROM) {
        this.CO_CIER_DATA_PROM = CO_CIER_DATA_PROM;
    }

    public void setCO_CIER(Integer CO_CIER) {
        this.CO_CIER = CO_CIER;
    }

    public void setCO_RECI(Long CO_RECI) {
        this.CO_RECI = CO_RECI;
    }

    public void setCO_FACT(Long CO_FACT) {
        this.CO_FACT = CO_FACT;
    }


    public void setCO_CIER_DATA_SUCU(Integer CO_CIER_DATA_SUCU) {
        this.CO_CIER_DATA_SUCU = CO_CIER_DATA_SUCU;
    }

    public void setIM_MONT(Double IM_MONT) {
        this.IM_MONT = IM_MONT;
    }

    public void setDE_NOMB(String DE_NOMB) {
        this.DE_NOMB = DE_NOMB;
    }

    public Integer getCO_CIER_DATA_NEGO() {
        return CO_CIER_DATA_NEGO;
    }

    public void setCO_CIER_DATA_NEGO(Integer CO_CIER_DATA_NEGO) {
        this.CO_CIER_DATA_NEGO = CO_CIER_DATA_NEGO;
    }

    public void setFE_INIC(Date FE_INIC) {
        this.FE_INIC = FE_INIC;
    }

    public void setFE_FIN(Date FE_FIN) {
        this.FE_FIN = FE_FIN;
    }
    
    @JsonIgnore
    public void save() {
        mapCIER_DATA_Prom_Mont.insertar(this);
    }
    
    
    
}
