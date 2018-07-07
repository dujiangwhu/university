﻿Ext.define('KitchenSink.view.interviewManagement.interviewArrange.interviewArrangeInfo', {
    extend: 'Ext.panel.Panel',
    xtype: 'interviewArrangeInfo', 
	controller: 'interviewArrangeController',
	requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.interviewManagement.interviewArrange.interviewAudienceStore',
        'KitchenSink.view.interviewManagement.interviewArrange.interviewArrangeModel',
		'KitchenSink.view.interviewManagement.interviewArrange.interviewArrangeStore',
		'KitchenSink.view.interviewManagement.interviewArrange.interviewArrangeController'
	],
    title: Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_CAL_ARR_STD.panelTitle",'面试日程安排'),
	bodyStyle:'overflow-y:auto;overflow-x:hidden',
	reference: 'interviewArrangeInfoPanel',
	listeners:{
		resize:function( panel, width, height, oldWidth, oldHeight, eOpts ){
			var buttonHeight = 44;/*button height plus panel body padding*/
			var grid = panel.child('form').child('grid');
			if(grid) grid.setHeight( height -buttonHeight -100);
		}
	},
	initComponent: function (){
		//开启状态
		var msOpenStateStore = new KitchenSink.view.common.store.appTransStore("TZ_MS_OPEN_STATE");
		msOpenStateStore.load();

		Ext.apply(this,{
			items: [{
				xtype: 'form',
				reference: 'interviewArrangeForm',
				layout: {
					type: 'vbox',       // Arrange child items vertically
					align: 'stretch'    // 控件横向拉伸至容器大小
				},
				border: false,
				bodyPadding: 10,
				bodyStyle:'overflow-y:auto;overflow-x:hidden',

				fieldDefaults: {
					msgTarget: 'side',
					labelWidth: 120,
					labelStyle: 'font-weight:bold'
				},
				items: [{
					xtype: 'textfield',
					fieldLabel:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_CAL_ARR_STD.classID"," 报考班级ID"),
					name: 'classID',
					hidden:true
				},{
					xtype: 'textfield',
					fieldLabel:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_CAL_ARR_STD.className",'报考班级') ,
					name: 'className',
					fieldStyle:'background:#F4F4F4',
					readOnly:true
				},{
					xtype: 'textfield',
					fieldLabel: Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_CAL_ARR_STD.batchID", '面试批次ID'),
					name: 'batchID',
					hidden:true
				},{
					xtype: 'textfield',
					fieldLabel: Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_CAL_ARR_STD.batchName", '面试批次'),
					name: 'batchName',
					fieldStyle:'background:#F4F4F4',
					readOnly:true
				},{
					xtype: 'textfield',
					fieldLabel:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_CAL_ARR_STD.clearAllTimeArr",'清除所有时间安排'),
					name: 'clearAllTimeArr',
					hidden:true
				},{
					xtype: 'textfield',
					fieldLabel: Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_CAL_ARR_STD.batchStartDate", '面试开始日期'),
					name: 'batchStartDate',
					hidden:true
				},{
					xtype: 'textfield',
					fieldLabel: Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_CAL_ARR_STD.batchEndDate", '面试结束日期'),
					name: 'batchEndDate',
					hidden:true
				},{
					xtype: 'grid',
					frame: true,
					name: 'msjh_grid',
					reference: 'interviewArrangeDetailGrid',
					store: {
						type: 'interviewArrangeStore'
					},

					dockedItems:[{
						xtype:"toolbar",
						items:[
							{text:"自动生成面试安排计划",tooltip:"自动生成面试安排计划",iconCls:"",handler:'SetInterviewTime'},'-',
							{text:"设置参与本批次面试的考生",tooltip:"设置参与本批次面试的考生",iconCls:"set",handler:'setInterviewApplicant'},'-',
							{text:"查看预约考生",tooltip:"查看预约考生",iconCls:"view",handler:'viewArrangeStuList'},'->',
							{
								xtype:'splitbutton',
								text:'更多操作',
								iconCls:  'list',
								glyph: 61,
								menu:[{
										text:'清除选中时间安排',
										iconCls:"remove",
										handler:'ms_cleanTimeArr'
									},{
										text:'清除所有时间安排',
										iconCls:"reset",
										handler:'ms_cleanAllTimeArr'
									},{
										text:"发布选中记录",
										iconCls:"publish",
										handler:'releaseSelList'
									},{
										text:"撤销选中记录",
										iconCls:"revoke",
										handler:'UndoSelList'
									},{
										text:"导出选中记录到Excel",
										iconCls:"excel",
										handler:'exportToExcel'
									}]
							}
						]
					}],
					plugins: [/*{
						ptype:'rowexpander',
						rowBodyTpl : new Ext.XTemplate(
							'<div class="x-grid-group-title" style="margin-left:30px">',
							'<table class="x-grid3-row-table" cellspacing="0" cellpadding="0" border="0" >',
							'<tpl for="moreInfo">',
							'<tr style="line-height:30px;">',
							'<td  class="x-grid3-hd x-grid3-cell x-grid3-td-11" style="padding-right: 20px;">所在城市 ：</td><td style="font-weight: normal;max-width:800px;">{city}</td>',
							'</tr>',
							'<tr style="line-height:30px;">',
							'<td  class="x-grid3-hd x-grid3-cell x-grid3-td-11" style="padding-right: 20px;">所在国家 ：</td><td style="font-weight: normal;max-width:800px;">{country}</td>',
							'</tr>',
							'<tr style="line-height:30px;">',
							'<td  class="x-grid3-hd x-grid3-cell x-grid3-td-11" style="padding-right: 20px;">所属时区 ：</td><td style="font-weight: normal;max-width:800px;">{timezone}</td>',
							'</tr>',
							'<tr style="line-height:30px;">',
							'<td  class="x-grid3-hd x-grid3-cell x-grid3-td-11" style="padding-right: 20px;">时差（同北京） ：</td><td style="font-weight: normal;max-width:800px;">{timezoneDiff}</td>',
							'</tr>',
							'<tr style="line-height:30px;">',
							'<td  class="x-grid3-hd x-grid3-cell x-grid3-td-11" style="padding-right: 20px;">当地开始日期 ：</td><td style="font-weight: normal;max-width:800px;">{localStartDate}</td>',
							'</tr>',
							'<tr style="line-height:30px;">',
							'<td  class="x-grid3-hd x-grid3-cell x-grid3-td-11" style="padding-right: 20px;">当地结束日期 ：</td><td style="font-weight: normal;max-width:800px;">{localFinishDate}</td>',
							'</tr>',
							'<tr style="line-height:30px;">',
							'<td  class="x-grid3-hd x-grid3-cell x-grid3-td-11" style="padding-right: 20px;">邮箱 ：</td><td style="font-weight: normal;max-width:800px;">{lxEmail}</td>',
							'</tr>',
							'</tpl>',
							'</table>',
							'</div>',{}),
						lazyRender : true,
						enableCaching : false
					},*/{
						ptype: 'cellediting',
						pluginId: 'msArrCellEditingPlugin',
						clicksToEdit: 1
					}],
					columnLines: true,    //显示纵向表格线
					selModel:{
						type: 'checkboxmodel'
					},
					columns: [{
						text: Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_CAL_ARR_STD.msDate", '面试日期'),
						xtype:'datecolumn',
						format:'Y-m-d',
						sortable: true,
						dataIndex: 'msDate',
						editor:{
							xtype:"datefield",
							format:"Y-m-d"
						},
						width: 120
					},{
						text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_CAL_ARR_STD.maxPerson", '最多预约人数'),
						sortable: true,
						dataIndex: 'maxPerson',
						editor:{
							xtype:'numberfield',
							allowBlank:false,
							minValue: 1
						},
						width: 120
					},{
						text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_CAL_ARR_STD.bjMsStartTime",'开始时间'),
						xtype:'datecolumn',
						format:'H:i',
						sortable: true,
						dataIndex: 'bjMsStartTime',
						editor:{
							xtype: 'timefield',
							increment:5,
							editable:false,
							allowBlank: false,
							format:'H:i'
						},
						width: 100
					},{
						text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_CAL_ARR_STD.bjMsEndTime", '结束时间'),
						sortable: true,
						dataIndex: 'bjMsEndTime',
						xtype:'datecolumn',
						format:'H:i',
						editor:{
							xtype: 'timefield',
							increment:5,
							editable:false,
							allowBlank: false,
							format:'H:i'
						},
						width: 100
					},{
						text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_CAL_ARR_STD.msLocation", '面试地点'),
						dataIndex: 'msLocation',
						editor:{
							xtype:'textfield'
						},
						minWidth: 120,
						width: 120,
						flex: 1
					},{
						text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_CAL_ARR_STD.msXxBz", '备注'),
						dataIndex: 'msXxBz',
						editor:{
							xtype:'textfield'
						},
						minWidth: 120,
						width: 120,
						flex: 1
					},{
						xtype: 'actioncolumn',
						header:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_CAL_ARR_STD.releaseOrUndo","发布/撤销") ,
						minWidth:100,
						width:100,
						items:[
							{
								iconCls: '',
								tooltip: '',
								handler:'releaseOrUndo',
								getClass: function(v, metadata , r,rowIndex ,colIndex ,store ){
									if(store.getAt(rowIndex).get("releaseOrUndo")=='Y'){
										metadata['tdAttr'] = "data-qtip=撤销";
										return 'revoke';
									}else{
										metadata['tdAttr'] = "data-qtip=发布";
										return 'publish';
									}
								}
							}
						]
					},{
						menuDisabled: true,
						sortable: false,
						header:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_CAL_ARR_STD.option", '操作'),
						width:60,
						xtype: 'actioncolumn',
						items:[
							{	iconCls: 'add',tooltip: '添加',handler:'addMsCalRow'},
							{	iconCls: 'remove',tooltip: '删除',handler:'deleteMsCalRow'}
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
						displayInfo: true,
						/*
						displayMsg: '显示{0}-{1}条，共{2}条',
						beforePageText: '第',
						afterPageText: '页/共{0}页',
						emptyMsg: '没有数据显示',
						*/
						plugins: new Ext.ux.ProgressBarPager()
					}
				}]
			}]
		});
		this.callParent();
	},

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
