Ext.define('KitchenSink.view.trainScheduleMg.stuScheduleController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.stuScheduleController',
    
    /*按条件查询项目列表，seachCfg在可配置中配置*/
        selectForm:function(btn){
        Ext.tzShowCFGSearch({
            cfgSrhId: 'PX_SCHEDULE_COM.PX_STUSCH_STD.PX_STU_COURSE_V', 
            callback: function(seachCfg){
                var store = btn.findParentByType("grid").store;
                store.tzStoreParams = seachCfg;
                store.load();
            }
        });
    },   

    //项目列表页确定按钮
    onGridEnsure:function(btn){
        this.onGridSave(btn);
        //关闭窗口
        var comView = this.getView();
        comView.close();
    },
    onGridClose: function(btn){
      //项目信息列表
    	var grid = btn.findParentByType("grid");
    	grid.close();
    },

    //项目详情页保存按钮
    onFormSave: function(btn){
        var form = this.getView().child("form").getForm();
        if (form.isValid()) {
            var tzParams = this.getResSetInfoParams();
            var comView = this.getView();
            Ext.tzSubmit(tzParams,function(responseData){
            	// formData = responseData.formData;
            	//form.setValues(formData);
            	///.findField("teacherId").setReadOnly(true);
    			//form.findField("teacherId").addCls("lanage_1");
                comView.actType = "update";
            },"",true,this);
        }
    },
    
     //项目详情页确定按钮
    onFormEnsure:function(btn){
    	var form = this.getView().child("form").getForm();

        if (form.isValid()) {
            //获得项目表单信息
            var tzParams = this.getResSetInfoParams();
            var comView = this.getView();
            Ext.tzSubmit(tzParams,function(responseData){
            contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
		    contentPanel.child("teacherMg").store.reload();
                comView.close();
            },"",true,this);
        }
    
    },

    onFormClose:function(btn){
        //关闭窗口
        var comView = this.getView();
        comView.close();
    },


    getResSetInfoParams: function(){
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
        var tzParams = '{"ComID":"TZ_PX_TEACHER_COM","PageID":"TZ_PX_TEACHER_STD","OperateType":"U","comParams":{'+comParams+'}}';
        console.log(tzParams);
        return tzParams;
    },
    
    
    //评论管理
    editReviewById: function(view, rowIndex){
    	var store = view.findParentByType("grid").store;
		var selRec = store.getAt(rowIndex);
		var oprid = selRec.get("oprid");
		this.editSiteColu(oprid);
	},
	editSiteColu: function(oprid){
		grid = this.getView();
		var contentPanel,cmp, className, ViewClass, clsProto;
		var themeName = Ext.themeName;
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_PX_TEACHER_COM"]["TZ_PX_REVIEW_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
	        return;
	     }
	      //该功能对应的JS类
	    className = pageResSet["jsClassName"];
	    if(className == "" || className == undefined){
	         Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_GD_WWLMGL_STD，请检查配置。');
	         return;
       }
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
           if (!clsProto.themeInfo) {
               Ext.log.warn ( 'Example \'' + className + '\' lacks a theme specification for the selected theme: \'' +
                   themeName + '\'. Is this intentional?');
           }
       }
       
       cmp = new ViewClass({ oprid:oprid });
       //操作类型设置为更新
       cmp.actType = "update";

       cmp.on('close',function(panel){
    	   try{
    		   grid.store.reload();
    	   }catch(e){
    	   }	  
       });

       tab = contentPanel.add(cmp);
       contentPanel.setActiveTab(tab);
       Ext.resumeLayouts(true);
       if (cmp.floating) {
           cmp.show();
       }
   },
	//关注列表
   editFocusById: function(view, rowIndex){
    	var store = view.findParentByType("grid").store;
		var selRec = store.getAt(rowIndex);
		var teaOprid = selRec.get("tzScheduleId");
		this.editFocus(tzScheduleId);
	},
	editFocus: function(tzScheduleId){
		grid = this.getView();
		var contentPanel,cmp, className, ViewClass, clsProto;
		var themeName = Ext.themeName;
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_PX_SCHEDULE_COM"]["TZ_PX_STUSCH_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
	        return;
	     }
	      //该功能对应的JS类
	    className = pageResSet["jsClassName"];
	    if(className == "" || className == undefined){
	         Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_GD_WWLMGL_STD，请检查配置。');
	         return;
       }
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
           if (!clsProto.themeInfo) {
               Ext.log.warn ( 'Example \'' + className + '\' lacks a theme specification for the selected theme: \'' +
                   themeName + '\'. Is this intentional?');
           }
       }
       
       cmp = new ViewClass({ teaOprid:teaOprid });
       //操作类型设置为更新
       cmp.actType = "update";

       cmp.on('close',function(panel){
    	   try{
    		   grid.store.reload();
    	   }catch(e){
    	   }	  
       });

       tab = contentPanel.add(cmp);
       contentPanel.setActiveTab(tab);
       Ext.resumeLayouts(true);
       if (cmp.floating) {
           cmp.show();
       }
   }

});