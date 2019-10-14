/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.controllers;

import com.americatel.facturacion.fncs.ajax;
import com.americatel.facturacion.fncs.encrypt;
import com.americatel.facturacion.fncs.permissions;
import com.americatel.facturacion.mappers.mapCIER;
import com.americatel.facturacion.mappers.mapCURSOR;
import com.americatel.facturacion.models.CIER;
import com.americatel.facturacion.reportes.MaestroCargas;
import com.americatel.facturacion.reportes.MaestroCorrespondencia;
import com.americatel.facturacion.reportes.MaestroGerente;
import com.americatel.facturacion.reportes.ReporteAnulados;
import com.americatel.facturacion.reportes.ReporteDatosClientes;
import com.americatel.facturacion.reportes.ReporteGeneral;
import com.americatel.facturacion.reportes.ReporteGerenteSunat;
import com.americatel.facturacion.reportes.ReporteNotasCredito;

import java.util.List;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author crodas
 */
@Controller
public class Repo {
    @Autowired mapCIER mapCier;
    @Autowired mapCURSOR mapCursor;
    
   
    @RequestMapping(value="/ajax/REPO/cierre/gerentes",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> cierre_gerentes(@RequestParam("CO_PERI") Integer CO_PERI,@RequestParam("CO_ANO") Integer CO_ANO,@RequestParam("COD_PROD_PADRE") Integer COD_PROD_PADRE,@RequestParam("CO_PROD") Integer CO_PROD,HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){  
            if (CO_PERI!=null && CO_ANO!=null){
                String d=encrypt.md5("@REPO_GERENTES_"+(CO_ANO*100+CO_PERI));
                permissions.setSessionVar(request, d+"_ANO",CO_ANO);
                permissions.setSessionVar(request, d+"_PERIODO",CO_PERI);
                permissions.setSessionVar(request, d+"_PRODUCTO",COD_PROD_PADRE);
                permissions.setSessionVar(request, d+"_SERVICIO",CO_PROD);
                ajax.setData(respJson, d);
                ajax.setTodoOk(respJson);
            }else{
                ajax.setError(respJson, "Faltan parametros para ejecutar la funci\u00f3n.");
            }
        }
        ajax.setResponseJSON(respJson);
        return respJson;
    }
    
    @RequestMapping(value="/ajax/REPO/cierre/gerentes/token",method = {RequestMethod.GET})//,produces = "application/json"
    public void cierre_gerentes(@RequestParam("tk") String tk,HttpServletRequest request,HttpServletResponse response){//@RequestBody MODU advancedFormData           
        if(tk!=null){
        	System.out.println("ENTRO cierre_gerentes");
            Integer CO_ANO = (Integer) permissions.getSessionVar(request, tk+"_ANO");  
            Integer CO_PERI = (Integer) permissions.getSessionVar(request, tk+"_PERIODO");
            Integer COD_PROD_PADRE = (Integer) permissions.getSessionVar(request, tk+"_PRODUCTO");
            Integer COD_SERVICIO = (Integer) permissions.getSessionVar(request, tk+"_SERVICIO");
            
            if (CO_ANO!=null && CO_PERI!=null){
            	List<CIER> cierres;
            	if(COD_SERVICIO == 0 && COD_PROD_PADRE != null){
            		cierres=mapCier.getCierresByProducto(CO_ANO,CO_PERI,COD_PROD_PADRE);
            		System.out.println("cantidad CIERRE1 :" + cierres.size());
            	}else{
            		cierres=mapCier.getCierresByServicio(CO_ANO,CO_PERI,COD_SERVICIO);
            		System.out.println("cantidad CIERRE2 :" + cierres.size());
            	}
                //EVALUAMOS SEGUN LOS PARAMETROS SELECCIONADOS EL REPORTE QUE SE GENERARA                                
                MaestroGerente g=new MaestroGerente(cierres,COD_PROD_PADRE,COD_SERVICIO);
                
                g.save(response);                
                permissions.destroySessionVar(request,tk+"_ANO");
                permissions.destroySessionVar(request,tk+"_PERIODO");
                permissions.destroySessionVar(request,tk+"_PRODUCTO");
                permissions.destroySessionVar(request,tk+"_SERVICIO");
            }
        }
    }
    
