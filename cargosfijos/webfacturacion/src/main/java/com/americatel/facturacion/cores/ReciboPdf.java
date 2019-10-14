/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.cores;

import com.americatel.facturacion.fncs.FechaHora;
import com.americatel.facturacion.fncs.Path;
import com.americatel.facturacion.models.CLIE;
import com.americatel.facturacion.models.DEPA;
import com.americatel.facturacion.models.DIST;
import com.americatel.facturacion.models.MONE_FACT;
import com.americatel.facturacion.models.NEGO;
import com.americatel.facturacion.models.NEGO_SUCU;
import com.americatel.facturacion.models.PROV;
import com.americatel.facturacion.models.RECI;
import com.americatel.facturacion.models.SUCU;
import java.awt.Color;
import java.awt.geom.PathIterator;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.JPEGFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

/**
 *
 * @author crodas
 */
public class ReciboPdf extends LibPdf{ 
    protected List<PDPage> pages=new ArrayList<PDPage>();
    protected List<PDPageContentStream> coss=new ArrayList<PDPageContentStream>();
    Recibo recibo=null;
    RECI reci=null;
    public  ReciboPdf(Recibo recibo){
        this.recibo=recibo; 
        this.reci=recibo.getRECI();
    }

    public void addNumeroRecibo(float y){
        this.drawString("RECIBO N\u00b0 "+this.recibo.getNumero(),this.rect.getWidth()-150,y);
    }     
    
    private String getClienteNumeroDocumento(){
        if (this.reci==null)
            return this.recibo.getClie().getDE_NUME_DOCU_ForJSON();
        else
            return this.reci.getDE_NUME_DOCU_ForJSON();
    }
    
    private String getClienteCodigo(){
        CLIE clie=this.recibo.getClie();
        String ret="";
        if (clie.getDE_CODI_BUM_ForJSON()!=null)
            ret+=clie.getDE_CODI_BUM_ForJSON() + "-"; 
        if (clie.getDE_DIGI_BUM_ForJSON()!=null)
            ret+=clie.getDE_DIGI_BUM_ForJSON();            
        return ret;
    } 
    
    private String getClienteNombre(){
        if (this.reci==null){
            CLIE clie=this.recibo.getClie();
            String rz=clie.getNO_RAZO_ForJSON();
            if (rz!=null)
                return rz;
            return clie.getAP_CLIE_ForJSON()+","+clie.getNO_CLIE_ForJSON();
        }else{
            return this.reci.getNO_CLIE_ForJSON();
        }
    }    
    
