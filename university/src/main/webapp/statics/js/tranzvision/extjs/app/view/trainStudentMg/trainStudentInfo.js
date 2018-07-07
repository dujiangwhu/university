Ext.define('KitchenSink.view.trainStudentMg.trainStudentInfo', {
    extend: 'Ext.panel.Panel',
    xtype: 'trainStudentInfo',
	//controller: 'trainStudentMg',
	requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
		'Ext.ux.ProgressBarPager',
        //'KitchenSink.view.trainStudentMg.trainStudentController',
		'KitchenSink.view.trainStudentMg.trainStudentStore'
	],
    title: '培训学员管理', 
	bodyStyle:'overflow-y:auto;overflow-x:hidden',
	actType: 'update',//默认修改
    listeners:{
        afterrender: function(panel){

            var form = panel.child('form').getForm();

            var tzParams = '{"ComID":"TZ_PX_STU_COM","PageID":"TZ_PX_STU_STD","OperateType":"QF","comParams":{}}';

            Ext.tzLoad (tzParams,function(responseData){
                //组件注册信息数据
                var formData = responseData.formData;
                form.setValues(formData);
                form.findField("orgId").setReadOnly(true);
                form.findField("orgName").setReadOnly(true);
                panel.commitChanges(panel);
            });
        }
    },
	initComponent: function (){
		Ext.apply(this, {
		items: [{
			xtype: 'form',
			reference: 'stuOrgInfoForm',
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
				labelWidth: 140,
				labelStyle: 'font-weight:bold'
			},
			items: [{
				xtype: 'textfield',
				//fieldLabel: '机构编号',
				fieldLabel: Ext.tzGetResourse("TZ_PX_STU_COM.TZ_PX_STU_STD.orgId","机构编号"),
				name: 'orgId',
				cls:'lanage_1'
			}, {
				xtype: 'textfield',
				//fieldLabel: '机构名称',
				cls:'lanage_1',
				fieldLabel: Ext.tzGetResourse("TZ_PX_STU_COM.TZ_PX_STU_STD.orgName","机构名称"),
				name: 'orgName'
			}]
		},{
			xtype: 'grid', 
			title: '学员管理',
			frame: true,
			columnLines: true,
			height: 350,
			reference: 'studentGrid',
			style: "margin:10px",
			viewConfig: {
				plugins: {
					ptype: 'gridviewdragdrop',
					containerScroll: true,
					dragGroup: this,
					dropGroup: this
				},
				listeners: {
					drop: function(node, data, dropRec, dropPosition) {
						data.view.store.beginUpdate();
						var items = data.view.store.data.items;
						for(var i = 0;i< items.length;i++){
							items[i].set('orderNum',i+1);
						}
						data.view.store.endUpdate();
					}
				}
			},
			columns: [{ 
				text: Ext.tzGetResourse("TZ_PX_STU_COM.TZ_PX_STU_STD.orgid","机构编号"),
				dataIndex: 'orgid',
				hidden: true
			},{ 
				text: Ext.tzGetResourse("TZ_PX_STU_COM.TZ_PX_STU_STD.oprid","学员ID"),
				dataIndex: 'oprid',
				hidden: true
			},{
				//xtype: 'rownumberer',
				text: '序号',
				dataIndex: 'orderNum',
				width:60
			},{
				text: Ext.tzGetResourse("TZ_PX_STU_COM.TZ_PX_STU_STD.stuName","学员姓名"),
				dataIndex: 'stuName',
				width:240,
				flex:1
			},{
				text: Ext.tzGetResourse("TZ_PX_STU_COM.TZ_PX_STU_STD.age","年龄"),
				dataIndex: 'age',
				width: 100
			},
			{
					text:Ext.tzGetResourse("TZ_AQ_YHZHGL_COM.TZ_AQ_YHZHXX_STD.sex","性别"),
					sortable: true,
					dataIndex: 'sex',
					width: 100,
					align:'center',
					renderer : function(value, metadata, record) {
						if (value=="M"){
							return "男";
						}else if(value=="F"){
							return "女";
						}
					}
			},{
				text: Ext.tzGetResourse("TZ_PX_STU_COM.TZ_PX_STU_STD.stuQQ","qq"),
				dataIndex: 'stuQQ',
				width: 120
			},{
				text: Ext.tzGetResourse("TZ_PX_STU_COM.TZ_PX_STU_STD.stuPhone","手机"),
				dataIndex: 'stuPhone',
				width: 120
			},{
				text: Ext.tzGetResourse("TZ_PX_STU_COM.TZ_PX_STU_STD.stuEmail","邮箱"),
				dataIndex: 'stuEmail',
				width: 150
			},{
				text: Ext.tzGetResourse("TZ_PX_STU_COM.TZ_PX_STU_STD.stuRemaindTimeCard","剩余课时"),
				dataIndex: 'stuRemaindTimeCard',
				width: 120
			},
			{
					menuDisabled: true,
					sortable: false,
					width:80,
					align:'center',
					xtype: 'actioncolumn',
					items:[
						{iconCls: 'edit',tooltip: '编辑',handler: 'editStudentInfoOne'}
					]
				}
			],
			store: {
				type: 'trainStudentStore'
			},
			dockedItems:[{
				xtype:"toolbar",
				items:[
					{text:"新增",tooltip:"添加学员",iconCls:"add",handler:"addStudentInfo"},"-",
					{text:"编辑",tooltip:"编辑学员信息",iconCls:"edit",handler:"editStudentInfo"},"-",
					{text:"给学员分配课时卡",tooltip:"给学员分配课时卡",iconCls:"edit",handler:"assignStudentTimeCard"}
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
		}]
	});
	this.callParent();
	},
    buttons: [{
		text: '关闭',
		iconCls:"close",
		handler: 'onFormClose'
	}]

});

