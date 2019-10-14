/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.models;

import com.americatel.facturacion.fncs.fnc;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author admin
 */
public class NEGO_COME {
    private Integer CO_NEGO_COME;
    private Integer CO_USUA;
    private Integer CO_NEGO;
    private String DE_COME;
    @DateTimeFormat(pattern = fnc.FORMAT_DATE_TIME_SEPARATOR)//"yyyy-MM-dd'T'HH:mm:ss")//mmss
    private Date FH_CREO;
    
    private USUA usua;
    
    @JsonProperty("CO_NEGO_COME")
    public Integer getCO_NEGO_COME_ForJSON() {
        return CO_NEGO_COME;
    }

    @JsonProperty("CO_USUA")
    public Integer getCO_USUA_ForJSON() {
        return CO_USUA;
    }

    @JsonProperty("CO_NEGO")
    public Integer getCO_NEGO_ForJSON() {
        return CO_NEGO;
    }

    @JsonProperty("DE_COME")
    public String getDE_COME_ForJSON() {
        return DE_COME;
    }
    
    @JsonProperty("USUA")
    public USUA getUSUA() {
        return usua;
    }

    @JsonProperty("FH_CREO")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = fnc.FORMAT_DATE_TIME_SEPARATOR)
    public Date getFH_CREO_ForJSON() {
        return FH_CREO;
    }
    
    public void setCO_NEGO_COME(Integer CO_NEGO_COME) {
        this.CO_NEGO_COME = CO_NEGO_COME;
    }

    public void setCO_USUA(Integer CO_USUA) {
        this.CO_USUA = CO_USUA;
    }

    public void setCO_NEGO(Integer CO_NEGO) {
        this.CO_NEGO = CO_NEGO;
    }

    public void setDE_COME(String DE_COME) {
        this.DE_COME = DE_COME;
    }

    public void setFH_CREO(Date FH_CREO) {
        this.FH_CREO = FH_CREO;
    }
    
    public void setUSUA(USUA usua) {
        this.usua = usua;
    }
    
}
