Ext.define('KitchenSink.view.judgesManagement.judgesGroupMg.jugClGroupMg', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.judgesManagement.judgesGroupMg.jugGroupMgModel',
        'KitchenSink.view.judgesManagement.judgesGroupMg.jugClGroupMgStore',
        'KitchenSink.view.judgesManagement.judgesGroupMg.jugClGroupMgController'
    ],
    xtype: 'jugClMg',
    controller: 'jugClMg',
    reference: "jugClMgPanel",
    columnLines: true,
    selModel: {
        type: 'checkboxmodel'
    },
    style: "margin:8px",
    multiSelect: false,
    title: '评委组管理',
    viewConfig: {
        enableTextSelection: false
    },
    header: false,
    frame: true,
    
    initComponent: function () {
    	 var comStore = new KitchenSink.view.common.store.comboxStore({
             recname: 'PS_TZ_JUGTYP_TBL',
             condition: {
                 TZ_JG_ID: {
                     value: Ext.tzOrgID,
                     operator: '01',
                     type: '01'
                 },
                 TZ_JUGTYP_STAT: {
                     value: '0',
                     operator: '01',
                     type: '01'
                 }
             },
             result: 'TZ_JUGTYP_ID,TZ_JUGTYP_NAME'
         });
        //材料评审评委组
        var store = new KitchenSink.view.judgesManagement.judgesGroupMg.jugClGroupMgStore('1');
        Ext.apply(this, {
            store: store,
            columns: [{
                text: '评审组ID',
                sortable: true,
                dataIndex: 'jugGroupId',
                width: 305
            }, {
                text: '评审组名称',
                sortable: true,
                dataIndex: 'jugGroupName',
                width: 305,
                flex: 1
            }, {
                text: '评审组类型',
                sortable: true,
                dataIndex: 'jugGroupTypeN',
                width: 305,
                flex: 1
            }, {
                menuDisabled: true,
                sortable: false,
                width: 50,
                xtype: 'actioncolumn',
                items: [
                    {iconCls: 'edit', tooltip: '编辑', handler: 'editSelData'},
                    {iconCls: 'remove', tooltip: '删除', handler: 'deleteSelData'}
                ]
            }],
            bbar: {
                xtype: 'pagingtoolbar',
                pageSize: 10,
                store: store,
                displayInfo: true,
                displayMsg: '显示{0}-{1}条，共{2}条',
                beforePageText: '第',
                afterPageText: '页/共{0}页',
                emptyMsg: '没有数据显示',
                plugins: new Ext.ux.ProgressBarPager()
            },
            dockedItems:[{xtype:"toolbar",dock:"bottom",ui:"footer",items:['->',{minWidth:80,text:"保存",iconCls:"save",handler:'saveList'},{minWidth:80,text:"确定",iconCls:"ensure",handler:'ensureList'},{minWidth:80,text:"关闭",iconCls:"close",handler:'closeList'}]},{
                xtype: "toolbar",
                items: [
                    {text: "查询", tooltip: "查询数据", iconCls: "query", handler: 'searchDataList'}, '-',
                    {text: "新增", tooltip: "新增数据", iconCls: "add", handler: 'addDataList'}, '-',
                    {text:"编辑",tooltip:"编辑数据",iconCls: "edit",handler:"editDataList"},'-',
        			{text:"删除",tooltip:"批量删除数据",iconCls: "remove",handler:"deleteDataList"},'->',
        			{
                    	xtype: 'combobox',
        	          	fieldLabel:'<div style="font-weight: bold;">'+"筛选规则"+'</div>',
        	          	labelSeparator:'',
        	          	//reference: '',
        	          	//name: '',
        	          	//style:'margin-top:10px',
        	          	store:comStore,
        	          	emptyText : "请选择",
        	          	valueField: 'TZ_JUGTYP_ID',
        	          	displayField: 'TZ_JUGTYP_NAME',
        	          	queryMode:'lcoal',
        	          	// allowBlank: false,
        	          	editable:false,
        	          	value:'1',
        	          	listeners:{
        					change:function(tvType,newValue, oldValue,eOpts){
//        						var selValueq = this.getValue();//得到valueField的值 
        						var store = new KitchenSink.view.judgesManagement.judgesGroupMg.jugClGroupMgStore(newValue);
        						if (newValue==''){
        							tvType.findParentByType("grid").store.load();
        							tvType.findParentByType("grid").child("pagingtoolbar").store.load();
        						}else{
        							tvType.findParentByType("grid").setStore(store);
        							tvType.findParentByType("grid").child("pagingtoolbar").setStore(store);
        						}
        					}
        	          	},
        	          	/*listeners : {//选择一行后触发的事件 
                            'select' : function() {
                                var selValue = this.getValue();//得到valueField的值 
                                var tzParams = '{"ComID":"TZ_CLPS_GROP_COM","PageID":"TZ_CLJUP_INFO_STD","OperateType":"QG","comParams":{"selValue":"' + selValue + '"}}';
                                Ext.tzSubmit(tzParams, function (responseData) {
//                                    var interviewMgrPanel = Ext.ComponentQuery.query("panel[reference=jugClMgPanel]");
//                                    interviewMgrPanel[0].getStore().reload();
                                }, "", true, this);
                            }
                        }*/
                    }
                ]
            }],
        });
        this.callParent();
    }
});
