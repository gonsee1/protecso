/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.mappers;

import com.americatel.facturacion.historial.Historial;
import com.americatel.facturacion.models.TIPO_FACT;
import com.americatel.facturacion.models.TIPO_FACT;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 *
 * @author crodas
 */
public interface mapTIPO_FACT {
    
    @Select("SELECT * FROM TMTIPO_FACT WHERE CO_TIPO_FACT=#{id} AND ST_ELIM=0")
    public TIPO_FACT getId(int id);
    
    @Select("SELECT * FROM TMTIPO_FACT WHERE ST_ELIM=0")
    public List<TIPO_FACT> getAll();
    
    @Update("UPDATE TMTIPO_FACT SET NO_TIPO_FACT='${NO_TIPO_FACT}',"+Historial.stringMapperModificar+" WHERE CO_TIPO_FACT=${CO_TIPO_FACT} AND ST_ELIM=0;")
    public int update(TIPO_FACT tipo_fact);

    //@Delete("DELETE FROM TMTIPO_FACT WHERE CO_TIPO_FACT=${CO_TIPO_FACT};")
    @Update("UPDATE TMTIPO_FACT SET ST_ELIM=1,"+Historial.stringMapperModificar+" WHERE CO_TIPO_FACT=${CO_TIPO_FACT};")
    public int delete(TIPO_FACT tipo_fact);

    @Insert("INSERT INTO TMTIPO_FACT (NO_TIPO_FACT,"+Historial.stringMapperInsertarTitles+") VALUES ('${NO_TIPO_FACT}',"+Historial.stringMapperInsertarValuesNumeral+")")
    public void insert(TIPO_FACT tipo_fact);

    @Select("SELECT * FROM TMTIPO_FACT WHERE NO_TIPO_FACT like '%${NO_TIPO_FACT}%' AND ST_ELIM=0")
    public List<TIPO_FACT> select(@Param("NO_TIPO_FACT") String NO_TIPO_FACT);    
}
