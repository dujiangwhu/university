Ext.define('KitchenSink.view.enrollmentManagement.bmb_lcpi.studentsStore', {
    extend: 'Ext.data.Store',
    alias: 'store.studentspiStore',
    model: 'KitchenSink.view.enrollmentManagement.bmb_lc.studentsModel',
	comID: 'TZ_BMGL_BMBPICI_COM',
	pageID: 'TZ_BMSH_FB_STD',
	//tzStoreParams: '{}',
	tzStoreParams:'',
	autoLoad: false,
	//autoLoad: true,
	pageSize: 1000,
	proxy: Ext.tzListProxy()	
});