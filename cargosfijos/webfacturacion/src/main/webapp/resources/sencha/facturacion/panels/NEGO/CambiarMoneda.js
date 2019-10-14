(function(){    
    var packageBase="Desktop.Facturacion.Panels.NEGO.CambiarMoneda";
    var settingsLocal=window.settings.real.get();
    var varSendPackage=settingsLocal.Permissions.varSendPackage;
    var AJAX_URI=settingsLocal.AJAX_URI;
    
    Ext.define(packageBase, {
        extend:'Ext.panel.Panel',
        alias:'widget.Desktop.Facturacion.Panels.NEGO.CambiarMoneda',
        overflowY:'scroll',
        defaults: {
           anchor: '100%'
        },  
        createViewsSucursales:function(desktop){

            var panel=this;
            panel.disable();
            panel.removeAll();            
            //panel.NEGO            
            panel.stores.nego_sucu.load({
                params:{CO_NEGO:panel.NEGO.CO_NEGO},
                callback:function(a,b,c){
                    if(c){ 
                        
                        var relacionSucursales=panel.stores.nego_sucu.data;
                        var CO_MONE_FACT_NEW;
                        var storeMoneFact=new Desktop.Facturacion.Stores.MONE_FACT();
                        fnc.addParameterStore(storeMoneFact,varSendPackage,panel.package);
                       
                        var combobox_moneda=Ext.create('Ext.form.field.ComboBox',{
                                fieldLabel:'Nueva Moneda Facturación',
                                store:storeMoneFact,
                                displayField: 'NO_MONE_FACT',
                                valueField: 'CO_MONE_FACT',
                                name: 'CO_MONE_FACT',
                                allowBlank: false,
                                editable:false,
                                readOnly:true
                        });
                        if (panel.NEGO.CO_MONE_FACT==1){
                            CO_MONE_FACT_NEW=2;
                        } else {
                            CO_MONE_FACT_NEW=1;
                        }
                        combobox_moneda.setValue(CO_MONE_FACT_NEW);
                        var field_producto=Ext.create('Ext.form.field.Text',{
                                fieldLabel: 'Producto',
                                name: 'NO_PROD',
                                value: panel.NEGO.PROD.NO_PROD,
                                editable:false,
                                readOnly:true,
                                width:400
                        }); 
                        
                        panel.add({
                            cls:'panel_descripcion_negocio',
//                            xtype:'fieldset',
                            title:'Descripción Negocio',
                            layout:'column',                             
                            margin: '0 0 10 0', 
                            items:[
                                 {
                                    columnWidth: .5,
                                    items:[
                                        combobox_moneda
                                    ]
                                 },
                                 {
                                    columnWidth: .5,
                                    items:[
                                        field_producto
                                    ] 
                                 }
                             ]
                        });
                        
                        
                        var nego_sucu=relacionSucursales.getAt(0).data.CO_NEGO_SUCU;
                        var mostrarSucu=false;
                        var titlePanel="";
                        var data=[];
                        var array_store=[];
                        var contador_sucu=0;
                        for(var i=0;i<relacionSucursales.length;i++){
                            (function(record){
                                var relacionSucursal=record.data;
                                if (relacionSucursal.CO_NEGO_SUCU==nego_sucu){
                                    data.push({
                                        CO_NEGO_SUCU:relacionSucursal.CO_NEGO_SUCU,
                                        CO_NEGO_SUCU_PLAN_SS:relacionSucursal.CO_NEGO_SUCU_PLAN_SS,
                                        CO_PLAN_SS:relacionSucursal.CO_PLAN_SS,
                                        TIPO:relacionSucursal.TIPO,
                                        NOMBRE:relacionSucursal.NOMBRE,
                                        CO_MONE_FACT:relacionSucursal.CO_MONE_FACT,
                                        NO_MONE_FACT:relacionSucursal.NO_MONE_FACT,
                                        IM_MONTO:relacionSucursal.IM_MONTO
                                    });
                                } else {
                                    mostrarSucu=true;
                                }
                                nego_sucu=relacionSucursal.CO_NEGO_SUCU;
                                if (mostrarSucu){
                                    mostrarSucu=false;
                                    var grid=Ext.create('Ext.grid.Panel',{ 
                                        name:'gridSucu_'+contador_sucu,
                                        layout:'fit',
                                        columns:[
                                            {text:'Tipo',dataIndex:'TIPO'},
                                            {text:'Nombre',width:600,dataIndex:'NOMBRE'},
                                            {text:'Tipo Moneda',width:100,dataIndex:'NO_MONE_FACT'},
                                            {text:'Monto',width:100,dataIndex:'IM_MONTO'}
                                        ],
                                        store:null,
                                        tbar:[
                                            {
                                                icon:fnc.images.uri.edit(),
                                                text:'Cambiar Plan',
                                                cls:'fac-btn fac-btn-danger',
                                                handler:function(){
                                                    var grid=this.up('grid');
                                                    var selectedPlan=grid.getView().getSelectionModel().getSelection();
                                                    if(selectedPlan.length==1 && selectedPlan[0].data.TIPO=='P'){
                                                        var dataPlan=selectedPlan[0].data;
                                                        
                                                        var storePlan=new Desktop.Facturacion.Stores.PLAN();
                                                        fnc.setApiProxyStore(storePlan,{read:'selectByPRODAndMONE/'})
                                                        fnc.addParameterStore(storePlan,varSendPackage,panel.package);
                                                        fnc.addParameterStore(storePlan,"CO_PROD",panel.NEGO.CO_PROD);
                                                        fnc.addParameterStore(storePlan,"CO_MONE_FACT",CO_MONE_FACT_NEW);
                                                        var gridPlanes=Ext.create('Ext.grid.Panel',{
                                                           store: storePlan,
                                                           columns:[
                                                               { text: 'Nombre',  dataIndex: 'NO_PLAN',width:400 },                                               
                                                               { text: 'Bajada',  dataIndex: 'DE_VELO_BAJA',width:80,filter: {type: 'string'}},
                                                               { text: 'Subida',  dataIndex: 'DE_VELO_SUBI',width:80,filter: {type: 'string'}},
                                                               { text: 'Medio',  dataIndex: 'CO_PLAN_MEDI_INST', renderer:function(a,b,c){return c.data.PLAN_MEDI_INST.CO_PLAN_MEDI_INST+" - "+c.data.PLAN_MEDI_INST.NO_PLAN_MEDI_INST},width:90,filter: {type: 'int'}},
                                                               { text: 'Moneda',  dataIndex: 'CO_MONE_FACT', renderer:function(a,b,c){return c.data.MONE_FACT.CO_MONE_FACT+" - "+c.data.MONE_FACT.NO_MONE_FACT},width:90,filter: {type: 'int'}},
                                                               { text: 'Monto',  dataIndex: 'IM_MONTO',width:80,filter: {type: 'float'}},
                                                           ],
                                                           features:[
                                                               {
                                                                    ftype: 'filters',
                                                                    encode: false, // json encode the filter query
                                                                    local: true, // defaults to false (remote filtering)
                                                                    filters: [{
                                                                        type: 'boolean',
                                                                        dataIndex: 'visible'
                                                                    }] 
                                                                }
                                                            ],
                                                           //plugins: 'gridfilters',
                                                           listeners:{
                                                               itemdblclick: function (view, record, item, index, e, eOpts ){
                                                                    var selected=view.getSelectionModel().getSelection();
                                                                    if(selected.length==1){                      
                                                                        var data=selected[0].data;
                                                                        // ACTUALIZAR EL VALOR DEL REGISTRO DEL GRID
                                                                        dataPlan.CO_PLAN_SS=data.CO_PLAN;
                                                                        dataPlan.IM_MONTO=data.IM_MONTO;
                                                                        dataPlan.NOMBRE=data.NO_PLAN;
                                                                        dataPlan.CO_MONE_FACT=data.MONE_FACT.CO_MONE_FACT;
                                                                        dataPlan.NO_MONE_FACT=data.MONE_FACT.NO_MONE_FACT;
                                                                        selectedPlan[0].store.load()
                                                                        window.close();
                                                                    }
                                                               }

                                                           }
                                                       });
                                                        var window=fnc.createWindowModal(desktop,{
                                                            width:800,
                                                            items:[
                                                                gridPlanes
                                                            ],
                                                            renderTo:Ext.getBody(),
                                                        });
                                                        window.show();
                                                    } else {
                                                        fnc.createMsgBoxError({msg:'Debe seleccionar un plan.'})
                                                    }
                                                }
                                            },
                                            {
                                                icon:fnc.images.uri.edit(),
                                                text:'Cambiar Servicio Suplementario',
                                                cls:'fac-btn fac-btn-warning',
                                                handler:function(){
                                                    var grid=this.up('grid');
                                                    var selectedServSupl=grid.getView().getSelectionModel().getSelection();
                                                    if(selectedServSupl.length==1 && selectedServSupl[0].data.TIPO=='SS'){
                                                        var dataServSupl=selectedServSupl[0].data;
                                                        
                                                        var storeSERV_SUPL=new Desktop.Facturacion.Stores.SERV_SUPL();
                                                        fnc.setApiProxyStore(storeSERV_SUPL,{read:'selectByPROD/'})
                                                        fnc.addParameterStore(storeSERV_SUPL,varSendPackage,panel.package);
                                                        fnc.addParameterStore(storeSERV_SUPL,"CO_PROD",panel.NEGO.CO_PROD);
                                                        fnc.addParameterStore(storeSERV_SUPL,"CO_MONE_FACT",CO_MONE_FACT_NEW);
                                                        
                                                        var gridServSupl=Ext.create('Ext.grid.Panel',{
                                                            store: storeSERV_SUPL,
                                                            columns:[
                                                                { text: 'Codigo',  dataIndex: 'CO_SERV_SUPL',width:50,filter: {type: 'string'} },
                                                                { text: 'Nombre',  dataIndex: 'NO_SERV_SUPL',width:400,filter: {type: 'string'} },
                                                                { text: 'Monto',  dataIndex: 'IM_MONTO',width:80,filter: {type: 'string'} },
                                                                { text: 'Detracción',  dataIndex: 'ST_AFEC_DETR',xtype: 'booleancolumn',trueText: 'Si',falseText: 'No',width:80,filter: {type: 'boolean'}  },
                                                            ],
                                                             features:[
                                                                 {
                                                                      ftype: 'filters',
                                                                      encode: false, // json encode the filter query
                                                                      local: true, // defaults to false (remote filtering)
                                                                 }
                                                             ], 
                                                           //plugins: 'gridfilters',
                                                           listeners:{
                                                               itemdblclick: function (view, record, item, index, e, eOpts ){
                                                                    var selected=view.getSelectionModel().getSelection();
                                                                    if(selected.length==1){                      
                                                                        var data=selected[0].data;
                                                                        // ACTUALIZAR EL VALOR DEL REGISTRO DEL GRID
                                                                        dataServSupl.CO_PLAN_SS=data.CO_SERV_SUPL;
                                                                        dataServSupl.IM_MONTO=data.IM_MONTO;
                                                                        dataServSupl.NOMBRE=data.NO_SERV_SUPL;
                                                                        dataServSupl.CO_MONE_FACT=data.MONE_FACT.CO_MONE_FACT;
                                                                        dataServSupl.NO_MONE_FACT=data.MONE_FACT.NO_MONE_FACT;
                                                                        selectedServSupl[0].store.load()
                                                                        window.close();
                                                                    }
                                                               }

                                                           }
                                                       });
                                                        var window=fnc.createWindowModal(desktop,{
                                                            width:800,
                                                            items:[
                                                                gridServSupl
                                                            ],
                                                            renderTo:Ext.getBody(),
                                                        });
                                                        window.show();
                                                    } else {
                                                        fnc.createMsgBoxError({msg:'Debe seleccionar un servicio suplementario.'})
                                                    }
                                                }
                                            }
                                        ]
                                    });

                                    var store=Ext.create('Ext.data.Store',{
                                       fields:[
                                           {name:'CO_NEGO_SUCU'},
                                           {name:'CO_NEGO_SUCU_PLAN_SS'},
                                           {name:'CO_PLAN_SS'},
                                           {name:'TIPO'},
                                           {name:'NOMBRE'},
                                           {name:'CO_MONE_FACT'},
                                           {name:'NO_MONE_FACT'},
                                           {name:'IM_MONTO'}
                                       ],
                                       data:data
                                    });
                                    grid.setStore(store);
                                    array_store.push(store);
                                    
//                                    titlePanel=relacionSucursal.getAt(i-1).data.DE_DIRE+' ('+relacionSucursal.getAt(i-1).NO_DEPA+" - "+relacionSucursal.getAt(i-1).NO_PROV+" - "+relacionSucursal.getAt(i-1).NO_DIST+')';
                                    panel.add({
                                        title:titlePanel,
                                        cls:'panel_versucursales',
                                        //flex: 1,                               
                                        margin: '0 0 10 0', 
//                                        xtype:'fieldset',
//                                        title:titlePanel,
                                        items:[
                                            grid
                                        ]
                                    });
                                    data=[];
                                    data.push({
                                        CO_NEGO_SUCU:relacionSucursal.CO_NEGO_SUCU,
                                        CO_NEGO_SUCU_PLAN_SS:relacionSucursal.CO_NEGO_SUCU_PLAN_SS,
                                        CO_PLAN_SS:relacionSucursal.CO_PLAN_SS,
                                        TIPO:relacionSucursal.TIPO,
                                        NOMBRE:relacionSucursal.NOMBRE,
                                        CO_MONE_FACT:relacionSucursal.CO_MONE_FACT,
                                        NO_MONE_FACT:relacionSucursal.NO_MONE_FACT,
                                        IM_MONTO:relacionSucursal.IM_MONTO
                                    });
                                    contador_sucu++;
                                }
                                titlePanel=relacionSucursal.DE_DIRE+' ('+relacionSucursal.NO_DEPA+" - "+relacionSucursal.NO_PROV+" - "+relacionSucursal.NO_DIST+')';
                            })(relacionSucursales.getAt(i));
                        }
                        
                        if (!mostrarSucu && relacionSucursales.length>0){
                            var grid=Ext.create('Ext.grid.Panel',{ 
                                name:'gridSucu_'+contador_sucu,
                                layout:'fit',
                                columns:[
                                    {text:'Tipo',dataIndex:'TIPO'},
                                    {text:'Nombre',width:600,dataIndex:'NOMBRE'},
                                    {text:'Tipo Moneda',width:100,dataIndex:'NO_MONE_FACT'},
                                    {text:'Monto',width:100,dataIndex:'IM_MONTO'}
                                ],
                                store:null,
                                tbar:[
                                    {
                                        icon:fnc.images.uri.edit(),
                                        text:'Cambiar Plan',
                                        cls:'fac-btn fac-btn-danger',
                                        handler:function(){
                                            var grid=this.up('grid');
                                            var selectedPlan=grid.getView().getSelectionModel().getSelection();
                                            if(selectedPlan.length==1 && selectedPlan[0].data.TIPO=='P'){
                                                var dataPlan=selectedPlan[0].data;

                                                var storePlan=new Desktop.Facturacion.Stores.PLAN();
                                                fnc.setApiProxyStore(storePlan,{read:'selectByPRODAndMONE/'})
                                                fnc.addParameterStore(storePlan,varSendPackage,panel.package);
                                                fnc.addParameterStore(storePlan,"CO_PROD",panel.NEGO.CO_PROD);
                                                fnc.addParameterStore(storePlan,"CO_MONE_FACT",CO_MONE_FACT_NEW);
                                                var gridPlanes=Ext.create('Ext.grid.Panel',{
                                                   store: storePlan,
                                                   columns:[
                                                       { text: 'Nombre',  dataIndex: 'NO_PLAN',width:400 },                                               
                                                       { text: 'Bajada',  dataIndex: 'DE_VELO_BAJA',width:80,filter: {type: 'string'}},
                                                       { text: 'Subida',  dataIndex: 'DE_VELO_SUBI',width:80,filter: {type: 'string'}},
                                                       { text: 'Medio',  dataIndex: 'CO_PLAN_MEDI_INST', renderer:function(a,b,c){return c.data.PLAN_MEDI_INST.CO_PLAN_MEDI_INST+" - "+c.data.PLAN_MEDI_INST.NO_PLAN_MEDI_INST},width:90,filter: {type: 'int'}},
                                                       { text: 'Moneda',  dataIndex: 'CO_MONE_FACT', renderer:function(a,b,c){return c.data.MONE_FACT.CO_MONE_FACT+" - "+c.data.MONE_FACT.NO_MONE_FACT},width:90,filter: {type: 'int'}},
                                                       { text: 'Monto',  dataIndex: 'IM_MONTO',width:80,filter: {type: 'float'}},
                                                   ],
                                                   features:[
                                                       {
                                                            ftype: 'filters',
                                                            encode: false, // json encode the filter query
                                                            local: true, // defaults to false (remote filtering)
                                                            filters: [{
                                                                type: 'boolean',
                                                                dataIndex: 'visible'
                                                            }] 
                                                        }
                                                    ],
                                                   //plugins: 'gridfilters',
                                                   listeners:{
                                                       itemdblclick: function (view, record, item, index, e, eOpts ){
                                                            var selected=view.getSelectionModel().getSelection();
                                                            if(selected.length==1){                      
                                                                var data=selected[0].data;
                                                                // ACTUALIZAR EL VALOR DEL REGISTRO DEL GRID
                                                                dataPlan.CO_PLAN_SS=data.CO_PLAN;
                                                                dataPlan.IM_MONTO=data.IM_MONTO;
                                                                dataPlan.NOMBRE=data.NO_PLAN;
                                                                dataPlan.CO_MONE_FACT=data.MONE_FACT.CO_MONE_FACT;
                                                                dataPlan.NO_MONE_FACT=data.MONE_FACT.NO_MONE_FACT;
                                                                selectedPlan[0].store.load()
                                                                window.close();
                                                            }
                                                       }

                                                   }
                                               });
                                                var window=fnc.createWindowModal(desktop,{
                                                    width:800,
                                                    items:[
                                                        gridPlanes
                                                    ],
                                                    renderTo:Ext.getBody(),
                                                });
                                                window.show();
                                            } else {
                                                fnc.createMsgBoxError({msg:'Debe seleccionar un plan.'})
                                            }
                                        }
                                    },
                                    {
                                        icon:fnc.images.uri.edit(),
                                        text:'Cambiar Servicio Suplementario',
                                        cls:'fac-btn fac-btn-warning',
                                        handler:function(){
                                            var grid=this.up('grid');
                                            var selectedServSupl=grid.getView().getSelectionModel().getSelection();
                                            if(selectedServSupl.length==1 && selectedServSupl[0].data.TIPO=='SS'){
                                                var dataServSupl=selectedServSupl[0].data;

                                                var storeSERV_SUPL=new Desktop.Facturacion.Stores.SERV_SUPL();
                                                fnc.setApiProxyStore(storeSERV_SUPL,{read:'selectByPROD/'})
                                                fnc.addParameterStore(storeSERV_SUPL,varSendPackage,panel.package);
                                                fnc.addParameterStore(storeSERV_SUPL,"CO_PROD",panel.NEGO.CO_PROD);
                                                fnc.addParameterStore(storeSERV_SUPL,"CO_MONE_FACT",CO_MONE_FACT_NEW);

                                                var gridServSupl=Ext.create('Ext.grid.Panel',{
                                                    store: storeSERV_SUPL,
                                                    columns:[
                                                        { text: 'Codigo',  dataIndex: 'CO_SERV_SUPL',width:50,filter: {type: 'string'} },
                                                        { text: 'Nombre',  dataIndex: 'NO_SERV_SUPL',width:400,filter: {type: 'string'} },
                                                        { text: 'Monto',  dataIndex: 'IM_MONTO',width:80,filter: {type: 'string'} },
                                                        { text: 'Detracción',  dataIndex: 'ST_AFEC_DETR',xtype: 'booleancolumn',trueText: 'Si',falseText: 'No',width:80,filter: {type: 'boolean'}  },
                                                    ],
                                                     features:[
                                                         {
                                                              ftype: 'filters',
                                                              encode: false, // json encode the filter query
                                                              local: true, // defaults to false (remote filtering)
                                                         }
                                                     ], 
                                                   //plugins: 'gridfilters',
                                                   listeners:{
                                                       itemdblclick: function (view, record, item, index, e, eOpts ){
                                                            var selected=view.getSelectionModel().getSelection();
                                                            if(selected.length==1){                      
                                                                var data=selected[0].data;
                                                                // ACTUALIZAR EL VALOR DEL REGISTRO DEL GRID
                                                                dataServSupl.CO_PLAN_SS=data.CO_SERV_SUPL;
                                                                dataServSupl.IM_MONTO=data.IM_MONTO;
                                                                dataServSupl.NOMBRE=data.NO_SERV_SUPL;
                                                                dataServSupl.CO_MONE_FACT=data.MONE_FACT.CO_MONE_FACT;
                                                                dataServSupl.NO_MONE_FACT=data.MONE_FACT.NO_MONE_FACT;
                                                                selectedServSupl[0].store.load()
                                                                window.close();
                                                            }
                                                       }

                                                   }
                                               });
                                                var window=fnc.createWindowModal(desktop,{
                                                    width:800,
                                                    items:[
                                                        gridServSupl
                                                    ],
                                                    renderTo:Ext.getBody(),
                                                });
                                                window.show();
                                            } else {
                                                fnc.createMsgBoxError({msg:'Debe seleccionar un servicio suplementario.'})
                                            }
                                        }
                                    }
                                ]
                            });

                            var store=Ext.create('Ext.data.Store',{
                               fields:[
                                   {name:'CO_NEGO_SUCU'},
                                   {name:'CO_NEGO_SUCU_PLAN_SS'},
                                   {name:'CO_PLAN_SS'},
                                   {name:'TIPO'},
                                   {name:'NOMBRE'},
                                   {name:'CO_MONE_FACT'},
                                   {name:'NO_MONE_FACT'},
                                   {name:'IM_MONTO'}
                               ],
                               data:data
                            });
                            grid.setStore(store);
                            array_store.push(store);
                            
                            panel.add({
                                title:titlePanel,
                                cls:'panel_versucursales',
                                //flex: 1,                               
                                margin: '0 0 10 0', 
//                                xtype:'fieldset',
//                                title:titlePanel,
                                items:[
                                    grid
                                ]
                            });
                        }
                        
                    }
                }
            });
            
            panel.enable();
            
        },
        initComponent:function(){
            this.callParent();            
        },
        buttons: 
        [{
            text: 'Cancelar',
            handler: function() {
                this.up('panel').up().close();
            }
        }, {
            text: 'Agregar',
            //formBind: true, //only enabled once the form is valid
            //disabled: true,
            handler: function() {
                var panel=this.up('panel');
                var sgteSucu=true;
                var contador_sucu=0;
                var todoOk=true;
                var monedaOK=true;
                var ARR_CO_NEGO_SUCU_PLAN_SS=[];
                var ARR_CO_NEGO_SUCU=[];
                var ARR_CO_PLAN_SS=[];
                var ARR_TIPO=[];
                var CO_MONE_FACT=panel.query('combobox[name=CO_MONE_FACT]')[0].value;
                do{
                    if (panel.down('[name=gridSucu_'+contador_sucu+']')!=null){
                        var gridSucu=panel.down('[name=gridSucu_'+contador_sucu+']');
                        var storeSucu=gridSucu.getStore();
                        for(var i=0;i<storeSucu.data.items.length;i++){
                            var r=storeSucu.data.items[i].data;
                            if (r.CO_NEGO_SUCU && r.CO_PLAN_SS && r.CO_NEGO_SUCU_PLAN_SS){
                                if (r.CO_MONE_FACT==CO_MONE_FACT){
                                    ARR_CO_NEGO_SUCU_PLAN_SS.push(r.CO_NEGO_SUCU_PLAN_SS);
                                    ARR_CO_NEGO_SUCU.push(r.CO_NEGO_SUCU);
                                    ARR_CO_PLAN_SS.push(r.CO_PLAN_SS);
                                    ARR_TIPO.push(r.TIPO);
                                } else {
                                    monedaOK=false;
                                    sgteSucu=false;
                                }
                            } else {
                                todoOk=false;
                                sgteSucu=false;
                            }
                        }
                    } else {
                        sgteSucu=false;
                    }
                    contador_sucu++;
                } while(sgteSucu);
                
                if (todoOk && monedaOK){
                    var params={CO_NEGO:panel.NEGO.CO_NEGO,CO_MONE_FACT:CO_MONE_FACT,ARR_CO_NEGO_SUCU_PLAN_SS:ARR_CO_NEGO_SUCU_PLAN_SS,ARR_CO_NEGO_SUCU:ARR_CO_NEGO_SUCU,ARR_CO_PLAN_SS:ARR_CO_PLAN_SS,ARR_TIPO:ARR_TIPO};
                    
                    params[varSendPackage]=panel.package;
                    Ext.Ajax.request({
                        method:'POST',
                        params:params,
                        url: AJAX_URI+'NEGO_SUCU/CAMB_MONE/',
                        success: function(response, opts) {
                            var json=fnc.responseExtAjaxRequestMsjError(response);
                            if(json.success){
                                fnc.createMsgBoxSuccess({msg:'Se realizo el cambio de moneda de manera satisfactoria.'});
                            }
                        }
                    }); 
                }else{
                    if (!todoOk){
                        fnc.createMsgBoxError({msg:'Existe parametros ingresados de manera incorrecta.'});
                    } else {
                        if (!monedaOK){
                            fnc.createMsgBoxError({msg:'Falta cambiar de moneda a aglunos planes/servicios suplementarios.'});
                        }
                    }
                }
            }
        }],
        items:[
            {
                title: 'Panel 1',
                flex: 1,
                margin: '0 0 10 0',
                html: 'Cargando...'
            } ],
        loadStructure:function(panel,CO_NEGO,desktopPar){
            panel.package=panel.packageParent.package;
//            varSendPackage=panel.packageParent.varSendPackage;
            fnc.load.ModelsAndStores(["PROD","SUCU","PLAN","SERV_SUPL","NEGO","NEGO_SUCU","CIER","NEGO_SUCU_PLAN","NEGO_SUCU_SERV_SUPL","NEGO_SUCU_SERV_UNIC"],function(){
                panel.stores={
                    nego:new Desktop.Facturacion.Stores.NEGO(),
                    nego_sucu:new Desktop.Facturacion.Stores.NEGO_SUCU()                    
                };
                for(var k in panel.stores){
                    fnc.addParameterStore(panel.stores[k],varSendPackage,panel.package);
                } 
                fnc.setApiProxyStore(panel.stores.nego_sucu,{read:'selectPlanes_ServSuplByNego/'});
                
                panel.stores.nego.load({params:{id:CO_NEGO},callback:function(){                     
                    var desktop=desktopPar;
                    var nego=panel.stores.nego.data.items;
                    if(nego.length==1){
                        nego=nego[0].data;
                        panel.NEGO=nego;
                        panel.createViewsSucursales(desktop);
                        fnc.load.scripts([
                            "widgets/selectPlanByProducto.js",
                            "widgets/selectServiciosSuplementariosByProducto.js",
                            "widgets/selectServiciosUnicosByProducto.js"
                        ]);
                        
                    }
                }});
            });            
        }
    });
    
})();