/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.controllers;

import com.americatel.facturacion.fncs.ajax;
import com.americatel.facturacion.fncs.fnc;
import com.americatel.facturacion.historial.Historial;
import com.americatel.facturacion.mappers.mapSERV_SUPL;
import com.americatel.facturacion.mappers.mapSERV_UNIC;
import com.americatel.facturacion.models.CIER;
import com.americatel.facturacion.models.SERV_UNIC;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;




/**
 *
 * @author crodas
 */
@Controller
public class Serv_Unic {
    @Autowired mapSERV_UNIC mapServ_Supl;
    @Autowired mapSERV_SUPL mapServ_Supl_original;
      
    @RequestMapping(value="/ajax/SERV_UNIC/getid/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> getid(HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (fnc.existsParameters(request, new String[]{"CO_SERV_UNIC"},respJson)){
                int c=fnc.getIntParameter(request,"CO_SERV_UNIC");            
                respJson.put("data",mapServ_Supl.getId(c));
                ajax.setTodoOk(respJson);
            }
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    }     
    
    @RequestMapping(value="/ajax/SERV_UNIC/select/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> select(@RequestParam(value="id",required = false) String id,HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (id==null){
                String nombre="";
                if (fnc.existsParameter(request, "NO_SERV_UNIC"))
                    nombre=fnc.getStringParameter(request, "NO_SERV_UNIC");
                respJson.put("data", mapServ_Supl.select(nombre));
            }else{
                respJson.put("data",mapServ_Supl.getId(Integer.parseInt(id)));            
            }        
            ajax.setTodoOk(respJson);
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    }
    
    
    @RequestMapping(value="/ajax/SERV_UNIC/insert/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> insert(@ModelAttribute("SERV_UNIC") SERV_UNIC reg, HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (fnc.existsParameters(request, new String[]{"CO_SERV_UNIC","CO_PROD","NO_SERV_UNIC","IM_MONTO","ST_AFEC_DETR","ST_ADIC"},respJson)){
              
                String codigo_producto="";
                double in_monto =0;
                String moneda = "";
                CIER objCier = new CIER();
                
                moneda = fnc.getStringParameter(request, "CO_MONE_FACT");
                in_monto = Double.parseDouble(fnc.getStringParameter(request, "IM_MONTO"));
                codigo_producto=fnc.getStringParameter(request, "COD_PROD_PADRE");
                
                objCier = mapServ_Supl_original.getTipoCambio();
                                                                 
                if(codigo_producto.equalsIgnoreCase("2")){                                                   
                    reg.setST_AFEC_DETR(true);
                }else{
                    
                    if(moneda.equalsIgnoreCase("2")){
                    
                        if(in_monto*objCier.getNU_TIPO_CAMB_ForJSON()> 700){
                          reg.setST_AFEC_DETR(true);
                        }else{
                          reg.setST_AFEC_DETR(false);
                        }
                        
                    }else{

                        if(in_monto > 700){
                          reg.setST_AFEC_DETR(true);
                        }else{
                          reg.setST_AFEC_DETR(false);
                        }

                    }
                                                            
                }
                
                                                
                Historial.initInsertar(request, reg);
                mapServ_Supl.insert(reg);
                ajax.setTodoOk(respJson);
            }
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    } 
    
    @RequestMapping(value="/ajax/SERV_UNIC/update/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> update(@ModelAttribute("SERV_UNIC") SERV_UNIC reg,HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (fnc.existsParameters(request, new String[]{"CO_SERV_UNIC","CO_PROD","NO_SERV_UNIC","IM_MONTO","ST_AFEC_DETR","ST_ADIC"},respJson)){
                
                String codigo_producto="";
                double in_monto =0;
                String moneda = "";
                CIER objCier = new CIER();
                
                moneda = fnc.getStringParameter(request, "CO_MONE_FACT");
                in_monto = Double.parseDouble(fnc.getStringParameter(request, "IM_MONTO"));
                codigo_producto=fnc.getStringParameter(request, "COD_PROD_PADRE");
                
                objCier = mapServ_Supl_original.getTipoCambio();
                                                             
                if(codigo_producto.equalsIgnoreCase("2")){                                                   
                    reg.setST_AFEC_DETR(true);
                }else{
                    
                    if(moneda.equalsIgnoreCase("2")){
                    
                        if(in_monto*objCier.getNU_TIPO_CAMB_ForJSON()> 700){
                          reg.setST_AFEC_DETR(true);
                        }else{
                          reg.setST_AFEC_DETR(false);
                        }
                        
                    }else{

                        if(in_monto > 700){
                          reg.setST_AFEC_DETR(true);
                        }else{
                          reg.setST_AFEC_DETR(false);
                        }

                    }
                    
                }                                 
                
                
                Historial.initModificar(request, reg);
                mapServ_Supl.update(reg);
                ajax.setTodoOk(respJson);
            }
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    } 
    
    @RequestMapping(value="/ajax/SERV_UNIC/delete/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> delete(HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (fnc.existsParameters(request, new String[]{"CO_SERV_UNIC"},respJson)){
                SERV_UNIC reg=mapServ_Supl.getId(fnc.getIntParameter(request, "CO_SERV_UNIC"));
                if (reg!=null){
                    mapServ_Supl.delete(reg);
                    ajax.setTodoOk(respJson);
                }
            }
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    }     
 

    @RequestMapping(value="/ajax/SERV_UNIC/selectByPROD/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> selectByPROD(@RequestParam(value="CO_PROD") int CO_PROD,@RequestParam(value="CO_MONE_FACT") int CO_MONE_FACT,HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            respJson.put("data", mapServ_Supl.selectByPROD(CO_PROD,CO_MONE_FACT));
            ajax.setTodoOk(respJson);
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    }    
}
