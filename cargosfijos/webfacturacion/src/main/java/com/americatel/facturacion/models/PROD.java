/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.models;

import com.americatel.facturacion.historial.Historial;
import com.americatel.facturacion.mappers.mapCIER;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author crodas
 * 
 * Productos
 */
@Component
public class PROD  extends Historial {
    private static mapCIER mapCier;
    
    private int CO_PROD;
    private int CO_TIPO_FACT;
    private int CO_MONE_FACT;
    private int CO_PERI_FACT;
    private String NO_PROD;
    private Boolean ST_ELIM=false;
    private Integer COD_PROD_PADRE;// EMANUEL BARBARAN - EFITEC (SE AGREGO VARIABLES)
    
    private PROD_PADRE prod_padre;    // EMANUEL BARBARAN - EFITEC (SE AGREGO VARIABLES)
    private TIPO_FACT tipo_fact;
    private MONE_FACT mone_fact;
    private PERI_FACT peri_fact;
    
    private String DESC_PROD_PADRE;
    
    @Autowired
    public void setmapCIER(mapCIER mapCier){
        this.mapCier=mapCier;
    }
    
    /*GET SET*/
    
    @JsonProperty("DESC_PROD_PADRE")
    public String getDESC_PROD_PADRE_ForJSON() {
        return DESC_PROD_PADRE;
    }
    
    
    public void setDESC_PROD_PADRE(String DESC_PROD_PADRE) {
        this.DESC_PROD_PADRE = DESC_PROD_PADRE;
    }
    
    @JsonProperty("ST_ELIM")
    public Boolean getST_ELIM_ForJSON() {
        return ST_ELIM;
    }      
    
    @JsonProperty("PROD_PADRE")
    public PROD_PADRE getProd_padre_ForJSON() {
        return prod_padre;
    }

    
    @JsonProperty("TIPO_FACT")
    public TIPO_FACT getTipo_fact_ForJSON() {
        return tipo_fact;
    }
    
    @JsonProperty("MONE_FACT")
    public MONE_FACT getMone_fact_ForJSON() {
        return mone_fact;
    }
    
    @JsonProperty("PERI_FACT")
    public PERI_FACT getPeri_fact_ForJSON() {
        return peri_fact;
    }
    
    @JsonProperty("COD_PROD_PADRE")
    public Integer getCOD_PROD_PADRE_ForJSON() {
        return COD_PROD_PADRE;
    }  
        
    @JsonProperty("CO_PROD")
    public int getCO_PROD_ForJSON() {
        return CO_PROD;
    }

    @JsonProperty("CO_TIPO_FACT")
    public int getCO_TIPO_FACT_ForJSON() {
        return CO_TIPO_FACT;
    }

    @JsonProperty("CO_MONE_FACT")
    public int getCO_MONE_FACT_ForJSON() {
        return CO_MONE_FACT;
    }

    @JsonProperty("CO_PERI_FACT")
    public int getCO_PERI_FACT_ForJSON() {
        return CO_PERI_FACT;
    }
    @JsonProperty("NO_PROD")
    public String getNO_PROD_ForJSON() {
        return NO_PROD;
    }

    public void setCO_PROD(int CO_PROD) {
        this.CO_PROD = CO_PROD;
    }

    public void setCO_TIPO_FACT(int CO_TIPO_FACT) {
        this.CO_TIPO_FACT = CO_TIPO_FACT;
    }

    public void setCO_MONE_FACT(int CO_MONE_FACT) {
        this.CO_MONE_FACT = CO_MONE_FACT;
    }

    public void setCO_PERI_FACT(int CO_PERI_FACT) {
        this.CO_PERI_FACT = CO_PERI_FACT;
    }

    public void setNO_PROD(String NO_PROD) {
        this.NO_PROD = NO_PROD;
    }
    public void setST_ELIM(Boolean ST_ELIM) {
        this.ST_ELIM = ST_ELIM;
    } 
    
    public void setCOD_PROD_PADRE(Integer COD_PROD_PADRE) {
        this.COD_PROD_PADRE = COD_PROD_PADRE;
    }   
    
    @JsonIgnore 
    public  CIER  getCierrePendiente() {
        //Obtiene el cierre pendiente por producto, busca el estado 1
        return PROD.mapCier.getCierrePendienteByProducto(this);
    }
    @JsonIgnore 
    public  List<CIER>  getCierresCerrados() {
        //Obtiene el cierre pendiente por producto, busca el estado 1
        return PROD.mapCier.getCierresCerradosByProducto(this);
    }
    @JsonIgnore 
    public  CIER  getCierreCerrado() {
        //Obtiene el cierre pendiente por producto, busca el estado 1
        return PROD.mapCier.getCierreCerrado(this);
    }
    @JsonIgnore
    public List<NEGO> getNegociosParaProcesoCierre() {
        //Obtiene el cierre pendiente por producto, busca el estado 1
        return PROD.mapCier.getNegociosParaProcesoCierre(this);
    }    
    @JsonIgnore
    public String getStringStateCierre() {
        CIER cier=this.getCierrePendiente();
        if (cier!=null){
        	return cier.getStringStateCierre();
        }        	
        return null;
    } 
    
    
    
     
}
