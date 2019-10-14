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
 */
public class NEGO_SUCU_PROM extends Historial {
    private Integer CO_NEGO_SUCU_PROM;
    private Integer CO_NEGO_SUCU;
    private Integer DE_TIPO;// tipo 1 porcentaje, 0 monto
    private Double IM_VALO;
    private Boolean ST_PLAN=false;
    private Boolean ST_SERV_SUPL=false;
    private Integer DE_ANO_INIC;
    private Integer DE_PERI_INIC;
    private Integer DE_ANO_FIN;
    private Integer DE_PERI_FIN;

    @JsonProperty("CO_NEGO_SUCU_PROM")
    public Integer getCO_NEGO_SUCU_PROM_ForJSON() {
        return CO_NEGO_SUCU_PROM;
    }

    @JsonProperty("CO_NEGO_SUCU")
    public Integer getCO_NEGO_SUCU_ForJSON() {
        return CO_NEGO_SUCU;
    }


    @JsonProperty("DE_TIPO")
    public Integer getDE_TIPO_ForJSON() {
        return DE_TIPO;
    }

    @JsonProperty("IM_VALO")
    public Double getIM_VALO_ForJSON() {
        return IM_VALO;
    }    
    
    @JsonProperty("ST_PLAN")
    public Boolean getST_PLAN_ForJSON() {
        return ST_PLAN;
    }

    @JsonProperty("ST_SERV_SUPL")
    public Boolean getST_SERV_SUPL_ForJSON() {
        return ST_SERV_SUPL;
    }


    @JsonProperty("DE_ANO_INIC")
    public Integer getDE_ANO_INIC_ForJSON() {
        return DE_ANO_INIC;
    } 

    @JsonProperty("DE_PERI_INIC")
    public Integer getDE_PERI_INIC_ForJSON() {
        return DE_PERI_INIC;
    }    
    
    @JsonProperty("DE_ANO_FIN")
    public Integer getDE_ANO_FIN_ForJSON() {
        return DE_ANO_FIN;
    } 

    @JsonProperty("DE_PERI_FIN")
    public Integer getDE_PERI_FIN_ForJSON() {
        return DE_PERI_FIN;
    }    

    
    @JsonProperty("DE_TIPO_DISPLAY")
    public String getDE_TIPO_DISPLAY_ForJSON() {
        if (this.DE_TIPO!=null){
            if (this.DE_TIPO.equals(1)){
                return "Procentaje";
            }else if (this.DE_TIPO.equals(2)){
                return "Monto";
            }
        }
        return null;
    }     
    @JsonProperty("ST_PLAN_DISPLAY")
    public String getST_PLAN_DISPLAY_ForJSON() {
        return ST_PLAN ? "Si" : "No";
    }

    @JsonProperty("ST_SERV_SUPL_DISPLAY")
    public String getST_SERV_SUPL_DISPLAY_ForJSON() {
        return ST_SERV_SUPL ? "Si" : "No";
    }
   

    public void setCO_NEGO_SUCU_PROM(Integer CO_NEGO_SUCU_PROM) {
        this.CO_NEGO_SUCU_PROM = CO_NEGO_SUCU_PROM;
    }

    public void setCO_NEGO_SUCU(Integer CO_NEGO_SUCU) {
        this.CO_NEGO_SUCU = CO_NEGO_SUCU;
    }

    public void setDE_TIPO(Integer DE_TIPO) {
        this.DE_TIPO = DE_TIPO;
    }

    public void setIM_VALO(Double IM_VALO) {
        this.IM_VALO = IM_VALO;
    } 
    
    public void setST_PLAN(Boolean ST_PLAN) {
        this.ST_PLAN = ST_PLAN;
    }

    public void setST_SERV_SUPL(Boolean ST_SERV_SUPL) {
        this.ST_SERV_SUPL = ST_SERV_SUPL;
    }

    public void setDE_ANO_INIC(Integer DE_ANO_INIC) {
        this.DE_ANO_INIC = DE_ANO_INIC;
    }

    public void setDE_PERI_INIC(Integer DE_PERI_INIC) {
        this.DE_PERI_INIC = DE_PERI_INIC;
    }

    public void setDE_ANO_FIN(Integer DE_ANO_FIN) {
        this.DE_ANO_FIN = DE_ANO_FIN;
    }

    public void setDE_PERI_FIN(Integer DE_PERI_FIN) {
        this.DE_PERI_FIN = DE_PERI_FIN;
    }


    
    
}
