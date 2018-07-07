Ext.define('KitchenSink.view.trainTeacherMg.teacherStore', {//项目列表获取数据
    extend: 'Ext.data.Store',
    alias: 'store.teacherStore',
    model: 'KitchenSink.view.trainTeacherMg.teacherModel',
	comID: 'TZ_PX_TEACHER_COM',
	pageID: 'TZ_PX_TEACHER_STD',
	tzStoreParams: '{"cfgSrhId":"TZ_PX_TEACHER_COM.TZ_PX_TEACHER_STD.PX_TEACHER_VW"}',
	autoLoad: true,
	pageSize:30 ,
	proxy: Ext.tzListProxy(),
	sorters: {
        property: 'oprid',
        direction: 'asc'
    }
});