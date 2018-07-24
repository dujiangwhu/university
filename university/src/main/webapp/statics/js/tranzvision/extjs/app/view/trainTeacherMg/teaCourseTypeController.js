Ext.define('KitchenSink.view.trainTeacherMg.teaCourseTypeController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.teaCourseTypeController',
    
    /*按条件查询项目列表，seachCfg在可配置中配置*/
        selectForm:function(btn){
        Ext.tzShowCFGSearch({
            cfgSrhId: 'PX_TEACHER_COM.TEA_COU_TYPE_STD.PX_TEA_COURSE_V', 
            callback: function(seachCfg){
                var store = btn.findParentByType("grid").store;
                store.tzStoreParams = seachCfg;
                store.load();
            }
        });
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
           recname: 'PX_COURSE_TYPE_V',
           searchDesc: '选择课程级别',
           maxRow:20,
           condition:{
               srhConFields:{
            	   TZ_COURSE_TYPE_ID:{
                       desc:'课程级别编号',
                       operator:'07',
                       type:'01'
                   },
                   COURSE_TYPE_NAME:{
                       desc:'课程级别名称',
                       operator:'07',
                       type:'01'
                   }
               }
           },
           srhresult:{
        	   TZ_COURSE_TYPE_ID: '课程级别编号',
        	   COURSE_TYPE_NAME: '课程级别名称'
           },
           multiselect: true,
           callback: function(selection){
               //var oprid= "TZ_14072";//form.findField("roleName").getValue();
               for(var i= 0;i<selection.length;i++){
                   var tzCourseTypeId=selection[i].data.TZ_COURSE_TYPE_ID;
                   var typeName=selection[i].data.COURSE_TYPE_NAME;

                   var rolePlstModal = Ext.create('KitchenSink.view.trainTeacherMg.teaCourseTypeModel',{
                	   oprid:teaOprid,
                	   tzCourseTypeId:tzCourseTypeId,
                	   typeName:typeName
                   });
                   if(store.findRecord("tzCourseTypeId",selection[i].data.tzCourseTypeId) == null){
                       //store.add(rolePlstModal);
                	   //新增
                       comParams = '"add":['+Ext.JSON.encode(rolePlstModal.data)+']';

                     //提交参数PX_TEACHER_COM.TEA_COU_TYPE_STD
   					var tzParams = '{"ComID":"PX_TEACHER_COM","PageID":"TEA_COU_TYPE_STD","OperateType":"U","comParams":{'+comParams+'}}';
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
				   var grid = this.getView();
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

					//提交参数PX_TEACHER_COM.TEA_COU_TYPE_STD
					var tzParams = '{"ComID":"PX_TEACHER_COM","PageID":"TEA_COU_TYPE_STD","OperateType":"U","comParams":{'+comParams+'}}';
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