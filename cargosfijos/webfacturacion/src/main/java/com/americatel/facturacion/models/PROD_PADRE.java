/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.models;

import com.americatel.facturacion.historial.Historial;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Component;

/**
 *
 * @author efitec01
 */
@Component
public class PROD_PADRE extends Historial{
    
  private int COD_PROD_PADRE;
  private String DESC_PROD_PADRE;
  private Boolean ST_ELIM=false;
       
  
   @JsonProperty("COD_PROD_PADRE")
    public int getCO_PROD_PADRE_ForJSON() {
        return COD_PROD_PADRE;
    }
    
    @JsonProperty("DESC_PROD_PADRE")
    public String getDESC_PROD_PADRE_ForJSON() {
        return DESC_PROD_PADRE;
    }
    
    
    @JsonProperty("ST_ELIM")
    public Boolean getST_ELIM_ForJSON() {
        return ST_ELIM;
    }   
    
    
    public void setCO_PROD_PADRE(int COD_PROD_PADRE) {
        this.COD_PROD_PADRE = COD_PROD_PADRE;
    }
    
     public void setDESC_PROD_PADRE(String DESC_PROD_PADRE) {
        this.DESC_PROD_PADRE = DESC_PROD_PADRE;
    }
    
     
    public void setST_ELIM(Boolean ST_ELIM) {
        this.ST_ELIM = ST_ELIM;
    }  
}
