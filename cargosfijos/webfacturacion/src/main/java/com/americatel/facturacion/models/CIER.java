/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.models;


import com.americatel.facturacion.Utils.Constante;
import com.americatel.facturacion.controllers.Cier;
import com.americatel.facturacion.fncs.fnc;
import com.americatel.facturacion.historial.Historial;
import com.americatel.facturacion.mappers.MapReglaNombrePlan;
import com.americatel.facturacion.mappers.mapBOLE;
import com.americatel.facturacion.mappers.mapCIER;
import com.americatel.facturacion.mappers.mapCORT;
import com.americatel.facturacion.mappers.mapDETALLE_DOCUMENTO;
import com.americatel.facturacion.mappers.mapFACT;
import com.americatel.facturacion.mappers.mapRECI;
import com.americatel.facturacion.mappers.mapNEGO_SALD;
import com.americatel.facturacion.mappers.mapPROD;
import com.americatel.facturacion.mappers.mapSUSP;
import com.americatel.facturacion.mappers.mapTABLA_TABLAS;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

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
public class CIER extends Historial{
    //Autowired
    private static mapFACT mapFact=null;
    private static mapRECI mapReci=null;
    private static mapCIER mapCier=null; 
    private static mapCORT mapCort=null; 
    private static mapSUSP mapSusp=null; 
    private static mapPROD mapProd=null;
    private static mapDETALLE_DOCUMENTO mapDetalle_Documento=null;
    private static MapReglaNombrePlan mapReglaNombrePlan=null;
    
    private static mapBOLE mapBole=null;
    
    private static mapNEGO_SALD mapNego_Sald=null;
    
    @Autowired
    private static mapTABLA_TABLAS mapTABLAS;
    
    static Logger logger = Logger.getLogger(CIER.class.getName());
    
    private Integer CO_CIER;
    private Integer CO_PROD;
    private Integer NU_PERI;
    private Integer NU_ANO;
    private Integer ST_CIER;
    private Long DE_RAIZ_RECI;
    private Long DE_RAIZ_FACT;
    private Long DE_RAIZ_BOLE;
    @DateTimeFormat(pattern = fnc.FORMAT_DATE_TIME_SEPARATOR )//"yyyy-MM-dd'T'HH:mm:ss"
    private Date FE_EMIS;
    @DateTimeFormat(pattern = fnc.FORMAT_DATE_TIME_SEPARATOR)
    private Date FE_VENC;

    @DateTimeFormat(pattern = fnc.FORMAT_DATE_TIME_SEPARATOR)
    private Date FH_INIC;    
    @DateTimeFormat(pattern = fnc.FORMAT_DATE_TIME_SEPARATOR)
    private Date FH_FIN; 
    
    private Boolean ST_ELIM=false;
    private Double NU_TIPO_CAMB;
    
    private PROD prod;
    

    @Autowired
    public void setmapPROD(mapPROD mapProd){
    	CIER.mapProd=mapProd;
    }
    @Autowired
    public void setmapRECI(mapRECI mapReci){
    	CIER.mapReci=mapReci;
    }    
    @Autowired
    public void setmapCIER(mapCIER mapCier){
    	CIER.mapCier=mapCier;
    } 
    @Autowired
    public void setmapSUSP(mapSUSP mapSusp){
    	CIER.mapSusp=mapSusp;
    } 
    @Autowired
    public void setmapCORT(mapCORT mapCort){
    	CIER.mapCort=mapCort;
    } 
    @Autowired
    public void setmapFACT(mapFACT mapFact){
    	CIER.mapFact=mapFact;
    }      
    @Autowired
    public void setmapNEGO_SALD(mapNEGO_SALD mapNego_Sald){
    	CIER.mapNego_Sald=mapNego_Sald;
    }
    
    @Autowired
    public void setmapBOLE(mapBOLE mapBole){
    	CIER.mapBole=mapBole;
    }   
    
    @Autowired
	public void setMapDetalle_Documento(mapDETALLE_DOCUMENTO mapDetalle_Documento) {
		CIER.mapDetalle_Documento = mapDetalle_Documento;
	}
    
    @Autowired
    public void setMapReglaNombrePlan(MapReglaNombrePlan mapReglaNombrePlan) {
		CIER.mapReglaNombrePlan = mapReglaNombrePlan;
	}
    
