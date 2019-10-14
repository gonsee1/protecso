/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.core3;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.americatel.facturacion.fncs.FechaHora;
import com.americatel.facturacion.mappers.mapAJUS;
import com.americatel.facturacion.mappers.mapBOLE;
import com.americatel.facturacion.mappers.mapCIER;
import com.americatel.facturacion.mappers.mapCIER_DATA_NEGO;
import com.americatel.facturacion.mappers.mapCIER_DATA_PLAN;
import com.americatel.facturacion.mappers.mapCIER_DATA_SERV_SUPL;
import com.americatel.facturacion.mappers.mapCIER_DATA_SERV_UNIC;
import com.americatel.facturacion.mappers.mapCIER_DATA_SUCU;
import com.americatel.facturacion.mappers.mapCURSOR;
import com.americatel.facturacion.mappers.mapDETALLE_DOCUMENTO;
import com.americatel.facturacion.mappers.mapFACT;
import com.americatel.facturacion.mappers.mapFACT_GLOS;
import com.americatel.facturacion.mappers.mapNEGO;
import com.americatel.facturacion.mappers.mapNEGO_SALD;
import com.americatel.facturacion.mappers.mapNEGO_SUCU_PLAN;
import com.americatel.facturacion.mappers.mapNEGO_SUCU_SERV_SUPL;
import com.americatel.facturacion.mappers.mapNEGO_SUCU_SERV_UNIC;
import com.americatel.facturacion.mappers.mapPLAN;
import com.americatel.facturacion.mappers.mapPROD;
import com.americatel.facturacion.mappers.mapRECI;
import com.americatel.facturacion.mappers.mapRECI_GLOS;
import com.americatel.facturacion.mappers.mapSERV_SUPL;
import com.americatel.facturacion.mappers.mapSERV_UNIC;
import com.americatel.facturacion.models.AJUS;
import com.americatel.facturacion.models.CIER;
import com.americatel.facturacion.models.CIER_DATA_NEGO;
import com.americatel.facturacion.models.CIER_DATA_PLAN;
import com.americatel.facturacion.models.CIER_DATA_SERV_SUPL;
import com.americatel.facturacion.models.CIER_DATA_SERV_UNIC;
import com.americatel.facturacion.models.CIER_DATA_SUCU;
import com.americatel.facturacion.models.CLIE;
import com.americatel.facturacion.models.CORT;
import com.americatel.facturacion.models.DEPA;
import com.americatel.facturacion.models.DIST;
import com.americatel.facturacion.models.MONE_FACT;
import com.americatel.facturacion.models.NEGO;
import com.americatel.facturacion.models.NEGO_PROM;
import com.americatel.facturacion.models.NEGO_SALD;
import com.americatel.facturacion.models.NEGO_SALD_CERO;
import com.americatel.facturacion.models.NEGO_SUCU;
import com.americatel.facturacion.models.NEGO_SUCU_PLAN;
import com.americatel.facturacion.models.NEGO_SUCU_PROM;
import com.americatel.facturacion.models.NEGO_SUCU_SERV_SUPL;
import com.americatel.facturacion.models.NEGO_SUCU_SERV_UNIC;
import com.americatel.facturacion.models.PROD;
import com.americatel.facturacion.models.PROD_PADRE;
import com.americatel.facturacion.models.PROV;
import com.americatel.facturacion.models.SUCU;
import com.americatel.facturacion.models.SUSP;
import com.americatel.facturacion.models.TIPO_DOCU;

/**
 *
 * @author crodas
 */
@Component
public class ProcesoNegocio {
	/*
	 * Reglas generales: 1)En cortes y suspensiones la fecha de fin ya tiene el
	 * servicio. 2)En la fecha desactivacion si tiene servicio ese dia
	 */
	final static Logger logger = Logger.getLogger(ProcesoNegocio.class);

	public static mapCURSOR mapCursor;

	@Autowired
	public void setmapCURSOR(mapCURSOR mapCursor) {
		ProcesoNegocio.mapCursor = mapCursor;
	}

	public static mapNEGO mapNego;

	@Autowired
	public void setmapNEGO(mapNEGO mapNego) {
		ProcesoNegocio.mapNego = mapNego;
	}

	public static mapNEGO_SALD mapNego_Sald;

	@Autowired
	public void setmapNEGO_SALD(mapNEGO_SALD mapNego_Sald) {
		ProcesoNegocio.mapNego_Sald = mapNego_Sald;
	}

	public static mapCIER_DATA_NEGO mapCier_Data_Nego;

	@Autowired
	public void setmapCIER_DATA_NEGO(mapCIER_DATA_NEGO mapCier_Data_Nego) {
		ProcesoNegocio.mapCier_Data_Nego = mapCier_Data_Nego;
	}

	public static mapCIER_DATA_SUCU mapCier_Data_Sucu;

	@Autowired
	public void setmapCIER_DATA_SUCU(mapCIER_DATA_SUCU mapCier_Data_Sucu) {
		ProcesoNegocio.mapCier_Data_Sucu = mapCier_Data_Sucu;
	}

	public static mapCIER_DATA_SERV_SUPL mapCier_Data_Serv_Supl;

	@Autowired
	public void setmapCIER_DATA_SERV_SUPL(mapCIER_DATA_SERV_SUPL mapCier_Data_Serv_Supl) {
		ProcesoNegocio.mapCier_Data_Serv_Supl = mapCier_Data_Serv_Supl;
	}

	public static mapCIER_DATA_SERV_UNIC mapCier_Data_Serv_Unic;

	@Autowired
	public void setmapCIER_DATA_SERV_UNIC(mapCIER_DATA_SERV_UNIC mapCier_Data_Serv_Unic) {
		ProcesoNegocio.mapCier_Data_Serv_Unic = mapCier_Data_Serv_Unic;
	}

	public static mapCIER_DATA_PLAN mapCier_Data_Plan;

	@Autowired
	public void setmapCIER_DATA_PLAN(mapCIER_DATA_PLAN mapCier_Data_Plan) {
		ProcesoNegocio.mapCier_Data_Plan = mapCier_Data_Plan;
	}

	public static mapRECI mapReci;

	@Autowired
	public void setmapRECI(mapRECI mapReci) {
		ProcesoNegocio.mapReci = mapReci;
	}

	public static mapRECI_GLOS mapReci_Glos;

	@Autowired
	public void setmapRECI_GLOS(mapRECI_GLOS mapReci_Glos) {
		ProcesoNegocio.mapReci_Glos = mapReci_Glos;
	}

	public static mapFACT_GLOS mapFact_Glos;

	@Autowired
	public void setmapFACT_GLOS(mapFACT_GLOS mapFact_Glos) {
		ProcesoNegocio.mapFact_Glos = mapFact_Glos;
	}

	public static mapFACT mapFact;

	@Autowired
	public void setmapFACT(mapFACT mapFact) {
		ProcesoNegocio.mapFact = mapFact;
	}

	public static mapNEGO_SUCU_PLAN mapNego_Sucu_Plan;

	@Autowired
	public void setmapNEGO_SUCU_PLAN(mapNEGO_SUCU_PLAN mapNego_Sucu_Plan) {
		ProcesoNegocio.mapNego_Sucu_Plan = mapNego_Sucu_Plan;
	}

	public static mapNEGO_SUCU_SERV_SUPL mapNego_Sucu_Serv_Supl;

	@Autowired
	public void setmapNEGO_SUCU_SERV_SUPL(mapNEGO_SUCU_SERV_SUPL mapNego_Sucu_Serv_Supl) {
		ProcesoNegocio.mapNego_Sucu_Serv_Supl = mapNego_Sucu_Serv_Supl;
	}

	public static mapNEGO_SUCU_SERV_UNIC mapNego_Sucu_Serv_Unic;

	@Autowired
	public void setmapNEGO_SUCU_SERV_UNIC(mapNEGO_SUCU_SERV_UNIC mapNego_Sucu_Serv_Unic) {
		ProcesoNegocio.mapNego_Sucu_Serv_Unic = mapNego_Sucu_Serv_Unic;
	}

	public static mapAJUS mapAjus;

	@Autowired
	public void setmapAJUS(mapAJUS mapAjus) {
		ProcesoNegocio.mapAjus = mapAjus;
	}

	public static mapCIER mapCier;

	@Autowired
	public void setmapCIER(mapCIER mapCier) {
		ProcesoNegocio.mapCier = mapCier;
	}

	public static mapPROD mapProd;

	@Autowired
	public void setmapPROD(mapPROD mapProd) {
		ProcesoNegocio.mapProd = mapProd;
	}

	public static mapBOLE mapBole;

	@Autowired
	public void setmapBOLE(mapBOLE mapBole) {
		ProcesoNegocio.mapBole = mapBole;
	}

	public static mapPLAN mapPlan;

	@Autowired
	public void setmapPLAN(mapPLAN mapPlan) {
		ProcesoNegocio.mapPlan = mapPlan;
	}

	public static mapDETALLE_DOCUMENTO mapDetalle_Documento;

	@Autowired
	public void setmapDETALLE_DOCUMENTO(mapDETALLE_DOCUMENTO mapDetalle_Documento) {
		ProcesoNegocio.mapDetalle_Documento = mapDetalle_Documento;
	}

	// public static mapSERV_SUPL mapServ_Supl;
	// @Autowired
	// public void setmapSERV_SUPL(mapSERV_SUPL mapServ_Supl){
	// ProcesoNegocio.mapServ_Supl=mapServ_Supl;
	// }
	//
	// public static mapSERV_UNIC mapServ_Unic;
	// @Autowired
	// public void setmapSERV_UNIC(mapSERV_UNIC mapServ_Unic){
	// ProcesoNegocio.mapServ_Unic=mapServ_Unic;
	// }

	private CIER_DATA_NEGO Cier_Data_Nego = new CIER_DATA_NEGO();
	private SUCU sucursal_fiscal = null;
	private SUCU sucursal_correspondencia = null;
	private DIST distrito_fiscal = null;
	private DIST distrito_correspondencia = null;
	private PROV provincia_fiscal = null;
	private PROV provincia_correspondencia = null;
	private DEPA departamento_fiscal = null;
	private DEPA departamento_correspondencia = null;
	private MONE_FACT moneda_facturacion = null;
	private TIPO_DOCU tipo_documento = null;
	private CLIE cliente = null;

	NEGO nego;
	PROD prod;
	CIER cier;
	NEGO_SALD_CERO saldo_cero;
	List<CORT> cortes;
	List<CORT> cortes_pend;
	List<NEGO_SUCU> sucursales;
	Map<NEGO_SUCU, CIER_DATA_SUCU> sucursales_data;
	private Map<CIER_DATA_SUCU, Object[]> listaPlanesySsPorSucursal;// data_facturar;
	Map<NEGO_SUCU, Map<Integer, Double>> saldo_aproximado_planes = new HashMap<NEGO_SUCU, Map<Integer, Double>>();
	Map<NEGO_SUCU, Map<Integer, Double>> saldo_aproximado_sss = new HashMap<NEGO_SUCU, Map<Integer, Double>>();
	Map<CIER_DATA_SUCU, List<NEGO_SUCU_SERV_UNIC>> servicios_unicos = new HashMap<CIER_DATA_SUCU, List<NEGO_SUCU_SERV_UNIC>>();
	Map<CIER_DATA_SUCU, Map<NEGO_SUCU_SERV_UNIC, CIER_DATA_SERV_UNIC>> servicios_unicos_data = new HashMap<CIER_DATA_SUCU, Map<NEGO_SUCU_SERV_UNIC, CIER_DATA_SERV_UNIC>>();

	// Factura factura=null;
	List<AJUS> ajustes = null;
	Boolean log_view = false;
	Double saldo_negocio = null;
	ListaRecibo lista_recibo = new ListaRecibo(this);
	Long semilla_recibo = 0L, semilla_factura = 0L, semilla_boleta = 0L;
	Long semilla_recibo_sgte = 0L, semilla_factura_sgte = 0L, semilla_boleta_sgte = 0L;
	Double saldo = 0d;
	Boolean preview = false;
	Boolean facturar_cesi_cont_padre = false;
	Boolean considerarNego = true;

	public ProcesoNegocio() {
	}

	private Double redondearPorDefecto(Double n) {
		return com.americatel.facturacion.fncs.Numero.redondear(n, 2);
	}

	public ProcesoNegocio(NEGO nego, PROD prod, CIER cier) {
		this(nego, prod, cier, 0d, false, false);
	}

	public ProcesoNegocio(NEGO nego, PROD prod, CIER cier, Boolean preview, Boolean facturar_cesi_cont_padre) {
		this(nego, prod, cier, 0d, preview, facturar_cesi_cont_padre);
	}