    private void addCuadroUsuario(float y){
        String dire_sucu_corr=null;
        DIST dist=null;
        Integer CO_NEGO=null;
        float font_size=8;
        if (this.reci==null){
            SUCU sucu=this.recibo.getSucu_Corr();
            NEGO nego=this.recibo.getNego();
            dist=sucu.getDist_ForJSON(); 
            dire_sucu_corr=sucu.getDE_DIRE_ForJSON();
            CO_NEGO=nego.getCO_NEGO_ForJSON();
        }else{
            dire_sucu_corr=this.reci.getDE_DIRE_CORR_ForJSON();
            dist=this.reci.getDistritoCorrespondencia();
            CO_NEGO=this.reci.getCO_NEGO_ForJSON();
        }
        PROV prov=dist.getPROV_ForJSON();
        DEPA depa=prov.getDEPA_ForJSON();         

        this.drawRectangle(35, y+47, 530, 80, 2);        

        this.drawString("SE\u00d1OR (ES)",40, y+115,font_size);
        this.drawString("RUC / DOC.ID",this.rect.getWidth()-200, y+115,font_size);
        this.drawString(this.getClienteNumeroDocumento(),this.rect.getWidth()-100, y+115,font_size);
        this.drawString("CODIGO CLIENTE",this.rect.getWidth()-200, y+105,font_size);
        this.drawString(this.getClienteCodigo(),this.rect.getWidth()-100, y+105,font_size);
        this.drawStringLimitWidth(this.getClienteNombre(),40, y+105,300,font_size+1,font_size);
        this.drawString(dire_sucu_corr,40, y+85,font_size);

        //numero negocio
        this.drawString("N\u00b0 NEGOCIO",this.rect.getWidth()-200, y+85,font_size);
        this.drawString(CO_NEGO+"",this.rect.getWidth()-100, y+85,font_size);
        //Ubicacion
        this.drawString(dist.getNO_DIST_ForJSON()+" - "+prov.getNO_PROV_ForJSON()+" - "+depa.getNO_DEPA_ForJSON(),40, y+70,font_size);
        //numero documento in info cliente
        this.drawStringChico(this.recibo.getNumero()+"",40, y+60);            
    }
    private void addPageUno(){
        float y=570;
        this.addCuadroUsuario(y);
        this.addNumeroRecibo(y+230);        
        this.drawRectangle(35, y+25,this.rect.getWidth()-65  , 20, 2, Color.BLACK  , Color.LIGHT_GRAY);
        this.drawString("RESUMEN DE SERVICIOS", 40, y+32,fontBold,10);           
        try {                        
            BufferedImage awtImage = ImageIO.read(new File(Path.getPathWEB_INF()+"/../resources/sencha/facturacion/images/imagen_factura.jpg"));
            //Para windows
//            BufferedImage awtImage = ImageIO.read(new File(getClass().getResource("/../../resources/sencha/facturacion/images/imagen_factura.jpg").toURI()));
//            PDXObjectImage ximage = new PDPixelMap(document, awtImage);
            PDImageXObject ximage = JPEGFactory.createFromImage(document, awtImage);
            float scale = 0.75f;
            float w=ximage.getWidth()*scale;
            float h=ximage.getHeight()*scale;
            cos.drawXObject(ximage, this.rect.getWidth()-w-30, y-h+20, w , h);
        } catch (Exception ex) {
            Logger.getLogger(ReciboPdf.class.getName()).log(Level.SEVERE, null, ex);
            Logger.getLogger("No se cargo imagen NGN americatel");
        }        
        this.drawStringLimitWidth (this.recibo.getPeriodoString(), 40, y+10,350,9,this.fontBold,8);
    }
    
    private void addDemasPages(){
        float y=570;
        this.addNumeroRecibo(y+230);         
    }
    
    private void addPage(){
        numero_paginas++;
        this.page = new PDPage(PDRectangle.A4);    
        this.document.addPage(this.page);
        this.rect= this.page.getMediaBox();
        this.createContentStream();
        pages.add(this.page);
        coss.add(this.cos);
        if (numero_paginas==1){            
            this.addPageUno();
        }else{
            this.addDemasPages();
        }       
    }
    