    /*GETS SETS*/
     @JsonProperty("NU_TIPO_CAMB")
    public Double getNU_TIPO_CAMB_ForJSON() {
        return NU_TIPO_CAMB;
    }      
       
    @JsonProperty("ST_ELIM")
    public Boolean getST_ELIM_ForJSON() {
        return ST_ELIM;
    }      
    
    @JsonProperty("CO_CIER")
    public Integer getCO_CIER_ForJSON() {
        return CO_CIER;
    }

    @JsonProperty("CO_PROD")
    public Integer getCO_PROD_ForJSON() {
        return CO_PROD;
    }

    @JsonProperty("NU_PERI")
    public Integer getNU_PERI_ForJSON() {
        return NU_PERI;
    }

    @JsonProperty("NU_ANO")
    public Integer getNU_ANO_ForJSON() {
        return NU_ANO;
    }

    @JsonProperty("ST_CIER")
    public Integer getST_CIER_ForJSON() {
        return ST_CIER;
    }

    @JsonProperty("DE_RAIZ_RECI")
    public Long getDE_RAIZ_RECI_ForJSON() {
        return DE_RAIZ_RECI;
    }
    
    @JsonProperty("DE_RAIZ_FACT")
    public Long getDE_RAIZ_FACT_ForJSON() {
        return DE_RAIZ_FACT;
    }    
    
