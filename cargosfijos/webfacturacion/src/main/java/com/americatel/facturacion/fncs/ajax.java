/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.fncs;

import java.util.Map;
import java.util.TreeMap;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author crodas
 */
public class ajax {
    public static final String MSG_ERROR="Ocurrio un problema, si este problema persiste consulte con el administrador.";
    public static final String MSG_TODO_OK="Se ejecuto sin probemas lo solicitado.";
    public static final String MSG_ERROR_PERMISSION="No tiene permiso para realizar lo solicitado.";
    public static final String VAR_SEND_PACKAGE="package_send";
    public static final String MSG_ERROR_NOT_VAR_SEND_PACKAGE="Falta el parametros.";
    
    
    public static Map<String,Object> getResponseJSON(){
        Map<String,Object> respJson=new TreeMap<String,Object>();
        respJson.put("success", false);
        respJson.put("data", "");
        respJson.put("numResults",0);
        respJson.put("msg", MSG_ERROR);
        return respJson;
    }
    public static void setResponseJSON(Map<String,Object> respJson){
        Boolean success=(Boolean)respJson.get("success");
        if (success){
            if ((String)respJson.get("msg")==MSG_ERROR){
                respJson.put("msg", "Se realizo lo solicitado con exito.");
            }
        }
    } 
    
    public static void setTodoOk(Map<String,Object> respJson){
        respJson.put("success", true);
        respJson.put("msg", MSG_TODO_OK);
    }

    public static void setNumResults(Map<String,Object> respJson,int numResults){
        respJson.put("numResults", numResults);
    }
    
    public static void setData(Map<String,Object> respJson,Object data) {
        respJson.put("data", data);
    }
    
    public static void setMsg(Map<String,Object> respJson,String data) {
        respJson.put("msg", data);
    }    
    
    public static void setError(Map<String,Object> respJson,String msg){
        respJson.put("msg", msg);
        respJson.put("success",false);
    }

    public static void addError(Map<String,Object> respJson,String msg){
        String antes=(String)respJson.get("msg");
        if (antes==null){
            respJson.put("msg", msg);
        }else{
            respJson.put("msg", antes+msg);
        }
        respJson.put("success",false);
    }    
    
    public static boolean hasPermission(HttpServletRequest request,Map<String,Object> respJson){
        if (fnc.MODE_DEBUG)
            return true;
        if (fnc.existsParameter(request,VAR_SEND_PACKAGE )){
            boolean ret=permissions.hasPermissionPackagePanels(request, fnc.getStringParameter(request, VAR_SEND_PACKAGE));        
            if (!ret){
                ajax.setError(respJson, MSG_ERROR_PERMISSION);
            }
            return ret;
        }else{
            ajax.setError(respJson, MSG_ERROR_NOT_VAR_SEND_PACKAGE);
        }
        return false;
    }
}
