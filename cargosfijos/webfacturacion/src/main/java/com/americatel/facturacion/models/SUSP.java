/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.models;

import com.americatel.facturacion.fncs.fnc;
import com.americatel.facturacion.historial.Historial;
import com.americatel.facturacion.mappers.mapCORT;
import com.americatel.facturacion.mappers.mapMODU;
import com.americatel.facturacion.mappers.mapNEGO;
import com.americatel.facturacion.mappers.mapNEGO_SUCU;
import com.americatel.facturacion.mappers.mapSUSP;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

/**
 *
 * @author crodas
 * 
 * Tipo Facturacion (Adelantado o vencido)
 */
@Component
public class SUSP  extends Historial {
    
    private static mapSUSP mapSusp;    
    private static mapNEGO_SUCU mapNegoSucu;    
    
    private Integer CO_SUSP;
    private Integer CO_NEGO_SUCU;
    private Integer CO_PROD;
    
    private Boolean ST_SOAR=false;
    private Integer CO_OIT_INST;
    private Integer CO_CIRC;
    private Boolean ST_ELIM=false;
    private String COD_PROD_PADRE;
    
    @DateTimeFormat(pattern = fnc.FORMAT_DATE_TIME_SEPARATOR)// "yyyy-MM-dd'T'HH:mm:ss")
    private Date FE_INIC;
    @DateTimeFormat(pattern = fnc.FORMAT_DATE_TIME_SEPARATOR)// "yyyy-MM-dd'T'HH:mm:ss")
    private Date FE_FIN;    
    
    private Integer CO_CIER_INIC;
    private Integer CO_CIER_FIN;
    private String DE_ESTADO;
    
    private NEGO_SUCU nego_sucu;

    
    
    @Autowired
    public void setmapSUSP(mapSUSP mapSusp){
        SUSP.mapSusp=mapSusp;
    }
    @Autowired
    public void setmapNEGO_SUCU(mapNEGO_SUCU mapNegoSucu){
        SUSP.mapNegoSucu=mapNegoSucu;
    }      
    
    /*GETS SETS*/
    
    @JsonProperty("ST_ELIM")
    public Boolean getST_ELIM_ForJSON() {
        return ST_ELIM;
    }      
    
    @JsonProperty("NEGO_SUCU")
    public NEGO_SUCU getNEGO_SUCU() {
        return nego_sucu;
    }
    
    
    @JsonProperty("CO_SUSP")
    public Integer getCO_SUSP_ForJSON() {
        return CO_SUSP;
    }

    @JsonProperty("CO_NEGO_SUCU")
    public Integer getCO_NEGO_SUCU_ForJSON() {
        return CO_NEGO_SUCU;
    }

    
    @JsonProperty("CO_PROD")
    public Integer getCO_PROD_ForJSON() {
        return CO_PROD;
    }

    @JsonProperty("ST_SOAR")
    public Boolean getST_SOAR_ForJSON() {
        return ST_SOAR;
    }

    @JsonProperty("CO_OIT_INST")
    public Integer getCO_OIT_INST_ForJSON() {
        return CO_OIT_INST;
    }

    @JsonProperty("CO_CIRC")
    public Integer getCO_CIRC_ForJSON() {
        return CO_CIRC;
    }

    @JsonProperty("CO_CIER_INIC")
    public Integer getCO_CIER_INIC_ForJSON() {
        return CO_CIER_INIC;
    }

    @JsonProperty("CO_CIER_FIN")
    public Integer getCO_CIER_FIN_ForJSON() {
        return CO_CIER_FIN;
    }

    @JsonProperty("FE_INIC")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = fnc.FORMAT_DATE )//"yyyy/MM/dd")
    public Date getFE_INIC_ForJSON() {
        return FE_INIC;
    }

    @JsonProperty("FE_FIN")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = fnc.FORMAT_DATE )//"yyyy/MM/dd")
    public Date getFE_FIN_ForJSON() {
        return FE_FIN;
    }

    @JsonProperty("DE_ESTADO")
    public String getDE_ESTADO_ForJSON() {
        return DE_ESTADO;
    }
    
    
    @JsonProperty("COD_PROD_PADRE")
    public String getCOD_PROD_PADRE_ForJSON() {
        return COD_PROD_PADRE;
    }
                
    public void setCO_SUSP(Integer CO_SUSP) {
        this.CO_SUSP = CO_SUSP;
    }

    public void setCO_NEGO_SUCU(Integer CO_NEGO_SUCU) {
        this.CO_NEGO_SUCU = CO_NEGO_SUCU;
    }

    public void setCO_PROD(Integer CO_PROD) {
        this.CO_PROD = CO_PROD;
    }

    public void setST_SOAR(Boolean ST_SOAR) {
        this.ST_SOAR = ST_SOAR;
    }

    public void setCO_OIT_INST(Integer CO_OIT_INST) {
        this.CO_OIT_INST = CO_OIT_INST;
    }

    public void setCO_CIRC(Integer CO_CIRC) {
        this.CO_CIRC = CO_CIRC;
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
    
    @JsonIgnore
    public boolean isValid(){
        //valida si el negocio que se registra pertenece al producto asignado
        NEGO_SUCU ns=mapNegoSucu.getId(this.CO_NEGO_SUCU);
        if (ns!=null){
            NEGO n=ns.getNEGO();
            if (n!=null){
                if (n.getCO_PROD_ForJSON()==this.CO_PROD)
                    return true;
            }
        }
        return false;
    }    
    
    @JsonIgnore
    public boolean existsByInicio(){
        //Verifica si este registro existe con la fecha de inicio y el negocio
        List<SUSP> lst=mapSusp.selectByNegoSucuAndInicio(this.CO_NEGO_SUCU,this.FE_INIC);
        int son=lst.size();
        if (son==1){
            return true;
        }        
        return false;
    }
    
    @JsonIgnore
    public boolean existsByNegoAndInicio(){
        //Verifica si este registro existe con la fecha de inicio y el negocio
        List<SUSP> lst=mapSusp.selectByNegoSucuAndInicio(this.CO_NEGO_SUCU,this.FE_INIC);
        int son=lst.size();
        if (son>0){
            return true;
        } else {
            return false;
        }
    }
    
    @JsonIgnore
    public boolean isFechasCorrectas() {
        //Verifica si la fecha a ingresar o modificar es valida con respecto a las demas fechas que existen
        //con este negocio. Ejemplo no puede registrarse fechas cuando esta sin reconexio o cuando esta con corte
        //Solo evalua las que tienen diferentes fechas de inicio
        //las iguales se validan con las funciones existsByInicio y isValidForUpdate
        List<SUSP> lst=mapSusp.selectByNegoSucu(this.CO_NEGO_SUCU);
        for(int i=0;i<lst.size();i++){
            SUSP c=lst.get(i);
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

    @JsonIgnore
    public boolean isValidForUpdate(){
        //Verifica si este registro es valido para modificarlo   
        List<SUSP> lst=mapSusp.selectByNegoSucuAndInicio(this.CO_NEGO_SUCU,this.FE_INIC);
        int son=lst.size();
        if (son==1){
            return lst.get(0).FE_FIN==null && this.FE_FIN!=null;
        }        
        return false;
    }  
    
    @JsonIgnore
    public SUSP getSuspensionValidForUpdate(){
        //Verifica si este registro es valido para modificarlo   
        List<SUSP> lst=mapSusp.selectByNegoSucuAndInicio(this.CO_NEGO_SUCU,this.FE_INIC);
        int son=lst.size();
        if (son==1){
            return lst.get(0);
        }        
        return null;
    } 
}


