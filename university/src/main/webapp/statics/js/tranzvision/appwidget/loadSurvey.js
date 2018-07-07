function pageReadOnly()
{
	$("form input").prop("readonly", "readonly");
	$("form select").prop("readonly", "readonly");
	$("form textarea").prop("readonly", "readonly");
	$("form input").prop("disabled", true);
	$("form select").prop("disabled", true); 
//	$("form textarea").prop("disabled", true);
	$("form textarea").unbind();
	$("form input").nextAll('img').unbind("click");
	if($("[id$='Btnselect']").length>0){
		$("[id$='Btnselect']").hide();
	}
	$(".addNext").hide();
	$(".btn-addcon").hide();
	$(".main_inner_content_info").hide();
	$(".main_inner_content_del").hide();
	$(".main_inner_file_del").hide();
	$(".main_inner_content_del_bmb").hide();
	$(".age-action-button").hide();
	$(".fileupload_input").hide();
	$(".file_upload_button").hide();
	$(".main_inner_content_info_right_option_168px").hide();
	$('.main_inner_content_info_right_photo a').unbind("click"); 
	$('#app_save').unbind("click");
	$('#app_submit').unbind("click"); 
	$(".chosen-container").unbind();
	$('#app_save').hide();
	$('#app_submit').hide();
}

/*加载formvalidator信息*/
try{
	$.formValidator.initConfig({	
		formID:"main_list",		
		debug:false,	
		submitOnce:true,
		onError:function(msg,obj,errorlist){
			$("#errorlist").empty();
			$.map(errorlist,function(msg){	
				$("#errorlist").append("<li>" + msg + "</li>")	
			});
		},
		submitAfterAjaxPrompt : '有数据正在异步验证，请稍等...'	
	});
}catch(e){
}

//处理formvalidator的tips
var $flowtips = $("#flowtips");

