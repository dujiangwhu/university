Ext.define('KitchenSink.view.trainTeacherMg.crashManage', {//项目管理列表页面
    extend: 'Ext.grid.Panel',
	requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.trainTeacherMg.crashController',//调用控制器
        'KitchenSink.view.trainTeacherMg.crashStore',
        'tranzvision.extension.grid.column.Link'
    ],
    xtype: 'crashMg',//不能变
	controller: 'crashController',
    columnLines: true,
	selModel: {
        type: 'checkboxmodel'
    },
	style:"margin:8px",
    multiSelect: true,
    title: "体现记录",
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
		var store = new KitchenSink.view.trainTeacherMg.crashStore;
        Ext.apply(this, {
        	store: store,
            columns: [{
            	text:"教师编号",
                sortable:true,
                dataIndex:'teaOprid',
                align: 'center',
                width:150
            },{
            	text:"姓名",
                sortable:true,
                dataIndex:'tzRealName',
                align: 'center',
                width:150
            },{ 
            	text: "提现积分",
                dataIndex: 'score',
				sortable: true,
				align: 'center',
				width: 200
            },{ 
            	text: "提现时间",
                dataIndex: 'operateTime',
				sortable: true,
				align: 'center',
				width: 200
            },{ 
            	text: "操作人",
                dataIndex: 'operateOprid',
				sortable: true,
				align: 'center',
				width: 200
            }/*,
            {
                menuDisabled: true,
                text:"操作",
                sortable: false,
                width: 100,
 			   align: 'center',
 			   xtype: 'actioncolumn',
 			   items:[
 			          //{iconCls: 'delete',tooltip:"删除",handler:'deleteInfo'}			         
 			   ]
             }*/
            ],
            buttons: [/*{
        		text: '保存',
        		iconCls:"save",
        		handler: 'onGridSave'
        	}, {
        		text: '确定',
        		iconCls:"ensure",
        		handler: 'onGridEnsure'
        	},*/ {
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