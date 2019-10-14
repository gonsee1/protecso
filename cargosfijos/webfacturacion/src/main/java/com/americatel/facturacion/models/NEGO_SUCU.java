/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.models;

import com.americatel.facturacion.fncs.fnc;
import com.americatel.facturacion.historial.Historial;
import com.americatel.facturacion.mappers.mapMOTI_DESC;
import com.americatel.facturacion.mappers.mapNEGO;
import com.americatel.facturacion.mappers.mapNEGO_SUCU;
import com.americatel.facturacion.mappers.mapNEGO_SUCU_PLAN;
import com.americatel.facturacion.mappers.mapNEGO_SUCU_PROM;
import com.americatel.facturacion.mappers.mapNEGO_SUCU_SERV_SUPL;
import com.americatel.facturacion.mappers.mapNEGO_SUCU_SERV_UNIC;
import com.americatel.facturacion.mappers.mapSUCU;
import com.americatel.facturacion.mappers.mapSUSP;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

/**
 *
 * @author crodas
 * 
 * Productos
 */
@Component
public class NEGO_SUCU  extends Historial {
    //Autowired
    private static mapNEGO_SUCU mapNego_Sucu=null;
    private static mapNEGO_SUCU_PLAN mapNego_Sucu_Plan=null;
    private static mapNEGO_SUCU_SERV_SUPL mapNego_Sucu_Serv_Supl=null;
    private static mapNEGO_SUCU_SERV_UNIC mapNego_Sucu_Serv_Unic=null;
    private static mapSUCU mapSucu=null;
    private static mapNEGO mapNego=null;
    private static mapSUSP mapSusp=null;
    private static mapNEGO_SUCU_PROM mapNego_Sucu_Prom=null;
    private static mapMOTI_DESC mapMoti_Desc=null;
    private Boolean ST_ELIM=false;
    
    private Integer CO_NEGO_SUCU;
    private Integer CO_NEGO;
    private Integer CO_SUCU;
    @DateTimeFormat(pattern = fnc.FORMAT_DATE_TIME_SEPARATOR)//"yyyy-MM-dd'T'HH:mm:ss")//mmss
    private Date FE_INIC;
    @DateTimeFormat(pattern =  fnc.FORMAT_DATE_TIME_SEPARATOR)//"yyyy-MM-dd'T'HH:mm:ss")//como lo lee
    private Date FE_FIN;
    private Boolean ST_SOAR_INST=false;
    private Integer CO_OIT_INST;
    private Boolean ST_SOAR_DESI=false;
    private Integer CO_OIT_DESI;    
    private Integer CO_CIRC;
    
    
    private Integer CO_NEGO_SUCU_CESI_CONT_PADR=null;//quien lo origino
    private Integer CO_NEGO_SUCU_CESI_CONT_HIJO=null;//que origino
    
    private Integer CO_NEGO_SUCU_MUDA_PADR=null;//quien lo origino
    private Integer CO_NEGO_SUCU_MUDA_HIJO=null;//que origino
    
    private String DE_ORDE_SERV;
    private String DE_PLAZ_CONT;    
    private String REFERENCIA_NEGO_SUCU;// EMANUEL BARBARAN - EFITEC (SE AGREGO VARIABLES)
    
    private Integer CO_MOTI_DESC;
    private String DE_INFO_DESC;
    private String DE_OBSERV;
    

    
    private Boolean isSuspendido;
    private Boolean isMudado;
    private Boolean isCesionContractual;
    private Boolean isDesactivado;
    
    private NEGO nego;
    private SUCU sucu;
    private MOTI_DESC moti_desc;
    
    //variables no BD
    //variable para obtener cuantas promociones pendientes tiene un negocio
    private List<NEGO_SUCU_PROM> promociones_pendientes=null;
    private List<NEGO_SUCU_PROM> promociones_porcentaje_pendientes=null;
    private List<NEGO_SUCU_PROM> promociones_monto_pendientes=null;    
    
    @Autowired
    public void setmapMOTI_DESC(mapMOTI_DESC mapMoti_Desc){
        NEGO_SUCU.mapMoti_Desc=mapMoti_Desc;
    } 
    
    @Autowired
    public void setmapSUSP(mapSUSP mapSusp){
        NEGO_SUCU.mapSusp=mapSusp;        
    }

    @Autowired
    public void setmapNEGO(mapNEGO mapNego){
        NEGO_SUCU.mapNego=mapNego;        
    }
    
    @Autowired
    public void setmapNEGO_SUCU(mapNEGO_SUCU mapNego_Sucu){
        NEGO_SUCU.mapNego_Sucu=mapNego_Sucu;        
    }

