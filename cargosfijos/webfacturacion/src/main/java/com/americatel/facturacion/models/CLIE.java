/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.models;

import com.americatel.facturacion.Utils.Constante;
import com.americatel.facturacion.historial.Historial;
import com.americatel.facturacion.mappers.mapCLIE;
import com.americatel.facturacion.mappers.mapCONT_CLIE;
import com.americatel.facturacion.mappers.mapTIPO_DOCU;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author crodas
 * 
 * Tipo Facturacion (Adelantado o vencido)
 */
@Component
public class CLIE extends Historial {
    private static mapCLIE mapClie=null;   
    private static mapTIPO_DOCU mapTipo_Docu=null;
    private static mapCONT_CLIE mapCont_Clie=null;  
    
    private int CO_CLIE;
    private String NO_RAZO;
    private String NO_CLIE;
    private String AP_CLIE;
    private int CO_TIPO_CLIE;
    private int CO_TIPO_DOCU;            
    private String DE_NUME_DOCU;
    private String DE_CODI_BUM;
    private String DE_DIGI_BUM;
    private TIPO_DOCU tipo_docu;
    private Integer CO_SUCU_FISC;
    private SUCU sucu_fisc;
    private Integer CO_CONT_CLIE_REPR_LEGA;// contacto representante legal
    private CONT_CLIE cont_clie_repre_lega;
    private Boolean ST_ELIM=false;
    
    private String DE_EJEC;
    private String DE_SUB_GERE;
    private String DE_SEGM;
    
    @Autowired
    public void setmapCONT_CLIE(mapCONT_CLIE mapCont_Clie){
        CLIE.mapCont_Clie=mapCont_Clie;
    }
    @Autowired
    public void setmapCLIE(mapCLIE mapClie){
        CLIE.mapClie=mapClie;
    }       
    @Autowired
    public void setmapTIPO_DOCU(mapTIPO_DOCU mapTipo_Docu){
        CLIE.mapTipo_Docu=mapTipo_Docu;
    }     
    /*GET SET*/
    
    @JsonProperty("ST_ELIM")
    public Boolean getST_ELIM_ForJSON() {
        return ST_ELIM;
    }      
    
    @JsonProperty("CO_CLIE")
    public int getCO_CLIE_ForJSON() {
        return CO_CLIE;
    }
    
    @JsonProperty("NO_RAZO")
    public String getNO_RAZO_ForJSON() {
        return NO_RAZO;
    }
    
    @JsonProperty("DE_CODI_BUM")
    public String getDE_CODI_BUM_ForJSON() {
        return DE_CODI_BUM;
    }
    
    @JsonProperty("DE_DIGI_BUM")
    public String getDE_DIGI_BUM_ForJSON() {
        return DE_DIGI_BUM;
    }    
    
    @JsonProperty("NO_CLIE")
    public String getNO_CLIE_ForJSON() {
        return NO_CLIE;
    }
    
    @JsonProperty("CO_SUCU_FISC")
    public Integer getCO_SUCU_FISC_ForJSON() {
        return CO_SUCU_FISC;
    }    
    
    @JsonProperty("CO_CONT_CLIE_REPR_LEGA")
    public Integer getCO_CONT_CLIE_REPR_LEGA_ForJSON() {
        return CO_CONT_CLIE_REPR_LEGA;
    }   
    
    @JsonProperty("CONT_CLIE_REPR_LEGA")
    public CONT_CLIE getCONT_CLIE_REPR_LEGA_ForJSON() {
        return cont_clie_repre_lega;
    }  
    
    
    @JsonProperty("AP_CLIE")
    public String getAP_CLIE_ForJSON() {
        return AP_CLIE;
    }    

    @JsonProperty("CO_TIPO_CLIE")
    public int getCO_TIPO_CLIE_ForJSON() {
        return CO_TIPO_CLIE;
    }    
    
    @JsonProperty("CO_TIPO_DOCU")
    public int getCO_TIPO_DOCU_ForJSON() {
        return CO_TIPO_DOCU;
    }
    
    @JsonProperty("TIPO_DOCU")
    public TIPO_DOCU getTipo_docu_ForJSON() {
        return tipo_docu;
    }
    
    @JsonProperty("DE_NUME_DOCU")
    public String getDE_NUME_DOCU_ForJSON() {
        return DE_NUME_DOCU;
    }

    @JsonProperty("DE_EJEC")
    public String getDE_EJEC_ForJSON() {
        return DE_EJEC;
    }
    
    @JsonProperty("DE_SUB_GERE")
    public String getDE_SUB_GERE_ForJSON() {
        return DE_SUB_GERE;
    }
    
