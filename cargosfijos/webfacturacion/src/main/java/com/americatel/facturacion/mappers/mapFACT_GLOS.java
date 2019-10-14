/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.mappers;

import com.americatel.facturacion.historial.Historial;
import com.americatel.facturacion.models.DIST;
import com.americatel.facturacion.models.FACT_GLOS;
import com.americatel.facturacion.models.NEGO_SUCU;
import com.americatel.facturacion.models.RECI_GLOS;
import com.americatel.facturacion.models.TIPO_GLOS;
import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 *
 * @author crodas
 */
public interface mapFACT_GLOS {
    @Insert("INSERT INTO TMFACT_GLOS"+
            "(CO_FACT,CO_NEGO_SUCU,DE_PERI,NO_GLOS,IM_MONT,TI_GLOS,DE_DIRE_SUCU,CO_DIST,NO_DIST,"+Historial.stringMapperInsertarTitles+") VALUES"+
            "(#{CO_FACT},#{CO_NEGO_SUCU},#{DE_PERI},#{NO_GLOS},#{IM_MONT},#{TI_GLOS},#{DE_DIRE_SUCU},#{CO_DIST},#{NO_DIST},"+Historial.stringMapperInsertarValuesNumeral+");")
    public void insertar(FACT_GLOS recibo_glosa);    
    
    //DE_DIRE_SUCU=#{DE_DIRE_SUCU},
    //NO_DIST=#{NO_DIST},
    //TI_GLOS=#{TI_GLOS},
    @Update("UPDATE TMFACT_GLOS SET TI_GLOS=#{TI_GLOS},NO_DIST=#{NO_DIST},DE_DIRE_SUCU=#{DE_DIRE_SUCU},CO_NEGO_SUCU=#{CO_NEGO_SUCU},CO_DIST=#{CO_DIST},NO_GLOS=#{NO_GLOS},IM_MONT=#{IM_MONT},"+Historial.stringMapperModificar+" WHERE CO_FACT_GLOS=#{CO_FACT_GLOS};")
    public void update(FACT_GLOS aThis);

    @Update("UPDATE TMFACT_GLOS SET ST_ELIM=1,"+Historial.stringMapperModificar+" WHERE CO_FACT_GLOS=#{CO_FACT_GLOS};")
    public void delete(FACT_GLOS reci_glos);
    
    
    //@Result(property = "DE_DIRE_SUCU",column="DE_DIRE_SUCU"),     
    //@Result(property = "CO_DIST",column="CO_DIST"),
    //@Result(property = "NO_DIST",column="NO_DIST"),
    //@Result(property = "dist",column = "CO_DIST",javaType = DIST.class,one = @One(select = "com.americatel.facturacion.mappers.mapDIST.getId"),id = false),
    
    @Select("SELECT * FROM TMFACT_GLOS WHERE CO_FACT=#{CO_FACT} AND ST_ELIM=0;")
    @Results(value={
            @Result(property = "CO_FACT_GLOS",column="CO_RECI_GLOS",id = true),
            @Result(property = "CO_FACT",column="CO_FACT"),     
            @Result(property = "CO_NEGO_SUCU",column="CO_NEGO_SUCU"),               
            @Result(property = "NO_GLOS",column="NO_GLOS"),
            @Result(property = "IM_MONT",column="IM_MONT"), 
            @Result(property = "TI_GLOS",column="TI_GLOS"),
            @Result(property = "ST_ELIM",column="ST_ELIM"),
            @Result(property = "DE_DIRE_SUCU",column="DE_DIRE_SUCU"),     
            @Result(property = "CO_DIST",column="CO_DIST"),
            @Result(property = "NO_DIST",column="NO_DIST"),
            @Result(property = "dist",column = "CO_DIST",javaType = DIST.class,one = @One(select = "com.americatel.facturacion.mappers.mapDIST.getId"),id = false),
            @Result(property = "nego_sucu",column = "CO_NEGO_SUCU",javaType = NEGO_SUCU.class,one = @One(select = "com.americatel.facturacion.mappers.mapNEGO_SUCU.getId"),id = false),    
            @Result(property = "tipo_glos",column = "TI_GLOS",javaType = TIPO_GLOS.class,one = @One(select = "com.americatel.facturacion.mappers.mapTIPO_GLOS.getId"),id = false)
        }
    )    
    public List<FACT_GLOS> getByFACT(Long CO_FACT);
    
    
    @Select("SELECT * FROM TMFACT_GLOS WHERE CO_NEGO_SUCU=#{CO_NEGO_SUCU} AND ST_ELIM=0;")
    @Results(value={
            @Result(property = "CO_FACT_GLOS",column="CO_FACT_GLOS",id = true),
            @Result(property = "CO_FACT",column="CO_FACT"),     
            @Result(property = "CO_NEGO_SUCU",column="CO_NEGO_SUCU"),  
            @Result(property = "DE_DIRE_SUCU",column="DE_DIRE_SUCU"), 
            @Result(property = "CO_DIST",column="CO_DIST"),
            @Result(property = "NO_DIST",column="NO_DIST"),
            @Result(property = "NO_GLOS",column="NO_GLOS"),     
            @Result(property = "IM_MONT",column="IM_MONT"), 
            @Result(property = "TI_GLOS",column="TI_GLOS"),
            @Result(property = "ST_ELIM",column="ST_ELIM"),
        } 
    )     
    public List<FACT_GLOS> getByCoNegoSucu(Integer CO_FACT_GLOS);
 
    @Select("SELECT * FROM TMFACT_GLOS WHERE CO_FACT_GLOS=#{CO_FACT_GLOS} AND ST_ELIM=0;")
    @Results(value={
            @Result(property = "CO_FACT_GLOS",column="CO_FACT_GLOS",id = true),
            @Result(property = "CO_FACT",column="CO_FACT"),     
            @Result(property = "CO_NEGO_SUCU",column="CO_NEGO_SUCU"),  
            @Result(property = "DE_DIRE_SUCU",column="DE_DIRE_SUCU"), 
            @Result(property = "CO_DIST",column="CO_DIST"),
            @Result(property = "NO_DIST",column="NO_DIST"),
            @Result(property = "NO_GLOS",column="NO_GLOS"),     
            @Result(property = "IM_MONT",column="IM_MONT"), 
            @Result(property = "TI_GLOS",column="TI_GLOS"),
            @Result(property = "ST_ELIM",column="ST_ELIM"),
            @Result(property = "nego_sucu",column = "CO_NEGO_SUCU",javaType = NEGO_SUCU.class,one = @One(select = "com.americatel.facturacion.mappers.mapNEGO_SUCU.getId"),id = false)            
        }
    )     
    public FACT_GLOS getById(Integer CO_FACT_GLOS);
    
    @Select("SELECT TOP 1 * FROM TMFACT_GLOS WHERE TI_GLOS=1 AND CO_FACT=#{CO_FACT} AND ST_ELIM=0;")
    public FACT_GLOS getAlgunaSucursalFacturable(Long CO_FACT);
    
    @Select("SELECT TOP 1 NO_GLOS FROM TMFACT_GLOS WHERE CO_FACT=#{CO_RECI} AND TI_GLOS=1 AND ST_ELIM=0;")
    public String getDetalleServicioByFACT(Long CO_FACT);
    
}
