Ext.define('KitchenSink.view.judgesManagement.judgeAccount.judgeReviewNumDetail', {
    extend: 'Ext.panel.Panel',
    xtype: 'judgeReviewDetail',
    controller: 'judgeCLAccController',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'Ext.ux.MaximizeTool',
        'KitchenSink.view.judgesManagement.judgeAccount.judgeReviewNumDetailStore',
        'KitchenSink.view.judgesManagement.judgeAccount.judgeCLAccController'
    ],
    title: "评委评审人次查看",
    bodyStyle: 'overflow-y:auto;overflow-x:hidden',
    ignoreChangesFlag:true,
    initComponent: function () {

        var judgeReviewNumDetailStore = new KitchenSink.view.judgesManagement.judgeAccount.judgeReviewNumDetailStore();

        Ext.apply(this, {
            items: [{
                xtype: 'form',
                reference: 'judgeReviewNumDetailForm',
                layout: {
                    type: 'vbox',
                    align: 'stretch'
                },
                border: false,
                bodyPadding: 10,
                bodyStyle: 'overflow-y:auto;overflow-x:hidden',
                fieldDefaults: {
                    msgTarget: 'side',
                    labelWidth: 110,
                    labelStyle: 'font-weight:bold'
                },
                items: [{
                    xtype:'displayfield',
                    name:'judgeOprid',
                    hidden:true
                },{
                    xtype:'displayfield',
                    fieldLabel:"用户账号",
                    name:'judgeDlzhId'
                }, {
                    xtype:'displayfield',
                    fieldLabel:"评审人数总和",
                    name:'reviewSumNum',
                    fieldStyle:'color:red'
                },{
                    xtype:'grid',
                    title:"评审人数详细",
                    minHeight:330,
                    name:'judgeReviewNumDetailGrid',
                    columnLines:true,
                    autoHeight:true,
                    frame:true,
                    columns:[{
                        xtype:'rownumberer',
                        width:50,
                        align:'center'
                    },{
                        text:"评审项目",
                        dataIndex:'className',
                        minWidth:300,
                        flex:1
                    },{
                        text:"评审批次",
                        dataIndex:'batchName',
                        minWidth:300,
                        flex:1
                    },{
                        text:"评审人数",
                        dataIndex:'clpsNum',
                        minWidth:100,
                        flex:1
                    }],
                    store:judgeReviewNumDetailStore,
                    bbar: {
                        xtype: 'pagingtoolbar',
                        pageSize: 10,
                        store:judgeReviewNumDetailStore,
                        plugins: new Ext.ux.ProgressBarPager()
                    }
                }]
            }]
        });
        this.callParent();
    },
    buttons: [{
        text: "关闭",
        iconCls: "close",
        handler: 'onClose'
    }]
});