(function(){
    var package='Desktop.Facturacion.Widgets.comboBoxServicios'; 
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
            obj.sufijoLabel=obj.sufijoLabel || "";
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
                            displayField: 'DESC_PROD_PADRE',
                            valueField: 'COD_PROD_PADRE',
                            name: 'COD_PROD_PADRE'+obj.sufijoName,
                            editable: false,
                            allowBlank: obj.allowBlank, 
                            queryMode: 'local',
                        },{
                            xtype:'combobox',
                            fieldLabel:'Servicio',
                            store:null,
                            displayField: 'NO_PROD',
                            valueField: 'CO_PROD',
                            name: 'CO_PROD'+obj.sufijoName,
                            editable: false,
                            allowBlank: obj.allowBlank,  
                            queryMode: 'local',
                        }
                    ]
                }                
            ];
                                  
            
            fnc.load.ModelsAndStores(["PROD","PROD_PADRE"],function(){
                
                var CO_SERVICIO_INITIAL=null;
                
                obj.packageParent=obj.packageParent || {};
                obj.stores={};
                obj.stores.PROD=new Desktop.Facturacion.Stores.PROD();
                obj.stores.PROD_PADRE=new Desktop.Facturacion.Stores.PROD_PADRE();                
                               
                obj.stores.PROD.getProxy().getApi().read=obj.stores.PROD.AJAX_URI+"selectServiceByProd/";
                
                fnc.addParameterStore(obj.stores.PROD,obj.packageParent.varSend,obj.packageParent.package);
                fnc.addParameterStore(obj.stores.PROD_PADRE,obj.packageParent.varSend,obj.packageParent.package);
                
                var combProd=obj.query("[name='COD_PROD_PADRE"+obj.sufijoName+"']")[0];
                var combServi=obj.query("[name='CO_PROD"+obj.sufijoName+"']")[0];
                //var combServi=obj.query("[name='"+obj.name+"']")[0];
                
                function createStoreServicio(data){return Ext.create("Ext.data.Store",{fields:[{name:'CO_PROD'},{name:'NO_PROD'}],data:data});}
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
                                    //CO_DIST_INICITAL=data.CO_DIST;
                                }
                            }
                        }
                    });
                }
                    
            /*
                combServi.on('change',function(combo,CO_PROV){
                    CO_PROV=fnc.parse.String.toInt(CO_PROV);
                    if (CO_PROV!=null){
                        obj.stores.DIST.load({params:{CO_PROV:CO_PROV},callback:function(){
                            var data=[];
                            for(var i=0;i<obj.stores.DIST.data.length;i++)
                                data.push(obj.stores.DIST.data.items[i].data);
                            var storeDist=createStoreDistrito(data);
                            //combDist.setStore(storeDist);
                            //combDist.setValue(CO_DIST_INICITAL);                            
                            if (CO_DIST_INICITAL!=null)
                                CO_DIST_INICITAL=null;
                        }});
                    }
                }); 
                 */
            /*
                if (typeof obj.CO_PROD != 'undefined'){
                    var store=new Desktop.Facturacion.Stores.PROD();
                    fnc.addParameterStore(store,obj.packageParent.varSend,obj.packageParent.package);
                    fnc.setApiProxyStore(store,{read:'getid/'});
                    store.load({
                        params:{
                            CO_PROD:obj.CO_PROD
                        },
                        callback: function(records, operation, success) {
                            if (success){
                                if (records.length==1){
                                    var data=records[0].data;
                                    //combProd.setValue(data.PROV.CO_DEPA);
                                    CO_SERVICIO_INITIAL=data.CO_PROD;
                                  
                                }
                            }
                        }
                    });
                } */
            });            
            obj.callParent(); 
        }
    })
})();