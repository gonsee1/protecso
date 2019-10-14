/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.mappers;

import com.americatel.facturacion.historial.Historial;
import com.americatel.facturacion.models.CIER;
import com.americatel.facturacion.models.CIER_DATA_PLAN;
import com.americatel.facturacion.models.CIER_DATA_SERV_SUPL;
import com.americatel.facturacion.models.CIER_DATA_SERV_UNIC;
import com.americatel.facturacion.models.CLIE;
import com.americatel.facturacion.models.DEPA;
import com.americatel.facturacion.models.MONE_FACT;
import com.americatel.facturacion.models.NEGO;
import com.americatel.facturacion.models.NEGO_SUCU_PLAN;
import com.americatel.facturacion.models.PERI_FACT;
import com.americatel.facturacion.models.PROD;
import com.americatel.facturacion.models.ReporteCargaTI;
import com.americatel.facturacion.models.SUCU;
import com.americatel.facturacion.models.TIPO_FACT;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 *
 * @author crodas
 */
public interface mapCIER {  
    
    @Select("SELECT * FROM TMCIER WHERE CO_CIER=#{id} AND ST_ELIM=0 ")
    public CIER getId(int id);
    
    @Select("SELECT * FROM TMNEGO WHERE CO_PROD=#{CO_PROD} AND ST_FACT_DEBU=1 AND ST_ELIM=0 and co_emp='A';")
    @Results(value={
            @Result(property = "CO_SUCU_CORR",column="CO_SUCU_CORR"),
            @Result(property = "sucu_corr",column = "CO_SUCU_CORR",javaType = SUCU.class,one = @One(select = "com.americatel.facturacion.mappers.mapSUCU.getId_paraProcesoFacturacion"),id = false),
            @Result(property = "CO_CLIE",column="CO_CLIE"),
            @Result(property = "clie",column = "CO_CLIE",javaType = CLIE.class,one = @One(select = "com.americatel.facturacion.mappers.mapCLIE.getId_paraProcesoFacturacion"),id = false),            
            @Result(property = "CO_TIPO_FACT",column="CO_TIPO_FACT"),
            @Result(property = "tipo_fact",column = "CO_TIPO_FACT",javaType = TIPO_FACT.class,one = @One(select = "com.americatel.facturacion.mappers.mapTIPO_FACT.getId"),id = false),
            @Result(property = "CO_MONE_FACT",column="CO_MONE_FACT"),
            @Result(property = "mone_fact",column = "CO_MONE_FACT",javaType = MONE_FACT.class,one = @One(select = "com.americatel.facturacion.mappers.mapMONE_FACT.getId"),id = false),
            @Result(property = "CO_PERI_FACT",column="CO_PERI_FACT"),
            @Result(property = "peri_fact",column = "CO_PERI_FACT",javaType = PERI_FACT.class,one = @One(select = "com.americatel.facturacion.mappers.mapPERI_FACT.getId"),id = false)            
        }
    )      
    public List<NEGO> getNegociosParaProcesoCierre(PROD prod); 
    
    @Select("SELECT * FROM TMNEGO WHERE CO_NEGO=#{CO_NEGO} AND ST_FACT_DEBU=1 AND ST_ELIM=0;")
    @Results(value={
            @Result(property = "CO_SUCU_CORR",column="CO_SUCU_CORR"),
            @Result(property = "sucu_corr",column = "CO_SUCU_CORR",javaType = SUCU.class,one = @One(select = "com.americatel.facturacion.mappers.mapSUCU.getId_paraProcesoFacturacion"),id = false),
            @Result(property = "CO_CLIE",column="CO_CLIE"),
            @Result(property = "clie",column = "CO_CLIE",javaType = CLIE.class,one = @One(select = "com.americatel.facturacion.mappers.mapCLIE.getId_paraProcesoFacturacion"),id = false),            
            @Result(property = "CO_TIPO_FACT",column="CO_TIPO_FACT"),
            @Result(property = "tipo_fact",column = "CO_TIPO_FACT",javaType = TIPO_FACT.class,one = @One(select = "com.americatel.facturacion.mappers.mapTIPO_FACT.getId"),id = false),
            @Result(property = "CO_MONE_FACT",column="CO_MONE_FACT"),
            @Result(property = "mone_fact",column = "CO_MONE_FACT",javaType = MONE_FACT.class,one = @One(select = "com.americatel.facturacion.mappers.mapMONE_FACT.getId"),id = false),
            @Result(property = "CO_PERI_FACT",column="CO_PERI_FACT"),
            @Result(property = "peri_fact",column = "CO_PERI_FACT",javaType = PERI_FACT.class,one = @One(select = "com.americatel.facturacion.mappers.mapPERI_FACT.getId"),id = false)            
        }
    )      
    public NEGO getUnNegocioParaProcesoCierre(int CO_NEGO);
    