	public ProcesoNegocio(NEGO nego, PROD prod, CIER cier, Double saldo_negocio, Boolean preview,
			Boolean facturar_cesi_cont_padre) {
		try {
			this.preview = preview;
			this.facturar_cesi_cont_padre = facturar_cesi_cont_padre;
			this.nego = nego;
			this.prod = prod;
			this.cier = cier;
			this.moneda_facturacion = nego.getMone_fact_ForJSON();
			this.cliente = nego.getCLIE_ForJSON();
			this.sucursal_correspondencia = nego.getSUCU_CORR_ForJSON();
			this.distrito_correspondencia = this.sucursal_correspondencia.getDist_ForJSON();
			this.provincia_correspondencia = this.distrito_correspondencia.getPROV_ForJSON();
			this.departamento_correspondencia = this.provincia_correspondencia.getDEPA_ForJSON();
			this.tipo_documento = this.cliente.getTipo_docu_ForJSON();
			this.sucursal_fiscal = this.cliente.getSUCU_FISC_ForJSON();
			this.distrito_fiscal = this.sucursal_fiscal.getDist_ForJSON();
			this.provincia_fiscal = this.distrito_fiscal.getPROV_ForJSON();
			this.departamento_fiscal = this.provincia_fiscal.getDEPA_ForJSON();

			// nego=mapNego.getId(CO_NEGO);
			// prod=nego.getPROD();
			// cier=prod.getCierrePendiente();
			// cortes=nego.getCortesPendientes_Fact();
			sucursales = nego.getSucursales();
			this.saldo_cero = nego.getSaldoCero();
			if (this.saldo_cero != null) {
				this.considerarNego = false;
			}

			sucursales_data = new HashMap<NEGO_SUCU, CIER_DATA_SUCU>();
			ajustes = nego.getAjustesPendientes(cier);
			for (NEGO_SUCU nego_sucu : sucursales) {
				CIER_DATA_SUCU data_sucu = new CIER_DATA_SUCU();
				SUCU sucu = nego_sucu.getSUCU();
				DIST dist = sucu.getDist_ForJSON();
				PROV prov = dist.getPROV_ForJSON();
				DEPA depa = prov.getDEPA_ForJSON();

				data_sucu.setCO_DEPA(depa.getCO_DEPA_ForJSON());
				data_sucu.setCO_DIST(dist.getCO_DIST_ForJSON());
				data_sucu.setCO_NEGO_SUCU(nego_sucu.getCO_NEGO_SUCU_ForJSON());
				data_sucu.setCO_PROV(prov.getCO_PROV_ForJSON());
				data_sucu.setDE_DIRE_SUCU(sucu.getDE_DIRE_ForJSON());
				data_sucu.setDE_ORDE(nego_sucu.getDE_ORDE_SERV_ForJSON());
				data_sucu.setNO_DEPA(depa.getNO_DEPA_ForJSON());
				data_sucu.setNO_DIST(dist.getNO_DIST_ForJSON());
				data_sucu.setNO_PROV(prov.getNO_PROV_ForJSON());

				sucursales_data.put(nego_sucu, data_sucu);

				// int codigoCierre;//
				// if(this.cier == null){
				// codigoCierre = 0;
				// }else{
				// codigoCierre = this.cier.getCO_CIER_ForJSON();
				// }
				// List<NEGO_SUCU_SERV_UNIC>
				// listaServUnicos=nego_sucu.getServiciosUnicosPendientes(codigoCierre);
				List<NEGO_SUCU_SERV_UNIC> listaServUnicos = nego_sucu
						.getServiciosUnicosPendientes(this.cier == null ? 0 : this.cier.getCO_CIER_ForJSON());
				if (listaServUnicos.size() > 0) {
					servicios_unicos.put(data_sucu, listaServUnicos);
				}
			}
			listaPlanesySsPorSucursal = new HashMap<CIER_DATA_SUCU, Object[]>();
			this.saldo_negocio = saldo_negocio + Math.abs(this.nego.getSaldoNegocio(this.cier));

			// this.moneda_facturacion=this.nego.getMonedaFacturacion();
			this.cortes = this.nego.getCortesPendientes_Fact();
			this.cortes_pend = nego.getCortesPendientes();
			// this.cliente=this.nego.getClie();
			// this.sucursal_fiscal=this.cliente.getSucursalFiscal();
			// this.sucursal_correspondencia=this.nego.getSucursalCorrespondencia();
			// this.distrito_fiscal=this.sucursal_fiscal.getDistrito();
			// this.distrito_correspondencia=this.sucursal_correspondencia.getDistrito();
			// this.provincia_fiscal=this.distrito_fiscal.getPROV_ForJSON();
			// this.provincia_correspondencia=this.distrito_correspondencia.getPROV_ForJSON();
			// this.departamento_fiscal=this.provincia_fiscal.getDEPA_ForJSON();
			// this.departamento_correspondencia=this.provincia_correspondencia.getDEPA_ForJSON();
			// tipo_documento=this.cliente.getTipoDocumento();
			if (this.cier != null) {
				this.Cier_Data_Nego.setCO_CIER(this.cier.getCO_CIER_ForJSON());
			}
			this.Cier_Data_Nego.setCO_NEGO(this.nego.getCO_NEGO_ForJSON());
		} catch (Exception e) {
			System.out.println("NEGOCIO  :  " + nego);
			System.out.println("ERRORRRRR  :  " + e);
		}
	}

	public void facturar(NEGO negocio) {
		boolean validador = (!negocio.isDesactivado() || negocio.isDesactivadoEnPresentCierre());
		if (this.nego.getCO_NEGO_ForJSON() == 5193337) {
			logger.info("Negocio : " + negocio.getCO_NEGO_ForJSON() + " - " + negocio.getDESC_PROD_PADRE_ForJSON());
		}
		logger.info("Negocio Activo : " + validador);
		logger.info("*******************************************************************************");
		//validamos si el negocio este activo
		if (validador) {
			// if (log_view) System.out.println(">NEGOCIO :
			// "+this.nego.getCO_NEGO_ForJSON());
			//Obtenemos los planes a facturar
			Map<CIER_DATA_SUCU, List<CIER_DATA_PLAN>> listaPlanesFacturados = facturarPlanes(this.cier, this.nego,
					this.sucursales, this.saldo_aproximado_planes, this.sucursales_data, this.considerarNego);
			for (Entry<CIER_DATA_SUCU, List<CIER_DATA_PLAN>> e : listaPlanesFacturados.entrySet()) {
				listaPlanesySsPorSucursal.put(e.getKey(),
						new Object[] { e.getValue(), new ArrayList<CIER_DATA_SERV_SUPL>() });
			}
			for (Entry<CIER_DATA_SUCU, List<CIER_DATA_SERV_SUPL>> e : this.facturarServiciosSuplementarios()
					.entrySet()) {
				if (listaPlanesySsPorSucursal.containsKey(e.getKey())) {
					listaPlanesySsPorSucursal.get(e.getKey())[1] = e.getValue();
				} else {
					listaPlanesySsPorSucursal.put(e.getKey(),
							new Object[] { new ArrayList<CIER_DATA_PLAN>(), e.getValue() });
				}
			}
			
			//Inicializamos variables de negocio
			NEGO objNego = new NEGO();
			PROD_PADRE prod = new PROD_PADRE();
			String descripcionProducto = "";

			prod = objNego.getProductoByNegocio(negocio.getCO_PROD_ForJSON());

			if (prod != null) {
				descripcionProducto = prod.getDESC_PROD_PADRE_ForJSON().toUpperCase();
			}

			this.calcularRecibos(descripcionProducto, listaPlanesySsPorSucursal);
		}
	}

	public List<Date[]> getPeriodos(Date inicio, Date fin) {
		List<Date[]> ret = new ArrayList<Date[]>();
		Date a = inicio;
		Date b = null;
		while (fin.getTime() >= a.getTime()) {
			b = FechaHora.getUltimaFechaMes(a);
			if (b.getTime() >= fin.getTime())
				b = fin;
			ret.add(new Date[] { a, b });
			a = FechaHora.addDays(b, 1);
		}
		return ret;
	}

	private Double getPromocionPorPorcentajeServicioSuplementario(Date fecha_inicio,
			List<NEGO_PROM> negocio_promociones, List<NEGO_SUCU_PROM> sucursal_promociones) {
		for (NEGO_PROM nego_prom : negocio_promociones) {
			Date i = FechaHora.getDate(nego_prom.getDE_ANO_INIC_ForJSON(), nego_prom.getDE_PERI_INIC_ForJSON() - 1, 1);
			Date f = FechaHora.getUltimaFechaMes(
					FechaHora.getDate(nego_prom.getDE_ANO_FIN_ForJSON(), nego_prom.getDE_PERI_FIN_ForJSON() - 1, 1));
			if (FechaHora.isBetween(i, f, fecha_inicio) && nego_prom.getST_SERV_SUPL_ForJSON()) {
				return redondearPorDefecto(nego_prom.getIM_VALO_ForJSON() / 100d);
			}
		}

		if (sucursal_promociones != null) {
			for (NEGO_SUCU_PROM nego_sucu_prom : sucursal_promociones) {
				Date i = FechaHora.getDate(nego_sucu_prom.getDE_ANO_INIC_ForJSON(),
						nego_sucu_prom.getDE_PERI_INIC_ForJSON() - 1, 1);
				Date f = FechaHora.getUltimaFechaMes(FechaHora.getDate(nego_sucu_prom.getDE_ANO_FIN_ForJSON(),
						nego_sucu_prom.getDE_PERI_FIN_ForJSON() - 1, 1));
				if (FechaHora.isBetween(i, f, fecha_inicio) && nego_sucu_prom.getST_SERV_SUPL_ForJSON()) {
					return redondearPorDefecto(nego_sucu_prom.getIM_VALO_ForJSON() / 100d);
				}
			}
		}

		return null;
	}

	private Double getPromocionPorPorcentajePlan(Date fecha_inicio, List<NEGO_PROM> negocio_promociones,
			List<NEGO_SUCU_PROM> sucursal_promociones) {
		for (NEGO_PROM nego_prom : negocio_promociones) {
			Date i = FechaHora.getDate(nego_prom.getDE_ANO_INIC_ForJSON(), nego_prom.getDE_PERI_INIC_ForJSON() - 1, 1);
			Date f = FechaHora.getUltimaFechaMes(
					FechaHora.getDate(nego_prom.getDE_ANO_FIN_ForJSON(), nego_prom.getDE_PERI_FIN_ForJSON() - 1, 1));
			if (FechaHora.isBetween(i, f, fecha_inicio) && nego_prom.getST_PLAN_ForJSON()) {
				return redondearPorDefecto(nego_prom.getIM_VALO_ForJSON() / 100d);
			}
		}

		if (sucursal_promociones != null) {

			for (NEGO_SUCU_PROM nego_sucu_prom : sucursal_promociones) {
				Date i = FechaHora.getDate(nego_sucu_prom.getDE_ANO_INIC_ForJSON(),
						nego_sucu_prom.getDE_PERI_INIC_ForJSON() - 1, 1);
				Date f = FechaHora.getUltimaFechaMes(FechaHora.getDate(nego_sucu_prom.getDE_ANO_FIN_ForJSON(),
						nego_sucu_prom.getDE_PERI_FIN_ForJSON() - 1, 1));
				if (FechaHora.isBetween(i, f, fecha_inicio) && nego_sucu_prom.getST_PLAN_ForJSON()) {
					return redondearPorDefecto(nego_sucu_prom.getIM_VALO_ForJSON() / 100d);
				}
			}
		}

		return null;
	}

	private Double getPromocionPorMonto(Date fecha_inicio, List<NEGO_SUCU_PROM> sucursal_promociones) {
		for (NEGO_SUCU_PROM nego_sucu_prom : sucursal_promociones) {
			Date i = FechaHora.getDate(nego_sucu_prom.getDE_ANO_INIC_ForJSON(),
					nego_sucu_prom.getDE_PERI_INIC_ForJSON() - 1, 1);
			Date f = FechaHora.getUltimaFechaMes(FechaHora.getDate(nego_sucu_prom.getDE_ANO_FIN_ForJSON(),
					nego_sucu_prom.getDE_PERI_FIN_ForJSON() - 1, 1));
			if (FechaHora.isBetween(i, f, fecha_inicio)) {
				return redondearPorDefecto(nego_sucu_prom.getIM_VALO_ForJSON());
			}
		}
		return null;
	}

	private Double getMontoPorDevolucionPorPromocion(List<CIER_DATA_PLAN> planes, Date fecha_inicio, Date fecha_fin) {
		// Busca que sea igual al detalle del cobro con el de devolucion
		Long li = fecha_inicio.getTime(), lf = fecha_fin.getTime();
		for (CIER_DATA_PLAN data : planes) {
			if (data.getFE_INIC().getTime() == li && data.getFE_INIC().getTime() == lf)
				return redondearPorDefecto(data.getIM_MONT());
		}
		return null;
	}

	private Double getMontoPorDevolucionPorPromocionSS(List<CIER_DATA_SERV_SUPL> sss, Date fecha_inicio,
			Date fecha_fin) {
		// Busca que sea igual al detalle del cobro con el de devolucion
		Long li = fecha_inicio.getTime(), lf = fecha_fin.getTime();
		for (CIER_DATA_SERV_SUPL data : sss) {
			if (data.getFE_INIC().getTime() == li && data.getFE_INIC().getTime() == lf)
				return redondearPorDefecto(data.getIM_MONT());
		}
		return null;
	}

	private Double getMontoPorDevolucionPorPromocionEntrePeriodos(List<CIER_DATA_PLAN> planes, Date fecha_inicio,
			Date fecha_fin) {
		// Busca la devolucion total entre un periodo sin importar que sean los
		// mismo
		Long li = fecha_inicio.getTime(), lf = fecha_fin.getTime();
		Double retornar = 0d;
		for (CIER_DATA_PLAN data : planes) {
			Date inter[] = FechaHora.getFechaIntersecta(new Date[] { data.getFE_INIC(), data.getFE_FIN() },
					new Date[] { fecha_inicio, fecha_fin });
			if (inter != null) {
				Double ppd = data.getIM_MONT() / (FechaHora.getDiffDays(data.getFE_INIC(), data.getFE_FIN()));
				retornar += FechaHora.getDiffDays(inter[0], inter[1]) * ppd;
			}
		}
		return redondearPorDefecto(retornar);
	}

