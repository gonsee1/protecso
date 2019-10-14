/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.mappers;

import com.americatel.facturacion.historial.Historial;
import com.americatel.facturacion.models.NEGO_SUCU;
import com.americatel.facturacion.models.NEGO_SUCU_PLAN;
import com.americatel.facturacion.models.PLAN;
import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;

/**
 *
 * @author crodas
 */
public interface mapNEGO_SUCU_PLAN {  
    
    @Select("SELECT * FROM TMNEGO_SUCU_PLAN WHERE FE_INIC BETWEEN #{NEGO_SUCU_PLAN.FE_FIN} AND #{UFF} AND CO_NEGO_SUCU=#{NEGO_SUCU_PLAN.CO_NEGO_SUCU} AND ST_ELIM=0 AND CO_NEGO_SUCU_PLAN<>#{NEGO_SUCU_PLAN.CO_NEGO_SUCU_PLAN} ORDER BY FE_INIC ASC")
    public List<NEGO_SUCU_PLAN> getPlanesEntrePeriodoDesactivado(@Param("NEGO_SUCU_PLAN") NEGO_SUCU_PLAN NEGO_SUCU_PLAN,@Param("UFF")  Date UFF);

    @Insert("INSERT INTO TMNEGO_SUCU_PLAN "+
            "(CO_NEGO_SUCU,CO_PLAN,ST_SOAR_INST,CO_OIT_INST,ST_SOAR_DESI,CO_OIT_DESI,NO_PLAN,DE_VELO_SUBI,DE_VELO_BAJA,FE_INIC,FE_FIN,FE_ULTI_FACT,IM_MONTO,CO_NEGO_SUCU_PLAN_CESI_CONT_PADR,CO_NEGO_SUCU_PLAN_CESI_CONT_HIJO,CO_NEGO_SUCU_PLAN_MUDA_PADR,CO_NEGO_SUCU_PLAN_MUDA_HIJO,"+Historial.stringMapperInsertarTitles+") VALUES "+
            "(#{CO_NEGO_SUCU},#{CO_PLAN},#{ST_SOAR_INST},#{CO_OIT_INST},#{ST_SOAR_DESI},#{CO_OIT_DESI},#{NO_PLAN},#{DE_VELO_SUBI},#{DE_VELO_BAJA},#{FE_INIC},#{FE_FIN},#{FE_ULTI_FACT},#{IM_MONTO},#{CO_NEGO_SUCU_PLAN_CESI_CONT_PADR},#{CO_NEGO_SUCU_PLAN_CESI_CONT_HIJO},#{CO_NEGO_SUCU_PLAN_MUDA_PADR},#{CO_NEGO_SUCU_PLAN_MUDA_HIJO},"+Historial.stringMapperInsertarValuesNumeral+");")
    @SelectKey(statement="SELECT @@IDENTITY;", keyProperty="CO_NEGO_SUCU_PLAN", before=false, resultType=Integer.class)
    public void insert(NEGO_SUCU_PLAN nego_sucu_plan);
    
    @Select("SELECT * FROM TMNEGO_SUCU_PLAN WHERE CO_NEGO_SUCU_PLAN=#{id} AND ST_ELIM=0;")
    @Results(value={
            @Result(property = "CO_NEGO_SUCU_PLAN",column="CO_NEGO_SUCU_PLAN",id = true), 
            @Result(property = "CO_NEGO_SUCU",column="CO_NEGO_SUCU"), 
            @Result(property = "ST_SOAR_INST",column="ST_SOAR_INST"), 
            @Result(property = "CO_OIT_INST",column="CO_OIT_INST"), 
            @Result(property = "ST_SOAR_DESI",column="ST_SOAR_DESI"), 
            @Result(property = "CO_OIT_DESI",column="CO_OIT_DESI"), 
            @Result(property = "CO_PLAN",column="CO_PLAN"), 
            @Result(property = "NO_PLAN",column="NO_PLAN"), 
            @Result(property = "DE_VELO_SUBI",column="DE_VELO_SUBI"), 
            @Result(property = "DE_VELO_BAJA",column="DE_VELO_BAJA"), 
            @Result(property = "FE_INIC",column="FE_INIC"),
            @Result(property = "FE_FIN",column="FE_FIN"), 
            @Result(property = "FE_ULTI_FACT",column="FE_ULTI_FACT"), 
            @Result(property = "FE_ULTI_FACT_SGTE",column="FE_ULTI_FACT_SGTE"), 
            @Result(property = "IM_MONTO",column="IM_MONTO"), 
            @Result(property = "ST_ELIM",column="ST_ELIM"), 
            @Result(property = "plan",column = "CO_PLAN",javaType = PLAN.class,one = @One(select = "com.americatel.facturacion.mappers.mapPLAN.getId"),id = false),
        }
    )
    public NEGO_SUCU_PLAN getId(int id);
    
