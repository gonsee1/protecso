/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.controllers;

import com.americatel.facturacion.fncs.ajax;
import com.americatel.facturacion.fncs.fnc;
import com.americatel.facturacion.historial.Historial;
import com.americatel.facturacion.mappers.mapPLAN;
import com.americatel.facturacion.mappers.mapPROD;
import com.americatel.facturacion.models.PLAN;
import com.americatel.facturacion.models.PROD;
import com.americatel.facturacion.models.REGLAS_NOMBRE_PLAN;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
public class Plan {
    @Autowired mapPLAN mapPlan;
    @Autowired mapPROD mapProd;
      
    @RequestMapping(value="/ajax/PLAN/getid/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> getid(HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (fnc.existsParameters(request, new String[]{"CO_PLAN"},respJson)){
                int c=fnc.getIntParameter(request,"CO_PLAN");            
                respJson.put("data",mapPlan.getId(c));
                ajax.setTodoOk(respJson);
            }
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    }     
    
    @RequestMapping(value="/ajax/PLAN/select/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> select(@RequestParam(value="id",required = false) String id,HttpServletRequest request){//@RequestBody MODU advancedFormData
    
        Map<String,Object> respJson=ajax.getResponseJSON();
                       
       if (ajax.hasPermission(request, respJson)){

              if (id==null){
              String nombre="";
              if (fnc.existsParameter(request, "NO_PLAN"))
                    nombre=fnc.getStringParameter(request, "NO_PLAN");
                respJson.put("data", mapPlan.select(nombre));
              }else{
                respJson.put("data",mapPlan.getId(Integer.parseInt(id)));            
              }        
             
             ajax.setTodoOk(respJson);
             ajax.setResponseJSON(respJson);
           
        }
        return respJson;
        
    }
    
    
    
    @RequestMapping(value="/ajax/PLAN/insert/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> insert(@ModelAttribute("PLAN") PLAN reg, HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (fnc.existsParameters(request, new String[]{"CO_PLAN","CO_PROD","IM_MONTO","ST_BACKUP_BU"},respJson)){
                                
                int codigo_producto;
                int codigo_servicio;
                int codigo_nombre_plan;
                int relacionadoTI;
                int aplicaArrendamiento;
                int velocidad;
                int velocidadCheck;
                String codigo_regla_nombre_plan;
                
                
                codigo_producto=Integer.parseInt(fnc.getStringParameter(request, "COD_PROD_PADRE"));
                codigo_servicio = Integer.parseInt(fnc.getStringParameter(request, "CO_PROD"));
                codigo_regla_nombre_plan = fnc.getStringParameter(request, "COD_REGLAS_NOMBRE_PLAN");
                
                PROD objProd = new PROD();
                
                //obtengo los valores del servicio
                objProd = mapProd.getOnlyId(codigo_servicio);               
                
                //comparo nombre del servicio 
                if(!objProd.getNO_PROD_ForJSON().equalsIgnoreCase("Ducteria") && 
                        !objProd.getNO_PROD_ForJSON().equalsIgnoreCase("Colocacion") && 
                             !objProd.getNO_PROD_ForJSON().equalsIgnoreCase("Otros servicios")){
                                        
                      codigo_regla_nombre_plan = "";
                }
                
                if(codigo_regla_nombre_plan.length()>0){
                    codigo_nombre_plan = Integer.parseInt(fnc.getStringParameter(request, "COD_REGLAS_NOMBRE_PLAN"));   
                }else{
                    codigo_nombre_plan = 0;   
                }                
                
                if(fnc.getStringParameter(request, "VELOCIDAD_OK_MAYORISTA").equalsIgnoreCase("true")){
                   velocidadCheck=1;
                }else{
                   velocidadCheck=0;
                }
                                                
                if(fnc.getStringParameter(request, "ST_BACKUP_BU").equalsIgnoreCase("true")){
                  aplicaArrendamiento=1;  
                }else{
                  aplicaArrendamiento=0;  
                }
                
                if(fnc.getStringParameter(request, "RELACIONADO_TI").equalsIgnoreCase("true")){
                    relacionadoTI=1;
                }else{
                    relacionadoTI=0;
                }
                                
                //velocidadCheck = Integer.parseInt(fnc.getStringParameter(request, "VELOCIDAD_OK_MAYORISTA"));                
                
                String velocidadBajada ="";
                
                if(fnc.getStringParameter(request, "DE_VELO_BAJA").length()>0){
                    velocidadBajada =fnc.getStringParameter(request, "DE_VELO_BAJA");
                    velocidadCheck = 1;
                }else{
                    velocidadBajada="";
                    velocidad=0;
                }
                
                //aplicaArrendamiento = Integer.parseInt(fnc.getStringParameter(request, "ST_BACKUP_BU"));
                //relacionadoTI = Integer.parseInt(fnc.getStringParameter(request, "VELOCIDAD_OK_MAYORISTA"));
                
                List<REGLAS_NOMBRE_PLAN> reglas_nombre_plan = null;
                
                if(codigo_nombre_plan!=0){
                  reglas_nombre_plan = mapPlan.getNombrePlanByCodigo(codigo_nombre_plan);
                }else{
                  reglas_nombre_plan = mapPlan.getNombrePlan(codigo_producto,codigo_servicio,velocidadCheck,aplicaArrendamiento,relacionadoTI);                  
                }                    
                                
                if(reglas_nombre_plan.size()>0){
                	
                	reg.setPRODUCTO_CARGA(reglas_nombre_plan.get(0).getPRODUCTO_CARGA_ForJSON());
                	
                    if(velocidadBajada.length()>0){
                        reg.setNO_PLAN(reglas_nombre_plan.get(0).getNOMBRE_PLAN_ForJSON()+" "+velocidadBajada+" "+"KBPS");
                    }else{
                        reg.setNO_PLAN(reglas_nombre_plan.get(0).getNOMBRE_PLAN_ForJSON());
                    }
                    
                }else{
                    reg.setNO_PLAN("");
                }
                
                
                //reg.setNO_PLAN(reg.getFullNamePlan(codigo_producto,codigo_servicio,codigo_nombre_plan,velocidadCheck,aplicaArrendamiento,relacionadoTI)); // Se autogenera el nombre del plan                                
                PLAN plan_exist=mapPlan.selectExistPlan(reg);
                
                if (plan_exist == null){
                    Historial.initInsertar(request, reg);                   
                    mapPlan.insert(reg);
                    ajax.setTodoOk(respJson);
                } else {
                    ajax.setError(respJson, "Ya existe un plan con las caracter\u00edsticas ingresadas.");
                }
            
            }
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    } 
    
    @RequestMapping(value="/ajax/PLAN/update/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> update(@ModelAttribute("PLAN") PLAN reg,HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (fnc.existsParameters(request, new String[]{"CO_PLAN","CO_PROD","NO_PLAN","IM_MONTO","ST_BACKUP_BU"},respJson)){
                
                //////////////////////////////////////////////
                int codigo_producto;
                int codigo_servicio;
                int codigo_nombre_plan;
                int relacionadoTI;
                int aplicaArrendamiento;
                int velocidad;
                int velocidadCheck;
                String codigo_regla_nombre_plan;
                
                
                codigo_producto=Integer.parseInt(fnc.getStringParameter(request, "COD_PROD_PADRE"));
                codigo_servicio = Integer.parseInt(fnc.getStringParameter(request, "CO_PROD"));
                codigo_regla_nombre_plan = fnc.getStringParameter(request, "COD_REGLAS_NOMBRE_PLAN");
                
                PROD objProd = new PROD();
                
                objProd = mapProd.getOnlyId(codigo_servicio);               
                
                if(!objProd.getNO_PROD_ForJSON().equalsIgnoreCase("Ducteria") && 
                        !objProd.getNO_PROD_ForJSON().equalsIgnoreCase("Colocacion") && 
                             !objProd.getNO_PROD_ForJSON().equalsIgnoreCase("Otros servicios")){
                                        
                      codigo_regla_nombre_plan = "";
                }
                
                if(codigo_regla_nombre_plan.length()>0){
                    codigo_nombre_plan = Integer.parseInt(fnc.getStringParameter(request, "COD_REGLAS_NOMBRE_PLAN"));   
                }else{
                    codigo_nombre_plan = 0;   
                }                
                
                if(fnc.getStringParameter(request, "VELOCIDAD_OK_MAYORISTA").equalsIgnoreCase("true")){
                   velocidadCheck=1;
                }else{
                   velocidadCheck=0;
                }
                                                
                if(fnc.getStringParameter(request, "ST_BACKUP_BU").equalsIgnoreCase("true")){
                  aplicaArrendamiento=1;  
                }else{
                  aplicaArrendamiento=0;  
                }
                
                if(fnc.getStringParameter(request, "RELACIONADO_TI").equalsIgnoreCase("true")){
                    relacionadoTI=1;
                }else{
                    relacionadoTI=0;
                }                                
                //velocidadCheck = Integer.parseInt(fnc.getStringParameter(request, "VELOCIDAD_OK_MAYORISTA"));                                
                String velocidadBajada ="";
                
                if(fnc.getStringParameter(request, "DE_VELO_BAJA").length()>0){
                    velocidadBajada =fnc.getStringParameter(request, "DE_VELO_BAJA");
                    velocidadCheck = 1;
                }else{
                    velocidadBajada="";
                    velocidad=0;
                }
                
                //aplicaArrendamiento = Integer.parseInt(fnc.getStringParameter(request, "ST_BACKUP_BU"));
                //relacionadoTI = Integer.parseInt(fnc.getStringParameter(request, "VELOCIDAD_OK_MAYORISTA"));
                
                List<REGLAS_NOMBRE_PLAN> reglas_nombre_plan = null;
                
                if(codigo_nombre_plan!=0){
                  reglas_nombre_plan = mapPlan.getNombrePlanByCodigo(codigo_nombre_plan);
                }else{
                  reglas_nombre_plan = mapPlan.getNombrePlan(codigo_producto,codigo_servicio,velocidadCheck,aplicaArrendamiento,relacionadoTI);                  
                }                    
                                
                if(reglas_nombre_plan.size()>0){
                	reg.setPRODUCTO_CARGA(reglas_nombre_plan.get(0).getPRODUCTO_CARGA_ForJSON());
                    if(velocidadBajada.length()>0){
                        reg.setNO_PLAN(reglas_nombre_plan.get(0).getNOMBRE_PLAN_ForJSON()+" "+velocidadBajada+" "+"KBPS");
                    }else{
                        reg.setNO_PLAN(reglas_nombre_plan.get(0).getNOMBRE_PLAN_ForJSON());
                    }
                    
                }else{
                    reg.setNO_PLAN("");
                }                                
                //////////////////////////////////////////////

                //reg.setNO_PLAN(reg.getFullNamePlan(1,1,1,1,1,1)); // Se autogenera el nombre del plan
                PLAN plan_exist=mapPlan.selectExistPlan(reg);
                if (plan_exist == null){
                    
                    String codigo_producto_string="";

                    codigo_producto_string=fnc.getStringParameter(request, "COD_PROD_PADRE");
                    
                    if(codigo_producto_string.equalsIgnoreCase("2")){
                        reg.setDE_VELO_BAJA("");
                        reg.setDE_VELO_SUBI("");                    
                    }                    
                    
                    Historial.initModificar(request, reg);
                    mapPlan.update(reg);
                    ajax.setTodoOk(respJson);
                } else {
                    ajax.setError(respJson, "Ya existe un plan con las caracter\u00edsticas ingresadas.");
                }
            }
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    } 
    
    @RequestMapping(value="/ajax/PLAN/delete/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> delete(HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (fnc.existsParameters(request, new String[]{"CO_PLAN"},respJson)){
                PLAN reg=mapPlan.getId(fnc.getIntParameter(request, "CO_PLAN"));
                if (reg!=null){
                    mapPlan.delete(reg);
                    ajax.setTodoOk(respJson);
                }
            }
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    }     

    
    @RequestMapping(value="/ajax/PLAN/selectByPRODAndMONE/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> selectByPRODAndMONE(@RequestParam(value="CO_PROD") Integer CO_PROD,@RequestParam(value="CO_MONE_FACT") Integer CO_MONE_FACT,HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
        	if (CO_PROD!=null){
	            respJson.put("data", mapPlan.selectByPRODAndMONE(CO_PROD,CO_MONE_FACT));	      
	            ajax.setTodoOk(respJson);
        	}
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    }
    
    @RequestMapping(value="/ajax/PLAN/getNombrePlanByServicio/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> getNombrePlanByServicio(@RequestParam(value="CO_PROD") String CO_PROD,HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
        	if (CO_PROD!=null){
	            respJson.put("data", mapPlan.getNombrePlanByServicio(CO_PROD));	      
	            ajax.setTodoOk(respJson);
        	}
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    }    
    
    @RequestMapping(value="/ajax/PLAN/getIdNombrePlan/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> getIdNombrePlan(@RequestParam(value="NO_PLAN") String NO_PLAN,HttpServletRequest request){//@RequestBody MODU advancedFormData

        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
        	if (NO_PLAN!=null){
	            respJson.put("data", mapPlan.getIdNombrePlan(NO_PLAN));	      
	            ajax.setTodoOk(respJson);
        	}
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    }

}
