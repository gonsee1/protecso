/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.controllers;

import com.americatel.facturacion.fncs.ajax;
import com.americatel.facturacion.mappers.mapPROD;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author efitec01
 */
@Controller
public class Prod_Padre {
    
    
    @Autowired mapPROD mapProd;

    @RequestMapping(value="/ajax/PROD_PADRE/select/",method = {RequestMethod.POST,RequestMethod.GET})//,produces = "application/json"
    public @ResponseBody Map<String,Object> select(@RequestParam(value="id",required = false) String id,HttpServletRequest request){//@RequestBody MODU advancedFormData
                        
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (id==null){                        
                respJson.put("data", mapProd.select_producto_padre(""));
            }        
            ajax.setTodoOk(respJson);
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    }
    
    
    @RequestMapping(value="/ajax/PROD_PADRE/getFechaByProd/",method = {RequestMethod.POST,RequestMethod.GET})//,produces = "application/json"
    public @ResponseBody Map<String,Object> getFechaByProd(@RequestParam(value="CO_PROD", required=true) Integer CO_PROD,HttpServletRequest request){//@RequestBody MODU advancedFormData
                        
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (CO_PROD!=null){                        
                respJson.put("data", mapProd.getFechaByProd(CO_PROD));
            }        
            ajax.setTodoOk(respJson);
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    }
    
    
    
}
