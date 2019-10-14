/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.historial;

import com.americatel.facturacion.fncs.FechaHora;
import com.americatel.facturacion.fncs.encrypt;
import com.americatel.facturacion.mappers.mapCURSOR;
import com.americatel.facturacion.mappers.mapUSUA;
import com.americatel.facturacion.models.USUA;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author crodas
 */
@Component
public class ReadHistorial {
    private static mapCURSOR mapCursor;
    private static mapUSUA mapUsua;
    private static String USER_NO_EXISTE="---";
    private static String SQL_GETSUCURSAL_SUCU="SELECT DE_DIRE FROM TMSUCU WHERE CO_SUCU=@@1;";
    private static String SQL_GETSUCURSAL_NEGOSUCU="SELECT a.DE_DIRE FROM TMSUCU a INNER JOIN TMNEGO_SUCU b ON a.CO_SUCU=b.CO_SUCU WHERE b.CO_NEGO_SUCU=@@1;";
    private static String SQL_GETNEGOCIO_NEGOSUCU="SELECT CO_NEGO FROM TMNEGO_SUCU WHERE CO_NEGO_SUCU=@@1;";
    private String type=null;
    private Integer idEntity=null;
    
    @Autowired
    public void setmapCURSOR(mapCURSOR mapCursor){
    	ReadHistorial.mapCursor=mapCursor;
    }
    @Autowired
    public void setmapUSUA(mapUSUA mapUsua){
    	ReadHistorial.mapUsua=mapUsua;
    }    
    
    ReadHistorial() {}    
    ReadHistorial(String type, Integer idEntity) {
        this.type=type;
        this.idEntity=idEntity;
    }
    
    public static String getInfoNegocioSucursal(Integer CO_NEGO_SUCU){
        List<Map<String, Object>> info=mapCursor.select("SELECT b.DE_DIRE FROM TMNEGO_SUCU a INNER JOIN TMSUCU b on b.CO_SUCU=a.CO_SUCU WHERE a.CO_NEGO_SUCU="+CO_NEGO_SUCU+";");
        if (info!=null && info.size()>0){
            Map<String, Object> reg=info.get(0);
            return (String)reg.get("DE_DIRE");
        }
        return null;
    }
    
    
    class ValueComparator implements Comparator<Map<String, Object>> {
        public int compare(Map<String, Object> a, Map<String, Object> b) {//times
            Date da=(Date)a.get("time");
            Date db=(Date)b.get("time");
            if (da!=null && db!=null){
                if (da.getTime()>=db.getTime())
                    return -1;
                else
                    return 1;
            }
            return 0;
        }
    }    
    
    /*
    public Map<String, Object> getData(){
        if (this.type.equals("negocio_sucursal")){
            //return this.getDataNegocioSucursal(this.idEntity);
        }else if (this.type.equals("negocio")){
            return this.getNegocio(this.idEntity);
            //return this.getDataNegocio(this.idEntity);
        }
        return null;
    }*/
    
    public List<Map<String, Object>> getData(){
        if (this.type.equals("negocio_sucursal")){
            return this.getDataNegocioSucursal(this.idEntity);
        }else if (this.type.equals("negocio")){
            return this.getDataNegocio(this.idEntity);
        }
        return null;
    }    
  
    private void enumerar(List<Map<String, Object>> as){
        Integer son=as.size();
        for(Map<String, Object> a : as ){
            a.put("#",son );
            son--;
        }
    }    
    
