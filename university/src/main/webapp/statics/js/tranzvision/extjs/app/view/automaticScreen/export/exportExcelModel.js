Ext.define('KitchenSink.view.automaticScreen.export.exportExcelModel', {
    extend: 'Ext.data.Model',
    fields: [
    	{name: 'procInsId'},
    	{name: 'classBatch'},
        {name: 'fileName'},
        {name: 'czPerName'},
        {name: 'bgTime'},
        {name: 'procStaDescr'},
        {name: 'procState'}
    ]
});
