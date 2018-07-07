Ext.define('KitchenSink.view.trainOrgmgmt.trainOrgRoleStore', {
    extend: 'Ext.data.Store',
    alias: 'store.trainOrgRoleStore',
    model: 'KitchenSink.view.trainOrgmgmt.trainOrgRoleModel',
	autoLoad: false,
    pageSize: 10,
    comID: 'TZ_PX_ORGGL_COM',
    pageID: 'TZ_PX_ORGDEF_STD',
    tzStoreParams: '{"orgId":"","queryType":"ROLE}',
    proxy: Ext.tzListProxy()
});
