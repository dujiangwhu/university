Ext.define('KitchenSink.view.trainOrgmgmt.trainOrgRoleModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name:'orgId'},
        {name: 'roleName'},
        {name: 'roleDesc'},
		{name: 'roleType'},
        {name: 'roleTypeDesc'}
	]
});
