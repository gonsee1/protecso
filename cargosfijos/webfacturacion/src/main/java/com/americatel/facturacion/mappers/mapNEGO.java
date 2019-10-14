/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.mappers;

import com.americatel.facturacion.fncs.Pagination;
import com.americatel.facturacion.historial.Historial;
import com.americatel.facturacion.models.CIER;
import com.americatel.facturacion.models.CLIE;
import com.americatel.facturacion.models.MONE_FACT;
import com.americatel.facturacion.models.MOTI_DESC;
import com.americatel.facturacion.models.PERI_FACT;
import com.americatel.facturacion.models.NEGO;
import com.americatel.facturacion.models.PROD;
import com.americatel.facturacion.models.PROD_PADRE;
import com.americatel.facturacion.models.SUCU;
import com.americatel.facturacion.models.TIPO_FACT;
import java.util.Date;
import java.util.List;
import java.util.Map;
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
public interface mapNEGO {
    //@Select("SELECT * FROM TMNEGO WHERE CO_NEGO=#{id} AND ST_ELIM=0;")
    @Select("SELECT * FROM TMNEGO N INNER JOIN TMPROD PR ON N.CO_PROD = PR.CO_PROD INNER JOIN TMPROD_PADRE_TMPROD PR_P ON PR.CO_PROD =  PR_P.COD_PROD INNER JOIN TMPROD_PADRE PR_PA ON PR_P.COD_PROD_PADRE = PR_PA.COD_PROD_PADRE  WHERE CO_NEGO=#{id} AND N.ST_ELIM=0;")  
    @Results(value={
            @Result(property = "CO_NEGO",column="CO_NEGO",id = true),
            @Result(property = "CO_SUCU_CORR",column="CO_SUCU_CORR"),
            @Result(property = "CO_CLIE",column="CO_CLIE"), 
            @Result(property = "CO_PROD",column="CO_PROD"), 
            @Result(property = "CO_TIPO_FACT",column="CO_TIPO_FACT"),     
            @Result(property = "CO_MONE_FACT",column="CO_MONE_FACT"),     
            @Result(property = "CO_PERI_FACT",column="CO_PERI_FACT"), 
            @Result(property = "CO_NEGO_ORIG",column="CO_NEGO_ORIG"), 
            @Result(property = "REFERENCIA",column="REFERENCIA"), 
            @Result(property = "ST_ELIM",column="ST_ELIM"),
            @Result(property = "clie",column = "CO_CLIE",javaType = CLIE.class,one = @One(select = "com.americatel.facturacion.mappers.mapCLIE.getId"),id = false),
            @Result(property = "prod",column = "CO_PROD",javaType = PROD.class,one = @One(select = "com.americatel.facturacion.mappers.mapPROD.getOnlyId"),id = false),            
            @Result(property = "prod_padre",column = "COD_PROD_PADRE",javaType = PROD_PADRE.class,one = @One(select = "com.americatel.facturacion.mappers.mapPROD.getIdProd"),id = false),// CARGOS FIJOS - EMANUEL BARBARAN - EFITEC (SE AGREGO CAMPO)
            @Result(property = "tipo_fact",column = "CO_TIPO_FACT",javaType = TIPO_FACT.class,one = @One(select = "com.americatel.facturacion.mappers.mapTIPO_FACT.getId"),id = false),
            @Result(property = "mone_fact",column = "CO_MONE_FACT",javaType = MONE_FACT.class,one = @One(select = "com.americatel.facturacion.mappers.mapMONE_FACT.getId"),id = false),
            @Result(property = "peri_fact",column = "CO_PERI_FACT",javaType = PERI_FACT.class,one = @One(select = "com.americatel.facturacion.mappers.mapPERI_FACT.getId"),id = false),
            @Result(property = "sucu_corr",column = "CO_SUCU_CORR",javaType = SUCU.class,one = @One(select = "com.americatel.facturacion.mappers.mapSUCU.getId"),id = false),            
            @Result(property = "isCortado",column = "CO_NEGO",javaType = Boolean.class,one = @One(select = "com.americatel.facturacion.mappers.mapCORT.isCortado"),id = false),
            @Result(property = "isDesactivado",column = "CO_NEGO",javaType = Boolean.class,one = @One(select = "com.americatel.facturacion.mappers.mapNEGO_SUCU.isNegoDesactivado"),id = false),
            @Result(property = "isPendiente",column = "CO_NEGO",javaType = Boolean.class,one = @One(select = "com.americatel.facturacion.mappers.mapNEGO_SUCU.isNegoPendiente"),id = false)
        }
    )
    public NEGO getId(int id);
    
