Ext.define('KitchenSink.view.trainOrgTimeCardMg.trainOrgTimeCardAddListModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'orgId'},
        {name: 'orgName'},
		{name: 'orgLxrName'},
		{name: 'orgLxrPhone'},
        {name: 'orgTimeCardAdd'},
		{name: 'orgTimeCardHaveAfter'},
		{name: 'orgTimeCardAddTime'},
		{name: 'orgTimeCardAddOprName'}
	]
});
