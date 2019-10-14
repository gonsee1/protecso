Ext.onReady(function(){
    fnc.load.scripts(["widgets/filterPagination.js"],function(){
       fnc.load.ModelsAndStores(["DEPA","PROV","DIST"],function(){
            var package="Desktop.Facturacion.Panels.UBIGEO.AdministrarDistrito";
            var AJAX_URI=settings.AJAX_URI;
            var objDesktop=null;
            var storeData=new Desktop.Facturacion.Stores.DIST();
            var varSendPackage=settings.Permissions.varSendPackage;
            fnc.addParameterStore(storeData,varSendPackage,package);
            storeData.load();
            
            var perfil=settings.online.perfil;
            var hiddenFactBtn=false;
            var hiddenBtn=true;
            if (perfil.trim()=='Facturación'){
                hiddenFactBtn=true;
            }
            if (perfil.trim()=='Administrador' || perfil.trim()=='Administrador Facturación'){
                hiddenBtn=false;
            }
            
            Ext.define(package,{ 
                extend:'Ext.panel.Panel',
                alias:'widget.Desktop.Facturacion.Panels.UBIGEO.AdministrarDistrito',
                layout:'fit',
                tbar: [{
                    xtype: 'button',
                    text: "Editar",
                    tooltip: "Editar",
                    iconCls: fnc.icons.class.edit(),
                    hidden: hiddenFactBtn,
                    listeners:{
                        click:function(btn,e,opts){
                            asignarDesktop(this);
                            var grid=this.up("panel").down('grid');
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
                    iconCls: fnc.icons.class.delete(),
                    hidden: hiddenBtn,
                    listeners:{
                        click:function(){
                            asignarDesktop(this);
                            var grid=this.up("panel").down('grid');
                            var selected=grid.getView().getSelectionModel().getSelection();
                            if(selected.length==1){
                                var store=grid.getStore();
                                var model=selected[0];                        
                                fnc.createMsgConfirm({
                                    msg:'Esta seguro que desea eliminar el item de modulo '+model.data.NO_DIST,
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
                    iconCls: fnc.icons.class.actualizar(),
                    listeners:{
                        click:function(){
                            storeData.load();
                        }
                    }
                }],
                items:[    
                    { 
                        xtype:'grid',
                        store:storeData,
                        columns: [
                            { text: 'Codigo',  dataIndex: 'CO_DIST',width:'30px' },
                            { text: 'Nombre', dataIndex: 'NO_DIST', flex: 1,width:'30%' },
                            { text: 'Provincia', dataIndex: 'NO_PROV', flex: 1,width:'30%',renderer:function(value, p, r){return r.data.PROV.NO_PROV;} },
                            { text: 'Departamento', dataIndex: 'NO_DEPA', flex: 1,width:'30%',renderer:function(value, p, r){return r.data.PROV.DEPA.NO_DEPA;} },
                        ],    
                    }    
                ],
                fbar: [{xtype:'Desktop.Facturacion.Widgets.filterPagination',store:storeData},'->'],//bien
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
                        model:'Desktop.Facturacion.Models.DIST',
                        proxy:{
                            type:'rest',
                            actionMethods: {create : 'POST',read   : 'POST',update : 'POST',destroy: 'POST'},
                            api: {
                                read:AJAX_URI+'DIST/getid/',
                                update:AJAX_URI+'DIST/update/',
                            },
                            reader:{rootProperty:'data'}
                        },                 
                    });
                    fnc.addParameterStore(store,varSendPackage,package);
                    store.load({
                        params:obj,
                        callback:function(records,operation,success){
                            if (success){
                                var panelForm=Ext.create('Desktop.Facturacion.Panels.UBIGEO.InsertarDistrito',{
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
                                                    params['CO_DIST']=model.data.CO_DIST;
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
                                    title:'Editar ' + obj.NO_DIST,
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
});