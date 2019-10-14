(function(){
    var package='Desktop.Facturacion.Widgets.comboBoxDistrito'; 
    Ext.define(package,{
        extend:'Ext.form.FieldContainer',
        alias:'widget.'+package,
        initComponent:function(){
            var obj=this;  
            obj.allowBlank= (typeof obj.allowBlank =='undefined'?false:obj.allowBlank);
            obj.width=obj.width || "400px";
            obj.width=parseInt(obj.width); 
            obj.title=obj.title || 'Selección Distrito';
            obj.name=obj.name || "CO_DIST";
            obj.sufijoName=obj.sufijoName || "";
            obj.sufijoLabel=obj.sufijoLabel || "";
            obj.items=[
                {
                    xtype:'fieldset',
                    title: obj.title ,
                    allowBlank: false,
                    layout:'vbox',
                    style:{
                        width:obj.width+'px',
                    },
                    items:[
                        {
                            xtype:'combobox',
                            fieldLabel:'Departamento '+obj.sufijoLabel,
                            store:null,
                            displayField: 'NO_DEPA',
                            valueField: 'CO_DEPA',
                            name: 'CO_DEPA'+obj.sufijoName,
                            editable: false,
                            allowBlank: obj.allowBlank, 
                            queryMode: 'local',
                        },{
                            xtype:'combobox',
                            fieldLabel:'Provincia '+obj.sufijoLabel,
                            store:null,
                            displayField: 'NO_PROV',
                            valueField: 'CO_PROV',
                            name: 'CO_PROV'+obj.sufijoName,
                            editable: false,
                            allowBlank: obj.allowBlank,  
                            queryMode: 'local',
                        },{
                            xtype:'combobox',
                            fieldLabel:'Distrito '+obj.sufijoLabel,
                            store:null,
                            displayField: 'NO_DIST',
                            valueField: 'CO_DIST',
                            name: obj.name,
                            editable: false,
                            allowBlank: obj.allowBlank,  
                            queryMode: 'local',
                        }
                    ]
                }                
            ];
            
            fnc.load.ModelsAndStores(["DEPA","PROV","DIST"],function(){
                
                var CO_PROV_INITIAL=null,CO_DIST_INICITAL=null;
                
                obj.packageParent=obj.packageParent || {};
                obj.stores={};
                obj.stores.DEPA=new Desktop.Facturacion.Stores.DEPA();
                obj.stores.PROV=new Desktop.Facturacion.Stores.PROV();
                obj.stores.DIST=new Desktop.Facturacion.Stores.DIST();
                obj.stores.PROV.getProxy().getApi().read=obj.stores.PROV.AJAX_URI+"selectByDepa/";
                obj.stores.DIST.getProxy().getApi().read=obj.stores.DIST.AJAX_URI+"selectByProv/";
                fnc.addParameterStore(obj.stores.DEPA,obj.packageParent.varSend,obj.packageParent.package);
                fnc.addParameterStore(obj.stores.PROV,obj.packageParent.varSend,obj.packageParent.package);
                fnc.addParameterStore(obj.stores.DIST,obj.packageParent.varSend,obj.packageParent.package);
                var combDepa=obj.query("[name='CO_DEPA"+obj.sufijoName+"']")[0];
                var combProv=obj.query("[name='CO_PROV"+obj.sufijoName+"']")[0];
                var combDist=obj.query("[name='"+obj.name+"']")[0];
                function createStoreDistrito(data){return Ext.create("Ext.data.Store",{fields:[{name:'CO_DIST'},{name:'NO_DIST'}],data:data});}
                function createStoreProvincia(data){return Ext.create("Ext.data.Store",{fields:[{name:'CO_PROV'},{name:'NO_PROV'}],data:data});}
                obj.stores.DEPA.load({callback:function(){
                    combDepa.setStore(obj.stores.DEPA);
                }});
                combDepa.on('change',function(combo,CO_DEPA){
                    CO_DEPA=fnc.parse.String.toInt(CO_DEPA);
                    if(CO_DEPA!=null){
                        obj.stores.PROV.load({params:{CO_DEPA:CO_DEPA},callback:function(){
                            var data=[];
                            for(var i=0;i<obj.stores.PROV.data.length;i++)
                                data.push(obj.stores.PROV.data.items[i].data);
                            var storeProv=createStoreProvincia(data);
                            combProv.setStore(storeProv);
                            combDist.setStore(createStoreDistrito([]));                            
                            combDist.setValue(null);
                            combProv.setValue(CO_PROV_INITIAL);
                            if (CO_PROV_INITIAL!=null)
                                CO_PROV_INITIAL=null;
                        }});
                    }
                });
                combProv.on('change',function(combo,CO_PROV){
                    CO_PROV=fnc.parse.String.toInt(CO_PROV);
                    if (CO_PROV!=null){
                        obj.stores.DIST.load({params:{CO_PROV:CO_PROV},callback:function(){
                            var data=[];
                            for(var i=0;i<obj.stores.DIST.data.length;i++)
                                data.push(obj.stores.DIST.data.items[i].data);
                            var storeDist=createStoreDistrito(data);
                            combDist.setStore(storeDist);
                            combDist.setValue(CO_DIST_INICITAL);                            
                            if (CO_DIST_INICITAL!=null)
                                CO_DIST_INICITAL=null;
                        }});
                    }
                }); 
                if (typeof obj.CO_DIST != 'undefined'){
                    var store=new Desktop.Facturacion.Stores.DIST();
                    fnc.addParameterStore(store,obj.packageParent.varSend,obj.packageParent.package);
                    fnc.setApiProxyStore(store,{read:'getid/'});
                    store.load({
                        params:{
                            CO_DIST:obj.CO_DIST
                        },
                        callback: function(records, operation, success) {
                            if (success){
                                if (records.length==1){
                                    var data=records[0].data;
                                    combDepa.setValue(data.PROV.CO_DEPA);
                                    CO_PROV_INITIAL=data.CO_PROV;
                                    CO_DIST_INICITAL=data.CO_DIST;
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