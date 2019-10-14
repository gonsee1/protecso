/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.units;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.americatel.facturacion.core2.ListaRecibo;
import com.americatel.facturacion.core2.NegocioFacturar;
import com.americatel.facturacion.core2.NegocioRecibo;
import com.americatel.facturacion.core2.Recibo;
import com.americatel.facturacion.mappers.mapNEGO;
import com.americatel.facturacion.models.CIER;
import com.americatel.facturacion.models.NEGO;
import com.americatel.facturacion.models.PROD;

/**
 *
 * @author crodas
 */
@Component
public class Main {    
    public static boolean MOSTRAR_LOGS=false;
    
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    @Autowired static mapNEGO mapNego;
    
    
    @Autowired
    public void setmapNEGO(mapNEGO mapNego){
        Main.mapNego=mapNego;
    }     
    
    public void run(){
        this.Escenario1();
        this.Escenario2();
        this.Escenario3();
        this.Escenario4();
        this.Escenario5();
        this.Escenario6();
        this.Escenario7();
    }
    
    NegocioFacturar getNegocioFacturar(int CO_NEGO){
        NEGO nego=mapNego.getId(CO_NEGO);
        PROD prod=nego.getPROD();
        CIER cier=prod.getCierrePendiente();
        
        NegocioFacturar nf=new NegocioFacturar(cier, prod, nego, null, Boolean.TRUE);
        nf.facturar();
        nf.crearPdf(); 
        return nf;
    }
    
    void validar(int CO_NEGO,NegocioFacturar nf,AssertNegocio as){
        String problema="";
        NegocioRecibo negocio_recibo=nf.getNegocio_recibo();
        ListaRecibo lista_recibo=negocio_recibo.getRecibos();
        List<Recibo> recibos=lista_recibo.getRecibos();        
        if (recibos.size()==as.recibos.length){
            Map<Integer,Boolean> flags_recibos_assert=new HashMap<Integer, Boolean>();
            for(int i=0;i<as.recibos.length;i++){
                flags_recibos_assert.put(i, true);
            }
            for(Recibo recibo : recibos){
                if (problema.length()==0){
                    AssertRecibo recibo_asset=null;                    
                    for(int i=0;i<as.recibos.length && recibo_asset==null;i++){
                        if (flags_recibos_assert.get(i).equals(true)){
                            recibo_asset=as.recibos[i];
                            if (recibo.getTotal().equals(recibo_asset.getTotal())){
                                recibo_asset=recibo_asset;
                                flags_recibos_assert.put(i, false);
                            }
                        }
                    }
                    if (recibo_asset==null){
                        problema="No existe el recibo.";
                    }else{
                        if (recibo.getNeto().equals(recibo_asset.getNeto())){
                            if (recibo.getAjusteAfecto().equals(recibo_asset.getAjuste_afecto())){
                                if (recibo.getIGV().equals(recibo_asset.getIgv())){
                                    if (recibo.getAjusteNoAfecto().equals(recibo_asset.getAjuste_no_afecto())){

                                    }else{
                                        problema="El monto no afecto a igv no es correcto. Debe ser "+recibo_asset.getAjuste_no_afecto()+" y genera "+recibo.getAjusteNoAfecto()+".";
                                    } 
                                }else{
                                    problema="El igv no es correcto. Debe ser "+recibo_asset.getIgv()+" y genera "+recibo.getIGV()+".";
                                }                               
                            }else{
                                problema="El monto afecto a igv no es correcto. Debe ser "+recibo_asset.getAjuste_afecto()+" y genera "+recibo.getAjusteAfecto()+".";
                            }
                        }else{
                            problema="El neto no es correcto. Debe ser "+recibo_asset.getNeto()+" y genera "+recibo.getNeto()+".";
                        }
                    }
                }else{
                    break;
                }
            }
            Double total_saldo=nf.getSaldo_aproximado() + negocio_recibo.getSaldo();
            if (!total_saldo.equals(as.getSaldo())){
                problema="El saldo generado no es correcto. Debe ser "+as.getSaldo()+" y genera "+total_saldo+".";
            }           
            
        }else{
            problema="El n\u00famero de recibos generados no es correcto. Debe ser "+as.recibos.length +" y genera "+recibos.size()+".";
        }
        
        System.out.print(ANSI_RESET);
        if (problema.length()==0){
            System.out.print("Negocio "+CO_NEGO);
            System.out.println(ANSI_GREEN+" OK ");
        }else{
            System.out.print("Negocio "+CO_NEGO);
            System.out.println(ANSI_RED+" "+problema);            
        }
    }
    
    
    void Escenario1(){
        NegocioFacturar nf=null;
        AssertNegocio an=null;
        int CO_NEGO=0;
        
        CO_NEGO=10001;
        nf=getNegocioFacturar(CO_NEGO);
        an=new AssertNegocio(new AssertRecibo[]{
            new AssertRecibo(250, -80.65, 30.48, 0, 199.83, null)
        }, null);
        validar(CO_NEGO,nf,an);
        
        CO_NEGO=10002;
        nf=getNegocioFacturar(CO_NEGO);
        an=new AssertNegocio(new AssertRecibo[]{
            new AssertRecibo(250, -169.35, 14.52, 0, 95.17, null)
        }, null);
        validar(CO_NEGO,nf,an);        

        
        CO_NEGO=10003;
        nf=getNegocioFacturar(CO_NEGO);
        an=new AssertNegocio(new AssertRecibo[]{
            new AssertRecibo(177.42, -177.42, 0, 0, 0, null)
        }, null);
        validar(CO_NEGO,nf,an);        
        
    }

