Ext.define('KitchenSink.view.trainStudentMg.stuCourse', {
    extend: 'Ext.grid.Panel',
	requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
		'KitchenSink.view.trainStudentMg.stuCourseModel',
        'KitchenSink.view.trainStudentMg.stuCourseStore',
		'KitchenSink.view.trainStudentMg.stuCourseController'
    ],
    xtype: 'stuCourse',
	controller: 'stuCourseController',
	/*store: {
        type: 'comStore'
    },*/
    columnLines: true,
    selModel: {
        type: 'checkboxmodel'
    },
	style:"margin:8px",
    multiSelect: true,
    title: '学生约课管理',
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
            //{minWidth:80,text:"保存",iconCls:"save",handler:"saveComRegInfos"},
            //{minWidth:80,text:'确定',iconCls:"ensure",handler: 'ensureComRegInfos'},
            {minWidth:80,text:'关闭',iconCls:"close",handler: 'closeComRegInfos'}]
		},{
		xtype:"toolbar",
		items:[
			{text:"查询",tooltip:"查询数据",iconCls: "query",handler:"searchComList"},"-",
			//{text:"新增",tooltip:"新增数据",iconCls:"add",handler:"addComRegInfo"},"-",
			//{text:"编辑",tooltip:"编辑数据",iconCls:"edit",handler:"editComRegInfo"},"-",
			//{text:"删除",tooltip:"删除选中的数据",iconCls:"remove",handler:"deleteComRegInfos"}
		]
	}],
    initComponent: function () { 
	    //组件注册信息列表
	    var store = new KitchenSink.view.trainStudentMg.stuCourseStore();
        Ext.apply(this, {
            columns: [{ 
                text: '学员编号',
                dataIndex: 'stuOprid',
				width: 150
            },{ 
                text: '学员姓名',
                dataIndex: 'stuRealname',
				width: 150
            },{ 
                text: '学员电话',
                dataIndex: 'stuPhone',
				width: 150
            },{ 
                text: '教师编号',
                dataIndex: 'teaOprid',
				width: 150
            },{ 
                text: '教师姓名',
                dataIndex: 'teaRealname',
				width: 150
            },{ 
                text: '教师电话',
                dataIndex: 'teaPhone',
				width: 150
            },{ 
                text: '课程名称',
                dataIndex: 'tzCourseName',
				width: 150
            },{ 
                text: '课程级别',
                dataIndex: 'courseTypeName',
				width: 150
            },{ 
                text: '开始时间',
                dataIndex: 'startTime',
				width: 150
            },{ 
                text: '结束时间',
                dataIndex: 'endTime',
				width: 150
            },{ 
                text: '课程状态',
                dataIndex: 'tzAppStatus',
				width: 150
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
