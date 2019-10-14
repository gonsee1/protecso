/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.mappers;

import com.americatel.facturacion.fncs.Pagination;
import com.americatel.facturacion.historial.Historial;
import com.americatel.facturacion.models.CIER;
import com.americatel.facturacion.models.CIER_DATA_PLAN;
import com.americatel.facturacion.models.CIER_DATA_SUCU;
import com.americatel.facturacion.models.CLIE;
import com.americatel.facturacion.models.DIST;
import com.americatel.facturacion.models.FACT;
import com.americatel.facturacion.models.RECI;
import com.americatel.facturacion.models.NEGO;
import com.americatel.facturacion.models.NEGO_SALD;
import com.americatel.facturacion.models.SUCU;
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
public interface mapRECI {
    
    @Select("SELECT * FROM TMRECI WHERE CO_RECI=#{CO_RECI} AND ST_ELIM=0;")
    @Results(value={
        @Result(property = "CO_RECI",column="CO_RECI",id = true),
        @Result(property = "CO_DIST_FISC",column="CO_DIST_FISC",id = false),
        @Result(property = "CO_CLIE",column="CO_CLIE",id = false),
        @Result(property = "CO_NEGO",column="CO_NEGO",id = false),
        @Result(property = "CO_CIER",column="CO_CIER",id = false),
        @Result(property = "dist_fisc",column = "CO_DIST_FISC",javaType = DIST.class,one = @One(select = "com.americatel.facturacion.mappers.mapDIST.getId"),id = false),
        @Result(property = "clie",column = "CO_CLIE",javaType = CLIE.class,one = @One(select = "com.americatel.facturacion.mappers.mapCLIE.getId"),id = false),
        @Result(property = "nego",column = "CO_NEGO",javaType = NEGO.class,one = @One(select = "com.americatel.facturacion.mappers.mapNEGO.getOnlyId"),id = false),
        @Result(property = "cier",column = "CO_CIER",javaType = CIER.class,one = @One(select = "com.americatel.facturacion.mappers.mapCIER.getId"),id = false),
        @Result(property = "cantidad_sucursales_facturaron",column = "CO_RECI",javaType = Integer.class,one = @One(select = "com.americatel.facturacion.mappers.mapRECI.getCantidadSucursalesFacturaronByReci"),id = false)
    }) 
    public RECI getById(Long CO_RECI);    
    
    @Insert("INSERT INTO TMRECI"+
            "(CO_RECI,CO_CIER,CO_NEGO,DE_PERI,IM_NETO,IM_AJUS_SIGV,IM_IGV,IM_TOTA,IM_AJUS_NIGV,IM_INST,IM_SERV_MENS,IM_ALQU,IM_OTRO,IM_DESC,FE_EMIS,FE_VENC,FE_INI,FE_FIN,CO_CLIE,DE_CODI_BUM,NO_CLIE,DE_DIRE_FISC,CO_DIST_FISC,CO_TIPO_DOCU,DE_NUME_DOCU,DE_DIRE_CORR,DE_REF_DIRE_CORR,CO_DIST_CORR,DE_DIRE_INST,CO_DIST_INST,NO_MONE_FACT,CO_MONE_FACT,NO_DIST_FISC,NO_TIPO_DOCU,NO_DIST_CORR,NO_DIST_INST,DE_SIMB_MONE_FACT,ST_ANUL,IM_DIF_CARGO_FIJO,"+Historial.stringMapperInsertarTitles+") VALUES "+
            "(#{CO_RECI},#{CO_CIER},#{CO_NEGO},#{DE_PERI},#{IM_NETO},#{IM_AJUS_SIGV},#{IM_IGV},#{IM_TOTA},#{IM_AJUS_NIGV},#{IM_INST},#{IM_SERV_MENS},#{IM_ALQU},#{IM_OTRO},#{IM_DESC},#{FE_EMIS},#{FE_VENC},#{FE_INI},#{FE_FIN},#{CO_CLIE},#{DE_CODI_BUM},#{NO_CLIE},#{DE_DIRE_FISC},#{CO_DIST_FISC},#{CO_TIPO_DOCU},#{DE_NUME_DOCU},#{DE_DIRE_CORR},#{DE_REF_DIRE_CORR},#{CO_DIST_CORR},#{DE_DIRE_INST},#{CO_DIST_INST},#{NO_MONE_FACT},#{CO_MONE_FACT},#{NO_DIST_FISC},#{NO_TIPO_DOCU},#{NO_DIST_CORR},#{NO_DIST_INST},#{DE_SIMB_MONE_FACT},#{ST_ANUL},#{IM_DIF_CARGO_FIJO},"+Historial.stringMapperInsertarValuesNumeral+");")
    //@SelectKey(statement="SELECT @@IDENTITY;", keyProperty="CO_RECI", before=false, resultType=Long.class)
    public void insertar(RECI f);

