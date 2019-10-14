/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.reportes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.americatel.facturacion.fncs.ExcelWriter;
import com.americatel.facturacion.mappers.mapNEGO;
import com.americatel.facturacion.mappers.mapNEGO_SUCU_PLAN;
import com.americatel.facturacion.mappers.mapPLAN;
import com.americatel.facturacion.models.NEGO;
import com.americatel.facturacion.models.NEGO_SUCU_PLAN;
import com.americatel.facturacion.models.PLAN;
import com.americatel.facturacion.models.REGLAS_NOMBRE_PLAN;

/**
 *
 * @author crodas
 */
@Component
public class ReporteGeneral {
    private ExcelWriter excel;
    private List<Map<String, Object>> select;
    
    
    public static mapNEGO mapNego;  
    public static mapNEGO_SUCU_PLAN mapNego_Sucu_Plan;
    public static mapPLAN mapPlan;
    
    @Autowired
    public void setmapNEGO_SUCU_PLAN(mapNEGO_SUCU_PLAN mapNego_Sucu_Plan){
    	ReporteGeneral.mapNego_Sucu_Plan=mapNego_Sucu_Plan;        
    } 
    
    @Autowired
    public void setmapPlan(mapPLAN mapPlan){
    	ReporteGeneral.mapPlan = mapPlan;
    }

	@Autowired
    public void setmapNego(mapNEGO mapNego){
       ReporteGeneral.mapNego = mapNego;
    }
   
	public ReporteGeneral(){
        
    }
    
    public ReporteGeneral(List<Map<String, Object>> select,Integer COD_PROD_PADRE,Integer COD_SERVICIO) {
        this.excel=new ExcelWriter();
        this.excel.addHoja("Reporte General");
        this.select=select;
        this.calcular(COD_PROD_PADRE,COD_SERVICIO);        
    }

    public void save(HttpServletResponse response) {
        response.setHeader("Content-Type", "application/vnd.ms-excel");
        response.addHeader("Content-Disposition", "attachment; filename=\"Reportes_General.xls\"" );
        this.excel.save(response);
    }
        
    public String getStringOrCero(String cadena){
        if (cadena!=null){
            if (!cadena.trim().equals(""))
                return cadena;
        }
        return "0";
    }
    /*
    private void calcular(Integer COD_PROD_PADRE,Integer COD_SERVICIO) {
        // Se quito la columna descuento a pedido de facturacion 19-02-2016
        this.excel.writeCols(new Object[]{"Negocio","OIT INSTALACION","TIPO FACTURACION","PERIODO FACTURACION","SERVICIO","MEDIO PLAN","CODIGO CLIENTE","CLIENTE","N\u00daMERO DOCUMENTO",
            "DIRECCION FISCAL","DIRECCION INSTALACION","ESTADO SUCURSAL","MONEDA FACTURACION","SALDO PENDIENTE","SALDO APROXIMADO","MONTO INSTALACION","MONTO MENSUAL","MONTO ALQUILER",
            "MONTO OTROS","NUEVA","PROMOCION","TIPO","PERIODO INICIO","A\u00d1O INICIO","PERIODO FIN","A\u00d1O FIN","NEGOCIO ORIGEN (CAMBIO DE MONEDA)"});//nombre columnas
        //for(IteratorMap<String, Object> x : select.iterator())
        String attrs[]={
            "CO_NEGO",
            "CO_OIT_INST",
            "NO_TIPO_FACT",
            "NO_PERI_FACT",
            "NO_SERV",
            "NO_PLAN_MEDI_INST",
            "CO_CLIE",
            "NO_CLIE",
            "DE_NUME_DOCU",
            "DE_DIRE_FISC",
            "DE_DIRE_INST",
            "NO_ESTA_SUCU",
            "NO_MONE_FACT",
            "IM_SALD",
            "IM_SALD_APROX",
            "IM_MONT_INST",
            "IM_MONT_MENS",
            "IM_MONT_ALQU",
            "IM_MONT_OTRO",
            "IS_NUEV",
            "IM_PROM",
            "DE_TIPO",
            "DE_PERI_INIC",
            "DE_ANO_INIC",
            "DE_PERI_FIN",
            "DE_ANO_FIN",
            "CO_NEGO_ORIG"
        };
        
        NEGO negoBean = new NEGO();
        NEGO_SUCU_PLAN nsp  = new NEGO_SUCU_PLAN();  
        Integer codCoProd;
        Integer codCoProdPadre=0;
        PLAN planBean ;
        Integer codPRODPADRE_SP=0;
        boolean imprime = false;
        for(Map<String, Object> item : select){
            List<Object> lista=new ArrayList<Object>();
            planBean = null;
            imprime = false;            
            nsp = null;
            if(COD_PROD_PADRE == Integer.parseInt(item.get("COD_PROD_PADRE").toString()))
            {
                for(String a : attrs){
                    
                    if(a.equalsIgnoreCase("CO_NEGO") && item.get(a)!=null){
                        
                        negoBean = mapNego.getId(Integer.parseInt(item.get(a).toString()));
                        codCoProd = negoBean.getCO_PROD_ForJSON();
                        codCoProdPadre = negoBean.getProd_padre_ForJSON().getCO_PROD_PADRE_ForJSON();
                        
                        if(codCoProd != COD_SERVICIO ){
                        	if(COD_SERVICIO > 0 )
                        	{
                        		imprime = true;
                        		break;  
                        	}
                        	else {
                        		imprime = false;
                        	}
    	                      
                        }
                    }
                    
                    if(a.equalsIgnoreCase("CO_OIT_INST") && item.get(a)!=null){
                    	planBean = mapPlan.getForOitInstalacion(Integer.parseInt(item.get(a).toString()));                	
//                		nsp = mapNego_Sucu_Plan.obtenerForOitInstalacion(Integer.parseInt(item.get(a).toString()));
                	}
                    
                    
                    if(planBean != null)
                    {
    	                codPRODPADRE_SP = planBean.getCOD_PROD_PADRE_ForJSON();
    	                
    	                if ((item.get(a)!=null) && (codCoProdPadre == codPRODPADRE_SP)){
    	                    
    	                    if(a.equalsIgnoreCase("NO_SERV")){
    	                    	
    	                    	lista.add(planBean.getPRODUCTO_CARGA());
    	//                    	lista.add(getCodServicio(COD_PROD_PADRE,String.valueOf(nsp.getCO_PLAN_ForJSON()))); 
    	                    }else{
    	                    	lista.add(item.get(a).toString());
    	                    }
    	                    
    	                }else{
    	                    lista.add(null);
    	                }    
                    }
                    else{
                    	System.out.println("ERROR");
                    }
                }
            	
            }

            if(!imprime){
            	this.excel.writeCols(lista);    
            }
            
        }
    }
    */
    
