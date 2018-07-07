Ext.define('KitchenSink.view.trainOrgmgmt.trainOrgMemListStore', {
    extend: 'Ext.data.Store',
    alias: 'store.trainOrgMemListStore',
    model: 'KitchenSink.view.trainOrgmgmt.trainUserModel',
	pageSize: 10,
	comID: 'TZ_PX_ORGGL_COM',
	pageID: 'TZ_PX_ORGDEF_STD',
	tzStoreParams: '{"queryType":"USER","cfgSrhId":"TZ_PX_ORGGL_COM.TZ_PX_ORGDEF_STD.TZ_PXJG_USER_VW","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+ Ext.orgId+'"}}',
	//tzStoreParams: '{"orgId":"","queryType":"USER"}',
	proxy: Ext.tzListProxy()
});