    @Select("SELECT * FROM TMRECI WHERE CO_CIER=#{CO_CIER} AND ST_ELIM=0 AND ST_ANUL=0;")
    @Results(value={
        @Result(property = "CO_RECI",column="CO_RECI",id = true),
        @Result(property = "CO_DIST_CORR",column="CO_DIST_CORR",id = false),
        @Result(property = "CO_DIST_FISC",column="CO_DIST_FISC",id = false),
        @Result(property = "CO_DIST_INST",column="CO_DIST_INST",id = false),
        @Result(property = "dist_corr",column = "CO_DIST_CORR",javaType = DIST.class,one = @One(select = "com.americatel.facturacion.mappers.mapDIST.getId"),id = false),
        @Result(property = "dist_fisc",column = "CO_DIST_FISC",javaType = DIST.class,one = @One(select = "com.americatel.facturacion.mappers.mapDIST.getId"),id = false),
        @Result(property = "dist_inst",column = "CO_DIST_INST",javaType = DIST.class,one = @One(select = "com.americatel.facturacion.mappers.mapDIST.getId"),id = false),
        @Result(property = "cantidad_sucursales_facturaron",column = "CO_RECI",javaType = Integer.class,one = @One(select = "com.americatel.facturacion.mappers.mapRECI.getCantidadSucursalesFacturaronByReci"),id = false),
        @Result(property = "is_afecto_detraccion",column = "CO_RECI",javaType = Boolean.class,one = @One(select = "com.americatel.facturacion.mappers.mapRECI.isAfectoDetraccionByCO_RECI"),id = false),    
        @Result(property = "detalle_servicio",column = "CO_RECI",javaType = Boolean.class,one = @One(select = "com.americatel.facturacion.mappers.mapRECI_GLOS.getDetalleServicioByRECI"),id = false)    
    })  
    public List<RECI> getRecibos(CIER cier);
    
    @Select("SELECT * FROM TMRECI WHERE CO_CIER=#{CO_CIER} AND ST_ELIM=0 AND ST_ANUL=1;")
    @Results(value={
        @Result(property = "CO_RECI",column="CO_RECI",id = true),
        @Result(property = "CO_DIST_CORR",column="CO_DIST_CORR",id = false),
        @Result(property = "CO_DIST_FISC",column="CO_DIST_FISC",id = false),
        @Result(property = "CO_DIST_INST",column="CO_DIST_INST",id = false),
        @Result(property = "dist_corr",column = "CO_DIST_CORR",javaType = DIST.class,one = @One(select = "com.americatel.facturacion.mappers.mapDIST.getId"),id = false),
        @Result(property = "dist_fisc",column = "CO_DIST_FISC",javaType = DIST.class,one = @One(select = "com.americatel.facturacion.mappers.mapDIST.getId"),id = false),
        @Result(property = "dist_inst",column = "CO_DIST_INST",javaType = DIST.class,one = @One(select = "com.americatel.facturacion.mappers.mapDIST.getId"),id = false),
        @Result(property = "cantidad_sucursales_facturaron",column = "CO_RECI",javaType = Integer.class,one = @One(select = "com.americatel.facturacion.mappers.mapRECI.getCantidadSucursalesFacturaronByReci"),id = false),
        @Result(property = "is_afecto_detraccion",column = "CO_RECI",javaType = Boolean.class,one = @One(select = "com.americatel.facturacion.mappers.mapRECI.isAfectoDetraccionByCO_RECI"),id = false),    
        @Result(property = "detalle_servicio",column = "CO_RECI",javaType = Boolean.class,one = @One(select = "com.americatel.facturacion.mappers.mapRECI_GLOS.getDetalleServicioByRECI"),id = false)    
    })  
    public List<RECI> getRecibosAnulados(CIER cier);
    