    private void adicionar(List<Map<String, Object>> acumulador,List<Map<String, Object>> adicionar){
        acumulador.addAll(adicionar);
        Collections.sort(acumulador,new ValueComparator());
    }
    
    
    private List<Map<String, Object>> getDataNegocioPromocion(Integer id){        
        return this.getDataDiferente("SELECT * FROM HMNEGO_PROM WHERE CO_NEGO_PROM="+id, new Object[]{
            new Object[]{"DE_TIPO","Tipo",null, new Object[]{new String[]{"1","Por Procentaje"},new String[]{"2","Por Monto"}}},
            new Object[]{"IM_VALO","Valor"},
            new Object[]{"ST_PLAN","Por Plan"},
            new Object[]{"ST_SERV_SUPL","Por Serv. Supl."},
            new Object[]{"ST_PEND","Pendiente"},
        },"Promoci\u00f3n por Negocio");                 
    }
    private List<Map<String, Object>> getDataNegocioSucursalPromocion(Integer id){        
        return this.getDataDiferente("SELECT * FROM HMNEGO_SUCU_PROM WHERE CO_NEGO_SUCU_PROM="+id, new Object[]{
            new Object[]{"DE_TIPO","Tipo",null, new Object[]{new String[]{"1","Por Procentaje"},new String[]{"2","Por Monto"}}},
            new Object[]{"IM_VALO","Valor"},
            new Object[]{"ST_PLAN","Por Plan"},
            new Object[]{"ST_SERV_SUPL","Por Serv. Supl."},
            new Object[]{"ST_PEND","Pendiente"},
            new Object[]{"CO_NEGO_SUCU","Sucursal",SQL_GETSUCURSAL_NEGOSUCU},
        },"Promoci\u00f3n por Sucursal");                 
    }    
    private List<Map<String, Object>> getDataNegocio(Integer id){
        List<Map<String, Object>> sucursales=mapCursor.select("SELECT CO_NEGO_SUCU FROM TMNEGO_SUCU WHERE CO_NEGO="+id);
        List<Map<String, Object>> promociones=mapCursor.select("SELECT CO_NEGO_PROM FROM TMNEGO_PROM WHERE CO_NEGO="+id);
        List<Map<String, Object>> ret=this.getDataDiferente("SELECT * FROM HMNEGO WHERE CO_NEGO="+id, new Object[]{
            new String[]{"CO_SUCU_CORR","Sucursal correspondencia",SQL_GETSUCURSAL_SUCU},
        },"Negocio "+id);
        for(Map<String, Object> sucursal : sucursales){
            Integer sucursal_id=(Integer) sucursal.get("CO_NEGO_SUCU");
            this.adicionar(ret, this.getDataNegocioSucursal(sucursal_id));
        }
        for(Map<String, Object> promocion : promociones){
            Integer promocion_id=(Integer) promocion.get("CO_NEGO_PROM");
            this.adicionar(ret, this.getDataNegocioPromocion(promocion_id));
        }
        this.enumerar(ret);
        return ret;
    }
    
