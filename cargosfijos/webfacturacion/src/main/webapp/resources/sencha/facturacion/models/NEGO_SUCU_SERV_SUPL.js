(function(){
    var AJAX_URI=settings.AJAX_URI+'NEGO_SUCU_SERV_SUPL/';
    Ext.define('Desktop.Facturacion.Models.NEGO_SUCU_SERV_SUPL', {
        extend: 'Ext.data.Model',
        AJAX_URI:AJAX_URI,
        fields: [
            {name: 'CO_NEGO_SUCU_SERV_SUPL', type: 'int'},
            {
                name: 'CO_NEGO_SUCU',
                type: 'int',
                reference: {
                    parent: 'NEGO_SUCU'
                }                
            },            
            {
                name: 'CO_SERV_SUPL',
                type: 'int',
                reference: {
                    parent: 'SERV_SUPL'
                }                
            }, 
            {name: 'ST_SOAR_INST', type: 'boolean'}, 
            {name: 'CO_OIT_INST', type: 'int'}, 
            {name: 'ST_SOAR_DESI', type: 'boolean'}, 
            {name: 'CO_OIT_DESI', type: 'int'},             
            {name: 'NO_SERV_SUPL', type: 'string'},
            {name: 'FE_INIC', type: 'date',dateFormat: 'Y-m-d'},
            {name: 'FE_FIN', type: 'date',dateFormat: 'Y-m-d'},
            {name: 'FE_ULTI_FACT', type: 'date',dateFormat: 'Y-m-d'},
            {name: 'IM_MONTO', type: 'float'},
            {name: 'ST_AFEC_DETR', type: 'boolean'},

            //
            {name:'SERV_SUPL.MONE_FACT.DE_SIMB',type:'string', mapping: 'SERV_SUPL.MONE_FACT.DE_SIMB'},
            {name:'SERV_SUPL.MONE_FACT.NO_MONE_FACT',type:'string', mapping: 'SERV_SUPL.MONE_FACT.NO_MONE_FACT'},
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