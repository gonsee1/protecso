/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.controllers;

import com.americatel.facturacion.cores.ETipoReciboGlosa;
import com.americatel.facturacion.cores.Main;
import com.americatel.facturacion.fncs.Pagination;
import com.americatel.facturacion.fncs.ajax;
import com.americatel.facturacion.fncs.encrypt;
import com.americatel.facturacion.fncs.fnc;
import com.americatel.facturacion.fncs.permissions;
import com.americatel.facturacion.historial.Historial;
import com.americatel.facturacion.mappers.mapCIER;
import com.americatel.facturacion.mappers.mapCLIE;
import com.americatel.facturacion.mappers.mapDIST;
import com.americatel.facturacion.mappers.mapFACT;
import com.americatel.facturacion.mappers.mapFACT_GLOS;
import com.americatel.facturacion.mappers.mapMONE_FACT;
import com.americatel.facturacion.mappers.mapNEGO_SUCU;
import com.americatel.facturacion.mappers.mapPROD;
import com.americatel.facturacion.mappers.mapRECI;
import com.americatel.facturacion.mappers.mapRECI_GLOS;
import com.americatel.facturacion.mappers.mapTIPO_DOCU;
import com.americatel.facturacion.models.CIER;
import com.americatel.facturacion.models.DIST;
import com.americatel.facturacion.models.FACT;
import com.americatel.facturacion.models.FACT_GLOS;
import com.americatel.facturacion.models.MONE_FACT;
import com.americatel.facturacion.models.NEGO_SUCU;
import com.americatel.facturacion.models.PROD;
import com.americatel.facturacion.models.RECI;
import com.americatel.facturacion.models.RECI_GLOS;
import com.americatel.facturacion.models.TIPO_DOCU;
import java.util.Date;
import java.util.List;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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
public class Fact {
    @Autowired mapRECI mapReci;
    @Autowired mapFACT mapFact;
    @Autowired mapRECI_GLOS mapReci_Glos;
    @Autowired mapFACT_GLOS mapfact_Glos;
    @Autowired mapMONE_FACT mapMone_Fact;
    @Autowired mapTIPO_DOCU mapTipo_Docu;
    @Autowired mapDIST mapDist;
    @Autowired mapCLIE mapClie;
    @Autowired mapNEGO_SUCU mapNego_Sucu;
   

