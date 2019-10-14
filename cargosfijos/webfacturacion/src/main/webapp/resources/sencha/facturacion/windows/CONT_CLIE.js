(function(){
    var package='Desktop.Facturacion.Windows.CONT_CLIE';
    var configWindow={};
    for(var key in settings.Permissions[package]){
        configWindow[key]=settings.Permissions[package][key];
    }
    Ext.define(configWindow.package, {
        extend: "Ext.ux.desktop.Module",
        id: configWindow.id,
        init: function() {
            this.launcher = {
                text: configWindow.title,
                iconCls: configWindow.iconCls || fnc.icons.class.win_modulo_default()
            }
        },
        createWindow: function() {
            var c = this.app.getDesktop();
            var d = c.getWindow(configWindow.id);
            if (!d) {
                var items=[];
                for (var i in configWindow.items){
                    var objPadre=configWindow.items[i];
                    var objHijo=objPadre.items[0];
                    for(var key in objPadre){
                        if (key!='items'){
                            objHijo[key]=objPadre[key];
                        }
                    }
                    items.push(Ext.create(objHijo));                
                }
                d = c.createWindow({
                    id: configWindow.id,
                    title: configWindow.title,
                    width: 800,
                    height: 400,
                    iconCls: configWindow.iconCls || fnc.icons.class.win_modulo_default(),
                    animCollapse: false,
                    constrainHeader: true,
                    border: false,
                    hideMode: "offsets",
                    layout: "fit",
                    items: [
                        Ext.create("Ext.tab.Panel",{ 
                            items: items
                        })
                    ]
                })
            };
            return d;
        }
    });
})();
