Ext.define('KitchenSink.view.trainTeacherMg.teaCourseTypeController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.teaCourseTypeController',
    
    /*按条件查询项目列表，seachCfg在可配置中配置*/
        selectForm:function(btn){
        Ext.tzShowCFGSearch({
            cfgSrhId: 'TZ_PX_TEACHER_COM.TEA_COU_TYPE_STD.PX_TEA_COURSE_V', 
            callback: function(seachCfg){
                var store = btn.findParentByType("grid").store;
                store.tzStoreParams = seachCfg;
                store.load();
            }
        });
    },
    
    //编辑教师信息
    scoreToCrash: function() {
		//选中行
	   	var selList = this.getView().getSelectionModel().getSelection();//返回一个当前被选择的记录的数组
	   	//选中行长度
	   	//console.log(this.getView());
	   	var checkLen = selList.length;
	   	if(checkLen == 0){
	   		Ext.Msg.alert("提示","请选择一条要修改的记录");   
			return;
	   	}
	   	//var store = this.getView().store;
	   	var removeJson = "";
        var removeRecs = selList;

        for(var i=0;i<removeRecs.length;i++){
            if(removeJson == ""){
                removeJson = Ext.JSON.encode(removeRecs[i].data);
            }else{
                removeJson = removeJson + ','+Ext.JSON.encode(removeRecs[i].data);
            }
        }
        if(removeJson != ""){
            comParams = '"update":[' + removeJson + "]";
            //提交参数
            var tzParams = '{"ComID":"TZ_PX_TEACHER_COM","PageID":"TZ_PX_SCORE_STD","OperateType":"U","comParams":{'+comParams+'}}';
            //保存数据
            Ext.tzSubmit(tzParams,function(){
                //store.reload();
                //grid.commitChanges(grid);
            },"",true,this);
        }else{
        	Ext.Msg.alert("提示","保存成功");
        }
    },
	//编辑项目（列表）
    editTeacherInfo: function(view, rowIndex){
    	//console.log(view);
    	var store = view.findParentByType("grid").store;
    	//console.log(view.findParentByType("grid"));
	 	var selRec = store.getAt(rowIndex);
	 	//皮肤
   	 	var projId = selRec.get("projId");
     	//显示皮肤设置编辑页面
     	this.editTeacherInfoByRecord(selRec);
    },
    editTeacherInfoByRecord:function(record){
    	//是否有访问权限
    	var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_PX_TEACHER_COM"]["TZ_PX_TEA_INFO_STD"];
		if (pageResSet == "" || pageResSet == undefined) {
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		// 该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if (className == "" || className == undefined) {
			Ext.MessageBox.alert('提示',
					'未找到该功能页面对应的JS类，页面ID为：TZ_PROJ_INFO_STD，请检查配置。');
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
			var OPRID = record.get("oprid");
			var tzParams = '{"ComID":"TZ_PX_TEACHER_COM","PageID":"TZ_PX_TEACHER_STD","OperateType":"QF","comParams":{"OPRID":"'+OPRID+'"}}';
			//加载数据
			var msgForm = this.lookupReference('teacherMgForm');
			var form = this.lookupReference('teacherMgForm').getForm();
			Ext.tzLoad(tzParams,function(responseData){
				//组件注册信息数据
				var formData = responseData.formData;
				
				form.setValues(formData);
				if(msgForm.down('hiddenfield[name=titleImageUrl]').getValue()){
					msgForm.down('image[name=titileImage]').setSrc(TzUniversityContextPath + msgForm.down('hiddenfield[name=titleImageUrl]').getValue());	
				}else{
					msgForm.down('image[name=titileImage]').setSrc(TzUniversityContextPath + "/statics/images/tranzvision/mrtx02.jpg");
				}
			});
			
		});
		
        tab = contentPanel.add(cmp);     
		
        contentPanel.setActiveTab(tab);

        Ext.resumeLayouts(true);

        if (cmp.floating) {
            cmp.show();
        }
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
       
       cmp = new ViewClass({ teaOprid:oprid });
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
		var teaOprid = selRec.get("oprid");
		this.editFocus(teaOprid);
	},
	editFocus: function(teaOprid){
		grid = this.getView();
		var contentPanel,cmp, className, ViewClass, clsProto;
		var themeName = Ext.themeName;
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_PX_TEACHER_COM"]["TZ_PX_FOCUS_STD"];
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
   },
	//课时变化列表
   addPermission:function(btn){
       var me = this;
       /*if(this.getView().actType == "add"){
           Ext.MessageBox.alert("提示","请先保存许可权信息后，再新增授权组件。");
           return;
       }
       var form = this.getView().child("form").getForm();*/
       //alert(this.teaOprid);
       var grid = btn.findParentByType("grid");
       var teaOprid=grid.teaOprid;
       var store = grid.store;
       Ext.tzShowPromptSearch({
           recname: 'PX_COURSE_TYPE_T',
           searchDesc: '选择课程级别',
           maxRow:20,
           condition:{
               srhConFields:{
            	   TZ_COURSE_TYPE_ID:{
                       desc:'课程级别编号',
                       operator:'07',
                       type:'01'
                   },
                   TZ_COURSE_TYPE:{
                       desc:'课程级别类型',
                       operator:'07',
                       type:'01'
                   }
               }
           },
           srhresult:{
        	   TZ_COURSE_TYPE_ID: '许可权编号',
        	   TZ_COURSE_TYPE: '许可权描述'
           },
           multiselect: true,
           callback: function(selection){
               //var oprid= "TZ_14072";//form.findField("roleName").getValue();
               for(var i= 0;i<selection.length;i++){
                   var tzCourseTypeId=selection[i].data.TZ_COURSE_TYPE_ID;
                   var typeName=selection[i].data.TZ_COURSE_TYPE;

                   var rolePlstModal = Ext.create('KitchenSink.view.trainTeacherMg.teaCourseTypeModel',{
                	   oprid:teaOprid,
                	   tzCourseTypeId:tzCourseTypeId,
                	   typeName:typeName
                   });
                   if(store.findRecord("tzCourseTypeId",selection[i].data.tzCourseTypeId) == null){
                       //store.add(rolePlstModal);
                	   //新增
                       comParams = '"add":['+Ext.JSON.encode(rolePlstModal.data)+']';

                     //提交参数TZ_PX_TEACHER_COM.TEA_COU_TYPE_STD
   					var tzParams = '{"ComID":"TZ_PX_TEACHER_COM","PageID":"TEA_COU_TYPE_STD","OperateType":"U","comParams":{'+comParams+'}}';
   			        //保存数据
   			        if(comParams!=""){
   			            Ext.tzSubmit(tzParams,function(){
   			                store.reload();
   			            },"",true,this);
   			        }
                   }
               }
           }
       });
   },

			//comInfoPanel.js 中grid 每行中的删除
			deletePageRegInfoOne: function(view, rowIndex){
				Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
					if(btnId == 'yes'){
						var store = view.findParentByType("grid").store;
						store.removeAt(rowIndex);
					}
				},this);
			},
			deletePageRegInfos: function(btn){
				   //页面注册信息列表
				   var grid = this.getView().child("grid");
					//选中行
				   var selList = grid.getSelectionModel().getSelection();
				   //选中行长度
				   var checkLen = selList.length;
				   if(checkLen == 0){
						Ext.Msg.alert("提示","请选择要删除的记录");   
						return;
				   }else{
						Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
							if(btnId == 'yes'){					   
							   var store = grid.store;
							   store.remove(selList);
							}												  
						},this);   
				   }
				},
				
				
				onComRegSave: function(btn){
					//组件注册表单
					//var form = this.getView().child("form").getForm();
					//if (form.isValid()) {
						//获取组件注册信息参数
						var tzParams = this.getComRegInfoParams();
						//var comView = this.getView();
						//页面注册信息列表
						//var grid = comView;
						//页面注册信息数据
						//var store = grid.getStore();
						//Ext.tzSubmit(tzParams,function(responseData){
							/*comView.actType = "update";	
							form.findField("tzCourseId").setReadOnly(true);
			                //if(store.isLoaded()){
			                	var tzStoreParams = '{"tzCourseId":"'+form.findField("tzCourseId").getValue()+'"}';
			                    store.tzStoreParams = tzStoreParams;
			                    store.reload();*/
			                //}
						//},"",true,this);
					//}
				},
				onComRegEnsure: function(btn){
					//组件注册表单
					this.onComRegSave();
					this.onComRegClose();
				},
				getComRegInfoParams: function(){
			        //组件信息标志
			        var actType = this.getView().actType;
			        //更新操作参数
			        var comParams = "";

			        //许可权列表
			        var grid = this.getView();
			        //许可权列表数据
			        var store = grid.getStore();
			        //修改记录
			        var gridEditJson = "";
			        var mfRecs = store.getModifiedRecords();
			        for(var i=0;i<mfRecs.length;i++){
			            if(gridEditJson == ""){
			                gridEditJson = Ext.JSON.encode(mfRecs[i].data);
			            }else{
			                gridEditJson = gridEditJson + ','+Ext.JSON.encode(mfRecs[i].data);
			            }
			        }
			        if(gridEditJson != ""){
			            if(comParams == ""){
			                comParams = '"update":[' + gridEditJson + "]";
			            }else{
			                editJson=gridEditJson;
			                comParams = comParams + ',"update":[' + gridEditJson + "]";
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

					//提交参数TZ_PX_TEACHER_COM.TEA_COU_TYPE_STD
					var tzParams = '{"ComID":"TZ_PX_TEACHER_COM","PageID":"TEA_COU_TYPE_STD","OperateType":"U","comParams":{'+comParams+'}}';
			        //保存数据
			        if(comParams!=""){
			            Ext.tzSubmit(tzParams,function(){
			                store.reload();
			            },"",true,this);
			        }
				},
				onComRegClose: function(btn){
					//关闭窗口
					this.getView().close();
				},

});