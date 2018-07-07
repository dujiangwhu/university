Ext.define('KitchenSink.view.template.sitetemplate.columnStore', {
    extend: 'Ext.data.Store',
    alias: 'store.columnStore',
    model: 'KitchenSink.view.template.sitetemplate.columnModel',
    comID: 'TZ_GD_ZDMB_COM',
	pageID: 'TZ_GD_ZDMB_STD',
	tzStoreParams: '',
    pageSize: 10,
	//autoLoad: true,
    /*
	proxy: {
				type: 'ajax',
				url : '/tranzvision/kitchensink/app/view/template/sitetemplate/columns.json',
				reader: {
					type: 'json',
					totalProperty: 'total',
					rootProperty: 'root'
				}
			}*/
    proxy: Ext.tzListProxy()
});