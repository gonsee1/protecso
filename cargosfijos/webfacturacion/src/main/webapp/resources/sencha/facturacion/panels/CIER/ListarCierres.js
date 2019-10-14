Ext.onReady(function(){
    fnc.load.ModelsAndStores(["PROD","CIER"],function(){
        var package="Desktop.Facturacion.Panels.CIER.ListarCierres";
        var varSendPackage=settings.Permissions.varSendPackage;
        var storeCier=new Desktop.Facturacion.Stores.CIER();
        fnc.addParameterStore(storeCier,varSendPackage,package);
        fnc.setApiProxyStore(storeCier,{read:'selectCierresCerrados/'}); 
        
        storeCier.load({
            callback:function(){
                Ext.define(package,{
                    extend:'Ext.grid.Panel',
                    alias:'widget.Desktop.Facturacion.Panels.CIER.ListarCierres',
//                    bodyPadding: 5,
//                    width: 1100,
//                    style:{
//                        fontSize:'8px',
//                    },
                    columns: [
                       {text:'Nombre',width:300,renderer:function(a,b,c){return c.data.PROD.NO_PROD;}},
                       {text:'Cierre',width:100,renderer:function(a,b,c){var cier=c.data;return "%02d".format(cier.NU_PERI)+" - "+cier.NU_ANO;}},                                              
                       {text:'Raiz Recibo',width:100,renderer:function(a,b,c){return c.data.DE_RAIZ_RECI;}},
                       {text:'Raiz Factura',width:100,renderer:function(a,b,c){return c.data.DE_RAIZ_FACT;}},
                       {text:'Emisión',width:100,renderer:function(a,b,c){
                               var ano=c.data.FE_EMIS.getFullYear();
                               var mes=(c.data.FE_EMIS.getMonth() + 1);
                               var dia=c.data.FE_EMIS.getDate();
                               if(mes<10){
                                   mes='0'+mes;
                               }
                               if(dia<10){
                                   dia='0'+dia;
                               }
                               return ano+"-"+mes+"-"+dia;
                           }},
                       {text:'Vencimiento',width:100,renderer:function(a,b,c){
                               var ano=c.data.FE_VENC.getFullYear();
                               var mes=(c.data.FE_VENC.getMonth() + 1);
                               var dia=c.data.FE_VENC.getDate();
                               if(mes<10){
                                   mes='0'+mes;
                               }
                               if(dia<10){
                                   dia='0'+dia;
                               }
                               return ano+"-"+mes+"-"+dia;
                           }},
                       {text:'Tipo Cambio',width:100,renderer:function(a,b,c){return c.data.NU_TIPO_CAMB;}},                       
                    ],
                    store:storeCier,
                    tbar:[
                        {
                            xtype: 'button',
                            text: "Actualizar",
                            tooltip: "Actualizar",
                            iconCls: fnc.icons.class.actualizar(),
                            handler: function() {
                                storeCier.load();
                            }	
                        }
                    ]
                });                   
            }
        });
               
    });
 
});