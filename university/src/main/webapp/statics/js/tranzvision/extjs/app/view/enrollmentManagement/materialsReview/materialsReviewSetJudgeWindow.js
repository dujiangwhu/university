Ext.define('KitchenSink.view.enrollmentManagement.materialsReview.materialsReviewSetJudgeWindow',{
	extend: 'Ext.window.Window',
    xtype: 'materialsReviewSetJudgeWindow',
    controller: 'materialsReviewExamineeController',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'KitchenSink.view.enrollmentManagement.materialsReview.materialsReviewSetJudgeWindowStore',
        'KitchenSink.view.enrollmentManagement.materialsReview.materialsReviewExamineeController'
    ],
    title: "指定评委",
    width: 860,
    height: 400,
    modal:true,
    layout: {
        type: 'fit'
    },
    constructor:function(obj) {
    	this.appRowIndex=obj.appRowIndex;
    	this.appSelList=obj.appSelList;
        this.searchCfg=obj.searchCfg;
        this.operateType=obj.operateType;
    	this.callParent();
    },
    initComponent:function() {

        //var judgeYnywStore = new KitchenSink.view.common.store.appTransStore("TZ_YNYW");

    	Ext.apply(this,{
    		items: [{
    			xtype:'textfield',
    			name:'appRowIndex',
    			value:this.appRowIndex,
    			hidden:true
    		},{
    			xtype:'textfield',
    			name:'appSelList',
    			value:this.appSelList,
    			hidden:true
    		},{
                xtype:'textfield',
                name:'searchCfg',
                value:this.searchCfg,
                hidden:true
            },{
                xtype:'textfield',
                name:'operateType',
                value:this.operateType,
                hidden:true
            },{
                xtype: 'grid',
                autoHeight: true,
                columnLines: true,
                frame: true,
                style: 'border:0',
                plugins: [
                    {
                        ptype: 'gridfilters'
                    }
                ],
                store: {
                    type: 'materialsReviewSetJudgeWindowStore'
                },
                columns: [
                    {
                        dataIndex:'classId',
                        hidden:true
                    },{
                        dataIndex:'batchId',
                        hidden:true
                    },{
                        dataIndex:'judgeOprid',
                        hidden:true
                    },{
                        text: "选择",
                        dataIndex: 'selectFlag',
                        minWidth: 80,
                        xtype: 'checkcolumn',
                        ignoreChangesFlag: true
                    },
                    {
                        text: "评委帐号",
                        dataIndex: 'judgeId',
                        minWidth: 90,
                        flex: 1
                    },
                    {
                        text: "评委姓名",
                        dataIndex: 'judgeName',
                        minWidth: 90,
                        flex: 1
                    },
                    {
                        text: "行业类别",
                        dataIndex: 'judgeIndustry',
                        minWidth:240,
                        xtype: 'templatecolumn',
                        tpl: Ext.create('Ext.XTemplate','{[this.labels(values)]}',{
                            labels: function(values){
                                var labels = "";
                                var val = values.judgeIndustry;
                                if(val.trim() != ""){
                                    var labelArr = val.split(",");
                                    for(var i=0;i<labelArr.length;i++){
                                        labels = labels
                                            + '<span style="margin:0px 2px;padding:3px 5px;background:#CCC7C7;border-radius:5px;">'
                                            + labelArr[i]
                                            + '</span>';
                                    }
                                }
                                return labels;
                            }
                        }),
                        flex: 1
                    },
                    {
                        text: '院内/院外',
                        dataIndex: 'judgeYnywDesc',
                        minWidth:80
                        /*
                        renderer:function(v) {
                            if(v) {
                                var rec = judgeYnywStore.find('TValue',v,0,false,true,true);
                                if(rec>-1) {
                                    return judgeYnywStore.getAt(rec).data.TSDesc;
                                }
                            }
                        }
                        */
                    },
                    {
                        text: "已分配考生数",
                        dataIndex: 'assignedNum',
                        minWidth: 110,
                        flex: 1
                    },
                    {
                        text: "需评审考生数",
                        dataIndex: 'judgeNum',
                        minWidth: 110,
                        flex: 1
                    }
                ]
            }]
    	});
    	this.callParent()
    },
    buttons:[{
        text: "确定",
        iconCls: "ensure",
        handler: 'setJudgeEnsure'
    },{
        text: "关闭",
        iconCls: "close",
        handler: 'setJudgeClose'
    }]
});