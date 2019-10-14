(function(){
    var AJAX_URI=settings.AJAX_URI+'PLAN_MEDI_INST/';
    Ext.define('Desktop.Facturacion.Models.PLAN_MEDI_INST', {
        extend: 'Ext.data.Model',
        AJAX_URI:AJAX_URI,
        fields: [
            {name: 'CO_PLAN_MEDI_INST', type: 'int'},
            {name: 'NO_PLAN_MEDI_INST', type: 'string'},        
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