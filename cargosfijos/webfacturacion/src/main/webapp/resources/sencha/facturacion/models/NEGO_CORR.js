(function(){
    var AJAX_URI=settings.AJAX_URI+'NEGO_CORR/';
    Ext.define('Desktop.Facturacion.Models.NEGO_CORR', {
        extend: 'Ext.data.Model',
        AJAX_URI:AJAX_URI,
        fields: [
            {name: 'CO_NEGO_CORR', type: 'int',defaultValue:null},
            {name: 'CO_NEGO',type: 'int',defaultValue:null},            
            {name: 'DE_CORR',type: 'string',defaultValue:null}, 
            {name: 'ST_ACTI', type: 'boolean',defaultValue:null}, 
        ],        
        proxy: {
            type: 'ajax',
            url : '/ajax/',
            noCache: false,
            actionMethods: {create : 'POST',read   : 'POST',update : 'POST',destroy: 'POST'},
            api: {
                read:AJAX_URI+'select/',
                create:AJAX_URI+'insert/',
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