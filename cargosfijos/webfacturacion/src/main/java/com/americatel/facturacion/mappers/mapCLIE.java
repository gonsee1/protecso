/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.mappers;

import com.americatel.facturacion.fncs.Pagination;
import com.americatel.facturacion.historial.Historial;
import com.americatel.facturacion.models.CLIE;
import com.americatel.facturacion.models.CONT_CLIE;
import com.americatel.facturacion.models.NEGO;
import com.americatel.facturacion.models.SUCU;
import com.americatel.facturacion.models.TIPO_DOCU;
import java.util.List;
import java.util.Map;
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
public interface mapCLIE {    

    @Select("SELECT * FROM TMCLIE WHERE CO_CLIE=#{id} AND ST_ELIM=0")
    @Results(value={
            @Result(property = "CO_CLIE",column="CO_CLIE",id = true),
            @Result(property = "NO_RAZO",column="NO_RAZO"),
            @Result(property = "NO_CLIE",column="NO_CLIE"),
            @Result(property = "AP_CLIE",column="AP_CLIE"), 
            @Result(property = "CO_SUCU_FISC",column="CO_SUCU_FISC"), 
            @Result(property = "sucu_fisc",column = "CO_SUCU_FISC",javaType = SUCU.class,one = @One(select = "com.americatel.facturacion.mappers.mapSUCU.getOnlyId"),id = false),
            @Result(property = "CO_CONT_CLIE_REPR_LEGA",column="CO_CONT_CLIE_REPR_LEGA"), 
            @Result(property = "cont_clie_repre_lega",column = "CO_CONT_CLIE_REPR_LEGA",javaType = SUCU.class,one = @One(select = "com.americatel.facturacion.mappers.mapCONT_CLIE.getById"),id = false),
            @Result(property = "DE_CODI_BUM",column="DE_CODI_BUM"),
            @Result(property = "DE_DIGI_BUM",column="DE_DIGI_BUM"),
            @Result(property = "CO_TIPO_DOC",column="CO_TIPO_DOC"),        
            @Result(property = "tipo_docu",column = "CO_TIPO_DOC",javaType = TIPO_DOCU.class,one = @One(select = "com.americatel.facturacion.mappers.mapTIPO_DOCU.getId"),id = false),
            @Result(property = "DE_NUME_DOCU",column="DE_NUME_DOCU"),
            @Result(property = "ST_ELIM",column="ST_ELIM")
        }
    )
    public CLIE getId(int id);
    
    @Select("SELECT * FROM TMCLIE WHERE CO_CLIE=#{id} AND ST_ELIM=0")
    @Results(value={
            @Result(property = "CO_CLIE",column="CO_CLIE",id = true),
            @Result(property = "NO_RAZO",column="NO_RAZO"),
            @Result(property = "NO_CLIE",column="NO_CLIE"),
            @Result(property = "AP_CLIE",column="AP_CLIE"), 
            @Result(property = "CO_SUCU_FISC",column="CO_SUCU_FISC"), 
            @Result(property = "sucu_fisc",column = "CO_SUCU_FISC",javaType = SUCU.class,one = @One(select = "com.americatel.facturacion.mappers.mapSUCU.getId_paraProcesoFacturacion"),id = false),
            @Result(property = "CO_CONT_CLIE_REPR_LEGA",column="CO_CONT_CLIE_REPR_LEGA"), 
            //@Result(property = "cont_clie_repre_lega",column = "CO_CONT_CLIE_REPR_LEGA",javaType = SUCU.class,one = @One(select = "com.americatel.facturacion.mappers.mapCONT_CLIE.getById"),id = false),
            @Result(property = "DE_CODI_BUM",column="DE_CODI_BUM"),
            @Result(property = "DE_DIGI_BUM",column="DE_DIGI_BUM"),
            @Result(property = "CO_TIPO_DOCU",column="CO_TIPO_DOCU"),        
            @Result(property = "tipo_docu",column = "CO_TIPO_DOCU",javaType = TIPO_DOCU.class,one = @One(select = "com.americatel.facturacion.mappers.mapTIPO_DOCU.getId"),id = false),
            @Result(property = "DE_NUME_DOCU",column="DE_NUME_DOCU"),
            @Result(property = "ST_ELIM",column="ST_ELIM")
            
        }
    )
    public CLIE getId_paraProcesoFacturacion(int id);    
    
