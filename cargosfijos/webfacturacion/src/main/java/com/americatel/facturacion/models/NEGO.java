/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.models;

import com.americatel.facturacion.fncs.FechaHora;
import com.americatel.facturacion.historial.Historial;
import com.americatel.facturacion.mappers.mapAJUS;
import com.americatel.facturacion.mappers.mapCLIE;
import com.americatel.facturacion.mappers.mapCORT;
import com.americatel.facturacion.mappers.mapCURSOR;
import com.americatel.facturacion.mappers.mapMONE_FACT;
import com.americatel.facturacion.mappers.mapMOTI_DESC;
import com.americatel.facturacion.mappers.mapNEGO;
import com.americatel.facturacion.mappers.mapNEGO_PROM;
import com.americatel.facturacion.mappers.mapNEGO_SALD_CERO;
import com.americatel.facturacion.mappers.mapNEGO_SUCU;
import com.americatel.facturacion.mappers.mapNEGO_SUCU_SERV_UNIC;
import com.americatel.facturacion.mappers.mapPROD;
import com.americatel.facturacion.mappers.mapSUCU;
import com.americatel.facturacion.mappers.mapTIPO_FACT;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author crodas
 * 
 * Productos
 */
@Component
public class NEGO  extends Historial {
    //Autowired
    private static mapNEGO_SUCU_SERV_UNIC mapNego_Sucu_Serv_Unic=null;
    private static mapPROD mapProd=null;
    private static mapCORT mapCort=null;
    private static mapNEGO_SUCU mapNego_Sucu=null;
    private static mapCLIE mapClie=null;
    private static mapSUCU mapSucu=null;
    private static mapMONE_FACT mapMone_Fact=null;
    private static mapNEGO mapNego=null;
    private static mapCURSOR mapCursor=null;
    private static mapNEGO_PROM mapNego_Prom=null;
    private static mapAJUS mapAjus=null;
    private static mapNEGO_SALD_CERO mapNego_Sald_Cero=null;
    
    private Boolean ST_ELIM=false;
    
    private Integer CO_NEGO;
    private Integer CO_TIPO_FACT;
    private Integer CO_MONE_FACT;
    private Integer CO_PERI_FACT;
    private Integer CO_NEGO_ORIG;
    private Integer CO_PROD;
    private Integer CO_CLIE;
    private Integer CO_SUCU_CORR;
        
    private Boolean isCortado;
    private Boolean isDesactivado;
    private Boolean isPendiente;
    private Boolean isSuspendido;
    
    private Boolean isArrendamiento;
    
    private String DESC_PROD_PADRE;    // EMANUEL BARBARAN - EFITEC (SE AGREGO VARIABLES)
    private Integer COD_PROD_PADRE;// EMANUEL BARBARAN - EFITEC (SE AGREGO VARIABLES)
    private String REFERENCIA;// EMANUEL BARBARAN - EFITEC (SE AGREGO VARIABLES)
    private PROD_PADRE prod_padre;    // EMANUEL BARBARAN - EFITEC (SE AGREGO VARIABLES)
    
    private PROD prod;
    private CLIE clie;
    private SUCU sucu_corr;//sucursal de correspondencia
    
    private TIPO_FACT tipo_fact;
    private MONE_FACT mone_fact;
    private PERI_FACT peri_fact;

    //variables no BD
    //variable que guarda el inicio de facturacion del negocio
    //cuando es periodo triemstral,semestral y anual
    //esta fecha se calcula obteniendo el primero plan activo (su fecha de activacion)
    private Date Fecha_Inicio_Ciclo_No_Mensual=null;
    //variable para obtener cuantas promociones pendientes tiene un negocio
    private List<NEGO_PROM> promociones_porcentaje_pendientes=null;
    private List<NEGO_PROM> promociones_monto_pendientes=null;
    
    private List<AJUS> ajustes_pendientes=null;

    
    @Autowired
    public void setmapCURSOR(mapCURSOR mapCursor){
        NEGO.mapCursor=mapCursor;
    }     
    
    @Autowired
    public void setmapNEGO(mapNEGO mapNego){
        NEGO.mapNego=mapNego;
    }      
    