    @RequestMapping(value="/ajax/REPO/cierre/anulados",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> cierre_anulados(@RequestParam("CO_PERI") Integer CO_PERI,@RequestParam("CO_ANO") Integer CO_ANO, @RequestParam("COD_PROD_PADRE") Integer COD_PROD_PADRE,@RequestParam("CO_PROD") Integer CO_PROD,HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){  
            if (CO_PERI!=null && CO_ANO!=null && COD_PROD_PADRE!=null){
                String d=encrypt.md5("@REPO_ANULADOS_"+(CO_ANO*100+CO_PERI));
                permissions.setSessionVar(request, d+"_ANO",CO_ANO);
                permissions.setSessionVar(request, d+"_PERIODO",CO_PERI);
                permissions.setSessionVar(request, d+"_PRODUCTO",COD_PROD_PADRE);
                permissions.setSessionVar(request, d+"_SERVICIO",CO_PROD);
                ajax.setData(respJson, d);
                ajax.setTodoOk(respJson);
            }else{
                ajax.setError(respJson, "Faltan parametros para ejecutar la funci\u00f3n.");
            }
        }
        ajax.setResponseJSON(respJson);
        return respJson;
    }
    
    @RequestMapping(value="/ajax/REPO/cierre/anulados/token",method = {RequestMethod.GET})//,produces = "application/json"
    public void cierre_anulados(@RequestParam("tk") String tk,HttpServletRequest request,HttpServletResponse response){//@RequestBody MODU advancedFormData           
        if(tk!=null){
            Integer CO_ANO = (Integer) permissions.getSessionVar(request, tk+"_ANO");  
            Integer CO_PERI = (Integer) permissions.getSessionVar(request, tk+"_PERIODO");
            Integer COD_PROD_PADRE = (Integer) permissions.getSessionVar(request, tk+"_PRODUCTO");
            Integer COD_SERVICIO = (Integer) permissions.getSessionVar(request, tk+"_SERVICIO");
                    
            if (CO_ANO!=null && CO_PERI!=null && COD_PROD_PADRE!=null && COD_SERVICIO!= null ){
            	List<CIER> cierres;
            	if(COD_SERVICIO == 0){
            		cierres=mapCier.getCierresByProducto(CO_ANO, CO_PERI, COD_PROD_PADRE);
            	}else{
            		cierres=mapCier.getReporteAnuladosByServicio(CO_ANO, CO_PERI, COD_SERVICIO);
            	}
                                                
                ReporteAnulados g=new ReporteAnulados(cierres,COD_PROD_PADRE);
                g.save(response);
                permissions.destroySessionVar(request,tk+"_ANO");
                permissions.destroySessionVar(request,tk+"_PERIODO");
                permissions.destroySessionVar(request,tk+"_PRODUCTO");
                permissions.destroySessionVar(request,tk+"_SERVICIO");
            }
        }
    }
    
    @RequestMapping(value="/ajax/REPO/cierre/correpondencia",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> cierre_correpondencia(@RequestParam("CO_PERI") Integer CO_PERI,@RequestParam("CO_ANO") Integer CO_ANO,@RequestParam("COD_PROD_PADRE") Integer COD_PROD_PADRE,@RequestParam("CO_PROD") Integer CO_PROD,HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){  
            if (CO_PERI!=null && CO_ANO!=null && COD_PROD_PADRE!=null && CO_PROD!=null){
                String d=encrypt.md5("@REPO_CORRESPONDENCIA_"+(CO_ANO*100+CO_PERI));
                permissions.setSessionVar(request, d+"_ANO",CO_ANO);
                permissions.setSessionVar(request, d+"_PERIODO",CO_PERI);
                permissions.setSessionVar(request, d+"_PRODUCTO",COD_PROD_PADRE);
                permissions.setSessionVar(request, d+"_SERVICIO",CO_PROD);
                ajax.setData(respJson, d);
                ajax.setTodoOk(respJson);
            }else{
                ajax.setError(respJson, "Faltan parametros para ejecutar la funci\u00f3n.");
            }
        }
        ajax.setResponseJSON(respJson);
        return respJson;
    }
    
