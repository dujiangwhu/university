package com.tranzvision.gd.TZApplicationVerifiedBundle.model;

public class PsTzBmbDceT {
    private String runCntlId;

    private String tzAppTplId;

    private String tzExportTmpId;

    private String tzExcelName;

    private String tzDbType;

    private String tzRelUrl;

    private String tzJdUrl;

    private String tzAudList;

    public String getRunCntlId() {
        return runCntlId;
    }

    public void setRunCntlId(String runCntlId) {
        this.runCntlId = runCntlId == null ? null : runCntlId.trim();
    }

    public String getTzAppTplId() {
        return tzAppTplId;
    }

    public void setTzAppTplId(String tzAppTplId) {
        this.tzAppTplId = tzAppTplId == null ? null : tzAppTplId.trim();
    }

    public String getTzExportTmpId() {
        return tzExportTmpId;
    }

    public void setTzExportTmpId(String tzExportTmpId) {
        this.tzExportTmpId = tzExportTmpId == null ? null : tzExportTmpId.trim();
    }

    public String getTzExcelName() {
        return tzExcelName;
    }

    public void setTzExcelName(String tzExcelName) {
        this.tzExcelName = tzExcelName == null ? null : tzExcelName.trim();
    }

    public String getTzDbType() {
        return tzDbType;
    }

    public void setTzDbType(String tzDbType) {
        this.tzDbType = tzDbType == null ? null : tzDbType.trim();
        
    }

    public String getTzRelUrl() {
        return tzRelUrl;
    }

    public void setTzRelUrl(String tzRelUrl) {
        this.tzRelUrl = tzRelUrl == null ? null : tzRelUrl.trim();
    }

    public String getTzJdUrl() {
        return tzJdUrl;
    }

    public void setTzJdUrl(String tzJdUrl) {
        this.tzJdUrl = tzJdUrl == null ? null : tzJdUrl.trim();
    }

    public String getTzAudList() {
        return tzAudList;
    }

    public void setTzAudList(String tzAudList) {
        this.tzAudList = tzAudList == null ? null : tzAudList.trim();
    }
}