Ext.define('KitchenSink.view.trainOrgTimeCardMg.trainOrgTimeCardMg', {
    extend: 'Ext.panel.Panel',
	requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
		'KitchenSink.view.trainOrgTimeCardMg.trainOrgTimeCardController',
		'KitchenSink.view.trainOrgTimeCardMg.trainOrgListModel',
        'KitchenSink.view.trainOrgTimeCardMg.trainOrgListStore'
    ],
    xtype: 'trainOrgTimeCardMg',
	controller: 'trainOrgTimeCardMg',
    title:Ext.tzGetResourse("TZ_PX_KSGL_COM.TZ_PX_KSGL_STD.titleOrg","机构列表"),
    bodyStyle:'overflow-y:auto;overflow-x:hidden',
    listeners:{
        resize:function( panel, width, height, oldWidth, oldHeight, eOpts ){
            var buttonHeight = 44;
            var grid = panel.child('grid');
            if(grid) grid.setHeight( height-buttonHeight-16);
        }
    },
    initComponent: function (){
        var store = new KitchenSink.view.trainOrgTimeCardMg.trainOrgListStore();
        Ext.apply(this, {
            items: [{
                xtype: 'grid',
                store: store,
                columnLines: true,
                selModel: {
                    type: 'checkboxmodel'
                },
                style:"margin:8px",
                multiSelect: true,
                viewConfig: {
                    enableTextSelection: true
                },
                header:false,
                frame: true,
                dockedItems:[{
                    xtype:"toolbar",
                    items:[
                        {text:"查询",tooltip:"查询数据",iconCls: "query",handler:'queryOrgList'},
                        {text:"新增订购",tooltip:"新增订购",iconCls:"add",handler:'addTimeCardOrder'},
                        {text:"订购记录查询",tooltip:"订购记录查询",iconCls:"view",handler:'queryOrgOrderHis'},
						{text:"分配记录查询",tooltip:"分配记录查询",iconCls:"view",handler:'queryOrgAssignHis'}
                    ]
                }],
                columns: [{
                    //text: '机构编号',
                    text:Ext.tzGetResourse("TZ_PX_KSGL_COM.TZ_PX_KSGL_STD.orgId","机构编号"),
                    dataIndex: 'orgId',
                    width: 120
                },{
                    //text: '机构名称',
                    text:Ext.tzGetResourse("TZ_PX_KSGL_COM.TZ_PX_KSGL_STD.orgName","机构名称"),
                    sortable: true,
                    dataIndex: 'orgName',
                    flex: 1
                },{
                    text:Ext.tzGetResourse("TZ_PX_KSGL_COM.TZ_PX_KSGL_STD.orgLxrName","联系人姓名"),
                    sortable: true,
                    dataIndex: 'orgLxrName',
                    width: 110
                },{
                    text:Ext.tzGetResourse("TZ_PX_KSGL_COM.TZ_PX_KSGL_STD.orgLxrPhone","联系人手机"),
                    sortable: true,
                    dataIndex: 'orgLxrPhone',
                    width: 110
                },{
                    text:Ext.tzGetResourse("TZ_PX_KSGL_COM.TZ_PX_KSGL_STD.orgTimeCardHave","拥有的课时卡"),
                    sortable: true,
                    dataIndex: 'orgTimeCardHave',
                    width: 100
                },{
                    
                    text:Ext.tzGetResourse("TZ_PX_KSGL_COM.TZ_PX_KSGL_STD.orgTimeCardAssign","分配的课时卡"),
                    sortable: true,
                    dataIndex: 'orgTimeCardAssign',
                    width: 100
                }/*,{
                    menuDisabled: true,
                    sortable: false,
                    width:40,
                    xtype: 'actioncolumn',
                    items:[
                        {iconCls: 'edit',tooltip: '编辑',handler:'editOrgAccountOne'}
                    ]
                }*/],
                store: store,
                bbar: {
                    xtype: 'pagingtoolbar',
                    pageSize: 10,
                    /*store: {
                     type: 'orgListStore'
                     },*/
                    store: store,
                    displayInfo: true,
                    displayMsg: '显示{0}-{1}条，共{2}条',
                    beforePageText: '第',
                    afterPageText: '页/共{0}页',
                    emptyMsg: '没有数据显示',
                    plugins: new Ext.ux.ProgressBarPager()
                }

            }]
        });
        this.callParent();
    },
    buttons: [{
        text: '关闭',
        iconCls:"close",
        handler: 'onPanelClose'
    }]
});
