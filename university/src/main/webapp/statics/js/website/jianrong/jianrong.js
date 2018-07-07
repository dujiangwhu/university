$(document).ready(function(){
	    var agent=navigator.userAgent.toLowerCase();
		var borswerName =getBorswerName(agent);
	
		var b=agent.substring(agent.indexOf(borswerName),agent.length);
	
		//把‘/’也去掉,版本中的小数去掉
		var v=Number(b.substring(borswerName.length+1,b.indexOf(".")));
		console.log("borswerName:"+borswerName+" v:"+v)
		if(borswerName=="chrome"&&(v==47||v>47)||borswerName=="firefox"&&(v==49||v>49)||borswerName=="ie"&&(v==11||v>11)||borswerName=="safari"){
			$("#tipBox").hide();
		}else{
			$("#tipBox").show()
		}
		//$("#tipBox").show()
	});
	
	function getBorswerName(userAgent){
		if (userAgent.indexOf("opera")>-1) {
			return "opera"
		} 
		else if (userAgent.indexOf("firefox") > -1) {
			return "firefox";
		} 
		else if (userAgent.indexOf("chrome") > -1){
			return "chrome";
		}
		else if (userAgent.indexOf("safari") > -1) {
			return "safari";
		} 
		else if (userAgent.indexOf("compatible") > -1 && userAgent.indexOf("msie") > -1 ) {
			return "ie";
		}
	}
