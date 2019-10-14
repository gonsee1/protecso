Ext.define("Ext.ux.desktop.Module", {
    mixins: {
        observable: "Ext.util.Observable"
    },
    constructor: function(b) {
        this.mixins.observable.constructor.call(this, b);
        this.init()
    },
    init: Ext.emptyFn
});
Ext.define("Ext.ux.desktop.ShortcutModel", {
    extend: "Ext.data.Model",
    fields: [{
        name: "name"
    }, {
        name: "iconCls"
    }, {
        name: "module"
    }]
});
var windowIndex=0;