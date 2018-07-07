Ext.define('KitchenSink.view.enrollmentManagement.materialsReview.materialsReviewSetJudgeWindowModel',{
	 extend: 'Ext.data.Model',
    fields: [
        {name: 'classId'},
        {name: 'batchId'},
        {name: 'judgeOprid'},
        {name: 'judgeId'},
        {name: 'judgeName'},
        {name: 'judgeGroup'},
        {name: 'judgeIndustry'},
        {name: 'assignedNum'},
        {name: 'selectFlag'},
        {name: 'judgeYnyw'},
        {name: 'judgeYnywDesc'},
        {name: 'judgeNum'}
    ]
});