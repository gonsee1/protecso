/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.mappers;

import com.americatel.facturacion.historial.Historial;
import com.americatel.facturacion.models.MOTI_DESC;
import com.americatel.facturacion.models.NEGO;
import com.americatel.facturacion.models.NEGO_SUCU;
import com.americatel.facturacion.models.PROD;
import com.americatel.facturacion.models.SUCU;
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
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;

/**
 *
 * @author crodas
 */
public interface mapNEGO_SUCU {

    
    @Select("SELECT * FROM TMNEGO_SUCU WHERE CO_NEGO_SUCU=#{id} AND ST_ELIM=0")
    @Results(value={
            @Result(property = "CO_NEGO_SUCU",column="CO_NEGO_SUCU",id = true), 
            @Result(property = "CO_NEGO",column="CO_NEGO"), 
            @Result(property = "CO_SUCU",column="CO_SUCU"), 
            @Result(property = "FE_INIC",column="FE_INIC"), 
            @Result(property = "FE_FIN",column="FE_FIN"), 
            @Result(property = "ST_SOAR_INST",column="ST_SOAR_INST"), 
            @Result(property = "CO_OIT_INST",column="CO_OIT_INST"), 
            @Result(property = "CO_CIRC",column="CO_CIRC"), 
            @Result(property = "DE_ORDE_SERV",column="DE_ORDE_SERV"), 
            @Result(property = "DE_PLAZ_CONT",column="DE_PLAZ_CONT"), 
            @Result(property = "ST_ELIM",column="ST_ELIM"),
            @Result(property = "CO_MOTI_DESC",column="CO_MOTI_DESC"), 
            @Result(property = "DE_INFO_DESC",column="DE_INFO_DESC"),
            @Result(property = "DE_OBSERV",column="DE_OBSERV"),
            @Result(property = "nego",column = "CO_NEGO",javaType = NEGO.class,one = @One(select = "com.americatel.facturacion.mappers.mapNEGO.getOnlyId"),id = false),
            @Result(property = "sucu",column = "CO_SUCU",javaType = SUCU.class,one = @One(select = "com.americatel.facturacion.mappers.mapSUCU.getOnlyId"),id = false),
            @Result(property = "moti_desc",column = "CO_MOTI_DESC",javaType = MOTI_DESC.class,one = @One(select = "com.americatel.facturacion.mappers.mapMOTI_DESC.getId"),id = false),
            @Result(property = "isSuspendido",column = "CO_NEGO_SUCU",javaType = Boolean.class,one = @One(select = "com.americatel.facturacion.mappers.mapSUSP.isSuspendido"),id = false),
            @Result(property = "isMudado",column = "CO_NEGO_SUCU",javaType = Boolean.class,one = @One(select = "com.americatel.facturacion.mappers.mapNEGO_SUCU.isMudado"),id = false),
            @Result(property = "isCesionContractual",column = "CO_NEGO_SUCU",javaType = Boolean.class,one = @One(select = "com.americatel.facturacion.mappers.mapNEGO_SUCU.isCesionContractual"),id = false),
            @Result(property = "isDesactivado",column = "CO_NEGO_SUCU",javaType = Boolean.class,one = @One(select = "com.americatel.facturacion.mappers.mapNEGO_SUCU.isDesactivado"),id = false)
        }
    )
    public NEGO_SUCU getId(Integer id);
     
    
    @Select("SELECT * FROM TMNEGO_SUCU WHERE CO_NEGO_SUCU=#{id} AND ST_ELIM=0")
    @Results(value={
            @Result(property = "CO_NEGO_SUCU",column="CO_NEGO_SUCU",id = true), 
            @Result(property = "CO_NEGO",column="CO_NEGO"), 
            @Result(property = "CO_SUCU",column="CO_SUCU"), 
            @Result(property = "FE_INIC",column="FE_INIC"), 
            @Result(property = "FE_FIN",column="FE_FIN"), 
            @Result(property = "ST_SOAR_INST",column="ST_SOAR_INST"), 
            @Result(property = "CO_OIT_INST",column="CO_OIT_INST"), 
            @Result(property = "CO_CIRC",column="CO_CIRC"),
            @Result(property = "DE_ORDE_SERV",column="DE_ORDE_SERV"), 
            @Result(property = "DE_PLAZ_CONT",column="DE_PLAZ_CONT"),
            @Result(property = "CO_MOTI_DESC",column="CO_MOTI_DESC"), 
            @Result(property = "DE_INFO_DESC",column="DE_INFO_DESC"),
            @Result(property = "DE_OBSERV",column="DE_OBSERV"),
            @Result(property = "ST_ELIM",column="ST_ELIM"),
        }
    )
    public NEGO_SUCU getOnlyId(int id);    
    
    @Update("UPDATE TMNEGO_SUCU SET CO_NEGO=#{CO_NEGO},CO_SUCU=#{CO_SUCU},FE_INIC=#{FE_INIC},FE_FIN=#{FE_FIN},ST_SOAR_INST=#{ST_SOAR_INST},CO_OIT_INST=#{CO_OIT_INST},CO_CIRC=#{CO_CIRC},CO_MOTI_DESC=#{CO_MOTI_DESC},DE_INFO_DESC=#{DE_INFO_DESC},DE_ORDE_SERV=#{DE_ORDE_SERV},DE_PLAZ_CONT=#{DE_PLAZ_CONT},"+Historial.stringMapperModificar+" WHERE CO_NEGO_SUCU=${CO_NEGO_SUCU} AND ST_ELIM=0;")
    public int update(NEGO_SUCU sucu);

    //@Delete("DELETE FROM TMNEGO_SUCU WHERE CO_SUCU=${CO_SUCU};")
    @Update("UPDATE TMNEGO_SUCU SET ST_ELIM=1,"+Historial.stringMapperModificar+" WHERE CO_NEGO_SUCU=${CO_NEGO_SUCU};")
    public int delete(NEGO_SUCU sucu);
    
