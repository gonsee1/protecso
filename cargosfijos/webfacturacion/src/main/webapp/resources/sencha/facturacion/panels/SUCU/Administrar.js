Ext.onReady(function(){
    fnc.load.ModelsAndStores(["PROD","SUCU","CLIE"],function(){
        var package="Desktop.Facturacion.Panels.SUCU.Administrar";
        var AJAX_URI=settings.AJAX_URI;
        var objDesktop=null;
        var storeData=new Desktop.Facturacion.Stores.CLIE();
        var varSendPackage=settings.Permissions.varSendPackage;
        fnc.addParameterStore(storeData,varSendPackage,package); 
        storeData.load();
        
        var perfil=settings.online.perfil;
        var hiddenBtn=false;
        if (perfil.trim()=='Facturación'){
            hiddenBtn=true;
        } 
            
        Ext.define(package,{ 
            extend:'Ext.grid.Panel',
            alias:'widget.Desktop.Facturacion.Panels.SUCU.Administrar',
            store:storeData,
            columns: [
                //{ text: 'Codigo',  dataIndex: 'CO_CLIE',width:90 },
                { text: 'Número Documento',  dataIndex:'DE_NUME_DOCU' },
                { text: 'Tipo',  renderer:function(a,b,c){return c.data.TIPO_DOCU.NO_TIPO_DOCU} },                
                { text: 'Razon Social',  dataIndex: 'NO_RAZO',width:300 },                
                { text: 'Nombre',  dataIndex: 'NO_CLIE',width:100 },
                { text: 'Apellido',  dataIndex: 'AP_CLIE',width:100 }
            ],
            tbar:[
                {
                    xtype:'Desktop.Facturacion.Widgets.filterPagination',
                    store:storeData,            
                    addBeforeItems:[
                        {
                            xtype: 'button',
                            text: "Ver Sucursales",
                            iconCls: fnc.icons.class.ver(),
                            listeners:{
                                click:function(btn,e,opts){
                                    asignarDesktop(this);
                                    var grid=this.up('grid');
                                    var selected=grid.getView().getSelectionModel().getSelection();
                                    if(selected.length==1){
                                        verSucursales(selected[0].data);
                                    }
                                }
                            }
                        }                    
                    ]
                }
            ]   
        });
        //Asignar Desktop
        function asignarDesktop(hijo){
            if (objDesktop==null)
                objDesktop=hijo.up("desktop");
        }
        //EDITAR
        function verSucursales(obj){
            if(objDesktop!==null){ 
                var store=new Desktop.Facturacion.Stores.SUCU();
                fnc.setApiProxyStore(store,{read:'selectByCLIE/'});
                
                fnc.addParameterStore(store,varSendPackage,package);
                fnc.addParameterStore(store,"CO_CLIE",obj.CO_CLIE);
                store.load({                    
                    callback:function(records,operation,success){
                        if (success){
                            var panelForm=Ext.create('Ext.grid.Panel',{
                                store:store,
                                columns: [
                                    {text: 'Codigo',  dataIndex: 'CO_SUCU'},
                                    {text: 'Dirección',  dataIndex: 'DE_DIRE'},
                                    {text: 'Referencia',  dataIndex: 'DE_REF_DIRE'},
                                    {text: 'Departamento',  renderer:function(a,b,c){return c.data.DIST.PROV.DEPA.NO_DEPA;}},
                                    {text: 'Provincia',  renderer:function(a,b,c){return c.data.DIST.PROV.NO_PROV;}},
                                    {text: 'Distrito',  renderer:function(a,b,c){return c.data.DIST.NO_DIST;}},
                                ],    
                                buttons: [
                                    {
                                        xtype:'button',
                                        text:'Editar',
                                        iconCls: fnc.icons.class.edit(),
                                        hidden: hiddenBtn,
                                        listeners:{
                                            click:function(){
                                                var grid=this.up('grid');
                                                var selected=grid.getView().getSelectionModel().getSelection();
                                                if(selected.length==1){
                                                    var storeSucu=new Desktop.Facturacion.Stores.SUCU();
                                                    fnc.addParameterStore(storeSucu,varSendPackage,package); 
                                                    fnc.setApiProxyStore(storeSucu,{read:'getid/'});
                                                    storeSucu.load({
                                                        params:selected[0].data,
                                                        callback:function(){
                                                            var formEditar=Ext.create("Desktop.Facturacion.Panels.SUCU.Insertar",{
                                                                varsInitComponent:{
                                                                    CO_DIST:selected[0].data.DIST.CO_DIST,
                                                                },
                                                                buttons: [  {
                                                                        text: 'Cancelar',
                                                                        handler: function() {
                                                                            winEditar.close();
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
                                                                                params['CO_SUCU']=model.data.CO_SUCU;
                                                                                params['CO_CLIE']=model.data.CO_CLIE;
                                                                                winEditar.disable();
                                                                                model.save({
                                                                                    params:fnc.getParamsSendPackage(params,varSendPackage,package),
                                                                                    success:function(){
                                                                                        winEditar.close();
                                                                                        store.load();                                                    
                                                                                    }
                                                                                });
                                                                            }                                                                           
                                                                        }
                                                                    }
                                                                ]
                                                            }); 
                                                            formEditar.query("[name='CO_CLIE']")[0].disable();
                                                            formEditar.loadRecord(storeSucu.getAt(0));
                                                            var winEditar=fnc.createWindowModal(objDesktop,{
                                                                items:[formEditar]
                                                            });
                                                            winEditar.show();                                                            
                                                        }
                                                    });
                                                }
                                            }
                                        }
                                    }
                                ]                                    
                            });
                            var window=fnc.createWindow(objDesktop,{
                                width:800,
                                height:400,
                                title:'Editar Sucursales de cliente ' + obj.NO_RAZO +' - '+obj.AP_CLIE+' , '+obj.AP_CLIE+' '+obj.TIPO_DOCU.NO_TIPO_DOCU+':'+obj.DE_NUME_DOCU,
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