/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.cores;

/**
 *
 * @author crodas
 */
public class FacturaGlosa {
    private String nombre=null;
    private Double monto=null;
    private String periodo=null;

    FacturaGlosa(String periodo,String nombre, Double monto) {
        this.nombre=nombre;
        this.monto=monto;
        this.periodo=periodo;
    }    
    
    public String toString(){
        return "Glosa: "+this.nombre+", monto: "+this.monto;
    }

    public String getNombre() {
        return nombre;
    }

    public Double getMonto() {
        return monto;
    } 
    
    public String getPeriodo() {
        return periodo;
    }     
}
