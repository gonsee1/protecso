/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.mappers;

import com.americatel.facturacion.models.DEPA;
import com.americatel.facturacion.models.PLAN_MEDI_INST;
import com.americatel.facturacion.models.PROD;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

/**
 *
 * @author crodas
 */
public interface mapPLAN_MEDI_INST {
    @Select("SELECT * FROM TMPLAN_MEDI_INST WHERE CO_PLAN_MEDI_INST=#{id} AND ST_ELIM=0")
    public PLAN_MEDI_INST getId(int id);    
    
    @Select("SELECT * FROM TMPLAN_MEDI_INST WHERE NO_PLAN_MEDI_INST like '%${NO_PLAN_MEDI_INST}%' AND ST_ELIM=0")
    public List<PLAN_MEDI_INST> select(@Param("NO_PLAN_MEDI_INST") String NO_PLAN_MEDI_INST);     
    
    @Select("SELECT * FROM TMPLAN_MEDI_INST WHERE COD_PROD_PADRE=#{CO_PROD} AND ST_ELIM=0")
    public List<Map<String, Object>> selectByProd(@Param("CO_PROD") String CO_PROD);     
        
}
