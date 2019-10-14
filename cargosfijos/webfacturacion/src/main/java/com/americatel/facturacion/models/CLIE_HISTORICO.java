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
import org.springframework.stereotype.Component;

/**
 *
 * @author rordonez
 */
@Component
public class CLIE_HISTORICO {
    private Integer CO_CLIE_HIST;
    private Integer CO_CLIE;
    private String DE_INFORMACION;
    @DateTimeFormat(pattern = fnc.FORMAT_DATE_TIME_SEPARATOR)//"yyyy-MM-dd'T'HH:mm:ss")//mmss
    private Date FH_REGI;
    private Integer CO_USUA_REGI;
    
    private USUA usua;
    
    @JsonProperty("CO_CLIE_HIST")
    public Integer getCO_CLIE_HIST_ForJSON() {
        return CO_CLIE_HIST;
    }
    
    @JsonProperty("CO_CLIE")
    public Integer getCO_CLIE_ForJSON() {
        return CO_CLIE;
    }
    
    @JsonProperty("DE_INFORMACION")
    public String getDE_INFORMACION_ForJSON() {
        return DE_INFORMACION;
    }
    
    @JsonProperty("FH_REGI")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = fnc.FORMAT_DATE_TIME_SEPARATOR)
    public Date getFH_REGI_ForJSON() {
        return FH_REGI;
    }
    
    @JsonProperty("CO_USUA_REGI")
    public Integer getCO_USUA_REGI_ForJSON() {
        return CO_USUA_REGI;
    }
    
    @JsonProperty("USUA")
    public USUA getUSUA() {
        return usua;
    }
    
    public void setCO_CLIE_HIST(Integer CO_CLIE_HIST) {
        this.CO_CLIE_HIST = CO_CLIE_HIST;
    }

    public void setCO_CLIE(Integer CO_CLIE) {
        this.CO_CLIE = CO_CLIE;
    }

    public void setDE_INFORMACION(String DE_INFORMACION) {
        this.DE_INFORMACION = DE_INFORMACION;
    }
    
    public void setFH_REGI(Date FH_REGI) {
        this.FH_REGI = FH_REGI;
    }
    
    public void setCO_USUA_REGI(Integer CO_USUA_REGI) {
        this.CO_USUA_REGI = CO_USUA_REGI;
    }
    
    public void setUSUA(USUA usua) {
        this.usua = usua;
    }
}
