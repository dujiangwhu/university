Ext.define('KitchenSink.view.enrollmentManagement.materialsReview.materialsReviewExaminee',{
	extend: 'Ext.panel.Panel',
	xtype: 'materialsReviewExaminee',
	controller: 'materialsReviewExamineeController',
    requires: [
    	'Ext.data.*', 
    	'Ext.grid.*', 
    	'Ext.util.*',
		'Ext.grid.filters.Filters',
    	'Ext.toolbar.Paging', 
    	'Ext.ux.ProgressBarPager',
    	'KitchenSink.view.enrollmentManagement.materialsReview.materialsReviewExamineeStore',
    	'KitchenSink.view.enrollmentManagement.materialsReview.materialsReviewExamineeController',
    	'KitchenSink.view.enrollmentManagement.materialsReview.export.exportExcelWindow'
    ],
	//title: Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_KS_STD.title","材料评审考生名单"),
	title: "材料评审考生名单",
    bodyStyle: 'overflow-y:auto;overflow-x:hidden',
	actType:'',
	constructor: function(obj) {
		this.classId = obj.classId;
		this.batchID = obj.batchId;
		this.hideFlag = obj.hideFlag;
		this.callParent();
	},
	viewModel:{
		data:{
			tips:{
				total:0,
				assigned:0,
				unassign:0
			}
		}
	},
	initComponent: function() {

		var me = this;

    	//考生名单store
    	var examineeStore = new KitchenSink.view.enrollmentManagement.materialsReview.materialsReviewExamineeStore();

    	Ext.apply(this,{
			items:[{
    			xtype:'form',
    			reference:'materialsReviewExamineeForm',
    			layout:{
    				type:'vbox',
    				align:'stretch'
    			},
    			border:false,
    			bodyPadding:10,
    			bodyStyle:'overflow-y:auto;overflow-x:hidden',
    			fieldDefaults:{
    				msgTarget:'side',
    				labelWidth:110,
    				labelStyle:'font-weight:bold'
    			},
				items:[{
    				xtype:'textfield',
    				name:'classId',
    				hidden:true
    			},{
    				xtype:'textfield',
    				name:'batchId',
    				hidden:true
    			},{
    				xtype:'textfield',
    				name:'dqpsLunc',
    				hidden:true
    			},{
    				xtype:'textfield',
    				name:'dqpsStatus',
    				hidden:true
    			},{
					xtype:'numberfield',
					name:'judgeNumSet',
					hidden:true
				},{
    				xtype:'textfield',
    				fieldLabel:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_KS_STD.className","项目名称"),
    				name:'className',
    				fieldStyle:'background:#F4F4F4',
    				readOnly:true
    			},{
    				xtype:'textfield',
    				fieldLabel:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_KS_STD.batchName","批次名称"),
    				name:'batchName',
    				fieldStyle:'background:#F4F4F4',
    				readOnly:true
    			},{
    				xtype:'numberfield',
    				fieldLabel:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_KS_STD.bkksNum","报考考生数量"),
    				name:'bkksNum',
    				fieldStyle:'background:#F4F4F4',
    				readOnly:true
    			},{
					xtype:'numberfield',
					fieldLabel:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_RULE_STD.submitKsNum","已提交考生数量"),
					name:'submitKsNum',
					fieldStyle:'background:#F4F4F4',
					readOnly:true
				},{
    				xtype:'numberfield',
    				fieldLabel:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_KS_STD.clpsksNum","材料评审考生"),
    				name:'clpsksNum',
    				fieldStyle:'background:#F4F4F4',
    				readOnly:true,
					value:0
    			},{
					xtype: 'component',
					style:{
						marginBottom: '10px',
						marginTop:'10px'
					},
					bind:{
						html: '<span style="color: #666; font-weight:bold;font-size:13px">共有考生 {tips.total}人，已分配评委 {tips.assigned} 人，未分配评委 {tips.unassign} 人</span>'
					}
				},{
    				xtype:'grid',
    				title:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_KS_STD.ksGridTitle","考生名单"),
    				minHeight:260,
    				name:'materialsReviewExamineeGrid',
    				columnLines:true,
    				autoHeight:true,
    				frame:true,
    				selModel:{
    					selType:'checkboxmodel',
						checkOnly:true
    				},
    				dockedItems:[{
    					xtype:'toolbar',
    					items:[{
    						text:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_STD.query","查询"),
    						tooltip:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_STD.query","查询"),
    						iconCls:'query',
    						handler:'queryExaminee'
    					},"-",{
    						text:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_STD.add","新增"),
    						tooltip:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_STD.add","新增"),
    						iconCls:'add',
    						handler:'addExaminee'
    					},"-",{
    						text:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_STD.remove","删除"),
    						tooltip:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_STD.remove","删除"),
    						iconCls:'remove',
    						handler:'removeExaminee'
    					},"-",{
    						text:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_STD.people","指定评委"),
    						tooltip:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_STD.people","指定评委"),
    						iconCls:'people',
							handler:'setJudgeForExaminee'
    					},"-",{
							text:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_STD.peopleAdd","追加评委"),
							tooltip:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_STD.peopleAdd","追加评委"),
							iconCls:'peoples',
							handler:'addJudgeForExaminee'
						}/*,"-",{
							text:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_STD.peopleAutopeopleAuto","自动分配评委"),
							tooltip:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_STD.peopleAuto","自动分配评委"),
							iconCls:'people',
							handler:'autoJudgeForExaminee'
						}*/,"-",{
							xtype:'displayfield',
							value:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_KS_STD.setPageSize1","每页显示"),
							hideLabel:true
						},{
							xtype:'numberfield',
							fieldLabel:"",
							name:'pageSizeSet',
							allowDecimals:false,
							minValue:1,
							value:50,
							style:'margin-left:5px;margin-right:5px;',
							negativeText:'每页条数不能为负数',
							nanText:'{0}不是有效的数字',
							width:100,
							hideLabel:true
						},{
							xtype:'displayfield',
							value:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_KS_STD.setPageSize2","条"),
							hideLabel:true,
							style:'margin-right:10px;'
						},{
							text:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_KS_STD.addJudge","设置"),
							iconCls:'set',
							tooltip:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_KS_STD.addJudge","设置"),
							handler:"setPageSize"
						},"->",{
    						xtype:'splitbutton',
							text:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_STD.more","更多操作"),
							iconCls:'list',
							glyph:61,
							/*修改为批处理导出，张浪，20170516
							menu:[{
								text:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_KS_STD.exportAllExcel","导出查询结果评议数据"),
    							name:'excel',
    							handler:'exportAllExcel'
							},{
								text:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_KS_STD.exportExcel","导出选中考生评议数据"),
    							name:'excel',
    							handler:'exportExcel'
							}]
							*/
							menu:[{
								text:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_KS_STD.exportExcel","导出考生评审数据"),
    							iconCls: 'excel',
    							menu:[{
    								text:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_KS_STD.exportSelectedExcel","导出选中考生评审数据"),
        							handler:'exportSelectedExcel'
    							},{
    								text:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_KS_STD.exportSearchExcel","导出查询结果考生评审数据"),
        							handler:'exportSearchExcel'
    							}]
							},{
								text:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_KS_STD.download","查看历史导出并下载"),
								iconCls:'download',
    							handler:'downloadHisExcel'
							}],
							hidden:this.hideFlag
    					}]
    				}],
    				plugins:[{
    					ptype:'gridfilters'
    				}],
    				columns:[{
    					xtype:'rownumberer',
    					width:50,
    					align:'center'
    				},{
    					text:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_KS_STD.name","考生姓名"),
    					dataIndex:'name',
    					width:100,
    					filter:{
    						type:'string'
    					}
    				},{
    					text:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_KS_STD.mssqh","面试申请号"),
    					dataIndex:'mssqh',
    					width:100,
    					filter:{
    						type:'string'
    					}
    				},/*{
    					text:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_KS_STD.appinsId","报名表编号"),
    					dataIndex:'appinsId',
    					width:100,
    					filter:{
    						type:'string'
    					}
    				},*/{
    					text:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_KS_STD.sex","性别"),
    					dataIndex:'sexDesc',
    					width:70,
    					filter:{
    						type:'list'
    					}
    				},{
						text:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_KS_STD.industry","所属行业"),
						dataIndex:'industryDesc',
						width:180,
						filter:{
							type:'list'
						}
					},{
    					text:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_KS_STD.judgeList","评委"),
    					dataIndex:'judgeList',
    					width:120,
    					flex:1
    				}/*,{
    					text:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_KS_STD.judgeTotal","评委总分"),
    					dataIndex:'judgeTotal',
    					width:120,
						hidden:true
    				}*/,{
    					text:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_KS_STD.reviewStatusDesc","评审状态"),
    					dataIndex:'reviewStatusDesc',
    					width:120,
    					filter:{
    						type:'list'
    					}
    				}/*,{
    					text:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_KS_STD.interviewStatusDesc","面试资格"),
    					dataIndex:'interviewStatusDesc',
    					width:120,
    					filter:{
    						type:'list'
    					}
    				}*/,{
    					menuDisabled:true,
    					text:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_STD.operate","操作"),
    					sortable:false,
    					width:80,
    					xtype:'actioncolumn',
    					items:[{
    						iconCls:'people',tooltip:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_STD.people","指定评委"),handler:'setJudgeForOne'
    					},{
							iconCls:'peoples',tooltip:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_STD.peopleAdd","追加评委"),handler:'addJudgeForOne'
						},{
    						iconCls:'remove',tooltip:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_STD.remove","删除"),handler:'removeExamineeOne'
    					}]
    				}],
    				store:{
						type: 'materialsReviewExamineeStore',
						listeners:{
							load:function(store,records,successful,eOpts){
								var responseObj = Ext.decode(eOpts.getResponse().responseText),
									viewModel = me.getViewModel();

								if(viewModel)viewModel.set("tips",{
									total:responseObj.comContent.allTotal,
									assigned:responseObj.comContent.assigned,
									unassign:responseObj.comContent.unassign
								});
							}
						}
					},
					bbar: {
						xtype: 'pagingtoolbar',
						pageSize: 20,
						listeners:{
							afterrender: function(pbar){
								var grid = pbar.findParentByType("grid");
								pbar.setStore(grid.store);
							}
						},
						plugins: new Ext.ux.ProgressBarPager()
					}
    			}]
    		}]
    	});
    	this.callParent();
    },
    buttons: [{
        text: Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_STD.save","保存"),
        iconCls:"save",
        handler: 'onExamineeSave'
    }, {
        text: Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_STD.ensure","确定"),
        iconCls:"ensure",
        handler: 'onExamineeEnsure'
    }, {
        text: Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_STD.close","关闭"),
        iconCls:"close",
        handler: 'onExamineeClose'
    }]
});