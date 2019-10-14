/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.controllers;

import com.americatel.facturacion.fncs.ajax;
import com.americatel.facturacion.fncs.fnc;
import com.americatel.facturacion.historial.Historial;
import com.americatel.facturacion.mappers.mapPERI_FACT;
import com.americatel.facturacion.mappers.mapTIPO_FACT;
import com.americatel.facturacion.models.MODU;
import com.americatel.facturacion.models.PERI_FACT;
import com.americatel.facturacion.models.TIPO_FACT;
import java.util.HashMap;
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
public class Peri_Fact {
    @Autowired mapPERI_FACT mapMone_Fact;

    @RequestMapping(value="/ajax/PERI_FACT/getid/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> getid(HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (fnc.existsParameters(request, new String[]{"CO_PERI_FACT"},respJson)){
                int c=fnc.getIntParameter(request,"CO_PERI_FACT");            
                respJson.put("data",mapMone_Fact.getId(c));
                ajax.setTodoOk(respJson);
            }
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    }     
    
    @RequestMapping(value="/ajax/PERI_FACT/select/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> select(@RequestParam(value="id",required = false) String id,HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (id==null){
                String nombre="";
                if (fnc.existsParameter(request, "NO_PERI_FACT"))
                    nombre=fnc.getStringParameter(request, "NO_PERI_FACT");
                respJson.put("data", mapMone_Fact.select(nombre));
            }else{
                respJson.put("data",mapMone_Fact.getId(Integer.parseInt(id)));            
            }        
            ajax.setTodoOk(respJson);
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    }
    @RequestMapping(value="/ajax/PERI_FACT/insert/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> insert(HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (fnc.existsParameters(request, new String[]{"NO_PERI_FACT"},respJson)){
                PERI_FACT reg=new PERI_FACT();
                reg.setNO_PERI_FACT(fnc.getStringParameter(request, "NO_PERI_FACT"));
                Historial.initInsertar(request, reg);
                mapMone_Fact.insert(reg);
                ajax.setTodoOk(respJson);
                //}
            }
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    } 
    
    @RequestMapping(value="/ajax/PERI_FACT/update/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> update(HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (fnc.existsParameters(request, new String[]{"CO_PERI_FACT","NO_PERI_FACT"},respJson)){
                PERI_FACT reg=mapMone_Fact.getId(fnc.getIntParameter(request, "CO_PERI_FACT"));
                if (reg!=null){
                    reg.setNO_PERI_FACT(fnc.getStringParameter(request, "NO_PERI_FACT"));
                    //Historico.modificar(reg);//new Object[]{ "CO_PERI_FACT",reg.getCO_PERI_FACT_ForJSON() }
                    Historial.initModificar(request, reg);
                    mapMone_Fact.update(reg);
                    ajax.setTodoOk(respJson);
                }
            }
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    } 
    
    @RequestMapping(value="/ajax/PERI_FACT/delete/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> delete(HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (fnc.existsParameters(request, new String[]{"CO_PERI_FACT"},respJson)){
                PERI_FACT reg=mapMone_Fact.getId(fnc.getIntParameter(request, "CO_PERI_FACT"));
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
