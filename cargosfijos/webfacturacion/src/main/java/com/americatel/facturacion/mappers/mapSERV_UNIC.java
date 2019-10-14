/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.mappers;

import com.americatel.facturacion.historial.Historial;
import com.americatel.facturacion.models.MONE_FACT;
import com.americatel.facturacion.models.PROD;
import com.americatel.facturacion.models.PROD_PADRE;
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
public interface mapSERV_UNIC {

    @Select("SELECT * FROM TMSERV_UNIC WHERE CO_SERV_UNIC=#{CO_SERV_UNIC} AND ST_ELIM=0;")
    @Results(value={
            @Result(property = "CO_PROD",column="CO_PROD"),            
            @Result(property = "CO_MONE_FACT",column="CO_MONE_FACT"),
            @Result(property = "prod",column = "CO_PROD",javaType = PROD.class,one = @One(select = "com.americatel.facturacion.mappers.mapPROD.getOnlyId"),id = false),
            @Result(property = "mone_fact",column = "CO_MONE_FACT",javaType = MONE_FACT.class,one = @One(select = "com.americatel.facturacion.mappers.mapMONE_FACT.getId"),id = false)
        }
    )    
    public SERV_UNIC getId(@Param("CO_SERV_UNIC") int CO_SERV_UNIC);

    //@Select("SELECT * FROM TMSERV_UNIC WHERE NO_SERV_UNIC like '%${nombre}%' AND ST_ELIM=0;")
    @Select("SELECT * FROM TMSERV_UNIC SER_U INNER JOIN TMPROD PR ON SER_U.CO_PROD = PR.CO_PROD INNER JOIN TMPROD_PADRE_TMPROD PR_P ON PR.CO_PROD =  PR_P.COD_PROD INNER JOIN TMPROD_PADRE PR_PA ON PR_P.COD_PROD_PADRE = PR_PA.COD_PROD_PADRE WHERE NO_SERV_UNIC like '%${nombre}%' AND SER_U.ST_ELIM=0;")
    @Results(value={
            @Result(property = "CO_PROD",column="CO_PROD"),
            @Result(property = "CO_MONE_FACT",column="CO_MONE_FACT"),
            @Result(property = "COD_PROD_PADRE",column="COD_PROD_PADRE"), // CARGOS FIJOS - EMANUEL BARBARAN - EFITEC (SE AGREGO CAMPO)
            @Result(property = "prod_padre",column = "COD_PROD_PADRE",javaType = PROD_PADRE.class,one = @One(select = "com.americatel.facturacion.mappers.mapPROD.getIdProd"),id = false),// CARGOS FIJOS - EMANUEL BARBARAN - EFITEC (SE AGREGO CAMPO)
            @Result(property = "prod",column = "CO_PROD",javaType = PROD.class,one = @One(select = "com.americatel.facturacion.mappers.mapPROD.getOnlyId"),id = false),
            @Result(property = "mone_fact",column = "CO_MONE_FACT",javaType = MONE_FACT.class,one = @One(select = "com.americatel.facturacion.mappers.mapMONE_FACT.getId"),id = false)
        }
    )    
    public List<SERV_UNIC> select(@Param("nombre") String nombre);

    @Insert("INSERT INTO TMSERV_UNIC (CO_PROD,NO_SERV_UNIC,CO_MONE_FACT,IM_MONTO,ST_AFEC_DETR,"+Historial.stringMapperInsertarTitles+") VALUES (#{CO_PROD},#{NO_SERV_UNIC},#{CO_MONE_FACT},#{IM_MONTO},#{ST_AFEC_DETR},"+Historial.stringMapperInsertarValuesNumeral+");")
    public void insert(SERV_UNIC reg);

    @Update("UPDATE TMSERV_UNIC SET  CO_PROD=#{CO_PROD},NO_SERV_UNIC=#{NO_SERV_UNIC},CO_MONE_FACT=#{CO_MONE_FACT},IM_MONTO=#{IM_MONTO},ST_AFEC_DETR=#{ST_AFEC_DETR},"+Historial.stringMapperModificar+" WHERE CO_SERV_UNIC=#{CO_SERV_UNIC} AND ST_ELIM=0;")
    public void update(SERV_UNIC reg);
    
    @Update("UPDATE TMSERV_UNIC SET ST_ELIM=1,"+Historial.stringMapperModificar+" WHERE CO_SERV_UNIC=#{CO_SERV_UNIC} AND ST_ELIM=0;")
    public void delete(SERV_UNIC reg);

    @Select("SELECT * FROM TMSERV_UNIC WHERE CO_PROD=#{CO_PROD} AND CO_MONE_FACT=#{CO_MONE_FACT} AND ST_ELIM=0;")
    @Results(value={
            @Result(property = "CO_PROD",column="CO_PROD"),
            @Result(property = "CO_MONE_FACT",column="CO_MONE_FACT"),        
            @Result(property = "prod",column = "CO_PROD",javaType = PROD.class,one = @One(select = "com.americatel.facturacion.mappers.mapPROD.getOnlyId"),id = false),
            @Result(property = "mone_fact",column = "CO_MONE_FACT",javaType = MONE_FACT.class,one = @One(select = "com.americatel.facturacion.mappers.mapMONE_FACT.getId"),id = false)
        }
    )    
    public List<SERV_UNIC> selectByPROD(@Param("CO_PROD") int CO_PROD,@Param("CO_MONE_FACT") int CO_MONE_FACT);


    
}