    @Update("UPDATE TMNEGO_SUCU_SERV_UNIC SET ST_ELIM=1,"+Historial.stringMapperModificar+" WHERE CO_NEGO_SUCU=${CO_NEGO_SUCU};"+
            "UPDATE TMNEGO_SUCU_SERV_SUPL SET ST_ELIM=1,"+Historial.stringMapperModificar+" WHERE CO_NEGO_SUCU=${CO_NEGO_SUCU};"+
            "UPDATE TMNEGO_SUCU_PLAN SET ST_ELIM=1,"+Historial.stringMapperModificar+" WHERE CO_NEGO_SUCU=${CO_NEGO_SUCU};"+
            "UPDATE TMNEGO_SUCU SET ST_ELIM=1,"+Historial.stringMapperModificar+" WHERE CO_NEGO_SUCU=${CO_NEGO_SUCU};"
            )
    public int deleteAllInclude(NEGO_SUCU sucu);

    @Insert("INSERT INTO TMNEGO_SUCU (CO_NEGO,CO_SUCU,FE_INIC,FE_FIN,ST_SOAR_INST,CO_OIT_INST,CO_CIRC,CO_MOTI_DESC,DE_INFO_DESC,DE_OBSERV,DE_ORDE_SERV,DE_PLAZ_CONT,CO_NEGO_SUCU_CESI_CONT_PADR,CO_NEGO_SUCU_CESI_CONT_HIJO,CO_NEGO_SUCU_MUDA_PADR,CO_NEGO_SUCU_MUDA_HIJO,REFERENCIA,"+Historial.stringMapperInsertarTitles+") VALUES (#{CO_NEGO},#{CO_SUCU},#{FE_INIC},#{FE_FIN},#{ST_SOAR_INST},#{CO_OIT_INST},#{CO_CIRC},#{CO_MOTI_DESC},#{DE_INFO_DESC},#{DE_OBSERV},#{DE_ORDE_SERV},#{DE_PLAZ_CONT},#{CO_NEGO_SUCU_CESI_CONT_PADR},#{CO_NEGO_SUCU_CESI_CONT_HIJO},#{CO_NEGO_SUCU_MUDA_PADR},#{CO_NEGO_SUCU_MUDA_HIJO},#{REFERENCIA_NEGO_SUCU},"+Historial.stringMapperInsertarValuesNumeral+");")
    @SelectKey(statement="SELECT @@IDENTITY;", keyProperty="CO_NEGO_SUCU", before=false, resultType=Integer.class)
    public int insert(NEGO_SUCU sucu);

    @Select("SELECT * FROM TMNEGO_SUCU WHERE CO_NEGO_SUCU like '%${CO_NEGO_SUCU}%' AND ST_ELIM=0")
    @Results(value={
            @Result(property = "CO_NEGO_SUCU",column="CO_NEGO_SUCU",id = true), 
            @Result(property = "CO_NEGO",column="CO_NEGO"), 
            @Result(property = "CO_SUCU",column="CO_SUCU"), 
            @Result(property = "FE_INIC",column="FE_INIC"), 
            @Result(property = "FE_FIN",column="FE_FIN"), 
            @Result(property = "ST_SOAR_INST",column="ST_SOAR_INST"), 
            @Result(property = "CO_OIT_INST",column="CO_OIT_INST"), 
            @Result(property = "CO_CIRC",column="CO_CIRC"),
            @Result(property = "DE_ORDE_SERV",column="DE_ORDE_SERV"), 
            @Result(property = "DE_PLAZ_CONT",column="DE_PLAZ_CONT"), 
            @Result(property = "ST_ELIM",column="ST_ELIM"),
            @Result(property = "CO_MOTI_DESC",column="CO_MOTI_DESC"), 
            @Result(property = "DE_INFO_DESC",column="DE_INFO_DESC"),
            @Result(property = "DE_OBSERV",column="DE_OBSERV"),
            @Result(property = "nego",column = "CO_NEGO",javaType = NEGO.class,one = @One(select = "com.americatel.facturacion.mappers.mapNEGO.getOnlyId"),id = false),
            @Result(property = "sucu",column = "CO_SUCU",javaType = SUCU.class,one = @One(select = "com.americatel.facturacion.mappers.mapSUCU.getOnlyId"),id = false),
            @Result(property = "moti_desc",column = "CO_MOTI_DESC",javaType = MOTI_DESC.class,one = @One(select = "com.americatel.facturacion.mappers.mapMOTI_DESC.getId"),id = false),
            @Result(property = "isSuspendido",column = "CO_NEGO_SUCU",javaType = Boolean.class,one = @One(select = "com.americatel.facturacion.mappers.mapSUSP.isSuspendido"),id = false),            
            @Result(property = "isMudado",column = "CO_NEGO_SUCU",javaType = Boolean.class,one = @One(select = "com.americatel.facturacion.mappers.mapNEGO_SUCU.isMudado"),id = false),
            @Result(property = "isCesionContractual",column = "CO_NEGO_SUCU",javaType = Boolean.class,one = @One(select = "com.americatel.facturacion.mappers.mapNEGO_SUCU.isCesionContractual"),id = false),
            @Result(property = "isDesactivado",column = "CO_NEGO_SUCU",javaType = Boolean.class,one = @One(select = "com.americatel.facturacion.mappers.mapNEGO_SUCU.isDesactivado"),id = false)
        }
    ) 
    public List<NEGO_SUCU> select(@Param("CO_NEGO_SUCU") String CO_NEGO_SUCU);    

    
    @Select("SELECT * FROM TMNEGO_SUCU WHERE CO_NEGO=#{CO_NEGO} AND ST_ELIM=0;")
    @Results(value={
            @Result(property = "CO_NEGO_SUCU",column="CO_NEGO_SUCU",id = true), 
            @Result(property = "CO_NEGO",column="CO_NEGO"), 
            @Result(property = "CO_SUCU",column="CO_SUCU"), 
            @Result(property = "FE_INIC",column="FE_INIC",javaType = Date.class), 
            @Result(property = "FE_FIN",column="FE_FIN",javaType = Date.class), 
            @Result(property = "ST_SOAR_INST",column="ST_SOAR_INST"), 
            @Result(property = "CO_OIT_INST",column="CO_OIT_INST"), 
            @Result(property = "CO_CIRC",column="CO_CIRC"), 
            @Result(property = "DE_ORDE_SERV",column="DE_ORDE_SERV"), 
            @Result(property = "DE_PLAZ_CONT",column="DE_PLAZ_CONT"),   
            @Result(property = "ST_ELIM",column="ST_ELIM"),
            @Result(property = "CO_MOTI_DESC",column="CO_MOTI_DESC"), 
            @Result(property = "DE_INFO_DESC",column="DE_INFO_DESC"),
            @Result(property = "DE_OBSERV",column="DE_OBSERV"),
            @Result(property = "CO_NEGO_SUCU_MUDA_PADR",column="CO_NEGO_SUCU_MUDA_PADR"),
            @Result(property = "CO_NEGO_SUCU_MUDA_HIJO",column="CO_NEGO_SUCU_MUDA_HIJO"),
            @Result(property = "nego",column = "CO_NEGO",javaType = NEGO.class,one = @One(select = "com.americatel.facturacion.mappers.mapNEGO.getId"),id = false),
            @Result(property = "sucu",column = "CO_SUCU",javaType = SUCU.class,one = @One(select = "com.americatel.facturacion.mappers.mapSUCU.getId"),id = false),
            @Result(property = "moti_desc",column = "CO_MOTI_DESC",javaType = MOTI_DESC.class,one = @One(select = "com.americatel.facturacion.mappers.mapMOTI_DESC.getId"),id = false),
            @Result(property = "isSuspendido",column = "CO_NEGO_SUCU",javaType = Boolean.class,one = @One(select = "com.americatel.facturacion.mappers.mapSUSP.isSuspendido"),id = false),
            @Result(property = "isMudado",column = "CO_NEGO_SUCU",javaType = Boolean.class,one = @One(select = "com.americatel.facturacion.mappers.mapNEGO_SUCU.isMudado"),id = false),
            @Result(property = "isCesionContractual",column = "CO_NEGO_SUCU",javaType = Boolean.class,one = @One(select = "com.americatel.facturacion.mappers.mapNEGO_SUCU.isCesionContractual"),id = false),
            @Result(property = "isDesactivado",column = "CO_NEGO_SUCU",javaType = Boolean.class,one = @One(select = "com.americatel.facturacion.mappers.mapNEGO_SUCU.isDesactivado"),id = false)
        }
    )     
    public List<NEGO_SUCU> selectByNego(int CO_NEGO);
    
