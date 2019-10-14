/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.controllers;

import com.americatel.facturacion.fncs.ajax;
import com.americatel.facturacion.fncs.fnc;
import com.americatel.facturacion.historial.Historial;
import com.americatel.facturacion.mappers.mapTIPO_DOCU;
import com.americatel.facturacion.models.MODU;
import com.americatel.facturacion.models.TIPO_DOCU;
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
public class Tipo_Docu {
    @Autowired mapTIPO_DOCU mapTipo_Docu;
      
    @RequestMapping(value="/ajax/TIPO_DOCU/getid/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> getid(HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (fnc.existsParameters(request, new String[]{"CO_TIPO_DOCU"},respJson)){
                int c=fnc.getIntParameter(request,"CO_TIPO_DOCU");            
                respJson.put("data",mapTipo_Docu.getId(c));
                ajax.setTodoOk(respJson);
            }
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    }     
    
    @RequestMapping(value="/ajax/TIPO_DOCU/select/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> select(@RequestParam(value="id",required = false) String id,HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (id==null){
                String nombre="";
                if (fnc.existsParameter(request, "NO_TIPO_DOCU"))
                    nombre=fnc.getStringParameter(request, "NO_TIPO_DOCU");
                respJson.put("data", mapTipo_Docu.select(nombre));
            }else{
                respJson.put("data",mapTipo_Docu.getId(Integer.parseInt(id)));            
            }        
            ajax.setTodoOk(respJson);
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    }
    @RequestMapping(value="/ajax/TIPO_DOCU/insert/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> insert(HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (fnc.existsParameters(request, new String[]{"NO_TIPO_DOCU"},respJson)){
                TIPO_DOCU modu=new TIPO_DOCU();
                modu.setNO_TIPO_DOCU(fnc.getStringParameter(request, "NO_TIPO_DOCU"));
                Historial.initInsertar(request, modu);
                mapTipo_Docu.insert(modu);
                ajax.setTodoOk(respJson);
            }
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    } 
    
    @RequestMapping(value="/ajax/TIPO_DOCU/update/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> update(HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (fnc.existsParameters(request, new String[]{"CO_TIPO_DOCU","NO_TIPO_DOCU"},respJson)){
                TIPO_DOCU reg=mapTipo_Docu.getId(fnc.getIntParameter(request, "CO_TIPO_DOCU"));
                if (reg!=null){
                    reg.setNO_TIPO_DOCU(fnc.getStringParameter(request, "NO_TIPO_DOCU"));
                    Historial.initModificar(request, reg);
                    mapTipo_Docu.update(reg);
                    ajax.setTodoOk(respJson);
                }
            }
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    } 
    
    @RequestMapping(value="/ajax/TIPO_DOCU/delete/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> delete(HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (fnc.existsParameters(request, new String[]{"CO_TIPO_DOCU"},respJson)){
                TIPO_DOCU reg=mapTipo_Docu.getId(fnc.getIntParameter(request, "CO_TIPO_DOCU"));
                if (reg!=null){
                    mapTipo_Docu.delete(reg);
                    ajax.setTodoOk(respJson);
                }
            }
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    }     
}
