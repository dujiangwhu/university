package com.tranzvision.gd.TZMbaPwClpsBundle.model;

public class PsTzPwExtT extends PsTzPwExtTKey {
    private String tzCsPassword;

    private String tzYnyw;

    public String getTzCsPassword() {
        return tzCsPassword;
    }

    public void setTzCsPassword(String tzCsPassword) {
        this.tzCsPassword = tzCsPassword == null ? null : tzCsPassword.trim();
    }

    public String getTzYnyw() {
        return tzYnyw;
    }

    public void setTzYnyw(String tzYnyw) {
        this.tzYnyw = tzYnyw == null ? null : tzYnyw.trim();
    }
}