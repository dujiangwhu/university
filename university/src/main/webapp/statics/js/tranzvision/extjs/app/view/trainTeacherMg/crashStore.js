Ext.define('KitchenSink.view.trainTeacherMg.crashStore', {//项目列表获取数据
    extend: 'Ext.data.Store',
    alias: 'store.crashStore',
    model: 'KitchenSink.view.trainTeacherMg.crashModel',
	comID: 'PX_CRASH_COM',
	pageID: 'PX_CRASH_STD',
	tzStoreParams: '{"cfgSrhId":"PX_CRASH_COM.PX_CRASH_STD.PX_TEA_TO_CRASH_V"}',
	autoLoad: true,
	pageSize:30 ,
	proxy: Ext.tzListProxy()/*,
	sorters: {
        property: 'TZ_REVIEW_ID',
        direction: 'asc'
    }*/
});