	private Double getMontoPorDevolucionPorPromocionEntrePeriodosSS(List<CIER_DATA_SERV_SUPL> sss, Date fecha_inicio,
			Date fecha_fin) {
		// Busca la devolucion total entre un periodo sin importar que sean los
		// mismo
		Long li = fecha_inicio.getTime(), lf = fecha_fin.getTime();
		Double retornar = 0d;
		for (CIER_DATA_SERV_SUPL data : sss) {
			Date inter[] = FechaHora.getFechaIntersecta(new Date[] { data.getFE_INIC(), data.getFE_FIN() },
					new Date[] { fecha_inicio, fecha_fin });
			if (inter != null) {
				Double ppd = data.getIM_MONT() / (FechaHora.getDiffDays(data.getFE_INIC(), data.getFE_FIN()));
				retornar += FechaHora.getDiffDays(inter[0], inter[1]) * ppd;
			}
		}
		return redondearPorDefecto(retornar);
	}

	private Double getPromocionPlan(Double numero_dias, Double dias_considerar, Double cobro, Date fecha_inicio,
			List<NEGO_PROM> promociones_negocios, List<NEGO_SUCU_PROM> promociones_sucursal_porcentaje,
			List<NEGO_SUCU_PROM> promociones_sucursal_monto) {
		Double promocion_por_procentaje = this.getPromocionPorPorcentajePlan(fecha_inicio, promociones_negocios,
				promociones_sucursal_porcentaje);
		Double promocion_por_monto = null;
		if (promocion_por_procentaje == null && promociones_sucursal_monto != null)
			promocion_por_monto = this.getPromocionPorMonto(fecha_inicio, promociones_sucursal_monto);
		if (promocion_por_procentaje != null || promocion_por_monto != null) {
			Double dev = null;
			if (promocion_por_monto != null) {
				if (Math.abs(cobro) > Math.abs(promocion_por_monto))
					dev = promocion_por_monto;
				else
					dev = cobro;
				// debe ser proporcional a los dias
				dev = dev / numero_dias * dias_considerar;
			} else {
				dev = cobro * promocion_por_procentaje;
			}
			return redondearPorDefecto(dev);
		}
		return null;
	}

	private Double getPromocionServicioSuplementario(Double numero_dias, Double dias_considerar, Double cobro,
			Date fecha_inicio, List<NEGO_PROM> promociones_negocios,
			List<NEGO_SUCU_PROM> promociones_sucursal_porcentaje) {
		Double promocion_por_procentaje = this.getPromocionPorPorcentajeServicioSuplementario(fecha_inicio,
				promociones_negocios, promociones_sucursal_porcentaje);
		if (promocion_por_procentaje != null) {
			Double dev = cobro * promocion_por_procentaje;
			return redondearPorDefecto(dev);
		}
		return null;
	}

