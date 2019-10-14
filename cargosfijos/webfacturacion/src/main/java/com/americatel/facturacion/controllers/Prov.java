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
import com.americatel.facturacion.mappers.mapPROV;
import com.americatel.facturacion.models.DEPA;
import com.americatel.facturacion.models.MODU;
import com.americatel.facturacion.models.PROV;
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
public class Prov {
    @Autowired mapPROV mapProv;
    /*
    @RequestMapping(value="ajax/PROV/select/",method = {RequestMethod.POST},produces = {"application/json"})
    public @ResponseBody Map<String,Object> select(HttpServletRequest request,@RequestParam(value = "NO_PROV",required = false) String NO_PROV){
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (NO_PROV==null)
                ajax.setData(respJson,mapProv.select(""));
            else
                ajax.setData(respJson,mapProv.select(NO_PROV));
            ajax.setTodoOk(respJson);
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    }
    */
      
    @RequestMapping(value="/ajax/PROV/getid/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> getid(HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (fnc.existsParameters(request, new String[]{"CO_PROV"},respJson)){
                int c=fnc.getIntParameter(request,"CO_PROV");            
                respJson.put("data",mapProv.getId(c));
                ajax.setTodoOk(respJson);
            }
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    }     
    
    @RequestMapping(value="/ajax/PROV/select/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> select(@ModelAttribute("Pagination") Pagination pagination,@RequestParam(value="SEARCH",required = false) String SEARCH,@RequestParam(value="id",required = false) String id, HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){ 
            if (SEARCH==null){
                if (id==null){
                    String nombre="";
                    if (fnc.existsParameter(request, "NO_PROV"))
                        nombre=fnc.getStringParameter(request, "NO_PROV");
                    List<PROV> lstProv=mapProv.select(nombre.trim(),pagination);
                    int son=mapProv.getNumResultsSelect(nombre.trim());                    
                    ajax.setData(respJson,lstProv);  
                    ajax.setNumResults(respJson, son);
                }else{                
                    ajax.setData(respJson,mapProv.getId(Integer.parseInt(id)));            
                }                
            }else{
                ajax.setData(respJson,mapProv.selectBySEARCH(SEARCH.trim(),pagination));  
                ajax.setNumResults(respJson, mapProv.getNumResultsSEARCH(SEARCH.trim()));                
            }
            ajax.setTodoOk(respJson);
        }
        
        return respJson;
    }
    
    
    @RequestMapping(value="/ajax/PROV/insert/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> insert(HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (fnc.existsParameters(request, new String[]{"NO_PROV","CO_DEPA"},respJson)){
                PROV reg=new PROV();
                reg.setNO_PROV(fnc.getStringParameter(request, "NO_PROV"));
                reg.setCO_DEPA(fnc.getIntParameter(request, "CO_DEPA"));
                PROV prov_exist=mapProv.selectByDepaAndNom(reg);
                
                if(prov_exist==null){
                    Historial.initInsertar(request, reg);
                    mapProv.insert(reg);
                    ajax.setTodoOk(respJson);
                } else {
                    ajax.setError(respJson, "La provincia ya existe asociada a dicho departamento.");
                }
                
            }
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    } 
    
    @RequestMapping(value="/ajax/PROV/update/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> update(HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (fnc.existsParameters(request, new String[]{"CO_PROV","NO_PROV","CO_DEPA"},respJson)){
                PROV reg=mapProv.getId(fnc.getIntParameter(request, "CO_PROV"));
                if (reg!=null){
                    reg.setNO_PROV(fnc.getStringParameter(request, "NO_PROV"));
                    reg.setCO_DEPA(fnc.getIntParameter(request, "CO_DEPA"));
                    Historial.initModificar(request, reg);
                    mapProv.update(reg);
                    ajax.setTodoOk(respJson);
                }
            }
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    } 
    
    @RequestMapping(value="/ajax/PROV/delete/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> delete(HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (fnc.existsParameters(request, new String[]{"CO_PROV"},respJson)){
                PROV reg=mapProv.getId(fnc.getIntParameter(request, "CO_PROV"));
                if (reg!=null){
                    mapProv.delete(reg);
                    ajax.setTodoOk(respJson);
                }
            }
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    }     
    
    @RequestMapping(value="/ajax/PROV/selectByDepa/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> select(@ModelAttribute("DEPA") DEPA depa,HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (depa!=null){
                respJson.put("data",mapProv.selectByDepa(depa));            
            }        
            ajax.setTodoOk(respJson);
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    }    
}
