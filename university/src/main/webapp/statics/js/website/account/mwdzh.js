function MM_goToURL() {
    var i, args = MM_goToURL.arguments;
    document.MM_returnValue = false;
    for (i = 0; i < (args.length - 1); i += 2) eval(args[i] + ".location='" + args[i + 1] + "'");
}

function setTab(m, n) {
    var menu = document.getElementById("tab" + m).getElementsByTagName("li");
    var showdiv = $("#content" + m + " .tab1");
    for (i = 0; i < menu.length; i++) {
        menu[i].className = i == n ? "now": "";
        showdiv[i].style.display = i == n ? "block": "none";
    }
}

var jsonValue;

var RegisterTips={
		_blank_eng:"It cannot be blank!",
		_blank_zhs:"必填项!",
		_email_eng:"The email address is incorrect!",
		_email_zhs:"邮箱地址不正确！",
		_phone_eng:"The mobile phone is incorrect!",
		_phone_zhs:"手机号码不正确！",
		_pwd_eng:"Stronger password needed.",
		_int_zhs:"必须为整数",
		_int_eng:"The value must be an integer. "
	}

var email_url = "";
var mobile_url = "";
var isBindEmail = "";
var isBindMobile = "";

function myrefresh() {
    window.location.reload();
}
$(document).ready(function() {
	
	var TipBlank = "";
	var TipEmail = "";
	var TipPhone = "";
	var TipInt = "";
	if ($("#lang").val()=="ENG")
	{
		TipBlank = RegisterTips._blank_eng;
		TipEmail = RegisterTips._email_eng;
		TipPhone = RegisterTips._phone_eng;
		TipInt = RegisterTips._int_eng;
	}else{
		TipBlank = RegisterTips._blank_zhs;
		TipEmail = RegisterTips._email_zhs;
		TipPhone = RegisterTips._phone_zhs;
		TipInt = RegisterTips._int_zhs;
    }
    /*$("#BIRTHDATE").click(function() {
        laydate({
        	elem: '#BIRTHDATE'
        });
    });
	$("#BIRTHDATE").focus(function(){
        document.activeElement.blur();
    });
    selectDate.init({trigger:"#BIRTHDATE",min:"1960/01/01",position:"bottom"});*/
	var calendar = new LCalendar();
	
	var bir = document.getElementById("BIRTHDATE");
	
	if(bir!=null&&bir!=undefined){
		calendar.init({
	        'trigger': '#BIRTHDATE', //标签id
	        'type': 'date', 		//date 调出日期选择 datetime 调出日期时间选择 time 调出时间选择 ym 调出年月选择,
	        'minDate': '1960-01-01', 	//最小日期
	        'maxDate': new Date().getFullYear() + '-' + (new Date().getMonth() + 1) + '-' + new Date().getDate() //最大日期
	    });
	}
    
    
    $.each([$("#TZ_COUNTRY"),$("#TZ_COUNTRY_click"),$("#TZ_SCH_CNAME_Country"),$("#TZ_SCH_CNAME"),$("#TZ_LEN_PROID")],function(i,el){	
		el.focus(function(){
        document.activeElement.blur();
       })
    });
	$.each([$("#TZ_COUNTRY"),$("#TZ_COUNTRY_click"),$("#TZ_SCH_CNAME_Country")],function(i,el){	
		el.click(function(e) { 
			
			$("#ParamCon").val(el.attr("id"));
			var tzParams = '{"ComID":"TZ_COMMON_COM","PageID":"TZ_M_COUNTRY_STD","OperateType":"HTML","comParams":{"orgid":"'+orgId+'","siteId":"'+siteId+'","lang":"ZHS","sen":"2"}}';
			$.ajax({
				type: "post",
				async :false,
				data:{
					tzParams:tzParams
				},
				url: TzUniversityContextPath + "/dispatcher",
				dataType: "html",
				success: function(result){
					$("#searchCountry").html("");
					$("#searchCountry").html(result);
					//$("#body").css("position","fixed");
					//$(".shade").show();
					
					$("#before").hide();
				    $("#searchCountry").fadeIn("slow"); 
                    //loaded ();
				}
			});
		});
	});
    /*$.each([$("#TZ_COUNTRY"), $("#TZ_COUNTRY_click")],
    function(i, el) {
        el.click(function(e) {
            var countryUrl = encodeURI(tzGdWdzhCountryUrl);
            $("#ParamCon").val("TZ_COUNTRY");
            s = layer.open({
                type: 2,
                title: false,
                fixed: false,
                closeBtn: 0,
                shadeClose: false,
                shade: [0.3, '#000', true],
                border: [3, 0.3, '#000', true],
                offset: ['20%', ''],
                area: ['830px', '610px'],
                content: countryUrl
            });            
        });
    });
    $("#TZ_COUNTRY_click").mouseover(function() {
        $("#TZ_COUNTRY_click").css("cursor", "pointer");
    });
    $("#TZ_COUNTRY_click").mouseout(function() {
        $("#TZ_COUNTRY_click").css("cursor", "");
    });
    $("#TZ_SCH_CNAME_Country").click(function(e) {
        $("#ParamCon").val("TZ_SCH_CNAME_Country");
        s = layer.open({
            type: 2,
            title: false,
            fixed: false,
            closeBtn: 0,
            shadeClose: false,
            shade: [0.3, '#000', true],
            border: [3, 0.3, '#000', true],
            offset: ['10%', ''],
            area: ['830px', '720px'],
            content: TzUniversityContextPath + '/dispatcher?tzParams={%22ComID%22:%22TZ_COMMON_COM%22,%22PageID%22:%22TZ_COUNTRY_STD%22,%22OperateType%22:%22HTML%22,%22comParams%22:{%22siteId%22:%22' + $("#siteid").val() + '%22}}'
        });
    });*/
    /*$.each([$("#TZ_SCH_CNAME"), $("#TZ_SCH_CNAME_click")],
    function(i, el) {
        el.click(function(e) {
            $("#ParamValue").val("TZ_SCH_CNAME");
            s = layer.open({
                type: 2,
                title: false,
                fixed: false,
                closeBtn: 0,
                shadeClose: false,
                shade: [0.3, '#000', true],
                border: [3, 0.3, '#000', true],
                offset: ['10%', ''],
                area: ['830px', '720px'],
                content: TzUniversityContextPath + '/dispatcher?tzParams={%22ComID%22:%22TZ_COMMON_COM%22,%22PageID%22:%22TZ_SCHOOL_STD%22,%22OperateType%22:%22HTML%22,%22comParams%22:{%22siteId%22:%22' + $("#siteid").val() + '%22,%22Type%22:%22A%22}}'
            })
        });
    });
    $("#TZ_SCH_CNAME_click").mouseover(function() {
        $("#TZ_SCH_CNAME_click").css("cursor", "pointer");
    });
    $("#TZ_SCH_CNAME_click").mouseout(function() {
        $("#TZ_SCH_CNAME_click").css("cursor", "");
    });
    $.each([$("#TZ_LEN_PROID"), $("#TZ_LEN_PROID_click")],
    function(i, el) {
        el.click(function(e) {
            var _prov_id = "TZ_LEN_PROID";
            var provUrl = encodeURI(tzGdWdzhProvUrl);
            prov = layer.open({
                type: 2,
                title: false,
                fixed: false,
                closeBtn: 0,
                shadeClose: false,
                shade: [0.3, '#000', true],
                border: [3, 0.3, '#000', true],
                offset: ['20%', ''],
                area: ['588px', '300px'],
                content: provUrl
            });
        });
    });
    $("#TZ_LEN_PROID_click").mouseover(function() {
        $("#TZ_LEN_PROID_click").css("cursor", "pointer");
    });
    $("#TZ_LEN_PROID_click").mouseout(function() {
        $("#TZ_LEN_PROID_click").css("cursor", "");
    });*/
	$("#TZ_SCH_CNAME").click(function(e) {
		/*$("#ParamValue").val("TZ_SCH_CNAME");		
		s = $.layer({
			type: 2,
			title: false,
			fix: false,
			closeBtn: false,
			shadeClose: false,
			shade : [0.3 , '#000' , true],
			border : [3 , 0.3 , '#000', true],
			offset: ['50%',''],
			area: ['830px','720px'],
			//iframe: {src: '/tranzvision/colselector_liu.html'}
			iframe: {src: TzUniversityContextPath + '/dispatcher?tzParams={%22ComID%22:%22TZ_COMMON_COM%22,%22PageID%22:%22TZ_SCHOOL_STD%22,%22OperateType%22:%22HTML%22,%22comParams%22:{%22siteId%22:%22'+$("#siteid").val()+'%22,%22Type%22:%22A%22}}'}
		});*/
		$("#ParamCon").val("TZ_SCH_CNAME");
		var tzParams = '{"ComID":"TZ_COMMON_COM","PageID":"TZ_M_SCHOOL_STD","OperateType":"HTML","comParams":{"orgid":"'+orgId+'","siteId":"'+siteId+'","lang":"ZHS","Type":"A"}}';
		$.ajax({
			type: "post",
			async :false,
			data:{
				tzParams:tzParams
			},
			url: TzUniversityContextPath + "/dispatcher",
			dataType: "html",
			success: function(result){
				$("#searchSchool").html("");
				$("#searchSchool").html(result);
				$("#body").css("position","fixed");
				$("#before").hide();
				$("#searchSchool").fadeIn("slow"); 
                 //loaded1 ();
				/*$(".shade").show();
			    $("#searchSchool").show();*/
			}
		});
    });
    $("#TZ_SCH_CNAME_click").mouseover(function() {
	   	$("#TZ_SCH_CNAME_click").css("cursor","pointer");
	});
	$("#TZ_SCH_CNAME_click").mouseout(function() {
	   	$("#TZ_SCH_CNAME_click").css("cursor","");
	});
	$.each([$("#TZ_LEN_PROID"),$("#TZ_LEN_PROID_click")],function(i,el){
		el.click(function(e) { 
			$("#TZ_LEN_PROID").focus(function(){
              document.activeElement.blur();
           });
           $("#TZ_LEN_PROID_click").focus(function(){
              document.activeElement.blur();
           });
			/*var _prov_id = "TZ_LEN_PROID";
			prov = $.layer({
				type: 2,
				title: false,
				fix: false,
				closeBtn: false,
				shadeClose: false,
				shade : [0.3 , '#000' , true],
				border : [3 , 0.3 , '#000', true],
				offset: ['100px',''],
				area: ['588px','300px'],
				iframe: {src: TzUniversityContextPath + '/dispatcher?tzParams={%22ComID%22:%22TZ_COMMON_COM%22,%22PageID%22:%22TZ_PROVINCE_STD%22,%22OperateType%22:%22HTML%22,%22comParams%22:{%22TZ_PROV_ID%22:%22'+_prov_id+'%22,%22siteId%22:%22'+$("#siteid").val()+'%22}}'},
			});*/
			var _prov_id = "TZ_LEN_PROID";
			$("#ParamCon").val(el.attr("id"));
			var tzParams = '{"ComID":"TZ_COMMON_COM","PageID":"TZ_M_PROVINCE_STD","OperateType":"HTML","comParams":{"TZ_PROV_ID":"'+_prov_id+'","siteId":"'+siteId+'"}}';
			$.ajax({
				type: "post",
				async :false,
				data:{
					tzParams:tzParams
				},
				url: TzUniversityContextPath + "/dispatcher",
				dataType: "html",
				success: function(result){
					$("#searchState").html("");
				$("#searchState").html(result);
				$("#searchState").focus(function(){
                     document.activeElement.blur();
                });
				
				
				$("#body").css("position","fixed");
				$("#before").hide();
				$("#searchState").fadeIn("slow"); 
                //loaded1 ();
				/*	$("#searchState").html("");
					$("#searchState").html(result);
					$("#body").css("position","fixed");
					$(".shade").show();
				    $("#searchState").show();*/
				}
			});
		});
    });
    $("#TZ_LEN_PROID_click").mouseover(function() {
	   	$("#TZ_LEN_PROID_click").css("cursor","pointer");
	});
	$("#TZ_LEN_PROID_click").mouseout(function() {
	   	$("#TZ_LEN_PROID_click").css("cursor","");
	});
    $.each([$("#TZ_LEN_CITY"), $("#TZ_LEN_CITY_click")],
    function(i, el) {
        el.click(function(e) {
            var _city_id = "TZ_LEN_CITY";
            var cityUrl = encodeURI(tzGdWdzhCityUrl);
            prov = layer.open({
                type: 2,
                title: false,
                fixed: false,
                closeBtn: 0,
                shadeClose: false,
                shade: [0.3, '#000', true],
                border: [3, 0.3, '#000', true],
                offset: ['20%', ''],
                area: ['588px', '400px'],
                content: cityUrl
            });
        });
    });
    $("#TZ_LEN_CITY_click").mouseover(function() {
        $("#TZ_LEN_CITY_click").css("cursor", "pointer");
    });
    $("#TZ_LEN_CITY_click").mouseout(function() {
        $("#TZ_LEN_CITY_click").css("cursor", "");
    });

    $.ajax({
        type: "post",
        dataType: "json",
        async: false,
        url: tzGdWdzhGetUserInfo,
        success: function(jsonData) {
            jsonValue = jsonData.comContent;
            var data = jsonData.comContent;

            // 个人设置
            for (var key in data) {
                if (key == "TZ_GENDER") {
                    $("input[name='TZ_GENDER'][value='" + data[key] + "']").attr("checked", "checked");
                } else {
                    $("#" + key).val(data[key]);
                    if (key == "TZ_SCH_COUNTRY") {
                        $("#TZ_SCH_CNAME_Country").attr("ccode", data[key]);
                    }
                }
                /*
                $("#" + key).on("blur",function(){
					var val = this.value;
					var fieldId = this.id;
					var fieldRequired = document.getElementById(fieldId).getAttribute("required");
					if(fieldId=="TZ_REALNAME"){//姓名
						if(val !=''){
							if(val.length>1){
								$('#' + fieldId + '_status').html("");
								$('#status_' + fieldId).attr("value", 0);
								$('#' + fieldId + 'Style').addClass("alert_display_none");
							}else{
								$('#' + fieldId + '_status').html("<span>"+TipBlank+"</span>");
								$('#status_' + fieldId).attr("value", 1);
								$('#' + fieldId + 'Style').removeClass("alert_display_none");
							}
					
						}else{
							if(fieldRequired == "Y"){
								//$('#' + fieldId).val("请输入真实名字");
								$('#' + fieldId + '_status').html("<span>"+TipBlank+"</span>");
								$('#status_' + fieldId).attr("value", 1);
								$('#' + fieldId + 'Style').removeClass("alert_display_none");
							}
						}
					}else if(fieldId=="userEmail"){//邮箱
						if(val!=''){
							var patrn = /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
							if (!patrn.test(val)){
							   $('#' + fieldId + '_status').html("<span>"+TipEmail+"</span>");
							   $('#status_' + fieldId).attr("value", 1);
							   $('#' + fieldId + 'Style').removeClass("alert_display_none");
							}else{
								var tzParams = '{"ComID":"TZ_SITE_UTIL_COM","PageID":"TZ_SITE_MAIL_STD","OperateType":"QF","comParams":{"email":"'+val+'","orgid":"'+orgId+'","siteId":"'+$("#siteid").val()+'","lang":"'+$("#lang").val()+'","sen":"1"}}';
								$.ajax({
									type: "get",
									async :false,
									data:{
										tzParams:tzParams
									},
									url: TzUniversityContextPath + "/dispatcher",
									dataType: "json",
									success: function(result){
										if(result.comContent =="success"){
											$('#' + fieldId + '_status').html("");
											$('#status_' + fieldId).attr("value", 0); 
											$('#' + fieldId + 'Style').addClass("alert_display_none");
										}else{
											$('#' + fieldId + '_status').html("<span>"+result.state.errdesc+"</span>");
					   						$('#status_' + fieldId).attr("value", 1);
											$('#' + fieldId + 'Style').removeClass("alert_display_none");
										}
									}
								});
							}
						}else{
							if(fieldRequired == "Y"){
								$('#' + fieldId + '_status').html("<span>"+TipBlank+"</span>");
								$('#status_' + fieldId).attr("value", 1);
								$('#' + fieldId + 'Style').removeClass("alert_display_none");
							}
						}
					}else if(fieldId=="userMoblie"){//手机
						if(val!=''){
							var patrn=/^\d{8}$|^\d{11}$/;
							if (!patrn.test(val)){
								$('#' + fieldId + '_status').html("<span>"+TipPhone+"</span>");
							  	$('#status_' + fieldId).attr("value", 1);
							  	$('#' + fieldId + 'Style').removeClass("alert_display_none");
							}else{
								var tzParams = '{"ComID":"TZ_SITE_UTIL_COM","PageID":"TZ_SITE_SMS_STD","OperateType":"QF","comParams":{"phone":"'+val+'","orgid":"'+orgId+'","siteId":"'+$("#siteid").val()+'","lang":"'+$("#lang").val()+'","sen":"1"}}';
								$.ajax({
									type: "get",
									async :false,
									data:{
										tzParams:tzParams
									},
									url: TzUniversityContextPath + "/dispatcher",
									dataType: "json",
									success: function(result){
										if(result.comContent =="success"){
											$('#' + fieldId + '_status').html("");
											$('#status_' + fieldId).attr("value", 0); 
											$('#' + fieldId + 'Style').addClass("alert_display_none");
										}else{
											$('#' + fieldId + '_status').html("<span>"+result.state.errdesc+"</span>");
											$('#status_' + fieldId).attr("value", 1);
											$('#' + fieldId + 'Style').removeClass("alert_display_none");
										}
									}
								});
							}
						}else{
							if(fieldRequired == "Y"){
								$('#' + fieldId + '_status').html("<span>"+TipBlank+"</span>");
								$('#status_' + fieldId).attr("value", 1);
								$('#' + fieldId + 'Style').removeClass("alert_display_none");
							}
						}
					}else{//其他
						if(fieldRequired == "Y"){
							if(val != ""){
								$('#' + fieldId + 'Style').addClass("alert_display_none");
							}else{
								$('#' + fieldId + '_status').html("<span>"+TipBlank+"</span>");
								$('#' + fieldId + 'Style').removeClass("alert_display_none");
							}
						}
					}
				})*/
            }
            // 通知设置
            if (data.isEmail == "Y") {
                $("#userEmailChecked").removeClass("toggle-off");
                $("#toggleOption1").attr("checked", "checked");
                $("#toggleOption2").removeAttr("checked");
            } else {
                $("#toggleOption2").attr("checked", "checked");
                $("#toggleOption1").removeAttr("checked");
            }
            if (data.isMobile == "Y") {
                $("#userMoblieChecked").removeClass("toggle-off");
                $("#toggleOption3").attr("checked", "checked");
                $("#toggleOption4").removeAttr("checked");
            } else {
                $("#toggleOption4").attr("checked", "checked");
                $("#toggleOption3").removeAttr("checked");
            }

            // 账号绑定

            isBindEmail = data.isBindEmail;
            isBindMobile = data.isBindMobile;

            email_url = data.bindEmailURL;
            mobile_url = data.bindMobileURL;

        }
    });
    // 保存个人信息
    $("#saveper").click(function() {
    	var _statusFlg="";
        var date1 = "";
        var userInfoJson = {};
        for (var key in jsonValue) {            
     
			//校验通过后值特殊处理
			if (key == "TZ_GENDER") {
                userInfoJson[key] = $("input[name='TZ_GENDER']:checked").val();
            } else {
                userInfoJson[key] = $("#" + key).val();
            }
            if (key == "TZ_SCH_CNAME_Country") {
                var countryCode = $("#TZ_SCH_CNAME_Country").attr("ccode");
                if (countryCode != null && countryCode != undefined) {
                    $("#TZ_SCH_COUNTRY").val(countryCode);
                    userInfoJson["TZ_SCH_COUNTRY"] = countryCode;
                }
            }
        }
        
        if(_statusFlg=="error"){
        	return false;
        }
        userInfoJson["siteId"] = siteId; 
        userInfoJson["jgid"] = orgId;
        userInfoJson["lang"] = "ZHS";

        var tzParams = '{"ComID":"TZ_GD_ZS_USERMNG","PageID":"TZ_ZS_USERMNG_STD","OperateType":"SAVEUSERINFO","comParams":' + JSON.stringify(userInfoJson) + '}';

        $.ajax({
            type: "post",
            dataType: "json",
            async: false,
            data: {
                "tzParams": tzParams
            },
            url: tzGdWdzhSaveActivateUrl,
            success: function(data) {
                layer.open({
                    content: data.comContent.success
                    ,skin: 'msg'
                    ,time: 2 //2秒后自动关闭
                  });
            }
        });
    });
    // 保存通知设置
    $("#saveRemind").click(function() {

        var lang = $("#lang").val();
        var isEmail = $("#toggleOption1").prop("checked");
        var isMoblie = $("#toggleOption3").prop("checked");

        var notice = {
            "isEmail": isEmail,
            "isMoblie": isMoblie,
            "lang": lang
        };

        var tzParams = '{"ComID":"TZ_GD_ZS_USERMNG","PageID":"TZ_ZS_USERMNG_STD","OperateType":"NOTICE","comParams":' + JSON.stringify(notice) + '}';

        $.ajax({
            type: "post",
            dataType: "json",
            async: false,
            data: {
                "tzParams": tzParams
            },
            url: tzGdWdzhSaveRemind,
            success: function(data) {
                //alert(data.comContent.success);
                layer.msg(data.comContent.success, {
                    time: 1000
                });
            }
        });
    });
    
    $("#btnpass").click(function() {
    	var $oldPass = $("#oldpass").val();
		var $newPass = $("#newpass").val();
		var $cfPass = $("#cfpass").val();

		if ($oldPass.length < 1) {
			var pass = document.getElementById("oldpass");

			$(".login_tips").html(tzGdFixpwdOldPass + " " + tzGdFixpwdBlankTips);
			$("#oldpassTip").show();
			return false;
		}else{
			$(".login_tips").empty();
			$("#oldpassTip").hide();
		}

		if ($newPass.length < 1) {
			var pass = document.getElementById("newpass");
			$(".login_tips").html(tzGdFixpwdNewPass + " " + tzGdFixpwdBlankTips);
			$("#newpassTip").show();
			return false;
		}else{
			$(".login_tips").empty();
			$("#newpassTip").hide();
		}

		if ($cfPass.length < 1) {
			var pass = document.getElementById("cfpass");
			$(".login_tips").html(tzGdFixpwdConfPass + " " + tzGdFixpwdBlankTips);
			$("#cfpassTip").show();
			return false;
		}else{
			$(".login_tips").empty();
			$("#cfpassTip").hide();
		}
		
		if ($newPass != $cfPass) {
			var pass = document.getElementById("cfpass");
			$(".login_tips").html(tzGdFixpwdNotSameTips);
			$("#cfpassTip").show();
			return false;
		}else{
			$(".login_tips").empty();
			$("#cfpassTip").hide();
		}
		
		var pwd = {
			"oldPass" : $oldPass,
			"newPass" : $newPass,
			"lang" : "ZHS"
		};

		$("#login_tips").html('');
		$("#messTip").hide();
		var tzParams = '{"ComID":"TZ_GD_ZS_USERMNG","PageID":"TZ_ZS_USERMNG_STD","OperateType":"PWD","comParams":'
					+ JSON.stringify(pwd) + '}';
		$.ajax({
			type : "POST",
			dataType : "json",
			url : tzGdFixpwdUrl,
			data : {
				"tzParams" : tzParams
			},
			success : function(jsondata) {
				$("#oldpass").val("");
				$("#newpass").val("");
				$("#cfpass").val("");
				layer.open({
                	content:jsondata.comContent.success,
                	skin:'msg',
                    time: 2
                });
			}
		});
		return false;
	});
})
// 绑定/解绑邮箱
function changeBindEmail() {
    var lang = $("#lang").val();
    var bindOrUnbindEmail = {
        "lang": lang,
        "BIND": isBindEmail
    };
    var tzParams = '{"ComID":"TZ_GD_ZS_USERMNG","PageID":"TZ_ZS_USERMNG_STD","OperateType":"EMAIL","comParams":' + JSON.stringify(bindOrUnbindEmail) + '}';

    $.ajax({
        type: "post",
        dataType: "json",
        async: false,
        url: email_url,
        data: {
            "tzParams": tzParams
        },
        success: function(data) {
            if (data.comContent.success == "Y") {
                email_url = data.comContent.url;
                isBindEmail = data.comContent.BIND;
                if (data.comContent.BIND == "Y") {
                    $("#bind_Email").html(tzGdWdzhReleaseBind);
                    $("#BindEmail").html(data.comContent.email);
                    $("#change_Email").show();
                    $("#bind_Email").attr("emailBindState", "Y");
                } else {
                    $("#BindEmail").html(tzGdWdzhAbsence);
                    $("#bind_Email").html(tzGdWdzhDoBind);
                    $("#change_Email").hide();
                    $("#bind_Email").attr("emailBindState", "N");
                }
                //alert(tzGdWdzhPassSucTips);
                layer.msg(tzGdWdzhPassSucTips, {
                    time: 1000
                });
            } else {
                //alert(data.comContent.errorDesc);
                layer.msg(data.comContent.errorDesc, {
                    time: 1000
                });
            }
        }
    });
}

