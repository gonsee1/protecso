/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.mappers;

import com.americatel.facturacion.historial.Historial;
import com.americatel.facturacion.models.CLIE;
import com.americatel.facturacion.models.DIST;
import com.americatel.facturacion.models.SUCU;
import com.americatel.facturacion.models.PROD;
import java.util.List;
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
public interface mapSUCU {

    @Select("SELECT * FROM TMSUCU WHERE CO_SUCU=#{id} AND ST_ELIM=0")
    @Results(value={
            @Result(property = "CO_SUCU",column="CO_SUCU",id = true), 
            @Result(property = "DE_DIRE",column="DE_DIRE"),
            @Result(property = "DE_REF_DIRE",column="DE_REF_DIRE"),
            @Result(property = "CO_DIST",column="CO_DIST"), 
            @Result(property = "CO_CLIE",column="CO_CLIE"), 
            @Result(property = "ST_ELIM",column="ST_ELIM"),
            @Result(property = "dist",column = "CO_DIST",javaType = DIST.class,one = @One(select = "com.americatel.facturacion.mappers.mapDIST.getId"),id = false),
            @Result(property = "clie",column = "CO_CLIE",javaType = CLIE.class,one = @One(select = "com.americatel.facturacion.mappers.mapCLIE.getId"),id = false)
        }
    )
    public SUCU getId(int id);
    
    @Select("SELECT * FROM TMSUCU WHERE CO_SUCU=#{id} AND ST_ELIM=0")
    @Results(value={
            @Result(property = "CO_DIST",column="CO_DIST"), 
            @Result(property = "dist",column = "CO_DIST",javaType = DIST.class,one = @One(select = "com.americatel.facturacion.mappers.mapDIST.getId"),id = false),            
        }
    )
    public SUCU getId_paraProcesoFacturacion(int id);
    
    @Select("SELECT * FROM TMSUCU WHERE CO_SUCU=#{id} AND ST_ELIM=0")
    @Results(value={
            @Result(property = "CO_SUCU",column="CO_SUCU",id = true), 
            @Result(property = "DE_DIRE",column="DE_DIRE"),
            @Result(property = "DE_REF_DIRE",column="DE_REF_DIRE"),
            @Result(property = "CO_DIST",column="CO_DIST"), 
            @Result(property = "CO_CLIE",column="CO_CLIE"), 
            @Result(property = "ST_ELIM",column="ST_ELIM"),
            @Result(property = "dist",column = "CO_DIST",javaType = PROD.class,one = @One(select = "com.americatel.facturacion.mappers.mapDIST.getId"),id = false),
            //@Result(property = "clie",column = "CO_CLIE",javaType = PROD.class,one = @One(select = "com.americatel.facturacion.mappers.mapCLIE.getOnlyId"),id = false)
        }
    )
    public SUCU getOnlyId(int id);   
        
    @Update("UPDATE TMSUCU SET DE_DIRE=#{DE_DIRE},DE_REF_DIRE=#{DE_REF_DIRE},CO_DIST=#{CO_DIST},CO_CLIE=#{CO_CLIE},"+Historial.stringMapperModificar+" WHERE CO_SUCU=${CO_SUCU} AND ST_ELIM=0;")
    public int update(SUCU sucu);

    //@Delete("DELETE FROM TMSUCU WHERE CO_SUCU=${CO_SUCU};")
    @Delete("UPDATE TMSUCU SET ST_ELIM=1,"+Historial.stringMapperModificar+" WHERE CO_SUCU=${CO_SUCU};")
    public int delete(SUCU sucu);

    @Insert("INSERT INTO TMSUCU (DE_DIRE,DE_REF_DIRE,CO_DIST,CO_CLIE,"+Historial.stringMapperInsertarTitles+") VALUES (#{DE_DIRE},#{DE_REF_DIRE},#{CO_DIST},#{CO_CLIE},"+Historial.stringMapperInsertarValuesNumeral+");")
    @SelectKey(statement="SELECT @@IDENTITY;", keyProperty="CO_SUCU", before=false, resultType=Integer.class)
    public void insert(SUCU sucu);

    @Select("SELECT * FROM TMSUCU WHERE CO_SUCU like '%${CO_SUCU}%' AND ST_ELIM=0")
    @Results(value={
            @Result(property = "CO_SUCU",column="CO_SUCU",id = true), 
            @Result(property = "DE_DIRE",column="DE_DIRE"),
            @Result(property = "DE_REF_DIRE",column="DE_REF_DIRE"),
            @Result(property = "CO_DIST",column="CO_DIST"), 
            @Result(property = "CO_CLIE",column="CO_CLIE"), 
            @Result(property = "ST_ELIM",column="ST_ELIM"),
            @Result(property = "dist",column = "CO_DIST",javaType = DIST.class,one = @One(select = "com.americatel.facturacion.mappers.mapDIST.getId"),id = false),
            @Result(property = "clie",column = "CO_CLIE",javaType = CLIE.class,one = @One(select = "com.americatel.facturacion.mappers.mapCLIE.getOnlyId"),id = false)
        }
    )  
    public List<SUCU> select(@Param("CO_SUCU") String CO_SUCU);    

    
    @Select("SELECT * FROM TMSUCU WHERE CO_CLIE='${CO_CLIE}' AND ST_ELIM=0")
    @Results(value={
            @Result(property = "CO_SUCU",column="CO_SUCU",id = true), 
            @Result(property = "DE_DIRE",column="DE_DIRE"),
            @Result(property = "DE_REF_DIRE",column="DE_REF_DIRE"),
            @Result(property = "CO_DIST",column="CO_DIST"), 
            @Result(property = "CO_CLIE",column="CO_CLIE"), 
            @Result(property = "ST_ELIM",column="ST_ELIM"),
            @Result(property = "dist",column = "CO_DIST",javaType = PROD.class,one = @One(select = "com.americatel.facturacion.mappers.mapDIST.getId"),id = false),
            @Result(property = "clie",column = "CO_CLIE",javaType = PROD.class,one = @One(select = "com.americatel.facturacion.mappers.mapCLIE.getOnlyId"),id = false)
        }
    )    
    public List<SUCU> selectByCLIE(@Param("CO_CLIE") int CO_CLIE);

    @Select("SELECT TOP 1 * FROM TMSUCU WHERE CO_CLIE=${CO_CLIE} AND CO_DIST=${CO_DIST} AND DE_DIRE=#{DE_DIRE}")
    public SUCU getByClienteAndDireccionAndDistrito(@Param("CO_CLIE") int CO_CLIE, @Param("CO_DIST") int CO_DIST,@Param("DE_DIRE") String DE_DIRE);
    
}
