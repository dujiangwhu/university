Ext.define('KitchenSink.view.judgesManagement.judgeAccount.judgeCLAccStore', {
    extend: 'Ext.data.Store',
    alias: 'store.judgeCLAccStore',
    model: 'KitchenSink.view.judgesManagement.judgeAccount.judgeAccModel',
	autoLoad: true,
	pageSize: 30,
	
	comID: 'TZ_JUDGE_CLACC_COM',
	pageID: 'TZ_JUDCLACC_GL_STD',
	// tzStoreParams: '{"cfgSrhId":"TZ_JUDGE_CLACC_COM.TZ_JUDCLACC_GL_STD.TZ_PWZHGL_VW","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+ Ext.tzOrgID+'"}}',
	constructor: function (jugTypeId) {
        this.tzStoreParams = '{"cfgSrhId":"TZ_JUDGE_CLACC_COM.TZ_JUDCLACC_GL_STD.TZ_PWZHGL_VW","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+ Ext.tzOrgID+'","TZ_JUGTYP_ID-operator":"01","TZ_JUGTYP_ID-value":"'+jugTypeId+'"}}';
        this.callParent();
    },
	proxy: Ext.tzListProxy()
});
