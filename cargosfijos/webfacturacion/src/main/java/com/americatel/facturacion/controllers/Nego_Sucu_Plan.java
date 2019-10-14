/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.controllers;

import com.americatel.facturacion.fncs.FechaHora;
import com.americatel.facturacion.fncs.ajax;
import com.americatel.facturacion.fncs.permissions;
import com.americatel.facturacion.fncs.fnc;
import com.americatel.facturacion.historial.Historial;
import com.americatel.facturacion.mappers.mapCIER;
import com.americatel.facturacion.mappers.mapNEGO_SUCU;
import com.americatel.facturacion.mappers.mapNEGO_SUCU_HISTORICO;
import com.americatel.facturacion.mappers.mapNEGO_SUCU_PLAN;
import com.americatel.facturacion.mappers.mapPLAN;
import com.americatel.facturacion.models.CIER;
import com.americatel.facturacion.models.NEGO;
import com.americatel.facturacion.models.NEGO_SUCU;
import com.americatel.facturacion.models.NEGO_SUCU_HISTORICO;
import com.americatel.facturacion.models.NEGO_SUCU_PLAN;
import com.americatel.facturacion.models.PLAN;
import com.americatel.facturacion.models.PROD;
import com.americatel.facturacion.models.USUA;
import java.util.Date;
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
public class Nego_Sucu_Plan {
    //Autowired
    @Autowired mapNEGO_SUCU mapNego_Sucu;
    @Autowired mapNEGO_SUCU_PLAN mapNego_Sucu_Plan;
    @Autowired mapPLAN mapPlan;
    @Autowired mapCIER mapCier;
    @Autowired  mapNEGO_SUCU_HISTORICO mapNego_Sucu_Historico;

    @RequestMapping(value="/ajax/NEGO_SUCU_PLAN/selectByNEGO_SUCU/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> selectByNEGO_SUCU(@RequestParam("CO_NEGO_SUCU") Integer CO_NEGO_SUCU,HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        ajax.setData(respJson,mapNego_Sucu_Plan.selectByNEGO_SUCU(CO_NEGO_SUCU));     
        ajax.setTodoOk(respJson);
        ajax.setResponseJSON(respJson);        
        return respJson;
    }
    
    @RequestMapping(value="/ajax/NEGO_SUCU_PLAN/selectActivoByNEGO_SUCU/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> selectActivoByNEGO_SUCU(@RequestParam("CO_NEGO_SUCU") Integer CO_NEGO_SUCU,HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        ajax.setData(respJson,mapNego_Sucu_Plan.selectActivoByNEGO_SUCU(CO_NEGO_SUCU));     
        ajax.setTodoOk(respJson);
        ajax.setResponseJSON(respJson);        
        return respJson;
    }
    
    @RequestMapping(value="/ajax/NEGO_SUCU_PLAN/desactivar/")
    public @ResponseBody Map<String,Object> desactivar(@ModelAttribute("NEGO_SUCU_PLAN") NEGO_SUCU_PLAN nego_sucu_plan,HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (nego_sucu_plan.getFE_FIN_ForJSON()!=null && nego_sucu_plan.getFE_INIC_ForJSON()!=null && nego_sucu_plan.getCO_OIT_DESI_ForJSON()!=null){
            String valido=nego_sucu_plan.isValid();
            if (valido==null){
                Historial.initModificar(request, nego_sucu_plan);
                mapNego_Sucu_Plan.desactivar(nego_sucu_plan);
                ajax.setTodoOk(respJson);                
            }else{
                ajax.setMsg(respJson, valido);
            }
        }else{
            ajax.setData(respJson, "No existe fecha desactivaci\u00f3n");
        }
        ajax.setResponseJSON(respJson);        
        return respJson;
    }
   
    @RequestMapping(value="/ajax/NEGO_SUCU_PLAN/agregar_plan/")
    public @ResponseBody Map<String,Object> agregar_plan(@ModelAttribute("NEGO_SUCU_PLAN") NEGO_SUCU_PLAN nego_sucu_plan,HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (nego_sucu_plan.getFE_INIC_ForJSON()!=null && nego_sucu_plan.getCO_PLAN_ForJSON()!=null){
            String valido=nego_sucu_plan.isValid();            
            if (valido==null) valido=nego_sucu_plan.isValidForInsert();                
            if (valido==null){
                Historial.initInsertar(request, nego_sucu_plan);
                mapNego_Sucu_Plan.insertar(nego_sucu_plan);
                ajax.setTodoOk(respJson); 
            }else{
                ajax.setMsg(respJson, valido);
            }
        }else{
            ajax.setData(respJson, "No existe fecha desactivaci\u00f3n");
        }
        ajax.setResponseJSON(respJson);        
        return respJson;
    }   
    
