/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.core3;

import com.americatel.facturacion.Utils.Constante;
import com.americatel.facturacion.Utils.Numero;
import com.americatel.facturacion.core2.LibPdf;
import com.americatel.facturacion.cores.EReciboTextAlign;
import com.americatel.facturacion.fncs.FechaHora;
import com.americatel.facturacion.fncs.Path;
import com.americatel.facturacion.fncs.fnc;
import com.americatel.facturacion.models.AJUS;
import com.americatel.facturacion.models.CIER;
import com.americatel.facturacion.models.CIER_DATA_PLAN;
import com.americatel.facturacion.models.CIER_DATA_SERV_SUPL;
import com.americatel.facturacion.models.CIER_DATA_SERV_UNIC;
import com.americatel.facturacion.models.CIER_DATA_SUCU;
import com.americatel.facturacion.models.CLIE;
import com.americatel.facturacion.models.DEPA;
import com.americatel.facturacion.models.DIST;
import com.americatel.facturacion.models.NEGO;
import com.americatel.facturacion.models.PROV;
import com.americatel.facturacion.models.SUCU;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.imageio.ImageIO;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.JPEGFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

/**
 *
 * @author crodas
 */
public class ReciboPdf extends LibPdf{
    public static String GLOSA_DIFERENCIAL_CARGOS_FIJOS="Diferencial de cargos fijos";
    public static String GLOSA_DEVOLUCION_CARGOS_FIJOS="Devoluci\u00f3n por cargos fijos";
    public static String GLOSA_DEVOLUCION_PROMOCION="Devoluci\u00f3n por promoci\u00f3n";
    Recibo recibo=null;
    protected List<PDPageContentStream> coss=new ArrayList<PDPageContentStream>();
    private float Font_Size_General=11;// min 7 por que existe un Font_Size_General-6
    ProcesoNegocio proceso_negocio=null;
    int cantidad_sucursales_facturan;
    ReciboPdf(Recibo recibo) {
        this.recibo=recibo;
        this.proceso_negocio=recibo.getProcesoNegocio();
        this.cantidad_sucursales_facturan=this.recibo.getCantidad_Sucursales_Facturan();
    }

    public void crearPdf(){
        if(this.recibo.isIsBoleta()){
            this.addPageBoleta();  
            this.crearPdfDetalladoBoleta(630);
            this.addCuadroFooterBoleta(350);
        }else{
            this.addPage();  
            this.crearPdfDetallado();
        }
         this.enumerarPaginasRecibo();
                
    }
    private void addPage(){
        this.numero_paginas++;
        this.page = new PDPage(PDRectangle.A4);        
        this.document.addPage(this.page);
        this.rect= this.page.getMediaBox();
        this.createContentStream(); 
        coss.add(this.cos);
        if (this.numero_paginas==1){
            this.addPageUno();
        }
        this.addNumeroRecibo();
    } 
    private void addPageBoleta(){
        this.numero_paginas++;
        this.page = new PDPage(PDRectangle.A4);        
        this.document.addPage(this.page);
        this.rect= this.page.getMediaBox();
        this.createContentStream(); 
        coss.add(this.cos);
        if (this.numero_paginas==1){
            this.addPageUnoBoleta();
        }
//        this.addNumeroBoleta();
    }
    private void addCuadroUsuario(float y){
        float font_size=Font_Size_General-4;
        NEGO nego=this.recibo.nego;
        CLIE clie=this.recibo.cliente;
        SUCU sucu=this.recibo.sucursal_fiscal;
        DIST dist=this.recibo.distrito_fiscal;
        PROV prov=this.recibo.provincia_fiscal;
        DEPA depa=this.recibo.departamento_fiscal;
        this.drawRectangle(35, y+47, 530, 80, 2); 
        this.drawString("SE\u00d1OR (ES)",40, y+115,font_size);
        this.drawString("RUC / DOC.ID",this.rect.getWidth()-200, y+115,font_size);
        this.drawString(clie.getDE_NUME_DOCU_ForJSON(),this.rect.getWidth()-100, y+115,font_size);
        this.drawString("CODIGO CLIENTE",this.rect.getWidth()-200, y+105,font_size);
        this.drawString(clie.getCodigoCliente(),this.rect.getWidth()-100, y+105,font_size);
        this.drawStringLimitWidth(clie.getFullNameCliente(),40, y+105,300,font_size+1,font_size);
//        this.drawString(sucu.getDE_DIRE_ForJSON(),40, y+85,font_size);
        this.drawStringLimitWidth(sucu.getDE_DIRE_ForJSON(),40, y+85,300,font_size+1,font_size);
//        y=this.drawStringLimitWidth ("", 40, y-15,350,9,this.fontPlain,Font_Size_General-4);
        //numero negocio
        this.drawString("N\u00b0 NEGOCIO",this.rect.getWidth()-200, y+85,font_size);
        this.drawString(nego.getCO_NEGO_ForJSON()+"",this.rect.getWidth()-100, y+85,font_size);
        //Ubicaci\u00f3n
        this.drawString(dist.getNO_DIST_ForJSON()+" - "+prov.getNO_PROV_ForJSON()+" - "+depa.getNO_DEPA_ForJSON(),40, y+70,font_size);
        //Moneda
        this.drawString("MONEDA",this.rect.getWidth()-200, y+70,font_size);
        this.drawString(nego.getStringMonedaFacturacion()+"",this.rect.getWidth()-100, y+70,font_size);
        //numero documento in info cliente
        this.drawString(this.recibo.NumeroRecibo+"",40, y+60,Font_Size_General-5);     
    }   
    
    private void addPageUno(){
        float y=570;
        this.addCuadroUsuario(y);                
        this.drawRectangle(35, y+25,this.rect.getWidth()-65  , 20, 2, Color.BLACK  , Color.LIGHT_GRAY);
        this.drawString("RESUMEN DE SERVICIOS", 40, y+32,fontBold,Font_Size_General-2); 
        try {                        
            BufferedImage awtImage = ImageIO.read(new File(Path.getPathWEB_INF()+"/../resources/sencha/facturacion/images/imagen_factura2.jpg"));
            //Para windows
//            BufferedImage awtImage = ImageIO.read(new File(getClass().getResource("/../../resources/sencha/facturacion/images/imagen_factura.jpg").toURI()));
//            PDXObjectImage ximage = new PDPixelMap(document, awtImage);
            PDImageXObject ximage = JPEGFactory.createFromImage(document, awtImage);
            float scale = 0.75f;
            float w=ximage.getWidth()*scale;
            float h=ximage.getHeight()*scale;
            cos.drawXObject(ximage, this.rect.getWidth()-w-30, y-h+20, w , h);
        } catch (Exception ex) {
            //ex.printStackTrace();
        }        
        this.drawStringLimitWidth (this.recibo.getPeriodo(), 40, y,350,9,this.fontBold,Font_Size_General-4);
        
        if (this.cantidad_sucursales_facturan==1){
            CIER_DATA_SUCU sucu=recibo.mi_data_facturar.entrySet().iterator().next().getKey();
            y=this.drawStringLimitWidth ("Sucursal : "+sucu.getDE_DIRE_SUCU()+ " ("+sucu.getNO_DIST()+" - "+sucu.getNO_PROV()+" - "+sucu.getNO_DEPA()+")", 40, y-15,350,9,this.fontPlain,Font_Size_General-4);
            this.crearPdfResumen(y-10);
            this.addCuadroFooter(200);
        }else{
            this.crearPdfResumen(y-20);
            this.addCuadroFooter(200);            
        }
    } 
    
    private void addPageUnoBoleta(){
        float y=850;
        this.addHeaderBoleta(y);
    }
    
     
    
