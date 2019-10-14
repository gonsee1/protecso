/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.cores;

import com.americatel.facturacion.fncs.FechaHora;
import com.americatel.facturacion.models.AJUS;
import com.americatel.facturacion.models.CIER;
import com.americatel.facturacion.models.CLIE;
import com.americatel.facturacion.models.CORT;
import com.americatel.facturacion.models.NEGO;
import com.americatel.facturacion.models.NEGO_PROM;
import com.americatel.facturacion.models.NEGO_SALD;
import com.americatel.facturacion.models.NEGO_SUCU;
import com.americatel.facturacion.models.NEGO_SUCU_PLAN;
import com.americatel.facturacion.models.NEGO_SUCU_PROM;
import com.americatel.facturacion.models.PROD;
import com.americatel.facturacion.models.SUCU;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author crodas
 */
public class NegocioFacturar {
    private CIER cier=null;
    private PROD prod=null;
    private NEGO nego=null;    
    private List<CORT> cortes=null;
    private CLIE clie=null;
    private SUCU sucu_corr=null;
    private Double nego_sald=null;
    
    private LstRecibo lstRecibo=null;
    private Factura factura=null;
    private HttpServletRequest request=null;
    
    private Double total_promociones=0d;
      
    private LstMotivoNoFactura lstMotivoNoFactura=new LstMotivoNoFactura();
    
    
    public EMotivoNoFactura getMotivoNoFactura(){
        return this.lstMotivoNoFactura.getMotivoNoFactura();
    }    
    
    public NegocioFacturar(CIER cier,PROD prod,NEGO nego,HttpServletRequest request){
        this.prod=prod;
        this.cier=cier;        
        this.nego=nego;
        this.lstRecibo=new LstRecibo();
        this.cortes=this.nego.getCortesPendientes();
        this.clie=this.nego.getClie();
        this.sucu_corr=this.nego.getSucursalCorrespondencia();
        this.factura=new Factura(this.cier,this.clie,this.nego);
        this.nego_sald=this.nego.getSaldoNegocio(this.cier);
        this.request=request;
    }
    
    public void facturar(Boolean preview){  
        Logs.printMsg("****************************************************************************************************",0,!preview,this.cier,this.nego);
        Logs.printMsg("Facturando Negocio "+this.nego.getCO_NEGO_ForJSON()+", Moneda: "+this.nego.getStringMonedaFacturacion()+", Periodo: "+this.nego.getStringPeriodoFacturacion()+", Tipo: "+this.nego.getStringTipoFacturacion(),1,!preview,this.cier,this.nego);
        List<NEGO_SUCU> sucursales= this.nego.getSucursales();
        NEGO_SUCU nego_sucu=null;
        Double saldo=null;
        for(int i=0;i<sucursales.size();i++){
            nego_sucu=sucursales.get(i);
            SucursalFacturar sf=new SucursalFacturar(this.cier,this.prod,this.clie,this.nego,this.sucu_corr,this.cortes,nego_sucu);
            LstRecibo lst=sf.facturar(preview);
            this.lstMotivoNoFactura.addMotivosNoFactura(sf.getMotivoNoFactura());
            this.lstRecibo.unir(lst);            
        }           
        this.calcularFactura();//va antes ya que las promociones no aplican a detraciones
        this.calcularPromociones();
        saldo=this.calcularSaldos(preview);                
        this.removeRecibosTodosMontoCero();
        this.removeGlosasPlanesDesactivadoAntesUltimaFacturacion();
        this.removeReciboSinGlosas();
        this.calcularAjustes();
        if (saldo!=null && !preview ){
            NEGO_SALD nego_sald=new NEGO_SALD();
            nego_sald.setCO_CIER(this.cier.getCO_CIER_ForJSON());
            nego_sald.setCO_NEGO(this.nego.getCO_NEGO_ForJSON());
            nego_sald.setIM_MONT(saldo);
            nego_sald.setDE_SALD("Queda saldo pendiente por cierre de negocio");            
            nego_sald.insertar();
            Logs.printMsg("Regitro de nota de credito valor "+saldo,5,!preview,this.cier,this.nego);            
        }        
    }

