Ext.define('KitchenSink.view.enrollmentManagement.applicationBatch.prjClassStore', {
    extend: 'Ext.data.Store',
    alias: 'store.prjClassBStore',
    model: 'KitchenSink.view.enrollmentManagement.applicationBatch.prjClassModel',
	autoLoad: true,
	pageSize: 20,
	comID: 'TZ_BMGL_BMBPICI_COM',
	pageID: 'TZ_BMGL_PRJ_STD',
	tzStoreParams: '{"cfgSrhId":"TZ_BMGL_BMBPICI_COM.TZ_BMGL_PRJ_STD.TZ_PRJ_TYPE_T","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+ Ext.tzOrgID+'"}}',
	proxy: Ext.tzListProxy()
});
