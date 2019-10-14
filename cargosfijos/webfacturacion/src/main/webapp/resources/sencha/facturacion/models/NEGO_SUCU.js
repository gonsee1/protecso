(function(){
    var AJAX_URI=settings.AJAX_URI+'NEGO_SUCU/';
    Ext.define('Desktop.Facturacion.Models.NEGO_SUCU', {
        extend: 'Ext.data.Model',
        AJAX_URI:AJAX_URI,
        fields: [
            {name: 'CO_NEGO_SUCU', type: 'int'},                        
            {
                name: 'CO_NEGO',
                type: 'int',
                reference: {
                    parent: 'NEGO'
                }                
            },                         
            {
                name: 'CO_SUCU',
                type: 'int',
                reference: {
                    parent: 'SUCU'
                }                
            },
            {
                name: 'CO_MOTI_DESC',
                type: 'int',
                reference: {
                    parent: 'MOTI_DESC'
                }                
            },  
            {name: 'FE_INIC', type: 'date', dateFormat: 'Y-m-d'}, 
            {name: 'FE_FIN', type: 'date', dateFormat: 'Y-m-d'}, 
            {name: 'ST_SOAR_INST', type: 'boolean'}, 
            {name: 'CO_OIT_INST', type: 'int'},  
            {name: 'ST_SOAR_DESI', type: 'boolean'}, 
            {name: 'CO_OIT_DESI', type: 'int'}, 
            {name: 'CO_CIRC', type: 'int'}, 
            //{name: 'DE_INFO_DESC', type: 'string'}, 
            {name: 'REFERENCIA_NEGO_SUCU', type: 'string'},             
            //para filters
            {name:'SUCU.DE_DIRE',type:'string', mapping: 'SUCU.DE_DIRE'},
            {name:'SUCU.DIST.NO_DIST',type:'string', mapping: 'SUCU.DIST.NO_DIST'},
            {name:'SUCU.DIST.PROV.NO_PROV',type:'string', mapping: 'SUCU.DIST.PROV.NO_PROV'},
            {name:'SUCU.DIST.PROV.DEPA.NO_DEPA',type:'string', mapping: 'SUCU.DIST.PROV.DEPA.NO_DEPA'},            
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