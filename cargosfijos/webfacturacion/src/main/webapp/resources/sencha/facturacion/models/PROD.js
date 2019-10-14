(function(){
    var AJAX_URI=settings.AJAX_URI+'PROD/';
    Ext.define('Desktop.Facturacion.Models.PROD', {
        extend: 'Ext.data.Model',
        AJAX_URI:AJAX_URI,
        fields: [
            {name: 'CO_PROD', type: 'int'},
            {name: 'NO_PROD', type: 'string'},
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