    @Select("SELECT * FROM TMNEGO WHERE CO_NEGO=#{id} AND ST_ELIM=0;")
    @Results(value={
            @Result(property = "CO_NEGO",column="CO_NEGO",id = true),
            @Result(property = "CO_CLIE",column="CO_CLIE"), 
            @Result(property = "CO_PROD",column="CO_PROD"), 
            @Result(property = "CO_MONE_FACT",column="CO_MONE_FACT"),  
            @Result(property = "CO_NEGO_ORIG",column="CO_NEGO_ORIG"), 
            @Result(property = "ST_ELIM",column="ST_ELIM"),
            @Result(property = "clie",column = "CO_CLIE",javaType = CLIE.class,one = @One(select = "com.americatel.facturacion.mappers.mapCLIE.getId"),id = false),
            @Result(property = "prod",column = "CO_PROD",javaType = PROD.class,one = @One(select = "com.americatel.facturacion.mappers.mapPROD.getOnlyId"),id = false),            
            @Result(property = "mone_fact",column = "CO_MONE_FACT",javaType = MONE_FACT.class,one = @One(select = "com.americatel.facturacion.mappers.mapMONE_FACT.getId"),id = false),
            @Result(property = "isDesactivado",column = "CO_NEGO",javaType = Boolean.class,one = @One(select = "com.americatel.facturacion.mappers.mapNEGO_SUCU.isNegoDesactivado"),id = false),
            @Result(property = "isArrendamiento",column = "CO_NEGO",javaType = NEGO.class,one = @One(select = "com.americatel.facturacion.mappers.mapNEGO.isArrendamiento"),id = false)
        }
    )
    public NEGO getIdClientAndProduct(int id);
    
    @Select("SELECT * FROM TMNEGO WHERE CO_NEGO=#{id} AND ST_ELIM=0;")
    @Results(value={
            @Result(property = "CO_NEGO",column="CO_NEGO",id = true),
            @Result(property = "CO_SUCU_CORR",column="CO_SUCU_CORR"),
            @Result(property = "CO_CLIE",column="CO_CLIE"), 
            @Result(property = "CO_PROD",column="CO_PROD"), 
            @Result(property = "CO_TIPO_FACT",column="CO_TIPO_FACT"),     
            @Result(property = "CO_MONE_FACT",column="CO_MONE_FACT"),     
            @Result(property = "CO_PERI_FACT",column="CO_PERI_FACT"),
            @Result(property = "CO_NEGO_ORIG",column="CO_NEGO_ORIG"), 
            @Result(property = "ST_ELIM",column="ST_ELIM")
            //@Result(property = "clie",column = "CO_CLIE",javaType = TIPO_FACT.class,one = @One(select = "com.americatel.facturacion.mappers.mapCLIE.getId"),id = false),
            //@Result(property = "prod",column = "CO_PROD",javaType = PROD.class,one = @One(select = "com.americatel.facturacion.mappers.mapPROD.getOnlyId"),id = false),            
            //@Result(property = "tipo_fact",column = "CO_TIPO_FACT",javaType = TIPO_FACT.class,one = @One(select = "com.americatel.facturacion.mappers.mapTIPO_FACT.getId"),id = false),
            //@Result(property = "mone_fact",column = "CO_MONE_FACT",javaType = MONE_FACT.class,one = @One(select = "com.americatel.facturacion.mappers.mapMONE_FACT.getId"),id = false),
            //@Result(property = "peri_fact",column = "CO_PERI_FACT",javaType = PERI_FACT.class,one = @One(select = "com.americatel.facturacion.mappers.mapPERI_FACT.getId"),id = false)
        }
    )
    public NEGO getOnlyId(int id); 
    
    
    @Select("SELECT * FROM TMNEGO WHERE ST_ELIM=0;")
    public List<NEGO> getAll();
    
    @Update("UPDATE TMNEGO SET CO_CLIE='${CO_CLIE}',CO_SUCU_CORR='${CO_SUCU_CORR}',CO_PROD='${CO_PROD}',CO_TIPO_FACT='${CO_TIPO_FACT}',CO_MONE_FACT='${CO_MONE_FACT}',CO_PERI_FACT='${CO_PERI_FACT}',CO_NEGO_ORIG='${CO_NEGO_ORIG}',"+Historial.stringMapperModificar+" WHERE CO_NEGO=${CO_NEGO} AND ST_ELIM=0;")
    public int update(NEGO prod);
    
    @Update("UPDATE TMNEGO SET CO_CLIE='${CO_CLIE}',CO_PROD='${CO_PROD}',CO_TIPO_FACT='${CO_TIPO_FACT}',CO_MONE_FACT='${CO_MONE_FACT}',CO_PERI_FACT='${CO_PERI_FACT}',CO_NEGO_ORIG='${CO_NEGO_ORIG}',"+Historial.stringMapperModificar+" WHERE CO_NEGO=${CO_NEGO} AND ST_ELIM=0;")
    public int update2(NEGO prod);//sin sucursal
    
    //@Delete("DELETE FROM TMNEGO WHERE CO_NEGO=${CO_NEGO};")
    @Update("UPDATE TMNEGO SET ST_ELIM=1,"+Historial.stringMapperModificar+" WHERE CO_NEGO=${CO_NEGO};")
    public int delete(NEGO prod);

