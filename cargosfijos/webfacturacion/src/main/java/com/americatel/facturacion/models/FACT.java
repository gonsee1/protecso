/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.models;

import com.americatel.facturacion.Utils.Constante;
import com.americatel.facturacion.core3.Factura;
import com.americatel.facturacion.core3.FacturaModificadoPdf;
import static com.americatel.facturacion.core3.ProcesoNegocio.mapCier;
import com.americatel.facturacion.fncs.fnc;
import com.americatel.facturacion.historial.Historial;
import com.americatel.facturacion.mappers.mapCLIE;
import com.americatel.facturacion.mappers.mapFACT;
import com.americatel.facturacion.mappers.mapFACT_GLOS;
import com.americatel.facturacion.mappers.mapNEGO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

/**
 *
 * @author crodas
 */
@Component
public class FACT extends Historial {
    private static mapFACT mapFact=null; 
    private static mapNEGO mapNego=null;
    private static mapFACT_GLOS mapFact_Glos = null;
    private static mapCLIE mapCLIE=null;
    
    private Long CO_FACT;
    private Integer CO_CIER;
    private Integer CO_NEGO;
    private String DE_MONT; //monto en texto 
    private Double IM_VALO_VENT;    
    private Double IM_VALO_DESC;    
    private Double IM_VALO_VENT_NETO;    
    private Double IM_IGV;    
    private Double IM_TOTA;  
    
    private Boolean ST_ELIM=false;
    private Boolean ST_ANUL=false;
    

    /*Historico*/
    @DateTimeFormat(pattern = fnc.FORMAT_DATE_TIME_SEPARATOR)//"yyyy-MM-dd'T'HH:mm:ss")//mmss
    private Date FE_EMIS;
    @DateTimeFormat(pattern = fnc.FORMAT_DATE_TIME_SEPARATOR)//"yyyy-MM-dd'T'HH:mm:ss")//mmss
    private Date FE_VENC;
    private Integer CO_CLIE;
    private String DE_CODI_BUM;
    private String NO_CLIE;
    private String DE_DIRE_FISC;
    private Integer CO_DIST_FISC;
    private String NO_DIST_FISC;
    private Integer CO_TIPO_DOCU;
    private String NO_TIPO_DOCU;
    private String DE_NUME_DOCU;
    private String DE_DIRE_CORR;
    private String DE_REF_DIRE_CORR;
    private String NO_DIST_CORR;
    private Integer CO_DIST_CORR;    
    private String DE_DIRE_INST;
    private Integer CO_DIST_INST; 
    private String NO_DIST_INST;
    private String NO_MONE_FACT;
    private Integer CO_MONE_FACT;
    private String DE_SIMB_MONE_FACT;
    private Double IM_AJUS_NIGV=0d;//no afecto
    private Boolean ST_AFECTO_DETRACCION=false;
    private Double IM_INST;
    private Double IM_ALQU;
    private Double IM_OTRO;
    private Double IM_AJUS_SIGV; 		
    private String DE_PERI; 			
    private Double IM_SERV_MENS; 		
    private Double IM_DIF_CARGO_FIJO; 	
    @DateTimeFormat(pattern = fnc.FORMAT_DATE_TIME_SEPARATOR)//"yyyy-MM-dd'T'HH:mm:ss")//mmss
    private Date FE_INI; 
    @DateTimeFormat(pattern = fnc.FORMAT_DATE_TIME_SEPARATOR)//"yyyy-MM-dd'T'HH:mm:ss")//mmss
    private Date FE_FIN; 				

    
    @Autowired
    public void setmapFACT(mapFACT mapFact){
        FACT.mapFact=mapFact;
    }     
    
    @Autowired
    public void setmapNEGO(mapNEGO mapNego){
        FACT.mapNego=mapNego;
    }
    
    @Autowired
    public void setmapFACT_GLOS(mapFACT_GLOS mapFact_Glos){
        FACT.mapFact_Glos=mapFact_Glos;
    }
    
    @Autowired
	public void setMapCLIE(mapCLIE mapCLIE) {
    	FACT.mapCLIE = mapCLIE;
	}
    
