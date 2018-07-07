Ext.define('KitchenSink.view.sendEmailAndSMS.emlTempletDef.emlTempletController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.emlTempletController', 
	requires: [
       'KitchenSink.view.sendEmailAndSMS.emlTempletDef.emlTempletInfo'
    ],
    addEmlTemplet: function() {
			//是否有访问权限
			var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_EML_TMPL_MG_COM"]["TZ_EML_TMPL_STD"];
			if( pageResSet == "" || pageResSet == undefined){
				Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
			}
			//该功能对应的JS类
			var className = pageResSet["jsClassName"];
			if(className == "" || className == undefined){
				Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_EML_TMPL_STD，请检查配置。');
				return;
			}
			
			var contentPanel,cmp, className, ViewClass, clsProto;
			var themeName = Ext.themeName;
			
            contentPanel = Ext.getCmp('tranzvision-framework-content-panel');			
       		contentPanel.body.addCls('kitchensink-example');

            //className = 'KitchenSink.view.sendEmailAndSMS.emailServer.emailServerInfo';
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
			
			cmp.actType = "add";
			
            tab = contentPanel.add(cmp);     
			
			tab.on(Ext.tzTabOn(tab,this.getView(),cmp));
			
            contentPanel.setActiveTab(tab);
	
            Ext.resumeLayouts(true);

            if (cmp.floating) {
                cmp.show();
            }
    },
	editSelEmlTemplet: function() {
        //选中行
        var selList = this.getView().getSelectionModel().getSelection();
        //选中行长度
        var checkLen = selList.length;
        if(checkLen == 0){
            Ext.Msg.alert("提示","请选择一条要修改的记录");
            return;
        }else if(checkLen >1){
            Ext.Msg.alert("提示","只能选择一条要修改的记录");
            return;
        }
        //邮件模版编号
        var emltempid = selList[0].get("emltempid");
		var emltemporg = selList[0].get("emltemporg");
		var restempid = selList[0].get("restempid");
		var keyid = selList[0].get("keyid");
        //显示邮箱服务器编辑页面
        this.editEmlTmplInfoByKey(emltempid,emltemporg,restempid,keyid);
    },
    editEmlTemplet: function(view, rowIndex) {
		 var store = view.findParentByType("grid").store;
		 var selRec = store.getAt(rowIndex);
		 //组件ID
	   	 var emltempid = selRec.get("emltempid");
		 var emltemporg = selRec.get("emltemporg");
		 var restempid = selRec.get("restempid");
		 var keyid = selRec.get("keyid");
	     //显示元模板信息编辑页面
	     this.editEmlTmplInfoByKey(emltempid,emltemporg,restempid,keyid);	 
    },
	editEmlTmplInfoByKey: function(emltempid,emltemporg,restempid,keyid) {
	
			//是否有访问权限
			var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_EML_TMPL_MG_COM"]["TZ_EML_TMPL_STD"];
			if( pageResSet == "" || pageResSet == undefined){
				Ext.MessageBox.alert('提示', '您没有修改数据的权限');
				return;
			}
			//该功能对应的JS类
			var className = pageResSet["jsClassName"];
			if(className == "" || className == undefined){
				Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_EML_TMPL_STD，请检查配置。');
				return;
			}
			
			var contentPanel,cmp, className, ViewClass, clsProto;
			var themeName = Ext.themeName;
			
            contentPanel = Ext.getCmp('tranzvision-framework-content-panel');			
       		contentPanel.body.addCls('kitchensink-example');

         //   className = 'KitchenSink.view.sendEmailAndSMS.emailServer.emailServerInfo';
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
				//邮箱信息配置数据表单
				//var form = this.lookupReference('emailServerForm').getForm();
				
				//组件注册表单信息;
				var form = panel.child('form').getForm();
				form.findField("emltempid").setReadOnly(true);
				form.findField("emltemporg").setReadOnly(true);
				form.findField("keyid").setReadOnly(true);
				form.findField("emltempid").addCls("lanage_1");
				form.findField("emltemporg").addCls("lanage_1");
				form.findField("keyid").addCls("lanage_1");
				
				var tzParamsJson = {
						"ComID":"TZ_EML_TMPL_MG_COM",
						"PageID":"TZ_EML_TMPL_STD",
						"OperateType":"QF",
						"comParams":{
							"emltempid": emltempid,
							"emltemporg": emltemporg,
							"keyid":keyid
						}
				}
				
				//参数
				var tzParams = Ext.JSON.encode(tzParamsJson);
				//var tzParams = '{"ComID":"TZ_EML_TMPL_MG_COM","PageID":"TZ_EML_TMPL_STD","OperateType":"QF","comParams":{"emltempid":"'+emltempid+'","emltemporg":"'+emltemporg+'"}}';
				
				Ext.tzLoad(tzParams,function(responseData){	
					form.setValues(responseData);
					//信息项数据
					var	emlTmplItemGrid = panel.down('grid[name=emlTmplItemGrid]');
					var tzStoreParamsItemJson = {
							"restempid": restempid	
					}
					var tzStoreParamsItem = Ext.JSON.encode(tzStoreParamsItemJson);
					//var tzStoreParamsItem = '{"restempid":"'+restempid+'"}';
					emlTmplItemGrid.store.tzStoreParams = tzStoreParamsItem;
					emlTmplItemGrid.store.load();
					panel.commitChanges(panel);
				});
				
			});
			
            tab = contentPanel.add(cmp);    

			tab.on(Ext.tzTabOn(tab,this.getView(),cmp));
			
            contentPanel.setActiveTab(tab);
	
            Ext.resumeLayouts(true);

            if (cmp.floating) {
                cmp.show();
            }	
    },
	deleteSelEmlTemplet: function(){
	   //选中行
	   var selList = this.getView().getSelectionModel().getSelection();
	   //选中行长度
	   var checkLen = selList.length;
	   if(checkLen == 0){
			Ext.Msg.alert("提示","请选择要删除的记录");   
			return;
	   }else{
			Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
				if(btnId == 'yes'){					   
				   var emlTempletStore = this.getView().store;
                    emlTempletStore.remove(selList);
				}												  
			},this);   
	   }
	},
    deleteEmlTemplet: function(view, rowIndex){
        Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
            if(btnId == 'yes'){
                var store = view.findParentByType("grid").store;
                store.removeAt(rowIndex);
            }
        },this);
    },
	searchComList: function(btn){     //searchComList为各自搜索按钮的handler event;

		Ext.tzShowCFGSearch({            

			cfgSrhId: 'TZ_EML_TMPL_MG_COM.TZ_EML_TMPL_MG_STD.TZ_EMALTMPL_VW',    
			condition:
            {
                "TZ_JG_ID_1": Ext.tzOrgID          //设置搜索字段的默认值，没有可以不设置condition;
            },			
			callback: function(seachCfg){
				var store = btn.findParentByType("grid").store;
				store.tzStoreParams = seachCfg;
				store.load();
			}
		});    
    },
	saveEmlTemplet: function(btn){
        
        var grid = btn.findParentByType("grid");
        
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
        }
        if(removeJson != ""){
            comParams = '"delete":[' + removeJson + "]";
        }else{
            return;
        }
        //提交参数
        var tzParams = '{"ComID":"TZ_EML_TMPL_MG_COM","PageID":"TZ_EML_TMPL_MG_STD","OperateType":"U","comParams":{'+comParams+'}}';
        //保存数据
        Ext.tzSubmit(tzParams,function(){
            store.reload();
        },"",true,this);
    },
	ensureEmlTemplet: function(btn){
        
        var grid = btn.findParentByType("grid");
        
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
        }
        if(removeJson != ""){
            comParams = '"delete":[' + removeJson + "]";
			//提交参数
			var tzParams = '{"ComID":"TZ_EML_TMPL_MG_COM","PageID":"TZ_EML_TMPL_MG_STD","OperateType":"U","comParams":{'+comParams+'}}';
			//保存数据
			Ext.tzSubmit(tzParams,function(){
				grid.close();
			},"",true,this);
        }else{
            grid.close();
        }
        
    },
	closeEmlTemplet: function(btn){
        
        var grid = btn.findParentByType("grid");
        grid.close();
        
    },
	resetAllTemplet: function(btn){
		
	   	Ext.MessageBox.confirm('确认', '“初始化设置默认模版”操作将会清除用户目前设置的所有模版数据，是否继续？', function(btnId){
			if(btnId == 'yes'){					   
				//提交参数
				var tzParams = '{"ComID":"TZ_EML_TMPL_MG_COM","PageID":"TZ_EML_TMPL_MG_STD","OperateType":"initializ","comParams":{}}';
				Ext.tzSubmit(tzParams,function(){
					//初始化成功
					var grid = btn.findParentByType("grid");
					var store = grid.getStore();
					store.reload();
					
				},"",true,this);
			}												  
		},this);   	   	
	}
});