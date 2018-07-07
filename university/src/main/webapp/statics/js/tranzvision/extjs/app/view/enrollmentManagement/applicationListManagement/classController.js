Ext.define('KitchenSink.view.enrollmentManagement.applicationListManagement.classController', {
    extend: 'Ext.app.ViewController',
    requires:['Ext.ux.IFrame'],
    alias: 'controller.appBatchClass',
    queryClass:function(btn){
        Ext.tzShowCFGSearch({
            cfgSrhId: 'TZ_PS_MGR_COM.TZ_PS_MGR_STD.PS_TZ_CLPS_MGR_VW',
            condition:{
                TZ_DLZH_ID:TranzvisionMeikecityAdvanced.Boot.loginUserId,
                TZ_JG_ID:Ext.tzOrgID
            },
            callback: function(seachCfg){
                var store = btn.findParentByType("grid").store;
                store.tzStoreParams = seachCfg;
                store.load();
            }
        });
    },
    addClass: function(btn) {
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_PS_MGR_COM"]["TZ_PS_ADD_STD"];
        if( pageResSet == "" || pageResSet == undefined){
        	Ext.MessageBox.alert(Ext.tzGetResourse("TZ_PS_MGR_COM.TZ_PS_MGR_STD.prompt","提示"),Ext.tzGetResourse("TZ_PS_MGR_COM.TZ_PS_MGR_STD.nmyqx","您没有权限"));
        	return;
        }
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_PS_MGR_COM.TZ_PS_MGR_STD.prompt","提示"),Ext.tzGetResourse("TZ_PS_MGR_COM.TZ_PS_MGR_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
            return;
        }

        var win = this.lookupReference('classAddWindow');
        if (!win) {
            Ext.syncRequire(className);
            ViewClass = Ext.ClassManager.get(className);
            win = new ViewClass();
            var form = win.child('form').getForm();

            this.getView().add(win);
        }
        win.show();

    },
    pmtSearchClass: function(btn){
		var form = btn.findParentByType("window").child("form").getForm();
		Ext.tzShowPromptSearch({
			recname: 'PS_TZ_CLS_MGR_VW',
			searchDesc: '搜索班级信息',
			maxRow:20,
			condition:{
				presetFields:{
					TZ_JG_ID:{
                        value: Ext.tzOrgID,
                        type: '01'
                    },
                    TZ_DLZH_ID:{
                    	value:TranzvisionMeikecityAdvanced.Boot.loginUserId,
                    	type:'01'
                    }
				},
				srhConFields:{
					TZ_CLASS_ID:{
						desc:'班级ID',
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
				TZ_CLASS_ID: '班级ID',
				TZ_CLASS_NAME: '班级名称'	
			},
			multiselect: false,
			callback: function(selection){
				form.findField("classID").setValue(selection[0].data.TZ_CLASS_ID);
				form.findField("className").setValue(selection[0].data.TZ_CLASS_NAME);
			}
		});	
	},
	pmtSearchBatch: function(btn){
		var form = btn.findParentByType("window").child("form").getForm();
		var classID = form.findField("classID").getValue();
		if(classID==null||classID==""){
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_PS_MGR_COM.TZ_PS_MGR_STD.prompt","提示"),Ext.tzGetResourse("TZ_PS_MGR_COM.TZ_PS_MGR_STD.selectClass","请选择班级"));
			return;
		}
		Ext.tzShowPromptSearch({
			recname: 'PS_TZ_BATCH_MGR_VW',
			searchDesc: '搜索班级信息',
			maxRow:20,
			condition:{
				presetFields:{
                    TZ_CLASS_ID:{
                    	value:classID,
                    	type:'01'
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
	/*评审进度管理*/
    clReviewScheduleMg:function(grid,rowIndex){
    	Ext.tzSetCompResourses("TZ_REVIEW_CL_COM");
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_REVIEW_CL_COM"]["TZ_CLPS_SCHE_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有管理评审进度的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_CLPS_SCHE_STD，请检查配置。');
            return;
        }
        var contentPanel, cmp, ViewClass, clsProto;

        contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
        contentPanel.body.addCls('kitchensink-example');

        if(!Ext.ClassManager.isCreated(className)){
            Ext.syncRequire(className);
        }

        ViewClass = Ext.ClassManager.get(className);
        //ViewClass = new KitchenSink.view.materialsReview.materialsReview.materialsReviewSchedule();
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

        var record = grid.store.getAt(rowIndex);
        var classID = record.get('classID');
        var batchID = record.get('batchID');
        var adminFlag = record.get('adminFlag');
        
        cmp = new ViewClass(classID,batchID,adminFlag);

        cmp.on('afterrender',function(panel){
            var judgeStore =panel.down('tabpanel').child("form[name=judgeInfoForm]").child('grid').store,
                judgeParams = '{"type":"judgeInfo","classID":"'+classID+'","batchID":"'+batchID+'"}',
                form = panel.child('form').getForm();
            var stuListStore = panel.down('tabpanel').child('grid[name=materialsStudentGrid]').store,
                stuListParams = '{"type":"stuList","classID":"'+classID+'","batchID":"'+batchID+'"}';
            var tzParams ='{"ComID":"TZ_REVIEW_CL_COM","PageID":"TZ_CLPS_SCHE_STD",' +
                '"OperateType":"QF","comParams":{"classID":"'+classID+'","batchID":"'+batchID+'"}}';
            Ext.tzLoad(tzParams,function(respData){
                respData.className = record.data.className;
                respData.batchName = record.data.batchName;
                form.setValues(respData);
                var formButton =panel.child('form');
                var btnStartNewReview=formButton.down('button[name=startNewReview]'),
                    btnCloseReview=formButton.down('button[name=closeReview]');
                if(respData.status=='进行中'){
                    btnStartNewReview.flagType='positive';
                    btnCloseReview.flagtype='positive';
                    btnStartNewReview.setDisabled(true);
                }
                if(respData.status=='已关闭'){
                    btnStartNewReview.flagType='positive';
                    btnCloseReview.flagtype='negative';
                    btnCloseReview.setDisabled(true);
                }
                if(respData.status=='未开始'){
                    btnStartNewReview.flagType='negative';
                    btnCloseReview.flagtype='negative';
                    btnCloseReview.setDisabled(true);
                }
                if(respData.delibCount==0){
                    btnStartNewReview.flagType='positive';
                    btnCloseReview.flagtype='negative';
                    btnCloseReview.setDisabled(true);
                }
            });
            judgeStore.tzStoreParams = judgeParams;
            judgeStore.load();
            stuListStore.tzStoreParams = stuListParams;
        });

        tab = contentPanel.add(cmp);

        contentPanel.setActiveTab(tab);

        Ext.resumeLayouts(true);

        if (cmp.floating) {
            cmp.show();
        }
    }
});

