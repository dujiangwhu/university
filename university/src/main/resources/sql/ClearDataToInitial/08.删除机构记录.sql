/*机构管理成员信息表*/
delete from PS_TZ_JG_MGR_T where TZ_JG_ID in (select TZ_JG_ID from PS_TZ_JG_BASE_T where TZ_JG_ID<>'ADMIN');

/*机构管理角色信息表*/
delete from PS_TZ_JG_ROLE_T where TZ_JG_ID in (select TZ_JG_ID from PS_TZ_JG_BASE_T where TZ_JG_ID<>'ADMIN');

/*机构基本信息表*/
delete from PS_TZ_JG_BASE_T where TZ_JG_ID<>'ADMIN';
