/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.models;

import com.americatel.facturacion.fncs.FechaHora;
import com.americatel.facturacion.fncs.fnc;
import com.americatel.facturacion.historial.Historial;
import com.americatel.facturacion.mappers.mapCIER_DATA_PLAN;
import com.americatel.facturacion.mappers.mapCORT;
import com.americatel.facturacion.mappers.mapMODU;
import com.americatel.facturacion.mappers.mapNEGO;
import com.americatel.facturacion.mappers.mapNEGO_SUCU;
import com.americatel.facturacion.mappers.mapNEGO_SUCU_PLAN;
import com.americatel.facturacion.mappers.mapNEGO_SUCU_SERV_SUPL;
import com.americatel.facturacion.mappers.mapPLAN;
import com.americatel.facturacion.mappers.mapSUCU;
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
public class NEGO_SUCU_PLAN  extends Historial {
    //Autowired
    private static mapNEGO_SUCU mapNego_Sucu=null;
    private static mapNEGO_SUCU_PLAN mapNego_Sucu_Plan=null;
    private static mapNEGO_SUCU_SERV_SUPL mapNego_Sucu_Serv_Supl=null;
    private static mapSUCU mapSucu=null;
    private static mapNEGO mapNego=null;
    private static mapSUSP mapSusp=null;
    private static mapCIER_DATA_PLAN mapCier_Data_Plan=null;
    private static mapPLAN mapPlan=null;
    private Boolean ST_ELIM=false;
    
    private Integer CO_NEGO_SUCU_PLAN;
    private Integer CO_NEGO_SUCU;
    private Boolean ST_SOAR_INST=false;
    private Integer CO_OIT_INST;
    private Boolean ST_SOAR_DESI=false;
    private Integer CO_OIT_DESI;    
    private Integer CO_PLAN;
    private String NO_PLAN;
    private String DE_VELO_SUBI;
    private String DE_VELO_BAJA;    
    @DateTimeFormat(pattern = fnc.FORMAT_DATE_TIME_SEPARATOR)//"yyyy-MM-dd'T'HH:mm:ss")
    private Date FE_INIC;
    @DateTimeFormat(pattern = fnc.FORMAT_DATE_TIME_SEPARATOR)//"yyyy-MM-dd'T'HH:mm:ss")
    private Date FE_FIN;
    @DateTimeFormat(pattern = fnc.FORMAT_DATE_TIME_SEPARATOR)//"yyyy-MM-dd'T'HH:mm:ss")
    private Date FE_ULTI_FACT;   
    @DateTimeFormat(pattern = fnc.FORMAT_DATE_TIME_SEPARATOR)//"yyyy-MM-dd'T'HH:mm:ss")
    private Date FE_ULTI_FACT_SGTE;     
    
    private Double IM_MONTO;
    private Double IM_SALD_APROX;

    private Integer CO_NEGO_SUCU_PLAN_CESI_CONT_PADR=null;//quien lo origino
    private Integer CO_NEGO_SUCU_PLAN_CESI_CONT_HIJO=null;//que origino
    private Integer CO_NEGO_SUCU_PLAN_MUDA_PADR=null;//quien lo origino
    private Integer CO_NEGO_SUCU_PLAN_MUDA_HIJO=null;//que origino
    //variables no BD
    //save consulta BD, ahorro
    
    private NEGO_SUCU_PLAN bd_upgrade_nuevo=null;
    private Boolean bd_upgrade_nuevo__busco=false;
    
    private NEGO_SUCU_PLAN bd_downgrade_nuevo=null;
    private Boolean bd_downgrade_nuevo__busco=false;
    
    private PLAN plan;
    @Autowired
    public void setmapCIER_DATA_PLAN(mapCIER_DATA_PLAN mapCier_Data_Plan){
        NEGO_SUCU_PLAN.mapCier_Data_Plan=mapCier_Data_Plan;        
    }
    
    @Autowired
    public void setmapSUSP(mapSUSP mapSusp){
        NEGO_SUCU_PLAN.mapSusp=mapSusp;        
    }

    @Autowired
    public void setmapNEGO(mapNEGO mapNego){
        NEGO_SUCU_PLAN.mapNego=mapNego;        
    }
    
    @Autowired
    public void setmapNEGO_SUCU(mapNEGO_SUCU mapNego_Sucu){
        NEGO_SUCU_PLAN.mapNego_Sucu=mapNego_Sucu;        
    }

    @Autowired
    public void setmapNEGO_SUCU_PLAN(mapNEGO_SUCU_PLAN mapNego_Sucu_Plan){
        NEGO_SUCU_PLAN.mapNego_Sucu_Plan=mapNego_Sucu_Plan;        
    }  
    
