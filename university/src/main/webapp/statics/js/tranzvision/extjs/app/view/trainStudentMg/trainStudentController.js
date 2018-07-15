Ext.define('KitchenSink.view.trainStudentMg.trainStudentController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.trainStudentInfoMg', 
    searchComList: function(btn){
		Ext.tzShowCFGSearch({
			cfgSrhId: 'PX_STU_COURSE_COM.PX_STU_COURSE_STD.PX_STU_COURSE_T',
			callback: function(seachCfg){
				var store = btn.findParentByType("grid").store;
				store.tzStoreParams = seachCfg;
				store.load();
			}
		});	
	},
	/*导入学员*/
	//编辑项目（列表）
	importStudentInfo: function(btn){
		var panelList = btn.findParentByType("trainStudentList");
		var orgid = panelList.orgId;
		
		var stuOrgInfoForm = panelList.child('form').getForm();	
		
		var orgAuditStatus = stuOrgInfoForm.findField("orgAuditStatus").getValue();
    	if (orgAuditStatus != "B") {
			Ext.MessageBox.alert('提示','当前机构未审核通过，不能导入学员，请联系系统管理员。');
			return;
		}
		
	},
    addStudentInfo: function(btn){
		
		var panelList = btn.findParentByType("trainStudentList");
		var orgid = panelList.orgId;
		
		var stuOrgInfoForm = panelList.child('form').getForm();	
		
		var orgAuditStatus = stuOrgInfoForm.findField("orgAuditStatus").getValue();
    	if (orgAuditStatus != "B") {
			Ext.MessageBox.alert('提示','当前机构未审核通过，不能添加学员，请联系系统管理员。');
			return;
		}
     	//是否有访问权限
    	var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_PX_STU_COM"]["TZ_PX_STU_INFO"];
		if (pageResSet == "" || pageResSet == undefined) {
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		// 该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if (className == "" || className == undefined) {
			Ext.MessageBox.alert('提示',
					'未找到该功能页面对应的JS类，页面ID为：TZ_PX_STU_INFO，请检查配置。');
			return;
		}

		var contentPanel, cmp, className, ViewClass, clsProto;
		var themeName = Ext.themeName;

		contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
		contentPanel.body.addCls('kitchensink-example');

		if (!Ext.ClassManager.isCreated(className)) {
			Ext.syncRequire(className);
		}

		ViewClass = Ext.ClassManager.get(className);

		clsProto = ViewClass.prototype;

		if (clsProto.themes) {
			clsProto.themeInfo = clsProto.themes[themeName];

			if (themeName === 'gray') {
				clsProto.themeInfo = Ext.applyIf(clsProto.themeInfo || {},
						clsProto.themes.classic);
			} else if (themeName !== 'neptune' && themeName !== 'classic') {
				if (themeName === 'crisp-touch') {
					clsProto.themeInfo = Ext.applyIf(clsProto.themeInfo || {},
							clsProto.themes['neptune-touch']);
				}
				clsProto.themeInfo = Ext.applyIf(clsProto.themeInfo || {},
						clsProto.themes.neptune);
			}
			// <debug warn>
			// Sometimes we forget to include allowances for other themes, so
			// issue a warning as a reminder.
			if (!clsProto.themeInfo) {
				Ext.log
						.warn('Example \''
								+ className
								+ '\' lacks a theme specification for the selected theme: \''
								+ themeName + '\'. Is this intentional?');
			}
			// </debug>
		}

		cmp = new ViewClass();
		// 操作类型设置为更新
		cmp.actType = "add";

		cmp.on('afterrender', function(panel) {
			// 组件注册表单信息;
			var form = panel.child('form').getForm();	
			form.findField("orgid").setValue(orgid);
		});
		
        tab = contentPanel.add(cmp);     
		
        contentPanel.setActiveTab(tab);

        Ext.resumeLayouts(true);

        if (cmp.floating) {
            cmp.show();
        }
    },
    //编辑项目（列表）
    editStudentInfo: function(btn){
		
		var panelList = btn.findParentByType("trainStudentList");
		var stuOrgInfoForm = panelList.child('form').getForm();	
		
		var orgAuditStatus = stuOrgInfoForm.findField("orgAuditStatus").getValue();
    	if (orgAuditStatus != "B") {
			Ext.MessageBox.alert('提示','当前机构未审核通过，不能编辑学员，请联系系统管理员。');
			return;
		}

    	//console.log(view);
    	var grid = btn.findParentByType("grid");

		var selList = grid.getSelectionModel().getSelection();
	   //选中行长度
	    var checkLen = selList.length;
	    if(checkLen == 0){
			Ext.Msg.alert("提示","请选择一条要修改的记录");
			return;
	    }else if(checkLen >1){
		   Ext.Msg.alert("提示","只能选择一条要修改的记录");
		   return;
	    }

		var oprid = selList[0].get("oprid");
		var orgid = selList[0].get("orgid");
		
     	//显示皮肤设置编辑页面
     	this.editStudentInfoByOprid(oprid,orgid);
    },
	editStudentInfoOne: function(view, rowIndex){
    	//console.log(view);
		var panelList = view.findParentByType("trainStudentList");
		var stuOrgInfoForm = panelList.child('form').getForm();	
		
		var orgAuditStatus = stuOrgInfoForm.findField("orgAuditStatus").getValue();
    	if (orgAuditStatus != "B") {
			Ext.MessageBox.alert('提示','当前机构未审核通过，不能编辑学员，请联系系统管理员。');
			return;
		}
		
    	var store = view.findParentByType("grid").store;
    	console.log(view.findParentByType("grid"));
	 	var selRec = store.getAt(rowIndex);
		var oprid = selRec.get("oprid");
		var orgid = selRec.get("orgid");
     	//显示皮肤设置编辑页面
     	this.editStudentInfoByOprid(oprid,orgid);
    },
    editStudentInfoByOprid:function(oprid,orgid){
    	//是否有访问权限
    	var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_PX_STU_COM"]["TZ_PX_STU_INFO"];
		if (pageResSet == "" || pageResSet == undefined) {
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		// 该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if (className == "" || className == undefined) {
			Ext.MessageBox.alert('提示',
					'未找到该功能页面对应的JS类，页面ID为：TZ_PX_STU_INFO，请检查配置。');
			return;
		}

		var contentPanel, cmp, className, ViewClass, clsProto;
		var themeName = Ext.themeName;

		contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
		contentPanel.body.addCls('kitchensink-example');

		if (!Ext.ClassManager.isCreated(className)) {
			Ext.syncRequire(className);
		}

		ViewClass = Ext.ClassManager.get(className);

		clsProto = ViewClass.prototype;

		if (clsProto.themes) {
			clsProto.themeInfo = clsProto.themes[themeName];

			if (themeName === 'gray') {
				clsProto.themeInfo = Ext.applyIf(clsProto.themeInfo || {},
						clsProto.themes.classic);
			} else if (themeName !== 'neptune' && themeName !== 'classic') {
				if (themeName === 'crisp-touch') {
					clsProto.themeInfo = Ext.applyIf(clsProto.themeInfo || {},
							clsProto.themes['neptune-touch']);
				}
				clsProto.themeInfo = Ext.applyIf(clsProto.themeInfo || {},
						clsProto.themes.neptune);
			}
			// <debug warn>
			// Sometimes we forget to include allowances for other themes, so
			// issue a warning as a reminder.
			if (!clsProto.themeInfo) {
				Ext.log
						.warn('Example \''
								+ className
								+ '\' lacks a theme specification for the selected theme: \''
								+ themeName + '\'. Is this intentional?');
			}
			// </debug>
		}

		cmp = new ViewClass();
		// 操作类型设置为更新
		cmp.actType = "update";

		cmp.on('afterrender', function(panel) {
			// 组件注册表单信息;
			var tzParams = '{"ComID":"TZ_PX_STU_COM","PageID":"TZ_PX_STU_INFO","OperateType":"QF","comParams":{"OPRID":"'+oprid+'","ORGID":"'+orgid+'"}}';
			//加载数据
			//var msgForm = this.lookupReference('studentMgForm');
			var form = panel.child('form').getForm();
			Ext.tzLoad(tzParams,function(responseData){
				//组件注册信息数据
				var formData = responseData.formData;				
				form.setValues(formData);
				if(panel.down('hiddenfield[name=titleImageUrl]').getValue()){
					panel.down('image[name=titileImage]').setSrc(TzUniversityContextPath + panel.down('hiddenfield[name=titleImageUrl]').getValue());	
				}else{
					panel.down('image[name=titileImage]').setSrc(TzUniversityContextPath + "/statics/images/tranzvision/mrtx02.jpg");
				}
				form.findField("phone").setReadOnly(true);
				form.findField("phone").setFieldStyle('background:#F4F4F4');
				form.findField("name").setReadOnly(true);
				form.findField("name").setFieldStyle('background:#F4F4F4');
			});
			
		});
		
        tab = contentPanel.add(cmp);     
		
        contentPanel.setActiveTab(tab);

        Ext.resumeLayouts(true);

        if (cmp.floating) {
            cmp.show();
        }
    },
	getStudentInfoParams: function(){
        //项目表单
        var form = this.getView().child("form").getForm();
        //表单数据
        var formParams = form.getValues();

        //组件信息标志
        var actType = this.getView().actType;
        //更新操作参数
        var comParams = "";
        //新增
        if(actType == "add"){
            comParams = '"add":['+Ext.JSON.encode(formParams)+']';
        }
        //修改json字符串
        var editJson = "";
        if(actType == "update"){
            comParams = '"update":['+Ext.JSON.encode(formParams)+']';
        }
        //提交参数
        var tzParams = '{"ComID":"TZ_PX_STU_COM","PageID":"TZ_PX_STU_INFO","OperateType":"U","comParams":{'+comParams+'}}';
        console.log(tzParams);
        return tzParams;
    },
	onFormSave: function(btn){
        var form = this.getView().child("form").getForm();
        if (form.isValid()) {
            var tzParams = this.getStudentInfoParams();
            var comView = this.getView();
            Ext.tzSubmit(tzParams,function(responseData){
				console.log(responseData);
            	form.setValues(responseData);
            	form.findField("phone").setReadOnly(true);
    			form.findField("phone").addCls("lanage_1");
				form.findField("name").setReadOnly(true);
    			form.findField("name").addCls("lanage_1");
                comView.actType = "update";
            },"",true,this);
        }
    },
	onFormEnsure:function(btn){
		//var panel = this.lookupReference('trainStudentList');
    	var form = this.getView().child("form").getForm();
        if (form.isValid()) {
            //获得项目表单信息
            var tzParams = this.getStudentInfoParams();
            var comView = this.getView();
            Ext.tzSubmit(tzParams,function(responseData){
				form.findField("phone").setReadOnly(true);
    			form.findField("phone").addCls("lanage_1");
				form.findField("name").setReadOnly(true);
    			form.findField("name").addCls("lanage_1");
                comView.actType = "update";
				contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
				contentPanel.child("trainStudentList").down('grid[name=studentGrid]').store.reload();
				//panel.down('grid[name=studentGrid]').store.reload();
                comView.close();
            },"",true,this);
        }
    
    },

    onFormClose:function(btn){
        //关闭窗口
        var comView = this.getView();
        comView.close();
    },
	addStuTimeCard:function(btn){
		
		var panelList = btn.findParentByType("trainStudentList");
		var stuOrgInfoForm = panelList.child('form').getForm();	
		
		var orgAuditStatus = stuOrgInfoForm.findField("orgAuditStatus").getValue();
    	if (orgAuditStatus != "B") {
			Ext.MessageBox.alert('提示','当前机构未审核通过，不能给学员分配课时卡，请联系系统管理员。');
			return;
		}
		
        //选中行
        var selList = btn.findParentByType("grid").getSelectionModel().getSelection();
        //选中行长度
        var checkLen = selList.length;
        if(checkLen == 0){
            Ext.Msg.alert("提示","请选择要分配课时卡的学员");
            return;
        }else if(checkLen >1){
            Ext.Msg.alert("提示","只能选择一个要分配课时卡的学员");
            return;
        }

        var win = this.lookupReference('trainStuTimeCardAddWindow');

        if (!win) {
            Ext.syncRequire("KitchenSink.view.trainStudentMg.trainStuTimeCardAddWindow");
            ViewClass = Ext.ClassManager.get("KitchenSink.view.trainStudentMg.trainStuTimeCardAddWindow");
            //新建类
            win = new ViewClass();
            this.getView().add(win);
        }

        //操作类型设置为更新
        win.actType = "update";
        //机构编号
        var orgid = selList[0].get("orgid");
		var oprid = selList[0].get("oprid");
        //学员姓名
        var stuName = selList[0].get("stuName");
        //拥有的课时卡
        var stuRemaindTimeCard = selList[0].get("stuRemaindTimeCard");
        //已使用的课时卡
        var stuTimeCardUsed = selList[0].get("stuUsedTimeCard");

        var form = win.child("form").getForm();
        var grid = win.child("grid");

        form.setValues(
            [
                {id:'orgid', value:orgid},
				{id:'oprid', value:oprid},
                {id:'stuName', value:stuName},
                {id:'stuTimeCardRemaind', value:stuRemaindTimeCard},
                {id:'stuTimeCardUsed', value:stuTimeCardUsed}
            ]
        );
        win.show();
    },
	getStuTimeCardAddInfoParams: function(btn){
        //机构信息表单
		var me = this;
        var win = btn.findParentByType('window');
        var form = win.child("form").getForm();
        //机构信息标志
        var actType = win.actType;
        //更新操作参数
        var comParams = "";
        //新增
        if(actType == "add"){
            comParams = '"add":[{"typeFlag":"ADDTIMECARD","data":'+Ext.JSON.encode(form.getValues())+'}]';
        }
        //修改json字符串
        var editJson = "";
        if(actType == "update"){
            editJson = '{"typeFlag":"ADDTIMECARD","data":'+Ext.JSON.encode(form.getValues())+'}';
        }

        if(editJson != ""){
            if(comParams == ""){
                comParams = '"update":[' + editJson + "]";
            }else{
                comParams = comParams + ',"update":[' + editJson + "]";
            }
        }

        //提交参数
        var tzParams = '{"ComID":"TZ_PX_STU_COM","PageID":"TZ_PX_STU_STD","OperateType":"U","comParams":{'+comParams+'}}';
        return tzParams;
    },
    onAddTimeCardFormSave: function(btn){
        //机构信息表单
		var me = this;
        var win = btn.findParentByType('window');
		var panel = win.findParentByType("trainStudentList");
        var form = win.child("form").getForm();
        if (form.isValid()) {
            //获取机构信息参数
            var tzParams = this.getStuTimeCardAddInfoParams(btn);
            Ext.tzSubmit(tzParams,function(responseData){
                //关闭窗口
                win.close();
				//刷新父窗口信息
				var gridStore = panel.child('grid').store;
				gridStore.reload();
            },"",true,this);
        }	
    },
});