    private List<Map<String, Object>> getDataNegocioSucursalPlanes(Integer id){
        return this.getDataDiferente("SELECT * FROM HMNEGO_SUCU_PLAN WHERE CO_NEGO_SUCU_PLAN="+id, new Object[]{
            new Object[]{"CO_OIT_INST","OIT Instalaci\u00f3n"},
            new Object[]{"NO_PLAN","Plan"},
        },"Planes");        
    }
    private List<Map<String, Object>> getDataNegocioSucursalServiciosSuplementarios(Integer id){
        return this.getDataDiferente("SELECT * FROM HMNEGO_SUCU_SERV_SUPL WHERE CO_NEGO_SUCU_SERV_SUPL="+id, new Object[]{
            new Object[]{"CO_OIT_INST","OIT Instalaci\u00f3n"},
            //new String[]{"NO_PLAN","Plan"},
        },"Servicios Suplementarios");        
    } 
    private List<Map<String, Object>> getDataNegocioSucursalServiciosUnicos(Integer id){
        return this.getDataDiferente("SELECT * FROM HMNEGO_SUCU_SERV_UNIC WHERE CO_NEGO_SUCU_SERV_UNIC="+id, new Object[]{
            new Object[]{"CO_OIT_INST","OIT Instalaci\u00f3n"},
            //new String[]{"NO_PLAN","Plan"},
        },"Servicios Unicos");        
    }     
    private List<Map<String, Object>> getDataNegocioSucursal(Integer id){
        List<Map<String, Object>> planes=mapCursor.select("SELECT CO_NEGO_SUCU_PLAN FROM TMNEGO_SUCU_PLAN WHERE CO_NEGO_SUCU="+id+ " AND ST_ELIM=0");
        List<Map<String, Object>> servicios_suplementarios=mapCursor.select("SELECT CO_NEGO_SUCU_SERV_SUPL FROM TMNEGO_SUCU_SERV_SUPL WHERE CO_NEGO_SUCU="+id+" AND ST_ELIM=0");
        List<Map<String, Object>> servicios_unicos=mapCursor.select("SELECT CO_NEGO_SUCU_SERV_UNIC FROM TMNEGO_SUCU_SERV_UNIC WHERE CO_NEGO_SUCU="+id+" AND ST_ELIM=0");
        List<Map<String, Object>> promociones=mapCursor.select("SELECT CO_NEGO_SUCU_PROM FROM TMNEGO_SUCU_PROM WHERE CO_NEGO_SUCU="+id);
        List<Map<String, Object>> ret=this.getDataDiferente("SELECT * FROM HMNEGO_SUCU WHERE CO_NEGO_SUCU="+id, new Object[]{
            new Object[]{"CO_NEGO","CO_NEGO"},
            new Object[]{"CO_OIT_INST","OIT Instalaci\u00f3n"},
            new Object[]{"FE_INIC","Fecha Inicio"},
            new Object[]{"FE_FIN","Fecha Fin"},
            new Object[]{"CO_CIRC","Circuito"},
            new Object[]{"CO_SUCU","Sucursal",SQL_GETSUCURSAL_SUCU},
            new String[]{"CO_NEGO_SUCU_CESI_CONT_PADR","Cesi\u00f3n Negocio Origen",SQL_GETNEGOCIO_NEGOSUCU},
            new String[]{"CO_NEGO_SUCU_CESI_CONT_HIJO","Cesi\u00f3n Negocio Genero",SQL_GETNEGOCIO_NEGOSUCU},            
        },"Negocio Sucursal "+ReadHistorial.getInfoNegocioSucursal(id));
        for(Map<String, Object> plan : planes){
            Integer plan_id=(Integer) plan.get("CO_NEGO_SUCU_PLAN");
            this.adicionar(ret, this.getDataNegocioSucursalPlanes(plan_id));
        }
        

        for(Map<String, Object> ss : servicios_suplementarios){
            Integer ss_id=(Integer) ss.get("CO_NEGO_SUCU_SERV_SUPL");
            this.adicionar(ret, this.getDataNegocioSucursalServiciosSuplementarios(ss_id));
        }        
        
        for(Map<String, Object> su : servicios_unicos){
            Integer su_id=(Integer) su.get("CO_NEGO_SUCU_SERV_UNIC");
            this.adicionar(ret, this.getDataNegocioSucursalServiciosUnicos(su_id));
        }
        for(Map<String, Object> promocion : promociones){
            Integer promocion_id=(Integer) promocion.get("CO_NEGO_SUCU_PROM");
            this.adicionar(ret, this.getDataNegocioSucursalPromocion(promocion_id));
        }        
        this.enumerar(ret);
        return ret;
    }
    
    private USUA getUsuario(Integer CO_USUA,Map<Integer, USUA> usuarios){
        USUA ret=null;
        if (CO_USUA!=null){
            ret=usuarios.get(CO_USUA);
            if (ret==null){            
                ret=mapUsua.getId(CO_USUA);
                usuarios.put(CO_USUA, ret);
            }
        }
        return ret;
    }
    
    
    
