Ext.define('KitchenSink.view.enrollProject.userMg.userBmSchView', {
	extend : 'Ext.panel.Panel',
	xtype : 'userBmSchView',
	reference : 'userBmSchView',
	controller : 'userMgController',
	requires : [ 
		'Ext.data.*', 
		'Ext.grid.*', 
		'Ext.util.*',
		'Ext.toolbar.Paging', 
		'Ext.ux.ProgressBarPager',
	],
	listeners : {
		resize : function(win) {
			win.doLayout();
		}
	},
	actType : '',
	title : '考生报名流程查看',
	bodyStyle : 'overflow-y:auto;overflow-x:hidden',
	
	items : [ {
		xtype : 'form',
		reference : 'userMgForm',
		name:'userMgForm',
		layout : {
			type : 'vbox',
			align : 'stretch'
		},
		border : false,
		bodyPadding : 10,
		bodyStyle : 'overflow-y:auto;overflow-x:hidden',

		fieldDefaults : {
			msgTarget : 'side',
			labelWidth : 110,
			labelStyle : 'font-weight:bold'
		},
		items : [ {
			layout : {
				type : 'column'
			},
			items : [ {
				columnWidth : .15,
				xtype : "image",
				src : "",
				height : 193,
				width : 140,
				name : "titileImage"

			}, 
			{
				columnWidth : .85,
				bodyStyle : 'padding-left:30px',
				layout : {
					type : 'vbox',
					align : 'stretch',
					readOnly : true,
				},
				items : [ {
					xtype : 'textfield',					
					fieldLabel : '用户编号',
					name : 'OPRID',
					readOnly:true,
					fieldStyle : 'background:#F4F4F4'
				},{
					xtype : 'textfield',					
					fieldLabel : '报名表编号',
					name : 'APPID',
					readOnly:true,
					hidden:true,
					fieldStyle : 'background:#F4F4F4'
				}, {
					xtype : 'textfield',
					fieldLabel : '面试申请号',
					readOnly : true,
					name : 'msSqh',
					fieldStyle : 'background:#F4F4F4'
				}, {
					xtype : 'textfield',
					fieldLabel : '姓名',
					readOnly : true,
					name : 'userName',
					fieldStyle : 'background:#F4F4F4'
				}, {
					xtype : 'textfield',
					fieldLabel : '性别',
					readOnly : true,
					name : 'userSex',
					fieldStyle : 'background:#F4F4F4'
				}, {
					xtype : 'textfield',
					fieldLabel : '邮箱',
					readOnly : true,
					name : 'userEmail',
					fieldStyle : 'background:#F4F4F4'
				}, {
					xtype : 'textfield',
					fieldLabel : '手机',
					readOnly : true,
					name : 'userPhone',
					fieldStyle : 'background:#F4F4F4'
				}, {
					xtype : 'textfield',
					fieldLabel : '创建日期时间',
					readOnly : true,
					name : 'zcTime',
					fieldStyle : 'background:#F4F4F4'
				}, {
					xtype : 'hiddenfield',
					name : 'titleImageUrl'
				} ]
			}, {
				xtype : 'tabpanel',
				frame : true,
				columnWidth : 1,
				bodyStyle : 'padding-top:10px',
				layout : {
					type : 'vbox',
					align : 'stretch'
				},				
				items : [
				{
					title : '考生导入信息',
					xtype : 'form',
					name : 'ksdrInfoForm',
					layout : {
						type : 'vbox',
						align : 'stretch'
					},
					// frame: true,
					border : false,
					bodyPadding : 10,
					// margin:10,
					bodyStyle : 'overflow-y:auto;overflow-x:hidden',
					fieldDefaults : {
						msgTarget : 'side',
						labelWidth : 200,
						readOnly : true,
						fieldStyle : 'background:#F4F4F4', 
						labelStyle : 'font-weight:bold'
					},
					items : [ 
					  ]
				} ]
			} ]
		} ]
	} ],
	buttons : [ {
		text : '保存',
		iconCls : "save",
		handler : 'onFormSave2'
	}, {
		text : '确定',
		iconCls : "ensure",
		handler : 'onFormEnsure2'
	}, {
		text : '关闭',
		iconCls : "close",
		handler : 'onFormClose'
	} ]
});