Ext.define('KitchenSink.view.enrollmentManagement.applicationForm.classSetBatchWindow', {
	extend: 'Ext.window.Window',
	xtype: 'classSetBatchWindow',
	controller: 'appFormClass',
	requires: [
		'Ext.data.*',
		'Ext.grid.*',
		'Ext.util.*',
		'Ext.toolbar.Paging',
		'Ext.ux.ProgressBarPager',
		'KitchenSink.view.enrollmentManagement.applicationForm.classSetBatchWinStore',
		'KitchenSink.view.enrollmentManagement.applicationForm.classController'
	],
	modal:true,//背景遮罩
	header:false,
	resizable:false,
	minHeight: 150,
	maxHeight: 400,
	ignoreChangesFlag:true,
	bodyStyle:'overflow-y:auto;overflow-x:hidden',
	selList:[],
	modalID:'',
	y:25,
	constructor:function(modalID){
		this.modalID = modalID;
		this.callParent();
	},
	initComponent: function(){
		var me =this;
		var modalID = this.modalID;
//		var processingStatus = new KitchenSink.view.common.store.appTransStore("TZ_AE_STATUS");
		var listStore = new KitchenSink.view.enrollmentManagement.applicationForm.classSetBatchWinStore();
        
		/*var excelTplStore = new KitchenSink.view.common.store.comboxStore({
			recname:'TZ_EXPORT_TMP_T',
			condition:{
				TZ_JG_ID:{
					value:Ext.tzOrgID,
					operator:'01',
					type:'01'
				},
				TZ_APP_MODAL_ID:{
					value:modalID,
					operator:'01',
					type:'01'
				},
				TZ_EXP_TMP_STATUS:{
					value:'A',
					operator:'01',
					type:'01'
				},
				TZ_EXPORT_TMP_TYPE:{
					value:'0',
					operator:'01',
					type:'01'
				}},
			result:'TZ_EXPORT_TMP_ID,TZ_EXPORT_TMP_NAME'
		})*/
		Ext.apply(this,{
			items: [{
				xtype: 'form',
				layout: {
					type: 'vbox',
					align: 'stretch'
				},
				border: false,
				bodyStyle:'overflow-y:auto;overflow-x:hidden',

				fieldDefaults: {
					msgTarget: 'side',
					labelStyle: 'font-weight:bold',
					labelWidth:150
				},
				title: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.setBatch", "设置批次"),
				frame:false,
				minHeight:150,
				autoHeight:true,
				reference: 'setBatchForm',
				name:'setBatchForm',
				bodyPadding: 10,
				buttons: [{
					text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.save", "保存"),
					iconCls:"save",
					handler: 'windowSave'
				},{
					text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.close", "关闭"),
					iconCls:"close",
					handler: 'windowClose'
				}],
				items: [
					{
//						layout:{
//							type:'column'
//						},
						style:'margin-bottom:10px',
						items:[{
							xtype: 'textfield',
		                    fieldLabel: "学生列表",
		                    name: 'stuList',
		                    cls:'lanage_1',
		                    readOnly:true,
		                    hidden:true
                        },{
                        	xtype: 'textfield',
		                    fieldLabel: "班級ID",
		                    name: 'classID',
		                    cls:'lanage_1',
		                    readOnly:true,
		                    hidden:true
                        },{
                            xtype: 'textfield',
		                    fieldLabel: "OPRID",
		                    name: 'oprID',
		                    cls:'lanage_1',
		                    readOnly:true,
		                    hidden:true
                        },{
							fieldLabel:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.setBatchName", "设置批次名称"),
							xtype: 'combobox',
							reference: 'setBatchName',
							name: 'setBatchName',
							style:'margin-top:10px',
							store:listStore,
							valueField:'TZ_BATCH_ID',
							displayField:'TZ_BATCH_NAME',
//							valueField:'batchID',
//							displayField:'batchName',
							queryMode:'lcoal',
//							allowBlank: false,
							editable:false,
							/*afterLabelTextTpl: [
								'<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
							],*/
							columnWidth:.8
						}]
					},
					{
						xtype: 'textfield',
						name: 'appInsID',
						hidden: true
					}
				]
			}]
		});
		this.callParent();
	}
});
