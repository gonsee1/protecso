(function(){
    var AJAX_URI=settings.AJAX_URI+'BOLE/';
    Ext.define('Desktop.Facturacion.Models.BOLE', {
        extend: 'Ext.data.Model',
        AJAX_URI:AJAX_URI,
        fields: [
            {name: 'CO_BOLE', type: 'int'},  
            {name: 'CO_CIER', type: 'int'},
            {name: 'CO_NEGO', type: 'int'}, 
            
            {name: 'DE_PERI', type: 'string'},
            {name: 'IM_NETO', type: 'float'},
            {name: 'IM_IGV', type: 'float'},
            {name: 'IM_TOTA', type: 'float'},
            {name: 'IM_INST', type: 'float'},
            {name: 'IM_SERV_MENS', type: 'float'},
            {name: 'IM_ALQU', type: 'float'},
            {name: 'IM_OTRO', type: 'float'},
            {name: 'IM_DESC', type: 'float'},
            
            {name: 'ST_ANUL', type: 'boolean'},
            {name: 'ST_ELIM', type: 'boolean'},
            
            
            {name: 'FE_EMIS', type: 'date',dateFormat: 'Y-m-d'},
            {name: 'FE_VENC', type: 'date',dateFormat: 'Y-m-d'},
            {name: 'CO_CLIE', type: 'int'},
            {name: 'DE_CODI_BUM', type: 'string'},
            {name: 'NO_CLIE', type: 'string'},
            {name: 'DE_DIRE_FISC', type: 'string'},
            {name: 'CO_DIST_FISC', type: 'int'},
            {name: 'NO_DIST_FISC', type: 'string'},
            {name: 'CO_TIPO_DOCU', type: 'int'},
            {name: 'NO_TIPO_DOCU', type: 'string'},
            {name: 'DE_NUME_DOCU', type: 'string'},
            {name: 'DE_DIRE_CORR', type: 'string'},
            {name: 'CO_DIST_CORR', type: 'int'},
            {name: 'NO_DIST_CORR', type: 'string'},
            {name: 'DE_DIRE_INST', type: 'string'},
            {name: 'CO_DIST_INST', type: 'int'},
            {name: 'NO_DIST_INST', type: 'string'},
            {name: 'CO_MONE_FACT', type: 'int'},
            {name: 'NO_MONE_FACT', type: 'string'},
            {name: 'DE_SIMB_MONE_FACT', type: 'string'},
            
           ],        
        proxy: {
//            timeout: 90000,
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