    private void calcularPromociones(){
        Recibo rec=this.getReciboDelPeriodo();
        this.total_promociones=0d;
        if(rec!=null){
            List<ReciboGlosa> glosas_agregar=new ArrayList<ReciboGlosa>();
            List<NEGO_PROM> nego_promociones_procentaje=this.nego.getPromocionesPorcentajePendientes(this.cier);
            List<NEGO_PROM> nego_promociones_monto=this.nego.getPromocionesMontoPendientes(this.cier);
            if (nego_promociones_procentaje.size()>0 || nego_promociones_monto.size()>0){               
                for(Map.Entry<NEGO_SUCU,List<ReciboGlosa>> entry : rec.getGlosas().entrySet()){
                    for (ReciboGlosa glosa : entry.getValue()){
                        //no aplican promocion para los cobros unicos y servicios suplementarios afectos a detraccion
                        if (!((glosa.isServicioSuplementario() && glosa.getServ_supl().getST_AFEC_DETR_ForJSON()) || glosa.isServicioUnico())){                            
                            for (NEGO_PROM prom : nego_promociones_procentaje){
                                if (prom.getST_PLAN_ForJSON() && glosa.isPlan()){
                                    Double porcentaje=prom.getIM_VALO_ForJSON()/100;
                                    Double monto_promo=porcentaje*glosa.getMonto();
                                    this.total_promociones+=monto_promo;
                                    ReciboGlosa new_glosa=new ReciboGlosa("Promoci\u00f3n por "+glosa.getPlan().getNO_PLAN_ForJSON(),monto_promo,ETipoReciboGlosa.PROMOCIONES_PORCENTAJE,glosa.getPlan());
                                    glosas_agregar.add(new_glosa);
                                }else if (prom.getST_PLAN_ForJSON() && glosa.isServicioSuplementario()){
                                    Double porcentaje=prom.getIM_VALO_ForJSON()/100;
                                    Double monto_promo=porcentaje*glosa.getMonto();
                                    this.total_promociones+=monto_promo;
                                    ReciboGlosa new_glosa=new ReciboGlosa("Promoci\u00f3n por "+glosa.getServ_supl().getNO_SERV_SUPL_ForJSON(),monto_promo,ETipoReciboGlosa.PROMOCIONES_PORCENTAJE,glosa.getServ_supl());
                                    glosas_agregar.add(new_glosa);
                                }
                            }
                        }
                    }                    
                }
                for (NEGO_PROM prom : nego_promociones_monto){
                    Double monto_promo=prom.getIM_VALO_ForJSON();
                    this.total_promociones+=monto_promo;
                    ReciboGlosa new_glosa=new ReciboGlosa("Promoci\u00f3n por Monto",monto_promo,ETipoReciboGlosa.PROMOCIONES_MONTO);
                    glosas_agregar.add(new_glosa);
                }                 
            }else{
                for(Map.Entry<NEGO_SUCU,List<ReciboGlosa>> entry : rec.getGlosas().entrySet()){
                    NEGO_SUCU nego_sucu=entry.getKey();
                    List<NEGO_SUCU_PROM> nego_sucu_promociones_procentaje=nego_sucu.getPromocionesPorcentajePendientes(this.cier);
                    List<NEGO_SUCU_PROM> nego_sucu_promociones_monto=nego_sucu.getPromocionesMontoPendientes(this.cier);
                    if (nego_sucu_promociones_procentaje.size()>0 || nego_sucu_promociones_monto.size()>0){
                        for (ReciboGlosa glosa : entry.getValue()){
                            //no aplican promocion para los cobros unicos y servicios suplementarios afectos a detraccion
                            if (!((glosa.isServicioSuplementario() && glosa.getServ_supl().getST_AFEC_DETR_ForJSON()) || glosa.isServicioUnico())){                            
                                for (NEGO_SUCU_PROM prom : nego_sucu_promociones_procentaje){
                                    if (prom.getST_PLAN_ForJSON() && glosa.isPlan()){
                                        Double porcentaje=prom.getIM_VALO_ForJSON()/100;
                                        Double monto_promo=porcentaje*glosa.getMonto();
                                        this.total_promociones+=monto_promo;
                                        ReciboGlosa new_glosa=new ReciboGlosa("Promoci\u00f3n por "+glosa.getPlan().getNO_PLAN_ForJSON(),monto_promo,ETipoReciboGlosa.PROMOCIONES_PORCENTAJE,glosa.getPlan());
                                        glosas_agregar.add(new_glosa);
                                    }else if (prom.getST_PLAN_ForJSON() && glosa.isServicioSuplementario()){
                                        Double porcentaje=prom.getIM_VALO_ForJSON()/100;
                                        Double monto_promo=porcentaje*glosa.getMonto();
                                        this.total_promociones+=monto_promo;
                                        ReciboGlosa new_glosa=new ReciboGlosa("Promoci\u00f3n por "+glosa.getServ_supl().getNO_SERV_SUPL_ForJSON(),monto_promo,ETipoReciboGlosa.PROMOCIONES_PORCENTAJE,glosa.getServ_supl());
                                        glosas_agregar.add(new_glosa);
                                    }
                                }
                            }                            
                        }
                    }
                    
                    for (NEGO_SUCU_PROM prom : nego_sucu_promociones_monto){
                        Double monto_promo=prom.getIM_VALO_ForJSON();
                        this.total_promociones+=monto_promo;
                        ReciboGlosa new_glosa=new ReciboGlosa("Promoci\u00f3n por Monto",monto_promo,ETipoReciboGlosa.PROMOCIONES_MONTO);
                        glosas_agregar.add(new_glosa);
                    }
                }
            }
            for (ReciboGlosa r : glosas_agregar){
                rec.addPromocion(r);
            }            
        }
        
    }
    
