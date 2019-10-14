/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.mappers;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.americatel.facturacion.historial.Historial;
import com.americatel.facturacion.models.NEGO_SUCU;
import com.americatel.facturacion.models.SUSP;

/**
 *
 * @author crodas
 */
public interface mapSUSP { 

    @Insert("INSERT INTO TMSUSP (CO_NEGO_SUCU,CO_PROD,ST_SOAR,CO_OIT_INST,CO_CIRC,FE_INIC,FE_FIN,CO_CIER_INIC,CO_CIER_FIN,"+Historial.stringMapperInsertarTitles+") VALUES (#{CO_NEGO_SUCU},#{CO_PROD},#{ST_SOAR},#{CO_OIT_INST},#{CO_CIRC},#{FE_INIC},#{FE_FIN},#{CO_CIER_INIC},#{CO_CIER_FIN},"+Historial.stringMapperInsertarValuesNumeral+")")
    public void insert(SUSP susp);    


    
    @Select("SELECT *, CASE WHEN (CO_CIER_INIC is NULL OR (CO_CIER_FIN is null AND not FE_FIN is null)) THEN 'PENDIENTE' " +
    "   ELSE 'FACTURADO' END AS DE_ESTADO FROM TMSUSP WHERE CO_NEGO_SUCU=#{CO_NEGO_SUCU} AND ST_ELIM=0 ORDER BY FE_INIC ASC")
    @Results(value = {
        @Result(column = "CO_SUSP",property = "CO_SUSP",id = true),
        @Result(column = "CO_NEGO_SUCU",property = "CO_NEGO_SUCU",id = true),
        @Result(column = "CO_PROD",property = "CO_PROD",id = true),
        @Result(column = "ST_SOAR",property = "ST_SOAR",id = true),
        @Result(column = "CO_OIT_INST",property = "CO_OIT_INST",id = true),
        @Result(column = "CO_CIRC",property = "CO_CIRC",id = true),
        @Result(column = "FE_INIC",property = "FE_INIC",id = true),
        @Result(column = "FE_FIN",property = "FE_FIN",id = true),
        @Result(column = "CO_CIER_INIC",property = "CO_CIER_INIC",id = true),
        @Result(column = "CO_CIER_FIN",property = "CO_CIER_FIN",id = true),
        @Result(property = "ST_ELIM",column="ST_ELIM"),
            
        @Result(property = "nego_sucu",column = "CO_NEGO_SUCU",javaType = NEGO_SUCU.class,one = @One(select = "com.americatel.facturacion.mappers.mapNEGO_SUCU.getOnlyId"),id = false),                    
    })
    public List<SUSP> selectByNegoSucu(Integer CO_NEGO_SUCU);
    
    @Select("SELECT * FROM TMSUSP WHERE CO_NEGO_SUCU=#{CO_NEGO_SUCU} AND FE_INIC=#{FE_INIC} AND ST_ELIM=0")
    @Results(value = {
        @Result(column = "CO_SUSP",property = "CO_SUSP",id = true),
        @Result(column = "CO_NEGO_SUCU",property = "CO_NEGO_SUCU",id = true),
        @Result(column = "CO_PROD",property = "CO_PROD",id = true),
        @Result(column = "ST_SOAR",property = "ST_SOAR",id = true),
        @Result(column = "CO_OIT_INST",property = "CO_OIT_INST",id = true),
        @Result(column = "CO_CIRC",property = "CO_CIRC",id = true),
        @Result(column = "FE_INIC",property = "FE_INIC",id = true),
        @Result(column = "FE_FIN",property = "FE_FIN",id = true),
        @Result(column = "CO_CIER_INIC",property = "CO_CIER_INIC",id = true),
        @Result(column = "CO_CIER_FIN",property = "CO_CIER_FIN",id = true),
        @Result(property = "ST_ELIM",column="ST_ELIM"),
            
        @Result(property = "nego_sucu",column = "CO_NEGO_SUCU",javaType = NEGO_SUCU.class,one = @One(select = "com.americatel.facturacion.mappers.mapNEGO_SUCU.getOnlyId"),id = false),                    
    })    
    public List<SUSP> selectByNegoSucuAndInicio(@Param("CO_NEGO_SUCU") int CO_NEGO,@Param("FE_INIC") Date FE_INIC);    
    