    @Select("SELECT * FROM TMNEGO_SUCU WHERE CO_NEGO=#{CO_NEGO} AND FE_FIN IS NULL AND ST_ELIM=0;")
    @Results(value={
            @Result(property = "CO_NEGO_SUCU",column="CO_NEGO_SUCU",id = true), 
            @Result(property = "CO_NEGO",column="CO_NEGO"), 
            @Result(property = "CO_SUCU",column="CO_SUCU"), 
            @Result(property = "FE_INIC",column="FE_INIC",javaType = Date.class), 
            @Result(property = "FE_FIN",column="FE_FIN",javaType = Date.class), 
            @Result(property = "ST_SOAR_INST",column="ST_SOAR_INST"), 
            @Result(property = "CO_OIT_INST",column="CO_OIT_INST"), 
            @Result(property = "CO_CIRC",column="CO_CIRC"), 
            @Result(property = "DE_ORDE_SERV",column="DE_ORDE_SERV"), 
            @Result(property = "DE_PLAZ_CONT",column="DE_PLAZ_CONT"),   
            @Result(property = "ST_ELIM",column="ST_ELIM"),
            @Result(property = "CO_MOTI_DESC",column="CO_MOTI_DESC"), 
            @Result(property = "DE_INFO_DESC",column="DE_INFO_DESC"),
            @Result(property = "DE_OBSERV",column="DE_OBSERV"),
            @Result(property = "nego",column = "CO_NEGO",javaType = NEGO.class,one = @One(select = "com.americatel.facturacion.mappers.mapNEGO.getId"),id = false),
            @Result(property = "sucu",column = "CO_SUCU",javaType = SUCU.class,one = @One(select = "com.americatel.facturacion.mappers.mapSUCU.getId"),id = false),
            @Result(property = "moti_desc",column = "CO_MOTI_DESC",javaType = MOTI_DESC.class,one = @One(select = "com.americatel.facturacion.mappers.mapMOTI_DESC.getId"),id = false),
            @Result(property = "isSuspendido",column = "CO_NEGO_SUCU",javaType = Boolean.class,one = @One(select = "com.americatel.facturacion.mappers.mapSUSP.isSuspendido"),id = false),
            @Result(property = "isMudado",column = "CO_NEGO_SUCU",javaType = Boolean.class,one = @One(select = "com.americatel.facturacion.mappers.mapNEGO_SUCU.isMudado"),id = false),
            @Result(property = "isCesionContractual",column = "CO_NEGO_SUCU",javaType = Boolean.class,one = @One(select = "com.americatel.facturacion.mappers.mapNEGO_SUCU.isCesionContractual"),id = false),
            @Result(property = "isDesactivado",column = "CO_NEGO_SUCU",javaType = Boolean.class,one = @One(select = "com.americatel.facturacion.mappers.mapNEGO_SUCU.isDesactivado"),id = false)
        }
    )     
    public List<NEGO_SUCU> selectActivosByNego(int CO_NEGO);

    
    @Select("SELECT TOP 1 * FROM TMNEGO_SUCU WHERE ST_SOAR_INST=#{ST_SOAR_INST} AND CO_OIT_INST=#{CO_OIT_INST} AND ST_ELIM=0")
    @Results(value={
            @Result(property = "CO_NEGO_SUCU",column="CO_NEGO_SUCU",id = true), 
            @Result(property = "CO_NEGO",column="CO_NEGO"), 
            @Result(property = "CO_SUCU",column="CO_SUCU"), 
            @Result(property = "FE_INIC",column="FE_INIC"), 
            @Result(property = "FE_FIN",column="FE_FIN"), 
            @Result(property = "ST_SOAR_INST",column="ST_SOAR_INST"), 
            @Result(property = "CO_OIT_INST",column="CO_OIT_INST"), 
            @Result(property = "CO_CIRC",column="CO_CIRC"),
            @Result(property = "DE_ORDE_SERV",column="DE_ORDE_SERV"), 
            @Result(property = "DE_PLAZ_CONT",column="DE_PLAZ_CONT") ,
            @Result(property = "ST_ELIM",column="ST_ELIM"),
            @Result(property = "CO_MOTI_DESC",column="CO_MOTI_DESC"), 
            @Result(property = "DE_INFO_DESC",column="DE_INFO_DESC"),
            @Result(property = "DE_OBSERV",column="DE_OBSERV"),
        }
    )    
    public NEGO_SUCU getByOit(@Param("ST_SOAR_INST") Boolean ST_SOAR_INST,@Param("CO_OIT_INST")  Integer CO_OIT_INST);
    
