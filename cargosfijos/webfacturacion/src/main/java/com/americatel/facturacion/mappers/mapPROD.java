/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.mappers;

import com.americatel.facturacion.historial.Historial;
import com.americatel.facturacion.models.DEPA;
import com.americatel.facturacion.models.DIST;
import com.americatel.facturacion.models.MONE_FACT;
import com.americatel.facturacion.models.NEGO;
import com.americatel.facturacion.models.PERI_FACT;
import com.americatel.facturacion.models.PROD;
import com.americatel.facturacion.models.PROD_PADRE;
import com.americatel.facturacion.models.PROV;
import com.americatel.facturacion.models.REGLAS_NOMBRE_PLAN;
import com.americatel.facturacion.models.TIPO_FACT;
import java.util.List;
import java.util.Map;
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
public interface mapPROD {
    @Select("SELECT * FROM TMPROD WHERE CO_PROD=#{id} AND ST_ELIM=0")
    @Results(value={
            @Result(property = "CO_PROD",column="CO_PROD",id = true),
            @Result(property = "NO_PROD",column="NO_PROD"),     
            @Result(property = "CO_TIPO_FACT",column="CO_TIPO_FACT"),     
            @Result(property = "CO_MONE_FACT",column="CO_MONE_FACT"),     
            @Result(property = "CO_PERI_FACT",column="CO_PERI_FACT"),  
            @Result(property = "ST_ELIM",column="ST_ELIM"),
        }
    )
    public PROD getOnlyId(int id);
    
    
    @Select("SELECT * FROM TMPROD PR INNER JOIN TMPROD_PADRE_TMPROD PR_P ON PR.CO_PROD =  PR_P.COD_PROD INNER JOIN TMPROD_PADRE PR_PA ON PR_P.COD_PROD_PADRE = PR_PA.COD_PROD_PADRE WHERE PR.CO_PROD=#{CO_PROD} AND PR.ST_ELIM=0")
    @Results(value={
            @Result(property = "CO_PROD",column="CO_PROD",id = true),
            @Result(property = "NO_PROD",column="NO_PROD"),   
            @Result(property = "COD_PROD_PADRE",column="COD_PROD_PADRE"), // CARGOS FIJOS - EMANUEL BARBARAN - EFITEC (SE AGREGO CAMPO)
            @Result(property = "CO_TIPO_FACT",column="CO_TIPO_FACT"),     
            @Result(property = "CO_MONE_FACT",column="CO_MONE_FACT"),     
            @Result(property = "CO_PERI_FACT",column="CO_PERI_FACT"), 
            @Result(property = "ST_ELIM",column="ST_ELIM"),
            @Result(property = "prod_padre",column = "COD_PROD_PADRE",javaType = PROD_PADRE.class,one = @One(select = "com.americatel.facturacion.mappers.mapPROD.getIdProd"),id = false),// CARGOS FIJOS - EMANUEL BARBARAN - EFITEC (SE AGREGO CAMPO)
            @Result(property = "tipo_fact",column = "CO_TIPO_FACT",javaType = TIPO_FACT.class,one = @One(select = "com.americatel.facturacion.mappers.mapTIPO_FACT.getId"),id = false),
            @Result(property = "mone_fact",column = "CO_MONE_FACT",javaType = MONE_FACT.class,one = @One(select = "com.americatel.facturacion.mappers.mapMONE_FACT.getId"),id = false),
            @Result(property = "peri_fact",column = "CO_PERI_FACT",javaType = PERI_FACT.class,one = @One(select = "com.americatel.facturacion.mappers.mapPERI_FACT.getId"),id = false)
        }
    )
    public PROD getServiceByIdCombo(@Param("CO_PROD") int CO_PROD);
    
