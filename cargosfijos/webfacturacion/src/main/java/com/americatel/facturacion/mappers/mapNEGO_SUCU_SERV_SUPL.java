/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.mappers;

import com.americatel.facturacion.historial.Historial;
import com.americatel.facturacion.models.DEPA;
import com.americatel.facturacion.models.DIST;
import com.americatel.facturacion.models.DIST;
import com.americatel.facturacion.models.NEGO_SUCU_PLAN;
import com.americatel.facturacion.models.NEGO_SUCU_SERV_SUPL;
import com.americatel.facturacion.models.PROV;
import com.americatel.facturacion.models.SERV_SUPL;
import java.util.Date;
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
public interface mapNEGO_SUCU_SERV_SUPL {  

    @Insert("INSERT INTO TMNEGO_SUCU_SERV_SUPL "+
            "(CO_NEGO_SUCU,CO_SERV_SUPL,ST_SOAR_INST,CO_OIT_INST,ST_SOAR_DESI,CO_OIT_DESI,NO_SERV_SUPL,FE_INIC,FE_FIN,FE_ULTI_FACT,IM_MONTO,ST_AFEC_DETR,CO_NEGO_SUCU_SERV_SUPL_CESI_CONT_PADR,CO_NEGO_SUCU_SERV_SUPL_CESI_CONT_HIJO,CO_NEGO_SUCU_SERV_SUPL_MUDA_PADR,CO_NEGO_SUCU_SERV_SUPL_MUDA_HIJO,"+Historial.stringMapperInsertarTitles+") VALUES "+
            "(#{CO_NEGO_SUCU},#{CO_SERV_SUPL},#{ST_SOAR_INST},#{CO_OIT_INST},#{ST_SOAR_DESI},#{CO_OIT_DESI},#{NO_SERV_SUPL},#{FE_INIC},#{FE_FIN},#{FE_ULTI_FACT},#{IM_MONTO},#{ST_AFEC_DETR},#{CO_NEGO_SUCU_SERV_SUPL_CESI_CONT_PADR},#{CO_NEGO_SUCU_SERV_SUPL_CESI_CONT_HIJO},#{CO_NEGO_SUCU_SERV_SUPL_MUDA_PADR},#{CO_NEGO_SUCU_SERV_SUPL_MUDA_HIJO},"+Historial.stringMapperInsertarValuesNumeral+");")
    @SelectKey(statement="SELECT @@IDENTITY;", keyProperty="CO_NEGO_SUCU_SERV_SUPL", before=false, resultType=Integer.class)
    public void insert(NEGO_SUCU_SERV_SUPL nego_sucu_serv_plan);

    @Select("SELECT * FROM TMNEGO_SUCU_SERV_SUPL WHERE CO_NEGO_SUCU_SERV_SUPL=#{id} AND ST_ELIM=0;")
    @Results(value={
            @Result(property = "CO_NEGO_SUCU_SERV_SUPL",column="CO_NEGO_SUCU_SERV_SUPL",id = true), 
            @Result(property = "CO_NEGO_SUCU",column="CO_NEGO_SUCU"), 
            @Result(property = "ST_SOAR_INST",column="ST_SOAR_INST"), 
            @Result(property = "CO_OIT_INST",column="CO_OIT_INST"), 
            @Result(property = "ST_SOAR_DESI",column="ST_SOAR_DESI"), 
            @Result(property = "CO_OIT_DESI",column="CO_OIT_DESI"), 
            @Result(property = "CO_SERV_SUPL",column="CO_SERV_SUPL"), 
            @Result(property = "NO_SERV_SUPL",column="NO_SERV_SUPL"),
            @Result(property = "FE_INIC",column="FE_INIC"),
            @Result(property = "FE_FIN",column="FE_FIN"), 
            @Result(property = "FE_ULTI_FACT",column="FE_ULTI_FACT"), 
            @Result(property = "FE_ULTI_FACT_SGTE",column="FE_ULTI_FACT_SGTE"), 
            @Result(property = "IM_MONTO",column="IM_MONTO"), 
            @Result(property = "ST_ELIM",column="ST_ELIM"), 
            @Result(property = "serv_supl",column = "CO_SERV_SUPL",javaType = SERV_SUPL.class,one = @One(select = "com.americatel.facturacion.mappers.mapSERV_SUPL.getId"),id = false),
        }
    )
    public NEGO_SUCU_SERV_SUPL getId(int id);
    
