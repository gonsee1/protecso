(function(){
    var AJAX_URI=settings.AJAX_URI+'PLAN_MEDI_INST/';
    Ext.define('Desktop.Facturacion.Stores.PLAN_MEDI_INST', {
        extend:'Ext.data.Store',
        model: 'Desktop.Facturacion.Models.PLAN_MEDI_INST',
        alias:'store.Desktop.Facturacion.Stores.PLAN_MEDI_INST',
        AJAX_URI:AJAX_URI,
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
                type:'json',
            }            
        },   
        autoLoad: true,   
    });
})();
    /*data:[
        { 'CO_MODU': 'Lisa',  "NO_MODU":"lisa@simpsons.com",  "DE_PACK":"555-111-1224"  },
        { 'CO_MODU': 'Bart',  "NO_MODU":"bart@simpsons.com",  "DE_PACK":"555-222-1234" },
        { 'CO_MODU': 'Homer', "NO_MODU":"homer@simpsons.com",  "DE_PACK":"555-222-1244"  },
        { 'CO_MODU': 'Marge', "NO_MODU":"marge@simpsons.com", "DE_PACK":"555-222-1254"  }        
    ],
    
    data:{'items':[
        { 'CO_MODU': 'Lisa',  "NO_MODU":"lisa@simpsons.com",  "DE_PACK":"555-111-1224"  },
        { 'CO_MODU': 'Bart',  "NO_MODU":"bart@simpsons.com",  "DE_PACK":"555-222-1234" },
        { 'CO_MODU': 'Homer', "NO_MODU":"homer@simpsons.com",  "DE_PACK":"555-222-1244"  },
        { 'CO_MODU': 'Marge', "NO_MODU":"marge@simpsons.com", "DE_PACK":"555-222-1254"  }
    ]},
    proxy: {
        type: 'memory',
        reader: {
            type: 'json',
            rootProperty: 'items'
        }
    },   
    autoLoad: true*/