(function(){
    var AJAX_URI=settings.AJAX_URI+'PROD/';
    Ext.define('Desktop.Facturacion.Stores.PROD', {
        extend:'Ext.data.Store',
        model: 'Desktop.Facturacion.Models.PROD',
        alias:'store.Desktop.Facturacion.Stores.PROD',
        AJAX_URI:AJAX_URI,
        proxy: {
            type: 'ajax',
            url : '/ajax/',
            noCache: false,            
            actionMethods: {create : 'POST',read   : 'POST',update : 'POST',destroy: 'POST'},
            api: {
                read:AJAX_URI+'select/',
                update:AJAX_URI+'update/',
                create:AJAX_URI+'insert/',
                destroy:AJAX_URI+'delete/',
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