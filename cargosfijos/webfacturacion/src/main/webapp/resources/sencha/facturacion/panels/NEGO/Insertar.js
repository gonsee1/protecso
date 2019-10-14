/* global fnc, Desktop, settings, Ext */

Ext.onReady(function(){
    fnc.load.scripts(['widgets/selectClienteAndSucursal.js',"widgets/comboBoxServicios.js"],function(){         
        fnc.load.ModelsAndStores(["TIPO_DOCU","TIPO_CLIE","CONT_CLIE","DIST","PROV","DEPA","TIPO_FACT","MONE_FACT","PERI_FACT","NEGO","CLIE","SUCU","NEGO","PROD","NEGO_CORR"],function(){
            var package="Desktop.Facturacion.Panels.NEGO.Insertar";
            var varSendPackage=settings.Permissions.varSendPackage;
            
            var storeTipo=new Desktop.Facturacion.Stores.TIPO_FACT();
            var storeMone=new Desktop.Facturacion.Stores.MONE_FACT();
            var storePeri=new Desktop.Facturacion.Stores.PERI_FACT();
            var storeProd=new Desktop.Facturacion.Stores.PROD();
            var storeClie=new Desktop.Facturacion.Stores.CLIE();
            var storeSucu=new Desktop.Facturacion.Stores.SUCU();
            
            fnc.addParameterStore(storeTipo,varSendPackage,package); 
            fnc.addParameterStore(storeMone,varSendPackage,package); 
            fnc.addParameterStore(storePeri,varSendPackage,package); 
            fnc.addParameterStore(storeProd,varSendPackage,package); 
            fnc.addParameterStore(storeClie,varSendPackage,package); 
            fnc.addParameterStore(storeSucu,varSendPackage,package);
            fnc.setApiProxyStore(storeSucu,{read:'selectByCLIE/'});
            fnc.addParameterStore(storeSucu,"CO_CLIE",-1);
            Ext.define(package,{
                model:'Desktop.Facturacion.Models.NEGO',
                extend:'Ext.form.Panel',
                alias:'widget.Desktop.Facturacion.Panels.NEGO.Insertar',
                defaultType: 'textfield',
                initComponent:function(){
                    var me=this;
                    
                    me.varsInitComponent=me.varsInitComponent || {};
                    
                    me.defaultValues = me.defaultValues || {};

                    if (me.defaultValues.CO_SUCU_CORR && me.defaultValues.CO_CLIE &&  me.defaultValues.CO_NEGO){
                        me.defaultValues={CO_SUCU:me.defaultValues.CO_SUCU_CORR,CO_CLIE:me.defaultValues.CO_CLIE, CO_NEGO:me.defaultValues.CO_NEGO};
                    }else{
                        me.defaultValues = {};
                    }

                	var arrayAux=[];
                    var arrayCorreos=Ext.create('Ext.data.Store',{
                        fields:[
                            {name:'CO_NEGO_CORR'},
                            {name:'CO_NEGO'},
                            {name:'DE_CORR'},
                            {name:'ST_ACTI'}
                        ],
                        data:arrayAux
                     });
                    
                    if (me.defaultValues.CO_NEGO) {
                    	var storeCorreosData=new Desktop.Facturacion.Stores.NEGO_CORR();
                        fnc.addParameterStore(storeCorreosData,varSendPackage,package); 
                        fnc.addParameterStore(storeCorreosData,"CO_NEGO", me.defaultValues.CO_NEGO);
                        fnc.setApiProxyStore(storeCorreosData,{read:'selectActivosByCO_NEGO/'});
                        storeCorreosData.load({ 
                            callback: function(records, operation, success) {
                                 if (success){
                                	 for (var i=0;i<records.length;i++) {
                                         var data=records[i].data;
                                         arrayAux.push({
                                        	 CO_NEGO_CORR:data.CO_NEGO_CORR,
                                        	 CO_NEGO:data.CO_NEGO,
                                        	 DE_CORR:data.DE_CORR,
                                        	 ST_ACTI:data.ST_ACTI
                                         });
                                	 }
                                	 arrayCorreos.loadData(arrayAux);
                                }
                            }            
                        });

                    }
                    
                    me.items=[
                        {
                            fieldLabel: 'Número Negocio',
                            name: 'CO_NEGO',
                            xtype: 'numberfield', 
                            allowExponential: false,
                            minValue: 0,
                            allowBlank: false
                        },
                        {
                            xtype:'Desktop.Facturacion.Widgets.comboBoxServicios',
                            packageParent:{package:package,varSend:varSendPackage},                            
                            //sufijoLabel:'Servicio',                            
                            //name:'CO_PROD',
                            CO_PROD:me.varsInitComponent.CO_PROD,
                        }
                        /*{
                            xtype:'combobox',
                            fieldLabel:'Producto',
                            store:storeProd,
                            displayField: 'NO_PROD',
                            valueField: 'CO_PROD',
                            name: 'CO_PROD',
                            allowBlank: false,
                            editable:false
                        }*/
                        ,{
                            xtype:'combobox',
                            fieldLabel:'Tipo Facturación',
                            store:storeTipo,
                            displayField: 'NO_TIPO_FACT',
                            valueField: 'CO_TIPO_FACT',
                            name: 'CO_TIPO_FACT',
                            allowBlank: false,
                            editable:false
                        },                        
                        {    
                            xtype:'textfield',
                            name:'REFERENCIA',
                            fieldLabel: 'Referencia',
                            valueField: 'REFERENCIA',
                            allowExponential: false,
                            allowBlank: false 
                        },
                        {
                            xtype:'Desktop.Facturacion.Widgets.selectClienteAndSucursal',
                            packageParent:{package:package,varSendPackage:varSendPackage},
                            names:{CO_CLIE:'CO_CLIE',CO_SUCU:'CO_SUCU_CORR'},
                            labels:{SUCU:'Sucursal Correspondencia'},
                            name:'combo_selectClienteAndSucursal',
                            defaultValues:me.defaultValues,
                        },
                        {
                            xtype:'combobox',
                            fieldLabel:'Moneda Facturación',
                            store:storeMone,
                            displayField: 'NO_MONE_FACT',
                            valueField: 'CO_MONE_FACT',
                            name: 'CO_MONE_FACT',
                            allowBlank: false,
                            editable:false
                        },{
                            xtype:'combobox',
                            fieldLabel:'Periodo Facturación',
                            store:storePeri,
                            displayField: 'NO_PERI_FACT',
                            valueField: 'CO_PERI_FACT',
                            name: 'CO_PERI_FACT',
                            allowBlank: false,
                            editable:false,
                        },{
                            fieldLabel: 'Número Negocio (Origen)',
                            name: 'CO_NEGO_ORIG',
                            xtype: 'numberfield', 
                            allowExponential: false,
                            minValue: 0,
                            allowBlank: true
                        },{
                            xtype:'grid',
                            name:'gridCorreos',
                           /* store:Ext.create('Ext.data.Store',{
                                fields:[{name:'DE_CORR'}],
                            }),*/

                            store:arrayCorreos,
                            columns:[
                                {text:'Correo',dataIndex:'DE_CORR',width:300},
                            ],

                            tbar:[
                                {
                                    text:'Agregar Correo',
                                    iconCls:fnc.icons.class.insert(),
                                    handler:function(){
                                        var grid=this.up('grid');
                                        if (grid){
                                            var storeCorr=grid.getStore();
                                            var winAddCorr=fnc.createWindowModal({
                                                title:'Agregar Correo',
                                                height:130,
                                                width:400,
                                                items:[
                                                    {
                                                        xtype:'form',
                                                        items:[
                                                            {
                                                                xtype:'textfield',
                                                                vtype: 'email',
                                                                fieldLabel:'Correo',
                                                                name:'DE_CORR',
                                                            }
                                                        ],
                                                        buttons:[
                                                            {
                                                                text:'Agregar',
                                                                handler:function(){
                                                                    var form=this.up('form');
                                                                    var vform=form.getForm();
                                                                    if (vform.isValid()){
                                                                        var data=vform.getFieldValues();
                                                                        storeCorr.add(data);
                                                                        winAddCorr.close();
                                                                    }
                                                                }
                                                            }
                                                        ]
                                                    }
                                                ]
                                            });
                                            winAddCorr.show();
                                        }
                                    }
                                },
                                {
                                    text:'Eliminar',
                                    handler:function(){
                                        var grid=this.up('grid');
                                        var storeCorr=grid.getStore();
                                        var selected=grid.getView().getSelectionModel().getSelection();
                                        if(selected.length==1){
                                            storeCorr.remove(selected);
                                        }
                                    }
                                }
                            ]
                        }
                    ];
                    
                    me.callParent();
                },                

                // Reset and Submit buttons
                buttonAlign:'left',
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
                        var grid=this.up('form').down('[name=gridCorreos]');
                        var form = this.up('form').getForm();
                        var store=grid.getStore();
                        if (form.isValid()) {
                            fnc.createMsgConfirm({msg:'¿Esta seguro de crear este negocio?',call:function(btn){
                                if (btn=='yes'){
                                    var model=new Desktop.Facturacion.Models.NEGO(form.getFieldValues());
                                    var params=fnc.getParamsSendPackage(model.getData(),varSendPackage,package);
                                    params.DE_CORRS=[];
                                    for(var i=0;i<store.data.items.length;i++){
                                        params.DE_CORRS.push(store.data.items[i].data.DE_CORR);
                                    }
//                                    model.save({params:params});
                                    
                                    model.save({
                                        params:params,
                                        success: function (record, operation) {
                                            store.removeAll();
                                            form.reset();
                                        },
                                        failure: function (record, operation) {
                                            fnc.responseEvaluateOperationError(operation);
                                        }
                                    }); 
                                    
                                }
                            }});
                        }
                    }
                }]         
            });
        });    
    });
});