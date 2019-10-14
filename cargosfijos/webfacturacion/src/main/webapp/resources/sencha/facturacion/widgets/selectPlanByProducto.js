(function(){
    var package='Desktop.Facturacion.Widgets.selectPlanByProducto';
    
    Ext.require([
        'Ext.grid.*',
        'Ext.data.*',
        'Ext.ux.grid.FiltersFeature',
        'Ext.toolbar.Paging',
        'Ext.ux.ajax.JsonSimlet',
        'Ext.ux.ajax.SimManager'
    ]);    
    
    Ext.define(package,{
        extend:'Ext.form.FieldContainer',
        alias:'widget.'+package,
        initComponent:function(){
            var obj=this;
            obj.width=obj.width || "400px";
            obj.items=[
                {
                    xtype:'textfield',
                    name:'CO_PLAN',
                    allowBlank: false,
                    hidden:true,
                },{
                    xtype:'fieldset',
                    title: 'Seleccione Plan',
                    //collapsed: true,
                    allowBlank: false,
                    layout:'hbox',
                    //width:obj.width,
                    items:[
                        {
                            xtype:'textarea',
                            fieldLabel: 'PLAN',
                            name:'DE_PLAN',
                            width:obj.width,
                            readOnly:true,
                            allowBlank: false,
                            style:{
                                marginBottom:'5px',
                            }
                        },
                        {
                            xtype:'button',
                            text:'...',
                            name:'btnSeleccionarPlan',
                            style:{
                                marginLeft:'5px',
                            },
                            listeners:{
                                click:function(){
                                    if(typeof obj.CO_PROD != 'undefined' && typeof obj.CO_MONE_FACT != 'undefined'){
                                        var storePlan=new Desktop.Facturacion.Stores.PLAN();
                                        fnc.setApiProxyStore(storePlan,{read:'selectByPRODAndMONE/'})
                                        fnc.addParameterStore(storePlan,obj.packageParent.varSend,obj.packageParent.package);
                                        fnc.addParameterStore(storePlan,"CO_PROD",obj.CO_PROD);
                                        fnc.addParameterStore(storePlan,"CO_MONE_FACT",obj.CO_MONE_FACT);
                                        var panel=Ext.create('Ext.grid.Panel',{
                                           store: storePlan,
                                           columns:[
                                               /*{ text: 'Codigo',  dataIndex: 'CO_PLAN',width:50 },*/
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
                                                        obj.query("[name='CO_PLAN']")[0].setValue(data.CO_PLAN);
                                                        obj.query("[name='DE_PLAN']")[0].setValue(data.NO_PLAN+" - Monto "+data.IM_MONTO);
                                                        window.close();
                                                    }
                                               }
                                               
                                           }
                                       });
                                        var window=fnc.createWindowModal(obj.desktop,{
                                            width:800,
                                            items:[
                                                panel
                                            ],
                                            /*
                                            tbar:[
                                                {
                                                    xtype:'combobox',
                                                    fieldLabel: 'Bajada',
                                                    labelWidth:50,
                                                    width:150,
                                                    editable:false,
                                                },
                                                {
                                                    xtype:'combobox',
                                                    fieldLabel: 'Subida', 
                                                    labelWidth:50,
                                                    width:150,
                                                    editable:false,
                                                },
                                                {
                                                    xtype:'combobox',
                                                    fieldLabel: 'Medio',
                                                    labelWidth:50,
                                                    width:200,
                                                    editable:false,
                                                },                         
                                            ],*/
                                            renderTo:Ext.getBody(),
                                        });
                                        window.show()
                                    }
                                }
                            }
                        }                        
                    ]
                }
            ];
            obj.callParent();           
        },
    }); 

})();