/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.controllers;

import com.americatel.facturacion.fncs.ajax;
import com.americatel.facturacion.fncs.permissions;
import com.americatel.facturacion.historial.Historial;
import com.americatel.facturacion.mappers.mapNEGO_PROM;
import com.americatel.facturacion.mappers.mapNEGO_SUCU;
import com.americatel.facturacion.mappers.mapNEGO_SUCU_HISTORICO;
import com.americatel.facturacion.mappers.mapNEGO_SUCU_PROM;
import com.americatel.facturacion.models.NEGO_PROM;
import com.americatel.facturacion.models.NEGO_SUCU;
import com.americatel.facturacion.models.NEGO_SUCU_HISTORICO;
import com.americatel.facturacion.models.NEGO_SUCU_PROM;
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
 * @author crodas
 */
@Controller
public class Nego_Sucu_Prom {
    @Autowired mapNEGO_SUCU_PROM mapNego_Sucu_Prom;
    @Autowired  mapNEGO_SUCU_HISTORICO mapNego_Sucu_Historico;
    @Autowired mapNEGO_SUCU mapNego_Sucu;
    
    @RequestMapping(value="/ajax/NEGO_SUCU_PROM/insert/")
    public @ResponseBody Map<String,Object> insert(@ModelAttribute("NEGO_SUCU_PROM") NEGO_SUCU_PROM nego_sucu_prom,HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (nego_sucu_prom != null){
                Historial.initInsertar(request, nego_sucu_prom);
                mapNego_Sucu_Prom.insert(nego_sucu_prom);
                
                //Registrar en el historico del negocio
                NEGO_SUCU nego=mapNego_Sucu.getId(nego_sucu_prom.getCO_NEGO_SUCU_ForJSON());
                USUA usua=permissions.getUsuarioLogin(request);
                NEGO_SUCU_HISTORICO nego_sucu_historico=new NEGO_SUCU_HISTORICO();
                nego_sucu_historico.setCO_NEGO(nego.getCO_NEGO_ForJSON());
                nego_sucu_historico.setCO_NEGO_SUCU(nego_sucu_prom.getCO_NEGO_SUCU_ForJSON());
                nego_sucu_historico.setDE_INFORMACION("Se agreg\u00f3 promoci\u00f3n");
                nego_sucu_historico.setCO_USUA_REGI(usua.getCO_USUA_ForJSON());
                nego_sucu_historico.setFH_REGI(new Date());
                mapNego_Sucu_Historico.insert(nego_sucu_historico);
                
                ajax.setTodoOk(respJson);
            }
        }
        ajax.setResponseJSON(respJson);        
        return respJson;
    }
    
    @RequestMapping(value="/ajax/NEGO_SUCU_PROM/selectByCO_NEGO_SUCU/")
    public @ResponseBody Map<String,Object> selectByCO_NEGO(@RequestParam("CO_NEGO_SUCU") Integer CO_NEGO_SUCU,HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (CO_NEGO_SUCU!=null){
            ajax.setData(respJson,mapNego_Sucu_Prom.selectByCO_NEGO_SUCU(CO_NEGO_SUCU));
            ajax.setTodoOk(respJson);
        }else{
            ajax.setData(respJson, "No existe fecha desactivaci\u00f3n");
        }
        ajax.setResponseJSON(respJson);        
        return respJson;
    }    
}
