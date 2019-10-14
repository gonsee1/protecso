/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.fncs;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 *
 * @author crodas
 */
public class Numero {
    public static double redondear(double numero, Integer decimales){
        /*
        DecimalFormat df = new DecimalFormat( "#########,##" );        
        return Double.parseDouble(df.format(numero));*/
        /*
        BigDecimal n=new BigDecimal(numero);
        BigDecimal x=n.setScale(decimales,RoundingMode.HALF_UP);
        return x.doubleValue();
        */
        /*
        double n=Math.pow(10, decimales);
        BigDecimal a = BigDecimal.valueOf(numero);
        BigDecimal b = BigDecimal.valueOf(n);
        a.r
        return a.divide(b).doubleValue();
        */
    	BigDecimal result = new BigDecimal(numero*10);
    	result = result.setScale(decimales, BigDecimal.ROUND_HALF_UP);
    	result = result.divide(BigDecimal.TEN);
    	result = result.setScale(decimales, BigDecimal.ROUND_HALF_UP);
//        double n=Math.pow(10, decimales);
        return result.doubleValue();
        //return Math.round(numero*n)/n;
    }
    public static String formatearNumeroDigitos(Long numero,int numero_digitos){
        String sn=numero.toString();
        int nda=sn.length();
        for(int i=1;i<=(numero_digitos-nda);i++){
            sn="0"+sn;
        }
        return sn;                
    }
    public static String formatearNumeroDigitos(Integer numero,int numero_digitos){
        String sn=numero.toString();
        int nda=sn.length();
        for(int i=1;i<=(numero_digitos-nda);i++){
            sn="0"+sn;
        }
        return sn;                
    } 
    
    public static String leer(Double numero){
        return "(Leer numero "+numero+")";
    }


}
