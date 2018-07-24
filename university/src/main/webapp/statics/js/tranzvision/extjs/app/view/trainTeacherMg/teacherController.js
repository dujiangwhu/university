Ext.define('KitchenSink.view.trainTeacherMg.teacherController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.teacherController',
    
    /*按条件查询项目列表，seachCfg在可配置中配置*/
        selectForm:function(btn){
        Ext.tzShowCFGSearch({
            cfgSrhId: 'PX_TEACHER_COM.PX_TEACHER_STD.PX_TEACHER_V', 
            callback: function(seachCfg){
                var store = btn.findParentByType("grid").store;
                store.tzStoreParams = seachCfg;
                store.load();
            }
        });
    },
    
    //提现
    scoreToCrashs: function() {
		//选中行
	   	var selList = this.getView().getSelectionModel().getSelection();//返回一个当前被选择的记录的数组
	   	//选中行长度
	   	var checkLen = selList.length;
	   	if(checkLen == 0){
			Ext.Msg.alert("提示","请选择一条要修改的记录");   
			return;
	    }else if(checkLen >1){
		   Ext.Msg.alert("提示","只能选择一条要修改的记录");   
		   return;
	    }
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
            var store = this.getView().store;
            Ext.tzSubmit(tzParams,function(){
            	store.reload();
                //grid.commitChanges(grid);
            },"",true,this);
        }else{
        	Ext.Msg.alert("提示","保存成功");
        }
    },
	//编辑项目（列表）
    editTeacherInfos: function(view, rowIndex){
    	//选中行
	   	var selList = this.getView().getSelectionModel().getSelection();//返回一个当前被选择的记录的数组
	   	//选中行长度
	   	var checkLen = selList.length;
	   	if(checkLen == 0){
			Ext.Msg.alert("提示","请选择一条要修改的记录");   
			return;
	    }else if(checkLen >1){
		   Ext.Msg.alert("提示","只能选择一条要修改的记录");   
		   return;
	    }
     	this.editTeacherInfoByRecord(selList[0]);
    },
    editTeacherInfoByRecord:function(record){
    	Ext.tzSetCompResourses("PX_TEACHER_COM");
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
			if (!clsProto.themeInfo) {
				Ext.log
						.warn('Example \''
								+ className
								+ '\' lacks a theme specification for the selected theme: \''
								+ themeName + '\'. Is this intentional?');
			}
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
                contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
    		    contentPanel.child("teacherMg").store.reload();
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
        var tzParams = '{"ComID":"PX_TEACHER_COM","PageID":"PX_TEACHER_STD","OperateType":"U","comParams":{'+comParams+'}}';
        console.log(tzParams);
        return tzParams;
    },
    
    
    //评论管理
    reviewInfos: function(view, rowIndex){
    	var selList = this.getView().getSelectionModel().getSelection();//返回一个当前被选择的记录的数组
	   	//选中行长度
	   	var checkLen = selList.length;
	   	if(checkLen == 0){
			Ext.Msg.alert("提示","请选择一条要修改的记录");   
			return;
	    }else if(checkLen >1){
		   Ext.Msg.alert("提示","只能选择一条要修改的记录");   
		   return;
	    }
     	this.reviewInfosByRecord(selList[0]);
	},
	reviewInfosByRecord: function(record){
		Ext.tzSetCompResourses("PX_REVIEW_COM");
		grid = this.getView();
		var contentPanel,cmp, className, ViewClass, clsProto;
		var themeName = Ext.themeName;
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["PX_REVIEW_COM"]["PX_REVIEW_STD"];
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
       
       cmp = new ViewClass({ teaOprid:record.get("oprid") });
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
   focusInfos: function(view, rowIndex){
	   var selList = this.getView().getSelectionModel().getSelection();//返回一个当前被选择的记录的数组
	   	//选中行长度
	   	var checkLen = selList.length;
	   	if(checkLen == 0){
			Ext.Msg.alert("提示","请选择一条要修改的记录");   
			return;
	    }else if(checkLen >1){
		   Ext.Msg.alert("提示","只能选择一条要修改的记录");   
		   return;
	    }
    	this.focusInfosByRecord(selList[0]);
	},
	focusInfosByRecord: function(record){
		Ext.tzSetCompResourses("PX_FOCUS_COM");
		grid = this.getView();
		var contentPanel,cmp, className, ViewClass, clsProto;
		var themeName = Ext.themeName;
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["PX_FOCUS_COM"]["PX_FOCUS_STD"];
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
       
       cmp = new ViewClass({ teaOprid:record.get("oprid") });
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
	//课程级别列表
   courseLevelInfos: function(view, rowIndex){
	   	var selList = this.getView().getSelectionModel().getSelection();//返回一个当前被选择的记录的数组
	   	//选中行长度
	   	var checkLen = selList.length;
	   	if(checkLen == 0){
			Ext.Msg.alert("提示","请选择一条要修改的记录");   
			return;
	    }else if(checkLen >1){
		   Ext.Msg.alert("提示","只能选择一条要修改的记录");   
		   return;
	    }
	   	this.courseLevelInfosByRecord(selList[0]);
	},
	courseLevelInfosByRecord: function(record){
		Ext.tzSetCompResourses("PX_TEACHER_COM");
				grid = this.getView();
				var contentPanel,cmp, className, ViewClass, clsProto;
				var themeName = Ext.themeName;
				//是否有访问权限
				var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["PX_TEACHER_COM"]["TEA_COU_TYPE_STD"];
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
		       //alert(teaOprid);
		       cmp = new ViewClass({ teaOprid:record.get("oprid") });
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