    @Select("SELECT * FROM TMCLIE WHERE CO_CLIE=#{id} AND ST_ELIM=0")
    @Results(value={
            @Result(property = "CO_CLIE",column="CO_CLIE",id = true),
            @Result(property = "NO_RAZO",column="NO_RAZO"),
            @Result(property = "NO_CLIE",column="NO_CLIE"),
            @Result(property = "AP_CLIE",column="AP_CLIE"),
            @Result(property = "CO_SUCU_FISC",column="CO_SUCU_FISC"),
            @Result(property = "sucu_fisc",column = "CO_SUCU_FISC",javaType = SUCU.class,one = @One(select = "com.americatel.facturacion.mappers.mapSUCU.getOnlyId"),id = false),
            @Result(property = "CO_CONT_CLIE_REPR_LEGA",column="CO_CONT_CLIE_REPR_LEGA"), 
            @Result(property = "cont_clie_repre_lega",column = "CO_CONT_CLIE_REPR_LEGA",javaType = SUCU.class,one = @One(select = "com.americatel.facturacion.mappers.mapCONT_CLIE.getById"),id = false),            
            @Result(property = "DE_CODI_BUM",column="DE_CODI_BUM"),
            @Result(property = "DE_DIGI_BUM",column="DE_DIGI_BUM"),
            @Result(property = "CO_TIPO_DOC",column="CO_TIPO_DOC"),        
            @Result(property = "tipo_docu",column = "CO_TIPO_DOC",javaType = TIPO_DOCU.class,one = @One(select = "com.americatel.facturacion.mappers.mapTIPO_DOCU.getId"),id = false),
            @Result(property = "DE_NUME_DOCU",column="DE_NUME_DOCU"),
            @Result(property = "ST_ELIM",column="ST_ELIM"),
        }
    )
    public CLIE getOnlyId(int id);   
    
    @Select("SELECT * FROM TMCLIE WHERE ST_ELIM=0")
    @Results(value={
            @Result(property = "CO_CLIE",column="CO_CLIE",id = true),
            @Result(property = "NO_RAZO",column="NO_RAZO"),
            @Result(property = "NO_CLIE",column="NO_CLIE"),
            @Result(property = "AP_CLIE",column="AP_CLIE"), 
            @Result(property = "CO_SUCU_FISC",column="CO_SUCU_FISC"), 
            @Result(property = "sucu_fisc",column = "CO_SUCU_FISC",javaType = SUCU.class,one = @One(select = "com.americatel.facturacion.mappers.mapSUCU.getOnlyId"),id = false),
            @Result(property = "CO_CONT_CLIE_REPR_LEGA",column="CO_CONT_CLIE_REPR_LEGA"), 
            @Result(property = "cont_clie_repre_lega",column = "CO_CONT_CLIE_REPR_LEGA",javaType = SUCU.class,one = @One(select = "com.americatel.facturacion.mappers.mapCONT_CLIE.getById"),id = false),
            @Result(property = "DE_CODI_BUM",column="DE_CODI_BUM"),
            @Result(property = "DE_DIGI_BUM",column="DE_DIGI_BUM"),
            @Result(property = "CO_TIPO_DOC",column="CO_TIPO_DOC"),        
            @Result(property = "tipo_docu",column = "CO_TIPO_DOC",javaType = TIPO_DOCU.class,one = @One(select = "com.americatel.facturacion.mappers.mapTIPO_DOCU.getId"),id = false),
            @Result(property = "DE_NUME_DOCU",column="DE_NUME_DOCU"),
            @Result(property = "ST_ELIM",column="ST_ELIM")
        }
    )
    public List<CLIE> getAllDetalle();
    
