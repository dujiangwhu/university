Ext.define('KitchenSink.view.trainScheduleMg.scheduleManage', {//项目管理列表页面
    extend: 'Ext.grid.Panel',
	requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.trainScheduleMg.scheduleController',//调用控制器
        'KitchenSink.view.trainScheduleMg.scheduleStore',
        'tranzvision.extension.grid.column.Link'
    ],
    xtype: 'scheduleMg',//不能变
	controller: 'scheduleController',
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
			{text:"学员列表",iconCls:"view",tooltip: '学员列表',handler:'editFocusById'}
			//{text:"评论管理",iconCls:"edit",handler:'editTeacherInfos'},"-",
			//{text:"关注学员",iconCls:"edit",handler:'editTeacherInfos'},"-",
			//{text:"课程级别",iconCls:"edit",handler:'editTeacherInfos'},"-",
		]}
		],
    initComponent: function () {   
		var store = new KitchenSink.view.trainScheduleMg.scheduleStore;
        Ext.apply(this, {
        	store: store,
            columns: [{
            	text:"课程",
                sortable:true,
                dataIndex:'tzCourseName',
                align: 'center',
                width:150
            },{
            	text:"课程级别",
                sortable:true,
                dataIndex:'courseTypeName',
                align: 'center',
                width:150
            },{
            	text:"上课老师",
                sortable:true,
                dataIndex:'teaName',
                align: 'center',
                width:150
            },{ 
            	text: "老师电话",
                dataIndex: 'teaPhone',
				sortable: true,
				align: 'center',
				width: 100
            },{ 
            	text: "预定状态",
                dataIndex: 'tzAppStatus',
				sortable: true,
				align: 'center',
				width: 100
            },{ 
            	text: "上课开始时间",
                dataIndex: 'tzClassStartTime',
				sortable: true,
				align: 'center',
				width: 150
            },{ 
            	text: "上课结束时间",
                dataIndex: 'tzClassEndTime',
				sortable: true,
				align: 'center',
				width: 100
            },{ 
            	text: "排课提交时间",
                dataIndex: 'rowLastmant',
				sortable: true,
				align: 'center',
				width: 100
            },{ 
            	text: "状态",
                dataIndex: 'tzScheduleType',
				sortable: true,
				align: 'center',
				 flex: 1,
				width: 150
            }/*,
            {
                menuDisabled: true,
                text:"操作",
                sortable: false,
                width: 150,
 			   align: 'center',
 			   xtype: 'actioncolumn',
 			 
 			   items:[
 			          //{iconCls: 'edit',tooltip:"学员列表",handler:'editTeacherInfo'},
 			          //{iconCls: 'copy',tooltip: '栏目管理',handler:'editSiteColuById'},
					  //{iconCls: 'set',tooltip: '评论管理',handler:'editReviewById'},
					  {iconCls: 'preview',tooltip: '学员列表',handler:'editFocusById'},
					  {iconCls: 'set',tooltip: '撤销课程',handler:'addPermission'}
 			   ]
             }*/
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