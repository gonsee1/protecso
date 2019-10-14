/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.mappers;


import com.americatel.facturacion.historial.Historial;
import com.americatel.facturacion.models.MONE_FACT;
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
public interface mapMONE_FACT {
    
    @Select("SELECT * FROM TMMONE_FACT WHERE CO_MONE_FACT=#{id} AND ST_ELIM=0;")
    public MONE_FACT getId(int id);
    
    @Update("UPDATE TMMONE_FACT SET NO_MONE_FACT='${NO_MONE_FACT}',DE_SIMB='${DE_SIMB}',"+Historial.stringMapperModificar+" WHERE CO_MONE_FACT=${CO_MONE_FACT} AND ST_ELIM=0;")
    public int update(MONE_FACT tipo_fact);

    //@Delete("DELETE FROM TMMONE_FACT WHERE CO_MONE_FACT=${CO_MONE_FACT};")
    @Update("UPDATE TMMONE_FACT SET ST_ELIM=1,"+Historial.stringMapperModificar+" WHERE CO_MONE_FACT=${CO_MONE_FACT};")
    public int delete(MONE_FACT tipo_fact);

    @Insert("INSERT INTO TMMONE_FACT (NO_MONE_FACT,DE_SIMB,"+Historial.stringMapperInsertarTitles+") VALUES ('${NO_MONE_FACT}','${DE_SIMB}',"+Historial.stringMapperInsertarValuesNumeral+")")
    public void insert(MONE_FACT tipo_fact);

    @Select("SELECT * FROM TMMONE_FACT WHERE NO_MONE_FACT like '%${NO_MONE_FACT}%' AND ST_ELIM=0;")
    public List<MONE_FACT> select(@Param("NO_MONE_FACT") String NO_MONE_FACT);    
}
