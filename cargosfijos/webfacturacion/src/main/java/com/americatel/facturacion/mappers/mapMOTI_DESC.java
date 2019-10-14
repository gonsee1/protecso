/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.mappers;

import com.americatel.facturacion.models.MOTI_DESC;
import java.util.List;
import org.apache.ibatis.annotations.Select;

/**
 *
 * @author rordonez
 */
public interface mapMOTI_DESC {
    
    @Select("SELECT * FROM TMMOTI_DESC WHERE CO_MOTI_DESC=#{id}")
    public MOTI_DESC getId(int id);
    
    @Select("SELECT * FROM TMMOTI_DESC")
    public List<MOTI_DESC> getAll();
}
