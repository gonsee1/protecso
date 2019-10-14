/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.historial;

import com.americatel.facturacion.fncs.ajax;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author crodas
 */
@Controller
public class Controlador {
    @RequestMapping(value="/ajax/HISTORIAL/getHistorial/",method = {RequestMethod.POST,RequestMethod.GET})//,produces = "application/json"
    public @ResponseBody Map<String,Object> getHistorial(@RequestParam(value="idEntity",required = false) Integer idEntity,@RequestParam(value="type",required = false) String type,HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            ReadHistorial rh=new ReadHistorial(type,idEntity);
            ajax.setData(respJson,rh.getData());
            ajax.setTodoOk(respJson);
        }
        return respJson;
    }    
}
