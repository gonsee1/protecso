/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.reportes;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.americatel.facturacion.fncs.ExcelWriter;
import com.americatel.facturacion.models.CLIE;
import com.americatel.facturacion.models.CONT_CLIE;
import com.americatel.facturacion.models.DEPA;
import com.americatel.facturacion.models.DIST;
import com.americatel.facturacion.models.PROV;
import com.americatel.facturacion.models.SUCU;
import com.americatel.facturacion.models.TIPO_DOCU;

/**
 *
 * @author rordonez
 */
public class ReporteDatosClientes {
    private ExcelWriter excel;
    private CLIE clie;
    public ReporteDatosClientes(Integer COD_PROD_PADRE, Integer COD_SERVICIO){
        this.clie = new CLIE();
        this.excel=new ExcelWriter();
        this.excel.addHoja("Reporte de Datos de Clientes");
        this.calcular(COD_PROD_PADRE,COD_SERVICIO);
    }
    
    public void save(HttpServletResponse response) {
        SimpleDateFormat formateador = new SimpleDateFormat("ddMMyyyy");
        response.setHeader("Content-Type", "application/vnd.ms-excel");
        response.addHeader("Content-Disposition", "attachment; filename=\"Reportes_Datos_Clientes_"+formateador.format(new Date())+"\".xls" );
        this.excel.save(response);
    }
        
    public String getStringOrCero(String cadena){
        if (cadena!=null){
            if (!cadena.trim().equals(""))
                return cadena;
        }
        return "0";
    }
    
    public String getStringOrVacio(String cadena){
        if (cadena!=null){
            if (!cadena.trim().equals(""))
                return cadena;
        }
        return "";
    }
    
    private void calcular(Integer COD_PROD_PADRE, Integer COD_SERVICIO) {
    	String cliente_temp = "";
        try{
	    	this.excel.writeCols(new Object[]{"C\u00f3digo Cliente","Raz\u00f3n Social","Tipo de Documento","RUC","Direcci\u00f3n Fiscal","Departamento Fiscal","Provincia Fiscal","Distrito Fiscal","EJECUTIVO","SUBGERENTE","SEGMENTACION","Rep Legal","DNI Rep Legal","Email Rep. Legal","Telef. Representan Legal","Contacto","DNI Contacto","Telf Referencia Contacto","Forma de pago","Mensaje de PAC"});
	        
	    	List<CLIE> listaClientes=clie.getAllDetalleClientesByServicio( COD_PROD_PADRE,  COD_SERVICIO);
	        
	    	for (CLIE cliente:listaClientes){
	            SUCU sucu_fisc = cliente.getSUCU_FISC_ForJSON();
	            DIST dist_fiscal = sucu_fisc.getDistrito();
	            PROV prov_fiscal = dist_fiscal.getPROV_ForJSON();
	            DEPA depa_fiscal = prov_fiscal.getDEPA_ForJSON();
	            CONT_CLIE rep_legal = cliente.getCONT_CLIE_REPR_LEGA_ForJSON();
	            List<CONT_CLIE> listaContactos = cliente.getContactos();
	            TIPO_DOCU tipo_docu = cliente.getTipoDocumento();            
	            
	            if (listaContactos.size()>0 && (!rep_legal.getST_ELIM_ForJSON()) ){
	                for (CONT_CLIE contacto:listaContactos){
	                    this.excel.writeCols(new Object[]{
	                        getStringOrVacio(cliente.getCodigoCliente()),
	                        getStringOrVacio(cliente.getFullNameCliente()),
	                        getStringOrVacio(tipo_docu.getNO_TIPO_DOCU_ForJSON()),
	                        getStringOrVacio(cliente.getDE_NUME_DOCU_ForJSON()),
	                        getStringOrVacio(sucu_fisc.getDE_DIRE_ForJSON()),
	                        getStringOrVacio(depa_fiscal.getNO_DEPA_ForJSON()),
	                        getStringOrVacio(prov_fiscal.getNO_PROV_ForJSON()),
	                        getStringOrVacio(dist_fiscal.getNO_DIST_ForJSON()),
	                        getStringOrVacio(cliente.getDE_EJEC_ForJSON()),
	                        getStringOrVacio(cliente.getDE_SUB_GERE_ForJSON()),
	                        getStringOrVacio(cliente.getDE_SEGM_ForJSON()),
	                        getStringOrVacio(rep_legal.getFullName()),
	                        getStringOrVacio(rep_legal.getDE_NUME_DOCU_ForJSON()),
	                        getStringOrVacio(rep_legal.getDE_CORR_ForJSON()),
	                        getStringOrVacio(rep_legal.getDE_TELE_ForJSON()),
	                        getStringOrVacio(contacto.getFullName()),
	                        getStringOrVacio(contacto.getDE_NUME_DOCU_ForJSON()),
	                        getStringOrVacio(contacto.getDE_TELE_ForJSON()),
	                        "",
	                        ""
	                    });
	                }
	            }else{

	                this.excel.writeCols(new Object[]{
	                    getStringOrVacio(cliente.getCodigoCliente()),
	                    getStringOrVacio(cliente.getFullNameCliente()),
	                    getStringOrVacio(tipo_docu.getNO_TIPO_DOCU_ForJSON()),
	                    getStringOrVacio(cliente.getDE_NUME_DOCU_ForJSON()),
	                    getStringOrVacio(sucu_fisc.getDE_DIRE_ForJSON()),
	                    getStringOrVacio(depa_fiscal.getNO_DEPA_ForJSON()),
	                    getStringOrVacio(prov_fiscal.getNO_PROV_ForJSON()),
	                    getStringOrVacio(dist_fiscal.getNO_DIST_ForJSON()),
	                    getStringOrVacio(cliente.getDE_EJEC_ForJSON()),
	                    getStringOrVacio(cliente.getDE_SUB_GERE_ForJSON()),
	                    getStringOrVacio(cliente.getDE_SEGM_ForJSON()),
	                    "",
	                    "",
	                    "",
	                    "",
	                    "",
	                    "",
	                    "",
	                    "",
	                    ""
	                });
	            }
	            
	        }
	    }
        catch(Exception e)
        {
        	System.out.print("error  "+ cliente_temp);
        	System.out.print(e);
        }
    }
}