    @Select("SELECT * FROM TMNEGO_SUCU_PLAN WHERE CO_NEGO_SUCU=#{CO_NEGO_SUCU} AND ST_ELIM=0;")
    @Results(value={
            @Result(property = "CO_NEGO_SUCU_PLAN",column="CO_NEGO_SUCU_PLAN",id = true), 
            @Result(property = "CO_NEGO_SUCU",column="CO_NEGO_SUCU"), 
            @Result(property = "ST_SOAR_INST",column="ST_SOAR_INST"), 
            @Result(property = "CO_OIT_INST",column="CO_OIT_INST"), 
            @Result(property = "ST_SOAR_DESI",column="ST_SOAR_DESI"), 
            @Result(property = "CO_OIT_DESI",column="CO_OIT_DESI"), 
            @Result(property = "CO_PLAN",column="CO_PLAN"), 
            @Result(property = "NO_PLAN",column="NO_PLAN"), 
            @Result(property = "DE_VELO_SUBI",column="DE_VELO_SUBI"), 
            @Result(property = "DE_VELO_BAJA",column="DE_VELO_BAJA"), 
            @Result(property = "FE_INIC",column="FE_INIC"),
            @Result(property = "FE_FIN",column="FE_FIN"), 
            @Result(property = "FE_ULTI_FACT",column="FE_ULTI_FACT"), 
            @Result(property = "FE_ULTI_FACT_SGTE",column="FE_ULTI_FACT_SGTE"), 
            @Result(property = "IM_SALD_APROX",column="IM_SALD_APROX"), 
            @Result(property = "CO_NEGO_SUCU_PLAN_CESI_CONT_PADR",column="CO_NEGO_SUCU_PLAN_CESI_CONT_PADR"), 
            @Result(property = "CO_NEGO_SUCU_PLAN_CESI_CONT_HIJO",column="CO_NEGO_SUCU_PLAN_CESI_CONT_HIJO"), 
            @Result(property = "CO_NEGO_SUCU_PLAN_MUDA_PADR",column="CO_NEGO_SUCU_PLAN_MUDA_PADR"), 
            @Result(property = "CO_NEGO_SUCU_PLAN_MUDA_HIJO",column="CO_NEGO_SUCU_PLAN_MUDA_HIJO"), 
            @Result(property = "IM_MONTO",column="IM_MONTO"), 
            @Result(property = "ST_ELIM",column="ST_ELIM"), 
            @Result(property = "plan",column = "CO_PLAN",javaType = PLAN.class,one = @One(select = "com.americatel.facturacion.mappers.mapPLAN.getId"),id = false),
        }
    )
    public List<NEGO_SUCU_PLAN> selectByNEGO_SUCU(Integer CO_NEGO_SUCU);
    
