Ext.define('KitchenSink.view.trainOrgTimeCardMg.trainOrgTimeCardController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.trainOrgTimeCardMg',
    onFormClose: function(){
        this.getView().close();
    },
    queryOrgOrderHis: function(btn) {
        var grid = btn.up('grid');
        //选中行
		   var selList = grid.getSelectionModel().getSelection();
		   //选中行长度
		   var checkLen = selList.length;
		   if(checkLen == 0){
				Ext.Msg.alert("提示","请选择一个机构。");
				return;
		   }else if(checkLen >1){
			   Ext.Msg.alert("提示","只能选择一个机构。");
			   return;
		   }

		   var orgId = selList[0].get("orgId");
		   /*if(orgId == "ADMIN"){
				Ext.Msg.alert("提示","平台管理机构为系统预留机构账号，不能修改");
				return;
		   }*/
		   this.queryOrgOrderHisByOrgId(orgId);
    },
	queryOrgOrderHisByOrgId: function(orgId){
		var contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
       		contentPanel.body.addCls('kitchensink-example');

            //更新机构信息类
		var cmp, className, ViewClass, clsProto;
		var themeName = Ext.themeName;
    	//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_PX_KSGL_COM"]["TZ_KS_ADD_HIS_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有访问或修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_KS_ADD_HIS_STD，请检查配置。');
			return;
		}

        //className = 'KitchenSink.view.trainOrgmgmt.orgInfoPanel';
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
			//操作标志
		cmp.actType = "update";
		
		cmp.on('afterrender',function(panel){
			//组件注册表单信息;
			//页面注册信息列表
			var grid = panel.child('grid');
			panel.orgId = orgId;
			var tzStoreParams = '{"cfgSrhId":"TZ_PX_KSGL_COM.TZ_KS_ADD_HIS_STD.PX_JG_KS_ORDER_VW","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+ orgId+'"}}';
			grid.store.tzStoreParams = tzStoreParams;
			grid.store.load();

		});

		tab = contentPanel.add(cmp);

		contentPanel.setActiveTab(tab);

		Ext.resumeLayouts(true);

		if (cmp.floating) {
			cmp.show();
		}
	},
    getOrgTimeCardAddInfoParams: function(btn){
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
        var tzParams = '{"ComID":"TZ_PX_KSGL_COM","PageID":"TZ_PX_KSGL_STD","OperateType":"U","comParams":{'+comParams+'}}';
        return tzParams;
    },
    onAddTimeCardFormSave: function(btn){
        //机构信息表单
		var me = this;
        var win = btn.findParentByType('window');
		var panel = win.findParentByType("trainOrgTimeCardMg");
        var form = win.child("form").getForm();
        if (form.isValid()) {
            //获取机构信息参数
            var tzParams = this.getOrgTimeCardAddInfoParams(btn);
            Ext.tzSubmit(tzParams,function(responseData){
                //关闭窗口
                win.close();
				//刷新父窗口信息
				var gridStore = panel.child('grid').store;
				gridStore.reload();
            },"",true,this);
        }	
    },
	queryOrgList: function(btn){     //searchComList为各自搜索按钮的handler event;
        Ext.tzShowCFGSearch({
           cfgSrhId: 'TZ_PX_KSGL_COM.TZ_PX_KSGL_STD.TZ_PX_JG_VW',
           condition:
            {
                //"TZ_JG_ID": Ext.tzOrgID           //设置搜索字段的默认值，没有可以不设置condition;
            },
            callback: function(seachCfg){
                var store = btn.findParentByType("grid").store;
                store.tzStoreParams = seachCfg;
                store.load();
            }
        });
    },
    addTimeCardOrder:function(btn){
        //选中行
        var selList = btn.findParentByType("grid").getSelectionModel().getSelection();
        //选中行长度
        var checkLen = selList.length;
        if(checkLen == 0){
            Ext.Msg.alert("提示","请选择要新增订购的培训机构");
            return;
        }else if(checkLen >1){
            Ext.Msg.alert("提示","只能选择一个要新增订购的培训机构");
            return;
        }

        var win = this.lookupReference('trainOrgTimeCardAddWindow');

        if (!win) {
            Ext.syncRequire("KitchenSink.view.trainOrgTimeCardMg.trainOrgTimeCardAddWindow");
            ViewClass = Ext.ClassManager.get("KitchenSink.view.trainOrgTimeCardMg.trainOrgTimeCardAddWindow");
            //新建类
            win = new ViewClass();
            this.getView().add(win);
        }

        //操作类型设置为更新
        win.actType = "update";
        //机构编号
        var orgId = selList[0].get("orgId");
        //机构名称
        var orgName = selList[0].get("orgName");
        //拥有的课时卡
        var orgTimeCardHave = selList[0].get("orgTimeCardHave");
        //已分配的课时卡
        var orgTimeCardAssign = selList[0].get("orgTimeCardAssign");

        var form = win.child("form").getForm();
        var grid = win.child("grid");

        form.setValues(
            [
                {id:'orgId', value:orgId},
                {id:'orgName', value:orgName},
                {id:'orgTimeCardHave', value:orgTimeCardHave},
                {id:'orgTimeCardAssign', value:orgTimeCardAssign}
            ]
        );
        win.show();
    },
	/*查看培训机构订购记录-按机构*/
    queryTimeCardAddHis: function(btn){     //searchComList为各自搜索按钮的handler event;
		var panel = btn.findParentByType("trainOrgTimeCardAddList");
		//console.log(panel);
		//console.log(panel.orgId);
        Ext.tzShowCFGSearch({
           cfgSrhId: 'TZ_PX_KSGL_COM.TZ_KS_ADD_HIS_STD.PX_JG_KS_ORDER_VW',
           condition:
            {
                "TZ_JG_ID": panel.orgId   //设置搜索字段的默认值，没有可以不设置condition;
            },
            callback: function(seachCfg){
                var store = btn.findParentByType("grid").store;
                store.tzStoreParams = seachCfg;
                store.load();
            }
        });
    },
	/*查看培训机构订购记录-查询所有*/
    queryTimeCardAddAll: function(btn){     //searchComList为各自搜索按钮的handler event;
        Ext.tzShowCFGSearch({
           cfgSrhId: 'TZ_KS_ADD_SRCH_COM.TZ_KS_ADD_SRCH_STD.PX_JG_KS_ORDER_VW',
           condition:
            {
                //"TZ_JG_ID": panel.orgId   //设置搜索字段的默认值，没有可以不设置condition;
            },
            callback: function(seachCfg){
                var store = btn.findParentByType("grid").store;
                store.tzStoreParams = seachCfg;
                store.load();
            }
        });
    },
    //关闭
    onPanelClose:function(){
        //alert("onPanelClose");
        this.getView().close();
    }
});