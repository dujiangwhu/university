Ext.define('KitchenSink.view.trainScheduleMg.stuScheduleStore', {//项目列表获取数据
    extend: 'Ext.data.Store',
    alias: 'store.scheduleStore',
    model: 'KitchenSink.view.trainScheduleMg.stuScheduleModel',
	comID: 'TZ_PX_SCHEDULE_COM',
	pageID: 'TZ_PX_STUSCH_STD',
	tzStoreParams: '{"cfgSrhId":"TZ_PX_SCHEDULE_COM.TZ_PX_STUSCH_STD.PX_STU_COURSE_V"}',
	autoLoad: true,
	pageSize:30 ,
	proxy: Ext.tzListProxy()/*,
	sorters: {
        property: 'oprid',
        direction: 'asc'
    }*/
});