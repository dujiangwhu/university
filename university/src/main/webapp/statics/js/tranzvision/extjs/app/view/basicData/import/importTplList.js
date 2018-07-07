﻿Ext.define('KitchenSink.view.basicData.import.importTplList', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.basicData.import.importTplController',
        'KitchenSink.view.basicData.import.importTplStore'
    ],
    alilas: 'widget.importTplList',
    controller: 'importTplController',
    columnLines: true,
    selModel: {
        type: 'checkboxmodel'
    },
    style:"margin:8px",
    multiSelect: true,
    title: '导入模板管理',
    viewConfig: {
        enableTextSelection: true,
		plugins: {
			ptype: 'gridviewdragdrop',
			containerScroll: true,
			dragGroup: this,
			dropGroup: this
		},
		listeners: {
			drop: function(node, data, dropRec, dropPosition) {
				data.view.store.beginUpdate();
				var items = data.view.store.data.items;
				for(var i = 0;i< items.length;i++){
					items[i].set('orderNum',i+1);
				}
				data.view.store.endUpdate();
			}
		}
    },
    header:false,
    frame: true,
    dockedItems:[{
        xtype:"toolbar",
        dock:"bottom",
        ui:"footer",
        items:['->',
            {minWidth:80,text:"保存",iconCls:"save",handler:"listSave",name:'save'},
            {minWidth:80,text:"确定",iconCls:"ensure",handler:"listSave",name:'ensure'},
            {minWidth:80,text:"关闭",iconCls:"close",handler:
                function(btn){
                    var grid = btn.findParentByType("grid");
                    grid.close();
                }
            }
        ]
    },{
        xtype:"toolbar",
        items:[
            {text:"查询",tooltip:"查询数据",iconCls: "query",handler:'listSearch'},"-",
            {text:"新增",tooltip:"新增数据",iconCls:"add",handler:'addTpl'},"-",
            {text:"编辑",tooltip:"编辑数据",iconCls:"edit",handler:'editTpl'},"-",
            {text:"删除",tooltip:"删除选中的数据",iconCls:"remove",handler:'deleteTpls'}
        ]
    }],
    initComponent: function(){
        var store = new KitchenSink.view.basicData.import.importTplStore();
        Ext.apply(this, {
            columns: [{
                text: '序号',
                dataIndex: 'orderNum',
                width: 80
            },{
                text: '模板编号',
                dataIndex: 'tplId',
                width: 150
            },{
                text: '模板名称',
                dataIndex: 'tplName',
                minWidth: 200,
                flex: 1
            },{
                text: '目标表',
                dataIndex: 'targetTbl',
                width: 200
            },{
                text: 'Java类',
                dataIndex: 'javaClass',
                minWidth: 200,
                flex: 1
            },{
                menuDisabled: true,
                sortable: false,
                width:60,
                xtype: 'actioncolumn',
                items:[
                    {iconCls: 'edit',tooltip: '编辑',handler:'editTpl'},
                    {iconCls: 'remove',tooltip: '删除',handler:'deleteTpl'}
                ]
            }],
            store:store,
            bbar: {
                xtype: 'pagingtoolbar',
                pageSize: 10,
                store:store,
                plugins: new Ext.ux.ProgressBarPager()
            }
        });
        this.callParent();
    }
});
