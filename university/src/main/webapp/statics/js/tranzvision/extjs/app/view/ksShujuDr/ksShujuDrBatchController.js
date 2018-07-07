Ext.define('KitchenSink.view.ksShujuDr.ksShujuDrBatchController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.ksShujuDrBatchController',
    //可配置搜索
	cfgSearchBatch: function(btn){     //searchComList为各自搜索按钮的handler event;
		Ext.tzShowCFGSearch({
			cfgSrhId: 'TZ_DRBATLIST_COM.TZ_DRBATLIST_STD.PS_TZ_DRBATCH_VW',
			condition:
			{
				"TZ_JG_ID": Ext.tzOrgID           //设置搜索字段的默认值，没有可以不设置condition;
			},
			callback: function(seachCfg){
				var store = btn.findParentByType("grid").store;
				store.tzStoreParams = seachCfg;
				store.load();
			}
		});
	},
    publish:function(view,rowIndex){
        
		var store = view.findParentByType("grid").store;
        var selRec = store.getAt(rowIndex);
		var batchId = selRec.get("batchId");
		var tplId = selRec.get("tplId");
		var jgId = selRec.get("jgId");
		
		var tzParams = '{"ComID":"TZ_DRBATLIST_COM","PageID":"TZ_DRBATLIST_STD","OperateType":"publishBatch","comParams":{"batchId":"'+batchId+'","tplId":"'+tplId+'","jgId":"'+jgId+'"}}';
		Ext.tzLoad(tzParams,function(respData){
			Ext.Msg.alert(Ext.tzGetResourse("TZ_DRBATLIST_COM.TZ_DRBATINFO_STD.prompt","提示"),"发布成功");
		})
    },
    cancel:function(view,rowIndex){
    	var store = view.findParentByType("grid").store;
        var selRec = store.getAt(rowIndex);
		var batchId = selRec.get("batchId");
		var tplId = selRec.get("tplId");
		var jgId = selRec.get("jgId");
		
		var tzParams = '{"ComID":"TZ_DRBATLIST_COM","PageID":"TZ_DRBATLIST_STD","OperateType":"cancelBatch","comParams":{"batchId":"'+batchId+'","tplId":"'+tplId+'","jgId":"'+jgId+'"}}';
		Ext.tzLoad(tzParams,function(respData){
			Ext.Msg.alert(Ext.tzGetResourse("TZ_DRBATLIST_COM.TZ_DRBATINFO_STD.prompt","提示"),"撤销成功");
		})
    },
	viewSelBatchInfo:function(grid, rowIndex, colIndex){
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_DRBATLIST_COM"]["TZ_DRBATINFO_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_DRBATLIST_COM.TZ_DRBATINFO_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_DRBATINFO_STD.nmyqx","您没有权限"));
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_DRBATLIST_COM.TZ_DRBATINFO_STD.prompt","提示"), Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_DRBATINFO_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
			return;
		}
		var contentPanel, cmp, ViewClass, clsProto;

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

		var record = grid.store.getAt(rowIndex);
		var batchId = record.data.batchId;
		var tplId = record.data.tplId;
		var jgId = record.data.jgId;
		
		var refClassId = "";
		var viewNoticeType = "";
		if(tplId=="TZ_INTER_QUAL" || tplId=="TZ_MSRC_DR"){
			refClassId = "previewmstz";
			viewNoticeType = "预览面试通知";
		}else if(tplId=="TZ_MSJG_DR"){
			refClassId = "previewmsjg";
			viewNoticeType = "预览面试结果";
		}else if(tplId=="TZ_WRITTEN_RES" || tplId=="TZ_LQJG_DR"){
			refClassId = "previewlqjg";
			viewNoticeType = "预览录取结果";
		}else if(tplId=="TZ_XMSJG_DR"){
			refClassId = "previewxmsjg";
			viewNoticeType = "预览面试结果";
		}
		
		var tzParams = '{"ComID":"TZ_DRBATLIST_COM","PageID":"TZ_DRBATINFO_STD","OperateType":"tzLoadGridColumns","comParams":{"batchId":"'+batchId+'","tplId":"'+tplId+'","jgId":"'+jgId+'"}}';
		Ext.tzLoad(tzParams,function(respData){
			
			var gridColumn = respData;
			
			var dynamicFields = [];
			for(var i=0;i<respData.length;i++){
				var name = respData[i].dataIndex;
				dynamicFields.push({name:name,type:'string'});
			}
			gridColumn.unshift({xtype:'rownumberer',minWidth:'20',maxWidth:'80'});
			gridColumn.push({dataIndex:'TZ_FLAG',width:'150',text:'发布状态'});

			gridColumn.push({dataIndex:'previewNotice',width:'120',text:viewNoticeType,
				renderer:function(value, cellmeta, record, rowIndex ,colIndex ,store, view){
					var previewUrl = "dispatcher?classid="+refClassId+"&TZ_MSH_ID=" + record.get("TZ_MSH_ID");
					return '<a href="'+previewUrl+'" target="_blank">'+viewNoticeType+'</a>';
				}
			});
			
			var model = new Ext.data.Model({
				fields:dynamicFields
			});
			
			cmp = new ViewClass(gridColumn);
		
			cmp.on('afterrender',function(panel){
				var form = panel.child('form').getForm();
				var panelGrid = panel.child('grid');
				
				panelGrid.store.setModel(model);
				
				var tzParams = '{"ComID":"TZ_DRBATLIST_COM","PageID":"TZ_DRBATINFO_STD","OperateType":"QF","comParams":{"batchId":"'+batchId+'","tplId":"'+tplId+'","jgId":"'+jgId+'"}}';
				Ext.tzLoad(tzParams,function(respData){
					var formData = respData.formData;
					form.setValues(formData);
					
					var tzStoreParams = '{"batchId":"'+batchId+'","tplId":"'+tplId+'","jgId":"'+jgId+'"}';
					
					panelGrid.store.tzStoreParams = tzStoreParams;
					panelGrid.store.load();
				});
			});
			
			tab = contentPanel.add(cmp);

			contentPanel.setActiveTab(tab);

			Ext.resumeLayouts(true);

			if (cmp.floating) {
				cmp.show();
			}
		});
		
		
	},
	viewBatchInfo:function(btn){
		var selList = this.getView().getSelectionModel().getSelection();
		//选中行长度
		var checkLen = selList.length;
		if(checkLen == 0){
			Ext.Msg.alert("提示","请选择一条要查看的记录");
			return;
		}else if(checkLen >1){
			Ext.Msg.alert("提示","只能选择一条要查看的记录");
			return;
		}
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_DRBATLIST_COM"]["TZ_DRBATINFO_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_DRBATLIST_COM.TZ_DRBATINFO_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_DRBATINFO_STD.nmyqx","您没有权限"));
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_DRBATLIST_COM.TZ_DRBATINFO_STD.prompt","提示"), Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_DRBATINFO_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
			return;
		}
		var contentPanel, cmp, ViewClass, clsProto;

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
		
		var batchId = selList[0].get("batchId");
		var tplId = selList[0].get("tplId");
		var jgId = selList[0].get("jgId");
		
		var tzParams = '{"ComID":"TZ_DRBATLIST_COM","PageID":"TZ_DRBATINFO_STD","OperateType":"tzLoadGridColumns","comParams":{"batchId":"'+batchId+'","tplId":"'+tplId+'","jgId":"'+jgId+'"}}';
		Ext.tzLoad(tzParams,function(respData){
			
			var gridColumn = respData;
			
			var dynamicFields = [];
			for(var i=0;i<respData.length;i++){
				var name = respData[i].dataIndex;
				dynamicFields.push({name:name,type:'string'});
			}
			gridColumn.unshift({xtype:'rownumberer',minWidth:'20',maxWidth:'80'});
			gridColumn.push({dataIndex:'TZ_FLAG',width:'150',text:'发布状态'});
			
			var model = new Ext.data.Model({
				fields:dynamicFields
			});
			
			cmp = new ViewClass(gridColumn);
		
			cmp.on('afterrender',function(panel){
				var form = panel.child('form').getForm();
				var panelGrid = panel.child('grid');
				
				panelGrid.store.setModel(model);
				
				var tzParams = '{"ComID":"TZ_DRBATLIST_COM","PageID":"TZ_DRBATINFO_STD","OperateType":"QF","comParams":{"batchId":"'+batchId+'","tplId":"'+tplId+'","jgId":"'+jgId+'"}}';
				Ext.tzLoad(tzParams,function(respData){
					var formData = respData.formData;
					form.setValues(formData);
					
					var tzStoreParams = '{"batchId":"'+batchId+'","tplId":"'+tplId+'","jgId":"'+jgId+'"}';
					
					panelGrid.store.tzStoreParams = tzStoreParams;
					panelGrid.store.load();
				});
			});
			
			tab = contentPanel.add(cmp);

			contentPanel.setActiveTab(tab);

			Ext.resumeLayouts(true);

			if (cmp.floating) {
				cmp.show();
			}
		});
	}
});
