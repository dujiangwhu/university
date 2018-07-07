Ext.define('KitchenSink.view.payment.payInfoModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name:'projectId'},
        {name:'payAccountId'},
        {name:'payAccountName'},
        {name: 'projectName'},
        {name: 'projectType'},
        {name:'payCount'},
        {name: 'sPayCount'},
        {name:'fPayCount'}
    ]
});