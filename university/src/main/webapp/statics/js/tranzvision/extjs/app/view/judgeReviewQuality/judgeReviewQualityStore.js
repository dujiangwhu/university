Ext.define('KitchenSink.view.judgeReviewQuality.judgeReviewQualityStore', {
    extend: 'Ext.data.Store',
    alias: 'store.judgeReviewQualityStore',
    model: 'KitchenSink.view.judgeReviewQuality.judgeReviewQualityModel',
    autoLoad:true,
    comID: 'TZ_CLPW_PSZL_COM',
    pageID: 'TZ_CLPW_PSZL_STD',
    pageSize:50,
    tzStoreParams:'{"cfgSrhId": "TZ_CLPW_PSZL_COM.TZ_CLPW_PSZL_STD.TZ_CLPW_INFO_VW","condition":{"TZ_JG_ID-operator":"01","TZ_JG_ID-value":"'+Ext.tzOrgID+'"}}',
    proxy: Ext.tzListProxy(),
    remoteSort:true
});
