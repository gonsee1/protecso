/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.controllers;

import com.americatel.facturacion.fncs.ajax;
import com.americatel.facturacion.fncs.fnc;
import com.americatel.facturacion.mappers.mapFACT_GLOS;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author crodas
 */
@Controller
public class Fact_Glosa {
    @Autowired mapFACT_GLOS mapFact_Glos;

    @RequestMapping(value="/ajax/FACT_GLOS/getByFACT/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> getByFACT(HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (fnc.existsParameters(request, new String[]{"CO_FACT"},respJson)){           
                Long CO_FACT=fnc.getLongParameter(request, "CO_FACT");
                respJson.put("data",mapFact_Glos.getByFACT(CO_FACT));
                ajax.setTodoOk(respJson);
            }
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    }     
    
}
