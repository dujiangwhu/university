Ext.define('KitchenSink.view.trainTeacherMg.reviewManage', {//项目管理列表页面
    extend: 'Ext.grid.Panel',
	requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.trainTeacherMg.reviewController',//调用控制器
        'KitchenSink.view.trainTeacherMg.reviewStore',
        'tranzvision.extension.grid.column.Link'
    ],
    xtype: 'reviewMg',//不能变
	controller: 'reviewController',
    columnLines: true,
	selModel: {
        type: 'checkboxmodel'
    },
	style:"margin:8px",
    multiSelect: true,
    title: "评论信息",
    viewConfig: {
        enableTextSelection:true
    },
	header:false,
	frame: true,
    dockedItems:[{
		xtype:"toolbar",
		//dock: 'top',设置工具条的位置
		items:[
			//{text:Ext.tzGetResourse("TZ_FIRE_VIDEO_COM.TZ_FIRE_PROJ_STD.query","查询"),iconCls:"query",handler:'selectForm'},"-",
			{text:Ext.tzGetResourse("TZ_FIRE_VIDEO_COM.TZ_FIRE_PROJ_STD.delete","删除"),iconCls:"delete",handler:'deleteInfos'},"-",
			
		]}
		],
    initComponent: function () {   
    	var stuOprid=this.stuOprid==undefined?"":this.stuOprid;
    	var teaOprid=this.teaOprid==undefined?"":this.teaOprid;
    	//alert(teaOprid);
    	var tzStoreParams='{"cfgSrhId":"TZ_PX_TEACHER_COM.TZ_PX_REVIEW_STD.PX_STU_REVIEW_V","condition":{'
    		+'"STU_OPRID-operator":"01","STU_OPRID-value":"'+stuOprid+'"'
    		+',"TEA_OPRID-operator":"01","TEA_OPRID-value":"'+teaOprid+'"'
    		+'}}';
		var store = new KitchenSink.view.trainTeacherMg.reviewStore({tzStoreParams:tzStoreParams});
        Ext.apply(this, {
        	store: store,
        	
            
            columns: [{
            	text:"编号",
                sortable:true,
                dataIndex:'tzReviewId',
                align: 'center',
                width:150
            },{
            	text:"学生姓名",
                sortable:true,
                dataIndex:'stuName',
                align: 'center',
                width:150
            },{
            	text:"被评论教师",
                sortable:true,
                dataIndex:'teaName',
                align: 'center',
                width:150
            },{ 
            	text: "评论类别",
                dataIndex: 'tzReviewType',
				sortable: true,
				align: 'center',
				width: 100
            },{ 
            	text: "评论时间",
                dataIndex: 'tzReviewTime',
				sortable: true,
				align: 'center',
				width: 200
            },{ 
            	text: "评论状态",
                dataIndex: 'tzReviewStatus',
				sortable: true,
				align: 'center',
				width: 100
            },{ 
            	text: "评论内容",
                dataIndex: 'tzReviewDesc',
				sortable: true,
				align: 'center',
				flex: 1,
				width: 500
            },
            {
                menuDisabled: true,
                text:"操作",
                sortable: false,
                width: 100,
 			   align: 'center',
 			   xtype: 'actioncolumn',
 			   items:[
 			          {iconCls: 'delete',tooltip:"删除",handler:'deleteInfo'}			         
 			   ]
             }
            ],
            buttons: [{
        		text: '保存',
        		iconCls:"save",
        		handler: 'onGridSave'
        	}, {
        		text: '确定',
        		iconCls:"ensure",
        		handler: 'onGridEnsure'
        	}, {
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