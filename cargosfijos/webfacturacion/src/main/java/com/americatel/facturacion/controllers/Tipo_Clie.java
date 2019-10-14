/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.controllers;

import com.americatel.facturacion.fncs.ajax;
import com.americatel.facturacion.mappers.mapTIPO_CLIE;
import com.americatel.facturacion.models.TIPO_CLIE;
import java.util.List;
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
public class Tipo_Clie {
    @Autowired mapTIPO_CLIE mapTipo_Clie;            
   
       
    @RequestMapping(value="/ajax/TIPO_CLIE/select/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> select(HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){            
            ajax.setData(respJson, mapTipo_Clie.getAll());
            ajax.setTodoOk(respJson);
        }
        ajax.setResponseJSON(respJson);
        return respJson;
    }    
}
