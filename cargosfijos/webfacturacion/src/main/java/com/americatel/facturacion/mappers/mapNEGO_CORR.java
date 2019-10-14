/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.mappers;

import com.americatel.facturacion.historial.Historial;
import com.americatel.facturacion.models.NEGO;
import com.americatel.facturacion.models.NEGO_CORR;
import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 *
 * @author crodas
 */
public interface mapNEGO_CORR {

    @Insert("INSERT INTO TMNEGO_CORR (CO_NEGO,DE_CORR,ST_ACTI) VALUES (#{CO_NEGO},#{DE_CORR},1);")
    public void insert(NEGO_CORR corr);

    @Select("SELECT * FROM TMNEGO_CORR WHERE ST_ACTI=1 AND CO_NEGO=#{CO_NEGO}")
    public List<NEGO_CORR> selectActivosByCO_NEGO(@Param("CO_NEGO") Integer CO_NEGO);
    
    @Update("UPDATE TMNEGO_CORR SET ST_ACTI=0 WHERE CO_NEGO=${CO_NEGO};")
    public int deleteLogic(@Param("CO_NEGO") Integer CO_NEGO);
    
}
