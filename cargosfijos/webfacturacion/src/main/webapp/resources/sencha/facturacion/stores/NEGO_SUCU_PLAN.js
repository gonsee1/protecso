(function(){
    var AJAX_URI=settings.AJAX_URI+'NEGO_SUCU_PLAN/';
    Ext.define('Desktop.Facturacion.Stores.NEGO_SUCU_PLAN', {
        extend:'Ext.data.Store',
        model: 'Desktop.Facturacion.Models.NEGO_SUCU_PLAN',
        alias:'store.Desktop.Facturacion.Stores.NEGO_SUCU_PLAN',
        AJAX_URI:AJAX_URI,
        proxy: {
            type: 'ajax',
            url : '/ajax/',
            noCache: false,            
            actionMethods: {create : 'POST',read   : 'POST',update : 'POST',destroy: 'POST'},
            api: {
                read:AJAX_URI+'select/',
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