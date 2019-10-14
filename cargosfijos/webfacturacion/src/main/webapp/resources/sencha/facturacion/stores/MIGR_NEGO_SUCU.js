(function(){
    var AJAX_URI=settings.AJAX_URI+'MIGR_NEGO_SUCU/';
    Ext.define('Desktop.Facturacion.Stores.MIGR_NEGO_SUCU', {
        extend:'Ext.data.Store',
        model: 'Desktop.Facturacion.Models.MIGR_NEGO_SUCU',
        alias:'store.Desktop.Facturacion.Stores.MIGR_NEGO_SUCU',
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





