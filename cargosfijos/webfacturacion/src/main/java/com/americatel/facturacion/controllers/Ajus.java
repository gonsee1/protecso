/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.americatel.facturacion.fncs.ExcelReader;
import com.americatel.facturacion.fncs.ajax;
import com.americatel.facturacion.historial.Historial;
import com.americatel.facturacion.mappers.mapAJUS;
import com.americatel.facturacion.mappers.mapNEGO;
import com.americatel.facturacion.models.AJUS;
import com.americatel.facturacion.models.NEGO;

/**
 *
 * @author crodas
 */
@Controller
public class Ajus {
    @Autowired mapAJUS mapAjus;
    @Autowired mapNEGO mapNego;
    
    @RequestMapping(value="/ajax/AJUS/upload/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> upload(@RequestParam("FL_AJUS") MultipartFile file,HttpServletRequest request,@RequestParam(value="COD_PROD_PADRE", required=true) Integer COD_PROD_PADRE){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if(!file.isEmpty()){
            ExcelReader excel=new ExcelReader(file);
            if (excel.selectSheet(0)){
                mapAjus.borrarPendientes();//se borrar para cargarlo nuevamente
                int iniRow=2;
                int numRows=excel.getNumRows();
                Integer CO_NEGO;
                String afecto_glosa=null;
                Double afecto_monto=null;
                String no_afecto_glosa=null;
                Double no_afecto_monto=null;
                List<String> mensajes=new ArrayList<String>();
                Map<Integer,NEGO> negocios=new TreeMap<Integer,NEGO>();
                NEGO nego=null;
                Integer ajustes_cargados=0;
                for(;iniRow<=numRows+1;iniRow++){
                    CO_NEGO=excel.getValueInteger("A", iniRow);                    
                    afecto_glosa=excel.getValue("B", iniRow);
                    afecto_monto=excel.getValueDouble("C", iniRow);
                    no_afecto_glosa=excel.getValue("D", iniRow);
                    no_afecto_monto=excel.getValueDouble("E", iniRow);
                    if (CO_NEGO!=null && CO_NEGO>0){
                        nego=null;
                        if (negocios.containsKey(CO_NEGO))
                            nego=negocios.get(CO_NEGO);
                        else{
                            nego=mapNego.getId(CO_NEGO);
                            if (nego!=null)
                                negocios.put(CO_NEGO, nego);
                            else{
                                mensajes.add("Fila "+iniRow+" no tiene un negocio valido.");
                            }
                        }
                        if (nego!=null){
                            
                            if(nego.getProd_padre_ForJSON().getCO_PROD_PADRE_ForJSON() == COD_PROD_PADRE){                                
                                                                                                                                             
                                if ((afecto_glosa!=null && afecto_monto!=null) || (no_afecto_glosa!=null && no_afecto_monto!=null)){
                                   AJUS ajus=new AJUS();
                                   ajus.setCO_NEGO(CO_NEGO);
                                   if (afecto_glosa!=null && afecto_monto!=null){                                    
                                       ajus.setDE_GLOS(afecto_glosa);
                                       ajus.setIM_MONT(Math.abs(afecto_monto));
                                       ajus.setST_AFEC_IGV(Boolean.TRUE);
                                   }else{
                                       ajus.setDE_GLOS(no_afecto_glosa);
                                       ajus.setIM_MONT(Math.abs(no_afecto_monto));
                                       ajus.setST_AFEC_IGV(Boolean.FALSE);                                    
                                   }
                                   ajus.setST_PEND(Boolean.TRUE);
                                   Historial.initInsertar(request, ajus);
                                   mapAjus.insert(ajus);
                                   ajustes_cargados++;
                               
                                }else{
                                   mensajes.add("Fila "+iniRow+" faltan parametros.");
                                }
                                
                            }else{
                                mensajes.add("Fila "+iniRow+" el producto del negocio no coincide con la opci\u00f3n seleccionada.");  
                            }    
                        }
                    }else{
                        mensajes.add("Fila "+iniRow+" no tiene un negocio valido.");
                    }
                }
                ajax.setTodoOk(respJson);
                mensajes.add(0, "Se cargaron "+ajustes_cargados+" ajustes.");
                ajax.setData(respJson, mensajes);                
            }
        }
        return respJson;
    }
    
    @RequestMapping(value="/ajax/AJUS/selectPendientes/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> selectPendientes(HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            ajax.setData(respJson, mapAjus.selectPendientes());
            ajax.setTodoOk(respJson);
        }
        ajax.setResponseJSON(respJson);
        return respJson;
    }
    
   
}
