(function(){
    var AJAX_URI=settings.AJAX_URI+'PLAN/';
    
    Ext.define('Desktop.Facturacion.Models.PLAN', {
        extend: 'Ext.data.Model',
        AJAX_URI:AJAX_URI,
        fields: [
            {name: 'CO_PLAN', type: 'int'}, 
            {name: 'NO_PLAN', type: 'string'},                         
            {
                name: 'CO_PROD',
                type: 'int',
                reference: {
                    parent: 'PROD'
                }                
            },
            {name: 'DE_VELO_SUBI', type: 'string'},
            {name: 'DE_VELO_BAJA', type: 'string'},
            {name: 'DE_MEDI_INST', type: 'int'},
            {name: 'IM_MONTO', type: 'float'},
            {name: 'CO_PLAN_MEDI_INST', type:'int'},
            {name: 'CO_MONE_FACT', type:'int'},
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