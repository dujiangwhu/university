Ext.define('KitchenSink.view.judgeReviewQuality.judgeReviewQualityMg', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.judgeReviewQuality.judgeReviewQualityStore',
        'KitchenSink.view.judgeReviewQuality.judgeReviewQualityController',
        'tranzvision.extension.grid.column.Link'
    ],
    xtype: 'judgeReviewQualityMg',
    columnLines: true,
    controller: 'judgeReviewQualityController',
    style:"margin:8px",
    multiSelect: true,
    title: Ext.tzGetResourse("TZ_CLPW_PSZL_COM.TZ_CLPW_PSZL_STD.title","评委评审情况管理"),
    viewConfig: {
        enableTextSelection: true
    },
    header:false,
    frame: true,
    dockedItems:[
        {
            xtype:"toolbar",
            items:[
                {
                    text:Ext.tzGetResourse("TZ_CLPW_PSZL_COM.TZ_CLPW_PSZL_STD.query","查询"),
                    iconCls:"query",
                    handler:'queryJudge'
                }
            ]},
        {
            xtype:"toolbar",
            dock:"bottom",
            ui:"footer",
            items:['->',
                {
                    minWidth:80,
                    text:Ext.tzGetResourse("TZ_CLPW_PSZL_COM.TZ_CLPW_PSZL_STD.close","关闭"),
                    iconCls:"close",
                    handler: 'closeJudge'
                }]
        }
    ],
    initComponent: function () {
        var store = new KitchenSink.view.judgeReviewQuality.judgeReviewQualityStore();
        Ext.apply(this, {
            columns: [{
                dataIndex:'judgeOprid',
                hidden:true
            },{
                xtype: 'rownumberer',
                width:30
            },{
                text:Ext.tzGetResourse("TZ_CLPW_PSZL_COM.TZ_CLPW_PSZL_STD.judgeDlzhId","用户账号"),
                dataIndex: 'judgeDlzhId',
                minWidth:240,
                flex:1
            },{
                text:Ext.tzGetResourse("TZ_CLPW_PSZL_COM.TZ_CLPW_PSZL_STD.judgeName","用户描述"),
                dataIndex: 'judgeName',
                minWidth:125,
                flex:1
            },{
                text: Ext.tzGetResourse("TZ_CLPW_PSZL_COM.TZ_CLPW_PSZL_STD.interviewPass","平均面试通过率"),
                dataIndex: 'interviewPass',
                minWidth:200,
                flex:1,
                renderer:function(v) {
                    var value ;
                    if(v!=0) {
                        value = v+"%";
                    } else {
                        value = v;
                    }
                    return value;
                }
            },{
                menuDisabled: true,
                sortable: false,
                width:100,
                text: "操作",
                xtype: 'actioncolumn',
                align:'center',
                items:[
                    {iconCls: 'view',tooltip: Ext.tzGetResourse("TZ_CLPW_PSZL_COM.TZ_CLPW_PSZL_STD.view","查看"),handler:'viewJudgeDetail'}
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

