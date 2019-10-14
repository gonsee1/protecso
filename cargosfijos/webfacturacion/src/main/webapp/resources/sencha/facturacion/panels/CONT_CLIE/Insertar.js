Ext.onReady(function(){
    fnc.load.ModelsAndStores(["TIPO_DOCU","CONT_CLIE"],function(){
        var package="Desktop.Facturacion.Panels.CONT_CLIE.Insertar";
        var varSendPackage=settings.Permissions.varSendPackage;
        var storeRef=new Desktop.Facturacion.Stores.TIPO_DOCU();
        fnc.addParameterStore(storeRef,varSendPackage,package);
        storeRef.load({
            callback:function(){
                Ext.define(package,{
                    model:'Desktop.Facturacion.Models.CONT_CLIE',
                    extend:'Ext.form.Panel',
                    alias:'widget.Desktop.Facturacion.Panels.CONT_CLIE.Insertar',
                    bodyPadding: 5,
                    width: 450,
                    autoScroll:true,
                    defaults: {
                        anchor: '100%'
                    },
                    defaultType: 'textfield',
                    initComponent:function(){
                        var obj=this;
                        this.items=[
                            {
                                xtype:'Desktop.Facturacion.Widgets.selectCliente',
                                packageParent:{package:package,varSendPackage:varSendPackage},  
                                //width:'200px',
                            },                            
                            {
                                fieldLabel: 'Nombre',
                                name: 'NO_CONT_CLIE',
                                allowBlank: true
                            },
                            {
                                fieldLabel: 'Apellido',
                                name: 'AP_CONT_CLIE',
                                allowBlank: true
                            },
                            {
                                xtype:'combobox',
                                fieldLabel:'Tipo Documento',
                                store:storeRef,
                                displayField: 'NO_TIPO_DOCU',
                                valueField: 'CO_TIPO_DOCU',
                                name: 'CO_TIPO_DOCU',
                                allowBlank: false,
                                editable:false,
                            },{
                                fieldLabel: 'Número Documento',
                                name: 'DE_NUME_DOCU',
                                allowBlank: false
                            },
                            {
                                fieldLabel: 'Email',
                                name: 'DE_CORR',
                                allowBlank: true,
                                vtype:'email',
                            },  
                            {
                                fieldLabel: 'Telefono',
                                name: 'DE_TELE',
                                allowBlank: true
                            }                              
                        ];
                        this.callParent();
                        var combTipoDocu=obj.query("[name='CO_TIPO_DOCU']")[0];
                        var txtNumeDocu=obj.query("[name='DE_NUME_DOCU']")[0];
                        combTipoDocu.on('change',function(combo,CO_TIPO_DOCU){
                            CO_TIPO_DOCU=fnc.parse.String.toInt(CO_TIPO_DOCU);
                            txtNumeDocu.setValue("");
                            if (CO_TIPO_DOCU === 1){
                                //RUC
                                txtNumeDocu.inputEl.dom.maxLength = 11;
                            } else if (CO_TIPO_DOCU === 2){
                                //DNI
                                txtNumeDocu.inputEl.dom.maxLength = 8;
                            } else if (CO_TIPO_DOCU === 3){
                                //CARNET EXTRANJERIA
                                txtNumeDocu.inputEl.dom.maxLength = 12;
                            } else if (CO_TIPO_DOCU === 4){
                                //PASAPORTE
                                txtNumeDocu.inputEl.dom.maxLength = 12;
                            }
                            
                        });
                    },
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
                                
                                var model=new Desktop.Facturacion.Models.CONT_CLIE(form.getFieldValues());
                                model.save({params:fnc.getParamsSendPackage(model.getData(),varSendPackage,package)});
                                form.reset();
                            }
                        }
                    }]         
                });                  
            }
        });
  
    });
});