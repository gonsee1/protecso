/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.reportes;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.faces.convert.BigDecimalConverter;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import com.americatel.facturacion.Utils.Constante;
import com.americatel.facturacion.fncs.ExcelWriter;
import com.americatel.facturacion.fncs.Numero;
import com.americatel.facturacion.mappers.mapCIER;
import com.americatel.facturacion.models.BOLE;
import com.americatel.facturacion.models.CIER;
import com.americatel.facturacion.models.CIER_DATA_PLAN;
import com.americatel.facturacion.models.CLIE;
import com.americatel.facturacion.models.DETALLE_DOCUMENTO;
import com.americatel.facturacion.models.DIST;
import com.americatel.facturacion.models.FACT;
import com.americatel.facturacion.models.NEGO_SUCU_PLAN;
import com.americatel.facturacion.models.PROD;
import com.americatel.facturacion.models.RECI;
import com.americatel.facturacion.models.ReporteCargaTI;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *
 * @author crodas
 */
public class MaestroCargas {
	private List<CIER> listaCier;
	private Integer NU_ANO;
	private Integer NU_PERI;
	private ExcelWriter excel;
	private static mapCIER mapCier;
	
	@Autowired
	public static void setMapCier(mapCIER mapCier) {
		MaestroCargas.mapCier = mapCier;
	}

	public MaestroCargas(List<CIER> listaCier, Integer COD_PROD_PADRE, Integer COD_SERVICIO) {
		this.listaCier = listaCier;
		if (listaCier.size() > 0) {
			this.NU_ANO = listaCier.get(0).getNU_ANO_ForJSON();
			this.NU_PERI = listaCier.get(0).getNU_PERI_ForJSON();
		}
		this.excel = new ExcelWriter();
		this.excel.addHoja("Reporte Cargas");
		// this.calcular();

		String formato = this.getTipoReporte(COD_PROD_PADRE, COD_SERVICIO);

		if (formato.equals("FormatoSatelital")) {
			this.calcular();
		} else if (formato.equals("FormatoH")) {
			this.calcularFormatoH();
		} else if (formato.equals("FormatoI")) {
			this.calcularFormatoI();
		} else if (formato.equals("FormatoB")) {
			this.calcularFormatoB();
		} else if (formato.equals("FormatoC")) {
			this.calcularFormatoC();
		} else if (formato.equals("FormatoF")) {
			this.calcularFormatoF();
		} else if (formato.equals("FormatoG")) {
			this.calcularFormatoG();
		} else if(formato.equals("formatoReporteTI")){
			this.calcularReporteTI(this.NU_ANO,this.NU_PERI);
		}


	}

	public void save(HttpServletResponse response) {
		response.setHeader("Content-Type", "application/vnd.ms-excel");
		response.addHeader("Content-Disposition",
				"attachment; filename=\"Reportes_Cargas_" + NU_ANO + "_" + NU_PERI + "\".xls");
		this.excel.save(response);
	}

	public void calcularFormatoG() {

		this.excel.writeCols(new Object[] { "RECIBO", "FECHA FACTURA", "COD. CLIENTE", "NEGOCIO", "CLIENTE",
				"DIRECCION", "RUC", "TIPO SERVICIO", "MONEDA", " 96: INSTALACION", "96: SERV.", "ALQ.EQUIPOS", "OTROS",
				"SUB TOTAL", "IGV", "TOTAL FACTURA", "FECHA VCTO.", "T.D.Ref.", "N.Doc.Ref.", "T.Cambio Doc Ref",
				"INTERESES (no afecto a IGV-REC)", "DESCUENTO", "HOSTING", "REDONDEO (no afecto a IGV )",
				"NEXTEL PREPAGO (7042004)", "", "CORRESPONSALIA  NEXTEL    7046400", "CORRESPONSALIA  NEXTEL  7046600",
				"CORRESPONSALIA NEXTEL 7045000", "CORRESPONSALIA  NEXTEL  7046500",
				"SERV. MENSUAL (housing housing - OTH)", "Inter\u00e9s (no afecto a IGV-NC)", "TDOC.IDENT", "NRO.IDENT",
				"DOMICILIADO", "DISTRITO", "VALREFENAC", "VALREFEINT", "DSCTO LL NAC", "DSCTO LL INT", "", "TIPO.DOC",
				"Situaci\u00f3n", "Indicador GC", "96 : DEVOLUCIONES", "96: PENALIDADES  OTROS 7046000",
				"96: FRACCIONAMIENTO", "ID INSTALACIONES", "", "", "Otros ingresos Extraord P-003007", "",
				"id RENTA MENSUAL", "Iass", "PRESTACION DE SERV PRIV. P-003006 ( INSTAL. Alquileres y otros)",
				"TI HOUSING P-006100", "TI HOUSING P-006101 ( OFFICE SPACE)", "TI HOUSING P-006102 ( housing serv adm",
				" TELEFONICA FIJA CARGO VARIABLE P-03043113 -TF SERV  LOCAL",
				"TELEFONICA FIJA CARGO VARIABLE P-03043113-TF SERV  MOVIL",
				"INTERNET MYORISTA RELACIONADAS7045102- P-04233115",
				"Arriendo de Infraestructura, Instalaciones Relacionadas -P-04233116  (DD Housing Hosting Mayorista )",
				"DATOS  MAYORISTA RELACIONADAS P-04233118 DD Renta Mensual  Mayorista",
				"TF Cargo fijo cta 7046100 - P-03023113", "TF Cargo fijo cta 7046100 - P-03023114",
				"Internet Mayorista- Arriendol 7045103 /P-04233116",
				"96: Arriendo de Infraestructura, Instalaciones-Housing Hosting 7047005/P-09053119",
				"96: Arriendo de Infraestructura, Instalaciones-Cubicac.,Colocacion y Otros 7047005/P-09053120",
				"96: Arriendo de Infraestructura, Instalaciones-Ducter\u00edas 7047005/P-09053121",
				"96: Arriendo de Infraestructura, Instalaciones-ptros 7047005/P-09053122",
				"Internet Mayorista- Renta Mensual 7045103 /P-04233115",
				"Internet Mayorista- Instalacionesl 7045103 /P-04233114", "", "",
		"96: Penalidades Otros Mayorista 7047005" });

		DIST dist_fisc;
		String codigoFactura;
		List<BOLE> boletas;
		List<RECI> recibos;
		List<FACT> facturas;
		PROD prod;
		String codigoBoleta;
		Double servicio_mensual = 0d;
		Double instalacion = 0d;
		Double alquiler_equipos = 0d;
		Double servicios_suplementarios = 0d;
		Double si_afectos = 0d;
		Double no_afectos = 0d;
		Double sub_total = 0d;
		Double InstalacionDUC = 0d;
		Double InstalacionCOL = 0d;
		Double InstalacionOTH = 0d;
		Double igv = 0d;
		Double total = 0d;
		for (CIER cier : listaCier) {
			
			servicio_mensual = 0d;
			instalacion = 0d;
			alquiler_equipos = 0d;
			servicios_suplementarios = 0d;
			si_afectos = 0d;
			no_afectos = 0d;
			sub_total = 0d;
			InstalacionDUC = 0d;
			InstalacionCOL = 0d;
			InstalacionOTH = 0d;
			igv = 0d;
			total = 0d;

			recibos = cier.getRecibos();
			prod = cier.getProducto();

			List<CIER_DATA_PLAN> data_planes_cobrados_en_cierre = cier.getDataPlanesCobrados_forMaestroGerentes();
			Map<Long, CIER_DATA_PLAN> data_detalle_del_servicio = new TreeMap<Long, CIER_DATA_PLAN>();

			List<NEGO_SUCU_PLAN> nego_sucu_planes = cier.getNEGO_SUCU_PLAN_forMaestroGerentes();
			Map<Integer, NEGO_SUCU_PLAN> map_nego_sucu_planes = new TreeMap<Integer, NEGO_SUCU_PLAN>();

			for (CIER_DATA_PLAN cier_data_plan : data_planes_cobrados_en_cierre) {
				if (cier_data_plan.getCO_RECI() != null) {
					if (!data_detalle_del_servicio.containsKey(cier_data_plan.getCO_RECI())) {
						data_detalle_del_servicio.put(cier_data_plan.getCO_RECI(), cier_data_plan);
						// cantidad_planes_por_recibos.put(cier_data_plan.getCO_RECI(),
						// 0);
					}
				} else if (cier_data_plan.getCO_BOLE() != null) {
					if (!data_detalle_del_servicio.containsKey(cier_data_plan.getCO_BOLE())) {
						data_detalle_del_servicio.put(cier_data_plan.getCO_BOLE(), cier_data_plan);
						// cantidad_planes_por_recibos.put(cier_data_plan.getCO_RECI(),
						// 0);
					}
				}

			}

			for (NEGO_SUCU_PLAN nego_sucu_plan : nego_sucu_planes) {
				if (!map_nego_sucu_planes.containsKey(nego_sucu_plan.getCO_NEGO_SUCU_PLAN_ForJSON())) {
					map_nego_sucu_planes.put(nego_sucu_plan.getCO_NEGO_SUCU_PLAN_ForJSON(), nego_sucu_plan);
				}
			}

			CIER_DATA_PLAN cier_data_plan = null;
			NEGO_SUCU_PLAN nego_sucu_plan = null;
			Boolean ST_BACKUP_BU;
			String tipo_servicio;
			for (RECI recibo : recibos) {
				dist_fisc = recibo.getDist_Fisc_ForJSON();

				// servicio_mensual=recibo.getIM_SERV_MENS_ForJSON();
				servicio_mensual = com.americatel.facturacion.fncs.Numero
						.redondear(recibo.getIM_SERV_MENS_ForJSON() + recibo.getIM_DIF_CARGO_FIJO_ForJSON(), 2);
				instalacion = recibo.getIM_INST_ForJSON();
				alquiler_equipos = recibo.getIM_ALQU_ForJSON();
				servicios_suplementarios = recibo.getIM_OTRO_ForJSON();
				si_afectos = Math.abs(recibo.getIM_AJUS_SIGV_ForJSON());
				no_afectos = Math.abs(recibo.getIM_AJUS_NIGV_ForJSON());
				igv = recibo.getIM_IGV_ForJSON();
				// sub_total=recibo.getIM_NETO_ForJSON();
				sub_total = com.americatel.facturacion.fncs.Numero
						.redondear(recibo.getIM_NETO_ForJSON() + recibo.getIM_AJUS_SIGV_ForJSON(), 2);
				total = recibo.getIM_TOTA_ForJSON();
				ST_BACKUP_BU = false;
				tipo_servicio = "";
				InstalacionDUC = 0d;
				InstalacionCOL = 0d;
				InstalacionOTH = 0d;
				cier_data_plan = null;
				nego_sucu_plan = null;

				if (data_detalle_del_servicio.containsKey(recibo.getCO_RECI_ForJSON())) {
					cier_data_plan = data_detalle_del_servicio.get(recibo.getCO_RECI_ForJSON());
					if (map_nego_sucu_planes.containsKey(cier_data_plan.getCO_NEGO_SUCU_PLAN())) {
						nego_sucu_plan = map_nego_sucu_planes.get(cier_data_plan.getCO_NEGO_SUCU_PLAN());
						ST_BACKUP_BU = nego_sucu_plan.getPLAN().getRELACIONADO_TI_ForJSON();
					}
					// cantidad_sucursales=cantidad_planes_por_recibos.get(recibo.getCO_RECI_ForJSON());
				}
				// 14 DCUTERIA - 15 COLOCAICON - 16 OTROS SERVICIOS
				if (prod.getCO_PROD_ForJSON() == 14) {
					tipo_servicio = "DUC";
					InstalacionDUC = sub_total;
				} else if (prod.getCO_PROD_ForJSON() == 15) {
					tipo_servicio = "COL";
					InstalacionCOL = sub_total;
				} else if (prod.getCO_PROD_ForJSON() == 16) {
					tipo_servicio = "OTH";
					InstalacionOTH = sub_total;
				}
				
				tipo_servicio = "HOS";

				this.excel.writeCols(new Object[] { recibo.getCO_RECI_ForJSON(), recibo.getFE_EMIS_ForJSON(),
						recibo.getDE_CODI_BUM_ForJSON(), recibo.getCO_NEGO_ForJSON(), recibo.getNO_CLIE_ForJSON(),
						recibo.getDE_DIRE_FISC_ForJSON() + "," + dist_fisc.getNO_DIST_ForJSON() + " - "
								+ dist_fisc.getPROV_ForJSON().getNO_PROV_ForJSON() + " - "
								+ dist_fisc.getPROV_ForJSON().getDEPA_ForJSON().getNO_DEPA_ForJSON(),
								recibo.getDE_NUME_DOCU_ForJSON(), tipo_servicio, // TIPO
								// DE
								// SERVICIO
								// 1:INTERNET,
								// 2:DATOS
								recibo.getNO_MONE_FACT_ForJSON(), instalacion, servicio_mensual, alquiler_equipos, servicios_suplementarios, // VERIFICAR
								// VERIFICAR
								// COLUMNA
								// 13
								sub_total, igv, // A solicitud al neto se le resta
								// ajuste afecto igv
								total, recibo.getFE_VENC_ForJSON(), "", // COLUMNA 18
								"", "", "", "", "", "", "", // 25
								"", "", "", "", "", // 30
								"", "", "RU", recibo.getDE_NUME_DOCU_ForJSON(), "S", // 35
								dist_fisc.getNO_DIST_ForJSON(), "", "", "", "", // 40
								"", "RC", "", "", "", // 45
								"", "", "", "", "", // 50
								"", "", "", "", "", // 45
								"", "", "", "", "", // 50
								"", "", "", "", "", "", InstalacionOTH, InstalacionCOL, InstalacionDUC, "", // 55
								"", "", "", "", "", });
				servicio_mensual = 0d;
				instalacion = 0d;
				alquiler_equipos = 0d;
				servicios_suplementarios = 0d;
				si_afectos = 0d;
				no_afectos = 0d;
				sub_total = 0d;
				InstalacionDUC = 0d;
				InstalacionCOL = 0d;
				InstalacionOTH = 0d;
				igv = 0d;
				total = 0d;
			}

			boletas = cier.getBoletas();
			prod = cier.getProducto();

			for (BOLE boleta : boletas) {
				dist_fisc = boleta.getDist_Fisc_ForJSON();
				codigoBoleta = "";
				// servicio_mensual=recibo.getIM_SERV_MENS_ForJSON();
				servicio_mensual = com.americatel.facturacion.fncs.Numero
						.redondear(boleta.getIM_SERV_MENS_ForJSON() + boleta.getIM_DIF_CARGO_FIJO_ForJSON(), 2);
				instalacion = boleta.getIM_INST_ForJSON();
				alquiler_equipos = boleta.getIM_ALQU_ForJSON();
				servicios_suplementarios = boleta.getIM_OTRO_ForJSON();
				si_afectos = Math.abs(boleta.getIM_AJUS_SIGV_ForJSON());
				no_afectos = Math.abs(boleta.getIM_AJUS_NIGV_ForJSON());
				igv = boleta.getIM_IGV_ForJSON();
				// sub_total=recibo.getIM_NETO_ForJSON();
				sub_total = com.americatel.facturacion.fncs.Numero
						.redondear(boleta.getIM_NETO_ForJSON() + boleta.getIM_AJUS_SIGV_ForJSON(), 2);
				total = boleta.getIM_TOTA_ForJSON();
				ST_BACKUP_BU = false;
				tipo_servicio = "";
				InstalacionDUC = 0d;
				InstalacionCOL = 0d;
				InstalacionOTH = 0d;
				cier_data_plan = null;
				nego_sucu_plan = null;

				if (data_detalle_del_servicio.containsKey(boleta.getCO_BOLE_ForJSON())) {
					cier_data_plan = data_detalle_del_servicio.get(boleta.getCO_BOLE_ForJSON());
					if (map_nego_sucu_planes.containsKey(cier_data_plan.getCO_NEGO_SUCU_PLAN())) {
						nego_sucu_plan = map_nego_sucu_planes.get(cier_data_plan.getCO_NEGO_SUCU_PLAN());
						ST_BACKUP_BU = nego_sucu_plan.getPLAN().getRELACIONADO_TI_ForJSON();
					}
					// cantidad_sucursales=cantidad_planes_por_recibos.get(recibo.getCO_RECI_ForJSON());
				}
				// 14 DCUTERIA - 15 COLOCAICON - 16 OTROS SERVICIOS
				if (prod.getCO_PROD_ForJSON() == 14) {
					tipo_servicio = "DUC";
					InstalacionDUC = sub_total;
				} else if (prod.getCO_PROD_ForJSON() == 15) {
					tipo_servicio = "COL";
					InstalacionCOL = sub_total;
				} else if (prod.getCO_PROD_ForJSON() == 16) {
					tipo_servicio = "OTH";
					InstalacionOTH = sub_total;
				}
				else if (prod.getCO_PROD_ForJSON() == 17) {
					tipo_servicio = "HOS";
					InstalacionOTH = sub_total;
				}
				
				tipo_servicio = "HOS";

				if (boleta.getCO_BOLE_ForJSON() != null) {
					codigoBoleta = getFormatoCodigo(boleta.getCO_BOLE_ForJSON(), "BOLE");
				}

				this.excel.writeCols(new Object[] { codigoBoleta, boleta.getFE_EMIS_ForJSON(),
						boleta.getDE_CODI_BUM_ForJSON(), boleta.getCO_NEGO_ForJSON(), boleta.getNO_CLIE_ForJSON(),
						boleta.getDE_DIRE_FISC_ForJSON() + "," + dist_fisc.getNO_DIST_ForJSON() + " - "
								+ dist_fisc.getPROV_ForJSON().getNO_PROV_ForJSON() + " - "
								+ dist_fisc.getPROV_ForJSON().getDEPA_ForJSON().getNO_DEPA_ForJSON(),
								boleta.getDE_NUME_DOCU_ForJSON(), tipo_servicio, // TIPO
								// DE
								// SERVICIO
								// 1:INTERNET,
								// 2:DATOS
								boleta.getNO_MONE_FACT_ForJSON(), instalacion, servicio_mensual, alquiler_equipos, servicios_suplementarios,// VERIFICAR
								// VERIFICAR
								// COLUMNA
								// 13
								sub_total, igv, // A solicitud al neto se le resta
								// ajuste afecto igv
								total, boleta.getFE_VENC_ForJSON(), "", // COLUMNA 18
								"", "", "", "", "", "", "", // 25
								"", "", "", "", "", // 30
								"", "", "RU", boleta.getDE_NUME_DOCU_ForJSON(), "S", // 35
								dist_fisc.getNO_DIST_ForJSON(), "", "", "", "", // 40
								"", "BV", "", "", "", // 45
								"", "", "", "", "", // 50
								"", "", "", "", "", // 45
								"", "", "", "", "", // 50
								"", "", "", "", "", "", InstalacionOTH, InstalacionCOL, InstalacionDUC, "", // 55
								"", "", "", "", "", });
				servicio_mensual = 0d;
				instalacion = 0d;
				alquiler_equipos = 0d;
				servicios_suplementarios = 0d;
				si_afectos = 0d;
				no_afectos = 0d;
				sub_total = 0d;
				InstalacionDUC = 0d;
				InstalacionCOL = 0d;
				InstalacionOTH = 0d;
				igv = 0d;
				total = 0d;
			}

			facturas = cier.getFacturas();
			prod = cier.getProducto();
			for (FACT fact : facturas) {
				dist_fisc = fact.getDist_Fisc_ForJSON();

				servicio_mensual = fact.getIM_SERV_MENS_ForJSON();
				instalacion = fact.getIM_INST_ForJSON();
				alquiler_equipos = fact.getIM_ALQU_ForJSON();
				servicios_suplementarios = fact.getIM_OTRO_ForJSON();
				si_afectos = 0d;
				no_afectos = Math.abs(fact.getIM_AJUS_NIGV_ForJSON());
				igv = fact.getIM_IGV_ForJSON();
				sub_total = fact.getIM_VALO_VENT_NETO_ForJSON();
				total = fact.getIM_TOTA_ForJSON();

				ST_BACKUP_BU = false;
				tipo_servicio = "";
				InstalacionDUC = 0d;
				InstalacionCOL = 0d;
				InstalacionOTH = 0d;
				cier_data_plan = null;
				nego_sucu_plan = null;

				for (RECI recibo : recibos) {
					if (recibo.getCO_NEGO_ForJSON().equals(fact.getCO_NEGO_ForJSON())) {
						if (data_detalle_del_servicio.containsKey(recibo.getCO_RECI_ForJSON())) {
							cier_data_plan = data_detalle_del_servicio.get(recibo.getCO_RECI_ForJSON());
							if (map_nego_sucu_planes.containsKey(cier_data_plan.getCO_NEGO_SUCU_PLAN())) {
								nego_sucu_plan = map_nego_sucu_planes.get(cier_data_plan.getCO_NEGO_SUCU_PLAN());
								ST_BACKUP_BU = nego_sucu_plan.getPLAN().getRELACIONADO_TI_ForJSON();
							}
						}
					}
				}

				// 14 DCUTERIA - 15 COLOCAICON - 16 OTROS SERVICIOS
				if (prod.getCO_PROD_ForJSON() == 14) {
					tipo_servicio = "DUC";
					InstalacionDUC = sub_total;
				} else if (prod.getCO_PROD_ForJSON() == 15) {
					tipo_servicio = "COL";
					InstalacionCOL = sub_total;
				} else if (prod.getCO_PROD_ForJSON() == 16) {
					tipo_servicio = "OTH";
					InstalacionOTH = sub_total;
				}

				tipo_servicio = "HOS";
				
				codigoFactura = "";

				if (fact.getCO_FACT_ForJSON() != null) {
					codigoFactura = getFormatoCodigo(fact.getCO_FACT_ForJSON(), "FACT");
				}

				this.excel.writeCols(new Object[] { codigoFactura, fact.getFE_EMIS_ForJSON(),
						fact.getDE_CODI_BUM_ForJSON(), fact.getCO_NEGO_ForJSON(), fact.getNO_CLIE_ForJSON(),
						fact.getDE_DIRE_FISC_ForJSON() + "," + dist_fisc.getNO_DIST_ForJSON() + " - "
								+ dist_fisc.getPROV_ForJSON().getNO_PROV_ForJSON() + " - "
								+ dist_fisc.getPROV_ForJSON().getDEPA_ForJSON().getNO_DEPA_ForJSON(),
								fact.getDE_NUME_DOCU_ForJSON(), tipo_servicio, // TIPO
								// DE
								// SERVICIO
								// 1:INTERNET,
								// 2:DATOS
								fact.getNO_MONE_FACT_ForJSON(), instalacion, servicio_mensual, alquiler_equipos, servicios_suplementarios, // VERIFICAR
								// VERIFICAR
								// COLUMNA
								// 13
								sub_total, igv, // A solicitud al neto se le resta
								// ajuste afecto igv
								total, fact.getFE_VENC_ForJSON(), "", // COLUMNA 18
								"", "", "", "", "", "", "", // 25
								"", "", "", "", "", // 30
								"", "", "RU", fact.getDE_NUME_DOCU_ForJSON(), "S", // 35
								dist_fisc.getNO_DIST_ForJSON(), "", "", "", "", // 40
								"", "RC", "", "", "", // 45
								"", "", "", "", "", // 50
								"", "", "", "", "", // 45
								"", "", "", "", "", // 50
								"", "", "", "", "", "", InstalacionOTH, InstalacionCOL, InstalacionDUC, "", // 55
								"", "", "", "", "", });
				servicio_mensual = 0d;
				instalacion = 0d;
				alquiler_equipos = 0d;
				servicios_suplementarios = 0d;
				si_afectos = 0d;
				no_afectos = 0d;
				sub_total = 0d;
				InstalacionDUC = 0d;
				InstalacionCOL = 0d;
				InstalacionOTH = 0d;
				igv = 0d;
				total = 0d;
			}
		}

	}