    @JsonProperty("DE_SEGM")
    public String getDE_SEGM_ForJSON() {
        return DE_SEGM;
    }  
    
    @JsonProperty("SUCU_FISC")
    public SUCU getSUCU_FISC_ForJSON() {
        return this.sucu_fisc;
    }     

    public void setCO_CLIE(int CO_CLIE) {
        this.CO_CLIE = CO_CLIE;
    }

    public void setNO_RAZO(String NO_RAZO) {
        this.NO_RAZO = NO_RAZO;
    }
    
    public void setDE_CODI_BUM(String DE_CODI_BUM) {
        this.DE_CODI_BUM = DE_CODI_BUM;
    }
    
    public void setDE_DIGI_BUM(String DE_DIGI_BUM) {
        this.DE_DIGI_BUM = DE_DIGI_BUM;
    }    
    
    public void setNO_CLIE(String NO_CLIE) {
        this.NO_CLIE = NO_CLIE;
    }
    
    public void setAP_CLIE(String AP_CLIE) {
        this.AP_CLIE = AP_CLIE;
    }
    
    public void setCO_TIPO_CLIE(int CO_TIPO_CLIE) {
        this.CO_TIPO_CLIE = CO_TIPO_CLIE;
    }  
    
    public void setCO_TIPO_DOCU(int CO_TIPO_DOCU) {
        this.CO_TIPO_DOCU = CO_TIPO_DOCU;
    }
    
    public void setDE_NUME_DOCU(String DE_NUME_DOCU) {
        this.DE_NUME_DOCU = DE_NUME_DOCU;
    }
    public void setST_ELIM(Boolean ST_ELIM) {
        this.ST_ELIM = ST_ELIM;
    }
    
    public void setCO_SUCU_FISC(Integer CO_SUCU_FISC) {
        this.CO_SUCU_FISC = CO_SUCU_FISC;
    } 
    
    public void setCO_CONT_CLIE_REPR_LEGA(Integer CO_CONT_CLIE_REPR_LEGA) {
        this.CO_CONT_CLIE_REPR_LEGA = CO_CONT_CLIE_REPR_LEGA;
    }    
    
    public void setDE_EJEC(String DE_EJEC) {
        this.DE_EJEC = DE_EJEC;
    } 

    public void setDE_SUB_GERE(String DE_SUB_GERE) {
        this.DE_SUB_GERE = DE_SUB_GERE;
    } 

    public void setDE_SEGM(String DE_SEGM) {
        this.DE_SEGM = DE_SEGM;
    } 
    
    
    @JsonIgnore
    public SUCU getSucursalFiscal() {
        SUCU sucu=mapClie.getSucursalFiscal(this);        
        if (sucu==null){
            sucu=mapClie.getSucursalFiscalPrimero(this);
        }
        return sucu;
    }
    @JsonIgnore
    public String getFullNameCliente() {
        if (this.NO_RAZO!=null)
            if (!this.NO_RAZO.isEmpty())
                return this.NO_RAZO.trim();
            else 
                return (this.AP_CLIE+" "+this.NO_CLIE).trim();
        else
            return (this.AP_CLIE+" "+this.NO_CLIE).trim();
    }
    
    @JsonIgnore
    public TIPO_DOCU getTipoDocumento(){
        return mapTipo_Docu.getId(this.CO_TIPO_DOCU);
    }

    @JsonIgnore
    public String getCodigoCliente() {
        if (this.DE_CODI_BUM!=null && this.DE_DIGI_BUM !=null)
            return this.DE_CODI_BUM+"-"+this.DE_DIGI_BUM;
        return "";
    }
    
    @JsonIgnore
    public List<CLIE> getAllDetalleClientes() {
        return mapClie.getAllDetalle();
    }
    
    @JsonIgnore
    public List<CONT_CLIE> getContactos() {
        return mapCont_Clie.selectByCLIE(this.CO_CLIE);
    }
    
    
    @JsonIgnore
    public List<CLIE> getAllDetalleClientesByServicio(Integer COD_PROD_PADRE, Integer COD_SERVICIO) {
    	if(COD_SERVICIO == 0){
    		return mapClie.getAllDetalleByProducto(COD_PROD_PADRE);
    	}else{
    		return mapClie.getAllDetalleByServicio( COD_PROD_PADRE,  COD_SERVICIO);
    	}
    }
    
    @JsonIgnore
    public String getAbrevTipoDoc() {
        return Constante.abreviaturaTipoDocumento.get(this.CO_TIPO_DOCU);
    }
}
