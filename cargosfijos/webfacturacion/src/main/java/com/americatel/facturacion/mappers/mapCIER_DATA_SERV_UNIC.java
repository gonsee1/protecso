/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.mappers;

import com.americatel.facturacion.models.CIER_DATA_SERV_UNIC;
import org.apache.ibatis.annotations.Insert;

/**
 *
 * @author crodas
 */
public interface mapCIER_DATA_SERV_UNIC {
    @Insert("INSERT INTO TMCIER_DATA_SERV_UNIC (CO_CIER_DATA_SUCU,CO_RECI,CO_FACT,IM_MONT,DE_NOMB, CO_BOLE) VALUES (#{CO_CIER_DATA_SUCU},#{CO_RECI},#{CO_FACT},#{IM_MONT},#{DE_NOMB}, #{CO_BOLE});")
    public void insertar(CIER_DATA_SERV_UNIC aThis);
    
}
