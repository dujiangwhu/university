Ext.define('KitchenSink.view.trainTeacherMg.teacherStore', {//项目列表获取数据
    extend: 'Ext.data.Store',
    alias: 'store.teacherStore',
    model: 'KitchenSink.view.trainTeacherMg.teacherModel',
	comID: 'PX_TEACHER_COM',
	pageID: 'PX_TEACHER_STD',
	tzStoreParams: '{"cfgSrhId":"PX_TEACHER_COM.PX_TEACHER_STD.PX_TEACHER_V"}',
	autoLoad: true,
	pageSize:30 ,
	proxy: Ext.tzListProxy(),
	sorters: {
        property: 'oprid',
        direction: 'asc'
    }
});