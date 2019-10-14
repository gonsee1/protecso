/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.mappers;

import com.americatel.facturacion.models.NEGO_SALD_CERO;
import java.util.List;
import org.apache.ibatis.annotations.Select;

/**
 *
 * @author rordonez
 */
public interface mapNEGO_SALD_CERO {
    
    @Select("SELECT * FROM TMNEGO_SALD_CERO WHERE ST_ELIM=0;")  
    public List<NEGO_SALD_CERO> getSaldosCero();
    
    @Select("SELECT * FROM TMNEGO_SALD_CERO WHERE CO_NEGO=#{CO_NEGO} AND ST_ELIM=0;")  
    public NEGO_SALD_CERO getSaldoCeroByNego(Integer CO_NEGO);
    
}
