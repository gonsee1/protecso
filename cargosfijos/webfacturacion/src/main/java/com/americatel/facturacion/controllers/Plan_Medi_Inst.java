/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.controllers;

import com.americatel.facturacion.fncs.ajax;
import com.americatel.facturacion.fncs.fnc;
import com.americatel.facturacion.mappers.mapPLAN_MEDI_INST;
import com.americatel.facturacion.mappers.mapPLAN_MEDI_INST;
import com.americatel.facturacion.models.MODU;
import com.americatel.facturacion.models.PLAN_MEDI_INST;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
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
public class Plan_Medi_Inst {
    @Autowired mapPLAN_MEDI_INST mapPlan_Medi_Inst;
      
    @RequestMapping(value="/ajax/PLAN_MEDI_INST/getid/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> getid(HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (fnc.existsParameters(request, new String[]{"CO_PLAN_MEDI_INST"},respJson)){
                int c=fnc.getIntParameter(request,"CO_PLAN_MEDI_INST");            
                respJson.put("data",mapPlan_Medi_Inst.getId(c));
                ajax.setTodoOk(respJson);
            }
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    }     
    
    @RequestMapping(value="/ajax/PLAN_MEDI_INST/select/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> select(@RequestParam(value="id",required = false) String id,HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (id==null){
                String nombre="";
                if (fnc.existsParameter(request, "NO_PLAN_MEDI_INST"))
                    nombre=fnc.getStringParameter(request, "NO_PLAN_MEDI_INST");
                respJson.put("data", mapPlan_Medi_Inst.select(nombre));
            }else{
                respJson.put("data",mapPlan_Medi_Inst.getId(Integer.parseInt(id)));            
            }        
            ajax.setTodoOk(respJson);
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    }
    

    @RequestMapping(value="/ajax/PLAN_MEDI_INST/selectByProd/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> selectByProd(@ModelAttribute("CO_PROD") String prod,HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
                            
                if (prod.length() > 0){                       
                List<Map<String, Object>> lista=mapPlan_Medi_Inst.selectByProd(prod);

                    ajax.setData(respJson, lista);
                    ajax.setTodoOk(respJson);
                }                        
                    ajax.setResponseJSON(respJson);
        }
        return respJson;
    }  

    
    /*
    @RequestMapping(value="/ajax/PLAN_MEDI_INST/insert/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> insert(HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (fnc.existsParameters(request, new String[]{"NO_PLAN_MEDI_INST"},respJson)){
                PLAN_MEDI_INST modu=new PLAN_MEDI_INST();
                modu.setNO_PLAN_MEDI_INST(fnc.getStringParameter(request, "NO_PLAN_MEDI_INST"));
                mapPlan_Medi_Inst.insert(modu);
                ajax.setTodoOk(respJson);
            }
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    } 
    
    @RequestMapping(value="/ajax/PLAN_MEDI_INST/update/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> update(HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (fnc.existsParameters(request, new String[]{"CO_PLAN_MEDI_INST","NO_PLAN_MEDI_INST"},respJson)){
                PLAN_MEDI_INST reg=mapPlan_Medi_Inst.getId(fnc.getIntParameter(request, "CO_PLAN_MEDI_INST"));
                if (reg!=null){
                    reg.setNO_PLAN_MEDI_INST(fnc.getStringParameter(request, "NO_PLAN_MEDI_INST"));
                    mapPlan_Medi_Inst.update(reg);
                    ajax.setTodoOk(respJson);
                }
            }
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    } 
    
    @RequestMapping(value="/ajax/PLAN_MEDI_INST/delete/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> delete(HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (fnc.existsParameters(request, new String[]{"CO_PLAN_MEDI_INST"},respJson)){
                PLAN_MEDI_INST reg=mapPlan_Medi_Inst.getId(fnc.getIntParameter(request, "CO_PLAN_MEDI_INST"));
                if (reg!=null){
                    mapPlan_Medi_Inst.delete(reg);
                    ajax.setTodoOk(respJson);
                }
            }
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    } 
    */
}
