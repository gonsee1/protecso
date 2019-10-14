(function(){
    var AJAX_URI=settings.AJAX_URI+'SERV_UNIC/';
    Ext.define('Desktop.Facturacion.Stores.SERV_UNIC', {
        extend:'Ext.data.Store',
        model: 'Desktop.Facturacion.Models.SERV_UNIC',
        alias:'store.Desktop.Facturacion.Stores.SERV_UNIC',
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