    private Double calcularSaldos(Boolean preview){
        //Verifica los recibos con totales negativos
        Double retornar=null;
        //double total_positivo=0d;
        double total_negativo=0d;
        List<Integer> positivos=new ArrayList<Integer>();
        List<Integer> negativos=new ArrayList<Integer>();
        Integer contador=0;
        List<Recibo> lst=this.lstRecibo.getLst();
        double total_sobra=0d;
        for(Recibo rec : lst){
            double total_sin_igv = rec.getTotales()[0];
            if (total_sin_igv!=0){
                if (total_sin_igv>0){
                    //total_positivo+=total_sin_igv;
                    positivos.add(contador);
                }else{
                    total_negativo+=total_sin_igv;
                    negativos.add(contador);
                }
            }
            contador++;
        }
        
        //busamos si tiene saldo en la BD si lo tiene primero descontamos
        //los que se descuentan se guardan en la BD                
        if (this.nego_sald<0d){//Hay un saldo en BD
            total_sobra=this.nego_sald;
            for(Integer positivo : positivos){
                Recibo rec=lst.get(positivo);
                NEGO_SUCU nego_sucu=rec.getGlosas().entrySet().iterator().next().getKey();
                double tmp_total_sin_igv=rec.getTotales()[0];//total sin igv
                double devolver=0;
                if (tmp_total_sin_igv>Math.abs(total_sobra)){
                    devolver=total_sobra;
                    total_sobra=0d;                    
                }else{
                    devolver=tmp_total_sin_igv*-1d;
                    total_sobra=tmp_total_sin_igv+total_sobra;                    
                }
                /*
                    Registrar positivo que es el valor de devolver
                */
                if(!preview){
                    //CODE
                    Double uso=devolver * -1d;
                    NEGO_SALD saldo=new NEGO_SALD();
                    saldo.setCO_CIER(this.cier.getCO_CIER_ForJSON());
                    saldo.setCO_NEGO(this.nego.getCO_NEGO_ForJSON());
                    saldo.setDE_SALD("Regitro uso de saldo de nota de credito valor "+uso);
                    saldo.setIM_MONT(devolver*-1d);//se vuelve positivo
                    saldo.insertar();
                    Logs.printMsg("Regitro uso de saldo de nota de credito valor "+uso,5,!preview,this.cier,this.nego);  
                }
                rec.addGlosa(nego_sucu,new ReciboGlosa("Devoluci\u00f3n de cargos fijos", devolver, ETipoReciboGlosa.DEVOLUCION_CARGOS_FIJOS_SALDO));
                //no hay nada que restar
                if (total_sobra==0)
                    break;
            } 
            //volvemos a calcular las posiciones de los positivos y negativos
            positivos=new ArrayList<Integer>();
            negativos=new ArrayList<Integer>();
            contador=0;
            total_negativo=0d;
            for(Recibo rec : lst){
                double total_sin_igv = rec.getTotales()[0];
                if (total_sin_igv!=0){
                    if (total_sin_igv>0){
                        //total_positivo+=total_sin_igv;
                        positivos.add(contador);
                    }else{
                        total_negativo+=total_sin_igv;
                        negativos.add(contador);
                    }
                }
                contador++;
            }            
        }
        
        //registramos la glosa negativa si tiene alguna factura positiva
        if (total_negativo<0){
            total_sobra=total_negativo; 
            for(Integer positivo : positivos){
                Recibo rec=lst.get(positivo);
                NEGO_SUCU nego_sucu=rec.getGlosas().entrySet().iterator().next().getKey();
                double tmp_total_sin_igv=rec.getTotales()[0];//total sin igv
                double devolver=0;
                if (tmp_total_sin_igv>Math.abs(total_sobra)){
                    devolver=total_sobra;
                    total_sobra=0d;                    
                }else{
                    devolver=tmp_total_sin_igv*-1d;
                    total_sobra=tmp_total_sin_igv+total_sobra;                    
                }
                
                rec.addGlosa(nego_sucu,new ReciboGlosa("Devoluci\u00f3n de cargos fijos", devolver, ETipoReciboGlosa.DEVOLUCION_CARGOS_FIJOS_SALDO));
                //no hay nada que restar
                if (total_sobra==0)
                    break;
            }
            //si despues de agotar el negocio saldo de total_sobra aun queda
            if (total_sobra<0){              
                retornar=total_sobra;
            }
        }
        //a los recibos que son negativos los volvemos monto cero
        for(Integer negativo : negativos){
            Recibo rec=lst.get(negativo);
            rec.si_es_total_negativo_volverlo_cero();
        }
        
        //verificamos si el saldo generado corresponde a las promociones
        //si esto fuera asi descontamos las promociones
        //Ejm. si el saldo es -100 pero en promociones es -200 por tanto saldo es 0
        //Ejm. si el saldo es -80 pero en promociones es -50 por tando el saldo es -30
        if (retornar!=null && retornar<0){
            Recibo recibo=this.getReciboDelPeriodo();
            if (recibo!=null){
                Double tmp_total_promociones_no_aplicadas=this.total_promociones-recibo.getTotalPromociones();
                if (tmp_total_promociones_no_aplicadas<0){
                    if (tmp_total_promociones_no_aplicadas>retornar){
                        retornar-=tmp_total_promociones_no_aplicadas;
                    }else{
                        retornar=null;
                    }                       
                }

            }
        }
        return retornar;
    }
    

