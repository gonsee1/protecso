/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.controllers;

import com.americatel.facturacion.fncs.ajax;
import com.americatel.facturacion.fncs.fnc;
import com.americatel.facturacion.fncs.permissions;
import com.americatel.facturacion.historial.Historial;
import com.americatel.facturacion.mappers.mapNEGO_SUCU;
import com.americatel.facturacion.mappers.mapNEGO_SUCU_HISTORICO;
import com.americatel.facturacion.mappers.mapNEGO_SUCU_PLAN;
import com.americatel.facturacion.mappers.mapNEGO_SUCU_SERV_SUPL;
import com.americatel.facturacion.mappers.mapSERV_SUPL;
import com.americatel.facturacion.models.NEGO_SUCU;
import com.americatel.facturacion.models.NEGO_SUCU_HISTORICO;
import com.americatel.facturacion.models.NEGO_SUCU_PLAN;
import com.americatel.facturacion.models.NEGO_SUCU_SERV_SUPL;
import com.americatel.facturacion.models.SERV_SUPL;
import com.americatel.facturacion.models.USUA;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
public class Nego_Sucu_Serv_Supl {
    @Autowired  mapNEGO_SUCU_HISTORICO mapNego_Sucu_Historico;
    @Autowired mapNEGO_SUCU_SERV_SUPL mapNego_Sucu_Serv_Supl;
    @Autowired mapSERV_SUPL mapServ_Supl;
    @Autowired mapNEGO_SUCU mapNego_Sucu;

    @RequestMapping(value="/ajax/NEGO_SUCU_SERV_SUPL/selectByNEGO_SUCU/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> selectByNEGO_SUCU(@RequestParam("CO_NEGO_SUCU") Integer CO_NEGO_SUCU,HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        ajax.setData(respJson,mapNego_Sucu_Serv_Supl.selectByNEGO_SUCU(CO_NEGO_SUCU));     
        ajax.setTodoOk(respJson);
        ajax.setResponseJSON(respJson);        
        return respJson;
    }
    
    @RequestMapping(value="/ajax/NEGO_SUCU_SERV_SUPL/selectActivoByNEGO_SUCU/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> selectActivoByNEGO_SUCU(@RequestParam("CO_NEGO_SUCU") Integer CO_NEGO_SUCU,HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        ajax.setData(respJson,mapNego_Sucu_Serv_Supl.selectActivoByNEGO_SUCU(CO_NEGO_SUCU));     
        ajax.setTodoOk(respJson);
        ajax.setResponseJSON(respJson);        
        return respJson;
    }
    