    @Insert("INSERT INTO TMNEGO (CO_NEGO,CO_SUCU_CORR,CO_CLIE,CO_PROD,CO_TIPO_FACT,CO_MONE_FACT,CO_PERI_FACT,CO_NEGO_ORIG,REFERENCIA,"+Historial.stringMapperInsertarTitles+") VALUES ('${CO_NEGO}','${CO_SUCU_CORR}','${CO_CLIE}','${CO_PROD}','${CO_TIPO_FACT}','${CO_MONE_FACT}','${CO_PERI_FACT}','${CO_NEGO_ORIG}','${REFERENCIA}',"+Historial.stringMapperInsertarValuesNumeral+")")
    public void insert(NEGO prod);

    //@Select("SELECT * FROM TMNEGO WHERE CO_NEGO like '%${CO_NEGO}%' AND ST_ELIM=0 ORDER BY [CO_NEGO] OFFSET ${Pagination.getGroup()} ROWS FETCH NEXT #{Pagination.Limit} ROWS ONLY")
    @Select("SELECT * FROM TMNEGO NEGO  INNER JOIN TMPROD PR ON NEGO.CO_PROD = PR.CO_PROD INNER JOIN TMPROD_PADRE_TMPROD PR_P ON PR.CO_PROD =  PR_P.COD_PROD INNER JOIN TMPROD_PADRE PR_PA ON PR_P.COD_PROD_PADRE = PR_PA.COD_PROD_PADRE  WHERE CO_NEGO like '%${CO_NEGO}%' AND NEGO.ST_ELIM=0 ORDER BY [CO_NEGO] OFFSET ${Pagination.getGroup()} ROWS FETCH NEXT #{Pagination.Limit} ROWS ONLY")    
    @Results(value={
            @Result(property = "CO_NEGO",column="CO_NEGO",id = true),
            @Result(property = "CO_CLIE",column="CO_CLIE"), 
            @Result(property = "CO_PROD",column="CO_PROD"), 
            @Result(property = "CO_TIPO_FACT",column="CO_TIPO_FACT"), 
            @Result(property = "COD_PROD_PADRE",column="COD_PROD_PADRE"), // CARGOS FIJOS - EMANUEL BARBARAN - EFITEC (SE AGREGO CAMPO)
            @Result(property = "CO_MONE_FACT",column="CO_MONE_FACT"),     
            @Result(property = "CO_PERI_FACT",column="CO_PERI_FACT"),
            @Result(property = "CO_NEGO_ORIG",column="CO_NEGO_ORIG"),
            @Result(property = "ST_ELIM",column="ST_ELIM"),            
            @Result(property = "prod_padre",column = "COD_PROD_PADRE",javaType = PROD_PADRE.class,one = @One(select = "com.americatel.facturacion.mappers.mapPROD.getIdProd"),id = false),// CARGOS FIJOS - EMANUEL BARBARAN - EFITEC (SE AGREGO CAMPO)
            @Result(property = "clie",column = "CO_CLIE",javaType = TIPO_FACT.class,one = @One(select = "com.americatel.facturacion.mappers.mapCLIE.getId"),id = false),
            @Result(property = "prod",column = "CO_PROD",javaType = PROD.class,one = @One(select = "com.americatel.facturacion.mappers.mapPROD.getOnlyId"),id = false),            
            @Result(property = "tipo_fact",column = "CO_TIPO_FACT",javaType = TIPO_FACT.class,one = @One(select = "com.americatel.facturacion.mappers.mapTIPO_FACT.getId"),id = false),
            @Result(property = "mone_fact",column = "CO_MONE_FACT",javaType = MONE_FACT.class,one = @One(select = "com.americatel.facturacion.mappers.mapMONE_FACT.getId"),id = false),
            @Result(property = "peri_fact",column = "CO_PERI_FACT",javaType = PERI_FACT.class,one = @One(select = "com.americatel.facturacion.mappers.mapPERI_FACT.getId"),id = false),
            @Result(property = "sucu_corr",column = "CO_SUCU_CORR",javaType = SUCU.class,one = @One(select = "com.americatel.facturacion.mappers.mapSUCU.getId"),id = false),
            @Result(property = "isCortado",column = "CO_NEGO",javaType = Boolean.class,one = @One(select = "com.americatel.facturacion.mappers.mapCORT.isCortado"),id = false),
            @Result(property = "isDesactivado",column = "CO_NEGO",javaType = Boolean.class,one = @One(select = "com.americatel.facturacion.mappers.mapNEGO_SUCU.isNegoDesactivado"),id = false),
            @Result(property = "isSuspendido",column = "CO_NEGO",javaType = Boolean.class,one = @One(select = "com.americatel.facturacion.mappers.mapNEGO_SUCU.isNegoSuspendido"),id = false),
            @Result(property = "isPendiente",column = "CO_NEGO",javaType = Boolean.class,one = @One(select = "com.americatel.facturacion.mappers.mapNEGO_SUCU.isNegoPendiente"),id = false)
        }
    )  
    public List<NEGO> select(@Param("CO_NEGO") String CO_NEGO,@Param("Pagination")Pagination pagination);    

