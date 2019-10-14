/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.mappers;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.mapping.StatementType;

/**
 *
 * @author crodas
 */
public interface mapCURSOR {
    @Select("${query}")
    public List<Map<String, Object>> select(@Param("query") String query);
    

    @Select("${query}")
    @Options(statementType = StatementType.CALLABLE)
    public List<Map<String, Object>> callSelect(@Param("query") String query);    
}
