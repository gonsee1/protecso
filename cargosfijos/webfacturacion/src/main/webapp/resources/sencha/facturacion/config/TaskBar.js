Ext.define("Ext.ux.desktop.TaskBar", {
    extend: "Ext.toolbar.Toolbar",
    alias: "widget.taskbar",
    cls: "ux-taskbar",
    startBtnText: "Inicio",
    initComponent: function() {
        var b = this;
        b.startMenu = new Ext.ux.desktop.StartMenu(b.startConfig);
        b.quickStart = new Ext.toolbar.Toolbar(b.getQuickStart());
        b.windowBar = new Ext.toolbar.Toolbar(b.getWindowBarConfig());
        b.tray = new Ext.toolbar.Toolbar(b.getTrayConfig());
        b.items = [{
            xtype: "button",
            cls: "ux-start-button",
            iconCls: "ux-start-button-icon",
            menu: b.startMenu,
            menuAlign: "bl-tl",
            text: b.startBtnText
        }, b.quickStart, {
            xtype: "splitter",
            html: "&#160;",
            height: 14,
            width: 2,
            cls: "x-toolbar-separator x-toolbar-separator-horizontal"
        }, b.windowBar, "-", b.tray];
        b.callParent()
    },
    afterLayout: function() {
        var b = this;
        b.callParent();
        b.windowBar.el.on("contextmenu", b.onButtonContextMenu, b)
    },
    getQuickStart: function() {
        var c = this,
            d = {
                minWidth: 20,
                width: Ext.themeName === "neptune" ? 70 : 60,
                items: [],
                enableOverflow: true
            };
        Ext.each(this.quickStart, function(a) {
            d.items.push({
                tooltip: {
                    text: a.name,
                    align: "bl-tl"
                },
                overflowText: a.name,
                iconCls: a.iconCls,
                module: a.module,
                handler: c.onQuickStartClick,
                scope: c
            })
        });
        return d
    },
    getTrayConfig: function() {
        var b = {
            items: this.trayItems
        };
        delete this.trayItems;
        return b
    },
    getWindowBarConfig: function() {
        return {
            flex: 1,
            cls: "ux-desktop-windowbar",
            items: ["&#160;"],
            layout: {
                overflowHandler: "Scroller"
            }
        }
    },
    getWindowBtnFromEl: function(d) {
        var c = this.windowBar.getChildByElement(d);
        return c || null
    },
    onQuickStartClick: function(d) {
        var e = this.app.getModule(d.module),
            f;
        if (e) {
            f = e.createWindow();
            f.show()
        }
    },
    onButtonContextMenu: function(g) {
        var h = this,
            e = g.getTarget(),
            f = h.getWindowBtnFromEl(e);
        if (f) {
            g.stopEvent();
            h.windowMenu.theWin = f.win;
            h.windowMenu.showBy(e)
        }
    },
    onWindowBtnClick: function(d) {
        var c = d.win;
        if (c.minimized || c.hidden) {
            d.disable();
            c.show(null, function() {
                d.enable()
            })
        } else {
            if (c.active) {
                d.disable();
                c.on("hide", function() {
                    d.enable()
                }, null, {
                    single: true
                });
                c.minimize()
            } else {
                c.toFront()
            }
        }
    },
    addTaskButton: function(f) {
        var e = {
            iconCls: f.iconCls,
            enableToggle: true,
            toggleGroup: "all",
            width: 140,
            margins: "0 2 0 3",
            text: Ext.util.Format.ellipsis(f.title, 20),
            listeners: {
                click: this.onWindowBtnClick,
                scope: this
            },
            win: f
        };
        var d = this.windowBar.add(e);
        d.toggle(true);
        return d
    },
    removeTaskButton: function(e) {
        var f, d = this;
        d.windowBar.items.each(function(a) {
            if (a === e) {
                f = a
            }
            return !f
        });
        if (f) {
            d.windowBar.remove(f)
        }
        return f
    },
    setActiveButton: function(b) {
        if (b) {
            b.toggle(true)
        } else {
            this.windowBar.items.each(function(a) {
                if (a.isButton) {
                    a.toggle(false)
                }
            })
        }
    }
});

Ext.define("Ext.ux.desktop.TrayClock", {
    extend: "Ext.toolbar.TextItem",
    alias: "widget.trayclock",
    cls: "ux-desktop-trayclock",
    html: "&#160;",
    timeFormat: "g:i A",
    tpl: "{time}",
    initComponent: function() {
        var b = this;
        b.callParent();
        if (typeof(b.tpl) == "string") {
            b.tpl = new Ext.XTemplate(b.tpl)
        }
    },
    afterRender: function() {
        var b = this;
        Ext.Function.defer(b.updateTime, 100, b);
        b.callParent()
    },
    onDestroy: function() {
        var b = this;
        if (b.timer) {
            window.clearTimeout(b.timer);
            b.timer = null
        }
        b.callParent()
    },
    updateTime: function() {
        var e = this,
            d = Ext.Date.format(new Date(), e.timeFormat),
            f = e.tpl.apply({
                time: d
            });
        if (e.lastText != f) {
            e.setText(f);
            e.lastText = f
        }
        e.timer = Ext.Function.defer(e.updateTime, 10000, e)
    }
});