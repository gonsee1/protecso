/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Component;


@Component
public class TABLA_TABLAS {
    
    private int ID_TABLAS;
    private String NOMBRE;
    private String DESCRIPCION;
    private int ID_TABLA_PADRE;
    private String CODIGO;
    private String VALOR1;
    private String VALOR2;
    private String TIPO;
    private boolean CONFIGURABLE;
    private int ESTADO;
    

    @JsonProperty("ID_TABLAS")
    public int getID_TABLAS() {
        return ID_TABLAS;
    }

    public void setID_TABLAS(int ID_TABLAS) {
        this.ID_TABLAS = ID_TABLAS;
    }

    @JsonProperty("NOMBRE")
    public String getNOMBRE() {
        return NOMBRE;
    }

    public void setNOMBRE(String NOMBRE) {
        this.NOMBRE = NOMBRE;
    }

    @JsonProperty("DESCRIPCION")
    public String getDESCRIPCION() {
        return DESCRIPCION;
    }

    public void setDESCRIPCION(String DESCRIPCION) {
        this.DESCRIPCION = DESCRIPCION;
    }

    @JsonProperty("ID_TABLA_PADRE")
    public int getID_TABLA_PADRE() {
        return ID_TABLA_PADRE;
    }

    public void setID_TABLA_PADRE(int ID_TABLA_PADRE) {
        this.ID_TABLA_PADRE = ID_TABLA_PADRE;
    }

    @JsonProperty("CODIGO")
    public String getCODIGO() {
        return CODIGO;
    }

    public void setCODIGO(String CODIGO) {
        this.CODIGO = CODIGO;
    }

    @JsonProperty("VALOR1")
    public String getVALOR1() {
        return VALOR1;
    }

    public void setVALOR1(String VALOR1) {
        this.VALOR1 = VALOR1;
    }

    @JsonProperty("VALOR2")
    public String getVALOR2() {
        return VALOR2;
    }

    public void setVALOR2(String VALOR2) {
        this.VALOR2 = VALOR2;
    }

    @JsonProperty("TIPO")
    public String getTIPO() {
        return TIPO;
    }
 
    public void setTIPO(String TIPO) {
        this.TIPO = TIPO;
    }

    @JsonProperty("CONFIGURABLE")
    public boolean isCONFIGURABLE() {
        return CONFIGURABLE;
    }

    
    public void setCONFIGURABLE(boolean CONFIGURABLE) {
        this.CONFIGURABLE = CONFIGURABLE;
    }

    @JsonProperty("ESTADO")
    public int getESTADO() {
        return ESTADO;
    }

    public void setESTADO(int ESTADO) {
        this.ESTADO = ESTADO;
    }
    
    
    
}