    @Select("SELECT * FROM TMNEGO_SUCU_PLAN WHERE FE_FIN is NULL AND CO_NEGO_SUCU=#{CO_NEGO_SUCU} AND ST_ELIM=0")
    @Results(value={
            @Result(property = "CO_NEGO_SUCU_PLAN",column="CO_NEGO_SUCU_PLAN",id = true), 
            @Result(property = "CO_NEGO_SUCU",column="CO_NEGO_SUCU"), 
            @Result(property = "ST_SOAR_INST",column="ST_SOAR_INST"), 
            @Result(property = "CO_OIT_INST",column="CO_OIT_INST"), 
            @Result(property = "ST_SOAR_DESI",column="ST_SOAR_DESI"), 
            @Result(property = "CO_OIT_DESI",column="CO_OIT_DESI"), 
            @Result(property = "CO_PLAN",column="CO_PLAN"), 
            @Result(property = "NO_PLAN",column="NO_PLAN"), 
            @Result(property = "DE_VELO_SUBI",column="DE_VELO_SUBI"), 
            @Result(property = "DE_VELO_BAJA",column="DE_VELO_BAJA"), 
            @Result(property = "FE_INIC",column="FE_INIC"),
            @Result(property = "FE_FIN",column="FE_FIN"), 
            @Result(property = "FE_ULTI_FACT",column="FE_ULTI_FACT"), 
            @Result(property = "FE_ULTI_FACT_SGTE",column="FE_ULTI_FACT_SGTE"), 
            @Result(property = "IM_MONTO",column="IM_MONTO"), 
            @Result(property = "ST_ELIM",column="ST_ELIM"), 
            @Result(property = "plan",column = "CO_PLAN",javaType = PLAN.class,one = @One(select = "com.americatel.facturacion.mappers.mapPLAN.getId"),id = false),
        }
    )
    public List<NEGO_SUCU_PLAN> selectActivoByNEGO_SUCU(Integer CO_NEGO_SUCU);

    @Select("SELECT * FROM TMNEGO_SUCU_PLAN WHERE FE_FIN is NULL AND CO_NEGO=#{CO_NEGO} AND ST_ELIM=0")
    @Results(value={
            @Result(property = "CO_NEGO_SUCU_PLAN",column="CO_NEGO_SUCU_PLAN",id = true), 
            @Result(property = "CO_NEGO_SUCU",column="CO_NEGO_SUCU"), 
            @Result(property = "ST_SOAR_INST",column="ST_SOAR_INST"), 
            @Result(property = "CO_OIT_INST",column="CO_OIT_INST"), 
            @Result(property = "ST_SOAR_DESI",column="ST_SOAR_DESI"), 
            @Result(property = "CO_OIT_DESI",column="CO_OIT_DESI"), 
            @Result(property = "CO_PLAN",column="CO_PLAN"), 
            @Result(property = "NO_PLAN",column="NO_PLAN"), 
            @Result(property = "DE_VELO_SUBI",column="DE_VELO_SUBI"), 
            @Result(property = "DE_VELO_BAJA",column="DE_VELO_BAJA"), 
            @Result(property = "FE_INIC",column="FE_INIC"),
            @Result(property = "FE_FIN",column="FE_FIN"), 
            @Result(property = "FE_ULTI_FACT",column="FE_ULTI_FACT"), 
            @Result(property = "FE_ULTI_FACT_SGTE",column="FE_ULTI_FACT_SGTE"), 
            @Result(property = "IM_MONTO",column="IM_MONTO"), 
            @Result(property = "ST_ELIM",column="ST_ELIM"), 
            @Result(property = "plan",column = "CO_PLAN",javaType = PLAN.class,one = @One(select = "com.americatel.facturacion.mappers.mapPLAN.getId"),id = false),
        }
    )
    public List<NEGO_SUCU_PLAN> selectActivoByNEGO(Integer CO_NEGO);
    