    public void addNumeroRecibo(){
        this.drawString("RECIBO N\u00b0 "+this.recibo.NumeroRecibo,this.rect.getWidth()-150,800,fontBold, Font_Size_General-2);
    }
    
    
    private void cerrarCoss(){
        try {
            for(PDPageContentStream cos : this.coss){
                cos.close();
            }             
        } catch (Exception e) {
            e.printStackTrace();
        }
    }      
    public void savePdf(OutputStream out){
        this.cerrarCoss();
        super.save(out);
    }
    
    private void addHeaderBoleta(float y) {
        CIER cier=this.recibo.cier;
        CLIE clie=this.recibo.cliente;
        SUCU sucu=this.recibo.sucursal_fiscal;
        DIST dist=this.recibo.distrito_fiscal;
        PROV prov=this.recibo.provincia_fiscal;
        DEPA depa=this.recibo.departamento_fiscal;
        NEGO nego=this.recibo.nego;
        
        float posFieldA = 30 ;
        float posFieldB = 360 ;
        float posInfoA = posFieldA + 70 ;
        float posInfoB = posFieldB + 70 ;
        float alto=8;
        float ty=y-80;        
        this.drawStringChico("Americatel Per\u00fa S.A.",posFieldA, ty);ty-=alto;
        this.drawStringChico("Av. Manuel Olguin 211 NRO 211 URB. LOS GRANADOS (PISO 9)",posFieldA, ty);ty-=alto;
        this.drawStringChico("Santiado de Surco - Lima - Lima",posFieldA, ty);ty-=alto;
        this.drawStringChico("Telf.: 710-1977 // Fax: 710-1299",posFieldA, ty);ty-=alto;
        this.drawStringChico("P\u00e1gina Web: www.americatel.com.pe",posFieldA, ty);ty-=alto;
//      
        try {                        
            BufferedImage awtImage = ImageIO.read(new File(Path.getPathWEB_INF()+"/../resources/sencha/facturacion/images/logoAmericatel.jpg"));
            //Para windows
//            BufferedImage awtImage = ImageIO.read(new File(getClass().getResource("/../../resources/sencha/facturacion/images/imagen_factura.jpg").toURI()));
//            PDXObjectImage ximage = new PDPixelMap(document, awtImage);
            PDImageXObject ximage = JPEGFactory.createFromImage(document, awtImage);
            float scale = 0.10f;
            float w=ximage.getWidth()*scale;
            float h=ximage.getHeight()*scale;
            cos.drawXObject(ximage, 30, (y-30)-h, w , h);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        
        this.drawRectangle(360,y-120 , 200, 90,2);
        this.drawString("R.U.C. N\u00b0 20428698569", 385, y-50,this.fontBold,14);
        this.drawString("BOLETA DE VENTA", 395, y-75,this.fontBold,14);
        this.drawString("ELECTRONICA", 410, y-90,this.fontBold,14);
        this.drawString("B050    N\u00b0 "+com.americatel.facturacion.fncs.Numero.formatearNumeroDigitos(this.recibo.getNumeroBoleta(), 6), 405, y-110,this.fontBold,14);
        
        
        ty-=alto;
        ty-=alto;
//        this.drawStringChico("Lugar y Fecha de Emisi\u00f3n",40, ty);
        
        ty-=alto;
        this.drawStringChico("Razon Social: ",posFieldA, ty);
        this.drawStringChico( clie.getFullNameCliente() ,posInfoA, ty);
        
        this.drawStringChico("Fecha de Emision: ",posFieldB, ty);
        if(cier != null){
            this.drawStringChico( FechaHora.getStringDateShortStringMonth(cier.getFE_EMIS_ForJSON()),posInfoB, ty);
        }else{
            this.drawStringChico( FechaHora.getStringDateShortStringMonth(new Date()),posInfoB, ty);
        }
                
        ty-=alto;  
        this.drawStringChico("Direcci\u00f3n: ",posFieldA, ty);
        this.drawStringChico( sucu.getDE_DIRE_ForJSON()+", "+dist.getNO_DIST_ForJSON()+" - "+prov.getNO_PROV_ForJSON()+" - "+depa.getNO_DEPA_ForJSON()  ,posInfoA, ty);
        
        this.drawStringChico("Fecha de Vencimiento:",posFieldB, ty);
        if(cier != null){
            this.drawStringChico( FechaHora.getStringDateShortStringMonth(cier.getFE_VENC_ForJSON()),posInfoB, ty);
        }else{
            this.drawStringChico( FechaHora.getStringDateShortStringMonth(new Date()),posInfoB, ty);
        }
        ty-=alto;  
        
        this.drawStringChico("Orden de Compra: ",posFieldB, ty);
//        this.drawStringChico( null ,480, ty);
        
        ty-=alto; 
        this.drawStringChico("R.U.C.: ",posFieldA, ty);
        this.drawStringChico( clie.getDE_NUME_DOCU_ForJSON()  ,posInfoA, ty);
        
        this.drawStringChico("Tipo de Moneda: ",posFieldB, ty);
        this.drawStringChico( nego.getMone_fact_ForJSON().getNO_MONE_FACT_ForJSON() ,posInfoB, ty); 
        
        ty-=alto; 
        this.drawStringChico("C\u00f3digo: ",posFieldA, ty);
        this.drawStringChico( clie.getDE_CODI_BUM_ForJSON() ,posInfoA, ty);
        
        
        ty-=alto;   
        this.drawStringChico("Negocio: ",posFieldA, ty);
        this.drawStringChico(this.recibo.nego.getCO_NEGO_ForJSON().toString(), posInfoA, ty);
        
        
        ty-=alto;        
    }
    
    public void crearPdfDetallado(){
        if (this.cantidad_sucursales_facturan>1){
            
                float font_size=Font_Size_General-6;
                int incide_va=0;
                int contador_items=0;
                float col_a=40;
                float col_b=110;
                float col_c=350;
                float salto_linea=font_size+1;
                float y=780;
                //Double monto_devolucion_cargos_fijos=0d;
                //Double diferencial_cargos_fijos=0d;
                int van_sucursales=0;
                int van_letras=0;

                this.addPage();                  
                this.drawString("SE\u00d1OR (ES): ",col_a , y,fontBold,font_size);
                this.drawString(this.recibo.cliente.getFullNameCliente(),col_b , y,font_size);
                y-=salto_linea;                  
                this.drawString("N\u00b0 NEGOCIO: ",col_a , y,fontBold,font_size);
                this.drawString(this.recibo.nego.getCO_NEGO_ForJSON().toString() ,col_b , y,font_size);
                y-=salto_linea*2F;
                this.drawRectangle(col_a-2, y, 500,salto_linea   , 1F, Color.BLACK,Color.LIGHT_GRAY);
                this.drawString("DETALLE DEL NEGOCIO",col_a , y+1,fontBold,font_size);
                y-=salto_linea*2; 

    //            ListaRecibo lista=proceso_negocio.getLista_Recibo();
                //for(ReciboPdf recibo_pdf : lista.getRecibos()){
                for(Entry<CIER_DATA_SUCU,Object[]> entry : recibo.mi_data_facturar.entrySet()){
                    van_sucursales++;
                    CIER_DATA_SUCU sucu_data=entry.getKey();
                    this.drawString("Sucursal "+van_sucursales+": ",col_a , y,fontBold,font_size);
                    y=this.drawStringLimitWidth(sucu_data.getDE_DIRE_SUCU()+ " ("+sucu_data.getNO_DIST()+" - "+sucu_data.getNO_PROV()+" - "+sucu_data.getNO_DEPA()+")",col_b , y,400,9,font_size);
                    y-=salto_linea; 
                    /*
                    if (sucu_data.getDE_ORDE()!=null && sucu_data.getDE_ORDE().length()>0 ){
                        this.drawString("Orden de Compra:",col_a , y,fontBold,font_size);
                        this.drawString(sucu_data.getDE_ORDE(),col_b , y,font_size);
                        y-=salto_linea; 
                    } 
                    */
                    List<CIER_DATA_PLAN> planes=(List<CIER_DATA_PLAN>) entry.getValue()[0];
                    List<CIER_DATA_SERV_SUPL> sss=(List<CIER_DATA_SERV_SUPL>) entry.getValue()[1];
                    van_letras=0;
                    for(CIER_DATA_PLAN plan : planes){                        
                        if (plan.getST_TIPO_DEVO()==null){
                            this.drawString(Character.toString((char)(van_letras+97))+". "+fnc.capitalize(plan.getDE_NOMB()),col_a , y,font_size);
                            this.drawString(this.getStringDecimalFormat(plan.getIM_MONT(),2),col_c , y,fontPlain,font_size,EReciboTextAlign.DERECHA);
                            van_letras++; 
                            y-=salto_linea;
                        }
                    }
                    y-=salto_linea;
                    for(CIER_DATA_SERV_SUPL ss : sss){                                                    
                        if (ss.getST_TIPO_DEVO()==null){
                            this.drawString(Character.toString((char)(van_letras+97))+". "+fnc.capitalize(ss.getDE_NOMB()),col_a , y,font_size);
                            this.drawString(this.getStringDecimalFormat(ss.getIM_MONT(),2),col_c , y,fontPlain,font_size,EReciboTextAlign.DERECHA);
                            van_letras++; 
                            y-=salto_linea;
                        }
                    }
                    y-=salto_linea;
                }
                
            }
    }   
    
    public void crearPdfDetalladoBoleta(float inPosition ){
        Map<String,Object[]> resumen_planes=new HashMap<String, Object[]>();
        Double monto_cobrar=0d;
        Double devolucion_por_promocion=0d;
        Double devolucion_cargos_fijos=0d;
        Map<String,Object[]> resumen_sss=new HashMap<String, Object[]>();
        
        for(Entry<CIER_DATA_SUCU,Object[]> entry : recibo.mi_data_facturar.entrySet()){
            CIER_DATA_SUCU sucu_data=entry.getKey();
            List<CIER_DATA_PLAN> planes=(List<CIER_DATA_PLAN>) entry.getValue()[0];
            List<CIER_DATA_SERV_SUPL> sss=(List<CIER_DATA_SERV_SUPL>) entry.getValue()[1];            
            Integer co_nego_sucu=sucu_data.getCO_NEGO_SUCU();
            for(CIER_DATA_PLAN plan : planes){                        
                if (plan.getST_TIPO_DEVO()==null){   
                    String nombre=plan.getDE_NOMB();
                    Double m=plan.getIM_MONT();
                    if (plan.getST_TIPO_COBR()==2){
                        nombre=ReciboPdf.GLOSA_DIFERENCIAL_CARGOS_FIJOS;
                        if (!plan.getCO_MONE_FACT().equals(this.recibo.getMonedaRecibo())){
                            if (this.recibo.esMonedaSolesRecibo()){
                                m*=this.recibo.cier.getNU_TIPO_CAMB_ForJSON();
                            }else{
                                m/=this.recibo.cier.getNU_TIPO_CAMB_ForJSON();
                            }
                        }
                        
                    }
                    if (!resumen_planes.containsKey(nombre)){
                        resumen_planes.put(nombre, new Object[]{nombre,0,0d,0});
                    }
                    Object data[]=resumen_planes.get(nombre);
                    if((Integer)data[3]==0 || (Integer)data[3]!=co_nego_sucu){
                        data[1]=(Integer)data[1]+1;
                    }
                    data[2]=(Double)data[2]+m;
                    data[3]=sucu_data.getCO_NEGO_SUCU();
                    monto_cobrar+=m; 
                }else{
                    if (plan.getST_TIPO_DEVO()==4){
                        devolucion_por_promocion+=Math.abs(plan.getIM_MONT())*-1;                        
                    }/*else{
                        devolucion_cargos_fijos+=Math.abs(plan.getIM_MONT())*-1;
                    }*/
                }
            }          
            for(CIER_DATA_SERV_SUPL ss : sss){                        
                if (ss.getST_TIPO_DEVO()==null){            
                    if (!resumen_sss.containsKey(ss.getDE_NOMB())){
                        resumen_sss.put(ss.getDE_NOMB(), new Object[]{ss.getDE_NOMB(),0,0d,0});
                    }
                    Object data[]=resumen_sss.get(ss.getDE_NOMB());
                    if((Integer)data[3]==0 || (Integer)data[3]!=co_nego_sucu){
                        data[1]=(Integer)data[1]+1;
                    }
                    data[2]=(Double)data[2]+ss.getIM_MONT(); 
                    data[3]=sucu_data.getCO_NEGO_SUCU();
                    monto_cobrar+=ss.getIM_MONT();
                }else{
                    if (ss.getST_TIPO_DEVO()==4){
                        devolucion_por_promocion+=Math.abs(ss.getIM_MONT())*-1;                        
                    }
                }
            }
        }
        for(Entry<CIER_DATA_SUCU,List<CIER_DATA_SERV_UNIC>>  entry : recibo.mi_servicios_unicos_data.entrySet() ){
            for (CIER_DATA_SERV_UNIC su : entry.getValue()){
                monto_cobrar+=su.getIM_MONT();
            }
        }
        
        float font_size=Font_Size_General-6;
        float posInicialInfo = 35;
        float posInicialCelda = posInicialInfo-5;
//        float salto_linea=font_size+1;
        
        float tamanioCeldaA = 30;
        float tamanioCeldaB = 280;
        float tamanioCeldaC = 60;
        float tamanioCeldaD = 80;
        float tamanioCeldaE = 80;
//        float tamaÃ±oCeldaF = 60;        
        float alturaCelda = 10;
        float tamanioCeldaCuerpo = 250;
        float y=inPosition;
        float alturaInfo =y+2;
        int item = 0;
        
        float posHeaderA = posInicialCelda;
        float posHeaderB = (posHeaderA + tamanioCeldaA);
        float posHeaderC = (posHeaderB + tamanioCeldaB);
        float posHeaderD = (posHeaderC + tamanioCeldaC);
        float posHeaderE = (posHeaderD + tamanioCeldaD);
//        float posHeaderF = (posHeaderE + tamanioCeldaE);
        
        float posInfoA = posInicialInfo;
        float posInfoB = posInfoA + tamanioCeldaA;
        float posInfoC = posInfoB + tamanioCeldaB;
        float posInfoD = posInfoC + tamanioCeldaC;
        float posInfoE = posInfoD + tamanioCeldaD;
//        float posInfoF = posInfoE + tamanioCeldaE;
        
        this.drawRectangle(posHeaderA,y , tamanioCeldaA, alturaCelda,2);
        this.drawRectangle(posHeaderB,y , tamanioCeldaB, alturaCelda,2);
        this.drawRectangle(posHeaderC,y , tamanioCeldaC, alturaCelda,2);
        this.drawRectangle(posHeaderD,y , tamanioCeldaD, alturaCelda,2);
        this.drawRectangle(posHeaderE,y , tamanioCeldaE, alturaCelda,2);
//        this.drawRectangle(posHeaderF,y , tamanioCeldaF, alturaCelda,2);
        
        
        y-=tamanioCeldaCuerpo;
        
        this.drawRectangle(posHeaderA,y , tamanioCeldaA, tamanioCeldaCuerpo,2);
        this.drawRectangle(posHeaderB,y , tamanioCeldaB, tamanioCeldaCuerpo,2);
        this.drawRectangle(posHeaderC,y , tamanioCeldaC, tamanioCeldaCuerpo,2);
        this.drawRectangle(posHeaderD,y , tamanioCeldaD, tamanioCeldaCuerpo,2);
        this.drawRectangle(posHeaderE,y , tamanioCeldaE, tamanioCeldaCuerpo,2);
//        this.drawRectangle(posHeaderF,y , tamanioCeldaF, tamanioCeldaCuerpo,2);
        
        this.drawStringChico( Constante.S_ITEM, posInfoA, alturaInfo);
        this.drawStringChico( Constante.S_DESCRIPCION, posInfoB, alturaInfo);
        this.drawStringChico( Constante.S_CANTIDAD, posInfoC, alturaInfo);
        this.drawStringChico( Constante.S_VUNITARIO, posInfoD, alturaInfo);
//        this.drawStringChico( Constante.S_DESCUENTO, posInfoE, alturaInfo);
        this.drawStringChico( Constante.S_TOTAL, posInfoE, alturaInfo);
        
        
        alturaInfo -= alturaCelda;
        
        for(Map.Entry<String,Object[]> p : resumen_planes.entrySet()){
            String nombre=fnc.capitalize((String)p.getValue()[0]);
            Integer cantidad=(Integer)p.getValue()[1];
            Double monto=(Double)p.getValue()[2];
            item++;
            this.drawStringChico( String.valueOf(item) , posInfoA, alturaInfo);
            this.drawStringChico( nombre, posInfoB, alturaInfo);
            this.drawStringChico(String.valueOf(cantidad),posInfoC , alturaInfo);
            this.drawStringChico(String.valueOf(monto/cantidad),posInfoD , alturaInfo);
            this.drawStringChico(this.getStringDecimalFormat(monto,2),posInfoE , alturaInfo);
            alturaInfo -= alturaCelda;
        }
        for(Map.Entry<String,Object[]> s : resumen_sss.entrySet()){
            String nombre=fnc.capitalize((String)s.getValue()[0]);
            Integer cantidad=(Integer)s.getValue()[1];
            Double monto=(Double)s.getValue()[2];
            item++;
            this.drawStringChico( String.valueOf(item) , posInfoA, alturaInfo);
            this.drawStringChico( nombre, posInfoB, alturaInfo);
            this.drawStringChico(String.valueOf(cantidad),posInfoC , alturaInfo);
            this.drawStringChico(String.valueOf(monto/cantidad),posInfoD , alturaInfo);
            this.drawStringChico(this.getStringDecimalFormat(monto,2),posInfoE , alturaInfo);
            alturaInfo -= alturaCelda;
        }     
        /*Agregar Â¨Servicios Unicos*/
        for(Entry<CIER_DATA_SUCU,List<CIER_DATA_SERV_UNIC>>  entry1 : this.recibo.mi_servicios_unicos_data.entrySet() ){
            for (CIER_DATA_SERV_UNIC su : entry1.getValue()){
                item++;
                this.drawStringChico( String.valueOf(item) , posInfoA, alturaInfo);
                this.drawStringChico( su.getDE_NOMB() , posInfoB, alturaInfo);
                this.drawStringChico(String.valueOf(1),posInfoC , alturaInfo);
                this.drawStringChico(this.getStringDecimalFormat(su.getIM_MONT(),2),posInfoD , alturaInfo);
                this.drawStringChico(this.getStringDecimalFormat(su.getIM_MONT(),2),posInfoE , alturaInfo);
                alturaInfo -= alturaCelda;
            }
        }
        
        /*Validamos la devolucion por promociones y por cargos fijos que no exeda lo que se cobra*/
        if (Math.abs(recibo.saldo_aplica)>Math.abs(devolucion_por_promocion))
            devolucion_cargos_fijos=(recibo.saldo_aplica+devolucion_por_promocion)*-1;
        if (monto_cobrar>0){
            if ((monto_cobrar+devolucion_por_promocion)<0d){
                devolucion_por_promocion=monto_cobrar*-1;
                devolucion_cargos_fijos=0d;
            }else if ((monto_cobrar+devolucion_por_promocion+devolucion_cargos_fijos)<0d){
                devolucion_cargos_fijos=(monto_cobrar+devolucion_por_promocion)*-1;
            }
        }else{
            devolucion_por_promocion=0d;
            devolucion_cargos_fijos=0d;            
        }
               
        int cantidad_ajustes_afectos=0,cantidad_ajustes_no_afectos=0;
        for(AJUS ajus : this.recibo.mi_ajustes){
            if (ajus.getST_AFEC_IGV_ForJSON()){
                cantidad_ajustes_afectos++;
            }else{
                cantidad_ajustes_no_afectos++;
            }
        }        
        if (this.recibo.ajustes_por_redondeo!=0d){
            cantidad_ajustes_no_afectos++;
        }
        
        if (cantidad_ajustes_afectos>0 || (devolucion_por_promocion!=0 || devolucion_cargos_fijos!=0 )){
            for(AJUS ajus : this.recibo.mi_ajustes){                
                if (ajus.getST_AFEC_IGV_ForJSON()){ 
                    item++;
                    this.drawStringChico( String.valueOf(item) , posInfoA, alturaInfo);
                    this.drawStringChico( ajus.getDE_GLOS_ForJSON() , posInfoB, alturaInfo);
                    this.drawStringChico(String.valueOf(1),posInfoC , alturaInfo);
                    this.drawStringChico(this.getStringDecimalFormat(Math.abs(ajus.getIM_MONT_ForJSON())*-1d,2),posInfoD , alturaInfo);
                    this.drawStringChico(this.getStringDecimalFormat(Math.abs(ajus.getIM_MONT_ForJSON())*-1d,2),posInfoE , alturaInfo);
                    alturaInfo -= alturaCelda;
                }
            }
            
            if (devolucion_por_promocion!=0){  
                item++;
                this.drawStringChico( String.valueOf(item) , posInfoA, alturaInfo);
                this.drawStringChico( Constante.GLOSA_DEVOLUCION_PROMOCION , posInfoB, alturaInfo);
                this.drawStringChico(String.valueOf(1),posInfoC , alturaInfo);
                this.drawStringChico(this.getStringDecimalFormat(devolucion_por_promocion,2),posInfoD , alturaInfo);
                this.drawStringChico(this.getStringDecimalFormat(devolucion_por_promocion,2),posInfoE , alturaInfo);
                alturaInfo -= alturaCelda;
            }
            if (devolucion_cargos_fijos!=0){  
                item++;
                this.drawStringChico( String.valueOf(item) , posInfoA, alturaInfo);
                this.drawStringChico( Constante.GLOSA_DEVOLUCION_CARGOS_FIJOS , posInfoB, alturaInfo);
                this.drawStringChico(String.valueOf(1),posInfoC , alturaInfo);
                this.drawStringChico(this.getStringDecimalFormat(devolucion_cargos_fijos,2),posInfoD , alturaInfo);
                this.drawStringChico(this.getStringDecimalFormat(devolucion_cargos_fijos,2),posInfoE , alturaInfo);
                alturaInfo -= alturaCelda;
            }            
        }
        if (cantidad_ajustes_no_afectos>0){
            for(AJUS ajus : this.recibo.mi_ajustes){
                if (!ajus.getST_AFEC_IGV_ForJSON()){ 
                   item++;
                    this.drawStringChico( String.valueOf(item) , posInfoA, alturaInfo);
                    this.drawStringChico( Constante.GLOSA_DEVOLUCION_CARGOS_FIJOS , posInfoB, alturaInfo);
                    this.drawStringChico(String.valueOf(1),posInfoC , alturaInfo);
                    this.drawStringChico(this.getStringDecimalFormat(devolucion_cargos_fijos,2),posInfoD , alturaInfo);
                    this.drawStringChico(this.getStringDecimalFormat(devolucion_cargos_fijos,2),posInfoE , alturaInfo);
                    alturaInfo -= alturaCelda; 
                }
            }
            if (this.recibo.ajustes_por_redondeo!=0d){  
                item++;
                this.drawStringChico( String.valueOf(item) , posInfoA, alturaInfo);
                this.drawStringChico( Constante.GLOSA_AJUSTE_REDONDEO , posInfoB, alturaInfo);
                this.drawStringChico(String.valueOf(1),posInfoC , alturaInfo);
                this.drawStringChico(this.getStringDecimalFormat(this.recibo.ajustes_por_redondeo,2),posInfoD , alturaInfo);
                this.drawStringChico(this.getStringDecimalFormat(this.recibo.ajustes_por_redondeo,2),posInfoE , alturaInfo);
                alturaInfo -= alturaCelda;
            }
        }

//        for(Entry<CIER_DATA_SUCU,Object[]> entry : recibo.mi_data_facturar.entrySet()){
//            List<CIER_DATA_PLAN> planes=(List<CIER_DATA_PLAN>) entry.getValue()[0];
//            List<CIER_DATA_SERV_SUPL> sss=(List<CIER_DATA_SERV_SUPL>) entry.getValue()[1];
//            
//            for(CIER_DATA_PLAN plan : planes){                        
//                if (plan.getST_TIPO_DEVO()==null){
//                    item++;
//                    this.drawStringChico( String.valueOf(item) , posInfoA, alturaInfo);
//                    this.drawStringChico( plan.getDE_NOMB() , posInfoB, alturaInfo);
//                    this.drawStringChico(String.valueOf(1),posInfoC , alturaInfo);
//                    this.drawStringChico(this.getStringDecimalFormat(plan.getIM_MONT(),2),posInfoD , alturaInfo);
//                    this.drawStringChico(this.getStringDecimalFormat(plan.getIM_MONT(),2),posInfoE , alturaInfo);
////                    this.drawStringChico(this.getStringDecimalFormat(plan.getIM_MONT(),2),posInfoF , alturaInfo);
//                    alturaInfo -= alturaCelda;
//                }
//            }
//            
//            for(CIER_DATA_SERV_SUPL ss : sss){                                                    
//                if (ss.getST_TIPO_DEVO()==null){
//                    item++;
//                    this.drawStringChico( String.valueOf(item) , posInfoA, alturaInfo);
//                    this.drawStringChico( ss.getDE_NOMB() , posInfoB, alturaInfo);
//                    this.drawStringChico(String.valueOf(1),posInfoC , alturaInfo);
//                    this.drawStringChico(this.getStringDecimalFormat(ss.getIM_MONT(),2),posInfoD , alturaInfo);
//                    this.drawStringChico(this.getStringDecimalFormat(ss.getIM_MONT(),2),posInfoE , alturaInfo);
//                    alturaInfo -= alturaCelda;
//                }
//            }
//            
//            /*Agregar Â¨Servicios Unicos*/
//            for(Entry<CIER_DATA_SUCU,List<CIER_DATA_SERV_UNIC>>  entry1 : this.recibo.mi_servicios_unicos_data.entrySet() ){
//                for (CIER_DATA_SERV_UNIC su : entry1.getValue()){
//                    item++;
//                    this.drawStringChico( String.valueOf(item) , posInfoA, alturaInfo);
//                    this.drawStringChico( su.getDE_NOMB() , posInfoB, alturaInfo);
//                    this.drawStringChico(String.valueOf(1),posInfoC , alturaInfo);
//                    this.drawStringChico(this.getStringDecimalFormat(su.getIM_MONT(),2),posInfoD , alturaInfo);
//                    this.drawStringChico(this.getStringDecimalFormat(su.getIM_MONT(),2),posInfoE , alturaInfo);
//                    alturaInfo -= alturaCelda;
//                }
//            }
//        }
            
    }  
    
    private void addCuadroFooterBoleta(float inPosition) {
        float posInicialInfo = 35;
        float posInicialCelda = posInicialInfo-5;
        
        float tamanioColumnaA = 330;
        float tamanioColumnaB = 90;
        float tamanioColumnaC = 50;
        float tamanioColumnaD = 60; 
        
        float alturaCeldaTipoA = 20;
        float alturaCeldaTipoB = 10;
        float alturaCeldaTipoC = 80;
        float y=inPosition;
        float alturaInfo =y+2;
        
        /*Posiciones para columnas*/
        float posHeaderA1 = posInicialCelda;
        float posHeaderB1 = (posHeaderA1 + tamanioColumnaA);
        float posHeaderC1 = (posHeaderB1 + tamanioColumnaB);
        float posHeaderD1 = (posHeaderC1 + tamanioColumnaC);
        
        
        /*Dibujar Celdas*/
        
        this.drawRectangle(posHeaderB1,y , tamanioColumnaB, alturaCeldaTipoB,2);
        this.drawRectangle(posHeaderC1,y , tamanioColumnaC, alturaCeldaTipoB,2);
        this.drawRectangle(posHeaderD1,y , tamanioColumnaD, alturaCeldaTipoB,2);
        
        y-=alturaCeldaTipoB;
        
        this.drawRectangle(posHeaderA1,y , tamanioColumnaA, alturaCeldaTipoA,2);
        
        this.drawRectangle(posHeaderB1,y , tamanioColumnaB, alturaCeldaTipoB,2);
        this.drawRectangle(posHeaderC1,y , tamanioColumnaC, alturaCeldaTipoB,2);
        this.drawRectangle(posHeaderD1,y , tamanioColumnaD, alturaCeldaTipoB,2);
        
        for (int i = 0; i < 8; i++) {
            y-=alturaCeldaTipoB;
        
            this.drawRectangle(posHeaderB1,y , tamanioColumnaB, alturaCeldaTipoB,2);
            this.drawRectangle(posHeaderC1,y , tamanioColumnaC, alturaCeldaTipoB,2);
            this.drawRectangle(posHeaderD1,y , tamanioColumnaD, alturaCeldaTipoB,2);
        }
        
        this.drawRectangle(posHeaderA1,y , tamanioColumnaA, alturaCeldaTipoC,2);
        
        /**/
        
        float posInfoX_A1 = posInicialInfo;        
        float posInfoX_B2 = posInfoX_A1 + tamanioColumnaA;
        float posInfoX_C3 = posInfoX_B2 + tamanioColumnaB;
        float posInfoX_D4 = posInfoX_C3 + tamanioColumnaC;
        
        
        Integer CO_MONE_FACT = this.recibo.moneda_facturacion.getCO_MONE_FACT_ForJSON();
        Double totales[]=this.recibo.getTotales();
        Numero numero = new Numero();
        String numeroTotal = this.getStringDecimalFormat(totales[4],2);
        this.drawStringChico(numero.convertirMontoToLetraReporte(numeroTotal, true, CO_MONE_FACT) , 
                                                                posInfoX_A1, alturaInfo);
        this.drawStringChico(Constante.LABEL_OP_GRAVADAS, posInfoX_B2, (alturaInfo - (alturaCeldaTipoB*0)));
        this.drawStringChico(Constante.LABEL_OP_INAFECTAS, posInfoX_B2, (alturaInfo - (alturaCeldaTipoB*1)));
        this.drawStringChico(Constante.LABEL_OP_EXONERADAS, posInfoX_B2, (alturaInfo - (alturaCeldaTipoB*2)));
        this.drawStringChico(Constante.LABEL_OP_GRATUITAS, posInfoX_B2, (alturaInfo - (alturaCeldaTipoB*3)));       
        this.drawStringChico(Constante.LABEL_DESCUENTO_GLO, posInfoX_B2, (alturaInfo - (alturaCeldaTipoB*4)));
        this.drawStringChico(Constante.LABEL_IGV, posInfoX_B2, (alturaInfo - (alturaCeldaTipoB*5)));
        this.drawStringChico(Constante.LABEL_OTROS_TRIBUTOS, posInfoX_B2, (alturaInfo - (alturaCeldaTipoB*6)));        
        this.drawStringChico(Constante.LABEL_OTROS_CARGOS, posInfoX_B2, (alturaInfo - (alturaCeldaTipoB*7)));        
        this.drawStringChico(Constante.LABEL_TOTAL, posInfoX_B2, (alturaInfo - (alturaCeldaTipoB*8)));        
        this.drawStringChico(Constante.LABEL_REDONDEO, posInfoX_B2, (alturaInfo - (alturaCeldaTipoB*9)));        
                
        for (int i = 0; i < 10; i++) {
            this.drawStringChico(Constante.MONEDAS.get(CO_MONE_FACT), posInfoX_C3, (alturaInfo - (alturaCeldaTipoB*i)));
        }
        this.drawStringChico(this.getStringDecimalFormat(totales[0],2)  , posInfoX_D4, (alturaInfo - (alturaCeldaTipoB*0)));
        this.drawStringChico(this.getStringDecimalFormat(0d,2)  , posInfoX_D4, (alturaInfo - (alturaCeldaTipoB*1)));
        this.drawStringChico(this.getStringDecimalFormat(0d,2)          , posInfoX_D4, (alturaInfo - (alturaCeldaTipoB*2)));
        this.drawStringChico(this.getStringDecimalFormat(0d,2)          , posInfoX_D4, (alturaInfo - (alturaCeldaTipoB*3)));
        this.drawStringChico(this.getStringDecimalFormat(totales[1]+(totales[3]-totales[11])+(totales[8]-totales[11]),2)  , posInfoX_D4, (alturaInfo - (alturaCeldaTipoB*4)));
        this.drawStringChico(this.getStringDecimalFormat(totales[2],2)  , posInfoX_D4, (alturaInfo - (alturaCeldaTipoB*5)));
        this.drawStringChico(this.getStringDecimalFormat(0d,2)          , posInfoX_D4, (alturaInfo - (alturaCeldaTipoB*6)));
        this.drawStringChico(this.getStringDecimalFormat(0d,2)          , posInfoX_D4, (alturaInfo - (alturaCeldaTipoB*7)));
        this.drawStringChico(this.getStringDecimalFormat(totales[4],2), posInfoX_D4, (alturaInfo - (alturaCeldaTipoB*8)));
        this.drawStringChico(this.getStringDecimalFormat(totales[11],2), posInfoX_D4, (alturaInfo - (alturaCeldaTipoB*9)));
        
        this.drawStringChico("Referencias Adicionales", posInfoX_A1, (alturaInfo - (alturaCeldaTipoB*3)));
        this.drawStringChico(this.recibo.getPeriodo(), posInfoX_A1, (alturaInfo - (alturaCeldaTipoB*4)));
        
        /*Dibujar Celda de Leyenda*/
        this.drawStringChico("Leyendas", 30, y-=18);
        y-=2;        
        for (int i = 0; i < 2; i++) {
            y-=10;
            this.drawRectangle(30,y , 20, 10,2);
            this.drawRectangle(50,y , 350, 10,2);
            this.drawRectangle(400,y , 40, 10,2);
        }
    } 
    
    public void enumerarPaginasRecibo(){
        if (this.cantidad_sucursales_facturan>1){
            PDPageContentStream tmp=this.cos;
            int son=this.coss.size();
            int van=0;
            for(PDPageContentStream cos : this.coss){
                this.cos=cos;
                van++;
                if (van>1){
                    this.drawString("P\u00e1g "+van+" / "+son, 480, 790,6);
                }               
            }            
            this.cos=tmp;
        }
    }
    
    public void crearPdfResumen(float y){
        Map<String,Object[]> resumen_planes=new HashMap<String, Object[]>();
        Double monto_cobrar=0d;
        Double devolucion_por_promocion=0d;
        Double devolucion_cargos_fijos=0d;
        Map<String,Object[]> resumen_sss=new HashMap<String, Object[]>();
        
        for(Entry<CIER_DATA_SUCU,Object[]> entry : recibo.mi_data_facturar.entrySet()){
            CIER_DATA_SUCU sucu_data=entry.getKey();
            List<CIER_DATA_PLAN> planes=(List<CIER_DATA_PLAN>) entry.getValue()[0];
            List<CIER_DATA_SERV_SUPL> sss=(List<CIER_DATA_SERV_SUPL>) entry.getValue()[1];            
            Integer co_nego_sucu=sucu_data.getCO_NEGO_SUCU();
            for(CIER_DATA_PLAN plan : planes){                        
                if (plan.getST_TIPO_DEVO()==null){   
                    String nombre=plan.getDE_NOMB();
                    Double m=plan.getIM_MONT();
                    if (plan.getST_TIPO_COBR()==2){
                        nombre=ReciboPdf.GLOSA_DIFERENCIAL_CARGOS_FIJOS;
                        if (!plan.getCO_MONE_FACT().equals(this.recibo.getMonedaRecibo())){
                            if (this.recibo.esMonedaSolesRecibo()){
                                m*=this.recibo.cier.getNU_TIPO_CAMB_ForJSON();
                            }else{
                                m/=this.recibo.cier.getNU_TIPO_CAMB_ForJSON();
                            }
                        }
                        
                    }
                    if (!resumen_planes.containsKey(nombre)){
                        resumen_planes.put(nombre, new Object[]{nombre,0,0d,0});
                    }
                    Object data[]=resumen_planes.get(nombre);
                    if((Integer)data[3]==0 || (Integer)data[3]!=co_nego_sucu){
                        data[1]=(Integer)data[1]+1;
                    }
                    data[2]=(Double)data[2]+m;
                    data[3]=sucu_data.getCO_NEGO_SUCU();
                    monto_cobrar+=m; 
                }else{
                    if (plan.getST_TIPO_DEVO()==4){
                        devolucion_por_promocion+=Math.abs(plan.getIM_MONT())*-1;                        
                    }/*else{
                        devolucion_cargos_fijos+=Math.abs(plan.getIM_MONT())*-1;
                    }*/
                }
            }          
            for(CIER_DATA_SERV_SUPL ss : sss){                        
                if (ss.getST_TIPO_DEVO()==null){            
                    if (!resumen_sss.containsKey(ss.getDE_NOMB())){
                        resumen_sss.put(ss.getDE_NOMB(), new Object[]{ss.getDE_NOMB(),0,0d,0});
                    }
                    Object data[]=resumen_sss.get(ss.getDE_NOMB());
                    if((Integer)data[3]==0 || (Integer)data[3]!=co_nego_sucu){
                        data[1]=(Integer)data[1]+1;
                    }
                    data[2]=(Double)data[2]+ss.getIM_MONT(); 
                    data[3]=sucu_data.getCO_NEGO_SUCU();
                    monto_cobrar+=ss.getIM_MONT();
                }else{
                    if (ss.getST_TIPO_DEVO()==4){
                        devolucion_por_promocion+=Math.abs(ss.getIM_MONT())*-1;                        
                    }
                }
            }
        }
        for(Entry<CIER_DATA_SUCU,List<CIER_DATA_SERV_UNIC>>  entry : recibo.mi_servicios_unicos_data.entrySet() ){
            for (CIER_DATA_SERV_UNIC su : entry.getValue()){
                monto_cobrar+=su.getIM_MONT();
            }
        }
        
        float font_size=Font_Size_General-4;
        float col_a=40;
        float col_b=300;
        float col_c=350;
        float max_w_a=250;
        float salto_linea=font_size+1;
        int contador_indice=0;
        int incide_va=0;
        contador_indice++;
        y=this.drawStringLimitWidth(contador_indice+". SERVICIOS", col_a , y,max_w_a,salto_linea ,fontBold, font_size);
        y-=salto_linea;
        for(Entry<String,Object[]> p : resumen_planes.entrySet()){
            String nombre=fnc.capitalize((String)p.getValue()[0]);
            Integer cantidad=(Integer)p.getValue()[1];
            Double monto=(Double)p.getValue()[2];
            this.drawString("("+cantidad+")", col_b , y,font_size);
            this.drawString(this.getStringDecimalFormat(monto,2), col_c , y,fontPlain,font_size,EReciboTextAlign.DERECHA);            
            y=this.drawStringLimitWidth(Character.toString((char)(incide_va+97))+". "+nombre, col_a+5 , y,max_w_a,salto_linea ,fontPlain, font_size);
            incide_va++;
        }
        for(Entry<String,Object[]> s : resumen_sss.entrySet()){
            String nombre=fnc.capitalize((String)s.getValue()[0]);
            Integer cantidad=(Integer)s.getValue()[1];
            Double monto=(Double)s.getValue()[2];
            this.drawString("("+cantidad+")", col_b , y,font_size);
            this.drawString(this.getStringDecimalFormat(monto,2), col_c , y,fontPlain,font_size,EReciboTextAlign.DERECHA);            
            y=this.drawStringLimitWidth(Character.toString((char)(incide_va+97))+". "+nombre, col_a+5 , y,max_w_a,salto_linea ,fontPlain, font_size);
            incide_va++;
        }     
        for(Entry<CIER_DATA_SUCU,List<CIER_DATA_SERV_UNIC>> entry : this.recibo.mi_servicios_unicos_data.entrySet() ){
            for(CIER_DATA_SERV_UNIC su : entry.getValue()){
                this.drawString("("+1+")", col_b , y,font_size);
                this.drawString(this.getStringDecimalFormat(su.getIM_MONT(),2), col_c , y,fontPlain,font_size,EReciboTextAlign.DERECHA);            
                y=this.drawStringLimitWidth(Character.toString((char)(incide_va+97))+". "+fnc.capitalize(su.getDE_NOMB()), col_a+5 , y,max_w_a,salto_linea ,fontPlain, font_size);                        
                incide_va++;            
            }
        }
             
        /*Validamos la devolucion por promociones y por cargos fijos que no exeda lo que se cobra*/
        if (Math.abs(recibo.saldo_aplica)>Math.abs(devolucion_por_promocion))
            devolucion_cargos_fijos=(recibo.saldo_aplica+devolucion_por_promocion)*-1;
        if (monto_cobrar>0){
            if ((monto_cobrar+devolucion_por_promocion)<0d){
                devolucion_por_promocion=monto_cobrar*-1;
                devolucion_cargos_fijos=0d;
            }else if ((monto_cobrar+devolucion_por_promocion+devolucion_cargos_fijos)<0d){
                devolucion_cargos_fijos=(monto_cobrar+devolucion_por_promocion)*-1;
            }
        }else{
            devolucion_por_promocion=0d;
            devolucion_cargos_fijos=0d;            
        }
               
        int cantidad_ajustes_afectos=0,cantidad_ajustes_no_afectos=0;
        for(AJUS ajus : this.recibo.mi_ajustes){
            if (ajus.getST_AFEC_IGV_ForJSON()){
                cantidad_ajustes_afectos++;
            }else{
                cantidad_ajustes_no_afectos++;
            }
        }        
        if (this.recibo.ajustes_por_redondeo!=0d){
            cantidad_ajustes_no_afectos++;
        }
        if (cantidad_ajustes_afectos>0 || (devolucion_por_promocion!=0 || devolucion_cargos_fijos!=0 )){
            y-=salto_linea;
            contador_indice++;
            y=this.drawStringLimitWidth(contador_indice+". AJUSTES AFECTOS A IGV", col_a , y,max_w_a,salto_linea ,fontBold, font_size);
            y-=salto_linea;
            incide_va=0;
            for(AJUS ajus : this.recibo.mi_ajustes){                
                if (ajus.getST_AFEC_IGV_ForJSON()){                    
                    this.drawString(this.getStringDecimalFormat(Math.abs(ajus.getIM_MONT_ForJSON())*-1d,2), col_c , y,fontPlain,font_size,EReciboTextAlign.DERECHA);            
                    y=this.drawStringLimitWidth(Character.toString((char)(incide_va+97))+". "+ajus.getDE_GLOS_ForJSON(), col_a+5 , y,max_w_a,salto_linea ,fontPlain, font_size);                    
                    incide_va++;
                }
            }
            
            if (devolucion_por_promocion!=0){                
                this.drawString(this.getStringDecimalFormat(devolucion_por_promocion,2), col_c , y,fontPlain,font_size,EReciboTextAlign.DERECHA);            
                y=this.drawStringLimitWidth(Character.toString((char)(incide_va+97))+". "+ReciboPdf.GLOSA_DEVOLUCION_PROMOCION, col_a+5 , y,max_w_a,salto_linea ,fontPlain, font_size);                
                incide_va++;
            }
            if (devolucion_cargos_fijos!=0){                
                this.drawString(this.getStringDecimalFormat(devolucion_cargos_fijos,2), col_c , y,fontPlain,font_size,EReciboTextAlign.DERECHA);            
                y=this.drawStringLimitWidth(Character.toString((char)(incide_va+97))+". "+ReciboPdf.GLOSA_DEVOLUCION_CARGOS_FIJOS, col_a+5 , y,max_w_a,salto_linea ,fontPlain, font_size);        
                incide_va++;
            }            
        }
        if (cantidad_ajustes_no_afectos>0){
            y-=salto_linea;
            contador_indice++;
            y=this.drawStringLimitWidth(contador_indice+". AJUSTES NO AFECTOS A IGV", col_a , y,max_w_a,salto_linea ,fontBold, font_size);
            y-=salto_linea;
            incide_va=0;
            for(AJUS ajus : this.recibo.mi_ajustes){
                if (!ajus.getST_AFEC_IGV_ForJSON()){                    
                    this.drawString(this.getStringDecimalFormat(Math.abs(ajus.getIM_MONT_ForJSON())*-1d,2), col_c , y,fontPlain,font_size,EReciboTextAlign.DERECHA);            
                    y=this.drawStringLimitWidth(Character.toString((char)(incide_va+97))+". "+ajus.getDE_GLOS_ForJSON(), col_a+5 , y,max_w_a,salto_linea ,fontPlain, font_size);                    
                    incide_va++;
                }
            }
            if (this.recibo.ajustes_por_redondeo!=0d){                
                this.drawString(this.getStringDecimalFormat(this.recibo.ajustes_por_redondeo,2), col_c , y,fontPlain,font_size,EReciboTextAlign.DERECHA);            
                y=this.drawStringLimitWidth(Character.toString((char)(incide_va+97))+". Ajuste por redondeo", col_a+5 , y,max_w_a,salto_linea ,fontPlain, font_size);                    
                incide_va++;
            }
        }        
    }
    
    private void addCuadroFooter(float y){
        Date FE_EMIS=null,FE_VENC=null;
        String moneda="S/";
        Double totales[]=this.recibo.getTotales();
        if (this.recibo.esMonedaDolaresRecibo()) moneda="$";
        float x=30F;
        float h=20F;
        float wt=0;
        float ys=0;
        float yts=0;
        float br=1.3F;
        float rz_footer_glosa=10;
        float rz_footer_monto=20;
        float font_size=Font_Size_General-5;
        
        if(this.recibo.cier!= null){
           FE_EMIS=this.recibo.cier.getFE_EMIS_ForJSON();
           FE_VENC=this.recibo.cier.getFE_VENC_ForJSON();
        }else{
           FE_EMIS=new Date();
           FE_VENC=new Date();
        }
        
        String glosa1="";
        String glosa2="";
        if (moneda.equals("S/")){
            glosa1="Pago de Servicio Telefon\u00eda Americatel - Facturaci\u00f3n Directa.";
            glosa2="Pago de Servicio Larga Distancia Americatel.";
        }else{
            glosa1="Pago de Servicio Telefon\u00eda Americatel - Otros Servicios D\u00f3lares.";
            glosa2="Pago de Servicio Americatel - Facturaci\u00f3n D\u00f3lares.";
        }
        
        ys=y;
        float marginLeftRectangle=25F;
        float widthRectangle=450F;
        float heightRectangle=65F;
        float marginInRectangle=5F;
        font_size=Font_Size_General-6;
        this.drawRectangle(x+marginLeftRectangle, ys-heightRectangle,widthRectangle, heightRectangle, br, Color.BLACK, Color.WHITE);
        this.drawString("", x+marginLeftRectangle+marginInRectangle,ys,font_size);ys-=rz_footer_glosa;
        this.drawString("ESTIMADO CLIENTE:", x+marginLeftRectangle+marginInRectangle,ys,font_size);ys-=rz_footer_glosa;        
        this.drawStringSize("S\u00edrvase cancelar su recibo en cualquier agencia del Banco de Cr\u00e9dito, Scotiabank \u00f3 Interbank. Indicar en ventanilla: C\u00f3digo n\u00famero de recibo "+this.recibo.getNumeroRecibo(), x+marginLeftRectangle+marginInRectangle,ys,font_size);ys-=rz_footer_glosa;
        this.drawStringSize(" - BCP: "+glosa1, x+marginLeftRectangle+marginInRectangle+10F,ys,font_size);ys-=rz_footer_glosa;
        this.drawStringSize(" - Scotiabank \u00f3 Interbank: "+glosa2, x+marginLeftRectangle+marginInRectangle+10F,ys,font_size);ys-=rz_footer_glosa;
        this.drawStringSize("Si realiza el pago v\u00eda Telecr\u00e9dito \u00f3 Telebanking, por favor notificarlo enviando el detalle (n\u00b0 documentos cancelados) al correo recaudacion@americatel.com.pe.", x+marginLeftRectangle+marginInRectangle,ys,font_size);ys-=rz_footer_glosa;
        this.drawStringSize("Ahora puedes realizar tus pagos en efectivo en nuestra oficina comercial", x+marginLeftRectangle+marginInRectangle,ys,font_size);ys-=rz_footer_glosa;
        
        ys=ys-rz_footer_glosa*0.7F;
        this.drawStringSize("No afecto al regimen de retenci\u00f3n del IGV, seg\u00fan resoluci\u00f3n de superitendencia N\u00b0 037-2002/SUNAT", x,ys,font_size);ys-=rz_footer_glosa;
        ys=ys-rz_footer_glosa*1.5F;
        yts=ys;
        wt=80;
        font_size=Font_Size_General-5;
        //F emision
        this.drawRectangle(x, yts,wt, h, br, Color.BLACK, Color.LIGHT_GRAY);
        this.drawStringCenter("FECHA DE EMISI\u00d3N", x, yts,wt, h,fontPlain,font_size);yts-=rz_footer_monto;
        this.drawRectangle(x, yts,wt, h, br, Color.BLACK, Color.WHITE);
        this.drawStringCenter(FechaHora.getStringDateShortStringMonth(FE_EMIS), x, yts,wt, h,fontPlain,font_size);
        //F vencimiento
        x+=wt-1;
        wt=85;
        yts=ys;
        this.drawRectangle(x, yts, wt, h, br, Color.BLACK, Color.LIGHT_GRAY);
        this.drawStringCenter("FECHA VENCIMIENTO", x, yts,wt, h,fontPlain,font_size);yts-=rz_footer_monto;
        this.drawRectangle(x, yts,wt, h, br, Color.BLACK, Color.WHITE);
        this.drawStringCenter(FechaHora.getStringDateShortStringMonth(FE_VENC), x, yts,wt, h,fontPlain,font_size);
        //NETO USD
        x+=wt-1;
        wt=65;
        yts=ys;
        this.drawRectangle(x, yts, wt, h, br, Color.BLACK, Color.LIGHT_GRAY);
        this.drawStringCenter("VALOR VENTA ("+moneda+")", x, yts,wt, h,fontPlain,font_size);yts-=rz_footer_monto;
        this.drawRectangle(x, yts,wt, h, br, Color.BLACK, Color.WHITE);
        this.drawStringCenter(this.getStringDecimalFormat(totales[0],2), x, yts,wt, h,fontPlain,font_size);
        //AJUSTE AFECTO IGV
        x+=wt-1;
        wt=70;
        yts=ys;
        this.drawRectangle(x, yts, wt, h, br, Color.BLACK, Color.LIGHT_GRAY);
        this.drawStringCenter("AJUSTE", x, yts+4,wt, h,fontPlain,font_size);
        this.drawStringCenter("(AFECTO A IGV)", x, yts-2,wt, h,fontPlain,font_size);
        yts-=rz_footer_monto;
        this.drawRectangle(x, yts,wt, h, br, Color.BLACK, Color.WHITE);
        this.drawStringCenter(this.getStringDecimalFormat(totales[1],2), x, yts,wt, h,fontPlain,font_size);        
        //IGV
        x+=wt-1;
        wt=65;
        yts=ys;
        this.drawRectangle(x, yts, wt, h, br, Color.BLACK, Color.LIGHT_GRAY);
        this.drawStringCenter("IGV 18% ("+moneda+")", x, yts,wt, h,fontPlain,font_size);yts-=rz_footer_monto;
        this.drawRectangle(x, yts,wt, h, br, Color.BLACK, Color.WHITE);
        this.drawStringCenter(this.getStringDecimalFormat(totales[2],2), x, yts,wt, h,fontPlain,font_size);
        //AJUSTE NO AFECTO IGV
        x+=wt-1;
        wt=75;
        yts=ys;
        this.drawRectangle(x, yts, wt, h, br, Color.BLACK, Color.LIGHT_GRAY);
        this.drawStringCenter("AJUSTE", x, yts+4,wt, h,fontPlain,font_size);
        this.drawStringCenter("(NO AFECTO A IGV)", x, yts-2,wt, h,fontPlain,font_size);
        yts-=rz_footer_monto;
        this.drawRectangle(x, yts,wt, h, br, Color.BLACK, Color.WHITE);
        this.drawStringCenter(this.getStringDecimalFormat((totales[3]),2), x, yts,wt, h,fontPlain,font_size);        
        //TOTAL
        x+=wt-1;
        wt=82;
        yts=ys;
        this.drawRectangle(x, yts, wt, h, br, Color.BLACK, Color.LIGHT_GRAY);        
        this.drawStringCenter("TOTAL RECIBO ("+moneda+")", x, yts,wt, h,fontPlain,font_size);yts-=rz_footer_monto;
        this.drawRectangle(x, yts,wt, h, br, Color.BLACK, Color.WHITE);
        this.drawStringCenter(this.getStringDecimalFormat(totales[4],2), x, yts,wt, h,fontPlain,font_size);
        
    }   

    public Recibo getRecibo() {
        return recibo;
    }
    
     
}