    @Select("SELECT count(*) FROM TMNEGO WHERE CO_NEGO like '%${CO_NEGO}%' AND ST_ELIM=0")  
    public int getNumResultsSelect(@Param("CO_NEGO") String CO_NEGO);    
    
    @Select("SELECT CAST(" +
        " CASE WHEN count(*) > 0 THEN 1" +
        " ELSE 0" +
        " END AS bit) AS EXIST FROM TMNEGO WHERE CO_NEGO = #{CO_NEGO} AND ST_ELIM=0")  
    public boolean existNegocio(@Param("CO_NEGO") int CO_NEGO); 
    
    @Select("SELECT CAST(" +
        "   CASE WHEN count(*) > 0 THEN 1" +
        "   ELSE 0" +
        "   END AS bit) AS EXIST FROM TMNEGO WHERE CO_NEGO = #{CO_NEGO}" +
        "   AND CO_NEGO IN (SELECT CO_NEGO FROM TMNEGO_SUCU S" +
        "   INNER JOIN TMMOTI_DESC M ON S.CO_MOTI_DESC=M.CO_MOTI_DESC" +
        "   WHERE M.NO_MOTI_DESC='CAMBIO DE MONEDA')" +
        "   AND ST_ELIM=0")  
    public boolean existNegocioOrig(@Param("CO_NEGO") int CO_NEGO);

    //@Select("SELECT * FROM TMNEGO a INNER JOIN TMCLIE b ON b.CO_CLIE=a.CO_CLIE WHERE (a.CO_NEGO like '%${SEARCH}%' OR b.NO_CLIE like '%${SEARCH}%' OR b.AP_CLIE like '%${SEARCH}%' OR b.NO_RAZO like '%${SEARCH}%') AND a.ST_ELIM=0 ORDER BY [CO_NEGO] OFFSET ${Pagination.getGroup()} ROWS FETCH NEXT #{Pagination.Limit} ROWS ONLY")
    @Select("SELECT * FROM TMNEGO a INNER JOIN TMCLIE b  ON b.CO_CLIE=a.CO_CLIE INNER JOIN TMPROD PR ON a.CO_PROD = PR.CO_PROD INNER JOIN TMPROD_PADRE_TMPROD PR_P ON PR.CO_PROD =  PR_P.COD_PROD INNER JOIN TMPROD_PADRE PR_PA ON PR_P.COD_PROD_PADRE = PR_PA.COD_PROD_PADRE   WHERE (a.CO_NEGO like '%${SEARCH}%' OR b.NO_CLIE like '%${SEARCH}%' OR b.AP_CLIE like '%${SEARCH}%' OR b.NO_RAZO like '%${SEARCH}%') AND a.ST_ELIM=0 ORDER BY [CO_NEGO] OFFSET ${Pagination.getGroup()} ROWS FETCH NEXT #{Pagination.Limit} ROWS ONLY")
    @Results(value={
            @Result(property = "CO_NEGO",column="CO_NEGO",id = true),
            @Result(property = "CO_CLIE",column="CO_CLIE"), 
            @Result(property = "CO_PROD",column="CO_PROD"), 
            @Result(property = "CO_TIPO_FACT",column="CO_TIPO_FACT"),  
            @Result(property = "COD_PROD_PADRE",column="COD_PROD_PADRE"), // CARGOS FIJOS - EMANUEL BARBARAN - EFITEC (SE AGREGO CAMPO)
            @Result(property = "CO_MONE_FACT",column="CO_MONE_FACT"),     
            @Result(property = "CO_PERI_FACT",column="CO_PERI_FACT"),
            @Result(property = "CO_NEGO_ORIG",column="CO_NEGO_ORIG"),
            @Result(property = "ST_ELIM",column="ST_ELIM"),
            @Result(property = "prod_padre",column = "COD_PROD_PADRE",javaType = PROD_PADRE.class,one = @One(select = "com.americatel.facturacion.mappers.mapPROD.getIdProd"),id = false),// CARGOS FIJOS - EMANUEL BARBARAN - EFITEC (SE AGREGO CAMPO)
            @Result(property = "clie",column = "CO_CLIE",javaType = TIPO_FACT.class,one = @One(select = "com.americatel.facturacion.mappers.mapCLIE.getId"),id = false),
            @Result(property = "prod",column = "CO_PROD",javaType = PROD.class,one = @One(select = "com.americatel.facturacion.mappers.mapPROD.getOnlyId"),id = false),            
            @Result(property = "tipo_fact",column = "CO_TIPO_FACT",javaType = TIPO_FACT.class,one = @One(select = "com.americatel.facturacion.mappers.mapTIPO_FACT.getId"),id = false),
            @Result(property = "mone_fact",column = "CO_MONE_FACT",javaType = MONE_FACT.class,one = @One(select = "com.americatel.facturacion.mappers.mapMONE_FACT.getId"),id = false),
            @Result(property = "peri_fact",column = "CO_PERI_FACT",javaType = PERI_FACT.class,one = @One(select = "com.americatel.facturacion.mappers.mapPERI_FACT.getId"),id = false),
            @Result(property = "sucu_corr",column = "CO_SUCU_CORR",javaType = SUCU.class,one = @One(select = "com.americatel.facturacion.mappers.mapSUCU.getId"),id = false),
            @Result(property = "isCortado",column = "CO_NEGO",javaType = Boolean.class,one = @One(select = "com.americatel.facturacion.mappers.mapCORT.isCortado"),id = false),
            @Result(property = "isDesactivado",column = "CO_NEGO",javaType = Boolean.class,one = @One(select = "com.americatel.facturacion.mappers.mapNEGO_SUCU.isNegoDesactivado"),id = false),
            @Result(property = "isSuspendido",column = "CO_NEGO",javaType = Boolean.class,one = @One(select = "com.americatel.facturacion.mappers.mapNEGO_SUCU.isNegoSuspendido"),id = false),
            @Result(property = "isPendiente",column = "CO_NEGO",javaType = Boolean.class,one = @One(select = "com.americatel.facturacion.mappers.mapNEGO_SUCU.isNegoPendiente"),id = false)
        }
    )     
    public List<NEGO> selectBySEARCH(@Param("SEARCH") String SEARCH,@Param("Pagination")Pagination pagination);
    
