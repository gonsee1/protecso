Ext.define("Ext.ux.desktop.App", {
    mixins: {
        observable: "Ext.util.Observable"
    },
    isReady: false,
    modules: null,
    useQuickTips: true,
    constructor: function(d) {
        var c = this;
        c.mixins.observable.constructor.call(this, d);
        if (Ext.isReady) {
            Ext.Function.defer(c.init, 10, c)
        } else {
            Ext.onReady(c.init, c)
        }
    },
    init: function() {
        var c = this,
            d;
        if (c.useQuickTips) {
            Ext.QuickTips.init()
        }
        c.modules = c.getModules();
        if (c.modules) {
            c.initModules(c.modules)
        }
        d = c.getDesktopConfig();
        c.desktop = new Ext.ux.desktop.Desktop(d);
        c.viewport = new Ext.container.Viewport({
            layout: "fit",
            items: [c.desktop]
        });
        Ext.getWin().on("beforeunload", c.onUnload, c);
        c.isReady = true;
        c.fireEvent("ready", c)
    },
    getDesktopConfig: function() {
        var c = this,
            d = {
                app: c,
                taskbarConfig: c.getTaskbarConfig()
            };
        Ext.apply(d, c.desktopConfig);
        return d
    },
    getModules: Ext.emptyFn,
    getStartConfig: function() {
        var d = this,
            e = {
                app: d,
                menu: []
            },
            f;
        Ext.apply(e, d.startConfig);
        Ext.each(d.modules, function(a) {
            f = a.launcher;
            if (f) {
                f.handler = f.handler || Ext.bind(d.createWindow, d, [a]);
                e.menu.push(a.launcher)
            }
        });
        return e
    },
    createWindow: function(d) {
        var c = d.createWindow();
        c.show()
    },
    getTaskbarConfig: function() {
        var c = this,
            d = {
                app: c,
                startConfig: c.getStartConfig()
            };
        Ext.apply(d, c.taskbarConfig);
        return d
    },
    initModules: function(d) {
        var c = this;
        Ext.each(d, function(a) {
            a.app = c
        })
    },
    getModule: function(j) {
        var k = this.modules;
        for (var h = 0, f = k.length; h < f; h++) {
            var g = k[h];
            if (g.id == j || g.appType == j) {
                return g
            }
        }
        return null
    },
    onReady: function(c, d) {
        if (this.isReady) {
            c.call(d, this)
        } else {
            this.on({
                ready: c,
                scope: d,
                single: true
            })
        }
    },
    getDesktop: function() {
        return this.desktop
    },
    onUnload: function(b) {
        if (this.fireEvent("beforeunload", this) === false) {
            b.stopEvent()
        }
    }
});