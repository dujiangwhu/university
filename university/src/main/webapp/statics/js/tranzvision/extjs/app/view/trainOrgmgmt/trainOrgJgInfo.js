Ext.define('KitchenSink.view.trainOrgmgmt.trainOrgJgInfo', {
    extend: 'Ext.panel.Panel',
    xtype: 'trainOrgJgInfo',
	controller: 'trainOrgMg',
	requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.trainOrgmgmt.trainOrgController'
	],
    title: '机构信息', 
	bodyStyle:'overflow-y:auto;overflow-x:hidden',
	actType: 'update',//默认修改
    listeners:{
        afterrender: function(panel){

            var form = panel.child('form').getForm();

            var tzParams = '{"ComID":"TZ_PX_ORG_COM","PageID":"TZ_PX_ORG_STD","OperateType":"QF","comParams":{}}';

            Ext.tzLoad (tzParams,function(responseData){
                //组件注册信息数据
                var formData = responseData.formData;
                form.setValues(formData);
                form.findField("orgId").setReadOnly(true);
                form.findField("orgName").setReadOnly(true);
                form.findField("orgYxState").setReadOnly(true);
                form.findField("orgAuditStatus").setReadOnly(true);
				if(formData.orgLoginBjImgUrl!=""){
					panel.down("image").setSrc(TzUniversityContextPath + formData.orgLoginBjImgUrl);
				}
                panel.commitChanges(panel);
            });
        }
    },
    items: [{
        xtype: 'form',
        reference: 'orgInfoForm',
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
            labelWidth: 140,
            labelStyle: 'font-weight:bold'
        },
        items: [{
            xtype: 'textfield',
            //fieldLabel: '机构编号',
			fieldLabel: Ext.tzGetResourse("TZ_PX_ORG_COM.TZ_PX_ORG_STD.orgId","机构编号"),
			name: 'orgId',
            cls:'lanage_1',
            afterLabelTextTpl: [
                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
            ],
            allowBlank: false
        }, {
            xtype: 'textfield',
            //fieldLabel: '机构名称',
            cls:'lanage_1',
			fieldLabel: Ext.tzGetResourse("TZ_PX_ORG_COM.TZ_PX_ORG_STD.orgName","机构名称"),
			name: 'orgName'
        }, {
            xtype: 'combobox',
            cls:'lanage_1',
			fieldLabel: Ext.tzGetResourse("TZ_PX_ORG_COM.TZ_PX_ORG_STD.stateStore","有效状态"),
			forceSelection: true,
            valueField: 'TValue',
            displayField: 'TSDesc',
			store: new KitchenSink.view.common.store.appTransStore("TZ_ORG_EFF_STATE"),
            typeAhead: true,
            queryMode: 'local',
			name: 'orgYxState',
            value:'Y'
        }, {
            xtype: 'combobox',
			cls:'lanage_1',
            fieldLabel: Ext.tzGetResourse("TZ_PX_ORGGL_COM.TZ_PX_ORGDEF_STD.orgAuditStatus","审核状态"),
            forceSelection: true,
            valueField: 'TValue',
            displayField: 'TSDesc',
            store: new KitchenSink.view.common.store.appTransStore("TZ_ORG_AUDIT_STATE"),
			typeAhead: true,
            queryMode: 'local',
            name: 'orgAuditStatus'
        }, {
            xtype: 'textarea',
            //fieldLabel: '备注',
			fieldLabel: Ext.tzGetResourse("TZ_PX_ORG_COM.TZ_PX_ORG_STD.orgBeizhu","备注"),
			name: 'orgBeizhu'
        },{
            xtype: 'textfield',
            //fieldLabel: '联系人',
			fieldLabel: Ext.tzGetResourse("TZ_PX_ORG_COM.TZ_PX_ORG_STD.orgLxrName","联系人"),
			name: 'orgLxrName'
        }, {
            xtype: 'textfield',
            //fieldLabel: '联系人电话',
			fieldLabel: Ext.tzGetResourse("TZ_PX_ORG_COM.TZ_PX_ORG_STD.orgLxrPhone","联系人电话"),
			name: 'orgLxrPhone'
        }, {
            xtype: 'textfield',
            //fieldLabel: '联系人邮箱',
			fieldLabel: Ext.tzGetResourse("TZ_PX_ORG_COM.TZ_PX_ORG_STD.orgLxrEmail","联系人邮箱"),
			name: 'orgLxrEmail',
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
            fieldLabel: Ext.tzGetResourse("TZ_PX_ORG_COM.TZ_PX_ORG_STD.orgLoginInf","登录系统名称"),
            name: 'orgLoginInf'
        }, {
            xtype: 'hidden',
            //fieldLabel: '背景图片路径',
            fieldLabel: Ext.tzGetResourse("TZ_PX_ORG_COM.TZ_PX_ORG_STD.orgLoginBjImgUrl","背景图片路径"),
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
				html:'<span style="font-weight:bold">'+ Ext.tzGetResourse("TZ_PX_ORG_COM.TZ_PX_ORG_STD.orgLoginBjImg","登录页面背景图") +':</span>'
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
						var panel = file.findParentByType("orgJgInfo");
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
										var panel = file.findParentByType("orgJgInfo");
										
											//获取后缀
											var fix = value.substring(value.lastIndexOf(".") + 1,value.length);
											if(fix.toLowerCase() == "jpg" || fix.toLowerCase() == "jpeg" || fix.toLowerCase() == "jpeg" || fix.toLowerCase() == "png" || fix.toLowerCase() == "gif" || fix.toLowerCase() == "bmp" || fix.toLowerCase() == "ico"){
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
																		
														tzParams = '{"ComID":"TZ_PX_ORG_COM","PageID":"TZ_PX_ORG_STD","OperateType":"HTML","comParams":' + Ext.JSON.encode(action.result.msg) +'}';
				
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
												Ext.MessageBox.alert("提示", "请上传jpg|png|gif|bmp|ico格式的图片。");
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
            name: 'orgTimeCardHave',
			readOnly:true,
			fieldStyle:'background:#F4F4F4',
        }, {
            xtype: 'textfield',
            //fieldLabel: '已分配课时卡',
            fieldLabel: Ext.tzGetResourse("TZ_PX_ORGGL_COM.TZ_PX_ORGDEF_STD.orgTimeCardAssign","已分配课时卡"),
			readOnly:true,
			fieldStyle:'background:#F4F4F4',
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
            //fieldLabel: '登录页面版权信息',
            fieldLabel: Ext.tzGetResourse("TZ_PX_ORG_COM.TZ_PX_ORG_STD.orgLoginCopr","登录页面版权信息"),
            name: 'orgLoginCopr',
            model: 'normal',
            panelXtype: 'orgJgInfo',
            zIndex: 900 
        }]
    }],
    buttons: [{
		text: '保存',
		iconCls:"save",
		handler: 'onJgdFormSave'
	}, {
		text: '确定',
		iconCls:"ensure",
		handler: 'onJgdFormEnsure'
	}, {
		text: '关闭',
		iconCls:"close",
		handler: 'onFormClose'
	}]
});