    @Select("SELECT n.* FROM TMNEGO n INNER JOIN TMNEGO_SUCU s ON n.CO_NEGO=s.CO_NEGO AND s.ST_ELIM=0 WHERE s.CO_NEGO_SUCU=#{CO_NEGO_SUCU} AND n.ST_FACT_DEBU=1 AND n.ST_ELIM=0;")
    @Results(value={
            @Result(property = "CO_SUCU_CORR",column="CO_SUCU_CORR"),
            @Result(property = "sucu_corr",column = "CO_SUCU_CORR",javaType = SUCU.class,one = @One(select = "com.americatel.facturacion.mappers.mapSUCU.getId_paraProcesoFacturacion"),id = false),
            @Result(property = "CO_CLIE",column="CO_CLIE"),
            @Result(property = "clie",column = "CO_CLIE",javaType = CLIE.class,one = @One(select = "com.americatel.facturacion.mappers.mapCLIE.getId_paraProcesoFacturacion"),id = false),            
            @Result(property = "CO_TIPO_FACT",column="CO_TIPO_FACT"),
            @Result(property = "tipo_fact",column = "CO_TIPO_FACT",javaType = TIPO_FACT.class,one = @One(select = "com.americatel.facturacion.mappers.mapTIPO_FACT.getId"),id = false),
            @Result(property = "CO_MONE_FACT",column="CO_MONE_FACT"),
            @Result(property = "mone_fact",column = "CO_MONE_FACT",javaType = MONE_FACT.class,one = @One(select = "com.americatel.facturacion.mappers.mapMONE_FACT.getId"),id = false),
            @Result(property = "CO_PERI_FACT",column="CO_PERI_FACT"),
            @Result(property = "peri_fact",column = "CO_PERI_FACT",javaType = PERI_FACT.class,one = @One(select = "com.americatel.facturacion.mappers.mapPERI_FACT.getId"),id = false)            
        }
    )
    public NEGO getUnNegocioParaProcesoCierreByCoNegoSucu(int CO_NEGO_SUCU);
    
    @Select("SELECT * FROM TMCIER WHERE CO_PROD=#{CO_PROD} AND ST_CIER<>4 AND ST_ELIM=0;") 
    @Results(value={
            @Result(property = "CO_PROD",column="CO_PROD"), 
            @Result(property = "prod",column = "CO_PROD",javaType = PROD.class,one = @One(select = "com.americatel.facturacion.mappers.mapPROD.getId"),id = false)
        }
    )    
    public CIER getCierrePendienteByProducto(PROD prod);//es el que no esta cerrado (4)

    @Update("UPDATE TMCIER SET ST_CIER=#{ST_CIER},FH_INIC=#{FH_INIC},FH_FIN=#{FH_FIN},"+Historial.stringMapperModificar+" WHERE CO_CIER=#{CO_CIER} AND ST_ELIM=0")
    public void updateEstados(CIER cier);  

    @Update("UPDATE TMCIER SET FE_EMIS=#{FE_EMIS},FE_VENC=#{FE_VENC},DE_RAIZ_RECI=#{DE_RAIZ_RECI},DE_RAIZ_FACT=#{DE_RAIZ_FACT},NU_TIPO_CAMB=#{NU_TIPO_CAMB},DE_RAIZ_BOLE=#{DE_RAIZ_BOLE},"+Historial.stringMapperModificar+" WHERE CO_CIER=#{CO_CIER} AND ST_ELIM=0")
    public void updateLanzar(CIER cier);  
    
    @Insert("INSERT INTO TMCIER (CO_PROD,NU_PERI,NU_ANO,DE_RAIZ_RECI,DE_RAIZ_FACT,FE_EMIS,FE_VENC,ST_CIER,NU_TIPO_CAMB,DE_RAIZ_BOLE,"+Historial.stringMapperInsertarTitles+") VALUES (#{CO_PROD},#{NU_PERI},#{NU_ANO},#{DE_RAIZ_RECI},#{DE_RAIZ_FACT},#{FE_EMIS},#{FE_VENC},#{ST_CIER},#{NU_TIPO_CAMB},#{DE_RAIZ_BOLE},"+Historial.stringMapperInsertarValuesNumeral+")")
    public void insert(CIER cier);

    @Select("SELECT * FROM TMCIER WHERE CO_PROD=#{CO_PROD} AND ST_CIER=4 AND ST_ELIM=0 AND NU_PERI=1 AND NU_ANO=2015;")   
    public CIER getCierreCerrado(PROD prod);
    