    @Select("SELECT count(*) FROM TMNEGO a INNER JOIN TMCLIE b ON b.CO_CLIE=a.CO_CLIE WHERE (a.CO_NEGO like '%${SEARCH}%' OR b.NO_CLIE like '%${SEARCH}%' OR b.AP_CLIE like '%${SEARCH}%' OR b.NO_RAZO like '%${SEARCH}%') AND a.ST_ELIM=0")
    public int getNumResultsBySEARCH(@Param("SEARCH") String SEARCH);
    
    
    @Select("SELECT * FROM TMCLIE WHERE CO_CLIE=#{CO_CLIE} AND ST_ELIM=0")
    public CLIE getCliente(NEGO nego);    

    @Select("SELECT * FROM TMMONE_FACT WHERE CO_MONE_FACT=#{CO_MONE_FACT} AND ST_ELIM=0")
    public MONE_FACT getMonedaFacturacion(NEGO nego);

    
    @Select("SELECT TOP 1 ISNULL(IM_MONT,0) FROM TMNEGO_SALD WHERE CO_NEGO=#{NEGO.CO_NEGO} AND CO_CIER<>#{CIER.CO_CIER} AND ST_ELIM=0 ORDER BY CO_NEGO_SALD DESC")
    public Double getSaldoNegocio(@Param("NEGO") NEGO nego,@Param("CIER") CIER cier);

