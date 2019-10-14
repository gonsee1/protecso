(function(){
    var AJAX_URI=settings.AJAX_URI+'MOTI_DESC/';
    Ext.define('Desktop.Facturacion.Models.MOTI_DESC', {
        extend: 'Ext.data.Model',
        AJAX_URI:AJAX_URI,
        fields: [
            {name: 'CO_MOTI_DESC', type: 'int'},
            {name: 'NO_MOTI_DESC', type: 'string'},
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
                destroy:AJAX_URI+'delete/',
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