    @Select("SELECT * FROM TMNEGO_SUCU_SERV_SUPL WHERE CO_NEGO_SUCU=#{CO_NEGO_SUCU} AND ST_ELIM=0;")
    @Results(value={
            @Result(property = "CO_NEGO_SUCU_SERV_SUPL",column="CO_NEGO_SUCU_SERV_SUPL",id = true), 
            @Result(property = "CO_NEGO_SUCU",column="CO_NEGO_SUCU"), 
            @Result(property = "ST_SOAR_INST",column="ST_SOAR_INST"), 
            @Result(property = "CO_OIT_INST",column="CO_OIT_INST"), 
            @Result(property = "ST_SOAR_DESI",column="ST_SOAR_DESI"), 
            @Result(property = "CO_OIT_DESI",column="CO_OIT_DESI"), 
            @Result(property = "CO_SERV_SUPL",column="CO_SERV_SUPL"), 
            @Result(property = "NO_SERV_SUPL",column="NO_SERV_SUPL"),
            @Result(property = "FE_INIC",column="FE_INIC"),
            @Result(property = "FE_FIN",column="FE_FIN"), 
            @Result(property = "FE_ULTI_FACT",column="FE_ULTI_FACT"), 
            @Result(property = "FE_ULTI_FACT_SGTE",column="FE_ULTI_FACT_SGTE"), 
            @Result(property = "IM_MONTO",column="IM_MONTO"), 
            @Result(property = "IM_SALD_APROX",column="IM_SALD_APROX"), 
            @Result(property = "CO_NEGO_SUCU_SERV_SUPL_CESI_CONT_PADR",column="CO_NEGO_SUCU_SERV_SUPL_CESI_CONT_PADR"), 
            @Result(property = "CO_NEGO_SUCU_SERV_SUPL_CESI_CONT_HIJO",column="CO_NEGO_SUCU_SERV_SUPL_CESI_CONT_HIJO"), 
            @Result(property = "CO_NEGO_SUCU_SERV_SUPL_MUDA_PADR",column="CO_NEGO_SUCU_SERV_SUPL_MUDA_PADR"), 
            @Result(property = "CO_NEGO_SUCU_SERV_SUPL_MUDA_HIJO",column="CO_NEGO_SUCU_SERV_SUPL_MUDA_HIJO"), 
            @Result(property = "ST_ELIM",column="ST_ELIM"), 
            @Result(property = "serv_supl",column = "CO_SERV_SUPL",javaType = SERV_SUPL.class,one = @One(select = "com.americatel.facturacion.mappers.mapSERV_SUPL.getId"),id = false),
        }
    )
    public List<NEGO_SUCU_SERV_SUPL> selectByNEGO_SUCU(Integer CO_NEGO_SUCU);

    @Select("SELECT * FROM TMNEGO_SUCU_SERV_SUPL WHERE CO_NEGO_SUCU=#{CO_NEGO_SUCU} AND FE_FIN IS NULL AND ST_ELIM=0;")
    @Results(value={
            @Result(property = "CO_NEGO_SUCU_SERV_SUPL",column="CO_NEGO_SUCU_SERV_SUPL",id = true), 
            @Result(property = "CO_NEGO_SUCU",column="CO_NEGO_SUCU"), 
            @Result(property = "ST_SOAR_INST",column="ST_SOAR_INST"), 
            @Result(property = "CO_OIT_INST",column="CO_OIT_INST"), 
            @Result(property = "ST_SOAR_DESI",column="ST_SOAR_DESI"), 
            @Result(property = "CO_OIT_DESI",column="CO_OIT_DESI"), 
            @Result(property = "CO_SERV_SUPL",column="CO_SERV_SUPL"), 
            @Result(property = "NO_SERV_SUPL",column="NO_SERV_SUPL"),
            @Result(property = "FE_INIC",column="FE_INIC"),
            @Result(property = "FE_FIN",column="FE_FIN"), 
            @Result(property = "FE_ULTI_FACT",column="FE_ULTI_FACT"), 
            @Result(property = "FE_ULTI_FACT_SGTE",column="FE_ULTI_FACT_SGTE"), 
            @Result(property = "IM_MONTO",column="IM_MONTO"), 
            @Result(property = "ST_ELIM",column="ST_ELIM"), 
            @Result(property = "serv_supl",column = "CO_SERV_SUPL",javaType = SERV_SUPL.class,one = @One(select = "com.americatel.facturacion.mappers.mapSERV_SUPL.getId"),id = false),
        }
    )
    public List<NEGO_SUCU_SERV_SUPL> selectActivoByNEGO_SUCU(Integer CO_NEGO_SUCU);
    
    @Select("SELECT * FROM TMNEGO_SUCU_SERV_SUPL WHERE CO_NEGO=#{CO_NEGO} AND FE_FIN IS NULL AND ST_ELIM=0;")
    @Results(value={
            @Result(property = "CO_NEGO_SUCU_SERV_SUPL",column="CO_NEGO_SUCU_SERV_SUPL",id = true), 
            @Result(property = "CO_NEGO_SUCU",column="CO_NEGO_SUCU"), 
            @Result(property = "ST_SOAR_INST",column="ST_SOAR_INST"), 
            @Result(property = "CO_OIT_INST",column="CO_OIT_INST"), 
            @Result(property = "ST_SOAR_DESI",column="ST_SOAR_DESI"), 
            @Result(property = "CO_OIT_DESI",column="CO_OIT_DESI"), 
            @Result(property = "CO_SERV_SUPL",column="CO_SERV_SUPL"), 
            @Result(property = "NO_SERV_SUPL",column="NO_SERV_SUPL"),
            @Result(property = "FE_INIC",column="FE_INIC"),
            @Result(property = "FE_FIN",column="FE_FIN"), 
            @Result(property = "FE_ULTI_FACT",column="FE_ULTI_FACT"), 
            @Result(property = "FE_ULTI_FACT_SGTE",column="FE_ULTI_FACT_SGTE"), 
            @Result(property = "IM_MONTO",column="IM_MONTO"), 
            @Result(property = "ST_ELIM",column="ST_ELIM"), 
            @Result(property = "serv_supl",column = "CO_SERV_SUPL",javaType = SERV_SUPL.class,one = @One(select = "com.americatel.facturacion.mappers.mapSERV_SUPL.getId"),id = false),
        }
    )
    public List<NEGO_SUCU_SERV_SUPL> selectActivoByNEGO(Integer CO_NEGO);
    
