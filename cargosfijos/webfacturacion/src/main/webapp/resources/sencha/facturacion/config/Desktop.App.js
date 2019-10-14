Ext.onReady(function(){
    var settings=window.settings;
    var getTitleWindowById=function(id){for (var r in settings.Permissions) {r=settings.Permissions[r];if(r.id==id) return r.title;}}
    var windows=(function(){
        var arr=[];
        var t; 
        Desktop=Desktop || {};
        Desktop.Facturacion=Desktop.Facturacion || {};
        Desktop.Facturacion.Windows=Desktop.Facturacion.Windows || {};        
        for(var reg in Desktop.Facturacion.Windows){            
            eval("t=new Desktop.Facturacion.Windows."+reg+"();");
            arr.push(t);
        }
        return arr;
    })();
    var itemsDesktop=(function(){
        /*
            {
                        name: "Grid Window",
                        iconCls: "grid-shortcut",
                        module: "grid-win"
                    }, {
                        name: "Accordion Window",
                        iconCls: "accordion-shortcut",
                        module: "acc-win"
                    }, {
                        name: "Notepad",
                        iconCls: "notepad-shortcut",
                        module: "notepad"
                    }, {
                        name: "System Status",
                        iconCls: "cpu-shortcut",
                        module: "systemstatus"
                    }
                        {
                            name:"Modulos",
                            iconCls:"grid-shortcut",
                            module:"idWin_1",
                        } ,{
                        name: "Notepad",
                        iconCls: "notepad-shortcut",
                        module: "notepad"
                    }
        */
       var ret=[];
       for(var w in windows){
           w=windows[w];      
           ret.push({name:getTitleWindowById(w.id),iconCls: w.launcher.iconCls || "grid-shortcut" ,module:w.id,position:"absolute",left:'10px',top:'20px'});
       }
       return ret;
    })();
    Ext.define("Desktop.App", {
        extend: "Ext.ux.desktop.App",
        init: function() {
            this.callParent();
            //this.desktop.id='id_desktop_facturacion';
        },
        getModules: function() {
            return windows;
        },
        getDesktopConfig: function() {
            var c = this,
                d = c.callParent();
            return Ext.apply(d, {
                contextMenuItems: [{
                    text: "Change Settings",
                    handler: c.onSettings,
                    scope: c
                }],
                shortcuts: Ext.create("Ext.data.Store", {
                    model: "Ext.ux.desktop.ShortcutModel",
                    data: itemsDesktop
                }),
                wallpaper: "resources/images/wallpapers/Blue-Sencha.jpg",
                wallpaperStretch: false
            })
        },
        getStartConfig: function() {
            var c = this,
                d = c.callParent();
            return Ext.apply(d, {
                //title: "Don Griffin",
                title: settings.online.nombre+" "+settings.online.apellido,
                iconCls: "user",
                height: 400,
                toolConfig: {
                    width: 150,
                    items: [{
                        text: "Configuración",
                        iconCls: "settings",
                        handler: c.onSettings,
                        scope: c
                    }, "-", {
                        text: "Cerrar Sesión",
                        iconCls: "logout",
                        handler: c.onLogout,
                        scope: c
                    }]
                }
            })
        },
        getTaskbarConfig: function() {
            var b = this.callParent();
            return Ext.apply(b, {
                quickStart: [{
                    name: "Accordion Window",
                    iconCls: "accordion",
                    module: "acc-win"
                }, {
                    name: "Grid Window",
                    iconCls: "icon-grid",
                    module: "grid-win"
                }],
                trayItems: [{
                    xtype: "trayclock",
                    flex: 1
                }]
            })
        },
        onLogout: function() {
            Ext.Msg.confirm("Cerrar Sesión", "¿Esta seguro de cerrar sessión?",function(a,b,c,d){
                if (a==='yes'){
                    window.location.href="./logout";
                }
            });
        },
        onSettings: function() {
            var b = new Desktop.Settings({
                desktop: this.desktop,            
            });
            b.show()
        }
    });
    Ext.define("Desktop.model.Wallpaper", {
        extend: "Ext.data.TreeModel",
        fields: [{
            name: "text"
        }, {
            name: "img"
        }]
    });    
});