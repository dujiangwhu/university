Ext.define('KitchenSink.view.enrollmentManagement.applicationMsMan.msjgXnfoStore', {
    extend: 'Ext.data.Store',
    alias: 'store.mszgXInfoStore',
    model: 'KitchenSink.view.enrollmentManagement.applicationMsMan.msjgInfoModel',
	autoLoad: true,
    pageSize: 30,
    comID: 'TZ_BMGL_BMBSH_COM',
    pageID: 'TZ_BMGL_XMSJG_STD',
    tzStoreParams:'{"cfgSrhId": "TZ_BMGL_BMBSH_COM.TZ_BMGL_XMSJG_STD.TZ_XMSJG_MAN_V","condition":{}}',
    proxy: Ext.tzListProxy(),
    remoteSort:true
});
