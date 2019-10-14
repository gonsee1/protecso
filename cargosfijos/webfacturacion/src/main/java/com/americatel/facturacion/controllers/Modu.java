/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.controllers;

import com.americatel.facturacion.fncs.ajax;
import com.americatel.facturacion.fncs.fnc;
import com.americatel.facturacion.mappers.mapMODU;
import com.americatel.facturacion.models.MODU;
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
public class Modu {
    @Autowired mapMODU mapModu;
    
    
    @RequestMapping(value="/ajax/MODU/getall.json",method = {RequestMethod.GET,RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> getall(HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            respJson.put("data", mapModu.getAll());
            ajax.setTodoOk(respJson);
        }
        return respJson;
    }
    
    @RequestMapping(value="/ajax/MODU/{id}",method = {RequestMethod.POST},produces = {"application/json"})
    public @ResponseBody Map<String,Object> getOne(HttpServletRequest request,@PathVariable("id") int id){
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            respJson.put("data", mapModu.getId(id));
            ajax.setTodoOk(respJson);
        }
        return respJson;
    }
    
    @RequestMapping(value="/ajax/MODU/getid/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> getid(HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (fnc.existsParameters(request, new String[]{"CO_MODU"},respJson)){
                int c=fnc.getIntParameter(request,"CO_MODU");            
                respJson.put("data",mapModu.getId(c));
                ajax.setTodoOk(respJson);
            }
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    }     
    
    @RequestMapping(value="/ajax/MODU/select/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> select(@RequestParam(value="id",required = false) String id,HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (id==null){
                String nombre="";
                if (fnc.existsParameter(request, "NO_MODU"))
                    nombre=fnc.getStringParameter(request, "NO_MODU");
                respJson.put("data", mapModu.select(nombre));
            }else{
                respJson.put("data",mapModu.getId(Integer.parseInt(id)));            
            }        
            ajax.setTodoOk(respJson);
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    }
    @RequestMapping(value="/ajax/MODU/insert/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> insert(@ModelAttribute("MODU") MODU reg,HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (fnc.existsParameters(request, new String[]{"NO_MODU","DE_PACK"},respJson)){
                //MODU modu=mapModu.getId(fnc.getIntParameter(request, "CO_MODU"));
                //if (modu==null){
                /*MODU modu=new MODU();
                modu.setNO_MODU(fnc.getStringParameter(request, "NO_MODU"));
                modu.setDE_PACK(fnc.getStringParameter(request, "DE_PACK"));
                if (fnc.existsParameter(request, "DE_ICON_CLSS"))
                    modu.setDE_ICON_CLSS(fnc.getStringParameter(request, "DE_ICON_CLSS"));*/
                if (reg.getDE_ICON_CLSS_ForJSON()!=null && reg.getDE_ICON_CLSS_ForJSON().length()==0)
                    reg.setDE_ICON_CLSS(null);
                mapModu.insert(reg);
                ajax.setTodoOk(respJson);
                //}
            }
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    } 
    
    @RequestMapping(value="/ajax/MODU/update/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> update(@ModelAttribute("MODU") MODU reg,HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (fnc.existsParameters(request, new String[]{"CO_MODU","NO_MODU","DE_PACK"},respJson)){
                /*MODU modu=mapModu.getId(fnc.getIntParameter(request, "CO_MODU"));
                if (modu!=null){
                    modu.setNO_MODU(fnc.getStringParameter(request, "NO_MODU"));
                    modu.setDE_PACK(fnc.getStringParameter(request, "DE_PACK"));
                    if (fnc.existsParameter(request, "DE_ICON_CLSS"))
                        modu.setDE_ICON_CLSS(fnc.getStringParameter(request, "DE_ICON_CLSS"));                    
                    mapModu.update(modu);
                    ajax.setTodoOk(respJson);
                }*/
                if (reg.getDE_ICON_CLSS_ForJSON()!=null && reg.getDE_ICON_CLSS_ForJSON().length()==0)
                    reg.setDE_ICON_CLSS(null);                
                mapModu.update(reg);
                ajax.setTodoOk(respJson);                
            }
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    } 
    
    @RequestMapping(value="/ajax/MODU/delete/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> delete(HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (fnc.existsParameters(request, new String[]{"CO_MODU"},respJson)){
                MODU modu=mapModu.getId(fnc.getIntParameter(request, "CO_MODU"));
                if (modu!=null){
                    mapModu.delete(modu);
                    ajax.setTodoOk(respJson);
                }
            }
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    }   
    
}
