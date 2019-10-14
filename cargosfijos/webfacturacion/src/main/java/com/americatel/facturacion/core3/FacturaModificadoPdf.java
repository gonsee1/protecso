/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.core3;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;

import com.americatel.facturacion.Utils.Constante;
import com.americatel.facturacion.Utils.Numero;
import com.americatel.facturacion.core2.LibPdf;
import com.americatel.facturacion.fncs.FechaHora;
import com.americatel.facturacion.fncs.fnc;
import com.americatel.facturacion.models.AJUS;
import com.americatel.facturacion.models.CIER;
import com.americatel.facturacion.models.CLIE;
import com.americatel.facturacion.models.DEPA;
import com.americatel.facturacion.models.DIST;
import com.americatel.facturacion.models.FACT_GLOS;
import com.americatel.facturacion.models.NEGO;
import com.americatel.facturacion.models.NEGO_SUCU;
import com.americatel.facturacion.models.PROV;
import com.americatel.facturacion.models.SUCU;

/**
 *
 * @author rordonez
 */
public class FacturaModificadoPdf extends LibPdf {
    Factura factura=null;
    int cantidad_sucursales_facturan;
    private float Font_Size_General=11;// min 7 por que existe un Font_Size_General-6
    protected List<PDPageContentStream> coss=new ArrayList<PDPageContentStream>();    
    public FacturaModificadoPdf(Factura factura) {
        this.factura=factura;
        //this.proceso_negocio=recibo.getProcesoNegocio();
        this.cantidad_sucursales_facturan=this.factura.getCantidad_Sucursales_Facturan();
    }
    
    public void crearPdf(){
        this.addPage();
        this.addDetalle(650);
        this.addCuadroFooter(150);
        this.cerrarCoss();
        
    }
    
    private void addPageUno(){
        float y=850;
        this.addHeader(y);
    } 
    
    private void addPage(){
        numero_paginas++;
        this.page = new PDPage(PDRectangle.A4);
        this.document.addPage(this.page);
        this.rect= this.page.getMediaBox();
        this.createContentStream();
        coss.add(this.cos);
        if (numero_paginas==1){            
            this.addPageUno();
        }else{
            //this.addDemasPages();
        } 
    }
    
    private void addHeader(float y) {
        CIER cier=this.factura.cier;
        CLIE clie=this.factura.fact_modificado.getClie_ForJSON();
        SUCU sucu=this.factura.sucursal_fiscal;
        DIST dist=this.factura.distrito_fiscal;
        PROV prov=this.factura.provincia_fiscal;
        DEPA depa=this.factura.departamento_fiscal;
        NEGO nego=this.factura.nego;
        float alto=8;
        float ty=y-80;        
        this.drawStringChico("Americatel Per\u00fa S.A.",40, ty);ty-=alto;
        this.drawStringChico("Av. Manuel Olguin 211 piso 9 - Santiado de Surco - Lima - Lima - Per\u00fa",40, ty);ty-=alto;
        this.drawStringChico("Telf.: 710-1977 // Fax: 710-1299",40, ty);ty-=alto;
        this.drawStringChico("P\u00e1gina Web: www.americatel.com.pe",40, ty);ty-=alto;
        this.drawStringChico("e-mail: americatel@americatel.com.pe",40, ty);ty-=alto;
        
        this.drawRectangle(300,y-120 , 250, 90,5);
        this.drawString("R.U.C. N\u00b0 20428698569", 320, y-50,this.fontBold,20);
        this.drawString("FACTURA", 380, y-80,this.fontBold,20);
        this.drawString("010 - "+com.americatel.facturacion.fncs.Numero.formatearNumeroDigitos(this.factura.getNumeroFactura(), 6), 360, y-110,this.fontBold,20);
        
        ty-=alto;
        ty-=alto;
        this.drawStringChico("Lugar y Fecha de Emisi\u00f3n",40, ty);
        if(cier != null){
        this.drawStringChico( FechaHora.getStringDateShortStringMonth(cier.getFE_EMIS_ForJSON()),120, ty);
        }else{
        this.drawStringChico( FechaHora.getStringDateShortStringMonth(new Date()),120, ty);
        }
        
        ty-=alto;
        this.drawStringChico("Se\u00f1or(es): ",40, ty);
        this.drawStringChico( clie.getFullNameCliente() ,120, ty);
        
        this.drawStringChico("Referencia: ",400, ty);
        this.drawStringChico( nego.getCO_NEGO_ForJSON().toString() ,480, ty); 
        
        ty-=alto;        
        this.drawStringChico("C\u00f3digo: ",40, ty);
        this.drawStringChico( clie.getDE_CODI_BUM_ForJSON() ,120, ty);
        
        this.drawStringChico("Condici\u00f3n de Pago: ",400, ty);
        this.drawStringChico( "" ,480, ty);         
        
        ty-=alto;   
        this.drawStringChico("Direcci\u00f3n: ",40, ty);
        this.drawStringChico( this.factura.fact_modificado.getDE_DIRE_FISC_ForJSON()+", "+dist.getNO_DIST_ForJSON()+" - "+prov.getNO_PROV_ForJSON()+" - "+depa.getNO_DEPA_ForJSON()  ,120, ty);
        ty-=alto;   
        this.drawStringChico("R.U.C.: ",40, ty);
        this.drawStringChico( clie.getDE_NUME_DOCU_ForJSON()  ,120, ty);
        ty-=alto; 
        this.drawStringChico("Fecha de Vencimiento:",40, ty);
        if(cier != null){
            this.drawStringChico( FechaHora.getStringDateShortStringMonth(cier.getFE_VENC_ForJSON()),120, ty);
        }else{
            this.drawStringChico( FechaHora.getStringDateShortStringMonth(new Date()),120, ty);
        }
        
        ty-=alto;        
    } 
    