    @JsonProperty("FE_EMIS")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = fnc.FORMAT_DATE )//"yyyy/MM/dd"
    public Date getFE_EMIS_ForJSON() {
        return FE_EMIS;
    }       
    @JsonProperty("FE_VENC")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = fnc.FORMAT_DATE )//"yyyy/MM/dd"
    public Date getFE_VENC_ForJSON() {
        return FE_VENC;
    }
    @JsonProperty("FH_INIC")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern =fnc.FORMAT_DATE_TIME)// "yyyy/MM/dd HH:mm:ss"
    public Date getFE_INIC_ForJSON() {
        return FH_INIC;
    }     
    @JsonProperty("FH_FIN")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = fnc.FORMAT_DATE_TIME)//"yyyy/MM/dd HH:mm:ss")
    public Date getFE_FIN_ForJSON() {
        return FH_FIN;
    }  
    
    @JsonProperty("StringDuracion")
    public String getStringDuracion() {
    	//en segundos
        if (this.FH_INIC!=null && this.FH_FIN!=null){
        	long a=this.FH_INIC.getTime();
        	long b=this.FH_FIN.getTime();
        	if (b>a){
        		return com.americatel.facturacion.fncs.FechaHora.getDiffSeconds(this.FH_INIC, this.FH_FIN).toString();
        	}
        }
        return "";
    }
    
    @JsonProperty("PROD")
    public PROD getProd_ForJSON() {
        return prod;
    }
    
    public void setNU_TIPO_CAMB(Double NU_TIPO_CAMB) {
        this.NU_TIPO_CAMB = NU_TIPO_CAMB;
    }    
    
    public void setCO_CIER(Integer CO_CIER) {
        this.CO_CIER = CO_CIER;
    }

    public void setCO_PROD(Integer CO_PROD) {
        this.CO_PROD = CO_PROD;
    }

    public void setNU_PERI(Integer NU_PERI) {
        this.NU_PERI = NU_PERI;
    }

    public void setNU_ANO(Integer NU_ANO) {
        this.NU_ANO = NU_ANO;
    }

    public void setST_CIER(Integer ST_CIER) {
        this.ST_CIER = ST_CIER;
    }

    public void setDE_RAIZ_RECI(Long DE_RAIZ_RECI) {
        this.DE_RAIZ_RECI = DE_RAIZ_RECI;
    }    

    public void setDE_RAIZ_FACT(Long DE_RAIZ_FACT) {
        this.DE_RAIZ_FACT = DE_RAIZ_FACT;
    } 
    
    public void setFE_EMIS(Date FE_EMIS) {
        this.FE_EMIS = FE_EMIS;
    }

    public void setFE_VENC(Date FE_VENC) {
        this.FE_VENC = FE_VENC;
    }
 
    public void setFH_INIC(Date FH_INIC) {
        this.FH_INIC = FH_INIC;
    }    
    public void setFH_FIN(Date FH_FIN) {
        this.FH_FIN = FH_FIN;
    }
    
    public void setST_ELIM(Boolean ST_ELIM) {
        this.ST_ELIM = ST_ELIM;
    }     
    
    
    //funcionalidades
    @JsonIgnore
    public String getStringStateCierre() {
    	switch(this.ST_CIER){
			case 1:	return "Pendiente";
			case 2:	return "En cierre";
			case 3:	return "Termino cierre";
			case 4:	return "Cerrado";
    	}
    	return null;
    }
    @JsonIgnore
    public void Pendiente(){
    	this.ST_CIER=Constante.ESTADO_CIERRE_PENDIENTE;
    	CIER.mapCier.updateEstados(this);
    }    
    @JsonIgnore
    public void EnCierre(HttpServletRequest request){
    	this.ST_CIER=Constante.ESTADO_CIERRE_EN_PROCESO;
    	this.FH_INIC=new Date();
        Historial.initModificar(request, this);
    	CIER.mapCier.updateEstados(this);        
        CIER.mapCier.removeDataGeneradaEnCierre(this);
        logger.info("Actualiza Cierre :" +this.getCO_CIER_ForJSON()+ " Estado : "+this.getST_CIER_ForJSON());
        
        
    }
    @JsonIgnore
    public void TerminoCierre(HttpServletRequest request){
    	this.ST_CIER=Constante.ESTADO_CIERRE_TERMINADO;
    	this.FH_FIN=new Date();
        Historial.initModificar(request, this);
    	CIER.mapCier.updateEstados(this);
        logger.info("Actualiza Cierre :" +this.getCO_CIER_ForJSON()+ " Estado : "+this.getST_CIER_ForJSON());
    } 
    
    @JsonIgnore
    public void Cerrar(HttpServletRequest request){
        if (this.ST_CIER==Constante.ESTADO_CIERRE_TERMINADO){//solo se puede cerrar si es que termino el cierre
            this.ST_CIER=Constante.ESTADO_CIERRE_CERRADO;
            Historial.initModificar(request, this);
            CIER.mapCier.updateEstados(this);
            //despues de cerrar el periodo debemos
            //1)Congelar las suspensiones y cortes
            CIER.mapCort.congelarPostCierre(this.CO_PROD,this.CO_CIER);
            CIER.mapSusp.congelarPostCierre(this.CO_PROD,this.CO_CIER);
            //2)Guardar la ultima fecha de facturacion de los planes y SS
            CIER.mapCier.congelarPostCierre(this);            
            //3)Generar el proximo cierre para el sistema
            
            CIER c=new CIER();
            c.CO_PROD=this.CO_PROD;
            c.DE_RAIZ_RECI=(this.DE_RAIZ_RECI==null?0:this.DE_RAIZ_RECI)+this.getCantidadRecibosGenerados();
            c.DE_RAIZ_FACT=(this.DE_RAIZ_FACT==null?0:this.DE_RAIZ_FACT)+this.getCantidadFacturasGeneradas();
            c.DE_RAIZ_BOLE=(this.DE_RAIZ_BOLE==null?0:this.DE_RAIZ_BOLE)+this.getCantidadBoletasGeneradas();
//            c.NU_TIPO_CAMB=3.2d;
            c.NU_TIPO_CAMB=this.NU_TIPO_CAMB;
            c.FE_EMIS=this.FE_EMIS;
            c.FE_VENC=this.FE_VENC;
            c.NU_ANO=this.NU_ANO;
            c.NU_PERI=this.NU_PERI;            
            c.ST_CIER=1;//pendiente
            c.NU_PERI++;            
            if (c.NU_PERI>12){
                c.NU_PERI=1;
                c.NU_ANO++;
            }
            Historial.initInsertar(request, c);
            mapCier.insert(c);
        }
    }
    
    @JsonIgnore
    public List<RECI> getRecibos() {
        return mapReci.getRecibos(this);
    }
    
    @JsonIgnore
    public List<RECI> getRecibosAnulados() {
        return mapReci.getRecibosAnulados(this);
    }
    
    @JsonIgnore
    public List<NEGO_SALD> getSaldos() {
        return mapNego_Sald.getSaldos(this);
    }

    public Integer getCantidadRecibosGenerados() {
        return mapCier.getCantidadRecibosGenerados(this);
    }
    
    public Integer getCantidadFacturasGeneradas() {
        return mapCier.getCantidadFacturasGeneradas(this);
    } 
    
    public Integer getCantidadBoletasGeneradas() {
        return mapCier.getCantidadBoletasGeneradas(this);
    }  

    @JsonIgnore
    public List<FACT> getFacturas() {
        return mapFact.getFacturas(this);
    }
    @JsonIgnore
    public PROD getProducto() {
        return mapProd.getId(this.CO_PROD);
    }

    @JsonIgnore
    public void updateLanzar() {
        
        logger.info(new StringBuilder().append("Actualiza" )
                            .append("FE_EMIS: ").append(this.FE_EMIS)
                            .append("FE_VENC: ").append(this.FE_VENC)
                            .append("DE_RAIZ_RECI: ").append(this.DE_RAIZ_RECI)
                            .append("DE_RAIZ_FACT: ").append(this.DE_RAIZ_FACT)
                            .append("NU_TIPO_CAMB: ").append(this.NU_TIPO_CAMB)
                            .append("DE_RAIZ_BOLE: ").append(this.DE_RAIZ_BOLE)
                            .append("PARA EL CIERRE :").append(this.CO_CIER).toString());
        mapCier.updateLanzar(this);
    }
    
    @JsonIgnore
    public void limpiarSaldosAproximados() {
        mapCier.limpiarSaldosAproximados(this);
    }

    @JsonIgnore
    public List<CIER_DATA_PLAN> getDataPlanesCobrados_forMaestroGerentes() {
        return mapCier.getDataPlanesCobrados_forMaestroGerentes(this);
    }
    
    @JsonIgnore
    public List<CIER_DATA_SERV_SUPL> getDataServiciosSuplCobrados_forMaestroGerentes() {
        return mapCier.getDataServiciosSuplCobrados_forMaestroGerentes(this);
    }
    
    @JsonIgnore
    public List<CIER_DATA_SERV_UNIC> getDataServiciosUnicCobrados_forMaestroGerentes() {
        return mapCier.getDataServiciosUnicCobrados_forMaestroGerentes(this);
    }

    public List<NEGO_SUCU_PLAN> getNEGO_SUCU_PLAN_forMaestroGerentes() {
        return mapCier.getNEGO_SUCU_PLAN_forMaestroGerentes(this);
    }


    @JsonIgnore
    public PROD getProd_fromMapper() {
        return prod;
    }

    /**
     * @return the DE_RAIZ_BOLE
     */
    public Long getDE_RAIZ_BOLE() {
        return DE_RAIZ_BOLE;
    }

    /**
     * @param DE_RAIZ_BOLE the DE_RAIZ_BOLE to set
     */
    public void setDE_RAIZ_BOLE(Long DE_RAIZ_BOLE) {
        this.DE_RAIZ_BOLE = DE_RAIZ_BOLE;
    }

    @JsonIgnore
    public List<BOLE> getBoletas() {
        return mapBole.getBoletas(this);
    }
    
    
    @JsonIgnore
    public List<BOLE> getBoletasAnulados() {
        return mapBole.getBoletasAnulados(this);
    }
    
     
    @JsonIgnore
    public List<FACT> getFacturasAnulados() {
        return mapFact.getFacturasAnulados(this);
    }

    @JsonIgnore
    public List<ReporteCargaTI> getReporteCargaTI() {
        return mapCier.getDataReporteCargaTI(this);
    }
    
    @JsonIgnore
	public List<DETALLE_DOCUMENTO>  getDetalleForCodFact(Integer coFact) {
		return mapDetalle_Documento.getDetalleForCodFact(coFact);
	}
	
	@JsonIgnore
    public String getNombreServicio(Integer codServicio) {
        return CIER.mapProd.obtenerNombreServicio(codServicio);
    }
	
	
	@JsonIgnore
    public String getNombreServicioSuplementario(Integer coFact) {
        return CIER.mapProd.getNombreServicioSuplementario(coFact);
    }
	
	@JsonIgnore
    public String obtenerNombreProductoCarga(Integer codProducto, Boolean stBackupBu, Integer codServicio, Boolean relacionadoTI, String nombrePlan) {
        return CIER.mapReglaNombrePlan.obtenerNombreProductoCarga(codProducto, stBackupBu, codServicio, relacionadoTI, nombrePlan);
    }
    
}

