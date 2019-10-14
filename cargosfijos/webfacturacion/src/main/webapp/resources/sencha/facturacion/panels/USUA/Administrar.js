Ext.onReady(function(){
    fnc.load.ModelsAndStores(["USUA"],function(){
        var package="Desktop.Facturacion.Panels.USUA.Administrar";
        var AJAX_URI=settings.AJAX_URI+'USUA/';
        var objDesktop=null;
        var storeUSUA=new Desktop.Facturacion.Stores.USUA();
        var varSendPackage=settings.Permissions.varSendPackage;
        fnc.addParameterStore(storeUSUA,varSendPackage,package);    
        Ext.define(package,{ 
            extend:'Ext.grid.GridPanel',
            alias:'widget.Desktop.Facturacion.Panels.USUA.Administrar',
            store:storeUSUA,
            columns: [
                { text: 'Codigo',  dataIndex: 'CO_USUA'},
                { text: 'Perfil', dataIndex: 'NO_PERF'},
                { text: 'Nombre', dataIndex: 'NO_USUA' },
                { text: 'Apellido', dataIndex:'AP_USUA'},
                { text: 'Correo', dataIndex:'DE_CORR'},
                { text: 'Usuario', dataIndex:'DE_USER'},
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
                                                storeUSUA.load();
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
                        storeUSUA.load();
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
                    model:'Desktop.Facturacion.Models.USUA',
                    proxy:{
                        type:'rest',
                        actionMethods: {create : 'POST',read   : 'POST',update : 'POST',destroy: 'POST'},
                        api: {
                            read:AJAX_URI+'getid/',
                            update:AJAX_URI+'update/',
                        },
                        reader:{rootProperty:'data'}
                    },                 
                });
                fnc.addParameterStore(store,varSendPackage,package);
                store.load({
                    params:obj,
                    callback:function(records,operation,success){
                        if (success){ 
                            var form=Ext.create('Desktop.Facturacion.Panels.USUA.Insertar',{
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
                                                 params['CO_USUA']=model.data.CO_USUA;
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
                            form.loadRecord(store.getAt(0));
                            form.stores.perf.load();
                            var window=fnc.createWindowModal(objDesktop,{
                                width:400,
                                height:400,
                                title:'Editar ' + obj.NO_USUA,
                                renderTo:Ext.getBody(),
                                items:[form]
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
