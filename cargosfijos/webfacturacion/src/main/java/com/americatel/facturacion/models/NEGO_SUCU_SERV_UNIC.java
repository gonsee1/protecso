/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.models;

import com.americatel.facturacion.historial.Historial;
import com.americatel.facturacion.mappers.mapNEGO_SUCU;
import com.americatel.facturacion.mappers.mapNEGO_SUCU_SERV_UNIC;
import com.americatel.facturacion.mappers.mapSERV_UNIC;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author crodas
 */
@Component
public class NEGO_SUCU_SERV_UNIC  extends Historial {
    private static mapNEGO_SUCU_SERV_UNIC mapNego_Sucu_Serv_Unic=null;
    private static mapSERV_UNIC mapServ_Unic=null;
    private static mapNEGO_SUCU mapNego_Sucu=null;    
    
    private Integer CO_NEGO_SUCU_SERV_UNIC;
    private Integer CO_NEGO_SUCU;
    private Boolean ST_SOAR_INST=false;
    private Integer CO_OIT_INST;
    private Integer CO_SERV_UNIC;
    private String NO_SERV_UNIC;
    private Double IM_MONTO;
    private Boolean ST_AFEC_DETR=true;
    private Boolean ST_ELIM=false;
    private Integer CO_CIER_COBR=null;

    private SERV_UNIC serv_unic;
    
    @Autowired
    public void setmapSERV_UNIC(mapSERV_UNIC mapServ_Unic){
        NEGO_SUCU_SERV_UNIC.mapServ_Unic=mapServ_Unic;        
    }    
    @Autowired
    public void setmapNEGO_SUCU(mapNEGO_SUCU mapNego_Sucu){
        NEGO_SUCU_SERV_UNIC.mapNego_Sucu=mapNego_Sucu;        
    } 
    @Autowired
    public void setmapNEGO_SUCU_SERV_UNIC(mapNEGO_SUCU_SERV_UNIC mapNego_Sucu_Serv_Unic){
        NEGO_SUCU_SERV_UNIC.mapNego_Sucu_Serv_Unic=mapNego_Sucu_Serv_Unic;        
    }    
    
    @JsonProperty("CO_NEGO_SUCU_SERV_UNIC")
    public Integer getCO_NEGO_SUCU_SERV_UNIC_ForJSON() {
        return CO_NEGO_SUCU_SERV_UNIC;
    }

    @JsonProperty("CO_NEGO_SUCU")
    public Integer getCO_NEGO_SUCU_ForJSON() {
        return CO_NEGO_SUCU;
    }

    @JsonProperty("ST_SOAR_INST")
    public Boolean getST_SOAR_INST_ForJSON() {
        return ST_SOAR_INST;
    }
    
    @JsonProperty("CO_OIT_INST")
    public Integer getCO_OIT_INST_ForJSON() {
        return CO_OIT_INST;
    }

    @JsonProperty("SERV_UNIC")
    public SERV_UNIC getServ_Unic_ForJSON() {
        return serv_unic;
    }
    
    @JsonProperty("CO_SERV_UNIC")
    public Integer getCO_SERV_UNIC_ForJSON() {
        return CO_SERV_UNIC;
    }

    @JsonProperty("NO_SERV_UNIC")
    public String getNO_SERV_UNIC_ForJSON() {
        return NO_SERV_UNIC;
    }

    @JsonProperty("IM_MONTO")
    public Double getIM_MONTO_ForJSON() {
        return IM_MONTO;
    }

    @JsonProperty("ST_AFEC_DETR")
    public Boolean getST_AFEC_DETR_ForJSON() {
        return ST_AFEC_DETR;
    }

    @JsonProperty("ST_ELIM")
    public Boolean getST_ELIM_ForJSON() {
        return ST_ELIM;
    }


    @JsonProperty("CO_CIER_COBR")
    public Integer getCO_CIER_ForJSON() {
        return CO_CIER_COBR;
    }

    public void setCO_NEGO_SUCU_SERV_UNIC(Integer CO_NEGO_SUCU_SERV_UNIC) {
        this.CO_NEGO_SUCU_SERV_UNIC = CO_NEGO_SUCU_SERV_UNIC;
    }

    public void setCO_NEGO_SUCU(Integer CO_NEGO_SUCU) {
        this.CO_NEGO_SUCU = CO_NEGO_SUCU;
    }

    public void setST_SOAR_INST(Boolean ST_SOAR_INST) {
        this.ST_SOAR_INST = ST_SOAR_INST;
    }

    public void setCO_OIT_INST(Integer CO_OIT_INST) {
        this.CO_OIT_INST = CO_OIT_INST;
    }

    public void setCO_SERV_UNIC(Integer CO_SERV_UNIC) {
        this.CO_SERV_UNIC = CO_SERV_UNIC;
    }

    public void setNO_SERV_UNIC(String NO_SERV_UNIC) {
        this.NO_SERV_UNIC = NO_SERV_UNIC;
    }

    public void setIM_MONTO(Double IM_MONTO) {
        this.IM_MONTO = IM_MONTO;
    }

    public void setST_AFEC_DETR(Boolean ST_AFEC_DETR) {
        this.ST_AFEC_DETR = ST_AFEC_DETR;
    }

    public void setST_ELIM(Boolean ST_ELIM) {
        this.ST_ELIM = ST_ELIM;
    }

    public void setCO_CIER_COBR(Integer CO_CIER_COBR) {
        this.CO_CIER_COBR = CO_CIER_COBR;
    }

    @JsonIgnore
    public String isValid() {
        /*
        Verifica antes de insertar o modificar
        */
        if (this.CO_SERV_UNIC==null)
            return "No se puede ingresar si no tiene asignado un servicio unico valido.";
        if (this.CO_NEGO_SUCU==null)
            return "No tiene una sucursal asignada el servicio unico";
        SERV_UNIC tmpSU=mapServ_Unic.getId(this.CO_SERV_UNIC);
        if (tmpSU!=null){
            NEGO_SUCU nego_sucu=mapNego_Sucu.getId(this.CO_NEGO_SUCU);
            NEGO nego=nego_sucu.getNEGO();
            if (! tmpSU.getCO_PROD_ForJSON().equals(nego.getCO_PROD_ForJSON())){
                return "El producto del servicio unico no coincide con la del negocio.";
            }
        }else{
            return "El servicio unico no es valido.";
        }
        
        return null;        
    }

    @JsonIgnore
    public void saveCierCobr() {
        mapNego_Sucu_Serv_Unic.saveCierCobr(this);
    }
    
    
    
    
}
