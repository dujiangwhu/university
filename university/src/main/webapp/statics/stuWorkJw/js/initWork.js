var TzUniversityContextPath = "/university";
var urlBegin = "";
$(function() {
	TzUniversityContextPath = $("#projectName").val();
	urlBegin = TzUniversityContextPath + "/dispatcher";
	var OPRID = $("#OPRID").val();
	$("#logout").click(function() {
				window.location.href = TzUniversityContextPath + "/stuLogout";
			});

	$("#serchMyCourseWork").click(function() {
		var OPRID = $("#OPRID").val();
		var searchValue = $(this).prev().find("input[class='text3']").val()
				.trim();
		$.post(TzUniversityContextPath + "/serchMyCourseWork", {
					"searchValue" : searchValue,
					"oprid" : OPRID
				}, function(data) {
					var listData = JSON.parse(data).listData;
					//console.log(listData);
					var reStr = dealCourseListData(listData);
					if(reStr==undefined){
					$("#courseWorkList").hide();
					}else{
						$("#courseWorkList").html(reStr);
					}
				});
	});

	$.ajax({
		type : "post",
		url : TzUniversityContextPath + "/courseWork",
		dataType : "json",
		data : {
			"oprid" : OPRID
		},
		success : function(response) {
			var listData = response.listData;
			var reStr = dealCourseListData(listData);
			//console.log("reStr1="+reStr+"\n");
			$("#courseWorkList").append(reStr);

			var lwListData = response.lwListData;
			//console.log(lwListData);
			var lunwenStr = "";
			if (lwListData != null && lwListData.length > 0) {
				//对提交和未提交进行排序
				var tdlwTJ=new Array();
				var tdlwWTJ=new Array();
				var x=0;
				var y=0;
				for (var i = 0; i < lwListData.length; i++) {
					//console.log("论文"+i);
					var tdStr = "";
					var tdStr1 = "";
					var TZ_MK_NAME = lwListData[i].TZ_MK_NAME == null
							? ""
							: lwListData[i].TZ_MK_NAME;
					var TZ_KECH_MC = lwListData[i].TZ_KECH_MC == null
							? ""
							: lwListData[i].TZ_KECH_MC;
					var TZ_KECH_EMC = lwListData[i].TZ_KECH_EMC == null
							? ""
							: lwListData[i].TZ_KECH_EMC;
					var TZ_EM_BGN_DT = lwListData[i].TZ_EM_BGN_DT == null
							? ""
							: lwListData[i].TZ_EM_BGN_DT;
					var TZ_EM_END_DT = lwListData[i].TZ_EM_END_DT == null
							? ""
							: lwListData[i].TZ_EM_END_DT;
					var TZ_SKDD = lwListData[i].TZ_SKDD == null
							? ""
							: lwListData[i].TZ_SKDD;
							
					var TZ_SHKE_JSHI = lwListData[i].TZ_SHKE_JSHI == null
							? ""
							: lwListData[i].TZ_SHKE_JSHI;
					if (TZ_SHKE_JSHI === null) {
						TZ_SHKE_JSHI = "";
					}
					var TZ_KECH_ZXF = lwListData[i].TZ_KECH_ZXF == null
							? ""
							: lwListData[i].TZ_KECH_ZXF;
					var TZ_ZY_TYPE = lwListData[i].TZ_ZY_TYPE == null
							? ""
							: lwListData[i].TZ_ZY_TYPE;
					var TZ_ZZCJ = lwListData[i].TZ_ZZCJ == null
							? ""
							: lwListData[i].TZ_ZZCJ;

					// if (TZ_ZY_TYPE == "KQ") {
					// TZ_ZY_TYPE = "课前";
					// } else if (TZ_ZY_TYPE == "KH") {
					// TZ_ZY_TYPE = "课后";
					// }
					var TZ_KJ_ID = lwListData[i].TZ_KJ_ID == null
							? ""
							: lwListData[i].TZ_KJ_ID;		
					var TZ_ZY_NAME = lwListData[i].TZ_ZY_NAME;
					
					var TZ_ZY_NAMEStr = "",TZ_KJ_IDStr="";
					if (TZ_ZY_NAME != "" && !(TZ_ZY_NAME === null)) {
						var vArray = TZ_ZY_NAME.split(";");
						var kjArray = TZ_KJ_ID.split(";");
						for (var m = 0; m < vArray.length; m++) {
							if (vArray[m] != "" && !(vArray[m] === null)) {
								var infoArray = vArray[m].split(",")
								var url = infoArray[1];
								var workName = infoArray[0];
								if (url == "") {
									url = "#";
								}
								if (m == vArray.length - 1) {
									TZ_ZY_NAMEStr = TZ_ZY_NAMEStr
											+ "<a target='_blank' href='"
											+ "workNameServlet?TZ_KJ_ID="+kjArray[m]
											+ "'>" + workName + "</a>";
								} else {
									TZ_ZY_NAMEStr = TZ_ZY_NAMEStr
											+ "<a target='_blank' href='"
											+ "workNameServlet?TZ_KJ_ID="+kjArray[m]
											+ "'>" + workName + "</a> </br>";
								}
							}
						}
					}
					var ZY = lwListData[i].ZY;
					var TZ_PKSK_XH = lwListData[i].TZ_PKSK_XH;
					var ZYStrZH = "";
					var ZYStrEN = "";
					var xuHaoInput = "<input type='hidden' class='ZYFJXH' value='"
							+ TZ_PKSK_XH + "' />";
					var delBtn = "&nbsp;<button class='but3' onclick='deleteFile(this)'><img src='"
							+ TzUniversityContextPath
							+ "/statics/stuWorkJw/images/del.png'/></button>";
					var uploadDiv = "<div class='filebtn'><div class='filebtn-org'>上传</div>"
							+ "<form name='uploadStuWorkForm' action='/university/dispatcher/UpdWebServlet?filePath=stuWork'"
							+ "enctype='multipart/form-data' method='post'><input type='file' class='filebtn-orgtext' "
							+ "name='websitefile' onchange='uploadStuLw(this)'></form></div>";
					if (ZY != "" && !(ZY == null)) {
						var vArray = ZY.split(";");
						var xuhaoArrya = TZ_PKSK_XH.split(",");
						for (var n = 0; n < vArray.length; n++) {
							var xuhao = xuhaoArrya[n];
							var xuHaoInput = "<input type='hidden' class='ZYFJXH' value='"
									+ xuhao + "' />";
							if (vArray[n] != "" && !(vArray[n] === null)) {
								var infoArray = vArray[n].split(",");

								var url = infoArray[infoArray.length - 1];
								var workName = "";
								for (var index2 = 0; index2 < infoArray.length
										- 1; index2++) {
									if (index2 == infoArray.length - 2) {
										workName += infoArray[index2];
									} else {
										workName += infoArray[index2] + ",";
									}
								}
								var zyTypeInput = "";
								if (workName.indexOf("中文版") > -1) {
									zyTypeInput = "<input class='TZ_ZY_TYPE' type='hidden' value='LWZ' />";
									ZYStrZH = "<div><a target='_blank' href='"
											+ TzUniversityContextPath + url
											+ "'>" + workName + "</a>"
											+ xuHaoInput + zyTypeInput + delBtn
											+ "</div>";
								} else if (workName.indexOf("英文版") > -1) {
									zyTypeInput = "<input class='TZ_ZY_TYPE' type='hidden' value='LWE' />";
									ZYStrEN = "<div><a target='_blank' href='"
											+ TzUniversityContextPath + url
											+ "'>" + workName + "</a> "
											+ xuHaoInput + zyTypeInput + delBtn
											+ "</div>";
								}
								if (url == "") {
									url = "#";
								}
								
								
								/*if (n == vArray.length - 1) {
									ZYStr = ZYStr
											+ "<div><a target='_blank' href='"
											+ TzUniversityContextPath + url
											+ "'>" + workName + "</a>"
											+ xuHaoInput + zyTypeInput + delBtn
											+ "</div>";
								} else {
									ZYStr = ZYStr
											+ "<div><a target='_blank' href='"
											+ TzUniversityContextPath + url
											+ "'>" + workName + "</a> "
											+ xuHaoInput + zyTypeInput + delBtn
											+ "</div>";
								} */
								
								
							}
						}
					}
					
					var TZ_KCZY_ZT = lwListData[i].TZ_KCZY_ZT;
						if (TZ_KCZY_ZT == null|| TZ_KCZY_ZT.indexOf("N") > -1) {
							TZ_KCZY_ZT = "未提交";
							tdStr1="<div class='black'>" + TZ_KCZY_ZT
								+ "</div>" + "</td><td>"+TZ_ZZCJ+"</td></tr>";
						} else{
							TZ_KCZY_ZT = "已提交";
								tdStr1="<div class='red'>" + TZ_KCZY_ZT
								+ "</div>" + "</td><td>"+TZ_ZZCJ+"</td></tr>";
						}
					
					var TZ_ZZCJ = lwListData[i].TZ_ZZCJ;

					var TZ_KAIK_KCH_ID = lwListData[i].TZ_KAIK_KCH_ID;
					//if (i == 0) {
						if(ZY != "" && !(ZY == null)&& ZYStrZH!="" && ZYStrEN !=""){
								lunwenStr = 
								"<tr><td class='kaikeid' style='display: none;'>"
								+ TZ_KAIK_KCH_ID
								+ "</td><td>"
								+ TZ_MK_NAME
								+ "</td><td>"
								+ TZ_KECH_MC
								+ "</br>"
								+ TZ_KECH_EMC
								+ "</td>"
								+ "<td>"
								/*+ TZ_EM_BGN_DT
								+ "</td><td>"*/
								+ TZ_EM_END_DT
								/*+ "</td><td>"
								+ TZ_SKDD
								+ "</td><td>"
								+ TZ_SHKE_JSHI
								+ "</td><td rowspan='"
								+ lwListData.length
								+ "'>"
								+ TZ_KECH_ZXF*/
								+ "</td>"
								+ "<td>"
								+ TZ_ZY_NAMEStr
								+ "</td><td>" + ZYStrZH
								+ "</td><td>" + ZYStrEN
								+"</td><td>"+tdStr1;
						}else if(ZY != "" && !(ZY == null)&&  ZYStrZH!="" && ZYStrEN ==""&&TZ_ZY_TYPE.indexOf("LWE")>-1){
							lunwenStr = 
								"<tr><td class='kaikeid' style='display: none;'>"
								+ TZ_KAIK_KCH_ID
								+ "</td><td>"
								+ TZ_MK_NAME
								+ "</td><td>"
								+ TZ_KECH_MC
								+ "</br>"
								+ TZ_KECH_EMC
								+ "</td>"
								+ "<td>"
								/*+ TZ_EM_BGN_DT
								+ "</td><td>"*/
								+ TZ_EM_END_DT
								/*+ "</td><td>"
								+ TZ_SKDD
								+ "</td><td>"
								+ TZ_SHKE_JSHI
								+ "</td><td rowspan='"
								+ lwListData.length
								+ "'>"
								+ TZ_KECH_ZXF*/
								+ "</td><td>"
								+ TZ_ZY_NAMEStr
								+ "</td><td>" + ZYStrZH
								+ "</td><td><div style='display:none'>LWE</div>"
							    + uploadDiv
								+"</td><td>"+tdStr1;
						}else if(ZY != "" && !(ZY == null)&&  ZYStrZH!="" && ZYStrEN ==""&&TZ_ZY_TYPE.indexOf("LWE")<=-1){
							lunwenStr = 
								 "<tr><td class='kaikeid' style='display: none;'>"
								+ TZ_KAIK_KCH_ID
								+ "</td><td>"
								+ TZ_MK_NAME
								+ "</td><td>"
								+ TZ_KECH_MC
								+ "</br>"
								+ TZ_KECH_EMC
								+ "</td>"
								+ "<td>"
								/*+ TZ_EM_BGN_DT
								+ "</td><td>"*/
								+ TZ_EM_END_DT
								/*+ "</td><td>"
								+ TZ_SKDD
								+ "</td><td>"
								+ TZ_SHKE_JSHI
								+ "</td><td rowspan='"
								+ lwListData.length
								+ "'>"
								+ TZ_KECH_ZXF*/
								+ "</td><td>"
								+ TZ_ZY_NAMEStr
								+ "</td><td>" + ZYStrZH
								+ "</td><td></td><td>"+tdStr1;
						}else if(ZY != "" && !(ZY == null)&&  ZYStrZH=="" && ZYStrEN !=""&&TZ_ZY_TYPE.indexOf("LWZ")>-1){
							lunwenStr = 
								"<tr><td class='kaikeid' style='display: none;'>"
								+ TZ_KAIK_KCH_ID
								+ "</td><td>"
								+ TZ_MK_NAME
								+ "</td><td>"
								+ TZ_KECH_MC
								+ "</br>"
								+ TZ_KECH_EMC
								+ "</td>"
								+ "<td>"
								/*+ TZ_EM_BGN_DT
								+ "</td><td>"*/
								+ TZ_EM_END_DT
								/*+ "</td><td>"
								+ TZ_SKDD
								+ "</td><td>"
								+ TZ_SHKE_JSHI
								+ "</td><td rowspan='"
								+ lwListData.length
								+ "'>"
								+ TZ_KECH_ZXF*/
								+ "</td><td>"
								+ TZ_ZY_NAMEStr
								+ "</td><td><div style='display:none'>LWZ</div>"
								+ uploadDiv
								+"</td><td>"+ZYStrEN
								+"</td><td>"+tdStr1;
						}else if(ZY != "" && !(ZY == null)&&  ZYStrZH=="" && ZYStrEN !=""&&TZ_ZY_TYPE.indexOf("LWZ")<=-1){
							lunwenStr = 
								"<tr><td class='kaikeid' style='display: none;'>"
								+ TZ_KAIK_KCH_ID
								+ "</td><td>"
								+ TZ_MK_NAME
								+ "</td><td>"
								+ TZ_KECH_MC
								+ "</br>"
								+ TZ_KECH_EMC
								+ "</td>"
								+ "<td>"
								/*+ TZ_EM_BGN_DT
								+ "</td><td>"*/
								+ TZ_EM_END_DT
								/*+ "</td><td>"
								+ TZ_SKDD
								+ "</td><td>"
								+ TZ_SHKE_JSHI
								+ "</td><td rowspan='"
								+ lwListData.length
								+ "'>"
								+ TZ_KECH_ZXF*/
								+ "</td><td>"
								+ TZ_ZY_NAMEStr
								+ "</td><td></td><td>"+ZYStrEN
								+"</td><td>"+tdStr1;
						}else if(ZY == "" ||(ZY == null)&&TZ_ZY_TYPE.indexOf("LWZ")>-1&&TZ_ZY_TYPE.indexOf("LWE")<=-1){ 
								lunwenStr = 
								"<tr><td class='kaikeid' style='display: none;'>"
								+ TZ_KAIK_KCH_ID
								+ "</td><td>"
								+ TZ_MK_NAME
								+ "</td><td>"
								+ TZ_KECH_MC
								+ "</br>"
								+ TZ_KECH_EMC
								+ "</td>"
								+ "<td>"
								/*+ TZ_EM_BGN_DT
								+ "</td><td>"*/
								+ TZ_EM_END_DT
								/*+ "</td><td>"
								+ TZ_SKDD
								+ "</td><td>"
								+ TZ_SHKE_JSHI
								+ "</td><td rowspan='"
								+ lwListData.length
								+ "'>"
								+ TZ_KECH_ZXF*/
								+ "</td><td>"
								+ TZ_ZY_NAMEStr
								+ "</td><td><div style='display:none'>LWZ</div>"
								+ uploadDiv
								+ "</td><td></td><td>"+tdStr1;
						}else if(ZY == "" ||(ZY == null)&&TZ_ZY_TYPE.indexOf("LWE")>-1&&TZ_ZY_TYPE.indexOf("LWZ")<=-1){ 
								lunwenStr =
								"<tr><td class='kaikeid' style='display: none;'>"
								+ TZ_KAIK_KCH_ID
								+ "</td><td>"
								+ TZ_MK_NAME
								+ "</td><td>"
								+ TZ_KECH_MC
								+ "</br>"
								+ TZ_KECH_EMC
								+ "</td>"
								+ "<td>"
								/*+ TZ_EM_BGN_DT
								+ "</td><td>"*/
								+ TZ_EM_END_DT
								/*+ "</td><td>"
								+ TZ_SKDD
								+ "</td><td>"
								+ TZ_SHKE_JSHI
								+ "</td><td rowspan='"
								+ lwListData.length
								+ "'>"
								+ TZ_KECH_ZXF*/
								+ "</td><td>"
								+ TZ_ZY_NAMEStr
								+ "</td><td></td><td><div style='display:none'>LWE</div>"
								+ uploadDiv
								+ "</td><td>"+tdStr1;
						}else if(ZY == "" ||(ZY == null)&&TZ_ZY_TYPE.indexOf("LWE")>-1&&TZ_ZY_TYPE.indexOf("LWZ")>-1){ 
								lunwenStr = 
								 "<tr><td class='kaikeid' style='display: none;'>"
								+ TZ_KAIK_KCH_ID
								+ "</td><td>"
								+ TZ_MK_NAME
								+ "</td><td>"
								+ TZ_KECH_MC
								+ "</br>"
								+ TZ_KECH_EMC
								+ "</td>"
								+ "<td>"
								/*+ TZ_EM_BGN_DT
								+ "</td><td>"*/
								+ TZ_EM_END_DT
								/*+ "</td><td>"
								+ TZ_SKDD
								+ "</td><td>"
								+ TZ_SHKE_JSHI
								+ "</td><td rowspan='"
								+ lwListData.length
								+ "'>"
								+ TZ_KECH_ZXF*/
								+ "</td><td>"
								+ TZ_ZY_NAMEStr
								+ "</td><td><div style='display:none'>LWZ</div>"
								+ uploadDiv
								+ "</td><td><div style='display:none'>LWE</div>"
								+ uploadDiv
								+ "</td><td>"+tdStr1;
						}else if(TZ_ZY_TYPE.indexOf("LWE")<=-1&&TZ_ZY_TYPE.indexOf("LWZ")<=-1){
						lunwenStr = 
								 "<tr><td class='kaikeid' style='display: none;'>"
								+ TZ_KAIK_KCH_ID
								+ "</td><td>"
								+ TZ_MK_NAME
								+ "</td><td>"
								+ TZ_KECH_MC
								+ "</br>"
								+ TZ_KECH_EMC
								+ "</td>"
								+ "<td>"
								/*+ TZ_EM_BGN_DT
								+ "</td><td>"*/
								+ TZ_EM_END_DT
								/*+ "</td><td>"
								+ TZ_SKDD
								+ "</td><td>"
								+ TZ_SHKE_JSHI
								+ "</td><td rowspan='"
								+ lwListData.length
								+ "'>"
								+ TZ_KECH_ZXF*/
								+ "</td><td>"
								+ TZ_ZY_NAMEStr
								+ "</td><td></td><td></td><td>"+tdStr1;
								
						}	
						if(lwListData[i].TZ_KCZY_ZT == null|| lwListData[i].TZ_KCZY_ZT.indexOf("N") > -1){
									tdlwWTJ[x]=lunwenStr;
									x=x+1;
							}else{
									tdlwTJ[y]=lunwenStr
									y=y+1
							}
			
				//}
						//console.log(lunwenStr);
				
			}
		var reStr = "";
		//console.log(tdArray_WTJ.length);
		for (var indexe1 = 0; indexe1 < tdlwWTJ.length; indexe1++) {
			//console.log("++++++++++++++++++++");
			//console.log("++++++++++++++++++++"+tdArray_WTJ);
			//console.log(tdArray_WTJ[indexe1]);
			if (tdlwWTJ[indexe1] != undefined) {
				reStr += tdlwWTJ[indexe1];
			}
		}
		//console.log("=========================");
		//console.log(reStr);
		
		//console.log(tdArray.length);
		for (var j = 0; j < tdlwTJ.length; j++) {
			//console.log("=========================");
			//console.log(tdTJ[j]);
			if (tdlwTJ[j] != undefined) {
				reStr += tdlwTJ[j];
			}
		}
			$("#luWenWorkList").append(reStr);
		}else {
			console.log("隐藏模块文件");
			$("#luWenWorkHeadList").attr("style","display:none;");
		}
		},
		error : function(response) {
			// var listData = response.listData;
			// alert("111");
		}

	
});
});