    @Autowired
    public void setmapMONE_FACT(mapMONE_FACT mapMone_Fact){
        NEGO.mapMone_Fact=mapMone_Fact;
    }  
    
    
    @Autowired
    public void setmapPROD(mapPROD mapProd){
        NEGO.mapProd=mapProd;
    }    
    
    @Autowired
    public void setmapCORT(mapCORT mapCort){
        NEGO.mapCort=mapCort;
    }
    
    @Autowired
    public void setmapNEGO_SUCU(mapNEGO_SUCU mapNego_Sucu){
        NEGO.mapNego_Sucu=mapNego_Sucu;
    }
    
    @Autowired
    public void setmapCLIE(mapCLIE mapClie){
        NEGO.mapClie=mapClie;
    }
    
    @Autowired
    public void setmapSUCU(mapSUCU mapSucu){
        NEGO.mapSucu=mapSucu;
    }
    
    @Autowired
    public void setmapNEGO_PROM(mapNEGO_PROM mapNego_Prom){
        NEGO.mapNego_Prom=mapNego_Prom;
    }
    
    @Autowired
    public void setmapAJUS(mapAJUS mapAjus){
        NEGO.mapAjus=mapAjus;
    }  
    
    @Autowired
    public void setmapNEGO_SALD_CERO(mapNEGO_SALD_CERO mapNego_Sald_Cero){
        NEGO.mapNego_Sald_Cero=mapNego_Sald_Cero;
    }
    
    @Autowired
    public void setmapNEGO_SUCU_SERV_UNIC(mapNEGO_SUCU_SERV_UNIC mapNego_Sucu_Serv_Unic){
        NEGO.mapNego_Sucu_Serv_Unic=mapNego_Sucu_Serv_Unic;        
    }     
    
    
    @JsonProperty("isCortado")
    public Boolean isCortado_ForJSON() {
        return isCortado;
    }
    
    @JsonProperty("isPendiente")
    public Boolean isPendiente_ForJSON() {
        return isPendiente;
    }
    
    @JsonProperty("isDesactivado")
    public Boolean isDesactivado_ForJSON() {
        return isDesactivado;
    }
    
    @JsonProperty("isSuspendido")
    public Boolean isSuspendido_ForJSON() {
        return isSuspendido;
    }
    
    @JsonProperty("isArrendamiento")
    public Boolean isArrendamiento_ForJSON() {
        return isArrendamiento;
    }
    
    /*GET SET*/
    
    @JsonProperty("REFERENCIA")
    public String getReferencia_ForJSON() {
        return REFERENCIA;
    }
            
            
    @JsonProperty("PROD_PADRE")
    public PROD_PADRE getProd_padre_ForJSON() {
        return prod_padre;
    }
    
    
    @JsonProperty("DESC_PROD_PADRE")
    public String getDESC_PROD_PADRE_ForJSON() {
        return DESC_PROD_PADRE;
    }  
    
    @JsonProperty("COD_PROD_PADRE")
    public Integer getCOD_PROD_PADRE_ForJSON() {
        return COD_PROD_PADRE;
    }  

    
    @JsonProperty("ST_ELIM")
    public Boolean getST_ELIM_ForJSON() {
        return ST_ELIM;
    }      
    
    @JsonProperty("TIPO_FACT")
    public TIPO_FACT getTipo_fact_ForJSON() {
        return tipo_fact;
    }
    
    @JsonProperty("MONE_FACT")
    public MONE_FACT getMone_fact_ForJSON() {
        return mone_fact;
    }
    
    @JsonProperty("PERI_FACT")
    public PERI_FACT getPeri_fact_ForJSON() {
        return peri_fact;
    }

    @JsonProperty("CO_PROD")
    public Integer getCO_PROD_ForJSON() {
        return CO_PROD;
    }

    @JsonProperty("PROD")
    public PROD getProd_ForJSON() {
        return prod;
    }
    
    @JsonProperty("SUCU_CORR")
    public SUCU getSUCU_CORR_ForJSON() {
        return this.sucu_corr;
    }    

