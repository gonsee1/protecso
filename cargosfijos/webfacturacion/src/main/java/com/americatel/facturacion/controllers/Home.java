/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.controllers;

import com.americatel.facturacion.core3.ProcesoNegocio;
import com.americatel.facturacion.cores.Recibo;
import com.americatel.facturacion.cores.ReciboPdf;
import com.americatel.facturacion.cores.LstRecibo;
import com.americatel.facturacion.cores.Main;
import com.americatel.facturacion.cores.NegocioFacturar;
import com.americatel.facturacion.fncs.FechaHora;
import com.americatel.facturacion.fncs.GraficaNegocio;
import com.americatel.facturacion.fncs.permissions;
import com.americatel.facturacion.mappers.mapCIER;
import com.americatel.facturacion.mappers.mapITEM_MODU;
import com.americatel.facturacion.mappers.mapMODU;
import com.americatel.facturacion.mappers.mapNEGO;
import com.americatel.facturacion.mappers.mapPERF;
import com.americatel.facturacion.mappers.mapPROD;
import com.americatel.facturacion.mappers.mapTIPO_FACT;
import com.americatel.facturacion.mappers.mapUSUA;
import com.americatel.facturacion.models.CIER;
import com.americatel.facturacion.models.ITEM_MODU;
import com.americatel.facturacion.models.MODU;
import com.americatel.facturacion.models.NEGO;
import com.americatel.facturacion.models.NEGO_SUCU;
import com.americatel.facturacion.models.PERF;
import com.americatel.facturacion.models.PROD;
import com.americatel.facturacion.models.TIPO_FACT;
import com.americatel.facturacion.models.USUA;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author crodas
 */
@Controller
public class Home {
    //@Autowired mapPROD p;
    //@Autowired mapTIPO_FACT t;
    @Autowired ApplicationContext appContext;   
    @Autowired mapPERF mapPerf;
    @Autowired mapPROD mapProd;
    @Autowired mapNEGO mapNego;
    @Autowired mapMODU mapModu;
    @Autowired mapCIER mapCier;
    @Autowired mapITEM_MODU mapItem_Modu;
    
    @RequestMapping(value="/",method = {RequestMethod.GET,RequestMethod.POST})
    public String home(ModelMap modelMap,HttpServletRequest request){
        if (permissions.isLogin(request)){
            permissions.initModelMap(request, modelMap);                  
            return "home";
        }else{
            USUA usu=permissions.login(request);
            if (usu!=null){ 
                return "redirect:.";
            }else{
                modelMap.addAttribute("usuario", request.getParameter("txt_usuario"));
                modelMap.addAttribute("clave", request.getParameter("txt_clave"));                
            }
        }
        return "login"; 
    }

    @RequestMapping(value="/logout",method = {RequestMethod.GET,RequestMethod.POST})
    public String logout(ModelMap modelMap,HttpServletRequest request){
        if (permissions.isLogin(request)){            
            permissions.close(request);
        }
        return "login";
    }
    
    @RequestMapping(value="/prueba",method = {RequestMethod.GET,RequestMethod.POST})
    public String prueba(){
        org.springframework.jdbc.datasource.DriverManagerDataSource a=(org.springframework.jdbc.datasource.DriverManagerDataSource) appContext.getBean("dataSource");
        //org.mybatis.spring.SqlSessionFactoryBean b=(org.mybatis.spring.SqlSessionFactoryBean ) appContext.getBean("sqlSessionFactory");
        Statement stmt = null;
        try {
            Connection cn=a.getConnection();             
            //PreparedStatement ps=cn.prepareStatement("SELECT 1+2");
            stmt = cn.createStatement(); 
            ResultSet res=stmt.executeQuery("SELECT @@SPID as SPID;");
            res.next();
            //cn.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            System.out.println("Error");
        }        
        return "prueba";
    }
    
    @RequestMapping(value="/fecha",method = {RequestMethod.GET,RequestMethod.POST})
    public String prueba(@RequestParam(value="date") @DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm:ss") Date dateTime){
        return "prueba";
    }

