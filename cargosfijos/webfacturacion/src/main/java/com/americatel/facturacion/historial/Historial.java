/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.historial;

import com.americatel.facturacion.fncs.fnc;
import com.americatel.facturacion.fncs.permissions;
import com.americatel.facturacion.models.USUA;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author crodas
 */
public class Historial {
    //public static final String stringMapperModificar="CO_USUA_CREO=#{CO_USUA_CREO},CO_USUA_MODI=#{CO_USUA_MODI},FH_CREO=#{FH_CREO},FH_MODI=#{FH_MODI}";
    public static final String stringMapperModificar="CO_USUA_MODI=#{CO_USUA_MODI},FH_MODI=#{FH_MODI}";
    public static final String stringMapperInsertarTitles="CO_USUA_CREO,CO_USUA_MODI,FH_CREO,FH_MODI";
    public static final String stringMapperInsertarValuesNumeral="#{CO_USUA_CREO},#{CO_USUA_MODI},#{FH_CREO},#{FH_MODI}";//,'${FH_CREO}','${FH_MODI}'
    public static void initInsertar(HttpServletRequest request, Object o) {
        if (o instanceof Historial){
            Historial h=(Historial)o;
            h.FH_CREO=new Date();
            h.FH_MODI=h.FH_CREO;
            if(request!=null && request.getSession()!=null && permissions.isLogin(request)){            
                USUA usua=permissions.getUsuarioLogin(request);                
                h.CO_USUA_CREO=usua.getCO_USUA_ForJSON();                
                h.CO_USUA_MODI=h.CO_USUA_CREO;                            
            }
        }
    }
    
    public static void initModificar(HttpServletRequest request, Object o) {
        if (o instanceof Historial){
            Historial h=(Historial)o;
            h.FH_MODI=new Date();
            if(request!=null && request.getSession()!=null && permissions.isLogin(request)){            
                USUA usua=permissions.getUsuarioLogin(request);                
                h.CO_USUA_MODI=usua.getCO_USUA_ForJSON();                
            }
        }
    }
  
    public static void initEliminar(HttpServletRequest request, Object o) {//Eliminacion Logica
        if (o instanceof Historial){
            Historial h=(Historial)o;
            h.FH_MODI=new Date();
            if(request!=null && request.getSession()!=null && permissions.isLogin(request)){            
                USUA usua=permissions.getUsuarioLogin(request);                
                h.CO_USUA_MODI=usua.getCO_USUA_ForJSON();                
            }
        }
    }    
    
    
    
    
    
    protected Integer CO_USUA_CREO;
    protected Integer CO_USUA_MODI;
    
    @DateTimeFormat(pattern = fnc.FORMAT_DATE_TIME_SEPARATOR)//"yyyy-MM-dd'T'HH:mm:ss")//mmss
    protected Date FH_CREO;
    @DateTimeFormat(pattern = fnc.FORMAT_DATE_TIME_SEPARATOR)//"yyyy-MM-dd'T'HH:mm:ss")//mmss
    protected Date FH_MODI;    

    @JsonProperty("CO_USUA_CREO")
    public Integer getCO_USUA_CREO() {
        return CO_USUA_CREO;
    }

    @JsonProperty("CO_USUA_MODI")
    public Integer getCO_USUA_MODI() {
        return CO_USUA_MODI;
    }

    @JsonProperty("FH_CREO")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = fnc.FORMAT_DATE)
    public Date getFH_CREO() {
        return FH_CREO;
    }

    @JsonProperty("FH_MODI")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = fnc.FORMAT_DATE)
    public Date getFH_MODI() {
        return FH_MODI;
    }
    

    
    
}
