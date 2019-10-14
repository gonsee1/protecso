/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.models;

import com.americatel.facturacion.Utils.Constante;
import com.americatel.facturacion.core3.Recibo;
import com.americatel.facturacion.core3.ReciboModificadoPdf;
import com.americatel.facturacion.fncs.fnc;
import com.americatel.facturacion.historial.Historial;
import com.americatel.facturacion.mappers.mapBOLE;
import com.americatel.facturacion.mappers.mapCIER;
import com.americatel.facturacion.mappers.mapCLIE;
import com.americatel.facturacion.mappers.mapDIST;
import com.americatel.facturacion.mappers.mapNEGO;
import com.americatel.facturacion.mappers.mapRECI_GLOS;
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
 * 
 * Factura
 */
@Component
public class BOLE  extends Historial {
    private static mapBOLE mapBole=null;    
    private static mapCIER mapCier=null;  
    private static mapDIST mapDist=null;
    private static mapNEGO mapNego=null;
    private static mapRECI_GLOS mapReci_Glos=null;
    private static mapCLIE mapCLIE=null;
    
    private Long CO_BOLE;
    private Integer CO_CIER;
    private Integer CO_NEGO;
    private String DE_PERI;
    private Double IM_NETO;
    private Double IM_AJUS_SIGV=0d;//si afecto
    private Double IM_IGV;
    private Double IM_TOTA;
    private Double IM_AJUS_NIGV=0d;//no afecto
    
    private Double IM_DIF_CARGO_FIJO=0d; //Diferencial de cargos fijos
    
    private Double IM_INST;
    private Double IM_SERV_MENS;
    private Double IM_ALQU;
    private Double IM_OTRO;
    private Double IM_DESC;
    private Boolean ST_ELIM=false;
    private Boolean ST_ANUL=false;
    
    /*Historico*/
    @DateTimeFormat(pattern = fnc.FORMAT_DATE_TIME_SEPARATOR)//"yyyy-MM-dd'T'HH:mm:ss")//mmss
    private Date FE_EMIS;
    @DateTimeFormat(pattern = fnc.FORMAT_DATE_TIME_SEPARATOR)//"yyyy-MM-dd'T'HH:mm:ss")//mmss
    private Date FE_VENC;
    @DateTimeFormat(pattern = fnc.FORMAT_DATE_TIME_SEPARATOR)//"yyyy-MM-dd'T'HH:mm:ss")//mmss
    private Date FE_INI; //Periodo Inicio
    @DateTimeFormat(pattern = fnc.FORMAT_DATE_TIME_SEPARATOR)//"yyyy-MM-dd'T'HH:mm:ss")//mmss
    private Date FE_FIN; //Periodo Fin
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
    
    @Autowired
    public void setmapRECI_GLOS(mapRECI_GLOS mapReci_Glos){
        BOLE.mapReci_Glos=mapReci_Glos;
    }
    
    @Autowired
    public void setmapDIST(mapDIST mapDist){
        BOLE.mapDist=mapDist;
    }     
    
    @Autowired
    public void setmapBole(mapBOLE mapBole){
        BOLE.mapBole=mapBole;
    }    
    @Autowired
    public void setmapCIER(mapCIER mapCier){
        BOLE.mapCier=mapCier;
    }      
    
    @Autowired
    public void setmapNEGO(mapNEGO mapNego){
        BOLE.mapNego=mapNego;
    }
    
    @Autowired
	public void setMapCLIE(mapCLIE mapCLIE) {
    	BOLE.mapCLIE = mapCLIE;
	}
    
    /*GETS y SETS*/
    
    @JsonProperty("ST_ANUL")
    public Boolean getST_ANUL_ForJSON() {
        return ST_ANUL;
    }     
    
    @JsonProperty("ST_ELIM")
    public Boolean getST_ELIM_ForJSON() {
        return ST_ELIM;
    }      
    
    @JsonProperty(value = "CO_BOLE")
    public Long getCO_BOLE_ForJSON() {
        return CO_BOLE;
    }

    @JsonProperty(value = "CO_CIER")
    public Integer getCO_CIER_ForJSON() {
        return CO_CIER;
    }

    @JsonProperty(value = "CO_NEGO")
    public Integer getCO_NEGO_ForJSON() {
        return CO_NEGO;
    }

    @JsonProperty(value = "DE_PERI")
    public String getDE_PERI_ForJSON() {
        return DE_PERI;
    }

    @JsonProperty(value = "IM_NETO")
    public Double getIM_NETO_ForJSON() {
        return IM_NETO;
    }
    
    @JsonProperty(value = "IM_AJUS_SIGV")
    public Double getIM_AJUS_SIGV_ForJSON() {
        return IM_AJUS_SIGV;
    }    

