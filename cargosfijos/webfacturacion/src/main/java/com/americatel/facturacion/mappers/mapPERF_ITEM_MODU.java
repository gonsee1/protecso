/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.mappers;

import com.americatel.facturacion.models.ITEM_MODU;
import com.americatel.facturacion.models.PERF;
import com.americatel.facturacion.models.PERF_ITEM_MODU;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Many;
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
public interface mapPERF_ITEM_MODU {

    @Select("SELECT a.* FROM TMPERF_ITEM_MODU a INNER JOIN TMPERF b ON b.CO_PERF=a.CO_PERF INNER JOIN TMITEM_MODU c ON c.CO_ITEM_MODU=a.CO_ITEM_MODU WHERE b.NO_PERF LIKE '%#{NO_PERF}%' AND c.NO_ITEM_MODU LIKE '%#{NO_ITEM_MODU}%' AND a.ST_ELIM=0 ")
    @Results(value={
        @Result(property="CO_PERF",column="CO_PERF"),
        @Result(property="CO_ITEM_MODU",column="CO_ITEM_MODU"),
        @Result(property = "ST_ELIM",column="ST_ELIM"),
        @Result(property="perf", javaType=PERF.class,column="CO_PERF",one = @One(select = "com.americatel.facturacion.mappers.mapPERF.getId") ),
        @Result(property="item_modu", javaType=PERF.class,column="CO_ITEM_MODU",one = @One(select = "com.americatel.facturacion.mappers.mapITEM_MODU.getId") )
    })
    public List<PERF_ITEM_MODU> select(String NO_PERF,String NO_ITEM_MODU);
    
    @Select("SELECT a.* FROM TMPERF_ITEM_MODU a INNER JOIN TMPERF b ON b.CO_PERF=a.CO_PERF INNER JOIN TMITEM_MODU c ON c.CO_ITEM_MODU=a.CO_ITEM_MODU WHERE a.CO_PERF=#{CO_PERF} AND a.ST_ELIM=0")
    @Results(value={
        @Result(property="CO_PERF",column="CO_PERF"),
        @Result(property="CO_ITEM_MODU",column="CO_ITEM_MODU"),
        @Result(property = "ST_ELIM",column="ST_ELIM"),
        @Result(property="perf", javaType=PERF.class,column="CO_PERF",one = @One(select = "com.americatel.facturacion.mappers.mapPERF.getId") ),
        @Result(property="item_modu", javaType=PERF.class,column="CO_ITEM_MODU",one = @One(select = "com.americatel.facturacion.mappers.mapITEM_MODU.getId") )
    })
    public List<PERF_ITEM_MODU> selectByCO_PERF(Integer CO_PERF);    
    
    
    
    //@Update("UPDATE TMPERF_ITEM_MODU SET ST_ELIM=1 WHERE CO_PERF=#{CO_PERF}")
    @Delete("DELETE FROM TMPERF_ITEM_MODU WHERE CO_PERF=#{CO_PERF}")
    public int deleteByCO_PERF(@Param("CO_PERF") Integer CO_PERF);

    @Insert("INSERT INTO TMPERF_ITEM_MODU (CO_PERF,CO_ITEM_MODU) VALUES (#{CO_PERF},#{CO_ITEM_MODU})")
    public int insert(@Param("CO_PERF") Integer CO_PERF,@Param("CO_ITEM_MODU") Integer CO_ITEM_MODU);    
    /*
    @Insert("INSERT INTO TMITEM_MODU (NO_ITEM_MODU,CO_MODU,DE_PACK) VALUES ('${NO_ITEM_MODU}',${CO_MODU},'${DE_PACK}');")
    public int insert(PERF_ITEM_MODU Item_Modu);

    @Select("SELECT a.*,b.NO_MODU FROM TMITEM_MODU a INNER JOIN TMMODU b ON b.CO_MODU=a.CO_MODU WHERE CO_ITEM_MODU=${CO_ITEM_MODU}")
    public ITEM_MODU getId(@Param("CO_ITEM_MODU") int CO_ITEM_MODU);

    @Update("UPDATE TMITEM_MODU SET NO_ITEM_MODU='${NO_ITEM_MODU}',CO_MODU='${CO_MODU}',DE_PACK='${DE_PACK}' WHERE CO_ITEM_MODU=${CO_ITEM_MODU};")
    public void update(PERF_ITEM_MODU Item_Modu);

    @Delete("DELETE FROM TMITEM_MODU WHERE CO_ITEM_MODU=${CO_ITEM_MODU};")
    public void delete(PERF_ITEM_MODU Item_Modu);*/
            
}
