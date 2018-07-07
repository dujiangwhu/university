Ext.define('KitchenSink.view.enrollmentManagement.applicationMsMan.bscjInfoForm', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.enrollmentManagement.applicationMsMan.bscjInfoStore',
        'KitchenSink.view.enrollmentManagement.applicationMsMan.msRelativeController'
    ],
    xtype: 'bscjInfoForm',
    columnLines: true,
    controller: 'msRelativeController',
    name:'bscjInfoForm',
    style:"margin:8px",
    multiSelect: true,
    actType:'add',
    title: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_BSCJ_STD.bscjManege","笔试成绩管理"),
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
                    text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_BSCJ_STD.query","查询"),
                    iconCls:"query",
                    handler:'viewBscjMan'
                }
            ]},
        {
            xtype:"toolbar",
            dock:"bottom",
            ui:"footer",

            items:['->',{
                minWidth:80,
                text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_MSZG_STD.publish","发布"),
                iconCls:"publish",
                handler: "publishBscjInfo"
            }, {
                minWidth:80,
                text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_MSZG_STD.cancel","撤销发布"),
                iconCls:"cancel",
                handler: "cancelBscjInfo"
            },{
                minWidth:80,
                text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_MSJG_STD.save","保存"),
                iconCls:"save",
                handler: "saveBscj"
            },{
                minWidth:80,
                text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_MSJG_STD.ensure","确定"),
                iconCls:"ensure",
                handler: "ensureBscj"
            },{
                minWidth:80,
                text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_BSCJ_STD.close","关闭"),
                iconCls:"close",
                handler: function(btn){
                    var grid = btn.findParentByType("grid");
                    grid.close();
                }
            }]
        }
    ],
    initComponent: function () {
        var store = new KitchenSink.view.enrollmentManagement.applicationMsMan.bscjInfoStore();
        var clqStore=new KitchenSink.view.common.store.appTransStore("TZ_C_RQZT");
        Ext.apply(this, {
            selModel: {
                type: 'checkboxmodel'
            },
            plugins: [
                {
                    ptype: 'gridfilters',
                    controller: 'msRelativeController'
                }, {
                    ptype: 'cellediting',
                    clicksToEdit:1
                }
        ],
        columns: [{
            text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_BSCJ_STD.oprid","Oprid"),
            dataIndex: 'oprid',
            hidden:true
        },{
            xtype:'rownumberer',
            minWidth:40,
            maxWidth:80
        },{
            text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_BSCJ_STD.mshId","面试申请号"),
            dataIndex: 'mshId',
            width:100,
            filter: {
                type: 'string',
                itemDefaults: {
                    emptyText: 'Search for...'
                }
            }
        },{
            text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_BSCJ_STD.name","姓名"),
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
            text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_BSCJ_STD.zkzh","准考证号"),
            dataIndex: 'zkzh',
            minWidth:120,
            flex:1,
            filter: {
                type: 'string',
                itemDefaults: {
                    emptyText: 'Search for...'
                }
            },
            editor:{xtype:'textfield'}
        },{
            text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_BSCJ_STD.bscjZf","总分"),
            dataIndex: 'bscjZf',
            width:100,
            filter: {
                type: 'string',
                itemDefaults: {
                    emptyText: 'Search for...'
                }
            },
            editor:{xtype:'textfield'}
        },{
            text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_BSCJ_STD.bscjZh","综合"),
            dataIndex: 'bscjZh',
            width:100,
            filter: {
                type: 'string',
                itemDefaults: {
                    emptyText: 'Search for...'
                }
            },
            editor:{xtype:'textfield'}
        },{
            text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_BSCJ_STD.bscjYy","英语"),
            dataIndex: 'bscjYy',
            width:100,
            filter: {
                type: 'string',
                itemDefaults: {
                    emptyText: 'Search for...'
                }
            },
            editor:{xtype:'textfield'}
        },{
            text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_BSCJ_STD.bscjZz","政治"),
            dataIndex: 'bscjZz',
            width: 100,
            filter: {
                type: 'string',
                itemDefaults: {
                    emptyText: 'Search for...'
                }
            },
            editor:{xtype:'textfield'}
        },{
            text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_BSCJ_STD.clqState","初录取情况"),
            dataIndex: 'clqState',
            width:120,
            filter: {
                type: 'string',
                itemDefaults: {
                    emptyText: 'Search for...'
                }
            },
            editor:{
                xtype:'combobox',
                store:clqStore,
                valueField: 'TValue',
                displayField: 'TSDesc',
                editable: false,
                triggers:{
                    clear: {
                        cls: 'x-form-clear-trigger',
                        handler: function(field){
                            field.setValue("");
                        }
                    }
                },
                renderer:function(v){
		            if(v){
		                var index = clqStore.find('TValue',v,0,false,true,true);
		                if(index>-1){
		                    return clqStore.getAt(index).get("TSDesc");
		                }
		                return "";
		            }
		        }
            }
        },{
            text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_BSCJ_STD.bscjFlag","发布状态"),
            dataIndex: 'bscjFlag',
            width:100,
            renderer:function(v){
                if(v=="Y"){
                    return "发布";
                }else{
                    return "未发布";
                }
            }
        },{ xtype:'linkcolumn',
            text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_BSCJ_STD.viewClqResult","预览初录取结果"),
            dataIndex: 'viewClqResult',
            width:140,
            hideable:false,
            menuDisabled:true,
            items:[{
                getText:function(v, meta, rec) {
                    return this.text;
                },
                handler: "viewClqResult"
            }],
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

