/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.fncs;

/**
 *
 * @author crodas
 */
public class OitUtils {
    public static Object[] getInfo(String oit){
        String uoit=oit.toUpperCase();
        Boolean oit_soart=Boolean.FALSE;
        Boolean inicia_texto=Boolean.FALSE; 
        Integer ioit=-1;
        if (uoit.indexOf("O")==0){
            oit_soart=Boolean.TRUE;
            inicia_texto=Boolean.TRUE;
        }
        if (uoit.indexOf("S")==0)
            inicia_texto=Boolean.TRUE;
        if (inicia_texto)
            uoit=uoit.substring(1);
        try {
            ioit=(int) Double.parseDouble(uoit);
        } catch (Exception e) {
            System.out.println("Error: "+e.getMessage());
        }
        return new Object[]{ioit,oit_soart};
    }
}
