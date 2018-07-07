Ext.define('KitchenSink.view.judgeReviewQuality.judgeReviewQualityDetailModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'classId'},
        {name: 'batchId'},
        {name: 'judgeOprid'},
        {name: 'clpsDesc'},
        {name: 'clpsNum'},
        {name: 'tjryMsNum'},
        {name: 'interviewPass'}
    ]
});