/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.mappers;

import com.americatel.facturacion.historial.Historial;
import com.americatel.facturacion.models.MONE_FACT;
import com.americatel.facturacion.models.PERI_FACT;
import com.americatel.facturacion.models.PLAN;
import com.americatel.facturacion.models.PLAN_MEDI_INST;
import com.americatel.facturacion.models.PROD;
import com.americatel.facturacion.models.PROD_PADRE;
import com.americatel.facturacion.models.REGLAS_NOMBRE_PLAN;
import com.americatel.facturacion.models.TIPO_FACT;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
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
public interface mapPLAN {

    @Select("SELECT * FROM TMPLAN WHERE CO_PLAN=#{id} AND ST_ELIM=0")
    @Results(value={
            @Result(property = "CO_PLAN",column="CO_PLAN",id = true), 
            @Result(property = "CO_PROD",column="CO_PROD"), 
            @Result(property = "NO_PLAN",column="NO_PLAN"),     
            @Result(property = "IM_MONTO",column="IM_MONTO"), 
            @Result(property = "DE_VELO_SUBI",column="DE_VELO_SUBI"),
            @Result(property = "DE_VELO_BAJA",column="DE_VELO_BAJA"),
            @Result(property = "CO_PLAN_MEDI_INST",column="CO_PLAN_MEDI_INST"),
            @Result(property = "CO_MONE_FACT",column="CO_MONE_FACT"),
            @Result(property = "ST_ELIM",column="ST_ELIM"),
            @Result(property = "ST_BACKUP_BU",column="ST_BACKUP_BU"),
            @Result(property = "RELACIONADO_TI",column="RELACIONADO_TI"),
            @Result(property = "PRODUCTO_CARGA",column="PRODUCTO_CARGA"),
            @Result(property = "prod",column = "CO_PROD",javaType = PROD.class,one = @One(select = "com.americatel.facturacion.mappers.mapPROD.getId"),id = false),
            @Result(property = "plan_medio_inst",column = "CO_PLAN_MEDI_INST",javaType = PLAN_MEDI_INST.class,one = @One(select = "com.americatel.facturacion.mappers.mapPLAN_MEDI_INST.getId"),id = false),
            @Result(property = "mone_fact",column = "CO_MONE_FACT",javaType = PLAN_MEDI_INST.class,one = @One(select = "com.americatel.facturacion.mappers.mapMONE_FACT.getId"),id = false),
        }
    )
    public PLAN getId(int id);
    
    @Select("SELECT * FROM TMPLAN WHERE CO_PLAN=#{id} AND ST_ELIM=0")
    @Results(value={
            @Result(property = "CO_PLAN",column="CO_PLAN",id = true), 
            @Result(property = "CO_PROD",column="CO_PROD"), 
            @Result(property = "NO_PLAN",column="NO_PLAN"),     
            @Result(property = "IM_MONTO",column="IM_MONTO"), 
            @Result(property = "DE_VELO_SUBI",column="DE_VELO_SUBI"),
            @Result(property = "DE_VELO_BAJA",column="DE_VELO_BAJA"),
            @Result(property = "CO_PLAN_MEDI_INST",column="CO_PLAN_MEDI_INST"),
            @Result(property = "CO_MONE_FACT",column="CO_MONE_FACT"),
            @Result(property = "ST_ELIM",column="ST_ELIM"),           
            @Result(property = "ST_BACKUP_BU",column="ST_BACKUP_BU"),
            @Result(property = "plan_medio_inst",column = "CO_PLAN_MEDI_INST",javaType = PLAN_MEDI_INST.class,one = @One(select = "com.americatel.facturacion.mappers.mapPLAN_MEDI_INST.getId"),id = false),
        }
    )
    public PLAN getOnlyId(int id);  
        
    @Update("UPDATE TMPLAN SET NO_PLAN='${NO_PLAN}',CO_PROD='${CO_PROD}',IM_MONTO='${IM_MONTO}',DE_VELO_SUBI='${DE_VELO_SUBI}',DE_VELO_BAJA='${DE_VELO_BAJA}',CO_PLAN_MEDI_INST='${CO_PLAN_MEDI_INST}',CO_MONE_FACT='${CO_MONE_FACT}',ST_BACKUP_BU='${ST_BACKUP_BU}',RELACIONADO_TI='${RELACIONADO_TI}',PRODUCTO_CARGA='${PRODUCTO_CARGA}',"+Historial.stringMapperModificar+" WHERE CO_PLAN=${CO_PLAN} AND ST_ELIM=0;")
    public int update(PLAN PROD);

