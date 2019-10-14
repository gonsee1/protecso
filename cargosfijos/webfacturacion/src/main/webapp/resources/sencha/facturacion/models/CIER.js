(function(){
    var AJAX_URI=settings.AJAX_URI+'CIER/';
    Ext.define('Desktop.Facturacion.Models.CIER', {
        extend: 'Ext.data.Model',
        AJAX_URI:AJAX_URI,
        fields: [
            {name: 'CO_CIER', type: 'int'},                       
            {
                name: 'CO_PROD',
                type: 'int',
                reference: {
                    parent: 'PROD'
                }                
            },
            {name: 'NU_PERI', type: 'int'},
            {name: 'NU_ANO', type: 'int'},
            
            {name: 'DE_RAIZ_RECI', type: 'int'},
            {name: 'DE_RAIZ_FACT', type: 'int'},
            {name: 'FE_EMIS', type: 'date', dateFormat: 'Y-m-d'},
            {name: 'FE_VENC', type: 'date', dateFormat: 'Y-m-d'},
            {name: 'ST_CIER', type: 'int'},
            {name: 'FH_INIC', type: 'date', dateFormat: 'Y-m-d'},
            {name: 'FH_FIN', type: 'date', dateFormat: 'Y-m-d'},
        ],        
        proxy: {
            type: 'ajax',
            url : '/ajax/',
            noCache: false,
            actionMethods: {create : 'POST',read   : 'POST',update : 'POST',destroy: 'POST'},
            api: {
                read:AJAX_URI+'select/',
                update:AJAX_URI+'update/',
                create:AJAX_URI+'insert/',
                delete:AJAX_URI+'delete/',
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