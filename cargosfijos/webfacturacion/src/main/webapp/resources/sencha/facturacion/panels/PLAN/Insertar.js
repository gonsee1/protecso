Ext.onReady(function(){
    fnc.load.ModelsAndStores(["TIPO_FACT","MONE_FACT","PERI_FACT","PROD","PLAN_MEDI_INST","MONE_FACT","PLAN"],function(){
     fnc.load.scripts(["widgets/comboBoxPlanes.js"],function(){            
        var package="Desktop.Facturacion.Panels.PLAN.Insertar";
        var varSendPackage=settings.Permissions.varSendPackage;
        var storeProd=new Desktop.Facturacion.Stores.PROD();
        var storeMoneFact=new Desktop.Facturacion.Stores.MONE_FACT();
        fnc.addParameterStore(storeProd,varSendPackage,package); 
        var storeMediInst=new Desktop.Facturacion.Stores.PLAN_MEDI_INST();
        fnc.addParameterStore(storeMediInst,varSendPackage,package); 
        fnc.addParameterStore(storeMoneFact,varSendPackage,package);
        
        var storeProd_Padre= new Desktop.Facturacion.Stores.PROD_PADRE();       
        fnc.addParameterStore(storeProd_Padre,varSendPackage,package); 
        
        Ext.define(package,{
            model:'Desktop.Facturacion.Models.PLAN',
            extend:'Ext.form.Panel',
            alias:'widget.Desktop.Facturacion.Panels.PLAN.Insertar',
            bodyPadding: 5,
            width: 350,
            layout: 'anchor',
            defaults: {
                anchor: '100%'
            },            
            initComponent:function(){
                    var obj=this;
                    obj.varsInitComponent=obj.varsInitComponent || {};                    
                      for(var k in this.stores){
                        fnc.addParameterStore(obj.stores[k],varSendPackage,package);
                        obj.stores[k].load();
                    }
            obj.items = [ 
            /*{
                fieldLabel: 'Nombre',
                name: 'NO_PLAN',
                allowBlank: false
            },*/
            {
            	xtype:'combobox',
            	fieldLabel:'Moneda',
            	name:'CO_MONE_FACT',
            	displayField:'NO_MONE_FACT',
            	valueField:'CO_MONE_FACT',
            	store:storeMoneFact,
            	allowBlank:false,
                editable:false,                
            },{
                xtype:'numberfield',
                fieldLabel: 'Monto',
                allowExponential: false,
                minValue: 0,
                name: 'IM_MONTO',
                allowBlank: false             
            },{
                xtype:'Desktop.Facturacion.Widgets.comboBoxPlanes',
                packageParent:{package:package,varSend:varSendPackage},
                CO_PROD:obj.varsInitComponent.CO_PROD,
                CO_PLAN_MEDI_INST:obj.varsInitComponent.CO_PLAN_MEDI_INST,
                DESC_PROD_PADRE:obj.varsInitComponent.DESC_PROD_PADRE,
                NO_PROD:obj.varsInitComponent.NO_PROD,
                DE_VELO_SUBI:obj.varsInitComponent.DE_VELO_SUBI,
                DE_VELO_BAJA:obj.varsInitComponent.DE_VELO_BAJA,
                NO_PLAN:obj.varsInitComponent.NO_PLAN,
                RELACIONADO_TI:obj.varsInitComponent.RELACIONADO_TI,
            }                      
            /*
            {
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
            	xtype:'combobox',
            	fieldLabel:'Servicio',
            	name:'CO_PROD',
            	displayField:'NO_PROD',
            	valueField:'CO_PROD',
            	store:storeProd,
            	allowBlank:false,
                editable:false,
            },*/
            
            /*     
            {
                fieldLabel: 'Velocidad Subida',
                name: 'DE_VELO_SUBI',
                xtype: 'numberfield', 
                allowExponential: false,
                minValue: 0,
                allowBlank: false                
            },
            {
                fieldLabel: 'Velocidad Bajada',
                name: 'DE_VELO_BAJA',
                xtype: 'numberfield', 
                allowExponential: false,
                minValue: 0,
                allowBlank: false              
            },*/
            /*{
            	xtype:'combobox',
            	fieldLabel:'Medio Instalación',
            	name:'CO_PLAN_MEDI_INST',
            	displayField:'NO_PLAN_MEDI_INST',
            	valueField:'CO_PLAN_MEDI_INST',
            	store:storeMediInst,
            	allowBlank:false, 
                editable:false,
            },*/
            /*{
                xtype:'checkbox',
                fieldLabel:'Aplicar arrendamiento',
                name:'ST_BACKUP_BU'
            }*/
            ];
              obj.callParent(); 
            },	
            // Reset and Submit buttons
            defaultType: 'textfield',
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
                        var model=new Desktop.Facturacion.Models.PLAN(form.getFieldValues());
                        model.save({
                            params:fnc.getParamsSendPackage(model.getData(),varSendPackage,package),
                            success: function (record, operation) {
                                fnc.createMsgBoxSuccess({msg:'Se registro con éxito el plan.'});
                                form.reset();
                            },
                            failure: function (record, operation) {
                                fnc.responseEvaluateOperationError(operation);
                            }
                        });
                    }
                }
            }]         
        });
      }); 
    });    
});

