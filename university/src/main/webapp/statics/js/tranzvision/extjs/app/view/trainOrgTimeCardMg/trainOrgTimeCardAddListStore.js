Ext.define('KitchenSink.view.trainOrgTimeCardMg.trainOrgTimeCardAddListStore', {
    extend: 'Ext.data.Store',
    alias: 'store.trainOrgListStore',
    model: 'KitchenSink.view.trainOrgTimeCardMg.trainOrgTimeCardAddListModel',
	autoLoad: true,
	pageSize: 10,
	comID: 'TZ_PX_KS_DGCX_COM',
	pageID: 'TZ_KS_ADD_HIS_STD',
	tzStoreParams: '{"cfgSrhId": "TZ_PX_KS_DGCX_COM.TZ_KS_ADD_HIS_STD.PX_JG_KS_ORDER_VW"}',
	proxy: Ext.tzListProxy()
});