    @Select("SELECT * FROM TMCLIE WHERE ST_ELIM=0")
    public List<CLIE> getAll();
    
    @Update("UPDATE TMCLIE SET NO_RAZO='${NO_RAZO}',NO_CLIE='${NO_CLIE}',AP_CLIE='${AP_CLIE}',CO_TIPO_CLIE='${CO_TIPO_CLIE}',CO_TIPO_DOCU='${CO_TIPO_DOCU}',DE_NUME_DOCU='${DE_NUME_DOCU}',DE_CODI_BUM='${DE_CODI_BUM}',DE_DIGI_BUM='${DE_DIGI_BUM}',CO_CONT_CLIE_REPR_LEGA=#{CO_CONT_CLIE_REPR_LEGA},CO_SUCU_FISC=#{CO_SUCU_FISC},DE_EJEC=#{DE_EJEC},DE_SUB_GERE=#{DE_SUB_GERE},DE_SEGM=#{DE_SEGM},"+Historial.stringMapperModificar+" WHERE CO_CLIE=${CO_CLIE} AND ST_ELIM=0;")
    public int update(CLIE clie);
    
    @Update("UPDATE TMCLIE SET CO_SUCU_FISC=#{CO_SUCU_FISC},"+Historial.stringMapperModificar+" WHERE CO_CLIE=${CO_CLIE} AND ST_ELIM=0;")
    public int updateSucursalFiscal(CLIE clie); 
    
    @Update("UPDATE TMCLIE SET CO_CONT_CLIE_REPR_LEGA=#{CO_CONT_CLIE_REPR_LEGA},"+Historial.stringMapperModificar+" WHERE CO_CLIE=${CO_CLIE} AND ST_ELIM=0;")
    public int updateRepresentanteLegal(CLIE clie);     

    @Update("UPDATE TMCLIE SET ST_ELIM=1,"+Historial.stringMapperModificar+" WHERE CO_CLIE=#{CO_CLIE};")
    public int delete(CLIE clie);

