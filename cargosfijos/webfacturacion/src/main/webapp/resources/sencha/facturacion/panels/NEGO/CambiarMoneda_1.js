(function(){    
    //var package="Desktop.Facturacion.Panels.NEGO.Administrar";
    var packageBase="Desktop.Facturacion.Panels.NEGO.CambiarMoneda";
    var settingsLocal=window.settings.real.get();
    var varSendPackage=settingsLocal.Permissions.varSendPackage;
    var AJAX_URI=settingsLocal.AJAX_URI;
    
    Ext.define(packageBase, {
        extend:'Ext.panel.Panel',
        alias:'widget.Desktop.Facturacion.Panels.NEGO.CambiarMoneda',
        overflowY:'scroll',
        defaults: {
            /*
            frame: true,
            collapsible: true,
            collapsed:true,
            bodyPadding: 5,
            flex: 1,
            margin: 5,              
            */
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
                        var realacionSucursales=panel.stores.nego_sucu.data;
                        var storeMoneFact=new Desktop.Facturacion.Stores.MONE_FACT();
                        fnc.addParameterStore(storeMoneFact,varSendPackage,panel.package);
                        
                        panel.add({
                             xtype:'fieldset',
                             title:'Negocio',
                             items:[
                                 {
                                     xtype:'combobox',
                                     fieldLabel:'Moneda Facturación',
                                     name:'CO_MONE_FACT',
                                     displayField:'NO_MONE_FACT',
//                                     valueField:panel.NEGO.CO_MONE_FACT,
                                     valueField:"CO_MONE_FACT",
                                     store:storeMoneFact,
                                     allowBlank:false,
                                     editable:false,                
                                 }
                             ]
                        });
                        
                        for(var i=0;i<realacionSucursales.length;i++){
                            (function(record){
                                var relacionSucursal=record.data;
                                var sucursal=relacionSucursal.SUCU;
                                var distrito=sucursal.DIST;
                                var provincia=distrito.PROV;
                                var departamento=provincia.DEPA;
                                var titlePanel="";
                                var direccionUbicacionSucursal="";
                                
                                var storePlanes=new Desktop.Facturacion.Stores.NEGO_SUCU_PLAN();
                                fnc.setApiProxyStore(storePlanes,{read:'selectActivoByNEGO_SUCU/'});
                                fnc.addParameterStore(storePlanes,varSendPackage,panel.package);
                                fnc.addParameterStore(storePlanes,"CO_NEGO_SUCU",relacionSucursal.CO_NEGO_SUCU);
                                
                                var storeServiciosSuplementarios=new Desktop.Facturacion.Stores.NEGO_SUCU_SERV_SUPL();
                                fnc.setApiProxyStore(storeServiciosSuplementarios,{read:'selectByNEGO_SUCU/'});
                                fnc.addParameterStore(storeServiciosSuplementarios,varSendPackage,panel.package);
                                fnc.addParameterStore(storeServiciosSuplementarios,"CO_NEGO_SUCU",relacionSucursal.CO_NEGO_SUCU);
                                
                                direccionUbicacionSucursal=sucursal.DE_DIRE+' ('+departamento.NO_DEPA+" - "+provincia.NO_PROV+" - "+distrito.NO_DIST+')';
                                titlePanel+=fnc.shortcodes.getSuspendido(relacionSucursal.isSuspendido)+fnc.shortcodes.getMudado(relacionSucursal.isMudado)+fnc.shortcodes.getDesactivado(relacionSucursal.isDesactivado)+ ' OIT '+(relacionSucursal.ST_SOAR_INST?'SOARC':'')+' : '+(relacionSucursal.CO_OIT_INST?relacionSucursal.CO_OIT_INST:'');
                                titlePanel+=';CIRCUITO : '+(relacionSucursal.CO_CIRC?relacionSucursal.CO_CIRC:'');
                                titlePanel+=';'+direccionUbicacionSucursal;
                                storePlanes.load();
//                                storeServiciosSuplementarios.load();
                                var itemsServSupl=[];
                                var itemsPlanes=[];
                                console.log("cantidad: "+storePlanes.getCount()>0);
                                if (storePlanes.getCount()>0){
                                    console.log("planes mayor q cero");
                                    itemsPlanes=[
                                        {
                                            xtype:'grid',
                                            store:storePlanes,
                                            columns:[
                                                {text:'OIT INST.',renderer:function(a,b,c){return c.data.CO_OIT_INST?((c.data.ST_SOAR_INST?'O':'S')+c.data.CO_OIT_INST):"";}},
                                                {text:'OIT DESI.',renderer:function(a,b,c){return c.data.CO_OIT_DESI?((c.data.ST_SOAR_DESI?'O':'S')+c.data.CO_OIT_DESI):"";}},
                                                {text:'Plan',dataIndex:'NO_PLAN'},
                                                {text:'Moneda',dataIndex:'PLAN.MONE_FACT.NO_MONE_FACT'},
                                                {text:'Monto',dataIndex:'IM_MONTO'},
                                                {text:'Veloc. Bajada',dataIndex:'DE_VELO_BAJA'},
                                                {text:'Veloc. Subida',dataIndex:'DE_VELO_SUBI'},
                                                {text:'Activación',dataIndex:'FE_INIC',xtype: 'datecolumn', format: 'd/m/Y'},
                                                {text:'Desactivación',dataIndex:'FE_FIN',xtype: 'datecolumn', format: 'd/m/Y'},
                                                {text:'Medio Instalación',renderer:function(a,b,c){return (c.data.PLAN.PLAN_MEDI_INST.CO_PLAN_MEDI_INST+" - "+c.data.PLAN.PLAN_MEDI_INST.NO_PLAN_MEDI_INST);}},
                                            ],
                                            tbar:[
                                            {
                                                icon:fnc.images.uri.edit(),
                                                text:'Cambiar Plan',
                                                cls:'fac-btn fac-btn-danger',                                                        
                                                handler:function(){                                                                
                                                    var grid=this.up('grid');
                                                    var selected=grid.getView().getSelectionModel().getSelection();  
                                                    if(selected.length==1){  
                                                        var data=selected[0].data;
                                                        if (data.FE_FIN==null){
                                                            var date = new Date();
                                                            var firstDay = new Date(date.getFullYear(), date.getMonth(), 1);
                                                            var w=fnc.createWindowModal({
                                                                    title:'Cambiar de Plan',
                                                                    height:300,
                                                                    width:500,
                                                                    items:[
                                                                        Ext.create("Ext.form.Panel",{
                                                                            items:[
                                                                                    {
                                                                                            xtype:'Desktop.Facturacion.Widgets.selectPlanByProducto',
                                                                                            anchor:'100%',
                                                                                            CO_PROD:panel.NEGO.CO_PROD,
                                                                                            CO_MONE_FACT:panel.NEGO.CO_MONE_FACT,
                                                                                            packageParent:{package:panel.package,varSend:varSendPackage},
                                                                                            desktop:desktop,
                                                                                            allowBlank: false
                                                                                    },                                                                                        
                                                                                    Ext.create("Ext.form.field.Date",{
                                                                                            anchor:'100%',
                                                                                            name:'FE_INIC',
                                                                                            fieldLabel:'Fecha activación',
                                                                                            maxValue: Ext.Date.add(firstDay,Ext.Date.DAY,5),
                                                                                            allowBlank: false
                                                                                    }),
                                                                                    {
                                                                                            xtype:'checkbox',
                                                                                            name:'ST_SOAR_INST',
                                                                                            fieldLabel:'OIT SOARC',
                                                                                            allowBlank: true
                                                                                    },
                                                                                    {
                                                                                            xtype:'numberfield',
                                                                                            name:'CO_OIT_INST',
                                                                                            fieldLabel:'OIT',
                                                                                            allowBlank: true
                                                                                    }
                                                                            ],
                                                                            buttons:[
                                                                                    {
                                                                                            text:'Guardar',
                                                                                            formBind: true, //only enabled once the form is valid
                                                                                            handler:function(){
                                                                                                    var form = this.up('form').getForm();
                                                                                                    if (form.isValid()) {
                                                                                                            var values=form.getFieldValues();
                                                                                                            if (data.FE_INIC<values.FE_INIC){
                                                                                                                    var m=new Desktop.Facturacion.Models.NEGO_SUCU_PLAN(values);
                                                                                                                    values.CO_NEGO_SUCU=relacionSucursal.CO_NEGO_SUCU;
                                                                                                                    fnc.setApiProxyModel(m,{create:'cambiar_plan/',update:'cambiar_plan/'});
                                                                                                                    m.save({
                                                                                                                            params:m.data,
                                                                                                                            success: function (record, operation) {
                                                                                                                                    storePlanes.load();
                                                                                                                                    w.close();
                                                                                                                            },
                                                                                                                            failure: function (record, operation) {
                                                                                                                                    fnc.responseEvaluateOperationError(operation);
                                                                                                                            }
                                                                                                                    });
                                                                                                            }else{
                                                                                                                    fnc.createMsgBoxError({msg:'La fecha de desactivación tiene que ser mayor a la de activación.'});
                                                                                                            }
                                                                                                    }
                                                                                            }
                                                                                    },
                                                                                    {
                                                                                            text:'Cancelar',
                                                                                            handler:function(){
                                                                                                    w.close();
                                                                                            }
                                                                                    }
                                                                            ]
                                                                        })
                                                                    ]
                                                            });
                                                            w.show();
                                                        }else{
                                                            fnc.createMsgBoxError({msg:'Este plan esta desactivado.'});
                                                        }
                                                    }
                                                }
                                            }
                                            ]
                                        }
                                     ];
                                } else {
                                    console.log("planes igual q cero");
                                    itemsPlanes=[
                                        {
                                            html: 'No hay planes registrados.'
                                        }
                                    ];
                                }
                                
                                if (storeServiciosSuplementarios.getCount()>0){
                                    itemsServSupl=[
						{
							xtype:'grid',
							store:storeServiciosSuplementarios,
							height: 200,
							width: '100%',
							columns:[
								{text:'OIT INST.',width:'50px',renderer:function(a,b,c){return c.data.CO_OIT_INST?((c.data.ST_SOAR_INST?'O':'S')+c.data.CO_OIT_INST):"";}},
								{text:'OIT DESI.',width:'50px',renderer:function(a,b,c){return c.data.CO_OIT_DESI?((c.data.ST_SOAR_DESI?'O':'S')+c.data.CO_OIT_DESI):"";}},
								{text:'Servicio',width:'14%',dataIndex:'NO_SERV_SUPL'},
								{text:'Moneda',width:'8%',dataIndex:'SERV_SUPL.MONE_FACT.NO_MONE_FACT'},
								{text:'Monto',width:'8%',dataIndex:'IM_MONTO'},
								{text:'Activación',width:'15px',dataIndex:'FE_INIC',xtype: 'datecolumn', format: 'd/m/Y'},
								{text:'Desactivación',width:'15px',dataIndex:'FE_FIN',xtype: 'datecolumn', format: 'd/m/Y'},
								{text:'Detracción',width:'15px',dataIndex:'ST_AFEC_DETR',renderer:function(a,b,c){return c.data.ST_AFEC_DETR?'SI':'NO';}},
							],
							tbar:[
								{
									icon:fnc.images.uri.edit(),
									text:'Desactivar Servicio',
									cls:'fac-btn fac-btn-danger',                                                        
									handler:function(){
										var grid=this.up('grid');
										var selected=grid.getView().getSelectionModel().getSelection();  
										if(selected.length==1){  
											var data=selected[0].data;
											if (data.FE_FIN==null){
												var w=fnc.createWindowModal({
													title:'Desactivar Servicio',
													height:250,
													width:300,
													items:[
														Ext.create("Ext.form.Panel",{
															items:[                                                                                        
																Ext.create("Ext.form.field.Date",{
																	anchor:'100%',
																	name:'FE_FIN',
																	fieldLabel:'Fecha desactivación',
																}),
																{
																	xtype:'checkbox',
																	name:'ST_SOAR_DESI',
																	fieldLabel:'OIT SOARC'
																},
																{
																	xtype:'numberfield',
																	name:'CO_OIT_DESI',
																	fieldLabel:'OIT Desinstalación'
																}
															],
															buttons:[
																{
																	text:'Guardar',
																	handler:function(){
																		var formCmp=this.up('form');
																		var form = formCmp.getForm();
																		if (form.isValid()) {                                                                                            
																			var values=form.getFieldValues();
																			data.FE_FIN=values.FE_FIN;
																			data.ST_SOAR_DESI=values.ST_SOAR_DESI;
																			data.CO_OIT_DESI=values.CO_OIT_DESI;                                                                                                    
																			var m=new Desktop.Facturacion.Models.NEGO_SUCU_SERV_SUPL(data);
																			fnc.setApiProxyModel(m,{create:'desactivar_servicio/',update:'desactivar_servicio/'});                                                                                                    
																			m.save({
																				params:m.data,
																				success: function (record, operation) {
																					storeServiciosSuplementarios.load();
																					w.close();
																				},
																				failure: function (record, operation) {
																					fnc.responseEvaluateOperationError(operation);
																				}
																			});                                                                                            
																		}
																	}
																},
																{
																	text:'Cancelar',
																	handler:function(){
																		w.close();
																	}
																}
															]
														})
													]
												});
												w.show();                                                                       
												
											}else{
											   fnc.createMsgBoxError({msg:'Este servicio esta desactivado.'}); 
											}
										}
									}
								},
								{
									icon:fnc.images.uri.edit(),
									text:'Agregar Servicio',
									cls:'fac-btn fac-btn-warning',                                                        
									handler:function(){
										if (relacionSucursal.FE_FIN==null){                                                                
											var w=fnc.createWindowModal({
												title:'Agregar de Servicio',
												height:550,
												width:500,
												items:[
													Ext.create("Ext.form.Panel",{
														items:[
															{
																xtype:'Desktop.Facturacion.Widgets.selectServiciosSuplementariosByProducto',
																anchor:'100%',
																CO_PROD:panel.NEGO.CO_PROD,
																CO_MONE_FACT:panel.NEGO.CO_MONE_FACT,
																packageParent:{package:panel.package,varSend:varSendPackage},
																desktop:desktop,
																allowBlank: false
															},                                                                                        
															Ext.create("Ext.form.field.Date",{
																anchor:'100%',
																name:'FE_INIC',
																fieldLabel:'Fecha activación',
																allowBlank: false
															}),
															{
																xtype:'checkbox',
																name:'ST_SOAR_INST',
																fieldLabel:'OIT SOARC',
																allowBlank: false
															},
															{
																xtype:'numberfield',
																name:'CO_OIT_INST',
																fieldLabel:'OIT Instalación',
																allowBlank: false
															}
														],
														buttons:[
															{
																text:'Guardar',
																formBind: true, //only enabled once the form is valid
																handler:function(){
																	var formCmp=this.up('form');
																	var form = formCmp.getForm();
																	if (form.isValid()) {                                                                                            
																		var values=form.getFieldValues();
																		values.CO_NEGO_SUCU=relacionSucursal.CO_NEGO_SUCU;
																		var m=new Desktop.Facturacion.Models.NEGO_SUCU_SERV_SUPL(values);                                                                                            
																		var dataSend=m.data;
																		fnc.setApiProxyModel(m,{create:'agregar_servicio/',update:'agregar_servicio/'});
																		dataSend.CO_SERV_SUPL_ARR=[];
																		var store=formCmp.down("grid").getStore();
																		for(var i=0;i<store.data.items.length;i++){
																			dataSend.CO_SERV_SUPL_ARR.push(store.data.items[i].data.CO_SERV_SUPL);
																		}
																		m.save({
																			params:dataSend,
																			success: function (record, operation) {
																				storeServiciosSuplementarios.load();
																				w.close();
																			},
																			failure: function (record, operation) {
																				fnc.responseEvaluateOperationError(operation);
																			}
																		});                                                                                            
																	}
																}
															},
															{
																text:'Cancelar',
																handler:function(){
																	w.close();
																}
															}
														]
													})
												]
											});
											w.show();
										}else{
											fnc.createMsgBoxError({msg:'Esta sucursal se encuentra desactivada.'})
										}
									}
								}
							]                                                    
						}
					];
                                } else {
                                    itemsServSupl=[
                                        {
                                            html: 'No hay servicios suplementarios registrados.'
                                        }
                                    ];
                                }
                                
                                panel.add({
                                    xtype:'fieldset',
                                    title:titlePanel,
                                    items:[
                                        //Planes
                                        {
                                            xtype:'fieldset',
                                            title: 'Planes contratados',
//                                            height: 100,
                                            width: '100%',                                        
                                            items: itemsPlanes
                                        },
                                        //Servicios Suplementarios
                                        {
                                            xtype:'fieldset',
                                            title: 'Servicios Suplementarios contratados',
                                            items: itemsServSupl
					}
                                    ]
                               });
                               console.log("cantidad: "+storePlanes.getCount()>0); 
                            })(realacionSucursales.getAt(i));
                        }
                    }
                }
            });
            
            panel.enable();
            
        },
        initComponent:function(){
            this.callParent();            
        },
        items:[
            {
                title: 'Panel 1',
                flex: 1,
                margin: '0 0 10 0',
                html: 'Cargando...'
            } ],
        loadStructure:function(panel,CO_NEGO,desktopPar,package){
            panel.package=package;
            fnc.load.ModelsAndStores(["PROD","SUCU","PLAN","SERV_SUPL","NEGO","NEGO_SUCU","CIER","NEGO_SUCU_PLAN","NEGO_SUCU_SERV_SUPL","NEGO_SUCU_SERV_UNIC"],function(){
                panel.stores={
                    nego:new Desktop.Facturacion.Stores.NEGO(),
                    nego_sucu:new Desktop.Facturacion.Stores.NEGO_SUCU()                    
                };
                for(var k in panel.stores){
                    fnc.addParameterStore(panel.stores[k],varSendPackage,panel.package);
                }            
                fnc.setApiProxyStore(panel.stores.nego_sucu,{read:'selectByNego/'});
                panel.stores.nego.load({params:{id:CO_NEGO},callback:function(){                     
                    var desktop=desktopPar;
                    var nego=panel.stores.nego.data.items;
                    if(nego.length==1){
                        nego=nego[0].data;
                        panel.NEGO=nego;
                        panel.createViewsSucursales(desktop);
                        fnc.load.scripts([
                            "widgets/selectSucursalByCliente.js",
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