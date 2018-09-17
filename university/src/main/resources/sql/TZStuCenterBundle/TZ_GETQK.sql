SELECT
	A.OPRID,
	A.TZ_SCHEDULE_ID,
	B.TZ_COURSE_ID,
	date_format(
		B.TZ_CLASS_START_TIME,
		'%Y-%m-%d %H:%i:%S'
	) AS TZ_CLASS_START_TIME,
	date_format(
		B.TZ_CLASS_END_TIME,
		'%Y-%m-%d %H:%i:%S'
	) AS TZ_CLASS_END_TIME,
	D.TZ_COURSE_TYPE_NAME,
	E.TZ_COURSE_NAME,
	C.TZ_REALNAME,
	A.TZ_APP_STATUS
FROM
	PX_STU_APP_COURSE_T A,
	PX_TEA_SCHEDULE_T B
LEFT JOIN PS_TZ_AQ_YHXX_TBL C ON B.OPRID = C.OPRID
LEFT JOIN PX_COURSE_TYPE_T D ON B.TZ_COURSE_TYPE_ID = D.TZ_COURSE_TYPE_ID
LEFT JOIN PX_COURSE_T E ON B.TZ_COURSE_ID = E.TZ_COURSE_ID
WHERE
	A.TZ_SCHEDULE_ID = B.TZ_SCHEDULE_ID
AND A.OPRID =?
AND A.TZ_APP_STATUS =?
AND B.TZ_CLASS_END_TIME < NOW()
ORDER BY
	B.TZ_CLASS_START_TIME DESC