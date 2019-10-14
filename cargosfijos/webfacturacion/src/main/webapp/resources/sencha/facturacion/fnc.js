/*
 * Desarrollado por Christian Rodas
 */
var fnc=(function(){
    return {
        clone:function(obj){
            if(obj == null || typeof(obj) != 'object')
                return obj;
            var temp = obj.constructor(); // changed
            for(var key in obj) {
                if(obj.hasOwnProperty(key)) {
                    temp[key] = fnc.clone(obj[key]);
                }
            }
            return temp;
        },
        mergeObjects:function (a,b){//si existe key se remplaza     
            var c={};        
            if(a instanceof Object && b instanceof Object ){
                for(var k in a) c[k]=a[k];
                for(var k in b){
                    //if(typeof c[k] =='undefined')
                    c[k]=b[k];
                }
            }
            return c;
        },
        createWindowModal:function(desktop,obj){
            if (typeof obj=='undefined' && typeof desktop!='undefined' && desktop instanceof Object ){
                obj=desktop;
                desktop=Desktop.Facturacion.getDesktop();
            }        
            obj=obj || {};
            obj.renderTo=obj.renderTo || Ext.getBody();
            var par=fnc.mergeObjects({
                xtype:'window',
                width:400,
                height:400,
                modal:true,
                hideMode: "offsets",
                layout: "fit",
                maximizable:false,
                minimizable:false,
                closable:true,
                floatable:false,
                isWindow:false,
                stateful:true,
                constrainHeader:false, 
                defaults: {
                    bodyPadding: 10,
                    scrollable: true,
                    autoScroll:true,
                },
                //renderTo:Ext.getBody(),
            },obj);
            return desktop.createWindow(par);
        },
        createWindow:function(desktop,obj){        
            if (typeof obj=='undefined' && typeof desktop!='undefined' && desktop instanceof Object ){
                obj=desktop;
                desktop=Desktop.Facturacion.getDesktop();
            }
            obj=obj || {};        
            var par=fnc.mergeObjects({
                xtype:'window',
                width:400,
                height:400,
                hideMode: "offsets",
                layout: "fit",
                defaults: {
                    bodyPadding: 10,
                    scrollable: true,
                    autoScroll:true,
                },                
            },obj);
            return desktop.createWindow(par);
        },
        createWindowHistorial:function(obj){
            if (typeof SubTable =='undefined'){
                Ext.define('SubTable.model.Base', {
                    extend: 'Ext.data.Model',
                    schema: {
                        namespace: 'SubTable.model'
                    }
                });
                Ext.define('SubTable.model.HistorialCampo', {
                    extend: 'SubTable.model.Base',

                    fields: [
                        { name: 'id', type: 'int' },
                        { name: 'historialId', type: 'int', reference: 'Historial' },
                        'shipped'
                    ]
                });
                Ext.define('SubTable.model.Historial', {
                    extend: 'SubTable.model.Base',
                    fields: ['name']
                });
            }
            if (typeof obj == 'object' && typeof obj.type != 'undefined' && typeof obj.idEntity != 'undefined' && typeof obj.AJAX_URI != 'undefined' && typeof obj.varSendPackage!='undefined' && typeof obj.packageBase!='undefined'){
                var type=obj.type;
                var idEntity=obj.idEntity;
                var AJAX_URI=obj.AJAX_URI;
                var params={type:type,idEntity:idEntity};
                params[obj.varSendPackage]=obj.packageBase;
                var grid=Ext.create('Ext.grid.Panel',{
                    plugins: {
                        ptype: "subtable",
                        association: 'historialCampos',
                        columns: [{
                            text: 'Campo',
                            dataIndex: 'campo',
                        },{
                            text: 'Anterior',
                            dataIndex: 'anterior',                        
                        },{
                            text: 'Nuevo',
                            dataIndex: 'nuevo',                      
                        }]
                    },

                    store: {
                        autoLoad: true,
                        proxy:{
                            type:'ajax',
                            url:AJAX_URI+'/HISTORIAL/getHistorial/',
                            actionMethods: {create : 'POST',read   : 'POST',update : 'POST',destroy: 'POST'},
                            reader:{
                                type: 'json',
                                rootProperty: 'data',
                                successProperty:'success',
                            },
                            extraParams:params
                        },
                        model: 'SubTable.model.Historial'
                    },
                    columns: [{
                        text: 'N°',
                        dataIndex: '#',
                        width: 50,
                    },{
                        text: 'Fecha Hora',
                        dataIndex: 'FH',
                        hideable: false,
                        width: 100,
                    },{
                        text: 'Usuario',
                        dataIndex: 'USUA',
                        hideable: false,
                        width: 100,
                    },{
                        text: 'Acción',
                        dataIndex: 'accion',
                        width:80,
                     },{
                        text: 'Descripción',
                        dataIndex: 'entidad',
                        width:420,
                     }]                
                });
                delete obj.type;    
                obj.width=800;
                obj.height=500;                
                obj.title=obj.title || 'Historial';
                obj.items=[grid];
                return fnc.createWindow(obj);
            }
        },
        createMsgBoxInfo:function(obj){
            Ext.MessageBox.show(fnc.mergeObjects({title:'Mensaje de Sistema',msg:'Este es un mensaje.',icon: Ext.MessageBox['INFO']},obj || {}));
        },
        createMsgBoxError:function(obj){
            Ext.MessageBox.show(fnc.mergeObjects({title:'Mensaje de Sistema',msg:'Se presento un error, favor de informar el problema al adminsitrador.',icon: Ext.MessageBox['ERROR']},obj || {}));
        },
        createMsgBoxSuccess:function(obj){
            Ext.MessageBox.show(fnc.mergeObjects({title:'Mensaje de Sistema',msg:'Se realizo lo solicitado con exito.',icon: Ext.MessageBox['INFO']},obj || {}));
        },    
        createMsgConfirm:function(obj){
            var o=fnc.mergeObjects({title:'Confirme',msg:'Si o No',call:function(){}},obj || {});
            Ext.MessageBox.confirm(o.title,o.msg,o.call);
        },    
        addParameterStore:function(store,param,value){
            if (store instanceof Ext.data.Store){
                var proxy=store.getProxy();
                proxy.setExtraParam(param,value);
            }
        },
        addParameterModel:function(model,param,value){
            if (model instanceof Ext.data.Model){
                var proxy=model.getProxy();
                proxy.setExtraParam(param,value);
            }
        },        
        getParamsSendPackage:function(params,key,package){
            var ret={};
            for(var k in params)
                ret[k]=params[k];
            ret[key]=package;
            return ret;
        },
        icons:{
            class:{
                edit:function(){return "fac_icon_edit"},
                insert:function(){return "fac_icon_edit"},
                delete:function(){return "fac_icon_edit"},
                explorar:function(){return "fac_icon_edit"},
                actualizar:function(){return "fac_icon_edit"},
                lanzar:function(){return "fac_icon_edit"},
                ver:function(){return "fac_icon_edit"},
                configurar:function(){return "fac_icon_edit"},
                
                win_modulo_default:function(){return "win_modulo_default"},

                win_modulo:function(cls){return "cssicon_"+cls}
            }
        },
        images:{
            uri:{
                edit:function(){return "./resources/images/icons/fam/page_white_edit.png"},
                insert:function(){return "./resources/images/icons/fam/page_white_edit.png"},
                delete:function(){return "./resources/images/icons/fam/page_white_edit.png"},
                explorar:function(){return "./resources/images/icons/fam/page_white_edit.png"},
                actualizar:function(){return "./resources/images/icons/fam/page_white_edit.png"},
                lanzar:function(){return "./resources/images/icons/fam/page_white_edit.png"},
                ver:function(){return "./resources/images/icons/fam/page_white_edit.png"},
                win_modulo_default:function(){return "./resources/images/icons/fam/page_white_edit.png"},
            }
        },    
        load:{
            script:function(obj){
                if(obj){
                    obj.url='./resources/sencha/facturacion/'+obj.url;
                    Ext.Loader.loadScript(obj);
                }
            },
            model:function(name,success,error){
                fnc.load.script({
                    url:"models/"+(name || "")+".js?t="+(new Date()).getTime(),
                    onLoad:success || function(){},
                    onError:error || function(){},
                })
            },
            store:function(name,success,error){
                fnc.load.script({
                    url:"stores/"+(name || "")+".js?t="+(new Date()).getTime(),
                    onLoad:success || function(){},
                    onError:error || function(){},
                })
            },
            ModelAndStore:function(name,success,error){
                name=name || "";
                success=success || function(){};
                error=error || function(){};
                fnc.load.model(name,function(){
                    fnc.load.store(name,success,error);
                },error)
            },
            scripts:function(jss,success,error){
                success=success || function(){};
                error=error || function(){}; 
                jss=jss || [];
                var van=0;
                var son=jss.length;
                var errores=0;
                function evaluate(){
                    if(van==son){
                        if(errores==0)
                            success();
                        else
                            error();
                    }
                }
                for(var i=0;i<jss.length;i++){
                    fnc.load.script({url:jss[i],onLoad:function(){van++;evaluate();},onError:function(){van++;errores=0;evaluate();}});
                }
                if(jss.length==0)
                    evaluate();
            },        
            ModelsAndStores:function(names,success,error){
                name=name || [];
                success=success || function(){};
                error=error || function(){};
                var son=names.length;
                var van=0;
                var vanErr=0;
                function evaluate(){
                    if(van==son){
                        if(vanErr==0)
                            success();
                        else
                            error();
                    }                
                }            
                for(var i in names){
                   fnc.load.ModelAndStore(names[i],function(){van++;evaluate();},function(){vanErr++;van++;evaluate();});
                }
                if(names.length==0)
                    evaluate();           
            },
            panel:function(package,name,success,error){
                package=package||"";
                name=name||"";
                success=success || function(){};
                error=error || function(){};
                fnc.load.script({
                    url:"panels/"+package+"/"+name+".js?t="+(new Date()).getTime(),
                    onLoad:success,
                    onError:error,
                })            
            },
            panels:function(arr,success,error){
                arr=arr || [];
                success=success || function(){};
                error=error || function(){};            
                var son=arr.length,van=0,vanE=0;
                function evaluate(){
                    if (son==van){
                        if (vanE==0)
                            success();
                        else
                            error();
                    }                
                }
                for(var i in arr){
                    var reg=arr[i];
                    if (reg instanceof Array && reg.length==2){
                        fnc.load.panel(reg[0],reg[1],function(){
                            van++;
                            evaluate();
                        },function(){
                            vanE++;
                            van++;
                            evaluate();
                        });
                    }
                }
            }        
        },
        parse:{
            String:{
                toInt:function(d){var r=null;try{r=parseInt(d);}catch(e){};if (isNaN(r))r=null;return r;},
                toFloat:function(d){var r=null;try{r=parseFloat(d);}catch(e){};if (isNaN(r))r=null;return r;},
            }
        },    
        setApiProxyStore:function(storePar,apiPar){
            if (storePar instanceof Ext.data.Store){
                var proxy=storePar.getProxy();
                var api=proxy.getApi();
                apiPar=apiPar || {};
                for(var k in apiPar){
                    /*if(typeof api[k]=='undefined')
                        api[k]=storePar.AJAX_URI+apiPar[k];
                    else*/
                    api[k]=storePar.AJAX_URI+apiPar[k];
                }
            }
        },    
        setApiProxyModel:function(modelPar,apiPar){
            if (modelPar instanceof Ext.data.Model){
                var proxy=modelPar.getProxy();
                var api=proxy.getApi();
                apiPar=apiPar || {};
                for(var k in apiPar){
                    /*if(typeof api[k]=='undefined')
                        api[k]=modelPar.AJAX_URI+apiPar[k];
                    else*/
                    api[k]=modelPar.AJAX_URI+apiPar[k];
                }
            }
        },
        modelSave:function(model,obj,success,error){
            success=success || function(){};
            error=error || function(){};
            if (typeof obj.callback=='undefined'){
                obj.callback=function(a,b,c){
                    if(c){
                        fnc.createMsgBoxSuccess();
                        success();                    
                    }else{
                        fnc.createMsgBoxError();
                        error();
                    }
                }
            }
            model.save(obj);
        },
        number:{
            redondear:function (n,d){
                    var d=fnc.parse.String.toInt(d);
                    var n=fnc.parse.String.toFloat(n);
                    var f=Math.pow(10,d);
                    return Math.round(n*f)/f;
            }    	
        },
        JSON:{
            encode:function(obj){
                if (typeof(obj)=='object'){
                    try {
                        return Ext.JSON.encode(obj);
                    }catch(e){}
                }
                return null;
            },
            decode:function(obj){
                if (typeof(obj)=='string'){
                    try {
                        return Ext.JSON.decode(obj);
                    }catch(e){}
                }
                return null;
            }
        },
        responseEvaluateOperationError:function(operation){
            var json=fnc.JSON.decode(operation._response.responseText);
            if (json!=null){
                fnc.createMsgBoxError({msg:json.msg});
            }else{
                fnc.createMsgBoxError({msg:'Ocurrio un error favor de validar, si persiste el problema pongase en contacto con el administrador.'});
            }            
        },
        responseExtAjaxRequestMsjError:function(response){
            response=response || "{success:false,msg:'Ocurrio un error favor de validar, si persiste el problema pongase en contacto con el administrador.'};";
            var json=fnc.JSON.decode(response.responseText);    
            if (!json.success){
                fnc.createMsgBoxError({msg:json.msg});
            }
            return json;
        },    
        openURL:function(url){
            var w=window.open(url);
        },
        getEnums:function(name,callback){
            /*callback=callback||function(){};
            Ext.Ajax.request({
                method:'POST',
                url: '/ajax/ENUMS/get'+name+'/',
                success: function(response){
                    var json=fnc.responseExtAjaxRequestMsjError(response);
                    if (json.success){
                        callback(json);
                    }
                }
            });*/
            var model=Desktop.Facturacion.Models.ENUMS;
            if (typeof model=='undefined'){
                (function(){
                    var AJAX_URI=settings.AJAX_URI+'ENUMS/';
                    Ext.define('Desktop.Facturacion.Models.ENUMS', {
                        extend: 'Ext.data.Model',
                        AJAX_URI:AJAX_URI,
                        fields: [
                            {name: 'name', type: 'string'},                       
                            {name: 'value', type: 'string'},
                        ],        
                        proxy: {
                            type: 'ajax',
                            url : '/ajax/',
                            noCache: false,
                            actionMethods: {create : 'POST',read   : 'POST',update : 'POST',destroy: 'POST'},
                            api: {
                                read:AJAX_URI+'select/',
                                update:AJAX_URI+'update/',
                                create:AJAX_URI+'insert/',
                                delete:AJAX_URI+'delete/',
                            },
                            reader:{
                                type: 'json',
                                rootProperty: 'data',
                                successProperty:'success',
                            },
                            write:{
                                type:'ajax',
                            }
                        },    
                    });    
                })();
                var model=Desktop.Facturacion.Models.ENUMS;
            }
            var store=Desktop.Facturacion.Stores.ENUMS;
            if (typeof store=='undefined'){
                (function(){
                    var AJAX_URI=settings.AJAX_URI+'ENUMS/';
                    Ext.define('Desktop.Facturacion.Stores.ENUMS', {
                        extend:'Ext.data.Store',
                        model: 'Desktop.Facturacion.Models.ENUMS',
                        alias:'store.Desktop.Facturacion.Stores.ENUMS',
                        AJAX_URI:AJAX_URI,
                        proxy: {
                            type: 'ajax',
                            url : '/ajax/',
                            noCache: false,            
                            actionMethods: {create : 'POST',read   : 'POST',update : 'POST',destroy: 'POST'},
                            api: {
                                read:AJAX_URI+'select/',
                                update:AJAX_URI+'update/',
                                create:AJAX_URI+'insert/',
                                destroy:AJAX_URI+'delete/',
                            },
                            reader:{
                                type: 'json',
                                rootProperty: 'data',
                                totalProperty:'numResults',
                                successProperty:'success',
                            },
                            write:{
                                type:'json',
                            }            
                        },   
                        autoLoad: true,   
                    });
                })(); 
                var store=Desktop.Facturacion.Stores.ENUMS;
            }            
            store=new store();            
            fnc.setApiProxyStore(store,{read:'get'+name+'/'});
            store.load({
                callback:function(){
                    callback(store);
                }
            });
        },
        forms:{
            combobox:{
                autoselect:function(combo){
                    if (combo.getSelectedRecord()) combo.fireEvent('select',this,[combo.getSelectedRecord()]);
                }
            }
        },
        shortcodes:{
            getCortado:function(bool){
                if (bool){
                    return '<span class="label label-danger">Cortado</span>';
                }else{
                    return '';
                }
            },
            getSuspendido:function(bool){
                if (bool){
                    return '<span class="label label-info">Suspendido</span>';
                }else{
                    return '';
                }
            },
            getMudado:function(bool){
                if (bool){
                    return '<span class="label label-warning">Mudado</span>';
                }else{
                    return '';
                }
            },
            getDesactivado:function(bool){
                if (bool){
                    return '<span class="label label-danger">Desactivado</span>';
                }else{
                    return '';
                }
            },
            getActivo:function(){
                return '<span class="label label-success">Activo</span>';
            },
            getPendiente:function(bool){
                if (bool){
                    return '<span class="label label-default">Pendiente</span>';
                }else{
                    return '';
                }
            }
        },
        export:{
            
            storeToExcel2: function (store) {

                var data = store.data.items;

                var table = document.createElement("table");
                for (var i = 0; i < data.length; i++) {
                    var val = data[i].data;
                    var tr = document.createElement("tr");
                    delete val['id'];
                    delete val['FH_MODI'];
                    delete val['FH_CREO'];
                    delete val['CO_USUA_MODI'];
                    delete val['CO_USUA_CREO'];
                    delete val['FH_CLIECREO'];
                    delete val['PROD'];
                    delete val['CO_SUCU_CORR'];
                    delete val['SUCU_CORR'];
                    delete val['id'];
                    delete val['PERI_FACT'];
                    delete val['MONE_FACT'];
                    delete val['TIPO_FACT'];
                    delete val['isArrendamiento'];
                    delete val['isSuspendido'];
                    delete val['isPendiente'];
                    delete val['isCortado'];
                    delete val['ST_ELIM'];
                    delete val['CO_MONE_FACT'];
                    delete val['isDesactivado'];
                    delete val['CO_PERI_FACT'];
                    delete val['CO_TIPO_FACT'];
                    delete val['CO_NEGO_ORIG'];
                    delete val['CO_PROD'];
                    delete val['CO_CLIE'];
                    delete val['nuevo'];
                    delete val['desactivado'];
                    delete val['CLIE'];
                    delete val['PROD_PADRE'];
                    delete val['REFERENCIA'];
                    delete val['COD_PROD_PADRE'];
                    if (i == 0) {
                        for (var x in val) {
                            var td = document.createElement("th");
                            td.innerHTML = x;
                            tr.appendChild(td);
                        }
                        table.appendChild(tr);
                        var tr = document.createElement("tr");
                    }
                    for (var x in val) {
                        var td = document.createElement("td");
                        td.innerHTML = val[x];
                        tr.appendChild(td);
                    }
                    table.appendChild(tr);
                }
                ExcellentExport.excel(document.location, table);
            },
            
            storeToExcel:function(store){
                var data=store.data.items;
                var table=document.createElement("table");
                for(var i=0;i<data.length;i++){
                    var val=data[i].data; 
                    var tr=document.createElement("tr");
                    delete val['id'];
                    delete val['COD_PROD_PADRE'];
                    if (i==0){
                        for(var x in val){
                            var td=document.createElement("th");
                            td.innerHTML=x;
                            tr.appendChild(td);
                        }                    
                        table.appendChild(tr);
                        var tr=document.createElement("tr");                    
                    }
                    for(var x in val){
                        var td=document.createElement("td");
                        td.innerHTML=val[x];
                        tr.appendChild(td);
                    }
                    table.appendChild(tr);
                }
                ExcellentExport.excel(document.location,table);
            },
            gridToExcel:function(grid){
                fnc.export.storeToExcel(grid.getStore());
            },
            gridToExcel2: function (grid) {
                fnc.export.storeToExcel2(grid.getStore());
            }
        }
    };
    
})();