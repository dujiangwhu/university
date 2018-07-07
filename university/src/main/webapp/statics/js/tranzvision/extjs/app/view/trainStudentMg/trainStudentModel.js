Ext.define('KitchenSink.view.trainStudentMg.trainStudentModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'orgid'},
        {name: 'oprid'},
        {name: 'stuName'},
		{name: 'age'},
        {name: 'sex'},
		{name: 'stuQQ'},
        {name: 'stuPhone'},
		{name: 'stuEmail'},
		{name: 'stuRemaindTimeCard'}
	]
});
