Ext.define('KitchenSink.view.judgeReviewQuality.judgeReviewQualityDetail', {
    extend: 'Ext.panel.Panel',
    xtype: 'judgeReviewQualityDetail',
    controller: 'judgeReviewQualityController',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'Ext.ux.MaximizeTool',
        'KitchenSink.view.judgeReviewQuality.judgeReviewQualityDetailStore',
        'KitchenSink.view.judgeReviewQuality.judgeReviewQualityController'
    ],
    title: Ext.tzGetResourse("TZ_CLPW_PSZL_COM.TZ_PSZL_DTL_STD.title", "评委评审情况查看"),
    bodyStyle: 'overflow-y:auto;overflow-x:hidden',
    ignoreChangesFlag:true,
    initComponent: function () {

        var judgeReviewQualityDetailStore = new KitchenSink.view.judgeReviewQuality.judgeReviewQualityDetailStore();

        Ext.apply(this, {
            items: [{
                xtype: 'form',
                reference: 'judgeReviewDetailForm',
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
                    fieldLabel:Ext.tzGetResourse("TZ_CLPW_PSZL_COM.TZ_PSZL_DTL_STD.judgeDlzhId","用户账号"),
                    name:'judgeDlzhId'
                }, {
                    xtype:'displayfield',
                    fieldLabel:Ext.tzGetResourse("TZ_CLPW_PSZL_COM.TZ_PSZL_DTL_STD.interviewPass","平均面试通过率"),
                    name:'interviewPass',
                    fieldStyle:'color:red'
                },{
                    xtype:'grid',
                    title:Ext.tzGetResourse("TZ_CLPW_PSZL_COM.TZ_PSZL_DTL_STD.gridTitle","评审情况"),
                    minHeight:330,
                    name:'judgeReviewDetailGrid',
                    columnLines:true,
                    autoHeight:true,
                    frame:true,
                    columns:[{
                        xtype:'rownumberer',
                        width:50,
                        align:'center'
                    },{
                        text:Ext.tzGetResourse("TZ_CLPW_PSZL_COM.TZ_PSZL_DTL_STD.clpsDesc","材料评审批次"),
                        dataIndex:'clpsDesc',
                        minWidth:300,
                        flex:1
                    },{
                        text:Ext.tzGetResourse("TZ_CLPW_PSZL_COM.TZ_PSZL_DTL_STD.clpsNum","材料评审结果"),
                        dataIndex:'clpsNum',
                        minWidth:100,
                        flex:1
                    },{
                        text:Ext.tzGetResourse("TZ_CLPW_PSZL_COM.TZ_PSZL_DTL_STD.tjryMsNum","推荐人员面试情况"),
                        dataIndex:'tjryMsNum',
                        minWidth:100,
                        flex:1
                    },{
                        text:Ext.tzGetResourse("TZ_CLPW_PSZL_COM.TZ_PSZL_DTL_STD.interviewPass","面试通过率"),
                        dataIndex:'interviewPass',
                        minWidth:100,
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
                    }],
                    store:judgeReviewQualityDetailStore,
                    bbar: {
                        xtype: 'pagingtoolbar',
                        pageSize: 10,
                        store:judgeReviewQualityDetailStore,
                        plugins: new Ext.ux.ProgressBarPager()
                    }
                }]
            }]
        });
        this.callParent();
    },
    buttons: [{
        text: Ext.tzGetResourse("TZ_CLPW_PSZL_COM.TZ_CLPW_PSZL_STD.close","关闭"),
        iconCls: "close",
        handler: 'closeJudge'
    }]
});