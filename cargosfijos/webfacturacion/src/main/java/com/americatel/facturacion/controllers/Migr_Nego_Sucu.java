/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.controllers;

import com.americatel.facturacion.fncs.ajax;
import com.americatel.facturacion.mappers.mapMIGR_NEGO_SUCU;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author rordonez
 */
@Controller
public class Migr_Nego_Sucu {
    @Autowired mapMIGR_NEGO_SUCU mapMigr_Nego_Sucu;
    
    @RequestMapping(value="/ajax/MIGR_NEGO_SUCU/selectByCO_NEGO/")
    public @ResponseBody Map<String,Object> selectByCO_NEGO(@RequestParam("CO_NEGO") Integer CO_NEGO,HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (CO_NEGO!=null){
            ajax.setData(respJson,mapMigr_Nego_Sucu.getByNego(CO_NEGO));
            ajax.setTodoOk(respJson);
        }else{
            ajax.setData(respJson, "No existe fecha desactivaci\u00f3n");
        }
        ajax.setResponseJSON(respJson);        
        return respJson;
    }
}
