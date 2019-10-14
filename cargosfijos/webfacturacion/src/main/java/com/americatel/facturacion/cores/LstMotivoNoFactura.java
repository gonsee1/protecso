/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.cores;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author crodas
 */
public class LstMotivoNoFactura {
    private List<EMotivoNoFactura> motivosNoFactura=new ArrayList<EMotivoNoFactura>();
    public List<EMotivoNoFactura> getList(){
        return this.motivosNoFactura;
    }
    public void addMotivosNoFactura(EMotivoNoFactura motivo){
        if (motivo!=null) this.motivosNoFactura.add(motivo);
    }    
    public EMotivoNoFactura getMotivoNoFactura(){
        EMotivoNoFactura ret=null;
        if (this.motivosNoFactura.size()>0){
            Map<EMotivoNoFactura,Integer> counter=new TreeMap<EMotivoNoFactura, Integer>();
            Integer max=0;
            Integer tmp=0;
            for(EMotivoNoFactura m : EMotivoNoFactura.values()){
                counter.put(m, 0);
            }
            for(int i=0;i<this.motivosNoFactura.size();i++){
                tmp=counter.get(this.motivosNoFactura.get(i));
                counter.put(this.motivosNoFactura.get(i), tmp+1);
            }
            for(EMotivoNoFactura m : EMotivoNoFactura.values()){
                if (counter.get(m)>max){
                    max=counter.get(m);
                }
            }
            for(EMotivoNoFactura m : EMotivoNoFactura.values()){
                if (max==counter.get(m)){
                    return m;
                }
            }
        }
        return ret;
    }    
}
