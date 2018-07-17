Ext.define('KitchenSink.view.trainCourseMg.trainCourseType', {
    extend: 'Ext.grid.Panel',
	requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
		'KitchenSink.view.trainCourseMg.trainCourseTypeController',
		'KitchenSink.view.trainCourseMg.trainCourseTypeModel',
        'KitchenSink.view.trainCourseMg.trainCourseTypeStore'
    ],
    xtype: 'trainCourseTypeMg',
	controller: 'trainCourseTypeController',

    columnLines: true,
    selModel: {
        type: 'checkboxmodel'
    },
	style:"margin:8px",
    multiSelect: true,
    title: '课程级别列表',
    viewConfig: {
        enableTextSelection: true
    },
	header:false,
	frame: true,
    dockedItems:[{
		xtype:"toolbar",
		dock:"bottom",
		ui:"footer",
		items:['->',
            {minWidth:80,text:"保存",iconCls:"save",handler:"onSaveInfos"},
            {minWidth:80,text:'确定',iconCls:"ensure",handler: 'onEnsureInfos'},
            {minWidth:80,text:'关闭',iconCls:"close",handler: 'onCloseInfos'}]
		},{
		xtype:"toolbar",
		items:[
			{text:"查询",tooltip:"查询数据",iconCls: "query",handler:"selectForm"},"-",
			{text:"新增",tooltip:"新增数据",iconCls:"add",handler:"addInfos"},"-",
			{text:"编辑",tooltip:"编辑数据",iconCls:"edit",handler:"editInfos"},"-",
			{text:"删除",tooltip:"删除选中的数据",iconCls:"remove",handler:"deleteInfos"}
		]
	}],
    initComponent: function () { 
	    //组件注册信息列表
	    var store = new KitchenSink.view.trainCourseMg.trainCourseTypeStore();
        Ext.apply(this, {
        	columns: [{
                text: '课程级别编号',
                dataIndex: 'tzCourseTypeId',
                width: 150
            },{
                text: '课程级别名称',
                dataIndex: 'tzCourseTypeName',
                width: 230
            },{
                text: '课程级别类型',
                dataIndex: 'typeDms',
                width: 230
            },{
                text: '课程最小年龄',
                sortable: true,
                dataIndex: 'tzMinAge',
                width: 120
            },{
                text: '课程最大年龄',
                sortable: true,
                dataIndex: 'tzMaxAge',
                width: 120,
                flex:1
            },{
                menuDisabled: true,
                sortable: false,
                width:40,
                xtype: 'actioncolumn',
                items:[
                    {iconCls: 'edit',tooltip: '编辑',handler:'editInfo'},
  			   	  	{iconCls: 'remove',tooltip: '删除',handler: 'deleteInfo'}
                ]
            }],
			store: store,
            bbar: {
                xtype: 'pagingtoolbar',
                pageSize: 10,
				store: store,
                plugins: new Ext.ux.ProgressBarPager()
            }
        });
        this.callParent();
    }
});
