/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.models;

import com.americatel.facturacion.mappers.mapCIER_DATA_SUCU;
import com.americatel.facturacion.mappers.mapNEGO_SUCU;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author crodas
 */
@Component
public class CIER_DATA_SUCU {
    static mapCIER_DATA_SUCU mapCIER_DATA_Sucu;
    static mapNEGO_SUCU mapNego_Sucu;
    private Integer CO_CIER_DATA_SUCU;
    private Integer CO_CIER_DATA_NEGO;
    private Integer CO_NEGO_SUCU;
    private String DE_DIRE_SUCU;
    
    private Integer CO_DEPA;
    private String NO_DEPA;
    private Integer CO_PROV;
    private String NO_PROV;
    private Integer CO_DIST;
    private String NO_DIST;
    
    private String DE_ORDE;
    
    private String DE_NOMB;
    
    

    
    @Autowired
    public void setmapCIER_DATA_SUCU(mapCIER_DATA_SUCU mapCIER_DATA_Sucu){
    	CIER_DATA_SUCU.mapCIER_DATA_Sucu=mapCIER_DATA_Sucu;
    }      
    @Autowired
    public void setmapNEGO_SUCU(mapNEGO_SUCU mapNego_Sucu){
    	CIER_DATA_SUCU.mapNego_Sucu=mapNego_Sucu;
    }    
    public Integer getCO_CIER_DATA_SUCU() {
        return CO_CIER_DATA_SUCU;
    }

    public String getDE_DIRE_SUCU() {
        return DE_DIRE_SUCU;
    }

    public String getDE_ORDE() {
        return DE_ORDE;
    }

    public void setCO_CIER_DATA_SUCU(Integer CO_CIER_DATA_SUCU) {
        this.CO_CIER_DATA_SUCU = CO_CIER_DATA_SUCU;
    }

    public void setDE_DIRE_SUCU(String DE_DIRE_SUCU) {
        this.DE_DIRE_SUCU = DE_DIRE_SUCU;
    }

    public void setDE_ORDE(String DE_ORDE) {
        this.DE_ORDE = DE_ORDE;
    }

    public void setCO_NEGO_SUCU(Integer CO_NEGO_SUCU) {
        this.CO_NEGO_SUCU = CO_NEGO_SUCU;
    }

    public Integer getCO_NEGO_SUCU() {
        return CO_NEGO_SUCU;
    }

    public Integer getCO_CIER_DATA_NEGO() {
        return CO_CIER_DATA_NEGO;
    }

    public void setCO_CIER_DATA_NEGO(Integer CO_CIER_DATA_NEGO) {
        this.CO_CIER_DATA_NEGO = CO_CIER_DATA_NEGO;
    }

    public Integer getCO_DEPA() {
        return CO_DEPA;
    }

    public String getNO_DEPA() {
        return NO_DEPA;
    }

    public Integer getCO_PROV() {
        return CO_PROV;
    }

    public String getNO_PROV() {
        return NO_PROV;
    }

    public Integer getCO_DIST() {
        return CO_DIST;
    }

    public String getNO_DIST() {
        return NO_DIST;
    }

    public String DE_NOMB() {
        return DE_NOMB;
    }
    
    public void setCO_DEPA(Integer CO_DEPA) {
        this.CO_DEPA = CO_DEPA;
    }

    public void setNO_DEPA(String NO_DEPA) {
        this.NO_DEPA = NO_DEPA;
    }

    public void setCO_PROV(Integer CO_PROV) {
        this.CO_PROV = CO_PROV;
    }

    public void setNO_PROV(String NO_PROV) {
        this.NO_PROV = NO_PROV;
    }

    public void setCO_DIST(Integer CO_DIST) {
        this.CO_DIST = CO_DIST;
    }

    public void setNO_DIST(String NO_DIST) {
        this.NO_DIST = NO_DIST;
    }
    
    @JsonIgnore
    public void save() {
        mapCIER_DATA_Sucu.insertar(this);
    }    

    @JsonIgnore
    public NEGO_SUCU getNEGO_SUCU(){
        if (this.CO_NEGO_SUCU!=null)
            return mapNego_Sucu.getId(this.CO_NEGO_SUCU);
        return null;
    }
    
    @JsonIgnore
    public CIER_DATA_SUCU getCIER_DATA_SUCU(){
        if (this.CO_CIER_DATA_SUCU!=null)
            return mapCIER_DATA_Sucu.getId(this.CO_CIER_DATA_SUCU);
        return null;
    }
}
