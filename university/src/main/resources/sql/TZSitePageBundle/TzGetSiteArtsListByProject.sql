select 
	PT2.TZ_COLU_ID,
    PT3.TZ_ART_ID,
    PT3.TZ_ART_TITLE, 
    PT2.TZ_ART_NEWS_DT
from 
	PS_TZ_SITEI_COLU_T PT1, 
    PS_TZ_LM_NR_GL_T PT2, 
    PS_TZ_ART_REC_TBL PT3 
where 
	PT1.TZ_SITEI_ID = PT2.TZ_SITE_ID 
    and PT1.TZ_COLU_ID = PT2.TZ_COLU_ID 
    and PT2.TZ_ART_ID = PT3.TZ_ART_ID 
    and PT2.TZ_ART_PUB_STATE = 'Y' 
    and PT1.TZ_SITEI_ID = ? 
    and PT1.TZ_COLU_ID = ? 
    and (PT3.TZ_PROJECT_LIMIT<>'B' 
		OR (PT3.TZ_PROJECT_LIMIT = 'B' AND EXISTS 
			(
				SELECT 
					* 
				from PS_TZ_ART_PRJ_T ARTPRJ
                    WHERE ARTPRJ.TZ_ART_ID=PT3.TZ_ART_ID
					AND (
						ARTPRJ.TZ_PRJ_ID IN (SELECT NEWS.TZ_PRJ_ID from PS_SHOW_PRJ_NEWS_T NEWS WHERE NEWS.OPRID=?)
                        OR ARTPRJ.TZ_PRJ_ID IN (
							SELECT 
								A.TZ_PRJ_ID
							from  PS_TZ_CLASS_INF_T A,PS_TZ_FORM_WRK_T B
							  where A.TZ_CLASS_ID=B.TZ_CLASS_ID AND B.OPRID=?
							  AND A.TZ_IS_APP_OPEN='Y' and 
							  A.TZ_APP_START_DT IS NOT NULL AND 
							  A.TZ_APP_START_TM IS NOT NULL AND 
							  A.TZ_APP_END_DT IS NOT NULL AND 
							  A.TZ_APP_END_TM IS NOT NULL AND 
							  str_to_date(concat(DATE_FORMAT(A.TZ_APP_START_DT,'%Y/%m/%d'),' ',  DATE_FORMAT(A.TZ_APP_START_TM,'%H:%i'),':00'),'%Y/%m/%d %H:%i:%s') <= now() 
							  AND str_to_date(concat(DATE_FORMAT(A.TZ_APP_END_DT,'%Y/%m/%d'),' ',  DATE_FORMAT(A.TZ_APP_END_TM,'%H:%i'),':59'),'%Y/%m/%d %H:%i:%s') >= now()
                        )
					)
            )
		)
	)
order by 
	PT2.TZ_MAX_ZD_SEQ desc, 
	PT2.TZ_ART_NEWS_DT desc
limit ?,?