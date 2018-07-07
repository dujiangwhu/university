Ext.define('KitchenSink.view.enrollmentManagement.applicationMsMan.mszgInfoStore', {
    extend: 'Ext.data.Store',
    alias: 'store.mszgInfoStore',
    model: 'KitchenSink.view.enrollmentManagement.applicationMsMan.mszgInfoModel',
	autoLoad: true,
    pageSize: 30,
    comID: 'TZ_BMGL_BMBSH_COM',
    pageID: 'TZ_BMGL_MSZG_STD',
    tzStoreParams:'{"cfgSrhId": "TZ_BMGL_BMBSH_COM.TZ_BMGL_MSZG_STD.TZ_MSZG_MAN_V","condition":{}}',
    proxy: Ext.tzListProxy(),
    remoteSort:true
});
