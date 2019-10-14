/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.core2;

import com.americatel.facturacion.cores.Logs;
import com.americatel.facturacion.historial.Historial;
import com.americatel.facturacion.models.CIER;
import com.americatel.facturacion.models.CORT;
import com.americatel.facturacion.models.NEGO;
import com.americatel.facturacion.models.PROD;
import com.americatel.facturacion.models.NEGO_SALD;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author crodas
 */
public class Main implements Runnable{
    Date FE_EMIS=null;
    Date FE_VENC=null;
    Long DE_RAIZ_RECI=null;
    Long DE_RAIZ_FACT=null;
    List<PROD> productos=null;
    HttpServletRequest request=null;
    HttpServletRequest request_tmp=null;

    public Main(List<PROD> productos,Date FE_EMIS,Date FE_VENC,Long DE_RAIZ_RECI,Long DE_RAIZ_FACT,HttpServletRequest request) {
        //Corre el cierre de todos los productos
        this.FE_EMIS=FE_EMIS;
        this.FE_VENC=FE_VENC;
        this.DE_RAIZ_RECI=DE_RAIZ_RECI;
        this.DE_RAIZ_FACT=DE_RAIZ_FACT;
        this.productos=productos;        
        this.request=null;
    }
    public void run() {
        Long cantida_recibos=0L;
        Long cantida_facturas=0L;
        Long ret[];
        List<CIER> ciers=new ArrayList<CIER>();
        List<PROD> prods=new ArrayList<PROD>();
        Boolean con_error=false;
        int index=0;
        for (PROD producto : productos){
            CIER cier=producto.getCierrePendiente();
            if (cier!=null){
                Integer st=cier.getST_CIER_ForJSON();
                if (st==1 || st==3){//pendiente o termino cierre
                    ciers.add(cier);
                    prods.add(producto);
                }else{
                    con_error=true;
                }
            }
        }
        if (!con_error){
            for (CIER cier : ciers){
                cier.EnCierre(request);
            }
            cantida_recibos=DE_RAIZ_RECI;
            cantida_facturas=DE_RAIZ_FACT;
            for (CIER cier : ciers){
                cier.setDE_RAIZ_RECI(cantida_recibos);
                cier.setDE_RAIZ_FACT(cantida_facturas);
                cier.setFE_EMIS(FE_EMIS);
                cier.setFE_VENC(FE_VENC);
                Historial.initModificar(request, cier);
                cier.updateLanzar();                    
                cier.limpiarSaldosAproximados();
                        
                ret=this.lanzar(cier, prods.get(index));                    
                cantida_recibos=ret[0];
                cantida_facturas=ret[1]; 
                index++;
            }
        }
    }
    private Long[] lanzar(CIER cier,PROD prod){//pos 0 cantidad recibos, 1 cantidad facturas
        Logs.printMsg("Facturando Producto "+prod.getNO_PROD_ForJSON()+" PERIODO / A\u00d1O "+cier.getNU_PERI_ForJSON()+"/"+cier.getNU_ANO_ForJSON(),true,cier);
        
        List<NEGO> lstNego=prod.getNegociosParaProcesoCierre();
        int son=lstNego.size();
        List<CORT> cortes=null;
        long numero_recibo=cier.getDE_RAIZ_RECI_ForJSON();
        long numero_factura=cier.getDE_RAIZ_FACT_ForJSON();
        Double saldo_aplico=0d;
        Double saldo_anterior=0d;
        Double saldo_queda=0d;
        for (NEGO nego : lstNego) {
            saldo_anterior=0d;
            NegocioFacturar nf=new NegocioFacturar(cier, prod,nego,this.request,false);
            nf.setNumero_Recibo_Semilla(numero_recibo);
            nf.setNumero_Factura_Semilla(numero_factura);
            nf.facturar();
            nf.crearPdf();
            nf.saveBD();
            numero_recibo=nf.getNumero_Recibo_Semilla();
            numero_factura=nf.getNumero_Factura_Semilla();
            
            saldo_queda=nf.negocio_recibo.getSaldo();
//            if (!nego.isDesactivado() || nego.isDesactivadoEnPresentCierre()){
//                saldo_anterior=nf.negocio_recibo.getSaldo_anterior(); 
//            } 
//            saldo_aplico=saldo_queda-saldo_anterior;
            saldo_aplico=saldo_queda;
            if (saldo_aplico!=0){
                NEGO_SALD ns=new NEGO_SALD();
                ns.setCO_CIER(cier.getCO_CIER_ForJSON());
                ns.setCO_NEGO(nego.getCO_NEGO_ForJSON());
                ns.setIM_MONT(saldo_aplico);
                ns.insertar();                
            }
        }
        cier.TerminoCierre(this.request); 
        return new Long[]{numero_recibo,numero_factura};
    }
    
}
