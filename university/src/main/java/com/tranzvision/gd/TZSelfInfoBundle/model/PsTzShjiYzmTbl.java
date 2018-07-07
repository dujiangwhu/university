package com.tranzvision.gd.TZSelfInfoBundle.model;

import java.util.Date;

public class PsTzShjiYzmTbl extends PsTzShjiYzmTblKey {
    private String tzSjyzm;

    private Date tzYzmYxq;

    private String tzEffFlag;

    private String tzSiteiId;

    public String getTzSjyzm() {
        return tzSjyzm;
    }

    public void setTzSjyzm(String tzSjyzm) {
        this.tzSjyzm = tzSjyzm == null ? null : tzSjyzm.trim();
    }

    public Date getTzYzmYxq() {
        return tzYzmYxq;
    }

    public void setTzYzmYxq(Date tzYzmYxq) {
        this.tzYzmYxq = tzYzmYxq;
    }

    public String getTzEffFlag() {
        return tzEffFlag;
    }

    public void setTzEffFlag(String tzEffFlag) {
        this.tzEffFlag = tzEffFlag == null ? null : tzEffFlag.trim();
    }

    public String getTzSiteiId() {
        return tzSiteiId;
    }

    public void setTzSiteiId(String tzSiteiId) {
        this.tzSiteiId = tzSiteiId == null ? null : tzSiteiId.trim();
    }
}