    private void addCuadroFooter(float y){
        Date FE_EMIS=null,FE_VENC=null;
        String moneda=this.recibo.getMone_Fact().getNO_MONE_FACT_ForJSON().toUpperCase();
        double totales[]=this.recibo.getTotales();//NETO,AJUSTE SI AFECTO,IGV,AJUSTE NO AFECTO,TOTAL_RECIBO
        float x=30F;
        float h=20F;
        float wt=0;
        float ys=0;
        float yts=0;
        float br=1.3F;
        float rz_footer_glosa=10;
        float rz_footer_monto=20;
        float font_size=7;
        
        if (this.reci==null){
            FE_EMIS=this.recibo.getCier().getFE_EMIS_ForJSON();
            FE_VENC=this.recibo.getCier().getFE_VENC_ForJSON();
        }else{
            FE_EMIS=this.reci.getFE_EMIS_ForJSON();
            FE_VENC=this.reci.getFE_VENC_ForJSON();
        }
        ys=y;
        float marginLeftRectangle=25F;
        float widthRectangle=450F;
        float heightRectangle=65F;
        float marginInRectangle=5F;
        font_size=6;
        this.drawRectangle(x+marginLeftRectangle, ys-heightRectangle,widthRectangle, heightRectangle, br, Color.BLACK, Color.WHITE);
        this.drawString("", x+marginLeftRectangle+marginInRectangle,ys,font_size);ys-=rz_footer_glosa;
        this.drawString("ESTIMADO CLIENTE:", x+marginLeftRectangle+marginInRectangle,ys,font_size);ys-=rz_footer_glosa;        
        this.drawStringSize("S\u00edrvase cancelar su recibo en cualquier agencia del Banco de Cr\u00e9dito, Scotiabank \u00f3 Interbank. Indicar en ventanilla: C\u00f3digo n\u00famero de recibo "+this.recibo.getNumero(), x+marginLeftRectangle+marginInRectangle,ys,font_size);ys-=rz_footer_glosa;
        this.drawStringSize(" - BCP: Pago de Servicio Telefon\u00eda Americatel - Otros Servicios D\u00f3lares.", x+marginLeftRectangle+marginInRectangle+10F,ys,font_size);ys-=rz_footer_glosa;
        this.drawStringSize(" - Scotiabank \u00f3 Interbank: Pago de Servicio Americatel - Facturaci\u00f3n D\u00f3lares.", x+marginLeftRectangle+marginInRectangle+10F,ys,font_size);ys-=rz_footer_glosa;
        this.drawStringSize("Si realiza el pago v\u00eda Telecr\u00e9dito \u00f3 Telebanking, por favor notificarlo enviando el detalle (n\u00b0 documentos cancelados) al correo recaudacion@americatel.com.pe.", x+marginLeftRectangle+marginInRectangle,ys,font_size);ys-=rz_footer_glosa;
        this.drawStringSize("Ahora puedes realizar tus pagos en efectivo en nuestra oficina comercial", x+marginLeftRectangle+marginInRectangle,ys,font_size);ys-=rz_footer_glosa;
        
        ys=ys-rz_footer_glosa*0.7F;
        this.drawStringSize("No afecto al regimen de retenci\u00f3n del IGV, seg\u00fan resoluci\u00f3n de superitendencia N\u00b0 037-2002/SUNAT", x,ys,font_size);ys-=rz_footer_glosa;
        ys=ys-rz_footer_glosa*1.5F;
        yts=ys;
        wt=80;
        font_size=7;
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
        this.drawStringCenter(this.getStringDecimalFormat(totales[3],2), x, yts,wt, h,fontPlain,font_size);        
        //TOTAL
        x+=wt-1;
        wt=82;
        yts=ys;
        this.drawRectangle(x, yts, wt, h, br, Color.BLACK, Color.LIGHT_GRAY);        
        this.drawStringCenter("TOTAL RECIBO ("+moneda+")", x, yts,wt, h,fontPlain,font_size);yts-=rz_footer_monto;
        this.drawRectangle(x, yts,wt, h, br, Color.BLACK, Color.WHITE);
        this.drawStringCenter(this.getStringDecimalFormat(totales[4],2), x, yts,wt, h,fontPlain,font_size);
        
    }
    
    private Float evaluarLineasAntes(Float y){
        return this.evaluarLineasAntes(y,200F);
    }
    
    private Float evaluarLineasAntes(Float y,Float min_y){
        //funcion que evalua las lineas dibujadas para agregar otra hoja de pdf, antes de dibujar
        if (y<min_y){
            this.addPage();        
            if (this.numero_paginas>1){
                y=780f;
            }
        }
        return y;
    }
    
