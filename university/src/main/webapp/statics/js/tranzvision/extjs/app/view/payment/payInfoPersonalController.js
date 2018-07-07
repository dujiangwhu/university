Ext.define('KitchenSink.view.payment.payInfoPersonalController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.payInfoPersonalController',

    savePersonalPayForm:function(btn){
    	
        var form=btn.findParentByType("form").getForm();
        if(!form.isValid() ){
            return false;
        }
        var id = form.findField("id").getValue();
        var payStatus = form.findField("payStatus").getValue();
        //if(payStatus!=2){
        var tzParams = '{"ComID":"TZ_ZFCKJGL_COM","PageID":"TZ_SEARCH_DETAIL","OperateType":"U","comParams":{"update":[{"id":"'+id+'","payStatus":"'+payStatus+'"}]}}';
        Ext.tzSubmit(tzParams,function(){
        },"",true,this);
       // }
    },
   
    //窗口确定
    ensurePersonalPayForm:function(btn){
    	var form=btn.findParentByType("form");
    	   if(!form.getForm().isValid() ){
               return false;
           }
           var id = form.getForm().findField("id").getValue();
           var payStatus = form.getForm().findField("payStatus").getValue();
           //if(payStatus!=2){
           	var tzParams = '{"ComID":"TZ_ZFCKJGL_COM","PageID":"TZ_SEARCH_DETAIL","OperateType":"U","comParams":{"update":[{"id":"'+id+'","payStatus":"'+payStatus+'"}]}}';
           	Ext.tzSubmit(tzParams,function(){
           },"",true,this);
          // }
           form.close();
    },
    //关闭
    closePersonalPayForm:function(btn){
        var win=btn.findParentByType("form");
        win.close();
    },
    
});

