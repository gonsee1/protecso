/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.models;

import com.americatel.facturacion.mappers.mapCIER_DATA_NEGO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author crodas
 */
@Component
public class CIER_DATA_NEGO {
    private static mapCIER_DATA_NEGO mapCier_Data_Nego;
    
    private Integer CO_CIER_DATA_NEGO;
    private Integer CO_NEGO;
    private Integer CO_CIER;

    @Autowired
    public void setmapCIER_DATA_NEGO(mapCIER_DATA_NEGO mapCier_Data_Nego){
    	CIER_DATA_NEGO.mapCier_Data_Nego=mapCier_Data_Nego;
    }        
    
    public Integer getCO_CIER_DATA_NEGO() {
        return CO_CIER_DATA_NEGO;
    }

    public Integer getCO_NEGO() {
        return CO_NEGO;
    }

    public void setCO_CIER_DATA_NEGO(Integer CO_CIER_DATA_NEGO) {
        this.CO_CIER_DATA_NEGO = CO_CIER_DATA_NEGO;
    }

    public void setCO_NEGO(Integer CO_NEGO) {
        this.CO_NEGO = CO_NEGO;
    }

    public Integer getCO_CIER() {
        return CO_CIER;
    }

    public void setCO_CIER(Integer CO_CIER) {
        this.CO_CIER = CO_CIER;
    }

    
    
    @JsonIgnore
    public void save() {
        mapCier_Data_Nego.insertar(this);
    }

}
