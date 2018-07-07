Ext.define('KitchenSink.view.studentBbTj.studentBbTjStore', {
    extend: 'Ext.data.Store',
    alias: 'store.studentBbTjStore',
    model: 'KitchenSink.view.studentBbTj.studentBbTjModel',
    comID: 'TZ_STU_BBTJ_COM',
	pageID: 'TZ_STU_BBTJ_STD',
	tzStoreParams: '',
    pageSize: 100,
    data: []
	//autoLoad: true,
    /*
	proxy: {
		type: 'ajax',
		 url: TzUniversityContextPath + "/dispatcher",
         reader:
         {
             type: 'json',
             totalProperty: 'comContent.age.total',
             rootProperty: 'comContent.age.root',
             messageProperty: 'state.errdesc'
         }
	}	
	*/
	//proxy: Ext.tzListProxy()
});