    //@Delete("DELETE FROM TMPLAN WHERE CO_PLAN=${CO_PLAN};")
    @Update("UPDATE TMPLAN SET ST_ELIM=1,"+Historial.stringMapperModificar+" WHERE CO_PLAN=${CO_PLAN};")
    public int delete(PLAN PROD);

    @Insert("INSERT INTO TMPLAN (NO_PLAN,CO_PROD,IM_MONTO,DE_VELO_SUBI,DE_VELO_BAJA,CO_PLAN_MEDI_INST,CO_MONE_FACT,ST_BACKUP_BU,RELACIONADO_TI,PRODUCTO_CARGA,"+Historial.stringMapperInsertarTitles+") VALUES ('${NO_PLAN}','${CO_PROD}','${IM_MONTO}','${DE_VELO_SUBI}','${DE_VELO_BAJA}','${CO_PLAN_MEDI_INST}','${CO_MONE_FACT}','${ST_BACKUP_BU}','${RELACIONADO_TI}','${PRODUCTO_CARGA}',"+Historial.stringMapperInsertarValuesNumeral+")")
    @SelectKey(statement="SELECT @@IDENTITY;", keyProperty="CO_PLAN", before=false, resultType=Integer.class)
    public void insert(PLAN PROD);

    //@Select("SELECT * FROM TMPLAN WHERE CO_PLAN like '%${CO_PLAN}%' AND ST_ELIM=0")
    // P.*,PR_PA.COD_PROD_PADRE
   
    
    @Select("SELECT P.CO_PLAN,P.NO_PLAN AS NO_PLAN,P.CO_MONE_FACT AS CO_MONE_FACT,P.IM_MONTO AS IM_MONTO,P.CO_PROD AS CO_PROD,PR_P.COD_PROD_PADRE AS COD_PROD_PADRE,P.DE_VELO_SUBI AS DE_VELO_SUBI,P.DE_VELO_BAJA AS DE_VELO_BAJA,P.CO_PLAN_MEDI_INST AS CO_PLAN_MEDI_INST,PR_PA.COD_PROD_PADRE AS COD_PROD_PADRE, P.RELACIONADO_TI AS RELACIONADO_TI FROM TMPLAN P INNER JOIN TMPROD PR ON P.CO_PROD = PR.CO_PROD INNER JOIN TMPROD_PADRE_TMPROD PR_P ON PR.CO_PROD =  PR_P.COD_PROD INNER JOIN TMPROD_PADRE PR_PA ON PR_P.COD_PROD_PADRE = PR_PA.COD_PROD_PADRE WHERE P.CO_PLAN  like '%${CO_PLAN}%' AND P.ST_ELIM=0")
    @Results(value={
            @Result(property = "CO_PLAN",column="CO_PLAN",id = true), 
            @Result(property = "CO_PROD",column="CO_PROD"),
            @Result(property = "COD_PROD_PADRE",column="COD_PROD_PADRE"), // CARGOS FIJOS - EMANUEL BARBARAN - EFITEC (SE AGREGO CAMPO)
            @Result(property = "NO_PLAN",column="NO_PLAN"),     
            @Result(property = "IM_MONTO",column="IM_MONTO"),
            @Result(property = "DE_VELO_SUBI",column="DE_VELO_SUBI"),
            @Result(property = "DE_VELO_BAJA",column="DE_VELO_BAJA"),            
            @Result(property = "CO_PLAN_MEDI_INST",column="CO_PLAN_MEDI_INST"),
            @Result(property = "CO_MONE_FACT",column="CO_MONE_FACT"),
            @Result(property = "ST_ELIM",column="ST_ELIM"),
            @Result(property = "ST_BACKUP_BU",column="ST_BACKUP_BU"),
            @Result(property = "RELACIONADO_TI",column="RELACIONADO_TI"),
            @Result(property = "prod_padre",column = "COD_PROD_PADRE",javaType = PROD_PADRE.class,one = @One(select = "com.americatel.facturacion.mappers.mapPROD.getIdProd"),id = false),// CARGOS FIJOS - EMANUEL BARBARAN - EFITEC (SE AGREGO CAMPO)
            @Result(property = "prod",column = "CO_PROD",javaType = PROD.class,one = @One(select = "com.americatel.facturacion.mappers.mapPROD.getId"),id = false),
            @Result(property = "plan_medio_inst",column = "CO_PLAN_MEDI_INST",javaType = PLAN_MEDI_INST.class,one = @One(select = "com.americatel.facturacion.mappers.mapPLAN_MEDI_INST.getId"),id = false),
            @Result(property = "mone_fact",column = "CO_MONE_FACT",javaType = PLAN_MEDI_INST.class,one = @One(select = "com.americatel.facturacion.mappers.mapMONE_FACT.getId"),id = false),
        }
    )  
    public List<PLAN> select(@Param("CO_PLAN") String CO_PLAN);    

    
    @Select("SELECT * FROM TMPLAN WHERE CO_PROD=#{CO_PROD} AND CO_MONE_FACT=#{CO_MONE_FACT} AND ST_ELIM=0")
    @Results(value={
            @Result(property = "CO_PLAN",column="CO_PLAN",id = true), 
            @Result(property = "CO_PROD",column="CO_PROD"), 
            @Result(property = "NO_PLAN",column="NO_PLAN"),     
            @Result(property = "IM_MONTO",column="IM_MONTO"), 
            @Result(property = "DE_VELO_SUBI",column="DE_VELO_SUBI"),
            @Result(property = "DE_VELO_BAJA",column="DE_VELO_BAJA"),
            @Result(property = "CO_PLAN_MEDI_INST",column="CO_PLAN_MEDI_INST"),
            @Result(property = "CO_MONE_FACT",column="CO_MONE_FACT"),
            @Result(property = "ST_ELIM",column="ST_ELIM"),
            @Result(property = "ST_BACKUP_BU",column="ST_BACKUP_BU"),
            @Result(property = "plan_medio_inst",column = "CO_PLAN_MEDI_INST",javaType = PLAN_MEDI_INST.class,one = @One(select = "com.americatel.facturacion.mappers.mapPLAN_MEDI_INST.getId"),id = false),
            @Result(property = "mone_fact",column = "CO_MONE_FACT",javaType = PLAN_MEDI_INST.class,one = @One(select = "com.americatel.facturacion.mappers.mapMONE_FACT.getId"),id = false),
            
        }
    )     
    public List<PLAN> selectByPRODAndMONE(@Param("CO_PROD") int CO_PROD,@Param("CO_MONE_FACT") int CO_MONE_FACT);
    
