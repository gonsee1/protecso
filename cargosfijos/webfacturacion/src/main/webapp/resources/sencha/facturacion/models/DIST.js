(function(){
    var AJAX_URI=settings.AJAX_URI+'DIST/';
    Ext.define('Desktop.Facturacion.Models.DIST', {
        extend: 'Ext.data.Model',
        AJAX_URI:AJAX_URI,
        fields: [
            {name: 'CO_DIST', type: 'int'},
            {name: 'NO_DIST', type: 'string'},
            {
                name: 'CO_PROV',
                type: 'int',
                reference: {
                    parent: 'PROV'
                }                
            },
            
            //para filters             
            {name:'PROV.NO_PROV',type:'string', mapping: 'PROV.NO_PROV'},
            {name:'PROV.DEPA.NO_DEPA',type:'string', mapping: 'PROV.DEPA.NO_DEPA'},  
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