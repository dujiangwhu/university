Ext.define('KitchenSink.view.trainCourseMg.trainCourse', {
    extend: 'Ext.grid.Panel',
	requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
		'KitchenSink.view.trainCourseMg.trainCourseModel',
        'KitchenSink.view.trainCourseMg.trainCourseStore',
		'KitchenSink.view.trainCourseMg.trainCourseController'
    ],
    xtype: 'trainCourseMg',
	controller: 'trainCourseMg',
	/*store: {
        type: 'comStore'
    },*/
    columnLines: true,
    selModel: {
        type: 'checkboxmodel'
    },
	style:"margin:8px",
    multiSelect: true,
    title: '课程管理',
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
            {minWidth:80,text:"保存",iconCls:"save",handler:"saveComRegInfos"},
            {minWidth:80,text:'确定',iconCls:"ensure",handler: 'ensureComRegInfos'},
            {minWidth:80,text:'关闭',iconCls:"close",handler: 'closeComRegInfos'}]
		},{
		xtype:"toolbar",
		items:[
			{text:"查询",tooltip:"查询数据",iconCls: "query",handler:"searchComList"},"-",
			{text:"新增",tooltip:"新增数据",iconCls:"add",handler:"addComRegInfo"},"-",
			{text:"编辑",tooltip:"编辑数据",iconCls:"edit",handler:"editComRegInfo"},"-",
			{text:"删除",tooltip:"删除选中的数据",iconCls:"remove",handler:"deleteComRegInfos"}
		]
	}],
    initComponent: function () { 
	    //组件注册信息列表
	    var store = new KitchenSink.view.trainCourseMg.trainCourseStore();
        Ext.apply(this, {
            columns: [{ 
                text: '课程编号',
                dataIndex: 'tzCourseId',
				width: 140
            },{ 
                text: '课程名称',
                dataIndex: 'tzCourseName',
				width: 180
            },{ 
                text: '所属课程类型',
                dataIndex: 'typeName',
				width: 180
            },{
                text: '课程类型级别',
                sortable: true,
                dataIndex: 'typeDms',
                width: 180
            },{
                text: '课程简介',
                sortable: true,
                dataIndex: 'tzCourseDesc',
                minWidth: 150,
				flex:1
            },{
               menuDisabled: true,
               sortable: false,
			   width:60,
               align:'center',
               xtype: 'actioncolumn',
			   items:[
				  {iconCls: 'edit',tooltip: '编辑',handler: 'editSelComRegInfo'},
			   	  {iconCls: 'remove',tooltip: '删除',handler: 'deleteComRegInfo'}
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
