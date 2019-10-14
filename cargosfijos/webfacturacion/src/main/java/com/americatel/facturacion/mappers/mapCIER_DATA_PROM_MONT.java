/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.mappers;

import com.americatel.facturacion.models.CIER_DATA_PROM_MONT;
import org.apache.ibatis.annotations.Insert;

/**
 *
 * @author crodas
 */
public interface mapCIER_DATA_PROM_MONT {

    @Insert("INSERT INTO TMCIER_DATA_PROM_MONT (CO_CIER_DATA_SUCU,CO_CIER_DATA_NEGO,CO_RECI,CO_FACT,IM_MONT,DE_NOMB) VALUES (#{CO_CIER_DATA_SUCU},#{CO_CIER_DATA_NEGO},#{CO_RECI},#{CO_FACT},#{IM_MONT},#{DE_NOMB});")
    public void insertar(CIER_DATA_PROM_MONT aThis);
    
}
