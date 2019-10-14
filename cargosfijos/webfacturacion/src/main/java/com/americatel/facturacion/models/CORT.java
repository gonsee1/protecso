/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.models;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import com.americatel.facturacion.fncs.fnc;
import com.americatel.facturacion.historial.Historial;
import com.americatel.facturacion.mappers.mapCORT;
import com.americatel.facturacion.mappers.mapNEGO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author crodas
 * 
 * Tipo Facturacion (Adelantado o vencido)
 */
@Component
public class CORT extends Historial {
    private static mapCORT mapCort;    
    private static mapNEGO mapNego;
    
    private Integer CO_CORT;
    private Integer CO_NEGO;
    private Integer CO_PROD;
    @DateTimeFormat(pattern = fnc.FORMAT_DATE_TIME_SEPARATOR )//"yyyy-MM-dd'T'HH:mm:ss")
    private Date FE_INIC;
    @DateTimeFormat(pattern = fnc.FORMAT_DATE_TIME_SEPARATOR)//"yyyy-MM-dd'T'HH:mm:ss")
    private Date FE_FIN;
    private Integer CO_CIER_INIC;
    private Integer CO_CIER_FIN;
    private String DE_ESTADO;
    
    private String COD_PROD_PADRE;

    private Boolean ST_ELIM=false;
    
    @Autowired
    public void setmapCORT(mapCORT mapCort){
        CORT.mapCort=mapCort;
    }
    @Autowired
    public void setmapNEGO(mapNEGO mapNego){
        CORT.mapNego=mapNego;
    }    
    
    @JsonIgnore
    public boolean existsByInicio(){
        //Verifica si este registro existe con la fecha de inicio y el negocio
        List<CORT> lst=mapCort.selectByNegoAndInicio(this.CO_NEGO,this.FE_INIC);
        int son=lst.size();
        if (son==1){
            return true;
        }        
        return false;
    }

    @JsonIgnore
    public boolean existsByNegoAndInicio(){
        //Verifica si este registro existe con la fecha de inicio y el negocio
        List<CORT> lst=mapCort.selectByNegoAndInicio(this.CO_NEGO,this.FE_INIC);
        int son=lst.size();
        if (son>0){
            return true;
        } else {
            return false;
        }
    }
    
    @JsonIgnore
    public boolean isValid(){
        //valida si el negocio que se registra pertenece al producto asignado
        NEGO n=mapNego.getId(this.CO_NEGO);
        if (n!=null){
            if (n.getCO_PROD_ForJSON()==this.CO_PROD)
                return true;
        }
        return false;
    }
    
    @JsonIgnore
    public boolean isValidForUpdate(){
        //Verifica si este registro es valido para modificarlo   
        List<CORT> lst=mapCort.selectByNegoAndInicio(this.CO_NEGO,this.FE_INIC);
        int son=lst.size();
        if (son==1){
            return lst.get(0).FE_FIN==null && this.FE_FIN!=null;
        }        
        return false;
    }
    
    @JsonIgnore
    public CORT getCorteValidForUpdate(){
        //Verifica si este registro es valido para modificarlo   
        List<CORT> lst=mapCort.selectByNegoAndInicio(this.CO_NEGO,this.FE_INIC);
        int son=lst.size();
        if (son==1){
            return lst.get(0);
        }        
        return null;
    }
    
    @JsonIgnore
    public boolean isFechasCorrectas() {
        //Verifica si la fecha a ingresar o modificar es valida con respecto a las demas fechas que existen
        //con este negocio. Ejemplo no puede registrarse fechas cuando esta sin reconexio o cuando esta con corte
        //Solo evalua las que tienen diferentes fechas de inicio
        //las iguales se validan con las funciones existsByInicio y isValidForUpdate
        List<CORT> lst=mapCort.selectByNego(this.CO_NEGO);
        for(int i=0;i<lst.size();i++){
            CORT c=lst.get(i);
            if(c.FE_INIC.getTime()!=this.FE_INIC.getTime()){
                long tfi=this.FE_INIC.getTime();
                long cfi=c.FE_INIC.getTime();
                if(cfi<tfi){
                    if(c.FE_FIN==null)//Si la fecha de inicio es menor a otra fecha pero esta no tiene reconexion por tanto no se considera
                        return false;
                    else if (c.FE_FIN.getTime()>tfi)//Si la fecha de reconexion es mayor a la otra fecha de corte no se te toma
                        return false;
                }else{
                    if(this.FE_FIN==null)//no es valido si esta fecha que se quiere ingresar no tiene FR ya que existe otro corte myor a la FC
                        return false;
                    if(this.FE_FIN.getTime()>c.FE_INIC.getTime())//Si la FR es myor a una FC de otra no es valido
                        return false;
                }
            }
        }            
        return true;
    }
    
    /*GETS SETS*/
    
    @JsonProperty("ST_ELIM")
    public Boolean getST_ELIM_ForJSON() {
        return ST_ELIM;
    }      
    
    @JsonProperty("CO_PROD")
    public Integer getCO_PROD_ForJSON() {
        return CO_PROD;
    }

    @JsonProperty(value = "CO_CORT")
    public Integer getCO_CORT_ForJSON() {
        return CO_CORT;
    }

    @JsonProperty("CO_NEGO")
    public Integer getCO_NEGO_ForJSON() {
        return CO_NEGO;
    }

    @JsonProperty("FE_INIC")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = fnc.FORMAT_DATE)//"yyyy/MM/dd")
    public Date getFE_INIC_ForJSON() {
        return FE_INIC;
    }

    @JsonProperty("FE_FIN")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = fnc.FORMAT_DATE)//"yyyy/MM/dd")
    public Date getFE_FIN_ForJSON() {
        return FE_FIN;
    }

    @JsonProperty("CO_CIER_INIC")
    public Integer getCO_CIER_INIC_ForJSON() {
        return CO_CIER_INIC;
    }

    @JsonProperty("CO_CIER_FIN")
    public Integer getCO_CIER_FIN_ForJSON() {
        return CO_CIER_FIN;
    }

    @JsonProperty("DE_ESTADO")
    public String getDE_ESTADO_ForJSON() {
        return DE_ESTADO;
    }
    
    @JsonProperty("COD_PROD_PADRE")
    public String getCOD_PROD_PADRE_ForJSON() {
        return COD_PROD_PADRE;
    }
        
    public void setCO_PROD(Integer CO_PROD) {
        this.CO_PROD = CO_PROD;
    }

    
    public void setCO_CORT(Integer CO_CORT) {
        this.CO_CORT = CO_CORT;
    }

    public void setCO_NEGO(Integer CO_NEGO) {
        this.CO_NEGO = CO_NEGO;
    }

    public void setFE_INIC(Date FE_INIC) {
        this.FE_INIC = FE_INIC;
    }

    public void setFE_FIN(Date FE_FIN) {
        this.FE_FIN = FE_FIN;
    }

    public void setCO_CIER_INIC(Integer CO_CIER_INIC) {
        this.CO_CIER_INIC = CO_CIER_INIC;
    }

    public void setCO_CIER_FIN(Integer CO_CIER_FIN) {
        this.CO_CIER_FIN = CO_CIER_FIN;
    }

    public void setST_ELIM(Boolean ST_ELIM) {
        this.ST_ELIM = ST_ELIM;
    } 
    
    public void setDE_ESTADO(String DE_ESTADO) {
        this.DE_ESTADO = DE_ESTADO;
    }
            
    public void setCOD_PROD_PADRE(String COD_PROD_PADRE) {
        this.COD_PROD_PADRE = COD_PROD_PADRE;
    }
    
    
}

