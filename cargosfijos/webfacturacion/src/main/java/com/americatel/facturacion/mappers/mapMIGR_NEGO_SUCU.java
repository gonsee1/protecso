/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.mappers;

import com.americatel.facturacion.historial.Historial;
import com.americatel.facturacion.models.MIGR_NEGO_SUCU;
import com.americatel.facturacion.models.NEGO;
import com.americatel.facturacion.models.NEGO_SUCU;
import com.americatel.facturacion.models.USUA;
import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

/**
 *
 * @author rordonez
 */
public interface mapMIGR_NEGO_SUCU {
    
    @Select("SELECT * FROM TMMIGR_NEGO_SUCU WHERE CO_NEGO_ORIG=#{CO_NEGO};")
    @Results(value={
            @Result(property = "CO_MIGR_NEGO_SUCU",column="CO_MIGR_NEGO_SUCU",id = true), 
            @Result(property = "CO_NEGO_ORIG",column="CO_NEGO_ORIG"), 
            @Result(property = "CO_NEGO_DEST",column="CO_NEGO_DEST"), 
            @Result(property = "CO_NEGO_SUCU",column="CO_NEGO_SUCU"), 
            @Result(property = "FH_REGI",column="FH_REGI",javaType = Date.class), 
            @Result(property = "CO_USUA_REGI",column="CO_USUA_REGI"), 
            @Result(property = "nego_sucu",column = "CO_NEGO_SUCU",javaType = NEGO_SUCU.class,one = @One(select = "com.americatel.facturacion.mappers.mapNEGO_SUCU.getId"),id = false),
            @Result(property = "usua",column = "CO_USUA_REGI",javaType = USUA.class,one = @One(select = "com.americatel.facturacion.mappers.mapUSUA.getid"),id = false)
        }
    )  
    public List<MIGR_NEGO_SUCU> getByNego(int CO_NEGO);
    
    @Insert("INSERT INTO TMMIGR_NEGO_SUCU (CO_NEGO_ORIG,CO_NEGO_DEST,CO_NEGO_SUCU,FH_REGI,CO_USUA_REGI) VALUES (#{CO_NEGO_ORIG},#{CO_NEGO_DEST},#{CO_NEGO_SUCU},#{FH_REGI},#{CO_USUA_REGI});")
    public void insert(MIGR_NEGO_SUCU migr_nego_sucu);
}
