/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.fncs;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author crodas
 */
public class encrypt {
    public static String md5(String cadena){
    	return encrypt.checkSumMd5(cadena).toString(16);
    	/*
        String ret=null;
        byte[] bytesOfMessage=null;
        bytesOfMessage = cadena.getBytes();
        if (bytesOfMessage!=null){
            MessageDigest md=null;
            try {
                md = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException ex) {
                md=null;
            }
            if (md!=null){
                byte[] thedigest = md.digest(bytesOfMessage);
                ret=new String(thedigest);
            }            
        }
        return ret;*/
    }
    public static BigInteger checkSumMd5(String cadena){
        MessageDigest m;
        try {
            m = MessageDigest.getInstance("MD5");
            m.update(cadena.getBytes(),0,cadena.length());
            return new BigInteger(1,m.digest());
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(encrypt.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
