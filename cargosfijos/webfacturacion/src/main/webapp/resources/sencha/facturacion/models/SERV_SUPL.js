(function(){
    var AJAX_URI=settings.AJAX_URI+'SERV_SUPL/';
    Ext.define('Desktop.Facturacion.Models.SERV_SUPL', {
        extend: 'Ext.data.Model',
        AJAX_URI:AJAX_URI,
        fields: [
            {name: 'CO_SERV_SUPL', type: 'int'}, 
            {name: 'NO_SERV_SUPL', type: 'string'},                         
            {
                name: 'CO_PROD',
                type: 'int',
                reference: {
                    parent: 'PROD'
                }                
            },
            {
                name: 'CO_MONE_FACT',
                type: 'int',
                reference: {
                    parent: 'MONE_FACT'
                }                
            },            
            {name: 'IM_MONTO', type: 'float'},
            {name: 'ST_AFEC_DETR', type: 'boolean'},
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