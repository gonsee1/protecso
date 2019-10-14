/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.mappers;

import com.americatel.facturacion.historial.Historial;
import com.americatel.facturacion.models.NEGO_SUCU;
import com.americatel.facturacion.models.NEGO_SUCU_SERV_SUPL;
import com.americatel.facturacion.models.NEGO_SUCU_SERV_UNIC;
import com.americatel.facturacion.models.SERV_UNIC;
import java.util.List;
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
public interface mapNEGO_SUCU_SERV_UNIC {

    @Select("SELECT * FROM TMNEGO_SUCU_SERV_UNIC WHERE CO_NEGO_SUCU_SERV_UNIC=#{id} AND ST_ELIM=0;")
    @Results(value={
            @Result(property = "CO_NEGO_SUCU_SERV_UNIC",column="CO_NEGO_SUCU_SERV_UNIC",id = true), 
            @Result(property = "CO_NEGO_SUCU",column="CO_NEGO_SUCU"), 
            @Result(property = "ST_SOAR_INST",column="ST_SOAR_INST"), 
            @Result(property = "CO_OIT_INST",column="CO_OIT_INST"),
            @Result(property = "CO_SERV_UNIC",column="CO_SERV_UNIC"), 
            @Result(property = "NO_SERV_UNIC",column="NO_SERV_UNIC"), 
            @Result(property = "IM_MONTO",column="IM_MONTO"),
            @Result(property = "ST_AFEC_DETR",column="ST_AFEC_DETR"),
            @Result(property = "ST_ELIM",column="ST_ELIM"),
            @Result(property = "CO_CIER_COBR",column="CO_CIER_COBR"),
            @Result(property = "serv_unic",column = "CO_SERV_UNIC",javaType = SERV_UNIC.class,one = @One(select = "com.americatel.facturacion.mappers.mapSERV_UNIC.getId"),id = false),
        }
    )
    public NEGO_SUCU_SERV_UNIC getId(int id);
    
    @Insert("INSERT INTO TMNEGO_SUCU_SERV_UNIC (CO_NEGO_SUCU,ST_SOAR_INST,CO_OIT_INST,CO_SERV_UNIC,NO_SERV_UNIC,IM_MONTO,ST_AFEC_DETR,"+Historial.stringMapperInsertarTitles+") VALUES "
            + "(#{CO_NEGO_SUCU},#{ST_SOAR_INST},#{CO_OIT_INST},#{CO_SERV_UNIC},#{NO_SERV_UNIC},#{IM_MONTO},#{ST_AFEC_DETR},"+Historial.stringMapperInsertarValuesNumeral+");")
    public void insert(NEGO_SUCU_SERV_UNIC nego_sucu_uni);
    
    @Update("UPDATE TMNEGO_SUCU_SERV_UNIC SET CO_NEGO_SUCU=#{CO_NEGO_SUCU}, "+Historial.stringMapperModificar+" WHERE CO_NEGO_SUCU_SERV_UNIC=#{CO_NEGO_SUCU_SERV_UNIC} AND ST_ELIM=0;")
    public void cambiarSucursal(NEGO_SUCU_SERV_UNIC aThis);

    @Select("SELECT * FROM TMNEGO_SUCU_SERV_UNIC WHERE CO_NEGO_SUCU=#{CO_NEGO_SUCU} AND ST_ELIM=0;")
    @Results(value={
        @Result(property = "serv_unic",column = "CO_SERV_UNIC",javaType = SERV_UNIC.class,one = @One(select = "com.americatel.facturacion.mappers.mapSERV_UNIC.getId"),id = false),
    })
    public List<NEGO_SUCU_SERV_UNIC> selectByNEGO_SUCU(Integer CO_NEGO_SUCU);
    
    @Select("SELECT * FROM TMNEGO_SUCU_SERV_UNIC WHERE CO_NEGO_SUCU=#{CO_NEGO_SUCU} AND ST_ELIM=0 AND (CO_CIER_COBR is null OR CO_CIER_COBR=#{CO_CIER})")
    public List<NEGO_SUCU_SERV_UNIC> getServiciosUnicosPendientes(@Param(value = "CO_NEGO_SUCU") Integer CO_NEGO_SUCU,@Param(value = "CO_CIER") Integer CO_CIER);

    @Select("SELECT * FROM TMNEGO_SUCU_SERV_UNIC WHERE CO_NEGO_SUCU=#{CO_NEGO_SUCU} AND ST_ELIM=0 AND (CO_CIER_COBR is null)")
    public List<NEGO_SUCU_SERV_UNIC> getServiciosUnicosPendientesSinCierre(@Param(value = "CO_NEGO_SUCU") Integer CO_NEGO_SUCU);    
    
    @Update("UPDATE TMNEGO_SUCU_SERV_UNIC SET CO_CIER_COBR=#{CO_CIER_COBR},"+Historial.stringMapperModificar+" WHERE CO_NEGO_SUCU_SERV_UNIC=#{CO_NEGO_SUCU_SERV_UNIC} AND ST_ELIM=0;")
    public void saveCierCobr(NEGO_SUCU_SERV_UNIC aThis);

    @Select("SELECT a.* FROM TMNEGO_SUCU_SERV_UNIC a INNER JOIN TMNEGO_SUCU b ON b.CO_NEGO_SUCU=a.CO_NEGO_SUCU AND b.CO_NEGO=#{CO_NEGO} WHERE a.ST_ELIM=0 AND (a.CO_CIER_COBR is null OR a.CO_CIER_COBR=#{CO_CIER})")
    public List<NEGO_SUCU_SERV_UNIC> getServiciosUnicosPendientesPorNegocio(@Param(value = "CO_NEGO") Integer CO_NEGO_SUCU,@Param(value = "CO_CIER") Integer CO_CIER);
    
    @Update("UPDATE TMNEGO_SUCU_SERV_UNIC SET ST_ELIM=1,"+Historial.stringMapperModificar+" WHERE CO_NEGO_SUCU_SERV_UNIC=#{CO_NEGO_SUCU_SERV_UNIC};")
    public int delete(NEGO_SUCU_SERV_UNIC aThis);
    
    @Select("SELECT CAST( " +
        "CASE WHEN COUNT(*) = (SELECT COUNT(*) FROM TMNEGO_SUCU_SERV_UNIC WHERE CO_NEGO_SUCU=#{CO_NEGO_SUCU} AND ST_ELIM=0) THEN 1 " +
        "ELSE 0 " +
        "END AS bit) AS IND " +
        "FROM TMNEGO_SUCU_SERV_UNIC WHERE CO_NEGO_SUCU=#{CO_NEGO_SUCU} AND (CO_CIER_COBR IS NULL OR CO_CIER_COBR = #{CO_CIER_COBR}) AND ST_ELIM=0")
    public boolean isTodosServUnicSinFacturar(@Param("CO_NEGO_SUCU") Integer CO_NEGO_SUCU,@Param("CO_CIER_COBR") Integer CO_CIER_COBR);
}
