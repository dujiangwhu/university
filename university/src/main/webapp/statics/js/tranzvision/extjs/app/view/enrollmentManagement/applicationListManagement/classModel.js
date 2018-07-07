Ext.define('KitchenSink.view.enrollmentManagement.applicationListManagement.classModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'classID',type:'number'},
        {name: 'className'},
        {name: 'batchID'},
        {name: 'batchName'},
        {name: 'stuNum'}
    ]
});
