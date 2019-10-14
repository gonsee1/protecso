/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.mappers;

import com.americatel.facturacion.models.TIPO_CLIE;
import java.util.List;
import org.apache.ibatis.annotations.Select;

/**
 *
 * @author crodas
 */
public interface mapTIPO_CLIE {

    @Select("SELECT * FROM TMTIPO_CLIE WHERE ST_ELIM=0;")
    public List<TIPO_CLIE> getAll();
    
}