function uploadStuWork(btn) {
	var file = $(btn).val();

	if (!/.(doc|docx|ppt|pptx|pdf|rar|zip)$/.test(file)) {
		alert("文件类型必须是.doc,docx,ppt,pptx,pdf,rar,zip中的一种");
		$(btn).val("");
		return;
	}

	try {
		/*
		 * var fileSize = 0; // for IE if ($.support.msie) { // before making an
		 * object of ActiveXObject, // please make sure ActiveX is enabled in
		 * your IE browser var objFSO = new
		 * ActiveXObject("Scripting.FileSystemObject"); var filePath =
		 * $(btn)[0].value; var objFile = objFSO.getFile(filePath); var fileSize =
		 * objFile.size; // size in kb // size in mb fileSize = fileSize /
		 * 1048576; if (fileSize > 20) { alert("文件大小不能超过20M"); $(btn).val("");
		 * return; } } // for FF, Safari, Opeara and Others else { fileSize =
		 * $(btn)[0].files[0].size // size in kb // size in mb fileSize =
		 * fileSize / 1048576; if (fileSize > 20) { alert("文件大小不能超过20M");
		 * $(btn).val(""); return; } }
		 */
		// var formData= new FormData($(btn).parent());
		var TZ_KJ_TYPE = $(btn).parents("td").prev().prev().html().trim();
		if (TZ_KJ_TYPE == "课前") {
			TZ_KJ_TYPE = "KQ";
		} else if (TZ_KJ_TYPE == "课后") {
			TZ_KJ_TYPE = "KH";
		}
		var TZ_KAIK_KCH_ID = $(btn).parents("tr").find("td[class='kaikeid']")
				.html();
		var OPRID = $("#OPRID").val();
		loading();
		$(btn).parent().ajaxSubmit({
			url : TzUniversityContextPath + "/UpdWebServlet?filePath=stuWork",
			dataType : "json",
			success : function(responseData) {
				var data = responseData.msg;
				var file_NAME = data.filename;
				var file_SYSNAME = data.sysFileName;
				var file_SAVEPATH = data.accessPath;
				$.ajax({
					url : TzUniversityContextPath + "/saveStuWork",
					type : "post",
					data : {
						"TZ_KAIK_KCH_ID" : TZ_KAIK_KCH_ID,
						"OPRID" : OPRID,
						"TZ_KJ_TYPE" : TZ_KJ_TYPE,
						"file_SYSNAME" : file_SYSNAME,
						"file_SAVEPATH" : file_SAVEPATH
					},
					dataType : "json",
					success : function(response) {
						if (response.success) {
							var divStr = "";
							var zyTypeInput = "<input class='TZ_ZY_TYPE' type='hidden' value='"
									+ TZ_KJ_TYPE + "' />";
							var xuHaoInput = "<input type='hidden' class='ZYFJXH' value='"
									+ response.TZ_PKSK_XH + "' />";
							var delBtn = "&nbsp;<button class='but3' onclick='deleteFile(this)'><img src='"
									+ TzUniversityContextPath
									+ "/statics/stuWorkJw/images/del.png'/></button>";
							divStr = "<div><a target='_blank' href='"
									+ TzUniversityContextPath + file_SAVEPATH
									+ file_SYSNAME + "'>" + response.file_NAME
									+ "</a> " + xuHaoInput + zyTypeInput
									+ delBtn + "</div>";
							"<div><a target='_blank' href='"
									+ TzUniversityContextPath + file_SAVEPATH
									+ file_SYSNAME + "'>" + response.file_NAME
									+ "</a> " + xuHaoInput + zyTypeInput
									+ delBtn + "</div>";
							$(btn).parents("td").append(divStr);
 							
							$(btn).parents("td").next()
									.html("<div class='red'>已提交</div>");
							$(btn).parents(".filebtn").remove();
						}else if(response.exist){
							alert(response.messge);
						}
					},
					error : function() {

					}
				});
				layer.close(layer.index);/* 关闭上传进度条 */
			},
			error : function() {
				layer.close(layer.index);/* 关闭上传进度条 */
			}
		});

	} catch (e) {
		alert("Error is :" + e);
	}
}

