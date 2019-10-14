Ext.define("Ext.ux.desktop.Wallpaper", {
    extend: "Ext.Component",
    alias: "widget.wallpaper",
    cls: "ux-wallpaper",
    html: '<img src="' + Ext.BLANK_IMAGE_URL + '">',
    stretch: false,
    wallpaper: null,
    stateful: true,
    stateId: "desk-wallpaper",
    afterRender: function() {
        var b = this;
        b.callParent();
        b.setWallpaper(b.wallpaper, b.stretch)
    },
    applyState: function() {
        var c = this,
            d = c.wallpaper;
        c.callParent(arguments);
        if (d != c.wallpaper) {
            c.setWallpaper(c.wallpaper)
        }
    },
    getState: function() {
        return this.wallpaper && {
            wallpaper: this.wallpaper
        }
    },
    setWallpaper: function(f, g) {
        var k = this,
            h, j;
        k.stretch = (g !== false);
        k.wallpaper = f;
        if (k.rendered) {
            h = k.el.dom.firstChild;
            if (!f || f == Ext.BLANK_IMAGE_URL) {
                Ext.fly(h).hide()
            } else {
                if (k.stretch) {
                    h.src = f;
                    k.el.removeCls("ux-wallpaper-tiled");
                    Ext.fly(h).setStyle({
                        width: "100%",
                        height: "100%"
                    }).show()
                } else {
                    Ext.fly(h).hide();
                    j = "url(" + f + ")";
                    k.el.addCls("ux-wallpaper-tiled")
                }
            }
            k.el.setStyle({
                backgroundImage: j || ""
            });
            if (k.stateful) {
                k.saveState()
            }
        }
        return k
    }
});
