/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.controllers;

import com.americatel.facturacion.fncs.ajax;
import com.americatel.facturacion.fncs.fnc;
import com.americatel.facturacion.mappers.mapITEM_MODU;
import com.americatel.facturacion.mappers.mapPERF;
import com.americatel.facturacion.models.ITEM_MODU;
import com.americatel.facturacion.models.PERF;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author crodas
 */
@Controller
public class Perf {

    @Autowired mapPERF mapPerf;
    
    @RequestMapping(value="ajax/PERF/select/",method = {RequestMethod.POST},produces = "application/json")
    public @ResponseBody Map<String,Object> select(HttpServletRequest request,@RequestParam(value="NO_ITEM_MODU", required=false) String NO_ITEM_MODU){
        Map<String,Object> respJson=ajax.getResponseJSON();        
        if (ajax.hasPermission(request, respJson)){
            if (NO_ITEM_MODU==null)
                ajax.setData(respJson, mapPerf.select(""));
            else
                ajax.setData(respJson, mapPerf.select(NO_ITEM_MODU));
            ajax.setTodoOk(respJson);
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    }
    
    @RequestMapping(value="ajax/PERF/insert/",method = {RequestMethod.POST},produces = "application/json")
    public @ResponseBody Map<String,Object> insert(HttpServletRequest request,@ModelAttribute(value="PERF") PERF perf){
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            mapPerf.insert(perf);
            ajax.setTodoOk(respJson);
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    }  
    
    @RequestMapping(value="/ajax/PERF/update/",method = {RequestMethod.POST},produces = "application/json")
    public @ResponseBody Map<String,Object> update(HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (fnc.existsParameters(request, new String[]{"CO_PERF","NO_PERF"},respJson)){
                PERF perf=mapPerf.getId(fnc.getIntParameter(request, "CO_PERF"));
                if (perf!=null){
                    perf.setNO_PERF(fnc.getStringParameter(request, "NO_PERF"));
                    mapPerf.update(perf);
                    ajax.setTodoOk(respJson);
                }
            }
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    }     
    

    @RequestMapping(value="/ajax/PERF/getid/",method = {RequestMethod.POST},produces = "application/json")
    public @ResponseBody Map<String,Object> getid(HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (fnc.existsParameters(request, new String[]{"CO_PERF"},respJson)){
                int c=fnc.getIntParameter(request,"CO_PERF");                            
                respJson.put("data",mapPerf.getId(c));
                ajax.setTodoOk(respJson);
            }
            ajax.setResponseJSON(respJson);
        }        
        return respJson;
    } 

    @RequestMapping(value="/ajax/PERF/delete/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> delete(HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (fnc.existsParameters(request, new String[]{"CO_PERF"},respJson)){
                PERF perf=mapPerf.getId(fnc.getIntParameter(request, "CO_PERF"));
                if (perf!=null){
                    mapPerf.delete(perf);
                    ajax.setTodoOk(respJson);
                }
            }
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    }
    
}
