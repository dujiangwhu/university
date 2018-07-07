package com.tranzvision.gd.TZPXBundle.model;

import java.util.Date;

public class PxStudentT {
    private String oprid;

    private String tzJgId;

    private String photoSysfilename;

    private String tzFilePath;

    private String sex;

    private Integer age;

    private String contact;

    private String contactQq;

    private String contactPhone;

    private String contactEmail;

    private String emergentContact;

    private String emergentContactPhone;

    private String emergentContactEmail;

    private Integer timecardRemaind;

    private String stuStatus;

    private Date rowLastmantDttm;

    private String rowLastmantOprid;

    public String getOprid() {
        return oprid;
    }

    public void setOprid(String oprid) {
        this.oprid = oprid == null ? null : oprid.trim();
    }

    public String getTzJgId() {
        return tzJgId;
    }

    public void setTzJgId(String tzJgId) {
        this.tzJgId = tzJgId == null ? null : tzJgId.trim();
    }

    public String getPhotoSysfilename() {
        return photoSysfilename;
    }

    public void setPhotoSysfilename(String photoSysfilename) {
        this.photoSysfilename = photoSysfilename == null ? null : photoSysfilename.trim();
    }

    public String getTzFilePath() {
        return tzFilePath;
    }

    public void setTzFilePath(String tzFilePath) {
        this.tzFilePath = tzFilePath == null ? null : tzFilePath.trim();
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex == null ? null : sex.trim();
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact == null ? null : contact.trim();
    }

    public String getContactQq() {
        return contactQq;
    }

    public void setContactQq(String contactQq) {
        this.contactQq = contactQq == null ? null : contactQq.trim();
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone == null ? null : contactPhone.trim();
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail == null ? null : contactEmail.trim();
    }

    public String getEmergentContact() {
        return emergentContact;
    }

    public void setEmergentContact(String emergentContact) {
        this.emergentContact = emergentContact == null ? null : emergentContact.trim();
    }

    public String getEmergentContactPhone() {
        return emergentContactPhone;
    }

    public void setEmergentContactPhone(String emergentContactPhone) {
        this.emergentContactPhone = emergentContactPhone == null ? null : emergentContactPhone.trim();
    }

    public String getEmergentContactEmail() {
        return emergentContactEmail;
    }

    public void setEmergentContactEmail(String emergentContactEmail) {
        this.emergentContactEmail = emergentContactEmail == null ? null : emergentContactEmail.trim();
    }

    public Integer getTimecardRemaind() {
        return timecardRemaind;
    }

    public void setTimecardRemaind(Integer timecardRemaind) {
        this.timecardRemaind = timecardRemaind;
    }

    public String getStuStatus() {
        return stuStatus;
    }

    public void setStuStatus(String stuStatus) {
        this.stuStatus = stuStatus == null ? null : stuStatus.trim();
    }

    public Date getRowLastmantDttm() {
        return rowLastmantDttm;
    }

    public void setRowLastmantDttm(Date rowLastmantDttm) {
        this.rowLastmantDttm = rowLastmantDttm;
    }

    public String getRowLastmantOprid() {
        return rowLastmantOprid;
    }

    public void setRowLastmantOprid(String rowLastmantOprid) {
        this.rowLastmantOprid = rowLastmantOprid == null ? null : rowLastmantOprid.trim();
    }
}