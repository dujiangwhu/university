select 
	TZ_COLU_ID,
	TZ_AREA_NAME,
	TZ_SHOW_MOBILE_FLG,
	TZ_SHOWM_ORDER
from 
	PS_TZ_SITEI_AREA_T PT1 
where 
	TZ_SITEI_ID=? 
	and TZ_AREA_ID=? 
	and TZ_AREA_STATE='Y'