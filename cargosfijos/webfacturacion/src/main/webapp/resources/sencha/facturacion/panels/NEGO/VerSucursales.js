(function(){    
    var packageBase="Desktop.Facturacion.Panels.NEGO.VerSucursales";
    var settingsLocal=window.settings.real.get();
    var varSendPackage=settingsLocal.Permissions.varSendPackage;
    var AJAX_URI=settingsLocal.AJAX_URI;
    var perfil=settings.online.perfil;
    var hiddenBtn=false;
    if (perfil.trim()=='Facturación'){
        hiddenBtn=true;
    } 
            
    function filtrarSucursales(obj){
        var fieldset=obj.up();
        var label=fieldset.down('label');
        var filter=fieldset.down('textfield').getValue().toLowerCase();
        var panel=fieldset.up().up();
        var items=panel.getEl().query(".panel_versucursales");
        var son=items.length;
        var visibles=0;
        for(var i=0;i<items.length;i++){
            var item=Ext.getCmp(items[i].id);
            if (item.title.toLowerCase().indexOf(filter)==-1){
                item.setVisible(false);
            }else{
                item.setVisible(true);
                visibles++;
            }
        }
        label.setText(visibles+" de "+son);
    }
    Ext.define(packageBase, {
        extend:'Ext.panel.Panel',
        alias:'widget.Desktop.Facturacion.Panels.NEGO.VerSucursales',
        overflowY:'scroll',
        defaults: {
            frame: true,
            collapsible: true,
            collapsed:true,
            bodyPadding: 5,
            flex: 1,
            margin: 5,              
        },         
        createViewsSucursales:function(desktop){

            var panel=this;
            panel.disable();
            panel.removeAll();            
            //panel.NEGO            
            var btnDisabled = false;
            panel.stores.nego_sucu.load({
                params:{CO_NEGO:panel.NEGO.CO_NEGO},
                callback:function(a,b,c){
                    if(c){
                        
                        ///////////////////////////////
                        var lastDay=0;  
                        var store=new Desktop.Facturacion.Stores.PROD_PADRE();
                        fnc.addParameterStore(store,varSendPackage,panel.package);
                        fnc.setApiProxyStore(store,{read:'getFechaByProd/'});
                        store.load({ 
                                    params:{
                                    CO_PROD:panel.NEGO.PROD_PADRE.COD_PROD_PADRE
                                    },
                                    callback: function(records, operation, success) {
                                         if (success){
                                            if (records.length==1){
                                            var data=records[0].data;
                                            //lastDay = new Date(panel.CIER.NU_ANO, panel.CIER.NU_PERI-1,data.FECHA_FIN);
                                            lastDay =data.FECHA_FIN;                                               
                                            }
                                        }
                                    }            
                                   });                                                        
                        //////////////////////////////                        
                        
                        var realacionSucursales=panel.stores.nego_sucu.data;
                        //load promociones de negocio
                        var storeNegoProm=new Desktop.Facturacion.Stores.NEGO_PROM();
                        var storeNegoResumen=new Desktop.Facturacion.Stores.NEGO();
                        var storeCorrActivos=new Desktop.Facturacion.Stores.NEGO_CORR();
                        var storeNegoCome=new Desktop.Facturacion.Stores.NEGO_COME(); // rordonez
                        var storeNegoHistMigraciones=new Desktop.Facturacion.Stores.MIGR_NEGO_SUCU(); // rordonez
                        
                        fnc.addParameterStore(storeCorrActivos,varSendPackage,panel.package); 
                        fnc.addParameterStore(storeCorrActivos,"CO_NEGO",panel.NEGO.CO_NEGO); 
                        fnc.setApiProxyStore(storeCorrActivos,{read:'selectActivosByCO_NEGO/'});                        
                        
                        fnc.addParameterStore(storeNegoProm,varSendPackage,panel.package); 
                        fnc.addParameterStore(storeNegoProm,"CO_NEGO",panel.NEGO.CO_NEGO); 
                        fnc.setApiProxyStore(storeNegoProm,{read:'selectByCO_NEGO/'});
                        
                        fnc.addParameterStore(storeNegoCome,varSendPackage,panel.package); 
                        fnc.addParameterStore(storeNegoCome,"CO_NEGO",panel.NEGO.CO_NEGO); 
                        fnc.setApiProxyStore(storeNegoCome,{read:'selectByCO_NEGO/'});
                        
                        fnc.addParameterStore(storeNegoHistMigraciones,varSendPackage,panel.package); 
                        fnc.addParameterStore(storeNegoHistMigraciones,"CO_NEGO",panel.NEGO.CO_NEGO); 
                        fnc.setApiProxyStore(storeNegoHistMigraciones,{read:'selectByCO_NEGO/'});
                        
                        fnc.addParameterStore(storeNegoResumen,varSendPackage,panel.package); 
                        fnc.addParameterStore(storeNegoResumen,"CO_NEGO",panel.NEGO.CO_NEGO); 
                        fnc.setApiProxyStore(storeNegoResumen,{read:'getResumen/'});                        
                                                
                                                
                        if((panel.NEGO.PROD_PADRE.DESC_PROD_PADRE).toUpperCase() == 'TI'){
                            btnDisabled = true;
                        }
                                             
                        panel.add({
                            title:"Descripción Negocio "+fnc.shortcodes.getCortado(panel.NEGO.isCortado)+ fnc.shortcodes.getDesactivado(panel.NEGO.isDesactivado),
                            cls:'panel_descripcion_negocio',
                            layout:'column',                             
                            margin: '0 0 10 0',                              
                            items:[
                                {
                                    columnWidth: .5,
                                    items:[
                                        {
                                            xtype:'textarea',
                                            fieldLabel: 'Cliente',
                                            value:Desktop.Facturacion.Models.CLIE.getFullInfo(panel.NEGO.CLIE),
                                            readOnly:true,
                                            width:400
                                        },
                                        {
                                            xtype:'textfield',
                                            fieldLabel: 'Codigo BUN',
                                            value:panel.NEGO.CLIE.DE_CODI_BUM && panel.NEGO.CLIE.DE_DIGI_BUM ? panel.NEGO.CLIE.DE_CODI_BUM +'-'+ panel.NEGO.CLIE.DE_DIGI_BUM : '(No definido)' ,
                                            readOnly:true,
                                        },
                                        {
                                            xtype:'textfield',
                                            fieldLabel: 'Ejecutivo',
                                            value:panel.NEGO.CLIE.DE_EJEC,
                                            readOnly:true,
                                            width:400
                                        },                                        
                                        {
                                            xtype:'textfield',
                                            fieldLabel: 'Negocio',
                                            value:panel.NEGO.CO_NEGO,
                                            readOnly:true,
                                        },                                            
                                        {
                                            xtype:'textfield',
                                            fieldLabel: 'Tipo Facturación',
                                            value:panel.NEGO.TIPO_FACT.NO_TIPO_FACT,
                                            readOnly:true,
                                        },                                           
                                        {
                                            xtype:'textfield',
                                            fieldLabel: 'Moneda',
                                            value:panel.NEGO.MONE_FACT.NO_MONE_FACT,
                                            readOnly:true,
                                        },
                                        {
                                            xtype:'grid',
                                            columns:[
                                                {text:'Correo',dataIndex:'DE_CORR',width:300},
                                            ],
                                            store:storeCorrActivos,
                                        }
                                    ]

                                },
                                {
                                    columnWidth: .5,
                                    items:[
                                        {
                                            xtype:'textarea',
                                            fieldLabel: 'Representante',
                                            value:Desktop.Facturacion.Models.CLIE.getInfoRepresentante(panel.NEGO.CLIE),
                                            readOnly:true,
                                            width:400
                                        }, 
                                        {
                                            xtype:'textfield',
                                            fieldLabel: 'Segementación',
                                            value:panel.NEGO.CLIE.DE_SEGM,
                                            readOnly:true,
                                            width:400
                                        },  
                                        {
                                            xtype:'textfield',
                                            fieldLabel: 'Sub-Gerente',
                                            value:panel.NEGO.CLIE.DE_SUB_GERE,
                                            readOnly:true,
                                            width:400
                                        },                                        
                                        {
                                            xtype:'textfield',
                                            fieldLabel: 'Producto',
                                            value:panel.NEGO.PROD_PADRE.DESC_PROD_PADRE,
                                            readOnly:true,
                                            width:400
                                        },                                            
                                        { 
                                            xtype:'textfield',
                                            fieldLabel: 'Servicio',
                                            value:panel.NEGO.PROD.NO_PROD,
                                            readOnly:true,
                                            width:400
                                        }, 
                                        {
                                            xtype:'textfield',
                                            fieldLabel: 'Periodo',
                                            value:panel.NEGO.PERI_FACT.NO_PERI_FACT,
                                            readOnly:true,
                                        },                                                                                       
                                        {
                                            xtype:'textarea',
                                            fieldLabel: 'Correspondencia',
                                            value:panel.NEGO.SUCU_CORR.DE_DIRE,
                                            readOnly:true,
                                            width:400
                                        },                                                                                       
                                        {
                                            xtype:'textarea',
                                            fieldLabel: 'Fiscal',
                                            value:panel.NEGO.CLIE.SUCU_FISC? panel.NEGO.CLIE.SUCU_FISC.DE_DIRE : '(No definido)',
                                            readOnly:true,
                                            width:400
                                        },
                                    ]
                                }
                            ],
                            tools: [
                                {
                                    type:'refresh',
                                    tooltip : 'Ver Historial de cambios',
                                    handler:function(){
                                        var storeNegoHistorico=new Desktop.Facturacion.Stores.NEGO_HISTORICO();
                                        fnc.addParameterStore(storeNegoHistorico,varSendPackage,panel.package);
                                        fnc.addParameterStore(storeNegoHistorico,"CO_NEGO",panel.NEGO.CO_NEGO);
                                        fnc.setApiProxyStore(storeNegoHistorico,{read:'selectByCO_NEGO/'});
                                        storeNegoHistorico.load();
                
                                        var winHis=fnc.createWindowModal({
                                            title:'Historial Negocio',
                                            layout:'fit',
                                            width:750,
                                            height:400,
                                            items:[
                                                {
                                                    xtype:'grid',
                                                    layout:'fit',
                                                    store:storeNegoHistorico,
                                                    columns:[
                                                        {text:'Información',dataIndex:'DE_INFORMACION',width:400},
                                                        {text:'Usuario',dataIndex:'USUA.DE_USER'},
                                                        {text:'Fecha',dataIndex:'FH_REGI', xtype: 'datecolumn',format: 'd/m/Y H:i:s',width:200},
                                                    ]                                                                                    
                                                }
                                            ]
                                        });
                                        winHis.show();
//                                        var winHis=fnc.createWindowHistorial({title:"Historial Negocio ",type:'negocio',idEntity:panel.NEGO.CO_NEGO,AJAX_URI:AJAX_URI,varSendPackage:varSendPackage,packageBase:panel.package});
//                                        winHis.show();
                                    }
                                }
                            ]
                        });    
                        //promociones                       
                        //resumen
                        panel.add({
                            title:"Resumen de Negocio",
                            cls:'panel_resumen_negocio',                             
                            //margin: '0 0 10 0',
                            items:[
                                {
                                    xtype:'grid',
                                    columns:[
                                        {text:'Descripción',dataIndex:'NOMB',width:400},
                                        {text:'Cantidad',dataIndex:'CANT'},
                                        {text:'Monto',dataIndex:'MONT'},
                                        {text:'Moneda',dataIndex:'MONEDA'}
                                    ],
                                    store:storeNegoResumen
                                }
                            ]
                        });
                        //comentarios
                        panel.add({
                            title:"Comentarios",
                            cls:'panel_comentarios_negocio',                             
                            //margin: '0 0 10 0',
                            items:[
                                {
                                    xtype:'grid',
                                    store:storeNegoCome,
                                    columns:[
                                        //{text:'Usuario',dataIndex:'CO_USUA'},
                                        //{text:'de usuario',dataIndex:'USUA',renderer:function(a,b,c){return c.data.USUA?c.data.USUA.DE_USER:'';}},
                                        {text:'Usuario',dataIndex:'USUA.DE_USER'},
                                        {text:'Comentario',dataIndex:'DE_COME'},
                                        {text:'Fecha',dataIndex:'FH_CREO', xtype: 'datecolumn',format: 'd/m/Y H:i:s'},
                                    ],
                                    tbar:[
                                        {
                                            text:'Agregar comentario por negocio',
                                            cls:'fac-btn fac-btn-warning', 
                                            hidden: hiddenBtn,
                                            handler:function(){
                                                var record=new Desktop.Facturacion.Models.NEGO_COME();
                                                fnc.addParameterModel(record,varSendPackage,panel.package);
                                                var frm=Ext.create('Ext.form.Panel',{
                                                    model:'NEGO_COME',
                                                    items:[
                                                        {
                                                            xtype:'textarea',
                                                            fieldLabel:'Comentario',
                                                            name:'DE_COME',
                                                            allowBlank: false,
                                                            width:600
                                                        },                                                               
                                                    ],
                                                    buttons:[
                                                        {
                                                            text:'Guardar',
                                                            formBind: true,
                                                            handler:function(){
                                                                var viewForm = this.up('form');
                                                                var form=viewForm.getForm();
                                                                var record=form.getRecord();
                                                                if (form.isValid()){
                                                                    form.updateRecord(record);                                                                    
                                                                    record.save({params:record.data,success:function(){storeNegoCome.load();}});
                                                                    win.close();
                                                                }                                                                        
                                                            }
                                                        }
                                                    ]
                                                });                                                
                                                record.data.CO_NEGO=panel.NEGO.CO_NEGO;
                                                frm.getForm().loadRecord(record);
                                                var win=fnc.createWindowModal({
                                                    title:'Agregar Comentario por negocio',
                                                    layout:'fit',
                                                    width: 650,
                                                    height: 200, 
                                                    items:[   
                                                        frm
                                                    ]
                                                });
                                                win.show();
                                            }
                                        }
                                    ]
                                }
                            ]
                        });
                        //Historial de Migraciones
                        panel.add({
                            title:"Historial de Migraciones",
                            cls:'panel_hist_migraciones_negocio',                             
                            //margin: '0 0 10 0',
                            items:[
                                {
                                    xtype:'grid',
                                    store:storeNegoHistMigraciones,
                                    columns:[
                                        //{text:'Usuario',dataIndex:'CO_USUA'},
                                        //{text:'de usuario',dataIndex:'USUA',renderer:function(a,b,c){return c.data.USUA?c.data.USUA.DE_USER:'';}},
                                        {text:'Negocio Origen',dataIndex:'CO_NEGO_ORIG'},
                                        {text:'Negocio Destino',dataIndex:'CO_NEGO_DEST'},
                                        //sucursal.DE_DIRE+' ('+departamento.NO_DEPA+" - "+provincia.NO_PROV+" - "+distrito.NO_DIST+')';
                                        {text:'Sucursal',renderer:function(a,b,c){return (c.data.NEGO_SUCU.SUCU.DE_DIRE+' ('+c.data.NEGO_SUCU.SUCU.DIST.PROV.DEPA.NO_DEPA+" - "+c.data.NEGO_SUCU.SUCU.DIST.PROV.NO_PROV+" - "+c.data.NEGO_SUCU.SUCU.DIST.NO_DIST+')');}},
                                        {text:'Usuario',dataIndex:'USUA.DE_USER'},
                                        {text:'Fecha',dataIndex:'FH_REGI', xtype: 'datecolumn',format: 'd/m/Y H:i:s'},
                                    ],
                                }
                            ]
                        });
                        for(var i=0;i<realacionSucursales.length;i++){                            
                            (function(record){
                                var storeMoti=new Desktop.Facturacion.Stores.MOTI_DESC();
                                var relacionSucursal=record.data;
                                var sucursal=relacionSucursal.SUCU;
                                var distrito=sucursal.DIST;
                                var provincia=distrito.PROV;
                                var departamento=provincia.DEPA;
                                var storePlanes=new Desktop.Facturacion.Stores.NEGO_SUCU_PLAN();
                                var titlePanel="";
                                var direccionUbicacionSucursal="";
                                var storeMediInst=new Desktop.Facturacion.Stores.PLAN_MEDI_INST();
                                
                                fnc.addParameterStore(storeMoti,varSendPackage,panel.package);
                                fnc.addParameterStore(storeMediInst,varSendPackage,panel.package);                                 
                                fnc.setApiProxyStore(storePlanes,{read:'selectByNEGO_SUCU/'});
                                fnc.addParameterStore(storePlanes,varSendPackage,panel.package);
                                fnc.addParameterStore(storePlanes,"CO_NEGO_SUCU",relacionSucursal.CO_NEGO_SUCU);
                                var storeServiciosSuplementarios=new Desktop.Facturacion.Stores.NEGO_SUCU_SERV_SUPL();
                                fnc.setApiProxyStore(storeServiciosSuplementarios,{read:'selectByNEGO_SUCU/'});
                                fnc.addParameterStore(storeServiciosSuplementarios,varSendPackage,panel.package);
                                fnc.addParameterStore(storeServiciosSuplementarios,"CO_NEGO_SUCU",relacionSucursal.CO_NEGO_SUCU);
                                var storeServiciosUnicos=new Desktop.Facturacion.Stores.NEGO_SUCU_SERV_UNIC();
                                fnc.setApiProxyStore(storeServiciosUnicos,{read:'selectByNEGO_SUCU/'});
                                fnc.addParameterStore(storeServiciosUnicos,varSendPackage,panel.package);
                                fnc.addParameterStore(storeServiciosUnicos,"CO_NEGO_SUCU",relacionSucursal.CO_NEGO_SUCU);
                                var storeNegoSucuProm=new Desktop.Facturacion.Stores.NEGO_SUCU_PROM();
                                fnc.addParameterStore(storeNegoSucuProm,varSendPackage,panel.package);
                                fnc.addParameterStore(storeNegoSucuProm,"CO_NEGO_SUCU",relacionSucursal.CO_NEGO_SUCU);
                                fnc.setApiProxyStore(storeNegoSucuProm,{read:'selectByCO_NEGO_SUCU/'});
                                
                                direccionUbicacionSucursal=sucursal.DE_DIRE+' ('+departamento.NO_DEPA+" - "+provincia.NO_PROV+" - "+distrito.NO_DIST+')';
                                titlePanel+=fnc.shortcodes.getSuspendido(relacionSucursal.isSuspendido)+fnc.shortcodes.getMudado(relacionSucursal.isMudado)+fnc.shortcodes.getDesactivado(relacionSucursal.isDesactivado)+ ' OIT '+(relacionSucursal.ST_SOAR_INST?'SOARC':'')+' : '+(relacionSucursal.CO_OIT_INST?relacionSucursal.CO_OIT_INST:'');
                                titlePanel+=';CIRCUITO : '+(relacionSucursal.CO_CIRC?relacionSucursal.CO_CIRC:'');
                                titlePanel+=';'+direccionUbicacionSucursal;
                                
                                
                                
                                
                                panel.add({
                                    title:titlePanel,
                                    cls:'panel_versucursales',
                                    //flex: 1,                               
                                    margin: '0 0 10 0',                                
                                    items:[
                                        //Sucursal
                                        {
                                            xtype:'fieldset',
                                            title: 'Datos de instalación', 
                                            layout:'column',
                                            items:[
                                                {
                                                    columnWidth: .5,
                                                    items:[
                                                        {
                                                            xtype:'textfield',
                                                            fieldLabel: 'Dirección Sucursal',
                                                            value:sucursal.DE_DIRE,
                                                            width:600,
                                                            readOnly:true,
                                                        },
                                                        {
                                                            xtype:'textfield',
                                                            fieldLabel: 'Ubicación Sucursal',
                                                            value:departamento.NO_DEPA+" - "+provincia.NO_PROV+" - "+distrito.NO_DIST,
                                                            width:600,
                                                            readOnly:true,
                                                        },
                                                        {
                                                            xtype:'datefield',
                                                            fieldLabel: 'Fecha Activación',
                                                            value:relacionSucursal.FE_INIC,
                                                            width:200,
                                                            readOnly:true,
                                                        },
                                                         {
                                                            xtype:'datefield',
                                                            fieldLabel: 'Fecha Desactivación',
                                                            value:relacionSucursal.FE_FIN,
                                                            width:200,
                                                            readOnly:true,
                                                        },
                                                        {
                                                            xtype:'textfield',
                                                            fieldLabel: 'Observación',
                                                            value:relacionSucursal.DE_OBSERV,
                                                            width:600,
                                                            readOnly:true,
                                                        }
                                                    ]
                                                },{
                                                    columnWidth: .5,
                                                    items:[
                                                        {
                                                            xtype:'textfield',
                                                            fieldLabel: 'Motivo Desactivación',
                                                            value:relacionSucursal.MOTI_DESC === null ? "": relacionSucursal.MOTI_DESC.NO_MOTI_DESC,
                                                            width:200,
                                                            readOnly:true,
                                                        },
                                                        {
                                                            xtype:'textarea',
                                                            fieldLabel: 'Información Desactivación',
                                                            value:relacionSucursal.DE_INFO_DESC,
                                                            width:400,
                                                            readOnly:true,
                                                        },
                                                        {
                                                            xtype:'textfield',
                                                            fieldLabel: 'Orden Servicio',
                                                            value:relacionSucursal.DE_ORDE_SERV,
                                                            width:200,
                                                            readOnly:true,
                                                        },
                                                        {
                                                            xtype:'textfield',
                                                            fieldLabel: 'Plazo (meses) ',
                                                            value:relacionSucursal.DE_PLAZ_CONT,
                                                            width:200,
                                                            readOnly:true,
                                                        },
                                                        {
                                                            xtype:'button',
                                                            text:'Editar Sucursal',
                                                            record:record,
                                                            relacionSucursal:relacionSucursal,
                                                            sucursal:sucursal,
                                                            departamento:departamento,
                                                            provincia:provincia,
                                                            distrito:distrito,
                                                            hidden: hiddenBtn,
                                                            style:{
                                                                marginBottom:'10px'
                                                            },
                                                            handler:function(){
                                                                if (relacionSucursal.FE_FIN==null){
                                                                    var win=getWindowEditarSucursal(panel.NEGO,desktop,panel,this.relacionSucursal);                                                    
                                                                    var pn=win.items.getAt(0);
                                                                    pn.loadRecord(this.record);
                                                                    pn.query("[name='DE_SUCU']")[0].setValue(this.sucursal.DE_DIRE+"("+this.departamento.NO_DEPA+" - "+this.provincia.NO_PROV+" - "+this.distrito.NO_DIST+")");
                                                                    win.show();   
                                                                }else{
                                                                    fnc.createMsgBoxError({msg:'Esta sucursal se encuentra desactivada.'})
                                                                }
                                                            }
                                                        },
                                                        {
                                                            xtype:'button',
                                                            text:'Desactivar Sucursal',
                                                            cls:'fac-btn fac-btn-danger', 
                                                            hidden: hiddenBtn,
                                                            style:{
                                                                marginBottom:'10px'
                                                            },
                                                            handler:function(){
                                                                if (relacionSucursal.FE_FIN==null){
                                                                    var nego=panel.NEGO;
                                                                    
                                                                     var firstDay = new Date();
                
                                                                     if(panel.CIER != null){        
                                                                        firstDay = new Date(panel.CIER.NU_ANO, panel.CIER.NU_PERI-1, 1);            
                                                                     } 
                                                                    
                                                                     //var firstDay = new Date(panel.CIER.NU_ANO, panel.CIER.NU_PERI-1, 1);
                                                                    
                                                                    var frm=Ext.create('Ext.form.Panel',{
                                                                        //model:'Desktop.Facturacion.Models.NEGO_SUCU',
                                                                        overflowY:'scroll',
                                                                        items:[               
                                                                           {
                                                                                xtype: 'datefield',
                                                                                fieldLabel:'Fecha de Desinstalación',
                                                                                name:'FE_FIN',
//                                                                                minDate: new Date(),
                                                                                maxValue: Ext.Date.add(firstDay,Ext.Date.DAY,lastDay),
                                                                                allowBlank: false,
                                                                           },{
                                                                               xtype: 'checkboxfield',
                                                                               fieldLabel:'OIT SOARC Desinstalación',
                                                                               checked:false,
                                                                               boxLabel  : 'OIT SOARC',
                                                                               name:'ST_SOAR_DESI',
                                                                               allowBlank: false,
                                                                           },{
                                                                                xtype:'numberfield',
                                                                                name: 'CO_OIT_DESI',
                                                                                fieldLabel: 'OIT Desinstalación',
                                                                                allowBlank: false,
                                                                           },{
                                                                                xtype:'combobox',
                                                                                fieldLabel:'Motivo Desactivación',
                                                                                store:storeMoti,
                                                                                displayField: 'NO_MOTI_DESC',
                                                                                valueField: 'CO_MOTI_DESC',
                                                                                name: 'CO_MOTI_DESC',
                                                                                allowBlank: false,
                                                                                editable:false,
                                                                                listeners:{
                                                                                    select:function( combo, record, eOpts ){
                                                                                        var f=this.up();
                                                                                        var ST_SOAR_DESI=f.query("[name=ST_SOAR_DESI]")[0];
                                                                                        var CO_OIT_DESI=f.query("[name=CO_OIT_DESI]")[0];
                                                                                        var data=record[0].data;
                                                                                        if (data.NO_MOTI_DESC.toLowerCase()==='cambio de moneda'){
                                                                                            ST_SOAR_DESI.allowBlank = true;
                                                                                            CO_OIT_DESI.allowBlank = true;
                                                                                        }else{
                                                                                            ST_SOAR_DESI.allowBlank = false;
                                                                                            CO_OIT_DESI.allowBlank = false;
                                                                                        }
                                                                                        ST_SOAR_DESI.validate();
                                                                                        CO_OIT_DESI.validate();
                                                                                        
                                                                                    }
                                                                                }
                                                                            },{
                                                                                xtype:'textarea',
                                                                                fieldLabel:'Información Desactivación',
                                                                                name:'DE_INFO_DESC',
                                                                                allowBlank: true,
                                                                                width:600
                                                                            }           
                                                                       ],
                                                                       buttons:[
                                                                           {
                                                                               text:'Guardar',
                                                                               formBind: true,
                                                                               handler:function(){
                                                                                    var form=this.up('form').getForm();
                                                                                    if (form.isValid()){
                                                                                        var data=form.getFieldValues();
                                                                                        var model=new Desktop.Facturacion.Models.NEGO_SUCU(relacionSucursal);
                                                                                        model.data.ST_SOAR_DESI=data.ST_SOAR_DESI;
                                                                                        model.data.CO_OIT_DESI=data.CO_OIT_DESI;
                                                                                        model.data.FE_FIN=data.FE_FIN;
                                                                                        model.data.CO_MOTI_DESC=data.CO_MOTI_DESC;
                                                                                        model.data.DE_INFO_DESC=data.DE_INFO_DESC;
                                                                                        data=fnc.getParamsSendPackage(model.getData(),varSendPackage,panel.package);
                                                                                        fnc.setApiProxyModel(model,{update:'desactivar/'});                                                                            
                                                                                        model.save({params:data,callback:function(a,b,c,d,e){
                                                                                            if(c){                                                        
                                                                                                form.reset();
                                                                                                win.close();
                                                                                                panel.createViewsSucursales(desktop);
//                                                                                                panel.loadStructure(panel,nego.CO_NEGO,varSendPackage,panel.package);
                                                                                            }else{
                                                                                                var jsonResponse=Ext.JSON.decode(b._response.responseText);
                                                                                                fnc.createMsgBoxError({msg:jsonResponse.msg});
                                                                                            }                                                  
                                                                                        }});  
                                                                                    }
                                                                               }
                                                                           }
                                                                       ]
                                                                    });
                                                                    var win=fnc.createWindowModal(desktop,{
                                                                        title:'Desactivar Sucursal de Negocio '+nego.CO_NEGO,
                                                                        width:380,
                                                                        //height:220,
                                                                        height:350,
                                                                        items:[frm],
                                                                        renderTo:Ext.getBody(),
                                                                    });  
                                                                    win.show();
                                                                }else{
                                                                    fnc.createMsgBoxError({msg:'La sucursal ya se encuentra desactivada'});
                                                                }
                                                            }
                                                        },
                                                        {
                                                            xtype:'button',
                                                            text:'Realizar mudanza',
                                                            cls:'fac-btn fac-btn-warning', 
                                                            hidden: hiddenBtn,
                                                            style:{
                                                                marginBottom:'10px',
                                                                paddingLeft:'10px',
                                                            },
                                                            handler:function(){
                                                                if (relacionSucursal.FE_FIN==null){
                                                                    var storeSucursales=new Desktop.Facturacion.Stores.SUCU();
                                                                    fnc.addParameterStore(storeSucursales,varSendPackage,panel.package);
                                                                    fnc.addParameterStore(storeSucursales,"CO_CLIE",relacionSucursal.NEGO.CO_CLIE);
                                                                    fnc.setApiProxyStore(storeSucursales,{read:'selectByCLIE/'});                                                                    
                                                                    
                                                                    var date = new Date();
                                                                    //var firstDay = new Date(panel.CIER.NU_ANO, panel.CIER.NU_PERI-1, 1);                                                                    
                                                                    var firstDay = new Date();
                
                                                                    if(panel.CIER != null){        
                                                                        firstDay = new Date(panel.CIER.NU_ANO, panel.CIER.NU_PERI-1, 1);            
                                                                    }
                                                                    
                                                                    var frm=Ext.create('Ext.form.Panel',{
                                                                        tbar:[
                                                                            {
                                                                                text:'Nueva Sucursal',
                                                                                iconCls: fnc.icons.class.insert(),
                                                                                handler:function(){
                                                                                    var win=fnc.createWindowModal({ 
                                                                                        title:'Agregar Sucursal',
                                                                                        items:[
                                                                                            {
                                                                                                xtype:'form',
                                                                                                items:[
                                                                                                    //{xtype:'Desktop.Facturacion.Widgets.selectSucursalByCliente',getCO_CLIE:function(){return relacionSucursal.NEGO.CO_CLIE;},packageParent:{package:panel.package,varSendPackage:varSendPackage}},
                                                                                                    {
                                                                                                        xtype:'Desktop.Facturacion.Widgets.comboBoxDistrito',
                                                                                                        packageParent:{package:panel.package,varSend:varSendPackage},
                                                                                                    },{               
                                                                                                        xtype:'textfield',
                                                                                                        fieldLabel: 'Dirección',
                                                                                                        name: 'DE_DIRE',
                                                                                                        allowBlank: false,                                                                                                        
                                                                                                    }                                                                                            
                                                                                                ],
                                                                                                buttons:[
                                                                                                    {
                                                                                                        text:'Crear',
                                                                                                        handler:function(){
                                                                                                            var form = this.up('form').getForm();
                                                                                                            if (form.isValid()) {
                                                                                                                var model=new Desktop.Facturacion.Models.SUCU(form.getFieldValues());
                                                                                                                model.data.CO_CLIE=relacionSucursal.NEGO.CO_CLIE;
                                                                                                                model.save({params:fnc.getParamsSendPackage(model.getData(),varSendPackage,panel.package)});
                                                                                                                form.reset();
                                                                                                                win.close();
                                                                                                                storeSucursales.load();
                                                                                                            }
                                                                                                        }
                                                                                                    },
                                                                                                    {
                                                                                                        text:'Cancelar',
                                                                                                        handler:function(){
                                                                                                            win.close();
                                                                                                        }
                                                                                                    }
                                                                                                ]
                                                                                            }
                                                                                        ]
                                                                                    });
                                                                                    win.show();                                                                                    
                                                                                }
                                                                            }
                                                                        ],
                                                                        items:[
                                                                            {
                                                                                xtype:'combobox',
                                                                                store:storeSucursales,
                                                                                width:'100%',
                                                                                name:'CO_SUCU',
                                                                                fieldLabel:'Mudar a la sucursal',
                                                                                displayField:'DE_DIRE',
                                                                                valueField:'CO_SUCU',
                                                                                allowBlank: false,
                                                                                editable:false,
                                                                                listConfig: {
                                                                                    itemTpl: [
                                                                                        '<div data-qtip="{DE_DIRE}">{DE_DIRE} ({DIST.PROV.DEPA.NO_DEPA} - {DIST.PROV.NO_PROV} - {DIST.NO_DIST})</div>'
                                                                                    ]
                                                                                }                                                                                
                                                                            },{
                                                                                xtype: 'datefield',
                                                                                fieldLabel:'Fecha de Instalación',
                                                                                name:'FE_INIC',
//                                                                                minDate: new Date(),
                                                                                maxValue: Ext.Date.add(firstDay,Ext.Date.DAY,lastDay),
                                                                                allowBlank: false
                                                                           },{
                                                                               xtype: 'checkboxfield',
                                                                               fieldLabel:'OIT SOARC Instalación',
                                                                               checked:false,
                                                                               boxLabel  : 'OIT SOARC',
                                                                               name:'ST_SOAR_INST',
                                                                               allowBlank: false
                                                                           },{
                                                                                xtype:'numberfield',
                                                                                name: 'CO_OIT_INST',
                                                                                fieldLabel: 'OIT Instalación',
                                                                                allowBlank: false
                                                                           },{
                                                                                xtype: 'datefield',
                                                                                fieldLabel:'Fecha de Desinstalación',
                                                                                name:'FE_FIN',
//                                                                                minDate: new Date(),
                                                                                maxValue: Ext.Date.add(firstDay,Ext.Date.DAY,lastDay),
                                                                                allowBlank: false
                                                                           },{
                                                                               xtype: 'checkboxfield',
                                                                               fieldLabel:'OIT SOARC Desinstalación',
                                                                               checked:false,
                                                                               boxLabel  : 'OIT SOARC',
                                                                               name:'ST_SOAR_DESI',
                                                                               allowBlank: false
                                                                           },{
                                                                                xtype:'numberfield',
                                                                                name: 'CO_OIT_DESI',
                                                                                fieldLabel: 'OIT Desinstalación',
                                                                                allowBlank: false
                                                                           }
                                                                        ],
                                                                        buttons:[
                                                                            {
                                                                                text:'Mudar',
                                                                                handler:function(){
                                                                                    var form=this.up('form').getForm();
                                                                                    if (form.isValid()){
                                                                                        var params=form.getFieldValues();
//                                                                                        params.CO_NEGO_SUCU=relacionSucursal.CO_NEGO_SUCU;
//                                                                                        params[varSendPackage]=panel.package;
                                                                                        var model=new Desktop.Facturacion.Models.NEGO_SUCU(relacionSucursal);
                                                                                        model.data.ST_SOAR_INST=params.ST_SOAR_INST;
                                                                                        model.data.CO_OIT_INST=params.CO_OIT_INST;
                                                                                        model.data.FE_INIC=params.FE_INIC;
                                                                                        model.data.ST_SOAR_DESI=params.ST_SOAR_DESI;
                                                                                        model.data.CO_OIT_DESI=params.CO_OIT_DESI;
                                                                                        model.data.FE_FIN=params.FE_FIN;
                                                                                        model.data.CO_MOTI_DESC=params.CO_MOTI_DESC;
                                                                                        model.data.CO_SUCU=params.CO_SUCU;
                                                                                        model.data.CO_NEGO_SUCU=relacionSucursal.CO_NEGO_SUCU;
                                                                                        
                                                                                        params=fnc.getParamsSendPackage(model.getData(),varSendPackage,panel.package);
                                                                                        Ext.Ajax.request({
                                                                                            method:'POST',
                                                                                            params:params,
                                                                                            url: AJAX_URI+'NEGO_SUCU/MUDAR/',
                                                                                            success: function(response, opts) {
                                                                                                var json=fnc.responseExtAjaxRequestMsjError(response);
                                                                                                if(json.success){
                                                                                                    panel.createViewsSucursales(desktop);
                                                                                                    winMS.close();
                                                                                                }
                                                                                            }
                                                                                        });  
                                                                                    }
                                                                                }
                                                                            }
                                                                        ]
                                                                    })
                                                                    var winMS=fnc.createWindowModal({
                                                                        height:450,
                                                                        width:700,
                                                                        title:'Mudar sucursal '+relacionSucursal.SUCU.DE_DIRE+' a otra sucursal del cliente',
                                                                        items:[frm] 
                                                                    });
                                                                    winMS.show();
                                                                }else{
                                                                    fnc.createMsgBoxError({msg:'Esta sucursal se encuentra desactivada.'})
                                                                }
                                                            }
                                                        },
                                                        {
                                                            xtype:'button',
                                                            text:'Ver Suspensiones',
                                                            cls:'fac-btn fac-btn-info', 
                                                            iconCls:fnc.icons.class.ver(),
                                                            handler:function(){                 
                                                                var storeSuspensiones=new Desktop.Facturacion.Stores.SUSP();
                                                                fnc.addParameterStore(storeSuspensiones,varSendPackage,panel.package);          
                                                                fnc.addParameterStore(storeSuspensiones,"CO_NEGO_SUCU",relacionSucursal.CO_NEGO_SUCU);          
                                                                fnc.setApiProxyStore(storeSuspensiones,{read:'selectByNegoSucu/'});                    
                                                                var winCor=fnc.createWindow({
                                                                    title:'Suspensiones (a pedido del cliente)',
                                                                    items:[
                                                                        {
                                                                            xtype:'grid',
                                                                            store:storeSuspensiones,
                                                                            columns:[
                                                                                {text:'Fecha inicio',dataIndex:'FE_INIC', xtype: 'datecolumn', format: 'd/m/Y'},
                                                                                {text:'Fecha fin',dataIndex:'FE_FIN', xtype: 'datecolumn', format: 'd/m/Y'},
                                                                                {text:'Estado',dataIndex:'DE_ESTADO'}
                                                                            ]
                                                                        }
                                                                    ]
                                                                });
                                                                winCor.show();                                                                
                                                            }
                                                        },
                                                        {
                                                            xtype:'button',
                                                            text:'Eliminar Sucursal',
                                                            cls:'fac-btn fac-btn-danger', 
                                                            iconCls:fnc.icons.class.ver(),
                                                            hidden: hiddenBtn,
                                                            handler:function(){ 
                                                                if (relacionSucursal.FE_FIN==null){
                                                                    fnc.createMsgConfirm({
                                                                        msg:'Esta seguro que desea eliminar la sucursal con OIT '+ relacionSucursal.CO_OIT_INST,
                                                                        call:function(btn){
                                                                            if(btn=='yes'){
                                                                                var model=new Desktop.Facturacion.Models.NEGO_SUCU(relacionSucursal);
                                                                                model.data.CO_NEGO_SUCU=relacionSucursal.CO_NEGO_SUCU;
                                                                                var params=fnc.getParamsSendPackage(model.getData(),varSendPackage,panel.package);
                                                                                Ext.Ajax.request({
                                                                                        method:'POST',
                                                                                        params:params,
                                                                                        url: AJAX_URI+'NEGO_SUCU/ELIMINAR/',
                                                                                        success: function(response, opts) {
                                                                                                var json=fnc.responseExtAjaxRequestMsjError(response);
                                                                                                if(json.success){
                                                                                                    panel.createViewsSucursales(desktop);
                                                                                                }
                                                                                        }
                                                                                });  
                                                                            }                                
                                                                        }
                                                                    });
                                                                } else {
                                                                    fnc.createMsgBoxError({msg:'La sucursal se encuentra desactivada. No se puede eliminar.'});
                                                                }                                                              
                                                            }
                                                        }
                                                    ]
                                                }
                                            ]
                                        },
                                        //Planes
                                        {
                                            xtype:'fieldset',
                                            title: 'Planes contratados',
                                            height: 200,
                                            width: '100%',                                        
                                            items:[
                                                {
                                                    xtype:'grid',
                                                    store:storePlanes,
                                                    columns:[
                                                        /*{text:'Codigo',dataIndex:'CO_NEGO_SUCU_PLAN'},*/
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
                                                        /*{text:'Ultima Facturación',dataIndex:'FE_ULTI_FACT',xtype: 'datecolumn', format: 'd/m/Y'},*/
                                                    ],
                                                    tbar:[
                                                        {
                                                            icon:fnc.images.uri.edit(),
                                                            text:'Cambiar Plan',
                                                            cls:'fac-btn fac-btn-primary',  
                                                            hidden: hiddenBtn,
                                                            handler:function(){                                                                
                                                                var grid=this.up('grid');
                                                                var selected=grid.getView().getSelectionModel().getSelection();  
                                                                if(selected.length==1){  
                                                                    var data=selected[0].data;
                                                                    if (data.FE_FIN==null){
                                                                        var date = new Date();
                                                                        //var firstDay = new Date(panel.CIER.NU_ANO, panel.CIER.NU_PERI-1, 1);
                                                                        var firstDay = new Date();
                
                                                                        if(panel.CIER != null){        
                                                                            firstDay = new Date(panel.CIER.NU_ANO, panel.CIER.NU_PERI-1, 1);            
                                                                        }
                                                                        
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
                                                                                            maxValue: Ext.Date.add(firstDay,Ext.Date.DAY,lastDay),
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
                                                        },
                                                        {
                                                            icon:fnc.images.uri.edit(),
                                                            text:'Cambiar Tarifa',
                                                            cls:'fac-btn fac-btn-warning',   
                                                            hidden: hiddenBtn,
                                                            handler:function(){                                                                
                                                                var grid=this.up('grid');
                                                                var selected=grid.getView().getSelectionModel().getSelection();  
                                                                if(selected.length==1){  
                                                                    var data=selected[0].data;
                                                                    if (data.FE_FIN==null){
                                                                        var w=fnc.createWindowModal({
                                                                            title:'Cambiar de Tarifa',
                                                                            height:200,
                                                                            width:300,
                                                                            items:[
                                                                                Ext.create("Ext.form.Panel",{
                                                                                    items:[                                                                                       
                                                                                        {
                                                                                            xtype:'numberfield',
                                                                                            name: 'IM_MONTO',
                                                                                            fieldLabel: 'Monto',
                                                                                            allowBlank: false
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
                                                                                                    if (values.IM_MONTO!=data.IM_MONTO){
                                                                                                        var m=new Desktop.Facturacion.Models.NEGO_SUCU_PLAN(values);
                                                                                                        values.CO_NEGO_SUCU=relacionSucursal.CO_NEGO_SUCU;
                                                                                                        fnc.setApiProxyModel(m,{create:'cambiar_tarifa/',update:'cambiar_tarifa/'});
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
                                                                                                    } else {
                                                                                                        fnc.createMsgBoxError({msg:'El valor del monto debe ser diferente al actual.'});
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
                                                        },
                                                        {
                                                            icon:fnc.images.uri.edit(),
                                                            text:'Cambiar Velocidad/Medio',
                                                            cls:'fac-btn fac-btn-success', 
                                                            disabled: btnDisabled,
                                                            hidden: hiddenBtn,
                                                            handler:function(){                                                                
                                                                var grid=this.up('grid');
                                                                var selected=grid.getView().getSelectionModel().getSelection();  
                                                                if(selected.length==1){  
                                                                    var data=selected[0].data;
                                                                    if (data.FE_ULTI_FACT==null){
                                                                        var combo_box=Ext.create('Ext.form.ComboBox',{
                                                                            xtype:'combobox',
                                                                            fieldLabel:'Medio Instalación',
                                                                            name:'CO_PLAN_MEDI_INST',
                                                                            displayField:'NO_PLAN_MEDI_INST',
                                                                            valueField:'CO_PLAN_MEDI_INST',
                                                                            store:storeMediInst,
                                                                            editable:false
                                                                        });
                                                                        
                                                                        combo_box.setValue(data.PLAN.CO_PLAN_MEDI_INST);
                                                                        var w=fnc.createWindowModal({
                                                                            title:'Cambiar Velocidad/Medio',
                                                                            height:250,
                                                                            width:350,
                                                                            items:[
                                                                                Ext.create("Ext.form.Panel",{
                                                                                    items:[
                                                                                        {
                                                                                            xtype:'textfield',
                                                                                            name: 'DE_VELO_SUBI',
                                                                                            fieldLabel: 'Velocidad Subida',
                                                                                            allowBlank: false,
                                                                                            value:data.PLAN.DE_VELO_SUBI,
                                                                                        },
                                                                                        {
                                                                                            xtype:'textfield',
                                                                                            name: 'DE_VELO_BAJA',
                                                                                            fieldLabel: 'Velocidad Bajada',
                                                                                            allowBlank: false,
                                                                                            value:data.PLAN.DE_VELO_BAJA,
                                                                                        },
                                                                                        combo_box,
                                                                                    ],
                                                                                    buttons:[
                                                                                        {
                                                                                            text:'Guardar',
                                                                                            formBind: true, //only enabled once the form is valid
                                                                                            handler:function(){
                                                                                                var form = this.up('form').getForm();
                                                                                                console.log("aqui");
                                                                                                if (form.isValid()) {
                                                                                                    console.log("1");
                                                                                                    var values=form.getFieldValues();
                                                                                                    var m=new Desktop.Facturacion.Models.NEGO_SUCU_PLAN();
                                                                                                    values.CO_NEGO_SUCU=relacionSucursal.CO_NEGO_SUCU;
                                                                                                    values.CO_NEGO_SUCU_PLAN=data.CO_NEGO_SUCU_PLAN;
                                                                                                    console.log(values);
                                                                                                    fnc.setApiProxyModel(m,{create:'cambiar_velocidad_medio/',update:'cambiar_velocidad_medio/'});
                                                                                                    m.save({
                                                                                                        params:values,
                                                                                                        success: function (record, operation) {
                                                                                                            storePlanes.load();
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
                                                                        fnc.createMsgBoxError({msg:'Este plan ya ha facturado. No se puede cambiar Velocidad/Medio.'});
                                                                    }
                                                                }                                                                                                                           
                                                                
                                                                
                                                            }
                                                        },
                                                        {
                                                            icon:fnc.images.uri.edit(),
                                                            text:'Eliminar Plan',
                                                            cls:'fac-btn fac-btn-danger', 
                                                            hidden: hiddenBtn,                                                       
                                                            handler:function(){                                                                
                                                                var grid=this.up('grid');
                                                                var selected=grid.getView().getSelectionModel().getSelection();  
                                                                if(selected.length==1){  
                                                                    var data=selected[0].data;
                                                                    if (data.FE_ULTI_FACT==null){
                                                                        var store=grid.getStore();
                                                                        
                                                                        if (store.data.items.length>1){
                                                                            var model=selected[0]; 
                                                                            fnc.createMsgConfirm({
                                                                                msg:'Esta seguro que desea eliminar el plan con OIT '+ data.CO_OIT_INST,
                                                                                call:function(btn){
                                                                                    if(btn=='yes'){
                                                                                        store.destroy({
                                                                                            params:model.data,
                                                                                            callback:function(a,b,c){
                                                                                                storePlanes.load();
                                                                                            }
                                                                                        });
                                                                                    }                                
                                                                                }
                                                                            });
                                                                        } else {
                                                                            fnc.createMsgBoxError({msg:'La sucursal no puede quedar sin plan. Eliminar la sucursal.'});
                                                                        }
                                                                        
                                                                    }else{
                                                                        fnc.createMsgBoxError({msg:'Este plan ya ha facturado. No es posible eliminar.'});
                                                                    }
                                                                }                                                                                                                           
                                                                
                                                            }
                                                        }
                                                    ]
                                                }
                                            ]
                                        } ,
                                        //Servicios Suplementarios
                                        {
                                            xtype:'fieldset',
                                            title: 'Otros Servicios Contratados',
                                            items:[
                                                {
                                                    xtype:'grid',
                                                    store:storeServiciosSuplementarios,
                                                    height: 200,
                                                    width: '100%',
                                                    columns:[
                                                        /*{text:'Codigo',width:'7%',dataIndex:'CO_NEGO_SUCU_SERV_SUPL'},*/
                                                        {text:'OIT INST.',width:'50px',renderer:function(a,b,c){return c.data.CO_OIT_INST?((c.data.ST_SOAR_INST?'O':'S')+c.data.CO_OIT_INST):"";}},
                                                        {text:'OIT DESI.',width:'50px',renderer:function(a,b,c){return c.data.CO_OIT_DESI?((c.data.ST_SOAR_DESI?'O':'S')+c.data.CO_OIT_DESI):"";}},
                                                        {text:'Servicio',width:'14%',dataIndex:'NO_SERV_SUPL'},
                                                        {text:'Moneda',width:'8%',dataIndex:'SERV_SUPL.MONE_FACT.NO_MONE_FACT'},
                                                        {text:'Monto',width:'8%',dataIndex:'IM_MONTO'},
                                                        {text:'Activación',width:'15px',dataIndex:'FE_INIC',xtype: 'datecolumn', format: 'd/m/Y'},
                                                        {text:'Desactivación',width:'15px',dataIndex:'FE_FIN',xtype: 'datecolumn', format: 'd/m/Y'},
                                                        /*{text:'Ultima Facturación',width:'12%',dataIndex:'FE_ULTI_FACT',xtype: 'datecolumn', format: 'd/m/Y'},*/
                                                        {text:'Detracción',width:'15px',dataIndex:'ST_AFEC_DETR',renderer:function(a,b,c){return c.data.ST_AFEC_DETR?'SI':'NO';}},
                                                        

                                                        /*{
                                                            xtype:'actioncolumn',
                                                            width:50,
                                                            items: [{                                                            
                                                                icon:fnc.images.uri.edit(),
                                                                tooltip: 'Editar',
                                                                handler: function(grid, rowIndex, colIndex) {
                                                                    var rec = grid.getStore().getAt(rowIndex);
                                                                    alert("Editar " + rec.get('NO_SERV_SUPL'));
                                                                }
                                                            }]
                                                        }*/
                                                    ],
                                                    tbar:[
                                                        {
                                                            icon:fnc.images.uri.edit(),
                                                            text:'Desactivar Servicio',
                                                            cls:'fac-btn fac-btn-danger', 
                                                            hidden: hiddenBtn,
                                                            handler:function(){
                                                                var grid=this.up('grid');
                                                                var selected=grid.getView().getSelectionModel().getSelection();
                                                                if(selected.length==1){  
                                                                    var data=selected[0].data;
                                                                    if (data.FE_FIN==null){
                                                                        
                                                                        //var firstDay = new Date(panel.CIER.NU_ANO, panel.CIER.NU_PERI-1, 1);
                                                                        
                                                                        var firstDay = new Date();
                
                                                                        if(panel.CIER != null){        
                                                                            firstDay = new Date(panel.CIER.NU_ANO, panel.CIER.NU_PERI-1, 1);            
                                                                        }
                                                                        
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
                                                                                            maxValue: Ext.Date.add(firstDay,Ext.Date.DAY,lastDay)
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
                                                                                                    values.CO_SERV_SUPL=data.CO_SERV_SUPL; 
                                                                                                    values.CO_NEGO_SUCU_SERV_SUPL=data.CO_NEGO_SUCU_SERV_SUPL;
                                                                                                    values.CO_NEGO_SUCU=data.CO_NEGO_SUCU;
                                                                                                    values.FE_INIC=data.FE_INIC;
                                                                                                    var m=new Desktop.Facturacion.Models.NEGO_SUCU_SERV_SUPL(values);
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
                                                            hidden: hiddenBtn,
                                                            handler:function(){
								if (relacionSucursal.FE_FIN==null){
                                                                    var date = new Date();
                                                                    
                                                                    //var firstDay = new Date(panel.CIER.NU_ANO, panel.CIER.NU_PERI-1, 1);
                                                                    
                                                                    var firstDay = new Date();
                
                                                                    if(panel.CIER != null){        
                                                                       firstDay = new Date(panel.CIER.NU_ANO, panel.CIER.NU_PERI-1, 1);            
                                                                    }
                                                                    
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
                                                                                        maxValue: Ext.Date.add(firstDay,Ext.Date.DAY,lastDay),
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
                                                        },
                                                        {
                                                            icon:fnc.images.uri.edit(),
                                                            text:'Eliminar Servicio',
                                                            cls:'fac-btn fac-btn-danger', 
                                                            hidden: hiddenBtn,
                                                            handler:function(){                                                                
                                                                var grid=this.up('grid');
                                                                var selected=grid.getView().getSelectionModel().getSelection();  
                                                                if(selected.length==1){  
                                                                    var data=selected[0].data;
                                                                    if (data.FE_ULTI_FACT==null){
                                                                        var store=grid.getStore();
                                                                        var model=selected[0]; 
                                                                        fnc.createMsgConfirm({
                                                                            msg:'Esta seguro que desea eliminar el servicio con OIT '+ data.CO_OIT_INST,
                                                                            call:function(btn){
                                                                                if(btn=='yes'){
                                                                                    store.destroy({
                                                                                        params:model.data,
                                                                                        callback:function(a,b,c){
                                                                                            storeServiciosSuplementarios.load();
                                                                                        }
                                                                                    });
                                                                                }                                
                                                                            }
                                                                        });
                                                                    }else{
                                                                        fnc.createMsgBoxError({msg:'Este servicio ya ha facturado. No es posible eliminar.'});
                                                                    }
                                                                }                                                                                                                           
                                                                
                                                            }
                                                        }
                                                    ]                                                    
                                                }
                                            ]
                                            }  ,
                                        //Servicios Unicos
                                        {
                                            xtype:'fieldset',
                                            title: 'Servicios Unicos',
                                            items:[
                                                {
                                                    xtype:'grid',
                                                    store:storeServiciosUnicos,
                                                    height: 200,
                                                    width: '100%',
                                                    columns:[
                                                        /*{text:'Codigo',width:'7%',dataIndex:'CO_NEGO_SUCU_SERV_SUPL'},*/
                                                        {text:'OIT INST.',width:'50px',renderer:function(a,b,c){return c.data.CO_OIT_INST?((c.data.ST_SOAR_INST?'O':'S')+c.data.CO_OIT_INST):"";}},
                                                        {text:'Servicio',width:'14%',dataIndex:'NO_SERV_UNIC'},
                                                        {text:'Moneda',width:'8%',dataIndex:'SERV_UNIC.MONE_FACT.NO_MONE_FACT'},
                                                        {text:'Monto',width:'8%',dataIndex:'IM_MONTO'},
                                                        {text:'Cobrado',width:'12%',renderer:function(a,b,c){if(c.data.CO_CIER_COBR==null || c.data.CO_CIER_COBR==panel.CIER.CO_CIER){return "NO";}else{return "SI";}}},
                                                        {text:'Detracción',width:'15px',dataIndex:'ST_AFEC_DETR',renderer:function(a,b,c){return c.data.ST_AFEC_DETR?'SI':'NO';}},                                                   

                                                        /*{
                                                            xtype:'actioncolumn',
                                                            width:50,
                                                            items: [{                                                            
                                                                icon:fnc.images.uri.edit(),
                                                                tooltip: 'Editar',
                                                                handler: function(grid, rowIndex, colIndex) {
                                                                    var rec = grid.getStore().getAt(rowIndex);
                                                                    alert("Editar " + rec.get('NO_SERV_SUPL'));
                                                                }
                                                            }]
                                                        }*/
                                                    ],
                                                    tbar:[
                                                        {
                                                            icon:fnc.images.uri.edit(),
                                                            text:'Agregar Servicio Unico',
                                                            cls:'fac-btn fac-btn-warning', 
                                                            hidden: hiddenBtn,
                                                            handler:function(){
								if (relacionSucursal.FE_FIN==null){
                                                                    var w=fnc.createWindowModal({
                                                                        title:'Agregar Servicio Unico',
                                                                        height:550,
                                                                        width:500,
                                                                        items:[
                                                                            Ext.create("Ext.form.Panel",{
                                                                                items:[
                                                                                    {
                                                                                        xtype:'Desktop.Facturacion.Widgets.selectServiciosUnicosByProducto',
                                                                                        anchor:'100%',
                                                                                        CO_PROD:panel.NEGO.CO_PROD,
                                                                                        CO_MONE_FACT:panel.NEGO.CO_MONE_FACT,
                                                                                        packageParent:{package:panel.package,varSend:varSendPackage},
                                                                                        desktop:desktop,
                                                                                        allowBlank: false
                                                                                    },
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
                                                                                                var m=new Desktop.Facturacion.Models.NEGO_SUCU_SERV_UNIC(values);                                                                                            
                                                                                                var dataSend=m.data;
                                                                                                fnc.setApiProxyModel(m,{create:'agregar_servicio/',update:'agregar_servicio/'});
                                                                                                dataSend.CO_SERV_UNIC_ARR=[];
                                                                                                var store=formCmp.down("grid").getStore();
                                                                                                for(var i=0;i<store.data.items.length;i++){
                                                                                                    dataSend.CO_SERV_UNIC_ARR.push(store.data.items[i].data.CO_SERV_UNIC);
                                                                                                }
                                                                                                m.save({
                                                                                                    params:dataSend,
                                                                                                    success: function (record, operation) {
                                                                                                        storeServiciosUnicos.load();
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
                                                        },
                                                        {
                                                            icon:fnc.images.uri.edit(),
                                                            text:'Eliminar Servicio Unico',
                                                            cls:'fac-btn fac-btn-danger',  
                                                            hidden: hiddenBtn,
                                                            handler:function(){                                                                
                                                                var grid=this.up('grid');
                                                                var selected=grid.getView().getSelectionModel().getSelection();  
                                                                if(selected.length==1){  
                                                                    var data=selected[0].data;
                                                                    if (data.CO_CIER_COBR==null || data.CO_CIER_COBR==panel.CIER.CO_CIER){
                                                                        var store=grid.getStore();
                                                                        var model=selected[0]; 
                                                                        fnc.createMsgConfirm({
                                                                            msg:'Esta seguro que desea eliminar el servicio unico con OIT '+ data.CO_OIT_INST,
                                                                            call:function(btn){
                                                                                if(btn=='yes'){
                                                                                    store.destroy({
                                                                                        params:model.data,
                                                                                        callback:function(a,b,c){
                                                                                            storeServiciosUnicos.load();
                                                                                        }
                                                                                    });
                                                                                }                                
                                                                            }
                                                                        });
                                                                    }else{
                                                                        fnc.createMsgBoxError({msg:'Este servicio unico ya ha facturado. No es posible eliminar.'});
                                                                    }
                                                                }                                                                                                                           
                                                                
                                                            }
                                                        }
                                                    ]                                                    
                                                }
                                            ]
                                            }   ,
                                        //Promociones
                                        {
                                            xtype:'fieldset',
                                            title: 'Promociones de Sucursal',
                                            items:[
                                                {
                                                    xtype:'grid',
                                                    store:storeNegoSucuProm,
                                                    columns:[
                                                        {text:'Tipo',dataIndex:'DE_TIPO_DISPLAY'},
                                                        {text:'Valor',dataIndex:'IM_VALO'},
                                                        {text:'Afecta Plan',dataIndex:'ST_PLAN_DISPLAY'},
                                                        {text:'Afecta Serv. Supl.',dataIndex:'ST_SERV_SUPL_DISPLAY'},
                                                        {text:'Año Inicio',dataIndex:'DE_ANO_INIC'},
                                                        {text:'Periodo Inicio',dataIndex:'DE_PERI_INIC'},
                                                        {text:'Año Fin',dataIndex:'DE_ANO_FIN'},
                                                        {text:'Periodo Fin',dataIndex:'DE_PERI_FIN'},
                                                    ],
                                                    tbar:[
                                                        {
                                                            text:'Agregar Promoción por sucursal',
                                                            cls:'fac-btn fac-btn-warning', 
                                                            hidden: hiddenBtn,
                                                            handler:function(){
								if (relacionSucursal.FE_FIN==null){                                                                
                                                                    var record=new Desktop.Facturacion.Models.NEGO_SUCU_PROM();
                                                                    fnc.addParameterModel(record,varSendPackage,panel.package);
                                                                    var frm=Ext.create('Ext.form.Panel',{
                                                                        model:'NEGO_SUCU_PROM',
                                                                        items:[
                                                                            {
                                                                                xtype:'combobox',
                                                                                fieldLabel:'Tipo',
                                                                                name:'DE_TIPO',
                                                                                store:Ext.create('Ext.data.Store',{fields:['CO','VAL'],data:[{CO:1,VAL:'Porcentaje'},{CO:2,VAL:'Monto'}]}),
                                                                                displayField:'VAL',
                                                                                valueField:'CO',
                                                                                allowBlank: false,
                                                                                editable:false                                                                   
                                                                            },
                                                                            {
                                                                                xtype:'numberfield',
                                                                                fieldLabel:'Valor',
                                                                                name:'IM_VALO',
                                                                                allowBlank: false,
                                                                            },                                                                
                                                                            {
                                                                                xtype:'checkbox',
                                                                                fieldLabel:'Aplicar a plan',
                                                                                name:'ST_PLAN',
                                                                            },
                                                                            {
                                                                                xtype:'checkbox',
                                                                                fieldLabel:'Aplicar a Servicio suplementario',
                                                                                name:'ST_SERV_SUPL',
                                                                            }, 
                                                                            {
                                                                                xtype:'combobox',
                                                                                fieldLabel:'Periodo Inicio',
                                                                                name:'DE_PERI_INIC',
                                                                                store:Ext.create('Ext.data.Store',{fields:['CO','VAL'],data:[{CO:1,VAL:'Enero'},{CO:2,VAL:'Febrero'},{CO:3,VAL:'Marzo'},{CO:4,VAL:'Abril'},{CO:5,VAL:'Mayo'},{CO:6,VAL:'Junio'},{CO:7,VAL:'Julio'},{CO:8,VAL:'Agosto'},{CO:9,VAL:'Septiembre'},{CO:10,VAL:'Octubre'},{CO:11,VAL:'Noviembre'},{CO:12,VAL:'Diciembre'}]}),
                                                                                displayField:'VAL',
                                                                                valueField:'CO', 
                                                                                allowBlank: false,
                                                                                editable:false                                                             
                                                                            },
                                                                            {
                                                                                xtype:'numberfield',
                                                                                fieldLabel:'Año Inicio',
                                                                                name:'DE_ANO_INIC',
                                                                                allowBlank: false,
                                                                                minValue:2000,
                                                                            },  
                                                                            {
                                                                                xtype:'combobox',
                                                                                fieldLabel:'Periodo Fin',
                                                                                name:'DE_PERI_FIN',
                                                                                store:Ext.create('Ext.data.Store',{fields:['CO','VAL'],data:[{CO:1,VAL:'Enero'},{CO:2,VAL:'Febrero'},{CO:3,VAL:'Marzo'},{CO:4,VAL:'Abril'},{CO:5,VAL:'Mayo'},{CO:6,VAL:'Junio'},{CO:7,VAL:'Julio'},{CO:8,VAL:'Agosto'},{CO:9,VAL:'Septiembre'},{CO:10,VAL:'Octubre'},{CO:11,VAL:'Noviembre'},{CO:12,VAL:'Diciembre'}]}),
                                                                                displayField:'VAL',
                                                                                valueField:'CO', 
                                                                                allowBlank: false,
                                                                                editable:false                                                             
                                                                            },
                                                                            {
                                                                                xtype:'numberfield',
                                                                                fieldLabel:'Año Fin',
                                                                                name:'DE_ANO_FIN',
                                                                                allowBlank: false,
                                                                                minValue:2000,
                                                                            },                                                              
                                                                        ],
                                                                        buttons:[
                                                                            {
                                                                                text:'Guardar',
                                                                                handler:function(){
                                                                                    var viewForm = this.up('form');
                                                                                    var form=viewForm.getForm();
                                                                                    var record=form.getRecord();
                                                                                    if (form.isValid()){
                                                                                        form.updateRecord(record);                                                                    
                                                                                        record.save({params:record.data,success:function(){storeNegoSucuProm.load();}});
                                                                                        win.close();
                                                                                    }                                                                        
                                                                                }
                                                                            }
                                                                        ]
                                                                    });                                                
                                                                    record.data.CO_NEGO_SUCU=relacionSucursal.CO_NEGO_SUCU;
                                                                    frm.getForm().loadRecord(record);
                                                                    var win=fnc.createWindowModal({
                                                                        title:'Agregar promoción por sucursal',
                                                                        layout:'fit',
                                                                        items:[   
                                                                            frm
                                                                        ]
                                                                    });
                                                                    win.show();
                                                                }else{
                                                                    fnc.createMsgBoxError({msg:'Esta sucursal se encuentra desactivada.'})
                                                                }
                                                            }
                                                        }
                                                    ]
                                                }
                                            ]
                                        }
                                    ],
                                    tools: [
                                        {
                                            type:'refresh',
                                            tooltip : 'Ver Historial de cambios',
                                            handler:function(){
                                                var storeNegoSucuHistorico=new Desktop.Facturacion.Stores.NEGO_SUCU_HISTORICO();
                                                fnc.addParameterStore(storeNegoSucuHistorico,varSendPackage,panel.package);
//                                                fnc.addParameterStore(storeNegoSucuHistorico,"CO_NEGO",panel.NEGO.CO_NEGO);
                                                fnc.addParameterStore(storeNegoSucuHistorico,"CO_NEGO_SUCU",relacionSucursal.CO_NEGO_SUCU);
                                                fnc.setApiProxyStore(storeNegoSucuHistorico,{read:'selectByCO_NEGO_SUCU/'});
                                                storeNegoSucuHistorico.load();

                                                var winHis=fnc.createWindowModal({
                                                    title:'Historial Sucursal'+direccionUbicacionSucursal,
                                                    layout:'fit',
                                                    width:750,
                                                    height:400,
                                                    items:[
                                                        {
                                                            xtype:'grid',
                                                            layout:'fit',
                                                            store:storeNegoSucuHistorico,
                                                            columns:[
                                                                {text:'Información',dataIndex:'DE_INFORMACION',width:400},
                                                                {text:'Usuario',dataIndex:'USUA.DE_USER'},
                                                                {text:'Fecha',dataIndex:'FH_REGI', xtype: 'datecolumn',format: 'd/m/Y H:i:s',width:200},
                                                            ]                                                                                    
                                                        }
                                                    ]
                                                });
                                                winHis.show();
                                                
//                                                var winHis=fnc.createWindowHistorial({title:"Historial Sucursal "+direccionUbicacionSucursal,type:'negocio_sucursal',idEntity:relacionSucursal.CO_NEGO_SUCU,AJAX_URI:AJAX_URI,varSendPackage:varSendPackage,packageBase:panel.package});
//                                                winHis.show();
                                            }
                                        }
                                    ]
                                });
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
        items:[            {
                title: 'Panel 1',
                flex: 1,
                margin: '0 0 10 0',
                html: 'Cargando...'
            } ],
        tbar:[
            {   
                xtype: 'button',
                text: "Agregar Sucursal",
                tooltip: "Agregar Sucursal",
                name:'btnAgregarSucursal',
                iconCls: fnc.icons.class.insert(),
                hidden: hiddenBtn,
            }, "-",
            {   
                xtype: 'button',
                text: "Preview Factura",
                tooltip: "Preview Factura",
                name:'btnPreviewFactura',
                iconCls: fnc.icons.class.insert(),
                handler:function(){
                    var panel=this.up("panel");
                    var nego=panel.NEGO;
                    var params={CO_NEGO:nego.CO_NEGO};                    
                    params[varSendPackage]=panel.package;
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
                    //panel.NEGO.CO_NEGO
                }
            }, "-",
            {   
                xtype: 'button',
                text: "Cesión contractual",
                tooltip: "Cesión contractual",
                name:'btnCesionContractual',
                iconCls: fnc.icons.class.insert(),
                hidden: hiddenBtn,
                handler:function(){
                    var panel=this.up('panel');
                    var grid=Ext.create('Ext.grid.Panel',{  
                        layout:'fit',
                        anchor: '100% 60%',
                        columns:[
                            {text:'OIT Instalación',dataIndex:'OIT_INST'},
                            {text:'Dirección',width:300,dataIndex:'DE_DIRE'},
                            {text:'Departamento',width:100,dataIndex:'DE_DEPA'},
                            {text:'Provincia',width:100,dataIndex:'DE_PROV'},
                            {text:'Distrito',width:100,dataIndex:'DE_DIST'},
                            {text: 'Oit SOARC Desinstalación',dataIndex:'ST_SOAR_DESI',xtype:'checkcolumn'},
                            {text: 'OIT Desinstalación',dataIndex:'CO_OIT_DESI',editor: {xtype:'numberfield'}},
                            {text: 'Oit SOARC',dataIndex:'ST_SOAR_INST_NEW',xtype:'checkcolumn'},
                            {text: 'Nueva OIT Inst.',dataIndex:'CO_OIT_INST_NEW',editor: {xtype:'numberfield'}},                            
                            {text: 'Observación',dataIndex:'DE_OBSERV',editor: {xtype:'textfield'}}
                            
                        ],
                        store:null,
                        plugins: {
                            ptype: 'cellediting',
                            clicksToEdit: 1
                        },
                        selModel: Ext.create('Ext.selection.CheckboxModel', { checkOnly: true }),
                    });
                    
                    var store_nego_sucu=new Desktop.Facturacion.Stores.NEGO_SUCU();
                    fnc.addParameterStore(store_nego_sucu,varSendPackage,panel.package);          
                    fnc.setApiProxyStore(store_nego_sucu,{read:'selectActivosByNego/'});
                    
                    store_nego_sucu.load({
                        params:{CO_NEGO:panel.NEGO.CO_NEGO},
                        callback:function(a,b,c){
                            if(c){ 
                                var data=[];
                                for(var i=0;i<store_nego_sucu.data.items.length;i++){
                                    var reg=store_nego_sucu.data.items[i].data;
                                    data.push({
                                        CO_NEGO_SUCU:reg.CO_NEGO_SUCU,
                                        OIT_INST:(reg.ST_SOAR_INST?'O':'S')+reg.CO_OIT_INST,
                                        DE_DIRE:reg.SUCU.DE_DIRE,
                                        DE_DEPA:reg.SUCU.DIST.PROV.DEPA.NO_DEPA,
                                        DE_PROV:reg.SUCU.DIST.PROV.NO_PROV,
                                        DE_DIST:reg.SUCU.DIST.NO_DIST,
                                        ST_SOAR_DESI:false,
                                        CO_OIT_DESI:'',
                                        ST_SOAR_INST_NEW:false,
                                        CO_OIT_INST_NEW:'',
                                        DE_OBSERV:''
                                    });
                                }
                                var store=Ext.create('Ext.data.Store',{
                                   fields:[
                                       {name:'CO_NEGO_SUCU'},
                                       {name:'OIT_INST'},
                                       {name:'DE_DIRE'},
                                       {name:'DE_DEPA'},
                                       {name:'DE_PROV'},
                                       {name:'DE_DIST'},
                                       {name:'ST_SOAR_DESI'},
                                       {name:'CO_OIT_DESI'},
                                       {name:'ST_SOAR_INST_NEW'},
                                       {name:'CO_OIT_INST_NEW'},
                                       {name:'DE_OBSERV'}
                                   ],
                                   data:data
                                });
                                grid.setStore(store);
                                //grid.setStore(store_nego_sucu);
                            }
                        }
                    });

                    var wincc=fnc.createWindowModal({
                        title:'Realizar cesión contractual',
                        layout:'fit',
                        width:1300,
                        height:400,
                        items:[
                            {
                                xtype:'form',
                                layout:'anchor',
                                items:[
                                    {
                                        xtype:'numberfield',
                                        fieldLabel:'Nuevo negocio', 
                                        allowBlank: false,
                                        name:'CO_NEGO',
                                        anchor: '50% 10%',
                                    },
                                    {
                                        xtype:'Desktop.Facturacion.Widgets.selectCliente',
                                        packageParent:{package:panel.package,varSendPackage:varSendPackage},
                                        anchor: '100% 20%',
                                    },
                                    grid,
                                ],
                                buttons:[
                                    {
                                        text:'Realizar cesión contractual',
                                        formBind: true,
                                        handler:function(){
                                            var element=this.up('form');
                                            var form=element.getForm();
                                            if (form.isValid()){
                                                var grid=element.down('grid');
                                                var selections=grid.getSelectionModel().getSelection();
                                                if (selections && selections.length>0){
                                                    var ARR_CO_NEGO_SUCU=[];
                                                    var ARR_ST_SOAR_INST_NEW=[];
                                                    var ARR_CO_OIT_INST_NEW=[];

                                                    var ARR_ST_SOAR_DESI=[];
                                                    var ARR_CO_OIT_DESI=[];
                                                    var ARR_DE_OBSERV=[];
                                                    var todoOk=true;
                                                    for(var i=0;i<selections.length && todoOk;i++){
                                                        if (selections[i].data.CO_NEGO_SUCU && selections[i].data.CO_OIT_DESI && selections[i].data.CO_OIT_INST_NEW ){
                                                            ARR_CO_NEGO_SUCU.push(selections[i].data.CO_NEGO_SUCU);
                                                            ARR_ST_SOAR_DESI.push(selections[i].data.ST_SOAR_DESI);
                                                            ARR_CO_OIT_DESI.push(selections[i].data.CO_OIT_DESI);
                                                            ARR_ST_SOAR_INST_NEW.push(selections[i].data.ST_SOAR_INST_NEW);
                                                            ARR_CO_OIT_INST_NEW.push(selections[i].data.CO_OIT_INST_NEW);
                                                            ARR_DE_OBSERV.push(selections[i].data.DE_OBSERV);
                                                        }else{
                                                            todoOk=false;
                                                        }
                                                    }
                                                    if (todoOk){
                                                        var data=form.getFieldValues();
                                                        var params={CO_NEGO:data.CO_NEGO,CO_CLIE:data.CO_CLIE,ARR_CO_NEGO_SUCU:ARR_CO_NEGO_SUCU,ARR_ST_SOAR_INST_NEW:ARR_ST_SOAR_INST_NEW,ARR_CO_OIT_INST_NEW:ARR_CO_OIT_INST_NEW,ARR_ST_SOAR_DESI:ARR_ST_SOAR_DESI,ARR_CO_OIT_DESI:ARR_CO_OIT_DESI,ARR_DE_OBSERV:ARR_DE_OBSERV,CO_CIER:panel.CIER.CO_CIER};                                                         
                                                        params[varSendPackage]=panel.package;
                                                        Ext.Ajax.request({
                                                            method:'POST',
                                                            params:params,
                                                            url: AJAX_URI+'NEGO_SUCU/REAL_CESI_CONT/',
                                                            success: function(response, opts) {
                                                                var json=fnc.responseExtAjaxRequestMsjError(response);
                                                                if(json.success){
                                                                    fnc.createMsgBoxSuccess({msg:'Se realizo la cesión contractual de manera satisfactoria.'})
                                                                    wincc.close();
                                                                }
                                                            }
                                                        });                                                        
                                                    }else{
                                                        fnc.createMsgBoxError({msg:'De las sucursales seleccionadas existe parametros ingresados de manera incorrecta.'});
                                                    }
                                                }else{
                                                    fnc.createMsgBoxError({msg:'No ha seleccionado ninguna sucursal'});
                                                }
                                            }
                                        }
                                    }
                                ]
                            }
                        ]
                    });
                    wincc.show();
                }
            }, "-",            
            {
                xtype:'fieldset',
                border: false,
                items:[
                    {
                        xtype:'textfield',
                        emptyText:'Buscar...',
                        style:{
                            float:'left',
                        },
                        width:'150px',
                        fieldStyle:"height:24px;padding-right: 33px;",
                        enableKeyEvents:true,
                        listeners:{
                            keypress:function(obj,e,eOpts){
                                if (e.charCode==13)
                                   filtrarSucursales(this);
                            }
                        }
                    },{
                        xtype:'button',
                        text:'Buscar',
                        style:{
                            marginLeft: '-30px',
                            paddingTop: '0px',
                            height: '26px',
                        },
                        listeners:{
                            click:function(){
                                filtrarSucursales(this);
                            }
                        }
                    },{
                        xtype: 'label',
                        text: '',
                        margin: '0 0 0 10'
                    }                    
                ]
            },
            '-',
            {
                text:'Ver Cortes',
                iconCls:fnc.icons.class.ver(),
                handler:function(){
                    var panel=this.up("panel");
                    var nego=panel.NEGO;                    
                    var storeCortes=new Desktop.Facturacion.Stores.CORT();
                    fnc.addParameterStore(storeCortes,varSendPackage,panel.package);          
                    fnc.addParameterStore(storeCortes,"CO_NEGO",nego.CO_NEGO);          
                    fnc.setApiProxyStore(storeCortes,{read:'selectByNego/'});                    
                    var winCor=fnc.createWindow({
                        title:'Cortes (morosidad) - Negocio '+nego.CO_NEGO,
                        items:[
                            {
                                xtype:'grid',
                                store:storeCortes,
                                columns:[
                                    {text:'Fecha inicio',dataIndex:'FE_INIC', xtype: 'datecolumn', format: 'd/m/Y'},
                                    {text:'Fecha fin',dataIndex:'FE_FIN', xtype: 'datecolumn', format: 'd/m/Y'},
                                    {text:'Estado',dataIndex:'DE_ESTADO'}
                                ]
                            }
                        ]
                    });
                    winCor.show();
                }
            }
        ],
        loadStructure:function(panel,CO_NEGO,CO_PROD,desktopPar,package){
            panel.package=package;
            //rordonez
            fnc.load.ModelsAndStores(["PROD","SUCU","PLAN","SERV_SUPL","NEGO","NEGO_CORR","NEGO_SUCU","CIER","NEGO_PROM", "NEGO_COME","NEGO_SUCU_PROM","NEGO_SUCU_PLAN","NEGO_SUCU_SERV_SUPL","NEGO_SUCU_SERV_UNIC","CORT","SUSP","MOTI_DESC","MIGR_NEGO_SUCU","NEGO_SUCU_HISTORICO","PLAN_MEDI_INST"],function(){
                panel.stores={
                    nego:new Desktop.Facturacion.Stores.NEGO(),
                    nego_sucu:new Desktop.Facturacion.Stores.NEGO_SUCU(),                    
                    cier:new Desktop.Facturacion.Stores.CIER()
                };
                for(var k in panel.stores){
                    fnc.addParameterStore(panel.stores[k],varSendPackage,panel.package);
                }            
                fnc.setApiProxyStore(panel.stores.nego_sucu,{read:'selectByNego/'});
                fnc.setApiProxyStore(panel.stores.cier,{read:'selectCierrePendienteByNego/'});
                panel.stores.nego.load({params:{id:CO_NEGO},callback:function(){
                    panel.stores.cier.load({
                        params:{CO_PROD:CO_PROD},callback:function(){
                            var desktop=desktopPar;
                            var nego=panel.stores.nego.data.items;
                            var cier=panel.stores.cier.data.items;
                            if(nego.length==1){
                                nego=nego[0].data;
                                panel.NEGO=nego;
                                
                                if(cier[0] != null){
                                    
                                  cier=cier[0].data;                                
                                  panel.CIER=cier;
                                }                                

                                
                                panel.createViewsSucursales(desktop);
                                fnc.load.scripts([
                                    "widgets/selectSucursalByCliente.js",
                                    "widgets/selectPlanByProducto.js",
                                    "widgets/selectServiciosSuplementariosByProducto.js",
                                    "widgets/selectServiciosUnicosByProducto.js"
                                ],function(){ 
                                    fnc.load.ModelsAndStores([],function(){
                                        panel.query("[name='btnAgregarSucursal']")[0].on('click', function() {
                                        if (panel.NEGO.isDesactivado){
                                            fnc.createMsgBoxError({msg:'Este negocio se encuentra desactivado.'});
                                        } else {
                                            
                                            var lastDay=0;  
                                            var store=new Desktop.Facturacion.Stores.PROD_PADRE();
                                            fnc.addParameterStore(store,varSendPackage,panel.package);
                                            fnc.setApiProxyStore(store,{read:'getFechaByProd/'});
                                            store.load({
                                                 params:{
                                                        CO_PROD:nego.PROD_PADRE.COD_PROD_PADRE
                                                        },
                                                        callback: function(records, operation, success) {
                                                            if (success){
                                                                if (records.length==1){
                                                                       var data=records[0].data;
                                                                       //lastDay = new Date(panel.CIER.NU_ANO, panel.CIER.NU_PERI-1,data.FECHA_FIN);
                                                                       lastDay =data.FECHA_FIN;
                                                                        var win=getWindowAgregarSucursal(nego,desktop,panel,lastDay);
                                                                        win.show();
                                                                       //alert(lastDay);
                                                                }
                                                            }
                                                        }            
                                            });
                                                                                                                                 
                                        }
                                    }); 
                                    });
                                });
                            }
                        }
                    });
                    
                }});
            });            
        }
    });
    function getWindowAgregarSucursal(nego,desktop,panel,lastDay){
        //Relaiza la instalación                        
        
        
        var date = new Date();                        
        var firstDay = new Date();
                
        if(panel.CIER != null){        
            firstDay = new Date(panel.CIER.NU_ANO, panel.CIER.NU_PERI-1, 1);            
        }                
        //var feche_final = Ext.Date.add(firstDay,Ext.Date.DAY,lastDay);
        //alert();
        var frm=Ext.create('Ext.form.Panel',{
            model:'Desktop.Facturacion.Models.NEGO_SUCU',
            overflowY:'scroll',
            items:[
               {xtype:'Desktop.Facturacion.Widgets.selectSucursalByCliente',getCO_CLIE:function(){return nego.CLIE.CO_CLIE;},packageParent:{package:panel.package,varSendPackage:varSendPackage},desktop:desktop },
               {
                    xtype: 'datefield',
                    fieldLabel:'Fecha de Inicio',
                    name:'FE_INIC',
//                    minDate: new Date(),
                    maxValue: Ext.Date.add(firstDay,Ext.Date.DAY,lastDay),
                    allowBlank: false,
               },{
                   xtype: 'checkboxfield',
                   fieldLabel:'OIT SOARC',
                   checked:false,
                   boxLabel  : 'OIT SOARC',
                   name:'ST_SOAR_INST',
                   allowBlank: false,
               },{
                    xtype:'numberfield',
                    name: 'CO_OIT_INST',
                    fieldLabel: 'OIT instalación',
                    allowBlank: true,
               },{
                    xtype:'numberfield',
                    name: 'CO_CIRC',
                    fieldLabel: 'Circuito',
                    allowBlank: true,
               },{
                    xtype:'textfield',
                    name: 'DE_ORDE_SERV',
                    fieldLabel: 'Orden de Servicio',
                    allowBlank: true,
               },{
                    xtype: 'numberfield', 
                    allowExponential: false,
                    minValue: 0,
                    name: 'DE_PLAZ_CONT',
                    fieldLabel: 'Plazo de contrato (meses)',
                    allowBlank: false,
               },{
                   xtype:'textfield',
                   name: 'REFERENCIA_NEGO_SUCU',
                   fieldLabel: 'Referencia',
                   allowBlank: true, 
               },
               new Ext.panel.Panel({
                  	title:'Plan',
                  	collapsible: true,
                  	width:600,
                  	items:[
                            {
                                xtype:'Desktop.Facturacion.Widgets.selectPlanByProducto',
                                CO_PROD:nego.CO_PROD,
                                CO_MONE_FACT:nego.CO_MONE_FACT,
                                packageParent:{package:panel.package,varSend:varSendPackage},
                                desktop:desktop,
                                allowBlank: false,
                            }        	       
                  	]
                }),
                {
                    xtype:'Desktop.Facturacion.Widgets.selectServiciosSuplementariosByProducto',
                    CO_PROD:nego.CO_PROD,
                    CO_MONE_FACT:nego.CO_MONE_FACT,
                    packageParent:{package:panel.package,varSend:varSendPackage},
                    desktop:desktop,
                    allowBlank: false,
                    width:600,
                    style:{
                        marginBottom:'10px',
                    }
                },
                {
                    xtype:'Desktop.Facturacion.Widgets.selectServiciosUnicosByProducto',
                    CO_PROD:nego.CO_PROD,
                    CO_MONE_FACT:nego.CO_MONE_FACT,
                    packageParent:{package:panel.package,varSend:varSendPackage},
                    desktop:desktop,
                    allowBlank: false,
                    width:600,
                    style:{
                        marginBottom:'10px',
                    }
                }               
           ],
           buttons:[
               {
                   text:'Guardar',
                   formBind: true,
                   handler:function(){
                        var form=this.up('form').getForm();
                        if (form.isValid()){
                            var data=form.getFieldValues();
                            data.CO_NEGO=nego.CO_NEGO;
                            var model=new Desktop.Facturacion.Models.NEGO_SUCU(data);
                            data=fnc.getParamsSendPackage(model.getData(),varSendPackage,panel.package);
                            data['CO_SERV_SUPL']=[];
                            data['CO_SERV_UNIC']=[];
                            var store=frm.query('grid')[0].getStore();
                            for(var i=0;i<store.data.items.length;i++){
                                data['CO_SERV_SUPL'].push(store.data.items[i].data.CO_SERV_SUPL);
                            } 
                            var store=frm.query('grid')[1].getStore();
                            for(var i=0;i<store.data.items.length;i++){
                                data['CO_SERV_UNIC'].push(store.data.items[i].data.CO_SERV_UNIC);
                            } 
                            model.save({params:data,callback:function(a,b,c){
                                if(c){
                                    //Mensaje donde el usuario ingresarara informacion para el historico del negocio
                                    var winHistorico=fnc.createWindowModal({
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
                                                                var formHistorico = this.up('form').getForm();
                                                                if (formHistorico.isValid()) {
                                                                    var params=formHistorico.getFieldValues();
                                                                    params.CO_NEGO=nego.CO_NEGO;
                                                                    params[varSendPackage]=panel.package;
                                                                    var model=new Desktop.Facturacion.Models.NEGO_HISTORICO(formHistorico.getFieldValues());
                                                                    model.save({params:params});
                                                                    formHistorico.reset();
                                                                    winHistorico.close();
                                                                    
                                                                    form.reset();
                                                                    win.close();
                                                                    panel.createViewsSucursales(desktop);
                                                                }
                                                            }
                                                        },{
                                                            text: 'Cancelar',
                                                            handler: function() {
                                                                winHistorico.close();
                                                            }
                                                        }
                                                    ] 
                                                }
                                            ]
                                    });
                                    winHistorico.show();
                                    /*
                                    form.reset();
                                    win.close();
                                    panel.createViewsSucursales(desktop);
                                    */
                                }else{
                                    var jsonResponse=Ext.JSON.decode(b._response.responseText);
                                    fnc.createMsgBoxError({msg:jsonResponse.msg});
                                }                                                  
                            }});  
                        }
                   }
               }
           ]
        });
       
        var win=fnc.createWindowModal(desktop,{
            title:'Agregar Sucursal en Negocio '+nego.CO_NEGO,
            width:650,
            items:[frm],
            renderTo:Ext.getBody(),
        });   
        return win;
    }
    function getWindowEditarSucursal(nego,desktop,panel,nego_sucu){
                
        var frm=Ext.create('Ext.form.Panel',{
            model:'Desktop.Facturacion.Models.NEGO_SUCU',
            items:[
               {xtype:'Desktop.Facturacion.Widgets.selectSucursalByCliente',getCO_CLIE:function(){return nego.CLIE.CO_CLIE;},packageParent:{package:panel.package,varSendPackage:varSendPackage},desktop:desktop },
               {
                    xtype: 'datefield',
                    fieldLabel:'Fecha de Inicio',
                    name:'FE_INIC',
                    minDate: new Date(),
                    allowBlank: false,
               },{
                    xtype: 'datefield',
                    fieldLabel:'Fecha de Fin',
                    name:'FE_FIN',
                    minDate: new Date(),
                    allowBlank: true,
               },{
                   xtype: 'checkboxfield',
                   fieldLabel:'OIT SOARC',
                   checked:false,
                   boxLabel  : 'OIT SOARC',
                   name:'ST_SOAR_INST',
                   allowBlank: false,
               },{
                    xtype:'numberfield',
                    name: 'CO_OIT_INST',
                    fieldLabel: 'OIT instalación',
                    allowBlank: true,
               },{
                    xtype:'numberfield',
                    name: 'CO_CIRC',
                    fieldLabel: 'Circuito',
                    allowBlank: true,
               }
           ],
           buttons:[
               {
                   text:'Guardar',
                   handler:function(){
                        var form=this.up('form').getForm();
                        if (form.isValid()){   
                            var model=form.getRecord();
                            var params=form.getFieldValues();
                            params['CO_NEGO']=nego.CO_NEGO;
                            params['CO_NEGO_SUCU']=nego_sucu.CO_NEGO_SUCU;
                            win.disable();
                            model.save({
                                params:fnc.getParamsSendPackage(params,varSendPackage,panel.package),
                                success:function(){
                                    var winHistorico=fnc.createWindowModal({
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
                                                                var formHistorico = this.up('form').getForm();
                                                                if (formHistorico.isValid()) {
                                                                    var params=formHistorico.getFieldValues();
                                                                    params.CO_NEGO=nego.CO_NEGO;
                                                                    params.CO_NEGO_SUCU=nego_sucu.CO_NEGO_SUCU;
                                                                    params[varSendPackage]=panel.package;
                                                                    var modelHistorico=new Desktop.Facturacion.Models.NEGO_SUCU_HISTORICO(formHistorico.getFieldValues());
                                                                    modelHistorico.save({params:params});
                                                                    formHistorico.reset();
                                                                    winHistorico.close();

                                                                    panel.createViewsSucursales(desktop);
                                                                    win.enable();
                                                                    win.close();
                                                                }
                                                            }
                                                        },{
                                                            text: 'Cancelar',
                                                            handler: function() {
                                                                winHistorico.close();
                                                            }
                                                        }
                                                    ] 
                                                }
                                            ]
                                    });
                                    winHistorico.show();
                                }
                            });
                                                                                 
                        }                                                                                               
                   }
               }
           ]
        });
        var win=fnc.createWindowModal(desktop,{
            title:'Editar Sucursal de Negocio '+nego.CO_NEGO,
            width:600,
            items:[frm],
            renderTo:Ext.getBody(),
        });   
        return win;
    }
})();