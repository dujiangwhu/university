Ext.define('KitchenSink.view.judgesManagement.judgeAccount.judgeCLAccList', {
    extend: 'Ext.grid.Panel',
	requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
		'KitchenSink.view.judgesManagement.judgeAccount.judgeCLAccStore',
		'KitchenSink.view.judgesManagement.judgeAccount.judgeCLAccController',
		'KitchenSink.view.judgesManagement.judgeAccount.judgeCLAccInfoWin',
		'KitchenSink.view.judgesManagement.judgeAccount.judgeCLAccStore'
    ],
    xtype: 'judgeCLAccountMg',
	controller: 'judgeCLAccController',
	reference: "jugClAccMgPanel",
    columnLines: true,
	style:"margin:8px",
	selModel: {
       	type: 'checkboxmodel'
    },
    multiSelect: false,
    title: '评委账号管理',
	header:false,
	frame: true,
    
    initComponent: function () {

		var store;
		var judgeTypeId;

		var comStore = new KitchenSink.view.common.store.comboxStore({
            recname: 'PS_TZ_JUGTYP_TBL',
            condition: {
                TZ_JG_ID: {
                    value: Ext.tzOrgID,
                    operator: '01',
                    type: '01'
                },
                TZ_JUGTYP_STAT: {
                    value: '0',
                    operator: '01',
                    type: '01'
                }
            },
            result: 'TZ_JUGTYP_ID,TZ_JUGTYP_NAME'
        });

		var tzStoreParams = comStore.tzStoreParams;

		Ext.tzLoadAsync(tzStoreParams,function(respData){
			judgeTypeId = respData.PS_TZ_JUGTYP_TBL[0].TZ_JUGTYP_ID;
			store = new KitchenSink.view.judgesManagement.judgeAccount.judgeCLAccStore(judgeTypeId);
		});


        Ext.apply(this, {
           columns: [{ 
               text: '机构ID',
               sortable: true,
               dataIndex: 'jgID',
				width: 200,
				hidden: true
           },{ 
                text: '用户账号',
                sortable: true,
                dataIndex: 'accountNo',
				width: 150
            },{
                text: '用户描述',
                sortable: true,
                dataIndex: 'judgeName',
                minWidth: 150,
				flex: 1
            },/*{
                text: '行业类别',
                sortable: true,
                dataIndex: 'judgeHY',
                minWidth: 200,
				flex: 1
            },*/{
				text: Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_RULE_STD.judgeIndustry","行业类别"),
				// dataIndex: 'judgeIndustry',
				dataIndex: 'judgeHY',
				minWidth:220,
				xtype: 'templatecolumn',
				tpl: Ext.create('Ext.XTemplate','{[this.labels(values)]}',{
					labels: function(values){
						var labels = "";
						var val = values.judgeHY;
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
				}),
				flex:1
			},{
                text: '手机',
                sortable: true,
                dataIndex: 'judPhoneNumber',
                minWidth: 150
            },{
                text: '邮箱',
                sortable: true,
                dataIndex: 'judEmail',
                minWidth: 150
            },{
                text: '评委账号类型id',
                sortable: true,
                dataIndex: 'judType',
                minWidth: 150,
				flex: 1,
				hidden: true
            },{
                text: '评委账号类型',
                sortable: true,
                dataIndex: 'judTypeDesc',
                minWidth: 150,
				flex: 1
            },{
			   text: '评审人数总和',
			   sortable:true,
			   dataIndex:'clpwNum',
			   minWidth:150,
			   flex:1
		   },{
               text: '操作',
			   menuDisabled: true,
               sortable: false,
			   draggable:false,
			   width:80,
			   align: 'center',
               xtype: 'actioncolumn',
			   items:[
					{
						text: '编辑',
						iconCls: 'edit',
						tooltip: '编辑',
						handler: "editCurrRow"
					},
					{
						text: '删除',
						iconCls: 'remove',
						tooltip: '删除',
						handler: "deleteCurrRow"
					},
				   {
					   text: '查看',
					   iconCls: 'view',
					   tooltip: '查看评审人数总和',
					   handler: "viewReviewDetail"
				   }
			   ]
             }],
			store:store,
			bbar: {
				xtype: 'pagingtoolbar',
				pageSize: 10,
				store:store,
				/*
				displayInfo: true,
				displayMsg: '显示{0}-{1}条，共{2}条',
				beforePageText: '第',
				afterPageText: '页/共{0}页',
				emptyMsg: '没有数据显示',
				*/
				plugins: new Ext.ux.ProgressBarPager()
			},
			dockedItems:[
			     		{
			     			xtype:"toolbar",
			     			items:[
			     				{text:"查询",tooltip:"查询数据",iconCls: "query",handler:"searchJudgeAcc"},'-',
			     				{text:"新增",tooltip:"新增数据",iconCls: "add",handler:"addNewJudgeAcc"},'-',
			     				{text:"编辑",tooltip:"编辑数据",iconCls: "edit",handler:"editSelJudgeAcc"},'-',
			     				{text:"删除",tooltip:"批量删除数据",iconCls: "remove",handler:"deleteJudgeBat"},'-',
			     				{
			                     	xtype: 'combobox',
			         	          	fieldLabel:'<div style="font-weight: bold;">'+"筛选规则"+'</div>',
			         	          	labelSeparator:'',
			         	          	//reference: '',
			         	          	//name: '',
			         	          	//style:'margin-top:10px',
			         	          	store:comStore,
			         	          	emptyText : "请选择",
			         	          	valueField: 'TZ_JUGTYP_ID',
			         	          	displayField: 'TZ_JUGTYP_NAME',
			         	          	queryMode:'lcoal',
			         	          	// allowBlank: false,
			         	          	editable:false,
			         	          	//value:'1',
									value:judgeTypeId,
			         	          	listeners:{
			         					change:function(tvType,newValue, oldValue,eOpts){
//			         						var selValueq = this.getValue();//得到valueField的值 
			         						var store = new KitchenSink.view.judgesManagement.judgeAccount.judgeCLAccStore(newValue);
			         						if (newValue==''){
			         							tvType.findParentByType("grid").store.load();
			         							tvType.findParentByType("grid").child("pagingtoolbar").store.load();
			         						}else{
			         							tvType.findParentByType("grid").setStore(store);
			         							tvType.findParentByType("grid").child("pagingtoolbar").setStore(store);
			         						}
			         					}
			         	          	}
			                     },'->', {
									xtype: 'splitbutton',
									text:  "更多操作",
									iconCls: 'list',
									glyph: 61,
									menu: [{
										text: "给评委发送邮件",
										name: 'sendEmail',
										handler: 'sendEmail'
									}, {
										text: "给评委发送短信",
										name: 'sendMessage',
										handler: 'sendMessage'
									}]
								}
			     			]
			     		},
			     		{
			     			xtype:"toolbar",
			     			dock:"bottom",
			     			ui:"footer",
			     			items:[
			     				'->',
			     				{minWidth:80,text:"保存",iconCls:"save",handler:"onSave"},
			     				{minWidth:80,text:"确定",iconCls:"ensure",handler:"onSure"},
			     				{minWidth:80,text:"关闭",iconCls:"close",handler:"onClose"}]
			     		}
			     	]
        });
		
        this.callParent();
    }
});