    @Delete("DELETE FROM TMSUSP WHERE CO_CIER_INIC is null and CO_PROD=#{CO_PROD};UPDATE TMSUSP SET FE_FIN=null WHERE FE_FIN is null and CO_CIER_FIN is not null and CO_PROD=#{CO_PROD};")
    public int quitarPendientes(@Param("CO_PROD")int CO_PROD);//Para volverlor a cargar
    
    @Select("SELECT count(*) FROM TMSUSP WHERE CO_PROD=#{CO_PROD} AND (CO_CIER_INIC is NULL OR (CO_CIER_FIN is null AND not FE_FIN is null)) AND ST_ELIM=0")
    public int getCantidadSuspensionesCargadosByProducto(@Param("CO_PROD")int CO_PROD);
    
    
    @Select("SELECT count(*) FROM TMSUSP WHERE (CO_CIER_INIC is NULL OR (CO_CIER_FIN is null AND not FE_FIN is null)) AND ST_ELIM=0")
    public int getCantidadSuspensionesCargados();
        
    @Update("UPDATE TMSUSP SET CO_NEGO_SUCU=#{CO_NEGO_SUCU},"+Historial.stringMapperModificar+" WHERE CO_SUSP=#{CO_SUSP} AND ST_ELIM=0;")
    public int cambiarSucursal(SUSP susp);
    
    @Update("UPDATE TMSUSP SET CO_PROD=#{CO_PROD},FE_FIN=#{FE_FIN},CO_CIER_INIC=#{CO_CIER_INIC},CO_CIER_FIN=#{CO_CIER_FIN},"+Historial.stringMapperModificar+" WHERE CO_SUSP=#{CO_SUSP} AND ST_ELIM=0;")
    public int update(SUSP susp);
    
    @Select("SELECT * FROM TMSUSP WHERE CO_PROD=#{CO_PROD} AND (CO_CIER_INIC is NULL OR (CO_CIER_FIN is null AND not FE_FIN is null)) AND ST_ELIM=0")
    @Results(value = {
        @Result(column = "CO_SUSP",property = "CO_SUSP",id = true),
        @Result(column = "CO_NEGO_SUCU",property = "CO_NEGO_SUCU",id = true),
        @Result(column = "CO_PROD",property = "CO_PROD",id = true),
        @Result(column = "ST_SOAR",property = "ST_SOAR",id = true),
        @Result(column = "CO_OIT_INST",property = "CO_OIT_INST",id = true),
        @Result(column = "CO_CIRC",property = "CO_CIRC",id = true),
        @Result(column = "FE_INIC",property = "FE_INIC",id = true),
        @Result(column = "FE_FIN",property = "FE_FIN",id = true),
        @Result(column = "CO_CIER_INIC",property = "CO_CIER_INIC",id = true),
        @Result(column = "CO_CIER_FIN",property = "CO_CIER_FIN",id = true),
        @Result(property = "ST_ELIM",column="ST_ELIM"),
        @Result(property = "nego_sucu",column = "CO_NEGO_SUCU",javaType = NEGO_SUCU.class,one = @One(select = "com.americatel.facturacion.mappers.mapNEGO_SUCU.getOnlyId"),id = false),                    
    })    
    public List<SUSP> selectSuspensionesPendientesByProducto(@Param("CO_PROD")int CO_PROD); 
    
