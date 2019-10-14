(function(){
    var package='Desktop.Facturacion.Widgets.selectServiciosSuplementariosByProducto';
    Ext.define(package,{
        extend:'Ext.grid.Panel',
        alias:'widget.'+package,
        title: 'Seleccione Servicios Suplementarios',
        allowBlank: false,
        collapsible: true,
        columns:[
            /*{text:'Codigo',dataIndex:'CO_SERV_SUPL',width:50 },*/
            {text:'Nombre',dataIndex:'NO_SERV_SUPL',width:400 },
            {text:'Monto',dataIndex:'IM_MONTO',width:80 },
            {
                xtype:'actioncolumn',
                width:50,
                items: [{                                                            
                    icon:fnc.images.uri.delete(),
                    tooltip: 'Eliminar',
                    width:50,
                    handler: function(grid, rowIndex, colIndex) {
                        var store=grid.getStore()
                        var rec = store.getAt(rowIndex)
                        if(rec){
                            fnc.createMsgConfirm({msg:'¿Desea borrar servicio suplementario?',
                                call:function(btn){
                                    if (btn=='yes')
                                        store.remove(rec);
                                }
                            });
                        }
                    }
                }]
            }
        ],        
        store:null,        
        initComponent:function(){
            var obj=this; 
            obj.width=obj.width || 600;
            obj.store=Ext.create("Ext.data.Store",{
                fields:[
                    {name:'CO_SERV_SUPL'},{name:'NO_SERV_SUPL'},{name:'IM_MONTO'}
                ]
            });
            obj.tbar=[
                {
                    text:'Agregar Servicio Suplementario',
                    cls:'fac-btn fac-btn-success btn-size-tiny',
                    handler:function(){
                        if(typeof obj.CO_PROD != 'undefined'){
                            var storeSERV_SUPL=new Desktop.Facturacion.Stores.SERV_SUPL();
                            fnc.setApiProxyStore(storeSERV_SUPL,{read:'selectByPROD/'})
                            fnc.addParameterStore(storeSERV_SUPL,obj.packageParent.varSend,obj.packageParent.package);
                            fnc.addParameterStore(storeSERV_SUPL,"CO_PROD",obj.CO_PROD);
                            fnc.addParameterStore(storeSERV_SUPL,"CO_MONE_FACT",obj.CO_MONE_FACT);
                            var panel=Ext.create('Ext.grid.Panel',{
                               store: storeSERV_SUPL,
                               columns:[
                                   { text: 'Codigo',  dataIndex: 'CO_SERV_SUPL',width:50,filter: {type: 'string'} },
                                   { text: 'Nombre',  dataIndex: 'NO_SERV_SUPL',width:400,filter: {type: 'string'} },
                                   { text: 'Monto',  dataIndex: 'IM_MONTO',width:80,filter: {type: 'string'} },
                                   { text: 'Detracción',  dataIndex: 'ST_AFEC_DETR',xtype: 'booleancolumn',trueText: 'Si',falseText: 'No',width:80,filter: {type: 'boolean'}  },
                               ],
                                features:[
                                    {
                                         ftype: 'filters',
                                         encode: false, // json encode the filter query
                                         local: true, // defaults to false (remote filtering)
                                    }
                                ],                               
                               listeners:{
                                   itemdblclick: function (view, record, item, index, e, eOpts ){
                                        var selected=view.getSelectionModel().getSelection();
                                        if(selected.length==1){                      
                                            var data=selected[0].data;
                                            var grid=obj;
                                            var store=grid.getStore();
                                            var existe=false;                                                                                                                
                                            for(var i=0;i<store.data.items.length && !existe;i++)
                                                if(store.data.items[i].data.CO_SERV_SUPL==data.CO_SERV_SUPL)
                                                    existe=true;
                                            if (!existe)
                                                store.add(data);
                                            else
                                                fnc.createMsgBoxError({msg:'Ya existe este servicio suplementario.'})
                                            window.close();
                                        }
                                   }

                               }
                           });
                           var window=fnc.createWindowModal(obj.desktop,{
                                width:800,
                                items:[panel],
                                renderTo:Ext.getBody(),
                            });
                            window.show();
                        }
                    }
                }
            ];
            
            obj.callParent();
        }
    });
    /*
    Ext.define(package,{
        extend:'Ext.form.FieldContainer',
        alias:'widget.'+package,
        initComponent:function(){
            var obj=this;
            var storeSeleccionado=Ext.create("Ext.data.Store",{
                fields:[
                    {name:'CO_SERV_SUPL'},{name:'NO_SERV_SUPL'},{name:'IM_MONTO'}
                ]
            });
            obj.items=[{
                    xtype:'fieldset',
                    title: 'Seleccione Servicios Suplementarios',
                    allowBlank: false,
                    layout:'hbox',
                    width:'100%',
                    items:[
                        {
                            xtype:'grid',                            
                            store:storeSeleccionado,
                            width:'90%',
                            columns:[
                                {text:'Codigo',dataIndex:'CO_SERV_SUPL',width:50 },
                                {text:'Nombre',dataIndex:'NO_SERV_SUPL',width:400 },
                                {text:'Monto',dataIndex:'IM_MONTO',width:80 },
                            ]
                        },
                        {
                            xtype:'button',
                            text:'...',
                            name:'btnSeleccionarPlan',
                            style:{
                                marginLeft:'5px',
                            },
                            listeners:{
                                click:function(){
                                    if(typeof obj.CO_PROD != 'undefined'){
                                        var storeSERV_SUPL=new Desktop.Facturacion.Stores.SERV_SUPL();
                                        fnc.setApiProxyStore(storeSERV_SUPL,{read:'selectByPROD/'})
                                        fnc.addParameterStore(storeSERV_SUPL,obj.packageParent.varSend,obj.packageParent.package);
                                        fnc.addParameterStore(storeSERV_SUPL,"CO_PROD",obj.CO_PROD);
                                        var panel=Ext.create('Ext.grid.Panel',{
                                           store: storeSERV_SUPL,
                                           columns:[
                                               { text: 'Codigo',  dataIndex: 'CO_SERV_SUPL',width:50 },
                                               { text: 'Nombre',  dataIndex: 'NO_SERV_SUPL',width:400 },
                                               { text: 'Monto',  dataIndex: 'IM_MONTO',width:80 },
                                           ],
                                           listeners:{
                                               itemdblclick: function (view, record, item, index, e, eOpts ){
                                                    var selected=view.getSelectionModel().getSelection();
                                                    if(selected.length==1){                      
                                                        var data=selected[0].data;
                                                        var grid=obj.items.getAt(0).items.getAt(0);
                                                        aaa=grid;
                                                        bbb=data;
                                                        ccc=storeSeleccionado;
                                                        
                                                        storeSeleccionado.suspendEvents();
                                                        
                                                        storeSeleccionado.add(data);
                                                        
                                                        storeSeleccionado.resumeEvents();
                                                        window.close();
                                                        //var store=grid.getStore();
                                                        //var existe=false;                                                                                                                
                                                        //for(var i=0;i<store.data.items.length && !existe;i++)
                                                        //    if(store.data.items[i].data.CO_SERV_SUPL==data.CO_SERV_SUPL)
                                                        //        existe=true;
                                                        //if (!existe)
                                                        //    store.add(data);
                                                        //else
                                                        //    fnc.createMsgBoxError({msg:'Ya existe este servicio.'})
                                                        //window.close();
                                                    }
                                               }
                                               
                                           }
                                       });
                                       var window=fnc.createWindowModal(obj.desktop,{
                                            width:800,
                                            items:[panel],
                                            renderTo:Ext.getBody(),
                                        });
                                        window.show()
                                    }
                                }
                            }
                        }                        
                    ]
                }
            ];
            obj.callParent();           
        },
    }); 
    */
})();