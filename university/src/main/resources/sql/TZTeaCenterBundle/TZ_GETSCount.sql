SELECT
	count(A.TZ_CHANGE_ID)
FROM
	PK_TES_INTEGRAL_CHANGE_T A
WHERE
	A.OPRID = ?
	AND A.TZ_CHANGE>0