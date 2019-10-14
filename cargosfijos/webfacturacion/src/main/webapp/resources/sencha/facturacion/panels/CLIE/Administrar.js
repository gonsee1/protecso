Ext.onReady(function(){
    fnc.load.scripts(["widgets/filterPagination.js"],function(){
        fnc.load.ModelsAndStores(["CONT_CLIE","TIPO_CLIE","TIPO_DOCU","SUCU","CLIE","CLIE_HISTORICO"],function(){
            var package="Desktop.Facturacion.Panels.CLIE.Administrar";
            var AJAX_URI=settings.AJAX_URI;
            var objDesktop=null;
            var storeData=new Desktop.Facturacion.Stores.CLIE();
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
                alias:'widget.Desktop.Facturacion.Panels.CLIE.Administrar',
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
                }, 
                "-", {
                    xtype: 'button',
                    text: "Eliminar",
                    tooltip: "Eliminar",
                    iconCls: fnc.icons.class.delete(),
//                    visible: visibleBtn,
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
                                    msg:'Esta seguro que desea eliminar el item de modulo '+model.data.NO_CLIE,
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
                }, "-", {
                    xtype: 'button',
                    text: "Historial",
                    tooltip: "Ver Historial",
                    iconCls: fnc.icons.class.explorar(),
                    listeners:{
                        click:function(){
                            asignarDesktop(this);
                            var grid=this.up("panel").down('grid');
                            var selected=grid.getView().getSelectionModel().getSelection();
                            if(selected.length==1){
                                verHistorial(selected[0].data); 
                            }                   
                        }
                    }
                }
                ],                
                items:[    
                    { 
                        xtype:'grid',
                        store:storeData,
                        columns: [
                            { text: 'Codigo',  dataIndex: 'CO_CLIE' },
                            { text: 'Razón Social', dataIndex: 'NO_RAZO' },
                            { text: 'Nombre', dataIndex: 'NO_CLIE'},
                            { text: 'Apellido', dataIndex: 'AP_CLIE' },
                            { text: 'Tipo Documento', renderer:function(value, p, r){return r.data.TIPO_DOCU.NO_TIPO_DOCU;}},
                            { text: 'Número Documento', dataIndex: 'DE_NUME_DOCU'} ,                
                            { text: 'Producto', dataIndex: 'DE_NUME_DOCU'} //EMANUEL BARBARAN EFITEC
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
                        model:'Desktop.Facturacion.Models.CLIE',
                        proxy:{
                            type:'rest',
                            actionMethods: {create : 'POST',read   : 'POST',update : 'POST',destroy: 'POST'},
                            api: {
                                read:AJAX_URI+'CLIE/getid/',
                                update:AJAX_URI+'CLIE/update/',
                            },
                            reader:{rootProperty:'data'}
                        },                 
                    });
                    fnc.addParameterStore(store,varSendPackage,package);
                    store.load({
                        params:obj,
                        callback:function(records,operation,success){
                            if (success){
                                var regStore=store.getAt(0);
                                var regStoreData=regStore.data;
                                var panelForm=Ext.create('Desktop.Facturacion.Panels.CLIE.Insertar',{
                                    esEdicion:true,
                                    CLIE:regStoreData,
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
                                                    params['CO_CLIE']=model.data.CO_CLIE;
                                                    window.disable();
                                                    model.save({
                                                        params:fnc.getParamsSendPackage(params,varSendPackage,package),
                                                        success:function(){
                                                            window.close();
                                                            storeData.load(); 
                                                            
                                                            //Mensaje donde el usuario ingresarara informacion para el historico del cliente
                                                            var winHist=fnc.createWindowModal({
                                                                    height:250,
                                                                    width:450,
                                                                    title:'Información Histórica',
                                                                    items:[
                                                                        {
                                                                            xtype:'form',
                                                                            items:[
                                                                                {
                                                                                    xtype:'textarea',
                                                                                    fieldLabel: 'Información',
                                                                                    name:'DE_INFORMACION',
                                                                                    allowBlank: false,
                                                                                    width:400
                                                                                }                                                
                                                                            ],
                                                                            buttons: [
                                                                                {
                                                                                    text: 'Guardar',
                                                                                    formBind: true, //only enabled once the form is valid
                                                                                    disabled: true,
                                                                                    handler: function() {
                                                                                        var form = this.up('form').getForm();
                                                                                        if (form.isValid()) {
                                                                                            var params=form.getFieldValues();
                                                                                            params.CO_CLIE=obj.CO_CLIE;
                                                                                            params[varSendPackage]=package;
                                                                                            var model=new Desktop.Facturacion.Models.CLIE_HISTORICO(form.getFieldValues());
                                                                                            model.save({params:params});
                                                                                            form.reset();
                                                                                            winHist.close();
                                                                                        }
                                                                                    }
                                                                                },{
                                                                                    text: 'Cancelar',
                                                                                    handler: function() {
                                                                                        winHist.close();
                                                                                    }
                                                                                }
                                                                            ] 
                                                                        }
                                                                    ]
                                                                });
                                                                winHist.show();
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
                                panelForm.loadRecord(regStore);
                                fnc.forms.combobox.autoselect(panelForm.query('combobox[name=CO_TIPO_CLIE]')[0]);
                                fnc.forms.combobox.autoselect(panelForm.query('combobox[name=CO_SUCU_FISC]')[0]);
                                fnc.forms.combobox.autoselect(panelForm.query('combobox[name=CO_CONT_CLIE_REPR_LEGA]')[0]);
                                var window=fnc.createWindowModal(objDesktop,{
                                    width:600, //400
                                    height:450,
                                    title:'Editar ' + obj.NO_CLIE,
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
            
            //VER HISTORIAL
            function verHistorial(obj){
                var storeClieHistorico=new Desktop.Facturacion.Stores.CLIE_HISTORICO();
                fnc.addParameterStore(storeClieHistorico,varSendPackage,package);
                fnc.addParameterStore(storeClieHistorico,"CO_CLIE",obj.CO_CLIE);
                fnc.setApiProxyStore(storeClieHistorico,{read:'selectByCO_CLIE/'});
                storeClieHistorico.load();

                var winHis=fnc.createWindowModal({
                    title:'Historial Cliente',
                    layout:'fit',
                    width:750,
                    height:400,
                    items:[
                        {
                            xtype:'grid',
                            layout:'fit',
                            store:storeClieHistorico,
                            columns:[
                                {text:'Información',dataIndex:'DE_INFORMACION',width:400},
                                {text:'Usuario',dataIndex:'USUA.DE_USER'},
                                {text:'Fecha',dataIndex:'FH_REGI', xtype: 'datecolumn',format: 'd/m/Y H:i:s',width:200},
                            ]                                                                                    
                        }
                    ]
                });
                winHis.show();
            }
        });
    });
});