    @Update("UPDATE TMNEGO_SUCU_SERV_SUPL SET ST_SOAR_DESI=#{ST_SOAR_DESI},CO_OIT_DESI=#{CO_OIT_DESI},FE_FIN=#{FE_FIN},"+Historial.stringMapperModificar+" WHERE CO_NEGO_SUCU_SERV_SUPL=#{CO_NEGO_SUCU_SERV_SUPL} AND ST_ELIM=0;")
    public void desactivar(NEGO_SUCU_SERV_SUPL nego_sucu_serv_supl);

    @Update("UPDATE TMNEGO_SUCU_SERV_SUPL SET FE_ULTI_FACT_SGTE=#{FE_ULTI_FACT_SGTE},"+Historial.stringMapperModificar+" WHERE CO_NEGO_SUCU_SERV_SUPL=#{CO_NEGO_SUCU_SERV_SUPL} AND ST_ELIM=0;") 
    public void saveSiguienteUltimaFacturacion(NEGO_SUCU_SERV_SUPL aThis);
    
    @Update("UPDATE TMNEGO_SUCU_SERV_SUPL SET IM_SALD_APROX=#{IM_SALD_APROX},"+Historial.stringMapperModificar+" WHERE CO_NEGO_SUCU_SERV_SUPL=#{CO_NEGO_SUCU_SERV_SUPL} AND ST_ELIM=0;")
    public void updateSaldoAproximado(NEGO_SUCU_SERV_SUPL aThis);

    @Update("UPDATE TMNEGO_SUCU_SERV_SUPL SET CO_NEGO_SUCU_SERV_SUPL_CESI_CONT_PADR=#{CO_NEGO_SUCU_SERV_SUPL_CESI_CONT_PADR} , CO_NEGO_SUCU_SERV_SUPL_CESI_CONT_HIJO=#{CO_NEGO_SUCU_SERV_SUPL_CESI_CONT_HIJO},"+Historial.stringMapperModificar+" WHERE CO_NEGO_SUCU_SERV_SUPL=#{CO_NEGO_SUCU_SERV_SUPL} AND ST_ELIM=0;")
    public void updateCesionContractual(NEGO_SUCU_SERV_SUPL aThis);
    
    @Update("UPDATE TMNEGO_SUCU_SERV_SUPL SET CO_NEGO_SUCU_SERV_SUPL_MUDA_PADR=#{CO_NEGO_SUCU_SERV_SUPL_MUDA_PADR} , CO_NEGO_SUCU_SERV_SUPL_MUDA_HIJO=#{CO_NEGO_SUCU_SERV_SUPL_MUDA_HIJO},"+Historial.stringMapperModificar+" WHERE CO_NEGO_SUCU_SERV_SUPL=#{CO_NEGO_SUCU_SERV_SUPL} AND ST_ELIM=0;")
    public void updateMudanza(NEGO_SUCU_SERV_SUPL aThis);
    
    @Update("UPDATE TMNEGO_SUCU_SERV_SUPL SET CO_NEGO_SUCU=#{CO_NEGO_SUCU}, "+Historial.stringMapperModificar+" WHERE CO_NEGO_SUCU_SERV_SUPL=#{CO_NEGO_SUCU_SERV_SUPL} AND ST_ELIM=0;")
    public void cambiarSucursal(NEGO_SUCU_SERV_SUPL aThis);
    
    @Update("UPDATE TMNEGO_SUCU_SERV_SUPL SET ST_ELIM=1,"+Historial.stringMapperModificar+" WHERE CO_NEGO_SUCU_SERV_SUPL=#{CO_NEGO_SUCU_SERV_SUPL};")
    public int delete(NEGO_SUCU_SERV_SUPL aThis);
    
    @Select("SELECT CAST( " +
        "CASE WHEN COUNT(*) = (SELECT COUNT(*) FROM TMNEGO_SUCU_SERV_SUPL WHERE CO_NEGO_SUCU=#{CO_NEGO_SUCU} AND ST_ELIM=0) THEN 1 " +
        "ELSE 0 " +
        "END AS bit) AS IND " +
        "FROM TMNEGO_SUCU_SERV_SUPL WHERE CO_NEGO_SUCU=#{CO_NEGO_SUCU} AND FE_ULTI_FACT IS NULL AND ST_ELIM=0")
    public boolean isTodosServSuplSinFacturar(Integer CO_NEGO_SUCU);
}
