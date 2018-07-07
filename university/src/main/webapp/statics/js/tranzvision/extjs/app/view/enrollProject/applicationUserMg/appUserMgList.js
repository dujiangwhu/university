Ext.define('KitchenSink.view.enrollProject.applicationUserMg.appUserMgList', {
    extend: 'Ext.grid.Panel',
	requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.enrollProject.applicationUserMg.appUserMgController',
        'KitchenSink.view.enrollProject.applicationUserMg.appUserMgStore'
    ],
    xtype: 'appUserMgGL',//不能变
    controller: 'appUserMgController',
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
		items:['->',
		       {minWidth:80,text:"保存",iconCls:"save",handler:"onListSave"},
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
				menu:[
				{
					text:Ext.tzGetResourse("TZ_BMGL_APPL_COM.TZ_UM_USERMG_STD.exportApplicantsInfo","导出选中人员信息到Excel"),
					name:'exportExcel',
					handler:'exportExcelOrDownload'
				},{
					text:Ext.tzGetResourse("TZ_BMGL_APPL_COM.TZ_UM_USERMG_STD.exportSearchResultInfo","导出搜索结果人员信息到Excel"),
					name:'exportSearchResultExcel',
					handler:'exportSearchResultExcel'
				},
				{
					text:Ext.tzGetResourse("TZ_BMGL_APPL_COM.TZ_UM_USERMG_STD.downloadExcel","查看导出结果并下载"),
					name:'downloadExcel',
					handler:'exportExcelOrDownload'
				},
                    {
                        text:Ext.tzGetResourse("TZ_BMGL_APPL_COM.TZ_UM_USERMG_STD.changeProject","调整选中人报考项目"),
                        handler:'changeProject'
                    }
                ]
			}
		]
	}],
	/*
	constructor: function (cfg){
        Ext.apply(this,cfg);
        this.callParent();
    },*/
    initComponent: function () {    
    	var store = new KitchenSink.view.enrollProject.applicationUserMg.appUserMgStore();
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
        
        var prjID = me.prjID;
        //是否显示自动标签
        var showAutoTags = false;
        var mbaPrj = Ext.tzGetHardcodeValue("TZ_MBA_PRJ_TYPE");
        if(prjID == mbaPrj){
        	showAutoTags = true;
        }
        
        Ext.apply(this, {
        	plugins: [
                      {
                          ptype: 'gridfilters',
                          controller: 'appFormClass'
                      },
                      {
                          ptype: 'cellediting',
                          clicksToEdit: 1
                      }
                  ],
            columns: [{ 
                text: '报名表编号',
                dataIndex: 'appInsID',
				width: 20,
				hidden:true
            },{ 
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
            },{
            	text: '自动标签',
                dataIndex: 'autoTags',
                lockable   : false,
                width: 140,
                menuDisabled: true,
    			sortable: false,
    			hidden: !showAutoTags,
    			xtype: 'templatecolumn',
    			tpl: Ext.create('Ext.XTemplate','{[this.labels(values)]}',{
    				labels: function(values){
    					var labels = "";
    					var val = values.autoTags;
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
    			})
            },{
	            text: '颜色类别',
	            dataIndex: 'sortID',
	            lockable   : false,
                width: 140,
                filter: {
                    type: 'list',
                    options: colorSortFilterOptions
                },
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
                renderer:function(v){
                    if(v){
                        var rec = orgColorSortStore.find('TZ_COLOR_SORT_ID',v,0,false,true,true);
                        if(rec>-1){
                            return "<div  class='x-colorpicker-field-swatch-inner' style='width:30px;height:50%;background-color: #"+orgColorSortStore.getAt(rec).get("TZ_COLOR_CODE")+"'></div><div style='margin-left:40px;'>"+orgColorSortStore.getAt(rec).get("TZ_COLOR_NAME")+"</div>";
                        }else{
                            return Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.pleaseSelect","请选择...");
                        }
                    }
                }
            },
//            {
//	            text: '颜色类别',
//	            sortable: true,
//	            dataIndex: 'sortName',
//	            width: 200
//            },
            {
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
            },{
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
