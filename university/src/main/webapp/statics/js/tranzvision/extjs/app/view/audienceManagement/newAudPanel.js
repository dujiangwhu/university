Ext.define('KitchenSink.view.audienceManagement.newAudPanel', {
    extend: 'Ext.panel.Panel',
    xtype: 'newAudPanel', 
	controller: 'audienceManagementController',
	requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.audienceManagement.newAudWindowStore',
	],
    title: '听众信息',
	bodyStyle:'overflow-y:auto;overflow-x:hidden', 
	actType: 'add',//默认新增
    items: [{
        xtype: 'form',
        reference: 'newAudPanelForm',
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
			xtype: 'textfield',
			fieldLabel: "机构",
			hidden:true,
			name: 'audJG',
			value:Ext.tzOrgID
		},{
			xtype: 'textfield',
			fieldLabel: Ext.tzGetResourse("TZ_AUD_COM.TZ_AUD_LIST_STD.audID","听众编号"),
			name: 'audID',
		//	maxLength: 18,
		/*	afterLabelTextTpl: [
				'<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
			],
			*/
			allowBlank: false,
            readOnly:true,
			fieldStyle:'background:#F4F4F4',
            value: 'NEXT'
		}, {
			xtype: 'textfield',
			fieldLabel: Ext.tzGetResourse("TZ_AUD_COM.TZ_AUD_LIST_STD.audName","听众名称"),
			name: 'audName'
		}, {
			xtype: 'combobox',
			fieldLabel: Ext.tzGetResourse("TZ_AUD_COM.TZ_AUD_LIST_STD.audStat","听众状态"),
			name: 'audStat',
			valueField: 'TValue',
            displayField: 'TSDesc',
            store: new KitchenSink.view.common.store.appTransStore("TZ_AUD_STAT"),
            queryMode: 'local',
            allowBlank: false
		}, {
			xtype: 'combobox',
			fieldLabel: Ext.tzGetResourse("TZ_AUD_COM.TZ_AUD_LIST_STD.audLY","听众来源"),
			name: 'audLY',
			valueField: 'TValue',
            displayField: 'TSDesc',
            store: new KitchenSink.view.common.store.appTransStore("TZ_AUD_LY"),
            queryMode: 'local',
            allowBlank: false
           
		},{
				xtype: 'combobox',
	        	fieldLabel  : Ext.tzGetResourse("TZ_AUD_COM.TZ_AUD_LIST_STD.audType","听众类型"),
	        	name      : 'audType',
	        	valueField: 'TValue',
	            displayField: 'TSDesc',
	            store: new KitchenSink.view.common.store.appTransStore("TZ_AUD_TYPE"),
	            queryMode: 'local',
	            allowBlank: false,
	            listeners: {
					"change": function( cbox , newValue, oldValue,lastValue){
						//必填
						var audSQLs=cbox.findParentByType("form").getForm().findField("audSQL");

						var form = cbox.findParentByType("form").getForm();
						//表单数据
						var formParams = form.getValues();
						var audTypeNow =formParams["audType"];

						if(audTypeNow=="1"){
							audSQLs.allowBlank = false;
						}else{
							audSQLs.allowBlank = true;
						}
					}
				}
		
		}, {
			xtype: 'textarea',
			fieldLabel: Ext.tzGetResourse("TZ_AUD_COM.TZ_AUD_LIST_STD.tips","备注"),
			name: 'audMS'
		},{
			xtype: 'textarea',
			fieldLabel: Ext.tzGetResourse("TZ_AUD_COM.TZ_AUD_LIST_STD.sql","SQL"),
			name: 'audSQL',
			afterBodyEl: Ext.create('Ext.XTemplate',
					'<div style="margin-top:5px;color:red;">',
						'*注意：SQL语句返回听众成员OPRID，例如：select OPRID from PS_TZ_XXXX_T。',
					'</div>'
				)
		}]
    },{
		
        xtype:'grid',
        frame: true,
        height: 350,
		reference: 'pageRegGrid',
		style: "margin:10px",
		dockedItems:[{
    		xtype:"toolbar",
    		items:[
    			{text:"查询",tooltip:"查询数据",iconCls: "query",handler:"searchAudCyList"},"-",
    			{text:"重新产生听众成员",tooltip:"重新产生听众成员",iconCls:"add",handler:"freshAudMember"},"-",
    			{text:"删除",tooltip:"删除选中的数据",iconCls:"remove",handler:"delSelAudList"}
    		]
    	}],
		columnLines: true,
		selModel: {
            type: 'checkboxmodel'
        },
        style:"margin:10px",
		reference: 'audItemGrid',
        scrollable: true,  
        store: {
			type: 'newAudWindowStore'
		},
		
		plugins: {
			ptype: 'cellediting'
		},
        viewConfig: {
			plugins: {
				ptype: 'gridviewdragdrop',
				dragText: '拖拽进行信息项的排序'
				},
				listeners: {
				
					drop: function(node, data, dropRec, dropPosition) {
						data.view.store.beginUpdate();
						var items = data.view.store.data.items;
						for(var i = 0;i< items.length;i++){
							items[i].set('applyItemNum',i+1);
						}
						data.view.store.endUpdate();
					}
				}
		},
        name:'tzcy',
        title: Ext.tzGetResourse("TZ_AUD_COM.TZ_AUD_LIST_STD.tzcy","听众成员"),
        columns: [
                  {
            text: '对象ID',
            dataIndex: 'dxID',
            hidden:true,
            flex:1
        },{
            text: '听众ID',
            dataIndex: 'audID',
            hidden:true,
            flex:1
        },{
            text: '姓名',
            dataIndex: 'audName',
            flex:1
        },{
            text: '手机',
            dataIndex: 'audMobile',
            flex:1
        },{
            text: "邮箱",
            dataIndex: 'audMail',
            flex:1
        },{
			text: '状态',
			dataIndex: 'audDxzt',
			sortable: false,
			flex: 1,
			editor: {
				xtype: 'combobox',
					store:{
						fields: [{name:'value'},{name:'desc'}],
						data: [{value: 'A', desc: '正常'},{value: 'N', desc: '失效'},{value: 'L', desc: '锁定'}]
							},
				displayField: 'desc',
				valueField: 'value'
			},
			renderer: function(v){
				if(v=='A'){
					return "正常";
				}else if(v=='N'){
					return "失效";
				}else{
					return "锁定";
				}
			}
		},{
            menuDisabled: true,
            sortable: false,
			   width:60,
            align:'center',
            xtype: 'actioncolumn',
			   items:[
			   	  {iconCls: 'remove',tooltip: '删除',handler: 'delSelAudCy'}
			   ]
         }],
       
      
						
		bbar: {
			xtype: 'pagingtoolbar',
			pageSize: 10,
			listeners:{
				afterrender: function(pbar){
					var grid = pbar.findParentByType("grid");
					pbar.setStore(grid.store);
				}
			},
			plugins: new Ext.ux.ProgressBarPager()
		}
    }
    ],
    buttons: [{
		text: '保存',
		iconCls:"save",
		handler: 'onAudDefnSave',
		name: 'audSave',
		id:'audSave',
		reference:'audSave'
		
	}, {
		text: '确定',
		iconCls:"ensure",
		handler: 'onAudDefnEnsure'
	}, {
		text: '关闭',
		iconCls:"close",
		handler: 'onAudDefnClose'
	}]
});
