Ext.define('KitchenSink.view.enrollmentManagement.autoTags.autoTagsPanel', {
    extend: 'Ext.panel.Panel',
    xtype: 'autoTagsPanel',
    controller: 'autoTagsController',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager'
    ],
    title: Ext.tzGetResourse("TZ_AUTO_TAGS_COM.TZ_TAG_DFN_STD.autoTagsDefn","自动标签定义"),
    bodyStyle:'overflow-y:auto;overflow-x:hidden',
    
    constructor: function (config) {
		var actType = config.actType;
		this.actType = actType;
		this.callback = config.callback;
		
		this.callParent();
	},
    initComponent: function () {
    	Ext.apply(this, {
		    items: [{
		        xtype: 'form',
		        reference: 'autoTagForm',
		        layout: {
		            type: 'vbox',
		            align: 'stretch'
		        },
		        border: false,
		        bodyPadding: 10,
		        bodyStyle:'overflow-y:auto;overflow-x:hidden',
		        fieldDefaults: {
		            msgTarget: 'side',
		            labelWidth: 130,
		            labelStyle: 'font-weight:bold'
		        },
		        items: [{
		            xtype: 'textfield',
		            fieldLabel: Ext.tzGetResourse("TZ_AUTO_TAGS_COM.TZ_TAG_DFN_STD.tagId","标签编号"),
		            name: 'tagId',
		            value:'NEXT',
		            readOnly:true,
		            editable:false,
		            cls:"lanage_1",
		            allowBlank:false,
		            afterLabelTextTpl:[
		                '<span style="color:red;font-weight:bold" data-qtip="required">*</span>'
		            ],
		        }, {
		            xtype: 'textfield',
		            fieldLabel: Ext.tzGetResourse("TZ_AUTO_TAGS_COM.TZ_TAG_DFN_STD.tagName","标签名称"),
		            name: 'tagsName',
		            maxLength: 30,
		            afterLabelTextTpl: [
		                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
		            ],
		            allowBlank: false
		        },{
		        	xtype: 'textareafield',
		        	fieldLabel: Ext.tzGetResourse("TZ_AUTO_TAGS_COM.TZ_TAG_DFN_STD.tagDescr","描述"),
		            name: 'tagDescr',
		            maxLength: 254
		        },{
		            xtype: 'combo',
		            fieldLabel: Ext.tzGetResourse("TZ_AUTO_TAGS_COM.TZ_TAG_DFN_STD.isValid","有效状态"),
		            name: 'isValid',
		            allowBlank:false,
		            mode:'local',
		            editable:false,
		            valueField:'TValue',
		            displayField:'TSDesc',
		            store:new KitchenSink.view.common.store.appTransStore("TZ_ISVALID"),
		            value: 'Y'
		        },{
		            xtype:'textfield',
		            fieldLabel:Ext.tzGetResourse("TZ_AUTO_TAGS_COM.TZ_TAG_DFN_STD.javaCls","java类"),
		            name:'javaCls'
		        },{
		        	xtype: 'component',
					style:{
						marginLeft: '133px'	
					},
					html: '<span style="color: #F00;">说明：java类必需实现com.tranzvision.gd.TZAutoTagsBundle.service.TzAutoTagsInterface接口并重写execute()方法。</span>'	
		        },{
		        	xtype: 'checkbox',
					fieldLabel: Ext.tzGetResourse('TZ_AUTO_TAGS_COM.TZ_TAG_DFN_STD.isViewClPwd','显示在材料评委端'),
					name:'isViewClPwd',
					inputValue:'Y',
					uncheckedValue:'N'
		        }]
		    }],
    	});
        this.callParent();
    },
    buttons: [{
        text: '保存',
        iconCls:"save",
        name: 'saveBtn',
        handler: 'onAutoTagSave'
    },{
        text: '确定',
        iconCls:"ensure",
        name: 'ensureBtn',
        handler: 'onAutoTagEnsure'
    },{
        text: '关闭',
        iconCls:"close",
        handler: function(btn){
        	btn.findParentByType('autoTagsPanel').close();
        }
    }]
});
