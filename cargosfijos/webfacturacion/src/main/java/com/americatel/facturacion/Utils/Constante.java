/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.Utils;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author efitec01
 */
public class Constante {
    /*Mensajes*/
    public static final String errorCierreEjecucion  = "No se puede ejecutar el cierre, existe un proceso en ejecucion";
    public static final String errorNroReciboEnUso   = "El numero de raiz de recibo ya esta en uso";
    public static final String errorNroFacturaEnUso  = "El numero de raiz de factura ya esta en uso";
    public static final String errorNegociosPendiente  = "Existen negocios pendientes. Favor de asociarles una sucursal para poder lanzar el cierre.";
    
    public static final String MENSAJE_DISPONIBILIDAD_CORRELATIVO = "El numero de semillas ingresada ya se encuentra en uso : </br>";
    
    /*Estados de cierre*/
    public static final int ESTADO_CIERRE_PENDIENTE = 1;
    public static final int ESTADO_CIERRE_EN_PROCESO = 2;
    public static final int ESTADO_CIERRE_TERMINADO = 3;
    public static final int ESTADO_CIERRE_CERRADO = 4;
    
    
    /*Estados generales*/
    public static final int ESTADO_INACTIVO = 0;
    public static final int ESTADO_ACTIVO = 1;
    
    /*Parametros*/
//    public static final int TABLA_PARAMETROS = 1;
//    public static final int PARAMETRO_PROCESANDO = 2;
    
    /*Productos*/    
    public static final Long COD_PROD_PADRE_SATELITAL = 1L;
    public static final Long COD_PROD_PADRE_EMPRESA = 2L;
    public static final Long COD_PROD_PADRE_INGENIO = 3L;
    public static final Long COD_PROD_PADRE_TI = 4L;
    public static final Long COD_PROD_PADRE_MAYORISTA = 5L;
    
    public static final int INT_COD_PROD_PADRE_TI = 4;
           
    /*Servicios*/   
    public static final Long COD_PROD_SATELITAL_CARG_FIJO_SATE_INTERNET = 1L;
    public static final Long COD_PROD_SATELITAL_CARG_FIJO_SATE_DATOS = 2L;    
    public static final Long COD_PROD_EMPRESA_INTERNET_DEDICADO = 3L;
    public static final Long COD_PROD_EMPRESA_DATOS_DEDICADO = 4L;
    public static final Long COD_PROD_INGENIO_INTERNET_MIGRACION = 5L;
    public static final Long COD_PROD_INGENIO_DATOS_MIGRACION = 6L;    
    public static final Long COD_PROD_TI_HOUSING = 7L;
    public static final Long COD_PROD_TI_IAAS = 8L;
    public static final Long COD_PROD_TI_SAAS_CB = 9L;
    public static final Long COD_PROD_TI_SAAS_SAS = 10L;
    public static final Long COD_PROD_TI_TI_HOU_ONE_SHOT = 11L;
    public static final Long COD_PROD_MAYORISTA_INTERNET_DEDICADO_MAYORISTA = 12L;
    public static final Long COD_PROD_MAYORISTA_DATOS_DEDICADO_MAYORISTA = 13L;
    public static final Long COD_PROD_MAYORISTA_DUCTERIA = 14L;
    public static final Long COD_PROD_MAYORISTA_COLOCACION = 15L;
    public static final Long COD_PROD_MAYORISTA_OTROS_SERVICIOS= 16L;

    
    public static final Map<Long, String> serviciosSatelital;
    static {
        Map<Long, String> aMap = new LinkedHashMap<Long, String>();
        aMap.put(COD_PROD_SATELITAL_CARG_FIJO_SATE_INTERNET, "Cargos Fijos Satelital - INTERNET");
        aMap.put(COD_PROD_SATELITAL_CARG_FIJO_SATE_DATOS, "Cargos Fijos Satelital - DATOS");
        
        serviciosSatelital = Collections.unmodifiableMap(aMap);
    }
    
    public static final Map<Long, String> serviciosEmpresa;
    static {
        Map<Long, String> aMap = new LinkedHashMap<Long, String>();
        aMap.put(COD_PROD_EMPRESA_INTERNET_DEDICADO, "Internet Dedicado");
        aMap.put(COD_PROD_EMPRESA_DATOS_DEDICADO, "Datos Dedicados");
        
        serviciosEmpresa = Collections.unmodifiableMap(aMap);
    }
    
