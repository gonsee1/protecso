(function(){
    var AJAX_URI=settings.AJAX_URI+'NEGO/';
    Ext.define('Desktop.Facturacion.Models.NEGO', {
        extend: 'Ext.data.Model',
        AJAX_URI:AJAX_URI,
        fields: [
            {name: 'CO_NEGO', type: 'int'},                      
            {
                name: 'CO_SUCU_CORR',
                type: 'int',
                reference: {
                    parent: 'SUCU'
                }                
            },  
            {
                name: 'CO_CLIE',
                type: 'int',
                reference: {
                    parent: 'CLIE'
                }                
            },           
            {
                name: 'CO_PROD',
                type: 'int',
                reference: {
                    parent: 'PROD'
                }                
            },           
            {
                name: 'CO_TIPO_FACT',
                type: 'int',
                reference: {
                    parent: 'TIPO_FACT'
                }                
            },
            {
                name: 'CO_MONE_FACT',
                type: 'int',
                reference: {
                    parent: 'MONE_FACT'
                }                
            },
            {
                name: 'CO_PERI_FACT',
                type: 'int',
                reference: {
                    parent: 'PERI_FACT'
                }                
            },
            {name: 'REFERENCIA', type: 'string'},    
            {
                
            }
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