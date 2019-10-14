/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.controllers;

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

import com.americatel.facturacion.fncs.ExcelReader;
import com.americatel.facturacion.fncs.FechaHora;
import com.americatel.facturacion.fncs.ajax;
import com.americatel.facturacion.historial.Historial;
import com.americatel.facturacion.mappers.mapCORT;
import com.americatel.facturacion.mappers.mapNEGO;
import com.americatel.facturacion.mappers.mapPROD;
import com.americatel.facturacion.models.CIER;
import com.americatel.facturacion.models.CORT;
import com.americatel.facturacion.models.NEGO;
import com.americatel.facturacion.models.PROD;




/**
 *
 * @author crodas
 */
@Controller
public class Cort {
    @Autowired mapCORT mapCort;
    @Autowired mapPROD mapPROD;
    @Autowired mapNEGO mapNego;
    
    @RequestMapping(value="/ajax/CORT/upload/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> upload(@RequestParam("FL_CORTES") MultipartFile file,HttpServletRequest request,@RequestParam(value="COD_PROD_PADRE", required=true) Integer COD_PROD_PADRE){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if(!file.isEmpty()){
            int limite=2;
            Map<Integer,Object[]> productos=new TreeMap<Integer, Object[]>();
            Map<Integer,NEGO> negocios=new TreeMap<Integer, NEGO>();
            CIER cier=null;
            PROD prod=null;
            NEGO nego=null;
            Object[] arreglo=null;
            Long fecha_inicio_minima=null;//regla no puede cargar menos a dos meses a tras de fecha_inicio            
            //for(PROD tmp_prod : mapPROD.getAll()){ 
            for(PROD tmp_prod : mapPROD.getAllByProduc(COD_PROD_PADRE)){                 
                
                mapCort.quitarPendientes(tmp_prod.getCO_PROD_ForJSON());
                cier=tmp_prod.getCierrePendiente();  
                fecha_inicio_minima=FechaHora.addMonths(FechaHora.getDate(cier.getNU_ANO_ForJSON(), cier.getNU_PERI_ForJSON()-1, 1),limite*-1).getTime();
                productos.put(tmp_prod.getCO_PROD_ForJSON() , new Object[]{tmp_prod,cier,fecha_inicio_minima});
            }         

            int numRows=0;
            int iniRow=2;
            String hoja="CORTES Y  RECO ID DD SAT";                
            ExcelReader excel=new ExcelReader(file);
            Logger.getLogger(Cort.class.getName()).log(Level.INFO, "Cargando archivo de cortes");
            if (excel.selectSheet(hoja)){
                numRows=excel.getNumRows();
                List<String> mensajes=new ArrayList<String>();
                Logger.getLogger(Cort.class.getName()).log(Level.SEVERE, ""+numRows);
                for(;iniRow<=numRows+1;iniRow++){
                    int CO_NEGO=0;                        
                    Date FE_INIC=null;
                    Date FE_FIN=null;
                    Boolean error=false;
                    String mensajeError="";
                    try {
                        CO_NEGO=excel.getValueInteger("D", iniRow);
                        FE_INIC=excel.getValueDate("G", iniRow);
                        FE_FIN=excel.getValueDate("I", iniRow);                               
                    } catch (Exception e) {
                        error=true;
                        System.out.println("Negocio: "+CO_NEGO);
                        System.out.println("Error: "+e.getMessage());
                        Logger.getLogger(Cort.class.getName()).log(Level.SEVERE, "Error en la carga de cortes");
                        Logger.getLogger(Cort.class.getName()).log(Level.SEVERE, null, e.getMessage());
                        mensajeError=e.getMessage();
                    }
                    
                    if (!error){
                        if (!negocios.containsKey(CO_NEGO))
                            negocios.put(CO_NEGO,mapNego.getId(CO_NEGO));
                        nego=negocios.get(CO_NEGO);
                        if (nego!=null){
                            
                          if(nego.getProd_padre_ForJSON().getCO_PROD_PADRE_ForJSON() == COD_PROD_PADRE){        
                                                                                
                            if (productos.containsKey(nego.getCO_PROD_ForJSON())){
                                arreglo=productos.get(nego.getCO_PROD_ForJSON());
                                prod=(PROD)arreglo[0];
                                cier=(CIER)arreglo[1];
                                fecha_inicio_minima=(Long)arreglo[2];
                                //regla no puede cargar menos a dos meses a tras de fecha_inicio_minima
                                if (FE_INIC!=null){
                                	
                                    if (fecha_inicio_minima<FE_INIC.getTime() || FE_FIN==null || (FE_FIN!=null && fecha_inicio_minima<FE_FIN.getTime()) ){
                                    	
                                        CORT c=new CORT();
                                        c.setCO_PROD(prod.getCO_PROD_ForJSON());
                                        c.setCO_NEGO(CO_NEGO);
                                        c.setFE_INIC(FE_INIC);
                                        c.setFE_FIN(FE_FIN); 
                                        if (c.isValid()){
                                            if (c.existsByInicio()){
                                                if (c.isValidForUpdate()){
                                                    //update
                                                    CORT corteExist=c.getCorteValidForUpdate();
                                                    corteExist.setFE_FIN(FE_FIN);
                                                    if (c.isFechasCorrectas()){
                                                        Historial.initModificar(request, corteExist);
                                                        mapCort.update(corteExist);
                                                    }
                                                }
                                            }else{
                                                //Insertamos
                                                if (c.isFechasCorrectas()){
                                                    Historial.initInsertar(request, c);
                                                    mapCort.insert(c);
                                                }
                                            }
                                        }else{
                                            mensajes.add("Registro "+iniRow+" no es valido, por que el negocio no pertenece al producto "+prod.getNO_PROD_ForJSON()+".");
                                        }
                                    }else{
                                        // Insertar en estado atendido
                                        PROD p = new PROD();
                                        p.setCO_PROD(prod.getCO_PROD_ForJSON());
                                        cier=p.getCierreCerrado();
                                        CORT c=new CORT();
                                        c.setCO_PROD(prod.getCO_PROD_ForJSON());
                                        
                                        
                                        if(CO_NEGO == 1414381)
                                        {
                                        	CO_NEGO = 1414381;
                                        	System.out.println("CMC ENTRO NEGOCIO");
                                        	
                                        }
                                        
                                        System.out.println("CMC INGRESO CORTES:  " + CO_NEGO);
                                        System.out.println("fecha_inicio_minima :  " + fecha_inicio_minima);
                                        System.out.println("FE_INIC.getTime() :  " + FE_INIC.getTime());
                                        System.out.println("FE_FIN :  " + FE_FIN);

                                        
                                        c.setCO_NEGO(CO_NEGO);
                                        System.out.println("CO_NEGO ok");
                                        c.setFE_INIC(FE_INIC);
                                        System.out.println("FE_INIC ok");
                                        c.setFE_FIN(FE_FIN);
                                        System.out.println("FE_FIN ok");
                                        
                                        
                                        if(cier != null){
                                        c.setCO_CIER_INIC(cier.getCO_CIER_ForJSON());
                                        System.out.println("setCO_CIER_INIC ok");
                                        c.setCO_CIER_FIN(cier.getCO_CIER_ForJSON());
                                        System.out.println("setCO_CIER_FIN ok");
                                        }
                                        
                 
                                        //Esto es para no insertar lo mismo mas de una vez
                                        if (!c.existsByNegoAndInicio()){
                                            Historial.initInsertar(request, c);
                                            mapCort.insert(c);
                                        }
                                        
                                    }
                                }else{
                                    mensajes.add("Error no tiene fecha de inicio,fila "+iniRow+".");
                                }
                            }else{
                                mensajes.add("Error producto no registrado fila "+iniRow+".");
                            }
                          }else{
                                mensajes.add("el producto del negocio no coincide con la opci\u00f3n seleccionada fila "+iniRow+".");
                          }                              
                        }else{
                            mensajes.add("Error negocio no registrado fila "+iniRow+".");
                        }
                    }else{
                        mensajes.add("Error "+mensajeError+" fila "+iniRow+".");
                    }
                }
                
                ajax.setTodoOk(respJson);
                int total_cargados=mapCort.getCantidadCortesCargados();
                mensajes.add(0,"Registros cargados "+total_cargados);
                if (total_cargados==0) mensajes.add(0,"Recordar que se cargan apartir de la fila  "+iniRow);
                ajax.setData(respJson, mensajes);  
            }else{
                ajax.setMsg(respJson, "No existe la hoja "+hoja);
            }
            
            
        }            
        ajax.setResponseJSON(respJson);        
        return respJson;
    }
    
    @RequestMapping(value="/ajax/CORT/selectCortesPendientesByProducto/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> selectCortesPendientesByProducto(@RequestParam("CO_PROD") int CO_PROD,HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        ajax.setData(respJson, mapCort.selectCortesPendientesByProducto(CO_PROD));
        ajax.setTodoOk(respJson);
        ajax.setResponseJSON(respJson);        
        return respJson;
    }
    
    @RequestMapping(value="/ajax/CORT/selectCortesPendientes/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> selectCortesPendientes(HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
//        ajax.setData(respJson, mapCort.selectCortesPendientes());
        ajax.setData(respJson, mapCort.selectAllCortesConEstado());
        ajax.setTodoOk(respJson);
        ajax.setResponseJSON(respJson);        
        return respJson;
    } 
    
    @RequestMapping(value="/ajax/CORT/selectByNego/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> selectByNego(@RequestParam("CO_NEGO") Integer CO_NEGO,HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (CO_NEGO!=null){
                ajax.setData(respJson, mapCort.selectByNego(CO_NEGO));
                ajax.setTodoOk(respJson);
            }
        }
        ajax.setResponseJSON(respJson);        
        return respJson;
    }    
}
