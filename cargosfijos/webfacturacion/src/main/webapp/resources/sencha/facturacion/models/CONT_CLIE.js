(function(){
    var AJAX_URI=settings.AJAX_URI+'CONT_CLIE/';
    Ext.define('Desktop.Facturacion.Models.CONT_CLIE', {
        extend: 'Ext.data.Model',
        AJAX_URI:AJAX_URI,
        fields: [
            {name: 'CO_CONT_CLIE', type: 'int'},
            {name: 'CO_CLIE',type: 'int'},
            {name: 'CO_TIPO_DOCU', type: 'int'},
            {name: 'DE_NUME_DOCU', type: 'string'},
            {name: 'NO_CONT_CLIE', type: 'string'},
            {name: 'AP_CONT_CLIE', type: 'string'},
            {name: 'DE_CORR', type: 'string'},
            {name: 'DE_TELE', type: 'string'},
            {
                name    : 'FULL_NAME_CONT_CLIE', 
                convert : function (v, rec) {
                   return rec.get('NO_CONT_CLIE') + ' ' + rec.get('AP_CONT_CLIE');
                }
            }
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
