/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.models;

import com.americatel.facturacion.historial.Historial;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author crodas
 * 
 * Tipo Facturacion (Adelantado o vencido)
 */
public class TIPO_CLIE  extends Historial {      
    private Integer CO_TIPO_CLIE;
    private String NO_TIPO_CLIE;

    @JsonProperty("CO_TIPO_CLIE")
    public Integer getCO_TIPO_CLIE_ForJSON() {
        return CO_TIPO_CLIE;
    }

    @JsonProperty("NO_TIPO_CLIE")
    public String getNO_TIPO_CLIE_ForJSON() {
        return NO_TIPO_CLIE;
    }
    
    
    public void setCO_TIPO_CLIE(Integer CO_TIPO_CLIE) {
        this.CO_TIPO_CLIE = CO_TIPO_CLIE;
    }

    public void setNO_TIPO_CLIE(String NO_TIPO_CLIE) {
        this.NO_TIPO_CLIE = NO_TIPO_CLIE;
    }
    
    
}

