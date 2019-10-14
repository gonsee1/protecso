(function(){
    var AJAX_URI=settings.AJAX_URI+'SUCU/';
    Ext.define('Desktop.Facturacion.Models.SUCU', {
        extend: 'Ext.data.Model',
        AJAX_URI:AJAX_URI,
        fields: [
            {name: 'CO_SUCU', type: 'int'}, 
            {name: 'DE_DIRE', type: 'string'},                         
            {name: 'DE_REF_DIRE', type: 'string'},
            {
                name: 'CO_CLIE',
                type: 'int',
                reference: {
                    parent: 'CLIE'
                }                
            },                         
            {
                name: 'CO_DIST',
                type: 'int',
                reference: {
                    parent: 'DIST'
                }                
            },
            
            //para filters            
            {name:'DIST.NO_DIST',type:'string', mapping: 'DIST.NO_DIST'},
            {name:'DIST.PROV.NO_PROV',type:'string', mapping: 'DIST.PROV.NO_PROV'},
            {name:'DIST.PROV.DEPA.NO_DEPA',type:'string', mapping: 'DIST.PROV.DEPA.NO_DEPA'},
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