    private void calcular(Integer COD_PROD_PADRE,Integer COD_SERVICIO) {
    	
        // Se quito la columna descuento a pedido de facturacion 19-02-2016
        this.excel.writeCols(new Object[]{"Negocio","OIT INSTALACION","TIPO FACTURACION","PERIODO FACTURACION","SERVICIO","MEDIO PLAN","CODIGO CLIENTE","CLIENTE","NÚMERO DOCUMENTO",
            "DIRECCION FISCAL","DIRECCION INSTALACION","ESTADO SUCURSAL","MONEDA FACTURACION","SALDO PENDIENTE","SALDO APROXIMADO","MONTO INSTALACION","MONTO MENSUAL","MONTO ALQUILER",
            "MONTO OTROS","NUEVA","PROMOCION","TIPO","PERIODO INICIO","AÑO INICIO","PERIODO FIN","AÑO FIN","NEGOCIO ORIGEN (CAMBIO DE MONEDA)"});//nombre columnas
        //for(IteratorMap<String, Object> x : select.iterator())
        String attrs[]={
            "CO_NEGO",
            "CO_OIT_INST",
            "NO_TIPO_FACT",
            "NO_PERI_FACT",
            "NO_SERV",
            "NO_PLAN_MEDI_INST",
            "CO_CLIE",
            "NO_CLIE",
            "DE_NUME_DOCU",
            "DE_DIRE_FISC",
            "DE_DIRE_INST",
            "NO_ESTA_SUCU",
            "NO_MONE_FACT",
            "IM_SALD",
            "IM_SALD_APROX",
            "IM_MONT_INST",
            "IM_MONT_MENS",
            "IM_MONT_ALQU",
            "IM_MONT_OTRO",
            "IS_NUEV",
            "IM_PROM",
            "DE_TIPO",
            "DE_PERI_INIC",
            "DE_ANO_INIC",
            "DE_PERI_FIN",
            "DE_ANO_FIN",
            "CO_NEGO_ORIG"
        };
        for(Map<String, Object> item : select){
            List<Object> lista=new ArrayList<Object>();
            
            if(COD_SERVICIO == 0)
            {
                if(COD_PROD_PADRE == Integer.parseInt(item.get("COD_PROD_PADRE").toString()))
                {            
    	            for(String a : attrs){
    	                if (item.get(a)!=null)
    	                    lista.add(item.get(a).toString());
    	                else
    	                    lista.add(null);
    	            }
    	            /*for (Entry <String,Object> valor : item.entrySet()){                
    	
    	            }*/
    	            this.excel.writeCols(lista);
                }
            }
            else {
                if((COD_PROD_PADRE == Integer.parseInt(item.get("COD_PROD_PADRE").toString()))&&(COD_SERVICIO == Integer.parseInt(item.get("COD_PROD").toString())))
                {            
    	            for(String a : attrs){
    	                if (item.get(a)!=null)
    	                    lista.add(item.get(a).toString());
    	                else
    	                    lista.add(null);
    	            }
    	            /*for (Entry <String,Object> valor : item.entrySet()){                
    	
    	            }*/
    	            this.excel.writeCols(lista);
                }
            	
            }
            	
            

        }
    }
    
}
