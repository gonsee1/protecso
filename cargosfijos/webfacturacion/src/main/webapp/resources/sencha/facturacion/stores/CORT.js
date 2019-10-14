(function(){
    var AJAX_URI=settings.AJAX_URI+'CORT/';
    Ext.define('Desktop.Facturacion.Stores.CORT', {
        extend:'Ext.data.Store',
        model: 'Desktop.Facturacion.Models.CORT',
        alias:'store.Desktop.Facturacion.Stores.CORT',
        AJAX_URI:AJAX_URI,
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
                type:'json',
            }            
        },   
        autoLoad: true,   
    });
})();