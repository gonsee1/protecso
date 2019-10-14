(function(){
    var package='Desktop.Facturacion.Widgets.selectServiciosUnicosByProducto';
    Ext.define(package,{
        extend:'Ext.grid.Panel',
        alias:'widget.'+package,
        title: 'Seleccione Servicios Unicos',
        allowBlank: false,
        collapsible: true,
        columns:[
            /*{text:'Codigo',dataIndex:'CO_SERV_UNIC',width:50 },*/
            {text:'Nombre',dataIndex:'NO_SERV_UNIC',width:400 },
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
                            fnc.createMsgConfirm({msg:'¿Desea borrar servicio unico?',
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
                    {name:'CO_SERV_UNIC'},{name:'NO_SERV_UNIC'},{name:'IM_MONTO'}
                ]
            });
            obj.tbar=[
                {
                    text:'Agregar Servicio Unico',
                    cls:'fac-btn fac-btn-success btn-size-tiny',
                    handler:function(){
                        if(typeof obj.CO_PROD != 'undefined'){
                            var storeSERV_UNIC=new Desktop.Facturacion.Stores.SERV_UNIC();
                            fnc.setApiProxyStore(storeSERV_UNIC,{read:'selectByPROD/'})
                            fnc.addParameterStore(storeSERV_UNIC,obj.packageParent.varSend,obj.packageParent.package);
                            fnc.addParameterStore(storeSERV_UNIC,"CO_PROD",obj.CO_PROD);
                            fnc.addParameterStore(storeSERV_UNIC,"CO_MONE_FACT",obj.CO_MONE_FACT);
                            var panel=Ext.create('Ext.grid.Panel',{
                               store: storeSERV_UNIC,
                               columns:[
                                    { text: 'Codigo',  dataIndex: 'CO_SERV_UNIC',width:50,filter: {type: 'string'} },
                                    { text: 'Nombre',  dataIndex: 'NO_SERV_UNIC',width:400,filter: {type: 'string'} },
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
                                                if(store.data.items[i].data.CO_SERV_UNIC==data.CO_SERV_UNIC)
                                                    existe=true;
                                            if (!existe)
                                                store.add(data);
                                            else
                                                fnc.createMsgBoxError({msg:'Ya existe este servicio unico.'})
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
})();