    //Borra la data degenera en un cierre dado, esto solo se puede hacer si el cierre esta en cierre
    //esto borrara despues de presionar el boton que lanzo el cierre pero antes de genere las facturas
    @Delete(
            //Borrar Promo Monto por sucursal
            "DELETE x " +
            "FROM TMCIER_DATA_PROM_MONT x " +
            "INNER JOIN TMCIER_DATA_SUCU s ON x.CO_CIER_DATA_SUCU=s.CO_CIER_DATA_SUCU "+
            "INNER JOIN TMCIER_DATA_NEGO n ON n.CO_CIER_DATA_NEGO=s.CO_CIER_DATA_NEGO "+
            "INNER JOIN TMCIER c ON c.CO_CIER=n.CO_CIER AND c.CO_CIER=#{CO_CIER} AND c.ST_CIER=2;"+
            //Borrar Promo Monto por negocio
            "DELETE x " +
            "FROM TMCIER_DATA_PROM_MONT x " +
            "INNER JOIN TMCIER_DATA_NEGO n ON n.CO_CIER_DATA_NEGO=x.CO_CIER_DATA_NEGO "+
            "INNER JOIN TMCIER c ON c.CO_CIER=n.CO_CIER AND c.CO_CIER=#{CO_CIER} AND c.ST_CIER=2;"+
            //Borrar Servicio Unico por sucursal
            "DELETE x " +
            "FROM TMCIER_DATA_SERV_UNIC x " +
            "INNER JOIN TMCIER_DATA_SUCU s ON x.CO_CIER_DATA_SUCU=s.CO_CIER_DATA_SUCU "+
            "INNER JOIN TMCIER_DATA_NEGO n ON n.CO_CIER_DATA_NEGO=s.CO_CIER_DATA_NEGO "+
            "INNER JOIN TMCIER c ON c.CO_CIER=n.CO_CIER AND c.CO_CIER=#{CO_CIER} AND c.ST_CIER=2;"+
            //Servicios suplemenarios
            "DELETE x " +
            "FROM TMCIER_DATA_SERV_SUPL x " +
            "INNER JOIN TMCIER_DATA_SUCU s ON x.CO_CIER_DATA_SUCU=s.CO_CIER_DATA_SUCU "+
            "INNER JOIN TMCIER_DATA_NEGO n ON n.CO_CIER_DATA_NEGO=s.CO_CIER_DATA_NEGO "+
            "INNER JOIN TMCIER c ON c.CO_CIER=n.CO_CIER AND c.CO_CIER=#{CO_CIER} AND c.ST_CIER=2;"+ 
            //Planes
            "DELETE x " +
            "FROM TMCIER_DATA_PLAN x " +
            "INNER JOIN TMCIER_DATA_SUCU s ON x.CO_CIER_DATA_SUCU=s.CO_CIER_DATA_SUCU "+
            "INNER JOIN TMCIER_DATA_NEGO n ON n.CO_CIER_DATA_NEGO=s.CO_CIER_DATA_NEGO "+
            "INNER JOIN TMCIER c ON c.CO_CIER=n.CO_CIER AND c.CO_CIER=#{CO_CIER} AND c.ST_CIER=2;"+
            //Borramos sucursales
            "DELETE x " +
            "FROM TMCIER_DATA_SUCU x "+
            "INNER JOIN TMCIER_DATA_NEGO n ON n.CO_CIER_DATA_NEGO=x.CO_CIER_DATA_NEGO "+
            "INNER JOIN TMCIER c ON c.CO_CIER=n.CO_CIER AND c.CO_CIER=#{CO_CIER} AND c.ST_CIER=2;"+
            //Borramos negocios
            "DELETE x " +
            "FROM TMCIER_DATA_NEGO x "+
            "INNER JOIN TMCIER c ON c.CO_CIER=x.CO_CIER AND c.CO_CIER=#{CO_CIER} AND c.ST_CIER=2;"+                        
            //Borramos Glosas Recibos
            "DELETE g " +
            "FROM TMRECI_GLOS g " +
            "INNER JOIN TMRECI r ON r.CO_RECI=g.CO_RECI " +
            "INNER JOIN TMCIER c ON r.CO_CIER=c.CO_CIER AND c.CO_CIER=#{CO_CIER} AND c.ST_CIER=2;" + 
			//Borramos Glosas boletas
			"DELETE g " +
			"FROM TMRECI_GLOS g " +
			"INNER JOIN TMBOLE r ON r.CO_BOLE=g.CO_BOLE " +
			"INNER JOIN TMCIER c ON r.CO_CIER=c.CO_CIER AND c.CO_CIER=#{CO_CIER} AND c.ST_CIER=2;" + 
            //Borramos Glosas Facturas
            "DELETE g " +
            "FROM TMFACT_GLOS g " +
            "INNER JOIN TMFACT r ON r.CO_FACT=g.CO_FACT " +
            "INNER JOIN TMCIER c ON r.CO_CIER=c.CO_CIER AND c.CO_CIER=#{CO_CIER} AND c.ST_CIER=2;" + 
            //Borramos Recibos
            "DELETE r " +
            "FROM TMRECI r " +
            "INNER JOIN TMCIER c ON r.CO_CIER=c.CO_CIER AND c.CO_CIER=#{CO_CIER} AND c.ST_CIER=2;"+
            //Borramos Facturas
            "DELETE r " +
            "FROM TMFACT r " +
            "INNER JOIN TMCIER c ON r.CO_CIER=c.CO_CIER AND c.CO_CIER=#{CO_CIER} AND c.ST_CIER=2;"+
            //Borramos Boletas
            "DELETE r " +
            "FROM TMBOLE r " +
            "INNER JOIN TMCIER c ON r.CO_CIER=c.CO_CIER AND c.CO_CIER=#{CO_CIER} AND c.ST_CIER=2;"+ 
            //Borramos saldos generados
            "DELETE FROM TMNEGO_SALD WHERE CO_CIER=#{CO_CIER};"+
            //Borramos logs
            "DELETE FROM TMCIER_LOGS WHERE CO_CIER=#{CO_CIER};"+
            //Limpiamos el campo CO_CIER de TMNEGO_SUCU_SERV_UNIC de 1 a 0
            "UPDATE TMNEGO_SUCU_SERV_UNIC SET CO_CIER_COBR=NULL WHERE CO_CIER_COBR=#{CO_CIER};"+
            //FE_ULTI_FACT_SGTE a null planes y SS
               //"UPDATE TMNEGO_SUCU_PLAN SET FE_ULTI_FACT_SGTE=null ;"+
               //"UPDATE TMNEGO_SUCU_SERV_SUPL SET FE_ULTI_FACT_SGTE=null;"+
            "UPDATE R SET FE_ULTI_FACT_SGTE=null FROM TMNEGO_SUCU_PLAN AS R INNER JOIN TMNEGO_SUCU NS ON NS.CO_NEGO_SUCU=R.CO_NEGO_SUCU INNER JOIN TMNEGO N ON N.CO_NEGO=NS.CO_NEGO AND N.CO_PROD=#{CO_PROD};"+
            "UPDATE R SET FE_ULTI_FACT_SGTE=null FROM TMNEGO_SUCU_SERV_SUPL AS R INNER JOIN TMNEGO_SUCU NS ON NS.CO_NEGO_SUCU=R.CO_NEGO_SUCU INNER JOIN TMNEGO N ON N.CO_NEGO=NS.CO_NEGO AND N.CO_PROD=#{CO_PROD};"+
            
            //Modificamos los Ajustes aplicados
            "UPDATE TMAJUS SET ST_PEND=1,CO_CIER_APLI=NULL WHERE CO_CIER_APLI=#{CO_CIER};"+
            //Resetear los saldos aproximados
            "UPDATE R SET R.IM_SALD_APROX=0.0 FROM TMNEGO_SUCU_PLAN AS R INNER JOIN TMNEGO_SUCU NS ON NS.CO_NEGO_SUCU=R.CO_NEGO_SUCU INNER JOIN TMNEGO N ON N.CO_NEGO=NS.CO_NEGO AND N.CO_PROD=#{CO_PROD};"+
            "UPDATE R SET R.IM_SALD_APROX=0.0 FROM TMNEGO_SUCU_SERV_SUPL AS R INNER JOIN TMNEGO_SUCU NS ON NS.CO_NEGO_SUCU=R.CO_NEGO_SUCU INNER JOIN TMNEGO N ON N.CO_NEGO=NS.CO_NEGO AND N.CO_PROD=#{CO_PROD};"+
            //Eliminar Detalle de Factura
            "DELETE FROM TMDETALLE_DOCUMENTO WHERE CO_CIER=#{CO_CIER} ;"    
    		
    )    
    public void removeDataGeneradaEnCierre(CIER cier);

