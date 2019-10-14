/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;	

import com.americatel.facturacion.historial.Historial;
import com.americatel.facturacion.models.PROD;
import com.americatel.facturacion.models.PROD_PADRE;
import com.americatel.facturacion.models.REGLAS_NOMBRE_PLAN;


public interface MapReglaNombrePlan {

    @Select("SELECT * FROM REGLAS_NOMBRE_PLAN WHERE COD_REGLAS_NOMBRE_PLAN=#{id} AND ST_ELIM=0")
    @Results(value={
            @Result(property = "COD_REGLAS_NOMBRE_PLAN",column="COD_REGLAS_NOMBRE_PLAN",id = true), 
            @Result(property = "COD_PROD_PADRE",column="COD_PRODUCTO"), 
            @Result(property = "CO_PROD",column="COD_SERVICIO"),     
            @Result(property = "NOMBRE_PLAN",column="NOMBRE_PLAN"), 
            @Result(property = "APLICA_VELOCIDAD",column="APLICA_VELOCIDAD"),
            @Result(property = "ARRENDAMIENTO",column="ARRENDAMIENTO"),
            @Result(property = "RELACIONADO_TI",column="RELACIONADO_TI"),
            @Result(property = "ST_ELIM",column="ST_ELIM"),
            @Result(property = "PRODUCTO_CARGA",column="PRODUCTO_CARGA"),
            @Result(property = "prod_padre",column = "COD_PRODUCTO",javaType = PROD_PADRE.class,one = @One(select = "com.americatel.facturacion.mappers.mapPROD.getIdProd"),id = false),
            @Result(property = "prod",column = "COD_SERVICIO",javaType = PROD.class,one = @One(select = "com.americatel.facturacion.mappers.mapPROD.getId"),id = false),
        }
    )
    public REGLAS_NOMBRE_PLAN getId(Long id);
    
    @Select("SELECT * FROM REGLAS_NOMBRE_PLAN WHERE COD_PRODUCTO=#{codProdPadre} AND ST_ELIM=0")
    @Results(value={
            @Result(property = "COD_REGLAS_NOMBRE_PLAN",column="COD_REGLAS_NOMBRE_PLAN",id = true), 
            @Result(property = "COD_PROD_PADRE",column="COD_PRODUCTO"), 
            @Result(property = "CO_PROD",column="COD_SERVICIO"),     
            @Result(property = "NOMBRE_PLAN",column="NOMBRE_PLAN"), 
            @Result(property = "APLICA_VELOCIDAD",column="APLICA_VELOCIDAD"),
            @Result(property = "ARRENDAMIENTO",column="ARRENDAMIENTO"),
            @Result(property = "RELACIONADO_TI",column="RELACIONADO_TI"),
            @Result(property = "ST_ELIM",column="ST_ELIM"),
            @Result(property = "PRODUCTO_CARGA",column="PRODUCTO_CARGA"),
            @Result(property = "prod_padre",column = "COD_PRODUCTO",javaType = PROD_PADRE.class,one = @One(select = "com.americatel.facturacion.mappers.mapPROD.getIdProd"),id = false),
            @Result(property = "prod",column = "COD_SERVICIO",javaType = PROD.class,one = @One(select = "com.americatel.facturacion.mappers.mapPROD.getId"),id = false),
        }
    )
    public List<REGLAS_NOMBRE_PLAN> select(Long codProdPadre);
    
    @Select("SELECT * FROM REGLAS_NOMBRE_PLAN WHERE ST_ELIM=0")
    @Results(value={
            @Result(property = "COD_REGLAS_NOMBRE_PLAN",column="COD_REGLAS_NOMBRE_PLAN",id = true), 
            @Result(property = "COD_PROD_PADRE",column="COD_PRODUCTO"), 
            @Result(property = "CO_PROD",column="COD_SERVICIO"),     
            @Result(property = "NOMBRE_PLAN",column="NOMBRE_PLAN"), 
            @Result(property = "APLICA_VELOCIDAD",column="APLICA_VELOCIDAD"),
            @Result(property = "ARRENDAMIENTO",column="ARRENDAMIENTO"),
            @Result(property = "RELACIONADO_TI",column="RELACIONADO_TI"),
            @Result(property = "ST_ELIM",column="ST_ELIM"),
            @Result(property = "PRODUCTO_CARGA",column="PRODUCTO_CARGA"),
            @Result(property = "prod_padre",column = "COD_PRODUCTO",javaType = PROD_PADRE.class,one = @One(select = "com.americatel.facturacion.mappers.mapPROD.getIdProd"),id = false),
            @Result(property = "prod",column = "COD_SERVICIO",javaType = PROD.class,one = @One(select = "com.americatel.facturacion.mappers.mapPROD.getId"),id = false),
        }
    )
    public List<REGLAS_NOMBRE_PLAN> selectAllActivos();
    
    
        
    @Update("UPDATE REGLAS_NOMBRE_PLAN SET COD_PRODUCTO = '${COD_PROD_PADRE}', COD_SERVICIO = '${CO_PROD}', NOMBRE_PLAN = '${NOMBRE_PLAN}', APLICA_VELOCIDAD = '${APLICA_VELOCIDAD_INT}', ARRENDAMIENTO = '${ARRENDAMIENTO}', RELACIONADO_TI = '${RELACIONADO_TI}', ST_ELIM = '${ST_ELIM}', PRODUCTO_CARGA = '${PRODUCTO_CARGA}',"+Historial.stringMapperModificar+" WHERE COD_REGLAS_NOMBRE_PLAN=${COD_REGLAS_NOMBRE_PLAN} AND ST_ELIM=0;")
    public int update(REGLAS_NOMBRE_PLAN reglaNombrePlan);

