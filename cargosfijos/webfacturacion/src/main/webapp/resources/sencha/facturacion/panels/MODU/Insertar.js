Ext.onReady(function(){
    fnc.load.ModelAndStore("MODU",function(){
        var package="Desktop.Facturacion.Panels.MODU.Insertar";
        var AJAX_URI=settings.AJAX_URI+'MODU/';
        var objDesktop=null;
        var storeModulo=new Desktop.Facturacion.Stores.MODU();
        var varSendPackage=settings.Permissions.varSendPackage;
        fnc.addParameterStore(storeModulo,varSendPackage,package);    
        Ext.define(package,{
            model:'Desktop.Facturacion.Models.MODU',
            store:storeModulo,
            extend:'Ext.form.Panel',
            alias:'widget.Desktop.Facturacion.Panels.MODU.Insertar',
            bodyPadding: 5,
            width: 350,
            layout: 'anchor',
            defaults: {
                anchor: '100%'
            },
            defaultType: 'textfield',
            items: [{
                fieldLabel: 'Nombre',
                name: 'NO_MODU',
                allowBlank: false
            },{
                fieldLabel: 'Modelo',
                name: 'DE_PACK',
                allowBlank: false
            },{
                xtype:'combobox',
                fieldLabel:'Icono',                
                name:'DE_ICON_CLSS',
                editable:false,
                displayField:'NO_ICON',
                valueField:'CO_ICON',                
                tpl: '<tpl for="."><div class="x-boundlist-item" ><img src="{URI_ICON}" width="20px"/> {NO_ICON}</div></tpl>',
                store:new Ext.data.Store({
                    fields:['CO_ICON','NO_ICON','URI_ICON'],
                    data:(function(){
                        var dict=settings.real.icons.getAll();
                        var arr=[];
                        for(var i in dict){
                            var d=dict[i];
                            arr.push({
                                URI_ICON:d.uri,NO_ICON:d.name,CO_ICON:d.cssName
                            });
                        }
                        return arr;
                    })()
                }),
                listeners: {
                    select: function (comboBox, records) {
                        var record = records[0];
                        comboBox.inputEl.setStyle({
                            'background-image':    'url(' + record.get('URI_ICON') + ')',
                            'background-repeat':   'no-repeat',
                            'background-position': '3px center',
                            'background-size': '16px',
                            'padding-left':        '25px'
                        });
                    }
                }                
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
                        var model=new Desktop.Facturacion.Models.MODU(form.getFieldValues());
                        model.save({params:fnc.getParamsSendPackage(model.getData(),varSendPackage,package)});
                        form.reset();
                    }
                }
            }]         
        });    
    });
});