    @Select("SELECT * FROM TMCIER WHERE NU_PERI=#{NU_PERI} AND NU_ANO=#{NU_ANO} AND ST_ELIM=0;")
    public List<CIER> getCierres(@Param("NU_ANO") Integer CO_ANO,@Param("NU_PERI") Integer CO_PERI);
    
    @Select("SELECT C.* FROM TMCIER C INNER JOIN TMPROD_PADRE_TMPROD PP ON C.CO_PROD = PP.COD_PROD WHERE C.NU_PERI=#{NU_PERI} AND C.NU_ANO=#{NU_ANO} AND C.ST_ELIM=0 AND PP.COD_PROD_PADRE=#{COD_PROD_PADRE};")
    public List<CIER> getReporteAnulados(@Param("NU_ANO") Integer CO_ANO,@Param("NU_PERI") Integer CO_PERI, @Param("COD_PROD_PADRE") Integer COD_PROD_PADRE);


    @Select("SELECT count(*)  FROM TMRECI WHERE CO_CIER=#{CO_CIER} ")
    public Integer getCantidadRecibosGenerados(CIER aThis);

    @Select("SELECT count(*)  FROM TMFACT WHERE CO_CIER=#{CO_CIER} ")
    public Integer getCantidadFacturasGeneradas(CIER aThis);    
    
    @Select("SELECT count(*)  FROM TMBOLE WHERE CO_CIER=#{CO_CIER} ")
    public Integer getCantidadBoletasGeneradas(CIER aThis); 
     
