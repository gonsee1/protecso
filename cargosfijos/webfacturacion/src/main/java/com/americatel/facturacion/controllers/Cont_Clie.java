/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.controllers;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.americatel.facturacion.fncs.ajax;
import com.americatel.facturacion.historial.Historial;
import com.americatel.facturacion.mappers.mapCONT_CLIE;
import com.americatel.facturacion.models.CONT_CLIE;




/**
 *
 * @author crodas
 */
@Controller
public class Cont_Clie {
    @Autowired mapCONT_CLIE mapCont_Clie;
    
    @RequestMapping(value="/ajax/CONT_CLIE/selectByCLIE/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> selectByCLIE(@RequestParam(value="CO_CLIE") int CO_CLIE,HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            respJson.put("data",mapCont_Clie.selectByCLIE(CO_CLIE));   
            ajax.setTodoOk(respJson);
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    } 
    
    @RequestMapping(value="/ajax/CONT_CLIE/insert/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> insert(@ModelAttribute("CLIE") CONT_CLIE reg,HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            Historial.initInsertar(request, reg);
            mapCont_Clie.insert(reg);
            ajax.setTodoOk(respJson);
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    } 
    
    @RequestMapping(value="/ajax/CONT_CLIE/selectById/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> selectById(@RequestParam("CO_CONT_CLIE") Integer CO_CONT_CLIE,HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (CO_CONT_CLIE!=null){
                ajax.setData(respJson, mapCont_Clie.getById(CO_CONT_CLIE));
                ajax.setTodoOk(respJson);                                
            }
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    }  
    
    @RequestMapping(value="/ajax/CONT_CLIE/update/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> update(@ModelAttribute("CLIE") CONT_CLIE reg,HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (reg.getCO_CONT_CLIE_ForJSON()!=null){
                CONT_CLIE db=mapCont_Clie.getById(reg.getCO_CONT_CLIE_ForJSON());
                if (db.getCO_CLIE_ForJSON().equals(reg.getCO_CLIE_ForJSON())){
                    db.setAP_CONT_CLIE(reg.getAP_CONT_CLIE_ForJSON());
                    db.setCO_TIPO_DOCU(reg.getCO_TIPO_DOCU_ForJSON());
                    db.setDE_CORR(reg.getDE_CORR_ForJSON());
                    db.setDE_NUME_DOCU(reg.getDE_NUME_DOCU_ForJSON());
                    db.setDE_TELE(reg.getDE_TELE_ForJSON());
                    db.setNO_CONT_CLIE(reg.getNO_CONT_CLIE_ForJSON());
                    Historial.initModificar(request, db);
                    mapCont_Clie.update(db);
                    ajax.setTodoOk(respJson);
                }                
            }
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    }     
    
}
