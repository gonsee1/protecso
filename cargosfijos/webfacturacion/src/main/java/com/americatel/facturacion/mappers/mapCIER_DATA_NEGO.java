/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.mappers;

import com.americatel.facturacion.models.CIER_DATA_NEGO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.SelectKey;

/**
 *
 * @author crodas
 */
public interface mapCIER_DATA_NEGO {
    
    @Insert("INSERT INTO TMCIER_DATA_NEGO (CO_NEGO,CO_CIER) VALUES (#{CO_NEGO},#{CO_CIER})")
    @SelectKey(statement="SELECT @@IDENTITY;", keyProperty="CO_CIER_DATA_NEGO", before=false, resultType=Integer.class)
    public void insertar(CIER_DATA_NEGO aThis);
    
}
