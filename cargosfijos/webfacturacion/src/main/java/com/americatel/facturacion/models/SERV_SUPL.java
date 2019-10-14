/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.models;

import com.americatel.facturacion.historial.Historial;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author crodas
 * 
 * Productos
 */
public class SERV_SUPL  extends Historial{
    private Integer CO_SERV_SUPL;    
    private Integer CO_PROD;
    private Integer CO_MONE_FACT;
    private String NO_SERV_SUPL;
    private double IM_MONTO;
    private boolean ST_AFEC_DETR;
    private Boolean ST_ELIM=false;
    private Integer COD_PROD_PADRE;// EMANUEL BARBARAN - EFITEC (SE AGREGO VARIABLES)
    
    private PROD_PADRE prod_padre;    // EMANUEL BARBARAN - EFITEC (SE AGREGO VARIABLES)
    private PROD prod;
    private MONE_FACT mone_fact;
    
    /*GET SET*/
    
    @JsonProperty("ST_ELIM")
    public Boolean getST_ELIM_ForJSON() {
        return ST_ELIM;
    }      
    
    @JsonProperty("CO_PROD")
    public Integer getCO_PROD_ForJSON() {
        return CO_PROD;
    }
    
    @JsonProperty("PROD_PADRE")
    public PROD_PADRE getProd_padre_ForJSON() {
        return prod_padre;
    }

    @JsonProperty("PROD")
    public PROD getProd_ForJSON() {
        return prod;
    }
    
    @JsonProperty("MONE_FACT")
    public MONE_FACT getMone_Fact_ForJSON() {
        return mone_fact;
    }    

    @JsonProperty("CO_SERV_SUPL")
    public Integer getCO_SERV_SUPL_ForJSON() {
        return CO_SERV_SUPL;
    }
    
    @JsonProperty("CO_MONE_FACT")
    public Integer getCO_MONE_FACT_ForJSON() {
        return CO_MONE_FACT;
    }
    
    @JsonProperty("NO_SERV_SUPL")
    public String getNO_SERV_SUPL_ForJSON() {
        return NO_SERV_SUPL;
    }

    @JsonProperty("IM_MONTO")
    public double getIM_MONTO_ForJSON() {
        return IM_MONTO;
    }

    @JsonProperty("COD_PROD_PADRE")
    public Integer getCOD_PROD_PADRE_ForJSON() {
        return COD_PROD_PADRE;
    }  

    
    @JsonProperty("ST_AFEC_DETR")
    public boolean isST_AFEC_DETR_ForJSON() {
        return ST_AFEC_DETR;
    }


    public void setCO_SERV_SUPL(Integer CO_SERV_SUPL) {
        this.CO_SERV_SUPL = CO_SERV_SUPL;
    }

    public void setNO_SERV_SUPL(String NO_SERV_SUPL) {
        this.NO_SERV_SUPL = NO_SERV_SUPL;
    }

    public void setCO_PROD(Integer CO_PROD) {
        this.CO_PROD = CO_PROD;
    }
    
    public void setCO_MONE_FACT(Integer CO_MONE_FACT) {
        this.CO_MONE_FACT = CO_MONE_FACT;
    }
    
    public void setIM_MONTO(double IM_MONTO) {
        this.IM_MONTO = IM_MONTO;
    }

    public void setST_AFEC_DETR(boolean ST_AFEC_DETR) {
        this.ST_AFEC_DETR = ST_AFEC_DETR;
    }

    public void setST_ELIM(Boolean ST_ELIM) {
        this.ST_ELIM = ST_ELIM;
    } 
    
    public void setCOD_PROD_PADRE(Integer COD_PROD_PADRE) {
        this.COD_PROD_PADRE = COD_PROD_PADRE;
    }   
    
}
