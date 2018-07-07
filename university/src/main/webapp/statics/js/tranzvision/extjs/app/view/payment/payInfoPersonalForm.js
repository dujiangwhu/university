Ext.define("KitchenSink.view.payment.payInfoPersonalForm",{
	extend:"Ext.form.Panel",
	requires:[
		"Ext.data.*",
        "Ext.grid.*",
        "Ext.util.*",
		"Ext.form.*",
		"KitchenSink.view.payment.payInfoPersonalController",
	],
	name:"payInfoPersonalForm",
	title:"个人支付信息",
	xtype:"payInfoPersonalForm",
	reference:"payInfoPersonalForm",
	controller:"payInfoPersonalController",
	columnLines: true,
	frame:true,
	padding:"20 0 0 20",
	fieldDefaults: {
		  width:"100%"
    },
	items:[
	     {fieldLabel:"班级支付Id",xtype:"displayfield",name:"tzPayPrjId",hidden:true},
		{fieldLabel:"支付Id",xtype:"displayfield",name:"id",hidden:true},//唯一标识
		{fieldLabel:"班级姓名",xtype:"textfield",editable:false,readOnly:true,name:"projectName"},
		{fieldLabel:"支付账户",xtype:"textfield",editable:false,readOnly:true,name:"payAccountName"},
		{fieldLabel:"姓名",xtype:"textfield",editable:false,readOnly:true,name:"userName"},
		{fieldLabel:"状态",name:"payStatus",
            xtype: 'combobox',
            displayField: 'payStatus',
            width:"100%",
            queryMode: 'local',
            name: 'payStatus',
            hiddenName:'payStatus',
            valueField: 'TValue',
            cls:'lanage_1',
            displayField: 'TSDesc',
            store: new KitchenSink.view.common.store.appTransStore("TZ_PAY_STATUES"),
            editable:false,
            readOnly:true,
            allowBlank: false,
            listeners: {
                change:{
                    fn: function (ch,eOpts ) {
                      //如果发布状态为‘未完成’，那么状态值可以修改；如果为‘支付完成’，状态值不能修改
                        if(ch.getValue()!="02"){
                            var form= ch.findParentByType("form").getForm();
                            var state=form.findField("payStatus");
                            state.setReadOnly(false);
                        }
                    }
                }
            },
		},
		{fieldLabel:"提交时间",xtype:"textfield",editable:false,readOnly:true,name:"submitTime"},
		{fieldLabel:"完成时间",xtype:"textfield",editable:false,readOnly:true,name:"payedTime"},
		{fieldLabel:"币种",xtype:"displayfield",name:"currency",renderer:function(val){
			//CNY（人民币）USD（美元）EUR（欧元）GBP（英镑）JPY（日元）KRW（韩元）ADU（澳元）
			if(val=="CNY"){
				return "人民币"
			}else if(val=="USD"){
				return "美元";
			}else if(val=="EUR"){
				return "欧元";
			}else if(val=="GBP"){
				return "英镑";
			}else if(val=="JPY"){
				return "日元";
			}else if(val=="KRW"){
				return "韩元";
			}else if(val=="ADU"){
				return "澳元";
			}
		}},
		{fieldLabel:"金额",xtype:"textfield",editable:false,readOnly:true,name:"payAmount"},
		//{fieldLabel:"提交人",xtype:"textfield",editable:false,hidden:true,readOnly:true,name:"submitName"}
	],
	dockedItems:[

	             {
	            	 xtype:"toolbar",
	            	 dock:"bottom",
	            	 margin:"0 0 20 0",
	            	 ui:"footer",
	            	 items:[
	            	        "->",{text:"保存",handler:"savePersonalPayForm"}, "-"
	            	        ,{text:"确定",handler:"ensurePersonalPayForm"}, "-"
	            	        ,{text:"关闭",handler:"closePersonalPayForm"}, "-"
	            	        ]
	             } 
	]
});