/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.controllers;

import com.americatel.facturacion.fncs.ExcelReader;
import com.americatel.facturacion.fncs.FechaHora;
import com.americatel.facturacion.fncs.OitUtils;
import com.americatel.facturacion.fncs.ajax;
import com.americatel.facturacion.fncs.fnc;
import com.americatel.facturacion.fncs.permissions;
import com.americatel.facturacion.historial.Historial;
import com.americatel.facturacion.mappers.mapCIER;
import com.americatel.facturacion.mappers.mapCIER_DATA_PLAN;
import com.americatel.facturacion.mappers.mapCIER_DATA_SERV_SUPL;
import com.americatel.facturacion.mappers.mapCLIE;
import com.americatel.facturacion.mappers.mapCORT;
import com.americatel.facturacion.mappers.mapMIGR_NEGO_SUCU;
import com.americatel.facturacion.mappers.mapNEGO;
import com.americatel.facturacion.mappers.mapNEGO_HISTORICO;
import com.americatel.facturacion.mappers.mapNEGO_SUCU;
import com.americatel.facturacion.mappers.mapNEGO_SUCU_HISTORICO;
import com.americatel.facturacion.mappers.mapNEGO_SUCU_PLAN;
import com.americatel.facturacion.mappers.mapNEGO_SUCU_PROM;
import com.americatel.facturacion.mappers.mapNEGO_SUCU_SERV_SUPL;
import com.americatel.facturacion.mappers.mapNEGO_SUCU_SERV_UNIC;
import com.americatel.facturacion.mappers.mapPLAN;
import com.americatel.facturacion.mappers.mapSERV_SUPL;
import com.americatel.facturacion.mappers.mapSERV_UNIC;
import com.americatel.facturacion.mappers.mapSUCU;
import com.americatel.facturacion.mappers.mapSUSP;
import com.americatel.facturacion.models.AJUS;
import com.americatel.facturacion.models.CIER;
import com.americatel.facturacion.models.CLIE;
import com.americatel.facturacion.models.CORT;
import com.americatel.facturacion.models.MIGR_NEGO_SUCU;
import com.americatel.facturacion.models.NEGO;
import com.americatel.facturacion.models.NEGO_HISTORICO;
import com.americatel.facturacion.models.NEGO_SUCU;
import com.americatel.facturacion.models.NEGO_SUCU_HISTORICO;
import com.americatel.facturacion.models.NEGO_SUCU_PLAN;
import com.americatel.facturacion.models.NEGO_SUCU_PROM;
import com.americatel.facturacion.models.NEGO_SUCU_SERV_SUPL;
import com.americatel.facturacion.models.NEGO_SUCU_SERV_UNIC;
import com.americatel.facturacion.models.PLAN;
import com.americatel.facturacion.models.PROD;
import com.americatel.facturacion.models.SERV_SUPL;
import com.americatel.facturacion.models.SERV_UNIC;
import com.americatel.facturacion.models.SUCU;
import com.americatel.facturacion.models.SUSP;
import com.americatel.facturacion.models.USUA;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;




/**
 *
 * @author crodas
 */
@Controller
public class Nego_Sucu {
    @Autowired mapNEGO mapNego;
    @Autowired mapCLIE mapClie;
    @Autowired mapSUCU mapSucu;
    @Autowired mapCIER mapCier;
    @Autowired mapSUSP mapSusp;
    @Autowired mapCORT mapCort;
    @Autowired mapPLAN mapPlan;
    @Autowired mapNEGO_SUCU mapNego_Sucu;
    @Autowired mapNEGO_SUCU_PLAN mapNego_Sucu_Plan;
    @Autowired mapMIGR_NEGO_SUCU mapMigr_Nego_Sucu;
    
    @Autowired mapNEGO_SUCU_PROM mapNego_Sucu_Prom;
    @Autowired mapNEGO_SUCU_SERV_SUPL mapNego_Sucu_Serv_Supl;
    @Autowired mapNEGO_SUCU_SERV_UNIC mapNego_Sucu_Serv_Unic;
    @Autowired mapSERV_SUPL mapServSupl;
    @Autowired mapSERV_UNIC mapServUnic; 
    @Autowired mapNEGO_HISTORICO mapNego_Historico;
    @Autowired mapNEGO_SUCU_HISTORICO mapNego_Sucu_Historico;
    
    @Autowired mapCIER_DATA_PLAN mapCier_Data_Plan;
    @Autowired mapCIER_DATA_SERV_SUPL mapCier_Data_Serv_Supl;
    
