Ext.define('KitchenSink.view.trainTeacherMg.scoreStore', {//项目列表获取数据
    extend: 'Ext.data.Store',
    alias: 'store.scoreStore',
    model: 'KitchenSink.view.trainTeacherMg.scoreModel',
	comID: 'PX_SCORE_COM',
	pageID: 'PX_SCORE_STD',
	tzStoreParams: '{"cfgSrhId":"PX_SCORE_COM.PX_SCORE_STD.PX_SCORE_LOG_V"}',
	autoLoad: true,
	pageSize:30 ,
	proxy: Ext.tzListProxy()/*,
	sorters: {
        property: 'TZ_REVIEW_ID',
        direction: 'asc'
    }*/
});