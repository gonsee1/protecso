Ext.define("Desktop.Facturacion.Windows.Notepad", {
    extend: "Ext.ux.desktop.Module",
    id: "notepad",
    init: function() {
        this.launcher = {
            text: "Notepad",
            iconCls: "notepad"
        }
    },
    createWindow: function() {
        var c = this.app.getDesktop();
        var d = c.getWindow("notepad");
        if (!d) {
            d = c.createWindow({
                id: "notepad",
                title: "Notepad",
                width: 600,
                height: 400,
                iconCls: "notepad",
                animCollapse: false,
                border: false,
                hideMode: "offsets",
                layout: "fit",
                items: [{
                    xtype: "htmleditor",
                    id: "notepad-editor",
                    value: ['Some <b>rich</b> <span style="color: rgb(255, 0, 0)">text</span> goes <u>here</u><br>', "Give it a try!"].join("")
                }]
            })
        }
        return d;
    }
});