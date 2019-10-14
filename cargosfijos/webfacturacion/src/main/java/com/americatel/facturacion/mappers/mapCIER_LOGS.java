/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.mappers;

import com.americatel.facturacion.historial.Historial;
import com.americatel.facturacion.models.CIER_LOGS;
import org.apache.ibatis.annotations.Insert;

/**
 *
 * @author crodas
 */
public interface mapCIER_LOGS {

    @Insert("INSERT INTO TMCIER_LOGS (CO_CIER,CO_NEGO,CO_NEGO_SUCU,ST_ERRO,DE_LOG,"+Historial.stringMapperInsertarTitles+") VALUES "
            + "(#{CO_CIER},#{CO_NEGO},#{CO_NEGO_SUCU},#{ST_ERRO},#{DE_LOG},"+Historial.stringMapperInsertarValuesNumeral+");")
    public void insertar(CIER_LOGS cier_logs);
    
}
