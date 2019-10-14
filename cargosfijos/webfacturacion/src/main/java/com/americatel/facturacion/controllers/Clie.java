/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.controllers;

import com.americatel.facturacion.fncs.Pagination;
import com.americatel.facturacion.fncs.ajax;
import com.americatel.facturacion.fncs.fnc;
import com.americatel.facturacion.historial.Historial;
import com.americatel.facturacion.mappers.mapCLIE;
import com.americatel.facturacion.mappers.mapCONT_CLIE;
import com.americatel.facturacion.mappers.mapSUCU;
import com.americatel.facturacion.models.CLIE;
import com.americatel.facturacion.models.CONT_CLIE;
import com.americatel.facturacion.models.SUCU;
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
public class Clie {
    @Autowired mapCLIE mapClie;
    @Autowired mapSUCU mapSucu;
    @Autowired mapCONT_CLIE mapCont_Clie;

      
    @RequestMapping(value="/ajax/CLIE/getid/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> getid(HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (fnc.existsParameters(request, new String[]{"CO_CLIE"},respJson)){
                int c=fnc.getIntParameter(request,"CO_CLIE");            
                respJson.put("data",mapClie.getId(c));
                ajax.setTodoOk(respJson);
            }
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    }     
    
    @RequestMapping(value="/ajax/CLIE/select/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> select(@ModelAttribute("Pagination") Pagination pagination,@RequestParam(value="SEARCH",required = false) String SEARCH,@RequestParam(value="id",required = false) String id,@RequestParam(value="FIL_CLIE",required = false) String FIL_CLIE,HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        
//        System.out.print("ENTRO PRUEBA");
        
        if (ajax.hasPermission(request, respJson)){ 
            if (SEARCH==null){
                if (id==null){
                    String nombre="";
                    if (fnc.existsParameter(request, "FIL_CLIE"))
                        nombre=fnc.getStringParameter(request, "FIL_CLIE");
                    List<CLIE> lstClie=mapClie.select(nombre.trim(),pagination);
                    int son=mapClie.getNumResultsSelect(nombre.trim());                    
                    ajax.setData(respJson,lstClie);  
                    ajax.setNumResults(respJson, son);
                }else{                
                    ajax.setData(respJson,mapClie.getId(Integer.parseInt(id)));            
                }                
            }else{
                ajax.setData(respJson,mapClie.selectBySEARCH(SEARCH.trim(),pagination));  
                ajax.setNumResults(respJson, mapClie.getNumResultsSEARCH(SEARCH.trim()));                
            }
            ajax.setTodoOk(respJson);
        }
        ajax.setResponseJSON(respJson);
        return respJson;
    }
    
    
    @RequestMapping(value="/ajax/CLIE/insert/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> insert(@ModelAttribute("CLIE") CLIE reg,@RequestParam(value="CONT_NO_CONT_CLIE",required = false) String[] CONT_NO_CONT_CLIE,@RequestParam(value="CONT_AP_CONT_CLIE",required = false) String[] CONT_AP_CONT_CLIE,@RequestParam(value="CONT_CO_TIPO_DOCU",required = false) Integer[] CONT_CO_TIPO_DOCU,@RequestParam(value="CONT_DE_NUME_DOCU",required = false) String[] CONT_DE_NUME_DOCU,@RequestParam(value="CONT_DE_CORR",required = false) String[] CONT_DE_CORR,@RequestParam(value="CONT_DE_TELE",required = false) String[] CONT_DE_TELE, HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if ( (CONT_NO_CONT_CLIE==null && CONT_AP_CONT_CLIE==null && CONT_CO_TIPO_DOCU==null && CONT_DE_NUME_DOCU==null && CONT_DE_CORR==null && CONT_DE_TELE==null) || (CONT_NO_CONT_CLIE.length==CONT_AP_CONT_CLIE.length && CONT_CO_TIPO_DOCU.length==CONT_AP_CONT_CLIE.length  && CONT_DE_NUME_DOCU.length==CONT_CO_TIPO_DOCU.length && CONT_DE_CORR.length==CONT_DE_NUME_DOCU.length && CONT_DE_TELE.length==CONT_DE_CORR.length) ){
                if (fnc.existsParameters(request, new String[]{"NO_RAZO","NO_CLIE","AP_CLIE","CO_TIPO_DOCU","DE_NUME_DOCU","CO_DIST_FISC","DE_DIRE_FISC","CO_TIPO_DOCU_CONT_CLIE","DE_NUME_DOCU_CONT_CLIE","NO_CONT_CLIE","AP_CONT_CLIE","DE_CORR","DE_TELE"},respJson)){                
                    Historial.initInsertar(request, reg);
                    mapClie.insert(reg);
                    SUCU sucu=new SUCU();
                    sucu.setCO_CLIE(reg.getCO_CLIE_ForJSON());
                    sucu.setCO_DIST(fnc.getIntParameter(request, "CO_DIST_FISC"));
                    sucu.setDE_DIRE(fnc.getStringParameter(request, "DE_DIRE_FISC"));
                    Historial.initInsertar(request, sucu);
                    mapSucu.insert(sucu);
                    reg.setCO_SUCU_FISC(sucu.getCO_SUCU_ForJSON());
                    Historial.initModificar(request, reg);
                    mapClie.updateSucursalFiscal(reg);                

                    CONT_CLIE cont=new CONT_CLIE();
                    cont.setNO_CONT_CLIE(fnc.getStringParameter(request, "NO_CONT_CLIE"));
                    cont.setAP_CONT_CLIE(fnc.getStringParameter(request, "AP_CONT_CLIE"));
                    cont.setCO_CLIE(reg.getCO_CLIE_ForJSON());
                    cont.setCO_TIPO_DOCU(fnc.getIntParameter(request, "CO_TIPO_DOCU_CONT_CLIE"));
                    cont.setDE_NUME_DOCU(fnc.getStringParameter(request, "DE_NUME_DOCU_CONT_CLIE"));
                    cont.setDE_CORR(fnc.getStringParameter(request, "DE_CORR"));
                    cont.setDE_TELE(fnc.getStringParameter(request, "DE_TELE"));
                    Historial.initInsertar(request, cont);
                    mapCont_Clie.insert(cont);
                    reg.setCO_CONT_CLIE_REPR_LEGA(cont.getCO_CONT_CLIE_ForJSON());
                    Historial.initModificar(request, reg);
                    mapClie.updateRepresentanteLegal(reg);
                    
                    if (CONT_NO_CONT_CLIE!=null){
                        for(int i=0;i<CONT_NO_CONT_CLIE.length;i++){
                            cont=new CONT_CLIE();
                            cont.setNO_CONT_CLIE(CONT_NO_CONT_CLIE[i]);
                            cont.setAP_CONT_CLIE(CONT_AP_CONT_CLIE[i]);
                            cont.setCO_CLIE(reg.getCO_CLIE_ForJSON());
                            cont.setCO_TIPO_DOCU(CONT_CO_TIPO_DOCU[i]);
                            cont.setDE_NUME_DOCU(CONT_DE_NUME_DOCU[i]);
                            if (!CONT_DE_CORR[i].equals("None")){
                                cont.setDE_CORR(CONT_DE_CORR[i]);
                            }
                            if (!CONT_DE_TELE[i].equals("None")){
                                cont.setDE_TELE(CONT_DE_TELE[i]);
                            }
                            Historial.initInsertar(request, cont);
                            mapCont_Clie.insert(cont);                            
                        }
                    }
                    ajax.setData(respJson,reg.getCO_CLIE_ForJSON());
                    ajax.setTodoOk(respJson);
                }                
            }
            ajax.setResponseJSON(respJson);
        }        
        return respJson;
    } 
    
    @RequestMapping(value="/ajax/CLIE/update/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> update(@ModelAttribute("CLIE") CLIE reg, HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
        	
        	System.out.println("CO_CLIENTE :    "+reg.getCO_CLIE_ForJSON());
        	
            if (fnc.existsParameters(request, new String[]{"CO_CLIE","NO_RAZO","NO_CLIE","AP_CLIE","DE_DIGI_BUM","CO_TIPO_CLIE","CO_TIPO_DOCU","DE_NUME_DOCU"},respJson)){
                if (reg!=null){
                    Historial.initModificar(request, reg);
                    mapClie.update(reg);
                    ajax.setTodoOk(respJson);
                }
            }
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    } 
    
    @RequestMapping(value="/ajax/CLIE/delete/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> delete(HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (fnc.existsParameters(request, new String[]{"CO_CLIE"},respJson)){
                CLIE reg=mapClie.getId(fnc.getIntParameter(request, "CO_CLIE"));
                if (reg!=null){
                    Historial.initEliminar(request, reg);
                    mapClie.delete(reg);
                    ajax.setTodoOk(respJson);
                }
            }
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    }  
    
    
    @RequestMapping(value="/ajax/CLIE/existeDocumento/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> existeDocumento(HttpServletRequest request,@RequestParam(value="DE_NUME_DOCU",required = true) String DE_NUME_DOCU,@RequestParam(value="CO_TIPO_DOCU",required = true) Integer CO_TIPO_DOCU,@RequestParam(value="CO_CLIE",required = true) Integer CO_CLIE){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (DE_NUME_DOCU!=null && CO_TIPO_DOCU!=null ){                
                if (CO_CLIE==null) CO_CLIE=0;
                ajax.setData(respJson,mapClie.existeDocumento(CO_CLIE,DE_NUME_DOCU,CO_TIPO_DOCU));
                ajax.setTodoOk(respJson);
            }
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    }     
       
}