    @Select("SELECT * FROM TMNEGO WHERE CO_NEGO=#{CO_NEGO} AND ST_ELIM=0;")
    public NEGO getNegocio(RECI aThis);

    @Select("SELECT top 1 s.* FROM TMRECI r"
            + " INNER JOIN TMRECI_GLOS g ON g.CO_RECI=r.CO_RECI AND g.ST_ELIM=0"
            + " INNER JOIN TMNEGO_SUCU ns ON ns.CO_NEGO_SUCU=g.CO_NEGO_SUCU"
            + " INNER JOIN TMSUCU s ON s.CO_SUCU=ns.CO_SUCU"
            + " WHERE r.CO_RECI=#{CO_RECI} AND r.ST_ELIM=0")
    public SUCU getPrimerSucursalFacturada(RECI reci);

    @Select("SELECT SUM (W.p) FROM ("
            + " SELECT 1 as p FROM TMRECI r"
            + " INNER JOIN TMRECI_GLOS g ON g.CO_RECI=r.CO_RECI AND g.ST_ELIM=0"
            + " WHERE r.CO_RECI=#{CO_RECI} AND g.TI_GLOS=1 AND r.ST_ELIM=0"
            + " GROUP BY g.CO_NEGO_SUCU"
            + " ) as W")    
    public Integer getCantidadSucursalesFacturaronByReci(Long CO_RECI);

    @Select(" SELECT count(*) FROM TMCIER_DATA_PLAN WHERE ST_TIPO_COBR=1 AND CO_RECI=#{CO_RECI}")
    public Integer getCantidadSucursalesFacturaronByCO_RECI(Long CO_RECI); 
    
    @Select("SELECT count(*) FROM ( "
            + "SELECT CO_RECI FROM TMCIER_DATA_SERV_SUPL WHERE CO_FACT IS NOT NULL AND CO_RECI=#{CO_RECI} UNION " 
            + "SELECT CO_RECI FROM TMCIER_DATA_SERV_UNIC WHERE CO_FACT IS NOT NULL AND CO_RECI=#{CO_RECI} UNION " 
            + "SELECT CO_RECI FROM TMCIER_DATA_PLAN WHERE CO_FACT IS NOT NULL AND CO_RECI=#{CO_RECI} UNION " 
            + "SELECT CO_RECI FROM TMCIER_DATA_PROM_MONT WHERE CO_FACT IS NOT NULL AND CO_RECI=#{CO_RECI} "
            + ") as W")
    public Boolean isAfectoDetraccionByCO_RECI(Long CO_RECI);     
    
