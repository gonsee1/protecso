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
public class AssertFactura {
    private double neto,igv,ajuste_por_redondeo,total;
    
    public AssertFactura(double neto, double igv, double ajuste_por_redondeo, double total) {
        this.neto = neto;
        this.igv = igv;
        this.ajuste_por_redondeo = ajuste_por_redondeo;
        this.total = total;
    }
    
    public double getNeto() {
        return neto;
    }

    public double getIgv() {
        return igv;
    }

    public double getAjuste_por_redondeo() {
        return ajuste_por_redondeo;
    }

    public double getTotal() {
        return total;
    }
    
}
