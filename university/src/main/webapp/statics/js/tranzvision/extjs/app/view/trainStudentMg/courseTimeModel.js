Ext.define('KitchenSink.view.trainStudentMg.courseTimeModel', {
    extend: 'Ext.data.Model',
    fields: [
		{name: 'tzJgId'},
        {name: 'oprid'},
        {name: 'tzRealname'},
        {name: 'modifyTypeDms'},
        {name: 'tzTimecardBefore'},
        {name: 'tzTimecardAfter'},
         {name: 'tzTimecardModify'},
        {name: 'rowLastMantDttm'}
	]
});
