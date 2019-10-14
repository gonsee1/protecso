(function(){
    var package='Desktop.Facturacion.Windows.CIER';
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
                var tp=Ext.create("Ext.tab.Panel",{
                    xtype: 'basic-tabs',   
                    defaults: {
                        bodyPadding: 10,
                        scrollable: true,
                        autoScroll:true,
                    },
                    layout: 'fit',
                    items: configWindow.items
                });
                d = c.createWindow({
                    id: configWindow.id,
                    title: configWindow.title,
                    width: 1320,
                    height: 550,
                    iconCls: configWindow.iconCls || fnc.icons.class.win_modulo_default(),
                    animCollapse: false,
                    constrainHeader: true,
                    border: false,
                    hideMode: "offsets",
                    layout: "fit",
                    items: [tp]
                });
            };
            return d;
        }
    });
})();
