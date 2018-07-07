Ext.define('KitchenSink.view.payment.payInfoStore', {
    extend: 'Ext.data.Store',
    alias: 'store.payInfoStore',
    model: 'KitchenSink.view.payment.payInfoModel',
    autoLoad: true,
    pageSize: 10,
    comID:'TZ_ZFCKJGL_COM',
    pageID:'TZ_ZFCKJGL_STD',
    tzStoreParams: '{"cfgSrhId":"TZ_ZFCKJGL_COM.TZ_ZFCKJGL_STD","condition":{"TZ_JG_ID-operator": "01"}}',
    proxy: Ext.tzListProxy()

})
