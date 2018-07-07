Ext.define('KitchenSink.view.enrollmentManagement.applicationBatch.classModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'classID'},
        {name: 'className'},
        {name: 'batchID'},
        {name: 'batchName'},
        {name: 'projectName'},
        {name: 'projectType'},
        {name: 'applyStatus'},
        {name: 'applicantsNumber'},
        {name: 'noauditNumber'},
        {name: 'reviewNumber'},
        {name: 'publishNumber'}
    ]
});
