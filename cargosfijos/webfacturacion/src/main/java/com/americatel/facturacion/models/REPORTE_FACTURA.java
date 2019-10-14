/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.models;

import com.americatel.facturacion.historial.Historial;
import com.americatel.facturacion.mappers.mapAJUS;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author crodas
 */
@Component
public class REPORTE_FACTURA extends Historial {
  
    private String CAMPO_1;
    private String CAMPO_2;
    private String CAMPO_3;
    private String CAMPO_4;
    private String CAMPO_5;
    private String DISTRITO;
    private String PROVINCIA;
    private String DEPARTAMENTO;
    private String RUC;
    private String FECHA_VENCIMIENTO;
    private String  DESCRIPCION;
    private String CANTIDAD;
    private String VALOR_UNITARIO;
    private String DESCUENTO;
    private String TOTAL;
    private String OP_GRAVADAS;
    private String OP_INAFECTAS;
    private String OP_EXONERADAS;
    private String OP_GRATUITAS;
    private String DESCUENTOS_GLOBALES;
    private String IGV;
    private String OTROS_TRIBUTOS;
    private String OTROS_CARGOS;
    private String TOTAL_2;
    private String AJUSTE_REDONDEO;
    private String PERIODO;
    private String NEGOCIO;       
    private String REFERENCIA;
    private String TIPO_MONEDA;
    private String REFERENCIAS_ADICIONALES_1;
    private String REFERENCIAS_ADICIONALES_2;
    private String REFERENCIAS_ADICIONALES_3;
    private String REFERENCIAS_ADICIONALES_4;
    private String GLOSA_DETRACCION;
    private String IGV_2;
    private String SERVICIO;
    private String ORDEN_COMPRA;
    private String TIPO_OPERACION;
    private String TIPO_DOC_IDENTIDAD;
    private String TIPO_AFECT_IGV;
    private String TIPO_PRECIO;

    @JsonProperty("CAMPO_1")
    public String getCAMPO_1_ForJSON() {
        return CAMPO_1 ;
    }
    
    public void setCAMPO_1 (String CAMPO_1) {
      this.CAMPO_1  = CAMPO_1 ;
    }
    
    @JsonProperty("CAMPO_2")
    public String getCAMPO_2_ForJSON() {
        return CAMPO_2 ;
    }
    
    public void setCAMPO_2 (String CAMPO_2) {
      this.CAMPO_2  = CAMPO_2 ;
    }
    
    
    @JsonProperty("CAMPO_3")
    public String getCAMPO_3_ForJSON() {
        return CAMPO_3 ;
    }
    
    public void setCAMPO_3 (String CAMPO_3) {
      this.CAMPO_3  = CAMPO_3 ;
    }
    
    
    
    @JsonProperty("CAMPO_4")
    public String getCAMPO_4_ForJSON() {
        return CAMPO_4 ;
    }
    
    public void setCAMPO_4 (String CAMPO_4) {
      this.CAMPO_4  = CAMPO_4 ;
    }
    
    
    @JsonProperty("CAMPO_5")
    public String getCAMPO_5_ForJSON() {
        return CAMPO_5 ;
    }
    
    public void setCAMPO_5 (String CAMPO_5) {
      this.CAMPO_5  = CAMPO_5 ;
    }
    
    
    
    @JsonProperty("DISTRITO")
    public String getDISTRITO_ForJSON() {
        return DISTRITO  ;
    }
    
    public void setDISTRITO(String DISTRITO ) {
      this.DISTRITO   = DISTRITO  ;
    }
    
    @JsonProperty("PROVINCIA")
    public String getPROVINCIA_ForJSON() {
        return PROVINCIA   ;
    }
    
    public void setPROVINCIA(String PROVINCIA  ) {
      this.PROVINCIA    = PROVINCIA   ;
    }
    
    
        
    @JsonProperty("DEPARTAMENTO")
    public String getDEPARTAMENTO_ForJSON() {
        return DEPARTAMENTO    ;
    }
    
    public void setDEPARTAMENTO (String DEPARTAMENTO   ) {
      this.DEPARTAMENTO     = DEPARTAMENTO    ;
    }
    
    @JsonProperty("RUC")
    public String getRUC_ForJSON() {
        return RUC    ;
    }
    
    public void setRUC (String RUC   ) {
      this.RUC     = RUC    ;
    }
    
    
    @JsonProperty("FECHA_VENCIMIENTO")
    public String getFECHA_VENCIMIENTO_ForJSON() {
        return FECHA_VENCIMIENTO    ;
    }
    
    public void setFECHA_VENCIMIENTO (String FECHA_VENCIMIENTO   ) {
      this.FECHA_VENCIMIENTO     = FECHA_VENCIMIENTO    ;
    }
    
    
    
    @JsonProperty("DESCRIPCION")
    public String getDESCRIPCION_ForJSON() {
        return DESCRIPCION    ;
    }
    
    public void setDESCRIPCION (String DESCRIPCION   ) {
      this.DESCRIPCION     = DESCRIPCION    ;
    }
    
