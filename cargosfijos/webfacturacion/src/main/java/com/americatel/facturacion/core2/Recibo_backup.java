/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.core2;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.JPEGFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import com.americatel.facturacion.cores.EReciboTextAlign;
import com.americatel.facturacion.fncs.FechaHora;
import com.americatel.facturacion.fncs.Path;
import com.americatel.facturacion.fncs.fnc;
import com.americatel.facturacion.models.AJUS;
import com.americatel.facturacion.models.CIER_DATA_PLAN;
import com.americatel.facturacion.models.CIER_DATA_PROM_MONT;
import com.americatel.facturacion.models.CIER_DATA_SERV_SUPL;
import com.americatel.facturacion.models.CIER_DATA_SERV_UNIC;
import com.americatel.facturacion.models.CIER_DATA_SUCU;
import com.americatel.facturacion.models.CLIE;
import com.americatel.facturacion.models.DEPA;
import com.americatel.facturacion.models.DIST;
import com.americatel.facturacion.models.NEGO;
import com.americatel.facturacion.models.NEGO_SUCU;
import com.americatel.facturacion.models.PROV;
import com.americatel.facturacion.models.RECI;
import com.americatel.facturacion.models.SUCU;

/**
 *
 * @author crodas
 */
public class Recibo_backup  extends LibPdf {
    private NegocioFacturar nf=null;
    private Date[] Periodo;
    private Double saldo_pendiente=0d;
    private Long Numero;
    protected List<PDPageContentStream> coss=new ArrayList<PDPageContentStream>();
    private Double[] totales=new Double[18];
    private Boolean periodo_principal=false;   
    private Boolean generaFacturaDetraccion=false;
    private Integer cantidad_sucursales=0;
    private Integer pos_sucursal=0;
    private float Font_Size_General=11;// min 7 por que existe un Font_Size_General-6
    private Double pendiente_upgrade=0d; // Recibo relacionado para los casos de upgrade y downgrade, para que se genere un solo recibo
    public Recibo_backup(NegocioFacturar nf,Date[] periodo,Long numero,Double saldo_pendiente,Boolean periodo_principal,Double pendiente_upgrade){
        this.nf=nf;
        this.Periodo=periodo;
        this.Numero=numero;
        this.saldo_pendiente=saldo_pendiente;
        this.periodo_principal=periodo_principal;
        this.pendiente_upgrade=pendiente_upgrade;
        this.calcularTotales();        
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
    private void cerrarCoss(){
        try {
            for(PDPageContentStream cos : this.coss){
                cos.close();
            }             
        } catch (Exception e) {
            e.printStackTrace();
        }
    }    
    
    public void crearPdf(){
        this.addPage();
        this.crearPdfDetallado();
        this.enumerarPaginasRecibo();
        this.cerrarCoss();
    }  
    private void addPageUno(){
        float y=570;
        this.addCuadroUsuario(y);                
        this.drawRectangle(35, y+25,this.rect.getWidth()-65  , 20, 2, Color.BLACK  , Color.LIGHT_GRAY);
        this.drawString("RESUMEN DE SERVICIOS", 40, y+32,fontBold,Font_Size_General-2); 
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
            ex.printStackTrace();
        }        
        this.drawStringLimitWidth (this.getPeriodoString(), 40, y,350,9,this.fontBold,Font_Size_General-4);
        if (this.cantidad_sucursales==1){
            CIER_DATA_SUCU sucu=this.nf.negocio_detalle.getDetalle_sucursales().get(this.pos_sucursal).getSucu();
            y=this.drawStringLimitWidth ("Sucursal : "+sucu.getDE_DIRE_SUCU()+ " ("+sucu.getNO_DIST()+" - "+sucu.getNO_PROV()+" - "+sucu.getNO_DEPA()+")", 40, y-15,350,9,this.fontPlain,Font_Size_General-4);
            this.crearPdfResumen(y-10);
            this.addCuadroFooter(200);
        }else{
            this.crearPdfResumen(y-20);
            this.addCuadroFooter(200);            
        }
    }
    public void addNumeroRecibo(){
        this.drawString("RECIBO N\u00b0 "+this.Numero,this.rect.getWidth()-150,800,Font_Size_General-4);
    }     
    public void enumerarPaginasRecibo(){
        if (this.cantidad_sucursales>1){
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
    private void addCuadroUsuario(float y){
        float font_size=Font_Size_General-4;
        NEGO nego=nf.getNego();
        CLIE clie=nf.getClie();
        SUCU sucu=nf.getSucu_fiscal();
        DIST dist=nf.getDist_fiscal();
        PROV prov=nf.getProv_fiscal();
        DEPA depa=nf.getDepa_fiscal();
        this.drawRectangle(35, y+47, 530, 80, 2); 
        this.drawString("SE\u00d1OR (ES)",40, y+115,font_size);
        this.drawString("RUC / DOC.ID",this.rect.getWidth()-200, y+115,font_size);
        this.drawString(clie.getDE_NUME_DOCU_ForJSON(),this.rect.getWidth()-100, y+115,font_size);
        this.drawString("CODIGO CLIENTE",this.rect.getWidth()-200, y+105,font_size);
        this.drawString(clie.getCodigoCliente(),this.rect.getWidth()-100, y+105,font_size);
        this.drawStringLimitWidth(clie.getFullNameCliente(),40, y+105,300,font_size+1,font_size);
        this.drawString(sucu.getDE_DIRE_ForJSON(),40, y+85,font_size);

        //numero negocio
        this.drawString("N\u00b0 NEGOCIO",this.rect.getWidth()-200, y+85,font_size);
        this.drawString(nego.getCO_NEGO_ForJSON()+"",this.rect.getWidth()-100, y+85,font_size);
        //Ubicacion
        this.drawString(dist.getNO_DIST_ForJSON()+" - "+prov.getNO_PROV_ForJSON()+" - "+depa.getNO_DEPA_ForJSON(),40, y+70,font_size);
        //numero documento in info cliente
        this.drawString(this.Numero+"",40, y+60,Font_Size_General-5);     
    }    
    
    public String getNombre() {
        return this.Numero+"-"+this.nf.getNego().getCO_NEGO_ForJSON()+"-"+FechaHora.getYear(this.Periodo[0])+"-"+(FechaHora.getMonth(this.Periodo[0])+1);
    }    
    
    private String getPeriodoString() {
        return "Periodo del "+FechaHora.getStringDateShortStringMonth(this.Periodo[0])+" al "+FechaHora.getStringDateShortStringMonth(this.Periodo[1]);
    }    
    
    public void crearPdfResumen(float y){
        Map<String,Double[]> servicios_planes=new TreeMap<String, Double[]>();
        Map<String,Double[]> servicios_serv_supls=new TreeMap<String, Double[]>();
        Map<String,Double[]> servicios_unicos=new TreeMap<String, Double[]>();
        Map<String,Double[]> afectos_igv=new TreeMap<String, Double[]>();
        Map<String,Double[]> no_afectos_igv=new TreeMap<String, Double[]>();
        Map<String,Double[]> diferencial_cargos_fijos=new TreeMap<String, Double[]>();
        float font_size=Font_Size_General-4;
        int incide_va=0;
        int contador_items=0;
        float col_a=40;
        float col_b=300;
        float col_c=350;
        float max_w_a=250;
        float salto_linea=font_size+1;
        //devolucion de cargos fijos
        Double aplico_devolucion_de=(this.totales[6]-this.totales[8])-this.totales[7];
        if (aplico_devolucion_de<0){
            Double queda=aplico_devolucion_de;
            Double aplicar;
            if (this.totales[8]<0 && queda<0){
                if ((queda-this.totales[8])<0){
                    queda-=this.totales[8];
                    aplicar=this.totales[8];
                }else{                    
                    aplicar=queda;
                    queda=0d;
                }
                String nombre=fnc.capitalize(NegocioDetalleCierre.GLOSA_DEVOLUCION_CARGOS_FIJOS);        
                if (!afectos_igv.containsKey(nombre)){
                    afectos_igv.put(nombre,  new Double[]{0d,0d});
                }
                Double m[]=(Double [])afectos_igv.get(nombre);
                m[0]++;
                m[1]+=aplicar;
            }
            if (this.totales[9]<0 && queda<0){
                if ((queda-this.totales[9])<0){
                    queda-=this.totales[9];
                    aplicar=this.totales[9];
                }else{
                    aplicar=queda;
                    queda=0d;                    
                }
                String nombre=fnc.capitalize(NegocioDetalleCierre.GLOSA_DEVOLUCION_CARGOS_FIJOS);        
                if (!afectos_igv.containsKey(nombre)){
                    afectos_igv.put(nombre,  new Double[]{0d,0d});
                }
                Double m[]=(Double [])afectos_igv.get(nombre);
                m[0]++;
                m[1]+=aplicar;
            }
        }
        
        if (this.totales[8]!=0){//promociones
            String nombre=fnc.capitalize(NegocioDetalleCierre.GLOSA_PROMOCION); 
            if (!afectos_igv.containsKey(nombre)){
                afectos_igv.put(nombre,  new Double[]{0d,0d});
            }
            Double m[]=(Double [])afectos_igv.get(nombre);
            m[0]++;
            m[1]+=this.totales[8];            
        }
        if (this.totales[17]!=0){//diferencial de cargo fijo
            String nombre=fnc.capitalize(NegocioDetalleCierre.GLOSA_DIFERENCIAL_CARGOS_FIJOS); 
            if (!diferencial_cargos_fijos.containsKey(nombre)){
                diferencial_cargos_fijos.put(nombre,  new Double[]{0d,0d});
            }
            Double m[]=(Double [])diferencial_cargos_fijos.get(nombre);
            m[0]++;
            m[1]+=this.totales[17];            
        }
        for(SucursalDetalle sd : this.nf.negocio_detalle.getDetalle_sucursales()){
            //planes
            for(CIER_DATA_PLAN plan: sd.getDetalle_planes()){
                //normal
                if (FechaHora.isBetween(Periodo[0], Periodo[1], plan.getFE_INIC()) && FechaHora.isBetween(Periodo[0], Periodo[1], plan.getFE_FIN())){
                    if (plan.getST_TIPO_DEVO()==null){
                        if (!servicios_planes.containsKey(plan.getDE_NOMB())){
                            servicios_planes.put(plan.getDE_NOMB(), new Double[]{0d,0d});
                        }
                        Double m[]=(Double [])servicios_planes.get(plan.getDE_NOMB());
                        m[0]++;
                        m[1]+=plan.getIM_MONT();
                    }
                }
            }
            //servicios suplementarios
            for(CIER_DATA_SERV_SUPL ss: sd.getDetalle_servicios_suplementarios()){
                //normal
                if (FechaHora.isBetween(Periodo[0], Periodo[1], ss.getFE_INIC()) && FechaHora.isBetween(Periodo[0], Periodo[1], ss.getFE_FIN())){
                    if (ss.getST_TIPO_DEVO()==null && !(this.generaFacturaDetraccion && ss.getST_AFEC_DETR())){
                        if (!servicios_serv_supls.containsKey(ss.getDE_NOMB())){
                            servicios_serv_supls.put(ss.getDE_NOMB(), new Double[]{0d,0d});
                        }
                        Double m[]=(Double [])servicios_serv_supls.get(ss.getDE_NOMB());
                        m[0]++;
                        m[1]+=ss.getIM_MONT();
                    }
                }
            }
            //servicios unicos
            if (this.periodo_principal){
                for(CIER_DATA_SERV_UNIC unic : sd.getDetalle_servicios_unicos()){
                    if (!(this.generaFacturaDetraccion && unic.getST_AFEC_DETR())){
                        if (!servicios_unicos.containsKey(unic.getDE_NOMB())){
                            servicios_unicos.put(unic.getDE_NOMB(), new Double[]{0d,0d});
                        }
                        Double m[]=(Double [])servicios_unicos.get(unic.getDE_NOMB());
                        m[0]++;
                        m[1]+=unic.getIM_MONT();
                    }
                }
            }
        }   
        if (this.periodo_principal){
            for(AJUS ajus : this.nf.ajustes_pendientes_aplican){            
                if (ajus.getST_AFEC_IGV_ForJSON()){
                    String nombre=ajus.getDE_GLOS_ForJSON();
                    if (!afectos_igv.containsKey(nombre)){
                        afectos_igv.put(nombre,  new Double[]{0d,0d});
                    }
                    Double m[]=(Double [])afectos_igv.get(nombre);
                    m[0]++;
                    m[1]+=Math.abs(ajus.getIM_MONT_ForJSON())*-1;               
                }else{
                    String nombre=ajus.getDE_GLOS_ForJSON();
                    if (!no_afectos_igv.containsKey(nombre)){
                        no_afectos_igv.put(nombre,  new Double[]{0d,0d});
                    }
                    Double m[]=(Double [])no_afectos_igv.get(nombre);
                    m[0]++;
                    m[1]+=Math.abs(ajus.getIM_MONT_ForJSON())*-1;                
                }
            }
        }        
        if (this.totales[10]<0){
            String nombre=fnc.capitalize(NegocioDetalleCierre.GLOSA_AJUSTE_POR_REDONDEO);
            if (!no_afectos_igv.containsKey(nombre)){
                no_afectos_igv.put(nombre,  new Double[]{0d,0d});
            }
            Double m[]=(Double [])no_afectos_igv.get(nombre);
            m[0]++;
            m[1]+=this.totales[10];              
        }
        
        //DIBUJAR
        contador_items=1;
        if (servicios_planes.size()>0 || servicios_serv_supls.size()>0 || servicios_unicos.size()>0){
            this.drawString(contador_items+". SERVICIOS", col_a, y,fontBold,font_size);
            contador_items++;
            y-=salto_linea*2;
            incide_va=0;
            for(Entry<String,Double[]> entry : servicios_planes.entrySet()){                
                this.drawString("("+(int)Math.floor(entry.getValue()[0])+")", col_b , y,font_size);
                this.drawString(this.getStringDecimalFormat(entry.getValue()[1],2), col_c , y,fontPlain,font_size,EReciboTextAlign.DERECHA);
                y=this.drawStringLimitWidth(Character.toString((char)(incide_va+97))+". "+entry.getKey(), col_a+5 , y,max_w_a,salto_linea ,fontPlain, font_size);
                incide_va++;                
            }
            for(Entry<String,Double[]> entry : diferencial_cargos_fijos.entrySet()){                
                this.drawString("("+(int)Math.floor(entry.getValue()[0])+")", col_b , y,font_size);
                this.drawString(this.getStringDecimalFormat(entry.getValue()[1],2), col_c , y,fontPlain,font_size,EReciboTextAlign.DERECHA);
                y=this.drawStringLimitWidth(Character.toString((char)(incide_va+97))+". "+entry.getKey(), col_a+5 , y,max_w_a,salto_linea ,fontPlain, font_size);
                incide_va++;                
            }
            for(Entry<String,Double[]> entry : servicios_serv_supls.entrySet()){                
                this.drawString("("+(int)Math.floor(entry.getValue()[0])+")", col_b , y,font_size);
                this.drawString(this.getStringDecimalFormat(entry.getValue()[1],2), col_c , y,fontPlain,font_size,EReciboTextAlign.DERECHA);
                y=this.drawStringLimitWidth(Character.toString((char)(incide_va+97))+". "+entry.getKey(), col_a+5 , y,max_w_a,salto_linea,fontPlain,font_size);
                incide_va++;
                
            }
            for(Entry<String,Double[]> entry : servicios_unicos.entrySet()){                
                this.drawString("("+(int)Math.floor(entry.getValue()[0])+")", col_b , y,font_size);
                this.drawString(this.getStringDecimalFormat(entry.getValue()[1],2), col_c , y,fontPlain,font_size,EReciboTextAlign.DERECHA);
                y=this.drawStringLimitWidth(Character.toString((char)(incide_va+97))+". "+entry.getKey(), col_a+5 , y,max_w_a,salto_linea,fontPlain,font_size);
                incide_va++;                
            }
        }
        if (afectos_igv.size()>0){
            y-=salto_linea*2;
            this.drawString(contador_items+". AJUSTES AFECTOS A IGV", col_a, y,fontBold,font_size);
            contador_items++;
            y-=salto_linea*2;
            incide_va=0;
            for(Entry<String,Double[]> entry : afectos_igv.entrySet()){                
                this.drawString("("+(int)Math.floor(entry.getValue()[0])+")", col_b , y,font_size);
                this.drawString(this.getStringDecimalFormat(entry.getValue()[1],2), col_c , y,fontPlain,font_size,EReciboTextAlign.DERECHA);
                y=this.drawStringLimitWidth(Character.toString((char)(incide_va+97))+". "+entry.getKey(), col_a+5 , y,max_w_a,salto_linea,fontPlain,font_size);
                incide_va++;
            }            
        }
        if (no_afectos_igv.size()>0){
            y-=salto_linea*2;
            this.drawString(contador_items+". AJUSTES NO AFECTOS A IGV", col_a, y,fontBold,font_size);
            contador_items++;
            y-=salto_linea*2;
            incide_va=0;
            for(Entry<String,Double[]> entry : no_afectos_igv.entrySet()){                
                this.drawString("("+(int)Math.floor(entry.getValue()[0])+")", col_b , y,font_size);
                this.drawString(this.getStringDecimalFormat(entry.getValue()[1],2), col_c , y,fontPlain,font_size,EReciboTextAlign.DERECHA);
                y=this.drawStringLimitWidth(Character.toString((char)(incide_va+97))+". "+entry.getKey(), col_a+5 , y,max_w_a,salto_linea,fontPlain,font_size);
                incide_va++;                
            }            
        }   
             
    }    

    private void addCuadroFooter(float y){
        Date FE_EMIS=null,FE_VENC=null;
        String moneda=nf.getMone_fact().getNO_MONE_FACT_ForJSON().toUpperCase();
        Double totales[]=this.totales ;//NETO,AJUSTE SI AFECTO,IGV,AJUSTE NO AFECTO,TOTAL_RECIBO
        float x=30F;
        float h=20F;
        float wt=0;
        float ys=0;
        float yts=0;
        float br=1.3F;
        float rz_footer_glosa=10;
        float rz_footer_monto=20;
        float font_size=Font_Size_General-5;
        

        FE_EMIS=nf.getCier().getFE_EMIS_ForJSON();
        FE_VENC=nf.getCier().getFE_VENC_ForJSON();

        ys=y;
        float marginLeftRectangle=25F;
        float widthRectangle=450F;
        float heightRectangle=65F;
        float marginInRectangle=5F;
        font_size=Font_Size_General-6;
        this.drawRectangle(x+marginLeftRectangle, ys-heightRectangle,widthRectangle, heightRectangle, br, Color.BLACK, Color.WHITE);
        this.drawString("", x+marginLeftRectangle+marginInRectangle,ys,font_size);ys-=rz_footer_glosa;
        this.drawString("ESTIMADO CLIENTE:", x+marginLeftRectangle+marginInRectangle,ys,font_size);ys-=rz_footer_glosa;        
        this.drawStringSize("S\u00edrvase cancelar su recibo en cualquier agencia del Banco de Cr\u00e9dito, Scotiabank \u00f3 Interbank. Indicar en ventanilla: C\u00f3digo n\u00famero de recibo "+this.getNumero(), x+marginLeftRectangle+marginInRectangle,ys,font_size);ys-=rz_footer_glosa;
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
    public void calcularTotales(){
        this.calcularTotales(null);
    }
    public void calcularTotales(Boolean excluir_detraccion){
        int son=18;
        //0 neto
        //1 ajustes afecto - Siempre se almacena negativo
        //2 igv
        //3 no afecto 
        //4 total
        //5 total positivo (+)
        //6 total negativo (-) = [8]+[9]
        //7 saldo (-)
        //8 devolucion promociones: es un valor negativo (-)
        //9 devolucion de cargos fijos (corte,suspensiones,downgrade,desactivacion) (-)       
        //10 ajuste no afecto igv - solo para soles el redondeo a 0.00 o 0.05 (-)
        //11 total retenciones 
        
        //12 total instalaciones
        //13 total alquiler equipos
        //14 total otros servicios
        //15 total renta mensual
        //16 total descuentos
        //17 diferencial de cargo fijo (para el caso de upgrade)
        
        boolean isUpgrade=false;
        boolean isUpgrade2=false;
        boolean conto_sucursal=false;
        this.cantidad_sucursales=0;
        this.pos_sucursal=-1;
        int pos_sucursal_local=-1;
        if (excluir_detraccion==null)
            excluir_detraccion=false;
        for(int i=0;i<son;i++)
            this.totales[i]=0d;
        for(SucursalDetalle sd : this.nf.negocio_detalle.getDetalle_sucursales()){
            pos_sucursal_local++;
//            isUpgrade=false;
            conto_sucursal=false;
            int cant_planes=sd.getDetalle_planes().size();
            if (cant_planes>1 && !this.periodo_principal){isUpgrade=true;}
            for(CIER_DATA_PLAN plan: sd.getDetalle_planes()){
                //normal
                if (FechaHora.isBetween(Periodo[0], Periodo[1], plan.getFE_INIC()) && FechaHora.isBetween(Periodo[0], Periodo[1], plan.getFE_FIN())){
                    if (plan.getST_TIPO_DEVO()==null){
                        this.totales[0]+=plan.getIM_MONT();//0 neto
                        this.totales[5]+=plan.getIM_MONT();//5 total positivo (+)
                        this.totales[15]+=plan.getIM_MONT();//15 total renta mensual
                        if (!conto_sucursal){
                            conto_sucursal=true;
                            this.cantidad_sucursales++;
                            this.pos_sucursal=pos_sucursal_local;
                        }
                    }else{ 
                        this.totales[6]+=plan.getIM_MONT()*-1;//6 total negativo (-) = [8]+[9]                        
                        if (plan.getST_TIPO_DEVO().equals(4))
                            this.totales[8]+=plan.getIM_MONT()*-1;//4 promociones
                        else{
                            this.totales[16]+=plan.getIM_MONT()*-1;//16 total descuentos
                            this.totales[9]+=plan.getIM_MONT()*-1;//9 devolucion de cargos fijos (corte,suspensiones,downgrade,desactivacion) //1 desactivacion,2 cortes,3 suspensiones, 5 downgrade 
                        }
                    }
                    if (plan.getIS_UPGRADE() != null){
                        if (plan.getIS_UPGRADE() && !this.periodo_principal){
                            isUpgrade2=true;
                        }
                    }
                }
            }
            for(CIER_DATA_SERV_SUPL ss: sd.getDetalle_servicios_suplementarios()){
                //normal
                if (FechaHora.isBetween(Periodo[0], Periodo[1], ss.getFE_INIC()) && FechaHora.isBetween(Periodo[0], Periodo[1], ss.getFE_FIN())){
                    if (!(excluir_detraccion && ss.getST_AFEC_DETR())){
                        if (ss.getST_TIPO_DEVO()==null){
                            this.totales[0]+=ss.getIM_MONT();//0 neto
                            this.totales[5]+=ss.getIM_MONT();//5 total positivo (+)
                            if (ss.getST_AFEC_DETR()){
                                this.totales[11]+=ss.getIM_MONT();//11 total retenciones
                            }
                            if (!conto_sucursal){
                                conto_sucursal=true;
                                this.cantidad_sucursales++;
                                this.pos_sucursal=pos_sucursal_local;
                            } 
                            
                            if (ss.getDE_NOMB().toLowerCase().indexOf("instalaci")!=-1){
                                this.totales[12]+=ss.getIM_MONT();
                            }else if (ss.getDE_NOMB().toLowerCase().indexOf("alquiler")!=-1){
                                this.totales[13]+=ss.getIM_MONT();
                            }else{
                                this.totales[14]+=ss.getIM_MONT();
                            }
                        }else{
                            this.totales[6]+=ss.getIM_MONT()*-1;//6 total negativo (-) = [8]+[9]                            
                            if (ss.getST_TIPO_DEVO().equals(4))
                                this.totales[8]+=ss.getIM_MONT()*-1;//4 promociones
                            else{
                                this.totales[16]+=ss.getIM_MONT()*-1;//16 total descuentos
                                this.totales[9]+=ss.getIM_MONT()*-1;//1 desactivacion,2 cortes,3 suspensiones, 5 downgrade 
                            }
                            if (ss.getST_AFEC_DETR()){
                                this.totales[11]+=ss.getIM_MONT()*-1;//11 total retenciones
                            }                        
                        }
                    }
                }
            }
            //servicios unicos
            if (this.periodo_principal){
                for(CIER_DATA_SERV_UNIC unic : sd.getDetalle_servicios_unicos()){
                    if (!(excluir_detraccion && unic.getST_AFEC_DETR())){
                        this.totales[0]+=unic.getIM_MONT();//0 neto
                        this.totales[5]+=unic.getIM_MONT();//5 total positivo (+)
                        if (unic.getST_AFEC_DETR()){
                            this.totales[11]+=unic.getIM_MONT();//11 total retenciones
                        }
                        if (!conto_sucursal){
                            conto_sucursal=true;
                            this.cantidad_sucursales++;
                            this.pos_sucursal=pos_sucursal_local;
                        } 
                        
                        if (unic.getDE_NOMB().toLowerCase().indexOf("instalaci")!=-1){
                            this.totales[12]+=unic.getIM_MONT();
                        }else if (unic.getDE_NOMB().toLowerCase().indexOf("alquiler")!=-1){
                            this.totales[13]+=unic.getIM_MONT();
                        }else{
                            this.totales[14]+=unic.getIM_MONT();
                        }                        
                    }
                }
            }
            
            //promociones de monto por sucursal
            for(CIER_DATA_PROM_MONT prom : sd.getDetalle_promociones_monto()){ 
                this.totales[6]+=prom.getIM_MONT()*-1;//6 total negativo (-) = [8]+[9]
                this.totales[8]+=prom.getIM_MONT()*-1;//4 promociones
                //this.totales[16]+=prom.getIM_MONT()*-1;
            }
        }
        //promociones de monto por negocio        
        for(CIER_DATA_PROM_MONT prom : this.nf.negocio_detalle.getDetalle_promociones_monto()){
            this.totales[6]+=prom.getIM_MONT()*-1;//6 total negativo (-) = [8]+[9]
            this.totales[8]+=prom.getIM_MONT()*-1;//4 promociones
            //this.totales[16]+=prom.getIM_MONT()*-1;
        }
        if(!isUpgrade2 && isUpgrade){
            isUpgrade=false;
        }
        if (this.saldo_pendiente<0){
            // Se setea el [9] para generar la devolucion correspondiente cuando haya un saldo pendiente por corte,suspensiones,downgrade,desactivacion. 
            // Y se suma a [6] porque es la suma de [8]+[9]
            this.totales[6]+=this.saldo_pendiente;//6 total negativo (-) = [8]+[9]
            this.totales[9]+=this.saldo_pendiente;//9 devolucion de cargos fijos (corte,suspensiones,downgrade,desactivacion) (-) 
            //this.totales[8]+=this.saldo_pendiente;//4 promociones            
        }
        
        if (this.pendiente_upgrade>0){
            this.totales[0]+=this.pendiente_upgrade;
            this.totales[5]+=this.pendiente_upgrade;//5 total positivo (+)
            this.totales[17]+=this.pendiente_upgrade;
        }
        
        this.totales[7]=this.totales[5]+this.totales[6]+this.totales[8]*-1;//saldo no consideradmos promociones por eso se quita
        if (this.totales[7]>0)
            this.totales[7]=0d;
        
        if (this.totales[5]>0){
            Double tmp=this.totales[5]+this.totales[6];
            if (tmp>=0){
                this.totales[1]+=this.totales[6];// Siempre es negativo [1]
            }else{
                //agregamos solo lo necesario de las promociones
                // La suma neto debe dar cero y el monto restante de las devoluciones es el saldo pendiente.
                // Aplicando la regla: Primero promociones y despues devoluciones.
                
                //Cuando las promociones (VALOR POSITIVO) es mayor o igual que el total positivo
                if (this.totales[8]*-1>=this.totales[5]){
                    
                    if (this.totales[9]!=0){ // [9] devoluciones
                        this.totales[7]+=this.totales[9];
                        this.totales[9]=0d;
                        
                        this.totales[6]-=this.totales[8];
                        this.totales[8]=this.totales[5]*-1;
                        this.totales[6]+=this.totales[8];
                        
                        this.totales[1]=this.totales[5]*-1; // Se actualiza [1] ajustes afecto
                    }else{
                        
                        this.totales[6]-=this.totales[8];
                        this.totales[8]=this.totales[5]*-1;
                        this.totales[6]+=this.totales[8];
                        
                        this.totales[1]=this.totales[5]*-1; // Se actualiza [1] ajustes afecto                       
                    }
                    /*this.totales[6]-=this.totales[8];
                    this.totales[8]=this.totales[5]*-1;
                    this.totales[6]+=this.totales[8];
                    
                    this.totales[1]=this.totales[5]*-1;*/
                }else{
                    this.totales[7]=this.totales[5]+(this.totales[6]);
                    this.totales[1]=this.totales[5]*-1;
                }
                /*
                
                this.totales[6]+=this.totales[8]*-1;
                this.totales[6]+=this.totales[0]*-1;
                
                this.totales[8]=this.totales[0]*-1;
                this.totales[1]+=this.totales[8];
                */
            }
        }
        
        /*
        if (this.totales[5]>0 && this.totales[7]<0) {
            //si tiene un valor positivo y tiene un saldo el recibo se vuelve cero
            this.totales[1]=this.totales[0]*-1;   
            //this.totales[16]+=Math.abs(this.totales[1])*-1;
        }else{
            this.totales[1]=this.totales[8]+this.totales[9];
            //this.totales[16]+=Math.abs(this.totales[1])*-1;
        }
        */
        
        this.nf.ajustes_pendientes_aplican=new ArrayList<AJUS>();
        if (this.periodo_principal){
            for(AJUS ajus : this.nf.ajustes_pendientes_todos){
                if (ajus.getST_AFEC_IGV_ForJSON()){
                    if (this.totales[0]+this.totales[1]-ajus.getIM_MONT_ForJSON()>=0){
                        this.totales[1]+=ajus.getIM_MONT_ForJSON()*-1;
                        this.totales[16]+=ajus.getIM_MONT_ForJSON()*-1;
                        this.nf.ajustes_pendientes_aplican.add(ajus);
                    }
                }else{
                    if ((this.totales[0]+this.totales[1])*1.18+this.totales[3]-ajus.getIM_MONT_ForJSON()>=0){
                        this.totales[3]+=ajus.getIM_MONT_ForJSON()*-1;
                        this.totales[16]+=ajus.getIM_MONT_ForJSON()*-1;
                        this.nf.ajustes_pendientes_aplican.add(ajus);
                    }               
                }
            }
        }
        
        this.totales[2]=(this.totales[0]+this.totales[1])*0.18;
        this.totales[4]=this.totales[0]+this.totales[1]+this.totales[2]+this.totales[3];          
        
        //redondeo soles
        if (this.nf.getNego().getCO_MONE_FACT_ForJSON()==1){//soles
            //redondeo a 0.00 o 0.05            
            Double tmp=com.americatel.facturacion.fncs.Numero.redondear(this.totales[4]*10,2);
            Double tmpEnt=Math.floor(tmp);
            Double tmpDiff=com.americatel.facturacion.fncs.Numero.redondear(tmp-tmpEnt,2);
            if (tmpDiff>=0.5d)
                this.totales[10]=(com.americatel.facturacion.fncs.Numero.redondear((tmpDiff-0.5d)/10,2))*-1;
            else
                this.totales[10]=(com.americatel.facturacion.fncs.Numero.redondear((tmpDiff)/10,2))*-1;
            this.totales[3]+=this.totales[10];
            this.totales[16]+=Math.abs(this.totales[10])*-1;
        }
        this.totales[4]=this.totales[0]+this.totales[1]+this.totales[2]+this.totales[3];  
        if (isUpgrade){
            this.totales[17]=this.totales[0];
            this.totales[5]=0d;
        }
        if (!excluir_detraccion){
            this.generaFacturaDetraccion=false;
            Double valor_soles=this.totales[11];
            if (this.nf.getNego().getCO_MONE_FACT_ForJSON().equals(2)){// 1 soles, 2 dolares
                valor_soles*=this.nf.getCier().getNU_TIPO_CAMB_ForJSON();
            }
            if (valor_soles>700){
                this.generaFacturaDetraccion=true;
            }
            if ( this.generaFacturaDetraccion){
                this.calcularTotales(true);
            }
        }

    }
    
    public Boolean isGeneraFacturaDetraccion(){
        return this.generaFacturaDetraccion;
    }
    public Boolean isReciboValido(){
        return this.totales[5]>0;
    }
    public Double getSaldo(){
        return this.totales[7];
    }
    public Double getPendienteUpgrade(){
        return this.totales[17];
    }
    private Float evaluarLineasAntes(Float y,Float min_y){
        //funcion que evalua las lineas dibujadas para agregar otra hoja de pdf, antes de dibujar
        if (y<min_y){
            this.addPage();        
            y=780f;
        }
        return y;
    }    
    public void crearPdfDetallado(){
        if (this.cantidad_sucursales>1){
            float font_size=Font_Size_General-6;
            int incide_va=0;
            int contador_items=0;
            float col_a=40;
            float col_b=110;
            float col_c=350;
            float salto_linea=font_size+1;
            float y=780;
            Double monto_devolucion_cargos_fijos=0d;
            Double diferencial_cargos_fijos=0d;
            int van_sucursales=0;
            int van_letras=0;

            this.addPage();                  
            this.drawString("SE\u00d1OR (ES): ",col_a , y,fontBold,font_size);
            this.drawString(this.nf.getClie().getFullNameCliente(),col_b , y,font_size);
            y-=salto_linea;                  
            this.drawString("N\u00b0 NEGOCIO: ",col_a , y,fontBold,font_size);
            this.drawString(this.nf.getNego().getCO_NEGO_ForJSON().toString() ,col_b , y,font_size);
            y-=salto_linea*2F;
            this.drawRectangle(col_a-2, y, 500,salto_linea   , 1F, Color.BLACK,Color.LIGHT_GRAY);
            this.drawString("DETALLE DEL NEGOCIO",col_a , y+1,fontBold,font_size);
            y-=salto_linea*2;        

            for(SucursalDetalle sd : this.nf.negocio_detalle.getDetalle_sucursales()){
                monto_devolucion_cargos_fijos=0d;
                diferencial_cargos_fijos=0d;
                van_sucursales++;
                CIER_DATA_SUCU sucu=sd.getSucu();
                y=this.evaluarLineasAntes(y,200F);
                this.drawString("Sucursal "+van_sucursales+": ",col_a , y,fontBold,font_size);
                this.drawString(sucu.getDE_DIRE_SUCU()+ " ("+sucu.getNO_DIST()+" - "+sucu.getNO_PROV()+" - "+sucu.getNO_DEPA()+")",col_b , y,font_size);
                y-=salto_linea; 
                if (sd.getSucu().getDE_ORDE()!=null && sd.getSucu().getDE_ORDE().length()>0 ){
                    this.drawString("Orden de Compra:",col_a , y,fontBold,font_size);
                    this.drawString(sd.getSucu().getDE_ORDE(),col_b , y,font_size);
                    y-=salto_linea; 
                }
                van_letras=0;
                for(CIER_DATA_PLAN plan : sd.getDetalle_planes()){
                    if (FechaHora.isBetween(Periodo[0], Periodo[1], plan.getFE_INIC()) && FechaHora.isBetween(Periodo[0], Periodo[1], plan.getFE_FIN())){
                        if (plan.getST_TIPO_DEVO()==null){
                            this.drawString(Character.toString((char)(van_letras+97))+". "+plan.getDE_NOMB(),col_a , y,font_size);
                            this.drawString(this.getStringDecimalFormat(plan.getIM_MONT(),2),col_c , y,fontPlain,font_size,EReciboTextAlign.DERECHA);
                            van_letras++; 
                            y-=salto_linea;
                        }else{
                            this.drawString(Character.toString((char)(van_letras+97))+". "+plan.getDE_NOMB(),col_a , y,font_size);
                            this.drawString(this.getStringDecimalFormat(plan.getIM_MONT()*-1,2),col_c , y,fontPlain,font_size,EReciboTextAlign.DERECHA);
                            van_letras++; 
                            y-=salto_linea;
                            if (!plan.getST_TIPO_DEVO().equals(4)){
                                monto_devolucion_cargos_fijos+=plan.getIM_MONT()*-1;
                            }
                        }
                    }
                }
//                y-=salto_linea;
                for(CIER_DATA_SERV_SUPL ss : sd.getDetalle_servicios_suplementarios()){
                    if (!(this.generaFacturaDetraccion && ss.getST_AFEC_DETR())){
                        if (FechaHora.isBetween(Periodo[0], Periodo[1], ss.getFE_INIC()) && FechaHora.isBetween(Periodo[0], Periodo[1], ss.getFE_FIN())){
                            if (ss.getST_TIPO_DEVO()==null){
                                this.drawString(Character.toString((char)(van_letras+97))+". "+ss.getDE_NOMB(),col_a , y,font_size);
                                this.drawString(this.getStringDecimalFormat(ss.getIM_MONT(),2),col_c , y,fontPlain,font_size,EReciboTextAlign.DERECHA);
                                van_letras++; 
                                y-=salto_linea;
                            }else{
                                this.drawString(Character.toString((char)(van_letras+97))+". "+ss.getDE_NOMB(),col_a , y,font_size);
                                this.drawString(this.getStringDecimalFormat(ss.getIM_MONT()*-1,2),col_c , y,fontPlain,font_size,EReciboTextAlign.DERECHA);
                                van_letras++; 
                                y-=salto_linea;  
                                if (!ss.getST_TIPO_DEVO().equals(4)){
                                    monto_devolucion_cargos_fijos+=ss.getIM_MONT()*-1;
                                }
                            }
                        }
                    }
                }
//                y-=salto_linea;
                
                if (this.periodo_principal){
                    for(CIER_DATA_SERV_UNIC su : sd.getDetalle_servicios_unicos()){
                        if (!(this.generaFacturaDetraccion && su.getST_AFEC_DETR())){
                            this.drawString(Character.toString((char)(van_letras+97))+". "+su.getDE_NOMB(),col_a , y,font_size);
                            this.drawString(this.getStringDecimalFormat(su.getIM_MONT(),2),col_c , y,fontPlain,font_size,EReciboTextAlign.DERECHA);
                            van_letras++; 
                            y-=salto_linea;                           
                        }
                    }
                }
//                y-=salto_linea; 
                
                // Devolucion de cargos fijos por sucursal
                this.drawString(Character.toString((char)(van_letras+97))+". "+"Devoluci\u00f3n de Cargo Fijo",col_a , y,font_size);
                this.drawString(this.getStringDecimalFormat(monto_devolucion_cargos_fijos,2),col_c , y,fontPlain,font_size,EReciboTextAlign.DERECHA);
                y-=salto_linea;
                y-=salto_linea;
            }
        }
    }    
    
    public Long getNumero(){
        return this.Numero;
    }

    public Date getPeriodoInicio(){
        return this.Periodo[0];
    }

    public Date getPeriodoFin(){
        return this.Periodo[1];
    }    

    void saveBD() {
        NEGO_SUCU nego_sucu_inst=this.nf.negocio_detalle.getDetalle_sucursales().get(0).getSucu().getNEGO_SUCU();
        SUCU sucu_inst=nego_sucu_inst.getSUCU();
        DIST dist_inst=sucu_inst.getDist_ForJSON();
        PROV prov_inst=dist_inst.getPROV_ForJSON();
        DEPA depa_inst=prov_inst.getDEPA_ForJSON();
        
        RECI reci=new RECI(); 
        reci.setCO_CIER(this.nf.getCier().getCO_CIER_ForJSON());
        reci.setCO_CLIE(this.nf.getClie().getCO_CLIE_ForJSON());
        reci.setCO_DIST_CORR(this.nf.getDist_corr().getCO_DIST_ForJSON());
        reci.setCO_DIST_FISC(this.nf.getDist_fiscal().getCO_DIST_ForJSON());
        reci.setCO_DIST_INST(dist_inst.getCO_DIST_ForJSON());
        reci.setCO_MONE_FACT(this.nf.getNego().getCO_MONE_FACT_ForJSON());
        reci.setCO_NEGO(this.nf.getNego().getCO_NEGO_ForJSON());
        reci.setCO_RECI(this.Numero);
        reci.setCO_TIPO_DOCU(this.nf.getClie().getCO_TIPO_DOCU_ForJSON());
        reci.setDE_CODI_BUM(this.nf.getClie().getDE_CODI_BUM_ForJSON()+"-"+this.nf.getClie().getDE_DIGI_BUM_ForJSON());
        reci.setDE_DIRE_CORR(this.nf.getSucu_corr().getDE_DIRE_ForJSON());
        reci.setDE_REF_DIRE_CORR(this.nf.getSucu_corr().getDE_REF_DIRE_ForJSON());
        reci.setDE_DIRE_FISC(this.nf.getSucu_fiscal().getDE_DIRE_ForJSON());
        reci.setDE_DIRE_INST(sucu_inst.getDE_DIRE_ForJSON());
        reci.setDE_NUME_DOCU(this.nf.getClie().getDE_NUME_DOCU_ForJSON());
        reci.setDE_PERI(this.getPeriodoString());
        reci.setDE_SIMB_MONE_FACT(this.nf.getMone_fact().getDE_SIMB_ForJSON());
        
        reci.setIM_AJUS_NIGV(this.totales[3]);
        reci.setIM_AJUS_SIGV(this.totales[1]);
        reci.setIM_ALQU(this.totales[13]);
        reci.setIM_DESC(this.totales[16]);
        reci.setIM_IGV(this.totales[2]);
        reci.setIM_INST(this.totales[12]);
        reci.setIM_NETO(this.totales[0]);
        reci.setIM_OTRO(this.totales[14]);
        reci.setIM_SERV_MENS(this.totales[15]);
        reci.setIM_TOTA(this.totales[4]);
        
        reci.setNO_CLIE(this.nf.getClie().getFullNameCliente());
        reci.setNO_DIST_CORR(this.nf.getDist_corr().getNO_DIST_ForJSON());
        reci.setNO_DIST_FISC(this.nf.getDist_fiscal().getNO_DIST_ForJSON());
        reci.setNO_DIST_INST(dist_inst.getNO_DIST_ForJSON());
        reci.setNO_MONE_FACT(this.nf.getMone_fact().getNO_MONE_FACT_ForJSON());
        reci.setNO_TIPO_DOCU(this.nf.getClie().getTipoDocumento().getNO_TIPO_DOCU_ForJSON());
        
        reci.setST_ANUL(Boolean.FALSE);
        reci.setST_ELIM(Boolean.FALSE);
        
        reci.setFE_EMIS(this.nf.getCier().getFE_EMIS_ForJSON());
        reci.setFE_VENC(this.nf.getCier().getFE_VENC_ForJSON());
        reci.save();
        
        //guardamos los ajustes que se aplicaron
        for(AJUS ajus : this.nf.ajustes_pendientes_aplican){
            ajus.setCO_CIER_APLI(this.nf.getCier().getCO_CIER_ForJSON());
            ajus.saveCierreAplico();
        }
    }

    public Boolean isPeriodo_principal() {
        return periodo_principal;
    }


}
