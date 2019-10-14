Ext.onReady(function(){
    fnc.load.ModelsAndStores(["CONT_CLIE","TIPO_CLIE","TIPO_DOCU","SUCU","CLIE"],function(){
        var package="Desktop.Facturacion.Panels.CLIE.Insertar";
        var varSendPackage=settings.Permissions.varSendPackage;
        var storeRef=new Desktop.Facturacion.Stores.TIPO_DOCU();
        var storeTipo_Clie=new Desktop.Facturacion.Stores.TIPO_CLIE();
        var storeSucu=Ext.create('Desktop.Facturacion.Stores.SUCU',{autoLoad:false});
        var storeCont_Clie=Ext.create('Desktop.Facturacion.Stores.CONT_CLIE',{autoLoad:false});
        var storeTipo_Docu=new Desktop.Facturacion.Stores.TIPO_DOCU();
        fnc.addParameterStore(storeTipo_Docu,varSendPackage,package);
        
        fnc.addParameterStore(storeRef,varSendPackage,package);
        fnc.addParameterStore(storeSucu,varSendPackage,package);
        fnc.addParameterStore(storeTipo_Clie,varSendPackage,package);
        fnc.addParameterStore(storeCont_Clie,varSendPackage,package);        
        fnc.setApiProxyStore(storeSucu,{read:'selectByCLIE/'});
        fnc.setApiProxyStore(storeCont_Clie,{read:'selectByCLIE/'});
        storeRef.load({
            callback:function(){
                Ext.define(package,{
                    model:'Desktop.Facturacion.Models.CLIE',
                    extend:'Ext.form.Panel',
                    alias:'widget.Desktop.Facturacion.Panels.CLIE.Insertar',
                    bodyPadding: 5,
                    width: 450,
                    autoScroll:true,
                    defaults: {
                        anchor: '100%'
                    },
                    defaultType: 'textfield',
                    initComponent:function(){
                        var obj=this;
                        
                        var field_razon_social=Ext.create('Ext.form.field.Text',{
                                fieldLabel: 'Razón Social',
                                name: 'NO_RAZO',
                                allowBlank: true,
                        });                        
                        var field_nombre=Ext.create('Ext.form.field.Text',{
                            fieldLabel: 'Nombre',
                            name: 'NO_CLIE',
                            allowBlank: true,
                        });                                                
                        var field_apellido=Ext.create('Ext.form.field.Text',{
                            fieldLabel: 'Apellido',
                            name: 'AP_CLIE',
                            allowBlank: true,
                        });
                        
                        field_razon_social.setVisible(false);
                        field_nombre.setVisible(false);
                        field_apellido.setVisible(false);
                        obj.esEdicion= (typeof obj.esEdicion =='undefined' && ! obj.esEdicion?false:true);
                        this.items=[
                            {
                                fieldLabel: 'Código BUN',
                                name: 'DE_CODI_BUM',
                                allowBlank: false
                            },
                            {
                                fieldLabel: 'Digito Verificador',
                                name: 'DE_DIGI_BUM',
                                allowBlank: false
                            },                             
                            {
                                xtype:'combobox',
                                fieldLabel:'Tipo Cliente',
                                store:storeTipo_Clie,
                                displayField: 'NO_TIPO_CLIE',
                                valueField: 'CO_TIPO_CLIE',
                                name: 'CO_TIPO_CLIE',
                                allowBlank: false,
                                editable:false,
                                listeners:{
                                    select:function( combo, record, eOpts ){
                                        var data=record[0].data;
                                        if (data.NO_TIPO_CLIE.toLowerCase()=='natural'){
                                            field_razon_social.setVisible(false);
                                            field_nombre.setVisible(true);
                                            field_apellido.setVisible(true);
                                            
                                            field_razon_social.allowBlank = true;
                                            field_nombre.allowBlank = false;
                                            field_apellido.allowBlank = false;
                                        }else{
                                            field_razon_social.setVisible(true);
                                            field_nombre.setVisible(false);
                                            field_apellido.setVisible(false);
                                            
                                            field_razon_social.allowBlank = false;
                                            field_nombre.allowBlank = true;
                                            field_apellido.allowBlank = true;
                                        }
                                    }
                                }
                            },                            
                            field_razon_social,
                            field_nombre,
                            field_apellido,
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
                                fieldLabel: 'Ejecutivo',
                                name: 'DE_EJEC',
                                allowBlank: true
                            },  
                            {
                                fieldLabel: 'Sub Gerente',
                                name: 'DE_SUB_GERE',
                                allowBlank: true
                            },   
                            {
                                fieldLabel: 'Segmentación',
                                name: 'DE_SEGM',
                                allowBlank: true
                            },                              
                        ];
                        if (!this.esEdicion){
                            this.items.push(                                  
                                {
                                    xtype:'Desktop.Facturacion.Widgets.comboBoxDistrito',
                                    packageParent:{package:package,varSend:varSendPackage},
                                    title:'Distrito de Fiscal',
                                    sufijoLabel:'Fiscal',
                                    sufijoName:'_FISC',
                                    name:'CO_DIST_FISC',
                                }                                  
                            );
                            this.items.push(
                                {               
                                    fieldLabel: 'Dirección Fiscal',
                                    name: 'DE_DIRE_FISC',
                                    allowBlank: false
                                }                                   
                            );
                            this.items.push({
                                        xtype:'fieldset',
                                        title:'Representante Legal',
                                        items:[
                                            {
                                                xtype:'combobox',
                                                fieldLabel:'Tipo Documento',
                                                store:storeRef,
                                                displayField: 'NO_TIPO_DOCU',
                                                valueField: 'CO_TIPO_DOCU',
                                                name: 'CO_TIPO_DOCU_CONT_CLIE',
                                                allowBlank: false,
                                                editable:false,
                                            },                                      
                                            {
                                                xtype:'textfield',
                                                fieldLabel: 'Número Documento',
                                                name: 'DE_NUME_DOCU_CONT_CLIE',
                                                allowBlank: false
                                            },                                      
                                            {
                                                xtype:'textfield',
                                                fieldLabel: 'Nombre',
                                                name: 'NO_CONT_CLIE', 
                                                allowBlank: false
                                            },                                      
                                            {
                                                xtype:'textfield',
                                                fieldLabel: 'Apellido',
                                                name: 'AP_CONT_CLIE',
                                                allowBlank: false
                                            },                                      
                                            {
                                                xtype:'textfield',
                                                vtype: 'email',
                                                fieldLabel: 'Email',
                                                name: 'DE_CORR',                                        
                                            },                                      
                                            {
                                                xtype:'textfield',
                                                fieldLabel: 'Telefono',
                                                name: 'DE_TELE',                                        
                                            }
                                        ]
                                  
                            });
                            //fnc.load.scripts(["panels/CONT_CLIE/Insertar.js"]);
                            var storeContactos=Ext.create('Ext.data.Store',{
                                fields:[
                                    {name:'NO_CONT_CLIE'},
                                    {name:'AP_CONT_CLIE'},
                                    {name:'CO_TIPO_DOCU'},
                                    {name:'DE_NUME_DOCU'},
                                    {name:'DE_CORR'},
                                    {name:'DE_TELE'},
                                ]
                             });
                            var dict_tipo_documento={};
                            for(var i=0;i<storeTipo_Docu.data.items.length;i++){
                                var reg_ele=storeTipo_Docu.data.items[i].data;
                                dict_tipo_documento[reg_ele.CO_TIPO_DOCU]=reg_ele.NO_TIPO_DOCU;
                            }
                            this.items.push({
                                name:'gridContactos',
                                title:'Contactos',
                                xtype:'grid',
                                store:storeContactos,
                                columns:[
                                    {text:'Nombre',dataIndex:'NO_CONT_CLIE'},
                                    {text:'Apellido',dataIndex:'AP_CONT_CLIE'},
                                    {text:'Tipo Documento',renderer:function(a,b,c){return dict_tipo_documento[c.data.CO_TIPO_DOCU];}},
                                    {text:'Número Documento',dataIndex:'DE_NUME_DOCU'},
                                    {text:'Correo',dataIndex:'DE_CORR'},
                                    {text:'Telefono',dataIndex:'DE_TELE'},                                    
                                ],
                                tbar:[
                                    {
                                        text:'Agregar Contacto',
                                        iconCls:fnc.icons.class.insert() ,
                                        handler:function(){
                                            var panelForm=Ext.create('Ext.form.Panel',{
                                                defaultType: 'textfield',
                                                items:[
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
                                                        store:storeTipo_Docu,
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
                                                ],
                                                buttons: [
                                                    {
                                                        text: 'Agregar',
                                                        formBind: true, //only enabled once the form is valid
                                                        disabled: true,
                                                        handler: function() {
                                                            var form = this.up('form').getForm();
                                                            if (form.isValid()) {
                                                                var data=form.getFieldValues();
                                                                window.close();
                                                                storeContactos.add(data);
                                                            }
                                                        }
                                                    }
                                                ]                                                
                                            });
                                            var window=fnc.createWindowModal({
                                                width:450,
                                                height:450,
                                                title:'Agregar Contacto',
                                                items:[panelForm]
                                            });
                                            window.show();
                                        }
                                    },
                                    {
                                        text:'Eliminar Contacto',
                                        iconCls:fnc.icons.class.delete() ,
                                        handler:function(){
                                            var grid=this.up('grid');
                                            var selected=grid.getView().getSelectionModel().getSelection();
                                            if(selected.length==1){ 
                                                storeContactos.remove(selected);
                                            }                                           
                                        }
                                    }
                                ]
                            });                            
                        }else{
                            fnc.addParameterStore(storeSucu,"CO_CLIE",obj.CLIE.CO_CLIE);
                            fnc.addParameterStore(storeCont_Clie,"CO_CLIE",obj.CLIE.CO_CLIE);
                            storeSucu.load();
                            storeCont_Clie.load();
                            var combobox=Ext.create({
                                    xtype:'combobox',
                                    fieldLabel: 'Seleccione sucursal fiscal',
                                    editable:false,
                                    name:'CO_SUCU_FISC',
                                    store:storeSucu,
                                    displayField:'DE_DIRE',
                                    valueField:'CO_SUCU', 
                                });
                            this.items.push(combobox);
                            
                            
                            var combobox=Ext.create({
                                    xtype:'fieldcontainer',
                                    layout: {
                                        type: 'hbox',
                                        defaultMargins: {top: 0, right: 5, bottom: 0, left: 0}
                                    },                                  
                                    items:[
                                        {
                                            xtype:'combobox',
                                            fieldLabel: 'Representante Legal',
                                            editable:false,
                                            name:'CO_CONT_CLIE_REPR_LEGA',
                                            store:storeCont_Clie,
                                            displayField:'NO_CONT_CLIE',
                                            valueField:'CO_CONT_CLIE',
                                        },
                                        {
                                            xtype:'button',
                                            text:'Agregar',
                                            style:{
                                                paddingRight:'10px',
                                            },
                                            handler:function(){
                                                fnc.load.panels([
                                                    ["CONT_CLIE","Insertar"]
                                                ],function(){
                                                    var panelForm=Ext.create('Desktop.Facturacion.Panels.CONT_CLIE.Insertar',{
                                                        buttons: [
                                                            {
                                                                text: 'Guardar',
                                                                formBind: true, //only enabled once the form is valid
                                                                disabled: true,
                                                                handler: function() {
                                                                    var form = this.up('form').getForm();
                                                                    if (form.isValid()) {
                                                                        var model=new Desktop.Facturacion.Models.CONT_CLIE(form.getFieldValues());
                                                                        model.save({params:fnc.getParamsSendPackage(model.getData(),varSendPackage,package)});
                                                                        form.reset();
                                                                        window.close();
                                                                        storeCont_Clie.load();
                                                                    }
                                                                }
                                                        },{
                                                            text: 'Cancelar',
                                                            handler: function() {
                                                                window.close();
                                                            }
                                                        }] 
                                                    });
                                                    panelForm.query('textfield[name=CO_CLIE]')[0].setValue(obj.CLIE.CO_CLIE);
                                                    panelForm.query('textarea[name=DE_CLIE]')[0].setValue(obj.CLIE.NO_RAZO);
                                                    panelForm.query("textarea[name=DE_CLIE]")[0].disable();  
                                                    panelForm.query("[name=btnSeleccionarCliente]")[0].disable();                                                    
                                                    var window=fnc.createWindowModal({
                                                        title:'Agregar Contacto',
                                                        width:450,
                                                        height:450,
                                                        items:[panelForm]
                                                    });
                                                    window.show();                                                    
                                                });

                                            }
                                        }
                                    ]
                                });
                            this.items.push(combobox);
                        }
                        this.callParent();
                        var combTipoDocu=obj.query("[name='CO_TIPO_DOCU']")[0];
                        var txtNumeDocu=obj.query("[name='DE_NUME_DOCU']")[0];
                        combTipoDocu.on('change',function(combo,CO_TIPO_DOCU){
                            CO_TIPO_DOCU=fnc.parse.String.toInt(CO_TIPO_DOCU);
                            if (txtNumeDocu && txtNumeDocu.inputEl && txtNumeDocu.inputEl.dom){
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
                        //formBind: true, //only enabled once the form is valid
                        //disabled: true,
                        handler: function() {
                            var gridContactos=this.up('form').down('[name=gridContactos]');
                            var storeContactos=gridContactos.getStore();
                            var form = this.up('form').getForm();
                            if (form.isValid()) {
                                var model=new Desktop.Facturacion.Models.CLIE(form.getFieldValues());
                                var data=model.getData();                                
                                data.CONT_NO_CONT_CLIE=[];
                                data.CONT_AP_CONT_CLIE=[];
                                data.CONT_CO_TIPO_DOCU=[];
                                data.CONT_DE_NUME_DOCU=[];
                                data.CONT_DE_CORR=[];
                                data.CONT_DE_TELE=[];                             
                                for(var i=0;i<storeContactos.data.items.length;i++){
                                    var r=storeContactos.data.items[i].data;
                                    data.CONT_NO_CONT_CLIE.push(r.NO_CONT_CLIE);
                                    data.CONT_AP_CONT_CLIE.push(r.AP_CONT_CLIE);
                                    data.CONT_CO_TIPO_DOCU.push(r.CO_TIPO_DOCU);
                                    data.CONT_DE_NUME_DOCU.push(r.DE_NUME_DOCU);
                                    data.CONT_DE_CORR.push(r.DE_CORR);
                                    data.CONT_DE_TELE.push(r.DE_TELE);
                                }
                                model.save({params:fnc.getParamsSendPackage(data,varSendPackage,package),success:function(){
                                    fnc.createMsgBoxSuccess({msg:'Se registro con exito el cliente.'});
                                }});
                                form.reset();
                                storeContactos.removeAll();
                            }
                        }
                    }]         
                });                  
            }
        });
  
    });
});