    @JsonProperty("CO_CLIE")
    public Integer getCO_CLIE_ForJSON() {
        return CO_CLIE;
    }
    
    @JsonProperty("CLIE")
    public CLIE getCLIE_ForJSON() {
        return clie;
    }
       
    @JsonIgnore
    public CLIE getClie() {
        return mapClie.getId(this.CO_CLIE);
    }
    
    @JsonIgnore
    public NEGO_SALD_CERO getSaldoCero() {
        return mapNego_Sald_Cero.getSaldoCeroByNego(this.CO_NEGO);
    }
    
    @JsonProperty("CO_NEGO")
    public Integer getCO_NEGO_ForJSON() {
        return CO_NEGO;
    }

    @JsonProperty("CO_SUCU_CORR")
    public Integer getCO_SUCU_CORR_ForJSON() {
        return CO_SUCU_CORR;
    }
    
    @JsonProperty("CO_TIPO_FACT")
    public Integer getCO_TIPO_FACT_ForJSON() {
        return CO_TIPO_FACT;
    }

    @JsonProperty("CO_MONE_FACT")
    public Integer getCO_MONE_FACT_ForJSON() {
        return CO_MONE_FACT;
    }

    @JsonProperty("CO_PERI_FACT")
    public Integer getCO_PERI_FACT_ForJSON() {
        return CO_PERI_FACT;
    }
    
    @JsonProperty("CO_NEGO_ORIG")
    public Integer getCO_NEGO_ORIG_ForJSON() {
        return CO_NEGO_ORIG;
    }
    
    public void setCO_NEGO(Integer CO_NEGO) {
        this.CO_NEGO = CO_NEGO;
    }

    public void setCO_TIPO_FACT(Integer CO_TIPO_FACT) {
        this.CO_TIPO_FACT = CO_TIPO_FACT;
    }

    public void setCO_MONE_FACT(Integer CO_MONE_FACT) {
        this.CO_MONE_FACT = CO_MONE_FACT;
    }

    public void setCO_PERI_FACT(Integer CO_PERI_FACT) {
        this.CO_PERI_FACT = CO_PERI_FACT;
    }

    public void setCO_PROD(Integer CO_PROD) {
        this.CO_PROD = CO_PROD;
    }

    public void setCO_CLIE(Integer CO_CLIE) {
        this.CO_CLIE = CO_CLIE;
    }

    public void setCO_NEGO_ORIG(Integer CO_NEGO_ORIG) {
        this.CO_NEGO_ORIG = CO_NEGO_ORIG;
    }
    
    public void setCO_SUCU_CORR(Integer CO_SUCU_CORR) {
        this.CO_SUCU_CORR = CO_SUCU_CORR;
    }
    
    public void setReferencia(String REFERENCIA){
        this.REFERENCIA = REFERENCIA;
        
    }
    
    public void setST_ELIM(Boolean ST_ELIM) {
        this.ST_ELIM = ST_ELIM;
    }     
        
    public void setDESC_PROD_PADRE(String DESC_PROD_PADRE) {
        this.DESC_PROD_PADRE = DESC_PROD_PADRE;
    }                   
                
    public void setCOD_PROD_PADRE(Integer COD_PROD_PADRE) {
        this.COD_PROD_PADRE = COD_PROD_PADRE;
    }   
    
    public Boolean isDesactivado() {
        return NEGO.mapNego_Sucu.isNegoDesactivado(this.CO_NEGO);
    }
    
    public Boolean isNuevo() {
        return NEGO.mapNego.isNuevo(this.CO_NEGO);
    }
    
    @JsonIgnore
    public List<CORT> getCortesPendientes_Fact() {
        //Lista los cortes de un negocios pendientes
        return NEGO.mapCort.selectCortesPendientesFactByCO_NEGOSortByFE_INIC(this.CO_NEGO);
        
        /*List<CORT> lst=new ArrayList<CORT>();
        CORT c=new CORT();
        c.setFE_INIC(FechaHora.getDate(2015, 0, 5));
        c.setFE_FIN(FechaHora.getDate(2015, 0, 8));
        lst.add(c);
        return lst;*/
    }
    
