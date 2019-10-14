/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.mappers;

import com.americatel.facturacion.historial.Historial;
import com.americatel.facturacion.models.CIER;
import com.americatel.facturacion.models.MONE_FACT;
import com.americatel.facturacion.models.NEGO;
import com.americatel.facturacion.models.NEGO_SALD;
import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

/**
 *
 * @author crodas
 */
public interface mapNEGO_SALD {

    @Insert("INSERT INTO TMNEGO_SALD (CO_NEGO,CO_CIER,CO_MONE_FACT,IM_MONT,DE_SALD,ST_ELIM,"+Historial.stringMapperInsertarTitles+") VALUES (#{CO_NEGO},#{CO_CIER},#{CO_MONE_FACT},#{IM_MONT},#{DE_SALD},#{ST_ELIM},"+Historial.stringMapperInsertarValuesNumeral+");")
    public void insertar(NEGO_SALD aThis);
    
    @Select("SELECT * FROM TMNEGO_SALD WHERE CO_CIER=#{CO_CIER} AND IM_MONT!=0 AND ST_ELIM=0;")  
    @Results(value={
            @Result(property = "CO_NEGO_SALD",column="CO_NEGO_SALD",id = true),
            @Result(property = "CO_NEGO",column="CO_NEGO"), 
            @Result(property = "CO_MONE_FACT",column="CO_MONE_FACT"),
            @Result(property = "ST_ELIM",column="ST_ELIM"),
            @Result(property = "nego",column = "CO_NEGO",javaType = NEGO.class,one = @One(select = "com.americatel.facturacion.mappers.mapNEGO.getIdClientAndProduct"),id = false),
            @Result(property = "mone_fact",column = "CO_MONE_FACT",javaType = MONE_FACT.class,one = @One(select = "com.americatel.facturacion.mappers.mapMONE_FACT.getId"),id = false)
        }
    )
    public List<NEGO_SALD> getSaldos(CIER cier);

    @Select("SELECT * FROM TMMONE_FACT WHERE CO_MONE_FACT=#{CO_MONE_FACT} AND ST_ELIM=0")
    public MONE_FACT getMonedaFacturacion(NEGO_SALD nego_sald);    
}
