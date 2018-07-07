Ext.define('KitchenSink.view.trainStudentMg.studentInfoMg.courseTimeStore', {//项目列表获取数据
    extend: 'Ext.data.Store',
    alias: 'store.courseTimeStore',
    model: 'KitchenSink.view.trainStudentMg.studentInfoMg.courseTimeModel',
	comID: 'PX_JG_KS_COM',
	pageID: 'PX_JG_KS_STD',
	tzStoreParams: '{"cfgSrhId":"TZ_COURSE_TIME_COM.TZ_COURSE_TIME_STD.PX_JG_KS_LOG_T"}',
	autoLoad: true,
	pageSize:30 ,
	proxy: Ext.tzListProxy()/*,
	sorters: {
        property: 'TZ_REVIEW_ID',
        direction: 'asc'
    }*/
});