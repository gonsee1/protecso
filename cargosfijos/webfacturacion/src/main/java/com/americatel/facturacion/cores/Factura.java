/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.cores;

import com.americatel.facturacion.fncs.Numero;
import com.americatel.facturacion.historial.Historial;
import com.americatel.facturacion.models.CIER;
import com.americatel.facturacion.models.CLIE;
import com.americatel.facturacion.models.DEPA;
import com.americatel.facturacion.models.DIST;
import com.americatel.facturacion.models.FACT;
import com.americatel.facturacion.models.FACT_GLOS;
import com.americatel.facturacion.models.MONE_FACT;
import com.americatel.facturacion.models.NEGO;
import com.americatel.facturacion.models.NEGO_SUCU;
import com.americatel.facturacion.models.PROV;
import com.americatel.facturacion.models.SUCU;
import com.americatel.facturacion.models.TIPO_DOCU;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author crodas
 */
public class Factura {
    /*La detraccion es por recibo, no por la lista de recibos*/
    private Long numero;
    private Map<NEGO_SUCU,List<FacturaGlosa>> glosas;    
    private CIER cier=null;
    private CLIE clie=null;
    private NEGO nego=null;

    public Factura(CIER cier,CLIE clie,NEGO nego) {
        this.numero=0L;
        this.glosas=new TreeMap<NEGO_SUCU,List<FacturaGlosa>>(new Comparator<NEGO_SUCU>() {
            public int compare(NEGO_SUCU a, NEGO_SUCU b) {
                int ca=a.getCO_NEGO_SUCU_ForJSON();
                int cb=b.getCO_NEGO_SUCU_ForJSON();
                if (ca!=cb){
                    if(ca>cb)
                        return 1;
                    else 
                        return -1;
                }
                return 0;
            }
        });
        this.cier=cier;
        this.clie=clie;
        this.nego=nego;
    }    
    
    public void addGlosa(NEGO_SUCU nego_sucu,FacturaGlosa g){        
        List<FacturaGlosa> lst=this.glosas.get(nego_sucu);
        if (lst==null){
            lst=new ArrayList<FacturaGlosa>();
            this.glosas.put(nego_sucu, lst);
        }
        lst.add(g);
    }    

