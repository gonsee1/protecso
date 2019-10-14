/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.controllers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.americatel.facturacion.core3.ProcesoNegocio;
import com.americatel.facturacion.fncs.Pagination;
import com.americatel.facturacion.fncs.ajax;
import com.americatel.facturacion.fncs.encrypt;
import com.americatel.facturacion.fncs.fnc;
import com.americatel.facturacion.fncs.permissions;
import com.americatel.facturacion.historial.Historial;
import com.americatel.facturacion.mappers.mapCIER;
import com.americatel.facturacion.mappers.mapCLIE;
import com.americatel.facturacion.mappers.mapNEGO;
import com.americatel.facturacion.mappers.mapNEGO_CORR;
import com.americatel.facturacion.models.CIER;
import com.americatel.facturacion.models.NEGO;
import com.americatel.facturacion.models.NEGO_CORR;
import com.americatel.facturacion.models.PROD;




/**
 *
 * @author crodas
 */
@Controller
public class Nego {
    @Autowired mapNEGO mapNego;
    @Autowired mapNEGO_CORR mapNego_Corr;
    @Autowired mapCLIE mapClie;
    @Autowired mapCIER mapCier;

      
    @RequestMapping(value="/ajax/NEGO/getid/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> getid(HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (fnc.existsParameters(request, new String[]{"CO_NEGO"},respJson)){
                int c=fnc.getIntParameter(request,"CO_NEGO");            
                respJson.put("data",mapNego.getId(c));
                ajax.setTodoOk(respJson);
            }
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    }     
    
    @RequestMapping(value="/ajax/NEGO/select/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> select(@ModelAttribute("Pagination") Pagination pagination,@RequestParam(value="id",required = false) String id,@RequestParam(value="SEARCH",required = false) String SEARCH,HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (SEARCH==null){
                if (id==null){
                    String nombre="";
                    if (fnc.existsParameter(request, "NO_NEGO"))
                        nombre=fnc.getStringParameter(request, "NO_NEGO");
                    respJson.put("data", mapNego.select(nombre,pagination));
                    ajax.setNumResults(respJson, mapNego.getNumResultsSelect(nombre));
                }else{
                    respJson.put("data",mapNego.getId(Integer.parseInt(id)));            
                }
            }else{
                ajax.setData(respJson, mapNego.selectBySEARCH(SEARCH,pagination));
                ajax.setNumResults(respJson, mapNego.getNumResultsBySEARCH(SEARCH));                
            }
            ajax.setTodoOk(respJson);
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    }
    
    
    @RequestMapping(value="/ajax/NEGO/insert/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> insert(@ModelAttribute("NEGO") NEGO reg,@ModelAttribute("REFERENCIA") String referencia,@RequestParam(value="DE_CORRS", required=false) String[] DE_CORRS, HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (fnc.existsParameters(request, new String[]{"CO_NEGO","CO_SUCU_CORR","CO_CLIE","CO_PROD","CO_TIPO_FACT","CO_MONE_FACT","CO_PERI_FACT"},respJson)){
                Historial.initInsertar(request, reg);
                boolean existNumNegocio=mapNego.existNegocio(reg.getCO_NEGO_ForJSON());
                if (!existNumNegocio){
                    boolean existNumNegOrig=false;
                    boolean isDesacPorCambMoneda=false;
                    boolean isDesactivadoNegOrig=false;
                    if (reg.getCO_NEGO_ORIG_ForJSON()!=null){
                        existNumNegOrig=mapNego.existNegocio(reg.getCO_NEGO_ORIG_ForJSON());
                        isDesacPorCambMoneda=mapNego.existNegocioOrig(reg.getCO_NEGO_ORIG_ForJSON());
                        isDesactivadoNegOrig = mapNego.isDesactivado(reg.getCO_NEGO_ORIG_ForJSON());
                    }
                    
                    if ((isDesactivadoNegOrig && existNumNegOrig && isDesacPorCambMoneda) || reg.getCO_NEGO_ORIG_ForJSON()==null){
                        if (reg.getCO_TIPO_FACT_ForJSON()==1){
                            if (reg.getCO_PERI_FACT_ForJSON() == 1){
                                if(referencia!=null && referencia.length()>0){
                                    reg.setReferencia(referencia);
                                }else{
                                    reg.setReferencia("");
                                }
                                mapNego.insert(reg);
                                insertUpdateCorreos(reg, DE_CORRS, true);
                                /*if (DE_CORRS!=null){
                                    for(String DE_CORR : DE_CORRS){
                                        NEGO_CORR corr=new NEGO_CORR();
                                        corr.setCO_NEGO(reg.getCO_NEGO_ForJSON());
                                        corr.setDE_CORR(DE_CORR);
                                        corr.setST_ACTI(Boolean.TRUE);                                        
                                        mapNego_Corr.insert(corr);
                                    }
                                }*/
                                ajax.setTodoOk(respJson);
                            } else {
                                ajax.setError(respJson, "Cuando la facturaci\u00f3n es de tipo Vencida, el periodo debe ser mensual.");
                            }

                        } else {
                            mapNego.insert(reg);
                            insertUpdateCorreos(reg, DE_CORRS, true);
                            /*if (DE_CORRS!=null){
                                for(String DE_CORR : DE_CORRS){
                                    NEGO_CORR corr=new NEGO_CORR();
                                    corr.setCO_NEGO(reg.getCO_NEGO_ForJSON());
                                    corr.setDE_CORR(DE_CORR);
                                    corr.setST_ACTI(Boolean.TRUE);
                                    mapNego_Corr.insert(corr);
                                }
                            }*/
                            ajax.setTodoOk(respJson);
                        }
                    }else{
                        if (!existNumNegOrig){
                            ajax.setError(respJson, "El n\u00famero de negocio origen no existe.");
                        }else if (!isDesactivadoNegOrig){
                            ajax.setError(respJson, "El n\u00famero de negocio origen no est\u00e1 desactivado. Tiene que estar desactivado.");
                        }else if (!isDesacPorCambMoneda){
                            ajax.setError(respJson, "El n\u00famero de negocio origen no est\u00e1 desactivado por CAMBIO DE MONEDA.");
                        }
                    }
                    
                } else {
                    ajax.setError(respJson, "El n\u00famero de negocio ya existe.");
                }   
            }
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    } 
    
    @RequestMapping(value="/ajax/NEGO/update/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> update(@ModelAttribute("NEGO") NEGO reg,@ModelAttribute("REFERENCIA") String referencia, @RequestParam(value="DE_CORRS", required=false) String[] DE_CORRS, HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (fnc.existsParameters(request, new String[]{"CO_NEGO","CO_CLIE","CO_PROD","CO_TIPO_FACT","CO_MONE_FACT","CO_PERI_FACT"},respJson)){
                Historial.initModificar(request, reg);
                if(referencia!=null && referencia.length()>0){
                  reg.setReferencia(referencia);
                }else{
                  reg.setReferencia("");
                }
                mapNego.update(reg);
                insertUpdateCorreos(reg, DE_CORRS, false);
                ajax.setTodoOk(respJson);
            }
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    } 
    
    @RequestMapping(value="/ajax/NEGO/delete/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> delete(HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (fnc.existsParameters(request, new String[]{"CO_NEGO"},respJson)){
                NEGO reg=mapNego.getId(fnc.getIntParameter(request, "CO_NEGO"));
                if (reg!=null){
                    mapNego.delete(reg);
                    ajax.setTodoOk(respJson);
                }
            }
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    }     
     

    @RequestMapping(value="/ajax/NEGO/createTokenPreviewPDF/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> createTokenPreviewPDF(HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (fnc.existsParameters(request, new String[]{"CO_NEGO"},respJson)){
                String c=fnc.getStringParameter(request, "CO_NEGO");
                String d=encrypt.md5("@CODIGO_"+c);
                permissions.setSessionVar(request, d,c);
                ajax.setData(respJson, d);
                ajax.setTodoOk(respJson);
            }
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    }
    
    @RequestMapping(value="/ajax/NEGO/previewPDF/",method = {RequestMethod.GET})
    public void previewPDF(HttpServletRequest request,HttpServletResponse response) throws Exception{
        if (fnc.existsParameters(request, new String[]{"tk"})){
            String tk=fnc.getStringParameter(request, "tk");
            String cs=(String)permissions.getSessionVar(request, tk);          
            if(cs!=null){
                Integer ci=Integer.parseInt(cs);
                NEGO nego=mapCier.getUnNegocioParaProcesoCierre(ci);   
                PROD prod=nego.getPROD();
                CIER cier=prod.getCierrePendiente();                
                ProcesoNegocio pn=new ProcesoNegocio(nego,prod,cier,true,false);
                pn.setLog_view(Boolean.TRUE);
                pn.facturar(nego);
                pn.savePdf(response);
                /*NEGO nego=mapNego.getId(ci);
                PROD prod=nego.getPROD();
                CIER cier=prod.getCierrePendiente();
                Logs.printMsg("Facturando Producto "+prod.getNO_PROD_ForJSON()+" PERIODO / A\u00d1O "+cier.getNU_PERI_ForJSON()+"/"+cier.getNU_ANO_ForJSON());
                NegocioFacturar nf=new NegocioFacturar(cier, prod, nego,request,true);
                nf.facturar();*/
                permissions.destroySessionVar(request,tk);
                //LstRecibo lst=nf.getLstRecibo();
                //nf.crearPdf(response);                
            }else{
                throw new Exception("No se puede realizar la operaci\u00f3n.");
            }
        }else{
            throw new Exception("Faltan parametros.");
        }
    }
    
    @RequestMapping(value="/ajax/NEGO/selectNegociosByCO_CLIE_Migrar/",method = {RequestMethod.POST})
    public @ResponseBody Map<String,Object> selectNegociosByCO_CLIE_Migrar(@RequestParam(value="CO_CLIE", required=true) Integer CO_CLIE,@RequestParam(value="CO_NEGO", required=true) Integer CO_NEGO,HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (CO_CLIE!=null){  
                List<NEGO> listaNegocios= mapClie.selectNegociosByCO_CLIE_Migrar(CO_CLIE,CO_NEGO);
                List<NEGO> listaNegociosOp = new ArrayList<NEGO>();
                for (NEGO nego:listaNegocios){
                    if (!nego.isDesactivado_ForJSON()){
                        listaNegociosOp.add(nego);
                    }
                }
                ajax.setData(respJson, listaNegociosOp);
                ajax.setTodoOk(respJson);
            }
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    }
    
    
    @RequestMapping(value="/ajax/NEGO/getResumen/",method = {RequestMethod.POST})
    public @ResponseBody Map<String,Object> getResumen(@RequestParam(value="CO_NEGO", required=true) Integer CO_NEGO,HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (CO_NEGO!=null){
                List<Map<String, Object>> lista=mapNego.getResumen(CO_NEGO);
                Map<String, Object> reg_total=new TreeMap<String, Object>();
                BigDecimal total=BigDecimal.ZERO;
                for(Map<String, Object> reg : lista){
                    total=total.add((BigDecimal)reg.get("MONT"));
                }
                reg_total.put("NOMB", "Total");
                reg_total.put("CANT", "");
                reg_total.put("MONT", total);
                lista.add(reg_total);
                ajax.setData(respJson,  lista);
                ajax.setTodoOk(respJson);
            }
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    }
    
    @RequestMapping(value="/ajax/NEGO/selectNegociosPendientes/",method = {RequestMethod.POST})
    public @ResponseBody Map<String,Object> selectNegociosPendientes(HttpServletRequest request){
        Map<String,Object> respJson=ajax.getResponseJSON();
        ajax.setData(respJson, mapNego.selectNegociosPendientes());  
        ajax.setTodoOk(respJson);
        ajax.setResponseJSON(respJson);        
        return respJson;
    }
    
    private void insertUpdateCorreos(NEGO reg, String[] DE_CORRS, boolean isNew) {
    	if (!isNew) {
    		mapNego_Corr.deleteLogic(reg.getCO_NEGO_ForJSON());
    	}
        if (DE_CORRS!=null){
            for(String DE_CORR : DE_CORRS){
                NEGO_CORR corr=new NEGO_CORR();
                corr.setCO_NEGO(reg.getCO_NEGO_ForJSON());
                corr.setDE_CORR(DE_CORR);
                corr.setST_ACTI(Boolean.TRUE);                                        
                mapNego_Corr.insert(corr);
            }
        }
    }
}
