/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.fncs;

import com.americatel.facturacion.mappers.mapCURSOR;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.imageio.ImageIO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 *
 * @author crodas
 */
@Component
public class GraficaNegocio {
    static String NOMBRES_MESES[]={"ENE","FEB","MAR","ABR","MAY","JUN","JUL","AGO","SEP","OCT","NOV","DIC"};
    static mapCURSOR mapCursor;    
    static int MARGIN=10;
    static int ANCHO_MES=200;
    static int ALTO_MES=4;
    static int ALTO_BANDA=12;
    static int ALTO_BANDA_DEV=6;
    static int ESPACIO_ENTRE_BANDA=10;
    
    static Color COLOR_BANDA_PLAN=new Color(181,230,29);
    static Color COLOR_BANDA_PLAN_DEV=new Color(237,28,36);
    static Color COLOR_BANDA_PLAN_TEXTO=Color.BLACK;
    
    Font font_normal=new Font("ARIAL", Font.PLAIN, 12);
    Font font_banda=new Font("ARIAL", Font.BOLD, 10);
    
    
    int CO_NEGO=0;  
    int WIDTH=800,HEIGHT=300;
    
    int periodo_minimo=0;
    int ano_minimo=0;
    Date fecha_minima=new Date();    
    int periodo_maxima=0;
    int ano_maxima=0;
    Date fecha_maxima=new Date();
    int cantidad_meses=0;
    
    BufferedImage buffImage=null;
    Graphics2D g2d = null;

    Map<String,Object[]> planes=new HashMap<String,Object[]>();//Object[] 0> cobros,1 devoluciones
    
