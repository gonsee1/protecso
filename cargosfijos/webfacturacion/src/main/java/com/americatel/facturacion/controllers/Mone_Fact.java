/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.controllers;

import com.americatel.facturacion.fncs.ajax;
import com.americatel.facturacion.fncs.fnc;
import com.americatel.facturacion.historial.Historial;
import com.americatel.facturacion.mappers.mapMONE_FACT;
import com.americatel.facturacion.mappers.mapTIPO_FACT;
import com.americatel.facturacion.models.MODU;
import com.americatel.facturacion.models.MONE_FACT;
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
public class Mone_Fact {
    @Autowired mapMONE_FACT mapMone_Fact;

    @RequestMapping(value="/ajax/MONE_FACT/getid/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> getid(HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (fnc.existsParameters(request, new String[]{"CO_MONE_FACT"},respJson)){
                int c=fnc.getIntParameter(request,"CO_MONE_FACT");            
                respJson.put("data",mapMone_Fact.getId(c));
                ajax.setTodoOk(respJson);
            }
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    }     
    
    @RequestMapping(value="/ajax/MONE_FACT/select/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> select(@RequestParam(value="id",required = false) String id,HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (id==null){
                String nombre="";
                if (fnc.existsParameter(request, "NO_MONE_FACT"))
                    nombre=fnc.getStringParameter(request, "NO_MONE_FACT");
                respJson.put("data", mapMone_Fact.select(nombre));
            }else{
                respJson.put("data",mapMone_Fact.getId(Integer.parseInt(id)));            
            }        
            ajax.setTodoOk(respJson);
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    }
    @RequestMapping(value="/ajax/MONE_FACT/insert/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> insert(HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (fnc.existsParameters(request, new String[]{"NO_MONE_FACT","DE_SIMB"},respJson)){
                MONE_FACT reg=new MONE_FACT();
                reg.setNO_MONE_FACT(fnc.getStringParameter(request, "NO_MONE_FACT"));
                reg.setDE_SIMB(fnc.getStringParameter(request, "DE_SIMB"));
                Historial.initInsertar(request, reg);
                mapMone_Fact.insert(reg);
                ajax.setTodoOk(respJson);
                //}
            }
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    } 
    
    @RequestMapping(value="/ajax/MONE_FACT/update/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> update(HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (fnc.existsParameters(request, new String[]{"CO_MONE_FACT","NO_MONE_FACT","DE_SIMB"},respJson)){
                MONE_FACT reg=mapMone_Fact.getId(fnc.getIntParameter(request, "CO_MONE_FACT"));
                if (reg!=null){
                    reg.setNO_MONE_FACT(fnc.getStringParameter(request, "NO_MONE_FACT"));
                    reg.setDE_SIMB(fnc.getStringParameter(request, "DE_SIMB"));
                    Historial.initModificar(request, reg);
                    mapMone_Fact.update(reg);
                    ajax.setTodoOk(respJson);
                }
            }
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    } 
    
    @RequestMapping(value="/ajax/MONE_FACT/delete/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> delete(HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (fnc.existsParameters(request, new String[]{"CO_MONE_FACT"},respJson)){
                MONE_FACT reg=mapMone_Fact.getId(fnc.getIntParameter(request, "CO_MONE_FACT"));
                if (reg!=null){
                    mapMone_Fact.delete(reg);
                    ajax.setTodoOk(respJson);
                }
            }
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    }     
}
