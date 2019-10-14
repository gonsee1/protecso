(function(){
    var AJAX_URI=settings.AJAX_URI+'MIGR_NEGO_SUCU/';
    Ext.define('Desktop.Facturacion.Models.MIGR_NEGO_SUCU', {
        extend: 'Ext.data.Model',
        AJAX_URI:AJAX_URI,
        fields: [
            {
                name: 'CO_NEGO_SUCU',
                type: 'int',
                reference: {
                    parent: 'NEGO_SUCU'
                }                
            },
            {name: 'CO_MIGR_NEGO_SUCU', type: 'int',defaultValue:null},
            {name: 'CO_NEGO_ORIG',type: 'int',defaultValue:null},            
            {name: 'CO_NEGO_DEST',type: 'int',defaultValue:null}, 
            {name: 'FH_REGI', type: 'date', dateFormat: 'd/m/Y H:i:s'},
            {name: 'USUA.DE_USER',type:'string', mapping: 'USUA.DE_USER'},
            
            //para filters
//            {name:'NEGO_SUCU.SUCU.DE_DIRE',type:'string', mapping: 'NEGO_SUCU.SUCU.DE_DIRE'},
//            {name:'NEGO_SUCU.SUCU.DIST.NO_DIST',type:'string', mapping: 'NEGO_SUCU.SUCU.DIST.NO_DIST'},
//            {name:'NEGO_SUCU.SUCU.DIST.PROV.NO_PROV',type:'string', mapping: 'NEGO_SUCU.SUCU.DIST.PROV.NO_PROV'},
//            {name:'NEGO_SUCU.SUCU.DIST.PROV.DEPA.NO_DEPA',type:'string', mapping: 'NEGO_SUCU.SUCU.DIST.PROV.DEPA.NO_DEPA'},  
        ],        
        proxy: {
            type: 'ajax',
            url : '/ajax/',
            noCache: false,
            actionMethods: {create : 'POST',read   : 'POST',update : 'POST',destroy: 'POST'},
            api: {
                read:AJAX_URI+'select/',
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





