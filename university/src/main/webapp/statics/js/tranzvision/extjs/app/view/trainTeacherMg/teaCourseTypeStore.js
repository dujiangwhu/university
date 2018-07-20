Ext.define('KitchenSink.view.trainTeacherMg.teaCourseTypeStore', {//项目列表获取数据
    extend: 'Ext.data.Store',
    alias: 'store.teaCourseTypeStore',
    model: 'KitchenSink.view.trainTeacherMg.teaCourseTypeModel',
	comID: 'PX_TEACHER_COM',
	pageID: 'TEA_COU_TYPE_STD',
	tzStoreParams: '{"cfgSrhId":"PX_TEACHER_COM.TEA_COU_TYPE_STD.PX_TEA_COURSE_V"}',
	autoLoad: true,
	pageSize:30 ,
	proxy: Ext.tzListProxy(),
	sorters: {
        property: 'oprid',
        direction: 'asc'
    }
});