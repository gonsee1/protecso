/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.controllers;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.americatel.facturacion.fncs.Pagination;
import com.americatel.facturacion.fncs.ajax;
import com.americatel.facturacion.fncs.encrypt;
import com.americatel.facturacion.fncs.fnc;
import com.americatel.facturacion.fncs.permissions;
import com.americatel.facturacion.historial.Historial;
import com.americatel.facturacion.mappers.mapBOLE;
import com.americatel.facturacion.mappers.mapCLIE;
import com.americatel.facturacion.mappers.mapDIST;
import com.americatel.facturacion.mappers.mapMONE_FACT;
import com.americatel.facturacion.mappers.mapNEGO_SUCU;
import com.americatel.facturacion.mappers.mapRECI_GLOS;
import com.americatel.facturacion.mappers.mapTIPO_DOCU;
import com.americatel.facturacion.models.BOLE;
import com.americatel.facturacion.models.CIER;
import com.americatel.facturacion.models.DIST;
import com.americatel.facturacion.models.RECI_GLOS;
import com.americatel.facturacion.models.TIPO_DOCU;

/**
 *
 * @author crodas
 */
@Controller
public class Bole {
    
    @Autowired mapBOLE mapBole;
    private mapRECI_GLOS mapReci_Glos;
    @Autowired
    public void setMapReci_Glos(mapRECI_GLOS mapReci_Glos) {
        this.mapReci_Glos = mapReci_Glos;
    }
    @Autowired mapMONE_FACT mapMone_Fact;
    @Autowired mapTIPO_DOCU mapTipo_Docu;
    @Autowired mapDIST mapDist;
    @Autowired mapCLIE mapClie;
    @Autowired mapNEGO_SUCU mapNego_Sucu;
   

    @RequestMapping(value="/ajax/BOLE/findBoleta/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> findBoleta(@ModelAttribute("Pagination") Pagination pagination,@RequestParam(value="SEARCH",required = false) String SEARCH,HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (SEARCH!=null)
                if (SEARCH.trim().length()==0)
                    SEARCH=null;
            ajax.setData(respJson, mapBole.selectBySEARCH(SEARCH,pagination));
            ajax.setNumResults(respJson, mapBole.getNumResultsBySEARCH(SEARCH));  
            
            ajax.setTodoOk(respJson);
        }
        ajax.setResponseJSON(respJson);
        return respJson;
    }
    
    @RequestMapping(value="/ajax/BOLE/update/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> update(@ModelAttribute("BOLE") BOLE paramBole,HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (fnc.existsParameters(request, new String[]{
                "CO_BOLE","DE_PERI","FE_EMIS","FE_VENC","DE_CODI_BUM","NO_CLIE","CO_DIST_FISC","DE_DIRE_FISC",
                "CO_TIPO_DOCU","DE_NUME_DOCU",
            },respJson)){
            //if (paramReci.getCO_RECI_ForJSON()!=null && paramReci.getCO_MONE_FACT_ForJSON()!=null && paramReci.getCO_TIPO_DOCU_ForJSON()!=null){
                BOLE reci=mapBole.getById(paramBole.getCO_BOLE_ForJSON());
                CIER cier=reci.getCierre();
                if (cier.getST_CIER_ForJSON()==2 || cier.getST_CIER_ForJSON()==3){//En cierre o termino cierre - SOLO AQUI SE PUEDEN EDITAR Recibos
                    reci.setNO_CLIE(paramBole.getNO_CLIE_ForJSON());
                    
                    reci.setDE_PERI(paramBole.getDE_PERI_ForJSON());
                    reci.setFE_EMIS(paramBole.getFE_EMIS_ForJSON());
                    reci.setFE_VENC(paramBole.getFE_VENC_ForJSON());
                    reci.setDE_CODI_BUM(paramBole.getDE_CODI_BUM_ForJSON());               
               
                    //obtener nombre tipo documento
                    TIPO_DOCU docu=mapTipo_Docu.getId(paramBole.getCO_TIPO_DOCU_ForJSON());
                    reci.setNO_TIPO_DOCU(docu.getNO_TIPO_DOCU_ForJSON());
                    reci.setDE_NUME_DOCU(paramBole.getDE_NUME_DOCU_ForJSON());
                    DIST dist;
                    //Distrito Fiscal
                    dist=mapDist.getId(paramBole.getCO_DIST_FISC_ForJSON());
                    reci.setCO_DIST_FISC(dist.getCO_DIST_ForJSON());
                    reci.setNO_DIST_FISC(dist.getNO_DIST_ForJSON());
                    reci.setDE_DIRE_FISC(paramBole.getDE_DIRE_FISC_ForJSON());
                    //Distrito Instalacion
                    /*
                    dist=mapDist.getId(paramReci.getCO_DIST_INST_ForJSON());
                    reci.setCO_DIST_INST(dist.getCO_DIST_ForJSON());
                    reci.setNO_DIST_INST(dist.getNO_DIST_ForJSON());
                    reci.setDE_DIRE_INST(paramReci.getDE_DIRE_INST_ForJSON());
                    */
                    Historial.initModificar(request, reci);
                    mapBole.update(reci);                
                    ajax.setTodoOk(respJson);
                }else{
                    ajax.setError(respJson, "No se puede editar el recibo si el ciclo ya esta cerrado.");
                }
            }
        }
        ajax.setResponseJSON(respJson);
        return respJson;
    } 
