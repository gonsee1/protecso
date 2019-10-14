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
public class AssertRecibo {
    private double neto,ajuste_afecto,igv,ajuste_no_afecto,total;
    private String periodo;
    
    
    public AssertRecibo(double neto, double ajuste_afecto, double igv, double ajuste_no_afecto, double total,String periodo) {
        this.neto = neto;
        this.ajuste_afecto = ajuste_afecto;
        this.igv = igv;
        this.ajuste_no_afecto = ajuste_no_afecto;
        this.total = total;
        this.periodo=periodo;
    }
    

    public double getNeto() {
        return neto;
    }

    public double getAjuste_afecto() {
        return ajuste_afecto;
    }

    public double getIgv() {
        return igv;
    }

    public double getAjuste_no_afecto() {
        return ajuste_no_afecto;
    }

    public double getTotal() {
        return total;
    }

    public String getPeriodo() {
        return periodo;
    }
    
}
