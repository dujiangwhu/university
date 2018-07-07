Ext.define('KitchenSink.view.enrollmentManagement.applicationBatch.stuStore', {
    extend: 'Ext.data.Store',
    alias: 'store.appBatchStuStore',
    model: 'KitchenSink.view.enrollmentManagement.applicationForm.stuModel',
    autoLoad: false,
    pageSize: 500,
    comID: 'TZ_BMGL_BMBPICI_COM',
    pageID: 'TZ_BMGL_STU_STD',
    tzStoreParams: '{}',
    proxy: Ext.tzListProxy(),
	remoteSort:true
});

