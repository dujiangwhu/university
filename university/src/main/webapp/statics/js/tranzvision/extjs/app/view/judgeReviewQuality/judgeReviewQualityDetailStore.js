Ext.define('KitchenSink.view.judgeReviewQuality.judgeReviewQualityDetailStore', {
    extend: 'Ext.data.Store',
    alias: 'store.judgeReviewQualityDetailStore',
    model: 'KitchenSink.view.judgeReviewQuality.judgeReviewQualityDetailModel',
    autoLoad:true,
    comID: 'TZ_CLPW_PSZL_COM',
    pageID: 'TZ_PSZL_DTL_STD',
    pageSize:10,
    tzStoreParams:'{}',
    proxy: Ext.tzListProxy(),
    remoteSort:true
});
