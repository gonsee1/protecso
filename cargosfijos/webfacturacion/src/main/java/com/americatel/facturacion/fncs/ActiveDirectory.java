/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.fncs;

import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

/**
 *
 * @author crodas
 */
public class ActiveDirectory {
    public static boolean login(String usuario,String clave){
        Hashtable env = new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, "ldap://oplim01scd02.americatelperu.red:389/dc=americatelperu,dc=red");
        env.put(Context.SECURITY_PRINCIPAL, "americatelperu\\"+usuario);
        env.put(Context.SECURITY_CREDENTIALS, clave);    
        DirContext ctx = null;
        try {
            ctx = new InitialDirContext(env);
            return true;
        } catch (Exception e) {
        }
        return false;
    }
}
