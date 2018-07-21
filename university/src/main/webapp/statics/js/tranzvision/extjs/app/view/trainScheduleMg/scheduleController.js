Ext.define('KitchenSink.view.trainScheduleMg.scheduleController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.scheduleController',
    
    /*按条件查询项目列表，seachCfg在可配置中配置*/
        selectForm:function(btn){
        Ext.tzShowCFGSearch({
            cfgSrhId: 'PX_SCHEDULE_COM.PX_SCHEDULE_STD.PX_TEA_SCHEDULE_V', 
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
            var tzParams = '{"ComID":"PX_TEACHER_COM","PageID":"PX_SCORE_STD","OperateType":"U","comParams":{'+comParams+'}}';
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
    	var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["PX_TEACHER_COM"]["PX_TEA_INFO_STD"];
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
			var tzParams = '{"ComID":"PX_TEACHER_COM","PageID":"PX_TEACHER_STD","OperateType":"QF","comParams":{"OPRID":"'+OPRID+'"}}';
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
                comView.actType = "update";
            },"",true,this);
        }
    },
    
     //项目详情页确定按钮
    onFormEnsure:function(btn){
    	this.onFormSave();
    	this.onFormClose();
    
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

        //提交参数PX_SCHEDULE_COM"]["SCHEDULE_INFO_STD"
        var tzParams = '{"ComID":"PX_SCHEDULE_COM","PageID":"SCHEDULE_INFO_STD","OperateType":"U","comParams":{'+comParams+'}}';
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
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["PX_TEACHER_COM"]["PX_REVIEW_STD"];
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
	 //选中行
	   	var selList = this.getView().getSelectionModel().getSelection();//返回一个当前被选择的记录的数组
	   	//选中行长度
	   	var checkLen = selList.length;
	   	if(checkLen == 0){
	   		Ext.Msg.alert("提示","请选择一条记录");   
			return;
	   	}
		var tzScheduleId = selList[0].get("tzScheduleId");
		this.editFocus(tzScheduleId);
	},
	editFocus: function(tzScheduleId){
		grid = this.getView();
		var contentPanel,cmp, className, ViewClass, clsProto;
		var themeName = Ext.themeName;
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["PX_SCHEDULE_COM"]["PX_STUSCH_STD"];
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
       
       cmp = new ViewClass({ tzScheduleId:tzScheduleId });
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
   addPermission:function(view, rowIndex){
	 //选中行
	   	var selList = this.getView().getSelectionModel().getSelection();//返回一个当前被选择的记录的数组
	   	//选中行长度
	   	var checkLen = selList.length;
	   	if(checkLen == 0){
	   		Ext.Msg.alert("提示","请选择一条记录");   
			return;
	   	}
		var record = selList[0];
		
	   //var store = view.findParentByType("grid").store;
	   //var record=store.getAt(rowIndex);
	   var tzScheduleType=record.get("tzScheduleType");
	   //alert(tzScheduleType);
	   if(tzScheduleType!='正常'){
		   Ext.MessageBox.confirm('确认', '无法撤销：原因为已上课或撤销', function(btnId){
				/*if(btnId == 'yes'){
					var store = view.findParentByType("grid").store;
					var record=store.getAt(rowIndex);
		   			var tzAppStatus=record.get("tzAppStatus");
		   			alert(tzAppStatus);
				}*/
			},this);
	   }else{
		  /* var selRec = store.getAt(rowIndex);
		   //组件ID
		   var comID = selRec.get("tzCourseId");*/
		   //显示组件注册信息编辑页面
		   this.editByID(record);
	   }  
   },
	editByID: function(comID){
		//是否有访问权限PX_SCHEDULE_COM.PX_SCHEDULE_STD.
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["PX_SCHEDULE_COM"]["SCHEDULE_INFO_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_AQ_COMREG_STD，请检查配置。');
			return;
		}
		
		var contentPanel,cmp, className, ViewClass, clsProto;
		var themeName = Ext.themeName;
		
		contentPanel = Ext.getCmp('tranzvision-framework-content-panel');			
		contentPanel.body.addCls('kitchensink-example');

		//className = 'KitchenSink.view.security.com.comInfoPanel';
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
			var form =panel.child('form').getForm();
			form.findField("tzScheduleId").setReadOnly(true);
			form.findField("tzScheduleId").setFieldStyle('background:#F4F4F4');
			var tzAppStatus=comID.get("tzAppStatus");
			//未预约
			if(tzAppStatus==1){
				form.findField("oprid").hide();
				form.findField("oprid").setFieldStyle('background:#F4F4F4');
				//form.findField("oprid").setReadOnly(true);
				//form.findField("oprid").setFieldStyle('background:#F4F4F4');
			}
			form.loadRecord(comID);
			//页面注册信息列表
			/*var grid = panel.child('grid');
			//参数
			var tzParams = '{"ComID":"PX_COURSE_COM","PageID":"PX_COURSE_STD","OperateType":"QF","comParams":{"tzCourseId":"'+comID+'"}}';
			//加载数据
			Ext.tzLoad(tzParams,function(responseData){
				//组件注册信息数据
				var formData = responseData.formData;
				form.setValues(formData);
				//页面注册信息列表数据
				var roleList = responseData.listData;	

				var tzStoreParams = '{"tzCourseId":"'+comID+'"}';
				grid.store.tzStoreParams = tzStoreParams;
				grid.store.load();						
			});*/
			
		});
		
		tab = contentPanel.add(cmp);     
		
		contentPanel.setActiveTab(tab);

		Ext.resumeLayouts(true);

		if (cmp.floating) {
			cmp.show();
		}
	},
	clearPmtSearchCom: function(btn){
		var form = this.getView().child("form").getForm();
		form.findField("oprid").setValue("");
		form.findField("name").setValue("");
		
	},
	pmtSearchCom: function(btn){
		var form = this.getView().child("form").getForm();
		Ext.tzShowPromptSearch({
			recname: 'px_teacher_v',
			searchDesc: '搜索组件',
			maxRow:20,
			condition:{
				srhConFields:{
					OPRID:{
						desc:'组件ID',
						operator:'07',
						type:'01'	
					},
					TZ_REALNAME:{
						desc:'组件名称',
						operator:'07',
						type:'01'		
					}	
				}	
			},
			srhresult:{
				OPRID: '组件ID',
				TZ_REALNAME: '组件名称'	
			},
			multiselect: false,
			callback: function(selection){
				form.findField("oprid").setValue(selection[0].data.OPRID);
				form.findField("name").setValue(selection[0].data.TZ_REALNAME);
			}
		});	
	}

});