Ext.define('KitchenSink.view.enrollmentManagement.applicationMsMan.lqjgInfoForm', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.enrollmentManagement.applicationMsMan.lqjgInfoStore',
        'KitchenSink.view.enrollmentManagement.applicationMsMan.msRelativeController'
    ],
    xtype: 'lqjgInfoForm',
    columnLines: true,
    controller: 'msRelativeController',
    name:'lqjgInfoForm',
    style:"margin:8px",
    multiSelect: true,
    actType:'add',
    title: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_LQJG_STD.lqjgInfoForm","录取结果管理"),
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
                text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_LQJG_STD.query","查询"),
                iconCls:"query",
                handler:'viewLqjgMan'
            }
        ]},
        {
        xtype:"toolbar",
        dock:"bottom",
        ui:"footer",
       
        items:['->',{
            minWidth:80,
            text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_LQJG_STD.publish","发布"),
            iconCls:"publish",
            handler: "publishLqjgInfo"
        }, {
            minWidth:80,
            text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_LQJG_STD.cancel","撤销发布"),
            iconCls:"cancel",
            handler: "cancelLqjgInfo"
        },{
            minWidth:80,
            text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_LQJG_STD.save","保存"),
            iconCls:"save",
            handler: "saveLqjg"
        },{
            minWidth:80,
            text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_LQJG_STD.ensure","确定"),
            iconCls:"ensure",
            handler: "ensureLqjg"
        },
            {
                minWidth:80,
                text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_LQJG_STD.close","关闭"),
                iconCls:"close",
                handler: function(btn){
                    var grid = btn.findParentByType("grid");
                    grid.close();
                }
            }]
        }
    ],
    initComponent: function () {
        var store = new KitchenSink.view.enrollmentManagement.applicationMsMan.lqjgInfoStore();
        Ext.apply(this, {
        	selModel:{
        		type:'checkboxmodel'
        	},
             plugins: [
                       {
                           ptype: 'gridfilters',
                           controller: 'msRelativeController'
                       },
                       {
                    	   ptype:'cellediting',
                    	   clicksToEdit:1
                       }
                   ],
            columns: [{
                text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_LQJG_STD.oprid","Oprid"),
                dataIndex: 'oprid',
                hidden:true
            },{
                xtype:'rownumberer',
                minWidth:40,
                maxWidth:80
            },{
                text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_LQJG_STD.mshId","面试申请号"),
                dataIndex: 'mshId',
                width:100,
                filter: {
                    type: 'string',
                    itemDefaults: {
                        emptyText: 'Search for...'
                    }
                }
            },{
                text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_LQJG_STD.name","姓名"),
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
                text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_LQJG_STD.clqState","初录取状态"),
                dataIndex: 'clqState',
                minWidth:100,
                flex:1,
                filter: {
                    type: 'string',
                    itemDefaults: {
                        emptyText: 'Search for...'
                    }
                }
            },{
                text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_LQJG_STD.lqState","录取状态"),
                dataIndex: 'lqState',
                minWidth:100,
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
                                name: 'lqState'
                            },
                            {
                                name: 'lqStateDesc'
                            }
                        ],
                        data: [{
                        	lqState: '已录取',
                        	lqStateDesc: '已录取'
                        },
                            {
                        	lqState: '未录取',
                        	lqStateDesc: '未录取'
                            }
                        ]
                    },
                    displayField: 'lqStateDesc',
                    valueField: 'lqState',
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
                text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_LQJG_STD.isFlag","发布状态"),
                dataIndex: 'isFlag',
                minWidth:100,
                flex:1,
                renderer:function(value, cellmeta, record, rowIndex ,colIndex ,store, view){
                    if (value == 'Y') {
                        return "发布";
                    } else  {
                        return "未发布";
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

