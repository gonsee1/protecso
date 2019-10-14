/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.core2;

import com.americatel.facturacion.cores.EReciboTextAlign;
import java.awt.Color;
import java.io.OutputStream;
import java.util.Locale;
import javax.servlet.http.HttpServletResponse;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

/**
 *
 * @author crodas
 */
public class LibPdf {
    protected PDDocument document = null;
    protected PDPage page=null;
    protected PDRectangle rect=null;//tamanio
    protected PDPageContentStream cos=null;
    
    protected PDFont fontPlain = PDType1Font.HELVETICA;
    protected PDFont fontBold = PDType1Font.HELVETICA_BOLD;
    protected PDFont fontItalic = PDType1Font.HELVETICA_OBLIQUE;
    protected PDFont fontMono = PDType1Font.COURIER;
    
    protected String name="sin_nombre"; 
    
    protected int numero_paginas=0;
    
	
    public LibPdf(){
        this.document= new PDDocument(); 
    }
    
    public void setName(String name) {
        this.name = name;
    }    
    
    /*SAVE*/
    /*
    protected void closeContentStream(){
        try {
            if (this.cos!=null)
                this.cos.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }*/
    protected void createContentStream(){
        try {
            //this.closeContentStream();
            this.cos = new PDPageContentStream(this.document, this.page);
            this.cos.setFont(this.fontPlain, 10);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void save(OutputStream out){
        try {
            //this.closeContentStream();
            this.document.save(out);
            this.document.close();            
        } catch (Exception ex) {
            ex.printStackTrace();
        }        
    }
    public void save(HttpServletResponse response){
        try {
            //this.closeContentStream();
            response.setHeader("Content-Type", "application/pdf");
            response.addHeader("Content-Disposition", "attachment; filename=\""+this.name+".pdf\"" );
            this.save(response.getOutputStream());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }    
    
    /*DRAW*/
    

    protected float[] getSizeString(String cadena,PDFont font,float size){
        try {
            return new float[]{font.getStringWidth(cadena) / 1000 * size,font.getFontDescriptor().getFontBoundingBox().getHeight() / 1300 * size};
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new float[]{0F,0F};
    }
    
    protected void drawStringCenter(String cad,float xi,float yi,float w,float h,PDFont font,float size){
        float xf=0F,yf=0F;
        try {
           float sizeString[]=this.getSizeString(cad, font, size);
           float txtWidth =  sizeString[0];
           float txtHeight = sizeString[1];
           xf=w/2-txtWidth/2+xi;
           yf=yi+h/2-txtHeight/2;//+(h/2-txtHeight/2)
        } catch (Exception ex) {
            ex.printStackTrace();
        }
       
       this.drawString(cad, xf, yf, font, size);
    }
    
    protected void drawString(String cad,float x,float y,PDFont font,float size,EReciboTextAlign textalign){
        if (cad==null) cad="";
        try {
            //Encoding e = EncodingManager.INSTANCE.getEncoding(COSName.WIN_ANSI_ENCODING);
            //String toPDF = String.valueOf(Character.toChars(e.getCode(e.getNameFromCharacter(cad.toCharArray()))));
            
            this.cos.setNonStrokingColor(Color.BLACK);
            if (textalign==EReciboTextAlign.IZQUIERDA){                                
                this.cos.beginText();     
                this.cos.setFont(font, size);
                this.cos.moveTextPositionByAmount(x, y);
                this.cos.drawString(cad);
                this.cos.endText();
            }else{
                float fontSize = size; // Or whatever font size you want.
                float txtWidth = font.getStringWidth(cad) / 1000 * fontSize;
                //float titleHeight = font.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * fontSize;
                this.cos.beginText();
                this.cos.setFont(font, fontSize);
                this.cos.moveTextPositionByAmount(x-txtWidth, y);
                this.cos.drawString(cad);
                this.cos.endText();               
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    protected void drawString(String cad,float x,float y,PDFont font,float size){
        this.drawString(cad, x, y, font, size,EReciboTextAlign.IZQUIERDA);
    } 
    protected void drawString(String cad,float x,float y,float size){
        this.drawString(cad, x, y, this.fontPlain, size,EReciboTextAlign.IZQUIERDA);
    }    
    
    protected void drawStringGrande(String cad,float x,float y){
        this.drawString(cad, x, y,this.fontPlain,12);
    }    
    protected void drawString(String cad,float x,float y){
        this.drawString(cad, x, y,this.fontPlain,10);
    }
    protected void drawStringSize(String cad,float x,float y,float size){
        this.drawString(cad, x, y,this.fontPlain,size);
    }     
    protected void drawStringMediano(String cad,float x,float y){
        this.drawString(cad, x, y,this.fontPlain,8);
    }    
    protected void drawStringChico(String cad,float x,float y){
        this.drawString(cad, x, y,this.fontPlain,6);
    } 
    protected void drawStringPequenio(String cad,float x,float y){
        this.drawString(cad, x, y,this.fontPlain,4.5f);
    }    
    protected void drawStringMedianoAlignDerecha(String cad,float x,float y){
        this.drawString(cad, x, y,this.fontPlain,8,EReciboTextAlign.DERECHA);
    }
    protected void drawStringMedianoAlignDerecha(String cad,float x,float y,float size){
        this.drawString(cad, x, y,this.fontPlain,size,EReciboTextAlign.DERECHA);
    }    
    protected void drawRectangle(float x,float y,float w,float h,float b,Color borde,Color background){
        try {
            //apartir del punto Y este se dibujo hacia arriba h (altura)
            float bmedios=b/2F;          
            this.cos.setNonStrokingColor(borde);
            this.cos.fillRect(x, y, w, h);
            this.cos.setNonStrokingColor(background);
            this.cos.fillRect(x+bmedios, y+bmedios, w-b, h-b);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    protected void drawRectangle(float x,float y,float w,float h,float b){
        this.drawRectangle(x, y, w, h, b,Color.BLACK,Color.WHITE);
    }
    
    protected float drawStringLimitWidth(String cad,float x,float y,float max_w,float salto,PDFont font,float font_size){
        float size[]=this.getSizeString(cad, font, font_size);//width,height
        if (size[0]>0){
            if (max_w>size[0]){
                this.drawString(cad, x, y, font, font_size);                
            }else{
                int len=cad.length();
                int limite_caracteres=(int)((max_w*(float)len)/size[0])-1;
                if (limite_caracteres>1){
                    int res=len % limite_caracteres;
                    int grupos=(len-res)/limite_caracteres;                    
                    //if (res!=0) grupos++;
                    int i=0;
                    for(i=0;i<grupos;i++){                        
                        if (i!=0) y-=salto;
                        this.drawString(cad.substring(i*limite_caracteres, (i+1)*limite_caracteres), x, y, font, font_size);                        
                    }
                    if (res!=0){
                        i=grupos;
                        y-=salto;
                        this.drawString(cad.substring(i*limite_caracteres, len), x, y, font, font_size);                        
                    }
                }
            }
        }
        return y-salto;
    }
    
    protected  float drawStringLimitWidth(String cad,float x,float y,float max_w,float salto){
        return this.drawStringLimitWidth(cad, x, y, max_w, salto, this.fontPlain, 7);
    }
    protected  float drawStringLimitWidth(String cad,float x,float y,float max_w,float salto,float font_size){
        return this.drawStringLimitWidth(cad, x, y, max_w, salto, this.fontPlain, font_size);
    }   
    
    
    protected String getStringDecimalFormat(Double n,int decimales){
        return String.format( Locale.ENGLISH,"%."+decimales+"f", n );
    }    
}
