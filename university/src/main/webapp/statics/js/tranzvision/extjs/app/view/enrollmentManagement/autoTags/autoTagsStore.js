Ext.define('KitchenSink.view.enrollmentManagement.autoTags.autoTagsStore', {
    extend: 'Ext.data.Store',
    alias: 'store.autoTagsStore',
    model: 'KitchenSink.view.enrollmentManagement.autoTags.autoTagsModel',
	autoLoad: true,
	pageSize: 10,
	comID: 'TZ_AUTO_TAGS_COM',
	pageID: 'TZ_AUTO_TAGS_STD',
	tzStoreParams: '{"cfgSrhId":"TZ_AUTO_TAGS_COM.TZ_AUTO_TAGS_STD.TZ_ZDBQ_DFN_TBL","condition":{}}',
	proxy: Ext.tzListProxy()
});