    @Select("SELECT * FROM TMSUSP WHERE (CO_CIER_INIC is NULL OR (CO_CIER_FIN is null AND not FE_FIN is null)) AND ST_ELIM=0")
    @Results(value = {
        @Result(column = "CO_SUSP",property = "CO_SUSP",id = true),
        @Result(column = "CO_NEGO_SUCU",property = "CO_NEGO_SUCU",id = true),
        @Result(column = "CO_PROD",property = "CO_PROD",id = true),
        @Result(column = "ST_SOAR",property = "ST_SOAR",id = true),
        @Result(column = "CO_OIT_INST",property = "CO_OIT_INST",id = true),
        @Result(column = "CO_CIRC",property = "CO_CIRC",id = true),
        @Result(column = "FE_INIC",property = "FE_INIC",id = true),
        @Result(column = "FE_FIN",property = "FE_FIN",id = true),
        @Result(column = "CO_CIER_INIC",property = "CO_CIER_INIC",id = true),
        @Result(column = "CO_CIER_FIN",property = "CO_CIER_FIN",id = true),
        @Result(property = "ST_ELIM",column="ST_ELIM"),
        @Result(property = "nego_sucu",column = "CO_NEGO_SUCU",javaType = NEGO_SUCU.class,one = @One(select = "com.americatel.facturacion.mappers.mapNEGO_SUCU.getOnlyId"),id = false),                    
    })    
    public List<SUSP> selectSuspensionesPendientes();     
    
    @Select("SELECT * FROM TMSUSP WHERE ST_ELIM=0")
    @Results(value = {
        @Result(column = "CO_SUSP",property = "CO_SUSP",id = true),
        @Result(column = "CO_NEGO_SUCU",property = "CO_NEGO_SUCU",id = true),
        @Result(column = "CO_PROD",property = "CO_PROD",id = true),
        @Result(column = "ST_SOAR",property = "ST_SOAR",id = true),
        @Result(column = "CO_OIT_INST",property = "CO_OIT_INST",id = true),
        @Result(column = "CO_CIRC",property = "CO_CIRC",id = true),
        @Result(column = "FE_INIC",property = "FE_INIC",id = true),
        @Result(column = "FE_FIN",property = "FE_FIN",id = true),
        @Result(column = "CO_CIER_INIC",property = "CO_CIER_INIC",id = true),
        @Result(column = "CO_CIER_FIN",property = "CO_CIER_FIN",id = true),
        @Result(property = "ST_ELIM",column="ST_ELIM"),
        @Result(property = "nego_sucu",column = "CO_NEGO_SUCU",javaType = NEGO_SUCU.class,one = @One(select = "com.americatel.facturacion.mappers.mapNEGO_SUCU.getOnlyId"),id = false),                    
    })    
    public List<SUSP> selectAllSuspensiones();
    
