Ext.define('KitchenSink.view.trainOrgTimeCardMg.trainOrgTimeCardAssignListStore', {
    extend: 'Ext.data.Store',
    alias: 'store.trainOrgListStore',
    model: 'KitchenSink.view.trainOrgTimeCardMg.trainOrgTimeCardAssignListModel',
	autoLoad: true,
	pageSize: 10,
	comID: 'TZ_PX_KS_FPCX_COM',
	pageID: 'TZ_KS_ASGN_HIS_STD',
	tzStoreParams: '{"cfgSrhId": "TZ_PX_KS_FPCX_COM.TZ_KS_ASGN_HIS_STD.PX_JG_KS_ASSIGN_VW"}',
	proxy: Ext.tzListProxy()
});
