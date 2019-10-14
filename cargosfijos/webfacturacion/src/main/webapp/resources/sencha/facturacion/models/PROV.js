(function(){
    var AJAX_URI=settings.AJAX_URI+'PROV/';
    Ext.define('Desktop.Facturacion.Models.PROV', {
        extend: 'Ext.data.Model',
        AJAX_URI:AJAX_URI,
        fields: [
            {name: 'CO_PROV', type: 'int'},
            {name: 'NO_PROV', type: 'string'},
            {
                name: 'CO_DEPA',
                type: 'int',
                reference: {
                    parent: 'DEPA'
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