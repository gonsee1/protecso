/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Component;

/**
 *
 * @author rordonez
 */
@Component
public class MOTI_DESC {
    private int CO_MOTI_DESC;
    private String NO_MOTI_DESC;

    @JsonProperty("CO_MOTI_DESC")
    public int getCO_MOTI_DESC_ForJSON() {
        return CO_MOTI_DESC;
    }
    
    @JsonProperty("NO_MOTI_DESC")
    public String getNO_MOTI_DESC_ForJSON() {
        return NO_MOTI_DESC;
    }

    public void setCO_MOTI_DESC(int CO_MOTI_DESC) {
        this.CO_MOTI_DESC = CO_MOTI_DESC;
    }
    
    public void setNO_MOTI_DESC(String NO_MOTI_DESC) {
        this.NO_MOTI_DESC = NO_MOTI_DESC;
    }
}
