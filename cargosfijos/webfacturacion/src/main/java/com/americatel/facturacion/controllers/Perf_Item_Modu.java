/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.controllers;

import com.americatel.facturacion.fncs.ajax;
import com.americatel.facturacion.fncs.fnc;
import com.americatel.facturacion.mappers.mapITEM_MODU;
import com.americatel.facturacion.mappers.mapPERF_ITEM_MODU;
import com.americatel.facturacion.models.ITEM_MODU;
import com.americatel.facturacion.models.MODU;
import com.americatel.facturacion.models.PERF_ITEM_MODU;
import java.util.List;
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
public class Perf_Item_Modu {
    @Autowired mapPERF_ITEM_MODU mapPerf_Item_Modu;
    
    @RequestMapping(value="ajax/PERF_ITEM_MODU/select/",method = {RequestMethod.POST},produces = "application/json")
    public @ResponseBody Map<String,Object> select(HttpServletRequest request,@RequestParam(value="NO_ITEM_MODU", required=false) String NO_ITEM_MODU,@RequestParam(value="NO_PERF", required=false) String NO_PERF,@RequestParam(value="CO_PERF", required=false) Integer CO_PERF){
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            System.err.println(CO_PERF);
            if (CO_PERF != null){
                ajax.setData(respJson, mapPerf_Item_Modu.selectByCO_PERF(CO_PERF));
            }else if (NO_ITEM_MODU!=null && NO_PERF!=null ){
                ajax.setData(respJson, mapPerf_Item_Modu.select(NO_PERF,NO_ITEM_MODU));
            }else{
                ajax.setData(respJson, mapPerf_Item_Modu.select("",""));
            }
            ajax.setTodoOk(respJson);
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    }
    
    
    @RequestMapping(value="/ajax/PERF_ITEM_MODU/update/",method = {RequestMethod.POST},produces = "application/json")
    public @ResponseBody Map<String,Object> update(HttpServletRequest request,@RequestParam(value="CO_PERF") int CO_PERF,@RequestParam(value="CO_ITEM_MODU",required = false) String[] listCO_ITEM_MODU){//@RequestBody MODU advancedFormData
        //Cambia los permisos de un perfil a la vez
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            mapPerf_Item_Modu.deleteByCO_PERF(CO_PERF);
            if(listCO_ITEM_MODU!=null){                
                for(int i=0;i<listCO_ITEM_MODU.length;i++){
                    mapPerf_Item_Modu.insert(CO_PERF,Integer.parseInt(listCO_ITEM_MODU[i]));
                }
                ajax.setTodoOk(respJson);                
                                   
            }

            //if (fnc.existsParameters(request, new String[]{"CO_ITEM_MODU","NO_ITEM_MODU","DE_PACK","CO_MODU"},respJson)){
                /*
                ITEM_MODU item_modu=mapPerf_Item_Modu.getId(fnc.getIntParameter(request, "CO_ITEM_MODU"));
                if (item_modu!=null){
                    item_modu.setNO_ITEM_MODU(fnc.getStringParameter(request, "NO_ITEM_MODU"));
                    item_modu.setDE_PACK(fnc.getStringParameter(request, "DE_PACK"));
                    item_modu.setCO_MODU(fnc.getIntParameter(request, "CO_MODU"));
                    mapPerf_Item_Modu.update(item_modu);
                    ajax.setTodoOk(respJson);
                }*/
            //}
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    }    
    
    /*
    @RequestMapping(value="ajax/PERF_ITEM_MODU/insert/",method = {RequestMethod.POST},produces = "application/json")
    public @ResponseBody Map<String,Object> insert(HttpServletRequest request,@ModelAttribute(value="ITEM_MODU") ITEM_MODU item_modu){
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            mapPerf_Item_Modu.insert(item_modu);
            ajax.setTodoOk(respJson);
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    }  
    
     
    

    @RequestMapping(value="/ajax/PERF_ITEM_MODU/getid/",method = {RequestMethod.POST},produces = "application/json")
    public @ResponseBody Map<String,Object> getid(HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (fnc.existsParameters(request, new String[]{"CO_ITEM_MODU"},respJson)){
                int c=fnc.getIntParameter(request,"CO_ITEM_MODU");            
                respJson.put("data",mapPerf_Item_Modu.getId(c));
                ajax.setTodoOk(respJson);
            }
            ajax.setResponseJSON(respJson);
        }        
        return respJson;
    } 

    @RequestMapping(value="/ajax/PERF_ITEM_MODU/delete/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> delete(HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (fnc.existsParameters(request, new String[]{"CO_ITEM_MODU"},respJson)){
                ITEM_MODU item_modu=mapPerf_Item_Modu.getId(fnc.getIntParameter(request, "CO_ITEM_MODU"));
                if (item_modu!=null){
                    mapPerf_Item_Modu.delete(item_modu);
                    ajax.setTodoOk(respJson);
                }
            }
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    }  */  
}