    @RequestMapping(value="/ajax/REPO/cierre/correpondencia/token",method = {RequestMethod.GET})//,produces = "application/json"
    public void cierre_correpondencia(@RequestParam("tk") String tk,HttpServletRequest request,HttpServletResponse response){//@RequestBody MODU advancedFormData           
        if(tk!=null){
            Integer CO_ANO = (Integer) permissions.getSessionVar(request, tk+"_ANO");  
            Integer CO_PERI = (Integer) permissions.getSessionVar(request, tk+"_PERIODO");
            Integer COD_PROD_PADRE = (Integer) permissions.getSessionVar(request, tk+"_PRODUCTO");
            Integer COD_SERVICIO = (Integer) permissions.getSessionVar(request, tk+"_SERVICIO");
            
            if (CO_ANO!=null && CO_PERI!=null && COD_PROD_PADRE!=null && COD_SERVICIO!= null){
            	List<CIER> cierres;
            	if(COD_SERVICIO == 0){
            		cierres=mapCier.getCierresByProducto(CO_ANO, CO_PERI, COD_PROD_PADRE);
            	}else{
            		cierres=mapCier.getCierresByServicio(CO_ANO,CO_PERI,COD_SERVICIO);
            	}
                
                
                MaestroCorrespondencia g=new MaestroCorrespondencia(cierres);
                g.save(response);                
                permissions.destroySessionVar(request,tk+"_ANO");
                permissions.destroySessionVar(request,tk+"_PERIODO");
                permissions.destroySessionVar(request,tk+"_PRODUCTO");
                permissions.destroySessionVar(request,tk+"_SERVICIO");
            }
        }
    }
    
    
    @RequestMapping(value="/ajax/REPO/cierre/cargas",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> cierre_cargas(@RequestParam("CO_PERI") Integer CO_PERI,@RequestParam("CO_ANO") Integer CO_ANO,@RequestParam("COD_PROD_PADRE") Integer COD_PROD_PADRE,@RequestParam("CO_PROD") Integer CO_PROD,HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){  
            if (CO_PERI!=null && CO_ANO!=null && COD_PROD_PADRE != null && CO_PROD != null){
                String d=encrypt.md5("@REPO_CARGAS_"+(CO_ANO*100+CO_PERI));
                permissions.setSessionVar(request, d+"_ANO",CO_ANO);
                permissions.setSessionVar(request, d+"_PERIODO",CO_PERI);
                permissions.setSessionVar(request, d+"_PRODUCTO",COD_PROD_PADRE);
                permissions.setSessionVar(request, d+"_SERVICIO",CO_PROD);
                
                
                ajax.setData(respJson, d);
                ajax.setTodoOk(respJson);
            }else{
                ajax.setError(respJson, "Faltan parametros para ejecutar la funci\u00f3n.");
            }
        }
        ajax.setResponseJSON(respJson);
        return respJson;
    }
    
    @RequestMapping(value="/ajax/REPO/cierre/cargas/token",method = {RequestMethod.GET})//,produces = "application/json"
    public void cierre_cargas(@RequestParam("tk") String tk,HttpServletRequest request,HttpServletResponse response){//@RequestBody MODU advancedFormData           
        if(tk!=null){
            Integer CO_ANO = (Integer) permissions.getSessionVar(request, tk+"_ANO");  
            Integer CO_PERI = (Integer) permissions.getSessionVar(request, tk+"_PERIODO");
            Integer COD_PROD_PADRE = (Integer) permissions.getSessionVar(request, tk+"_PRODUCTO");
            Integer COD_SERVICIO = (Integer) permissions.getSessionVar(request, tk+"_SERVICIO");
            
            
            if (CO_ANO!=null && CO_PERI!=null && COD_PROD_PADRE != null && COD_SERVICIO!=null){
            	List<CIER> cierres;
            	if(COD_SERVICIO == 0){
            		cierres=mapCier.getCierresByProducto(CO_ANO, CO_PERI, COD_PROD_PADRE);
            	}else{
            		cierres=mapCier.getCierresByServicio(CO_ANO,CO_PERI,COD_SERVICIO);
            	}
                MaestroCargas g=new MaestroCargas(cierres,COD_PROD_PADRE,COD_SERVICIO);
                g.save(response);                
                permissions.destroySessionVar(request,tk+"_ANO");
                permissions.destroySessionVar(request,tk+"_PERIODO");
                permissions.destroySessionVar(request,tk+"_PRODUCTO");
                permissions.destroySessionVar(request,tk+"_SERVICIO");
            }
        }
    }
    
    @RequestMapping(value="/ajax/REPO/cierre/reporteNotasCredito",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> reporteNotasCredito(@RequestParam("CO_PERI") Integer CO_PERI,@RequestParam("CO_ANO") Integer CO_ANO,@RequestParam("COD_PROD_PADRE") Integer COD_PROD_PADRE,@RequestParam("CO_PROD") Integer CO_PROD,HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){  
            if (CO_PERI!=null && CO_ANO!=null  && COD_PROD_PADRE!=null && CO_PROD!=null){
                String d=encrypt.md5("@REPO_NOTAS_CREDITO_"+(CO_ANO*100+CO_PERI));
                permissions.setSessionVar(request, d+"_ANO",CO_ANO);
                permissions.setSessionVar(request, d+"_PERIODO",CO_PERI);
                permissions.setSessionVar(request, d+"_PRODUCTO",COD_PROD_PADRE);
                permissions.setSessionVar(request, d+"_SERVICIO",CO_PROD);
                
                ajax.setData(respJson, d);
                ajax.setTodoOk(respJson);
            }else{
                ajax.setError(respJson, "Faltan parametros para ejecutar la funci\u00f3n.");
            }
        }
        ajax.setResponseJSON(respJson);
        return respJson;
    }
    
