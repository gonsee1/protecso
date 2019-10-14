/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.models;

import com.americatel.facturacion.historial.Historial;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Component;

/**
 *
 * @author crodas
 * 
 * Tipo Facturacion (Adelantado o vencido)
 */
@Component
public class TIPO_DOCU  extends Historial {
    private int CO_TIPO_DOCU;
    private String NO_TIPO_DOCU;
    private Boolean ST_ELIM=false;
    
    
    /*GET SET*/
    
    @JsonProperty("ST_ELIM")
    public Boolean getST_ELIM_ForJSON() {
        return ST_ELIM;
    }      
    
    @JsonProperty("CO_TIPO_DOCU")
    public int getCO_TIPO_DOCU_ForJSON() {
        return CO_TIPO_DOCU;
    }
    @JsonProperty("NO_TIPO_DOCU")
    public String getNO_TIPO_DOCU_ForJSON() {
        return NO_TIPO_DOCU;
    }

    public void setCO_TIPO_DOCU(int CO_TIPO_DOCU) {
        this.CO_TIPO_DOCU = CO_TIPO_DOCU;
    }

    public void setNO_TIPO_DOCU(String NO_TIPO_DOCU) {
        this.NO_TIPO_DOCU = NO_TIPO_DOCU;
    }
    public void setST_ELIM(Boolean ST_ELIM) {
        this.ST_ELIM = ST_ELIM;
    }     
    
}