    @Select("SELECT CAST( CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END AS bit) AS EXISTE " +
    "FROM TMPLAN WHERE CO_PROD='${CO_PROD}' AND DE_VELO_SUBI='${DE_VELO_SUBI}' AND DE_VELO_BAJA='${DE_VELO_BAJA}' " +
    "AND CO_PLAN_MEDI_INST='${CO_PLAN_MEDI_INST}' AND IM_MONTO='${IM_MONTO}' AND CO_MONE_FACT='${CO_MONE_FACT}' " +
    "AND ST_BACKUP_BU='${ST_BACKUP_BU}' AND ST_ELIM=0")
    public boolean existePlan(PLAN PROD);
   
    @Select("SELECT TOP 1 * FROM TMPLAN WHERE CO_PROD='${CO_PROD}' AND DE_VELO_SUBI='${DE_VELO_SUBI}' " +
    "AND DE_VELO_BAJA='${DE_VELO_BAJA}' AND CO_PLAN_MEDI_INST='${CO_PLAN_MEDI_INST}' AND IM_MONTO='${IM_MONTO}' " +
    "AND CO_MONE_FACT='${CO_MONE_FACT}' AND ST_BACKUP_BU='${ST_BACKUP_BU}' AND ST_ELIM=0 AND NO_PLAN='${NO_PLAN}' AND RELACIONADO_TI='${RELACIONADO_TI}' ")
    @Results(value={
            @Result(property = "CO_PLAN",column="CO_PLAN",id = true), 
            @Result(property = "CO_PROD",column="CO_PROD"), 
            @Result(property = "NO_PLAN",column="NO_PLAN"),     
            @Result(property = "IM_MONTO",column="IM_MONTO"), 
            @Result(property = "DE_VELO_SUBI",column="DE_VELO_SUBI"),
            @Result(property = "DE_VELO_BAJA",column="DE_VELO_BAJA"),
            @Result(property = "CO_PLAN_MEDI_INST",column="CO_PLAN_MEDI_INST"),
            @Result(property = "CO_MONE_FACT",column="CO_MONE_FACT"),
            @Result(property = "ST_ELIM",column="ST_ELIM"),
            @Result(property = "ST_BACKUP_BU",column="ST_BACKUP_BU"),
            @Result(property = "plan_medio_inst",column = "CO_PLAN_MEDI_INST",javaType = PLAN_MEDI_INST.class,one = @One(select = "com.americatel.facturacion.mappers.mapPLAN_MEDI_INST.getId"),id = false),
        }
    )
    public PLAN selectExistPlan(PLAN PROD); 
    
