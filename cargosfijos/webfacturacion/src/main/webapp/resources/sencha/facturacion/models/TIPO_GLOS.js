(function(){
    var AJAX_URI=settings.AJAX_URI+'TIPO_GLOS/';
    Ext.define('Desktop.Facturacion.Models.TIPO_GLOS', {
        extend: 'Ext.data.Model',
        AJAX_URI:AJAX_URI,
        fields: [
            {name: 'CO_TIPO_GLOS', type: 'int'},
            {name: 'NO_TIPO_GLOS', type: 'string'},        
        ],
        proxy: {
            type: 'ajax',
            url : '/ajax/',
            noCache: false,
            actionMethods: {create : 'POST',read   : 'POST',update : 'POST',destroy: 'POST'},
            api: {
                read:AJAX_URI+'select/',
                update:AJAX_URI+'update/',
                create:AJAX_URI+'insert/',
                delete:AJAX_URI+'delete/',
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