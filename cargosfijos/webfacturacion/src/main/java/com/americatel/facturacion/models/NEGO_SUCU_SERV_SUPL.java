/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.models;

import com.americatel.facturacion.fncs.fnc;
import com.americatel.facturacion.historial.Historial;
import com.americatel.facturacion.mappers.mapCIER_DATA_SERV_SUPL;
import com.americatel.facturacion.mappers.mapCORT;
import com.americatel.facturacion.mappers.mapMODU;
import com.americatel.facturacion.mappers.mapNEGO_SUCU;
import com.americatel.facturacion.mappers.mapNEGO_SUCU_SERV_SUPL;
import com.americatel.facturacion.mappers.mapSERV_SUPL;
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
public class NEGO_SUCU_SERV_SUPL  extends Historial {
    //Autowired
    private static mapNEGO_SUCU_SERV_SUPL mapNego_Sucu_Serv_Supl=null;
    private static mapSERV_SUPL mapServ_Supl=null;
    private static mapNEGO_SUCU mapNego_Sucu=null;
    private Boolean ST_ELIM=false;
    
    private Integer CO_NEGO_SUCU_SERV_SUPL;
    private Integer CO_NEGO_SUCU;
    private Boolean ST_SOAR_INST=false;
    private Integer CO_OIT_INST;
    private Boolean ST_SOAR_DESI=false;
    private Integer CO_OIT_DESI;    
    private Integer CO_SERV_SUPL;
    private String NO_SERV_SUPL;
    @DateTimeFormat(pattern =  fnc.FORMAT_DATE_TIME_SEPARATOR )//"yyyy-MM-dd'T'HH:mm:ss")
    private Date FE_INIC;
    @DateTimeFormat(pattern = fnc.FORMAT_DATE_TIME_SEPARATOR )//"yyyy-MM-dd'T'HH:mm:ss")
    private Date FE_FIN;
    @DateTimeFormat(pattern = fnc.FORMAT_DATE_TIME_SEPARATOR )//"yyyy-MM-dd'T'HH:mm:ss")
    private Date FE_ULTI_FACT; 
    @DateTimeFormat(pattern = fnc.FORMAT_DATE_TIME_SEPARATOR )//"yyyy-MM-dd'T'HH:mm:ss")
    private Date FE_ULTI_FACT_SGTE;    
    private Double IM_MONTO;
    private Boolean ST_AFEC_DETR=false;
    private Double IM_SALD_APROX;
    
    private Integer CO_NEGO_SUCU_SERV_SUPL_CESI_CONT_PADR=null;//quien lo origino
    private Integer CO_NEGO_SUCU_SERV_SUPL_CESI_CONT_HIJO=null;//que origino
    private Integer CO_NEGO_SUCU_SERV_SUPL_MUDA_PADR=null;//quien lo origino
    private Integer CO_NEGO_SUCU_SERV_SUPL_MUDA_HIJO=null;//que origino
    
    private SERV_SUPL serv_supl;
    
    private static mapCIER_DATA_SERV_SUPL mapCier_Data_Serv_Supl=null;
    
    @Autowired
    public void setmapSERV_SUPL(mapSERV_SUPL mapServ_Supl){
        NEGO_SUCU_SERV_SUPL.mapServ_Supl=mapServ_Supl;        
    }    
    @Autowired
    public void setmapNEGO_SUCU(mapNEGO_SUCU mapNego_Sucu){
        NEGO_SUCU_SERV_SUPL.mapNego_Sucu=mapNego_Sucu;        
    } 
    @Autowired
    public void setmapNEGO_SUCU_SERV_SUPL(mapNEGO_SUCU_SERV_SUPL mapNego_Sucu_Serv_Supl){
        NEGO_SUCU_SERV_SUPL.mapNego_Sucu_Serv_Supl=mapNego_Sucu_Serv_Supl;        
    }  
    @Autowired
    public void setmapCIER_DATA_SERV_SUPL(mapCIER_DATA_SERV_SUPL mapCier_Data_Serv_Supl){
        NEGO_SUCU_SERV_SUPL.mapCier_Data_Serv_Supl=mapCier_Data_Serv_Supl;        
    }