    @Select("SELECT * FROM REGLAS_NOMBRE_PLAN WHERE COD_SERVICIO=#{COD_SERVICIO} AND ST_ELIM=0")
    @Results(value={
            @Result(property = "COD_REGLAS_NOMBRE_PLAN",column="COD_REGLAS_NOMBRE_PLAN",id = true), 
            @Result(property = "NOMBRE_PLAN",column="NOMBRE_PLAN")   
        }
    )     
    public List<REGLAS_NOMBRE_PLAN> getNombrePlanByServicio(@Param("COD_SERVICIO") String COD_SERVICIO);
    
    @Select("SELECT * FROM REGLAS_NOMBRE_PLAN WHERE COD_PRODUCTO=#{COD_PRODUCTO} AND COD_SERVICIO=#{COD_SERVICIO} AND APLICA_VELOCIDAD=#{APLICA_VELOCIDAD} AND ARRENDAMIENTO=#{ARRENDAMIENTO} AND RELACIONADO_TI=#{RELACIONADO_TI} AND ST_ELIM=0")
    @Results(value={
            @Result(property = "COD_REGLAS_NOMBRE_PLAN",column="COD_REGLAS_NOMBRE_PLAN",id = true), 
            @Result(property = "NOMBRE_PLAN",column="NOMBRE_PLAN")   
        }
    )      
    public List<REGLAS_NOMBRE_PLAN> getNombrePlan(@Param("COD_PRODUCTO") int COD_PRODUCTO,@Param("COD_SERVICIO") int COD_SERVICIO,
                                            @Param("APLICA_VELOCIDAD") int APLICA_VELOCIDAD,@Param("ARRENDAMIENTO") int ARRENDAMIENTO,@Param("RELACIONADO_TI") int RELACIONADO_TI);
       
    
    @Select("SELECT * FROM REGLAS_NOMBRE_PLAN WHERE COD_REGLAS_NOMBRE_PLAN=#{COD_REGLAS_NOMBRE_PLAN} AND ST_ELIM=0")
    @Results(value={
            @Result(property = "COD_REGLAS_NOMBRE_PLAN",column="COD_REGLAS_NOMBRE_PLAN",id = true), 
            @Result(property = "NOMBRE_PLAN",column="NOMBRE_PLAN")   
        }
    )      
    public List<REGLAS_NOMBRE_PLAN> getNombrePlanByCodigo(@Param("COD_REGLAS_NOMBRE_PLAN") int COD_REGLAS_NOMBRE_PLAN);
    
    @Select("SELECT * FROM REGLAS_NOMBRE_PLAN WHERE NOMBRE_PLAN=#{NO_PLAN} AND ST_ELIM=0")
    @Results(value={
            @Result(property = "COD_REGLAS_NOMBRE_PLAN",column="COD_REGLAS_NOMBRE_PLAN",id = true), 
            @Result(property = "NOMBRE_PLAN",column="NOMBRE_PLAN")   
        }
    )      
    public List<REGLAS_NOMBRE_PLAN> getIdNombrePlan(@Param("NO_PLAN") String NO_PLAN);
    
    
        
    @Select("SELECT PRODUCTO_CARGA,NOMBRE_PLAN FROM REGLAS_NOMBRE_PLAN WHERE COD_PRODUCTO=#{COD_PRODUCTO} AND COD_SERVICIO=#{COD_SERVICIO} AND APLICA_VELOCIDAD=#{APLICA_VELOCIDAD} AND ARRENDAMIENTO=#{ARRENDAMIENTO} AND RELACIONADO_TI=#{RELACIONADO_TI} AND ST_ELIM=0")
    @Results(value={
            @Result(property = "PRODUCTO_CARGA",column="PRODUCTO_CARGA"), 
            @Result(property = "NOMBRE_PLAN",column="NOMBRE_PLAN")   
        }
    )      
    public List<REGLAS_NOMBRE_PLAN> getProductoCarga(@Param("COD_PRODUCTO") int COD_PRODUCTO,@Param("COD_SERVICIO") int COD_SERVICIO,
                                            @Param("APLICA_VELOCIDAD") int APLICA_VELOCIDAD,@Param("ARRENDAMIENTO") int ARRENDAMIENTO,@Param("RELACIONADO_TI") int RELACIONADO_TI);
    