//    
//    @RequestMapping(value="/ajax/RECI/anular/",method = {RequestMethod.POST})//,produces = "application/json"
//    public @ResponseBody Map<String,Object> anular(@RequestParam(value = "CO_RECI") Long CO_RECI,HttpServletRequest request){//@RequestBody MODU advancedFormData
//        Map<String,Object> respJson=ajax.getResponseJSON();
//        if (ajax.hasPermission(request, respJson)){
//            if (CO_RECI!=null){
//                RECI reci=mapReci.getById(CO_RECI);
//                Historial.initModificar(request, reci);
//                String rpta=reci.anular();                
//                if (rpta==null){
//                    ajax.setTodoOk(respJson);  
//                }else{
//                    ajax.setError(respJson, rpta);
//                }
//            }
//        }
//        ajax.setResponseJSON(respJson);
//        return respJson;
//    }
//    
    @RequestMapping(value="/ajax/BOLE/createTokenGeneratePDF/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> createTokenGeneratePDF(@RequestParam(value = "CO_RECI") Long CO_RECI,HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (CO_RECI!=null){
//                RECI reci=mapReci.getById(CO_RECI);
                String c=CO_RECI.toString();
                String d=encrypt.md5("@CODIGO_"+c);
                permissions.setSessionVar(request, d,c);
                ajax.setData(respJson, d);
                ajax.setTodoOk(respJson);                
            }
        }
        ajax.setResponseJSON(respJson);
        return respJson;
    }    
   
    @RequestMapping(value="/ajax/BOLE/verBoletaModificadoPDF/",method = {RequestMethod.GET})//,produces = "application/json"
    public void verBoletaModificadoPDF(@RequestParam(value = "numero") Long numero,HttpServletRequest request,HttpServletResponse response)  throws Exception{//@RequestBody MODU advancedFormData
        BOLE bole=mapBole.getById(numero); 
        bole.generarBoletaModificadoPDF(response);
    }  
//    /*@RequestMapping(value="/ajax/RECI/generatePDF/",method = {RequestMethod.GET})//,produces = "application/json"
//    public void generatePDF(@RequestParam(value = "tk") String token,HttpServletRequest request,HttpServletResponse response)  throws Exception{//@RequestBody MODU advancedFormData
//        String cs=(String)permissions.getSessionVar(request, token);
//        if (cs!=null){
//            Long CO_RECI=Long.parseLong(cs);
//            RECI reci=mapReci.getById(CO_RECI);            
//            permissions.destroySessionVar(request,token);
//            reci.generarPDF(response);
//        }else{
//            throw new Exception("No se puede realizar la operacion.");
//        }
//    } */ 
//    
    @RequestMapping(value="/ajax/BOLE/addGlosa/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> addGlosa(
            @RequestParam(value = "CO_BOLE") Long CO_BOLE,
            @RequestParam(value = "CO_NEGO_SUCU") Integer CO_NEGO_SUCU,
            @RequestParam(value = "NO_GLOS") String NO_GLOS,
            @RequestParam(value = "IM_GLOS") Double IM_GLOS,
            @RequestParam(value = "CO_TIPO_GLOS") Integer CO_TIPO_GLOS,
            HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (CO_BOLE!=null){
                
                BOLE reci=mapBole.getById(CO_BOLE);
                CIER cier=reci.getCierre();
                if (cier.getST_CIER_ForJSON()==2 || cier.getST_CIER_ForJSON()==3){//En cierre o termino cierre - SOLO AQUI SE PUEDEN EDITAR Recibos
                    List<RECI_GLOS> glosas=mapReci_Glos.getByCoNegoSucu(CO_NEGO_SUCU);
                    DIST dist=mapDist.getOnlyId(glosas.get(0).getCO_DIST_ForJSON());
                    RECI_GLOS rg=new RECI_GLOS();                
                    rg.setCO_NEGO_SUCU(CO_NEGO_SUCU);
                    rg.setDE_DIRE_SUCU(glosas.get(0).getDE_DIRE_SUCU_ForJSON());
                    rg.setCO_DIST(dist.getCO_DIST_ForJSON());
                    rg.setNO_DIST(dist.getNO_DIST_ForJSON());
                    rg.setCO_BOLE(CO_BOLE);
                    rg.setIM_MONT(IM_GLOS);
                    rg.setNO_GLOS(NO_GLOS);
                    rg.setTI_GLOS(CO_TIPO_GLOS);
                    Historial.initInsertar(request, rg);
                    rg.insertar();
                    
                    //Registra la direccion de instalacion
                    RECI_GLOS rgf=mapReci_Glos.getAlgunaSucursalFacturableBole(CO_BOLE);
                    reci.setCO_DIST_INST(rgf.getCO_DIST_ForJSON());
                    reci.setNO_DIST_INST(rgf.getNO_DIST_ForJSON());
                    reci.setDE_DIRE_INST(rgf.getDE_DIRE_SUCU_ForJSON());
                    Historial.initModificar(request, reci);
                    mapBole.update(reci);  
                    //Recalcula los montos del recibo
                    reci.recalcular_totales();
                    Historial.initModificar(request, reci);
                    reci.updateMontos();
                    
                    ajax.setTodoOk(respJson);
                }
            }
        }
        ajax.setResponseJSON(respJson);
        return respJson;
    }    