    @JsonProperty(value = "IM_IGV")
    public Double getIM_IGV_ForJSON() {
        return IM_IGV;
    }

    @JsonProperty(value = "IM_TOTA")
    public Double getIM_TOTA_ForJSON() {
        return IM_TOTA;
    }
    
    @JsonProperty(value = "IM_AJUS_NIGV")
    public Double getIM_AJUS_NIGV_ForJSON() {
        return IM_AJUS_NIGV;
    }     

    @JsonProperty(value = "IM_INST")
    public Double getIM_INST_ForJSON() {
        return IM_INST;
    }

    @JsonProperty(value = "IM_SERV_MENS")
    public Double getIM_SERV_MENS_ForJSON() {
        return IM_SERV_MENS;
    }
        
    @JsonProperty(value = "IM_ALQU")
    public Double getIM_ALQU_ForJSON() {
        return IM_ALQU;
    }

    @JsonProperty(value = "IM_OTRO")
    public Double getIM_OTRO_ForJSON() {
        return IM_OTRO;
    }

    @JsonProperty(value = "IM_DESC")
    public Double getIM_DESC_ForJSON() {
        return IM_DESC;
    }

    /*historico get*/
    @JsonProperty(value = "FE_EMIS")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = fnc.FORMAT_DATE)// "yyyy-MM-dd")
    public Date getFE_EMIS_ForJSON() {
        return FE_EMIS;
    }

    @JsonProperty(value = "FE_VENC")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = fnc.FORMAT_DATE)// "yyyy-MM-dd")
    public Date getFE_VENC_ForJSON() {
        return FE_VENC;
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
    @JsonProperty(value = "CO_TIPO_DOCU")
    public Integer getCO_TIPO_DOCU_ForJSON() {
        return this.CO_TIPO_DOCU;
    }      
    @JsonProperty(value = "NO_DIST_FISC")
    public String getNO_DIST_FISC_ForJSON() {
        return this.NO_DIST_FISC;
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
    @JsonProperty(value = "IM_DIF_CARGO_FIJO")
    public Double getIM_DIF_CARGO_FIJO_ForJSON() {
        return this.IM_DIF_CARGO_FIJO;
    }
    
    public void setIM_DIF_CARGO_FIJO(Double IM_DIF_CARGO_FIJO) {
        this.IM_DIF_CARGO_FIJO = IM_DIF_CARGO_FIJO;
    }
    
    public void setCO_BOLE(Long CO_BOLE) {
        this.CO_BOLE = CO_BOLE;
    }

    public void setCO_CIER(Integer CO_CIER) {
        this.CO_CIER = CO_CIER;
    }

    public void setCO_NEGO(Integer CO_NEGO) {
        this.CO_NEGO = CO_NEGO;
    }

    public void setDE_PERI(String DE_PERI) {
        this.DE_PERI = DE_PERI;
    }

    public void setIM_NETO(Double IM_NETO) {
        this.IM_NETO = IM_NETO;
    }
    
    
    public void setIM_AJUS_SIGV(Double IM_AJUS_SIGV) {
        this.IM_AJUS_SIGV = IM_AJUS_SIGV;
    }    

    public void setIM_IGV(Double IM_IGV) {
        this.IM_IGV = IM_IGV;
    }

    public void setIM_TOTA(Double IM_TOTA) {
        this.IM_TOTA = IM_TOTA;
    }

    public void setIM_AJUS_NIGV(Double IM_AJUS_NIGV) {
        this.IM_AJUS_NIGV = IM_AJUS_NIGV;
    }     
    
    public void setIM_INST(Double IM_INST) {
        this.IM_INST = IM_INST;
    }

    public void setIM_SERV_MENS(Double IM_SERV_MENS) {
        this.IM_SERV_MENS = IM_SERV_MENS;
    }

    public void setIM_ALQU(Double IM_ALQU) {
        this.IM_ALQU = IM_ALQU;
    }

    public void setIM_OTRO(Double IM_OTRO) {
        this.IM_OTRO = IM_OTRO;
    }

    public void setIM_DESC(Double IM_DESC) {
        this.IM_DESC = IM_DESC;
    }
    public void setST_ELIM(Boolean ST_ELIM) {
        this.ST_ELIM = ST_ELIM;
    }     
    /*historico sets*/

    public void setFE_EMIS(Date FE_EMIS) {
        this.FE_EMIS = FE_EMIS;
    }

    public void setFE_VENC(Date FE_VENC) {
        this.FE_VENC = FE_VENC;
    }
    
    public void setFE_INI(Date FE_INI) {
        this.FE_INI = FE_INI;
    }
    
    public void setFE_FIN(Date FE_FIN) {
        this.FE_FIN = FE_FIN;
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
    
    public void setST_ANUL(Boolean ST_ANUL) {
        this.ST_ANUL = ST_ANUL;
    }  
    
    @JsonIgnore
    public void save() {
        mapBole.insertar(this);
    }
    
    @JsonIgnore
    public NEGO getNegocio(){
        return mapBole.getNegocio(this);
    }

    @JsonIgnore
    public SUCU getPrimeraSucursalFacturada() {
        return mapBole.getPrimerSucursalFacturada(this);
    }
    
    @JsonIgnore
    public Integer getCantidadSucursalesFacturaron(){
        Integer cantidad = mapBole.getCantidadSucursalesFacturaronByBole(this.CO_BOLE);
        if(cantidad==null){
            cantidad=0;
        }
        
        return cantidad;
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
            mapBole.anular(this);
        }else{
            ret="No se puede anular una boleta de un ciclo ya cerrado.";
        }
        return ret;
    }

    @JsonIgnore
    public void generarBoletaModificadoPDF(HttpServletResponse response) {
        Recibo reci=new Recibo(null,this);
        ReciboModificadoPdf fp=new ReciboModificadoPdf(reci);
        fp.crearPdf();
        fp.cerrarCoss();
        fp.setName(reci.getNombreBoleta());
        fp.save(response);
        
        /*ReciboPdf fp=reci.getReciboPdf();
        fp.crearPdf();
        fp.save(response);*/
        
    }

    @JsonIgnore
    public DIST getDistritoCorrespondencia() {
        return mapDist.getId(this.CO_DIST_CORR);
    }

    @JsonIgnore
    public List<RECI_GLOS> getGlosas() {
        return mapReci_Glos.getByBOLE(this.CO_BOLE);
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
        @Result(property = "cantidad_sucursales_facturaron",column = "CO_RECI",javaType = Integer.class,one = @One(select = "com.americatel.facturacion.mappers.mapBole.getCantidadSucursalesFacturaron"),id = false)
    }    
    */
    private DIST dist_corr=null;
    private DIST dist_fisc=null;
    private DIST dist_inst=null;
    private Integer cantidad_sucursales_facturaron=null; 
    private Boolean is_afecto_detraccion=null;
    private String detalle_servicio=null;
    
    private CIER cier=null;
    private CLIE clie=null;
    private NEGO nego=null;
    
    @JsonProperty("cantidad_sucursales_facturaron")
    public Integer getCantidad_sucursales_facturaron() {
        return this.cantidad_sucursales_facturaron;
    }
    @JsonProperty("is_afecto_detraccion")
    public Boolean isAfectoDetraccion() {
        return this.is_afecto_detraccion;
    }    
    @JsonProperty("detalle_servicio")
    public String getDetalleServicio() {
        return this.detalle_servicio;
    }
    
    @JsonProperty("DIST_CORR")
    public DIST getDist_Corr_ForJSON() {
        return dist_corr;
    }
    @JsonProperty("DIST_FISC")
    public DIST getDist_Fisc_ForJSON() {
        return dist_fisc;
    }  
    @JsonProperty("DIST_INST")
    public DIST getDist_Inst_ForJSON() {
        return dist_inst;
    } 
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
    
    @JsonIgnore
    public void updateMontos() {
        BOLE.mapBole.updateMontos(this);
    }
    
    @JsonIgnore
    public CIER_DATA_PLAN getPlanFromDiferencial() {
        return BOLE.mapBole.getPlanFromDiferencial(this.CO_BOLE);
    }
    
    public CIER_DATA_PLAN getPlanDescription() {
        return BOLE.mapBole.getPlanDescription(this.CO_BOLE);
    }
    
    @JsonIgnore
    public Integer getCantidadPuntos() {
        return BOLE.mapBole.getCantidadPuntos(this.CO_BOLE);
    }
    
//Funcion para recalcular los montos del recibo en el mantenimiento del recibo
    @JsonIgnore
    public void recalcular_totales(){
        Double neto=0d,ai=0d,igv=0d,nai=0d,total=0d;
        Double inst=0d,alqu=0d,otros=0d;
        Double descuentos=0d,renta_mensual=0d, diferencial_cargo_fijo=0d;
        List<RECI_GLOS> glosasReci=this.getGlosas();
        
        for(RECI_GLOS glos:glosasReci){
            String nombre=glos.getNO_GLOS_ForJSON();
            Double monto=glos.getIM_MONT_ForJSON();
            if (glos.getTI_GLOS_ForJSON()==1){
                neto=redondearPorDefecto(neto+monto);
                renta_mensual=redondearPorDefecto(renta_mensual+monto);
            }else if (glos.getTI_GLOS_ForJSON()==2){
                neto=redondearPorDefecto(neto+monto);
                if (nombre.toLowerCase().indexOf("instalaci")!=-1){
                    inst=redondearPorDefecto(inst+monto);
                }else if (nombre.toLowerCase().indexOf("alquiler")!=-1){
                    alqu=redondearPorDefecto(alqu+monto);
                }else{
                    otros=redondearPorDefecto(otros+monto);
                }
            }else if (glos.getTI_GLOS_ForJSON()==3){
                neto=redondearPorDefecto(neto+Math.abs(monto));
                if (nombre.toLowerCase().indexOf("instalaci")!=-1){
                    inst=redondearPorDefecto(inst+monto);
                }else if (nombre.toLowerCase().indexOf("alquiler")!=-1){
                    alqu=redondearPorDefecto(alqu+monto);
                }else{
                    otros=redondearPorDefecto(otros+monto);
                }
            }else if (glos.getTI_GLOS_ForJSON()==4 || glos.getTI_GLOS_ForJSON()==7){
                ai=redondearPorDefecto(ai+Math.abs(monto)*-1);
            }else if (glos.getTI_GLOS_ForJSON()==5){
                nai=redondearPorDefecto(nai+Math.abs(monto)*-1);
            }else if (glos.getTI_GLOS_ForJSON()==6){
                descuentos=redondearPorDefecto(descuentos+monto*-1);
            }else if (glos.getTI_GLOS_ForJSON()==8){
                neto=redondearPorDefecto(neto+monto);
                diferencial_cargo_fijo=redondearPorDefecto(diferencial_cargo_fijo+monto);
            }
        }

        igv=redondearPorDefecto((neto+ai)*Constante.IGV);            
        total=redondearPorDefecto(neto+ai+igv+nai);   
        Double totales[];
        Double ajustes_por_redondeo;
        totales=new Double[]{neto,ai,igv,nai,total,inst,alqu,otros,descuentos,renta_mensual,diferencial_cargo_fijo};                
        if (this.getCO_MONE_FACT_ForJSON()==1){ //Soles
            ajustes_por_redondeo=0d;
            //redondeo a 0.00 o 0.05            
            Double tmp=com.americatel.facturacion.fncs.Numero.redondear(totales[4],2);
            Double tmpEnt=Math.floor(tmp);
            Double tmpDiff=com.americatel.facturacion.fncs.Numero.redondear(tmp-tmpEnt,2)*10;
            tmpEnt=Math.floor(tmpDiff);
            tmpDiff=com.americatel.facturacion.fncs.Numero.redondear(tmpDiff-tmpEnt,1);

            if (tmpDiff!=0.0d && tmpDiff!=0.5d){
                tmp=com.americatel.facturacion.fncs.Numero.redondear(totales[4]*10,2);
                tmpEnt=Math.floor(tmp);
                tmpDiff=com.americatel.facturacion.fncs.Numero.redondear(tmp-tmpEnt,2);
                if (tmpDiff>=0.5d)
                    ajustes_por_redondeo=(com.americatel.facturacion.fncs.Numero.redondear((tmpDiff-0.5d)/10,2))*-1;
                else
                    ajustes_por_redondeo=(com.americatel.facturacion.fncs.Numero.redondear((tmpDiff)/10,2))*-1;
                totales[3]=com.americatel.facturacion.fncs.Numero.redondear(totales[3]+ajustes_por_redondeo,2);
                totales[8]=com.americatel.facturacion.fncs.Numero.redondear(totales[8]+Math.abs(ajustes_por_redondeo),2)*-1;
            }                
            totales[4]=com.americatel.facturacion.fncs.Numero.redondear(totales[0]+totales[1]+totales[2]+totales[3],2); 
        }
        
        this.setIM_NETO(totales[0]);
        this.setIM_AJUS_SIGV(totales[1]);
        this.setIM_IGV(totales[2]);
        this.setIM_AJUS_NIGV(totales[3]);
        this.setIM_TOTA(totales[4]);  
        this.setIM_INST(totales[5]);                
        this.setIM_ALQU(totales[6]);
        this.setIM_OTRO(totales[7]);
        this.setIM_DESC(totales[8]);
        this.setIM_SERV_MENS(totales[9]);
        this.setIM_DIF_CARGO_FIJO(totales[10]);                
        
    }
    private Double redondearPorDefecto(Double n){return com.americatel.facturacion.fncs.Numero.redondear(n,2);}
    
    @JsonIgnore
    public CLIE obtenerCliente(){
    	return this.mapCLIE.getId(this.getCO_CLIE_ForJSON());
    }
}