/*处理报名表数据*/
if(dataApp){	

	/*处理推荐信重新拖之后实例发生变化的问题*/
	var refInstanceId = "";
	$.each(dataApp,function(instanceIdApp,objApp){
		if(objApp.classname=="recommendletter")
		{
			refInstanceId = instanceIdApp;
			return false;
		}
	})
	$.each(data.items,function(instanceId,obj){
		var recApp;
		//console.log("classname:"+obj.classname);	
		if(obj.classname=="recommendletter" && refInstanceId!="")
		{
			recApp = dataApp[refInstanceId];
		}else
		{
			recApp = dataApp[instanceId];
		}

		
		if(recApp){
			//console.log(recApp.isDoubleLine);
			if(recApp.isDoubleLine  == "Y"){
				//模板中的children为对象，报名表中的多行为数组，对象转换成数组并value赋值
				obj["children"] = [	obj["children"]] ;
				//console.log("length:"+recApp["children"] .length);	
				for(var j = 1; j< recApp["children"] .length;j++){
					obj["children"].push(cloneObj(obj["children"][0]));
				}
				/*是通用容器还是固定多行容器*/
				$.each(obj["children"],function(i,perChildren){
					//console.log("perChildren:"+perChildren);
					//console.dir(perChildren);
					$.each(perChildren,function(j,perXxx){
						//console.log("perXxx:"+perXxx);
						//console.dir(perXxx);
						var itemId0 = perXxx["itemId"];		
						
						if(i> 0)
						{	//设置itemId;
								if(perXxx.hasOwnProperty("isSingleLine"))
								{
									if(perXxx.isSingleLine  == "Y"){
									}else
									{
										perXxx["itemId"] =  itemId0 + "_" + i;
									}
								}else
								{
									perXxx["itemId"] =  itemId0 + "_" + i;
								}
								
						}
						
						if(recApp["children"][i][j]){							
							if(perXxx.isSingleLine  == "Y"){
								$.each(perXxx["children"],function(z,signlePerXxx){	
									var itemId1 = signlePerXxx["itemId"];	
									if(i>0)
									{
										signlePerXxx["itemId"] = itemId1 + "_" + i;
									}
									if(perXxx.classname=="FirmType"||perXxx.classname=="StartBusinessExp"){
										try{
											signlePerXxx["value"] =  SurveyBuild.htmlCharReplace(recApp["children"][i][j]["children"][0][z]["value"]);
										}catch(error){
											signlePerXxx["value"] =  SurveyBuild.htmlCharReplace(recApp["children"][i][j]["children"][z]["value"]);
										}
									}else{
										signlePerXxx["value"] =  SurveyBuild.htmlCharReplace(recApp["children"][i][j]["children"][z]["value"]);
									}if(signlePerXxx.hasOwnProperty("wzsm"))
									{
										signlePerXxx["wzsm"] =  recApp["children"][i][j]["children"][z]["wzsm"];
									}
								})
							}
							
							else if(perXxx.classname  == "imagesUpload" || perXxx.classname  == "AttachmentUpload"){
								/*多行容器的控件*/
								if(recApp["children"][i][j].hasOwnProperty("children"))
								{
									for(var k = 1; k< recApp["children"][i][j]["children"].length;k++){
										perXxx["children"].push(cloneObj(perXxx["children"][0]));
									}
									if( perXxx["children"] && $.isArray( perXxx["children"])){
										/*如果有多个附件，是需要克隆的*/
										$.each(perXxx["children"],function(x,filePerXxx){										
											filePerXxx["fileName"] = recApp["children"][i][j]["children"][x]["fileName"];
											filePerXxx["sysFileName"] = recApp["children"][i][j]["children"][x]["sysFileName"];
											filePerXxx["orderby"] = recApp["children"][i][j]["children"][x]["orderby"];
											filePerXxx["accessPath"] = recApp["children"][i][j]["children"][x]["accessPath"];
											filePerXxx["viewFileName"] = recApp["children"][i][j]["children"][x]["viewFileName"];
										})
									}
								}else{
									if(perXxx.hasOwnProperty("filename"))
									{
										perXxx["filename"] =  recApp["children"][i][j]["filename"];
									}
									if(perXxx.hasOwnProperty("sysFileName"))
									{
										perXxx["sysFileName"] =  recApp["children"][i][j]["sysFileName"];
									}
									if(perXxx.hasOwnProperty("path"))
									{
										perXxx["path"] =  recApp["children"][i][j]["path"];
									}
									if(perXxx.hasOwnProperty("accessPath"))
									{
										perXxx["accessPath"] =  recApp["children"][i][j]["accessPath"];
									}
								}
							}else if(perXxx.classname  == "Radio" || perXxx.classname  == "Check"){
								/*单选框和复选框处理方式*/
								if( perXxx["option"] ){
									$.each(perXxx["option"],function(ii,optionInfo){
										if(recApp["children"][i][j]["option"][ii]){
											optionInfo["checked"] = recApp["children"][i][j]["option"][ii]["checked"];											
											if(recApp["children"][i][j]["option"][ii]["othervalue"]!=undefined){
												optionInfo["othervalue"] = recApp["children"][i][j]["option"][ii]["othervalue"];
											}
											else{
												optionInfo["othervalue"] = "";
											}
										}
										
									})
								} 
								if(perXxx.hasOwnProperty("value")&&recApp["children"][i][j].hasOwnProperty("value"))
								{
									perXxx["value"] = recApp["children"][i][j]["value"];
								}
								
								if(recApp["children"][i][j]["othervalue"]){
									perXxx["othervalue"] = SurveyBuild.htmlCharReplace(recApp["children"][i][j]["othervalue"]);
								}
							}else if(perXxx.classname  == "refLetterFile"){
								/*推荐信附件*/
								////console.log(recApp["children"][i][j]["filename"]);	
								perXxx["filename"] = recApp["children"][i][j]["filename"];
								perXxx["sysFileName"] = recApp["children"][i][j]["sysFileName"];
								//perXxx["path"] = recApp["children"][i][j]["path"];	By WRL
								perXxx["accessPath"] = recApp["children"][i][j]["accessPath"];
								perXxx["viewFileName"] = recApp["children"][i][j]["viewFileName"];
							}else if(perXxx.classname  == "ChooseClass"){
								//console.log("111");
									perXxx["children"]["bmrClass"]["value"]  =SurveyBuild.htmlCharReplace(recApp["children"][i][j]["children"]["bmrClass"]["value"]);
									perXxx["children"]["bmrBatch"]["value"]  =SurveyBuild.htmlCharReplace(recApp["children"][i][j]["children"]["bmrBatch"]["value"]);
									////console.log("LoadbmrClassvalue:"+perXxx["children"]["bmrClass"]["value"]);
									////console.log("LoadbmrBatchvalue:"+perXxx["children"]["bmrBatch"]["value"]);
								
									
									perXxx["children"]["bmrClass"]["wzsm"]  =SurveyBuild.htmlCharReplace(recApp["children"][i][j]["children"]["bmrClass"]["wzsm"]);
									perXxx["children"]["bmrBatch"]["wzsm"]  =SurveyBuild.htmlCharReplace(recApp["children"][i][j]["children"]["bmrBatch"]["wzsm"]);
									////console.log("LoadbmrClasswzsm:"+perXxx["children"]["bmrClass"]["wzsm"]);
									////console.log("LoadbmrBatchwzsm:"+perXxx["children"]["bmrBatch"]["wzsm"]);
											
							}else if(perXxx.classname  == "FirmType"){
		
								//console.dir(recApp["children"][i][j]["children"]);
								perXxx["children"]["WorkExper1"]["value"]  =SurveyBuild.htmlCharReplace(recApp["children"][i][j]["children"]["WorkExper1"]["value"]);
								perXxx["children"]["WorkExper2"]["value"]  =SurveyBuild.htmlCharReplace(recApp["children"][i][j]["children"]["WorkExper2"]["value"]);										
							}else if(perXxx.classname=="StartBusinessExp"){
								//console.log("StartBusinessExp");
								//WorkExper1 2 2_1 2_2 2_3 3 4 5 6 7 8 9
								var tempGp=["1", "2", "2_1", "2_2" ,"2_3" ,"3" ,"4" ,"5" ,"6" ,"7" ,"8" ,"9"];
								
								for(var index=0;index<tempGp.length;index++){
									tempStr="WorkExper"+tempGp[index];
									perXxx["children"][tempStr]["value"]  =SurveyBuild.htmlCharReplace(recApp["children"][i][j]["children"][tempStr]["value"]);
									perXxx["children"][tempStr]["value"]  =SurveyBuild.htmlCharReplace(recApp["children"][i][j]["children"][tempStr]["value"]);										
								}
							}  
							//------------
							else{	
								if( recApp["children"][i][j]["value"]){
									perXxx["value"] = SurveyBuild.htmlCharReplace(recApp["children"][i][j]["value"]);
								}
								
								if(recApp["children"][i][j]){
									perXxx["othervalue"] = SurveyBuild.htmlCharReplace(recApp["children"][i][j]["othervalue"]);
								}
								if(perXxx.hasOwnProperty("wzsm"))
								{
									perXxx["wzsm"] =  recApp["children"][i][j]["wzsm"];
									
								}
							}
						}		
					})
				})						
			}else if(recApp.isSingleLine  == "Y"){
				//单行容器
				if( obj["children"] && $.isArray( obj["children"])){
					$.each(obj["children"],function(i,perXxx){
						////console.log(perXxx["itemId"] +"-->"+recApp["children"][i]["value"]+"-->"+i);										
						perXxx["value"] = 	SurveyBuild.htmlCharReplace(recApp["children"][i]["value"]);
						////console.log("Loadvalue:"+recApp["children"][i]["value"]);
						if(perXxx.hasOwnProperty("wzsm"))
						{
							perXxx["wzsm"] =  recApp["children"][i]["wzsm"];
							////console.log("Loadwzsm:"+recApp["children"][i]["wzsm"]);
						}
					})
				}
			}else if(recApp.classname  == "imagesUpload" || recApp.classname  == "AttachmentUpload"){
				//图片或者附件
				for(var j = 1; j< recApp["children"] .length;j++){
					obj["children"].push(cloneObj(obj["children"][0]));
				}
				if( obj["children"] && $.isArray( obj["children"])){
					////console.log(recApp["children"].length);
					$.each(obj["children"],function(i,perXxx){	
						////console.log( recApp["children"][i]["fileName"]);
						perXxx["fileName"] = recApp["children"][i]["fileName"];
						perXxx["sysFileName"] = recApp["children"][i]["sysFileName"];
						perXxx["orderby"] = recApp["children"][i]["orderby"];
						//perXxx["path"] = recApp["children"][i]["path"];	By WRL
						perXxx["accessPath"] = recApp["children"][i]["accessPath"];
						perXxx["viewFileName"] = recApp["children"][i]["viewFileName"];
					})
				}
			}else if(recApp.classname  == "Radio" || recApp.classname  == "Check"){
				/*单选框和复选框处理方式*/
				if( obj["option"] ){
					$.each(obj["option"],function(i,optionInfo){	
						////console.log(optionInfo);
						if(recApp["option"][i]){
							optionInfo["checked"] = recApp["option"][i]["checked"];
							if(recApp["option"][i]["othervalue"]!=undefined ){
								optionInfo["othervalue"] = recApp["option"][i]["othervalue"];
							}else{
								optionInfo["othervalue"] = "";
							}
						}
					})
				}
				if(recApp.hasOwnProperty("value")&&obj.hasOwnProperty("value"))
				{
					obj["value"] = recApp["value"];
				}
				if(recApp["othervalue"]){
					obj["othervalue"] = SurveyBuild.htmlCharReplace(recApp["othervalue"]);
				}	
			}else{
				/*leve0级别普通字段*/
				if( recApp["value"]){
					obj["value"] = SurveyBuild.htmlCharReplace(recApp["value"]);
				}
				if(recApp["othervalue"]){
					obj["othervalue"] = SurveyBuild.htmlCharReplace(recApp["othervalue"]);
				}
				if(recApp["wzsm"]){
					obj["wzsm"] = recApp["wzsm"];
				}
			}
		}
	})
}

