Ext.onReady(function(){
    fnc.load.ModelsAndStores(["PERI_FACT"],function(){
        var package="Desktop.Facturacion.Panels.PERI_FACT.Administrar";
        var AJAX_URI=settings.AJAX_URI+'PERI_FACT/';
        var objDesktop=null;
        var storeData=new Desktop.Facturacion.Stores.PERI_FACT();
        var varSendPackage=settings.Permissions.varSendPackage;
        fnc.addParameterStore(storeData,varSendPackage,package);    
        Ext.define(package,{ 
            extend:'Ext.grid.Panel',
            alias:'widget.Desktop.Facturacion.Panels.PERI_FACT.Administrar',
            store:storeData,
            columns: [
                { text: 'Codigo',  dataIndex: 'CO_PERI_FACT',width:'30px' },
                { text: 'Nombre', dataIndex: 'NO_PERI_FACT', flex: 1,width:'30%' },
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
                                msg:'Esta seguro que desea eliminar el item de modulo '+model.data.NO_PERI_FACT,
                                call:function(btn){
                                    if(btn=='yes'){
                                        store.destroy({
                                            params:model.data,
                                            callback:function(a,b,c){
                                                storeData.load();
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
                        storeData.load();
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
                    model:'Desktop.Facturacion.Models.PERI_FACT',
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
                            var panelForm=Ext.create('Desktop.Facturacion.Panels.PERI_FACT.Insertar',{
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
                                                params['CO_PERI_FACT']=model.data.CO_PERI_FACT;
                                                window.disable();
                                                model.save({
                                                    params:fnc.getParamsSendPackage(params,varSendPackage,package),
                                                    success:function(){
                                                        window.close();
                                                        storeData.load();                                                    
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
                                title:'Editar ' + obj.NO_PERI_FACT,
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