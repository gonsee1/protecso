(function(){
    var AJAX_URI=settings.AJAX_URI+'RECI_GLOS/';
    Ext.define('Desktop.Facturacion.Models.RECI_GLOS', {
        extend: 'Ext.data.Model',
        AJAX_URI:AJAX_URI,
        fields: [
            {name: 'CO_RECI_GLOS', type: 'int'}, 
            {name: 'CO_RECI', type: 'int'}, 
            {name: 'CO_NEGO_SUCU', type: 'int'},
            {name: 'NO_GLOS', type: 'string'},
            {name: 'DE_DIRE_SUCU', type: 'string'},
            {name: 'IM_MONT', type: 'float'},            
            {name: 'TI_GLOS', type: 'int'},
            {name: 'ST_ELIM', type: 'boolean'}
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
