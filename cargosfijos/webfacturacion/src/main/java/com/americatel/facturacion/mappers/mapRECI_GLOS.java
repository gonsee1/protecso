/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.mappers;

import com.americatel.facturacion.historial.Historial;
import com.americatel.facturacion.models.DIST;
import com.americatel.facturacion.models.NEGO_SUCU;
import com.americatel.facturacion.models.RECI;
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
public interface mapRECI_GLOS {

    @Insert("INSERT INTO TMRECI_GLOS"+
            "(CO_RECI,CO_NEGO_SUCU,DE_DIRE_SUCU,CO_DIST,NO_DIST,NO_GLOS,IM_MONT,TI_GLOS,CO_BOLE,"+Historial.stringMapperInsertarTitles+") VALUES"+
            "(#{CO_RECI},#{CO_NEGO_SUCU},#{DE_DIRE_SUCU},#{CO_DIST},#{NO_DIST},#{NO_GLOS},#{IM_MONT},#{TI_GLOS}, #{CO_BOLE},"+Historial.stringMapperInsertarValuesNumeral+");")
    public void insertar(RECI_GLOS aThis);
    
    @Select("SELECT * FROM TMRECI_GLOS WHERE CO_RECI=#{CO_RECI} AND ST_ELIM=0;")
    @Results(value={
            @Result(property = "CO_RECI_GLOS",column="CO_RECI_GLOS",id = true),
            @Result(property = "CO_RECI",column="CO_RECI"),     
            @Result(property = "CO_NEGO_SUCU",column="CO_NEGO_SUCU"),     
            @Result(property = "DE_DIRE_SUCU",column="DE_DIRE_SUCU"),     
            @Result(property = "CO_DIST",column="CO_DIST"),
            @Result(property = "NO_DIST",column="NO_DIST"),
            @Result(property = "NO_GLOS",column="NO_GLOS"),
            @Result(property = "IM_MONT",column="IM_MONT"), 
            @Result(property = "TI_GLOS",column="TI_GLOS"),
            @Result(property = "ST_ELIM",column="ST_ELIM"),
            @Result(property = "CO_BOLE",column="CO_BOLE"),
            @Result(property = "nego_sucu",column = "CO_NEGO_SUCU",javaType = NEGO_SUCU.class,one = @One(select = "com.americatel.facturacion.mappers.mapNEGO_SUCU.getId"),id = false),
            @Result(property = "dist",column = "CO_DIST",javaType = DIST.class,one = @One(select = "com.americatel.facturacion.mappers.mapDIST.getId"),id = false),
            @Result(property = "tipo_glos",column = "TI_GLOS",javaType = TIPO_GLOS.class,one = @One(select = "com.americatel.facturacion.mappers.mapTIPO_GLOS.getId"),id = false)
        }
    )    
    public List<RECI_GLOS> getByRECI(Long CO_RECI);
    
    
    @Select("SELECT * FROM TMRECI_GLOS WHERE CO_BOLE=#{CO_BOLE} AND ST_ELIM=0;")
    @Results(value={
            @Result(property = "CO_RECI_GLOS",column="CO_RECI_GLOS",id = true),
            @Result(property = "CO_RECI",column="CO_RECI"),     
            @Result(property = "CO_NEGO_SUCU",column="CO_NEGO_SUCU"),     
            @Result(property = "DE_DIRE_SUCU",column="DE_DIRE_SUCU"),     
            @Result(property = "CO_DIST",column="CO_DIST"),
            @Result(property = "NO_DIST",column="NO_DIST"),
            @Result(property = "NO_GLOS",column="NO_GLOS"),
            @Result(property = "IM_MONT",column="IM_MONT"), 
            @Result(property = "TI_GLOS",column="TI_GLOS"),
            @Result(property = "ST_ELIM",column="ST_ELIM"),
            @Result(property = "CO_BOLE",column="CO_BOLE"),
            @Result(property = "nego_sucu",column = "CO_NEGO_SUCU",javaType = NEGO_SUCU.class,one = @One(select = "com.americatel.facturacion.mappers.mapNEGO_SUCU.getId"),id = false),
            @Result(property = "dist",column = "CO_DIST",javaType = DIST.class,one = @One(select = "com.americatel.facturacion.mappers.mapDIST.getId"),id = false),
            @Result(property = "tipo_glos",column = "TI_GLOS",javaType = TIPO_GLOS.class,one = @One(select = "com.americatel.facturacion.mappers.mapTIPO_GLOS.getId"),id = false)
        }
    )    
    public List<RECI_GLOS> getByBOLE(Long CO_BOLE);

