﻿/*生成返回菜单的函数*/
function createReturnMenu(parentObject, jsonObject)
{
	var tb = Ext.create('Ext.toolbar.Toolbar');
	
  tb.render(parentObject);
  
  tb.add(
  				{
  					text: '返回批次列表页面',
  					tooltip:'单击此按钮返回评审批次列表页面。',
  					glyph:'xf04a@FontAwesome',
  					width: 130,
  					handler: function(item,pressed){showPreviousEvaluatePage(1);},
  					pressed: true
  				},
				'<b>'+jsonObject['ps_class_mc'] + " " + jsonObject['ps_baok_pc'] +'</b>'
  			);
}

function loadEvaluateBatchDataById(batchId,jsonObject)
{
	//加载选中批次的考生列表数据
	if(mainPageObjectArray['PreviousBatchId'] != batchId)
	{
		if(mainPageObjectArray['PreviousBatchId'] == '' || mainPageObjectArray['PreviousBatchId'] == null || mainPageObjectArray['PreviousBatchId'] == 'undefined')
		{
			mainPageObjectArray['PreviousBatchId'] = batchId;
		}
		else
		{
			var pBatchId = mainPageObjectArray['PreviousBatchId'];
			
			mainPageObjectArray['PreviousBatchId'] = batchId;
			mainPageObjectArray['DivObjectList'][pBatchId].hide();
		}
		
		var tmpDivObject = null;
		if(mainPageObjectArray['DivObjectList'][batchId] == null)
		{
			var tmpMEP = initializeMainEvaluatePage(batchId,jsonObject);

			if(window.batchEvaluateMainPageObject[batchId] == null)
			{
				window.batchEvaluateMainPageObject[batchId] = tmpMEP;
			}
			
			tmpDivObject = $('<div/>');
			
			
			window.myPageSlider[0].divPage.show();
			
			
			tmpDivObject.appendTo($('#tz_plps_main_page'));
			mainPageObjectArray['DivObjectList'][batchId] = tmpDivObject;
			
			
			//生成返回菜单并挂载到批次评审主页面
			createReturnMenu(tmpDivObject[0], jsonObject);
			//挂载生成的批次评审主页面
			tmpMEP.render(tmpDivObject[0]);
			
		}
		else
		{
			tmpDivObject = mainPageObjectArray['DivObjectList'][batchId];
			tmpDivObject.show();
		}


		//第一次进入评审主页面时，评审说明弹出显示
		if(jsonObject!=null) {
			var reviewDescHtml = jsonObject.ps_description;
			openRemindWindow("<span style='color:red;font-weight:bold;font-size:16px;'>评审说明</span>",reviewDescHtml,"900");
		}
	}
	
}

function loadEvaluateBatchData(callBackFunction)
{
	var loadSuccess = false;
	
	/*服务器联调代码*/
	Ext.Ajax.request({
		url:window.getBatchListUrl,
		method:'POST',
		timeout:60000,
		params:{LanguageCd:'ZHS',MaxRowCount:1000,StartRowNumber:1,MoreRowsFlag:'N'},
		success:function(response)
		{
			var jsonObject = null;
			
			var jsonText = response.responseText;
            
			try
			{
				var jsonObject = Ext.util.JSON.decode(jsonText);
                /*判断服务器是否返回了正确的信息*/
                if(jsonObject.state.errcode == 1){
                	Ext.Msg.alert("提示",jsonObject.state.timeout==true?"您当前登录已超时或者已经退出，请重新登录！":jsonObject.state.errdesc);
                }else{
                	jsonObject = jsonObject.comContent;

    				if(jsonObject.error_code != '0')
    				{
    					loadSuccess = false;
    					Ext.Msg.alert("提示",'当前材料评审批次数据加载失败：' + jsonObject.error_decription);
    				}
    				else
    				{
    					loadSuccess = true;
    					callBackFunction(jsonObject);
    				}
                }
			}
			catch(e1)
			{
				loadSuccess = false;
				if(window.evaluateSystemDebugFlag == 'Y')
				{
					Ext.Msg.alert("提示",'当前材料评审批次数据加载失败，请与系统管理员联系：错误的JSON数据[' + e1.description + ']' + response.responseText);
				}
				else
				{
					Ext.Msg.alert("提示",'当前材料评审批次数据加载失败，请与系统管理员联系：错误的JSON数据[' + e1.description + ']。');
				}
			}
		},
		failure:function(response)
		{
			loadSuccess = false;
			if(window.evaluateSystemDebugFlag == 'Y')
			{
				Ext.Msg.alert("提示",'当前材料评审批次数据加载失败，请与系统管理员联系：' + response.responseText);
			}
			else
			{
				Ext.Msg.alert("提示",'当前材料评审批次数据加载失败，请与系统管理员联系。');
			}
		}
	}
);

	
	return loadSuccess;
}