	public Map<CIER_DATA_SUCU, List<CIER_DATA_PLAN>> facturarPlanes(CIER cierreActual, NEGO negocio,
			List<NEGO_SUCU> listaNegoSucu, Map<NEGO_SUCU, Map<Integer, Double>> saldoAproximadoPlanes,
			Map<NEGO_SUCU, CIER_DATA_SUCU> listaCierDataSucu, boolean considerarNegocio) {
		logger.info("Facturar Planes");
		Date fecha_real_inicio = null;
		Date fecha_real_fin = null;
		
		logger.info(fecha_real_inicio);
		logger.info(fecha_real_fin);

		
		Map<CIER_DATA_SUCU, List<CIER_DATA_PLAN>> retornar_facturar_por_sucursal_planes = new HashMap<CIER_DATA_SUCU, List<CIER_DATA_PLAN>>();
		List<NEGO_PROM> promociones_negocios = new ArrayList<NEGO_PROM>(); // nego.getPromocionesPorcentajePendientes(cier);

		logger.info("Cantidad de Sucursales :" + sucursales.size());
		
		//Obtenemos cada sucursal del negocio
		for (NEGO_SUCU sucursal : listaNegoSucu) {
			
		//Inicializamos las variables del ultimo mes a facturar por cada sucursal
			if (cierreActual != null) {

				fecha_real_inicio = FechaHora.getDate(cierreActual.getNU_ANO_ForJSON(),
						cierreActual.getNU_PERI_ForJSON() - 1, 1);
				if (negocio.isTipoFacturacionVencida())
					fecha_real_inicio = FechaHora.addMonths(fecha_real_inicio, -1);
				fecha_real_fin = FechaHora.getUltimaFechaMes(fecha_real_inicio);
				logger.info(fecha_real_inicio);
				
			} else {

				Calendar cal = Calendar.getInstance();
				int year = cal.get(Calendar.YEAR);

				int mes = cal.get(Calendar.MONTH);

				fecha_real_inicio = FechaHora.getDate(year, mes - 1, 1);
				if (negocio.isTipoFacturacionVencida())
					fecha_real_inicio = FechaHora.addMonths(fecha_real_inicio, -1);
				fecha_real_fin = FechaHora.getUltimaFechaMes(fecha_real_inicio);
			}
			
			
			if (!sucursal.isMudado_ForJSON()) {
				if (!sucursal.isCesionContractual_ForJSON()) {

					List<NEGO_SUCU_PROM> promociones_sucursal_porcentaje = null;
					List<NEGO_SUCU_PROM> promociones_sucursal_monto = null;
					
					//Si el cierre actual existe
					if (cierreActual != null) {
						promociones_sucursal_porcentaje = sucursal.getPromocionesPorcentajePendientes(cier);
						promociones_sucursal_monto = sucursal.getPromocionesMontoPendientes(cier);
					}

					Map<Integer, Double> saldo_aproximado_sucursal = new HashMap<Integer, Double>();
					saldoAproximadoPlanes.put(sucursal, saldo_aproximado_sucursal);
					List<CIER_DATA_PLAN> retornar_facturar_planes = new ArrayList<CIER_DATA_PLAN>();
					retornar_facturar_por_sucursal_planes.put(listaCierDataSucu.get(sucursal),
							retornar_facturar_planes);
					SUCU sucu = sucursal.getSUCU();
					List<SUSP> suspensiones = sucursal.getSuspensionesPendientes_Fact();
					// List<SUSP>
					// suspensiones_pend=sucursal.getSuspensionesPendientes();
					//Obtenemos los periodos de suspencion y cortes 
					List<Date[]> periodoCORSUP = FechaHora.getPeriodosUnionCortesSuspensiones(cortes, suspensiones);
					
					//Validamos si existe periodos de suspencion y cortes
					if (periodoCORSUP.isEmpty()) {
						//Lo inicializo con la fecha de inicio del mes a facturar
						fecha_real_inicio = fecha_real_inicio;
						
					} else {
						//En caso contrario obtnengo la fecha final del corte o suspencion
						fecha_real_inicio = FechaHora.getFechaInicial(periodoCORSUP);
					}

					Date FechaInicio_Temp = FechaHora.getFechaInicial(periodoCORSUP);

					List<NEGO_SUCU_PLAN> planes = sucursal.getPlanes();
					logger.info("Sucursal :" + sucu.getDE_DIRE_ForJSON());
					logger.info("Cantidad de planes :" + planes.size());
					logger.info("*******************************************************************************");
					// if (log_view)
					// //System.out.println(">"+sucursal.getCO_OIT_INST_ForJSON()+"
					// > "+sucu.getDE_DIRE_ForJSON());
					//Iteramos por cada plan 
					for (NEGO_SUCU_PLAN plan : planes) {
						logger.info("Plan :" + plan.getNO_PLAN_ForJSON());
						// if (log_view) System.out.println("plan
						// "+plan.getNO_PLAN_ForJSON()+" id
						// "+plan.getCO_NEGO_SUCU_PLAN_ForJSON());
						
						Date UFF = plan.getFE_ULTI_FACT_ForJSON();
						Date FE_FIN = plan.getFE_FIN_ForJSON();
						Date FE_INIC = plan.getFE_INIC_ForJSON();

						List<CIER_DATA_PLAN> planes_ya_cobrados = null;
						List<CIER_DATA_PLAN> planes_devoluciones_por_promociones = null;
						List<CIER_DATA_PLAN> mi_plan_ya_cobrados = null;

						if (cierreActual != null) {
							planes_ya_cobrados = plan.getCIER_DATA_PLANES_COBRADOS(cierreActual);
							planes_devoluciones_por_promociones = plan
									.getCIER_DATA_DEVOLUCIONES_POR_PROMOCIONES(cierreActual);
							mi_plan_ya_cobrados = plan.getCIER_DATA_MI_PLAN_COBRADOS(cierreActual);
						}
						
						if (sucursal.getCO_NEGO_SUCU_CESI_CONT_PADR_ForJSON() != null) {
							//Inicializo la variable
							NEGO_SUCU_PLAN plan_cesi_cont = new NEGO_SUCU_PLAN();
							//Envio el codigo del plan de cese del contrato padre e hijo de la iteracion al campo CO_NEGO_SUCU_PLAN y CO_NEGO_SUCU
							plan_cesi_cont.setCO_NEGO_SUCU_PLAN(plan.getCO_NEGO_SUCU_PLAN_CESI_CONT_PADR_ForJSON());
							plan_cesi_cont.setCO_NEGO_SUCU(sucursal.getCO_NEGO_SUCU_CESI_CONT_PADR_ForJSON());
							List<CIER_DATA_PLAN> planes_ya_cobrados_cesi_cont = plan_cesi_cont
									.getCIER_DATA_PLANES_COBRADOS(cierreActual);
							List<CIER_DATA_PLAN> planes_devoluciones_por_promociones_cesi_cont = plan_cesi_cont
									.getCIER_DATA_DEVOLUCIONES_POR_PROMOCIONES(cier);
							List<CIER_DATA_PLAN> mi_plan_ya_cobrados_cesi_cont = plan_cesi_cont
									.getCIER_DATA_MI_PLAN_COBRADOS(cierreActual);
							//Si hay plan ya cobrado 
							if (!planes_ya_cobrados_cesi_cont.isEmpty()) {
								planes_ya_cobrados.addAll(planes_ya_cobrados_cesi_cont);
							}
							//Si hay planes de devolucion por promociones 
							if (!planes_devoluciones_por_promociones_cesi_cont.isEmpty()) {
								planes_devoluciones_por_promociones
										.addAll(planes_devoluciones_por_promociones_cesi_cont);
							}
							//Si hay planes ya cobrados
							if (!mi_plan_ya_cobrados_cesi_cont.isEmpty()) {
								mi_plan_ya_cobrados.addAll(mi_plan_ya_cobrados_cesi_cont);
							}
						}

						if (sucursal.getCO_NEGO_SUCU_MUDA_PADR_ForJSON() != null) {
							NEGO_SUCU_PLAN plan_muda = new NEGO_SUCU_PLAN();
							plan_muda.setCO_NEGO_SUCU_PLAN(plan.getCO_NEGO_SUCU_PLAN_MUDA_PADR_ForJSON());
							plan_muda.setCO_NEGO_SUCU(sucursal.getCO_NEGO_SUCU_MUDA_PADR_ForJSON());
							List<CIER_DATA_PLAN> planes_ya_cobrados_muda = plan_muda
									.getCIER_DATA_PLANES_COBRADOS(cierreActual);
							List<CIER_DATA_PLAN> planes_devoluciones_por_promociones_muda = plan_muda
									.getCIER_DATA_DEVOLUCIONES_POR_PROMOCIONES(cierreActual);
							List<CIER_DATA_PLAN> mi_plan_ya_cobrados_muda = plan_muda
									.getCIER_DATA_MI_PLAN_COBRADOS(cierreActual);
							if (!planes_ya_cobrados_muda.isEmpty()) {
								planes_ya_cobrados.addAll(planes_ya_cobrados_muda);
							}
							if (!planes_devoluciones_por_promociones_muda.isEmpty()) {
								planes_devoluciones_por_promociones.addAll(planes_devoluciones_por_promociones_muda);
							}
							if (!mi_plan_ya_cobrados_muda.isEmpty()) {
								mi_plan_ya_cobrados.addAll(mi_plan_ya_cobrados_muda);
							}
						}

						List<Date[]> periodos_planes_ya_cobrados = new ArrayList<Date[]>();
						
						//Si hay plan ya cobrado
						if (planes_ya_cobrados != null) {

							for (CIER_DATA_PLAN plan_ya_cobrado : planes_ya_cobrados) {
								periodos_planes_ya_cobrados
										.add(new Date[] { plan_ya_cobrado.getFE_INIC(), plan_ya_cobrado.getFE_FIN() });
							}
						}
						
						//Si la ultima fecha de facturacion del plan es nulo
						if (UFF == null)
							UFF = FE_INIC;
						
						//Si la fecha de fin es nula: no hay suspension del plan
						if (FE_FIN == null || FE_FIN.getTime() != UFF.getTime()) {
							// if (!(this.nego.isCortado() ||
							// sucursal.isSuspendido_ForJSON()) ||
							// periodoCORSUP.size()>0){
							if (FE_FIN == null || FE_FIN.getTime() > UFF.getTime()) {
								// P2*****
								// Generar Periodos de Facturacion
								//fecha_real_inicio = plan.getFE_INIC_ForJSON();
								Date periodo_inicio = fecha_real_inicio;
								Date periodo_fin = fecha_real_fin;
								Date nueva_ultima_facturacion = null;
								// CMC12072018
								if (UFF.getTime() == FE_INIC.getTime())
									periodo_inicio = UFF;
								// else
								// periodo_inicio=FechaHora.addDays(UFF, 1);
								if (FE_FIN != null) {
									if (periodo_fin.getTime() > FE_FIN.getTime()) {
										periodo_fin = FE_FIN;
									}
								}
								// P2A*****
								// Se cobrar los complementos: fecha inicio, fecha final

								List<Date[]> complementos = FechaHora.getQuitarCortesSuspensionesNoConsiderarReconexion(
										new Date[] { periodo_inicio, periodo_fin }, periodoCORSUP);
								for (Date[] complemento : complementos) {
									List<Date[]> periodos = this.getPeriodos(complemento[0], complemento[1]);
									for (Date[] periodo : periodos) {
										// Aqui se tiene que carlcular los
										// diferencias y los montos a cobrar si
										// no se han cobrado en otro plan

										List<Date[]> no_se_cobrars = FechaHora.getQuitar(periodo,
												periodos_planes_ya_cobrados);
										// Estos son los planes que no han
										// pagado
										//Itera la fecha que no se ha cobrado
										for (Date no_se_cobrar[] : no_se_cobrars) {
											Double nd = (double) FechaHora.getNumDaysOfMonth(no_se_cobrar[0]);
											Double ppd = plan.getIM_MONTO_ForJSON() / nd;
											Double dias_considerar = (double) FechaHora.getDiffDays(no_se_cobrar[0],
													no_se_cobrar[1]);
											Double cob = redondearPorDefecto(dias_considerar * ppd);
											// if (log_view)
											// System.out.println("cobrar 1 de
											// "+FechaHora.gF(no_se_cobrar[0])+"
											// a
											// "+FechaHora.gF(no_se_cobrar[1])+"
											// pagar "+cob);

											logger.info("Cobrar Desde  :" + FechaHora.gF(no_se_cobrar[0]) + " Hasta :"
													+ FechaHora.gF(no_se_cobrar[1]));
											logger.info("Precio del plan :" + plan.getIM_MONTO_ForJSON());
											logger.info("Total Cobrar:" + cob);

											CIER_DATA_PLAN data = new CIER_DATA_PLAN();
											data.setCO_NEGO_SUCU_PLAN(plan.getCO_NEGO_SUCU_PLAN_ForJSON());
											data.setDE_NOMB(plan.getNO_PLAN_ForJSON());
											data.setFE_INIC(no_se_cobrar[0]);
											data.setFE_FIN(no_se_cobrar[1]);
											data.setCO_MONE_FACT(plan.getPlan_ForJSON().getCO_MONE_FACT_ForJSON());
											data.setIM_MONT(cob);
											data.setST_TIPO_COBR(1);
											retornar_facturar_planes.add(data);

											// cobrar+=cob;
											
											Double dev = this.getPromocionPlan(nd, dias_considerar, cob,
													no_se_cobrar[0], promociones_negocios,
													promociones_sucursal_porcentaje, promociones_sucursal_monto);
											
											//Si hay devoluciones 
											if (dev != null) {
												data = new CIER_DATA_PLAN();
												data.setCO_NEGO_SUCU_PLAN(plan.getCO_NEGO_SUCU_PLAN_ForJSON());
												data.setDE_NOMB(plan.getNO_PLAN_ForJSON());
												data.setFE_INIC(no_se_cobrar[0]);
												data.setFE_FIN(no_se_cobrar[1]);
												data.setCO_MONE_FACT(plan.getPlan_ForJSON().getCO_MONE_FACT_ForJSON());
												data.setIM_MONT(dev);
												data.setST_TIPO_DEVO(4); // promocion
												retornar_facturar_planes.add(data);

												// if (log_view)
												// System.out.println("devolver
												// 6 de
												// "+FechaHora.gF(no_se_cobrar[0])+"
												// a
												// "+FechaHora.gF(no_se_cobrar[1])+"
												// devolver "+dev);
												logger.info("Devolver Desde : " + FechaHora.gF(no_se_cobrar[0])
														+ "Hasta :" + FechaHora.gF(no_se_cobrar[1]));
												logger.info("Total Devolver" + dev);
												// cobrar+=cob;
											}

											if (nueva_ultima_facturacion == null) {
												nueva_ultima_facturacion = no_se_cobrar[1];
											} else if (nueva_ultima_facturacion.getTime() < no_se_cobrar[1].getTime()) {
												nueva_ultima_facturacion = no_se_cobrar[1];
											}

										}
										// Aqui se cobrar o se devuelve los
										// diferenciales

										if (planes_ya_cobrados != null) {

											for (CIER_DATA_PLAN plane_ya_cobrado : planes_ya_cobrados) {
												Date fecha_diferencial[] = FechaHora.getFechaIntersecta(periodo,
														new Date[] { plane_ya_cobrado.getFE_INIC(),
																plane_ya_cobrado.getFE_FIN() });
												if (fecha_diferencial != null) {
													Double monto_promocion_en_periodo = this
															.getMontoPorDevolucionPorPromocion(
																	planes_devoluciones_por_promociones,
																	plane_ya_cobrado.getFE_INIC(),
																	plane_ya_cobrado.getFE_FIN());

													Double nd = (double) FechaHora
															.getNumDaysOfMonth(fecha_diferencial[0]);
													int dias = FechaHora.getDiffDays(fecha_diferencial[0],
															fecha_diferencial[1]);
													Double ppdAhora = (plan.getIM_MONTO_ForJSON() / nd) * dias;
													Double ppdAntes;
													if (monto_promocion_en_periodo == null)
														ppdAntes = (plane_ya_cobrado.getIM_MONT()
																/ FechaHora.getDiffDays(plane_ya_cobrado.getFE_INIC(),
																		plane_ya_cobrado.getFE_FIN()))
																* dias;
													else
														ppdAntes = ((plane_ya_cobrado.getIM_MONT()
																- monto_promocion_en_periodo)
																/ FechaHora.getDiffDays(plane_ya_cobrado.getFE_INIC(),
																		plane_ya_cobrado.getFE_FIN()))
																* dias;
													Double mon = redondearPorDefecto(ppdAhora - ppdAntes);

													CIER_DATA_PLAN data = new CIER_DATA_PLAN();
													data.setCO_NEGO_SUCU_PLAN(plan.getCO_NEGO_SUCU_PLAN_ForJSON());
													data.setDE_NOMB(plan.getNO_PLAN_ForJSON());
													data.setFE_INIC(fecha_diferencial[0]);
													data.setFE_FIN(fecha_diferencial[1]);
													data.setCO_MONE_FACT(plane_ya_cobrado.getCO_MONE_FACT());// Proceso
																												// de
																												// cambio
																												// de
																												// tarifa
																												// *****
													data.setIM_MONT(Math.abs(mon)); // Guardar
																					// el
																					// valor
																					// en
																					// positivo
																					// 06-12-2016
													if (mon > 0) {
														if (log_view)
															System.out.println(
																	"cobrar 2 de " + FechaHora.gF(fecha_diferencial[0])
																			+ " a " + FechaHora.gF(fecha_diferencial[1])
																			+ "  pagar " + mon);
														data.setST_TIPO_COBR(2);// upgrade
													} else {
														if (log_view)
															System.out.println("devolver 3 de "
																	+ FechaHora.gF(fecha_diferencial[0]) + " a "
																	+ FechaHora.gF(fecha_diferencial[1]) + "  devolver "
																	+ mon);
														data.setST_TIPO_DEVO(5);// downgrade
													}

													if (nueva_ultima_facturacion == null) {
														nueva_ultima_facturacion = fecha_diferencial[1];
													} else if (nueva_ultima_facturacion.getTime() < fecha_diferencial[1]
															.getTime()) {
														nueva_ultima_facturacion = fecha_diferencial[1];
													}

													retornar_facturar_planes.add(data);
													// cobrar+=mon;

												}
											}
										}
									}
								}
								// P2B*****
								// Se calculas las devoluciones
								// CMC SUSPENCIONES
								for (Date[] periodo_devolucion : periodoCORSUP) {
									for (CIER_DATA_PLAN plane_ya_cobrado : mi_plan_ya_cobrados) {
										Date inter[] = FechaHora.getFechaIntersecta(
												new Date[] { periodo_devolucion[0],
														FechaHora.addDays(periodo_devolucion[1], -1) },
												new Date[] { plane_ya_cobrado.getFE_INIC(),
														plane_ya_cobrado.getFE_FIN() });
										if (inter != null) {
											Double devolucion_por_promocion = this.getMontoPorDevolucionPorPromocion(
													planes_devoluciones_por_promociones, plane_ya_cobrado.getFE_INIC(),
													plane_ya_cobrado.getFE_FIN());
											double dias;
											// if
											// (periodo_devolucion[1].getTime()==inter[1].getTime())
											// dias=FechaHora.getDiffDays(plane_ya_cobrado.getFE_INIC(),
											// FechaHora.addDays(plane_ya_cobrado.getFE_FIN(),1));
											// else
											dias = FechaHora.getDiffDays(plane_ya_cobrado.getFE_INIC(),
													plane_ya_cobrado.getFE_FIN());
											double ppd = 0;
											if (devolucion_por_promocion == null)
												ppd = plane_ya_cobrado.getIM_MONT() / dias;
											else
												ppd = (plane_ya_cobrado.getIM_MONT() - devolucion_por_promocion) / dias;
											double monto = redondearPorDefecto(
													FechaHora.getDiffDays(inter[0], inter[1]) * ppd);

											if (log_view)
												System.out.println("devolver 1 de " + FechaHora.gF(inter[0]) + " a "
														+ FechaHora.gF(inter[1]) + "  devolver " + monto);

											boolean esFechaCorteDif = false;
											
											if (this.saldo_cero != null) {
												if (this.saldo_cero.getFE_CORTE_ForJSON()
														.getTime() != periodo_devolucion[0].getTime()) {
													esFechaCorteDif = true;
												}
											}

											if (considerarNegocio || esFechaCorteDif) {
												if (periodo_devolucion[1]
														.getTime() != FechaHora.FECHA_MAXIMA_SI_NO_TIENE.getTime()
														|| sucursal.isDesactivado_ForJSON()) {
													CIER_DATA_PLAN data = new CIER_DATA_PLAN();
													data.setCO_NEGO_SUCU_PLAN(plan.getCO_NEGO_SUCU_PLAN_ForJSON());
													data.setDE_NOMB(plan.getNO_PLAN_ForJSON());
													data.setFE_INIC(inter[0]);
													data.setFE_FIN(inter[1]);
													data.setCO_MONE_FACT(plane_ya_cobrado.getCO_MONE_FACT());// Proceso
																												// de
																												// cambio
																												// de
																												// tarifa
																												// *****
													data.setIM_MONT(monto);
													data.setST_TIPO_DEVO(2);// corte
																			// o
																			// suspension
													retornar_facturar_planes.add(data);

													// cobrar-=monto;
												} else {
													// saldo aproximado
													if (!saldo_aproximado_sucursal
															.containsKey(plan.getCO_NEGO_SUCU_PLAN_ForJSON())) {
														saldo_aproximado_sucursal
																.put(plan.getCO_NEGO_SUCU_PLAN_ForJSON(), 0d);
													}
													saldo_aproximado_sucursal
															.put(plan.getCO_NEGO_SUCU_PLAN_ForJSON(),
																	saldo_aproximado_sucursal
																			.get(plan.getCO_NEGO_SUCU_PLAN_ForJSON())
																			+ monto);

													// P2B1*****
													// GUARDAMOS SALDO
													// APROXIMADO si no es
													// preview
													if (!this.preview) {
														plan.setIM_SALD_APROX(saldo_aproximado_sucursal
																.get(plan.getCO_NEGO_SUCU_PLAN_ForJSON()));
														mapNego_Sucu_Plan.updateSaldoAproximado(plan);
													}

												}
											}

											if (nueva_ultima_facturacion == null) {
												nueva_ultima_facturacion = inter[1];
											} else if (nueva_ultima_facturacion.getTime() < inter[1].getTime()) {
												nueva_ultima_facturacion = inter[1];
											}
										}
									}
								}
								// P2C*****
								// Actualizamos la UFF_SGTE del plan con >>
								// periodo_fin
								if (!this.preview) {
									plan.setFE_ULTI_FACT_SGTE(nueva_ultima_facturacion);
									mapNego_Sucu_Plan.saveSiguienteUltimaFacturacion(plan);
								}

							} else {
								// P3*****
								// Proceso de Devolucion por Desactivacion
								List<NEGO_SUCU_PLAN> planes_entre_periodo_desactivado = plan
										.getPlanesEntrePeriodoDesactivado(UFF);
								Date periodo_devolver[] = new Date[] { FechaHora.addDays(FE_FIN, 1), UFF };// El
																											// ultimo
																											// dia
																											// si
																											// tenia
																											// servicio
								List<Date[]> quitar = new ArrayList<Date[]>();
								for (NEGO_SUCU_PLAN plane_entre_periodo_desactivado : planes_entre_periodo_desactivado) {
									Date periodo_plan_no_devolver[] = new Date[] {
											plane_entre_periodo_desactivado.getFE_INIC_ForJSON(), UFF };
									if (plane_entre_periodo_desactivado.getFE_FIN_ForJSON() != null) {
										if (UFF.getTime() > plane_entre_periodo_desactivado.getFE_FIN_ForJSON()
												.getTime()) {
											periodo_plan_no_devolver[1] = plane_entre_periodo_desactivado
													.getFE_FIN_ForJSON();
										}
									}
									// Si se deolveria si se intercepta con un
									// corte o suspension
									// por eso se quita los cortes y
									// suspensiones
									for (Date q[] : FechaHora.getQuitarCortesSuspensionesNoConsiderarReconexion(
											periodo_plan_no_devolver, periodoCORSUP)) {
										quitar.add(q);
									}
								}
								List<Date[]> devolvers = FechaHora.getQuitar(periodo_devolver, quitar);
								for (Date devolver[] : devolvers) {
									Date devolver_inicio = devolver[0];
									Date devolver_fin = devolver[1];

									for (Date periodo[] : this.getPeriodos(devolver_inicio, devolver_fin)) {

										for (CIER_DATA_PLAN plane_ya_cobrado : mi_plan_ya_cobrados) {
											Date[] inter = FechaHora.getFechaIntersecta(periodo, new Date[] {
													plane_ya_cobrado.getFE_INIC(), plane_ya_cobrado.getFE_FIN() });
											if (inter != null) {
												Double devolucion_por_promocion = this
														.getMontoPorDevolucionPorPromocionEntrePeriodos(
																planes_devoluciones_por_promociones, periodo[0],
																periodo[1]);
												int ndd = FechaHora.getDiffDays(inter[0], inter[1]);
												Double ppd = plane_ya_cobrado.getIM_MONT() / FechaHora.getDiffDays(
														plane_ya_cobrado.getFE_INIC(), plane_ya_cobrado.getFE_FIN());

												Double dev = redondearPorDefecto(ndd * ppd - devolucion_por_promocion);
												boolean generaSaldoAprox = false;
												boolean esFechaCorteDif = false;
												for (Date sin_servicio[] : periodoCORSUP) {
													Date interCORSUP[] = FechaHora.getFechaIntersecta(periodo,
															new Date[] { sin_servicio[0],
																	FechaHora.addDays(sin_servicio[1], -1) });

													if (this.saldo_cero != null) {
														if (this.saldo_cero.getFE_CORTE_ForJSON()
																.getTime() != sin_servicio[0].getTime()) {
															esFechaCorteDif = true;
														}
													}

													if (considerarNegocio || esFechaCorteDif) {
														if (interCORSUP != null) {
															// plan.getCO_NEGO_SUCU_PLAN_CESI_CONT_HIJO_ForJSON()!=null
															if (sin_servicio[1]
																	.getTime() == FechaHora.FECHA_MAXIMA_SI_NO_TIENE
																			.getTime()
																	&& !sucursal.isDesactivado_ForJSON()) {
																generaSaldoAprox = true;
																// saldo
																// aproximado
																if (!saldo_aproximado_sucursal.containsKey(
																		plan.getCO_NEGO_SUCU_PLAN_ForJSON())) {
																	saldo_aproximado_sucursal.put(
																			plan.getCO_NEGO_SUCU_PLAN_ForJSON(), 0d);
																}
																saldo_aproximado_sucursal.put(
																		plan.getCO_NEGO_SUCU_PLAN_ForJSON(),
																		saldo_aproximado_sucursal.get(
																				plan.getCO_NEGO_SUCU_PLAN_ForJSON())
																				+ dev);

																// GUARDAMOS
																// SALDO
																// APROXIMADO si
																// no es preview
																if (!this.preview) {
																	plan.setIM_SALD_APROX(saldo_aproximado_sucursal
																			.get(plan.getCO_NEGO_SUCU_PLAN_ForJSON()));
																	mapNego_Sucu_Plan.updateSaldoAproximado(plan);
																}
															}
														}
													}
												}

												if (!generaSaldoAprox && (considerarNegocio || esFechaCorteDif)) {
													CIER_DATA_PLAN data = new CIER_DATA_PLAN();
													data.setCO_NEGO_SUCU_PLAN(plan.getCO_NEGO_SUCU_PLAN_ForJSON());
													data.setDE_NOMB(plan.getNO_PLAN_ForJSON());
													data.setFE_INIC(inter[0]);
													data.setFE_FIN(inter[1]);
													data.setCO_MONE_FACT(
															plan.getPlan_ForJSON().getCO_MONE_FACT_ForJSON());
													data.setIM_MONT(dev);
													data.setST_TIPO_DEVO(1);// desactivacion
													retornar_facturar_planes.add(data);

													// cobrar-=dev;
												}

												if (log_view)
													System.out.println("devolver 4 de " + FechaHora.gF(inter[0]) + " a "
															+ FechaHora.gF(inter[1]) + " devolver " + dev);
											}
										}

									}

								}

								// P3A*****
								// Proceso de devolucion antes de FD
								for (CIER_DATA_PLAN plane_ya_cobrado : mi_plan_ya_cobrados) {
									Date mi_plan_inicio = plane_ya_cobrado.getFE_INIC();
									Date mi_plan_fin = plane_ya_cobrado.getFE_FIN();
									if (mi_plan_inicio.getTime() < FE_FIN.getTime()) {
										if (mi_plan_fin.getTime() > FE_FIN.getTime())
											mi_plan_fin = FE_FIN;
										Date mi_plan_fecha[] = new Date[] { mi_plan_inicio, mi_plan_fin };
										for (Date sin_servicio[] : periodoCORSUP) {
											Date inter[] = FechaHora.getFechaIntersecta(mi_plan_fecha, new Date[] {
													sin_servicio[0], FechaHora.addDays(sin_servicio[1], -1) });
											if (inter != null) {
												Double devolucion_por_promocion = this
														.getMontoPorDevolucionPorPromocionEntrePeriodos(
																planes_devoluciones_por_promociones, inter[0],
																inter[1]);

												Double nd = (double) FechaHora.getNumDaysOfMonth(inter[0]);
												Double ppd = plan.getIM_MONTO_ForJSON() / nd;
												Double dev = redondearPorDefecto(
														((double) FechaHora.getDiffDays(inter[0], inter[1])) * ppd
																- devolucion_por_promocion);
												// &&
												// plan.getCO_NEGO_SUCU_PLAN_CESI_CONT_HIJO_ForJSON()!=null
												boolean esFechaCorteDif = false;
												if (this.saldo_cero != null) {
													if (this.saldo_cero.getFE_CORTE_ForJSON()
															.getTime() != sin_servicio[0].getTime()) {
														esFechaCorteDif = true;
													}
												}

												if (considerarNegocio || esFechaCorteDif) {
													if (sin_servicio[1].getTime() == FechaHora.FECHA_MAXIMA_SI_NO_TIENE
															.getTime() && !sucursal.isDesactivado_ForJSON()) {
														// saldo aproximado
														if (!saldo_aproximado_sucursal
																.containsKey(plan.getCO_NEGO_SUCU_PLAN_ForJSON())) {
															saldo_aproximado_sucursal
																	.put(plan.getCO_NEGO_SUCU_PLAN_ForJSON(), 0d);
														}
														saldo_aproximado_sucursal
																.put(plan.getCO_NEGO_SUCU_PLAN_ForJSON(),
																		saldo_aproximado_sucursal.get(
																				plan.getCO_NEGO_SUCU_PLAN_ForJSON())
																				+ dev);

														// P2B1*****
														// GUARDAMOS SALDO
														// APROXIMADO si no es
														// preview
														if (!this.preview) {
															plan.setIM_SALD_APROX(saldo_aproximado_sucursal
																	.get(plan.getCO_NEGO_SUCU_PLAN_ForJSON()));
															mapNego_Sucu_Plan.updateSaldoAproximado(plan);
														}
													} else {
														CIER_DATA_PLAN data = new CIER_DATA_PLAN();
														data.setCO_NEGO_SUCU_PLAN(plan.getCO_NEGO_SUCU_PLAN_ForJSON());
														data.setDE_NOMB(plan.getNO_PLAN_ForJSON());
														data.setFE_INIC(inter[0]);
														data.setFE_FIN(inter[1]);
														data.setCO_MONE_FACT(
																plan.getPlan_ForJSON().getCO_MONE_FACT_ForJSON());
														data.setIM_MONT(dev);
														data.setST_TIPO_DEVO(2);// corte
																				// o
																				// suspension
														retornar_facturar_planes.add(data);

														// cobrar-=dev;
													}
												}

												if (log_view)
													System.out.println("devolver 5 de " + FechaHora.gF(inter[0]) + " a "
															+ FechaHora.gF(inter[1]) + " devolver " + dev);
											}
										}
									}
								}
							}
							/*
							 * } else { //Evalua los saldos aproximados Double
							 * monto=plan.getIM_SALD_APROX_ForJSON(); if
							 * (!saldo_aproximado_sucursal.containsKey(plan.
							 * getCO_NEGO_SUCU_PLAN_ForJSON())){
							 * saldo_aproximado_sucursal.put(plan.
							 * getCO_NEGO_SUCU_PLAN_ForJSON(), 0d); }
							 * saldo_aproximado_sucursal.put(plan.
							 * getCO_NEGO_SUCU_PLAN_ForJSON(),
							 * saldo_aproximado_sucursal.get(plan.
							 * getCO_NEGO_SUCU_PLAN_ForJSON())+monto); }
							 */
						} else {
							// P1*****
							for (CIER_DATA_PLAN data_plan : mi_plan_ya_cobrados) {
								for (Date periodo[] : periodoCORSUP) {
									Date limite[] = FechaHora.getFechaIntersecta(
											new Date[] { data_plan.getFE_INIC(), data_plan.getFE_FIN() },
											new Date[] { periodo[0], FechaHora.addDays(periodo[1], -1) });
									if (limite != null) {
										Double devolucion_por_promocion = this
												.getMontoPorDevolucionPorPromocionEntrePeriodos(
														planes_devoluciones_por_promociones, limite[0], limite[1]);

										Double nd = (double) FechaHora.getDiffDays(data_plan.getFE_INIC(),
												data_plan.getFE_FIN());// (double)FechaHora.getNumDaysOfMonth(periodo[0]);
										Double ppd = data_plan.getIM_MONT() / nd;
										Double dev = redondearPorDefecto(
												((double) FechaHora.getDiffDays(limite[0], limite[1])) * ppd
														- devolucion_por_promocion);

										boolean esFechaCorteDif = false;
										if (this.saldo_cero != null) {
											if (this.saldo_cero.getFE_CORTE_ForJSON().getTime() != periodo[0]
													.getTime()) {
												esFechaCorteDif = true;
											}
										}

										if (considerarNegocio || esFechaCorteDif) {
											if (periodo[1].getTime() == FechaHora.FECHA_MAXIMA_SI_NO_TIENE.getTime()
													&& !sucursal.isDesactivado_ForJSON()) {
												// saldo aproximado
												if (!saldo_aproximado_sucursal
														.containsKey(plan.getCO_NEGO_SUCU_PLAN_ForJSON())) {
													saldo_aproximado_sucursal.put(plan.getCO_NEGO_SUCU_PLAN_ForJSON(),
															0d);
												}
												saldo_aproximado_sucursal.put(plan.getCO_NEGO_SUCU_PLAN_ForJSON(),
														saldo_aproximado_sucursal
																.get(plan.getCO_NEGO_SUCU_PLAN_ForJSON()) + dev);

												// P2B1*****
												// GUARDAMOS SALDO APROXIMADO si
												// no es preview
												if (!this.preview) {
													plan.setIM_SALD_APROX(saldo_aproximado_sucursal
															.get(plan.getCO_NEGO_SUCU_PLAN_ForJSON()));
													mapNego_Sucu_Plan.updateSaldoAproximado(plan);
												}
											} else {
												if (sucursal.isDesactivadoEnPresentCierre()) {
													CIER_DATA_PLAN data = new CIER_DATA_PLAN();
													data.setCO_NEGO_SUCU_PLAN(plan.getCO_NEGO_SUCU_PLAN_ForJSON());
													data.setDE_NOMB(plan.getNO_PLAN_ForJSON());
													data.setFE_INIC(limite[0]);
													data.setFE_FIN(limite[1]);
													data.setCO_MONE_FACT(data_plan.getCO_MONE_FACT());// Proceso
																										// de
																										// cambio
																										// de
																										// tarifa
																										// *****
													data.setIM_MONT(dev);
													data.setST_TIPO_DEVO(2);// corte
																			// o
																			// suspension
													retornar_facturar_planes.add(data);
												}
											}
										}

										// cobrar-=dev;
										if (log_view)
											System.out.println("devolver 2 " + FechaHora.gF(limite[0]) + " - "
													+ FechaHora.gF(limite[1]) + " devolver " + dev);

									}

								}
							}

						}
					}
				}
			}
		}
		return retornar_facturar_por_sucursal_planes;
	}

