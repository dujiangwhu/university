﻿Ext.define('KitchenSink.view.interviewManagement.interviewReview.interviewReview', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.interviewManagement.interviewReview.interviewReviewStore',
        'KitchenSink.view.interviewManagement.interviewReview.interviewReviewController',
        'KitchenSink.view.common.store.comboxStore',
        'Ext.ux.MaximizeTool'
    ],
    xtype: 'interviewReview',
    columnLines: true,
//    selModel: {
//        type: 'checkboxmodel'
//    },
    controller: 'interviewReview',
    style:"margin:8px",
    multiSelect: true,
    title: '面试评审',
    viewConfig: {
        enableTextSelection: true
    },
    header:false,
    frame: true,
    dockedItems:[{
        xtype:"toolbar",
        items:[
            {text:"查询",tooltip:"查询数据",iconCls:"query",handler:"queryClassBatch"}
        ]
    },{
            xtype:"toolbar",
            dock:"bottom",
            ui:"footer",
            items:['->',
                {
                    minWidth:80,
                    text:"关闭",
                    iconCls:"close",
                    handler: 'onGridPanelClose'
                }]
        }],
    initComponent: function () {
        var store = new KitchenSink.view.interviewManagement.interviewReview.interviewReviewStore(),
            transValue = this.controller.transvalueCollection = this.controller.transValues(),
            self = this;
        transValue.set("TZ_MSPS_STAGE",function(){
            store.load();
        });

        Ext.apply(this, {
            columns: [{
                text: '班级编号',
                dataIndex: 'classID',
                hidden:true
            },{
                text: '班级名称',
                dataIndex: 'className',
                minWidth:180,
                sortable:false,
                flex:2
            },{
                text: '批次编号',
                dataIndex: 'batchID',
                hidden:true
            },{
                text: '批次',
                dataIndex: 'batchName',
                minWidth:100,
                sortable:false,
                flex:1
            },{
                text: '开始日期',
                dataIndex: 'startDate',
                width:110,
                align:'center'
            },{
                text: '结束日期',
                dataIndex: 'endDate',
                width:110,
                align:'center'
            },{
                text: '评审状态',
                dataIndex: 'status',
                width:100,
                align:'center',
                renderer: function (v,grid,record){
                        var x;
                        var KSPWPSEHNZT = transValue.get("TZ_MSPS_STAGE");
                        v = v?v:'N';
                        if((x = KSPWPSEHNZT.find('TValue',v))>=0){
                            return KSPWPSEHNZT.getAt(x).data.TSDesc;
                        }else{
                            return v;
                        }
                    }

            },{
                menuDisabled: true,
                sortable: false,
                width:150,
                text: "操作",
                flex:1,
                xtype: 'actioncolumn',
                align:'center',
                items:[
                    {iconCls: 'set',tooltip: '设置评审规则',handler:'setReviewRule'},"-",
                    {iconCls: 'people',tooltip: '查看考生名单',handler:'viewApplicants'},"-",
                    {iconCls: 'schedule',tooltip: '评审进度管理',handler:'reviewScheduleMg'}
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

