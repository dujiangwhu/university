Ext.define('KitchenSink.view.trainOrgTimeCardMg.trainOrgTimeCardAssignListModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'orgId'},
        {name: 'orgName'},
		{name: 'stuName'},
		{name: 'stuPhone'},
        {name: 'stuTimeCardAssign'},
		{name: 'stuTimeCardAssignAfter'},
		{name: 'stuTimeCardAssignTime'},
		{name: 'stuTimeCardAssignOprName'}
	]
});
