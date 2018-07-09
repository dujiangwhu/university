Ext.define('KitchenSink.view.trainOrgTimeCardMg.trainOrgTimeCardAssignList', {
    extend: 'Ext.panel.Panel',
	requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
		'KitchenSink.view.trainOrgTimeCardMg.trainOrgTimeCardController',
        'KitchenSink.view.trainOrgTimeCardMg.trainOrgTimeCardAssignListStore'
    ],
    xtype: 'trainOrgTimeCardAssignList',
	controller: 'trainOrgTimeCardMg',
    title:Ext.tzGetResourse("TZ_PX_KS_FPCX_COM.TZ_KS_ASGN_HIS_STD.titleOrderView","机构课时分配查询"),
    bodyStyle:'overflow-y:auto;overflow-x:hidden',
    listeners:{
        resize:function( panel, width, height, oldWidth, oldHeight, eOpts ){
            var buttonHeight = 44;
            var grid = panel.child('grid');
            if(grid) grid.setHeight( height-buttonHeight-16);
        }
    },
	orgId:Ext.tzOrgID,
    initComponent: function (){
        var store = new KitchenSink.view.trainOrgTimeCardMg.trainOrgTimeCardAssignListStore();
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
                        {text:"查询",tooltip:"查询数据",iconCls: "query",handler:'queryTimeCardAssignHis'}
                    ]
                }],
                columns: [{
                    //text: '机构编号',
                    text:Ext.tzGetResourse("TZ_PX_KS_FPCX_COM.TZ_KS_ASGN_HIS_STD.orgId","机构编号"),
                    dataIndex: 'orgId',
					hidden:true
                },{
                    //text: '机构名称',
                    text:Ext.tzGetResourse("TZ_PX_KS_FPCX_COM.TZ_KS_ASGN_HIS_STD.orgName","机构名称"),
                    sortable: true,
                    dataIndex: 'orgName',
                    flex: 1
                },{
                    text:Ext.tzGetResourse("TZ_PX_KS_FPCX_COM.TZ_KS_ASGN_HIS_STD.stuName","学员姓名"),
                    sortable: true,
                    dataIndex: 'stuName',
                    width: 120
                },{
                    text:Ext.tzGetResourse("TZ_PX_KS_FPCX_COM.TZ_KS_ASGN_HIS_STD.stuPhone","学员手机"),
                    sortable: true,
                    dataIndex: 'stuPhone',
                    width: 120
                },{
                    text:Ext.tzGetResourse("TZ_PX_KS_FPCX_COM.TZ_KS_ASGN_HIS_STD.stuTimeCardAssign","分配课时卡数"),
                    sortable: true,
                    dataIndex: 'stuTimeCardAssign',
                    width: 120
                },{
                    text:Ext.tzGetResourse("TZ_PX_KS_FPCX_COM.TZ_KS_ASGN_HIS_STD.stuTimeCardAssignAfter","分配后课时卡数"),
                    sortable: true,
                    dataIndex: 'stuTimeCardAssignAfter',
                    width: 120
                },{
                    
                    text:Ext.tzGetResourse("TZ_PX_KS_FPCX_COM.TZ_KS_ASGN_HIS_STD.stuTimeCardAssignTime","分配时间"),
                    sortable: true,
                    dataIndex: 'stuTimeCardAssignTime',
                    width: 140
                },{
                    
                    text:Ext.tzGetResourse("TZ_PX_KS_FPCX_COM.TZ_KS_ASGN_HIS_STD.stuTimeCardAssignOprName","分配操作人"),
                    sortable: true,
                    dataIndex: 'stuTimeCardAssignOprName',
                    width: 120
                }],
                store: store,
                bbar: {
                    xtype: 'pagingtoolbar',
                    pageSize: 10,
                    listeners:{
						afterrender: function(pbar){
							var grid = pbar.findParentByType("grid");
							var panel = grid.findParentByType("panel");
							var gridStore = grid.store;
							gridStore.tzStoreParams ='{"cfgSrhId":"TZ_PX_KS_FPCX_COM.TZ_KS_ASGN_HIS_STD.PX_STU_KS_CHG_V","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+panel.orgId+'"}}',
							pbar.setStore(gridStore);
						}
					},
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
