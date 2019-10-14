Ext.onReady(function(){
    fnc.load.ModelsAndStores(["PROD","SUSP"],function(){
        var package="Desktop.Facturacion.Panels.SUSP.Listar";
        var storeSusp=new Desktop.Facturacion.Stores.SUSP();
        var varSendPackage=settings.Permissions.varSendPackage;
        
        var storeProd_Padre= new Desktop.Facturacion.Stores.PROD_PADRE();     
                
        fnc.addParameterStore(storeSusp,varSendPackage,package);
        fnc.setApiProxyStore(storeSusp,{read:'selectSuspensionesPendientes/'});
        
        fnc.addParameterStore(storeProd_Padre,varSendPackage,package); 
        
        Ext.define(package,{
            //model:'Desktop.Facturacion.Models.CLIE',
            extend:'Ext.form.Panel',
            alias:'widget.Desktop.Facturacion.Panels.SUSP.Listar',
            bodyPadding: 5,
            width: '100%',
            layout: 'anchor',
            defaults: {
                anchor: '100%'
            },
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
                                        storeSusp.reload();
                                        storeSusp.filter('COD_PROD_PADRE',str);                                                                                        
                                    }
                                }
                }, "-",
                {
                    text:'Actualizar',
                    iconCls: fnc.icons.class.actualizar(),
                    handler:function(){
                        storeSusp.load();
                    }
                }
            ],
            items: [
                {
                    xtype: 'gridpanel',
                    store:storeSusp,
                    columns:[
                        {dataIndex:'CO_SUSP',text:'Codigo'},
                        {text:'Negocio',renderer:function(a,b,c){return c.data.NEGO_SUCU.CO_NEGO;}},
                        {text:'OIT',renderer:function(a,b,c){return (c.data.ST_SOAR?"O":"S")+c.data.CO_OIT_INST;}},                        
                        {dataIndex:'FE_INIC',text:'Fecha Inicio', xtype: 'datecolumn', format: 'd/m/Y'},
                        {dataIndex:'FE_FIN',text:'Fecha Fin', xtype: 'datecolumn',format: 'd/m/Y'},
                        {dataIndex:'DE_ESTADO',text:'Estado'},
                        {dataIndex:'COD_PROD_PADRE',text:'Producto',hidden: true}                        
                    ]
                }
            ],        
        });                  
  
    });
});