Ext.define('KitchenSink.view.trainStudentMg.stuCourseStore', {
    extend: 'Ext.data.Store',
    alias: 'store.stuCourseStore',
    model: 'KitchenSink.view.trainStudentMg.stuCourseModel',
	autoLoad: true,
	pageSize: 10,
	comID: 'PX_STU_COURSE_COM',
	pageID: 'PX_STU_COURSE_STD',
	tzStoreParams: '{"cfgSrhId":"PX_STU_COURSE_COM.PX_STU_COURSE_STD.PX_STU_COURSE_V"}',
	/*proxy: {
		type: 'ajax',
		url : '/tranzvision/kitchensink/app/view/security/com/coms.json',
		reader: {
			type: 'json',
			rootProperty: ''
		}
	}*/
	proxy: Ext.tzListProxy()
});
