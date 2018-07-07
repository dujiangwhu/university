package com.tranzvision.gd.TZOrganizationMgBundle.model;

public class PsTzJgBaseTWithBLOBs extends PsTzJgBaseT {
    private String tzJgLoginCopr;

    private String tzJgIntro;

    public String getTzJgLoginCopr() {
        return tzJgLoginCopr;
    }

    public void setTzJgLoginCopr(String tzJgLoginCopr) {
        this.tzJgLoginCopr = tzJgLoginCopr == null ? null : tzJgLoginCopr.trim();
    }

    public String getTzJgIntro() {
        return tzJgIntro;
    }

    public void setTzJgIntro(String tzJgIntro) {
        this.tzJgIntro = tzJgIntro == null ? null : tzJgIntro.trim();
    }
}