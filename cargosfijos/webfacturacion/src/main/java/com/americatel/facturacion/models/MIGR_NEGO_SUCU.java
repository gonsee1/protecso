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
public class MIGR_NEGO_SUCU {
    private Integer CO_MIGR_NEGO_SUCU;
    private Integer CO_NEGO_ORIG;
    private Integer CO_NEGO_DEST;
    private Integer CO_NEGO_SUCU;
    @DateTimeFormat(pattern = fnc.FORMAT_DATE_TIME_SEPARATOR)//"yyyy-MM-dd'T'HH:mm:ss")//mmss
    private Date FH_REGI;
    private Integer CO_USUA_REGI;
    
    private NEGO_SUCU nego_sucu;
    private USUA usua;

    @JsonProperty("CO_MIGR_NEGO_SUCU")
    public Integer getCO_MIGR_NEGO_SUCU_ForJSON() {
        return CO_MIGR_NEGO_SUCU;
    }

    @JsonProperty("CO_NEGO_ORIG")
    public Integer getCO_NEGO_ORIG_ForJSON() {
        return CO_NEGO_ORIG;
    }
    
    @JsonProperty("CO_NEGO_DEST")
    public Integer getCO_NEGO_DEST_ForJSON() {
        return CO_NEGO_DEST;
    }
    
    @JsonProperty("CO_NEGO_SUCU")
    public Integer getCO_NEGO_SUCU_ForJSON() {
        return CO_NEGO_SUCU;
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
    
    @JsonProperty("NEGO_SUCU")
    public NEGO_SUCU getNEGO_SUCU() {
        return nego_sucu;
    }
    
    @JsonProperty("USUA")
    public USUA getUSUA() {
        return usua;
    }
    
    public void setCO_MIGR_NEGO_SUCU(Integer CO_MIGR_NEGO_SUCU) {
        this.CO_MIGR_NEGO_SUCU = CO_MIGR_NEGO_SUCU;
    }
    
    public void setCO_NEGO_ORIG(Integer CO_NEGO_ORIG) {
        this.CO_NEGO_ORIG = CO_NEGO_ORIG;
    }
    
    public void setCO_NEGO_DEST(Integer CO_NEGO_DEST) {
        this.CO_NEGO_DEST = CO_NEGO_DEST;
    }
    
    public void setCO_NEGO_SUCU(Integer CO_NEGO_SUCU) {
        this.CO_NEGO_SUCU = CO_NEGO_SUCU;
    }
    
    public void setFH_REGI(Date FH_REGI) {
        this.FH_REGI = FH_REGI;
    }
    
    public void setCO_USUA_REGI(Integer CO_USUA_REGI) {
        this.CO_USUA_REGI = CO_USUA_REGI;
    }
    
    public void setNEGO_SUCU(NEGO_SUCU nego_sucu) {
        this.nego_sucu = nego_sucu;
    }
    
    public void setUSUA(USUA usua) {
        this.usua = usua;
    }
}
