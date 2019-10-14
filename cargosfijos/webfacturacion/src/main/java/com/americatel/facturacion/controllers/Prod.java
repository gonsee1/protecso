/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.controllers;

import com.americatel.facturacion.fncs.ajax;
import com.americatel.facturacion.fncs.CopyClass;
import com.americatel.facturacion.fncs.fnc;
import com.americatel.facturacion.historial.Historial;
import com.americatel.facturacion.mappers.mapPROD;
import com.americatel.facturacion.models.CIER;
import com.americatel.facturacion.models.CONT_CLIE;
import com.americatel.facturacion.models.DEPA;
import com.americatel.facturacion.models.MODU;
import com.americatel.facturacion.models.PROD;
import com.americatel.facturacion.models.PROD_PADRE;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.NotFoundException;

import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;




/**
 *
 * @author crodas
 */
@Controller
public class Prod {
    final static Logger logger = Logger.getLogger(Prod.class);
    @Autowired mapPROD mapProd;
    
    
   
    @RequestMapping(value="/ajax/PROD/getid/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> getid(HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (fnc.existsParameters(request, new String[]{"CO_PROD"},respJson)){
                int c=fnc.getIntParameter(request,"CO_PROD");            
                respJson.put("data",mapProd.getId(c));
                ajax.setTodoOk(respJson);
            }
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    }     
    
    @RequestMapping(value="/ajax/PROD/getServiceByIdCombo/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> getServiceByIdCombo(HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (fnc.existsParameters(request, new String[]{"CO_PROD"},respJson)){
                int c=fnc.getIntParameter(request,"CO_PROD");            
                respJson.put("data",mapProd.getServiceByIdCombo(c));
                ajax.setTodoOk(respJson);
            }
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    }     
    
    
    @RequestMapping(value="/ajax/PROD/select/",method = {RequestMethod.POST,RequestMethod.GET})//,produces = "application/json"
    public @ResponseBody Map<String,Object> select(@RequestParam(value="id",required = false) String id,HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (id==null){
                String nombre="";
                if (fnc.existsParameter(request, "NO_PROD"))
                    nombre=fnc.getStringParameter(request, "NO_PROD");
                respJson.put("data", mapProd.select(nombre));
            }else{
                respJson.put("data",mapProd.getId(Integer.parseInt(id)));            
            }        
            ajax.setTodoOk(respJson);
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    }
    
    
    @RequestMapping(value="/ajax/PROD/insert/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> insert(@ModelAttribute("PROD") PROD reg, HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (fnc.existsParameters(request, new String[]{"NO_PROD","CO_TIPO_FACT","CO_MONE_FACT","CO_PERI_FACT","COD_PROD_PADRE"},respJson)){
                Historial.initInsertar(request, reg);
                mapProd.insert(reg);               
                mapProd.insertRelacionProducto(Integer.parseInt(fnc.getStringParameter(request, "COD_PROD_PADRE")),reg.getCO_PROD_ForJSON());
                                 
                ajax.setTodoOk(respJson);
            }
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    } 
    
    @RequestMapping(value="/ajax/PROD/update/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> update(@ModelAttribute("PROD") PROD reg,HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (fnc.existsParameters(request, new String[]{"CO_PROD","NO_PROD","CO_TIPO_FACT","CO_MONE_FACT","CO_PERI_FACT"},respJson)){
                Historial.initModificar(request, reg);
                mapProd.update(reg);
                ajax.setTodoOk(respJson);
            }
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    } 
    
    @RequestMapping(value="/ajax/PROD/delete/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> delete(HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
            if (fnc.existsParameters(request, new String[]{"CO_PROD"},respJson)){
                PROD reg=mapProd.getId(fnc.getIntParameter(request, "CO_PROD"));
                if (reg!=null){
                    mapProd.delete(reg);
                    ajax.setTodoOk(respJson);
                }
            }
            ajax.setResponseJSON(respJson);
        }
        return respJson;
    }     
    
    
    //Retorna los productos devolviendo el estado que tiene el producto con el cierre
    @RequestMapping(value="/ajax/PROD/selectStateCierre/",method = {RequestMethod.POST,RequestMethod.GET})//,produces = "application/json"
    public @ResponseBody Map<String,Object> selectStateCierre(HttpServletRequest request,@ModelAttribute("CO_PROD") int prod){//@RequestBody MODU advancedFormData
        //logger.info("Producto seleccionado :" + prod);
        Map<String,Object> respJson=ajax.getResponseJSON(); 
        
        //if (ajax.hasPermission(request, respJson)){
        	String nameClass="selectStateStringCierre";
        	//List<PROD> lista=mapProd.getAll();
                List<PROD> lista=mapProd.getAllByProduc(prod);
        	
                List<Object> listaRet=new ArrayList<Object>();
        	
        	CopyClass copy=new CopyClass();
        	List <CopyClass.Attribute> atributos=new ArrayList<CopyClass.Attribute>();       	
        	atributos.add(new CopyClass.Attribute("VerStateCierre",String.class, "" ,new Object[]{
        		new Object[]{
        			com.fasterxml.jackson.annotation.JsonProperty.class,
        			new Object[]{
        				new Object[]{"value","VerStateCierre"}
        			}	
        		}
        	}));
        	atributos.add(new CopyClass.Attribute("VerCierrePendiente",CIER.class, null ,new Object[]{
            		new Object[]{
            			com.fasterxml.jackson.annotation.JsonProperty.class,
            			new Object[]{
            				new Object[]{"value","VerCierrePendiente"} 
            			}	
            		}
            	}));        	
        	atributos.add(new CopyClass.Attribute("CantidadRecibos",Integer.class, null ,new Object[]{
            		new Object[]{
            			com.fasterxml.jackson.annotation.JsonProperty.class,
            			new Object[]{
            				new Object[]{"value","CantidadRecibos"} 
            			}	
            		}
            	}));
                atributos.add(new CopyClass.Attribute("CantidadFacturas",Integer.class, null ,new Object[]{
            		new Object[]{
            			com.fasterxml.jackson.annotation.JsonProperty.class,
            			new Object[]{
            				new Object[]{"value","CantidadFacturas"} 
            			}	
            		}
            	}));  
                atributos.add(new CopyClass.Attribute("CantidadBoletas",Integer.class, null ,new Object[]{
            		new Object[]{
            			com.fasterxml.jackson.annotation.JsonProperty.class,
            			new Object[]{
            				new Object[]{"value","CantidadBoletas"} 
            			}	
            		}
            	}));
        	//Object o=c.copyObject(nameClass, lista.get(0),atributos);                
        	for(PROD p:lista){
                    Object o=copy.copyObject(nameClass, p, atributos);
                    CIER cier=p.getCierrePendiente();                     
                    copy.setValueAttribute(o,"VerStateCierre",p.getStringStateCierre());                    
                    if (cier!=null){
                        copy.setValueAttribute(o,"VerCierrePendiente",cier);                        
                        copy.setValueAttribute(o,"CantidadRecibos",cier.getCantidadRecibosGenerados());
                        copy.setValueAttribute(o,"CantidadFacturas",cier.getCantidadFacturasGeneradas());
                        copy.setValueAttribute(o,"CantidadBoletas",cier.getCantidadBoletasGeneradas());
                    }else{
                        
                        CIER cierNull= new CIER();                     

                        Calendar cal= Calendar.getInstance(); 
                        int year= cal.get(Calendar.YEAR);    
                        int mes= cal.get(Calendar.MONTH);  
                        
                        cierNull.setFE_EMIS(null);
                        cierNull.setFE_VENC(null);
                        cierNull.setDE_RAIZ_FACT(1L);
                        cierNull.setDE_RAIZ_RECI(1L);
                        cierNull.setDE_RAIZ_BOLE(1L);
                        cierNull.setNU_PERI(mes);
                        cierNull.setNU_ANO(year);

                        copy.setValueAttribute(o,"VerCierrePendiente",cierNull);                        
                    }
                    listaRet.add(o);
        	}        	
        	ajax.setData(respJson, listaRet);
            ajax.setTodoOk(respJson);
            ajax.setResponseJSON(respJson);
        //}
        //logger.info("Producto encontrado :" + respJson);
        return respJson;
    }
    

    @RequestMapping(value="/ajax/PROD/selectServiceByProd/",method = {RequestMethod.POST})//,produces = "application/json"
    public @ResponseBody Map<String,Object> selectServiceByProd(@ModelAttribute("CO_PROD") String prod,HttpServletRequest request){//@RequestBody MODU advancedFormData
        Map<String,Object> respJson=ajax.getResponseJSON();
        if (ajax.hasPermission(request, respJson)){
                            
                if (prod.length() > 0){                       
                List<Map<String, Object>> lista=mapProd.selectByProd(Integer.parseInt(prod));
                Map<String, Object> reg_total=new TreeMap<String, Object>();
               
                        //respJson.put("data",mapProd.selectByProd(prod));   
                    ajax.setData(respJson, lista);
                    ajax.setTodoOk(respJson);
                }                        
                    ajax.setResponseJSON(respJson);
        }
        return respJson;
    }  


        
}