    private void addDetalle(float y) {
        Map<String,Object[]> resumen_planes=new HashMap<String, Object[]>();
        Map<String,Object[]> resumen_sss=new HashMap<String, Object[]>();
        Map<Integer,Object[]> resumen_su=new HashMap<Integer, Object[]>();
        List<AJUS> ajustes_aigv=new ArrayList<AJUS>();
        List<AJUS> ajustes_naigv=new ArrayList<AJUS>();
        int cantidad_ajustes_afectos=0,cantidad_ajustes_no_afectos=0;
        
        for(Map.Entry<NEGO_SUCU,List<FACT_GLOS>> entry : factura.mi_data_factura.entrySet()){
            NEGO_SUCU sucu_data=entry.getKey();
            List<FACT_GLOS> glosas=entry.getValue();
            Integer co_nego_sucu=sucu_data.getCO_NEGO_SUCU_ForJSON();
            for(FACT_GLOS glos:glosas){
                String nombre=glos.getNO_GLOS_ForJSON();
                Double m=glos.getIM_MONT_ForJSON();
                //Plan y Diferencial de cargos fijos
                if (glos.getTI_GLOS_ForJSON()==1 || glos.getTI_GLOS_ForJSON()==8){
                    if (!resumen_planes.containsKey(nombre)){
                        resumen_planes.put(nombre, new Object[]{nombre,1,m,co_nego_sucu,""});
                    }else{
                        Object data[]=resumen_planes.get(nombre);
                        data[1]=(Integer)data[1]+1;
                        data[2]=(Double)data[2]+m;
                    }
                } else if (glos.getTI_GLOS_ForJSON()==2){
                    if (!resumen_sss.containsKey(nombre)){
                        resumen_sss.put(nombre, new Object[]{nombre,1,m,co_nego_sucu,""});
                    }else{
                        Object data[]=resumen_sss.get(nombre);
                        data[1]=(Integer)data[1]+1;
                        data[2]=(Double)data[2]+m;
                    }
                } else if (glos.getTI_GLOS_ForJSON()==3){
                    //Servicio Unico
                    resumen_su.put(glos.getCO_FACT_GLOS_ForJSON(), new Object[]{nombre,0d,0});
                    Object data[]=resumen_su.get(glos.getCO_FACT_GLOS_ForJSON());
                    data[1]=(Double)m; 
                    data[2]=co_nego_sucu;
                }
            }
        }
        
        float x_a=40;
        float x_b=300;
        float x_c=450;
        float x_d=500;
        float alto=10;
        
        for(Map.Entry<String,Object[]> p : resumen_planes.entrySet()){
            String nombre=fnc.capitalize((String)p.getValue()[0]);
            Integer cantidad=(Integer)p.getValue()[1];
            Double monto=(Double)p.getValue()[2];
            String periodo=(String)p.getValue()[4];

            this.drawStringChico( nombre , x_a, y);
            if(periodo!=null && !periodo.isEmpty()){
                this.drawStringPequenio( "("+periodo+")" , x_b, y);
            }
            this.drawStringChico("("+cantidad+")", x_c , y);
            this.drawStringChico( this.getStringDecimalFormat(monto,2), x_d, y);

            y-=alto;
            
        }
        for(Map.Entry<String,Object[]> s : resumen_sss.entrySet()){
            
            String nombre=fnc.capitalize((String)s.getValue()[0]);
            Integer cantidad=(Integer)s.getValue()[1];
            Double monto=(Double)s.getValue()[2];
//            String periodo=(String)s.getValue()[4];

            this.drawStringChico( nombre , x_a, y);
//            this.drawStringPequenio( "("+periodo+")" , x_b, y);
            this.drawStringChico("("+cantidad+")", x_c , y);            
            this.drawStringChico( this.getStringDecimalFormat(monto,2), x_d, y);
            
            y-=alto;
                       
        }
        for(Map.Entry<Integer,Object[]> su : resumen_su.entrySet() ){
            String nombre=fnc.capitalize((String)su.getValue()[0]);
            Double monto=(Double)su.getValue()[1];
            this.drawStringChico( nombre , x_a, y);
            //this.drawStringPequenio( "("+data.getValue().getPeriodoAnidadoFormateado()+")" , x_b, y);
            this.drawStringChico( this.getStringDecimalFormat(monto,2), x_d, y);

            y-=alto;
        }
        
        for(AJUS ajus : this.factura.mi_ajustes){
            if (ajus.getST_AFEC_IGV_ForJSON()){
                cantidad_ajustes_afectos++;
            }else{
                cantidad_ajustes_no_afectos++;
            }
        }
        if (this.factura.ajustes_por_redondeo!=0d){
            cantidad_ajustes_no_afectos++;
        }
        if (cantidad_ajustes_afectos>0){            
            for(AJUS ajus : this.factura.mi_ajustes){                
                this.drawStringChico( ajus.getDE_GLOS_ForJSON() , x_a, y);
                this.drawStringChico( this.getStringDecimalFormat(Math.abs(ajus.getIM_MONT_ForJSON())*-1d,2), x_d, y);
                y-=alto;
            }
        }
        if (cantidad_ajustes_no_afectos>0){
            for(AJUS ajus : this.factura.mi_ajustes){
               if (!ajus.getST_AFEC_IGV_ForJSON()){                    
                   this.drawStringChico( ajus.getDE_GLOS_ForJSON() , x_a, y);
                   this.drawStringChico( this.getStringDecimalFormat(Math.abs(ajus.getIM_MONT_ForJSON())*-1d,2), x_d, y);
                   y-=alto;
               }
           }
           if (this.factura.ajustes_por_redondeo!=0d){                
               this.drawStringChico( Constante.GLOSA_AJUSTE_REDONDEO , x_a, y);
               this.drawStringChico( this.getStringDecimalFormat(this.factura.ajustes_por_redondeo,2), x_d, y);
               y-=alto;
           }

        }
        
        
//        for(Map.Entry<NEGO_SUCU,List<FACT_GLOS>> entry : factura.mi_data_factura.entrySet()){
//            NEGO_SUCU sucu_data=entry.getKey();
//            List<FACT_GLOS> glosas=entry.getValue();  
//            
//            Collections.sort(glosas, new Comparator<FACT_GLOS>() {
//                public int compare(FACT_GLOS o1, FACT_GLOS o2) {
//                    return new Integer(o1.getTI_GLOS_ForJSON()).compareTo(new Integer(o2.getTI_GLOS_ForJSON()));
//                }
//            });
//            
//            for(FACT_GLOS glos : glosas){                      
//                    this.drawStringChico( glos.getNO_GLOS_ForJSON() , x_a, y);
//                    Double m=redondearPorDefecto(glos.getIM_MONT_ForJSON());
//                    this.drawStringChico( this.getStringDecimalFormat(m,2), x_c, y);
//
//                    y-=alto;
//            }
//        }
    }
    
