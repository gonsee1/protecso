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
import com.americatel.facturacion.models.DIST;
import com.americatel.facturacion.models.FACT;
import com.americatel.facturacion.models.NEGO;
import com.americatel.facturacion.models.RECI;
import com.americatel.facturacion.models.REPORTE_FACTURA;
import java.util.List;
import java.util.Map;
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
public interface mapFACT {
    @Insert("INSERT INTO TMFACT"+
            "(CO_FACT,CO_CIER,CO_NEGO,IM_VALO_VENT,IM_VALO_DESC,IM_VALO_VENT_NETO,IM_IGV,IM_TOTA,DE_MONT,FE_EMIS,FE_VENC,CO_CLIE,DE_CODI_BUM,NO_CLIE,DE_DIRE_FISC,CO_DIST_FISC,CO_TIPO_DOCU,DE_NUME_DOCU,DE_DIRE_CORR,DE_REF_DIRE_CORR,CO_DIST_CORR,DE_DIRE_INST,CO_DIST_INST,NO_MONE_FACT,CO_MONE_FACT,NO_DIST_FISC,NO_TIPO_DOCU,NO_DIST_CORR,NO_DIST_INST,DE_SIMB_MONE_FACT,ST_ANUL,ST_AFECTO_DETRACCION,IM_AJUS_NIGV,IM_INST,IM_ALQU,IM_OTRO,IM_AJUS_SIGV, DE_PERI, IM_SERV_MENS, IM_DIF_CARGO_FIJO, FE_INI, FE_FIN,"+Historial.stringMapperInsertarTitles+") VALUES "+
            "(#{CO_FACT},#{CO_CIER},#{CO_NEGO},#{IM_VALO_VENT},#{IM_VALO_DESC},#{IM_VALO_VENT_NETO},#{IM_IGV},#{IM_TOTA},#{DE_MONT},#{FE_EMIS},#{FE_VENC},#{CO_CLIE},#{DE_CODI_BUM},#{NO_CLIE},#{DE_DIRE_FISC},#{CO_DIST_FISC},#{CO_TIPO_DOCU},#{DE_NUME_DOCU},#{DE_DIRE_CORR},#{DE_REF_DIRE_CORR},#{CO_DIST_CORR},#{DE_DIRE_INST},#{CO_DIST_INST},#{NO_MONE_FACT},#{CO_MONE_FACT},#{NO_DIST_FISC},#{NO_TIPO_DOCU},#{NO_DIST_CORR},#{NO_DIST_INST},#{DE_SIMB_MONE_FACT},#{ST_ANUL},#{ST_AFECTO_DETRACCION},#{IM_AJUS_NIGV},#{IM_INST},#{IM_ALQU},#{IM_OTRO},#{IM_AJUS_SIGV}, #{DE_PERI}, #{IM_SERV_MENS}, #{IM_DIF_CARGO_FIJO}, #{FE_INI}, #{FE_FIN} ,"+Historial.stringMapperInsertarValuesNumeral+");")
    public void insertar(FACT f); 
    
    @Select("SELECT * FROM TMFACT WHERE CO_CIER=#{CO_CIER} AND ST_ELIM=0 AND ST_ANUL=0;")
    @Results(value={
        @Result(property = "CO_RECI",column="CO_RECI",id = true),
        @Result(property = "dist_corr",column = "CO_DIST_CORR",javaType = DIST.class,one = @One(select = "com.americatel.facturacion.mappers.mapDIST.getId"),id = false),
        @Result(property = "dist_fisc",column = "CO_DIST_FISC",javaType = DIST.class,one = @One(select = "com.americatel.facturacion.mappers.mapDIST.getId"),id = false),
        @Result(property = "dist_inst",column = "CO_DIST_INST",javaType = DIST.class,one = @One(select = "com.americatel.facturacion.mappers.mapDIST.getId"),id = false)
    })     
    public List<FACT> getFacturas(CIER cier);     

    @Select("SELECT TOP 1 * FROM TMFACT f INNER JOIN TMCIER c ON c.CO_CIER=f.CO_CIER AND c.ST_CIER=4 ORDER BY f.CO_FACT DESC;")
    public FACT getUltimaFacturaGeneradaCierreCerrado();
    
