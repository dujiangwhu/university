package com.tranzvision.gd.TZPXBundle.model;

public class PxCourseAnnexTKey {
    private String tzCourseId;

    private Integer tzPkskXh;

    public String getTzCourseId() {
        return tzCourseId;
    }

    public void setTzCourseId(String tzCourseId) {
        this.tzCourseId = tzCourseId == null ? null : tzCourseId.trim();
    }

    public Integer getTzPkskXh() {
        return tzPkskXh;
    }

    public void setTzPkskXh(Integer tzPkskXh) {
        this.tzPkskXh = tzPkskXh;
    }
}