    @Select("SELECT TOP 1 * FROM TMNEGO_SUCU WHERE ST_SOAR_INST=#{ST_SOAR_INST} AND CO_OIT_INST=#{CO_OIT_INST} AND FE_FIN IS NULL AND ST_ELIM=0")
    @Results(value={
            @Result(property = "CO_NEGO_SUCU",column="CO_NEGO_SUCU",id = true), 
            @Result(property = "CO_NEGO",column="CO_NEGO"), 
            @Result(property = "CO_SUCU",column="CO_SUCU"), 
            @Result(property = "FE_INIC",column="FE_INIC"), 
            @Result(property = "FE_FIN",column="FE_FIN"), 
            @Result(property = "ST_SOAR_INST",column="ST_SOAR_INST"), 
            @Result(property = "CO_OIT_INST",column="CO_OIT_INST"), 
            @Result(property = "CO_CIRC",column="CO_CIRC"),
            @Result(property = "DE_ORDE_SERV",column="DE_ORDE_SERV"), 
            @Result(property = "DE_PLAZ_CONT",column="DE_PLAZ_CONT") ,
            @Result(property = "ST_ELIM",column="ST_ELIM"),
            @Result(property = "CO_MOTI_DESC",column="CO_MOTI_DESC"), 
            @Result(property = "DE_INFO_DESC",column="DE_INFO_DESC"),
            @Result(property = "DE_OBSERV",column="DE_OBSERV"),
        }
    )    
    public NEGO_SUCU validByOit(@Param("ST_SOAR_INST") Boolean ST_SOAR_INST,@Param("CO_OIT_INST")  Integer CO_OIT_INST);
    
    @Select("SELECT * FROM TMNEGO_SUCU WHERE ST_SOAR_INST=#{ST_SOAR_INST} AND CO_OIT_INST=#{CO_OIT_INST} AND CO_NEGO=#{CO_NEGO} AND ST_ELIM=0")
    @Results(value={
            @Result(property = "CO_NEGO_SUCU",column="CO_NEGO_SUCU",id = true), 
            @Result(property = "CO_NEGO",column="CO_NEGO"), 
            @Result(property = "CO_SUCU",column="CO_SUCU"), 
            @Result(property = "FE_INIC",column="FE_INIC"), 
            @Result(property = "FE_FIN",column="FE_FIN"), 
            @Result(property = "ST_SOAR_INST",column="ST_SOAR_INST"), 
            @Result(property = "CO_OIT_INST",column="CO_OIT_INST"), 
            @Result(property = "CO_CIRC",column="CO_CIRC"),
            @Result(property = "DE_ORDE_SERV",column="DE_ORDE_SERV"), 
            @Result(property = "DE_PLAZ_CONT",column="DE_PLAZ_CONT") ,
            @Result(property = "ST_ELIM",column="ST_ELIM"),
            @Result(property = "CO_MOTI_DESC",column="CO_MOTI_DESC"), 
            @Result(property = "DE_INFO_DESC",column="DE_INFO_DESC"),
            @Result(property = "DE_OBSERV",column="DE_OBSERV"),
        }
    )    
    public NEGO_SUCU getByOitNego(@Param("ST_SOAR_INST") Boolean ST_SOAR_INST,@Param("CO_OIT_INST")  Integer CO_OIT_INST, @Param("CO_NEGO")  Integer CO_NEGO);

