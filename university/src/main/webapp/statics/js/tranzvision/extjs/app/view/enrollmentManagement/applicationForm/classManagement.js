Ext.define('KitchenSink.view.enrollmentManagement.applicationForm.classManagement', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'tranzvision.extension.grid.Exporter',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.enrollmentManagement.applicationForm.classStore',
        'KitchenSink.view.enrollmentManagement.applicationForm.classController',
        'tranzvision.extension.grid.column.Link'
    ],
    xtype: 'appFormAuditing',
    columnLines: true,
    controller: 'appFormClass',
    name:'appFormClass',
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
        var store = new KitchenSink.view.enrollmentManagement.applicationForm.classStore();
        Ext.apply(this, {
            columns: [{
                text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_CLASS_STD.className","项目名称"),
                dataIndex: 'className',
                minWidth:180,
                flex:1
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
                width:110
            },{
            	text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_CLASS_STD.submittedNumber","已提交人数"),
            	dataIndex: 'submittedNumber',
            	width:110
            }/*,{
                text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_CLASS_STD.noauditNumber","未审核人数"),
                dataIndex: 'noauditNumber',
                width:140,
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
                width:130
            }/*,{
                xtype:'linkcolumn',
                text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_CLASS_STD.publishResult","报名流程结果公布"),
                width:160,
                menuDisabled:true,
                hideable:false,
                items:[{
                    getText:function(v, meta, rec) {
                        return this.text;
                    },
                    handler: "publishResult"
                }]
            }*/],
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

