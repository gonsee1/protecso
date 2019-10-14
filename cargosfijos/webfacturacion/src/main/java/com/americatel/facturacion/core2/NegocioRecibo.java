/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.core2;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.servlet.http.HttpServletResponse;

import com.americatel.facturacion.fncs.FechaHora;
import com.americatel.facturacion.models.CIER_DATA_PLAN;
import com.americatel.facturacion.models.CIER_DATA_PROM_MONT;
import com.americatel.facturacion.models.CIER_DATA_SERV_SUPL;
import com.americatel.facturacion.models.CIER_DATA_SERV_UNIC;

/**
 *
 * @author crodas
 */
public class NegocioRecibo{
    private NegocioFacturar nf=null;
    private List<Date[]> Periodos=null;
    private Long Numero_Recibo_Semilla=0L;
    private Long Numero_Factura_Semilla=0L;
    private ListaRecibo recibos=new ListaRecibo();
    private Double saldo=0d;
    private Double pendiente_upgrade=0d;
    private Double saldo_anterior=0d;
    public NegocioRecibo(NegocioFacturar nf){
        this.nf=nf;        
        this.saldo_anterior=nf.getNego().getSaldoNegocio(nf.getCier());
    }
    
    private void calcularPeriodos(){
        //calcula los periodos del los detalles generados
        this.Periodos=new ArrayList<Date[]>();
        Map<Integer,Date[]> keys=new TreeMap<Integer,Date[]>();
        for(SucursalDetalle sd : this.nf.negocio_detalle.getDetalle_sucursales()){
            Integer key=null;
            for(CIER_DATA_PLAN plan: sd.getDetalle_planes()){
                key=FechaHora.getYear(plan.getFE_INIC())*100+FechaHora.getMonth(plan.getFE_INIC());
                if (!keys.containsKey(key)){
                    keys.put(key, new Date[]{plan.getFE_INIC(),plan.getFE_FIN()});
                }else{                    
                    Date fechas[]=(Date[])keys.get(key);
                    if (plan.getFE_INIC().getTime()<fechas[0].getTime())
                        fechas[0]=plan.getFE_INIC();
                    if (plan.getFE_FIN().getTime()>fechas[1].getTime())
                        fechas[1]=plan.getFE_FIN();
                    keys.put(key, fechas);
                }                
            }
            for(CIER_DATA_SERV_SUPL ss: sd.getDetalle_servicios_suplementarios()){
                key=FechaHora.getYear(ss.getFE_INIC())*100+FechaHora.getMonth(ss.getFE_INIC());
                if (!keys.containsKey(key)){
                    keys.put(key, new Date[]{ss.getFE_INIC(),ss.getFE_FIN()});
                }else{                    
                    Date fechas[]=(Date[])keys.get(key);
                    if (ss.getFE_INIC().getTime()<fechas[0].getTime())
                        fechas[0]=ss.getFE_INIC();
                    if (ss.getFE_FIN().getTime()>fechas[1].getTime())
                        fechas[1]=ss.getFE_FIN();
                    keys.put(key, fechas);
                }                
            }
        }
        for(Entry <Integer,Date[]> entry: keys.entrySet()){
           Periodos.add(entry.getValue());
        }
    }
    
