/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.fncs;

import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author crodas
 */
public class ExcelReader {
    private static final String []ABCDARIO="ABCDEFGHIJKLMNOPQRSTUVWXYZ".split("");//crea uno vacio adelante
    private XSSFWorkbook workbook;
    private XSSFSheet worksheet;
    private String nameSheet;
    
    public ExcelReader(MultipartFile file){
        try {
            this.workbook = new XSSFWorkbook(file.getInputStream());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            Logger.getLogger(ExcelReader.class.getName()).log(Level.SEVERE, null, ex);
            this.workbook=null;
        }
    }
    
    public boolean selectSheet(String name){
        this.nameSheet=name;
        this.worksheet=workbook.getSheet(this.nameSheet);
        return this.worksheet!=null;
    }
    
    public boolean selectSheet(int index){
        this.worksheet=workbook.getSheetAt(index);
        this.nameSheet=this.worksheet.getSheetName();
        return this.worksheet!=null;
    }    
    
    public int getNumRows(){
//        return this.worksheet.getPhysicalNumberOfRows();
        return this.worksheet.getLastRowNum();
    }
    
    public int getNumsCols(int rowPos){
        XSSFRow row=worksheet.getRow(rowPos);
        if (row!=null){
            return row.getLastCellNum();
        }
        return -1;
    }
    public XSSFCell getCell(int colPos,int rowPos){
        XSSFRow row=worksheet.getRow(rowPos);
        Logger.getLogger(ExcelReader.class.getName()).log(Level.SEVERE,"colPos: "+colPos +"/rowPos: "+rowPos);
        if (row!=null){
            Logger.getLogger(ExcelReader.class.getName()).log(Level.SEVERE,"row != null");
            XSSFCell col=row.getCell(colPos);
            if (col!=null)
                Logger.getLogger(ExcelReader.class.getName()).log(Level.SEVERE,"col != null");
                return col;
        }
        return null;
    }
    public XSSFCell getCell(String letras,int numero){
        int valorLetra=this.getValueLetras(letras);
        return this.getCell(valorLetra, numero-1);        
    }
    
    public String getValue(int colPos,int rowPos){//inicia en cero,cero
        XSSFCell col=this.getCell(colPos,rowPos);
        System.out.println("*******]51");
        if (col!=null){
            System.out.println("*******]52");
            Boolean date_formater=false;
            try {
                date_formater=DateUtil.isCellDateFormatted(col);
            } catch (Exception e) {
                System.out.println("Error: "+e.getMessage());
                Logger.getLogger(ExcelReader.class.getName()).log(Level.SEVERE, null, e.getMessage());
            }
            System.out.println("*******]53");
            if (!date_formater){
                System.out.println("*******]54");
                switch (col.getCellType()) {                    
                    case XSSFCell.CELL_TYPE_NUMERIC:
                        System.out.println("*******]55");
                        return ""+col.getNumericCellValue();
                    case XSSFCell.CELL_TYPE_STRING:
                        System.out.println("*******]56");
                        return col.getStringCellValue();
                    default:
                        System.out.println("*******]57");
                        return col.getRawValue();
                }
            }else{
                System.out.println("*******]58");
                Date d=col.getDateCellValue();
                System.out.println("*******]d: "+d);
                if (d!=null){
                    System.out.println("*******]59");
                    return ""+d.getTime();
                }
            }
        }
        System.out.println("*******]510");
        return null;
    }
    
