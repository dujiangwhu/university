Ext.define('KitchenSink.view.trainStudentMg.studentModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'orgid'},
        {name: 'oprid'},
        {name: 'name'},
		{name: 'age'},
        {name: 'sex'},
		{name: 'stuQQ'},
        {name: 'phone'},
		{name: 'stuEmail'},
		{name: 'stuRemaindTimeCard'},
		{name: 'stuStatusDms'},
		{name: 'tzSexValue'}
	]
});
