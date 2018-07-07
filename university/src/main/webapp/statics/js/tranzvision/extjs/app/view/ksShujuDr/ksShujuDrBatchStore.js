Ext.define('KitchenSink.view.ksShujuDr.ksShujuDrBatchStore', {
    extend: 'Ext.data.Store',
    alias: 'store.ksShujuDrBatchStore',
    model: 'KitchenSink.view.ksShujuDr.ksShujuDrBatchModel',
	autoLoad: true,
	pageSize: 10,
	comID: 'TZ_DRBATLIST_COM',
	pageID: 'TZ_DRBATLIST_STD',
	tzStoreParams: '{"cfgSrhId":"TZ_DRBATLIST_COM.TZ_DRBATLIST_STD.PS_TZ_DRBATCH_VW","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+Ext.tzOrgID+'"}}',
	proxy: Ext.tzListProxy()
});