    private void addDetalleShort(){
        Map<NEGO_SUCU,List<ReciboGlosa>> ListaGlosas=this.recibo.getGlosas();
        SUCU sucu=null;
        NEGO_SUCU nego_sucu=null;
        List<ReciboGlosa> glosas=null; 
        int van_sucursales=0;
        Float y=580f;
        Float razonY=12f;
        //float van_lineas=0;
        //float max_lineas=36;
        int espacios=1,numero_glosa=0;
        boolean dibujar_glosa=true;
        float font_size=9;
        float position_x_monto=370;
        float position_x_repite=320;
        int cantidad_sucursales_involucradas=this.recibo.getCantidadSucursalesInvolucradas();
        if (cantidad_sucursales_involucradas==1){
            y-=razonY;
            nego_sucu=ListaGlosas.entrySet().iterator().next().getKey();
            sucu=nego_sucu.getSUCU();
            
            y=this.drawStringLimitWidth("Sucursal: "+sucu.getDireccion(), 40, y,350,font_size-2,font_size-2);
        }
        y-=razonY;
        this.drawString("1. SERVICIOS", 40, y,this.fontBold,font_size-2);         
        for(Object detalleServicios[] : this.recibo.getDetalleShortServicios()){
            numero_glosa++;
            y-=razonY;
            y=this.evaluarLineasAntes(y); 
            this.drawStringMedianoAlignDerecha(this.getStringDecimalFormat((Double)detalleServicios[2],2), position_x_monto, y,font_size-2); //monto primero
            y=this.drawStringLimitWidth(Character.toString((char)(numero_glosa+96)) +". "+(String)detalleServicios[0], 50, y,250,font_size-2);            
            if (cantidad_sucursales_involucradas>1)
                y=this.drawStringLimitWidth("("+(int)Math.floor((Double)detalleServicios[1])+")", position_x_repite, y,250,font_size-2);
        }
        /*
        for (Map.Entry<NEGO_SUCU, List<ReciboGlosa>> entry : ListaGlosas.entrySet()){
            nego_sucu=entry.getKey();
            glosas=entry.getValue();
            sucu=nego_sucu.getSUCU();
            y=this.evaluarLineasAntes(y);
            numero_glosa=0;
            for(ReciboGlosa glosa: glosas){
                dibujar_glosa=true;
                //Verificamos si la glosa se debuja en un recibo
                //Si es Servicio Suplementario (SS) o Servicio Unico (SU)
                //Si esta afecto a detraccion y es servicio suplementario
                if (this.recibo.isAfectoDetraccion() && glosa.isAfectoDetraccion())
                    dibujar_glosa=false;
                if (dibujar_glosa){
                    numero_glosa++;
                    //van_lineas++;
                    y=this.evaluarLineasAntes(y);                    
                    this.drawStringMedianoAlignDerecha(this.getStringDecimalFormat(glosa.getMonto(),2), 350, y,font_size-2); //monto primero
                    y=this.drawStringLimitWidth(Character.toString((char)(numero_glosa+96)) +". "+glosa.getNombre(), 50, y,250,font_size-2);
                }
            }
            for(int i=1;i<=espacios;i++){
                y-=razonY;               
            }  
        }*/
        //ajustes y promociones
        int contador_titulos=1;
        y-=razonY;
        if (this.tieneGlosasAfectosIgv() || this.tienePromocion() || this.tieneGlosasDevoluciones()){
            contador_titulos++;
            y=this.evaluarLineasAntes(y);
            numero_glosa=0;
            this.drawString(contador_titulos+".Ajustes afectos a IGV", 40, y,this.fontBold,font_size-2);            
            for(Object detalleServicios[] : this.recibo.getDetalleShortAfectosIgv()){
                numero_glosa++;
                y-=razonY;
                y=this.evaluarLineasAntes(y); 
                this.drawStringMedianoAlignDerecha(this.getStringDecimalFormat((Double)detalleServicios[2],2), position_x_monto, y,font_size-2); //monto primero
                y=this.drawStringLimitWidth(Character.toString((char)(numero_glosa+96)) +". "+(String)detalleServicios[0], 50, y,250,font_size-2);            
                if (cantidad_sucursales_involucradas>1)
                    y=this.drawStringLimitWidth("("+(int)Math.floor((Double)detalleServicios[1])+")", position_x_repite, y,250,font_size-2);
            }            
            /*for(ReciboGlosa glosa : this.recibo.getAjustes()){           
                if (glosa.getETipoGlosa()==ETipoReciboGlosa.AJUSTES_AFECTOS_IGV){
                    y=this.evaluarLineasAntes(y);
                    this.drawString("."+glosa.getNombre(), 60, y,font_size-2);
                    this.drawStringMedianoAlignDerecha(this.getStringDecimalFormat(glosa.getMonto(),2), position_x_monto, y,font_size-2); 
                    y-=razonY;
                }
            }            
            //promociones
            Double tmp_total_promocion=0d;
            for(ReciboGlosa glosa : this.recibo.getPromociones()){
                tmp_total_promocion+=glosa.getMonto();
            }
            if (tmp_total_promocion>0){   
                y=this.evaluarLineasAntes(y);
                this.drawString(". Descuento por promocion", 60, y,font_size-2);
                this.drawStringMedianoAlignDerecha(this.getStringDecimalFormat(tmp_total_promocion,2), position_x_monto, y,font_size-2); 
                y-=razonY;
            } */           
        } 
        
        if (this.tieneGlosasNoAfectosIgv()){
            contador_titulos++;
            y=this.evaluarLineasAntes(y);
            numero_glosa=0;
            this.drawString(contador_titulos+".Ajustes no afectos a IGV", 40, y,this.fontBold,font_size-2);            
            for(Object detalleServicios[] : this.recibo.getDetalleShortNoAfectosIgv()){
                numero_glosa++;
                y-=razonY;
                y=this.evaluarLineasAntes(y); 
                this.drawStringMedianoAlignDerecha(this.getStringDecimalFormat((Double)detalleServicios[2],2), position_x_monto, y,font_size-2); //monto primero
                y=this.drawStringLimitWidth(Character.toString((char)(numero_glosa+96)) +". "+(String)detalleServicios[0], 50, y,250,font_size-2);            
                if (cantidad_sucursales_involucradas>1)
                    y=this.drawStringLimitWidth("("+(int)Math.floor((Double)detalleServicios[1])+")", position_x_repite, y,250,font_size-2);
            }                
        }
        
        if (this.recibo.getCantidadSucursalesInvolucradas()==1){
            y-=razonY;
            nego_sucu=this.recibo.getGlosas().entrySet().iterator().next().getKey();
            this.drawStringMediano("Orden de compra : "+nego_sucu.getDE_ORDE_SERV_ForJSON(), 40,y);
        }        
    }
    
