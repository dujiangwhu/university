Ext.define('KitchenSink.view.trainCourseMg.attachStore', {
    extend: 'Ext.data.Store',
    alias: 'store.attachStore',
    model: 'KitchenSink.view.trainCourseMg.attachModel',
	comID: 'PX_COURSE_COM',
	pageID: 'PX_COURSE_ATT_STD',
	tzStoreParams:  '{"tzCourseId":""}',
	pageSize: 5,
	proxy: Ext.tzListProxy()
});
