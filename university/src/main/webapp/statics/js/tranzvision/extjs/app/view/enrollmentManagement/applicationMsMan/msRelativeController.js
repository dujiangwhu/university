Ext.define('KitchenSink.view.enrollmentManagement.applicationMsMan.msRelativeController', {
	extend: 'Ext.app.ViewController',
    alias: 'controller.msRelativeController',
   // requires: ['KitchenSink.view.enrollmentManagement.applicationMsMan.downloadCommit'],
    pmtSearchClassIDTmp:function(btn){
		var form = btn.findParentByType("window").child("form").getForm();
		Ext.tzShowPromptSearch({
			recname: 'TZ_CLASS_OPR_V',
			searchDesc: '搜索班级信息',
			maxRow:20,
			condition:{
				presetFields:{
					TZ_DLZH_ID:{
						value:TranzvisionMeikecityAdvanced.Boot.loginUserId,
						type:'01'
					},
					TZ_JG_ID:{
						value:Ext.tzOrgID,
						type:'01'
					},
					TZ_IS_APP_OPEN:{
						value:"Y",
						type:'01'
					}
				},
				srhConFields:{
					TZ_CLASS_ID:{
						desc:'班级编号',
						operator:'07',
						type:'01'	
					},
					TZ_CLASS_NAME:{
						desc:'班级名称',
						operator:'07',
						type:'01'		
					}	
				}	
			},
			srhresult:{
				TZ_CLASS_ID: '班级编号',
				TZ_CLASS_NAME: '班级名称'	
			},
			multiselect: false,
			callback: function(selection){
				form.findField("classID").setValue(selection[0].data.TZ_CLASS_ID);
				form.findField("className").setValue(selection[0].data.TZ_CLASS_NAME);
			}
		});
    },
    pmtSearchBatchIDTmp:function(btn){
		var form = btn.findParentByType("window").child("form").getForm();
		var values = form.getValues();
		var classID = values['classID'];
		Ext.tzShowPromptSearch({
			recname: 'TZ_CLASS_BATCH_V',
			searchDesc: '搜索批次信息',
			maxRow:20,
			condition:{
				presetFields:{
					TZ_DLZH_ID:{
						value:TranzvisionMeikecityAdvanced.Boot.loginUserId,
						type:'01'
					},
					TZ_JG_ID:{
						value:Ext.tzOrgID,
						type:'01'
					},
					TZ_IS_APP_OPEN:{
						value:"Y",
						type:'01'
					},
					TZ_CLASS_ID:{
						value:classID,
						type: '01'	
					}
				},
				srhConFields:{
					TZ_BATCH_ID:{
						desc:'批次ID',
						operator:'07',
						type:'01'		
					},
					TZ_BATCH_NAME:{
						desc:'批次名称',
						operator:'07',
						type:'01'		
					}	
				}	
			},
			srhresult:{
				TZ_BATCH_ID: '批次ID',
				TZ_BATCH_NAME: '批次名称'	
			},
			multiselect: false,
			callback: function(selection){
				form.findField("batchID").setValue(selection[0].data.TZ_BATCH_ID);
				form.findField("batchName").setValue(selection[0].data.TZ_BATCH_NAME);
			}
		});	
    },
    viewMszgMan:function(btn){
    	var config=btn.findParentByType("panel").config;
        Ext.tzShowCFGSearch({
            cfgSrhId: 'TZ_BMGL_BMBSH_COM.TZ_BMGL_MSZG_STD.TZ_MSZG_MAN_V',
            condition:{
            	TZ_CLASS_ID:config.classID,
            	TZ_BATCH_ID:config.batchID
            },
            callback: function(seachCfg){
                var store = btn.findParentByType("grid").store;
                store.tzStoreParams = seachCfg;
                store.load();
            }
        });
    },
//    searchInfo:function(btn){
//    	var win = btn.findParentByType("window");
//        var formValues = win.child("form").getForm().getValues();
//        var classId = formValues['classID'];
//        var batchId = formValues['batchID'];
//        var mshId=formValues['mshId'];
//        var name=formValues['name'];
//        var type=formValues['type'];
//        //var tzStoreParams='{"TZ_CLASS_ID-operator":"01","TZ_CLASS_ID-value":"'+classId+'","TZ_BATCH_ID-operator":"01","TZ_BATCH_ID-value":"'+batchId+'","TZ_MSH_ID-operator":"07","TZ_MSH_ID-value":"'+mshId+'","TZ_REALNAME-operator":"07","TZ_REALNAME-value":"'+name+'"}';
//        if(type=="mszg"){
//        	var tzStoreParams = {
//            		"cfgSrhId": "TZ_BMGL_BMBSH_COM.TZ_BMGL_MSZG_STD.TZ_MSZG_MAN_V",
//            		"condition":{
//            			"TZ_CLASS_ID-operator": "01",
//            			"TZ_CLASS_ID-value": classId,
//            			"TZ_BATCH_ID-operator": "01",
//            			"TZ_BATCH_ID-value": batchId,
//            			"TZ_MSH_ID-operator": "07",
//            			"TZ_MSH_ID-value": mshId,
//            			"TZ_REALNAME-operator": "07",
//            			"TZ_REALNAME-value": name,
//            				}
//            };
//            contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
//    		contentPanel.child("mszgInfoForm").store.tzStoreParams=Ext.encode(tzStoreParams);
//    		contentPanel.child("mszgInfoForm").store.reload();
//        }else if(type=="msjg"){
//        	var tzStoreParams = {
//            		"cfgSrhId": "TZ_BMGL_BMBSH_COM.TZ_BMGL_MSJG_STD.TZ_MSJG_MAN_V",
//            		"condition":{
//            			"TZ_CLASS_ID-operator": "01",
//            			"TZ_CLASS_ID-value": classId,
//            			"TZ_BATCH_ID-operator": "01",
//            			"TZ_BATCH_ID-value": batchId,
//            			"TZ_MSH_ID-operator": "07",
//            			"TZ_MSH_ID-value": mshId,
//            			"TZ_REALNAME-operator": "07",
//            			"TZ_REALNAME-value": name,
//            				}
//            };
//        	contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
//    		contentPanel.child("msjgInfoForm").store.tzStoreParams=Ext.encode(tzStoreParams);
//    		contentPanel.child("msjgInfoForm").store.reload();
//        }else if(type=="bscj"){
//        	var tzStoreParams = {
//            		"cfgSrhId": "TZ_BMGL_BMBSH_COM.TZ_BMGL_BSCJ_STD.TZ_BSCJ_MAN_V",
//            		"condition":{
//            			"TZ_CLASS_ID-operator": "01",
//            			"TZ_CLASS_ID-value": classId,
//            			"TZ_BATCH_ID-operator": "01",
//            			"TZ_BATCH_ID-value": batchId,
//            			"TZ_MSH_ID-operator": "07",
//            			"TZ_MSH_ID-value": mshId,
//            			"TZ_REALNAME-operator": "07",
//            			"TZ_REALNAME-value": name,
//            				}
//            };
//        	contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
//    		contentPanel.child("bscjInfoForm").store.tzStoreParams=Ext.encode(tzStoreParams);
//    		contentPanel.child("bscjInfoForm").store.reload();
//        }else if(type=="lqjg"){
//        	var tzStoreParams = {
//            		"cfgSrhId": "TZ_BMGL_BMBSH_COM.TZ_BMGL_LQJG_STD.TZ_LQJG_MAN_V",
//            		"condition":{
//            			"TZ_CLASS_ID-operator": "01",
//            			"TZ_CLASS_ID-value": classId,
//            			"TZ_BATCH_ID-operator": "01",
//            			"TZ_BATCH_ID-value": batchId,
//            			"TZ_MSH_ID-operator": "07",
//            			"TZ_MSH_ID-value": mshId,
//            			"TZ_REALNAME-operator": "07",
//            			"TZ_REALNAME-value": name,
//            				}
//            };
//        	contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
//    		contentPanel.child("lqjgInfoForm").store.tzStoreParams=Ext.encode(tzStoreParams);
//    		contentPanel.child("lqjgInfoForm").store.reload();
//        }
//        win.close();
//    },
    closeWin:function(btn){
    	//关闭窗口
        this.getView().close();
    },
    viewMsjgMan:function(btn){
    	var config=btn.findParentByType("panel").config;
    	Ext.tzShowCFGSearch({
            cfgSrhId: 'TZ_BMGL_BMBSH_COM.TZ_BMGL_MSJG_STD.TZ_MSJG_MAN_V',
            condition:{
            	TZ_CLASS_ID:config.classID,
            	TZ_BATCH_ID:config.batchID
            },
            callback: function(seachCfg){
                var store = btn.findParentByType("grid").store;
                store.tzStoreParams = seachCfg;
                store.load();
            }
        });
    },
    viewXMsjgMan:function(btn){
    	var config=btn.findParentByType("panel").config;
    	Ext.tzShowCFGSearch({
            cfgSrhId: 'TZ_BMGL_BMBSH_COM.TZ_BMGL_XMSJG_STD.TZ_XMSJG_MAN_V',
            condition:{
            	TZ_CLASS_ID:config.classID,
            	TZ_BATCH_ID:config.batchID
            },
            callback: function(seachCfg){
                var store = btn.findParentByType("grid").store;
                store.tzStoreParams = seachCfg;
                store.load();
            }
        });
    },
    viewBscjMan:function(btn){
    	var config=btn.findParentByType("panel").config;
    	Ext.tzShowCFGSearch({
            cfgSrhId: 'TZ_BMGL_BMBSH_COM.TZ_BMGL_BSCJ_STD.TZ_BSCJ_MAN_V',
            condition:{
            	TZ_CLASS_ID:config.classID,
            	TZ_BATCH_ID:config.batchID
            },
            callback: function(seachCfg){
                var store = btn.findParentByType("grid").store;
                store.tzStoreParams = seachCfg;
                store.load();
            }
        });
    },
    viewLqjgMan:function(btn){
    	var config=btn.findParentByType("panel").config;
    	Ext.tzShowCFGSearch({
            cfgSrhId: 'TZ_BMGL_BMBSH_COM.TZ_BMGL_LQJG_STD.TZ_LQJG_MAN_V',
            condition:{
            	TZ_CLASS_ID:config.classID,
            	TZ_BATCH_ID:config.batchID
            },
            callback: function(seachCfg){
                var store = btn.findParentByType("grid").store;
                store.tzStoreParams = seachCfg;
                store.load();
            }
        });
    },
    saveMsjg:function(btn){
        //面试结果列表
    	 var grid = btn.findParentByType("grid");
        //面试结果数据
        var store = grid.getStore();

        var mfRecs = store.getModifiedRecords();
        var editJson = "";
        var comParams="";
        for(var i=0;i<mfRecs.length;i++){
            if(editJson == ""){
                editJson = '{"data":'+Ext.JSON.encode(mfRecs[i].data)+'}';
            }else{
                editJson = editJson + ',{"data":'+Ext.JSON.encode(mfRecs[i].data)+'}';
            }
        }
        if(editJson != ""){
            if(comParams == ""){
                comParams = '"update":[' + editJson + "]";
            }else{
                comParams = comParams + ',"update":[' + editJson + "]";
            }
        }
        //提交参数
        var tzParams="";
        if(comParams!=""){
            tzParams= '{"ComID":"TZ_BMGL_BMBSH_COM","PageID":"TZ_BMGL_MSJG_STD","OperateType":"U","comParams":{'+comParams+'}}';
        }
            var comView = this.getView();
            if(tzParams!=""){
                Ext.tzSubmit(tzParams,function(responseData){
                    store.reload();
                },"",true,this);
            }
    },
    saveXMsjg:function(btn){
        //面试结果列表
    	 var grid = btn.findParentByType("grid");
        //面试结果数据
        var store = grid.getStore();

        var mfRecs = store.getModifiedRecords();
        var editJson = "";
        var comParams="";
        for(var i=0;i<mfRecs.length;i++){
            if(editJson == ""){
                editJson = '{"data":'+Ext.JSON.encode(mfRecs[i].data)+'}';
            }else{
                editJson = editJson + ',{"data":'+Ext.JSON.encode(mfRecs[i].data)+'}';
            }
        }
        if(editJson != ""){
            if(comParams == ""){
                comParams = '"update":[' + editJson + "]";
            }else{
                comParams = comParams + ',"update":[' + editJson + "]";
            }
        }
        //提交参数
        var tzParams="";
        if(comParams!=""){
            tzParams= '{"ComID":"TZ_BMGL_BMBSH_COM","PageID":"TZ_BMGL_XMSJG_STD","OperateType":"U","comParams":{'+comParams+'}}';
        }
            var comView = this.getView();
            if(tzParams!=""){
                Ext.tzSubmit(tzParams,function(responseData){
                    store.reload();
                },"",true,this);
            }
    },
    ensureMsjg:function(btn){
    	//面试结果列表
   	 var grid = btn.findParentByType("grid");
       //面试结果数据
       var store = grid.getStore();

           //获取学生列表参数
       //获取修改的记录
       var mfRecs = store.getModifiedRecords();
       var editJson = "";
       var comParams="";
       for(var i=0;i<mfRecs.length;i++){
           if(editJson == ""){
               editJson = '{"data":'+Ext.JSON.encode(mfRecs[i].data)+'}';
           }else{
               editJson = editJson + ',{"data":'+Ext.JSON.encode(mfRecs[i].data)+'}';
           }
       }
       if(editJson != ""){
           if(comParams == ""){
               comParams = '"update":[' + editJson + "]";
           }else{
               comParams = comParams + ',"update":[' + editJson + "]";
           }
       }
       //提交参数
       var tzParams="";
       if(comParams!=""){
           tzParams= '{"ComID":"TZ_BMGL_BMBSH_COM","PageID":"TZ_BMGL_MSJG_STD","OperateType":"U","comParams":{'+comParams+'}}';
       }
           var comView = this.getView();
           if(tzParams!=""){
               Ext.tzSubmit(tzParams,function(responseData){
                   store.commitChanges();
               },"",true,this);
           }
       btn.findParentByType("grid").close();
    },
    ensureXMsjg:function(btn){
    	//面试结果列表
   	 var grid = btn.findParentByType("grid");
       //面试结果数据
       var store = grid.getStore();

           //获取学生列表参数
       //获取修改的记录
       var mfRecs = store.getModifiedRecords();
       var editJson = "";
       var comParams="";
       for(var i=0;i<mfRecs.length;i++){
           if(editJson == ""){
               editJson = '{"data":'+Ext.JSON.encode(mfRecs[i].data)+'}';
           }else{
               editJson = editJson + ',{"data":'+Ext.JSON.encode(mfRecs[i].data)+'}';
           }
       }
       if(editJson != ""){
           if(comParams == ""){
               comParams = '"update":[' + editJson + "]";
           }else{
               comParams = comParams + ',"update":[' + editJson + "]";
           }
       }
       //提交参数
       var tzParams="";
       if(comParams!=""){
           tzParams= '{"ComID":"TZ_BMGL_BMBSH_COM","PageID":"TZ_BMGL_XMSJG_STD","OperateType":"U","comParams":{'+comParams+'}}';
       }
           var comView = this.getView();
           if(tzParams!=""){
               Ext.tzSubmit(tzParams,function(responseData){
                   store.commitChanges();
               },"",true,this);
           }
       btn.findParentByType("grid").close();
    },
    viewMsNotice:function(grid, rowIndex, colIndex){
        var record = grid.store.getAt(rowIndex);
        var classId = record.data.classId;
        var mshId = record.data.mshId;
        var appins = record.data.appIns;
        var oprid = record.data.oprid;
        var url = TzUniversityContextPath + "/dispatcher?classid=viewmstz&TZ_APP_INS_ID="+appins+"&TZ_MSH_ID="+mshId+"&TZ_CLASS_ID="+classId+"&OPRID="+oprid;
		window.open(url, '_blank');
    },
    viewMsResult:function(grid, rowIndex, colIndex){
        var record = grid.store.getAt(rowIndex);
        var classId = record.data.classID;
        var mshId = record.data.mshId;
        var appins = record.data.appIns;
        var oprid = record.data.oprid;
        var url = TzUniversityContextPath + "/dispatcher?classid=viewmsjg&TZ_APP_INS_ID="+appins+"&TZ_MSH_ID="+mshId+"&TZ_CLASS_ID="+classId+"&OPRID="+oprid;
		window.open(url, '_blank');
    },
    viewXMsResult:function(grid, rowIndex, colIndex){
        var record = grid.store.getAt(rowIndex);
        var classId = record.data.classID;
        var mshId = record.data.mshId;
        var appins = record.data.appIns;
        var oprid = record.data.oprid;
        var url = TzUniversityContextPath + "/dispatcher?classid=viewxmsjg&TZ_APP_INS_ID="+appins+"&TZ_MSH_ID="+mshId+"&TZ_CLASS_ID="+classId+"&OPRID="+oprid;
		window.open(url, '_blank');
    },
    viewClqResult:function(grid, rowIndex, colIndex){
        var record = grid.store.getAt(rowIndex);
        var classId = record.data.classId;
        var mshId = record.data.mshId;
        var appins = record.data.appIns;
        var oprid = record.data.oprid;
        var url = TzUniversityContextPath + "/dispatcher?classid=viewlqjg&TZ_APP_INS_ID="+appins+"&TZ_MSH_ID="+mshId+"&TZ_CLASS_ID="+classId+"&OPRID="+oprid+"&isAdmin=Y";
		window.open(url, '_blank');
    },
    saveMszg:function(btn){

        //面试资格列表
    	 var grid = btn.findParentByType("grid");
        //面试资格数据
        var store = grid.getStore();
        //获取修改的记录
        var mfRecs = store.getModifiedRecords();
        var editJson = "";
        var comParams="";
        for(var i=0;i<mfRecs.length;i++){
            if(editJson == ""){
                editJson = '{"data":'+Ext.JSON.encode(mfRecs[i].data)+'}';
            }else{
                editJson = editJson + ',{"data":'+Ext.JSON.encode(mfRecs[i].data)+'}';
            }
        }
        if(editJson != ""){
            if(comParams == ""){
                comParams = '"update":[' + editJson + "]";
            }else{
                comParams = comParams + ',"update":[' + editJson + "]";
            }
        }
        //提交参数
        var tzParams="";
        if(comParams!=""){
            tzParams= '{"ComID":"TZ_BMGL_BMBSH_COM","PageID":"TZ_BMGL_MSZG_STD","OperateType":"U","comParams":{'+comParams+'}}';
        }
            var comView = this.getView();
            if(tzParams!=""){
                Ext.tzSubmit(tzParams,function(responseData){
                	comView.actType = "update";
                    store.reload();
                },"",true,this);
            }
    
    },
    ensureMszg:function(btn){
    	this.saveMszg(btn);
    	var comView = this.getView();
        comView.close();
    },
    publishMszgrcInfo:function(btn){
    	
    	var grid = btn.findParentByType("grid");
    	var store = grid.getStore();
    	var mfRecs = store.getModifiedRecords();
    	if(mfRecs.length!=0){
    		Ext.MessageBox.alert("提示","请先保存页面信息。");
            return;
    	}
    	var selList = grid.getSelectionModel().getSelection();
        if (selList.length === 0 ) {
            Ext.Msg.alert("提示", "请选择要发布的记录");
            return;
        }
        var editJson="";
        for(var i=0;i<selList.length;i++){
            if(editJson == ""){
                editJson = Ext.JSON.encode(selList[i].data);
            }else{
                editJson = editJson + ','+Ext.JSON.encode(selList[i].data);
            }
        }
        comParams = '"data":[' + editJson + "]";

        //提交参数
        var tzParams="";
        if(comParams!=""){
            tzParams= '{"ComID":"TZ_BMGL_BMBSH_COM","PageID":"TZ_BMGL_MSZG_STD","OperateType":"publish","comParams":{'+comParams+'}}';
        }
            var comView = this.getView();
            if(tzParams!=""){
                Ext.tzSubmit(tzParams,function(responseData){
                    store.reload();
                },"发布成功",true,this);
            }
    
    
    },
    cancelMszgrcInfo:function(btn){
    	var grid = btn.findParentByType("grid");
    	var store = grid.getStore();
    	var selList = grid.getSelectionModel().getSelection();
        if (selList.length === 0 ) {
            Ext.Msg.alert("提示", "请选择要撤销发布的记录");
            return;
        }
        var editJson="";
        for(var i=0;i<selList.length;i++){
            if(editJson == ""){
                editJson = Ext.JSON.encode(selList[i].data);
            }else{
                editJson = editJson + ','+Ext.JSON.encode(selList[i].data);
            }
        }
        comParams = '"data":[' + editJson + "]";

        //提交参数
        var tzParams="";
        if(comParams!=""){
            tzParams= '{"ComID":"TZ_BMGL_BMBSH_COM","PageID":"TZ_BMGL_MSZG_STD","OperateType":"cancel","comParams":{'+comParams+'}}';
        }
            var comView = this.getView();
            if(tzParams!=""){
                Ext.tzSubmit(tzParams,function(responseData){
                    store.reload();
                },"撤销成功",true,this);
            }
    
    
    
    },
    publishMsjgInfo:function(btn){
    	var grid = btn.findParentByType("grid");
    	var store = grid.getStore();
    	var mfRecs = store.getModifiedRecords();
    	if(mfRecs.length!=0){
    		Ext.MessageBox.alert("提示","请先保存页面信息。");
            return;
    	}
    	var selList = grid.getSelectionModel().getSelection();
        if (selList.length === 0 ) {
            Ext.Msg.alert("提示", "请选择要发布的记录");
            return;
        }
        var editJson="";
        for(var i=0;i<selList.length;i++){
            if(editJson == ""){
                editJson = Ext.JSON.encode(selList[i].data);
            }else{
                editJson = editJson + ','+Ext.JSON.encode(selList[i].data);
            }
        }
        comParams = '"data":[' + editJson + "]";

        //提交参数
        var tzParams="";
        if(comParams!=""){
            tzParams= '{"ComID":"TZ_BMGL_BMBSH_COM","PageID":"TZ_BMGL_MSJG_STD","OperateType":"publish","comParams":{'+comParams+'}}';
        }
            var comView = this.getView();
            if(tzParams!=""){
                Ext.tzSubmit(tzParams,function(responseData){
                    store.reload();
                },"发布成功",true,this);
            }
    
    },
    publishXMsjgInfo:function(btn){
    	var grid = btn.findParentByType("grid");
    	var store = grid.getStore();
    	var mfRecs = store.getModifiedRecords();
    	if(mfRecs.length!=0){
    		Ext.MessageBox.alert("提示","请先保存页面信息。");
            return;
    	}
    	var selList = grid.getSelectionModel().getSelection();
        if (selList.length === 0 ) {
            Ext.Msg.alert("提示", "请选择要发布的记录");
            return;
        }
        var editJson="";
        for(var i=0;i<selList.length;i++){
            if(editJson == ""){
                editJson = Ext.JSON.encode(selList[i].data);
            }else{
                editJson = editJson + ','+Ext.JSON.encode(selList[i].data);
            }
        }
        comParams = '"data":[' + editJson + "]";

        //提交参数
        var tzParams="";
        if(comParams!=""){
            tzParams= '{"ComID":"TZ_BMGL_BMBSH_COM","PageID":"TZ_BMGL_XMSJG_STD","OperateType":"publish","comParams":{'+comParams+'}}';
        }
            var comView = this.getView();
            if(tzParams!=""){
                Ext.tzSubmit(tzParams,function(responseData){
                    store.reload();
                },"发布成功",true,this);
            }
    
    },
    cancelMsjgInfo:function(btn){
    	var grid = btn.findParentByType("grid");
    	var store = grid.getStore();
    	var selList = grid.getSelectionModel().getSelection();
        if (selList.length === 0 ) {
            Ext.Msg.alert("提示", "请选择要撤销发布的记录");
            return;
        }
        var editJson="";
        for(var i=0;i<selList.length;i++){
            if(editJson == ""){
                editJson = Ext.JSON.encode(selList[i].data);
            }else{
                editJson = editJson + ','+Ext.JSON.encode(selList[i].data);
            }
        }
        comParams = '"data":[' + editJson + "]";

        //提交参数
        var tzParams="";
        if(comParams!=""){
            tzParams= '{"ComID":"TZ_BMGL_BMBSH_COM","PageID":"TZ_BMGL_MSJG_STD","OperateType":"cancel","comParams":{'+comParams+'}}';
        }
            var comView = this.getView();
            if(tzParams!=""){
                Ext.tzSubmit(tzParams,function(responseData){
                    store.reload();
                },"撤销成功",true,this);
            }
    
    
    
    },
    cancelXMsjgInfo:function(btn){
    	var grid = btn.findParentByType("grid");
    	var store = grid.getStore();
    	var selList = grid.getSelectionModel().getSelection();
        if (selList.length === 0 ) {
            Ext.Msg.alert("提示", "请选择要撤销发布的记录");
            return;
        }
        var editJson="";
        for(var i=0;i<selList.length;i++){
            if(editJson == ""){
                editJson = Ext.JSON.encode(selList[i].data);
            }else{
                editJson = editJson + ','+Ext.JSON.encode(selList[i].data);
            }
        }
        comParams = '"data":[' + editJson + "]";

        //提交参数
        var tzParams="";
        if(comParams!=""){
            tzParams= '{"ComID":"TZ_BMGL_BMBSH_COM","PageID":"TZ_BMGL_XMSJG_STD","OperateType":"cancel","comParams":{'+comParams+'}}';
        }
            var comView = this.getView();
            if(tzParams!=""){
                Ext.tzSubmit(tzParams,function(responseData){
                    store.reload();
                },"撤销成功",true,this);
            }
    
    
    
    },
    saveBscj:function(btn){

        //bscj列表
    	 var grid = btn.findParentByType("grid");
        //笔试成绩数据
        var store = grid.getStore();
        //获取修改的记录
        var mfRecs = store.getModifiedRecords();
        var editJson = "";
        var comParams="";
        for(var i=0;i<mfRecs.length;i++){
            if(editJson == ""){
                editJson = '{"data":'+Ext.JSON.encode(mfRecs[i].data)+'}';
            }else{
                editJson = editJson + ',{"data":'+Ext.JSON.encode(mfRecs[i].data)+'}';
            }
        }
        if(editJson != ""){
            if(comParams == ""){
                comParams = '"update":[' + editJson + "]";
            }else{
                comParams = comParams + ',"update":[' + editJson + "]";
            }
        }
        //提交参数
        var tzParams="";
        if(comParams!=""){
            tzParams= '{"ComID":"TZ_BMGL_BMBSH_COM","PageID":"TZ_BMGL_BSCJ_STD","OperateType":"U","comParams":{'+comParams+'}}';
        }
            var comView = this.getView();
            if(tzParams!=""){
                Ext.tzSubmit(tzParams,function(responseData){
                	comView.actType = "update";
                    store.reload();
                },"",true,this);
            }
    
    
    },
    ensureBscj:function(btn){
    	this.saveBscj(btn);
    	var comView = this.getView();
        comView.close();
    },
    publishBscjInfo:function(btn){

    	var grid = btn.findParentByType("grid");
    	var store = grid.getStore();
    	var mfRecs = store.getModifiedRecords();
    	if(mfRecs.length!=0){
    		Ext.MessageBox.alert("提示","请先保存页面信息。");
            return;
    	}
    	var selList = grid.getSelectionModel().getSelection();
        if (selList.length === 0 ) {
            Ext.Msg.alert("提示", "请选择要发布的记录");
            return;
        }
        var editJson="";
        for(var i=0;i<selList.length;i++){
            if(editJson == ""){
                editJson = Ext.JSON.encode(selList[i].data);
            }else{
                editJson = editJson + ','+Ext.JSON.encode(selList[i].data);
            }
        }
        comParams = '"data":[' + editJson + "]";

        //提交参数
        var tzParams="";
        if(comParams!=""){
            tzParams= '{"ComID":"TZ_BMGL_BMBSH_COM","PageID":"TZ_BMGL_BSCJ_STD","OperateType":"publish","comParams":{'+comParams+'}}';
        }
            var comView = this.getView();
            if(tzParams!=""){
                Ext.tzSubmit(tzParams,function(responseData){
                    store.reload();
                },"发布成功",true,this);
            }
    },
    cancelBscjInfo:function(btn){
    	var grid = btn.findParentByType("grid");
    	var store = grid.getStore();
    	var selList = grid.getSelectionModel().getSelection();
        if (selList.length === 0 ) {
            Ext.Msg.alert("提示", "请选择要发布的记录");
            return;
        }
        var editJson="";
        for(var i=0;i<selList.length;i++){
            if(editJson == ""){
                editJson = Ext.JSON.encode(selList[i].data);
            }else{
                editJson = editJson + ','+Ext.JSON.encode(selList[i].data);
            }
        }
        comParams = '"data":[' + editJson + "]";

        //提交参数
        var tzParams="";
        if(comParams!=""){
            tzParams= '{"ComID":"TZ_BMGL_BMBSH_COM","PageID":"TZ_BMGL_BSCJ_STD","OperateType":"cancel","comParams":{'+comParams+'}}';
        }
            var comView = this.getView();
            if(tzParams!=""){
                Ext.tzSubmit(tzParams,function(responseData){
                    store.reload();
                },"撤销成功",true,this);
            }
    
    },
    saveLqjg:function(btn){

   	 //lqjg列表
  	 var grid = btn.findParentByType("grid");
      //lqjg数据
      var store = grid.getStore();
      //获取修改的记录
      var mfRecs = store.getModifiedRecords();
      var editJson = "";
      var comParams="";
      for(var i=0;i<mfRecs.length;i++){
          if(editJson == ""){
              editJson = '{"data":'+Ext.JSON.encode(mfRecs[i].data)+'}';
          }else{
              editJson = editJson + ',{"data":'+Ext.JSON.encode(mfRecs[i].data)+'}';
          }
      }
      if(editJson != ""){
          if(comParams == ""){
              comParams = '"update":[' + editJson + "]";
          }else{
              comParams = comParams + ',"update":[' + editJson + "]";
          }
      }
    	//提交参数
        var tzParams="";
        if(comParams!=""){
            tzParams= '{"ComID":"TZ_BMGL_BMBSH_COM","PageID":"TZ_BMGL_LQJG_STD","OperateType":"U","comParams":{'+comParams+'}}';
        }
            var comView = this.getView();
            if(tzParams!=""){
                Ext.tzSubmit(tzParams,function(responseData){
                	comView.actType = "update";
                    store.reload();
                },"",true,this);
            }
    },
    ensureLqjg:function(btn){
    	this.saveLqjg(btn);
    	var comView= this.getView();
    	comView.close();
    },
    publishLqjgInfo:function(btn){

    	var grid = btn.findParentByType("grid");
    	var store = grid.getStore();
    	var mfRecs = store.getModifiedRecords();
    	if(mfRecs.length!=0){
    		Ext.MessageBox.alert("提示","请先保存页面信息。");
            return;
    	}
    	var selList = grid.getSelectionModel().getSelection();
        if (selList.length === 0 ) {
            Ext.Msg.alert("提示", "请选择要发布的记录");
            return;
        }
        var editJson="";
        for(var i=0;i<selList.length;i++){
            if(editJson == ""){
                editJson = Ext.JSON.encode(selList[i].data);
            }else{
                editJson = editJson + ','+Ext.JSON.encode(selList[i].data);
            }
        }
        comParams = '"data":[' + editJson + "]";
    
    	  //提交参数
        var tzParams="";
        if(comParams!=""){
            tzParams= '{"ComID":"TZ_BMGL_BMBSH_COM","PageID":"TZ_BMGL_LQJG_STD","OperateType":"publish","comParams":{'+comParams+'}}';
        }
            var comView = this.getView();
            if(tzParams!=""){
                Ext.tzSubmit(tzParams,function(responseData){
                    store.reload();
                },"发布成功",true,this);
            }
    },
    cancelLqjgInfo:function(btn){

    	var grid = btn.findParentByType("grid");
    	var store = grid.getStore();
    	var mfRecs = store.getModifiedRecords();
    	if(mfRecs.length!=0){
    		Ext.MessageBox.alert("提示","请先保存页面信息。");
            return;
    	}
    	var selList = grid.getSelectionModel().getSelection();
        if (selList.length === 0 ) {
            Ext.Msg.alert("提示", "请选择要发布的记录");
            return;
        }
        var editJson="";
        for(var i=0;i<selList.length;i++){
            if(editJson == ""){
                editJson = Ext.JSON.encode(selList[i].data);
            }else{
                editJson = editJson + ','+Ext.JSON.encode(selList[i].data);
            }
        }
        comParams = '"data":[' + editJson + "]";
    
  	  //提交参数
      var tzParams="";
      if(comParams!=""){
          tzParams= '{"ComID":"TZ_BMGL_BMBSH_COM","PageID":"TZ_BMGL_LQJG_STD","OperateType":"cancel","comParams":{'+comParams+'}}';
      }
          var comView = this.getView();
          if(tzParams!=""){
              Ext.tzSubmit(tzParams,function(responseData){
                  store.reload();
              },"撤销成功",true,this);
          }
    },
    download:function(grid, rowIndex, colIndex){
        var record = grid.store.getAt(rowIndex);
        var mshId = record.data.mshId;
        var oprid = record.data.oprid;
        var downloadUrl="";
        var tzParams = '{"ComID":"TZ_BMGL_BMBSH_COM","PageID":"TZ_BMGL_MSJG_STD","OperateType":"DOWNLOAD","comParams":{"OPRID":"'+oprid+'","TZ_MSH_ID":"'+mshId+'"}}';
        Ext.tzLoad(tzParams,function(responseData){
        	downloadUrl = responseData.formData;
        	if(downloadUrl==""||downloadUrl=="nullnull"){
            	Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_UPLOAD_STD.prompt","提示"), "推荐信未上传");
            }else{
            	window.open(downloadUrl, '_blank');
            }
        });
        
        
    },
    upload:function(grid, rowIndex, colIndex){
    	//是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_BMGL_BMBSH_COM"]["TZ_BMGL_UPLOAD_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_UPLOAD_STD.prompt","提示"), Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_UPLOAD_STD.nmyqx","您没有权限"));
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_UPLOAD_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_UPLOAD_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
            return;
        }
    	 var record = grid.store.getAt(rowIndex);
         var downloadUrl = record.data.downloadCN;
         var mshId = record.data.mshId;
         var oprid = record.data.oprid;
        var win = this.lookupReference('uploadCommitLetter');

        if (!win) {
            Ext.syncRequire(className);
            ViewClass = Ext.ClassManager.get(className);
            //新建类
            win = new ViewClass();
            this.getView().add(win);
        }

        var winForm = win.child("form").getForm();
        winForm.findField("currentRowIndex").setValue(rowIndex);
        winForm.findField("oprid").setValue(oprid);
		winForm.findField("mshId").setValue(mshId);
        win.show();
    },
    onUploadComLetterEnsure:function(btn){
    	  //获取窗口
        var win = btn.findParentByType("window");

        var form = win.child("form").getForm();

        /*获取页面数据*/
        var mshId = form.findField("mshId").getValue();
        var oprid = form.findField("oprid").getValue();
        var filename = form.findField("orguploadfile").getValue();
        if(filename != ""){
        	var upUrl = TzUniversityContextPath +  '/UpdServlet?filePath=letterAttachment';
        	 var myMask = new Ext.LoadMask({
                 msg    : Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_UPLOAD_STD.loading","上传中..."),
                 target : Ext.getCmp('tranzvision-framework-content-panel')
             });
        	 myMask.show();
        	 form.submit({
        		url:upUrl,
        		success:function(form, action) {
        				var filePath = "";
        				var fileSysFileName = "";
        				var fileUserFileName = "";
        				filePath = action.result.msg.accessPath;
        				fileSysFileName = action.result.msg.sysFileName;
        				fileUserFileName = action.result.msg.filename;
        				var tzSubmitParams = '{"ComID":"TZ_BMGL_BMBSH_COM","PageID":"TZ_BMGL_MSJG_STD","OperateType":"UPLOAD","comParams":{"OPRID":"'+oprid+'","TZ_MSH_ID":"'+mshId+'","filePath":"'+filePath+'","fileSysFileName":"'+fileSysFileName+'","fileUserFileName":"'+fileUserFileName+'"}}';
        				$.ajax({
        		            type: "post",   
        		            url: Ext.tzGetGeneralURL(),
        		            data: {tzParams: tzSubmitParams}, 
        		            dataType: 'json',  
        		            success: function(result) {
        						if(result.comContent.resultFlg =="success"){
        							Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_UPLOAD_STD.prompt","提示"), "上传成功");
        						}else{
        							
        						}
        		            }
				        	 })
        			myMask.hide();
                    form.reset();
        		},
        		failure: function (form, action) {
                    myMask.hide();
                    Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_UPLOAD_STD.error","错误"), action.result.msg);
                }
        	 });
        	 win.close();
        	 
        }
        
    },
    onUploadComLetterClose:function(btn){
    	 var win = btn.findParentByType("window");
         win.close();
    },
    batchDownload:function(btn){
    	var store = btn.findParentByType("grid").store;

//		var strConfSearCond=store.tzStoreParams;
//		if (strConfSearCond.length==0) {
//			strConfSearCond="{\"cfgSrhId\":\"TZ_BMGL_BMBSH_COM.TZ_BMGL_MSJG_STD.TZ_MSJG_MAN_V\",\"condition\": {\"TZ_CLASS_ID-operator\": \"01\", \"TZ_CLASS_ID-value\":\""+store.classID +"\"}}";
//			}
//		
//        //顾贤达 2017年6月26日 11:08:54 修改SQL提交逻辑
//		var tzParams = '{"ComID":"TZ_BMGL_BMBSH_COM","PageID":"TZ_BMGL_MSJG_STD","OperateType":"tzConfigSearchSQL","comParams":'+strConfSearCond+'}';
//		var strMshId="";
//		Ext.tzLoadAsync(tzParams,function(resp){
//			 if(resp.formData!=undefined&&resp.formData!=""){
//				 strMshId =resp.formData;
//	            }
//	    },"",true,this);
    	var strMshId = "";
    	var count = store.getCount();
    	for (var i=0; i<count;i++){
    		if(strMshId==""){
    			strMshId=store.getAt(i).get('mshId');
    		}else{
    			strMshId=strMshId+";"+store.getAt(i).get('mshId');
    		}
    	}
		
		 strMshId=strMshId+";";
        
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_BMGL_BMBSH_COM"]["TZ_BMGL_DOWN_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_DOWN_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.nmyqx","您没有权限"));
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_DOWN_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
            return;
        }
        var win = this.lookupReference('downloadCommit');
        
        if (!win) {
        	 Ext.syncRequire(className);
             ViewClass = Ext.ClassManager.get(className);
            //新建类
            win = new ViewClass();
            this.getView().add(win);
        }

        //操作类型设置为新增
        win.actType = "cnsAdd";
        var form = win.child("form").getForm();
        form.reset();
        form.setValues({mshId:"'"+strMshId+"'"});
        
        win.show();
        
        getAppIdSQL="";

    },
    selectedDownload:function(btn){
    	//var store = btn.findParentByType("grid").store;
    	var selList = btn.findParentByType("grid").getSelectionModel().getSelection();
        if (selList.length === 0 ) {
            Ext.Msg.alert("提示", "请选择要下载承诺书的记录");
            return;
        }
        var strMshId="";
        for(var i=0;i<selList.length;i++){
            if(strMshId == ""){
            	strMshId = selList[i].get('mshId');
            }else{
            	strMshId = strMshId + ';'+selList[i].get('mshId');
            }
        }
        strMshId=strMshId+";";
        console.log(strMshId);
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_BMGL_BMBSH_COM"]["TZ_BMGL_DOWN_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_DOWN_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.nmyqx","您没有权限"));
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_DOWN_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
            return;
        }
        var win = this.lookupReference('downloadCommit');
        
        if (!win) {
        	 Ext.syncRequire(className);
             ViewClass = Ext.ClassManager.get(className);
            //新建类
            win = new ViewClass();
            this.getView().add(win);
        }

        //操作类型设置为新增
        win.actType = "cnsAdd";
        var form = win.child("form").getForm();
        form.reset();
        form.setValues({mshId:"'"+strMshId+"'"});
        
        win.show();
        
    },
    
    
    //下载导入模板
    downloadImportTpl: function(btn){
    	var stuGrid = btn.findParentByType("grid");
		var selList = stuGrid.getSelectionModel().getSelection();

		if(selList.length<1) {
			Ext.MessageBox.alert("提示", "请选择需要导入的学生记录");
			return;
		};
		
		var expArr = [];
		for(var i=0; i<selList.length; i++){
			expArr.push(selList[i].data);
		}
		var tzParamsObj = {
        		ComID: "TZ_BMGL_BMBSH_COM",
        		PageID: "TZ_BMGL_MSJG_STD",
        		OperateType: "downloadImportTpl",
        		comParams:{
        			selData: expArr
        		}
        	};
        var tzParams = Ext.JSON.encode(tzParamsObj);
		Ext.tzLoad(tzParams,function(respData){
			var fileUrl = respData.fileUrl;
			if(fileUrl != ""){
				window.open(fileUrl, "download","status=no,menubar=yes,toolbar=no,location=no");
			}else{
				Ext.Msg.alert("提示","下载失败，文件不存在");
			}
		});
    },
    
    //批量导入面试信息
    importInterviewInfo: function(btn){
    	
    	var stuGrid = btn.findParentByType("grid");
		var panel = stuGrid.up('mszgInfoForm');
//		var classID = panel.classID;
		
		var className = "KitchenSink.view.enrollmentManagement.applicationMsMan.importMsIsBan";
		if(!Ext.ClassManager.isCreated(className)){
            Ext.syncRequire(className);
        }
		ViewClass = Ext.ClassManager.get(className);
		
		var win = new ViewClass({
//			classID: classID,
			callback: function(){
				stuGrid.getStore().reload();
			}
		});
		win.show();
    },
    //确认导入
    onEnsureImportMsInfo: function(btn){
    	var win = btn.findParentByType('importMsInfoWin');
		var form = win.child('form').getForm();

		if(form.isValid()){
			var classID = win.classID;
			
			var filePath = 'tmpFileUpLoad';
            var updateUrl = TzUniversityContextPath + '/UpdServlet?filePath='+filePath;

            form.submit({
                url: updateUrl,
                waitMsg: '正在上传Excel...',
                success: function (form, action) {
                    var sysFileName = action.result.msg.sysFileName;
                    var path = action.result.msg.accessPath;
                    
                    /*后台解析Excsel*/
                    Ext.MessageBox.show({
                        msg: '解析数据中，请稍候...',
                        progress: true,
                        progressText:'解析中...',
                        width: 300,
                        wait: {
                            interval: 50
                        }
                    });
                    
                    var tzParamsObj = {
                    		ComID: "TZ_BMGL_BMBSH_COM",
                    		PageID: "TZ_BMGL_MSJG_STD",
                    		OperateType: "importStuMsInfo",
                    		comParams:{
                    			classID: classID,
                    			path: path,
                    			sysFileName: sysFileName
                    		}
                    	};
                    var tzParams = Ext.JSON.encode(tzParamsObj);
                    
                    Ext.tzLoad(tzParams,function(respData){
                    	Ext.MessageBox.hide();
                    	
                        if(respData.result == 'success'){
                        	win.close();
                        	win.GridReload();
                        	
                        	if(respData.message != ""){
                        		Ext.Msg.alert("提示",respData.message);
                        	}
                        }
                    });
                },
                failure: function (form, action) {
                    Ext.MessageBox.alert("错误", "文件上传失败");
                    return false;
                }
            });
		}
    }
    
    
});

