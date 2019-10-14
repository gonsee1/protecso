(function(){
    var AJAX_URI=settings.AJAX_URI+'CORT/';
    Ext.define('Desktop.Facturacion.Models.CORT', {
        extend: 'Ext.data.Model',
        AJAX_URI:AJAX_URI,
        fields: [
            {name: 'CO_CORT', type: 'int'},
            {name: 'CO_NEGO', type: 'int'},
            {name: 'CO_PROD', type: 'int'},
            {name: 'FE_INIC', type: 'date',dateFormat: 'Y-m-d'},
            {name: 'FE_FIN', type: 'date',dateFormat: 'Y-m-d'},
            {name: 'CO_CIER_INIC', type: 'int'},
            {name: 'CO_CIER_FIN', type: 'int'},
            {name: 'DE_ESTADO', type: 'string'},
        ],        
        proxy: {
            type: 'ajax',
            url : '/ajax/',
            noCache: false,
            actionMethods: {create : 'POST',read   : 'POST',update : 'POST',destroy: 'POST'},
            api: {
                read:AJAX_URI+'selectCortesPendientesByProducto/',
            },
            reader:{
                type: 'json',
                rootProperty: 'data',
                successProperty:'success',
            },
            write:{
                type:'ajax',
            }
        },    
    });    
})();