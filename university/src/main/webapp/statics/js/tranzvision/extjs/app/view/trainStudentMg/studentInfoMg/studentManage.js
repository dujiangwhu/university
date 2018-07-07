Ext.define('KitchenSink.view.trainStudentMg.studentInfoMg.studentManage', {//项目管理列表页面
    extend: 'Ext.grid.Panel',
	requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.trainStudentMg.studentInfoMg.studentController',//调用控制器
        'KitchenSink.view.trainStudentMg.studentInfoMg.studentStore',
        'tranzvision.extension.grid.column.Link'
    ],
    xtype: 'studentInfoMg',//不能变
	controller: 'studentInfoMg',
    columnLines: true,
	selModel: {
        type: 'checkboxmodel'
    },
	style:"margin:8px",
    multiSelect: true,
    title: "学员管理",
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
			{text:"编辑",iconCls:"edit",handler:'scoreToCrash'},"-",
			//{text:"评论管理",iconCls:"edit",handler:'editTeacherInfos'},"-",
			//{text:"关注学员",iconCls:"edit",handler:'editTeacherInfos'},"-",
			//{text:"课程级别",iconCls:"edit",handler:'editTeacherInfos'},"-",
		]}
		],
    initComponent: function () {   
		var store = new KitchenSink.view.trainStudentMg.studentInfoMg.studentStore;
        Ext.apply(this, {
        	store: store,
            columns: [{
            	text:"姓名",
                sortable:true,
                dataIndex:'name',
                align: 'center',
                width:150
            },{ 
            	text: "性别",
                dataIndex: 'sex',
				sortable: true,
				align: 'center',
				width: 100
            },{ 
            	text: "年龄",
                dataIndex: 'level',
				sortable: true,
				align: 'center',
				width: 100
            },{ 
            	text: "手机",
                dataIndex: 'phone',
				sortable: true,
				align: 'center',
				width: 150
            },{ 
            	text: "状态信息",
                dataIndex: 'statu',
				sortable: true,
				align: 'center',
				 flex: 1,
				width: 150
            },
            {
                menuDisabled: true,
                text:"操作",
                sortable: false,
                width: 150,
 			   align: 'center',
 			   xtype: 'actioncolumn',
 			 
 			   items:[
 			          {iconCls: 'edit',tooltip:"编辑",handler:'editTeacherInfo'},
 			          //{iconCls: 'copy',tooltip: '栏目管理',handler:'editSiteColuById'},
					  {iconCls: 'set',tooltip: '评论管理',handler:'editReviewById'},
					  {iconCls: 'preview',tooltip: '关注老师',handler:'editFocusById'},
					  {iconCls: 'publish',tooltip: '课时变动查询',handler:'editSiteMenuById'}
 			   ]
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