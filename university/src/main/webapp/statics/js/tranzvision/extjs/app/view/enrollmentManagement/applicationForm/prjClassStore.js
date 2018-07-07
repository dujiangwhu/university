Ext.define('KitchenSink.view.enrollmentManagement.applicationForm.prjClassStore', {
    extend: 'Ext.data.Store',
    alias: 'store.prjClassStore',
    model: 'KitchenSink.view.enrollmentManagement.applicationForm.prjClassModel',
	autoLoad: true,
	pageSize: 20,
	comID: 'TZ_BMGL_BMBSH_COM',
	pageID: 'TZ_BMGL_PRJ_STD',
	tzStoreParams: '{"cfgSrhId":"TZ_BMGL_BMBSH_COM.TZ_BMGL_PRJ_STD.TZ_PRJ_TYPE_T","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+ Ext.tzOrgID+'"}}',
	proxy: Ext.tzListProxy()
});
