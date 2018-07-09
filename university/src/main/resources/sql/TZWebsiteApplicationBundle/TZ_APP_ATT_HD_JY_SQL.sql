SELECT COUNT(1) 
  FROM PS_TZ_TEMP_FIELD_T A 
 WHERE EXISTS ( 
 SELECT * 
  FROM PS_TZ_APP_HIDDEN_T X 
 WHERE X.TZ_APP_INS_ID = ? 
   AND A.TZ_XXX_BH = X.TZ_XXX_BH 
   AND X.TZ_IS_HIDDEN <> 'Y') 
   AND NOT EXISTS ( 
 SELECT * 
  FROM PS_TZ_FORM_ATT_T X 
 WHERE X.TZ_APP_INS_ID = ? 
   AND A.TZ_XXX_BH = X.TZ_XXX_BH) 
   AND A.TZ_APP_TPL_ID = ? 
   AND A.TZ_XXX_NO = ?