/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.cores;

import com.americatel.facturacion.models.CIER;
import com.americatel.facturacion.models.CIER_LOGS;
import com.americatel.facturacion.models.NEGO;
import com.americatel.facturacion.models.NEGO_SUCU;

/**
 *
 * @author crodas
 */
public class Logs {
    public static void printMsg(String msj,boolean guardarBD,CIER cier){
        String msjBD=printMsg(msj);
        if (guardarBD) Logs.printMsgBD(msj,cier);
    }
    public static void printMsg(String msj,boolean guardarBD,CIER cier,NEGO nego){
        String msjBD=printMsg(msj);
        if (guardarBD) Logs.printMsgBD(msj,cier,nego);
    }
    public static void printMsg(String msj,boolean guardarBD,CIER cier,NEGO nego,NEGO_SUCU nego_sucu){
        String msjBD=printMsg(msj);
        if (guardarBD) Logs.printMsgBD(msj,cier,nego,nego_sucu);
    }    
    public static void printMsg(String msj,int prioridad,boolean guardarBD,CIER cier){
        String msjBD=printMsg(msj,prioridad);
        if (guardarBD) Logs.printMsgBD(msj,cier);
    }
    public static void printMsg(String msj,int prioridad,boolean guardarBD,CIER cier,NEGO nego){
        String msjBD=printMsg(msj,prioridad);
        if (guardarBD) Logs.printMsgBD(msj,cier,nego);
    }
    public static void printMsg(String msj,int prioridad,boolean guardarBD,CIER cier,NEGO nego,NEGO_SUCU nego_sucu){
        String msjBD=printMsg(msj,prioridad);
        if (guardarBD) Logs.printMsgBD(msj,cier,nego,nego_sucu);
    }    
    public static String printMsg(String msj,int prioridad){
        for(int i=1;i<prioridad;i++) msj="\t"+msj;
        Logs.printMsg(msj);
        return msj;
    }    
    public static String printMsg(String msj){
        if (com.americatel.facturacion.units.Main.MOSTRAR_LOGS ){
            System.out.println(msj);
        }        
        return msj;
    }
    
    private static void printMsgBD(String msj,CIER cier,NEGO nego,NEGO_SUCU nego_sucu){
        CIER_LOGS log=new CIER_LOGS();
        log.setCO_CIER(cier.getCO_CIER_ForJSON());
        log.setCO_NEGO(nego.getCO_NEGO_ForJSON());
        log.setCO_NEGO_SUCU(nego_sucu.getCO_NEGO_SUCU_ForJSON());
        log.setDE_LOG(msj);
        log.setST_ERRO(false);
        log.insertar();
    }    

    private static void printMsgBD(String msj,CIER cier,NEGO nego){
        CIER_LOGS log=new CIER_LOGS();
        log.setCO_CIER(cier.getCO_CIER_ForJSON());
        log.setCO_NEGO(nego.getCO_NEGO_ForJSON());
        log.setDE_LOG(msj);
        log.setST_ERRO(false);
        log.insertar();
    }
    private static void printMsgBD(String msj,CIER cier){
        CIER_LOGS log=new CIER_LOGS();
        log.setCO_CIER(cier.getCO_CIER_ForJSON());
        log.setDE_LOG(msj);
        log.setST_ERRO(false);
        log.insertar();
    }    
    
    //ERROR
    
    public static void printError(String msj,Boolean guardarBD,CIER cier){
        String msjBD=printError(msj);
        if (guardarBD) Logs.printErrorBD(msj,cier);
    }
    public static void printError(String msj,Boolean guardarBD,CIER cier,NEGO nego){
        String msjBD=printError(msj);
        if (guardarBD) Logs.printErrorBD(msj,cier,nego);
    }
    public static void printError(String msj,Boolean guardarBD,CIER cier,NEGO nego,NEGO_SUCU nego_sucu){
        String msjBD=printError(msj);
        if (guardarBD) Logs.printErrorBD(msj,cier,nego,nego_sucu);
    }     
    
    public static void printError(String msj,int prioridad,Boolean guardarBD,CIER cier){
        String msjBD=printError(msj,prioridad);
        if (guardarBD) Logs.printErrorBD(msj,cier);
    }
    public static void printError(String msj,int prioridad,Boolean guardarBD,CIER cier,NEGO nego){
        String msjBD=printError(msj,prioridad);
        if (guardarBD) Logs.printErrorBD(msj,cier,nego);
    }
    public static void printError(String msj,int prioridad,Boolean guardarBD,CIER cier,NEGO nego,NEGO_SUCU nego_sucu){
        String msjBD=printError(msj,prioridad);
        if (guardarBD) Logs.printErrorBD(msj,cier,nego,nego_sucu);
    }  
    
    public static String printError(String msj,int prioridad){
        for(int i=1;i<prioridad;i++) msj="\t"+msj;
        printError(msj);
        return msj;
    }
    public static String printError(String msj){
        if (com.americatel.facturacion.units.Main.MOSTRAR_LOGS ){
            System.err.println(msj);
        }
        return msj;
    }
    
    private static void printErrorBD(String msj,CIER cier,NEGO nego,NEGO_SUCU nego_sucu){
        CIER_LOGS log=new CIER_LOGS();
        log.setCO_CIER(cier.getCO_CIER_ForJSON());
        log.setCO_NEGO(nego.getCO_MONE_FACT_ForJSON());
        log.setCO_NEGO_SUCU(nego_sucu.getCO_NEGO_SUCU_ForJSON());
        log.setDE_LOG(msj);
        log.setST_ERRO(true);
        log.insertar();
    }
    
    private static void printErrorBD(String msj,CIER cier,NEGO nego){
        CIER_LOGS log=new CIER_LOGS();
        log.setCO_CIER(cier.getCO_CIER_ForJSON());
        log.setCO_NEGO(nego.getCO_MONE_FACT_ForJSON());
        log.setDE_LOG(msj);
        log.setST_ERRO(true);
        log.insertar();
    }    
    
    private static void printErrorBD(String msj,CIER cier){
        CIER_LOGS log=new CIER_LOGS();
        log.setCO_CIER(cier.getCO_CIER_ForJSON());
        log.setDE_LOG(msj);
        log.setST_ERRO(true);
        log.insertar();
    }
 
}
