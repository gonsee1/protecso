Ext.onReady(function(){
    fnc.load.ModelsAndStores(["TIPO_FACT","MONE_FACT","PERI_FACT","PROD","CIER"],function(){
        var package="Desktop.Facturacion.Panels.CIER.Lanzar";
        var varSendPackage=settings.Permissions.varSendPackage;
        var settingsLocal=window.settings.real.get();
        var storeRef=new Desktop.Facturacion.Stores.PROD();        
        fnc.addParameterStore(storeRef,varSendPackage,package);
        fnc.setApiProxyStore(storeRef,{read:'selectStateCierre/'});
        fnc.addParameterStore(storeRef,"CO_PROD",-1);
        var storeProd_Padre;
        var valorProducto=0;
        storeRef.load({
            callback:function(){
                var tiempoMonitoreo=5000;
                var monitoreoCierre=function(inicio){
                    storeRef.load({callback:function(){
                        var items=storeRef.data.items;
                        if (inicio){
                            var monitorear=false;
                            monitoreoCierre.monitorear={};
                            for(var i=0;i<items.length;i++){
                                var item=items[i].data;
                                var state=item.VerStateCierre.toLowerCase();
                                if (state=='en cierre'){
                                    monitoreoCierre.monitorear[item.CO_PROD]=null;
                                    monitorear=true;
                                }
                            }                            
                            if (monitorear) setTimeout(monitoreoCierre,tiempoMonitoreo);
                        }else{
                            var monitorear=false;
                            for(var i=0;i<items.length&&!monitorear;i++){
                                var item=items[i].data;
                                if (typeof monitoreoCierre.monitorear[item.CO_PROD]!='undefined'){                                    
                                    var state=item.VerStateCierre.toLowerCase();
                                    if (state=='termino cierre'){
                                        fnc.createMsgBoxSuccess({msg:'Termino el cierre de '+item.NO_PROD});
                                        delete monitoreoCierre.monitorear[item.CO_PROD];
                                    }else{
                                        monitorear=true;
                                    }
                                }
                            }
                            if (monitorear) setTimeout(monitoreoCierre,tiempoMonitoreo);
                        }                        
                    }});  
                  storeProd_Padre= new Desktop.Facturacion.Stores.PROD_PADRE();  
                  fnc.addParameterStore(storeProd_Padre,varSendPackage,package); 
                };
                monitoreoCierre(true);
              
                Ext.define(package,{                                                                           
                    extend:'Ext.grid.Panel',
                    alias:'widget.Desktop.Facturacion.Panels.CIER.Lanzar',
                    bodyPadding: 5,
                    width: 1300,                    
                        style:{
                            fontSize:'8px',
                        },       
                    store:storeRef,
                    columns:[
                       /*{text:'Codigo',dataIndex:'CO_PROD',width:80},*/
                       {text:'Nombre',dataIndex:'NO_PROD',width:220},
                       {text:'Cierre',width:100,renderer:function(a,b,c){var cier=c.data.VerCierrePendiente;return "%02d".format(cier.NU_PERI)+" - "+cier.NU_ANO;}},                                              
                       {text:'Raiz Recibo',width:100,renderer:function(a,b,c){return c.data.VerCierrePendiente.DE_RAIZ_RECI;}},
                       {text:'Raiz Factura',width:100,renderer:function(a,b,c){return c.data.VerCierrePendiente.DE_RAIZ_FACT;}},
                       {text:'Raiz Boleta',width:100,renderer:function(a,b,c){return c.data.VerCierrePendiente.de_RAIZ_BOLE;}},
                       {text:'Emisión',width:100,renderer:function(a,b,c){return c.data.VerCierrePendiente.FE_EMIS;}},
                       {text:'Vencimiento',width:100,renderer:function(a,b,c){return c.data.VerCierrePendiente.FE_VENC;}},
                       {text:'Estado',dataIndex:'VerStateCierre',width:120},
                       {text:'Recibos',width:80,renderer:function(a,b,c){return c.data.CantidadRecibos;}},
                       {text:'Facturas',width:80,renderer:function(a,b,c){return c.data.CantidadFacturas;}},
                       {text:'Boletas',width:80,renderer:function(a,b,c){return c.data.CantidadBoletas;}},
                       {text:'Duración(Min.)',width:110,renderer:function(a,b,c){return fnc.number.redondear(fnc.parse.String.toInt(c.data.VerCierrePendiente.StringDuracion)/60,2);}},
                       
                        ],                        
                        tbar: [                            
                            {
                                id : 'comboCierre',    
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
                                xtype: 'button',
                                text: "Buscar",
                                tooltip: "Buscar",
                                iconCls: fnc.icons.class.explorar(),
                                listeners:{
                                    click:function(){
                                                                                                                       
                                    var str = Ext.getCmp('comboCierre').getValue(); // document.getElementById('combo').value;.                                       
                                                                                                                                                                                     
                                    valorProducto = str;
                                    fnc.addParameterStore(storeRef,"CO_PROD",str);
                                        storeRef.load({callback:function(){
                                               
                                        }});                                                                                                                      
                                    }
                                }
                            },
                            {
                                    text:'Actualizar',
                                    iconCls: fnc.icons.class.actualizar(),
                                    handler: function() {
                                        storeRef.load();
                                    }	
                            },
                            {
                                text: 'Ejecutar',
                                iconCls: fnc.icons.class.lanzar(),
                                handler: function() {
                                    
                                  if(valorProducto != 0){
                                    
                                    var grid=this.up("grid");
                                    var store=grid.getStore();
                                    //var selected=grid.getView().getSelectionModel().getSelection();
                                    //if (selected.length==1){
                                    //var registro=selected[0].data.VerCierrePendiente;
                                    var registro=(function(items){//busca el mayor de todos como inicio de raiz
                                        var menor=Number.MIN_VALUE;
//                                        alert("menor :"+menor);
                                        var pos=-1;
                                        for(var i=0;i<items.length;i++){
                                            var cierrePendiente=items[i].data.VerCierrePendiente;
                                            if(cierrePendiente.DE_RAIZ_RECI>menor){
                                                menor=cierrePendiente.DE_RAIZ_RECI;
                                                pos=i;
                                            }
                                        }
                                        if (pos!=-1){   
                                            return items[pos];
                                        }
                                        return null;
                                    })(store.data.items);
                                    var registroCierre=registro.data.VerCierrePendiente;
                                    var win=fnc.createWindowModal({
                                        title:"Datos de Cierre",
                                        height:300,
                                        width:300,
                                        items:[
                                            Ext.create("Ext.form.Panel",{
                                                items:[
                                                    {
                                                        id: 'semillaBoleta',
                                                        xtype:'numberfield',
                                                        fieldLabel:'Semilla Boleta (Raiz)',
                                                        name:'DE_RAIZ_BOLE',
                                                        value:registroCierre.de_RAIZ_BOLE
                                                    },
                                                    {
                                                        id: 'semillaRecibo',
                                                        xtype:'numberfield',
                                                        fieldLabel:'Semilla Recibo (Raiz)',
                                                        name:'DE_RAIZ_RECI',
                                                        value:registroCierre.DE_RAIZ_RECI
                                                    },
                                                    {
                                                        xtype:'numberfield',
                                                        fieldLabel:'Semilla Factura (Raiz)',
                                                        name:'DE_RAIZ_FACT',
                                                        value:registroCierre.DE_RAIZ_FACT,
                                                        allowBlank: false,
                                                    },
                                                    {
                                                        xtype:'numberfield',
                                                        fieldLabel:'Tipo de cambio',
                                                        name:'NU_TIPO_CAMB',
                                                        value:registroCierre.NU_TIPO_CAMB,
                                                        allowBlank: false,
                                                    },
                                                    {
                                                        xtype:'datefield',
                                                        fieldLabel: 'Fecha Emisión',
                                                        value: new Date(),
                                                        name:'FE_EMIS',
                                                        allowBlank: false,
                                                    },
                                                    {
                                                        xtype:'datefield',
                                                        fieldLabel: 'Fecha Vencimiento',
                                                        value: new Date(),
                                                        name:'FE_VENC',
                                                        allowBlank: false,
                                                    },
                                                ],
                                                buttons:[
                                                    {
                                                        text:'Iniciar Cierre',
                                                        formBind: true, //only enabled once the form is valid
                                                        handler:function(){  
                                                                                                                                                                                   
                                                                var values=this.up('form').getForm().getFieldValues();
                                                                var data=registro.data;
                                                                data.DE_RAIZ_RECI=values.DE_RAIZ_RECI;
                                                                data.DE_RAIZ_FACT=values.DE_RAIZ_FACT;
                                                                data.DE_RAIZ_BOLE=values.DE_RAIZ_BOLE;
                                                                data.FE_EMIS=values.FE_EMIS;
                                                                data.FE_VENC=values.FE_VENC;
                                                                data.NU_TIPO_CAMB=values.NU_TIPO_CAMB;
                                                                data.CO_PRO=valorProducto;
                                                                var m=new Desktop.Facturacion.Models.CIER(data);
                                                                data=m.data;
                                                                fnc.createMsgConfirm({msg:'Esta seguro de lanzar el cierre de todos los productos.',call:function(a){
                                                                    if (a=='yes'){
                                                                        win.disable();
                                                                        data[varSendPackage]=package;
                                                                        Ext.Ajax.request({
                                                                            url:settingsLocal.AJAX_URI+'CIER/lanzar/',
                                                                            params: data,
                                                                            success: function(response){
                                                                                win.enable();
                                                                                var text = response.responseText;
                                                                                var json=Ext.JSON.decode(text);
                                                                                if (json.success){                                                                                    
                                                                                    fnc.createMsgBoxSuccess({msg:'Se ha lanzado el cierre de todos los productos'});
                                                                                    win.close();
                                                                                    monitoreoCierre(true);
                                                                                }else{
                                                                                    fnc.createMsgBoxError({msg:json.msg});
                                                                                }                                                                            
                                                                            }
                                                                        })
                                                                    }
                                                                }})                                                                                                                    
                                                        }
                                                    }
                                                ]
                                            })
                                        ]
                                    });
                                             
                                    var str = Ext.getCmp('semillaRecibo');
                                    str.setVisible(!(valorProducto == 4));
                                    str.allowBlank = (valorProducto == 4);
                                    
//                                    var stb = Ext.getCmp('semillaBoleta');
//                                    stb.setVisible(!(valorProducto == 4));
//                                    stb.allowBlank = (valorProducto == 4);
                                  
                                    
                                    win.show();
                                    //}
                                  }else{
                                    fnc.createMsgBoxSuccess({msg:"Seleccione un producto"});  
                                  } 
                                }
                            },                        
                            {
                                text:'Cerrar',
                                iconCls: fnc.icons.class.actualizar(),
                                handler: function() {
                                                                                                                                                
                                    if(valorProducto != 0){
                                        //fnc.createMsgConfirm({msg:'Esta seguro de cerrar este periodo de todos los productos.',call:function(a){
                                        fnc.createMsgConfirm({msg:'Esta seguro de cerrar este periodo de todos los productos.',call:function(a){
                                            if (a=='yes'){
                                                var data={};
                                                data.CO_PRO=valorProducto;
                                                data[varSendPackage]=package;
                                                Ext.Ajax.request({
                                                    url:settingsLocal.AJAX_URI+'CIER/cerrar/',
                                                    params:data,
                                                    method:'POST',                                                
                                                    success: function(response){
                                                        var text = response.responseText;
                                                        var json=Ext.JSON.decode(text);
                                                        if (json.success)
                                                            fnc.createMsgBoxSuccess({msg:json.msg});                                                      
                                                        else
                                                            fnc.createMsgBoxError({msg:json.msg});                                                      
                                                        storeRef.load();
                                                    }
                                                })
                                            }
                                        }})
                                    }else{
                                        fnc.createMsgBoxSuccess({msg:"Seleccione un producto"});                                                                                              
                                    }                                
                                }	
                            }
                        ]       
                });    
                                             
            }
        });
  
    });
});