    private void calcularFactura(){        
        //permite verificar si este negocio genera factura
        String periodo=null;
        for(Recibo recibo : this.getLstRecibo().getLst()){            
            if (recibo.isAfectoDetraccion()){ 
                periodo=recibo.getPeriodoString();
                for(Map.Entry<NEGO_SUCU,List<ReciboGlosa>> entry : recibo.getGlosas().entrySet()){
                    for(ReciboGlosa rg : entry.getValue()){
                        //Si esta afecto a detraccion la glosa se genera la glosa a la Factura
                        if (rg.isAfectoDetraccion()){                             
                            this.factura.addGlosa(entry.getKey(), new FacturaGlosa(periodo, rg.getNombre(), rg.getMonto()));
                        }
                    }
                }
            }
        } 
    }

    private void removeRecibosTodosMontoCero(){
        //Se borrar todos los recibos que son monto cero a no ser que
        //el plan sea monto cero        
        List<Recibo> lst=this.lstRecibo.getLst();
        List<Integer> borrar=new ArrayList<Integer>();
        int contador=0;
        for(Recibo rec : lst){
            Boolean alguno_diferente_cero=false;
            for(Map.Entry<NEGO_SUCU,List<ReciboGlosa>> entry : rec.getGlosas().entrySet()){
                for(ReciboGlosa rg : entry.getValue()){
                    if (rec.isAfectoDetraccion() && rg.isAfectoDetraccion()){}else{
                        if (rg.getMonto()!=0d){
                           alguno_diferente_cero=true;
                           break;
                        }
                    }
                    //Si la glosa es un plan y el plan activo de la sucursal es monto cero
                    //no se debe eliminar este recibo
                    if (rg.getETipoGlosa()==ETipoReciboGlosa.PLAN){
                        NEGO_SUCU nego_sucu=entry.getKey();
                        NEGO_SUCU_PLAN plan=nego_sucu.getPlanActivo();   
                        if (plan.getIM_MONTO_ForJSON()==0d){
                            alguno_diferente_cero=true;
                            break;
                        }
                    }
                }
                if(alguno_diferente_cero)
                    break;
            }

            if(!alguno_diferente_cero){//Borrarmos recibo
                borrar.add(contador);
            }            
            contador++;
        }
        contador=0;
        for(Integer pos : borrar){            
            lst.remove(pos.intValue()-contador);
            contador++;
        }
    }
    
