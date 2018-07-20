Ext.define('KitchenSink.view.trainTeacherMg.focusStore', {//项目列表获取数据
    extend: 'Ext.data.Store',
    alias: 'store.focusStore',
    model: 'KitchenSink.view.trainTeacherMg.focusModel',
	comID: 'PX_FOCUS_COM',
	pageID: 'PX_FOCUS_STD',
	//tzStoreParams: '{"cfgSrhId":"TZ_PX_TEACHER_COM.TZ_PX_FOCUS_STD.PX_STU_FOCUS_T","condition":{"STU_OPRID-operator":"01","STU_OPRID-value":"TZ_14072"}}',
	autoLoad: true,
	pageSize:30 ,
	proxy: Ext.tzListProxy()/*,
	sorters: {
        property: 'TZ_REVIEW_ID',
        direction: 'asc'
    }*/
});