    @Select("SELECT * FROM TMNEGO_SUCU_PLAN WHERE CO_NEGO_SUCU=#{CO_NEGO_SUCU} AND CO_PLAN=#{CO_PLAN} AND ST_ELIM=0;")
    @Results(value={
            @Result(property = "CO_NEGO_SUCU_PLAN",column="CO_NEGO_SUCU_PLAN",id = true), 
            @Result(property = "CO_NEGO_SUCU",column="CO_NEGO_SUCU"), 
            @Result(property = "ST_SOAR_INST",column="ST_SOAR_INST"), 
            @Result(property = "CO_OIT_INST",column="CO_OIT_INST"), 
            @Result(property = "ST_SOAR_DESI",column="ST_SOAR_DESI"), 
            @Result(property = "CO_OIT_DESI",column="CO_OIT_DESI"), 
            @Result(property = "CO_PLAN",column="CO_PLAN"), 
            @Result(property = "NO_PLAN",column="NO_PLAN"), 
            @Result(property = "DE_VELO_SUBI",column="DE_VELO_SUBI"), 
            @Result(property = "DE_VELO_BAJA",column="DE_VELO_BAJA"), 
            @Result(property = "FE_INIC",column="FE_INIC"),
            @Result(property = "FE_FIN",column="FE_FIN"), 
            @Result(property = "FE_ULTI_FACT",column="FE_ULTI_FACT"), 
            @Result(property = "FE_ULTI_FACT_SGTE",column="FE_ULTI_FACT_SGTE"), 
            @Result(property = "IM_MONTO",column="IM_MONTO"), 
            @Result(property = "ST_ELIM",column="ST_ELIM"), 
            @Result(property = "plan",column = "CO_PLAN",javaType = PLAN.class,one = @One(select = "com.americatel.facturacion.mappers.mapPLAN.getId"),id = false),
        }
    )
    public NEGO_SUCU_PLAN selectByNEGO_SUCUandPLAN(@Param("CO_NEGO_SUCU") Integer CO_NEGO_SUCU,@Param("CO_PLAN") Integer CO_PLAN);
    
    @Update("UPDATE TMNEGO_SUCU_PLAN SET "+
            "FE_FIN=#{FE_FIN},ST_SOAR_DESI=#{ST_SOAR_DESI},CO_OIT_DESI=#{CO_OIT_DESI},"+Historial.stringMapperModificar+" "+
            "WHERE CO_NEGO_SUCU_PLAN=#{CO_NEGO_SUCU_PLAN} AND FE_FIN IS NULL AND ST_ELIM=0;")
    public void desactivar(NEGO_SUCU_PLAN nego_sucu_plan);

    
    @Insert("INSERT INTO TMNEGO_SUCU_PLAN (CO_NEGO_SUCU,ST_SOAR_INST,CO_OIT_INST,CO_PLAN,IM_MONTO,NO_PLAN,DE_VELO_SUBI,DE_VELO_BAJA,FE_INIC,"+Historial.stringMapperInsertarTitles+") VALUES "+
            "(#{CO_NEGO_SUCU},#{ST_SOAR_INST},#{CO_OIT_INST},#{CO_PLAN},(SELECT IM_MONTO FROM TMPLAN  WHERE CO_PLAN=#{CO_PLAN}),"+
            "(SELECT NO_PLAN FROM TMPLAN  WHERE CO_PLAN=#{CO_PLAN}),(SELECT DE_VELO_SUBI FROM TMPLAN  WHERE CO_PLAN=#{CO_PLAN}),"+
            "(SELECT DE_VELO_BAJA FROM TMPLAN  WHERE CO_PLAN=#{CO_PLAN}),#{FE_INIC},"+Historial.stringMapperInsertarValuesNumeral+");")
    @SelectKey(statement="SELECT @@IDENTITY;", keyProperty="CO_NEGO_SUCU_PLAN", before=false, resultType=Integer.class)
    public void insertar(NEGO_SUCU_PLAN nego_sucu_plan);

    
    @Select("SELECT * FROM TMNEGO_SUCU_PLAN WHERE ST_SOAR_INST=#{ST_SOAR_INST} "
            + "AND CO_NEGO_SUCU NOT IN (SELECT CO_NEGO_SUCU FROM TMNEGO_SUCU WHERE FE_FIN IS NOT NULL AND ST_ELIM=0) "
            + "AND CO_OIT_INST=#{CO_OIT_INST} AND ST_ELIM=0 AND FE_FIN IS NULL;")
    public List<NEGO_SUCU_PLAN> getByOitInstalacion(NEGO_SUCU_PLAN nego_sucu_plan);

    @Select("SELECT * FROM TMNEGO_SUCU_PLAN WHERE ST_SOAR_DESI=#{ST_SOAR_DESI} and CO_OIT_DESI=#{CO_OIT_DESI} "
            + "AND CO_NEGO_SUCU NOT IN (SELECT CO_NEGO_SUCU FROM TMNEGO_SUCU WHERE FE_FIN IS NOT NULL AND ST_ELIM=0) "
            + "AND ST_ELIM=0;")
    public List<NEGO_SUCU_PLAN> getByOitDesinstalacion(NEGO_SUCU_PLAN aThis);