    /*
    @Select("SELECT p.NO_PLAN as NOMB,count(*) as CANT,SUM(p.IM_MONTO) AS MONT FROM TMNEGO n "
            + "INNER JOIN TMNEGO_SUCU s on s.CO_NEGO=n.CO_NEGO "
            + "INNER JOIN TMNEGO_SUCU_PLAN p on p.CO_NEGO_SUCU=s.CO_NEGO_SUCU "
            + "WHERE p.FE_FIN is null AND n.CO_NEGO=#{CO_NEGO}"
            + "GROUP BY p.NO_PLAN UNION "
            + "SELECT ss.NO_SERV_SUPL as NOMB,count(*) as CANT,SUM(ss.IM_MONTO) AS MONT FROM TMNEGO n "
            + "INNER JOIN TMNEGO_SUCU s on s.CO_NEGO=n.CO_NEGO "
            + "INNER JOIN TMNEGO_SUCU_SERV_SUPL ss on ss.CO_NEGO_SUCU=s.CO_NEGO_SUCU "
            + "WHERE ss.FE_FIN is null AND n.CO_NEGO=#{CO_NEGO}"
            + "GROUP BY ss.NO_SERV_SUPL")*/
    @Select("SELECT sp.NO_PLAN as NOMB,count(*) as CANT,SUM(sp.IM_MONTO) AS MONT,f.NO_MONE_FACT AS MONEDA " +
        "FROM TMNEGO n " +
        "INNER JOIN TMNEGO_SUCU s on s.CO_NEGO=n.CO_NEGO " +
        "INNER JOIN TMNEGO_SUCU_PLAN sp on sp.CO_NEGO_SUCU=s.CO_NEGO_SUCU " +
        "INNER JOIN TMPLAN p on p.CO_PLAN=sp.CO_PLAN " +
        "INNER JOIN TMMONE_FACT f on f.CO_MONE_FACT=p.CO_MONE_FACT " +
        "WHERE sp.FE_FIN is null AND n.CO_NEGO=#{CO_NEGO} AND sp.ST_ELIM=0 " +
        "GROUP BY sp.NO_PLAN, f.NO_MONE_FACT " +
        "UNION " +
        "SELECT ss.NO_SERV_SUPL as NOMB,count(*) as CANT,SUM(ss.IM_MONTO) AS MONT,f.NO_MONE_FACT AS MONEDA " +
        "FROM TMNEGO n " +
        "INNER JOIN TMNEGO_SUCU s on s.CO_NEGO=n.CO_NEGO " +
        "INNER JOIN TMNEGO_SUCU_SERV_SUPL ss on ss.CO_NEGO_SUCU=s.CO_NEGO_SUCU " +
        "INNER JOIN TMSERV_SUPL su on su.CO_SERV_SUPL=ss.CO_SERV_SUPL " +
        "INNER JOIN TMMONE_FACT f on f.CO_MONE_FACT=su.CO_MONE_FACT " +
        "WHERE ss.FE_FIN is null AND n.CO_NEGO=#{CO_NEGO} AND ss.ST_ELIM=0 " +
        "GROUP BY ss.NO_SERV_SUPL,f.NO_MONE_FACT")
    public List<Map<String, Object>> getResumen(@Param("CO_NEGO") Integer CO_NEGO);
    
    
    @Select("SELECT MIN(nsp.FE_ULTI_FACT) FROM TMNEGO n "
            + "INNER JOIN TMNEGO_SUCU ns ON ns.CO_NEGO=n.CO_NEGO "
            + "INNER JOIN TMNEGO_SUCU_PLAN nsp ON nsp.CO_NEGO_SUCU=ns.CO_NEGO_SUCU "
            + "WHERE n.CO_NEGO=#{CO_NEGO} AND nsp.FE_FIN IS NULL AND nsp.ST_ELIM=0 "
            + "UNION "
            + "SELECT MIN(nss.FE_ULTI_FACT) FROM TMNEGO n "
            + "INNER JOIN TMNEGO_SUCU ns ON ns.CO_NEGO=n.CO_NEGO "            
            + "INNER JOIN TMNEGO_SUCU_SERV_SUPL nss ON nss.CO_NEGO_SUCU=ns.CO_NEGO_SUCU "
            + "WHERE n.CO_NEGO=#{CO_NEGO} AND nss.FE_FIN IS NULL AND nss.ST_ELIM=0")
    public List<Date> getFechaUltimaFacturacionMenor(NEGO aThis);
    
    @Select("SELECT MIN(nsp.FE_INIC) FROM TMNEGO n "
            + "INNER JOIN TMNEGO_SUCU ns ON ns.CO_NEGO=n.CO_NEGO "
            + "INNER JOIN TMNEGO_SUCU_PLAN nsp ON nsp.CO_NEGO_SUCU=ns.CO_NEGO_SUCU "
            + "WHERE n.CO_NEGO=#{CO_NEGO} AND nsp.FE_ULTI_FACT IS NULL AND nsp.FE_FIN IS NULL AND nsp.ST_ELIM=0 "
            + "UNION "
            + "SELECT MIN(nss.FE_INIC) FROM TMNEGO n "
            + "INNER JOIN TMNEGO_SUCU ns ON ns.CO_NEGO=n.CO_NEGO "            
            + "INNER JOIN TMNEGO_SUCU_SERV_SUPL nss ON nss.CO_NEGO_SUCU=ns.CO_NEGO_SUCU "
            + "WHERE n.CO_NEGO=#{CO_NEGO} AND nss.FE_ULTI_FACT IS NULL AND nss.FE_FIN IS NULL AND nss.ST_ELIM=0 ")    
    public List<Date> getFechaActivacionSinFacturarMenor(NEGO aThis);//obtiene la menores fechas de activacion sin facturar
    
    
    
    
    /*@Select("SELECT CO_NEGO FROM TMNEGO WHERE CO_NEGO NOT IN ( "
            + "SELECT S.CO_NEGO FROM TMNEGO_SUCU S "
            + "INNER JOIN TMNEGO_SUCU_PLAN P ON S.CO_NEGO_SUCU=P.CO_NEGO_SUCU "
            + "WHERE S.ST_ELIM=0 AND P.ST_ELIM=0 GROUP BY S.CO_NEGO) "
            + "AND CO_NEGO NOT IN (SELECT CO_NEGO_ORIG FROM TMMIGR_NEGO_SUCU) " 
            + "AND ST_ELIM=0")*/
    @Select("SELECT N.CO_NEGO AS CO_NEGO,PR_PA.DESC_PROD_PADRE AS DESC_PROD_PADRE  FROM TMNEGO N INNER JOIN TMPROD PR ON N.CO_PROD = PR.CO_PROD "
            + "INNER JOIN TMPROD_PADRE_TMPROD PR_P ON PR.CO_PROD =  PR_P.COD_PROD "
	    + "INNER JOIN TMPROD_PADRE PR_PA ON PR_P.COD_PROD_PADRE = PR_PA.COD_PROD_PADRE  WHERE CO_NEGO NOT IN ( "
	    + "SELECT S.CO_NEGO FROM TMNEGO_SUCU S "
	    + "INNER JOIN TMNEGO_SUCU_PLAN P ON S.CO_NEGO_SUCU=P.CO_NEGO_SUCU "
	    + "WHERE S.ST_ELIM=0 AND P.ST_ELIM=0 GROUP BY S.CO_NEGO) "
	    + "AND CO_NEGO NOT IN (SELECT CO_NEGO_ORIG FROM TMMIGR_NEGO_SUCU) "
	    + "AND N.ST_ELIM=0 ORDER BY PR_PA.DESC_PROD_PADRE")
    public List<NEGO> selectNegociosPendientes();
    