    private List<Map<String, Object>> getDataDiferente(String query,Object params_fields[],String entidad){
        query=query+" ORDER BY FH_MODI DESC;";
        BigInteger numero=encrypt.checkSumMd5(query);
        Map<Integer, USUA> usuarios=new TreeMap<Integer, USUA>();
        List<Map<String, Object>> ret=new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> historicos=mapCursor.select(query);
        List<Object> value_nuevo=null;
        List<Object> fields_names=new ArrayList<Object>();
        List<Object> fields_displays=new ArrayList<Object>();
        List<Object> fields_sqls=new ArrayList<Object>();//obtiene los valores de otra tabla
        List<Object> fields_options=new ArrayList<Object>();//options select ['key','value']
        Map<String, Object> historico_anterior=null;
        Map<String, Object> historico = null;
        for(Object of : params_fields){
            Object fp[]=(Object[]) of;
            if (fp.length>0){
                if (fp.length==1){
                    fields_names.add(fp[0]);
                    fields_displays.add(fp[0]);
                    fields_sqls.add(null);
                    fields_options.add(null);
                }else if (fp.length==2){
                    fields_names.add(fp[0]);
                    fields_displays.add(fp[1]);                              
                    fields_sqls.add(null);
                    fields_options.add(null);
                }else if (fp.length==3){
                    fields_names.add(fp[0]);
                    fields_displays.add(fp[1]);                
                    fields_sqls.add(fp[2]);
                    fields_options.add(null);
                }else if (fp.length==4){
                    fields_names.add(fp[0]);
                    fields_displays.add(fp[1]);                
                    fields_sqls.add(fp[2]);
                    fields_options.add(fp[3]);
                }
            }
        }
        for (int phi=0;phi<historicos.size();phi++){
            historico=historicos.get(phi);
            if(historico!=null){
                if (value_nuevo!=null){
                    List<Integer> pos_diff=new ArrayList<Integer>();
                    Object nuevo=null;
                    Object anterior=null;
                    for(int i=0;i<fields_names.size();i++){                        
                        anterior=value_nuevo.get(i);
                        nuevo=historico.get(fields_names.get(i));
                        if (anterior!=null){
                            if (!anterior.equals(nuevo))
                                pos_diff.add(i);
                        }else if (nuevo!=null){
                            pos_diff.add(i);
                        }
                    }
                    if (pos_diff.size()>0){//Si tiene diferencias
                        String idPadre=numero.toString()+"_"+(ret.size()+1);
                        Map<String, Object> row=new TreeMap<String, Object>();
                        Date FE_MODI=(Date)historico_anterior.get("FH_MODI");
                        Integer CO_USUA=(Integer)historico_anterior.get("CO_USUA_MODI");
                        USUA usua=this.getUsuario(CO_USUA, usuarios);
                        row.put("id",idPadre); 
                        row.put("time",FE_MODI);
                        row.put("entidad",entidad);
                        row.put("accion","Modific\u00f3");
                        //row.put("#",ret.size()+1);                        
                        row.put("FH",FechaHora.getStringDateTime(FE_MODI,"/"));
                        if (usua!=null)
                            row.put("USUA",usua.getDE_USER_ForJSON());
                        else
                            row.put("USUA",USER_NO_EXISTE);                        
                        List<Map<String, Object>> diferencias=new ArrayList<Map<String, Object>>();
                        for (Integer pos : pos_diff){
                            Map<String, Object> diferencia=new TreeMap<String, Object>();
                            diferencia.put("historicoId",idPadre);
                            diferencia.put("campo",fields_displays.get(pos));
                            diferencia.put("nuevo",this.getValue(fields_sqls.get(pos),value_nuevo.get(pos),fields_options.get(pos)));
                            diferencia.put("anterior",this.getValue(fields_sqls.get(pos),historico.get(fields_names.get(pos)),fields_options.get(pos)));
                            diferencias.add(diferencia);
                            //diferencias+=fields_displays.get(pos)+"={antes:"+value_nuevo.get(pos)+",ahora:"+historico.get(fields_names.get(pos))+"};";
                        }
                        row.put("historialCampos", diferencias);
                        ret.add(row);
                    }
                    value_nuevo=new ArrayList<Object>();
                    for(int i=0;i<fields_names.size();i++)
                        value_nuevo.add(historico.get(fields_names.get(i)));                    
                }else{
                    value_nuevo=new ArrayList<Object>();                    
                    for(int i=0;i<fields_names.size();i++){
                        value_nuevo.add(historico.get(fields_names.get(i)));
                    }
                }
                historico_anterior=historico;
            }
            
        }
        //el primero
        if (historicos.size()>0){
            historico=historicos.get(historicos.size()-1);//el perimero es el ultimo ya que esta ordenando desendentemente
            String idPadre=numero.toString()+"_"+(ret.size()+1);
            Map<String, Object> row=new TreeMap<String, Object>();
            Date FE_MODI=(Date)historico.get("FH_MODI");
            Integer CO_USUA=(Integer)historico.get("CO_USUA_MODI");
            USUA usua=this.getUsuario(CO_USUA, usuarios);
            row.put("id",idPadre); 
            row.put("time",FE_MODI);
            row.put("entidad",entidad);
            row.put("accion","Agreg\u00f3");
            //row.put("#",ret.size()+1);                        
            row.put("FH",FechaHora.getStringDateTime(FE_MODI,"/"));
            if (usua!=null)
                row.put("USUA",usua.getDE_USER_ForJSON());
            else
                row.put("USUA",USER_NO_EXISTE);                        
            List<Map<String, Object>> diferencias=new ArrayList<Map<String, Object>>();
            for (int pos=0;pos<fields_names.size();pos++){
                Map<String, Object> diferencia=new TreeMap<String, Object>();
                diferencia.put("historicoId",idPadre);
                diferencia.put("campo",fields_displays.get(pos));
                //diferencia.put("anterior",this.getValue(fields_sqls.get(pos),value_nuevo.get(pos),fields_options.get(pos)));
                diferencia.put("nuevo",this.getValue(fields_sqls.get(pos),historico.get(fields_names.get(pos)),fields_options.get(pos)));
                diferencias.add(diferencia);
                //diferencias+=fields_displays.get(pos)+"={antes:"+value_nuevo.get(pos)+",ahora:"+historico.get(fields_names.get(pos))+"};";
            }
            row.put("historialCampos", diferencias);
            ret.add(row);   
        }
        return ret;
    } 

