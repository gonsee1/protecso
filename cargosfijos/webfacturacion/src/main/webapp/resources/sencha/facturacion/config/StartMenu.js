Ext.define("Ext.ux.desktop.StartMenu", {
    extend: "Ext.panel.Panel",
    ariaRole: "menu",
    cls: "x-menu ux-start-menu",
    defaultAlign: "bl-tl",
    iconCls: "user",
    floating: true,
    shadow: true,
    width: 500,
    initComponent: function() {
        var d = this,
            c = d.menu;
        d.menu = new Ext.menu.Menu({
            cls: "ux-start-menu-body",
            border: false,
            floating: false,
            items: c
        });
        d.menu.layout.align = "stretch";
        d.items = [d.menu];
        d.layout = "fit";
        Ext.menu.Manager.register(d);
        d.callParent();
        d.toolbar = new Ext.toolbar.Toolbar(Ext.apply({
            dock: "right",
            cls: "ux-start-menu-toolbar",
            vertical: true,
            width: 100,
            listeners: {
                add: function(b, a) {
                    a.on({
                        click: function() {
                            d.hide()
                        }
                    })
                }
            }
        }, d.toolConfig));
        d.toolbar.layout.align = "stretch";
        d.addDocked(d.toolbar);
        delete d.toolItems
    },
    addMenuItem: function() {
        var b = this.menu;
        b.add.apply(b, arguments)
    },
    addToolItem: function() {
        var b = this.toolbar;
        b.add.apply(b, arguments)
    }
});