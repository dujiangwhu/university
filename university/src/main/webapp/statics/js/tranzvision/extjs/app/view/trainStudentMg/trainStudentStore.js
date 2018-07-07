Ext.define('KitchenSink.view.trainStudentMg.trainStudentStore', {
    extend: 'Ext.data.Store',
    alias: 'store.trainStudentStore',
    model: 'KitchenSink.view.trainStudentMg.trainStudentModel',
	autoLoad: true,
	pageSize: 10,
	comID: 'TZ_PX_STU_COM',
	pageID: 'TZ_PX_STU_STD',
	//tzStoreParams: '{"cfgSrhId": "TZ_PX_STU_COM.TZ_PX_STU_STD.PX_STUDENT_VW"}',
	tzStoreParams: '{"cfgSrhId":"TZ_PX_STU_COM.TZ_PX_STU_STD.PX_STUDENT_VW","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+ Ext.orgId+'"}}',
	proxy: Ext.tzListProxy()
});
