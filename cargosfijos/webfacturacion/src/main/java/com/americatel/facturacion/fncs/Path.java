/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.fncs;

import java.net.URL;

/**
 *
 * @author crodas
 */
public class Path {
    public static String getPathWEB_INF(){
        ClassLoader cl=Path.class.getClassLoader();        
        URL url=cl.getResource("");
        return url.getPath()+"..";
    }
}
