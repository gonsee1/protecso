(function(){
    var AJAX_URI=settings.AJAX_URI+'NEGO_SUCU_PROM/';
    Ext.define('Desktop.Facturacion.Models.NEGO_SUCU_PROM', {
        extend: 'Ext.data.Model',
        AJAX_URI:AJAX_URI,
        fields: [
            {name: 'CO_NEGO_SUCU_PROM', type: 'int',defaultValue:null},
            {name: 'CO_NEGO_SUCU',type: 'int',defaultValue:null},             
            {name: 'DE_TIPO', type: 'int',defaultValue:2}, 
            {name: 'IM_VALO', type: 'float',defaultValue:0}, 
            {name: 'ST_PLAN', type: 'boolean',defaultValue:false}, 
            {name: 'ST_SERV_SUPL', type: 'boolean',defaultValue:false}, 
            {name: 'DE_PERI_INIC', type: 'int',defaultValue:null},
            {name: 'DE_ANO_INIC', type: 'int',defaultValue:null},
            {name: 'DE_PERI_FIN', type: 'int',defaultValue:null},
            {name: 'DE_ANO_FIN', type: 'int',defaultValue:null},
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