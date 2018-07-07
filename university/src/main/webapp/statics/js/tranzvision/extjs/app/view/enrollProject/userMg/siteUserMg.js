Ext.define('KitchenSink.view.enrollProject.userMg.siteUserMg', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.enrollProject.userMg.userMgController',
        'KitchenSink.view.enrollProject.userMg.siteUserMgStore'
    ],
    xtype: 'siteUserMg',//不能变
    controller: 'userMgController',
    columnLines: true,
    selModel: {
       // type: 'checkboxmodel'
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
        items:['->',{minWidth:80,text:"关闭",iconCls:"close",handler:"onSiteListClose"}]
    },{
        xtype:"toolbar",
        items:[
            {text:"查询",tooltip:"查询数据",iconCls: "query",handler:'querySite'}
        ]
    }],
    initComponent: function () {
        var store = new KitchenSink.view.enrollProject.userMg.siteUserMgStore();

        Ext.apply(this, {
            columns: [{
                text: '站点ID',
                dataIndex: 'siteID',
                width: 100,
                hidden:true
            },{
                text:  Ext.tzGetResourse("TZ_UM_USERMG_COM.TZ_UM_USER_STD.sitename",'名称'),
                sortable: true,
                dataIndex: 'siteName',
                minWidth: 75,
                flex:1
           },{
        	  // xtype:'linkcolumn',
                text:Ext.tzGetResourse("TZ_UM_USERMG_COM.TZ_UM_USER_STD.view","查看申请用户"),
                dataIndex: 'viewUser',
                width:140,
                renderer:function(){
                    return '<a href="javascript:void(0)" >查看申请用户</a>';
                },
                listeners:{
                    click:'viewSiteUser'
                }
            }],
            store: store,
            bbar: {
                xtype: 'pagingtoolbar',
                pageSize: 20,
                store: store,
                plugins: new Ext.ux.ProgressBarPager()
            }
        });

        this.callParent();
    }
});