    @JsonIgnore
    public List<CORT> getCortesPendientes() {
        //Lista los cortes de un negocios pendientes
        return NEGO.mapCort.selectCortesPendientesByCO_NEGOSortByFE_INIC(this.CO_NEGO);
        
        /*List<CORT> lst=new ArrayList<CORT>();
        CORT c=new CORT();
        c.setFE_INIC(FechaHora.getDate(2015, 0, 5));
        c.setFE_FIN(FechaHora.getDate(2015, 0, 8));
        lst.add(c);
        return lst;*/
    }
    
    
    @JsonIgnore
    public PROD_PADRE getProductoByNegocio(int CO_PROD) {
        //Lista los cortes de un negocios pendientes
        return mapProd.getProductoByNegocio(CO_PROD);
    }    
            
            
    
    @JsonIgnore
    public Boolean isCortadoFacturado() {
        //Lista los cortes de un negocios pendientes
        return NEGO.mapCort.isCortadoFacturado(this.CO_NEGO);
    }

    @JsonIgnore
    public List<NEGO_SUCU> getSucursales() {
        //Lista todas las sucursales de un negocio
        return NEGO.mapNego_Sucu.selectByNego(this.CO_NEGO);
    }


    @JsonIgnore
    public Integer getNumMesesPeriodo(){ 
        //Obtiene el numero de mes del periodo
        switch(this.CO_PERI_FACT){
            case 1:
                return 1;
            case 2:
                return 3;
            case 3:
                return 6;
            case 4:
                return 12;
        }
        return 0;
    }
    
    @JsonIgnore
    public String getStringTipoFacturacion(){
        if (this.CO_TIPO_FACT==1)
            return "Vencida";
        return "Adelantada";
    }

    @JsonIgnore
    public String getStringMonedaFacturacion(){
        if (this.CO_MONE_FACT==1)
            return "Soles";
        return "Dolares";
    }
    
    @JsonIgnore
    public String getStringPeriodoFacturacion(){
        switch(this.CO_PERI_FACT){
            case 1: return "Mensual";
            case 2: return "Trimestral"; 
            case 3: return "Semestral";
            case 4: return "Anual";                    
        }
        return null;
    }    

    @JsonIgnore
    public SUCU getSucursalCorrespondencia() {
        return mapSucu.getId(this.CO_SUCU_CORR);
    }

    @JsonIgnore
    public PROD getPROD() {
        return mapProd.getId(this.CO_PROD);
    }

    @JsonIgnore
    public CLIE getCliente() {
        return mapNego.getCliente(this);
    }

    @JsonIgnore
    public MONE_FACT getMonedaFacturacion() {
        return mapNego.getMonedaFacturacion(this);
    }

    @JsonIgnore
    public Double getSaldoNegocio(CIER cier) {
        Double r=mapNego.getSaldoNegocio(this,cier);
        if (r==null) r=0d;
        return r;
    }
    
    @JsonIgnore
    public Date getFecha_Inicio_Ciclo_No_Mensual(){
        //Obtiene el inicio de facturacion
        //para negocio trim seme anual
        if (this.Fecha_Inicio_Ciclo_No_Mensual==null){
            String sql="SELECT TOP 1 Cast(nsp.FE_INIC as datetime) as FE_INIC FROM TMNEGO_SUCU ns INNER JOIN TMNEGO_SUCU_PLAN nsp ON nsp.CO_NEGO_SUCU=ns.CO_NEGO_SUCU WHERE ns.CO_NEGO="+this.CO_NEGO.toString()+" ORDER BY nsp.FE_INIC ASC";
            List<Map<String,Object>> lista=NEGO.mapCursor.select(sql);
            if(lista.size()==1){
                this.Fecha_Inicio_Ciclo_No_Mensual=(Date) lista.get(0).get("FE_INIC");
            }
        }
        return this.Fecha_Inicio_Ciclo_No_Mensual;
    }
    
    @JsonIgnore
    public List<NEGO_PROM> getPromocionesPorcentajePendientes(CIER cier){
        if (this.promociones_porcentaje_pendientes==null){
            promociones_porcentaje_pendientes=mapNego_Prom.getPromocionesPorcentajePendientes(this,cier);
        }
        return promociones_porcentaje_pendientes;
    } 
    
