function selectRadioClassId(num){
	$("input[name='classidradio']").eq(num).click();
}

//报名人选择班级;
//报名人选择可以报名的班级;
function viewClass(siteId,language,viewType){
	 //var siteId = TZ_GD_LOGIN_SITEI_ID;
	 var tzParams = '{"ComID":"TZ_ZLSQ_JD_COM","PageID":"TZ_ZLSQ_JD_PAGE","OperateType":"HTML","comParams":{"viewType":"' + viewType+ '","siteId":"' + siteId+ '","language":"' + language+ '","oprate":"R"}}';

	 $.ajax({
 					type:"POST",
 					url: urlBegin,
 					data:{
 						tzParams:tzParams
 					},
 					success:function(response){
 						$("div#showWindDiv").html(response);
 						$("div#showWindDiv").show();
 	
 						$("span.close").click(function(){
 							$("div#showWindDiv").hide();
 						});
 					
 					},
	 		    	failure: function () {
	 				  		
	 		    	}    
 			});
}


//选择新闻活动范围弹出框显示;
function selectNewsProject(siteid){
	var tzParams = '{"ComID":"TZ_ZLSQ_JD_COM","PageID":"TZ_ZLSQ_JD_PAGE","OperateType":"HTML","comParams":{"viewType":"SELECTPROJECT","siteid":"' + siteid+ '","oprate":"R"}}';

	 $.ajax({
				type:"POST",
				url: urlBegin,
				data:{
					tzParams:tzParams
				},
				success:function(response){
					$("div#showWindPerDiv").html(response);
					$("div#showWindPerDiv").show();

					$("span.close").click(function(){
						$("div#showWindPerDiv").hide();
					});
				
				},
		    	failure: function () {
				  		
		    	}    
		});
}

//选择新闻活动范围(全选和取消全选);
function selectAllProject(){
	var code_Values = document.getElementsByName("projectCheckbox"); 
	var i = 0;
	if ($("#selectAllProject").get(0).checked){
		for (i = 0; i < code_Values.length; i++) {  
			code_Values[i].checked = true;    
		}  
	}else{
		for (i = 0; i < code_Values.length; i++) { 
			code_Values[i].checked = false;    
		} 
	}
}

//选择新闻活动范围(确定);
function addSelectPrj(){
	var selectPrjs = "";
	var code_Values = document.getElementsByName("projectCheckbox"); 
	var i = 0;
	for (i = 0; i < code_Values.length; i++) {  
		if(code_Values[i].checked){
			if(selectPrjs == ""){
				selectPrjs = code_Values[i].value;
			}else{
				selectPrjs = selectPrjs + ";" + code_Values[i].value;
			}
		}
	}
	
	var tzParams = '{"ComID":"TZ_ZLSQ_JD_COM","PageID":"TZ_ZLSQ_JD_PAGE","OperateType":"HTML","comParams":{"viewType":"ADDSELECTPROJECT","selectPrjs":"' + selectPrjs+ '","oprate":"R"}}';

	 $.ajax({
		type:"POST",
		url: urlBegin,
		data:{
			tzParams:tzParams
		},
		success:function(response){
			iniArea();
		},
		failure: function () {
				  		
		}    
	});
	 
	$("div#showWindPerDiv").hide();
}

//选择新闻活动范围(取消);
function cancleSelectPrj(){
	$("div#showWindPerDiv").hide();
}

//查看报名进度;
function viewJd(classId, instanceId,language,viewType){
	var tzParams = '{"ComID":"TZ_ZLSQ_JD_COM","PageID":"TZ_ZLSQ_JD_PAGE","OperateType":"HTML","comParams":{"bmClassId":"' + classId + '","instanceId":"' + instanceId+ '","viewType":"' + viewType+ '","language":"' + language+ '","oprate":"R"}}';
    $.ajax({
		type:"POST",
		url:  TzUniversityContextPath+"/dispatcher",
		data:{
				tzParams:tzParams
		},
		success:function(response){
			$("div#showWindDiv").html(response);
			$("div#showWindDiv").show();
	
			$("span.close").click(function(){
				$("div#showWindDiv").hide();
			});
					
		},
		failure: function () {
				  		
		}    
	});
}



$(document).ready(function(){
	$("#weixin").hover(function(){
	      $(this).next("img").toggle();
	    });
	if ($(".main_mid").height()>760){
		$("#letf_menu").height($(".main_mid").height());
	}else{
		$("#letf_menu").height(760);
	}
	
	$("#jgid").val(TZ_GD_LOGIN_SITEI_ORG_CODE);
	$("#siteid").val(TZ_GD_LOGIN_SITEI_ID);
	$("#operator").val(getOperatorType());

	LoadHeader($("#jgid").val(),$("#siteid").val(),$("#operator").val());
	LoadFooter($("#jgid").val(),$("#siteid").val(),$("#operator").val());
	LoadMenu($("#jgid").val(),$("#siteid").val(),"");
	LoadWelcome($("#jgid").val(),$("#siteid").val(),$("#operator").val());
	iniArea();
	getPerInfCard();
	
	//加载报名中心;
	var siteid = $("#siteid").val();
	var oprate = $("#operator").val();

	var tzParams = '{"ComID":"TZ_APPLY_CENTER_COM","PageID":"TZ_APPLY_CENT_PAGE","OperateType":"HTML","comParams":{"siteId":"'+siteid +'","oprate":"'+oprate+'"}}';
	
	
	ajaxRequestHtml(tzParams,function(response){
		$('.applicationCenter').prop('outerHTML', response);
		 
		$(".active").parent().parent().parent().next("table").find(".index_bd").hide();
		$(".index-bm table tr.index_hd td:first-child").click(function(){
		
			$(this).toggleClass("active").parent().parent().parent().next("table").find(".index_bd").slideToggle();
			if($(this).hasClass("active")){
				 $(this).find("i").removeClass("application_shrink");
				 $(this).find("i").addClass("application_expand");
		    
			}else{
				$(this).find("i").removeClass("application_expand");
				$(this).find("i").addClass("application_shrink");
			}
		})
	});
//	$.ajax({
//		type:"POST",
//		url: TzUniversityContextPath+"/dispatcher",
//		data:{
//			tzParams:tzParams
//		},
//		success:function(response){
//			$('.applicationCenter').prop('outerHTML', response);
//					 
//			$(".active").parent().parent().parent().next("table").find(".index_bd").hide();
//			$(".index-bm table tr.index_hd td:first-child").click(function(){
//	        
//				$(this).toggleClass("active").parent().parent().parent().next("table").find(".index_bd").slideToggle();
//				if($(this).hasClass("active")){
//	         		 $(this).find("i").removeClass("application_shrink");
//	         		 $(this).find("i").addClass("application_expand");
//	             
//				}else{
//					$(this).find("i").removeClass("application_expand");
//					$(this).find("i").addClass("application_shrink");
//				}
//			})
//		},
//		failure: function () {
//		  	
//		} 
//		
//	});
	//end 报名中心;
});