    public static final Map<Long, String> serviciosIngenio;
    static {
        Map<Long, String> aMap = new LinkedHashMap<Long, String>();
        aMap.put(COD_PROD_INGENIO_INTERNET_MIGRACION, "Internet Migraci\u00f3n Ingenio");
        aMap.put(COD_PROD_INGENIO_DATOS_MIGRACION, "Datos Migraci\u00f3n Ingenio");
        
        serviciosIngenio = Collections.unmodifiableMap(aMap);
    }
    
    public static final Map<Long, String> serviciosTI;
    static {
        Map<Long, String> aMap = new LinkedHashMap<Long, String>();
        aMap.put(COD_PROD_TI_HOUSING, "TI-Housing");
        aMap.put(COD_PROD_TI_IAAS, "TI-IAAS");
        aMap.put(COD_PROD_TI_SAAS_CB, "TI-SAAS-CB");
        aMap.put(COD_PROD_TI_SAAS_SAS, "TI-SAAS-SAS");
        aMap.put(COD_PROD_TI_TI_HOU_ONE_SHOT, "TI - HOU - ONE SHOT");
        
        serviciosTI = Collections.unmodifiableMap(aMap);
    }
    
    public static final Map<Long, String> serviciosMayorista;
    static {
        Map<Long, String> aMap = new LinkedHashMap<Long, String>();
        aMap.put(COD_PROD_MAYORISTA_INTERNET_DEDICADO_MAYORISTA, "Internet Dedicados Mayorista");
        aMap.put(COD_PROD_MAYORISTA_DATOS_DEDICADO_MAYORISTA, "Datos Dedicado Mayorista");
        aMap.put(COD_PROD_MAYORISTA_DUCTERIA, "Ducteria");
        aMap.put(COD_PROD_MAYORISTA_COLOCACION, "Colocacion");
        aMap.put(COD_PROD_MAYORISTA_OTROS_SERVICIOS, "Otros Servicios");
        
        serviciosMayorista = Collections.unmodifiableMap(aMap);
    }
    
    public static final Map<Long, Map<Long, String>> productos;
    static {
        Map<Long, Map<Long, String>> aMap = new LinkedHashMap<Long, Map<Long, String>>();
        aMap.put(COD_PROD_PADRE_SATELITAL, serviciosSatelital);
        aMap.put(COD_PROD_PADRE_EMPRESA, serviciosEmpresa);
        aMap.put(COD_PROD_PADRE_INGENIO, serviciosIngenio);
        aMap.put(COD_PROD_PADRE_TI, serviciosTI);
        aMap.put(COD_PROD_PADRE_MAYORISTA, serviciosMayorista);
        
        productos = Collections.unmodifiableMap(aMap);
    }
    
    
    
    /*Tipos de Documentos*/   
    public static final int COD_TIPO_DOC_RUC = 1;
    public static final int COD_TIPO_DOC_DNI = 2;
    public static final int COD_TIPO_DOC_CARNET_EXTRANJERIA = 3;
    public static final int COD_TIPO_DOC_PASAPORTE = 4;
    public static final int COD_TIPO_DOC_NO_DOMICILIADO = 5;
    public static final int COD_TIPO_DOC_OTRO_DOC = 6;
    
    
    /*Reporte*/
    public static final String S_FACTURA               = "Factura";
    public static final String S_RECIBO              = "Recibo";
    public static final String S_BOLETA               = "Boleta";

    public static final String S_ITEM               = "Item";
    public static final String S_DESCRIPCION        = "Descripcion";
    public static final String S_CANTIDAD           = "Cantidad";
    public static final String S_VUNITARIO          = "V. Unitario";
    public static final String S_DESCUENTO          = "Descuento";
    public static final String S_TOTAL              = "Total";
    
    public static final String LABEL_OP_GRAVADAS    = "OP GRAVADAS";
    public static final String LABEL_OP_INAFECTAS   = "OP INAFECTAS";
    public static final String LABEL_OP_EXONERADAS  = "OP EXONERADAS";
    public static final String LABEL_OP_GRATUITAS   = "OP GRATUITAS";
    public static final String LABEL_DESCUENTO_GLO  = "DESCUENTOS GLOBALES";
    public static final String LABEL_IGV            = "IGV";
    public static final String LABEL_OTROS_TRIBUTOS = "OTROS TRIBUTOS";
    public static final String LABEL_OTROS_CARGOS   = "OTROS CARGOS";
    public static final String LABEL_TOTAL          = "TOTAL";
    public static final String LABEL_REDONDEO       = "REDONDEO";
    
