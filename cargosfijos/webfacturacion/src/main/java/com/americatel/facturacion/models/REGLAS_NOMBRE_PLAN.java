/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.models;

import com.americatel.facturacion.historial.Historial;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author crodas
 * 
 * Productos
 */
public class REGLAS_NOMBRE_PLAN  extends Historial {
    private int COD_REGLAS_NOMBRE_PLAN;
    private String NOMBRE_PLAN;
    private String PRODUCTO_CARGA;
    
    /*GET SET*/

    @JsonProperty("COD_REGLAS_NOMBRE_PLAN")
    public int getCOD_REGLAS_NOMBRE_PLAN_ForJSON() {
        return COD_REGLAS_NOMBRE_PLAN;
    }
    
    @JsonProperty("NOMBRE_PLAN")
    public String getNOMBRE_PLAN_ForJSON() {
        return NOMBRE_PLAN;
    }
  
    @JsonProperty("PRODUCTO_CARGA")
    public String getPRODUCTO_CARGA_ForJSON() {
        return PRODUCTO_CARGA;
    }
    
    
    public void setCOD_REGLAS_NOMBRE_PLAN(int COD_REGLAS_NOMBRE_PLAN) {
        this.COD_REGLAS_NOMBRE_PLAN = COD_REGLAS_NOMBRE_PLAN;
    }

    public void setNOMBRE_PLAN(String NOMBRE_PLAN) {
        this.NOMBRE_PLAN = NOMBRE_PLAN;
    }

    public void setPRODUCTO_CARGA(String PRODUCTO_CARGA) {
        this.PRODUCTO_CARGA = PRODUCTO_CARGA;
    }
    
}
