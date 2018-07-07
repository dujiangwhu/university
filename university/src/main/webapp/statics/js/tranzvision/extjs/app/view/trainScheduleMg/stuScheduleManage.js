Ext.define('KitchenSink.view.trainScheduleMg.stuScheduleManage', {//项目管理列表页面
    extend: 'Ext.grid.Panel',
	requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.trainScheduleMg.stuScheduleController',//调用控制器
        'KitchenSink.view.trainScheduleMg.stuScheduleStore',
        'tranzvision.extension.grid.column.Link'
    ],
    xtype: 'stuScheduleMg',//不能变
	controller: 'stuScheduleController',
    columnLines: true,
	selModel: {
        type: 'checkboxmodel'
    },
	style:"margin:8px",
    multiSelect: true,
    title: "教师排课管理",
    viewConfig: {
        enableTextSelection:true
    },
	header:false,
	frame: true,
    dockedItems:[{
		xtype:"toolbar",
		//dock: 'top',设置工具条的位置
		items:[
			{text:Ext.tzGetResourse("TZ_FIRE_VIDEO_COM.TZ_FIRE_PROJ_STD.query","查询"),iconCls:"query",handler:'selectForm'},"-",
			{text:"提现",iconCls:"copy",handler:'scoreToCrash'},"-",
			//{text:"评论管理",iconCls:"edit",handler:'editTeacherInfos'},"-",
			//{text:"关注学员",iconCls:"edit",handler:'editTeacherInfos'},"-",
			//{text:"课程级别",iconCls:"edit",handler:'editTeacherInfos'},"-",
		]}
		],
    initComponent: function () {   
		var store = new KitchenSink.view.trainScheduleMg.stuScheduleStore;
        Ext.apply(this, {
        	store: store,
            columns: [{
            	text:"学生编号",
                sortable:true,
                dataIndex:'oprid',
                align: 'center',
                width:150
            },{ 
            	text: "排课编号",
                dataIndex: 'tzScheduleId',
				sortable: true,
				align: 'center',
				width: 100
            }
            ],
            buttons: [{
        		text: '关闭',
        		iconCls:"close",
        		handler: 'onGridClose'
        	}],
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