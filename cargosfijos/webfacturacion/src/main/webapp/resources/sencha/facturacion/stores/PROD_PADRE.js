(function(){
    var AJAX_URI=settings.AJAX_URI+'PROD_PADRE/';
    Ext.define('Desktop.Facturacion.Stores.PROD_PADRE', {
        extend:'Ext.data.Store',
        model: 'Desktop.Facturacion.Models.PROD_PADRE',
        alias:'store.Desktop.Facturacion.Stores.PROD_PADRE',
        AJAX_URI:AJAX_URI,
        proxy: {
            type: 'ajax',
            url : '/ajax/',
            noCache: false,            
            actionMethods: {read   : 'POST'},
            api: {
                read:AJAX_URI+'select/',             
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


