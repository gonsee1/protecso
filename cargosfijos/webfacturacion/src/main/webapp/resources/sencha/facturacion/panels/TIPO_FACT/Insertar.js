Ext.onReady(function(){
    fnc.load.ModelsAndStores(["TIPO_FACT"],function(){
        var package="Desktop.Facturacion.Panels.TIPO_FACT.Insertar";
        var varSendPackage=settings.Permissions.varSendPackage;  
        Ext.define(package,{
            model:'Desktop.Facturacion.Models.TIPO_FACT',
            extend:'Ext.form.Panel',
            alias:'widget.Desktop.Facturacion.Panels.TIPO_FACT.Insertar',
            bodyPadding: 5,
            width: 350,
            layout: 'anchor',
            defaults: {
                anchor: '100%'
            },
            defaultType: 'textfield',
            items: [{
                fieldLabel: 'Nombre',
                name: 'NO_TIPO_FACT',
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
                        var model=new Desktop.Facturacion.Models.TIPO_FACT(form.getFieldValues());
                        model.save({params:fnc.getParamsSendPackage(model.getData(),varSendPackage,package)});
                        form.reset();
                    }
                }
            }]         
        });    
    });
});