    private void removeGlosasPlanesDesactivadoAntesUltimaFacturacion(){
        /*
        Proceso que elimina las glosas si existen como DEVOLUCION_POR_DESACTIVACION
        y la suma de glosas en una sucursal es cero
        */
        List<Recibo> lst=this.lstRecibo.getLst();
        for(Recibo rec : lst){
            for(Map.Entry<NEGO_SUCU,List<ReciboGlosa>> entry : rec.getGlosas().entrySet()){
                Double suma_devolucion=0d;
                List<ReciboGlosa> borras_negativos=new ArrayList<ReciboGlosa>();
                for(ReciboGlosa rg : entry.getValue()){
                    if (rg.isPlan()){
                        if (rg.getETipoGlosa()==ETipoReciboGlosa.DEVOLUCION_POR_DESACTIVACION){
                            suma_devolucion+=rg.getMonto();
                            borras_negativos.add(rg);
                        }
                    }
                }
                List<ReciboGlosa> borras=new ArrayList<ReciboGlosa>();
                for(ReciboGlosa rg : entry.getValue()){
                    if (rg.isPlan()){
                        if (rg.getETipoGlosa()!=ETipoReciboGlosa.DEVOLUCION_POR_DESACTIVACION){
                            if (rg.getMonto()>0 && suma_devolucion<0){
                                borras.add(rg);
                                suma_devolucion+=rg.getMonto();
                            }
                        }
                    }
                }  
                if (suma_devolucion==0){
                    
                    for (ReciboGlosa borrar : borras ){
                        entry.getValue().remove(borrar);
                    }
                    
                    for (ReciboGlosa borrar : borras_negativos ){
                        entry.getValue().remove(borrar);
                    }                  
                    
                }
            }
        }
    }
    
