(function(){
    var AJAX_URI=settings.AJAX_URI+'ITEM_MODU/';
    Ext.define('Desktop.Facturacion.Models.ITEM_MODU', {
        extend: 'Ext.data.Model',
        AJAX_URI:AJAX_URI,
        fields: [
            {name: 'CO_ITEM_MODU', type: 'int'},
            {name: 'NO_ITEM_MODU', type: 'string'},
            {name: 'DE_PACK', type: 'string'},
            {
                name: 'CO_MODU',
                type: 'int',
                reference: {
                    parent: 'MODU'
                }                
            },
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