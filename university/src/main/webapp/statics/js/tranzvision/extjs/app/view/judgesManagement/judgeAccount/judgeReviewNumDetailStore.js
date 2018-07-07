Ext.define('KitchenSink.view.judgesManagement.judgeAccount.judgeReviewNumDetailStore', {
    extend: 'Ext.data.Store',
    alias: 'store.judgeReviewNumDetailStore',
    model: 'KitchenSink.view.judgesManagement.judgeAccount.judgeReviewNumDetailModel',
    autoLoad: true,
    pageSize: 20,
    comID: 'TZ_JUDGE_CLACC_COM',
    pageID: 'TZ_JUDGE_PSNUM_STD',
    tzStoreParams: '',
    proxy: Ext.tzListProxy()
});