    /*GEST SETS*/
    
    @JsonProperty("ST_ELIM")
    public Boolean getST_ELIM_ForJSON() {
        return ST_ELIM;
    }      
    
    @JsonProperty(value = "ST_SOAR_INST")
    public Boolean getST_SOAR_INST_ForJSON() {    
        return ST_SOAR_INST;
    } 
    
    @JsonProperty(value = "CO_OIT_INST")
    public Integer getCO_OIT_INST_ForJSON() {
        return CO_OIT_INST;
    }
    @JsonProperty(value = "ST_SOAR_DESI")
    public Boolean getST_SOAR_DESI_ForJSON() {    
        return ST_SOAR_DESI;
    } 
    
    @JsonProperty(value = "CO_OIT_DESI")
    public Integer getCO_OIT_DESI_ForJSON() {
        return CO_OIT_DESI;
    }
    @JsonProperty(value = "ST_AFEC_DETR")
    public Boolean getST_AFEC_DETR_ForJSON() {    
        return ST_AFEC_DETR;
    } 
    
    @JsonProperty("SERV_SUPL")
    public SERV_SUPL getServ_Supl_ForJSON() {
        return serv_supl;
    }
    
    @JsonProperty(value = "NO_SERV_SUPL")
    public String getNO_SERV_SUPL_ForJSON() {
        return NO_SERV_SUPL;
    }

    @JsonProperty(value = "CO_NEGO_SUCU_SERV_SUPL")
    public Integer getCO_NEGO_SUCU_SERV_SUPL_ForJSON() {
        return CO_NEGO_SUCU_SERV_SUPL;
    }
    
    @JsonProperty("CO_NEGO_SUCU")
    public Integer getCO_NEGO_SUCU_ForJSON() {
        return CO_NEGO_SUCU;
    }

    @JsonProperty("CO_SERV_SUPL")
    public Integer getCO_SERV_SUPL_ForJSON() {
        return CO_SERV_SUPL;
    }

