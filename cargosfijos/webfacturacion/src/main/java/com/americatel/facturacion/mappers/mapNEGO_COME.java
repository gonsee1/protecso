/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.mappers;

import com.americatel.facturacion.models.NEGO_COME;
import com.americatel.facturacion.models.USUA;
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
public interface mapNEGO_COME {
    
    @Insert("INSERT INTO TMNEGO_COME (CO_USUA,CO_NEGO,DE_COME,FH_CREO) VALUES (#{CO_USUA},#{CO_NEGO},#{DE_COME},#{FH_CREO});")
    public void insert(NEGO_COME nego_prom);

    @Select("SELECT * FROM TMNEGO_COME WHERE CO_NEGO=#{CO_NEGO}")
    @Results(value={
            @Result(property = "CO_USUA",column="CO_USUA"),
            @Result(property = "usua",column = "CO_USUA",javaType = USUA.class,one = @One(select = "com.americatel.facturacion.mappers.mapUSUA.getid"),id = false)
        }
    )    
    public List<NEGO_COME> selectByCO_NEGO(Integer CO_NEGO);
    
}