function getEvaluateBatchListData(JEBDObject)
{
	var ebList = [];
	
	for(var i=0;i<JEBDObject['data'].length;i++)
	{
		var tmpArray = [];
		
		tmpArray[0] = JEBDObject['data'][i]['class_id'];
		tmpArray[1] = JEBDObject['data'][i]['class_name'];
		tmpArray[2] = JEBDObject['data'][i]['pc_id'];
		tmpArray[3] = JEBDObject['data'][i]['pc_name'];
		tmpArray[4] = JEBDObject['data'][i]['pc_zt'];
		tmpArray[5] = '进行评审';
		tmpArray[6] = '打印评审总表';
		tmpArray[7] = JEBDObject['data'][i]['pc_ztid'];
		tmpArray[8] = JEBDObject['data'][i]['pc_end_num'];
		
		ebList[i] = tmpArray;
	}
	
	return ebList;
}

function initializeGridColumnHeaders()
{
	var tmpObject0 = new Object;
	tmpObject0['name'] = 'ClassID';
	gridColumnHeaders_01[0] = tmpObject0;
	
	var tmpObject1 = new Object;
	tmpObject1['name'] = 'ClassName';
	gridColumnHeaders_01[1] = tmpObject1;
	
	var tmpObject2 = new Object;
	tmpObject2['name'] = 'BatchID';
	gridColumnHeaders_01[2] = tmpObject2;
	
	var tmpObject3 = new Object;
	tmpObject3['name'] = 'BatchName';
	gridColumnHeaders_01[3] = tmpObject3;
	
	var tmpObject4 = new Object;
	tmpObject4['name'] = 'EvaluateStatus';
	gridColumnHeaders_01[4] = tmpObject4;
	
	var tmpObject5 = new Object;
	tmpObject5['name'] = 'Evaluate';
	gridColumnHeaders_01[5] = tmpObject5;
	
	var tmpObject6 = new Object;
	tmpObject6['name'] = 'PrintResult';
	gridColumnHeaders_01[6] = tmpObject6;

	var tmpObject7 = new Object;
	tmpObject7['name'] = 'EvaluateStatusId';
	gridColumnHeaders_01[7] = tmpObject7;

	var tmpObject8 = new Object;
	tmpObject8['name'] = 'BatchEndTimeNum';
	gridColumnHeaders_01[8] = tmpObject8;
	
	Ext.define('EvaluateBatchList', {
    	extend: 'Ext.data.Model',
    	fields: gridColumnHeaders_01,
    	idProperty: 'EvaluateBatchList'
	});
}

