Ext.define('KitchenSink.view.trainTeacherMg.teacherMgInfoPanel', {
    extend: 'Ext.panel.Panel',
    xtype: 'teacherMgInfoPanel',
    controller: 'teacherController',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager'
    ],
    listeners:{
        resize: function(win){
            win.doLayout();
        }
    },
    actType: '',
    title: '教师信息',
    bodyStyle:'overflow-y:auto;overflow-x:hidden',
    items: [{
        xtype: 'form',
        reference: 'teacherMgForm',
        layout: {
            type: 'vbox',
            align: 'stretch'
        },
        border: false,
        bodyPadding: 10,
        bodyStyle:'overflow-y:auto;overflow-x:hidden',

        fieldDefaults: {
            msgTarget: 'side',
            labelWidth: 110,
            labelStyle: 'font-weight:bold'
        },
        items: [{
            layout: {
                type: 'column'
            },
            items:[{
                columnWidth:.2,
                xtype: "image",
                src: "",
                height:300,
                width:217,
                name: "titileImage"
            },{
                columnWidth:.8,
                bodyStyle:'padding-left:30px',
                layout: {
                    type: 'vbox',
                    align: 'stretch'
                },
                items: [{
                    xtype: 'hiddenfield',
                    fieldLabel: 'OPRID',
                    name: 'oprid'
                    //fieldStyle:'background:#F4F4F4'
                },{
                    xtype: 'textfield',
                    fieldLabel: '教师姓名',
                    readOnly:true,
                    afterLabelTextTpl: [
                                        '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                                    ],
                    name: 'name',
                    fieldStyle:'background:#F4F4F4'
                },{
                    xtype: 'textfield',
                    fieldLabel: '手机号码',
                    afterLabelTextTpl: [
                                        '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                                    ],
                    readOnly:true,
                    name: 'phone',
                    fieldStyle:'background:#F4F4F4'
                },{
            		xtype: 'combobox',
                    fieldLabel: '性别',
                    allowBlank: false,
                    afterLabelTextTpl: [
                                        '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                                    ],
                    editable:false,
                    emptyText:'请选择',
                    queryMode: 'remote',
            	    	name: 'sex',
            	    	valueField: 'TValue',
                		displayField: 'TSDesc',
                		store: new KitchenSink.view.common.store.appTransStore("TZ_SEX")
                },
                {
                    xtype: 'textfield',
                    fieldLabel: '年龄',
                    allowBlank: false,
                    afterLabelTextTpl: [
                                        '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                                    ],
                    name: 'age'
                },{
                    xtype: 'textfield',
                    fieldLabel: '身份证号',
                    allowBlank: false,
                    afterLabelTextTpl: [
                                        '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                                    ],
                    name: 'idCard'
                },{
            		xtype: 'combobox',
                    fieldLabel: '级别',
                    allowBlank: false,
                    afterLabelTextTpl: [
                                        '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                                    ],
                    editable:false,
                    emptyText:'请选择',
                    queryMode: 'remote',
            	    	name: 'level',
            	    	valueField: 'TValue',
                		displayField: 'TSDesc',
                		store: new KitchenSink.view.common.store.appTransStore("PX_TEACHER_LEVEL")
                },{
                    xtype: 'textfield',
                    fieldLabel: '毕业院校',
                    name: 'school'
                },{
                    xtype: 'textfield',
                    fieldLabel: '学历',
                    name: 'educationBg'
                },{
                    xtype: 'textfield',
                    fieldLabel: '教龄',
                    allowBlank: false,
                    afterLabelTextTpl: [
                                        '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                                    ],
                    name: 'schoolAge'
                },{
                    xtype: 'textfield',
                    fieldLabel: '教师证书号码',
                    name: 'teacherCard'
                },{
            		xtype: 'combobox',
                    fieldLabel: '账户类型',
                    allowBlank: false,
                    afterLabelTextTpl: [
                                        '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                                    ],
                    editable:false,
                    emptyText:'请选择',
                    queryMode: 'remote',
            	    	name: 'accountType',
            	    	valueField: 'TValue',
                		displayField: 'TSDesc',
                		store: new KitchenSink.view.common.store.appTransStore("PX_ACCOUNT_TYPE")
                },{
                    xtype: 'textfield',
                    fieldLabel: '账户号码',
                    allowBlank: false,
                    afterLabelTextTpl: [
                                        '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                                    ],
                    name: 'accountNum'
                },{
                    xtype: 'textfield',
                    fieldLabel: '积分',
                    readOnly:true,
                    allowBlank: false,
                    afterLabelTextTpl: [
                                        '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                                    ],
                    name: 'score',
                    fieldStyle:'background:#F4F4F4'
                },{
                    xtype: 'textfield',
                    fieldLabel: 'QQ',
                    name: 'qq'
                },{
                    xtype: 'textfield',
                    fieldLabel: '邮箱',
                    name: 'email'
                },{
                    xtype: 'textfield',
                    fieldLabel: '紧急联系人',
                    name: 'contactor'
                },{
                    xtype: 'textfield',
                    fieldLabel: '紧急联系人电话',
                    name: 'contactorPhone'
                },{
                    xtype: 'textfield',
                    fieldLabel: '紧急联系人地址',
                    name: 'contactorAddress'
                },{
            		xtype: 'combobox',
                    fieldLabel: '状态信息',
                    allowBlank: false,
                    afterLabelTextTpl: [
                                        '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                                    ],
                    editable:false,
                    emptyText:'请选择',
                    queryMode: 'remote',
            	    	name: 'statu',
            	    	valueField: 'TValue',
                		displayField: 'TSDesc',
                		store: new KitchenSink.view.common.store.appTransStore("PX_TEACHER_STATU")
                },{
                    xtype: 'textfield',
                    //height:100,
                    //width:300,
                    grow:false,
                    fieldLabel: '自我介绍',
                    name: 'introduce'
                },{
                        xtype: 'hiddenfield',
                        name: 'titleImageUrl'
                    }]
            }]
        }]
    }],
    buttons: [
        {
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