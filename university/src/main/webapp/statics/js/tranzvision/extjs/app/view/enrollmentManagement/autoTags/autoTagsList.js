Ext.define('KitchenSink.view.enrollmentManagement.autoTags.autoTagsList', {
    extend: 'Ext.grid.Panel',
	requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.enrollmentManagement.autoTags.autoTagsController',
        'KitchenSink.view.enrollmentManagement.autoTags.autoTagsStore'
    ],
    xtype: 'autoTagsList',
	controller: 'autoTagsController',
	title: Ext.tzGetResourse("TZ_AUTO_TAGS_COM.TZ_AUTO_TAGS_STD.autoTagsDefn","自动标签定义"),
	
	columnLines: true,
    selModel: {
        type: 'checkboxmodel'
    },
	style:"margin:8px",
	multiSelect: true,
	viewConfig: {
        enableTextSelection: true
    },
	header:false,
	frame: true,
    dockedItems:[{
		xtype:"toolbar",
		items:[
			{text:Ext.tzGetResourse("TZ_AUTO_TAGS_COM.TZ_AUTO_TAGS_STD.query","查询"),iconCls:"query",handler:'queryList'},"-",
			{text:Ext.tzGetResourse("TZ_AUTO_TAGS_COM.TZ_AUTO_TAGS_STD.add","新增"),iconCls:"add",handler:'addAutoTag'},"-",
			{text:Ext.tzGetResourse("TZ_AUTO_TAGS_COM.TZ_AUTO_TAGS_STD.edit","编辑"),iconCls:"edit",handler:'ediAutoTag'},"-",
			{text:Ext.tzGetResourse("TZ_AUTO_TAGS_COM.TZ_AUTO_TAGS_STD.delete","删除"),iconCls:"remove",handler:'delAutoTag'}
		]
	},{
        xtype:"toolbar",
        dock:"bottom",
        ui:"footer",
        items:['->',
        	{
	            text:Ext.tzGetResourse("TZ_AUTO_TAGS_COM.TZ_AUTO_TAGS_STD.save","保存"),
	            iconCls:"save",
	            name: 'saveBtn',
	            handler: 'autoTagsListSave'
	        },{
	            text:Ext.tzGetResourse("TZ_AUTO_TAGS_COM.TZ_AUTO_TAGS_STD.ensure","确定"),
	            iconCls:"ensure",
	            name: 'ensureBtn',
	            handler: 'autoTagsListEnsure'
	        },{
                text:Ext.tzGetResourse("TZ_AUTO_TAGS_COM.TZ_AUTO_TAGS_STD.close","关闭"),
                iconCls:"close",
                handler: function(btn){
                	btn.findParentByType('autoTagsList').close();
                }
            }]
    }],
    initComponent: function () {   
		var store = new KitchenSink.view.enrollmentManagement.autoTags.autoTagsStore();
        Ext.apply(this, {
            columns: [{ 
                text: Ext.tzGetResourse("TZ_AUTO_TAGS_COM.TZ_AUTO_TAGS_STD.rowNum","序号"),
                xtype:'rownumberer',
				sortable: false,
				width: 60
            },{ 
                text: Ext.tzGetResourse("TZ_AUTO_TAGS_COM.TZ_AUTO_TAGS_STD.tagId","标签编号"),
				sortable: true,
				hidden: true,
                dataIndex: 'tagId',
                width: 120
            },{
                text: Ext.tzGetResourse("TZ_AUTO_TAGS_COM.TZ_AUTO_TAGS_STD.tagName","标签名称"),
                sortable: true,
                dataIndex: 'tagName',
                minWidth: 120,
                flex: 1
            },{
                text: Ext.tzGetResourse("TZ_AUTO_TAGS_COM.TZ_AUTO_TAGS_STD.isValid","有效状态"),
                sortable: true,
                dataIndex: 'isValid',
                width: 120,
                renderer: function(val){
                	if(val == "Y"){
                		return "有效"
                	}else{
                		return "无效"
                	}
                }
            },{
               menuDisabled: true,
               sortable: false,
               width:60,
			   align: 'center',
			   xtype: 'actioncolumn',
			   items:[
				   {iconCls: 'edit',tooltip: Ext.tzGetResourse("TZ_AUTO_TAGS_COM.TZ_AUTO_TAGS_STD.edit","编辑"),handler:'editCurrAutoTag'},
				   {iconCls: 'remove',tooltip: Ext.tzGetResourse("TZ_AUTO_TAGS_COM.TZ_AUTO_TAGS_STD.delete","删除"),handler:'delCurrAutoTag'}
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