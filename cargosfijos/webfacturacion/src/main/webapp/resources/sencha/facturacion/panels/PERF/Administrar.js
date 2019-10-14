Ext.onReady(function(){
    fnc.load.ModelsAndStores(["PERF"],function(){
        var package="Desktop.Facturacion.Panels.PERF.Administrar";
        var AJAX_URI=settings.AJAX_URI+'PERF/';
        var objDesktop=null;
        var storePerf=new Desktop.Facturacion.Stores.PERF();
        var varSendPackage=settings.Permissions.varSendPackage;
        fnc.addParameterStore(storePerf,varSendPackage,package);
        Ext.define(package,{ 
            extend:'Ext.grid.GridPanel',
            alias:'widget.Desktop.Facturacion.Panels.PERF.Administrar',
            store:storePerf,
            columns: [
                { text: 'Codigo',  dataIndex: 'CO_PERF',width:'20%' },
                { text: 'Nombre', dataIndex: 'NO_PERF', flex: 1,width:'40%' },
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
                                msg:'Esta seguro que desea eliminar el modulo '+model.data.NO_PERF,
                                call:function(btn){
                                    if(btn=='yes'){
                                        store.destroy({
                                            params:model.data,
                                            callback:function(a,b,c){
                                                storePerf.load();
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
                        storePerf.load();
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
                    model:'Desktop.Facturacion.Models.PERF',
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
                            var panelForm=Ext.create('Desktop.Facturacion.Panels.PERF.Insertar',{                        
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
                                                params['CO_PERF']=model.data.CO_PERF;
                                                window.disable();
                                                model.save({
                                                    params:fnc.getParamsSendPackage(params,varSendPackage,package),
                                                    success:function(){
                                                        window.close();
                                                        storePerf.load();                                                    
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
                                height:170,
                                title:'Editar ' + obj.NO_PERF,
                                renderTo:Ext.getBody(),
                                items:[panelForm]
                            });
                            window.show();  
                        }else{
                            fnc.createMsgBoxError();
                        }
                    }
                })
            }
        }
    });
});