(function(){
    var AJAX_URI=settings.AJAX_URI+'USUA/';
    Ext.define('Desktop.Facturacion.Models.USUA', {
        extend: 'Ext.data.Model',
        AJAX_URI:AJAX_URI,
        fields: [
            {name: 'CO_USUA', type: 'int'},                        
            {
                name: 'CO_PERF',
                type: 'int',
                reference: {
                    parent: 'PERF'
                }                
            },
            {name: 'NO_USUA', type: 'string'},
            {name: 'AP_USUA', type: 'string'},
            {name: 'DE_CORR', type: 'string'},
            {name: 'DE_USER', type: 'string'}
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