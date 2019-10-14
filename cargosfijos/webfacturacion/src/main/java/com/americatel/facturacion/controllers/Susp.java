/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.controllers;

import com.americatel.facturacion.fncs.ExcelReader;
import com.americatel.facturacion.fncs.FechaHora;
import com.americatel.facturacion.fncs.OitUtils;
import com.americatel.facturacion.fncs.ajax;
import com.americatel.facturacion.historial.Historial;
import com.americatel.facturacion.mappers.mapNEGO;
import com.americatel.facturacion.mappers.mapNEGO_SUCU;
import com.americatel.facturacion.mappers.mapPROD;
import com.americatel.facturacion.mappers.mapSUSP;
import com.americatel.facturacion.models.CIER;
import com.americatel.facturacion.models.NEGO;
import com.americatel.facturacion.models.NEGO_SUCU;
import com.americatel.facturacion.models.PROD;
import com.americatel.facturacion.models.SUSP;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;




/**
 *
 * @author crodas
 */
@Controller
public class Susp {
    @Autowired mapSUSP mapSusp;
    @Autowired mapNEGO_SUCU mapNEGO_SUCU;
    @Autowired mapPROD mapPROD;
    @Autowired mapNEGO mapNEGO;
    
    @RequestMapping(value="/ajax/SUSP/upload/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> upload(@RequestParam("FL_SUSPESIONES") MultipartFile file,HttpServletRequest request,@RequestParam(value="COD_PROD_PADRE", required=true) Integer COD_PROD_PADRE){//@RequestBody MODU advancedFormData
        System.out.println("carga susp 1");
        Map<String,Object> respJson=ajax.getResponseJSON();
        if(!file.isEmpty()){
            System.out.println("carga susp 2");
            Map<Integer,Object[]> productos=new TreeMap<Integer, Object[]>();
            Map<Integer,NEGO> negocios=new TreeMap<Integer, NEGO>();
            int limite=2;
            CIER cier=null;
            PROD prod=null;
            NEGO nego=null;
            Object[] arreglo=null;
            Long fecha_inicio_minima=null;//regla no puede cargar menos a dos meses a tras de fecha_inicio            
            //for(PROD tmp_prod : mapPROD.getAll()){ 
            for(PROD tmp_prod : mapPROD.getAllByProduc(COD_PROD_PADRE)){         
                mapSusp.quitarPendientes(tmp_prod.getCO_PROD_ForJSON());
                cier=tmp_prod.getCierrePendiente();  
                fecha_inicio_minima=FechaHora.addMonths(FechaHora.getDate(cier.getNU_ANO_ForJSON(), cier.getNU_PERI_ForJSON()-1, 1),limite*-1).getTime();
                productos.put(tmp_prod.getCO_PROD_ForJSON() , new Object[]{tmp_prod,cier,fecha_inicio_minima});
            }            
            int numRows=0;
            int iniRow=2;
            String hoja="Suspensiones";
            ExcelReader excel=new ExcelReader(file);
            if (excel.selectSheet(hoja)){
                numRows=excel.getNumRows();
                System.out.println("numRows: "+numRows);
                List<String> mensajes=new ArrayList<String>();
                System.out.println("Empieza la carga de suspensiones");
                for(;iniRow<=numRows+1;iniRow++){
                    Boolean error=false;
                    String mensajeError="";

                    int CO_NEGO=0;
                    Date FE_INIC=null;
                    Date FE_FIN=null;
                    String oit="";
                    Object info[]=null;//oit, st_soarc                            
                    try {
                        System.out.println("Empieza.............");
                        CO_NEGO=excel.getValueInteger("C", iniRow);
                        FE_INIC=excel.getValueDate("K", iniRow);
                        FE_FIN=excel.getValueDate("M", iniRow);
                        oit=excel.getValue("AC", iniRow);
                        info=OitUtils.getInfo(oit);//oit, st_soarc  
                        System.out.println("Fin.............");
                    } catch (Exception e) {
                        error=true;
                        System.out.println("Negocio: "+CO_NEGO);
                        System.out.println("Error: "+e.getMessage());
                        Logger.getLogger(Susp.class.getName()).log(Level.SEVERE, "Error en la carga de suspensiones");
                        Logger.getLogger(Susp.class.getName()).log(Level.SEVERE, null, e.getMessage());
                        mensajeError=e.getMessage();
                    }
                    
                    if (!error){
                        NEGO_SUCU nego_sucu=mapNEGO_SUCU.getByOitNego((Boolean) info[1], (Integer) info[0], CO_NEGO);                       
                        
                        if (nego_sucu!=null){
                            if (nego_sucu.getCO_NEGO_ForJSON().equals(CO_NEGO)){                                
                                if (!negocios.containsKey(CO_NEGO))
                                    negocios.put(CO_NEGO,mapNEGO.getId(CO_NEGO));
                                nego=negocios.get(CO_NEGO);
                                if (nego!=null){
                                    
                                  if(nego.getProd_padre_ForJSON().getCO_PROD_PADRE_ForJSON() == COD_PROD_PADRE){ 
                                                                                                                                             
                                    if (productos.containsKey(nego.getCO_PROD_ForJSON())){
                                        arreglo=productos.get(nego.getCO_PROD_ForJSON());
                                        prod=(PROD)arreglo[0];
                                        cier=(CIER)arreglo[1];
                                        fecha_inicio_minima=(Long)arreglo[2];
                                        
                                        if (fecha_inicio_minima<FE_INIC.getTime() || FE_FIN==null || (FE_FIN!=null && fecha_inicio_minima<FE_FIN.getTime()) ){
                                            SUSP s=new SUSP();
                                            s.setCO_PROD(nego.getCO_PROD_ForJSON());
                                            s.setCO_NEGO_SUCU(nego_sucu.getCO_NEGO_SUCU_ForJSON());
                                            s.setFE_INIC(FE_INIC);
                                            s.setFE_FIN(FE_FIN);

                                            s.setCO_OIT_INST((Integer) info[0]);
                                            s.setST_SOAR((Boolean) info[1]);

                                            if (s.isValid()){
                                                if (s.existsByInicio()){
                                                    if (s.isValidForUpdate()){
                                                        SUSP suspExist=s.getSuspensionValidForUpdate();
                                                        suspExist.setFE_FIN(FE_FIN);
                                                        //update
                                                        if (s.isFechasCorrectas()){
                                                            Historial.initModificar(request, suspExist);
                                                            mapSusp.update(suspExist);
                                                        }
                                                    }                                                
                                                }else{
                                                    if (s.isFechasCorrectas()){
                                                        Historial.initInsertar(request, s);
                                                        mapSusp.insert(s);
                                                    }
                                                }                                            
                                            }else{
                                                mensajes.add("Registro "+iniRow+" no es valido, por que el negocio no pertenece al producto "+prod.getNO_PROD_ForJSON()+".");
                                            }
                                        }else{
                                            //mensajes.add("Error, el corte excede el limite de "+limite+" meses atras, fila "+iniRow+"."); 
                                            PROD p = new PROD();
                                            p.setCO_PROD(prod.getCO_PROD_ForJSON());
                                            cier=p.getCierreCerrado();
                                            SUSP s=new SUSP();
                                            s.setCO_PROD(nego.getCO_PROD_ForJSON());
                                            s.setCO_NEGO_SUCU(nego_sucu.getCO_NEGO_SUCU_ForJSON());
                                            s.setFE_INIC(FE_INIC);
                                            s.setFE_FIN(FE_FIN);
                                            s.setCO_OIT_INST((Integer) info[0]);
                                            s.setST_SOAR((Boolean) info[1]);
                                            s.setCO_CIER_INIC(cier.getCO_CIER_ForJSON());
                                            s.setCO_CIER_FIN(cier.getCO_CIER_ForJSON());
                                            //Esto es para no insertar lo mismo mas de una vez
                                            if (!s.existsByNegoAndInicio()){
                                                Historial.initInsertar(request, s);
                                                mapSusp.insert(s);
                                            }
                                            
                                        }
                                    }else{
                                        mensajes.add("Error producto no registrado fila "+iniRow+".");
                                    }
                                  }else{
                                    mensajes.add("Error el producto del negocio no coincide con la opci\u00f3n seleccionada fila "+iniRow+".");
                                  }
                                }else{
                                    mensajes.add("Error negocio no registrado fila "+iniRow+".");
                                }
                            }else{
                                mensajes.add("No pertenece la sucursal con la oit "+oit+" al negocio "+nego_sucu+" fila "+iniRow+".");
                            }
                        }else{
                            mensajes.add("No existe sucursal con la oit "+oit+" fila "+iniRow+".");
                        }
                    }else{
                        mensajes.add("Error "+mensajeError+" fila "+iniRow+".");
                    }
                }
                ajax.setTodoOk(respJson);
                int total_cargados=mapSusp.getCantidadSuspensionesCargados();                
                mensajes.add(0,"Registros cargados "+total_cargados);
                if (total_cargados==0) mensajes.add(0,"Recordar que se cargan apartir de la fila  "+iniRow);
                ajax.setData(respJson, mensajes);
            }else{
                ajax.setError(respJson, "No existe la hoja "+hoja);
            }               
        }
        System.out.println("carga susp 3");
        ajax.setResponseJSON(respJson);        
        return respJson;
    }
    
    @RequestMapping(value="/ajax/SUSP/selectSuspensionesPendientes/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> selectSuspensionesPendientes(HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
//        ajax.setData(respJson, mapSusp.selectSuspensionesPendientes());
        ajax.setData(respJson, mapSusp.selectAllSuspensionesConEstado());
        ajax.setTodoOk(respJson);
        ajax.setResponseJSON(respJson);        
        return respJson;
    }
    
    @RequestMapping(value="/ajax/SUSP/selectByNegoSucu/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> selectByNegoSucu(@RequestParam("CO_NEGO_SUCU") Integer CO_NEGO_SUCU,HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (CO_NEGO_SUCU!=null){
                ajax.setData(respJson, mapSusp.selectByNegoSucu(CO_NEGO_SUCU));
                ajax.setTodoOk(respJson);
            }
        }
        ajax.setResponseJSON(respJson);        
        return respJson;
    }    
    
}
