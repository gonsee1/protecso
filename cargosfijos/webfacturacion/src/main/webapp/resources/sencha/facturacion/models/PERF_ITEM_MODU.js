(function(){
    var AJAX_URI=settings.AJAX_URI+'PERF_ITEM_MODU/';
    Ext.define('Desktop.Facturacion.Models.PERF_ITEM_MODU', {
        extend: 'Ext.data.Model',
        AJAX_URI:AJAX_URI,
        fields: [
            {
                name: 'CO_PERF',
                type: 'int',
                reference: {
                    parent: 'PERF'
                }                
            },
            {
                name: 'CO_ITEM_MODU',
                type: 'int',
                reference: {
                    parent: 'ITEM_MODU'
                }                
            },
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