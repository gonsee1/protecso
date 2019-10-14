/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.mappers;

import com.americatel.facturacion.models.ITEM_MODU;
import com.americatel.facturacion.models.MODU;
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
public interface mapITEM_MODU {

    @Select("SELECT a.* FROM TMITEM_MODU a INNER JOIN TMMODU b ON b.CO_MODU=a.CO_MODU WHERE NO_ITEM_MODU LIKE '%${NO_ITEM_MODU}%' AND a.ST_ELIM=0")
    @Results(value={
        @Result(property = "CO_ITEM_MODU",column = "CO_ITEM_MODU",id = true),
        @Result(property = "NO_ITEM_MODU",column = "NO_ITEM_MODU"),
        @Result(property = "CO_MODU",column = "CO_MODU"),
        @Result(property = "DE_PACK",column = "DE_PACK"),
        @Result(property = "ST_ELIM",column="ST_ELIM"),
        @Result(property = "modu",column = "CO_MODU",javaType = MODU.class,one =@One(select = "com.americatel.facturacion.mappers.mapMODU.getId"))
    })
    public List<ITEM_MODU> select(@Param("NO_ITEM_MODU") String NO_ITEM_MODU); 

    @Insert("INSERT INTO TMITEM_MODU (NO_ITEM_MODU,CO_MODU,DE_PACK) VALUES ('${NO_ITEM_MODU}',${CO_MODU},'${DE_PACK}');")
    public int insert(ITEM_MODU Item_Modu);

    @Select("SELECT a.* FROM TMITEM_MODU a INNER JOIN TMMODU b ON b.CO_MODU=a.CO_MODU WHERE CO_ITEM_MODU=#{CO_ITEM_MODU} AND a.ST_ELIM=0")
    @Results(value={
        @Result(property = "CO_ITEM_MODU",column = "CO_ITEM_MODU",id = true),
        @Result(property = "NO_ITEM_MODU",column = "NO_ITEM_MODU"),
        @Result(property = "CO_MODU",column = "CO_MODU"),
        @Result(property = "DE_PACK",column = "DE_PACK"), 
        @Result(property = "ST_ELIM",column="ST_ELIM"),
        @Result(property = "modu",column = "CO_MODU",javaType = MODU.class,one =@One(select = "com.americatel.facturacion.mappers.mapMODU.getId"))
    })    
    public ITEM_MODU getId(int CO_ITEM_MODU);

    @Update("UPDATE TMITEM_MODU SET NO_ITEM_MODU='${NO_ITEM_MODU}',CO_MODU='${CO_MODU}',DE_PACK='${DE_PACK}' WHERE CO_ITEM_MODU=${CO_ITEM_MODU};")
    public void update(ITEM_MODU Item_Modu);

    //@Delete("DELETE FROM TMITEM_MODU WHERE CO_ITEM_MODU=${CO_ITEM_MODU};")
    @Update("¨UPDATE TMITEM_MODU SET ST_ELIM=1 WHERE CO_ITEM_MODU=${CO_ITEM_MODU};")
    public void delete(ITEM_MODU Item_Modu);

}
