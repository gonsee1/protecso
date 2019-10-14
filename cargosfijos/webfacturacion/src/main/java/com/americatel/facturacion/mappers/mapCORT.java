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
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.americatel.facturacion.historial.Historial;
import com.americatel.facturacion.models.CORT;

/**
 *
 * @author crodas
 */
public interface mapCORT { 

    @Insert("INSERT INTO TMCORT (CO_NEGO,CO_PROD,FE_INIC,FE_FIN,CO_CIER_INIC,CO_CIER_FIN,"+Historial.stringMapperInsertarTitles+") VALUES (#{CO_NEGO},#{CO_PROD},#{FE_INIC},#{FE_FIN},#{CO_CIER_INIC},#{CO_CIER_FIN},"+Historial.stringMapperInsertarValuesNumeral+")")
    public void insert(CORT cort);    

    
    @Select("SELECT * FROM TMCORT WHERE CO_NEGO=#{CO_NEGO} AND FE_INIC=#{FE_INIC} AND ST_ELIM=0;")
    public List<CORT> selectByNegoAndInicio(@Param("CO_NEGO") int CO_NEGO,@Param("FE_INIC") Date FE_INIC);

    @Select("SELECT *, "+
    "       CASE WHEN (CO_CIER_INIC is NULL OR (CO_CIER_FIN is null AND not FE_FIN is null)) THEN 'PENDIENTE' " +
    "       ELSE 'FACTURADO' END AS DE_ESTADO " +
    "       FROM TMCORT WHERE CO_NEGO=#{CO_NEGO} AND ST_ELIM=0 ORDER BY FE_INIC ASC")
    public List<CORT> selectByNego(Integer CO_NEGO);
    
    @Select("SELECT * FROM TMCORT WHERE CO_NEGO=#{CO_NEGO} AND ST_ELIM=0 ORDER BY FE_INIC ASC")
    public List<CORT> selectByNegoSortByFE_INIC(Integer CO_NEGO);    
    
    @Update("UPDATE TMCORT SET CO_PROD=#{CO_PROD},FE_FIN=#{FE_FIN},CO_CIER_INIC=#{CO_CIER_INIC},CO_CIER_FIN=#{CO_CIER_FIN} WHERE CO_CORT=#{CO_CORT} AND ST_ELIM=0;")
    public int update(CORT c);
    
    @Select("SELECT count(*) FROM TMCORT WHERE (CO_CIER_INIC is NULL OR (CO_CIER_FIN is null AND not FE_FIN is null)) AND ST_ELIM=0")
    public int getCantidadCortesCargados();
    
    @Select("SELECT count(*) FROM TMCORT WHERE CO_PROD=#{CO_PROD} AND (CO_CIER_INIC is NULL OR (CO_CIER_FIN is null AND not FE_FIN is null)) AND ST_ELIM=0")
    public int getCantidadCortesCargadosByProducto(@Param("CO_PROD")int CO_PROD);

    @Select("SELECT * FROM TMCORT WHERE (CO_CIER_INIC is NULL OR (CO_CIER_FIN is null AND not FE_FIN is null)) AND ST_ELIM=0")
    public List<CORT> selectCortesPendientes();    
    
//    @Select("SELECT * FROM TMCORT WHERE CO_NEGO=#{CO_NEGO} AND (CO_CIER_INIC is NULL OR (CO_CIER_FIN is null AND not FE_FIN is null)) AND ST_ELIM=0 ORDER BY FE_INIC ASC")    
    @Select("SELECT * FROM TMCORT WHERE CO_NEGO=#{CO_NEGO} AND (CO_CIER_INIC is NULL OR CO_CIER_FIN is null) AND ST_ELIM=0  ORDER BY FE_INIC ASC")
    public List<CORT> selectCortesPendientesFactByCO_NEGOSortByFE_INIC(@Param("CO_NEGO")int CO_NEGO);     
    
    @Select("SELECT * FROM TMCORT WHERE CO_NEGO=#{CO_NEGO} AND (CO_CIER_INIC is NULL OR (CO_CIER_FIN is null AND not FE_FIN is null)) AND ST_ELIM=0  ORDER BY FE_INIC ASC")
    public List<CORT> selectCortesPendientesByCO_NEGOSortByFE_INIC(@Param("CO_NEGO")int CO_NEGO);     
    
