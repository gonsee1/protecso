Ext.onReady(function(){
    fnc.load.ModelsAndStores(["TIPO_DOCU"],function(){
        var package="Desktop.Facturacion.Panels.TIPO_DOCU.Insertar";
        var varSendPackage=settings.Permissions.varSendPackage;  
        Ext.define(package,{
            model:'Desktop.Facturacion.Models.TIPO_DOCU',
            extend:'Ext.form.Panel',
            alias:'widget.Desktop.Facturacion.Panels.TIPO_DOCU.Insertar',
            bodyPadding: 5,
            width: 350,
            layout: 'anchor',
            defaults: {
                anchor: '100%'
            },
            defaultType: 'textfield',
            items: [{
                fieldLabel: 'Nombre',
                name: 'NO_TIPO_DOCU',
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
                        var model=new Desktop.Facturacion.Models.TIPO_DOCU(form.getFieldValues());
                        model.save({params:fnc.getParamsSendPackage(model.getData(),varSendPackage,package)});
                        form.reset();
                    }
                }
            }]         
        });    
    });
});