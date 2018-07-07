Ext.define('KitchenSink.view.enrollmentManagement.materialsReview.materialsReviewSchedule', {
    extend: 'Ext.panel.Panel',
    xtype: 'materialsReviewSchedule',
    controller: 'materialsReview',
    requires: [
    	'Ext.data.*', 
    	'Ext.grid.*', 
    	'Ext.util.*', 
    	'Ext.toolbar.Paging', 
    	'Ext.ux.ProgressBarPager', 
    	'KitchenSink.AdvancedVType', 
    	'Ext.ux.MaximizeTool', 
    	'tranzvision.extension.grid.column.Link', 
    	'KitchenSink.view.enrollmentManagement.materialsReview.materialsReviewController', 
    	'KitchenSink.view.enrollmentManagement.materialsReview.materialsReviewScheduleJudgeStore', 
    	'KitchenSink.view.enrollmentManagement.materialsReview.materialsReviewScheduleAppJudgeWindow', 
    	'KitchenSink.view.enrollmentManagement.materialsReview.materialsReviewScheduleAppsStore'
    ],
    title: '材料评审进度管理',
    scrollable: false,
    //bodyStyle: 'overflow-y:auto;overflow-x:hidden',
    bodyStyle: 'overflow-y:auto;overflow-x:auto',
    constructor: function(classID, batchID, adminFlag) {
        this.classID = classID;
        this.batchID = batchID;
        this.adminFlag = adminFlag;
        this.callParent();
    },
    initComponent: function() {
        var classID = this.classID;
        var batchID = this.batchID;
        var adminFlag = this.adminFlag;
        
        var statisticsGridDataModel;
        var statisticsGoalGridDataModel;
        var msyrtislReviewScheduleChartStore = new Ext.data.JsonStore({
            fields: ['name', 'data1'],
            data: []
        });

        var msyrtislReviewScheduleChartStore2 = new Ext.data.JsonStore({
            fields: [],
            data: []
        });
        var materialsReviewScheduleAppsStore = new KitchenSink.view.enrollmentManagement.materialsReview.materialsReviewScheduleAppsStore();
        var materialsReviewScheduleJudgeStore = new KitchenSink.view.enrollmentManagement.materialsReview.materialsReviewScheduleJudgeStore();
       

        // 柱状图和曲线图最大值最小值
        var columnMaxCount = 100;
        var columnMinCount = 0;
        var lineMaxCount = 100;
        var lineMinCount = 0;
        var chartfields;

        var tzParams = '{"ComID":"TZ_REVIEW_CL_COM","PageID":"TZ_CLPS_SCHE_STD","OperateType":"QG","comParams":{"type":"getPwDfData" , "classID":"' + classID + '","batchID":"' + batchID + '"}}';

        Ext.tzLoadAsync(tzParams, function(respData) {
            //统计信息
            var tmpArray = respData.pw_dfqk_grid;
           
            statisticsGridDataModel = {
                gridFields: [],
                gridColumns: [],
                gridData: []
            };          
            
            var tmpObject = {
                columns: []
            };
            for (var i = 0; i < tmpArray.length; i++) {
                var colName = '00' + (i + 1);
                colName = 'col' + colName.substr(colName.length - 2);
                
                var tmpColumn = {
                    text: tmpArray[i][colName],
                    sortable: false,
                    flex:1,
                    dataIndex: colName
                };

                statisticsGridDataModel['gridColumns'].push(tmpColumn);
                statisticsGridDataModel['gridFields'].push({
                    name: colName
                });
            }

            tmpArray = respData.pw_dfqk_grid_data;

            for (var i = 0; i < tmpArray.length; i++) {
                var tmpdataArray = tmpArray[i].field_value;
                var dataRow = [];
                for (var j = 0; j < tmpdataArray.length; j++) {
                    var colName = '00' + (j + 1);
                    colName = 'col' + colName.substr(colName.length - 2);
                    dataRow.push(tmpdataArray[j][colName]);
                }
                statisticsGridDataModel['gridData'].push(dataRow);
            }            
        });

        //考生名单，评委按钮，根据用户角色不同而不同
        var dockedItems;
        var applicantsColumns;
        if(adminFlag=="S"){
        	applicantsColumns = [{
                text: "项目名称",
                dataIndex: 'classID',
                hidden:true
            },
            {
                text: "批次名称",
                dataIndex: 'batchID',
                hidden:true
            },
            {
                text: "人员编号",
                dataIndex: 'oprID',
                hidden:true
            },
            {
                text: "报名表编号",
                dataIndex: 'insID',
                hidden:true
            },
            {
                text: "面试申请号",
                dataIndex: 'mssqh',
                align: 'center',
                minWidth: 120
            },
            {
                text: "姓名",
                dataIndex: 'name',
                align: 'center',
                minWidth: 120,
                xtype: 'linkcolumn',
                handler: 'viewThisApplicationForm',
                renderer: function(v) {
                    this.text = v;
                    return;
                }

            },
            {
                text: "性别",
                dataIndex: 'gender',
                align: 'center',
                minWidth: 30,
                renderer: function(v) {
                    switch (v) {
                    case 'M':
                        return '男';
                    case 'F':
                        return '女';
                    }
                }
            },            
            {
                text: "评委信息",
                dataIndex: 'judgeInfo',
                align: 'center',
                name: 'judgeInfo',
                // id:'judgeInfo',
                width: 100,
                flex: 1,
                renderer: function(v) {
                    if (v) {
                        return '<a class="tz_materialsReview_app" href = "javaScript:void(0)" >' + v + '</a>';
                    } else {
                        return "";
                    }
                },
                listeners: {
                    click: 'viewJudge'
                }
            },
            {
                text: "评审状态",
                dataIndex: 'judgeStatus',
                align: 'center',
                width: 200
            }];
        	
        	dockedItems = [{
        		xtype: "toolbar",
        		items: [
                {
                    text: "设置提交状态为未提交",
                    tooltip: "设置提交状态为未提交",
                    handler: "setNoSubmit"
                },"-",
                {
                    text: "打印评分总表",
                    tooltip: "打印评分总表",
                    handler: "printPFZB"
                }]
            }]        
        	
        }else{
        	applicantsColumns = [{
                text: "班级",
                dataIndex: 'classID',
                hidden:true
            },
            {
                text: "批次",
                dataIndex: 'batchID',
                hidden:true
            },
            {
                text: "人员编号",
                dataIndex: 'oprID',
                hidden:true
            },
            {
                text: "报名表编号",
                dataIndex: 'insID',
                hidden:true
            },
            {
                text: "面试申请号",
                dataIndex: 'mssqh',
                align: 'center',
                minWidth: 120
            },
            {
                text: "姓名",
                dataIndex: 'name',
                align: 'center',
                minWidth: 120,
                xtype: 'linkcolumn',
                handler: 'viewThisApplicationForm',
                renderer: function(v) {
                    this.text = v;
                    return;
                }

            },
            {
                text: "性别",
                dataIndex: 'gender',
                align: 'center',
                minWidth: 30,
                renderer: function(v) {
                    switch (v) {
                    case 'M':
                        return '男';
                    case 'F':
                        return '女';
                    }
                }
            },
            {
                text: "评委信息",
                dataIndex: 'judgeInfo',
                align: 'center',
                name: 'judgeInfo',
                width: 100,
                flex: 1
            },
            {
                text: "评审状态",
                dataIndex: 'judgeStatus',
                align: 'center',
                width: 200
            }];
        	
        	dockedItems = [{
        		xtype: "toolbar",
        		items: [
                {
                    text: "设置提交状态为未提交",
                    tooltip: "设置提交状态为未提交",
                    handler: "setNoSubmit"
                }]
            }];
        }
        
        Ext.apply(this, {
            items: [{
                xtype: 'form',
                name: 'materialsProgressForm',
                layout: {
                    type: 'vbox',
                    align: 'stretch'
                },
                border: false,
                bodyPadding: 10,
                bodyStyle: 'overflow-y:auto;overflow-x:hidden',

                fieldDefaults: {
                    labelWidth: 110,
                    labelStyle: 'font-weight:bold'
                },

                items: [{
                    xtype: 'textfield',
                    name: 'classID',
                    hidden: true
                },
                {
                    xtype: 'textfield',
                    name: 'batchID',
                    hidden: true
                },
                {
                    xtype: 'textfield',
                    name: 'requiredCount',
                    hidden: true
                },
                {
                    xtype: 'textfield',
                    name: 'reviewAllCount',
                    hidden: true
                },
                {
                    xtype: 'textfield',
                    fieldLabel: "项目名称",
                    name: 'className',
                    fieldStyle: 'background:#F4F4F4',
                    readOnly: true,
                    labelWidth: 150
                },
                {
                    xtype: 'textfield',
                    fieldLabel: "批次名称",
                    name: 'batchName',
                    fieldStyle: 'background:#F4F4F4',
                    readOnly: true,
                    labelWidth: 150
                },
                {
                    xtype: 'numberfield',
                    fieldLabel: "报考考生数量",
                    name: 'totalStudents',
                    fieldStyle: 'background:#F4F4F4',
                    readOnly: true,
                    labelWidth: 150
                },
                {
                    xtype: 'numberfield',
                    fieldLabel: "已提交考生数量",
                    name: 'subTotalStudents',
                    fieldStyle: 'background:#F4F4F4',
                    readOnly: true,
                    labelWidth: 150
                },
                {
                    xtype: 'numberfield',
                    fieldLabel: "已选择材料评审考生",
                    name: 'materialStudents',
                    fieldStyle: 'background:#F4F4F4',
                    readOnly: true,
                    labelWidth: 150
                },
                {
                    xtype: 'hiddenfield',
                    fieldLabel: "是否合格",
                    name: 'strWaring'
                },                
                {
                    xtype: 'fieldset',
                    title: '阶段状态',
                    collapsible: true,
                    defaults: {
                        anchor: '100%',
                        layout: {
                            type: 'hbox',
                            defaultMargins: {
                                top: 0,
                                right: 5,
                                bottom: 0,
                                left: 0
                            }
                        }
                    },
                    items: [{
                        layout: {
                            type: 'column'
                        },
                        padding: '0 0 8px 0',
                        items: [
                        {
                            margin: '8px',
                            xtype: 'displayfield',
                            // style:'margin-left:1px',
                            value: "<b>当前评审状态是：</b>",
                            // hideLabel:
                            // true,
                            labelWidth: 70
                        },
                        {
                            margin: '8px',
                            xtype: 'displayfield',
                            name: 'status',
                            style: 'display:inline-block',
                            readOnly: true
                        },
                        {
                            style: 'margin-left:15px;margin-top:8px;',
                            xtype: 'button',
                            flagType: 'positive',
                            name: 'startNewReview',
                            defaultColor: '',
                            setType: 0,
                            text: '开启评审',
                            handler: 'startNewReview',
                            width: 90
                        },
                        {
                            style: 'margin-left:15px;margin-top:8px;',
                            xtype: 'button',
                            text: '关闭评审',
                            defaultColor: '',
                            name: 'closeReview',
                            flagType: 'positive',
                            setType: 0,
                            handler: 'closeReview',
                            width: 90
                        }]
                    }]
                },
                {
                    xtype: 'tabpanel',
                    frame: true,
                    activeTab: 0,
                    scrollable: true,
                    plain: false,
                    resizeTabs: true,
                    defaults: {
                        autoScroll: true
                    },
                    listeners: {
                        tabchange: function(tabPanel, newCard, oldCard) {
                            if (newCard.name == "materialsStudentGrid") {
                                /*var form = tabPanel.findParentByType('materialsReviewSchedule').child('form').getForm();
                                var store = newCard.store;
                                var classID = form.findField('classID').getValue();
                                var batchID = form.findField('batchID').getValue();
                                if (store.isLoaded() == false) {
                                    var tzParams = '{"ComID":"TZ_REVIEW_CL_COM","PageID":"TZ_CLPS_SCHE_STD","OperateType":"QG",' + '"comParams":{"type":"stuList","classID":"' + classID + '","batchID":"' + batchID + '"}}';
                                    Ext.tzLoad(tzParams,
                                    function(respData) {
                                        // store.loadData(respData.root);
                                    });
                                    this.doLayout();
                                }*/
                            }
                            if (newCard.name == "judgeInfoForm") {
                                var form = tabPanel.findParentByType('materialsReviewSchedule').child('form').getForm();
                                var store = newCard.child('grid').store;
                                //console.log(store);
                                var classID = form.findField('classID').getValue();
                                var batchID = form.findField('batchID').getValue();
                                if (store.isLoaded() == false) {
                                    var tzParams = '{"ComID":"TZ_REVIEW_CL_COM","PageID":"TZ_CLPS_SCHE_STD","OperateType":"QG",' + '"comParams":{"type":"judgeInfo","classID":"' + classID + '","batchID":"' + batchID + '"}}';
                                    Ext.tzLoad(tzParams,
                                    function(respData) {
                                        // store.loadData(respData.root);
                                    });

                                    this.doLayout();
                                }
                            }
                        }
                    },
                    items: [{
                        title: '评委信息',
                        xtype: 'form',
                        name: 'judgeInfoForm',
                        style: 'margin-left:0',
                        layout: {
                            type: 'vbox',
                            align: 'stretch'
                        },
                        items: [{
                            layout: {
                                type: 'column'
                            },
                            items: [{
                                margin: '8px 8px 0px 8px',
                                xtype: 'displayfield',
                                value: "<b>要求每位考生被</b>",
                                labelWidth: 1
                            },
                            {

                                xtype: 'displayfield',
                                margin: '8px 8px 0px 8px',
                                readOnly: true,
                                name: 'reviewCount',
                                style: 'display:inline-block'
                            },
                            {
                                margin: '8px 8px 0px 8px',
                                xtype: 'displayfield',
                                // style:'margin-left:1px',
                                value: "<b>个评委审批</b>",
                                // hideLabel:
                                // true,
                                labelWidth: 70
                            }]
                        },
                        {
                            layout: {
                                type: 'column'
                            },
                            items: [{
                                margin: '0px 8px',
                                xtype: 'displayfield',
                                value: "<b>要求评审人次：</b>",
                                labelWidth: 1
                            },
                            {

                                xtype: 'displayfield',
                                margin: '0px 8px',
                                readOnly: true,
                                name: 'requiredReviewCount',
                                style: 'display:inline-block'
                            },
                            {
                                margin: '0px 8px',
                                xtype: 'displayfield',
                                // style:'margin-left:1px',
                                value: "<b>人次</b>",
                                // hideLabel:
                                // true,
                                labelWidth: 70
                            }]
                        },                       
                        {
                            xtype: 'grid',
                            minHeight: 150,
                            autoHeight: true,
                            name: 'materialsJudgeGrid',
                            border: false,
                            plugins: {
                                ptype: 'cellediting',
                                clicksToEdit: 1
                            },
                            store: {
                                type: 'materialsReviewScheduleJudgeStore'
                            },
                            multiSelect: true,
                            selModel: {
                                selType: 'checkboxmodel',
                                checkOnly:true
                            },
                            dockedItems: dockedItems,
                            scrollable:false,
                            columns: [{
                                width: 1
                            },
                            {
                                text: "选择",
                                xtype: 'rownumberer',
                                width: 50,
                                align: 'center'
                            },
                            {
                                text: "评委帐号",
                                dataIndex: 'judgeID',
                                width: 150,
                                align: 'center'
                            },
                            {
                                text: "评委姓名",
                                dataIndex: 'judgeName',
                                align: 'center',
                                flex: 1
                            },
                            {
                                text: "评审考生人数",
                                align: 'center',
                                dataIndex: 'lower',
                                width: 140
                            }, {
                                text: "已评审考生人数",
                                dataIndex: 'hasSubmited',
                                align: 'center',
                                width: 140
                            },
                            {
                                text: "提交状态",
                                dataIndex: 'submitYN',
                                width: 100,
                                align: 'center',
                                renderer: function(v) {
                                    if (v == 'Y') {
                                        return '已提交'
                                    }
                                    if (v == 'N') {
                                        return '未提交'
                                    }
                                    if (v == 'C') {
                                        return '被撤销'
                                    }
                                    if (v == '') {
                                        return '未提交'
                                    }
                                }
                            }]
                        }]
                    },
                    {
                        xtype: 'grid',
                        title: '考生名单',
                        minHeight: 260,
                        name: 'materialsStudentGrid',
                        reference: 'materialsReviewAppsGrid',
                        columnLines: true,
                        autoHeight: true,
                        /*selModel: {
                            type: 'checkboxmodel'
                        },*/
                        /*listeners: {
                            activate: 'stuListActive'
                        },*/
                        /*store: {
                            type: 'materialsReviewScheduleAppsStore'
                        },*/
                        plugins: [{
                            ptype: 'cellediting',
                            pluginId: 'judgeCellEditing',
                            clicksToEdit: 1,
                            listeners: {
                                beforeedit: function(editor, context, eOpts) {
                                    if (context.field == "judgeID" && context.value.length > 0 && !context.record.isModified('judgeID')) {
                                        return false;
                                    }
                                }
                            }
                        }],
                        /*dockedItems: dockedItems,*/
                        columns: applicantsColumns,
                        store:materialsReviewScheduleAppsStore,
                        bbar: {
                            xtype: 'pagingtoolbar',
                            pageSize: 10,
                            listeners:{
                                afterrender: function(pbar){
                                    var grid = pbar.findParentByType("grid");
                                    pbar.setStore(grid.store);
                                }
                            },
                            plugins: new Ext.ux.ProgressBarPager()
                        }
                    },                    
                    {
                        xtype: 'form',
                        title: '统计信息',
                        autoHeight: true,
                        name: 'statisticalInfoForm',
                        items: [
                        	Ext.create('Ext.grid.Panel', {
	                            store: Ext.create('Ext.data.ArrayStore', {
	                                fields: statisticsGridDataModel['gridFields'],
	                                data: statisticsGridDataModel['gridData']
	                            }),
	                            minHeight: 180,
	                            name:'statisticalGrid',
	                            margin:'5 0',
	                            selModel: {
	                                type: 'checkboxmodel',
                                    checkOnly:true
	                            },
	                            columns: statisticsGridDataModel['gridColumns'],
	                            header: false,
	                            border: false,
	                            listeners:{
	                            	beforeselect:function(_this,record,index,opts){
	                            		var data = record.getData();
	                            		var pwId = data["col01"];
	                            		if(pwId!=undefined&&pwId==''){
	                            			return false;
	                            		}
	                            	}
	                            },
	                            /*dockedItems: [{
	                                xtype: "toolbar",
	                                items: [{
	                                    text: "计算选中评委的标准评分分布",
	                                    tooltip: "计算选中评委的标准评分分布",
	                                    handler:'calcuScoreDist'
	                                }]
	                            }],*/
	                            viewConfig: {
	                                enableTextSelection: true
	                            }
                        	})]
                    }]
                }]
            }]

        });
        this.callParent();
    },
    buttons: [{
        text: '保存',
        iconCls: "save",
        handler: 'onScheduleSave'
    },
    {
        text: '确定',
        iconCls: "ensure",
        handler: 'onScheduleEnsure'
    },
    {
        text: '关闭',
        iconCls: "close",
        handler: 'onScheduleClose'
    }]
});