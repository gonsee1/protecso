/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.mappers;

import com.americatel.facturacion.models.CIER;
import com.americatel.facturacion.models.CIER_DATA_SERV_SUPL;
import com.americatel.facturacion.models.NEGO_SUCU_SERV_SUPL;
import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 *
 * @author crodas
 */
public interface mapCIER_DATA_SERV_SUPL {

    @Insert("INSERT INTO TMCIER_DATA_SERV_SUPL (CO_NEGO_SUCU_SERV_SUPL,FE_INIC,FE_FIN,CO_MONE_FACT,IM_MONT,ST_TIPO_DEVO,DE_NOMB,CO_CIER_DATA_SUCU,CO_RECI,CO_FACT,ST_TIPO_COBR,ST_AFEC_DETR, CO_BOLE) VALUES (#{CO_NEGO_SUCU_SERV_SUPL},#{FE_INIC},#{FE_FIN},#{CO_MONE_FACT},#{IM_MONT},#{ST_TIPO_DEVO},#{DE_NOMB},#{CO_CIER_DATA_SUCU},#{CO_RECI},#{CO_FACT},#{ST_TIPO_COBR},#{ST_AFEC_DETR}, #{CO_BOLE})")
    public void insertar(CIER_DATA_SERV_SUPL aThis);
    
    @Insert("INSERT INTO TMCIER_DATA_SERV_SUPL (CO_NEGO_SUCU_SERV_SUPL,FE_INIC,FE_FIN,CO_MONE_FACT,IM_MONT,ST_TIPO_DEVO,DE_NOMB,CO_CIER_DATA_SUCU,CO_RECI,CO_FACT,ST_TIPO_COBR,ST_AFEC_DETR) "
            + " SELECT #{CO_NEGO_SUCU_SERV_SUPL_NUEVO},FE_INIC,FE_FIN,CO_MONE_FACT,IM_MONT,ST_TIPO_DEVO,DE_NOMB,CO_CIER_DATA_SUCU,CO_RECI,CO_FACT,ST_TIPO_COBR,ST_AFEC_DETR FROM TMCIER_DATA_SERV_SUPL WHERE CO_NEGO_SUCU_SERV_SUPL=#{CO_NEGO_SUCU_SERV_SUPL}")
    public void insertarMasivo(@Param("CO_NEGO_SUCU_SERV_SUPL_NUEVO") Integer CO_NEGO_SUCU_SERV_SUPL_NUEVO,@Param("CO_NEGO_SUCU_SERV_SUPL") Integer CO_NEGO_SUCU_SERV_SUPL);
    
    //ss.ST_TIPO_COBR=1
    @Select("SELECT ss.* FROM TMCIER_DATA_SERV_SUPL ss "
            + "INNER JOIN TMCIER_DATA_SUCU s ON s.CO_CIER_DATA_SUCU=ss.CO_CIER_DATA_SUCU "
            + "INNER JOIN TMCIER_DATA_NEGO n ON n.CO_CIER_DATA_NEGO=s.CO_CIER_DATA_NEGO AND (n.CO_CIER<>#{CIER.CO_CIER} OR n.CO_CIER is null) "
            + "WHERE ss.ST_TIPO_DEVO IS NULL AND s.CO_NEGO_SUCU=#{NEGO_SUCU_SERV_SUPL.CO_NEGO_SUCU} ")    
    public List<CIER_DATA_SERV_SUPL> getCIER_DATA_SERVICIOS_SUPL_COBRADOS(@Param("NEGO_SUCU_SERV_SUPL") NEGO_SUCU_SERV_SUPL aThis,@Param("CIER") CIER cier);
    
    //ss.ST_TIPO_COBR=1
    @Select("SELECT ss.* FROM TMCIER_DATA_SERV_SUPL ss "
            + "INNER JOIN TMCIER_DATA_SUCU s ON s.CO_CIER_DATA_SUCU=ss.CO_CIER_DATA_SUCU "
            + "INNER JOIN TMCIER_DATA_NEGO n ON n.CO_CIER_DATA_NEGO=s.CO_CIER_DATA_NEGO AND (n.CO_CIER<>#{CIER.CO_CIER} OR n.CO_CIER is null) "
            + "WHERE ss.ST_TIPO_DEVO IS NULL AND s.CO_NEGO_SUCU=#{NEGO_SUCU_SERV_SUPL.CO_NEGO_SUCU} AND  ss.CO_NEGO_SUCU_SERV_SUPL=#{NEGO_SUCU_SERV_SUPL.CO_NEGO_SUCU_SERV_SUPL}")      
    public List<CIER_DATA_SERV_SUPL> getCIER_DATA_MI_SERVICIOS_SUPL_COBRADOS(@Param("NEGO_SUCU_SERV_SUPL") NEGO_SUCU_SERV_SUPL aThis,@Param("CIER") CIER cier);
    
    @Select("SELECT ss.* FROM TMCIER_DATA_SERV_SUPL ss "
        + "INNER JOIN TMCIER_DATA_SUCU s ON s.CO_CIER_DATA_SUCU=ss.CO_CIER_DATA_SUCU "
        + "INNER JOIN TMCIER_DATA_NEGO n ON n.CO_CIER_DATA_NEGO=s.CO_CIER_DATA_NEGO AND (n.CO_CIER<>#{CIER.CO_CIER} OR n.CO_CIER is null) "
        + "WHERE ss.ST_TIPO_DEVO=4 AND s.CO_NEGO_SUCU=#{NEGO_SUCU_SERV_SUPL.CO_NEGO_SUCU} ")       
    public List<CIER_DATA_SERV_SUPL> getCIER_DATA_DEVOLUCIONES_POR_PROMOCIONES(@Param("NEGO_SUCU_SERV_SUPL") NEGO_SUCU_SERV_SUPL aThis,@Param("CIER") CIER cier);
}
