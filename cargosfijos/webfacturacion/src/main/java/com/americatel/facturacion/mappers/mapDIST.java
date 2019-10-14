/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.mappers;

import com.americatel.facturacion.fncs.Pagination;
import com.americatel.facturacion.historial.Historial;
import com.americatel.facturacion.models.DEPA;
import com.americatel.facturacion.models.DIST;
import com.americatel.facturacion.models.DIST;
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
public interface mapDIST {   
    
    @Select("SELECT * FROM TMDIST WHERE CO_DIST=#{id} AND ST_ELIM=0 ")
    public DIST getOnlyId(int id);
    
    @Select("SELECT * FROM TMDIST WHERE CO_DIST=#{id} AND ST_ELIM=0 ")
    @Results(value={
            @Result(property = "CO_DIST",column="CO_DIST",id = true),
            @Result(property = "NO_DIST",column="NO_DIST"),
            @Result(property = "CO_PROV",column="CO_PROV"), 
            @Result(property = "ST_ELIM",column="ST_ELIM"),
            @Result(property = "prov",column = "CO_PROV",javaType = PROV.class,one = @One(select = "com.americatel.facturacion.mappers.mapPROV.getId"),id = false)
        }
    )
    public DIST getId(int id);
    
    @Select("SELECT * FROM TMDIST WHERE ST_ELIM=0")
    public List<DIST> getAll();
    
    @Update("UPDATE TMDIST SET NO_DIST='${NO_DIST}',CO_PROV='${CO_PROV}',"+Historial.stringMapperModificar+" WHERE CO_DIST=${CO_DIST} AND ST_ELIM=0;")
    public int update(DIST dist);

    //@Delete("DELETE FROM TMDIST WHERE CO_DIST=${CO_DIST};")
    @Update("UPDATE TMDIST SET ST_ELIM=1,"+Historial.stringMapperModificar+" WHERE CO_DIST=${CO_DIST};")
    public int delete(DIST dist);

    @Insert("INSERT INTO TMDIST (NO_DIST,CO_PROV,"+Historial.stringMapperInsertarTitles+") VALUES ('${NO_DIST}','${CO_PROV}',"+Historial.stringMapperInsertarValuesNumeral+")")
    public void insert(DIST dist);

    @Select("SELECT * FROM TMDIST WHERE NO_DIST like '%${NO_DIST}%' AND ST_ELIM=0 ORDER BY [NO_DIST] OFFSET ${Pagination.getGroup()} ROWS FETCH NEXT #{Pagination.Limit} ROWS ONLY;")
    @Results(value = {
            @Result(property = "CO_DIST",column="CO_DIST",id = true),
            @Result(property = "NO_DIST",column="NO_DIST"),
            @Result(property = "CO_PROV",column="CO_PROV"),
            @Result(property = "ST_ELIM",column="ST_ELIM"),
            @Result(property = "prov",column = "CO_PROV",javaType = PROV.class,one = @One(select = "com.americatel.facturacion.mappers.mapPROV.getId"),id = false)
        }
    )    
    public List<DIST> select(@Param("NO_DIST") String NO_DIST, @Param(value = "Pagination") Pagination pagination);
    
    @Select("SELECT count(*) FROM TMDIST WHERE NO_DIST like '%${NO_DIST}%' AND ST_ELIM=0")  
    public int getNumResultsSelect(@Param("NO_DIST") String NO_DIST);
    
    @Select("SELECT * FROM TMDIST WHERE NO_DIST like '%${SEARCH}%' AND ST_ELIM=0 ORDER BY [NO_DIST] OFFSET ${Pagination.getGroup()} ROWS FETCH NEXT #{Pagination.Limit} ROWS ONLY;")
    @Results(value = {
            @Result(property = "CO_DIST",column="CO_DIST",id = true),
            @Result(property = "NO_DIST",column="NO_DIST"),
            @Result(property = "CO_PROV",column="CO_PROV"),
            @Result(property = "ST_ELIM",column="ST_ELIM"),
            @Result(property = "prov",column = "CO_PROV",javaType = PROV.class,one = @One(select = "com.americatel.facturacion.mappers.mapPROV.getId"),id = false)
        }
    )     
    public List<DIST> selectBySEARCH(@Param(value = "SEARCH") String SEARCH,@Param(value = "Pagination")  Pagination pagination);

    @Select("SELECT count(*) FROM TMDIST WHERE (NO_DIST like '%${SEARCH}%') AND ST_ELIM=0")    
    public int getNumResultsSEARCH(@Param(value = "SEARCH") String SEARCH);
    
    @Select("SELECT * FROM TMDIST WHERE CO_PROV=#{CO_PROV} AND ST_ELIM=0;") 
    @Results(value = {
            @Result(property = "CO_DIST",column="CO_DIST",id = true),
            @Result(property = "NO_DIST",column="NO_DIST"),
            @Result(property = "CO_PROV",column="CO_PROV"),
            @Result(property = "ST_ELIM",column="ST_ELIM"),
            @Result(property = "prov",column = "CO_PROV",javaType = PROV.class,one = @One(select = "com.americatel.facturacion.mappers.mapPROV.getId"),id = false)
        }
    )     
    public List<DIST> selectByProv(PROV prov);
    
    @Select("SELECT TOP 1 * FROM TMDIST WHERE CO_PROV=${CO_PROV} AND NO_DIST='${NO_DIST}' AND ST_ELIM=0;")
    public DIST selectByDepAndProvAndNom(DIST dist);


}