    @RequestMapping(value="/ajax/REPO/cierre/reporteNotasCredito/token",method = {RequestMethod.GET})//,produces = "application/json"
    public void reporteNotasCredito(@RequestParam("tk") String tk,HttpServletRequest request,HttpServletResponse response){//@RequestBody MODU advancedFormData           
        if(tk!=null){
            Integer CO_ANO = (Integer) permissions.getSessionVar(request, tk+"_ANO");  
            Integer CO_PERI = (Integer) permissions.getSessionVar(request, tk+"_PERIODO");
            Integer COD_PROD_PADRE = (Integer) permissions.getSessionVar(request, tk+"_PRODUCTO");
            Integer COD_SERVICIO = (Integer) permissions.getSessionVar(request, tk+"_SERVICIO");
            if (CO_ANO!=null && CO_PERI!=null && COD_PROD_PADRE!=null && COD_SERVICIO!=null){
            	List<CIER> cierres;
            	if(COD_SERVICIO == 0){
            		cierres=mapCier.getCierresByProducto(CO_ANO, CO_PERI, COD_PROD_PADRE);
            	}else{
            		cierres=mapCier.getCierresByServicio(CO_ANO,CO_PERI,COD_SERVICIO);
            	}
                ReporteNotasCredito g=new ReporteNotasCredito(cierres,COD_PROD_PADRE,COD_SERVICIO);
                g.save(response);                
                permissions.destroySessionVar(request,tk+"_ANO");
                permissions.destroySessionVar(request,tk+"_PERIODO");
                permissions.destroySessionVar(request,tk+"_PRODUCTO");
                permissions.destroySessionVar(request,tk+"_SERVICIO");
            }
        }
    }
    
    @RequestMapping(value="/ajax/REPO/cierre/reporteDatosClientes",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> reporteDatosClientes(@RequestParam("COD_PROD_PADRE") Integer COD_PROD_PADRE,@RequestParam("CO_PROD") Integer CO_PROD,HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            
            if (COD_PROD_PADRE!=null && CO_PROD!=null){
                        
                double numAleatorio=Math.random()*100;
                String d=encrypt.md5("@REPO_DATOS_CLIENTES_"+numAleatorio);
                
                permissions.setSessionVar(request, d+"_PRODUCTO",COD_PROD_PADRE);
                permissions.setSessionVar(request, d+"_SERVICIO",CO_PROD);
                            
                ajax.setData(respJson, d);
                ajax.setTodoOk(respJson);
            }
        }
        ajax.setResponseJSON(respJson);
        return respJson;
    }
    
    @RequestMapping(value="/ajax/REPO/cierre/reporteDatosClientes/token",method = {RequestMethod.GET})//,produces = "application/json"
    public void reporteDatosClientes(@RequestParam("tk") String tk,HttpServletRequest request,HttpServletResponse response){//@RequestBody MODU advancedFormData           
        if(tk!=null){
            
            
            Integer COD_PROD_PADRE = (Integer) permissions.getSessionVar(request, tk+"_PRODUCTO");
            Integer COD_SERVICIO = (Integer) permissions.getSessionVar(request, tk+"_SERVICIO");
            
            if (COD_PROD_PADRE!=null && COD_SERVICIO!=null){
                 
                ReporteDatosClientes g=new ReporteDatosClientes(COD_PROD_PADRE,COD_SERVICIO);
                g.save(response);   
                                  
                permissions.destroySessionVar(request,tk+"_PRODUCTO");
                permissions.destroySessionVar(request,tk+"_SERVICIO");                 
            }                                    
        }
    }
    
    @RequestMapping(value="/ajax/REPO/cierre/general",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> cierre_general(@RequestParam("COD_PROD_PADRE") Integer COD_PROD_PADRE,@RequestParam("CO_PROD") Integer CO_PROD,HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){  
            
            if (COD_PROD_PADRE!=null && CO_PROD!=null){
                  
                String d=encrypt.md5("@REPO_GENERAL_");
                permissions.setSessionVar(request, d,true);
                
                
                permissions.setSessionVar(request, d+"_PRODUCTO",COD_PROD_PADRE);
                permissions.setSessionVar(request, d+"_SERVICIO",CO_PROD);
                
                ajax.setData(respJson, d);
                ajax.setTodoOk(respJson);
            }            
        }
        ajax.setResponseJSON(respJson);
        return respJson;
    }
    