    @Autowired
    public void setmapCURSOR(mapCURSOR mapCursor){        
        GraficaNegocio.mapCursor=mapCursor;
    }
    public GraficaNegocio(){}
    public GraficaNegocio(int CO_NEGO) {
        this.CO_NEGO=CO_NEGO;
        this.readPlanes();
        this.calcularConfiguraciones();        
        
        
        buffImage=new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_ARGB_PRE);
        g2d=buffImage.createGraphics();
        g2d.setColor(Color.white);
        g2d.fillRect(0, 0, WIDTH, HEIGHT);   
        g2d.setFont(font_normal);
        this.drawPlano(); 
        this.drawPlanes();
    }
    private void calcularConfiguraciones(){
        periodo_minimo=FechaHora.getMonth(fecha_minima);
        ano_minimo=FechaHora.getYear(fecha_minima);
        periodo_maxima=FechaHora.getMonth(fecha_maxima);
        ano_maxima=FechaHora.getYear(fecha_maxima);
        cantidad_meses=this.getCantidadMeses(ano_minimo,periodo_minimo,ano_maxima,periodo_maxima);
        
        WIDTH=(cantidad_meses+2)*ANCHO_MES;
    }
    private void drawPlano(){
        int YC=HEIGHT-2*MARGIN;
        g2d.setColor(Color.BLACK);
        g2d.drawLine( MARGIN, YC, WIDTH-2*MARGIN, YC ); 
        int ano=ano_minimo,periodo=periodo_minimo;
        for(int valor=MARGIN;valor<WIDTH;valor+=ANCHO_MES){
            g2d.drawLine( valor, YC-ALTO_MES/2, valor, YC+ALTO_MES/2);
            g2d.drawString(NOMBRES_MESES[periodo-1]+" "+(ano-2000), valor - 12 , YC+ALTO_MES*3);
            periodo++;
            if (periodo==13){
                periodo=1;
                ano++;
            }            
        }
    }
    private void readPlanes(){
        //Cobros
        String sql="SELECT CO_NEGO_SUCU_PLAN,FE_INIC,FE_FIN,IM_MONT,DE_NOMB FROM [dbo].[TMCIER_DATA_PLAN] WHERE CO_NEGO_SUCU_PLAN IN " +
            "(" +
            "SELECT nsp.CO_NEGO_SUCU_PLAN  FROM [dbo].[TMNEGO] n " +
            "inner join [dbo].[TMNEGO_SUCU] ns ON ns.CO_NEGO=n.CO_NEGO " +
            "inner join [dbo].[TMNEGO_SUCU_PLAN] nsp ON  nsp.CO_NEGO_SUCU=ns.CO_NEGO_SUCU and nsp.ST_ELIM=0 " +
            "WHERE n.CO_NEGO="+this.CO_NEGO+"" +
            ") and ST_TIPO_COBR is not null ORDER BY CO_NEGO_SUCU_PLAN,FE_INIC ASC;";
        List<Map<String,Object>> lista=mapCursor.select(sql);
        
        for(Map<String,Object> reg : lista){
            String key=reg.get("CO_NEGO_SUCU_PLAN")+"_"+reg.get("DE_NOMB");
            if (!planes.containsKey(key))                
                planes.put(key, new Object[]{new ArrayList<Object>(),new ArrayList<Object>()});
            List<Object> cobros=(ArrayList<Object>)planes.get(key)[0];
            cobros.add(reg);
            //planes.get(key).add(reg);            
            Date FE_INIC=FechaHora.getDateFromString((String)reg.get("FE_INIC"), "yyyy-MM-d");
            if (fecha_minima.getTime()>FE_INIC.getTime()){
                fecha_minima=FE_INIC;
            }
        }
        //Devoluciones
        sql="SELECT CO_NEGO_SUCU_PLAN,FE_INIC,FE_FIN,IM_MONT,DE_NOMB FROM [dbo].[TMCIER_DATA_PLAN] WHERE CO_NEGO_SUCU_PLAN IN " +
            "(" +
            "SELECT nsp.CO_NEGO_SUCU_PLAN  FROM [dbo].[TMNEGO] n " +
            "inner join [dbo].[TMNEGO_SUCU] ns ON ns.CO_NEGO=n.CO_NEGO " +
            "inner join [dbo].[TMNEGO_SUCU_PLAN] nsp ON  nsp.CO_NEGO_SUCU=ns.CO_NEGO_SUCU AND nsp.ST_ELIM=0 " +
            "WHERE n.CO_NEGO="+this.CO_NEGO+"" +
            ") and ST_TIPO_DEVO is not null ORDER BY CO_NEGO_SUCU_PLAN,FE_INIC ASC;";
        lista=mapCursor.select(sql);  
        for(Map<String,Object> reg : lista){
            String key=reg.get("CO_NEGO_SUCU_PLAN")+"_"+reg.get("DE_NOMB");
            if (!planes.containsKey(key))                
                planes.put(key, new Object[]{new ArrayList<Object>(),new ArrayList<Object>()});
            List<Object> cobros=(ArrayList<Object>)planes.get(key)[1];
            cobros.add(reg);
            //planes.get(key).add(reg);            
            Date FE_INIC=FechaHora.getDateFromString((String)reg.get("FE_INIC"), "yyyy-MM-d");
            if (fecha_minima.getTime()>FE_INIC.getTime()){
                fecha_minima=FE_INIC;
            }
        }        
    }
    
    private void drawPlanes(){
        int alto=HEIGHT-MARGIN*2;
        Color color_antes=g2d.getColor();
        g2d.setFont(font_banda);
        
        for(Entry<String,Object[]> plan :  planes.entrySet()){
            alto-=(ALTO_BANDA+ESPACIO_ENTRE_BANDA);
            for(Object regObj : (List<Object>)plan.getValue()[0]){
                Map<String,Object> regList=(Map<String,Object>) regObj;
                Date FE_INIC=FechaHora.getDateFromString((String)regList.get("FE_INIC"), "yyyy-MM-d");
                Date FE_FIN=FechaHora.getDateFromString((String)regList.get("FE_FIN"), "yyyy-MM-d");
                int x_ini=this.getPosXfromDate(FE_INIC);
                int x_fin=this.getPosXfromDate(FE_FIN);             
                g2d.setColor(COLOR_BANDA_PLAN);
                g2d.fillRect(x_ini, alto, x_fin-x_ini, ALTO_BANDA);
                BigDecimal  monto=(BigDecimal )regList.get("IM_MONT");
                g2d.setColor(COLOR_BANDA_PLAN_TEXTO);
                g2d.drawString(FechaHora.getDay(FE_INIC)+" a "+FechaHora.getDay(FE_FIN)+ " ("+monto.toString()+")", x_ini+1, alto+ALTO_BANDA-2);
                
            }
            

            for(Object regObj : (List<Object>)plan.getValue()[1]){
                Map<String,Object> regList=(Map<String,Object>) regObj;
                Date FE_INIC=FechaHora.getDateFromString((String)regList.get("FE_INIC"), "yyyy-MM-d");
                Date FE_FIN=FechaHora.getDateFromString((String)regList.get("FE_FIN"), "yyyy-MM-d");
                int x_ini=this.getPosXfromDate(FE_INIC);
                int x_fin=this.getPosXfromDate(FE_FIN);             
                g2d.setColor(COLOR_BANDA_PLAN_DEV);
                g2d.fillRect(x_ini, alto+ALTO_BANDA_DEV+4, x_fin-x_ini, ALTO_BANDA_DEV);
                //BigDecimal  monto=(BigDecimal )regList.get("IM_MONT");
                //g2d.setColor(COLOR_BANDA_PLAN_TEXTO);
                //g2d.drawString(FechaHora.getDay(FE_INIC)+" a "+FechaHora.getDay(FE_FIN)+ " ("+monto.toString()+")", x_ini+1, alto+ALTO_BANDA-2);
                
            }            
        }
        g2d.setColor(color_antes);
        g2d.setFont(font_normal);
    }   
    
    private int getPosXfromDate(Date fecha){        
        if (this.fecha_minima.getTime()>fecha.getTime())   return -200;
        if (this.fecha_maxima.getTime()<fecha.getTime())   return WIDTH+200;
        int meses_despues=this.getCantidadMeses(this.ano_minimo,this.periodo_minimo,FechaHora.getYear(fecha),FechaHora.getMonth(fecha));
        int num_d=FechaHora.getNumDaysOfMonth(fecha);
        return ANCHO_MES*meses_despues+(int)((float)ANCHO_MES/(float)num_d*(float)FechaHora.getDay(fecha))+MARGIN;
    }
    
    private int getCantidadMeses(int ano_inicio,int periodo_inicio,int ano_fin,int periodo_fin){
        int ret=0;
        while ((ano_inicio*100+periodo_inicio)<=(ano_fin*100+periodo_fin)){
            ret++;
            periodo_inicio++;
            if (periodo_inicio==13){
                ano_inicio++;
                periodo_inicio=1;
            }
        }        
        return ret;
    }
    public ResponseEntity getResponseEntity(){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "image/png");        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] bytes = null;//FileUtils.getBytes(imageFile);
        try {
            ImageIO.write( buffImage, "png", baos );
            baos.flush();
            bytes=baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity (bytes,headers,HttpStatus.NOT_FOUND);        
    }
}