    @RequestMapping(value="/ajax/NEGO_SUCU/getid/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> getid(HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (fnc.existsParameters(request, new String[]{"CO_NEGO_SUCU"},respJson)){
                int c=fnc.getIntParameter(request,"CO_NEGO_SUCU");            
                respJson.put("data",mapNego_Sucu.getId(c));
                ajax.setTodoOk(respJson);
            }
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    }     
    
    @RequestMapping(value="/ajax/NEGO_SUCU/select/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> select(@RequestParam(value="id",required = false) String id,HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (id==null){
                String nombre="";
                if (fnc.existsParameter(request, "NO_NEGO_SUCU"))
                    nombre=fnc.getStringParameter(request, "NO_NEGO_SUCU");
                respJson.put("data", mapNego_Sucu.select(nombre));
            }else{
                respJson.put("data",mapNego_Sucu.getId(Integer.parseInt(id)));            
            }        
            ajax.setTodoOk(respJson);
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    }
    
    
    @RequestMapping(value="/ajax/NEGO_SUCU/insert/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> insert(@ModelAttribute NEGO_SUCU reg,@RequestParam("CO_PLAN") Integer CO_PLAN,@RequestParam(value="CO_SERV_SUPL",required = false) Integer[] CO_SERV_SUPL,@RequestParam(value="CO_SERV_UNIC",required = false) Integer[] CO_SERV_UNIC, HttpServletRequest request/*,BindingResult errors*/){//@RequestBody MODU advancedFormData
        //if (!errors.hasErrors()){
            Map<String,Object> respJson=ajax.getResponseJSON();
            if (ajax.hasPermission(request, respJson)){
                if (fnc.existsParameters(request, new String[]{"CO_NEGO","CO_SUCU","FE_INIC","ST_SOAR","CO_OIT_INST","CO_CIRC"},respJson)){
                    String msg=reg.isValid();
                    if (msg==null){  
                        if (reg.getCO_MOTI_DESC_ForJSON()==0)
                            reg.setCO_MOTI_DESC(null);
                        Historial.initInsertar(request, reg);
                        mapNego_Sucu.insert(reg);
                        PLAN plan=mapPlan.getId(CO_PLAN);                    
                        //getDE_PLAZ_CONT_ForJSON
                        NEGO_SUCU_PLAN nego_suco_plan=new NEGO_SUCU_PLAN();
                        nego_suco_plan.setCO_PLAN(plan.getCO_PLAN_ForJSON());
                        nego_suco_plan.setNO_PLAN(plan.getNO_PLAN_ForJSON());
                        nego_suco_plan.setFE_INIC(reg.getFE_INIC_ForJSON());
                        nego_suco_plan.setFE_FIN(reg.getFE_FIN_ForJSON());                    
                        nego_suco_plan.setIM_MONTO(plan.getIM_MONTO_ForJSON());
                        nego_suco_plan.setDE_VELO_BAJA(plan.getDE_VELO_BAJA_ForJSON());
                        nego_suco_plan.setDE_VELO_SUBI(plan.getDE_VELO_SUBI_ForJSON());
                        nego_suco_plan.setCO_NEGO_SUCU(reg.getCO_NEGO_SUCU_ForJSON());
                        nego_suco_plan.setST_SOAR_INST(reg.isST_SOAR_INST_ForJSON());
                        nego_suco_plan.setCO_OIT_INST(reg.getCO_OIT_INST_ForJSON());                        
                        
                        Historial.initInsertar(request, nego_suco_plan);
                        mapNego_Sucu_Plan.insert(nego_suco_plan);

                        if (CO_SERV_SUPL!=null){
                            for (int i=0;i<CO_SERV_SUPL.length;i++){
                                SERV_SUPL ser_spl=mapServSupl.getId(CO_SERV_SUPL[i]);
                                NEGO_SUCU_SERV_SUPL nego_suco_serv_supl=new NEGO_SUCU_SERV_SUPL();
                                nego_suco_serv_supl.setCO_NEGO_SUCU(reg.getCO_NEGO_SUCU_ForJSON());
                                nego_suco_serv_supl.setCO_SERV_SUPL(ser_spl.getCO_SERV_SUPL_ForJSON());
                                nego_suco_serv_supl.setFE_FIN(reg.getFE_FIN_ForJSON());
                                nego_suco_serv_supl.setFE_INIC(reg.getFE_INIC_ForJSON());
                                nego_suco_serv_supl.setIM_MONTO(ser_spl.getIM_MONTO_ForJSON());
                                nego_suco_serv_supl.setNO_SERV_SUPL(ser_spl.getNO_SERV_SUPL_ForJSON());                                
                                nego_suco_serv_supl.setST_AFEC_DETR(ser_spl.isST_AFEC_DETR_ForJSON());
                                nego_suco_serv_supl.setST_SOAR_INST(reg.isST_SOAR_INST_ForJSON());
                                nego_suco_serv_supl.setCO_OIT_INST(reg.getCO_OIT_INST_ForJSON());
                                Historial.initInsertar(request, nego_suco_serv_supl);
                                mapNego_Sucu_Serv_Supl.insert(nego_suco_serv_supl);
                            }
                        }
                        
                        if (CO_SERV_UNIC!=null){
                            for (int i=0;i<CO_SERV_UNIC.length;i++){
                                SERV_UNIC ser_unic=mapServUnic.getId(CO_SERV_UNIC[i]);
                                NEGO_SUCU_SERV_UNIC nego_sucu_uni=new NEGO_SUCU_SERV_UNIC();
                                nego_sucu_uni.setCO_NEGO_SUCU(reg.getCO_NEGO_SUCU_ForJSON());                                
                                nego_sucu_uni.setST_SOAR_INST(reg.isST_SOAR_INST_ForJSON());
                                nego_sucu_uni.setCO_OIT_INST(reg.getCO_OIT_INST_ForJSON());
                                nego_sucu_uni.setCO_SERV_UNIC(ser_unic.getCO_SERV_UNIC_ForJSON());
                                nego_sucu_uni.setIM_MONTO(ser_unic.getIM_MONTO_ForJSON());
                                nego_sucu_uni.setNO_SERV_UNIC(ser_unic.getNO_SERV_UNIC_ForJSON());
                                nego_sucu_uni.setST_AFEC_DETR(ser_unic.getST_AFEC_DETR_ForJSON());
                                Historial.initInsertar(request, nego_sucu_uni);
                                mapNego_Sucu_Serv_Unic.insert(nego_sucu_uni);
                            }
                        }

                        ajax.setTodoOk(respJson);
                    }else{
                        ajax.setMsg(respJson, msg);
                    }
                }
                ajax.setResponseJSON(respJson);
            }
            return respJson;
        //}
    } 
    
    @RequestMapping(value="/ajax/NEGO_SUCU/update/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> update(@ModelAttribute("NEGO_SUCU") NEGO_SUCU reg,HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (fnc.existsParameters(request, new String[]{"CO_NEGO_SUCU","CO_NEGO","CO_SUCU","FE_INIC","ST_SOAR","CO_OIT_INST","CO_CIRC","CO_MOTI_DESC","DE_INFO_DESC"},respJson)){                
                String msg=reg.isValid();
                if (msg==null){
                    Historial.initModificar(request, reg);
                    mapNego_Sucu.update(reg);
                    ajax.setTodoOk(respJson);
                }else{
                    ajax.setMsg(respJson, msg);
                }
            }
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    } 
    
    @RequestMapping(value="/ajax/NEGO_SUCU/delete/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> delete(HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (fnc.existsParameters(request, new String[]{"CO_NEGO_SUCU"},respJson)){
                NEGO_SUCU reg=mapNego_Sucu.getId(fnc.getIntParameter(request, "CO_NEGO_SUCU"));
                if (reg!=null){
                    mapNego_Sucu.delete(reg);
                    ajax.setTodoOk(respJson);
                }
            }
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    }     
 
    @RequestMapping(value="/ajax/NEGO_SUCU/selectByNego/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> selectByNego(HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (fnc.existsParameters(request, new String[]{"CO_NEGO"},respJson)){                
                respJson.put("data",mapNego_Sucu.selectByNego(fnc.getIntParameter(request, "CO_NEGO")));
                ajax.setTodoOk(respJson);
            }
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    }
    
    @RequestMapping(value="/ajax/NEGO_SUCU/selectActivosByNego/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> selectActivosByNego(HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (fnc.existsParameters(request, new String[]{"CO_NEGO"},respJson)){                
                respJson.put("data",mapNego_Sucu.selectActivosByNego(fnc.getIntParameter(request, "CO_NEGO")));
                ajax.setTodoOk(respJson);
            }
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    }
    
    @RequestMapping(value="/ajax/NEGO_SUCU/getByRECI/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> getByRECI(HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (fnc.existsParameters(request, new String[]{"CO_RECI"},respJson)){  
                Long CO_RECI=fnc.getLongParameter(request, "CO_RECI");
                respJson.put("data",mapNego_Sucu.getByRECI(CO_RECI));
                ajax.setTodoOk(respJson);
            }
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    } 
    
    @RequestMapping(value="/ajax/NEGO_SUCU/getByBOLE/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> getByBOLE(HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (fnc.existsParameters(request, new String[]{"CO_BOLE"},respJson)){  
                Long CO_BOLE=fnc.getLongParameter(request, "CO_BOLE");
                respJson.put("data",mapNego_Sucu.getByBOLE(CO_BOLE));
                ajax.setTodoOk(respJson);
            }
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    } 
    
    @RequestMapping(value="/ajax/NEGO_SUCU/desactivar/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> desactivar(@ModelAttribute("NEGO_SUCU") NEGO_SUCU reg,HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (fnc.existsParameters(request, new String[]{"CO_NEGO_SUCU","CO_OIT_DESI","ST_SOAR_DESI","ST_OIT_DESI","CO_MOTI_DESC","DE_INFO_DESC"},respJson)){  
                NEGO_SUCU nego_sucu=mapNego_Sucu.getId(reg.getCO_NEGO_SUCU_ForJSON());
                if (nego_sucu.getFE_FIN_ForJSON()==null){
                    nego_sucu.setCO_OIT_DESI(reg.getCO_OIT_DESI_ForJSON());
                    nego_sucu.setST_SOAR_DESI(reg.isST_SOAR_DESI_ForJSON());
                    nego_sucu.setFE_FIN(reg.getFE_FIN_ForJSON());
                    nego_sucu.setCO_MOTI_DESC(reg.getCO_MOTI_DESC_ForJSON());
                    nego_sucu.setDE_INFO_DESC(reg.getDE_INFO_DESC_ForJSON());
                    mapNego_Sucu.desactivar(nego_sucu);
                    
                    //Registrar en el historico del negocio
                    USUA usua=permissions.getUsuarioLogin(request);
                    
                    NEGO_SUCU_HISTORICO nego_sucu_historico=new NEGO_SUCU_HISTORICO();
                    nego_sucu_historico.setCO_NEGO(nego_sucu.getCO_NEGO_ForJSON());
                    nego_sucu_historico.setCO_NEGO_SUCU(nego_sucu.getCO_NEGO_SUCU_ForJSON());
                    nego_sucu_historico.setDE_INFORMACION("La sucursal se desactiv\u00f3");
                    nego_sucu_historico.setCO_USUA_REGI(usua.getCO_USUA_ForJSON());
                    nego_sucu_historico.setFH_REGI(new Date());
                    mapNego_Sucu_Historico.insert(nego_sucu_historico);
                    
                    ajax.setTodoOk(respJson);
                }
            }
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    }     

    @RequestMapping(value="/ajax/NEGO_SUCU/REAL_CESI_CONT/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> real_cesi_cont(@RequestParam(value="CO_NEGO", required=true) Integer CO_NEGO,@RequestParam(value="CO_CLIE", required=true) Integer CO_CLIE,@RequestParam(value="ARR_CO_NEGO_SUCU", required=true) Integer[] ARR_CO_NEGO_SUCU,@RequestParam(value="ARR_ST_SOAR_INST_NEW", required=true) Boolean[] ARR_ST_SOAR_INST_NEW,@RequestParam(value="ARR_CO_OIT_INST_NEW", required=true) Integer[] ARR_CO_OIT_INST_NEW,@RequestParam(value="ARR_ST_SOAR_DESI", required=true) Boolean[] ARR_ST_SOAR_DESI,@RequestParam(value="ARR_CO_OIT_DESI", required=true) Integer[] ARR_CO_OIT_DESI,@RequestParam(value="ARR_DE_OBSERV", required=false) String[] ARR_DE_OBSERV,@RequestParam(value="CO_CIER", required=true) Integer CO_CIER,HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (CO_NEGO!=null && CO_CLIE!=null &&  ARR_CO_NEGO_SUCU!=null && ARR_ST_SOAR_INST_NEW!=null && ARR_CO_OIT_INST_NEW!=null &&  ARR_ST_SOAR_DESI!=null && ARR_CO_OIT_DESI!=null){
                if (ARR_CO_NEGO_SUCU.length==ARR_CO_OIT_INST_NEW.length && ARR_CO_OIT_INST_NEW.length==ARR_ST_SOAR_INST_NEW.length && ARR_CO_OIT_INST_NEW.length==ARR_ST_SOAR_DESI.length && ARR_ST_SOAR_DESI.length==ARR_CO_OIT_DESI.length && ARR_ST_SOAR_INST_NEW.length>0){
                    NEGO nego=mapNego.getId(CO_NEGO);
                    if (nego==null){
                        CLIE clie=mapClie.getId(CO_CLIE);
                        if (clie!=null){
                            NEGO_SUCU nego_sucu=null;
                            Boolean correcto=true;
                            List<NEGO_SUCU> negos_sucus_mover=new ArrayList<NEGO_SUCU>();
                            List<SUCU> sucus_mover=new ArrayList<SUCU>();
                            List<Date> ultimas_facturaciones=new ArrayList<Date>();
                            Integer negocio_origen_anterior=null;
                            for (int i=0;i<ARR_CO_NEGO_SUCU.length && correcto;i++){
                                if (ARR_CO_NEGO_SUCU[i]!=null && ARR_CO_OIT_DESI[i]!=null && ARR_CO_OIT_INST_NEW[i]!=null && ARR_ST_SOAR_DESI[i]!=null && ARR_ST_SOAR_INST_NEW[i]!=null){
                                    nego_sucu=mapNego_Sucu.getByOit(ARR_ST_SOAR_INST_NEW[i],ARR_CO_OIT_INST_NEW[i]);
                                    if (nego_sucu==null){
                                        nego_sucu=mapNego_Sucu.getId(ARR_CO_NEGO_SUCU[i]);
                                        Date ultima_facturacion=nego_sucu.getUltimaFacturacion();
                                        ultimas_facturaciones.add(ultima_facturacion);
                                        if (nego_sucu!=null){
                                            SUCU sucu=nego_sucu.getSUCU();
                                            if (sucu!=null){                                            
                                                sucus_mover.add(sucu);
                                                if (negocio_origen_anterior==null || negocio_origen_anterior.equals(nego_sucu.getCO_NEGO_ForJSON())){
                                                    negocio_origen_anterior=nego_sucu.getCO_NEGO_ForJSON();
                                                    negos_sucus_mover.add(nego_sucu);
                                                }else{
                                                    correcto=false;
                                                    ajax.setError(respJson, "Las sucursales no pertenecen en mismo negocio.");
                                                }                                        
                                            }else{
                                                correcto=false;
                                                ajax.setError(respJson, "No existe sucursal en BD.");
                                            }
                                        }else{
                                            correcto=false;
                                            ajax.setError(respJson, "No existe sucursal.");
                                        }
                                    }else{
                                        correcto=false;
                                        ajax.setError(respJson, "Ya existe registrada la OIT "+(ARR_ST_SOAR_INST_NEW[i]?"SOARC":"")+" n\u00famero "+ARR_CO_OIT_INST_NEW[i]+" en otra sucursal.");
                                    }
                                    //NEGO_SUCU nego_sucu=mapNego_Sucu.getId(ARR_CO_NEGO_SUCU[i]);
                                }else{
                                    correcto=false;
                                    ajax.setError(respJson, "Existen parametros ingresados de manera incorrecta.");                                    
                                }
                            }
                            if (correcto && negos_sucus_mover.size()>0 && sucus_mover.size()>0 && sucus_mover.size()==negos_sucus_mover.size() && ARR_CO_NEGO_SUCU.length==negos_sucus_mover.size()){
                                NEGO nego_origen=mapNego.getId(negos_sucus_mover.get(0).getCO_NEGO_ForJSON());
                                PROD prod = new PROD();
                                prod.setCO_PROD(nego_origen.getCO_PROD_ForJSON());
                                CIER cier = mapCier.getCierrePendienteByProducto(prod);
                                Date fecha_activacion;

                                if (nego_origen.getCO_TIPO_FACT_ForJSON() == 1){ // Vencida
                                    fecha_activacion = FechaHora.addMonths(FechaHora.getDate(cier.getNU_ANO_ForJSON(),cier.getNU_PERI_ForJSON()-1, 1), -1);
                                } else { // Adelantada
                                    fecha_activacion = FechaHora.getDate(cier.getNU_ANO_ForJSON(),cier.getNU_PERI_ForJSON()-1, 1);
                                }
                                Date fecha_desactivacion=FechaHora.addDays(fecha_activacion, -1);
                                
                                NEGO nego_nuevo=new NEGO();
                                SUCU sucu=clie.getSucursalFiscal();
                                nego_nuevo.setCO_NEGO(CO_NEGO);
                                nego_nuevo.setCO_CLIE(CO_CLIE);
                                nego_nuevo.setCO_PROD(nego_origen.getCO_PROD_ForJSON());
                                nego_nuevo.setCO_TIPO_FACT(nego_origen.getCO_TIPO_FACT_ForJSON());
                                nego_nuevo.setCO_MONE_FACT(nego_origen.getCO_MONE_FACT_ForJSON());
                                nego_nuevo.setCO_PERI_FACT(nego_origen.getCO_PERI_FACT_ForJSON());

                                nego_nuevo.setCO_SUCU_CORR(sucu.getCO_SUCU_ForJSON());
                                Historial.initInsertar(request, nego_nuevo);

                                mapNego.insert(nego_nuevo);

                                List<CORT> cortes=nego_origen.getCortesPendientes();
                                for(CORT corte:cortes){
                                    corte.setCO_NEGO(nego_nuevo.getCO_NEGO_ForJSON());
                                    Historial.initInsertar(request, corte);
                                    mapCort.insert(corte);
                                }

                                int contador=0;
                                for(NEGO_SUCU nego_sucu_move : negos_sucus_mover){
                                    SUCU sucu_move=sucus_mover.get(contador);
                                    List<SUSP> suspensiones=nego_sucu_move.getSuspensionesPendientes();
                                    Date ultima_facturacion=ultimas_facturaciones.get(contador);

                                    if (sucu_move!=null && ultima_facturacion!=null){
                                        SUCU sucu_nuevo=mapSucu.getByClienteAndDireccionAndDistrito(CO_CLIE,sucu_move.getCO_DIST_ForJSON(),sucu_move.getDE_DIRE_ForJSON());
                                        if (sucu_nuevo==null){
                                            //si no existe sucursal se crea en el cliente del nuevo negocio
                                            sucu_nuevo=new SUCU();
                                            sucu_nuevo.setCO_CLIE(CO_CLIE);
                                            sucu_nuevo.setCO_DIST(sucu_move.getCO_DIST_ForJSON());
                                            sucu_nuevo.setDE_DIRE(sucu_move.getDE_DIRE_ForJSON());                                            
                                            Historial.initInsertar(request, sucu_nuevo);
                                            mapSucu.insert(sucu_nuevo);
                                        }
                                        NEGO_SUCU nego_sucu_nuevo=new NEGO_SUCU();
                                        nego_sucu_nuevo.setCO_NEGO(CO_NEGO); 
                                        nego_sucu_nuevo.setCO_NEGO_SUCU_CESI_CONT_PADR(nego_sucu_move.getCO_NEGO_SUCU_ForJSON());
                                        nego_sucu_nuevo.setCO_OIT_INST(ARR_CO_OIT_INST_NEW[contador]);
                                        nego_sucu_nuevo.setST_SOAR_INST(ARR_ST_SOAR_INST_NEW[contador]);
                                        nego_sucu_nuevo.setCO_SUCU(sucu_nuevo.getCO_SUCU_ForJSON());
                                        nego_sucu_nuevo.setDE_ORDE_SERV(nego_sucu_move.getDE_ORDE_SERV_ForJSON());
                                        nego_sucu_nuevo.setDE_PLAZ_CONT(nego_sucu_move.getDE_PLAZ_CONT_ForJSON());
                                        nego_sucu_nuevo.setFE_INIC(fecha_activacion);
                                        if (ARR_DE_OBSERV.length>0){
                                            nego_sucu_nuevo.setDE_OBSERV(ARR_DE_OBSERV[contador]);
                                        }

                                        Historial.initInsertar(request, nego_sucu_nuevo);
                                        mapNego_Sucu.insert(nego_sucu_nuevo);

                                        List<NEGO_SUCU_PLAN> lista_planes=nego_sucu_move.getPlanes();
                                        if (!lista_planes.isEmpty()){
                                            for (NEGO_SUCU_PLAN plan_move:lista_planes){
                                                NEGO_SUCU_PLAN plan_nuevo=new NEGO_SUCU_PLAN();
                                                plan_nuevo.setCO_NEGO_SUCU(nego_sucu_nuevo.getCO_NEGO_SUCU_ForJSON());
                                                plan_nuevo.setCO_OIT_INST(ARR_CO_OIT_INST_NEW[contador]);
                                                plan_nuevo.setST_SOAR_INST(ARR_ST_SOAR_INST_NEW[contador]);
                                                plan_nuevo.setCO_PLAN(plan_move.getCO_PLAN_ForJSON());
                                                plan_nuevo.setFE_INIC(plan_move.getFE_INIC_ForJSON());
                                                plan_nuevo.setFE_FIN(plan_move.getFE_FIN_ForJSON());
                                                plan_nuevo.setFE_ULTI_FACT(plan_move.getFE_ULTI_FACT_ForJSON()); 
                                                plan_nuevo.setFE_ULTI_FACT_SGTE(plan_move.getFE_ULTI_FACT_SGTE_ForJSON());
                                                plan_nuevo.setIM_MONTO(plan_move.getIM_MONTO_ForJSON());
                                                plan_nuevo.setNO_PLAN(plan_move.getNO_PLAN_ForJSON());
                                                plan_nuevo.setDE_VELO_BAJA(plan_move.getDE_VELO_BAJA_ForJSON());
                                                plan_nuevo.setDE_VELO_SUBI(plan_move.getDE_VELO_SUBI_ForJSON());
                                                plan_nuevo.setCO_NEGO_SUCU_PLAN_CESI_CONT_PADR(plan_move.getCO_NEGO_SUCU_PLAN_ForJSON());
                                                Historial.initInsertar(request, plan_nuevo);
                                                mapNego_Sucu_Plan.insert(plan_nuevo);

                                                plan_move.setCO_NEGO_SUCU_PLAN_CESI_CONT_HIJO(plan_nuevo.getCO_NEGO_SUCU_PLAN_ForJSON());
                                                Historial.initModificar(request, plan_move);
                                                mapNego_Sucu_Plan.updateCesionContractual(plan_move);
                                            }
                                        }

                                        List<NEGO_SUCU_SERV_SUPL> lista_serv_supl_move=nego_sucu_move.getServiciosSuplementarios();
                                        if (!lista_serv_supl_move.isEmpty()){
                                            for (NEGO_SUCU_SERV_SUPL serv_supl_move:lista_serv_supl_move){
                                                //Solo se pasan las que se encuentren activas
                                                if (serv_supl_move.getFE_FIN_ForJSON()==null){
                                                    if (serv_supl_move.getFE_ULTI_FACT_ForJSON()!=null){
                                                        NEGO_SUCU_SERV_SUPL ss_nuevo=new NEGO_SUCU_SERV_SUPL();
                                                        ss_nuevo.setCO_NEGO_SUCU(nego_sucu_nuevo.getCO_NEGO_SUCU_ForJSON());
                                                        ss_nuevo.setCO_OIT_INST(ARR_CO_OIT_INST_NEW[contador]);
                                                        ss_nuevo.setST_SOAR_INST(ARR_ST_SOAR_INST_NEW[contador]);
                                                        ss_nuevo.setCO_SERV_SUPL(serv_supl_move.getCO_SERV_SUPL_ForJSON());
                                                        ss_nuevo.setFE_INIC(serv_supl_move.getFE_INIC_ForJSON());
                                                        ss_nuevo.setFE_FIN(serv_supl_move.getFE_FIN_ForJSON());
                                                        ss_nuevo.setFE_ULTI_FACT(serv_supl_move.getFE_ULTI_FACT_ForJSON());
                                                        ss_nuevo.setFE_ULTI_FACT_SGTE(serv_supl_move.getFE_ULTI_FACT_SGTE_ForJSON());
                                                        ss_nuevo.setIM_MONTO(serv_supl_move.getIM_MONTO_ForJSON());
                                                        ss_nuevo.setST_AFEC_DETR(serv_supl_move.getST_AFEC_DETR_ForJSON());
                                                        ss_nuevo.setNO_SERV_SUPL(serv_supl_move.getNO_SERV_SUPL_ForJSON());
                                                        ss_nuevo.setCO_NEGO_SUCU_SERV_SUPL_CESI_CONT_PADR(serv_supl_move.getCO_NEGO_SUCU_SERV_SUPL_ForJSON());
                                                        Historial.initInsertar(request, ss_nuevo);
                                                        mapNego_Sucu_Serv_Supl.insert(ss_nuevo);

                                                        serv_supl_move.setCO_NEGO_SUCU_SERV_SUPL_CESI_CONT_HIJO(ss_nuevo.getCO_NEGO_SUCU_SERV_SUPL_ForJSON());
                                                        Historial.initModificar(request, serv_supl_move);
                                                        mapNego_Sucu_Serv_Supl.updateCesionContractual(serv_supl_move);

                                                    } else {
                                                        // Si el servicio suplementario no ha facturado, entonces se pasa todo el serv supl a la nueva sucursal
                                                        serv_supl_move.setCO_NEGO_SUCU(nego_sucu_nuevo.getCO_NEGO_SUCU_ForJSON());
                                                        Historial.initModificar(request, serv_supl_move);
                                                        mapNego_Sucu_Serv_Supl.cambiarSucursal(serv_supl_move);
                                                    }

                                                }
                                            }
                                        }

                                        List<NEGO_SUCU_PROM> lista_promociones=nego_sucu_move.getPromociones();
                                        if (!lista_promociones.isEmpty()){
                                            for (NEGO_SUCU_PROM nego_sucu_promo:lista_promociones){
                                                NEGO_SUCU_PROM sucu_promo_nuevo=new NEGO_SUCU_PROM();
                                                sucu_promo_nuevo.setCO_NEGO_SUCU(nego_sucu_nuevo.getCO_NEGO_SUCU_ForJSON());
                                                sucu_promo_nuevo.setDE_ANO_FIN(nego_sucu_promo.getDE_ANO_FIN_ForJSON());
                                                sucu_promo_nuevo.setDE_ANO_INIC(nego_sucu_promo.getDE_ANO_INIC_ForJSON());
                                                sucu_promo_nuevo.setDE_PERI_FIN(nego_sucu_promo.getDE_PERI_FIN_ForJSON());
                                                sucu_promo_nuevo.setDE_PERI_INIC(nego_sucu_promo.getDE_PERI_INIC_ForJSON());
                                                sucu_promo_nuevo.setDE_TIPO(nego_sucu_promo.getDE_TIPO_ForJSON());
                                                sucu_promo_nuevo.setIM_VALO(nego_sucu_promo.getIM_VALO_ForJSON());
                                                sucu_promo_nuevo.setST_PLAN(nego_sucu_promo.getST_PLAN_ForJSON());
                                                sucu_promo_nuevo.setST_SERV_SUPL(nego_sucu_promo.getST_SERV_SUPL_ForJSON());
                                                Historial.initInsertar(request, sucu_promo_nuevo);
                                                mapNego_Sucu_Prom.insert(sucu_promo_nuevo);
                                            }
                                        }

                                        List<NEGO_SUCU_SERV_UNIC> lista_serv_unic=nego_sucu_move.getServiciosUnicosPendientes(CO_CIER);
                                        if (!lista_serv_unic.isEmpty()){
                                            for (NEGO_SUCU_SERV_UNIC serv_unic:lista_serv_unic){
                                                /*
                                                NEGO_SUCU_SERV_UNIC serv_unic_nuevo=serv_unic;
                                                serv_unic_nuevo.setCO_NEGO_SUCU(nego_sucu_nuevo.getCO_NEGO_SUCU_ForJSON());
                                                serv_unic_nuevo.setCO_NEGO_SUCU_SERV_UNIC(null);
                                                Historial.initInsertar(request, serv_unic_nuevo);
                                                mapNego_Sucu_Serv_Unic.insert(serv_unic_nuevo);
                                                */
                                                //Servicios unicos que aun no se han cobrado, se pasan a la sucursal destino
                                                //sin dejar rastro en la sucursal origen
                                                serv_unic.setCO_NEGO_SUCU(nego_sucu_nuevo.getCO_NEGO_SUCU_ForJSON());
                                                Historial.initModificar(request, serv_unic);
                                                mapNego_Sucu_Serv_Unic.cambiarSucursal(serv_unic);
                                            }
                                        }

                                        //Se pasan las suspesiones que tiene la sucursal origen
                                        for (SUSP susp:suspensiones){
                                            susp.setCO_NEGO_SUCU(nego_sucu_nuevo.getCO_NEGO_SUCU_ForJSON());
                                            Historial.initInsertar(request, susp);
                                            mapSusp.insert(susp);
                                        }

                                        nego_sucu_move.desactivar(ARR_CO_OIT_DESI[contador],ARR_ST_SOAR_DESI[contador],fecha_desactivacion,request);
                                        nego_sucu_move.setCO_NEGO_SUCU_CESI_CONT_HIJO(nego_sucu_nuevo.getCO_NEGO_SUCU_ForJSON());
                                        Historial.initModificar(request, nego_sucu_move);
                                        mapNego_Sucu.updateCesionContractual(nego_sucu_move);

                                        //Registrar en el historico del negocio
                                        USUA usua=permissions.getUsuarioLogin(request);

                                        NEGO_HISTORICO nego_historico=new NEGO_HISTORICO();
                                        nego_historico.setCO_NEGO(nego_origen.getCO_NEGO_ForJSON());
                                        nego_historico.setDE_INFORMACION("Se realiz\u00f3 cesi\u00f3n contractual al negocio "+CO_NEGO);
                                        nego_historico.setCO_USUA_REGI(usua.getCO_USUA_ForJSON());
                                        nego_historico.setFH_REGI(new Date());
                                        mapNego_Historico.insert(nego_historico);
                                    } else if (ultima_facturacion==null){
                                        //Cesion Contractual para las sucursales que no han facturado antes
                                        nego_sucu_move.setCO_NEGO(CO_NEGO);
                                        Historial.initModificar(request, nego_sucu_move);
                                        mapNego_Sucu.updateCambiarNegocio(nego_sucu_move);
                                    }                                    
                                    contador++;
                                }

                                ajax.setTodoOk(respJson);
                                
                            }                            
                        }else{
                            ajax.setError(respJson, "Error no existe cliente.");
                        }
                    }else{
                        ajax.setError(respJson, "El negocio ya esta registrado.");
                    }
                }
            }
        }
        return respJson;
    }    
    
    @RequestMapping(value="/ajax/NEGO_SUCU/MOVER_NEGO_SUCU/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> MOVER_NEGO_SUCU(@RequestParam(value="CO_NEGO_SUCU", required=true) Integer CO_NEGO_SUCU,@RequestParam(value="CO_NEGO", required=true) Integer CO_NEGO,HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (CO_NEGO_SUCU!=null && CO_NEGO!=null){  
                NEGO nego_mover=mapNego.getId(CO_NEGO);
                if (nego_mover!=null){
                    NEGO_SUCU nego_sucu=mapNego_Sucu.getId(CO_NEGO_SUCU);
                    Integer co_nego_actual = nego_sucu.getCO_NEGO_ForJSON();
                    NEGO nego=mapNego.getId(nego_sucu.getCO_NEGO_ForJSON());
                    if (nego_sucu!=null && nego!=null){
                        if (!nego_mover.getCO_NEGO_ForJSON().equals(nego.getCO_NEGO_ForJSON())){
                            if (nego.getCO_CLIE_ForJSON().equals(nego_mover.getCO_CLIE_ForJSON())){
                                if(nego.getCO_PROD_ForJSON().equals(nego_mover.getCO_PROD_ForJSON())){
                                    if(nego.getCO_MONE_FACT_ForJSON().equals(nego_mover.getCO_MONE_FACT_ForJSON())){
                                        if(nego.getCO_PERI_FACT_ForJSON().equals(nego_mover.getCO_PERI_FACT_ForJSON())){                                            
                                            if(nego.getCO_TIPO_FACT_ForJSON().equals(nego_mover.getCO_TIPO_FACT_ForJSON())){  
                                                nego_sucu.setCO_NEGO(nego_mover.getCO_NEGO_ForJSON());
                                                Historial.initModificar(request, nego_sucu);
                                                mapNego_Sucu.updateCambiarNegocio(nego_sucu);
                                                
                                                //Historial de migracion
                                                USUA usua=permissions.getUsuarioLogin(request);
                                                
                                                MIGR_NEGO_SUCU migr_nego_sucu = new MIGR_NEGO_SUCU();
                                                migr_nego_sucu.setCO_NEGO_ORIG(co_nego_actual);
                                                migr_nego_sucu.setCO_NEGO_DEST(nego_mover.getCO_NEGO_ForJSON());
                                                migr_nego_sucu.setCO_NEGO_SUCU(CO_NEGO_SUCU);
                                                migr_nego_sucu.setCO_USUA_REGI(usua.getCO_USUA_ForJSON());
                                                migr_nego_sucu.setFH_REGI(new Date());
                                                mapMigr_Nego_Sucu.insert(migr_nego_sucu);
                                                
                                                //Registrar en el historico del negocio
                                                NEGO_SUCU_HISTORICO nego_sucu_historico=new NEGO_SUCU_HISTORICO();
                                                nego_sucu_historico.setCO_NEGO(co_nego_actual);
                                                nego_sucu_historico.setCO_NEGO_SUCU(CO_NEGO_SUCU);
                                                nego_sucu_historico.setDE_INFORMACION("La sucursal se migr\u00f3 del negocio "+co_nego_actual+" al negocio "+nego_mover.getCO_NEGO_ForJSON());
                                                nego_sucu_historico.setCO_USUA_REGI(usua.getCO_USUA_ForJSON());
                                                nego_sucu_historico.setFH_REGI(new Date());
                                                mapNego_Sucu_Historico.insert(nego_sucu_historico);
                                        
                                                ajax.setTodoOk(respJson);
                                            }else{
                                                ajax.setError(respJson, "No es el mismo tipo de facturaci\u00f3n.");
                                            }                                            
                                        }else{
                                            ajax.setError(respJson, "No es el mismo periodo de facturaci\u00f3n.");
                                        }                                        
                                    }else{
                                        ajax.setError(respJson, "No es la mismo moneda.");
                                    }                                    
                                }else{
                                    ajax.setError(respJson, "No es el mismo producto.");
                                }    
                            }
                        }else{
                            ajax.setError(respJson, "No se puede mover al mismo negocio.");
                        }
                    }
                }
            }
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    }     
    
    @RequestMapping(value="/ajax/NEGO_SUCU/MUDAR/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> MUDAR(@ModelAttribute("NEGO_SUCU") NEGO_SUCU reg,HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (reg.getCO_NEGO_SUCU_ForJSON()!=null && reg.getCO_SUCU_ForJSON()!=null){  
                Integer CO_NEGO_SUCU = reg.getCO_NEGO_SUCU_ForJSON();
                Integer CO_SUCU = reg.getCO_SUCU_ForJSON();
                SUCU sucu=mapSucu.getId(CO_SUCU);
                if (sucu!=null){
                    NEGO_SUCU nego_sucu_cambiar=mapNego_Sucu.getId(CO_NEGO_SUCU);                    
                    if (nego_sucu_cambiar!=null){
                        NEGO nego=nego_sucu_cambiar.getNEGO();
                        if (nego!=null){
                            if (nego.getCO_CLIE_ForJSON().equals(sucu.getCO_CLIE_ForJSON())){
                                if (!sucu.getCO_SUCU_ForJSON().equals(nego_sucu_cambiar.getCO_SUCU_ForJSON()) ){
                                    nego_sucu_cambiar.setCO_NEGO_SUCU(CO_NEGO_SUCU);
                                    
                                    //Nueva sucursal
                                    NEGO_SUCU nego_sucu=mapNego_Sucu.getByOit(reg.isST_SOAR_INST_ForJSON(),reg.getCO_OIT_INST_ForJSON());
                                    // Validar la oit de instalacion
                                    if (nego_sucu==null){
                                        NEGO_SUCU nego_sucu_nueva = new NEGO_SUCU();
                                        nego_sucu_nueva.setCO_SUCU(CO_SUCU);
                                        nego_sucu_nueva.setCO_NEGO(reg.getCO_NEGO_ForJSON());
                                        nego_sucu_nueva.setCO_CIRC(nego_sucu_cambiar.getCO_CIRC_ForJSON());
                                        nego_sucu_nueva.setDE_ORDE_SERV(nego_sucu_cambiar.getDE_ORDE_SERV_ForJSON());
                                        nego_sucu_nueva.setDE_PLAZ_CONT(nego_sucu_cambiar.getDE_PLAZ_CONT_ForJSON());
                                        nego_sucu_nueva.setCO_OIT_INST(reg.getCO_OIT_INST_ForJSON());
                                        nego_sucu_nueva.setST_SOAR_INST(reg.isST_SOAR_INST_ForJSON());
                                        nego_sucu_nueva.setFE_INIC(nego_sucu_cambiar.getFE_INIC_ForJSON());
                                        nego_sucu_nueva.setCO_NEGO_SUCU_MUDA_PADR(CO_NEGO_SUCU);
                                        Historial.initInsertar(request, nego_sucu_nueva);
                                        mapNego_Sucu.insert(nego_sucu_nueva);
                                        
                                        List<NEGO_SUCU_PLAN> planes = nego_sucu_cambiar.getPlanes();
                                        if (planes!=null){
                                            for (NEGO_SUCU_PLAN plan:planes){
                                                NEGO_SUCU_PLAN plan_nuevo=new NEGO_SUCU_PLAN();
                                                plan_nuevo.setCO_OIT_INST(CO_SUCU);
                                                plan_nuevo.setCO_NEGO_SUCU(nego_sucu_nueva.getCO_NEGO_SUCU_ForJSON());
                                                plan_nuevo.setCO_OIT_INST(plan.getCO_OIT_INST_ForJSON());
                                                plan_nuevo.setST_SOAR_INST(plan.getST_SOAR_INST_ForJSON());
                                                plan_nuevo.setCO_OIT_DESI(plan.getCO_OIT_DESI_ForJSON());
                                                plan_nuevo.setST_SOAR_DESI(plan.getST_SOAR_DESI_ForJSON());
                                                plan_nuevo.setCO_PLAN(plan.getCO_PLAN_ForJSON());
                                                plan_nuevo.setFE_INIC(plan.getFE_INIC_ForJSON());
                                                plan_nuevo.setFE_FIN(plan.getFE_FIN_ForJSON());
                                                plan_nuevo.setFE_ULTI_FACT(plan.getFE_ULTI_FACT_ForJSON()); 
                                                plan_nuevo.setFE_ULTI_FACT_SGTE(plan.getFE_ULTI_FACT_SGTE_ForJSON()); 
                                                plan_nuevo.setIM_MONTO(plan.getIM_MONTO_ForJSON());
                                                plan_nuevo.setNO_PLAN(plan.getNO_PLAN_ForJSON());
                                                plan_nuevo.setDE_VELO_BAJA(plan.getDE_VELO_BAJA_ForJSON());
                                                plan_nuevo.setDE_VELO_SUBI(plan.getDE_VELO_SUBI_ForJSON());
                                                plan_nuevo.setCO_NEGO_SUCU_PLAN_MUDA_PADR(plan.getCO_NEGO_SUCU_PLAN_ForJSON());
                                                Historial.initInsertar(request, plan_nuevo);
                                                mapNego_Sucu_Plan.insert(plan_nuevo);

                                                plan.setCO_NEGO_SUCU_PLAN_MUDA_HIJO(plan_nuevo.getCO_NEGO_SUCU_PLAN_ForJSON());
                                                Historial.initModificar(request, plan);
                                                mapNego_Sucu_Plan.updateMudanza(plan);
                                            }
                                        }

                                        List<NEGO_SUCU_SERV_SUPL> listaServiciosSup = nego_sucu_cambiar.getServiciosSuplementarios();
                                        if (listaServiciosSup != null){
                                            for (NEGO_SUCU_SERV_SUPL nego_suco_serv_supl:listaServiciosSup){
                                                if (nego_suco_serv_supl.getFE_ULTI_FACT_ForJSON()!=null){
                                                    NEGO_SUCU_SERV_SUPL ss_nuevo=new NEGO_SUCU_SERV_SUPL();
                                                    ss_nuevo.setCO_NEGO_SUCU(nego_sucu_nueva.getCO_NEGO_SUCU_ForJSON());
                                                    ss_nuevo.setCO_OIT_INST(nego_suco_serv_supl.getCO_OIT_INST_ForJSON());
                                                    ss_nuevo.setST_SOAR_INST(nego_suco_serv_supl.getST_SOAR_INST_ForJSON());
                                                    ss_nuevo.setCO_OIT_DESI(nego_suco_serv_supl.getCO_OIT_DESI_ForJSON());
                                                    ss_nuevo.setST_SOAR_DESI(nego_suco_serv_supl.getST_SOAR_DESI_ForJSON());
                                                    ss_nuevo.setCO_SERV_SUPL(nego_suco_serv_supl.getCO_SERV_SUPL_ForJSON());
                                                    ss_nuevo.setFE_INIC(nego_suco_serv_supl.getFE_INIC_ForJSON());
                                                    ss_nuevo.setFE_FIN(nego_suco_serv_supl.getFE_FIN_ForJSON());
                                                    ss_nuevo.setFE_ULTI_FACT(nego_suco_serv_supl.getFE_ULTI_FACT_ForJSON());
                                                    ss_nuevo.setFE_ULTI_FACT_SGTE(nego_suco_serv_supl.getFE_ULTI_FACT_SGTE_ForJSON());
                                                    ss_nuevo.setIM_MONTO(nego_suco_serv_supl.getIM_MONTO_ForJSON());
                                                    ss_nuevo.setST_AFEC_DETR(nego_suco_serv_supl.getST_AFEC_DETR_ForJSON());
                                                    ss_nuevo.setNO_SERV_SUPL(nego_suco_serv_supl.getNO_SERV_SUPL_ForJSON());
                                                    ss_nuevo.setCO_NEGO_SUCU_SERV_SUPL_MUDA_PADR(nego_suco_serv_supl.getCO_NEGO_SUCU_SERV_SUPL_ForJSON());
                                                    Historial.initInsertar(request, ss_nuevo);
                                                    mapNego_Sucu_Serv_Supl.insert(ss_nuevo);

                                                    nego_suco_serv_supl.setCO_NEGO_SUCU_SERV_SUPL_MUDA_HIJO(ss_nuevo.getCO_NEGO_SUCU_SERV_SUPL_ForJSON());
                                                    Historial.initModificar(request, nego_suco_serv_supl);
                                                    mapNego_Sucu_Serv_Supl.updateMudanza(nego_suco_serv_supl);
                                                } else {
                                                    // Si el servicio suplementario no ha facturado, entonces se pasa todo el serv supl a la nueva sucursal
                                                    nego_suco_serv_supl.setCO_NEGO_SUCU(nego_sucu_nueva.getCO_NEGO_SUCU_ForJSON());
                                                    Historial.initModificar(request, nego_suco_serv_supl);
                                                    mapNego_Sucu_Serv_Supl.cambiarSucursal(nego_suco_serv_supl);
                                                }

                                            }
                                        }

                                        List<NEGO_SUCU_PROM> lista_promociones=nego_sucu_cambiar.getPromociones();
                                        if (!lista_promociones.isEmpty()){
                                            for (NEGO_SUCU_PROM nego_sucu_promo:lista_promociones){
                                                NEGO_SUCU_PROM sucu_promo_nuevo=new NEGO_SUCU_PROM();
                                                sucu_promo_nuevo.setCO_NEGO_SUCU(nego_sucu_nueva.getCO_NEGO_SUCU_ForJSON());
                                                sucu_promo_nuevo.setDE_ANO_FIN(nego_sucu_promo.getDE_ANO_FIN_ForJSON());
                                                sucu_promo_nuevo.setDE_ANO_INIC(nego_sucu_promo.getDE_ANO_INIC_ForJSON());
                                                sucu_promo_nuevo.setDE_PERI_FIN(nego_sucu_promo.getDE_PERI_FIN_ForJSON());
                                                sucu_promo_nuevo.setDE_PERI_INIC(nego_sucu_promo.getDE_PERI_INIC_ForJSON());
                                                sucu_promo_nuevo.setDE_TIPO(nego_sucu_promo.getDE_TIPO_ForJSON());
                                                sucu_promo_nuevo.setIM_VALO(nego_sucu_promo.getIM_VALO_ForJSON());
                                                sucu_promo_nuevo.setST_PLAN(nego_sucu_promo.getST_PLAN_ForJSON());
                                                sucu_promo_nuevo.setST_SERV_SUPL(nego_sucu_promo.getST_SERV_SUPL_ForJSON());
                                                Historial.initInsertar(request, sucu_promo_nuevo);
                                                mapNego_Sucu_Prom.insert(sucu_promo_nuevo);
                                            }
                                        }

                                        PROD prod = new PROD();
                                        prod.setCO_PROD(nego.getCO_PROD_ForJSON());
                                        CIER cier = mapCier.getCierrePendienteByProducto(prod);
                                        
                                         List<NEGO_SUCU_SERV_UNIC> lista_serv_unic=null;
                                        
                                        if(cier != null){
                                            
                                          lista_serv_unic=nego_sucu_cambiar.getServiciosUnicosPendientes(cier.getCO_CIER_ForJSON());                                           
                                        }else{ 
                                                    
                                          lista_serv_unic=nego_sucu_cambiar.getServiciosUnicosPendientes(0);                                           
                                        }                                        
                                        
                                        if (!lista_serv_unic.isEmpty()){
                                            for (NEGO_SUCU_SERV_UNIC serv_unic:lista_serv_unic){
                                                serv_unic.setCO_NEGO_SUCU(nego_sucu_nueva.getCO_NEGO_SUCU_ForJSON());
                                                Historial.initModificar(request, serv_unic);
                                                mapNego_Sucu_Serv_Unic.cambiarSucursal(serv_unic);
                                            }
                                        }

                                        //Se pasan las suspesiones que tiene la sucursal origen
                                        List<SUSP> suspensiones=nego_sucu_cambiar.getSuspensionesPendientes();
                                        for (SUSP susp:suspensiones){
                                            susp.setCO_NEGO_SUCU(nego_sucu_nueva.getCO_NEGO_SUCU_ForJSON());
                                            Historial.initInsertar(request, susp);
                                            mapSusp.insert(susp);
                                        }

                                        Date fecha_activacion;
                                        
                                         Calendar cal= Calendar.getInstance(); 
                                         int year= cal.get(Calendar.YEAR);    
                                         int mes= cal.get(Calendar.MONTH);  
                                        
                                        if (nego.getCO_TIPO_FACT_ForJSON() == 1){ // Vencida
                                            if(cier != null){
                                                fecha_activacion = FechaHora.addMonths(FechaHora.getDate(year,mes-1, 1), -1);
                                            }else{
                                                fecha_activacion = FechaHora.addMonths(FechaHora.getDate(year,mes-1, 1), -1);
                                            }
                                            
                                        } else { // Adelantada
                                            if(cier != null){
                                                fecha_activacion = FechaHora.getDate(year,mes-1, 1);
                                            }else{
                                                fecha_activacion = FechaHora.getDate(year,mes-1, 1);
                                            }
                                            
                                        }
                                        Date fecha_desactivacion=FechaHora.addDays(fecha_activacion, -1);
                                        // Sucursal a desactivar
                                        nego_sucu_cambiar.setCO_OIT_DESI(reg.getCO_OIT_DESI_ForJSON());
                                        nego_sucu_cambiar.setST_SOAR_DESI(reg.isST_SOAR_DESI_ForJSON());
                                        nego_sucu_cambiar.setFE_FIN(fecha_desactivacion);
                                        nego_sucu_cambiar.setCO_MOTI_DESC(4); // MOTIVO: 4 MUDANZA
                                        nego_sucu_cambiar.setCO_NEGO_SUCU_MUDA_HIJO(nego_sucu_nueva.getCO_NEGO_SUCU_ForJSON());
                                        Historial.initModificar(request, nego_sucu_cambiar);
                                        mapNego_Sucu.updateMudanza(nego_sucu_cambiar);
                                        mapNego_Sucu.desactivar(nego_sucu_cambiar);
    //                                    mapNego_Sucu.mudar(nego_sucu_cambiar);

                                        //Registrar en el historico del negocio
                                        String fecha_inst = new SimpleDateFormat("dd/MM/yyyy").format(reg.getFE_INIC_ForJSON());
                                        String fecha_desinst = new SimpleDateFormat("dd/MM/yyyy").format(reg.getFE_FIN_ForJSON());
                                        USUA usua=permissions.getUsuarioLogin(request);
                                        NEGO_SUCU_HISTORICO nego_sucu_historico=new NEGO_SUCU_HISTORICO();
                                        nego_sucu_historico.setCO_NEGO(reg.getCO_NEGO_ForJSON());
                                        nego_sucu_historico.setCO_NEGO_SUCU(CO_NEGO_SUCU);
                                        nego_sucu_historico.setDE_INFORMACION("La sucursal realiz\u00f3 una mudanza. La fecha de instalaci\u00f3n: "+fecha_inst+" y la fecha de desinstalaci\u00f3n: "+fecha_desinst);
                                        nego_sucu_historico.setCO_USUA_REGI(usua.getCO_USUA_ForJSON());
                                        nego_sucu_historico.setFH_REGI(new Date());
                                        mapNego_Sucu_Historico.insert(nego_sucu_historico);

                                        ajax.setTodoOk(respJson);
                                    } else {
                                        ajax.setMsg(respJson, "La OIT "+reg.getCO_OIT_INST_ForJSON()+" ya existe en otra sucursal.");
                                    }
                                }else{
                                    ajax.setMsg(respJson, "Error la sucursal no puede ser la misma.");
                                }
                            }else{
                                ajax.setMsg(respJson, "Error cliente no valido.");
                            }
                        }else{
                            ajax.setMsg(respJson, "Error negocio no valido.");
                        }
                    }else{
                        ajax.setMsg(respJson, "Error sucursal no valida.");
                    }
                }else{
                    ajax.setMsg(respJson, "Error sucursal no valida.");
                }
            }
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    }
    
    @RequestMapping(value="/ajax/NEGO_SUCU/upload/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> upload(@RequestParam("FL_SUCU") MultipartFile file, @RequestParam(value="CO_MOTI_DESC", required=true) Integer CO_MOTI_DESC,HttpServletRequest request,@RequestParam(value="COD_PROD_PADRE", required=true) Integer COD_PROD_PADRE){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if(!file.isEmpty()){
            ExcelReader excel=new ExcelReader(file);
            if (excel.selectSheet(0)){
                int iniRow=2;
                int numRows=excel.getNumRows();
                Integer CO_NEGO;
                Integer CO_OIT_INST=null;
                Integer CO_OIT_DESI=null;
                Date FE_FIN=null;
                Boolean ST_SOAR_DESI = null;
                Boolean ST_SOAR_INST = null;
                String oit_inst = null;
                String oit_desi = null;
                Object info_oit_inst[]=null;//oit, st_soarc 
                Object info_oit_desi[]=null;//oit, st_soarc 
                List<String> mensajes=new ArrayList<String>();
                Map<Integer,NEGO_SUCU> sucursales=new TreeMap<Integer,NEGO_SUCU>();
                NEGO nego=null;
//                NEGO_SUCU nego_sucu=null;
                Integer sucursales_cargadas=0;
                for(;iniRow<=numRows+1;iniRow++){
                    
                    CO_NEGO=excel.getValueInteger("A", iniRow);
                    oit_inst=excel.getValue("B", iniRow);
                    oit_desi=excel.getValue("C", iniRow);
                    FE_FIN=excel.getValueDate("D", iniRow);
                    
                    if (CO_NEGO!=null && CO_NEGO>0){
                        
                        nego=mapNego.getId(CO_NEGO);
                        if (nego!=null){
                                                                                 
                          if(nego.getProd_padre_ForJSON().getCO_PROD_PADRE_ForJSON() == COD_PROD_PADRE){ //CARGOS FIJOS - EMANUEL BARBARAN - EFITEC (SE AGREGO CAMPO)
                                                                                    
                            if (oit_inst!=null){
                                try {
                                    info_oit_inst=OitUtils.getInfo(oit_inst);//oit, st_soarc                                
                                } catch (Exception e) {
                                    mensajes.add("Fila "+iniRow+" no tiene una fecha activaci\u00f3n v\u00e1lida.");
                                }
                                CO_OIT_INST = (Integer) info_oit_inst[0];
                                ST_SOAR_INST = (Boolean) info_oit_inst[1];
                                
                                if (oit_desi!=null){
                                    try {
                                        info_oit_desi=OitUtils.getInfo(oit_desi);//oit, st_soarc                                
                                    } catch (Exception e) {
                                        mensajes.add("Fila "+iniRow+" no tiene una fecha desactivaci\u00f3n v\u00e1lida.");
                                    }
                                    CO_OIT_DESI = (Integer) info_oit_desi[0];
                                    ST_SOAR_DESI = (Boolean) info_oit_desi[1];
                                    
                                   if (FE_FIN!=null){
                                       NEGO_SUCU nego_sucu=new NEGO_SUCU();
                                       nego_sucu.setCO_NEGO(CO_NEGO);
                                       nego_sucu.setCO_OIT_INST(CO_OIT_INST);
                                       //nego_sucu.setST_SOAR_INST(ST_SOAR_INST);
                                       nego_sucu = mapNego_Sucu.getByOitAndNego(nego_sucu);
                                       //Verificar si ya esta desactivada
                                       if (nego_sucu != null){
                                           if (nego_sucu.getFE_FIN_ForJSON()==null){
                                                nego_sucu.setCO_OIT_DESI(CO_OIT_DESI);
                                                nego_sucu.setST_SOAR_DESI(ST_SOAR_DESI);
                                                nego_sucu.setFE_FIN(FE_FIN);
                                                nego_sucu.setCO_MOTI_DESC(CO_MOTI_DESC);
                                                mapNego_Sucu.desactivar(nego_sucu);
                                                sucursales.put(sucursales_cargadas, nego_sucu);
                                                sucursales_cargadas++;
                                           } else {
                                               mensajes.add("Fila "+iniRow+" ya est\u00e1 desactivada.");
                                           }
                                       } else {
                                           mensajes.add("Fila "+iniRow+" no se encuentra el negocio " +CO_NEGO+ " con la oit instalaci\u00f3n " +CO_OIT_INST+ ".");
                                       }
                                       
                                    } else {
                                        mensajes.add("Fila "+iniRow+" no tiene una fecha desactivaci\u00f3n v\u00e1lida.");
                                    } 
                                } else {
                                    mensajes.add("Fila "+iniRow+" no tiene una OIT desinstalaci\u00f3n v\u00e1lida.");
                                }
                            } else {
                                mensajes.add("Fila "+iniRow+" no tiene una OIT instalaci\u00f3n v\u00e1lida.");
                            }
                          } else {
                            mensajes.add("Fila "+iniRow+" el producto del negocio no coincide con la opci\u00f3n seleccionada"); //CARGOS FIJOS - EMANUEL BARBARAN - EFITEC (SE AGREGO CAMPO)
                          }                              
                        } else{
                            mensajes.add("Fila "+iniRow+" no tiene un negocio v\u00e1lido.");
                        }
                        
                    }else{
                        mensajes.add("Fila "+iniRow+" no tiene un negocio v\u00e1lido.");
                    }
                }
                ajax.setTodoOk(respJson);
                mensajes.add(0, "Se desactivaron "+sucursales_cargadas+" sucursales.");
                mensajes.add(1, "Se encontraron "+(mensajes.size()-1)+" filas con errores.");
                ajax.setData(respJson, mensajes);                
            }
        }
        return respJson;
    }
    
    @RequestMapping(value="/ajax/NEGO_SUCU/selectPlanes_ServSuplByNego/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> selectPlanes_ServSuplByNego(HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (fnc.existsParameters(request, new String[]{"CO_NEGO"},respJson)){                
                respJson.put("data",mapNego_Sucu.getPlanes_ServSuplByCoNego(fnc.getIntParameter(request, "CO_NEGO")));
                ajax.setTodoOk(respJson);
            }
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    }
    
    @RequestMapping(value="/ajax/NEGO_SUCU/CAMB_MONE/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> camb_mone(@RequestParam(value="CO_NEGO", required=true) Integer CO_NEGO,@RequestParam(value="CO_MONE_FACT", required=true) Integer CO_MONE_FACT,@RequestParam(value="ARR_CO_NEGO_SUCU_PLAN_SS", required=true) Integer[] ARR_CO_NEGO_SUCU_PLAN_SS,@RequestParam(value="ARR_CO_NEGO_SUCU", required=true) Integer[] ARR_CO_NEGO_SUCU,@RequestParam(value="ARR_CO_PLAN_SS", required=true) Integer[] ARR_CO_PLAN_SS,@RequestParam(value="ARR_TIPO", required=true) String[] ARR_TIPO,HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (CO_NEGO!=null && ARR_CO_NEGO_SUCU!=null && ARR_CO_PLAN_SS!=null && ARR_TIPO!=null){
                if (ARR_CO_NEGO_SUCU.length==ARR_CO_PLAN_SS.length && ARR_CO_NEGO_SUCU.length==ARR_TIPO.length && ARR_CO_NEGO_SUCU_PLAN_SS.length==ARR_TIPO.length && ARR_CO_NEGO_SUCU.length>0){
                    NEGO nego=mapNego.getId(CO_NEGO);
                    if (nego!=null){
                        //Actualizamos la moneda del negocio
                        nego.setCO_MONE_FACT(CO_MONE_FACT);
                        Historial.initModificar(request, nego);
                        mapNego.update(nego);
                        PROD prod = new PROD();
                        prod.setCO_PROD(nego.getCO_PROD_ForJSON());
                        CIER cier = mapCier.getCierrePendienteByProducto(prod);
                        
                        for (int i=0;i<ARR_CO_NEGO_SUCU.length;i++){
                            
                            if(ARR_TIPO[i].equals("P")){ //Planes
                                NEGO_SUCU_PLAN nego_sucu_plan = mapNego_Sucu_Plan.getId(ARR_CO_NEGO_SUCU_PLAN_SS[i]);
                                Date fecha_activacion=null;
                                if (nego.getCO_TIPO_FACT_ForJSON() == 1){//Vencida
                                    fecha_activacion = FechaHora.addMonths(FechaHora.getDate(cier.getNU_ANO_ForJSON(),cier.getNU_PERI_ForJSON()-1, 1), -1);
                                } else { // Adelantada
                                    fecha_activacion = FechaHora.getDate(cier.getNU_ANO_ForJSON(),cier.getNU_PERI_ForJSON()-1, 1);
                                }
                                
                                //Desactivar plan activo
                                nego_sucu_plan.setST_SOAR_DESI(nego_sucu_plan.getST_SOAR_INST_ForJSON());
                                nego_sucu_plan.setCO_OIT_DESI(nego_sucu_plan.getCO_OIT_INST_ForJSON());
                                nego_sucu_plan.setFE_FIN(FechaHora.addDays(fecha_activacion, -1));
                                Historial.initModificar(request, nego_sucu_plan);
                                mapNego_Sucu_Plan.desactivar(nego_sucu_plan);
                                
                                //Insertar nuevo plan
                                PLAN plan = mapPlan.getId(ARR_CO_PLAN_SS[i]);
                                NEGO_SUCU_PLAN nego_sucu_plan_nuevo = new NEGO_SUCU_PLAN();
                                nego_sucu_plan_nuevo.setCO_NEGO_SUCU(nego_sucu_plan.getCO_NEGO_SUCU_ForJSON());
                                nego_sucu_plan_nuevo.setCO_PLAN(plan.getCO_PLAN_ForJSON());
                                nego_sucu_plan_nuevo.setNO_PLAN(plan.getNO_PLAN_ForJSON());
                                nego_sucu_plan_nuevo.setDE_VELO_SUBI(plan.getDE_VELO_SUBI_ForJSON());
                                nego_sucu_plan_nuevo.setDE_VELO_BAJA(plan.getDE_VELO_BAJA_ForJSON());
                                nego_sucu_plan_nuevo.setIM_MONTO(plan.getIM_MONTO_ForJSON());
                                nego_sucu_plan_nuevo.setST_SOAR_INST(nego_sucu_plan.getST_SOAR_INST_ForJSON());
                                nego_sucu_plan_nuevo.setCO_OIT_INST(nego_sucu_plan.getCO_OIT_INST_ForJSON());
                                nego_sucu_plan_nuevo.setFE_INIC(fecha_activacion);
                                Historial.initInsertar(request, nego_sucu_plan_nuevo);
                                mapNego_Sucu_Plan.insert(nego_sucu_plan_nuevo);
                                
                            } else { //Servicios Suplementarios
                                NEGO_SUCU_SERV_SUPL nego_sucu_serv_supl = mapNego_Sucu_Serv_Supl.getId(ARR_CO_NEGO_SUCU_PLAN_SS[i]);
                                Date fecha_activacion=null;
                                if (nego.getCO_TIPO_FACT_ForJSON() == 1){//Vencida
                                    fecha_activacion = FechaHora.addMonths(FechaHora.getDate(cier.getNU_ANO_ForJSON(),cier.getNU_PERI_ForJSON()-1, 1), -1);
                                } else { // Adelantada
                                    fecha_activacion = FechaHora.getDate(cier.getNU_ANO_ForJSON(),cier.getNU_PERI_ForJSON()-1, 1);
                                }
                                
                                //Desactivar plan activo
                                nego_sucu_serv_supl.setST_SOAR_DESI(nego_sucu_serv_supl.getST_SOAR_INST_ForJSON());
                                nego_sucu_serv_supl.setCO_OIT_DESI(nego_sucu_serv_supl.getCO_OIT_INST_ForJSON());
                                nego_sucu_serv_supl.setFE_FIN(FechaHora.addDays(fecha_activacion, -1));
                                Historial.initModificar(request, nego_sucu_serv_supl);
                                mapNego_Sucu_Serv_Supl.desactivar(nego_sucu_serv_supl);
                                
                                //Insertar nuevo servicio suplementario
                                SERV_SUPL serv_supl = mapServSupl.getId(ARR_CO_PLAN_SS[i]);
                                NEGO_SUCU_SERV_SUPL nego_sucu_serv_supl_nuevo = new NEGO_SUCU_SERV_SUPL();
                                nego_sucu_serv_supl_nuevo.setCO_NEGO_SUCU(nego_sucu_serv_supl.getCO_NEGO_SUCU_ForJSON());
                                nego_sucu_serv_supl_nuevo.setCO_SERV_SUPL(serv_supl.getCO_SERV_SUPL_ForJSON());
                                nego_sucu_serv_supl_nuevo.setNO_SERV_SUPL(serv_supl.getNO_SERV_SUPL_ForJSON());
                                nego_sucu_serv_supl_nuevo.setIM_MONTO(serv_supl.getIM_MONTO_ForJSON());
                                nego_sucu_serv_supl_nuevo.setST_AFEC_DETR(nego_sucu_serv_supl.getST_AFEC_DETR_ForJSON());
                                nego_sucu_serv_supl_nuevo.setST_SOAR_INST(nego_sucu_serv_supl.getST_SOAR_INST_ForJSON());
                                nego_sucu_serv_supl_nuevo.setCO_OIT_INST(nego_sucu_serv_supl.getCO_OIT_INST_ForJSON());
                                nego_sucu_serv_supl_nuevo.setFE_INIC(fecha_activacion);
                                Historial.initInsertar(request, nego_sucu_serv_supl_nuevo);
                                mapNego_Sucu_Serv_Supl.insert(nego_sucu_serv_supl_nuevo);
                            }
                        }
                        
                        ajax.setTodoOk(respJson);
                    }else{
                        ajax.setError(respJson, "El negocio no existe.");
                    }
                }
            }
        }
        return respJson;
    }
    
    @RequestMapping(value="/ajax/NEGO_SUCU/ELIMINAR/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> ELIMINAR(@ModelAttribute("NEGO_SUCU") NEGO_SUCU reg,HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (reg.getCO_NEGO_SUCU_ForJSON()!=null && reg.getCO_SUCU_ForJSON()!=null){  
                boolean isFactibleEliminar = mapNego_Sucu_Plan.isTodosPlanesSinFacturar(reg.getCO_NEGO_SUCU_ForJSON());
                if (isFactibleEliminar){
                    isFactibleEliminar = mapNego_Sucu_Serv_Supl.isTodosServSuplSinFacturar(reg.getCO_NEGO_SUCU_ForJSON());
                    if (isFactibleEliminar){
                        NEGO_SUCU nego_sucu=mapNego_Sucu.getId(reg.getCO_NEGO_SUCU_ForJSON());
                        PROD prod=nego_sucu.getNego_ForJSON().getPROD();
                        CIER cier=mapCier.getCierrePendienteByProducto(prod);
                        isFactibleEliminar = mapNego_Sucu_Serv_Unic.isTodosServUnicSinFacturar(reg.getCO_NEGO_SUCU_ForJSON(),cier.getCO_CIER_ForJSON());
                        if (isFactibleEliminar){
                            Historial.initEliminar(request, reg);
                            mapNego_Sucu.deleteAllInclude(reg);
                            ajax.setTodoOk(respJson);
                        } else {
                            ajax.setError(respJson, "La sucursal tiene servicios unicos que ya han facturado. No es posible eliminar.");
                        }
                    } else {
                        ajax.setError(respJson, "La sucursal tiene servicios suplementarios que ya han facturado. No es posible eliminar.");
                    }
                } else {
                    ajax.setError(respJson, "La sucursal tiene planes que ya han facturado. No es posible eliminar.");
                }
                
            }
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    }
    
    @RequestMapping(value="/ajax/NEGO_SUCU/getByFACT/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> getByFACT(HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (fnc.existsParameters(request, new String[]{"CO_FACT"},respJson)){  
                Long CO_FACT=fnc.getLongParameter(request, "CO_FACT");
                respJson.put("data",mapNego_Sucu.getByFACT(CO_FACT));
                ajax.setTodoOk(respJson);
            }
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    } 
    
}
