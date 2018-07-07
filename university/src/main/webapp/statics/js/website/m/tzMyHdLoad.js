//活动列表
var zlzqNum = 0;

function getNotices(siteid,columnId,pagenum){
	$('.viewport-adaptive').dropload({
		scrollArea: window,
		loadDownFn: function(me) {
			$.ajax({
				type: 'GET',
				data:{
					"tzParams": '{"ComID":"TZ_M_WEB_INDEX_COM","PageID":"TZ_M_MYHD_STD","OperateType":"LOADNOTICES","comParams":{"siteId": "'+ siteid +'","columnId": "' + columnId + '","pagenum": '+pagenum+'}}'
				},
				url: TzUniversityContextPath+"/dispatcher",
				dataType: 'json',
				success: function(resultJson) {	
					pagenum=pagenum+1;
					var resultNum = resultJson.comContent.resultNum;
					if(resultNum > 0){
						zlzqNum = zlzqNum + resultNum;
						// 插入数据到页面，放到最后面
						$('.ziliao').append(resultJson.comContent.result);
					}else{
						 // 锁定
                        me.lock();
                        // 无数据
                        me.noData();
					}
					
					me.resetload();
					if(zlzqNum > 0){
						$('.dropload-noData').html("数据已全部加载");
					}
					
				},
				error: function(xhr, type) {
					alert('数据加载失败!');
					me.resetload();
				}
			});
		}
	});
	
}


function tzCancleBm(actId,bmrId){
	$.ajax({
		type: 'GET',
		data:{
			"tzParams": '{"ComID":"TZ_HD_MOBILE_COM","PageID":"TZ_HD_DETAILS_STD","OperateType":"EJSON","comParams":{"actId": "'+ actId +'","bmrId": "' + bmrId + '"}}'
		},
		url: TzUniversityContextPath+"/dispatcher",
		dataType: 'json',
		success: function(resp) {	
			var canRes = resp.comContent;
			var resultDesc = canRes.resultDesc;
			if(canRes.result == "0"){
				//撤销报名成功
				alert(resultDesc);	
				window.location.reload(true);
			}else{
				alert(resultDesc);	
			}
		},
		error: function(xhr, type) {
			alert('数据加载失败!');
			me.resetload();
		}
	});
}