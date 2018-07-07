Ext.define('KitchenSink.view.enrollmentManagement.applicationMsMan.lqjgInfoStore', {
    extend: 'Ext.data.Store',
    alias: 'store.lqjgInfoStore',
    model: 'KitchenSink.view.enrollmentManagement.applicationMsMan.lqjgInfoModel',
	autoLoad: true,
    pageSize: 30,
    comID: 'TZ_BMGL_BMBSH_COM',
    pageID: 'TZ_BMGL_LQJG_STD',
    tzStoreParams:'{"cfgSrhId": "TZ_BMGL_BMBSH_COM.TZ_BMGL_LQJG_STD.TZ_LQJG_MAN_V","condition":{}}',
    proxy: Ext.tzListProxy(),
    remoteSort:true
});