    public static final String LABEL_VALOR_VENTA    = "VALOR VENTA";
    public static final String LABEL_VALOR_VENTA_NETO    = "VALOR VENTA NETO";
    public static final String LABEL_DESCUENTO    = "DESCUENTO";
    public static final String LABEL_AJUSTE_REDONDEO    = "AJUSTE REDONDEO";
    
    /*Tipo de Moneda*/
    
    public static final Map<Integer,String> MONEDAS = new LinkedHashMap<Integer, String>();
    static{
        MONEDAS.put(1,"SOLES");
        MONEDAS.put(2,"USD");
    }
    
    public static final Integer SOLES = 1;
    public static final Integer USD = 2;
    
    /*Tipo Glosa*/
    public static final Integer GLOSA_TIPO_PLAN = 1;
    public static final Integer GLOSA_TIPO_SERVICIO_SUPLEMENTARIO = 2;
    public static final Integer GLOSA_TIPO_SERVICIO_UNICO = 3;
    public static final Integer GLOSA_TIPO_AJUSTE_AFECTO_IGV = 4;
    public static final Integer GLOSA_TIPO_AJUSTE_NO_AFECTO_IGV = 5;
    public static final Integer GLOSA_TIPO_DEVOLUCION_X_PROMOCION = 6;
    public static final Integer GLOSA_TIPO_DEVOLUCION_POR_CARGO_FIJO = 7;
    public static final Integer GLOSA_TIPO_DIFERENCIAL_CARGO_FIJO = 8;
    
    public static String GLOSA_DIFERENCIAL_CARGOS_FIJOS="Diferencial de cargos fijos";
    public static String GLOSA_DEVOLUCION_CARGOS_FIJOS="Devoluci\u00f3n por cargos fijos";
    public static String GLOSA_DEVOLUCION_PROMOCION="Devoluci\u00f3n por promoci\u00f3n";
    public static String GLOSA_AJUSTE_REDONDEO="Ajuste por redondeo";
    
    /*Parametros generales*/    
    public static final Double IGV = 0.18;
    public static final String S_TRUE = "true";
    public static final String S_FALSE = "false";
    
    /*Parametros Response*/
    public static final String RESULTADO = "resultado";
    public static final String MENSAJE = "mensaje";
    
    /*Codigos tipo documento (front)*/
    public static final Integer FACTURA = 1;
    public static final Integer BOLETA = 2;
    
    
    public static final Map<Integer, String> abreviaturaTipoDocumento;
    static {
        Map<Integer, String> aMap = new LinkedHashMap<Integer, String>();
        aMap.put(1, "RU");
        aMap.put(2, "DN");
        aMap.put(3, "CE");
        aMap.put(4, "PA");
        aMap.put(5, "ND");
        aMap.put(6, "OD");
        abreviaturaTipoDocumento = Collections.unmodifiableMap(aMap);
    }
    
    /*Tipo de Servicios*/
    public static final String  HOU				= "HOU";
    //public static final String  HOS_COM			= "TI-HOS-COM";
    public static final String  IAAS_Compartido = "IAAS - Compartido";
    public static final String  IAAS_Dedicado	= "IAAS - Dedicado";
    public static final String  SAAS_CB		  	= "SAAS - CB";
    public static final String  OF365_CB		= "OF365 - CB";
    public static final String  IAAS_CB 	  	= "IAAS - CB";
    public static final String  STAAS_CB 	  	= "STAAS - CB";
    public static final String  MAS_CB 	  		= "MAS - CB";
    public static final String  CRM_CB 	  		= "CRM - CB";
    public static final String  SAAS_SAS	  	= "SAAS - SAS";
    public static final String  HOS_ONE_SHOT  	= "HOS - ONE SHOT";
    public static final String  CB_ONE_SHOT   	= "CB - ONE SHOT";
    public static final String  HOU_ONE_SHOT  	= "HOU - ONE SHOT";
    
    
}
