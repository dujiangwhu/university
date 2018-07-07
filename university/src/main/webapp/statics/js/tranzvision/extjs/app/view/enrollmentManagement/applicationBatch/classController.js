﻿Ext.define('KitchenSink.view.enrollmentManagement.applicationBatch.classController', {
    extend: 'Ext.app.ViewController',
    requires:['Ext.ux.IFrame'],
    alias: 'controller.appBatchClass',
    queryClass:function(btn){
    	var config=btn.findParentByType("panel").config;
        Ext.tzShowCFGSearch({
            cfgSrhId: 'TZ_BMGL_BMBPICI_COM.TZ_BMGL_CLASS_STD.TZ_CLASS_BATCH_V',
            condition:{
                TZ_DLZH_ID:TranzvisionMeikecityAdvanced.Boot.loginUserId,
                TZ_JG_ID:Ext.tzOrgID,
                TZ_PRJ_TYPE_ID:config.prjID
            },
            callback: function(seachCfg){
                var store = btn.findParentByType("grid").store;
                store.tzStoreParams = seachCfg;
                store.load();
            }
        });
    },
    queryStudents:function(btn){
    	var store = btn.findParentByType("grid").store;
    	var getOpridSQL2;
    	var getAppIdSQL2;
    	
        Ext.tzShowCFGSearch({
            cfgSrhId: 'TZ_BMGL_BMBPICI_COM.TZ_BMGL_STU_STD.TZ_APP_LIST_VW',
            condition:{
                TZ_CLASS_ID:store.classID,
                TZ_BATCH_ID:store.batchID
            },
            callback: function(seachCfg){
                var tzStoreParams = Ext.decode(seachCfg);
                tzStoreParams.classID = store.classID;
                tzStoreParams.batchID = store.batchID;
                store.tzStoreParams = Ext.encode(tzStoreParams);
                
                //顾贤达 2017年5月25日 11:06:26 添加全选 
                var tzParams = '{"ComID":"TZ_UM_USERMG_COM","PageID":"TZ_UM_GETSQL_STD","OperateType":"getQuerySQL","comParams":'+seachCfg+'}';
				Ext.tzLoadAsync(tzParams,function(responseData){
						getOpridSQL2= responseData.SQL;
						
						
						if((typeof store.batchID)!="undefined"){							
							getAppIdSQL2="SELECT TZ_APP_INS_ID FROM PS_TZ_APP_LIST_VW WHERE OPRID = ANY("+getOpridSQL2+") AND TZ_BATCH_ID='"+store.batchID+"'";
						}
						
						
						if((typeof store.classID)!="undefined"){
							getAppIdSQL2=getAppIdSQL2+" AND TZ_CLASS_ID='"+store.classID+"'";
						}
						
						console.log(getAppIdSQL2);
						
						window.getAppIdSQL2=getAppIdSQL2;
						btn.findParentByType('classInfoB').strSQL=getAppIdSQL2;
						btn.findParentByType('classInfoB').strConfSearCond=seachCfg;
							});
				
                store.load();
            }
        });
    },
    onStuInfoSave: function(btn){
    	var prjID= btn.findParentByType("panel").prjID;
        //学生信息列表
        var grid = this.getView().child("grid");
        //学生信息数据
        var store = grid.getStore();
        var form = this.getView().child("form").getForm();
        if (form.isValid()) {
            //获取学生列表参数
            var tzParams = this.getStuInfoParams(prjID);
            var comView = this.getView();
            if(tzParams!=""){
                Ext.tzSubmit(tzParams,function(responseData){
                    store.commitChanges();
                },"",true,this);
            }
        }
    },
    onStuInfoEnsure: function(btn){
    	var prjID= btn.findParentByType("panel").prjID;
        var form = this.getView().child("form").getForm();
        if (form.isValid()) {
            //获取学生列表参数
            var tzParams = this.getStuInfoParams(prjID);
            var comView = this.getView();
            if(tzParams!=""){
                Ext.tzSubmit(tzParams,function(responseData){
                    comView.close();
                },"",true,this);
            }else{
                comView.close();
            }
        }
    },
    getStuInfoParams: function(prjID){
        //更新操作参数
        var comParams = "";
        //学生信息列表
        var grid = this.getView().child("grid");
        //学生信息数据
        var store = grid.getStore();
        //修改json字符串
        var editJson = "";
        var mfRecs = store.getModifiedRecords();
        for(var i=0;i<mfRecs.length;i++){
            if(editJson == ""){
                editJson = '{"data":'+Ext.JSON.encode(mfRecs[i].data)+',"prjID":'+'"'+prjID+'"}';
            }else{
                editJson = editJson + ',{"data":'+Ext.JSON.encode(mfRecs[i].data)+',"prjID":'+'"'+prjID+'"}';
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
//            tzParams= '{"ComID":"TZ_BMGL_BMBSH_COM","PageID":"TZ_BMGL_STU_STD","OperateType":"U","comParams":{'+comParams+'}}';
        	 tzParams= '{"ComID":"TZ_BMGL_BMBPICI_COM","PageID":"TZ_BMGL_STU_STD","OperateType":"U","comParams":{'+comParams+'}}';
        }
        return tzParams;
    },
    onStuInfoClose: function(btn){
        //关闭窗口
        this.getView().close();
    },
    viewApplicants:function(grid, rowIndex, colIndex){
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_BMGL_BMBPICI_COM"]["TZ_BMGL_STU_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBPICI_COM.TZ_BMGL_STU_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBPICI_COM.TZ_BMGL_STU_STD.nmyqx","您没有权限"));
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBPICI_COM.TZ_BMGL_STU_STD.prompt","提示"), Ext.tzGetResourse("TZ_BMGL_BMBPICI_COM.TZ_BMGL_STU_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
            return;
        }
        var contentPanel, cmp, ViewClass, clsProto;

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
            // <debug warn>
            // Sometimes we forget to include allowances for other themes, so issue a warning as a reminder.
            if (!clsProto.themeInfo) {
                Ext.log.warn ( 'Example \'' + className + '\' lacks a theme specification for the selected theme: \'' +
                    themeName + '\'. Is this intentional?');
            }
            // </debug>
        }

        var record = grid.store.getAt(rowIndex);
        var classID = record.data.classID+"";
        var batchID = record.data.batchID;
        var prjID=config.prjID;
        var render = function(initialData){
        	
	        	cmp = new ViewClass({
	        		initialData:initialData,
	                classID:classID,
	                batchID:batchID,
	                prjID:prjID
		            }
		        );
		        cmp.on('afterrender',function(panel){
		            var form = panel.child('form').getForm();
		            var panelGrid = panel.child('grid');
		            panelGrid.getView().on('expandbody', function (rowNode, record, expandRow, eOpts){
		                if(!record.get('moreInfo')){
		                    var appInsID = record.get('appInsID');
		                    var tzExpandParams = '{"ComID":"TZ_BMGL_BMBPICI_COM","PageID":"TZ_BMGL_STU_STD","OperateType":"tzLoadExpandData","comParams":{"classID":"'+classID+'","appInsID":"'+appInsID+'"}}';
		                    Ext.tzLoad(tzExpandParams,function(respData){
		                            if(panelGrid.getStore().getModifiedRecords().length>0){
		                                record.set('moreInfo',respData);
		                            }else{
		                                record.set('moreInfo',respData);
		                                panelGrid.getStore().commitChanges( );
		                            }
		                        },panelGrid
		                    );
		                }
		            });
		            var tzParams = '{"ComID":"TZ_BMGL_BMBPICI_COM","PageID":"TZ_BMGL_STU_STD","OperateType":"QF","comParams":{"classID":"'+classID+'","batchID":"'+batchID+'"}}';
		            Ext.tzLoad(tzParams,function(respData){
		                var formData = respData.formData;
		                form.setValues(formData);
		
		                var tzStoreParams = {
		                		"classID":classID,
		                		"batchID":batchID,
		                		"cfgSrhId": "TZ_BMGL_BMBPICI_COM.TZ_BMGL_STU_STD.TZ_APP_LIST_VW",
		                		"condition":{
		                			"TZ_CLASS_ID-operator": "01",
		                			"TZ_CLASS_ID-value": classID,
		                			"TZ_BATCH_ID-operator": "01",
		                			"TZ_BATCH_ID-value": batchID
		                				}
		                };
		                panelGrid.store.classID=classID;
		                panelGrid.store.batchID=batchID;
		                panelGrid.store.tzStoreParams = Ext.encode(tzStoreParams);
		                panelGrid.store.load();
		            });
		        });
		
		        tab = contentPanel.add(cmp);
		
		        contentPanel.setActiveTab(tab);
		
		        Ext.resumeLayouts(true);
		
		        if (cmp.floating) {
		            cmp.show();
		        }
        };
        

        var submitStateStore = new KitchenSink.view.common.store.appTransStore("TZ_APPFORM_STATE"),
        	auditStateStore = new KitchenSink.view.common.store.appTransStore("TZ_AUDIT_STATE"),
        	tzIsMszgStore=new KitchenSink.view.common.store.appTransStore("TZ_IS_MSZG"),
        	orgColorSortStore = new KitchenSink.view.common.store.comboxStore({
                recname:'TZ_ORG_COLOR_V',
                condition:{
                    TZ_JG_ID:{
                        value:Ext.tzOrgID,
                        operator:'01',
                        type:'01'
                    }},
                result:'TZ_COLOR_SORT_ID,TZ_COLOR_NAME,TZ_COLOR_CODE'
            });
        
        //下拉项过滤器数据
        var colorSortFilterOptions=[],
        	submitStateFilterOptions=[],
        	auditStateFilterOptions=[],
        	tzIsMszgFilterOptions=[];
        
        //颜色类别初始化数据-学生颜色类别列渲染数据
        var initialColorSortData=[];
        
        //4个下拉控件Store加载完毕之后打开页面
        var times = 4;
        var beforeRender = function(){
        	times--;
	        if(times==0){
	        	render({
	        		submitStateStore:submitStateStore,
	        		auditStateStore:auditStateStore,
	        		orgColorSortStore:orgColorSortStore,
	        		tzIsMszgStore:tzIsMszgStore,
	        		colorSortFilterOptions:colorSortFilterOptions,
	            	submitStateFilterOptions:submitStateFilterOptions,
	            	auditStateFilterOptions:auditStateFilterOptions,
	            	tzIsMszgFilterOptions:tzIsMszgFilterOptions,
	            	initialColorSortData:initialColorSortData
	        	});
	        }
	    };
	    
        orgColorSortStore.on("load",function(store, records, successful, eOpts){
        	for(var i=0;i<records.length;i++){
            	initialColorSortData.push(records[i].data);
                colorSortFilterOptions.push([records[i].data.TZ_COLOR_SORT_ID,records[i].data.TZ_COLOR_NAME]);
            }
            beforeRender();
	    });
	    submitStateStore.on("load",function(store, records, successful, eOpts){
	    	for(var i=0;i<records.length;i++){
	    		submitStateFilterOptions.push([records[i].data.TValue,records[i].data.TSDesc]);
            }
            beforeRender();
	    });
	    auditStateStore.on("load",function(store, records, successful, eOpts){
	    	for(var i=0;i<records.length;i++){
	    		auditStateFilterOptions.push([records[i].data.TValue,records[i].data.TSDesc]);
            }
            beforeRender();
	    });
	    tzIsMszgStore.on("load",function(store, records, successful, eOpts){
	    	for(var i=0;i<records.length;i++){
	    		tzIsMszgFilterOptions.push([records[i].data.TValue,records[i].data.TSDesc]);
            }
            beforeRender();
	    });
    },
    
    //报名流程结果公布（LZ添加）
    publishResult:function(grid, rowIndex, colIndex){
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_BMGL_BMBPICI_COM"]["TZ_BMSH_FB_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.nmyqx","您没有权限"));
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
            return;
        }
        var contentPanel, cmp, ViewClass, clsProto;
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
        cmp = new ViewClass();
        var record = grid.store.getAt(rowIndex);
        var classID = record.data.classID;
        var batchID = record.data.batchID;
        cmp.on('afterrender',function(panel){
            var tzParams = '{"ComID":"TZ_BMGL_BMBPICI_COM","PageID":"TZ_BMSH_FB_STD","OperateType":"tzLoadGridColumns","comParams":{"classID":"'+classID+'"}}';
            Ext.tzLoad(tzParams,function(responseData){
                var msResGridColumns = [{
                    text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.bmbbh","报名表编号"),
                    dataIndex: 'bmb_id',
                    filter: {
                        type: 'number',
                        fields:{gt: {iconCls: Ext.baseCSSPrefix + 'grid-filters-gt', margin: '0 0 3px 0'}, lt: {iconCls: Ext.baseCSSPrefix + 'grid-filters-lt', margin: '0 0 3px 0'}, eq: {iconCls: Ext.baseCSSPrefix + 'grid-filters-eq', margin: 0}}
                    },
                    minWidth: 125,
                    flex:1
                },{
                    text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.name","姓名"),
                    dataIndex: 'ry_name',
                    minWidth: 100,
                    flex:1,
                    filter: {
                        type: 'string'
                    }
                },{
                    text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.country","国籍"),
                    dataIndex: 'country',
                    minWidth: 100,
                    flex:1,
                    filter: {
                        type: 'string'
                    }
                },{
                    text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.ckqtgbnr","查看前台公布内容"),
                    align: 'center',
                    groupable: false,
                    width: 150,
                    renderer: function(v) {
                        return '<a href="javascript:void(0)">'+Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.view","查看")+'</a>';
                    },
                    listeners:{
                        click:'sec_frontDeac'
                    }
                }];
                var bmlc_zd=responseData['bmlc_zd'];

                //LYY  2015-08-14  流程发布更改为单元格点击事件，不使用actioncolumn
                var lcfbLCJDdataIndexId = "";
                for(var i=0;i<bmlc_zd.length;i++){
                    lcfbLCJDdataIndexId= bmlc_zd[i]['bmlc_id']+"#^"+bmlc_zd[i]['bmlc_name'];
                    msResGridColumns.push({
                        text:bmlc_zd[i]['bmlc_name'] ,
                        dataIndex: lcfbLCJDdataIndexId,
                        width: 170,
                        editable:false,
                        renderer:function(value){
                            var _url1=value.split(",");
                            if(value!=""){
                                return "<div class='x-colorpicker-field-swatch-inner' style='width:20%;height:50%;background-color: #"+_url1[0]+"'></div><div style='width:70%;margin-left:auto;'>"+_url1[1]+"</div>";
                            }
                        }
                    });
                }
                var bmb_lcStudentsStore = new KitchenSink.view.enrollmentManagement.bmb_lcpi.studentsStore();
                var msResGrid = Ext.create("Ext.grid.Panel",{
                    height: panel.getHeight()-58,
                    frame: true,
                    name: 'student_listGr',
                    reference: 'student_listGr',
                    store:bmb_lcStudentsStore,
                    columnLines: true,    //显示纵向表格线
                    selModel:{
                        type: 'checkboxmodel'
                    },
                    plugins:[
                        {
                            ptype: 'gridfilters',
                            controller: 'appBatchClass'
                        },
                        {
                            ptype: 'cellediting',
                            clicksToEdit: 1
                        }
                    ],
                    dockedItems:[{
                        xtype: "toolbar",
                        items: [{
                            text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.plfbjg","批量发布结果"),
                            handler: 'onPLPublisResult'
                        }]
                    }],
                    columns: msResGridColumns,
                    bbar: {
                        xtype: 'pagingtoolbar',
                        pageSize: 1000,
                        store: bmb_lcStudentsStore,
                        plugins: new Ext.ux.ProgressBarPager()
                    }
                });

                var onClickColumnsIndex=0;
                for(var i=0;i<msResGrid.columns.length;i++){
                    if(msResGrid.columns[i].dataIndex!=null||msResGrid.columns[i].dataIndex!=undefined){
                        if(msResGrid.columns[i].dataIndex.indexOf("#^")!=-1){
                            onClickColumnsIndex=i;
                            break;
                        }
                    }
                }
                msResGrid.on("cellclick",function( table,td, cellIndex, record, tr, rowIndex, e, eOpts ){
                    if(onClickColumnsIndex>0){
                        if(cellIndex>onClickColumnsIndex){
                            var lciddataindexarr = msResGrid.columns[cellIndex-1].dataIndex.split("#^");
                            var lciddataindex=lciddataindexarr[0];
                            var classId = msResGrid.store.getAt(rowIndex).data.bj_id;
                            var appId = msResGrid.store.getAt(rowIndex).data.bmb_id;
                             console.log(msResGrid.store.getAt(rowIndex).data)
                            var bmlcId = lciddataindex;
                            Ext.tzSetCompResourses("TZ_BMGL_LCJGPUB_COM");
                            var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_BMGL_LCJGPUB_COM"]["TZ_PER_LCJGPUB_STD"];

                            if( pageResSet == "" || pageResSet == undefined){
                                Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.nmyqx","您没有权限"));
                                return;
                            }
                            var className = pageResSet["jsClassName"];
                            if(className == "" || className == undefined){
                                Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"), Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
                                return;
                            }

                            var win = this.lookupReference('perLcjgPubWindow');
                            if (!win) {
                                Ext.syncRequire(className);
                                ViewClass = Ext.ClassManager.get(className);
                                win = new ViewClass({
                                    perLcjgCallBack:function(){
                                        msResGrid.store.reload();
                                    }
                                });
                                var record = grid.store.getAt(rowIndex);
                                var classID = classId;
                                var bmlc_id = bmlcId;
                                var bmb_id = appId;
                                var form = win.child('form').getForm();
                                var lm_mbStore = new KitchenSink.view.common.store.comboxStore({
                                    recname: 'TZ_CLS_BMLCHF_T',
                                    condition:{
                                        TZ_CLASS_ID:{
                                            value:classID,
                                            operator:"01",
                                            type:"01"
                                        },
                                        TZ_APPPRO_ID:{
                                            value:bmlc_id,
                                            operator:"01",
                                            type:"01"
                                        }
                                    },
                                    result:'TZ_APPPRO_HF_BH,TZ_CLS_RESULT'
                                });
                                form.findField("jg_id").setStore(lm_mbStore);
                                win.on('afterrender',function(panel){
                                    var tzParams = '{"ComID":"TZ_BMGL_LCJGPUB_COM","PageID":"TZ_PER_LCJGPUB_STD","OperateType":"QF","comParams":{"bj_id":"'+classID+'","bmlc_id":"'+bmlc_id+'","bmb_id":"'+bmb_id+'"}}';
                                    //"callback"'+this.testLqlc2()+'"
                                    Ext.tzLoad(tzParams,function(responseData){
                                        var formData = responseData.formData;
                                        form.setValues(formData);
                                    });
                                });
                                //this.getView().add(win);
                            }
                            win.show();
                        }
                    }
                })

                cmp.add(msResGrid);

                var lcfbStuListFormData={'classID':classID};
                var lcfbStuListForm = panel.child('form').getForm();
                lcfbStuListForm.setValues(lcfbStuListFormData);

                var Params= '{"bj_id":"'+classID+'","batchID":"'+batchID+'"}';
                var panelGrid = panel.down('grid');
                panelGrid.store.tzStoreParams = Params;
                panelGrid.store.reload();
            });
        });
        tab = contentPanel.add(cmp);
        contentPanel.setActiveTab(tab);
        Ext.resumeLayouts(true);
        if (cmp.floating) {
            cmp.show();
        }
    },
    //添加结束LZ

    auditApplicationForm:function(grid, rowIndex, colIndex){
        //是否有访问权限
    	Ext.tzSetCompResourses("TZ_BMGL_BMBSH_COM");
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_BMGL_BMBSH_COM"]["TZ_BMGL_AUDIT_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.nmyqx","您没有权限"));
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"), Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
            return;
        }
        var contentPanel, cmp, ViewClass, clsProto;

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
            // <debug warn>
            // Sometimes we forget to include allowances for other themes, so issue a warning as a reminder.
            if (!clsProto.themeInfo) {
                Ext.log.warn ( 'Example \'' + className + '\' lacks a theme specification for the selected theme: \'' +
                    themeName + '\'. Is this intentional?');
            }
            // </debug>
        }

        var classID,oprID ,appInsID,record;

        if(this.getView().name=="applicationFormWindow"){
            classID = this.getView().classID;
            oprID = this.getView().oprID;
            appInsID= this.getView().appInsID;
            record= this.getView().gridRecord;
            this.getView().close();
        }else{
            record = grid.store.getAt(rowIndex);
            classID = record.data.classID;
            oprID = record.data.oprID;
            appInsID= record.data.appInsID;
        }

        var applicationFormTagStore= new KitchenSink.view.common.store.comboxStore({
            recname:'TZ_TAG_STORE_V',
            condition:{
                TZ_JG_ID:{
                    value:Ext.tzOrgID,
                    operator:'01',
                    type:'01'
                },
                TZ_APP_INS_ID:{
                    value:appInsID,
                    operator:'01',
                    type:'01'
                }
            },
            result:'TZ_LABEL_ID,TZ_LABEL_NAME'
        });
        //自动标签
        var appAutoTagsStore= new KitchenSink.view.common.store.comboxStore({
            recname:'TZ_ZDBQ_DFN_TBL',
            condition:{},
            result:'TZ_ZDBQ_ID,TZ_ZDBQ_NAME'
        });
        
        applicationFormTagStore.load({
            scope:this,
            callback:function(){
            	//加载自动标签
            	appAutoTagsStore.load({
            		scope:this,
                    callback:function(){
                    	cmp = new ViewClass({
                    		appInsID:appInsID,
                    		applicationFormTagStore:applicationFormTagStore,
                    		gridRecord:record,
                    		appAutoTagsStore: appAutoTagsStore
                    	});
                        cmp.on('afterrender',function(panel){
                            var form = panel.child('form').getForm();
                            var tabpanel = panel.child("tabpanel");
                            var tzParams = '{"ComID":"TZ_BMGL_BMBSH_COM","PageID":"TZ_BMGL_AUDIT_STD","OperateType":"QF","comParams":{"classID":"'+classID+'","oprID":"'+oprID+'"}}';
                            Ext.tzLoad(tzParams,function(respData){
                                var formData = respData.formData;
                                form.setValues(formData);
                                tabpanel.down('form[name=remarkForm]').getForm().setValues(formData);
                                tabpanel.down('form[name=contactInfoForm]').getForm().setValues(formData);
                            });
                        });

                        tab = contentPanel.add(cmp);

                        contentPanel.setActiveTab(tab);

                        Ext.resumeLayouts(true);

                        if (cmp.floating) {
                            cmp.show();
                        }
                    }
            	});
            }

        })
    },
    viewApplicationForm: function(grid, rowIndex,colIndex){
        Ext.tzSetCompResourses("TZ_ONLINE_REG_COM");
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_ONLINE_REG_COM"]["TZ_ONLINE_APP_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.nmyqx","您没有权限"));
            return;
        }

        var store = grid.getStore();
        var record = store.getAt(rowIndex);
        var classID=record.get("classID");
        var oprID=record.get("oprID");
        var appInsID=record.get("appInsID");
        var stuName=record.get("stuName");
        var stuId=record.get("interviewApplicationID");
        var stuState=record.get("submitState");
        
        if(stuState=="BACK"){
        	stuState="退回修改";
        }else if(stuState=="OUT"){
        	stuState="撤销";
        }else if(stuState=="S"){
        	stuState="新建";
        }else if(stuState=="U"){
        	stuState="已提交";
        }else{
        	stuState=stuState;
        }
        

        var title=Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.viewApplicationForm","查看报名表");
   
        var titleResult="<div>" +
        		"<span id = 'tz_titlecolor' style=\"font-size:14px;font-weight:normal;color:#FFFFFF\">"+"面试申请号:"+"</span>" +
        		"<span id = 'tz_titlecolor' style=\"font-size:14px;font-weight:normal;color:#FFFFFF\">"+stuId+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+"</span>" +
        		"<span id = 'tz_titlecolor' style=\"font-size:14px;font-weight:normal;color:#FFFFFF\">"+"姓名:"+"</span>" +
        		"<span id = 'tz_titlecolor' style=\"font-size:14px;font-weight:normal;color:#FFFFFF\">"+stuName+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+"</span>" +
        		"<span id = 'tz_titlecolor' style=\"font-size:14px;font-weight:normal;color:#FFFFFF\">"+"报名表状态:"+"</span>" +
        		"<span id = 'tz_titlecolor' style=\"font-size:14px;font-weight:normal;color:#FFFFFF\">"+stuState+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+"</span>" +
        			"</div>";
        
        if(appInsID!=""){
            var tzParams='{"ComID":"TZ_ONLINE_REG_COM","PageID":"TZ_ONLINE_APP_STD","OperateType":"HTML","comParams":{"TZ_APP_INS_ID":"'+appInsID+'","isEdit":"Y"}}';
            var viewUrl =Ext.tzGetGeneralURL()+"?tzParams="+encodeURIComponent(tzParams);
            var mask ;
            var win = new Ext.Window({
                name:'applicationFormWindow',
                title : titleResult,
                maximized : true,
                controller:'appBatchClass',
                classID :classID,
                oprID :oprID,
                appInsID : appInsID,
                gridRecord:record,
                width : Ext.getBody().width,
                height : Ext.getBody().height,
                autoScroll : true,
                border:false,
                bodyBorder : false,
                isTopContainer : true,
                modal : true,
                resizable : false,
                items:[
                    new Ext.ux.IFrame({
                        xtype: 'iframepanel',
                        layout: 'fit',
                        style : "border:0px none;scrollbar:true",
                        border: false,
                        src : viewUrl,
                        height : "100%",
                        width : "100%"
                    })
                ],
                buttons: [ {
                    text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.audit","审批"),
                    iconCls:"send",
                    handler: "auditApplicationForm"
                },
                    {
                        text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.close","关闭"),
                        iconCls:"close",
                        handler: function(){
                            win.close();
                        }
                    }]
            })
            win.show();
        }else{
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.cantFindAppForm","找不到该报名人的报名表"));
        }
    },
    /*材料打包和下载*/
    packageAndDownload:function(btn){
        var selList = btn.findParentByType("grid").getSelectionModel().getSelection();
        if(selList.length<1) {
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"), Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.youSelectedNothing","您没有选中任何记录"));
            return;
        }else{
            var strAppId;
            for (var i = 0; i < selList.length; i++) {
                if (i>0) {
                    strAppId=selList[i].get('appInsID')+";"+strAppId
                }else{
                    strAppId=selList[i].get('appInsID')+";"
                }

            }
        }
        // Ext.MessageBox.alert('测试',strAppId);
        //是否有访问权限
        Ext.tzSetCompResourses("TZ_BMGL_BMBSH_COM");
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_BMGL_BMBSH_COM"]["TZ_BMGL_DBDL_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.nmyqx","您没有权限"));
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
            return;
        }
        var win = this.lookupReference('cldbForm');
        if (!win) {
            Ext.syncRequire(className);
            ViewClass = Ext.ClassManager.get(className);
            //新建类
            win = new ViewClass();
            this.getView().add(win);
        }

        //操作类型设置为新增
        win.actType = "add";
        var form = win.child("form").getForm();
        form.reset();
        form.setValues({appInsID:"'"+strAppId+"'"});
        win.show();

    },
    
    /*全部人员材料打包和下载*/
    packageAndDownloadOfAll:function(btn){
    	
    	var store = btn.findParentByType("grid").store;

		var strConfSearCond=btn.findParentByType('classInfoB').strConfSearCond;
		
		if (strConfSearCond.length==0) {
			
			strConfSearCond="{\"cfgSrhId\":\"TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.TZ_APP_LIST_VW\",\"condition\": {\"TZ_CLASS_ID-operator\": \"01\", \"TZ_CLASS_ID-value\":\""+store.classID +"\",\"TZ_BATCH_ID-operator\": \"01\", \"TZ_BATCH_ID-value\":\""+store.batchID +"\"}}";
				
		}
		
        //顾贤达 2017年6月26日 11:08:54 修改SQL提交逻辑
		var tzParams = '{"ComID":"TZ_BMGL_BMBSH_COM","PageID":"TZ_BMGL_STU_STD","OperateType":"tzConfigSearchSQL","comParams":'+strConfSearCond+'}';

		Ext.tzLoadAsync(tzParams,function(resp){
			 if(resp.AppID!=undefined){
					strAppId =resp.AppID;
	            }
	    },"",true,this);
		
		strAppId=strAppId+";";
		console.log(strAppId);
		/*
		var strSQL=btn.findParentByType('classInfoB').strSQL;
    	if (strSQL.length==0) {
    		var store = btn.findParentByType("grid").store;
    		var OriSQL="SELECT TZ_APP_INS_ID FROM PS_TZ_APP_LIST_VW where TZ_CLASS_ID='"+store.classID+"' AND TZ_BATCH_ID='"+store.batchID+"'";
    		
    		getAppIdSQL2=OriSQL;
		}else{
			getAppIdSQL2=strSQL;
		}
		
		
		 var strAppId; 
		 var tzParams = '{"ComID":"TZ_BMGL_BMBSH_COM","PageID":"TZ_BMGL_STU_STD","OperateType":"tzGetAppIdStr","comParams":{"getAppIdSQL":"'+getAppIdSQL2+'"}}';
		//后台执行查询AppID表操作
		Ext.tzLoadAsync(tzParams,function(resp){
			 if(resp.AppID!=undefined){
					strAppId =resp.AppID;
	            }
	    },"",true,this);
		strAppId=strAppId+";";*/
		
        //是否有访问权限
        Ext.tzSetCompResourses("TZ_BMGL_BMBSH_COM");
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_BMGL_BMBSH_COM"]["TZ_BMGL_DBDL_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.nmyqx","您没有权限"));
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
            return;
        }
        var win = this.lookupReference('cldbForm');
        if (!win) {
            Ext.syncRequire(className);
            ViewClass = Ext.ClassManager.get(className);
            //新建类
            win = new ViewClass();
            this.getView().add(win);
        }

        //操作类型设置为新增
        win.actType = "add";
        var form = win.child("form").getForm();
        form.reset();
        form.setValues({appInsID:"'"+strAppId+"'"});
        win.show();

        getAppIdSQL2="";
    },
    
    /*推荐信打包和下载*/
    packageTjxDownload:function(btn){
        var selList = btn.findParentByType("grid").getSelectionModel().getSelection();
        if(selList.length<1) {
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"), Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.youSelectedNothing","您没有选中任何记录"));
            return;
        }else{
            var strAppId;
            for (var i = 0; i < selList.length; i++) {
                if (i>0) {
                    strAppId=selList[i].get('appInsID')+";"+strAppId
                }else{
                    strAppId=selList[i].get('appInsID')+";"
                }

            }
        }
        // Ext.MessageBox.alert('测试',strAppId);
        //是否有访问权限
        Ext.tzSetCompResourses("TZ_BMGL_BMBSH_COM");
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_BMGL_BMBSH_COM"]["TZ_BMGL_DBDL_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.nmyqx","您没有权限"));
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
            return;
        }
        var win = this.lookupReference('cldbForm');
        if (!win) {
            Ext.syncRequire(className);
            ViewClass = Ext.ClassManager.get(className);
            //新建类
            win = new ViewClass();
            this.getView().add(win);
        }

        //操作类型设置为新增
        win.actType = "tjxAdd";
        var form = win.child("form").getForm();
        form.reset();
        form.setValues({appInsID:"'"+strAppId+"'"});
        win.show();

    },
    
    /*全部人员推荐信打包和下载*/
    packageTjxDownloadOfAll:function(btn){
    	
		var strAppId;  
		var store = btn.findParentByType("grid").store;
		
		var strConfSearCond=btn.findParentByType('classInfoB').strConfSearCond;
		
		if (strConfSearCond.length==0) {
			strConfSearCond="{\"cfgSrhId\":\"TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.TZ_APP_LIST_VW\",\"condition\": {\"TZ_CLASS_ID-operator\": \"01\", \"TZ_CLASS_ID-value\":\""+store.classID +"\",\"TZ_BATCH_ID-operator\": \"01\", \"TZ_BATCH_ID-value\":\""+store.batchID +"\"}}";
		}
		
        //顾贤达 2017年6月26日 11:08:54 修改SQL提交逻辑
		var tzParams = '{"ComID":"TZ_BMGL_BMBSH_COM","PageID":"TZ_BMGL_STU_STD","OperateType":"tzConfigSearchSQL","comParams":'+strConfSearCond+'}';

		Ext.tzLoadAsync(tzParams,function(resp){
			 if(resp.AppID!=undefined){
					strAppId =resp.AppID;
	            }
	    },"",true,this);
		
		strAppId=strAppId+";";
		console.log(strAppId);
    	/*
    	var strSQL=btn.findParentByType('classInfoB').strSQL;
    	if (strSQL.length==0) {
    		var store = btn.findParentByType("grid").store;
    		var OriSQL="SELECT TZ_APP_INS_ID FROM PS_TZ_APP_LIST_VW where TZ_CLASS_ID='"+store.classID+"' AND TZ_BATCH_ID='"+store.batchID+"'";
    		
    		getAppIdSQL2=OriSQL;
		}else{
			getAppIdSQL2=strSQL;
		}
		
		 var strAppId; 
		 var tzParams = '{"ComID":"TZ_BMGL_BMBSH_COM","PageID":"TZ_BMGL_STU_STD","OperateType":"tzGetAppIdStr","comParams":{"getAppIdSQL":"'+getAppIdSQL2+'"}}';
		//后台执行查询AppID表操作
		Ext.tzLoadAsync(tzParams,function(resp){
			 if(resp.AppID!=undefined){
					strAppId =resp.AppID;
	            }
	    },"",true,this);
		strAppId=strAppId+";";
		*/
        //是否有访问权限
        Ext.tzSetCompResourses("TZ_BMGL_BMBSH_COM");
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_BMGL_BMBSH_COM"]["TZ_BMGL_DBDL_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.nmyqx","您没有权限"));
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
            return;
        }
        var win = this.lookupReference('cldbForm');
        if (!win) {
            Ext.syncRequire(className);
            ViewClass = Ext.ClassManager.get(className);
            //新建类
            win = new ViewClass();
            this.getView().add(win);
        }

        //操作类型设置为新增
        win.actType = "tjxAdd";
        var form = win.child("form").getForm();
        form.reset();
        form.setValues({appInsID:"'"+strAppId+"'"});
        win.show();

        getAppIdSQL2="";
    },
    
    viewAndDownload:function(btn){
        //是否有访问权限
    	Ext.tzSetCompResourses("TZ_BMGL_BMBSH_COM");
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_BMGL_BMBSH_COM"]["TZ_BMGL_DBDL_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.nmyqx","您没有权限"));
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"), Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
            return;
        }
        var win = this.lookupReference('cldbForm');
        if (!win) {
            Ext.syncRequire(className);
            ViewClass = Ext.ClassManager.get(className);
            //新建类
            win = new ViewClass();
            this.getView().add(win);
        }

        //操作类型设置为更新
        win.actType = "update";
        //var comRegParams = this.getView().child("form").getForm().getValues();
        var tabPanel = win.lookupReference("packageTabPanel");
        tabPanel.setActiveTab(1);
        win.show();
    },
    /*导出到Excel or 下载导出结果*/
    exportExcelOrDownload:function(btn){
        var btnName = btn.name;
        var selList = btn.findParentByType("grid").getSelectionModel().getSelection();
        if(btnName=='exportExcel'){
            if(selList.length<1) {
                Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"), Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.youSelectedNothing","您没有选中任何记录"));
                return;
            };
        }

        //是否有访问权限
        Ext.tzSetCompResourses("TZ_BMGL_BMBSH_COM");
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_BMGL_BMBSH_COM"]["TZ_EXP_EXCEL_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.nmyqx","您没有权限") );
            return;
        }

        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"), Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
            return;
        }

        var win = this.lookupReference('exportExcelForm');
        if (!win) {
            Ext.syncRequire(className);
            ViewClass = Ext.ClassManager.get(className);
            //新建类
            var modalID =btn.findParentByType('classInfoB').child('form').getForm().findField('modalID').getValue();
            win = new ViewClass(modalID);
            this.getView().add(win);
        };
        win.selList=selList;

        if(btnName=='downloadExcel'){
            var tabPanel = win.lookupReference("exportExcelTabPanel");
            tabPanel.setActiveTab(1);
        }
        var form = win.child("form").getForm();
        form.reset();
        win.show();
    },
    
    /*导出全部人员到Excel or 下载导出结果*/
    exportExcelOfAll:function(btn){
    	Ext.tzSetCompResourses("TZ_BMGL_BMBSH_COM");

    	var store = btn.findParentByType("grid").store;

		var strConfSearCond=btn.findParentByType('classInfoB').strConfSearCond;
		
		if (strConfSearCond.length==0) {
			strConfSearCond="{\"cfgSrhId\":\"TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.TZ_APP_LIST_VW\",\"condition\": {\"TZ_CLASS_ID-operator\": \"01\", \"TZ_CLASS_ID-value\":\""+store.classID +"\",\"TZ_BATCH_ID-operator\": \"01\", \"TZ_BATCH_ID-value\":\""+store.batchID +"\"}}";
			
		}
		
        //顾贤达 2017年6月26日 11:08:54 修改SQL提交逻辑
		var tzParams = '{"ComID":"TZ_BMGL_BMBSH_COM","PageID":"TZ_BMGL_STU_STD","OperateType":"tzConfigSearchSQL","comParams":'+strConfSearCond+'}';

		Ext.tzLoadAsync(tzParams,function(resp){
			 if(resp.AppID!=undefined){
					strAppId =resp.AppID;
	            }
	    },"",true,this);

		console.log(strAppId);
		
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_BMGL_BMBSH_COM"]["TZ_EXP_EXCEL_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.nmyqx","您没有权限") );
            return;
        }

        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"), Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
            return;
        }

        var win = this.lookupReference('exportExcelForm');
        if (!win) {
            Ext.syncRequire(className);
            ViewClass = Ext.ClassManager.get(className);
            //新建类
//            var modalID =btn.findParentByType('auditClassInfo').child('form').getForm().findField('modalID').getValue();
            var modalID =btn.findParentByType('classInfoB').child('form').getForm().findField('modalID').getValue();
            win = new ViewClass(modalID);
            this.getView().add(win);
        };
        win.strAppID=strAppId;
        

        var form = win.child("form").getForm();
        form.reset();
        win.show();
        
        getAppIdSQL2="";
    },
    printAppForm:function(obj,rowIndex,colIndex){
        Ext.tzSetCompResourses("TZ_ONLINE_REG_COM");
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_ONLINE_REG_COM"]["TZ_PRINT_ADMIN_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.nmyqx","您没有权限"));
            return;
        }
        var appInsID;
        if(!obj.store){
            var selList = obj.findParentByType("grid").getSelectionModel().getSelection();
            
            if(selList.length<1) {
                Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"), Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.youSelectedNothing","您没有选中任何记录"));
                return;
            }
            if(selList.length>1) {
                Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"), Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.canSelectOneOnly","只能选中一条记录"));
                return;
            }
            appInsID =selList[0].get('appInsID');
        }else{
            appInsID = obj.getStore().getAt(rowIndex).get('appInsID');
        }

        if(appInsID!=""){
            //var tzParams='{"ComID":"TZ_ONLINE_REG_COM","PageID":"TZ_PRINT_ADMIN_STD","OperateType":"HTML","comParams":{"appInsID":"'+appInsID+'"}}';
            //var viewUrl =Ext.tzGetGeneralURL()+"?tzParams="+tzParams;
        	
        	//html转pdf的;
        	//var viewUrl = TzUniversityContextPath+"/admission/expform/"+appInsID;
            //window.location.href=viewUrl;
        	
    		//var url = TzUniversityContextPath + "/DownPdfServlet?instanceID="+appInsID;
    		//window.open(url, '_blank');
        	
        	var url = TzUniversityContextPath + "/PrintPdfServlet?instanceID="+appInsID;
    		window.open(url, '_blank');
        }else{
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.cantFindAppForm","找不到该报名人的报名表"));
        }

    },
    /*清除过滤条件*/
    onClearFilters: function (btn) {
        btn.findParentByType('grid').filters.clearFilters();
    },
    onComRegClose: function(btn){
        //关闭窗口
        this.getView().close();
    },
    /*查看邮件发送历史*/
    viewMailHistory: function(btn){
        var grid=btn.findParentByType("grid");
        var store = grid.getStore();
        var selList = grid.getSelectionModel().getSelection();

        //选中行长度
        var checkLen = selList.length;
        if(checkLen == 0){
            Ext.Msg.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.youSelectedNothing","您没有选中任何记录"));
            return;
        }else if(checkLen >1){
            Ext.Msg.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.znxzytjl","只能选择一条记录"));
            return;
        }
        var appInsID = selList[0].get("appInsID");

        var tzParams = '{"ComID":"TZ_BMGL_BMBSH_COM","PageID":"TZ_BMGL_STU_STD","OperateType":"tzGetEmail","comParams":{"appInsID":"'+appInsID+'"}}';

        Ext.tzLoad(tzParams,function(respData){
            if(respData.email!=undefined){
            Ext.tzSearchMailHistory(respData.email);
            }else{
                Ext.Msg.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.nxzdrymytjzyyx","您选中的人员没有添加邮箱"));
                return;
            }
        });
    },
	/*给选中人发送邮件*/
    sendEmlSelPers:function(btn) {
        var grid = btn.findParentByType("grid");
        var store = grid.getStore();
        var selList = grid.getSelectionModel().getSelection();

        //选中行长度
        var checkLen = selList.length;
        if (checkLen == 0) {
            Ext.Msg.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt", "提示"), Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.youSelectedNothing", "您没有选中任何记录"));
            return;
        }

        var personList = [];
        for (var i = 0; i < checkLen; i++) {
            var oprID = selList[i].get('oprID');
            var appInsID = selList[i].get('appInsID');
            personList.push({"oprID": oprID, "appInsID": appInsID});
        };
        var params = {
            "ComID": "TZ_BMGL_BMBSH_COM",
            "PageID": "TZ_BMGL_YJDX_STD",
            "OperateType": "U",
            "comParams": {"add": [
                {"type": 'MULTI', "personList": personList}
            ]}
        };
        Ext.tzLoad(Ext.JSON.encode(params), function (responseData) {
            Ext.tzSendEmail({
                //发送的邮件模板;
//                "EmailTmpName": ["TZ_EML_N_001"],
                "EmailTmpName": ["TZ_EML_N_001","TZ_MAL_NAME_CN"],
                //创建的需要发送的听众ID;
                "audienceId": responseData,
                //是否有附件: Y 表示可以发送附件,"N"表示无附件;
                "file": "N"
            });
        });
    },
    /*给选中人发送短信*/
    sendSmsSelPers:function(btn) {
        var grid = btn.findParentByType("grid");
        var store = grid.getStore();
        var selList = grid.getSelectionModel().getSelection();

        //选中行长度
        var checkLen = selList.length;
        if (checkLen == 0) {
            Ext.Msg.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt", "提示"), Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.youSelectedNothing", "您没有选中任何记录"));
            return;
        }

        var personList = [];
        for (var i = 0; i < checkLen; i++) {
            var oprID = selList[i].get('oprID');
            var appInsID = selList[i].get('appInsID');
            personList.push({"oprID": oprID, "appInsID": appInsID});
        };
        var params = {
            "ComID": "TZ_BMGL_BMBSH_COM",
            "PageID": "TZ_BMGL_YJDX_STD",
            "OperateType": "U",
            "comParams": {"add": [
                {"type": 'MULTI', "personList": personList}
            ]}
        };
        Ext.tzLoad(Ext.JSON.encode(params), function (responseData) {
            Ext.tzSendSms({
                //发送的邮件模板;
                "SmsTmpName": ["TZ_SMS_N_002"],
                //创建的需要发送的听众ID;
                "audienceId": responseData,
                //是否有附件: Y 表示可以发送附件,"N"表示无附件;
                "file": "N"
            });
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
            var tzParams ='{"ComID":"TZ_REVIEW_CL_COM","PageID":"TZ_CLPS_SCHE_STD",' +'"OperateType":"QF","comParams":{"classID":"'+classID+'","batchID":"'+batchID+'"}}';
            Ext.tzLoad(tzParams,function(respData){
                respData.className = record.data.className;
                respData.batchName = record.data.batchName;
                form.setValues(respData);
                var formButton =panel.child('form');
                var btnStartNewReview=formButton.down('button[name=startNewReview]'),
                    btnCloseReview=formButton.down('button[name=closeReview]');
                if(respData.status=='进行中'){
                    btnStartNewReview.flagType='negative';
                    btnCloseReview.flagtype='positive';
                    btnStartNewReview.setDisabled(true);
                }
                if(respData.status=='已关闭'){
                    btnStartNewReview.flagType='positive';
                    btnCloseReview.flagtype='negative';
                    btnCloseReview.setDisabled(true);
                }
                if(respData.status=='未开始'){
                    btnStartNewReview.flagType='positive';
                    btnCloseReview.flagtype='negative';
                    btnCloseReview.setDisabled(true);
                }
            });
            judgeStore.tzStoreParams = judgeParams;
            judgeStore.load();
            stuListStore.tzStoreParams = stuListParams;
            stuListStore.load();
        });

        tab = contentPanel.add(cmp);

        contentPanel.setActiveTab(tab);

        Ext.resumeLayouts(true);

        if (cmp.floating) {
            cmp.show();
        }
    },
    /*材料评审--设置评审规则*/
    setMaterialReviewRule:function(grid,rowIndex) {
        Ext.tzSetCompResourses("TZ_REVIEW_CL_COM");
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_REVIEW_CL_COM"]["TZ_CLPS_RULE_STD"];

        if (pageResSet == "" || pageResSet == undefined) {
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if (className == "" || className == undefined) {
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_CLPS_RULE_STD，请检查配置。');
            return;
        }
        var contentPanel, cmp, ViewClass, clsProto;

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
                Ext.log.warn('Example \'' + className + '\' lacks a theme specification for the selected theme: \'' +
                    themeName + '\'. Is this intentional?');
            }
            // </debug>
        }

        var record = grid.store.getAt(rowIndex);
        var classId = record.data.classID;
        var className = record.data.className;
        var batchId = record.data.batchID;
        var batchName = record.data.batchName;
        var applicantsNumber = record.data.applicantsNumber;
        var submittedNumber = record.data.submittedNumber;


        cmp = new ViewClass({
            classId: classId,
            batchId: batchId,
            judgeGroupData: ''
        });

        cmp.on('afterrender', function (panel) {

            panel.judgeGroupData = "";

            var form = panel.child('form').getForm();
            var statisticsNumForm = panel.down("form[name=statisticsNumForm]").getForm();

            var tzParams = '{"ComID":"TZ_REVIEW_CL_COM","PageID":"TZ_CLPS_RULE_STD",' +
                '"OperateType":"QF","comParams":{"classId":"' + classId + '","batchId":"' + batchId + '"}}';

            Ext.tzLoad(tzParams, function (respData) {
                var formData = respData.formData;
                if (formData != "" && formData != undefined) {
                    panel.actType = "update";
                    form.setValues(formData);

                    if (formData.dqpsStatus == "A") {
                        form.findField("judgeNumSet").setDisabled(true);
                    }

                    statisticsNumForm.findField("clpsksNum").setValue(formData.clpsksNum);
                    //要求评审人次更新
                    statisticsNumForm.findField("reviewNumSet").setValue(parseInt(formData.clpsksNum) * (formData.judgeNumSet));
                } else {
                    var materialDesc = respData.materialDesc;
                    panel.actType = "add";
                    form.findField("classId").setValue(classId);
                    form.findField("className").setValue(className);
                    form.findField("batchId").setValue(batchId);
                    form.findField("batchName").setValue(batchName);
                    form.findField("bkksNum").setValue(applicantsNumber);
                    form.findField("submitKsNum").setValue(submittedNumber);
                    form.findField("clpsksNum").setValue(0);
                    form.findField("dqpsStatus").setValue("N");
                    form.findField("dqpsStatusDesc").setValue("未开始");
                    form.findField("judgeNumSet").setValue(2);
                    form.findField("materialDesc").setValue(materialDesc);

                    var statisticsForm = panel.down("form[name=statisticsNumForm]").getForm();
                    statisticsForm.findField("clpsksNum").setValue(0);
                    statisticsForm.findField("reviewNumSet").setValue(0);
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
    /*材料评审--材料评审考生名单*/
    viewMaterialStuApplicants:function(grid,rowIndex){
        Ext.tzSetCompResourses("TZ_REVIEW_CL_COM");
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_REVIEW_CL_COM"]["TZ_CLPS_KS_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_CLPS_KS_STD，请检查配置。');
            return;
        }
        var contentPanel, cmp, ViewClass, clsProto;

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
            // <debug warn>
            // Sometimes we forget to include allowances for other themes, so issue a warning as a reminder.
            if (!clsProto.themeInfo) {
                Ext.log.warn ( 'Example \'' + className + '\' lacks a theme specification for the selected theme: \'' +
                    themeName + '\'. Is this intentional?');
            }
            // </debug>
        }


        var record = grid.store.getAt(rowIndex);
        var classId = record.data.classID;
        var className = record.data.className;
        var batchId = record.data.batchID;
        var batchName = record.data.batchName;
        var applicantsNumber = record.data.applicantsNumber;
        var submittedNumber = record.data.submittedNumber;

        //当前登录人是否超级管理员
        /*var tzParamsAdmin = '{"ComID":"TZ_REVIEW_CL_COM","PageID":"TZ_CLPS_RULE_STD",' +
            '"OperateType":"tzIsSuperAdmin","comParams":{}}';
        Ext.tzLoad(tzParamsAdmin,function(respData) {
            var hideFlag = true;
            var superAdminFlag = respData.superAdminFlag;
            if (superAdminFlag == "Y") {
                hideFlag = false;
            }*/

            var hideFlag = false;

            cmp = new ViewClass({
                classId: classId,
                batchId: batchId,
                hideFlag: hideFlag
            });

            cmp.on('afterrender', function (panel) {
                var form = panel.child('form').getForm();
                var tzParams = '{"ComID":"TZ_REVIEW_CL_COM","PageID":"TZ_CLPS_KS_STD",' + '"OperateType":"QF","comParams":{"classId":"' + classId + '","batchId":"' + batchId + '"}}';

                Ext.tzLoad(tzParams, function (respData) {
                    var examineeGrid = panel.down('grid');
                    var tzStoreParams = '{"cfgSrhId": "TZ_REVIEW_CL_COM.TZ_CLPS_KS_STD.TZ_CLPS_KS_VW","condition":{"TZ_CLASS_ID-operator": "01","TZ_CLASS_ID-value": "' + classId + '","TZ_APPLY_PC_ID-operator": "01","TZ_APPLY_PC_ID-value": "' + batchId + '"}}';
                    examineeGrid.store.tzStoreParams = tzStoreParams;

                    var formData = respData.formData;
                    if (formData != "" && formData != undefined) {
                        panel.actType = "update";
                        formData.className = record.data.className;
                        formData.batchName = record.data.batchName;
                        form.setValues(formData);
                        examineeGrid.store.load();
                    } else {
                        panel.actType = "add";
                        form.findField("classId").setValue(classId);
                        form.findField("className").setValue(className);
                        form.findField("batchId").setValue(batchId);
                        form.findField("batchName").setValue(batchName);
                        form.findField("bkksNum").setValue(applicantsNumber);
                        form.findField("submitKsNum").setValue(submittedNumber);
                        form.findField("clpsksNum").setValue(0);
                    }
                });
            });

            tab = contentPanel.add(cmp);
            contentPanel.setActiveTab(tab);
            Ext.resumeLayouts(true);
            if (cmp.floating) {
                cmp.show();
            }
        /*});*/
    },
    
    //自动初筛
    automaticScreen: function(grid,rowIndex){
    	Ext.tzSetCompResourses("TZ_AUTO_SCREEN_COM");
		
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_AUTO_SCREEN_COM"]["TZ_AUTO_SCREEN_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_AUTO_SCREEN_STD，请检查配置。');
			return;
		}

		var contentPanel,cmp, className, ViewClass, clsProto;
		
		contentPanel = Ext.getCmp('tranzvision-framework-content-panel');			
		contentPanel.body.addCls('kitchensink-example');
	
		if(!Ext.ClassManager.isCreated(className)){
			Ext.syncRequire(className);
		}	
		
		ViewClass = Ext.ClassManager.get(className);
		
		var record = grid.store.getAt(rowIndex);
        var classID = record.data.classID;
        var batchID = record.data.batchID;
        var batchName = record.data.batchName;
        var className = record.data.className;

		cmp = new ViewClass({
			classId: classID,
			batchId: batchID,
			className:className,
			batchName: batchName
		});
		
		cmp.on('afterrender',function(gridPanel){});
		
		tab = contentPanel.add(cmp);     
		
		contentPanel.setActiveTab(tab);

		Ext.resumeLayouts(true);

		if (cmp.floating) {
			cmp.show();
		}
    },

    viewMszgMan:function(btn){
    	var panel=btn.findParentByType("grid").findParentByType("panel");
    	var classID=panel.classID;
    	var batchID=panel.batchID;
    	Ext.tzSetCompResourses("TZ_BMGL_BMBSH_COM");

        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_BMGL_BMBSH_COM"]["TZ_BMGL_MSZG_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_MSZG_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_MSZG_STD.nmyqx","您没有权限"));
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_MSZG_STD.prompt","提示"), Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_MSZG_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
            return;
        }
        var contentPanel, cmp, ViewClass, clsProto;

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
            // <debug warn>
            // Sometimes we forget to include allowances for other themes, so issue a warning as a reminder.
            if (!clsProto.themeInfo) {
                Ext.log.warn ( 'Example \'' + className + '\' lacks a theme specification for the selected theme: \'' +
                    themeName + '\'. Is this intentional?');
            }
            // </debug>
        }
        
    	var form=btn.findParentByType("grid").findParentByType("panel");
    	var classID=form.classID;
        var config = {
            classID:classID,
            batchID:batchID
        }
        cmp = new ViewClass(config);
        cmp.on('afterrender',function(panel){
            panel.store.tzStoreParams='{"cfgSrhId": "TZ_BMGL_BMBSH_COM.TZ_BMGL_MSZG_STD.TZ_MSZG_MAN_V","condition":{"TZ_CLASS_ID-operator":"01","TZ_CLASS_ID-value":"'+classID+'","TZ_BATCH_ID-operator":"01","TZ_BATCH_ID-value":"'+batchID+'"}}';
            panel.store.load();

        });
        tab = contentPanel.add(cmp);


        contentPanel.setActiveTab(tab);

        Ext.resumeLayouts(true);
        if (cmp.floating) {
            cmp.show();
        }
    },

    viewMsjgMan:function(btn){

    	var panel=btn.findParentByType("grid").findParentByType("panel");
    	var classID=panel.classID;
    	var batchID=panel.batchID;
    	Ext.tzSetCompResourses("TZ_BMGL_BMBSH_COM");
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_BMGL_BMBSH_COM"]["TZ_BMGL_MSJG_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_MSJG_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_MSJG_STD.nmyqx","您没有权限"));
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_MSJG_STD.prompt","提示"), Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_MSJG_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
            return;
        }
        var contentPanel, cmp, ViewClass, clsProto;

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
            // <debug warn>
            // Sometimes we forget to include allowances for other themes, so issue a warning as a reminder.
            if (!clsProto.themeInfo) {
                Ext.log.warn ( 'Example \'' + className + '\' lacks a theme specification for the selected theme: \'' +
                    themeName + '\'. Is this intentional?');
            }
            // </debug>
        }
        
    	var form=btn.findParentByType("grid").findParentByType("panel");
    	var classID=form.classID;
        var config = {
            classID:classID,
            batchID:batchID
        }
        cmp = new ViewClass(config);
        cmp.on('afterrender',function(panel){
            panel.store.tzStoreParams='{"cfgSrhId": "TZ_BMGL_BMBSH_COM.TZ_BMGL_MSJG_STD.TZ_MSJG_MAN_V","condition":{"TZ_CLASS_ID-operator":"01","TZ_CLASS_ID-value":"'+classID+'","TZ_BATCH_ID-operator":"01","TZ_BATCH_ID-value":"'+batchID+'"}}';
            panel.store.load();

        });
        tab = contentPanel.add(cmp);


        contentPanel.setActiveTab(tab);

        Ext.resumeLayouts(true);
        if (cmp.floating) {
            cmp.show();
        }
    },
    viewXMsjgMan:function(btn){
    	var panel=btn.findParentByType("grid").findParentByType("panel");
    	var classID=panel.classID;
    	var batchID=panel.batchID;
    	Ext.tzSetCompResourses("TZ_BMGL_BMBSH_COM");
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_BMGL_BMBSH_COM"]["TZ_BMGL_XMSJG_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_XMSJG_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.XTZ_BMGL_MSJG_STD.nmyqx","您没有权限"));
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_XMSJG_STD.prompt","提示"), Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_XMSJG_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
            return;
        }
        var contentPanel, cmp, ViewClass, clsProto;

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
            // <debug warn>
            // Sometimes we forget to include allowances for other themes, so issue a warning as a reminder.
            if (!clsProto.themeInfo) {
                Ext.log.warn ( 'Example \'' + className + '\' lacks a theme specification for the selected theme: \'' +
                    themeName + '\'. Is this intentional?');
            }
            // </debug>
        }
        
    	var form=btn.findParentByType("grid").findParentByType("panel");
    	var classID=form.classID;
        var config = {
            classID:classID,
            batchID:batchID
        }
        cmp = new ViewClass(config);
        cmp.on('afterrender',function(panel){
            panel.store.tzStoreParams='{"cfgSrhId": "TZ_BMGL_BMBSH_COM.TZ_BMGL_XMSJG_STD.TZ_XMSJG_MAN_V","condition":{"TZ_CLASS_ID-operator":"01","TZ_CLASS_ID-value":"'+classID+'","TZ_BATCH_ID-operator":"01","TZ_BATCH_ID-value":"'+batchID+'"}}';
            panel.store.load();

        });
        tab = contentPanel.add(cmp);


        contentPanel.setActiveTab(tab);

        Ext.resumeLayouts(true);
        if (cmp.floating) {
            cmp.show();
        }
    },
    viewBscjMan:function(btn){

    	var panel=btn.findParentByType("grid").findParentByType("panel");
    	var classID=panel.classID;
    	var batchID=panel.batchID;
    	Ext.tzSetCompResourses("TZ_BMGL_BMBSH_COM");
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_BMGL_BMBSH_COM"]["TZ_BMGL_BSCJ_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_BSCJ_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_BSCJ_STD.nmyqx","您没有权限"));
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_BSCJ_STD.prompt","提示"), Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_BSCJ_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
            return;
        }
        var contentPanel, cmp, ViewClass, clsProto;

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
            // <debug warn>
            // Sometimes we forget to include allowances for other themes, so issue a warning as a reminder.
            if (!clsProto.themeInfo) {
                Ext.log.warn ( 'Example \'' + className + '\' lacks a theme specification for the selected theme: \'' +
                    themeName + '\'. Is this intentional?');
            }
            // </debug>
        }
        
    	var form=btn.findParentByType("grid").findParentByType("panel");
    	var classID=form.classID;
        var config = {
            classID:classID,
            batchID:batchID
        }
        cmp = new ViewClass(config);
        cmp.on('afterrender',function(panel){
            panel.store.tzStoreParams='{"cfgSrhId": "TZ_BMGL_BMBSH_COM.TZ_BMGL_BSCJ_STD.TZ_BSCJ_MAN_V","condition":{"TZ_CLASS_ID-operator":"01","TZ_CLASS_ID-value":"'+classID+'","TZ_BATCH_ID-operator":"01","TZ_BATCH_ID-value":"'+batchID+'"}}';
            panel.store.load();

        });
        tab = contentPanel.add(cmp);


        contentPanel.setActiveTab(tab);

        Ext.resumeLayouts(true);
        if (cmp.floating) {
            cmp.show();
        }
    },
    viewLqjgMan:function(btn){

    	var panel=btn.findParentByType("grid").findParentByType("panel");
    	var classID=panel.classID;
    	var batchID=panel.batchID;
    	Ext.tzSetCompResourses("TZ_BMGL_BMBSH_COM");
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_BMGL_BMBSH_COM"]["TZ_BMGL_LQJG_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_LQJG_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_LQJG_STD.nmyqx","您没有权限"));
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_LQJG_STD.prompt","提示"), Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_LQJG_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
            return;
        }
        var contentPanel, cmp, ViewClass, clsProto;

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
        
    	var form=btn.findParentByType("grid").findParentByType("panel");
    	var classID=form.classID;
        var config = {
            classID:classID,
            batchID:batchID
        }
        cmp = new ViewClass(config);
        cmp.on('afterrender',function(panel){
            panel.store.tzStoreParams='{"cfgSrhId": "TZ_BMGL_BMBSH_COM.TZ_BMGL_LQJG_STD.TZ_LQJG_MAN_V","condition":{"TZ_CLASS_ID-operator":"01","TZ_CLASS_ID-value":"'+classID+'","TZ_BATCH_ID-operator":"01","TZ_BATCH_ID-value":"'+batchID+'"}}';
            panel.store.load();

        });
        tab = contentPanel.add(cmp);


        contentPanel.setActiveTab(tab);

        Ext.resumeLayouts(true);
        if (cmp.floating) {
            cmp.show();
        }
    },
    onPrjClose:function(btn){
    	 //关闭窗口
        this.getView().close();
    },
    //查询
    queryPrj:function(btn){
		Ext.tzShowCFGSearch({
			cfgSrhId: 'TZ_BMGL_BMBPICI_COM.TZ_BMGL_PRJ_STD.TZ_PRJ_TYPE_T',
			condition:
            {
                "TZ_JG_ID": Ext.tzOrgID
            }, 
			callback: function(seachCfg){
				var store = btn.findParentByType("grid").store;
				store.tzStoreParams = seachCfg;
				store.load();
			}
		});
    },
    
	/*给搜索结果发送邮件*/
    sendEmlSelPersOfAll:function(btn) {

    	var strAppId;   
    	var store = btn.findParentByType("grid").store;
		var strConfSearCond=btn.findParentByType('classInfoB').strConfSearCond;
		if (strConfSearCond.length==0) {
			strConfSearCond="{\"cfgSrhId\":\"TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.TZ_APP_LIST_VW\",\"condition\": {\"TZ_CLASS_ID-operator\": \"01\", \"TZ_CLASS_ID-value\":\""+store.classID +"\",\"TZ_BATCH_ID-operator\": \"01\", \"TZ_BATCH_ID-value\":\""+store.batchID +"\"}}";
			
		}
		
        //顾贤达 2017年6月26日 11:08:54 修改SQL提交逻辑
		var tzParams = '{"ComID":"TZ_BMGL_BMBSH_COM","PageID":"TZ_BMGL_STU_STD","OperateType":"tzGetAppIdAndOprID","comParams":'+strConfSearCond+'}';
		console.log(tzParams);
		
		Ext.tzLoadAsync(tzParams,function(resp){
			 if(resp.AppID!=undefined){
					strAppId =resp.AppID;
	            }
	    },"",true,this);
		console.log(strAppId);
    	
		var personList = [];
		var strs= new Array(); 
		strs=strAppId.split(";"); 
    /*	
		var strSQL=btn.findParentByType('classInfoB').strSQL;
    	if (strSQL.length==0) {
    		var store = btn.findParentByType("grid").store;
    		var OriSQL="SELECT TZ_APP_INS_ID FROM PS_TZ_APP_LIST_VW where TZ_CLASS_ID='"+store.classID+"' AND TZ_BATCH_ID='"+store.batchID+"'";
    		
    		getAppIdSQL2=OriSQL;
		}else{
			getAppIdSQL2=strSQL;
		}
		 
		 var strAppId; 
		 var tzParams = '{"ComID":"TZ_BMGL_BMBSH_COM","PageID":"TZ_BMGL_STU_STD","OperateType":"tzGetAppIdAndOprID","comParams":{"getAppIdSQL":"'+getAppIdSQL2+'"}}';
		//后台执行查询操作
		Ext.tzLoadAsync(tzParams,function(resp){
			if(resp.AppID!=undefined){
					strAppId =resp.AppID;
	            }
	    },"",true,this);
		
		var personList = [];
		var strs= new Array(); 
		strs=strAppId.split(";"); 
		*/
		for (i=0;i<strs.length ;i++ ){ 
			var strs2= new Array();
			strs2=strs[i].split("+");
				var oprID = strs2[1];
		        var appInsID = strs2[0];
		        personList.push({"oprID": oprID, "appInsID": appInsID});
		}  
     
        var params = {
            "ComID": "TZ_BMGL_BMBSH_COM",
            "PageID": "TZ_BMGL_YJDX_STD",
            "OperateType": "U",
            "comParams": {"add": [
                {"type": 'MULTI', "personList": personList}
            ]}
        };
        
        Ext.tzLoad(Ext.JSON.encode(params), function (responseData) {
            Ext.tzSendEmail({
                //发送的邮件模板;
//                "EmailTmpName": ["TZ_EML_N_001"],
                "EmailTmpName": ["TZ_EML_N_001","TZ_MAL_NAME_CN"],
                //创建的需要发送的听众ID;
                "audienceId": responseData,
                //是否有附件: Y 表示可以发送附件,"N"表示无附件;
                "file": "N"
            });
        });
        
        getAppIdSQL2="";
        
    },
    
	/*给搜索结果发送短信*/
    sendSmsSelPersOfAll:function(btn) {

    	var strAppId;   
    	var store = btn.findParentByType("grid").store;
		var strConfSearCond=btn.findParentByType('classInfoB').strConfSearCond;
		if (strConfSearCond.length==0) {
			strConfSearCond="{\"cfgSrhId\":\"TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.TZ_APP_LIST_VW\",\"condition\": {\"TZ_CLASS_ID-operator\": \"01\", \"TZ_CLASS_ID-value\":\""+store.classID +"\",\"TZ_BATCH_ID-operator\": \"01\", \"TZ_BATCH_ID-value\":\""+store.batchID +"\"}}";
			}
		
        //顾贤达 2017年6月26日 11:08:54 修改SQL提交逻辑
		var tzParams = '{"ComID":"TZ_BMGL_BMBSH_COM","PageID":"TZ_BMGL_STU_STD","OperateType":"tzGetAppIdAndOprID","comParams":'+strConfSearCond+'}';
		console.log(tzParams);
		
		Ext.tzLoadAsync(tzParams,function(resp){
			 if(resp.AppID!=undefined){
					strAppId =resp.AppID;
	            }
	    },"",true,this);
		console.log(strAppId);

		/*var strSQL=btn.findParentByType('classInfoB').strSQL;
    	if (strSQL.length==0) {
    		var store = btn.findParentByType("grid").store;
    		var OriSQL="SELECT TZ_APP_INS_ID FROM PS_TZ_APP_LIST_VW where TZ_CLASS_ID='"+store.classID+"' AND TZ_BATCH_ID='"+store.batchID+"'";
    		
    		getAppIdSQL2=OriSQL;
		}else{
			getAppIdSQL2=strSQL;
		}
		 
		 var strAppId; 
		 var tzParams = '{"ComID":"TZ_BMGL_BMBSH_COM","PageID":"TZ_BMGL_STU_STD","OperateType":"tzGetAppIdAndOprID","comParams":{"getAppIdSQL":"'+getAppIdSQL2+'"}}';
		//后台执行查询操作
		Ext.tzLoadAsync(tzParams,function(resp){
			if(resp.AppID!=undefined){
					strAppId =resp.AppID;
	            }
	    },"",true,this);*/
		
		var personList = [];
		var strs= new Array(); 
		strs=strAppId.split(";"); 
		
		for (i=0;i<strs.length ;i++ ){ 
			var strs2= new Array();
			strs2=strs[i].split("+");
				var oprID = strs2[1];
		        var appInsID = strs2[0];
		        personList.push({"oprID": oprID, "appInsID": appInsID});
		}  
     
        var params = {
            "ComID": "TZ_BMGL_BMBSH_COM",
            "PageID": "TZ_BMGL_YJDX_STD",
            "OperateType": "U",
            "comParams": {"add": [
                {"type": 'MULTI', "personList": personList}
            ]}
        };
        
        Ext.tzLoad(Ext.JSON.encode(params), function (responseData) {
        	 Ext.tzSendSms({
                 //发送的短信模板;
                 "SmsTmpName": ["TZ_SMS_N_002"],
                //创建的需要发送的听众ID;
                "audienceId": responseData,
                //是否有附件: Y 表示可以发送附件,"N"表示无附件;
                "file": "N"
            });
        });
        
        getAppIdSQL2="";
        
    },
    viewSiteUser:function(grid,html,rowIndex,colIndex,table,record){
        var prjID = record.data.prjID;
        var contentPanel, className, ViewClass, cmp,bugID;
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_BMGL_BMBPICI_COM"]["TZ_BMGL_CLASS_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        className = pageResSet["jsClassName"];
        contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
        contentPanel.body.addCls('kitchensink-example');
        if (!Ext.ClassManager.isCreated(className)) {
            Ext.syncRequire(className);
        }
  	var ViewClass =Ext.ClassManager.get('KitchenSink.view.enrollmentManagement.applicationBatch.classManagement');
  	config={
  			prjID:prjID	
  	     };	
  	var cmp =new ViewClass(config);
		 tab = contentPanel.add(cmp);
		cmp.on('afterrender',function(panel){
			var store = panel.getStore();
	        	var tzStoreParams = {
	            		"cfgSrhId": "TZ_BMGL_BMBPICI_COM.TZ_BMGL_CLASS_STD.TZ_CLASS_BATCH_V",
	            		"condition":{
	            			"TZ_DLZH_ID-operator": "01",
	            			"TZ_DLZH_ID-value": TranzvisionMeikecityAdvanced.Boot.loginUserId,
	            			"TZ_JG_ID-operator": "01",
	            			"TZ_JG_ID-value": Ext.tzOrgID,
	            			"TZ_IS_APP_OPEN-operator":"01",
	            			"TZ_IS_APP_OPEN-value":"Y",
	            			"TZ_PRJ_TYPE_ID-operator":"01",
	            			"TZ_PRJ_TYPE_ID-value":prjID
	            				}
	            };
	        store.tzStoreParams=Ext.encode(tzStoreParams);
	        store.reload();
			
		});
		
		contentPanel.setActiveTab(tab);
      Ext.resumeLayouts(true);
      if (cmp.floating) {
          cmp.show();
      }
  },
    //调整考生报考项目，卢艳添加，2017-9-5
    changeProject: function (btn) {
        var view = this.getView();
        var grid = btn.findParentByType("grid");
        var selList = grid.getSelectionModel().getSelection();

        var classId = "", opridList = "", nameList = "";

        if (selList.length == 0) {
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt", "提示"), Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.youSelectedNothing", "您没有选中任何记录"));
            return;
        } else {
            for (var i = 0; i < selList.length; i++) {
                classId = selList[i].data.classID;
                if (opridList == "") {
                    opridList = selList[i].data.oprID;
                    nameList = selList[i].data.stuName;
                } else {
                    opridList = opridList + "," + selList[i].data.oprID;
                    nameList = nameList + "," + selList[i].data.stuName;
                }
            }
        }

        Ext.tzSetCompResourses("TZ_BMGL_BMBSH_COM");
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_BMGL_BMBSH_COM"]["TZ_BMGL_TZXM_STD"];
        if (pageResSet == "" || pageResSet == undefined) {
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt", "提示"), Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.nmyqx", "您没有权限"));
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if (className == "" || className == undefined) {
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt", "提示"), Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.wzdgjs", "未找到该功能页面对应的JS类，请检查配置。"));
            return;
        }

        var contentPanel, cmp, ViewClass, clsProto;

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
                clsProto.themeInfo = Ext.applyIf(clsProto.themeInfo || {}, clsProto.themes.classic);
            } else if (themeName !== 'neptune' && themeName !== 'classic') {
                if (themeName === 'crisp-touch') {
                    clsProto.themeInfo = Ext.applyIf(clsProto.themeInfo || {}, clsProto.themes['neptune-touch']);
                }
                clsProto.themeInfo = Ext.applyIf(clsProto.themeInfo || {}, clsProto.themes.neptune);
            }

            if (!clsProto.themeInfo) {
                Ext.log.warn('Example \'' + className + '\' lacks a theme specification for the selected theme: \'' +
                    themeName + '\'. Is this intentional?');
            }
        }

        var render = function(initialData){
            cmp = new ViewClass({
                classId:classId,
                opridList:opridList,
                nameList:nameList,
                initialData:initialData,
                from:'BMSH'
            });

            cmp.show();
        }


        var classData = [],
            classBatchData = [];

        var classDataParams = '{"ComID":"TZ_BMGL_BMBSH_COM","PageID":"TZ_BMGL_TZXM_STD","OperateType":"tzGetClassInfo","comParams":{"classId":"'+classId+'"}}';

        var times = 1;
        var beforeRender = function(){
            times--;
            if(times==0){
                render({
                    classData:classData,
                    classBatchData:classBatchData
                });
            }
        };

        Ext.tzLoad(classDataParams, function (responseData) {
            classData = responseData.classData;
            classBatchData = responseData.classBatchData;
            beforeRender();
        });

    },
  
  exportSerachResult:function(btn){
  	
  	var grid = btn.findParentByType("grid");
  	 grid.saveDocumentAs({
  	     type: 'excel',
  	     title: '报名表审核结果',
  	     fileName: 'BMBresult.xls'
  	 });
  }
});

