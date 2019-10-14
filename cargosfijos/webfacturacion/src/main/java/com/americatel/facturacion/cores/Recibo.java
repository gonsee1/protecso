/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.cores;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import com.americatel.facturacion.fncs.FechaHora;
import com.americatel.facturacion.fncs.Numero;
import com.americatel.facturacion.historial.Historial;
import com.americatel.facturacion.models.CIER;
import com.americatel.facturacion.models.CLIE;
import com.americatel.facturacion.models.MONE_FACT;
import com.americatel.facturacion.models.NEGO;
import com.americatel.facturacion.models.NEGO_SUCU;
import com.americatel.facturacion.models.RECI;
import com.americatel.facturacion.models.RECI_GLOS;
import com.americatel.facturacion.models.SUCU;
import com.americatel.facturacion.models.TIPO_DOCU;

/**
 *
 * @author crodas
 */
public class Recibo {
    /*La detraccion es por recibo, no por la lista de recibos*/
    private Long numero;
    private Date cierre_factura_inicio=null;
    private Date cierre_factura_fin=null;
    private Date cierre_factura_inicio_real=null;//cruzandolo con los cortes y suspensiones
    private Date cierre_factura_fin_real=null;//cruzandolo con los cortes y suspensiones    
    private NEGO nego=null;
    private CLIE clie=null;
    private SUCU sucu_corr=null;
    private List <Date []> lstDiasSinServicio=null;
    private CIER cier=null;
    private SUCU sucu_fiscal;
    private MONE_FACT mone_fact;
    private TIPO_DOCU tipo_docu;
    
    private Double  IM_AJUS_SIGV=null;//ajustes afecto igv
    private Double  IM_AJUS_NIGV=null;//ajustes no afecto igv
    
