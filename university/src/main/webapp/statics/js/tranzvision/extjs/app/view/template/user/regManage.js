Ext.define('KitchenSink.view.template.user.regManage', {
    extend: 'Ext.panel.Panel',
    xtype: 'regManage',
    controller: 'regManage',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'KitchenSink.view.template.user.regManageController',
        'KitchenSink.view.template.user.regManageStore'
    ],
    title: '用户注册项管理',
    bodyStyle: 'overflow-y:auto;overflow-x:hidden',
    constructor: function (config) {
    	if(config == ""){
    		this.siteId = "";
    	}else{
   		 	this.siteId = config.siteId;
    	}
		this.callParent();
	},
    listeners: {
        afterrender: function(panel) {
        	var siteId = this.siteId;
            var form = panel.child('form').getForm();

            var tzParams = '{"ComID":"TZ_USER_REG_COM","PageID":"TZ_REGGL_STD","OperateType":"QF","comParams":{"siteId": "' + siteId + '"}}';
            Ext.tzLoad(tzParams,
                function(responseData) {
                    var formData = responseData.formData;
                    form.setValues(formData);
                    
                    var mobileLogo = formData.mobileLogo;
                    if(mobileLogo!=""&&mobileLogo!=null){
                    	panel.down("image").setSrc(TzUniversityContextPath + mobileLogo);
                    }
                    var grid = panel.child("grid");
        			//注册项数据
        			var store = grid.getStore();
        			var tzStoreParams = '{"siteId":"'+siteId+'"}';
        			store.tzStoreParams = tzStoreParams;
        			store.load();
                });
        }
    },
    items: [
        {
            xtype: 'form',
            reference: 'themeForm',
            layout: {
                type: 'vbox',
                align: 'stretch'
            },
            border: false,
            bodyPadding: 10,
            bodyStyle: 'overflow-y:auto;overflow-x:hidden',

            fieldDefaults: {
                msgTarget: 'side',
                labelWidth: 110,
                labelStyle: 'font-weight:bold'
            },

            items: [   
                {
                	xtype: 'textfield',
                	fieldLabel: '站点id',
                    name: 'siteId',
                    readOnly:true,
                    cls:'lanage_1'
                },
                {
                    xtype: 'textfield',
                    fieldLabel: '机构名称',
                    name: 'name',
                    readOnly:true,
                    cls:'lanage_1'
                },
                {
                    xtype: 'checkboxgroup',
                    fieldLabel: '注册方式',
                    columns:1,
                    allowBlank: false,
                    blankText:"您至少选择一种注册方式！",
                    items:[
                        {boxLabel:'邮件',name:'activate',inputValue:'EMAIL'},
                        {boxLabel:'短信',name:'activate',inputValue:'MOBILE'}
                    ]
                }, {
                    xtype: 'checkboxfield',
                    name: 'photo',
                    boxLabel:'<span style="font-weight:bold;">显示首页个人头像</span>',
                    hideLabel:true
                },
                {
	        		xtype: 'form',
	        		layout: 'hbox',
	        		width:'100%',
	        		name:'uploadForm',
					defaults:{
						margin:'0 20px 0 20px',
					},
					items:[{
			            xtype: 'hidden',
			            name: 'mobileLogoImg'
			        },{
						margin:'10 35 0 0',		
						xtype:'label',
						html:'<span style="font-weight:bold">个人头像区背景图:</span>'
					},{
						xtype:'image',
						width:120,
						height:36,
						border:1,
						style: {
							borderColor: '#eee'
						},
						margin:'0 20 0 0',
						src:''
					},{
						xtype:'button',
						text:'删除',
						listeners:{
							click:function(file, value, eOpts ){
								var pform = file.findParentByType("form").findParentByType("form").getForm();
								var siteId = pform.findField("siteId").getValue();
								if(siteId==null||siteId==undefined||siteId==""||siteId=="NEXT"){
									Ext.MessageBox.alert("提示", "请先保存站点信息");
									return;
								}
								file.previousSibling().setSrc("");
								//获取该类									
								tzParams = '{"ComID":"TZ_USER_REG_COM","PageID":"TZ_REGGL_STD","OperateType":"BUTTON","comParams":{"type":"removeLogo","siteId":"' + siteId + '"}}';
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
									var pform = file.findParentByType("form").findParentByType("form").getForm();
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
																
												tzParams = '{"ComID":"TZ_USER_REG_COM","PageID":"TZ_REGGL_STD","OperateType":"BUTTON","comParams":{"type":"addLogo","logoUrl":"' + path + '","siteId":"' + siteId + '"}}';
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
                    xtype:'checkboxfield',
                    name:'photo2',
                    hideLabel:true,
                    boxLabel: '<span style="font-weight:bold;">显示个人信息头像</span>'
               },{
                	xtype: 'textfield',
                    fieldLabel: '注册页面静态化路径',
                    name: 'enrollDir',
                }
            ]
        },
        {
            xtype: 'grid',
            title: '注册项列表',
            minHeight:500,
            frame: true,
            columnLines: true,
            style: "margin:0 8px",
            multiSelect: true,
            viewConfig: {
                enableTextSelection: true
            },

            plugins: [{
                ptype: 'cellediting',
                listeners: {
                    beforeedit: function( editor, context, eOpts ){
                        var dataIndex = context.field;
                        var isSysField = context.record.data.isSysField;
                        if(dataIndex == "regFieldType" && isSysField =="Y"){
                            editor.cancelEdit();
                            return false;
                        }
                        var regFieldType = context.record.data.regFieldType;
                        if(dataIndex == "defaultValue" && regFieldType == "DROP"){
                            editor.cancelEdit();
                            return false;
                        }
                    }
                }
            }],
            store: {
                type: 'regManageStore'
            },
            viewConfig: {
                plugins: {
                    ptype: 'gridviewdragdrop',
                    dragText: '拖拽进行选项的排序'
                },
                listeners: {
                    drop: function(node, data, dropRec, dropPosition) {
                        data.view.store.beginUpdate();
                        var items = data.view.store.data.items;

                        for (var i = 0; i < items.length; i++) {
                            items[i].set('order', i + 1);
                        }
                        data.view.store.endUpdate();
                    }
                }
            },
            columns: [
				{
				    text: '站点ID',
				    dataIndex: 'siteId',
				    width: 60,
				    hidden: true
				},     
                {
                    text: '顺序',
                    dataIndex: 'order',
                    width: 60
                },
                {
                    text: '注册项ID',
                    dataIndex: 'regId',
                    width: 140
                },
                {
                    text: '名称',
                    dataIndex: 'regName',
                    minWidth: 100,
                    flex: 1,
                    editor: {
                        xtype: 'textfield',
                        allowBlank: false,
                        emptyText:"不允许为空！"
                    }
                },
                {
                    text: '英文名称',
                    dataIndex: 'regEnName',
                    minWidth: 100,
                    flex: 1,
                    editor: {
                        xtype: 'textfield',
                        allowBlank: true
                    }
                },
                {
                    xtype: 'checkcolumn',
                    text: '是否启用',
                    dataIndex: 'isEnable',
                    width:100,
                    listeners: {
                        "beforecheckchange": function(col, rowIndex, checked, eOpts) {
                            var store = col.findParentByType("grid").store;
                            var regId = store.getAt(rowIndex).data.regId;

                            if(regId == "TZ_GENDER" || regId == "TZ_EMAIL" || regId == "TZ_PASSWORD" || regId == "TZ_REPASSWORD" ){
                                return false;
                            }
                            /*取消启用时，要求检查TZ_REALNAME、TZ_FIRST_NAME、TZ_LAST_NAME必选其一*/
                            if(!checked){
                                var enableSize = 0;
                                for(var i=0; i< store.getCount(); i++){
                                    var zcxbh = store.getAt(i).data.regId;
                                    var sfqy = store.getAt(i).data.isEnable;
                                    if((zcxbh == "TZ_REALNAME" || zcxbh == "TZ_FIRST_NAME" || zcxbh == "TZ_LAST_NAME") && sfqy && zcxbh != regId ){
                                        enableSize ++;
                                    }
                                }
                                if((regId == "TZ_REALNAME" || regId == "TZ_FIRST_NAME" || regId == "TZ_LAST_NAME") && enableSize < 1){
                                    Ext.Msg.alert("提示","姓名、FIRST_NAME、LAST_NAME至少选择一项！");
                                    return false;
                                }
                                /*如果不启用，除去姓名、性别、邮件外， 后面的是否外网显示、是否必填设置清空*/
                                if(regId != "TZ_REALNAME" && regId != "TZ_GENDER" && regId != "TZ_EMAIL"){
                                    var record = store.getAt(rowIndex);
                                    record.data.isShowWzsy = false;
                                    //console.log(record.data);
                                    record.data.isRequired = false;
                                    record.data.isPerfectInfo = false;
                                    record.data.isReg = false;
                                    record.data.isZhgl = false;
                                   // record.set('isShowWzsy',false);
                                }
                                if(regId == "TZ_REALNAME"){
                                    var record2 = store.getAt(rowIndex);
                                    record2.data.isRequired = false;
                                }
                            }
                        }
                    },
                    renderer:function(value, cellmeta, record, rowIndex ,colIndex ,store, view){
                        var regId = record.get('regId');
                        if (regId == "TZ_GENDER" || regId == "TZ_EMAIL" || regId == "TZ_PASSWORD" || regId == "TZ_REPASSWORD" ){
                            //如何为当前TD添加disabled属性？
                            cellmeta.tdCls = "x-item-disabled";
                        }
                        return (new Ext.grid.column.CheckColumn).renderer(value);
                    }
                },
                {
                    text: '注册项类型',
                    dataIndex: 'regFieldType',
                    width:110,
                    editor: {
                        xtype: 'combobox',
                        store: {
                            fields: [
                                {
                                    name: 'regTypeValue'
                                },
                                {
                                    name: 'regTypeDesc'
                                }
                            ],
                            data: [
                                {
                                    regTypeValue: 'INP',
                                    regTypeDesc: '文本框'
                                },
                                {
                                    regTypeValue: 'DROP',
                                    regTypeDesc: '下拉框'
                                }
                            ]
                        },
                        displayField: 'regTypeDesc',
                        valueField: 'regTypeValue',
                        editable: false
                    },
                    renderer:function(value, cellmeta, record, rowIndex ,colIndex ,store, view){

                        var isSysField = record.get('isSysField');

                        if (isSysField == "Y"){
                            //如何为当前TD添加disabled属性？
                            cellmeta.style = "color:#adadad";
                        }
                        if (value == 'DROP') {
                            return "下拉框";
                        } else {
                            return "文本框";
                        }
                    }
                },
                {
                    text: '默认值',
                    dataIndex: 'defaultValue',
                    minWidth: 40,
                    flex: 1,
                    editor: {
                        xtype: 'textfield'
                    },
                    renderer:function(value, cellmeta, record, rowIndex ,colIndex ,store, view){

                        var regFieldType = record.get('regFieldType');
                        if (regFieldType == "DROP"){
                            cellmeta.tdCls = "x-item-disabled";
                            record.set('defaultValue', "");
                        }
                        return value;
                    }
                },
                {
                    xtype: 'checkcolumn',
                    text: '是否必填',
                    dataIndex: 'isRequired',
                    width:100,
                    listeners: {
                        beforecheckchange: function(col, rowIndex, checked, eOpts) {
                            var store = col.findParentByType("grid").store;
                            var regId = store.getAt(rowIndex).data.regId;

                            var isEnable = store.getAt(rowIndex).data.isEnable;
                            if(isEnable == false ){
                                Ext.Msg.alert("提示","请先启用此项，再进行设置！");
                                return false;
                            }

                            if(regId == "TZ_GENDER" || regId == "TZ_EMAIL" || regId == "TZ_PASSWORD" || regId == "TZ_REPASSWORD" ){
                                return false;
                            }
                        }
                    },
                    renderer:function(value, cellmeta, record, rowIndex ,colIndex ,store, view){

                        var regId = record.get('regId');
                        if (regId == "TZ_GENDER" || regId == "TZ_EMAIL" || regId == "TZ_PASSWORD" || regId == "TZ_REPASSWORD" ){
                            //如何为当前CheckColumn添加disabled属性？
                            cellmeta.tdCls = "x-item-disabled";
                        }else{
                            var isEnable = record.get("isEnable");
                            if(!isEnable){
                               // cellmeta.tdCls = "x-item-disabled";
                            }
                        }

                        return (new Ext.grid.column.CheckColumn).renderer(value);
                    }
                },
                {
                    text: '设置下拉项',
                    width: 130,
                    renderer:function(value, cellmeta, record, rowIndex ,colIndex ,store, view){
                        var isSysField = store.getAt(rowIndex).data.isSysField;
                        var regFieldType = store.getAt(rowIndex).data.regFieldType;

                        if(isSysField == "Y" || regFieldType == "INP"){
                            return '<span style="color:#adadad">设置下拉项</span>';
                        }else{
                            return '<a href="javascript:void(0);">设置下拉项</a>';
                        }
                    },
                    listeners:{
                        click:'onReSetDropVal'
                    }
                },
                {
                    xtype: 'checkcolumn',
                    text: '注册项',
                    dataIndex: 'isReg',
                    width:80,
                    listeners: {
                        beforecheckchange: function(col, rowIndex, checked, eOpts) {
                            var store = col.findParentByType("grid").store;
                            var regId = store.getAt(rowIndex).data.regId;
                            var isEnable = store.getAt(rowIndex).data.isEnable;

                            if(isEnable == false ){
                                Ext.Msg.alert("提示","请先启用此项，再进行设置！");
                                return false;
                            }
                            if(regId == "TZ_GENDER" || regId == "TZ_EMAIL" || regId == "TZ_PASSWORD" || regId == "TZ_REPASSWORD" ){
                                return false;
                            }
                        }
                    },
                    renderer:function(value, cellmeta, record, rowIndex ,colIndex ,store, view){
                        var regId = record.get('regId');
                        if (regId == "TZ_GENDER" || regId == "TZ_EMAIL" || regId == "TZ_PASSWORD" || regId == "TZ_REPASSWORD" ){
                            //如何为当前TD添加disabled属性？
                            cellmeta.tdCls = "x-item-disabled";
                            //邮箱、密码、性别为注册必须项
                            value = true;
                        }
                        return (new Ext.grid.column.CheckColumn).renderer(value);
                    }
                },
                {
                    xtype: 'checkcolumn',
                    text: '完善项',
                    dataIndex: 'isPerfectInfo',
                    width:80,
                    listeners: {
                        beforecheckchange: function(col, rowIndex, checked, eOpts) {
                            var store = col.findParentByType("grid").store;
                            var regId = store.getAt(rowIndex).data.regId;
                            var isEnable = store.getAt(rowIndex).data.isEnable;

                            if(isEnable == false ){
                                Ext.Msg.alert("提示","请先启用此项，再进行设置！");
                                return false;
                            }

                            if(regId == "TZ_PASSWORD" || regId == "TZ_REPASSWORD" || regId == "TZ_EMAIL"){
                                return false;
                            }
                        }
                    },
                    renderer:function(value, cellmeta, record, rowIndex ,colIndex ,store, view){

                        var regId = record.get('regId');

                        if (regId == "TZ_PASSWORD" || regId == "TZ_REPASSWORD"||regId == "TZ_EMAIL"){
                            var isEnable = store.getAt(rowIndex).data.isEnable;

                            //如何为当前CheckColumn添加disabled属性？
                            cellmeta.tdCls = "x-item-disabled";
                            return (new Ext.grid.column.CheckColumn).renderer(false);

                        }
                        else {
                            return (new Ext.grid.column.CheckColumn).renderer(value);
                        }
                    }
                },
                {
                    xtype: 'checkcolumn',
                    text: '首页',
                    dataIndex: 'isShowWzsy',
                    width:80,
                    listeners: {
                        beforecheckchange: function(col, rowIndex, checked, eOpts) {
                            var store = col.findParentByType("grid").store;
                            var regId = store.getAt(rowIndex).data.regId;
                            var isEnable = store.getAt(rowIndex).data.isEnable;

                            if(isEnable == false ){
                                Ext.Msg.alert("提示","请先启用此项，再进行设置！");
                                return false;
                            }

                            if(regId == "TZ_PASSWORD" || regId == "TZ_REPASSWORD" ){
                                return false;
                            }
                        }
                    },
                    renderer:function(value, cellmeta, record, rowIndex ,colIndex ,store, view){

                        var regId = record.get('regId');

                        if (regId == "TZ_PASSWORD" || regId == "TZ_REPASSWORD" ){
                            var isEnable = store.getAt(rowIndex).data.isEnable;

                            //如何为当前CheckColumn添加disabled属性？
                            cellmeta.tdCls = "x-item-disabled";
                            return (new Ext.grid.column.CheckColumn).renderer(false);

                        }/*else if(regId == "TZ_REALNAME" || regId == "TZ_GENDER" || regId == "TZ_EMAIL"){

                            cellmeta.tdCls = "x-item-disabled";
                           // return (new Ext.grid.column.CheckColumn).renderer(true);
                            return (new Ext.grid.column.CheckColumn).renderer(value);*//*保持gird与数据库中的值一致*//*
                        }*/else
                        {
                            return (new Ext.grid.column.CheckColumn).renderer(value);
                        }
                    }
                },
                {
                    xtype: 'checkcolumn',
                    text: '维护项',
                    dataIndex: 'isZhgl',
                    width:80,
                    listeners: {
                        beforecheckchange: function(col, rowIndex, checked, eOpts) {
                            var store = col.findParentByType("grid").store;
                            var regId = store.getAt(rowIndex).data.regId;
                            var isEnable = store.getAt(rowIndex).data.isEnable;

                            if(isEnable == false ){
                                Ext.Msg.alert("提示","请先启用此项，再进行设置！");
                                return false;
                            }

                            if(regId == "TZ_PASSWORD" || regId == "TZ_REPASSWORD"){
                                return false;
                            }
                        }
                    },
                    renderer:function(value, cellmeta, record, rowIndex ,colIndex ,store, view){

                        var regId = record.get('regId');

                        if (regId == "TZ_PASSWORD" || regId == "TZ_REPASSWORD"){
                            var isEnable = store.getAt(rowIndex).data.isEnable;

                            //如何为当前CheckColumn添加disabled属性？
                            cellmeta.tdCls = "x-item-disabled";
                            return (new Ext.grid.column.CheckColumn).renderer(false);

                        }
                        else {
                            return (new Ext.grid.column.CheckColumn).renderer(value);
                        }
                    }
                }
            ]
        }],
    buttons: [
        {
            text: '保存',
            iconCls: "save",
            handler: 'onUserRegSave'
        },
        {
            text: '预览',
            iconCls: "preview",
            handler: 'onUserRegPreView'
        },{
            text: '关闭',
            iconCls:"close",
            handler: 'onPanelClose'
        }
    ]
});