    private void addDetalleFull(){
        if (this.recibo.getCantidadSucursalesInvolucradas()>=1){
            float min_y_new_page=100;
            float y=780F;
            float x_a=40F;
            float x_b=105F;
            float x_c=400F;
            float width_direccion=420F;
            float font_size=7F; 
            float salto_linea=9F;
            
            this.addPage();                  
            this.drawString("SE\u00d1OR (ES): ",x_a , y,fontBold,font_size);
            this.drawString(this.getClienteNombre(),x_b , y,font_size);
            y-=salto_linea;                  
            this.drawString("N\u00b0 NEGOCIO: ",x_a , y,fontBold,font_size);
            this.drawString(this.recibo.getNego().getCO_NEGO_ForJSON().toString() ,x_b , y,font_size);
            y-=salto_linea*2F;
            this.drawRectangle(x_a-2, y-2, 500,salto_linea   , 1F, Color.BLACK,Color.LIGHT_GRAY);
            this.drawString("DETALLE DEL NEGOCIO",x_a , y,fontBold,font_size);
            y-=salto_linea;
            

            
            Map<NEGO_SUCU,List<ReciboGlosa>> ListaGlosas=this.recibo.getGlosas();
            SUCU sucu=null;
            NEGO_SUCU nego_sucu=null;
            List<ReciboGlosa> glosas=null;   
            boolean dibujar_glosa=false;
            int numero_glosa=0;
            int sucursal_van=0;
            for (Map.Entry<NEGO_SUCU, List<ReciboGlosa>> entry : ListaGlosas.entrySet()){
                y-=salto_linea;
                sucursal_van++;                
                nego_sucu=entry.getKey();
                glosas=entry.getValue();
                sucu=nego_sucu.getSUCU();
                y=this.evaluarLineasAntes(y,min_y_new_page);
                numero_glosa=0;
                
                this.drawString("Sucursal "+sucursal_van+": ", x_a , y,fontBold,font_size);
                y=this.drawStringLimitWidth(sucu.getDireccion(), x_b , y,width_direccion,salto_linea,font_size);
                y-=salto_linea;
                
                this.drawString("Orden de Compra: ", x_a , y,fontBold,font_size);
                this.drawString(nego_sucu.getDE_ORDE_SERV_ForJSON(), x_b , y,font_size);
                y-=salto_linea;
                
                for(ReciboGlosa glosa: glosas){
                    dibujar_glosa=true;
                    //Verificamos si la glosa se debuja en un recibo
                    //Si es Servicio Suplementario (SS) o Servicio Unico (SU)
                    //Si esta afecto a detraccion y es servicio suplementario
                    if (this.recibo.isAfectoDetraccion() && glosa.isAfectoDetraccion())
                        dibujar_glosa=false;
                    if (dibujar_glosa){
                        numero_glosa++;
                        //van_lineas++;
                        y=this.evaluarLineasAntes(y,min_y_new_page);                    
                        this.drawStringMedianoAlignDerecha(this.getStringDecimalFormat(glosa.getMonto(),2), x_c, y,font_size); //monto primero
                        y=this.drawStringLimitWidth(Character.toString((char)(numero_glosa+96)) +". "+glosa.getNombre(), x_a, y,250,font_size);
                        y-=salto_linea;
                    }
                }
                y-=salto_linea;  
            }  
            
            for(ReciboGlosa glosa : this.recibo.getAjustes()){           
                if (glosa.getETipoGlosa()==ETipoReciboGlosa.AJUSTES_AFECTOS_IGV){
                    y=this.evaluarLineasAntes(y,min_y_new_page);
                    this.drawString("."+glosa.getNombre(), x_a, y,font_size);
                    this.drawStringMedianoAlignDerecha(this.getStringDecimalFormat(glosa.getMonto(),2), x_c, y,font_size); 
                    y-=salto_linea;
                }
            }            
            //promociones
            Double tmp_total_promocion=0d;
            for(ReciboGlosa glosa : this.recibo.getPromociones()){
                tmp_total_promocion+=glosa.getMonto();
            }
            if (tmp_total_promocion>0){   
                y=this.evaluarLineasAntes(y,min_y_new_page);
                this.drawString(". Descuento por promoci\u00f3n", x_a, y,font_size);
                this.drawStringMedianoAlignDerecha(this.getStringDecimalFormat(tmp_total_promocion,2), x_c, y,font_size); 
                y-=salto_linea;
            }            
        }
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
    
    private void addPaginador(){
        try {
            if (this.recibo.getCantidadSucursalesInvolucradas()>1){
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }    
    
    public boolean tieneGlosasNoAfectosIgv(){
        for(ReciboGlosa glosa : this.recibo.getAjustes()){           
            if (glosa.getETipoGlosa()==ETipoReciboGlosa.AJUSTES_NO_AFECTOS_IGV){
                return true;
            }
        }        
        return false;
    }
    
    public boolean tieneGlosasAfectosIgv(){
        for(ReciboGlosa glosa : this.recibo.getAjustes()){           
            if (glosa.getETipoGlosa()==ETipoReciboGlosa.AJUSTES_AFECTOS_IGV){
                return true;
            }
        }        
        return false;
    }    
    public boolean tieneGlosasDevoluciones(){
        Map<NEGO_SUCU,List<ReciboGlosa>> ListaGlosas=this.recibo.getGlosas();
        for (Map.Entry<NEGO_SUCU, List<ReciboGlosa>> entry : ListaGlosas.entrySet()){
            for(ReciboGlosa glosa : entry.getValue()){           
                if (    glosa.getETipoGlosa()==ETipoReciboGlosa.DEVOLUCION_CARGOS_FIJOS_SALDO || 
                        glosa.getETipoGlosa()==ETipoReciboGlosa.DEVOLUCION_POR_CORTE_O_SUSPENSION_SERVICIO ||
                        glosa.getETipoGlosa()==ETipoReciboGlosa.DEVOLUCION_POR_DESACTIVACION
                    ){
                    return true;
                }
            }            
        }        
        
        return false;
    }    
    public boolean tienePromocion(){
        return this.recibo.getPromociones().size()>0;
    }
    
    
    public void createPDF(){
        this.addPage();
        this.addDetalleShort();
        this.addCuadroFooter(170);
        this.addDetalleFull();
        this.addPaginador();
        this.cerrarCoss();
    }

}