	public Map<CIER_DATA_SUCU, List<CIER_DATA_SERV_SUPL>> facturarServiciosSuplementarios() {

		Date fecha_real_inicio = null;

		if (cier == null) {

			Calendar cal = Calendar.getInstance();
			int year = cal.get(Calendar.YEAR);
			int mes = cal.get(Calendar.MONTH);

			fecha_real_inicio = FechaHora.getDate(year, mes - 1, 1);

		} else {

			fecha_real_inicio = FechaHora.getDate(cier.getNU_ANO_ForJSON(), cier.getNU_PERI_ForJSON() - 1, 1);

		}

		if (this.nego.isTipoFacturacionVencida())
			fecha_real_inicio = FechaHora.addMonths(fecha_real_inicio, -1);
		Date fecha_real_fin = FechaHora.getUltimaFechaMes(fecha_real_inicio);
		Map<CIER_DATA_SUCU, List<CIER_DATA_SERV_SUPL>> retornar_facturar_por_sucursal_sss = new HashMap<CIER_DATA_SUCU, List<CIER_DATA_SERV_SUPL>>();
		List<NEGO_PROM> promociones_negocios = new ArrayList<NEGO_PROM>();// nego.getPromocionesPorcentajePendientes(cier);
		double cobrar = 0;
		for (NEGO_SUCU sucursal : sucursales) {
			if (sucursal.getCO_NEGO_SUCU_CESI_CONT_HIJO_ForJSON() == null) {

				List<NEGO_SUCU_PROM> promociones_sucursal_porcentaje = null;

				if (cier != null) {
					promociones_sucursal_porcentaje = sucursal.getPromocionesPorcentajePendientes(cier);
				}

				List<CIER_DATA_SERV_SUPL> retornar_facturar_sss = new ArrayList<CIER_DATA_SERV_SUPL>();
				retornar_facturar_por_sucursal_sss.put(sucursales_data.get(sucursal), retornar_facturar_sss);
				SUCU sucu = sucursal.getSUCU();
				List<SUSP> suspensiones = sucursal.getSuspensionesPendientes_Fact();
				List<SUSP> suspensiones_pend = sucursal.getSuspensionesPendientes();
				List<Date[]> periodoCORSUP = FechaHora.getPeriodosUnionCortesSuspensiones(cortes, suspensiones);
				List<NEGO_SUCU_SERV_SUPL> sss = sucursal.getServiciosSuplementarios();
				if (log_view)
					System.out.println(">" + sucursal.getCO_OIT_INST_ForJSON() + " >  " + sucu.getDE_DIRE_ForJSON());
				for (NEGO_SUCU_SERV_SUPL ss : sss) {
					if (log_view)
						System.out.println("ss " + ss.getNO_SERV_SUPL_ForJSON());

					Map<Integer, Double> saldo_aproximado_sucursal = new HashMap<Integer, Double>();
					saldo_aproximado_sss.put(sucursal, saldo_aproximado_sucursal);

					Date UFF = ss.getFE_ULTI_FACT_ForJSON();
					Date FE_FIN = ss.getFE_FIN_ForJSON();
					Date FE_INIC = ss.getFE_INIC_ForJSON();

					List<CIER_DATA_SERV_SUPL> sss_ya_cobrados = null;
					List<CIER_DATA_SERV_SUPL> sss_devoluciones_por_promociones = null;
					List<CIER_DATA_SERV_SUPL> mi_ss_ya_cobrados = null;

					if (cier != null) {
						sss_ya_cobrados = ss.getCIER_DATA_SERVICIOS_SUPL_COBRADOS(cier);
						sss_devoluciones_por_promociones = ss.getCIER_DATA_DEVOLUCIONES_POR_PROMOCIONES(cier);
						mi_ss_ya_cobrados = ss.getCIER_DATA_MI_SERVICIOS_SUPL_COBRADOS(cier);
					}

					if (sucursal.getCO_NEGO_SUCU_CESI_CONT_PADR_ForJSON() != null) {
						NEGO_SUCU_SERV_SUPL ss_cesi_cont = new NEGO_SUCU_SERV_SUPL();
						ss_cesi_cont.setCO_NEGO_SUCU_SERV_SUPL(ss.getCO_NEGO_SUCU_SERV_SUPL_CESI_CONT_PADR_ForJSON());
						ss_cesi_cont.setCO_NEGO_SUCU(sucursal.getCO_NEGO_SUCU_CESI_CONT_PADR_ForJSON());

						List<CIER_DATA_SERV_SUPL> sss_ya_cobrados_cesi_cont = null;
						List<CIER_DATA_SERV_SUPL> sss_devoluciones_por_promociones_cesi_cont = null;
						List<CIER_DATA_SERV_SUPL> mi_ss_ya_cobrados_cesi_cont = null;

						if (cier != null) {
							sss_ya_cobrados_cesi_cont = ss_cesi_cont.getCIER_DATA_SERVICIOS_SUPL_COBRADOS(cier);
							sss_devoluciones_por_promociones_cesi_cont = ss_cesi_cont
									.getCIER_DATA_DEVOLUCIONES_POR_PROMOCIONES(cier);
							mi_ss_ya_cobrados_cesi_cont = ss_cesi_cont.getCIER_DATA_MI_SERVICIOS_SUPL_COBRADOS(cier);
						}

						if (!sss_ya_cobrados_cesi_cont.isEmpty()) {
							sss_ya_cobrados.addAll(sss_ya_cobrados_cesi_cont);
						}
						if (!sss_devoluciones_por_promociones_cesi_cont.isEmpty()) {
							sss_devoluciones_por_promociones.addAll(sss_devoluciones_por_promociones_cesi_cont);
						}
						if (!mi_ss_ya_cobrados_cesi_cont.isEmpty()) {
							mi_ss_ya_cobrados.addAll(mi_ss_ya_cobrados_cesi_cont);
						}
					}

					if (sucursal.getCO_NEGO_SUCU_MUDA_PADR_ForJSON() != null) {
						NEGO_SUCU_SERV_SUPL ss_muda = new NEGO_SUCU_SERV_SUPL();
						ss_muda.setCO_NEGO_SUCU_SERV_SUPL(ss.getCO_NEGO_SUCU_SERV_SUPL_MUDA_PADR_ForJSON());
						ss_muda.setCO_NEGO_SUCU(sucursal.getCO_NEGO_SUCU_MUDA_PADR_ForJSON());

						List<CIER_DATA_SERV_SUPL> sss_ya_cobrados_muda = null;
						List<CIER_DATA_SERV_SUPL> sss_devoluciones_por_promociones_muda = null;
						List<CIER_DATA_SERV_SUPL> mi_ss_ya_cobrados_muda = null;

						if (cier != null) {

							sss_ya_cobrados_muda = ss_muda.getCIER_DATA_SERVICIOS_SUPL_COBRADOS(cier);
							sss_devoluciones_por_promociones_muda = ss_muda
									.getCIER_DATA_DEVOLUCIONES_POR_PROMOCIONES(cier);
							mi_ss_ya_cobrados_muda = ss_muda.getCIER_DATA_MI_SERVICIOS_SUPL_COBRADOS(cier);

						}

						if (sss_ya_cobrados_muda != null) {
							if (!sss_ya_cobrados_muda.isEmpty()) {
								sss_ya_cobrados.addAll(sss_ya_cobrados_muda);
							}
						}

						if (sss_devoluciones_por_promociones_muda != null) {
							if (!sss_devoluciones_por_promociones_muda.isEmpty()) {
								sss_devoluciones_por_promociones.addAll(sss_devoluciones_por_promociones_muda);
							}
						}

						if (mi_ss_ya_cobrados_muda != null) {
							if (!mi_ss_ya_cobrados_muda.isEmpty()) {
								mi_ss_ya_cobrados.addAll(mi_ss_ya_cobrados_muda);
							}
						}
					}

					List<Date[]> periodos_sss_ya_cobrados = new ArrayList<Date[]>();

					if (sss_ya_cobrados != null) {

						for (CIER_DATA_SERV_SUPL ss_ya_cobrado : sss_ya_cobrados) {
							periodos_sss_ya_cobrados
									.add(new Date[] { ss_ya_cobrado.getFE_INIC(), ss_ya_cobrado.getFE_FIN() });
						}
					}

					if (UFF == null)
						UFF = FE_INIC;

					if (FE_FIN == null || !FE_FIN.equals(UFF)) {
						// if (!(this.nego.isCortado() ||
						// sucursal.isSuspendido_ForJSON()) ||
						// periodoCORSUP.size()>0){
						if (FE_FIN == null || FE_FIN.getTime() > UFF.getTime()) {
							// P2*****
							// Generar Periodos de Facturacion
							Date periodo_inicio = fecha_real_inicio;
							Date periodo_fin = fecha_real_fin;
							Date nueva_ultima_facturacion = null;
							if (UFF.getTime() == FE_INIC.getTime())
								periodo_inicio = UFF;
							// else
							// periodo_inicio=FechaHora.addDays(UFF, 1);
							if (FE_FIN != null) {
								if (periodo_fin.getTime() > FE_FIN.getTime()) {
									periodo_fin = FE_FIN;
								}
							}
							// P2A*****
							// Se cobrar los complementos
							List<Date[]> complementos = FechaHora.getQuitarCortesSuspensionesNoConsiderarReconexion(
									new Date[] { periodo_inicio, periodo_fin }, periodoCORSUP);
							for (Date[] complemento : complementos) {
								List<Date[]> periodos = this.getPeriodos(complemento[0], complemento[1]);
								for (Date[] periodo : periodos) {
									// Aqui se tiene que carlcular los
									// diferencias y los montos a cobrar si no
									// se han cobrado en otro plan

									List<Date[]> no_se_cobrars = FechaHora.getQuitar(periodo, periodos_sss_ya_cobrados);
									// Estos son los planes que no han pagado
									for (Date no_se_cobrar[] : no_se_cobrars) {
										Double nd = (double) FechaHora.getNumDaysOfMonth(no_se_cobrar[0]);
										Double ppd = ss.getIM_MONTO_ForJSON() / nd;
										Double dias_considerar = (double) FechaHora.getDiffDays(no_se_cobrar[0],
												no_se_cobrar[1]);
										Double cob = redondearPorDefecto(dias_considerar * ppd);
										if (log_view)
											System.out.println("cobrar 1 de " + FechaHora.gF(no_se_cobrar[0]) + " a "
													+ FechaHora.gF(no_se_cobrar[1]) + "  pagar " + cob);

										CIER_DATA_SERV_SUPL data = new CIER_DATA_SERV_SUPL();
										data.setCO_NEGO_SUCU_SERV_SUPL(ss.getCO_NEGO_SUCU_SERV_SUPL_ForJSON());
										data.setDE_NOMB(ss.getNO_SERV_SUPL_ForJSON());
										data.setFE_INIC(no_se_cobrar[0]);
										data.setFE_FIN(no_se_cobrar[1]);
										data.setIM_MONT(cob);
										data.setST_TIPO_COBR(1);
										data.setST_AFEC_DETR(ss.getST_AFEC_DETR_ForJSON());
										data.setCO_MONE_FACT(ss.getServ_Supl_ForJSON().getCO_MONE_FACT_ForJSON());
										retornar_facturar_sss.add(data);

										cobrar += cob;

										Double dev = this.getPromocionServicioSuplementario(nd, dias_considerar, cob,
												no_se_cobrar[0], promociones_negocios, promociones_sucursal_porcentaje);
										if (dev != null) {
											data = new CIER_DATA_SERV_SUPL();
											data.setCO_NEGO_SUCU_SERV_SUPL(ss.getCO_NEGO_SUCU_SERV_SUPL_ForJSON());
											data.setDE_NOMB(ss.getNO_SERV_SUPL_ForJSON());
											data.setFE_INIC(no_se_cobrar[0]);
											data.setFE_FIN(no_se_cobrar[1]);
											data.setIM_MONT(dev);
											data.setST_TIPO_DEVO(4);
											data.setST_AFEC_DETR(ss.getST_AFEC_DETR_ForJSON());
											data.setCO_MONE_FACT(ss.getServ_Supl_ForJSON().getCO_MONE_FACT_ForJSON());
											retornar_facturar_sss.add(data);

											if (log_view)
												System.out.println("devolver 6 de " + FechaHora.gF(no_se_cobrar[0])
														+ " a " + FechaHora.gF(no_se_cobrar[1]) + "  devolver " + dev);
											cobrar += cob;
										}

										if (nueva_ultima_facturacion == null) {
											nueva_ultima_facturacion = no_se_cobrar[1];
										} else if (nueva_ultima_facturacion.getTime() < no_se_cobrar[1].getTime()) {
											nueva_ultima_facturacion = no_se_cobrar[1];
										}

									}
									// no se cobran diferenciales por que son SS
								}
							}
							// P2B*****
							// Se calculas las devoluciones
							for (Date[] periodo_devolucion : periodoCORSUP) {
								for (CIER_DATA_SERV_SUPL ss_ya_cobrado : mi_ss_ya_cobrados) {
									Date inter[] = FechaHora.getFechaIntersecta(
											new Date[] { periodo_devolucion[0],
													FechaHora.addDays(periodo_devolucion[1], -1) },
											new Date[] { ss_ya_cobrado.getFE_INIC(), ss_ya_cobrado.getFE_FIN() });
									if (inter != null) {
										Double devolucion_por_promocion = this.getMontoPorDevolucionPorPromocionSS(
												sss_devoluciones_por_promociones, ss_ya_cobrado.getFE_INIC(),
												ss_ya_cobrado.getFE_FIN());
										double dias;
										// if
										// (periodo_devolucion[1].getTime()==inter[1].getTime())
										// dias=FechaHora.getDiffDays(ss_ya_cobrado.getFE_INIC(),
										// FechaHora.addDays(ss_ya_cobrado.getFE_FIN(),1));
										// else
										dias = FechaHora.getDiffDays(ss_ya_cobrado.getFE_INIC(),
												ss_ya_cobrado.getFE_FIN());
										double ppd = 0;
										if (devolucion_por_promocion == null)
											ppd = ss_ya_cobrado.getIM_MONT() / dias;
										else
											ppd = (ss_ya_cobrado.getIM_MONT() - devolucion_por_promocion) / dias;
										double monto = redondearPorDefecto(
												FechaHora.getDiffDays(inter[0], inter[1]) * ppd);

										if (log_view)
											System.out.println("devolver 1 de " + FechaHora.gF(inter[0]) + " a "
													+ FechaHora.gF(inter[1]) + "  devolver " + monto);

										boolean esFechaCorteDif = false;
										if (this.saldo_cero != null) {
											if (this.saldo_cero.getFE_CORTE_ForJSON().getTime() != periodo_devolucion[0]
													.getTime()) {
												esFechaCorteDif = true;
											}
										}

										if (this.considerarNego || esFechaCorteDif) {
											if (periodo_devolucion[1].getTime() != FechaHora.FECHA_MAXIMA_SI_NO_TIENE
													.getTime() || sucursal.isDesactivado_ForJSON()) {
												CIER_DATA_SERV_SUPL data = new CIER_DATA_SERV_SUPL();
												data.setCO_NEGO_SUCU_SERV_SUPL(ss.getCO_NEGO_SUCU_SERV_SUPL_ForJSON());
												data.setDE_NOMB(ss.getNO_SERV_SUPL_ForJSON());
												data.setFE_INIC(inter[0]);
												data.setFE_FIN(inter[1]);
												data.setIM_MONT(monto);
												data.setST_TIPO_DEVO(2);// corte
																		// o
																		// suspension
												data.setST_AFEC_DETR(ss.getST_AFEC_DETR_ForJSON());
												data.setCO_MONE_FACT(ss_ya_cobrado.getCO_MONE_FACT());// Proceso
																										// de
																										// cambio
																										// de
																										// tarifa
																										// *****
												retornar_facturar_sss.add(data);

												cobrar -= monto;
											} else {
												// saldo aproximado
												if (!saldo_aproximado_sucursal
														.containsKey(ss.getCO_NEGO_SUCU_SERV_SUPL_ForJSON())) {
													saldo_aproximado_sucursal
															.put(ss.getCO_NEGO_SUCU_SERV_SUPL_ForJSON(), 0d);
												}
												saldo_aproximado_sucursal
														.put(ss.getCO_NEGO_SUCU_SERV_SUPL_ForJSON(),
																saldo_aproximado_sucursal
																		.get(ss.getCO_NEGO_SUCU_SERV_SUPL_ForJSON())
																		+ monto);

												// P2B1*****
												// GUARDAMOS SALDO APROXIMADO si
												// no es preview
												if (!this.preview) {
													ss.setIM_SALD_APROX(saldo_aproximado_sucursal
															.get(ss.getCO_NEGO_SUCU_SERV_SUPL_ForJSON()));
													mapNego_Sucu_Serv_Supl.updateSaldoAproximado(ss);
												}
											}
										}

										if (nueva_ultima_facturacion == null) {
											nueva_ultima_facturacion = inter[1];
										} else if (nueva_ultima_facturacion.getTime() < inter[1].getTime()) {
											nueva_ultima_facturacion = inter[1];
										}
									}
								}
							}
							// P2C*****
							// Actualizamos la UFF_SGTE del plan con >>
							// periodo_fin
							if (!this.preview) {
								ss.setFE_ULTI_FACT_SGTE(nueva_ultima_facturacion);
								mapNego_Sucu_Serv_Supl.saveSiguienteUltimaFacturacion(ss);
							}
						} else {
							// P3*****
							// Proceso de Devolucion por Desactivacion
							for (Date[] periodo : this.getPeriodos(FechaHora.addDays(FE_FIN, 1), UFF)) {
								Double ppd = ss.getIM_MONTO_ForJSON() / FechaHora.getNumDaysOfMonth(periodo[0]);
								Double dev = redondearPorDefecto(ppd * FechaHora.getDiffDays(periodo[0], periodo[1]));

								CIER_DATA_SERV_SUPL data = new CIER_DATA_SERV_SUPL();
								data.setCO_NEGO_SUCU_SERV_SUPL(ss.getCO_NEGO_SUCU_SERV_SUPL_ForJSON());
								data.setDE_NOMB(ss.getNO_SERV_SUPL_ForJSON());
								data.setFE_INIC(periodo[0]);
								data.setFE_FIN(periodo[1]);
								data.setIM_MONT(dev);
								data.setST_TIPO_DEVO(1);// desactivacion
								data.setST_AFEC_DETR(ss.getST_AFEC_DETR_ForJSON());
								data.setCO_MONE_FACT(ss.getServ_Supl_ForJSON().getCO_MONE_FACT_ForJSON());
								retornar_facturar_sss.add(data);

								if (log_view)
									System.out.println("devolver 4 de " + FechaHora.gF(periodo[0]) + " a "
											+ FechaHora.gF(periodo[1]) + " devolver " + dev);
								cobrar -= dev;
							}

							// P3A*****
							// Proceso de devolucion antes de FD
							for (CIER_DATA_SERV_SUPL ss_ya_cobrado : mi_ss_ya_cobrados) {
								Date mi_plan_inicio = ss_ya_cobrado.getFE_INIC();
								Date mi_plan_fin = ss_ya_cobrado.getFE_FIN();
								if (mi_plan_inicio.getTime() < FE_FIN.getTime()) {
									if (mi_plan_fin.getTime() > FE_FIN.getTime())
										mi_plan_fin = FE_FIN;
									Date mi_plan_fecha[] = new Date[] { mi_plan_inicio, mi_plan_fin };
									for (Date sin_servicio[] : periodoCORSUP) {
										Date inter[] = FechaHora.getFechaIntersecta(mi_plan_fecha,
												new Date[] { sin_servicio[0], FechaHora.addDays(sin_servicio[1], -1) });
										if (inter != null) {
											Double devolucion_por_promocion = this
													.getMontoPorDevolucionPorPromocionEntrePeriodosSS(
															sss_devoluciones_por_promociones, inter[0], inter[1]);

											Double nd = (double) FechaHora.getNumDaysOfMonth(inter[0]);
											Double ppd = ss.getIM_MONTO_ForJSON() / nd;
											Double dev = redondearPorDefecto(
													((double) FechaHora.getDiffDays(inter[0], inter[1])) * ppd
															- devolucion_por_promocion);

											CIER_DATA_SERV_SUPL data = new CIER_DATA_SERV_SUPL();
											data.setCO_NEGO_SUCU_SERV_SUPL(ss.getCO_NEGO_SUCU_SERV_SUPL_ForJSON());
											data.setDE_NOMB(ss.getNO_SERV_SUPL_ForJSON());
											data.setFE_INIC(inter[0]);
											data.setFE_FIN(inter[1]);
											data.setIM_MONT(dev);
											data.setST_TIPO_DEVO(2);// corte o
																	// suspension
											data.setST_AFEC_DETR(ss.getST_AFEC_DETR_ForJSON());
											data.setCO_MONE_FACT(ss.getServ_Supl_ForJSON().getCO_MONE_FACT_ForJSON());
											retornar_facturar_sss.add(data);

											if (log_view)
												System.out.println("devolver 5 de " + FechaHora.gF(inter[0]) + " a "
														+ FechaHora.gF(inter[1]) + " devolver " + dev);
											cobrar -= dev;
										}
									}
								}
							}
						}
						/*
						 * } else { //Evalua los saldos aproximados Double
						 * monto=ss.getIM_SALD_APROX_ForJSON(); if
						 * (!saldo_aproximado_sucursal.containsKey(ss.
						 * getCO_NEGO_SUCU_SERV_SUPL_ForJSON())){
						 * saldo_aproximado_sucursal.put(ss.
						 * getCO_NEGO_SUCU_SERV_SUPL_ForJSON(), 0d); }
						 * saldo_aproximado_sucursal.put(ss.
						 * getCO_NEGO_SUCU_SERV_SUPL_ForJSON(),
						 * saldo_aproximado_sucursal.get(ss.
						 * getCO_NEGO_SUCU_SERV_SUPL_ForJSON())+monto); }
						 */
					} else {
						// P1*****
						for (CIER_DATA_SERV_SUPL data_ss : mi_ss_ya_cobrados) {
							for (Date periodo[] : periodoCORSUP) {
								Date limite[] = FechaHora.getFechaIntersecta(
										new Date[] { data_ss.getFE_INIC(), data_ss.getFE_FIN() },
										new Date[] { periodo[0], FechaHora.addDays(periodo[1], -1) });
								if (limite != null) {
									Double devolucion_por_promocion = this
											.getMontoPorDevolucionPorPromocionEntrePeriodosSS(
													sss_devoluciones_por_promociones, limite[0], limite[1]);

									Double nd = (double) FechaHora.getDiffDays(data_ss.getFE_INIC(),
											data_ss.getFE_FIN());// (double)FechaHora.getNumDaysOfMonth(periodo[0]);
									Double ppd = data_ss.getIM_MONT() / nd;
									Double dev = redondearPorDefecto(
											((double) FechaHora.getDiffDays(limite[0], limite[1])) * ppd
													- devolucion_por_promocion);

									boolean esFechaCorteDif = false;
									if (this.saldo_cero != null) {
										if (this.saldo_cero.getFE_CORTE_ForJSON().getTime() != periodo[0].getTime()) {
											esFechaCorteDif = true;
										}
									}

									if (this.considerarNego || esFechaCorteDif) {
										if (periodo[1].getTime() == FechaHora.FECHA_MAXIMA_SI_NO_TIENE.getTime()
												&& !sucursal.isDesactivado_ForJSON()) {
											// saldo aproximado
											if (!saldo_aproximado_sucursal
													.containsKey(ss.getCO_NEGO_SUCU_SERV_SUPL_ForJSON())) {
												saldo_aproximado_sucursal.put(ss.getCO_NEGO_SUCU_SERV_SUPL_ForJSON(),
														0d);
											}
											saldo_aproximado_sucursal.put(ss.getCO_NEGO_SUCU_SERV_SUPL_ForJSON(),
													saldo_aproximado_sucursal
															.get(ss.getCO_NEGO_SUCU_SERV_SUPL_ForJSON()) + dev);

											// P2B1*****
											// GUARDAMOS SALDO APROXIMADO si no
											// es preview
											if (!this.preview) {
												ss.setIM_SALD_APROX(saldo_aproximado_sucursal
														.get(ss.getCO_NEGO_SUCU_SERV_SUPL_ForJSON()));
												mapNego_Sucu_Serv_Supl.updateSaldoAproximado(ss);
											}
										} else {
											if (sucursal.isDesactivadoEnPresentCierre()) {
												CIER_DATA_SERV_SUPL data = new CIER_DATA_SERV_SUPL();
												data.setCO_NEGO_SUCU_SERV_SUPL(ss.getCO_NEGO_SUCU_SERV_SUPL_ForJSON());
												data.setDE_NOMB(ss.getNO_SERV_SUPL_ForJSON());
												data.setFE_INIC(limite[0]);
												data.setFE_FIN(limite[1]);
												data.setIM_MONT(dev);
												data.setST_TIPO_DEVO(2);// corte
																		// o
																		// suspension
												data.setST_AFEC_DETR(ss.getST_AFEC_DETR_ForJSON());
												data.setCO_MONE_FACT(data_ss.getCO_MONE_FACT());// Proceso
																								// de
																								// cambio
																								// de
																								// tarifa
																								// *****
												retornar_facturar_sss.add(data);
											}
										}
									}

									cobrar -= dev;
									if (log_view)
										System.out.println("devolver 2 " + FechaHora.gF(limite[0]) + " - "
												+ FechaHora.gF(limite[1]) + " devolver " + dev);

								}

							}
						}

					}
				}
			}
		}
		return retornar_facturar_por_sucursal_sss;
	}

