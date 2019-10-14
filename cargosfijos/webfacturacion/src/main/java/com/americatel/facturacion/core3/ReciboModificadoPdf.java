/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.core3;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.JPEGFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import com.americatel.facturacion.Utils.Constante;
import com.americatel.facturacion.Utils.Numero;
import com.americatel.facturacion.core2.LibPdf;
import com.americatel.facturacion.cores.EReciboTextAlign;
import com.americatel.facturacion.fncs.FechaHora;
import com.americatel.facturacion.fncs.Path;
import com.americatel.facturacion.fncs.fnc;
import com.americatel.facturacion.models.AJUS;
import com.americatel.facturacion.models.CIER;
import com.americatel.facturacion.models.CLIE;
import com.americatel.facturacion.models.DEPA;
import com.americatel.facturacion.models.DIST;
import com.americatel.facturacion.models.NEGO;
import com.americatel.facturacion.models.NEGO_SUCU;
import com.americatel.facturacion.models.PROV;
import com.americatel.facturacion.models.RECI_GLOS;

/**
 *
 * @author rordonez
 */
public class ReciboModificadoPdf extends LibPdf {
    Recibo recibo=null;
    int cantidad_sucursales_facturan;
    private float Font_Size_General=11;// min 7 por que existe un Font_Size_General-6
    protected List<PDPageContentStream> coss=new ArrayList<PDPageContentStream>();    
    public ReciboModificadoPdf(Recibo recibo) {
        this.recibo=recibo;
        //this.proceso_negocio=recibo.getProcesoNegocio();
        this.cantidad_sucursales_facturan=this.recibo.getCantidad_Sucursales_Facturan();
    }
    