	public void calcularFormatoB() {

		// 79 COLUMNAS
		this.excel.writeCols(new Object[] { "RECIBO", "FECHA FACTURA", "COD. CLIENTE ", "NEGOCIO", "CLIENTE ",
				"DIRECCION", "RUC", "TIPO SERVICIO ", "MONEDA ",
				"Prod 99 7045301 Analitico I-09900003 /22-01 Instalaciones y Reubicaciones",
				"Prod 99 7045301 Analitico I-09900002 /23-01 Renta Mensual",
				"Prod 99 7045301 Analitico I-09900001/ 23-01 Arriendo de equipos", "OTROS", "SUB TOTAL", "IGV",
				"TOTAL FACTURA ", "FECHA VCTO.", "T.D.Ref.", "N.Doc.Ref.", "T.Cambio Doc Ref ",
				"INTERESES (no afecto a IGV-REC)", "DESCUENTO", "HOSTING", "REDONDEO (no afecto a IGV )",
				"NEXTEL PREPAGO (7042004)", "", "CORRESPONSALIA  NEXTEL    7046400", "CORRESPONSALIA  NEXTEL  7046600",
				"CORRESPONSALIA NEXTEL 7045000", "CORRESPONSALIA  NEXTEL  7046500",
				"SERV. MENSUAL (housing housing - OTH)", "PROD 99 Inter\u00e9s (no afecto a IGV-NC) P-0010000",
				"TDOC.IDENT", "NRO.IDENT", "DOMICILIADO", "DISTRITO", "VALREFENAC", "VALREFEINT", "DSCTO LL NAC",
				"DSCTO LL INT", "", "TIPO.DOC", "Situaci\u00f3n", "Indicador GC", "DEVOLUCIONES",
				"Prod 99: PENALIDADES 7045301", "FRACCIONAMIENTO", "I-09700007", "", "", "adm y manttto de red 7563000",
				"", "id RENTA MENSUAL", "Iass", "PRESTACION DE SERV PRIV. P-003006 ( INSTAL. Alquileres y otros)",
				"TI HOUSING P-006100", "TI HOUSING P-006101 ( OFFICE SPACE) ", "TI HOUSING P-006102 ( housing serv adm",
				" TELEFONICA FIJA CARGO VARIABLE P-03043113 -TF SERV  LOCAL",
				" TELEFONICA FIJA CARGO VARIABLE P-03043113-TF SERV  MOVIL ",
				"INTERNET CONTRATADO MYORISTA RELACIONADAS7045102- P-04233115",
				"Arriendo de Infraestructura, Instalaciones Relacionadas -P-04233116  (DD Housing Hosting Mayorista )",
				"DATOS  MAYORISTA RELACIONADAS P-09053118 DD Renta Mensual  Mayorista",
				"TF Cargo fijo cta 7046100 - P-03023113", "TF Cargo fijo cta 7046100 - P-03023114",
				"Internet Mayorista- Arriendol 7045103 /I-08900003",
				"Arriendo de Infraestructura, Instalaciones-Housing Hosting 7047005/P-09053119",
				"Arriendo de Infraestructura, Instalaciones-Cubicac.,Colocacion y Otros 7047005/P-09053120",
				"Arriendo de Infraestructura, Instalaciones-Ducter\u00edas 7047005/P-09053121",
				"Arriendo de Infraestructura, Instalaciones-ptros 7047005/P-09053122",
				"89: Internet Mayorista- Renta Mensual 7045103 /  I-08900001",
				"89:Internet Mayorista- Instalaciones 7045103 /I-08900002",
				"Servicios Adicionales TIC - Entel Per\u00fa P-006236  Cta 7047008",
				"DATOS DEDICADOS EMPRESAS- RENTA 7045300 P-09033113", "",
				"89:Penalidades  ID Mayorista 7045103 I-08900004",
				"89:interes  afecto a IGV 7721000  P-00010000 fraccionam",
				"PROD 99 ngn ENLACE INTERCONEXION POR CAPACIDAD ", "prod 99 TF ENLACE INTERCONEXION POR CAPACIDAD " });

		String codigoFactura;
		String codigoBoleta;
		DIST dist_fisc;
		List<RECI> recibos;
		List<FACT> facturas;
		List<BOLE> boletas;
		PROD prod;
		Double servicio_mensual = 0d;
		Double instalacion = 0d;
		Double alquiler_equipos = 0d;
		Double servicios_suplementarios = 0d;
		Double si_afectos = 0d;
		Double no_afectos = 0d;
		Double sub_total = 0d;
		Double InstalacionDUC = 0d;
		Double InstalacionCOL = 0d;
		Double InstalacionOTH = 0d;
		Double igv = 0d;
		Double total = 0d;

		Double InstalacionADD = 0d;
		Double rentaMensualADD = 0d;
		Double InstalacionIPB = 0d;
		Double rentaMensualIPB = 0d;

		for (CIER cier : listaCier) {

			recibos = cier.getRecibos();
			prod = cier.getProducto();

			List<CIER_DATA_PLAN> data_planes_cobrados_en_cierre = cier.getDataPlanesCobrados_forMaestroGerentes();
			Map<Long, CIER_DATA_PLAN> data_detalle_del_servicio = new TreeMap<Long, CIER_DATA_PLAN>();

			List<NEGO_SUCU_PLAN> nego_sucu_planes = cier.getNEGO_SUCU_PLAN_forMaestroGerentes();
			Map<Integer, NEGO_SUCU_PLAN> map_nego_sucu_planes = new TreeMap<Integer, NEGO_SUCU_PLAN>();

			for (CIER_DATA_PLAN cier_data_plan : data_planes_cobrados_en_cierre) {
				if (cier_data_plan.getCO_RECI() != null) {
					if (!data_detalle_del_servicio.containsKey(cier_data_plan.getCO_RECI())) {
						data_detalle_del_servicio.put(cier_data_plan.getCO_RECI(), cier_data_plan);
						// cantidad_planes_por_recibos.put(cier_data_plan.getCO_RECI(),
						// 0);
					}
				} else if (cier_data_plan.getCO_BOLE() != null) {
					if (!data_detalle_del_servicio.containsKey(cier_data_plan.getCO_BOLE())) {
						data_detalle_del_servicio.put(cier_data_plan.getCO_BOLE(), cier_data_plan);
						// cantidad_planes_por_recibos.put(cier_data_plan.getCO_RECI(),
						// 0);
					}
				}
			}

			for (NEGO_SUCU_PLAN nego_sucu_plan : nego_sucu_planes) {
				if (!map_nego_sucu_planes.containsKey(nego_sucu_plan.getCO_NEGO_SUCU_PLAN_ForJSON())) {
					map_nego_sucu_planes.put(nego_sucu_plan.getCO_NEGO_SUCU_PLAN_ForJSON(), nego_sucu_plan);
				}
			}

			CIER_DATA_PLAN cier_data_plan = null;
			NEGO_SUCU_PLAN nego_sucu_plan = null;
			Boolean ST_BACKUP_BU;
			String tipo_servicio;
			for (RECI recibo : recibos) {
				dist_fisc = recibo.getDist_Fisc_ForJSON();

				InstalacionADD = 0d;
				rentaMensualADD = 0d;
				InstalacionIPB = 0d;
				rentaMensualIPB = 0d;
				// servicio_mensual=recibo.getIM_SERV_MENS_ForJSON();
				servicio_mensual = com.americatel.facturacion.fncs.Numero
						.redondear(recibo.getIM_SERV_MENS_ForJSON() + recibo.getIM_DIF_CARGO_FIJO_ForJSON(), 2);
				instalacion = recibo.getIM_INST_ForJSON();
				alquiler_equipos = recibo.getIM_ALQU_ForJSON();
				servicios_suplementarios = recibo.getIM_OTRO_ForJSON();
				si_afectos = Math.abs(recibo.getIM_AJUS_SIGV_ForJSON());
				no_afectos = Math.abs(recibo.getIM_AJUS_NIGV_ForJSON());
				igv = recibo.getIM_IGV_ForJSON();
				// sub_total=recibo.getIM_NETO_ForJSON();
				sub_total = com.americatel.facturacion.fncs.Numero
						.redondear(recibo.getIM_NETO_ForJSON() + recibo.getIM_AJUS_SIGV_ForJSON(), 2);
				total = recibo.getIM_TOTA_ForJSON();
				ST_BACKUP_BU = false;
				tipo_servicio = "";
				cier_data_plan = null;
				nego_sucu_plan = null;

				if (data_detalle_del_servicio.containsKey(recibo.getCO_RECI_ForJSON())) {
					cier_data_plan = data_detalle_del_servicio.get(recibo.getCO_RECI_ForJSON());
					if (map_nego_sucu_planes.containsKey(cier_data_plan.getCO_NEGO_SUCU_PLAN())) {
						nego_sucu_plan = map_nego_sucu_planes.get(cier_data_plan.getCO_NEGO_SUCU_PLAN());
						ST_BACKUP_BU = nego_sucu_plan.getPLAN().getRELACIONADO_TI_ForJSON();
					}
					// cantidad_sucursales=cantidad_planes_por_recibos.get(recibo.getCO_RECI_ForJSON());
				}
				// 12 Internet Dedicados Mayorista - 13 Datos Dedicado Mayorista
				if (prod.getCO_PROD_ForJSON() == 12) {
					tipo_servicio = "IPB";
					InstalacionIPB = instalacion;
					rentaMensualIPB = servicio_mensual;
				} else if (prod.getCO_PROD_ForJSON() == 13) {
					tipo_servicio = "ADD";
					InstalacionADD = instalacion;
					rentaMensualADD = servicio_mensual;
				}

				this.excel.writeCols(new Object[] { recibo.getCO_RECI_ForJSON(), recibo.getFE_EMIS_ForJSON(),
						recibo.getDE_CODI_BUM_ForJSON(), recibo.getCO_NEGO_ForJSON(), recibo.getNO_CLIE_ForJSON(),
						recibo.getDE_DIRE_FISC_ForJSON() + "," + dist_fisc.getNO_DIST_ForJSON() + " - "
								+ dist_fisc.getPROV_ForJSON().getNO_PROV_ForJSON() + " - "
								+ dist_fisc.getPROV_ForJSON().getDEPA_ForJSON().getNO_DEPA_ForJSON(),
								recibo.getDE_NUME_DOCU_ForJSON(), tipo_servicio, // TIPO
								// DE
								// SERVICIO
								// 1:INTERNET,
								// 2:DATOS
								recibo.getNO_MONE_FACT_ForJSON(), InstalacionADD, rentaMensualADD, "", "", // VERIFICAR
								// VERIFICAR
								// COLUMNA
								// 13
								sub_total, igv, // A solicitud al neto se le resta
								// ajuste afecto igv
								total, recibo.getFE_VENC_ForJSON(), "", // COLUMNA 18
								"", "", "", "", "", "", "", // 25
								"", "", "", "", "", // 30
								"", "", "RU", recibo.getDE_NUME_DOCU_ForJSON(), "S", // 35
								dist_fisc.getNO_DIST_ForJSON(), "", "", "", "", // 40
								"", "RC", "", "", "", // 45
								"", "", "", "", "", // 50
								"", "", "", "", "", // 45
								"", "", "", "", "", // 50
								"", "", "", "", "", "", "", "", "", "", rentaMensualIPB, InstalacionIPB, "", "", // 55
								"", "", "", "", "", "", });
			}

			boletas = cier.getBoletas();
			prod = cier.getProducto();

			for (BOLE boleta : boletas) {
				dist_fisc = boleta.getDist_Fisc_ForJSON();

				codigoBoleta = "";
				InstalacionADD = 0d;
				rentaMensualADD = 0d;
				InstalacionIPB = 0d;
				rentaMensualIPB = 0d;
				// servicio_mensual=recibo.getIM_SERV_MENS_ForJSON();
				servicio_mensual = com.americatel.facturacion.fncs.Numero
						.redondear(boleta.getIM_SERV_MENS_ForJSON() + boleta.getIM_DIF_CARGO_FIJO_ForJSON(), 2);
				instalacion = boleta.getIM_INST_ForJSON();
				alquiler_equipos = boleta.getIM_ALQU_ForJSON();
				servicios_suplementarios = boleta.getIM_OTRO_ForJSON();
				si_afectos = Math.abs(boleta.getIM_AJUS_SIGV_ForJSON());
				no_afectos = Math.abs(boleta.getIM_AJUS_NIGV_ForJSON());
				igv = boleta.getIM_IGV_ForJSON();
				// sub_total=recibo.getIM_NETO_ForJSON();
				sub_total = com.americatel.facturacion.fncs.Numero
						.redondear(boleta.getIM_NETO_ForJSON() + boleta.getIM_AJUS_SIGV_ForJSON(), 2);
				total = boleta.getIM_TOTA_ForJSON();
				ST_BACKUP_BU = false;
				tipo_servicio = "";
				cier_data_plan = null;
				nego_sucu_plan = null;

				if (data_detalle_del_servicio.containsKey(boleta.getCO_BOLE_ForJSON())) {
					cier_data_plan = data_detalle_del_servicio.get(boleta.getCO_BOLE_ForJSON());
					if (map_nego_sucu_planes.containsKey(cier_data_plan.getCO_NEGO_SUCU_PLAN())) {
						nego_sucu_plan = map_nego_sucu_planes.get(cier_data_plan.getCO_NEGO_SUCU_PLAN());
						ST_BACKUP_BU = nego_sucu_plan.getPLAN().getRELACIONADO_TI_ForJSON();
					}
					// cantidad_sucursales=cantidad_planes_por_recibos.get(recibo.getCO_RECI_ForJSON());
				}
				// 12 Internet Dedicados Mayorista - 13 Datos Dedicado Mayorista
				if (prod.getCO_PROD_ForJSON() == 12) {
					tipo_servicio = "IPB";
					InstalacionIPB = instalacion;
					rentaMensualIPB = servicio_mensual;
				} else if (prod.getCO_PROD_ForJSON() == 13) {
					tipo_servicio = "ADD";
					InstalacionADD = instalacion;
					rentaMensualADD = servicio_mensual;
				}

				if (boleta.getCO_BOLE_ForJSON() != null) {
					codigoBoleta = getFormatoCodigo(boleta.getCO_BOLE_ForJSON(), "BOLE");
				}

				this.excel.writeCols(new Object[] { codigoBoleta, boleta.getFE_EMIS_ForJSON(),
						boleta.getDE_CODI_BUM_ForJSON(), boleta.getCO_NEGO_ForJSON(), boleta.getNO_CLIE_ForJSON(),
						boleta.getDE_DIRE_FISC_ForJSON() + "," + dist_fisc.getNO_DIST_ForJSON() + " - "
								+ dist_fisc.getPROV_ForJSON().getNO_PROV_ForJSON() + " - "
								+ dist_fisc.getPROV_ForJSON().getDEPA_ForJSON().getNO_DEPA_ForJSON(),
								boleta.getDE_NUME_DOCU_ForJSON(), tipo_servicio, // TIPO
								// DE
								// SERVICIO
								// 1:INTERNET,
								// 2:DATOS
								boleta.getNO_MONE_FACT_ForJSON(), InstalacionADD, rentaMensualADD, "", "", // VERIFICAR
								// VERIFICAR
								// COLUMNA
								// 13
								sub_total, igv, // A solicitud al neto se le resta
								// ajuste afecto igv
								total, boleta.getFE_VENC_ForJSON(), "", // COLUMNA 18
								"", "", "", "", "", "", "", // 25
								"", "", "", "", "", // 30
								"", "", "RU", boleta.getDE_NUME_DOCU_ForJSON(), "S", // 35
								dist_fisc.getNO_DIST_ForJSON(), "", "", "", "", // 40
								"", "BV", "", "", "", // 45
								"", "", "", "", "", // 50
								"", "", "", "", "", // 45
								"", "", "", "", "", // 50
								"", "", "", "", "", "", "", "", "", "", rentaMensualIPB, InstalacionIPB, "", "", // 55
								"", "", "", "", "", "", });
			}

			facturas = cier.getFacturas();
			prod = cier.getProducto();
			for (FACT fact : facturas) {
				dist_fisc = fact.getDist_Fisc_ForJSON();

				InstalacionADD = 0d;
				rentaMensualADD = 0d;
				InstalacionIPB = 0d;
				rentaMensualIPB = 0d;

				servicio_mensual = fact.getIM_SERV_MENS_ForJSON();
				instalacion = fact.getIM_INST_ForJSON();
				alquiler_equipos = fact.getIM_ALQU_ForJSON();
				servicios_suplementarios = fact.getIM_OTRO_ForJSON();
				si_afectos = 0d;
				no_afectos = Math.abs(fact.getIM_AJUS_NIGV_ForJSON());
				igv = fact.getIM_IGV_ForJSON();
				sub_total = fact.getIM_VALO_VENT_NETO_ForJSON();
				total = fact.getIM_TOTA_ForJSON();

				ST_BACKUP_BU = false;
				tipo_servicio = "";
				cier_data_plan = null;
				nego_sucu_plan = null;

				for (RECI recibo : recibos) {
					if (recibo.getCO_NEGO_ForJSON().equals(fact.getCO_NEGO_ForJSON())) {
						if (data_detalle_del_servicio.containsKey(recibo.getCO_RECI_ForJSON())) {
							cier_data_plan = data_detalle_del_servicio.get(recibo.getCO_RECI_ForJSON());
							if (map_nego_sucu_planes.containsKey(cier_data_plan.getCO_NEGO_SUCU_PLAN())) {
								nego_sucu_plan = map_nego_sucu_planes.get(cier_data_plan.getCO_NEGO_SUCU_PLAN());
								ST_BACKUP_BU = nego_sucu_plan.getPLAN().getRELACIONADO_TI_ForJSON();
							}
						}
					}
				}

				// 12 Internet Dedicados Mayorista - 13 Datos Dedicado Mayorista
				if (prod.getCO_PROD_ForJSON() == 12) {
					tipo_servicio = "IPB";
					InstalacionIPB = instalacion;
					rentaMensualIPB = servicio_mensual;
				} else if (prod.getCO_PROD_ForJSON() == 13) {
					tipo_servicio = "ADD";
					InstalacionADD = instalacion;
					rentaMensualADD = servicio_mensual;
				}

				codigoFactura = "";

				if (fact.getCO_FACT_ForJSON() != null) {
					codigoFactura = getFormatoCodigo(fact.getCO_FACT_ForJSON(), "FACT");
				}

				this.excel.writeCols(new Object[] { codigoFactura, fact.getFE_EMIS_ForJSON(),
						fact.getDE_CODI_BUM_ForJSON(), fact.getCO_NEGO_ForJSON(), fact.getNO_CLIE_ForJSON(),
						fact.getDE_DIRE_FISC_ForJSON() + "," + dist_fisc.getNO_DIST_ForJSON() + " - "
								+ dist_fisc.getPROV_ForJSON().getNO_PROV_ForJSON() + " - "
								+ dist_fisc.getPROV_ForJSON().getDEPA_ForJSON().getNO_DEPA_ForJSON(),
								fact.getDE_NUME_DOCU_ForJSON(), tipo_servicio, // TIPO
								// DE
								// SERVICIO
								// 1:INTERNET,
								// 2:DATOS
								fact.getNO_MONE_FACT_ForJSON(), InstalacionADD, rentaMensualADD, "", "", // VERIFICAR
								// VERIFICAR
								// COLUMNA
								// 13
								sub_total, igv, // A solicitud al neto se le resta
								// ajuste afecto igv
								total, fact.getFE_VENC_ForJSON(), "", // COLUMNA 18
								"", "", "", "", "", "", "", // 25
								"", "", "", "", "", // 30
								"", "", "RU", fact.getDE_NUME_DOCU_ForJSON(), "S", // 35
								dist_fisc.getNO_DIST_ForJSON(), "", "", "", "", // 40
								"", "RC", "", "", "", // 45
								"", "", "", "", "", // 50
								"", "", "", "", "", // 45
								"", "", "", "", "", // 50
								"", "", "", "", "", "", "", "", "", "", rentaMensualIPB, InstalacionIPB, "", "", // 55
								"", "", "", "", "", "", });
			}
		}

	}