    @Select("SELECT COUNT(*) FROM (" +
        "   SELECT COUNT(CO_NEGO_SUCU) AS actCount FROM TMCIER_DATA_SERV_UNIC su" +
        "	INNER JOIN TMCIER_DATA_SUCU s ON s.CO_CIER_DATA_SUCU=su.CO_CIER_DATA_SUCU" +
        "	WHERE CO_FACT=#{CO_FACT} GROUP BY s.CO_NEGO_SUCU" +
        ") t;")
    public Integer getCantidadSucursalesFacturaronByCO_FACT(Long CO_FACT); 
    
    
    @Select("SELECT f.* FROM TMFACT f INNER JOIN TMCIER c ON c.CO_CIER=f.CO_CIER"
            + " AND (f.CO_FACT like '%${SEARCH}%' or f.CO_NEGO like '%${SEARCH}%')"
            + " AND (c.ST_CIER<>4) AND f.ST_ANUL=0 ORDER BY f.CO_FACT"
            + " OFFSET ${Pagination.getGroup()} ROWS FETCH NEXT #{Pagination.Limit} ROWS ONLY;")
    @Results(value={
        @Result(property = "CO_FACT",column="CO_FACT",id = true),
        @Result(property = "CO_CIER",column="CO_CIER",id = false),
        @Result(property = "CO_DIST_CORR",column="CO_DIST_CORR",id = false),
        @Result(property = "CO_DIST_FISC",column="CO_DIST_FISC",id = false),
        @Result(property = "CO_DIST_INST",column="CO_DIST_INST",id = false),        
        @Result(property = "cier",column = "CO_CIER",javaType = CIER.class,one = @One(select = "com.americatel.facturacion.mappers.mapCIER.getId"),id = false),
        @Result(property = "dist_corr",column = "CO_DIST_CORR",javaType = DIST.class,one = @One(select = "com.americatel.facturacion.mappers.mapDIST.getId"),id = false),
        @Result(property = "dist_fisc",column = "CO_DIST_FISC",javaType = DIST.class,one = @One(select = "com.americatel.facturacion.mappers.mapDIST.getId"),id = false),
        @Result(property = "dist_inst",column = "CO_DIST_INST",javaType = DIST.class,one = @One(select = "com.americatel.facturacion.mappers.mapDIST.getId"),id = false),
        @Result(property = "cantidad_sucursales_facturaron_select",column = "CO_FACT",javaType = Integer.class,one = @One(select = "com.americatel.facturacion.mappers.mapFACT.getCantidadSucursalesFacturaronByCO_FACT"),id = false), 
        @Result(property = "is_afecto_detraccion",column = "CO_FACT",javaType = Boolean.class,one = @One(select = "com.americatel.facturacion.mappers.mapFACT.isAfectoDetraccionByCO_FACT"),id = false)        
    })    
    public List<FACT> selectBySEARCH(@Param("SEARCH") String SEARCH,@Param("Pagination")Pagination pagination);
    
    @Select("SELECT count(*) FROM ( "
            + "SELECT CO_FACT FROM TMCIER_DATA_SERV_SUPL WHERE CO_FACT IS NOT NULL AND CO_FACT=#{CO_FACT} UNION " 
            + "SELECT CO_FACT FROM TMCIER_DATA_SERV_UNIC WHERE CO_FACT IS NOT NULL AND CO_FACT=#{CO_FACT} UNION " 
            + "SELECT CO_FACT FROM TMCIER_DATA_PLAN WHERE CO_FACT IS NOT NULL AND CO_FACT=#{CO_FACT} UNION " 
            + "SELECT CO_FACT FROM TMCIER_DATA_PROM_MONT WHERE CO_FACT IS NOT NULL AND CO_FACT=#{CO_FACT} "
            + ") as W")
    public Boolean isAfectoDetraccionByCO_FACT(Long CO_FACT);         
    
