Ext.define('KitchenSink.view.trainStudentMg.trainStuTimeCardAddWindow', {
    extend: 'Ext.window.Window',
    reference: 'trainStuTimeCardAddWindow',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging'
    ],
    title: '给学员分配课时卡',
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
        reference: 'stuAddTimeCardForm',
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
                xtype: 'hiddenfield',
                name: 'oprid',
                allowBlank:false
            },{
                xtype: 'textfield',
				fieldLabel: '学员姓名',
                name: 'stuName',
                readOnly:true,
				fieldStyle:'background:#F4F4F4'
            },{
				xtype: 'textfield',
				fieldLabel: '现剩余的课时卡',
				name: 'stuTimeCardRemaind',
				readOnly:true,
				fieldStyle:'background:#F4F4F4'
			}, {
				xtype: 'textfield',
				fieldLabel: '已使用的课时卡',
				name: 'stuTimeCardUsed',
				readOnly:true,
                fieldStyle:'background:#F4F4F4'
			}, {
				xtype: 'numberfield',
				fieldLabel: '充值课时卡数',
				name: 'addTimeCardNumber',
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
