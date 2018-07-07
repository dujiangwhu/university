Ext.define('KitchenSink.view.audienceManagement.newAudWindowStore', {
    extend: 'Ext.data.Store',
    alias: 'store.newAudWindowStore',
    model: 'KitchenSink.view.audienceManagement.newAudWindowModel',
	autoLoad: false,
	pageSize: 10,
	comID: 'TZ_AUD_COM',
	pageID: 'TZ_AUD_PANEL_STD',
	tzStoreParams: '{"cfgSrhId":"TZ_AUD_COM.TZ_AUD_PANEL_STD.PS_TZ_AUDCY_VW","condition":{}}',
	proxy: Ext.tzListProxy()
});
