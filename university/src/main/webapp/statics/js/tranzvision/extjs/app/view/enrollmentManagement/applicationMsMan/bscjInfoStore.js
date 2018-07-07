Ext.define('KitchenSink.view.enrollmentManagement.applicationMsMan.bscjInfoStore', {
    extend: 'Ext.data.Store',
    alias: 'store.bscjInfoStore',
    model: 'KitchenSink.view.enrollmentManagement.applicationMsMan.bscjInfoModel',
	autoLoad: true,
    pageSize: 30,
    comID: 'TZ_BMGL_BMBSH_COM',
    pageID: 'TZ_BMGL_BSCJ_STD',
    tzStoreParams:'{"cfgSrhId": "TZ_BMGL_BMBSH_COM.TZ_BMGL_BSCJ_STD.TZ_BSCJ_MAN_V","condition":{}}',
    proxy: Ext.tzListProxy(),
    remoteSort:true
});
