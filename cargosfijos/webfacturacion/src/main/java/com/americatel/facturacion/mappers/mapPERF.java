/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.mappers;

import com.americatel.facturacion.models.ITEM_MODU;
import com.americatel.facturacion.models.PERF;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 *
 * @author crodas
 */
public interface mapPERF {
    /*
    @Select("SELECT * FROM TMPERF WHERE CO_PERF=${id}")
    public PERF getId(@Param("id") int id);
    */
    @Select("SELECT b.* FROM TMPERF_ITEM_MODU a INNER JOIN TMITEM_MODU b ON b.CO_ITEM_MODU=a.CO_ITEM_MODU AND a.CO_PERF=${CO_PERF} AND a.ST_ELIM=0;")
    public List<ITEM_MODU> getItemModulosByPerfil(@Param("CO_PERF") int codigo_perfil); 
    
    
    @Select("SELECT CO_PERF,NO_PERF FROM TMPERF WHERE CO_PERF=#{id} AND ST_ELIM=0")
    public PERF getId(Integer id);    
    /*
    @Select("SELECT CO_PERF,NO_PERF FROM TMPERF WHERE CO_PERF=${id}")
    @Results(value={
        @Result(id = true,column = "CO_PERF",property = "CO_PERF",javaType = Integer.class),
        @Result(id = false,column = "NO_PERF",property = "NO_PERF",javaType = String.class),
        @Result(property = "lista",column = "CO_PERF",many = @Many(select = "com.americatel.facturacion.mappers.mapPERF.getzzz"),javaType = List.class)
    })
    public PERF getId(@Param("id") int id);
    
    @Select("SELECT b.* FROM TMPERF_ITEM_MODU a INNER JOIN TMITEM_MODU b ON b.CO_ITEM_MODU=a.CO_ITEM_MODU AND a.CO_PERF=#{CO_PERF}")
    public List<ITEM_MODU> getzzz(String CO_PERF);*/
    
    @Select("SELECT * FROM TMPERF WHERE ST_ELIM=0")
    public List<PERF> getAll();
    
    @Update("UPDATE TMPERF SET NO_PERF='${NO_PERF}' WHERE CO_PERF=${CO_PERF} AND ST_ELIM=0;")
    public int update(PERF modu);

    //@Delete("DELETE FROM TMPERF WHERE CO_PERF=${CO_PERF};")
    @Update("UPDATE TMPERF SET ST_ELIM=1 WHERE CO_PERF=${CO_PERF};")
    public int delete(PERF modu);

    @Insert("INSERT INTO TMPERF (NO_PERF) VALUES ('${NO_PERF}')")
    public void insert(PERF modu);

    @Select("SELECT * FROM TMPERF WHERE NO_PERF like '%${NO_PERF}%' AND ST_ELIM=0")
    public List<PERF> select(@Param("NO_PERF") String NO_PERF);
    
}