    @Select("SELECT CAST(CASE WHEN COUNT(CO_NEGO)>0 THEN 1 ELSE 0 END AS bit) AS EXIST "
            + "FROM TMNEGO WHERE CO_NEGO NOT IN ( "
            + "SELECT S.CO_NEGO FROM TMNEGO_SUCU S "
            + "INNER JOIN TMNEGO_SUCU_PLAN P ON S.CO_NEGO_SUCU=P.CO_NEGO_SUCU "
            + "WHERE S.ST_ELIM=0 AND P.ST_ELIM=0 GROUP BY S.CO_NEGO) "
            + "AND CO_NEGO NOT IN (SELECT CO_NEGO_ORIG FROM TMMIGR_NEGO_SUCU) "
            + "AND ST_ELIM=0")
    public boolean existenNegociosPendientes();
            
    @Select("SELECT CAST(CASE WHEN COUNT(TN.CO_NEGO)>0 THEN 1 ELSE 0 END AS bit) AS EXIST "
            + "FROM TMNEGO TN "
            + "INNER JOIN TMPROD_PADRE_TMPROD TPP ON TN.CO_PROD = TPP.COD_PROD "
	    + "INNER JOIN TMPROD_PADRE TP ON TPP.COD_PROD_PADRE = TP.COD_PROD_PADRE "
	    + "WHERE TN.CO_NEGO NOT IN ( "
            + "SELECT S.CO_NEGO FROM TMNEGO_SUCU S " 
            + "INNER JOIN TMNEGO_SUCU_PLAN P ON S.CO_NEGO_SUCU=P.CO_NEGO_SUCU "
            + "WHERE S.ST_ELIM=0 AND P.ST_ELIM=0 GROUP BY S.CO_NEGO) "
            + "AND TN.CO_NEGO NOT IN (SELECT CO_NEGO_ORIG FROM TMMIGR_NEGO_SUCU) "
            + "AND TN.ST_ELIM=0 "
	    + "AND TPP.COD_PROD_PADRE = #{CO_PROD}")    
    public boolean existenNegociosPendientesByProducto(@Param("CO_PROD") Integer CO_PROD);
    
    
    @Select(
    		
    		
    		
    		//"SELECT TOP 1"
    		//+ "  CAST("
    		//+ "	  CASE"
    		//+ "		WHEN NSP.FE_ULTI_FACT IS NULL THEN 1"
    		//+ "		WHEN count(*) < 2 THEN 1"
    		//+ "		ELSE 0"
    		//+ "	  END  AS BIT) AS ESNUEVO"
    		//+ " FROM TMNEGO_SUCU NS INNER JOIN TMNEGO_SUCU_PLAN NSP ON NS.CO_NEGO_SUCU = NSP.CO_NEGO_SUCU"
    		//+ " WHERE NS.CO_NEGO = #{CO_NEGO}"
    		
    		"SELECT TOP 1"
    		+ "  CAST("
    		+ "	  CASE"
    		//+ "		WHEN NSP.FE_ULTI_FACT IS NULL THEN 1"
    		+ "		WHEN count(*) < 1 THEN 1"
    		+ "		ELSE 0"
    		+ "	  END  AS BIT) AS ESNUEVO"
    		+ " FROM tmcier_data_nego cdn"
    		+ " inner join tmcier_data_sucu cds on cdn.CO_CIER_DATA_NEGO = cds.CO_CIER_DATA_NEGO"
    		+ " inner join tmcier c on cdn.co_cier = c.co_cier"
    		+ " WHERE cdn.CO_NEGO = #{CO_NEGO}"
    		+ " and c.st_cier = 4"
    		)
    public boolean isNuevo(@Param("CO_NEGO") Integer CO_NEGO);
    
    @Select("SELECT COUNT(*) FROM TMNEGO_SUCU s " +
    "   INNER JOIN TMNEGO_SUCU_PLAN sp ON s.CO_NEGO_SUCU=sp.CO_NEGO_SUCU " +
    "   WHERE sp.NO_PLAN LIKE '%ARRENDAMIENTO%' AND s.CO_NEGO=#{CO_NEGO} AND sp.ST_ELIM=0;")
    public boolean isArrendamiento(@Param("CO_NEGO") Integer CO_NEGO);
    
