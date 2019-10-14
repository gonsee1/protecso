/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.core2;

import com.americatel.facturacion.fncs.FechaHora;
import com.americatel.facturacion.models.CIER;
import com.americatel.facturacion.models.CIER_DATA_SERV_SUPL;
import com.americatel.facturacion.models.CIER_DATA_SERV_UNIC;
import com.americatel.facturacion.models.CLIE;
import com.americatel.facturacion.models.DEPA;
import com.americatel.facturacion.models.DIST;
import com.americatel.facturacion.models.FACT;
import com.americatel.facturacion.models.NEGO;
import com.americatel.facturacion.models.NEGO_SUCU;
import com.americatel.facturacion.models.PROV;
import com.americatel.facturacion.models.SUCU;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;

/**
 *
 * @author crodas
 */
public class Factura   extends LibPdf  {
    private NegocioFacturar nf=null;
    private Long Numero;
    private Double[] totales=new Double[9];
    private ListaRecibo listaRecibo;  
    private List<Date[]> periodos;
    protected List<PDPageContentStream> coss=new ArrayList<PDPageContentStream>();
    Factura(NegocioFacturar nf, Long Numero_Factura_Semilla,ListaRecibo listaRecibo,List<Date[]> periodos) {
        this.nf=nf;
        this.Numero=Numero_Factura_Semilla;
        this.listaRecibo=listaRecibo; 
        this.periodos=periodos;
    }
    
    public String getNombre(){        
        return "F"+this.Numero+"-"+this.nf.getNego().getCO_NEGO_ForJSON()+"-"+this.nf.getCier().getNU_ANO_ForJSON()+"-"+this.nf.getCier().getNU_PERI_ForJSON();
    }
    
    public Boolean generaFactura(List<Date[]> periodos){
        Boolean generaFactura=false;
        List<Date[]> periodosFactura=periodos;
        Double totalFactura=0d;
        int son=periodos.size();
        Boolean periodo_principal=false;
        int van=0;
        
        if (!periodosFactura.isEmpty()){
            for(Date[] periodo : periodosFactura){
                van++;
                periodo_principal=(van==son);
                for(SucursalDetalle sd : this.nf.negocio_detalle.getDetalle_sucursales()){
                    for(CIER_DATA_SERV_SUPL ss: sd.getDetalle_servicios_suplementarios()){
                        if (FechaHora.isBetween(periodo[0], periodo[1], ss.getFE_INIC()) && FechaHora.isBetween(periodo[0], periodo[1], ss.getFE_FIN())){
                            if (ss.getST_AFEC_DETR()){
                                if (ss.getST_TIPO_DEVO()==null){
                                    totalFactura+=com.americatel.facturacion.fncs.Numero.redondear(ss.getIM_MONT(),2);
                                }
                            }
                        }
                    }
                    if (periodo_principal){
                        for(CIER_DATA_SERV_UNIC unic : sd.getDetalle_servicios_unicos()){
                            if (unic.getST_AFEC_DETR()){
                                totalFactura+=com.americatel.facturacion.fncs.Numero.redondear(unic.getIM_MONT(),2);                               
                            }
                        }                               
                    }
                }  
            }
        } else {
            for(SucursalDetalle sd : this.nf.negocio_detalle.getDetalle_sucursales()){
                for(CIER_DATA_SERV_UNIC unic : sd.getDetalle_servicios_unicos()){
                    if (unic.getST_AFEC_DETR()){
                        totalFactura+=com.americatel.facturacion.fncs.Numero.redondear(unic.getIM_MONT(),2);                               
                    }
                }
            }
        }
        
        Double valor_soles=totalFactura;
        if (this.nf.getNego().getCO_MONE_FACT_ForJSON().equals(2)){// 1 soles, 2 dolares
            valor_soles*=this.nf.getCier().getNU_TIPO_CAMB_ForJSON();
        }
        if (valor_soles > 700){
            generaFactura=true;
        }
        return generaFactura;
    }
    
    void crearPdf() {
        this.addPage();
        this.addDetalle(650);
        this.addCuadroFooter(150);
        this.cerrarCoss();
    }
    
