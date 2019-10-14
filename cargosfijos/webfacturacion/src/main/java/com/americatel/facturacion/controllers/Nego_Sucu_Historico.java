/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.controllers;

import com.americatel.facturacion.fncs.ajax;
import com.americatel.facturacion.fncs.permissions;
import com.americatel.facturacion.mappers.mapNEGO_SUCU_HISTORICO;
import com.americatel.facturacion.models.NEGO_SUCU_HISTORICO;
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
public class Nego_Sucu_Historico {
    @Autowired mapNEGO_SUCU_HISTORICO mapNego_Sucu_Historico;
    
    @RequestMapping(value="/ajax/NEGO_SUCU_HISTORICO/selectByCO_NEGO_SUCU/")
    public @ResponseBody Map<String,Object> selectByCO_NEGO_SUCU(@RequestParam("CO_NEGO_SUCU") Integer CO_NEGO_SUCU,HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (CO_NEGO_SUCU!=null){
            ajax.setData(respJson,mapNego_Sucu_Historico.getByNegoSucu(CO_NEGO_SUCU));
            ajax.setTodoOk(respJson);
        }
        ajax.setResponseJSON(respJson);        
        return respJson;
    }
    
    @RequestMapping(value="/ajax/NEGO_SUCU_HISTORICO/insert/",method = {RequestMethod.POST})
    public @ResponseBody Map<String,Object> insert(@RequestParam(value="CO_NEGO", required=true) Integer CO_NEGO,@RequestParam(value="CO_NEGO_SUCU", required=true) Integer CO_NEGO_SUCU,@RequestParam(value="DE_INFORMACION", required=true) String DE_INFORMACION,HttpServletRequest request){
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (CO_NEGO!=null && CO_NEGO_SUCU!=null && DE_INFORMACION!=null){
                USUA usua=permissions.getUsuarioLogin(request);
                
                NEGO_SUCU_HISTORICO nego_sucu_historico=new NEGO_SUCU_HISTORICO();
                nego_sucu_historico.setCO_NEGO(CO_NEGO);
                nego_sucu_historico.setCO_NEGO_SUCU(CO_NEGO_SUCU);
                nego_sucu_historico.setDE_INFORMACION(DE_INFORMACION);
                nego_sucu_historico.setCO_USUA_REGI(usua.getCO_USUA_ForJSON());
                nego_sucu_historico.setFH_REGI(new Date());
                mapNego_Sucu_Historico.insert(nego_sucu_historico);
            }
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    } 
}
