Ext.define('KitchenSink.view.enrollmentManagement.applicationMsMan.mszgInfoForm', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.enrollmentManagement.applicationMsMan.mszgInfoStore',
        'KitchenSink.view.enrollmentManagement.applicationMsMan.msRelativeController'
    ],
    xtype: 'mszgInfoForm',
    columnLines: true,
    controller: 'msRelativeController',
    name:'mszgInfoForm',
    style:"margin:8px",
    multiSelect: true,
    actType:'add',
    title: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_MSZG_STD.mszgManege","面试资格管理"),
    viewConfig: {
        enableTextSelection: true
    },
    header:false,
    frame: true,
    dockedItems:[
        {
        xtype:"toolbar",
        items:[
            {
                text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_MSZG_STD.query","查询"),
                iconCls:"query",
                handler:'viewMszgMan'
            }
        ]},
        {
        xtype:"toolbar",
        dock:"bottom",
        ui:"footer",
        items:['->',
               {
	            minWidth:80,
	            text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_MSZG_STD.publish","发布"),
	            iconCls:"publish",
	            handler: "publishMszgrcInfo"
        	},
               {
	            minWidth:80,
	            text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_MSZG_STD.cancel","撤销发布"),
	            iconCls:"cancel",
	            handler: "cancelMszgrcInfo"
        	},{
	            minWidth:80,
	            text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_MSZG_STD.save","保存"),
	            iconCls:"save",
	            handler:"saveMszg"
        	},
        	{
	            minWidth:80,
	            text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_MSZG_STD.ensure","确定"),
	            iconCls:"ensure",
	            handler:"ensureMszg"
        	},
        	{
                minWidth:80,
                text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_MSZG_STD.close","关闭"),
                iconCls:"close",
                handler: function(btn){
                    var grid = btn.findParentByType("grid");
                    grid.close();
                }
            }]
        }
    ],
    initComponent: function () {
        var store = new KitchenSink.view.enrollmentManagement.applicationMsMan.mszgInfoStore();
        Ext.apply(this, {
        	selModel: {
                type: 'checkboxmodel'
            },
        	plugins: [{
			               ptype: 'cellediting',
			               clicksToEdit: 1
		             },{
			               ptype: 'gridfilters',
			               controller: 'msRelativeController'
                      }
                  ],
            columns: [{
                xtype:'rownumberer',
                minWidth:40,
                maxWidth:80
            },{
                text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_MSZG_STD.mshId","面试申请号"),
                dataIndex: 'mshId',
                width:100,
                filter: {
                    type: 'string',
                    itemDefaults: {
                        emptyText: 'Search for...'
                    }
                }
            },{
                text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_MSZG_STD.name","姓名"),
                dataIndex: 'name',
                width:80,
                filter: {
                    type: 'string',
                    itemDefaults: {
                        emptyText: 'Search for...'
                    }
                }
            },{
                text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_MSZG_STD.isMszg","是否有面试资格"),
                dataIndex: 'isMszg',
                width:120,
                filter: {
                    type: 'list'
                },
                editor:{
                	xtype:'combobox',
                	anchor: '100%' ,
                	store:{
                		fields: [
                                 {
                                     name: 'isMszg'
                                 },
                                 {
                                     name: 'isMszgDesc'
                                 }
                             ],
                             data: [
                                 {
                                	 isMszg: '是',
                                	 isMszgDesc: '是'
                                 },{
                                	 isMszg: '等待',
                                	 isMszgDesc: '等待'
                                 },
                                 {
                                	 isMszg: '否',
                                	 isMszgDesc: '否'
                                 }
                             ],
                            
                	},
                	 displayField: 'isMszgDesc',
                     valueField: 'isMszg',
                     editable: false,
                     triggers:{
                         clear: {
                             cls: 'x-form-clear-trigger',
                             handler: function(field){
                                 field.setValue("");
                             }
                         }
                     },
                	} 
                	
            },{
                text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_MSZG_STD.msBatch","面试批次"),
                dataIndex: 'msBatch',
                width:100,
                filter: {
                    type: 'list'
                },
                editor: {
    				xtype: 'combobox',
    					store:{
    						fields: [{name:'value'},{name:'desc'}],
    						data: [{value: '第一批', desc: '第一批'},{value: '第二批', desc: '第二批'},{value: '第三批', desc: '第三批'},{value: '第四批', desc: '第四批'},{value: '第五批', desc: '第五批'},{value: '第六批', desc: '第六批'}]
    							},
    				displayField: 'desc',
    				valueField: 'value',
    				triggers:{
                        clear: {
                            cls: 'x-form-clear-trigger',
                            handler: function(field){
                                field.setValue("");
                            }
                        }
                    }
    			},
    			
    			renderer: function(v){
    			if (v !== null || v !== undefined || v !== '') { 
//    				if(v.length>0){
    					if(v=='第一批'){
        					return "第一批";
        				}else if(v=='第二批'){
        					return "第二批";
        				}else if(v=='第三批'){
        					return "第三批";
        				}else if(v=='第四批'){
        					return "第四批";
        				}else if(v=='第五批'){
        					return "第五批";
        				}else if(v=='第六批'){
        					return "第六批";
        				}
//        				else{
//        					return "其他批次";
//        				}
    				}else{
    					return " ";
    				}
    				
    			}
            },{
                text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_MSZG_STD.mszgFlag","面试资格发布状态"),
                dataIndex: 'mszgFlag',
                width:160,
                renderer:function(v){
					if(v=="Y"){
						return "发布";
						}else{
							return "未发布";
						}
                	}
            },{
                text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_MSZG_STD.msDate","面试日期"),
                dataIndex: 'msDate',
                format: 'Y-m-d',
                width:140,
                renderer:Ext.util.Format.dateRenderer('Y-m-d '),
                editor:{
                	xtype:'datefield',
                	format:'Y-m-d',
                	anchor: '100%' 
                }
            },{
                text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_MSZG_STD.baoTime","报到时间"),
                dataIndex: 'baTime',
                width:140,
                editor:{
                	xtype:'timefield',
                	format:'H:i',
                	anchor: '100%' ,
//                	triggers:{
//                        clear: {
//                            cls: 'x-form-clear-trigger',
//                            handler: function(field){
//                                field.setValue("");
//                            }
//                        }
//                    },
                },
                renderer:Ext.util.Format.dateRenderer('H:i')
            	
            },{
                text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_MSZG_STD.msAddress","面试地点"),
                dataIndex: 'msAddress',
                minWidth:140,
                flex:1,
                editor:{
                	xtype:'textfield'
                	}
            },{
                text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_MSZG_STD.msrcFlag","面试日程发布状态"),
                dataIndex: 'msrcFlag',
                width:160,
                renderer:function(v){
					if(v=="Y"){
						return "发布";
						}else{
							return "未发布";
						}
                	}
            },{xtype:'linkcolumn',
                text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_MSZG_STD.viewMsNotice","预览面试通知"),
                dataIndex: 'viewMsNotice',
                width:140,
                menuDisabled:true,
                hideable:false,
                items:[{
                    getText:function(v, meta, rec) {
                        return this.text;
                    },
                    handler: "viewMsNotice"
                }],
            },{
                text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_MSZG_STD.msfyFlag","面试费支付状态"),
                dataIndex: 'msfyFlag',
                width:140,
                renderer:function(v){
                	if(v=="01"){
                		return "等待支付";
                	}else if(v=="02"){
                		return "支付成功";
                	}else if(v=="03"){
                		return "支付失败";
                	}else if (v=="04"){
                		return "验证失败";
                	}
                }
            },{
                text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_MSZG_STD.mshfFlag","面试回复状态"),
                dataIndex: 'mshfFlag',
                width:140,
                editor:{
                	xtype:'combobox',
                	anchor: '100%' ,
                	store:{
                		fields: [
                                 {
                                     name: 'mshfFlag'
                                 },
                                 {
                                     name: 'mshfFlagDesc'
                                 }
                             ],
                             data: [
                                 {
                                	 mshfFlag: 'Y',
                                	 mshfFlagDesc: '确认参加'
                                 },{
                                	 mshfFlag: 'N',
                                	 mshfFlagDesc: '放弃'
                                 },
                                 {
                                	 mshfFlag: '',
                                	 mshfFlagDesc: '待确认'
                                 }
                             ]
                	},
                	 displayField: 'mshfFlagDesc',
                     valueField: 'mshfFlag',
                     editable: false
                	} ,
                	   renderer:function(v){
                       	if(v=="Y"){
                       		return "确认参加";
                       	}else if(v=="N"){
                       		return "放弃";
                       	}else if(v==""){
                       		return "待确认";
                       	}
                       }
            }],
            store: store,
            bbar: {
                xtype: 'pagingtoolbar',
                pageSize: 30,
                store: store,
                plugins: new Ext.ux.ProgressBarPager()
            }
        });

        this.callParent();
    }
});