    @Select("SELECT count(*) FROM TMFACT r INNER JOIN TMCIER c ON c.CO_CIER=r.CO_CIER"
            + " AND (r.CO_FACT like '%${SEARCH}%' or r.CO_NEGO like '%${SEARCH}%')"
            + " AND (c.ST_CIER<>4) AND r.ST_ANUL=0")
    public int getNumResultsBySEARCH(@Param("SEARCH") String SEARCH);
    
    
        @Select("SELECT * FROM TMFACT WHERE CO_FACT=#{CO_FACT} AND ST_ELIM=0;")
    @Results(value={
        @Result(property = "CO_FACT",column="CO_FACT",id = true),
        @Result(property = "CO_DIST_FISC",column="CO_DIST_FISC",id = false),
        @Result(property = "CO_CLIE",column="CO_CLIE",id = false),
        @Result(property = "CO_NEGO",column="CO_NEGO",id = false),
        @Result(property = "CO_CIER",column="CO_CIER",id = false),
        @Result(property = "dist_fisc",column = "CO_DIST_FISC",javaType = DIST.class,one = @One(select = "com.americatel.facturacion.mappers.mapDIST.getId"),id = false),
        @Result(property = "clie",column = "CO_CLIE",javaType = CLIE.class,one = @One(select = "com.americatel.facturacion.mappers.mapCLIE.getId"),id = false),
        @Result(property = "nego",column = "CO_NEGO",javaType = NEGO.class,one = @One(select = "com.americatel.facturacion.mappers.mapNEGO.getOnlyId"),id = false),
        @Result(property = "cier",column = "CO_CIER",javaType = CIER.class,one = @One(select = "com.americatel.facturacion.mappers.mapCIER.getId"),id = false),
        @Result(property = "cantidad_sucursales_facturaron_select",column = "CO_RECI",javaType = Integer.class,one = @One(select = "com.americatel.facturacion.mappers.mapFACT.getCantidadSucursalesFacturaronByCO_FACT"),id = false)
    }) 
    public FACT getById(Long CO_FACT);
    
    
    @Update("UPDATE TMFACT SET "
            + "FE_EMIS=#{FE_EMIS},FE_VENC=#{FE_VENC},DE_CODI_BUM=#{DE_CODI_BUM},NO_CLIE=#{NO_CLIE},"
            + "DE_DIRE_FISC=#{DE_DIRE_FISC},CO_DIST_FISC=#{CO_DIST_FISC},CO_TIPO_DOCU=#{CO_TIPO_DOCU},DE_NUME_DOCU=#{DE_NUME_DOCU},"
            + "DE_DIRE_CORR=#{DE_DIRE_CORR},DE_REF_DIRE_CORR=#{DE_REF_DIRE_CORR},CO_DIST_CORR=#{CO_DIST_CORR},DE_DIRE_INST=#{DE_DIRE_INST},"
            + "CO_DIST_INST=#{CO_DIST_INST},NO_MONE_FACT=#{NO_MONE_FACT},CO_MONE_FACT=#{CO_MONE_FACT},NO_DIST_FISC=#{NO_DIST_FISC},"
            + "NO_TIPO_DOCU=#{NO_TIPO_DOCU},NO_DIST_CORR=#{NO_DIST_CORR},NO_DIST_INST=#{NO_DIST_INST},DE_SIMB_MONE_FACT=#{DE_SIMB_MONE_FACT},"
            + "IM_AJUS_SIGV=#{IM_AJUS_SIGV}, DE_PERI=#{DE_PERI}, IM_SERV_MENS=#{IM_SERV_MENS}, IM_DIF_CARGO_FIJO=#{IM_DIF_CARGO_FIJO}, FE_INI=#{FE_INI}, FE_FIN=#{FE_FIN} ,"+Historial.stringMapperModificar+""
            + "WHERE CO_FACT=#{CO_FACT};")
    public void update(FACT fact);
    
    @Update("UPDATE TMFACT SET ST_ANUL=1,"+Historial.stringMapperModificar+" WHERE CO_FACT=#{CO_FACT} AND ST_ELIM=0;")
    public void anular(FACT fact);
    
    @Select("SELECT SUM (W.p) FROM ("
            + " SELECT 1 as p FROM TMFACT r"
            + " INNER JOIN TMFACT_GLOS g ON g.CO_FACT=r.CO_FACT AND g.ST_ELIM=0"
            + " WHERE r.CO_FACT=#{CO_FACT} AND g.TI_GLOS=1 AND r.ST_ELIM=0"
            + " GROUP BY g.CO_NEGO_SUCU"
            + " ) as W")    
    public Integer getCantidadSucursalesFacturaronByFact(Long CO_FACT);
    
    @Update("UPDATE TMFACT SET IM_VALO_VENT=#{IM_VALO_VENT},"
            + "IM_AJUS_SIGV=#{IM_AJUS_SIGV},IM_IGV=#{IM_IGV},IM_AJUS_NIGV=#{IM_AJUS_NIGV},"
            + "IM_TOTA=#{IM_TOTA},IM_INST=#{IM_INST},IM_ALQU=#{IM_ALQU},IM_OTRO=#{IM_OTRO},"
            + "IM_VALO_DESC=#{IM_VALO_DESC},IM_SERV_MENS=#{IM_SERV_MENS},IM_DIF_CARGO_FIJO=#{IM_DIF_CARGO_FIJO},"
            + "IM_VALO_VENT_NETO=#{IM_VALO_VENT_NETO},"+Historial.stringMapperModificar+""
            + "WHERE CO_FACT=#{CO_FACT};")
    public void updateMontos(FACT fact);
    
