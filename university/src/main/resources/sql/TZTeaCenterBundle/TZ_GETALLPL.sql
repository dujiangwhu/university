SELECT
	B.TZ_REALNAME,
	A.TZ_REVIEW_TYPE,
	date_format(
		A.TZ_REVIEW_TIME,
		'%Y-%m-%d %H:%i:%S'
	) AS TZ_REVIEW_TIME,
	A.TZ_REVIEW_DESC
FROM
	PX_STU_REVIEW_TEA_T A
LEFT JOIN PS_TZ_AQ_YHXX_TBL B ON B.OPRID = A.STU_OPRID
WHERE
A.TEA_OPRID =?
AND A.TZ_REVIEW_STATUS=0
ORDER BY
	A.TZ_REVIEW_STATUS DESC