    @Insert("INSERT INTO TMCLIE (NO_RAZO,NO_CLIE,AP_CLIE,CO_TIPO_CLIE,CO_TIPO_DOCU,DE_NUME_DOCU,DE_CODI_BUM,DE_DIGI_BUM,DE_EJEC,DE_SUB_GERE,DE_SEGM,"+Historial.stringMapperInsertarTitles+") VALUES ('${NO_RAZO}','${NO_CLIE}','${AP_CLIE}','${CO_TIPO_CLIE}','${CO_TIPO_DOCU}','${DE_NUME_DOCU}','${DE_CODI_BUM}','${DE_DIGI_BUM}','${DE_EJEC}','${DE_SUB_GERE}','${DE_SEGM}',"+Historial.stringMapperInsertarValuesNumeral+")")
    @SelectKey(statement="SELECT @@IDENTITY;", keyProperty="CO_CLIE", before=false, resultType=Integer.class)
    public void insert(CLIE dist);
    /*
    @Select("SELECT * FROM TMCLIE WHERE (NO_CLIE like '%${FIL_CLIE}%' OR  AP_CLIE like '%${FIL_CLIE}%' OR  NO_RAZO like '%${FIL_CLIE}%' OR  DE_NUME_DOCU like '%${FIL_CLIE}%') AND ST_ELIM=0  ORDER BY [NO_RAZO],[NO_CLIE],[AP_CLIE] OFFSET ${Pagination.getGroup()} ROWS FETCH NEXT #{Pagination.Limit} ROWS ONLY")
    @Results(value = {
            @Result(property = "CO_CLIE",column="CO_CLIE",id = true),
            @Result(property = "NO_RAZO",column="NO_RAZO"),
            @Result(property = "NO_CLIE",column="NO_CLIE"),
            @Result(property = "AP_CLIE",column="AP_CLIE"),  
            @Result(property = "CO_SUCU_FISC",column="CO_SUCU_FISC"),
            //@Result(property = "sucu_fisc",column = "CO_SUCU_FISC",javaType = SUCU.class,one = @One(select = "com.americatel.facturacion.mappers.mapSUCU.getOnlyId"),id = false),
            @Result(property = "CO_CONT_CLIE_REPR_LEGA",column="CO_CONT_CLIE_REPR_LEGA"), 
            //@Result(property = "cont_clie_repre_lega",column = "CO_CONT_CLIE_REPR_LEGA",javaType = SUCU.class,one = @One(select = "com.americatel.facturacion.mappers.mapCONT_CLIE.getById"),id = false),            
            @Result(property = "CO_TIPO_DOC",column="CO_TIPO_DOC"),            
            @Result(property = "DE_NUME_DOCU",column="DE_NUME_DOCU"),
            @Result(property = "ST_ELIM",column="ST_ELIM"),
            //@Result(property = "tipo_docu",column = "CO_TIPO_DOCU",javaType = TIPO_DOCU.class,one = @One(select = "com.americatel.facturacion.mappers.mapTIPO_DOCU.getId"),id = false)
        }
    ) */
    @Select("SELECT * FROM TMCLIE WHERE (NO_CLIE like '%${FIL_CLIE}%' OR  AP_CLIE like '%${FIL_CLIE}%' OR  NO_RAZO like '%${FIL_CLIE}%' OR  DE_NUME_DOCU like '%${FIL_CLIE}%') AND ST_ELIM=0 ORDER BY [NO_RAZO],[NO_CLIE],[AP_CLIE] OFFSET ${Pagination.getGroup()} ROWS FETCH NEXT #{Pagination.Limit} ROWS ONLY")
    @Results(value = {
            @Result(property = "CO_CLIE",column="CO_CLIE",id = true),
            @Result(property = "NO_RAZO",column="NO_RAZO"),
            @Result(property = "NO_CLIE",column="NO_CLIE"),
            @Result(property = "AP_CLIE",column="AP_CLIE"),  
            @Result(property = "CO_SUCU_FISC",column="CO_SUCU_FISC"),
            @Result(property = "sucu_fisc",column = "CO_SUCU_FISC",javaType = SUCU.class,one = @One(select = "com.americatel.facturacion.mappers.mapSUCU.getOnlyId"),id = false),
            @Result(property = "CO_CONT_CLIE_REPR_LEGA",column="CO_CONT_CLIE_REPR_LEGA"), 
            @Result(property = "cont_clie_repre_lega",column = "CO_CONT_CLIE_REPR_LEGA",javaType = SUCU.class,one = @One(select = "com.americatel.facturacion.mappers.mapCONT_CLIE.getById"),id = false),            
            @Result(property = "CO_TIPO_DOC",column="CO_TIPO_DOC"),            
            @Result(property = "DE_NUME_DOCU",column="DE_NUME_DOCU"),
            @Result(property = "ST_ELIM",column="ST_ELIM"),
            @Result(property = "tipo_docu",column = "CO_TIPO_DOCU",javaType = TIPO_DOCU.class,one = @One(select = "com.americatel.facturacion.mappers.mapTIPO_DOCU.getId"),id = false)
        }
    )    
    public List<CLIE> select(@Param(value = "FIL_CLIE") String FIL_CLIE,@Param(value = "Pagination") Pagination pagination);    

