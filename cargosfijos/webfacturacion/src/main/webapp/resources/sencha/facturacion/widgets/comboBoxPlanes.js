(function(){
    var package='Desktop.Facturacion.Widgets.comboBoxPlanes'; 
    Ext.define(package,{
        extend:'Ext.form.FieldContainer',
        alias:'widget.'+package,
        initComponent:function(){
            var obj=this;  
            obj.allowBlank= (typeof obj.allowBlank =='undefined'?false:obj.allowBlank);
            obj.width=obj.width || "400px";
            obj.width=parseInt(obj.width); 
            //obj.title=obj.title || 'Selección Distrito';
            obj.name=obj.name || "CO_DIST";
            obj.sufijoName=obj.sufijoName || "";
            obj.sufijoNameMedio = obj.sufijoNameMedio || "";
            obj.sufijoNombrePlan = obj.sufijoNombrePlan || "";
            obj.sufijoLabel=obj.sufijoLabel || "";
            
            var producto_descripcion = '';
            
            var combo_planes_mayorista=Ext.create('Ext.form.field.ComboBox',{                
                            fieldLabel:'Descripcion Plan',
                            store:null,
                            //id:'idComboNombreMayorista',
                            displayField: 'NOMBRE_PLAN',
                            valueField: 'COD_REGLAS_NOMBRE_PLAN',
                            name: 'COD_REGLAS_NOMBRE_PLAN'+obj.sufijoNombrePlan,
                            editable: false,
                            
                            //allowBlank: obj.allowBlank, 
                            bodyPadding: 5,
                            width: 340,
                            queryMode: 'local',
            });
                      
            
            var velocidad_subida=Ext.create('Ext.form.field.Number',{
                                fieldLabel: 'Velocidad Subida',
                                name: 'DE_VELO_SUBI',                                
                                allowExponential: false,
                                minValue: 0,
                                bodyPadding: 5,
                                width: 340,
                                allowBlank: false ,
                        });   
                        
            var velocidad_bajada=Ext.create('Ext.form.field.Number',{
                                fieldLabel: 'Velocidad Bajada',
                                name: 'DE_VELO_BAJA',                                
                                allowExponential: false,
                                bodyPadding: 5,
                                width: 340,                
                                minValue: 0,
                                allowBlank: false,  
                        });                                                   
            
            var aplicar_arrendamiento=Ext.create('Ext.form.field.Checkbox',{
                                fieldLabel: 'Aplicar arrendamiento',
                                name: 'ST_BACKUP_BU',                                
                                allowExponential: false,                               
                                allowBlank: false,  
                        });  
            
            var aplicar_arrendamiento_IDAD =Ext.create('Ext.form.field.Checkbox',{
                fieldLabel: 'Aplicar arrendamiento',
                name: 'ST_BACKUP_BU',                                
                allowExponential: false,                               
                allowBlank: false, 
                
                listeners:{
                    change: function(checkbox, newValue, oldValue, eOpts) {
                            
                        if (newValue) {
                              velocidad_subida.setVisible(false);
                              velocidad_bajada.setVisible(false);
                                                                  
                              velocidad_subida.allowBlank = true;
                              velocidad_bajada.allowBlank = true; 
                              
                              velocidad_bajada.setValue("");
                              velocidad_subida.setValue("");
                              aplicar_arrendamiento.setValue(1);
                              
                        } else {
                              velocidad_subida.setVisible(true);
                              velocidad_bajada.setVisible(true);
                                                                              
                              velocidad_subida.allowBlank = false;
                              velocidad_bajada.allowBlank = false;
                              aplicar_arrendamiento.setValue("");
 
                        }
                    }
              }
        }); 
            
            
           
            var relacionado_ti=Ext.create('Ext.form.field.Checkbox',{
                                fieldLabel: 'Relacionado a TI',
                                name: 'RELACIONADO_TI',                                
                                allowExponential: false,                               
                                allowBlank: false,  
                        });   
                        
            var velocidad_ok_mayorista=Ext.create('Ext.form.field.Checkbox',{
                                fieldLabel: 'Aplica Velocidad',
                                name: 'VELOCIDAD_OK_MAYORISTA',                                
                                allowExponential: false,                               
                                allowBlank: false,
                                listeners:{
                                      change: function(checkbox, newValue, oldValue, eOpts) {
                                              
                                          if (newValue) {
                                                velocidad_subida.setVisible(true);
                                                velocidad_bajada.setVisible(true);
                                                                                    
                                                velocidad_subida.allowBlank = false;
                                                velocidad_bajada.allowBlank = false;  
                                          } else {
                                                velocidad_subida.setVisible(false);
                                                velocidad_bajada.setVisible(false);
                                                                                                
                                                velocidad_subida.allowBlank = true;
                                                velocidad_bajada.allowBlank = true;                                            
                                                
                                                velocidad_bajada.setValue("");
                                                velocidad_subida.setValue("");
                                          }
                                      }
                                }
                        });                
                                    
            velocidad_subida.setVisible(true);
            velocidad_bajada.setVisible(true);
            aplicar_arrendamiento.setVisible(true);
            //arrendamiento_IDAD 
            aplicar_arrendamiento_IDAD.setVisible(false);
            
            relacionado_ti.setVisible(true);
            velocidad_ok_mayorista.setVisible(false);
            combo_planes_mayorista.setVisible(false);
            
            obj.items=[
                {
                    //xtype:'fieldset',
                    title: obj.title ,
                    allowBlank: false,
                    layout:'vbox',
                    style:{
                        width:obj.width+'px',
                    },
                    items:[
                        {
                            xtype:'combobox',
                            fieldLabel:'Producto',
                            store:null,
                            bodyPadding: 5,
                            width: 340,                           
                            displayField: 'DESC_PROD_PADRE',
                            valueField: 'COD_PROD_PADRE',
                            name: 'COD_PROD_PADRE'+obj.sufijoName,
                            editable: false,
                            allowBlank: obj.allowBlank, 
                            queryMode: 'local',
                                listeners:{
                                    select:function( combo, record, eOpts ){
                                         var data=record[0].data.DESC_PROD_PADRE;
                                                                
                                          producto_descripcion = data;
                                          
                                          velocidad_ok_mayorista.setVisible(false);
                                          velocidad_ok_mayorista.allowBlank = true;
                                          velocidad_ok_mayorista.setValue(0);
                                          
                                          combo_planes_mayorista.setVisible(false);     
                                          combo_planes_mayorista.allowBlank = true;
                                        
                                          if(data=='Satelital'){                                             
                                                aplicar_arrendamiento.setVisible(true);
                                                aplicar_arrendamiento.allowBlank = false;
                                                
                                               
                                          }else{
                                               aplicar_arrendamiento.setVisible(false);
                                               aplicar_arrendamiento.allowBlank = true;
                                               aplicar_arrendamiento.setValue(0);
                                          }
                                         
                                          if(data=='ID AD EMPRESAS'){                                              
                                              relacionado_ti.setVisible(true);
                                              relacionado_ti.allowBlank = false; 
                                              //arrendamiento IDAD
                                              aplicar_arrendamiento_IDAD.setVisible(true);
                                              aplicar_arrendamiento_IDAD.allowBlank = false;
                          
                                          }else{                                                          
                                              relacionado_ti.setVisible(false);
                                              relacionado_ti.allowBlank = true;
                                              relacionado_ti.setValue(0);
                                              
                                              //arrendamiento IDAD no se debe visualizar
                                              aplicar_arrendamiento_IDAD.setVisible(false);
                                              aplicar_arrendamiento_IDAD.allowBlank = true;
                                              
                                          }                                         
                                         
                                          if (data=='TI' | data=='ID AD MAYORISTA'){                                                                                            
                                                velocidad_subida.setVisible(false);
                                                velocidad_bajada.setVisible(false);
                                                                                                
                                                velocidad_subida.allowBlank = true;
                                                velocidad_bajada.allowBlank = true;                                            
                                                
                                                velocidad_bajada.setValue("");
                                                velocidad_subida.setValue("");
                                                                                             
 
                                          }else{
                                                velocidad_subida.setVisible(true);
                                                velocidad_bajada.setVisible(true);
                                                                                    
                                                velocidad_subida.allowBlank = false;
                                                velocidad_bajada.allowBlank = false;  
                                                
                                          }
                                    }
                                }
                        },{
                            xtype:'combobox',
                            fieldLabel:'Servicio',
                            store:null,
                            bodyPadding: 5,
                            width: 340,
                            displayField: 'NO_PROD',
                            valueField: 'CO_PROD',
                            name: 'CO_PROD'+obj.sufijoName,
                            editable: false,
                            allowBlank: obj.allowBlank,  
                            queryMode: 'local',
                             listeners:{
                                 select:function( combo, record, eOpts ){                                                
                                     //combo_planes_mayorista = combo_planes_mayorista_2;
                                     //var cc = Ext.getCmp('combobox-1070-inputEl');
                                     //cc.clearValue();
                                     //cc.reset();
                                      
                                     var codigoServicio = record[0].data.CO_PROD;
                                     var dataServicio=record[0].data.NO_PROD;
                                        
                                     if(dataServicio=='Datos Dedicado Mayorista'){
                                                                                        
                                              velocidad_ok_mayorista.setVisible(true);
                                              velocidad_ok_mayorista.allowBlank = false;
                                              velocidad_ok_mayorista.setValue(1);
                                     }else{
                                            
                                              velocidad_ok_mayorista.setVisible(false);
                                              velocidad_ok_mayorista.allowBlank = true;
                                              velocidad_ok_mayorista.setValue(0);
                                     }  
                                     
                                     
                                     if(producto_descripcion == 'ID AD MAYORISTA'){
                                        
                                        if(dataServicio=='Datos Dedicado Mayorista' | dataServicio=='Internet Dedicados Mayorista'){
                                                                                   
                                            velocidad_subida.setVisible(true);
                                            velocidad_bajada.setVisible(true);

                                            velocidad_subida.allowBlank = false;
                                            velocidad_bajada.allowBlank = false;  
                                            
                                            combo_planes_mayorista.setVisible(false);
                                            
                                        }else{
                                            
                                            velocidad_subida.setVisible(false);
                                            velocidad_bajada.setVisible(false);

                                            velocidad_subida.allowBlank = true;
                                            velocidad_bajada.allowBlank = true;
                                            
                                            ///////////////////////////////////
                                            // INCIO COMBO DESCRIPCION DEL PLAN
                                            ///////////////////////////////////
                                            combo_planes_mayorista.setVisible(true);
                                            var CO_NOMBRE_PLAN_INITIAL=null;
                                            function createStoreNombrePlanes(data){return Ext.create("Ext.data.Store",{fields:[{name:'COD_REGLAS_NOMBRE_PLAN'},{name:'NOMBRE_PLAN'}],data:data});}                                            
                                            var comboNombrePlan=obj.query("[name='COD_REGLAS_NOMBRE_PLAN"+obj.sufijoNombrePlan+"']")[0];
                                            var storeNombrePlan=new Desktop.Facturacion.Stores.PLAN();
                                            fnc.addParameterStore(storeNombrePlan,obj.packageParent.varSend,obj.packageParent.package);
                                            fnc.setApiProxyStore(storeNombrePlan,{read:'getNombrePlanByServicio/'});
                                            storeNombrePlan.load({

                                                params:{
                                                    CO_PROD:codigoServicio//obj.CO_PROD
                                                },
                                                callback: function(records, operation, success) {
                                                    if (success){
                                                        
                                                        if (records.length>0){
                                                                    
                                                            var data=[];
                                                            //comboNombrePlan.setStore(data);
                                                            
                                                            for(var i=0;i<records.length;i++)
                                                                data.push(records[i].data);  
                                                            var storeNombrePlan=createStoreNombrePlanes(data);
                                                            comboNombrePlan.setStore(storeNombrePlan); 
                                                            comboNombrePlan.setValue(CO_NOMBRE_PLAN_INITIAL);
                                                            if (CO_NOMBRE_PLAN_INITIAL!=null)
                                                                CO_NOMBRE_PLAN_INITIAL=null;
                                                            //for(var i=0;i<obj.stores.PROD.data.length;i++)
                                                            
                                                            //comboNombrePlan.setStore(data);
                                                            //comboNombrePlan.setValue(data.PROD_PADRE.COD_PROD_PADRE);                                                          
                                                        }
                                                    }
                                                }
                                            });
                                            
                                            /////////////////////////////////
                                            // FIN COMBO DESCRIPCION DEL PLAN
                                            /////////////////////////////////
   
                                        } 
                                                                                
                                     }
                                                                                                                        
                                 }                                         
                             }
                        },
                        velocidad_ok_mayorista,
                        velocidad_subida,
                        velocidad_bajada,
                        {
                            xtype:'combobox',
                            fieldLabel:'Medio Instalación',
                            store:null,
                            bodyPadding: 5,
                            width: 340,
                            displayField:'NO_PLAN_MEDI_INST',
                            valueField:'CO_PLAN_MEDI_INST',                            
                            name:'CO_PLAN_MEDI_INST'+obj.sufijoNameMedio,                            
                            editable:false, 
                            allowBlank:false, 
                            queryMode: 'local',                                        
                        },
                        combo_planes_mayorista,
                        aplicar_arrendamiento,
                        aplicar_arrendamiento_IDAD,
                        relacionado_ti
                    ]
                }                
            ];
                                  
            
            fnc.load.ModelsAndStores(["PROD","PROD_PADRE"],function(){
                
                var CO_SERVICIO_INITIAL=null;
                var CO_MEDIO_INITIAL=null;
                
                obj.packageParent=obj.packageParent || {};
                obj.stores={};
                obj.stores.PROD=new Desktop.Facturacion.Stores.PROD();
                obj.stores.PROD_PADRE=new Desktop.Facturacion.Stores.PROD_PADRE();                
                obj.stores.PLAN_MEDI_INST=new Desktop.Facturacion.Stores.PLAN_MEDI_INST();
                                               
                obj.stores.PROD.getProxy().getApi().read=obj.stores.PROD.AJAX_URI+"selectServiceByProd/";
                obj.stores.PLAN_MEDI_INST.getProxy().getApi().read=obj.stores.PLAN_MEDI_INST.AJAX_URI+"selectByProd/";
                
                fnc.addParameterStore(obj.stores.PROD,obj.packageParent.varSend,obj.packageParent.package);
                fnc.addParameterStore(obj.stores.PROD_PADRE,obj.packageParent.varSend,obj.packageParent.package);
                fnc.addParameterStore(obj.stores.PLAN_MEDI_INST,obj.packageParent.varSend,obj.packageParent.package);
                
                var combProd=obj.query("[name='COD_PROD_PADRE"+obj.sufijoName+"']")[0];
                var combServi=obj.query("[name='CO_PROD"+obj.sufijoName+"']")[0];
                var combMedio=obj.query("[name='CO_PLAN_MEDI_INST"+obj.sufijoNameMedio+"']")[0];
                //var combServi=obj.query("[name='"+obj.name+"']")[0];
                
                function createStoreServicio(data){return Ext.create("Ext.data.Store",{fields:[{name:'CO_PROD'},{name:'NO_PROD'}],data:data});}
                function createStoreMedio(data){return Ext.create("Ext.data.Store",{fields:[{name:'CO_PROD'},{name:'NO_PROD'}],data:data});}
                //function createStoreProvincia(data){return Ext.create("Ext.data.Store",{fields:[{name:'CO_PROV'},{name:'NO_PROV'}],data:data});}
                
                obj.stores.PROD_PADRE.load({callback:function(){
                    combProd.setStore(obj.stores.PROD_PADRE);
                }});
                combProd.on('change',function(combo,COD_PROD_PADRE){
                    COD_PROD_PADRE=fnc.parse.String.toInt(COD_PROD_PADRE);
                    if(COD_PROD_PADRE!=null){   
                        
                        obj.stores.PROD.load({params:{CO_PROD:COD_PROD_PADRE},callback:function(){
                            
                            var data=[];
                            for(var i=0;i<obj.stores.PROD.data.length;i++)
                                data.push(obj.stores.PROD.data.items[i].data);                          
                            var storeServi=createStoreServicio(data);
                            combServi.setStore(storeServi);                                                        
                            combServi.setValue(CO_SERVICIO_INITIAL);
                            if (CO_SERVICIO_INITIAL!=null)
                                CO_SERVICIO_INITIAL=null;
                        }});
                    
                        obj.stores.PLAN_MEDI_INST.load({params:{CO_PROD:COD_PROD_PADRE},callback:function(){
                            
                            var data=[];
                            for(var i=0;i<obj.stores.PLAN_MEDI_INST.data.length;i++)
                                data.push(obj.stores.PLAN_MEDI_INST.data.items[i].data);                          
                            var storeMedio=createStoreMedio(data);
                            combMedio.setStore(storeMedio);                                                        
                            combMedio.setValue(CO_MEDIO_INITIAL);
                            if (CO_MEDIO_INITIAL!=null)
                                CO_MEDIO_INITIAL=null;
                        }});
                       
                    
                    }
                });
                
                if (typeof obj.CO_PROD != 'undefined'){
                    var store=new Desktop.Facturacion.Stores.PROD();
                    fnc.addParameterStore(store,obj.packageParent.varSend,obj.packageParent.package);
                    fnc.setApiProxyStore(store,{read:'getServiceByIdCombo/'});
                    store.load({
                        
                        params:{
                            CO_PROD:obj.CO_PROD
                        },
                        callback: function(records, operation, success) {
                            if (success){
                                if (records.length==1){
                                                                                                                                                                                                                                                              
                                    var data=records[0].data;                                    
                                    combProd.setValue(data.PROD_PADRE.COD_PROD_PADRE);
                                    CO_SERVICIO_INITIAL=data.CO_PROD;
                                    CO_MEDIO_INITIAL = obj.CO_PLAN_MEDI_INST;       
                                    
                                    ////////////////////////////////////////////////
                                    if(obj.DESC_PROD_PADRE=='ID AD MAYORISTA'){
                                                                               
                                        if(obj.NO_PROD=='Ducteria' | obj.NO_PROD=='Colocacion' | obj.NO_PROD=='Otros servicios'){ 
                                        var idNombre;   
                                            //////////////////////////////////////////
                                           
                                            var storeCodigoNombrePlan=new Desktop.Facturacion.Stores.PLAN();
                                            fnc.addParameterStore(storeCodigoNombrePlan,obj.packageParent.varSend,obj.packageParent.package);
                                            fnc.setApiProxyStore(storeCodigoNombrePlan,{read:'getIdNombrePlan/'});
                                            storeCodigoNombrePlan.load({                                              
                                                params:{
                                                    NO_PLAN:obj.NO_PLAN//obj.CO_PROD
                                                }, 
                                                callback: function(records, operation, success) {
                                                   
                                                    if (success){
                                                        
                                                          if (records.length>0){
                                                             
                                                            idNombre =records[0].data.COD_REGLAS_NOMBRE_PLAN
                                                             
                                                          }
                                                        
                                                    }  
                                                }
                                            });
                                             
                                            combo_planes_mayorista.setVisible(true);
                                            var CO_NOMBRE_PLAN_INITIAL=null;
                                            function createStoreNombrePlanes(data){return Ext.create("Ext.data.Store",{fields:[{name:'COD_REGLAS_NOMBRE_PLAN'},{name:'NOMBRE_PLAN'}],data:data});}                                            
                                            var comboNombrePlan=obj.query("[name='COD_REGLAS_NOMBRE_PLAN"+obj.sufijoNombrePlan+"']")[0];
                                            var storeNombrePlan=new Desktop.Facturacion.Stores.PLAN();
                                            fnc.addParameterStore(storeNombrePlan,obj.packageParent.varSend,obj.packageParent.package);
                                            fnc.setApiProxyStore(storeNombrePlan,{read:'getNombrePlanByServicio/'});
                                            storeNombrePlan.load({

                                                params:{
                                                    CO_PROD:obj.CO_PROD//obj.CO_PROD
                                                },
                                                callback: function(records, operation, success) {
                                                    if (success){
                                                        
                                                        if (records.length>0){
                                                                    
                                                            var data=[];
                                                            //comboNombrePlan.setStore(data);
                                                            
                                                            for(var i=0;i<records.length;i++)
                                                                data.push(records[i].data);  
                                                            var storeNombrePlan=createStoreNombrePlanes(data);
                                                            comboNombrePlan.setStore(storeNombrePlan);
                                                            //alert("llego primero 2");
                                                            comboNombrePlan.setValue(idNombre)//(CO_NOMBRE_PLAN_INITIAL);
                                                            if (CO_NOMBRE_PLAN_INITIAL!=null)
                                                                CO_NOMBRE_PLAN_INITIAL=null;
                                                            //for(var i=0;i<obj.stores.PROD.data.length;i++)
                                                            
                                                            //comboNombrePlan.setStore(data);
                                                            //comboNombrePlan.setValue(data.PROD_PADRE.COD_PROD_PADRE);                                                          
                                                        }
                                                    }
                                                }
                                            });                                                                                        
                                            //////////////////////////////////////////                                                                                                                                   
                                            velocidad_subida.setVisible(false);
                                            velocidad_bajada.setVisible(false);
                                    
                                            velocidad_subida.allowBlank = true;
                                            velocidad_bajada.allowBlank = true;                                            
                                           
                                            combo_planes_mayorista.setVisible(true);
                                            combo_planes_mayorista.allowBlank = false;
                                        }else{
                                            
                                            if(obj.NO_PROD=='Datos Dedicado Mayorista'){                                                
                                                    
                                                velocidad_ok_mayorista.setVisible(true);
                                                velocidad_ok_mayorista.allowBlank = false;
                                                
                                                if(obj.DE_VELO_SUBI.length>0 | obj.DE_VELO_SUBI.length>0){                                                    
                                                    velocidad_ok_mayorista.setValue(1);
                                                }else{                                                
                                                    velocidad_ok_mayorista.setValue(0);
                                                    velocidad_subida.setVisible(false);
                                                    velocidad_bajada.setVisible(false);

                                                    velocidad_subida.allowBlank = true;
                                                    velocidad_bajada.allowBlank = true;     
                                                }                                              
                                            }
                                           
                                            combo_planes_mayorista.setVisible(false);
                                            combo_planes_mayorista.allowBlank = true;
                                        }                                                                              
                                    }else{                                        
                                           
                                            combo_planes_mayorista.setVisible(false);
                                            combo_planes_mayorista.allowBlank = true;
                                    }
                                    ////////////////////////////////////////////////
                                   
                                    if(obj.DESC_PROD_PADRE=='Satelital'){  
                                    
                                       aplicar_arrendamiento.setVisible(true);
                                       aplicar_arrendamiento.allowBlank = false;
                                    }else{
                                       aplicar_arrendamiento.setVisible(false);
                                       aplicar_arrendamiento.allowBlank = true;
                                       aplicar_arrendamiento.setValue(0);
                                    }
                                    ////////////////////////////////////////////////
                                    if(obj.DESC_PROD_PADRE=='TI'){                                     
                                      velocidad_subida.setVisible(false);
                                      velocidad_bajada.setVisible(false);
                                    
                                      velocidad_subida.allowBlank = true;
                                      velocidad_bajada.allowBlank = true;                                                                                            
                                    }
                                    
                                    if(obj.DESC_PROD_PADRE=='ID AD EMPRESAS'){                                    
                                      relacionado_ti.setVisible(true);                                      
                                      relacionado_ti.allowBlank = false;
                                      
                                    //arrendamiento IDAD
                                      aplicar_arrendamiento_IDAD.setVisible(true);
                                      aplicar_arrendamiento_IDAD.allowBlank = false;
                                    //
                                      
                                      if(obj.RELACIONADO_TI==true){                                          
                                          relacionado_ti.setValue(1);
                                      }else{
                                          relacionado_ti.setValue(0);
                                      }
                                      
                                    }else{
                                      relacionado_ti.setVisible(false);                                      
                                      relacionado_ti.allowBlank = true;
                                      
                                    //arrendamiento IDAD no se debe visualizar
                                      aplicar_arrendamiento_IDAD.setVisible(false);
                                      aplicar_arrendamiento_IDAD.allowBlank = true;
                                      
                                      relacionado_ti.setValue(0);
                                    }
                                    
                                 
                                    
                                }
                            }
                        }
                    });
                }
           
            });            
            obj.callParent(); 
        }
    })
})();