    @Select("SELECT * FROM TMCORT WHERE CO_PROD=#{CO_PROD} AND (CO_CIER_INIC is NULL OR (CO_CIER_FIN is null AND not FE_FIN is null)) AND ST_ELIM=0")
    public List<CORT> selectCortesPendientesByProducto(@Param("CO_PROD")int CO_PROD);    
    
    @Select("SELECT * FROM TMCORT WHERE ST_ELIM=0")
    public List<CORT> selectAllCortes(); 
       
    /*@Select("SELECT CO_CORT, CO_NEGO, FE_INIC, FE_FIN, CASE WHEN (CO_CIER_INIC is NULL OR (CO_CIER_FIN is null AND not FE_FIN is null)) THEN 'PENDIENTE' " +
    "ELSE 'FACTURADO' END AS DE_ESTADO " +
    "FROM TMCORT WHERE ST_ELIM=0")*/    
    @Select("SELECT COR.CO_CORT AS CO_CORT, COR.CO_NEGO AS CO_NEGO, COR.FE_INIC AS FE_INIC, COR.FE_FIN AS FE_FIN, CASE WHEN (COR.CO_CIER_INIC is NULL OR (COR.CO_CIER_FIN is null AND not COR.FE_FIN is null)) THEN 'PENDIENTE' " +
    "ELSE 'FACTURADO' END AS DE_ESTADO, PR_PA.COD_PROD_PADRE AS COD_PROD_PADRE " +
    "FROM TMCORT COR INNER JOIN TMPROD_PADRE_TMPROD PR_P ON COR.CO_PROD =  PR_P.COD_PROD " +
    "INNER JOIN TMPROD_PADRE PR_PA ON PR_P.COD_PROD_PADRE = PR_PA.COD_PROD_PADRE " +  
    "WHERE COR.ST_ELIM=0") 
    public List<CORT> selectAllCortesConEstado();
    
    @Delete("DELETE FROM TMCORT WHERE CO_CIER_INIC is null and CO_PROD=#{CO_PROD};UPDATE TMCORT SET FE_FIN=null WHERE FE_FIN is null and CO_CIER_FIN is not null and CO_PROD=#{CO_PROD};")
    public int quitarPendientes(@Param("CO_PROD")int CO_PROD);//Para volverlor a cargar    
/*
    @Update("UPDATE TMCORT SET FE_FIN=#{FE_FIN},CO_CIER_INIC=#{CO_CIER_INIC},CO_CIER_FIN=#{CO_CIER_FIN} WHERE CO_CORT=#{CO_CORT};")
    public int update(CORT c);   */ 

    
    //Despues de cerrar el periodo congelamos los valores
    @Update("UPDATE TMCORT SET CO_CIER_INIC=${CO_CIER},CO_CIER_FIN=${CO_CIER} WHERE CO_PROD=${CO_PROD} and CO_CIER_INIC is null and CO_CIER_FIN is null and FE_INIC is not null and FE_FIN is not null AND ST_ELIM=0;"+
            "UPDATE TMCORT SET CO_CIER_INIC=${CO_CIER} WHERE CO_PROD=${CO_PROD} AND CO_CIER_INIC IS NULL AND CO_CIER_FIN IS NULL AND FE_INIC IS NOT NULL AND FE_FIN IS NULL AND ST_ELIM=0;"+
            "UPDATE TMCORT SET CO_CIER_FIN=${CO_CIER} WHERE CO_PROD=${CO_PROD} AND CO_CIER_INIC IS NOT NULL AND CO_CIER_FIN IS NULL AND FE_INIC IS NOT NULL AND FE_FIN IS NOT NULL AND ST_ELIM=0;"
            )
    public void congelarPostCierre(@Param("CO_PROD") Integer CO_PROD,@Param("CO_CIER") Integer CO_CIER);


    @Select("SELECT count(*) FROM TMCORT WHERE "
            + "CO_NEGO=#{CO_NEGO} AND "
            + "FE_FIN is null AND ST_ELIM=0 ")
    public boolean isCortado(@Param("CO_NEGO") Integer CO_NEGO);
    
    @Select("SELECT COUNT(*) FROM TMCORT " +
    "   WHERE CO_NEGO=#{CO_NEGO} AND " +
    "   (not CO_CIER_INIC is NULL AND (CO_CIER_FIN is null AND FE_FIN is null)) " +
    "   AND ST_ELIM=0;")
    public boolean isCortadoFacturado(@Param("CO_NEGO") Integer CO_NEGO);
    
}