    @RequestMapping(value="/ajax/NEGO_SUCU_SERV_SUPL/agregar_servicio/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> agregar_servicio(@ModelAttribute("NEGO_SUCU_SERV_SUPL") NEGO_SUCU_SERV_SUPL nego_sucu_serv_supl,@RequestParam(value="CO_SERV_SUPL_ARR",required = false) Integer[] cod_serv_supls,HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (cod_serv_supls!=null){    
            ajax.setTodoOk(respJson);
            List<NEGO_SUCU_SERV_SUPL> lista=new ArrayList<NEGO_SUCU_SERV_SUPL>();
            String mensajeError="",valido=null;
            for(Integer cod_serv_supl : cod_serv_supls){
                NEGO_SUCU_SERV_SUPL tmp=new NEGO_SUCU_SERV_SUPL();
                tmp.setCO_NEGO_SUCU(nego_sucu_serv_supl.getCO_NEGO_SUCU_ForJSON());                
                tmp.setST_SOAR_INST(nego_sucu_serv_supl.getST_SOAR_INST_ForJSON());
                tmp.setCO_OIT_INST(nego_sucu_serv_supl.getCO_OIT_INST_ForJSON());                
                tmp.setFE_INIC(nego_sucu_serv_supl.getFE_INIC_ForJSON());
                tmp.setCO_SERV_SUPL(cod_serv_supl);
                SERV_SUPL tmpSS=mapServ_Supl.getId(cod_serv_supl);
                if (tmpSS!=null){
                    tmp.setCO_SERV_SUPL(tmpSS.getCO_SERV_SUPL_ForJSON());
                    tmp.setIM_MONTO(tmpSS.getIM_MONTO_ForJSON());
                    tmp.setNO_SERV_SUPL(tmpSS.getNO_SERV_SUPL_ForJSON());
                    tmp.setST_AFEC_DETR(tmpSS.isST_AFEC_DETR_ForJSON());
                }              
                
                valido=tmp.isValid();                
                if (valido==null){
                    Historial.initInsertar(request, tmp);
                    mapNego_Sucu_Serv_Supl.insert(tmp);
                    
                    //Registrar en el historico del negocio
                    NEGO_SUCU nego=mapNego_Sucu.getId(nego_sucu_serv_supl.getCO_NEGO_SUCU_ForJSON());
                    USUA usua=permissions.getUsuarioLogin(request);
                    NEGO_SUCU_HISTORICO nego_sucu_historico=new NEGO_SUCU_HISTORICO();
                    nego_sucu_historico.setCO_NEGO(nego.getCO_NEGO_ForJSON());
                    nego_sucu_historico.setCO_NEGO_SUCU(nego_sucu_serv_supl.getCO_NEGO_SUCU_ForJSON());
                    nego_sucu_historico.setDE_INFORMACION("Se agreg\u00f3 servicio");
                    nego_sucu_historico.setCO_USUA_REGI(usua.getCO_USUA_ForJSON());
                    nego_sucu_historico.setFH_REGI(new Date());
                    mapNego_Sucu_Historico.insert(nego_sucu_historico);
                    
                }else{
                    mensajeError+=valido+";";
                }
            }
            if (mensajeError.equals("")){                
                ajax.setTodoOk(respJson);
            }else{
                ajax.setError(respJson, mensajeError);
            }
        }else{
            ajax.setError(respJson, "No existen servicios suplementarios para agregar.");
        }
        ajax.setResponseJSON(respJson);        
        return respJson;
    }  
    
    
    @RequestMapping(value="/ajax/NEGO_SUCU_SERV_SUPL/desactivar_servicio/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> desactivar_servicio(@ModelAttribute("NEGO_SUCU_SERV_SUPL") NEGO_SUCU_SERV_SUPL nego_sucu_serv_supl,HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        
        String msjError=nego_sucu_serv_supl.isValid();
        if (msjError==null){
            Historial.initModificar(request, nego_sucu_serv_supl);
            mapNego_Sucu_Serv_Supl.desactivar(nego_sucu_serv_supl); 
            
            NEGO_SUCU nego=mapNego_Sucu.getId(nego_sucu_serv_supl.getCO_NEGO_SUCU_ForJSON());
            //Registrar en el historico del negocio
            USUA usua=permissions.getUsuarioLogin(request);
            NEGO_SUCU_HISTORICO nego_sucu_historico=new NEGO_SUCU_HISTORICO();
            nego_sucu_historico.setCO_NEGO(nego.getCO_NEGO_ForJSON());
            nego_sucu_historico.setCO_NEGO_SUCU(nego_sucu_serv_supl.getCO_NEGO_SUCU_ForJSON());
            nego_sucu_historico.setDE_INFORMACION("Se desactiv\u00f3 el servicio");
            nego_sucu_historico.setCO_USUA_REGI(usua.getCO_USUA_ForJSON());
            nego_sucu_historico.setFH_REGI(new Date());
            mapNego_Sucu_Historico.insert(nego_sucu_historico);
            
            ajax.setTodoOk(respJson);
        }else{
            ajax.setError(respJson, msjError);
        }        
        
        ajax.setResponseJSON(respJson);        
        return respJson;
    } 
    
    @RequestMapping(value="/ajax/NEGO_SUCU_SERV_SUPL/delete/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> delete(HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (fnc.existsParameters(request, new String[]{"CO_NEGO_SUCU_SERV_SUPL"},respJson)){
                NEGO_SUCU_SERV_SUPL reg=mapNego_Sucu_Serv_Supl.getId(fnc.getIntParameter(request, "CO_NEGO_SUCU_SERV_SUPL"));
                if (reg!=null){
                    Historial.initEliminar(request, reg);
                    mapNego_Sucu_Serv_Supl.delete(reg);
                    ajax.setTodoOk(respJson);
                }
            }
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    }
}
