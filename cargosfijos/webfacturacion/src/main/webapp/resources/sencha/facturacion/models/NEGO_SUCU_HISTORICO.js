(function(){
    var AJAX_URI=settings.AJAX_URI+'NEGO_SUCU_HISTORICO/';
    Ext.define('Desktop.Facturacion.Models.NEGO_SUCU_HISTORICO', {
        extend: 'Ext.data.Model',
        AJAX_URI:AJAX_URI,
        fields: [
            {name: 'CO_NEGO_SUCU_HIST', type: 'int'},            
            {
                name: 'CO_NEGO',
                type: 'int',
                reference: {
                    parent: 'NEGO'
                }                
            },      
            {
                name: 'CO_NEGO_SUCU',
                type: 'int',
                reference: {
                    parent: 'NEGO_SUCU'
                }                
            }, 
            {name: 'DE_INFORMACION', type: 'string'},       
            {name: 'FH_REGI', type: 'date', dateFormat: 'd/m/Y H:i:s'},
            {name: 'USUA.DE_USER',type:'string', mapping: 'USUA.DE_USER'},
            
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