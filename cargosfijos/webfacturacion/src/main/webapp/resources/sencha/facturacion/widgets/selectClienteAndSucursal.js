(function(){
    var package='Desktop.Facturacion.Widgets.selectClienteAndSucursal';    
    Ext.define(package,{
        extend:'Ext.form.FieldContainer',
        alias:'widget.'+package,        
        initComponent:function(){            
            var obj=this;
            obj.width=obj.width || "500px";
            obj.width=parseInt(obj.width);            
            obj.names=obj.names || {};
            obj.defaultValues=obj.defaultValues || {};
            obj.labels=obj.labels || {};
            obj.names.CO_CLIE=obj.names.CO_CLIE || "CO_CLIE";
            obj.names.CO_SUCU=obj.names.CO_SUCU || "CO_SUCU";
            obj.labels.SUCU=obj.labels.SUCU || "Sucursal";
            obj.items=[];
            obj.callParent();            
            fnc.load.scripts(['widgets/selectCliente.js','widgets/selectSucursalByCliente.js'],function(){
                var selectCliente=obj.insert({xtype:'Desktop.Facturacion.Widgets.selectCliente',
                    name:obj.names.CO_CLIE,
                    defaultValues:obj.defaultValues.CO_CLIE,
                    changeValue:function(value){
                        sucursal.query("[name='"+obj.names.CO_SUCU+"']")[0].reset();
                        sucursal.query("[name='DE_SUCU']")[0].reset();
                    },
                    packageParent:obj.packageParent,                    
                    width:obj.width,
                });
                var sucursal=obj.insert({xtype:'Desktop.Facturacion.Widgets.selectSucursalByCliente',
                    name:obj.names.CO_SUCU,
                    label:obj.labels.SUCU,
                    defaultValues:obj.defaultValues.CO_SUCU,
                    getCO_CLIE:function(){
                        var c=selectCliente.query('[name="CO_CLIE"]');
                        if(c.length==1)
                            if (c[0].value!=null){
                                var v=Ext.String.trim(c[0].value);
                                if (v.length>0)
                                    return v;
                            }
                        return -1;
                    },
                    packageParent:obj.packageParent,
                    width:obj.width,
                });
            });            
            
        },
    }); 

})();