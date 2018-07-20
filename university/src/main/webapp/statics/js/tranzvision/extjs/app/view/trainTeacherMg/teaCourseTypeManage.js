Ext.define('KitchenSink.view.trainTeacherMg.teaCourseTypeManage', {//项目管理列表页面
    extend: 'Ext.grid.Panel',
	requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.trainTeacherMg.teaCourseTypeController',//调用控制器
        'KitchenSink.view.trainTeacherMg.teaCourseTypeStore',
        'tranzvision.extension.grid.column.Link'
    ],
    xtype: 'teaCourseTypeMg',//不能变
	controller: 'teaCourseTypeController',
    columnLines: true,
	selModel: {
        type: 'checkboxmodel'
    },
	style:"margin:8px",
    multiSelect: true,
    title: "教师课程级别管理",
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
			{text:"新增",tooltip:"新增数据",iconCls:"add",handler:"addPermission"},"-",
			{text:"删除",tooltip:"删除选中的数据",iconCls:"remove",handler:"deletePageRegInfos"}
			//{text:"提现",iconCls:"copy",handler:'scoreToCrash'},"-",
			//{text:"评论管理",iconCls:"edit",handler:'editTeacherInfos'},"-",
			//{text:"关注学员",iconCls:"edit",handler:'editTeacherInfos'},"-",
			//{text:"课程级别",iconCls:"edit",handler:'editTeacherInfos'},"-",
		]}
		],
    initComponent: function () { 
    	var teaOprid=this.teaOprid;
    	//alert("alert(this.teaOprid);"+this.teaOprid);
		var store = new KitchenSink.view.trainTeacherMg.teaCourseTypeStore();
        Ext.apply(this, {
        	store: store,
            columns: [{
            	text:"教师编号",
                sortable:true,
                dataIndex:'oprid',
                align: 'center',
                width:150
            },{
            	text:"姓名",
                sortable:true,
                dataIndex:'name',
                align: 'center',
                width:150
            },{
            	text: "课程级别编号",
                dataIndex: 'tzCourseTypeId',
				sortable: true,
				align: 'center',
				width: 100
            },{ 
            	text: "课程级别名称",
                dataIndex: 'typeName',
				sortable: true,
				align: 'center',
				width: 100,
				flex:1
            }/*,
            {
                menuDisabled: true,
                text:"操作",
                sortable: false,
                width: 150,
 			   align: 'center',
 			   xtype: 'actioncolumn',
 			 
 			   items:[
 			          {iconCls: 'remove',tooltip:"删除",handler:'deletePageRegInfoOne'}//,
 			          //{iconCls: 'copy',tooltip: '栏目管理',handler:'editSiteColuById'},
					  //{iconCls: 'set',tooltip: '评论管理',handler:'editReviewById'},
					  //{iconCls: 'preview',tooltip: '关注学员',handler:'editFocusById'},
					  //{iconCls: 'publish',tooltip: '课程级别',handler:'editSiteMenuById'}
 			   ]
             }*/
            ],
            buttons: [{
        		text: '保存',
        		iconCls:"save",
        		handler: 'onComRegSave'
        	}, {
        		text: '确定',
        		iconCls:"ensure",
        		handler: 'onComRegEnsure'
        	}, {
        		text: '关闭',
        		iconCls:"close",
        		handler: 'onComRegClose'
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