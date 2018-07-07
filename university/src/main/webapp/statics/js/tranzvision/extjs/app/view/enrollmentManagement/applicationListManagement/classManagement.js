Ext.define('KitchenSink.view.enrollmentManagement.applicationListManagement.classManagement', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.enrollmentManagement.applicationListManagement.classStore',
        'KitchenSink.view.enrollmentManagement.applicationListManagement.classController',
        'tranzvision.extension.grid.column.Link'
    ],
    xtype: 'appBatchAuditing',
    columnLines: true,
    controller: 'appBatchClass',
    name:'appBatchClass',
    style:"margin:8px",
    multiSelect: true,
    title: Ext.tzGetResourse("TZ_PS_MGR_COM.TZ_PS_MGR_STD.applicationFormAuditing","材料评审"),
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
                text:Ext.tzGetResourse("TZ_PS_MGR_COM.TZ_PS_MGR_STD.query","查询"),
                iconCls:"query",
                handler:'queryClass'
            },'-',
            {
                text:Ext.tzGetResourse("TZ_PS_MGR_COM.TZ_PS_MGR_STD.add","新增"),
                iconCls:"add",
                handler:'addClass'
            }
        ]},
        {
        xtype:"toolbar",
        dock:"bottom",
        ui:"footer",
        items:['->',
            {
                minWidth:80,
                text:Ext.tzGetResourse("TZ_PS_MGR_COM.TZ_PS_MGR_STD.close","关闭"),
                iconCls:"close",
                handler: 'onComRegClose'
            }]
        }
    ],
    initComponent: function () {
        var store = new KitchenSink.view.enrollmentManagement.applicationListManagement.classStore();
        Ext.apply(this, {
            columns: [{
                xtype: 'rownumberer',
                width:50
            },{
                dataIndex: 'classID',                
                hidden:true
            },{
                dataIndex: 'batchID',                
                hidden:true
            },{
                text:Ext.tzGetResourse("TZ_PS_MGR_COM.TZ_PS_MGR_STD.className","班级名称"),
                dataIndex: 'className',
                minWidth:240,
                flex:1
            },{
                text:Ext.tzGetResourse("TZ_PS_MGR_COM.TZ_PS_MGR_STD.batchName","批次名称"),
                dataIndex: 'batchName',
                width:95
            },{
                text:Ext.tzGetResourse("TZ_PS_MGR_COM.TZ_PS_MGR_STD.applicantsNumber","报名人数"),
                dataIndex: 'stuNum',
                width:110
            },{
                menuDisabled: true,
                sortable: false,
                width:150,
                text: "材料评审",
                xtype: 'actioncolumn',
                align:'center',
                items:[
                	{iconCls: 'import',tooltip: Ext.tzGetResourse("TZ_PS_MGR_COM.TZ_PS_MGR_STD.autoScreen","自动初筛"),handler:'automaticScreen'},"-",
                    {iconCls: 'set',tooltip: Ext.tzGetResourse("TZ_PS_MGR_COM.TZ_PS_MGR_STD.clmspsSet","设置评审规则"),handler:'setMaterialReviewRule'},"-",
                    {iconCls: 'people',tooltip: Ext.tzGetResourse("TZ_PS_MGR_COM.TZ_PS_MGR_STD.clmspsPeople","查看考生名单"),handler:'viewMaterialStuApplicants'},"-",
                    {iconCls: 'schedule',tooltip: Ext.tzGetResourse("TZ_PS_MGR_COM.TZ_PS_MGR_STD.clmspsSchedule","评审进度管理"),handler:'clReviewScheduleMg'}
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

