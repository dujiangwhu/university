﻿Ext.define('KitchenSink.view.template.sitetemplate.area.typeManges.siteAreaTypeInfoPanel', {
    extend: 'Ext.window.Window',
    xtype: 'siteAreaTypeInfoPanel', 
	controller: 'typeInfo',
	requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.template.sitetemplate.area.sitestateStroe',
        'KitchenSink.view.template.sitetemplate.area.typeManges.areaTypeInfoController'
	],
    title: '区域类型设置',
    reference:'siteAreaTypeInfoPanel',
    width:600,
	bodyStyle:'overflow-y:auto;overflow-x:hidden',
	actType: 'add',//默认新增
    items: [{
        xtype: 'form',
        reference: 'areaTypeForm',
		layout: {
            type: 'vbox',
            align: 'stretch'
        },
        border: false,
        bodyPadding: 10,
		//heigth: 600,
		bodyStyle:'overflow-y:auto;overflow-x:hidden',
		
        fieldDefaults: {
            msgTarget: 'side',
            labelWidth: 100,
            labelStyle: 'font-weight:bold'
        },
		
        items: [{
            xtype: 'hiddenfield',
            readOnly:true,
            fieldLabel: '站点模板编号',
			name: 'siteId',
        	},{
            xtype: 'textfield',
            name: 'areatypeid',
            readOnly:true,
            fieldLabel: '区域类型编号'
        },{
            xtype: 'combobox',
            fieldLabel: '状态',
            emptyText:'请选择',
            queryMode: 'remote',
            editable:false,
			name: 'areatypestate',
			valueField: 'TValue',
    		displayField: 'TSDesc',
    		store: new KitchenSink.view.common.store.appTransStore("TZ_AREA_TYPE_STATE")
    		/*
			listeners: {
			  	afterrender: function(tvType){
					Ext.tzLoad('{"OperateType":"TV","fieldName":"TZ_AREA_TYPE_STATE"}',function(response){
						tvType.setStore(new Ext.data.Store({		
							fields: ['TValue', 'TSDesc', 'TLDesc'],
							data:response.TZ_AREA_TYPE_STATE
						}));
					});
				}
			}*/
        },{
            xtype: 'textfield',
            fieldLabel: '区域类型名称',
			name: 'areatypename'
        },{
            xtype: 'combobox',
            fieldLabel: '区域类型标识',
            emptyText:'请选择',
            queryMode: 'remote',
            editable:false,
			name: 'areatypearea',
			valueField: 'TValue',
    		displayField: 'TSDesc',
    		store: new KitchenSink.view.common.store.appTransStore("TZ_AREA_TYPE")
        },{
            xtype: 'textfield',
            fieldLabel: '业务人员设置区域时的处理程序类',
            labelWidth: 250,
			name: 'areasetcode'
        },{
            xtype: 'textfield',
            fieldLabel: '区域代码展示的处理程序类',
            labelWidth: 250,
			name: 'areahtmlcode'
        },{
            xtype: 'textfield',
            fieldLabel: '移动版区域内容展示类',
            labelWidth: 250,
			name: 'mobilehtmlcode'
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