    void saveBD(HttpServletRequest request) {
        SUCU sucu_corr=this.nego.getSucursalCorrespondencia();
        DIST dist_corr=sucu_corr.getDistrito();
        PROV prov_corr=dist_corr.getProvincia();
        DEPA depa_corr=prov_corr.getDepartamento();
        
        SUCU sucu_fisc=this.clie.getSucursalFiscal();
        DIST dist_fisc=sucu_fisc.getDistrito();
        PROV prov_fisc=dist_fisc.getProvincia();
        DEPA depa_fisc=prov_fisc.getDepartamento(); 
        
        SUCU sucu_inst=this.glosas.entrySet().iterator().next().getKey().getSUCU();
        DIST dist_inst=sucu_inst.getDistrito();
        PROV prov_inst=dist_inst.getProvincia();
        DEPA depa_inst=prov_inst.getDepartamento(); 
        
        MONE_FACT mone_fact=this.nego.getMonedaFacturacion();
        
        TIPO_DOCU tipo_docu=this.clie.getTipoDocumento();
        
        double totales[]=this.getTotales();//VALOR_VENTA [0],DESCUENTO [1],VALOR_DE_VENTA_NETO [2],IGV [3],TOTAL [4]
        
        FACT f=new FACT();
        f.setCO_CIER(this.cier.getCO_CIER_ForJSON());
        f.setCO_CLIE(this.clie.getCO_CLIE_ForJSON());
        f.setCO_DIST_CORR(dist_corr.getCO_DIST_ForJSON());
        f.setCO_DIST_FISC(dist_fisc.getCO_DIST_ForJSON());
        f.setCO_DIST_INST(dist_inst.getCO_DIST_ForJSON());
        f.setCO_FACT(this.numero);
        f.setCO_MONE_FACT(mone_fact.getCO_MONE_FACT_ForJSON());
        f.setCO_NEGO(this.nego.getCO_NEGO_ForJSON());
        f.setCO_TIPO_DOCU(tipo_docu.getCO_TIPO_DOCU_ForJSON());
        f.setDE_CODI_BUM(this.clie.getDE_CODI_BUM_ForJSON());
        f.setDE_DIRE_CORR(sucu_corr.getDE_DIRE_ForJSON());
        f.setDE_DIRE_FISC(sucu_fisc.getDE_DIRE_ForJSON());
        f.setDE_DIRE_INST(sucu_inst.getDE_DIRE_ForJSON());
        f.setDE_MONT(Numero.leer(totales[4]));
        f.setDE_NUME_DOCU(clie.getDE_NUME_DOCU_ForJSON());
        f.setDE_SIMB_MONE_FACT(mone_fact.getDE_SIMB_ForJSON());
        f.setFE_EMIS(this.cier.getFE_EMIS_ForJSON());
        f.setFE_VENC(this.cier.getFE_VENC_ForJSON());
        f.setIM_IGV(totales[3]);
        f.setIM_TOTA(totales[4]);
        f.setIM_VALO_DESC(totales[1]);
        f.setIM_VALO_VENT(totales[0]);
        f.setIM_VALO_VENT_NETO(totales[2]);
        f.setNO_CLIE(this.clie.getFullNameCliente());
        f.setNO_DIST_CORR(dist_corr.getNO_DIST_ForJSON());
        f.setNO_DIST_FISC(dist_fisc.getNO_DIST_ForJSON());
        f.setNO_DIST_INST(dist_inst.getNO_DIST_ForJSON());
        f.setNO_MONE_FACT(mone_fact.getNO_MONE_FACT_ForJSON());
        f.setNO_TIPO_DOCU(tipo_docu.getNO_TIPO_DOCU_ForJSON());
        
        Historial.initInsertar(request, f);
        
        f.save();
        
        Map<NEGO_SUCU,List<FacturaGlosa>> ListaGlosas=this.getGlosas();
        for (Map.Entry<NEGO_SUCU, List<FacturaGlosa>> entry : ListaGlosas.entrySet()){
            NEGO_SUCU nego_sucu=entry.getKey();
            for(FacturaGlosa fg : entry.getValue()){
                FACT_GLOS g=new FACT_GLOS();
                g.setCO_FACT(this.numero);
                g.setCO_NEGO_SUCU(nego_sucu.getCO_NEGO_SUCU_ForJSON());
                g.setDE_PERI(fg.getPeriodo());
                g.setIM_MONT(fg.getMonto());
                g.setNO_GLOS(fg.getNombre());                    
                Historial.initInsertar(request, g);
                g.save();
            }
        }
        
    }

    void setNumero(Long numero_factura) {
        this.numero=numero_factura;
    }
    public Long getNumero(){
        return this.numero;
    }
    
    public CIER getCier(){
        return this.cier;
    }
     
    public CLIE getClie(){
        return this.clie;
    }   
     
    public NEGO getNego(){
        return this.nego;
    } 
    
    public Map<NEGO_SUCU,List<FacturaGlosa>> getGlosas(){
        return this.glosas;
    }
    
    public double[] getTotales(){
        //Obtiene todo lo que va abajo de cada factura
        //VALOR_VENTA,DESCUENTO,VALOR_DE_VENTA_NETO,IGV,TOTAL
        double igv=0.18;
        double ret[]=new double[]{0,0,0,0,0};
        for (Map.Entry<NEGO_SUCU, List<FacturaGlosa>> entry : this.glosas.entrySet()){
            for(FacturaGlosa glosa:entry.getValue()){
                ret[0]+=glosa.getMonto();
            }
        }
        ret[0]=Numero.redondear(ret[0],2);
        ret[1]=0f;
        ret[2]=Numero.redondear(ret[0]-ret[1],2);
        ret[3]=Numero.redondear(ret[2]*igv, 2);
        ret[4]=Numero.redondear(ret[2]+ret[3], 2);
        return ret;
    }    
}
