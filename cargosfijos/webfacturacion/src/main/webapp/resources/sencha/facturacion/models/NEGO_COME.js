(function(){
    var AJAX_URI=settings.AJAX_URI+'NEGO_COME/';
    Ext.define('Desktop.Facturacion.Models.NEGO_COME', {
        extend: 'Ext.data.Model',
        AJAX_URI:AJAX_URI,
        fields: [
            {name: 'CO_NEGO_COME', type: 'int',defaultValue:null},
            {name: 'CO_USUA',type: 'int',defaultValue:null},            
            {name: 'CO_NEGO',type: 'int',defaultValue:null}, 
            {name: 'DE_COME', type: 'string',defaultValue:null}, 
            {name: 'FH_CREO', type: 'date', dateFormat: 'd/m/Y H:i:s'},
            {name:'USUA.DE_USER',type:'string', mapping: 'USUA.DE_USER'}
        ],        
        proxy: {
            type: 'ajax',
            url : '/ajax/',
            noCache: false,
            actionMethods: {create : 'POST',read   : 'POST',update : 'POST',destroy: 'POST'},
            api: {
                read:AJAX_URI+'select/',
                create:AJAX_URI+'insert/'
            },
            reader:{
                type: 'json',
                rootProperty: 'data',
                successProperty:'success'
            },
            write:{
                type:'ajax'
            }
        }    
    });    
})();


