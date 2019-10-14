/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.models;

import com.americatel.facturacion.historial.Historial;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author crodas
 * 
 * Tipo Facturacion (Adelantado o vencido)
 */
public class CONT_CLIE extends Historial {
    private Integer CO_CONT_CLIE;
    private Integer CO_CLIE;
    private Integer CO_TIPO_DOCU;
    private String DE_NUME_DOCU;
    private String NO_CONT_CLIE;
    private String AP_CONT_CLIE;
    private String DE_CORR;
    private String DE_TELE;
    private Boolean ST_ELIM=false;
     
    /*GET SET*/
    
    @JsonProperty("ST_ELIM")
    public Boolean getST_ELIM_ForJSON() {
        return ST_ELIM;
    }      
    
    @JsonProperty("CO_CONT_CLIE")
    public Integer getCO_CONT_CLIE_ForJSON() {
        return CO_CONT_CLIE;
    }
    
    @JsonProperty("CO_CLIE")
    public Integer getCO_CLIE_ForJSON() {
        return CO_CLIE;
    }
    
    @JsonProperty("CO_TIPO_DOCU")
    public Integer getCO_TIPO_DOCU_ForJSON() {
        return CO_TIPO_DOCU;
    }
    
    @JsonProperty("DE_NUME_DOCU")
    public String getDE_NUME_DOCU_ForJSON() {
        return DE_NUME_DOCU;
    }    
    
    @JsonProperty("NO_CONT_CLIE")
    public String getNO_CONT_CLIE_ForJSON() {
        return NO_CONT_CLIE;
    }

    @JsonProperty("AP_CONT_CLIE")
    public String getAP_CONT_CLIE_ForJSON() {
        return AP_CONT_CLIE;
    }    
    
    @JsonProperty("DE_CORR")
    public String getDE_CORR_ForJSON() {
        return DE_CORR;
    } 
    
    @JsonProperty("DE_TELE")
    public String getDE_TELE_ForJSON() {
        return DE_TELE;
    }     

    public void setCO_CONT_CLIE(int CO_CONT_CLIE) {
        this.CO_CONT_CLIE = CO_CONT_CLIE;
    }

    public void setCO_CLIE(int CO_CLIE) {
        this.CO_CLIE = CO_CLIE;
    }

    public void setCO_TIPO_DOCU(int CO_TIPO_DOCU) {
        this.CO_TIPO_DOCU = CO_TIPO_DOCU;
    }

    public void setDE_NUME_DOCU(String DE_NUME_DOCU) {
        this.DE_NUME_DOCU = DE_NUME_DOCU;
    }    
    
    public void setNO_CONT_CLIE(String NO_CONT_CLIE) {
        this.NO_CONT_CLIE = NO_CONT_CLIE;
    }

    public void setAP_CONT_CLIE(String AP_CONT_CLIE) {
        this.AP_CONT_CLIE = AP_CONT_CLIE;
    }

    public void setDE_CORR(String DE_CORR) {
        this.DE_CORR = DE_CORR;
    }

    public void setDE_TELE(String DE_TELE) {
        this.DE_TELE = DE_TELE;
    }

    public void setST_ELIM(Boolean ST_ELIM) {
        this.ST_ELIM = ST_ELIM;
    }
    
    @JsonIgnore
    public String getFullName() {
        String apellidos="";
        String nombres="";
        if (this.AP_CONT_CLIE!=null){
            apellidos=this.AP_CONT_CLIE;
        }
        if (this.NO_CONT_CLIE!=null){
            nombres=this.NO_CONT_CLIE;
        }
        return apellidos +" "+ nombres;
    }
    
}
