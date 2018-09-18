Ext.define('KitchenSink.view.trainScheduleMg.scheduleInfoPanel', {
    extend: 'Ext.panel.Panel',
    xtype: 'scheduleInfoPanel',
    controller: 'scheduleController',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager'
    ],
    title: '课程级别信息',
    bodyStyle:'overflow-y:auto;overflow-x:hidden',
    actType: 'update',//默认新增
    items: [{
        xtype: 'form',
        //reference: 'orgInfoForm',
        layout: {
            type: 'vbox',
            align: 'stretch'
        },
        border: false,
        bodyPadding: 10,
        bodyStyle:'overflow-y:auto;overflow-x:hidden',

        fieldDefaults: {
            msgTarget: 'side',
            labelWidth: 140,
            labelStyle: 'font-weight:bold'
        },

        items: [{
            xtype: 'textfield',
            fieldLabel: "排课编号",
            name: 'tzScheduleId',
            afterLabelTextTpl: [
                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
            ],
            allowBlank: false,
			maxLength: 15,
			enforceMaxLength: true
        },{
            xtype: 'textfield',
            fieldLabel: "撤销原因",
            allowBlank: false,
			afterLabelTextTpl: [
			                    '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
			                ],
            name: 'cancelReason'
        },{
			layout : {
				type : 'column'
			},
			items : [
					{
						columnWidth : .55,
						xtype : 'textfield',
						fieldLabel :"新授课老师",
						name : 'oprid',
						allowBlank: false,
						afterLabelTextTpl: [
						                    '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
						                ],
						
						editable : false,
						triggers : {
							clear : {
								cls : 'x-form-clear-trigger',
								handler : 'clearPmtSearchCom'
							},
							search : {
								cls : 'x-form-search-trigger',
								handler : "pmtSearchCom"
							}
						}
					},
					{
						columnWidth : .45,
						xtype : 'displayfield',
						hideLabel : true,
						style : 'margin-left:5px',
						name : 'name'
					} ]
		}]
    }],
    buttons: [{
        text: '保存',
        iconCls:"save",
        handler: 'onFormSave'
    }, {
        text: '确定',
        iconCls:"ensure",
        handler: 'onFormEnsure'
    }, {
        text: '关闭',
        iconCls:"close",
        handler: 'onFormClose'
    }]
});