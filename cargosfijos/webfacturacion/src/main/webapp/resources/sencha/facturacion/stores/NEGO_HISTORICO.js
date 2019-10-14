(function(){
    var AJAX_URI=settings.AJAX_URI+'NEGO_HISTORICO/';
    Ext.define('Desktop.Facturacion.Stores.NEGO_HISTORICO', {
        extend:'Ext.data.Store',
        model: 'Desktop.Facturacion.Models.NEGO_HISTORICO',
        alias:'store.Desktop.Facturacion.Stores.NEGO_HISTORICO',
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