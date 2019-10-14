Ext.onReady(function(){
    fnc.load.scripts(["widgets/filterPagination.js"],function(){
        fnc.load.ModelsAndStores(["PROD","CLIE","SUCU","TIPO_FACT","MONE_FACT","PERI_FACT","NEGO","MOTI_DESC","NEGO_SUCU","NEGO_HISTORICO","PROD_PADRE"],function(){
            var package="Desktop.Facturacion.Panels.NEGO.Administrar";
            var settingsLocal=window.settings.real.get();
            var AJAX_URI=settingsLocal.AJAX_URI;
            var objDesktop=null;
            var storeData=new Desktop.Facturacion.Stores.NEGO();
            var varSendPackage=settings.Permissions.varSendPackage;            
            fnc.addParameterStore(storeData,varSendPackage,package); 
            
            var storeProd_Padre= new Desktop.Facturacion.Stores.PROD_PADRE();       
            fnc.addParameterStore(storeProd_Padre,varSendPackage,package); 
            
            storeData.load();
            
            var perfil=settings.online.perfil;
            var hiddenBtn=false;
            if (perfil.trim()=='Facturación'){
                hiddenBtn=true;
            }    
            
            Ext.define(package,{ 
                extend:'Ext.grid.Panel',
                alias:'widget.Desktop.Facturacion.Panels.NEGO.Administrar',                    
                store:storeData,
                //enableLocking: true,
                columns: [
                    { text: 'Negocio',  dataIndex: 'CO_NEGO' },
                    { text: 'Cliente',width:300, renderer:function(value, p, r){return Desktop.Facturacion.Models.CLIE.getFullInfo(r.data.CLIE);} },
                    { text: 'Producto', dataIndex: 'COD_PROD_PADRE', renderer:function(value, p, r){return r.data.PROD_PADRE.DESC_PROD_PADRE;}},        
                    { text: 'Servicio',width:250, renderer:function(value, p, r){return r.data.PROD.NO_PROD;} },
                    { text: 'Tipo',width:100, renderer:function(value, p, r){return r.data.TIPO_FACT.NO_TIPO_FACT;}},               
                    { text: 'Moneda',width:100, renderer:function(value, p, r){return r.data.MONE_FACT.NO_MONE_FACT;}},               
                    { text: 'Periodo',width:100, renderer:function(value, p, r){return r.data.PERI_FACT.NO_PERI_FACT;}},
                    { text: 'Estado',width:100, renderer:function(value, p, r){if (r.data.isPendiente) {return fnc.shortcodes.getPendiente(r.data.isPendiente);} else if (r.data.isDesactivado) {return fnc.shortcodes.getDesactivado(r.data.isDesactivado);} else if (r.data.isCortado) {return fnc.shortcodes.getCortado(r.data.isCortado);} else if (r.data.isSuspendido) {return fnc.shortcodes.getSuspendido(r.data.isSuspendido);} else {return fnc.shortcodes.getActivo();}}},
                    {
                        xtype:'actioncolumn',
                        width:50,
                        items: [{                                                            
                            icon:fnc.images.uri.ver(),
                            tooltip: 'Preview',
                            width:50,
                            handler: function(grid, rowIndex, colIndex) {
                                var rec = grid.getStore().getAt(rowIndex);
                                if(rec){
                                    var CO_NEGO=rec.data.CO_NEGO; 
                                    var params={CO_NEGO:CO_NEGO};
                                    params[varSendPackage]=package;
                                    Ext.Ajax.request({
                                        method:'POST',
                                        params:params,
                                        url: AJAX_URI+'NEGO/createTokenPreviewPDF/',
                                        success: function(response, opts) {
                                            var json=Ext.JSON.decode(response.responseText);
                                            if(json){
                                                fnc.openURL(AJAX_URI+"NEGO/previewPDF/?tk="+json.data);
                                            }
                                        }
                                    });
                                }
                            }
                        }]
                    }
                ],
                tbar: [                    
                    {
                            id : 'comboUni',    
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

                                        var str = Ext.getCmp('comboUni').getValue(); // document.getElementById('combo').value;.

                                        //storeData.reload();
                                        storeData.filter('COD_PROD_PADRE',str);                                                                                        
                                    }
                                }
                    }, "-",  
                    
                    
                    {
                            xtype: 'button',
                            text: "Editar",
                            tooltip: "Editar",
                            iconCls: "fac_icon_edit",
                            hidden: hiddenBtn,
                            listeners:{
                                click:function(btn,e,opts){
                                    asignarDesktop(this);
                                    var grid=this.up('grid');
                                    var selected=grid.getView().getSelectionModel().getSelection();
                                    
                                    if(selected.length==1){
                                    	console.info('selected. data='+selected[0].data);
                                        editar(selected[0].data);
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
                            text: "Detalle de Negocio",
                            tooltip: "Detalle de Negocio",
                            iconCls: fnc.icons.class.explorar(),
                            listeners:{
                                click:function(){
                                    asignarDesktop(this);
                                    var grid=this.up('grid');
                                    var selected=grid.getView().getSelectionModel().getSelection();
                                    if(selected.length==1){
                                        verSucursales(selected[0].data);
                                    }                        
                                }
                            }
                        },"-", {
                            xtype: 'button',
                            text: "Desactivación Masiva",
                            tooltip: "Desactivación Masiva",
                            iconCls: fnc.icons.class.explorar(),
                            listeners:{
                                click:function(){
                                    asignarDesktop(this);  
                                    cargarDesactivacionMasiva();
                                }
                            }
                        },"-", {
                            xtype: 'button',
                            text: "Ver Negocios Pendientes",
                            tooltip: "Ver Negocios Pendientes",
                            iconCls: fnc.icons.class.explorar(),
                            listeners:{
                                click:function(){
                                    asignarDesktop(this);  
                                    verNegociosPendientes();
                                }
                            }
                        }/*,"-", {
                            xtype: 'button',
                            text: "Cambiar Moneda",
                            tooltip: "Cambiar Moneda",
                            iconCls: fnc.icons.class.explorar(),
                            hidden: hiddenBtn,
                            listeners:{
                                click:function(btn,e,opts){
                                    asignarDesktop(this);
                                    var grid=this.up('grid');
                                    var selected=grid.getView().getSelectionModel().getSelection();
                                    if(selected.length==1){
                                        cambiarMonedaNegocio(selected[0].data);
                                    }
                                }
                            }
                        }*/
                        
                        /*{
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
                                            msg:'Esta seguro que desea eliminar el item de modulo '+model.data.CO_NEGO,
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
                        },*/
                    ],
                //fbar: Ext.create('Desktop.Facturacion.Widgets.filterPagination',{store:storeData})// mal
                fbar: [{xtype:'Desktop.Facturacion.Widgets.filterPagination',store:storeData},'->'],//bien
                listeners:{
                    rowdblclick:function(a,b,c,d,e,f,g,h,i){
                        asignarDesktop(this);
                        var grid=this;
                        var selected=grid.getView().getSelectionModel().getSelection();
                        if(selected.length==1){
                            verSucursales(selected[0].data);
                        }  
                    }
                }
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
                        model:'Desktop.Facturacion.Models.NEGO',
                        proxy:{
                            type:'rest',
                            actionMethods: {create : 'POST',read   : 'POST',update : 'POST',destroy: 'POST'},
                            api: {
                                read:AJAX_URI+'NEGO/getid/',
                                update:AJAX_URI+'NEGO/update/',
                            },
                            reader:{rootProperty:'data'}
                        },                 
                    });
                    fnc.addParameterStore(store,varSendPackage,package);
                    store.load({
                        params:obj,
                        callback:function(records,operation,success){
                            if (success){
                                var panelForm=Ext.create('Desktop.Facturacion.Panels.NEGO.Insertar',{
                                    varsInitComponent:{
                                    CO_PROD:obj.PROD.CO_PROD,                                     
                                    },
                                    defaultValues:store.getAt(0).data,
                                    buttons: [  {
                                            text: 'Limpiar',
                                            handler: function() {
                                                this.up('form').getForm().reset();
                                            }
                                        }, {
                                            text: 'Guardar',
                                            //formBind: true, //only enabled once the form is valid
                                            //disabled: true,
                                            handler: function() {
                                                var form = this.up('form').getForm();
                                                var grid = this.up('form').down('[name=gridCorreos]');
                                                //if (form.isValid()) {
                                                    var model=form.getRecord();
                                                    var params=form.getFieldValues();
                                                    
                                                    var store=grid.getStore();
                                                    params.DE_CORRS=[];
                                                    for(var i=0;i<store.data.items.length;i++){
                                                        params.DE_CORRS.push(store.data.items[i].data.DE_CORR);
                                                    }
                                                    
                                                    params['CO_NEGO']=model.data.CO_NEGO;

                                                    if (params['CO_SUCU_CORR'] == '') {
                                                    	params['CO_SUCU_CORR']=model.data.CO_SUCU_CORR;
                                                    }
                                                    if (params['CO_CLIE'] == '') {
                                                    	params['CO_CLIE']=model.data.CO_CLIE;
                                                    }

                                                    window.disable();
                                                    model.save({
                                                        params:fnc.getParamsSendPackage(params,varSendPackage,package),
                                                        success:function(){
                                                            window.close();
                                                            storeData.load();
                                                            
                                                            //Mensaje donde el usuario ingresarara informacion para el historico del negocio
                                                            var win=fnc.createWindowModal({
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
                                                                                            params.CO_NEGO=obj.CO_NEGO;
                                                                                            params[varSendPackage]=package;
                                                                                            var model=new Desktop.Facturacion.Models.NEGO_HISTORICO(form.getFieldValues());
                                                                                            model.save({params:params});
                                                                                            form.reset();
                                                                                            win.close();
                                                                                        }
                                                                                    }
                                                                                },{
                                                                                    text: 'Cancelar',
                                                                                    handler: function() {
                                                                                        win.close();
                                                                                    }
                                                                                }
                                                                            ] 
                                                                        }
                                                                    ]
                                                                });
                                                                win.show();
                                                        }
                                                    });
                                                //}
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
                                    width:500,
                                    height:450,
                                    title:'Editar ' + obj.CO_NEGO,
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

            //DESCARGAR NEGOCIOS PENDIENTES
            function verNegociosPendientes(){
                settings.Permissions.varSendPackage=varSendPackage;
                var storeNego=new Desktop.Facturacion.Stores.NEGO();
                fnc.addParameterStore(storeNego,varSendPackage,package);
                fnc.setApiProxyStore(storeNego,{read:'selectNegociosPendientes/'});
                storeNego.load();
                 
                var winRpta=fnc.createWindowModal({
                    title:'Resultado de carga',
                    items:[                       
                        {
                            xtype:'grid',
                            layout:'fit',
                            store:storeNego,
                            columns:[
//                                { text: 'Negocio',  renderer:function(a,b,c){return c.data.CO_NEGO;},width:300}
                                {text:'Negocio',dataIndex:'CO_NEGO',width:150},
                                {text:'Producto',dataIndex:'DESC_PROD_PADRE',width:150}
//                                {text:'Negocio',dataIndex:'negocio',width:380}
                            ],
                            tbar:[
                                {
                                    id:'comboNegocioPendiente', 
                                    xtype:'combobox',                
                                    fieldLabel:'Producto',                          
                                    name:'COD_PROD_PADRE',
                                    displayField:'DESC_PROD_PADRE',
                                    valueField:'COD_PROD_PADRE',
                                    store:storeProd_Padre,
                                    allowBlank:false,
                                    editable:false,
                                    listeners:{
                                           select:function( combo, record, eOpts ){
                                               
                                              var data=record[0].data.DESC_PROD_PADRE;
                                                                                                                                                
                                                storeNego.filter('DESC_PROD_PADRE',data);                                                                                                                                                  
                                           }
                                          
                                    }
                                },
                                {
                                    text:'Exportar',
                                    handler:function(){
                                        var grid=this.up('grid');
                                        fnc.export.gridToExcel2(grid);
                                    }
                                }
                            ]                                                                                    
                        }
                    ]                                                                            
                });
                winRpta.show();
            }
            //CAMBIAR MONEDA DEL NEGOCIO
            function cambiarMonedaNegocio(obj){
                settings.Permissions.varSendPackage=varSendPackage;
                fnc.load.script({
                    url:'panels/NEGO/CambiarMoneda.js',
                    onLoad:function(){
                        var panel=Ext.create("Desktop.Facturacion.Panels.NEGO.CambiarMoneda");
                        panel.packageParent={package:package,varSend:varSendPackage};
                        panel.loadStructure(panel,obj.CO_NEGO,objDesktop);
                        var window=fnc.createWindow(objDesktop,{
                            width:900,
                            height:400,
                            title:'Neg. ' + obj.CO_NEGO+ ' (Cambiar Moneda)',
                            items:[panel]
                        });
                        window.show();                      
                    }
                });
            }
            //VER SUCURSALES
            function verSucursales(obj){
                settings.Permissions.varSendPackage=varSendPackage;
                fnc.load.script({
                    url:'panels/NEGO/VerSucursales.js',
                    onLoad:function(){
                        var panel=Ext.create("Desktop.Facturacion.Panels.NEGO.VerSucursales");
                        panel.loadStructure(panel,obj.CO_NEGO,obj.PROD.CO_PROD,objDesktop,package);
                        var window=fnc.createWindow(objDesktop,{
                            width:900,
                            height:400,
                            title:'Neg. ' + obj.CO_NEGO+ ' (Editar)',
                            items:[panel]
                        });
                        window.show();                      
                    }
                }); 
                /*
                var window=fnc.createWindowModal(objDesktop,{
                    width:400,
                    height:380,
                    title:'Editar ' + obj.CO_NEGO,
                    renderTo:Ext.getBody(),
                    items:[panelForm]
                });
                window.show(); */
            }
            
            //DESACTIVACION MASIVA
            function cargarDesactivacionMasiva(){
                settings.Permissions.varSendPackage=varSendPackage;
                var storeMoti=new Desktop.Facturacion.Stores.MOTI_DESC();
                fnc.addParameterStore(storeMoti,varSendPackage,package);
                var grid=Ext.create('Ext.grid.Panel',{
                    layout:'fit',
                    store: null,
                    columns:[
                    {text:'Resultados',dataIndex:'resultado',width:500}
                    ],
                    tbar:[
                        {
                            text:'Exportar',
                            handler:function(){
                                var grid=this.up('grid');
                                fnc.export.gridToExcel(grid);
                            }
                        }
                    ]                                                                                    
                });
                var win=fnc.createWindowModal({
                        title:'Desactivación Masiva',
                        //height:150,
                        width: 600,
                        tbar: [
                        {
                            xtype: 'button',
                            text: "Cargar Sucursales",
                            tooltip: "Cargar Sucursales",
                            iconCls: fnc.icons.class.edit(),
                            listeners:{
                                click:function(btn,e,opts){
                                    var win=fnc.createWindowModal({
                                        height:250,
                                        title:'Cargar Sucursales',
                                        items:[
                                            {
                                                xtype:'form',
                                                items:[
                                                    {
                                                        id:'comboDesactivacion', 
                                                        xtype:'combobox',                
                                                        fieldLabel:'Producto',
                                                        labelWidth: 120,
                                                        name:'COD_PROD_PADRE',
                                                        displayField:'DESC_PROD_PADRE',
                                                        valueField:'COD_PROD_PADRE',
                                                        store:storeProd_Padre,
                                                        allowBlank:false,
                                                        editable:false
                                                    },
                                                    {
                                                        xtype:'combobox',
                                                        fieldLabel:'Motivo Desactivación',
                                                        labelWidth: 120,
                                                        store:storeMoti,
                                                        displayField: 'NO_MOTI_DESC',
                                                        valueField: 'CO_MOTI_DESC',
                                                        name: 'CO_MOTI_DESC',
                                                        allowBlank: false,
                                                        editable:false
                                                    },
                                                    {
                                                        xtype: 'filefield',
                                                        name: 'FL_SUCU',
                                                        fieldLabel: 'Archivo de Sucursales',
                                                        labelWidth: 120,
                                                        msgTarget: 'side',
                                                        allowBlank: false,
                                                        anchor: '100%',
                                                        buttonText: '::'
                                                    }                                                
                                                ],
                                                buttons:[
                                                    {
                                                        text: 'Cargar',
                                                        formBind: true, //only enabled once the form is valid
                                                        disabled: true,
                                                        handler: function() {
                                                            var form = this.up('form').getForm();
                                                            if (form.isValid()) {
                                                                form.submit({
                                                                    url: 'ajax/NEGO_SUCU/upload/',
                                                                    waitMsg: 'Desactivando sucursales',
                                                                    success: function(fp, o) {
                                                                        if (o.result){
                                                                            var data=[];
                                                                            for(var i=0;i<o.result.data.length;i++)
                                                                                data.push({resultado:o.result.data[i]});
                                                                            grid.setStore(new Ext.data.Store({
                                                                                fields:['resultado'],
                                                                                data:data,
                                                                            }));
                                                                            
                                                                            win.close();
                                                                        }                                                                    
                                                                    }
                                                                });
                                                            }
                                                        }
                                                    }
                                                ]
                                            }
                                        ]
                                    });
                                    win.show();
                                }
                            }
                        }
                    ],  
                    items:[
                        grid
                    ] 
                    });
                    win.show();
                
            }
        });
    });
});