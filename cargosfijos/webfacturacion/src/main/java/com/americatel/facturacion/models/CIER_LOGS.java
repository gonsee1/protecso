/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.models;

import com.americatel.facturacion.historial.Historial;
import com.americatel.facturacion.mappers.mapCIER_LOGS;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author crodas
 */
@Component
public class CIER_LOGS extends Historial {
    private static mapCIER_LOGS mapCier_Logs=null; 
    
    
    private Integer CO_CIER_LOGS=null;
    private Integer CO_CIER=null;
    private Integer CO_NEGO=null;
    private Integer CO_NEGO_SUCU=null;
    private String DE_LOG=null;
    private Boolean ST_ERRO=false;
    
    
    @Autowired
    public void setmapCIER_LOGS(mapCIER_LOGS mapCier_Logs){
        CIER_LOGS.mapCier_Logs=mapCier_Logs;
    }         
    
    /*GETS y SETS*/
    @JsonProperty("ST_ERRO")
    public Boolean getST_ERRO_ForJSON() {
        return ST_ERRO;
    }    
    
    @JsonProperty("CO_CIER_LOGS")
    public Integer getCO_CIER_LOGS_ForJSON() {
        return CO_CIER_LOGS;
    }

    @JsonProperty("CO_CIER")
    public Integer getCO_CIER_ForJSON() {
        return CO_CIER;
    }
    
    @JsonProperty("CO_NEGO")
    public Integer getCO_NEGO_ForJSON() {
        return CO_NEGO;
    }
    @JsonProperty("CO_NEGO_SUCU")
    public Integer getCO_NEGO_SUCU_ForJSON() {
        return CO_NEGO_SUCU;
    }
    @JsonProperty("DE_LOG")
    public String getDE_LOG_ForJSON() {
        return DE_LOG;
    }
    
    
    public void setCO_CIER_LOGS(Integer CO_CIER_LOGS) {
        this.CO_CIER_LOGS = CO_CIER_LOGS;
    }

    public void setCO_CIER(Integer CO_CIER) {
        this.CO_CIER = CO_CIER;
    }

    public void setCO_NEGO(Integer CO_NEGO) {
        this.CO_NEGO = CO_NEGO;
    }
    public void setCO_NEGO_SUCU(Integer CO_NEGO_SUCU) {
        this.CO_NEGO_SUCU = CO_NEGO_SUCU;
    }
    public void setDE_LOG(String DE_LOG) {
        this.DE_LOG = DE_LOG;
    }
    public void setST_ERRO(Boolean ST_ERRO) {
        this.ST_ERRO = ST_ERRO;
    }    


    @JsonIgnore
    public void insertar() {
        CIER_LOGS.mapCier_Logs.insertar(this);
    }
    
}
