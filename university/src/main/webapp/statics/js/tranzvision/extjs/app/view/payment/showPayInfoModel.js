Ext.define('KitchenSink.view.payment.showPayInfoModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name:"id"},
        {name:"paymentId"},
        {name:"tzPayPrjId"},
        {name:"projectName"},
        {name:"payRole"},
        {name:"userId"},
        {name:"payAccountName"},
        {name:"currency"},
        {name:"payStatus"},
        {name:"submitTime"},
        {name:"payedTime"},
        {name:"payCompanyOrderId"},
        {name:"payReturnAmount"},
        {name:"payReturnCurrency"},
        {name:"payMode"},
        {name:"userName"}
    ]

});
