(function(){
    var AJAX_URI=settings.AJAX_URI+'PROD_PADRE/';
    Ext.define('Desktop.Facturacion.Models.PROD_PADRE', {
        extend: 'Ext.data.Model',
        AJAX_URI:AJAX_URI,
        fields: [
            {name: 'COD_PROD_PADRE', type: 'int'},
            {name: 'DESC_PROD_PADRE', type: 'string'},
        ],        
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
                type:'ajax',
            }
        },    
    });    
})();