    @Select("SELECT * FROM TMNEGO_SUCU WHERE CO_CIRC=#{CO_CIRC} AND ST_ELIM=0")
    @Results(value={
            @Result(property = "CO_NEGO_SUCU",column="CO_NEGO_SUCU",id = true), 
            @Result(property = "CO_NEGO",column="CO_NEGO"), 
            @Result(property = "CO_SUCU",column="CO_SUCU"), 
            @Result(property = "FE_INIC",column="FE_INIC"), 
            @Result(property = "FE_FIN",column="FE_FIN"), 
            @Result(property = "ST_SOAR_INST",column="ST_SOAR_INST"), 
            @Result(property = "CO_OIT_INST",column="CO_OIT_INST"), 
            @Result(property = "CO_CIRC",column="CO_CIRC"),
            @Result(property = "DE_ORDE_SERV",column="DE_ORDE_SERV"), 
            @Result(property = "DE_PLAZ_CONT",column="DE_PLAZ_CONT"),
            @Result(property = "ST_ELIM",column="ST_ELIM"),
            @Result(property = "CO_MOTI_DESC",column="CO_MOTI_DESC"), 
            @Result(property = "DE_INFO_DESC",column="DE_INFO_DESC"),
            @Result(property = "DE_OBSERV",column="DE_OBSERV"),
        }
    )     
    public NEGO_SUCU getByCircuito(Integer CO_CIRC);

    
    @Select("SELECT ns.* FROM TMRECI r "
            + "INNER JOIN TMNEGO_SUCU ns ON ns.CO_NEGO=r.CO_NEGO WHERE r.CO_RECI=#{CO_RECI} AND r.ST_ELIM=0 AND ns.ST_ELIM=0;")
    @Results(value={
            @Result(property = "CO_NEGO_SUCU",column="CO_NEGO_SUCU",id = true), 
            @Result(property = "CO_NEGO",column="CO_NEGO"), 
            @Result(property = "CO_SUCU",column="CO_SUCU"), 
            @Result(property = "FE_INIC",column="FE_INIC",javaType = Date.class), 
            @Result(property = "FE_FIN",column="FE_FIN",javaType = Date.class), 
            @Result(property = "ST_SOAR_INST",column="ST_SOAR_INST"), 
            @Result(property = "CO_OIT_INST",column="CO_OIT_INST"), 
            @Result(property = "CO_CIRC",column="CO_CIRC"), 
            @Result(property = "DE_ORDE_SERV",column="DE_ORDE_SERV"), 
            @Result(property = "DE_PLAZ_CONT",column="DE_PLAZ_CONT"),   
            @Result(property = "ST_ELIM",column="ST_ELIM"),
            @Result(property = "CO_MOTI_DESC",column="CO_MOTI_DESC"), 
            @Result(property = "DE_INFO_DESC",column="DE_INFO_DESC"),
            @Result(property = "DE_OBSERV",column="DE_OBSERV"),
            @Result(property = "nego",column = "CO_NEGO",javaType = NEGO.class,one = @One(select = "com.americatel.facturacion.mappers.mapNEGO.getId"),id = false),
            @Result(property = "sucu",column = "CO_SUCU",javaType = SUCU.class,one = @One(select = "com.americatel.facturacion.mappers.mapSUCU.getId"),id = false),
            @Result(property = "moti_desc",column = "CO_MOTI_DESC",javaType = MOTI_DESC.class,one = @One(select = "com.americatel.facturacion.mappers.mapMOTI_DESC.getId"),id = false),
            @Result(property = "isSuspendido",column = "CO_NEGO_SUCU",javaType = Boolean.class,one = @One(select = "com.americatel.facturacion.mappers.mapSUSP.isSuspendido"),id = false),
            @Result(property = "isMudado",column = "CO_NEGO_SUCU",javaType = Boolean.class,one = @One(select = "com.americatel.facturacion.mappers.mapNEGO_SUCU.isMudado"),id = false),
            @Result(property = "isCesionContractual",column = "CO_NEGO_SUCU",javaType = Boolean.class,one = @One(select = "com.americatel.facturacion.mappers.mapNEGO_SUCU.isCesionContractual"),id = false),
            @Result(property = "isDesactivado",column = "CO_NEGO_SUCU",javaType = Boolean.class,one = @One(select = "com.americatel.facturacion.mappers.mapNEGO_SUCU.isDesactivado"),id = false)
        }
    )    
    public List<NEGO_SUCU> getByRECI(Long CO_RECI);
    
    @Select("SELECT ns.* FROM TMBOLE r "
            + "INNER JOIN TMNEGO_SUCU ns ON ns.CO_NEGO=r.CO_NEGO WHERE r.CO_BOLE=#{CO_BOLE} AND r.ST_ELIM=0 AND ns.ST_ELIM=0;")
    @Results(value={
            @Result(property = "CO_NEGO_SUCU",column="CO_NEGO_SUCU",id = true), 
            @Result(property = "CO_NEGO",column="CO_NEGO"), 
            @Result(property = "CO_SUCU",column="CO_SUCU"), 
            @Result(property = "FE_INIC",column="FE_INIC",javaType = Date.class), 
            @Result(property = "FE_FIN",column="FE_FIN",javaType = Date.class), 
            @Result(property = "ST_SOAR_INST",column="ST_SOAR_INST"), 
            @Result(property = "CO_OIT_INST",column="CO_OIT_INST"), 
            @Result(property = "CO_CIRC",column="CO_CIRC"), 
            @Result(property = "DE_ORDE_SERV",column="DE_ORDE_SERV"), 
            @Result(property = "DE_PLAZ_CONT",column="DE_PLAZ_CONT"),   
            @Result(property = "ST_ELIM",column="ST_ELIM"),
            @Result(property = "CO_MOTI_DESC",column="CO_MOTI_DESC"), 
            @Result(property = "DE_INFO_DESC",column="DE_INFO_DESC"),
            @Result(property = "DE_OBSERV",column="DE_OBSERV"),
            @Result(property = "nego",column = "CO_NEGO",javaType = NEGO.class,one = @One(select = "com.americatel.facturacion.mappers.mapNEGO.getId"),id = false),
            @Result(property = "sucu",column = "CO_SUCU",javaType = SUCU.class,one = @One(select = "com.americatel.facturacion.mappers.mapSUCU.getId"),id = false),
            @Result(property = "moti_desc",column = "CO_MOTI_DESC",javaType = MOTI_DESC.class,one = @One(select = "com.americatel.facturacion.mappers.mapMOTI_DESC.getId"),id = false),
            @Result(property = "isSuspendido",column = "CO_NEGO_SUCU",javaType = Boolean.class,one = @One(select = "com.americatel.facturacion.mappers.mapSUSP.isSuspendido"),id = false),
            @Result(property = "isMudado",column = "CO_NEGO_SUCU",javaType = Boolean.class,one = @One(select = "com.americatel.facturacion.mappers.mapNEGO_SUCU.isMudado"),id = false),
            @Result(property = "isCesionContractual",column = "CO_NEGO_SUCU",javaType = Boolean.class,one = @One(select = "com.americatel.facturacion.mappers.mapNEGO_SUCU.isCesionContractual"),id = false),
            @Result(property = "isDesactivado",column = "CO_NEGO_SUCU",javaType = Boolean.class,one = @One(select = "com.americatel.facturacion.mappers.mapNEGO_SUCU.isDesactivado"),id = false)
        }
    )    
    public List<NEGO_SUCU> getByBOLE(Long CO_BOLE);

    
    @Update(
            "UPDATE TMNEGO_SUCU SET CO_OIT_DESI=#{CO_OIT_DESI},ST_SOAR_DESI=#{ST_SOAR_DESI},FE_FIN=#{FE_FIN},CO_MOTI_DESC=#{CO_MOTI_DESC},DE_INFO_DESC=#{DE_INFO_DESC},CO_NEGO_SUCU_MUDA_HIJO=#{CO_NEGO_SUCU_MUDA_HIJO},"+Historial.stringMapperModificar+" WHERE CO_NEGO_SUCU=#{CO_NEGO_SUCU} AND FE_FIN IS NULL AND ST_ELIM=0; " + //sucursal
            "UPDATE TMNEGO_SUCU_PLAN SET ST_SOAR_DESI=#{ST_SOAR_DESI},CO_OIT_DESI=#{CO_OIT_DESI},FE_FIN=#{FE_FIN},"+Historial.stringMapperModificar+" WHERE CO_NEGO_SUCU=#{CO_NEGO_SUCU} AND FE_FIN IS NULL AND ST_ELIM=0; "+//planes
            "UPDATE TMNEGO_SUCU_SERV_SUPL SET ST_SOAR_DESI=#{ST_SOAR_DESI},CO_OIT_DESI=#{CO_OIT_DESI},FE_FIN=#{FE_FIN},"+Historial.stringMapperModificar+" WHERE CO_NEGO_SUCU=#{CO_NEGO_SUCU} AND FE_FIN IS NULL AND ST_ELIM=0;" //servicios
    )
    public void desactivar(NEGO_SUCU nego_sucu);

