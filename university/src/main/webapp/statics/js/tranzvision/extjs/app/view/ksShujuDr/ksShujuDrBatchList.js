Ext.define('KitchenSink.view.ksShujuDr.ksShujuDrBatchList', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.ksShujuDr.ksShujuDrBatchController',
        'KitchenSink.view.ksShujuDr.ksShujuDrBatchStore'
    ],
    controller: 'ksShujuDrBatchController',
    columnLines: true,
    selModel: {
        type: 'checkboxmodel'
    },
    style:"margin:8px",
    title: '考生数据导入批次管理',
    viewConfig: {
        enableTextSelection: true
    },
    header:false,
    frame: true,
    dockedItems:[{
			xtype:"toolbar",
			items:[
				{text:Ext.tzGetResourse("TZ_DRBATLIST_COM.TZ_DRBATLIST_STD.query","查询"),iconCls: "query",handler:"cfgSearchBatch"},'-',
				{text:Ext.tzGetResourse("TZ_DRBATLIST_COM.TZ_DRBATLIST_STD.edit","查看"),iconCls:"edit",handler:'viewBatchInfo'}
			]
		},{
        xtype:"toolbar",
        dock:"bottom",
        ui:"footer",
        items:['->',
            {minWidth:80,text:"关闭",iconCls:"close",handler:
                function(btn){
                    var grid = btn.findParentByType("grid");
                    grid.close();
                }
            }
        ]
    }],
    initComponent: function(){
        var store = new KitchenSink.view.ksShujuDr.ksShujuDrBatchStore();
        Ext.apply(this, {
            columns: [{
                text: '机构编号',
                dataIndex: 'jgId',
                width: 150,
				hidden:true
            },{
                text: '批次编号',
                dataIndex: 'batchId',
                width: 150,
				hidden:true
            },{
                text: '模板编号',
                dataIndex: 'tplId',
                width: 150,
				hidden:true
            },{
                text: '导入描述信息',
                dataIndex: 'tplName',
                minWidth: 200,
                flex: 1
            },{ text: '文件名',
                dataIndex: 'filename',
                minWidth: 200,
                flex: 2
            },{
                text: '导入时间',
                dataIndex: 'drDate',
                minWidth: 150
            },{
                text: '导入人',
                dataIndex: 'drrName',
                minWidth: 150
            },{
                menuDisabled: true,
                sortable: false,
                width:70,
                xtype: 'actioncolumn',
                items:[
                    {iconCls: 'publish',tooltip: '发布',handler:'publish'},
                    {iconCls: 'cancel',tooltip: '撤销发布',handler:'cancel'},
					{iconCls: 'view',tooltip: '查看',handler:'viewSelBatchInfo'}
                    
                ]
            }],
            store:store,
            bbar: {
                xtype: 'pagingtoolbar',
                pageSize: 10,
                store:store,
                plugins: new Ext.ux.ProgressBarPager()
            }
        });
        this.callParent();
    }
});