    private void removeReciboSinGlosas(){
        List<Recibo> lst=this.lstRecibo.getLst();
        List<Recibo> borras=new ArrayList<Recibo>();
        for(Recibo rec : lst){
            int numero_glosas=0;
            for(Map.Entry<NEGO_SUCU,List<ReciboGlosa>> entry : rec.getGlosas().entrySet()){
                numero_glosas+=entry.getValue().size();
            }
            
            if (numero_glosas==0){
                borras.add(rec);
            }
        }
        
        for (Recibo borrar : borras){
            lst.remove(borrar);
        }
    }
    
    private void calcularAjustes(){
        List<AJUS> lista=this.nego.getAjustesPendientes(this.cier);
        if (!lista.isEmpty()){
            Double monto=this.nego.getTotalMontoAjustesPendientes();
            List<Recibo> lst=this.lstRecibo.getLst();
            for(Recibo rec : lst){
                double totales[]=rec.getTotales();
                if (totales[0]>monto){
                    /*Aplicamos todos los ajustes aqui*/
                    for (AJUS ajuste : lista){  
                        ReciboGlosa glosa;
                        if (ajuste.getST_AFEC_IGV_ForJSON())
                            glosa=new ReciboGlosa(ajuste.getDE_GLOS_ForJSON(), ajuste.getIM_MONT_ForJSON() , ETipoReciboGlosa.AJUSTES_AFECTOS_IGV);
                        else
                            glosa=new ReciboGlosa(ajuste.getDE_GLOS_ForJSON(), ajuste.getIM_MONT_ForJSON() , ETipoReciboGlosa.AJUSTES_NO_AFECTOS_IGV);
                        rec.addAjuste(glosa);
                    }
                    break;
                }
            }            
        }
    }
    public LstRecibo getLstRecibo() {
        return lstRecibo;
    }
        
    public static boolean isValidPathBaseRecibo(String ruta){
        File f=new File(ruta);
        try {
            return (f.exists() && f.isDirectory());
        } catch (Exception e) {}
        return false;        
    }    
    public static String getPathBaseRecibos(){
//        String rutas[]=new String[]{"/home/crodas/facturas_satelital/","C:\\facturas_satelital\\"};
        String rutas[]=new String[]{"/sistemas/satelital/input/"};
        for (String ruta : rutas){
            if (NegocioFacturar.isValidPathBaseRecibo(ruta))
                return ruta;
        }
        return null;
    }  
    public String getNombreFileFacturaPdf() {
        Integer periodo=0;
        Integer ano=0;
        Integer negocio=0;
        if (this.lstRecibo.size()>0){
            Recibo reci=this.lstRecibo.get(0);
            CIER cier=reci.getCier();
            periodo=cier.getNU_PERI_ForJSON();
            ano=cier.getNU_ANO_ForJSON();
            negocio=reci.getNego().getCO_NEGO_ForJSON();
        }
        return "Factura-"+negocio+"-"+periodo+"-"+ano;
    }     
    
    public Factura getFactura(){
        return this.factura;
    }
    public void save(){
        String pathBase=this.getPathBaseRecibos();
        if (pathBase!=null){
            for(Recibo recibo :this.lstRecibo.getLst()){
                ReciboPdf rp=new ReciboPdf(recibo);
                File file=new File(pathBase+recibo.getNombreFileReciboPdf()+".pdf");
                FileOutputStream fos;
                try {
                    fos = new FileOutputStream(file);
                    rp.createPDF();
                    rp.save(fos);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(LstRecibo.class.getName()).log(Level.SEVERE, null, ex);
                }  
                recibo.saveBD(this.request);
            }
            
            if (this.generaFactura()){                
                FacturaPdf fp=new FacturaPdf(this.factura);
                File file=new File(pathBase+this.getNombreFileFacturaPdf()+".pdf");
                FileOutputStream fos;
                try {
                    fos = new FileOutputStream(file);
                    fp.createPDF();
                    fp.save(fos);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(LstRecibo.class.getName()).log(Level.SEVERE, null, ex);
                }  
                this.factura.saveBD(this.request);                
            }
        }
    }    
    
