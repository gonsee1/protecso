/* global Ext, fnc, Desktop */

(function(){
    var package='Desktop.Facturacion.Widgets.selectCliente';    
    Ext.define(package,{
        extend:'Ext.form.FieldContainer',
        alias:'widget.'+package,       
        initComponent:function(){
            var obj=this;   
            var textArea=null;
            obj.width=obj.width || "400px";
            obj.width=parseInt(obj.width);
            obj.name=obj.name || "CO_CLIE";
            var textArea=Ext.create("Ext.form.field.TextArea",{
                fieldLabel: 'Cliente',
                name:'DE_CLIE',
                readOnly:true,
                fieldStyle:"font-size:10px;",   
                style:{
                    width:(obj.width-65)+'px',  
                    marginBottom:'5px',
                },                             
            });
            if (obj.defaultValues){
                var store=new Desktop.Facturacion.Stores.CLIE();
                fnc.setApiProxyStore(store,{read:'getid/'});
                fnc.addParameterStore(store,obj.packageParent.varSendPackage,obj.packageParent.package);
                fnc.addParameterStore(store,"CO_CLIE",obj.defaultValues);
                
                
                //fnc.addParameterStore(store,"CO_CLIE",store.data.items[0].data.CO_CLIE);
                
                store.load({
                    callback:function(){ 
                        if (store.data.items.length==1){
                        	obj.query("[name='CO_CLIE']")[0].setValue(store.data.items[0].data.CO_CLIE);                      	
                            textArea.setValue(Desktop.Facturacion.Models.CLIE.getFullInfo(store.data.items[0].data));
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
                    title: 'Selección Cliente',
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
                            name:'btnSeleccionarCliente',
                            style:{
                                marginLeft:'5px',
                            },
                            listeners:{
                                click:function(){
                                    fnc.load.scripts(["widgets/filterPagination.js"],function(){
                                        fnc.load.ModelsAndStores(["CLIE"],function(){
                                            fnc.load.panels([
                                                ["CLIE","Insertar"]
                                            ],function(){                                        
                                                    var store=new Desktop.Facturacion.Stores.CLIE();
                                                    fnc.addParameterStore(store,obj.packageParent.varSendPackage,obj.packageParent.package);                                    
                                                    var panel=Ext.create('Ext.grid.Panel',{
                                                       store: store,
                                                       columns:[
                                                           { text: 'Codigo',  dataIndex: 'CO_CLIE' },
                                                           { text: 'N° Documento', dataIndex: 'DE_NUME_DOCU', width:120  },                                               
                                                           { text: 'Razón',  dataIndex: 'NO_RAZO' , width:200 },
                                                           { text: 'Nombre',  dataIndex: 'NO_CLIE' },
                                                           { text: 'Apellido', dataIndex: 'AP_CLIE'  },
                                                       ],
                                                       listeners:{
                                                           itemdblclick: function (view, record, item, index, e, eOpts ){
                                                                var selected=view.getSelectionModel().getSelection();
                                                                if(selected.length==1){                      
                                                                    var data=selected[0].data;
                                                                    obj.query("[name='"+obj.name+"']")[0].setValue(data.CO_CLIE);
                                                                    obj.query("[name='DE_CLIE']")[0].setValue("N° Documento : "+Ext.String.trim(data.DE_NUME_DOCU)+", RAZON : "+data.NO_RAZO+", Nombre : "+data.NO_CLIE+", Apellido : "+data.AP_CLIE);
                                                                    window.close();
                                                                    if(typeof obj.changeValue=='function')
                                                                        obj.changeValue(data.CO_CLIE);                                                            
                                                                }
                                                           }

                                                       },
                                                       tbar:Ext.create('Desktop.Facturacion.Widgets.filterPagination',{store:store,
                                                           addBeforeItems:[
                                                               {
                                                                    xtype: 'button',
                                                                    text: 'Agregar Cliente',
                                                                    iconCls: fnc.icons.class.insert(),                                                       
                                                                    handler:function(){
                                                                        var win=fnc.createWindowModal({
                                                                            renderTo:Ext.getBody(),
                                                                            title:'Agregar Cliente',
                                                                            items:[
                                                                                Ext.create("Desktop.Facturacion.Panels.CLIE.Insertar",{
                                                                                    buttons:[
                                                                                        {
                                                                                            text:'Agregar',
                                                                                            //formBind: false, //only enabled once the form is valid
                                                                                            handler:function(){
                                                                                                var form=this.up("form").getForm();
                                                                                                if (form.isValid()) {
                                                                                                    var model=new Desktop.Facturacion.Models.CLIE(form.getFieldValues());
                                                                                                    model.save({params:fnc.getParamsSendPackage(model.getData(),obj.packageParent.varSendPackage,obj.packageParent.package)});
                                                                                                    win.close();
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
                                                                                })
                                                                            ]
                                                                        });
                                                                        win.show();
                                                                    }
                                                               }
                                                           ]
                                                       })
                                                    });
                                                    var desktop=Desktop.Facturacion.getDesktop();
                                                    var window=fnc.createWindowModal(desktop,{
                                                        title:'Seleccione Cliente',
                                                        width:850,
                                                        items:[panel],
                                                        renderTo:Ext.getBody(),
                                                    });
                                                    window.show();
                                                }
                                            );                                            
                                        })
                                    });
                                }
                            }                            
                        }                            
                    ]
                }
            ];
            
            obj.callParent(); 
        }
    });
    /*
    var package='Desktop.Facturacion.Widgets.comboBoxCliente';    
    Ext.define(package,{
        extend:'Ext.form.FieldSet',
        title:'Selección Cliente',
        //extend:'Ext.form.FieldContainer',
        alias:'widget.'+package,       
        initComponent:function(){
            var obj=this;   
            obj.width=obj.width || "400px";
            obj.width=parseInt(obj.width);            
            obj.name=obj.name || "CO_CLIE";
            obj.items=[
                {
                    xtype:'combobox',
                    fieldLabel: 'Cliente',
                    displayField: 'DE_NUME_DOCU',
                    valueField: 'CO_CLIE',
                    anchor: '-15',
                    store: {
                        type: 'Desktop.Facturacion.Stores.CLIE'
                    }, 
                    tpl:'<tpl for="."><div class="x-boundlist-item" >{NO_RAZO} - {DE_NUME_DOCU} - {AP_CLIE}, {NO_CLIE}  </div></tpl>',
                    minChars: 3,
                    queryParam: 'FIL_CLIE',
                    queryMode: 'remote',
                    name:obj.name,   
                    width:obj.width,
                }           
            ];
            fnc.load.ModelsAndStores(["CLIE"],function(){
                obj.packageParent=obj.packageParent || {};
                obj.stores={};
                obj.stores.CLIE=new Desktop.Facturacion.Stores.CLIE();
                fnc.addParameterStore(obj.stores.CLIE,obj.packageParent.varSend,obj.packageParent.package);
                var combClie=obj.query("[name='CO_CLIE']")[0];
                var  termino=true;
                combClie.on('change',function(combo,FIL_CLIE){
                    if(FIL_CLIE!=null && termino){
                        termino=false;
                        obj.stores.CLIE.load({params:{FIL_CLIE:FIL_CLIE},callback:function(){
                            combClie.setStore(obj.stores.CLIE);
                            termino=true;
                        }});
                    }
                });
                if(typeof obj.changeValue=='function')
                    combClie.on('select',function(a,b){obj.changeValue(a,b);});
            });
            obj.callParent();           
        }
    }); 
    */
})();