(function(){
    var package='Desktop.Facturacion.Widgets.selectSucursalByCliente'; 
    Ext.require([
        'Ext.grid.*',
        'Ext.data.*',
        'Ext.ux.grid.FiltersFeature',
        'Ext.toolbar.Paging',
        'Ext.ux.ajax.JsonSimlet',
        'Ext.ux.ajax.SimManager'
    ]);
    
    Ext.define(package,{
        extend:'Ext.form.FieldContainer',
        alias:'widget.'+package,       
        initComponent:function(){
            var obj=this;
            obj.width=obj.width || "400px";
            obj.width=parseInt(obj.width);
            obj.name=obj.name || "CO_SUCU";
            obj.label=obj.label || "Sucursal";
            var textArea=Ext.create("Ext.form.field.TextArea",{
                fieldLabel: obj.label,
                name:'DE_SUCU',
                readOnly:true,
                fieldStyle:"font-size:10px;",   
                style:{
                    width:(obj.width-65)+'px',  
                    marginBottom:'5px',
                }                             
            });            
            if (obj.defaultValues){
                var store=new Desktop.Facturacion.Stores.SUCU();
                fnc.setApiProxyStore(store,{read:'getid/'});
                fnc.addParameterStore(store,obj.packageParent.varSendPackage,obj.packageParent.package);
                fnc.addParameterStore(store,"CO_SUCU",obj.defaultValues);
                
                store.load({
                    callback:function(){
                    	
                        if (store.data.items.length==1){
                        	
                        	
                            textArea.setValue(store.data.items[0].data.DE_DIRE);
                            
                            obj.query("CO_SUCU")[0].setValue(store.data.items[0].data.CO_SUCU);
                        }
                    }
                });
            }
            obj.items=[
                {
                    xtype:'textfield',
                    name:obj.name,
                    allowBlank: false,
                    hidden:true,
                },{
                    xtype:'fieldset',
                    title: 'Selección '+obj.label,
                    allowBlank: false,
                    layout:'hbox',
                    style:{
                        width:obj.width+'px',
                    },                     
                    items:[
                        textArea,
                        {
                            xtype:'button',
                            text:'...',
                            name:'btnSeleccionarSucursal',
                            style:{
                                marginLeft:'5px',
                            },
                            listeners:{
                                click:function(){
                                    if(typeof obj.getCO_CLIE == 'function'){
                                        fnc.load.panels([
                                            ['SUCU','Insertar']
                                        ],function(){
                                            var store=new Desktop.Facturacion.Stores.SUCU();
                                            var CO_CLIE=obj.getCO_CLIE();
                                            fnc.setApiProxyStore(store,{read:'selectByCLIE/'})
                                            fnc.addParameterStore(store,obj.packageParent.varSendPackage,obj.packageParent.package);
                                            fnc.addParameterStore(store,"CO_CLIE",CO_CLIE);
                                            var panel=Ext.create('Ext.grid.Panel',{
                                               store: store,
                                               columns:[
                                                   { text: 'Codigo',  dataIndex: 'CO_SUCU', width:50,filter: {type: 'string'} },
                                                   { text: 'Departamento',  dataIndex: 'DIST.PROV.DEPA.NO_DEPA', width:120,filter: {type: 'list'} },
                                                   { text: 'Provincia',  dataIndex: 'DIST.PROV.NO_PROV', width:120,filter: {type: 'list'} },
                                                   { text: 'Distrito', dataIndex: 'DIST.NO_DIST', width:120,filter: {type: 'list'}},                                               
                                                   { text: 'Dirección',  dataIndex: 'DE_DIRE', width:250,filter: {type: 'string'}},
                                                   { text: 'Referencia',  dataIndex: 'DE_REF_DIRE', width:250,filter: {type: 'string'}},
                                               ],
                                                features:[
                                                   {
                                                        ftype: 'filters',
                                                        encode: false, // json encode the filter query
                                                        local: true, // defaults to false (remote filtering)
                                                        filters: [] 
                                                    }
                                                ],  
                                                //plugins: 'gridfilters',
                                               listeners:{
                                                   itemdblclick: function (view, record, item, index, e, eOpts ){
                                                        var selected=view.getSelectionModel().getSelection();
                                                        if(selected.length==1){                      
                                                            var data=selected[0].data;
                                                            obj.query("[name='"+obj.name+"']")[0].setValue(data.CO_SUCU);
                                                            obj.query("[name='DE_SUCU']")[0].setValue(data.DE_DIRE+" ("+data.DIST.PROV.DEPA.NO_DEPA+" - "+data.DIST.PROV.NO_PROV+" - "+data.DIST.NO_DIST+")");
                                                            window.close();
                                                        }
                                                   }

                                               },
                                               tbar:[
                                                   {
                                                       text:'Agregar Sucursal',
                                                       iconCls: fnc.icons.class.insert(),
                                                       handler:function(){
                                                            var panelInsert=Ext.create("Desktop.Facturacion.Panels.SUCU.Insertar",{
                                                                 buttons:[
                                                                    {
                                                                        text:'Guardar',
                                                                        formBind: true, //only enabled once the form is valid
                                                                        disabled: true,                                                                          
                                                                        handler:function(){
                                                                            var form=this.up("form").getForm();
                                                                            if (form.isValid()){
                                                                                var model=new Desktop.Facturacion.Models.SUCU(form.getFieldValues());
                                                                                model.save({params:fnc.getParamsSendPackage(model.getData(),obj.packageParent.varSendPackage,obj.packageParent.package)});
                                                                                form.reset(); 
                                                                                win.close();
                                                                                store.load();
                                                                            }                                                                            
                                                                        }
                                                                    },
                                                                    {
                                                                        text:'Cancelar',                                                                       
                                                                        handler:function(){
                                                                            win.close();
                                                                        }
                                                                    }                                                                    
                                                                 ]
                                                            });
                                                            var win=fnc.createWindowModal({
                                                                title:'Agregar Sucursal',
                                                                items:[panelInsert]
                                                            });
                                                            panelInsert.query("fieldset")[0].disable();//desactivamos el cuadro del cliente
                                                            panelInsert.query("field[name='CO_CLIE']")[0].setValue(CO_CLIE);
                                                            win.show();                                                            
                                                       }
                                                   }
                                               ]
                                            });
                                            var desktop=Desktop.Facturacion.getDesktop();
                                            var window=fnc.createWindowModal(desktop,{
                                                title:'Seleccione Sucursal',
                                                width:950,
                                                items:[panel],
                                                renderTo:Ext.getBody(),
                                            });
                                            window.show();
                                        });
                                    }
                                }
                            }
                        },
                    ]
                }
            ];
            obj.callParent();           
        },
    }); 

})();