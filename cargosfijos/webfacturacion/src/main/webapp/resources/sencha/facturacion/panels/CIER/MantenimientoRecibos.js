Ext.onReady(function(){
    fnc.load.scripts(["widgets/filterPagination.js","widgets/comboBoxDistrito.js"],function(){
    fnc.load.ModelsAndStores(["PROD","CIER","CLIE","RECI","TIPO_DOCU","MONE_FACT","RECI_GLOS","NEGO_SUCU","TIPO_GLOS"],function(){
        var package="Desktop.Facturacion.Panels.CIER.MantenimientoRecibos";
        var varSendPackage=settings.Permissions.varSendPackage;
        var storeRef=new Desktop.Facturacion.Stores.RECI();
        var storeTipoDocu=new Desktop.Facturacion.Stores.TIPO_DOCU();
        var storeMoneFact=new Desktop.Facturacion.Stores.MONE_FACT(); 
        var settingsLocal=window.settings.real.get();
        fnc.addParameterStore(storeRef,varSendPackage,package);
        fnc.addParameterStore(storeTipoDocu,varSendPackage,package);
        fnc.addParameterStore(storeMoneFact,varSendPackage,package);
        fnc.setApiProxyStore(storeRef,{read:'findRecibo/'}); 
        storeRef.load();
        storeTipoDocu.load({
            callback:function(){
                Ext.define(package,{
//                    extend:'Ext.panel.Panel',
                    extend:'Ext.grid.Panel',
                    alias:'widget.Desktop.Facturacion.Panels.CIER.MantenimientoRecibos',
//                    enableLocking: true,
//                    bodyPadding: 5,
//                    width: 1100,
//                    style:{
//                        fontSize:'8px',
//                    },
                    store:storeRef,
                    columns: [
                        {text:'Número',dataIndex:'CO_RECI',width:125},
                        {text:'Negocio',dataIndex:'CO_NEGO',width:157},
                        {text:'Cliente',dataIndex:'NO_CLIE',width:508},
                        {text:'Emitida',dataIndex:'FE_EMIS',xtype: 'datecolumn', format: 'd/m/Y',width:100},
                        {text:'Vence',dataIndex:'FE_VENC',xtype: 'datecolumn', format: 'd/m/Y',width:100},
                        {text:'Total',dataIndex:'IM_TOTA',width:100},
                        {text:'Anulada',dataIndex:'ST_ANUL',xtype: 'booleancolumn',trueText: 'Si',falseText: 'No',width:100},
                        {text: 'Cierre',renderer:function(a,b,c){return "%02d".format(c.data.CIER.NU_PERI)+" - "+c.data.CIER.NU_ANO;},width:100},
                    ],
                    tbar:[
                        /*{
                            xtype:'combobox',
                            fieldLabel: 'Periodo',
                            store:Ext.create("Ext.data.Store",{
                                fields:[{name:'NU_PERI'},{name:'NO_PERI'},],
                                data:[
                                    {NO_PERI:'Enero',NU_PERI:1},{NO_PERI:'Febrero',NU_PERI:2},{NO_PERI:'Marzo',NU_PERI:3},
                                    {NO_PERI:'Abril',NU_PERI:4},{NO_PERI:'Mayo',NU_PERI:5},{NO_PERI:'Junio',NU_PERI:6},
                                    {NO_PERI:'Julio',NU_PERI:8},{NO_PERI:'Agosto',NU_PERI:8},{NO_PERI:'Septiembre',NU_PERI:9},
                                    {NO_PERI:'Octubre',NU_PERI:10},{NO_PERI:'Noviembre',NU_PERI:11},{NO_PERI:'Diciembre',NU_PERI:12},
                                ],
                            }),
                            displayField: 'NO_PERI',
                            valueField: 'NU_PERI',
                            editable:false,
                            name:'combPeriodo',
                        },
                        {
                            xtype:'numberfield',
                            fieldLabel: 'Anio',
                            name:'txtAno',
                        }, 
                        {
                            xtype:'textfield', 
                            emptyText:'Buscar',
                            name:'txtBuscar',
                        },
                        {
                            xtype:'button',
                            text:'Buscar',
                            cls:'fac-btn fac-btn-primary btn-size-tiny',
                            handler:function(){
                                var tool=this.up();
                                var comboPeriodo=tool.query('[name=combPeriodo]')[0];
                                var txtAno=tool.query('[name=txtAno]')[0];
                                var txtBuscar=tool.query('[name=txtBuscar]')[0];                        
                                fnc.addParameterStore(storeRef,"CO_PERI",comboPeriodo.getValue());
                                fnc.addParameterStore(storeRef,"CO_ANO",txtAno.getValue());
                                fnc.addParameterStore(storeRef,"DE_BUSC",txtBuscar.getValue());
                                storeRef.load();
                            }
                        },*/
                        {
                            xtype: 'button',
                            text: "Actualizar",
                            tooltip: "Actualizar",
                            iconCls: fnc.icons.class.actualizar(),
                            handler: function() {
                                storeRef.load();
                            }	
                        }, "-",
                        {
                            xtype: 'button',
                            text: "Editar",
                            tooltip: "Editar",
                            iconCls: "fac_icon_edit",
                            handler:function(){
                                var grid=this.up('grid');
                                var selected=grid.getView().getSelectionModel().getSelection();
                                if(selected.length==1){
                                    var data=selected[0].data;
                                    var storeGlosas=new Desktop.Facturacion.Stores.RECI_GLOS();
                                    var storeNegoSucus=new Desktop.Facturacion.Stores.NEGO_SUCU();
                                    var storeTipoGlos=new Desktop.Facturacion.Stores.TIPO_GLOS();
                                    
                                    fnc.addParameterStore(storeGlosas,varSendPackage,package);
                                    fnc.addParameterStore(storeNegoSucus,varSendPackage,package);
                                    fnc.addParameterStore(storeTipoGlos,varSendPackage,package);
                                    fnc.setApiProxyStore(storeGlosas,{read:'getByRECI/'});
                                    fnc.setApiProxyStore(storeNegoSucus,{read:'getByRECI/'});
                                    console.log(data);
                                    storeNegoSucus.load({
                                        params:data,
                                        callback:function(){
                                            storeGlosas.load({
                                                params:data,
                                                callback:function(){
                                                    var win=fnc.createWindow({
                                                       title:'Editando recibo '+data.CO_RECI,
                                                       width:850,
                                                       items:[
                                                           {
                                                               xtype:'form',
                                                               defaults: {
                                                                anchor: '100%'
                                                               },                                                               
                                                               items:[
                                                                   {
                                                                       name:'DE_PERI',
                                                                       fieldLabel: 'Periodo',
                                                                       xtype:'textfield',
                                                                       value:data.DE_PERI,
                                                                   },                                                           
                                                                   {
                                                                       name:'FE_EMIS',
                                                                       fieldLabel: 'Emitio',
                                                                       xtype:'datefield',
                                                                       value:data.FE_EMIS,
                                                                   },
                                                                   {
                                                                       name:'FE_VENC',
                                                                       fieldLabel: 'Vencimiento',
                                                                       xtype:'datefield',
                                                                       value:data.FE_VENC,
                                                                   },
                                                                   {
                                                                       name:'DE_CODI_BUM',
                                                                       fieldLabel: 'Codigo BUM',
                                                                       xtype:'textfield',
                                                                       value:data.DE_CODI_BUM,
                                                                   },
                                                                   {
                                                                       name:'NO_CLIE',
                                                                       fieldLabel: 'Cliente',
                                                                       xtype:'textfield',
                                                                       value:data.NO_CLIE,
                                                                   },
                                                                   {
                                                                        xtype:'Desktop.Facturacion.Widgets.comboBoxDistrito',
                                                                        packageParent:{package:package,varSend:varSendPackage},
                                                                        title:'Seleccione Distrito Fiscal',
                                                                        CO_DIST:data.CO_DIST_FISC,
                                                                        sufijoName:'FISC',
                                                                        name:'CO_DIST_FISC',
                                                                        sufijoLabel:'Fiscal'
                                                                   },
                                                                   {
                                                                       name:'DE_DIRE_FISC',
                                                                       fieldLabel: 'Dirección Fiscal',
                                                                       xtype:'textfield',
                                                                       value:data.DE_DIRE_FISC,
                                                                   },
                                                                   {
                                                                       name:'CO_TIPO_DOCU',
                                                                       fieldLabel: 'Tipo Documento',
                                                                       xtype:'combobox',
                                                                       displayField:'NO_TIPO_DOCU',
                                                                       valueField:'CO_TIPO_DOCU',
                                                                       editable: false,
                                                                       store:storeTipoDocu,
                                                                   },
                                                                   {
                                                                       name:'DE_NUME_DOCU',
                                                                       fieldLabel: 'Número documento',
                                                                       xtype:'textfield',
                                                                       value:data.DE_NUME_DOCU,
                                                                   },/*
                                                                   {
                                                                        xtype:'Desktop.Facturacion.Widgets.comboBoxDistrito',
                                                                        packageParent:{package:package,varSend:varSendPackage},
                                                                        title:'Seleccione Distrito Correspondencia',
                                                                        CO_DIST:data.CO_DIST_CORR,
                                                                        sufijoName:'CORR',
                                                                        name:'CO_DIST_CORR',
                                                                        sufijoLabel:'Correspondencia'
                                                                   },
                                                                   {
                                                                       name:'DE_DIRE_CORR',
                                                                       fieldLabel: 'Dirección Correspondencia',
                                                                       xtype:'textfield',
                                                                       value:data.DE_DIRE_CORR,
                                                                   },
                                                                   {
                                                                        xtype:'Desktop.Facturacion.Widgets.comboBoxDistrito',
                                                                        packageParent:{package:package,varSend:varSendPackage},
                                                                        title:'Seleccione Distrito Instalación',
                                                                        CO_DIST:data.CO_DIST_INST,
                                                                        sufijoName:'INST',
                                                                        name:'CO_DIST_INST',
                                                                        sufijoLabel:'Instalación'
                                                                   },
                                                                   {
                                                                       name:'DE_DIRE_INST',
                                                                       fieldLabel: 'Dirección Instalación',
                                                                       xtype:'textfield',
                                                                       value:data.DE_DIRE_INST,
                                                                   },
                                                                   {
                                                                       name:'CO_MONE_FACT',
                                                                       fieldLabel: 'Moneda',
                                                                       xtype:'combobox',
                                                                       displayField:'NO_MONE_FACT',
                                                                       valueField:'CO_MONE_FACT',
                                                                       editable: false,
                                                                       store:storeMoneFact,
                                                                   },*/
                                                                   {
                                                                       xtype:'grid',
                                                                       title:'Glosas',
                                                                       store:storeGlosas,
                                                                       width:1300,
                                                                       tbar:[
                                                                           {
                                                                               text:'Agregar Glosa',
                                                                               cls:'fac-btn fac-btn-success btn-size-tiny',
                                                                               handler:function(){
                                                                                   var w=fnc.createWindowModal({
                                                                                       height:220,
                                                                                       width:600,
                                                                                       title:'Agregar Glosa',
                                                                                       items:[
                                                                                           {
                                                                                               xtype:'form',
                                                                                               defaults: {
                                                                                                  anchor: '100%'
                                                                                               },
                                                                                               items:[
                                                                                                   {
                                                                                                        xtype:'combobox',
                                                                                                        name:'CO_NEGO_SUCU',
                                                                                                        fieldLabel: 'Sucursal',
                                                                                                        editable: false,
                                                                                                        store:storeNegoSucus,
                                                                                                        valueField:'CO_NEGO_SUCU',

                                                                                                        displayTpl:new Ext.XTemplate('<tpl for=".">{SUCU.DE_DIRE}</tpl>'),
                                                                                                        tpl:new Ext.XTemplate('<tpl for="."><div class="x-boundlist-item">{SUCU.DE_DIRE} ({SUCU.DIST.PROV.DEPA.NO_DEPA} - {SUCU.DIST.PROV.NO_PROV} - {SUCU.DIST.NO_DIST} )</div></tpl>'),
                                                                                                   },
                                                                                                   {
                                                                                                       xtype:'textfield',
                                                                                                       fieldLabel: 'Nombre Glosa',
                                                                                                       name:'NO_GLOS'
                                                                                                   },
                                                                                                   {
                                                                                                       xtype:'numberfield',
                                                                                                       fieldLabel: 'Monto Glosa',
                                                                                                       name:'IM_GLOS'
                                                                                                   },
                                                                                                   {
                                                                                                        xtype:'combobox',
                                                                                                        name:'CO_TIPO_GLOS',
                                                                                                        fieldLabel: 'Tipo Glosa',
                                                                                                        displayField:'NO_TIPO_GLOS',
                                                                                                        valueField:'CO_TIPO_GLOS',
                                                                                                        editable: false,
                                                                                                        store:storeTipoGlos,
                                                                                                    }
                                                                                               ],
                                                                                               buttons:[
                                                                                                   {
                                                                                                       text:'Guardar',
                                                                                                       cls:'fac-btn fac-btn-success btn-size-tiny',
                                                                                                       handler:function(){
                                                                                                            var values=this.up().up().getForm().getFieldValues();
                                                                                                            values.CO_RECI=data.CO_RECI; 
                                                                                                            values[varSendPackage]=package;
                                                                                                            Ext.Ajax.request({
                                                                                                                method:'POST',
                                                                                                                params:values,
                                                                                                                url: settingsLocal.AJAX_URI+'RECI/addGlosa/',
                                                                                                                success: function(response, opts) {
                                                                                                                    var json=fnc.responseExtAjaxRequestMsjError(response);
                                                                                                                    if(json.success){
                                                                                                                        w.close();
                                                                                                                        storeGlosas.load({
                                                                                                                            params:data,
                                                                                                                        });
                                                                                                                    }
                                                                                                                }
                                                                                                            });                                                                                                               
                                                                                                       }
                                                                                                   },
                                                                                                   {
                                                                                                       text:'Cancelar',
                                                                                                       cls:'fac-btn fac-btn-warning btn-size-tiny',
                                                                                                       handler:function(){
                                                                                                           w.close();
                                                                                                       }
                                                                                                   }
                                                                                               ]
                                                                                           }
                                                                                       ]
                                                                                   });
                                                                                   w.show();
                                                                               } 
                                                                           },
                                                                           {
                                                                               text:'Editar Glosa',
                                                                               cls:'fac-btn fac-btn-warning btn-size-tiny',
                                                                               handler:function(){
                                                                                    var grid=this.up().up();
                                                                                    var selected=grid.getView().getSelectionModel().getSelection();
                                                                                    if(selected.length==1){
                                                                                        var dataGlosa=selected[0].data;
                                                                                        
                                                                                        var items=[];
                                                                                        if (dataGlosa['TI_GLOS']==4 ||dataGlosa['TI_GLOS']==5 ||dataGlosa['TI_GLOS']==6 ||dataGlosa['TI_GLOS']==7){
                                                                                           items.push(
                                                                                                {
                                                                                                    xtype:'textfield',
                                                                                                    fieldLabel: 'Nombre Glosa',
                                                                                                    name:'NO_GLOS',
                                                                                                    value:dataGlosa.NO_GLOS,
                                                                                                },
                                                                                                {
                                                                                                    xtype:'numberfield',
                                                                                                    fieldLabel: 'Monto Glosa',
                                                                                                    name:'IM_GLOS',
                                                                                                    value:dataGlosa.IM_MONT,
                                                                                                },
                                                                                                {
                                                                                                    xtype:'combobox',
                                                                                                    name:'CO_TIPO_GLOS',
                                                                                                    fieldLabel: 'Tipo Glosa',
                                                                                                    displayField:'NO_TIPO_GLOS',
                                                                                                    valueField:'CO_TIPO_GLOS',
                                                                                                    editable: false,
                                                                                                    store:storeTipoGlos,
                                                                                                }                                
                                                                                            );
                                                                                        } else {
                                                                                            items.push(
                                                                                                {
                                                                                                    xtype:'textfield',
                                                                                                    name:'DE_DIRE_SUCU',
                                                                                                    fieldLabel: 'Dirección Sucursal',
                                                                                                    value:dataGlosa.DE_DIRE_SUCU,
                                                                                                },
                                                                                                {
                                                                                                    xtype:'Desktop.Facturacion.Widgets.comboBoxDistrito',
                                                                                                    packageParent:{package:package,varSend:varSendPackage},
                                                                                                    title:'Seleccione Distrito',
                                                                                                    CO_DIST:dataGlosa.CO_DIST,
                                                                                                    name:'CO_DIST'
                                                                                                },
                                                                                                {
                                                                                                    xtype:'textfield',
                                                                                                    fieldLabel: 'Nombre Glosa',
                                                                                                    name:'NO_GLOS',
                                                                                                    value:dataGlosa.NO_GLOS,
                                                                                                },
                                                                                                {
                                                                                                    xtype:'numberfield',
                                                                                                    fieldLabel: 'Monto Glosa',
                                                                                                    name:'IM_GLOS',
                                                                                                    value:dataGlosa.IM_MONT,
                                                                                                },
                                                                                                {
                                                                                                    xtype:'combobox',
                                                                                                    name:'CO_TIPO_GLOS',
                                                                                                    fieldLabel: 'Tipo Glosa',
                                                                                                    displayField:'NO_TIPO_GLOS',
                                                                                                    valueField:'CO_TIPO_GLOS',
                                                                                                    editable: false,
                                                                                                    store:storeTipoGlos,
                                                                                                }
                                                                                            );
                                                                                        }
                                                                                        
                                                                                        var w=fnc.createWindowModal({
                                                                                            height:420,
                                                                                            width:600,
                                                                                            title:'Editar Glosa',
                                                                                            items:[
                                                                                                {
                                                                                                    xtype:'form',
                                                                                                    defaults: {
                                                                                                       anchor: '100%'
                                                                                                    },
                                                                                                    items:items,
                                                                                                    buttons:[
                                                                                                        {
                                                                                                            text:'Guardar',
                                                                                                            cls:'fac-btn fac-btn-success btn-size-tiny',
                                                                                                            handler:function(){
                                                                                                                var values=this.up().up().getForm().getFieldValues();
                                                                                                                values.CO_RECI=data.CO_RECI;                                                                                                                
                                                                                                                values.CO_RECI_GLOS=dataGlosa.CO_RECI_GLOS; 
                                                                                                                bb=values;
                                                                                                                values[varSendPackage]=package;
                                                                                                                Ext.Ajax.request({
                                                                                                                    method:'POST',
                                                                                                                    params:values,
                                                                                                                    url: settingsLocal.AJAX_URI+'RECI/updateGlosa/',
                                                                                                                    success: function(response, opts) {
                                                                                                                       var json=fnc.responseExtAjaxRequestMsjError(response);
                                                                                                                        if(json.success){
                                                                                                                           w.close();
                                                                                                                           storeGlosas.load({
                                                                                                                               params:data,
                                                                                                                           });
                                                                                                                       }
                                                                                                                    }
                                                                                                                });                                                                                                               
                                                                                                            }
                                                                                                        },
                                                                                                        {
                                                                                                            text:'Cancelar',
                                                                                                            cls:'fac-btn fac-btn-warning btn-size-tiny',
                                                                                                            handler:function(){
                                                                                                                w.close();
                                                                                                            }
                                                                                                        }
                                                                                                    ]
                                                                                                }
                                                                                            ]
                                                                                        });
//                                                                                            w.query("[name=CO_TIPO_GLOS]")[0].setValue(dataGlosa['DE_NAME_TIPO_GLOS']);
                                                                                       w.query("[name=CO_TIPO_GLOS]")[0].setValue(dataGlosa['TI_GLOS']); 
//                                                                                       w.query("[name=CO_NEGO_SUCU]")[0].setValue(dataGlosa['CO_NEGO_SUCU']);
                                                                                        w.show();                                                                                            
                                                                                    }
                                                                               } 
                                                                           },
                                                                           {
                                                                               text:'Eliminar Glosa',
                                                                               cls:'fac-btn fac-btn-danger btn-size-tiny',
                                                                               handler:function(){
                                                                                    var grid=this.up().up();
                                                                                    var selected=grid.getView().getSelectionModel().getSelection();
                                                                                    if(selected.length==1){
                                                                                        var dataGlosa=selected[0].data;                                                                                       
                                                                                        fnc.createMsgConfirm({msg:'¿Esta seguro que lo desea eliminar?',call:function(a){
                                                                                            if (a=='yes'){
                                                                                                var values={};
                                                                                                values.CO_RECI=data.CO_RECI;                                                                                                                
                                                                                                values.CO_RECI_GLOS=dataGlosa.CO_RECI_GLOS;
                                                                                                values[varSendPackage]=package;
                                                                                                Ext.Ajax.request({
                                                                                                    method:'POST',
                                                                                                    params:values,
                                                                                                    url: settingsLocal.AJAX_URI+'RECI/deleteGlosa/',
                                                                                                    success: function(response, opts) {
                                                                                                       var json=fnc.responseExtAjaxRequestMsjError(response);
                                                                                                       if(json.success){                                                                                                               
                                                                                                           storeGlosas.load({
                                                                                                               params:data,
                                                                                                           });
                                                                                                       }
                                                                                                    }
                                                                                                });                                                                                                
                                                                                            }
                                                                                        }});
                                                                                    }
                                                                                } 
                                                                           }
                                                                       ],
                                                                       columns:[
                                                                           { text: 'Sucursal',  renderer:function(a,b,c){if (c.data.NEGO_SUCU==null){return "";}else {return c.data.DE_DIRE_SUCU;}},width:600},
                                                                           { text: 'Nombre',  dataIndex: 'NO_GLOS',width:400 },
                                                                           { text: 'Tipo',  renderer:function(a,b,c){return c.data.TIPO_GLOS.NO_TIPO_GLOS;},width:300},
                                                                           { text: 'Monto',  dataIndex: 'IM_MONT',width:80 },
                                                                       ]
                                                                   }
                                                               ]
                                                           }
                                                       ],
                                                       buttons:[
                                                           {
                                                               text:'Generar PDF',
                                                               cls:'fac-btn fac-btn-success btn-size-tiny',
                                                               handler:function(){
                                                                    /*var params={CO_RECI:data.CO_RECI};  
                                                                    params[varSendPackage]=package;
                                                                    Ext.Ajax.request({
                                                                        method:'POST',
                                                                        params:params,
                                                                        url: settingsLocal.AJAX_URI+'RECI/verReciboModificadoPDF/',
                                                                        success: function(response, opts) {
                                                                            var json=fnc.responseExtAjaxRequestMsjError(response);
                                                                            if(json.success){*/
                                                                                fnc.openURL(settingsLocal.AJAX_URI+"RECI/verReciboModificadoPDF/?numero="+data.CO_RECI);
                                                                            /*}
                                                                        }
                                                                    });  */                                                         
                                                               }
                                                           },
                                                           {
                                                               text:'Guardar',
                                                               cls:'fac-btn fac-btn-success btn-size-tiny',
                                                               handler:function(){
                                                                   var f=this.up().up().query("form")[0];
                                                                   var dataUpdate=f.getForm().getFieldValues();
                                                                   dataUpdate['CO_RECI']=data.CO_RECI;
                                                                   dataUpdate[varSendPackage]=package;
                                                                    Ext.Ajax.request({
                                                                        url:settingsLocal.AJAX_URI+'RECI/update/',
                                                                        params: dataUpdate,
                                                                        success: function(response){
                                                                            var json=fnc.responseExtAjaxRequestMsjError(response);
                                                                            if (json.success){
                                                                                //win.close(); 
                                                                                fnc.createMsgBoxSuccess({msg:"Se guarda con exito."});
                                                                                storeRef.load();
                                                                            }
                                                                        }
                                                                    });                                                            
                                                               }
                                                           },
                                                           {
                                                               text:'Anular',
                                                               cls:'fac-btn fac-btn-danger btn-size-tiny',
                                                               handler:function(){
                                                                    var params={CO_RECI:data.CO_RECI};  
                                                                    params[varSendPackage]=package;
                                                                    Ext.Ajax.request({
                                                                        url:settingsLocal.AJAX_URI+'RECI/anular/',
                                                                        params: params,
                                                                        success: function(response){
                                                                            var json=fnc.responseExtAjaxRequestMsjError(response);
                                                                            if (json.success){
                                                                                fnc.createMsgBoxSuccess({msg:"Se anuló con éxito."});
                                                                                win.close(); 
                                                                                storeRef.load();
                                                                            }
                                                                        }
                                                                    });                                                            
                                                               }
                                                           },
                                                           {
                                                               text:'Cerrar',
                                                               cls:'fac-btn fac-btn-primary btn-size-tiny',
                                                               handler:function(){
                                                                   win.close();
                                                               }
                                                           }                                                   
                                                       ]
                                                    });
                                                    win.show();             
                                                    //cargamos data de combo
                                                    win.query("[name=CO_TIPO_DOCU]")[0].setValue(data.CO_TIPO_DOCU);
//                                                    win.query("[name=CO_MONE_FACT]")[0].setValue(data.CO_MONE_FACT);
                                                }
                                            });
                                        }
                                    });
                                }
                            }                    
                        }
                    ],
                    fbar: [{xtype:'Desktop.Facturacion.Widgets.filterPagination',store:storeRef},'->'],//bien
                });                   
            }
        });
               
    });
    });
});