    @Autowired
    public void setmapNEGO_SUCU_PLAN(mapNEGO_SUCU_PLAN mapNego_Sucu_Plan){
        NEGO_SUCU.mapNego_Sucu_Plan=mapNego_Sucu_Plan;        
    }  
    
    @Autowired
    public void setmapNEGO_SUCU_SERV_SUPL(mapNEGO_SUCU_SERV_SUPL mapNego_Sucu_Serv_Supl){
        NEGO_SUCU.mapNego_Sucu_Serv_Supl=mapNego_Sucu_Serv_Supl;        
    } 
    
    @Autowired
    public void setmapNEGO_SUCU_SERV_UNIC(mapNEGO_SUCU_SERV_UNIC mapNego_Sucu_Serv_Unic){
        NEGO_SUCU.mapNego_Sucu_Serv_Unic=mapNego_Sucu_Serv_Unic;        
    } 
    
    
    @Autowired
    public void setmapSUCU(mapSUCU mapSucu){
        NEGO_SUCU.mapSucu=mapSucu;        
    }   
    
    @Autowired
    public void setmapNEGO_SUCU_PROM(mapNEGO_SUCU_PROM mapNego_Sucu_Prom){
        NEGO_SUCU.mapNego_Sucu_Prom=mapNego_Sucu_Prom;        
    }      
    
    /*GETS y SETS*/
    @JsonProperty(value = "isSuspendido")
    public Boolean isSuspendido_ForJSON() {
        return isSuspendido;
    }      
    
    @JsonProperty(value = "isMudado")
    public Boolean isMudado_ForJSON() {
        return isMudado;
    } 
    
    @JsonProperty(value = "isCesionContractual")
    public Boolean isCesionContractual_ForJSON() {
        return isCesionContractual;
    } 
    
    @JsonProperty("isDesactivado")
    public Boolean isDesactivado_ForJSON() {
        return isDesactivado;
    }
            
    @JsonProperty("MOTI_DESC")
    public MOTI_DESC getMOTI_DESC_ForJSON() {
        return this.moti_desc;
    }
    
    @JsonProperty(value = "CO_NEGO_SUCU_CESI_CONT_PADR")
    public Integer getCO_NEGO_SUCU_CESI_CONT_PADR_ForJSON() {
        return CO_NEGO_SUCU_CESI_CONT_PADR;
    }      

    @JsonProperty(value = "CO_NEGO_SUCU_CESI_CONT_HIJO")
    public Integer getCO_NEGO_SUCU_CESI_CONT_HIJO_ForJSON() {
        return CO_NEGO_SUCU_CESI_CONT_HIJO;
    }
    
    @JsonProperty(value = "CO_NEGO_SUCU_MUDA_PADR")
    public Integer getCO_NEGO_SUCU_MUDA_PADR_ForJSON() {
        return CO_NEGO_SUCU_MUDA_PADR;
    }      

    @JsonProperty(value = "CO_NEGO_SUCU_MUDA_HIJO")
    public Integer getCO_NEGO_SUCU_MUDA_HIJO_ForJSON() {
        return CO_NEGO_SUCU_MUDA_HIJO;
    }

    @JsonProperty(value = "ST_ELIM")
    public Boolean getST_ELIM_ForJSON() {
        return ST_ELIM;
    }      
    
    @JsonProperty("DE_ORDE_SERV")
    public String getDE_ORDE_SERV_ForJSON() {
        return DE_ORDE_SERV;
    }

    @JsonProperty("DE_PLAZ_CONT")
    public String getDE_PLAZ_CONT_ForJSON() {
        return DE_PLAZ_CONT;
    }  
    
    @JsonProperty("REFERENCIA_NEGO_SUCU")
    public String getREFERENCIA_NEGO_SUCU_ForJSON() {
        return REFERENCIA_NEGO_SUCU;
    }
    
    @JsonProperty("CO_NEGO_SUCU")
    public Integer getCO_NEGO_SUCU_ForJSON() {
        return CO_NEGO_SUCU;
    }

    @JsonProperty("CO_NEGO")    
    public Integer getCO_NEGO_ForJSON() {
        return CO_NEGO;
    }

    @JsonProperty("CO_SUCU")
    public Integer getCO_SUCU_ForJSON() {
        return CO_SUCU;
    }

