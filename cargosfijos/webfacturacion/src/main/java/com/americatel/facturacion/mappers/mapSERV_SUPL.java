/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.mappers;

import com.americatel.facturacion.historial.Historial;
import com.americatel.facturacion.models.CIER;
import com.americatel.facturacion.models.MONE_FACT;
import com.americatel.facturacion.models.SERV_SUPL;
import com.americatel.facturacion.models.PROD;
import com.americatel.facturacion.models.PROD_PADRE;
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
public interface mapSERV_SUPL {

    @Select("SELECT * FROM TMSERV_SUPL WHERE CO_SERV_SUPL=#{id} AND ST_ELIM=0")
    @Results(value={
            @Result(property = "CO_SERV_SUPL",column="CO_SERV_SUPL",id = true), 
            @Result(property = "CO_PROD",column="CO_PROD"), 
            @Result(property = "CO_MONE_FACT",column="CO_MONE_FACT"), 
            @Result(property = "NO_SERV_SUPL",column="NO_SERV_SUPL"),     
            @Result(property = "IM_MONTO",column="IM_MONTO"), 
            @Result(property = "ST_AFEC_DETR",column="ST_AFEC_DETR"),
            @Result(property = "ST_ELIM",column="ST_ELIM"),
            @Result(property = "prod",column = "CO_PROD",javaType = PROD.class,one = @One(select = "com.americatel.facturacion.mappers.mapPROD.getOnlyId"),id = false),
            @Result(property = "mone_fact",column = "CO_MONE_FACT",javaType = MONE_FACT.class,one = @One(select = "com.americatel.facturacion.mappers.mapMONE_FACT.getId"),id = false)
        }
    )
    public SERV_SUPL getId(int id);
        
    @Update("UPDATE TMSERV_SUPL SET NO_SERV_SUPL='${NO_SERV_SUPL}',CO_PROD='${CO_PROD}',CO_MONE_FACT='${CO_MONE_FACT}',IM_MONTO='${IM_MONTO}',ST_AFEC_DETR='${ST_AFEC_DETR}',"+Historial.stringMapperModificar+" WHERE CO_SERV_SUPL=${CO_SERV_SUPL} AND ST_ELIM=0;")
    public int update(SERV_SUPL prod);

    //@Delete("DELETE FROM TMSERV_SUPL WHERE CO_SERV_SUPL=${CO_SERV_SUPL};")
    @Update("UPDATE TMSERV_SUPL SET ST_ELIM=1,"+Historial.stringMapperModificar+" WHERE CO_SERV_SUPL=${CO_SERV_SUPL};")
    public int delete(SERV_SUPL prod);

    @Insert("INSERT INTO TMSERV_SUPL (NO_SERV_SUPL,CO_PROD,CO_MONE_FACT,IM_MONTO,ST_AFEC_DETR,"+Historial.stringMapperInsertarTitles+") VALUES ('${NO_SERV_SUPL}','${CO_PROD}','${CO_MONE_FACT}','${IM_MONTO}','${ST_AFEC_DETR}',"+Historial.stringMapperInsertarValuesNumeral+")")
    public void insert(SERV_SUPL prod);

    //@Select("SELECT * FROM TMSERV_SUPL WHERE CO_SERV_SUPL like '%${CO_SERV_SUPL}%' AND ST_ELIM=0")
    @Select("SELECT * FROM TMSERV_SUPL SER_S INNER JOIN TMPROD PR ON SER_S.CO_PROD = PR.CO_PROD INNER JOIN TMPROD_PADRE_TMPROD PR_P ON PR.CO_PROD =  PR_P.COD_PROD INNER JOIN TMPROD_PADRE PR_PA ON PR_P.COD_PROD_PADRE = PR_PA.COD_PROD_PADRE WHERE CO_SERV_SUPL like '%${CO_SERV_SUPL}%' AND SER_S.ST_ELIM=0")
    @Results(value={
            @Result(property = "CO_SERV_SUPL",column="CO_SERV_SUPL",id = true), 
            @Result(property = "CO_PROD",column="CO_PROD"), 
            @Result(property = "COD_PROD_PADRE",column="COD_PROD_PADRE"), // CARGOS FIJOS - EMANUEL BARBARAN - EFITEC (SE AGREGO CAMPO)
            @Result(property = "CO_MONE_FACT",column="CO_MONE_FACT"), 
            @Result(property = "NO_SERV_SUPL",column="NO_SERV_SUPL"),     
            @Result(property = "IM_MONTO",column="IM_MONTO"), 
            @Result(property = "ST_AFEC_DETR",column="ST_AFEC_DETR"),
            @Result(property = "ST_ELIM",column="ST_ELIM"),
            @Result(property = "prod_padre",column = "COD_PROD_PADRE",javaType = PROD_PADRE.class,one = @One(select = "com.americatel.facturacion.mappers.mapPROD.getIdProd"),id = false),// CARGOS FIJOS - EMANUEL BARBARAN - EFITEC (SE AGREGO CAMPO)
            @Result(property = "prod",column = "CO_PROD",javaType = PROD.class,one = @One(select = "com.americatel.facturacion.mappers.mapPROD.getOnlyId"),id = false),
            @Result(property = "mone_fact",column = "CO_MONE_FACT",javaType = MONE_FACT.class,one = @One(select = "com.americatel.facturacion.mappers.mapMONE_FACT.getId"),id = false)
        }
    )  
    public List<SERV_SUPL> select(@Param("CO_SERV_SUPL") String CO_SERV_SUPL);    

    
    @Select("SELECT * FROM TMSERV_SUPL WHERE CO_PROD=#{CO_PROD} AND CO_MONE_FACT=#{CO_MONE_FACT} AND ST_ELIM=0")
    @Results(value={
            @Result(property = "CO_SERV_SUPL",column="CO_SERV_SUPL",id = true), 
            @Result(property = "CO_PROD",column="CO_PROD"), 
            @Result(property = "CO_MONE_FACT",column="CO_MONE_FACT"), 
            @Result(property = "NO_SERV_SUPL",column="NO_SERV_SUPL"),     
            @Result(property = "IM_MONTO",column="IM_MONTO"), 
            @Result(property = "ST_AFEC_DETR",column="ST_AFEC_DETR"),
            @Result(property = "ST_ELIM",column="ST_ELIM"),
            @Result(property = "mone_fact",column = "CO_MONE_FACT",javaType = MONE_FACT.class,one = @One(select = "com.americatel.facturacion.mappers.mapMONE_FACT.getId"),id = false)
        }
    )  
    public List<SERV_SUPL> selectByPROD(@Param("CO_PROD") int CO_PROD,@Param("CO_MONE_FACT") int CO_MONE_FACT);
    
    
    @Select("SELECT TOP 1 NU_TIPO_CAMB FROM TMCIER WHERE CO_PROD = 2 AND ST_CIER <> 4 AND ST_ELIM = 0 ORDER BY CO_CIER DESC;")
    public CIER getTipoCambio();
}
