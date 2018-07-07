Ext.define('KitchenSink.view.enrollProject.applicationUserMg.appUserMgStore', {
    extend: 'Ext.data.Store',
    alias: 'store.userMgStore',
    model: 'KitchenSink.view.enrollProject.applicationUserMg.appUserMgModel',
	autoLoad: true,
	pageSize: 500,
	comID: 'TZ_BMGL_APPL_COM',
	pageID: 'TZ_UM_USERMG_STD',
	tzStoreParams: '{"cfgSrhId":"TZ_BMGL_APPL_COM.TZ_UM_USERMG_STD.TZ_APP_USER_VW","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+ Ext.tzOrgID+'"}}',
	proxy: Ext.tzListProxy()	
});
