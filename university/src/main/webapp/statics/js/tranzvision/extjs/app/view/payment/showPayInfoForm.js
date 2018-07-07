Ext.define("KitchenSink.view.payment.showPayInfoForm",{
	extend:"Ext.grid.Panel",
	requires:[
		"Ext.panel.*",
		"Ext.util.*",
		"Ext.data.*",
		"Ext.toolbar.Paging",
        "Ext.ux.ProgressBarPager",
		"Ext.form.*",
		"Ext.grid.*",
		"KitchenSink.view.payment.showPayInfoModel",
		"KitchenSink.view.payment.showPayInfoStore",
		//"KitchenSink.view.payment.showPayInfoController",
		"KitchenSink.view.payment.payInfoPersonalForm",
	],
	name:"showPayInfoForm",
	xtype:"showPayInfoForm",
	title:"项目支付查看",
	frame:true,
	bodyPadding:10,
	reference:"showPayInfoForm",
	columnLines: true,
	dockedItems:[
		             {
		            	 xtype:"toolbar",
		            	 dock:"bottom",
		            	 ui:"footer",
		            	 items:[
		            	        "->",{text:"关闭",icoCls:"save",handler:function(btn){var win=btn.findParentByType("grid"); win.close();}},"-"
		            	        ]
		             },{
		            	 dock:"top",
		            	 name:"classData",
		            	 xtype:"form",
		            	 items:[{
		            		 layout:{
		            			 xtype:"vbox",
		            			 align:"stretch"
		            		 },
		            		 margin:"10 0 0 20",
		            		 items:[
		            		        {fieldLabel:"班级支付Id",xtype:"textfield",name:"tzPayPrjId",readOnly:true,editable:true,hidden:true},
		            		        {fieldLabel:"支付Id",xtype:"textfield",name:"id",readOnly:true,editable:true,hidden:true},
		            		        {fieldLabel:"班级名称",xtype:"textfield",name:"projectName",readOnly:true,editable:true},
		            		        {fieldLabel:"支付账户",xtype:"textfield",name:"payAccountName",readOnly:true,editable:true},  
		            		 ]
		            	 }]
		             }
		],
		initComponent:function(){
			var store=new KitchenSink.view.payment.showPayInfoStore();
			//alert(store);
			 Ext.apply(this,{
						store:store,
					 	columns:[
						        {text:"学生ID",dataIndex:"userId",width:200,hidden:true},
								{text:"姓名",width:200,dataIndex:"userName"},
								{text:"支付状态",width:200,dataIndex:"payStatus",renderer:function(val){
									if(val=="01"){
										return "等待支付";
									}else if(val=="02"){
										return "支付成功";
									}else if(val=="03"){
										return "支付失败";
									}else if(val=="04"){
										return "验证失败";
									}
								}},
								{text:"提交时间",width:200,dataIndex:"submitTime"},
								{text:"支付时间",width:200,dataIndex:"payedTime"},
								{text:"操作",width:150, xtype:"actioncolumn", iconCls:'query',
							     items:[{
							    	 handler:function(btn,rowIndex){
							    		//选中行
							    		   var me=this;
						            	   //var selList =  this.up("grid[name='showPayInfoForm']").getSelectionModel().getSelection();
						            	   //选中行长度
						            	   //var checkLen = selList.length;
						            	   //console.log("selList:"+selList);
						            	   // if(checkLen == 0){
						            	   //   Ext.Msg.alert("提示","请选择要查看的项目");
						            	   //   return;
						            	   //}
						            	  //点出行数据
							    		var recordData=this.up("grid[name='showPayInfoForm']").store.getAt(rowIndex);
						            	   //------加载 信息显示窗口
						            	   //--1.验证权限
						            	   //取得进行编辑的窗体
						           		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_ZFCKJGL_COM"]["TZ_PERSONAL_DETAIL"];
						                   if (pageResSet == "" || pageResSet == undefined) {
						                       Ext.MessageBox.alert('提示', '您没有修改数据的权限');
						                       return;
						                   }
						                   var className = pageResSet["jsClassName"];
						                   //alert("className:"+className);
						                   if (className == "" || className == undefined) {
						                       Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_PERSONAL_DETAIL，请检查配置。');
						                       return;
						                   }
						               	   var view=this.findParentByType("grid");
						               	   var personalForm = this.lookupReference('payInfoPersonalForm');
						            	   //--2.加载窗口
						               	   //console.dir(formData);
						               	   var tzParams = '{"ComID":"TZ_ZFCKJGL_COM","PageID":"TZ_SEARCH_DETAIL","OperateType":"QF","comParams":{"id":"'+recordData.get("id")+'","tzPayPrjId":"'+recordData.get("tzPayPrjId")+'"}}';
						            	   Ext.tzLoad(tzParams,function(response){
						            		   console.dir(response.root);
						            		   var data=response.root;
						            		   //---支付ID是唯一标识
						            		   if(!personalForm){
						            			   Ext.syncRequire(className);
						            			   ViewClass=Ext.ClassManager.get(className);
						            			   
						            			   var themeName = Ext.themeName;
						            			   
						            			   var contentPanel = Ext.getCmp('tranzvision-framework-content-panel');			
						            			   contentPanel.body.addCls('kitchensink-example');
						            			   
						            			   var personalForm = new ViewClass();
						            			   
						            			   var tab = contentPanel.add(personalForm);     
						            			   
						            			   contentPanel.setActiveTab(tab);
						            			   
						            			   Ext.resumeLayouts(true);
						            			   
						            			   if (personalForm.floating) {
						            				   personalForm.show();
						            			   }
						            			   personalForm.getForm().setValues(data);
						            			   //班级名称 和支付账户是固定不变的
						            			   var classData=me.up("grid[name='showPayInfoForm']").down("form[name='classData']");
						           					//classData.down("textfield[name='projectId']").getVaule();
						           					//classData.down("textfield[name='projectName']").getValue();
						           					//classData.down("textfield[name='payAccount']").getValue();
						           					
						            			   //personalForm.getForm().findField("projectName").setValue(classData.down("textfield[name='projectName']").getValue());
						            			   //personalForm.getForm().findField("payAccountName").setValue(classData.down("textfield[name='payAccountName']").getValue());
						            			   //personalForm.getForm().findField("id").setValue(recordData.get("id"));
						            			   //personalForm.getForm().findField("projectId").setValue(recordData.get("projectId"));
						            			   //填坑
						           					var dataString='{"tzPayPrjId":"'+recordData.get("tzPayPrjId")+'","projectName":"'+classData.down("textfield[name='projectName']").getValue()+'","payAccountName":"'+classData.down("textfield[name='payAccountName']").getValue()+'","id":"'+recordData.get("id")+'"}';
						           					//console.log("dataString"+dataString);
						           					personalForm.getForm().setValues(Ext.decode(dataString));
						            		   }
						            	   })
							    	 }
							     }]
							    }
						],
						bbar: {
			            xtype: 'pagingtoolbar',
			            pageSize: 10,
			            store: store,
			            displayInfo: true,
			            displayMsg: '显示{0}-{1}条，共{2}条',
			            beforePageText: '第',
			            afterPageText: '页/共{0}页',
			            emptyMsg: '没有数据显示',
			            plugins: new Ext.ux.ProgressBarPager()
			 } 
			 });
		
	        this.callParent();
		}
	
});