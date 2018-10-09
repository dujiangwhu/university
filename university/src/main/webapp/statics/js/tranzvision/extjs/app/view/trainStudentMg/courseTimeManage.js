Ext.define('KitchenSink.view.trainStudentMg.courseTimeManage', {//项目管理列表页面
    extend: 'Ext.grid.Panel',
	requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.trainStudentMg.courseTimeController',//调用控制器
        'KitchenSink.view.trainStudentMg.courseTimeStore',
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
    	var oprid=this.oprid;
    	var tzStoreParams='{"cfgSrhId":"PX_STU_COM.PX_COU_TIME_STD.PX_STU_KS_CHG_V","condition":{"OPRID-operator":"01","OPRID-value":"'+oprid+'"}}';
		var store = new KitchenSink.view.trainStudentMg.courseTimeStore({tzStoreParams:tzStoreParams});
        Ext.apply(this, {
        	store: store,
        	
            columns: [/*{
            	text:"机构ID",
                sortable:true,
                dataIndex:'tzJgId',
                align: 'center',
                width:150
            },*/{
            	text:"学生姓名",
                sortable:true,
                dataIndex:'tzRealname',
                align: 'center',
                width:150
            },{ 
            	text: "变化原因",
                dataIndex: 'modifyTypeDms',
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
                dataIndex: 'rowLastMantDttm',
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