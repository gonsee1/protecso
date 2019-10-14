/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.controllers;

import com.americatel.facturacion.fncs.ajax;
import com.americatel.facturacion.fncs.fnc;
import com.americatel.facturacion.mappers.mapITEM_MODU;
import com.americatel.facturacion.models.ITEM_MODU;
import com.americatel.facturacion.models.MODU;
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
public class Item_Modu {
    @Autowired mapITEM_MODU mapItem_Modu;
    
    @RequestMapping(value="ajax/ITEM_MODU/select/",method = {RequestMethod.POST},produces = "application/json")
    public @ResponseBody Map<String,Object> select(HttpServletRequest request,@RequestParam(value="NO_ITEM_MODU", required=false) String NO_ITEM_MODU){
        Map<String,Object> respJson=ajax.getResponseJSON();        
        if (ajax.hasPermission(request, respJson)){
            if (NO_ITEM_MODU==null)
                ajax.setData(respJson, mapItem_Modu.select(""));
            else
                ajax.setData(respJson, mapItem_Modu.select(NO_ITEM_MODU));
            ajax.setTodoOk(respJson);
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    }
    
    @RequestMapping(value="ajax/ITEM_MODU/insert/",method = {RequestMethod.POST},produces = "application/json")
    public @ResponseBody Map<String,Object> insert(HttpServletRequest request,@ModelAttribute(value="ITEM_MODU") ITEM_MODU item_modu){
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            mapItem_Modu.insert(item_modu);
            ajax.setTodoOk(respJson);
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    }  
    
    @RequestMapping(value="/ajax/ITEM_MODU/update/",method = {RequestMethod.POST},produces = "application/json")
    public @ResponseBody Map<String,Object> update(HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (fnc.existsParameters(request, new String[]{"CO_ITEM_MODU","NO_ITEM_MODU","DE_PACK","CO_MODU"},respJson)){
                ITEM_MODU item_modu=mapItem_Modu.getId(fnc.getIntParameter(request, "CO_ITEM_MODU"));
                if (item_modu!=null){
                    item_modu.setNO_ITEM_MODU(fnc.getStringParameter(request, "NO_ITEM_MODU"));
                    item_modu.setDE_PACK(fnc.getStringParameter(request, "DE_PACK"));
                    item_modu.setCO_MODU(fnc.getIntParameter(request, "CO_MODU"));
                    mapItem_Modu.update(item_modu);
                    ajax.setTodoOk(respJson);
                }
            }
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    }     
    

    @RequestMapping(value="/ajax/ITEM_MODU/getid/",method = {RequestMethod.POST},produces = "application/json")
    public @ResponseBody Map<String,Object> getid(HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (fnc.existsParameters(request, new String[]{"CO_ITEM_MODU"},respJson)){
                int c=fnc.getIntParameter(request,"CO_ITEM_MODU");            
                respJson.put("data",mapItem_Modu.getId(c));
                ajax.setTodoOk(respJson);
            }
            ajax.setResponseJSON(respJson);
        }        
        return respJson;
    } 

    @RequestMapping(value="/ajax/ITEM_MODU/delete/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> delete(HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (fnc.existsParameters(request, new String[]{"CO_ITEM_MODU"},respJson)){
                ITEM_MODU item_modu=mapItem_Modu.getId(fnc.getIntParameter(request, "CO_ITEM_MODU"));
                if (item_modu!=null){
                    mapItem_Modu.delete(item_modu);
                    ajax.setTodoOk(respJson);
                }
            }
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    }    
}
