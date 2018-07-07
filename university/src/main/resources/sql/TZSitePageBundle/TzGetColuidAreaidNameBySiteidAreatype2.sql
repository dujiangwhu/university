select 
	TZ_AREA_ID,
	TZ_COLU_ID,
	TZ_AREA_NAME,
	TZ_SHOW_MOBILE_FLG,
	TZ_SHOWM_ORDER
from 
	PS_TZ_SITEI_AREA_T PT1,
	PS_TZ_SITEI_ATYP_T PT2 
where 
	PT1.TZ_SITEI_ID=PT2.TZ_SITEI_ID 
	and PT1.TZ_AREA_TYPE_ID=PT2.TZ_AREA_TYPE_ID 
	and PT1.TZ_SITEI_ID=? 
	and PT2.TZ_AREA_TYPE=?