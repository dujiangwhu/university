select 
	a.TZ_ATTACHSYSFILENA 
from 
	PS_TZ_ART_PIC_T a, 
	PS_TZ_ART_TPJ_T b 
where 
	a.TZ_ART_ID=? 
	and a.TZ_ATTACHSYSFILENA=b.TZ_ATTACHSYSFILENA