function uploadStuLw(btn) {
	var file = $(btn).val();

	if (!/.(doc|docx|ppt|pptx|pdf|rar|zip)$/.test(file)) {
		alert("文件类型必须是.doc,docx,ppt,pptx,pdf,rar,zip中的一种");
		$(btn).val("");
		return;
	}

	try {
		/*
		 * var fileSize = 0; // for IE if ($.support.msie) { // before making an
		 * object of ActiveXObject, // please make sure ActiveX is enabled in
		 * your IE browser var objFSO = new
		 * ActiveXObject("Scripting.FileSystemObject"); var filePath =
		 * $(btn)[0].value; var objFile = objFSO.getFile(filePath); var fileSize =
		 * objFile.size; // size in kb // size in mb fileSize = fileSize /
		 * 1048576; if (fileSize > 20) { alert("文件大小不能超过20M"); $(btn).val("");
		 * return; } } // for FF, Safari, Opeara and Others else { fileSize =
		 * $(btn)[0].files[0].size // size in kb // size in mb fileSize =
		 * fileSize / 1048576; if (fileSize > 20) { alert("文件大小不能超过20M");
		 * $(btn).val(""); return; } }
		 */
		// var formData= new FormData($(btn).parent());
//console.log($(btn).parents("div").html());
		//console.log($(btn).parents("div").prev().html());
		var TZ_KJ_TYPE = $(btn).parents("div").prev().html().trim();
//console.log($(btn).parents("div").prev());
		//var vArray = TZ_KJ_TYPE.split(",");
		
		//for(var i=0;i<vArray.length;i++){
		//	var vArrayLW=vArray[i]
		//	if(vArrayLW=="LWZ"){
		//		TZ_KJ_TYPE="LWZ";
		//	}else if(vArrayLW=="LWE"){
		//		TZ_KJ_TYPE="LWE";
		//	}
		//}
		//console.log(TZ_KJ_TYPE);
		var TZ_KAIK_KCH_ID = $(btn).parents("tr").find("td[class='kaikeid']")
				.html();
		var OPRID = $("#OPRID").val();
		loading();
		$(btn).parent().ajaxSubmit({
			url : TzUniversityContextPath + "/UpdWebServlet?filePath=stuWork",
			dataType : "json",
			success : function(responseData) {
				var data = responseData.msg;
				var file_NAME = data.filename;
				var file_SYSNAME = data.sysFileName;
				var file_SAVEPATH = data.accessPath;
				$.ajax({
					url : TzUniversityContextPath + "/saveStuWork",
					type : "post",
					data : {
						"TZ_KAIK_KCH_ID" : TZ_KAIK_KCH_ID,
						"OPRID" : OPRID,
						"TZ_KJ_TYPE" : TZ_KJ_TYPE,
						"file_SYSNAME" : file_SYSNAME,
						"file_SAVEPATH" : file_SAVEPATH,
						"LW" : "LW"
					},
					dataType : "json",
					success : function(response) {
						if (response.success) {
							var divStr = "";
							var zyTypeInput = "<input class='TZ_ZY_TYPE' type='hidden' value='"
									+ TZ_KJ_TYPE + "' />";
							var xuHaoInput = "<input type='hidden' class='ZYFJXH' value='"
									+ response.TZ_PKSK_XH + "' />";
							var delBtn = "&nbsp;<button class='but3' onclick='deleteFile(this)'><img src='"
									+ TzUniversityContextPath
									+ "/statics/stuWorkJw/images/del.png'/></button>";
							divStr = "<div><a target='_blank' href='"
									+ TzUniversityContextPath + file_SAVEPATH
									+ file_SYSNAME + "'>" + response.file_NAME
									+ "</a> " + xuHaoInput + zyTypeInput
									+ delBtn + "</div>";
							"<div><a target='_blank' href='"
									+ TzUniversityContextPath + file_SAVEPATH
									+ file_SYSNAME + "'>" + response.file_NAME
									+ "</a> " + xuHaoInput + zyTypeInput
									+ delBtn + "</div>";
							if (TZ_KJ_TYPE == "LWZ") {
								$(btn).parents("td")
										.append(divStr);
										
								//console.log($(btn).parents("td").html());
								//console.log($(btn).parents("td").next().html());
								if(response.TZ_KCZY_ZT=="已提交"){
									$(btn).parents("td").next().next()
											.html("<div class='red'>"
													+ response.TZ_KCZY_ZT
													+ "</div>");
								}else{
									$(btn).parents("td").next().next()
										.html("<div class='black'>"
												+ response.TZ_KCZY_ZT
												+ "</div>");
								}
								
								$(btn).parents(".filebtn").remove();
							} else if (TZ_KJ_TYPE == "LWE") {
								$(btn).parents("td").append(divStr);
								if(response.TZ_KCZY_ZT=="已提交"){
									$(btn).parents("td").next()
											.html("<div class='red'>"
													+ response.TZ_KCZY_ZT
													+ "</div>");
								}else{
									$(btn).parents("td").next()
										.html("<div class='black'>"
												+ response.TZ_KCZY_ZT
												+ "</div>");
								}
								$(btn).parents(".filebtn").remove();
							}
							

						}else if(response.exist){
							alert(response.messge);
						}
					},
					error : function() {

					}
				});
				layer.close(layer.index);/* 关闭上传进度条 */
			},
			error : function() {
				layer.close(layer.index);/* 关闭上传进度条 */
			}
		});

	} catch (e) {
		alert("Error is :" + e);
	}
}

