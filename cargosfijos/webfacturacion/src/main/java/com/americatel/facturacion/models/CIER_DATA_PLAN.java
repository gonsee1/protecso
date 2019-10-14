/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.models;

import com.americatel.facturacion.fncs.FechaHora;
import com.americatel.facturacion.mappers.mapCIER_DATA_PLAN;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author crodas
 */
@Component
public class CIER_DATA_PLAN{
    static @Autowired mapCIER_DATA_PLAN mapCIER_DATA_Plan;
    
    private Integer CO_CIER_DATA_PLAN;
    private Integer CO_CIER_DATA_SUCU;
    private Integer CO_NEGO_SUCU_PLAN;
    private Long CO_RECI;
    private Long CO_FACT;
    private Date FE_INIC;
    private Date FE_FIN;
    private Double IM_MONT;
    private Integer ST_TIPO_DEVO;//1 desactivacion,2 cortes,3 suspensiones, 4 promociones , 5 downgrade 
    private Integer ST_TIPO_COBR;//1 normal, 2 upgrade    
    private String DE_NOMB;
    private Boolean IS_UPGRADE;
    private Integer CO_MONE_FACT;
    private Long CO_BOLE;

    //private NEGO_SUCU_PLAN nego_sucu_plan;
    
    @Autowired
    public void setmapCIER_DATA_PLAN(mapCIER_DATA_PLAN mapCIER_DATA_Plan){
    	CIER_DATA_PLAN.mapCIER_DATA_Plan=mapCIER_DATA_Plan;
    }    
    
    public Integer getCO_CIER_DATA_PLAN() {
        return CO_CIER_DATA_PLAN;
    }


    public Integer getCO_NEGO_SUCU_PLAN() {
        return CO_NEGO_SUCU_PLAN;
    }

    public Date getFE_INIC() {
        return FE_INIC;
    }

    public Date getFE_FIN() {
        return FE_FIN;
    }
    
    public Boolean getIS_UPGRADE() {
        return IS_UPGRADE;
    }

    public Double getIM_MONT() {
        return IM_MONT;
    }

    public Integer getST_TIPO_DEVO() {
        return ST_TIPO_DEVO;
    }
    
    public Integer getCO_MONE_FACT() {
        return CO_MONE_FACT;
    }

    public void setCO_CIER_DATA_PLAN(Integer CO_CIER_DATA_PLAN) {
        this.CO_CIER_DATA_PLAN = CO_CIER_DATA_PLAN;
    }


    public void setCO_NEGO_SUCU_PLAN(Integer CO_NEGO_SUCU_PLAN) {
        this.CO_NEGO_SUCU_PLAN = CO_NEGO_SUCU_PLAN;
    }

    public void setFE_INIC(Date FE_INIC) {
        this.FE_INIC = FE_INIC;
    }

    public void setFE_FIN(Date FE_FIN) {
        this.FE_FIN = FE_FIN;
    }

    public void setIS_UPGRADE(Boolean IS_UPGRADE) {
        this.IS_UPGRADE = IS_UPGRADE;
    }
    
    public void setIM_MONT(Double IM_MONT) {
        this.IM_MONT = IM_MONT;
    }

    public void setST_TIPO_DEVO(Integer ST_TIPO_DEVO) {//1 desactivacion,2 cortes,3 suspensiones, 4 promociones , 5 downgrade 
        this.ST_TIPO_DEVO = ST_TIPO_DEVO;
    }

    public String getDE_NOMB() {
        return DE_NOMB;
    }

    public void setDE_NOMB(String DE_NOMB) {
        this.DE_NOMB = DE_NOMB;
    }

    public Integer getCO_CIER_DATA_SUCU() {
        return CO_CIER_DATA_SUCU;
    }

    public void setCO_CIER_DATA_SUCU(Integer CO_CIER_DATA_SUCU) {
        this.CO_CIER_DATA_SUCU = CO_CIER_DATA_SUCU;
    }

    public Long getCO_RECI() {
        return CO_RECI;
    }

    public Long getCO_FACT() {
        return CO_FACT;
    }

    public void setCO_RECI(Long CO_RECI) {
        this.CO_RECI = CO_RECI;
    }

    public void setCO_FACT(Long CO_FACT) {
        this.CO_FACT = CO_FACT;
    }
    
    public Integer getST_TIPO_COBR() {
        return ST_TIPO_COBR;
    }

    public void setST_TIPO_COBR(Integer ST_TIPO_COBR) {//1 normal, 2 upgrade 
        this.ST_TIPO_COBR = ST_TIPO_COBR;
    }    
    
    public void setCO_MONE_FACT(Integer CO_MONE_FACT) {
        this.CO_MONE_FACT = CO_MONE_FACT;
    }
    
    @JsonIgnore
    public void mostrar(){
        System.out.println(FechaHora.getStringDate(FE_INIC)+" - "+FechaHora.getStringDate(FE_FIN)+" - "+this.IM_MONT+"  "+this.DE_NOMB+" ("+this.getTipoString()+") ");
    }
    
    @JsonIgnore
    public String getTipoString(){
        if (this.ST_TIPO_COBR!=null){
            if (this.ST_TIPO_COBR==1)
                return "Cobrar normal";
            else
                return "Cobrar por upgrade";
        }else if(this.ST_TIPO_DEVO!=null){
            if (this.ST_TIPO_DEVO==1)
                return "Devoluci\u00f3n por desactivaci\u00f3n";
            else if (this.ST_TIPO_DEVO==2)
                return "Devoluci\u00f3n por corte o suspensi\u00f3n";
            else if (this.ST_TIPO_DEVO==4)
                return "Devoluci\u00f3n por promoci\u00f3n";
            else
                return "Devoluci\u00f3n por downgrade";
        }
        return null;
    }

    @JsonIgnore
    public void save() {
        mapCIER_DATA_Plan.insertar(this);
    }
    
    @JsonIgnore
    public Double getMontoPorDia(){        
        return this.IM_MONT/FechaHora.getDiffDays(FE_INIC, FE_FIN);
    }

    @JsonIgnore
    public String getPeriodo(){
        return "Periodo del "+FechaHora.getStringDateShortStringMonth(this.FE_INIC)+" al "+FechaHora.getStringDateShortStringMonth(this.FE_FIN);
    }
    /*@JsonIgnore
    public NEGO_SUCU_PLAN getNego_sucu_plan() {
        return nego_sucu_plan;
    }

    @JsonIgnore
    public void setNego_sucu_plan(NEGO_SUCU_PLAN nego_sucu_plan) {
        //this.nego_sucu_plan = nego_sucu_plan;
    }
   
 */    

    /**
     * @return the CO_BOLE
     */
    public Long getCO_BOLE() {
        return CO_BOLE;
    }

    /**
     * @param CO_BOLE the CO_BOLE to set
     */
    public void setCO_BOLE(Long CO_BOLE) {
        this.CO_BOLE = CO_BOLE;
    }
}
