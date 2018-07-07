Ext.define('KitchenSink.view.trainOrgTimeCardMg.trainOrgListModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'orgId'},
        {name: 'orgName'},
		{name: 'orgLxrName'},
		{name: 'orgLxrPhone'},
        {name: 'orgTimeCardHave'},
		{name: 'orgTimeCardAssign'}
	]
});