    @Select("SELECT count(*) FROM TMCLIE WHERE (NO_CLIE like '%${FIL_CLIE}%' OR  AP_CLIE like '%${FIL_CLIE}%' OR  NO_RAZO like '%${FIL_CLIE}%' OR  DE_NUME_DOCU like '%${FIL_CLIE}%')  AND ST_ELIM=0; ")
    public int getNumResultsSelect(@Param(value = "FIL_CLIE") String FIL_CLIE);

    
    @Select("SELECT * FROM TMCLIE WHERE (NO_CLIE like '%${SEARCH}%' OR  AP_CLIE like '%${SEARCH}%' OR  NO_RAZO like '%${SEARCH}%' OR  DE_NUME_DOCU like '%${SEARCH}%')  AND ST_ELIM=0 ORDER BY [NO_RAZO],[NO_CLIE],[AP_CLIE] OFFSET ${Pagination.getGroup()} ROWS FETCH NEXT #{Pagination.Limit} ROWS ONLY")
    @Results(value = {
            @Result(property = "CO_CLIE",column="CO_CLIE",id = true),
            @Result(property = "NO_RAZO",column="NO_RAZO"),
            @Result(property = "NO_CLIE",column="NO_CLIE"),
            @Result(property = "AP_CLIE",column="AP_CLIE"), 
            @Result(property = "CO_SUCU_FISC",column="CO_SUCU_FISC"),
            @Result(property = "sucu_fisc",column = "CO_SUCU_FISC",javaType = SUCU.class,one = @One(select = "com.americatel.facturacion.mappers.mapSUCU.getOnlyId"),id = false),
            @Result(property = "CO_CONT_CLIE_REPR_LEGA",column="CO_CONT_CLIE_REPR_LEGA"), 
            @Result(property = "cont_clie_repre_lega",column = "CO_CONT_CLIE_REPR_LEGA",javaType = SUCU.class,one = @One(select = "com.americatel.facturacion.mappers.mapCONT_CLIE.getById"),id = false),            
            @Result(property = "CO_TIPO_DOC",column="CO_TIPO_DOC"),            
            @Result(property = "DE_NUME_DOCU",column="DE_NUME_DOCU"),
            @Result(property = "DE_CODI_BUM",column="DE_CODI_BUM"),
            @Result(property = "DE_DIGI_BUM",column="DE_DIGI_BUM"),
            @Result(property = "ST_ELIM",column="ST_ELIM"),
            @Result(property = "tipo_docu",column = "CO_TIPO_DOCU",javaType = TIPO_DOCU.class,one = @One(select = "com.americatel.facturacion.mappers.mapTIPO_DOCU.getId"),id = false)
        }
    )    
    public List<CLIE> selectBySEARCH(@Param(value = "SEARCH") String SEARCH,@Param(value = "Pagination")  Pagination pagination);

    @Select("SELECT count(*) FROM TMCLIE WHERE (NO_CLIE like '%${SEARCH}%' OR  AP_CLIE like '%${SEARCH}%' OR  NO_RAZO like '%${SEARCH}%' OR  DE_NUME_DOCU like '%${SEARCH}%') AND ST_ELIM=0")    
    public int getNumResultsSEARCH(@Param(value = "SEARCH") String SEARCH);    

    @Select("SELECT TOP 1 * FROM TMSUCU WHERE (CO_CLIE=#{CO_CLIE} AND CO_SUCU=#{CO_SUCU_FISC}) AND ST_ELIM=0;") 
    public SUCU getSucursalFiscalxxx(@Param(value = "CO_CLIE") Integer CO_CLIE,@Param(value = "CO_SUCU_FISC") Integer CO_SUCU_FISC);
    
    @Select("SELECT TOP 1 * FROM TMSUCU WHERE (CO_CLIE=#{CO_CLIE} AND CO_SUCU=#{CO_SUCU_FISC}) AND ST_ELIM=0;")
    public SUCU getSucursalFiscal(CLIE aThis);
    
    @Select("SELECT TOP 1 * FROM TMSUCU WHERE (CO_CLIE=#{CO_CLIE}) AND ST_ELIM=0;")
    public SUCU getSucursalFiscalPrimero(CLIE aThis);    //Si no existe direccion fiscal con el flag toma el primero

