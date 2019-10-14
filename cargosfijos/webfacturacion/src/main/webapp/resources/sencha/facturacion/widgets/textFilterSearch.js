(function(){
    var package='Desktop.Facturacion.Widgets.textFilterSearch';  
    Ext.define(package,{
        extend:'Ext.form.FieldContainer',
        alias:'widget.'+package,
        initComponent:function(){
            var objCon=this;            
            this.onSearch=this.onSearch || function(){};
            this.onBeforeSearch=this.onBeforeSearch || function(){};
            
            this.getValue=function(){
                return objCon.items.items[0].value;
            }
            function sendSearch(value){
                objCon.onBeforeSearch();
                if (objCon.store){
                    objCon.store.load({
                        params:{SEARCH:value}
                    });
                }
                objCon.onSearch(value);
            }
            this.items=[
                {
                    xtype:'textfield',
                    emptyText:'Buscar...',
                    style:{
                        float:'left',
                    },
                    width:'150px',
                    fieldStyle:"height:24px;padding-right: 33px;",
                    enableKeyEvents:true,
                    listeners:{
                        keypress:function(obj,e,eOpts){
                            if (e.charCode==13)
                               sendSearch(objCon.getValue()); 
                        }
                    }
                },{
                    xtype:'button',
                    text:'Buscar',
                    style:{
                        marginLeft: '-30px',
                        paddingTop: '0px',
                        height: '26px',
                    },
                    listeners:{
                        click:function(){
                            sendSearch(objCon.getValue());
                        }
                    }
                }
            ];
            this.callParent();
        }
    });
})();