////console.log(JSON.stringify(data.items));
/*加载页面信息*/

SurveyBuild["_items"] = data.items;
SurveyBuild.is_edit_moda = false;
SurveyBuild._load();
SurveyBuild._initAssociatedShowHide();

/*处理select的样式*/
var config = {
				  '.chosen-select'           : {},
				  '.chosen-select-deselect'  : {allow_single_deselect:true},
				  '.chosen-select-no-single' : {disable_search_threshold:10},
				  '.chosen-select-no-results': {no_results_text:'Oops, nothing found!'},
				  '.chosen-select-width'     : {width:"95%"}
			};
for (var selector in config) {
  $(selector).chosen(config[selector]);
}
////console.log($(".bmbtjxtitle"));
$(".bmbtjxtitle").find(".chosen-container").unbind();


/*默认点击选中的Tab页签*/
$(".menu-active").click();

////console.log(appReadOnly);
if(appReadOnly == "Y"){
	pageReadOnly();
}else{
	if(!currentPageId){
		$('#app_save').show(); 
		$('#app_submit').show();
	}
}

layer.closeAll();
//单选框、复选框、多选框，其他

/*
	Custom checkbox and radio button - Jun 18, 2013
	(c) 2013 @ElmahdiMahmoud 
	license: http://www.opensource.org/licenses/mit-license.php
*/   
//$('input[type="radio"]').wrap('<div class="radio-btn"><i></i></div>');
//$(".radio-btn").on('click', function () {
//    var _this = $(this),block = _this.parent().parent();
//    block.find('input:radio').attr('checked', false);
//    block.find(".radio-btn").removeClass('checkedRadio');
//    _this.addClass('checkedRadio');
//    _this.find('input:radio').attr('checked', true);
//});

//$('input[type="checkbox"]').wrap('<div class="check-box"><i></i></div>');
//$.fn.toggleCheckbox = function () {
//    this.attr('checked', !this.attr('checked'));
//}
//$('.check-box').on('click', function () {
//    $(this).find(':checkbox').toggleCheckbox();
//    $(this).toggleClass('checkedBox');
//});