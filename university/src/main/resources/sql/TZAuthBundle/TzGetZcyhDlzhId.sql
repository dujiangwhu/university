select 
	A.TZ_DLZH_ID 
from 
	PS_TZ_AQ_YHXX_TBL A
where 
	A.TZ_MOBILE=? 
	and A.TZ_RYLX='PXXY' 
	and A.TZ_JG_ID=?
	and B.TZ_SITEI_ID=?