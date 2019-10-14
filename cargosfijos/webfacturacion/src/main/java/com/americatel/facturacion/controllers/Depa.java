/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.controllers;

import com.americatel.facturacion.fncs.Pagination;
import com.americatel.facturacion.fncs.ajax;
import com.americatel.facturacion.fncs.fnc;
import com.americatel.facturacion.historial.Historial;
import com.americatel.facturacion.mappers.mapDEPA;
import com.americatel.facturacion.models.CLIE;
import com.americatel.facturacion.models.MODU;
import com.americatel.facturacion.models.DEPA;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;




/**
 *
 * @author crodas
 */
@Controller
public class Depa {
    @Autowired mapDEPA mapDepa;
    /*
    @RequestMapping(value="ajax/DEPA/select/",method = {RequestMethod.POST},produces = {"application/json"})
    public @ResponseBody Map<String,Object> select(HttpServletRequest request,@RequestParam(value = "NO_DEPA",required = false) String NO_DEPA){
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (NO_DEPA==null)
                ajax.setData(respJson,mapTipo_Fact.select(""));
            else
                ajax.setData(respJson,mapTipo_Fact.select(NO_DEPA));
            ajax.setTodoOk(respJson);
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    }
    */
      
    @RequestMapping(value="/ajax/DEPA/getid/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> getid(HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (fnc.existsParameters(request, new String[]{"CO_DEPA"},respJson)){
                int c=fnc.getIntParameter(request,"CO_DEPA");            
                respJson.put("data",mapDepa.getId(c));
                ajax.setTodoOk(respJson);
            }
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    }     
    
    @RequestMapping(value="/ajax/DEPA/select/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> select(@ModelAttribute("Pagination") Pagination pagination,@RequestParam(value="SEARCH",required = false) String SEARCH,@RequestParam(value="id",required = false) String id, HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){ 
            if (SEARCH==null){
                if (id==null){
                    String nombre="";
                    if (fnc.existsParameter(request, "NO_DEPA"))
                        nombre=fnc.getStringParameter(request, "NO_DEPA");
                    List<DEPA> lstDepa=mapDepa.select(nombre.trim(),pagination);
                    int son=mapDepa.getNumResultsSelect(nombre.trim());                    
                    ajax.setData(respJson,lstDepa);  
                    ajax.setNumResults(respJson, son);
                }else{                
                    ajax.setData(respJson,mapDepa.getId(Integer.parseInt(id)));            
                }                
            }else{
                ajax.setData(respJson,mapDepa.selectBySEARCH(SEARCH.trim(),pagination));  
                ajax.setNumResults(respJson, mapDepa.getNumResultsSEARCH(SEARCH.trim()));                
            }
            ajax.setTodoOk(respJson);
        }
        
        return respJson;
    }
    @RequestMapping(value="/ajax/DEPA/insert/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> insert(HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (fnc.existsParameters(request, new String[]{"NO_DEPA"},respJson)){
                DEPA modu=new DEPA();
                modu.setNO_DEPA(fnc.getStringParameter(request, "NO_DEPA"));
                DEPA depa_exist = mapDepa.selectByNOM(modu);
                if (depa_exist == null){
                    Historial.initInsertar(request,modu);
                    mapDepa.insert(modu);
                    ajax.setTodoOk(respJson);
                } else {
                    ajax.setError(respJson, "El departamento ya existe.");
                }
            }
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    } 
    
    @RequestMapping(value="/ajax/DEPA/update/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> update(HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (fnc.existsParameters(request, new String[]{"CO_DEPA","NO_DEPA"},respJson)){
                DEPA reg=mapDepa.getId(fnc.getIntParameter(request, "CO_DEPA"));
                if (reg!=null){
                    reg.setNO_DEPA(fnc.getStringParameter(request, "NO_DEPA"));
                    Historial.initModificar(request,reg);
                    mapDepa.update(reg);
                    ajax.setTodoOk(respJson);
                }
            }
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    } 
    
    @RequestMapping(value="/ajax/DEPA/delete/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> delete(HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (fnc.existsParameters(request, new String[]{"CO_DEPA"},respJson)){
                DEPA reg=mapDepa.getId(fnc.getIntParameter(request, "CO_DEPA"));
                if (reg!=null){
                    mapDepa.delete(reg);
                    ajax.setTodoOk(respJson);
                }
            }
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    }     
}
