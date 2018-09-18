Ext.define('KitchenSink.view.trainStudentMg.courseTimeStore', {//项目列表获取数据
    extend: 'Ext.data.Store',
    alias: 'store.courseTimeStore',
    model: 'KitchenSink.view.trainStudentMg.courseTimeModel',
	comID: 'PX_STU_COM',
	pageID: 'PX_COU_TIME_STD',
	tzStoreParams: '{"cfgSrhId":"PX_STU_COM.PX_COU_TIME_STD.PX_JG_KS_LOG_V"}',
	autoLoad: true,
	pageSize:30 ,
	proxy: Ext.tzListProxy()/*,
	sorters: {
        property: 'TZ_REVIEW_ID',
    }*/
});