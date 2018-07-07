Ext.define('KitchenSink.view.enrollmentManagement.applicationMsMan.mxjgXInfoForm', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.enrollmentManagement.applicationMsMan.msjgXnfoStore',
        'KitchenSink.view.enrollmentManagement.applicationMsMan.msRelativeController'
    ],
    xtype: 'msjgXInfoForm',
    columnLines: true,
    controller: 'msRelativeController',
    name:'msjgXInfoForm',
    style:"margin:8px",
    actType:'add',
    multiSelect: true,
    title: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_XMSJG_STD.msjgXManee","面试结果管理"),
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
                text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_XMSJG_STD.query","查询"),
                iconCls:"query",
                handler:'viewXMsjgMan'
            },'->',{
            	xtype:'splitbutton',
           	 text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_XMSJG_STD.moreOperation","更多操作"),
           	 iconCls:'list',
           	 glyph: 61,
                menu:[{
               	 text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_XMSJG_STD.batchDownload","按搜索结果打包承诺书"),
                 iconCls:"download",
                  handler:'batchDownload'
                },{
                  	 text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_XMSJG_STD.selectedDownload","选中打包承诺书"),
                     iconCls:"download",
                      handler:'selectedDownload'
                    }]
           }
        ]},
        {
        xtype:"toolbar",
        dock:"bottom",
        ui:"footer",
       
        items:['->', {
            minWidth:80,
            text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_XMSJG_STD.publish","发布"),
            iconCls:"publish",
            handler: "publishXMsjgInfo"
    	}, {
            minWidth:80,
            text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_XMSJG_STD.cancel","撤销发布"),
            iconCls:"cancel",
            handler: "cancelXMsjgInfo"
    	},{
            minWidth:80,
            text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_XMSJG_STD.save","保存"),
            iconCls:"save",
            handler: "saveXMsjg"
        },{
            minWidth:80,
            text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_XMSJG_STD.ensure","确定"),
            iconCls:"ensure",
            handler: "ensureXMsjg"
        },
            {
                minWidth:80,
                text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_XMSJG_STD.close","关闭"),
                iconCls:"close",
                handler: function(btn){
                    var grid = btn.findParentByType("grid");
                    grid.close();
                }
            }]
        }
    ],
    initComponent: function () {
        var store = new KitchenSink.view.enrollmentManagement.applicationMsMan.msjgXnfoStore();
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
             }],
            columns: [{
                text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_XMSJG_STD.oprid","Oprid"),
                dataIndex: 'oprid',
                hidden:true
            },{
                xtype:'rownumberer',
                minWidth:40,
                maxWidth:80
            },{
                text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_XMSJG_STD.mshId","面试申请号"),
                dataIndex: 'mshId',
                width:100,
                filter: {
                    type: 'string',
                    itemDefaults: {
                        emptyText: 'Search for...'
                    }
                }
            },{
                text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_XMSJG_STD.name","姓名"),
                dataIndex: 'name',
                minWidth:80,
                flex:1,
                filter: {
                    type: 'string',
                    itemDefaults: {
                        emptyText: 'Search for...'
                    }
                }
            },{
                text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_MSJG_STD.msjg","面试结果"),
                dataIndex: 'msjg',
                minWidth:120,
                flex:1,
                filter: {
                    type: 'string',
                    itemDefaults: {
                        emptyText: 'Search for...'
                    }
                },
                editor:{
                    xtype:'combobox',
                    store:{
                        fields: [
                            {
                                name: 'msjg'
                            },
                            {
                                name: 'msjgDesc'
                            }
                        ],
                        data: [
                            {
                                msjg: '未通过',
                                msjgDesc: '未通过'
                            },
                            {
                                msjg: '等待',
                                msjgDesc: '等待'
                            },
                            {
                                msjg: '通过',
                                msjgDesc: '通过'
                            }
                        ]
                    },
                    displayField: 'msjgDesc',
                    valueField: 'msjg',
                    editable: false,
                    triggers:{
                        clear: {
                            cls: 'x-form-clear-trigger',
                            handler: function(field){
                                field.setValue("");
                            }
                        }
                    }
                }
            },{
                text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_MSJG_STD.isAban","是否放弃"),
                dataIndex: 'isAban',
                lockable   : false,
                width: 95,
                editor:{
                    xtype:'combobox',
                    store:{
                        fields: [
                            {
                                name: 'isAban'
                            },
                            {
                                name: 'isAbanDesc'
                            }
                        ],
                        data: [
                            {
                                isAban: 'Y',
                                isAbanDesc: '是'
                            },
                            {
                                isAban: 'N',
                                isAbanDesc: '否'
                            }
                        ]
                    },
                    displayField: 'isAbanDesc',
                    valueField: 'isAban',
                    editable: false,
                    triggers:{
                        clear: {
                            cls: 'x-form-clear-trigger',
                            handler: function(field){
                                field.setValue("");
                            }
                        }
                    }
                },
                renderer:function(value, cellmeta, record, rowIndex ,colIndex ,store, view){
                    if (value == 'Y') {
                        return "是";
                    } else if (value == 'N') {
                        return "否";
                    }
                }
            },{
                text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_XMSJG_STD.isFlag","面试结果发布状态"),
                dataIndex: 'isFlag',
                width:140,
                renderer:function(value, cellmeta, record, rowIndex ,colIndex ,store, view){
                    if (value == 'Y') {
                        return "发布";
                    } else if (value == 'N') {
                        return "未发布";
                    }
                }
            },{
                text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_XMSJG_STD.lqsFlag","录取通知书接收方式"),
                dataIndex: 'lqsFlag',
                width:140,
                renderer:function(value, cellmeta, record, rowIndex ,colIndex ,store, view){
                    if (value == 'A') {
                        return "邮寄";
                    } else if (value == 'B') {
                        return "自取";
                    }
                }
            },{ xtype:'linkcolumn',
                text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_XMSJG_STD.viewMsResult","预览面试结果"),
                dataIndex: 'viewMsResult',
                width:140,
                hideable:false,
                menuDisabled:true,
                items:[{
                    getText:function(v, meta, rec) {
                        return this.text;
                    },
                    handler: "viewXMsResult"
                }],
            },{ 
            	xtype:'linkcolumn',
                text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_XMSJG_STD.downloadCN","下载承诺书"),
                dataIndex: 'downloadCN',
                width:140,
                hideable:false,
                menuDisabled:true,
                items:[{
                    getText:function(v, meta, rec) {
                    	if(v==""||v=="nullnull"){
                    		return "无";
                    	}else{
                    		return this.text;
                    	}
                    },
                    handler: "download"
                }],
            },{
			   xtype: 'actioncolumn',
			   text: '操作',	
               menuDisabled: true,
			   menuText: '上传',
               sortable: false,
               width:100,
			   align: 'center',
			   			 items:[
			   			  {text: '上传',iconCls: 'upload',tooltip: '上传承诺书',handler:'upload'}
			   			]
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

