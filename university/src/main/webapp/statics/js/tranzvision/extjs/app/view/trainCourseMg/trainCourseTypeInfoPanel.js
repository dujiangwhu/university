Ext.define('KitchenSink.view.trainCourseMg.trainCourseTypeInfoPanel', {
    extend: 'Ext.panel.Panel',
    xtype: 'trainCourseTypeInfoPanel',
    controller: 'trainCourseTypeController',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.trainCourseMg.trainCourseTypeStore'
    ],
    title: '课程级别信息',
    bodyStyle:'overflow-y:auto;overflow-x:hidden',
    actType: 'add',//默认新增
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
            fieldLabel: "课程级别编号",
            name: 'tzCourseTypeId',
            afterLabelTextTpl: [
                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
            ],
            allowBlank: false,
			maxLength: 15,
			enforceMaxLength: true
        },{
            xtype: 'textfield',
            fieldLabel: "课程级别名称",
            afterLabelTextTpl: [
                                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                            ],
                            allowBlank: false,
            name: 'tzCourseTypeName'
        },{
            xtype: 'combobox',
            editable:false,
            fieldLabel: "课程级别类型",
            afterLabelTextTpl: [
                                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                            ],
                            allowBlank: false,
            forceSelection: true,
            valueField: 'TValue',
            displayField: 'TSDesc',
            store: new KitchenSink.view.common.store.appTransStore("PX_COURSE_TYPE"),
            queryMode: 'remote',
            name: 'tzCourseType'//,
            //value:'Y'
        },{
            xtype: 'numberfield',
            fieldLabel: "课程最小年龄",
            afterLabelTextTpl: [
                                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                            ],
                            allowBlank: false,
            maxValue:100,
            minValue:0,
            name: 'tzMinAge'
        },{
            xtype: 'numberfield',
            fieldLabel: "课程最大年龄",
            afterLabelTextTpl: [
                                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                            ],
                            allowBlank: false,
                            maxValue:100,
                            minValue:0,
            name: 'tzMaxAge'
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