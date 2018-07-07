SELECT 
	A.TZ_PWEI_OPRID,
	A.TZ_SCORE_INS_ID,
    F.TZ_DLZH_ID,
    B.TZ_SUBMIT_YN
FROM 
	PS_TZ_CP_PW_KS_TBL A
	LEFT JOIN PS_TZ_KSCLPSLS_TBL B
	ON A.TZ_CLASS_ID=B.TZ_CLASS_ID
	AND A.TZ_APPLY_PC_ID=B.TZ_APPLY_PC_ID
	AND A.TZ_APP_INS_ID=B.TZ_APP_INS_ID
	AND A.TZ_PWEI_OPRID=B.TZ_PWEI_OPRID
	AND B.TZ_CLPS_LUNC=?
 	AND B.TZ_SUBMIT_YN<>'C'
	LEFT JOIN PS_TZ_CLPS_GZ_TBL C
	ON A.TZ_CLASS_ID=C.TZ_CLASS_ID
	AND B.TZ_CLPS_LUNC = C.TZ_DQPY_LUNC
	AND A.TZ_APPLY_PC_ID=C.TZ_APPLY_PC_ID
    LEFT JOIN PS_TZ_AQ_YHXX_TBL F
    ON A.TZ_PWEI_OPRID=F.OPRID
WHERE 
	A.TZ_CLASS_ID = ? 
	AND A.TZ_APPLY_PC_ID = ?
	AND A.TZ_APP_INS_ID = ?