    public void crearPdf(){
//        this.addPage();  
//        this.crearPdfDetallado();
//        this.enumerarPaginasRecibo();   
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
    
    private void addPageUnoBoleta(){
        float y=850;
        this.addHeaderBoleta(y);
    }
    
     private void addHeaderBoleta(float y) {
        CIER cier=this.recibo.cier;
        CLIE clie=this.recibo.bole_modificado.getClie_ForJSON();
//        SUCU sucu=this.recibo.bole_modificado.getPrimeraSucursalFacturada();
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
        this.drawStringChico( this.recibo.bole_modificado.getDE_DIRE_FISC_ForJSON()+", "+dist.getNO_DIST_ForJSON()+" - "+prov.getNO_PROV_ForJSON()+" - "+depa.getNO_DEPA_ForJSON()  ,posInfoA, ty);
        
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
        this.drawStringChico( this.recibo.bole_modificado.getNO_MONE_FACT_ForJSON() ,posInfoB, ty); 
        
        ty-=alto; 
        this.drawStringChico("C\u00f3digo: ",posFieldA, ty);
        this.drawStringChico( clie.getDE_CODI_BUM_ForJSON() ,posInfoA, ty);
        
        
        ty-=alto;   
        this.drawStringChico("Negocio: ",posFieldA, ty);
        this.drawStringChico(this.recibo.nego.getCO_NEGO_ForJSON().toString(), posInfoA, ty);
        
        
        ty-=alto;        
    }
    public void crearPdfDetalladoBoleta(float inPosition ){
        Map<String,Object[]> resumen_planes=new HashMap<String, Object[]>();
        Map<String,Object[]> resumen_sss=new HashMap<String, Object[]>();
        Map<Integer,Object[]> resumen_su=new HashMap<Integer, Object[]>();
        List<AJUS> ajustes_aigv=new ArrayList<AJUS>();
        List<AJUS> ajustes_naigv=new ArrayList<AJUS>();
        int cantidad_ajustes_afectos=0,cantidad_ajustes_no_afectos=0;
        
        for(Map.Entry<NEGO_SUCU,List<RECI_GLOS>> entry : recibo.mi_data_recibo.entrySet()){
            NEGO_SUCU sucu_data=entry.getKey();
            List<RECI_GLOS> glosas=entry.getValue();
            Integer co_nego_sucu=sucu_data.getCO_NEGO_SUCU_ForJSON();
            for(RECI_GLOS glos:glosas){
                String nombre=glos.getNO_GLOS_ForJSON();
                Double m=glos.getIM_MONT_ForJSON();
                //Plan y Diferencial de cargos fijos
                if (glos.getTI_GLOS_ForJSON()==1 || glos.getTI_GLOS_ForJSON()==8){
                    if (!resumen_planes.containsKey(nombre)){
                        resumen_planes.put(nombre, new Object[]{nombre,0,0d,0});
                    }
                    Object data[]=resumen_planes.get(nombre);
                    if((Integer)data[3]==0 || (Integer)data[3]!=co_nego_sucu){
                        data[1]=(Integer)data[1]+1;
                    }
                    data[2]=(Double)data[2]+m;
                    data[3]=co_nego_sucu;
                } else if (glos.getTI_GLOS_ForJSON()==2){
                    //Servicio Suplementario
                    if (!resumen_sss.containsKey(nombre)){
                        resumen_sss.put(nombre, new Object[]{nombre,0,0d,0});
                    }
                    Object data[]=resumen_sss.get(nombre);
                    if((Integer)data[3]==0 || (Integer)data[3]!=co_nego_sucu){
                        data[1]=(Integer)data[1]+1;
                    }
                    data[2]=(Double)data[2]+m; 
                    data[3]=co_nego_sucu;
                } else if (glos.getTI_GLOS_ForJSON()==3){
                    //Servicio Unico
                    resumen_su.put(glos.getCO_RECI_GLOS_ForJSON(), new Object[]{nombre,0d,0});
                    Object data[]=resumen_su.get(glos.getCO_RECI_GLOS_ForJSON());
                    data[1]=(Double)m; 
                    data[2]=co_nego_sucu;
                }
            }
        }
        
        for(AJUS ajuste:recibo.mi_ajustes){
            if (ajuste.getST_AFEC_IGV_ForJSON()){
                cantidad_ajustes_afectos++;
                ajustes_aigv.add(ajuste);
            }else{
                cantidad_ajustes_no_afectos++;
                ajustes_naigv.add(ajuste);
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
        for(Map.Entry<Integer,Object[]> su : resumen_su.entrySet()){
            String nombre=fnc.capitalize((String)su.getValue()[0]);
            Double monto=(Double)su.getValue()[1];
            item++;
            this.drawStringChico( String.valueOf(item) , posInfoA, alturaInfo);
            this.drawStringChico( nombre, posInfoB, alturaInfo);
            this.drawStringChico(String.valueOf(1),posInfoC , alturaInfo);
            this.drawStringChico(this.getStringDecimalFormat(monto,2),posInfoD , alturaInfo);
            this.drawStringChico(this.getStringDecimalFormat(monto,2),posInfoE , alturaInfo);
            alturaInfo -= alturaCelda;
        }
        
        //Arreglar este ajuste por redondeo
        if (this.recibo.ajustes_por_redondeo!=0d){
            cantidad_ajustes_no_afectos++;
        }
        if (cantidad_ajustes_afectos>0){
            for(AJUS aigv : ajustes_aigv){
                String nombre=fnc.capitalize(aigv.getDE_GLOS_ForJSON());
                Double monto=aigv.getIM_MONT_ForJSON();
                item++;
                this.drawStringChico( String.valueOf(item) , posInfoA, alturaInfo);
                this.drawStringChico( nombre, posInfoB, alturaInfo);
                this.drawStringChico(String.valueOf(1),posInfoC , alturaInfo);
                this.drawStringChico(this.getStringDecimalFormat(monto,2),posInfoD , alturaInfo);
                this.drawStringChico(this.getStringDecimalFormat(monto,2),posInfoE , alturaInfo);
                alturaInfo -= alturaCelda;
            }          
        }
        if (cantidad_ajustes_no_afectos>0){
            for(AJUS anigv : ajustes_naigv){
                String nombre=fnc.capitalize(anigv.getDE_GLOS_ForJSON());
                Double monto=anigv.getIM_MONT_ForJSON();
                item++;
                this.drawStringChico( String.valueOf(item) , posInfoA, alturaInfo);
                this.drawStringChico( nombre, posInfoB, alturaInfo);
                this.drawStringChico(String.valueOf(1),posInfoC , alturaInfo);
                this.drawStringChico(this.getStringDecimalFormat(monto,2),posInfoD , alturaInfo);
                this.drawStringChico(this.getStringDecimalFormat(monto,2),posInfoE , alturaInfo);
                alturaInfo -= alturaCelda;
            } 
            //Corregir
            if (this.recibo.ajustes_por_redondeo!=0d){  
                item++;
                this.drawStringChico( String.valueOf(item) , posInfoA, alturaInfo);
                this.drawStringChico( Constante.GLOSA_AJUSTE_REDONDEO, posInfoB, alturaInfo);
                this.drawStringChico(String.valueOf(1),posInfoC , alturaInfo);
                this.drawStringChico(this.getStringDecimalFormat(this.recibo.ajustes_por_redondeo,2),posInfoD , alturaInfo);
                this.drawStringChico(this.getStringDecimalFormat(this.recibo.ajustes_por_redondeo,2),posInfoE , alturaInfo);
                alturaInfo -= alturaCelda;
            }
        } 
        
        
//        for(Map.Entry<NEGO_SUCU,List<RECI_GLOS>> entry : recibo.mi_data_recibo.entrySet()){
//            NEGO_SUCU sucu_data=entry.getKey();
//            List<RECI_GLOS> glosas=entry.getValue();
//            
//            Collections.sort(glosas, new Comparator<RECI_GLOS>() {
//                public int compare(RECI_GLOS o1, RECI_GLOS o2) {
//                    return new Integer(o1.getTI_GLOS_ForJSON()).compareTo(new Integer(o2.getTI_GLOS_ForJSON()));
//                }
//            });
//            
//            for(RECI_GLOS glos : glosas){                       
////                if (glos.getTI_GLOS_ForJSON()==1){
//                item++;
//                this.drawStringChico( String.valueOf(item) , posInfoA, alturaInfo);
//                this.drawStringChico( glos.getNO_GLOS_ForJSON(), posInfoB, alturaInfo);
//                this.drawStringChico(String.valueOf(1),posInfoC , alturaInfo);
//                this.drawStringChico(this.getStringDecimalFormat(glos.getIM_MONT_ForJSON(),2),posInfoD , alturaInfo);
//                this.drawStringChico(this.getStringDecimalFormat(glos.getIM_MONT_ForJSON(),2),posInfoE , alturaInfo);
////                    this.drawStringChico(this.getStringDecimalFormat(plan.getIM_MONT(),2),posInfoF , alturaInfo);
//                alturaInfo -= alturaCelda;
////                }
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
        
        
        Integer CO_MONE_FACT = this.recibo.bole_modificado.getCO_MONE_FACT_ForJSON();
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
        this.drawStringChico(this.recibo.getBole_modificado().getDE_PERI_ForJSON(), posInfoX_A1, (alturaInfo - (alturaCeldaTipoB*4)));
        
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
    
    private void addPageUno(){
        float y=570;
        this.addCuadroUsuario(y);                
        this.drawRectangle(35, y+25,this.rect.getWidth()-65  , 20, 2, Color.BLACK  , Color.LIGHT_GRAY);
        this.drawString("RESUMEN DE SERVICIOS", 40, y+32,fontBold,Font_Size_General-2); 
        try {                        
            BufferedImage awtImage = ImageIO.read(new File(Path.getPathWEB_INF()+"/../resources/sencha/facturacion/images/imagen_factura.jpg"));
            PDImageXObject ximage = JPEGFactory.createFromImage(document, awtImage);
            float scale = 0.75f;
            float w=ximage.getWidth()*scale;
            float h=ximage.getHeight()*scale;
            cos.drawXObject(ximage, this.rect.getWidth()-w-30, y-h+20, w , h);
        } catch (Exception ex) {
            //ex.printStackTrace();
        }        
        this.drawStringLimitWidth (this.recibo.getReci_modificado().getDE_PERI_ForJSON(), 40, y,350,9,this.fontBold,Font_Size_General-4);
        
        if (this.cantidad_sucursales_facturan==1){
            List<RECI_GLOS> glosas=recibo.mi_data_recibo.entrySet().iterator().next().getValue();
            String direccion=glosas.get(0).getDE_DIRE_SUCU_ForJSON();
            DIST dist=glosas.get(0).getDIST_ForJSON();
            y=this.drawStringLimitWidth ("Sucursal : "+direccion+ " ("+dist.getNO_DIST_ForJSON()+" - "+dist.getPROV_ForJSON().getNO_PROV_ForJSON()+" - "+dist.getPROV_ForJSON().getDEPA_ForJSON().getNO_DEPA_ForJSON()+")", 40, y-15,350,9,this.fontPlain,Font_Size_General-4);
            this.crearPdfResumen(y-10);
            this.addCuadroFooter(200);
        }else{
            this.crearPdfResumen(y-20);
            this.addCuadroFooter(200);            
        }
    } 
    public void addNumeroRecibo(){
        this.drawString("RECIBO N\u00b0 "+this.recibo.NumeroRecibo,this.rect.getWidth()-150,800,Font_Size_General-4);
    }      
    private void addCuadroUsuario(float y){
        float font_size=Font_Size_General-4;
        NEGO nego=this.recibo.nego;
        String numero_doc=this.recibo.reci_modificado.getDE_NUME_DOCU_ForJSON();
        String codi_bum=this.recibo.reci_modificado.getDE_CODI_BUM_ForJSON();
        String nombre_cliente=this.recibo.reci_modificado.getNO_CLIE_ForJSON();
        String dire_fiscal=this.recibo.reci_modificado.getDE_DIRE_FISC_ForJSON();
        DIST dist=this.recibo.distrito_fiscal;
        PROV prov=this.recibo.provincia_fiscal;
        DEPA depa=this.recibo.departamento_fiscal;
        
        this.drawRectangle(35, y+47, 530, 80, 2); 
        this.drawString("SE\u00d1OR (ES)",40, y+115,font_size);
        this.drawString("RUC / DOC.ID",this.rect.getWidth()-200, y+115,font_size);
        this.drawString(numero_doc,this.rect.getWidth()-100, y+115,font_size);
        this.drawString("CODIGO CLIENTE",this.rect.getWidth()-200, y+105,font_size);
        this.drawString(codi_bum,this.rect.getWidth()-100, y+105,font_size);
        this.drawStringLimitWidth(nombre_cliente,40, y+105,300,font_size+1,font_size);
        this.drawStringLimitWidth(dire_fiscal,40, y+85,300,font_size+1,font_size);
        //numero negocio
        this.drawString("N\u00b0 NEGOCIO",this.rect.getWidth()-200, y+85,font_size);
        this.drawString(nego.getCO_NEGO_ForJSON()+"",this.rect.getWidth()-100, y+85,font_size);
        //Ubicacion
        this.drawString(dist.getNO_DIST_ForJSON()+" - "+prov.getNO_PROV_ForJSON()+" - "+depa.getNO_DEPA_ForJSON(),40, y+70,font_size);
        //numero documento in info cliente
        this.drawString(this.recibo.NumeroRecibo+"",40, y+60,Font_Size_General-5);     
    } 
    public void crearPdfDetallado(){
        if (this.cantidad_sucursales_facturan>1){
            float font_size=Font_Size_General-6;
            float col_a=40;
            float col_b=110;
            float col_c=350;
            float salto_linea=font_size+1;
            float y=780;
            int van_sucursales=0;
            int van_letras=0;

            this.addPage();                  
            this.drawString("SE\u00d1OR (ES): ",col_a , y,fontBold,font_size);
            this.drawString(this.recibo.reci_modificado.getNO_CLIE_ForJSON(),col_b , y,font_size);
            y-=salto_linea;                  
            this.drawString("N\u00b0 NEGOCIO: ",col_a , y,fontBold,font_size);
            this.drawString(this.recibo.nego.getCO_NEGO_ForJSON().toString() ,col_b , y,font_size);
            y-=salto_linea*2F;
            this.drawRectangle(col_a-2, y, 500,salto_linea   , 1F, Color.BLACK,Color.LIGHT_GRAY);
            this.drawString("DETALLE DEL NEGOCIO",col_a , y+1,fontBold,font_size);
            y-=salto_linea*2; 
            
            for(Map.Entry<NEGO_SUCU,List<RECI_GLOS>> entry : recibo.mi_data_recibo.entrySet()){
               van_sucursales++; 
               NEGO_SUCU sucu_data=entry.getKey();
               List<RECI_GLOS> glosas=entry.getValue();
               String direccion=glosas.get(0).getDE_DIRE_SUCU_ForJSON();
               DIST dist=glosas.get(0).getDIST_ForJSON();
               this.drawString("Sucursal "+van_sucursales+": ",col_a , y,fontBold,font_size);
                y=this.drawStringLimitWidth(direccion+ " ("+dist.getNO_DIST_ForJSON()+" - "+dist.getPROV_ForJSON().getNO_PROV_ForJSON()+" - "+dist.getPROV_ForJSON().getDEPA_ForJSON().getNO_DEPA_ForJSON()+")",col_b , y,400,9,font_size);
                y-=salto_linea; 
                if (sucu_data.getDE_ORDE_SERV_ForJSON()!=null && sucu_data.getDE_ORDE_SERV_ForJSON().length()>0 ){
                    this.drawString("Orden de Compra:",col_a , y,fontBold,font_size);
                    this.drawString(sucu_data.getDE_ORDE_SERV_ForJSON(),col_b , y,font_size);
                    y-=salto_linea; 
                }
                
                van_letras=0;
                for(RECI_GLOS glos : glosas){
                    //Plan
                    if (glos.getTI_GLOS_ForJSON()==1){
                        this.drawString(Character.toString((char)(van_letras+97))+". "+fnc.capitalize(glos.getNO_GLOS_ForJSON()),col_a , y,font_size);
                        this.drawString(this.getStringDecimalFormat(glos.getIM_MONT_ForJSON(),2),col_c , y,fontPlain,font_size,EReciboTextAlign.DERECHA);
                        van_letras++; 
                        y-=salto_linea;
                    }
                }
                y-=salto_linea;
                for(RECI_GLOS glos : glosas){
                    //Servicio Suplementario
                    if (glos.getTI_GLOS_ForJSON()==2){
                        this.drawString(Character.toString((char)(van_letras+97))+". "+fnc.capitalize(glos.getNO_GLOS_ForJSON()),col_a , y,font_size);
                        this.drawString(this.getStringDecimalFormat(glos.getIM_MONT_ForJSON(),2),col_c , y,fontPlain,font_size,EReciboTextAlign.DERECHA);
                        van_letras++; 
                        y-=salto_linea;
                    }
                }
                y-=salto_linea;
                
            }                       
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
        Map<String,Object[]> resumen_sss=new HashMap<String, Object[]>();
        Map<Integer,Object[]> resumen_su=new HashMap<Integer, Object[]>();
        List<AJUS> ajustes_aigv=new ArrayList<AJUS>();
        List<AJUS> ajustes_naigv=new ArrayList<AJUS>();
        int cantidad_ajustes_afectos=0,cantidad_ajustes_no_afectos=0;
        
        for(Map.Entry<NEGO_SUCU,List<RECI_GLOS>> entry : recibo.mi_data_recibo.entrySet()){
            NEGO_SUCU sucu_data=entry.getKey();
            List<RECI_GLOS> glosas=entry.getValue();
            
            Integer co_nego_sucu=sucu_data.getCO_NEGO_SUCU_ForJSON();
            for(RECI_GLOS glos:glosas){
                String nombre=glos.getNO_GLOS_ForJSON();
                Double m=glos.getIM_MONT_ForJSON();
                //Plan y Diferencial de cargos fijos
                if (glos.getTI_GLOS_ForJSON()==1 || glos.getTI_GLOS_ForJSON()==8){
                    if (!resumen_planes.containsKey(nombre)){
                        resumen_planes.put(nombre, new Object[]{nombre,0,0d,0});
                    }
                    Object data[]=resumen_planes.get(nombre);
                    if((Integer)data[3]==0 || (Integer)data[3]!=co_nego_sucu){
                        data[1]=(Integer)data[1]+1;
                    }
                    data[2]=(Double)data[2]+m;
                    data[3]=co_nego_sucu;
                } else if (glos.getTI_GLOS_ForJSON()==2){
                    //Servicio Suplementario
                    if (!resumen_sss.containsKey(nombre)){
                        resumen_sss.put(nombre, new Object[]{nombre,0,0d,0});
                    }
                    Object data[]=resumen_sss.get(nombre);
                    if((Integer)data[3]==0 || (Integer)data[3]!=co_nego_sucu){
                        data[1]=(Integer)data[1]+1;
                    }
                    data[2]=(Double)data[2]+m; 
                    data[3]=co_nego_sucu;
                } else if (glos.getTI_GLOS_ForJSON()==3){
                    //Servicio Unico
                    resumen_su.put(glos.getCO_RECI_GLOS_ForJSON(), new Object[]{nombre,0d,0});
                    Object data[]=resumen_su.get(glos.getCO_RECI_GLOS_ForJSON());
                    data[1]=(Double)m; 
                    data[2]=co_nego_sucu;
                }
            }
        }
        
        for(AJUS ajuste:recibo.mi_ajustes){
            if (ajuste.getST_AFEC_IGV_ForJSON()){
                cantidad_ajustes_afectos++;
                ajustes_aigv.add(ajuste);
            }else{
                cantidad_ajustes_no_afectos++;
                ajustes_naigv.add(ajuste);
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
        for(Map.Entry<String,Object[]> p : resumen_planes.entrySet()){
            String nombre=fnc.capitalize((String)p.getValue()[0]);
            Integer cantidad=(Integer)p.getValue()[1];
            Double monto=(Double)p.getValue()[2];
            this.drawString("("+cantidad+")", col_b , y,font_size);
            this.drawString(this.getStringDecimalFormat(monto,2), col_c , y,fontPlain,font_size,EReciboTextAlign.DERECHA);            
            y=this.drawStringLimitWidth(Character.toString((char)(incide_va+97))+". "+nombre, col_a+5 , y,max_w_a,salto_linea ,fontPlain, font_size);
            incide_va++;
        }
        for(Map.Entry<String,Object[]> s : resumen_sss.entrySet()){
            String nombre=fnc.capitalize((String)s.getValue()[0]);
            Integer cantidad=(Integer)s.getValue()[1];
            Double monto=(Double)s.getValue()[2];
            this.drawString("("+cantidad+")", col_b , y,font_size);
            this.drawString(this.getStringDecimalFormat(monto,2), col_c , y,fontPlain,font_size,EReciboTextAlign.DERECHA);            
            y=this.drawStringLimitWidth(Character.toString((char)(incide_va+97))+". "+nombre, col_a+5 , y,max_w_a,salto_linea ,fontPlain, font_size);
            incide_va++;
        }     
        for(Map.Entry<Integer,Object[]> su : resumen_su.entrySet()){
            String nombre=fnc.capitalize((String)su.getValue()[0]);
            Double monto=(Double)su.getValue()[1];
            
            this.drawString("("+1+")", col_b , y,font_size);
            this.drawString(this.getStringDecimalFormat(monto,2), col_c , y,fontPlain,font_size,EReciboTextAlign.DERECHA);            
            y=this.drawStringLimitWidth(Character.toString((char)(incide_va+97))+". "+fnc.capitalize(nombre), col_a+5 , y,max_w_a,salto_linea ,fontPlain, font_size);                        
            incide_va++;
        }
        //Arreglar este ajuste por redondeo
        if (this.recibo.ajustes_por_redondeo!=0d){
            cantidad_ajustes_no_afectos++;
        }
        if (cantidad_ajustes_afectos>0){
            y-=salto_linea;
            contador_indice++;
            y=this.drawStringLimitWidth(contador_indice+". AJUSTES AFECTOS A IGV", col_a , y,max_w_a,salto_linea ,fontBold, font_size);
            y-=salto_linea;
            incide_va=0;
            for(AJUS aigv : ajustes_aigv){
                String nombre=fnc.capitalize(aigv.getDE_GLOS_ForJSON());
                Double monto=aigv.getIM_MONT_ForJSON();
                
                this.drawString(this.getStringDecimalFormat(Math.abs(monto)*-1d,2), col_c , y,fontPlain,font_size,EReciboTextAlign.DERECHA);            
                y=this.drawStringLimitWidth(Character.toString((char)(incide_va+97))+". "+nombre, col_a+5 , y,max_w_a,salto_linea ,fontPlain, font_size);                    
                incide_va++;
            }          
        }
        if (cantidad_ajustes_no_afectos>0){
            y-=salto_linea;
            contador_indice++;
            y=this.drawStringLimitWidth(contador_indice+". AJUSTES NO AFECTOS A IGV", col_a , y,max_w_a,salto_linea ,fontBold, font_size);
            y-=salto_linea;
            incide_va=0;
            for(AJUS anigv : ajustes_naigv){
                String nombre=fnc.capitalize(anigv.getDE_GLOS_ForJSON());
                Double monto=anigv.getIM_MONT_ForJSON();
                
                this.drawString(this.getStringDecimalFormat(Math.abs(monto)*-1d,2), col_c , y,fontPlain,font_size,EReciboTextAlign.DERECHA);            
                y=this.drawStringLimitWidth(Character.toString((char)(incide_va+97))+". "+nombre, col_a+5 , y,max_w_a,salto_linea ,fontPlain, font_size);                    
                incide_va++;
            } 
            //Corregir
            if (this.recibo.ajustes_por_redondeo!=0d){                
                this.drawString(this.getStringDecimalFormat(this.recibo.ajustes_por_redondeo,2), col_c , y,fontPlain,font_size,EReciboTextAlign.DERECHA);            
                y=this.drawStringLimitWidth(Character.toString((char)(incide_va+97))+". Ajuste por redondeo", col_a+5 , y,max_w_a,salto_linea ,fontPlain, font_size);                    
                incide_va++;
            }
        }        
    }    
    
    public void cerrarCoss(){
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
    private void addCuadroFooter(float y){
        Date FE_EMIS=null,FE_VENC=null;
        String moneda="SOLES";
        //Double totales[]=this.recibo.getTotales();
        if (this.recibo.reci_modificado.getCO_MONE_FACT_ForJSON()==2) moneda="USD";
        float x=30F;
        float h=20F;
        float wt=0;
        float ys=0;
        float yts=0;
        float br=1.3F;
        float rz_footer_glosa=10;
        float rz_footer_monto=20;
        float font_size=Font_Size_General-5;
        

        FE_EMIS=this.recibo.reci_modificado.getFE_EMIS_ForJSON();
        FE_VENC=this.recibo.reci_modificado.getFE_VENC_ForJSON();

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
        this.drawStringSize(" - BCP: Pago de Servicio Telefon\u00eda Americatel - Otros Servicios D\u00f3lares.", x+marginLeftRectangle+marginInRectangle+10F,ys,font_size);ys-=rz_footer_glosa;
        this.drawStringSize(" - Scotiabank \u00f3 Interbank: Pago de Servicio Americatel - Facturaci\u00f3n D\u00f3lares.", x+marginLeftRectangle+marginInRectangle+10F,ys,font_size);ys-=rz_footer_glosa;
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
        this.drawStringCenter("NETO ("+moneda+")", x, yts,wt, h,fontPlain,font_size);yts-=rz_footer_monto;
        this.drawRectangle(x, yts,wt, h, br, Color.BLACK, Color.WHITE);
        this.drawStringCenter(this.getStringDecimalFormat(this.recibo.getReci_modificado().getIM_NETO_ForJSON(),2), x, yts,wt, h,fontPlain,font_size);
        //AJUSTE AFECTO IGV
        x+=wt-1;
        wt=70;
        yts=ys;
        this.drawRectangle(x, yts, wt, h, br, Color.BLACK, Color.LIGHT_GRAY);
        this.drawStringCenter("AJUSTE", x, yts+4,wt, h,fontPlain,font_size);
        this.drawStringCenter("(AFECTO A IGV)", x, yts-2,wt, h,fontPlain,font_size);
        yts-=rz_footer_monto;
        this.drawRectangle(x, yts,wt, h, br, Color.BLACK, Color.WHITE);
        this.drawStringCenter(this.getStringDecimalFormat(this.recibo.getReci_modificado().getIM_AJUS_SIGV_ForJSON(),2), x, yts,wt, h,fontPlain,font_size);        
        //IGV
        x+=wt-1;
        wt=65;
        yts=ys;
        this.drawRectangle(x, yts, wt, h, br, Color.BLACK, Color.LIGHT_GRAY);
        this.drawStringCenter("IGV 18% ("+moneda+")", x, yts,wt, h,fontPlain,font_size);yts-=rz_footer_monto;
        this.drawRectangle(x, yts,wt, h, br, Color.BLACK, Color.WHITE);
        this.drawStringCenter(this.getStringDecimalFormat(this.recibo.getReci_modificado().getIM_IGV_ForJSON(),2), x, yts,wt, h,fontPlain,font_size);
        //AJUSTE NO AFECTO IGV
        x+=wt-1;
        wt=75;
        yts=ys;
        this.drawRectangle(x, yts, wt, h, br, Color.BLACK, Color.LIGHT_GRAY);
        this.drawStringCenter("AJUSTE", x, yts+4,wt, h,fontPlain,font_size);
        this.drawStringCenter("(NO AFECTO A IGV)", x, yts-2,wt, h,fontPlain,font_size);
        yts-=rz_footer_monto;
        this.drawRectangle(x, yts,wt, h, br, Color.BLACK, Color.WHITE);
        this.drawStringCenter(this.getStringDecimalFormat(this.recibo.getReci_modificado().getIM_AJUS_NIGV_ForJSON(),2), x, yts,wt, h,fontPlain,font_size);        
        //TOTAL
        x+=wt-1;
        wt=82;
        yts=ys;
        this.drawRectangle(x, yts, wt, h, br, Color.BLACK, Color.LIGHT_GRAY);        
        this.drawStringCenter("TOTAL RECIBO ("+moneda+")", x, yts,wt, h,fontPlain,font_size);yts-=rz_footer_monto;
        this.drawRectangle(x, yts,wt, h, br, Color.BLACK, Color.WHITE);
        this.drawStringCenter(this.getStringDecimalFormat(this.recibo.getReci_modificado().getIM_TOTA_ForJSON(),2), x, yts,wt, h,fontPlain,font_size);
        
    }     
}
