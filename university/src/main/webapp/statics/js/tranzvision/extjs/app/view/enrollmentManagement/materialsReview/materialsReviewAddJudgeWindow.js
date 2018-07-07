Ext.define('KitchenSink.view.enrollmentManagement.materialsReview.materialsReviewAddJudgeWindow',{
    extend:'Ext.window.Window',
    xtype: 'materialsReviewAddJudgeWindow',
    controller: 'materialsReviewRuleController',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.enrollmentManagement.materialsReview.materialsReviewAddJudgeWindowStore',
        'KitchenSink.view.enrollmentManagement.materialsReview.materialsReviewRuleController'
    ],
    title: Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_ADDPW_STD.title","批量添加评委"),
    width: 650,
    minWidth: 400,
    minHeight: 300,
    maxHeight: 360,
    resizable: true,
    modal:true,
    closeAction: 'destroy',
    ignoreChangesFlag: true,//让框架程序不要提示用户保存的属性设置
    bodyStyle: 'overflow-y:auto;overflow-x:hidden',
    listeners:{
        resize: function(win){
             win.doLayout();
        }
    },
    constructor: function(obj) {
        this.judgeGroupData = obj.judgeGroupData;
        this.callParent();
    },
    initComponent:function() {
        var me = this;

        //评委组
        /*var judgeGroupStore = new KitchenSink.view.common.store.comboxStore({
            recname:'TZ_CLPS_GR_TBL',
            condition:{
                TZ_JG_ID:{
                    value:Ext.tzOrgID,
                    operator:'01',
                    type:'01'
                }
            },
            result:'TZ_CLPS_GR_ID,TZ_CLPS_GR_NAME'
        });*/

        var judgeGroupStore = Ext.create("Ext.data.Store", {
            fields: ["TZ_CLPS_GR_ID", "TZ_CLPS_GR_NAME"],
            data: this.judgeGroupData
        });

        //var judgeYnywStore = new KitchenSink.view.common.store.appTransStore("TZ_YNYW");

        var store = new KitchenSink.view.enrollmentManagement.materialsReview.materialsReviewAddJudgeWindowStore();


        Ext.apply(this, {
            items: [{
                xtype: 'form',
                layout: {
                    type: 'vbox',
                    align: 'stretch'
                },
                border: false,
                bodyPadding: 10,
                ignoreLabelWidth: true,
                fieldDefaults: {
                    msgTarget: 'side',
                    labelStyle: 'font-weight:bold'
                },
                items: [{
                    layout: 'column',
                    items: [{
                        xtype: 'combobox',
                        labelWidth: 80,
                        width: 200,
                        labelSeparator: '',
                        fieldLabel: '评委账号',
                        store:{
                            fields: [{name:'transId'},{name:'transDesc'}],
                            data: [
                                ["07","包含"],
                                ["10","在......之内"]
                            ]
                        },
                        displayField: 'transDesc',
                        valueField: 'transId',
                        value:"07",
                        name: 'judgeId-operator'
                    }, {
                        xtype: 'textfield',
                        columnWidth: 1,
                        hideEmptyLabel: true,
                        name: 'judgeId-value',
                        listeners: {
                            specialkey: function (textfield, e) {
                                if (e.getKey() == Ext.EventObject.ENTER) {
                                    me.searchPrompt();
                                }
                            }
                        }
                    }]
                }, {
                    layout: 'column',
                    items: [{
                        xtype: 'combobox',
                        labelWidth: 80,
                        width: 200,
                        labelSeparator: '',
                        fieldLabel: '评委姓名',
                        store:{
                            fields: [{name:'transId'},{name:'transDesc'}],
                            data: [
                                ["07","包含"],
                                ["10","在......之内"]
                            ]
                        },
                        displayField: 'transDesc',
                        valueField: 'transId',
                        value:"07",
                        name: 'judgeName-operator'
                    }, {
                        xtype: 'textfield',
                        columnWidth: 1,
                        hideEmptyLabel: true,
                        name: 'judgeName-value',
                        listeners: {
                            specialkey: function (textfield, e) {
                                if (e.getKey() == Ext.EventObject.ENTER) {
                                    me.searchPrompt();
                                }
                            }
                        }
                    }]
                }],
                buttonAlign: 'left',
                buttons: [{
                    text: '搜索',
                    iconCls: "search",
                    handler: function (btn) {
                        me.searchPrompt();
                    }
                }, {
                    text: '清除',
                    iconCls: "clean",
                    handler: function (btn) {
                        //搜索信息表单
                        var form = btn.findParentByType("form").getForm();
                        //重置表单
                        form.reset();
                    }
                }]
            }, {

                xtype: 'form',
                name: 'setGroupForm',
                autoHeight: true,
                items: [{
                    xtype: 'grid',
                    title: "搜索结果列表",
                    height: 'auto',
                    minHeight: 180,
                    //title: '搜索结果列表',
                    columnLines: true,
                    selModel: {
                        type: 'checkboxmodel',
                        checkOnly:true
                    },
                    /*plugins: [{
                        ptype: 'cellediting',
                        clicksToEdit: 1
                    }],
                    dockedItems: {
                        xtype: 'toolbar',
                        items: [{
                            xtype: 'combobox',
                            margin: '0 10px 0 0',
                            labelStyle: 'font-weight:bold',
                            labelWidth: 120,
                            hideLabel: true,
                            name: 'judgeGroupSet',
                            store: judgeGroupStore,
                            displayField: "TZ_CLPS_GR_NAME",
                            valueField: "TZ_CLPS_GR_ID",
                            editable: false,
                            queryMode: 'local'
                        }, {
                            text: '批量设置选中教师评委组',
                            iconCls: 'set',
                            tooltip: '批量设置选中教师评委组',
                            handler: 'setJudgeGroupBatch'
                        }]
                    },*/
                    columns: [{
                        xtype: 'rownumberer',
                        width: 30
                    }, {
                        dataIndex: 'judgeOprid',
                        hidden: true
                    }, {
                        text: "评委账号",
                        dataIndex: 'judgeId',
                        minWidth: 100,
                        flex: 1
                    }, {
                        text: "评委姓名",
                        dataIndex: 'judgeName',
                        minWidth: 110,
                        flex: 1
                    }, {
                        text: '行业类别',
                        dataIndex: 'judgeIndustry',
                        minWidth:240,
                        xtype: 'templatecolumn',
                        tpl: Ext.create('Ext.XTemplate','{[this.labels(values)]}',{
                            labels: function(values){
                                var labels = "";
                                var val = values.judgeIndustry;
                                if(val.trim() != ""){
                                    var labelArr = val.split("|");
                                    for(var i=0;i<labelArr.length;i++){
                                        labels = labels
                                            + '<span style="margin:0px 2px;padding:3px 5px;background:#CCC7C7;border-radius:5px;">'
                                            + labelArr[i]
                                            + '</span>';
                                    }
                                }
                                return labels;
                            }
                        })
                    },{
                        text: '院内/院外',
                        dataIndex: 'judgeYnywDesc',
                        minWidth:110
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
                    }/*{
                        text: "评委组",
                        dataIndex: 'judgeGroup',
                        minWidth: 100,
                        flex: 1,
                        editor: {
                            xtype: 'combobox',
                            editable: false,
                            emptyText: '请选择...',
                            valueField: 'TZ_CLPS_GR_ID',
                            displayField: 'TZ_CLPS_GR_NAME',
                            store: judgeGroupStore,
                            queryMode: 'local',
                            triggerAction: 'all'
                        },
                        renderer: function (v) {
                            if (v) {
                                var rec = judgeGroupStore.find('TZ_CLPS_GR_ID', v, 0, false, true, true);
                                if (rec > -1) {
                                    return judgeGroupStore.getAt(rec).data.TZ_CLPS_GR_NAME;
                                } else {
                                    return "请选择..."
                                }
                            }
                        }
                    }*/],
                    store: store,
                    bbar: {
                        xtype: 'pagingtoolbar',
                        pageSize: 500,
                        listeners: {
                            afterrender: function (pbar) {
                                var grid = pbar.findParentByType("grid");
                                pbar.setStore(grid.store);
                            }
                        },
                        displayInfo: true,
                        displayMsg: '显示{0}-{1}条，共{2}条',
                        beforePageText: '第',
                        afterPageText: '页/共{0}页',
                        emptyMsg: '没有数据显示',
                        plugins: new Ext.ux.ProgressBarPager()
                    }
                }],
                buttons: [{
                    text: '确定',
                    iconCls: "ensure",
                    handler: function (btn) {
                        //获取窗口
                        var win = btn.findParentByType("window");
                        //选中行
                        var selection = win.child("form[name=setGroupForm]").down("grid").getSelectionModel().getSelection();
                        //选中行长度
                        var checkLen = selection.length;
                        if (checkLen == 0) {
                            Ext.Msg.alert("提示", "未选中记录");
                            return;
                        }

                        //回调函数
                        var activeTab = Ext.getCmp('tranzvision-framework-content-panel').getActiveTab(),
                            targetForm = activeTab.down("form").getForm(),
                            targetStore = activeTab.down("grid[name=materialJudgeGrid]").getStore();
                        var classId = targetForm.findField("classId").getValue();
                        var batchId = targetForm.findField("batchId").getValue();

                        var existFlag = false;

                        for (var i = 0; i < selection.length; i++) {
                            var judgeOprid =selection[i].data.judgeOprid;
                            //查询是否存在，如果存在则不插入
                            var find = targetStore.find("judgeOprid",judgeOprid,0, false, true, true);
                            if(find==-1) {
                                var r = Ext.create('KitchenSink.view.enrollmentManagement.materialsReview.materialsReviewJudgeModel', {
                                    classId: classId,
                                    batchId: batchId,
                                    judgeOprid: judgeOprid,
                                    judgeId: selection[i].data.judgeId,
                                    judgeName: selection[i].data.judgeName,
                                    judgeIndustry : selection[i].data.judgeIndustry,
                                    judgeMobile: selection[i].data.judgeMobile,
                                    judgeEmail: selection[i].data.judgeEmail,
                                    judgeGroup: selection[i].data.judgeGroup,
                                    judgeExamineeNum: 200,
                                    judgeStatus: "A",
                                    judgeStatusDesc: "正常",
                                    judgeYnyw: selection[i].data.judgeYnyw,
                                    judgeYnywDesc: selection[i].data.judgeYnywDesc
                                });

                                targetStore.insert(0, r);
                            } else {
                                existFlag=true;
                            }
                        }

                        if(existFlag) {
                            Ext.Msg.alert("提示","在您所选的记录中，有评委已经存在于名单中");
                        }

                        //表单
                        var form = win.child("form").getForm();
                        //重置表单
                        form.reset();
                        //关闭窗口
                        win.close();
                    }
                }, {
                    text: '关闭',
                    iconCls: "close",
                    handler: function (btn) {
                        //获取窗口
                        var win = btn.findParentByType("window");
                        //关闭窗口
                        win.close();
                    }
                }]
            }]
        });

        this.callParent();
    },
    //搜索
    searchPrompt: function(){
        //搜索信息表单
        var form = this.child("form").getForm();
        //搜索结果列表
        var grid = this.down("grid");
        //搜索结果数据源
        var store = grid.getStore();
        //搜索条件
        var judgeIdOperator = form.findField("judgeId-operator").getValue();
        var judgeIdValue = form.findField("judgeId-value").getValue();
        var judgeNameOperator = form.findField("judgeName-operator").getValue();
        var judgeNameValue = form.findField("judgeName-value").getValue();

        //交互参数
        store.tzStoreParams = '{"judgeIdOperator":"'+judgeIdOperator+'","judgeIdValue":"'+judgeIdValue+'","judgeNameOperator":"'+judgeNameOperator+'","judgeNameValue":"'+judgeNameValue+'"}';
        store.load();
    }
});