    private Object getValue(Object query,Object value,Object options){
        Object value_real=this.getValuesSubQuery(query, value);
        if (options!=null){
            value_real=this.getValueOptions(value_real, options);
        }
        if (value_real instanceof Boolean){
            if (value_real==null)
                value_real="";
            else{
                if ((Boolean)value_real)
                    value_real="Si";
                else
                    value_real="No";
            }
        }
        return value_real;
    }
    
    private Object getValuesSubQuery(Object query,Object value){
        Object value_real=value;
        if (query!=null && value!=null){
            String queryString=query.toString();
            queryString=queryString.replace("@@1",  value.toString());
            List<Map<String, Object>> values=mapCursor.select(queryString);
            if (values.size()==1){
                Map<String, Object> res=values.get(0);
                if (res!=null){
                    String ret="";
                    for(Entry<String,Object> entry : res.entrySet()){
                        ret+=(entry.getValue()!=null?entry.getValue().toString():"")+";";
                    }
                    value_real=ret;
                }
            }
        }       
        return value_real;
    }
    
    private Object getValueOptions(Object value,Object options){
        if (options!=null){            
            boolean encontro=false;
            for(Object reg : (Object[]) options){                
                String r[]=(String [])reg;
                if (r.length==2){
                    if (value.toString().equals(r[0])){
                        value=r[1];
                        encontro=true;
                        break;
                    }
                }
            }
            if (!encontro)
                value="";
        }
        return value;
    }
    
    
    
    ///Nueva version
    
