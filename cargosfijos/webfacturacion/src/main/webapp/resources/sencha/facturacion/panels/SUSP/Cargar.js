Ext.onReady(function(){
    fnc.load.ModelsAndStores(["PROD"],function(){
        var package="Desktop.Facturacion.Panels.SUSP.Cargar";
        var storeProd=new Desktop.Facturacion.Stores.PROD();
        var varSendPackage=settings.Permissions.varSendPackage;
        fnc.addParameterStore(storeProd,varSendPackage,package);
        
        var storeProd_Padre= new Desktop.Facturacion.Stores.PROD_PADRE();       
        fnc.addParameterStore(storeProd_Padre,varSendPackage,package); 
        
        Ext.define(package,{
            //model:'Desktop.Facturacion.Models.CLIE',
            extend:'Ext.form.Panel',
            alias:'widget.Desktop.Facturacion.Panels.SUSP.Cargar',
            bodyPadding: 5,
            width: '100%',
            layout: 'anchor',
            defaults: {
                anchor: '100%'
            },
            defaultType: 'textfield',
            items: [
                 {
                    id:'comboSuspensiones', 
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
                    name: 'FL_SUSPESIONES',
                    fieldLabel: 'Archivo de Suspensiones',
                    labelWidth: 120,
                    msgTarget: 'side',
                    allowBlank: false,
                    anchor: '100%',
                    buttonText: '::'
                }
            ],

            // Reset and Submit buttons
            buttons: [{
                text: 'Borrar',
                handler: function() {
                    this.up('form').getForm().reset();
                }
            }, {
                text: 'Agregar',
                formBind: true, //only enabled once the form is valid
                disabled: true,
                handler: function() {
                    var form = this.up('form').getForm();
                    if (form.isValid()) {
                        form.submit({
                            url: 'ajax/SUSP/upload/',
                            waitMsg: 'Cargando archivo de suspensiones',
                            success: function(fp, o) {      
                                var data=[];
                                for(var i in o.result.data){
                                    data.push({resultado:o.result.data[i]});
                                }
                                var w=fnc.createWindowModal(Desktop.Facturacion.getDesktop(),{
                                    width:700,
                                    renderTo:Ext.getBody(),
                                    title:'Resultado de carga',
                                    items:[
                                        {
                                            xtype:'grid',
                                            store:new Ext.data.Store({
                                                fields:['resultado'],
                                                data:data
                                            }),
                                            columns:[
                                                {text:'Resultados',dataIndex:'resultado',width:650}
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
                                w.show();
                            },
                            failure:function(form, action){
                                if (action && action.result){
                                    fnc.createMsgBoxError({msg:action.result.msg});
                                }
                            }
                        });
                    }
                }
            }]         
        });                  
  
    });
});