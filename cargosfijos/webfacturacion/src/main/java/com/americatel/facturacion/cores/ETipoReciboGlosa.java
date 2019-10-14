/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.americatel.facturacion.cores;

/**
 *
 * @author crodas
 */
public enum ETipoReciboGlosa {
    SERVICIO_SUPLEMENTARIO{//(1)
        public String toString() {
            return "Servicio Suplementario";
        }
    },
    PLAN{//(2)
        public String toString() {
            return "Plan";
        }        
    },
    SERVICIO_UNICO{//(3)
        public String toString() {
            return "Servicio Unico";
        }        
    },
    DEVOLUCION_POR_CARGOS_FIJOS{//(4)
        public String toString() {
            return "Devoluci\u00f3n por cargos fijos.";
        }        
    },
    DEVOLUCION_POR_DESACTIVACION{//(10)
        public String toString() {
            return "Devoluci\u00f3n por desactivaci\u00f3n.";
        }        
    },
    DEVOLUCION_CARGOS_FIJOS_SALDO{//(11) cuando se genera una devolucion y pasa de un recibo a otro (no se tiene la glosa)
        public String toString() {
            return "Devoluci\u00f3n por saldo.";
        }        
    },
    DEVOLUCION_POR_CORTE_O_SUSPENSION_SERVICIO{//(12) cuando se genera una devolucion y pasa de un recibo a otro (no se tiene la glosa)
        public String toString() {
            return "Devoluci\u00f3n por corte o suspensi\u00f3n.";
        }        
    },
    
    PROMOCIONES_PORCENTAJE{//(20) promociones
        public String toString() {
            return "Promociones por procentaje.";
        }        
    },
    PROMOCIONES_MONTO{//(21) promociones
        public String toString() {
            return "Promociones por monto.";
        }        
    },   
    
    AJUSTES_AFECTOS_IGV{//(30) promociones
        public String toString() {
            return "Ajustes afectos a igv.";
        }        
    },
    AJUSTES_NO_AFECTOS_IGV{//(31) promociones
        public String toString() {
            return "Ajustes no afectos a igv.";
        }        
    },  
    
    COBRO_ADICIONAL_UPGRADE{//(50)
        public String toString() {
            return "Cobro adicional por upgrade.";
        }        
    },       
}
