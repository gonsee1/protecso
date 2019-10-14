/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.units;

/**
 *
 * @author crodas
 */
public class AssertNegocio {
    AssertRecibo recibos[]=null;
    AssertFactura factura=null;
    Double saldo=0d;

    public AssertNegocio(AssertRecibo recibos[],AssertFactura factura) {
        this.recibos=recibos;
        this.factura=factura;
    }    
    
    public AssertNegocio(AssertRecibo recibos[],AssertFactura factura,Double saldo) {
        this.recibos=recibos;
        this.factura=factura;
        this.saldo=saldo;
    }

    public Double getSaldo() {
        return saldo;
    }
    public int getNumeroRecibos(){
        if (recibos!=null) return recibos.length;
        return 0;
    }

    
}