    @Select("SELECT SUM(nsp.IM_SALD_APROX) FROM TMNEGO n "
            + "INNER JOIN TMNEGO_SUCU ns ON ns.CO_NEGO=n.CO_NEGO "
            + "INNER JOIN TMNEGO_SUCU_PLAN nsp ON nsp.CO_NEGO_SUCU=ns.CO_NEGO_SUCU "
            + "WHERE n.CO_NEGO=#{CO_NEGO} AND nsp.FE_FIN IS NULL AND nsp.ST_ELIM=0 "
            + "UNION "
            + "SELECT SUM(nss.IM_SALD_APROX) FROM TMNEGO n "
            + "INNER JOIN TMNEGO_SUCU ns ON ns.CO_NEGO=n.CO_NEGO "            
            + "INNER JOIN TMNEGO_SUCU_SERV_SUPL nss ON nss.CO_NEGO_SUCU=ns.CO_NEGO_SUCU "
            + "WHERE n.CO_NEGO=#{CO_NEGO} AND nss.FE_FIN IS NULL AND nss.ST_ELIM=0 ") 
    public List<Double> getSaldosAproximados(NEGO aThis);
    
    @Select("SELECT CAST( " +
    "   CASE WHEN SUM(CANT) > 0 THEN 1 " +
    "   ELSE 0 " +
    "   END AS bit) AS NEGO_NUEVO " +
    "   FROM ( " +
    "   SELECT COUNT(*) AS CANT FROM TMNEGO n " +
    "   INNER JOIN TMNEGO_SUCU ns ON ns.CO_NEGO=n.CO_NEGO " +
    "   INNER JOIN TMNEGO_SUCU_PLAN nsp ON nsp.CO_NEGO_SUCU=ns.CO_NEGO_SUCU " +
    "   WHERE n.CO_NEGO=#{CO_NEGO} AND nsp.FE_FIN IS NOT NULL AND nsp.FE_ULTI_FACT_FINA IS NULL AND nsp.ST_ELIM=0 " +
    "   UNION " +
    "   SELECT COUNT(*) AS CANT FROM TMNEGO n " +
    "   INNER JOIN TMNEGO_SUCU ns ON ns.CO_NEGO=n.CO_NEGO " +
    "   INNER JOIN TMNEGO_SUCU_SERV_SUPL nss ON nss.CO_NEGO_SUCU=ns.CO_NEGO_SUCU " +
    "   WHERE n.CO_NEGO=#{CO_NEGO} AND nss.FE_FIN IS NOT NULL AND nss.FE_ULTI_FACT_FINA IS NULL AND nss.ST_ELIM=0" +
    "   ) AS T;")
    public boolean isDesactivadoEnPresentCierre(@Param("CO_NEGO") Integer CO_NEGO);
    
    @Select("SELECT CAST( " +
    "   CASE WHEN SUM(CANT) > 0 THEN 0 " +
    "   ELSE 1 " +
    "   END AS bit) AS NEGO_NUEVO " +
    "   FROM ( " +
    "   SELECT COUNT(NSP.CO_NEGO_SUCU) AS CANT " +
    "   FROM TMNEGO_SUCU NS " +
    "   INNER JOIN TMNEGO_SUCU_PLAN NSP ON NSP.CO_NEGO_SUCU=NS.CO_NEGO_SUCU " +
    "   WHERE NS.CO_NEGO=#{CO_NEGO} AND NSP.FE_FIN IS NULL AND NSP.FE_INIC>=#{FE_FIN} AND NSP.ST_ELIM=0 " +
    "   UNION " +
    "   SELECT COUNT(NSS.CO_NEGO_SUCU) AS CANT " +
    "   FROM TMNEGO_SUCU NS " +
    "   INNER JOIN TMNEGO_SUCU_SERV_SUPL NSS ON NSS.CO_NEGO_SUCU=NS.CO_NEGO_SUCU " +
    "   WHERE NS.CO_NEGO=#{CO_NEGO} AND NSS.FE_FIN IS NULL AND NSS.FE_INIC>=#{FE_FIN} AND NSS.ST_ELIM=0 " +
    "   )AS T;")
    public boolean validarFechaDesactivacion(@Param("CO_NEGO") Integer CO_NEGO, @Param("FE_FIN") Date FE_FIN);
    
    @Select("SELECT CAST( " +
        "CASE WHEN COUNT(*)=0 AND (select COUNT(*) from TMMIGR_NEGO_SUCU WHERE CO_NEGO_ORIG=#{CO_NEGO})>0 THEN 1 " +
        "WHEN COUNT(*)=0 THEN 0 " +
        "WHEN (SELECT COUNT(*) FROM TMNEGO_SUCU WHERE CO_NEGO=#{CO_NEGO} AND ST_ELIM=0 AND FE_FIN IS NULL)>0 THEN 0 " +
        "ELSE 1 " +
        "END AS bit) AS DESACTIVADO " +
        "FROM TMNEGO_SUCU WHERE CO_NEGO=#{CO_NEGO} AND ST_ELIM=0")
    public boolean isDesactivado(Integer CO_NEGO);
 
        
    
    
}
