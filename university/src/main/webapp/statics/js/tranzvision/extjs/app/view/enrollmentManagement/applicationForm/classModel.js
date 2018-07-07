Ext.define('KitchenSink.view.enrollmentManagement.applicationForm.classModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'classID'},
        {name: 'className'},
        {name: 'projectName'},
        {name: 'projectType'},
        {name: 'applicantsNumber'},
        {name: 'noauditNumber'},
        {name: 'reviewNumber'},
        {name: 'publishNumber'}
    ]
});
