Ext.define('KitchenSink.view.enrollmentManagement.applicationForm.classSetBatchWinStore', {
    extend: 'Ext.data.Store',
    alias: 'store.classSetBatchWinStore',
    model: 'KitchenSink.view.enrollmentManagement.applicationForm.classSetBatchWinModel',
//    autoLoad: true,
    pageSize: 100,
    comID: 'TZ_BMGL_BMBSH_COM',
    pageID: 'TZ_SET_BATCH_STD',
    tzStoreParams: '',
    proxy: Ext.tzListProxy()
});

