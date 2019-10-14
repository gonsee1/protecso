/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.cores.modelo;

import com.americatel.facturacion.cores.ObjetoFacturar;

/**
 *
 * @author crodas
 */
public class TrimestralAdelantado extends GenericoNoMensualAdelantado{

    public TrimestralAdelantado(ObjetoFacturar objeto) {        
        super(objeto);
        this.MesesPeriodo=3; 
    }
    
}
