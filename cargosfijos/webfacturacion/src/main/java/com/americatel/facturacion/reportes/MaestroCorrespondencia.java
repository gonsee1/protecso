/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.reportes;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.americatel.facturacion.fncs.ExcelWriter;
import com.americatel.facturacion.models.BOLE;
import com.americatel.facturacion.models.CIER;
import com.americatel.facturacion.models.DIST;
import com.americatel.facturacion.models.FACT;
import com.americatel.facturacion.models.PROD;
import com.americatel.facturacion.models.RECI;

/**
 *
 * @author crodas
 */
public class MaestroCorrespondencia {
    private List<CIER> listaCier;
    private Integer NU_ANO;
    private Integer NU_PERI;
    private ExcelWriter excel;
    public MaestroCorrespondencia(List<CIER> listaCier){
        this.listaCier=listaCier;
        if (listaCier.size()>0){
            this.NU_ANO=listaCier.get(0).getNU_ANO_ForJSON();
            this.NU_PERI=listaCier.get(0).getNU_PERI_ForJSON();
        }
        this.excel=new ExcelWriter();
        this.excel.addHoja("Reporte Correspondencia");
        this.calcular();
    }

    public void save(HttpServletResponse response) {
        response.setHeader("Content-Type", "application/vnd.ms-excel");
        response.addHeader("Content-Disposition", "attachment; filename=\"Reportes_Correspondencia_"+NU_ANO+"_"+NU_PERI+"\".xls" );
        this.excel.save(response);
    }

    private void calcular() {
        this.excel.writeCols(new Object[]{"TIPO DOCUMENTO","NRO DOCUMENTO","FECHA EMISION","FECHA VENCIMIENTO","NEGOCIO","RAZON SOCIAL","RUC","DIRECCI\u00d3N DE CORRESPONDENCIA","REFERENCIA DE CORRESPONDENCIA","DISTRITO DE CORRESPONDENCIA","PROVINCIA DE CORRESPONDENCIA","DEPARTAMENTO DE CORRESPONDENCIA"});
        for(CIER cier:listaCier){
            List<RECI> recibos=cier.getRecibos();
            PROD prod=cier.getProducto();
            for(RECI recibo : recibos){
                DIST dist_corr=recibo.getDist_Corr_ForJSON();
                this.excel.writeCols(new Object[]{
                    "RECIBO",
                    recibo.getCO_RECI_ForJSON(),
                    recibo.getFE_EMIS_ForJSON(),                    
                    recibo.getFE_VENC_ForJSON(),
                    recibo.getCO_NEGO_ForJSON(),
                    recibo.getNO_CLIE_ForJSON(),
                    recibo.getDE_NUME_DOCU_ForJSON(),
                    recibo.getDE_DIRE_CORR_ForJSON(),
                    recibo.getDE_REF_DIRE_CORR_ForJSON(),
                    dist_corr.getNO_DIST_ForJSON(),
                    dist_corr.getPROV_ForJSON().getNO_PROV_ForJSON(),
                    dist_corr.getPROV_ForJSON().getDEPA_ForJSON().getNO_DEPA_ForJSON(),
                });                
            }
            List<BOLE> boletas=cier.getBoletas();            
            for(BOLE boleta : boletas){
                DIST dist_corr=boleta.getDist_Corr_ForJSON();
                this.excel.writeCols(new Object[]{
                    "BOLETA",
                    boleta.getCO_BOLE_ForJSON(),
                    boleta.getFE_EMIS_ForJSON(),                    
                    boleta.getFE_VENC_ForJSON(),
                    boleta.getCO_NEGO_ForJSON(),
                    boleta.getNO_CLIE_ForJSON(),
                    boleta.getDE_NUME_DOCU_ForJSON(),
                    boleta.getDE_DIRE_CORR_ForJSON(),
                    boleta.getDE_REF_DIRE_CORR_ForJSON(),
                    dist_corr.getNO_DIST_ForJSON(),
                    dist_corr.getPROV_ForJSON().getNO_PROV_ForJSON(),
                    dist_corr.getPROV_ForJSON().getDEPA_ForJSON().getNO_DEPA_ForJSON(),
                });                
            }
            
            List<FACT> facturas=cier.getFacturas();
            for(FACT factura : facturas){
                DIST dist_corr=factura.getDist_Corr_ForJSON();
                this.excel.writeCols(new Object[]{
                    "FACTURA",
                    factura.getCO_FACT_ForJSON(),
                    factura.getFE_EMIS_ForJSON(),                    
                    factura.getFE_VENC_ForJSON(),
                    factura.getCO_NEGO_ForJSON(),
                    factura.getNO_CLIE_ForJSON(),
                    factura.getDE_NUME_DOCU_ForJSON(),
                    factura.getDE_DIRE_CORR_ForJSON(),
                    factura.getDE_REF_DIRE_CORR_ForJSON(),
                    dist_corr.getNO_DIST_ForJSON(),
                    dist_corr.getPROV_ForJSON().getNO_PROV_ForJSON(),
                    dist_corr.getPROV_ForJSON().getDEPA_ForJSON().getNO_DEPA_ForJSON(),
                });                 
            }
        }
    }
    
    
}