	public void savePdf(HttpServletResponse response) {
		lista_recibo.savePdf(response);
	}

	public Double getTotalSaldoAproximado() {
		// Retornar el valor en positivo
		Double r = 0d;
		for (Entry<NEGO_SUCU, Map<Integer, Double>> ea : saldo_aproximado_planes.entrySet()) {
			for (Entry<Integer, Double> eb : ea.getValue().entrySet()) {
				// Por el cambio de moneda
				r += redondearPorDefecto(eb.getValue());
				if (r != 0d) {
					NEGO_SUCU_PLAN nego_sucu_plan = mapNego_Sucu_Plan.getId(eb.getKey());
					if (nego_sucu_plan.getPlan_ForJSON().getCO_MONE_FACT_ForJSON() != null
							&& !nego_sucu_plan.getPlan_ForJSON().getCO_MONE_FACT_ForJSON()
									.equals(this.moneda_facturacion.getCO_MONE_FACT_ForJSON())) {
						if (this.moneda_facturacion.esSoles())
							r *= redondearPorDefecto(this.cier.getNU_TIPO_CAMB_ForJSON());
						else
							r /= redondearPorDefecto(this.cier.getNU_TIPO_CAMB_ForJSON());
					}
				}

			}
		}
		for (Entry<NEGO_SUCU, Map<Integer, Double>> ea : saldo_aproximado_sss.entrySet()) {
			for (Entry<Integer, Double> eb : ea.getValue().entrySet()) {
				r += redondearPorDefecto(eb.getValue());
				if (r != 0d) {
					NEGO_SUCU_SERV_SUPL nego_sucu_serv_supl = mapNego_Sucu_Serv_Supl.getId(eb.getKey());
					if (nego_sucu_serv_supl.getServ_Supl_ForJSON().getCO_MONE_FACT_ForJSON() != null
							&& !nego_sucu_serv_supl.getServ_Supl_ForJSON().getCO_MONE_FACT_ForJSON()
									.equals(this.moneda_facturacion.getCO_MONE_FACT_ForJSON())) {
						if (this.moneda_facturacion.esSoles())
							r *= redondearPorDefecto(this.cier.getNU_TIPO_CAMB_ForJSON());
						else
							r /= redondearPorDefecto(this.cier.getNU_TIPO_CAMB_ForJSON());
					}
				}
			}
		}

		return r;
	}