    @JsonIgnore
    public List<NEGO_PROM> getPromocionesMontoPendientes(CIER cier){
        if (this.promociones_monto_pendientes==null){
            promociones_monto_pendientes=mapNego_Prom.getPromocionesMontoPendientes(this,cier);
        }
        return promociones_monto_pendientes;
    }  
    
    @JsonIgnore
    public List<AJUS> getAjustesPendientes(CIER cier){
        if (this.ajustes_pendientes==null){
            ajustes_pendientes=mapAjus.getAjustesPendientes(this,cier);
        }
        return ajustes_pendientes;
    }  
    
    @JsonIgnore
    public Double getTotalMontoAjustesPendientes(){
        Double ret=0d;
        for(AJUS ajus : this.ajustes_pendientes){
            ret+=ajus.getIM_MONT_ForJSON();
        }
        return ret;
    }
    
    @JsonIgnore
    public Double getSaldoAproximado(){
        Double ret=0d;
        List<Double> montosSaldosAprox=mapNego.getSaldosAproximados(this);
        for (Double monto:montosSaldosAprox){
            if (monto!=null){
                ret+=monto;
            }
        }
        return ret;
    }
    
    @JsonIgnore
    public Boolean isDesactivadoEnPresentCierre(){
        Boolean ret=false;
        ret=mapNego.isDesactivadoEnPresentCierre(this.CO_NEGO);
        return ret;
    }

    @JsonIgnore
    public List<NEGO_PROM> getPromocionesPendientes(CIER cier) {
        return mapNego_Prom.getPromocionesPendientes(this,cier );
    }

    @JsonIgnore
    public Date getFechaUltimaFacturacionMenor() {
        //Obtiene la menor ultima fecha de facturacion de Planes y Servicios Suplementarios
        //siempre y cuando la Fecha desactivacion sea diferente a la ultima fecha de desactivacion
        //y no este desactivada.
        //y su ultima fecha de facturacion debe ser menor a fecha desactivacion si la tubiera
        //Si no tiene se toma la fecha de activacion del menor ,de las que no esten desactivada
        List<Date> fechas;
        fechas=mapNego.getFechaUltimaFacturacionMenor(this);
        Date ret=null;
        if (fechas!=null){
            for(Date fecha : fechas){
                if(fecha!=null){
                    if (ret==null){
                        ret=fecha;
                    }else{
                        if (ret.getTime()>fecha.getTime()){
                            ret=fecha;
                        }
                    }
                }
            }
        }
        if (ret==null){
            fechas=mapNego.getFechaActivacionSinFacturarMenor(this);
            for(Date fecha : fechas){
                if(fecha!=null){
                    if (ret==null){
                        ret=fecha;
                    }else{
                        if (ret.getTime()>fecha.getTime()){
                            ret=fecha;
                        }
                    }
                }
            }
            if (ret!=null)
                ret=FechaHora.addDays(ret, -1);//se quita uno ya que la funcion retorna la ultima fecha de facturacion
        }
        return ret;
    }
    
    @JsonIgnore
    public Boolean isCortado() {
        return NEGO.mapCort.isCortado(this.CO_NEGO);
    }
    
    @JsonIgnore
    public Boolean tieneNegoSucuSuspendido() {
        return NEGO.mapNego_Sucu.isNegoSuspendido(this.CO_NEGO);
    }
    
    
    @JsonIgnore
    public boolean isTipoFacturacionAdelantada(){
        return this.CO_TIPO_FACT==2;
    }
    @JsonIgnore
    public boolean isTipoFacturacionVencida(){
        return this.CO_TIPO_FACT==1;
    }    

    @JsonIgnore
    public List<NEGO_SUCU_SERV_UNIC> getServiciosUnicosPendientes(Integer CO_CIER) {
        return NEGO.mapNego_Sucu_Serv_Unic.getServiciosUnicosPendientesPorNegocio(this.CO_NEGO, CO_CIER);
    }
}