    //Para el valor temporal de a ultima facturacion FE_ULTI_FACT_SGTE al valor de FE_ULTI_FACT , cuando se cierra un cierre
    @Update( 
            //SS
            "UPDATE R SET R.FE_ULTI_FACT=R.FE_ULTI_FACT_SGTE FROM TMNEGO_SUCU_SERV_SUPL AS R INNER JOIN TMNEGO_SUCU NS ON NS.CO_NEGO_SUCU=R.CO_NEGO_SUCU AND R.FE_ULTI_FACT_SGTE IS NOT NULL INNER JOIN TMNEGO N ON N.CO_NEGO=NS.CO_NEGO AND N.CO_PROD=#{CO_PROD};"+
            "UPDATE R SET R.FE_ULTI_FACT_SGTE=NULL FROM TMNEGO_SUCU_SERV_SUPL AS R INNER JOIN TMNEGO_SUCU NS ON NS.CO_NEGO_SUCU=R.CO_NEGO_SUCU AND R.FE_ULTI_FACT_SGTE IS NOT NULL INNER JOIN TMNEGO N ON N.CO_NEGO=NS.CO_NEGO AND N.CO_PROD=#{CO_PROD};"+
            //PLAN
            "UPDATE R SET R.FE_ULTI_FACT=R.FE_ULTI_FACT_SGTE FROM TMNEGO_SUCU_PLAN AS R INNER JOIN TMNEGO_SUCU NS ON NS.CO_NEGO_SUCU=R.CO_NEGO_SUCU AND R.FE_ULTI_FACT_SGTE IS NOT NULL INNER JOIN TMNEGO N ON N.CO_NEGO=NS.CO_NEGO AND N.CO_PROD=#{CO_PROD};"+
            "UPDATE R SET R.FE_ULTI_FACT_SGTE=NULL FROM TMNEGO_SUCU_PLAN AS R INNER JOIN TMNEGO_SUCU NS ON NS.CO_NEGO_SUCU=R.CO_NEGO_SUCU AND R.FE_ULTI_FACT_SGTE IS NOT NULL INNER JOIN TMNEGO N ON N.CO_NEGO=NS.CO_NEGO AND N.CO_PROD=#{CO_PROD};"+
            //SS se actualiza la ultima fecha de facturacion cuando se da de baja 
            // Antes tenia esta condicion: R.FE_FIN<=R.FE_ULTI_FACT, se le quito porque en el caso que FE_FIN>FE_ULTI_FACT en el caso de devoluciones por cortes/suspensiones sin reconexion no se cumplia.
            "UPDATE R SET R.FE_ULTI_FACT_FINA=R.FE_ULTI_FACT FROM TMNEGO_SUCU_SERV_SUPL AS R INNER JOIN TMNEGO_SUCU NS ON NS.CO_NEGO_SUCU=R.CO_NEGO_SUCU AND R.FE_FIN IS NOT NULL INNER JOIN TMNEGO N ON N.CO_NEGO=NS.CO_NEGO AND N.CO_PROD=#{CO_PROD};"+
            "UPDATE R SET R.FE_ULTI_FACT=R.FE_FIN FROM TMNEGO_SUCU_SERV_SUPL AS R INNER JOIN TMNEGO_SUCU NS ON NS.CO_NEGO_SUCU=R.CO_NEGO_SUCU AND R.FE_FIN IS NOT NULL INNER JOIN TMNEGO N ON N.CO_NEGO=NS.CO_NEGO AND N.CO_PROD=#{CO_PROD};"+
            //PLAN se actualiza la ultima fecha de facturacion cuando se da de baja 
            "UPDATE R SET R.FE_ULTI_FACT_FINA=R.FE_ULTI_FACT FROM TMNEGO_SUCU_PLAN AS R INNER JOIN TMNEGO_SUCU NS ON NS.CO_NEGO_SUCU=R.CO_NEGO_SUCU AND R.FE_FIN IS NOT NULL INNER JOIN TMNEGO N ON N.CO_NEGO=NS.CO_NEGO AND N.CO_PROD=#{CO_PROD};"+
            "UPDATE R SET R.FE_ULTI_FACT=R.FE_FIN FROM TMNEGO_SUCU_PLAN AS R INNER JOIN TMNEGO_SUCU NS ON NS.CO_NEGO_SUCU=R.CO_NEGO_SUCU AND R.FE_FIN IS NOT NULL INNER JOIN TMNEGO N ON N.CO_NEGO=NS.CO_NEGO AND N.CO_PROD=#{CO_PROD};"
    )
    public void congelarPostCierre(CIER aThis);
    
    //Para el valor temporal de a ultima facturacion FE_ULTI_FACT_SGTE al valor de FE_ULTI_FACT , cuando se cierra un cierre
    @Update( 
            "UPDATE R SET R.IM_SALD_APROX=0.0 FROM TMNEGO_SUCU_PLAN AS R INNER JOIN TMNEGO_SUCU NS ON NS.CO_NEGO_SUCU=R.CO_NEGO_SUCU INNER JOIN TMNEGO N ON N.CO_NEGO=NS.CO_NEGO AND N.CO_PROD=#{CO_PROD};"+
            "UPDATE R SET R.IM_SALD_APROX=0.0 FROM TMNEGO_SUCU_SERV_SUPL AS R INNER JOIN TMNEGO_SUCU NS ON NS.CO_NEGO_SUCU=R.CO_NEGO_SUCU INNER JOIN TMNEGO N ON N.CO_NEGO=NS.CO_NEGO AND N.CO_PROD=#{CO_PROD};"
    )
    public void limpiarSaldosAproximados(CIER aThis);

    
    @Select("SELECT * FROM TMCIER WHERE CO_CIER=#{CO_CIER} AND ST_ELIM=0;")
    public CIER getById(Integer CO_CIER);

