Ext.onReady(function(){
    fnc.load.ModelsAndStores(["MODU","ITEM_MODU"],function(){
        var package="Desktop.Facturacion.Panels.ITEM_MODU.Insertar";
        var AJAX_URI=settings.AJAX_URI+'ITEM_MODU/';
        var objDesktop=null;
        var storeModulo=new Desktop.Facturacion.Stores.MODU();
        var varSendPackage=settings.Permissions.varSendPackage;
        fnc.addParameterStore(storeModulo,varSendPackage,package);    
        storeModulo.load();
        Ext.define(package,{
            model:'Desktop.Facturacion.Models.ITEM_MODU',
            extend:'Ext.form.Panel',
            alias:'widget.Desktop.Facturacion.Panels.ITEM_MODU.Insertar',
            bodyPadding: 5,
            width: 350,
            layout: 'anchor',
            defaults: {
                anchor: '100%'
            },
            defaultType: 'textfield',
            items: [{
                fieldLabel: 'Nombre',
                name: 'NO_ITEM_MODU',
                allowBlank: false
            },{
                fieldLabel: 'Paquete',
                name: 'DE_PACK',
                allowBlank: false
            },{
                xtype:'combobox',
                fieldLabel:'Modulo',
                store:storeModulo,
                displayField: 'NO_MODU',
                valueField: 'CO_MODU',
                name: 'CO_MODU',
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
                        var model=new Desktop.Facturacion.Models.ITEM_MODU(form.getFieldValues());
                        model.save({params:fnc.getParamsSendPackage(model.getData(),varSendPackage,package)});
                        form.reset();
                    }
                }
            }]         
        });    
    });
});