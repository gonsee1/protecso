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
public class PLAN  extends Historial{ 
        
    private int CO_PLAN;
    private String NO_PLAN;
    private int CO_PROD;
    private String DE_VELO_SUBI;
    private String DE_VELO_BAJA;
    private Integer CO_PLAN_MEDI_INST;
    private Integer CO_MONE_FACT;     
    private Integer COD_PROD_PADRE;// EMANUEL BARBARAN - EFITEC (SE AGREGO VARIABLES)
    private double IM_MONTO;
    private Boolean ST_ELIM=false;
    private Boolean ST_BACKUP_BU=false;
    private Boolean RELACIONADO_TI=false;
    private Boolean VELOCIDAD_OK_MAYORISTA=false;
    private String PRODUCTO_CARGA;
    
    private PROD prod;
    private PLAN_MEDI_INST plan_medio_inst;
    private MONE_FACT mone_fact;
    
    private PROD_PADRE prod_padre;    // EMANUEL BARBARAN - EFITEC (SE AGREGO VARIABLES)
    
    /*GET SET*/
    
    
    @JsonProperty("PROD_PADRE")
    public PROD_PADRE getProd_padre_ForJSON() {
        return prod_padre;
    }
    
    @JsonProperty("MONE_FACT")
    public MONE_FACT getMone_Fact_ForJSON() {
        return mone_fact;
    }
    
    
    
    @JsonProperty("CO_MONE_FACT")
    public Integer getCO_MONE_FACT_ForJSON() {
        return CO_MONE_FACT;
    } 
    
    @JsonProperty("COD_PROD_PADRE")
    public Integer getCOD_PROD_PADRE_ForJSON() {
        return COD_PROD_PADRE;
    }      
    
    @JsonProperty("ST_ELIM")
    public Boolean getST_ELIM_ForJSON() {
        return ST_ELIM;
    }
    
    @JsonProperty("ST_BACKUP_BU")
    public Boolean getST_BACKUP_BU_ForJSON() {
        return ST_BACKUP_BU;
    }
    
    @JsonProperty("RELACIONADO_TI")
    public Boolean getRELACIONADO_TI_ForJSON() {
        return RELACIONADO_TI;
    }
    
    
    @JsonProperty("VELOCIDAD_OK_MAYORISTA")
    public Boolean getVELOCIDAD_OK_MAYORISTA_ForJSON() {
        return VELOCIDAD_OK_MAYORISTA;
    }
    
    
    @JsonProperty("CO_PROD")
    public int getCO_PROD_ForJSON() {
        return CO_PROD;
    }
    
    @JsonProperty("PLAN_MEDI_INST")
    public PLAN_MEDI_INST getPLAN_MEDI_INST_ForJSON() {
        return plan_medio_inst;
    }    

    @JsonProperty("PROD")
    public PROD getProd_ForJSON() {
        return prod;
    }

    @JsonProperty("CO_PLAN")
    public int getCO_PLAN_ForJSON() {
        return CO_PLAN;
    }
    
    @JsonProperty("NO_PLAN")
    public String getNO_PLAN_ForJSON() {
        return NO_PLAN;
    }

    @JsonProperty("IM_MONTO")
    public double getIM_MONTO_ForJSON() {
        return IM_MONTO;
    }

    @JsonProperty("DE_VELO_SUBI")
    public String getDE_VELO_SUBI_ForJSON() {
        return DE_VELO_SUBI;
    }

    @JsonProperty("DE_VELO_BAJA")
    public String getDE_VELO_BAJA_ForJSON() {
        return DE_VELO_BAJA;
    }

    @JsonProperty("CO_PLAN_MEDI_INST")
    public Integer getCO_PLAN_MEDI_INST_ForJSON() {
        return CO_PLAN_MEDI_INST;
    }

    @JsonIgnore
    public String getFullNamePlan(int codigo_producto,int codigo_servicio,int codigo_nombre_plan,int velocidad,int aplicaArrendamiento,int relacionadoTI) {
        String arrendamiento="";
        String nombre_plan="";
    
        //REGLAS_NOMBRE_PLAN reglas_nombre_plan = new REGLAS_NOMBRE_PLAN();
        
                if (this.getST_BACKUP_BU_ForJSON()){
            arrendamiento="ARRENDAMIENTO DE EQUIPOS - ";
        }
        if (this.getProd_ForJSON() != null){
            if (this.getProd_ForJSON().getCO_PROD_ForJSON()==1){ // Internet
                return arrendamiento+"INTERNET SATELITAL "+this.DE_VELO_BAJA+" KBPS";
            } else { //Datos
                return arrendamiento+"ENLACE DE DATOS SATELITAL "+this.DE_VELO_BAJA+" KBPS";
            }
        } else {
            if (this.getCO_PROD_ForJSON()==1){ // Internet
                return arrendamiento+"INTERNET SATELITAL "+this.DE_VELO_BAJA+" KBPS";
            } else { //Datos
                return arrendamiento+"ENLACE DE DATOS SATELITAL "+this.DE_VELO_BAJA+" KBPS";
            }
        }
    }
    
    
    public void setCOD_PROD_PADRE(Integer COD_PROD_PADRE) {
        this.COD_PROD_PADRE = COD_PROD_PADRE;
    }   
    
    public void setCO_MONE_FACT(Integer CO_MONE_FACT) {
        this.CO_MONE_FACT = CO_MONE_FACT;
    }   
    
    public void setCO_PLAN(int CO_PLAN) {
        this.CO_PLAN = CO_PLAN;
    }

    public void setNO_PLAN(String NO_PLAN) {
        this.NO_PLAN = NO_PLAN;
    }

    public void setCO_PROD(int CO_PROD) {
        this.CO_PROD = CO_PROD;
    }

    public void setIM_MONTO(double IM_MONTO) {
        this.IM_MONTO = IM_MONTO;
    }

    public void setDE_VELO_SUBI(String DE_VELO_SUBI) {
        this.DE_VELO_SUBI = DE_VELO_SUBI;
    }

    public void setDE_VELO_BAJA(String DE_VELO_BAJA) {
        this.DE_VELO_BAJA = DE_VELO_BAJA;
    }

    public void setCO_PLAN_MEDI_INST(Integer CO_PLAN_MEDI_INST) {
        this.CO_PLAN_MEDI_INST = CO_PLAN_MEDI_INST;
    }
    public void setST_ELIM(Boolean ST_ELIM) {
        this.ST_ELIM = ST_ELIM;
    }     
    
    public void setST_BACKUP_BU(Boolean ST_BACKUP_BU) {
        this.ST_BACKUP_BU = ST_BACKUP_BU;
    } 
     
    public void setRELACIONADO_TI(Boolean RELACIONADO_TI) {
        this.RELACIONADO_TI = RELACIONADO_TI;
    } 
    
    public void setVELOCIDAD_OK_MAYORISTA(Boolean VELOCIDAD_OK_MAYORISTA) {
        this.VELOCIDAD_OK_MAYORISTA = VELOCIDAD_OK_MAYORISTA;
    }
    
    @JsonProperty("PRODUCTO_CARGA")
	public String getPRODUCTO_CARGA() {
		return PRODUCTO_CARGA;
	}

	public void setPRODUCTO_CARGA(String pRODUCTO_CARGA) {
		PRODUCTO_CARGA = pRODUCTO_CARGA;
	}
    
    
}
