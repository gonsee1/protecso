(function(){
    var AJAX_URI=settings.AJAX_URI+'SUSP/';
    Ext.define('Desktop.Facturacion.Stores.SUSP', {
        extend:'Ext.data.Store',
        model: 'Desktop.Facturacion.Models.SUSP',
        alias:'store.Desktop.Facturacion.Stores.SUSP',
        AJAX_URI:AJAX_URI,
        proxy: {
            type: 'ajax',
            url : '/ajax/',
            noCache: false,            
            actionMethods: {create : 'POST',read   : 'POST',update : 'POST',destroy: 'POST'},
            api: {
                read:AJAX_URI+'selectSuspensionesPendientesByProducto/',
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