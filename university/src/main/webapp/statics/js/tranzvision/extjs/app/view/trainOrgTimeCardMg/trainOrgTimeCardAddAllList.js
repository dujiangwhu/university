Ext.define('KitchenSink.view.trainOrgTimeCardMg.trainOrgTimeCardAddAllList', {
    extend: 'Ext.panel.Panel',
	requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
		'KitchenSink.view.trainOrgTimeCardMg.trainOrgTimeCardController',
        'KitchenSink.view.trainOrgTimeCardMg.trainOrgTimeCardAddAllListStore'
    ],
    xtype: 'trainOrgTimeCardAddAllList',
	controller: 'trainOrgTimeCardMg',
    title:Ext.tzGetResourse("TZ_KS_ADD_SRCH_COM.TZ_KS_ADD_SRCH_STD.titleOrderView","机构课时订购查询"),
    bodyStyle:'overflow-y:auto;overflow-x:hidden',
    listeners:{
        resize:function( panel, width, height, oldWidth, oldHeight, eOpts ){
            var buttonHeight = 44;
            var grid = panel.child('grid');
            if(grid) grid.setHeight( height-buttonHeight-16);
        }
    },
    initComponent: function (){
        var store = new KitchenSink.view.trainOrgTimeCardMg.trainOrgTimeCardAddAllListStore();
        Ext.apply(this, {
            items: [{
                xtype: 'grid',
                store: store,
                columnLines: true,
                style:"margin:8px",
                viewConfig: {
                    enableTextSelection: true
                },
                header:false,
                frame: true,
                dockedItems:[{
                    xtype:"toolbar",
                    items:[
                        {text:"查询",tooltip:"查询数据",iconCls: "query",handler:'queryTimeCardAddAll'}
                    ]
                }],
                columns: [{
                    //text: '机构编号',
                    text:Ext.tzGetResourse("TZ_PX_KSGL_COM.TZ_PX_KSGL_STD.orgId","机构编号"),
                    dataIndex: 'orgId',
					hidden:true
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
                    width: 120
                },{
                    text:Ext.tzGetResourse("TZ_PX_KSGL_COM.TZ_PX_KSGL_STD.orgLxrPhone","联系人手机"),
                    sortable: true,
                    dataIndex: 'orgLxrPhone',
                    width: 120
                },{
                    text:Ext.tzGetResourse("TZ_PX_KSGL_COM.TZ_PX_KSGL_STD.orgTimeCardAdd","充值课时卡数"),
                    sortable: true,
                    dataIndex: 'orgTimeCardAdd',
                    width: 120
                },{
                    text:Ext.tzGetResourse("TZ_PX_KSGL_COM.TZ_PX_KSGL_STD.orgTimeCardHaveAfter","充值后课时卡数"),
                    sortable: true,
                    dataIndex: 'orgTimeCardHaveAfter',
                    width: 120
                },{
                    
                    text:Ext.tzGetResourse("TZ_PX_KSGL_COM.TZ_PX_KSGL_STD.orgTimeCardAddTime","充值时间"),
                    sortable: true,
                    dataIndex: 'orgTimeCardAddTime',
                    width: 140
                },{
                    
                    text:Ext.tzGetResourse("TZ_PX_KSGL_COM.TZ_PX_KSGL_STD.orgTimeCardAddOprName","充值操作人"),
                    sortable: true,
                    dataIndex: 'orgTimeCardAddOprName',
                    width: 120
                }],
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
