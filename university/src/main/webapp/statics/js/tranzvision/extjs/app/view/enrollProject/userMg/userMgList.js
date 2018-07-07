Ext.define('KitchenSink.view.enrollProject.userMg.userMgList', {
    extend: 'Ext.grid.Panel',
	requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.enrollProject.userMg.userMgController',
        'KitchenSink.view.enrollProject.userMg.userMgStore'
    ],
    xtype: 'userMgGL',//不能变
    controller: 'userMgController',
    columnLines: true,
    selModel: {
        type: 'checkboxmodel'
    },
	style:"margin:8px",
    multiSelect: true,
    title: '申请用户管理',
    viewConfig: {
        enableTextSelection: true
    },
	header:false,
	frame: true,
    dockedItems:[{
		xtype:"toolbar",
		dock:"bottom",
		ui:"footer",
		items:['->',{minWidth:80,text:"保存",iconCls:"save",handler:"onListSave"},
		       {minWidth:80,text:"关闭",iconCls:"close",handler:"onListClose"}]
		},{
		xtype:"toolbar",
		items:[
			{text:"查询",tooltip:"查询数据",iconCls: "query",handler:'queryUser'},"-",
			{text:"查看",tooltip:"查看数据",iconCls: 'view',handler:'viewUserByBtn'},'->',
			{
				xtype:'splitbutton',
				text:'更多操作',
				iconCls:  'list',
				glyph: 61,
				menu:[{
					text:'重置密码',
					handler:'resetPassword'
				},{
					text:'关闭账号',
					handler:'deleteUser'
				},{
					text:'加入黑名单',
					handler:'addHmd'
				},/*{
					text:'邮件发送历史',
					iconCls: 'mail',
					handler:'viewMailHistory'	
				},*/{
					text:'选中申请人另存为听众',
					handler:'saveAsStaAud'	
				},{
					text:'搜索结果另存为听众',
					handler:'saveAsDynAud'	
				}/*,{
					text:'另存为动态听众',
					handler:'saveAsDynAud'	
				}*/,{
					text:'添加到已有听众',
					handler:'saveToStaAud'	
				},
				{
					text:Ext.tzGetResourse("TZ_UM_USERMG_COM.TZ_UM_USERMG_STD.exportApplicantsInfo","导出选中人员信息到Excel"),
					name:'exportExcel',
					handler:'exportExcelOrDownload'
				},{
					text:Ext.tzGetResourse("TZ_UM_USERMG_COM.TZ_UM_USERMG_STD.exportSearchResultInfo","导出搜索结果人员信息到Excel"),
					name:'exportSearchResultExcel',
					handler:'exportSearchResultExcel'
				},
				{
					text:Ext.tzGetResourse("TZ_UM_USERMG_COM.TZ_UM_USERMG_STD.downloadExcel","查看导出结果并下载"),
					name:'downloadExcel',
					handler:'exportExcelOrDownload'
				}]
			}
		]
	}],
    initComponent: function () {    
    	var store = new KitchenSink.view.enrollProject.userMg.userMgStore();
    	/*出现问题，store未执行完成就加载了列表
    	//性别
    	var sexStore = new KitchenSink.view.common.store.appTransStore("TZ_GENDER");
    	//账户激活状态
    	var jihuoStore = new KitchenSink.view.common.store.appTransStore("TZ_JIHUO_ZT");
    	//锁定状态
    	var acctLockStore = new KitchenSink.view.common.store.appTransStore("ACCTLOCK");
    	//黑名单用户
    	var isYnStore = new KitchenSink.view.common.store.appTransStore("TZ_SF_SALE");
    	*/
        var me = this;

        var orgColorSortStore = me.initialData.orgColorSortStore;
    
        /*过滤器Options数据*/
        var colorSortFilterOptions=me.initialData.colorSortFilterOptions;
    	  /*初始颜色类别数据*/
        var initialColorSortData=me.initialData.initialColorSortData;
        var validColorSortStore =  new KitchenSink.view.common.store.comboxStore({
            recname:'TZ_COLOR_SORT_T',
            condition:{TZ_JG_ID:{
                value:Ext.tzOrgID,
                operator:'01',
                type:'01'
            },TZ_COLOR_STATUS:{
                value:'N',
                operator:'02',
                type:'01'
            }},
            result:'TZ_COLOR_SORT_ID,TZ_COLOR_NAME,TZ_COLOR_CODE'
        });

        var dynamicColorSortStore = Ext.create("Ext.data.Store",{
            fields:[
                "TZ_COLOR_SORT_ID","TZ_COLOR_NAME","TZ_COLOR_CODE"
            ],
            data:initialColorSortData
        });
        Ext.apply(this, {
        	plugins: [
                      {
                          ptype: 'gridfilters',
                          controller: 'appFormClass'
                      },
                      {
                          ptype: 'cellediting',
                          clicksToEdit: 1,
                          listeners:{
                        	  beforeedit:function(editor,context){
                        		  var applyInfo = context.record.get("applyInfo");
                         		 if (applyInfo== ""||applyInfo== " "){
                         			 return false;
                         		 }else{
                         			 return true;
                         		 }
                         	}
                          }
                      }
                  ],
            columns: [{ 
                text: '用户ID',
                dataIndex: 'OPRID',
				width: 20,
				hidden:true
            },{
                text: '姓名',
                sortable: true,
                dataIndex: 'userName',
                width: 75
            },{
                text: '身份证号',
                sortable: true,
                dataIndex: 'nationId',
                width: 150
            },{
                text: '性别',
                sortable: true,
				dataIndex: 'userSex',
                width: 55,
                renderer:function(value,metadata,record){
                	if(value=="F"){
                		return "女";
                	}else if(value=="M"){
                		return "男";
                	}else{
                		return value;
                	}
                }
                /*renderer:function(value,metadata,record){
    				if(value == null || value==""){
    					return "";	
    				}
    				var index = sexStore.find('TValue',value);   
    				if(index!=-1){   
    					   return sexStore.getAt(index).data.TSDesc;   
    				}   
    				return value;     				 
    			}*/
            },{
                text: '手机',
                sortable: true,
                dataIndex: 'userPhone',
                width: 100
            },{
                text: '电子邮箱',
                sortable: true,
                dataIndex: 'userEmail',
                width: 180
            },{
                text: '公司名称',
                sortable: true,
                dataIndex: 'company',
                width: 180
            },{
                text: '职位',
                sortable: true,
                dataIndex: 'post',
                width: 180
            },{
	            text: '所属项目',
	            sortable: true,
	            dataIndex: 'applyInfo',
	            width: 200
            },
            {
	            text: '颜色类别',
	            dataIndex: 'sortID',
	            lockable   : false,
                width: 140,
                filter: {
                    type: 'list',
                    options: colorSortFilterOptions
                },
                sortable:true,
                editor: {
                    xtype: 'combo',
                    queryMode:'local',
                    valueField: 'TZ_COLOR_SORT_ID',
                    displayField: 'TZ_COLOR_NAME',
                    triggerAction: 'all',
                    editable : false,
                    triggers:{
                        clear: {
                            cls: 'x-form-clear-trigger',
                            handler: function(field){
                                field.setValue("");
                            }
                        }
                    },
                    store:dynamicColorSortStore,
                    tpl: Ext.create('Ext.XTemplate',
                        '<tpl for=".">',
                        '<div class="x-boundlist-item"><div class="x-colorpicker-field-swatch-inner" style="margin-top:6px;width:30px;height:50%;background-color: #{TZ_COLOR_CODE}"></div><div style="margin-left:40px;display: block;  overflow:  hidden; white-space: nowrap; -o-text-overflow: ellipsis; text-overflow:  ellipsis;"> {TZ_COLOR_NAME}</div></div>',
                        '</tpl>'
                    ),
                    displayTpl: Ext.create('Ext.XTemplate',
                        '<tpl for=".">',
                        '{TZ_COLOR_NAME}',
                        '</tpl>'
                    ),
                    listeners: {
                    	
                        focus: function (combo,event, eOpts) {
                            var selList = this.findParentByType("grid").getView().getSelectionModel().getSelection();
                            var colorSortID =selList[0].raw.sortID;

                            var arrayData = new Array();
                            for(var i=0;i<validColorSortStore.getCount();i++){
                                arrayData.push(validColorSortStore.data['items'][i].data);
                            };
                            if(colorSortID.length>0&&validColorSortStore.find("TZ_COLOR_SORT_ID",colorSortID)==-1){
                                var tmpRec = orgColorSortStore.getAt(orgColorSortStore.find("TZ_COLOR_SORT_ID",colorSortID));
                                arrayData.push(tmpRec.data);
                            };
                            if(arrayData.length<1){
                                arrayData.push({TZ_COLOR_SORT_ID:'',TZ_COLOR_NAME:'',TZ_COLOR_CODE:''});
                            }
                            combo.store.loadData(arrayData);
                        },
//                        blur: function (combo,event, eOpts) {
//                            combo.store.loadData(initialColorSortData);
//                        }
                    }
                },
                renderer:function(v,rowIndex,store){
//                        var applyInfo = store.getAt(rowIndex).data.applyInfo;
//
//                        if(applyInfo == ""){
//                             this.disable();
//                        }else{
//                        	this.enable();
//                        }
                    if(v){
                        var rec = orgColorSortStore.find('TZ_COLOR_SORT_ID',v,0,false,true,true);
                        if(rec>-1){
                            return "<div  class='x-colorpicker-field-swatch-inner' style='width:30px;height:50%;background-color: #"+orgColorSortStore.getAt(rec).get("TZ_COLOR_CODE")+"'></div><div style='margin-left:40px;'>"+orgColorSortStore.getAt(rec).get("TZ_COLOR_NAME")+"</div>";
                        }else{
                            return Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.pleaseSelect","请选择...");
                        }
                    }
                }
            },{
                text: '面试申请号',
                sortable: true,
                dataIndex: 'mshId',
                width: 95
            },{
                text: '面试资格',
                dataIndex: 'mszg',
                width: 95
            },{
                text: '面试结果',
                dataIndex: 'msjg',
                width: 95
            },{
                text: '初录取结果',
                sortable: true,
                dataIndex: 'clqjg',
                width: 95
            },{ 
                text: '激活方式',
                dataIndex: 'jihuoFs',
				width: 75,
				renderer:function(value,metadata,record){
	                	if(value=="M"){
	                		return "手机";
	                	}else if(value=="E"){
	                		return "邮件";
	                	}else{
	                		return "其他";
	                	}
	                }
            },{
                text: '地址',
                sortable: true,
                dataIndex: 'postAddress',
                width: 120
            },{
                text: '邮编',
                sortable: true,
                dataIndex: 'postCode',
                width: 70
            },{
                text: '收件人姓名',
                sortable: true,
                dataIndex: 'sjrName',
                width: 80
            },{
                text: '收件人电话',
                sortable: true,
                dataIndex: 'sjrPhone',
                width: 100
            },{
                text: '账号激活状态',
                sortable: true,
                dataIndex: 'jihuoZt',
                width: 100,
                renderer:function(value,metadata,record){
                	if(value=="Y"){
                		return "已激活";
                	}else{
                		return "未激活";
                	}
                }
                /*renderer:function(value,metadata,record){
    				if(value == null || value==""){
    					return "";	
    				}
    				var index = jihuoStore.find('TValue',value);   
    				if(index!=-1){   
    					   return jihuoStore.getAt(index).data.TSDesc;   
    				}   
    				return value;     				 
    			}*/
            },{
                text: '创建日期时间',
                sortable: true,
                dataIndex: 'zcTime',
                width: 130
            },/*{
                text: '锁定状态',
                sortable: true,
                dataIndex: 'acctlock',
                width: 80,
                hidden:true,
                renderer:function(value,metadata,record){
    				if(value == null || value==""){
    					return "";	
    				}
    				var index = acctLockStore.find('TValue',value);   
    				if(index!=-1){   
    					   return acctLockStore.getAt(index).data.TSDesc;   
    				}   
    				return value;     				 
    			}
            },{
                text: '黑名单',
                sortable: true,
                dataIndex: 'hmdUser',
                width: 60,
                hidden:true,
                renderer:function(value,metadata,record){
    				if(value == null || value==""){
    					return "否";	
    				}
    				var index = isYnStore.find('TValue',value);   
    				if(index!=-1){   
    					   return isYnStore.getAt(index).data.TSDesc;   
    				}   
    				return value;     				 
    			}
            },*/{
			   xtype: 'actioncolumn',
			   text: '操作',	
               menuDisabled: true,
			   menuText: '操作',
               sortable: false,
			   "with":50,
			   align: 'center',
			   			 items:[
			   			  {text: '查看',iconCls: 'view',tooltip: '查看',handler:'viewUserByRow'}
			   			]
            }],
            store: store,
            bbar: {
                xtype: 'pagingtoolbar',
                pageSize: 10,
                store: store,
                plugins: new Ext.ux.ProgressBarPager()
            }
        });
		
        this.callParent();
    }
});
