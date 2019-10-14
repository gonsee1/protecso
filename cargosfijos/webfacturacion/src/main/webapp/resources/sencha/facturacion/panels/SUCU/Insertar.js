Ext.onReady(function(){
    fnc.load.ModelsAndStores(["CLIE","DIST","PROV","DEPA","SUCU"],function(){
        fnc.load.scripts(["widgets/comboBoxDistrito.js","widgets/selectCliente.js"],function(){
            var package="Desktop.Facturacion.Panels.SUCU.Insertar";
            var varSendPackage=settings.Permissions.varSendPackage;
            Ext.define(package,{
                model:'Desktop.Facturacion.Models.SUCU',
                extend:'Ext.form.Panel',
                alias:'widget.Desktop.Facturacion.Panels.SUCU.Insertar',
                bodyPadding: 5,
                layout: 'anchor',
                width:450,
                initComponent:function(){
                    var obj=this;
                    obj.varsInitComponent=obj.varsInitComponent || {};
                    for(var k in this.stores){
                        fnc.addParameterStore(obj.stores[k],varSendPackage,package);
                        obj.stores[k].load();
                    }
                    obj.items=[
                        {
                            xtype:'Desktop.Facturacion.Widgets.selectCliente',
                            packageParent:{package:package,varSendPackage:varSendPackage},  
                            //width:'200px',
                        },{
                            xtype:'Desktop.Facturacion.Widgets.comboBoxDistrito',
                            packageParent:{package:package,varSend:varSendPackage},
                            CO_DIST:obj.varsInitComponent.CO_DIST,
                        },{
                            fieldLabel: 'Dirección',
                            name: 'DE_DIRE',
                            allowBlank: false
                        },
                        {               
                            fieldLabel: 'Referencia',
                            name: 'DE_REF_DIRE',
                            allowBlank: true
                        }
                    ];
                    obj.callParent();  
                },
                defaultType: 'textfield',
                // Reset and Submit buttons
                buttons: [{
                    text: 'Borrar',
                    handler: function() {
                        this.up('form').getForm().reset();
                    }
                }, {
                    text: 'Agregar',
                    formBind: true, //only enabled once the form is valid
                    disabled: true,
                    handler: function() {
                        var form = this.up('form').getForm();
                        if (form.isValid()) {
                            var model=new Desktop.Facturacion.Models.SUCU(form.getFieldValues());
                            model.save({params:fnc.getParamsSendPackage(model.getData(),varSendPackage,package)});
                            form.reset();
                        }
                    }
                }]         
            });
        });
    });
});