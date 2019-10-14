/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.controllers;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.americatel.facturacion.Utils.Constante;
import com.americatel.facturacion.core3.Main;
import com.americatel.facturacion.fncs.ajax;
import com.americatel.facturacion.fncs.fnc;
import com.americatel.facturacion.mappers.mapCIER;
import com.americatel.facturacion.mappers.mapFACT;
import com.americatel.facturacion.mappers.mapNEGO;
import com.americatel.facturacion.mappers.mapPROD;
import com.americatel.facturacion.mappers.mapRECI;
import com.americatel.facturacion.models.CIER;
import com.americatel.facturacion.models.PROD;
import com.americatel.facturacion.models.PROD_PADRE;

/**
 *
 * @author crodas
 */
@Controller
public class Cier {
    @Autowired mapPROD mapProd;
    @Autowired mapCIER mapCier;
    @Autowired mapFACT mapFact;
    @Autowired mapRECI mapReci;
    @Autowired mapNEGO mapNego;
    
    private static Logger log = Logger.getLogger(Cier.class.getName());
    
    
    @RequestMapping(value="/ajax/CIER/lanzar/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> lanzar(@RequestParam("FE_EMIS") @DateTimeFormat(pattern=fnc.FORMAT_DATE_TIME_SEPARATOR) Date FE_EMIS,
                                                    @RequestParam("FE_VENC") @DateTimeFormat(pattern=fnc.FORMAT_DATE_TIME_SEPARATOR) Date FE_VENC,
                                                    @RequestParam("DE_RAIZ_RECI") Long DE_RAIZ_RECI,
                                                    @RequestParam("DE_RAIZ_FACT") Long DE_RAIZ_FACT,
                                                    @RequestParam("CO_PRO") Integer CO_PRO,
                                                    @RequestParam("NU_TIPO_CAMB") Double NU_TIPO_CAMB,
                                                    @RequestParam("DE_RAIZ_BOLE") Long DE_RAIZ_BOLE,
                                                    HttpServletRequest request){//@RequestBody MODU advancedFormData
        log.logp(Level.INFO,Cier.class.getName() , "lanzar", "Inicio del servicio de lanzar");
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            log.log(Level.INFO,"Se valida los permisos - OK");
            //boolean existenNegPend=mapNego.existenNegociosPendientes(); 
            /*VALIDA SI EXISTEN NEGOCIOS CON SUCURSALES Y COMO MINIMO CON UN PLAN, Y QUE NO ESTEN PENDIENTE DE MIGRACION; CORRESPONDIENTE AL
            PRODUCTO SELECCIONADO*/
            boolean existenNegPend=mapNego.existenNegociosPendientesByProducto(CO_PRO);
            
            if (!existenNegPend){
                String error="";
                int error2 = 0;
                /*INICIO - AVM - VALIDACION PARA LA EJECUCION UNICA DE CIERRE*/ 
                List<PROD> listaValidar=mapProd.getAllServices();
                List<PROD> lista=mapProd.getAllByProduc(CO_PRO);
                for (PROD prod : listaValidar){
                    /*OBTENER EL CIERRE (PENDIENTE, EN PROCESO O TERMINADO) SEGUN EL PRODUCTO*/
                	try{
                    CIER cier=prod.getCierrePendiente();
                    Integer st=cier.getST_CIER_ForJSON();

                    /*VALIDA QUE EL ESTADO DEL CIERRE SE ENCUENTRE EN ESTADO -EN PROCESO */
                    if(st==Constante.ESTADO_CIERRE_EN_PROCESO){
                      log.log(Level.INFO,Constante.errorCierreEjecucion);
                      error+=Constante.errorCierreEjecucion;
                      break;
                    }
                	}
                	catch(Exception e)
                	{
                		error2 =prod.getCO_PROD_ForJSON();
                	}
                }
                if (error.length()==0){
                    Map<String,Object> resultado = validarDisponibilidadParaCorrelativos(CO_PRO,DE_RAIZ_FACT,DE_RAIZ_RECI,DE_RAIZ_BOLE);
                    if(Constante.S_FALSE == resultado.get(Constante.RESULTADO)){
                        error += resultado.get(Constante.MENSAJE);
                    }
                }
                
                if (error.length()==0){                                                            
                    log.log(Level.INFO,"Errores :" + error.length());
                    //se añadio codigo de padre
                    Thread t=new Thread(new Main(CO_PRO,lista,FE_EMIS,FE_VENC,DE_RAIZ_RECI,DE_RAIZ_FACT,NU_TIPO_CAMB,DE_RAIZ_BOLE ));
                    t.start();
                    ajax.setTodoOk(respJson);
                }else{
                    ajax.setError(respJson, error);
                }
            } else {
                log.log(Level.INFO,Constante.errorNegociosPendiente);
                ajax.setError(respJson,Constante.errorNegociosPendiente );
            }
        }
        ajax.setResponseJSON(respJson);
        return respJson;
    }
    
    @RequestMapping(value="/ajax/CIER/cerrar/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> cerrar(@RequestParam("CO_PRO") Integer CO_PRO,HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){            
            
            //List<PROD> productos=mapProd.getAll();
            List<PROD> productos=mapProd.getAllByProduc(CO_PRO);                      
            
            String error="";
            for(PROD producto : productos){
                CIER cier=producto.getCierrePendiente();
                if (cier!=null){
                    if (cier.getST_CIER_ForJSON()==3){
                    }else{
                        error+="No se puede cerrar si es que no termina el cierre del producto "+producto.getNO_PROD_ForJSON()+".";
                    }
                }
            }
            if (error.length()==0){
                for(PROD producto : productos){
                    CIER cier=producto.getCierrePendiente();
                    cier.Cerrar(request);
                }
                ajax.setTodoOk(respJson);
            }else{
                ajax.setError(respJson, error);
            }
        }
        ajax.setResponseJSON(respJson);
        return respJson;
    }
    
    @RequestMapping(value="/ajax/CIER/selectCierrePendienteByNego/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> selectCierrePendienteByNego(HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (fnc.existsParameters(request, new String[]{"CO_PROD"},respJson)){
                PROD prod = new PROD();
                prod.setCO_PROD(fnc.getIntParameter(request, "CO_PROD"));//Prueba
                respJson.put("data",mapCier.getCierrePendienteByProducto(prod));
                ajax.setTodoOk(respJson);
            }
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    }
    
    @RequestMapping(value="/ajax/CIER/selectCierresCerrados/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> selectCierresCerrados(HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            /*
            List<CIER> listaRet=new ArrayList<CIER>();
            List<PROD> lista=mapProd.getAll();
            for(PROD p:lista){
                List<CIER> ciers=p.getCierresCerrados();                     
                listaRet.addAll(ciers);
            }        	
            ajax.setData(respJson, listaRet);
            */
        	System.out.println ("Busqueda Cierre");
            respJson.put("data",mapCier.selectCierresCerrados());
            ajax.setTodoOk(respJson);
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    }
    
    private Map<String,Object> validarDisponibilidadParaCorrelativos(Integer cod_prod_padre, Long semillaFacturaInicial,Long semillaReciboInicial,Long semillaBoletaInicial){
        Map<String,Object> response = new LinkedHashMap<String, Object>();
        response.put(Constante.RESULTADO, Constante.S_TRUE);
        StringBuilder mensaje = new StringBuilder();
        Map<String,CIER> lista = new LinkedHashMap<String, CIER>();
        lista.put(Constante.S_FACTURA,mapCier.validarDisponibilidadCorrelativoFact(cod_prod_padre, semillaFacturaInicial));        
        lista.put(Constante.S_RECIBO,mapCier.validarDisponibilidadCorrelativoReci(cod_prod_padre, semillaReciboInicial));
        lista.put(Constante.S_BOLETA,mapCier.validarDisponibilidadCorrelativoBole(cod_prod_padre, semillaBoletaInicial));
        PROD_PADRE pp = null;
        if(!lista.isEmpty()){
            
            for(Map.Entry<String,CIER> obj : lista.entrySet()){
                if(obj.getValue()!=null){
                    if(Constante.S_TRUE.equals(response.get(Constante.RESULTADO))){
                        response.put(Constante.RESULTADO, Constante.S_FALSE);
                        mensaje.append(Constante.MENSAJE_DISPONIBILIDAD_CORRELATIVO);
                    }
                    pp = mapProd.obtenerProductoPorServicio(obj.getValue().getCO_PROD_ForJSON());
                    
                    mensaje.append("Periodo de Cierre : ");
                    mensaje.append(obj.getValue().getNU_PERI_ForJSON());
                    mensaje.append("/");
                    mensaje.append(obj.getValue().getNU_ANO_ForJSON());
                    mensaje.append("</br>");
                    mensaje.append("Producto : ");
                    mensaje.append(pp.getDESC_PROD_PADRE_ForJSON());
                    mensaje.append("</br>");
                    mensaje.append("Conflico : semilla de ");
                    mensaje.append(obj.getKey());
                }
            }
            response.put(Constante.MENSAJE, mensaje.toString());
        }
        return response;
    }
    
}
