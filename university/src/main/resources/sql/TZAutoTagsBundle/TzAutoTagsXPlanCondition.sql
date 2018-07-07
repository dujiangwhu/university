SELECT 
    A.TZ_APP_INS_ID, YEAR(B.TZ_RX_DT) TZ_RX_YEAR
FROM
    PS_TZ_FORM_WRK_T A
        JOIN
    PS_TZ_CLASS_INF_T B ON (A.TZ_CLASS_ID = B.TZ_CLASS_ID)
        JOIN
    PS_TZ_REG_USER_T C ON (A.OPRID = C.OPRID)
        JOIN
    PS_TZ_XMSJG_DR_T M ON (A.TZ_MSH_ID = M.TZ_MSH_ID)
WHERE
    EXISTS( SELECT 
            'Y'
        FROM
            PS_TZ_PRJ_INF_T P
        WHERE
            P.TZ_PRJ_ID = B.TZ_PRJ_ID
                AND P.TZ_PRJ_TYPE_ID = ?)
        AND NATIONAL_ID = ?
        AND TZ_MSJG = '通过'
        AND TZ_RX_DT IS NOT NULL