function loadBatchDataById(batchId,callBackFunction)
{
	var arr = batchId.split("_");
	var classid = arr[0];
	var pcid = arr[1];
	//var pwzId = arr[2];
	
	/*服务器联调代码*/
	if(mainPageObjectArray['PreviousBatchId'] != batchId && mainPageObjectArray['DivObjectList'][batchId] == null)
	{
		Ext.Ajax.request({
				url:window.getBatchDataUrl,
				method:'POST',
				timeout:60000,
				params: {
									LanguageCd:'ZHS',
									BaokaoClassID:classid,
									BaokaoPCID:pcid,
									RequestDataType:'A',
									MaxRowCount:1000,
									StartRowNumber:1,
									MoreRowsFlag:'N'
								},
				success:function(response)
				{
					//unmask window
					unmaskWindow();
					
					//返回值内容
                    var jsonText = response.responseText;
                    
					var jsonObject = null;
					
					try
					{
						var jsonObject = Ext.util.JSON.decode(jsonText);
		                /*判断服务器是否返回了正确的信息*/
		                if(jsonObject.state.errcode == 1){
		                	Ext.Msg.alert("提示",jsonObject.state.timeout==true?"您当前登录已超时或者已经退出，请重新登录！":jsonObject.state.errdesc);
		                }else{
		                	jsonObject = jsonObject.comContent;

							if(jsonObject.error_code != "0")
							{
								loadSuccess = false;
								Ext.Msg.alert("提示",'当前材料评审[' + getBatchNameById(batchId) + ']数据加载失败：' + jsonObject.error_decription);
							}
							else
							{
								loadSuccess = true;
								/*将当前请求到的JSON数据缓存到本地*/
								window.batchJSONArray[batchId] = jsonObject;
								callBackFunction(batchId,jsonObject);
							}
		                }
						
					}
					catch(e1)
					{
						if(console)console.error(e1);
						loadSuccess = false;
						if(window.evaluateSystemDebugFlag == 'Y')
						{
							Ext.Msg.alert("提示",'当前材料评审[' + batchId + ']数据加载失败，请与系统管理员联系：错误的JSON数据[' + e1.toString() + ']' + response.responseText);
						}
						else
						{
							Ext.Msg.alert("提示",'当前材料评审[' + getBatchNameById(batchId) + ']数据加载失败，请与系统管理员联系：错误的JSON数据[' + e1.description + ']。');
						}
					}
				},
				failure:function(response)
				{
					//unmask window
					unmaskWindow();
					
					loadSuccess = false;
					if(window.evaluateSystemDebugFlag == 'Y')
					{
						Ext.Msg.alert("提示",'当前材料评审批次[' + getBatchNameById(batchId) + ']数据加载失败，请与系统管理员联系：' + response.responseText);
					}
					else
					{
						Ext.Msg.alert("提示",'当前材料评审批次[' + getBatchNameById(batchId) + ']数据加载失败，请与系统管理员联系。');
					}
				}
			}
		);
	}
	else
	{
		callBackFunction(batchId,null);
	}
}

function loadBatchDataByIdandShowNextPage(batchId,jsonObject)
{
	loadEvaluateBatchDataById(batchId,jsonObject);
	
	/*显示批准评审主页面*/
	window.setTimeout(function(){showNextEvaluatePage(1);},10);
	
	//将已评审考生得分统计的Column Header Text 居中
	for(var PJFTJGrid_i=0;PJFTJGrid_i<PJFTJGridColumnHeaderTextAry.length;PJFTJGrid_i++){
		$("#"+ PJFTJGrid_batchID +" span:contains('"+ PJFTJGridColumnHeaderTextAry[PJFTJGrid_i] +"')").parent().css("text-align","center");
	}
	
	
	//unmask window
	unmaskWindow();
}