    /*
    @Select("SELECT *, CASE WHEN (CO_CIER_INIC is NULL OR (CO_CIER_FIN is null AND not FE_FIN is null)) THEN 'PENDIENTE' " +
    "ELSE 'FACTURADO' END AS DE_ESTADO FROM TMSUSP WHERE ST_ELIM=0")
    */
    @Select("SELECT *, CASE WHEN (CO_CIER_INIC is NULL OR (CO_CIER_FIN is null AND not FE_FIN is null)) THEN 'PENDIENTE' " + 
    "ELSE 'FACTURADO' END AS DE_ESTADO " +
    "FROM TMSUSP SUS INNER JOIN TMPROD_PADRE_TMPROD PR_P ON SUS.CO_PROD =  PR_P.COD_PROD " +
    "INNER JOIN TMPROD_PADRE PR_PA ON PR_P.COD_PROD_PADRE = PR_PA.COD_PROD_PADRE " +
    "WHERE SUS.ST_ELIM=0") 
    @Results(value = {
        @Result(column = "CO_SUSP",property = "CO_SUSP",id = true),
        @Result(column = "CO_NEGO_SUCU",property = "CO_NEGO_SUCU",id = true),
        @Result(column = "CO_PROD",property = "CO_PROD",id = true),
        @Result(column = "ST_SOAR",property = "ST_SOAR",id = true),
        @Result(column = "CO_OIT_INST",property = "CO_OIT_INST",id = true),
        @Result(column = "CO_CIRC",property = "CO_CIRC",id = true),
        @Result(column = "FE_INIC",property = "FE_INIC",id = true),
        @Result(column = "FE_FIN",property = "FE_FIN",id = true),
        @Result(column = "CO_CIER_INIC",property = "CO_CIER_INIC",id = true),
        @Result(column = "CO_CIER_FIN",property = "CO_CIER_FIN",id = true),
        @Result(property = "ST_ELIM",column="ST_ELIM"),
        @Result(property = "DE_ESTADO",column="DE_ESTADO"),    
        @Result(property = "COD_PROD_PADRE",column="COD_PROD_PADRE"),    
        @Result(property = "nego_sucu",column = "CO_NEGO_SUCU",javaType = NEGO_SUCU.class,one = @One(select = "com.americatel.facturacion.mappers.mapNEGO_SUCU.getOnlyId"),id = false),                    
    })    
    public List<SUSP> selectAllSuspensionesConEstado();
/*
    @Update("UPDATE TMSUSP SET FE_FIN=#{FE_FIN},CO_CIER_INIC=#{CO_CIER_INIC},CO_CIER_FIN=#{CO_CIER_FIN} WHERE CO_SUSP=#{CO_SUSP};")
    public int update(SUSP c);   */ 

    
    @Update("UPDATE TMSUSP SET CO_CIER_INIC=#{CO_CIER},CO_CIER_FIN=#{CO_CIER} WHERE CO_PROD=#{CO_PROD} and CO_CIER_INIC is null and CO_CIER_FIN is null and FE_INIC is not null and FE_FIN is not null;"+
            "UPDATE TMSUSP SET CO_CIER_INIC=#{CO_CIER} WHERE CO_PROD=#{CO_PROD} AND CO_CIER_INIC IS NULL AND CO_CIER_FIN IS NULL AND FE_INIC IS NOT NULL AND FE_FIN IS NULL;"+
            "UPDATE TMSUSP SET CO_CIER_FIN=#{CO_CIER} WHERE CO_PROD=#{CO_PROD} AND CO_CIER_INIC IS NOT NULL AND CO_CIER_FIN IS NULL AND FE_INIC IS NOT NULL AND FE_FIN IS NOT NULL;"
            )    
    public void congelarPostCierre(@Param("CO_PROD") Integer CO_PROD,@Param("CO_CIER") Integer CO_CIER);//verificar historial ya que no se envia parametro

    @Select("SELECT * FROM TMSUSP WHERE CO_NEGO_SUCU=#{CO_NEGO_SUCU}"
            + " AND (CO_CIER_INIC is NULL OR CO_CIER_FIN is null)"
            + " AND ST_ELIM=0"
            + " ORDER BY FE_INIC ASC")
    public List<SUSP> selectByNegoSucuPendientesFactSortByFE_INIC(Integer CO_NEGO_SUCU);
    
    @Select("SELECT * FROM TMSUSP WHERE CO_NEGO_SUCU=#{CO_NEGO_SUCU}"
            + " AND (CO_CIER_INIC is NULL OR (CO_CIER_FIN is null AND not FE_FIN is null))"
            + " AND ST_ELIM=0"
            + " ORDER BY FE_INIC ASC")
    public List<SUSP> selectByNegoSucuPendientesSortByFE_INIC(Integer CO_NEGO_SUCU);
    
    @Select("SELECT count(*) FROM TMSUSP WHERE CO_NEGO_SUCU=#{CO_NEGO_SUCU}"
            + " AND FE_FIN is null "
            + " AND ST_ELIM=0")
    public boolean isSuspendido(Integer CO_NEGO_SUCU);
    
    @Select("SELECT COUNT(*) FROM TMSUSP " +
    "   WHERE CO_NEGO_SUCU=#{CO_NEGO_SUCU} AND " +
    "   (not CO_CIER_INIC is NULL AND (CO_CIER_FIN is null AND FE_FIN is null)) " +
    "   AND ST_ELIM=0;")
    public boolean isSuspendidoFacturado(Integer CO_NEGO_SUCU);
    
}