// 绑定/解绑手机
function changeBindMobile() {
    var lang = $("#lang").val();
    var bindOrUnbindMobile = {
        "lang": lang,
        "BIND": isBindMobile
    };
    var tzParams = '{"ComID":"TZ_GD_ZS_USERMNG","PageID":"TZ_ZS_USERMNG_STD","OperateType":"MOBILE","comParams":' + JSON.stringify(bindOrUnbindMobile) + '}';

    $.ajax({
        type: "post",
        dataType: "json",
        async: false,
        url: mobile_url,
        data: {
            "tzParams": tzParams
        },
        success: function(data) {
            if (data.comContent.success == "Y") {
                mobile_url = data.comContent.url;
                isBindMobile = data.comContent.BIND;
                if (data.comContent.BIND == "Y") {
                    $("#bind_Mobile").html(tzGdWdzhReleaseBind);
                } else {
                    $("#BindMobile").html(tzGdWdzhAbsence);
                    $("#bind_Mobile").html(tzGdWdzhDoBind).attr("phonebindstate", "N");
                    $("#change_Mobile").hide();
                }
                //alert(tzGdWdzhPassSucTips);
                layer.msg(tzGdWdzhPassSucTips, {
                    time: 1000
                });
            } else {
                //alert(data.comContent.errorDesc);
                layer.msg(data.comContent.errorDesc, {
                    time: 1000
                });
            }
        }
    });
}