    @Select("SELECT * FROM TMNEGO_SUCU_PLAN WHERE FE_FIN is NULL AND CO_NEGO_SUCU=#{CO_NEGO_SUCU} AND ST_ELIM=0;")
    public NEGO_SUCU_PLAN getPlanActivo(NEGO_SUCU aThis);
    
    @Select("SELECT TOP 1 * FROM TMNEGO_SUCU_PLAN WHERE FE_FIN is NULL AND CO_NEGO_SUCU=#{CO_NEGO_SUCU} AND ST_ELIM=0 ORDER BY FE_FIN;")
    public NEGO_SUCU_PLAN getUltimoPlanActivo(NEGO_SUCU aThis);

    @Update(
    		//"UPDATE TMNEGO_SUCU_PLAN SET FE_ULTI_FACT=FE_ULTI_FACT_SGTE,"+Historial.stringMapperModificar+" WHERE CO_NEGO_SUCU_PLAN=#{CO_NEGO_SUCU_PLAN} AND ST_ELIM=0;"+
    		"UPDATE TMNEGO_SUCU_PLAN SET FE_ULTI_FACT_SGTE=#{FE_ULTI_FACT_SGTE},"+Historial.stringMapperModificar+" WHERE CO_NEGO_SUCU_PLAN=#{CO_NEGO_SUCU_PLAN} AND ST_ELIM=0;"
    )
    public void saveSiguienteUltimaFacturacion(NEGO_SUCU_PLAN aThis);

    
    //Busca el anterior plan desde el nuevo si es upgrade
    @Select("SELECT TOP 1 * FROM TMNEGO_SUCU_PLAN WHERE CO_NEGO_SUCU_PLAN<#{CO_NEGO_SUCU_PLAN} AND CO_NEGO_SUCU=#{CO_NEGO_SUCU} AND IM_MONTO<#{IM_MONTO} AND ST_ELIM=0 ORDER BY CO_NEGO_SUCU_PLAN DESC;")
    public NEGO_SUCU_PLAN getUpgradeAntiguo(NEGO_SUCU_PLAN nuevo);

    //Busca desde el viejo el nuevo plan de upgrade
    @Select("SELECT TOP 1 * FROM TMNEGO_SUCU_PLAN WHERE CO_NEGO_SUCU_PLAN>#{CO_NEGO_SUCU_PLAN} AND CO_NEGO_SUCU=#{CO_NEGO_SUCU} AND IM_MONTO>#{IM_MONTO} AND ST_ELIM=0 ORDER BY CO_NEGO_SUCU_PLAN DESC;")
    public NEGO_SUCU_PLAN getUpgradeNuevo(NEGO_SUCU_PLAN viejo);

    //Busca el anterior plan desde el nuevo si es downgrade
    @Select("SELECT TOP 1 * FROM TMNEGO_SUCU_PLAN WHERE CO_NEGO_SUCU_PLAN<#{CO_NEGO_SUCU_PLAN} AND CO_NEGO_SUCU=#{CO_NEGO_SUCU} AND IM_MONTO>#{IM_MONTO} AND ST_ELIM=0 ORDER BY CO_NEGO_SUCU_PLAN DESC;")
    public NEGO_SUCU_PLAN getDowngradeAntiguo(NEGO_SUCU_PLAN nuevo);

    //Busca desde el viejo el nuevo plan de downgrade
    @Select("SELECT TOP 1 * FROM TMNEGO_SUCU_PLAN WHERE CO_NEGO_SUCU_PLAN>#{CO_NEGO_SUCU_PLAN} AND CO_NEGO_SUCU=#{CO_NEGO_SUCU} AND IM_MONTO<#{IM_MONTO} AND ST_ELIM=0 ORDER BY CO_NEGO_SUCU_PLAN DESC;")
    public NEGO_SUCU_PLAN getDowngradeNuevo(NEGO_SUCU_PLAN viejo);

    @Update("UPDATE TMNEGO_SUCU_PLAN SET IM_SALD_APROX=#{IM_SALD_APROX},"+Historial.stringMapperModificar+" WHERE CO_NEGO_SUCU_PLAN=#{CO_NEGO_SUCU_PLAN} AND ST_ELIM=0;")
    public void updateSaldoAproximado(NEGO_SUCU_PLAN aThis);
 
