Ext.define('KitchenSink.view.studentBbTj.studentBbTj', {
    extend: 'Ext.panel.Panel',
    xtype: 'studentBbTj',
    requires: [
        'Ext.data.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
		'Ext.data.TreeStore',
		'Ext.layout.container.Table',
		'KitchenSink.view.studentBbTj.studentBbTjTreeStore',
		'KitchenSink.view.studentBbTj.studentYearStore',
		'KitchenSink.view.studentBbTj.studentBbTjStore',
		'KitchenSink.view.studentBbTj.studentBbTjController',
		'KitchenSink.view.common.store.comboxStore'
    ],
    controller: 'studentBbTjController',
    title: '考生报表统计',
    bodyStyle:'overflow-y:hidden;overflow-x:hidden',
	style:"margin:8px",
    layout: 'border',
    modal:true,
    viewModel: true,
    frame: true,
    initComponent: function() {
    	
    	var studentBbTjTreeStore = new KitchenSink.view.studentBbTj.studentBbTjTreeStore();
    	var studentYearStore = new KitchenSink.view.studentBbTj.studentYearStore();
    	
    	//年龄;
    	var studentBbTjAgeStore = new KitchenSink.view.studentBbTj.studentBbTjStore();
    	//院校
    	var studentBbTjSchoolStore = new KitchenSink.view.studentBbTj.studentBbTjStore();
    	//工龄
    	var studentBbTjWorkAgeStore = new KitchenSink.view.studentBbTj.studentBbTjStore();
    	//年薪
    	var studentBbTjWorkSaralyStore = new KitchenSink.view.studentBbTj.studentBbTjStore(); 
    	//专业类别
    	var studentBbTjMajorTypeStore = new KitchenSink.view.studentBbTj.studentBbTjStore(); 
    	//教育背景
    	var studentBbTjEducationStore = new KitchenSink.view.studentBbTj.studentBbTjStore(); 
    	//入学前单位性质
    	var studentBbTjInsStore = new KitchenSink.view.studentBbTj.studentBbTjStore(); 
    	//入学前行业背景
    	var studentBbTjIndStore = new KitchenSink.view.studentBbTj.studentBbTjStore(); 
    	//管理经验
    	var studentBbTjExperienceStore = new KitchenSink.view.studentBbTj.studentBbTjStore();
    	//性别
    	var studentBbTjSexStore = new KitchenSink.view.studentBbTj.studentBbTjStore();
    	
    	//颜色类别
    	var orgColorSortStore = new KitchenSink.view.common.store.comboxStore({
            recname:'TZ_ORG_COLOR_V',
            condition:{
                TZ_JG_ID:{
                    value:Ext.tzOrgID,
                    operator:'01',
                    type:'01'
                }},
            result:'TZ_COLOR_SORT_ID,TZ_COLOR_NAME,TZ_COLOR_CODE'
        });
    	
    	
        this.items = [
            {
                title: '选择项目',
                region:'west',
                header:true,
                xtype: 'treepanel',
                width: 280,
                split: true,
                collapsible: true,
                autoScroll : true,
                lines: true,
                rootVisible: false,
                store: studentBbTjTreeStore,
                listeners : {
                	itemclick: function(vieww,record, item, index, e, eOpts ){
                		var isCheck = record.get('checked');
					 	record.set('checked', !isCheck);
                	}
            		,checkchange: function(node, checked, obj){
            			node.set('checked', !checked);
            		}
                }
            },{
            	xtype:'panel',
                region: 'center',
                title: '报表统计',
                autoScroll: true,
                viewModel: true,
                bodyStyle: 'overflow-y:hidden;overflow-x:hidden',
                name:'studentCenterPanel',
                items:[{
	                 xtype: 'form',
	                 reference: 'studentBbTjForm',
	                 name: 'studentBbTjForm',
	                 layout: {
	                        type: 'vbox',
	                        align: 'stretch'
	                 },
	                 border: false,
	                 bodyPadding: 10,
	            	 bodyStyle:'overflow-y:auto;overflow-x:hidden',
	                 fieldDefaults: {
	                    msgTarget: 'side',
	                    labelWidth: 120,
	                    labelStyle: 'font-weight:bold'
	                 },
	                 items: [{
	                	 xtype: 'tagfield',
	                	 reference: 'years',
	                	 name: 'years',
	                	 fieldLabel: '年份',
	                	 displayField: 'desc',
	                     valueField: 'id',
	                	 autoScroll: true,
	                     createNewOnEnter: false,
	                     createNewOnBlur: false,
	                     filterPickList: true,
	                	 queryMode: 'local',
	                	 publishes: 'value',
	                	 store: studentYearStore
						 
	                 },{
	                	 xtype: 'tagfield',
	                	 reference: 'bmbStatus',
	                	 name: 'bmbStatus',
	                	 fieldLabel: '报名表状态',
	                	 displayField: 'TSDesc',
	                     valueField: 'TValue',
	                	 autoScroll: true,
	                     createNewOnEnter: false,
	                     createNewOnBlur: false,
	                     filterPickList: true,
	                	 queryMode: 'local',
	                	 publishes: 'value',
	                	 store: new KitchenSink.view.common.store.appTransStore("TZ_APPFORM_STATE")
						 
	                 },{
	                	 xtype: 'tagfield',
	                	 reference: 'msZg',
	                	 name: 'msZg',
	                	 fieldLabel: '面试资格',
	                	 displayField: 'TSDesc',
	                     valueField: 'TValue',
	                	 autoScroll: true,
	                     createNewOnEnter: false,
	                     createNewOnBlur: false,
	                     filterPickList: true,
	                	 queryMode: 'local',
	                	 publishes: 'value',
	                	 //store: new KitchenSink.view.common.store.appTransStore("TZ_MSZG")
	                	 store:{
								fields: [{name:'TValue'},{name:'TSDesc'}],
								data: [{TValue: '是', TSDesc: '是'},{TValue: '否', TSDesc: '否'},{TValue: '等待', TSDesc: '等待'}]
						}
						 
	                 },{
	                	 xtype: 'tagfield',
	                	 reference: 'msJg',
	                	 name: 'msJg',
	                	 fieldLabel: '面试结果',
	                	 displayField: 'TSDesc',
	                     valueField: 'TValue',
	                	 autoScroll: true,
	                     createNewOnEnter: false,
	                     createNewOnBlur: false,
	                     filterPickList: true,
	                	 queryMode: 'local',
	                	 publishes: 'value',
	                	 store: new KitchenSink.view.common.store.appTransStore("TZ_MSJG")
	                 },{
	                	 xtype: 'tagfield',
	                	 reference: 'lqZt',
	                	 name: 'lqZt',
	                	 fieldLabel: '录取状态',
	                	 displayField: 'TSDesc',
	                     valueField: 'TValue',
	                	 autoScroll: true,
	                     createNewOnEnter: false,
	                     createNewOnBlur: false,
	                     filterPickList: true,
	                	 queryMode: 'local',
	                	 publishes: 'value',
	                	 //store: new KitchenSink.view.common.store.appTransStore("TZ_LQZT")
	                	 store:{
								fields: [{name:'TValue'},{name:'TSDesc'}],
								data: [{TValue: '已录取', TSDesc: '已录取'},{TValue: '未录取', TSDesc: '未录取'}]
						}
						 
	                 },{
	                     xtype: 'checkboxfield',
	                     name : 'fq',
	                     fieldLabel: '不统计放弃'
	                 }/* {
	                        xtype: 'tagfield',
	                        fieldLabel:'颜色类别',
	                        name: 'colorType',
	                        reference: 'colorType',
	                        //emptyText:'请选择...',
	                        queryMode:'local',
	                        publishes: 'value',
	                        valueField: 'TZ_COLOR_SORT_ID',
	                        displayField: 'TZ_COLOR_NAME',
	                        createNewOnEnter: false,
		                    createNewOnBlur: false,
		                    filterPickList: true,
		                    store:orgColorSortStore,
		                    tpl: Ext.create('Ext.XTemplate',
		                            '<tpl for=".">',
		                            '<div class="x-boundlist-item"><div class="x-colorpicker-field-swatch-inner" style="margin-top:6px;width:30px;height:50%;background-color: #{TZ_COLOR_CODE}"></div><div style="margin-left:40px;display: block;  overflow:  hidden; white-space: nowrap; -o-text-overflow: ellipsis; text-overflow:  ellipsis;"> {TZ_COLOR_NAME}</div></div>',
		                            '</tpl>',{}
		                    )
	                    }*/,{
	                	 xtype: 'toolbar',
	                	 style: "margin-left:120px",
	                	 items: [{
	                		 xtype: 'button',
		                	 text: '计算/重新计算',
							 name: 'searchButton',
							 listeners: {
								click:  function(btn){
							    	//console.log(btn);
							    	var studentBbTj = btn.findParentByType("studentBbTj");
							    	var form = studentBbTj.down("form[name=studentBbTjForm]").getForm();
							    	
							    	//console.log(form.getValues());
							    	//console.log(form.getFieldValues());
							    	
							    	//console.log(studentBbTj);
							    	
							    	
							    	//分类,项目,班级,批次;
							        var i = 0,j = 0,k = 0,h = 0;
							        var typ = new Array(), prj = new Array(),cls = new Array(), bat = new Array();
							    	
							        var tree = studentBbTj.down("treepanel");
							      //console.log(tree);
							    	var selNodes = tree.getChecked();
							    	Ext.each(selNodes, function(node){
							    		//console.log(node);
							    		//console.log(node.data.nodeId + "===>" + node.data.text);
							    		//alert(node.nodeId);
							    		var nodeId = node.data.nodeId;
							    		//console.log(nodeId);
							    		if(nodeId.indexOf("TYP_") == 0){
							    			typ[i] = nodeId.substring(4,nodeId.length);
							    			i ++;
							    		}
							    		
							    		if(nodeId.indexOf("PRJ_") == 0){
							    			prj[j] = nodeId.substring(4,nodeId.length);
							    			j ++;
							    		}
							    		
							    		if(nodeId.indexOf("CLS_") == 0){
							    			cls[k] = nodeId.substring(4,nodeId.length);
							    			k ++;
							    		}
							    		
							    		if(nodeId.indexOf("BAT_") == 0){
							    			bat[h] = nodeId.substring(4,nodeId.length);
							    			h ++;
							    		}
							    	});
							    	
							    	
							    	//分布;
							    	var tzParameter = {
							    		"ComID": "TZ_STU_BBTJ_COM",	
							    		"PageID": "TZ_STU_BBTJ_STD",	
							    		"OperateType": "QG",
							    		"comParams": {
								    		"formDate": form.getFieldValues(),
								    		"typ": typ,
								    		"cls": cls,
								    		"prj": prj,
								    		"bat": bat
							    		}
							    	}
							    	var tzParameterJson = Ext.JSON.encode(tzParameter);
						
							    	
							    	var tjTabPanel = studentBbTj.down("tabpanel");
							    	
							    	var grid1 = tjTabPanel.down("grid[name=grid1]").getStore();
							    	var grid2 = tjTabPanel.down("grid[name=grid2]").getStore();
							    	var grid3 = tjTabPanel.down("grid[name=grid3]").getStore();
							    	var grid4 = tjTabPanel.down("grid[name=grid4]").getStore();
							    	var grid5 = tjTabPanel.down("grid[name=grid5]").getStore();
							    	var grid6 = tjTabPanel.down("grid[name=grid6]").getStore();
							    	var grid7 = tjTabPanel.down("grid[name=grid7]").getStore();
							    	var grid8 = tjTabPanel.down("grid[name=grid8]").getStore();
							    	var grid9 = tjTabPanel.down("grid[name=grid9]").getStore();
							    	var grid10 = tjTabPanel.down("grid[name=grid10]").getStore();
							    	
							    	//tab1.tzStoreParams = tzParameterJson;
							    	//开始
							    	//等待信息
							        //Ext.MessageBox.wait("加载中", "请稍候");
							    	var target;
							        var myMask = new Ext.LoadMask(
							            {
							                msg    : TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00022"),
							                target : target!=undefined?target:Ext.getCmp('tranzvision-framework-content-panel')
							            });

							        myMask.show();
							        
							        try
							        {
							            //获取数据
							            Ext.Ajax.request(
							                {
							                    url: TranzvisionMeikecityAdvanced.Boot.mainapp_interaction_url,
							                    params:{tzParams: tzParameterJson},
							                    timeout: 120000,
							                    success: function(response, opts)
							                    {
							                        //返回值内容
							                        var jsonText = response.responseText;
							                        try
							                        {
							                            var jsonObject = Ext.util.JSON.decode(jsonText);
							                            
							                            if(jsonObject.state.errcode == 0)
							                            {
							                                
							                              //年龄分布
							                                var responseData = jsonObject.comContent;
															var ageData = responseData.age.root;
															grid1.loadData(ageData,false);
															//平均年龄;
															//console.log(tjTabPanel.getActiveTab()); 
															var agePj = responseData.age.agePj;
															//var agePjComponent = tjTabPanel.down("component[name=agePj]");
															var activeTab = tjTabPanel.getActiveTab();
															//var pj = activeTab.down("component[name=pj]");
															//pj.getEl().update('平均年龄:' + agePj);
															//console.log(agePjComponent);
															//console.log(agePjComponent.getEl());
															//agePjComponent.getEl().update('平均年龄:' + agePj);
															
															//var workAgePjComponent = tjTabPanel.down("component[name=workAgePj]");
															//workAgePjComponent.getEl().update('平均年龄:' + agePj);
															
															
															//毕业院校
															var schoolData = responseData.school.root;
															grid2.loadData(schoolData,false);
															
															//工龄范围
															var workAgeData = responseData.workAge.root;
															grid3.loadData(workAgeData,false);
															
															//年薪范围
															var workSalaryData = responseData.workSalary.root;
															grid4.loadData(workSalaryData,false);
															
															//专业类别
															var majorTypeData = responseData.majorType.root;
															grid5.loadData(majorTypeData,false);
															
															//教育背景
															var educationData = responseData.education.root;
															grid6.loadData(educationData,false);
															
															//入学前单位性质
															var insData = responseData.ins.root;
															grid7.loadData(insData,false);
															
															//入学前行业背景
															var indData = responseData.ind.root;
															grid8.loadData(indData,false);
															
															//管理经验
															var experienceData = responseData.experience.root;
															grid9.loadData(experienceData,false);
															
															//性别
															var sexData = responseData.sex.root;
															grid10.loadData(sexData,false);
															
															studentBbTj.commitChanges(studentBbTj);
							                            }
							                            else if(jsonObject.state.timeout == true)
							                            {
							                                try
							                                {
							                                    if(Ext.getCmp("tranzvision_relogin_20150626") == undefined)
							                                    {
																	Ext.Msg.alert(TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00021"),TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00101"),function(optional){
																		if(optional=="ok"){											
																			var org=Ext.tzOrgID.toLowerCase();
																			window.location.href= TzUniversityContextPath + "/login/"+org;
																		}
																	});
							                                    }
							                                }
							                                catch(e2)
							                                {}
							                            }
							                            else
							                            {
							                                TranzvisionMeikecityAdvanced.Boot.errorMessage(jsonObject.state.errdesc);
							                            }
							                        }
							                        catch(e)
							                        {
							                            TranzvisionMeikecityAdvanced.Boot.errorMessage(e.toString());
							                        }
							                    },
							                    failure: function(response, opts)
							                    {
							                        //错误信息响应报文
							                        try
							                        {
							                            var respText = Ext.util.JSON.decode(response.responseText);
							                            TranzvisionMeikecityAdvanced.Boot.errorMessage(respText.error);
							                        }
							                        catch(e2)
							                        {
							                            if(response.responseText == undefined || response.responseText == "")
							                            {
							                                TranzvisionMeikecityAdvanced.Boot.errorMessage(boot.getMessage("TZGD_FWINIT_00026"));
							                            }
							                            else
							                            {
							                                TranzvisionMeikecityAdvanced.Boot.errorMessage(response.responseText);
							                            }
							                        }
							                    },
							                    callback: function(opts,success,response)
							                    {
							                        myMask.hide();
							                    }
							                });
							        }
							        catch(e1)
							        {
							            boot.errorMessage(boot.getMessage("TZGD_FWINIT_00026"));
							            myMask.hide();
							        }
							    	
							    	//结束
									//加载数据
							        /*
									Ext.tzLoad(tzParameterJson,function(responseData){
										//年龄分布
										var ageData = responseData.age.root;
										grid1.loadData(ageData,false);
										//平均年龄;
										//console.log(tjTabPanel.getActiveTab()); 
										var agePj = responseData.age.agePj;
										//var agePjComponent = tjTabPanel.down("component[name=agePj]");
										var activeTab = tjTabPanel.getActiveTab();
										//var pj = activeTab.down("component[name=pj]");
										//pj.getEl().update('平均年龄:' + agePj);
										//console.log(agePjComponent);
										//console.log(agePjComponent.getEl());
										//agePjComponent.getEl().update('平均年龄:' + agePj);
										
										//var workAgePjComponent = tjTabPanel.down("component[name=workAgePj]");
										//workAgePjComponent.getEl().update('平均年龄:' + agePj);
										
										
										//毕业院校
										var schoolData = responseData.school.root;
										grid2.loadData(schoolData,false);
										
										//工龄范围
										var workAgeData = responseData.workAge.root;
										grid3.loadData(workAgeData,false);
										
										//年薪范围
										var workSalaryData = responseData.workSalary.root;
										grid4.loadData(workSalaryData,false);
										
										//专业类别
										var majorTypeData = responseData.majorType.root;
										grid5.loadData(majorTypeData,false);
										
										//教育背景
										var educationData = responseData.education.root;
										grid6.loadData(educationData,false);
										
										//入学前单位性质
										var insData = responseData.ins.root;
										grid7.loadData(insData,false);
										
										//入学前行业背景
										var indData = responseData.ind.root;
										grid8.loadData(indData,false);
										
										//管理经验
										var experienceData = responseData.experience.root;
										grid9.loadData(experienceData,false);
										
										//性别
										var sexData = responseData.sex.root;
										grid10.loadData(sexData,false);
										
										studentBbTj.commitChanges(studentBbTj);
									});
							    	*/
							    	//var data = [{"TZ_ZHZ_ID":"A","TZ_ZHZ_DMS":"小于24","name":"小于24","num":3,"scale":"7.5000%"},{"TZ_ZHZ_ID":"B","TZ_ZHZ_DMS":"24~26岁","name":"24~26岁","num":1,"scale":"2.5000%"},{"TZ_ZHZ_ID":"C","TZ_ZHZ_DMS":"26~30岁","name":"26~30岁","num":0,"scale":"0.0000%"},{"TZ_ZHZ_ID":"D","TZ_ZHZ_DMS":"31~34岁","name":"31~34岁","num":0,"scale":"0.0000%"},{"TZ_ZHZ_ID":"E","TZ_ZHZ_DMS":"35岁以上","name":"35岁以上","num":0,"scale":"0.0000%"},{"TZ_ZHZ_ID":"F","TZ_ZHZ_DMS":"未知","name":"未知","num":36,"scale":"90.0000%"}];
							    	//tab1.loadData(data,false);
							    	//tab1.load();
							    	
							    	//console.log(tjTabPanel);
							    	//var tab1 = tjTabPanel.down("component[name=tab1]");
							    	//tab1.getEl().update('<table cellpadding="0" cellspacing="0"  width="100%" border="1" style="margin-left: 20;margin-right: 20;"><tr><td width="30%">1</td><td width="30%">2</td><td>3</td></tr><tr><td>4</td><td>5</td><td>6</td></tr></table>');
							    }
							}
	                	 },{
	                		 xtype: 'button',
		                	 text: '导出到Excel',
							 name: 'excelButton',
							 listeners: {
								click:  function(btn){
							    	var studentBbTj = btn.findParentByType("studentBbTj");
							    	var form = studentBbTj.down("form[name=studentBbTjForm]").getForm();

							    	//分类,项目,班级,批次;
							        var i = 0,j = 0,k = 0,h = 0;
							        var typ = new Array(), prj = new Array(),cls = new Array(), bat = new Array();
							    	
							        var tree = studentBbTj.down("treepanel");
							    	var selNodes = tree.getChecked();
							    	Ext.each(selNodes, function(node){
							    		var nodeId = node.data.nodeId;
							    		//console.log(nodeId);
							    		if(nodeId.indexOf("TYP_") == 0){
							    			typ[i] = nodeId.substring(4,nodeId.length);
							    			i ++;
							    		}
							    		
							    		if(nodeId.indexOf("PRJ_") == 0){
							    			prj[j] = nodeId.substring(4,nodeId.length);
							    			j ++;
							    		}
							    		
							    		if(nodeId.indexOf("CLS_") == 0){
							    			cls[k] = nodeId.substring(4,nodeId.length);
							    			k ++;
							    		}
							    		
							    		if(nodeId.indexOf("BAT_") == 0){
							    			bat[h] = nodeId.substring(4,nodeId.length);
							    			h ++;
							    		}
							    	});
							    	
							    	
							    	//分布;
							    	var tzParameter = {
							    		"ComID": "TZ_STU_BBTJ_COM",	
							    		"PageID": "TZ_STU_BBTJ_STD",	
							    		"OperateType": "QF",
							    		"comParams": {
								    		"formDate": form.getFieldValues(),
								    		"typ": typ,
								    		"cls": cls,
								    		"prj": prj,
								    		"bat": bat
							    		}
							    	}
							    	var tzParameterJson = Ext.JSON.encode(tzParameter);
							    	
							    	//加载数据
									Ext.tzLoad(tzParameterJson,function(responseData){
										//console.log(responseData);
										studentBbTj.commitChanges(studentBbTj);
										var url = responseData.url;
										if(url == ""){
											Ext.MessageBox("提示","导出Excel失败");
										}else{
											window.open(TzUniversityContextPath + url);
										}
										
									});
								}
							 }
	                	 }]
	                	 
	                 }]
               },{
                   xtype: 'tabpanel',
                   reference: 'tjTabPanel',
                   name: 'tjTabPanel',
                   height: 400,
                   border: false,
                   frame: true,
                   margin: 10,
                   defaults: {
                       //bodyPadding: 10,
                       scrollable: false,
                       closable: false,
                       border: false
                   },
                   tabPosition: 'left',
                   tabRotation: 0,
                   items: [{
                      xtype:'panel',
                      title: '年龄分布',
                      name: 'tab1',
                      autoScroll: true,
                      viewModel: true,
                      bodyStyle: 'overflow-y:hidden;overflow-x:hidden',
                      items: [{
                	    xtype: 'grid',
                	    title: '年龄分布',
						height: 250, 
						//frame: true,
						columnLines: true,
						name: 'grid1',
						reference: 'grid1',
						//style:"margin:10px",
						store: studentBbTjAgeStore,
						columns: [{
						        text: '年龄段',
						        minWidth: 250,
						        dataIndex: 'name'
						    }, {
						        text: '数量',
						        minWidth: 125,
						        dataIndex: 'num'
						    }, {
						        text: '比例',
						        flex: 1,
						        dataIndex: 'scale'
						    }]
                      },{
                    	  xtype: 'component',
                    	  name: 'htmlContent',
                     	  html: ''
                      }]    
                   }, {
                       xtype:'panel',
                       title: '毕业院校',
                       name: 'tab2',
                       autoScroll: true,
                       viewModel: true,
                       bodyStyle: 'overflow-y:hidden;overflow-x:hidden',
                       items: [{
                 	    xtype: 'grid',
                 	    title: '毕业院校',
 						height: 350, 
 						//frame: true,
 						columnLines: true,
 						name: 'grid2',
 						reference: 'grid2',
 						//style:"margin:10px",
 						store: studentBbTjSchoolStore,
 						columns: [{
	 					        text: '学校分类',
	 					        minWidth: 250,
	 					        dataIndex: 'name'
	 					    }, {
	 					        text: '数量',
	 					        minWidth: 125,
	 					        dataIndex: 'num'
	 					    }, {
	 					        text: '比例',
	 					        flex: 1,
	 					        dataIndex: 'scale'
	 					    }]
                       },{
                    	  xtype: 'component',
                     	  name: 'htmlContent',
                      	  html: ''
                       }]    
                    }, {
                        xtype:'panel',
                        title: '工龄范围',
                        name: 'tab3',
                        autoScroll: true,
                        viewModel: true,
                        bodyStyle: 'overflow-y:hidden;overflow-x:hidden',
                        items: [{
                  	    xtype: 'grid',
                  	    title: '工龄范围',
  						height: 250, 
  						//frame: true,
  						columnLines: true,
  						name: 'grid3',
  						reference: 'grid3',
  						//style:"margin:10px",
  						store: studentBbTjWorkAgeStore,
  						columns: [{
	  					        text: '工龄范围',
	  					        minWidth: 250,
	  					        dataIndex: 'name'
	  					    }, {
	  					        text: '数量',
	  					        minWidth: 125,
	  					        dataIndex: 'num'
	  					    }, {
	  					        text: '比例',
	  					        flex: 1,
	  					        dataIndex: 'scale'
	  					    }]
                        },{
                        	xtype: 'component',
                       	  	name: 'htmlContent',
                        	html: ''
                        }]    
                    },{
                        xtype:'panel',
                        title: '年薪范围',
                        name: 'tab4',
                        autoScroll: true,
                        viewModel: true,
                        bodyStyle: 'overflow-y:hidden;overflow-x:hidden',
                        items: [{
                  	    xtype: 'grid',
                  	    title: '年薪范围',
  						height: 250, 
  						//frame: true,
  						columnLines: true,
  						name: 'grid4',
  						reference: 'grid4',
  						//style:"margin:10px",
  						store: studentBbTjWorkSaralyStore,
  						columns: [{
	  					        text: '年薪范围',
	  					        minWidth: 250,
	  					        dataIndex: 'name'
	  					    }, {
	  					        text: '数量',
	  					        minWidth: 125,
	  					        dataIndex: 'num'
	  					    }, {
	  					        text: '比例',
	  					        flex: 1,
	  					        dataIndex: 'scale'
	  					    }]
                        },{
                        	xtype: 'component',
                       	    name: 'htmlContent',
                        	html: ''
                        }]    
                    },{
                        xtype:'panel',
                        title: '专业类别',
                        name: 'tab5',
                        autoScroll: true,
                        viewModel: true,
                        bodyStyle: 'overflow-y:hidden;overflow-x:hidden',
                        items: [{
                  	    xtype: 'grid',
                  	    title: '专业类别',
  						height: 300, 
  						//frame: true,
  						columnLines: true,
  						name: 'grid5',
  						reference: 'grid5',
  						//style:"margin:10px",
  						store: studentBbTjMajorTypeStore,
  						columns: [{
	  					        text: '专业类别',
	  					        minWidth: 250,
	  					        dataIndex: 'name'
	  					    }, {
	  					        text: '数量',
	  					        minWidth: 125,
	  					        dataIndex: 'num'
	  					    }, {
	  					        text: '比例',
	  					        flex: 1,
	  					        dataIndex: 'scale'
	  					    }]
                        },{
                          xtype: 'component',
                       	  name: 'htmlContent',
                          html: ''
                        }]    
                    },{
                        xtype:'panel',
                        title: '教育背景',
                        name: 'tab6',
                        autoScroll: true,
                        viewModel: true,
                        bodyStyle: 'overflow-y:hidden;overflow-x:hidden',
                        items: [{
                  	    xtype: 'grid',
                  	    title: '教育背景',
  						height: 200, 
  						//frame: true,
  						columnLines: true,
  						name: 'grid6',
  						reference: 'grid6',
  						//style:"margin:10px",
  						store: studentBbTjEducationStore,
  						columns: [{
	  					        text: '教育背景',
	  					        minWidth: 250,
	  					        dataIndex: 'name'
	  					    }, {
	  					        text: '数量',
	  					        minWidth: 125,
	  					        dataIndex: 'num'
	  					    }, {
	  					        text: '比例',
	  					        flex: 1,
	  					        dataIndex: 'scale'
	  					    }]
                        },{
                          xtype: 'component',
                       	  name: 'htmlContent',
                          html: ''
                        }]    
                    },{
                        xtype:'panel',
                        title: '入学前单位性质',
                        name: 'tab7',
                        autoScroll: true,
                        viewModel: true,
                        bodyStyle: 'overflow-y:hidden;overflow-x:hidden',
                        items: [{
                  	    xtype: 'grid',
                  	    title: '入学前单位性质',
  						height: 300, 
  						//frame: true,
  						columnLines: true,
  						name: 'grid7',
  						reference: 'grid7',
  						//style:"margin:10px",
  						store: studentBbTjInsStore,
  						columns: [{
	  					        text: '入学前单位性质',
	  					        minWidth: 250,
	  					        dataIndex: 'name'
	  					    }, {
	  					        text: '数量',
	  					        minWidth: 125,
	  					        dataIndex: 'num'
	  					    }, {
	  					        text: '比例',
	  					        flex: 1,
	  					        dataIndex: 'scale'
	  					    }]
                        },{
                           xtype: 'component',
                       	   name: 'htmlContent',
                           html: ''
                        }]    
                    },{
                        xtype:'panel',
                        name: 'tab8',
                        title: '入学前行业背景',
                        autoScroll: true,
                        viewModel: true,
                        bodyStyle: 'overflow-y:hidden;overflow-x:hidden',
                        items: [{
                  	    xtype: 'grid',
                  	    title: '入学前行业背景',
  						height: 380, 
  						//frame: true,
  						columnLines: true,
  						name: 'grid8',
  						reference: 'grid8',
  						//style:"margin:10px",
  						store: studentBbTjIndStore,
  						columns: [{
	  					        text: '入学前行业背景',
	  					        minWidth: 250,
	  					        dataIndex: 'name'
	  					    }, {
	  					        text: '数量',
	  					        minWidth: 125,
	  					        dataIndex: 'num'
	  					    }, {
	  					        text: '比例',
	  					        flex: 1,
	  					        dataIndex: 'scale'
	  					    }]
                        },{
                        	xtype: 'component',
                        	name: 'htmlContent',
                            html: ''
                        }]    
                    },{
                        xtype:'panel',
                        title: '管理经验',
                        name: 'tab9',
                        autoScroll: true,
                        viewModel: true,
                        bodyStyle: 'overflow-y:hidden;overflow-x:hidden',
                        items: [{
                  	    xtype: 'grid',
                  	    title: '管理经验',
  						height: 200, 
  						//frame: true,
  						columnLines: true,
  						name: 'grid9',
  						reference: 'grid9',
  						//style:"margin:10px",
  						store: studentBbTjExperienceStore,
  						columns: [{
	  					        text: '管理经验',
	  					        minWidth: 250,
	  					        dataIndex: 'name'
	  					    }, {
	  					        text: '平均值',
	  					        flex: 1,
	  					        dataIndex: 'num'
	  					    }]
                        },{
                        	xtype: 'component',
                        	name: 'htmlContent',
                            html: ''
                        }]    
                    },{
                        xtype:'panel',
                        title: '性别',
                        name: 'tab10',
                        autoScroll: true,
                        viewModel: true,
                        bodyStyle: 'overflow-y:hidden;overflow-x:hidden',
                        items: [{
                  	    xtype: 'grid',
                  	    title: '性别',
  						height: 200, 
  						//frame: true,
  						columnLines: true,
  						name: 'grid10',
  						reference: 'grid10',
  						//style:"margin:10px",
  						store: studentBbTjSexStore,
  						columns: [{
	  					        text: '性别',
	  					        minWidth: 250,
	  					        dataIndex: 'name'
	  					    }, {
	  					        text: '数量',
	  					        minWidth: 125,
	  					        dataIndex: 'num'
	  					    }, {
	  					        text: '比例',
	  					        flex: 1,
	  					        dataIndex: 'scale'
	  					    }]
                        },{
                        	xtype: 'component',
                        	name: 'htmlContent',
                            html: ''
                        }]    
                    }]
               }]
        }]
        this.callParent();
    },
    buttons: [{
        text: '关闭',
        iconCls:"close",
        handler: 'onCloseButton'
    }]
});
