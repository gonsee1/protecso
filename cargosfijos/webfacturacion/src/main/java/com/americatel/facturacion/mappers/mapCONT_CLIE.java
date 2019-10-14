/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.mappers;

import com.americatel.facturacion.models.CONT_CLIE;
import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;

/**
 *
 * @author crodas
 */
public interface mapCONT_CLIE {

    @Insert("INSERT INTO TMCONT_CLIE (CO_CLIE,CO_TIPO_DOCU,DE_NUME_DOCU,NO_CONT_CLIE,AP_CONT_CLIE,DE_CORR,DE_TELE) VALUES (#{CO_CLIE},#{CO_TIPO_DOCU},#{DE_NUME_DOCU},#{NO_CONT_CLIE},#{AP_CONT_CLIE},#{DE_CORR},#{DE_TELE});")
    @SelectKey(statement="SELECT @@IDENTITY;", keyProperty="CO_CONT_CLIE", before=false, resultType=Integer.class)
    public void insert(CONT_CLIE cont);

    @Select("SELECT * FROM TMCONT_CLIE WHERE CO_CLIE=#{CO_CLIE} AND ST_ELIM=0;")
    public List<CONT_CLIE> selectByCLIE(int CO_CLIE);

    /**@Select("SELECT * FROM TMCONT_CLIE WHERE CO_CONT_CLIE=#{CO_CONT_CLIE} AND ST_ELIM=0;")*/
    @Select("SELECT * FROM TMCONT_CLIE WHERE CO_CONT_CLIE=#{CO_CONT_CLIE};")
    public CONT_CLIE getById(Integer CO_CONT_CLIE);

    @Update("UPDATE TMCONT_CLIE SET NO_CONT_CLIE=#{NO_CONT_CLIE},AP_CONT_CLIE=#{AP_CONT_CLIE},CO_TIPO_DOCU=#{CO_TIPO_DOCU},DE_NUME_DOCU=#{DE_NUME_DOCU},DE_CORR=#{DE_CORR},DE_TELE=#{DE_TELE}  WHERE CO_CONT_CLIE=#{CO_CONT_CLIE};")
    public void update(CONT_CLIE db);
    
}
