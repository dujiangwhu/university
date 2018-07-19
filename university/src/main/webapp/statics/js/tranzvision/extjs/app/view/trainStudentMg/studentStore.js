Ext.define('KitchenSink.view.trainStudentMg.studentStore', {//项目列表获取数据
    extend: 'Ext.data.Store',
    alias: 'store.studentStore',
    model: 'KitchenSink.view.trainStudentMg.studentModel',
	comID: 'PX_STU_COM',
	pageID: 'PX_STU_STD',
	tzStoreParams: '{"cfgSrhId":"PX_STU_COM.PX_STU_STD.PX_STUDENT_V"}',
	autoLoad: true,
	pageSize:30 ,
	proxy: Ext.tzListProxy(),
	sorters: {
        property: 'oprid',
        direction: 'asc'
    }
});