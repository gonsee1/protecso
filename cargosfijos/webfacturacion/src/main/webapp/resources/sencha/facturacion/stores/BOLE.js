(function(){
    var AJAX_URI=settings.AJAX_URI+'BOLE/';
    Ext.define('Desktop.Facturacion.Stores.BOLE', {
        extend:'Ext.data.Store',
        model: 'Desktop.Facturacion.Models.BOLE',
        alias:'store.Desktop.Facturacion.Stores.BOLE',
        AJAX_URI:AJAX_URI,
        proxy: {
//            timeout: 90000,
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
                totalProperty:'numResults',
                successProperty:'success',
            },
            write:{
                type:'json',
            }            
        },   
        autoLoad: true,   
    });
})();