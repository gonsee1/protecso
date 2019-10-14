Ext.onReady(function(){
    fnc.load.ModelsAndStores(["PROD","SERV_SUPL"],function(){
        var package="Desktop.Facturacion.Panels.SERV_SUPL.Administrar";
        var AJAX_URI=settings.AJAX_URI;
        var objDesktop=null;
        var storeData=new Desktop.Facturacion.Stores.SERV_SUPL();
        var varSendPackage=settings.Permissions.varSendPackage;
        fnc.addParameterStore(storeData,varSendPackage,package); 
        var storeProd_Padre= new Desktop.Facturacion.Stores.PROD_PADRE();       
        fnc.addParameterStore(storeProd_Padre,varSendPackage,package); 
                
        storeData.load();
        
        var perfil=settings.online.perfil;
        var hiddenContratosBtn=false;
        var hiddenBtn=true;
        if (perfil.trim()=='Contratos'){
            hiddenContratosBtn=true;
        } 
        if (perfil.trim()=='Administrador' || perfil.trim()=='Administrador Facturación'){
            hiddenBtn=false;
        } 
        
        Ext.define(package,{ 
            extend:'Ext.grid.Panel',
            alias:'widget.Desktop.Facturacion.Panels.SERV_SUPL.Administrar',
            store:storeData,
            columns: [
                /*{ text: 'Codigo',  dataIndex: 'CO_SERV_SUPL' },*/
                { text: 'Nombre',  dataIndex: 'NO_SERV_SUPL' },                
                { text: 'Moneda', renderer:function(value, p, r){return r.data.MONE_FACT.NO_MONE_FACT;} },
                { text: 'Producto', dataIndex: 'COD_PROD_PADRE', renderer:function(value, p, r){return r.data.PROD_PADRE.DESC_PROD_PADRE;}},        
                { text: 'Servicio', renderer:function(value, p, r){return r.data.PROD.NO_PROD;} },
                { text: 'Monto',  dataIndex: 'IM_MONTO' },
                { text: 'Afecto Detracción',  renderer:function(value, p, r){return r.data.ST_AFEC_DETR?"SI":"NO";} }
            ],
            width:'100%',
            tbar: [                
            {
                id : 'comboSuple',    
                xtype:'combobox',                
            	fieldLabel:'Producto',
            	name:'COD_PROD_PADRE',
            	displayField:'DESC_PROD_PADRE',
            	valueField:'COD_PROD_PADRE',
            	store:storeProd_Padre,
            	allowBlank:false,
                editable:false,     
            }, "-", 
            {
                xtype: 'button',
                text: "Buscar",
                tooltip: "Buscar",
                iconCls: fnc.icons.class.explorar(),
                listeners:{
                    click:function(){
                        
                        var str = Ext.getCmp('comboSuple').getValue(); // document.getElementById('combo').value;.
                                                                                    
                        storeData.reload();
                        storeData.filter('COD_PROD_PADRE',str);                                                                                        
                    }
                }
            }, "-",                                 
            {
                xtype: 'button',
                text: "Editar",
                tooltip: "Editar",
                iconCls: "fac_icon_edit",
                hidden: hiddenContratosBtn,
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
                hidden: hiddenBtn,
                listeners:{
                    click:function(){
                        asignarDesktop(this);
                        var grid=this.up('grid');
                        var selected=grid.getView().getSelectionModel().getSelection();
                        if(selected.length==1){
                            var store=grid.getStore();
                            var model=selected[0];                        
                            fnc.createMsgConfirm({
                                msg:'Esta seguro que desea eliminar el item de modulo '+model.data.NO_SERV_SUPL,
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
                    model:'Desktop.Facturacion.Models.SERV_SUPL',
                    proxy:{
                        type:'rest',
                        actionMethods: {create : 'POST',read   : 'POST',update : 'POST',destroy: 'POST'},
                        api: {
                            read:AJAX_URI+'SERV_SUPL/getid/',
                            update:AJAX_URI+'SERV_SUPL/update/',
                        },
                        reader:{rootProperty:'data'}
                    },                 
                });
                fnc.addParameterStore(store,varSendPackage,package);
                store.load({
                    params:obj,
                    callback:function(records,operation,success){
                        if (success){
                            var panelForm=Ext.create('Desktop.Facturacion.Panels.SERV_SUPL.Insertar',{
                                 varsInitComponent:{
                                    CO_PROD:obj.PROD.CO_PROD,                                     
                                 }, 
                                
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
                                                params['CO_SERV_SUPL']=model.data.CO_SERV_SUPL;
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
                                height:380,
                                title:'Editar ' + obj.CO_SERV_SUPL,
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