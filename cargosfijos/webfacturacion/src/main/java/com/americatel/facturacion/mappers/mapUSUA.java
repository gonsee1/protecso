/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.mappers;

import com.americatel.facturacion.models.ITEM_MODU;
import com.americatel.facturacion.models.PERF;
import com.americatel.facturacion.models.USUA;
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
public interface mapUSUA {
    @Select("SELECT * FROM TMUSUA WHERE DE_USER=#{usuario} AND DE_PASS='${clave}' AND ST_ELIM=0;")
    @Results(value={
            @Result(property = "CO_USUA",column="CO_USUA",id = true),
            @Result(property = "CO_PERF",column="CO_PERF"),
            @Result(property = "NO_USUA",column="NO_USUA"),
            @Result(property = "AP_USUA",column="AP_USUA"), 
            @Result(property = "DE_USER",column="DE_USER"), 
            @Result(property = "DE_PASS",column="DE_PASS"), 
            @Result(property = "DE_TOCK",column="DE_TOCK"), 
            @Result(property = "ST_ELIM",column="ST_ELIM"), 
            @Result(property = "perf",column = "CO_PERF",javaType = PERF.class,one = @One(select = "com.americatel.facturacion.mappers.mapPERF.getId"),id = false),
        }
    )
    public USUA getUserFromLogin(@Param("usuario") String usuario,@Param("clave") String clave);
    
    @Select("SELECT * FROM TMUSUA WHERE DE_USER=#{usuario};")
    public USUA getByUser(@Param("usuario") String usuario);    
    
    @Select("SELECT * FROM TMUSUA WHERE CO_USUA=#{id} AND ST_ELIM=0;")
    public USUA getid(@Param("id") int id);
    
    //////////////
    
    @Select("SELECT a.*,b.NO_PERF FROM TMUSUA a INNER JOIN TMPERF b ON b.CO_PERF=a.CO_PERF WHERE (NO_USUA LIKE '%${NO_USUA}%' OR AP_USUA LIKE '%${AP_USUA}%') AND a.ST_ELIM=0")
    public List<USUA> select(USUA usua); 

    @Insert("INSERT INTO TMUSUA (CO_PERF,NO_USUA,AP_USUA,DE_CORR,DE_USER,DE_PASS) VALUES (#{CO_PERF},#{NO_USUA},#{AP_USUA},#{DE_CORR},#{DE_USER},#{DE_PASS});")
    public int insert(USUA usua);

    @Select("SELECT a.*,b.NO_PERF FROM TMUSUA a INNER JOIN TMPERF b ON b.CO_PERF=a.CO_PERF WHERE CO_USUA=${CO_USUA}  AND a.ST_ELIM=0;")
    public USUA getId(@Param("CO_USUA") int CO_USUA);

    @Update("UPDATE TMUSUA SET CO_PERF='${CO_PERF}',NO_USUA='${NO_USUA}',AP_USUA='${AP_USUA}',DE_CORR='${DE_CORR}',DE_USER='${DE_USER}',DE_PASS='${DE_PASS}' WHERE CO_USUA=${CO_USUA} AND ST_ELIM=0;")
    public void update(USUA usua);
 
    //@Delete("DELETE FROM TMUSUA WHERE CO_USUA=${CO_USUA};")
    @Update("UPDATE TMUSUA SET ST_ELIM=1 WHERE CO_USUA=${CO_USUA};")
    public void delete(USUA usua);
            
}
