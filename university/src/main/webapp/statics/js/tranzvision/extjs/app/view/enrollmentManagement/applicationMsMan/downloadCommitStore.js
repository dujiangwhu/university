Ext.define('KitchenSink.view.enrollmentManagement.applicationMsMan.downloadCommitStore', {
    extend: 'Ext.data.Store',
    alias: 'store.downloadCommitStore',
    model: 'KitchenSink.view.enrollmentManagement.applicationMsMan.downloadCommitModel',
    autoLoad: true,
    pageSize: 5,
    comID: 'TZ_BMGL_BMBSH_COM',
    pageID: 'TZ_BMGL_DOWN_STD',
    tzStoreParams: '{"cfgSrhId":"TZ_BMGL_BMBSH_COM.TZ_BMGL_DOWN_STD.TZ_GD_DOWN_V","condition":{"OPRID-operator": "01","OPRID-value": "'+ TranzvisionMeikecityAdvanced.Boot.loginUserId+'"}}',
    proxy: Ext.tzListProxy()
});