Ext.define('KitchenSink.view.template.survey.report.PinShuBB.PinShuBBAnswerTuBiaoChartStore', {
    extend: 'Ext.data.Store',
    alias: 'store.PinShuBBAnswerTuBiaoChartStore',
    model: 'KitchenSink.view.template.survey.report.PinShuBB.PinShuBBAnswerTuBiaoChartModel',
    autoLoad:false,
    comID: 'TZ_ZXDC_PSBB_COM',
    pageID: 'TZ_ZXDC_PSBB_W_STD',
    tzStoreParams: '{}',
    proxy: Ext.tzListProxy()
});
