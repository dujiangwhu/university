package com.tranzvision.gd.TZPXBundle.model;

public class PxJgKsOrderTKey {
    private String tzJgId;

    private String tzOrderId;

    public String getTzJgId() {
        return tzJgId;
    }

    public void setTzJgId(String tzJgId) {
        this.tzJgId = tzJgId == null ? null : tzJgId.trim();
    }

    public String getTzOrderId() {
        return tzOrderId;
    }

    public void setTzOrderId(String tzOrderId) {
        this.tzOrderId = tzOrderId == null ? null : tzOrderId.trim();
    }
}