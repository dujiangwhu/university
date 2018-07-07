Ext.define('KitchenSink.view.enrollProject.userMg.siteUserMgStore', {
    extend: 'Ext.data.Store',
    alias: 'store.siteUserMgStore',
    model: 'KitchenSink.view.enrollProject.userMg.siteUserMgModel',
	autoLoad: true,
	pageSize: 20,
	comID: 'TZ_UM_USERMG_COM',
	pageID: 'TZ_UM_USER_STD',
	tzStoreParams: '{"cfgSrhId":"TZ_UM_USERMG_COM.TZ_UM_USER_STD.TZ_WEBSIT_SET_VW","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+ Ext.tzOrgID+'"}}',
	proxy: Ext.tzListProxy()
});
