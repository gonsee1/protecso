/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.controllers;

import com.americatel.facturacion.fncs.ajax;
import com.americatel.facturacion.fncs.fnc;
import com.americatel.facturacion.fncs.permissions;
import com.americatel.facturacion.historial.Historial;
import com.americatel.facturacion.mappers.mapNEGO_HISTORICO;
import com.americatel.facturacion.models.DEPA;
import com.americatel.facturacion.models.NEGO_HISTORICO;
import com.americatel.facturacion.models.USUA;
import java.util.Date;
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
 * @author rordonez
 */
@Controller
public class Nego_Historico {
    @Autowired mapNEGO_HISTORICO mapNego_Historico;
    
    @RequestMapping(value="/ajax/NEGO_HISTORICO/selectByCO_NEGO/")
    public @ResponseBody Map<String,Object> selectByCO_NEGO(@RequestParam("CO_NEGO") Integer CO_NEGO,HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (CO_NEGO!=null){
            ajax.setData(respJson,mapNego_Historico.getByNego(CO_NEGO));
            ajax.setTodoOk(respJson);
        }
        ajax.setResponseJSON(respJson);        
        return respJson;
    }
    
    @RequestMapping(value="/ajax/NEGO_HISTORICO/insert/",method = {RequestMethod.POST})
    public @ResponseBody Map<String,Object> insert(@RequestParam(value="CO_NEGO", required=true) Integer CO_NEGO,@RequestParam(value="DE_INFORMACION", required=true) String DE_INFORMACION,HttpServletRequest request){
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (CO_NEGO!=null && DE_INFORMACION!=null){
                USUA usua=permissions.getUsuarioLogin(request);
                
                NEGO_HISTORICO nego_historico=new NEGO_HISTORICO();
                nego_historico.setCO_NEGO(CO_NEGO);
                nego_historico.setDE_INFORMACION(DE_INFORMACION);
                nego_historico.setCO_USUA_REGI(usua.getCO_USUA_ForJSON());
                nego_historico.setFH_REGI(new Date());
                mapNego_Historico.insert(nego_historico);
            }
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    } 
}
