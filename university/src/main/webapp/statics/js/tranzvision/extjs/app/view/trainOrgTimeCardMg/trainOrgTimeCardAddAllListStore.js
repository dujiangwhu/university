Ext.define('KitchenSink.view.trainOrgTimeCardMg.trainOrgTimeCardAddAllListStore', {
    extend: 'Ext.data.Store',
    alias: 'store.trainOrgListStore',
    model: 'KitchenSink.view.trainOrgTimeCardMg.trainOrgTimeCardAddListModel',
	autoLoad: true,
	pageSize: 10,
	comID: 'TZ_KS_ADD_SRCH_COM',
	pageID: 'TZ_KS_ADD_SRCH_STD',
	tzStoreParams: '{"cfgSrhId": "TZ_KS_ADD_SRCH_COM.TZ_KS_ADD_SRCH_STD.PX_JG_KS_ORDER_VW"}',
	proxy: Ext.tzListProxy()
});
