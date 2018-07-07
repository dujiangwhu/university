Ext.define('KitchenSink.view.trainTeacherMg.reviewStore', {//项目列表获取数据
    extend: 'Ext.data.Store',
    alias: 'store.reviewStore',
    model: 'KitchenSink.view.trainTeacherMg.reviewModel',
	comID: 'TZ_PX_TEACHER_COM',
	pageID: 'TZ_PX_REVIEW_STD',
	tzStoreParams: '{"cfgSrhId":"TZ_PX_TEACHER_COM.TZ_PX_REVIEW_STD.PX_STU_REVIEW_V"}',
	autoLoad: true,
	pageSize:30 ,
	proxy: Ext.tzListProxy(),
	sorters: {
        property: 'TZ_REVIEW_ID',
        direction: 'asc'
    }
});