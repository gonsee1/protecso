Ext.onReady(function(){
    fnc.load.ModelsAndStores(["DEPA","PROV"],function(){
        var package="Desktop.Facturacion.Panels.UBIGEO.InsertarProvincia";
        var varSendPackage=settings.Permissions.varSendPackage;
        var storeRef=new Desktop.Facturacion.Stores.DEPA();
        fnc.addParameterStore(storeRef,varSendPackage,package); 
        storeRef.load();
        Ext.define(package,{
            model:'Desktop.Facturacion.Models.PROV',
            extend:'Ext.form.Panel',
            alias:'widget.Desktop.Facturacion.Panels.UBIGEO.InsertarProvincia',
            defaultType: 'textfield',
            items: [
            {
                xtype:'combobox',
                fieldLabel:'Departamento',
                store:storeRef,
                displayField: 'NO_DEPA',
                valueField: 'CO_DEPA',
                name: 'CO_DEPA',
            },                
            {
                fieldLabel: 'Nombre',
                name: 'NO_PROV',
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
                        var model=new Desktop.Facturacion.Models.PROV(form.getFieldValues());
                        model.save({
                            params:fnc.getParamsSendPackage(model.getData(),varSendPackage,package),
                            success: function (record, operation) {
                                fnc.createMsgBoxSuccess({msg:'Se registro con éxito la provincia.'});
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