/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.cores;

import com.americatel.facturacion.fncs.FechaHora;
import com.americatel.facturacion.fncs.Numero;
import com.americatel.facturacion.models.CIER;
import com.americatel.facturacion.models.CLIE;
import com.americatel.facturacion.models.DEPA;
import com.americatel.facturacion.models.DIST;
import com.americatel.facturacion.models.NEGO;
import com.americatel.facturacion.models.NEGO_SUCU;
import com.americatel.facturacion.models.PROV;
import com.americatel.facturacion.models.SUCU;
import java.util.List;
import java.util.Map;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;

/**
 *
 * @author crodas
 */
class FacturaPdf extends LibPdf{
    Factura factura=null;

    FacturaPdf(Factura factura) {
        this.factura=factura;
    }    
    private void addHeader(float y) {
        CIER cier=this.factura.getCier();
        CLIE clie=this.factura.getClie();
        SUCU sucu=clie.getSucursalFiscal();
        DIST dist=sucu.getDistrito();
        PROV prov=dist.getProvincia();
        DEPA depa=prov.getDepartamento();
        NEGO nego=this.factura.getNego();
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
        this.drawString("010 - "+Numero.formatearNumeroDigitos(this.factura.getNumero(), 6), 360, y-110,this.fontBold,20);
        
        ty-=alto;
        ty-=alto;
        this.drawStringChico("Lugar y Fecha de Emisi\u00f3n",40, ty);
        this.drawStringChico( FechaHora.getStringDateShortStringMonth(cier.getFE_EMIS_ForJSON()),120, ty);
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
        this.drawStringChico( sucu.getDE_DIRE_ForJSON()+", "+dist.getNO_DIST_ForJSON()+" - "+prov.getNO_PROV_ForJSON()+" - "+depa.getNO_DEPA_ForJSON()  ,120, ty);
        ty-=alto;   
        this.drawStringChico("R.U.C.: ",40, ty);
        this.drawStringChico( clie.getDE_NUME_DOCU_ForJSON()  ,120, ty);
        ty-=alto; 
        this.drawStringChico("Fecha de Vencimiento:",40, ty);
        this.drawStringChico( FechaHora.getStringDateShortStringMonth(cier.getFE_VENC_ForJSON()),120, ty);
        ty-=alto;        
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
        if (numero_paginas==1){            
            this.addPageUno();
        }else{
            //this.addDemasPages();
        }       
    }    
    
    private void addDetalle(float y) {
        float posXi=70;
        float posXf=500;
        float alto=10;
        float ty=y;
        int contador_sucursales=0;
        Map<NEGO_SUCU,List<FacturaGlosa>> ListaGlosas=this.factura.getGlosas();
        for (Map.Entry<NEGO_SUCU, List<FacturaGlosa>> entry : ListaGlosas.entrySet()){
            contador_sucursales++;
            SUCU sucu=entry.getKey().getSUCU();
            DIST dist=sucu.getDistrito();
            PROV prov=dist.getProvincia();
            DEPA depa=prov.getDepartamento();            
            List<FacturaGlosa> glosas=entry.getValue();
            this.drawStringChico(contador_sucursales+ ")Sucursal "+sucu.getDE_DIRE_ForJSON()+" ("+dist.getNO_DIST_ForJSON()+" - "+prov.getNO_PROV_ForJSON()+" - "+depa.getNO_DEPA_ForJSON()+")" , posXi-10, ty);
            ty-=alto;            
            for(FacturaGlosa fg : glosas){
                this.drawStringChico( fg.getNombre() , posXi, ty);
                this.drawStringPequenio( "("+fg.getPeriodo()+")" , posXi+300, ty);
                this.drawStringChico( fg.getMonto().toString(), posXf, ty);
                ty-=alto;
            }
        }
    }    
    
    private void addCuadroFooter(float y) {
        float alto=10;
        float ty=y;
        float posXi=440;
        float posXf=450;
        float borde=2f;
        float widthRect=90f;
                
        double totales[]=this.factura.getTotales();

        this.drawStringMedianoAlignDerecha( "VALOR VENTA" , posXi, ty);
        this.drawRectangle(posXf,ty -borde, widthRect, alto,borde);
        this.drawStringMedianoAlignDerecha( ""+totales[0] , posXi+widthRect, ty);
        ty-=alto;

        this.drawStringMedianoAlignDerecha( "DESCUENTO" , posXi, ty);
        this.drawRectangle(posXf,ty -borde, widthRect, alto,borde);
        this.drawStringMedianoAlignDerecha( ""+totales[1] , posXi+widthRect, ty);
        ty-=alto;  
        
        this.drawStringMedianoAlignDerecha( "VALOR DE VENTA NETO" , posXi, ty);
        this.drawRectangle(posXf,ty -borde, widthRect, alto,borde);
        this.drawStringMedianoAlignDerecha( ""+totales[2] , posXi+widthRect, ty);
        ty-=alto;         

        this.drawStringMedianoAlignDerecha( "I.G.V." , posXi, ty);
        this.drawRectangle(posXf,ty -borde, widthRect, alto,borde);
        this.drawStringMedianoAlignDerecha( ""+totales[3] , posXi+widthRect, ty);
        ty-=alto;
        
        this.drawStringMedianoAlignDerecha( "TOTAL" , posXi, ty);
        this.drawRectangle(posXf,ty -borde, widthRect, alto,borde);
        this.drawStringMedianoAlignDerecha( ""+totales[4] , posXi+widthRect, ty);
        ty-=alto;        
        
    }    
    
    void createPDF() {
        this.addPage();
        this.addDetalle(650);
        this.addCuadroFooter(150);
    }

    



}