    @Select("SELECT * FROM TMNEGO WHERE CO_CLIE=#{CO_CLIE} AND ST_ELIM=0  AND CO_PROD=(SELECT CO_PROD FROM TMNEGO WHERE CO_NEGO=#{CO_NEGO});")
    @Results(value={
            @Result(property = "CO_NEGO",column="CO_NEGO",id = true),
            @Result(property = "CO_SUCU_CORR",column="CO_SUCU_CORR"),
            @Result(property = "CO_CLIE",column="CO_CLIE"), 
            @Result(property = "CO_PROD",column="CO_PROD"), 
            @Result(property = "CO_TIPO_FACT",column="CO_TIPO_FACT"),     
            @Result(property = "CO_MONE_FACT",column="CO_MONE_FACT"),     
            @Result(property = "CO_PERI_FACT",column="CO_PERI_FACT"), 
            @Result(property = "ST_ELIM",column="ST_ELIM"),
            @Result(property = "isDesactivado",column = "CO_NEGO",javaType = Boolean.class,one = @One(select = "com.americatel.facturacion.mappers.mapNEGO_SUCU.isNegoDesactivado"),id = false),
        }
    )
    public List<NEGO> selectNegociosByCO_CLIE_Migrar(@Param(value = "CO_CLIE") Integer CO_CLIE, @Param(value = "CO_NEGO") Integer CO_NEGO);
    
    @Select("SELECT CAST( " +
    "CASE WHEN SUM(CANT) > 0 THEN 0 " +
    "ELSE 1 " +
    "END AS bit) AS CLIE_NUEVO " +
    "FROM ( " +
    "SELECT COUNT(*) AS CANT FROM TMNEGO_SUCU_PLAN WHERE CO_NEGO_SUCU IN (SELECT CO_NEGO_SUCU FROM TMNEGO_SUCU WHERE CO_NEGO IN ( " +
    "SELECT CO_NEGO FROM TMNEGO WHERE CO_CLIE=#{CO_CLIE} " +
    ")) AND FE_ULTI_FACT IS NOT NULL AND ST_ELIM=0" +
    "UNION " +
    "SELECT COUNT(*) AS CANT FROM TMNEGO_SUCU_SERV_SUPL WHERE CO_NEGO_SUCU IN (SELECT CO_NEGO_SUCU FROM TMNEGO_SUCU WHERE CO_NEGO IN ( " +
    "SELECT CO_NEGO FROM TMNEGO WHERE CO_CLIE=#{CO_CLIE} " +
    ")) AND FE_ULTI_FACT IS NOT NULL AND ST_ELIM=0" +
    ") as W;")
    public boolean isNuevo(@Param(value = "CO_CLIE") Integer CO_CLIE);

    @Select("SELECT count(*) as existe FROM TMCLIE WHERE Rtrim(Ltrim(DE_NUME_DOCU))=Rtrim(Ltrim(#{DE_NUME_DOCU})) and CO_TIPO_DOCU=#{CO_TIPO_DOCU} and CO_CLIE<>#{CO_CLIE}")
    public List<Map<String, Object>> existeDocumento(@Param(value = "CO_CLIE") Integer CO_CLIE,@Param(value = "DE_NUME_DOCU") String DE_NUME_DOCU,@Param(value = "CO_TIPO_DOCU") Integer CO_TIPO_DOCU);

