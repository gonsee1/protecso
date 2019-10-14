Ext.onReady(function(){
    fnc.load.ModelsAndStores(["TIPO_FACT","MONE_FACT","PERI_FACT","PROD","CORT"],function(){
        var package="Desktop.Facturacion.Panels.CORT.Listar";
        var storeProd=new Desktop.Facturacion.Stores.PROD();
        var storeCort=new Desktop.Facturacion.Stores.CORT();
        var varSendPackage=settings.Permissions.varSendPackage;
        
        var storeProd_Padre= new Desktop.Facturacion.Stores.PROD_PADRE();       
        
        fnc.addParameterStore(storeProd,varSendPackage,package);
        fnc.addParameterStore(storeCort,varSendPackage,package);
        fnc.setApiProxyStore(storeCort,{read:'selectCortesPendientes/'});
        
        fnc.addParameterStore(storeProd_Padre,varSendPackage,package); 
        
        Ext.define(package,{
            //model:'Desktop.Facturacion.Models.CLIE',
            extend:'Ext.form.Panel',
            alias:'widget.Desktop.Facturacion.Panels.CORT.Listar',
            bodyPadding: 5,
            width: '100%',
            layout: 'anchor',
            defaults: {
                anchor: '100%'
            },            
//            tbar:[
//                {
//                    text:'Actualizar',
//                    iconCls: fnc.icons.class.actualizar(),
//                    handler:function(){
//                        storeCort.load();
//                    }
//                }
//            ],            
            items: [
                {
                    xtype: 'gridpanel',                    
                    store:storeCort,
                    columns:[
                        {dataIndex:'CO_CORT',text:'Codigo'},
                        {dataIndex:'CO_NEGO',text:'Negocio'},
                        {dataIndex:'FE_INIC',text:'Fecha Inicio', xtype: 'datecolumn', format: 'd/m/Y'},
                        {dataIndex:'FE_FIN',text:'Fecha Fin', xtype: 'datecolumn',format: 'd/m/Y'},
                        {dataIndex:'DE_ESTADO',text:'Estado'},
                        {dataIndex:'COD_PROD_PADRE',text:'Producto',hidden: true}                      
                    ],
                    tbar:[
                        {
                            id : 'comboCortesListar',    
                            xtype:'combobox',                
                            fieldLabel:'Producto',
                            name:'COD_PROD_PADRE',
                            displayField:'DESC_PROD_PADRE',
                            valueField:'COD_PROD_PADRE',
                            store:storeProd_Padre,
                            allowBlank:false,
                            editable:false,     
                        }, "-", ,
                        {
                            xtype: 'button',
                            text: "Buscar",
                            tooltip: "Buscar",
                            iconCls: fnc.icons.class.explorar(),
                                listeners:{
                                    click:function(){
                                        var str = Ext.getCmp('comboCortesListar').getValue(); 
                                        storeCort.reload();
                                        storeCort.filter('COD_PROD_PADRE',str);                                                                                        
                                    }
                                }
                        }, "-",
                        {
                            text:'Exportar',
                            handler:function(){
                                var grid=this.up('gridpanel');
                                bbb=grid;
                                fnc.export.gridToExcel(grid);
                            }
                        },
                        {
                            text:'Actualizar',
                            iconCls: fnc.icons.class.actualizar(),
                            handler:function(){
                                storeCort.load();
                            }
                        }
                    ] 
                }
            ],        
        });                  
  
    });
});