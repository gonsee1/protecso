Ext.onReady(function(){
    fnc.load.ModelsAndStores(["PERF"],function(){
        var package="Desktop.Facturacion.Panels.PERF.Insertar";
        var AJAX_URI=settings.AJAX_URI+'PERF/';
        var objDesktop=null;
        var storePerf=new Desktop.Facturacion.Stores.PERF();
        var varSendPackage=settings.Permissions.varSendPackage;
        fnc.addParameterStore(storePerf,varSendPackage,package);    
        Ext.define(package,{
            model:'Desktop.Facturacion.Models.PERF',
            store:storePerf,
            extend:'Ext.form.Panel',
            alias:'widget.Desktop.Facturacion.Panels.PERF.Insertar',
            bodyPadding: 5,
            width: 350,
            layout: 'anchor',
            defaults: {
                anchor: '100%'
            },
            defaultType: 'textfield',
            items: [{
                fieldLabel: 'Nombre',
                name: 'NO_PERF',
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
                        var model=new Desktop.Facturacion.Models.PERF(form.getFieldValues());
                        model.save({params:fnc.getParamsSendPackage(model.getData(),varSendPackage,package)});
                        form.reset();
                    }
                }
            }]         
        });    
    });
});