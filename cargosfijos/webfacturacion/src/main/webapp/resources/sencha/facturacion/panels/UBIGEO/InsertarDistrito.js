Ext.onReady(function(){
    fnc.load.ModelsAndStores(["DEPA","PROV","DIST"],function(){
        var AJAX_URI=settings.AJAX_URI;
        var package="Desktop.Facturacion.Panels.UBIGEO.InsertarDistrito";
        var varSendPackage=settings.Permissions.varSendPackage;
        var storeDepa=new Desktop.Facturacion.Stores.DEPA();
        fnc.addParameterStore(storeDepa,varSendPackage,package); 
        var storeProv=Ext.create('Ext.data.Store',{
            model:'Desktop.Facturacion.Models.PROV',
            proxy: {
                type: 'ajax',
                url : '/ajax/',
                noCache: false,
                actionMethods: {create : 'POST',read   : 'POST',update : 'POST',destroy: 'POST'},
                api: {
                    read:AJAX_URI+'PROV/selectByDepa/',
                },
                reader:{
                    type: 'json',
                    rootProperty: 'data',
                    successProperty:'success',
                },
                write:{
                    type:'json',
                }            
            },
            autoLoad:false,
        });
        fnc.addParameterStore(storeProv,varSendPackage,package); 
        storeDepa.load();
        Ext.define(package,{
            model:'Desktop.Facturacion.Models.DIST',
            extend:'Ext.form.Panel',
            alias:'widget.Desktop.Facturacion.Panels.UBIGEO.InsertarDistrito',
            defaultType: 'textfield',
            items: [
              {
                xtype:'combobox',
                fieldLabel:'Departamento',
                store:storeDepa,
                displayField: 'NO_DEPA',
                valueField: 'CO_DEPA',
                name: 'CO_DEPA',
                allowBlank: false,
                listeners:{
                    change:function(obj,newValue, oldValue, eOpts){
                        var combo=obj.up("form").getForm().findField("CO_PROV");
                        if(newValue!=null){
                            storeProv.load({
                               params:{CO_DEPA:newValue},
                               callback:function(){
                                   var items=storeProv.data.items;
                                   if(items.length>0){
                                       var data=[];
                                       for(var i=0;i<items.length;i++)
                                           data.push(items[i].data);
                                       var store=Ext.create("Ext.data.Store",{fields:[{name:'CO_PROV'},{name:'NO_PROV'}],data:data});
                                       combo.setStore(store);
                                       combo.setValue(items[0].data.CO_PROV);
                                   }
                               }
                            });
                        }else{
                           combo.setStore(Ext.create("Ext.data.Store",{fields:[{name:'CO_PROV'},{name:'NO_PROV'}],data:[]})); 
                        }
                    }
                }
                
            },{
                xtype:'combobox',
                fieldLabel:'Provincia',
                store:null,
                displayField: 'NO_PROV',
                valueField: 'CO_PROV',
                name: 'CO_PROV', 
                allowBlank: false,
      
            },                
            {
                fieldLabel: 'Nombre',
                name: 'NO_DIST',
                allowBlank: false
            }],

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
                        var model=new Desktop.Facturacion.Models.DIST(form.getFieldValues());
                        model.save({
                            params:fnc.getParamsSendPackage(model.getData(),varSendPackage,package),
                            success: function (record, operation) {
                                fnc.createMsgBoxSuccess({msg:'Se registro con éxito el distrito.'});
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