/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.mappers;

import com.americatel.facturacion.models.NEGO_HISTORICO;
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
public interface mapNEGO_HISTORICO {
    
    @Select("SELECT * FROM TMNEGO_HISTORICO WHERE CO_NEGO=#{CO_NEGO} ORDER BY FH_REGI ASC;")
    @Results(value={
            @Result(property = "CO_NEGO_HIST",column="CO_NEGO_HIST",id = true), 
            @Result(property = "CO_NEGO",column="CO_NEGO"), 
            @Result(property = "DE_INFORMACION",column="DE_INFORMACION"), 
            @Result(property = "FH_REGI",column="FH_REGI",javaType = Date.class), 
            @Result(property = "CO_USUA_REGI",column="CO_USUA_REGI"), 
            @Result(property = "usua",column = "CO_USUA_REGI",javaType = USUA.class,one = @One(select = "com.americatel.facturacion.mappers.mapUSUA.getid"),id = false)
        }
    )  
    public List<NEGO_HISTORICO> getByNego(int CO_NEGO);
    
    @Insert("INSERT INTO TMNEGO_HISTORICO (CO_NEGO,DE_INFORMACION,FH_REGI,CO_USUA_REGI) VALUES (#{CO_NEGO},#{DE_INFORMACION},#{FH_REGI},#{CO_USUA_REGI});")
    public void insert(NEGO_HISTORICO nego_historico);
}
