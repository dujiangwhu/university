﻿Ext.define('KitchenSink.view.siteManage.siteManage.siteTemplateInfo', {
    extend: 'Ext.panel.Panel',
    xtype: 'siteTemplateInfoGL', 
	controller: 'siteChildInfo1',
	requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.siteManage.siteManage.templateStore',
        'KitchenSink.view.siteManage.siteManage.columnStore',
        'KitchenSink.view.siteManage.siteManage.areaStore',
        'KitchenSink.view.siteManage.siteManage.menuStore',
        'KitchenSink.view.siteManage.siteManage.siteChildsController'
        
	],
    title: '站点基本信息', 
	bodyStyle:'overflow-y:auto;overflow-x:hidden',
	actType: 'add',//默认新增
    items: [{
        xtype: 'form',
        reference: 'siteAccountForm',
		layout: {
            type: 'vbox',
            align: 'stretch'
        },
        //frame: true,
        border: false,
        bodyPadding: 10,
		bodyStyle:'overflow-y:auto;overflow-x:hidden',
		buttonAlign: 'center',
        fieldDefaults: {
            msgTarget: 'side',
            labelWidth: 170,
            labelStyle: 'font-weight:bold'
        },
        items: [{
            xtype: 'textfield',
            readOnly:true,
			fieldStyle:'background:#F4F4F4',
            fieldLabel: '站点编号',
			name: 'siteId',
			value: 'NEXT'
        }, {
            xtype: 'combobox',
            fieldLabel: '启用状态',
            editable:false,
            emptyText:'请选择',
            queryMode: 'remote',
			name: 'enabled',
			valueField: 'TValue',
    		displayField: 'TSDesc',
    		store: new KitchenSink.view.common.store.appTransStore("TZ_SITEM_ENABLE")
    		/*
			listeners: {
			  	afterrender: function(tvType){
					Ext.tzLoad('{"OperateType":"TV","fieldName":"TZ_SITEM_ENABLE"}',function(response){
						tvType.store = new Ext.data.Store({		
							fields: ['TValue', 'TSDesc', 'TLDesc'],
							data:response.TZ_SITEM_ENABLE
						});
					});
					tvType.setValue("N");
				}
			}*/
        }, {
			xtype: 'combobox',
            fieldLabel: '站点语言',
            editable:false,
            emptyText:'请选择',
            queryMode: 'remote',
			name: 'siteLanguage',
			valueField: 'TValue',
    		displayField: 'TSDesc',
    		store: new KitchenSink.view.common.store.appTransStore("TZ_GD_LANGUAGE")
		},{
            xtype: 'textfield',
            fieldLabel: '站点名称',
			name: 'siteName'
        },{  
            xtype: 'textareafield',  
            grow: true,
            name: 'siteIntroduce',  
            fieldLabel: '站点说明',
            preventScrollbars : false
        },{
            xtype: 'textfield',
            fieldLabel: '图片存放的服务器文件夹',
			name: 'picPrefix'
        },{
            xtype: 'textfield',
            fieldLabel: '附件存放的服务器文件夹',
			name: 'attPrefix'
        },{
            xtype: 'textfield',
            fieldLabel: '视频存放的服务器文件夹',
			name: 'viewPrefix'
        },{
            xtype: 'textfield',
            fieldLabel: '站点首页处理程序',
			name: 'siteHomeProgram'
        },{
            xtype: 'textfield',
            fieldLabel: '站点登录页处理程序',
			name: 'siteLoginProgram',
			hidden: true
        },{
            xtype: 'textfield',
            fieldLabel: '站点注册页处理程序',
			name: 'siteRegistProgram',
			hidden: true
        },{
			xtype : 'tabpanel',
			activeTab: 0,
        	plain:false,
        	frame: true,
        	resizeTabs:true,
        	autoHeight:true,
        	defaults :{
            	autoScroll: false,
            	//margin:5
        	},
        	listeners:{
        		tabchange:function(tp,p){
        			var queryID;
        			var siteId = tp.findParentByType("form").getForm().findField('siteId').getValue();
        			tp.findParentByType("form").getForm().findField('siteLoginProgram').setHidden(true);
        			tp.findParentByType("form").getForm().findField('siteRegistProgram').setHidden(true);
        			if(p.title == "皮肤设置"){
        				queryID = "1";
        				tp.findParentByType("form").getForm().findField('siteLoginProgram').setHidden(false);
        				tp.findParentByType("form").getForm().findField('siteRegistProgram').setHidden(false);
        			}
        			if(p.title == "站点模板集合"){
        				queryID = "2";
        			}
        			if(p.title == "站点栏目集合"){
        				queryID = "3";
        			}
        			if(p.title == "站点区域集合"){
        				queryID = "4";
        			}
        			if(p.title == "站点菜单集合"){
        				queryID = "5";
        			}
        			this.doLayout();
        			if(queryID == "2" || queryID == "3" || queryID == "4" || queryID == "5"){
	        			var Params = '{"siteId":"'+siteId+'","queryID":"' + queryID + '"}';
						p.store.tzStoreParams = Params;
						p.store.load();
        			}
                }
            },
			items : [{
				//id:'skin',
				title:'皮肤设置',
				xtype: 'form',
		        reference: 'siteSkinForm',
				layout: {
		            type: 'vbox',
		            align: 'stretch'
		        },
		        //frame: true,
		        border: false,
		        bodyPadding: 10,
				bodyStyle:'overflow-y:auto;overflow-x:hidden',
				buttonAlign: 'center',
		        fieldDefaults: {
		            msgTarget: 'side',
		            labelWidth: 170,
		            labelStyle: 'font-weight:bold'
		        },
		        items: [{
		            xtype: 'textfield',
		            fieldLabel: '皮肤名称',
					name: 'skinname'
		        },{  
		            xtype: 'textareafield',  
		            grow: true,
		            name: 'skincode',  
		            fieldLabel: '皮肤样式源码',
		            preventScrollbars : false
		        },{  
		            xtype: 'textareafield',  
		            grow: true,
		            name: 'skinMcode',  
		            fieldLabel: '手机皮肤样式源码',
		            preventScrollbars : false
		        },{
		            xtype: 'textfield',
		            fieldLabel: '皮肤样式文件存储路径',
					name: 'skinstor'
		        }]
			},{
				//id:'template',
				title:'站点模板集合',
				xtype: 'grid',
				columnLines: true,
				minHeight: 250,
				reference: 'templateGrid',
				store: {
					type: 'templateMStore'
				},
				columns: [{ 
					text: '模板编号',
					dataIndex: 'templateId',
					width: 200
				},{
					text: '模板名称',
					dataIndex: 'templateName',
					minWidth: 5,
					flex: 1
				},{
					text: '模板类型',
					dataIndex: 'templateType',
					width: 200
				},{
					text: '操作',
	               	menuDisabled: true,
	               	sortable: false,
				   	width:100,
	               	xtype: 'actioncolumn',
				   	items:[
				   		{iconCls: 'edit',tooltip: '编辑',handler: 'editSiteTemplate'},
				   		'-',
						{iconCls: 'remove',tooltip: '删除',handler: 'deleteSiteTemplate'}
				   	]
	            }],
	            tbar: [{
					xtype:"toolbar",
                    items: [{text:"新增",tooltip:"新增数据",iconCls:"add",handler:'addTemplate'}]
                }],
				bbar: {
					xtype: 'pagingtoolbar',
					pageSize: 10,
					/*
					reference: 'templateToolBar',
					store: {
						type: 'templateStore'
					},
					*/
					listeners:{
						afterrender: function(pbar){
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
			},{
				//id:'column',
				title:'站点栏目集合',
				xtype: 'grid',
				columnLines: true,
				minHeight: 250,
				reference: 'columnGrid',
				store: {
					type: 'columnStoreI'
				},
				columns: [{ 
					text: '栏目编号',
					dataIndex: 'lm_id',
					width: '150'
				},{
					text: '栏目名称',
					dataIndex: 'lm_name',
					flex: 1
				},{
					text: '栏目类型',
					dataIndex: 'lm_lx',
					width: '150'
				},{
					text: '内容类型',
					dataIndex: 'lm_nrlx',
					width: '150'
				},{
				  text:'',
				  width:10,   
				  dataIndex:'lm_yxztms',   
                  hidden:true
				},{
					text: '有效状态',
					dataIndex: 'lm_yxzt',
					width: '150'/*,
					renderer: function(value,metadata,record){   
					
					var _yxzt=record.get('lm_yxzt');
					
						if (_yxzt=='Y'){
							return '是';
						}else{
							return '否';
						}
						
					}*/				
				},{
					text: '操作',
	               	menuDisabled: true,
	               	sortable: false,
				   	width:100,
	               	xtype: 'actioncolumn',
				   	items:[
				   		{iconCls: 'edit',tooltip: '编辑',handler: 'editSiteColumn'},
				   		'-',
						{iconCls: 'remove',tooltip: '删除',handler: 'deleteSiteColumn'}
				   	]
	            }],
	            tbar: [{
					xtype:"toolbar",
                    items: [{text:"新增",tooltip:"新增数据",iconCls:"add",handler:'addColumn'}]
                }],
				bbar: {
					xtype: 'pagingtoolbar',
					pageSize: 10,
					/*
					reference: 'columnToolBar',
					store: {
						type: 'columnStore'
					},
					*/
					listeners:{
						afterrender: function(pbar){
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
			},{
				//id:'area',
				title:'站点区域集合',
				xtype: 'grid',
				columnLines: true,
				minHeight: 250,
				reference: 'areaGrid',
				store: {
					type: 'areaMStore'
				},
				columns: [{ 
					text: '区域编号',
					dataIndex: 'areaid',
					width: '150'
				},{
					text: '区域名称',
					dataIndex: 'areaname',
					flex: 1
				},{
					text: '区域类型',
					dataIndex: 'areatypeid',
					width: '150'
				},{
					text: '区域位置',
					dataIndex: 'areaposition',
					width: '150'
				},{
					text: '操作',
	               	menuDisabled: true,
	               	sortable: false,
				   	width:100,
	               	xtype: 'actioncolumn',
				   	items:[
				   		{iconCls: 'edit',tooltip: '编辑',handler: 'editSiteArea'},
				   		'-',
						{iconCls: 'remove',tooltip: '删除',handler: 'deleteSiteArea'}
				   	]
	            }],
	            tbar: [{
					xtype:"toolbar",
                    items: [{text:"新增",tooltip:"新增数据",iconCls:"add",handler:'addArea'},'-',
                    		{text:"区域类型管理",tooltip:"区域类型管理",iconCls:"add",handler:'addTypeArea'}]
                }],
				bbar: {
					xtype: 'pagingtoolbar',
					pageSize: 10,
					/*
					reference: 'areaToolBar',
					store: {
						type: 'areaStore'
					},
					*/
					listeners:{
						afterrender: function(pbar){
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
			},{
				//id:'menu',
				title:'站点菜单集合',
				xtype: 'grid',
				columnLines: true,
				minHeight: 250,
				reference: 'menuGrid',
				store: {
					type: 'menuMStore'
				},
				columns: [{ 
					text: '菜单编号',
					dataIndex: 'menuid',
					width: 200
				},{
					text: '菜单名称',
					dataIndex: 'menuname',
					minWidth: 5,
					flex: 1
				},{
					text: '菜单功能类型',
					dataIndex: 'menutypename',
					width: 200
				},{
					text: '操作',
	               	menuDisabled: true,
	               	sortable: false,
				   	width:100,
	               	xtype: 'actioncolumn',
				   	items:[
				   		{iconCls: 'edit',tooltip: '编辑',handler: 'editSiteMenu'},
				   		'-',
						{iconCls: 'remove',tooltip: '删除',handler: 'deleteSiteMenu'}
				   	]
	            }],
	            tbar: [{
					xtype:"toolbar",
                    items: [{text:"新增",tooltip:"新增数据",iconCls:"add",handler:'addMenu'},'-',
                    		{text:"菜单类型管理",tooltip:"菜单类型管理",iconCls:"add",handler:'addTypeMenu'}]
                }],
				bbar: {
					xtype: 'pagingtoolbar',
					pageSize: 10,
					/*
					reference: 'menuToolBar',
					store: {
						type: 'menuStore'
					},
					*/
					listeners:{
						afterrender: function(pbar){
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
			},{
				title:'首页源代码',
				xtype: 'form',
				layout: {
				 type: 'vbox',
				 align: 'stretch'
				},
				minHeight: 350,
				border: false,
				bodyPadding: 10,
				bodyStyle:'overflow-y:auto;overflow-x:hidden',
				buttonAlign: 'center',
				fieldDefaults: {
				msgTarget: 'side',
					labelWidth: 170,
					labelStyle: 'font-weight:bold'
				},
				items: [{
				 xtype: 'textarea',
				 height: 350,
                 fieldLabel: '保存时源代码',
			     name: 'indexSaveCode'
				}/*,{
				 xtype: 'textarea',
				 height: 350,
                 fieldLabel: '发布时源代码',
			     name: 'indexPubCode'
				}*/]
			},{
				title:'登录页源代码',
				xtype: 'form',
				layout: {
				 type: 'vbox',
				 align: 'stretch'
				},
				border: false,
				bodyPadding: 10,
				bodyStyle:'overflow-y:auto;overflow-x:hidden',
				buttonAlign: 'center',
				fieldDefaults: {
				msgTarget: 'side',
					labelWidth: 170,
					labelStyle: 'font-weight:bold'
				},
				items: [{
				 xtype: 'textarea',
				 height: 350,
                 fieldLabel: '保存时源代码',
			     name: 'loginSaveCode'
				}/*,{
				 xtype: 'textarea',
				 height: 350,
                 fieldLabel: '发布时源代码',
			     name: 'loginPubCode'
				}*/]
			},
			{
				//id:'skin',
				title:'手机站点设置',
				xtype: 'form',
		        reference: 'mobileSiteForm',
				layout: {
		            type: 'vbox',
		            align: 'stretch'
		        },
		        //frame: true,
		        border: false,
		        bodyPadding: 10,
				bodyStyle:'overflow-y:auto;overflow-x:hidden',				
				buttonAlign: 'center',
		        fieldDefaults: {
		            msgTarget: 'side',
		            labelWidth: 100,
		            labelStyle: 'font-weight:bold'
		        },
		        items: [
		        	{
		        		xtype: 'form',
		        		layout: 'hbox',
		        		width:'100%',
		        		name:'uploadForm',
						minHeight:80,
						defaults:{
							margin:'0 0 0 20px',
						},
						items:[{
				            xtype: 'hidden',
				            name: 'mobileLogoImg'
				        },{
							margin:'10 35 0 0',		
							xtype:'label',
							html:'<span style="font-weight:bold">手机版logo:</span>'
						},{
							xtype:'image',
							width:414,
							height:64,
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
									var pform = file.findParentByType("form").findParentByType("form").findParentByType("form").getForm();
									var siteId = pform.findField("siteId").getValue();
									if(siteId==null||siteId==undefined||siteId==""||siteId=="NEXT"){
										Ext.MessageBox.alert("提示", "请先保存站点信息");
										return;
									}
									file.previousSibling().setSrc("");
									//获取该类									
									tzParams = '{"ComID":"TZ_GD_ZDGL_COM","PageID":"TZ_GD_ZDDY_STD","OperateType":"BUTTON","comParams":{"type":"removeLogo","siteId":"' + siteId + '"}}';
									Ext.tzSubmit(tzParams,function(response){},"","删除成功");
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
										var pform = file.findParentByType("form").findParentByType("form").findParentByType("form").getForm();
										var siteId = pform.findField("siteId").getValue();
										if(siteId==null||siteId==undefined||siteId==""||siteId=="NEXT"){
											Ext.MessageBox.alert("提示", "请先保存站点信息");
											return;
										}
										var form = file.findParentByType("form").getForm();

										//获取后缀
										var fix = value.substring(value.lastIndexOf(".") + 1,value.length);
										if(fix.toLowerCase() == "jpg" || fix.toLowerCase() == "jpeg" || fix.toLowerCase() == "jpeg" || fix.toLowerCase() == "png" || fix.toLowerCase() == "gif" || fix.toLowerCase() == "bmp" || fix.toLowerCase() == "ico"){
											form.submit({
												url: TzUniversityContextPath + '/UpdServlet?filePath=website',
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
																	
													tzParams = '{"ComID":"TZ_GD_ZDGL_COM","PageID":"TZ_GD_ZDDY_STD","OperateType":"BUTTON","comParams":{"type":"addLogo","logoUrl":"' + path + '","siteId":"' + siteId + '"}}';
													Ext.tzSubmit(tzParams,function(response){},"","上传成功");
																
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
		        	},{
						xtype:'ueditor',
						fieldLabel:'关于我们',
						height:300,
						zIndex:900,
						name:'mobileDesc'
					}]
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