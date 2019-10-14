/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.models;

import org.springframework.stereotype.Component;

import com.americatel.facturacion.historial.Historial;

/**
 *
 * @author crodas
 */
@Component
public class ReporteCargaTI extends Historial {
    private String CO_FACT;
    private String FE_EMIS;
    private String DE_CODI_BUM;
    private String CO_NEGO;
    private String NO_CLIE;
    private String DE_DIRE_FISC;
    private String RUC;
    private String TIP_SERV;
    private String NO_MONE_FACT;
    private String TI_HOST_DEDI;
    
    private String TI_HOST_COMP;
    private String TI_CLOUD_SAAS;
    private String TI_CLOUD_IAAS;
    private String SUB_TOTAL;
    private String IGV;
    private String TOTAL_FACTURA;
    private String FE_VENC;
    private String TI_CLOUD_MCAFEE;
    private String REDONDEO;
    private String TI_CLOUD_BACK_UP;
    
    private String ABREV_TIPO_DOCUMENTO;
    private String DE_NUME_DOCU;
    private String DOMICILIADO;
    private String DIST_FISC;
    private String TIPO_DOCU;
    private String TI_HOU_RENTA;
    
	public String getCO_FACT() {
		return CO_FACT;
	}
	public void setCO_FACT(String cO_FACT) {
		CO_FACT = cO_FACT;
	}
	public String getFE_EMIS() {
		return FE_EMIS;
	}
	public void setFE_EMIS(String fE_EMIS) {
		FE_EMIS = fE_EMIS;
	}
	public String getDE_CODI_BUM() {
		return DE_CODI_BUM;
	}
	public void setDE_CODI_BUM(String dE_CODI_BUM) {
		DE_CODI_BUM = dE_CODI_BUM;
	}
	public String getCO_NEGO() {
		return CO_NEGO;
	}
	public void setCO_NEGO(String cO_NEGO) {
		CO_NEGO = cO_NEGO;
	}
	public String getNO_CLIE() {
		return NO_CLIE;
	}
	public void setNO_CLIE(String nO_CLIE) {
		NO_CLIE = nO_CLIE;
	}
	public String getDE_DIRE_FISC() {
		return DE_DIRE_FISC;
	}
	public void setDE_DIRE_FISC(String dE_DIRE_FISC) {
		DE_DIRE_FISC = dE_DIRE_FISC;
	}
	public String getRUC() {
		return RUC;
	}
	public void setRUC(String rUC) {
		RUC = rUC;
	}
	public String getTIP_SERV() {
		return TIP_SERV;
	}
	public void setTIP_SERV(String tIP_SERV) {
		TIP_SERV = tIP_SERV;
	}
	public String getNO_MONE_FACT() {
		return NO_MONE_FACT;
	}
	public void setNO_MONE_FACT(String nO_MONE_FACT) {
		NO_MONE_FACT = nO_MONE_FACT;
	}
	public String getTI_HOST_DEDI() {
		return TI_HOST_DEDI;
	}
	public void setTI_HOST_DEDI(String tI_HOST_DEDI) {
		TI_HOST_DEDI = tI_HOST_DEDI;
	}
	public String getTI_HOST_COMP() {
		return TI_HOST_COMP;
	}
	public void setTI_HOST_COMP(String tI_HOST_COMP) {
		TI_HOST_COMP = tI_HOST_COMP;
	}
	public String getTI_CLOUD_SAAS() {
		return TI_CLOUD_SAAS;
	}
	public void setTI_CLOUD_SAAS(String tI_CLOUD_SAAS) {
		TI_CLOUD_SAAS = tI_CLOUD_SAAS;
	}
	public String getTI_CLOUD_IAAS() {
		return TI_CLOUD_IAAS;
	}
	public void setTI_CLOUD_IAAS(String tI_CLOUD_IAAS) {
		TI_CLOUD_IAAS = tI_CLOUD_IAAS;
	}
	public String getSUB_TOTAL() {
		return SUB_TOTAL;
	}
	public void setSUB_TOTAL(String sUB_TOTAL) {
		SUB_TOTAL = sUB_TOTAL;
	}
	public String getIGV() {
		return IGV;
	}
	public void setIGV(String iGV) {
		IGV = iGV;
	}
	public String getTOTAL_FACTURA() {
		return TOTAL_FACTURA;
	}
	public void setTOTAL_FACTURA(String tOTAL_FACTURA) {
		TOTAL_FACTURA = tOTAL_FACTURA;
	}
	public String getFE_VENC() {
		return FE_VENC;
	}
	public void setFE_VENC(String fE_VENC) {
		FE_VENC = fE_VENC;
	}
	public String getTI_CLOUD_MCAFEE() {
		return TI_CLOUD_MCAFEE;
	}
	public void setTI_CLOUD_MCAFEE(String tI_CLOUD_MCAFEE) {
		TI_CLOUD_MCAFEE = tI_CLOUD_MCAFEE;
	}
	public String getREDONDEO() {
		return REDONDEO;
	}
	public void setREDONDEO(String rEDONDEO) {
		REDONDEO = rEDONDEO;
	}
	public String getTI_CLOUD_BACK_UP() {
		return TI_CLOUD_BACK_UP;
	}
	public void setTI_CLOUD_BACK_UP(String tI_CLOUD_BACK_UP) {
		TI_CLOUD_BACK_UP = tI_CLOUD_BACK_UP;
	}
	public String getABREV_TIPO_DOCUMENTO() {
		return ABREV_TIPO_DOCUMENTO;
	}
	public void setABREV_TIPO_DOCUMENTO(String aBREV_TIPO_DOCUMENTO) {
		ABREV_TIPO_DOCUMENTO = aBREV_TIPO_DOCUMENTO;
	}
	public String getDE_NUME_DOCU() {
		return DE_NUME_DOCU;
	}
	public void setDE_NUME_DOCU(String dE_NUME_DOCU) {
		DE_NUME_DOCU = dE_NUME_DOCU;
	}
	public String getDOMICILIADO() {
		return DOMICILIADO;
	}
	public void setDOMICILIADO(String dOMICILIADO) {
		DOMICILIADO = dOMICILIADO;
	}
	public String getDIST_FISC() {
		return DIST_FISC;
	}
	public void setDIST_FISC(String dIST_FISC) {
		DIST_FISC = dIST_FISC;
	}
	public String getTIPO_DOCU() {
		return TIPO_DOCU;
	}
	public void setTIPO_DOCU(String tIPO_DOCU) {
		TIPO_DOCU = tIPO_DOCU;
	}
	public String getTI_HOU_RENTA() {
		return TI_HOU_RENTA;
	}
	public void setTI_HOU_RENTA(String tI_HOU_RENTA) {
		TI_HOU_RENTA = tI_HOU_RENTA;
	}
    
    
    
    
}
