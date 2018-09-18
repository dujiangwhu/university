Ext.define('KitchenSink.view.trainTeacherMg.reviewStore', {//项目列表获取数据
    extend: 'Ext.data.Store',
    alias: 'store.reviewStore',
    model: 'KitchenSink.view.trainTeacherMg.reviewModel',
	comID: 'PX_REVIEW_COM',
	pageID: 'PX_REVIEW_STD',
	//tzStoreParams: '{"cfgSrhId":"PX_REVIEW_COM.PX_REVIEW_STD.PX_STU_REVIEW_V"}',
	autoLoad: true,
	pageSize:30 ,
	proxy: Ext.tzListProxy()/*,
	sorters: {
        property: 'TZ_REVIEW_ID',
        direction: 'asc'
    }*/
});