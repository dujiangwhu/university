Ext.define('KitchenSink.view.trainTeacherMg.reviewModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'tzReviewId'},
        {name: 'stuOprid'},
        {name: 'teaOprid'},
        {name: 'tzReviewType'},
        {name: 'tzReviewDesc'},
        {name: 'tzReviewTime'},
        {name: 'tzReviewStatus'},
        {name: 'rowLastmantDttm'},
        {name: 'rowLastmantOprid'},
        {name: 'stuName'},
        {name: 'teaName'}
	]
});
