(function(){
    var AJAX_URI=settings.AJAX_URI+'DEPA/';
    Ext.define('Desktop.Facturacion.Models.DEPA', {
        extend: 'Ext.data.Model',
        AJAX_URI:AJAX_URI,
        fields: [
            {name: 'CO_DEPA', type: 'int'},
            {name: 'NO_DEPA', type: 'string'},        
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
/*
        proxy:{
            type:'rest',
            noCache: false,
            actionMethods: {create : 'POST',read   : 'POST',update : 'POST',destroy: 'POST'},
            api: {
                read:AJAX_URI,
                update:AJAX_URI+'update/',
                create:AJAX_URI+'create/',
                delete:AJAX_URI+'destroy/',
            },
            reader:{rootProperty:'data'}
        }  
 */