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
import com.americatel.facturacion.models.CIER_DATA_PLAN;
import com.americatel.facturacion.models.CIER_DATA_SERV_SUPL;
import com.americatel.facturacion.models.CIER_DATA_SERV_UNIC;
import com.americatel.facturacion.models.CIER_DATA_SUCU;
import com.americatel.facturacion.models.CLIE;
import com.americatel.facturacion.models.DEPA;
import com.americatel.facturacion.models.DIST;
import com.americatel.facturacion.models.NEGO;
import com.americatel.facturacion.models.PROD_PADRE;
import com.americatel.facturacion.models.PROV;
import com.americatel.facturacion.models.SUCU;

/**
 *
 * @author crodas
 */
public class FacturaPdf  extends LibPdf{
    Factura factura = null;
    ProcesoNegocio proceso_negocio;
    protected List<PDPageContentStream> coss=new ArrayList<PDPageContentStream>();
    private float Font_Size_General=11;// min 7 por que existe un Font_Size_General-6
    private Double redondearPorDefecto(Double n){return com.americatel.facturacion.fncs.Numero.redondear(n,2);}
    FacturaPdf(Factura factura) {
        this.factura=factura;
        this.proceso_negocio=factura.getProceso_negocio();
    }
   
    void crearPdf() {
        this.addPage();
        this.addDetalle(650);
        this.addCuadroFooter(150);
        this.cerrarCoss();
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
    
    private void addPageUno(){
        float y=850;
        this.addHeader(y);
    }    
    
    public void savePdf(OutputStream out){
        this.cerrarCoss();
        super.save(out);
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
    
    private void addHeader(float y) {
        CIER cier=this.factura.cier;
        CLIE clie=this.factura.cliente;
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
        this.drawStringChico( sucu.getDE_DIRE_ForJSON()+", "+dist.getNO_DIST_ForJSON()+" - "+prov.getNO_PROV_ForJSON()+" - "+depa.getNO_DEPA_ForJSON()  ,120, ty);
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
        Double monto_cobrar=0d,devolucion_por_promocion=0d, devolucion_cargos_fijos=0d;
        PROD_PADRE pp = ProcesoNegocio.mapProd.getProductoByNegocio(this.factura.nego.getCO_PROD_ForJSON());
        boolean validacion = (pp != null && pp.getCO_PROD_PADRE_ForJSON() == Constante.INT_COD_PROD_PADRE_TI);
        if(validacion){
        	
        	
            for(Map.Entry<CIER_DATA_SUCU,Object[]> entry : this.factura.getPlanes().entrySet()){
                CIER_DATA_SUCU sucu_data=entry.getKey();
                Integer co_nego_sucu=sucu_data.getCO_NEGO_SUCU();
                
                if (co_nego_sucu == 2057)
                {
                	
                	System.out.println("aaaaa");
                }
                
                List<CIER_DATA_PLAN> planes=(List<CIER_DATA_PLAN>) entry.getValue()[0];                
                for (CIER_DATA_PLAN plan : planes){
                    if (plan.getST_TIPO_DEVO()==null){
                        String nombre=plan.getDE_NOMB();
                        Double m=plan.getIM_MONT();
                        if (plan.getST_TIPO_COBR()==2){
                            nombre=ReciboPdf.GLOSA_DIFERENCIAL_CARGOS_FIJOS;
//                            if (!plan.getCO_MONE_FACT().equals(this.factura.fact_modificado.getCO_MONE_FACT_ForJSON())){
//                                if (Constante.SOLES == this.factura.fact_modificado.getCO_MONE_FACT_ForJSON()){
                            if (!plan.getCO_MONE_FACT().equals(this.factura.moneda_facturacion.getCO_MONE_FACT_ForJSON())){
                                if (Constante.SOLES == this.factura.moneda_facturacion.getCO_MONE_FACT_ForJSON()){
                            		m*=this.factura.cier.getNU_TIPO_CAMB_ForJSON();
                                }else{
                                    m/=this.factura.cier.getNU_TIPO_CAMB_ForJSON();
                                }
                            }

                        }
                        
                        
                        
                        if (!resumen_planes.containsKey(nombre)){ //vALIDAR QUE NO PERTENEZCA AL MISMO PERIODO
                            resumen_planes.put(nombre, new Object[]{nombre,1,m,co_nego_sucu,plan.getPeriodo()});
                        }else{
                            Object data[]=resumen_planes.get(nombre);
                            data[1]=(Integer)data[1]+1;
                            data[2]=(Double)data[2]+m;
                        }
                        
                        monto_cobrar+=m; 
                        
                    }else if (plan.getST_TIPO_DEVO()==4){
                        devolucion_por_promocion+=Math.abs(plan.getIM_MONT())*-1;                        
                    }
                }
                
            }
        }
        for(Map.Entry<CIER_DATA_SUCU,List<CIER_DATA_SERV_SUPL>> entry : this.factura.servicios_suplementarios.entrySet()){
            CIER_DATA_SUCU sucu_data=entry.getKey();
            Integer co_nego_sucu=sucu_data.getCO_NEGO_SUCU();
            for (CIER_DATA_SERV_SUPL ss : entry.getValue()){
                if (ss.getST_TIPO_DEVO()==null){    
                    String nombre=ss.getDE_NOMB();
                    Double m=ss.getIM_MONT();
                     if (!resumen_sss.containsKey(nombre)){
                        resumen_sss.put(nombre, new Object[]{nombre,1,m,co_nego_sucu,ss.getPeriodo()});
                    }else{
                        Object data[]=resumen_sss.get(nombre);
                        data[1]=(Integer)data[1]+1;
                        data[2]=(Double)data[2]+m;
                    }
                    
//                    if (!resumen_sss.containsKey(ss.getDE_NOMB())){
//                        resumen_sss.put(ss.getDE_NOMB(), new Object[]{ss.getDE_NOMB(),0,0d,0});
//                    }
//                    Object data[]=resumen_sss.get(ss.getDE_NOMB());
//                    if((Integer)data[3]==0 || (Integer)data[3]!=co_nego_sucu){
//                        data[1]=(Integer)data[1]+1;
//                    }
//                    data[2]=(Double)data[2]+ss.getIM_MONT(); 
//                    data[3]=sucu_data.getCO_NEGO_SUCU();
                    monto_cobrar+=ss.getIM_MONT();
                }else{
                    if (ss.getST_TIPO_DEVO()==4){
                        devolucion_por_promocion+=Math.abs(ss.getIM_MONT())*-1;                        
                    }
                }
            }             
        }
        for(Map.Entry<CIER_DATA_SUCU,List<CIER_DATA_SERV_UNIC>> entry : this.factura.servicios_unicos_data.entrySet() ){
            for (CIER_DATA_SERV_UNIC su : entry.getValue()){
                monto_cobrar+=su.getIM_MONT();
            } 
        }
        
        float x_a=40;
        float x_b=300;
        float x_c=450;
        float x_d=500;
        float alto=10;
        
        
        if(validacion){
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
        for(Map.Entry<CIER_DATA_SUCU,List<CIER_DATA_SERV_UNIC>> entry : this.factura.servicios_unicos_data.entrySet() ){
            for (CIER_DATA_SERV_UNIC data : entry.getValue())
            {
                this.drawStringChico( data.getDE_NOMB() , x_a, y);
                Double m=redondearPorDefecto(data.getIM_MONT());
                //this.drawStringPequenio( "("+data.getValue().getPeriodoAnidadoFormateado()+")" , x_b, y);
                this.drawStringChico( this.getStringDecimalFormat(m,2), x_d, y);

                y-=alto;
            } 
        }
        
        /*Validamos la devolucion por promociones y por cargos fijos que no exeda lo que se cobra*/
        if (Math.abs(factura.saldo_aplica)>Math.abs(devolucion_por_promocion)){
            devolucion_cargos_fijos=(factura.saldo_aplica+devolucion_por_promocion)*-1;
        }
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
        if (cantidad_ajustes_afectos>0 || (devolucion_por_promocion!=0 || devolucion_cargos_fijos!=0 )){
            
            y-=alto;
            for(AJUS ajus : this.factura.mi_ajustes){
                if (ajus.getST_AFEC_IGV_ForJSON()){ 
                    this.drawStringChico( ajus.getDE_GLOS_ForJSON() , x_a, y);
                    this.drawStringChico( this.getStringDecimalFormat(Math.abs(ajus.getIM_MONT_ForJSON())*-1d,2), x_d, y);
                    y-=alto;
                }
            }
            if (devolucion_por_promocion!=0){                
                this.drawStringChico( Constante.GLOSA_DEVOLUCION_PROMOCION , x_a, y);
                this.drawStringChico( this.getStringDecimalFormat(devolucion_por_promocion,2), x_d, y);
                y-=alto;
            }
            if (devolucion_cargos_fijos!=0){                
                this.drawStringChico( Constante.GLOSA_DEVOLUCION_CARGOS_FIJOS , x_a, y);
                this.drawStringChico( this.getStringDecimalFormat(devolucion_cargos_fijos,2), x_d, y);
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
        
//        this.drawRectangle(posHeaderA1,y , tamanioColumnaA, alturaCeldaTipoA,2);
        
        this.drawRectangle(posHeaderB1,y , tamanioColumnaB, alturaCeldaTipoB,2);
        this.drawRectangle(posHeaderC1,y , tamanioColumnaC, alturaCeldaTipoB,2);
        this.drawRectangle(posHeaderD1,y , tamanioColumnaD, alturaCeldaTipoB,2);
        
        for (int i = 0; i < 8; i++) {
            y-=alturaCeldaTipoB;
        
            this.drawRectangle(posHeaderB1,y , tamanioColumnaB, alturaCeldaTipoB,2);
            this.drawRectangle(posHeaderC1,y , tamanioColumnaC, alturaCeldaTipoB,2);
            this.drawRectangle(posHeaderD1,y , tamanioColumnaD, alturaCeldaTipoB,2);
        }
        
//        this.drawRectangle(posHeaderA1,y , tamanioColumnaA, alturaCeldaTipoC,2);
        
        /**/
        
        float posInfoX_A1 = posInicialInfo;        
        float posInfoX_B2 = posInfoX_A1 + tamanioColumnaA;
        float posInfoX_C3 = posInfoX_B2 + tamanioColumnaB;
        float posInfoX_D4 = posInfoX_C3 + tamanioColumnaC;
        
        
        Integer CO_MONE_FACT = this.factura.moneda_facturacion.getCO_MONE_FACT_ForJSON();
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


 
}