    @Select("SELECT r.* FROM TMRECI r INNER JOIN TMCIER c ON c.CO_CIER=r.CO_CIER"
            + " AND (c.NU_ANO=#{CO_ANO} or ISNULL(#{CO_ANO},-1)=-1)"
            + " AND (c.NU_PERI=#{CO_PERI} or ISNULL(#{CO_PERI},-1)=-1)"
            + " AND (r.CO_RECI like '%${DE_BUSC}%' or r.CO_NEGO like '%${DE_BUSC}%' or ISNULL(#{DE_BUSC},-1)=-1)"
            + " AND (c.ST_CIER<>4);")
    @Results(value={
        @Result(property = "CO_RECI",column="CO_RECI",id = true),
        @Result(property = "CO_CIER",column="CO_CIER",id = false),
        @Result(property = "CO_DIST_CORR",column="CO_DIST_CORR",id = false),
        @Result(property = "CO_DIST_FISC",column="CO_DIST_FISC",id = false),
        @Result(property = "CO_DIST_INST",column="CO_DIST_INST",id = false),        
        @Result(property = "cier",column = "CO_CIER",javaType = CIER.class,one = @One(select = "com.americatel.facturacion.mappers.mapCIER.getId"),id = false),
        @Result(property = "dist_corr",column = "CO_DIST_CORR",javaType = DIST.class,one = @One(select = "com.americatel.facturacion.mappers.mapDIST.getId"),id = false),
        @Result(property = "dist_fisc",column = "CO_DIST_FISC",javaType = DIST.class,one = @One(select = "com.americatel.facturacion.mappers.mapDIST.getId"),id = false),
        @Result(property = "dist_inst",column = "CO_DIST_INST",javaType = DIST.class,one = @One(select = "com.americatel.facturacion.mappers.mapDIST.getId"),id = false),
        @Result(property = "cantidad_sucursales_facturaron",column = "CO_RECI",javaType = Integer.class,one = @One(select = "com.americatel.facturacion.mappers.mapRECI.getCantidadSucursalesFacturaronByReci"),id = false)        ,
        @Result(property = "is_afecto_detraccion",column = "CO_RECI",javaType = Boolean.class,one = @One(select = "com.americatel.facturacion.mappers.mapRECI.isAfectoDetraccionByCO_RECI"),id = false)        
    })    
    public List<RECI> findRecibo(@Param("CO_PERI") Integer CO_PERI,@Param("CO_ANO") Integer CO_ANO,@Param("DE_BUSC") String DE_BUSC);
    
    @Select("SELECT r.* FROM TMRECI r INNER JOIN TMCIER c ON c.CO_CIER=r.CO_CIER"
            + " AND (r.CO_RECI like '%${SEARCH}%' or r.CO_NEGO like '%${SEARCH}%')"
            + " AND (c.ST_CIER<>4) AND r.ST_ANUL=0 ORDER BY r.CO_RECI"
            + " OFFSET ${Pagination.getGroup()} ROWS FETCH NEXT #{Pagination.Limit} ROWS ONLY;")
    @Results(value={
        @Result(property = "CO_RECI",column="CO_RECI",id = true),
        @Result(property = "CO_CIER",column="CO_CIER",id = false),
        @Result(property = "CO_DIST_CORR",column="CO_DIST_CORR",id = false),
        @Result(property = "CO_DIST_FISC",column="CO_DIST_FISC",id = false),
        @Result(property = "CO_DIST_INST",column="CO_DIST_INST",id = false),        
        @Result(property = "cier",column = "CO_CIER",javaType = CIER.class,one = @One(select = "com.americatel.facturacion.mappers.mapCIER.getId"),id = false),
        @Result(property = "dist_corr",column = "CO_DIST_CORR",javaType = DIST.class,one = @One(select = "com.americatel.facturacion.mappers.mapDIST.getId"),id = false),
        @Result(property = "dist_fisc",column = "CO_DIST_FISC",javaType = DIST.class,one = @One(select = "com.americatel.facturacion.mappers.mapDIST.getId"),id = false),
        @Result(property = "dist_inst",column = "CO_DIST_INST",javaType = DIST.class,one = @One(select = "com.americatel.facturacion.mappers.mapDIST.getId"),id = false),
        @Result(property = "cantidad_sucursales_facturaron",column = "CO_RECI",javaType = Integer.class,one = @One(select = "com.americatel.facturacion.mappers.mapRECI.getCantidadSucursalesFacturaronByReci"),id = false)        ,
        @Result(property = "is_afecto_detraccion",column = "CO_RECI",javaType = Boolean.class,one = @One(select = "com.americatel.facturacion.mappers.mapRECI.isAfectoDetraccionByCO_RECI"),id = false)        
    })    
    public List<RECI> selectBySEARCH(@Param("SEARCH") String SEARCH,@Param("Pagination")Pagination pagination);
    
    @Select("SELECT count(*) FROM TMRECI r INNER JOIN TMCIER c ON c.CO_CIER=r.CO_CIER"
            + " AND (r.CO_RECI like '%${SEARCH}%' or r.CO_NEGO like '%${SEARCH}%')"
            + " AND (c.ST_CIER<>4) AND r.ST_ANUL=0")
    public int getNumResultsBySEARCH(@Param("SEARCH") String SEARCH);
    