    void Escenario2(){
        NegocioFacturar nf=null;
        AssertNegocio an=null;
        int CO_NEGO=0;
        
        CO_NEGO=20001;
        nf=getNegocioFacturar(CO_NEGO);
        an=new AssertNegocio(new AssertRecibo[]{
            new AssertRecibo(250, -88.71, 29.03, 0, 190.32, null)
        }, null);
        validar(CO_NEGO,nf,an);
        
        
        CO_NEGO=20002;
        nf=getNegocioFacturar(CO_NEGO);
        an=new AssertNegocio(new AssertRecibo[]{
            new AssertRecibo(250, -120.97, 23.23, 0, 152.26, null)
        }, null);
        validar(CO_NEGO,nf,an);
        
        CO_NEGO=20003;
        nf=getNegocioFacturar(CO_NEGO);
        an=new AssertNegocio(new AssertRecibo[]{
            new AssertRecibo(250, -120.97, 23.23, 0, 152.26, null)
        }, null);
        validar(CO_NEGO,nf,an); 
        
        CO_NEGO=20004;
        nf=getNegocioFacturar(CO_NEGO);
        an=new AssertNegocio(new AssertRecibo[]{
            new AssertRecibo(250, -80.65, 30.48, 0, 199.83, null)
        }, null);
        validar(CO_NEGO,nf,an);  
        
        CO_NEGO=20005;
        nf=getNegocioFacturar(CO_NEGO);
        an=new AssertNegocio(new AssertRecibo[]{
            new AssertRecibo(250, -80.65, 30.48, 0, 199.83, null)
        }, null);
        validar(CO_NEGO,nf,an); 
        
        CO_NEGO=20006;
        nf=getNegocioFacturar(CO_NEGO);
        an=new AssertNegocio(new AssertRecibo[]{
            new AssertRecibo(250, -169.35, 14.52, 0, 95.17, null)
        }, null);
        validar(CO_NEGO,nf,an);
        
        CO_NEGO=20007;
        nf=getNegocioFacturar(CO_NEGO);
        an=new AssertNegocio(new AssertRecibo[]{}, null,-177.42);
        validar(CO_NEGO,nf,an);  //saldo -177.42  
        
        CO_NEGO=20008;
        nf=getNegocioFacturar(CO_NEGO);
        an=new AssertNegocio(new AssertRecibo[]{}, null,-177.42);
        validar(CO_NEGO,nf,an);  //saldo -177.42
        
        CO_NEGO=20009;
        nf=getNegocioFacturar(CO_NEGO);
        an=new AssertNegocio(new AssertRecibo[]{}, null,-137.09);
        validar(CO_NEGO,nf,an);  //saldo -137.09   
        
        CO_NEGO=20010;
        nf=getNegocioFacturar(CO_NEGO);
        an=new AssertNegocio(new AssertRecibo[]{}, null,-161.29);
        validar(CO_NEGO,nf,an);  
        
    }

    void Escenario3(){
        NegocioFacturar nf=null;
        AssertNegocio an=null;
        int CO_NEGO=0;
        
        CO_NEGO=30001;
        nf=getNegocioFacturar(CO_NEGO);
        an=new AssertNegocio(new AssertRecibo[]{
        }, null,-88.71); // saldo -88.71 
        validar(CO_NEGO,nf,an);
        
        CO_NEGO=30002;
        nf=getNegocioFacturar(CO_NEGO);
        an=new AssertNegocio(new AssertRecibo[]{
        }, null,-145.16); // saldo -145.16 
        validar(CO_NEGO,nf,an); 
        
        CO_NEGO=30003;
        nf=getNegocioFacturar(CO_NEGO);
        an=new AssertNegocio(new AssertRecibo[]{
        }, null,-120.96); // saldo -120.96  
        validar(CO_NEGO,nf,an); 
        
        CO_NEGO=30004;
        nf=getNegocioFacturar(CO_NEGO);
        an=new AssertNegocio(new AssertRecibo[]{
        }, null,-112.9); // saldo -112.9 
        validar(CO_NEGO,nf,an);
        
        CO_NEGO=30005;
        nf=getNegocioFacturar(CO_NEGO);
        an=new AssertNegocio(new AssertRecibo[]{
        }, null,-112.9); // saldo -112.9 
        validar(CO_NEGO,nf,an);
        
        CO_NEGO=30006;
        nf=getNegocioFacturar(CO_NEGO);
        an=new AssertNegocio(new AssertRecibo[]{
        }, null,-120.96); // saldo -120.96 
        validar(CO_NEGO,nf,an); 
        
        CO_NEGO=30007;
        nf=getNegocioFacturar(CO_NEGO);
        an=new AssertNegocio(new AssertRecibo[]{
        }, null,-177.42); // saldo -177.42 
        validar(CO_NEGO,nf,an);  
        
        
    }
    