    /*GETS y SETS*/
    @JsonProperty(value = "IM_INST")
    public Double getIM_INST_ForJSON() {
        return IM_INST;
    }
    @JsonProperty(value = "IM_ALQU")
    public Double getIM_ALQU_ForJSON() {
        return IM_ALQU;
    }

    @JsonProperty(value = "IM_OTRO")
    public Double getIM_OTRO_ForJSON() {
        return IM_OTRO;
    }
    @JsonProperty("ST_AFECTO_DETRACCION")
    public Boolean getST_AFECTO_DETRACCION_ForJSON() {
        return this.ST_AFECTO_DETRACCION;
    }
    @JsonProperty("CO_FACT")
    public Long getCO_FACT_ForJSON() {
        return CO_FACT;
    }
    @JsonProperty("CO_CIER")
    public Integer getCO_CIER_ForJSON() {
        return CO_CIER;
    }
    @JsonProperty("CO_NEGO")
    public Integer getCO_NEGO_ForJSON() {
        return CO_NEGO;
    }
    @JsonProperty("DE_MONT")
    public String getDE_MONT_ForJSON() {
        return DE_MONT;
    }
    @JsonProperty("IM_VALO_VENT")
    public Double getIM_VALO_VENT_ForJSON() {
        return IM_VALO_VENT;
    }
    @JsonProperty("IM_VALO_DESC")
    public Double getIM_VALO_DESC_ForJSON() {
        return IM_VALO_DESC;
    }
    @JsonProperty("IM_VALO_VENT_NETO")
    public Double getIM_VALO_VENT_NETO_ForJSON() {
        return IM_VALO_VENT_NETO;
    }
    @JsonProperty("IM_IGV")
    public Double getIM_IGV_ForJSON() {
        return IM_IGV;
    }
    @JsonProperty("IM_TOTA")
    public Double getIM_TOTA_ForJSON() {
        return IM_TOTA;
    }
    @JsonProperty("ST_ELIM")
    public Boolean getST_ELIM_ForJSON() {
        return ST_ELIM;
    }
    @JsonProperty("ST_ANUL")
    public Boolean getST_ANUL_ForJSON() {
        return ST_ANUL;
    }    
    /*historico get*/
    @JsonProperty(value = "FE_EMIS")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = fnc.FORMAT_DATE)// "yyyy-MM-dd")
    public Date getFE_EMIS_ForJSON() {
        return FE_EMIS;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = fnc.FORMAT_DATE)// "yyyy-MM-dd")
    @JsonProperty(value = "FE_VENC")
    public Date getFE_VENC_ForJSON() {
        return FE_VENC;
    }

    @JsonProperty(value = "CO_CLIE")
    public Integer getCO_CLIE_ForJSON() {
        return CO_CLIE;
    }

    @JsonProperty(value = "DE_CODI_BUM")
    public String getDE_CODI_BUM_ForJSON() {
        return DE_CODI_BUM;
    }

    @JsonProperty(value = "NO_CLIE")
    public String getNO_CLIE_ForJSON() {
        return NO_CLIE;
    }

    @JsonProperty(value = "DE_DIRE_FISC")
    public String getDE_DIRE_FISC_ForJSON() {
        return DE_DIRE_FISC;
    }

    @JsonProperty(value = "CO_DIST_FISC")
    public Integer getCO_DIST_FISC_ForJSON() {
        return CO_DIST_FISC;
    }

    @JsonProperty(value = "DE_NUME_DOCU")
    public String getDE_NUME_DOCU_ForJSON() {
        return DE_NUME_DOCU;
    }

    @JsonProperty(value = "DE_DIRE_CORR")
    public String getDE_DIRE_CORR_ForJSON() {
        return DE_DIRE_CORR;
    }

    @JsonProperty(value = "DE_REF_DIRE_CORR")
    public String getDE_REF_DIRE_CORR_ForJSON() {
        return DE_REF_DIRE_CORR;
    }
    
    @JsonProperty(value = "CO_DIST_CORR")
    public Integer getCO_DIST_CORR_ForJSON() {
        return CO_DIST_CORR;
    }

    @JsonProperty(value = "DE_DIRE_INST")
    public String getDE_DIRE_INST_ForJSON() {
        return DE_DIRE_INST;
    }

    @JsonProperty(value = "CO_DIST_INST")
    public Integer getCO_DIST_INST_ForJSON() {
        return CO_DIST_INST;
    }

    @JsonProperty(value = "NO_MONE_FACT")
    public String getNO_MONE_FACT_ForJSON() {
        return NO_MONE_FACT;
    }

    @JsonProperty(value = "CO_MONE_FACT")
    public Integer getCO_MONE_FACT_ForJSON() {
        return CO_MONE_FACT;
    }    
    
    @JsonProperty(value = "NO_DIST_FISC")
    public String getNO_DIST_FISC_ForJSON() {
        return this.NO_DIST_FISC;
    }
    
    @JsonProperty(value = "CO_TIPO_DOCU")
    public Integer getCO_TIPO_DOCU_ForJSON() {
        return this.CO_TIPO_DOCU;
    }    
    
    @JsonProperty(value = "NO_TIPO_DOCU")
    public String getNO_TIPO_DOCU_ForJSON() {
        return this.NO_TIPO_DOCU;
    }
    @JsonProperty(value = "NO_DIST_CORR")
    public String getNO_DIST_CORR_ForJSON() {
        return this.NO_DIST_CORR;
    }
    @JsonProperty(value = "NO_DIST_INST")
    public String getNO_DIST_INST_ForJSON() {
        return this.NO_DIST_INST;
    }
    @JsonProperty(value = "DE_SIMB_MONE_FACT")
    public String getDE_SIMB_MONE_FACT_ForJSON() {
        return this.DE_SIMB_MONE_FACT;
    }    
    
    @JsonProperty(value = "IM_AJUS_NIGV")
    public Double getIM_AJUS_NIGV_ForJSON() {
        return IM_AJUS_NIGV;
    } 
    
    @JsonProperty(value = "IM_AJUS_SIGV")
    public Double getIM_AJUS_SIGV_ForJSON() {
        return IM_AJUS_SIGV;
    } 
    
    @JsonProperty(value = "DE_PERI")
    public String getDE_PERI_ForJSON() {
        return DE_PERI;
    }
    
    @JsonProperty(value = "IM_SERV_MENS")
    public Double getIM_SERV_MENS_ForJSON() {
        return IM_SERV_MENS;
    }
    
    @JsonProperty(value = "IM_DIF_CARGO_FIJO")
    public Double getIM_DIF_CARGO_FIJO_ForJSON() {
        return this.IM_DIF_CARGO_FIJO;
    }
    
    @JsonProperty(value = "FE_INI")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = fnc.FORMAT_DATE)// "yyyy-MM-dd")
    public Date getFE_INI_ForJSON() {
        return FE_INI;
    }
    
    @JsonProperty(value = "FE_FIN")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = fnc.FORMAT_DATE)// "yyyy-MM-dd")
    public Date getFE_FIN_ForJSON() {
        return FE_FIN;
    }
    
    public void setIM_AJUS_SIGV(Double IM_AJUS_SIGV) {
        this.IM_AJUS_SIGV = IM_AJUS_SIGV;
    }
    
    public void setDE_PERI(String DE_PERI) {
        this.DE_PERI = DE_PERI;
    }
    
    public void setIM_SERV_MENS(Double IM_SERV_MENS) {
        this.IM_SERV_MENS = IM_SERV_MENS;
    }
    
    public void setIM_DIF_CARGO_FIJO(Double IM_DIF_CARGO_FIJO) {
        this.IM_DIF_CARGO_FIJO = IM_DIF_CARGO_FIJO;
    }
    
    public void setFE_INI(Date FE_INI) {
        this.FE_INI = FE_INI;
    }
    
    public void setFE_FIN(Date FE_FIN) {
        this.FE_FIN = FE_FIN;
    }
    
    public void setIM_INST(Double IM_INST) {
        this.IM_INST = IM_INST;
    }
    
    public void setIM_ALQU(Double IM_ALQU) {
        this.IM_ALQU = IM_ALQU;
    }

    public void setIM_OTRO(Double IM_OTRO) {
        this.IM_OTRO = IM_OTRO;
    }
    
    public void setCO_FACT(Long CO_FACT) {
        this.CO_FACT = CO_FACT;
    }
    
    public void setST_AFECTO_DETRACCION(Boolean ST_AFECTO_DETRACCION) {
        this.ST_AFECTO_DETRACCION = ST_AFECTO_DETRACCION;
    }

    public void setCO_CIER(Integer CO_CIER) {
        this.CO_CIER = CO_CIER;
    }

    public void setCO_NEGO(Integer CO_NEGO) {
        this.CO_NEGO = CO_NEGO;
    }

    public void setDE_MONT(String DE_MONT) {
        this.DE_MONT = DE_MONT;
    }

    public void setIM_VALO_VENT(Double IM_VALO_VENT) {
        this.IM_VALO_VENT = IM_VALO_VENT;
    }

    public void setIM_VALO_DESC(Double IM_VALO_DESC) {
        this.IM_VALO_DESC = IM_VALO_DESC;
    }

    public void setIM_VALO_VENT_NETO(Double IM_VALO_VENT_NETO) {
        this.IM_VALO_VENT_NETO = IM_VALO_VENT_NETO;
    }

    public void setIM_IGV(Double IM_IGV) {
        this.IM_IGV = IM_IGV;
    }

    public void setIM_TOTA(Double IM_TOTA) {
        this.IM_TOTA = IM_TOTA;
    }

    public void setST_ELIM(Boolean ST_ELIM) {
        this.ST_ELIM = ST_ELIM;
    }
    
    public void setST_ANUL(Boolean ST_ANUL) {
        this.ST_ANUL = ST_ANUL;
    }    
    
    /*historico sets*/
    
    public void setFE_EMIS(Date FE_EMIS) {
        this.FE_EMIS = FE_EMIS;
    }

    public void setFE_VENC(Date FE_VENC) {
        this.FE_VENC = FE_VENC;
    }

    public void setCO_CLIE(Integer CO_CLIE) {
        this.CO_CLIE = CO_CLIE;
    }

    public void setDE_CODI_BUM(String DE_CODI_BUM) {
        this.DE_CODI_BUM = DE_CODI_BUM;
    }

    public void setNO_CLIE(String NO_CLIE) {
        this.NO_CLIE = NO_CLIE;
    }

    public void setDE_DIRE_FISC(String DE_DIRE_FISC) {
        this.DE_DIRE_FISC = DE_DIRE_FISC;
    }

    public void setCO_DIST_FISC(Integer CO_DIST_FISC) {
        this.CO_DIST_FISC = CO_DIST_FISC;
    }

    public void setCO_TIPO_DOCU(Integer CO_TIPO_DOCU) {
        this.CO_TIPO_DOCU = CO_TIPO_DOCU;
    }

    public void setDE_NUME_DOCU(String DE_NUME_DOCU) {
        this.DE_NUME_DOCU = DE_NUME_DOCU;
    }

    public void setDE_DIRE_CORR(String DE_DIRE_CORR) {
        this.DE_DIRE_CORR = DE_DIRE_CORR;
    }
    
    public void setDE_REF_DIRE_CORR(String DE_REF_DIRE_CORR) {
        this.DE_REF_DIRE_CORR = DE_REF_DIRE_CORR;
    }
    public void setCO_DIST_CORR(Integer CO_DIST_CORR) {
        this.CO_DIST_CORR = CO_DIST_CORR;
    }

    public void setDE_DIRE_INST(String DE_DIRE_INST) {
        this.DE_DIRE_INST = DE_DIRE_INST;
    }

    public void setCO_DIST_INST(Integer CO_DIST_INST) {
        this.CO_DIST_INST = CO_DIST_INST;
    }

    public void setNO_MONE_FACT(String NO_MONE_FACT) {
        this.NO_MONE_FACT = NO_MONE_FACT;
    }

    public void setCO_MONE_FACT(Integer CO_MONE_FACT) {
        this.CO_MONE_FACT = CO_MONE_FACT;
    }

    public void setNO_DIST_FISC(String NO_DIST_FISC) {
        this.NO_DIST_FISC = NO_DIST_FISC;
    }

    public void setNO_TIPO_DOCU(String NO_TIPO_DOCU) {
        this.NO_TIPO_DOCU = NO_TIPO_DOCU;
    }

    public void setNO_DIST_CORR(String NO_DIST_CORR) {
        this.NO_DIST_CORR = NO_DIST_CORR;
    }

    public void setNO_DIST_INST(String NO_DIST_INST) {
        this.NO_DIST_INST = NO_DIST_INST;
    }

    public void setDE_SIMB_MONE_FACT(String DE_SIMB_MONE_FACT) {
        this.DE_SIMB_MONE_FACT = DE_SIMB_MONE_FACT;
    }
    
    public void setIM_AJUS_NIGV(Double IM_AJUS_NIGV) {
        this.IM_AJUS_NIGV = IM_AJUS_NIGV;
    } 
    
    
    
    @JsonIgnore
    public CIER getCierre() {
        return mapCier.getById(this.CO_CIER);
    }
    
    @JsonIgnore
    public String anular() {
        String ret=null;
        CIER cier=this.getCierre();
        if (cier.getST_CIER_ForJSON()==2 || cier.getST_CIER_ForJSON()==3){//En cierre o termino cierre - SOLO AQUI SE PUEDEN EDITAR Recibos  
            mapFact.anular(this);
        }else{
            ret="No se puede anular un recibo de una ciclo ya cerrado.";
        }
        return ret;
    }
    
    @JsonIgnore
    public void generarReciboModificadoPDF(HttpServletResponse response) {
    	System.out.print("aaaaaaaaaaaaaaaaaaaaa");
        Factura fact=new Factura(this);
        FacturaModificadoPdf fp=new FacturaModificadoPdf(fact);
        fp.crearPdf();
        fp.cerrarCoss();
        fp.setName(fact.getNombreFactura());
        try{
        	System.out.print("bbbbbbb:" + response.getOutputStream());
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }    
        
        fp.save(response);
    }
    
    @JsonIgnore
    public void save() {
        mapFact.insertar(this);
    }
    
    @JsonIgnore
    public Boolean isClienteNuevo() {
        return mapNego.isNuevo(this.CO_NEGO);
    }

    /*EXTRAS*/
    /*
    Son valores que no se guardan en la BD pero se calcula cuando se llaman desde un @Result, es mucho mas rapido
    que consultar uno por uno.
    Para la asignacion no son necesarios los SETs solo se necesitan los GETs para obtenerlos desde donde lo vamos a necesitar.
    
    Ejemplo
    @Results(value={
        @Result(property = "dist_corr",column = "CO_DIST_CORR",javaType = DIST.class,one = @One(select = "com.americatel.facturacion.mappers.mapDIST.getId"),id = false),
        @Result(property = "dist_fisc",column = "CO_DIST_FISC",javaType = DIST.class,one = @One(select = "com.americatel.facturacion.mappers.mapDIST.getId"),id = false),
        @Result(property = "dist_inst",column = "CO_DIST_INST",javaType = DIST.class,one = @One(select = "com.americatel.facturacion.mappers.mapDIST.getId"),id = false),
        @Result(property = "cantidad_sucursales_facturaron",column = "CO_RECI",javaType = Integer.class,one = @One(select = "com.americatel.facturacion.mappers.mapRECI.getCantidadSucursalesFacturaron"),id = false)
    }    
    */
    private DIST dist_corr=null;
    private DIST dist_fisc=null;
    private DIST dist_inst=null;
    private Integer cantidad_sucursales_facturaron_select=null;     
    private Boolean is_afecto_detraccion=null;
    private String detalle_servicio=null;
    private CIER cier=null;
    private CLIE clie=null;
    private NEGO nego=null;
        
    @JsonProperty("CIER")
    public CIER getCier_ForJSON() {
        return cier;
    }
    public void setCIER(CIER cier) {
        this.cier = cier;
    }
    
    @JsonProperty("CLIE")
    public CLIE getClie_ForJSON() {
        return clie;
    }
    public void setCLIE(CLIE clie) {
        this.clie = clie;
    }
    @JsonProperty("NEGO")
    public NEGO getNego_ForJSON() {
        return nego;
    }
    public void setNEGO(NEGO nego) {
        this.nego = nego;
    }
    
    
    @JsonProperty("DIST_CORR")
    public DIST getDist_Corr_ForJSON() {
        return dist_corr;
    }
    
    @JsonProperty("is_afecto_detraccion")
    public Boolean isAfectoDetraccion() {
        return this.is_afecto_detraccion;
    }
    
    @JsonProperty("detalle_servicio")
    public String getDetalleServicio() {
        return this.detalle_servicio;
    }
    
    @JsonProperty("DIST_FISC")
    public DIST getDist_Fisc_ForJSON() {
        return dist_fisc;
    }  
    @JsonProperty("DIST_INST")
    public DIST getDist_Inst_ForJSON() {
        return dist_inst;
    }   
    
    @JsonProperty("cantidad_sucursales_facturaron_select")
    public Integer getCantidad_sucursales_facturaron_select() {
        return this.cantidad_sucursales_facturaron_select;
    }

    @JsonIgnore
    public Integer getCantidad_sucursales_facturaron(){
        if (this.CO_FACT!=null)
            return mapFact.getCantidadSucursalesFacturaronByCO_FACT(this.CO_FACT);
        return null;
    }
    
    @JsonIgnore
    public List<FACT_GLOS> getGlosas() {
        return mapFact_Glos.getByFACT(this.CO_FACT);
    }
    
    @JsonIgnore
    public Integer getCantidadSucursalesFacturaron(){
        Integer cantidad = mapFact.getCantidadSucursalesFacturaronByFact(this.CO_FACT);
        if(cantidad==null){
            cantidad=0;
        }
        
        return cantidad;
    }
    
    @JsonIgnore
    public void recalcular_totales(){
        Double neto=0d,ai=0d,igv=0d,nai=0d,total=0d;
        Double inst=0d,alqu=0d,otros=0d;
        Double descuentos=0d,renta_mensual=0d, diferencial_cargo_fijo=0d;
        List<FACT_GLOS> glosas=this.getGlosas() ;

        for(FACT_GLOS glos:glosas){
            String nombre=glos.getNO_GLOS_ForJSON();
            Double monto=glos.getIM_MONT_ForJSON();
            if (glos.getTI_GLOS_ForJSON()==Constante.GLOSA_TIPO_PLAN){ 
                neto=redondearPorDefecto(neto+monto);
                renta_mensual=redondearPorDefecto(renta_mensual+monto);
            }else if (glos.getTI_GLOS_ForJSON()==Constante.GLOSA_TIPO_SERVICIO_SUPLEMENTARIO){
                neto=redondearPorDefecto(neto+monto);
                if (nombre.toLowerCase().indexOf("instalaci")!=-1){
                    inst=redondearPorDefecto(inst+monto);
                }else if (nombre.toLowerCase().indexOf("alquiler")!=-1){
                    alqu=redondearPorDefecto(alqu+monto);
                }else{
                    otros=redondearPorDefecto(otros+monto);
                }
            }else if (glos.getTI_GLOS_ForJSON()==Constante.GLOSA_TIPO_SERVICIO_UNICO){
                neto=redondearPorDefecto(neto+Math.abs(monto));
                if (nombre.toLowerCase().indexOf("instalaci")!=-1){
                    inst=redondearPorDefecto(inst+monto);
                }else if (nombre.toLowerCase().indexOf("alquiler")!=-1){
                    alqu=redondearPorDefecto(alqu+monto);
                }else{
                    otros=redondearPorDefecto(otros+monto);
                }
            }else if (glos.getTI_GLOS_ForJSON()==Constante.GLOSA_TIPO_AJUSTE_AFECTO_IGV || 
                        glos.getTI_GLOS_ForJSON()==Constante.GLOSA_TIPO_DEVOLUCION_POR_CARGO_FIJO){
                ai=redondearPorDefecto(ai+Math.abs(monto)*-1);
            }else if (glos.getTI_GLOS_ForJSON()==Constante.GLOSA_TIPO_AJUSTE_NO_AFECTO_IGV){
                nai=redondearPorDefecto(nai+Math.abs(monto)*-1);
            }else if (glos.getTI_GLOS_ForJSON()==Constante.GLOSA_TIPO_DEVOLUCION_X_PROMOCION){
                descuentos=redondearPorDefecto(descuentos+monto*-1);
            }else if (glos.getTI_GLOS_ForJSON()==Constante.GLOSA_TIPO_DIFERENCIAL_CARGO_FIJO){
                neto=redondearPorDefecto(neto+monto);
                diferencial_cargo_fijo=redondearPorDefecto(diferencial_cargo_fijo+monto);
            }
        }

        igv     =   redondearPorDefecto((neto+ai)*Constante.IGV);
        total   =   redondearPorDefecto(neto+ai+igv+nai);  
        Double totales[];
        Double ajustes_por_redondeo=0d;
        if (Constante.SOLES == this.nego.getCO_MONE_FACT_ForJSON()){
               //redondeo a 0.00 o 0.05            
               Double tmp=com.americatel.facturacion.fncs.Numero.redondear(total,2);
               Double tmpEnt=Math.floor(tmp);
               Double tmpDiff=com.americatel.facturacion.fncs.Numero.redondear(tmp-tmpEnt,2)*10;
               tmpEnt=Math.floor(tmpDiff);
               tmpDiff=com.americatel.facturacion.fncs.Numero.redondear(tmpDiff-tmpEnt,1);

               if (tmpDiff!=0.0d && tmpDiff!=0.5d){
                   tmp=com.americatel.facturacion.fncs.Numero.redondear(total*10,2);
                   tmpEnt=Math.floor(tmp);
                   tmpDiff=com.americatel.facturacion.fncs.Numero.redondear(tmp-tmpEnt,2);
                   if (tmpDiff>=0.5d){
                       ajustes_por_redondeo=(com.americatel.facturacion.fncs.Numero.redondear((tmpDiff-0.5d)/10,2))*-1;
                   }else{
                       ajustes_por_redondeo=(com.americatel.facturacion.fncs.Numero.redondear((tmpDiff)/10,2))*-1;
                   }
                   nai=com.americatel.facturacion.fncs.Numero.redondear(nai+ajustes_por_redondeo,2);
                   descuentos=com.americatel.facturacion.fncs.Numero.redondear(descuentos+Math.abs(ajustes_por_redondeo),2)*-1;
               }
               total   =   com.americatel.facturacion.fncs.Numero.redondear(neto+ai+igv+nai,2);
        } 

        totales=new Double[]{neto,ai,igv,nai,total,inst,alqu,otros,descuentos,renta_mensual,diferencial_cargo_fijo,ajustes_por_redondeo};         
    
        this.setIM_VALO_VENT(totales[0]);
        this.setIM_AJUS_SIGV(totales[1]);
        this.setIM_IGV(totales[2]);        
        this.setIM_AJUS_NIGV(totales[3]);        
        this.setIM_TOTA(totales[4]);
        this.setIM_INST(totales[5]);
        this.setIM_ALQU(totales[6]);
        this.setIM_OTRO(totales[7]);
        this.setIM_VALO_DESC(totales[8]);
        this.setIM_SERV_MENS(totales[9]);
        this.setIM_DIF_CARGO_FIJO(totales[10]);
        this.setIM_VALO_VENT_NETO(totales[0]);
    
    }
    
    @JsonIgnore
    public void updateMontos() {
        mapFact.updateMontos(this);
    }
    
    private Double redondearPorDefecto(Double n){return com.americatel.facturacion.fncs.Numero.redondear(n,2);}
    
    @JsonIgnore
    public CLIE obtenerCliente(){
    	return this.mapCLIE.getId(this.getCO_CLIE_ForJSON());
    }
}
