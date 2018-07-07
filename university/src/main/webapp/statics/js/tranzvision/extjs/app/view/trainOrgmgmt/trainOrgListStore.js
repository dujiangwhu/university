Ext.define('KitchenSink.view.trainOrgmgmt.trainOrgListStore', {
    extend: 'Ext.data.Store',
    alias: 'store.trainOrgListStore',
    model: 'KitchenSink.view.trainOrgmgmt.trainUserModel',
	autoLoad: true,
	pageSize: 10,
	comID: 'TZ_PX_ORGGL_COM',
	pageID: 'TZ_PX_ORGGL_STD',
	tzStoreParams: '{"cfgSrhId": "TZ_PX_ORGGL_COM.TZ_PX_ORGGL_STD.TZ_PX_JG_VW"}',
	proxy: Ext.tzListProxy()
});
