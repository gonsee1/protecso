package com.americatel.facturacion.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import com.americatel.facturacion.models.DETALLE_DOCUMENTO;


public interface mapDETALLE_DOCUMENTO {

    @Insert("INSERT INTO TMDETALLE_DOCUMENTO"+
            "(CO_CIER,CO_FACT,TIP_SERV,CO_TIPO_GLOS,NOMBRE_SERVICIO,MON_SERVICIO,ABREV_TIPO_DOCUMENTO) VALUES "+
            "(#{CO_CIER},#{CO_FACT},#{TIP_SERV},#{CO_TIPO_GLOS},#{NOMBRE_SERVICIO},#{MON_SERVICIO},#{ABREV_TIPO_DOCUMENTO}); ")
    public void insertar(DETALLE_DOCUMENTO documento);
    
    
    @Delete("DELETE FROM TMDETALLE_DOCUMENTO WHERE CO_CIER=#{CO_CIER} ;")
    public void delete(DETALLE_DOCUMENTO documento);
    
    @Select("SELECT * FROM TMDETALLE_DOCUMENTO WHERE CO_FACT=#{coFact} ;")
    public List<DETALLE_DOCUMENTO> getDetalleForCodFact(Integer coFact);
    
}