    @Select("SELECT * FROM TMPROD WHERE CO_PROD=#{id} AND ST_ELIM=0")
    @Results(value={
            @Result(property = "CO_PROD",column="CO_PROD",id = true),
            @Result(property = "NO_PROD",column="NO_PROD"),     
            @Result(property = "CO_TIPO_FACT",column="CO_TIPO_FACT"),     
            @Result(property = "CO_MONE_FACT",column="CO_MONE_FACT"),     
            @Result(property = "CO_PERI_FACT",column="CO_PERI_FACT"), 
            @Result(property = "ST_ELIM",column="ST_ELIM"),
            @Result(property = "tipo_fact",column = "CO_TIPO_FACT",javaType = TIPO_FACT.class,one = @One(select = "com.americatel.facturacion.mappers.mapTIPO_FACT.getId"),id = false),
            @Result(property = "mone_fact",column = "CO_MONE_FACT",javaType = MONE_FACT.class,one = @One(select = "com.americatel.facturacion.mappers.mapMONE_FACT.getId"),id = false),
            @Result(property = "peri_fact",column = "CO_PERI_FACT",javaType = PERI_FACT.class,one = @One(select = "com.americatel.facturacion.mappers.mapPERI_FACT.getId"),id = false)
        }
    )
    public PROD getId(int id);
    
    @Select("SELECT * FROM TMPROD WHERE ST_ELIM=0")
    public List<PROD> getAll();
    
    //@Select("SELECT PR.* FROM TMPROD PR INNER JOIN TMPROD_PADRE_TMPROD PR_P ON PR.CO_PROD =  PR_P.COD_PROD INNER JOIN TMPROD_PADRE PR_PA ON PR_P.COD_PROD_PADRE = PR_PA.COD_PROD_PADRE WHERE PR_PA.COD_PROD_PADRE = '${CO_PROD}' AND PR.ST_ELIM=0;")    
    @Select("SELECT PR.* FROM TMPROD PR INNER JOIN TMPROD_PADRE_TMPROD PR_P ON PR.CO_PROD =  PR_P.COD_PROD INNER JOIN TMPROD_PADRE PR_PA ON PR_P.COD_PROD_PADRE = PR_PA.COD_PROD_PADRE WHERE PR_PA.COD_PROD_PADRE = '${CO_PROD}' AND PR.ST_ELIM=0 ;") //and PR.CO_PROD in (7,8,9);   
    public List<PROD> getAllByProduc(@Param("CO_PROD") int CO_PROD);
    
    @Update("UPDATE TMPROD SET NO_PROD='${NO_PROD}',CO_TIPO_FACT='${CO_TIPO_FACT}',CO_MONE_FACT='${CO_MONE_FACT}',CO_PERI_FACT='${CO_PERI_FACT}',"+Historial.stringMapperModificar+" WHERE CO_PROD=${CO_PROD} AND ST_ELIM=0;")
    public int update(PROD prod);

    //@Delete("DELETE FROM TMPROD WHERE CO_PROD=${CO_PROD};")
    @Update("UPDATE TMPROD SET ST_ELIM=1,"+Historial.stringMapperModificar+" WHERE CO_PROD=${CO_PROD};")
    public int delete(PROD prod);

    @Insert("INSERT INTO TMPROD (NO_PROD,CO_TIPO_FACT,CO_MONE_FACT,CO_PERI_FACT,"+Historial.stringMapperInsertarTitles+") VALUES ('${NO_PROD}','${CO_TIPO_FACT}','${CO_MONE_FACT}','${CO_PERI_FACT}',"+Historial.stringMapperInsertarValuesNumeral+")")
    @SelectKey(statement="SELECT @@IDENTITY;", keyProperty="CO_PROD", before=false, resultType=Integer.class)
    public void insert(PROD prod);

