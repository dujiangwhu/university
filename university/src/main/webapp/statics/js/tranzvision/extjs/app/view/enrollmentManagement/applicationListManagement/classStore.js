Ext.define('KitchenSink.view.enrollmentManagement.applicationListManagement.classStore', {
    extend: 'Ext.data.Store',
    alias: 'store.appBatchClassStore',
    model: 'KitchenSink.view.enrollmentManagement.applicationListManagement.classModel',
	autoLoad: true,
    pageSize: 30,
    comID: 'TZ_PS_MGR_COM',
    pageID: 'TZ_PS_MGR_STD',   
    tzStoreParams:'{"cfgSrhId": "TZ_PS_MGR_COM.TZ_PS_MGR_STD.PS_TZ_CLPS_MGR_VW","condition":{"TZ_DLZH_ID-operator": "01","TZ_DLZH_ID-value": "'+TranzvisionMeikecityAdvanced.Boot.loginUserId+'","TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+Ext.tzOrgID+'"}}',
    proxy: Ext.tzListProxy(),
    sorters:[
        {
            direction:'DESC',
            property:'classID'
        },{
            direction:'ASC',
            property:'batchID'
        }
    ]
});