    @Select(  "SELECT TOP 1 F.FE_ULTI_FACT FROM "
            + "(SELECT FE_ULTI_FACT FROM TMNEGO_SUCU_PLAN  WHERE CO_NEGO_SUCU=#{CO_NEGO_SUCU} AND NOT FE_ULTI_FACT IS NULL AND ST_ELIM=0 UNION "
            + "SELECT FE_ULTI_FACT FROM TMNEGO_SUCU_SERV_SUPL  WHERE CO_NEGO_SUCU=#{CO_NEGO_SUCU} AND NOT FE_ULTI_FACT IS NULL AND ST_ELIM=0) AS F "
            + " ORDER BY F.FE_ULTI_FACT DESC")
    public Date getUltimaFacturacion(NEGO_SUCU aThis);

    @Update ("UPDATE TMNEGO_SUCU  SET CO_NEGO_SUCU_CESI_CONT_PADR=#{CO_NEGO_SUCU_CESI_CONT_PADR} , CO_NEGO_SUCU_CESI_CONT_HIJO=#{CO_NEGO_SUCU_CESI_CONT_HIJO},"+Historial.stringMapperModificar+" WHERE CO_NEGO_SUCU=#{CO_NEGO_SUCU}")
    public void updateCesionContractual(NEGO_SUCU nego_sucu);
    
    @Update ("UPDATE TMNEGO_SUCU  SET CO_NEGO_SUCU_MUDA_PADR=#{CO_NEGO_SUCU_MUDA_PADR} , CO_NEGO_SUCU_MUDA_HIJO=#{CO_NEGO_SUCU_MUDA_HIJO},"+Historial.stringMapperModificar+" WHERE CO_NEGO_SUCU=#{CO_NEGO_SUCU}")
    public void updateMudanza(NEGO_SUCU nego_sucu);

    @Update ("UPDATE TMNEGO_SUCU  SET CO_NEGO=#{CO_NEGO},"+Historial.stringMapperModificar+" WHERE CO_NEGO_SUCU=#{CO_NEGO_SUCU}")
    public void updateCambiarNegocio(NEGO_SUCU nego_sucu);

    @Update ("UPDATE TMNEGO_SUCU SET CO_SUCU=#{CO_SUCU},"+Historial.stringMapperModificar+" WHERE CO_NEGO_SUCU=#{CO_NEGO_SUCU}")
    public void mudar(NEGO_SUCU nego_sucu_cambiar);

    @Select("SELECT * FROM TMNEGO_SUCU WHERE CO_NEGO=#{CO_NEGO} AND CO_OIT_INST=#{CO_OIT_INST} AND ST_ELIM=0")
    @Results(value={
            @Result(property = "CO_NEGO_SUCU",column="CO_NEGO_SUCU",id = true), 
            @Result(property = "CO_NEGO",column="CO_NEGO"), 
            @Result(property = "CO_SUCU",column="CO_SUCU"), 
            @Result(property = "FE_INIC",column="FE_INIC"), 
            @Result(property = "FE_FIN",column="FE_FIN"), 
            @Result(property = "ST_SOAR_INST",column="ST_SOAR_INST"), 
            @Result(property = "CO_OIT_INST",column="CO_OIT_INST"), 
            @Result(property = "CO_CIRC",column="CO_CIRC"),
            @Result(property = "DE_ORDE_SERV",column="DE_ORDE_SERV"), 
            @Result(property = "DE_PLAZ_CONT",column="DE_PLAZ_CONT"),
            @Result(property = "CO_MOTI_DESC",column="CO_MOTI_DESC"), 
            @Result(property = "DE_INFO_DESC",column="DE_INFO_DESC"), 
            @Result(property = "DE_OBSERV",column="DE_OBSERV"),
            @Result(property = "ST_ELIM",column="ST_ELIM"),
        }
    )
    public NEGO_SUCU getByOitAndNego(NEGO_SUCU nego_sucu); 
    