    private Map<NEGO_SUCU,List<ReciboGlosa>> glosas=new TreeMap<NEGO_SUCU,List<ReciboGlosa>>(new Comparator<NEGO_SUCU>() {
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
    private List<ReciboGlosa> promociones=new ArrayList<ReciboGlosa>();
    private List<ReciboGlosa> ajustes=new ArrayList<ReciboGlosa>();
    
    private Boolean AfectoDetraccion=null;
    
    private RECI reci=null;//Recibo que se editar y se vuelve a generar desde la tabla TMRECI no lee de otro lado
    public Recibo(RECI reci){
        this.numero=reci.getCO_RECI_ForJSON();
        this.clie=reci.getClie_ForJSON();
        this.reci=reci;
        List<RECI_GLOS> lista=reci.getGlosas();
        for(RECI_GLOS glosa : lista){
            NEGO_SUCU nego_sucu=glosa.getNEGO_SUCU_ForJSON();
            if(nego_sucu!=null){
                List<ReciboGlosa> base=glosas.get(nego_sucu);
                if(base==null){
                    glosas.put(nego_sucu, new ArrayList<ReciboGlosa>());
                    base=glosas.get(nego_sucu);
                }
//                base.add(new ReciboGlosa(glosa.getNO_GLOS_ForJSON(), glosa.getIM_MONT_ForJSON(), glosa.getETipoReciboGlosa()));
            }
        }
    }
    
    public Recibo(Date cierre_factura_inicio,Date cierre_factura_fin,CIER cier,CLIE clie,NEGO nego,SUCU sucu_corr){
        this(cierre_factura_inicio,cierre_factura_fin,cier,clie,nego,sucu_corr,new ArrayList<Date[]>());
    }
    public Recibo(Date cierre_factura_inicio,Date cierre_factura_fin,CIER cier,CLIE clie,NEGO nego,SUCU sucu_corr,List <Date []> lstDiasSinServicio){
        this.cier=cier;
        this.clie=clie;
        this.tipo_docu=clie.getTipoDocumento();
        this.sucu_fiscal=this.clie.getSucursalFiscal();
        this.cierre_factura_inicio=cierre_factura_inicio;
        this.cierre_factura_fin=cierre_factura_fin;
        this.mone_fact=nego.getMonedaFacturacion();
        this.nego=nego;
        this.sucu_corr=sucu_corr;
        this.lstDiasSinServicio=lstDiasSinServicio;
        this.calcularPeriodosReales();
    }
    
    
    
    private void calcularPeriodosReales(){
        //Proceso que permite calcular los periodos reales entre
        //los normales(cierre_factura_inicio y cierre_factura_fin)
        //esto se realiza cruzando con la informacion de cortes y suspensiones que son (lstDiasSinServicio)        
        this.cierre_factura_inicio_real=this.cierre_factura_inicio;
        this.cierre_factura_fin_real=this.cierre_factura_fin;
        int periodo_facturacion=this.nego.getCO_PERI_FACT_ForJSON();
        if (periodo_facturacion==1){//mes a mes 
            if (this.lstDiasSinServicio.size()>0){//Si no tiene elementos sera lo mismo que las otras fechas
                Date fi=this.cierre_factura_inicio_real;
                Date ff=this.cierre_factura_fin_real;
                Integer ultimo_dia=FechaHora.getNumDaysOfMonth(FechaHora.getYear(this.cierre_factura_fin_real), FechaHora.getMonth(this.cierre_factura_fin_real));
                for(Date d[] : this.lstDiasSinServicio){
                    if(FechaHora.getDay(d[0])==1 && fi.getTime()<d[1].getTime())
                        fi=d[1];
                    if(FechaHora.getDay(d[1])==ultimo_dia && ff.getTime()>d[0].getTime())
                        ff=d[0];
                }
                this.cierre_factura_inicio_real=fi;
                this.cierre_factura_fin_real=ff;
            }
        }else{// trimestras, semestral anual
            
        }
    }    
    public double[] getTotales(){
        //Obtiene todo lo que va abajo de cada factura
        //NETO,AJUSTE SI AFECTO,IGV,AJUSTE NO AFECTO,TOTAL_RECIBO
        double igv=0.18;
        double ret[]=new double[]{0,0,0,0,0};
        boolean detraccion=this.isAfectoDetraccion();
        for (Map.Entry<NEGO_SUCU, List<ReciboGlosa>> entry : this.glosas.entrySet()){
            for(ReciboGlosa glosa:entry.getValue()){
                if (!(detraccion && glosa.isServicioSuplementario())){
                    ret[0]+=glosa.getMonto();
                }
            }
        }
        //leemos los ajustes
        this.IM_AJUS_NIGV=0d;
        this.IM_AJUS_SIGV=0d;
        //Quitamos promociones si las tubiera
        for(ReciboGlosa glosa : this.promociones){
            this.IM_AJUS_SIGV+=glosa.getMonto();
        }        
        for(ReciboGlosa glosa : this.ajustes){
            if (glosa.getETipoGlosa()==ETipoReciboGlosa.AJUSTES_AFECTOS_IGV){
                this.IM_AJUS_SIGV+=glosa.getMonto();
            }else{
                this.IM_AJUS_NIGV+=glosa.getMonto();
            }            
        }
                   
        ret[0]=Numero.redondear(ret[0],2);//Neto
        ret[1]=this.IM_AJUS_SIGV*-1;//AJUSTE SI AFECTO
        ret[2]=Numero.redondear((ret[0]+ret[1])*igv, 2);//IGV
        //calculamos el IM_AJUS_NIGV
        if (this.nego.getCO_MONE_FACT_ForJSON()==1){//soles
            //redondeo a 0.00 o 0.05
            Double tmp=Numero.redondear((ret[0] + ret[1] + ret[2] - this.IM_AJUS_NIGV)*10,2);
            Double tmpEnt=Math.floor(tmp);
            Double tmpDiff=Numero.redondear(tmp-tmpEnt,2);
            if (tmpDiff>=0.5d)
                this.IM_AJUS_NIGV+=Numero.redondear((tmpDiff-0.5d)/10,2);
            else
                this.IM_AJUS_NIGV+=Numero.redondear((tmpDiff)/10,2);
        }
        ret[3]=this.IM_AJUS_NIGV*-1;//AJUSTE NO AFECTO        
        ret[4]=Numero.redondear(ret[0]+ret[1]+ret[2]+ret[3], 2);//TOTAL_RECIBO
        return ret;
    }
 
    public void addGlosa(NEGO_SUCU nego_sucu,ReciboGlosa g){        
        List<ReciboGlosa> lst=this.glosas.get(nego_sucu);
        if (lst==null){
            lst=new ArrayList<ReciboGlosa>();
            this.glosas.put(nego_sucu, lst);
        }
        lst.add(g);
    }
    
    @Override
    public String toString(){
        String msj="Recibo n\u00famero : "+this.numero+"\r\n";
        for (Map.Entry<NEGO_SUCU, List<ReciboGlosa>> entry : this.glosas.entrySet()){
            NEGO_SUCU nego_sucu=entry.getKey();
            List<ReciboGlosa> lst=entry.getValue();
            SUCU sucu=nego_sucu.getSUCU();
            msj+=" Sucursal "+sucu.getDist_ForJSON()+"\r\n";
            for(ReciboGlosa g: lst){
                msj+="  "+g.toString()+"\r\n";
            }            
        }
        /*

        */
        return msj;
    }
    
    public String getPeriodoString(){
        if (this.reci==null){
            //Crea el periodo en cadena que va en la factura        
            if (lstDiasSinServicio.size()>0){
                String cadena="Periodo ";
                //cadena+="del "+FechaHora.getStringDateShortStringMonth(cierre_factura_inicio_real);
                //cadena+=" al "+FechaHora.getStringDateShortStringMonth(cierre_factura_fin_real);

                Boolean conFinal=true;
                Long long_fin_periodo=cierre_factura_fin_real.getTime();
                Long long_inicio_periodo=cierre_factura_inicio_real.getTime();
                Long long_fin_inicio=lstDiasSinServicio.get(0)[1].getTime();
                if (long_inicio_periodo<long_fin_inicio)
                    cadena+="del "+FechaHora.getStringDateShortStringMonth(cierre_factura_inicio_real);
                for(Date d[]:lstDiasSinServicio){  
                    if (long_inicio_periodo<long_fin_inicio)
                        cadena+=" al "+FechaHora.getStringDateShortStringMonth(FechaHora.addDays(d[0],-1));


                    if (d[1].getTime()<long_fin_periodo)//la siguiente fecha tiene que ser menor que el fin de periodo si no fuese asi ya no seguir interando
                        cadena+=", del "+FechaHora.getStringDateShortStringMonth(FechaHora.addDays(d[1],1));
                    else{
                        conFinal=false;
                        break;
                    }
                }
                if (conFinal)
                    cadena+=" al "+FechaHora.getStringDateShortStringMonth(cierre_factura_fin_real);
                return cadena;
            }
            return "Periodo del "+FechaHora.getStringDateShortStringMonth(cierre_factura_inicio_real)+" al "+FechaHora.getStringDateShortStringMonth(cierre_factura_fin_real);
        }else{
            return this.reci.getDE_PERI_ForJSON();
        }
    }

    public Date getCierre_Factura_Inicio() {
        return cierre_factura_inicio;
    }

    public Date getCierre_Factura_Fin() {
        return cierre_factura_fin;
    }

    public Map<NEGO_SUCU,List<ReciboGlosa>> getGlosas() {
        return glosas;
    }
    
    private void unirPeriodo(Recibo f){
        if (this.nego.getCO_PERI_FACT_ForJSON()==1){//mes a mes
            //Regla de negocio puesta por leslie 08-junio-2015
            //Si tiene mas de dos servicios debe poner como periodo inicio el primer dia del mes
            //y fin el ultimo dia del mes
            //this.cierre_factura_inicio=FechaHora.getPrimeraFechaMes(cierre_factura_inicio);
            //this.cierre_factura_fin=FechaHora.getUltimaFechaMes(cierre_factura_fin);
            
            //Se volvio a cambiar la regla 21 07 2015
            //Se debe tomar como fecha de inicio la menor
            if (f.cierre_factura_inicio.getTime()<this.cierre_factura_inicio.getTime())
                this.cierre_factura_inicio=f.cierre_factura_inicio;            

            this.calcularPeriodosReales();
        }
    }
    
    public void unir(Recibo f){
        /*for(ReciboGlosa g:f.getGlosas()){
            this.glosas.add(g);
        }*/ 
        if (f.equals(this)) return;
        this.unirPeriodo(f);
        for (Map.Entry<NEGO_SUCU, List<ReciboGlosa>> entry : f.glosas.entrySet()){
            NEGO_SUCU nego_sucu=entry.getKey();
            List<ReciboGlosa> lst=entry.getValue();            
            List<ReciboGlosa> thisLst=this.glosas.get(nego_sucu);
            
            if(thisLst==null){
                thisLst=new ArrayList<ReciboGlosa>();
                this.glosas.put(nego_sucu, thisLst);
            }
            for(ReciboGlosa g:lst){
                thisLst.add(g);
            }
        }
    }

    public Long getNumero() {
        if (this.reci==null){
            if (this.numero==null)
                return 0L;
            return numero;
        }else{
            return this.reci.getCO_RECI_ForJSON();
        }
    }

    public void setNumero(Long numero) {
        this.numero = numero;
    }

    public NEGO getNego() {
        return nego;
    }

    public CLIE getClie() {
        return clie;
    }

    public SUCU getSucu_Corr() {
        return sucu_corr;
    }

    public CIER getCier(){
        return this.cier;
    }
    public MONE_FACT getMone_Fact(){
        return this.mone_fact;
    }    
    public String getNombreFileReciboPdf() {
        Date fecha=this.cierre_factura_fin;
        return this.getNumero()+"-"+this.nego.getCO_NEGO_ForJSON()+"-"+FechaHora.getYear(fecha)+"-"+(FechaHora.getMonth(fecha)+1);
    }
    
    public void saveBD(HttpServletRequest request){
        CLIE clie=this.getClie();
        SUCU sucu_instalacion=glosas.entrySet().iterator().next().getKey().getSUCU();
        
        RECI f=new RECI();
        double totales[]=this.getTotales();//NETO,AJUSTE SI AFECTO,IGV,AJUSTE NO AFECTO,TOTAL_RECIBO
        Boolean agregar_glosa=false;
        f.setCO_RECI(this.numero);
        f.setCO_CIER(this.cier.getCO_CIER_ForJSON());
        f.setCO_NEGO(this.nego.getCO_NEGO_ForJSON());
        f.setDE_PERI(this.getPeriodoString());
        f.setIM_NETO(totales[0]);
        f.setIM_IGV(totales[2]);
        f.setIM_TOTA(totales[4]);
        
        f.setIM_AJUS_SIGV(totales[1]);
        f.setIM_AJUS_NIGV(totales[3]);
        f.setIM_INST(this.getMontoInstalacion());
        f.setIM_SERV_MENS(this.getMontoServicioMensual());
        f.setIM_ALQU(this.getMontoAlquiler());
        f.setIM_OTRO(this.getMontoOtros());
        f.setIM_DESC(this.getMontoDescuentos());
        
        /*Historico*/
        f.setFE_EMIS(this.cier.getFE_EMIS_ForJSON());
        f.setFE_VENC(this.cier.getFE_VENC_ForJSON());
        f.setCO_CLIE(clie.getCO_CLIE_ForJSON());
        f.setDE_CODI_BUM(clie.getDE_CODI_BUM_ForJSON());
        f.setNO_CLIE(clie.getFullNameCliente());
        f.setCO_DIST_FISC(this.sucu_fiscal.getCO_DIST_ForJSON());
        f.setDE_DIRE_FISC(this.sucu_fiscal.getDE_DIRE_ForJSON());
        f.setCO_TIPO_DOCU(tipo_docu.getCO_TIPO_DOCU_ForJSON());
        f.setDE_NUME_DOCU(clie.getDE_NUME_DOCU_ForJSON());
        f.setDE_DIRE_CORR(this.sucu_corr.getDE_DIRE_ForJSON());
        f.setCO_DIST_CORR(this.sucu_corr.getCO_DIST_ForJSON());
        f.setCO_DIST_INST(sucu_instalacion.getCO_DIST_ForJSON());
        f.setDE_DIRE_INST(sucu_instalacion.getDE_DIRE_ForJSON());
        f.setCO_MONE_FACT(this.mone_fact.getCO_MONE_FACT_ForJSON());
        f.setNO_MONE_FACT(this.mone_fact.getNO_MONE_FACT_ForJSON());
        
        f.setNO_DIST_FISC(this.sucu_fiscal.getDistrito().getNO_DIST_ForJSON());
        f.setNO_DIST_CORR(this.sucu_corr.getDistrito().getNO_DIST_ForJSON());
        f.setNO_DIST_INST(sucu_instalacion.getDistrito().getNO_DIST_ForJSON());
        f.setNO_TIPO_DOCU(tipo_docu.getNO_TIPO_DOCU_ForJSON());
        f.setDE_SIMB_MONE_FACT(this.mone_fact.getDE_SIMB_ForJSON());
        
        Historial.initInsertar(request, f);
        
        f.save();
        
        for(Entry<NEGO_SUCU,List<ReciboGlosa>> entry : glosas.entrySet()){
            NEGO_SUCU nego_sucu=entry.getKey();
            List<ReciboGlosa> lista=entry.getValue();
            for(ReciboGlosa glosa : lista){
                agregar_glosa=true;
                
                if (glosa.isAfectoDetraccion() && this.isAfectoDetraccion()){
                    agregar_glosa=false;                 
                }
                
                if (agregar_glosa){                    
                    RECI_GLOS g=new RECI_GLOS();
                    g.setCO_RECI(f.getCO_RECI_ForJSON());
                    g.setCO_NEGO_SUCU(nego_sucu.getCO_NEGO_SUCU_ForJSON());
                    g.setIM_MONT(glosa.getMonto());
                    g.setNO_GLOS(glosa.getNombre());
                    g.setTI_GLOS(glosa.getTipoGlosa());

                    Historial.initInsertar(request, g);
                    
                    g.save();
                }
            }
        }
        
    }

    private Double getMontoServicioMensual() {
        Double ret=0.0;
        for(Entry<NEGO_SUCU,List<ReciboGlosa>> entry : glosas.entrySet()){
            List<ReciboGlosa> lista=entry.getValue();
            for(ReciboGlosa glosa : lista){
                if (!glosa.getNombre().toLowerCase().contains("alquiler")){
                    ret+=glosa.getMonto();
                }
            }            
        }
        return ret;
    }

    private Double getMontoAlquiler() {
        Double ret=0.0;
        for(Entry<NEGO_SUCU,List<ReciboGlosa>> entry : glosas.entrySet()){
            List<ReciboGlosa> lista=entry.getValue();
            for(ReciboGlosa glosa : lista){
                if (glosa.getNombre().toLowerCase().contains("alquiler")){
                    ret+=glosa.getMonto();
                }
            }            
        }
        return ret;
    }
    
    private Double getMontoInstalacion() {
        Double ret=0.0;
        for(Entry<NEGO_SUCU,List<ReciboGlosa>> entry : glosas.entrySet()){
            List<ReciboGlosa> lista=entry.getValue();
            for(ReciboGlosa glosa : lista){
                if (glosa.getNombre().toLowerCase().contains("instalaci")){
                    ret+=glosa.getMonto();
                }
            }            
        }
        return ret;
    }
    
    private Double getMontoOtros() {
        return 0.0;
    }

    private Double getMontoDescuentos() {
        Double ret=0d;
        for(ReciboGlosa rg : this.promociones){
            ret+=rg.getMonto();
        }        
        return ret;
    }
    
    public Integer getCodigoCierreRecibo(){
        /*
        Permite saber el codigo del cierre de la factura, esto es solo
        para el cobro mes a mes periodo=1
        Ejemplo:
        CFI:12/01/15,CFF:28/02/15 --> retorna 201502 (2015 Feb)        
        CFI:18/02/15,CFF:31/03/15 --> retorna 201503 (2015 Mar)        
        CFI:01/03/15,CFF:31/03/15 --> retorna 201503 (2015 Mar)        
        */
        if (this.nego.getCO_PERI_FACT_ForJSON().equals(1)){//mes a mes
           return FechaHora.getYear(this.cierre_factura_fin)*100+(FechaHora.getMonth(this.cierre_factura_fin)+1);
        }else{
           return FechaHora.getYear(this.cierre_factura_fin)*100+(FechaHora.getMonth(this.cierre_factura_fin)+1); 
        }
        //return 0;
    }
    
    public Boolean isAfectoDetraccion(){
        /*La detraccion es por recibo, no por la lista de recibos*/
        /*si es mayor a 700 es un recibo con detraccion*/
        if (this.AfectoDetraccion==null){                    
            Integer CO_MONE_FACT=null;
            Double TIPO_CAMBIO=null;
            if (this.reci==null){
                CO_MONE_FACT=this.nego.getCO_MONE_FACT_ForJSON();
                TIPO_CAMBIO=this.cier.getNU_TIPO_CAMB_ForJSON();
            }else{
                CO_MONE_FACT=this.reci.getCO_MONE_FACT_ForJSON();
                TIPO_CAMBIO=this.reci.getCierre().getNU_TIPO_CAMB_ForJSON();
            }
            
            Double monto=0.0;
            for(Entry<NEGO_SUCU,List<ReciboGlosa>> entry : glosas.entrySet()){
                List<ReciboGlosa> lista=entry.getValue();
                for(ReciboGlosa glosa : lista){
                    if (glosa.isAfectoDetraccion()){
                        monto+=glosa.getMonto();
                    }
                }
            }
            
            if (CO_MONE_FACT==2)//dolares multiplcamos por el factor del cierre
                monto*=TIPO_CAMBIO;            
            
            this.AfectoDetraccion = monto>700.0; 
        }
        return this.AfectoDetraccion;
    }

    void si_es_total_negativo_volverlo_cero() {
        if(this.getTotales()[0]<0){
            double suma_positivos=0;
            //si el total es menor a cero entonces al recibo lo volvemos cero
            for (Map.Entry<NEGO_SUCU, List<ReciboGlosa>> entry : this.glosas.entrySet()){
                for(ReciboGlosa rg : entry.getValue()){
                    if (rg.getMonto()>0)
                        suma_positivos+=rg.getMonto();
                }
            }
            //verificamos uno a una las glosas negativas 
            double queda=suma_positivos;
            for (Map.Entry<NEGO_SUCU, List<ReciboGlosa>> entry : this.glosas.entrySet()){
                for(ReciboGlosa rg : entry.getValue()){
                    if (rg.getMonto()<0){
                        if ((queda+rg.getMonto())<0){
                            rg.setMonto(queda*-1d);
                        }else{
                            queda+=rg.getMonto();
                        }
                    }                        
                }
            }
            //promociones
            for(ReciboGlosa rg : this.promociones){
                if (rg.getMonto()<0){
                    if ((queda+rg.getMonto())<0){
                        rg.setMonto(queda*-1d);
                    }else{
                        queda+=rg.getMonto();
                    }
                }                  
            }
        }
    }
    
    public RECI getRECI(){
        return this.reci;
    }

    public Double getIM_AJUS_SIGV() {
        return IM_AJUS_SIGV;
    }

    public void setIM_AJUS_SIGV(Double IM_AJUS_SIGV) {
        this.IM_AJUS_SIGV = IM_AJUS_SIGV;
    }
    
    public Double getIM_AJUS_NIGV() {
        return IM_AJUS_NIGV;
    }

    public void setIM_AJUS_NIGV(Double IM_AJUS_NIGV) {
        this.IM_AJUS_NIGV = IM_AJUS_NIGV;
    }    
    
    
    public void addPromocion(ReciboGlosa glosa){
        promociones.add(glosa);
    }

    public void addAjuste(ReciboGlosa glosa){
        ajustes.add(glosa);
    }    
    
    public List<ReciboGlosa> getPromociones(){
        return this.promociones;
    }
    
    public List<ReciboGlosa> getAjustes(){
        return this.ajustes;
    }    
    
    
    
    public Double getTotalPromociones(){
        Double ret=0d;
        for(ReciboGlosa glosa  : this.promociones){
            ret+=glosa.getMonto();
        }
        return ret;
    }
    
    public Integer getCantidadSucursalesInvolucradas(){
        Integer ret=null;
        if (this.glosas!=null){             
            ret=0;
            for(Entry<NEGO_SUCU,List<ReciboGlosa>> entry : this.glosas.entrySet()){
                if (entry.getKey()!=null){
                    ret+=1;
                }
            }
            
        }
        return ret;
    }
    
    public List<Object[]> getDetalleShortServicios(){
        //retorna glosa,cantidad de veces repite, monto
        List<Object[]>ret=new ArrayList<Object[]>();        
        if (this.glosas!=null){  
            Map<ETipoReciboGlosa,Map<String,Double[]>> iguales=new TreeMap<ETipoReciboGlosa, Map<String,Double[]>>();
            
            iguales.put(ETipoReciboGlosa.PLAN,new TreeMap<String, Double[]>());
            iguales.put(ETipoReciboGlosa.SERVICIO_SUPLEMENTARIO,new TreeMap<String, Double[]>());
            iguales.put(ETipoReciboGlosa.COBRO_ADICIONAL_UPGRADE,new TreeMap<String, Double[]>());
            
            String key=null;

            for(Entry<NEGO_SUCU,List<ReciboGlosa>> entry : this.glosas.entrySet()){
                if (entry.getKey()!=null){
                    for (ReciboGlosa glosa : entry.getValue()){
                        for(Entry<ETipoReciboGlosa,Map<String,Double[]>>  entryIguales : iguales.entrySet()){
                            if (entryIguales.getKey().equals(glosa.getETipoGlosa())){
                                Map<String,Double[]> map=entryIguales.getValue();
                                if (!map.containsKey(glosa.getNombre())){
                                    map.put(glosa.getNombre(), new Double[]{0d,0d});
                                }
                                Double data[]=map.get(glosa.getNombre());
                                data[0]++;
                                data[1]+=glosa.getMonto();
                            }
                        }                        
                    }
                }
            } 
            
            for(Entry<ETipoReciboGlosa,Map<String,Double[]>>  entryIguales : iguales.entrySet()){
                Map<String,Double[]> map=entryIguales.getValue();
                for(Entry<String,Double[]> entry_values :map.entrySet()  ){
                    ret.add(new Object[]{entry_values.getKey(),entry_values.getValue()[0],entry_values.getValue()[1]});
                }
                
            }
        }
        return ret;
    }
    
    
    public List<Object[]> getDetalleShortAfectosIgv(){
        //retorna glosa,cantidad de veces repite, monto
        List<Object[]>ret=new ArrayList<Object[]>();        
        if (this.glosas!=null){  
            Map<ETipoReciboGlosa,Map<String,Double[]>> iguales=new TreeMap<ETipoReciboGlosa, Map<String,Double[]>>();
            iguales.put(ETipoReciboGlosa.DEVOLUCION_CARGOS_FIJOS_SALDO,new TreeMap<String, Double[]>());
            iguales.put(ETipoReciboGlosa.DEVOLUCION_POR_CORTE_O_SUSPENSION_SERVICIO,new TreeMap<String, Double[]>());
            iguales.put(ETipoReciboGlosa.DEVOLUCION_POR_DESACTIVACION,new TreeMap<String, Double[]>());
            iguales.put(ETipoReciboGlosa.AJUSTES_AFECTOS_IGV,new TreeMap<String, Double[]>());
            iguales.put(ETipoReciboGlosa.PROMOCIONES_MONTO,new TreeMap<String, Double[]>());
            iguales.put(ETipoReciboGlosa.PROMOCIONES_PORCENTAJE,new TreeMap<String, Double[]>());            
            String key=null;
            Double val[]=null;
            //glosas
            for (Map.Entry<NEGO_SUCU, List<ReciboGlosa>> entry : this.glosas.entrySet()){
                for(ReciboGlosa glosa : entry.getValue()){           
                    if (    glosa.getETipoGlosa()==ETipoReciboGlosa.DEVOLUCION_CARGOS_FIJOS_SALDO || 
                            glosa.getETipoGlosa()==ETipoReciboGlosa.DEVOLUCION_POR_CORTE_O_SUSPENSION_SERVICIO ||
                            glosa.getETipoGlosa()==ETipoReciboGlosa.DEVOLUCION_POR_DESACTIVACION
                        ){
                        Map<String,Double[]> map=iguales.get(glosa.getETipoGlosa());
                        if (!map.containsKey(glosa.getNombre())){
                            map.put(glosa.getNombre(), new Double[]{0d,0d});
                        }
                        val=map.get(glosa.getNombre());
                        val[0]++;
                        val[1]+=glosa.getMonto();                        
                    }
                }            
            }             
            
            //ajustes
            for(ReciboGlosa glosa : this.getAjustes()){           
                if (glosa.getETipoGlosa()==ETipoReciboGlosa.AJUSTES_AFECTOS_IGV){
                    Map<String,Double[]> map=iguales.get(glosa.getETipoGlosa());
                    if (!map.containsKey(glosa.getNombre())){
                        map.put(glosa.getNombre(), new Double[]{0d,0d});
                    }
                    val=map.get(glosa.getNombre());
                    val[0]++;
                    val[1]+=glosa.getMonto();
                }
            }            
            //promociones
            Double tmp_total_promocion=0d;
            String etiqueta="Descuento por promoci\u00f3n";
            for(ReciboGlosa glosa : this.getPromociones()){
                if (glosa.getETipoGlosa()==ETipoReciboGlosa.PROMOCIONES_MONTO || glosa.getETipoGlosa()==ETipoReciboGlosa.PROMOCIONES_PORCENTAJE){
                    Map<String,Double[]> map=iguales.get(glosa.getETipoGlosa());
                    if (!map.containsKey(etiqueta)){
                        map.put(etiqueta, new Double[]{0d,0d});
                    }
                    val=map.get(etiqueta);
                    val[0]++;
                    val[1]+=glosa.getMonto();
                }
                
                tmp_total_promocion+=glosa.getMonto();
            }

            for(Entry<ETipoReciboGlosa,Map<String,Double[]>>  entryIguales : iguales.entrySet()){
                Map<String,Double[]> map=entryIguales.getValue();
                for(Entry<String,Double[]> entry_values :map.entrySet()  ){
                    ret.add(new Object[]{entry_values.getKey(),entry_values.getValue()[0],entry_values.getValue()[1]});
                }
                
            }
                        
        }
        return ret;
    }
    
    
    public List<Object[]> getDetalleShortNoAfectosIgv(){
        //retorna glosa,cantidad de veces repite, monto
        List<Object[]>ret=new ArrayList<Object[]>();        
        if (this.glosas!=null){  
            Map<ETipoReciboGlosa,Map<String,Double[]>> iguales=new TreeMap<ETipoReciboGlosa, Map<String,Double[]>>();
            
            iguales.put(ETipoReciboGlosa.AJUSTES_NO_AFECTOS_IGV,new TreeMap<String, Double[]>());            
            String key=null;
            Double val[]=null;
            for(ReciboGlosa glosa : this.getAjustes()){           
                if (glosa.getETipoGlosa()==ETipoReciboGlosa.AJUSTES_NO_AFECTOS_IGV){
                    Map<String,Double[]> map=iguales.get(glosa.getETipoGlosa());
                    if (!map.containsKey(glosa.getNombre())){
                        map.put(glosa.getNombre(), new Double[]{0d,0d});
                    }
                    val=map.get(glosa.getNombre());
                    val[0]++;
                    val[1]+=glosa.getMonto();
                }
            }            
            
            for(Entry<ETipoReciboGlosa,Map<String,Double[]>>  entryIguales : iguales.entrySet()){
                Map<String,Double[]> map=entryIguales.getValue();
                for(Entry<String,Double[]> entry_values :map.entrySet()  ){
                    ret.add(new Object[]{entry_values.getKey(),entry_values.getValue()[0],entry_values.getValue()[1]});
                }
                
            }
                        
        }
        return ret;
    }    
    
    
}
