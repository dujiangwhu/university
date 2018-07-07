Ext.define('KitchenSink.view.studentBbTj.studentBbTjTreeStore', {
    extend: 'Ext.data.TreeStore',
    alias: 'store.studentBbTjTreeStore',
    rootVisible: false,
    constructor: function (config) {
        var root;
        var tzParams = '{"ComID":"TZ_STU_BBTJ_COM","PageID":"TZ_STU_BbTJ_STORE","OperateType":"QF","comParams":{}}';
        Ext.tzLoadAsync(tzParams, function (responseData) {
            root = responseData.root;
        });
        this.callParent([Ext.apply({
            root: root
        }, config)]);
    }
});
