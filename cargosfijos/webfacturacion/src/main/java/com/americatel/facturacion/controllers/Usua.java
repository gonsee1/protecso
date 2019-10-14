/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.controllers;

import com.americatel.facturacion.fncs.ajax;
import com.americatel.facturacion.fncs.encrypt;
import com.americatel.facturacion.fncs.fnc;
import com.americatel.facturacion.historial.Historial;
import com.americatel.facturacion.mappers.mapITEM_MODU;
import com.americatel.facturacion.mappers.mapUSUA;
import com.americatel.facturacion.models.ITEM_MODU;
import com.americatel.facturacion.models.MODU;
import com.americatel.facturacion.models.USUA;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.UTF_8;
/**
 *
 * @author crodas
 */
@Controller
public class Usua {
    @Autowired mapUSUA mapUsua;
    
    @RequestMapping(value="ajax/USUA/select/",method = {RequestMethod.POST},produces = "application/json")
    public @ResponseBody Map<String,Object> select(HttpServletRequest request,@ModelAttribute(value="USUA") USUA usua){
        Map<String,Object> respJson=ajax.getResponseJSON();        
        if (ajax.hasPermission(request, respJson)){
            ajax.setData(respJson, mapUsua.select(usua));
            ajax.setTodoOk(respJson);
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    }
    
    @RequestMapping(value="ajax/USUA/insert/",method = {RequestMethod.POST},produces = "application/json")
    public @ResponseBody Map<String,Object> insert(HttpServletRequest request,@ModelAttribute(value="USUA") USUA usua){
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            usua.setDE_PASS(encrypt.md5(usua.getDE_PASS()));
            Historial.initInsertar(request, usua);
            mapUsua.insert(usua);
            ajax.setTodoOk(respJson);
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    }  
    
    @RequestMapping(value="/ajax/USUA/update/",method = {RequestMethod.POST},produces = "application/json")
    public @ResponseBody Map<String,Object> update(HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (fnc.existsParameters(request, new String[]{"CO_USUA","CO_PERF","NO_USUA","AP_USUA","DE_CORR","DE_USER","DE_PASS"},respJson)){
                USUA usua=mapUsua.getId(fnc.getIntParameter(request, "CO_USUA"));
                if (usua!=null){
                    usua.setCO_PERF(fnc.getIntParameter(request, "CO_PERF"));
                    usua.setNO_USUA(fnc.getStringParameter(request, "NO_USUA"));
                    usua.setAP_USUA(fnc.getStringParameter(request, "AP_USUA"));
                    usua.setDE_CORR(fnc.getStringParameter(request, "DE_CORR"));
                    usua.setDE_USER(fnc.getStringParameter(request, "DE_USER"));
                    usua.setDE_PASS(encrypt.md5(fnc.getStringParameter(request, "DE_PASS")));
                    Historial.initModificar(request, usua);
                    mapUsua.update(usua);
                    ajax.setTodoOk(respJson);
                }
            }
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    }     
    

    @RequestMapping(value="/ajax/USUA/getid/",method = {RequestMethod.POST},produces = "application/json")
    public @ResponseBody Map<String,Object> getid(HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (fnc.existsParameters(request, new String[]{"CO_USUA"},respJson)){
                int c=fnc.getIntParameter(request,"CO_USUA");            
                respJson.put("data",mapUsua.getId(c));
                ajax.setTodoOk(respJson);
            }
            ajax.setResponseJSON(respJson);
        }        
        return respJson;
    } 

    @RequestMapping(value="/ajax/USUA/delete/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> delete(HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (fnc.existsParameters(request, new String[]{"CO_USUA"},respJson)){
                USUA usua=mapUsua.getId(fnc.getIntParameter(request, "CO_USUA"));
                if (usua!=null){
                    mapUsua.delete(usua);
                    ajax.setTodoOk(respJson);
                }
            }
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    }    
}
