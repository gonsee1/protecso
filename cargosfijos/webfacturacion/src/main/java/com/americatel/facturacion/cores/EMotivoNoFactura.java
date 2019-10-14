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
public enum EMotivoNoFactura {
    RECIEN_ACTIVADA{//(1)
        public String toString() {
            return "Recien activada";
        }
    },
    DESACTIVADA{//(2)
        public String toString() {
            return "Desactivada";
        }        
    },
    SUSPENDIDO{//(3)
        public String toString() {
            return "Suspendido";
        }        
    },
    CORTADO{//(4)
        public String toString() {
            return "Cortado";
        }        
    }
}
