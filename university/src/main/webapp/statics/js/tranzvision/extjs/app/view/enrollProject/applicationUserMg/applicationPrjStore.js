Ext.define('KitchenSink.view.enrollProject.applicationUserMg.applicationPrjStore', {
    extend: 'Ext.data.Store',
    alias: 'store.applicationPrjStore',
    model: 'KitchenSink.view.enrollProject.applicationUserMg.applicationPrjModel',
	autoLoad: true,
	pageSize: 20,
	comID: 'TZ_BMGL_APPL_COM',
	pageID: 'TZ_BMGL_PRJ_STD',
	tzStoreParams: '{"cfgSrhId":"TZ_BMGL_APPL_COM.TZ_BMGL_PRJ_STD.TZ_PRJ_TYPE_T","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+ Ext.tzOrgID+'"}}',
	proxy: Ext.tzListProxy()
});
