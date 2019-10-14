/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.mappers;

import com.americatel.facturacion.historial.Historial;
import com.americatel.facturacion.models.CIER;
import com.americatel.facturacion.models.NEGO_SUCU;
import com.americatel.facturacion.models.NEGO_SUCU_PROM;
import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 *
 * @author crodas
 */
public interface mapNEGO_SUCU_PROM {
    @Insert("INSERT INTO TMNEGO_SUCU_PROM (CO_NEGO_SUCU,DE_TIPO,IM_VALO,ST_PLAN,ST_SERV_SUPL,DE_ANO_INIC,DE_PERI_INIC,DE_ANO_FIN,DE_PERI_FIN,"+Historial.stringMapperInsertarTitles+") VALUES (#{CO_NEGO_SUCU},#{DE_TIPO},#{IM_VALO},#{ST_PLAN},#{ST_SERV_SUPL},#{DE_ANO_INIC},#{DE_PERI_INIC},#{DE_ANO_FIN},#{DE_PERI_FIN},"+Historial.stringMapperInsertarValuesNumeral+");")
    public void insert(NEGO_SUCU_PROM nego_prom);
    
    @Select("SELECT * FROM TMNEGO_SUCU_PROM WHERE CO_NEGO_SUCU=#{CO_NEGO_SUCU}")
    public List<NEGO_SUCU_PROM> selectByCO_NEGO_SUCU(Integer CO_NEGO_SUCU);

    @Select("SELECT * FROM TMNEGO_SUCU_PROM WHERE CO_NEGO_SUCU=${nego_sucu.getCO_NEGO_SUCU_ForJSON()} AND DE_TIPO=1 "
            + " AND (((DE_ANO_INIC*100+DE_PERI_INIC)<=(${cier.getNU_ANO_ForJSON()}*100+${cier.getNU_PERI_ForJSON()})) AND "
            + " ((DE_ANO_FIN*100+DE_PERI_FIN)>=(${cier.getNU_ANO_ForJSON()}*100+${cier.getNU_PERI_ForJSON()}-1)))")
    public List<NEGO_SUCU_PROM> getPromocionesPorcentajePendientes(@Param("nego_sucu") NEGO_SUCU nego_sucu,@Param("cier") CIER cier);

    @Select("SELECT * FROM TMNEGO_SUCU_PROM WHERE CO_NEGO_SUCU=${nego_sucu.getCO_NEGO_SUCU_ForJSON()} AND DE_TIPO=2 "
            + " AND (((DE_ANO_INIC*100+DE_PERI_INIC)<=(${cier.getNU_ANO_ForJSON()}*100+${cier.getNU_PERI_ForJSON()})) AND "
            + " ((DE_ANO_FIN*100+DE_PERI_FIN)>=(${cier.getNU_ANO_ForJSON()}*100+${cier.getNU_PERI_ForJSON()}-1)))")
    public List<NEGO_SUCU_PROM> getPromocionesMontoPendientes(@Param("nego_sucu") NEGO_SUCU nego_sucu,@Param("cier") CIER cier);

    @Select("SELECT * FROM TMNEGO_SUCU_PROM WHERE CO_NEGO_SUCU=${nego_sucu.getCO_NEGO_SUCU_ForJSON()} "
            + " AND (((DE_ANO_INIC*100+DE_PERI_INIC)<=(${cier.getNU_ANO_ForJSON()}*100+${cier.getNU_PERI_ForJSON()})) AND "
            + " ((DE_ANO_FIN*100+DE_PERI_FIN)>=(${cier.getNU_ANO_ForJSON()}*100+${cier.getNU_PERI_ForJSON()}-1)))")    
    public List<NEGO_SUCU_PROM> getPromocionesPendientes(@Param("nego_sucu") NEGO_SUCU nego_sucu,@Param("cier") CIER cier);
    
}
