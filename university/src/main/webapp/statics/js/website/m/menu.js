var urlBegin= TzUniversityContextPath + "/dispatcher";

function JumpToColu(siteId,menuId,opentype){

	if(opentype=="index"){
		window.location = TzUniversityContextPath + "/dispatcher?classid=mIndex&siteId=" + siteId;
	} else if (opentype=="_blank") {
		window.open(urlBegin+'?classid=askMenu&siteId='+siteId+'&menuId='+menuId+'&oprate=R',"_blank");
	}else{
		location.href =urlBegin+'?classid=askMenu&siteId='+siteId+'&menuId='+menuId+'&oprate=R';
	}
}