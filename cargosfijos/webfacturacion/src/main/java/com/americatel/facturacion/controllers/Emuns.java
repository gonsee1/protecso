/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.controllers;

import com.americatel.facturacion.cores.ETipoReciboGlosa;
import com.americatel.facturacion.fncs.ajax;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author crodas
 */
@Controller
public class Emuns {
    @RequestMapping(value="/ajax/ENUMS/getETipoReciboGlosa/",method = {RequestMethod.POST,RequestMethod.GET})//,produces = "application/json"
    public @ResponseBody Map<String,Object> getETipoReciboGlosa(HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            List<Map<String,Object>> arr=new ArrayList<Map<String, Object>>();
            for(ETipoReciboGlosa e : ETipoReciboGlosa.values()){                   
                Map<String,Object> reg=new HashMap<String, Object>();
                reg.put("name", e);
                reg.put("value", e.toString());
                arr.add(reg);
            }
            ajax.setData(respJson,arr); 
            ajax.setTodoOk(respJson);
        }
        return respJson;
    }
    
}
