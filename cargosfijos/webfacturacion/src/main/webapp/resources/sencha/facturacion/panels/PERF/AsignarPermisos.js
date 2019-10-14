Ext.onReady(function(){
    fnc.load.ModelsAndStores(["PERF","ITEM_MODU","PERF_ITEM_MODU"],function(){
        var package="Desktop.Facturacion.Panels.PERF.AsignarPermisos";
        var AJAX_URI=settings.AJAX_URI+'PERF/';
        var objDesktop=null;
        var storePerf=new Desktop.Facturacion.Stores.PERF();
        var varSendPackage=settings.Permissions.varSendPackage;
        fnc.addParameterStore(storePerf,varSendPackage,package);
        Ext.define(package,{ 
            extend:'Ext.grid.GridPanel',
            alias:'widget.Desktop.Facturacion.Panels.PERF.AsignarPermisos',
            store:storePerf,
            columns: [
                { text: 'Codigo',  dataIndex: 'CO_PERF',width:'20%' },
                { text: 'Nombre', dataIndex: 'NO_PERF', flex: 1,width:'40%' },
            ],
            width:'100%',
            tbar: [{
                xtype: 'button',
                text: "Administrar Permisos",
                tooltip: "Editar",
                iconCls: fnc.icons.class.edit(),
                listeners:{
                    click:function(btn,e,opts){
                        asignarDesktop(this);
                        var grid=this.up('grid');
                        var selected=grid.getView().getSelectionModel().getSelection();
                        if(selected.length==1){
                            administrar(selected[0].data);
                        }
                    }
                }
            }, "-", {
                xtype: 'button',
                text: "Actualizar",
                tooltip: "Actualizar",
                iconCls: "refresh",
                listeners:{
                    click:function(){
                        storePerf.load();
                    }
                }
            }]    
        });
        //Asignar Desktop
        function asignarDesktop(hijo){
            if (objDesktop==null)
                objDesktop=hijo.up("desktop");
        }
        //Administrar Permisos
        function administrar(obj){
            if(objDesktop!==null){
                var storePermisions=new Desktop.Facturacion.Stores.PERF_ITEM_MODU();
                var storeItems=new Desktop.Facturacion.Stores.ITEM_MODU();
                fnc.addParameterStore(storePermisions,varSendPackage,package);
                fnc.addParameterStore(storeItems,varSendPackage,package);
                storePermisions.load({
                    params:obj,
                    callback:function(){
                        storeItems.load({
                           callback:function(){  
                                var losDemas=(function(){
                                        var pad=[];
                                        function existe(a,t){for(var k=0;k<a.length;k++) if(a[k].CO_MODU==t) return k;return -1;}
                                        function tengoEstePermiso(item){
                                            for(var i=0;i<storePermisions.data.items.length;i++){
                                                var itemTmp=storePermisions.data.items[i].data;
                                                if (itemTmp.ITEM_MODU.CO_ITEM_MODU==item.CO_ITEM_MODU){
                                                    return true;
                                                }
                                            }
                                            return false;
                                        }
                                        for(var i=0;i<storeItems.data.items.length;i++){
                                            var item=storeItems.data.items[i].data;
                                            var modu=item.MODU;                                            
                                            if (!tengoEstePermiso(item)){
                                                var pos=existe(pad,modu.CO_MODU);
                                                if(pos==-1){
                                                    pad.push({text:modu.NO_MODU,expanded: true,children:[],CO_MODU:item.CO_MODU});
                                                    pos=pad.length-1;
                                                }
                                                pad[pos].children.push({text:item.NO_ITEM_MODU,leaf:true,CO_MODU:item.CO_MODU,CO_ITEM_MODU:item.CO_ITEM_MODU});
                                            }
                                        }
                                        return pad;
                                    })();
                                var tengo=(function(){
                                    var pad=[];
                                    function existe(a,t){for(var k=0;k<a.length;k++) if(a[k].text==t) return k;return -1;}
                                    for(var i=0;i<storePermisions.data.items.length;i++){
                                        var item=storePermisions.data.items[i].data.ITEM_MODU;
                                        var modu=item.MODU;
                                        var pos=existe(pad,modu.NO_MODU);
                                        if(pos==-1){
                                            pad.push({text:modu.NO_MODU,expanded: true,children:[],CO_MODU:item.CO_MODU});
                                            pos=pad.length-1;
                                        }
                                        pad[pos].children.push({text:item.NO_ITEM_MODU,leaf:true,CO_MODU:item.CO_MODU,CO_ITEM_MODU:item.CO_ITEM_MODU});
                                    }                                    
                                    return pad;
                                })();
                                function getPadreModulo(node,CO_MODU){
                                    for(var i=0;i<node.childNodes.length;i++)
                                        if(node.childNodes[i].data.CO_MODU==CO_MODU)
                                            return node.childNodes[i];
                                    return null;
                                }
                                function getItemModulo(node,CO_ITEM_MODU){
                                    for(var i=0;i<node.childNodes.length;i++)
                                        if(node.childNodes[i].data.CO_ITEM_MODU==CO_ITEM_MODU)
                                            return node.childNodes[i];
                                    return null;                                    
                                }
                                function mover(de,a,item){
                                    if(item.children==null){
                                        //Es Hijo
                                        var nodeDe=de.getStore().getRootNode();
                                        var nodeA=a.getStore().getRootNode(); 
                                        var padreDe=getPadreModulo(nodeDe,item.CO_MODU);
                                        var padreA=getPadreModulo(nodeA,item.CO_MODU);
                                        var itemDe=getItemModulo(padreDe,item.CO_ITEM_MODU);
                                        if(padreA==null){
                                            nodeA.appendChild({text:padreDe.data.text,expanded: true,children:[],CO_MODU:padreDe.data.CO_MODU}); 
                                            padreA=getPadreModulo(nodeA,item.CO_MODU);
                                        }
                                        padreA.appendChild({text:item.text,leaf: true,CO_MODU:item.CO_MODU,CO_ITEM_MODU:item.CO_ITEM_MODU});
                                        itemDe.remove();
                                        if(padreDe.childNodes.length==0)
                                            padreDe.remove();
                                    }else{
                                        //Es padre
                                        var nodeDe=de.getStore().getRootNode();
                                        var padreDe=getPadreModulo(nodeDe,item.CO_MODU);                                        
                                        var son=padreDe.childNodes.length;
                                        var arr=[];
                                        for(var i=0;i<son;i++){                                            
                                            arr.push(padreDe.childNodes[i].data);
                                        }
                                        for(var i=0;i<son;i++){ 
                                            mover(de,a,arr[i]);
                                        }
                                    }                                    
                                }
                                var panelA=Ext.create('Ext.tree.Panel', {
                                    title: 'Los demas',
                                    store: Ext.create('Ext.data.TreeStore', {
                                        root: {
                                            expanded: true,
                                            children: losDemas,
                                        }
                                    }),
                                    rootVisible: false,
                                    width: 300,
                                    listeners:{
                                        itemdblclick:function(record, item, index, e, eOpts){
                                            var data=record.getStore().data.items[e].data;
                                            mover(panelA,panelB,data);
                                        }
                                    }                                                                       
                                });
                               
                                var panelB=Ext.create('Ext.tree.Panel', {
                                    title: 'Mis Permisos',
                                    store: Ext.create('Ext.data.TreeStore', {
                                        root: {
                                            expanded: true,
                                            children: tengo,
                                        }
                                    }),
                                    rootVisible: false,
                                    width: 300,                                    
                                    listeners:{
                                        itemdblclick:function(record, item, index, e, eOpts){
                                            var data=record.getStore().data.items[e].data;
                                            mover(panelB,panelA,data);
                                        }
                                    }                                   
                                }); 
                                var window=fnc.createWindowModal(objDesktop,{
                                    width:600,
                                    height:400,
                                    title:'Editar ' + obj.NO_PERF,
                                    renderTo:Ext.getBody(),
                                    layout: {
                                        type: "hbox",
                                        align: "stretch"
                                    },
                                    items:[panelA,panelB],
                                    buttons: [{
                                        text: 'Guardar',
                                        handler: function() {
                                            var node=panelB.getRootNode();
                                            var data={CO_ITEM_MODU:[]};
                                            var van=0;
                                            for(var i=0;i<node.childNodes.length;i++){
                                                var sub=node.childNodes[i];
                                                for(var j=0;j<sub.childNodes.length;j++){
                                                    data.CO_ITEM_MODU.push(sub.childNodes[j].data.CO_ITEM_MODU);
                                                    van++;
                                                }
                                            }
                                            var params={CO_PERF:obj.CO_PERF,CO_ITEM_MODU:data.CO_ITEM_MODU};
                                            params[varSendPackage] = package;
                                            Ext.Ajax.request({
                                                url: storePermisions.getProxy().getApi().update,
                                                params: params,
                                                success: function(response){
                                                    var json = Ext.JSON.decode(response.responseText);
                                                    if (json.success){
                                                        window.close();
                                                    }
                                                }
                                            });
                                            /*storePermisions.update({
                                                params:{CO_PERF:obj.CO_PERF,CO_ITEM_MODU:data.CO_ITEM_MODU},
                                                
                                            });*/
                                        }
                                    }, {
                                        text: 'Cancelar',
                                        handler: function() {
                                            window.close();
                                        }
                                    }] 
                                });
                                window.show();
                           } 
                        });
                    }
                });
            }
        }
    });
});