    @Select("SELECT a.* FROM TMCIER_DATA_PLAN a "
            + "INNER JOIN TMCIER_DATA_SUCU b ON b.CO_CIER_DATA_SUCU=a.CO_CIER_DATA_SUCU "
            + "INNER JOIN TMCIER_DATA_NEGO c ON c.CO_CIER_DATA_NEGO=b.CO_CIER_DATA_NEGO AND c.CO_CIER=#{CO_CIER} "
            + "WHERE a.ST_TIPO_COBR=1;"
    )
    public List<CIER_DATA_PLAN> getDataPlanesCobrados_forMaestroGerentes(CIER aThis);
    
    @Select("SELECT a.* FROM TMCIER_DATA_SERV_SUPL a "
            + "INNER JOIN TMCIER_DATA_SUCU b ON b.CO_CIER_DATA_SUCU=a.CO_CIER_DATA_SUCU "
            + "INNER JOIN TMCIER_DATA_NEGO c ON c.CO_CIER_DATA_NEGO=b.CO_CIER_DATA_NEGO AND c.CO_CIER=#{CO_CIER} "
            + "WHERE a.ST_TIPO_COBR=1;"
    )
    public List<CIER_DATA_SERV_SUPL> getDataServiciosSuplCobrados_forMaestroGerentes(CIER aThis);
    
    @Select("SELECT a.* FROM TMCIER_DATA_SERV_UNIC a "
            + "INNER JOIN TMCIER_DATA_SUCU b ON b.CO_CIER_DATA_SUCU=a.CO_CIER_DATA_SUCU "
            + "INNER JOIN TMCIER_DATA_NEGO c ON c.CO_CIER_DATA_NEGO=b.CO_CIER_DATA_NEGO AND c.CO_CIER=#{CO_CIER}; "
    )
    public List<CIER_DATA_SERV_UNIC> getDataServiciosUnicCobrados_forMaestroGerentes(CIER aThis);

    @Select("SELECT d.* FROM TMCIER_DATA_PLAN a "
            + "INNER JOIN TMCIER_DATA_SUCU b ON b.CO_CIER_DATA_SUCU=a.CO_CIER_DATA_SUCU "
            + "INNER JOIN TMCIER_DATA_NEGO c ON c.CO_CIER_DATA_NEGO=b.CO_CIER_DATA_NEGO AND c.CO_CIER=#{CO_CIER} "
            + "INNER JOIN TMNEGO_SUCU_PLAN d ON d.CO_NEGO_SUCU_PLAN=a.CO_NEGO_SUCU_PLAN "
            + "WHERE a.ST_TIPO_COBR IS NOT NULL;"
    )    
    public List<NEGO_SUCU_PLAN> getNEGO_SUCU_PLAN_forMaestroGerentes(CIER aThis);
    
    @Select("SELECT * FROM TMCIER WHERE ST_CIER=4 AND ST_ELIM=0 AND CO_CIER NOT IN (3,4);")
    @Results(value={
            @Result(property = "CO_PROD",column="CO_PROD"), 
            @Result(property = "prod",column = "CO_PROD",javaType = PROD.class,one = @One(select = "com.americatel.facturacion.mappers.mapPROD.getId"),id = false)
        }
    )    
    public List<CIER> selectCierresCerrados();
    
    @Select("SELECT * FROM TMCIER WHERE ST_CIER=4 AND ST_ELIM=0 AND CO_CIER NOT IN (3,4) AND CO_PROD=#{CO_PROD};")
    @Results(value={
            @Result(property = "CO_PROD",column="CO_PROD"), 
            @Result(property = "prod",column = "CO_PROD",javaType = PROD.class,one = @One(select = "com.americatel.facturacion.mappers.mapPROD.getId"),id = false)
        }
    )
    public List<CIER> getCierresCerradosByProducto(PROD prod);
    
    
    @Select("SELECT * FROM TMCIER WHERE NU_PERI=#{NU_PERI} AND NU_ANO=#{NU_ANO}  AND CO_PROD=#{CO_PROD}  AND ST_ELIM=0;")
    public List<CIER> getCierresByServicio(@Param("NU_ANO") Integer CO_ANO,@Param("NU_PERI") Integer CO_PERI,@Param("CO_PROD") Integer CO_PROD);
    
    @Select("SELECT C.* FROM TMCIER C WHERE C.NU_PERI=#{NU_PERI} AND C.NU_ANO=#{NU_ANO} AND C.ST_ELIM=0 AND C.CO_PROD=#{CO_PROD};")
    public List<CIER> getReporteAnuladosByServicio(@Param("NU_ANO") Integer CO_ANO,@Param("NU_PERI") Integer CO_PERI, @Param("CO_PROD") Integer CO_PROD);

