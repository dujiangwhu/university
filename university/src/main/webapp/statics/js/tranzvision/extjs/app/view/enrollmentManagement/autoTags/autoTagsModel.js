Ext.define('KitchenSink.view.enrollmentManagement.autoTags.autoTagsModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'tagId'},
        {name: 'tagName'},
        {name: 'tagDescr'},
        {name: 'isValid'},
        {name: 'javaCls'},
        {name: 'isViewClPwd'}
	]
});