    @Select("SELECT TOP 1 NO_GLOS FROM TMRECI_GLOS WHERE CO_RECI=#{CO_RECI} AND TI_GLOS=1 AND ST_ELIM=0;")
    public String getDetalleServicioByRECI(Long CO_RECI);
    
    @Select("SELECT * FROM TMRECI_GLOS WHERE CO_RECI_GLOS=#{CO_RECI_GLOS} AND ST_ELIM=0;")
    @Results(value={
            @Result(property = "CO_RECI_GLOS",column="CO_RECI_GLOS",id = true),
            @Result(property = "CO_RECI",column="CO_RECI"),     
            @Result(property = "CO_NEGO_SUCU",column="CO_NEGO_SUCU"),  
            @Result(property = "DE_DIRE_SUCU",column="DE_DIRE_SUCU"), 
            @Result(property = "CO_DIST",column="CO_DIST"),
            @Result(property = "NO_DIST",column="NO_DIST"),
            @Result(property = "NO_GLOS",column="NO_GLOS"),     
            @Result(property = "IM_MONT",column="IM_MONT"), 
            @Result(property = "TI_GLOS",column="TI_GLOS"),
            @Result(property = "ST_ELIM",column="ST_ELIM"),
            @Result(property = "CO_BOLE",column="CO_BOLE"),
            @Result(property = "nego_sucu",column = "CO_NEGO_SUCU",javaType = NEGO_SUCU.class,one = @One(select = "com.americatel.facturacion.mappers.mapNEGO_SUCU.getId"),id = false)            
        }
    )     
    public RECI_GLOS getById(Integer CO_RECI_GLOS);

    @Update("UPDATE TMRECI_GLOS SET CO_NEGO_SUCU=#{CO_NEGO_SUCU},DE_DIRE_SUCU=#{DE_DIRE_SUCU},CO_DIST=#{CO_DIST},NO_DIST=#{NO_DIST},NO_GLOS=#{NO_GLOS},IM_MONT=#{IM_MONT},TI_GLOS=#{TI_GLOS}, CO_BOLE=#{CO_BOLE},"+Historial.stringMapperModificar+" WHERE CO_RECI_GLOS=#{CO_RECI_GLOS};")
    public void update(RECI_GLOS aThis);

    @Update("UPDATE TMRECI_GLOS SET ST_ELIM=1,"+Historial.stringMapperModificar+" WHERE CO_RECI_GLOS=#{CO_RECI_GLOS};")
    public void delete(RECI_GLOS reci_glos);
    
    @Select("SELECT * FROM TMRECI_GLOS WHERE CO_NEGO_SUCU=#{CO_NEGO_SUCU} AND ST_ELIM=0;")
    @Results(value={
            @Result(property = "CO_RECI_GLOS",column="CO_RECI_GLOS",id = true),
            @Result(property = "CO_RECI",column="CO_RECI"),     
            @Result(property = "CO_NEGO_SUCU",column="CO_NEGO_SUCU"),  
            @Result(property = "DE_DIRE_SUCU",column="DE_DIRE_SUCU"), 
            @Result(property = "CO_DIST",column="CO_DIST"),
            @Result(property = "NO_DIST",column="NO_DIST"),
            @Result(property = "NO_GLOS",column="NO_GLOS"),     
            @Result(property = "IM_MONT",column="IM_MONT"), 
            @Result(property = "TI_GLOS",column="TI_GLOS"),
            @Result(property = "ST_ELIM",column="ST_ELIM"),
            @Result(property = "CO_BOLE",column="CO_BOLE"),
        } 
    )     
    public List<RECI_GLOS> getByCoNegoSucu(Integer CO_NEGO_SUCU);
    
    @Select("SELECT TOP 1 * FROM TMRECI_GLOS WHERE TI_GLOS=1 AND CO_RECI=#{CO_RECI} AND ST_ELIM=0;")
    public RECI_GLOS getAlgunaSucursalFacturable(Long CO_RECI);
    
    @Select("SELECT TOP 1 * FROM TMRECI_GLOS WHERE TI_GLOS=1 AND CO_BOLE=#{CO_BOLE} AND ST_ELIM=0;")
    public RECI_GLOS getAlgunaSucursalFacturableBole(Long CO_BOLE);
}
