select 
	A.TZ_DLZH_ID 
from 
	PS_TZ_AQ_YHXX_TBL A,PS_TZ_REG_USER_T B
where 
	((A.TZ_MOBILE=? and A.TZ_SJBD_BZ='Y') or (LCASE(A.TZ_EMAIL)=? and A.TZ_YXBD_BZ='Y')) 
	and A.TZ_RYLX='ZCYH' 
	and A.OPRID=B.OPRID
	and A.TZ_JG_ID=?
	and B.TZ_SITEI_ID=?