    @RequestMapping(value="/ajax/NEGO_SUCU_PLAN/cambiar_plan/")
    public @ResponseBody Map<String,Object> cambiar_plan(@ModelAttribute("NEGO_SUCU_PLAN") NEGO_SUCU_PLAN nego_sucu_plan,HttpServletRequest request){//@RequestBody MODU advancedFormData
        //Activa y desactiva un plan en una sucursal
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (nego_sucu_plan.getFE_INIC_ForJSON()!=null && nego_sucu_plan.getCO_PLAN_ForJSON()!=null && nego_sucu_plan.getCO_NEGO_SUCU_ForJSON()!=null){            
            
            if (nego_sucu_plan.getCO_OIT_INST_ForJSON()!=null){
                if (nego_sucu_plan.getCO_OIT_INST_ForJSON()==0){
                    nego_sucu_plan.setCO_OIT_INST(null);
                    nego_sucu_plan.setST_SOAR_INST(null);
                }
            }
            if (nego_sucu_plan.getCO_OIT_DESI_ForJSON()!=null){
                if (nego_sucu_plan.getCO_OIT_DESI_ForJSON()==0){
                    nego_sucu_plan.setCO_OIT_DESI(null);
                    nego_sucu_plan.setST_SOAR_DESI(null);
                }
            }            
                        
            NEGO_SUCU nego=mapNego_Sucu.getId(nego_sucu_plan.getCO_NEGO_SUCU_ForJSON());
            NEGO_SUCU_PLAN plan_activo=nego.getPlanActivo();
            String valido=null;
            if (plan_activo!=null){
                //Si existe plan activo lo desactivamos
                plan_activo.setFE_FIN(FechaHora.addDays(nego_sucu_plan.getFE_INIC_ForJSON(), -1));//Le quitamos un dia
                //La oit de instalacion se convierte en OIT de desinstalacion
                plan_activo.setST_SOAR_DESI(nego_sucu_plan.getST_SOAR_INST_ForJSON());
                plan_activo.setCO_OIT_DESI(nego_sucu_plan.getCO_OIT_INST_ForJSON());
                valido=plan_activo.isValid();  //retorna mensajes de validacion
            }     
            
            if (valido==null){
                valido=nego_sucu_plan.isValid();
            }
            
            if (valido!=null)
                ajax.setMsg(respJson, valido);
            else{
                Historial.initModificar(request, plan_activo);
                Historial.initInsertar(request, nego_sucu_plan);
                mapNego_Sucu_Plan.desactivar(plan_activo);
                mapNego_Sucu_Plan.insertar(nego_sucu_plan);
                
                //Registrar en el historico del negocio
                USUA usua=permissions.getUsuarioLogin(request);
                NEGO_SUCU_HISTORICO nego_sucu_historico=new NEGO_SUCU_HISTORICO();
                nego_sucu_historico.setCO_NEGO(nego.getCO_NEGO_ForJSON());
                nego_sucu_historico.setCO_NEGO_SUCU(nego_sucu_plan.getCO_NEGO_SUCU_ForJSON());
                nego_sucu_historico.setDE_INFORMACION("Cambi\u00f3 de plan");
                nego_sucu_historico.setCO_USUA_REGI(usua.getCO_USUA_ForJSON());
                nego_sucu_historico.setFH_REGI(new Date());
                mapNego_Sucu_Historico.insert(nego_sucu_historico);
                                    
                ajax.setTodoOk(respJson);
            }
        }else{
            ajax.setData(respJson, "No existe fecha desactivaci\u00f3n");
        }
        ajax.setResponseJSON(respJson);        
        return respJson;
    }      
    