    @Select("SELECT C.* FROM TMCIER C INNER JOIN TMPROD_PADRE_TMPROD PPP ON PPP.COD_PROD = C.CO_PROD "
    		+ "WHERE NU_PERI=#{NU_PERI} AND NU_ANO=#{NU_ANO} AND PPP.COD_PROD_PADRE =#{CO_PROD} AND ST_ELIM=0;")
    public List<CIER> getCierresByProducto(@Param("NU_ANO") Integer CO_ANO,@Param("NU_PERI") Integer CO_PERI,@Param("CO_PROD") Integer CO_PROD);
    /*
    @Select("SELECT * FROM TMCIER WHERE  AND NU_ANO=#{NU_ANO} AND ST_ELIM=0;")
    public List<CIER> getReporteGerenteSunatRecibo(@Param("COD_PROD_PADRE") Integer COD_PROD_PADRE);
    */

//    @Select("SELECT top 1 C.CO_CIER,C.CO_PROD,C.NU_PERI,C.NU_ANO, MAX(F.CO_FACT) FACTMAX, MIN(F.CO_FACT) FACTMIN FROM TMCIER C 	LEFT JOIN TMFACT F ON C.CO_CIER = F.CO_CIER	INNER JOIN TMPROD_PADRE_TMPROD PP ON C.CO_PROD = PP.cod_prod WHERE  PP.cod_prod_padre <> #{cod_prod_padre} GROUP BY C.CO_CIER,C.CO_PROD,C.NU_PERI,C.NU_ANO HAVING  MIN(F.CO_FACT)<=#{cod_inicial} AND MAX(F.CO_FACT) >=#{cod_inicial}")
    @Select("SELECT top 1 C.CO_CIER,C.CO_PROD,C.NU_PERI,C.NU_ANO, MAX(F.CO_FACT) FACTMAX, MIN(F.CO_FACT) FROM TMCIER C 						"+
    		"LEFT JOIN TMFACT F ON C.CO_CIER = F.CO_CIER	INNER JOIN TMPROD_PADRE_TMPROD PP ON C.CO_PROD = PP.cod_prod                    "+
    		"WHERE  PP.cod_prod_padre <> #{cod_prod_padre} or C.CO_CIER NOT IN (select distinct cier.CO_CIER from TMCIER cier              	"+
    		"												INNER JOIN TMPROD_PADRE_TMPROD ProdPadre ON cier.CO_PROD = ProdPadre.cod_prod  	"+
    		"												where ProdPadre.cod_prod_padre = #{cod_prod_padre} and cier.ST_CIER BETWEEN 1 AND 3)"+ 
    		"GROUP BY C.CO_CIER,C.CO_PROD,C.NU_PERI,C.NU_ANO HAVING  MIN(F.CO_FACT)<=#{cod_inicial} AND MAX(F.CO_FACT) >=#{cod_inicial}  	")
    @Results(value={
            @Result(property = "CO_CIER",column="CO_CIER"), 
            @Result(property = "CO_PROD",column = "CO_PROD"),
            @Result(property = "NU_PERI",column = "NU_PERI"),
            @Result(property = "NU_ANO",column = "NU_ANO")
        }
    )
    public CIER validarDisponibilidadCorrelativoFact(@Param("cod_prod_padre") Integer cod_prod_padre,@Param("cod_inicial") Long cod_inicial);
    
    @Select("SELECT top 1 C.CO_CIER,C.CO_PROD,C.NU_PERI,C.NU_ANO, MAX(F.CO_RECI) RECIMAX, MIN(F.CO_RECI) FROM TMCIER C 						"+
    		"LEFT JOIN TMRECI F ON C.CO_CIER = F.CO_CIER	INNER JOIN TMPROD_PADRE_TMPROD PP ON C.CO_PROD = PP.cod_prod                    "+
    		"WHERE  PP.cod_prod_padre <> #{cod_prod_padre} or C.CO_CIER NOT IN (select distinct cier.CO_CIER from TMCIER cier              	"+
    		"												INNER JOIN TMPROD_PADRE_TMPROD ProdPadre ON cier.CO_PROD = ProdPadre.cod_prod  	"+
    		"												where ProdPadre.cod_prod_padre = #{cod_prod_padre} and cier.ST_CIER BETWEEN 1 AND 3)"+ 
    		"GROUP BY C.CO_CIER,C.CO_PROD,C.NU_PERI,C.NU_ANO HAVING  MIN(F.CO_RECI)<=#{cod_inicial} AND MAX(F.CO_RECI) >=#{cod_inicial}  	")
    public CIER validarDisponibilidadCorrelativoReci(@Param("cod_prod_padre") Integer cod_prod_padre,@Param("cod_inicial") Long cod_inicial);
    