function loading() {
	var Tipenr = "";
	if ($("#lang").val() == "ENG") {
		Tipenr = "saving";

	} else {
		Tipenr = "保存中...";

	}
	var loadi = layer.load(Tipenr);
	layer.area(loadi, "top:200px");
}

function deleteFile(btn) {
	var msg = "您确定要删除吗？\n\n请确认！";
	var TZ_PKSK_XH = $(btn).parent().find("input[class='ZYFJXH']").val();
	//console.log("删除"+TZ_PKSK_XH);
	var TZ_KJ_TYPE = $(btn).parent().find("input[class='TZ_ZY_TYPE']").val();
	var TZ_KAIK_KCH_ID = $(btn).parents("tr").find("td[class='kaikeid']")
			.html();
	var OPRID = $("#OPRID").val();
	var uploadDiv = "<div class='filebtn'><div class='filebtn-org'>上传</div>"
					+ "<form name='uploadStuWorkForm' action='/university/dispatcher/UpdWebServlet?filePath=stuWork'"
					+ "enctype='multipart/form-data' method='post'><input type='file' class='filebtn-orgtext'"
					+ "name='websitefile' onchange='uploadStuWork(this)'></form></div>";
	var uploadDiv1 = "<div class='filebtn'><div class='filebtn-org'>上传</div>"
							+ "<form name='uploadStuWorkForm' action='/university/dispatcher/UpdWebServlet?filePath=stuWork'"
							+ "enctype='multipart/form-data' method='post'><input type='file' class='filebtn-orgtext' "
							+ "name='websitefile' onchange='uploadStuLw(this)'></form></div>";
							//console.log("删除"+btn);
	if (confirm(msg) == true) {
		$.ajax({
					type : "POST",
					url : TzUniversityContextPath + "/deleteStuWork",
					dataType : "json",
					data : {
						"TZ_KAIK_KCH_ID" : TZ_KAIK_KCH_ID,
						"OPRID" : OPRID,
						"TZ_PKSK_XH" : TZ_PKSK_XH,
						"TZ_KJ_TYPE" : TZ_KJ_TYPE
					},
					success : function(data) {
						if (data.TJZT == "N") {
							if(TZ_KJ_TYPE=="LWZ"){
								$(btn).parents("td").next().next()
									.html("<div class='black'>未提交</div>");
									
							}else{
								$(btn).parents("td").next()
									.html("<div class='black'>未提交</div>");
									
							}
							
									
						}
						if(TZ_KJ_TYPE=="LWZ"){
								$(btn).parents("td").append("<div style='display:none'>LWZ</div>"+uploadDiv1);
							}else if(TZ_KJ_TYPE=="LWE"){
								$(btn).parents("td").append("<div style='display:none'>LWE</div>"+uploadDiv1);
							}else{
								$(btn).parents("td").append(uploadDiv);
								}
						$(btn).parent().remove();
						alert("删除成功");
						
					}
				});
	} else {
		return;
	}
}