    @Update("UPDATE TMNEGO_SUCU_PLAN SET CO_NEGO_SUCU_PLAN_CESI_CONT_PADR=#{CO_NEGO_SUCU_PLAN_CESI_CONT_PADR} , CO_NEGO_SUCU_PLAN_CESI_CONT_HIJO=#{CO_NEGO_SUCU_PLAN_CESI_CONT_HIJO},"+Historial.stringMapperModificar+" WHERE CO_NEGO_SUCU_PLAN=#{CO_NEGO_SUCU_PLAN} AND ST_ELIM=0;")
    public void updateCesionContractual(NEGO_SUCU_PLAN aThis);
    
    @Update("UPDATE TMNEGO_SUCU_PLAN SET CO_NEGO_SUCU_PLAN_MUDA_PADR=#{CO_NEGO_SUCU_PLAN_MUDA_PADR} , CO_NEGO_SUCU_PLAN_MUDA_HIJO=#{CO_NEGO_SUCU_PLAN_MUDA_HIJO},"+Historial.stringMapperModificar+" WHERE CO_NEGO_SUCU_PLAN=#{CO_NEGO_SUCU_PLAN} AND ST_ELIM=0;")
    public void updateMudanza(NEGO_SUCU_PLAN aThis);
    
    @Update("UPDATE TMNEGO_SUCU_PLAN SET CO_PLAN=#{CO_PLAN}, DE_VELO_BAJA=#{DE_VELO_BAJA}, DE_VELO_SUBI=#{DE_VELO_SUBI}, NO_PLAN=#{NO_PLAN},"+Historial.stringMapperModificar+" WHERE CO_NEGO_SUCU_PLAN=#{CO_NEGO_SUCU_PLAN} AND ST_ELIM=0;")
    public void updateVelocidadMedio(NEGO_SUCU_PLAN aThis);
    
    @Update("UPDATE TMNEGO_SUCU_PLAN SET ST_ELIM=1,"+Historial.stringMapperModificar+" WHERE CO_NEGO_SUCU_PLAN=#{CO_NEGO_SUCU_PLAN};")
    public int delete(NEGO_SUCU_PLAN aThis);
    
    @Select("SELECT CAST( " +
        "CASE WHEN COUNT(*) = (SELECT COUNT(*) FROM TMNEGO_SUCU_PLAN WHERE CO_NEGO_SUCU=#{CO_NEGO_SUCU} AND ST_ELIM=0) THEN 1 " +
        "ELSE 0 " +
        "END AS bit) AS IND " +
        "FROM TMNEGO_SUCU_PLAN WHERE CO_NEGO_SUCU=#{CO_NEGO_SUCU} AND FE_ULTI_FACT IS NULL AND ST_ELIM=0")
    public boolean isTodosPlanesSinFacturar(Integer CO_NEGO_SUCU);
    
    //Busca el ultimo plan desactivado para activarlo luego de eliminar un plan
    @Select("SELECT TOP 1 * FROM TMNEGO_SUCU_PLAN WHERE CO_NEGO_SUCU=#{CO_NEGO_SUCU} AND FE_FIN IS NOT NULL AND ST_ELIM=0 ORDER BY CO_NEGO_SUCU_PLAN DESC;")
    public NEGO_SUCU_PLAN getUltimoPlanDesactivado(Integer CO_NEGO_SUCU);
    
    @Update("UPDATE TMNEGO_SUCU_PLAN SET FE_FIN=NULL, ST_SOAR_DESI=NULL, CO_OIT_DESI=NULL,"+Historial.stringMapperModificar+" WHERE CO_NEGO_SUCU_PLAN=#{CO_NEGO_SUCU_PLAN};")
    public void updateActivarPlan(NEGO_SUCU_PLAN aThis);
    
    @Select("select TOP 1 * from TMNEGO_SUCU_PLAN where CO_OIT_INST = #{CO_OIT_INST};")
    public NEGO_SUCU_PLAN obtenerForOitInstalacion(Integer CO_OIT_INST);
}