    @Autowired
    public void setmapNEGO_SUCU_SERV_SUPL(mapNEGO_SUCU_SERV_SUPL mapNego_Sucu_Serv_Supl){
        NEGO_SUCU_PLAN.mapNego_Sucu_Serv_Supl=mapNego_Sucu_Serv_Supl;        
    } 
    
    @Autowired
    public void setmapSUCU(mapSUCU mapSucu){
        NEGO_SUCU_PLAN.mapSucu=mapSucu;        
    }     
    
    @Autowired
    public void setmapPLAN(mapPLAN mapPlan){
        NEGO_SUCU_PLAN.mapPlan=mapPlan;        
    } 

    /*GEST SETS*/
    
    @JsonIgnore
    public PLAN getPLAN() {
        return mapPlan.getId(this.CO_PLAN);
    }
    
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
    @JsonProperty(value = "NO_PLAN")
    public String getNO_PLAN_ForJSON() {
        return NO_PLAN;
    }
    
    @JsonProperty("DE_VELO_SUBI")
    public String getDE_VELO_SUBI_ForJSON() {
        return DE_VELO_SUBI;
    }

    @JsonProperty("DE_VELO_BAJA")
    public String getDE_VELO_BAJA_ForJSON() {
        return DE_VELO_BAJA;
    }    

    @JsonProperty(value = "CO_NEGO_SUCU_PLAN")
    public Integer getCO_NEGO_SUCU_PLAN_ForJSON() {
        return CO_NEGO_SUCU_PLAN;
    }
    

    @JsonProperty("CO_NEGO_SUCU")
    public Integer getCO_NEGO_SUCU_ForJSON() {
        return CO_NEGO_SUCU;
    }

    @JsonProperty("CO_PLAN")
    public Integer getCO_PLAN_ForJSON() {
        return CO_PLAN;
    }

