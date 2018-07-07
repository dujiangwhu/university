Ext.define('KitchenSink.view.enrollmentManagement.applicationMsMan.searchWindow', {
	extend: 'Ext.window.Window',
    reference: 'searchWindow',
    xtype: 'searchWindow',
    controller:'msRelativeController',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'KitchenSink.view.enrollmentManagement.applicationMsMan.msRelativeController'
    ],
    width: 800,
    height: 250,
    layout: 'fit',
    resizable: true,
    modal: true,
    title: '查询', 
    closeAction: 'destroy',
    ignoreChangesFlag: true,
	items: [{
		xtype: 'form',	
		layout: {
			type: 'vbox',
			align: 'stretch'
		},
		border: false,
		bodyPadding: 10,
	
		fieldDefaults: {
			msgTarget: 'side',
			labelWidth: 80,
			labelStyle: 'font-weight:bold'
		},
		items: [{
        xtype:"form",
        border:false,
        layout: {
            type: 'column'
        },
        items:[{
            xtype:'textfield',
            name: 'className',
            style:'margin-bottom:4.5px',
            bodyStyle:'overflow-y:auto;overflow-x:hidden',
            fieldLabel: Ext.tzGetResourse("TZ_GD_FILTER_COM.TZ_GD_FILTERTZ_STD.classID","班级"),
            columnWidth:1,
            editable: false,
            triggers: {
                search: {
                    cls: 'x-form-search-trigger',
                    handler: "pmtSearchClassIDTmp"
                }
            }
        },{
            xtype: 'textfield',
            hidden:true,
            hideLabel: true,
            name:"classID"
        }]
    },{
        xtype:"form",
        border:false,
        layout: {
            type: 'column'
        },
        items:[{
            xtype:'textfield',
            name: 'batchName',
            style:'margin-bottom:4.5px',
            bodyStyle:'overflow-y:auto;overflow-x:hidden',
            fieldLabel: Ext.tzGetResourse("TZ_GD_FILTER_COM.TZ_GD_FILTERTZ_STD.batchID","批次"),
            columnWidth:1,
            editable: false,
            triggers: {
                search: {
                    cls: 'x-form-search-trigger',
                    handler: "pmtSearchBatchIDTmp"
                }
            }
        },{
            xtype: 'textfield',
            hidden:true,
            hideLabel: true,
            name:"batchID"
        }]
    },{
			xtype: 'textfield',
			fieldLabel: '面试申请号',
			name: 'mshId',
			maxLength: 18
		},{
			xtype: 'textfield',
			fieldLabel: '姓名',
			name: 'name',
			maxLength: 18
		},{
			xtype: 'textfield',
			fieldLabel: '姓名',
			name: 'type',
			hidden: true
		}]
	}],
    buttons: [{
		text: '搜索',
		iconCls:"search",
		handler: 'searchInfo'
	},{
		text: '关闭',
		iconCls:"close",
		handler: 'closeWin'
	}]
});