    @Update("UPDATE TMRECI SET DE_PERI=#{DE_PERI},"
            + "FE_EMIS=#{FE_EMIS},FE_VENC=#{FE_VENC},FE_INI=#{FE_INI},FE_FIN=#{FE_FIN},DE_CODI_BUM=#{DE_CODI_BUM},NO_CLIE=#{NO_CLIE},"
            + "DE_DIRE_FISC=#{DE_DIRE_FISC},CO_DIST_FISC=#{CO_DIST_FISC},CO_TIPO_DOCU=#{CO_TIPO_DOCU},DE_NUME_DOCU=#{DE_NUME_DOCU},"
            + "DE_DIRE_CORR=#{DE_DIRE_CORR},DE_REF_DIRE_CORR=#{DE_REF_DIRE_CORR},CO_DIST_CORR=#{CO_DIST_CORR},DE_DIRE_INST=#{DE_DIRE_INST},"
            + "CO_DIST_INST=#{CO_DIST_INST},NO_MONE_FACT=#{NO_MONE_FACT},CO_MONE_FACT=#{CO_MONE_FACT},NO_DIST_FISC=#{NO_DIST_FISC},"
            + "NO_TIPO_DOCU=#{NO_TIPO_DOCU},NO_DIST_CORR=#{NO_DIST_CORR},NO_DIST_INST=#{NO_DIST_INST},DE_SIMB_MONE_FACT=#{DE_SIMB_MONE_FACT},"+Historial.stringMapperModificar+""
            + "WHERE CO_RECI=#{CO_RECI};")
    public void update(RECI reci);
    
    @Update("UPDATE TMRECI SET IM_NETO=#{IM_NETO},"
            + "IM_AJUS_SIGV=#{IM_AJUS_SIGV},IM_IGV=#{IM_IGV},IM_AJUS_NIGV=#{IM_AJUS_NIGV},"
            + "IM_TOTA=#{IM_TOTA},IM_INST=#{IM_INST},IM_ALQU=#{IM_ALQU},IM_OTRO=#{IM_OTRO},"
            + "IM_DESC=#{IM_DESC},IM_SERV_MENS=#{IM_SERV_MENS},IM_DIF_CARGO_FIJO=#{IM_DIF_CARGO_FIJO},"+Historial.stringMapperModificar+""
            + "WHERE CO_RECI=#{CO_RECI};")
    public void updateMontos(RECI reci);

    @Update("UPDATE TMRECI SET ST_ANUL=1,"+Historial.stringMapperModificar+" WHERE CO_RECI=#{CO_RECI} AND ST_ELIM=0;")
    public void anular(RECI reci);

    @Select("SELECT TOP 1 r.* FROM TMRECI r INNER JOIN TMCIER c ON c.CO_CIER=r.CO_CIER AND c.ST_CIER=4 ORDER BY r.CO_RECI DESC;")
    public RECI getUltimoGeneradoGeneradaCierreCerrado();
    
    @Select("SELECT * FROM TMRECI WHERE CO_CIER=#{CO_CIER} AND CO_NEGO=#{CO_NEGO} ORDER BY CO_RECI DESC;")
    public List<RECI> getRecibosByCierreAndNego(@Param("CO_CIER") Integer CO_CIER,@Param("CO_NEGO") Integer CO_NEGO);
    
    @Select("SELECT TOP 1 * FROM TMCIER_DATA_PLAN WHERE CO_RECI=#{CO_RECI} AND ST_TIPO_COBR=2;")
    public CIER_DATA_PLAN getPlanFromDiferencial(@Param("CO_RECI") Long CO_RECI);
    
    @Select("SELECT count(*) FROM TMCIER_DATA_PLAN WHERE CO_RECI=#{CO_RECI} AND ST_TIPO_COBR=2;")
    public Integer getCantidadPuntos(@Param("CO_RECI") Long CO_RECI);

    @Select("SELECT TOP 1 * FROM TMCIER_DATA_SERV_SUPL WHERE CO_RECI=#{CO_RECI};")
    public CIER_DATA_SUCU getPlanDescription_SS(@Param("CO_RECI") Long CO_RECI);
}