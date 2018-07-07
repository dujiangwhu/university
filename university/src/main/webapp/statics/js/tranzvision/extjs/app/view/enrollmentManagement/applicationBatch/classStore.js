Ext.define('KitchenSink.view.enrollmentManagement.applicationBatch.classStore', {
    extend: 'Ext.data.Store',
    alias: 'store.appBatchClassStore',
    model: 'KitchenSink.view.enrollmentManagement.applicationBatch.classModel',
	autoLoad: true,
    pageSize: 30,
    comID: 'TZ_BMGL_BMBPICI_COM',
    pageID: 'TZ_BMGL_CLASS_STD',
//    tzStoreParams:'{"cfgSrhId": "TZ_BMGL_BMBPICI_COM.TZ_BMGL_CLASS_STD.TZ_CLASS_BATCH_V","condition":{"TZ_DLZH_ID-operator": "01","TZ_DLZH_ID-value": "'+TranzvisionMeikecityAdvanced.Boot.loginUserId+'","TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+Ext.tzOrgID+'","TZ_IS_APP_OPEN-operator":"01","TZ_IS_APP_OPEN-value":["Y"]}}',
    tzStoreParams:'{"cfgSrhId": "TZ_BMGL_BMBPICI_COM.TZ_BMGL_CLASS_STD.TZ_CLASS_BATCH_V","condition":{"TZ_DLZH_ID-operator": "01","TZ_DLZH_ID-value": "'+TranzvisionMeikecityAdvanced.Boot.loginUserId+'","TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+Ext.tzOrgID+'","TZ_IS_APP_OPEN-operator":"01","TZ_IS_APP_OPEN-value":"Y"}}',
    proxy: Ext.tzListProxy()
});
