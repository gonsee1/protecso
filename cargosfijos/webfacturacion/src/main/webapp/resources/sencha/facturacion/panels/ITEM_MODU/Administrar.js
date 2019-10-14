Ext.onReady(function(){
    fnc.load.ModelsAndStores(["MODU","ITEM_MODU"],function(){
        var package="Desktop.Facturacion.Panels.ITEM_MODU.Administrar";
        var AJAX_URI=settings.AJAX_URI+'ITEM_MODU/';
        var objDesktop=null;
        var storeITEM_MODU=new Desktop.Facturacion.Stores.ITEM_MODU();
        var varSendPackage=settings.Permissions.varSendPackage;
        fnc.addParameterStore(storeITEM_MODU,varSendPackage,package);    
        Ext.define(package,{ 
            //xtype: "grid",
            extend:'Ext.grid.Panel',
            //autoScroll: true,
            //height:'100px',
            alias:'widget.Desktop.Facturacion.Panels.ITEM_MODU.Administrar',
            store:storeITEM_MODU,
            columns: [
                { text: 'Codigo',  dataIndex: 'CO_ITEM_MODU',width:'30px' },
                { text: 'Nombre', dataIndex: 'NO_ITEM_MODU', flex: 1,width:'30%' },
                { text: 'Paquete', dataIndex: 'DE_PACK',width:'30%' },
                { text: 'Modulo', dataIndex:'NO_MODU',width:'20%',}
            ],
            width:'100%',
            tbar: [{
                xtype: 'button',
                text: "Editar",
                tooltip: "Editar",
                iconCls: "fac_icon_edit",
                listeners:{
                    click:function(btn,e,opts){
                        asignarDesktop(this);
                        var grid=this.up('grid');
                        var selected=grid.getView().getSelectionModel().getSelection();
                        if(selected.length==1){
                            editar(selected[0].data);
                        }
                    }
                }
            }, "-", {
                xtype: 'button',
                text: "Eliminar",
                tooltip: "Eliminar",
                iconCls: "remove",
                listeners:{
                    click:function(){
                        asignarDesktop(this);
                        var grid=this.up('grid');
                        var selected=grid.getView().getSelectionModel().getSelection();
                        if(selected.length==1){
                            var store=grid.getStore();
                            var model=selected[0];                        
                            fnc.createMsgConfirm({
                                msg:'Esta seguro que desea eliminar el item de modulo '+model.data.NO_ITEM_MODU,
                                call:function(btn){
                                    if(btn=='yes'){
                                        store.destroy({
                                            params:model.data,
                                            callback:function(a,b,c){
                                                storeITEM_MODU.load();
                                            }
                                        });

                                    }                                
                                }
                            });

                        }                   
                    }
                }
            }, "-", {
                xtype: 'button',
                text: "Actualizar",
                tooltip: "Actualizar",
                iconCls: "refresh",
                listeners:{
                    click:function(){
                        storeITEM_MODU.load();
                    }
                }
            }]    
        });
        //Asignar Desktop
        function asignarDesktop(hijo){
            if (objDesktop==null)
                objDesktop=hijo.up("desktop");
        }
        //EDITAR
        function editar(obj){
            if(objDesktop!==null){          
                var store= new Ext.data.Store({
                    model:'Desktop.Facturacion.Models.ITEM_MODU',
                    proxy:{
                        type:'rest',
                        //url: AJAX_URI+'getid/',
                        actionMethods: {create : 'POST',read   : 'POST',update : 'POST',destroy: 'POST'},
                        api: {
                            read:AJAX_URI+'getid/',
                            update:AJAX_URI+'update/',
                            //create
                            //destroy
                        },
                        reader:{rootProperty:'data'}
                    },                 
                });
                fnc.addParameterStore(store,varSendPackage,package);
                store.load({
                    params:obj,
                    callback:function(records,operation,success){
                        if (success){
                            var panelForm=Ext.create('Desktop.Facturacion.Panels.ITEM_MODU.Insertar',{
                                buttons: [  {
                                        text: 'Limpiar',
                                        handler: function() {
                                            this.up('form').getForm().reset();
                                        }
                                    }, {
                                        text: 'Guardar',
                                        formBind: true, //only enabled once the form is valid
                                        disabled: true,
                                        handler: function() {
                                            var form = this.up('form').getForm();
                                            if (form.isValid()) {
                                                var model=form.getRecord();
                                                var params=form.getFieldValues();
                                                params['CO_ITEM_MODU']=model.data.CO_ITEM_MODU;
                                                window.disable();
                                                model.save({
                                                    params:fnc.getParamsSendPackage(params,varSendPackage,package),
                                                    success:function(){
                                                        window.close();
                                                        storeITEM_MODU.load();                                                    
                                                    }
                                                });
                                            }
                                        }
                                },{
                                    text: 'Cancelar',
                                    handler: function() {
                                        window.close();
                                    }
                                }]                                    
                            });
                            panelForm.loadRecord(store.getAt(0));
                            var window=fnc.createWindowModal(objDesktop,{
                                width:400,
                                height:200,
                                title:'Editar ' + obj.NO_ITEM_MODU,
                                renderTo:Ext.getBody(),
                                items:[panelForm]
                            });
                            window.show();  
                        }else{
                            fnc.createMsgBoxError();
                        }
                    }
                });           
            }
        }
    });
});