    @Select("SELECT count(*) FROM TMNEGO_SUCU WHERE CO_NEGO_SUCU=#{CO_NEGO_SUCU}"
            + " AND CO_NEGO_SUCU_MUDA_HIJO is not null AND ST_ELIM=0")
    public boolean isMudado(Integer CO_NEGO_SUCU);
    
    @Select("SELECT count(*) FROM TMNEGO_SUCU WHERE CO_NEGO_SUCU=#{CO_NEGO_SUCU}"
            + " AND CO_NEGO_SUCU_CESI_CONT_HIJO is not null AND ST_ELIM=0")
    public boolean isCesionContractual(Integer CO_NEGO_SUCU);
    
    @Select("SELECT CAST( " +
        "CASE WHEN COUNT(*)=0 AND (select COUNT(*) from TMMIGR_NEGO_SUCU WHERE CO_NEGO_ORIG=#{CO_NEGO})>0 THEN 1 " +
        "WHEN COUNT(*)=0 THEN 0 " +
        "WHEN (SELECT COUNT(*) FROM TMNEGO_SUCU WHERE CO_NEGO=#{CO_NEGO} AND ST_ELIM=0 AND FE_FIN IS NULL)>0 THEN 0 " +
        "ELSE 1 " +
        "END AS bit) AS DESACTIVADO " +
        "FROM TMNEGO_SUCU WHERE CO_NEGO=#{CO_NEGO} AND ST_ELIM=0")
    public boolean isNegoDesactivado(Integer CO_NEGO);
    
    @Select("SELECT CAST( " +
        "CASE WHEN COUNT(*)=0 AND (select COUNT(*) from TMMIGR_NEGO_SUCU WHERE CO_NEGO_ORIG=#{CO_NEGO})=0 THEN 1 " +
        "ELSE 0 " +
        "END AS bit) AS PENDIENTE " +
        "FROM TMNEGO_SUCU S " +
        "INNER JOIN TMNEGO_SUCU_PLAN P ON S.CO_NEGO_SUCU=P.CO_NEGO_SUCU " +
        "WHERE S.ST_ELIM=0 AND P.ST_ELIM=0 AND S.CO_NEGO=#{CO_NEGO}")
    public boolean isNegoPendiente(Integer CO_NEGO);
    
    @Select("SELECT COUNT(*) FROM TMNEGO_SUCU WHERE CO_NEGO_SUCU=#{CO_NEGO_SUCU} AND FE_FIN IS NOT NULL AND ST_ELIM=0")
    public boolean isDesactivado(Integer CO_NEGO_SUCU);
    
    @Select("SELECT CAST( " +
        "CASE WHEN COUNT(*) = (SELECT COUNT(*) FROM TMNEGO_SUCU WHERE CO_NEGO=#{CO_NEGO}) THEN 1 " +
        "ELSE 0 " +
        "END AS bit) AS SUSPENDIDO " +
        "FROM TMNEGO_SUCU N INNER JOIN TMSUSP T ON N.CO_NEGO_SUCU=T.CO_NEGO_SUCU AND T.FE_FIN is null AND T.ST_ELIM=0 " +
        "WHERE N.CO_NEGO=#{CO_NEGO} AND N.ST_ELIM=0")
    public boolean isNegoSuspendido(Integer CO_NEGO);
    
    @Select("SELECT S.CO_NEGO_SUCU, S.CO_SUCU, SU.DE_DIRE, D.NO_DIST, PR.NO_PROV, DE.NO_DEPA, SP.CO_NEGO_SUCU_PLAN AS CO_NEGO_SUCU_PLAN_SS, " +
        "'P' AS TIPO, SP.NO_PLAN AS NOMBRE, SP.CO_PLAN AS CO_PLAN_SS, SP.IM_MONTO AS IM_MONTO, M.CO_MONE_FACT, M.NO_MONE_FACT " +
        "FROM TMNEGO_SUCU S " +
        "INNER JOIN TMNEGO_SUCU_PLAN SP ON S.CO_NEGO_SUCU=SP.CO_NEGO_SUCU AND SP.ST_ELIM=0 " +
        "INNER JOIN TMPLAN P ON P.CO_PLAN=SP.CO_PLAN AND P.ST_ELIM=0 " +
        "INNER JOIN TMMONE_FACT M ON P.CO_MONE_FACT=M.CO_MONE_FACT AND M.ST_ELIM=0 " +
        "INNER JOIN TMSUCU SU ON S.CO_SUCU=SU.CO_SUCU AND SU.ST_ELIM=0 " +
        "INNER JOIN TMDIST D ON SU.CO_DIST=D.CO_DIST AND D.ST_ELIM=0 " +
        "INNER JOIN TMPROV PR ON D.CO_PROV=PR.CO_PROV AND PR.ST_ELIM=0 " +
        "INNER JOIN TMDEPA DE ON PR.CO_DEPA=DE.CO_DEPA AND DE.ST_ELIM=0 " +
        "WHERE S.CO_NEGO=#{CO_NEGO} AND SP.FE_FIN IS NULL AND S.ST_ELIM=0 " +
        "UNION " +
        "SELECT S.CO_NEGO_SUCU, S.CO_SUCU, SU.DE_DIRE, D.NO_DIST, PR.NO_PROV, DE.NO_DEPA, SS.CO_NEGO_SUCU_SERV_SUPL AS CO_NEGO_SUCU_PLAN_SS, " +
        "'SS' AS TIPO, SS.NO_SERV_SUPL AS NOMBRE, SS.CO_SERV_SUPL AS CO_PLAN_SS, SS.IM_MONTO AS IM_MONTO, M.CO_MONE_FACT, M.NO_MONE_FACT " +
        "FROM TMNEGO_SUCU S " +
        "INNER JOIN TMNEGO_SUCU_SERV_SUPL SS ON S.CO_NEGO_SUCU=SS.CO_NEGO_SUCU AND SS.ST_ELIM=0 " +
        "INNER JOIN TMSERV_SUPL SP ON SP.CO_SERV_SUPL=SS.CO_SERV_SUPL AND SP.ST_ELIM=0 " +
        "INNER JOIN TMMONE_FACT M ON SP.CO_MONE_FACT=M.CO_MONE_FACT AND M.ST_ELIM=0 " +
        "INNER JOIN TMSUCU SU ON S.CO_SUCU=SU.CO_SUCU AND SU.ST_ELIM=0 " +
        "INNER JOIN TMDIST D ON SU.CO_DIST=D.CO_DIST AND D.ST_ELIM=0 " +
        "INNER JOIN TMPROV PR ON D.CO_PROV=PR.CO_PROV AND PR.ST_ELIM=0 " +
        "INNER JOIN TMDEPA DE ON PR.CO_DEPA=DE.CO_DEPA AND DE.ST_ELIM=0 " +
        "WHERE S.CO_NEGO=#{CO_NEGO} AND SS.FE_FIN IS NULL AND S.ST_ELIM=0")
    public List<Map<String, Object>> getPlanes_ServSuplByCoNego(@Param("CO_NEGO") Integer CO_NEGO);
    