    //@Select("SELECT * FROM TMPROD WHERE NO_PROD like '%${NO_PROD}%' AND ST_ELIM=0")
    @Select("SELECT PR.CO_PROD AS CO_PROD, PR.NO_PROD AS NO_PROD,PR_PA.COD_PROD_PADRE AS COD_PROD_PADRE ,PR.CO_TIPO_FACT AS CO_TIPO_FACT, PR.CO_MONE_FACT AS CO_MONE_FACT, PR.CO_PERI_FACT AS CO_PERI_FAC, PR.ST_ELIM AS ST_ELIM  FROM TMPROD PR INNER JOIN TMPROD_PADRE_TMPROD PR_P ON PR.CO_PROD =  PR_P.COD_PROD INNER JOIN TMPROD_PADRE PR_PA ON PR_P.COD_PROD_PADRE = PR_PA.COD_PROD_PADRE  WHERE PR.NO_PROD like '%${NO_PROD}%' AND PR.ST_ELIM=0")
    @Results(value={
            @Result(property = "CO_PROD",column="CO_PROD",id = true),
            @Result(property = "NO_PROD",column="NO_PROD"), 
            @Result(property = "COD_PROD_PADRE",column="COD_PROD_PADRE"), // CARGOS FIJOS - EMANUEL BARBARAN - EFITEC (SE AGREGO CAMPO)
            @Result(property = "CO_TIPO_FACT",column="CO_TIPO_FACT"),     
            @Result(property = "CO_MONE_FACT",column="CO_MONE_FACT"),     
            @Result(property = "CO_PERI_FACT",column="CO_PERI_FACT"), 
            @Result(property = "ST_ELIM",column="ST_ELIM"),
            @Result(property = "prod_padre",column = "COD_PROD_PADRE",javaType = PROD_PADRE.class,one = @One(select = "com.americatel.facturacion.mappers.mapPROD.getIdProd"),id = false),// CARGOS FIJOS - EMANUEL BARBARAN - EFITEC (SE AGREGO CAMPO)
            @Result(property = "tipo_fact",column = "CO_TIPO_FACT",javaType = TIPO_FACT.class,one = @One(select = "com.americatel.facturacion.mappers.mapTIPO_FACT.getId"),id = false),
            @Result(property = "mone_fact",column = "CO_MONE_FACT",javaType = MONE_FACT.class,one = @One(select = "com.americatel.facturacion.mappers.mapMONE_FACT.getId"),id = false),
            @Result(property = "peri_fact",column = "CO_PERI_FACT",javaType = PERI_FACT.class,one = @One(select = "com.americatel.facturacion.mappers.mapPERI_FACT.getId"),id = false)
        }
    )  
    public List<PROD> select(@Param("NO_PROD") String NO_PROD);    
    

    @Select("SELECT * FROM TMPROD_PADRE WHERE DESC_PROD_PADRE like '%${NO_PROD}%' AND ST_ELIM=0")
    @Results(value={
            @Result(property = "COD_PROD_PADRE",column="COD_PROD_PADRE",id = true),
            @Result(property = "DESC_PROD_PADRE",column="DESC_PROD_PADRE"),             
            @Result(property = "ST_ELIM",column="ST_ELIM")
        }
    )  
    public List<PROD_PADRE> select_producto_padre(@Param("NO_PROD") String NO_PROD);    
    
    
    @Select("SELECT * FROM TMPROD_PADRE WHERE COD_PROD_PADRE=#{COD_PROD_PADRE} AND ST_ELIM=0;")
    @Results(value={
            @Result(property = "COD_PROD_PADRE",column="COD_PROD_PADRE",id = true),
            @Result(property = "DESC_PROD_PADRE",column="DESC_PROD_PADRE"),             
            @Result(property = "ST_ELIM",column="ST_ELIM")
        }
    )
    public PROD_PADRE getIdProd(@Param("COD_PROD_PADRE") int COD_PROD_PADRE);
    
    
    