    public void save(HttpServletResponse response) {
        int son=this.lstRecibo.size();
        if(son>0){
            if(son==1 && ! this.generaFactura()){
                ReciboPdf fp=new ReciboPdf(this.lstRecibo.get(0));
                fp.createPDF();
                fp.save(response);
            }else{
                try {
                    response.setHeader("Content-Type", "application/zip");
                    response.addHeader("Content-Disposition", "attachment; filename=\"pdfs.zip\"" );                    
                    ZipOutputStream zout = new ZipOutputStream(response.getOutputStream());
                    List<String> listaNombres=new ArrayList<String>();
                    Boolean existe=false;
                    Integer seRepite=0;
                    for(Recibo f :this.lstRecibo.getLst()){
                        OutputStream outStream=new ByteArrayOutputStream ();  
                        ReciboPdf fp=new ReciboPdf(f);
                        fp.createPDF();
                        fp.save(outStream);
                        ByteArrayOutputStream outStreamByte=(ByteArrayOutputStream) outStream;  
                        String nombre=f.getNombreFileReciboPdf(); 
                        //buscamos si el nombre existe, si existe se le pone (veces que se repite)
                        existe=false;
                        seRepite=0;
                        for(String tmpNombre : listaNombres){
                            if(tmpNombre.equals(nombre)){
                                seRepite++;
                                existe=true;
                            }
                        }
                        if(existe){
                            nombre=nombre +" ("+seRepite+")";
                        }
                        listaNombres.add(nombre);//agregar para las busquedas futuras
                        ZipEntry ze = new ZipEntry(nombre+".pdf");
                        zout.putNextEntry(ze);
                        zout.write(outStreamByte.toByteArray());
                        zout.closeEntry();
                    }
                    
                    if (this.generaFactura()){
                        OutputStream outStream=new ByteArrayOutputStream ();  
                        FacturaPdf fp=new FacturaPdf(this.factura);
                        fp.createPDF();
                        fp.save(outStream);
                        ByteArrayOutputStream outStreamByte=(ByteArrayOutputStream) outStream;  
                        String nombre=this.getNombreFileFacturaPdf();
                        //buscamos si el nombre existe, si existe se le pone (veces que se repite)
                        existe=false;
                        seRepite=0;
                        for(String tmpNombre : listaNombres){
                            if(tmpNombre.equals(nombre)){
                                seRepite++;
                                existe=true;
                            }
                        }
                        if(existe){
                            nombre=nombre +" ("+seRepite+")";
                        } 
                        listaNombres.add(nombre);//agregar para las busquedas futuras
                        ZipEntry ze = new ZipEntry(nombre+".pdf");
                        zout.putNextEntry(ze);
                        zout.write(outStreamByte.toByteArray());
                        zout.closeEntry();                        
                    }
                    zout.close();
                } catch (Exception e) {
                    Logs.printError("ERRROR AL CREAR PDF ZIP."+e.getMessage());
                } 
            }
        }else{
            response.setContentType("text/html");
            try {
                PrintWriter writer = response.getWriter();
                writer.write("No genera recibo, motivo \""+this.getMotivoNoFactura().toString()+"\".");
            } catch (IOException ex) {}            
        }
    }  
    
    public Recibo getReciboDelPeriodo(){
        //obtiene el recibo que le corresponde en el periodo de cierre
        Date inicio=FechaHora.getDate(this.cier.getNU_ANO_ForJSON(), this.cier.getNU_PERI_ForJSON()-1 ,1);
        Date fin=FechaHora.getUltimaFechaMes(inicio);
        for(Recibo recibo  : this.lstRecibo.getLst()){
            if (FechaHora.isBetween(inicio, fin, recibo.getCierre_Factura_Inicio()))
                return recibo;
        }
        return null;
    }
    
    public Boolean generaFactura(){
        return this.factura.getGlosas().size()>0;
    }
}