    @Select("SELECT CAST( " +
        "CASE WHEN SUM(CANT) > 0 THEN 1 " +
        "ELSE 0 " +
        "END AS bit) AS DESACTIVADO " +
        "FROM ( " +
        "SELECT COUNT(*) AS CANT FROM TMNEGO_SUCU ns " +
        "INNER JOIN TMNEGO_SUCU_PLAN nsp ON nsp.CO_NEGO_SUCU=ns.CO_NEGO_SUCU " +
        "WHERE ns.CO_NEGO_SUCU=#{CO_NEGO_SUCU} AND nsp.FE_FIN IS NOT NULL AND nsp.FE_ULTI_FACT_FINA IS NULL AND nsp.ST_ELIM=0 " +
        "UNION " +
        "SELECT COUNT(*) AS CANT FROM TMNEGO_SUCU ns " +
        "INNER JOIN TMNEGO_SUCU_SERV_SUPL nss ON nss.CO_NEGO_SUCU=ns.CO_NEGO_SUCU " +
        "WHERE ns.CO_NEGO_SUCU=#{CO_NEGO_SUCU} AND nss.FE_FIN IS NOT NULL AND nss.FE_ULTI_FACT_FINA IS NULL AND nss.ST_ELIM=0 " +
        ") AS T;")
    public boolean isDesactivadoEnPresentCierre(@Param("CO_NEGO_SUCU") Integer CO_NEGO_SUCU);
    
    
    @Select("SELECT ns.* FROM TMFACT r "
            + "INNER JOIN TMNEGO_SUCU ns ON ns.CO_NEGO=r.CO_NEGO WHERE r.CO_FACT=#{CO_FACT} AND r.ST_ELIM=0 AND ns.ST_ELIM=0;")
    @Results(value={
            @Result(property = "CO_NEGO_SUCU",column="CO_NEGO_SUCU",id = true), 
            @Result(property = "CO_NEGO",column="CO_NEGO"), 
            @Result(property = "CO_SUCU",column="CO_SUCU"), 
            @Result(property = "FE_INIC",column="FE_INIC",javaType = Date.class), 
            @Result(property = "FE_FIN",column="FE_FIN",javaType = Date.class), 
            @Result(property = "ST_SOAR_INST",column="ST_SOAR_INST"), 
            @Result(property = "CO_OIT_INST",column="CO_OIT_INST"), 
            @Result(property = "CO_CIRC",column="CO_CIRC"), 
            @Result(property = "DE_ORDE_SERV",column="DE_ORDE_SERV"), 
            @Result(property = "DE_PLAZ_CONT",column="DE_PLAZ_CONT"),   
            @Result(property = "ST_ELIM",column="ST_ELIM"),
            @Result(property = "CO_MOTI_DESC",column="CO_MOTI_DESC"), 
            @Result(property = "DE_INFO_DESC",column="DE_INFO_DESC"),
            @Result(property = "DE_OBSERV",column="DE_OBSERV"),
            @Result(property = "nego",column = "CO_NEGO",javaType = NEGO.class,one = @One(select = "com.americatel.facturacion.mappers.mapNEGO.getId"),id = false),
            @Result(property = "sucu",column = "CO_SUCU",javaType = SUCU.class,one = @One(select = "com.americatel.facturacion.mappers.mapSUCU.getId"),id = false),
            @Result(property = "moti_desc",column = "CO_MOTI_DESC",javaType = MOTI_DESC.class,one = @One(select = "com.americatel.facturacion.mappers.mapMOTI_DESC.getId"),id = false),
            @Result(property = "isSuspendido",column = "CO_NEGO_SUCU",javaType = Boolean.class,one = @One(select = "com.americatel.facturacion.mappers.mapSUSP.isSuspendido"),id = false),
            @Result(property = "isMudado",column = "CO_NEGO_SUCU",javaType = Boolean.class,one = @One(select = "com.americatel.facturacion.mappers.mapNEGO_SUCU.isMudado"),id = false),
            @Result(property = "isCesionContractual",column = "CO_NEGO_SUCU",javaType = Boolean.class,one = @One(select = "com.americatel.facturacion.mappers.mapNEGO_SUCU.isCesionContractual"),id = false),
            @Result(property = "isDesactivado",column = "CO_NEGO_SUCU",javaType = Boolean.class,one = @One(select = "com.americatel.facturacion.mappers.mapNEGO_SUCU.isDesactivado"),id = false)
        }
    )    
    public List<NEGO_SUCU> getByFACT(Long CO_FACT);
    
}
