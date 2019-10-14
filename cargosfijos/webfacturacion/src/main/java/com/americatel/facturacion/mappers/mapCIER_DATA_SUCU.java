/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.mappers;

import com.americatel.facturacion.models.CIER_DATA_SUCU;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;

/**
 *
 * @author crodas
 */
public interface mapCIER_DATA_SUCU {
    
    @Select("SELECT * FROM TMCIER_DATA_SUCU WHERE CO_CIER_DATA_SUCU=#{id}")
    @Results(value={
            @Result(property = "CO_CIER_DATA_SUCU",column="CO_CIER_DATA_SUCU",id = true), 
            @Result(property = "CO_CIER_DATA_NEGO",column="CO_CIER_DATA_NEGO"), 
            @Result(property = "CO_NEGO_SUCU",column="CO_NEGO_SUCU"), 
            @Result(property = "DE_DIRE_SUCU",column="DE_DIRE_SUCU"), 
            @Result(property = "CO_DEPA",column="CO_DEPA"), 
            @Result(property = "NO_DEPA",column="NO_DEPA"), 
            @Result(property = "CO_PROV",column="CO_PROV"), 
            @Result(property = "NO_PROV",column="NO_PROV"), 
            @Result(property = "CO_DIST",column="CO_DIST"), 
            @Result(property = "NO_DIST",column="NO_DIST"), 
            @Result(property = "DE_ORDE",column="DE_ORDE"),
        }
    )
    public CIER_DATA_SUCU getId(int id);
    
    @Insert("INSERT INTO TMCIER_DATA_SUCU (CO_NEGO_SUCU,CO_CIER_DATA_NEGO,DE_DIRE_SUCU,CO_DEPA,NO_DEPA,CO_PROV,NO_PROV,CO_DIST,NO_DIST,DE_ORDE) VALUES (#{CO_NEGO_SUCU},#{CO_CIER_DATA_NEGO},#{DE_DIRE_SUCU},#{CO_DEPA},#{NO_DEPA},#{CO_PROV},#{NO_PROV},#{CO_DIST},#{NO_DIST},#{DE_ORDE});")
    @SelectKey(statement="SELECT @@IDENTITY;", keyProperty="CO_CIER_DATA_SUCU", before=false, resultType=Integer.class)
    public void insertar(CIER_DATA_SUCU aThis);
    
}
