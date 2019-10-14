(function(){
    var AJAX_URI=settings.AJAX_URI+'NEGO_SUCU_PLAN/';
    Ext.define('Desktop.Facturacion.Models.NEGO_SUCU_PLAN', {
        extend: 'Ext.data.Model',
        AJAX_URI:AJAX_URI,
        fields: [
            {name: 'CO_NEGO_SUCU_PLAN', type: 'int'},
            {
                name: 'CO_NEGO_SUCU',
                type: 'int',
                reference: {
                    parent: 'NEGO_SUCU'
                }                
            },            
            {
                name: 'CO_PLAN',
                type: 'int',
                reference: {
                    parent: 'PLAN'
                }                
            }, 
            {name: 'ST_SOAR_INST', type: 'boolean'}, 
            {name: 'CO_OIT_INST', type: 'int'}, 
            {name: 'ST_SOAR_DESI', type: 'boolean'}, 
            {name: 'CO_OIT_DESI', type: 'int'},             
            {name: 'NO_PLAN', type: 'string'},
            {name: 'FE_INIC', type: 'date',dateFormat: 'Y-m-d'},
            {name: 'FE_FIN', type: 'date',dateFormat: 'Y-m-d'},
            {name: 'FE_ULTI_FACT', type: 'date',dateFormat: 'Y-m-d'},
            {name: 'IM_MONTO', type: 'float'},
            {name: 'DE_VELO_SUBI', type: 'string'},
            {name: 'DE_VELO_BAJA', type: 'string'},
            
            
            //
            {name:'PLAN.MONE_FACT.DE_SIMB',type:'string', mapping: 'PLAN.MONE_FACT.DE_SIMB'},
            {name:'PLAN.MONE_FACT.NO_MONE_FACT',type:'string', mapping: 'PLAN.MONE_FACT.NO_MONE_FACT'},
        ],        
        proxy: {
            type: 'ajax',
            url : '/ajax/',
            noCache: false,
            actionMethods: {create : 'POST',read   : 'POST',update : 'POST',destroy: 'POST'},
            api: {
                read:AJAX_URI+'select/',
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