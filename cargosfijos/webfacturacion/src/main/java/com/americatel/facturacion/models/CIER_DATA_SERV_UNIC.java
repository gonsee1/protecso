/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.models;

import com.americatel.facturacion.mappers.mapCIER_DATA_SERV_UNIC;
import com.americatel.facturacion.mappers.mapFACT;
import static com.americatel.facturacion.models.CIER_DATA_SUCU.mapCIER_DATA_Sucu;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author crodas
 */
@Component
public class CIER_DATA_SERV_UNIC {
    static mapCIER_DATA_SERV_UNIC mapCier_Data_Serv_Unic;
    
    private Integer CO_CIER_DATA_SERV_UNIC;
    private Integer CO_NEGO_SUCU_SERV_UNIC;
    private Long CO_RECI;
    private Long CO_FACT;
    private Integer CO_CIER_DATA_SUCU;
    private Double IM_MONT;
    private String DE_NOMB;
    private Integer CO_CIER_COBR=null;
    private Boolean ST_AFEC_DETR=false;
    private Long CO_BOLE;
    @Autowired
    public void setmapCIER_DATA_SERV_UNIC(mapCIER_DATA_SERV_UNIC mapCier_Data_Serv_Unic){
    	CIER_DATA_SERV_UNIC.mapCier_Data_Serv_Unic=mapCier_Data_Serv_Unic;
    }    
    
    public Integer getCO_CIER_DATA_SERV_UNIC() {
        return CO_CIER_DATA_SERV_UNIC;
    }
    
    public Integer getCO_NEGO_SUCU_SERV_UNIC() {
        return CO_NEGO_SUCU_SERV_UNIC;
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
    
    public Integer getCO_CIER_COBR() {
        return CO_CIER_COBR;
    }

    public void setCO_CIER_DATA_SERV_UNIC(Integer CO_CIER_DATA_SERV_UNIC) {
        this.CO_CIER_DATA_SERV_UNIC = CO_CIER_DATA_SERV_UNIC;
    }
    
    public void setCO_NEGO_SUCU_SERV_UNIC(Integer CO_NEGO_SUCU_SERV_UNIC) {
        this.CO_NEGO_SUCU_SERV_UNIC = CO_NEGO_SUCU_SERV_UNIC;
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

    
    public Boolean getST_AFEC_DETR() {
        return ST_AFEC_DETR;
    }

    public void setST_AFEC_DETR(Boolean ST_AFEC_DETR) {
        this.ST_AFEC_DETR = ST_AFEC_DETR;
    }
    
    public void setCO_CIER_COBR(Integer CO_CIER_COBR) {
        this.CO_CIER_COBR = CO_CIER_COBR;
    }

    @JsonIgnore
    public void save() {
        mapCier_Data_Serv_Unic.insertar(this);
    }
    
    @JsonIgnore
    public CIER_DATA_SUCU getCIER_DATA_SUCU(){
        if (this.CO_CIER_DATA_SUCU!=null)
            return mapCIER_DATA_Sucu.getId(this.CO_CIER_DATA_SUCU);
        return null;
    }

    /**
     * @return the CO_BOLE
     */
    public Long getCO_BOLE() {
        return CO_BOLE;
    }

    /**
     * @param CO_BOLE the CO_BOLE to set
     */
    public void setCO_BOLE(Long CO_BOLE) {
        this.CO_BOLE = CO_BOLE;
    }
    
}
