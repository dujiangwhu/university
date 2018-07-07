Ext.define('KitchenSink.view.trainOrgTimeCardMg.trainOrgTimeCardAddWindow', {
    extend: 'Ext.window.Window',
    reference: 'trainOrgTimeCardAddWindow',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging'
    ],
    title: '培训机构课时订购',
    bodyStyle:'overflow-y:auto;overflow-x:hidden;padding-top:10px',
    actType: 'update',
    closeAction:'destroy',
    width: 500,
    minHeight: 240,
    resizable: true,
    modal:true,
    listeners:{
        resize: function(win){
            win.doLayout();
        }
    },
    viewConfig: {
        enableTextSelection: true
    },
    items: [{
        xtype: 'form',
        reference: 'orgRoleForm',
        layout: {
            type: 'vbox',
            align: 'stretch'
        },
        border: false,
        bodyPadding: 10,
        bodyStyle:'overflow-y:auto;overflow-x:hidden',
        fieldDefaults: {
            msgTarget: 'side',
            labelWidth: 100,
            labelStyle: 'font-weight:bold'
        },

        items: [
            {
                xtype: 'hiddenfield',
                name: 'orgId',
                allowBlank:false
            },{
                xtype: 'textfield',
				fieldLabel: '机构名称',
                name: 'orgName',
                readOnly:true,
				fieldStyle:'background:#F4F4F4'
            },{
				xtype: 'textfield',
				fieldLabel: '拥有的课时卡',
				name: 'orgTimeCardHave',
				readOnly:true,
				fieldStyle:'background:#F4F4F4'
			}, {
				xtype: 'textfield',
				fieldLabel: '已分配的课时卡',
				name: 'orgTimeCardAssign',
				readOnly:true,
                fieldStyle:'background:#F4F4F4'
			}, {
				xtype: 'numberfield',
				fieldLabel: '充值课时卡数',
				name: 'addTimeCardNumber',
				decimalPrecision:0,
				allowBlank:false
			}, {
				xtype: 'numberfield',
				fieldLabel: '充值金额',
				name: 'addTimeCardMoney',
				decimalPrecision:0,
				allowBlank:false
			}]
    }],
    buttons: [{
        text: '确定',
        iconCls:"ensure",
        name:"ensure",
        handler: 'onAddTimeCardFormSave'
    },{
        text: '关闭',
        iconCls:"close",
        handler: function(btn){
            btn.findParentByType("window").close();
        }
    }]
});
