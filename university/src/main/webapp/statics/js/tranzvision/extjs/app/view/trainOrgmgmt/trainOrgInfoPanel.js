Ext.define('KitchenSink.view.trainOrgmgmt.trainOrgInfoPanel', {
    extend: 'Ext.panel.Panel',
    xtype: 'trainOrgInfo',
    controller: 'trainOrgMg',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.security.user.userController',
        'KitchenSink.view.trainOrgmgmt.trainOrgMemListStore'
    ],
    title: '机构信息',
    bodyStyle:'overflow-y:auto;overflow-x:hidden',
    actType: 'add',//默认新增
    items: [{
        xtype: 'form',
        reference: 'orgInfoForm',
        layout: {
            type: 'vbox',
            align: 'stretch'
        },
        border: false,
        bodyPadding: 10,
        bodyStyle:'overflow-y:auto;overflow-x:hidden',

        fieldDefaults: {
            msgTarget: 'side',
            labelWidth: 140,
            labelStyle: 'font-weight:bold'
        },

        items: [{
            xtype: 'textfield',
            fieldLabel: Ext.tzGetResourse("TZ_PX_ORGGL_COM.TZ_PX_ORGDEF_STD.orgId","机构编号"),
            name: 'orgId',
            afterLabelTextTpl: [
                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
            ],
            allowBlank: false,
			maxLength: 10,
			enforceMaxLength: true
        }, {
            xtype: 'textfield',
            fieldLabel: Ext.tzGetResourse("TZ_PX_ORGGL_COM.TZ_PX_ORGDEF_STD.orgName","机构名称"),
            name: 'orgName'
        }, {
            xtype: 'combobox',
            editable:false,
            fieldLabel: Ext.tzGetResourse("TZ_PX_ORGGL_COM.TZ_PX_ORGDEF_STD.stateStore","有效状态"),
            forceSelection: true,
            valueField: 'TValue',
            displayField: 'TSDesc',
            store: new KitchenSink.view.common.store.appTransStore("TZ_ORG_EFF_STATE"),
            queryMode: 'local',
            name: 'orgYxState',
            value:'Y'
        }, {
            xtype: 'combobox',
            editable:false,
            fieldLabel: Ext.tzGetResourse("TZ_PX_ORGGL_COM.TZ_PX_ORGDEF_STD.orgAuditStatus","审核状态"),
            forceSelection: true,
            valueField: 'TValue',
            displayField: 'TSDesc',
            store: new KitchenSink.view.common.store.appTransStore("TZ_ORG_AUDIT_STATE"),
            queryMode: 'local',
            name: 'orgAuditStatus',
            value:'A'
        }, {
            xtype: 'textarea',
            fieldLabel: Ext.tzGetResourse("TZ_PX_ORGGL_COM.TZ_PX_ORGDEF_STD.orgBeizhu","备注"),
            name: 'orgBeizhu'
        }, {
            xtype: 'textfield',
            fieldLabel: Ext.tzGetResourse("TZ_PX_ORGGL_COM.TZ_PX_ORGDEF_STD.staticPath","静态文件存放路径"),
            name: 'staticPath',
			hidden: true
        },{
            xtype: 'textfield',
            //fieldLabel: '联系人',
            fieldLabel: Ext.tzGetResourse("TZ_PX_ORGGL_COM.TZ_PX_ORGDEF_STD.orgLxrName","联系人"),
            name: 'orgLxrName'
        }, {
            xtype: 'textfield',
            //fieldLabel: '联系人电话',
            fieldLabel: Ext.tzGetResourse("TZ_PX_ORGGL_COM.TZ_PX_ORGDEF_STD.orgLxrPhone","联系人电话"),
			maxLength: 30,
            name: 'orgLxrPhone'
        }, {
            xtype: 'textfield',
            //fieldLabel: '联系人邮箱',
            fieldLabel: Ext.tzGetResourse("TZ_PX_ORGGL_COM.TZ_PX_ORGDEF_STD.orgLxrEmail","联系人邮箱"),
            name: 'orgLxrEmail',
			maxLength: 75,
            vtype: 'email'
        }, {
            xtype: 'textfield',
            //fieldLabel: '联系人QQ',
            fieldLabel: Ext.tzGetResourse("TZ_PX_ORGGL_COM.TZ_PX_ORGDEF_STD.orgLxrQQ","联系人QQ"),
            name: 'orgLxrQQ',
			maxLength: 15
        }, {
            xtype: 'textfield',
            //fieldLabel: '登录系统文字',
            fieldLabel: Ext.tzGetResourse("TZ_PX_ORGGL_COM.TZ_PX_ORGDEF_STD.orgLoginInf","登录页面系统名称"),
            name: 'orgLoginInf'
        }, {
            xtype: 'hidden',
            //fieldLabel: '背景图片路径',
            fieldLabel: Ext.tzGetResourse("TZ_PX_ORGGL_COM.TZ_PX_ORGDEF_STD.orgLoginBjImgUrl","背景图片路径"),
            name: 'orgLoginBjImgUrl'
        },{
			xtype: 'form',
			layout: 'hbox',
			width:'100%',
			height:'100%',
			defaults:{
				margin:'0 0 0 20px',
			},
			items:[{
				margin:'10 35 0 0',		
				xtype:'label',
				html:'<span style="font-weight:bold">'+ Ext.tzGetResourse("TZ_PX_ORGGL_COM.TZ_PX_ORGDEF_STD.orgLoginBjImg","登录页面背景图") +':</span>'
			},{
				xtype:'image',
				width:70,
				height:50,
				border:1,
				style: {
				    borderColor: '#eee'
				},
				margin:'0 20 10 0',
				src:''
			},{
				xtype:'button',
				text:'删除',
				listeners:{
					click:function(file, value, eOpts ){
						file.previousSibling().setSrc("");
						//获取该类
						var panel = file.findParentByType("trainOrgInfo");
						panel.child("form").getForm().findField("orgLoginBjImgUrl").setValue("");
					}
				}
			},{
	            xtype: 'fileuploadfield',
	            name: 'orguploadfile',
	            buttonText: '上传',
	            //msgTarget: 'side',
	            buttonOnly:true,
							listeners:{
								change:function(file, value, eOpts ){
									if(value != ""){
										var form = file.findParentByType("form").getForm();
										//获取该类
										var panel = file.findParentByType("trainOrgInfo");
										
											//获取后缀
											var fix = value.substring(value.lastIndexOf(".") + 1,value.length);
											if(fix.toLowerCase() == "jpg" || fix.toLowerCase() == "jpeg" || fix.toLowerCase() == "png" || fix.toLowerCase() == "gif" || fix.toLowerCase() == "bmp" || fix.toLowerCase() == "ico"){
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
														
														file.previousSibling().previousSibling().setSrc(TzUniversityContextPath + path);
														panel.child("form").getForm().findField("orgLoginBjImgUrl").setValue(path);
														
														tzParams = '{"ComID":"TZ_PX_ORGGL_COM","PageID":"TZ_PX_ORGDEF_STD","OperateType":"HTML","comParams":' + Ext.JSON.encode(action.result.msg) +'}';
				
														Ext.Ajax.request({
														    url: Ext.tzGetGeneralURL,
														    params: {
														        tzParams: tzParams
														    },
														    success: function(response){
														    	var responseText = eval( "(" + response.responseText + ")" );
														      if(responseText.success == 0){
																		
																	}else{
																		file.previousSibling().previousSibling().setSrc("");
																		panel.child("form").getForm().findField("orgLoginBjImgUrl").setValue("");
																		Ext.MessageBox.alert("错误", responseText.message);	
																	}
																}
														});
														
														//重置表单
														form.reset();
													},
													failure: function (form, action) {
														//重置表单
														form.reset();
														Ext.MessageBox.alert("错误", action.result.msg);
													}
												});
											}else{
												//重置表单
												form.reset();
												Ext.MessageBox.alert("提示", "请上传jpg|jpeg|png|gif|bmp|ico格式的图片。");
											}
										
									}
								}
							}
	      }]
			}, {
            xtype: 'textfield',
            //fieldLabel: '机构地址',
            fieldLabel: Ext.tzGetResourse("TZ_PX_ORGGL_COM.TZ_PX_ORGDEF_STD.orgAddress","机构地址"),
            name: 'orgAddress'
        }, {
            xtype: 'textfield',
            //fieldLabel: '机构区域',
            fieldLabel: Ext.tzGetResourse("TZ_PX_ORGGL_COM.TZ_PX_ORGDEF_STD.orgArea","机构区域"),
            name: 'orgArea'
        }, {
            xtype: 'textfield',
            //fieldLabel: '拥有的课时卡',
            fieldLabel: Ext.tzGetResourse("TZ_PX_ORGGL_COM.TZ_PX_ORGDEF_STD.orgTimeCardHave","拥有的课时卡"),
            name: 'orgTimeCardHave'
        }, {
            xtype: 'textfield',
            //fieldLabel: '已分配课时卡',
            fieldLabel: Ext.tzGetResourse("TZ_PX_ORGGL_COM.TZ_PX_ORGDEF_STD.orgTimeCardAssign","已分配课时卡"),
            name: 'orgTimeCardAssign'
        },{
            xtype: 'ueditor',
            fieldLabel: Ext.tzGetResourse("TZ_PX_ORGGL_COM.TZ_PX_ORGDEF_STD.orgIntro","机构介绍"),
            name: 'orgIntro',
            model: 'normal',
            panelXtype: 'orgInfo',
            zIndex: 900 
        },{
            xtype: 'ueditor',
            fieldLabel: Ext.tzGetResourse("TZ_PX_ORGGL_COM.TZ_PX_ORGDEF_STD.orgLoginCopr","登录页面版权信息"),
            name: 'orgLoginCopr',
            model: 'normal',
            panelXtype: 'orgInfo',
            zIndex: 900 
        },{
            xtype: 'tabpanel',
            frame: true,
            activeTab: 0,
            plain:false,
            resizeTabs:true,
            defaults :{
                autoScroll: false
            },

            listeners:{
                tabchange:function(tabPanel, newCard, oldCard){
                    var queryType;
                    var orgId = tabPanel.findParentByType('form').getForm().findField('orgId').getValue();
                    if (newCard.title == "机构用户"){
                        queryType = "USER";
                    }
                    if (newCard.title == "机构角色"){
                        queryType = "ROLE";
                    }
					if(orgId != null && orgId != ""){
                        this.doLayout();
                        if(newCard.store.isLoaded()==false){
                            //var tzStoreParams = '{"orgId":"'+orgId+'","queryType":"' + queryType + '"}';
                            var tzStoreParams;
                            if (newCard.title == "机构用户"){
                                tzStoreParams = '{"orgId":"'+orgId+'","queryType":"' + queryType + '","cfgSrhId":"TZ_PX_ORGGL_COM.TZ_PX_ORGDEF_STD.TZ_PXJG_USER_VW","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+ orgId+'"}}';
                            }else{
                                tzStoreParams = '{"orgId":"'+orgId+'","queryType":"' + queryType + '"}';
                            }
                            newCard.store.tzStoreParams = tzStoreParams;
                            newCard.store.load();
                        }
                    }

                }
            },
            items:[{
                xtype: 'grid',
                height: 460,
                title: '机构用户',
                columnLines: true,
				name: 'userRoleGrid',
                reference: 'userRoleGrid',
                selModel: {
                    type: 'checkboxmodel'
                },
                columns: [{
                    text: '机构编号',
                    dataIndex: 'orgId',
                    hidden: true
                },{
                    text: '用户账号',
                    dataIndex: 'usAccNum',
                    width: 300
                },{
                    text: '用户姓名',
                    dataIndex: 'usName',
                    minWidth: 100,
                    flex: 1
                },{
                    menuDisabled: true,
                    sortable: false,
                    width:50,
                    xtype: 'actioncolumn',
                    items:[
                        {iconCls: 'edit',tooltip: '编辑',handler: 'editUserAccountLine'},
                        {iconCls: 'remove',tooltip: '删除',handler: 'deleteUserAccountLine'}
                    ]
                }],
                store: {
                    type: 'trainOrgMemListStore'
                },
                dockedItems:[{
                    xtype:"toolbar",
                    items:[
                        {text:"查询",tooltip:"查询数据",iconCls: "query",handler:'queryUser'},"-",
                        {text:"新增",tooltip:"添加机构用户",iconCls:"add",handler:'addUserAccount'},"-",
						{text:"编辑",tooltip:"编辑机构用户",iconCls:"edit",handler:'editUserAccount'},"-",
                        {text:"删除",tooltip:"删除选中的机构用户",iconCls:"remove",handler:'deleteUserAccountOrRole'}
                    ]
                }],
                bbar: {
                    xtype: 'pagingtoolbar',
                    pageSize: 10,
                    listeners:{
                        afterrender: function(pbar){
                            var grid = pbar.findParentByType("grid");
                            pbar.setStore(grid.store);
                        }
                    },
                    reference: 'adminUserToolBar',
                    plugins: new Ext.ux.ProgressBarPager()
                }
            }]
        }]
    }],
    buttons: [{
        text: '保存',
        iconCls:"save",
        handler: 'onFormSave'
    }, {
        text: '确定',
        iconCls:"ensure",
        handler: 'onFormEnsure'
    }, {
        text: '关闭',
        iconCls:"close",
        handler: 'onFormClose'
    }]
});