    @JsonProperty("FE_INIC")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = fnc.FORMAT_DATE )//"yyyy-MM-dd")
    public Date getFE_INIC_ForJSON() {
        return FE_INIC;
    }

    @JsonProperty("FE_FIN")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = fnc.FORMAT_DATE)//"yyyy-MM-dd")
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
    
    @JsonProperty("PLAN")
    public PLAN getPlan_ForJSON() {
        return plan;
    }

    @JsonProperty(value = "CO_NEGO_SUCU_PLAN_CESI_CONT_PADR")
    public Integer getCO_NEGO_SUCU_PLAN_CESI_CONT_PADR_ForJSON() {
        return CO_NEGO_SUCU_PLAN_CESI_CONT_PADR;
    }      

    @JsonProperty(value = "CO_NEGO_SUCU_PLAN_CESI_CONT_HIJO")
    public Integer getCO_NEGO_SUCU_PLAN_CESI_CONT_HIJO_ForJSON() {
        return CO_NEGO_SUCU_PLAN_CESI_CONT_HIJO;
    }
    
    @JsonProperty(value = "CO_NEGO_SUCU_PLAN_MUDA_PADR")
    public Integer getCO_NEGO_SUCU_PLAN_MUDA_PADR_ForJSON() {
        return CO_NEGO_SUCU_PLAN_MUDA_PADR;
    }      

    @JsonProperty(value = "CO_NEGO_SUCU_PLAN_MUDA_HIJO")
    public Integer getCO_NEGO_SUCU_PLAN_MUDA_HIJO_ForJSON() {
        return CO_NEGO_SUCU_PLAN_MUDA_HIJO;
    }
    
    public void setPlan(PLAN plan) {
        this.plan = plan;
    }
    
    public void setDE_VELO_SUBI(String DE_VELO_SUBI) {
        this.DE_VELO_SUBI = DE_VELO_SUBI;
    }

    public void setDE_VELO_BAJA(String DE_VELO_BAJA) {
        this.DE_VELO_BAJA = DE_VELO_BAJA;
    }    
    
    @JsonProperty("IM_MONTO")
    public Double getIM_MONTO_ForJSON() {
        return IM_MONTO;
    }
    
    @JsonProperty("IM_SALD_APROX")
    public Double getIM_SALD_APROX_ForJSON() {
        return IM_SALD_APROX;
    }

    public void setNO_PLAN(String NO_PLAN) {
        this.NO_PLAN = NO_PLAN;
    }
    
    public void setCO_NEGO_SUCU_PLAN(Integer CO_NEGO_SUCU_PLAN) {
        this.CO_NEGO_SUCU_PLAN = CO_NEGO_SUCU_PLAN;
    }
      

    public void setCO_NEGO_SUCU(Integer CO_NEGO_SUCU) {
        this.CO_NEGO_SUCU = CO_NEGO_SUCU;
    }

    public void setCO_PLAN(Integer CO_PLAN) {
        this.CO_PLAN = CO_PLAN;
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
    
    public void setIM_SALD_APROX(Double IM_SALD_APROX) {
        this.IM_SALD_APROX = IM_SALD_APROX;
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
    
    public void setCO_NEGO_SUCU_PLAN_CESI_CONT_PADR(Integer CO_NEGO_SUCU_PLAN_CESI_CONT_PADR) {
        this.CO_NEGO_SUCU_PLAN_CESI_CONT_PADR = CO_NEGO_SUCU_PLAN_CESI_CONT_PADR;
    }

    public void setCO_NEGO_SUCU_PLAN_CESI_CONT_HIJO(Integer CO_NEGO_SUCU_PLAN_CESI_CONT_HIJO) {
        this.CO_NEGO_SUCU_PLAN_CESI_CONT_HIJO = CO_NEGO_SUCU_PLAN_CESI_CONT_HIJO;
    }
    
    public void setCO_NEGO_SUCU_PLAN_MUDA_PADR(Integer CO_NEGO_SUCU_PLAN_MUDA_PADR) {
        this.CO_NEGO_SUCU_PLAN_MUDA_PADR = CO_NEGO_SUCU_PLAN_MUDA_PADR;
    }

    public void setCO_NEGO_SUCU_PLAN_MUDA_HIJO(Integer CO_NEGO_SUCU_PLAN_MUDA_HIJO) {
        this.CO_NEGO_SUCU_PLAN_MUDA_HIJO = CO_NEGO_SUCU_PLAN_MUDA_HIJO;
    }
    
    @JsonIgnore
    public String isValid(){
        //Verifica antes de insertar o modificar si es valido      
        //Si retorna null no existe error 
        if (this.FE_INIC==null)
            return "La fecha de inicio no puede estar vacia.";
    
        if (this.FE_FIN!=null)
            if (this.FE_INIC.getTime()>this.FE_FIN.getTime())
                return "La fecha de termino tiene que ser mayor a la fecha de inicio.";         
        
        //Verificamos que esta OIT de instalacion no este en otro PLAN
        if (this.CO_OIT_INST!=null){
            List<NEGO_SUCU_PLAN> tmpLista=NEGO_SUCU_PLAN.mapNego_Sucu_Plan.getByOitInstalacion(this);
            NEGO_SUCU_PLAN tmpUnico=null;
            if (tmpLista.size()<=1){
                if (tmpLista.size()==1){
                    tmpUnico=tmpLista.get(0); 
                    if (!tmpUnico.CO_NEGO_SUCU_PLAN.equals(this.CO_NEGO_SUCU_PLAN)){ 
                        return "Ya existe esta OIT de instalaci\u00f3n para otro PLAN("+tmpUnico.NO_PLAN+").";
                    }
                }             
            }else{
                return "Error existen demasiados planes con la misma OIT de instalaci\u00f3n";
            }
        }
        
        if (this.CO_OIT_DESI!=null){
            List<NEGO_SUCU_PLAN> tmpLista=tmpLista=NEGO_SUCU_PLAN.mapNego_Sucu_Plan.getByOitDesinstalacion(this);
            NEGO_SUCU_PLAN tmpUnico=null;
            if (tmpLista.size()<=1){
                if (tmpLista.size()==1){
                    tmpUnico=tmpLista.get(0);
                    if (!tmpUnico.CO_NEGO_SUCU_PLAN.equals(this.CO_NEGO_SUCU_PLAN)){
                        return "Ya existe esta OIT de desinstalaci\u00f3n para otro PLAN("+tmpUnico.NO_PLAN+").";
                    }
                }
            }else{
                return "Error existen demasiados planes con la misma OIT de desinstalaci\u00f3n";
            }            
        }
        return null;
    }
    
    
    @JsonIgnore
    public String isValidForInsert(){
        //Es valido para insertar
        NEGO_SUCU nego_sucu=this.getNEGO_SUCU();
        //Retorna null ni no existe error        
        if (nego_sucu.getPlanActivo()!=null)//no se puede agregar si ya existe otro plan activo
                return "Ya existe otro plan activo.";
        //Verificamos que el ultimo plan activo su fecha desactivacion debe ser menor a la fecha de activacion de este nuevo plan
        NEGO_SUCU_PLAN ultimo=nego_sucu.getUltimoPlanActivo();
        if (ultimo!=null){
            if (ultimo.FE_FIN.getTime()>=this.FE_INIC.getTime())
                return "El ultimo plan "+ultimo.getNO_PLAN_ForJSON()+" se desactivo el "+this.getFE_FIN_ForJSON()+" el nuevo plan tiene que ser mayor a esta fecha.";
        }
        return null;
    }
    
    @JsonIgnore
    public NEGO_SUCU getNEGO_SUCU(){
        return mapNego_Sucu.getId(this.CO_NEGO_SUCU);
    }
    
    @JsonIgnore
    public void saveSiguienteUltimaFacturacion(Date sgte) {
        this.FE_ULTI_FACT_SGTE=sgte;
        mapNego_Sucu_Plan.saveSiguienteUltimaFacturacion(this);
    }

    @JsonIgnore
    public void updateSaldoAproximado() {
        mapNego_Sucu_Plan.updateSaldoAproximado(this);
    }
       
    
    @JsonIgnore
    public NEGO_SUCU_PLAN getUpgradeAntiguo(Date periodo_inicio,Date periodo_fin) {
        /*
        Verificamos si el plan es un upgrade.
        Logica:
        Sabemos que el pk de la tabla es autoincrement tomamos top 1 los menores a este con un
        y este monto es menor al this(actual) entonces es un upgrade
        y debe estar en el periodo de facturacion como activacion.
        */
        if (FechaHora.isBetween(periodo_inicio, periodo_fin, this.FE_INIC)){
            NEGO_SUCU_PLAN tmp=mapNego_Sucu_Plan.getUpgradeAntiguo(this);
            if (tmp!=null)
                return tmp;                
        }
        return null;
    }    
    
    @JsonIgnore
    public boolean isUpgradeAntiguo(Date periodo_inicio,Date periodo_fin) {
        return null!=this.getUpgradeAntiguo(periodo_inicio,periodo_fin);
    }
    
    @JsonIgnore
    public Boolean isUpgradeNuevo() {
        /*
        Buscamos el nuevo apartir del viejo plan
        Logica:
        Buscamos el primer pk mayor a este de la misma sucursal y verficamos si este es mayor en monto a este
        */
        if (!this.bd_upgrade_nuevo__busco){
            this.bd_upgrade_nuevo=mapNego_Sucu_Plan.getUpgradeNuevo(this);
            this.bd_upgrade_nuevo__busco=true;
        }
        return this.bd_upgrade_nuevo!=null;
    }

    
    @JsonIgnore
    public NEGO_SUCU_PLAN getDowngradeAntiguo(Date periodo_inicio,Date periodo_fin) {
        /*
        Verificamos si el plan es un downgrade.
        Logica:
        Sabemos que el pk de la tabla es autoincrement tomamos top 1 los menores a este con un
        y este monto es mayor al this(actual) entonces es un downgrade
        y debe estar en el periodo de facturacion como activacion.
        */
        if (FechaHora.isBetween(periodo_inicio, periodo_fin, this.FE_INIC)){
            NEGO_SUCU_PLAN comparar=mapNego_Sucu_Plan.getDowngradeAntiguo(this);                            
            if (comparar!=null){
                return comparar;                
            }
        }
        return null;
    } 
    
    @JsonIgnore
    public boolean isDowngradeAntiguo(Date periodo_inicio, Date periodo_fin) {
        return null!=this.getDowngradeAntiguo(periodo_inicio,periodo_fin);
    }
       
    @JsonIgnore
    public Boolean isDowngradeNuevo() {
        /*
        Buscamos el nuevo apartir del viejo plan
        Logica:
        Buscamos el primer pk mayor a este de la misma sucursal y verficamos si este es menor en monto a este
        */
        if (!this.bd_downgrade_nuevo__busco){
            this.bd_downgrade_nuevo=mapNego_Sucu_Plan.getDowngradeNuevo(this);
            this.bd_downgrade_nuevo__busco=true;
        }
        return this.bd_downgrade_nuevo!=null;
    }  
    
    @JsonIgnore
    public List<CIER_DATA_PLAN> getCIER_DATA_PLANES_COBRADOS(CIER cier) {
        return mapCier_Data_Plan.getCIER_DATA_PLANES_COBRADOS(this,cier);
    }

    @JsonIgnore
    public List<NEGO_SUCU_PLAN> getPlanesEntrePeriodoDesactivado(Date UFF) {
        return mapNego_Sucu_Plan.getPlanesEntrePeriodoDesactivado(this,UFF);
    }

    @JsonIgnore
    public List<CIER_DATA_PLAN> getCIER_DATA_MI_PLAN_COBRADOS(CIER cier) {
        return mapCier_Data_Plan.getCIER_DATA_MI_PLAN_COBRADOS(this,cier);
    }

    @JsonIgnore
    public List<CIER_DATA_PLAN> getCIER_DATA_DEVOLUCIONES_POR_PROMOCIONES(CIER cier) {
        return mapCier_Data_Plan.getCIER_DATA_DEVOLUCIONES_POR_PROMOCIONES(this,cier);
    }
  
    @JsonIgnore
    public NEGO_SUCU_PLAN getById(Integer codigo) {
        return mapNego_Sucu_Plan.getId(codigo);
    }
    
}

