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
import com.americatel.facturacion.mappers.mapDIST;
import com.americatel.facturacion.models.DEPA;
import com.americatel.facturacion.models.MODU;
import com.americatel.facturacion.models.DIST;
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
public class Dist {
    @Autowired mapDIST mapDist;

      
    @RequestMapping(value="/ajax/DIST/getid/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> getid(HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (fnc.existsParameters(request, new String[]{"CO_DIST"},respJson)){
                int c=fnc.getIntParameter(request,"CO_DIST");            
                respJson.put("data",mapDist.getId(c));
                ajax.setTodoOk(respJson);
            }
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    }     
    
    @RequestMapping(value="/ajax/DIST/select/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> select(@ModelAttribute("Pagination") Pagination pagination,@RequestParam(value="SEARCH",required = false) String SEARCH,@RequestParam(value="id",required = false) String id, HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){ 
            if (SEARCH==null){
                if (id==null){
                    String nombre="";
                    if (fnc.existsParameter(request, "NO_DIST"))
                        nombre=fnc.getStringParameter(request, "NO_DIST");
                    List<DIST> lstDist=mapDist.select(nombre.trim(),pagination);
                    int son=mapDist.getNumResultsSelect(nombre.trim());                    
                    ajax.setData(respJson,lstDist);  
                    ajax.setNumResults(respJson, son);
                }else{                
                    ajax.setData(respJson,mapDist.getId(Integer.parseInt(id)));            
                }                
            }else{
                ajax.setData(respJson,mapDist.selectBySEARCH(SEARCH.trim(),pagination));  
                ajax.setNumResults(respJson, mapDist.getNumResultsSEARCH(SEARCH.trim()));                
            }
            ajax.setTodoOk(respJson);
        }
        
        return respJson;
    }
    
    
    @RequestMapping(value="/ajax/DIST/insert/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> insert(HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (fnc.existsParameters(request, new String[]{"NO_DIST","CO_PROV"},respJson)){
                DIST reg=new DIST();
                reg.setNO_DIST(fnc.getStringParameter(request, "NO_DIST"));
                reg.setCO_PROV(fnc.getIntParameter(request, "CO_PROV"));
                
                DIST dist_exist=mapDist.selectByDepAndProvAndNom(reg);
                if (dist_exist == null){
                    Historial.initInsertar(request, reg);
                    mapDist.insert(reg);
                    ajax.setTodoOk(respJson);
                } else {
                    ajax.setError(respJson, "El distrito ya existe asociado a la provincia seleccionada.");
                }
                
            }
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    } 
    
    @RequestMapping(value="/ajax/DIST/update/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> update(HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (fnc.existsParameters(request, new String[]{"CO_DIST","NO_DIST","CO_PROV"},respJson)){
                DIST reg=mapDist.getId(fnc.getIntParameter(request, "CO_DIST"));
                if (reg!=null){
                    reg.setNO_DIST(fnc.getStringParameter(request, "NO_DIST"));
                    reg.setCO_PROV(fnc.getIntParameter(request, "CO_PROV"));
                    Historial.initModificar(request, reg);
                    mapDist.update(reg);
                    ajax.setTodoOk(respJson);
                }
            }
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    } 
    
    @RequestMapping(value="/ajax/DIST/delete/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> delete(HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (fnc.existsParameters(request, new String[]{"CO_DIST"},respJson)){
                DIST reg=mapDist.getId(fnc.getIntParameter(request, "CO_DIST"));
                if (reg!=null){
                    mapDist.delete(reg);
                    ajax.setTodoOk(respJson);
                }
            }
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    }     

    @RequestMapping(value="/ajax/DIST/selectByProv/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> select(@ModelAttribute("PROV") PROV prov,HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (prov!=null){
                respJson.put("data",mapDist.selectByProv(prov));            
            }        
            ajax.setTodoOk(respJson);
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    }    
}
