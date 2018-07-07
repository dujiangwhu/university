Ext.define('KitchenSink.view.enrollmentManagement.applicationMsMan.msjgInfoStore', {
    extend: 'Ext.data.Store',
    alias: 'store.mszgInfoStore',
    model: 'KitchenSink.view.enrollmentManagement.applicationMsMan.msjgInfoModel',
	autoLoad: true,
    pageSize: 30,
    comID: 'TZ_BMGL_BMBSH_COM',
    pageID: 'TZ_BMGL_MSJG_STD',
    tzStoreParams:'{"cfgSrhId": "TZ_BMGL_BMBSH_COM.TZ_BMGL_MSJG_STD.TZ_MSJG_MAN_V","condition":{}}',
    proxy: Ext.tzListProxy(),
    remoteSort:true
});
