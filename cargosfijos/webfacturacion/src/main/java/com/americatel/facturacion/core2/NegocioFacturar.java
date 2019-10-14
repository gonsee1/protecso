/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.core2;

import com.americatel.facturacion.models.CIER;
import com.americatel.facturacion.models.NEGO;
import com.americatel.facturacion.models.PROD;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 *
 * @author crodas
 */
public class NegocioFacturar extends NegocioDetalleCierre{
    NegocioRecibo negocio_recibo=null;    
    public NegocioFacturar(CIER cier, PROD prod, NEGO nego, HttpServletRequest request,Boolean preview) {
        super(cier, prod, nego, request);
        negocio_recibo=new NegocioRecibo(this);
        this.preview=preview;
    }
    
    public void facturar(){
        super.facturar();        
    }
    
    public void crearPdf(){
        negocio_recibo.crearPdf(null);
    }
    public void crearPdf(HttpServletResponse response){
        negocio_recibo.crearPdf(response);
    }
    
    public void saveBD(){
        if (!this.preview){
            negocio_recibo.saveBD();
        }
    }
    
    public Long getNumero_Recibo_Semilla() {
        return this.negocio_recibo.getNumero_Recibo_Semilla();
    }

    public void setNumero_Recibo_Semilla(Long Numero_Recibo_Semilla) {
        this.negocio_recibo.setNumero_Recibo_Semilla(Numero_Recibo_Semilla);
    }
    
    public Long getNumero_Factura_Semilla() {
        return this.negocio_recibo.getNumero_Factura_Semilla();
    }

    public void setNumero_Factura_Semilla(Long Numero_Factura_Semilla) {
        this.negocio_recibo.setNumero_Factura_Semilla(Numero_Factura_Semilla);
    }

    public NegocioRecibo getNegocio_recibo() {
        return negocio_recibo;
    }

    public Double getSaldo_aproximado() {
        return saldo_aproximado;
    }
    
    
    
    
}
