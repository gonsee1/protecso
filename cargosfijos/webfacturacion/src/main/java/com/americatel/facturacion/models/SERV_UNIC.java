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
 */
public class SERV_UNIC  extends Historial {
    private Integer CO_SERV_UNIC;
    private Integer CO_PROD;
    private Integer CO_MONE_FACT;
    private String NO_SERV_UNIC;    
    private Double IM_MONTO;
    private Boolean ST_AFEC_DETR=true; 
    private Boolean ST_ELIM=false; 
    private Integer COD_PROD_PADRE;// EMANUEL BARBARAN - EFITEC (SE AGREGO VARIABLES)
    
    private PROD_PADRE prod_padre;    // EMANUEL BARBARAN - EFITEC (SE AGREGO VARIABLES)
    private PROD prod;
    private MONE_FACT mone_fact;
    
    
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
    
    
    @JsonProperty("COD_PROD_PADRE")
    public Integer getCOD_PROD_PADRE_ForJSON() {
        return COD_PROD_PADRE;
    }  
    
    @JsonProperty("CO_MONE_FACT")
    public Integer getCO_MONE_FACT_ForJSON() {
        return CO_MONE_FACT;
    }
    
    
    @JsonProperty("CO_SERV_UNIC")
    public Integer getCO_SERV_UNIC_ForJSON() {
        return CO_SERV_UNIC;
    }

    @JsonProperty("CO_PROD")
    public Integer getCO_PROD_ForJSON() {
        return CO_PROD;
    }

    @JsonProperty("NO_SERV_UNIC")
    public String getNO_SERV_UNIC_ForJSON() {
        return NO_SERV_UNIC;
    }

    @JsonProperty("IM_MONTO")
    public Double getIM_MONTO_ForJSON() {
        return IM_MONTO;
    }

    @JsonProperty("ST_AFEC_DETR")
    public Boolean getST_AFEC_DETR_ForJSON() {
        return ST_AFEC_DETR;
    }

    @JsonProperty("ST_ELIM")
    public Boolean getST_ELIM_ForJSON() {
        return ST_ELIM;
    }

    
    public void setCO_SERV_UNIC(Integer CO_SERV_UNIC) {
        this.CO_SERV_UNIC = CO_SERV_UNIC;
    }

    public void setCO_PROD(Integer CO_PROD) {
        this.CO_PROD = CO_PROD;
    }

    public void setNO_SERV_UNIC(String NO_SERV_UNIC) {
        this.NO_SERV_UNIC = NO_SERV_UNIC;
    }
    
    public void setCO_MONE_FACT(Integer CO_MONE_FACT) {
        this.CO_MONE_FACT = CO_MONE_FACT;
    }
    
    public void setIM_MONTO(Double IM_MONTO) {
        this.IM_MONTO = IM_MONTO;
    }

    public void setST_AFEC_DETR(Boolean ST_AFEC_DETR) {
        this.ST_AFEC_DETR = ST_AFEC_DETR;
    }

    public void setST_ELIM(Boolean ST_ELIM) {
        this.ST_ELIM = ST_ELIM;
    }
    
    public void setCOD_PROD_PADRE(Integer COD_PROD_PADRE) {
        this.COD_PROD_PADRE = COD_PROD_PADRE;
    }   
    
}