    @Select("SELECT DISTINCT(C.CO_CLIE),C.* FROM TMCLIE C INNER JOIN TMNEGO N ON C.CO_CLIE = N.CO_CLIE " +
            "WHERE C.ST_ELIM=0 AND N.CO_PROD=#{COD_SERVICIO} ")
    @Results(value={
            @Result(property = "CO_CLIE",column="CO_CLIE",id = true),
            @Result(property = "NO_RAZO",column="NO_RAZO"),
            @Result(property = "NO_CLIE",column="NO_CLIE"),
            @Result(property = "AP_CLIE",column="AP_CLIE"), 
            @Result(property = "CO_SUCU_FISC",column="CO_SUCU_FISC"), 
            @Result(property = "sucu_fisc",column = "CO_SUCU_FISC",javaType = SUCU.class,one = @One(select = "com.americatel.facturacion.mappers.mapSUCU.getOnlyId"),id = false),
            @Result(property = "CO_CONT_CLIE_REPR_LEGA",column="CO_CONT_CLIE_REPR_LEGA"), 
            @Result(property = "cont_clie_repre_lega",column = "CO_CONT_CLIE_REPR_LEGA",javaType = CONT_CLIE.class,one = @One(select = "com.americatel.facturacion.mappers.mapCONT_CLIE.getById"),id = false),
            @Result(property = "DE_CODI_BUM",column="DE_CODI_BUM"),
            @Result(property = "DE_DIGI_BUM",column="DE_DIGI_BUM"),
            @Result(property = "CO_TIPO_DOC",column="CO_TIPO_DOC"),        
            @Result(property = "tipo_docu",column = "CO_TIPO_DOC",javaType = TIPO_DOCU.class,one = @One(select = "com.americatel.facturacion.mappers.mapTIPO_DOCU.getId"),id = false),
            @Result(property = "DE_NUME_DOCU",column="DE_NUME_DOCU"),
            @Result(property = "ST_ELIM",column="ST_ELIM")
        }
    )        
    public List<CLIE> getAllDetalleByServicio(@Param(value = "COD_PROD_PADRE") Integer COD_PROD_PADRE, @Param(value = "COD_SERVICIO") Integer COD_SERVICIO);
    
    @Select("SELECT DISTINCT(C.CO_CLIE),C.* FROM TMCLIE C INNER JOIN TMNEGO N ON C.CO_CLIE = N.CO_CLIE INNER JOIN TMPROD_PADRE_TMPROD PPP ON PPP.COD_PROD = N.CO_PROD" +
            " WHERE C.ST_ELIM=0 AND PPP.COD_PROD_PADRE =#{COD_PROD_PADRE} ")
    @Results(value={
            @Result(property = "CO_CLIE",column="CO_CLIE",id = true),
            @Result(property = "NO_RAZO",column="NO_RAZO"),
            @Result(property = "NO_CLIE",column="NO_CLIE"),
            @Result(property = "AP_CLIE",column="AP_CLIE"), 
            @Result(property = "CO_SUCU_FISC",column="CO_SUCU_FISC"), 
            @Result(property = "sucu_fisc",column = "CO_SUCU_FISC",javaType = SUCU.class,one = @One(select = "com.americatel.facturacion.mappers.mapSUCU.getOnlyId"),id = false),
            @Result(property = "CO_CONT_CLIE_REPR_LEGA",column="CO_CONT_CLIE_REPR_LEGA"), 
            @Result(property = "cont_clie_repre_lega",column = "CO_CONT_CLIE_REPR_LEGA",javaType = CONT_CLIE.class,one = @One(select = "com.americatel.facturacion.mappers.mapCONT_CLIE.getById"),id = false),
            @Result(property = "DE_CODI_BUM",column="DE_CODI_BUM"),
            @Result(property = "DE_DIGI_BUM",column="DE_DIGI_BUM"),
            @Result(property = "CO_TIPO_DOC",column="CO_TIPO_DOC"),        
            @Result(property = "tipo_docu",column = "CO_TIPO_DOC",javaType = TIPO_DOCU.class,one = @One(select = "com.americatel.facturacion.mappers.mapTIPO_DOCU.getId"),id = false),
            @Result(property = "DE_NUME_DOCU",column="DE_NUME_DOCU"),
            @Result(property = "ST_ELIM",column="ST_ELIM")
        }
    )        
    public List<CLIE> getAllDetalleByProducto(@Param(value = "COD_PROD_PADRE") Integer COD_PROD_PADRE);
    
}