    private int getValueLetraInABCDARIO(String L){
        if (L.length()==1){
            for(int i=0;i<ABCDARIO.length;i++){
//           if(ABCDARIO[i+1].equals(L)) //pruebas
                if(ABCDARIO[i].equals(L)) //jdk7
                    return i;
            }
        }
        return -1;
    }
    public String getValue(String key){//key = A8 AZ5 B15 C45 o 8A 5AZ 15B 45C
        String ret=null;
        Pattern p = Pattern.compile("[0-9]"); 
        Matcher m = p.matcher(key);
        int inicio=-1;
        boolean encontro=false;
        boolean primerioNumero=false;
        if (m.find()) {
           inicio= m.start();
           encontro=true;
           if (inicio==0){                       
                encontro=false;
                Pattern p2 = Pattern.compile("[A-Z]"); 
                Matcher m2 = p.matcher(key);
                if (m2.find()){
                    primerioNumero=false;
                    encontro=true;
                    inicio=m2.start();                    
                }
           }
        } 
        if (encontro){
            int row=0;
            String letras="";
            if(primerioNumero){
                row=Integer.parseInt(key.substring(0,inicio));
                letras=key.substring(inicio);
            }else{
                row=Integer.parseInt(key.substring(inicio));
                letras=key.substring(0,inicio); 
            }
            ret=this.getValue(letras, row);
        }
        return ret;
    }
    public int getValueLetras(String letras){
        int valorLetra=0;
        int value;
        String arr[]=letras.split("");
        System.out.println("*******]5");
        System.out.println(">>>1>"+letras);
        System.out.println(">>>2>"+arr.length);
        System.out.println(">>>ABCDARIO>"+ABCDARIO.length);
        for(int i=0;i<arr.length;i++){
            //Comemtar condicional si produccion jdk 7
//            if (i>0){//posicion 0 es vacia no se cuenta
                System.out.println("*******]7");
                value=this.getValueLetraInABCDARIO(arr[i])+1;
                System.out.println(">>>3>"+value);
                if (value>0){
                    System.out.println("*******]8");
                    valorLetra+=value*((int)Math.pow(ABCDARIO.length,arr.length-1-i)); //jdk 7
//                    valorLetra+=value*((int)Math.pow(ABCDARIO.length-1,arr.length-1-i));
                }        
//            }
        }        
        if (arr.length>0){ //jdk 7 produccion
//        if (arr.length>1){
            valorLetra--;
        }
        System.out.println("*******]6");
        System.out.println("valorLetra: "+valorLetra);
        return valorLetra;
    }
    
    public String getValue(String letras,int numero){
        System.out.println("*******]3");
        System.out.println(">>>letras:"+letras);
        System.out.println(">>>numero:"+numero);
        System.out.println("*******]3");
        int valorLetra=this.getValueLetras(letras);
        System.out.println("*******]4");
        return this.getValue(valorLetra, numero-1);
    }
    
    public boolean isDateCell(String letras,int numero){
        XSSFCell col=this.getCell(letras, numero);
        if (col!=null){
            return DateUtil.isCellDateFormatted(col);
        }
        return false;
    }
    public boolean isNumberCell(String letras,int numero){
        XSSFCell col=this.getCell(letras, numero);
        if (col!=null){
            return col.getCellType()==XSSFCell.CELL_TYPE_NUMERIC;
        }
        return false;
    }    
    public boolean isStringCell(String letras,int numero){
        XSSFCell col=this.getCell(letras, numero);
        if (col!=null){
            return col.getCellType()==XSSFCell.CELL_TYPE_STRING;
        }
        return false;
    }    
    public Date getValueDate(String letras,int numero){
        System.out.println("*******]12");
        System.out.println(letras);
        System.out.println(numero);
        if (this.isDateCell(letras, numero)){
            String value=this.getValue(letras, numero);
            if (value!=null)
                return new Date(Long.parseLong(value));
        }
        return null;
    }
    public Long getValueLong(String letras,int numero){
        if (this.isNumberCell(letras, numero)){
            String value=this.getValue(letras, numero);
            if (value!=null)            
            return (long)Double.parseDouble(value);
        }
        return null;
    }
    public Double getValueDouble(String letras,int numero){
        if (this.isNumberCell(letras, numero)){
            String value=this.getValue(letras, numero);
            if (value!=null)            
            return Double.parseDouble(value);
        }
        return null;
    }   
    public Integer getValueInteger(String letras,int numero){
        System.out.println("*******]1");
        System.out.println(letras);
        System.out.println(numero);
        if (this.isNumberCell(letras, numero)){
            System.out.println("*******]2");
            String value=this.getValue(letras, numero);
            System.out.println(value);
            if (value!=null)            
                return (int)Double.parseDouble(value);
        }else{
            System.out.println("*******]99");
        }
        return null;
    }    
}