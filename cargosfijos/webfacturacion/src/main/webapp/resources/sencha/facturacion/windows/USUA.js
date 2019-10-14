(function(){
    var package='Desktop.Facturacion.Windows.USUA';
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
                d = c.createWindow({
                    id: configWindow.id,
                    title: configWindow.title,
                    width: 800,
                    height: 400,
                    iconCls: configWindow.iconCls || fnc.icons.class.win_modulo_default(),
                    animCollapse: false,
                    border: false,
                    hideMode: "offsets",
                    layout: "fit",
                    items: [
                        Ext.create("Ext.tab.Panel",{
                            xtype: 'basic-tabs',   
                            defaults: {
                                bodyPadding: 10,
                                scrollable: true,
                                autoScroll:true,
                            },
                            layout: 'fit',
                            items: configWindow.items,
                        })
                    ]
                })
            };
            return d;
        }
    });
})();