    @RequestMapping(value="/ajax/REPO/cierre/general/token",method = {RequestMethod.GET})//,produces = "application/json"
    public void cierre_general(@RequestParam("tk") String tk,HttpServletRequest request,HttpServletResponse response){//@RequestBody MODU advancedFormData           
        if(tk!=null){
            
            Integer COD_PROD_PADRE = (Integer) permissions.getSessionVar(request, tk+"_PRODUCTO");
            Integer COD_SERVICIO = (Integer) permissions.getSessionVar(request, tk+"_SERVICIO");
            
            if (COD_PROD_PADRE!=null && COD_SERVICIO!=null){
                
                //ReporteGeneral g=new ReporteGeneral(mapCursor.callSelect("EXEC SP_REPORTE_GENERAL_Q01(#{"+COD_SERVICIO+"})"));
                ReporteGeneral g=new ReporteGeneral(mapCursor.callSelect("EXEC SP_REPORTE_GENERAL_Q01()"),COD_PROD_PADRE,COD_SERVICIO);
                g.save(response);       
                
                permissions.destroySessionVar(request,tk);
                
                
                permissions.destroySessionVar(request,tk+"_PRODUCTO");
                permissions.destroySessionVar(request,tk+"_SERVICIO");    
                
            }                        
        }
    }
    
    
    @RequestMapping(value="/ajax/REPO/cierre/gerenteSunat",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> cierre_gerenteSunat(@RequestParam("CO_PERI") Integer CO_PERI,
                                                                @RequestParam("CO_ANO") Integer CO_ANO,
                                                                @RequestParam("COD_PROD_PADRE") Integer COD_PROD_PADRE, 
                                                                @RequestParam("COD_TIPO_DOC") Integer COD_TIPO_DOC, 
                                                                HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){  
            if (COD_TIPO_DOC!=null && COD_PROD_PADRE!=null && CO_ANO!=null && CO_PERI!=null){
                String d=encrypt.md5("@REPO_GERENTE"+(Math.random()*100));
                
                permissions.setSessionVar(request, d+"_ANO",CO_ANO);
                permissions.setSessionVar(request, d+"_PERIODO",CO_PERI);
                permissions.setSessionVar(request, d+"_PRODUCTO",COD_PROD_PADRE);
                permissions.setSessionVar(request, d+"_TIPO_DOCUMENTO",COD_TIPO_DOC);
                ajax.setData(respJson, d);
                ajax.setTodoOk(respJson);
            }else{
                ajax.setError(respJson, "Faltan parametros para ejecutar la funci\u00f3n.");
            }
        }
        ajax.setResponseJSON(respJson);
        return respJson;
    }
    
    @RequestMapping(value="/ajax/REPO/cierre/gerenteSunat/token",method = {RequestMethod.GET})//,produces = "application/json"
    public void cierre_gerenteSunat(@RequestParam("tk") String tk,HttpServletRequest request,HttpServletResponse response){//@RequestBody MODU advancedFormData           
        if(tk!=null){
            Integer COD_TIPO_DOC = (Integer) permissions.getSessionVar(request, tk+"_TIPO_DOCUMENTO");
            Integer COD_PROD_PADRE = (Integer) permissions.getSessionVar(request, tk+"_PRODUCTO");
            Integer ANO = (Integer) permissions.getSessionVar(request, tk+"_ANO");
            Integer PERIODO = (Integer) permissions.getSessionVar(request, tk+"_PERIODO");
            
            if (COD_TIPO_DOC!=null && COD_PROD_PADRE!=null && ANO!= null && PERIODO != null){
                
                List<CIER> cierres=mapCier.getCierres(ANO,PERIODO);                
                //EVALUAMOS SEGUN LOS PARAMETROS SELECCIONADOS EL REPORTE QUE SE GENERARA                                  
                ReporteGerenteSunat g=new ReporteGerenteSunat(cierres,COD_PROD_PADRE,ANO,PERIODO,COD_TIPO_DOC);               
                g.save(response);                                                

                permissions.destroySessionVar(request,tk+"_ANO");
                permissions.destroySessionVar(request,tk+"_PERIODO");
                permissions.destroySessionVar(request,tk+"_PRODUCTO");
                permissions.destroySessionVar(request,tk+"_TIPO_DOCUMENTO");
                
            }
        }
    }
    
}
