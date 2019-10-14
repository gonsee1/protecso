/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.fncs;

import java.io.UnsupportedEncodingException;
import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.UTF_8;
import java.util.Date;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author crodas
 */
public class fnc {
    
    public static final String FORMAT_DATE="yyyy-MM-dd";
    public static final String FORMAT_DATE_TIME_SEPARATOR="yyyy-MM-dd'T'HH:mm:ss";
    public static final String FORMAT_DATE_TIME=fnc.FORMAT_DATE_TIME_SEPARATOR;//"yyyy-MM-dd HH:mm:ss";
    
    public static final boolean MODE_DEBUG=false;
    public static boolean existsParameters(HttpServletRequest request,String [] keys){
        for(String key: keys){
            if (request.getParameter(key)==null)
                return false;            
        }
        return true;        
    }
    
    public static boolean existsParameters(HttpServletRequest request,String [] keys,Map<String,Object> respJson){
        for(String key: keys)
            return fnc.existsParameter(request, key, respJson);
        return true;        
    }    
    
    public static boolean existsParameter(HttpServletRequest request,String key,Map<String,Object> respJson){
        if (request.getParameter(key)==null){
            respJson.put("success",false);
            respJson.put("msg", "No se pudo procesar la solicitud por la falta del parametro ("+key+").");
            return false;            
        }
        return true;        
    }
    
    public static long getLongParameter(HttpServletRequest request,String name){
        return Long.parseLong(request.getParameter(name));
    }
    
    public static int getIntParameter(HttpServletRequest request,String name){
        return Integer.parseInt(request.getParameter(name));
    }
    
    public static String getStringParameter(HttpServletRequest request,String key){
        //return new String(request.getParameter(key).getBytes(ISO_8859_1),UTF_8);
    	return request.getParameter(key);
    }

    public static boolean existsParameter(HttpServletRequest request, String key) {
        return !(request.getParameter(key)==null);
    }
/*
    public static boolean existsParametersPagination(HttpServletRequest request, Map<String, Object> respJson) {
        return fnc.existsParameters(request, new String[]{"page","limit",},respJson);
    }
    public static String[] getParametersPagination(HttpServletRequest request){
        return new String[]{fnc.getStringParameter(request, "page"),fnc.getStringParameter(request, "limit")};
    }
 */   

    public static Date getDateParameter(HttpServletRequest request, String key) {        
        String val=request.getParameter(key);
        if (val!=null)
            if (val.trim().length()==0)
                val=null;
        if (val!=null)
            return new Date(val);
        return null;
    }
    
    
    
    public static String capitalize(String cad){
        if (cad!=null){
            if (cad.length()>1)
                return Character.toUpperCase(cad.charAt(0))+cad.substring(1).toLowerCase();
            else
                return cad.toUpperCase();
        }
        return null;        
    }
}

