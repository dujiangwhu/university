Ext.define('KitchenSink.view.trainOrgTimeCardMg.trainOrgListStore', {
    extend: 'Ext.data.Store',
    alias: 'store.trainOrgListStore',
    model: 'KitchenSink.view.trainOrgTimeCardMg.trainOrgListModel',
	autoLoad: true,
	pageSize: 10,
	comID: 'TZ_PX_KSGL_COM',
	pageID: 'TZ_PX_KSGL_STD',
	tzStoreParams: '{"cfgSrhId": "TZ_PX_KSGL_COM.TZ_PX_KSGL_STD.TZ_PX_JG_VW"}',
	proxy: Ext.tzListProxy()
});
