Ext.define('KitchenSink.view.trainStudentMg.studentController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.studentInfoMg',
    
    /*按条件查询项目列表，seachCfg在可配置中配置*/
        selectForm:function(btn){
        Ext.tzShowCFGSearch({
            cfgSrhId: 'PX_STU_COM.PX_STU_STD.PX_STUDENT_V', 
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
	   	this.editTeacherInfoByRecord(selList[0]);
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
    	var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["PX_STU_COM"]["PX_STU_INFO_STD"];
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
			var orgid = record.get("orgid");
			var tzParams = '{"ComID":"PX_STU_COM","PageID":"PX_STU_STD","OperateType":"QF","comParams":{"OPRID":"'+OPRID+'","orgid":"'+orgid+'"}}';
			//加载数据
			var msgForm = this.lookupReference('studentMgForm');
			var form = this.lookupReference('studentMgForm').getForm();
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
		    contentPanel.child("studentInfoMg").store.reload();
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
        var tzParams = '{"ComID":"PX_STU_COM","PageID":"PX_STU_STD","OperateType":"U","comParams":{'+comParams+'}}';
        console.log(tzParams);
        return tzParams;
    },
    
    
    //评论管理
    reviewInfos: function(view, rowIndex){
    	var selList = this.getView().getSelectionModel().getSelection();//返回一个当前被选择的记录的数组
	   	//选中行长度
	   	var checkLen = selList.length;
	   	if(checkLen == 0){
	   		Ext.Msg.alert("提示","请选择一条记录");   
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
       
       cmp = new ViewClass({ oprid:record.get("oprid") });
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
	   		Ext.Msg.alert("提示","请选择一条记录");   
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
	    className ="KitchenSink.view.trainTeacherMg.focusManage";// pageResSet["jsClassName"];
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
       
       cmp = new ViewClass({ teaOprid:record.get("teaOprid") });
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
   editCourseById: function(view, rowIndex){
	   var selList = this.getView().getSelectionModel().getSelection();//返回一个当前被选择的记录的数组
	   	//选中行长度
	   	var checkLen = selList.length;
	   	if(checkLen == 0){
	   		Ext.Msg.alert("提示","请选择一条记录");   
			return;
	   	}
    	this.editCourseByIds(selList[0]);
   },
   editCourseByIds: function(record){
		grid = this.getView();
		var contentPanel,cmp, className, ViewClass, clsProto;
		var themeName = Ext.themeName;
		//是否有访问权限"PX_STU_COURSE_COM.PX_STU_COURSE_STD
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["PX_STU_COM"]["PX_COU_TIME_STD"];
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
      
      cmp = new ViewClass({ oprid:record.get("oprid") });
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