    @JsonProperty("CANTIDAD")
    public String getCANTIDAD_ForJSON() {
        return CANTIDAD    ;
    }
    
    public void setCANTIDAD (String CANTIDAD   ) {
      this.CANTIDAD     = CANTIDAD    ;
    }
    
    
    @JsonProperty("VALOR_UNITARIO")
    public String getVALOR_UNITARIO_ForJSON() {
        return VALOR_UNITARIO    ;
    }
    
    public void setVALOR_UNITARIO (String VALOR_UNITARIO   ) {
      this.VALOR_UNITARIO     = VALOR_UNITARIO    ;
    }
    
    
    @JsonProperty("DESCUENTO")
    public String getDESCUENTO_ForJSON() {
        return DESCUENTO    ;
    }
    
    public void setDESCUENTO (String DESCUENTO   ) {
      this.DESCUENTO     = DESCUENTO    ;
    }
    
    
    
    @JsonProperty("TOTAL")
    public String getTOTAL_ForJSON() {
        return TOTAL    ;
    }
    
    public void setTOTAL (String TOTAL   ) {
      this.TOTAL     = TOTAL    ;
    }
    
    
    @JsonProperty("OP_GRAVADAS")
    public String getOP_GRAVADAS_ForJSON() {
        return OP_GRAVADAS    ;
    }
    
    public void setOP_GRAVADAS (String OP_GRAVADAS   ) {
      this.OP_GRAVADAS     = OP_GRAVADAS    ;
    }
    
    @JsonProperty("OP_INAFECTAS")
    public String getOP_INAFECTAS_ForJSON() {
        return OP_INAFECTAS    ;
    }
    
    public void setOP_INAFECTAS (String OP_INAFECTAS   ) {
      this.OP_INAFECTAS     = OP_INAFECTAS    ;
    }
    
    @JsonProperty("OP_EXONERADAS")
    public String getOP_EXONERADAS_ForJSON() {
        return OP_EXONERADAS    ;
    }
    
    public void setOP_EXONERADAS (String OP_EXONERADAS   ) {
      this.OP_EXONERADAS     = OP_EXONERADAS    ;
    }
    
    @JsonProperty("OP_GRATUITAS")
    public String getOP_GRATUITAS_ForJSON() {
        return OP_GRATUITAS    ;
    }
    
    public void setOP_GRATUITAS (String OP_GRATUITAS   ) {
      this.OP_GRATUITAS     = OP_GRATUITAS    ;
    }
    
    
    @JsonProperty("DESCUENTOS_GLOBALES")
    public String getDESCUENTOS_GLOBALES_ForJSON() {
        return DESCUENTOS_GLOBALES    ;
    }
    
    public void setDESCUENTOS_GLOBALES (String DESCUENTOS_GLOBALES   ) {
      this.DESCUENTOS_GLOBALES     = DESCUENTOS_GLOBALES    ;
    }
    
    
    @JsonProperty("IGV")
    public String getIGV_ForJSON() {
        return IGV    ;
    }
    
    public void setIGV (String IGV   ) {
      this.IGV     = IGV    ;
    }
    
    @JsonProperty("OTROS_TRIBUTOS")
    public String getOTROS_TRIBUTOS_ForJSON() {
        return OTROS_TRIBUTOS    ;
    }
    
    public void setOTROS_TRIBUTOS (String OTROS_TRIBUTOS   ) {
      this.OTROS_TRIBUTOS     = OTROS_TRIBUTOS    ;
    }
    
      
    @JsonProperty("OTROS_CARGOS")
    public String getOTROS_CARGOS_ForJSON() {
        return OTROS_CARGOS    ;
    }
    
    public void setOTROS_CARGOS (String OTROS_CARGOS   ) {
      this.OTROS_CARGOS     = OTROS_CARGOS    ;
    }
    
         
    @JsonProperty("TOTAL_2")
    public String getTOTAL_2_ForJSON() {
        return TOTAL_2    ;
    }
    
    public void setTOTAL_2 (String TOTAL_2   ) {
      this.TOTAL_2     = TOTAL_2    ;
    }
    
    @JsonProperty("AJUSTE_REDONDEO")
    public String getAJUSTE_REDONDEO_ForJSON() {
        return AJUSTE_REDONDEO    ;
    }
    
    public void setAJUSTE_REDONDEO (String AJUSTE_REDONDEO   ) {
      this.AJUSTE_REDONDEO     = AJUSTE_REDONDEO    ;
    }
    
    
    @JsonProperty("PERIODO")
    public String getPERIODO_ForJSON() {
        return PERIODO    ;
    }
    
    public void setPERIODO (String PERIODO   ) {
      this.PERIODO     = PERIODO    ;
    }
    
    
    @JsonProperty("NEGOCIO")
    public String getNEGOCIO_ForJSON() {
        return NEGOCIO    ;
    }
    
    public void setNEGOCIO(String NEGOCIO           ) {
      this.NEGOCIO = NEGOCIO            ;
    }
    
    
    @JsonProperty("REFERENCIA")
    public String getREFERENCIA_ForJSON() {
        return REFERENCIA    ;
    }
    
