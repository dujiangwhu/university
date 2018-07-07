Ext.define('KitchenSink.view.payment.payInfoSerchWin', {
    extend: 'Ext.window.Window',
    xtype: 'payInfoSerchWin',
    requires:['Ext.window.Window'],
    reference:'payInfoSerchWin',
    title: '查询',
    width: 600,
    height: 240,
    plain: true,
    //border: true,
    resizable:false,
    items: [{
            layout:{
            	xtype:'vbox',
            },
            fieldDefaults: {
            	labelWidth: 60
            },
            bodyPadding:5,
            margin:'10 10 10 10',
			items:[
				{
				    margin:'10 0 10 0',//上右下左
					layout: {
							type: 'hbox',
					},
					items:[
					{
						fieldLabel:"项目名称",
						xtype:"textfield",
						value:"包含",
					    margin:'0 10 0 0',//上右下左
						editable:false,
					},
					{
						xtype:"textfield",
						name:"projectName"
					}]
			   },
					//-----------------
			   {
					layout: {
						type: 'hbox',
					},
					margin:'10 0 10 0',//上右下左
					items:[
					{
						fieldLabel:"项目类型",
						xtype:"textfield",
						margin:'0 10 0 0',//上右下左
						value:"等于",
						editable:false
					},
					{
						xtype:"combobox",
						name:"projectType",
						editable:false,
			            displayField: 'state',
			            queryMode: 'local',
			            name: 'projectType',
			            hiddenName:'projectType',
			            valueField: 'TValue',
			            displayField: 'TSDesc',
			            store: new KitchenSink.view.common.store.appTransStore("TZ_PAY_PRO_TYPE"),
			            editable:false,
			           // value:'002',
			            allowBlank: false
					}]
				},      
			]
    }] ,
    buttons: [
                  {
               	   text: '搜索',
               	   iconCls:'query',
               	   handler:'queryRecord',
                  },
                  {
               	   text: '清除',
               	   iconCls:'remove',
               	   handler:'clearRecord'
                  },
                  {
               	   text: '关闭',
               	   handler:'closeWin',
       }]
});
