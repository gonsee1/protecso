/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.controllers;

import com.americatel.facturacion.cores.Main;
import com.americatel.facturacion.fncs.ExcelReader;
import com.americatel.facturacion.fncs.ajax;
import com.americatel.facturacion.fncs.fnc;
import com.americatel.facturacion.historial.Historial;
import com.americatel.facturacion.mappers.mapAJUS;
import com.americatel.facturacion.mappers.mapCIER;
import com.americatel.facturacion.mappers.mapFACT;
import com.americatel.facturacion.mappers.mapNEGO;
import com.americatel.facturacion.mappers.mapNEGO_CORR;
import com.americatel.facturacion.mappers.mapPROD;
import com.americatel.facturacion.mappers.mapRECI;
import com.americatel.facturacion.models.AJUS;
import com.americatel.facturacion.models.CIER;
import com.americatel.facturacion.models.FACT;
import com.americatel.facturacion.models.NEGO;
import com.americatel.facturacion.models.PROD;
import com.americatel.facturacion.models.RECI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author crodas
 */
@Controller
public class Nego_Corr {
    @Autowired mapNEGO_CORR mapNego_Corr;
    
    @RequestMapping(value="/ajax/NEGO_CORR/selectActivosByCO_NEGO/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> selectActivosByCO_NEGO(@RequestParam(value="CO_NEGO",required = true) Integer CO_NEGO ,HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (CO_NEGO!=null){
                ajax.setData(respJson, mapNego_Corr.selectActivosByCO_NEGO(CO_NEGO));
                ajax.setTodoOk(respJson);
            }
        }
        ajax.setResponseJSON(respJson);
        return respJson;
    }
    
   
}
