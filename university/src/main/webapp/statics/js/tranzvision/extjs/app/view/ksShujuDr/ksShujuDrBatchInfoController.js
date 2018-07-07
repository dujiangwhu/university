Ext.define('KitchenSink.view.ksShujuDr.ksShujuDrBatchInfoController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.ksShujuDrBatchInfoController',
    
    onInfoClose:function(){
        this.getView().close();
    }
});