    @RequestMapping(value="/ajax/FACT/findFactura/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> findRecibo(@ModelAttribute("Pagination") Pagination pagination,@RequestParam(value="SEARCH",required = false) String SEARCH,HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (SEARCH!=null)
                if (SEARCH.trim().length()==0)
                    SEARCH=null;
//            respJson.put("data",mapReci.selectBySEARCH(SEARCH,pagination));
            ajax.setData(respJson, mapFact.selectBySEARCH(SEARCH,pagination));
            ajax.setNumResults(respJson, mapFact.getNumResultsBySEARCH(SEARCH));  
            
            ajax.setTodoOk(respJson);
        }
        ajax.setResponseJSON(respJson);
        return respJson;
    }
    
    
    @RequestMapping(value="/ajax/FACT/update/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> update(@ModelAttribute("FACT") FACT paramReci,HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (fnc.existsParameters(request, new String[]{
                "CO_FACT","FE_EMIS","FE_VENC","DE_CODI_BUM","NO_CLIE","CO_DIST_FISC","DE_DIRE_FISC",
                "CO_TIPO_DOCU","DE_NUME_DOCU",
            },respJson)){
            //if (paramReci.getCO_RECI_ForJSON()!=null && paramReci.getCO_MONE_FACT_ForJSON()!=null && paramReci.getCO_TIPO_DOCU_ForJSON()!=null){
                FACT fact=mapFact.getById(paramReci.getCO_FACT_ForJSON());
                CIER cier=fact.getCierre();
                if (cier.getST_CIER_ForJSON()==2 || cier.getST_CIER_ForJSON()==3){//En cierre o termino cierre - SOLO AQUI SE PUEDEN EDITAR Recibos
                    fact.setNO_CLIE(paramReci.getNO_CLIE_ForJSON());
                    
                    //reci.setDE_PERI(paramReci.getDE_PERI_ForJSON());
                    fact.setFE_EMIS(paramReci.getFE_EMIS_ForJSON());
                    fact.setFE_VENC(paramReci.getFE_VENC_ForJSON());
                    fact.setDE_CODI_BUM(paramReci.getDE_CODI_BUM_ForJSON());               
               
                    //obtener nombre tipo documento
                    TIPO_DOCU docu=mapTipo_Docu.getId(paramReci.getCO_TIPO_DOCU_ForJSON());
                    fact.setNO_TIPO_DOCU(docu.getNO_TIPO_DOCU_ForJSON());
                    fact.setDE_NUME_DOCU(paramReci.getDE_NUME_DOCU_ForJSON());
                    DIST dist;
                    //Distrito Fiscal
                    dist=mapDist.getId(paramReci.getCO_DIST_FISC_ForJSON());
                    fact.setCO_DIST_FISC(dist.getCO_DIST_ForJSON());
                    fact.setNO_DIST_FISC(dist.getNO_DIST_ForJSON());
                    fact.setDE_DIRE_FISC(paramReci.getDE_DIRE_FISC_ForJSON());
                    Historial.initModificar(request, fact);
                    mapFact.update(fact);                
                    ajax.setTodoOk(respJson);
                }else{
                    ajax.setError(respJson, "No se puede editar la factura si el ciclo ya esta cerrado.");
                }
            }
        }
        ajax.setResponseJSON(respJson);
        return respJson;
    } 
    
    @RequestMapping(value="/ajax/FACT/anular/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> anular(@RequestParam(value = "CO_FACT") Long CO_FACT,HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (CO_FACT!=null){
                FACT fact=mapFact.getById(CO_FACT);
                //Historial.initModificar(request, fact);
                String rpta=fact.anular();                
                if (rpta==null){
                    ajax.setTodoOk(respJson);  
                }else{
                    ajax.setError(respJson, rpta);
                }
            }
        }
        ajax.setResponseJSON(respJson);
        return respJson;
    }
    
    @RequestMapping(value="/ajax/FACT/createTokenGeneratePDF/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> createTokenGeneratePDF(@RequestParam(value = "CO_FACT") Long CO_FACT,HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (CO_FACT!=null){
                FACT reci=mapFact.getById(CO_FACT);
                String c=CO_FACT.toString();
                String d=encrypt.md5("@CODIGO_"+c);
                permissions.setSessionVar(request, d,c);
                ajax.setData(respJson, d);
                ajax.setTodoOk(respJson);                
            }
        }
        ajax.setResponseJSON(respJson);
        return respJson;
    }       
   
    @RequestMapping(value="/ajax/FACT/verFacturaModificadoPDF/",method = {RequestMethod.GET})//,produces = "application/json"
    public void verReciboModificadoPDF(@RequestParam(value = "numero") Long numero,HttpServletRequest request,HttpServletResponse response)  throws Exception{//@RequestBody MODU advancedFormData
        FACT fact=mapFact.getById(numero); 
        fact.generarReciboModificadoPDF(response);
    }
    
    
    @RequestMapping(value="/ajax/FACT/addGlosa/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> addGlosa(
            @RequestParam(value = "CO_FACT") Long CO_FACT,
            @RequestParam(value = "CO_NEGO_SUCU") Integer CO_NEGO_SUCU,
            @RequestParam(value = "NO_GLOS") String NO_GLOS,
            @RequestParam(value = "IM_GLOS") Double IM_GLOS,
            @RequestParam(value = "CO_TIPO_GLOS") Integer CO_TIPO_GLOS,
            HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (CO_FACT!=null){
                //RECI reci=mapReci.getById(CO_FACT);
                FACT fact=mapFact.getById(CO_FACT);
//                        
                CIER cier=fact.getCierre();
                if (cier.getST_CIER_ForJSON()==2 || cier.getST_CIER_ForJSON()==3){//En cierre o termino cierre - SOLO AQUI SE PUEDEN EDITAR Recibos
                    List<FACT_GLOS> glosas=mapfact_Glos.getByCoNegoSucu(CO_NEGO_SUCU);
                    DIST dist=mapDist.getOnlyId(glosas.get(0).getCO_DIST_ForJSON());
//                    //RECI_GLOS rg=new RECI_GLOS(); 
                    FACT_GLOS fg=new FACT_GLOS();
//                                                            
                    fg.setCO_NEGO_SUCU(CO_NEGO_SUCU);
                    fg.setDE_DIRE_SUCU(glosas.get(0).getDE_DIRE_SUCU_ForJSON());
                    fg.setCO_DIST(dist.getCO_DIST_ForJSON());
                    fg.setNO_DIST(dist.getNO_DIST_ForJSON());
                    fg.setCO_FACT(CO_FACT);
                    fg.setIM_MONT(IM_GLOS);
                    fg.setNO_GLOS(NO_GLOS);
                    fg.setTI_GLOS(CO_TIPO_GLOS);
                    Historial.initInsertar(request, fg);                    
                    fg.insertar();
//                    //Registra la direccion de instalacion
                    FACT_GLOS rgf=mapfact_Glos.getAlgunaSucursalFacturable(CO_FACT);
                    fact.setCO_DIST_INST(rgf.getCO_DIST_ForJSON());
                    fact.setNO_DIST_INST(rgf.getNO_DIST_ForJSON());
                    fact.setDE_DIRE_INST(rgf.getDE_DIRE_SUCU_ForJSON());
                    Historial.initModificar(request, fact);
                    mapFact.update(fact);  
                    //Recalcula los montos del recibo
                    fact.recalcular_totales();
                    Historial.initModificar(request, fact);
                    fact.updateMontos();
//                    
                    ajax.setTodoOk(respJson);
                }
            }
        }
        ajax.setResponseJSON(respJson);
        return respJson;
    }    


    @RequestMapping(value="/ajax/FACT/updateGlosa/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> updateGlosa(
            @RequestParam(value = "CO_FACT_GLOS") Integer CO_FACT_GLOS,
            @RequestParam(value = "CO_FACT") Long CO_FACT,
            @RequestParam(value = "DE_DIRE_SUCU",required = false) String DE_DIRE_SUCU,
            @RequestParam(value = "CO_DIST", required=false) Integer CO_DIST,
            @RequestParam(value = "NO_GLOS") String NO_GLOS,
            @RequestParam(value = "IM_GLOS") Double IM_GLOS,
            @RequestParam(value = "CO_TIPO_GLOS") Integer CO_TIPO_GLOS,
            HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (CO_FACT!=null){
                //RECI reci=mapReci.getById(CO_RECI);
                FACT fact=mapFact.getById(CO_FACT);
                
                if (fact!=null){
                    CIER cier=fact.getCierre();
                    if (cier.getST_CIER_ForJSON()==2 || cier.getST_CIER_ForJSON()==3){//En cierre o termino cierre - SOLO AQUI SE PUEDEN EDITAR Recibos
                        if (CO_DIST==null){
                            FACT_GLOS glos=mapfact_Glos.getById(CO_FACT_GLOS);
                            glos.setIM_MONT(IM_GLOS);
                            glos.setNO_GLOS(NO_GLOS);
                            glos.setTI_GLOS(CO_TIPO_GLOS);
                            Historial.initModificar(request, glos);
                            glos.update();
                            
                            //Recalcula los montos del facturas
                            fact.recalcular_totales();
                            Historial.initModificar(request, fact);
                            fact.updateMontos();

                            ajax.setTodoOk(respJson);
                        } else {
                            FACT_GLOS glos=mapfact_Glos.getById(CO_FACT_GLOS);
                            DIST dist=mapDist.getOnlyId(CO_DIST);
                            glos.setDE_DIRE_SUCU(DE_DIRE_SUCU);
                            glos.setCO_DIST(dist.getCO_DIST_ForJSON());
                            glos.setNO_DIST(dist.getNO_DIST_ForJSON());
                            glos.setIM_MONT(IM_GLOS);
                            glos.setNO_GLOS(NO_GLOS);
                            glos.setTI_GLOS(CO_TIPO_GLOS);
                            Historial.initModificar(request, glos);
                            glos.update();
                            
                            //Registra la direccion de instalacion
                            FACT_GLOS rgf=mapfact_Glos.getAlgunaSucursalFacturable(CO_FACT);
                            fact.setCO_DIST_INST(rgf.getCO_DIST_ForJSON());
                            fact.setNO_DIST_INST(rgf.getNO_DIST_ForJSON());
                            fact.setDE_DIRE_INST(rgf.getDE_DIRE_SUCU_ForJSON());
                            Historial.initModificar(request, fact);
                            mapFact.update(fact); 
                            
                            //Actualizar las direcciones de todas las glosas que pertenezcan a la misma sucursal
                            List<FACT_GLOS> glosas=mapfact_Glos.getByCoNegoSucu(glos.getCO_NEGO_SUCU_ForJSON());
                            for (FACT_GLOS g:glosas){
                                g.setDE_DIRE_SUCU(DE_DIRE_SUCU);
                                g.setCO_DIST(dist.getCO_DIST_ForJSON());
                                g.setNO_DIST(dist.getNO_DIST_ForJSON());
                                Historial.initModificar(request, g);
                                g.update();
                            }
                            //Recalcula los montos del recibo
                            fact.recalcular_totales();
                            Historial.initModificar(request, fact);
                            fact.updateMontos();

                            ajax.setTodoOk(respJson);
                        }
                    }
                }
            }
        }
        ajax.setResponseJSON(respJson);
        return respJson;
    }      
    
    @RequestMapping(value="/ajax/FACT/deleteGlosa/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> deleteGlosa(
            @RequestParam(value = "CO_FACT_GLOS") Integer CO_FACT_GLOS,
            @RequestParam(value = "CO_FACT") Long CO_FACT,
            HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (CO_FACT!=null){
                FACT fact=mapFact.getById(CO_FACT);
                if (fact!=null){
                    CIER cier=fact.getCierre();
                    if (cier.getST_CIER_ForJSON()==2 || cier.getST_CIER_ForJSON()==3){//En cierre o termino cierre - SOLO AQUI SE PUEDEN EDITAR Recibos
                        FACT_GLOS glos=mapfact_Glos.getById(CO_FACT_GLOS);
                        if (glos.getCO_FACT_ForJSON().equals(CO_FACT)){
                            Historial.initEliminar(request, glos);
                            glos.delete();
                            
                            //Recalcula los montos del recibo
                            fact.recalcular_totales();
                            Historial.initModificar(request, fact);
                            fact.updateMontos();
                                
                            ajax.setTodoOk(respJson);
                        }
                    }
                }
            }
        }
        ajax.setResponseJSON(respJson);
        return respJson;
    }    
    
}
