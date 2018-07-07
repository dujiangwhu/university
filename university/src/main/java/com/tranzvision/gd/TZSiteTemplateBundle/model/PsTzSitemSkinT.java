package com.tranzvision.gd.TZSiteTemplateBundle.model;

public class PsTzSitemSkinT extends PsTzSitemSkinTKey {
    private String tzSkinState;

    private String tzSkinName;

    public String getTzSkinState() {
        return tzSkinState;
    }

    public void setTzSkinState(String tzSkinState) {
        this.tzSkinState = tzSkinState == null ? null : tzSkinState.trim();
    }

    public String getTzSkinName() {
        return tzSkinName;
    }

    public void setTzSkinName(String tzSkinName) {
        this.tzSkinName = tzSkinName == null ? null : tzSkinName.trim();
    }
}