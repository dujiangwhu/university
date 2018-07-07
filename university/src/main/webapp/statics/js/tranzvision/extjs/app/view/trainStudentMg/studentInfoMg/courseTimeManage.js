Ext.define('KitchenSink.view.trainStudentMg.studentInfoMg.courseTimeManage', {//项目管理列表页面
    extend: 'Ext.grid.Panel',
	requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.trainStudentMg.studentInfoMg.courseTimeController',//调用控制器
        'KitchenSink.view.trainStudentMg.studentInfoMg.courseTimeStore',
        'tranzvision.extension.grid.column.Link'
    ],
    xtype: 'courseTimeManage',//不能变
	controller: 'courseTimeController',
    columnLines: true,
	selModel: {
        type: 'checkboxmodel'
    },
	style:"margin:8px",
    multiSelect: true,
    title: "课时变化信息",
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
			//{text:Ext.tzGetResourse("TZ_FIRE_VIDEO_COM.TZ_FIRE_PROJ_STD.delete","删除"),iconCls:"delete",handler:'deleteInfos'},"-",
			
		]}
		],
    initComponent: function () {   
    	var teaOprid=this.teaOprid;
    	alert(teaOprid);
    	//var tzStoreParams='{"cfgSrhId":"TZ_PX_TEACHER_COM.TZ_PX_FOCUS_STD.PX_STU_FOCUS_T","condition":{"STU_OPRID-operator":"01","STU_OPRID-value":"'+"TZ_14072"+'"}}';
		var store = new KitchenSink.view.trainStudentMg.studentInfoMg.courseTimeStore();//({tzStoreParams:tzStoreParams});
        Ext.apply(this, {
        	store: store,
        	
            columns: [{
            	text:"机构ID",
                sortable:true,
                dataIndex:'tzJgId',
                align: 'center',
                width:150
            },{
            	text:"学生姓名",
                sortable:true,
                dataIndex:'oprid',
                align: 'center',
                width:150
            },{ 
            	text: "变化原因",
                dataIndex: 'tzKsModifyType',
				sortable: true,
				align: 'center',
				width: 200
            },{ 
            	text: "变化前课时",
                dataIndex: 'tzTimecardBefore',
				sortable: true,
				align: 'center',
				width: 200
            },{ 
            	text: "变化后课时",
                dataIndex: 'tzTimecardAfter',
				sortable: true,
				align: 'center',
				width: 200
            },{ 
            	text: "变化课时数",
                dataIndex: 'tzTimecardModify',
				sortable: true,
				align: 'center',
				width: 200
            },{ 
            	text: "变化时间",
                dataIndex: 'rowLastmantDttm',
				sortable: true,
				align: 'center',
				width: 200
            }
            ],
            buttons: [ {
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