    public void crearPdf(HttpServletResponse response){
        this.calcularPeriodos();
        // Para el caso de los negocios que estan en baja y tienen saldo pendiente
        //Por modificar
        if (!nf.getNego().isDesactivado() || nf.getNego().isDesactivadoEnPresentCierre()){
            this.saldo=this.saldo_anterior;
        }
        
        Double saldoAproximado=nf.saldo_aproximado;
        Boolean aplicarSaldoAprox=false;
        
        Boolean periodo_principal=false;
        Boolean generaFactura=false;
        int van=0;
        int son=Periodos.size();
        for(Date[] periodo : Periodos){            
            van++;
            periodo_principal=(van==son);
            Recibo r=new Recibo(nf, periodo,this.Numero_Recibo_Semilla,this.saldo,periodo_principal,this.pendiente_upgrade);
            if (r.isReciboValido()){
                r.crearPdf();
                recibos.getRecibos().add(r);
                // Como ya se cobro el diferencial de cargos fijos (totales[17]) entonces se va resetear a 0 para ya no cobrarlo en otro recibo
                r.resetearPendienteUpgrade();
                this.Numero_Recibo_Semilla++;
            }
            this.pendiente_upgrade=r.getPendienteUpgrade();
            this.saldo=r.getSaldo();
        }
        
        if (recibos.isGeneraFacturaDetraccion()){            
            Factura fac=new Factura(nf,this.Numero_Factura_Semilla,this.recibos,null);
            fac.crearPdf();            
            this.recibos.setFactura(fac);
            this.Numero_Factura_Semilla++;
        } else {
            //Verificar que genera factura cuando no genera ningun recibo
            if (recibos.getRecibos().isEmpty()){
                Factura fac=new Factura(nf,this.Numero_Factura_Semilla,this.recibos,Periodos);
                generaFactura=fac.generaFactura(Periodos);
                if (generaFactura){
                    fac.crearPdf();            
                    this.recibos.setFactura(fac);
                    this.Numero_Factura_Semilla++;
                }
            }
        }
        
        if (saldoAproximado<0){ // saldoAproximado<0
            this.saldo+=saldoAproximado;
            aplicarSaldoAprox=true;
        }
        
        recibos.setSaldo(this.saldo);
        recibos.savePdf(response);
        recibos.savePdfFactura();
        recibos.closeZip();
        
        //Descontamos el saldo aproximado para no guardarlo en la tabla de saldos
        if (aplicarSaldoAprox){
            this.saldo=this.saldo-saldoAproximado;
            recibos.setSaldo(this.saldo);
        }
    }

    public Long getNumero_Recibo_Semilla() {
        return Numero_Recibo_Semilla;
    }

    public void setNumero_Recibo_Semilla(Long Numero_Recibo_Semilla) {
        this.Numero_Recibo_Semilla = Numero_Recibo_Semilla;
    }

    public Long getNumero_Factura_Semilla() {
        return Numero_Factura_Semilla;
    }

    public void setNumero_Factura_Semilla(Long Numero_Factura_Semilla) {
        this.Numero_Factura_Semilla = Numero_Factura_Semilla;
    }

    public void saveBD() {
        for(Recibo recibo : recibos.getRecibos()){
            for(SucursalDetalle sd : this.nf.negocio_detalle.getDetalle_sucursales()){
                //Asignamos los codigo de los recibos
                for(CIER_DATA_PLAN plan : sd.getDetalle_planes()){
                    if (
                            FechaHora.isBetween(recibo.getPeriodoInicio(), recibo.getPeriodoFin(), plan.getFE_INIC() ) &&
                            FechaHora.isBetween(recibo.getPeriodoInicio(), recibo.getPeriodoFin(), plan.getFE_FIN() )
                        ){
                        plan.setCO_RECI(recibo.getNumero());
                    }
                }
                
                for(CIER_DATA_SERV_SUPL ss : sd.getDetalle_servicios_suplementarios()){
                    if (
                            FechaHora.isBetween(recibo.getPeriodoInicio(), recibo.getPeriodoFin(), ss.getFE_INIC() ) &&
                            FechaHora.isBetween(recibo.getPeriodoInicio(), recibo.getPeriodoFin(), ss.getFE_FIN() )
                        ){
                        ss.setCO_RECI(recibo.getNumero());
                    }
                }
                                
                if (recibo.isPeriodo_principal()){
                    for(CIER_DATA_PROM_MONT pro : sd.getDetalle_promociones_monto()){
                        pro.setCO_RECI(recibo.getNumero());
                    }                    
                    for(CIER_DATA_SERV_UNIC su : sd.getDetalle_servicios_unicos()){
                        su.setCO_RECI(recibo.getNumero());                       
                    }
                }
            }
            recibo.saveBD();
        }
        if (this.recibos.getFactura()!=null){
            this.recibos.getFactura().saveBD();
        }
        this.nf.negocio_detalle.save();
    }

    public Double getSaldo() {
        return saldo;
    }

    public Double getSaldo_anterior() {
        return saldo_anterior;
    }

    public ListaRecibo getRecibos() {
        return recibos;
    }
    
    
    
}
