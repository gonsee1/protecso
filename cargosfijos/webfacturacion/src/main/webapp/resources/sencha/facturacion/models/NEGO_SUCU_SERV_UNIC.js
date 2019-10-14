(function(){
    var AJAX_URI=settings.AJAX_URI+'NEGO_SUCU_SERV_UNIC/';
    Ext.define('Desktop.Facturacion.Models.NEGO_SUCU_SERV_UNIC', {
        extend: 'Ext.data.Model',
        AJAX_URI:AJAX_URI,
        fields: [
            {name: 'CO_NEGO_SUCU_SERV_UNIC', type: 'int'},
            {
                name: 'CO_NEGO_SUCU',
                type: 'int',
                reference: {
                    parent: 'NEGO_SUCU'
                }                
            },            
            {
                name: 'CO_SERV_UNIC',
                type: 'int',
                reference: {
                    parent: 'SERV_UNIC'
                }                
            }, 
            {name: 'ST_SOAR_INST', type: 'boolean'}, 
            {name: 'CO_OIT_INST', type: 'int'},             
            {name: 'NO_SERV_UNIC', type: 'string'},            
            {name: 'IM_MONTO', type: 'float'},
            {name: 'ST_AFEC_DETR', type: 'boolean'},
            {name: 'ST_ELIM', type: 'boolean'},
            {
                name: 'CO_CIER',
                type: 'int',
                reference: {
                    parent: 'CIER'
                }                
            },
            // MONEDA
            {name:'SERV_UNIC.MONE_FACT.DE_SIMB',type:'string', mapping: 'SERV_UNIC.MONE_FACT.DE_SIMB'},
            {name:'SERV_UNIC.MONE_FACT.NO_MONE_FACT',type:'string', mapping: 'SERV_UNIC.MONE_FACT.NO_MONE_FACT'},
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