	private void calcularFormatoC() {

		this.excel.writeCols(new Object[] { "FACTURA", "FECHA FACTURA", "COD. CLIENTE", "NEGOCIO", "CLIENTE ",
				"DIRECCION", "RUC", "TIPO SERVICIO", "MONEDA", "INSTALACION", "93:7041003 SERV. MENSUAL",
				"93: 7047003 OFFICE SPACE", "93:7047003 SERVICIO ADMINISTRATIVO", "SUB TOTAL", "IGV", "TOTAL FACTURA",
				"FECHA VCTO.", "T.D.Ref.", "N.Doc.Ref.", "T.Cambio Doc Ref", "INTERES", "ENTEL Y SCC", "", "REDONDEO",
				"", "", "", "", "", "", "", "", "TDOC.IDENT", "NRO.IDENT", "DOMICILIADO", "DISTRITO", "", "", "", "",
				"", "TIPO DOCUMENTO", "Situaci\u00f3n", "Indicador GC", "93:Interes no afecto a IGV 9459200",
				"PENALIDADES  P-006103", "93 Fraccionamientos  dev cheque",
		"93: Prestac. Serv. TI Equipamiento 7047006 (P-006235) one shoot" });

		String codigoFactura;
		DIST dist_fisc;
		List<FACT> facturas;
		PROD prod;
		Double servicio_mensual = 0d;
		Double instalacion = 0d;
		Double alquiler_equipos = 0d;
		Double servicios_suplementarios = 0d;
		Double si_afectos = 0d;
		Double no_afectos = 0d;
		Double sub_total = 0d;

		Double sub_total_Sas = 0d;
		Double sub_total_Iaas = 0d;

		Double igv = 0d;
		Double total = 0d;
		for (CIER cier : listaCier) {

			prod = cier.getProducto();

			List<CIER_DATA_PLAN> data_planes_cobrados_en_cierre = cier.getDataPlanesCobrados_forMaestroGerentes();
			Map<Long, CIER_DATA_PLAN> data_detalle_del_servicio = new TreeMap<Long, CIER_DATA_PLAN>();

			List<NEGO_SUCU_PLAN> nego_sucu_planes = cier.getNEGO_SUCU_PLAN_forMaestroGerentes();
			Map<Integer, NEGO_SUCU_PLAN> map_nego_sucu_planes = new TreeMap<Integer, NEGO_SUCU_PLAN>();

			/*
			 * for (CIER_DATA_PLAN cier_data_plan :
			 * data_planes_cobrados_en_cierre){ if
			 * (!data_detalle_del_servicio.containsKey(cier_data_plan.getCO_RECI
			 * ())){ data_detalle_del_servicio.put(cier_data_plan.getCO_RECI(),
			 * cier_data_plan);
			 * //cantidad_planes_por_recibos.put(cier_data_plan.getCO_RECI(),
			 * 0); } }
			 */

			/*
			 * for (NEGO_SUCU_PLAN nego_sucu_plan : nego_sucu_planes){ if
			 * (!map_nego_sucu_planes.containsKey(nego_sucu_plan.
			 * getCO_NEGO_SUCU_PLAN_ForJSON())){
			 * map_nego_sucu_planes.put(nego_sucu_plan.
			 * getCO_NEGO_SUCU_PLAN_ForJSON(), nego_sucu_plan); } }
			 */

			String tipo_servicio;

			facturas = cier.getFacturas();
			prod = cier.getProducto();
			for (FACT fact : facturas) {
				dist_fisc = fact.getDist_Fisc_ForJSON();

				servicio_mensual = fact.getIM_SERV_MENS_ForJSON();
				instalacion = fact.getIM_INST_ForJSON();
				alquiler_equipos = fact.getIM_ALQU_ForJSON();
				servicios_suplementarios = fact.getIM_OTRO_ForJSON();
				si_afectos = 0d;
				no_afectos = Math.abs(fact.getIM_AJUS_NIGV_ForJSON());
				igv = fact.getIM_IGV_ForJSON();
				sub_total = fact.getIM_VALO_VENT_NETO_ForJSON();
				total = fact.getIM_TOTA_ForJSON();

				tipo_servicio = "";

				if (prod.getCO_PROD_ForJSON() == 7) {
					tipo_servicio = "HOU";
					// sub_total_Iaas=sub_total;
				} else if (prod.getCO_PROD_ForJSON() == 11) {
					// sub_total_Sas=sub_total;
					tipo_servicio = "HOU - ONE SHOT";
				}

				codigoFactura = "";

				if (fact.getCO_FACT_ForJSON() != null) {
					codigoFactura = getFormatoCodigo(fact.getCO_FACT_ForJSON(), "FACT");

				}

				this.excel.writeCols(new Object[] { codigoFactura, fact.getFE_EMIS_ForJSON(),
						fact.getDE_CODI_BUM_ForJSON(), fact.getCO_NEGO_ForJSON(), fact.getNO_CLIE_ForJSON(),
						fact.getDE_DIRE_FISC_ForJSON() + "," + dist_fisc.getNO_DIST_ForJSON() + " - "
								+ dist_fisc.getPROV_ForJSON().getNO_PROV_ForJSON() + " - "
								+ dist_fisc.getPROV_ForJSON().getDEPA_ForJSON().getNO_DEPA_ForJSON(),
								fact.getDE_NUME_DOCU_ForJSON(), tipo_servicio, // TIPO
								// DE
								// SERVICIO
								// 1:INTERNET,
								// 2:DATOS
								fact.getNO_MONE_FACT_ForJSON(), "", servicio_mensual, servicios_suplementarios, "", // VERIFICAR
								// VERIFICAR
								// COLUMNA
								// 13
								sub_total, igv, // A solicitud al neto se le resta
								// ajuste afecto igv
								total, fact.getFE_VENC_ForJSON(), "", // COLUMNA 18
								"", "", "", "", "", no_afectos, "", // 25
								"", "", "", "", "", // 30
								"", "", "RU", fact.getDE_NUME_DOCU_ForJSON(), "S", // 35
								dist_fisc.getNO_DIST_ForJSON(), "", "", "", "", // 40
								"", "FC", "", "", "", // 45
								"", "", "", });

			}
		}

	}

	
	
	
	private void calcularReporteTI(Integer nu_ano, Integer nu_peri) {
		this.excel.writeCols(new Object[] { "FACTURA", "FECHA FACTURA", "COD. CLIENTE", "NEGOCIO",	"CLIENTE", 	"DIRECCION", "RUC","TIPO SERVICIO", 	
				"MONEDA", "94 TI HOSTING: Dedicado 7047012 P-006210", "94  TI HOSTING 7047012 Compartido P-006220", "94 TI CLOUD: 7047002 SAAS  P-006246", 
				"94  TI CLOUD :7047002     IAAS   P-006230", "SUB TOTAL", "IGV", "TOTAL FACTURA", "FECHA VCTO.", "T.D.Ref.", "N.Doc.Ref.", "T.Cambio Doc Ref", "INTERES",	"ENTEL Y SCC",
				"94  TI CLOUD: 7047002 Mc Afee Secure P-006242", "REDONDEO", "94 TI CLOUD Back up 7047002 P-006243","","","","","","", "Interés (no afecto a IGV-NC)", "TDOC.IDENT", "NRO.IDENT", 
				"DOMICILIADO", "DISTRITO","","","","","", "TIPO DOCUMENTO", "Situación", "Indicador GC", "9459200  Interes no afecto a IGV P-00010000", "94: PENALIDADES Cloud 7047002  P-006245", 
				"Fraccionamiento 7721000 - dev cheque P-00010000", "enajenacion Prestac. Serv. TI Equipamiento 7561000 (P-006247)", "TI HOUSING 7041003 Renta P-006100", 
				"TI HOUSING 7047003 OFFICE SPACE P-006101", "TI HOUSING 7047003 KVAS ENERGIA   P-006251", "Servicios Gestionados 7047075 Servicios Administrados P-006102",	
				"one shoot 7047006 housing (P-006235)  Vta Equipos TIC", "one shoot  7047006  hosting (P-006253)", "PENALIDADES HOUSING 7047002   P-006103", 
				"94 TI CLOUD: 7047002 STAAS  (P-006248)", "94 TI CLOUD: 7047002 Office 365 (P-006249)",  "94 TI CLOUD: 7047002 CRM/ERP (P-006250)", "Servicios Gestionados 7047075 Manos Remotas P-6252",
				"one shoot  CB - ONE SHOT", "Servicios Suplementarios Adicionales",	"Servicios Unicos Adicionales","","","7048001/I-09400001/00-00 Renta Mensual Fact Elect", "7048001/I-09400002/00-00 Renta Variable Fact. Elect", "7048001/I-09400003/00-00 Instalacion y/o implementación","","","","7047060/I-09400004/00-00 Renta Mensual", "7047060/I-09400005/00-00 Renta Variable"});
		
		Float monto_serv_supl = null;
		
		for (CIER cier : listaCier) {
			//lista de facturas con respecto al cierre
			List<ReporteCargaTI> resultado = cier.getReporteCargaTI();
			
			//itera factura por factura
			for(ReporteCargaTI rep : resultado){
				
				if(rep.getCO_FACT() == "F 20016270"){
				 System.out.println("factura");
				}
				
				
				List<DETALLE_DOCUMENTO> listaDetalle;
				//lista de los detalles por factura
				listaDetalle = cier.getDetalleForCodFact(Integer.parseInt(rep.getCO_FACT().substring(2).trim()));
				
				String tipoServPlan = "";
				for(DETALLE_DOCUMENTO det :listaDetalle){
					if(det.getCO_TIPO_GLOS() == Constante.GLOSA_TIPO_PLAN){
						tipoServPlan = det.getTIP_SERV();
						break;
					}
				}
				BigDecimal tiHousingOfficeSpace			= BigDecimal.ZERO;
				BigDecimal tiHousingKvasEnergia			= BigDecimal.ZERO;
				BigDecimal oneShootHousingVtaEquipoTIC  = BigDecimal.ZERO;
				BigDecimal oneShootHousing              = BigDecimal.ZERO;
				BigDecimal tiCloudStaas                 = BigDecimal.ZERO;
				BigDecimal tiCloudOffice                = BigDecimal.ZERO;
				BigDecimal tiCloudCRM                   = BigDecimal.ZERO;
				BigDecimal servGestionadosManosRemotas  = BigDecimal.ZERO;
				BigDecimal oneShootCbOneShot            = BigDecimal.ZERO;
				BigDecimal servSupleAdicionales         = BigDecimal.ZERO;
				BigDecimal servUnicAdicionales          = BigDecimal.ZERO;
		
				
				for(DETALLE_DOCUMENTO det :listaDetalle){
					
					//itera por cada detalle tipo 1 o 3
					if(det.getCO_FACT() == 95000056)
					{
						
						int a=1;
					}

					String serv_suplementario = "";
					serv_suplementario = cier.getNombreServicioSuplementario(Integer.parseInt(rep.getCO_FACT().substring(2).trim()));
					
					
					if(det.getCO_TIPO_GLOS() == Constante.GLOSA_TIPO_SERVICIO_SUPLEMENTARIO
							&& Constante.HOU.equalsIgnoreCase(tipoServPlan) && det.getNOMBRE_SERVICIO().equalsIgnoreCase("OFFICE SPACE")){
						tiHousingOfficeSpace = tiHousingOfficeSpace.add(det.getMON_SERVICIO());
					}
					if(det.getCO_TIPO_GLOS() == Constante.GLOSA_TIPO_SERVICIO_SUPLEMENTARIO
							&& Constante.HOU.equalsIgnoreCase(tipoServPlan) && (det.getNOMBRE_SERVICIO().equalsIgnoreCase("KVA")||det.getNOMBRE_SERVICIO().equalsIgnoreCase("ENERGIA"))){
						tiHousingKvasEnergia = tiHousingKvasEnergia.add(det.getMON_SERVICIO());
					}
					if(det.getCO_TIPO_GLOS() == Constante.GLOSA_TIPO_SERVICIO_UNICO
							&& Constante.HOU_ONE_SHOT.equalsIgnoreCase(tipoServPlan) ){
						oneShootHousingVtaEquipoTIC = oneShootHousingVtaEquipoTIC.add(det.getMON_SERVICIO());
					}
					if(det.getCO_TIPO_GLOS() == Constante.GLOSA_TIPO_SERVICIO_UNICO
							&& Constante.HOS_ONE_SHOT.equalsIgnoreCase(tipoServPlan) ){
						oneShootHousing = oneShootHousing.add(det.getMON_SERVICIO());
					}
					if(det.getCO_TIPO_GLOS() == Constante.GLOSA_TIPO_PLAN
							&& Constante.STAAS_CB.equalsIgnoreCase(tipoServPlan) ){
						tiCloudStaas = tiCloudStaas.add(det.getMON_SERVICIO());
					}
					
					/////
					if(det.getCO_TIPO_GLOS() == Constante.GLOSA_TIPO_PLAN
							&& Constante.HOU.equalsIgnoreCase(tipoServPlan)){
						
						if ( serv_suplementario !=null)
						{
							if(serv_suplementario.equalsIgnoreCase("OFFICE SPACE") )
							{
						monto_serv_supl = (Float.parseFloat(rep.getSUB_TOTAL())) - (Float.parseFloat(det.getMON_SERVICIO().toString()));
						tiHousingOfficeSpace = tiHousingOfficeSpace.add(new BigDecimal(monto_serv_supl));
						}
							else {
								monto_serv_supl = (Float.parseFloat(rep.getSUB_TOTAL())) - (Float.parseFloat(det.getMON_SERVICIO().toString()));
								servSupleAdicionales = servSupleAdicionales.add(new BigDecimal(monto_serv_supl));
							}}
						
						
					}
					
					
					if(det.getCO_TIPO_GLOS() == Constante.GLOSA_TIPO_PLAN
							&& Constante.IAAS_Compartido.equalsIgnoreCase(tipoServPlan)){						
						if ( serv_suplementario !=null)
						{

								monto_serv_supl = (Float.parseFloat(rep.getSUB_TOTAL())) - (Float.parseFloat(det.getMON_SERVICIO().toString()));
								servSupleAdicionales = servSupleAdicionales.add(new BigDecimal(monto_serv_supl));
						}					
						
					}
					
					
					
					
					
					if(det.getCO_TIPO_GLOS() == Constante.GLOSA_TIPO_PLAN
							&& Constante.OF365_CB.equalsIgnoreCase(tipoServPlan) ){
						tiCloudOffice = tiCloudOffice.add(det.getMON_SERVICIO());
					}
					if(det.getCO_TIPO_GLOS() == Constante.GLOSA_TIPO_PLAN
							&& Constante.CRM_CB.equalsIgnoreCase(tipoServPlan) ){
						tiCloudCRM = tiCloudCRM.add(det.getMON_SERVICIO());
					}
					

					
					if(det.getCO_TIPO_GLOS() == Constante.GLOSA_TIPO_SERVICIO_SUPLEMENTARIO
							&& det.getNOMBRE_SERVICIO().equalsIgnoreCase("MANOS REMOTAS")
							&& Constante.HOU.equalsIgnoreCase(tipoServPlan)){
						servGestionadosManosRemotas = servGestionadosManosRemotas.add(det.getMON_SERVICIO());
					}
					if(det.getCO_TIPO_GLOS() == Constante.GLOSA_TIPO_SERVICIO_UNICO
							&& Constante.CB_ONE_SHOT.equalsIgnoreCase(tipoServPlan) ){
						oneShootCbOneShot = oneShootCbOneShot.add(det.getMON_SERVICIO());
					}
					if(	det.getCO_TIPO_GLOS() == Constante.GLOSA_TIPO_SERVICIO_SUPLEMENTARIO && 
						tiHousingOfficeSpace.equals(BigDecimal.ZERO) && 
						tiHousingKvasEnergia.equals(BigDecimal.ZERO)&&
						servGestionadosManosRemotas.equals(BigDecimal.ZERO)){					
						servSupleAdicionales = servSupleAdicionales.add(det.getMON_SERVICIO());
					}
					if(	det.getCO_TIPO_GLOS() == Constante.GLOSA_TIPO_SERVICIO_UNICO && 
						oneShootHousingVtaEquipoTIC.equals(BigDecimal.ZERO) && 
						oneShootHousing.equals(BigDecimal.ZERO)&&
						oneShootCbOneShot.equals(BigDecimal.ZERO)){					
						servUnicAdicionales = servUnicAdicionales.add(det.getMON_SERVICIO());
					}
				}

				String SubTotalFE_Column = " ";
				String SSFE_Column = " " ;
				String SUFE_Column = " " ;
				String SubTotalESC_Column = " " ;
				String SUESC_Column = " " ;
				
				Double SSFE = 0.0 ;
				Double SUESC = 0.0 ;
				Double SUFE = 0.0 ;
				
				System.out.println(rep.getTIP_SERV());
				
				if((rep.getTIP_SERV()).equals("FE"))
				{
				SubTotalFE_Column = rep.getSUB_TOTAL();
				SSFE = Numero.redondear(servSupleAdicionales.doubleValue(), 2);
				SSFE_Column = SSFE.toString();
				SUFE = Numero.redondear(servUnicAdicionales.doubleValue(), 2);
				SUFE_Column = SUFE.toString();
				}
				if((rep.getTIP_SERV()).equals("ESC"))
				{
				SubTotalESC_Column = rep.getSUB_TOTAL();
				SUESC = Numero.redondear(servUnicAdicionales.doubleValue(), 2);	
				SUESC_Column = SUESC.toString();
				}
				
								
				this.excel.writeCols(new Object[] { 
						rep.getCO_FACT(),
						rep.getFE_EMIS(),
						rep.getDE_CODI_BUM(),
						rep.getCO_NEGO(),
						rep.getNO_CLIE(),
						rep.getDE_DIRE_FISC(),
						rep.getRUC(),
						rep.getTIP_SERV(),
						rep.getNO_MONE_FACT(),
						rep.getTI_HOST_DEDI(),
						rep.getTI_HOST_COMP(),
						rep.getTI_CLOUD_SAAS(),
						rep.getTI_CLOUD_IAAS(),
						rep.getSUB_TOTAL(),
						rep.getIGV(),
						rep.getTOTAL_FACTURA(),
						rep.getFE_VENC(),
						"",
						"",
						"",
						"",
						"",
						rep.getTI_CLOUD_MCAFEE(),
						rep.getREDONDEO(),
						rep.getTI_CLOUD_BACK_UP(),
						"",
						"",
						"",
						"",
						"",
						"",
						"",
						rep.getABREV_TIPO_DOCUMENTO(),
						rep.getDE_NUME_DOCU(),
						rep.getDOMICILIADO(),
						rep.getDIST_FISC(),
						"",
						"",
						"",
						"",
						"",
						rep.getTIPO_DOCU(),
						"",
						"",
						"",
						"",
						"",
						"",
						rep.getTI_HOU_RENTA(),
						Numero.redondear(tiHousingOfficeSpace.doubleValue(), 2),//F
						Numero.redondear(tiHousingKvasEnergia.doubleValue(), 2),//F
						"",//F
						Numero.redondear(oneShootHousingVtaEquipoTIC.doubleValue(), 2),//F
						Numero.redondear(oneShootHousing.doubleValue(), 2),//F
						"",//F
						Numero.redondear(tiCloudStaas.doubleValue(), 2),//F
						Numero.redondear(tiCloudOffice.doubleValue(), 2),//F
						Numero.redondear(tiCloudCRM.doubleValue(), 2),//F
						Numero.redondear(servGestionadosManosRemotas.doubleValue(), 2),//F
						Numero.redondear(oneShootCbOneShot.doubleValue(), 2),//F
						Numero.redondear(servSupleAdicionales.doubleValue(), 2),//F
						Numero.redondear(servUnicAdicionales.doubleValue(), 2),//F
						"",
						"",
						SubTotalFE_Column,
						SSFE_Column,
						SUFE_Column,
						"",
						"",
						"",
						SubTotalESC_Column,
						SUESC_Column	
						
					  
						
				});
				
			}
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private void calcularFormatoF() {

		this.excel.writeCols(new Object[] { "FACTURA", "FECHA FACTURA", "COD. CLIENTE", "NEGOCIO", "CLIENTE ",
				"DIRECCION", "RUC", "TIPO SERVICIO", "MONEDA", "94: DEDICADO", "94: COMPARTIDO", "94:SAS", "94:IAAS",
				"SUB TOTAL", "IGV", "TOTAL FACTURA", "FECHA VCTO.", "T.D.Ref.", "N.Doc.Ref.", "T.Cambio Doc Ref",
				"INTERES", "ENTEL Y SCC", "94: Mc Afee Secure", "REDONDEO", " 94:Cloud Back up", "", "", "", "", "", "",
				"Inter\u00e9s (no afecto a IGV-NC)", "TDOC.IDENT", "NRO.IDENT", "DOMICILIADO", "DISTRITO", "", "", "",
				"", "", "TIPO DOCUMENTO", "", "", "94: Interes no afecto a IGV", "94: PENALIDADES",
				"94: fraccionamiento - dev cehque",
		"94: enajenacion Prestac. Serv. TI Equipamiento 7561000 (P-006247)" });

		String codigoFactura;
		DIST dist_fisc;
		List<FACT> facturas;
		PROD prod;
		Double servicio_mensual = 0d;
		Double instalacion = 0d;
		Double alquiler_equipos = 0d;
		Double servicios_suplementarios = 0d;
		Double si_afectos = 0d;
		Double no_afectos = 0d;
		Double sub_total = 0d;

		Double sub_total_Sas = 0d;
		Double sub_total_Iaas = 0d;
		Double sub_total_Sas_Cb = 0d;

		Double igv = 0d;
		Double total = 0d;
		for (CIER cier : listaCier) {

			prod = cier.getProducto();

			List<CIER_DATA_PLAN> data_planes_cobrados_en_cierre = cier.getDataPlanesCobrados_forMaestroGerentes();
			Map<Long, CIER_DATA_PLAN> data_detalle_del_servicio = new TreeMap<Long, CIER_DATA_PLAN>();

			List<NEGO_SUCU_PLAN> nego_sucu_planes = cier.getNEGO_SUCU_PLAN_forMaestroGerentes();
			Map<Integer, NEGO_SUCU_PLAN> map_nego_sucu_planes = new TreeMap<Integer, NEGO_SUCU_PLAN>();

			for (NEGO_SUCU_PLAN nego_sucu_plan : nego_sucu_planes) {
				if (!map_nego_sucu_planes.containsKey(nego_sucu_plan.getCO_NEGO_SUCU_PLAN_ForJSON())) {
					map_nego_sucu_planes.put(nego_sucu_plan.getCO_NEGO_SUCU_PLAN_ForJSON(), nego_sucu_plan);
				}
			}

			String tipo_servicio;

			facturas = cier.getFacturas();
			prod = cier.getProducto();
			for (FACT fact : facturas) {
				dist_fisc = fact.getDist_Fisc_ForJSON();

				servicio_mensual = fact.getIM_SERV_MENS_ForJSON();
				instalacion = fact.getIM_INST_ForJSON();
				alquiler_equipos = fact.getIM_ALQU_ForJSON();
				servicios_suplementarios = fact.getIM_OTRO_ForJSON();
				si_afectos = 0d;
				no_afectos = Math.abs(fact.getIM_AJUS_NIGV_ForJSON());
				igv = fact.getIM_IGV_ForJSON();
				sub_total = fact.getIM_VALO_VENT_NETO_ForJSON();
				total = fact.getIM_TOTA_ForJSON();

				sub_total_Iaas = 0d;
				sub_total_Sas = 0d;
				sub_total_Sas_Cb = 0d;
				tipo_servicio = "";

				if (prod.getCO_PROD_ForJSON() == 8) {
					tipo_servicio = "IAAS";
					sub_total_Iaas = sub_total;
				} else if (prod.getCO_PROD_ForJSON() == 9) {
					sub_total_Sas_Cb = sub_total;
					tipo_servicio = "SAAS - CB";
				} else if (prod.getCO_PROD_ForJSON() == 10) {
					sub_total_Sas = sub_total;
					tipo_servicio = "SAAS - SAS";
				}

				codigoFactura = "";

				if (fact.getCO_FACT_ForJSON() != null) {
					codigoFactura = getFormatoCodigo(fact.getCO_FACT_ForJSON(), "FACT");
				}

				this.excel.writeCols(new Object[] { codigoFactura, fact.getFE_EMIS_ForJSON(),
						fact.getDE_CODI_BUM_ForJSON(), fact.getCO_NEGO_ForJSON(), fact.getNO_CLIE_ForJSON(),
						fact.getDE_DIRE_FISC_ForJSON() + "," + dist_fisc.getNO_DIST_ForJSON() + " - "
								+ dist_fisc.getPROV_ForJSON().getNO_PROV_ForJSON() + " - "
								+ dist_fisc.getPROV_ForJSON().getDEPA_ForJSON().getNO_DEPA_ForJSON(),
								fact.getDE_NUME_DOCU_ForJSON(), tipo_servicio, // TIPO
								// DE
								// SERVICIO
								// 1:INTERNET,
								// 2:DATOS
								fact.getNO_MONE_FACT_ForJSON(), "", "", sub_total_Sas, sub_total_Iaas, // VERIFICAR
								// VERIFICAR
								// COLUMNA
								// 13
								sub_total, igv, // A solicitud al neto se le resta
								// ajuste afecto igv
								total, fact.getFE_VENC_ForJSON(), "", // COLUMNA 18
								"", "", "", "", "", no_afectos, sub_total_Sas_Cb, // 25
								"", "", "", "", "", // 30
								"", "", "RU", fact.getDE_NUME_DOCU_ForJSON(), "S", // 35
								dist_fisc.getNO_DIST_ForJSON(), "", "", "", "", // 40
								"", "FC", "", "", "", // 45
								"", "", "", });

			}
		}

	}

	private void calcularFormatoI() {
		// 57 COLUMNAS
		this.excel.writeCols(new Object[] { "FACTURA ", "FECHA FACTURA", "COD. CLIENTE", "NEGOCIO", "CLIENTE",
				"DIRECCION", "RUC", "TIPO SERVICIO ", "MONEDA ",
				"Produc 95: 7045300/I-09500004/22-02 Intalaciones y Reubicaciones",
				"Produc 95: 7045300/I-09500003/23-02 Renta Mensual",
				"Produc 95: 7045300/I-09500002/23-02 Arriendo de equipos",
				"Produc 95: 7045300/I-09500001/19-00 Servicios Suplementarios", "SUB TOTAL", "IGV", "TOTAL FACTURA ",
				"FECHA VCTO.", "T.D.Ref.", "N.Doc.Ref.", "T.Cambio Doc Ref ", "INTERESES (no afecto a IGV-REC)",
				"Prod 95 /7445300/I-00200012/23-02 Descuentos", "HOSTING", "REDONDEO (no afecto a IGV )",
				"NEXTEL PREPAGO (7042004)", "Arriendo de equipos ID ( prod 97)", "CORRESPONSALIA  NEXTEL    7046400",
				"CORRESPONSALIA  NEXTEL  7046600", "CORRESPONSALIA NEXTEL 7045000", "CORRESPONSALIA  NEXTEL  7046500",
				"SERV. MENSUAL (housing housing - OTH)", "Inter\u00e9s (no afecto a IGV-NC)", "TDOC.IDENT", "NRO.IDENT",
				"DOMICILIADO", "DISTRITO", "VALREFENAC", "VALREFEINT", "DSCTO LL NAC", "DSCTO LL INT", "", "TIPO.DOC",
				"Situaci\u00f3n", "Indicador GC", "Produc 95: 7721000/P-00010000/Intereses  Fracciona",
				"Produc 95: 7045300/I-09500005/23-02 Penalidad incump ctt Datos",
				"Produc 95: 9459200/P-00010000/Interes Dev",
				"Prod 95  : 7561000 / I-09500013 / 21-00 Venta Activo Fijo Datos", "Renta ID- TI",
				"Instalaciones ID- TI", "Arriendo ID- TI",
				"Produc 95: 7045304/I-09500006/19-00 Servicios Suplementarios Datos asoc TI",
				"Produc 95: 7045304/I-09500007/23-02 Arriendo de equipos Datos asoc TI",
				"Produc 95: 7045304/I-09500008/23-02 Renta Mensual Datos asoc TI",
				"Produc 95: 7045304/I-09500009/22-02 Instalaciones y Reubicaciones Datos asoc TI",
				"Servicio Suplementario ID -TI",
				"Produc 95: 7045304/I-09500010/23-02 Penalidad incump ctt Datos asoc TI",
		"Produc 95: 7445304/I-09500011/23-02 Descuentos - Datos asoc TI" }); // 7

		String codigoFactura;
		String codigoBoleta;
		DIST dist_fisc;
		List<RECI> recibos;
		List<FACT> facturas;
		List<BOLE> boletas;
		Double servicio_mensual = 0d;
		Double servicio_mensual_TI = 0d;
		Double instalacion = 0d;
		Double instalacion_TI = 0d;
		Double alquiler_equipos = 0d;
		Double alquiler_equipos_TI = 0d;
		Double servicios_suplementarios = 0d;
		Double servicios_suplementarios_TI = 0d;
		Double si_afectos = 0d;
		Double no_afectos = 0d;
		Double sub_total = 0d;
		Double igv = 0d;
		Double total = 0d;
		CLIE cliente = null;
		for (CIER cier : listaCier) {
			
			recibos = cier.getRecibos();

			List<CIER_DATA_PLAN> data_planes_cobrados_en_cierre = cier.getDataPlanesCobrados_forMaestroGerentes();
			Map<Long, CIER_DATA_PLAN> data_detalle_del_servicio = new TreeMap<Long, CIER_DATA_PLAN>();

			List<NEGO_SUCU_PLAN> nego_sucu_planes = cier.getNEGO_SUCU_PLAN_forMaestroGerentes();
			Map<Integer, NEGO_SUCU_PLAN> map_nego_sucu_planes = new TreeMap<Integer, NEGO_SUCU_PLAN>();

			for (CIER_DATA_PLAN cier_data_plan : data_planes_cobrados_en_cierre) {
				if (cier_data_plan.getCO_RECI() != null) {
					if (!data_detalle_del_servicio.containsKey(cier_data_plan.getCO_RECI())) {
						data_detalle_del_servicio.put(cier_data_plan.getCO_RECI(), cier_data_plan);
						// cantidad_planes_por_recibos.put(cier_data_plan.getCO_RECI(),
						// 0);
					}
				} else if (cier_data_plan.getCO_BOLE() != null) {
					if (!data_detalle_del_servicio.containsKey(cier_data_plan.getCO_BOLE())) {
						data_detalle_del_servicio.put(cier_data_plan.getCO_BOLE(), cier_data_plan);
						// cantidad_planes_por_recibos.put(cier_data_plan.getCO_RECI(),
						// 0);
					}
				}
			}

			for (NEGO_SUCU_PLAN nego_sucu_plan : nego_sucu_planes) {
				if (!map_nego_sucu_planes.containsKey(nego_sucu_plan.getCO_NEGO_SUCU_PLAN_ForJSON())) {
					map_nego_sucu_planes.put(nego_sucu_plan.getCO_NEGO_SUCU_PLAN_ForJSON(), nego_sucu_plan);
				}
			}

			CIER_DATA_PLAN cier_data_plan = null;
			NEGO_SUCU_PLAN nego_sucu_plan = null;
			Boolean ST_BACKUP_BU;
			String tipo_servicio;
			for (RECI recibo : recibos) {
				servicio_mensual = 0d;           
				servicio_mensual_TI = 0d;        
				instalacion = 0d;                
				instalacion_TI = 0d;             
				alquiler_equipos = 0d;           
				alquiler_equipos_TI = 0d;        
				servicios_suplementarios = 0d;   
				servicios_suplementarios_TI = 0d;
				
				dist_fisc = recibo.getDist_Fisc_ForJSON();
				cliente = recibo.obtenerCliente();
				
				si_afectos = Math.abs(recibo.getIM_AJUS_SIGV_ForJSON());
				no_afectos = Math.abs(recibo.getIM_AJUS_NIGV_ForJSON());
				igv = recibo.getIM_IGV_ForJSON();
				// sub_total=recibo.getIM_NETO_ForJSON();
				sub_total = com.americatel.facturacion.fncs.Numero
						.redondear(recibo.getIM_NETO_ForJSON() + recibo.getIM_AJUS_SIGV_ForJSON(), 2);
				total = recibo.getIM_TOTA_ForJSON();
				ST_BACKUP_BU = false;
				tipo_servicio = "";
				cier_data_plan = null;
				nego_sucu_plan = null;
				

				if (data_detalle_del_servicio.containsKey(recibo.getCO_RECI_ForJSON())) {
					cier_data_plan = data_detalle_del_servicio.get(recibo.getCO_RECI_ForJSON());
					if (map_nego_sucu_planes.containsKey(cier_data_plan.getCO_NEGO_SUCU_PLAN())) {
						nego_sucu_plan = map_nego_sucu_planes.get(cier_data_plan.getCO_NEGO_SUCU_PLAN());
						ST_BACKUP_BU = nego_sucu_plan.getPLAN().getRELACIONADO_TI_ForJSON();
					}
					// cantidad_sucursales=cantidad_planes_por_recibos.get(recibo.getCO_RECI_ForJSON());
				}

				if (ST_BACKUP_BU) {
					tipo_servicio = "ADD (ICX-TI)";
					servicio_mensual_TI = com.americatel.facturacion.fncs.Numero
							.redondear(recibo.getIM_SERV_MENS_ForJSON() + recibo.getIM_DIF_CARGO_FIJO_ForJSON(), 2);
					instalacion_TI = recibo.getIM_INST_ForJSON();
					alquiler_equipos_TI = recibo.getIM_ALQU_ForJSON();
					servicios_suplementarios_TI = recibo.getIM_OTRO_ForJSON();

				} else {
					tipo_servicio = "ADD";
					servicio_mensual = com.americatel.facturacion.fncs.Numero
							.redondear(recibo.getIM_SERV_MENS_ForJSON() + recibo.getIM_DIF_CARGO_FIJO_ForJSON(), 2);
					instalacion = recibo.getIM_INST_ForJSON();
					alquiler_equipos = recibo.getIM_ALQU_ForJSON();
					servicios_suplementarios = recibo.getIM_OTRO_ForJSON();
				}

				this.excel.writeCols(new Object[] { recibo.getCO_RECI_ForJSON(), recibo.getFE_EMIS_ForJSON(),
						recibo.getDE_CODI_BUM_ForJSON(), recibo.getCO_NEGO_ForJSON(), recibo.getNO_CLIE_ForJSON(),
						recibo.getDE_DIRE_FISC_ForJSON() + "," + dist_fisc.getNO_DIST_ForJSON() + " - "
								+ dist_fisc.getPROV_ForJSON().getNO_PROV_ForJSON() + " - "
								+ dist_fisc.getPROV_ForJSON().getDEPA_ForJSON().getNO_DEPA_ForJSON(),
								recibo.getDE_NUME_DOCU_ForJSON(), tipo_servicio, // TIPO
								recibo.getNO_MONE_FACT_ForJSON(), instalacion, servicio_mensual, alquiler_equipos,
								servicios_suplementarios, // VERIFICAR VERIFICAR COLUMNA
								// 13
								sub_total, igv, // A solicitud al neto se le resta
								total, recibo.getFE_VENC_ForJSON(), "", // COLUMNA 18
								"", "", "", si_afectos, "",no_afectos,  "", // 25
								"", "", "", "", "", // 30
								"", "", Constante.abreviaturaTipoDocumento.get(cliente.getCO_TIPO_DOCU_ForJSON()), recibo.getDE_NUME_DOCU_ForJSON(), "S", // 35
								dist_fisc.getNO_DIST_ForJSON(), "", "", "", "", // 40
								"", "RC", "", "", "", // 45
								"", "", "", "", "", // 50
								"", servicios_suplementarios_TI, alquiler_equipos_TI, servicio_mensual_TI, instalacion_TI, // 55
								"", "", "", });
					
			}

			boletas = cier.getBoletas();

			for (BOLE boleta : boletas) {
				servicio_mensual = 0d;           
				servicio_mensual_TI = 0d;        
				instalacion = 0d;                
				instalacion_TI = 0d;             
				alquiler_equipos = 0d;           
				alquiler_equipos_TI = 0d;        
				servicios_suplementarios = 0d;   
				servicios_suplementarios_TI = 0d;
				
				dist_fisc = boleta.getDist_Fisc_ForJSON();
				cliente = boleta.obtenerCliente();
				instalacion = boleta.getIM_INST_ForJSON();
				alquiler_equipos = boleta.getIM_ALQU_ForJSON();
				servicios_suplementarios = boleta.getIM_OTRO_ForJSON();
				si_afectos = Math.abs(boleta.getIM_AJUS_SIGV_ForJSON());
				no_afectos = Math.abs(boleta.getIM_AJUS_NIGV_ForJSON());
				igv = boleta.getIM_IGV_ForJSON();
				sub_total = com.americatel.facturacion.fncs.Numero
						.redondear(boleta.getIM_NETO_ForJSON() + boleta.getIM_AJUS_SIGV_ForJSON(), 2);
				total = boleta.getIM_TOTA_ForJSON();
				ST_BACKUP_BU = false;
				tipo_servicio = "";
				cier_data_plan = null;
				nego_sucu_plan = null;

				if (data_detalle_del_servicio.containsKey(boleta.getCO_BOLE_ForJSON())) {
					cier_data_plan = data_detalle_del_servicio.get(boleta.getCO_BOLE_ForJSON());
					if (map_nego_sucu_planes.containsKey(cier_data_plan.getCO_NEGO_SUCU_PLAN())) {
						nego_sucu_plan = map_nego_sucu_planes.get(cier_data_plan.getCO_NEGO_SUCU_PLAN());
						ST_BACKUP_BU = nego_sucu_plan.getPLAN().getRELACIONADO_TI_ForJSON();
					}
					// cantidad_sucursales=cantidad_planes_por_recibos.get(recibo.getCO_RECI_ForJSON());
				}

				if (ST_BACKUP_BU) {
					tipo_servicio = "ADD (ICX-TI)";
					servicio_mensual_TI = com.americatel.facturacion.fncs.Numero
							.redondear(boleta.getIM_SERV_MENS_ForJSON() + boleta.getIM_DIF_CARGO_FIJO_ForJSON(), 2);
					instalacion_TI = boleta.getIM_INST_ForJSON();
					alquiler_equipos_TI = boleta.getIM_ALQU_ForJSON();
					servicios_suplementarios_TI = boleta.getIM_OTRO_ForJSON();

				} else {
					tipo_servicio = "ADD";
					servicio_mensual = com.americatel.facturacion.fncs.Numero
							.redondear(boleta.getIM_SERV_MENS_ForJSON() + boleta.getIM_DIF_CARGO_FIJO_ForJSON(), 2);
					instalacion = boleta.getIM_INST_ForJSON();
					alquiler_equipos = boleta.getIM_ALQU_ForJSON();
					servicios_suplementarios = boleta.getIM_OTRO_ForJSON();

				}

				codigoBoleta = "";

				if (boleta.getCO_BOLE_ForJSON() != null) {
					codigoBoleta = getFormatoCodigo(boleta.getCO_BOLE_ForJSON(), "BOLE");
				}

				this.excel.writeCols(new Object[] { codigoBoleta, boleta.getFE_EMIS_ForJSON(),
						boleta.getDE_CODI_BUM_ForJSON(), boleta.getCO_NEGO_ForJSON(), boleta.getNO_CLIE_ForJSON(),
						boleta.getDE_DIRE_FISC_ForJSON() + "," + dist_fisc.getNO_DIST_ForJSON() + " - "
								+ dist_fisc.getPROV_ForJSON().getNO_PROV_ForJSON() + " - "
								+ dist_fisc.getPROV_ForJSON().getDEPA_ForJSON().getNO_DEPA_ForJSON(),
								boleta.getDE_NUME_DOCU_ForJSON(), tipo_servicio, // TIPO
								boleta.getNO_MONE_FACT_ForJSON(), instalacion, servicio_mensual, alquiler_equipos,
								servicios_suplementarios, // VERIFICAR VERIFICAR COLUMNA
								// 13
								sub_total, igv, // A solicitud al neto se le resta
								// ajuste afecto igv
								total, boleta.getFE_VENC_ForJSON(), "", // COLUMNA 18
								"", "", "", si_afectos, "",no_afectos,  "", // 25
								"", "", "", "", "", // 30
								"", "", Constante.abreviaturaTipoDocumento.get(cliente.getCO_TIPO_DOCU_ForJSON()), boleta.getDE_NUME_DOCU_ForJSON(), "S", // 35
								dist_fisc.getNO_DIST_ForJSON(), "", "", "", "", // 40
								"", "BV", "", "", "", // 45
								"", "", "", "", "", // 50
								"", servicios_suplementarios_TI, alquiler_equipos_TI, servicio_mensual_TI, instalacion_TI, // 55
								"", "", "", });

			}

			facturas = cier.getFacturas();
			for (FACT fact : facturas) {
				servicio_mensual = 0d;           
				servicio_mensual_TI = 0d;        
				instalacion = 0d;                
				instalacion_TI = 0d;             
				alquiler_equipos = 0d;           
				alquiler_equipos_TI = 0d;        
				servicios_suplementarios = 0d;   
				servicios_suplementarios_TI = 0d;
				
				dist_fisc = fact.getDist_Fisc_ForJSON();
				cliente = fact.obtenerCliente();
				instalacion = fact.getIM_INST_ForJSON();
				alquiler_equipos = fact.getIM_ALQU_ForJSON();
				servicios_suplementarios = fact.getIM_OTRO_ForJSON();
				si_afectos = 0d;
				no_afectos = Math.abs(fact.getIM_AJUS_NIGV_ForJSON());
				igv = fact.getIM_IGV_ForJSON();
				sub_total = fact.getIM_VALO_VENT_NETO_ForJSON();
				total = fact.getIM_TOTA_ForJSON();

				ST_BACKUP_BU = false;
				tipo_servicio = "";
				cier_data_plan = null;
				nego_sucu_plan = null;

				for (RECI recibo : recibos) {
					if (recibo.getCO_NEGO_ForJSON().equals(fact.getCO_NEGO_ForJSON())) {
						if (data_detalle_del_servicio.containsKey(recibo.getCO_RECI_ForJSON())) {
							cier_data_plan = data_detalle_del_servicio.get(recibo.getCO_RECI_ForJSON());
							if (map_nego_sucu_planes.containsKey(cier_data_plan.getCO_NEGO_SUCU_PLAN())) {
								nego_sucu_plan = map_nego_sucu_planes.get(cier_data_plan.getCO_NEGO_SUCU_PLAN());
								ST_BACKUP_BU = nego_sucu_plan.getPLAN().getRELACIONADO_TI_ForJSON();
							}
						}
					}
				}

				if (ST_BACKUP_BU) {
					tipo_servicio = "ADD (ICX-TI)";
					servicio_mensual_TI = fact.getIM_SERV_MENS_ForJSON();
					instalacion_TI = fact.getIM_INST_ForJSON();
					alquiler_equipos_TI = fact.getIM_ALQU_ForJSON();
					servicios_suplementarios_TI = fact.getIM_OTRO_ForJSON();
				} else {
					tipo_servicio = "ADD";
					servicio_mensual = fact.getIM_SERV_MENS_ForJSON();
					instalacion = fact.getIM_INST_ForJSON();
					alquiler_equipos = fact.getIM_ALQU_ForJSON();
					servicios_suplementarios = fact.getIM_OTRO_ForJSON();
				}

				codigoFactura = "";

				if (fact.getCO_FACT_ForJSON() != null) {
					codigoFactura = getFormatoCodigo(fact.getCO_FACT_ForJSON(), "FACT");
				}

				this.excel.writeCols(new Object[] { codigoFactura, fact.getFE_EMIS_ForJSON(),
						fact.getDE_CODI_BUM_ForJSON(), fact.getCO_NEGO_ForJSON(), fact.getNO_CLIE_ForJSON(),
						fact.getDE_DIRE_FISC_ForJSON() + "," + dist_fisc.getNO_DIST_ForJSON() + " - "
								+ dist_fisc.getPROV_ForJSON().getNO_PROV_ForJSON() + " - "
								+ dist_fisc.getPROV_ForJSON().getDEPA_ForJSON().getNO_DEPA_ForJSON(),
								fact.getDE_NUME_DOCU_ForJSON(), tipo_servicio, // TIPO
								// DE
								// SERVICIO
								// 1:INTERNET,
								// 2:DATOS
								fact.getNO_MONE_FACT_ForJSON(), instalacion, servicio_mensual, alquiler_equipos,
								servicios_suplementarios, // VERIFICAR VERIFICAR COLUMNA
								// 13
								sub_total, igv, // A solicitud al neto se le resta
								// ajuste afecto igv
								total, fact.getFE_VENC_ForJSON(), "", // COLUMNA 18
								"", "", "", si_afectos, "", no_afectos,  "", // 25
								"", "", "", "", "", // 30
								"", "", Constante.abreviaturaTipoDocumento.get(cliente.getCO_TIPO_DOCU_ForJSON()), fact.getDE_NUME_DOCU_ForJSON(), "S", // 35
								dist_fisc.getNO_DIST_ForJSON(), "", "", "", "", // 40
								"", "FC", "", "", "", // 45
								"", "", "", "", "", // 50
								"", servicios_suplementarios_TI, alquiler_equipos_TI, servicio_mensual_TI, instalacion_TI, // 55
								"", "", "", });

			}

		}

	}

	private void calcularFormatoH() {

		// 58 COLUMNAS
		this.excel.writeCols(new Object[] { "RECIBO", "FECHA FACTURA", "COD. CLIENTE ", "NEGOCIO", "CLIENTE ",
				"DIRECCION ", "RUC", "TIPO SERVICIO ", "MONEDA ",
				"Prod 02 /7045100/I-00200004/13-00 Instalaciones y Reubicaciones",
				"Prod 02 /7045100/I-00200003/14-00 Renta Mensual",
				"Prod 02 /7045100/I-00200011/Arriendo de equipos Internet no esta esficificado consultar KPMG", "OTROS",
				"SUB TOTAL", "IGV", "TOTAL FACTURA ", "FECHA VCTO.", "T.D.Ref.", "N.Doc.Ref.", "T.Cambio Doc Ref",
				"INTERESES (no afecto a IGV-REC)", "Prod 02 /7445100/I-00200002/14-00 Descuentos", "HOSTING",
				"REDONDEO (no afecto a IGV )", "NEXTEL PREPAGO (7042004)", "", "CORRESPONSALIA  NEXTEL    7046400",
				"CORRESPONSALIA  NEXTEL  7046600", "CORRESPONSALIA NEXTEL 7045000", "CORRESPONSALIA  NEXTEL  7046500",
				"SERV.MENSUAL (housing housing - OTH)", "Inter\u00e9s (no afecto a IGV-NC)", "TDOC.IDENT", "NRO.IDENT",
				"DOMICILIADO", "DISTRITO", "VALREFENAC", "VALREFEINT", "DSCTO LL NAC", "DSCTO LL INT", "", "TIPO.DOC",
				"Situaci\u00f3n", "Indicador GC", "Prod 02 /9459200/P-00010000/Intereses  no afecto a IGV    ",
				"Prod 02 /7045100/I-00200005/14-00 Penalidad incumplimiento de ctt Internet",
				"Prod 02 /7721000/P-00010000/Intereses -Fraccionamiento",
				"Prod 02  : 7561000 / I-00200013 / 21-00 Venta Activo Fijo Internet",
				"Prod 02 /7045104/I-00200007/14-00 Renta Mensual Internet asoc TI",
				"Prod 02 /7045104/I-00200008/13-00 Instalaciones y Reubicaciones Internet asoc TI",
				"Prod 02 /7045104/P-04253117/Arriendo de equipos Internet asoc TI N/A", "Servicio Suplementario DD -TI",
				"Renta DD- TI", "Instalaciones DD- TI", "Arriendo DD- TI",
				"Prod 02 /7045104/I-00200006/19-00 Servicios Suplementarios Internet asoc TI ",
				"Prod 02 /7045104/I-00200009/14-00 Penalidad incump ctt Internet asoc TI",
		"Prod 02 /7445104/I-00200010/14-00 Descuentos - Internet asoc TI" });// 8

		String codigoFactura;
		String codigoBoleta;
		DIST dist_fisc;
		List<RECI> recibos;
		List<FACT> facturas;
		List<BOLE> boletas;
		Double servicio_mensual = 0d;
		Double servicio_mensual_TI = 0d;
		Double instalacion = 0d;
		Double instalacion_TI = 0d;
		Double alquiler_equipos = 0d;
		Double alquiler_equipos_TI = 0d;
		Double servicios_suplementarios = 0d;
		Double servicios_suplementarios_TI = 0d;
		Double si_afectos = 0d;
		Double no_afectos = 0d;
		Double sub_total = 0d;
		Double igv = 0d;
		Double total = 0d;
		CLIE cliente = null;
		for (CIER cier : listaCier) {

			recibos = cier.getRecibos();

			List<CIER_DATA_PLAN> data_planes_cobrados_en_cierre = cier.getDataPlanesCobrados_forMaestroGerentes();
			Map<Long, CIER_DATA_PLAN> data_detalle_del_servicio = new TreeMap<Long, CIER_DATA_PLAN>();

			List<NEGO_SUCU_PLAN> nego_sucu_planes = cier.getNEGO_SUCU_PLAN_forMaestroGerentes();
			Map<Integer, NEGO_SUCU_PLAN> map_nego_sucu_planes = new TreeMap<Integer, NEGO_SUCU_PLAN>();

			for (CIER_DATA_PLAN cier_data_plan : data_planes_cobrados_en_cierre) {
				if (cier_data_plan.getCO_RECI() != null) {
					if (!data_detalle_del_servicio.containsKey(cier_data_plan.getCO_RECI())) {
						data_detalle_del_servicio.put(cier_data_plan.getCO_RECI(), cier_data_plan);
					}
				} else if (cier_data_plan.getCO_BOLE() != null) {
					if (!data_detalle_del_servicio.containsKey(cier_data_plan.getCO_BOLE())) {
						data_detalle_del_servicio.put(cier_data_plan.getCO_BOLE(), cier_data_plan);
					}
				}

			}

			for (NEGO_SUCU_PLAN nego_sucu_plan : nego_sucu_planes) {
				if (!map_nego_sucu_planes.containsKey(nego_sucu_plan.getCO_NEGO_SUCU_PLAN_ForJSON())) {
					map_nego_sucu_planes.put(nego_sucu_plan.getCO_NEGO_SUCU_PLAN_ForJSON(), nego_sucu_plan);
				}
			}

			CIER_DATA_PLAN cier_data_plan = null;
			NEGO_SUCU_PLAN nego_sucu_plan = null;
			Boolean ST_BACKUP_BU;
			String tipo_servicio;
			for (RECI recibo : recibos) {
				servicio_mensual = 0d;           
				servicio_mensual_TI = 0d;        
				instalacion = 0d;                
				instalacion_TI = 0d;             
				alquiler_equipos = 0d;           
				alquiler_equipos_TI = 0d;        
				servicios_suplementarios = 0d;   
				servicios_suplementarios_TI = 0d;
				
				dist_fisc = recibo.getDist_Fisc_ForJSON();
				cliente = recibo.obtenerCliente();
				
				si_afectos = Math.abs(recibo.getIM_AJUS_SIGV_ForJSON());
				no_afectos = Math.abs(recibo.getIM_AJUS_NIGV_ForJSON());
				igv = recibo.getIM_IGV_ForJSON();
				// sub_total=recibo.getIM_NETO_ForJSON();
				sub_total = com.americatel.facturacion.fncs.Numero
						.redondear(recibo.getIM_NETO_ForJSON() + recibo.getIM_AJUS_SIGV_ForJSON(), 2);
				total = recibo.getIM_TOTA_ForJSON();
				ST_BACKUP_BU = false;
				tipo_servicio = "";
				cier_data_plan = null;
				nego_sucu_plan = null;

				if (data_detalle_del_servicio.containsKey(recibo.getCO_RECI_ForJSON())) {
					cier_data_plan = data_detalle_del_servicio.get(recibo.getCO_RECI_ForJSON());
					if (map_nego_sucu_planes.containsKey(cier_data_plan.getCO_NEGO_SUCU_PLAN())) {
						nego_sucu_plan = map_nego_sucu_planes.get(cier_data_plan.getCO_NEGO_SUCU_PLAN());
						ST_BACKUP_BU = nego_sucu_plan.getPLAN().getRELACIONADO_TI_ForJSON();
						;
					}
				}

				if (ST_BACKUP_BU) {
					tipo_servicio = "IPB (ICX-TI)";
					servicio_mensual_TI = com.americatel.facturacion.fncs.Numero
							.redondear(recibo.getIM_SERV_MENS_ForJSON() + recibo.getIM_DIF_CARGO_FIJO_ForJSON(), 2);
					instalacion_TI 				= recibo.getIM_INST_ForJSON();				
					alquiler_equipos_TI			= recibo.getIM_ALQU_ForJSON();
					servicios_suplementarios_TI = recibo.getIM_OTRO_ForJSON();
				} else {
					tipo_servicio = "IPB";
					servicio_mensual 			= com.americatel.facturacion.fncs.Numero
													.redondear(recibo.getIM_SERV_MENS_ForJSON() + recibo.getIM_DIF_CARGO_FIJO_ForJSON(), 2);
					instalacion 				= recibo.getIM_INST_ForJSON();				
					alquiler_equipos 			= recibo.getIM_ALQU_ForJSON();
					servicios_suplementarios 	= recibo.getIM_OTRO_ForJSON();
				}

				this.excel.writeCols(new Object[] { recibo.getCO_RECI_ForJSON(), recibo.getFE_EMIS_ForJSON(),
						recibo.getDE_CODI_BUM_ForJSON(), recibo.getCO_NEGO_ForJSON(), recibo.getNO_CLIE_ForJSON(),
						recibo.getDE_DIRE_FISC_ForJSON() + "," + dist_fisc.getNO_DIST_ForJSON() + " - "
								+ dist_fisc.getPROV_ForJSON().getNO_PROV_ForJSON() + " - "
								+ dist_fisc.getPROV_ForJSON().getDEPA_ForJSON().getNO_DEPA_ForJSON(),
								recibo.getDE_NUME_DOCU_ForJSON(), tipo_servicio, // TIPO
								recibo.getNO_MONE_FACT_ForJSON(), instalacion, servicio_mensual, alquiler_equipos,
								servicios_suplementarios, // VERIFICAR VERIFICAR COLUMNA
								// 13
								sub_total, igv, // A solicitud al neto se le resta
								total, recibo.getFE_VENC_ForJSON(), "", // COLUMNA 18
								"", "", "", si_afectos, "", no_afectos, "", "", "", "", "", "", // 30
								"", "", Constante.abreviaturaTipoDocumento.get(cliente.getCO_TIPO_DOCU_ForJSON()), // validar tipo documento
								recibo.getDE_NUME_DOCU_ForJSON(), "S", // 35
								dist_fisc.getNO_DIST_ForJSON(), // DISTRITO
								"", "", "", "", // 40
								"", "RC", "", "", "", // 45
								"", "", "", servicio_mensual_TI, instalacion_TI, // 50
								alquiler_equipos_TI, "", "", "", "", // 55
								servicios_suplementarios_TI, "", "", });
				
			}

			boletas = cier.getBoletas();

			for (BOLE boleta : boletas) {
				servicio_mensual = 0d;
				servicio_mensual_TI = 0d;
				instalacion = 0d;       
				instalacion_TI = 0d;     
				alquiler_equipos = 0d;
				alquiler_equipos_TI = 0d;     
				servicios_suplementarios = 0d;
				servicios_suplementarios_TI = 0d;
				
				dist_fisc = boleta.getDist_Fisc_ForJSON();
				cliente = boleta.obtenerCliente();
				instalacion = boleta.getIM_INST_ForJSON();
				alquiler_equipos = boleta.getIM_ALQU_ForJSON();
				servicios_suplementarios = boleta.getIM_OTRO_ForJSON();
				si_afectos = Math.abs(boleta.getIM_AJUS_SIGV_ForJSON());
				no_afectos = Math.abs(boleta.getIM_AJUS_NIGV_ForJSON());
				igv = boleta.getIM_IGV_ForJSON();
				// sub_total=recibo.getIM_NETO_ForJSON();
				sub_total = com.americatel.facturacion.fncs.Numero
						.redondear(boleta.getIM_NETO_ForJSON() + boleta.getIM_AJUS_SIGV_ForJSON(), 2);
				total = boleta.getIM_TOTA_ForJSON();
				ST_BACKUP_BU = false;
				tipo_servicio = "";
				cier_data_plan = null;
				nego_sucu_plan = null;

				if (data_detalle_del_servicio.containsKey(boleta.getCO_BOLE_ForJSON())) {
					cier_data_plan = data_detalle_del_servicio.get(boleta.getCO_BOLE_ForJSON());
					if (map_nego_sucu_planes.containsKey(cier_data_plan.getCO_NEGO_SUCU_PLAN())) {
						nego_sucu_plan = map_nego_sucu_planes.get(cier_data_plan.getCO_NEGO_SUCU_PLAN());
						ST_BACKUP_BU = nego_sucu_plan.getPLAN().getRELACIONADO_TI_ForJSON();
						;
					}
					// cantidad_sucursales=cantidad_planes_por_recibos.get(recibo.getCO_RECI_ForJSON());
				}

				if (ST_BACKUP_BU) {
					tipo_servicio = "IPB (ICX-TI)";
					servicio_mensual_TI = com.americatel.facturacion.fncs.Numero
							.redondear(boleta.getIM_SERV_MENS_ForJSON() + boleta.getIM_DIF_CARGO_FIJO_ForJSON(), 2);
					instalacion_TI 				= boleta.getIM_INST_ForJSON();				
					alquiler_equipos_TI			= boleta.getIM_ALQU_ForJSON();
					servicios_suplementarios_TI = boleta.getIM_OTRO_ForJSON();
				} else {
					tipo_servicio = "IPB";
					servicio_mensual 			= com.americatel.facturacion.fncs.Numero
													.redondear(boleta.getIM_SERV_MENS_ForJSON() + boleta.getIM_DIF_CARGO_FIJO_ForJSON(), 2);
					instalacion 				= boleta.getIM_INST_ForJSON();				
					alquiler_equipos 			= boleta.getIM_ALQU_ForJSON();
					servicios_suplementarios 	= boleta.getIM_OTRO_ForJSON();
				}

				codigoBoleta = "";

				if (boleta.getCO_BOLE_ForJSON() != null) {
					codigoBoleta = getFormatoCodigo(boleta.getCO_BOLE_ForJSON(), "BOLE");
				}

				this.excel.writeCols(new Object[] { codigoBoleta, boleta.getFE_EMIS_ForJSON(),
						boleta.getDE_CODI_BUM_ForJSON(), boleta.getCO_NEGO_ForJSON(), boleta.getNO_CLIE_ForJSON(),
						boleta.getDE_DIRE_FISC_ForJSON() + "," + dist_fisc.getNO_DIST_ForJSON() + " - "
								+ dist_fisc.getPROV_ForJSON().getNO_PROV_ForJSON() + " - "
								+ dist_fisc.getPROV_ForJSON().getDEPA_ForJSON().getNO_DEPA_ForJSON(),
								boleta.getDE_NUME_DOCU_ForJSON(), tipo_servicio, // TIPO
								// DE
								// SERVICIO
								// 1:INTERNET,
								// 2:DATOS
								boleta.getNO_MONE_FACT_ForJSON(), instalacion, servicio_mensual, alquiler_equipos,
								servicios_suplementarios, // VERIFICAR VERIFICAR COLUMNA
								// 13
								sub_total, igv, // A solicitud al neto se le resta
								// ajuste afecto igv
								total, boleta.getFE_VENC_ForJSON(), "", // COLUMNA 18
								"", "", "", si_afectos, "", no_afectos,  "", // 25
								"", "", "", "", "", // 30
								"", "", Constante.abreviaturaTipoDocumento.get(cliente.getCO_TIPO_DOCU_ForJSON()), boleta.getDE_NUME_DOCU_ForJSON(), "S", // 35
								dist_fisc.getNO_DIST_ForJSON(), "", "", "", "", // 40
								"", "BV", "", "", "", // 45
								"", "", "", servicio_mensual_TI, instalacion_TI, // 50
								alquiler_equipos_TI, "", "", "", "", // 55
								servicios_suplementarios_TI, "", "", });

			}

			facturas = cier.getFacturas();

			for (FACT fact : facturas) {
				servicio_mensual = 0d;           
				servicio_mensual_TI = 0d;        
				instalacion = 0d;                
				instalacion_TI = 0d;             
				alquiler_equipos = 0d;           
				alquiler_equipos_TI = 0d;        
				servicios_suplementarios = 0d;   
				servicios_suplementarios_TI = 0d;
				
				dist_fisc = fact.getDist_Fisc_ForJSON();
				cliente = fact.obtenerCliente();
				instalacion = fact.getIM_INST_ForJSON();
				alquiler_equipos = fact.getIM_ALQU_ForJSON();
				servicios_suplementarios = fact.getIM_OTRO_ForJSON();
				si_afectos = 0d;
				no_afectos = Math.abs(fact.getIM_AJUS_NIGV_ForJSON());
				igv = fact.getIM_IGV_ForJSON();
				sub_total = fact.getIM_VALO_VENT_NETO_ForJSON();
				total = fact.getIM_TOTA_ForJSON();

				ST_BACKUP_BU = false;
				tipo_servicio = "";
				cier_data_plan = null;
				nego_sucu_plan = null;

				for (RECI recibo : recibos) {
					if (recibo.getCO_NEGO_ForJSON().equals(fact.getCO_NEGO_ForJSON())) {
						if (data_detalle_del_servicio.containsKey(recibo.getCO_RECI_ForJSON())) {
							cier_data_plan = data_detalle_del_servicio.get(recibo.getCO_RECI_ForJSON());
							if (map_nego_sucu_planes.containsKey(cier_data_plan.getCO_NEGO_SUCU_PLAN())) {
								nego_sucu_plan = map_nego_sucu_planes.get(cier_data_plan.getCO_NEGO_SUCU_PLAN());
								ST_BACKUP_BU = nego_sucu_plan.getPLAN().getRELACIONADO_TI_ForJSON();

							}
						}
					}
				}

				if (ST_BACKUP_BU) {
					tipo_servicio = "IPB (ICX-TI)";
					servicio_mensual_TI = fact.getIM_SERV_MENS_ForJSON();
					instalacion_TI 				= fact.getIM_INST_ForJSON();				
					alquiler_equipos_TI			= fact.getIM_ALQU_ForJSON();
					servicios_suplementarios_TI = fact.getIM_OTRO_ForJSON();
				} else {
					tipo_servicio = "IPB";
					servicio_mensual = fact.getIM_SERV_MENS_ForJSON();
					instalacion 				= fact.getIM_INST_ForJSON();				
					alquiler_equipos 			= fact.getIM_ALQU_ForJSON();
					servicios_suplementarios 	= fact.getIM_OTRO_ForJSON();
				}

				codigoFactura = "";

				if (fact.getCO_FACT_ForJSON() != null) {
					codigoFactura = getFormatoCodigo(fact.getCO_FACT_ForJSON(), "FACT");
				}

				this.excel.writeCols(new Object[] { codigoFactura, fact.getFE_EMIS_ForJSON(),
						fact.getDE_CODI_BUM_ForJSON(), fact.getCO_NEGO_ForJSON(), fact.getNO_CLIE_ForJSON(),
						fact.getDE_DIRE_FISC_ForJSON() + "," + dist_fisc.getNO_DIST_ForJSON() + " - "
								+ dist_fisc.getPROV_ForJSON().getNO_PROV_ForJSON() + " - "
								+ dist_fisc.getPROV_ForJSON().getDEPA_ForJSON().getNO_DEPA_ForJSON(),
								fact.getDE_NUME_DOCU_ForJSON(), tipo_servicio, // TIPO
								fact.getNO_MONE_FACT_ForJSON(), instalacion, servicio_mensual, alquiler_equipos,
								servicios_suplementarios, // VERIFICAR VERIFICAR COLUMNA
								// 13
								sub_total, igv, // A solicitud al neto se le resta
								// ajuste afecto igv
								total, fact.getFE_VENC_ForJSON(), "", // COLUMNA 18
								"", "", "", si_afectos, "",no_afectos, "",  // 25
								"", "", "", "", "", // 30
								"", "", Constante.abreviaturaTipoDocumento.get(cliente.getCO_TIPO_DOCU_ForJSON()), fact.getDE_NUME_DOCU_ForJSON(), "S", // 35
								dist_fisc.getNO_DIST_ForJSON(), "", "", "", "", // 40
								"", "FC", "", "", "", // 45
								"", "", "", servicio_mensual_TI, instalacion_TI, // 50
								alquiler_equipos_TI, "", "", "", "", // 55
								servicios_suplementarios_TI, "", "", });
			}

		}
	}

	private void calcular() {
		this.excel.writeCols(new Object[] { "N\u00b0 DOCUMENTO", "FECHA EMISION", "COD. CLIENTE", "NEGOCIO",
				"RAZON SOCIAL", "DIRECCI\u00d3N FISCAL", "RUC", "TIPO SERVICIO", "MONEDA", "INSTALACI\u00d3N DATOS",
				"SERV. MENSUAL DATOS", "ALQ. EQUIPOS DATOS", "SERV. SUPLEMENTARIO DATOS", "SUB TOTAL", "IGV",
				"TOTAL FACTURA", "FECHA VCTO.", ".", ".", ".", "AJUSTES AFECTOS", ".", "INSTALACI\u00d3N INTERNET",
				"AJUSTES NO AFECTOS", "SEV. MENSUAL INTERNET", "ALQ. EQUIPOS INTERNET", "SERV. SUPLEMENTARIO INTERNET",
				".", ".", ".", ".", ".", "TIPO DOCUMENTO IDENTIDAD", "NRO. DOCUMENTO", "DOMICILIADO", "DISTRITO FISCAL",
				".", ".", ".", ".", ".", "TIPO DOCUMENTO" });

		String codigoFactura;
		String codigoBoleta;
		DIST dist_fisc;
		List<RECI> recibos;
		List<FACT> facturas;
		List<BOLE> boletas;
		PROD prod;
		Double servicio_mensual = 0d;
		Double instalacion = 0d;
		Double alquiler_equipos = 0d;
		Double servicios_suplementarios = 0d;
		Double si_afectos = 0d;
		Double no_afectos = 0d;
		Double sub_total = 0d;
		Double igv = 0d;
		Double total = 0d;

		for (CIER cier : listaCier) {
			recibos = cier.getRecibos();
			prod = cier.getProducto();

			List<CIER_DATA_PLAN> data_planes_cobrados_en_cierre = cier.getDataPlanesCobrados_forMaestroGerentes();
			Map<Long, CIER_DATA_PLAN> data_detalle_del_servicio = new TreeMap<Long, CIER_DATA_PLAN>();

			List<NEGO_SUCU_PLAN> nego_sucu_planes = cier.getNEGO_SUCU_PLAN_forMaestroGerentes();
			Map<Integer, NEGO_SUCU_PLAN> map_nego_sucu_planes = new TreeMap<Integer, NEGO_SUCU_PLAN>();

			for (CIER_DATA_PLAN cier_data_plan : data_planes_cobrados_en_cierre) {
				if (cier_data_plan.getCO_RECI() != null) {
					if (!data_detalle_del_servicio.containsKey(cier_data_plan.getCO_RECI())) {
						data_detalle_del_servicio.put(cier_data_plan.getCO_RECI(), cier_data_plan);
						// cantidad_planes_por_recibos.put(cier_data_plan.getCO_RECI(),
						// 0);
					}
				} else if (cier_data_plan.getCO_BOLE() != null) {
					if (!data_detalle_del_servicio.containsKey(cier_data_plan.getCO_BOLE())) {
						data_detalle_del_servicio.put(cier_data_plan.getCO_BOLE(), cier_data_plan);
						// cantidad_planes_por_recibos.put(cier_data_plan.getCO_RECI(),
						// 0);
					}
				}
			}

			for (NEGO_SUCU_PLAN nego_sucu_plan : nego_sucu_planes) {
				if (!map_nego_sucu_planes.containsKey(nego_sucu_plan.getCO_NEGO_SUCU_PLAN_ForJSON())) {
					map_nego_sucu_planes.put(nego_sucu_plan.getCO_NEGO_SUCU_PLAN_ForJSON(), nego_sucu_plan);
				}
			}

			CIER_DATA_PLAN cier_data_plan = null;
			NEGO_SUCU_PLAN nego_sucu_plan = null;
			Boolean ST_BACKUP_BU;
			String tipo_servicio;
			for (RECI recibo : recibos) {
				dist_fisc = recibo.getDist_Fisc_ForJSON();
				// servicio_mensual=recibo.getIM_SERV_MENS_ForJSON();
				servicio_mensual = com.americatel.facturacion.fncs.Numero
						.redondear(recibo.getIM_SERV_MENS_ForJSON() + recibo.getIM_DIF_CARGO_FIJO_ForJSON(), 2);
				instalacion = recibo.getIM_INST_ForJSON();
				alquiler_equipos = recibo.getIM_ALQU_ForJSON();
				servicios_suplementarios = recibo.getIM_OTRO_ForJSON();
				si_afectos = Math.abs(recibo.getIM_AJUS_SIGV_ForJSON());
				no_afectos = Math.abs(recibo.getIM_AJUS_NIGV_ForJSON());
				igv = recibo.getIM_IGV_ForJSON();
				// sub_total=recibo.getIM_NETO_ForJSON();
				sub_total = com.americatel.facturacion.fncs.Numero
						.redondear(recibo.getIM_NETO_ForJSON() + recibo.getIM_AJUS_SIGV_ForJSON(), 2);
				total = recibo.getIM_TOTA_ForJSON();
				ST_BACKUP_BU = false;
				tipo_servicio = "";
				cier_data_plan = null;
				nego_sucu_plan = null;

				if (data_detalle_del_servicio.containsKey(recibo.getCO_RECI_ForJSON())) {
					cier_data_plan = data_detalle_del_servicio.get(recibo.getCO_RECI_ForJSON());
					if (map_nego_sucu_planes.containsKey(cier_data_plan.getCO_NEGO_SUCU_PLAN())) {
						nego_sucu_plan = map_nego_sucu_planes.get(cier_data_plan.getCO_NEGO_SUCU_PLAN());
						ST_BACKUP_BU = nego_sucu_plan.getPLAN().getST_BACKUP_BU_ForJSON();
					}
					// cantidad_sucursales=cantidad_planes_por_recibos.get(recibo.getCO_RECI_ForJSON());
				}

				if (!ST_BACKUP_BU) {
					if (prod.getCO_PROD_ForJSON() == 1) {
						tipo_servicio = "IPB SAT";
					} else {
						tipo_servicio = "ADD SAT";
					}
				} else {
					if (prod.getCO_PROD_ForJSON() == 1) {
						tipo_servicio = "IPB SAT - BU";
					} else {
						tipo_servicio = "ADD SAT - BU";
					}
				}

				if (prod.getCO_PROD_ForJSON() == 1) {// internet
					this.excel.writeCols(new Object[] { recibo.getCO_RECI_ForJSON(), recibo.getFE_EMIS_ForJSON(),
							recibo.getDE_CODI_BUM_ForJSON(), recibo.getCO_NEGO_ForJSON(), recibo.getNO_CLIE_ForJSON(),
							recibo.getDE_DIRE_FISC_ForJSON() + " - " + dist_fisc.getPROV_ForJSON().getNO_PROV_ForJSON()
							+ " - " + dist_fisc.getPROV_ForJSON().getDEPA_ForJSON().getNO_DEPA_ForJSON(),
							recibo.getDE_NUME_DOCU_ForJSON(), tipo_servicio, recibo.getNO_MONE_FACT_ForJSON(), null,
							null, null, null, sub_total, igv, total, recibo.getFE_VENC_ForJSON(), null, null, null,
							si_afectos, null, instalacion, no_afectos, servicio_mensual, alquiler_equipos,
							servicios_suplementarios, null, null, null, null, null, recibo.getNO_TIPO_DOCU_ForJSON(),
							recibo.getDE_NUME_DOCU_ForJSON(), null, dist_fisc.getNO_DIST_ForJSON(), null, null, null,
							null, null, "RC" });
				} else {// datos
					this.excel.writeCols(new Object[] { recibo.getCO_RECI_ForJSON(), recibo.getFE_EMIS_ForJSON(),
							recibo.getDE_CODI_BUM_ForJSON(), recibo.getCO_NEGO_ForJSON(), recibo.getNO_CLIE_ForJSON(),
							recibo.getDE_DIRE_FISC_ForJSON() + " - " + dist_fisc.getPROV_ForJSON().getNO_PROV_ForJSON()
							+ " - " + dist_fisc.getPROV_ForJSON().getDEPA_ForJSON().getNO_DEPA_ForJSON(),
							recibo.getDE_NUME_DOCU_ForJSON(), tipo_servicio, recibo.getNO_MONE_FACT_ForJSON(),
							instalacion, servicio_mensual, alquiler_equipos, servicios_suplementarios, sub_total, igv,
							total, recibo.getFE_VENC_ForJSON(), null, null, null, si_afectos, null, null, no_afectos,
							null, null, null, null, null, null, null, null, recibo.getNO_TIPO_DOCU_ForJSON(),
							recibo.getDE_NUME_DOCU_ForJSON(), null, dist_fisc.getNO_DIST_ForJSON(), null, null, null,
							null, null, "RC" });
				}
			}

			boletas = cier.getBoletas();
			prod = cier.getProducto();

			for (BOLE boleta : boletas) {
				dist_fisc = boleta.getDist_Fisc_ForJSON();
				// servicio_mensual=recibo.getIM_SERV_MENS_ForJSON();
				servicio_mensual = com.americatel.facturacion.fncs.Numero
						.redondear(boleta.getIM_SERV_MENS_ForJSON() + boleta.getIM_DIF_CARGO_FIJO_ForJSON(), 2);
				instalacion = boleta.getIM_INST_ForJSON();
				alquiler_equipos = boleta.getIM_ALQU_ForJSON();
				servicios_suplementarios = boleta.getIM_OTRO_ForJSON();
				si_afectos = Math.abs(boleta.getIM_AJUS_SIGV_ForJSON());
				no_afectos = Math.abs(boleta.getIM_AJUS_NIGV_ForJSON());
				igv = boleta.getIM_IGV_ForJSON();
				// sub_total=recibo.getIM_NETO_ForJSON();
				sub_total = com.americatel.facturacion.fncs.Numero
						.redondear(boleta.getIM_NETO_ForJSON() + boleta.getIM_AJUS_SIGV_ForJSON(), 2);
				total = boleta.getIM_TOTA_ForJSON();
				ST_BACKUP_BU = false;
				tipo_servicio = "";
				cier_data_plan = null;
				nego_sucu_plan = null;

				if (data_detalle_del_servicio.containsKey(boleta.getCO_BOLE_ForJSON())) {
					cier_data_plan = data_detalle_del_servicio.get(boleta.getCO_BOLE_ForJSON());
					if (map_nego_sucu_planes.containsKey(cier_data_plan.getCO_NEGO_SUCU_PLAN())) {
						nego_sucu_plan = map_nego_sucu_planes.get(cier_data_plan.getCO_NEGO_SUCU_PLAN());
						ST_BACKUP_BU = nego_sucu_plan.getPLAN().getST_BACKUP_BU_ForJSON();
					}
					// cantidad_sucursales=cantidad_planes_por_recibos.get(recibo.getCO_RECI_ForJSON());
				}

				if (!ST_BACKUP_BU) {
					if (prod.getCO_PROD_ForJSON() == 1) {
						tipo_servicio = "IPB SAT";
					} else {
						tipo_servicio = "ADD SAT";
					}
				} else {
					if (prod.getCO_PROD_ForJSON() == 1) {
						tipo_servicio = "IPB SAT - BU";
					} else {
						tipo_servicio = "ADD SAT - BU";
					}
				}

				codigoBoleta = "";

				if (boleta.getCO_BOLE_ForJSON() != null) {
					codigoBoleta = getFormatoCodigo(boleta.getCO_BOLE_ForJSON(), "BOLE");
				}

				if (prod.getCO_PROD_ForJSON() == 1) {// internet
					this.excel.writeCols(new Object[] { codigoBoleta, boleta.getFE_EMIS_ForJSON(),
							boleta.getDE_CODI_BUM_ForJSON(), boleta.getCO_NEGO_ForJSON(), boleta.getNO_CLIE_ForJSON(),
							boleta.getDE_DIRE_FISC_ForJSON() + " - " + dist_fisc.getPROV_ForJSON().getNO_PROV_ForJSON()
							+ " - " + dist_fisc.getPROV_ForJSON().getDEPA_ForJSON().getNO_DEPA_ForJSON(),
							boleta.getDE_NUME_DOCU_ForJSON(), tipo_servicio, boleta.getNO_MONE_FACT_ForJSON(), null,
							null, null, null, sub_total, igv, total, boleta.getFE_VENC_ForJSON(), null, null, null,
							si_afectos, null, instalacion, no_afectos, servicio_mensual, alquiler_equipos,
							servicios_suplementarios, null, null, null, null, null, boleta.getNO_TIPO_DOCU_ForJSON(),
							boleta.getDE_NUME_DOCU_ForJSON(), null, dist_fisc.getNO_DIST_ForJSON(), null, null, null,
							null, null, "BV" });
				} else {// datos
					this.excel.writeCols(new Object[] { codigoBoleta, boleta.getFE_EMIS_ForJSON(),
							boleta.getDE_CODI_BUM_ForJSON(), boleta.getCO_NEGO_ForJSON(), boleta.getNO_CLIE_ForJSON(),
							boleta.getDE_DIRE_FISC_ForJSON() + " - " + dist_fisc.getPROV_ForJSON().getNO_PROV_ForJSON()
							+ " - " + dist_fisc.getPROV_ForJSON().getDEPA_ForJSON().getNO_DEPA_ForJSON(),
							boleta.getDE_NUME_DOCU_ForJSON(), tipo_servicio, boleta.getNO_MONE_FACT_ForJSON(),
							instalacion, servicio_mensual, alquiler_equipos, servicios_suplementarios, sub_total, igv,
							total, boleta.getFE_VENC_ForJSON(), null, null, null, si_afectos, null, null, no_afectos,
							null, null, null, null, null, null, null, null, boleta.getNO_TIPO_DOCU_ForJSON(),
							boleta.getDE_NUME_DOCU_ForJSON(), null, dist_fisc.getNO_DIST_ForJSON(), null, null, null,
							null, null, "BV" });
				}
			}

			facturas = cier.getFacturas();
			prod = cier.getProducto();
			for (FACT fact : facturas) {
				dist_fisc = fact.getDist_Fisc_ForJSON();

				servicio_mensual = fact.getIM_SERV_MENS_ForJSON();
				instalacion = fact.getIM_INST_ForJSON();
				alquiler_equipos = fact.getIM_ALQU_ForJSON();
				servicios_suplementarios = fact.getIM_OTRO_ForJSON();
				si_afectos = 0d;
				no_afectos = Math.abs(fact.getIM_AJUS_NIGV_ForJSON());
				igv = fact.getIM_IGV_ForJSON();
				sub_total = fact.getIM_VALO_VENT_NETO_ForJSON();
				total = fact.getIM_TOTA_ForJSON();

				ST_BACKUP_BU = false;
				tipo_servicio = "";
				cier_data_plan = null;
				nego_sucu_plan = null;

				for (RECI recibo : recibos) {
					if (recibo.getCO_NEGO_ForJSON().equals(fact.getCO_NEGO_ForJSON())) {
						if (data_detalle_del_servicio.containsKey(recibo.getCO_RECI_ForJSON())) {
							cier_data_plan = data_detalle_del_servicio.get(recibo.getCO_RECI_ForJSON());
							if (map_nego_sucu_planes.containsKey(cier_data_plan.getCO_NEGO_SUCU_PLAN())) {
								nego_sucu_plan = map_nego_sucu_planes.get(cier_data_plan.getCO_NEGO_SUCU_PLAN());
								ST_BACKUP_BU = nego_sucu_plan.getPLAN().getST_BACKUP_BU_ForJSON();
							}
						}
					}
				}

				if (!ST_BACKUP_BU) {
					if (prod.getCO_PROD_ForJSON() == 1) {
						tipo_servicio = "IPB SAT";
					} else {
						tipo_servicio = "ADD SAT";
					}
				} else {
					if (prod.getCO_PROD_ForJSON() == 1) {
						tipo_servicio = "IPB SAT - BU";
					} else {
						tipo_servicio = "ADD SAT - BU";
					}
				}

				codigoFactura = "";

				if (fact.getCO_FACT_ForJSON() != null) {
					codigoFactura = getFormatoCodigo(fact.getCO_FACT_ForJSON(), "FACT");
				}

				if (prod.getCO_PROD_ForJSON() == 1) {// internet
					this.excel.writeCols(new Object[] { codigoFactura, fact.getFE_EMIS_ForJSON(),
							fact.getDE_CODI_BUM_ForJSON(), fact.getCO_NEGO_ForJSON(), fact.getNO_CLIE_ForJSON(),
							fact.getDE_DIRE_FISC_ForJSON() + " - " + dist_fisc.getPROV_ForJSON().getNO_PROV_ForJSON()
							+ " - " + dist_fisc.getPROV_ForJSON().getDEPA_ForJSON().getNO_DEPA_ForJSON(),
							fact.getDE_NUME_DOCU_ForJSON(), tipo_servicio, fact.getNO_MONE_FACT_ForJSON(), null, null,
							null, null, sub_total, igv, total, fact.getFE_VENC_ForJSON(), null, null, null, si_afectos,
							null, instalacion, no_afectos, servicio_mensual, alquiler_equipos, servicios_suplementarios,
							null, null, null, null, null, fact.getNO_TIPO_DOCU_ForJSON(),
							fact.getDE_NUME_DOCU_ForJSON(), null, dist_fisc.getNO_DIST_ForJSON(), null, null, null,
							null, null, "FC" });
				} else {// datos
					this.excel.writeCols(new Object[] { codigoFactura, fact.getFE_EMIS_ForJSON(),
							fact.getDE_CODI_BUM_ForJSON(), fact.getCO_NEGO_ForJSON(), fact.getNO_CLIE_ForJSON(),
							fact.getDE_DIRE_FISC_ForJSON() + " - " + dist_fisc.getPROV_ForJSON().getNO_PROV_ForJSON()
							+ " - " + dist_fisc.getPROV_ForJSON().getDEPA_ForJSON().getNO_DEPA_ForJSON(),
							fact.getDE_NUME_DOCU_ForJSON(), tipo_servicio, fact.getNO_MONE_FACT_ForJSON(), instalacion,
							servicio_mensual, alquiler_equipos, servicios_suplementarios, sub_total, igv, total,
							fact.getFE_VENC_ForJSON(), null, null, null, si_afectos, null, null, no_afectos, null, null,
							null, null, null, null, null, null, fact.getNO_TIPO_DOCU_ForJSON(),
							fact.getDE_NUME_DOCU_ForJSON(), null, dist_fisc.getNO_DIST_ForJSON(), null, null, null,
							null, null, "FC" });
				}
			}
		}
	}

	public String getNombreServicio(Integer COD_PRODUCTO, Integer COD_SERVICIO) {

		String nombreServicio = "";

		switch (COD_PRODUCTO) {

		case 1:
			if (COD_SERVICIO == 1) {
				nombreServicio = "Cargos Fijos Satelital - INTERNET";
			} else if (COD_SERVICIO == 2) {
				nombreServicio = "Cargos Fijos Satelital - DATOS";
			}
			break;
		case 2:
			if (COD_SERVICIO == 3) {
				nombreServicio = "Internet Dedicado";
			} else if (COD_SERVICIO == 4) {
				nombreServicio = "Datos Dedicados";
			} else if (COD_SERVICIO == 4) {
				nombreServicio = "Hosting";
			}
			break;
		case 3:
			if (COD_SERVICIO == 5) {
				nombreServicio = "Internet Migracion Ingenio";
			} else if (COD_SERVICIO == 6) {
				nombreServicio = "Datos Migracion Ingenio";
			}
			break;
		case 4:
			if (COD_SERVICIO == 7) {
				nombreServicio = "TI-Housing";
			} else if (COD_SERVICIO == 8) {
				nombreServicio = "TI-IAAS";
			} else if (COD_SERVICIO == 9) {
				nombreServicio = "TI-SAAS-CB";
			} else if (COD_SERVICIO == 10) {
				nombreServicio = "TI-SAAS-SAS";
			} else if (COD_SERVICIO == 11) {
				nombreServicio = "TI - HOU - ONE SHOT";
			}
			break;
		case 5:
			if (COD_SERVICIO == 12) {
				nombreServicio = "Internet Dedicados Mayorista";
			} else if (COD_SERVICIO == 13) {
				nombreServicio = "Datos Dedicado Mayorista";
			} else if (COD_SERVICIO == 14) {
				nombreServicio = "Ducteria";
			} else if (COD_SERVICIO == 15) {
				nombreServicio = "Colocacion";
			} else if (COD_SERVICIO == 16) {
				nombreServicio = "Otros Servicios";
			}
			break;
		default:
			nombreServicio = "null";
			break;

		}
		return nombreServicio;
	}

	// SE EVALUA EL FORMATO DEL REPORTE SEGUN EL PRODUCTO Y SERVICIO
	public String getTipoReporte(Integer COD_PRODUCTO, Integer COD_SERVICIO) {

		String formato = "";

		switch (COD_PRODUCTO) {

		case 1:
			formato = "FormatoSatelital";
			break;
		case 2:
			if (COD_SERVICIO == 3) {
				formato = "FormatoH";
			} else if (COD_SERVICIO == 4) {
				formato = "FormatoI";
			}
			else if (COD_SERVICIO == 17) {
					formato = "FormatoG";
			}
			break;
		case 3:
			formato = "null";
			break;
		case 4:
		//	if (COD_SERVICIO == 7 || COD_SERVICIO == 11) {
//				formato = "FormatoC";
//			} else if (COD_SERVICIO == 8 || COD_SERVICIO == 9 || COD_SERVICIO == 10) {
//				formato = "FormatoF";
//			}
		
				formato = "formatoReporteTI";
		
			
			break;
		case 5:
			if (COD_SERVICIO == 14 || COD_SERVICIO == 15 || COD_SERVICIO == 16) {
				formato = "FormatoG";
			} else if (COD_SERVICIO == 12 || COD_SERVICIO == 13) {
				formato = "FormatoB";
			}
			break;
		default:
			formato = "null";
			break;
		}

		return formato;
	}

	public String getCodServicio(Integer COD_PRODUCTO, Integer COD_SERVICIO) {

		String cod_servicio = "";
		return cod_servicio;
	}

	public String getFormatoCodigo(Long codigoParam, String tipoDocumento) {

		String codigo = "";

		if (tipoDocumento.equals("BOLE")) {
			codigo = "B050" + String.format("%08d", codigoParam);
		} else if (tipoDocumento.equals("FACT")) {
			codigo = "F050" + String.format("%08d", codigoParam);
		}

		return codigo;
	}

	
}
