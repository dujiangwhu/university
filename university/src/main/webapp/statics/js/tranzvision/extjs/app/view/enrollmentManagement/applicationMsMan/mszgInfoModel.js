Ext.define('KitchenSink.view.enrollmentManagement.applicationMsMan.mszgInfoModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'mshId'},
        {name: 'name'},
        {name: 'isMszg'},
        {name: 'msBatch'},
        {name: 'mszgFlag'},
        {name: 'msDate',  type : 'date',dateFormat : 'Y-m-d'},
        {name: 'baTime',  type : 'date',dateFormat : 'H:i'},
        {name: 'msAddress'},
        {name: 'msrcFlag'},
        {name:'mshfFlag'}
    ]
});
