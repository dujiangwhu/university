Ext.define('KitchenSink.view.payment.showPayInfoStore', {
    extend: 'Ext.data.Store',
    alias: 'store.showPayInfoStore',
    model: 'KitchenSink.view.payment.showPayInfoModel',
    autoLoad: true,
    pageSize: 10,
    comID:'TZ_ZFCKJGL_COM',
    pageID:'TZ_SEARCH_DETAIL',
    tzStoreParams: '{"ComID":"TZ_ZFCKJGL_COM","PageID":"TZ_SEARCH_DETAIL","OperateType":"QF"}',
    proxy: Ext.tzListProxy()
})
