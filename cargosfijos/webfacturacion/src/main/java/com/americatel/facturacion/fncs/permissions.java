/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.fncs;

import com.americatel.facturacion.mappers.mapMODU;
import com.americatel.facturacion.mappers.mapPERF;
import com.americatel.facturacion.mappers.mapUSUA;
import com.americatel.facturacion.models.ITEM_MODU;
import com.americatel.facturacion.models.MODU;
import com.americatel.facturacion.models.PERF;
import com.americatel.facturacion.models.USUA;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;

/**
 *
 * @author crodas
 */
@Component
public class permissions {
    private static mapUSUA mapUsu;
    private static mapMODU mapModu;
    //private static mapPERF mapPerf;
    
    private static final String VAR_SESSION_USUARIO="user_login_system";
    private static final String VAR_SESSION_ACCESS_MODULOS="session_access_modulos";
    private static final String VAR_SESSION_PERFIL="perfil_login_system";
    private static final String VAR_SESSION_TXT_USUARIO="txt_usuario";
    private static final String VAR_SESSION_TXT_CLAVE="txt_clave";
    
   @Autowired
    public void setmapUSUA(mapUSUA mapUsu){
        permissions.mapUsu = mapUsu;
    }
    
   @Autowired
    public void setmapMODU(mapMODU mapModu){
        permissions.mapModu = mapModu;
    }
    /*    
   @Autowired
    public void setmapPERF(mapPERF mapPerf){
        permissions.mapPerf = mapPerf;
    }
    */
    public static boolean isLogin(HttpServletRequest request){
        return getUsuarioLogin(request)!=null;
    }
    public static void setSessionVar(HttpServletRequest request,String name,Object value){
        HttpSession s=request.getSession();
        s.setAttribute(name, value);        
    }
    public static Object getSessionVar(HttpServletRequest request,String name){
        HttpSession s=request.getSession();
        return s.getAttribute(name);        
    }
    public static void destroySessionVar(HttpServletRequest request,String name){
        HttpSession s=request.getSession();
        s.removeAttribute(name);
    }
    public static USUA getUsuarioLogin(HttpServletRequest request){
        USUA usu=null;
        HttpSession s=request.getSession();
        usu = (USUA) s.getAttribute(VAR_SESSION_USUARIO);
        return usu;
    } 
    public static PERF getPerfilLogin(HttpServletRequest request){
        PERF perf=null;
        HttpSession s=request.getSession();
        perf = (PERF) s.getAttribute(VAR_SESSION_PERFIL);
        return perf;
    }    
    public static void close(HttpServletRequest request){
        HttpSession s=request.getSession();
        s.removeAttribute(VAR_SESSION_USUARIO);
        s.removeAttribute(VAR_SESSION_PERFIL);
        s.removeAttribute(VAR_SESSION_ACCESS_MODULOS);
    } 
    
    public static USUA login(HttpServletRequest request){
        USUA ret=null;
        if ("POST".equals(request.getMethod())){        	
            String usuario=(String)request.getParameter(VAR_SESSION_TXT_USUARIO),clave=(String)request.getParameter(VAR_SESSION_TXT_CLAVE);
            if (usuario!=null && clave!=null){
                /*if (ActiveDirectory.login(usuario, clave)){
                    USUA usua=mapUsu.getByUser(usuario);
                    if (usua==null){
                        usua=new USUA();
                        usua.setDE_USER(usuario);
                        mapUsu.insert(usua);
                        usua=mapUsu.getByUser(usuario);
                    }
                    HttpSession s=request.getSession();
                    s.setAttribute(VAR_SESSION_USUARIO, usua);
                    s.setAttribute(VAR_SESSION_PERFIL, usua.getPerfil());
                    permissions.initSessionAccessModulo(request);                    
                }
                */
                clave=encrypt.md5(clave);
                try {
                    ret=mapUsu.getUserFromLogin(usuario, clave);
                } catch (Exception e) {
                	e.printStackTrace();
                    ret=null;
                }                
                if (ret!=null){
                    HttpSession s=request.getSession();
                    s.setAttribute(VAR_SESSION_USUARIO, ret);
                    s.setAttribute(VAR_SESSION_PERFIL, ret.getPerfil());
                    permissions.initSessionAccessModulo(request);
                }
            }
        }       
        return ret;
    }
    
