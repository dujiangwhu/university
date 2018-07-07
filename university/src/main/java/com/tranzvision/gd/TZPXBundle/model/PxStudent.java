package com.tranzvision.gd.TZPXBundle.model;

public class PxStudent {
    private String oprid;

    private String tzJgId;

    private String photoSysfilename;

    private String tzFilePath;

    private String sex;

    private Integer age;

    private String qq;

    private String phone;

    private String email;

    private String contact;

    private String contactPhone;

    private String contactAddress;

    private Integer timecardRemaind;

    private String stuStatus;

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

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq == null ? null : qq.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact == null ? null : contact.trim();
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone == null ? null : contactPhone.trim();
    }

    public String getContactAddress() {
        return contactAddress;
    }

    public void setContactAddress(String contactAddress) {
        this.contactAddress = contactAddress == null ? null : contactAddress.trim();
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
}