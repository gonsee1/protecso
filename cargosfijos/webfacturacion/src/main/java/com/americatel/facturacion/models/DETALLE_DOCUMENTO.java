/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.models;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.americatel.facturacion.historial.Historial;
import com.fasterxml.jackson.annotation.JsonProperty;

@Component
public class DETALLE_DOCUMENTO  extends Historial {
    
    private Long NRO_DETALLE_DOCUMENTO;
    private Integer CO_CIER;
    private Long CO_FACT;    
    private String TIP_SERV;
    private Integer CO_TIPO_GLOS;
    private String NOMBRE_SERVICIO;
    private BigDecimal MON_SERVICIO;
    private String ABREV_TIPO_DOCUMENTO;
    
    public DETALLE_DOCUMENTO() {
		super();
	}

	public DETALLE_DOCUMENTO(Long nRO_DETALLE_DOCUMENTO, Integer cO_CIER, Long cO_FACT, String tIP_SERV,
			Integer cO_TIPO_GLOS, String nOMBRE_SERVICIO, BigDecimal mON_SERVICIO, String ABREV_TIPO_DOCUMENTO) {
		super();
		this.NRO_DETALLE_DOCUMENTO = nRO_DETALLE_DOCUMENTO;
		this.CO_CIER = cO_CIER;
		this.CO_FACT = cO_FACT;
		this.TIP_SERV = tIP_SERV;
		this.CO_TIPO_GLOS = cO_TIPO_GLOS;
		this.NOMBRE_SERVICIO = nOMBRE_SERVICIO;
		this.MON_SERVICIO = mON_SERVICIO;
		this.ABREV_TIPO_DOCUMENTO = ABREV_TIPO_DOCUMENTO;
	}
    
    
	@JsonProperty("NRO_DETALLE_DOCUMENTO")
	public Long getNRO_DETALLE_DOCUMENTO() {
		return NRO_DETALLE_DOCUMENTO;
	}
	public void setNRO_DETALLE_DOCUMENTO(Long nRO_DETALLE_DOCUMENTO) {
		NRO_DETALLE_DOCUMENTO = nRO_DETALLE_DOCUMENTO;
	}
	@JsonProperty("CO_CIER")
	public Integer getCO_CIER() {
		return CO_CIER;
	}
	public void setCO_CIER(Integer cO_CIER) {
		CO_CIER = cO_CIER;
	}
	@JsonProperty("CO_FACT")
	public Long getCO_FACT() {
		return CO_FACT;
	}
	public void setCO_FACT(Long cO_FACT) {
		CO_FACT = cO_FACT;
	}
	@JsonProperty("TIP_SERV")
	public String getTIP_SERV() {
		return TIP_SERV;
	}
	public void setTIP_SERV(String tIP_SERV) {
		TIP_SERV = tIP_SERV;
	}
	@JsonProperty("CO_TIPO_GLOS")
	public Integer getCO_TIPO_GLOS() {
		return CO_TIPO_GLOS;
	}
	public void setCO_TIPO_GLOS(Integer cO_TIPO_GLOS) {
		CO_TIPO_GLOS = cO_TIPO_GLOS;
	}
	@JsonProperty("NOMBRE_SERVICIO")
	public String getNOMBRE_SERVICIO() {
		return NOMBRE_SERVICIO;
	}
	public void setNOMBRE_SERVICIO(String nOMBRE_SERVICIO) {
		NOMBRE_SERVICIO = nOMBRE_SERVICIO;
	}
	@JsonProperty("MON_SERVICIO")
	public BigDecimal getMON_SERVICIO() {
		return MON_SERVICIO;
	}
	public void setMON_SERVICIO(BigDecimal mON_SERVICIO) {
		MON_SERVICIO = mON_SERVICIO;
	}
	@JsonProperty("ABREV_TIPO_DOCUMENTO")
	public String getABREV_TIPO_DOCUMENTO() {
		return ABREV_TIPO_DOCUMENTO;
	}

	public void setABREV_TIPO_DOCUMENTO(String aBREV_TIPO_DOCUMENTO) {
		ABREV_TIPO_DOCUMENTO = aBREV_TIPO_DOCUMENTO;
	}
    
    
    
}

