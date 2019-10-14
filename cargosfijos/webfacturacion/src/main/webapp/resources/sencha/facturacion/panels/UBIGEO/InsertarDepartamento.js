Ext.onReady(function(){
    fnc.load.ModelsAndStores(["DEPA"],function(){
        var package="Desktop.Facturacion.Panels.UBIGEO.InsertarDepartamento";
        var varSendPackage=settings.Permissions.varSendPackage;  
        Ext.define(package,{
            model:'Desktop.Facturacion.Models.DEPA',
            extend:'Ext.form.Panel',
            alias:'widget.Desktop.Facturacion.Panels.UBIGEO.InsertarDepartamento',
            defaultType: 'textfield',
            items: [{
                fieldLabel: 'Nombre',
                name: 'NO_DEPA',
                allowBlank: false
            }],

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
                        var model=new Desktop.Facturacion.Models.DEPA(form.getFieldValues());
                        model.save({
                            params:fnc.getParamsSendPackage(model.getData(),varSendPackage,package),
                            success: function (record, operation) {
                                fnc.createMsgBoxSuccess({msg:'Se registro con éxito el departamento.'});
                                form.reset();
                            },
                            failure: function (record, operation) {
                                fnc.responseEvaluateOperationError(operation);
                            }
                        });
                    }
                }
            }]         
        });    
    });
});