    private void addHeader(float y) {
        CIER cier=this.nf.getCier();
        CLIE clie=this.nf.getClie();
        SUCU sucu=this.nf.getSucu_fiscal();
        DIST dist=this.nf.getDist_fiscal();
        PROV prov=this.nf.getProv_fiscal();
        DEPA depa=this.nf.getDepa_fiscal();
        NEGO nego=this.nf.getNego();
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
        this.drawString("010 - "+com.americatel.facturacion.fncs.Numero.formatearNumeroDigitos(this.Numero, 6), 360, y-110,this.fontBold,20);
        
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
        coss.add(this.cos);
        if (numero_paginas==1){            
            this.addPageUno();
        }else{
            //this.addDemasPages();
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
    
    private void addDetalle(float y) {
        float x_a=70;
        float x_b=350;
        float x_c=500;
        float alto=10;
        int son=9;
        for(int i=0;i<son;i++)
            this.totales[i]=0d;
        
        Map<Integer,CIER_DATA_SERV_SUPL> data_servicios_supl;
        Integer contador=0;
        if (!listaRecibo.getRecibos().isEmpty()){
            for(Recibo rec : listaRecibo.getRecibos()){
                Date Periodo[]=new Date[]{rec.getPeriodoInicio(),rec.getPeriodoFin()};
                if (rec.isGeneraFacturaDetraccion()){
                    for(SucursalDetalle sd : this.nf.negocio_detalle.getDetalle_sucursales()){
                        data_servicios_supl = new TreeMap<Integer, CIER_DATA_SERV_SUPL>();
                        contador=0;
                        for(CIER_DATA_SERV_SUPL ss: sd.getDetalle_servicios_suplementarios()){
                            contador++;
                            if (FechaHora.isBetween(Periodo[0], Periodo[1], ss.getFE_INIC()) && FechaHora.isBetween(Periodo[0], Periodo[1], ss.getFE_FIN())){
                                if (ss.getST_AFEC_DETR()){
                                    if (ss.getST_TIPO_DEVO()==null){
                                        this.totales[0]+=com.americatel.facturacion.fncs.Numero.redondear(ss.getIM_MONT(),2);

                                        if (ss.getDE_NOMB().toLowerCase().indexOf("instalaci")!=-1){
                                            this.totales[6]+=com.americatel.facturacion.fncs.Numero.redondear(ss.getIM_MONT(),2);
                                        }else if (ss.getDE_NOMB().toLowerCase().indexOf("alquiler")!=-1){
                                            this.totales[7]+=com.americatel.facturacion.fncs.Numero.redondear(ss.getIM_MONT(),2);
                                        }else{
                                            this.totales[8]+=com.americatel.facturacion.fncs.Numero.redondear(ss.getIM_MONT(),2);
                                        }
                                        
                                        ss.setCO_RECI(null);
                                        ss.setCO_FACT(this.Numero);
                                        
                                        if (!data_servicios_supl.containsKey(ss.getCO_NEGO_SUCU_SERV_SUPL())){
                                            ss.setIM_TOTAL(com.americatel.facturacion.fncs.Numero.redondear(ss.getIM_MONT(),2));
                                            ss.setDE_PERIODO(ss.getPeriodosFormateados());
                                            data_servicios_supl.put(ss.getCO_NEGO_SUCU_SERV_SUPL(), ss);
                                        } else {
                                            Double total=data_servicios_supl.get(ss.getCO_NEGO_SUCU_SERV_SUPL()).getIM_TOTAL();
                                            String periodo_serv_supl=data_servicios_supl.get(ss.getCO_NEGO_SUCU_SERV_SUPL()).getDE_PERIODO();
                                            total+=com.americatel.facturacion.fncs.Numero.redondear(ss.getIM_MONT(),2);
                                            
                                            if (sd.getDetalle_servicios_suplementarios().size()==contador){
                                                periodo_serv_supl=periodo_serv_supl.substring(0, periodo_serv_supl.length()-1);
                                                periodo_serv_supl+=" y "+ss.getPeriodosFormateados();  
                                             } else {
                                                periodo_serv_supl+=ss.getPeriodosFormateados()+ ",";
                                             }
                                            
                                            data_servicios_supl.get(ss.getCO_NEGO_SUCU_SERV_SUPL()).setIM_TOTAL(total);
                                            data_servicios_supl.get(ss.getCO_NEGO_SUCU_SERV_SUPL()).setDE_PERIODO(periodo_serv_supl);
                                        }
                                    }
                                }
                            }
                        }
                        
                        for (Map.Entry<Integer,CIER_DATA_SERV_SUPL> entry : data_servicios_supl.entrySet())
                        {
                            this.drawStringChico( entry.getValue().getDE_NOMB() , x_a, y);
                            this.drawStringPequenio( "("+entry.getValue().getPeriodoAnidadoFormateado()+")" , x_b, y);
                            this.drawStringChico( this.getStringDecimalFormat(entry.getValue().getIM_TOTAL(),2), x_c, y);
                            
                            y-=alto;
                        }
                        
                        if (rec.isPeriodo_principal()){
                            for(CIER_DATA_SERV_UNIC unic : sd.getDetalle_servicios_unicos()){
                                if (unic.getST_AFEC_DETR()){
                                    this.drawStringChico( unic.getDE_NOMB() , x_a, y);
                                    this.drawStringChico( this.getStringDecimalFormat(unic.getIM_MONT(),2), x_c, y);
                                    this.totales[0]+=com.americatel.facturacion.fncs.Numero.redondear(unic.getIM_MONT(),2);

                                    if (unic.getDE_NOMB().toLowerCase().indexOf("instalaci")!=-1){
                                        this.totales[6]+=com.americatel.facturacion.fncs.Numero.redondear(unic.getIM_MONT(),2);
                                    }else if (unic.getDE_NOMB().toLowerCase().indexOf("alquiler")!=-1){
                                        this.totales[7]+=com.americatel.facturacion.fncs.Numero.redondear(unic.getIM_MONT(),2);
                                    }else{
                                        this.totales[8]+=com.americatel.facturacion.fncs.Numero.redondear(unic.getIM_MONT(),2);
                                    }

                                    y-=alto;  
                                    unic.setCO_RECI(null);
                                    unic.setCO_FACT(this.Numero);                                
                                }
                            }                               
                        }
                    }

                }
            }
        } else {
            Boolean periodo_principal=false;
            int van=0;
            int sonPeriodos=periodos.size();
            if (sonPeriodos != 0){
                for(Date[] periodo : periodos){
                    van++;
                    periodo_principal=(van==sonPeriodos);
                    for(SucursalDetalle sd : this.nf.negocio_detalle.getDetalle_sucursales()){
                        data_servicios_supl = new TreeMap<Integer, CIER_DATA_SERV_SUPL>();
                        for(CIER_DATA_SERV_SUPL ss: sd.getDetalle_servicios_suplementarios()){
                            if (FechaHora.isBetween(periodo[0], periodo[1], ss.getFE_INIC()) && FechaHora.isBetween(periodo[0], periodo[1], ss.getFE_FIN())){
                                if (ss.getST_AFEC_DETR()){
                                    if (ss.getST_TIPO_DEVO()==null){
                                        this.totales[0]+=com.americatel.facturacion.fncs.Numero.redondear(ss.getIM_MONT(),2);

                                        if (ss.getDE_NOMB().toLowerCase().indexOf("instalaci")!=-1){
                                            this.totales[6]+=com.americatel.facturacion.fncs.Numero.redondear(ss.getIM_MONT(),2);
                                        }else if (ss.getDE_NOMB().toLowerCase().indexOf("alquiler")!=-1){
                                            this.totales[7]+=com.americatel.facturacion.fncs.Numero.redondear(ss.getIM_MONT(),2);
                                        }else{
                                            this.totales[8]+=com.americatel.facturacion.fncs.Numero.redondear(ss.getIM_MONT(),2);
                                        }

                                        ss.setCO_RECI(null);
                                        ss.setCO_FACT(this.Numero);
                                        
                                        if (!data_servicios_supl.containsKey(ss.getCO_NEGO_SUCU_SERV_SUPL())){
                                            ss.setIM_TOTAL(com.americatel.facturacion.fncs.Numero.redondear(ss.getIM_MONT(),2));
                                            ss.setDE_PERIODO(ss.getPeriodosFormateados());
                                            data_servicios_supl.put(ss.getCO_NEGO_SUCU_SERV_SUPL(), ss);
                                        } else {
                                            Double total=data_servicios_supl.get(ss.getCO_NEGO_SUCU_SERV_SUPL()).getIM_TOTAL();
                                            String periodo_serv_supl=data_servicios_supl.get(ss.getCO_NEGO_SUCU_SERV_SUPL()).getDE_PERIODO();
                                            total+=com.americatel.facturacion.fncs.Numero.redondear(ss.getIM_MONT(),2);
                                            
                                            if (sd.getDetalle_servicios_suplementarios().size()==contador){
                                                periodo_serv_supl=periodo_serv_supl.substring(0, periodo_serv_supl.length()-1);
                                                periodo_serv_supl+=" y "+ss.getPeriodosFormateados();  
                                             } else {
                                                periodo_serv_supl+=ss.getPeriodosFormateados()+ ",";
                                             }
                                            
                                            data_servicios_supl.get(ss.getCO_NEGO_SUCU_SERV_SUPL()).setIM_TOTAL(total);
                                            data_servicios_supl.get(ss.getCO_NEGO_SUCU_SERV_SUPL()).setDE_PERIODO(periodo_serv_supl);
                                            
                                        }
                                    }
                                }
                            }
                        }

                        for (Map.Entry<Integer,CIER_DATA_SERV_SUPL> entry : data_servicios_supl.entrySet())
                        {
                            this.drawStringChico( entry.getValue().getDE_NOMB() , x_a, y);
                            this.drawStringPequenio( "("+entry.getValue().getPeriodoAnidadoFormateado()+")" , x_b, y);
                            this.drawStringChico( this.getStringDecimalFormat(entry.getValue().getIM_TOTAL(),2), x_c, y);

                            y-=alto;
                        }
                        if (periodo_principal){
                            for(CIER_DATA_SERV_UNIC unic : sd.getDetalle_servicios_unicos()){
                                if (unic.getST_AFEC_DETR()){
                                    this.drawStringChico( unic.getDE_NOMB() , x_a, y);
                                    this.drawStringChico( this.getStringDecimalFormat(unic.getIM_MONT(),2), x_c, y);
                                    this.totales[0]+=com.americatel.facturacion.fncs.Numero.redondear(unic.getIM_MONT(),2);

                                    if (unic.getDE_NOMB().toLowerCase().indexOf("instalaci")!=-1){
                                        this.totales[6]+=com.americatel.facturacion.fncs.Numero.redondear(unic.getIM_MONT(),2);
                                    }else if (unic.getDE_NOMB().toLowerCase().indexOf("alquiler")!=-1){
                                        this.totales[7]+=com.americatel.facturacion.fncs.Numero.redondear(unic.getIM_MONT(),2);
                                    }else{
                                        this.totales[8]+=com.americatel.facturacion.fncs.Numero.redondear(unic.getIM_MONT(),2);
                                    }

                                    y-=alto;  
                                    unic.setCO_RECI(null);
                                    unic.setCO_FACT(this.Numero);                                
                                }
                            }                               
                        }
                    }
                }
            } else {
                for(SucursalDetalle sd : this.nf.negocio_detalle.getDetalle_sucursales()){
                    for(CIER_DATA_SERV_UNIC unic : sd.getDetalle_servicios_unicos()){
                        if (unic.getST_AFEC_DETR()){
                            this.drawStringChico( unic.getDE_NOMB() , x_a, y);
                            this.drawStringChico( this.getStringDecimalFormat(unic.getIM_MONT(),2), x_c, y);
                            this.totales[0]+=com.americatel.facturacion.fncs.Numero.redondear(unic.getIM_MONT(),2);

                            if (unic.getDE_NOMB().toLowerCase().indexOf("instalaci")!=-1){
                                this.totales[6]+=com.americatel.facturacion.fncs.Numero.redondear(unic.getIM_MONT(),2);
                            }else if (unic.getDE_NOMB().toLowerCase().indexOf("alquiler")!=-1){
                                this.totales[7]+=com.americatel.facturacion.fncs.Numero.redondear(unic.getIM_MONT(),2);
                            }else{
                                this.totales[8]+=com.americatel.facturacion.fncs.Numero.redondear(unic.getIM_MONT(),2);
                            }

                            y-=alto;  
                            unic.setCO_RECI(null);
                            unic.setCO_FACT(this.Numero);                                
                        }
                    }
                }
            }
        }
        
        this.totales[2]=com.americatel.facturacion.fncs.Numero.redondear(this.totales[0]+this.totales[1],2);
        this.totales[3]=com.americatel.facturacion.fncs.Numero.redondear(this.totales[2]*0.18,2);
        this.totales[4]=com.americatel.facturacion.fncs.Numero.redondear(this.totales[2]+this.totales[3],2);
        
        //redondeo soles
        if (this.nf.getNego().getCO_MONE_FACT_ForJSON()==1){//soles
            //redondeo a 0.00 o 0.05            
            Double tmp=com.americatel.facturacion.fncs.Numero.redondear(this.totales[4],2);
            Double tmpEnt=Math.floor(tmp);
            Double tmpDiff=com.americatel.facturacion.fncs.Numero.redondear(tmp-tmpEnt,2)*10;
            tmpEnt=Math.floor(tmpDiff);
            tmpDiff=com.americatel.facturacion.fncs.Numero.redondear(tmpDiff-tmpEnt,1);

            if (tmpDiff!=0.0d && tmpDiff!=0.5d){
                tmp=com.americatel.facturacion.fncs.Numero.redondear(this.totales[4]*10,2);
                tmpEnt=Math.floor(tmp);
                tmpDiff=com.americatel.facturacion.fncs.Numero.redondear(tmp-tmpEnt,2);
                if (tmpDiff>=0.5d)
                    this.totales[5]+=(com.americatel.facturacion.fncs.Numero.redondear((tmpDiff-0.5d)/10,2))*-1;
                else
                    this.totales[5]+=(com.americatel.facturacion.fncs.Numero.redondear((tmpDiff)/10,2))*-1;
            }
        }
        
        this.totales[4]=com.americatel.facturacion.fncs.Numero.redondear(this.totales[4]+this.totales[5],2);
        
    }    
    private void addCuadroFooter(float y) {
        float alto=10;
        float ty=y;
        float posXi=440;
        float posXf=450;
        float borde=2f;
        float widthRect=90f;                
        

        this.drawStringMedianoAlignDerecha( "VALOR VENTA" , posXi, ty);
        this.drawRectangle(posXf,ty -borde, widthRect, alto,borde);
        this.drawStringMedianoAlignDerecha( ""+this.getStringDecimalFormat(totales[0],2) , posXi+widthRect, ty);
        ty-=alto;

        this.drawStringMedianoAlignDerecha( "DESCUENTO" , posXi, ty);
        this.drawRectangle(posXf,ty -borde, widthRect, alto,borde);
        this.drawStringMedianoAlignDerecha( ""+this.getStringDecimalFormat(totales[1],2) , posXi+widthRect, ty);
        ty-=alto;  
        
        this.drawStringMedianoAlignDerecha( "VALOR DE VENTA NETO" , posXi, ty);
        this.drawRectangle(posXf,ty -borde, widthRect, alto,borde);
        this.drawStringMedianoAlignDerecha( ""+this.getStringDecimalFormat(totales[2],2) , posXi+widthRect, ty);
        ty-=alto;         

        this.drawStringMedianoAlignDerecha( "I.G.V." , posXi, ty);
        this.drawRectangle(posXf,ty -borde, widthRect, alto,borde);
        this.drawStringMedianoAlignDerecha( ""+this.getStringDecimalFormat(totales[3],2) , posXi+widthRect, ty);
        ty-=alto;
        
        this.drawStringMedianoAlignDerecha( "AJUSTE POR REDONDEO" , posXi, ty);
        this.drawRectangle(posXf,ty -borde, widthRect, alto,borde);
        this.drawStringMedianoAlignDerecha( ""+this.getStringDecimalFormat(totales[5],2) , posXi+widthRect, ty);
        ty-=alto;
        
        this.drawStringMedianoAlignDerecha( "TOTAL" , posXi, ty);
        this.drawRectangle(posXf,ty -borde, widthRect, alto,borde);
        this.drawStringMedianoAlignDerecha( ""+this.getStringDecimalFormat(totales[4],2) , posXi+widthRect, ty);
        ty-=alto;        
        
    }     
    
    public void savePdf(){        
        if (this.nf.preview){
            try {  
                ZipOutputStream zout=this.listaRecibo.getZout();
                OutputStream outStream=new ByteArrayOutputStream (); 
                this.save(outStream);
                ByteArrayOutputStream outStreamByte=(ByteArrayOutputStream) outStream;  
                String nombre=this.getNombre(); 
                ZipEntry ze = new ZipEntry(nombre+".pdf");
                zout.putNextEntry(ze);
                zout.write(outStreamByte.toByteArray());
                zout.closeEntry();
                zout.close();
            } catch (Exception e) {
                e.printStackTrace();
            }            
        }else{
            String pathBase=ListaRecibo.getPathBaseRecibos();
            String nombre=this.getNombre(); 
            File file=new File(pathBase+nombre+".pdf");
            FileOutputStream fos;
            try {
                fos = new FileOutputStream(file);
                this.save(fos);
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }                   
        }            
    }

    void saveBD() {
        NEGO_SUCU nego_sucu_inst=this.nf.negocio_detalle.getDetalle_sucursales().get(0).getSucu().getNEGO_SUCU();
        SUCU sucu_inst=nego_sucu_inst.getSUCU();
        DIST dist_inst=sucu_inst.getDist_ForJSON();
        PROV prov_inst=dist_inst.getPROV_ForJSON();
        DEPA depa_inst=prov_inst.getDEPA_ForJSON();
        
        Double valor_soles=this.totales[4];
        Boolean is_afecto_detraccion=false;
        if (this.nf.getNego().getCO_MONE_FACT_ForJSON().equals(2)){// 1 soles, 2 dolares
            valor_soles*=this.nf.getCier().getNU_TIPO_CAMB_ForJSON();
        }
        if (valor_soles>700){
           is_afecto_detraccion=true;
        }
        
        FACT fact=new FACT(); 
        fact.setCO_CIER(this.nf.getCier().getCO_CIER_ForJSON());
        fact.setCO_CLIE(this.nf.getClie().getCO_CLIE_ForJSON());
        fact.setCO_DIST_CORR(this.nf.getDist_corr().getCO_DIST_ForJSON());
        fact.setCO_DIST_FISC(this.nf.getDist_fiscal().getCO_DIST_ForJSON());
        fact.setCO_DIST_INST(dist_inst.getCO_DIST_ForJSON());
        fact.setCO_MONE_FACT(this.nf.getNego().getCO_MONE_FACT_ForJSON());
        fact.setCO_NEGO(this.nf.getNego().getCO_NEGO_ForJSON());
        fact.setCO_FACT(this.Numero);
        fact.setCO_TIPO_DOCU(this.nf.getClie().getCO_TIPO_DOCU_ForJSON());
        fact.setDE_CODI_BUM(this.nf.getClie().getDE_CODI_BUM_ForJSON()+"-"+this.nf.getClie().getDE_DIGI_BUM_ForJSON()); // Solicitado 25-01-2016
        fact.setDE_DIRE_CORR(this.nf.getSucu_corr().getDE_DIRE_ForJSON());
        fact.setDE_REF_DIRE_CORR(this.nf.getSucu_corr().getDE_REF_DIRE_ForJSON());
        fact.setDE_DIRE_FISC(this.nf.getSucu_fiscal().getDE_DIRE_ForJSON());
        fact.setDE_DIRE_INST(sucu_inst.getDE_DIRE_ForJSON());
        fact.setDE_NUME_DOCU(this.nf.getClie().getDE_NUME_DOCU_ForJSON());
        
        fact.setDE_SIMB_MONE_FACT(this.nf.getMone_fact().getDE_SIMB_ForJSON());
        
        fact.setIM_VALO_VENT(this.totales[0]);
        fact.setIM_VALO_DESC(this.totales[1]);
        fact.setIM_VALO_VENT_NETO(this.totales[2]);
        fact.setIM_IGV(this.totales[3]);
        fact.setIM_TOTA(this.totales[4]);
        fact.setIM_AJUS_NIGV(this.totales[5]);
        fact.setST_AFECTO_DETRACCION(is_afecto_detraccion);
        
        fact.setIM_INST(this.totales[6]);
        fact.setIM_ALQU(this.totales[7]);
        fact.setIM_OTRO(this.totales[8]);
        
        fact.setNO_CLIE(this.nf.getClie().getFullNameCliente());
        fact.setNO_DIST_CORR(this.nf.getDist_corr().getNO_DIST_ForJSON());
        fact.setNO_DIST_FISC(this.nf.getDist_fiscal().getNO_DIST_ForJSON());
        fact.setNO_DIST_INST(dist_inst.getNO_DIST_ForJSON());
        fact.setNO_MONE_FACT(this.nf.getMone_fact().getNO_MONE_FACT_ForJSON());
        fact.setNO_TIPO_DOCU(this.nf.getClie().getTipoDocumento().getNO_TIPO_DOCU_ForJSON());
        
        fact.setST_ANUL(Boolean.FALSE);
        fact.setST_ELIM(Boolean.FALSE);
        
        fact.setFE_EMIS(this.nf.getCier().getFE_EMIS_ForJSON());
        fact.setFE_VENC(this.nf.getCier().getFE_VENC_ForJSON());
        
        fact.setDE_MONT("");
        fact.save();
    }
}
