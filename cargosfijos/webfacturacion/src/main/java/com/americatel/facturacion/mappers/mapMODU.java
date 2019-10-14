/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.mappers;

import com.americatel.facturacion.models.MODU;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;

/**
 *
 * @author crodas
 */
public interface mapMODU  {
    @Select("SELECT * FROM TMMODU WHERE CO_MODU=#{id} AND ST_ELIM=0;")
    public MODU getId(int id);
    
    @Select("SELECT * FROM TMMODU WHERE ST_ELIM=0;")
    public List<MODU> getAll();
    
    //@UpdateProvider(type =Nego.class,method = "abc")

    @Update("UPDATE TMMODU SET NO_MODU=#{NO_MODU},DE_PACK=#{DE_PACK},DE_ICON_CLSS=#{DE_ICON_CLSS} WHERE CO_MODU=#{CO_MODU} AND ST_ELIM=0;")
    public int update(MODU modu);

    //@Delete("DELETE FROM TMMODU WHERE CO_MODU=${CO_MODU} ;")
    @Update("UPDATE TMMODU SET ST_ELIM=1 WHERE CO_MODU=${CO_MODU} ;")
    public int delete(MODU modu);

    @Insert("INSERT INTO TMMODU (NO_MODU,DE_PACK,DE_ICON_CLSS) VALUES (#{NO_MODU},#{DE_PACK},#{DE_ICON_CLSS});")
    public void insert(MODU modu);

    @Select("SELECT * FROM TMMODU WHERE NO_MODU like '%${NO_MODU}%' AND ST_ELIM=0;")
    public List<MODU> select(@Param("NO_MODU") String NO_MODU);
    
}