    @JsonProperty("FE_INIC")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = fnc.FORMAT_DATE)// "yyyy-MM-dd")
    public Date getFE_INIC_ForJSON() {
        return FE_INIC;
    }

    @JsonProperty("FE_FIN")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = fnc.FORMAT_DATE)//"yyyy-MM-dd")
    public Date getFE_FIN_ForJSON() {
        return FE_FIN;
    }

    @JsonProperty("ST_SOAR_INST")
    public Boolean isST_SOAR_INST_ForJSON() {
        return ST_SOAR_INST;
    }

    @JsonProperty("CO_OIT_INST")
    public Integer getCO_OIT_INST_ForJSON() {
        return CO_OIT_INST;
    }

    @JsonProperty("ST_SOAR_DESI")
    public Boolean isST_SOAR_DESI_ForJSON() {
        return ST_SOAR_DESI;
    }

    @JsonProperty("CO_OIT_DESI")
    public Integer getCO_OIT_DESI_ForJSON() {
        return CO_OIT_DESI;
    }    
    
    @JsonProperty("CO_CIRC")
    public Integer getCO_CIRC_ForJSON() {
        return CO_CIRC;
    }

    @JsonProperty("NEGO")
    public NEGO getNego_ForJSON() {
        return nego;
    }

    @JsonProperty("SUCU")
    public SUCU getSucu_ForJSON() {
        return sucu;
    }

    @JsonProperty("CO_MOTI_DESC")
    public Integer getCO_MOTI_DESC_ForJSON() {
        return CO_MOTI_DESC;
    }
    
    @JsonProperty("DE_INFO_DESC")
    public String getDE_INFO_DESC_ForJSON() {
        return DE_INFO_DESC;
    }
    
    @JsonProperty("DE_OBSERV")
    public String getDE_OBSERV_ForJSON() {
        return DE_OBSERV;
    }

    public void setCO_MOTI_DESC(Integer CO_MOTI_DESC) {
        this.CO_MOTI_DESC = CO_MOTI_DESC;
    }
    
    public void setDE_INFO_DESC(String DE_INFO_DESC) {
        this.DE_INFO_DESC = DE_INFO_DESC;
    }
    
    public void setDE_OBSERV(String DE_OBSERV) {
        this.DE_OBSERV = DE_OBSERV;
    }
    
    public void setCO_NEGO_SUCU(Integer CO_NEGO_SUCU) {
        this.CO_NEGO_SUCU = CO_NEGO_SUCU;
    }

    public void setCO_NEGO(Integer CO_NEGO) {
        this.CO_NEGO = CO_NEGO;
    }

    public void setCO_SUCU(Integer CO_SUCU) {
        this.CO_SUCU = CO_SUCU;
    }

    public void setFE_INIC(Date FE_INIC) {
        this.FE_INIC = FE_INIC;
    }
  
    public void setFE_FIN(Date FE_FIN) {
        this.FE_FIN = FE_FIN;
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
    
    public void setCO_CIRC(Integer CO_CIRC) {
        this.CO_CIRC = CO_CIRC;
    }

    public void setDE_ORDE_SERV(String DE_ORDE_SERV) {
        this.DE_ORDE_SERV = DE_ORDE_SERV;
    }

    public void setDE_PLAZ_CONT(String DE_PLAZ_CONT) {
        this.DE_PLAZ_CONT = DE_PLAZ_CONT;
    }
    
    public void setREFERENCIA_NEGO_SUCU(String REFERENCIA_NEGO_SUCU){
        this.REFERENCIA_NEGO_SUCU = REFERENCIA_NEGO_SUCU;
        
    }
    
    public void setST_ELIM(Boolean ST_ELIM) {
        this.ST_ELIM = ST_ELIM;
    } 

    public void setCO_NEGO_SUCU_CESI_CONT_PADR(Integer CO_NEGO_SUCU_CESI_CONT_PADR) {
        this.CO_NEGO_SUCU_CESI_CONT_PADR = CO_NEGO_SUCU_CESI_CONT_PADR;
    }

    public void setCO_NEGO_SUCU_CESI_CONT_HIJO(Integer CO_NEGO_SUCU_CESI_CONT_HIJO) {
        this.CO_NEGO_SUCU_CESI_CONT_HIJO = CO_NEGO_SUCU_CESI_CONT_HIJO;
    }
    
    public void setCO_NEGO_SUCU_MUDA_PADR(Integer CO_NEGO_SUCU_MUDA_PADR) {
        this.CO_NEGO_SUCU_MUDA_PADR = CO_NEGO_SUCU_MUDA_PADR;
    }

    public void setCO_NEGO_SUCU_MUDA_HIJO(Integer CO_NEGO_SUCU_MUDA_HIJO) {
        this.CO_NEGO_SUCU_MUDA_HIJO = CO_NEGO_SUCU_MUDA_HIJO;
    }
    
   
    
    @JsonIgnore
    public String isValid(){
        //Verifica antes de insertar o modificar si es valido
        //Si retorna null no existe error        
        if (this.CO_OIT_INST!=null && this.CO_OIT_INST<=0){
            this.CO_OIT_INST=null;
            this.ST_SOAR_INST=null;
        }
        if (this.CO_CIRC!=null && this.CO_CIRC<=0)
            this.CO_CIRC=null;
        
        if (this.CO_NEGO_SUCU==null){
            //inserta
            if(this.ST_SOAR_INST!=null && this.CO_OIT_INST!=null){
                NEGO_SUCU nego_sucu=mapNego_Sucu.validByOit(this.ST_SOAR_INST,this.CO_OIT_INST);
                if (nego_sucu!=null)
                    return "La oit ya existe en otra sucursal, negocio "+nego_sucu.getCO_NEGO_ForJSON()+".";
            }
            if (this.CO_CIRC!=null){
                NEGO_SUCU nego_sucu=mapNego_Sucu.getByCircuito(this.CO_CIRC);
                if (nego_sucu!=null)
                    return "El circuito ya existe en otra sucursal, negocio "+nego_sucu.getCO_NEGO_ForJSON()+".";
            }
        }else{
            //modifica
            if(this.ST_SOAR_INST!=null && this.CO_OIT_INST!=null){
                NEGO_SUCU nego_sucu=mapNego_Sucu.validByOit(this.ST_SOAR_INST,this.CO_OIT_INST);
                if (nego_sucu!=null && !nego_sucu.getCO_NEGO_SUCU_ForJSON().equals(this.CO_NEGO_SUCU))
                    return "La oit ya existe en otra sucursal, negocio "+nego_sucu.getCO_NEGO_ForJSON()+".";
            }
            if (this.CO_CIRC!=null){
                NEGO_SUCU nego_sucu=mapNego_Sucu.getByCircuito(this.CO_CIRC);
                if (nego_sucu!=null && !nego_sucu.getCO_NEGO_SUCU_ForJSON().equals(this.CO_NEGO_SUCU))
                    return "El circuito ya existe en otra sucursal, negocio "+nego_sucu.getCO_NEGO_ForJSON()+".";
            }           
        } 
        if (this.ST_SOAR_INST==null && this.CO_OIT_INST==null && this.CO_CIRC==null ){
            return "Se necesita una oit de instalaci\u00f3n o un circuito para el registro de la sucursal.";
        }         
        return null;
    }    
    
    @JsonIgnore
    public List<NEGO_SUCU_PLAN> getPlanes() {
        //Obtienes los planes de la sucursal
        return NEGO_SUCU.mapNego_Sucu_Plan.selectByNEGO_SUCU(this.CO_NEGO_SUCU);
    }
    @JsonIgnore
    public List<NEGO_SUCU_SERV_SUPL> getServiciosSuplementarios() {
        //Obtienes los servicios de la sucursal
        return NEGO_SUCU.mapNego_Sucu_Serv_Supl.selectByNEGO_SUCU(this.CO_NEGO_SUCU);
    } 
    @JsonIgnore
    public List<SUSP> getSuspensionesPendientes_Fact() {
        //lista las suspensiones pendientes por una sucursal
        return NEGO_SUCU.mapSusp.selectByNegoSucuPendientesFactSortByFE_INIC(this.CO_NEGO_SUCU);
    }
    @JsonIgnore
    public List<SUSP> getSuspensionesPendientes() {
        //lista las suspensiones pendientes por una sucursal
        return NEGO_SUCU.mapSusp.selectByNegoSucuPendientesSortByFE_INIC(this.CO_NEGO_SUCU);
    }
    
    @JsonIgnore
    public Boolean isSuspendidoFacturado() {
        //Verifica si la sucursal esta suspendida y ya se facturo
        return NEGO_SUCU.mapSusp.isSuspendidoFacturado(this.CO_NEGO_SUCU);
    }

    @JsonIgnore
    public MOTI_DESC getMOTI_DESC() {
        return mapMoti_Desc.getId(this.CO_MOTI_DESC);
    }
    
    @JsonIgnore
    public SUCU getSUCU() {
        return mapSucu.getId(this.CO_SUCU);
    }
    
    @JsonIgnore
    public NEGO getNEGO() {
        return mapNego.getId(this.CO_NEGO);
    }    
    
    @JsonIgnore
    @Override
    public String toString(){
    	String ret="";
    	ret+="CO_NEGO_SUCU:"+(this.CO_NEGO_SUCU)+",";
    	ret+="CO_NEGO:"+(this.CO_NEGO)+",";
    	ret+="CO_SUCU:"+(this.CO_SUCU)+",";
    	ret+="FE_INIC:"+(this.FE_INIC)+",";
    	ret+="FE_FIN:"+(this.FE_FIN)+",";
    	ret+="ST_SOAR:"+(this.ST_SOAR_INST)+",";
    	ret+="CO_OIT_INST:"+(this.CO_OIT_INST)+",";
    	ret+="CO_CIRC:"+(this.CO_CIRC)+",";
    	return ret;
    }
    
    @JsonIgnore
    public NEGO_SUCU_PLAN getPlanActivo(){
        //Retorna de un sucursal instalada el plan activo
        return mapNego_Sucu_Plan.getPlanActivo(this);      
    }
    
    @JsonIgnore
    public NEGO_SUCU_PLAN getUltimoPlanActivo(){
         //Obtiene el ultimo plan activo
        return mapNego_Sucu_Plan.getUltimoPlanActivo(this);        
    }
    
    @JsonIgnore
    public List<NEGO_SUCU_SERV_UNIC> getServiciosUnicosPendientes(Integer CO_CIER) {
        
        if(CO_CIER==0){
            return NEGO_SUCU.mapNego_Sucu_Serv_Unic.getServiciosUnicosPendientesSinCierre(this.CO_NEGO_SUCU);
        }else{
            return NEGO_SUCU.mapNego_Sucu_Serv_Unic.getServiciosUnicosPendientes(this.CO_NEGO_SUCU, CO_CIER);
        }
        
    }
    
    @JsonIgnore
    public List<NEGO_SUCU_PROM> getPromocionesPorcentajePendientes(CIER cier) {
        if (this.promociones_porcentaje_pendientes==null){
            this.promociones_porcentaje_pendientes=mapNego_Sucu_Prom.getPromocionesPorcentajePendientes(this,cier);
        }
        return this.promociones_porcentaje_pendientes;
    }
    @JsonIgnore
    public List<NEGO_SUCU_PROM> getPromocionesMontoPendientes(CIER cier) {
        if (this.promociones_monto_pendientes==null){
            this.promociones_monto_pendientes=mapNego_Sucu_Prom.getPromocionesMontoPendientes(this,cier);
        }
        return this.promociones_monto_pendientes;
    }

    @JsonIgnore
    public Date getUltimaFacturacion() {
        /*Obtiene la ultima fecha que facturo -- FE_ULTI_FACT*/       
        return mapNego_Sucu.getUltimaFacturacion(this);
    }

    @JsonIgnore
    public void desactivar(Integer CO_OIT_DESI,Boolean ST_SOAR_DESI,Date FE_FIN,HttpServletRequest request) {        
        if (FE_FIN!=null && CO_OIT_DESI!=null && ST_SOAR_DESI!=null && this.FE_FIN==null){
            this.FE_FIN=FE_FIN;
            this.CO_OIT_DESI=CO_OIT_DESI;
            this.ST_SOAR_DESI=ST_SOAR_DESI;
            Historial.initModificar(request, this);
            mapNego_Sucu.desactivar(this);
        }
    }

    @JsonIgnore
    public List<NEGO_SUCU_PROM> getPromocionesPendientes(CIER cier) {
        if (this.promociones_pendientes==null){
            this.promociones_pendientes=mapNego_Sucu_Prom.getPromocionesPendientes(this,cier);
        }
        return this.promociones_pendientes;        
    }
    
    @JsonIgnore
    public List<NEGO_SUCU_PROM> getPromociones() {
        return mapNego_Sucu_Prom.selectByCO_NEGO_SUCU(this.CO_NEGO_SUCU);        
    }
    
    @JsonIgnore
    public Date getFechaInicio(){
        //Retorna de un sucursal instalada el plan activo
        return this.FE_INIC;      
    }
    
    @JsonIgnore
    public Boolean isDesactivadoEnPresentCierre(){
        Boolean ret=false;
        ret=mapNego_Sucu.isDesactivadoEnPresentCierre(this.CO_NEGO_SUCU);
        return ret;
    }
    
    @JsonIgnore
    public NEGO_SUCU_PLAN getUltimoPlanDesactivado() {
        return mapNego_Sucu_Plan.getUltimoPlanDesactivado(this.CO_NEGO_SUCU);
    }
}
