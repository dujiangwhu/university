Ext.define('KitchenSink.view.trainTeacherMg.teacherMgInfoPanel', {
    extend: 'Ext.panel.Panel',
    xtype: 'teacherMgInfoPanel',
    controller: 'teacherController',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager'
    ],
    listeners:{
        resize: function(win){
            win.doLayout();
        }
    },
    actType: '',
    title: '教师信息',
    bodyStyle:'overflow-y:auto;overflow-x:hidden',
    items: [{
        xtype: 'form',
        reference: 'teacherMgForm',
        layout: {
            type: 'vbox',
            align: 'stretch'
        },
        border: false,
        bodyPadding: 10,
        bodyStyle:'overflow-y:auto;overflow-x:hidden',

        fieldDefaults: {
            msgTarget: 'side',
            labelWidth: 110,
            labelStyle: 'font-weight:bold'
        },
        items: [{
            layout: {
                type: 'column'
            },
            items:[{
                columnWidth:.2,
                xtype: "image",
                src: "",
                height:300,
                width:217,
                name: "titileImage"
            },{
                columnWidth:.8,
                bodyStyle:'padding-left:30px',
                layout: {
                    type: 'vbox',
                    align: 'stretch'
                },
                items: [{
                    xtype: 'hiddenfield',
                    fieldLabel: 'OPRID',
                    name: 'oprid'
                    //fieldStyle:'background:#F4F4F4'
                },{
                    xtype: 'textfield',
                    fieldLabel: '教师姓名',
                    readOnly:true,
                    afterLabelTextTpl: [
                                        '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                                    ],
                    name: 'name',
                    fieldStyle:'background:#F4F4F4'
                },{
                    xtype: 'textfield',
                    fieldLabel: '手机号码',
                    afterLabelTextTpl: [
                                        '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                                    ],
                    readOnly:true,
                    name: 'phone',
                    fieldStyle:'background:#F4F4F4'
                },{
            		xtype: 'combobox',
                    fieldLabel: '性别',
                    allowBlank: false,
                    afterLabelTextTpl: [
                                        '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                                    ],
                    editable:false,
                    emptyText:'请选择',
                    queryMode: 'remote',
            	    	name: 'sex',
            	    	valueField: 'TValue',
                		displayField: 'TSDesc',
                		store: new KitchenSink.view.common.store.appTransStore("TZ_SEX")
                },
                {
                    xtype: 'textfield',
                    fieldLabel: '年龄',
                    allowBlank: false,
                    afterLabelTextTpl: [
                                        '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                                    ],
                    name: 'age'
                },{
                    xtype: 'textfield',
                    fieldLabel: '身份证号',
                    allowBlank: false,
                    afterLabelTextTpl: [
                                        '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                                    ],
                    name: 'idCard'
                },{
            		xtype: 'combobox',
                    fieldLabel: '级别',
                    allowBlank: false,
                    afterLabelTextTpl: [
                                        '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                                    ],
                    editable:false,
                    emptyText:'请选择',
                    queryMode: 'remote',
            	    	name: 'level',
            	    	valueField: 'TValue',
                		displayField: 'TSDesc',
                		store: new KitchenSink.view.common.store.appTransStore("PX_TEACHER_LEVEL")
                },{
                    xtype: 'textfield',
                    fieldLabel: '毕业院校',
                    name: 'school'
                },{
                    xtype: 'combobox',
                    fieldLabel: '学历',
                    name: 'educationBg',
					editable:false,
                    emptyText:'请选择',
                    queryMode: 'remote',
            	    	valueField: 'TValue',
                		displayField: 'TSDesc',
                		store: new KitchenSink.view.common.store.appTransStore("TZ_EDU_BG")
                },{
                    xtype: 'textfield',
                    fieldLabel: '教龄',
                    allowBlank: false,
                    afterLabelTextTpl: [
                                        '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                                    ],
                    name: 'schoolAge'
                },{
                    xtype: 'textfield',
                    fieldLabel: '教师证书号码',
                    name: 'teacherCard'
                },{
            		xtype: 'combobox',
                    fieldLabel: '账户类型',
                    allowBlank: false,
                    afterLabelTextTpl: [
                                        '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                                    ],
                    editable:false,
                    emptyText:'请选择',
                    queryMode: 'remote',
            	    	name: 'accountType',
            	    	valueField: 'TValue',
                		displayField: 'TSDesc',
                		store: new KitchenSink.view.common.store.appTransStore("PX_ACCOUNT_TYPE")
                },{
                    xtype: 'textfield',
                    fieldLabel: '账户号码',
                    allowBlank: false,
                    afterLabelTextTpl: [
                                        '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                                    ],
                    name: 'accountNum'
                },{
                    xtype: 'textfield',
                    fieldLabel: '积分',
                    readOnly:true,
                    allowBlank: false,
                    afterLabelTextTpl: [
                                        '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                                    ],
                    name: 'score',
                    fieldStyle:'background:#F4F4F4'
                },{
                    xtype: 'textfield',
                    fieldLabel: '微信',
                    name: 'qq'
                },{
                    xtype: 'textfield',
                    fieldLabel: '邮箱',
                    name: 'email'
                },{
                    xtype: 'textfield',
                    fieldLabel: '紧急联系人',
                    name: 'contactor'
                },{
                    xtype: 'textfield',
                    fieldLabel: '紧急联系人电话',
                    name: 'contactorPhone'
                },{
                    xtype: 'textfield',
                    fieldLabel: '紧急联系人地址',
                    name: 'contactorAddress'
                },{
            		xtype: 'combobox',
                    fieldLabel: '状态信息',
                    allowBlank: false,
                    afterLabelTextTpl: [
                                        '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                                    ],
                    editable:false,
                    emptyText:'请选择',
                    queryMode: 'remote',
            	    	name: 'statu',
            	    	valueField: 'TValue',
                		displayField: 'TSDesc',
                		store: new KitchenSink.view.common.store.appTransStore("PX_TEACHER_STATU")
                },{
                    xtype: 'textarea',
                    //height:100,
                    //width:300,
                    grow:false,
                    fieldLabel: '自我介绍',
                    name: 'introduce'
                }, {
					xtype: 'hiddenfield',
					//fieldLabel: '背景图片路径',
					name: 'teacherCardUrl'
				}, {
					xtype: 'hiddenfield',
					//fieldLabel: '教师证存储路径',
					name: 'teacherCardPath'
				}, {
					xtype: 'hiddenfield',
					//fieldLabel: '教师证文件名',
					name: 'teacherCardFileName'
				}, {
					xtype: 'hiddenfield',
					//fieldLabel: '教师证系统文件名',
					name: 'teacherCardSysFileName'
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
				html:'<span style="font-weight:bold">'+ '教师证' +':</span>'
			},{
				xtype:'image',
				name:'teacherCardImage',
				width:320,
				height:150,
				border:1,
				style: {
				    borderColor: '#eee'
				},
				margin:'0 20 10 35',
				src:''
			},{
				xtype:'button',
				text:'删除',
				listeners:{
					click:function(file, value, eOpts ){
						file.previousSibling().setSrc("");
						//获取该类
						var panel = file.findParentByType("teacherMgInfoPanel");
						panel.child("form").getForm().findField("teacherCardUrl").setValue("");
						panel.child("form").getForm().findField("teacherCardPath").setValue("");
						panel.child("form").getForm().findField("teacherCardFileName").setValue("");
						panel.child("form").getForm().findField("teacherCardSysFileName").setValue("");
					}
				}
			},{
	            xtype: 'fileuploadfield',
	            name: 'websitefile',
	            buttonText: '上传',
	            //msgTarget: 'side',
	            buttonOnly:true,
							listeners:{
								change:function(file, value, eOpts ){
									if(value != ""){
										var form = file.findParentByType("form").getForm();
										//获取该类
										var panel = file.findParentByType("teacherMgInfoPanel");
										
											//获取后缀
											var fix = value.substring(value.lastIndexOf(".") + 1,value.length);
											if(fix.toLowerCase() == "jpg" || fix.toLowerCase() == "jpeg" || fix.toLowerCase() == "png" || fix.toLowerCase() == "gif" || fix.toLowerCase() == "bmp" || fix.toLowerCase() == "ico"){
												form.submit({
													url: TzUniversityContextPath + '/UpdWebServlet?filePath=TeaFile',
													waitMsg: '图片正在上传，请耐心等待....',
													success: function (form, action) {
														var message = action.result.msg;
														var path = message.accessPath;
														var fileName = message.filename;
														var sysFileName = message.sysFileName;
														
														panel.child("form").getForm().findField("teacherCardPath").setValue(path);
														panel.child("form").getForm().findField("teacherCardFileName").setValue(fileName);
														panel.child("form").getForm().findField("teacherCardSysFileName").setValue(sysFileName);
														
														if(path.charAt(path.length - 1) == '/'){
															path = path + sysFileName;
														}else{
															path = path + "/" + sysFileName;
														}
														
														file.previousSibling().previousSibling().setSrc(TzUniversityContextPath + path);
														panel.child("form").getForm().findField("teacherCardUrl").setValue(path);
														/*
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
														*/
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
			},{
                        xtype: 'hiddenfield',
                        name: 'titleImageUrl'
                    }]
            }]
        }]
    }],
    buttons: [
        {
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