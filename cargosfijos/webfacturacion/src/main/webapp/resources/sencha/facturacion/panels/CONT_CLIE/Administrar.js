Ext.onReady(function(){
    fnc.load.scripts(["widgets/filterPagination.js"],function(){
        fnc.load.ModelsAndStores(["CONT_CLIE","TIPO_CLIE","TIPO_DOCU","SUCU","CLIE"],function(){
            var package="Desktop.Facturacion.Panels.CONT_CLIE.Administrar";
            var AJAX_URI=settings.AJAX_URI;
            var objDesktop=null;
            var storeData=new Desktop.Facturacion.Stores.CLIE();
            var varSendPackage=settings.Permissions.varSendPackage;
            var storeCont_Clie=Ext.create('Desktop.Facturacion.Stores.CONT_CLIE',{autoLoad:false});
            fnc.addParameterStore(storeCont_Clie,varSendPackage,package);
            fnc.addParameterStore(storeData,varSendPackage,package);
            fnc.setApiProxyStore(storeCont_Clie,{read:'selectByCLIE/'});
            storeData.load();
            
            var perfil=settings.online.perfil;
            var hiddenBtn=false;
            if (perfil.trim()=='Facturación'){
                hiddenBtn=true;
            }
        
            Ext.define(package,{ 
                extend:'Ext.panel.Panel',               
                alias:'widget.Desktop.Facturacion.Panels.CONT_CLIE.Administrar',
                layout:'fit',
                tbar: [{
                    xtype: 'button',
                    text: "Ver Contactos",
                    tooltip: "Ver Contactos",
                    iconCls: fnc.icons.class.edit(),
                    listeners:{
                        click:function(btn,e,opts){
                            asignarDesktop(this);
                            var grid=this.up("panel").down('grid');
                            var selected=grid.getView().getSelectionModel().getSelection();
                            if(selected.length==1){
                                ver_contactos(selected[0].data);
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
                            { text: 'Codigo',  dataIndex: 'CO_CLIE' },
                            { text: 'Razón Social', dataIndex: 'NO_RAZO' },
                            { text: 'Nombre', dataIndex: 'NO_CLIE'},
                            { text: 'Apellido', dataIndex: 'AP_CLIE' },
                            { text: 'Tipo Documento', renderer:function(value, p, r){return r.data.TIPO_DOCU.NO_TIPO_DOCU;}},
                            { text: 'Número Documento', dataIndex: 'DE_NUME_DOCU'}                
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
            function ver_contactos(obj){
                if(objDesktop!==null){ 
                    fnc.addParameterStore(storeCont_Clie,"CO_CLIE",obj.CO_CLIE);
                    storeCont_Clie.load();
                    var win=fnc.createWindowModal({
                        title:'Contactos',
                        width:600,
                        height:400,
                        items:[
                            {
                                xtype:'grid',
                                store:storeCont_Clie,
                                columns:[
                                    {text:'Documento',dataIndex:'DE_NUME_DOCU'},
                                    {text:'Apellido',dataIndex:'AP_CONT_CLIE'},
                                    {text:'Nombre',dataIndex:'NO_CONT_CLIE'},
                                    {text:'Correo',dataIndex:'DE_CORR'},
                                    {text:'Telefono',dataIndex:'DE_TELE'}
                                ]
                            }
                        ],
                        tbar:[
                            {
                                text:'Agregar',
                                iconCls: fnc.icons.class.insert(),
                                hidden: hiddenBtn,
                                handler:function(){
                                    //var grid=this.up("panel").down('grid');
                                    //var selected=grid.getView().getSelectionModel().getSelection();
                                    //if(selected.length==1){
                                    //var data=selected[0].data;
                                    fnc.load.panels([
                                        ["CONT_CLIE","Insertar"]
                                    ],function(){
                                        var panelForm=Ext.create('Desktop.Facturacion.Panels.CONT_CLIE.Insertar',{
                                            buttons: [
                                                {
                                                    text: 'Guardar',
                                                    formBind: true, //only enabled once the form is valid
                                                    disabled: true,
                                                    handler: function() {
                                                        var form = this.up('form').getForm();
                                                        if (form.isValid()) {
                                                            var model=new Desktop.Facturacion.Models.CONT_CLIE(form.getFieldValues());
                                                            model.save({params:fnc.getParamsSendPackage(model.getData(),varSendPackage,package)});
                                                            form.reset();
                                                            window.close();
                                                            storeCont_Clie.load();
                                                        }
                                                    }
                                            },{
                                                text: 'Cancelar',
                                                handler: function() {
                                                    window.close();
                                                }
                                            }]                                    
                                        });
                                        panelForm.query('textfield[name=CO_CLIE]')[0].setValue(obj.CO_CLIE);
                                        panelForm.query('textarea[name=DE_CLIE]')[0].setValue(obj.NO_RAZO);
                                        panelForm.query("textarea[name=DE_CLIE]")[0].disable();  
                                        panelForm.query("[name=btnSeleccionarCliente]")[0].disable();
                                        var window=fnc.createWindowModal(objDesktop,{
                                            width:450,
                                            height:450,
                                            title:'Agregar',
                                            renderTo:Ext.getBody(),
                                            items:[panelForm]
                                        });
                                        window.show();                                             
                                    });
                                    //}
                                }
                            },
                            {
                                text:'Editar',
                                iconCls: fnc.icons.class.edit(),
                                hidden: hiddenBtn,
                                handler:function(){
                                    
                                    var grid=this.up("panel").down('grid');
                                    var selected=grid.getView().getSelectionModel().getSelection();
                                    if(selected.length==1){
                                        var data=selected[0].data;
                                        
                                        var storeEditarContacto=Ext.create("Desktop.Facturacion.Stores.CONT_CLIE",{autoLoad:false});
                                        fnc.addParameterStore(storeEditarContacto,varSendPackage,package);
                                        fnc.setApiProxyStore(storeEditarContacto,{read:'selectById/'});
                                        storeEditarContacto.load({
                                            params:data,
                                            callback:function(records,operation,success){
                                                if (success){
                                                    var regStore=storeEditarContacto.getAt(0);
                                                    var regStoreData=regStore.data;
                                                    
                                                    var panelForm=Ext.create('Desktop.Facturacion.Panels.CONT_CLIE.Insertar',{
                                                        buttons: [
                                                            {
                                                                text: 'Guardar',
                                                                formBind: true, //only enabled once the form is valid
                                                                disabled: true,
                                                                handler: function() {
                                                                    var form = this.up('form').getForm();
                                                                    if (form.isValid()) {
                                                                        var model=form.getRecord();
                                                                        var params=form.getFieldValues();
                                                                        params['CO_CONT_CLIE']=model.data.CO_CONT_CLIE;
                                                                        window.disable();
                                                                        model.save({
                                                                            params:fnc.getParamsSendPackage(params,varSendPackage,package),
                                                                            success:function(){
                                                                                window.close();
                                                                                storeCont_Clie.load();                                                    
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
                                                    panelForm.query('textfield[name=CO_CLIE]')[0].setValue(obj.CO_CLIE);
                                                    panelForm.query('textarea[name=DE_CLIE]')[0].setValue(obj.NO_RAZO);
                                                    panelForm.query("textarea[name=DE_CLIE]")[0].disable();  
                                                    panelForm.query("[name=btnSeleccionarCliente]")[0].disable();
                                                    var window=fnc.createWindowModal({
                                                        width:450,
                                                        height:450,
                                                        title:'Agregar',
                                                        renderTo:Ext.getBody(),
                                                        items:[panelForm]
                                                    });
                                                    
                                                    panelForm.loadRecord(regStore);
                                                    window.show();                                                     
                                                    
                                                    
                                                }
                                            }
                                        });
                                        
                                                                             
                                    }
                                }
                            },
                        ],                                
                        buttons:[
                            {
                                text:'Cerrar',
                                handler:function(){
                                    win.close();
                                }
                            }
                        ]                        
                    });
                    win.show();
                }
            }
        });
    });
});