/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.controllers;

import com.americatel.facturacion.fncs.ajax;
import com.americatel.facturacion.fncs.permissions;
import com.americatel.facturacion.mappers.mapNEGO_COME;
import com.americatel.facturacion.mappers.mapNEGO_HISTORICO;
import com.americatel.facturacion.models.NEGO_COME;
import com.americatel.facturacion.models.NEGO_HISTORICO;
import com.americatel.facturacion.models.USUA;
import java.util.Date;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author rordonez
 */
@Controller
public class Nego_Come {
    @Autowired mapNEGO_COME mapNego_Come;
    @Autowired mapNEGO_HISTORICO mapNego_Historico;
    
    @RequestMapping(value="/ajax/NEGO_COME/insert/")
    public @ResponseBody Map<String,Object> insert(@ModelAttribute("NEGO_COME") NEGO_COME nego_come,HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (nego_come!=null){
                USUA usua=permissions.getUsuarioLogin(request);
                
                //nego_come.setDE_COME(nego_come.getDE_COME_ForJSON());
                nego_come.setCO_USUA(usua.getCO_USUA_ForJSON());
                nego_come.setFH_CREO(new Date());
                mapNego_Come.insert(nego_come);
                
                //Registrar en el historico del negocio
                NEGO_HISTORICO nego_historico=new NEGO_HISTORICO();
                nego_historico.setCO_NEGO(nego_come.getCO_NEGO_ForJSON());
                nego_historico.setDE_INFORMACION("Se ha agregado un comentario");
                nego_historico.setCO_USUA_REGI(usua.getCO_USUA_ForJSON());
                nego_historico.setFH_REGI(new Date());
                mapNego_Historico.insert(nego_historico);
                
                ajax.setTodoOk(respJson);
            }
        }
        ajax.setResponseJSON(respJson);        
        return respJson;
    }
    
    @RequestMapping(value="/ajax/NEGO_COME/selectByCO_NEGO/")
    public @ResponseBody Map<String,Object> selectByCO_NEGO(@RequestParam("CO_NEGO") Integer CO_NEGO,HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (CO_NEGO!=null){
            ajax.setData(respJson,mapNego_Come.selectByCO_NEGO(CO_NEGO));
            ajax.setTodoOk(respJson);
        }else{
            ajax.setData(respJson, "No existe fecha desactivaci\u00f3n");
        }
        ajax.setResponseJSON(respJson);        
        return respJson;
    } 
}
