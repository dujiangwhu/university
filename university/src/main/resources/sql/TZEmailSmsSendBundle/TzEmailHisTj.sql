select `A`.`TZ_EML_SMS_TASK_ID` AS `TZ_EML_SMS_TASK_ID`,`A`.`TZ_JG_ID` AS `TZ_JG_ID`,`A`.`TZ_TMPL_ID` AS `TZ_TMPL_ID`,
`A`.`TZ_RWZX_ZT` AS `TZ_RWZX_ZT`,(select `PS_TZ_PT_ZHZXX_TBL`.`TZ_ZHZ_DMS` from `PS_TZ_PT_ZHZXX_TBL` 
where ((`PS_TZ_PT_ZHZXX_TBL`.`TZ_ZHZJH_ID` = 'TZ_RWZX_ZT') and (`PS_TZ_PT_ZHZXX_TBL`.`TZ_ZHZ_ID` = `A`.`TZ_RWZX_ZT`) 
and (`PS_TZ_PT_ZHZXX_TBL`.`TZ_EFF_STATUS` = 'A'))) AS `TZ_RWZX_ZT_DESC`,
(select count(1) from `PS_TZ_AUDCYUAN_T` where (`PS_TZ_AUDCYUAN_T`.`TZ_AUDIENCE_ID` = `A`.`TZ_AUDIENCE_ID`)) 
AS `TZ_SEND_COUNT`,(select count(1) from `PS_TZ_YJFSLSHI_TBL` where 
((`PS_TZ_YJFSLSHI_TBL`.`TZ_EML_SMS_TASK_ID` = `A`.`TZ_EML_SMS_TASK_ID`) and (`PS_TZ_YJFSLSHI_TBL`.`TZ_FS_ZT` = 'SUC'))) 
AS `TZ_SEND_SUC_COUNT`,(select count(1) from `PS_TZ_YJFSLSHI_TBL` 
where ((`PS_TZ_YJFSLSHI_TBL`.`TZ_EML_SMS_TASK_ID` = `A`.`TZ_EML_SMS_TASK_ID`) 
and (`PS_TZ_YJFSLSHI_TBL`.`TZ_FS_ZT` = 'FAIL'))) AS `TZ_SEND_FAIL_COUNT`,
(select count(1) from `PS_TZ_YJFSLSHI_TBL` 
where ((`PS_TZ_YJFSLSHI_TBL`.`TZ_EML_SMS_TASK_ID` = `A`.`TZ_EML_SMS_TASK_ID`) and 
(`PS_TZ_YJFSLSHI_TBL`.`TZ_FS_ZT` = 'RPT'))) AS `TZ_SEND_RPT_COUNT`,date_format(`A`.`TZ_RWTJ_DT`,'%Y-%m-%d %H:%i:%s') 
AS `TZ_RWZX_DT_STR`,`B`.`TZ_REALNAME` AS `TZ_REALNAME` from `PS_TZ_DXYJFSRW_TBL` `A` , `PS_TZ_AQ_YHXX_TBL` `B` 
where `A`.`TZ_TASK_LX` = 'MAL' AND `A`.`ROW_ADDED_OPRID` = `B`.`OPRID` and A.TZ_JG_ID=? AND A.TZ_TMPL_ID=?  order by `A`.`TZ_RWTJ_DT` desc limit ?,?