    private void addCuadroFooter(float inPosition) {
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
        
        this.drawRectangle(posHeaderB1,y , tamanioColumnaB, alturaCeldaTipoB,2);
        this.drawRectangle(posHeaderC1,y , tamanioColumnaC, alturaCeldaTipoB,2);
        this.drawRectangle(posHeaderD1,y , tamanioColumnaD, alturaCeldaTipoB,2);
        
        for (int i = 0; i < 8; i++) {
            y-=alturaCeldaTipoB;
        
            this.drawRectangle(posHeaderB1,y , tamanioColumnaB, alturaCeldaTipoB,2);
            this.drawRectangle(posHeaderC1,y , tamanioColumnaC, alturaCeldaTipoB,2);
            this.drawRectangle(posHeaderD1,y , tamanioColumnaD, alturaCeldaTipoB,2);
        }
        
        float posInfoX_A1 = posInicialInfo;        
        float posInfoX_B2 = posInfoX_A1 + tamanioColumnaA;
        float posInfoX_C3 = posInfoX_B2 + tamanioColumnaB;
        float posInfoX_D4 = posInfoX_C3 + tamanioColumnaC;
        
        
        Integer CO_MONE_FACT = this.factura.fact_modificado.getCO_MONE_FACT_ForJSON();
        Double totales[]=this.factura.getTotales();
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
        this.drawStringChico(this.getStringDecimalFormat(totales[0] + totales[1],2)  , posInfoX_D4, (alturaInfo - (alturaCeldaTipoB*0)));
        this.drawStringChico(this.getStringDecimalFormat(0d,2)  , posInfoX_D4, (alturaInfo - (alturaCeldaTipoB*1)));
        this.drawStringChico(this.getStringDecimalFormat(0d,2)          , posInfoX_D4, (alturaInfo - (alturaCeldaTipoB*2)));
        this.drawStringChico(this.getStringDecimalFormat(0d,2)          , posInfoX_D4, (alturaInfo - (alturaCeldaTipoB*3)));
        this.drawStringChico(this.getStringDecimalFormat((totales[1]+(totales[3]-totales[11])+(totales[8]-totales[11])),2)  , posInfoX_D4, (alturaInfo - (alturaCeldaTipoB*4)));
        this.drawStringChico(this.getStringDecimalFormat(totales[2],2)  , posInfoX_D4, (alturaInfo - (alturaCeldaTipoB*5)));
        this.drawStringChico(this.getStringDecimalFormat(0d,2)          , posInfoX_D4, (alturaInfo - (alturaCeldaTipoB*6)));
        this.drawStringChico(this.getStringDecimalFormat(0d,2)          , posInfoX_D4, (alturaInfo - (alturaCeldaTipoB*7)));
        this.drawStringChico(this.getStringDecimalFormat(totales[4],2), posInfoX_D4, (alturaInfo - (alturaCeldaTipoB*8)));
        this.drawStringChico(this.getStringDecimalFormat(totales[11],2), posInfoX_D4, (alturaInfo - (alturaCeldaTipoB*9)));
        
    } 
    
    public void addNumeroRecibo(){
        this.drawString("RECIBO N\u00b0 "+this.factura.NumeroFactura,this.rect.getWidth()-150,800,Font_Size_General-4);
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
    	System.out.print("aaaaaaaaaaaaaaaaaaaaa");
        this.cerrarCoss();
        super.save(out);
    }      
    private Double redondearPorDefecto(Double n){
        return com.americatel.facturacion.fncs.Numero.redondear(n,2);
    }
    
    
}