//课程列表
function dealCourseListData(listData) {
	
	//console.log("dealCourseListData");
	if (listData != null && listData.length > 0) {

		//var tdArray = new Array();
		
		//tdArray_WTJ 作为循环里面 每一行，每次循环，比较 该次循环的开课ID 是否和上一次循环的开课ID 一致
		// 如果 不一致，没问题 tdArray_WTJ数组加1，如果 一致，tdArray_WTJ 不加一，修改上一次循环的行
		var tdArray_WTJ = new Array();
		
		//数组用来记录上一行数据 下载作业要求   上传 作业作业  作业类型
		var last_Array;
		
		var data_Array = new Array();
		var tdTJ=new Array();
		var k=0;
		//var specialArray = new Array(3);
		var tdArrayIndex = 0;
		//var tdArray_WTJIndex = 0;
		for (var i = 0; i < listData.length; i++) {
			
			last_Array = new Array(3);
			
			
			var tdStr = "";
			var tdStr1 = "";
			var tdStr2 = "";
			var tdStr3 = "";
			var tdStr4 = "";
			var tdStr5 = "";
			var tdStr6 = "";
			var TZ_MK_NAME = listData[i].TZ_MK_NAME == null? ""
					: listData[i].TZ_MK_NAME;
			var TZ_KECH_MC = listData[i].TZ_KECH_MC == null
					? ""
					: listData[i].TZ_KECH_MC;
			var TZ_KECH_EMC = listData[i].TZ_KECH_EMC == null
					? ""
					: listData[i].TZ_KECH_EMC;
			var TZ_EM_BGN_DT = listData[i].TZ_EM_BGN_DT == null
					? ""
					: listData[i].TZ_EM_BGN_DT;
			var TZ_EM_END_DT = listData[i].TZ_EM_END_DT == null
					? ""
					: listData[i].TZ_EM_END_DT;
			var TZ_SKDD = listData[i].TZ_SKDD == null
					? ""
					: listData[i].TZ_SKDD;
			var TZ_SHKE_JSHI = listData[i].TZ_SHKE_JSHI == null
					? ""
					: listData[i].TZ_SHKE_JSHI;
			var TZ_KECH_ZXF = listData[i].TZ_KECH_ZXF == null
					? ""
					: listData[i].TZ_KECH_ZXF;
			var TZ_ZY_TYPE = listData[i].TZ_ZY_TYPE == null
					? ""
					: listData[i].TZ_ZY_TYPE;
			//console.log("----------------------");
			//console.log(i);			
			//console.log(TZ_KECH_MC);
			//console.log(TZ_SHKE_JSHI);
			var zyTypeInput = "<input class='TZ_ZY_TYPE' type='hidden' value='"
					+ TZ_ZY_TYPE + "' />";
			if (TZ_ZY_TYPE == "KQ") {
				TZ_ZY_TYPE = "课前";
			} else if (TZ_ZY_TYPE == "KH") {
				TZ_ZY_TYPE = "课后";
			}
			var TZ_ZY_NAME = listData[i].TZ_ZY_NAME;
			var TZ_ATTACHFILE_NAME = listData[i].TZ_ATTACHFILE_NAME;//作业要求文件名
			var TZ_KJ_ID = listData[i].TZ_KJ_ID == null
							? ""
							: listData[i].TZ_KJ_ID;	
			var TZ_ZY_NAMEStr = "",TZ_KJ_IDStr="";
			if (TZ_ZY_NAME != "" && !(TZ_ZY_NAME === null)) {
				var vArray = TZ_ZY_NAME.split(";");
				var kjArray = TZ_KJ_ID.split(";");
				var attName = TZ_ATTACHFILE_NAME.split(";");
				for (var m = 0; m < vArray.length; m++) {
					if (vArray[m] != "" && !(vArray[m] === null)) {
						var infoArray = vArray[m].split(",")
						
						var url = infoArray[1];
						var workName = infoArray[0];
						if (url == "") {
							url = "#";
						}
						if (m == vArray.length - 1) {
							TZ_ZY_NAMEStr = TZ_ZY_NAMEStr
									+ "<a target='_blank' href='"
									+ "workNameServlet?TZ_KJ_ID="+kjArray[m] + "'>"
									+ attName[m] + "</a>";//workNameworkNameworkNameworkNameworkNameworkNameworkNameworkName
						} else {
							TZ_ZY_NAMEStr = TZ_ZY_NAMEStr
									+ "<a target='_blank' href='"
									+ "workNameServlet?TZ_KJ_ID="+kjArray[m] + "'>"
									+ attName[m] + "</a> </br>";
						}
					}
				}
			}
			
			last_Array[0] = TZ_ZY_NAMEStr;
			last_Array[2] = TZ_ZY_TYPE;
			
			
			
			
			var ZY = listData[i].ZY;
			var TZ_PKSK_XH = listData[i].TZ_PKSK_XH;
			
			var ZYStr = "";
//			var xuHaoInput = "<input type='hidden' class='ZYFJXH' value='"
//					+ TZ_PKSK_XH + "' />";
			var delBtn = "&nbsp;<button class='but3' onclick='deleteFile(this)'><img src='"
					+ TzUniversityContextPath
					+ "/statics/stuWorkJw/images/del.png'/></button>";
			var uploadDiv = "<div class='filebtn'><div class='filebtn-org'>上传</div>"
					+ "<form name='uploadStuWorkForm' action='/university/dispatcher/UpdWebServlet?filePath=stuWork'"
					+ "enctype='multipart/form-data' method='post'><input type='file' class='filebtn-orgtext'"
					+ "name='websitefile' onchange='uploadStuWork(this)'></form></div>";
					
			//console.log(ZY);	
			//有作业，显示作业
			if (ZY != "" && !(ZY === null)) {
				var vArray = ZY.split(";");
				console.log("作业个数"+vArray.length);
				//var xuhaoArrya = TZ_PKSK_XH.split(",");
				for (var n = 0; n < vArray.length; n++) {
					//var xuhao = xuhaoArrya[n];
					console.log("序号"+TZ_PKSK_XH);
					var xuHaoInput = "<input type='hidden' class='ZYFJXH' value='"
							+ TZ_PKSK_XH + "' />";
					if (vArray[n] != "" && !(vArray[n] === null)) {
						var infoArray = vArray[n].split(",");

						var url = infoArray[infoArray.length - 1];
						var workName = "";
						for (var index2 = 0; index2 < infoArray.length - 1; index2++) {
							if (index2 == infoArray.length - 2) {
								workName += infoArray[index2];
							} else {
								workName += infoArray[index2] + ",";
							}
						}

						if (url == "") {
							url = "#";
						}
						if (n == vArray.length - 1) {
							ZYStr = ZYStr + "<div><a target='_blank' href='"
									+ TzUniversityContextPath + url + "'>"
									+ workName + "</a>" + xuHaoInput
									+ zyTypeInput + delBtn + "</div>";
						} else {
							ZYStr = ZYStr + "<div><a target='_blank' href='"
									+ TzUniversityContextPath + url + "'>"
									+ workName + "</a> " + xuHaoInput
									+ zyTypeInput + delBtn + "</div>";
						}
					}
				}
			} 
			//没有作业显示上传按钮
			else {
				ZYStr = uploadDiv;
			}
			last_Array[1] = ZYStr;
			
			
			
			
			
			data_Array[i]=last_Array;
			//console.log("存储"+i);
			//console.log("数据"+data_Array[i]);
			
			
			
			var TZ_KCZY_ZT = listData[i].TZ_KCZY_ZT;
			if (TZ_KCZY_ZT == "Y") {
				TZ_KCZY_ZT = "已提交";
			} else {
				TZ_KCZY_ZT = "未提交";
			}
			
			var TZ_ZZCJ = listData[i].TZ_ZZCJ;

			var TZ_KAIK_KCH_ID = listData[i].TZ_KAIK_KCH_ID;
			//如果有数据
			
			if (i >= 0) {
				//如果本次循环的开课ID和上一个开课ID一样，那么合并表格
				if (i>0 && TZ_KAIK_KCH_ID == listData[i - 1].TZ_KAIK_KCH_ID) {
					//var last = i-1;
					var preTJZT = listData[i - 1].TZ_KCZY_ZT;
					if (preTJZT == "Y") {
						preTJZT = "已提交";
					} else {
						preTJZT = "未提交";
					}
					
					for( var xxxx in data_Array ){
						
					//console.log("______"+data_Array[xxxx]);
					}
					
					//console.log("上一行"+data_Array[i-1]);
					//console.log("本行"+data_Array[i]);
					//修改上一行的tr表格
					tdStr = "<tr><td class='kaikeid' style='display: none;'>"
								+ TZ_KAIK_KCH_ID
								+ "</td><td rowspan='2'>"
								+ TZ_MK_NAME
								+ "</td><td rowspan='2'>"
								+ TZ_KECH_MC
								+ "</br>"
								+ TZ_KECH_EMC
								+ "</td>"
								+ "<td rowspan='2'>"
								+ TZ_EM_BGN_DT
								+ "</td><td rowspan='2'>"
								+ TZ_EM_END_DT
								+ "</td><td rowspan='2'>"
								+ TZ_SKDD
								+ "</td><td rowspan='2'>"
								+ TZ_SHKE_JSHI
								+ "</td><td rowspan='2'>"
								+ TZ_KECH_ZXF
								+ "</td><td>"+data_Array[i-1][2]+"</td>"
								+ "<td>"+data_Array[i-1][0]+"</td><td>"+data_Array[i-1][1]+"</td>";
								
								tdStr2="<td><div class='red'>"
								+ preTJZT
								+ "</div></td>"
								+ "<td rowspan='2'>"
								+ TZ_ZZCJ
								+ "</td></tr>";
								
								tdStr3="<td><div class='black'>"
								+ preTJZT
								+ "</div></td>"
								+ "<td rowspan='2'>"
								+ TZ_ZZCJ
								+ "</td></tr>";
								if(preTJZT=="已提交"){
									tdStr=tdStr+tdStr2;
								}else{
									tdStr=tdStr+tdStr3;
								}
								
						//tdArray_WTJ[i-1]=	tdStr;
						//生产本行的数据
					
								
						tdStr1="<tr><td class='kaikeid' style='display: none;'>"
								+ TZ_KAIK_KCH_ID
								+ "</td><td>"
								+ TZ_ZY_TYPE
								+ "</td><td>"
								+ TZ_ZY_NAMEStr
								+ "</td><td>"
								+ ZYStr
								+ "</td><td><div class='red'>"
								+ TZ_KCZY_ZT
								+ "</div></td></tr>";
						tdStr6="<tr><td class='kaikeid' style='display: none;'>"
								+ TZ_KAIK_KCH_ID
								+ "</td><td>"
								+ TZ_ZY_TYPE
								+ "</td><td>"
								+ TZ_ZY_NAMEStr
								+ "</td><td>"
								+ ZYStr
								+ "</td><td><div class='black'>"
								+ TZ_KCZY_ZT
								+ "</div></td></tr>";
								//tdArray_WTJ[i]=	tdStr;
					if (TZ_KCZY_ZT == "已提交" && preTJZT=="已提交") {
						
							tdTJ[k-1]="";
							k=k-1;

						tdTJ[k]=tdStr;
						tdTJ[k+1]=tdStr1;
						k=k+2;
					}else{
						tdArray_WTJ[i-1]=tdStr;
						if(TZ_KCZY_ZT=="已提交"){
						tdArray_WTJ[i]=	tdStr1;
						}else{
							if(preTJZT=="已提交"){
								tdTJ[k-1]="";
								k=k-1;
							}
							tdArray_WTJ[i]=	tdStr6;
						}
						
					} 
					
				} 
				//如果本次循环的开课ID和上一个开课ID不一样，单独表格
				else {
					tdStr = "<tr><td class='kaikeid' style='display: none;'>"
							+ TZ_KAIK_KCH_ID
							+ "</td><td>"
							+ TZ_MK_NAME
							+ "</td><td>"
							+ TZ_KECH_MC
							+ "</br>"
							+ TZ_KECH_EMC
							+ "</td>"
							+ "<td>"
							+ TZ_EM_BGN_DT
							+ "</td><td>"
							+ TZ_EM_END_DT
							+ "</td><td>"
							+ TZ_SKDD
							+ "</td><td>"
							+ TZ_SHKE_JSHI
							+ "</td><td>"
							+ TZ_KECH_ZXF + "</td><td>" + TZ_ZY_TYPE + "</td><td>"
							+ TZ_ZY_NAMEStr + "</td><td>" + ZYStr
							+ "</td><td>";
							tdStr4="<div class='red'>" + TZ_KCZY_ZT
							+ "</div>" + "</td><td>" + TZ_ZZCJ + "</td></tr>";
							tdStr5="<div class='black'>" + TZ_KCZY_ZT
							+ "</div>" + "</td><td>" + TZ_ZZCJ + "</td></tr>";
			
					//tdArray_WTJ[i] =tdStr;
					if(TZ_KCZY_ZT=="已提交"){
						tdStr=tdStr+tdStr4;
						tdTJ[k]=tdStr;
						k=k+1;
					}else{
						tdStr=tdStr+tdStr5
						tdArray_WTJ[i] =tdStr;
					}
					
				}
			} 
			//如果没有数据
			/*else {
				specialArray[0] = "<tr><td class='kaikeid' style='display: none;'>"
						+ TZ_KAIK_KCH_ID
						+ "</td><td>"
						+ TZ_MK_NAME
						+ "</td><td>"
						+ TZ_KECH_MC
						+ "</br>"
						+ TZ_KECH_EMC
						+ "</td>"
						+ "<td>"
						+ TZ_EM_BGN_DT
						+ "</td><td>"
						+ TZ_EM_END_DT
						+ "</td><td>"
						+ TZ_SKDD
						+ "</td><td>"
						+ TZ_SHKE_JSHI + "</td><td>" + TZ_KECH_ZXF + "</td>";

				specialArray[1] = "<td>" + TZ_ZY_TYPE + "</td><td>"
						+ TZ_ZY_NAMEStr + "</td><td>" + uploadDiv 
					    + "</td><td><div class='red'>" + TZ_KCZY_ZT
						+ "</div>" + "</td>";
				specialArray[3] = "<td>" + TZ_ZY_TYPE + "</td><td>"
						+ TZ_ZY_NAMEStr + "</td><td>" + ZYStr 
					    + "</td><td><div class='red'>" + TZ_KCZY_ZT
						+ "</div>" + "</td>";

				specialArray[2] = "<td>" + TZ_ZZCJ + "</td></tr>";
				if (TZ_KCZY_ZT == "未提交") {
					tdArray_WTJ[tdArray_WTJIndex] = specialArray[0]
							+ specialArray[1] + specialArray[2];
				} else {
					//tdArray[tdArrayIndex] = specialArray[0] + specialArray[3]
					//		+ specialArray[2];
					tdArray_WTJ[tdArray_WTJIndex] = specialArray[0] + specialArray[3]
							+ specialArray[2];
				}
			} */
		}
		var reStr = "";
		//console.log(tdArray_WTJ.length);
		for (var indexe1 = 0; indexe1 < tdArray_WTJ.length; indexe1++) {
			//console.log("++++++++++++++++++++");
			//console.log("++++++++++++++++++++"+tdArray_WTJ);
			//console.log(tdArray_WTJ[indexe1]);
			if (tdArray_WTJ[indexe1] != undefined) {
				reStr += tdArray_WTJ[indexe1];
			}
		}
		//console.log("=========================");
		//console.log(reStr);
		
		//console.log(tdArray.length);
		for (var j = 0; j < tdTJ.length; j++) {
			//console.log("=========================");
			//console.log(tdTJ[j]);
			if (tdTJ[j] != undefined) {
				reStr += tdTJ[j];
			}
		}
		//console.log("长度"+tdTJ.length);
		//console.log("=========================");
		//console.log(reStr);
	}
	return reStr;
}