    private static void initSessionAccessModulo(HttpServletRequest request){
        if (permissions.isLogin(request)){
            HttpSession s=request.getSession();            
            PERF perf=permissions.getPerfilLogin(request);            
            List<ITEM_MODU> LstItem =new ArrayList<ITEM_MODU>();
            List<MODU> LstModu= new ArrayList<MODU>();
            Map<MODU,List<ITEM_MODU>> LstGroup=new TreeMap<MODU,List<ITEM_MODU>>(new Comparator<MODU>(){
                public int compare(MODU o1, MODU o2) {     
                    return ((Integer)o1.getCO_MODU_ForJSON()).compareTo((Integer)o2.getCO_MODU_ForJSON());
                }                

            }); 
            
            if (perf!=null){
                LstItem =perf.getItemsModulos();
                ITEM_MODU item;
                MODU modu;
                boolean existe;
                for(int i=0;i<LstItem.size();i++){
                    item=LstItem.get(i);
                    existe=false;
                    modu=null;
                    for(int k=0;k<LstModu.size() && !existe;k++){
                        modu=LstModu.get(k);
                        if (modu.getCO_MODU_ForJSON()==item.getCO_MODU_ForJSON()){
                            existe=true;
                        }
                    }
                    if (!existe){
                        modu=item.getModulo();
                        LstModu.add(modu);
                        LstGroup.put(modu,new ArrayList<ITEM_MODU> ());

                    }
                    if (modu!=null){                        
                        LstGroup.get(modu).add(item);
                    }
                }
            }
            s.setAttribute(VAR_SESSION_ACCESS_MODULOS, new Object[]{LstModu,LstItem,LstGroup});
        }
    }
    /*
    public static Map <String,List<String>> getItemsModulos(HttpServletRequest request){
        Map<String,List<String>> ret= new HashMap<String,List<String>>();
        PERF perf=permissions.getPerfilLogin(request);
        List<ITEM_MODU> Lst =perf.getItemsModulos();
        ITEM_MODU item;
        MODU modu;
        for(int i=0;i<Lst.size();i++){
            item=Lst.get(i);
            modu=item.getModulo();
            if (ret.get(modu.NO_MODU)==null){
               ret.put(modu.NO_MODU, new ArrayList<String>() {});
            }
            ret.get(modu.NO_MODU).add(item.NO_ITEM_MODU);
        }
        return ret;
    }
    */
    
    public static void initModelMap(HttpServletRequest request,ModelMap modelmap){
        if (permissions.isLogin(request)){
            HttpSession session=request.getSession();
            Object data[]=(Object [])session.getAttribute(VAR_SESSION_ACCESS_MODULOS);
            List<MODU> LstModu=(List<MODU>)data[0];
            List<ITEM_MODU> LstItem=(List<ITEM_MODU>)data[1]; 
            Map<MODU,List<ITEM_MODU>> LstGroup=(Map<MODU,List<ITEM_MODU>>) data[2];
            modelmap.addAttribute("settings__access__windows", LstModu);
            modelmap.addAttribute("settings__access__panels", LstItem);
            modelmap.addAttribute("settings__access__groups", LstGroup);
            
            modelmap.addAttribute("settings__user_online", getUsuarioLogin(request));
            
            //format datetime
            modelmap.addAttribute("settings__format__date", fnc.FORMAT_DATE);
            modelmap.addAttribute("settings__format__date_time", fnc.FORMAT_DATE_TIME);
            
            modelmap.addAttribute("settings__var__send__permission__package", ajax.VAR_SEND_PACKAGE);
            
            List<Object[]> lst=new ArrayList<Object[]>();
            String base="/resources/images/icons/definidos";
            String uri="";
            File folder = new File(Path.getPathWEB_INF()+"/.."+base);
            if (folder.exists()){
                for (final File fileEntry : folder.listFiles()) {
                    uri="."+base+"/"+fileEntry.getName();
                    if (fileEntry.isFile()){
                        lst.add(new Object[]{encrypt.checkSumMd5(uri),uri,fileEntry.getName()});
                    }
                }
            }
            modelmap.addAttribute("settings__var__list_icons", lst);
        }
    }
    
    public static boolean hasPermissionPackagePanels(HttpServletRequest request,String pack){
         /*return true;
       
        if (fnc.MODE_DEBUG)
            return true;*/
        if (permissions.isLogin(request)){
           Object data[]=(Object [])request.getSession().getAttribute(VAR_SESSION_ACCESS_MODULOS);
           List<ITEM_MODU> LstItem=(List<ITEM_MODU>)data[1]; 
           for(int i=0;i<LstItem.size();i++){
               String nombre="Desktop.Facturacion.Panels."+LstItem.get(i).getDE_PACK_ForJSON();
               if(nombre.equals(pack)){
                   return true;
               }
           }
           
       }
       return false;
    }
}
