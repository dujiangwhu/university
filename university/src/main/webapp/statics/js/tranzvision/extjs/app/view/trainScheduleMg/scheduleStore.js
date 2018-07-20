Ext.define('KitchenSink.view.trainScheduleMg.scheduleStore', {//项目列表获取数据
    extend: 'Ext.data.Store',
    alias: 'store.scheduleStore',
    model: 'KitchenSink.view.trainScheduleMg.scheduleModel',
	comID: 'PX_SCHEDULE_COM',
	pageID: 'PX_SCHEDULE_STD',
	tzStoreParams: '{"cfgSrhId":"PX_SCHEDULE_COM.PX_SCHEDULE_STD.PX_TEA_SCHEDULE_V"}',
	autoLoad: true,
	pageSize:30 ,
	proxy: Ext.tzListProxy()/*,
	sorters: {
        property: 'oprid',
        direction: 'asc'
    }*/
});