/*打印评分总表的函数*/
function printStatisticsTotalTable(classId,batchId,className,batchName)
{
	
    var url = window.printStatisticsTableUrl;
    maskWindow("正在生成评审总表，请稍候...");
    Ext.Ajax.request(
    		{
    			url:url,
    			method:'POST',
    			timeout:30000,
    			params: {
    				TZ_CLASS_ID:classId,
    				TZ_PC_ID:batchId
				},
    			success:function(response)
    			{
    				//unmask window
					unmaskWindow();
					
					//返回值内容
                    var jsonText = response.responseText;
                    
    				var jsonObject = null;
    				
    				try
    				{
    					var jsonObject = Ext.util.JSON.decode(jsonText);
		                /*判断服务器是否返回了正确的信息*/
		                if(jsonObject.state.errcode == 1){
		                	Ext.Msg.alert("提示",jsonObject.state.timeout==true?"您当前登录已超时或者已经退出，请重新登录！":jsonObject.state.errdesc);
		                }else{
		                	jsonObject = jsonObject.comContent;
	    					
	    					if(jsonObject.errorCode&&jsonObject.errorCode == '1')
	    					{
	    						loadSuccess = false;
	    						Ext.Msg.alert("提示","生成评审总表失败："+jsonObject.errorMsg);
	    					}
	    					else
	    					{
	    						url = jsonObject.url;
	    						if(url){
	    							window.open(url);
	    						}else{
	    							Ext.Msg.alert("提示","生成评审总表失败");
	    						}
	    					}
		                }
    					
    				}
    				catch(e1)
    				{
    					Ext.Msg.alert("提示","生成评审总表失败："+e1.toString());
    				}
    			},
    			failure:function(response)
    			{
    				//unmask window
					unmaskWindow();
					
    				loadSuccess = false;
    				if(window.evaluateSystemDebugFlag == 'Y')
    				{
    					Ext.Msg.alert("提示","生成评审总表失败："+response.responseText+"，请与系统管理员联系。");
    				}
    				else
    				{
    					Ext.Msg.alert("提示","生成评审总表失败：请与系统管理员联系。");
    				}
    			}
    		}
    	);
}

function initializeEvaluatePiciGrid(jsonObject)
{
	// sample static data for the store
  var myData = getEvaluateBatchListData(jsonObject);
	
  // create the data store
  var store = Ext.create('Ext.data.ArrayStore', {
      model: 'EvaluateBatchList',
      data: myData
  });
	
  // create the Grid
  var grid = Ext.create('Ext.grid.Panel', {
      store: store,
      stateful: true,
      collapsible: false,
      multiSelect: false,
      columnLines: true,
      width:'100%',
      minHeight:100,
      stateId: 'EvaluateBatchListGrid',
      style:'cursor:default;vertical:middle;',
      columns: [
          {
              text     : '报考方向名称',
              flex     : 1,
              sortable : true,
              resizable: false,
              dataIndex: 'ClassName'
          },
          {
              text     : '批次名称',
              width    : 140,
              sortable : true,
              resizable: false,
              dataIndex: 'BatchName'
          },
          {
              text     : '评审状态',
              width    : 80,
              sortable : true,
              resizable: false,
              dataIndex: 'EvaluateStatus',
              align    : 'center'
          },
          {
              text     : '进行评审',
              width    : 90,
              sortable : false,
              resizable: false,
              dataIndex: 'Evaluate',
              align    : 'center',
              renderer : function(value,cellmeta, record, rowIndex, columnIndex, store){
				  var psztId = record.data["EvaluateStatusId"];
				  var endTimeNum = record.data["BatchEndTimeNum"];
				  var currentDttm = new Date();
				  var currentDttmNum = currentDttm.getTime();

				  var tmpBtn;

				  if(psztId=="B" || currentDttm>endTimeNum) {
					  tmpBtn = Ext.create('Ext.Button',{text:value,height:20,margin:'0 0 0 0',style:'background-color:#999 !important;border-color:#999 !important;',padding:'0 0 0 0',valign:'left',tooltip:'单击此按钮进入该批次评审主页面'});
				  }  else {
					  tmpBtn = Ext.create('Ext.Button',{text:value,height:20,margin:'0 0 0 0',padding:'0 0 0 0',valign:'left',tooltip:'单击此按钮进入该批次评审主页面'});
				  }
		 		var divTmp = $('<div/>');
		 		
		 		divTmp[0].id = Ext.id();
		 		tmpBtn.render(divTmp[0]);
		 		
		 		return divTmp[0].outerHTML;
			 }
          },
          {
              text     : '打印评审总表',
              width    : 100,
              sortable : false,
              resizable: false,
              dataIndex: 'PrintResult',
              align    : 'center',
              renderer : function(value){
			 		var tmpBtn = Ext.create('Ext.Button',{text:value,height:20,margin:'0 0 0 0',padding:'0 0 0 0',valign:'left',tooltip:'单击此按钮打印当前批次评审统计总表'});
			 		var divTmp = $('<div align="align:center"/>');
			 		
			 		divTmp[0].id = Ext.id();
			 		tmpBtn.render(divTmp[0]);
			 		
			 		return divTmp[0].outerHTML;
			 }
          }
      ],
      title: '当前评审批次列表',
      renderTo: 'tz_zlps_pclb_a',
      viewConfig: {
          stripeRows: true,
          enableTextSelection: true
      }
  });
  
  //列表渲染完毕之后加载评委提醒信息
  if(jsonObject.remindData!=undefined&&jsonObject.remindData.length>0){
		 var reminHtml = "尊敬的评委：<br/><br/><span style='margin-left:20px;'>";
		 reminHtml+=jsonObject.remindData.join("</span><br/><br/><span style='margin-left:20px;'>");
		 reminHtml+="</span>";
		 openRemindWindow("",reminHtml,"650");
  }
  
  grid.on('cellClick', function(gridViewObject,cellHtml,colIndex,dataModel,rowHtml,rowIndex){

		var rec = store.getAt(rowIndex);
		
		var classId = rec.get('ClassID');
		var batchId = rec.get('BatchID');
		var className = rec.get('ClassName');
		var batchName = rec.get('BatchName');
		var pwzId ;
		if(jsonObject['data'].length > 0){
			pwzId = jsonObject['data'][0]['pc_pwzid']
		}
	    var psztId = rec.get("EvaluateStatusId");
	    var endTimeNum = rec.get("BatchEndTimeNum");
		
		batchId = jQuery.trim(batchId);
		if(colIndex == 3)//评审按钮
		{
			//装载指定批次数据
			if(batchId == null || batchId == '' || batchId == 'undefined')
			{
				Ext.Msg.alert("提示",'系统错误：无法获取指定评审批次的编号。');
			}
			else
			{
				//判断批次是否已关闭或者超过结束时间
				if(psztId=="B") {
					Ext.Msg.alert("提示",'当前评审批次已结束。');
				}  else  {
					var currentDttm = new Date();
					var currentDttmNum = currentDttm.getTime();

					if(currentDttm>endTimeNum) {
						Ext.Msg.alert("提示",'当前评审批次已到结束时间。');
					} else {
						//mask window
						maskWindow();
						var cls_batchId = classId + "_" + batchId;

						loadBatchDataById(cls_batchId,loadBatchDataByIdandShowNextPage);
					}
				}
			}
		}
		else if(colIndex == 4)//打印评审总表按钮
		{
			if(classId == null || classId == '' || classId == 'undefined' || batchId == null || batchId == '' || batchId == 'undefined')
			{
				Ext.Msg.alert("提示",'系统错误：无法获取指定评审批次的编号。');
			}
			else
			{
				printStatisticsTotalTable(classId,batchId,className,batchName);
			}
		}
	}
  			 );
}

