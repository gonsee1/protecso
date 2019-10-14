/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.controllers;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.americatel.facturacion.fncs.ajax;
import com.americatel.facturacion.fncs.permissions;
import com.americatel.facturacion.mappers.mapCLIE_HISTORICO;
import com.americatel.facturacion.models.CLIE_HISTORICO;
import com.americatel.facturacion.models.USUA;

/**
 *
 * @author rordonez
 */
@Controller
public class Clie_Historico {
    @Autowired mapCLIE_HISTORICO mapClie_Historico;
    
    @RequestMapping(value="/ajax/CLIE_HISTORICO/selectByCO_CLIE/")
    public @ResponseBody Map<String,Object> selectByCO_NEGO(@RequestParam("CO_CLIE") Integer CO_CLIE,HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (CO_CLIE!=null){
            ajax.setData(respJson,mapClie_Historico.getByCoClie(CO_CLIE));
            ajax.setTodoOk(respJson);
        }
        ajax.setResponseJSON(respJson);        
        return respJson;
    }
    
    @RequestMapping(value="/ajax/CLIE_HISTORICO/insert/",method = {RequestMethod.POST})
    public @ResponseBody Map<String,Object> insert(@RequestParam(value="CO_CLIE", required=true) Integer CO_CLIE,@RequestParam(value="DE_INFORMACION", required=true) String DE_INFORMACION,HttpServletRequest request){
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (CO_CLIE!=null && DE_INFORMACION!=null){
                USUA usua=permissions.getUsuarioLogin(request);
                
                CLIE_HISTORICO clie_historico=new CLIE_HISTORICO();
                clie_historico.setCO_CLIE(CO_CLIE);
                clie_historico.setDE_INFORMACION(DE_INFORMACION);
                clie_historico.setCO_USUA_REGI(usua.getCO_USUA_ForJSON());
                clie_historico.setFH_REGI(new Date());
                mapClie_Historico.insert(clie_historico);
            }
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    } 
}