    @Select("SELECT * FROM TMFACT WHERE CO_CIER=#{CO_CIER} AND ST_ELIM=0 AND ST_ANUL=1;")
    @Results(value={
        @Result(property = "CO_FACT",column="CO_FACT",id = true),
        @Result(property = "CO_DIST_CORR",column="CO_DIST_CORR",id = false),
        @Result(property = "CO_DIST_FISC",column="CO_DIST_FISC",id = false),
        @Result(property = "CO_DIST_INST",column="CO_DIST_INST",id = false),
        @Result(property = "dist_corr",column = "CO_DIST_CORR",javaType = DIST.class,one = @One(select = "com.americatel.facturacion.mappers.mapDIST.getId"),id = false),
        @Result(property = "dist_fisc",column = "CO_DIST_FISC",javaType = DIST.class,one = @One(select = "com.americatel.facturacion.mappers.mapDIST.getId"),id = false),
        @Result(property = "dist_inst",column = "CO_DIST_INST",javaType = DIST.class,one = @One(select = "com.americatel.facturacion.mappers.mapDIST.getId"),id = false),
        @Result(property = "cantidad_sucursales_facturaron_select",column = "CO_FACT",javaType = Integer.class,one = @One(select = "com.americatel.facturacion.mappers.mapFACT.getCantidadSucursalesFacturaronByCO_FACT"),id = false),
        @Result(property = "is_afecto_detraccion",column = "CO_FACT",javaType = Boolean.class,one = @One(select = "com.americatel.facturacion.mappers.mapFACT.isAfectoDetraccionByCO_FACT"),id = false),    
        @Result(property = "detalle_servicio",column = "CO_FACT",javaType = Boolean.class,one = @One(select = "com.americatel.facturacion.mappers.mapFACT_GLOS.getDetalleServicioByFACT"),id = false)    
    })  
    public List<FACT> getFacturasAnulados(CIER cier);

    
    @Select("SELECT      " +
    "       TR.CO_FACT          AS 'CAMPO_1',     " +
    "       FORMAT(TC.FE_EMIS,'dd/MM/yyyy')           AS  'CAMPO_2',     " +
    "       TCLI.NO_RAZO         AS  'CAMPO_3',     " +
    "       TCLI.CO_CLIE         AS  'CAMPO_4',     " +
    "       TR.DE_DIRE_FISC      AS  'CAMPO_5',     " +
    "       TDI.NO_DIST		   AS 'DISTRITO',     " +
    "       TP.NO_PROV		   AS 'PROVINCIA',     " +
    "       TDE.NO_DEPA		   AS 'DEPARTAMENTO',     " +
    "       TCLI.DE_NUME_DOCU   AS 'RUC',     " +
    "       FORMAT(TC.FE_VENC,'dd/MM/yyyy')		   AS 'FECHA_VENCIMIENTO',     " +
    "       CDSU.DE_NOMB         AS 'DESCRIPCION',       " +
    "           COUNT(1)         AS 'CANTIDAD',     " +
    "       CDSU.IM_MONT	       AS 'VALOR_UNITARIO',     " +
    "       '0.00'               AS 'DESCUENTO',     " +
    "       (COUNT(1) * CDSU.IM_MONT) AS 'TOTAL',     " +
    "       TR.IM_VALO_VENT_NETO	       AS 'OP_GRAVADAS',     " +
    "       '0'	               AS 'OP_INAFECTAS',     " +
    "       '0'	               AS 'OP_EXONERADAS',     " +
    "       '0'			       AS 'OP_GRATUITAS',     " +
    "       '0'	AS 'DESCUENTOS_GLOBALES',     " +
    "       TR.IM_IGV			   AS 'IGV',     " +
    "       '0'				   AS 'OTROS_TRIBUTOS',     " +
    "       '0'				   AS 'OTROS_CARGOS',     " +
    "       TR.IM_TOTA           AS 'TOTAL_2',     " +
    "       ((TR.IM_VALO_VENT_NETO + TR.IM_IGV) - TR.IM_TOTA )  AS 'AJUSTE_REDONDEO',     " +
    "       TR.DE_PERI           AS 'PERIODO',     " +
    "       TR.CO_NEGO           AS 'NEGOCIO',            " +
    "       ' '                  AS 'REFERENCIA',     " +
    "       TR.NO_MONE_FACT      AS 'TIPO_MONEDA',     " +
    "       TN.REFERENCIA        AS 'REFERENCIAS_ADICIONALES_1',     " +
    "       ''                   AS 'REFERENCIAS_ADICIONALES_2',     " +
    "       ''                   AS 'REFERENCIAS_ADICIONALES_3',     " +
    "       ''                   AS 'REFERENCIAS_ADICIONALES_4',     " +
    "       CASE WHEN TR.CO_MONE_FACT = 1 AND TR.IM_TOTA > 700 THEN 'Cuenta Banco de la Naci\u00f3n MN Nº 00000506680'               " +
    "		WHEN TR.CO_MONE_FACT = 2 AND (TR.IM_TOTA*TC.NU_TIPO_CAMB) > 700 THEN 'Cuenta Banco de la Naci\u00f3n MN Nº 00000506680'               " +
    "	   ELSE '' END AS 'GLOSA_DETRACCION',     " +
    "       '0.18'			   AS 'IGV_2',     " +
    "       TNSP.CO_PLAN			AS 'SERVICIO',     " +
    "       ' '                  AS 'ORDEN_COMPRA',     " +
    "       '01'                 AS 'TIPO_OPERACION',     " +
    "       'RU'                 AS 'TIPO_DOC_IDENTIDAD',     " +
    "       '10'                 AS 'TIPO_AFECT_IGV',     " +
    "       '01'				   AS 'TIPO_PRECIO'     " +
    "     FROM TMCIER TC      " +
    "     INNER JOIN TMFACT TR	ON TR.CO_CIER = TC.CO_CIER      " +
    "     INNER JOIN TMCLIE TCLI ON TR.CO_CLIE = TCLI.CO_CLIE      " +
    "     INNER JOIN TMCIER_DATA_SERV_UNIC CDSU ON CDSU.CO_FACT = TR.CO_FACT     " +
    "     INNER JOIN TMPROD P	ON P.CO_PROD = TC.CO_PROD     " +
    "     INNER JOIN TMPROD_PADRE_TMPROD TPP	ON TPP.COD_PROD = P.CO_PROD     " +
    "     INNER JOIN TMNEGO TN ON TN.CO_NEGO = TR.CO_NEGO     " +
    "     INNER JOIN TMDIST TDI  ON TDI.CO_DIST = TR.CO_DIST_FISC     " +
    "     INNER JOIN TMPROV TP  ON TP.CO_PROV = TDI.CO_PROV     " +
    "     INNER JOIN TMDEPA TDE ON TDE.CO_DEPA = TP.CO_DEPA  " +
    "	 INNER JOIN TMNEGO_SUCU  TNS ON TNS.CO_NEGO = TR.CO_NEGO  " +
    "	 INNER JOIN TMNEGO_SUCU_PLAN TNSP ON TNSP.CO_NEGO_SUCU = TNS.CO_NEGO_SUCU      " +
    "    WHERE TPP.COD_PROD_PADRE = #{COD_PROD_PADRE}  " +
    "     AND TC.ST_CIER = 4  " +
    "     AND TC.NU_PERI = #{CO_PERI}  " +
    "     AND TC.NU_ANO = #{CO_ANO}  " +
    "     GROUP BY TR.CO_FACT,TC.FE_EMIS,TCLI.NO_RAZO,TCLI.CO_CLIE,     " +
    "       TR.DE_DIRE_FISC,TDI.NO_DIST,TP.NO_PROV,TDE.NO_DEPA,TCLI.DE_NUME_DOCU,  " +
    "	   TC.FE_VENC,CDSU.DE_NOMB,CDSU.IM_MONT,     " +
    "       TR.IM_VALO_VENT_NETO,TR.IM_VALO_DESC,TR.IM_IGV, TR.IM_TOTA,     " +
    "       TR.IM_AJUS_NIGV,     " +
    "       TR.DE_PERI,TR.CO_NEGO,TR.NO_MONE_FACT,TN.REFERENCIA,TNSP.CO_PLAN, TR.CO_MONE_FACT, TC.NU_TIPO_CAMB	       " +
    "     UNION     " +
    "     SELECT      " +
    "       TR.CO_FACT      AS 'CAMPO_1',     " +
    "       FORMAT(TC.FE_EMIS,'dd/MM/yyyy')           AS  'CAMPO_2',     " +
    "       TCLI.NO_RAZO         AS  'CAMPO_3',     " +
    "       TCLI.CO_CLIE         AS  'CAMPO_4',     " +
    "       TR.DE_DIRE_FISC      AS  'CAMPO_5',     " +
    "       TDI.NO_DIST		   AS 'DISTRITO',     " +
    "       TP.NO_PROV		   AS 'PROVINCIA',     " +
    "       TDE.NO_DEPA		   AS 'DEPARTAMENTO',     " +
    "       TCLI.DE_NUME_DOCU				   AS 'RUC',     " +
    "       FORMAT(TC.FE_VENC,'dd/MM/yyyy')		   AS 'FECHA_VENCIMIENTO',     " +
    "       CDSS.DE_NOMB         AS 'DESCRIPCION',      " +
    "           COUNT(1)         AS 'CANTIDAD',     " +
    "       CDSS.IM_MONT	       AS 'VALOR_UNITARIO',     " +
    "       '0.00'               AS 'DESCUENTO',    " +
    "       (COUNT(1) * CDSS.IM_MONT) AS 'TOTAL',     " +
    "       TR.IM_VALO_VENT_NETO	       AS 'OP_GRAVADAS',     " +
    "       '0'	               AS 'OP_INAFECTAS',     " +
    "       '0'	               AS 'OP_EXONERADAS',     " +
    "       '0'			       AS 'OP_GRATUITAS',     " +
    "       '0'	AS 'DESCUENTOS_GLOBALES',  " +
    "       TR.IM_IGV			   AS 'IGV',     " +
    "       '0'				   AS 'OTROS_TRIBUTOS',     " +
    "       '0'				   AS 'OTROS_CARGOS',     " +
    "       TR.IM_TOTA           AS 'TOTAL_2',     " +
    "       ((TR.IM_VALO_VENT_NETO + TR.IM_IGV) - TR.IM_TOTA )      AS 'AJUSTE_REDONDEO',     " +
    "       TR.DE_PERI           AS 'PERIODO',     " +
    "       TR.CO_NEGO           AS 'NEGOCIO',            " +
    "       ' '                  AS 'REFERENCIA',     " +
    "       TR.NO_MONE_FACT      AS 'TIPO_MONEDA',     " +
    "       TN.REFERENCIA        AS 'REFERENCIAS_ADICIONALES_1',     " +
    "       ''                   AS 'REFERENCIAS_ADICIONALES_2',     " +
    "       ''                   AS 'REFERENCIAS_ADICIONALES_3',     " +
    "       ''                   AS 'REFERENCIAS_ADICIONALES_4',     " +
    "       CASE WHEN TR.CO_MONE_FACT = 1 AND TR.IM_TOTA > 700 THEN 'Cuenta Banco de la Naci\u00f3n MN Nº 00000506680'               " +
    "		WHEN TR.CO_MONE_FACT = 2 AND (TR.IM_TOTA*TC.NU_TIPO_CAMB) > 700 THEN 'Cuenta Banco de la Naci\u00f3n MN Nº 00000506680'               " +
    "	   ELSE '' END AS 'GLOSA_DETRACCION',     " +
    "       '0.18'			   AS 'IGV_2',     " +
    "       TNSP.CO_PLAN			AS 'SERVICIO',     " +
    "       ' '                  AS 'ORDEN_COMPRA',     " +
    "       '01'                 AS 'TIPO_OPERACION',     " +
    "       'RU'                 AS 'TIPO_DOC_IDENTIDAD',     " +
    "       '10'                 AS 'TIPO_AFECT_IGV',     " +
    "       '01'				   AS 'TIPO_PRECIO'     " +
    "     FROM TMCIER TC      " +
    "     INNER JOIN TMFACT TR	ON TR.CO_CIER = TC.CO_CIER      " +
    "     INNER JOIN TMCLIE TCLI ON TR.CO_CLIE = TCLI.CO_CLIE      " +
    "     INNER JOIN TMCIER_DATA_SERV_SUPL CDSS ON CDSS.CO_FACT = TR.CO_FACT     " +
    "     INNER JOIN TMPROD P	ON P.CO_PROD = TC.CO_PROD     " +
    "     INNER JOIN TMPROD_PADRE_TMPROD TPP	ON TPP.COD_PROD = P.CO_PROD     " +
    "     INNER JOIN TMNEGO TN ON TN.CO_NEGO = TR.CO_NEGO     " +
    "     INNER JOIN TMDIST TDI  ON TDI.CO_DIST = TR.CO_DIST_FISC     " +
    "     INNER JOIN TMPROV TP  ON TP.CO_PROV = TDI.CO_PROV     " +
    "     INNER JOIN TMDEPA TDE ON TDE.CO_DEPA = TP.CO_DEPA     " +
    "	 INNER JOIN TMNEGO_SUCU  TNS ON TNS.CO_NEGO = TR.CO_NEGO  " +
    "	 INNER JOIN TMNEGO_SUCU_PLAN TNSP ON TNSP.CO_NEGO_SUCU = TNS.CO_NEGO_SUCU      " +
    "    WHERE TPP.COD_PROD_PADRE = #{COD_PROD_PADRE}  " +
    "     AND TC.ST_CIER = 4  " +
    "     AND TC.NU_PERI = #{CO_PERI}  " +
    "     AND TC.NU_ANO = #{CO_ANO}  " +
    "     GROUP BY TR.CO_FACT,TC.FE_EMIS,TCLI.NO_RAZO,TCLI.CO_CLIE,     " +
    "       TR.DE_DIRE_FISC,TDI.NO_DIST,TP.NO_PROV,TDE.NO_DEPA,TCLI.DE_NUME_DOCU,TC.FE_VENC,CDSS.DE_NOMB,CDSS.IM_MONT,     " +
    "       TR.IM_VALO_VENT_NETO,TR.IM_VALO_DESC,TR.IM_IGV,TR.IM_TOTA,     " +
    "       TR.IM_AJUS_NIGV,     " +
    "       TR.DE_PERI,TR.CO_NEGO,TR.NO_MONE_FACT,TN.REFERENCIA,TNSP.CO_PLAN, TR.CO_MONE_FACT, TC.NU_TIPO_CAMB	  " +
    "     UNION     " +
    "     SELECT      " +
    "       TR.CO_FACT         AS 'CAMPO_1',     " +
    "       FORMAT(TC.FE_EMIS,'dd/MM/yyyy')           AS  'CAMPO_2',     " +
    "       TCLI.NO_RAZO         AS  'CAMPO_3',     " +
    "       TCLI.CO_CLIE         AS  'CAMPO_4',     " +
    "       TR.DE_DIRE_FISC      AS  'CAMPO_5',     " +
    "       TDI.NO_DIST		   AS 'DISTRITO',     " +
    "       TP.NO_PROV		   AS 'PROVINCIA',     " +
    "       TDE.NO_DEPA		   AS 'DEPARTAMENTO',     " +
    "       TCLI.DE_NUME_DOCU				   AS 'RUC',       " +
    "       FORMAT(TC.FE_VENC,'dd/MM/yyyy')		   AS 'FECHA_VENCIMIENTO',     " +
    "       CDP.DE_NOMB         AS 'DESCRIPCION',       " +
    "           COUNT(1)         AS 'CANTIDAD',     " +
    "       CDP.IM_MONT	       AS 'VALOR_UNITARIO',     " +
    "	    '0.00'               AS 'DESCUENTO',         " +
    "       (COUNT(1) * CDP.IM_MONT) AS 'TOTAL',     " +
    "       TR.IM_VALO_VENT_NETO	       AS 'OP_GRAVADAS',     " +
    "       '0'	               AS 'OP_INAFECTAS',     " +
    "       '0'	               AS 'OP_EXONERADAS',     " +
    "       '0'			       AS 'OP_GRATUITAS',     " +
    "       '0'	AS 'DESCUENTOS_GLOBALES',     " +
    "       TR.IM_IGV			   AS 'IGV',     " +
    "       '0'				   AS 'OTROS_TRIBUTOS',     " +
    "       '0'				   AS 'OTROS_CARGOS',     " +
    "       TR.IM_TOTA           AS 'TOTAL_2',     " +
    "       ((TR.IM_VALO_VENT_NETO + TR.IM_IGV) - TR.IM_TOTA )      AS 'AJUSTE_REDONDEO',     " +
    "       TR.DE_PERI           AS 'PERIODO',     " +
    "       TR.CO_NEGO           AS 'NEGOCIO',            " +
    "       ' '                  AS 'REFERENCIA',     " +
    "       TR.NO_MONE_FACT      AS 'TIPO_MONEDA',     " +
    "       TN.REFERENCIA        AS 'REFERENCIAS_ADICIONALES_1',     " +
    "       ''                   AS 'REFERENCIAS_ADICIONALES_2',     " +
    "       ''                   AS 'REFERENCIAS_ADICIONALES_3',     " +
    "       ''                   AS 'REFERENCIAS_ADICIONALES_4',     " +
    "       CASE WHEN TR.CO_MONE_FACT = 1 AND TR.IM_TOTA > 700 THEN 'Cuenta Banco de la Naci\u00f3n MN Nº 00000506680'               " +
    "		WHEN TR.CO_MONE_FACT = 2 AND (TR.IM_TOTA*TC.NU_TIPO_CAMB) > 700 THEN 'Cuenta Banco de la Naci\u00f3n MN Nº 00000506680'               " +
    "	   ELSE '' END AS 'GLOSA_DETRACCION',     " +
    "       '0.18'			   AS 'IGV_2',     " +
    "       TNSP.CO_PLAN			AS 'SERVICIO',     " +
    "       ' '                  AS 'ORDEN_COMPRA',     " +
    "       '01'                 AS 'TIPO_OPERACION',     " +
    "       'RU'                 AS 'TIPO_DOC_IDENTIDAD',     " +
    "       '10'                 AS 'TIPO_AFECT_IGV',     " +
    "       '01'				   AS 'TIPO_PRECIO'     " +
    "     FROM TMCIER TC      " +
    "     INNER JOIN TMFACT TR	ON TR.CO_CIER = TC.CO_CIER      " +
    "     INNER JOIN TMCLIE TCLI ON TR.CO_CLIE = TCLI.CO_CLIE      " +
    "     INNER JOIN TMCIER_DATA_PLAN	CDP ON CDP.CO_FACT = TR.CO_FACT     " +
    "     INNER JOIN TMPROD P	ON P.CO_PROD = TC.CO_PROD     " +
    "     INNER JOIN TMPROD_PADRE_TMPROD TPP	ON TPP.COD_PROD = P.CO_PROD     " +
    "     INNER JOIN TMNEGO TN ON TN.CO_NEGO = TR.CO_NEGO     " +
    "     INNER JOIN TMDIST TDI  ON TDI.CO_DIST = TR.CO_DIST_FISC     " +
    "     INNER JOIN TMPROV TP  ON TP.CO_PROV = TDI.CO_PROV     " +
    "     INNER JOIN TMDEPA TDE ON TDE.CO_DEPA = TP.CO_DEPA     " +
    "	 INNER JOIN TMNEGO_SUCU  TNS ON TNS.CO_NEGO = TR.CO_NEGO  " +
    "	 INNER JOIN TMNEGO_SUCU_PLAN TNSP ON TNSP.CO_NEGO_SUCU = TNS.CO_NEGO_SUCU      " +
    "    WHERE TPP.COD_PROD_PADRE = #{COD_PROD_PADRE}  " +
    "     AND TC.ST_CIER = 4  " +
    "     AND TC.NU_PERI = #{CO_PERI}  " +
    "     AND TC.NU_ANO = #{CO_ANO}  " +
    "     GROUP BY TR.CO_FACT,TC.FE_EMIS,TCLI.NO_RAZO,TCLI.CO_CLIE,     " +
    "       TR.DE_DIRE_FISC,TDI.NO_DIST,TP.NO_PROV,TDE.NO_DEPA,TCLI.DE_NUME_DOCU,TC.FE_VENC,CDP.DE_NOMB,CDP.IM_MONT,     " +
    "       TR.IM_VALO_VENT_NETO,TR.IM_VALO_DESC	,TR.IM_IGV,TR.IM_TOTA,     " +
    "       TR.IM_AJUS_NIGV,     " +
    "       TR.DE_PERI,TR.CO_NEGO,TR.NO_MONE_FACT,TN.REFERENCIA,TNSP.CO_PLAN, TR.CO_MONE_FACT, TC.NU_TIPO_CAMB	       " +
    "     ORDER BY TR.CO_FACT,TR.CO_NEGO;") 
    @Results(value={
    @Result(property = "CAMPO_1",column="CAMPO_1",id = false),
    @Result(property = "CAMPO_2",column="CAMPO_2",id = false),
    @Result(property = "CAMPO_3",column="CAMPO_3",id = false),
    @Result(property = "CAMPO_4",column="CAMPO_4",id = false),
    @Result(property = "CAMPO_5",column="CAMPO_5",id = false),
    @Result(property = "DISTRITO",column="DISTRITO",id = false),
    @Result(property = "PROVINCIA",column="PROVINCIA",id = false),
    @Result(property = "DEPARTAMENTO",column="DEPARTAMENTO",id = false),
    @Result(property = "RUC",column="RUC",id = false),
    @Result(property = "FECHA_VENCIMIENTO",column="FECHA_VENCIMIENTO",id = false),
    @Result(property = "DESCRIPCION",column="DESCRIPCION",id = false),
    @Result(property = "CANTIDAD",column="CANTIDAD",id = false),
    @Result(property = "VALOR_UNITARIO",column="VALOR_UNITARIO",id = false),
    @Result(property = "DESCUENTO",column="DESCUENTO",id = false),
    @Result(property = "TOTAL",column="TOTAL",id = false),
    @Result(property = "OP_GRAVADAS",column="OP_GRAVADAS",id = false),
    @Result(property = "OP_INAFECTAS",column="OP_INAFECTAS",id = false),
    @Result(property = "OP_EXONERADAS",column="OP_EXONERADAS",id = false),
    @Result(property = "OP_GRATUITAS",column="OP_GRATUITAS",id = false),
    @Result(property = "DESCUENTOS_GLOBALES",column="DESCUENTOS_GLOBALES",id = false),
    @Result(property = "IGV",column="IGV",id = false),
    @Result(property = "OTROS_TRIBUTOS",column="OTROS_TRIBUTOS",id = false),
    @Result(property = "OTROS_CARGOS",column="OTROS_CARGOS",id = false),
    @Result(property = "TOTAL_2",column="TOTAL_2",id = false),
    @Result(property = "AJUSTE_REDONDEO",column="AJUSTE_REDONDEO",id = false),
    @Result(property = "PERIODO",column="PERIODO",id = false),
    @Result(property = "NEGOCIO",column="NEGOCIO",id = false),
    @Result(property = "REFERENCIA",column="REFERENCIA",id = false),
    @Result(property = "TIPO_MONEDA",column="TIPO_MONEDA",id = false),
    @Result(property = "REFERENCIAS_ADICIONALES_1",column="REFERENCIAS_ADICIONALES_1",id = false),
    @Result(property = "REFERENCIAS_ADICIONALES_2",column="REFERENCIAS_ADICIONALES_2",id = false),
    @Result(property = "REFERENCIAS_ADICIONALES_3",column="REFERENCIAS_ADICIONALES_3",id = false),
    @Result(property = "REFERENCIAS_ADICIONALES_4",column="REFERENCIAS_ADICIONALES_4",id = false),
    @Result(property = "GLOSA_DETRACCION",column="GLOSA_DETRACCION",id = false),
    @Result(property = "IGV_2",column="IGV_2",id = false),
    @Result(property = "SERVICIO",column="SERVICIO",id = false),
    @Result(property = "ORDEN_COMPRA",column="ORDEN_COMPRA",id = false),
    @Result(property = "TIPO_OPERACION",column="TIPO_OPERACION",id = false),
    @Result(property = "TIPO_DOC_IDENTIDAD",column="TIPO_DOC_IDENTIDAD",id = false),
    @Result(property = "TIPO_AFECT_IGV",column="TIPO_AFECT_IGV",id = false),
    @Result(property = "TIPO_PRECIO",column="TIPO_PRECIO",id = false)
    }) 
    public List<REPORTE_FACTURA> consultaReporteGerenteSunatFactura(@Param("COD_PROD_PADRE") Integer COD_PROD_PADRE, @Param("CO_PERI") Integer CO_PERI , @Param("CO_ANO")  Integer CO_ANO);
    //Integer COD_PROD_PADRE, Integer CO_ANO, Integer CO_PERI
}
