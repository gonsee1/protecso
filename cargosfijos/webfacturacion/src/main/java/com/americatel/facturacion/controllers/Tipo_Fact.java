/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.controllers;

import com.americatel.facturacion.fncs.ajax;
import com.americatel.facturacion.fncs.fnc;
import com.americatel.facturacion.historial.Historial;
import com.americatel.facturacion.mappers.mapTIPO_FACT;
import com.americatel.facturacion.models.MODU;
import com.americatel.facturacion.models.TIPO_FACT;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
public class Tipo_Fact {
    @Autowired mapTIPO_FACT mapTipo_Fact;
    /*
    @RequestMapping(value="ajax/TIPO_FACT/select/",method = {RequestMethod.POST},produces = {"application/json"})
    public @ResponseBody Map<String,Object> select(HttpServletRequest request,@RequestParam(value = "NO_TIPO_FACT",required = false) String NO_TIPO_FACT){
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (NO_TIPO_FACT==null)
                ajax.setData(respJson,mapTipo_Fact.select(""));
            else
                ajax.setData(respJson,mapTipo_Fact.select(NO_TIPO_FACT));
            ajax.setTodoOk(respJson);
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    }
    */
      
    @RequestMapping(value="/ajax/TIPO_FACT/getid/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> getid(HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (fnc.existsParameters(request, new String[]{"CO_TIPO_FACT"},respJson)){
                int c=fnc.getIntParameter(request,"CO_TIPO_FACT");            
                respJson.put("data",mapTipo_Fact.getId(c));
                ajax.setTodoOk(respJson);
            }
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    }     
    
    @RequestMapping(value="/ajax/TIPO_FACT/select/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> select(@RequestParam(value="id",required = false) String id,HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (id==null){
                String nombre="";
                if (fnc.existsParameter(request, "NO_TIPO_FACT"))
                    nombre=fnc.getStringParameter(request, "NO_TIPO_FACT");
                respJson.put("data", mapTipo_Fact.select(nombre));
            }else{
                respJson.put("data",mapTipo_Fact.getId(Integer.parseInt(id)));            
            }        
            ajax.setTodoOk(respJson);
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    }
    @RequestMapping(value="/ajax/TIPO_FACT/insert/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> insert(HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (fnc.existsParameters(request, new String[]{"NO_TIPO_FACT"},respJson)){
                TIPO_FACT modu=new TIPO_FACT();
                modu.setNO_TIPO_FACT(fnc.getStringParameter(request, "NO_TIPO_FACT"));
                Historial.initInsertar(request, modu);
                mapTipo_Fact.insert(modu);
                ajax.setTodoOk(respJson);
                //}
            }
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    } 
    
    @RequestMapping(value="/ajax/TIPO_FACT/update/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> update(HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (fnc.existsParameters(request, new String[]{"CO_TIPO_FACT","NO_TIPO_FACT"},respJson)){
                TIPO_FACT reg=mapTipo_Fact.getId(fnc.getIntParameter(request, "CO_TIPO_FACT"));
                if (reg!=null){
                    reg.setNO_TIPO_FACT(fnc.getStringParameter(request, "NO_TIPO_FACT"));
                    Historial.initModificar(request, reg);
                    mapTipo_Fact.update(reg);
                    ajax.setTodoOk(respJson);
                }
            }
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    } 
    
    @RequestMapping(value="/ajax/TIPO_FACT/delete/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> delete(HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (fnc.existsParameters(request, new String[]{"CO_TIPO_FACT"},respJson)){
                TIPO_FACT reg=mapTipo_Fact.getId(fnc.getIntParameter(request, "CO_TIPO_FACT"));
                if (reg!=null){
                    mapTipo_Fact.delete(reg);
                    ajax.setTodoOk(respJson);
                }
            }
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    }     
}
