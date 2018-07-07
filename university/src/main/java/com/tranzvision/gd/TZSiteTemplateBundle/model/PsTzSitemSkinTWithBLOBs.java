package com.tranzvision.gd.TZSiteTemplateBundle.model;

public class PsTzSitemSkinTWithBLOBs extends PsTzSitemSkinT {
    private String tzSkinCode;

    private String tzSkinMcode;

    public String getTzSkinCode() {
        return tzSkinCode;
    }

    public void setTzSkinCode(String tzSkinCode) {
        this.tzSkinCode = tzSkinCode == null ? null : tzSkinCode.trim();
    }

    public String getTzSkinMcode() {
        return tzSkinMcode;
    }

    public void setTzSkinMcode(String tzSkinMcode) {
        this.tzSkinMcode = tzSkinMcode == null ? null : tzSkinMcode.trim();
    }
}