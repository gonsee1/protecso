/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.mappers;

import com.americatel.facturacion.historial.Historial;
import com.americatel.facturacion.models.AJUS;
import com.americatel.facturacion.models.CIER;
import com.americatel.facturacion.models.NEGO;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 *
 * @author crodas
 */
public interface mapAJUS {

    @Select("SELECT * FROM TMAJUS WHERE ST_PEND=1;")
    public List<AJUS> selectPendientes();

    @Delete("DELETE FROM TMAJUS WHERE ST_PEND=1;")
    public void borrarPendientes();

    @Insert("INSERT INTO TMAJUS (CO_NEGO,DE_GLOS,IM_MONT,ST_AFEC_IGV,ST_PEND,"+Historial.stringMapperInsertarTitles+") VALUES (#{CO_NEGO},#{DE_GLOS},#{IM_MONT},#{ST_AFEC_IGV},#{ST_PEND},"+Historial.stringMapperInsertarValuesNumeral+");")
    public void insert(AJUS ajus);

    @Select("SELECT * FROM TMAJUS WHERE (ST_PEND=1 OR CO_CIER_APLI=#{cier.CO_CIER}) AND CO_NEGO=#{nego.CO_NEGO};")
    public List<AJUS> getAjustesPendientes(@Param("nego") NEGO nego,@Param("cier") CIER cier);

    @Update("UPDATE TMAJUS SET CO_CIER_APLI=#{CO_CIER_APLI},ST_PEND=0 WHERE CO_AJUS=#{CO_AJUS} AND ST_PEND=1;")
    public void saveCierreAplico(AJUS aThis);
    
}