//
//
    @RequestMapping(value="/ajax/BOLE/updateGlosa/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> updateGlosa(
            @RequestParam(value = "CO_RECI_GLOS") Integer CO_RECI_GLOS,
            @RequestParam(value = "CO_BOLE") Long CO_BOLE,
            @RequestParam(value = "DE_DIRE_SUCU",required = false) String DE_DIRE_SUCU,
            @RequestParam(value = "CO_DIST", required=false) Integer CO_DIST,
            @RequestParam(value = "NO_GLOS") String NO_GLOS,
            @RequestParam(value = "IM_GLOS") Double IM_GLOS,
            @RequestParam(value = "CO_TIPO_GLOS") Integer CO_TIPO_GLOS,
            HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (CO_BOLE!=null){
                BOLE reci=mapBole.getById(CO_BOLE);
                if (reci!=null){
                    CIER cier=reci.getCierre();
                    if (cier.getST_CIER_ForJSON()==2 || cier.getST_CIER_ForJSON()==3){//En cierre o termino cierre - SOLO AQUI SE PUEDEN EDITAR Recibos
                        if (CO_DIST==null){
                            RECI_GLOS glos=mapReci_Glos.getById(CO_RECI_GLOS);
                            glos.setIM_MONT(IM_GLOS);
                            glos.setNO_GLOS(NO_GLOS);
                            glos.setTI_GLOS(CO_TIPO_GLOS);
                            Historial.initModificar(request, glos);
                            glos.update();
                            
                            //Recalcula los montos del recibo
                            reci.recalcular_totales();
                            Historial.initModificar(request, reci);
                            reci.updateMontos();

                            ajax.setTodoOk(respJson);
                        } else {
                            RECI_GLOS glos=mapReci_Glos.getById(CO_RECI_GLOS);
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
                            RECI_GLOS rgf=mapReci_Glos.getAlgunaSucursalFacturableBole(CO_BOLE);
                            reci.setCO_DIST_INST(rgf.getCO_DIST_ForJSON());
                            reci.setNO_DIST_INST(rgf.getNO_DIST_ForJSON());
                            reci.setDE_DIRE_INST(rgf.getDE_DIRE_SUCU_ForJSON());
                            Historial.initModificar(request, reci);
                            mapBole.update(reci); 
                            
                            //Actualizar las direcciones de todas las glosas que pertenezcan a la misma sucursal
                            List<RECI_GLOS> glosas=mapReci_Glos.getByCoNegoSucu(glos.getCO_NEGO_SUCU_ForJSON());
                            for (RECI_GLOS g:glosas){
                                g.setDE_DIRE_SUCU(DE_DIRE_SUCU);
                                g.setCO_DIST(dist.getCO_DIST_ForJSON());
                                g.setNO_DIST(dist.getNO_DIST_ForJSON());
                                Historial.initModificar(request, g);
                                g.update();
                            }
                            //Recalcula los montos del recibo
                            reci.recalcular_totales();
                            Historial.initModificar(request, reci);
                            reci.updateMontos();

                            ajax.setTodoOk(respJson);
                        }
                    }
                }
            }
        }
        ajax.setResponseJSON(respJson);
        return respJson;
    }      
//    
    @RequestMapping(value="/ajax/BOLE/deleteGlosa/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> deleteGlosa(
            @RequestParam(value = "CO_RECI_GLOS") Integer CO_RECI_GLOS,
            @RequestParam(value = "CO_BOLE") Long CO_BOLE,
            HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (CO_BOLE!=null){
                BOLE reci=mapBole.getById(CO_BOLE);
                if (reci!=null){
                    CIER cier=reci.getCierre();
                    if (cier.getST_CIER_ForJSON()==2 || cier.getST_CIER_ForJSON()==3){//En cierre o termino cierre - SOLO AQUI SE PUEDEN EDITAR Recibos
                        RECI_GLOS glos=mapReci_Glos.getById(CO_RECI_GLOS);
                        if (glos.getCO_BOLE().equals(CO_BOLE)){
                            Historial.initEliminar(request, glos);
                            glos.delete();
                            
                            //Recalcula los montos del recibo
                            reci.recalcular_totales();
                            Historial.initModificar(request, reci);
                            reci.updateMontos();
                                
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
