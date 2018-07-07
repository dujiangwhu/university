Ext.define('KitchenSink.view.ksShujuDr.ksShujuDrBatchInfoStore', {
    extend: 'Ext.data.Store',
    alias: 'store.ksShujuDrBatchInfoStore',
    model: 'KitchenSink.view.ksShujuDr.ksShujuDrBatchInfoModel',
	autoLoad: false,
	pageSize: 500,
	comID: 'TZ_DRBATLIST_COM',
	pageID: 'TZ_DRBATINFO_STD',
	tzStoreParams: '',
	proxy: Ext.tzListProxy()
});