/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.mappers;

import com.americatel.facturacion.historial.Historial;
import com.americatel.facturacion.models.TIPO_GLOS;
import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 *
 * @author rordonez
 */
public interface mapTIPO_GLOS {
    
    @Select("SELECT * FROM TMTIPO_GLOS WHERE CO_TIPO_GLOS=#{id} AND ST_ELIM=0")
    public TIPO_GLOS getId(Integer id);
    
    @Select("SELECT * FROM TMTIPO_GLOS WHERE ST_ELIM=0")
    public List<TIPO_GLOS> getAll();
    
    @Update("UPDATE TMTIPO_GLOS SET NO_TIPO_GLOS='${NO_TIPO_GLOS}',"+Historial.stringMapperModificar+" WHERE CO_TIPO_GLOS=${CO_TIPO_GLOS} AND ST_ELIM=0;")
    public int update(TIPO_GLOS tipo_glos);
    
    @Update("UPDATE TMTIPO_GLOS SET ST_ELIM=1,"+Historial.stringMapperModificar+" WHERE CO_TIPO_GLOS=${CO_TIPO_GLOS};")
    public int delete(TIPO_GLOS tipo_glos);

    @Insert("INSERT INTO TMTIPO_GLOS (NO_TIPO_GLOS,"+Historial.stringMapperInsertarTitles+") VALUES ('${NO_TIPO_GLOS}',"+Historial.stringMapperInsertarValuesNumeral+")")
    public void insert(TIPO_GLOS tipo_glos);

    @Select("SELECT * FROM TMTIPO_GLOS WHERE NO_TIPO_GLOS like '%${NO_TIPO_GLOS}%' AND ST_ELIM=0")
    public List<TIPO_GLOS> select(@Param("NO_TIPO_GLOS") String NO_TIPO_GLOS);    
}
