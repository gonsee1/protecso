Ext.onReady(function(){
    fnc.load.ModelsAndStores(["TIPO_FACT","MONE_FACT","PERI_FACT","PROD","SERV_SUPL"],function(){
        fnc.load.scripts(["widgets/comboBoxServicios.js"],function(){        
        var package="Desktop.Facturacion.Panels.SERV_SUPL.Insertar";
        var varSendPackage=settings.Permissions.varSendPackage;
        var storeProd=new Desktop.Facturacion.Stores.PROD();
        var storeMoneFact=new Desktop.Facturacion.Stores.MONE_FACT();
        fnc.addParameterStore(storeProd,varSendPackage,package); 
        fnc.addParameterStore(storeMoneFact,varSendPackage,package); 
        Ext.define(package,{
            model:'Desktop.Facturacion.Models.SERV_SUPL',
            extend:'Ext.form.Panel',
            alias:'widget.Desktop.Facturacion.Panels.SERV_SUPL.Insertar',
            bodyPadding: 5,
            width: 350,
            layout: 'anchor',
            defaults: {
                anchor: '100%'
            },         
            initComponent:function(){
                    var obj=this;
                    obj.varsInitComponent=obj.varsInitComponent || {};
                     
                      for(var k in this.stores){
                        fnc.addParameterStore(obj.stores[k],varSendPackage,package);
                        obj.stores[k].load();
                    }
            obj.items = [{                
                fieldLabel: 'Nombre',
                name: 'NO_SERV_SUPL',
                allowBlank: false
            },{
                xtype:'combobox',
                fieldLabel:'Moneda',
                store:storeMoneFact,
                displayField: 'NO_MONE_FACT',
                valueField: 'CO_MONE_FACT',
                name: 'CO_MONE_FACT',
                allowBlank: false,
                editable:false
            },{
                fieldLabel: 'Monto',
                name: 'IM_MONTO',
                xtype:'numberfield',
                allowExponential: false,
                minValue: 0,
                allowBlank: false
            },
            {
                xtype:'Desktop.Facturacion.Widgets.comboBoxServicios',
                packageParent:{package:package,varSend:varSendPackage},               
                CO_PROD:obj.varsInitComponent.CO_PROD,
            }
            /*{
                xtype:'combobox',
                fieldLabel:'Producto',
                store:storeProd,
                displayField: 'NO_PROD',
                valueField: 'CO_PROD',
                name: 'CO_PROD',
                allowBlank: false,
                editable:false
            },*/
            /*{
                xtype:'checkboxfield',
                fieldLabel: 'Afecto a detracción',
                name: 'ST_AFEC_DETR',
                allowBlank: false                
            }*/];
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
                        var model=new Desktop.Facturacion.Models.SERV_SUPL(form.getFieldValues());
                        model.save({params:fnc.getParamsSendPackage(model.getData(),varSendPackage,package)});
                        form.reset();
                    }
                }
            }]         
        });
       });
    });
});