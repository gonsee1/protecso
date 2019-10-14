/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.fncs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCreationHelper;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;

/**
 *
 * @author crodas
 */
public class ExcelWriter {
    HSSFWorkbook workbook;
    HSSFSheet sheet;  
    Integer num_row;
    CellStyle cellStyleFormaDate;
    CellStyle cellStyleFormaDateTime;
    HSSFCreationHelper createHelper;
    public ExcelWriter(){
        workbook = new HSSFWorkbook();
        createHelper = workbook.getCreationHelper();
        cellStyleFormaDate=workbook.createCellStyle();
        cellStyleFormaDateTime=workbook.createCellStyle();
        cellStyleFormaDate.setDataFormat(createHelper.createDataFormat().getFormat("m/d/yy"));
        cellStyleFormaDateTime.setDataFormat(createHelper.createDataFormat().getFormat("m/d/yy h:mm"));
    }
    public void addHoja(String name){
       sheet = workbook.createSheet(name);   
       num_row=0;
    }
    public void save(HttpServletResponse response){
        try {
            workbook.write(response.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(ExcelWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
        //workbook.close();
    }
    public void writeCols(List<Object> cols){
        HSSFRow row=this.sheet.createRow(num_row);
        int ncol=0;
        for(Object col : cols){
            HSSFCell cell=row.createCell(ncol);
            if (col instanceof String)
                cell.setCellValue((String)col);
            else if (col instanceof Integer)
                cell.setCellValue((Integer)col);
            else if (col instanceof Long)
                cell.setCellValue((Long)col);
            else if (col instanceof Double)
                cell.setCellValue((Double)col);
            else if (col instanceof Date){
                cell.setCellValue((Date)col);
                cell.setCellStyle(cellStyleFormaDate);
            }else if (col instanceof Boolean)
                cell.setCellValue((Boolean)col);
            ncol++;
        }
        
        this.num_row++;
    }
    public void writeCols(Object[] cols){
        List<Object> l=new ArrayList<Object>();
        for(Object o : cols)
            l.add(o);
        this.writeCols(l);
    }
    
}
