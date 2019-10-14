/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.mappers;


import com.americatel.facturacion.historial.Historial;
import com.americatel.facturacion.models.PERI_FACT;
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
public interface mapPERI_FACT {
    
    @Select("SELECT * FROM TMPERI_FACT WHERE CO_PERI_FACT=#{id} AND ST_ELIM=0")
    public PERI_FACT getId(int id);
    
    @Update("UPDATE TMPERI_FACT SET NO_PERI_FACT='${NO_PERI_FACT}',"+Historial.stringMapperModificar+" WHERE CO_PERI_FACT=${CO_PERI_FACT} AND ST_ELIM=0;")
    public int update(PERI_FACT tipo_fact);

    //@Delete("DELETE FROM TMPERI_FACT WHERE CO_PERI_FACT=${CO_PERI_FACT};")
    @Update("UPDATE TMPERI_FACT SET ST_ELIM=1 WHERE CO_PERI_FACT=${CO_PERI_FACT};")
    public int delete(PERI_FACT tipo_fact);

    @Insert("INSERT INTO TMPERI_FACT (NO_PERI_FACT,"+Historial.stringMapperInsertarTitles+") VALUES ('${NO_PERI_FACT}',"+Historial.stringMapperInsertarValuesNumeral+")")
    public void insert(PERI_FACT tipo_fact);

    @Select("SELECT * FROM TMPERI_FACT WHERE NO_PERI_FACT like '%${NO_PERI_FACT}%' AND ST_ELIM=0")
    public List<PERI_FACT> select(@Param("NO_PERI_FACT") String NO_PERI_FACT);    
}