var up;
function openUpload() {
    var photoUrl = encodeURI(tzGdWdzhPhotoUrl);
    up = $.layer({
        type: 2,
        title: false,
        fix: false,
        closeBtn: 2,
        shadeClose: false,
        shade: [0.3, '#000', true],

        border: [3, 0.3, '#000', true],
        offset: ['50%', ''],
        area: ['840px', '610px'],
        iframe: {
            src: photoUrl
        }
    });
}
function changeEmail() {
    changeEmailUrlParams = '{"ComID":"TZ_SITE_UTIL_COM","PageID":"TZ_SITE_ENROLL_STD","OperateType":"HTML","comParams": {"siteid":"' + $("#siteid").val() + '","orgid":"' + orgId + '","lang":"' + $("#lang").val() + '","sen":"6"}}';
    changeEmailUrl = tzGdWdzhChangeEmailUrl + "?tzParams=" + encodeURI(changeEmailUrlParams);
 
    layer.open({
        type: 2,
        title: false,
        fixed: false,
        closeBtn: 0,
        shadeClose: false,
        shade: [0.3, '#000', true],
        border: [3, 0.3, '#000', true],
        offset: ['20%', ''],
        area: ['589px', '306px'],
        content: changeEmailUrl
    })
}

function changeMobile() {
    var changeMobileUrl = encodeURI(tzGdWdzhChangeMobileUrl);

    layer.open({
        type: 2,
        title: false,
        fixed: false,
        closeBtn: 0,
        shadeClose: false,
        shade: [0.3, '#000', true],
        border: [3, 0.3, '#000', true],
        offset: ['20%', ''],
        area: ['589px', '306px'],
        content: changeMobileUrl
    })
}

function bindMobile(el) {
    var attrState = $(el).attr("phoneBindState");
    if (attrState == "Y") {
        changeBindMobile();
        // myrefresh();
        //setTab(1, 3);
    } else {
        changeMobile();
    }
}

function bindEmail(el) {
    //changeBindEmail();
    var attrState = $(el).attr("emailBindState");
    if (attrState == "Y") {
        changeBindEmail();
        // myrefresh();
        // setTab(1,3); 
    } else {
        changeEmail();
    }

}