    @JsonProperty("FE_INIC")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = fnc.FORMAT_DATE)// "yyyy/MM/dd")
    public Date getFE_INIC_ForJSON() {
        return FE_INIC;
    }

    @JsonProperty("FE_FIN")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = fnc.FORMAT_DATE)//"yyyy/MM/dd")
    public Date getFE_FIN_ForJSON() {
        return FE_FIN;
    }    
    

    @JsonProperty("FE_ULTI_FACT")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = fnc.FORMAT_DATE)//"yyyy/MM/dd")
    public Date getFE_ULTI_FACT_ForJSON() {
        return FE_ULTI_FACT;
    }
    
    @JsonProperty("FE_ULTI_FACT_SGTE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = fnc.FORMAT_DATE)//"yyyy/MM/dd")
    public Date getFE_ULTI_FACT_SGTE_ForJSON() {
        return FE_ULTI_FACT_SGTE;
    }    
    

    @JsonProperty("IM_MONTO")
    public Double getIM_MONTO_ForJSON() {
        return IM_MONTO;
    }
    
    @JsonProperty("IM_SALD_APROX")
    public Double getIM_SALD_APROX_ForJSON() {
        return IM_SALD_APROX;
    }

    @JsonProperty(value = "CO_NEGO_SUCU_SERV_SUPL_CESI_CONT_PADR")
    public Integer getCO_NEGO_SUCU_SERV_SUPL_CESI_CONT_PADR_ForJSON() {
        return CO_NEGO_SUCU_SERV_SUPL_CESI_CONT_PADR;
    }      

    @JsonProperty(value = "CO_NEGO_SUCU_SERV_SUPL_CESI_CONT_HIJO")
    public Integer getCO_NEGO_SUCU_SERV_SUPL_CESI_CONT_HIJO_ForJSON() {
        return CO_NEGO_SUCU_SERV_SUPL_CESI_CONT_HIJO;
    }
    
    @JsonProperty(value = "CO_NEGO_SUCU_SERV_SUPL_MUDA_PADR")
    public Integer getCO_NEGO_SUCU_SERV_SUPL_MUDA_PADR_ForJSON() {
        return CO_NEGO_SUCU_SERV_SUPL_MUDA_PADR;
    }      

    @JsonProperty(value = "CO_NEGO_SUCU_SERV_SUPL_MUDA_HIJO")
    public Integer getCO_NEGO_SUCU_SERV_SUPL_MUDA_HIJO_ForJSON() {
        return CO_NEGO_SUCU_SERV_SUPL_MUDA_HIJO;
    }
    
    public void setNO_SERV_SUPL(String NO_SERV_SUPL) {
        this.NO_SERV_SUPL = NO_SERV_SUPL;
    }
    
    public void setCO_NEGO_SUCU_SERV_SUPL(Integer CO_NEGO_SUCU_SERV_SUPL) {
        this.CO_NEGO_SUCU_SERV_SUPL = CO_NEGO_SUCU_SERV_SUPL;
    }

    public void setCO_NEGO_SUCU(Integer CO_NEGO_SUCU) {
        this.CO_NEGO_SUCU = CO_NEGO_SUCU;
    }

    public void setFE_INIC(Date FE_INIC) {
        this.FE_INIC = FE_INIC;
    }

    public void setFE_FIN(Date FE_FIN) {
        this.FE_FIN = FE_FIN;
    }

    public void setFE_ULTI_FACT(Date FE_ULTI_FACT) {
        this.FE_ULTI_FACT = FE_ULTI_FACT;
    }
    
    public void setFE_ULTI_FACT_SGTE(Date FE_ULTI_FACT_SGTE) {
        this.FE_ULTI_FACT_SGTE = FE_ULTI_FACT_SGTE;
    }    

    public void setIM_MONTO(Double IM_MONTO) {
        this.IM_MONTO = IM_MONTO;
    }

    public void setCO_SERV_SUPL(Integer CO_SERV_SUPL) {
        this.CO_SERV_SUPL = CO_SERV_SUPL;
    }

    public void setST_AFEC_DETR(Boolean ST_AFEC_DETR) {
        this.ST_AFEC_DETR = ST_AFEC_DETR;
    }


    public void setST_SOAR_INST(Boolean ST_SOAR_INST) {
        this.ST_SOAR_INST = ST_SOAR_INST;
    }

    public void setCO_OIT_INST(Integer CO_OIT_INST) {
        this.CO_OIT_INST = CO_OIT_INST;
    }

    public void setST_SOAR_DESI(Boolean ST_SOAR_DESI) {
        this.ST_SOAR_DESI = ST_SOAR_DESI;
    }

    public void setCO_OIT_DESI(Integer CO_OIT_DESI) {
        this.CO_OIT_DESI = CO_OIT_DESI;
    }
    
    public void setST_ELIM(Boolean ST_ELIM) {
        this.ST_ELIM = ST_ELIM;
    } 
    
    public void setIM_SALD_APROX(Double IM_SALD_APROX) {
        this.IM_SALD_APROX = IM_SALD_APROX;
    }
    
    public void setCO_NEGO_SUCU_SERV_SUPL_CESI_CONT_PADR(Integer CO_NEGO_SUCU_SERV_SUPL_CESI_CONT_PADR) {
        this.CO_NEGO_SUCU_SERV_SUPL_CESI_CONT_PADR = CO_NEGO_SUCU_SERV_SUPL_CESI_CONT_PADR;
    }

    public void setCO_NEGO_SUCU_SERV_SUPL_CESI_CONT_HIJO(Integer CO_NEGO_SUCU_SERV_SUPL_CESI_CONT_HIJO) {
        this.CO_NEGO_SUCU_SERV_SUPL_CESI_CONT_HIJO = CO_NEGO_SUCU_SERV_SUPL_CESI_CONT_HIJO;
    }
    
    public void setCO_NEGO_SUCU_SERV_SUPL_MUDA_PADR(Integer CO_NEGO_SUCU_SERV_SUPL_MUDA_PADR) {
        this.CO_NEGO_SUCU_SERV_SUPL_MUDA_PADR = CO_NEGO_SUCU_SERV_SUPL_MUDA_PADR;
    }

    public void setCO_NEGO_SUCU_SERV_SUPL_MUDA_HIJO(Integer CO_NEGO_SUCU_SERV_SUPL_MUDA_HIJO) {
        this.CO_NEGO_SUCU_SERV_SUPL_MUDA_HIJO = CO_NEGO_SUCU_SERV_SUPL_MUDA_HIJO;
    }
    
    @JsonIgnore
    public String isValid() {
        /*
        Verifica antes de insertar o modificar
        */
        if (this.CO_SERV_SUPL==null)
            return "No se puede ingresar si no tiene asignado un servicio suplementario valido.";
        if (this.CO_NEGO_SUCU==null)
            return "No tiene una sucursal asignada el servicio suplementario";
        SERV_SUPL tmpSS=mapServ_Supl.getId(this.CO_SERV_SUPL);
        if (tmpSS!=null){
            NEGO_SUCU nego_sucu=mapNego_Sucu.getId(this.CO_NEGO_SUCU);
            NEGO nego=nego_sucu.getNEGO();
            if (! tmpSS.getCO_PROD_ForJSON().equals(nego.getCO_PROD_ForJSON())){
                return "El producto del servicio suplementario no coincide con la del negocio.";
            }
        }else{
            return "El servicio suplementario no es valido.";
        }
        
        //validacion de fechas
        if (this.getFE_INIC_ForJSON()==null)
            return "El servicio suplementario no tiene fecha de activaci\u00f3n.";
        
        if (this.getFE_FIN_ForJSON()!=null)
            if (this.getFE_INIC_ForJSON().getTime()>this.getFE_FIN_ForJSON().getTime())
                return "La fecha de desactivaci\u00f3n tiene que ser mayor a la fecha de activac\u00f3n del servicio suplementario";
        
        return null;
    }

    @JsonIgnore
    public void saveSiguienteUltimaFacturacion(Date sgte) {
        this.FE_ULTI_FACT_SGTE=sgte;
        mapNego_Sucu_Serv_Supl.saveSiguienteUltimaFacturacion(this);
    }
    
    @JsonIgnore
    public void updateSaldoAproximado() {
        mapNego_Sucu_Serv_Supl.updateSaldoAproximado(this);
    }
    
    @JsonIgnore
    public List<CIER_DATA_SERV_SUPL> getCIER_DATA_SERVICIOS_SUPL_COBRADOS(CIER cier) {
        return mapCier_Data_Serv_Supl.getCIER_DATA_SERVICIOS_SUPL_COBRADOS(this, cier);
    }

    @JsonIgnore
    public List<CIER_DATA_SERV_SUPL> getCIER_DATA_DEVOLUCIONES_POR_PROMOCIONES(CIER cier) {
        return mapCier_Data_Serv_Supl.getCIER_DATA_DEVOLUCIONES_POR_PROMOCIONES(this,cier);
    }

    @JsonIgnore
    public List<CIER_DATA_SERV_SUPL> getCIER_DATA_MI_SERVICIOS_SUPL_COBRADOS(CIER cier) {
        return mapCier_Data_Serv_Supl.getCIER_DATA_MI_SERVICIOS_SUPL_COBRADOS(this,cier);
    }
    
    @JsonIgnore
    public NEGO_SUCU_SERV_SUPL getById(Integer codigo) {
        return mapNego_Sucu_Serv_Supl.getId(codigo);
    }
}

