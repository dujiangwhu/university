Ext.define('KitchenSink.view.enrollmentManagement.applicationForm.stuModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'classID'},
        {name: 'oprID'},
        {name: 'appInsID'},
        {name: 'batchID'},
        {name: 'batchName'},
        {name: 'stuName'},
        {name: 'submitState'},
        {name: 'submitDate',type:'date'},
        {name: 'auditState'},
		{name: 'interviewResult'},
		{name: 'interviewResultX'},
        {name: 'colorType'},
        {name:'isMszg'},
        {name: 'interviewDate',type:'date',dateFormat:'Y-m-d'},
        {name: 'checkinTime',type : 'date',dateFormat : 'H:i'},
        {name: 'enrollStatus'},
        {name: 'moreInfo'},
        {name: 'clpsksFlag'}
    ]
});
