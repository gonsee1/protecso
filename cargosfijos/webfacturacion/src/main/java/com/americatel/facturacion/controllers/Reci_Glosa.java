/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.controllers;

import com.americatel.facturacion.fncs.ajax;
import com.americatel.facturacion.fncs.fnc;
import com.americatel.facturacion.mappers.mapRECI_GLOS;
import com.americatel.facturacion.models.RECI;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author crodas
 */
@Controller
public class Reci_Glosa {
    @Autowired mapRECI_GLOS mapReci_Glos;

    @RequestMapping(value="/ajax/RECI_GLOS/getByRECI/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> getByRECI(HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (fnc.existsParameters(request, new String[]{"CO_RECI"},respJson)){           
                Long CO_RECI=fnc.getLongParameter(request, "CO_RECI");
                respJson.put("data",mapReci_Glos.getByRECI(CO_RECI));
                ajax.setTodoOk(respJson);
            }
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    } 
    
    @RequestMapping(value="/ajax/RECI_GLOS/getByBOLE/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> getByBOLE(HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (fnc.existsParameters(request, new String[]{"CO_BOLE"},respJson)){           
                Long CO_BOLE=fnc.getLongParameter(request, "CO_BOLE");
                respJson.put("data",mapReci_Glos.getByBOLE(CO_BOLE));
                ajax.setTodoOk(respJson);
            }
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    }
    
}
