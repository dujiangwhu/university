Ext.define('KitchenSink.view.materialsReview.materialsReview.materialsReviewScheduleAppJudgeStore', {
    extend: 'Ext.data.Store',
    alias: 'store.materialsReviewScheduleAppJudgeStore',
    model: 'KitchenSink.view.materialsReview.materialsReview.materialsReviewScheduleAppJudgeModel',
    autoLoad:false,
    comID: 'TZ_REVIEW_CL_COM',
    pageID: 'TZ_CLPS_VWJUD_STD',
    pageSize:0,
    tzStoreParams:'',
    proxy: Ext.tzListProxy()
});