/*根据指定批次编号获取对应班级批次名称的方法*/
function getBatchNameById(batchId)
{
	var batchName = '';

	if(Object.prototype.toString.call(window.batchJSONArray[batchId]) == '[object Object]')
	{
		batchName = window.batchJSONArray[batchId]['ps_class_mc']+" "+window.batchJSONArray[batchId]['ps_baok_pc'];
	}

	return batchName;
}

/*登录之后批次列表也买呢显示评委未完成的任务提醒*/
function openRemindWindow(title,html,width){
	var win = new Ext.window.Window({
        modal:true,
        defaults: {
            border:false
        },
        style:"overflow-y:auto",
        layout:'fit',
        buttonAlign:'center',
        iframeLoad:function(iframe){
            win.body.unmask();
        },
		title:title,
        items:[{
            xtype:'component',
            padding:10,
            style:"font-size:13px;font-family:MicroSoft Yahei;overflow-y:auto;",
            html:html,
            minHeight : 160,
			maxHeight : 500,
			minWidth  : 500,
            width : width
        }],
        buttons:[
            {
                text:"我知道了",
                style:'background-color:#a9abd1;width:100px;height:30px;border-radius:6px;-webkit-border-radius:6px;line-height:30px;font-size:14px',
                handler:function(btn){
                    btn.findParentByType('window').close();
                }
            }
        ]
    });
	win.show();
}