Ext.define('KitchenSink.view.trainStudentMg.stuCourseModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'tzScheduleId'},
        {name: 'stuOprid'},
        {name: 'stuRealname'},
        {name: 'stuPhone'},
        {name: 'teaOprid'},
        {name: 'teaRealname'},
        {name: 'teaPhone'},
        {name: 'tzAppStatus'},
        {name: 'tzCourseName'},
        {name: 'courseTypeName'},
        {name: 'startTime'},
        {name: 'endTime'}
	]
});
