
Ext.define("Desktop.Settings", {
    extend: "Ext.window.Window",
    layout: "anchor",
    title: "Change Settings",
    modal: true,
    width: 640,
    height: 480,
    border: false,
    initComponent: function() {
        var b = this;
        b.selected = b.desktop.getWallpaper();
        b.stretch = b.desktop.wallpaper.stretch;
        b.preview = Ext.create("widget.wallpaper");
        b.preview.setWallpaper(b.selected);
        b.tree = b.createTree();
        b.buttons = [{
            text: "OK",
            handler: b.onOK,
            scope: b
        }, {
            text: "Cancel",
            handler: b.close,
            scope: b
        }];
        b.items = [{
            anchor: "0 -30",
            border: false,
            layout: "border",
            items: [b.tree, {
                xtype: "panel",
                title: "Preview",
                region: "center",
                layout: "fit",
                items: [b.preview]
            }]
        }, {
            xtype: "checkbox",
            boxLabel: "Stretch to fit",
            checked: b.stretch,
            listeners: {
                change: function(a) {
                    b.stretch = a.checked
                }
            }
        }];
        b.callParent()
    },
    createTree: function() {
        var d = this;

        function f(a) {
            return {
                img: a,
                text: d.getTextOfWallpaper(a),
                iconCls: "",
                leaf: true
            }
        }
        var e = new Ext.tree.Panel({
            title: "Desktop Background",
            rootVisible: false,
            lines: false,
            autoScroll: true,
            width: 150,
            region: "west",
            split: true,
            minWidth: 100,
            listeners: {
                afterrender: {
                    fn: this.setInitialSelection,
                    delay: 100
                },
                select: this.onSelect,
                scope: this
            },
            store: new Ext.data.TreeStore({
                model: "Desktop.model.Wallpaper",
                root: {
                    text: "Wallpaper",
                    expanded: true,
                    children: [{
                        text: "None",
                        iconCls: "",
                        leaf: true
                    }, f("Blue-Sencha.jpg"), f("Dark-Sencha.jpg"), f("Wood-Sencha.jpg"), f("blue.jpg"), f("desk.jpg"), f("desktop.jpg"), f("desktop2.jpg"), f("sky.jpg")]
                }
            })
        });
        return e
    },
    getTextOfWallpaper: function(h) {
        var g = h,
            e = h.lastIndexOf("/");
        if (e >= 0) {
            g = g.substring(e + 1)
        }
        var f = g.lastIndexOf(".");
        g = Ext.String.capitalize(g.substring(0, f));
        g = g.replace(/[-]/g, " ");
        return g
    },
    onOK: function() {
        var b = this;
        if (b.selected) {
            b.desktop.setWallpaper(b.selected, b.stretch)
        }
        b.destroy()
    },
    onSelect: function(e, d) {
        var f = this;
        if (d.data.img) {
            f.selected = "resources/images/wallpapers/" + d.data.img
        } else {
            f.selected = Ext.BLANK_IMAGE_URL
        }
        f.preview.setWallpaper(f.selected)
    },
    setInitialSelection: function() {
        var d = this.desktop.getWallpaper();
        if (d) {
            var c = "/Wallpaper/" + this.getTextOfWallpaper(d);
            this.tree.selectPath(c, "text")
        }
    }
});