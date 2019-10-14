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
import com.americatel.facturacion.mappers.mapNEGO_SUCU_SERV_UNIC;
import com.americatel.facturacion.mappers.mapSERV_SUPL;
import com.americatel.facturacion.mappers.mapSERV_UNIC;
import com.americatel.facturacion.models.NEGO_SUCU;
import com.americatel.facturacion.models.NEGO_SUCU_HISTORICO;
import com.americatel.facturacion.models.NEGO_SUCU_PLAN;
import com.americatel.facturacion.models.NEGO_SUCU_SERV_SUPL;
import com.americatel.facturacion.models.NEGO_SUCU_SERV_UNIC;
import com.americatel.facturacion.models.SERV_SUPL;
import com.americatel.facturacion.models.SERV_UNIC;
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
public class Nego_Sucu_Serv_Unic {
    @Autowired  mapNEGO_SUCU_HISTORICO mapNego_Sucu_Historico;
    @Autowired mapNEGO_SUCU_SERV_UNIC mapNego_Sucu_Serv_Unic;
    @Autowired mapSERV_UNIC mapServ_Unic;
    @Autowired mapNEGO_SUCU mapNego_Sucu;

    @RequestMapping(value="/ajax/NEGO_SUCU_SERV_UNIC/selectByNEGO_SUCU/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> selectByNEGO_SUCU(@RequestParam("CO_NEGO_SUCU") Integer CO_NEGO_SUCU,HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        ajax.setData(respJson,mapNego_Sucu_Serv_Unic.selectByNEGO_SUCU(CO_NEGO_SUCU));     
        ajax.setTodoOk(respJson);
        ajax.setResponseJSON(respJson);        
        return respJson;
    }
    
    
    @RequestMapping(value="/ajax/NEGO_SUCU_SERV_UNIC/agregar_servicio/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> agregar_servicio(@ModelAttribute("NEGO_SUCU_SERV_UNIC") NEGO_SUCU_SERV_UNIC nego_sucu_serv_unic,@RequestParam(value="CO_SERV_UNIC_ARR",required = false) Integer[] cod_serv_unics,HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (cod_serv_unics!=null){    
            ajax.setTodoOk(respJson);
            List<NEGO_SUCU_SERV_UNIC> lista=new ArrayList<NEGO_SUCU_SERV_UNIC>();
            String mensajeError="",valido=null;
            for(Integer cod_serv_unic : cod_serv_unics){
                NEGO_SUCU_SERV_UNIC tmp=new NEGO_SUCU_SERV_UNIC();
                tmp.setCO_NEGO_SUCU(nego_sucu_serv_unic.getCO_NEGO_SUCU_ForJSON());                
                tmp.setST_SOAR_INST(nego_sucu_serv_unic.getST_SOAR_INST_ForJSON());
                tmp.setCO_OIT_INST(nego_sucu_serv_unic.getCO_OIT_INST_ForJSON());               
                
                tmp.setCO_SERV_UNIC(cod_serv_unic);
                SERV_UNIC tmpSS=mapServ_Unic.getId(cod_serv_unic);
                if (tmpSS!=null){
                    tmp.setCO_SERV_UNIC(tmpSS.getCO_SERV_UNIC_ForJSON());
                    tmp.setIM_MONTO(tmpSS.getIM_MONTO_ForJSON());
                    tmp.setNO_SERV_UNIC(tmpSS.getNO_SERV_UNIC_ForJSON());
                    tmp.setST_AFEC_DETR(tmpSS.getST_AFEC_DETR_ForJSON());                    
                }              
                
                valido=tmp.isValid();                
                if (valido==null){
                    Historial.initModificar(request, tmp);
                    mapNego_Sucu_Serv_Unic.insert(tmp);
                    
                    //Registrar en el historico del negocio
                    NEGO_SUCU nego=mapNego_Sucu.getId(nego_sucu_serv_unic.getCO_NEGO_SUCU_ForJSON());
                    USUA usua=permissions.getUsuarioLogin(request);
                    NEGO_SUCU_HISTORICO nego_sucu_historico=new NEGO_SUCU_HISTORICO();
                    nego_sucu_historico.setCO_NEGO(nego.getCO_NEGO_ForJSON());
                    nego_sucu_historico.setCO_NEGO_SUCU(nego_sucu_serv_unic.getCO_NEGO_SUCU_ForJSON());
                    nego_sucu_historico.setDE_INFORMACION("Se agreg\u00f3 servicio \u00fanico");
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
            ajax.setError(respJson, "No existen servicios unicos para agregar.");
        }
        ajax.setResponseJSON(respJson);        
        return respJson;
    }  
    
    @RequestMapping(value="/ajax/NEGO_SUCU_SERV_UNIC/delete/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> delete(HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (fnc.existsParameters(request, new String[]{"CO_NEGO_SUCU_SERV_UNIC"},respJson)){
                NEGO_SUCU_SERV_UNIC reg=mapNego_Sucu_Serv_Unic.getId(fnc.getIntParameter(request, "CO_NEGO_SUCU_SERV_UNIC"));
                if (reg!=null){
                    Historial.initEliminar(request, reg);
                    mapNego_Sucu_Serv_Unic.delete(reg);
                    ajax.setTodoOk(respJson);
                }
            }
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    }
    
    /*
    @RequestMapping(value="/ajax/NEGO_SUCU_SERV_UNIC/desactivar_servicio/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> desactivar_servicio(@ModelAttribute("NEGO_SUCU_SERV_UNIC") NEGO_SUCU_SERV_UNIC nego_sucu_serv_supl,HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        
        String msjError=nego_sucu_serv_supl.isValid();
        if (msjError==null){
            mapNego_Sucu_Serv_Unic.desactivar(nego_sucu_serv_supl);            
            ajax.setTodoOk(respJson);
        }else{
            ajax.setError(respJson, msjError);
        }        
        
        ajax.setResponseJSON(respJson);        
        return respJson;
    } 
    
    */
}
