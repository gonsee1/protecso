/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.mappers;

import com.americatel.facturacion.fncs.Pagination;
import com.americatel.facturacion.historial.Historial;
import com.americatel.facturacion.models.DEPA;
import com.americatel.facturacion.models.PROV;
import com.americatel.facturacion.models.PROV;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 *
 * @author crodas
 */
public interface mapPROV {    
    @Select("SELECT * FROM TMPROV WHERE CO_PROV=#{id} AND ST_ELIM=0")
    @Results(value={
            @Result(property = "CO_PROV",column="CO_PROV",id = true),
            @Result(property = "NO_PROV",column="NO_PROV"),
            @Result(property = "CO_DEPA",column="CO_DEPA"),   
            @Result(property = "ST_ELIM",column="ST_ELIM"),
            @Result(property = "depa",column = "CO_DEPA",javaType = DEPA.class,one = @One(select = "com.americatel.facturacion.mappers.mapDEPA.getId"),id = false)
        }
    )
    public PROV getId(int id);
    
    @Select("SELECT * FROM TMPROV WHERE ST_ELIM=0")
    public List<PROV> getAll();
    
    @Update("UPDATE TMPROV SET NO_PROV='${NO_PROV}',CO_DEPA='${CO_DEPA}',"+Historial.stringMapperModificar+" WHERE CO_PROV=${CO_PROV} AND ST_ELIM=0;")
    public int update(PROV depa);

    //@Delete("DELETE FROM TMPROV WHERE CO_PROV=${CO_PROV};")
    @Update("UPDATE TMPROV SET ST_ELIM=1,"+Historial.stringMapperModificar+" WHERE CO_PROV=${CO_PROV};")
    public int delete(PROV depa);

    @Insert("INSERT INTO TMPROV (NO_PROV,CO_DEPA,"+Historial.stringMapperInsertarTitles+") VALUES ('${NO_PROV}','${CO_DEPA}',"+Historial.stringMapperInsertarValuesNumeral+")")
    public void insert(PROV depa);

    @Select("SELECT * FROM TMPROV WHERE NO_PROV like '%${NO_PROV}%' AND ST_ELIM=0 ORDER BY [NO_PROV] OFFSET ${Pagination.getGroup()} ROWS FETCH NEXT #{Pagination.Limit} ROWS ONLY")
    @Results(value = {
            @Result(property = "CO_PROV",column="CO_PROV",id = true),
            @Result(property = "NO_PROV",column="NO_PROV"),
            @Result(property = "CO_DEPA",column="CO_DEPA"),
            @Result(property = "ST_ELIM",column="ST_ELIM"),
            @Result(property = "depa",column = "CO_DEPA",javaType = DEPA.class,one = @One(select = "com.americatel.facturacion.mappers.mapDEPA.getId"),id = false)
        }
    )    
    public List<PROV> select(@Param(value = "NO_PROV") String NO_PROV,@Param(value = "Pagination") Pagination pagination);
    
    @Select("SELECT count(*) FROM TMPROV WHERE NO_PROV like '%${NO_PROV}%' AND ST_ELIM=0")  
    public int getNumResultsSelect(@Param("NO_PROV") String NO_PROV);
    
    @Select("SELECT * FROM TMPROV WHERE (NO_PROV like '%${SEARCH}%')  AND ST_ELIM=0 ORDER BY [NO_PROV] OFFSET ${Pagination.getGroup()} ROWS FETCH NEXT #{Pagination.Limit} ROWS ONLY")
    @Results(value = {
            @Result(property = "CO_PROV",column="CO_PROV",id = true),
            @Result(property = "NO_PROV",column="NO_PROV"),
            @Result(property = "CO_DEPA",column="CO_DEPA"),
            @Result(property = "ST_ELIM",column="ST_ELIM"),
            @Result(property = "depa",column = "CO_DEPA",javaType = DEPA.class,one = @One(select = "com.americatel.facturacion.mappers.mapDEPA.getId"),id = false)
        }
    )    
    public List<PROV> selectBySEARCH(@Param(value = "SEARCH") String SEARCH,@Param(value = "Pagination")  Pagination pagination);

    @Select("SELECT count(*) FROM TMPROV WHERE (NO_PROV like '%${SEARCH}%') AND ST_ELIM=0")    
    public int getNumResultsSEARCH(@Param(value = "SEARCH") String SEARCH);

    @Select("SELECT * FROM TMPROV WHERE CO_DEPA=#{CO_DEPA} AND ST_ELIM=0")
    public List<PROV> selectByDepa(DEPA depa);
    
    @Select("SELECT TOP 1 * FROM TMPROV WHERE CO_DEPA=${CO_DEPA} AND NO_PROV='${NO_PROV}' AND ST_ELIM=0")
    public PROV selectByDepaAndNom(PROV prov);
}
