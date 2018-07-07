Ext.define('KitchenSink.view.trainCourseMg.trainCourseStore', {
    extend: 'Ext.data.Store',
    alias: 'store.comStore',
    model: 'KitchenSink.view.trainCourseMg.trainCourseModel',
	autoLoad: true,
	pageSize: 10,
	comID: 'PX_COURSE_COM',
	pageID: 'PX_COURSE_STD',
	tzStoreParams: '{"cfgSrhId":"PX_COURSE_COM.PX_COURSE_STD.PX_COURSE_V","condition":{}}',
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