    @RequestMapping(value="/ajax/NEGO_SUCU_PLAN/cambiar_tarifa/")
    public @ResponseBody Map<String,Object> cambiar_tarifa(@ModelAttribute("NEGO_SUCU_PLAN") NEGO_SUCU_PLAN nego_sucu_plan,HttpServletRequest request){//@RequestBody MODU advancedFormData
        //Activa y desactiva un plan en una sucursal
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (nego_sucu_plan.getCO_NEGO_SUCU_ForJSON()!=null){            
                        
            NEGO_SUCU nego_sucu=mapNego_Sucu.getId(nego_sucu_plan.getCO_NEGO_SUCU_ForJSON());
            NEGO nego = nego_sucu.getNego_ForJSON();
            NEGO_SUCU_PLAN nego_sucu_plan_activo=nego_sucu.getPlanActivo();
            
            nego_sucu_plan.setCO_OIT_DESI(null);
            nego_sucu_plan.setST_SOAR_DESI(null);
            
            if (nego_sucu_plan_activo!=null){
                //Crear un nuevo Plan
                PLAN plan_nuevo = nego_sucu_plan_activo.getPLAN();
                plan_nuevo.setCO_PLAN(0);
                plan_nuevo.setIM_MONTO(nego_sucu_plan.getIM_MONTO_ForJSON());
                PLAN plan_exist=mapPlan.selectExistPlan(plan_nuevo);
                Integer co_plan;
                if (plan_exist != null){
                    co_plan=plan_exist.getCO_PLAN_ForJSON();
                } else {
                    Historial.initInsertar(request, plan_nuevo);
                    mapPlan.insert(plan_nuevo);
                    co_plan=plan_nuevo.getCO_PLAN_ForJSON();
                }
                
                PROD prod = new PROD();
                prod.setCO_PROD(plan_nuevo.getCO_PROD_ForJSON());
                CIER cier = mapCier.getCierrePendienteByProducto(prod);
                Date fecha_activacion;
                
                if (nego_sucu_plan_activo.getFE_ULTI_FACT_ForJSON() != null){
                    fecha_activacion = FechaHora.addDays(nego_sucu_plan_activo.getFE_ULTI_FACT_ForJSON(), 1);
                } else {
                    if (nego.getCO_TIPO_FACT_ForJSON() == 1){ // Vencida
                        fecha_activacion = FechaHora.addMonths(FechaHora.getDate(cier.getNU_ANO_ForJSON(),cier.getNU_PERI_ForJSON()-1, 1), -1);
                    } else { // Adelantada
                        fecha_activacion = FechaHora.getDate(cier.getNU_ANO_ForJSON(),cier.getNU_PERI_ForJSON()-1, 1);
                    }
                }
                
                nego_sucu_plan.setFE_INIC(fecha_activacion);
                nego_sucu_plan.setCO_PLAN(co_plan);
                nego_sucu_plan.setCO_OIT_INST(nego_sucu_plan_activo.getCO_OIT_INST_ForJSON());
                nego_sucu_plan.setST_SOAR_INST(nego_sucu_plan_activo.getST_SOAR_INST_ForJSON());
                
                //Si existe plan activo lo desactivamos
                nego_sucu_plan_activo.setFE_FIN(FechaHora.addDays(nego_sucu_plan.getFE_INIC_ForJSON(), -1));//Le quitamos un dia
                //La oit de instalacion se convierte en OIT de desinstalacion
                nego_sucu_plan_activo.setST_SOAR_DESI(nego_sucu_plan_activo.getST_SOAR_INST_ForJSON());
                nego_sucu_plan_activo.setCO_OIT_DESI(nego_sucu_plan_activo.getCO_OIT_INST_ForJSON());
//                valido=plan_activo.isValid(); 
            }
            
            Historial.initModificar(request, nego_sucu_plan_activo);
            Historial.initInsertar(request, nego_sucu_plan);
            mapNego_Sucu_Plan.desactivar(nego_sucu_plan_activo);
            mapNego_Sucu_Plan.insertar(nego_sucu_plan);

            //Registrar en el historico del negocio
            USUA usua=permissions.getUsuarioLogin(request);
            NEGO_SUCU_HISTORICO nego_sucu_historico=new NEGO_SUCU_HISTORICO();
            nego_sucu_historico.setCO_NEGO(nego.getCO_NEGO_ForJSON());
            nego_sucu_historico.setCO_NEGO_SUCU(nego_sucu_plan.getCO_NEGO_SUCU_ForJSON());
            nego_sucu_historico.setDE_INFORMACION("Cambi\u00f3 de tarifa");
            nego_sucu_historico.setCO_USUA_REGI(usua.getCO_USUA_ForJSON());
            nego_sucu_historico.setFH_REGI(new Date());
            mapNego_Sucu_Historico.insert(nego_sucu_historico);
                
            ajax.setTodoOk(respJson);
        }
        ajax.setResponseJSON(respJson);        
        return respJson;
    } 
    