    @Select("SELECT * FROM TMPLAN WHERE CO_PLAN=#{id} AND ST_ELIM=0")
    @Results(value={
            @Result(property = "CO_PLAN",column="CO_PLAN",id = true), 
            @Result(property = "CO_PROD",column="CO_PROD"), 
            @Result(property = "NO_PLAN",column="NO_PLAN"),     
            @Result(property = "IM_MONTO",column="IM_MONTO"), 
            @Result(property = "DE_VELO_SUBI",column="DE_VELO_SUBI"),
            @Result(property = "DE_VELO_BAJA",column="DE_VELO_BAJA"),
            @Result(property = "CO_PLAN_MEDI_INST",column="CO_PLAN_MEDI_INST"),
            @Result(property = "CO_MONE_FACT",column="CO_MONE_FACT"),
            @Result(property = "ST_ELIM",column="ST_ELIM"),
            @Result(property = "ST_BACKUP_BU",column="ST_BACKUP_BU"),
            @Result(property = "RELACIONADO_TI",column="RELACIONADO_TI"),
            @Result(property = "PRODUCTO_CARGA",column="PRODUCTO_CARGA")
            }
    )
    public PLAN getOnlyObjectForId(Integer id);
    
    @Select("SELECT TOP 1 P.* , PP.COD_PROD_PADRE FROM TMNEGO_SUCU_PLAN NSP INNER JOIN TMPLAN P ON P.CO_PLAN = NSP.CO_PLAN INNER JOIN TMPROD_PADRE_TMPROD PP ON PP.COD_PROD = P.CO_PROD WHERE NSP.CO_OIT_INST = #{CO_OIT_INST} AND P.ST_ELIM = 0")
    @Results(value={
            @Result(property = "CO_PLAN",column="CO_PLAN",id = true), 
            @Result(property = "CO_PROD",column="CO_PROD"), 
            @Result(property = "NO_PLAN",column="NO_PLAN"),     
            @Result(property = "IM_MONTO",column="IM_MONTO"), 
            @Result(property = "DE_VELO_SUBI",column="DE_VELO_SUBI"),
            @Result(property = "DE_VELO_BAJA",column="DE_VELO_BAJA"),
            @Result(property = "CO_PLAN_MEDI_INST",column="CO_PLAN_MEDI_INST"),
            @Result(property = "CO_MONE_FACT",column="CO_MONE_FACT"),
            @Result(property = "ST_ELIM",column="ST_ELIM"),
            @Result(property = "ST_BACKUP_BU",column="ST_BACKUP_BU"),
            @Result(property = "RELACIONADO_TI",column="RELACIONADO_TI"),
            @Result(property = "COD_PROD_PADRE",column="COD_PROD_PADRE"),
            @Result(property = "PRODUCTO_CARGA",column="PRODUCTO_CARGA")
            }
    )
    public PLAN getForOitInstalacion(Integer CO_OIT_INST);
    
    
    
    @Select("SELECT P.* FROM TMPLAN P INNER JOIN TMNEGO_SUCU_PLAN NSP ON P.CO_PLAN = NSP.CO_PLAN "
    		+ "WHERE NSP.CO_NEGO_SUCU_PLAN = #{CO_NEGO_SUCU_PLAN} ")
    @Results(value={
            @Result(property = "CO_PLAN",column="CO_PLAN",id = true), 
            @Result(property = "CO_PROD",column="CO_PROD"), 
            @Result(property = "NO_PLAN",column="NO_PLAN"),     
            @Result(property = "IM_MONTO",column="IM_MONTO"), 
            @Result(property = "DE_VELO_SUBI",column="DE_VELO_SUBI"),
            @Result(property = "DE_VELO_BAJA",column="DE_VELO_BAJA"),
            @Result(property = "CO_PLAN_MEDI_INST",column="CO_PLAN_MEDI_INST"),
            @Result(property = "CO_MONE_FACT",column="CO_MONE_FACT"),
            @Result(property = "ST_ELIM",column="ST_ELIM"),
            @Result(property = "ST_BACKUP_BU",column="ST_BACKUP_BU"),
            @Result(property = "RELACIONADO_TI",column="RELACIONADO_TI"),
            @Result(property = "PRODUCTO_CARGA",column="PRODUCTO_CARGA")
            }
    )
    public PLAN getPlanForCodNegoSucuPlan(Integer CO_NEGO_SUCU_PLAN);
    
    
    
    
}   