    public void setREFERENCIA(String REFERENCIA           ) {
      this.REFERENCIA = REFERENCIA            ;
    }
    
    @JsonProperty("TIPO_MONEDA")
    public String getTIPO_MONEDA_ForJSON() {
        return TIPO_MONEDA    ;
    }
    
    public void setTIPO_MONEDA(String TIPO_MONEDA           ) {
      this.TIPO_MONEDA = TIPO_MONEDA            ;
    }
    
    @JsonProperty("REFERENCIAS_ADICIONALES_1")
    public String getREFERENCIAS_ADICIONALES_1_ForJSON() {
        return REFERENCIAS_ADICIONALES_1    ;
    }
    
    public void setREFERENCIAS_ADICIONALES_1(String REFERENCIAS_ADICIONALES_1) {
      this.REFERENCIAS_ADICIONALES_1 = REFERENCIAS_ADICIONALES_1            ;
    }
    
    
    @JsonProperty("REFERENCIAS_ADICIONALES_2")
    public String getREFERENCIAS_ADICIONALES_2_ForJSON() {
        return REFERENCIAS_ADICIONALES_2    ;
    }
    
    public void setREFERENCIAS_ADICIONALES_2(String REFERENCIAS_ADICIONALES_2) {
      this.REFERENCIAS_ADICIONALES_2 = REFERENCIAS_ADICIONALES_2;
    }
    
    @JsonProperty("REFERENCIAS_ADICIONALES_3")
    public String getREFERENCIAS_ADICIONALES_3_ForJSON() {
        return REFERENCIAS_ADICIONALES_3    ;
    }
    
    public void setREFERENCIAS_ADICIONALES_3(String REFERENCIAS_ADICIONALES_3) {
      this.REFERENCIAS_ADICIONALES_3 = REFERENCIAS_ADICIONALES_3;
    }
    
    @JsonProperty("REFERENCIAS_ADICIONALES_4")
    public String getREFERENCIAS_ADICIONALES_4_ForJSON() {
        return REFERENCIAS_ADICIONALES_4    ;
    }
    
    public void setREFERENCIAS_ADICIONALES_4(String REFERENCIAS_ADICIONALES_4) {
      this.REFERENCIAS_ADICIONALES_4 = REFERENCIAS_ADICIONALES_4;
    }
    
    @JsonProperty("GLOSA_DETRACCION")
    public String getGLOSA_DETRACCION_ForJSON() {
        return GLOSA_DETRACCION    ;
    }
    
    public void setGLOSA_DETRACCION(String GLOSA_DETRACCION) {
      this.GLOSA_DETRACCION = GLOSA_DETRACCION;
    }
    
    @JsonProperty("IGV_2")
    public String getIGV_2_ForJSON() {
        return IGV_2    ;
    }
    
    public void setIGV_2(String IGV_2) {
      this.IGV_2 = IGV_2;
    }
    
    @JsonProperty("SERVICIO")
    public String getSERVICIO_ForJSON() {
        return SERVICIO    ;
    }
    
    public void setSERVICIO(String SERVICIO) {
      this.SERVICIO = SERVICIO;
    }
    
    @JsonProperty("ORDEN_COMPRA")
    public String getORDEN_COMPRA_ForJSON() {
        return ORDEN_COMPRA    ;
    }
    
    public void setORDEN_COMPRA(String ORDEN_COMPRA) {
      this.ORDEN_COMPRA = ORDEN_COMPRA;
    }

    @JsonProperty("TIPO_OPERACION")
    public String getTIPO_OPERACION_ForJSON() {
        return TIPO_OPERACION    ;
    }
    
    public void setTIPO_OPERACION(String TIPO_OPERACION) {
      this.TIPO_OPERACION = TIPO_OPERACION;
    }
    
    @JsonProperty("TIPO_DOC_IDENTIDAD")
    public String getTIPO_DOC_IDENTIDAD_ForJSON() {
        return TIPO_DOC_IDENTIDAD    ;
    }
    
    public void setTIPO_DOC_IDENTIDAD(String TIPO_DOC_IDENTIDAD) {
      this.TIPO_DOC_IDENTIDAD = TIPO_DOC_IDENTIDAD;
    }
    
    
    @JsonProperty("TIPO_AFECT_IGV")
    public String getTIPO_AFECT_IGV_ForJSON() {
        return TIPO_AFECT_IGV    ;
    }
    
    public void setTIPO_AFECT_IGV(String TIPO_AFECT_IGV) {
      this.TIPO_AFECT_IGV = TIPO_AFECT_IGV;
    }
    
    
    @JsonProperty("TIPO_PRECIO")
    public String getTIPO_PRECIO_ForJSON() {
        return TIPO_PRECIO    ;
    }
    
    public void setTIPO_PRECIO(String TIPO_PRECIO) {
      this.TIPO_PRECIO = TIPO_PRECIO;
    }
    
    
}
