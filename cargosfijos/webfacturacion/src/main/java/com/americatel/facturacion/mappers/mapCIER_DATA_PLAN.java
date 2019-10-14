/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.mappers;

import com.americatel.facturacion.models.CIER;
import com.americatel.facturacion.models.CIER_DATA_PLAN;
import com.americatel.facturacion.models.NEGO_SUCU_PLAN;
import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 *
 * @author crodas
 */
public interface mapCIER_DATA_PLAN {

    @Insert("INSERT INTO TMCIER_DATA_PLAN (CO_NEGO_SUCU_PLAN,FE_INIC,FE_FIN,CO_MONE_FACT,IM_MONT,ST_TIPO_DEVO,DE_NOMB,CO_CIER_DATA_SUCU,CO_RECI,CO_FACT,ST_TIPO_COBR,CO_BOLE) VALUES (#{CO_NEGO_SUCU_PLAN},#{FE_INIC},#{FE_FIN},#{CO_MONE_FACT},#{IM_MONT},#{ST_TIPO_DEVO},#{DE_NOMB},#{CO_CIER_DATA_SUCU},#{CO_RECI},#{CO_FACT},#{ST_TIPO_COBR},#{CO_BOLE})")
    public void insertar(CIER_DATA_PLAN aThis);
    
    @Insert("INSERT INTO TMCIER_DATA_PLAN (CO_NEGO_SUCU_PLAN,FE_INIC,FE_FIN,CO_MONE_FACT,IM_MONT,ST_TIPO_DEVO,DE_NOMB,CO_CIER_DATA_SUCU,CO_RECI,CO_FACT,ST_TIPO_COBR,CO_BOLE) "
            + " SELECT #{CO_NEGO_SUCU_PLAN_NUEVO},FE_INIC,FE_FIN,CO_MONE_FACT,IM_MONT,ST_TIPO_DEVO,DE_NOMB,CO_CIER_DATA_SUCU,CO_RECI,CO_FACT,ST_TIPO_COBR,CO_BOLE FROM TMCIER_DATA_PLAN WHERE CO_NEGO_SUCU_PLAN=#{CO_NEGO_SUCU_PLAN}")
    public void insertarMasivo(@Param("CO_NEGO_SUCU_PLAN_NUEVO") Integer CO_NEGO_SUCU_PLAN_NUEVO,@Param("CO_NEGO_SUCU_PLAN") Integer CO_NEGO_SUCU_PLAN);

    //p.ST_TIPO_COBR=1
    @Select("SELECT p.* FROM TMCIER_DATA_PLAN p "
            + "INNER JOIN TMCIER_DATA_SUCU s ON s.CO_CIER_DATA_SUCU=p.CO_CIER_DATA_SUCU "
            + "INNER JOIN TMCIER_DATA_NEGO n ON n.CO_CIER_DATA_NEGO=s.CO_CIER_DATA_NEGO AND (n.CO_CIER<>#{CIER.CO_CIER} OR n.CO_CIER is null) "
            + "WHERE p.ST_TIPO_DEVO IS NULL AND s.CO_NEGO_SUCU=#{NEGO_SUCU_PLAN.CO_NEGO_SUCU} ")    
    public List<CIER_DATA_PLAN> getCIER_DATA_PLANES_COBRADOS(@Param("NEGO_SUCU_PLAN") NEGO_SUCU_PLAN aThis,@Param("CIER") CIER cier);

    //p.ST_TIPO_COBR=1
    @Select("SELECT p.* FROM TMCIER_DATA_PLAN p "
        + "INNER JOIN TMCIER_DATA_SUCU s ON s.CO_CIER_DATA_SUCU=p.CO_CIER_DATA_SUCU "
        + "INNER JOIN TMCIER_DATA_NEGO n ON n.CO_CIER_DATA_NEGO=s.CO_CIER_DATA_NEGO AND (n.CO_CIER<>#{CIER.CO_CIER} OR n.CO_CIER is null) "
        + "WHERE p.ST_TIPO_DEVO IS NULL AND s.CO_NEGO_SUCU=#{NEGO_SUCU_PLAN.CO_NEGO_SUCU} AND  p.CO_NEGO_SUCU_PLAN=#{NEGO_SUCU_PLAN.CO_NEGO_SUCU_PLAN}")   
    public List<CIER_DATA_PLAN> getCIER_DATA_MI_PLAN_COBRADOS(@Param("NEGO_SUCU_PLAN") NEGO_SUCU_PLAN aThis,@Param("CIER") CIER cier);

    @Select("SELECT p.* FROM TMCIER_DATA_PLAN p "
            + "INNER JOIN TMCIER_DATA_SUCU s ON s.CO_CIER_DATA_SUCU=p.CO_CIER_DATA_SUCU "
            + "INNER JOIN TMCIER_DATA_NEGO n ON n.CO_CIER_DATA_NEGO=s.CO_CIER_DATA_NEGO AND (n.CO_CIER<>#{CIER.CO_CIER} OR n.CO_CIER is null) "
            + "WHERE p.ST_TIPO_DEVO=4 AND s.CO_NEGO_SUCU=#{NEGO_SUCU_PLAN.CO_NEGO_SUCU} ")    
    public List<CIER_DATA_PLAN> getCIER_DATA_DEVOLUCIONES_POR_PROMOCIONES(@Param("NEGO_SUCU_PLAN") NEGO_SUCU_PLAN aThis,@Param("CIER") CIER cier);
    
    
}
