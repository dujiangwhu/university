Ext.define('KitchenSink.view.enrollmentManagement.applicationBatch.classManagement', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'tranzvision.extension.grid.Exporter',
        'KitchenSink.view.enrollmentManagement.applicationBatch.classStore',
        'KitchenSink.view.enrollmentManagement.applicationBatch.classController',
        'tranzvision.extension.grid.column.Link'
    ],
    xtype: 'appBatchAuditing',
    columnLines: true,
    controller: 'appBatchClass',
    name:'appBatchClass',
    style:"margin:8px",
    multiSelect: true,
    plugins: [{
        ptype: 'gridexporter'
    }],
    title: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_CLASS_STD.applicationFormAuditing","报名表审核"),
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
                text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_CLASS_STD.query","查询"),
                iconCls:"query",
                handler:'queryClass'
            },'-',
            {
                text:"导出搜索结果到Excel",
                iconCls:"excel",
                handler:"exportSerachResult"
            }
        ]},
        {
        xtype:"toolbar",
        dock:"bottom",
        ui:"footer",
        items:['->',
            {
                minWidth:80,
                text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_CLASS_STD.close","关闭"),
                iconCls:"close",
                handler: 'onComRegClose'
            }]
        }
    ],
    initComponent: function () {
        var store = new KitchenSink.view.enrollmentManagement.applicationBatch.classStore();
        //管理员标志，S-超级管理员，J-机构管理员，N-其他角色
        var adminFlag = "";
        var tzExpandParams = '{"ComID":"TZ_BMGL_BMBPICI_COM","PageID":"TZ_BMGL_CLASS_STD","OperateType":"QF","comParams":{}}';
        Ext.tzLoadAsync(tzExpandParams,function(respData){
        	adminFlag = respData.adminFlag;
        });
        var columns = [{
            xtype: 'rownumberer',
            width:30
        },{
            text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_CLASS_STD.proName","项目名称"),
            dataIndex: 'className',
            minWidth:240,
            flex:1
        },{
            text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_CLASS_STD.batchName","批次名称"),
            dataIndex: 'batchName',
            width:85
        },{
            text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_CLASS_STD.projectName","所属项目"),
            dataIndex: 'projectName',
            minWidth:120,
            flex:1
        },{
            text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_CLASS_STD.projectType","项目类别"),
            dataIndex: 'projectType',
            minWidth:110
        },{
            text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_CLASS_STD.applicantsNumber","报名人数"),
            dataIndex: 'applicantsNumber',
            width:80
        },{
        	text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_CLASS_STD.submittedNumber","已提交人数"),
        	dataIndex: 'submittedNumber',
        	width:110
        }/*,{
            text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_CLASS_STD.noauditNumber","未审核人数"),
            dataIndex: 'noauditNumber',
            width:100,
            renderer: function(val){
                if(val>0){
                    return '<span  style="color: #ED0048;" >'+val+'</span>';
                }else{
                    return val;
                }
            }
        }*/,{
            text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_CLASS_STD.reviewNumber","已评审人数"),
            dataIndex: 'reviewNumber',
            width:100,
            renderer: function(val){
                if(val != '0'){
                    return '<span  style="color: #ED0048;" >'+val+'</span>';
                }else{
                    return val;
                }
            }
        },{
            text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_CLASS_STD.publishNumber","已发布人数"),
            dataIndex: 'publishNumber',
            width:100,
            renderer: function(val){
                if(val != '0'){
                    return '<span  style="color: #ED0048;" >'+val+'</span>';
                }else{
                    return val;
                }
            }
        },{
        	text:"预录取人数",
        	dataIndex: 'preEnroll',
        	width:110
        },{
        	text:"有条件预录取人数",
        	dataIndex: 'ifPreEnroll',
        	width:140
        },{
        	text:"等待人数",
        	dataIndex: 'wait',
        	width:110
        },{
        	text:"未通过人数",
        	dataIndex: 'noPass',
        	width:110
        },{
        	text:"颜色类别（保留预录取）",
        	dataIndex: 'colorSortSave',
        	width:150
        },{
        	text:"颜色类别（放弃）",
        	dataIndex: 'colorSortGiveUp',
        	width:130
        },{
        	text:"颜色类别（X计划）",
        	dataIndex: 'colorSortXPlan',
        	width:130
        },{
            xtype:'linkcolumn',
            text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_CLASS_STD.viewApplicants","查看报考学生"),
            menuDisabled:true,
            hideable:false,
            items:[{
                getText:function(v, meta, rec) {
                    return this.text;
                },
                handler: "viewApplicants"
            }],
            width:110
        }];
        
        if(adminFlag=="S"||adminFlag=="J"){
        	columns.push({
                menuDisabled: true,
                sortable: false,
                width:100,
                text: "材料评审",
                xtype: 'actioncolumn',
                align:'center',
                items:[
                	/*{iconCls: 'import',tooltip: Ext.tzGetResourse("TZ_PS_MGR_COM.TZ_PS_MGR_STD.autoScreen","自动初筛"),handler:'automaticScreen'},"-",*/
                    {iconCls: 'set',tooltip: Ext.tzGetResourse("TZ_PS_MGR_COM.TZ_PS_MGR_STD.clmspsSet","设置评审规则"),handler:'setMaterialReviewRule'},"-",
                    {iconCls: 'people',tooltip: Ext.tzGetResourse("TZ_PS_MGR_COM.TZ_PS_MGR_STD.clmspsPeople","查看考生名单"),handler:'viewMaterialStuApplicants'},"-",
                    {iconCls: 'schedule',tooltip: Ext.tzGetResourse("TZ_PS_MGR_COM.TZ_PS_MGR_STD.clmspsSchedule","评审进度管理"),handler:'clReviewScheduleMg'}
                ]
            });
        }
        
        Ext.apply(this, {
            columns: columns,
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

