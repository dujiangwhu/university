Ext.define("KitchenSink.view.payment.payInfoGrid",{
	extend:"Ext.grid.Panel",
	requires:[
		"Ext.data.*",
        "Ext.grid.*",
        "Ext.util.*",
        "Ext.toolbar.Paging",
        "Ext.ux.ProgressBarPager",
		"Ext.form.*",
		"KitchenSink.view.payment.payInfoModel",
		"KitchenSink.view.payment.payInfoStore",
		"KitchenSink.view.payment.payInfoController",
		"KitchenSink.view.payment.payInfoSerchWin",
		"KitchenSink.view.payment.showPayInfoForm"
	],
	xtype: "payInfoGrid",
    controller: "payInfoController",
	name:'payInfoGrid',
    reference:"payInfoGrid",
    columnLines: true,
    rowLines:true,
    style:"margin:8px",
    frame:true,
	title:"支付查看及管理",
	dockedItems:[
		{
			xtype:"toolbar",
			dock:"bottom",
			ui:"footer",
			items:["->",
				{minWidth:80,text:"关闭",iconCls:"close",handler:"closePayInfoGrid"},"-"
			]
		},{
			xtype:"toolbar",
			dock:"top",
			//ui:"footer",
			items:["-",{minWidth:80,text:"查询",iconCls:"query",handler:"queryPayInfo"}]
		}
	],
	 columns: [
		      	 {
		      	     text:"项目Id",
		      	     sortable: true,
		      	     hidden:true,
		      	     dataIndex: 'projectId',//班级Id
		      	     width:200
		      	},	
		      	 {
		      	     text:"账户ID",
		      	     sortable: true,
		      	     hidden:true,
		      	     dataIndex: 'payAccountId',//账户ID
		      	     width:200
		      	},	
		      	 {
		      	     text:"账户号",
		      	     sortable: true,
		      	     hidden:true,
		      	     dataIndex: 'payAccountName',//账户号
		      	     width:200
		      	},	
		          {
		      		text:"项目名称",
		      		sortable: true,
		              dataIndex: 'projectName',//班级名称
		              width:200
		          },
		      	{
		              text:"项目类别",//类型002 翻译称"班级招生报名费"
		              sortable: true,
		              dataIndex: 'projectType',
		              renderer:function(val){
		            	  if(val=="002"){
		            		  return "班级招生报名费";
		            	  }
		              },
		              width:200
		          },
		          {
		              text:"支付 人数",
		              sortable: true,
		              dataIndex: 'payCount',
		              width:150
		          },
		      	{
		              text:"成功 人数",
		              sortable: true,
		              dataIndex: 'sPayCount',
		              width:150
		          },
		      	{
		              text:"不成功 人数",
		              sortable: true,
		             dataIndex: 'fPayCount',
		               width:150
		          },
		      	{
		          text:"查看",
		          xtype:"actioncolumn",
		          items:[{
		               dataIndex: 'resultShow',
		               width:'fit',
		               iconCls:'query',
		               tooltip:"Edit",
		               width:100,
		               handler:function(obj,rowIndex){
		            	   //选中行
//		            	   var selList =  this.up("grid[name='payInfoGrid']").getSelectionModel().getSelection();
//		            	   //选中行长度
//		            	   var checkLen = selList.length;
//		            	  console.log("selList:"+selList);
//		            	   if(checkLen == 0){
//		            		   Ext.Msg.alert("提示","请选择要查看的项目");
//		            		   return;
//		            	   }
		            	   //点中行
		            	   var selList=obj.up("grid[name='payInfoGrid']").store.getAt(rowIndex);
		            	  
		            	   //------加载 信息显示窗口
		            	   //--1.验证权限
		            	   //取得进行编辑的窗体
		           		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_ZFCKJGL_COM"]["TZ_SEARCH_DETAIL"];
		                   if (pageResSet == "" || pageResSet == undefined) {
		                       Ext.MessageBox.alert('提示', '您没有修改数据的权限');
		                       return;
		                   }
		                   var className = pageResSet["jsClassName"];
		                   //alert("className:"+className);
		                   if (className == "" || className == undefined) {
		                       Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_SEARCH_DETAIL，请检查配置。');
		                       return;
		                   }
		               	   var view=this.findParentByType("grid");
		               	   var win = this.lookupReference('showPayInfoForm');
		            	   //--2.加载窗口
		            	   //var tzParams = '{"ComID":"TZ_ZFCKJGL_COM","PageID":"TZ_SEARCH_DETAIL","OperateType":"QF","comParams":{"projectId":"'+selList.get("projectId")+'"}}';
		            	  // Ext.tzLoad(tzParams,function(response){
		           				
		           				
		           				if(!win){
		           					Ext.syncRequire(className);
		           					ViewClass=Ext.ClassManager.get(className);
		           
		           					var themeName = Ext.themeName;
		           					
		           					var contentPanel = Ext.getCmp('tranzvision-framework-content-panel');			
		           					contentPanel.body.addCls('kitchensink-example');

		           					var cmp = new ViewClass();
		           					
		           					var tab = contentPanel.add(cmp);     
		           					
		           					contentPanel.setActiveTab(tab);

		           					Ext.resumeLayouts(true);

		           					if (cmp.floating) {
		           						cmp.show();
		           					}
		           					//
		           					//alert(cmp.down("container[name='classData']"));
		           					//var classData=cmp.down("container[name='classData']");
		           					//classData.down("textfield[name='projectId']").setValue(selList[0].get("projectId"));
		           					//classData.down("textfield[name='projectName']").setValue(selList[0].get("projectName"));
		           					//classData.down("textfield[name='payAccountName']").setValue(selList[0].get("payAccountName"));
		           					//填坑
		           					var classData=cmp.down("form[name='classData']");
		           					var dataString='{"projectId":"'+selList.get("projectId")+'","projectName":"'+selList.get("projectName")+'","payAccountName":"'+selList.get("payAccountName")+'"}';
		           					//console.log("dataString"+dataString);
		           					classData.getForm().setValues(Ext.decode(dataString));
		           					
		           					var tzParams = '{"ComID":"TZ_ZFCKJGL_COM","PageID":"TZ_SEARCH_DETAIL","OperateType":"QG","comParams":{"projectId":"'+selList.get("projectId")+'"}}';
		           					cmp.getStore().tzStoreParams=tzParams;
		           					cmp.getStore().reload();
		           				}
		            	  // });
		               }
		         }]
		          }],
	//初始化grid
	initComponent:function(){
		var store=new KitchenSink.view.payment.payInfoStore();
		 Ext.apply(this,{
					store:store,
					bbar: {
		            xtype: 'pagingtoolbar',
		            pageSize: 10,
		            store: store,
		            displayInfo: true,
		            displayMsg: '显示{0}-{1}条，共{2}条',
		            beforePageText: '第',
		            afterPageText: '页/共{0}页',
		            emptyMsg: '没有数据显示',
		            plugins: new Ext.ux.ProgressBarPager()} 
		 });
	
        this.callParent();
	}
});