    @Select("SELECT top 1 C.CO_CIER,C.CO_PROD,C.NU_PERI,C.NU_ANO, MAX(F.CO_BOLE) BOLEMAX, MIN(F.CO_BOLE) FROM TMCIER C 						"+
    		"LEFT JOIN TMBOLE F ON C.CO_CIER = F.CO_CIER	INNER JOIN TMPROD_PADRE_TMPROD PP ON C.CO_PROD = PP.cod_prod                    "+
    		"WHERE  PP.cod_prod_padre <> #{cod_prod_padre} or C.CO_CIER NOT IN (select distinct cier.CO_CIER from TMCIER cier              	"+
    		"												INNER JOIN TMPROD_PADRE_TMPROD ProdPadre ON cier.CO_PROD = ProdPadre.cod_prod  	"+
    		"												where ProdPadre.cod_prod_padre = #{cod_prod_padre} and cier.ST_CIER BETWEEN 1 AND 3)"+ 
    		"GROUP BY C.CO_CIER,C.CO_PROD,C.NU_PERI,C.NU_ANO HAVING  MIN(F.CO_BOLE)<=#{cod_inicial} AND MAX(F.CO_BOLE) >=#{cod_inicial}  	")
    public CIER validarDisponibilidadCorrelativoBole(@Param("cod_prod_padre") Integer cod_prod_padre,@Param("cod_inicial") Long cod_inicial);

    
    @Select("SELECT 																	"+
    		"('F'+STR(F.CO_FACT)) AS CO_FACT,                                           "+
    		"CONVERT(VARCHAR(10), F.FE_EMIS, 103) AS FE_EMIS,                           "+
    		"F.DE_CODI_BUM	AS DE_CODI_BUM,                                             "+
    		"F.CO_NEGO		AS CO_NEGO,                                                 "+
    		"F.NO_CLIE		AS NO_CLIE,                                                 "+
    		"F.DE_DIRE_FISC	AS DE_DIRE_FISC,                                            "+
    		"F.DE_NUME_DOCU	AS  RUC,                                           			"+
    		"DD.TIP_SERV		AS TIP_SERV,                                            "+
    		"F.NO_MONE_FACT	AS NO_MONE_FACT,                                            "+
    		"                                                                           "+
    		"cast(CASE DD.TIP_SERV                                                      "+
    		"	WHEN 'IAAS - Dedicado' THEN STR(DD.MON_SERVICIO,12,2)                   "+
    		"	ELSE ''                                                                 "+
    		"END AS varchar) AS TI_HOST_DEDI,                                           "+
    		"                                                                           "+
    		"cast(CASE DD.TIP_SERV                                                      "+
    		"	WHEN 'IAAS - Compartido' THEN STR(DD.MON_SERVICIO,12,2)                 "+
    		"	ELSE ''                                                                 "+
    		"END AS varchar) AS TI_HOST_COMP,                                           "+
    		"                                                                           "+
    		"cast(CASE DD.TIP_SERV                                                      "+
    		"	WHEN 'SAAS - SAS' THEN STR(DD.MON_SERVICIO,12,2)                         "+
    		"	ELSE ''                                                                 "+
    		"END AS varchar) AS TI_CLOUD_SAAS,                                          "+
    		"                                                                           "+
    		"cast(CASE DD.TIP_SERV                                                      "+
    		"	WHEN 'IAAS - CB' THEN STR(DD.MON_SERVICIO,12,2)                         "+
    		"	ELSE ''                                                                 "+
    		"END AS varchar) AS TI_CLOUD_IAAS,                                          "+
    		"                                                                           "+
    		"F.IM_VALO_VENT	AS SUB_TOTAL,                                               "+
    		"F.IM_IGV		AS IGV,                                                     "+
    		"F.IM_TOTA		AS TOTAL_FACTURA,                                           "+
    		"CONVERT(VARCHAR(10), F.FE_VENC, 103) AS FE_VENC,                           "+
    		"                                                                           "+
    		"cast(CASE DD.TIP_SERV                                                      "+
    		"	WHEN 'MAS - CB' THEN STR(DD.MON_SERVICIO,12,2)                          "+
    		"	ELSE ''                                                                 "+
    		"END AS varchar) AS TI_CLOUD_MCAFEE,                                        "+
    		"((F.IM_VALO_VENT + F.IM_IGV)-IM_TOTA) AS REDONDEO,                         "+
    		"cast(CASE DD.TIP_SERV                                                      "+
    		"	WHEN 'SAAS - CB' THEN STR(DD.MON_SERVICIO,12,2)                        "+
    		"	ELSE ''                                                                 "+
    		"END AS varchar) AS TI_CLOUD_BACK_UP,                                       "+
    		"DD.ABREV_TIPO_DOCUMENTO AS ABREV_TIPO_DOCUMENTO,                           "+
    		"F.DE_NUME_DOCU	AS  DE_NUME_DOCU,                                           "+
    		"CASE DD.ABREV_TIPO_DOCUMENTO                                               "+
    		"	WHEN 'OD' THEN 'N'                                                      "+
    		"	WHEN 'ND' THEN 'N'                                                      "+
    		"	ELSE 'S'                                                                "+
    		"END				AS DOMICILIADO,                                         "+
    		"F.NO_DIST_FISC	AS DIST_FISC,                                               "+
    		"'FC'			AS TIPO_DOCU,                                               "+
    		"cast(CASE DD.TIP_SERV                                                      "+
    		"	WHEN 'HOU' THEN STR(DD.MON_SERVICIO,12,2)                               "+
    		"	ELSE ''                                                                 "+
    		"END AS varchar) AS TI_HOU_RENTA                                            "+
    		"                                                                           "+
    		"FROM TMFACT F INNER JOIN TMCIER C ON F.CO_CIER = C.CO_CIER                 "+
    		"			  INNER JOIN TMDETALLE_DOCUMENTO DD ON DD.CO_FACT = F.CO_FACT   "+
    		"WHERE C.CO_CIER =#{CO_CIER} AND F.ST_ANUL = 0 AND DD.CO_TIPO_GLOS = 1;									"
    )
    public List<ReporteCargaTI> getDataReporteCargaTI(CIER aThis);
    
}