    @RequestMapping(value="/modeloNEGO_SUCU",method = {RequestMethod.GET,RequestMethod.POST})
    public String modeloNEGO_SUCU(@ModelAttribute NEGO_SUCU reg){
        return "prueba";
    }
        
    
    @RequestMapping(value="/pdf")//,method = {RequestMethod.GET,RequestMethod.POST},produces = "application/pdf",name = "ejemplo.pdf"
    public void getpdf(HttpServletResponse response){

        PDDocument document = new PDDocument();
//        PDPage page1 = new PDPage(PDPage.PAGE_SIZE_A4); version 1.8.8
        PDPage page1 = new PDPage(PDRectangle.A4);
            // PDPage.PAGE_SIZE_LETTER is also possible
        PDRectangle rect = page1.getMediaBox();
            // rect can be used to get the page width and height
        document.addPage(page1);

        // Create a new font object selecting one of the PDF base fonts
        PDFont fontPlain = PDType1Font.HELVETICA;
        PDFont fontBold = PDType1Font.HELVETICA_BOLD;
        PDFont fontItalic = PDType1Font.HELVETICA_OBLIQUE;
        PDFont fontMono = PDType1Font.COURIER;

        // Start a new content stream which will "hold" the to be created content
        PDPageContentStream cos = null;
        try {
            cos = new PDPageContentStream(document, page1);

            int line = 0;

            // Define a text content stream using the selected font, move the cursor and draw some text
            cos.beginText();
            cos.setFont(fontPlain, 12);
            cos.moveTextPositionByAmount(100, rect.getHeight() - 50*(++line));
            cos.drawString("Hello Worldssss");
            cos.endText();

            cos.beginText();
            cos.setFont(fontItalic, 12);
            cos.moveTextPositionByAmount(100, rect.getHeight() - 50*(++line));
            cos.drawString("Italic");
            cos.endText();

            cos.beginText();
            cos.setFont(fontBold, 12);
            cos.moveTextPositionByAmount(100, rect.getHeight() - 50*(++line));
            cos.drawString("Bold");
            cos.endText();

            cos.beginText();
            cos.setFont(fontMono, 12);
            cos.setNonStrokingColor(Color.BLUE);
            cos.moveTextPositionByAmount(100, rect.getHeight() - 50*(++line));
            cos.drawString("Monospaced blue");
            cos.endText();

            // Make sure that the content stream is closed:
            cos.close();

//            PDPage page2 = new PDPage(PDPage.PAGE_SIZE_A4);
            PDPage page2 = new PDPage(PDRectangle.A4);
            document.addPage(page2);
            cos = new PDPageContentStream(document, page2);

            // draw a red box in the lower left hand corner
            cos.setNonStrokingColor(Color.RED);
            cos.fillRect(10, 10, 100, 100);

            // add two lines of different widths
            cos.setLineWidth(1);
            cos.addLine(200, 250, 400, 250);
            cos.closeAndStroke();
            cos.setLineWidth(5);
            cos.addLine(200, 300, 400, 300);
            cos.closeAndStroke();

            cos.close();            
            
        } catch (IOException ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            response.setHeader("Content-Type", "application/pdf");
            response.addHeader("Content-Disposition", "attachment; filename=\"ejemplo.pdf\"" );
            document.save(response.getOutputStream());
            document.close();      
        } catch (Exception ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }
        //return output;
    }    
    
    @RequestMapping(value="/formato")
    public void formato(HttpServletRequest request,HttpServletResponse response){
        PROD prod=mapProd.getId(1);
        CIER cier=prod.getCierrePendiente();
        NEGO nego=mapNego.getId(1);
        NegocioFacturar nf=new NegocioFacturar(cier, prod, nego,request);
        nf.facturar(true);
        LstRecibo lst=nf.getLstRecibo(); 
        nf.save(response);
    }
    
    @RequestMapping(value="/test")
    public void test(HttpServletResponse response){
        ITEM_MODU i=mapItem_Modu.getId(1);
        MODU m=i.getModulo();//mapModu.getId(1);
    }    
    
    @RequestMapping(value="/units")
    public void units(HttpServletResponse response){
        com.americatel.facturacion.units.Main m=new com.americatel.facturacion.units.Main();
        m.run();
    } 
    
    @RequestMapping(value="/get_image")
    public ResponseEntity<byte[]> get_image(HttpServletResponse response){
        return new GraficaNegocio(22222).getResponseEntity();// 5075794
        
        /*HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "image/jpg");
        BufferedImage buff=new BufferedImage(800,600,BufferedImage.TYPE_INT_RGB);
        
        Graphics2D g2d = buff.createGraphics();
        g2d.setColor(Color.white);

        g2d.setColor(Color.yellow);
        g2d.drawString("Java Code Geeks", 50, 120);

        
        g2d.dispose();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] bytes = null;//FileUtils.getBytes(imageFile);
        try {
            ImageIO.write( buff, "jpg", baos );
            baos.flush();
            bytes=baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity (bytes,headers,HttpStatus.NOT_FOUND);*/
    }     
    @RequestMapping(value="/facturar")
    public void facturar(HttpServletResponse response,@RequestParam(value="CO_NEGO") int CO_NEGO,@RequestParam(value="saldo",required = false) Double saldo,@RequestParam(value="log_view",required = false) Boolean log_view){
          if (saldo==null) saldo=0d;
          if (log_view==null) log_view=false;
          NEGO nego=mapCier.getUnNegocioParaProcesoCierre(CO_NEGO);   
          PROD prod=nego.getPROD();
          CIER cier=prod.getCierrePendiente();
          ProcesoNegocio pn=new ProcesoNegocio(nego,prod,cier,saldo,true,false); 
          pn.setLog_view(log_view);
          pn.facturar(nego);
          pn.savePdf(response);
    }
    
    @RequestMapping(value="/units_proceso_3")
    public void units_proceso_3(HttpServletResponse response){
        //com.americatel.facturacion.units.MainProceso3 m=new com.americatel.facturacion.units.MainProceso3();
        //m.run();
    }    
}