    public Map<Integer, String> getDiccionario(String sql,String value,String display){
        Map<Integer, String> ret=new TreeMap<Integer, String>();
        List<Map<String, Object>> datas=mapCursor.select(sql);
        if (datas!=null){
            Integer key=null;
            String val=null;
            for(Map<String, Object> data : datas){
                key=(Integer) data.get(value);
                val=(String) data.get(display);
                ret.put(key, val);
            }
        }
        return ret;
    }
    
    public Map<Integer, String> getDiccionarioSucursalesByNegocio(Integer CO_NEGO){
        return this.getDiccionario("SELECT s.CO_SUCU,s.DE_DIRE FROM HMNEGO n INNER JOIN HMNEGO_SUCU ns ON ns.CO_NEGO=n.CO_NEGO INNER JOIN HMSUCU s ON s.CO_SUCU=ns.CO_SUCU WHERE n.CO_NEGO="+CO_NEGO+" GROUP BY s.CO_SUCU,s.DE_DIRE","CO_SUCU","DE_DIRE");
    }
    
    public Map<String, Object> getDiferencias(Map<String, Object> antes,Map<String, Object> ahora){
        Map<String, Object> ret=new TreeMap<String, Object>();
        for (String key : ahora.keySet()){
            if (!ahora.get(key).equals(antes.get(key))){
                ret.put(key, ahora.get(key));
            }
        }            
        return ret;
    }
    
    public List<Map<String, Object>> getDiferencias(List<Map<String, Object>> registros){
        List<Map<String, Object>> ret=new ArrayList<Map<String, Object>>();//new TreeMap<String, Object>()
        int van=0;
        Map<String, Object> antes=null;
        Map<String, Object> diff=null;
        for (Map<String, Object> ahora : registros){
            if (van==0){
                ret.add(ahora);
            }else{
                diff=this.getDiferencias(antes,ahora);
                if (diff!=null)
                    ret.add(diff);  
            }
            antes=ahora;
            van++;
        }
        return ret;
    } 
    
    
    
    public Map<String, Object> getNegocio(Integer CO_NEGO){
        Map<String, Object> ret=null;
        if (CO_NEGO!=null){
            ret=new TreeMap<String, Object>();
            List<Map<String, Object>> historicos=mapCursor.select("SELECT n.* FROM HMNEGO n WHERE n.CO_NEGO="+CO_NEGO+" ORDER BY n.FH_MODI DESC");
            if (historicos!=null && historicos.size()>0){                
                ret.put("negocio", this.getDiferencias(historicos));
                List<Map<String, Object>> historicos_sucursales=mapCursor.select("SELECT ns.* FROM HMNEGO n INNER JOIN HMNEGO_SUCU ns ON ns.CO_NEGO=n.CO_NEGO WHERE n.CO_NEGO="+CO_NEGO+" ORDER BY ns.CO_SUCU,ns.FH_MODI DESC");
                if (historicos_sucursales!=null && historicos_sucursales.size()>0){
                    Map<Integer, String> sucursales=this.getDiccionarioSucursalesByNegocio(CO_NEGO);
                    
                    List<Map<String, Object>> historicos_sucursales_planes=mapCursor.select("SELECT nsp.* FROM HMNEGO n INNER JOIN HMNEGO_SUCU ns ON ns.CO_NEGO=n.CO_NEGO INNER JOIN HMNEGO_SUCU_PLAN nsp ON nsp.CO_NEGO_SUCU=ns.CO_NEGO_SUCU WHERE n.CO_NEGO="+CO_NEGO+" ORDER BY nsp.FH_MODI DESC");
                    List<Map<String, Object>> historicos_sucursales_sss=mapCursor.select("SELECT nss.* FROM HMNEGO n INNER JOIN HMNEGO_SUCU ns ON ns.CO_NEGO=n.CO_NEGO INNER JOIN HMNEGO_SUCU_SERV_SUPL nss ON nss.CO_NEGO_SUCU=ns.CO_NEGO_SUCU WHERE n.CO_NEGO="+CO_NEGO+" ORDER BY nss.FH_MODI DESC");
                }
            }
        }
        return ret;
    }
    
    
}
