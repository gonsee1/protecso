Ext.onReady(function(){
    var package="Desktop.Facturacion.Panels.USUA.Insertar";
    var AJAX_URI=settings.AJAX_URI+'USUA/';
    var objDesktop=null;
    var storePerf=new Desktop.Facturacion.Stores.PERF();
    var varSendPackage=settings.Permissions.varSendPackage;
    fnc.addParameterStore(storePerf,varSendPackage,package);    
    storePerf.load();
    Ext.define(package,{
        model:'Desktop.Facturacion.Models.USUA',
        extend:'Ext.form.Panel',
        alias:'widget.Desktop.Facturacion.Panels.USUA.Insertar',
        bodyPadding: 5,
        width: 350,
        layout: 'anchor',
        stores:{
            perf:storePerf.load()
        },
        defaults: {
            anchor: '100%'
        },
        defaultType: 'textfield',
        items: [{
            xtype:'combobox',
            fieldLabel:'Perfil',
            store:storePerf,
            displayField: 'NO_PERF',
            valueField: 'CO_PERF',
            name: 'CO_PERF',
        },{
            fieldLabel: 'Nombre',
            name: 'NO_USUA',
            allowBlank: false
        },{
            fieldLabel: 'Apellido',
            name: 'AP_USUA',
            allowBlank: false
        },{
            fieldLabel: 'Correo',
            name: 'DE_CORR',
            allowBlank: false
        },{
            fieldLabel: 'Usuario',
            name: 'DE_USER',
            allowBlank: false
        },{
            fieldLabel: 'Clave',
            name: 'DE_PASS',
            allowBlank: true
        },],

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
                    var model=new Desktop.Facturacion.Models.USUA(form.getFieldValues());
                    model.save({params:fnc.getParamsSendPackage(model.getData(),varSendPackage,package)});
                    form.reset();
                }
            }
        }]         
    });    
});