	private void calcularRecibos(String producto, Map<CIER_DATA_SUCU, Object[]> listaPlanesySsPorSucursal) {

		/* calculamos periodos */
		Date fecha_minima = null;
		Date fecha_maxima = null;
		Double total_devoluciones = 0d;

		for (Entry<CIER_DATA_SUCU, List<NEGO_SUCU_SERV_UNIC>> entry : servicios_unicos.entrySet()) {
			Map<NEGO_SUCU_SERV_UNIC, CIER_DATA_SERV_UNIC> map = new HashMap<NEGO_SUCU_SERV_UNIC, CIER_DATA_SERV_UNIC>();
			for (NEGO_SUCU_SERV_UNIC su : entry.getValue()) {
				CIER_DATA_SERV_UNIC dsu = new CIER_DATA_SERV_UNIC();
				dsu.setCO_NEGO_SUCU_SERV_UNIC(su.getCO_NEGO_SUCU_SERV_UNIC_ForJSON());
				dsu.setDE_NOMB(su.getNO_SERV_UNIC_ForJSON());
				dsu.setIM_MONT(redondearPorDefecto(su.getIM_MONTO_ForJSON()));
				dsu.setST_AFEC_DETR(su.getST_AFEC_DETR_ForJSON());
				map.put(su, dsu);
			}
			servicios_unicos_data.put(entry.getKey(), map);
		}

		/* Calcula monto de devolucion de plan y ss */
		for (Entry<CIER_DATA_SUCU, Object[]> entry : listaPlanesySsPorSucursal.entrySet()) {

			List<CIER_DATA_PLAN> planes = (List<CIER_DATA_PLAN>) entry.getValue()[0];
			List<CIER_DATA_SERV_SUPL> sss = (List<CIER_DATA_SERV_SUPL>) entry.getValue()[1];

			for (CIER_DATA_PLAN plan : planes) {
				if (fecha_minima == null || fecha_minima.getTime() > plan.getFE_INIC().getTime())
					fecha_minima = plan.getFE_INIC();
				if (fecha_maxima == null || fecha_maxima.getTime() < plan.getFE_FIN().getTime())
					fecha_maxima = plan.getFE_FIN();
				if (plan.getST_TIPO_DEVO() != null && plan.getST_TIPO_DEVO() != 4) {
					Double m = Math.abs(plan.getIM_MONT());
					if (!plan.getCO_MONE_FACT().equals(this.moneda_facturacion.getCO_MONE_FACT_ForJSON())) {
						if (this.moneda_facturacion.esSoles()) {
							m *= this.cier.getNU_TIPO_CAMB_ForJSON();
						} else {
							m /= this.cier.getNU_TIPO_CAMB_ForJSON();
						}
					}
					total_devoluciones += m;
				}
			}

			for (CIER_DATA_SERV_SUPL ss : sss) {
				if (fecha_minima == null || fecha_minima.getTime() > ss.getFE_INIC().getTime())
					fecha_minima = ss.getFE_INIC();
				if (fecha_maxima == null || fecha_maxima.getTime() < ss.getFE_FIN().getTime())
					fecha_maxima = ss.getFE_FIN();
				if (ss.getST_TIPO_DEVO() != null && ss.getST_TIPO_DEVO() != 4) {
					Double m = Math.abs(ss.getIM_MONT());
					if (!ss.getCO_MONE_FACT().equals(this.moneda_facturacion.getCO_MONE_FACT_ForJSON())) {
						if (this.moneda_facturacion.esSoles()) {
							m *= this.cier.getNU_TIPO_CAMB_ForJSON();
						} else {
							m /= this.cier.getNU_TIPO_CAMB_ForJSON();
						}
					}
					total_devoluciones += m;
				}
			}
		}

		if (fecha_minima != null && fecha_maxima != null) {

			fecha_minima = FechaHora.getPrimeraFechaMes(fecha_minima);
			fecha_maxima = FechaHora.getUltimaFechaMes(fecha_maxima);
			List<Date[]> periodos = this.getPeriodos(fecha_minima, fecha_maxima);
			this.saldo = this.saldo_negocio + total_devoluciones;// se maneja en
																	// positivo

			Recibo recibo = null;

			Map<Recibo, Map<CIER_DATA_SUCU, List<CIER_DATA_PLAN>>> planes_diferenciales_por_upgrade = null;
			for (int i = 0; i < periodos.size(); i++) {
				// Date[] periodo=periodos.get(periodos.size()-(i+1));
				Date[] periodo = periodos.get(i);
				if (recibo != null) {
					planes_diferenciales_por_upgrade = recibo.getPlanes_diferenciales_por_upgrade();
				}

				if (i == (periodos.size() - 1))
					recibo = new Recibo(this, periodo[0], periodo[1], listaPlanesySsPorSucursal, saldo,
							servicios_unicos, ajustes, planes_diferenciales_por_upgrade, this.cier, this.nego,
							this.cliente, this.sucursal_fiscal, this.sucursal_correspondencia, this.distrito_fiscal,
							this.distrito_correspondencia, this.provincia_fiscal, this.provincia_correspondencia,
							this.departamento_fiscal, this.departamento_correspondencia, this.moneda_facturacion,
							this.tipo_documento, producto);
				else
					recibo = new Recibo(this, periodo[0], periodo[1], listaPlanesySsPorSucursal, saldo,
							new HashMap<CIER_DATA_SUCU, List<NEGO_SUCU_SERV_UNIC>>(), new ArrayList<AJUS>(),
							planes_diferenciales_por_upgrade, this.cier, this.nego, this.cliente, this.sucursal_fiscal,
							this.sucursal_correspondencia, this.distrito_fiscal, this.distrito_correspondencia,
							this.provincia_fiscal, this.provincia_correspondencia, this.departamento_fiscal,
							this.departamento_correspondencia, this.moneda_facturacion, this.tipo_documento, producto);
				this.lista_recibo.addRecibo(recibo);
				// recibo.mostrar();
				this.saldo = recibo.getSaldoQueda();// retornar el valor en
													// positivo
			}
			if (recibo != null) {
				planes_diferenciales_por_upgrade = recibo.getPlanes_diferenciales_por_upgrade();
			}
			// Generar recibos por los upgrades
			// SOLO se generan estos recibos si no hay recibos generados
			if (planes_diferenciales_por_upgrade != null) {
				for (Entry<Recibo, Map<CIER_DATA_SUCU, List<CIER_DATA_PLAN>>> entry_a : planes_diferenciales_por_upgrade
						.entrySet()) {
					// entry_a.getKey().setGeneraPdf(true);
					entry_a.getKey().generar_recibo_diferencial();
				}
			}
		} else {
			// 07-06-2016: Se ha agregado esto porque cuando tiene un saldo
			// pendiente no aparecia ya que no tenia data_facturar
			this.saldo = this.saldo_negocio + total_devoluciones;
		}

		boolean booleanGF = this.generaFactura();

		logger.info("**Genera Factura :" + booleanGF);

		if (booleanGF) {

			Double saldo = this.saldo_negocio + total_devoluciones;
			Factura fact;
			for (Recibo r : this.lista_recibo.getRecibos()) {
				
				if(r.generaFactura == true){

				fact = new Factura(this, this.cier, this.cliente, this.nego, this.moneda_facturacion,
						this.tipo_documento, this.sucursal_fiscal, this.distrito_fiscal, this.provincia_fiscal,
						this.departamento_fiscal, this.sucursal_correspondencia, this.distrito_correspondencia,
						this.provincia_correspondencia, this.departamento_correspondencia, saldo, r.periodo_inicio,
						r.periodo_fin);
				// fact.addPlanes(listaPlanesySsPorSucursal);
				fact.addPlanes(r.getDataPlanesParaFactura());
				fact.addServiciosSuplementarios(r.getDataServiciosSuplementariosParaFactura());
				fact.addServiciosUnicos(r.getDataServiciosUnicosParaFactura());
				fact.addServiciosUnicos(r.getServiciosUnicosParaFactura());
				fact.calcular_totales();
				fact.setPeriodos(r.getPeriodos());
				fact.setGeneraFactura(r.getGeneraFactura());
				this.lista_recibo.addFactura(fact);
				
				}
			}
			// logger.info("**ServiciosSuplementarios size :" +
			// fact.servicios_suplementarios.size());
			// logger.info("**ServiciosUnicos Data size :" +
			// fact.servicios_unicos_data);
			// logger.info("**ServiciosUnicos size :" + fact.servicios_unicos);

		}
		this.enumerarRecibos();
	}

