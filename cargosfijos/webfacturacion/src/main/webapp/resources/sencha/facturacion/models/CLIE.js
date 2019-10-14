(function(){
    var AJAX_URI=settings.AJAX_URI+'CLIE/';
    Ext.define('Desktop.Facturacion.Models.CLIE', {
        extend: 'Ext.data.Model',
        AJAX_URI:AJAX_URI,
        fields: [
            {name: 'CO_CLIE', type: 'int'},
            {name: 'NO_RAZO', type: 'string'},
            {name: 'NO_CLIE', type: 'string'},
            {name: 'AP_CLIE', type: 'string'},
            {name: 'DE_CODI_BUM', type: 'string'},
            {name: 'DE_DIGI_BUM', type: 'string'},
            {
                name: 'CO_CONT_CLIE_REPR_LEGA',
                type: 'int',
                reference: {
                    parent: 'CONT_CLIE'
                }                
            },
            {
                name: 'CO_TIPO_CLIE',
                type: 'int',
                reference: {
                    parent: 'TIPO_CLIE'
                }                
            },             
            {
                name: 'CO_SUCU_FISC',
                type: 'int',
                reference: {
                    parent: 'SUCU'
                }                
            },            
            {
                name: 'CO_TIPO_DOCU',
                type: 'int',
                reference: {
                    parent: 'TIPO_DOCU'
                }                
            },
            {name: 'DE_NUME_DOCU', type: 'string'},
            {
                name: 'CO_SUCU_FISC',
                type: 'int'             
            },
            {name: 'DE_EJEC', type: 'string'},
            {name: 'DE_SUB_GERE', type: 'string'},
            {name: 'DE_SEGM', type: 'string'},
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
    Desktop.Facturacion.Models.CLIE.getData=function(o){
        if (typeof o == 'object'){
            if (o instanceof Desktop.Facturacion.Models.CLIE)
                return o.data;
            return o;
        }
        return null;
    }
    Desktop.Facturacion.Models.CLIE.getFullName=function(po){
        var o=Desktop.Facturacion.Models.CLIE.getData(po);
        if (typeof o =='object'){
            if (o.CO_TIPO_CLIE==1)
                return o.NO_CLIE+" "+o.AP_CLIE; 
            if (o.CO_TIPO_CLIE==2)
                return o.NO_RAZO;
        }
        return '';
    }
    Desktop.Facturacion.Models.CLIE.getFullInfo=function(po){
        var ret='';
        var o=Desktop.Facturacion.Models.CLIE.getData(po);        
        if (typeof o =='object'){
            ret+=Desktop.Facturacion.Models.CLIE.getFullName(o);
            ret+=" (ND: "+Ext.String.trim(o.DE_NUME_DOCU)+")";
        }        
        return ret;
    } 
    Desktop.Facturacion.Models.CLIE.getInfoRepresentante=function(po){
        var ret='';
        var o=Desktop.Facturacion.Models.CLIE.getData(po);        
        if (typeof o =='object'){
            if (o.CONT_CLIE_REPR_LEGA){
                return (o.CONT_CLIE_REPR_LEGA.AP_CONT_CLIE || "") +', '+ (o.CONT_CLIE_REPR_LEGA.NO_CONT_CLIE || "") +" ND: "+ (o.CONT_CLIE_REPR_LEGA.DE_NUME_DOCU || "");
            }            
        }        
        return "(No definido)";
    }    
    
})();
