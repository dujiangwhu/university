select 
	TZ_AREA_TYPE_ID 
from 
	PS_TZ_SITEI_AREA_T 
where 
	TZ_SITEI_ID=? 
	and TZ_AREA_ID=? 
	and TZ_AREA_STATE='Y'