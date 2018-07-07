Ext.define('KitchenSink.view.audienceManagement.audienceManagementController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.audienceManagementController', 
	//新增听众
	addAudInfo: function() {
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_AUD_COM"]["TZ_AUD_PANEL_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_AUD_PANEL_STD，请检查配置。');
			return;
		}
		
		var contentPanel,cmp, className, ViewClass, clsProto;
		var themeName = Ext.themeName;
		
		contentPanel = Ext.getCmp('tranzvision-framework-content-panel');			
		contentPanel.body.addCls('kitchensink-example');

		if(!Ext.ClassManager.isCreated(className)){
			Ext.syncRequire(className);
		}	
		ViewClass = Ext.ClassManager.get(className);

		cmp = new ViewClass();
	    cmp.on('afterrender',function(panel){
			var cmpForm = panel.child("form").getForm();
			
			var initData = {
				audStat: "1"
			};
			
			cmpForm.setValues(initData);
		});

		tab = contentPanel.add(cmp);     
		
		contentPanel.setActiveTab(tab);

		Ext.resumeLayouts(true);

		if (cmp.floating) {
			cmp.show();
		}
    },


	//删除一条听众    
	delSelAudCy: function(view, rowIndex){
		Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
			if(btnId == 'yes'){					   
			   var store = view.findParentByType("grid").store;
			   store.removeAt(rowIndex);
			}												  
		},this);  
	},
	
	//删除选中听众  	
	delSelAudList: function(btn){
	   //选中行
	   var selList = btn.findParentByType("grid").getSelectionModel().getSelection();
	   //选中行长度
	   var checkLen = selList.length;
	   if(checkLen == 0){
			Ext.Msg.alert("提示","请选择要删除的记录");   
			return;
	   }else{
			Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
				if(btnId == 'yes'){		
				   var gridStore =btn.findParentByType("grid").getStore();
				   gridStore.remove(selList);
				}												  
			},this);   
	   }
	},
	
	editAudInfo: function(view, rowIndex){
		var store = view.findParentByType("grid").store;
		var selRec = store.getAt(rowIndex);
	    var audId = selRec.data.audId;
	    
		this.editAudDefnByID(audId);
	},
	
	editSelAudiInfo: function(view, rowIndex){
		 var store = view.findParentByType("grid").store;
		 var selRec = store.getAt(rowIndex);
		 //ID
	   	var audId = selRec.get("audId");
	   	var audName = selRec.get("audName");
		var audStat = selRec.get("audStat");
		var audType = selRec.get("audType");
		var audMS = selRec.get("audMS");
		var audSQL = selRec.get("audSQL");
		var audLY = selRec.get("audLY");
	     //显示组件注册信息编辑页面
	     this.editSelAudiInfoByID(audId,audName,audStat,audType,audMS,audSQL,audLY);
	},
	editSelAudiInfoByID: function(audId,audName,audStat,audType,audMS,audSQL,audLY){
		//是否有访问权限
		
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_AUD_COM"]["TZ_AUD_PANEL_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_AUD_PANEL_STD，请检查配置。');
			return;
		}
		
		var contentPanel,cmp, className, ViewClass, clsProto;
		var themeName = Ext.themeName;
		
		contentPanel = Ext.getCmp('tranzvision-framework-content-panel');			
		contentPanel.body.addCls('kitchensink-example');

		if(!Ext.ClassManager.isCreated(className)){
			Ext.syncRequire(className);
		}	
		ViewClass = Ext.ClassManager.get(className);

		clsProto = ViewClass.prototype;

		if (clsProto.themes) {
			clsProto.themeInfo = clsProto.themes[themeName];

			if (themeName === 'gray') {
				clsProto.themeInfo = Ext.applyIf(clsProto.themeInfo || {}, clsProto.themes.classic);
			} else if (themeName !== 'neptune' && themeName !== 'classic') {
				if (themeName === 'crisp-touch') {
					clsProto.themeInfo = Ext.applyIf(clsProto.themeInfo || {}, clsProto.themes['neptune-touch']);
				}
				clsProto.themeInfo = Ext.applyIf(clsProto.themeInfo || {}, clsProto.themes.neptune);
			}
			// <debug warn>
			// Sometimes we forget to include allowances for other themes, so issue a warning as a reminder.
			if (!clsProto.themeInfo) {
				Ext.log.warn ( 'Example \'' + className + '\' lacks a theme specification for the selected theme: \'' +
					themeName + '\'. Is this intentional?');
			}
			// </debug>
		}

		cmp = new ViewClass();
		//操作类型设置为更新
		cmp.actType = "update";
		
		cmp.on('afterrender',function(panel){
			//组件注册表单信息;
			var form = panel.child('form').getForm();
			//页面注册信息列表
			var gridStore =panel.child('grid').getStore();
			//参数
			var tzParams = '{"ComID":"TZ_AUD_COM","PageID":"TZ_AUD_NEW_STD","OperateType":"QF","comParams":{"audId":"'+audId+'","audName":"'+audName+'","audStat":"'+audStat+'","audType":"'+audType+'","audMS":"'+audMS+'","audSQL":"'+audSQL+'","audLY":"'+audLY+'"}}';

			//加载数据
			var tzStoreParams =  '{"cfgSrhId":"TZ_AUD_COM.TZ_AUD_NEW_STD.PS_TZ_AUDCY_VW","condition":{"TZ_AUD_ID-operator": "01","TZ_AUD_ID-value": "'+ audId+'"}}';
			
			Ext.tzLoad(tzParams,function(responseData){
			
				form.setValues(responseData);
			
				//页面注册信息列表数据
				var roleList = responseData.listData;
				gridStore.tzStoreParams = tzStoreParams;
				gridStore.reload();
				
			});
			
			
		});
		
		tab = contentPanel.add(cmp);     
		
		contentPanel.setActiveTab(tab);

		Ext.resumeLayouts(true);

		if (cmp.floating) {
			cmp.show();
		}
	},
	
	
	editSelAudInfo: function(btn){
		//选中行
	    var selList = btn.findParentByType("grid").getSelectionModel().getSelection();
	    //选中行长度
	    var checkLen = selList.length;
	    if(checkLen == 0){
			Ext.Msg.alert("提示","请选择一条要修改的记录");   
			return;
	    }else if(checkLen >1){
		   Ext.Msg.alert("提示","只能选择一条要修改的记录");   
		   return;
	    }
	    var audId = selList[0].data.audId;
	    
	    this.editAudDefnByID(audId);
	},
	//
	editAudiInfo: function(btn){
		
		//选中行
	    var selList = btn.findParentByType("grid").getSelectionModel().getSelection();
	    //选中行长度
	    var checkLen = selList.length;
	    if(checkLen == 0){
			Ext.Msg.alert("提示","请选择一条要修改的记录");   
			return;
	    }else if(checkLen >1){
		   Ext.Msg.alert("提示","只能选择一条要修改的记录");   
		   return;
	    }
		
		//组件ID
		var audId = selList[0].get("audId");
		var audName = selList[0].get("audName");
		var audStat = selList[0].get("audStat");
		var audType = selList[0].get("audType");
		var audMS = selList[0].get("audMS");
		var audSQL = selList[0].get("audSQL");
		var audLY = selList[0].get("audLY");
		
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_AUD_COM"]["TZ_AUD_PANEL_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_AUD_PANEL_STD，请检查配置。');
			return;
		}
		
		var contentPanel,cmp, className, ViewClass, clsProto;
		var themeName = Ext.themeName;
		
		contentPanel = Ext.getCmp('tranzvision-framework-content-panel');			
		contentPanel.body.addCls('kitchensink-example');

		if(!Ext.ClassManager.isCreated(className)){
			Ext.syncRequire(className);
		}	
		ViewClass = Ext.ClassManager.get(className);

		clsProto = ViewClass.prototype;

		if (clsProto.themes) {
			clsProto.themeInfo = clsProto.themes[themeName];

			if (themeName === 'gray') {
				clsProto.themeInfo = Ext.applyIf(clsProto.themeInfo || {}, clsProto.themes.classic);
			} else if (themeName !== 'neptune' && themeName !== 'classic') {
				if (themeName === 'crisp-touch') {
					clsProto.themeInfo = Ext.applyIf(clsProto.themeInfo || {}, clsProto.themes['neptune-touch']);
				}
				clsProto.themeInfo = Ext.applyIf(clsProto.themeInfo || {}, clsProto.themes.neptune);
			}
			// <debug warn>
			// Sometimes we forget to include allowances for other themes, so issue a warning as a reminder.
			if (!clsProto.themeInfo) {
				Ext.log.warn ( 'Example \'' + className + '\' lacks a theme specification for the selected theme: \'' +
					themeName + '\'. Is this intentional?');
			}
			// </debug>
		}

		cmp = new ViewClass();
		//操作类型设置为更新
		cmp.actType = "update";
		
		cmp.on('afterrender',function(panel){
			//组件注册表单信息;
			var form = panel.child('form').getForm();

			//页面注册信息列表
			var gridStore =panel.child('grid').getStore();
			//参数
			var tzParams = '{"ComID":"TZ_AUD_COM","PageID":"TZ_AUD_NEW_STD","OperateType":"QF","comParams":{"audId":"'+audId+'","audName":"'+audName+'","audStat":"'+audStat+'","audType":"'+audType+'","audMS":"'+audMS+'","audSQL":"'+audSQL+'","audLY":"'+audLY+'"}}';

			//加载数据
			var tzStoreParams =  '{"cfgSrhId":"TZ_AUD_COM.TZ_AUD_NEW_STD.PS_TZ_AUDCY_VW","condition":{"TZ_AUD_ID-operator": "01","TZ_AUD_ID-value": "'+ audId+'"}}';
			
			Ext.tzLoad(tzParams,function(responseData){
				//组件注册信息数据
			
				form.setValues(responseData);
			
				//页面注册信息列表数据
				var roleList = responseData.listData;	
				gridStore.tzStoreParams = tzStoreParams;
				gridStore.reload();
				
			});
			
			
		});
		
		tab = contentPanel.add(cmp);     
		
		contentPanel.setActiveTab(tab);

		Ext.resumeLayouts(true);

		if (cmp.floating) {
			cmp.show();
		}
	
},
	
	editAudDefnByID: function(audId){
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_AUD_COM"]["TZ_AUD_PANEL_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_AUD_PANEL_STD，请检查配置。');
			return;
		}
		
		var contentPanel,cmp, className, ViewClass, clsProto;
		var themeName = Ext.themeName;
		
		contentPanel = Ext.getCmp('tranzvision-framework-content-panel');			
		contentPanel.body.addCls('kitchensink-example');

		if(!Ext.ClassManager.isCreated(className)){
			Ext.syncRequire(className);
		}	
		ViewClass = Ext.ClassManager.get(className);

		cmp = new ViewClass();
		//操作类型设置为更新
		cmp.actType = "update";
		cmp.on('afterrender',function(panel){
			var form = panel.child('form').getForm();
			var gridStore =panel.child('grid').getStore();
			//参数	
			var tzParams = '{"ComID":"TZ_AUD_COM","PageID":"TZ_AUD_PANEL_STD","OperateType":"QF","comParams":{"audId":"'+ audId +'"}}';
			//加载数据
			var tzStoreParams =  '{"cfgSrhId":"TZ_AUD_COM.TZ_AUD_PANEL_STD.PS_TZ_AUDCY_VW","condition":{"TZ_AUD_ID-operator": "01","TZ_AUD_ID-value": "'+ audId+'"}}';
			
			Ext.tzLoad(tzParams,function(responseData){
				form.setValues(responseData);

				gridStore.tzStoreParams = tzStoreParams;
				gridStore.reload();				
			});
		});
		
		tab = contentPanel.add(cmp);     
		contentPanel.setActiveTab(tab);
		Ext.resumeLayouts(true);
		if (cmp.floating) {
			cmp.show();
		}
	},

	
	//保存	
	saveAudList: function(btn){
		//组件注册信息列表
		var grid = btn.findParentByType("grid");
		//组件注册信息数据
		var store = grid.getStore();
		
		//删除json字符串
		var removeJson = "";
		//删除记录
		var removeRecs = store.getRemovedRecords();
		
		
		for(var i=0;i<removeRecs.length;i++){
			if(removeJson == ""){
				removeJson = Ext.JSON.encode(removeRecs[i].data);
			}else{
				removeJson = removeJson + ','+Ext.JSON.encode(removeRecs[i].data);
			}
		};

        var comParams = "";
		if(removeJson != ""){
			comParams = '"delete":[' + removeJson + "]";
		}

		//提交参数
		var tzParams = '{"ComID":"TZ_AUD_COM","PageID":"TZ_AUD_LIST_STD","OperateType":"U","comParams":{'+comParams+'}}';
		//保存数据
        if(comParams!=""){
            Ext.tzSubmit(tzParams,function(){
                store.reload();
            },"保存成功",true,this);
        }

	},

	
	onAudDefnSave: function(btn){
		var form = this.getView().child("form").getForm();
		if (form.isValid()) {
			var tzParams = this.getAudDefnInfoParams();
			
			var comView = this.getView();
			var grid = comView.child("grid");
			var store = grid.getStore();
			
			Ext.tzSubmit(tzParams,function(responseData){
				if(comView.actType == "add"){
					comView.actType = "update";
					form.setValues(responseData);
				}
					
                if(store.isLoaded()){
                    store.reload();
                }
			},"保存成功",true,this);
		}
	},
	
	
	onAudDefnEnsure: function(btn){
		var me = this;
		var form = this.getView().child("form").getForm();
		if (form.isValid()) {
			var tzParams = this.getAudDefnInfoParams();
			var comView = this.getView();

			Ext.tzSubmit(tzParams,function(responseData){
				contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
			    contentPanel.child("audienceManagementDa").store.reload();
			    
			    me.onAudDefnClose(btn)
			},"保存成功",true,this);
		}
		
	},
	
	getAudDefnInfoParams: function(){
		var form = this.getView().child("form").getForm();
		var actType = this.getView().actType;
		
		//更新操作参数
		var comParams = "";
		//新增
		if(actType == "add"){
			comParams = '"add":['+Ext.JSON.encode(form.getValues())+']';
		}
		//修改json字符串
		var editJson = "";
		if(actType == "update"){
			editJson = '{"typeFlag":"FORM","data":'+Ext.JSON.encode(form.getValues())+'}';
		}
		
		var grid = this.getView().child("grid");
		var store = grid.getStore();
		//修改记录
		var mfRecs = store.getModifiedRecords(); 
		for(var i=0;i<mfRecs.length;i++){
			if(editJson == ""){
				editJson = '{"typeFlag":"GRID","data":'+Ext.JSON.encode(mfRecs[i].data)+'}';
			}else{
				editJson = editJson + ',{"typeFlag":"GRID","data":'+Ext.JSON.encode(mfRecs[i].data)+'}';
			}
		}
		
		if(editJson != ""){
			if(comParams == ""){
				comParams = '"update":[' + editJson + "]";
			}else{
				comParams = comParams + ',"update":[' + editJson + "]";
			}
		}
		//删除json字符串
		var removeJson = "";
		//删除记录
		var removeRecs = store.getRemovedRecords();
		for(var i=0;i<removeRecs.length;i++){
			if(removeJson == ""){
				removeJson = Ext.JSON.encode(removeRecs[i].data);
			}else{
				removeJson = removeJson + ','+Ext.JSON.encode(removeRecs[i].data);
			}
		}
		if(removeJson != ""){
			if(comParams == ""){
				comParams = '"delete":[' + removeJson + "]";
			}else{
				comParams = comParams + ',"delete":[' + removeJson + "]";
			}
		}
		//提交参数
		var tzParams = '{"ComID":"TZ_AUD_COM","PageID":"TZ_AUD_PANEL_STD","OperateType":"U","comParams":{'+comParams+'}}';
		return tzParams;
	},
	
	
	onAudDefnClose: function(btn){
		//关闭窗口
		this.getView().close();
	},

	//查询窗口	
	searchAudList: function(btn){
		Ext.tzShowCFGSearch({
		
			cfgSrhId: 'TZ_AUD_COM.TZ_AUD_LIST_STD.PS_TZ_AUDCX_VW',
			condition:
			{
				"TZ_JG_ID": Ext.tzOrgID
			},
			
			callback: function(seachCfg){
				var store = btn.findParentByType("grid").store;
				store.tzStoreParams = seachCfg;
				store.load();
			}
		});	
	},
	
	searchAudCyList: function(btn){
		var form = this.getView().child("form").getForm();
		//表单数据
		var formParams = form.getValues();
		
		var audChildID =formParams["audID"];

		Ext.tzShowCFGSearch({
			cfgSrhId: 'TZ_AUD_COM.TZ_AUD_PANEL_STD.PS_TZ_AUDCY_VW',
			condition:
			{
				"TZ_AUD_ID": audChildID
			},
		
			callback: function(seachCfg){
				var store = btn.findParentByType("grid").store;
				store.tzStoreParams = seachCfg;
				store.load();
			}
		});	
	},
	
	
	
	closeAudList: function(btn){
		//关闭
		var grid = btn.findParentByType("grid");
		grid.close();
	},
	
	//确定
	ensureSaveAudList:function(btn) {
		this.saveAudList(btn);
		this.closeAudList(btn);
	},
	
	
	//重新产生听众	
	freshAudMember: function(btn){
		var form = this.getView().child("form").getForm();
		//表单数据
		var formParams = form.getValues();
		var audSQL =formParams["audSQL"];
		var audID =formParams["audID"];

		if(audID == "NEXT" || audID == ""){
			Ext.Msg.alert("提示","请先执行保存操作");
			return;
		}
		
		if(audSQL==""){
			Ext.Msg.alert("提示","请先填写SQL信息后，再执行操作。");
		}else{
			var tzParams = '{"ComID":"TZ_AUD_COM","PageID":"TZ_AUD_PANEL_STD","OperateType":"tzOther","comParams":{"audSQL":"'+audSQL+'","audID":"'+audID+'"}}';
			var gridStore = this.getView().child("grid").getStore();	
	
			Ext.tzLoad(tzParams,function(responseData){
				gridStore.reload();
			});
		}
	}
	
	
})