    void Escenario4(){
        NegocioFacturar nf=null;
        AssertNegocio an=null;
        int CO_NEGO=0;
        
        CO_NEGO=40001;
        nf=getNegocioFacturar(CO_NEGO);
        an=new AssertNegocio(new AssertRecibo[]{
            new AssertRecibo(250, -83.33, 30, 0, 196.67, null)
        }, null); 
        validar(CO_NEGO,nf,an); 
        
        CO_NEGO=40002;
        nf=getNegocioFacturar(CO_NEGO);
        an=new AssertNegocio(new AssertRecibo[]{
            new AssertRecibo(250, -166.67, 15, 0, 98.33, null)
        }, null);  
        validar(CO_NEGO,nf,an); 
        
        CO_NEGO=40003;
        nf=getNegocioFacturar(CO_NEGO);
        an=new AssertNegocio(new AssertRecibo[]{
            new AssertRecibo(250, -250, 0, 0, 0, null)
        }, null,-118.55); // saldo  -118.55
        validar(CO_NEGO,nf,an);  
    }
    
    void Escenario5(){
        NegocioFacturar nf=null;
        AssertNegocio an=null;
        int CO_NEGO=0;
        
        CO_NEGO=50001;
        nf=getNegocioFacturar(CO_NEGO);
        an=new AssertNegocio(new AssertRecibo[]{
            new AssertRecibo(250, -40.32, 37.74, 0, 247.42, null)
        }, null); 
        validar(CO_NEGO,nf,an);
        
        CO_NEGO=50002;
        nf=getNegocioFacturar(CO_NEGO);
        an=new AssertNegocio(new AssertRecibo[]{
            new AssertRecibo(250, -169.35, 14.52, 0, 95.17, null)
        }, null); 
        validar(CO_NEGO,nf,an);
        
        CO_NEGO=50003;
        nf=getNegocioFacturar(CO_NEGO);
        an=new AssertNegocio(new AssertRecibo[]{
            new AssertRecibo(250, -250, 0, 0, 0, null)
        }, null,-44.09); // saldo  -44.09 
        validar(CO_NEGO,nf,an);
        
        CO_NEGO=50004;
        nf=getNegocioFacturar(CO_NEGO);
        an=new AssertNegocio(new AssertRecibo[]{
            new AssertRecibo(250, -250, 0, 0, 0, null)
        }, null,-169.09); // saldo  -169.09 
        validar(CO_NEGO,nf,an);
        
        CO_NEGO=50005;
        nf=getNegocioFacturar(CO_NEGO);
        an=new AssertNegocio(new AssertRecibo[]{
            new AssertRecibo(250, -250, 0, 0, 0, null)
        }, null,-290.32); // saldo  -290.32 
        validar(CO_NEGO,nf,an);
    }

    void Escenario6(){
        NegocioFacturar nf=null;
        AssertNegocio an=null;
        int CO_NEGO=0;
        
        CO_NEGO=60001;
        nf=getNegocioFacturar(CO_NEGO);
        an=new AssertNegocio(new AssertRecibo[]{
        }, null,-177.42); // saldo -177.42 
        validar(CO_NEGO,nf,an);
        
        CO_NEGO=60002;
        nf=getNegocioFacturar(CO_NEGO);
        an=new AssertNegocio(new AssertRecibo[]{
        }, null,-425.0); // saldo -425.0 
        validar(CO_NEGO,nf,an);
        
        CO_NEGO=60003;
        nf=getNegocioFacturar(CO_NEGO);
        an=new AssertNegocio(new AssertRecibo[]{
        }, null,-677.42); // saldo -677.42
        validar(CO_NEGO,nf,an);
    }

    void Escenario7(){
        NegocioFacturar nf=null;
        AssertNegocio an=null;
        int CO_NEGO=0;
        
        CO_NEGO=70001;
        nf=getNegocioFacturar(CO_NEGO);
        an=new AssertNegocio(new AssertRecibo[]{
            new AssertRecibo(250, -72.58, 31.94, 0, 209.36, null)
        }, null); 
        validar(CO_NEGO,nf,an);


        CO_NEGO=70002;
        nf=getNegocioFacturar(CO_NEGO);
        an=new AssertNegocio(new AssertRecibo[]{
        }, null,-169.35999999999999); // saldo -169.35999999999999 
        validar(CO_NEGO,nf,an); 


        CO_NEGO=70003;
        nf=getNegocioFacturar(CO_NEGO);
        an=new AssertNegocio(new AssertRecibo[]{
        }, null,-193.55); // saldo -193.55  
        validar(CO_NEGO,nf,an); 

        CO_NEGO=70004;
        nf=getNegocioFacturar(CO_NEGO);
        an=new AssertNegocio(new AssertRecibo[]{
        }, null,-217.74); // saldo -217.74 
        validar(CO_NEGO,nf,an); 
        
        
    }

    
    
}
