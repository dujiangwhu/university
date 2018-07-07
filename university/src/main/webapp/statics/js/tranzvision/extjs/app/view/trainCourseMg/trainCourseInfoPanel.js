Ext.define('KitchenSink.view.trainCourseMg.trainCourseInfoPanel', {
    extend: 'Ext.panel.Panel',
    xtype: 'trainCourseInfoPanel', 
	controller: 'trainCourseMg',
	requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
		'KitchenSink.view.trainCourseMg.attachStore'
	],
    title: '课程信息',
	bodyStyle:'overflow-y:auto;overflow-x:hidden', 
	actType: 'add',//默认新增
    items: [{
        xtype: 'form',
        reference: 'courseAttForm',
		layout: {
            type: 'vbox',
            align: 'stretch'
        },
        border: false,
        bodyPadding: 10,
		//heigth: 600,
		bodyStyle:'overflow-y:auto;overflow-x:hidden',
		
        fieldDefaults: {
            msgTarget: 'side',
            labelWidth: 100,
            labelStyle: 'font-weight:bold'
        },
        items: [{
            xtype: 'textfield',
            fieldLabel:"课程编号",
			name: 'tzCourseId',
			maxLength: 20,
            afterLabelTextTpl: [
                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
            ],
            allowBlank: false
        }, {
            xtype: 'textfield',
            fieldLabel:"课程名称",
			name: 'tzCourseName'
        }, {
            xtype: 'textfield',
            fieldLabel: "课程类型编号",
			name: 'tzCourseTypeId'
        }, {
            xtype: 'textfield',
            fieldLabel: "课程简介",
			name: 'tzCourseDesc'
        }]
    },{
		xtype: 'grid', 
		title: '课程附件列表',
		frame: true,
		columnLines: true,
		height: 350,
		reference: 'attachGrid',
		style: "margin:10px",
	 	multiSelect: true,
		selModel: {
			type: 'checkboxmodel'
		},
		plugins: [
			Ext.create('Ext.grid.plugin.CellEditing',{
				clicksToEdit: 1
			})
		],
		viewConfig: {
			plugins: {
				ptype: 'gridviewdragdrop',
				containerScroll: true,
				dragGroup: this,
				dropGroup: this
			},
			listeners: {
				drop: function(node, data, dropRec, dropPosition) {
					data.view.store.beginUpdate();
					var items = data.view.store.data.items;
					for(var i = 0;i< items.length;i++){
						items[i].set('orderNum',i+1);
					}
					data.view.store.endUpdate();
				}
			}
		},
		columns: [/*{
			//xtype: 'rownumberer',
			text: '序号',
			dataIndex: 'orderNum',
			width:60
		},*/{
			text: '文件名',
			dataIndex: 'tzAttachfileName',
			minWidth: 100,
			flex: 1
		},
			{
				menuDisabled: true,
				sortable: false,
				width:60,
				align:'center',
				xtype: 'actioncolumn',
				items:[
					//{iconCls: 'edit',tooltip: '编辑',handler: 'editPageRegInfoOne'},
					{iconCls: 'remove',tooltip: '删除',handler: 'deletePageRegInfoOne'}
				]
			}
		],
		store: {
			type: 'attachStore'
		},
		dockedItems:[{
			xtype:"toolbar",
			items:[
			{
				xtype: 'form',
				items:[{
				    xtype: 'fileuploadfield',
				    tooltip:"新增数据",
				    iconCls:"add",
				    name: 'orguploadfile',
				    buttonText: '上传',
				    //msgTarget: 'side',
				    buttonOnly:true,
								listeners:{
									change:function(file, value, eOpts ){
										if(value != ""){
											var form = file.findParentByType("form").getForm();
											//获取该类
											var panel = file.findParentByType("trainCourseInfoPanel");
											var tzCourseId=panel.child("form").getForm().findField("tzCourseId").getValue();
											//alert(tzCourseId);
												//获取后缀
												var fix = value.substring(value.lastIndexOf(".") + 1,value.length);
												//if(fix.toLowerCase() == "jpg" || fix.toLowerCase() == "jpeg" || fix.toLowerCase() == "png" || fix.toLowerCase() == "gif" || fix.toLowerCase() == "bmp" || fix.toLowerCase() == "ico"){
													form.submit({
														url: TzUniversityContextPath + '/UpdServlet?filePath=org',
														waitMsg: '图片正在上传，请耐心等待....',
														success: function (form, action) {
															var message = action.result.msg;
															var path = message.accessPath;
															var sysFileName = message.sysFileName;
															if(path.charAt(path.length - 1) == '/'){
																path = path + sysFileName;
															}else{
																path = path + "/" + sysFileName;
															}
															message.tzCourseId=tzCourseId;
															//message.
															//file.previousSibling().previousSibling().setSrc(TzUniversityContextPath + path);
															//panel.child("form").getForm().findField("orgLoginBjImgUrl").setValue(path);
															
															
															tzParams = '{"ComID":"PX_COURSE_COM","PageID":"PX_COURSE_ATT_STD","OperateType":"U","comParams":{"'+"add"+'":['+Ext.JSON.encode(message)+']}}';//"comParams":' + Ext.JSON.encode(action.result.msg) +'}';
					
															Ext.Ajax.request({
															    url: Ext.tzGetGeneralURL,
															    params: {
															        tzParams: tzParams
															    },
															    success: function(response){
															    	var responseText = eval( "(" + response.responseText + ")" );
															      /*if(responseText.success == 0){
																			
																		}else{
																			//file.previousSibling().previousSibling().setSrc("");
																			///panel.child("form").getForm().findField("orgLoginBjImgUrl").setValue("");
																			Ext.MessageBox.alert("错误1", responseText.message);	
																		}
																	}*/
															    }
															});
															
															//重置表单
															//form.reset();
														},
														failure: function (form, action) {
															//重置表单
															//form.reset();
															Ext.MessageBox.alert("错误", action.result.msg);
														}
													});
												//}else{
													//重置表单
													//form.reset();
													//Ext.MessageBox.alert("提示", "请上传jpg|jpeg|png|gif|bmp|ico格式的图片。");
												//}
											
										}
									}
								}
				}]
			}//,
				//{text:"新增",tooltip:"新增数据",iconCls:"add",handler:"addPageRegInfo"},"-",
				//{text:"删除",tooltip:"删除选中的数据",iconCls:"remove",handler:"deletePageRegInfos"}
			]
		}],
		bbar: {
			xtype: 'pagingtoolbar',
			pageSize: 5,
			store: {
				type: 'attachStore'
			},
			listeners:{
				afterrender: function(pbar){
					var grid = pbar.findParentByType("grid");
					pbar.setStore(grid.store);
				}
			},
			plugins: new Ext.ux.ProgressBarPager()
		}
	}],
    buttons: [{
		text: '保存',
		iconCls:"save",
		handler: 'onComRegSave'
	}, {
		text: '确定',
		iconCls:"ensure",
		handler: 'onComRegEnsure'
	}, {
		text: '关闭',
		iconCls:"close",
		handler: 'onComRegClose'
	}]
});
