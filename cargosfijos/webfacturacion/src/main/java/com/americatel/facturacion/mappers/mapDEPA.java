/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.mappers;

import com.americatel.facturacion.fncs.Pagination;
import com.americatel.facturacion.historial.Historial;
import com.americatel.facturacion.models.DEPA;
import com.americatel.facturacion.models.DEPA;
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
public interface mapDEPA {
    
    @Select("SELECT * FROM TMDEPA WHERE CO_DEPA=#{id} AND ST_ELIM=0")
    public DEPA getId(@Param("id") int id);
    
    @Select("SELECT * FROM TMDEPA WHERE ST_ELIM=0")
    public List<DEPA> getAll();
    
    @Update("UPDATE TMDEPA SET NO_DEPA='${NO_DEPA}',"+Historial.stringMapperModificar+" WHERE CO_DEPA=${CO_DEPA} AND ST_ELIM=0;")
    public int update(DEPA depa);

    //@Delete("DELETE FROM TMDEPA WHERE CO_DEPA=${CO_DEPA};")    
    @Update("UPDATE TMDEPA SET ST_ELIM=1,"+Historial.stringMapperModificar+" WHERE CO_DEPA=${CO_DEPA}")
    public int delete(DEPA depa);

    @Insert("INSERT INTO TMDEPA (NO_DEPA,"+Historial.stringMapperInsertarTitles+") VALUES ('${NO_DEPA}',"+Historial.stringMapperInsertarValuesNumeral+")")
    public void insert(DEPA depa);

   // @Select("SELECT * FROM TMDEPA WHERE NO_DEPA like '%${NO_DEPA}%' AND ST_ELIM=0 ORDER BY [NO_DEPA] OFFSET ${Pagination.getGroup()} ROWS FETCH NEXT #{Pagination.Limit} ROWS ONLY")
    @Select("SELECT * FROM TMDEPA WHERE NO_DEPA like '%${NO_DEPA}%' AND ST_ELIM=0 ORDER BY [NO_DEPA]")
    public List<DEPA> select(@Param("NO_DEPA") String NO_DEPA, @Param(value = "Pagination") Pagination pagination);
    
    @Select("SELECT count(*) FROM TMDEPA WHERE (NO_DEPA like '%${NO_DEPA}%') AND ST_ELIM=0; ")
    public int getNumResultsSelect(@Param(value = "NO_DEPA") String NO_DEPA);
    
    @Select("SELECT * FROM TMDEPA WHERE (NO_DEPA like '%${SEARCH}%')  AND ST_ELIM=0 ORDER BY [NO_DEPA] OFFSET ${Pagination.getGroup()} ROWS FETCH NEXT #{Pagination.Limit} ROWS ONLY")   
    public List<DEPA> selectBySEARCH(@Param(value = "SEARCH") String SEARCH,@Param(value = "Pagination")  Pagination pagination);
    
    @Select("SELECT count(*) FROM TMDEPA WHERE (NO_DEPA like '%${SEARCH}%') AND ST_ELIM=0")    
    public int getNumResultsSEARCH(@Param(value = "SEARCH") String SEARCH);
    
    @Select("SELECT TOP 1 * FROM TMDEPA WHERE NO_DEPA='${NO_DEPA}' AND ST_ELIM=0")
    public DEPA selectByNOM(DEPA depa);  
}
