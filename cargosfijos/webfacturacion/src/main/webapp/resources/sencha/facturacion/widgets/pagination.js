(function(){
    var package='Desktop.Facturacion.Widgets.pagination';  
    Ext.define(package,{
        extend:'Ext.container.Container',
        alias:'widget.'+package,
        width:'300',
        layout: {
            type: 'hbox',
            align: 'left'
        },
        style:{
        	paddingTop:'5px',
        },
        initComponent:function(){
            var thisObj=this;
            thisObj.addAfterItems=thisObj.addAfterItems || [];
            thisObj.addBeforeItems=thisObj.addBeforeItems || [];
            thisObj.items=[];
            thisObj.callParent();
            if (thisObj.store){
                
                var pagination=Ext.create({ xtype: 'pagingtoolbar',displayInfo: true,width: '120',store:thisObj.store});
                for(var i in thisObj.addBeforeItems){
                    var to=thisObj.addBeforeItems[i];
                    to.style=to.style || {};
                    to.style.marginLeft=to.style.marginLeft ||  '5px';
                    to.style.marginRight=to.style.marginRight ||  '5px';
                    to.style.paddingTop=to.style.paddingTop || '0px';
                    to.style.height=to.style.height || '26px';
                    thisObj.add(to);   
                }                
                thisObj.add(pagination);                    

                for(var i in thisObj.addAfterItems){
                    var to=thisObj.addAfterItems[i];
                    to.style=to.style || {};
                    to.style.marginLeft=to.style.marginLeft ||  '5px';
                    to.style.marginRight=to.style.marginRight ||  '5px';
                    to.style.paddingTop=to.style.paddingTop || '0px';
                    to.style.height=to.style.height || '26px';                        
                    thisObj.add(to);   
                }
            }
        }
    });
})();