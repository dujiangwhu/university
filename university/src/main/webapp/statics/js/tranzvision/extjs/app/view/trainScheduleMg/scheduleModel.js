Ext.define('KitchenSink.view.trainScheduleMg.scheduleModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'tzScheduleId'},
        {name: 'oprid'},
        {name: 'tzCourseId'},
        {name: 'tzAppStatus'},
        {name: 'tzClassStartTime'},
        {name: 'tzClassEndTime'},
        {name: 'tzScheduleType'},
        {name: 'tzScheduleDate'}
	]
});
