Ext.onReady(function(){
    fnc.load.ModelsAndStores(["PROD"],function(){
        fnc.load.scripts(["widgets/comboBoxServiciosReportes.js"],function(){        
        var package="Desktop.Facturacion.Panels.REPO.Cierre";
        var varSendPackage=settings.Permissions.varSendPackage;
        var settingsLocal=window.settings.real.get();
        
        var storeProd_Padre= new Desktop.Facturacion.Stores.PROD_PADRE();       
        fnc.addParameterStore(storeProd_Padre,varSendPackage,package); 
        
        var storeCboTipoDocumento = Ext.create('Ext.data.Store', {
            fields: ['COD_TIPO_DOC', 'DESC_TIPO_DOCUMENTO'],
            data : [
                {"COD_TIPO_DOC":"01", "DESC_TIPO_DOCUMENTO":"FACTURA"},               
                {"COD_TIPO_DOC":"02", "DESC_TIPO_DOCUMENTO":"BOLETA"}
            ]
        });
        
        
        var storePeriodo=Ext.create("Ext.data.Store",{
            fields:[{name:'CO_PERI'},{name:'NO_PERI'}],
            data:[
                {CO_PERI:1,NO_PERI:'Enero'},{CO_PERI:2,NO_PERI:'Febrero'},{CO_PERI:3,NO_PERI:'Marzo'},
                {CO_PERI:4,NO_PERI:'Abril'},{CO_PERI:5,NO_PERI:'Mayo'},{CO_PERI:6,NO_PERI:'Junio'},
                {CO_PERI:7,NO_PERI:'Julio'},{CO_PERI:8,NO_PERI:'Agosto'},{CO_PERI:9,NO_PERI:'Septiembre'},
                {CO_PERI:10,NO_PERI:'Octubre'},{CO_PERI:11,NO_PERI:'Noviembre'},{CO_PERI:12,NO_PERI:'Diciembre'}
            ]
        });
        Ext.define(package,{
            extend:'Ext.Container',
            alias:'widget.Desktop.Facturacion.Panels.REPO.Cierre',             
            layout:{
                type: 'table',
                columns:1,               
            },
            defaults: {
                xtype: 'panel',
                width: 400,
                height:220,
                bodyPadding: 10
            },
                       
            initComponent:function(){
            var me=this;
            me.varsInitComponent=me.varsInitComponent || {};
                                                  
            me.items=[
                {
                    xtype:'form',
                    title:'Reporte Maestro de gerentes',
                    collapsed: true,
                    collapsible:true,
                    items:[
                        /*
                        {
                            id : 'comboReporteMaestroGerente',    
                            xtype:'combobox',                
                            fieldLabel:'Producto',
                            name:'COD_PROD_PADRE',
                            displayField:'DESC_PROD_PADRE',
                            valueField:'COD_PROD_PADRE',
                            store:storeProd_Padre,
                            allowBlank:false,
                            editable:false,   
                        },*/
                        {
                            xtype:'Desktop.Facturacion.Widgets.comboBoxServiciosReportes',
                            packageParent:{package:package,varSend:varSendPackage},               
                            CO_PROD:me.varsInitComponent.CO_PROD,
                        },
                        {
                            xtype:'numberfield',
                            fieldLabel:'Año',
                            name:'CO_ANO'
                        },
                        {
                            xtype:'combobox',
                            fieldLabel:'Periodo',
                            store:storePeriodo,
                            displayField:'NO_PERI',
                            valueField:'CO_PERI',
                            editable:false,
                            name:'CO_PERI'
                        },
                    ],
                    buttons:[
                        {
                            text:'Generar',
                            handler:function(){
                                var form=this.up('form').getForm();
                                var values=form.getFieldValues();
                                values[varSendPackage]=package;                                
                                Ext.Ajax.request({
                                    url:settingsLocal.AJAX_URI+'REPO/cierre/gerentes/',
                                    params: values,
                                    success: function(response){
                                        var json=fnc.responseExtAjaxRequestMsjError(response);
                                        if (json.success){
                                            var tk=json.data;
                                            fnc.openURL("./ajax/REPO/cierre/gerentes/token?tk="+tk);                                            
                                        }
                                    }
                                });                                
                            }
                        }
                    ]
                },
                {
                    xtype:'form',
                    title:'Reporte Anulados',
                    collapsed: true,
                    collapsible:true,
                    items:[
                        {
                            xtype:'Desktop.Facturacion.Widgets.comboBoxServiciosReportes',
                            packageParent:{package:package,varSend:varSendPackage},               
                            CO_PROD:me.varsInitComponent.CO_PROD,
                        },
                        {
                            xtype:'numberfield',
                            fieldLabel:'Año',
                            name:'CO_ANO'
                        },
                        {
                            xtype:'combobox',
                            fieldLabel:'Periodo',
                            store:storePeriodo,
                            displayField:'NO_PERI',
                            valueField:'CO_PERI',
                            editable:false,
                            name:'CO_PERI'
                        },
                    ],
                    buttons:[
                        {
                            text:'Generar',
                            handler:function(){
                                var form=this.up('form').getForm();
                                var values=form.getFieldValues();
                                values[varSendPackage]=package;                                
                                Ext.Ajax.request({
                                    url:settingsLocal.AJAX_URI+'REPO/cierre/anulados/',
                                    params: values,
                                    success: function(response){
                                        var json=fnc.responseExtAjaxRequestMsjError(response);
                                        if (json.success){
                                            var tk=json.data;
                                            fnc.openURL("./ajax/REPO/cierre/anulados/token?tk="+tk);                                            
                                        }
                                    }
                                });                                
                            }
                        }
                    ]
                },
                {
                    xtype:'form',
                    title:'Reporte Maestro de Correspondencia',
                    collapsible:true,
                    collapsed: true,
                    items:[
                        {
                            xtype:'Desktop.Facturacion.Widgets.comboBoxServiciosReportes',
                            packageParent:{package:package,varSend:varSendPackage},               
                            CO_PROD:me.varsInitComponent.CO_PROD,
                        },
                        {
                            xtype:'numberfield',
                            fieldLabel:'Año',
                            name:'CO_ANO'
                        },
                        {
                            xtype:'combobox',
                            fieldLabel:'Periodo',
                            store:storePeriodo,
                            displayField:'NO_PERI',
                            valueField:'CO_PERI',
                            editable:false,
                            name:'CO_PERI'
                        },
                    ],
                    buttons:[
                        {
                            text:'Generar',
                            handler:function(){
                                var form=this.up('form').getForm();
                                var values=form.getFieldValues();  
                                values[varSendPackage]=package;  
                                Ext.Ajax.request({
                                    url:settingsLocal.AJAX_URI+'REPO/cierre/correpondencia/',
                                    params: values,
                                    success: function(response){
                                        var json=fnc.responseExtAjaxRequestMsjError(response);
                                        if (json.success){
                                            var tk=json.data;
                                            fnc.openURL("./ajax/REPO/cierre/correpondencia/token?tk="+tk);                                            
                                        }
                                    }
                                });                                
                            }
                        }
                    ]
                }, 
                {
                    xtype:'form',
                    title:'Reporte Maestro de cargas',//speed
                    collapsed: true,
                    collapsible:true,
                    items:[
                        {
                            xtype:'Desktop.Facturacion.Widgets.comboBoxServiciosReportes',
                            packageParent:{package:package,varSend:varSendPackage},               
                            CO_PROD:me.varsInitComponent.CO_PROD,
                        },
                        {
                            xtype:'numberfield',
                            fieldLabel:'Año',
                            name:'CO_ANO'
                        },
                        {
                            xtype:'combobox',
                            fieldLabel:'Periodo',
                            store:storePeriodo,
                            displayField:'NO_PERI',
                            valueField:'CO_PERI',
                            editable:false,
                            name:'CO_PERI'
                        },
                    ],
                    buttons:[
                        {
                            text:'Generar',
                            handler:function(){
                                var form=this.up('form').getForm();
                                var values=form.getFieldValues();
                                values[varSendPackage]=package;                                
                                Ext.Ajax.request({
                                    url:settingsLocal.AJAX_URI+'REPO/cierre/cargas/',
                                    params: values,
                                    success: function(response){
                                        var json=fnc.responseExtAjaxRequestMsjError(response);
                                        if (json.success){
                                            var tk=json.data;
                                            fnc.openURL("./ajax/REPO/cierre/cargas/token?tk="+tk);                                            
                                        }
                                    }
                                });                                
                            }
                        }
                    ]
                },   
                {
                    xtype:'form',
                    title:'Reporte Notas de Crédito',
                    collapsed: true,
                    collapsible:true,
                    items:[
                        {
                            xtype:'Desktop.Facturacion.Widgets.comboBoxServiciosReportes',
                            packageParent:{package:package,varSend:varSendPackage},               
                            CO_PROD:me.varsInitComponent.CO_PROD,
                        },
                        {
                            xtype:'numberfield',
                            fieldLabel:'Año',
                            name:'CO_ANO'
                        },
                        {
                            xtype:'combobox',
                            fieldLabel:'Periodo',
                            store:storePeriodo,
                            displayField:'NO_PERI',
                            valueField:'CO_PERI',
                            editable:false,
                            name:'CO_PERI'
                        },
                    ],
                    buttons:[
                        {
                            text:'Generar',
                            handler:function(){
                                var form=this.up('form').getForm();
                                var values=form.getFieldValues();
                                values[varSendPackage]=package;                                
                                Ext.Ajax.request({
                                    url:settingsLocal.AJAX_URI+'REPO/cierre/reporteNotasCredito/',
                                    params: values,
                                    success: function(response){
                                        var json=fnc.responseExtAjaxRequestMsjError(response);
                                        if (json.success){
                                            var tk=json.data;
                                            fnc.openURL("./ajax/REPO/cierre/reporteNotasCredito/token?tk="+tk);                                            
                                        }
                                    }
                                });                                
                            }
                        }
                    ]
                },
                {
                    xtype:'form',
                    title:'Reporte de Datos de Clientes',
                    collapsed: true,
                    collapsible:true,
                    items:[
                        {
                            xtype:'Desktop.Facturacion.Widgets.comboBoxServiciosReportes',
                            packageParent:{package:package,varSend:varSendPackage},               
                            CO_PROD:me.varsInitComponent.CO_PROD,
                        }
                    ],
                    buttons:[
                        {
                            text:'Generar',
                            handler:function(){
                                var form=this.up('form').getForm();
                                var values=form.getFieldValues();
                                values[varSendPackage]=package; 
                                Ext.Ajax.request({
                                    url:settingsLocal.AJAX_URI+'REPO/cierre/reporteDatosClientes/',
                                    params: values,
                                    success: function(response){
                                        var json=fnc.responseExtAjaxRequestMsjError(response);
                                        if (json.success){
                                            var tk=json.data;
                                            fnc.openURL("./ajax/REPO/cierre/reporteDatosClientes/token?tk="+tk);                                            
                                        }
                                    }
                                });                                
                            }
                        }
                    ]
                },
                {
                    xtype:'form',
                    title:'Reporte General',
                    collapsed: true,
                    collapsible:true,
                    items:[
                        {
                            xtype:'Desktop.Facturacion.Widgets.comboBoxServiciosReportes',
                            packageParent:{package:package,varSend:varSendPackage},               
                            CO_PROD:me.varsInitComponent.CO_PROD,
                        }
                    ],       
                    buttons:[
                        {
                            text:'Generar',
                            handler:function(){
                                var form=this.up('form').getForm();
                                var values=form.getFieldValues();
                                values[varSendPackage]=package;                                
                                Ext.Ajax.request({
                                    url:settingsLocal.AJAX_URI+'REPO/cierre/general/',
                                    params: values,
                                    success: function(response){
                                        var json=fnc.responseExtAjaxRequestMsjError(response);
                                        if (json.success){
                                            var tk=json.data;
                                            fnc.openURL("./ajax/REPO/cierre/general/token?tk="+tk);                                            
                                        }
                                    }
                                });                                
                            }
                        }
                    ]
                },
                {
                    xtype:'form',
                    title:'Reporte de Gerentes SUNAT',
                    collapsed: true,
                    height:250,
                    collapsible:true,
                    items:[
                        {
                            id : 'combo',    
                            xtype:'combobox',                
                            fieldLabel:'Producto',
                            name:'COD_PROD_PADRE',
                            displayField:'DESC_PROD_PADRE',
                            valueField:'COD_PROD_PADRE',
                            store:storeProd_Padre,
                            allowBlank:false,
                            editable:false,     
                        },
                        {
                            id : 'comboTipoDocumento',    
                            xtype:'combobox',                
                            fieldLabel:'Tipo documento',
                            name:'COD_TIPO_DOC',
                            displayField:'DESC_TIPO_DOCUMENTO',
                            valueField:'COD_TIPO_DOC',
                            store:storeCboTipoDocumento,
                            allowBlank:false,
                            editable:false,   
                        },
                        {
                            xtype:'numberfield',
                            fieldLabel:'Año',
                            name:'CO_ANO'
                        },
                        {
                            xtype:'combobox',
                            fieldLabel:'Periodo',
                            store:storePeriodo,
                            displayField:'NO_PERI',
                            valueField:'CO_PERI',
                            editable:false,
                            name:'CO_PERI'
                        },                        
                        
                    ],
                    buttons:[
                        {
                            text:'Generar',
                            handler:function(){
                                var form=this.up('form').getForm();
                                var values=form.getFieldValues();
                                values[varSendPackage]=package; 
                                Ext.Ajax.request({
                                    url:settingsLocal.AJAX_URI+'REPO/cierre/gerenteSunat/',
                                    params: values,
                                    success: function(response){
                                        var json=fnc.responseExtAjaxRequestMsjError(response);
                                        if (json.success){
                                            var tk=json.data;
                                            fnc.openURL("./ajax/REPO/cierre/gerenteSunat/token?tk="+tk);                                            
                                        }
                                    }
                                });                                
                            }
                        }
                    ]
                },                
            //]
             ];
             me.callParent();
             },   
        });                  

       });
    });
});