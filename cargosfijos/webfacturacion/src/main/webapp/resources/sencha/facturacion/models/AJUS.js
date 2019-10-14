(function(){
    var AJAX_URI=settings.AJAX_URI+'AJUS/';
    Ext.define('Desktop.Facturacion.Models.AJUS', {
        extend: 'Ext.data.Model',
        AJAX_URI:AJAX_URI,
        fields: [
            {name: 'CO_AJUS', type: 'int',defaultValue:null},
            {name: 'CO_NEGO',type: 'int',defaultValue:null},            
            {name: 'DE_GLOS',type: 'string',defaultValue:null}, 
            {name: 'IM_MONT', type: 'float',defaultValue:null}, 
            {name: 'ST_AFEC_IGV', type: 'boolean',defaultValue:1}, 
            {name: 'CO_CIER_APLI', type: 'int',defaultValue:false},             
            {name: 'ST_PEND', type: 'boolean',defaultValue:false},
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