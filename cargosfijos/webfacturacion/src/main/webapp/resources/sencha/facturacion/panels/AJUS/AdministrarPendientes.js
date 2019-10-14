Ext.onReady(function(){
    fnc.load.scripts(["widgets/filterPagination.js"],function(){
        fnc.load.ModelsAndStores(["TIPO_FACT","MONE_FACT","PERI_FACT","NEGO","PROD","CIER","AJUS"],function(){
            var package="Desktop.Facturacion.Panels.AJUS.AdministrarPendientes";
            var AJAX_URI=settings.AJAX_URI;
            var objDesktop=null;
            var storeData=new Desktop.Facturacion.Stores.AJUS();
            var varSendPackage=settings.Permissions.varSendPackage;
            fnc.addParameterStore(storeData,varSendPackage,package); 
            fnc.setApiProxyStore(storeData,{read:'selectPendientes/'});
            
            var storeProd_Padre= new Desktop.Facturacion.Stores.PROD_PADRE();       
            fnc.addParameterStore(storeProd_Padre,varSendPackage,package); 
            
            storeData.load();
            Ext.define(package,{ 
                extend:'Ext.panel.Panel',               
                alias:'widget.Desktop.Facturacion.Panels.AJUS.AdministrarPendientes',
                layout:'fit',
                tbar: [
                    {
                        xtype: 'button',
                        text: "Cargar Ajustes",
                        tooltip: "Cargar Ajustes",
                        iconCls: fnc.icons.class.edit(),
                        listeners:{
                            click:function(btn,e,opts){
                                var win=fnc.createWindowModal({
                                    height:150,
                                    title:'Cargar Ajustes',
                                    items:[
                                        {
                                            xtype:'form',
                                            items:[
                                                {
                                                    id:'comboAjustes', 
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
                                                    xtype: 'filefield',
                                                    name: 'FL_AJUS',
                                                    fieldLabel: 'Archivo de Ajustes',
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
                                                                url: 'ajax/AJUS/upload/',
                                                                waitMsg: 'Cargando ajustes',
                                                                success: function(fp, o) {
                                                                    if (o.result){
                                                                        var data=[];
                                                                        for(var i=0;i<o.result.data.length;i++)
                                                                            data.push({resultado:o.result.data[i]});
                                                                        var winRpta=fnc.createWindowModal({
                                                                            title:'Resultado de carga',
                                                                            items:[
                                                                                {
                                                                                    xtype:'grid',
                                                                                    layout:'fit',
                                                                                    store:new Ext.data.Store({
                                                                                        fields:['resultado'],
                                                                                        data:data,
                                                                                    }),
                                                                                    columns:[
                                                                                        {text:'Resultados',dataIndex:'resultado',width:380}
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
                                                                                }
                                                                            ]                                                                            
                                                                        });
                                                                        storeData.load();
                                                                        winRpta.show();
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
                    { 
                        xtype:'grid',
                        store:storeData,
                        columns: [
                            { text: 'Negocio',  dataIndex: 'CO_NEGO' },
                            { text: 'Glosa', dataIndex: 'DE_GLOS' },
                            { text: 'Monto', dataIndex: 'IM_MONT'},
                            { text: 'Afecto IGV', renderer:function(a,b,c){return c.data.ST_AFEC_IGV?'Si':'No';} },                            
                        ],    
                    }    
                ],
                //fbar: [{xtype:'Desktop.Facturacion.Widgets.filterPagination',store:storeData},'->'],//bien
            });            
        });
    });
});