    @Update("UPDATE REGLAS_NOMBRE_PLAN SET ST_ELIM=1,"+Historial.stringMapperModificar+" WHERE COD_REGLAS_NOMBRE_PLAN=${COD_REGLAS_NOMBRE_PLAN};")
    public int delete(REGLAS_NOMBRE_PLAN reglaNombrePlan);

    @Insert("INSERT INTO REGLAS_NOMBRE_PLAN (COD_REGLAS_NOMBRE_PLAN, COD_PRODUCTO,COD_SERVICIO,NOMBRE_PLAN,APLICA_VELOCIDAD,ARRENDAMIENTO,RELACIONADO_TI,ST_ELIM,PRODUCTO_CARGA,"+Historial.stringMapperInsertarTitles+") VALUES ('${COD_REGLAS_NOMBRE_PLAN}', '${COD_PROD_PADRE}', '${CO_PROD}', '${NOMBRE_PLAN}', '${APLICA_VELOCIDAD_INT}', '${ARRENDAMIENTO}', '${RELACIONADO_TI}', '${ST_ELIM}', '${PRODUCTO_CARGA}',"+Historial.stringMapperInsertarValuesNumeral+")")
    public void insert(REGLAS_NOMBRE_PLAN reglaNombrePlan);

    @Select("SELECT MAX(COD_REGLAS_NOMBRE_PLAN)+1 FROM REGLAS_NOMBRE_PLAN ")
    public Long getMaxId();
    
    @Select("SELECT COUNT(1) FROM TMNEGO N INNER JOIN REGLAS_NOMBRE_PLAN RNP ON N.CO_PROD = RNP.COD_SERVICIO  WHERE RNP.COD_REGLAS_NOMBRE_PLAN = ${COD_REGLAS_NOMBRE_PLAN} AND RNP.ST_ELIM = 0")
    public Long getCountBussinesPartnersById(REGLAS_NOMBRE_PLAN reglaNombrePlan);
    
    @Select("SELECT COUNT(1) FROM REGLAS_NOMBRE_PLAN RNP WHERE RNP.COD_SERVICIO = ${CO_PROD} AND RNP.ST_ELIM = 0 AND RNP.APLICA_VELOCIDAD = ${APLICA_VELOCIDAD_INT}")
    public Long getCountForService(REGLAS_NOMBRE_PLAN reglaNombrePlan);
    
    @Select("SELECT  *  FROM REGLAS_NOMBRE_PLAN  "
    		+ "WHERE COD_PRODUCTO = #{codProducto} AND COD_SERVICIO = #{codServicio} AND APLICA_VELOCIDAD = #{aplicaVelocidad} "
    		+ "AND ARRENDAMIENTO = #{arrendamiento} AND RELACIONADO_TI = #{relacionadoTI} AND ST_ELIM = 0")
    @Results(value={
            @Result(property = "COD_REGLAS_NOMBRE_PLAN",column="COD_REGLAS_NOMBRE_PLAN",id = true), 
            @Result(property = "COD_PROD_PADRE",column="COD_PRODUCTO"), 
            @Result(property = "CO_PROD",column="COD_SERVICIO"),     
            @Result(property = "NOMBRE_PLAN",column="NOMBRE_PLAN"), 
            @Result(property = "APLICA_VELOCIDAD",column="APLICA_VELOCIDAD"),
            @Result(property = "ARRENDAMIENTO",column="ARRENDAMIENTO"),
            @Result(property = "RELACIONADO_TI",column="RELACIONADO_TI"),
            @Result(property = "ST_ELIM",column="ST_ELIM"),
            @Result(property = "PRODUCTO_CARGA",column="PRODUCTO_CARGA")
    		}
    )
    public String obtenerNombreRegla (Long codProducto, Long codServicio, Long aplicaVelocidad, Long arrendamiento, Long relacionadoTI);
    
    
    @Select("SELECT  *  FROM REGLAS_NOMBRE_PLAN  "
    		+ "WHERE COD_PRODUCTO = #{codProducto} AND COD_SERVICIO = #{codServicio} AND NOMBRE_PLAN = #{nombrePlan}"
    		+ "AND ARRENDAMIENTO = #{stBackupBu} AND RELACIONADO_TI = #{relacionadoTI} AND ST_ELIM = 0")
    @Results(value={
            @Result(property = "COD_REGLAS_NOMBRE_PLAN",column="COD_REGLAS_NOMBRE_PLAN",id = true), 
            @Result(property = "COD_PROD_PADRE",column="COD_PRODUCTO"), 
            @Result(property = "CO_PROD",column="COD_SERVICIO"),     
            @Result(property = "NOMBRE_PLAN",column="NOMBRE_PLAN"), 
            @Result(property = "APLICA_VELOCIDAD",column="APLICA_VELOCIDAD"),
            @Result(property = "ARRENDAMIENTO",column="ARRENDAMIENTO"),
            @Result(property = "RELACIONADO_TI",column="RELACIONADO_TI"),
            @Result(property = "ST_ELIM",column="ST_ELIM"),
            @Result(property = "PRODUCTO_CARGA",column="PRODUCTO_CARGA")
    		}
    )
    public String obtenerNombreProductoCarga (Integer codProducto, Boolean stBackupBu, Integer codServicio, Boolean relacionadoTI, String nombrePlan);
    
    
}   