	private void enumerarRecibos() {
		// logger.info("**Semilla Recibo Inicial" + semilla_recibo);
		logger.info("**Semilla Factura Inicial" + semilla_factura);
		// logger.info("**Semilla Boleta Inicial" + getSemilla_boleta());
		this.semilla_recibo_sgte = this.semilla_recibo;
		this.semilla_boleta_sgte = this.semilla_boleta;
		this.semilla_factura_sgte = this.semilla_factura;

		for (Recibo r : this.lista_recibo.getRecibos()) {
			if (r.generaPdf()) {
				if (r.isIsBoleta()) {
					r.setNumeroBoleta(this.semilla_boleta_sgte);
					this.semilla_boleta_sgte++;
				} else {
					r.setNumeroRecibo(this.semilla_recibo_sgte);
					this.semilla_recibo_sgte++;
				}
			}
		}
		for (Factura f : this.lista_recibo.getfacturas()) {
			if (this.generaFactura()) {
				if (f != null) {
					// logger.info("**FACTURAAAAAAAAAAAAAAAA GENERAAAAADDDAAAAAA
					// " + this.semilla_factura_sgte);
					// this.semilla_factura_sgte=this.semilla_factura;
					f.setNumeroFactura(this.semilla_factura_sgte);
					this.semilla_factura_sgte++;
				}
			}
		}
		logger.info("**Semilla Recibo Final" + semilla_recibo_sgte);
		logger.info("**Semilla Factura Final" + semilla_factura_sgte);
		logger.info("**Semilla Boleta Final" + semilla_boleta_sgte);
	}

	/*
	 * private void facturar_Cesion_Contractual_Padre(Boolean
	 * facturar_cesi_cont_padre, Integer CO_NEGO_SUCU_CESI_CONT_PADRE){ NEGO
	 * nego_cc=mapCier.getUnNegocioParaProcesoCierreByCoNegoSucu(
	 * CO_NEGO_SUCU_CESI_CONT_PADRE); PROD prod_cc=nego_cc.getPROD(); CIER
	 * cier_cc=prod_cc.getCierrePendiente(); ProcesoNegocio pn=new
	 * ProcesoNegocio(nego_cc,prod_cc,cier_cc,true,facturar_cesi_cont_padre);
	 * pn.facturar(); if (pn.getSaldo()==0d)
	 * this.data_facturar=pn.getData_facturar();
	 * this.saldo_negocio=this.saldo_negocio+pn.getSaldo();
	 * this.saldo_aproximado_planes=pn.getSaldo_Aproximado_Planes();
	 * this.saldo_aproximado_sss=pn.getSaldo_Aproximado_SSS(); }
	 */
	public Map<NEGO_SUCU, Map<Integer, Double>> getSaldo_Aproximado_Planes() {
		return saldo_aproximado_planes;
	}

	public Map<NEGO_SUCU, Map<Integer, Double>> getSaldo_Aproximado_SSS() {
		return saldo_aproximado_sss;
	}

	public ListaRecibo getLista_Recibo() {
		return lista_recibo;
	}

	public List<NEGO_SUCU> getSucursales() {
		return sucursales;
	}

	public Map<NEGO_SUCU, CIER_DATA_SUCU> getSucursales_data() {
		return sucursales_data;
	}

	public Map<CIER_DATA_SUCU, List<NEGO_SUCU_SERV_UNIC>> getServicios_unicos() {
		return servicios_unicos;
	}

	public Map<CIER_DATA_SUCU, Map<NEGO_SUCU_SERV_UNIC, CIER_DATA_SERV_UNIC>> getServicios_unicos_datas() {
		return servicios_unicos_data;
	}

	public Double getSaldo() {
		return saldo;// retornar el valor en positivo
	}

	// public Map<CIER_DATA_SUCU,Object[]> getData_facturar() {
	// return data_facturar;
	// }

	public int getNumeroRecibosPdfs() {
		return this.lista_recibo.getNumeroRecibosPdfs();
	}

	public void setLog_view(Boolean log_view) {
		this.log_view = log_view;
	}

	public Boolean generaFactura() {
		return this.lista_recibo.generaFactura();
	}

	// public Factura getFactura() {
	// return factura;
	// }

	public Boolean esPreview() {
		return preview;
	}

	public void saveBD() {
		logger.info("Vista Previa : " + this.esPreview());
		if (!this.esPreview()) {
			ProcesoNegocio.mapCier_Data_Nego.insertar(Cier_Data_Nego);
			this.getLista_Recibo().saveBD();
			// guardamos el saldo si ha generado
			if (this.saldo != null && (this.saldo != 0 || this.saldo_negocio != 0)) {
				NEGO_SALD s = new NEGO_SALD();
				s.setCO_CIER(this.cier.getCO_CIER_ForJSON());
				s.setCO_MONE_FACT(this.moneda_facturacion.getCO_MONE_FACT_ForJSON());
				s.setCO_NEGO(this.nego.getCO_NEGO_ForJSON());
				s.setIM_MONT(this.saldo);
				ProcesoNegocio.mapNego_Sald.insertar(s);
			}
		}
	}

	public CIER_DATA_NEGO getCier_Data_Nego() {
		return Cier_Data_Nego;
	}

	public void setSemilla_recibo(Long semilla_recibo) {
		this.semilla_recibo = semilla_recibo;
		this.semilla_recibo_sgte = semilla_recibo;
		;
	}

	public void setSemilla_factura(Long semilla_factura) {
		this.semilla_factura = semilla_factura;
		this.semilla_factura_sgte = semilla_factura;
	}

	public Long getSemilla_recibo_sgte() {
		return semilla_recibo_sgte;
	}

	public Long getSemilla_factura_sgte() {
		return semilla_factura_sgte;
	}

	void savePDF(String ruta) {
		logger.info("Ruta de archivo: " + ruta);
		if (!this.esPreview()) {
			this.getLista_Recibo().savePDF(ruta);
		}
	}

	public Long getSemilla_boleta() {
		return semilla_boleta;
	}

	public void setSemilla_boleta(Long semilla_boleta) {
		this.semilla_boleta = semilla_boleta;
		this.semilla_boleta_sgte = semilla_boleta;
		;

	}

	public Long getSemilla_boleta_sgte() {
		return semilla_boleta_sgte;
	}

	public Map<CIER_DATA_SUCU, Object[]> getListaPlanesySsPorSucursal() {
		return listaPlanesySsPorSucursal;
	}

}
