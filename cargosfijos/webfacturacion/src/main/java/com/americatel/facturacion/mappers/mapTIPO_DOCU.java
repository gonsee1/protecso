/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.mappers;

import com.americatel.facturacion.historial.Historial;
import com.americatel.facturacion.models.TIPO_DOCU;
import com.americatel.facturacion.models.TIPO_DOCU;
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
public interface mapTIPO_DOCU {
    
    @Select("SELECT * FROM TMTIPO_DOCU WHERE CO_TIPO_DOCU=#{id} AND ST_ELIM=0")
    public TIPO_DOCU getId(@Param("id") int id);
    
    @Select("SELECT * FROM TMTIPO_DOCU WHERE ST_ELIM=0")
    public List<TIPO_DOCU> getAll();
    
    @Update("UPDATE TMTIPO_DOCU SET NO_TIPO_DOCU='${NO_TIPO_DOCU}',"+Historial.stringMapperModificar+" WHERE CO_TIPO_DOCU=${CO_TIPO_DOCU} AND ST_ELIM=0;")
    public int update(TIPO_DOCU depa);

    //@Delete("DELETE FROM TMTIPO_DOCU WHERE CO_TIPO_DOCU=${CO_TIPO_DOCU};")
    @Update("UPDATE TMTIPO_DOCU SET ST_ELIM=1,"+Historial.stringMapperModificar+" WHERE CO_TIPO_DOCU=${CO_TIPO_DOCU};")
    public int delete(TIPO_DOCU depa);

    @Insert("INSERT INTO TMTIPO_DOCU (NO_TIPO_DOCU,"+Historial.stringMapperInsertarTitles+") VALUES ('${NO_TIPO_DOCU}',"+Historial.stringMapperInsertarValuesNumeral+")")
    public void insert(TIPO_DOCU depa);

    @Select("SELECT * FROM TMTIPO_DOCU WHERE NO_TIPO_DOCU like '%${NO_TIPO_DOCU}%' AND ST_ELIM=0")
    public List<TIPO_DOCU> select(@Param("NO_TIPO_DOCU") String NO_TIPO_DOCU);    
}
