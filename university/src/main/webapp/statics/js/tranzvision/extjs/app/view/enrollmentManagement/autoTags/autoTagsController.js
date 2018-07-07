Ext.define('KitchenSink.view.enrollmentManagement.autoTags.autoTagsController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.autoTagsController',
    
    queryList:function(btn){
        Ext.tzShowCFGSearch({
            cfgSrhId:'TZ_AUTO_TAGS_COM.TZ_AUTO_TAGS_STD.TZ_ZDBQ_DFN_TBL',
            callback:function(searchCfg){
                var store = btn.findParentByType("grid").store;
                store.tzStoreParams = searchCfg;
                store.load();
            }
        })
    },
    
    addAutoTag:function(btn){
    	var grid = btn.findParentByType('autoTagsList');
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_AUTO_TAGS_COM"]["TZ_TAG_DFN_STD"];
        if(pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_AUTO_TAGS_COM.TZ_AUTO_TAGS_STD.promt","提示"),
            		Ext.tzGetResourse("TZ_AUTO_TAGS_COM.TZ_AUTO_TAGS_STD.nmyqx","您没有权限"));
            return;
        }
        //该功能对应的js类
        var className = pageResSet["jsClassName"];
        if(className =="" || className == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_AUTO_TAGS_COM.TZ_AUTO_TAGS_STD.promt","提示"),
            		Ext.tzGetResourse("TZ_AUTO_TAGS_COM.TZ_AUTO_TAGS_STD.wzdgjs","无法获取对应的js类，请检查配置"));
            return;
        }
        var contentPanel,cmp,className,ViewClass,clsProto;
        var themeName = Ext.themeName;
        contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
        contentPanel.body.addCls('kitchenSink-example');
        
        if(!Ext.ClassManager.get(className)){
            Ext.syncRequire(className);
        }
        ViewClass = Ext.ClassManager.get(className);

        cmp = new ViewClass({
        	actType: "add",
        	callback: function(){
        		grid.getStore().reload();
        	}
        });
        cmp.on('afterrender',function(){
            
        });
        tab = contentPanel.add(cmp);
        contentPanel.setActiveTab(tab);
        Ext.resumeLayouts(true);
        if(cmp.floating){
            cmp.show();
        }
    },
    
    ediAutoTag: function(btn){
    	var grid = this.getView();
    	//选中行
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
		var tagId = selList[0].get("tagId");

		this.editAutoTagByTagID(grid, tagId);
    },
    
    editCurrAutoTag:function(view, rowIndex){
    	var grid = view.findParentByType("grid");
        var store = grid.store;
        var selRec = store.getAt(rowIndex);
        var tagId = selRec.get("tagId");
        
        this.editAutoTagByTagID(grid, tagId);
    },
    editAutoTagByTagID:function(grid, tagId){
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_AUTO_TAGS_COM"]["TZ_TAG_DFN_STD"];
        if(pageResSet == "" || pageResSet== undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_AUTO_TAGS_COM.TZ_AUTO_TAGS_STD.prompt","提示"),
            		Ext.tzGetResourse("TZ_AUTO_TAGS_COM.TZ_AUTO_TAGS_STD.nmyqx","您没有权限"));
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_AUTO_TAGS_COM.TZ_AUTO_TAGS_STD.prompt","提示"),
            		Ext.tzGetResourse("TZ_AUTO_TAGS_COM.TZ_AUTO_TAGS_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
            return;
        }
        var contentPanel,cmp, className, ViewClass, clsProto;
        var themeName = Ext.themeName;
        contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
        contentPanel.body.addCls('kitchensink-example');
        if(!Ext.ClassManager.isCreated(className)){
            Ext.syncRequire(className);
        }
        ViewClass = Ext.ClassManager.get(className);

        cmp = new ViewClass({
        	actType: "update",
        	callback: function(){
        		grid.getStore().reload();
        	}
        });

        cmp.on('afterrender',function(panel){
        	var form = panel.child('form').getForm();
            //参数
            var tzParams = '{"ComID":"TZ_AUTO_TAGS_COM","PageID":"TZ_TAG_DFN_STD","OperateType":"QF","comParams":{"tagId":"'+tagId+'"}}';
            //加载数据
            Ext.tzLoad(tzParams,function(respData){
                form.setValues(respData.formData);
            });

        });

        tab = contentPanel.add(cmp);
        contentPanel.setActiveTab(tab);
        Ext.resumeLayouts(true);
        if (cmp.floating) {
            cmp.show();
        }
    },
    
    delAutoTag: function(btn){
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
 				   	var store = grid.getStore();
                     store.remove(selList);
 				}												  
 			},this);   
 	   	}
    },
    
    delCurrAutoTag: function(view, rowIndex){
    	Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
            if(btnId == 'yes'){
                var store = view.findParentByType("grid").store;
                store.removeAt(rowIndex);
            }
        },this);
    },
    
    autoTagsListSave: function(btn){
    	var grid = btn.findParentByType("grid");
        var store = grid.getStore();
        var removeRecs = store.getRemovedRecords();
		var btnName = btn.name;
		
		var deleteArr = [];
		if(removeRecs.length > 0){
			for(var i=0;i<removeRecs.length;i++){
            	deleteArr.push(removeRecs[i].data);
            }
			
			var tzParamsObj = {
				ComID: 'TZ_AUTO_TAGS_COM',
				PageID: 'TZ_AUTO_TAGS_STD',
				OperateType: 'U',
				comParams: {
					delete: deleteArr
				}
			};
			
			//提交参数
			var tzParams = Ext.JSON.encode(tzParamsObj);
			//保存数据
			Ext.tzSubmit(tzParams,function(){
				if(btnName == "ensureBtn"){
					grid.close();
				}else{
					store.reload();
				}
			},"",true,this);	
        }else{
			if(btnName == "ensureBtn") grid.close();
		}
    },
    
    autoTagsListEnsure: function(btn){
    	this.autoTagsListSave(btn);
    },
    

    onAutoTagSave:function(btn){
        var panel = btn.findParentByType("panel");
        var actType= panel.actType;
        var form = panel.child("form").getForm();
        var btnName = btn.name;
        
        if(form.isValid()){
        	var comParamsObj = {};
        	
            if(actType == "add"){
            	comParamsObj.add = [form.getValues()];
            }else{
            	comParamsObj.update = [form.getValues()];
            }
            
            var tzParamsObj = {
    				ComID: 'TZ_AUTO_TAGS_COM',
    				PageID: 'TZ_TAG_DFN_STD',
    				OperateType: 'U',
    				comParams: comParamsObj
    			};
    			
    		var tzParams = Ext.JSON.encode(tzParamsObj);
            Ext.tzSubmit(tzParams,function(formData){
            	if(actType == "add"){
                    form.setValues(formData);  
                    panel.actType = "update";
            	}

                panel.callback();
                if(btnName == "ensureBtn"){
                	panel.close();
                }
            },"",true,this)
        }
    },
    onAutoTagEnsure:function(btn){
        this.onAutoTagSave(btn);
    }
});