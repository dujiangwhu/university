Ext.define('KitchenSink.view.trainStudentMg.trainStudentInfo', {
    extend: 'Ext.panel.Panel',
    xtype: 'trainStudentInfo',
    controller: 'trainStudentInfoMg',
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
    actType: 'add',
    title: '学生信息',
    bodyStyle:'overflow-y:auto;overflow-x:hidden',
    items: [{
        xtype: 'form',
        reference: 'studentInfoForm',
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
                    fieldLabel: 'ORGID',
                    name: 'orgid'
                    //fieldStyle:'background:#F4F4F4'
                },{
                    xtype: 'hiddenfield',
                    fieldLabel: 'OPRID',
                    name: 'oprid'
                    //fieldStyle:'background:#F4F4F4'
                },{
                    xtype: 'textfield',
                    fieldLabel: '姓名',
                    name: 'name',
					allowBlank: false
                },{
                    xtype: 'textfield',
                    fieldLabel: '手机号码',
                    name: 'phone',
					allowBlank: false
                },{
            		xtype: 'combobox',
                    fieldLabel: '性别',
                    allowBlank: false,
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
                    name: 'age'
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
					xtype: 'textfield',
					fieldLabel: '拥有的课时卡',
					name: 'timecardRemaind',
					readOnly:true,
					fieldStyle:'background:#F4F4F4'
				},{
					xtype: 'textfield',
					fieldLabel: '已使用的课时卡',
					name: 'timecardUsed',
					readOnly:true,
					fieldStyle:'background:#F4F4F4'
				},{
            		xtype: 'combobox',
                    fieldLabel: '状态信息',
                    allowBlank: false,
                    editable:false,
                    emptyText:'请选择',
                    queryMode: 'remote',
            	    name: 'statu',
            	    valueField: 'TValue',
                	displayField: 'TSDesc',
                	store: new KitchenSink.view.common.store.appTransStore("PX_TEACHER_STATU"),
					value:'A'
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