    @RequestMapping(value="/ajax/NEGO_SUCU_PLAN/delete/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> delete(HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (fnc.existsParameters(request, new String[]{"CO_NEGO_SUCU_PLAN"},respJson)){
                NEGO_SUCU_PLAN reg=mapNego_Sucu_Plan.getId(fnc.getIntParameter(request, "CO_NEGO_SUCU_PLAN"));
                if (reg!=null){
                    // Activar el ultimo plan desactivado
                    NEGO_SUCU_PLAN reg_des=mapNego_Sucu_Plan.getUltimoPlanDesactivado(reg.getCO_NEGO_SUCU_ForJSON());
                    if (reg_des!=null){
                        Historial.initModificar(request, reg_des);
                        mapNego_Sucu_Plan.updateActivarPlan(reg_des);
                    }
                    
                    // Elimina plan que no ha facturado
                    Historial.initEliminar(request, reg);
                    mapNego_Sucu_Plan.delete(reg);
                    ajax.setTodoOk(respJson);
                }
            }
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    }
    
    @RequestMapping(value="/ajax/NEGO_SUCU_PLAN/cambiar_velocidad_medio/")
    public @ResponseBody Map<String,Object> cambiar_velocidad_medio(@RequestParam("CO_NEGO_SUCU") Integer CO_NEGO_SUCU,@RequestParam(value="CO_NEGO_SUCU_PLAN") Integer CO_NEGO_SUCU_PLAN,@RequestParam(value="DE_VELO_SUBI",required = false) String DE_VELO_SUBI,@RequestParam(value="DE_VELO_BAJA",required = false) String DE_VELO_BAJA,@RequestParam(value="CO_PLAN_MEDI_INST",required = false) Integer CO_PLAN_MEDI_INST, HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (CO_NEGO_SUCU!=null && CO_NEGO_SUCU_PLAN!=null && DE_VELO_SUBI!=null && DE_VELO_BAJA!=null && CO_PLAN_MEDI_INST!=null){                
            NEGO_SUCU_PLAN nego_sucu_plan=mapNego_Sucu_Plan.getId(CO_NEGO_SUCU_PLAN);
            PLAN plan=nego_sucu_plan.getPlan_ForJSON();
            plan.setDE_VELO_BAJA(DE_VELO_BAJA);
            plan.setDE_VELO_SUBI(DE_VELO_SUBI);
            plan.setCO_PLAN_MEDI_INST(CO_PLAN_MEDI_INST);
            //plan.setNO_PLAN(plan.getFullNamePlan());BOTCHA
            if (!mapPlan.existePlan(plan)){
                Historial.initInsertar(request, plan);
                mapPlan.insert(plan);
                Integer co_plan=plan.getCO_PLAN_ForJSON();
                
                nego_sucu_plan.setCO_PLAN(co_plan);
                //nego_sucu_plan.setNO_PLAN(plan.getFullNamePlan());BOTCHA
                nego_sucu_plan.setDE_VELO_BAJA(plan.getDE_VELO_BAJA_ForJSON());
                nego_sucu_plan.setDE_VELO_SUBI(plan.getDE_VELO_SUBI_ForJSON());
            }else{
                PLAN plan_exist=mapPlan.selectExistPlan(plan);
                nego_sucu_plan.setCO_PLAN(plan_exist.getCO_PLAN_ForJSON());
                //nego_sucu_plan.setNO_PLAN(plan_exist.getFullNamePlan());BOTCHA
                nego_sucu_plan.setDE_VELO_BAJA(plan_exist.getDE_VELO_BAJA_ForJSON());
                nego_sucu_plan.setDE_VELO_SUBI(plan_exist.getDE_VELO_SUBI_ForJSON());
            }
            
            Historial.initModificar(request, nego_sucu_plan);
            mapNego_Sucu_Plan.updateVelocidadMedio(nego_sucu_plan);
            
            ajax.setTodoOk(respJson);
        }
        ajax.setResponseJSON(respJson);
        
        return respJson;
    }
    
}