    @Select("SELECT P.CO_PROD AS CO_PROD , P.NO_PROD AS NO_PROD  FROM TMPROD P INNER JOIN TMPROD_PADRE_TMPROD PP ON P.CO_PROD = PP.COD_PROD "
    		+ "WHERE PP.COD_PROD_PADRE=#{CO_PROD_PADRE} AND P.ST_ELIM=0 "
    		//+ "union select 0 as CO_PROD , 'TODOS' as NO_PROD from TMPROD" // COMENTAR PARA ELIMINAR TODOS EN LOS COMBOS DE REPORTE
    		+ ";")     
    public List<Map<String, Object>>  selectByProd(@Param("CO_PROD_PADRE") int CO_PROD_PADRE);
    /*@Results(value = {
            @Result(property = "CO_PROD",column="CO_PROD",id = true),
            @Result(property = "NO_PROD",column="NO_PROD")                         
        }
    )*/
    
    
    @Insert("INSERT INTO TMPROD_PADRE_TMPROD (COD_PROD_PADRE,COD_PROD) VALUES ('${COD_PROD_PADRE}','${COD_PROD}')")    
    public void insertRelacionProducto(@Param("COD_PROD_PADRE") int COD_PROD_PADRE ,@Param("COD_PROD") int COD_PROD);
                            
          
    @Select("SELECT FECHA_FIN FROM TMCONFIGURACION_FECHAS WHERE COD_PRODUCTO=#{COD_PRODUCTO};")
    public List<Map<String, Object>>  getFechaByProd(@Param("COD_PRODUCTO") int COD_PRODUCTO);
    
    
    @Select("SELECT TP.COD_PROD_PADRE AS COD_PROD_PADRE, TP.DESC_PROD_PADRE AS DESC_PROD_PADRE, TP.ST_ELIM AS ST_ELIM  FROM TMPROD_PADRE_TMPROD TPP INNER JOIN TMPROD_PADRE TP ON TPP.COD_PROD_PADRE = TP.COD_PROD_PADRE WHERE TPP.COD_PROD = #{CO_PROD};")   
     @Results(value={
            @Result(property = "COD_PROD_PADRE",column="COD_PROD_PADRE",id = true),
            @Result(property = "DESC_PROD_PADRE",column="DESC_PROD_PADRE"),             
            @Result(property = "ST_ELIM",column="ST_ELIM")
        }
    )
    public PROD_PADRE getProductoByNegocio(@Param("CO_PROD") int CO_PROD);
    
    
    @Select("SELECT PP.COD_PROD_PADRE AS COD_PROD_PADRE, PP.DESC_PROD_PADRE AS DESC_PROD_PADRE, PP.ST_ELIM AS ST_ELIM FROM TMNEGO_SUCU_PLAN NSP INNER JOIN TMNEGO_SUCU NS ON NSP.CO_NEGO_SUCU = NS.CO_NEGO_SUCU INNER JOIN TMNEGO N ON N.CO_NEGO = NS.CO_NEGO INNER JOIN tmprod_padre_tmprod PPP ON PPP.cod_prod = N.CO_PROD INNER JOIN tmprod_padre PP ON PP.cod_prod_padre = PPP.cod_prod_padre WHERE NSP.CO_NEGO_SUCU_PLAN = ${CO_NEGO_SUCU_PLAN} AND NS.ST_ELIM = 0;")
    @Results(value={
            @Result(property = "COD_PROD_PADRE",column="COD_PROD_PADRE",id = true),
            @Result(property = "DESC_PROD_PADRE",column="DESC_PROD_PADRE"),             
            @Result(property = "ST_ELIM",column="ST_ELIM")
        }
    )  
    public PROD_PADRE obtenerProductoPadreXCoNegoSucuPlan(@Param("CO_NEGO_SUCU_PLAN") Integer CO_NEGO_SUCU_PLAN);
    
    
    @Select("SELECT PR.* FROM TMPROD PR INNER JOIN TMPROD_PADRE_TMPROD PR_P ON PR.CO_PROD =  PR_P.COD_PROD INNER JOIN TMPROD_PADRE PR_PA ON PR_P.COD_PROD_PADRE = PR_PA.COD_PROD_PADRE WHERE PR.ST_ELIM=0;")    
    public List<PROD> getAllServices();
    
    
    @Select("SELECT PP.* FROM tmprod_padre_tmprod PPP INNER JOIN tmprod_padre PP ON PPP.cod_prod_padre = PP.cod_prod_padre INNER JOIN TMPROD P ON P.CO_PROD = PPP.cod_prod WHERE P.CO_PROD = #{CO_PROD}")    
    public PROD_PADRE obtenerProductoPorServicio(@Param("CO_PROD") int CO_PROD);
    
    @Select("SELECT NO_PROD FROM TMPROD WHERE CO_PROD=#{codServicio} AND ST_ELIM=0")
    public String obtenerNombreServicio(Integer codServicio);
    
    @Select("SELECT TOP 1 DE_NOMB FROM TMCIER_DATA_SERV_SUPL WHERE CO_FACT=#{coFact}")
    public String getNombreServicioSuplementario(Integer coFact);
    	
    	
}
