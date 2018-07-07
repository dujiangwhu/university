Ext.define('KitchenSink.view.trainCourseMg.trainCourseTypeStore', {
    extend: 'Ext.data.Store',
    alias: 'store.trainCourseTypeStore',
    model: 'KitchenSink.view.trainCourseMg.trainCourseTypeModel',
	autoLoad: true,
	pageSize: 10,
	comID: 'PX_COURSE_TYPE_COM',
	pageID: 'PX_COURSE_TYPE_STD',
	tzStoreParams: '{"cfgSrhId": "PX_COURSE_TYPE_COM.PX_COURSE_TYPE_STD.PX_COURSE_TYPE_V"}',
	proxy: Ext.tzListProxy()
});
