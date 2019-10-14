/*
Ext.Loader.setConfig({
    enabled: true
});
Ext.Loader.setPath('Ext', '/resources/sencha/framework/Ext/');
Ext.Loader.loadScript({url:'/resources/sencha/framework/src/lang/Assert.js'});
*/
Ext.application({
    name: "Desktop",
    init: function() {
        var b = new Desktop.App();        
        Desktop.Facturacion.getDesktop=function(){return b.getDesktop()};
        
        //disable acciones de botton borrar <-
        //Ext.EventManager.on(document,'keydown',function(e){  if( e.getKey()==8){ if (e.target.type!='text') e.stopEvent();}});       
    }
});