Ext.onReady(function(){
    fnc.load.ModelsAndStores(["TIPO_FACT","MONE_FACT","PERI_FACT","PROD","PROD_PADRE"],function(){
        var package="Desktop.Facturacion.Panels.PROD.Insertar";
        var varSendPackage=settings.Permissions.varSendPackage;
        var storeTipo=new Desktop.Facturacion.Stores.TIPO_FACT();
        var storeMone=new Desktop.Facturacion.Stores.MONE_FACT();
        var storePeri=new Desktop.Facturacion.Stores.PERI_FACT();
        var storeProd_Padre= new Desktop.Facturacion.Stores.PROD_PADRE();       
        fnc.addParameterStore(storeProd_Padre,varSendPackage,package); 
        fnc.addParameterStore(storeTipo,varSendPackage,package); 
        fnc.addParameterStore(storeMone,varSendPackage,package); 
        fnc.addParameterStore(storePeri,varSendPackage,package); 
        Ext.define(package,{
            model:'Desktop.Facturacion.Models.PROD',
            extend:'Ext.form.Panel',
            alias:'widget.Desktop.Facturacion.Panels.PROD.Insertar',
            bodyPadding: 5,
            width: 350,
            layout: 'anchor',
            defaults: {
                anchor: '100%'
            },
            defaultType: 'textfield',
            items: [{
                fieldLabel: 'Nombre',
                name: 'NO_PROD',
                allowBlank: true
            },
            {
            	xtype:'combobox',
            	fieldLabel:'Producto',
            	name:'COD_PROD_PADRE',
            	displayField:'DESC_PROD_PADRE',
            	valueField:'COD_PROD_PADRE',
            	store:storeProd_Padre,
            	allowBlank:false,                
            },{
                xtype:'combobox',
                fieldLabel:'Tipo Facturación',
                store:storeTipo,
                displayField: 'NO_TIPO_FACT',
                valueField: 'CO_TIPO_FACT',
                name: 'CO_TIPO_FACT',
                allowBlank: false
            },{
                xtype:'combobox',
                fieldLabel:'Moneda Facturación',
                store:storeMone,
                displayField: 'NO_MONE_FACT',
                valueField: 'CO_MONE_FACT',
                name: 'CO_MONE_FACT',
                allowBlank: false
            },{
                xtype:'combobox',
                fieldLabel:'Periodo Facturación',
                store:storePeri,
                displayField: 'NO_PERI_FACT',
                valueField: 'CO_PERI_FACT',
                name: 'CO_PERI_FACT',
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
                        var model=new Desktop.Facturacion.Models.PROD(form.getFieldValues());
                        model.save({params:fnc.getParamsSendPackage(model.getData(),varSendPackage,package)});
                        form.reset();
                    }
                }
            }]         
        });
    });
});